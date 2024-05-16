package androidx.constraintlayout.core.motion.utils;

public class Rect {
  public int bottom;
  
  public int left;
  
  public int right;
  
  public int top;
  
  public int height() {
    return this.bottom - this.top;
  }
  
  public int width() {
    return this.right - this.left;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\motio\\utils\Rect.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */