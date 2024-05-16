package com.bumptech.glide.manager;

import android.app.Activity;
import android.content.ComponentCallbacks2;
import android.content.res.Configuration;

final class FirstFrameAndAfterTrimMemoryWaiter implements FrameWaiter, ComponentCallbacks2 {
  public void onConfigurationChanged(Configuration paramConfiguration) {}
  
  public void onLowMemory() {
    onTrimMemory(20);
  }
  
  public void onTrimMemory(int paramInt) {}
  
  public void registerSelf(Activity paramActivity) {}
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\manager\FirstFrameAndAfterTrimMemoryWaiter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */