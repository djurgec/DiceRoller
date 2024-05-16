package androidx.transition;

import android.os.Build;
import android.view.ViewGroup;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class ViewGroupUtils {
  private static Method sGetChildDrawingOrderMethod;
  
  private static boolean sGetChildDrawingOrderMethodFetched;
  
  private static boolean sTryHiddenSuppressLayout = true;
  
  static int getChildDrawingOrder(ViewGroup paramViewGroup, int paramInt) {
    if (Build.VERSION.SDK_INT >= 29)
      return paramViewGroup.getChildDrawingOrder(paramInt); 
    if (!sGetChildDrawingOrderMethodFetched) {
      try {
        Method method1 = ViewGroup.class.getDeclaredMethod("getChildDrawingOrder", new Class[] { int.class, int.class });
        sGetChildDrawingOrderMethod = method1;
        method1.setAccessible(true);
      } catch (NoSuchMethodException noSuchMethodException) {}
      sGetChildDrawingOrderMethodFetched = true;
    } 
    Method method = sGetChildDrawingOrderMethod;
    if (method != null)
      try {
        return ((Integer)method.invoke(paramViewGroup, new Object[] { Integer.valueOf(paramViewGroup.getChildCount()), Integer.valueOf(paramInt) })).intValue();
      } catch (IllegalAccessException illegalAccessException) {
      
      } catch (InvocationTargetException invocationTargetException) {} 
    return paramInt;
  }
  
  static ViewGroupOverlayImpl getOverlay(ViewGroup paramViewGroup) {
    return (ViewGroupOverlayImpl)((Build.VERSION.SDK_INT >= 18) ? new ViewGroupOverlayApi18(paramViewGroup) : ViewGroupOverlayApi14.createFrom(paramViewGroup));
  }
  
  private static void hiddenSuppressLayout(ViewGroup paramViewGroup, boolean paramBoolean) {
    if (sTryHiddenSuppressLayout)
      try {
        paramViewGroup.suppressLayout(paramBoolean);
      } catch (NoSuchMethodError noSuchMethodError) {
        sTryHiddenSuppressLayout = false;
      }  
  }
  
  static void suppressLayout(ViewGroup paramViewGroup, boolean paramBoolean) {
    if (Build.VERSION.SDK_INT >= 29) {
      paramViewGroup.suppressLayout(paramBoolean);
    } else if (Build.VERSION.SDK_INT >= 18) {
      hiddenSuppressLayout(paramViewGroup, paramBoolean);
    } else {
      ViewGroupUtilsApi14.suppressLayout(paramViewGroup, paramBoolean);
    } 
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\transition\ViewGroupUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */