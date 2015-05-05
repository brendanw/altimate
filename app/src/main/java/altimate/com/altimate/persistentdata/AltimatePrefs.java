package altimate.com.altimate.persistentdata;

import android.content.Context;
import android.content.SharedPreferences;

import altimate.com.altimate.models.DistanceUnit;

/**
 * Created by jeanweatherwax on 4/25/15.
 */
public class AltimatePrefs {

  private static final String SHARED_PREFS_FILE_NAME = "altimate_prefs";

  private static final String KEY_UNIT_PREFERENCE = "unit_preference";

  public static DistanceUnit getUnitPreference(Context context) {
    SharedPreferences prefs = getSharedPreferences(context);
    String val = prefs.getString(KEY_UNIT_PREFERENCE, "FEET");
    DistanceUnit unit = DistanceUnit.valueOf(val);
    return unit;
  }

  public static void setUnitPreference(Context context, String unitPreference) {
    SharedPreferences.Editor editor = getEditor(context);
    editor.putString(KEY_UNIT_PREFERENCE, unitPreference);
    editor.commit();
  }


  /**
   * Utility method for getting an Editor instance
   */
  private static SharedPreferences.Editor getEditor(Context context) {
    return context.getSharedPreferences(SHARED_PREFS_FILE_NAME, Context.MODE_PRIVATE).edit();
  }

  /**
   * Utility method for getting a SharedPreferences instance
   */
  private static SharedPreferences getSharedPreferences(Context context) {
    return context.getSharedPreferences(SHARED_PREFS_FILE_NAME, Context.MODE_PRIVATE);
  }

}
