package com.google.android.material.internal;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class BaselineLayout extends ViewGroup {
  private int baseline = -1;
  
  public BaselineLayout(Context paramContext) {
    super(paramContext, null, 0);
  }
  
  public BaselineLayout(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet, 0);
  }
  
  public BaselineLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public int getBaseline() {
    return this.baseline;
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    int j = getChildCount();
    int m = getPaddingLeft();
    int k = getPaddingRight();
    int i = getPaddingTop();
    for (paramInt2 = 0; paramInt2 < j; paramInt2++) {
      View view = getChildAt(paramInt2);
      if (view.getVisibility() != 8) {
        int i2 = view.getMeasuredWidth();
        int n = view.getMeasuredHeight();
        int i1 = (paramInt3 - paramInt1 - k - m - i2) / 2 + m;
        if (this.baseline != -1 && view.getBaseline() != -1) {
          paramInt4 = this.baseline + i - view.getBaseline();
        } else {
          paramInt4 = i;
        } 
        view.layout(i1, paramInt4, i1 + i2, paramInt4 + n);
      } 
    } 
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    int i2 = getChildCount();
    int i1 = 0;
    int j = 0;
    int k = -1;
    int i = -1;
    int n = 0;
    for (byte b = 0; b < i2; b++) {
      View view = getChildAt(b);
      if (view.getVisibility() != 8) {
        measureChild(view, paramInt1, paramInt2);
        int i5 = view.getBaseline();
        int i3 = k;
        int i4 = i;
        if (i5 != -1) {
          i3 = Math.max(k, i5);
          i4 = Math.max(i, view.getMeasuredHeight() - i5);
        } 
        i1 = Math.max(i1, view.getMeasuredWidth());
        j = Math.max(j, view.getMeasuredHeight());
        n = View.combineMeasuredStates(n, view.getMeasuredState());
        i = i4;
        k = i3;
      } 
    } 
    int m = j;
    if (k != -1) {
      m = Math.max(j, k + Math.max(i, getPaddingBottom()));
      this.baseline = k;
    } 
    i = Math.max(m, getSuggestedMinimumHeight());
    j = Math.max(i1, getSuggestedMinimumWidth());
    setMeasuredDimension(View.resolveSizeAndState(j, paramInt1, n), View.resolveSizeAndState(i, paramInt2, n << 16));
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\internal\BaselineLayout.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */