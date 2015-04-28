package altimate.com.altimate.altimeter;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;

import altimate.com.altimate.R;
import altimate.com.altimate.models.DistanceUnit;
import altimate.com.altimate.persistentdata.AltimatePrefs;

/**
 * Created by jeanweatherwax on 4/5/15.
 */
public class AltimeterActivity extends Activity implements SensorEventListener {

  private static final String TAG = AltimeterActivity.class.getSimpleName();


  private SensorManager mSensorManager;
  private Sensor mPressure;
  private TextView mTextView;


//rounding to decimal place if needed
  public static double round(double value, int places) {
    if (places < 0) throw new IllegalArgumentException();

    BigDecimal bd = new BigDecimal(value);
    bd = bd.setScale(places, RoundingMode.HALF_UP);
    return bd.doubleValue();
  }
  //possible viable equations
  // pressure = 1013250*(1-7.4008204468E-5*height)^5.25588 in kPa and ft
  // 1 m = 3.28084 ft
  //possibly not linear enough in this region to extrapolate
  //reformatted equation:  height = ((pressure/1013250e-3)^0.19026309580888 -1)*-13512.01542


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.altimeter);
    // Get an instance of the sensor service, and use that to get an instance of
    // a particular sensor.
    mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    mPressure = mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
    //inflate textview after calling setContentView()
    mTextView = (TextView) findViewById(R.id.altitude);
    mTextView.setText("test");
    Log.d("hello", "helloooo");
    for (int i = 0; i < 100; i++) {
      System.out.println("TEST");

    }
  }


  @Override
  public final void onAccuracyChanged(Sensor sensor, int accuracy) {
    // Do something here if sensor accuracy changes.
  }

  @Override
  public final void onSensorChanged(SensorEvent event) {
    double millibars_of_pressure = event.values[0];
    //implement simplified equation for pressure/altitude
    double adjust_pressure = millibars_of_pressure * 0.000986923;
    double altitude_ft = (1 - (Math.pow((adjust_pressure), 0.190284))) * 145366.45;
    //1 meter = 3.28084 ft
    double altitude_m = 0.3047999902464 * altitude_ft;
    long altitude_ft_round = Math.round(altitude_ft);
    long altitude_m_round = Math.round(altitude_m);
    String altitude_string_ft = Long.toString(altitude_ft_round);
    String altitude_string_m = Long.toString(altitude_m_round);
    Log.d(TAG, altitude_string_ft);
    Log.d(TAG, Double.toString(millibars_of_pressure));


    DistanceUnit distanceUnit = AltimatePrefs.getUnitPreference(AltimeterActivity.this);


    switch (distanceUnit) {
      case FEET:
        mTextView.setText("Current altitude: " + altitude_string_ft + distanceUnit.name());
        break;
      case METERS:
        mTextView.setText("Current altitude: " + altitude_string_m + distanceUnit.name());
        break;
    }


    System.out.println("expected altitude value " + altitude_string_ft + " millibars of pressure " + millibars_of_pressure);
  }

  @Override
  protected void onResume() {
    // Register a listener for the sensor.
    super.onResume();
    mSensorManager.registerListener(AltimeterActivity.this, mPressure, SensorManager.SENSOR_DELAY_NORMAL);

  }

  @Override
  protected void onPause() {
    // unregister the sensor when the activity pauses.
    super.onPause();
    mSensorManager.unregisterListener(AltimeterActivity.this);
  }

}
