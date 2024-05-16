package com.bumptech.glide.manager;

class ApplicationLifecycle implements Lifecycle {
  public void addListener(LifecycleListener paramLifecycleListener) {
    paramLifecycleListener.onStart();
  }
  
  public void removeListener(LifecycleListener paramLifecycleListener) {}
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\manager\ApplicationLifecycle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */