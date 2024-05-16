package androidx.lifecycle;

import androidx.arch.core.executor.ArchTaskExecutor;
import androidx.arch.core.internal.SafeIterableMap;
import java.util.Map;

public abstract class LiveData<T> {
  static final Object NOT_SET = new Object();
  
  static final int START_VERSION = -1;
  
  int mActiveCount = 0;
  
  private boolean mChangingActiveState;
  
  private volatile Object mData;
  
  final Object mDataLock = new Object();
  
  private boolean mDispatchInvalidated;
  
  private boolean mDispatchingValue;
  
  private SafeIterableMap<Observer<? super T>, ObserverWrapper> mObservers = new SafeIterableMap();
  
  volatile Object mPendingData;
  
  private final Runnable mPostValueRunnable;
  
  private int mVersion;
  
  public LiveData() {
    Object object = NOT_SET;
    this.mPendingData = object;
    this.mPostValueRunnable = new Runnable() {
        final LiveData this$0;
        
        public void run() {
          synchronized (LiveData.this.mDataLock) {
            Object object = LiveData.this.mPendingData;
            LiveData.this.mPendingData = LiveData.NOT_SET;
            LiveData.this.setValue(object);
            return;
          } 
        }
      };
    this.mData = object;
    this.mVersion = -1;
  }
  
  public LiveData(T paramT) {
    this.mPendingData = NOT_SET;
    this.mPostValueRunnable = new Runnable() {
        final LiveData this$0;
        
        public void run() {
          synchronized (LiveData.this.mDataLock) {
            Object object = LiveData.this.mPendingData;
            LiveData.this.mPendingData = LiveData.NOT_SET;
            LiveData.this.setValue(object);
            return;
          } 
        }
      };
    this.mData = paramT;
    this.mVersion = 0;
  }
  
  static void assertMainThread(String paramString) {
    if (ArchTaskExecutor.getInstance().isMainThread())
      return; 
    throw new IllegalStateException("Cannot invoke " + paramString + " on a background thread");
  }
  
  private void considerNotify(ObserverWrapper paramObserverWrapper) {
    if (!paramObserverWrapper.mActive)
      return; 
    if (!paramObserverWrapper.shouldBeActive()) {
      paramObserverWrapper.activeStateChanged(false);
      return;
    } 
    int j = paramObserverWrapper.mLastVersion;
    int i = this.mVersion;
    if (j >= i)
      return; 
    paramObserverWrapper.mLastVersion = i;
    paramObserverWrapper.mObserver.onChanged((T)this.mData);
  }
  
  void changeActiveCounter(int paramInt) {
    int i = this.mActiveCount;
    this.mActiveCount += paramInt;
    if (this.mChangingActiveState)
      return; 
    this.mChangingActiveState = true;
    try {
      while (true) {
        int j = this.mActiveCount;
        if (i != j) {
          if (i == 0 && j > 0) {
            paramInt = 1;
          } else {
            paramInt = 0;
          } 
          if (i > 0 && j == 0) {
            i = 1;
          } else {
            i = 0;
          } 
          if (paramInt != 0) {
            onActive();
          } else if (i != 0) {
            onInactive();
          } 
          i = j;
          continue;
        } 
        return;
      } 
    } finally {
      this.mChangingActiveState = false;
    } 
  }
  
  void dispatchingValue(ObserverWrapper paramObserverWrapper) {
    if (this.mDispatchingValue) {
      this.mDispatchInvalidated = true;
      return;
    } 
    this.mDispatchingValue = true;
    for (ObserverWrapper observerWrapper = paramObserverWrapper;; observerWrapper = paramObserverWrapper) {
      this.mDispatchInvalidated = false;
      if (observerWrapper != null) {
        considerNotify(observerWrapper);
        paramObserverWrapper = null;
      } else {
        SafeIterableMap.IteratorWithAdditions<Map.Entry> iteratorWithAdditions = this.mObservers.iteratorWithAdditions();
        while (true) {
          paramObserverWrapper = observerWrapper;
          if (iteratorWithAdditions.hasNext()) {
            considerNotify((ObserverWrapper)((Map.Entry)iteratorWithAdditions.next()).getValue());
            if (this.mDispatchInvalidated) {
              paramObserverWrapper = observerWrapper;
              break;
            } 
            continue;
          } 
          break;
        } 
      } 
      if (!this.mDispatchInvalidated) {
        this.mDispatchingValue = false;
        return;
      } 
    } 
  }
  
  public T getValue() {
    Object object = this.mData;
    return (T)((object != NOT_SET) ? object : null);
  }
  
  int getVersion() {
    return this.mVersion;
  }
  
  public boolean hasActiveObservers() {
    boolean bool;
    if (this.mActiveCount > 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean hasObservers() {
    boolean bool;
    if (this.mObservers.size() > 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void observe(LifecycleOwner paramLifecycleOwner, Observer<? super T> paramObserver) {
    assertMainThread("observe");
    if (paramLifecycleOwner.getLifecycle().getCurrentState() == Lifecycle.State.DESTROYED)
      return; 
    LifecycleBoundObserver lifecycleBoundObserver = new LifecycleBoundObserver(paramLifecycleOwner, paramObserver);
    ObserverWrapper observerWrapper = (ObserverWrapper)this.mObservers.putIfAbsent(paramObserver, lifecycleBoundObserver);
    if (observerWrapper == null || observerWrapper.isAttachedTo(paramLifecycleOwner)) {
      if (observerWrapper != null)
        return; 
      paramLifecycleOwner.getLifecycle().addObserver(lifecycleBoundObserver);
      return;
    } 
    throw new IllegalArgumentException("Cannot add the same observer with different lifecycles");
  }
  
  public void observeForever(Observer<? super T> paramObserver) {
    assertMainThread("observeForever");
    AlwaysActiveObserver alwaysActiveObserver = new AlwaysActiveObserver(paramObserver);
    ObserverWrapper observerWrapper = (ObserverWrapper)this.mObservers.putIfAbsent(paramObserver, alwaysActiveObserver);
    if (!(observerWrapper instanceof LifecycleBoundObserver)) {
      if (observerWrapper != null)
        return; 
      alwaysActiveObserver.activeStateChanged(true);
      return;
    } 
    throw new IllegalArgumentException("Cannot add the same observer with different lifecycles");
  }
  
  protected void onActive() {}
  
  protected void onInactive() {}
  
  protected void postValue(T paramT) {
    synchronized (this.mDataLock) {
      boolean bool;
      if (this.mPendingData == NOT_SET) {
        bool = true;
      } else {
        bool = false;
      } 
      this.mPendingData = paramT;
      if (!bool)
        return; 
      ArchTaskExecutor.getInstance().postToMainThread(this.mPostValueRunnable);
      return;
    } 
  }
  
  public void removeObserver(Observer<? super T> paramObserver) {
    assertMainThread("removeObserver");
    ObserverWrapper observerWrapper = (ObserverWrapper)this.mObservers.remove(paramObserver);
    if (observerWrapper == null)
      return; 
    observerWrapper.detachObserver();
    observerWrapper.activeStateChanged(false);
  }
  
  public void removeObservers(LifecycleOwner paramLifecycleOwner) {
    assertMainThread("removeObservers");
    for (Map.Entry entry : this.mObservers) {
      if (((ObserverWrapper)entry.getValue()).isAttachedTo(paramLifecycleOwner))
        removeObserver((Observer<? super T>)entry.getKey()); 
    } 
  }
  
  protected void setValue(T paramT) {
    assertMainThread("setValue");
    this.mVersion++;
    this.mData = paramT;
    dispatchingValue(null);
  }
  
  private class AlwaysActiveObserver extends ObserverWrapper {
    final LiveData this$0;
    
    AlwaysActiveObserver(Observer<? super T> param1Observer) {
      super(param1Observer);
    }
    
    boolean shouldBeActive() {
      return true;
    }
  }
  
  class LifecycleBoundObserver extends ObserverWrapper implements LifecycleEventObserver {
    final LifecycleOwner mOwner;
    
    final LiveData this$0;
    
    LifecycleBoundObserver(LifecycleOwner param1LifecycleOwner, Observer<? super T> param1Observer) {
      super(param1Observer);
      this.mOwner = param1LifecycleOwner;
    }
    
    void detachObserver() {
      this.mOwner.getLifecycle().removeObserver(this);
    }
    
    boolean isAttachedTo(LifecycleOwner param1LifecycleOwner) {
      boolean bool;
      if (this.mOwner == param1LifecycleOwner) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public void onStateChanged(LifecycleOwner param1LifecycleOwner, Lifecycle.Event param1Event) {
      Lifecycle.State state = this.mOwner.getLifecycle().getCurrentState();
      if (state == Lifecycle.State.DESTROYED) {
        LiveData.this.removeObserver(this.mObserver);
        return;
      } 
      param1Event = null;
      while (param1Event != state) {
        Lifecycle.State state1 = state;
        activeStateChanged(shouldBeActive());
        state = this.mOwner.getLifecycle().getCurrentState();
      } 
    }
    
    boolean shouldBeActive() {
      return this.mOwner.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED);
    }
  }
  
  private abstract class ObserverWrapper {
    boolean mActive;
    
    int mLastVersion = -1;
    
    final Observer<? super T> mObserver;
    
    final LiveData this$0;
    
    ObserverWrapper(Observer<? super T> param1Observer) {
      this.mObserver = param1Observer;
    }
    
    void activeStateChanged(boolean param1Boolean) {
      byte b;
      if (param1Boolean == this.mActive)
        return; 
      this.mActive = param1Boolean;
      LiveData liveData = LiveData.this;
      if (param1Boolean) {
        b = 1;
      } else {
        b = -1;
      } 
      liveData.changeActiveCounter(b);
      if (this.mActive)
        LiveData.this.dispatchingValue(this); 
    }
    
    void detachObserver() {}
    
    boolean isAttachedTo(LifecycleOwner param1LifecycleOwner) {
      return false;
    }
    
    abstract boolean shouldBeActive();
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\lifecycle\LiveData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */