package altimate.com.altimate.api;

/**
 * Created by brendanweinstein on 5/16/15.
 */
public class Switchboard {

  private static final String API_KEY_OPENWEATHER = "78e3150ff1903ea6a3f9c54cf0edab86";
  private static final String API_BASE_URL_OPENWEATHER = "http://api.openweathermap.org";
  private static final String API_PATH_OPENWEATHER = "/data/2.5/weather";

  private static final Switchboard INSTANCE = new Switchboard();

  private Switchboard() {

  }

  public static Switchboard getInstance() {
    return INSTANCE;
  }

  public String getOpenWeatherKey() {
    return API_KEY_OPENWEATHER;
  }

  public String getOpenWeatherEndPoint() {
    return API_BASE_URL_OPENWEATHER;
  }

}
