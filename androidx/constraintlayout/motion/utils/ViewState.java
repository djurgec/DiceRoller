package androidx.constraintlayout.motion.utils;

import android.view.View;

public class ViewState {
  public int bottom;
  
  public int left;
  
  public int right;
  
  public float rotation;
  
  public int top;
  
  public void getState(View paramView) {
    this.left = paramView.getLeft();
    this.top = paramView.getTop();
    this.right = paramView.getRight();
    this.bottom = paramView.getBottom();
    this.rotation = paramView.getRotation();
  }
  
  public int height() {
    return this.bottom - this.top;
  }
  
  public int width() {
    return this.right - this.left;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\motio\\utils\ViewState.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */