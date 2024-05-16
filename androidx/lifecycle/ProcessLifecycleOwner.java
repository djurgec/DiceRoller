package androidx.lifecycle;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

public class ProcessLifecycleOwner implements LifecycleOwner {
  static final long TIMEOUT_MS = 700L;
  
  private static final ProcessLifecycleOwner sInstance = new ProcessLifecycleOwner();
  
  private Runnable mDelayedPauseRunnable = new Runnable() {
      final ProcessLifecycleOwner this$0;
      
      public void run() {
        ProcessLifecycleOwner.this.dispatchPauseIfNeeded();
        ProcessLifecycleOwner.this.dispatchStopIfNeeded();
      }
    };
  
  private Handler mHandler;
  
  ReportFragment.ActivityInitializationListener mInitializationListener = new ReportFragment.ActivityInitializationListener() {
      final ProcessLifecycleOwner this$0;
      
      public void onCreate() {}
      
      public void onResume() {
        ProcessLifecycleOwner.this.activityResumed();
      }
      
      public void onStart() {
        ProcessLifecycleOwner.this.activityStarted();
      }
    };
  
  private boolean mPauseSent = true;
  
  private final LifecycleRegistry mRegistry = new LifecycleRegistry(this);
  
  private int mResumedCounter = 0;
  
  private int mStartedCounter = 0;
  
  private boolean mStopSent = true;
  
  public static LifecycleOwner get() {
    return sInstance;
  }
  
  static void init(Context paramContext) {
    sInstance.attach(paramContext);
  }
  
  void activityPaused() {
    int i = this.mResumedCounter - 1;
    this.mResumedCounter = i;
    if (i == 0)
      this.mHandler.postDelayed(this.mDelayedPauseRunnable, 700L); 
  }
  
  void activityResumed() {
    int i = this.mResumedCounter + 1;
    this.mResumedCounter = i;
    if (i == 1)
      if (this.mPauseSent) {
        this.mRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME);
        this.mPauseSent = false;
      } else {
        this.mHandler.removeCallbacks(this.mDelayedPauseRunnable);
      }  
  }
  
  void activityStarted() {
    int i = this.mStartedCounter + 1;
    this.mStartedCounter = i;
    if (i == 1 && this.mStopSent) {
      this.mRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START);
      this.mStopSent = false;
    } 
  }
  
  void activityStopped() {
    this.mStartedCounter--;
    dispatchStopIfNeeded();
  }
  
  void attach(Context paramContext) {
    this.mHandler = new Handler();
    this.mRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
    ((Application)paramContext.getApplicationContext()).registerActivityLifecycleCallbacks(new EmptyActivityLifecycleCallbacks() {
          final ProcessLifecycleOwner this$0;
          
          public void onActivityCreated(Activity param1Activity, Bundle param1Bundle) {
            if (Build.VERSION.SDK_INT < 29)
              ReportFragment.get(param1Activity).setProcessListener(ProcessLifecycleOwner.this.mInitializationListener); 
          }
          
          public void onActivityPaused(Activity param1Activity) {
            ProcessLifecycleOwner.this.activityPaused();
          }
          
          public void onActivityPreCreated(Activity param1Activity, Bundle param1Bundle) {
            param1Activity.registerActivityLifecycleCallbacks(new EmptyActivityLifecycleCallbacks() {
                  final ProcessLifecycleOwner.null this$1;
                  
                  public void onActivityPostResumed(Activity param2Activity) {
                    ProcessLifecycleOwner.this.activityResumed();
                  }
                  
                  public void onActivityPostStarted(Activity param2Activity) {
                    ProcessLifecycleOwner.this.activityStarted();
                  }
                });
          }
          
          public void onActivityStopped(Activity param1Activity) {
            ProcessLifecycleOwner.this.activityStopped();
          }
        });
  }
  
  void dispatchPauseIfNeeded() {
    if (this.mResumedCounter == 0) {
      this.mPauseSent = true;
      this.mRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE);
    } 
  }
  
  void dispatchStopIfNeeded() {
    if (this.mStartedCounter == 0 && this.mPauseSent) {
      this.mRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP);
      this.mStopSent = true;
    } 
  }
  
  public Lifecycle getLifecycle() {
    return this.mRegistry;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\lifecycle\ProcessLifecycleOwner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */