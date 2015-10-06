package com.altimate.altimeter;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.altimate.R;
import com.altimate.models.DistanceUnit;
import com.altimate.persistentdata.AltimatePrefs;
import com.altimate.utils.ToastUtils;
import com.altimate.weather.api.WeatherActions;
import com.altimate.weather.api.WeatherResponseWrapper;
import com.altimate.weather.events.LocationUpdateEvent;
import com.altimate.weather.events.WeatherResponseFailure;
import com.altimate.weather.events.WeatherResponseSuccess;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by jeanweatherwax on 4/5/15.
 */
public class AltimeterActivity extends Activity implements SensorEventListener {

  private Button mZeroButton, mStartButton, mStopButton, mLoadButton;

  private boolean started = false;

  private ArrayList<AltiData> altiData = new ArrayList<AltiData>();

  private View mChart;

  private static final String TAG = AltimeterActivity.class.getSimpleName();

  private static final float DEFAULT_BASE_PRESSURE = 145366.45f;

  private RelativeLayout layout;

  //private AltiData altiData2;


  /** Sensor objects */
  private SensorManager mSensorManager;
  private Sensor mPressure;

  /** Views */
  private TextView mTextView;

  /** Other */
  private DistanceUnit mDistanceUnit;
  private double mBasePressure;
  private double mBasePressureCoefficient = 0.000986923;
  double altitude_ft_zero;


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.altimeter);
    mTextView = (TextView) findViewById(R.id.altitude);
    layout = (RelativeLayout) findViewById(R.id.alti_layout_id);
    altiData = new ArrayList();

    /**
     *  Get an instance of the sensor service, and use that to get an instance of
     *  a particular sensor.
     */
    mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    mPressure = mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);

    mDistanceUnit = AltimatePrefs.getUnitPreference(AltimeterActivity.this);

    Location location = AltimatePrefs.getLocation(AltimeterActivity.this);
    /** TODO: properly select unit system */
    String units = "imperial";
    String latitude = Double.toString(location.getLatitude());
    String longitude = Double.toString(location.getLongitude());
    WeatherActions.getWeather(units, latitude, longitude, weatherCallBack);

    float basePressure = AltimatePrefs.getBasePressure(AltimeterActivity.this);
    if(basePressure != 0f) {
      mBasePressure = AltimatePrefs.getBasePressure(AltimeterActivity.this);
      mBasePressureCoefficient = 1.0 / mBasePressure;
    }

    /**
    /**Initiate start and stop recording buttons
    mStartButton = (Button) findViewById(R.id.start_button);
    mStopButton = (Button) findViewById(R.id.stop_button);
    mLoadButton = (Button) findViewById(R.id.load_button);
    mStartButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Log.d(TAG, "start button pressed");
      }
    });
    mStopButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Log.d(TAG, "stop button pressed");
      }
    });
    mLoadButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Log.d(TAG, "load button pressed");
      }
    });
    */


  }

  @Override
  public final void onAccuracyChanged(Sensor sensor, int accuracy) {
    /** Do something here if sensor accuracy changes. */
  }

  /** TODO: Clean this bit of code - altitude variables look disorganized
   * possible viable equations
   * pressure = 1013250*(1-7.4008204468E-5*height)^5.25588 in kPa and ft
   * 1 m = 3.28084 ft
   * possibly not linear enough in this region to extrapolate
   * reformatted equation:  height = ((pressure/1013250e-3)^0.19026309580888 -1)*-13512.01542
   */
  @Override
  public final void onSensorChanged(SensorEvent event) {
    final double current_millibars_of_pressure = event.values[0];
    final double adjust_pressure = current_millibars_of_pressure * mBasePressureCoefficient;

    /**implement simplified equation for pressure/altitude */
    final double altitude_ft = (1 - (Math.pow((adjust_pressure), 0.190284))) * 145366.45;




    /**Initiate zero and calibrate button*/
    mZeroButton = (Button) findViewById(R.id.zero_button);
    mZeroButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        //implement simplified equation for pressure/altitude
        altitude_ft_zero = altitude_ft;
        Log.d(TAG, "calibrate button pressed");

      }
    });



    double altitude_ft_calibrated = altitude_ft - altitude_ft_zero;
    long altitude_ft_round = Math.round(altitude_ft_calibrated);
    String altitude_string_ft = Long.toString(altitude_ft_round);

    Log.d(TAG, altitude_string_ft);
    Log.d(TAG, Double.toString(current_millibars_of_pressure));

    switch (mDistanceUnit) {
      case FEET:
        mTextView.setText("Current altitude: " + altitude_string_ft + " " + mDistanceUnit.getShortFormValue());
        break;
      case METERS:
        //1 meter = 3.28084 ft
        double altitude_m = 0.3047999902464 * altitude_ft_calibrated;
        long altitude_m_round = Math.round(altitude_m);
        String altitude_string_m = Long.toString(altitude_m_round);
        mTextView.setText("Current altitude: " + altitude_string_m + " " + mDistanceUnit.getShortFormValue());
        break;
    }

    Log.d(TAG, "expected altitude value " + altitude_string_ft + " millibars of pressure " + current_millibars_of_pressure);

  }

  @Override
  protected void onResume() {
    super.onResume();
    EventBus.getDefault().register(AltimeterActivity.this);
    mSensorManager.registerListener(AltimeterActivity.this, mPressure, SensorManager.SENSOR_DELAY_NORMAL);
  }

  @Override
  protected void onPause() {
    super.onPause();
    EventBus.getDefault().unregister(AltimeterActivity.this);
    mSensorManager.unregisterListener(AltimeterActivity.this);
  }

  public void onEvent(LocationUpdateEvent locationUpdateEvent) {
    Location location = locationUpdateEvent.getLocation();
    /** get updated base pressure */
    String units = "imperial";
    String latitude = Double.toString(location.getLatitude());
    String longitude = Double.toString(location.getLongitude());
    WeatherActions.getWeather(units, latitude, longitude, weatherCallBack);
  }

  public void onEvent(WeatherResponseSuccess success) {
    WeatherResponseWrapper weatherResponseWrapper = success.getWeatherResponseWrapper();
    Float basePressure = weatherResponseWrapper.getMainWrapper().getPressure();
    mBasePressure = basePressure;
    mBasePressureCoefficient = 1.0 / mBasePressure;
    AltimatePrefs.setBasePressure(AltimeterActivity.this, basePressure);
  }

  public void onEvent(WeatherResponseFailure failure) {
    ToastUtils.showLongToast(AltimeterActivity.this, "Unsuccessful base pressure request");
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
