# widget
widget and Base class

# Add code in AndroidManifest
`````
 <provider
            android:name="android.support.v4.content.FileProvider"
            android:authorities="${applicationId}.provider"
            android:exported="false"
            android:grantUriPermissions="true">
            <meta-data
                android:name="android.support.FILE_PROVIDER_PATHS"
                android:resource="@xml/provider_paths" />
        </provider>
        
       
Add code in xml/provider_paths file

<?xml version="1.0" encoding="utf-8"?>
<paths xmlns:android="http://schemas.android.com/apk/res/android">
    <external-path name="images" path="."/>
</paths>
 `````       
# Add code in SigningConfigs
`````        
 signingConfigs {
        release {
            storeFile file(rootProject.file('/keystore.jks'))
            storePassword ""
            keyAlias ""
            keyPassword ""
        }
    }
    buildTypes {
        release {
            minifyEnabled true
            useProguard true
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
            signingConfig signingConfigs.release
        }
    }
    vectorDrawables.useSupportLibrary = true
