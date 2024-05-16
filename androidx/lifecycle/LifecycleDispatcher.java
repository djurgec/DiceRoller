package androidx.lifecycle;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import java.util.concurrent.atomic.AtomicBoolean;

class LifecycleDispatcher {
  private static AtomicBoolean sInitialized = new AtomicBoolean(false);
  
  static void init(Context paramContext) {
    if (sInitialized.getAndSet(true))
      return; 
    ((Application)paramContext.getApplicationContext()).registerActivityLifecycleCallbacks(new DispatcherActivityCallback());
  }
  
  static class DispatcherActivityCallback extends EmptyActivityLifecycleCallbacks {
    public void onActivityCreated(Activity param1Activity, Bundle param1Bundle) {
      ReportFragment.injectIfNeededIn(param1Activity);
    }
    
    public void onActivitySaveInstanceState(Activity param1Activity, Bundle param1Bundle) {}
    
    public void onActivityStopped(Activity param1Activity) {}
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\lifecycle\LifecycleDispatcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */