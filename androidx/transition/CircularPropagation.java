package androidx.transition;

import android.graphics.Rect;
import android.view.ViewGroup;

public class CircularPropagation extends VisibilityPropagation {
  private float mPropagationSpeed = 3.0F;
  
  private static float distance(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
    paramFloat1 = paramFloat3 - paramFloat1;
    paramFloat2 = paramFloat4 - paramFloat2;
    return (float)Math.sqrt((paramFloat1 * paramFloat1 + paramFloat2 * paramFloat2));
  }
  
  public long getStartDelay(ViewGroup paramViewGroup, Transition paramTransition, TransitionValues paramTransitionValues1, TransitionValues paramTransitionValues2) {
    int i;
    int j;
    if (paramTransitionValues1 == null && paramTransitionValues2 == null)
      return 0L; 
    byte b = 1;
    if (paramTransitionValues2 == null || getViewVisibility(paramTransitionValues1) == 0) {
      b = -1;
    } else {
      paramTransitionValues1 = paramTransitionValues2;
    } 
    int m = getViewX(paramTransitionValues1);
    int k = getViewY(paramTransitionValues1);
    Rect rect = paramTransition.getEpicenter();
    if (rect != null) {
      j = rect.centerX();
      i = rect.centerY();
    } else {
      int[] arrayOfInt = new int[2];
      paramViewGroup.getLocationOnScreen(arrayOfInt);
      j = Math.round((arrayOfInt[0] + paramViewGroup.getWidth() / 2) + paramViewGroup.getTranslationX());
      i = Math.round((arrayOfInt[1] + paramViewGroup.getHeight() / 2) + paramViewGroup.getTranslationY());
    } 
    float f = distance(m, k, j, i) / distance(0.0F, 0.0F, paramViewGroup.getWidth(), paramViewGroup.getHeight());
    long l2 = paramTransition.getDuration();
    long l1 = l2;
    if (l2 < 0L)
      l1 = 300L; 
    return Math.round((float)(b * l1) / this.mPropagationSpeed * f);
  }
  
  public void setPropagationSpeed(float paramFloat) {
    if (paramFloat != 0.0F) {
      this.mPropagationSpeed = paramFloat;
      return;
    } 
    throw new IllegalArgumentException("propagationSpeed may not be 0");
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\transition\CircularPropagation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */