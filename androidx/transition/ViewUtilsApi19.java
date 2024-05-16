package androidx.transition;

import android.view.View;

class ViewUtilsApi19 extends ViewUtilsBase {
  private static boolean sTryHiddenTransitionAlpha = true;
  
  public void clearNonTransitionAlpha(View paramView) {}
  
  public float getTransitionAlpha(View paramView) {
    if (sTryHiddenTransitionAlpha)
      try {
        return paramView.getTransitionAlpha();
      } catch (NoSuchMethodError noSuchMethodError) {
        sTryHiddenTransitionAlpha = false;
      }  
    return paramView.getAlpha();
  }
  
  public void saveNonTransitionAlpha(View paramView) {}
  
  public void setTransitionAlpha(View paramView, float paramFloat) {
    if (sTryHiddenTransitionAlpha)
      try {
        paramView.setTransitionAlpha(paramFloat);
        return;
      } catch (NoSuchMethodError noSuchMethodError) {
        sTryHiddenTransitionAlpha = false;
      }  
    paramView.setAlpha(paramFloat);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\transition\ViewUtilsApi19.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */