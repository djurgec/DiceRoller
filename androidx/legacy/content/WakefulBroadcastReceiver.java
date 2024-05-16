package androidx.legacy.content;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;
import android.util.SparseArray;

@Deprecated
public abstract class WakefulBroadcastReceiver extends BroadcastReceiver {
  private static final String EXTRA_WAKE_LOCK_ID = "androidx.contentpager.content.wakelockid";
  
  private static int mNextId;
  
  private static final SparseArray<PowerManager.WakeLock> sActiveWakeLocks = new SparseArray();
  
  static {
    mNextId = 1;
  }
  
  public static boolean completeWakefulIntent(Intent paramIntent) {
    int i = paramIntent.getIntExtra("androidx.contentpager.content.wakelockid", 0);
    if (i == 0)
      return false; 
    synchronized (sActiveWakeLocks) {
      PowerManager.WakeLock wakeLock = (PowerManager.WakeLock)null.get(i);
      if (wakeLock != null) {
        wakeLock.release();
        null.remove(i);
        return true;
      } 
      StringBuilder stringBuilder = new StringBuilder();
      this();
      Log.w("WakefulBroadcastReceiv.", stringBuilder.append("No active wake lock id #").append(i).toString());
      return true;
    } 
  }
  
  public static ComponentName startWakefulService(Context paramContext, Intent paramIntent) {
    synchronized (sActiveWakeLocks) {
      int i = mNextId;
      int j = i + 1;
      mNextId = j;
      if (j <= 0)
        mNextId = 1; 
      paramIntent.putExtra("androidx.contentpager.content.wakelockid", i);
      ComponentName componentName = paramContext.startService(paramIntent);
      if (componentName == null)
        return null; 
      PowerManager powerManager = (PowerManager)paramContext.getSystemService("power");
      StringBuilder stringBuilder = new StringBuilder();
      this();
      PowerManager.WakeLock wakeLock = powerManager.newWakeLock(1, stringBuilder.append("androidx.core:wake:").append(componentName.flattenToShortString()).toString());
      wakeLock.setReferenceCounted(false);
      wakeLock.acquire(60000L);
      null.put(i, wakeLock);
      return componentName;
    } 
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\legacy\content\WakefulBroadcastReceiver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */