package androidx.dynamicanimation.animation;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.Choreographer;
import androidx.collection.SimpleArrayMap;
import java.util.ArrayList;

class AnimationHandler {
  private static final long FRAME_DELAY_MS = 10L;
  
  public static final ThreadLocal<AnimationHandler> sAnimatorHandler = new ThreadLocal<>();
  
  final ArrayList<AnimationFrameCallback> mAnimationCallbacks = new ArrayList<>();
  
  private final AnimationCallbackDispatcher mCallbackDispatcher = new AnimationCallbackDispatcher();
  
  long mCurrentFrameTime = 0L;
  
  private final SimpleArrayMap<AnimationFrameCallback, Long> mDelayedCallbackStartTime = new SimpleArrayMap();
  
  private boolean mListDirty = false;
  
  private AnimationFrameCallbackProvider mProvider;
  
  private void cleanUpList() {
    if (this.mListDirty) {
      for (int i = this.mAnimationCallbacks.size() - 1; i >= 0; i--) {
        if (this.mAnimationCallbacks.get(i) == null)
          this.mAnimationCallbacks.remove(i); 
      } 
      this.mListDirty = false;
    } 
  }
  
  public static long getFrameTime() {
    ThreadLocal<AnimationHandler> threadLocal = sAnimatorHandler;
    return (threadLocal.get() == null) ? 0L : ((AnimationHandler)threadLocal.get()).mCurrentFrameTime;
  }
  
  public static AnimationHandler getInstance() {
    ThreadLocal<AnimationHandler> threadLocal = sAnimatorHandler;
    if (threadLocal.get() == null)
      threadLocal.set(new AnimationHandler()); 
    return threadLocal.get();
  }
  
  private boolean isCallbackDue(AnimationFrameCallback paramAnimationFrameCallback, long paramLong) {
    Long long_ = (Long)this.mDelayedCallbackStartTime.get(paramAnimationFrameCallback);
    if (long_ == null)
      return true; 
    if (long_.longValue() < paramLong) {
      this.mDelayedCallbackStartTime.remove(paramAnimationFrameCallback);
      return true;
    } 
    return false;
  }
  
  public void addAnimationFrameCallback(AnimationFrameCallback paramAnimationFrameCallback, long paramLong) {
    if (this.mAnimationCallbacks.size() == 0)
      getProvider().postFrameCallback(); 
    if (!this.mAnimationCallbacks.contains(paramAnimationFrameCallback))
      this.mAnimationCallbacks.add(paramAnimationFrameCallback); 
    if (paramLong > 0L)
      this.mDelayedCallbackStartTime.put(paramAnimationFrameCallback, Long.valueOf(SystemClock.uptimeMillis() + paramLong)); 
  }
  
  void doAnimationFrame(long paramLong) {
    long l = SystemClock.uptimeMillis();
    for (byte b = 0; b < this.mAnimationCallbacks.size(); b++) {
      AnimationFrameCallback animationFrameCallback = this.mAnimationCallbacks.get(b);
      if (animationFrameCallback != null && isCallbackDue(animationFrameCallback, l))
        animationFrameCallback.doAnimationFrame(paramLong); 
    } 
    cleanUpList();
  }
  
  AnimationFrameCallbackProvider getProvider() {
    if (this.mProvider == null)
      if (Build.VERSION.SDK_INT >= 16) {
        this.mProvider = new FrameCallbackProvider16(this.mCallbackDispatcher);
      } else {
        this.mProvider = new FrameCallbackProvider14(this.mCallbackDispatcher);
      }  
    return this.mProvider;
  }
  
  public void removeCallback(AnimationFrameCallback paramAnimationFrameCallback) {
    this.mDelayedCallbackStartTime.remove(paramAnimationFrameCallback);
    int i = this.mAnimationCallbacks.indexOf(paramAnimationFrameCallback);
    if (i >= 0) {
      this.mAnimationCallbacks.set(i, null);
      this.mListDirty = true;
    } 
  }
  
  public void setProvider(AnimationFrameCallbackProvider paramAnimationFrameCallbackProvider) {
    this.mProvider = paramAnimationFrameCallbackProvider;
  }
  
  class AnimationCallbackDispatcher {
    final AnimationHandler this$0;
    
    void dispatchAnimationFrame() {
      AnimationHandler.this.mCurrentFrameTime = SystemClock.uptimeMillis();
      AnimationHandler animationHandler = AnimationHandler.this;
      animationHandler.doAnimationFrame(animationHandler.mCurrentFrameTime);
      if (AnimationHandler.this.mAnimationCallbacks.size() > 0)
        AnimationHandler.this.getProvider().postFrameCallback(); 
    }
  }
  
  static interface AnimationFrameCallback {
    boolean doAnimationFrame(long param1Long);
  }
  
  static abstract class AnimationFrameCallbackProvider {
    final AnimationHandler.AnimationCallbackDispatcher mDispatcher;
    
    AnimationFrameCallbackProvider(AnimationHandler.AnimationCallbackDispatcher param1AnimationCallbackDispatcher) {
      this.mDispatcher = param1AnimationCallbackDispatcher;
    }
    
    abstract void postFrameCallback();
  }
  
  private static class FrameCallbackProvider14 extends AnimationFrameCallbackProvider {
    private final Handler mHandler = new Handler(Looper.myLooper());
    
    long mLastFrameTime = -1L;
    
    private final Runnable mRunnable = new Runnable() {
        final AnimationHandler.FrameCallbackProvider14 this$0;
        
        public void run() {
          AnimationHandler.FrameCallbackProvider14.this.mLastFrameTime = SystemClock.uptimeMillis();
          AnimationHandler.FrameCallbackProvider14.this.mDispatcher.dispatchAnimationFrame();
        }
      };
    
    FrameCallbackProvider14(AnimationHandler.AnimationCallbackDispatcher param1AnimationCallbackDispatcher) {
      super(param1AnimationCallbackDispatcher);
    }
    
    void postFrameCallback() {
      long l = Math.max(10L - SystemClock.uptimeMillis() - this.mLastFrameTime, 0L);
      this.mHandler.postDelayed(this.mRunnable, l);
    }
  }
  
  class null implements Runnable {
    final AnimationHandler.FrameCallbackProvider14 this$0;
    
    public void run() {
      this.this$0.mLastFrameTime = SystemClock.uptimeMillis();
      this.this$0.mDispatcher.dispatchAnimationFrame();
    }
  }
  
  private static class FrameCallbackProvider16 extends AnimationFrameCallbackProvider {
    private final Choreographer mChoreographer = Choreographer.getInstance();
    
    private final Choreographer.FrameCallback mChoreographerCallback = new Choreographer.FrameCallback() {
        final AnimationHandler.FrameCallbackProvider16 this$0;
        
        public void doFrame(long param2Long) {
          AnimationHandler.FrameCallbackProvider16.this.mDispatcher.dispatchAnimationFrame();
        }
      };
    
    FrameCallbackProvider16(AnimationHandler.AnimationCallbackDispatcher param1AnimationCallbackDispatcher) {
      super(param1AnimationCallbackDispatcher);
    }
    
    void postFrameCallback() {
      this.mChoreographer.postFrameCallback(this.mChoreographerCallback);
    }
  }
  
  class null implements Choreographer.FrameCallback {
    final AnimationHandler.FrameCallbackProvider16 this$0;
    
    public void doFrame(long param1Long) {
      this.this$0.mDispatcher.dispatchAnimationFrame();
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\dynamicanimation\animation\AnimationHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */