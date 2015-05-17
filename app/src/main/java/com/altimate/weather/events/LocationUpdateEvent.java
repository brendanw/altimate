package com.altimate.weather.events;

import android.location.Location;

/**
 * Created by brendanweinstein on 5/16/15.
 */
public class LocationUpdateEvent {

  private Location mLocation;

  public LocationUpdateEvent(Location location) {
    mLocation = location;
  }

  public Location getLocation() {
    return mLocation;
  }

}
