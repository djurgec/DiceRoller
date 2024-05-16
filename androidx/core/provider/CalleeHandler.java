package androidx.core.provider;

import android.os.Handler;
import android.os.Looper;

class CalleeHandler {
  static Handler create() {
    Handler handler;
    if (Looper.myLooper() == null) {
      handler = new Handler(Looper.getMainLooper());
    } else {
      handler = new Handler();
    } 
    return handler;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\provider\CalleeHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */