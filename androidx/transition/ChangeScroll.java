package androidx.transition;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class ChangeScroll extends Transition {
  private static final String[] PROPERTIES = new String[] { "android:changeScroll:x", "android:changeScroll:y" };
  
  private static final String PROPNAME_SCROLL_X = "android:changeScroll:x";
  
  private static final String PROPNAME_SCROLL_Y = "android:changeScroll:y";
  
  public ChangeScroll() {}
  
  public ChangeScroll(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
  }
  
  private void captureValues(TransitionValues paramTransitionValues) {
    paramTransitionValues.values.put("android:changeScroll:x", Integer.valueOf(paramTransitionValues.view.getScrollX()));
    paramTransitionValues.values.put("android:changeScroll:y", Integer.valueOf(paramTransitionValues.view.getScrollY()));
  }
  
  public void captureEndValues(TransitionValues paramTransitionValues) {
    captureValues(paramTransitionValues);
  }
  
  public void captureStartValues(TransitionValues paramTransitionValues) {
    captureValues(paramTransitionValues);
  }
  
  public Animator createAnimator(ViewGroup paramViewGroup, TransitionValues paramTransitionValues1, TransitionValues paramTransitionValues2) {
    ObjectAnimator objectAnimator1;
    ObjectAnimator objectAnimator2;
    if (paramTransitionValues1 == null || paramTransitionValues2 == null)
      return null; 
    View view = paramTransitionValues2.view;
    int m = ((Integer)paramTransitionValues1.values.get("android:changeScroll:x")).intValue();
    int k = ((Integer)paramTransitionValues2.values.get("android:changeScroll:x")).intValue();
    int j = ((Integer)paramTransitionValues1.values.get("android:changeScroll:y")).intValue();
    int i = ((Integer)paramTransitionValues2.values.get("android:changeScroll:y")).intValue();
    paramViewGroup = null;
    paramTransitionValues1 = null;
    if (m != k) {
      view.setScrollX(m);
      objectAnimator1 = ObjectAnimator.ofInt(view, "scrollX", new int[] { m, k });
    } 
    if (j != i) {
      view.setScrollY(j);
      objectAnimator2 = ObjectAnimator.ofInt(view, "scrollY", new int[] { j, i });
    } 
    return TransitionUtils.mergeAnimators((Animator)objectAnimator1, (Animator)objectAnimator2);
  }
  
  public String[] getTransitionProperties() {
    return PROPERTIES;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\transition\ChangeScroll.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */