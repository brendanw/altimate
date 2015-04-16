package altimate.com.altimate;

import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;

/**
 * Created by jeanweatherwax on 4/14/15.
 */
public class SettingsActivity extends Activity {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.settings);

   Spinner spinner = (Spinner) findViewById(R.id.units_spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
            R.array.units_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
    spinner.setAdapter(adapter);
  }
}