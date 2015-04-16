package altimate.com.altimate;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;
import android.view.ViewGroup;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;

/**
 * Created by jeanweatherwax on 4/5/15.
 */
public class AltimeterActivity extends Activity implements SensorEventListener {

  private static HashMap<Integer, Double> sHashMapPressure;

  private SensorManager mSensorManager;
  private Sensor mPressure;
  private TextView mTextView;

  static {
    sHashMapPressure = new HashMap<>();
    sHashMapPressure.put(0, 101.33);
    sHashMapPressure.put(500, 99.49);
    sHashMapPressure.put(1000, 97.63);
    sHashMapPressure.put(1500, 95.91);
    sHashMapPressure.put(2000, 94.19);
    sHashMapPressure.put(2500, 92.46);
    sHashMapPressure.put(3000, 90.81);
    sHashMapPressure.put(3500, 89.15);
    sHashMapPressure.put(4000, 87.49);
    sHashMapPressure.put(4500, 85.91);
    sHashMapPressure.put(5000, 84.33);
    sHashMapPressure.put(6000, 81.22);
    sHashMapPressure.put(7000, 78.19);
    sHashMapPressure.put(8000, 75.22);
    sHashMapPressure.put(9000, 72.40);
    sHashMapPressure.put(10000, 69.64);
    sHashMapPressure.put(15000, 57.16);
    sHashMapPressure.put(20000, 46.61);
    sHashMapPressure.put(25000, 37.65);
    sHashMapPressure.put(30000, 30.13);

  }

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
    float millibars_of_pressure_f = (float) millibars_of_pressure;
    //method 1
    // does not work as accurately as equation used further on
    //float altitude_ft = SensorManager.getAltitude(SensorManager.PRESSURE_STANDARD_ATMOSPHERE, millibars_of_pressure_f);
    // Have sensor data here
    // 1 mbar = 100 Pa
    double kPa_of_pressure = millibars_of_pressure * 10;
    //method 2
    //implement simplified equation for pressure/altitude
    double adjust_pressure = millibars_of_pressure * 0.000986923;
    double altitude_ft = (1-(Math.pow((adjust_pressure),0.190284)))*145366.45;
    //1 meter = 3.28084 ft
    double altitude_m = 0.3047999902464 * altitude_ft;
    double altitude_ft_round = round(altitude_ft,3);
    String altitude_string_ft = Double.toString(altitude_ft_round);
    //TextView altitude = (TextView) findViewById(R.id.altitude);
    //altitude.setText(altitude_string);
    //LayoutInflater inflater = getLayoutInflater();
    //View layout = inflater.inflate(R.layout.altimeter,(ViewGroup) findViewById(R.id.alti_layout_id));
    //TextView altitude = (TextView) layout.findViewById(R.id.altitude);
    //altitude.setText("test");
    Log.d("altimate", altitude_string_ft);
    Log.d("altimate", Double.toString(millibars_of_pressure));
    //for (int i = 0; i < 100; i++) {
      //System.out.println("expected altitude value" + altitude_string);
    //}
    System.out.println("expected altitude value " + altitude_string_ft + " millibars of pressure " + millibars_of_pressure);
    mTextView = (TextView) findViewById(R.id.altitude);
    //mTextView.setText(altitude_string);
    mTextView.setText("Current altitude: " + altitude_string_ft + " ft");
  }

  @Override
  protected void onResume() {
    // Register a listener for the sensor.
    super.onResume();
    mSensorManager.registerListener(this, mPressure, SensorManager.SENSOR_DELAY_NORMAL);

  }

  @Override
  protected void onPause() {
    // unregister the sensor when the activity pauses.
    super.onPause();
    mSensorManager.unregisterListener(this);
  }

}
