package altimate.com.altimate.settings;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import altimate.com.altimate.R;
import altimate.com.altimate.models.DistanceUnit;
import altimate.com.altimate.persistentdata.AltimatePrefs;

/**
 * Created by jeanweatherwax on 4/14/15.
 */
public class SettingsActivity extends Activity {

  private static final String TAG = SettingsActivity.class.getSimpleName();

  private ArrayList<String> unitsList = new ArrayList<>();

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    /** Instantiate Views */
    setContentView(R.layout.settings);
    final Spinner spinner = (Spinner) findViewById(R.id.units_spinner);

    /** Setup Units Adapter */
    UnitsAdapter unitsAdapter = new UnitsAdapter(SettingsActivity.this);
    List<DistanceUnit> distanceUnits = Arrays.asList(DistanceUnit.values());
    unitsAdapter.setItems(distanceUnits);

    spinner.setAdapter(unitsAdapter);

    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
        String unitPreference = spinner.getSelectedItem().toString();
        int pos = unitsList.indexOf(unitPreference);
        spinner.setSelection(pos, true);
        AltimatePrefs.setUnitPreference(SettingsActivity.this, unitPreference);
        Log.d(TAG, "unitPreference: " + unitPreference);
      }

      @Override
      public void onNothingSelected(AdapterView<?> parentView) { }
    });
  }

}

