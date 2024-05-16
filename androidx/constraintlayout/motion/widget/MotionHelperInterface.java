package androidx.constraintlayout.motion.widget;

import android.graphics.Canvas;
import android.view.View;
import java.util.HashMap;

public interface MotionHelperInterface extends Animatable, MotionLayout.TransitionListener {
  boolean isDecorator();
  
  boolean isUseOnHide();
  
  boolean isUsedOnShow();
  
  void onFinishedMotionScene(MotionLayout paramMotionLayout);
  
  void onPostDraw(Canvas paramCanvas);
  
  void onPreDraw(Canvas paramCanvas);
  
  void onPreSetup(MotionLayout paramMotionLayout, HashMap<View, MotionController> paramHashMap);
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\motion\widget\MotionHelperInterface.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */