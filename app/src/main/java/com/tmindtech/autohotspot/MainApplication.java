package com.tmindtech.autohotspot;

import android.app.Application;
import android.content.Intent;

/**
 * @author RexQian
 * @date 2020-03-06
 */
public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initFrontService();
    }

    public void initFrontService() {
        FrontServiceInfo info = new FrontServiceInfo(MainActivity.class, R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,
            getString(R.string.app_name),
            getString(R.string.app_is_running),
            android.R.drawable.ic_menu_close_clear_cancel,
            getString(R.string.close_service));

        NotifyService.startBackground(this, info, new NotifyService.BackgroundListener() {
            @Override
            public void closeApplication() {
                stopService(new Intent(getApplicationContext(), NotifyService.class));
                System.exit(0);
            }
        });
    }
}
