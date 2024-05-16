package androidx.core.view;

import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowInsetsController;

public final class WindowCompat {
  public static final int FEATURE_ACTION_BAR = 8;
  
  public static final int FEATURE_ACTION_BAR_OVERLAY = 9;
  
  public static final int FEATURE_ACTION_MODE_OVERLAY = 10;
  
  public static WindowInsetsControllerCompat getInsetsController(Window paramWindow, View paramView) {
    return (Build.VERSION.SDK_INT >= 30) ? Impl30.getInsetsController(paramWindow) : new WindowInsetsControllerCompat(paramWindow, paramView);
  }
  
  public static <T extends View> T requireViewById(Window paramWindow, int paramInt) {
    if (Build.VERSION.SDK_INT >= 28)
      return (T)paramWindow.requireViewById(paramInt); 
    View view = paramWindow.findViewById(paramInt);
    if (view != null)
      return (T)view; 
    throw new IllegalArgumentException("ID does not reference a View inside this Window");
  }
  
  public static void setDecorFitsSystemWindows(Window paramWindow, boolean paramBoolean) {
    if (Build.VERSION.SDK_INT >= 30) {
      Impl30.setDecorFitsSystemWindows(paramWindow, paramBoolean);
    } else if (Build.VERSION.SDK_INT >= 16) {
      Impl16.setDecorFitsSystemWindows(paramWindow, paramBoolean);
    } 
  }
  
  private static class Impl16 {
    static void setDecorFitsSystemWindows(Window param1Window, boolean param1Boolean) {
      View view = param1Window.getDecorView();
      int i = view.getSystemUiVisibility();
      if (param1Boolean) {
        i &= 0xFFFFF8FF;
      } else {
        i |= 0x700;
      } 
      view.setSystemUiVisibility(i);
    }
  }
  
  private static class Impl30 {
    static WindowInsetsControllerCompat getInsetsController(Window param1Window) {
      WindowInsetsController windowInsetsController = param1Window.getInsetsController();
      return (windowInsetsController != null) ? WindowInsetsControllerCompat.toWindowInsetsControllerCompat(windowInsetsController) : null;
    }
    
    static void setDecorFitsSystemWindows(Window param1Window, boolean param1Boolean) {
      param1Window.setDecorFitsSystemWindows(param1Boolean);
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\view\WindowCompat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */