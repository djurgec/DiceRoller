package androidx.transition;

import android.graphics.Matrix;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class GhostViewPlatform implements GhostView {
  private static final String TAG = "GhostViewApi21";
  
  private static Method sAddGhostMethod;
  
  private static boolean sAddGhostMethodFetched;
  
  private static Class<?> sGhostViewClass;
  
  private static boolean sGhostViewClassFetched;
  
  private static Method sRemoveGhostMethod;
  
  private static boolean sRemoveGhostMethodFetched;
  
  private final View mGhostView;
  
  private GhostViewPlatform(View paramView) {
    this.mGhostView = paramView;
  }
  
  static GhostView addGhost(View paramView, ViewGroup paramViewGroup, Matrix paramMatrix) {
    fetchAddGhostMethod();
    if (sAddGhostMethod != null)
      try {
        return new GhostViewPlatform((View)sAddGhostMethod.invoke((Object)null, new Object[] { paramView, paramViewGroup, paramMatrix }));
      } catch (IllegalAccessException illegalAccessException) {
      
      } catch (InvocationTargetException invocationTargetException) {
        throw new RuntimeException(invocationTargetException.getCause());
      }  
    return null;
  }
  
  private static void fetchAddGhostMethod() {
    if (!sAddGhostMethodFetched) {
      try {
        fetchGhostViewClass();
        Method method = sGhostViewClass.getDeclaredMethod("addGhost", new Class[] { View.class, ViewGroup.class, Matrix.class });
        sAddGhostMethod = method;
        method.setAccessible(true);
      } catch (NoSuchMethodException noSuchMethodException) {
        Log.i("GhostViewApi21", "Failed to retrieve addGhost method", noSuchMethodException);
      } 
      sAddGhostMethodFetched = true;
    } 
  }
  
  private static void fetchGhostViewClass() {
    if (!sGhostViewClassFetched) {
      try {
        sGhostViewClass = Class.forName("android.view.GhostView");
      } catch (ClassNotFoundException classNotFoundException) {
        Log.i("GhostViewApi21", "Failed to retrieve GhostView class", classNotFoundException);
      } 
      sGhostViewClassFetched = true;
    } 
  }
  
  private static void fetchRemoveGhostMethod() {
    if (!sRemoveGhostMethodFetched) {
      try {
        fetchGhostViewClass();
        Method method = sGhostViewClass.getDeclaredMethod("removeGhost", new Class[] { View.class });
        sRemoveGhostMethod = method;
        method.setAccessible(true);
      } catch (NoSuchMethodException noSuchMethodException) {
        Log.i("GhostViewApi21", "Failed to retrieve removeGhost method", noSuchMethodException);
      } 
      sRemoveGhostMethodFetched = true;
    } 
  }
  
  static void removeGhost(View paramView) {
    fetchRemoveGhostMethod();
    Method method = sRemoveGhostMethod;
    if (method != null)
      try {
        method.invoke((Object)null, new Object[] { paramView });
      } catch (IllegalAccessException illegalAccessException) {
      
      } catch (InvocationTargetException invocationTargetException) {
        throw new RuntimeException(invocationTargetException.getCause());
      }  
  }
  
  public void reserveEndViewTransition(ViewGroup paramViewGroup, View paramView) {}
  
  public void setVisibility(int paramInt) {
    this.mGhostView.setVisibility(paramInt);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\transition\GhostViewPlatform.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */