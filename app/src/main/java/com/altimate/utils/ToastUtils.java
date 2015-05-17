package com.altimate.utils;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class ToastUtils {

  public static void showLongToast(Activity activity, int textResId) {
    String text = activity.getResources().getString(textResId);
    showLongToast(activity, text);
  }

  public static void showToast(Activity activity, int textResId) {
    String text = activity.getResources().getString(textResId);
    showToast(activity, text);
  }

  public static void showLongToast(Activity activity, String text) {
    showToast(activity, text, Toast.LENGTH_LONG);
  }

  public static void showToast(Activity activity, String text) {
    showToast(activity, text, Toast.LENGTH_SHORT);
  }

  public static void showToast(Context context, String text) {
    showToast((Activity) context, text, Toast.LENGTH_SHORT);
  }

  public static void showToast(Activity activity, String text, int duration) {
    Toast toast = makeToast(activity, text, duration);
    toast.show();
  }

  public static Toast makeToast(Activity activity, int textResId, int duration) {
    String text = activity.getResources().getString(textResId);
    return makeToast(activity, text, duration);
  }

  private static Toast makeToast(Activity activity, String text, int duration) {
    Toast toast = new Toast(activity);
    toast.setGravity(Gravity.CENTER, 0, 0);
    toast.setDuration(duration);
    toast.setText(text);
    return toast;
  }
}