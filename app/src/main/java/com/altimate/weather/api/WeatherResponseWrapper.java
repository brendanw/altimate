package altimate.com.altimate.weather.api;

/**
 * Created by brendanweinstein on 5/16/15.
 */
public class WeatherResponseWrapper {

  private MainWrapper main;

  public MainWrapper getMainWrapper() {
    return main;
  }

  public static class MainWrapper {

    private Float temp;

    public Float getTemp() {
      return temp;
    }
  }
}

