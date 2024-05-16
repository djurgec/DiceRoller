package androidx.transition;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.view.ViewCompat;

public class SidePropagation extends VisibilityPropagation {
  private float mPropagationSpeed = 3.0F;
  
  private int mSide = 80;
  
  private int distance(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8) {
    int j = this.mSide;
    int i = 5;
    boolean bool2 = false;
    boolean bool1 = false;
    if (j == 8388611) {
      if (ViewCompat.getLayoutDirection(paramView) == 1)
        bool1 = true; 
      if (!bool1)
        i = 3; 
    } else if (j == 8388613) {
      bool1 = bool2;
      if (ViewCompat.getLayoutDirection(paramView) == 1)
        bool1 = true; 
      if (bool1)
        i = 3; 
    } else {
      i = this.mSide;
    } 
    bool1 = false;
    switch (i) {
      default:
        return bool1;
      case 80:
        return paramInt2 - paramInt6 + Math.abs(paramInt3 - paramInt1);
      case 48:
        return paramInt8 - paramInt2 + Math.abs(paramInt3 - paramInt1);
      case 5:
        return paramInt1 - paramInt5 + Math.abs(paramInt4 - paramInt2);
      case 3:
        break;
    } 
    return paramInt7 - paramInt1 + Math.abs(paramInt4 - paramInt2);
  }
  
  private int getMaxDistance(ViewGroup paramViewGroup) {
    switch (this.mSide) {
      default:
        return paramViewGroup.getHeight();
      case 3:
      case 5:
      case 8388611:
      case 8388613:
        break;
    } 
    return paramViewGroup.getWidth();
  }
  
  public long getStartDelay(ViewGroup paramViewGroup, Transition paramTransition, TransitionValues paramTransitionValues1, TransitionValues paramTransitionValues2) {
    boolean bool;
    int i;
    int j;
    if (paramTransitionValues1 == null && paramTransitionValues2 == null)
      return 0L; 
    Rect rect = paramTransition.getEpicenter();
    if (paramTransitionValues2 == null || getViewVisibility(paramTransitionValues1) == 0) {
      bool = true;
    } else {
      bool = true;
      paramTransitionValues1 = paramTransitionValues2;
    } 
    int i3 = getViewX(paramTransitionValues1);
    int i2 = getViewY(paramTransitionValues1);
    int[] arrayOfInt = new int[2];
    paramViewGroup.getLocationOnScreen(arrayOfInt);
    int n = arrayOfInt[0] + Math.round(paramViewGroup.getTranslationX());
    int i1 = arrayOfInt[1] + Math.round(paramViewGroup.getTranslationY());
    int k = n + paramViewGroup.getWidth();
    int m = i1 + paramViewGroup.getHeight();
    if (rect != null) {
      i = rect.centerX();
      j = rect.centerY();
    } else {
      i = (n + k) / 2;
      j = (i1 + m) / 2;
    } 
    float f = distance((View)paramViewGroup, i3, i2, i, j, n, i1, k, m) / getMaxDistance(paramViewGroup);
    long l2 = paramTransition.getDuration();
    long l1 = l2;
    if (l2 < 0L)
      l1 = 300L; 
    return Math.round((float)(bool * l1) / this.mPropagationSpeed * f);
  }
  
  public void setPropagationSpeed(float paramFloat) {
    if (paramFloat != 0.0F) {
      this.mPropagationSpeed = paramFloat;
      return;
    } 
    throw new IllegalArgumentException("propagationSpeed may not be 0");
  }
  
  public void setSide(int paramInt) {
    this.mSide = paramInt;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\transition\SidePropagation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */