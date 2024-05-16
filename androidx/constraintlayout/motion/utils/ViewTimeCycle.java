package androidx.constraintlayout.motion.utils;

import android.os.Build;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import androidx.constraintlayout.core.motion.utils.CurveFit;
import androidx.constraintlayout.core.motion.utils.KeyCache;
import androidx.constraintlayout.core.motion.utils.TimeCycleSplineSet;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.constraintlayout.widget.ConstraintAttribute;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class ViewTimeCycle extends TimeCycleSplineSet {
  private static final String TAG = "ViewTimeCycle";
  
  public static ViewTimeCycle makeCustomSpline(String paramString, SparseArray<ConstraintAttribute> paramSparseArray) {
    return new CustomSet(paramString, paramSparseArray);
  }
  
  public static ViewTimeCycle makeSpline(String paramString, long paramLong) {
    ProgressSet progressSet;
    TranslationZset translationZset;
    TranslationYset translationYset;
    TranslationXset translationXset;
    ScaleYset scaleYset;
    ScaleXset scaleXset;
    PathRotate pathRotate;
    RotationYset rotationYset;
    RotationXset rotationXset;
    RotationSet rotationSet;
    ElevationSet elevationSet;
    byte b;
    switch (paramString.hashCode()) {
      default:
        b = -1;
        break;
      case 92909918:
        if (paramString.equals("alpha")) {
          b = 0;
          break;
        } 
      case 37232917:
        if (paramString.equals("transitionPathRotate")) {
          b = 5;
          break;
        } 
      case -4379043:
        if (paramString.equals("elevation")) {
          b = 1;
          break;
        } 
      case -40300674:
        if (paramString.equals("rotation")) {
          b = 2;
          break;
        } 
      case -908189617:
        if (paramString.equals("scaleY")) {
          b = 7;
          break;
        } 
      case -908189618:
        if (paramString.equals("scaleX")) {
          b = 6;
          break;
        } 
      case -1001078227:
        if (paramString.equals("progress")) {
          b = 11;
          break;
        } 
      case -1225497655:
        if (paramString.equals("translationZ")) {
          b = 10;
          break;
        } 
      case -1225497656:
        if (paramString.equals("translationY")) {
          b = 9;
          break;
        } 
      case -1225497657:
        if (paramString.equals("translationX")) {
          b = 8;
          break;
        } 
      case -1249320805:
        if (paramString.equals("rotationY")) {
          b = 4;
          break;
        } 
      case -1249320806:
        if (paramString.equals("rotationX")) {
          b = 3;
          break;
        } 
    } 
    switch (b) {
      default:
        return null;
      case 11:
        progressSet = new ProgressSet();
        progressSet.setStartTime(paramLong);
        return progressSet;
      case 10:
        translationZset = new TranslationZset();
        translationZset.setStartTime(paramLong);
        return translationZset;
      case 9:
        translationYset = new TranslationYset();
        translationYset.setStartTime(paramLong);
        return translationYset;
      case 8:
        translationXset = new TranslationXset();
        translationXset.setStartTime(paramLong);
        return translationXset;
      case 7:
        scaleYset = new ScaleYset();
        scaleYset.setStartTime(paramLong);
        return scaleYset;
      case 6:
        scaleXset = new ScaleXset();
        scaleXset.setStartTime(paramLong);
        return scaleXset;
      case 5:
        pathRotate = new PathRotate();
        pathRotate.setStartTime(paramLong);
        return pathRotate;
      case 4:
        rotationYset = new RotationYset();
        rotationYset.setStartTime(paramLong);
        return rotationYset;
      case 3:
        rotationXset = new RotationXset();
        rotationXset.setStartTime(paramLong);
        return rotationXset;
      case 2:
        rotationSet = new RotationSet();
        rotationSet.setStartTime(paramLong);
        return rotationSet;
      case 1:
        elevationSet = new ElevationSet();
        elevationSet.setStartTime(paramLong);
        return elevationSet;
      case 0:
        break;
    } 
    AlphaSet alphaSet = new AlphaSet();
    alphaSet.setStartTime(paramLong);
    return alphaSet;
  }
  
  public float get(float paramFloat, long paramLong, View paramView, KeyCache paramKeyCache) {
    this.mCurveFit.getPos(paramFloat, this.mCache);
    float f1 = this.mCache[1];
    boolean bool = false;
    if (f1 == 0.0F) {
      this.mContinue = false;
      return this.mCache[2];
    } 
    if (Float.isNaN(this.last_cycle)) {
      this.last_cycle = paramKeyCache.getFloatValue(paramView, this.mType, 0);
      if (Float.isNaN(this.last_cycle))
        this.last_cycle = 0.0F; 
    } 
    long l = this.last_time;
    this.last_cycle = (float)((this.last_cycle + (paramLong - l) * 1.0E-9D * f1) % 1.0D);
    paramKeyCache.setFloatValue(paramView, this.mType, 0, this.last_cycle);
    this.last_time = paramLong;
    float f2 = this.mCache[0];
    paramFloat = calcWave(this.last_cycle);
    float f3 = this.mCache[2];
    if (f2 != 0.0F || f1 != 0.0F)
      bool = true; 
    this.mContinue = bool;
    return f2 * paramFloat + f3;
  }
  
  public abstract boolean setProperty(View paramView, float paramFloat, long paramLong, KeyCache paramKeyCache);
  
  static class AlphaSet extends ViewTimeCycle {
    public boolean setProperty(View param1View, float param1Float, long param1Long, KeyCache param1KeyCache) {
      param1View.setAlpha(get(param1Float, param1Long, param1View, param1KeyCache));
      return this.mContinue;
    }
  }
  
  public static class CustomSet extends ViewTimeCycle {
    String mAttributeName;
    
    float[] mCache;
    
    SparseArray<ConstraintAttribute> mConstraintAttributeList;
    
    float[] mTempValues;
    
    SparseArray<float[]> mWaveProperties = new SparseArray();
    
    public CustomSet(String param1String, SparseArray<ConstraintAttribute> param1SparseArray) {
      this.mAttributeName = param1String.split(",")[1];
      this.mConstraintAttributeList = param1SparseArray;
    }
    
    public void setPoint(int param1Int1, float param1Float1, float param1Float2, int param1Int2, float param1Float3) {
      throw new RuntimeException("don't call for custom attribute call setPoint(pos, ConstraintAttribute,...)");
    }
    
    public void setPoint(int param1Int1, ConstraintAttribute param1ConstraintAttribute, float param1Float1, int param1Int2, float param1Float2) {
      this.mConstraintAttributeList.append(param1Int1, param1ConstraintAttribute);
      this.mWaveProperties.append(param1Int1, new float[] { param1Float1, param1Float2 });
      this.mWaveShape = Math.max(this.mWaveShape, param1Int2);
    }
    
    public boolean setProperty(View param1View, float param1Float, long param1Long, KeyCache param1KeyCache) {
      this.mCurveFit.getPos(param1Float, this.mTempValues);
      float[] arrayOfFloat = this.mTempValues;
      param1Float = arrayOfFloat[arrayOfFloat.length - 2];
      float f1 = arrayOfFloat[arrayOfFloat.length - 1];
      long l = this.last_time;
      if (Float.isNaN(this.last_cycle)) {
        this.last_cycle = param1KeyCache.getFloatValue(param1View, this.mAttributeName, 0);
        if (Float.isNaN(this.last_cycle))
          this.last_cycle = 0.0F; 
      } 
      this.last_cycle = (float)((this.last_cycle + (param1Long - l) * 1.0E-9D * param1Float) % 1.0D);
      this.last_time = param1Long;
      float f2 = calcWave(this.last_cycle);
      this.mContinue = false;
      for (byte b = 0; b < this.mCache.length; b++) {
        boolean bool1;
        boolean bool2 = this.mContinue;
        if (this.mTempValues[b] != 0.0D) {
          bool1 = true;
        } else {
          bool1 = false;
        } 
        this.mContinue = bool2 | bool1;
        this.mCache[b] = this.mTempValues[b] * f2 + f1;
      } 
      ((ConstraintAttribute)this.mConstraintAttributeList.valueAt(0)).setInterpolatedValue(param1View, this.mCache);
      if (param1Float != 0.0F)
        this.mContinue = true; 
      return this.mContinue;
    }
    
    public void setup(int param1Int) {
      int j = this.mConstraintAttributeList.size();
      int i = ((ConstraintAttribute)this.mConstraintAttributeList.valueAt(0)).numberOfInterpolatedValues();
      double[] arrayOfDouble1 = new double[j];
      this.mTempValues = new float[i + 2];
      this.mCache = new float[i];
      double[][] arrayOfDouble = new double[j][i + 2];
      byte b = 0;
      while (b < j) {
        int k = this.mConstraintAttributeList.keyAt(b);
        ConstraintAttribute constraintAttribute = (ConstraintAttribute)this.mConstraintAttributeList.valueAt(b);
        float[] arrayOfFloat = (float[])this.mWaveProperties.valueAt(b);
        arrayOfDouble1[b] = k * 0.01D;
        constraintAttribute.getValuesToInterpolate(this.mTempValues);
        k = 0;
        while (true) {
          float[] arrayOfFloat1 = this.mTempValues;
          if (k < arrayOfFloat1.length) {
            arrayOfDouble[b][k] = arrayOfFloat1[k];
            k++;
            continue;
          } 
          arrayOfDouble[b][i] = arrayOfFloat[0];
          arrayOfDouble[b][i + 1] = arrayOfFloat[1];
          b++;
        } 
      } 
      this.mCurveFit = CurveFit.get(param1Int, arrayOfDouble1, arrayOfDouble);
    }
  }
  
  static class ElevationSet extends ViewTimeCycle {
    public boolean setProperty(View param1View, float param1Float, long param1Long, KeyCache param1KeyCache) {
      if (Build.VERSION.SDK_INT >= 21)
        param1View.setElevation(get(param1Float, param1Long, param1View, param1KeyCache)); 
      return this.mContinue;
    }
  }
  
  public static class PathRotate extends ViewTimeCycle {
    public boolean setPathRotate(View param1View, KeyCache param1KeyCache, float param1Float, long param1Long, double param1Double1, double param1Double2) {
      param1View.setRotation(get(param1Float, param1Long, param1View, param1KeyCache) + (float)Math.toDegrees(Math.atan2(param1Double2, param1Double1)));
      return this.mContinue;
    }
    
    public boolean setProperty(View param1View, float param1Float, long param1Long, KeyCache param1KeyCache) {
      return this.mContinue;
    }
  }
  
  static class ProgressSet extends ViewTimeCycle {
    boolean mNoMethod = false;
    
    public boolean setProperty(View param1View, float param1Float, long param1Long, KeyCache param1KeyCache) {
      if (param1View instanceof MotionLayout) {
        ((MotionLayout)param1View).setProgress(get(param1Float, param1Long, param1View, param1KeyCache));
      } else {
        if (this.mNoMethod)
          return false; 
        try {
          Method method = param1View.getClass().getMethod("setProgress", new Class[] { float.class });
        } catch (NoSuchMethodException noSuchMethodException) {
          this.mNoMethod = true;
          noSuchMethodException = null;
        } 
        if (noSuchMethodException != null)
          try {
            noSuchMethodException.invoke(param1View, new Object[] { Float.valueOf(get(param1Float, param1Long, param1View, param1KeyCache)) });
          } catch (IllegalAccessException illegalAccessException) {
            Log.e("ViewTimeCycle", "unable to setProgress", illegalAccessException);
          } catch (InvocationTargetException invocationTargetException) {
            Log.e("ViewTimeCycle", "unable to setProgress", invocationTargetException);
          }  
      } 
      return this.mContinue;
    }
  }
  
  static class RotationSet extends ViewTimeCycle {
    public boolean setProperty(View param1View, float param1Float, long param1Long, KeyCache param1KeyCache) {
      param1View.setRotation(get(param1Float, param1Long, param1View, param1KeyCache));
      return this.mContinue;
    }
  }
  
  static class RotationXset extends ViewTimeCycle {
    public boolean setProperty(View param1View, float param1Float, long param1Long, KeyCache param1KeyCache) {
      param1View.setRotationX(get(param1Float, param1Long, param1View, param1KeyCache));
      return this.mContinue;
    }
  }
  
  static class RotationYset extends ViewTimeCycle {
    public boolean setProperty(View param1View, float param1Float, long param1Long, KeyCache param1KeyCache) {
      param1View.setRotationY(get(param1Float, param1Long, param1View, param1KeyCache));
      return this.mContinue;
    }
  }
  
  static class ScaleXset extends ViewTimeCycle {
    public boolean setProperty(View param1View, float param1Float, long param1Long, KeyCache param1KeyCache) {
      param1View.setScaleX(get(param1Float, param1Long, param1View, param1KeyCache));
      return this.mContinue;
    }
  }
  
  static class ScaleYset extends ViewTimeCycle {
    public boolean setProperty(View param1View, float param1Float, long param1Long, KeyCache param1KeyCache) {
      param1View.setScaleY(get(param1Float, param1Long, param1View, param1KeyCache));
      return this.mContinue;
    }
  }
  
  static class TranslationXset extends ViewTimeCycle {
    public boolean setProperty(View param1View, float param1Float, long param1Long, KeyCache param1KeyCache) {
      param1View.setTranslationX(get(param1Float, param1Long, param1View, param1KeyCache));
      return this.mContinue;
    }
  }
  
  static class TranslationYset extends ViewTimeCycle {
    public boolean setProperty(View param1View, float param1Float, long param1Long, KeyCache param1KeyCache) {
      param1View.setTranslationY(get(param1Float, param1Long, param1View, param1KeyCache));
      return this.mContinue;
    }
  }
  
  static class TranslationZset extends ViewTimeCycle {
    public boolean setProperty(View param1View, float param1Float, long param1Long, KeyCache param1KeyCache) {
      if (Build.VERSION.SDK_INT >= 21)
        param1View.setTranslationZ(get(param1Float, param1Long, param1View, param1KeyCache)); 
      return this.mContinue;
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\motio\\utils\ViewTimeCycle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */