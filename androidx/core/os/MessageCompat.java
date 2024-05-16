package androidx.core.os;

import android.os.Build;
import android.os.Message;

public final class MessageCompat {
  private static boolean sTryIsAsynchronous;
  
  private static boolean sTrySetAsynchronous = true;
  
  static {
    sTryIsAsynchronous = true;
  }
  
  public static boolean isAsynchronous(Message paramMessage) {
    if (Build.VERSION.SDK_INT >= 22)
      return paramMessage.isAsynchronous(); 
    if (sTryIsAsynchronous && Build.VERSION.SDK_INT >= 16)
      try {
        return paramMessage.isAsynchronous();
      } catch (NoSuchMethodError noSuchMethodError) {
        sTryIsAsynchronous = false;
      }  
    return false;
  }
  
  public static void setAsynchronous(Message paramMessage, boolean paramBoolean) {
    if (Build.VERSION.SDK_INT >= 22) {
      paramMessage.setAsynchronous(paramBoolean);
      return;
    } 
    if (sTrySetAsynchronous && Build.VERSION.SDK_INT >= 16)
      try {
        paramMessage.setAsynchronous(paramBoolean);
      } catch (NoSuchMethodError noSuchMethodError) {
        sTrySetAsynchronous = false;
      }  
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\os\MessageCompat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */