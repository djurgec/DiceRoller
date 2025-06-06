package androidx.startup;

import android.util.Log;

public final class StartupLogger {
  static final boolean DEBUG = false;
  
  private static final String TAG = "StartupLogger";
  
  public static void e(String paramString, Throwable paramThrowable) {
    Log.e("StartupLogger", paramString, paramThrowable);
  }
  
  public static void i(String paramString) {
    Log.i("StartupLogger", paramString);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\startup\StartupLogger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */