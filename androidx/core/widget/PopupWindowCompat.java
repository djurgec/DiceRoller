package androidx.core.widget;

import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.PopupWindow;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public final class PopupWindowCompat {
  private static final String TAG = "PopupWindowCompatApi21";
  
  private static Method sGetWindowLayoutTypeMethod;
  
  private static boolean sGetWindowLayoutTypeMethodAttempted;
  
  private static Field sOverlapAnchorField;
  
  private static boolean sOverlapAnchorFieldAttempted;
  
  private static Method sSetWindowLayoutTypeMethod;
  
  private static boolean sSetWindowLayoutTypeMethodAttempted;
  
  public static boolean getOverlapAnchor(PopupWindow paramPopupWindow) {
    if (Build.VERSION.SDK_INT >= 23)
      return Api23Impl.getOverlapAnchor(paramPopupWindow); 
    if (Build.VERSION.SDK_INT >= 21) {
      if (!sOverlapAnchorFieldAttempted) {
        try {
          Field field1 = PopupWindow.class.getDeclaredField("mOverlapAnchor");
          sOverlapAnchorField = field1;
          field1.setAccessible(true);
        } catch (NoSuchFieldException noSuchFieldException) {
          Log.i("PopupWindowCompatApi21", "Could not fetch mOverlapAnchor field from PopupWindow", noSuchFieldException);
        } 
        sOverlapAnchorFieldAttempted = true;
      } 
      Field field = sOverlapAnchorField;
      if (field != null)
        try {
          return ((Boolean)field.get(paramPopupWindow)).booleanValue();
        } catch (IllegalAccessException illegalAccessException) {
          Log.i("PopupWindowCompatApi21", "Could not get overlap anchor field in PopupWindow", illegalAccessException);
        }  
    } 
    return false;
  }
  
  public static int getWindowLayoutType(PopupWindow paramPopupWindow) {
    if (Build.VERSION.SDK_INT >= 23)
      return Api23Impl.getWindowLayoutType(paramPopupWindow); 
    if (!sGetWindowLayoutTypeMethodAttempted) {
      try {
        Method method1 = PopupWindow.class.getDeclaredMethod("getWindowLayoutType", new Class[0]);
        sGetWindowLayoutTypeMethod = method1;
        method1.setAccessible(true);
      } catch (Exception exception) {}
      sGetWindowLayoutTypeMethodAttempted = true;
    } 
    Method method = sGetWindowLayoutTypeMethod;
    if (method != null)
      try {
        return ((Integer)method.invoke(paramPopupWindow, new Object[0])).intValue();
      } catch (Exception exception) {} 
    return 0;
  }
  
  public static void setOverlapAnchor(PopupWindow paramPopupWindow, boolean paramBoolean) {
    if (Build.VERSION.SDK_INT >= 23) {
      Api23Impl.setOverlapAnchor(paramPopupWindow, paramBoolean);
    } else if (Build.VERSION.SDK_INT >= 21) {
      if (!sOverlapAnchorFieldAttempted) {
        try {
          Field field1 = PopupWindow.class.getDeclaredField("mOverlapAnchor");
          sOverlapAnchorField = field1;
          field1.setAccessible(true);
        } catch (NoSuchFieldException noSuchFieldException) {
          Log.i("PopupWindowCompatApi21", "Could not fetch mOverlapAnchor field from PopupWindow", noSuchFieldException);
        } 
        sOverlapAnchorFieldAttempted = true;
      } 
      Field field = sOverlapAnchorField;
      if (field != null)
        try {
          field.set(paramPopupWindow, Boolean.valueOf(paramBoolean));
        } catch (IllegalAccessException illegalAccessException) {
          Log.i("PopupWindowCompatApi21", "Could not set overlap anchor field in PopupWindow", illegalAccessException);
        }  
    } 
  }
  
  public static void setWindowLayoutType(PopupWindow paramPopupWindow, int paramInt) {
    if (Build.VERSION.SDK_INT >= 23) {
      Api23Impl.setWindowLayoutType(paramPopupWindow, paramInt);
    } else {
      if (!sSetWindowLayoutTypeMethodAttempted) {
        try {
          Method method1 = PopupWindow.class.getDeclaredMethod("setWindowLayoutType", new Class[] { int.class });
          sSetWindowLayoutTypeMethod = method1;
          method1.setAccessible(true);
        } catch (Exception exception) {}
        sSetWindowLayoutTypeMethodAttempted = true;
      } 
      Method method = sSetWindowLayoutTypeMethod;
      if (method != null)
        try {
          method.invoke(paramPopupWindow, new Object[] { Integer.valueOf(paramInt) });
        } catch (Exception exception) {} 
    } 
  }
  
  public static void showAsDropDown(PopupWindow paramPopupWindow, View paramView, int paramInt1, int paramInt2, int paramInt3) {
    if (Build.VERSION.SDK_INT >= 19) {
      Api19Impl.showAsDropDown(paramPopupWindow, paramView, paramInt1, paramInt2, paramInt3);
    } else {
      int i = paramInt1;
      if ((GravityCompat.getAbsoluteGravity(paramInt3, ViewCompat.getLayoutDirection(paramView)) & 0x7) == 5)
        i = paramInt1 - paramPopupWindow.getWidth() - paramView.getWidth(); 
      paramPopupWindow.showAsDropDown(paramView, i, paramInt2);
    } 
  }
  
  static class Api19Impl {
    static void showAsDropDown(PopupWindow param1PopupWindow, View param1View, int param1Int1, int param1Int2, int param1Int3) {
      param1PopupWindow.showAsDropDown(param1View, param1Int1, param1Int2, param1Int3);
    }
  }
  
  static class Api23Impl {
    static boolean getOverlapAnchor(PopupWindow param1PopupWindow) {
      return param1PopupWindow.getOverlapAnchor();
    }
    
    static int getWindowLayoutType(PopupWindow param1PopupWindow) {
      return param1PopupWindow.getWindowLayoutType();
    }
    
    static void setOverlapAnchor(PopupWindow param1PopupWindow, boolean param1Boolean) {
      param1PopupWindow.setOverlapAnchor(param1Boolean);
    }
    
    static void setWindowLayoutType(PopupWindow param1PopupWindow, int param1Int) {
      param1PopupWindow.setWindowLayoutType(param1Int);
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\widget\PopupWindowCompat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */