package androidx.constraintlayout.core.motion.utils;

import androidx.constraintlayout.core.motion.MotionWidget;

public class ViewState {
  public int bottom;
  
  public int left;
  
  public int right;
  
  public float rotation;
  
  public int top;
  
  public void getState(MotionWidget paramMotionWidget) {
    this.left = paramMotionWidget.getLeft();
    this.top = paramMotionWidget.getTop();
    this.right = paramMotionWidget.getRight();
    this.bottom = paramMotionWidget.getBottom();
    this.rotation = (int)paramMotionWidget.getRotationZ();
  }
  
  public int height() {
    return this.bottom - this.top;
  }
  
  public int width() {
    return this.right - this.left;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\motio\\utils\ViewState.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */