package com.altimate.utils;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;

import com.altimate.persistentdata.AltimatePrefs;
import com.altimate.weather.events.LocationUpdateEvent;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.lang.ref.WeakReference;

import de.greenrobot.event.EventBus;

/**
 * Created by brendanweinstein on 5/16/15.
 */
public class LocationService {

  private static final String TAG = LocationService.class.getSimpleName();

  /** Defined in milliseconds, set to update every 30 minutes */
  private static final int LOCATION_REQUEST_INTERVAL = 30 * 60 * 1000;

  private static WeakReference<Context> mContextRef;
  private static LocationService INSTANCE = new LocationService();
  private static GoogleApiClient mGoogleApiClient;

  private LocationService() {
  }

  public void init(Context context) {
    mContextRef = new WeakReference<>(context);
  }

  public static LocationService getInstance() {
    return INSTANCE;
  }


  public void startListeningForLocation() {
    Context context = mContextRef.get();
    mGoogleApiClient = new GoogleApiClient.Builder(context)
            .addConnectionCallbacks(connectionListener)
            .addOnConnectionFailedListener(connectionFailedListener)
            .addApi(com.google.android.gms.location.LocationServices.API)
            .build();
    mGoogleApiClient.connect();
  }

  static GoogleApiClient.ConnectionCallbacks connectionListener = new GoogleApiClient.ConnectionCallbacks() {
    @Override
    public void onConnected(Bundle bundle) {
      Location lastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(
              mGoogleApiClient);
      EventBus.getDefault().post(new LocationUpdateEvent(lastKnownLocation));
      AltimatePrefs.setLocation(mContextRef.get(), lastKnownLocation);
      LocationRequest locationRequest = new LocationRequest();
      locationRequest.setInterval(10000);
      locationRequest.setFastestInterval(5000);
      locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
      LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, locationListener);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }
  };

  static GoogleApiClient.OnConnectionFailedListener connectionFailedListener = new GoogleApiClient.OnConnectionFailedListener() {
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
  };

  static LocationListener locationListener = new LocationListener() {
    @Override
    public void onLocationChanged(Location location) {
      EventBus.getDefault().post(new LocationUpdateEvent(location));
      AltimatePrefs.setLocation(mContextRef.get(), location);
    }
  };

}
