package com.altimate.weather;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.altimate.R;
import com.altimate.weather.api.WeatherActions;
import com.altimate.weather.api.WeatherResponseWrapper;
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

  Callback<WeatherResponseWrapper> weatherCallBack = new Callback<WeatherResponseWrapper>() {
    @Override
    public void success(WeatherResponseWrapper weatherResponseWrapper, Response response) {
      Toast.makeText(WeatherActivity.this, "Successful request", Toast.LENGTH_LONG)
              .show();
      String tempInDegrees = weatherResponseWrapper.getMainWrapper().getTemp().toString();
      mWeatherInSFTextView.setText("It is " + tempInDegrees + " degrees in SF");
    }

    @Override
    public void failure(RetrofitError error) {
      Toast.makeText(WeatherActivity.this, "Unsuccessful request", Toast.LENGTH_LONG)
              .show();
    }
  };

  @Override
  public void onDestroy() {
    super.onDestroy();
  }


}
