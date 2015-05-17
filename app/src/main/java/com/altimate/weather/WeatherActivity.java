package com.altimate.weather;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.altimate.R;
import com.altimate.weather.api.WeatherActions;
import com.altimate.weather.api.WeatherResponseWrapper;
import com.altimate.weather.events.WeatherResponseFailure;
import com.altimate.weather.events.WeatherResponseSuccess;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by brendanweinstein on 5/16/15.
 */
public class WeatherActivity extends Activity {

  private static final String TAG = WeatherActivity.class.getSimpleName();

  private TextView mWeatherInSFTextView;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.weather);
    mWeatherInSFTextView = (TextView) findViewById(R.id.temperature_in_sf);

    String query = "San Francisco, CA";
    String units = "imperial";
    WeatherActions.getWeather(query, units, weatherCallBack);
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
    Toast.makeText(WeatherActivity.this, "Successful request", Toast.LENGTH_LONG)
            .show();
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


}
