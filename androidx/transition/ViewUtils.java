package androidx.transition;

import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Build;
import android.util.Property;
import android.view.View;
import androidx.core.view.ViewCompat;

class ViewUtils {
  static final Property<View, Rect> CLIP_BOUNDS;
  
  private static final ViewUtilsBase IMPL;
  
  private static final String TAG = "ViewUtils";
  
  static final Property<View, Float> TRANSITION_ALPHA = new Property<View, Float>(Float.class, "translationAlpha") {
      public Float get(View param1View) {
        return Float.valueOf(ViewUtils.getTransitionAlpha(param1View));
      }
      
      public void set(View param1View, Float param1Float) {
        ViewUtils.setTransitionAlpha(param1View, param1Float.floatValue());
      }
    };
  
  static {
    CLIP_BOUNDS = new Property<View, Rect>(Rect.class, "clipBounds") {
        public Rect get(View param1View) {
          return ViewCompat.getClipBounds(param1View);
        }
        
        public void set(View param1View, Rect param1Rect) {
          ViewCompat.setClipBounds(param1View, param1Rect);
        }
      };
  }
  
  static void clearNonTransitionAlpha(View paramView) {
    IMPL.clearNonTransitionAlpha(paramView);
  }
  
  static ViewOverlayImpl getOverlay(View paramView) {
    return (ViewOverlayImpl)((Build.VERSION.SDK_INT >= 18) ? new ViewOverlayApi18(paramView) : ViewOverlayApi14.createFrom(paramView));
  }
  
  static float getTransitionAlpha(View paramView) {
    return IMPL.getTransitionAlpha(paramView);
  }
  
  static WindowIdImpl getWindowId(View paramView) {
    return (WindowIdImpl)((Build.VERSION.SDK_INT >= 18) ? new WindowIdApi18(paramView) : new WindowIdApi14(paramView.getWindowToken()));
  }
  
  static void saveNonTransitionAlpha(View paramView) {
    IMPL.saveNonTransitionAlpha(paramView);
  }
  
  static void setAnimationMatrix(View paramView, Matrix paramMatrix) {
    IMPL.setAnimationMatrix(paramView, paramMatrix);
  }
  
  static void setLeftTopRightBottom(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    IMPL.setLeftTopRightBottom(paramView, paramInt1, paramInt2, paramInt3, paramInt4);
  }
  
  static void setTransitionAlpha(View paramView, float paramFloat) {
    IMPL.setTransitionAlpha(paramView, paramFloat);
  }
  
  static void setTransitionVisibility(View paramView, int paramInt) {
    IMPL.setTransitionVisibility(paramView, paramInt);
  }
  
  static void transformMatrixToGlobal(View paramView, Matrix paramMatrix) {
    IMPL.transformMatrixToGlobal(paramView, paramMatrix);
  }
  
  static void transformMatrixToLocal(View paramView, Matrix paramMatrix) {
    IMPL.transformMatrixToLocal(paramView, paramMatrix);
  }
  
  static {
    if (Build.VERSION.SDK_INT >= 29) {
      IMPL = new ViewUtilsApi29();
    } else if (Build.VERSION.SDK_INT >= 23) {
      IMPL = new ViewUtilsApi23();
    } else if (Build.VERSION.SDK_INT >= 22) {
      IMPL = new ViewUtilsApi22();
    } else if (Build.VERSION.SDK_INT >= 21) {
      IMPL = new ViewUtilsApi21();
    } else if (Build.VERSION.SDK_INT >= 19) {
      IMPL = new ViewUtilsApi19();
    } else {
      IMPL = new ViewUtilsBase();
    } 
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\transition\ViewUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */