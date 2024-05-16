package androidx.appcompat.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.R;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;

public class AlertDialogLayout extends LinearLayoutCompat {
  public AlertDialogLayout(Context paramContext) {
    super(paramContext);
  }
  
  public AlertDialogLayout(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
  }
  
  private void forceUniformWidth(int paramInt1, int paramInt2) {
    int i = View.MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 1073741824);
    for (byte b = 0; b < paramInt1; b++) {
      View view = getChildAt(b);
      if (view.getVisibility() != 8) {
        LinearLayoutCompat.LayoutParams layoutParams = (LinearLayoutCompat.LayoutParams)view.getLayoutParams();
        if (layoutParams.width == -1) {
          int j = layoutParams.height;
          layoutParams.height = view.getMeasuredHeight();
          measureChildWithMargins(view, i, 0, paramInt2, 0);
          layoutParams.height = j;
        } 
      } 
    } 
  }
  
  private static int resolveMinimumHeight(View paramView) {
    int i = ViewCompat.getMinimumHeight(paramView);
    if (i > 0)
      return i; 
    if (paramView instanceof ViewGroup) {
      ViewGroup viewGroup = (ViewGroup)paramView;
      if (viewGroup.getChildCount() == 1)
        return resolveMinimumHeight(viewGroup.getChildAt(0)); 
    } 
    return 0;
  }
  
  private void setChildFrame(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    paramView.layout(paramInt1, paramInt2, paramInt1 + paramInt3, paramInt2 + paramInt4);
  }
  
  private boolean tryOnMeasure(int paramInt1, int paramInt2) {
    View view3 = null;
    View view2 = null;
    View view1 = null;
    int i5 = getChildCount();
    int i;
    for (i = 0; i < i5; i++) {
      View view = getChildAt(i);
      if (view.getVisibility() != 8) {
        int i8 = view.getId();
        if (i8 == R.id.topPanel) {
          view3 = view;
        } else if (i8 == R.id.buttonPanel) {
          view2 = view;
        } else if (i8 == R.id.contentPanel || i8 == R.id.customPanel) {
          if (view1 != null)
            return false; 
          view1 = view;
        } else {
          return false;
        } 
      } 
    } 
    int i7 = View.MeasureSpec.getMode(paramInt2);
    int i1 = View.MeasureSpec.getSize(paramInt2);
    int i6 = View.MeasureSpec.getMode(paramInt1);
    int m = 0;
    i = getPaddingTop() + getPaddingBottom();
    int n = i;
    if (view3 != null) {
      view3.measure(paramInt1, 0);
      n = i + view3.getMeasuredHeight();
      m = View.combineMeasuredStates(0, view3.getMeasuredState());
    } 
    i = 0;
    int i2 = 0;
    int j = m;
    int k = n;
    if (view2 != null) {
      view2.measure(paramInt1, 0);
      i = resolveMinimumHeight(view2);
      i2 = view2.getMeasuredHeight() - i;
      k = n + i;
      j = View.combineMeasuredStates(m, view2.getMeasuredState());
    } 
    int i3 = 0;
    if (view1 != null) {
      if (i7 == 0) {
        m = 0;
      } else {
        m = View.MeasureSpec.makeMeasureSpec(Math.max(0, i1 - k), i7);
      } 
      view1.measure(paramInt1, m);
      i3 = view1.getMeasuredHeight();
      k += i3;
      j = View.combineMeasuredStates(j, view1.getMeasuredState());
    } 
    int i4 = i1 - k;
    m = i4;
    i1 = j;
    n = k;
    if (view2 != null) {
      i1 = Math.min(i4, i2);
      m = i4;
      n = i;
      if (i1 > 0) {
        m = i4 - i1;
        n = i + i1;
      } 
      view2.measure(paramInt1, View.MeasureSpec.makeMeasureSpec(n, 1073741824));
      n = k - i + view2.getMeasuredHeight();
      i1 = View.combineMeasuredStates(j, view2.getMeasuredState());
    } 
    k = m;
    j = i1;
    i = n;
    if (view1 != null) {
      k = m;
      j = i1;
      i = n;
      if (m > 0) {
        view1.measure(paramInt1, View.MeasureSpec.makeMeasureSpec(i3 + m, i7));
        i = n - i3 + view1.getMeasuredHeight();
        j = View.combineMeasuredStates(i1, view1.getMeasuredState());
        k = m - m;
      } 
    } 
    n = 0;
    m = 0;
    while (m < i5) {
      View view = getChildAt(m);
      k = n;
      if (view.getVisibility() != 8)
        k = Math.max(n, view.getMeasuredWidth()); 
      m++;
      n = k;
    } 
    setMeasuredDimension(View.resolveSizeAndState(n + getPaddingLeft() + getPaddingRight(), paramInt1, j), View.resolveSizeAndState(i, paramInt2, 0));
    if (i6 != 1073741824)
      forceUniformWidth(i5, paramInt2); 
    return true;
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    int i = getPaddingLeft();
    int j = paramInt3 - paramInt1;
    int m = getPaddingRight();
    int k = getPaddingRight();
    paramInt1 = getMeasuredHeight();
    int n = getChildCount();
    int i1 = getGravity();
    switch (i1 & 0x70) {
      default:
        paramInt1 = getPaddingTop();
        break;
      case 80:
        paramInt1 = getPaddingTop() + paramInt4 - paramInt2 - paramInt1;
        break;
      case 16:
        paramInt1 = getPaddingTop() + (paramInt4 - paramInt2 - paramInt1) / 2;
        break;
    } 
    Drawable drawable = getDividerDrawable();
    if (drawable == null) {
      paramInt3 = 0;
    } else {
      paramInt3 = drawable.getIntrinsicHeight();
    } 
    for (paramInt4 = 0; paramInt4 < n; paramInt4++) {
      View view = getChildAt(paramInt4);
      if (view != null && view.getVisibility() != 8) {
        int i4 = view.getMeasuredWidth();
        int i3 = view.getMeasuredHeight();
        LinearLayoutCompat.LayoutParams layoutParams = (LinearLayoutCompat.LayoutParams)view.getLayoutParams();
        paramInt2 = layoutParams.gravity;
        if (paramInt2 < 0)
          paramInt2 = i1 & 0x800007; 
        switch (GravityCompat.getAbsoluteGravity(paramInt2, ViewCompat.getLayoutDirection((View)this)) & 0x7) {
          default:
            paramInt2 = layoutParams.leftMargin + i;
            break;
          case 5:
            paramInt2 = j - m - i4 - layoutParams.rightMargin;
            break;
          case 1:
            paramInt2 = (j - i - k - i4) / 2 + i + layoutParams.leftMargin - layoutParams.rightMargin;
            break;
        } 
        int i2 = paramInt1;
        if (hasDividerBeforeChildAt(paramInt4))
          i2 = paramInt1 + paramInt3; 
        paramInt1 = i2 + layoutParams.topMargin;
        setChildFrame(view, paramInt2, paramInt1, i4, i3);
        paramInt1 += i3 + layoutParams.bottomMargin;
      } 
    } 
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    if (!tryOnMeasure(paramInt1, paramInt2))
      super.onMeasure(paramInt1, paramInt2); 
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\appcompat\widget\AlertDialogLayout.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */