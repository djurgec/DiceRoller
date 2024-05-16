package androidx.dynamicanimation.animation;

import android.os.Looper;
import android.util.AndroidRuntimeException;
import android.view.View;
import androidx.core.view.ViewCompat;
import java.util.ArrayList;

public abstract class DynamicAnimation<T extends DynamicAnimation<T>> implements AnimationHandler.AnimationFrameCallback {
  public static final ViewProperty ALPHA;
  
  public static final float MIN_VISIBLE_CHANGE_ALPHA = 0.00390625F;
  
  public static final float MIN_VISIBLE_CHANGE_PIXELS = 1.0F;
  
  public static final float MIN_VISIBLE_CHANGE_ROTATION_DEGREES = 0.1F;
  
  public static final float MIN_VISIBLE_CHANGE_SCALE = 0.002F;
  
  public static final ViewProperty ROTATION;
  
  public static final ViewProperty ROTATION_X;
  
  public static final ViewProperty ROTATION_Y;
  
  public static final ViewProperty SCALE_X;
  
  public static final ViewProperty SCALE_Y;
  
  public static final ViewProperty SCROLL_X;
  
  public static final ViewProperty SCROLL_Y;
  
  private static final float THRESHOLD_MULTIPLIER = 0.75F;
  
  public static final ViewProperty TRANSLATION_X = new ViewProperty("translationX") {
      public float getValue(View param1View) {
        return param1View.getTranslationX();
      }
      
      public void setValue(View param1View, float param1Float) {
        param1View.setTranslationX(param1Float);
      }
    };
  
  public static final ViewProperty TRANSLATION_Y = new ViewProperty("translationY") {
      public float getValue(View param1View) {
        return param1View.getTranslationY();
      }
      
      public void setValue(View param1View, float param1Float) {
        param1View.setTranslationY(param1Float);
      }
    };
  
  public static final ViewProperty TRANSLATION_Z = new ViewProperty("translationZ") {
      public float getValue(View param1View) {
        return ViewCompat.getTranslationZ(param1View);
      }
      
      public void setValue(View param1View, float param1Float) {
        ViewCompat.setTranslationZ(param1View, param1Float);
      }
    };
  
  private static final float UNSET = 3.4028235E38F;
  
  public static final ViewProperty X;
  
  public static final ViewProperty Y;
  
  public static final ViewProperty Z;
  
  private final ArrayList<OnAnimationEndListener> mEndListeners = new ArrayList<>();
  
  private long mLastFrameTime = 0L;
  
  float mMaxValue = Float.MAX_VALUE;
  
  float mMinValue = -Float.MAX_VALUE;
  
  private float mMinVisibleChange;
  
  final FloatPropertyCompat mProperty = new FloatPropertyCompat("FloatValueHolder") {
      final DynamicAnimation this$0;
      
      final FloatValueHolder val$floatValueHolder;
      
      public float getValue(Object param1Object) {
        return floatValueHolder.getValue();
      }
      
      public void setValue(Object param1Object, float param1Float) {
        floatValueHolder.setValue(param1Float);
      }
    };
  
  boolean mRunning = false;
  
  boolean mStartValueIsSet = false;
  
  final Object mTarget = null;
  
  private final ArrayList<OnAnimationUpdateListener> mUpdateListeners = new ArrayList<>();
  
  float mValue = Float.MAX_VALUE;
  
  float mVelocity = 0.0F;
  
  static {
    SCALE_X = new ViewProperty("scaleX") {
        public float getValue(View param1View) {
          return param1View.getScaleX();
        }
        
        public void setValue(View param1View, float param1Float) {
          param1View.setScaleX(param1Float);
        }
      };
    SCALE_Y = new ViewProperty("scaleY") {
        public float getValue(View param1View) {
          return param1View.getScaleY();
        }
        
        public void setValue(View param1View, float param1Float) {
          param1View.setScaleY(param1Float);
        }
      };
    ROTATION = new ViewProperty("rotation") {
        public float getValue(View param1View) {
          return param1View.getRotation();
        }
        
        public void setValue(View param1View, float param1Float) {
          param1View.setRotation(param1Float);
        }
      };
    ROTATION_X = new ViewProperty("rotationX") {
        public float getValue(View param1View) {
          return param1View.getRotationX();
        }
        
        public void setValue(View param1View, float param1Float) {
          param1View.setRotationX(param1Float);
        }
      };
    ROTATION_Y = new ViewProperty("rotationY") {
        public float getValue(View param1View) {
          return param1View.getRotationY();
        }
        
        public void setValue(View param1View, float param1Float) {
          param1View.setRotationY(param1Float);
        }
      };
    X = new ViewProperty("x") {
        public float getValue(View param1View) {
          return param1View.getX();
        }
        
        public void setValue(View param1View, float param1Float) {
          param1View.setX(param1Float);
        }
      };
    Y = new ViewProperty("y") {
        public float getValue(View param1View) {
          return param1View.getY();
        }
        
        public void setValue(View param1View, float param1Float) {
          param1View.setY(param1Float);
        }
      };
    Z = new ViewProperty("z") {
        public float getValue(View param1View) {
          return ViewCompat.getZ(param1View);
        }
        
        public void setValue(View param1View, float param1Float) {
          ViewCompat.setZ(param1View, param1Float);
        }
      };
    ALPHA = new ViewProperty("alpha") {
        public float getValue(View param1View) {
          return param1View.getAlpha();
        }
        
        public void setValue(View param1View, float param1Float) {
          param1View.setAlpha(param1Float);
        }
      };
    SCROLL_X = new ViewProperty("scrollX") {
        public float getValue(View param1View) {
          return param1View.getScrollX();
        }
        
        public void setValue(View param1View, float param1Float) {
          param1View.setScrollX((int)param1Float);
        }
      };
    SCROLL_Y = new ViewProperty("scrollY") {
        public float getValue(View param1View) {
          return param1View.getScrollY();
        }
        
        public void setValue(View param1View, float param1Float) {
          param1View.setScrollY((int)param1Float);
        }
      };
  }
  
  DynamicAnimation(final FloatValueHolder floatValueHolder) {
    this.mMinVisibleChange = 1.0F;
  }
  
  <K> DynamicAnimation(K paramK, FloatPropertyCompat<K> paramFloatPropertyCompat) {
    if (paramFloatPropertyCompat == ROTATION || paramFloatPropertyCompat == ROTATION_X || paramFloatPropertyCompat == ROTATION_Y) {
      this.mMinVisibleChange = 0.1F;
      return;
    } 
    if (paramFloatPropertyCompat == ALPHA) {
      this.mMinVisibleChange = 0.00390625F;
    } else {
      if (paramFloatPropertyCompat == SCALE_X || paramFloatPropertyCompat == SCALE_Y) {
        this.mMinVisibleChange = 0.00390625F;
        return;
      } 
      this.mMinVisibleChange = 1.0F;
    } 
  }
  
  private void endAnimationInternal(boolean paramBoolean) {
    this.mRunning = false;
    AnimationHandler.getInstance().removeCallback(this);
    this.mLastFrameTime = 0L;
    this.mStartValueIsSet = false;
    for (byte b = 0; b < this.mEndListeners.size(); b++) {
      if (this.mEndListeners.get(b) != null)
        ((OnAnimationEndListener)this.mEndListeners.get(b)).onAnimationEnd(this, paramBoolean, this.mValue, this.mVelocity); 
    } 
    removeNullEntries(this.mEndListeners);
  }
  
  private float getPropertyValue() {
    return this.mProperty.getValue(this.mTarget);
  }
  
  private static <T> void removeEntry(ArrayList<T> paramArrayList, T paramT) {
    int i = paramArrayList.indexOf(paramT);
    if (i >= 0)
      paramArrayList.set(i, null); 
  }
  
  private static <T> void removeNullEntries(ArrayList<T> paramArrayList) {
    for (int i = paramArrayList.size() - 1; i >= 0; i--) {
      if (paramArrayList.get(i) == null)
        paramArrayList.remove(i); 
    } 
  }
  
  private void startAnimationInternal() {
    if (!this.mRunning) {
      this.mRunning = true;
      if (!this.mStartValueIsSet)
        this.mValue = getPropertyValue(); 
      float f = this.mValue;
      if (f <= this.mMaxValue && f >= this.mMinValue) {
        AnimationHandler.getInstance().addAnimationFrameCallback(this, 0L);
      } else {
        throw new IllegalArgumentException("Starting value need to be in between min value and max value");
      } 
    } 
  }
  
  public T addEndListener(OnAnimationEndListener paramOnAnimationEndListener) {
    if (!this.mEndListeners.contains(paramOnAnimationEndListener))
      this.mEndListeners.add(paramOnAnimationEndListener); 
    return (T)this;
  }
  
  public T addUpdateListener(OnAnimationUpdateListener paramOnAnimationUpdateListener) {
    if (!isRunning()) {
      if (!this.mUpdateListeners.contains(paramOnAnimationUpdateListener))
        this.mUpdateListeners.add(paramOnAnimationUpdateListener); 
      return (T)this;
    } 
    throw new UnsupportedOperationException("Error: Update listeners must be added beforethe animation.");
  }
  
  public void cancel() {
    if (Looper.myLooper() == Looper.getMainLooper()) {
      if (this.mRunning)
        endAnimationInternal(true); 
      return;
    } 
    throw new AndroidRuntimeException("Animations may only be canceled on the main thread");
  }
  
  public boolean doAnimationFrame(long paramLong) {
    long l = this.mLastFrameTime;
    if (l == 0L) {
      this.mLastFrameTime = paramLong;
      setPropertyValue(this.mValue);
      return false;
    } 
    this.mLastFrameTime = paramLong;
    boolean bool = updateValueAndVelocity(paramLong - l);
    float f = Math.min(this.mValue, this.mMaxValue);
    this.mValue = f;
    f = Math.max(f, this.mMinValue);
    this.mValue = f;
    setPropertyValue(f);
    if (bool)
      endAnimationInternal(false); 
    return bool;
  }
  
  abstract float getAcceleration(float paramFloat1, float paramFloat2);
  
  public float getMinimumVisibleChange() {
    return this.mMinVisibleChange;
  }
  
  float getValueThreshold() {
    return this.mMinVisibleChange * 0.75F;
  }
  
  abstract boolean isAtEquilibrium(float paramFloat1, float paramFloat2);
  
  public boolean isRunning() {
    return this.mRunning;
  }
  
  public void removeEndListener(OnAnimationEndListener paramOnAnimationEndListener) {
    removeEntry(this.mEndListeners, paramOnAnimationEndListener);
  }
  
  public void removeUpdateListener(OnAnimationUpdateListener paramOnAnimationUpdateListener) {
    removeEntry(this.mUpdateListeners, paramOnAnimationUpdateListener);
  }
  
  public T setMaxValue(float paramFloat) {
    this.mMaxValue = paramFloat;
    return (T)this;
  }
  
  public T setMinValue(float paramFloat) {
    this.mMinValue = paramFloat;
    return (T)this;
  }
  
  public T setMinimumVisibleChange(float paramFloat) {
    if (paramFloat > 0.0F) {
      this.mMinVisibleChange = paramFloat;
      setValueThreshold(0.75F * paramFloat);
      return (T)this;
    } 
    throw new IllegalArgumentException("Minimum visible change must be positive.");
  }
  
  void setPropertyValue(float paramFloat) {
    this.mProperty.setValue(this.mTarget, paramFloat);
    for (byte b = 0; b < this.mUpdateListeners.size(); b++) {
      if (this.mUpdateListeners.get(b) != null)
        ((OnAnimationUpdateListener)this.mUpdateListeners.get(b)).onAnimationUpdate(this, this.mValue, this.mVelocity); 
    } 
    removeNullEntries(this.mUpdateListeners);
  }
  
  public T setStartValue(float paramFloat) {
    this.mValue = paramFloat;
    this.mStartValueIsSet = true;
    return (T)this;
  }
  
  public T setStartVelocity(float paramFloat) {
    this.mVelocity = paramFloat;
    return (T)this;
  }
  
  abstract void setValueThreshold(float paramFloat);
  
  public void start() {
    if (Looper.myLooper() == Looper.getMainLooper()) {
      if (!this.mRunning)
        startAnimationInternal(); 
      return;
    } 
    throw new AndroidRuntimeException("Animations may only be started on the main thread");
  }
  
  abstract boolean updateValueAndVelocity(long paramLong);
  
  static class MassState {
    float mValue;
    
    float mVelocity;
  }
  
  public static interface OnAnimationEndListener {
    void onAnimationEnd(DynamicAnimation param1DynamicAnimation, boolean param1Boolean, float param1Float1, float param1Float2);
  }
  
  public static interface OnAnimationUpdateListener {
    void onAnimationUpdate(DynamicAnimation param1DynamicAnimation, float param1Float1, float param1Float2);
  }
  
  public static abstract class ViewProperty extends FloatPropertyCompat<View> {
    private ViewProperty(String param1String) {
      super(param1String);
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\dynamicanimation\animation\DynamicAnimation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */