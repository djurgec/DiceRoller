package androidx.transition;

import android.os.Build;
import android.view.View;

class ViewUtilsApi23 extends ViewUtilsApi22 {
  private static boolean sTryHiddenSetTransitionVisibility = true;
  
  public void setTransitionVisibility(View paramView, int paramInt) {
    if (Build.VERSION.SDK_INT == 28) {
      super.setTransitionVisibility(paramView, paramInt);
    } else if (sTryHiddenSetTransitionVisibility) {
      try {
        paramView.setTransitionVisibility(paramInt);
      } catch (NoSuchMethodError noSuchMethodError) {
        sTryHiddenSetTransitionVisibility = false;
      } 
    } 
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\transition\ViewUtilsApi23.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */