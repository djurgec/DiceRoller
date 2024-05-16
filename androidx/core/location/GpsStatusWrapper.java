package androidx.core.location;

import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.os.Build;
import androidx.core.util.Preconditions;
import java.util.Iterator;

class GpsStatusWrapper extends GnssStatusCompat {
  private static final int BEIDOU_PRN_COUNT = 35;
  
  private static final int BEIDOU_PRN_OFFSET = 200;
  
  private static final int GLONASS_PRN_COUNT = 24;
  
  private static final int GLONASS_PRN_OFFSET = 64;
  
  private static final int GPS_PRN_COUNT = 32;
  
  private static final int GPS_PRN_OFFSET = 0;
  
  private static final int QZSS_SVID_MAX = 200;
  
  private static final int QZSS_SVID_MIN = 193;
  
  private static final int SBAS_PRN_MAX = 64;
  
  private static final int SBAS_PRN_MIN = 33;
  
  private static final int SBAS_PRN_OFFSET = -87;
  
  private Iterator<GpsSatellite> mCachedIterator;
  
  private int mCachedIteratorPosition;
  
  private GpsSatellite mCachedSatellite;
  
  private int mCachedSatelliteCount;
  
  private final GpsStatus mWrapped;
  
  GpsStatusWrapper(GpsStatus paramGpsStatus) {
    paramGpsStatus = (GpsStatus)Preconditions.checkNotNull(paramGpsStatus);
    this.mWrapped = paramGpsStatus;
    this.mCachedSatelliteCount = -1;
    this.mCachedIterator = paramGpsStatus.getSatellites().iterator();
    this.mCachedIteratorPosition = -1;
    this.mCachedSatellite = null;
  }
  
  private static int getConstellationFromPrn(int paramInt) {
    return (paramInt > 0 && paramInt <= 32) ? 1 : ((paramInt >= 33 && paramInt <= 64) ? 2 : ((paramInt > 64 && paramInt <= 88) ? 3 : ((paramInt > 200 && paramInt <= 235) ? 5 : ((paramInt >= 193 && paramInt <= 200) ? 4 : 0))));
  }
  
  private GpsSatellite getSatellite(int paramInt) {
    synchronized (this.mWrapped) {
      if (paramInt < this.mCachedIteratorPosition) {
        this.mCachedIterator = this.mWrapped.getSatellites().iterator();
        this.mCachedIteratorPosition = -1;
      } 
      while (true) {
        int i = this.mCachedIteratorPosition;
        if (i < paramInt) {
          this.mCachedIteratorPosition = i + 1;
          if (!this.mCachedIterator.hasNext()) {
            this.mCachedSatellite = null;
            break;
          } 
          this.mCachedSatellite = this.mCachedIterator.next();
          continue;
        } 
        break;
      } 
      GpsSatellite gpsSatellite = this.mCachedSatellite;
      return (GpsSatellite)Preconditions.checkNotNull(gpsSatellite);
    } 
  }
  
  private static int getSvidFromPrn(int paramInt) {
    switch (getConstellationFromPrn(paramInt)) {
      default:
        return paramInt;
      case 5:
        paramInt -= 200;
      case 3:
        paramInt -= 64;
      case 2:
        break;
    } 
    paramInt += 87;
  }
  
  public boolean equals(Object paramObject) {
    if (this == paramObject)
      return true; 
    if (!(paramObject instanceof GpsStatusWrapper))
      return false; 
    paramObject = paramObject;
    return this.mWrapped.equals(((GpsStatusWrapper)paramObject).mWrapped);
  }
  
  public float getAzimuthDegrees(int paramInt) {
    return getSatellite(paramInt).getAzimuth();
  }
  
  public float getBasebandCn0DbHz(int paramInt) {
    throw new UnsupportedOperationException();
  }
  
  public float getCarrierFrequencyHz(int paramInt) {
    throw new UnsupportedOperationException();
  }
  
  public float getCn0DbHz(int paramInt) {
    return getSatellite(paramInt).getSnr();
  }
  
  public int getConstellationType(int paramInt) {
    return (Build.VERSION.SDK_INT < 24) ? 1 : getConstellationFromPrn(getSatellite(paramInt).getPrn());
  }
  
  public float getElevationDegrees(int paramInt) {
    return getSatellite(paramInt).getElevation();
  }
  
  public int getSatelliteCount() {
    synchronized (this.mWrapped) {
      if (this.mCachedSatelliteCount == -1) {
        for (GpsSatellite gpsSatellite : this.mWrapped.getSatellites())
          this.mCachedSatelliteCount++; 
        this.mCachedSatelliteCount++;
      } 
      return this.mCachedSatelliteCount;
    } 
  }
  
  public int getSvid(int paramInt) {
    return (Build.VERSION.SDK_INT < 24) ? getSatellite(paramInt).getPrn() : getSvidFromPrn(getSatellite(paramInt).getPrn());
  }
  
  public boolean hasAlmanacData(int paramInt) {
    return getSatellite(paramInt).hasAlmanac();
  }
  
  public boolean hasBasebandCn0DbHz(int paramInt) {
    return false;
  }
  
  public boolean hasCarrierFrequencyHz(int paramInt) {
    return false;
  }
  
  public boolean hasEphemerisData(int paramInt) {
    return getSatellite(paramInt).hasEphemeris();
  }
  
  public int hashCode() {
    return this.mWrapped.hashCode();
  }
  
  public boolean usedInFix(int paramInt) {
    return getSatellite(paramInt).usedInFix();
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\location\GpsStatusWrapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */