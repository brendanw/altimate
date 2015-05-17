package com.altimate;

import android.app.Application;
import android.content.Context;

import com.altimate.utils.LocationService;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by brendanweinstein on 5/16/15.
 */
public class AltimateApp extends Application {

  public static RefWatcher getRefWatcher(Context context) {
    AltimateApp application = (AltimateApp) context.getApplicationContext();
    return application.refWatcher;
  }

  private RefWatcher refWatcher;

  @Override
  public void onCreate() {
    super.onCreate();
    refWatcher = LeakCanary.install(this);

    /** Retrieve location immediately */
    LocationService.getInstance().init(AltimateApp.this);
    LocationService.getInstance().startListeningForLocation();
  }

}
