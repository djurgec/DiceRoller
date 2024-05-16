package androidx.transition;

import android.animation.TypeEvaluator;
import android.graphics.Rect;

class RectEvaluator implements TypeEvaluator<Rect> {
  private Rect mRect;
  
  RectEvaluator() {}
  
  RectEvaluator(Rect paramRect) {
    this.mRect = paramRect;
  }
  
  public Rect evaluate(float paramFloat, Rect paramRect1, Rect paramRect2) {
    int i = paramRect1.left + (int)((paramRect2.left - paramRect1.left) * paramFloat);
    int k = paramRect1.top + (int)((paramRect2.top - paramRect1.top) * paramFloat);
    int m = paramRect1.right + (int)((paramRect2.right - paramRect1.right) * paramFloat);
    int j = paramRect1.bottom + (int)((paramRect2.bottom - paramRect1.bottom) * paramFloat);
    paramRect1 = this.mRect;
    if (paramRect1 == null)
      return new Rect(i, k, m, j); 
    paramRect1.set(i, k, m, j);
    return this.mRect;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\transition\RectEvaluator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */