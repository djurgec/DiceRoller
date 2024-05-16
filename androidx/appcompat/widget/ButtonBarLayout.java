package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import androidx.appcompat.R;
import androidx.core.view.ViewCompat;

public class ButtonBarLayout extends LinearLayout {
  private static final int PEEK_BUTTON_DP = 16;
  
  private boolean mAllowStacking;
  
  private int mLastWidthSize = -1;
  
  private boolean mStacked;
  
  public ButtonBarLayout(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    TypedArray typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.ButtonBarLayout);
    ViewCompat.saveAttributeDataForStyleable((View)this, paramContext, R.styleable.ButtonBarLayout, paramAttributeSet, typedArray, 0, 0);
    this.mAllowStacking = typedArray.getBoolean(R.styleable.ButtonBarLayout_allowStacking, true);
    typedArray.recycle();
    if (getOrientation() == 1)
      setStacked(this.mAllowStacking); 
  }
  
  private int getNextVisibleChildIndex(int paramInt) {
    int i = getChildCount();
    while (paramInt < i) {
      if (getChildAt(paramInt).getVisibility() == 0)
        return paramInt; 
      paramInt++;
    } 
    return -1;
  }
  
  private boolean isStacked() {
    return this.mStacked;
  }
  
  private void setStacked(boolean paramBoolean) {
    if (this.mStacked != paramBoolean && (!paramBoolean || this.mAllowStacking)) {
      byte b;
      this.mStacked = paramBoolean;
      setOrientation(paramBoolean);
      if (paramBoolean) {
        b = 8388613;
      } else {
        b = 80;
      } 
      setGravity(b);
      View view = findViewById(R.id.spacer);
      if (view != null) {
        byte b1;
        if (paramBoolean) {
          b1 = 8;
        } else {
          b1 = 4;
        } 
        view.setVisibility(b1);
      } 
      for (int i = getChildCount() - 2; i >= 0; i--)
        bringChildToFront(getChildAt(i)); 
    } 
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    int j = View.MeasureSpec.getSize(paramInt1);
    if (this.mAllowStacking) {
      if (j > this.mLastWidthSize && isStacked())
        setStacked(false); 
      this.mLastWidthSize = j;
    } 
    int i = 0;
    if (!isStacked() && View.MeasureSpec.getMode(paramInt1) == 1073741824) {
      j = View.MeasureSpec.makeMeasureSpec(j, -2147483648);
      i = 1;
    } else {
      j = paramInt1;
    } 
    super.onMeasure(j, paramInt2);
    j = i;
    if (this.mAllowStacking) {
      j = i;
      if (!isStacked()) {
        boolean bool;
        if ((0xFF000000 & getMeasuredWidthAndState()) == 16777216) {
          bool = true;
        } else {
          bool = false;
        } 
        j = i;
        if (bool) {
          setStacked(true);
          j = 1;
        } 
      } 
    } 
    if (j != 0)
      super.onMeasure(paramInt1, paramInt2); 
    i = 0;
    int k = getNextVisibleChildIndex(0);
    if (k >= 0) {
      View view = getChildAt(k);
      LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)view.getLayoutParams();
      j = 0 + getPaddingTop() + view.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin;
      if (isStacked()) {
        k = getNextVisibleChildIndex(k + 1);
        i = j;
        if (k >= 0)
          i = j + getChildAt(k).getPaddingTop() + (int)((getResources().getDisplayMetrics()).density * 16.0F); 
      } else {
        i = j + getPaddingBottom();
      } 
    } 
    if (ViewCompat.getMinimumHeight((View)this) != i) {
      setMinimumHeight(i);
      if (paramInt2 == 0)
        super.onMeasure(paramInt1, paramInt2); 
    } 
  }
  
  public void setAllowStacking(boolean paramBoolean) {
    if (this.mAllowStacking != paramBoolean) {
      this.mAllowStacking = paramBoolean;
      if (!paramBoolean && isStacked())
        setStacked(false); 
      requestLayout();
    } 
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\appcompat\widget\ButtonBarLayout.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */