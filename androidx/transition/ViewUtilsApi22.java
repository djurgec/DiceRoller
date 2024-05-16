package androidx.transition;

import android.view.View;

class ViewUtilsApi22 extends ViewUtilsApi21 {
  private static boolean sTryHiddenSetLeftTopRightBottom = true;
  
  public void setLeftTopRightBottom(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    if (sTryHiddenSetLeftTopRightBottom)
      try {
        paramView.setLeftTopRightBottom(paramInt1, paramInt2, paramInt3, paramInt4);
      } catch (NoSuchMethodError noSuchMethodError) {
        sTryHiddenSetLeftTopRightBottom = false;
      }  
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\transition\ViewUtilsApi22.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */