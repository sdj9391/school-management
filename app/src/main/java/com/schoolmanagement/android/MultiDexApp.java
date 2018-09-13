package com.schoolmanagement.android;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.schoolmanagement.BuildConfig;
import com.schoolmanagement.android.utils.AppConfig;
import com.schoolmanagement.android.utils.AppUtils;
import com.schoolmanagement.android.utils.ApplicationPreferences;
import com.schoolmanagement.android.utils.DebugLog;

public class MultiDexApp extends MultiDexApplication {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ApplicationPreferences.init(getApplicationContext());

        initImageLoader(this);
        // initAppConfig();

        // disable logging of universal image loader
        // L.writeLogs(false);

        // To enable Crashlytics debugger manually
        /*final Fabric fabric = new Fabric.Builder(this)
                .kits(new Crashlytics())
                .debuggable(true)           // Enables Crashlytics debugger
                .build();
        Fabric.with(fabric);*/

        /*if (!BuildConfig.DEBUG) {
            FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(this);

            if (firebaseAnalytics != null) {
                firebaseAnalytics.setAnalyticsCollectionEnabled(false);
                firebaseAnalytics.setSessionTimeoutDuration(TimeUnit.MINUTES.toMillis(3));

                // Radiologist radiologist = ApplicationPreferences.get().getRadiologistDetails();
                Radiologist radiologist = new DoctorsAccountManager(getApplicationContext()).getUserDetails();
                if (radiologist != null) {
                    firebaseAnalytics.setUserProperty("name", radiologist.getName());
                    firebaseAnalytics.setUserProperty("specialization", radiologist.getDoctorSpecialization());
                    firebaseAnalytics.setUserProperty("number", radiologist.getMobileNumber());
                    firebaseAnalytics.setUserProperty("is_verified", Boolean.toString(radiologist.isVerified()));
                }
            }
        }*/
    }

    public static void initImageLoader(Context context) {

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();

        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        // ImageLoaderConfiguration.createDefault(this); method.
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.threadPoolSize(10);

        //config.diskCache(new UnlimitedDiskCache(Environment.getDownloadCacheDirectory()));
        //config.diskCache(new LruDiskCache(Environment.getDownloadCacheDirectory(),new Md5FileNameGenerator(),50 * 1024 * 1024));

        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.defaultDisplayImageOptions(defaultOptions);

        if (BuildConfig.DEBUG) {
            config.writeDebugLogs(); // Remove for release app
        }

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }


    /*public void initAppConfig() {
        AppConfig.getInstance().setServerUrl(BuildConfig.SERVER_URL);

        User user = ApplicationPreferences.get().getCurrentUser();
        if (user != null) {
            String token = user.getToken();

            DebugLog.d("token: " + token);
            if (AppUtils.isEmpty(token)) {
                DebugLog.e("User is logged in. However, token is null. Token: " + token);
            } else {
                DebugLog.v("User is logged in. token found");
                AppConfig.getInstance().setToken(token);
            }
        } else {
            DebugLog.w("Token not found found");
            AppConfig.getInstance().setToken(null);
        }
    }*/
}
