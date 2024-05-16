package com.bumptech.glide;

import com.bumptech.glide.manager.RequestManagerRetriever;
import com.bumptech.glide.module.AppGlideModule;
import java.util.Set;

abstract class GeneratedAppGlideModule extends AppGlideModule {
  abstract Set<Class<?>> getExcludedModuleClasses();
  
  RequestManagerRetriever.RequestManagerFactory getRequestManagerFactory() {
    return null;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\GeneratedAppGlideModule.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */