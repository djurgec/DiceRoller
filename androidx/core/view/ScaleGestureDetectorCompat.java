package androidx.core.view;

import android.os.Build;
import android.view.ScaleGestureDetector;

public final class ScaleGestureDetectorCompat {
  public static boolean isQuickScaleEnabled(ScaleGestureDetector paramScaleGestureDetector) {
    return (Build.VERSION.SDK_INT >= 19) ? paramScaleGestureDetector.isQuickScaleEnabled() : false;
  }
  
  @Deprecated
  public static boolean isQuickScaleEnabled(Object paramObject) {
    return isQuickScaleEnabled((ScaleGestureDetector)paramObject);
  }
  
  public static void setQuickScaleEnabled(ScaleGestureDetector paramScaleGestureDetector, boolean paramBoolean) {
    if (Build.VERSION.SDK_INT >= 19)
      paramScaleGestureDetector.setQuickScaleEnabled(paramBoolean); 
  }
  
  @Deprecated
  public static void setQuickScaleEnabled(Object paramObject, boolean paramBoolean) {
    setQuickScaleEnabled((ScaleGestureDetector)paramObject, paramBoolean);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\view\ScaleGestureDetectorCompat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */