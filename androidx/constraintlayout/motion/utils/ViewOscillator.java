package androidx.constraintlayout.motion.utils;

import android.os.Build;
import android.util.Log;
import android.view.View;
import androidx.constraintlayout.core.motion.utils.KeyCycleOscillator;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.constraintlayout.widget.ConstraintAttribute;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class ViewOscillator extends KeyCycleOscillator {
  private static final String TAG = "ViewOscillator";
  
  public static ViewOscillator makeSpline(String paramString) {
    if (paramString.startsWith("CUSTOM"))
      return new CustomSet(); 
    byte b = -1;
    switch (paramString.hashCode()) {
      case 156108012:
        if (paramString.equals("waveOffset"))
          b = 8; 
        break;
      case 92909918:
        if (paramString.equals("alpha"))
          b = 0; 
        break;
      case 37232917:
        if (paramString.equals("transitionPathRotate"))
          b = 5; 
        break;
      case -4379043:
        if (paramString.equals("elevation"))
          b = 1; 
        break;
      case -40300674:
        if (paramString.equals("rotation"))
          b = 2; 
        break;
      case -797520672:
        if (paramString.equals("waveVariesBy"))
          b = 9; 
        break;
      case -908189617:
        if (paramString.equals("scaleY"))
          b = 7; 
        break;
      case -908189618:
        if (paramString.equals("scaleX"))
          b = 6; 
        break;
      case -1001078227:
        if (paramString.equals("progress"))
          b = 13; 
        break;
      case -1225497655:
        if (paramString.equals("translationZ"))
          b = 12; 
        break;
      case -1225497656:
        if (paramString.equals("translationY"))
          b = 11; 
        break;
      case -1225497657:
        if (paramString.equals("translationX"))
          b = 10; 
        break;
      case -1249320805:
        if (paramString.equals("rotationY"))
          b = 4; 
        break;
      case -1249320806:
        if (paramString.equals("rotationX"))
          b = 3; 
        break;
    } 
    switch (b) {
      default:
        return null;
      case 13:
        return new ProgressSet();
      case 12:
        return new TranslationZset();
      case 11:
        return new TranslationYset();
      case 10:
        return new TranslationXset();
      case 9:
        return new AlphaSet();
      case 8:
        return new AlphaSet();
      case 7:
        return new ScaleYset();
      case 6:
        return new ScaleXset();
      case 5:
        return new PathRotateSet();
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
  
  static class AlphaSet extends ViewOscillator {
    public void setProperty(View param1View, float param1Float) {
      param1View.setAlpha(get(param1Float));
    }
  }
  
  static class CustomSet extends ViewOscillator {
    protected ConstraintAttribute mCustom;
    
    float[] value = new float[1];
    
    protected void setCustom(Object param1Object) {
      this.mCustom = (ConstraintAttribute)param1Object;
    }
    
    public void setProperty(View param1View, float param1Float) {
      this.value[0] = get(param1Float);
      this.mCustom.setInterpolatedValue(param1View, this.value);
    }
  }
  
  static class ElevationSet extends ViewOscillator {
    public void setProperty(View param1View, float param1Float) {
      if (Build.VERSION.SDK_INT >= 21)
        param1View.setElevation(get(param1Float)); 
    }
  }
  
  public static class PathRotateSet extends ViewOscillator {
    public void setPathRotate(View param1View, float param1Float, double param1Double1, double param1Double2) {
      param1View.setRotation(get(param1Float) + (float)Math.toDegrees(Math.atan2(param1Double2, param1Double1)));
    }
    
    public void setProperty(View param1View, float param1Float) {}
  }
  
  static class ProgressSet extends ViewOscillator {
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
            Log.e("ViewOscillator", "unable to setProgress", illegalAccessException);
          } catch (InvocationTargetException invocationTargetException) {
            Log.e("ViewOscillator", "unable to setProgress", invocationTargetException);
          }  
      } 
    }
  }
  
  static class RotationSet extends ViewOscillator {
    public void setProperty(View param1View, float param1Float) {
      param1View.setRotation(get(param1Float));
    }
  }
  
  static class RotationXset extends ViewOscillator {
    public void setProperty(View param1View, float param1Float) {
      param1View.setRotationX(get(param1Float));
    }
  }
  
  static class RotationYset extends ViewOscillator {
    public void setProperty(View param1View, float param1Float) {
      param1View.setRotationY(get(param1Float));
    }
  }
  
  static class ScaleXset extends ViewOscillator {
    public void setProperty(View param1View, float param1Float) {
      param1View.setScaleX(get(param1Float));
    }
  }
  
  static class ScaleYset extends ViewOscillator {
    public void setProperty(View param1View, float param1Float) {
      param1View.setScaleY(get(param1Float));
    }
  }
  
  static class TranslationXset extends ViewOscillator {
    public void setProperty(View param1View, float param1Float) {
      param1View.setTranslationX(get(param1Float));
    }
  }
  
  static class TranslationYset extends ViewOscillator {
    public void setProperty(View param1View, float param1Float) {
      param1View.setTranslationY(get(param1Float));
    }
  }
  
  static class TranslationZset extends ViewOscillator {
    public void setProperty(View param1View, float param1Float) {
      if (Build.VERSION.SDK_INT >= 21)
        param1View.setTranslationZ(get(param1Float)); 
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\motio\\utils\ViewOscillator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */