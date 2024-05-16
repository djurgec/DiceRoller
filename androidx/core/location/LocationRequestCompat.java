package androidx.core.location;

import android.location.LocationRequest;
import android.os.Build;
import androidx.core.util.Preconditions;
import androidx.core.util.TimeUtils;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;

public final class LocationRequestCompat {
  private static final long IMPLICIT_MIN_UPDATE_INTERVAL = -1L;
  
  public static final long PASSIVE_INTERVAL = 9223372036854775807L;
  
  public static final int QUALITY_BALANCED_POWER_ACCURACY = 102;
  
  public static final int QUALITY_HIGH_ACCURACY = 100;
  
  public static final int QUALITY_LOW_POWER = 104;
  
  private static Method sCreateFromDeprecatedProviderMethod;
  
  private static Method sSetExpireInMethod;
  
  private static Method sSetFastestIntervalMethod;
  
  private static Method sSetNumUpdatesMethod;
  
  private static Method sSetQualityMethod;
  
  final long mDurationMillis;
  
  final long mIntervalMillis;
  
  final long mMaxUpdateDelayMillis;
  
  final int mMaxUpdates;
  
  final float mMinUpdateDistanceMeters;
  
  final long mMinUpdateIntervalMillis;
  
  final int mQuality;
  
  LocationRequestCompat(long paramLong1, int paramInt1, long paramLong2, int paramInt2, long paramLong3, float paramFloat, long paramLong4) {
    this.mIntervalMillis = paramLong1;
    this.mQuality = paramInt1;
    this.mMinUpdateIntervalMillis = paramLong3;
    this.mDurationMillis = paramLong2;
    this.mMaxUpdates = paramInt2;
    this.mMinUpdateDistanceMeters = paramFloat;
    this.mMaxUpdateDelayMillis = paramLong4;
  }
  
  public boolean equals(Object paramObject) {
    boolean bool = true;
    if (this == paramObject)
      return true; 
    if (!(paramObject instanceof LocationRequestCompat))
      return false; 
    paramObject = paramObject;
    if (this.mQuality != ((LocationRequestCompat)paramObject).mQuality || this.mIntervalMillis != ((LocationRequestCompat)paramObject).mIntervalMillis || this.mMinUpdateIntervalMillis != ((LocationRequestCompat)paramObject).mMinUpdateIntervalMillis || this.mDurationMillis != ((LocationRequestCompat)paramObject).mDurationMillis || this.mMaxUpdates != ((LocationRequestCompat)paramObject).mMaxUpdates || Float.compare(((LocationRequestCompat)paramObject).mMinUpdateDistanceMeters, this.mMinUpdateDistanceMeters) != 0 || this.mMaxUpdateDelayMillis != ((LocationRequestCompat)paramObject).mMaxUpdateDelayMillis)
      bool = false; 
    return bool;
  }
  
  public long getDurationMillis() {
    return this.mDurationMillis;
  }
  
  public long getIntervalMillis() {
    return this.mIntervalMillis;
  }
  
  public long getMaxUpdateDelayMillis() {
    return this.mMaxUpdateDelayMillis;
  }
  
  public int getMaxUpdates() {
    return this.mMaxUpdates;
  }
  
  public float getMinUpdateDistanceMeters() {
    return this.mMinUpdateDistanceMeters;
  }
  
  public long getMinUpdateIntervalMillis() {
    long l = this.mMinUpdateIntervalMillis;
    return (l == -1L) ? this.mIntervalMillis : l;
  }
  
  public int getQuality() {
    return this.mQuality;
  }
  
  public int hashCode() {
    int j = this.mQuality;
    long l = this.mIntervalMillis;
    int i = (int)(l ^ l >>> 32L);
    l = this.mMinUpdateIntervalMillis;
    return (j * 31 + i) * 31 + (int)(l ^ l >>> 32L);
  }
  
  public LocationRequest toLocationRequest() {
    return (new LocationRequest.Builder(this.mIntervalMillis)).setQuality(this.mQuality).setMinUpdateIntervalMillis(this.mMinUpdateIntervalMillis).setDurationMillis(this.mDurationMillis).setMaxUpdates(this.mMaxUpdates).setMinUpdateDistanceMeters(this.mMinUpdateDistanceMeters).setMaxUpdateDelayMillis(this.mMaxUpdateDelayMillis).build();
  }
  
  public LocationRequest toLocationRequest(String paramString) {
    if (Build.VERSION.SDK_INT >= 31)
      return toLocationRequest(); 
    try {
      if (sCreateFromDeprecatedProviderMethod == null) {
        Method method = LocationRequest.class.getDeclaredMethod("createFromDeprecatedProvider", new Class[] { String.class, long.class, float.class, boolean.class });
        sCreateFromDeprecatedProviderMethod = method;
        method.setAccessible(true);
      } 
      LocationRequest locationRequest = (LocationRequest)sCreateFromDeprecatedProviderMethod.invoke((Object)null, new Object[] { paramString, Long.valueOf(this.mIntervalMillis), Float.valueOf(this.mMinUpdateDistanceMeters), Boolean.valueOf(false) });
      if (locationRequest == null)
        return null; 
      if (sSetQualityMethod == null) {
        Method method = LocationRequest.class.getDeclaredMethod("setQuality", new Class[] { int.class });
        sSetQualityMethod = method;
        method.setAccessible(true);
      } 
      sSetQualityMethod.invoke(locationRequest, new Object[] { Integer.valueOf(this.mQuality) });
      if (getMinUpdateIntervalMillis() != this.mIntervalMillis) {
        if (sSetFastestIntervalMethod == null) {
          Method method = LocationRequest.class.getDeclaredMethod("setFastestInterval", new Class[] { long.class });
          sSetFastestIntervalMethod = method;
          method.setAccessible(true);
        } 
        sSetFastestIntervalMethod.invoke(locationRequest, new Object[] { Long.valueOf(this.mMinUpdateIntervalMillis) });
      } 
      if (this.mMaxUpdates < Integer.MAX_VALUE) {
        if (sSetNumUpdatesMethod == null) {
          Method method = LocationRequest.class.getDeclaredMethod("setNumUpdates", new Class[] { int.class });
          sSetNumUpdatesMethod = method;
          method.setAccessible(true);
        } 
        sSetNumUpdatesMethod.invoke(locationRequest, new Object[] { Integer.valueOf(this.mMaxUpdates) });
      } 
      if (this.mDurationMillis < Long.MAX_VALUE) {
        if (sSetExpireInMethod == null) {
          Method method = LocationRequest.class.getDeclaredMethod("setExpireIn", new Class[] { long.class });
          sSetExpireInMethod = method;
          method.setAccessible(true);
        } 
        sSetExpireInMethod.invoke(locationRequest, new Object[] { Long.valueOf(this.mDurationMillis) });
      } 
      return locationRequest;
    } catch (NoSuchMethodException|java.lang.reflect.InvocationTargetException|IllegalAccessException noSuchMethodException) {
      return null;
    } 
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Request[");
    if (this.mIntervalMillis != Long.MAX_VALUE) {
      stringBuilder.append("@");
      TimeUtils.formatDuration(this.mIntervalMillis, stringBuilder);
      switch (this.mQuality) {
        case 104:
          stringBuilder.append(" LOW_POWER");
          break;
        case 102:
          stringBuilder.append(" BALANCED");
          break;
        case 100:
          stringBuilder.append(" HIGH_ACCURACY");
          break;
      } 
    } else {
      stringBuilder.append("PASSIVE");
    } 
    if (this.mDurationMillis != Long.MAX_VALUE) {
      stringBuilder.append(", duration=");
      TimeUtils.formatDuration(this.mDurationMillis, stringBuilder);
    } 
    if (this.mMaxUpdates != Integer.MAX_VALUE)
      stringBuilder.append(", maxUpdates=").append(this.mMaxUpdates); 
    long l = this.mMinUpdateIntervalMillis;
    if (l != -1L && l < this.mIntervalMillis) {
      stringBuilder.append(", minUpdateInterval=");
      TimeUtils.formatDuration(this.mMinUpdateIntervalMillis, stringBuilder);
    } 
    if (this.mMinUpdateDistanceMeters > 0.0D)
      stringBuilder.append(", minUpdateDistance=").append(this.mMinUpdateDistanceMeters); 
    if (this.mMaxUpdateDelayMillis / 2L > this.mIntervalMillis) {
      stringBuilder.append(", maxUpdateDelay=");
      TimeUtils.formatDuration(this.mMaxUpdateDelayMillis, stringBuilder);
    } 
    stringBuilder.append(']');
    return stringBuilder.toString();
  }
  
  public static final class Builder {
    private long mDurationMillis;
    
    private long mIntervalMillis;
    
    private long mMaxUpdateDelayMillis;
    
    private int mMaxUpdates;
    
    private float mMinUpdateDistanceMeters;
    
    private long mMinUpdateIntervalMillis;
    
    private int mQuality;
    
    public Builder(long param1Long) {
      setIntervalMillis(param1Long);
      this.mQuality = 102;
      this.mDurationMillis = Long.MAX_VALUE;
      this.mMaxUpdates = Integer.MAX_VALUE;
      this.mMinUpdateIntervalMillis = -1L;
      this.mMinUpdateDistanceMeters = 0.0F;
      this.mMaxUpdateDelayMillis = 0L;
    }
    
    public Builder(LocationRequestCompat param1LocationRequestCompat) {
      this.mIntervalMillis = param1LocationRequestCompat.mIntervalMillis;
      this.mQuality = param1LocationRequestCompat.mQuality;
      this.mDurationMillis = param1LocationRequestCompat.mDurationMillis;
      this.mMaxUpdates = param1LocationRequestCompat.mMaxUpdates;
      this.mMinUpdateIntervalMillis = param1LocationRequestCompat.mMinUpdateIntervalMillis;
      this.mMinUpdateDistanceMeters = param1LocationRequestCompat.mMinUpdateDistanceMeters;
      this.mMaxUpdateDelayMillis = param1LocationRequestCompat.mMaxUpdateDelayMillis;
    }
    
    public LocationRequestCompat build() {
      if (this.mIntervalMillis != Long.MAX_VALUE || this.mMinUpdateIntervalMillis != -1L) {
        boolean bool1 = true;
        Preconditions.checkState(bool1, "passive location requests must have an explicit minimum update interval");
        long l1 = this.mIntervalMillis;
        return new LocationRequestCompat(l1, this.mQuality, this.mDurationMillis, this.mMaxUpdates, Math.min(this.mMinUpdateIntervalMillis, l1), this.mMinUpdateDistanceMeters, this.mMaxUpdateDelayMillis);
      } 
      boolean bool = false;
      Preconditions.checkState(bool, "passive location requests must have an explicit minimum update interval");
      long l = this.mIntervalMillis;
      return new LocationRequestCompat(l, this.mQuality, this.mDurationMillis, this.mMaxUpdates, Math.min(this.mMinUpdateIntervalMillis, l), this.mMinUpdateDistanceMeters, this.mMaxUpdateDelayMillis);
    }
    
    public Builder clearMinUpdateIntervalMillis() {
      this.mMinUpdateIntervalMillis = -1L;
      return this;
    }
    
    public Builder setDurationMillis(long param1Long) {
      this.mDurationMillis = Preconditions.checkArgumentInRange(param1Long, 1L, Long.MAX_VALUE, "durationMillis");
      return this;
    }
    
    public Builder setIntervalMillis(long param1Long) {
      this.mIntervalMillis = Preconditions.checkArgumentInRange(param1Long, 0L, Long.MAX_VALUE, "intervalMillis");
      return this;
    }
    
    public Builder setMaxUpdateDelayMillis(long param1Long) {
      this.mMaxUpdateDelayMillis = param1Long;
      this.mMaxUpdateDelayMillis = Preconditions.checkArgumentInRange(param1Long, 0L, Long.MAX_VALUE, "maxUpdateDelayMillis");
      return this;
    }
    
    public Builder setMaxUpdates(int param1Int) {
      this.mMaxUpdates = Preconditions.checkArgumentInRange(param1Int, 1, 2147483647, "maxUpdates");
      return this;
    }
    
    public Builder setMinUpdateDistanceMeters(float param1Float) {
      this.mMinUpdateDistanceMeters = param1Float;
      this.mMinUpdateDistanceMeters = Preconditions.checkArgumentInRange(param1Float, 0.0F, Float.MAX_VALUE, "minUpdateDistanceMeters");
      return this;
    }
    
    public Builder setMinUpdateIntervalMillis(long param1Long) {
      this.mMinUpdateIntervalMillis = Preconditions.checkArgumentInRange(param1Long, 0L, Long.MAX_VALUE, "minUpdateIntervalMillis");
      return this;
    }
    
    public Builder setQuality(int param1Int) {
      if (param1Int == 104 || param1Int == 102 || param1Int == 100) {
        boolean bool1 = true;
        Preconditions.checkArgument(bool1, "quality must be a defined QUALITY constant, not %d", new Object[] { Integer.valueOf(param1Int) });
        this.mQuality = param1Int;
        return this;
      } 
      boolean bool = false;
      Preconditions.checkArgument(bool, "quality must be a defined QUALITY constant, not %d", new Object[] { Integer.valueOf(param1Int) });
      this.mQuality = param1Int;
      return this;
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface Quality {}
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\location\LocationRequestCompat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */