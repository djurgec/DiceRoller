package androidx.transition;

import android.graphics.Matrix;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;

class GhostViewUtils {
  static GhostView addGhost(View paramView, ViewGroup paramViewGroup, Matrix paramMatrix) {
    return (Build.VERSION.SDK_INT == 28) ? GhostViewPlatform.addGhost(paramView, paramViewGroup, paramMatrix) : GhostViewPort.addGhost(paramView, paramViewGroup, paramMatrix);
  }
  
  static void removeGhost(View paramView) {
    if (Build.VERSION.SDK_INT == 28) {
      GhostViewPlatform.removeGhost(paramView);
    } else {
      GhostViewPort.removeGhost(paramView);
    } 
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\transition\GhostViewUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */