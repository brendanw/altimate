package com.altimate;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.altimate.altimeter.AltimeterActivity;
import com.altimate.settings.SettingsActivity;


public class HomeActivity extends ActionBarActivity {

  /**simple GPS checker intent */
  //Intent gpsOptionsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
  //startActivity(gpsOptionsIntent);

  private Button mLaunchAltimeterButton;
  //private Button mViewWeatherButton;

  /**Alert if GPS not enabled */
  public void buildAlertGPS() {
    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
            .setCancelable(false)
            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
              public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
              }
            })
            .setNegativeButton("No", new DialogInterface.OnClickListener() {
              public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                dialog.cancel();
              }
            });
    final AlertDialog alert = builder.create();
    alert.show();
  }


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    /**simple GPS checker */
    //startActivity(gpsOptionsIntent);

    /** check if location services enabled and prompt if not */
    final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
    if ( !manager.isProviderEnabled((LocationManager.GPS_PROVIDER))) {
      buildAlertGPS();
    }

    setContentView(R.layout.home);

    /** Initiate Launch Altimeter Button */
    mLaunchAltimeterButton = (Button) findViewById(R.id.launch_button);
    mLaunchAltimeterButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        /** Launch altimeter activity here */
        Intent launchAltimeterIntent = new Intent(HomeActivity.this, AltimeterActivity.class);
        HomeActivity.this.startActivity(launchAltimeterIntent);
      }
    });

     /** Initiate Weather Button */
    /* mViewWeatherButton = (Button) findViewById(R.id.weather_button);
    mViewWeatherButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent launchWeatherIntent = new Intent(HomeActivity.this, WeatherActivity.class);
        HomeActivity.this.startActivity(launchWeatherIntent);
      }
    }); */

  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    /** Inflate the menu; this adds items to the action bar if it is present. */
    getMenuInflater().inflate(R.menu.menu_home, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    switch(item.getItemId()) {
    //menu options
    case R.id.settings:
      Intent myIntent = new Intent(HomeActivity.this, SettingsActivity.class);
      HomeActivity.this.startActivity(myIntent);
      break;
      default:
        return super.onOptionsItemSelected(item);
    }

    return true;

  }

}
