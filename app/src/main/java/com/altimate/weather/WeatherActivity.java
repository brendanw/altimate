package com.altimate.weather;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.altimate.R;
import com.altimate.utils.ToastUtils;
import com.altimate.weather.api.WeatherActions;
import com.altimate.weather.api.WeatherResponseWrapper;
import com.altimate.weather.events.WeatherResponseFailure;
import com.altimate.weather.events.WeatherResponseSuccess;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by brendanweinstein on 5/16/15.
 */
public class WeatherActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

  private static final String TAG = WeatherActivity.class.getSimpleName();

  private TextView mWeatherInSFTextView;

  private GoogleApiClient mGoogleApiClient;
  private Location mLastLocation;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.weather);
    mWeatherInSFTextView = (TextView) findViewById(R.id.temperature_in_sf);

    mGoogleApiClient = new GoogleApiClient.Builder(this)
            .addConnectionCallbacks(WeatherActivity.this)
            .addOnConnectionFailedListener(WeatherActivity.this)
            .addApi(LocationServices.API)
            .build();
    mGoogleApiClient.connect();
  }

  @Override
  public void onResume() {
    super.onResume();
    EventBus.getDefault().register(WeatherActivity.this);
  }

  @Override
  public void onPause() {
    super.onPause();
    EventBus.getDefault().unregister(WeatherActivity.this);
  }

  public void onEvent(WeatherResponseSuccess success) {
    WeatherResponseWrapper weatherResponseWrapper = success.getWeatherResponseWrapper();
    String tempInDegrees = weatherResponseWrapper.getMainWrapper().getTemp().toString();
    mWeatherInSFTextView.setText("It is " + tempInDegrees + " degrees in SF");
  }

  public void onEvent(WeatherResponseFailure failure) {
    Toast.makeText(WeatherActivity.this, "Unsuccessful request", Toast.LENGTH_LONG)
            .show();
  }

  /**
   * Inner static classes do not hold reference to the class they are within, so
   * there is no risk of leaking WeatherActivity if weatherCallback exists longer
   */
  static Callback<WeatherResponseWrapper> weatherCallBack = new Callback<WeatherResponseWrapper>() {
    @Override
    public void success(WeatherResponseWrapper weatherResponseWrapper, Response response) {
      WeatherResponseSuccess successEvent = new WeatherResponseSuccess(weatherResponseWrapper);
      EventBus.getDefault().post(successEvent);
    }

    @Override
    public void failure(RetrofitError error) {
      WeatherResponseFailure failureEvent = new WeatherResponseFailure();
      EventBus.getDefault().post(failureEvent);
    }
  };

  @Override
  public void onConnected(Bundle bundle) {
    mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
            mGoogleApiClient);
    if(mLastLocation != null) {
      String units = "imperial";
      Double latitude = mLastLocation.getLatitude();
      Double longitude = mLastLocation.getLongitude();
      Geocoder geoCoder = new Geocoder(WeatherActivity.this, Locale.getDefault());
      List<Address> addressList = null;
      try {
        addressList = geoCoder.getFromLocation(latitude, longitude, 1);
      } catch (IOException e) {
        e.printStackTrace();
      }
      if(addressList != null) {
        Address address = addressList.get(0);
        if(address != null) {
          ToastUtils.showLongToast(WeatherActivity.this, "we are in " + address.getLocality());
        }
      }
      WeatherActions.getWeather(units, latitude.toString(), longitude.toString(), weatherCallBack);
    }
  }

  @Override
  public void onConnectionSuspended(int i) {
    /** TODO: Not sure when this happens */
  }

  @Override
  public void onConnectionFailed(ConnectionResult connectionResult) {
    /** User does not have Google Play Services enabled */
  }

}
