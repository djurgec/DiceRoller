package androidx.constraintlayout.core.motion.utils;

public class FloatRect {
  public float bottom;
  
  public float left;
  
  public float right;
  
  public float top;
  
  public final float centerX() {
    return (this.left + this.right) * 0.5F;
  }
  
  public final float centerY() {
    return (this.top + this.bottom) * 0.5F;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\motio\\utils\FloatRect.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */