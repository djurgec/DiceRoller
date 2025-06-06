package androidx.appcompat.app;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import androidx.core.content.PermissionChecker;
import java.util.Calendar;

class TwilightManager {
  private static final int SUNRISE = 6;
  
  private static final int SUNSET = 22;
  
  private static final String TAG = "TwilightManager";
  
  private static TwilightManager sInstance;
  
  private final Context mContext;
  
  private final LocationManager mLocationManager;
  
  private final TwilightState mTwilightState = new TwilightState();
  
  TwilightManager(Context paramContext, LocationManager paramLocationManager) {
    this.mContext = paramContext;
    this.mLocationManager = paramLocationManager;
  }
  
  static TwilightManager getInstance(Context paramContext) {
    if (sInstance == null) {
      paramContext = paramContext.getApplicationContext();
      sInstance = new TwilightManager(paramContext, (LocationManager)paramContext.getSystemService("location"));
    } 
    return sInstance;
  }
  
  private Location getLastKnownLocation() {
    Location location1 = null;
    Location location2 = null;
    if (PermissionChecker.checkSelfPermission(this.mContext, "android.permission.ACCESS_COARSE_LOCATION") == 0)
      location1 = getLastKnownLocationForProvider("network"); 
    if (PermissionChecker.checkSelfPermission(this.mContext, "android.permission.ACCESS_FINE_LOCATION") == 0)
      location2 = getLastKnownLocationForProvider("gps"); 
    if (location2 != null && location1 != null) {
      if (location2.getTime() > location1.getTime())
        location1 = location2; 
      return location1;
    } 
    if (location2 == null)
      location2 = location1; 
    return location2;
  }
  
  private Location getLastKnownLocationForProvider(String paramString) {
    try {
      if (this.mLocationManager.isProviderEnabled(paramString))
        return this.mLocationManager.getLastKnownLocation(paramString); 
    } catch (Exception exception) {
      Log.d("TwilightManager", "Failed to get last known location", exception);
    } 
    return null;
  }
  
  private boolean isStateValid() {
    boolean bool;
    if (this.mTwilightState.nextUpdate > System.currentTimeMillis()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  static void setInstance(TwilightManager paramTwilightManager) {
    sInstance = paramTwilightManager;
  }
  
  private void updateState(Location paramLocation) {
    TwilightState twilightState = this.mTwilightState;
    long l1 = System.currentTimeMillis();
    TwilightCalculator twilightCalculator = TwilightCalculator.getInstance();
    twilightCalculator.calculateTwilight(l1 - 86400000L, paramLocation.getLatitude(), paramLocation.getLongitude());
    long l4 = twilightCalculator.sunset;
    twilightCalculator.calculateTwilight(l1, paramLocation.getLatitude(), paramLocation.getLongitude());
    int i = twilightCalculator.state;
    boolean bool = true;
    if (i != 1)
      bool = false; 
    long l2 = twilightCalculator.sunrise;
    long l5 = twilightCalculator.sunset;
    twilightCalculator.calculateTwilight(86400000L + l1, paramLocation.getLatitude(), paramLocation.getLongitude());
    long l3 = twilightCalculator.sunrise;
    if (l2 == -1L || l5 == -1L) {
      l1 += 43200000L;
    } else {
      if (l1 > l5) {
        l1 = 0L + l3;
      } else if (l1 > l2) {
        l1 = 0L + l5;
      } else {
        l1 = 0L + l2;
      } 
      l1 += 60000L;
    } 
    twilightState.isNight = bool;
    twilightState.yesterdaySunset = l4;
    twilightState.todaySunrise = l2;
    twilightState.todaySunset = l5;
    twilightState.tomorrowSunrise = l3;
    twilightState.nextUpdate = l1;
  }
  
  boolean isNight() {
    TwilightState twilightState = this.mTwilightState;
    if (isStateValid())
      return twilightState.isNight; 
    Location location = getLastKnownLocation();
    if (location != null) {
      updateState(location);
      return twilightState.isNight;
    } 
    Log.i("TwilightManager", "Could not get last known location. This is probably because the app does not have any location permissions. Falling back to hardcoded sunrise/sunset values.");
    int i = Calendar.getInstance().get(11);
    return (i < 6 || i >= 22);
  }
  
  private static class TwilightState {
    boolean isNight;
    
    long nextUpdate;
    
    long todaySunrise;
    
    long todaySunset;
    
    long tomorrowSunrise;
    
    long yesterdaySunset;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\appcompat\app\TwilightManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */