package androidx.appcompat.widget;

import android.graphics.Insets;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.ScaleDrawable;
import android.os.Build;
import androidx.appcompat.graphics.drawable.DrawableWrapper;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.graphics.drawable.WrappedDrawable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class DrawableUtils {
  private static final int[] CHECKED_STATE_SET = new int[] { 16842912 };
  
  private static final int[] EMPTY_STATE_SET = new int[0];
  
  public static final Rect INSETS_NONE = new Rect();
  
  public static boolean canSafelyMutateDrawable(Drawable paramDrawable) {
    Drawable[] arrayOfDrawable;
    if (Build.VERSION.SDK_INT < 15 && paramDrawable instanceof android.graphics.drawable.InsetDrawable)
      return false; 
    if (Build.VERSION.SDK_INT < 15 && paramDrawable instanceof android.graphics.drawable.GradientDrawable)
      return false; 
    if (Build.VERSION.SDK_INT < 17 && paramDrawable instanceof android.graphics.drawable.LayerDrawable)
      return false; 
    if (paramDrawable instanceof DrawableContainer) {
      Drawable.ConstantState constantState = paramDrawable.getConstantState();
      if (constantState instanceof DrawableContainer.DrawableContainerState) {
        arrayOfDrawable = ((DrawableContainer.DrawableContainerState)constantState).getChildren();
        int i = arrayOfDrawable.length;
        for (byte b = 0; b < i; b++) {
          if (!canSafelyMutateDrawable(arrayOfDrawable[b]))
            return false; 
        } 
      } 
    } else {
      if (arrayOfDrawable instanceof WrappedDrawable)
        return canSafelyMutateDrawable(((WrappedDrawable)arrayOfDrawable).getWrappedDrawable()); 
      if (arrayOfDrawable instanceof DrawableWrapper)
        return canSafelyMutateDrawable(((DrawableWrapper)arrayOfDrawable).getWrappedDrawable()); 
      if (arrayOfDrawable instanceof ScaleDrawable)
        return canSafelyMutateDrawable(((ScaleDrawable)arrayOfDrawable).getDrawable()); 
    } 
    return true;
  }
  
  static void fixDrawable(Drawable paramDrawable) {
    String str = paramDrawable.getClass().getName();
    if (Build.VERSION.SDK_INT == 21 && "android.graphics.drawable.VectorDrawable".equals(str)) {
      forceDrawableStateChange(paramDrawable);
    } else if (Build.VERSION.SDK_INT >= 29 && Build.VERSION.SDK_INT < 31 && "android.graphics.drawable.ColorStateListDrawable".equals(str)) {
      forceDrawableStateChange(paramDrawable);
    } 
  }
  
  private static void forceDrawableStateChange(Drawable paramDrawable) {
    int[] arrayOfInt = paramDrawable.getState();
    if (arrayOfInt == null || arrayOfInt.length == 0) {
      paramDrawable.setState(CHECKED_STATE_SET);
    } else {
      paramDrawable.setState(EMPTY_STATE_SET);
    } 
    paramDrawable.setState(arrayOfInt);
  }
  
  public static Rect getOpticalBounds(Drawable paramDrawable) {
    Insets insets;
    if (Build.VERSION.SDK_INT >= 29) {
      insets = Api29Impl.getOpticalInsets(paramDrawable);
      return new Rect(insets.left, insets.top, insets.right, insets.bottom);
    } 
    return (Build.VERSION.SDK_INT >= 18) ? Api18Impl.getOpticalInsets(DrawableCompat.unwrap((Drawable)insets)) : INSETS_NONE;
  }
  
  public static PorterDuff.Mode parseTintMode(int paramInt, PorterDuff.Mode paramMode) {
    switch (paramInt) {
      default:
        return paramMode;
      case 16:
        return PorterDuff.Mode.ADD;
      case 15:
        return PorterDuff.Mode.SCREEN;
      case 14:
        return PorterDuff.Mode.MULTIPLY;
      case 9:
        return PorterDuff.Mode.SRC_ATOP;
      case 5:
        return PorterDuff.Mode.SRC_IN;
      case 3:
        break;
    } 
    return PorterDuff.Mode.SRC_OVER;
  }
  
  static class Api18Impl {
    private static final Field sBottom;
    
    private static final Method sGetOpticalInsets;
    
    private static final Field sLeft;
    
    private static final boolean sReflectionSuccessful;
    
    private static final Field sRight;
    
    private static final Field sTop;
    
    static {
      Method method5 = null;
      Method method6 = null;
      Method method1 = null;
      Field field1 = null;
      Field field20 = null;
      Field field19 = null;
      Field field2 = null;
      Field field18 = null;
      Field field17 = null;
      Field field3 = null;
      Field field15 = null;
      Field field16 = null;
      Field field14 = null;
      Field field13 = null;
      boolean bool1 = false;
      boolean bool2 = false;
      Method method3 = method1;
      Field field11 = field19;
      Field field7 = field17;
      Field field5 = field16;
      Method method4 = method5;
      Field field12 = field1;
      Field field8 = field2;
      Field field6 = field3;
      Method method2 = method6;
      Field field4 = field20;
      Field field9 = field18;
      Field field10 = field15;
      try {
        Class<?> clazz = Class.forName("android.graphics.Insets");
        method3 = method1;
        field11 = field19;
        field7 = field17;
        field5 = field16;
        method4 = method5;
        field12 = field1;
        field8 = field2;
        field6 = field3;
        method2 = method6;
        field4 = field20;
        field9 = field18;
        field10 = field15;
        method1 = Drawable.class.getMethod("getOpticalInsets", new Class[0]);
        method3 = method1;
        field11 = field19;
        field7 = field17;
        field5 = field16;
        method4 = method1;
        field12 = field1;
        field8 = field2;
        field6 = field3;
        method2 = method1;
        field4 = field20;
        field9 = field18;
        field10 = field15;
        field1 = clazz.getField("left");
        method3 = method1;
        field11 = field1;
        field7 = field17;
        field5 = field16;
        method4 = method1;
        field12 = field1;
        field8 = field2;
        field6 = field3;
        method2 = method1;
        field4 = field1;
        field9 = field18;
        field10 = field15;
        field2 = clazz.getField("top");
        method3 = method1;
        field11 = field1;
        field7 = field2;
        field5 = field16;
        method4 = method1;
        field12 = field1;
        field8 = field2;
        field6 = field3;
        method2 = method1;
        field4 = field1;
        field9 = field2;
        field10 = field15;
        field3 = clazz.getField("right");
        method3 = method1;
        field11 = field1;
        field7 = field2;
        field5 = field3;
        method4 = method1;
        field12 = field1;
        field8 = field2;
        field6 = field3;
        method2 = method1;
        field4 = field1;
        field9 = field2;
        field10 = field3;
        field15 = clazz.getField("bottom");
        field5 = field15;
        bool1 = true;
      } catch (NoSuchMethodException noSuchMethodException) {
        bool1 = bool2;
        field5 = field13;
        field3 = field10;
        field2 = field9;
        field1 = field4;
        Method method = method2;
      } catch (ClassNotFoundException classNotFoundException) {
        Method method = method4;
        field1 = field12;
        field2 = field8;
        field3 = field6;
        field5 = field13;
        bool1 = bool2;
      } catch (NoSuchFieldException noSuchFieldException) {
        method1 = method3;
        field1 = field11;
        field2 = field7;
        field3 = field5;
        field5 = field14;
      } 
      if (bool1) {
        sGetOpticalInsets = method1;
        sLeft = field1;
        sTop = field2;
        sRight = field3;
        sBottom = field5;
        sReflectionSuccessful = true;
      } else {
        sGetOpticalInsets = null;
        sLeft = null;
        sTop = null;
        sRight = null;
        sBottom = null;
        sReflectionSuccessful = false;
      } 
    }
    
    static Rect getOpticalInsets(Drawable param1Drawable) {
      if (Build.VERSION.SDK_INT < 29 && sReflectionSuccessful)
        try {
          Object object = sGetOpticalInsets.invoke(param1Drawable, new Object[0]);
          if (object != null)
            return new Rect(sLeft.getInt(object), sTop.getInt(object), sRight.getInt(object), sBottom.getInt(object)); 
        } catch (IllegalAccessException illegalAccessException) {
        
        } catch (InvocationTargetException invocationTargetException) {} 
      return DrawableUtils.INSETS_NONE;
    }
  }
  
  static class Api29Impl {
    static Insets getOpticalInsets(Drawable param1Drawable) {
      return param1Drawable.getOpticalInsets();
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\appcompat\widget\DrawableUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */