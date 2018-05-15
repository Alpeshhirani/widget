public class BaseActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_GALLERY = 1;
    public static final int REQUEST_CODE_TAKE_PICTURE = 2;
    public static final String TEMP_PHOTO_FILE_NAME = "temp_photo.jpg";
    public static File mFileTemp;
    public static boolean isChatActivityAlreadyOpened = false;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    public RequestAPI requestAPI;
    public Toolbar toolbar;
    public TextView txtTitle;

    public Preferences pref;
    public MarshMallowPermission marshMallowPermission = new MarshMallowPermission(getActivity());
    AlertDialog dialog;
    Uri mImageCaptureUri = null;
    //    AsyncProgressDialog ad;
    private Toast toast;
    private DrawerLayout drawer;
    private ImageView imgRightIcon;

//    private Callback<CommonResponse> logoutResponseCallback = new Callback<CommonResponse>() {
//        @Override
//        public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
//            dismissProgress();
//            if (response.isSuccessful()) {
//                if (response.body().getReturnCode().equals("1")) {
//                    pref.clear();
//                    Intent intent = new Intent(getActivity(), LoginActivity.class);
//                    startActivity(intent);
//                    getActivity().finishAffinity();
//                } else
//                    showDialog("", response.body().getReturnMsg(), response.body().getReturnCode());
//            } else {
//                log.LOGE("Code: " + response.code() + " Message: " + response.message());
//            }
//        }
//
//        @Override
//        public void onFailure(Call<CommonResponse> call, Throwable t) {
//            t.printStackTrace();
//            dismissProgress();
//        }
//    };

    public static void copyStream(InputStream input, OutputStream output) throws IOException {

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }

    public static NetworkInfo getNetworkInfo(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }

    public void showDialog() {
        View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.progress_dialog, null);
        dialog = new AlertDialog.Builder(getActivity())
                .setView(v)
                .create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);
        RotateLoading rotateLoading = v.findViewById(R.id.rotateloading);
        rotateLoading.start();
        dialog.show();

    }

    public void dismissDialog() {
        dialog.dismiss();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toast = Toast.makeText(getActivity(), "", Toast.LENGTH_LONG);
        requestAPI = ApiClient.getClient().create(RequestAPI.class);
        pref = new Preferences(getActivity());

    }


    public void initState() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            mFileTemp = new File(Environment.getExternalStorageDirectory(), TEMP_PHOTO_FILE_NAME);
        } else {
            mFileTemp = new File(getFilesDir(), TEMP_PHOTO_FILE_NAME);
        }
    }

    public void setRightIcon(int drawable) {
        imgRightIcon.setImageResource(drawable);
    }

    public void showToolBar(boolean isBack, String title) {
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        try {
            txtTitle = toolbar.findViewById(R.id.txtTitle);
            txtTitle.setText(title);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (isBack) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            final Drawable upArrow = getResources().getDrawable((R.drawable.ic_arrow_back));
//            upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
            toolbar.setNavigationIcon(upArrow);
        } else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
        }

    }

//    private void applyFontToMenuItem(MenuItem mi) {
//        Typeface font = FontUtils.fontName(getActivity(), 1);
//        SpannableString mNewTitle = new SpannableString(mi.getTitle());
//        mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//        mi.setTitle(mNewTitle);
//    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public String formatDeciPoint(double value) {
        DecimalFormat formatVal = new DecimalFormat("##.##");
        return formatVal.format(value);
    }

    public void hideKeyboard() {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
        }
    }

    public void showToast(final int text, final boolean isShort) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                toast.setText(getString(text).toString());
                toast.setDuration(isShort ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }

    public void showToast(final String text, final boolean isShort) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                toast.setText(text);
                toast.setDuration(isShort ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }
    
    public BaseActivity getActivity() {
        return this;
    }


    public void noDataFound(DTextView dTextView, ListView view) {
        dTextView.setVisibility(View.VISIBLE);
        view.setVisibility(View.GONE);
    }

    public void takePicture() {
        initState();
        if (!marshMallowPermission.checkPermissionForCamera()) {
            marshMallowPermission.requestPermissionForCamera();
        } else {
            if (!marshMallowPermission.checkPermissionForWriteexternal()) {
                marshMallowPermission.requestPermissionForWriteexternal();
            } else {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {

                    String state = Environment.getExternalStorageState();

                    if (Environment.MEDIA_MOUNTED.equals(state)) {
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                            mImageCaptureUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID +
                                            ".provider",
                                    mFileTemp);
                        } else {
                            mImageCaptureUri = Uri.fromFile(mFileTemp);
                        }
                    } else {
                        mImageCaptureUri = InternalStorageContentProvider.CONTENT_URI;
                    }

                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
                    intent.putExtra("return-data", true);
                    startActivityForResult(intent, REQUEST_CODE_TAKE_PICTURE);

                } catch (ActivityNotFoundException e) {

                }
            }
        }
    }

    public void openGallery() {
        if (!marshMallowPermission.checkPermissionForReadexternal()) {
            marshMallowPermission.requestPermissionForReadexternal();
        } else {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, REQUEST_CODE_GALLERY);
        }
    }

//    public void checkResponseCode(String code) {
//        if (code.equals("21")) {
//            pref.clear();
//            Intent intent = new Intent(getActivity(), LoginActivity.class);
//            startActivity(intent);
//            getActivity().finishAffinity();
//        }
//    }

//    public void showDialog(String title, String msg, final String returnCode) {
//
//        if (title.isEmpty())
//            title = getString(R.string.app_name);
//        if (returnCode.equals("21"))
//            msg = getString(R.string.session_closed);
//        AlertDialog builder = new AlertDialog.Builder(getActivity())
//                .setTitle(title)
//                .setMessage(msg)
//                .setPositiveButton(R.string.hint_ok, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        if (returnCode.equals("21"))
//                            checkResponseCode(returnCode);
//                        dialog.dismiss();
//
//                    }
//                }).create();
//
//        builder.show();
//    }

    public void showNoInternetDialog() {

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.title_no_internet))
                .setMessage(getString(R.string.msg_no_internet))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.hint_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finishAffinity();
                    }
                }).create();
        dialog.show();
    }

    public Fragment getCurrentFragment(int containerId) {
        return getSupportFragmentManager().findFragmentById(containerId);
    }

    public void showSettingAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(BaseActivity.this);
        alertDialog.setTitle("GPS");
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        alertDialog.show();
    }

    public String getDistance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return formatDeciPoint(dist);
    }

    public boolean checkConnection() {
        NetworkInfo info = getNetworkInfo(this);
        return (info != null && info.isConnected());
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
}
