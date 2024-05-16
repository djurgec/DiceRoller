package androidx.constraintlayout.motion.utils;

import android.os.Build;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import androidx.constraintlayout.core.motion.utils.CurveFit;
import androidx.constraintlayout.core.motion.utils.SplineSet;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.constraintlayout.widget.ConstraintAttribute;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class ViewSpline extends SplineSet {
  private static final String TAG = "ViewSpline";
  
  public static ViewSpline makeCustomSpline(String paramString, SparseArray<ConstraintAttribute> paramSparseArray) {
    return new CustomSet(paramString, paramSparseArray);
  }
  
  public static ViewSpline makeSpline(String paramString) {
    byte b;
    switch (paramString.hashCode()) {
      default:
        b = -1;
        break;
      case 156108012:
        if (paramString.equals("waveOffset")) {
          b = 10;
          break;
        } 
      case 92909918:
        if (paramString.equals("alpha")) {
          b = 0;
          break;
        } 
      case 37232917:
        if (paramString.equals("transitionPathRotate")) {
          b = 7;
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
      case -760884509:
        if (paramString.equals("transformPivotY")) {
          b = 6;
          break;
        } 
      case -760884510:
        if (paramString.equals("transformPivotX")) {
          b = 5;
          break;
        } 
      case -797520672:
        if (paramString.equals("waveVariesBy")) {
          b = 11;
          break;
        } 
      case -908189617:
        if (paramString.equals("scaleY")) {
          b = 9;
          break;
        } 
      case -908189618:
        if (paramString.equals("scaleX")) {
          b = 8;
          break;
        } 
      case -1001078227:
        if (paramString.equals("progress")) {
          b = 15;
          break;
        } 
      case -1225497655:
        if (paramString.equals("translationZ")) {
          b = 14;
          break;
        } 
      case -1225497656:
        if (paramString.equals("translationY")) {
          b = 13;
          break;
        } 
      case -1225497657:
        if (paramString.equals("translationX")) {
          b = 12;
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
      case 15:
        return new ProgressSet();
      case 14:
        return new TranslationZset();
      case 13:
        return new TranslationYset();
      case 12:
        return new TranslationXset();
      case 11:
        return new AlphaSet();
      case 10:
        return new AlphaSet();
      case 9:
        return new ScaleYset();
      case 8:
        return new ScaleXset();
      case 7:
        return new PathRotate();
      case 6:
        return new PivotYset();
      case 5:
        return new PivotXset();
      case 4:
        return new RotationYset();
      case 3:
        return new RotationXset();
      case 2:
        return new RotationSet();
      case 1:
        return new ElevationSet();
      case 0:
        break;
    } 
    return new AlphaSet();
  }
  
  public abstract void setProperty(View paramView, float paramFloat);
  
  static class AlphaSet extends ViewSpline {
    public void setProperty(View param1View, float param1Float) {
      param1View.setAlpha(get(param1Float));
    }
  }
  
  public static class CustomSet extends ViewSpline {
    String mAttributeName;
    
    SparseArray<ConstraintAttribute> mConstraintAttributeList;
    
    float[] mTempValues;
    
    public CustomSet(String param1String, SparseArray<ConstraintAttribute> param1SparseArray) {
      this.mAttributeName = param1String.split(",")[1];
      this.mConstraintAttributeList = param1SparseArray;
    }
    
    public void setPoint(int param1Int, float param1Float) {
      throw new RuntimeException("don't call for custom attribute call setPoint(pos, ConstraintAttribute)");
    }
    
    public void setPoint(int param1Int, ConstraintAttribute param1ConstraintAttribute) {
      this.mConstraintAttributeList.append(param1Int, param1ConstraintAttribute);
    }
    
    public void setProperty(View param1View, float param1Float) {
      this.mCurveFit.getPos(param1Float, this.mTempValues);
      ((ConstraintAttribute)this.mConstraintAttributeList.valueAt(0)).setInterpolatedValue(param1View, this.mTempValues);
    }
    
    public void setup(int param1Int) {
      int j = this.mConstraintAttributeList.size();
      int i = ((ConstraintAttribute)this.mConstraintAttributeList.valueAt(0)).numberOfInterpolatedValues();
      double[] arrayOfDouble1 = new double[j];
      this.mTempValues = new float[i];
      double[][] arrayOfDouble = new double[j][i];
      i = 0;
      while (i < j) {
        int k = this.mConstraintAttributeList.keyAt(i);
        ConstraintAttribute constraintAttribute = (ConstraintAttribute)this.mConstraintAttributeList.valueAt(i);
        arrayOfDouble1[i] = k * 0.01D;
        constraintAttribute.getValuesToInterpolate(this.mTempValues);
        k = 0;
        while (true) {
          float[] arrayOfFloat = this.mTempValues;
          if (k < arrayOfFloat.length) {
            arrayOfDouble[i][k] = arrayOfFloat[k];
            k++;
            continue;
          } 
          i++;
        } 
      } 
      this.mCurveFit = CurveFit.get(param1Int, arrayOfDouble1, arrayOfDouble);
    }
  }
  
  static class ElevationSet extends ViewSpline {
    public void setProperty(View param1View, float param1Float) {
      if (Build.VERSION.SDK_INT >= 21)
        param1View.setElevation(get(param1Float)); 
    }
  }
  
  public static class PathRotate extends ViewSpline {
    public void setPathRotate(View param1View, float param1Float, double param1Double1, double param1Double2) {
      param1View.setRotation(get(param1Float) + (float)Math.toDegrees(Math.atan2(param1Double2, param1Double1)));
    }
    
    public void setProperty(View param1View, float param1Float) {}
  }
  
  static class PivotXset extends ViewSpline {
    public void setProperty(View param1View, float param1Float) {
      param1View.setPivotX(get(param1Float));
    }
  }
  
  static class PivotYset extends ViewSpline {
    public void setProperty(View param1View, float param1Float) {
      param1View.setPivotY(get(param1Float));
    }
  }
  
  static class ProgressSet extends ViewSpline {
    boolean mNoMethod = false;
    
    public void setProperty(View param1View, float param1Float) {
      if (param1View instanceof MotionLayout) {
        ((MotionLayout)param1View).setProgress(get(param1Float));
      } else {
        if (this.mNoMethod)
          return; 
        Method method = null;
        try {
          Method method1 = param1View.getClass().getMethod("setProgress", new Class[] { float.class });
          method = method1;
        } catch (NoSuchMethodException noSuchMethodException) {
          this.mNoMethod = true;
        } 
        if (method != null)
          try {
            method.invoke(param1View, new Object[] { Float.valueOf(get(param1Float)) });
          } catch (IllegalAccessException illegalAccessException) {
            Log.e("ViewSpline", "unable to setProgress", illegalAccessException);
          } catch (InvocationTargetException invocationTargetException) {
            Log.e("ViewSpline", "unable to setProgress", invocationTargetException);
          }  
      } 
    }
  }
  
  static class RotationSet extends ViewSpline {
    public void setProperty(View param1View, float param1Float) {
      param1View.setRotation(get(param1Float));
    }
  }
  
  static class RotationXset extends ViewSpline {
    public void setProperty(View param1View, float param1Float) {
      param1View.setRotationX(get(param1Float));
    }
  }
  
  static class RotationYset extends ViewSpline {
    public void setProperty(View param1View, float param1Float) {
      param1View.setRotationY(get(param1Float));
    }
  }
  
  static class ScaleXset extends ViewSpline {
    public void setProperty(View param1View, float param1Float) {
      param1View.setScaleX(get(param1Float));
    }
  }
  
  static class ScaleYset extends ViewSpline {
    public void setProperty(View param1View, float param1Float) {
      param1View.setScaleY(get(param1Float));
    }
  }
  
  static class TranslationXset extends ViewSpline {
    public void setProperty(View param1View, float param1Float) {
      param1View.setTranslationX(get(param1Float));
    }
  }
  
  static class TranslationYset extends ViewSpline {
    public void setProperty(View param1View, float param1Float) {
      param1View.setTranslationY(get(param1Float));
    }
  }
  
  static class TranslationZset extends ViewSpline {
    public void setProperty(View param1View, float param1Float) {
      if (Build.VERSION.SDK_INT >= 21)
        param1View.setTranslationZ(get(param1Float)); 
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\motio\\utils\ViewSpline.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */