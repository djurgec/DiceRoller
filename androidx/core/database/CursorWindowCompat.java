package androidx.core.database;

import android.database.CursorWindow;
import android.os.Build;

public final class CursorWindowCompat {
  public static CursorWindow create(String paramString, long paramLong) {
    return (Build.VERSION.SDK_INT >= 28) ? new CursorWindow(paramString, paramLong) : ((Build.VERSION.SDK_INT >= 15) ? new CursorWindow(paramString) : new CursorWindow(false));
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\database\CursorWindowCompat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */