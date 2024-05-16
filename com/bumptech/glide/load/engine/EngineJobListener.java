package com.bumptech.glide.load.engine;

import com.bumptech.glide.load.Key;

interface EngineJobListener {
  void onEngineJobCancelled(EngineJob<?> paramEngineJob, Key paramKey);
  
  void onEngineJobComplete(EngineJob<?> paramEngineJob, Key paramKey, EngineResource<?> paramEngineResource);
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\engine\EngineJobListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */