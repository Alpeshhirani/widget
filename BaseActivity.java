package com.hirani.activity;

import android.support.v7.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hirani.R;
import com.hirani.retrofit.ApiClient;
import com.hirani.retrofit.RequestAPI;
import com.hirani.utils.Constants;
import com.hirani.utils.GsonUtils;
import com.hirani.utils.MarshMallowPermission;
import com.hirani.utils.Preferences;
import com.hirani.widget.DTextView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;


public class BaseActivity extends AppCompatActivity {

    AlertDialog dialog;

    public static final int REQUEST_CODE_GALLERY = 0x1;
    public static final int REQUEST_CODE_TAKE_PICTURE = 0x2;
    public static final String TEMP_PHOTO_FILE_NAME = "temp_photo.jpg";
    public static File mFileTemp;
    public static boolean isChatActivityAlreadyOpened = false;
    public RequestAPI requestAPI;
    public Toolbar toolbar;
    public TextView txtTitle;

    public Preferences pref;
    public GsonUtils gsonUtils;
    public MarshMallowPermission marshMallowPermission = new MarshMallowPermission(getActivity());
    //    AsyncProgressDialog ad;
    private Toast toast;
    private DrawerLayout drawer;
    private ImageView imgRightIcon;
    private BottomSheetBehavior mBehavior;
    private BottomSheetDialog mBottomSheetDialog;

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

    public void showDialog() {
        View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.progress_dialog, null);
        dialog = new AlertDialog.Builder(getActivity())
                .setView(v)
                .create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);
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
//        log = new LogUtils(this.getClass());
        pref = new Preferences(getActivity());
        gsonUtils = GsonUtils.getInstance();
    }

    public void initBottomSheet(View mBottomSheet) {
        mBehavior = BottomSheetBehavior.from(mBottomSheet);
    }

    public void initState() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            mFileTemp = new File(Environment.getExternalStorageDirectory(), TEMP_PHOTO_FILE_NAME);
        } else {
            mFileTemp = new File(getFilesDir(), TEMP_PHOTO_FILE_NAME);
        }
    }

//    @SuppressLint("InflateParams")
//    public void showBottomSheetDialog() {
//        if (mBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
//            mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//        }
//
//        mBottomSheetDialog = new BottomSheetDialog(this);
//        View view = getLayoutInflater().inflate(R.layout.sheet, null);
//        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(new ItemAdapter(getActivity(), createItems(), new ItemAdapter.ItemListener() {
//            @Override
//            public void onItemClick(Item item) {
//
//                if (item.getTitle().equals("Camera")) {
//                    takePicture();
//                } else if (item.getTitle().equals("Gallery")) {
//                    openGallery();
//                }
//                if (mBottomSheetDialog != null) {
//                    mBottomSheetDialog.dismiss();
//                    mBottomSheetDialog = null;
//                }
//            }
//        }));
//
//        mBottomSheetDialog.setContentView(view);
//        mBottomSheetDialog.show();
//        mBottomSheetDialog.setCancelable(false);
//    }

//    public List<Item> createItems() {
//
//        ArrayList<Item> items = new ArrayList<>();
//        items.add(new Item(R.drawable.ic_add_a_photo_black_24dp, "Camera"));
//        items.add(new Item(R.drawable.ic_photo_black_24dp, "Gallery"));
//        items.add(new Item(R.drawable.ic_close_black_24dp, "Cancel"));
//
//        return items;
//    }

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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (isBack) {
            final Drawable upArrow = getResources().getDrawable((R.drawable.ic_back_arrow));
//            upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
            toolbar.setNavigationIcon(upArrow);
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

//    private void applyFontToMenuItem(MenuItem mi) {
//        Typeface font = FontUtils.fontName(getActivity(), 1);
//        SpannableString mNewTitle = new SpannableString(mi.getTitle());
//        mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//        mi.setTitle(mNewTitle);
//    }

    public boolean isLoggedIn() {
        return pref.getBoolean(Constants.IS_LOGIN, false);
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

//    public void confirmLogout() {
//        DialogFactory.alertDialog(getActivity(), getString(R.string.logout_title), getString(R.string.logout_msg), getString(R.string.hint_logout), getString(R.string.cancel), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//                showProgress();
//                LogoutRequest request = new LogoutRequest();
//                request.setAPIKey(Constants.API_KEY);
//                request.setToken(Utils.getLoginDetail(getActivity()).getReturnValue());
//                requestAPI.postLogoutRequest(request).enqueue(logoutResponseCallback);
//
//            }
//        }, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//
//
//    }

    public void noDataFound(DTextView dTextView, ListView view) {
        dTextView.setVisibility(View.VISIBLE);
        view.setVisibility(View.GONE);
    }
//
//    public void showProgress() {
//        try {
//            if (ad != null && ad.isShowing()) {
//                return;
//            }
//
//            ad = AsyncProgressDialog.getInstant(getActivity());
//            ad.show();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    public void dismissProgress() {
//        try {
//            if (ad != null) {
//                ad.dismiss();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    public void takePicture() {
//        if (!marshMallowPermission.checkPermissionForCamera()) {
//            marshMallowPermission.requestPermissionForCamera();
//        } else {
//            if (!marshMallowPermission.checkPermissionForWriteExternalStorage()) {
//                marshMallowPermission.requestPermissionForWriteExternalStorage();
//            } else {
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                try {
//                    Uri mImageCaptureUri = null;
//                    String state = Environment.getExternalStorageState();
//                    if (Environment.MEDIA_MOUNTED.equals(state)) {
//                        mImageCaptureUri = Uri.fromFile(mFileTemp);
//                    } else {
//                /*
//                 * The solution is taken from here:
//				 * http://stackoverflow.com/questions
//				 * /10042695/how-to-get-camera-result-as-a-uri-in-data-folder
//				 */
//                        mImageCaptureUri = InternalStorageContentProvider.CONTENT_URI;
//                    }
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
//                    intent.putExtra("return-data", true);
//                    startActivityForResult(intent, REQUEST_CODE_TAKE_PICTURE);
//                } catch (ActivityNotFoundException e) {
//                    log.LOGE("cannot take picture", e);
//                }
//            }
//        }
//    }

//    public void openGallery() {
//        if (!marshMallowPermission.checkPermissionForReadExternalStorage()) {
//            marshMallowPermission.requestPermissionForReadExternalStorage();
//        } else {
//            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
//            photoPickerIntent.setType("image/*");
//            startActivityForResult(photoPickerIntent, REQUEST_CODE_GALLERY);
//        }
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


    public Fragment getCurrentFragment(int containerId) {
        return getSupportFragmentManager().findFragmentById(containerId);
    }

    public void showSettingAlert() {
        android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(BaseActivity.this);
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

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
}
