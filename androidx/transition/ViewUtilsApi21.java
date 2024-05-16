package androidx.transition;

import android.graphics.Matrix;
import android.view.View;

class ViewUtilsApi21 extends ViewUtilsApi19 {
  private static boolean sTryHiddenSetAnimationMatrix = true;
  
  private static boolean sTryHiddenTransformMatrixToGlobal = true;
  
  private static boolean sTryHiddenTransformMatrixToLocal = true;
  
  public void setAnimationMatrix(View paramView, Matrix paramMatrix) {
    if (sTryHiddenSetAnimationMatrix)
      try {
        paramView.setAnimationMatrix(paramMatrix);
      } catch (NoSuchMethodError noSuchMethodError) {
        sTryHiddenSetAnimationMatrix = false;
      }  
  }
  
  public void transformMatrixToGlobal(View paramView, Matrix paramMatrix) {
    if (sTryHiddenTransformMatrixToGlobal)
      try {
        paramView.transformMatrixToGlobal(paramMatrix);
      } catch (NoSuchMethodError noSuchMethodError) {
        sTryHiddenTransformMatrixToGlobal = false;
      }  
  }
  
  public void transformMatrixToLocal(View paramView, Matrix paramMatrix) {
    if (sTryHiddenTransformMatrixToLocal)
      try {
        paramView.transformMatrixToLocal(paramMatrix);
      } catch (NoSuchMethodError noSuchMethodError) {
        sTryHiddenTransformMatrixToLocal = false;
      }  
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\transition\ViewUtilsApi21.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */