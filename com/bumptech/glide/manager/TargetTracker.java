package com.bumptech.glide.manager;

import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.util.Util;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;

public final class TargetTracker implements LifecycleListener {
  private final Set<Target<?>> targets = Collections.newSetFromMap(new WeakHashMap<>());
  
  public void clear() {
    this.targets.clear();
  }
  
  public List<Target<?>> getAll() {
    return Util.getSnapshot(this.targets);
  }
  
  public void onDestroy() {
    Iterator<Target> iterator = Util.getSnapshot(this.targets).iterator();
    while (iterator.hasNext())
      ((Target)iterator.next()).onDestroy(); 
  }
  
  public void onStart() {
    Iterator<Target> iterator = Util.getSnapshot(this.targets).iterator();
    while (iterator.hasNext())
      ((Target)iterator.next()).onStart(); 
  }
  
  public void onStop() {
    Iterator<Target> iterator = Util.getSnapshot(this.targets).iterator();
    while (iterator.hasNext())
      ((Target)iterator.next()).onStop(); 
  }
  
  public void track(Target<?> paramTarget) {
    this.targets.add(paramTarget);
  }
  
  public void untrack(Target<?> paramTarget) {
    this.targets.remove(paramTarget);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\manager\TargetTracker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */