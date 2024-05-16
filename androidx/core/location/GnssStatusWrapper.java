package androidx.core.location;

import android.location.GnssStatus;
import android.os.Build;
import androidx.core.util.Preconditions;

class GnssStatusWrapper extends GnssStatusCompat {
  private final GnssStatus mWrapped;
  
  GnssStatusWrapper(GnssStatus paramGnssStatus) {
    this.mWrapped = (GnssStatus)Preconditions.checkNotNull(paramGnssStatus);
  }
  
  public boolean equals(Object paramObject) {
    if (this == paramObject)
      return true; 
    if (!(paramObject instanceof GnssStatusWrapper))
      return false; 
    paramObject = paramObject;
    return this.mWrapped.equals(((GnssStatusWrapper)paramObject).mWrapped);
  }
  
  public float getAzimuthDegrees(int paramInt) {
    return this.mWrapped.getAzimuthDegrees(paramInt);
  }
  
  public float getBasebandCn0DbHz(int paramInt) {
    if (Build.VERSION.SDK_INT >= 30)
      return this.mWrapped.getBasebandCn0DbHz(paramInt); 
    throw new UnsupportedOperationException();
  }
  
  public float getCarrierFrequencyHz(int paramInt) {
    if (Build.VERSION.SDK_INT >= 26)
      return this.mWrapped.getCarrierFrequencyHz(paramInt); 
    throw new UnsupportedOperationException();
  }
  
  public float getCn0DbHz(int paramInt) {
    return this.mWrapped.getCn0DbHz(paramInt);
  }
  
  public int getConstellationType(int paramInt) {
    return this.mWrapped.getConstellationType(paramInt);
  }
  
  public float getElevationDegrees(int paramInt) {
    return this.mWrapped.getElevationDegrees(paramInt);
  }
  
  public int getSatelliteCount() {
    return this.mWrapped.getSatelliteCount();
  }
  
  public int getSvid(int paramInt) {
    return this.mWrapped.getSvid(paramInt);
  }
  
  public boolean hasAlmanacData(int paramInt) {
    return this.mWrapped.hasAlmanacData(paramInt);
  }
  
  public boolean hasBasebandCn0DbHz(int paramInt) {
    return (Build.VERSION.SDK_INT >= 30) ? this.mWrapped.hasBasebandCn0DbHz(paramInt) : false;
  }
  
  public boolean hasCarrierFrequencyHz(int paramInt) {
    return (Build.VERSION.SDK_INT >= 26) ? this.mWrapped.hasCarrierFrequencyHz(paramInt) : false;
  }
  
  public boolean hasEphemerisData(int paramInt) {
    return this.mWrapped.hasEphemerisData(paramInt);
  }
  
  public int hashCode() {
    return this.mWrapped.hashCode();
  }
  
  public boolean usedInFix(int paramInt) {
    return this.mWrapped.usedInFix(paramInt);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\location\GnssStatusWrapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */