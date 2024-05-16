package androidx.core.location;

import android.location.LocationListener;
import android.os.Bundle;

public interface LocationListenerCompat extends LocationListener {
  default void onProviderDisabled(String paramString) {}
  
  default void onProviderEnabled(String paramString) {}
  
  default void onStatusChanged(String paramString, int paramInt, Bundle paramBundle) {}
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\location\LocationListenerCompat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */