package com.google.android.material.animation;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.Log;
import android.util.Property;
import androidx.collection.SimpleArrayMap;
import java.util.ArrayList;
import java.util.List;

public class MotionSpec {
  private static final String TAG = "MotionSpec";
  
  private final SimpleArrayMap<String, PropertyValuesHolder[]> propertyValues = new SimpleArrayMap();
  
  private final SimpleArrayMap<String, MotionTiming> timings = new SimpleArrayMap();
  
  private static void addInfoFromAnimator(MotionSpec paramMotionSpec, Animator paramAnimator) {
    ObjectAnimator objectAnimator;
    if (paramAnimator instanceof ObjectAnimator) {
      objectAnimator = (ObjectAnimator)paramAnimator;
      paramMotionSpec.setPropertyValues(objectAnimator.getPropertyName(), objectAnimator.getValues());
      paramMotionSpec.setTiming(objectAnimator.getPropertyName(), MotionTiming.createFromAnimator((ValueAnimator)objectAnimator));
      return;
    } 
    throw new IllegalArgumentException("Animator must be an ObjectAnimator: " + objectAnimator);
  }
  
  private PropertyValuesHolder[] clonePropertyValuesHolder(PropertyValuesHolder[] paramArrayOfPropertyValuesHolder) {
    PropertyValuesHolder[] arrayOfPropertyValuesHolder = new PropertyValuesHolder[paramArrayOfPropertyValuesHolder.length];
    for (byte b = 0; b < paramArrayOfPropertyValuesHolder.length; b++)
      arrayOfPropertyValuesHolder[b] = paramArrayOfPropertyValuesHolder[b].clone(); 
    return arrayOfPropertyValuesHolder;
  }
  
  public static MotionSpec createFromAttribute(Context paramContext, TypedArray paramTypedArray, int paramInt) {
    if (paramTypedArray.hasValue(paramInt)) {
      paramInt = paramTypedArray.getResourceId(paramInt, 0);
      if (paramInt != 0)
        return createFromResource(paramContext, paramInt); 
    } 
    return null;
  }
  
  public static MotionSpec createFromResource(Context paramContext, int paramInt) {
    try {
      Animator animator = AnimatorInflater.loadAnimator(paramContext, paramInt);
      if (animator instanceof AnimatorSet)
        return createSpecFromAnimators(((AnimatorSet)animator).getChildAnimations()); 
      if (animator != null) {
        ArrayList<Animator> arrayList = new ArrayList();
        this();
        arrayList.add(animator);
        return createSpecFromAnimators(arrayList);
      } 
      return null;
    } catch (Exception exception) {
      Log.w("MotionSpec", "Can't load animation resource ID #0x" + Integer.toHexString(paramInt), exception);
      return null;
    } 
  }
  
  private static MotionSpec createSpecFromAnimators(List<Animator> paramList) {
    MotionSpec motionSpec = new MotionSpec();
    byte b = 0;
    int i = paramList.size();
    while (b < i) {
      addInfoFromAnimator(motionSpec, paramList.get(b));
      b++;
    } 
    return motionSpec;
  }
  
  public boolean equals(Object paramObject) {
    if (this == paramObject)
      return true; 
    if (!(paramObject instanceof MotionSpec))
      return false; 
    paramObject = paramObject;
    return this.timings.equals(((MotionSpec)paramObject).timings);
  }
  
  public <T> ObjectAnimator getAnimator(String paramString, T paramT, Property<T, ?> paramProperty) {
    ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(paramT, getPropertyValues(paramString));
    objectAnimator.setProperty(paramProperty);
    getTiming(paramString).apply((Animator)objectAnimator);
    return objectAnimator;
  }
  
  public PropertyValuesHolder[] getPropertyValues(String paramString) {
    if (hasPropertyValues(paramString))
      return clonePropertyValuesHolder((PropertyValuesHolder[])this.propertyValues.get(paramString)); 
    throw new IllegalArgumentException();
  }
  
  public MotionTiming getTiming(String paramString) {
    if (hasTiming(paramString))
      return (MotionTiming)this.timings.get(paramString); 
    throw new IllegalArgumentException();
  }
  
  public long getTotalDuration() {
    long l = 0L;
    byte b = 0;
    int i = this.timings.size();
    while (b < i) {
      MotionTiming motionTiming = (MotionTiming)this.timings.valueAt(b);
      l = Math.max(l, motionTiming.getDelay() + motionTiming.getDuration());
      b++;
    } 
    return l;
  }
  
  public boolean hasPropertyValues(String paramString) {
    boolean bool;
    if (this.propertyValues.get(paramString) != null) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean hasTiming(String paramString) {
    boolean bool;
    if (this.timings.get(paramString) != null) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public int hashCode() {
    return this.timings.hashCode();
  }
  
  public void setPropertyValues(String paramString, PropertyValuesHolder[] paramArrayOfPropertyValuesHolder) {
    this.propertyValues.put(paramString, paramArrayOfPropertyValuesHolder);
  }
  
  public void setTiming(String paramString, MotionTiming paramMotionTiming) {
    this.timings.put(paramString, paramMotionTiming);
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append('\n');
    stringBuilder.append(getClass().getName());
    stringBuilder.append('{');
    stringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
    stringBuilder.append(" timings: ");
    stringBuilder.append(this.timings);
    stringBuilder.append("}\n");
    return stringBuilder.toString();
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\animation\MotionSpec.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */