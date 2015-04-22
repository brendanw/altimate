package altimate.com.altimate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jeanweatherwax on 4/14/15.
 */
public class SettingsActivity extends Activity {

  // create unitsList for ft, m
  ArrayList<String> unitsList = new ArrayList<String>();

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.settings);

    final Spinner spinner = (Spinner) findViewById(R.id.units_spinner);
    //fill unitsList with ft, m
    unitsList.add("ft");
    unitsList.add("m");
// Create an ArrayAdapter using the string array and a default spinner layout
    //old method using resource
    //ArrayAdapter<String> adapter = ArrayAdapter.createFromResource(this, R.array.units_array, android.R.layout.simple_spinner_item);
    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, unitsList);
// Specify the layout to use when the list of choices appears
    //from old method
    //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
    spinner.setAdapter(arrayAdapter);
    //String unitString = spinner.getSelectedItem().toString();
    //Log.d("altimate", unitString);


    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
        String unitString = spinner.getSelectedItem().toString();
        int pos = unitsList.indexOf(unitString);
        spinner.setSelection(pos, true);
        SharedPreferences prefs = getSharedPreferences("unitStringFIle", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("unitStringFile", unitString);
        editor.commit(); //write unit settings to shared preferences on disk
        //spinner.setAdapter(arrayAdapter);
        //spinner.setSelection(position, true);
        Log.d("altimate", unitString);


      }

      @Override
      public void onNothingSelected(AdapterView<?> parentView) {

      }

    });




//earlier attempt to use intent to save and pass unitString value
    /* Intent i = new Intent(SettingsActivity.this, AltimeterActivity.class);
    i.putExtra("unitString", unitString);
    startActivity(i); */
  }

}

