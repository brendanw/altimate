package altimate.com.altimate.weather;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import altimate.com.altimate.R;
import altimate.com.altimate.api.Switchboard;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.GET;

/**
 * Created by brendanweinstein on 5/16/15.
 */
public class WeatherActivity extends Activity {

  private static final String TAG = WeatherActivity.class.getSimpleName();

  private TextView mWeatherInSfTextView;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.weather);
    mWeatherInSfTextView = (TextView) findViewById(R.id.temperature_in_sf);

    String openWeatherEndpoint = Switchboard.getInstance().getOpenWeatherEndPoint();
    RestAdapter restAdapter = new RestAdapter.Builder()
            .setEndpoint(openWeatherEndpoint)
            .setLogLevel(RestAdapter.LogLevel.FULL)
            .build();

    OpenWeatherService weatherService = restAdapter.create(OpenWeatherService.class);

    Callback<WeatherResponseWrapper> weatherCallBack = new Callback<WeatherResponseWrapper>() {
      @Override
      public void success(WeatherResponseWrapper weatherResponseWrapper, Response response) {
        Log.d(TAG, "Sucessfully received WeatherResponse");
        Toast.makeText(WeatherActivity.this, "Successful request", Toast.LENGTH_LONG)
                .show();
        String tempInDegrees = weatherResponseWrapper.main.temp.toString();
        mWeatherInSfTextView.setText("It is "+tempInDegrees+" degrees in SF");
      }

      @Override
      public void failure(RetrofitError error) {
        Log.w(TAG, "Did not successfully receive WeatherResponse");
        Toast.makeText(WeatherActivity.this, "Unsuccessful request", Toast.LENGTH_LONG)
                .show();
      }
    };

    weatherService.getWeatherResponseWrapper(weatherCallBack);
  }

  public interface OpenWeatherService {
    @GET("/data/2.5/weather?q=San%20Francisco,%20US&units=imperial&APPID=78e3150ff1903ea6a3f9c54cf0edab86")
    void getWeatherResponseWrapper(Callback<WeatherResponseWrapper> callBack);
  }

  static class WeatherResponseWrapper {
    public MainWrapper main;
  }

  static class MainWrapper {
    public Float temp;
  }

}
