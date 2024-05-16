package androidx.transition;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

public class Explode extends Visibility {
  private static final String PROPNAME_SCREEN_BOUNDS = "android:explode:screenBounds";
  
  private static final TimeInterpolator sAccelerate;
  
  private static final TimeInterpolator sDecelerate = (TimeInterpolator)new DecelerateInterpolator();
  
  private int[] mTempLoc = new int[2];
  
  static {
    sAccelerate = (TimeInterpolator)new AccelerateInterpolator();
  }
  
  public Explode() {
    setPropagation(new CircularPropagation());
  }
  
  public Explode(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    setPropagation(new CircularPropagation());
  }
  
  private static float calculateDistance(float paramFloat1, float paramFloat2) {
    return (float)Math.sqrt((paramFloat1 * paramFloat1 + paramFloat2 * paramFloat2));
  }
  
  private static float calculateMaxDistance(View paramView, int paramInt1, int paramInt2) {
    paramInt1 = Math.max(paramInt1, paramView.getWidth() - paramInt1);
    paramInt2 = Math.max(paramInt2, paramView.getHeight() - paramInt2);
    return calculateDistance(paramInt1, paramInt2);
  }
  
  private void calculateOut(View paramView, Rect paramRect, int[] paramArrayOfint) {
    int i;
    int j;
    paramView.getLocationOnScreen(this.mTempLoc);
    int[] arrayOfInt = this.mTempLoc;
    int k = arrayOfInt[0];
    int m = arrayOfInt[1];
    Rect rect = getEpicenter();
    if (rect == null) {
      i = paramView.getWidth() / 2 + k + Math.round(paramView.getTranslationX());
      j = paramView.getHeight() / 2 + m + Math.round(paramView.getTranslationY());
    } else {
      i = rect.centerX();
      j = rect.centerY();
    } 
    int n = paramRect.centerX();
    int i1 = paramRect.centerY();
    float f2 = (n - i);
    float f1 = (i1 - j);
    if (f2 == 0.0F && f1 == 0.0F) {
      f2 = (float)(Math.random() * 2.0D) - 1.0F;
      f1 = (float)(Math.random() * 2.0D) - 1.0F;
    } 
    float f3 = calculateDistance(f2, f1);
    f2 /= f3;
    f1 /= f3;
    f3 = calculateMaxDistance(paramView, i - k, j - m);
    paramArrayOfint[0] = Math.round(f3 * f2);
    paramArrayOfint[1] = Math.round(f3 * f1);
  }
  
  private void captureValues(TransitionValues paramTransitionValues) {
    View view = paramTransitionValues.view;
    view.getLocationOnScreen(this.mTempLoc);
    int[] arrayOfInt = this.mTempLoc;
    int m = arrayOfInt[0];
    int i = arrayOfInt[1];
    int k = view.getWidth();
    int j = view.getHeight();
    paramTransitionValues.values.put("android:explode:screenBounds", new Rect(m, i, k + m, j + i));
  }
  
  public void captureEndValues(TransitionValues paramTransitionValues) {
    super.captureEndValues(paramTransitionValues);
    captureValues(paramTransitionValues);
  }
  
  public void captureStartValues(TransitionValues paramTransitionValues) {
    super.captureStartValues(paramTransitionValues);
    captureValues(paramTransitionValues);
  }
  
  public Animator onAppear(ViewGroup paramViewGroup, View paramView, TransitionValues paramTransitionValues1, TransitionValues paramTransitionValues2) {
    if (paramTransitionValues2 == null)
      return null; 
    Rect rect = (Rect)paramTransitionValues2.values.get("android:explode:screenBounds");
    float f4 = paramView.getTranslationX();
    float f3 = paramView.getTranslationY();
    calculateOut((View)paramViewGroup, rect, this.mTempLoc);
    int[] arrayOfInt = this.mTempLoc;
    float f2 = arrayOfInt[0];
    float f1 = arrayOfInt[1];
    return TranslationAnimationCreator.createAnimation(paramView, paramTransitionValues2, rect.left, rect.top, f4 + f2, f3 + f1, f4, f3, sDecelerate, this);
  }
  
  public Animator onDisappear(ViewGroup paramViewGroup, View paramView, TransitionValues paramTransitionValues1, TransitionValues paramTransitionValues2) {
    if (paramTransitionValues1 == null)
      return null; 
    Rect rect = (Rect)paramTransitionValues1.values.get("android:explode:screenBounds");
    int i = rect.left;
    int j = rect.top;
    float f5 = paramView.getTranslationX();
    float f6 = paramView.getTranslationY();
    float f4 = f5;
    float f1 = f6;
    int[] arrayOfInt2 = (int[])paramTransitionValues1.view.getTag(R.id.transition_position);
    float f3 = f4;
    float f2 = f1;
    if (arrayOfInt2 != null) {
      f3 = f4 + (arrayOfInt2[0] - rect.left);
      f2 = f1 + (arrayOfInt2[1] - rect.top);
      rect.offsetTo(arrayOfInt2[0], arrayOfInt2[1]);
    } 
    calculateOut((View)paramViewGroup, rect, this.mTempLoc);
    int[] arrayOfInt1 = this.mTempLoc;
    return TranslationAnimationCreator.createAnimation(paramView, paramTransitionValues1, i, j, f5, f6, f3 + arrayOfInt1[0], f2 + arrayOfInt1[1], sAccelerate, this);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\transition\Explode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */