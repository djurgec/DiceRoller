package androidx.viewpager2.widget;

import android.animation.LayoutTransition;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import java.util.Arrays;
import java.util.Comparator;

final class AnimateLayoutChangeDetector {
  private static final ViewGroup.MarginLayoutParams ZERO_MARGIN_LAYOUT_PARAMS;
  
  private LinearLayoutManager mLayoutManager;
  
  static {
    ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(-1, -1);
    ZERO_MARGIN_LAYOUT_PARAMS = marginLayoutParams;
    marginLayoutParams.setMargins(0, 0, 0, 0);
  }
  
  AnimateLayoutChangeDetector(LinearLayoutManager paramLinearLayoutManager) {
    this.mLayoutManager = paramLinearLayoutManager;
  }
  
  private boolean arePagesLaidOutContiguously() {
    int k = this.mLayoutManager.getChildCount();
    if (k == 0)
      return true; 
    if (this.mLayoutManager.getOrientation() == 0) {
      i = 1;
    } else {
      i = 0;
    } 
    int[][] arrayOfInt = new int[k][2];
    int j = 0;
    while (j < k) {
      View view = this.mLayoutManager.getChildAt(j);
      if (view != null) {
        int m;
        ViewGroup.MarginLayoutParams marginLayoutParams;
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
          marginLayoutParams = (ViewGroup.MarginLayoutParams)layoutParams;
        } else {
          marginLayoutParams = ZERO_MARGIN_LAYOUT_PARAMS;
        } 
        int[] arrayOfInt1 = arrayOfInt[j];
        if (i) {
          m = view.getLeft() - marginLayoutParams.leftMargin;
        } else {
          m = view.getTop() - marginLayoutParams.topMargin;
        } 
        arrayOfInt1[0] = m;
        arrayOfInt1 = arrayOfInt[j];
        if (i) {
          m = view.getRight() + marginLayoutParams.rightMargin;
        } else {
          m = view.getBottom() + marginLayoutParams.bottomMargin;
        } 
        arrayOfInt1[1] = m;
        j++;
        continue;
      } 
      throw new IllegalStateException("null view contained in the view hierarchy");
    } 
    Arrays.sort(arrayOfInt, (Comparator)new Comparator<int[]>() {
          final AnimateLayoutChangeDetector this$0;
          
          public int compare(int[] param1ArrayOfint1, int[] param1ArrayOfint2) {
            return param1ArrayOfint1[0] - param1ArrayOfint2[0];
          }
        });
    int i;
    for (i = 1; i < k; i++) {
      if (arrayOfInt[i - 1][1] != arrayOfInt[i][0])
        return false; 
    } 
    i = arrayOfInt[0][1];
    j = arrayOfInt[0][0];
    return !(arrayOfInt[0][0] > 0 || arrayOfInt[k - 1][1] < i - j);
  }
  
  private boolean hasRunningChangingLayoutTransition() {
    int i = this.mLayoutManager.getChildCount();
    for (byte b = 0; b < i; b++) {
      if (hasRunningChangingLayoutTransition(this.mLayoutManager.getChildAt(b)))
        return true; 
    } 
    return false;
  }
  
  private static boolean hasRunningChangingLayoutTransition(View paramView) {
    if (paramView instanceof ViewGroup) {
      ViewGroup viewGroup = (ViewGroup)paramView;
      LayoutTransition layoutTransition = viewGroup.getLayoutTransition();
      if (layoutTransition != null && layoutTransition.isChangingLayout())
        return true; 
      int i = viewGroup.getChildCount();
      for (byte b = 0; b < i; b++) {
        if (hasRunningChangingLayoutTransition(viewGroup.getChildAt(b)))
          return true; 
      } 
    } 
    return false;
  }
  
  boolean mayHaveInterferingAnimations() {
    boolean bool1 = arePagesLaidOutContiguously();
    boolean bool = true;
    if ((bool1 && this.mLayoutManager.getChildCount() > 1) || !hasRunningChangingLayoutTransition())
      bool = false; 
    return bool;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\viewpager2\widget\AnimateLayoutChangeDetector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */