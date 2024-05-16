package com.bumptech.glide;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class GlideExperiments {
  private final Map<Class<?>, Experiment> experiments;
  
  GlideExperiments(Builder paramBuilder) {
    this.experiments = Collections.unmodifiableMap(new HashMap<>(paramBuilder.experiments));
  }
  
  <T extends Experiment> T get(Class<T> paramClass) {
    return (T)this.experiments.get(paramClass);
  }
  
  public boolean isEnabled(Class<? extends Experiment> paramClass) {
    return this.experiments.containsKey(paramClass);
  }
  
  static final class Builder {
    private final Map<Class<?>, GlideExperiments.Experiment> experiments = new HashMap<>();
    
    Builder add(GlideExperiments.Experiment param1Experiment) {
      this.experiments.put(param1Experiment.getClass(), param1Experiment);
      return this;
    }
    
    GlideExperiments build() {
      return new GlideExperiments(this);
    }
    
    Builder update(GlideExperiments.Experiment param1Experiment, boolean param1Boolean) {
      if (param1Boolean) {
        add(param1Experiment);
      } else {
        this.experiments.remove(param1Experiment.getClass());
      } 
      return this;
    }
  }
  
  static interface Experiment {}
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\GlideExperiments.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */