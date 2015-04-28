package altimate.com.altimate;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import altimate.com.altimate.altimeter.AltimeterActivity;
import altimate.com.altimate.settings.SettingsActivity;


public class HomeActivity extends ActionBarActivity {

  private Button mButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.home);
    mButton = (Button) findViewById(R.id.launch_button);
    mButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        //launch altimeter activity here
        Intent myIntent = new Intent(HomeActivity.this, AltimeterActivity.class);
        HomeActivity.this.startActivity(myIntent);

     }
    });
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
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
