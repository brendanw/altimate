package com.altimate.weather.api;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by brendanweinstein on 5/16/15.
 */
public class WeatherActions {

  private static final String TAG = WeatherActions.class.getSimpleName();

  private static final String API_WEATHER_BASE_URL = "http://api.openweathermap.org";
  private static final String API_WEATHER_PATH = "/data/2.5/weather";

  private static final String PARAM_QUERY = "q";
  private static final String PARAM_UNITS = "units";
  private static final String PARAM_TOKEN = "APPID";

  private static final String TOKEN_WEATHER = "78e3150ff1903ea6a3f9c54cf0edab86";

  public static void getWeather(String query, String units,
                                Callback<WeatherResponseWrapper> callBack) {
    RestAdapter restAdapter = new RestAdapter.Builder()
            .setEndpoint(API_WEATHER_BASE_URL)
            .setLogLevel(RestAdapter.LogLevel.FULL)
            .build();

    WeatherActions.OpenWeatherService weatherService = restAdapter.create(WeatherActions.OpenWeatherService.class);

    weatherService.getWeatherResponseWrapper(query, units, TOKEN_WEATHER, callBack);

  }

  public interface OpenWeatherService {
    @GET(API_WEATHER_PATH)
    void getWeatherResponseWrapper(@Query(PARAM_QUERY) String query,
                                   @Query(PARAM_UNITS) String units,
                                   @Query(PARAM_TOKEN) String apiToken,
                                   Callback<WeatherResponseWrapper> callBack);
  }

}
