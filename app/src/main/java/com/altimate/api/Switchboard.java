package com.altimate.api;

/**
 * Created by brendanweinstein on 5/16/15.
 */
public class Switchboard {

  private static final Switchboard INSTANCE = new Switchboard();

  private static final boolean IS_DEBUGGING = false;

  private Switchboard() {
  }

  public static Switchboard getInstance() {
    return INSTANCE;
  }

  public boolean isDebugging() {
    return IS_DEBUGGING;
  }

}
