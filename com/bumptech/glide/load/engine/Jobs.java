package com.bumptech.glide.load.engine;

import com.bumptech.glide.load.Key;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

final class Jobs {
  private final Map<Key, EngineJob<?>> jobs = new HashMap<>();
  
  private final Map<Key, EngineJob<?>> onlyCacheJobs = new HashMap<>();
  
  private Map<Key, EngineJob<?>> getJobMap(boolean paramBoolean) {
    Map<Key, EngineJob<?>> map;
    if (paramBoolean) {
      map = this.onlyCacheJobs;
    } else {
      map = this.jobs;
    } 
    return map;
  }
  
  EngineJob<?> get(Key paramKey, boolean paramBoolean) {
    return getJobMap(paramBoolean).get(paramKey);
  }
  
  Map<Key, EngineJob<?>> getAll() {
    return Collections.unmodifiableMap(this.jobs);
  }
  
  void put(Key paramKey, EngineJob<?> paramEngineJob) {
    getJobMap(paramEngineJob.onlyRetrieveFromCache()).put(paramKey, paramEngineJob);
  }
  
  void removeIfCurrent(Key paramKey, EngineJob<?> paramEngineJob) {
    Map<Key, EngineJob<?>> map = getJobMap(paramEngineJob.onlyRetrieveFromCache());
    if (paramEngineJob.equals(map.get(paramKey)))
      map.remove(paramKey); 
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\engine\Jobs.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */