package com.bumptech.glide.manager;

import com.bumptech.glide.util.Util;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.WeakHashMap;

class ActivityFragmentLifecycle implements Lifecycle {
  private boolean isDestroyed;
  
  private boolean isStarted;
  
  private final Set<LifecycleListener> lifecycleListeners = Collections.newSetFromMap(new WeakHashMap<>());
  
  public void addListener(LifecycleListener paramLifecycleListener) {
    this.lifecycleListeners.add(paramLifecycleListener);
    if (this.isDestroyed) {
      paramLifecycleListener.onDestroy();
    } else if (this.isStarted) {
      paramLifecycleListener.onStart();
    } else {
      paramLifecycleListener.onStop();
    } 
  }
  
  void onDestroy() {
    this.isDestroyed = true;
    Iterator<LifecycleListener> iterator = Util.getSnapshot(this.lifecycleListeners).iterator();
    while (iterator.hasNext())
      ((LifecycleListener)iterator.next()).onDestroy(); 
  }
  
  void onStart() {
    this.isStarted = true;
    Iterator<LifecycleListener> iterator = Util.getSnapshot(this.lifecycleListeners).iterator();
    while (iterator.hasNext())
      ((LifecycleListener)iterator.next()).onStart(); 
  }
  
  void onStop() {
    this.isStarted = false;
    Iterator<LifecycleListener> iterator = Util.getSnapshot(this.lifecycleListeners).iterator();
    while (iterator.hasNext())
      ((LifecycleListener)iterator.next()).onStop(); 
  }
  
  public void removeListener(LifecycleListener paramLifecycleListener) {
    this.lifecycleListeners.remove(paramLifecycleListener);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\manager\ActivityFragmentLifecycle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */