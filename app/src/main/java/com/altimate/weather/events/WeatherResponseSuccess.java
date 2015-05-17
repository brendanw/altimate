package com.altimate.weather.events;

import com.altimate.weather.api.WeatherResponseWrapper;

/**
 * Created by brendanweinstein on 5/16/15.
 */
public class WeatherResponseSuccess {

  public WeatherResponseSuccess(WeatherResponseWrapper weatherResponseWrapper) {
    mWeatherResponseWrapper = weatherResponseWrapper;
  }

  private WeatherResponseWrapper mWeatherResponseWrapper;

  public WeatherResponseWrapper getWeatherResponseWrapper() {
    return mWeatherResponseWrapper;
  }

}
