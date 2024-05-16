package com.google.android.material.internal;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.view.MarginLayoutParamsCompat;
import androidx.core.view.ViewCompat;
import com.google.android.material.R;

public class FlowLayout extends ViewGroup {
  private int itemSpacing;
  
  private int lineSpacing;
  
  private int rowCount;
  
  private boolean singleLine = false;
  
  public FlowLayout(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public FlowLayout(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public FlowLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    loadFromAttributes(paramContext, paramAttributeSet);
  }
  
  public FlowLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2) {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    loadFromAttributes(paramContext, paramAttributeSet);
  }
  
  private static int getMeasuredDimension(int paramInt1, int paramInt2, int paramInt3) {
    switch (paramInt2) {
      default:
        return paramInt3;
      case 1073741824:
        return paramInt1;
      case -2147483648:
        break;
    } 
    return Math.min(paramInt3, paramInt1);
  }
  
  private void loadFromAttributes(Context paramContext, AttributeSet paramAttributeSet) {
    TypedArray typedArray = paramContext.getTheme().obtainStyledAttributes(paramAttributeSet, R.styleable.FlowLayout, 0, 0);
    this.lineSpacing = typedArray.getDimensionPixelSize(R.styleable.FlowLayout_lineSpacing, 0);
    this.itemSpacing = typedArray.getDimensionPixelSize(R.styleable.FlowLayout_itemSpacing, 0);
    typedArray.recycle();
  }
  
  protected int getItemSpacing() {
    return this.itemSpacing;
  }
  
  protected int getLineSpacing() {
    return this.lineSpacing;
  }
  
  protected int getRowCount() {
    return this.rowCount;
  }
  
  public int getRowIndex(View paramView) {
    Object object = paramView.getTag(R.id.row_index_key);
    return !(object instanceof Integer) ? -1 : ((Integer)object).intValue();
  }
  
  public boolean isSingleLine() {
    return this.singleLine;
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    paramInt2 = getChildCount();
    boolean bool = false;
    if (paramInt2 == 0) {
      this.rowCount = 0;
      return;
    } 
    this.rowCount = 1;
    if (ViewCompat.getLayoutDirection((View)this) == 1)
      bool = true; 
    if (bool) {
      paramInt2 = getPaddingRight();
    } else {
      paramInt2 = getPaddingLeft();
    } 
    if (bool) {
      i = getPaddingLeft();
    } else {
      i = getPaddingRight();
    } 
    paramInt4 = paramInt2;
    int k = getPaddingTop();
    int j = k;
    int m = paramInt3 - paramInt1 - i;
    int i = 0;
    paramInt1 = k;
    paramInt3 = paramInt4;
    while (i < getChildCount()) {
      View view = getChildAt(i);
      if (view.getVisibility() == 8) {
        view.setTag(R.id.row_index_key, Integer.valueOf(-1));
      } else {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        int n = 0;
        k = 0;
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
          ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams)layoutParams;
          n = MarginLayoutParamsCompat.getMarginStart(marginLayoutParams);
          k = MarginLayoutParamsCompat.getMarginEnd(marginLayoutParams);
        } 
        int i2 = view.getMeasuredWidth();
        int i1 = paramInt3;
        paramInt4 = paramInt1;
        if (!this.singleLine) {
          i1 = paramInt3;
          paramInt4 = paramInt1;
          if (paramInt3 + n + i2 > m) {
            i1 = paramInt2;
            paramInt4 = j + this.lineSpacing;
            this.rowCount++;
          } 
        } 
        view.setTag(R.id.row_index_key, Integer.valueOf(this.rowCount - 1));
        paramInt1 = i1 + n + view.getMeasuredWidth();
        j = view.getMeasuredHeight() + paramInt4;
        if (bool) {
          view.layout(m - paramInt1, paramInt4, m - i1 - n, j);
        } else {
          view.layout(i1 + n, paramInt4, paramInt1, j);
        } 
        paramInt3 = i1 + n + k + view.getMeasuredWidth() + this.itemSpacing;
        paramInt1 = paramInt4;
      } 
      i++;
    } 
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    int i;
    int i2 = View.MeasureSpec.getSize(paramInt1);
    int i5 = View.MeasureSpec.getMode(paramInt1);
    int i4 = View.MeasureSpec.getSize(paramInt2);
    int i3 = View.MeasureSpec.getMode(paramInt2);
    if (i5 == Integer.MIN_VALUE || i5 == 1073741824) {
      i = i2;
    } else {
      i = Integer.MAX_VALUE;
    } 
    int m = getPaddingLeft();
    int k = getPaddingTop();
    int n = k;
    int j = 0;
    int i6 = getPaddingRight();
    byte b = 0;
    int i1 = i;
    while (b < getChildCount()) {
      View view = getChildAt(b);
      if (view.getVisibility() != 8) {
        int i9;
        measureChild(view, paramInt1, paramInt2);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        int i8 = 0;
        int i7 = 0;
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
          ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams)layoutParams;
          i8 = 0 + marginLayoutParams.leftMargin;
          i7 = 0 + marginLayoutParams.rightMargin;
        } 
        if (m + i8 + view.getMeasuredWidth() > i - i6 && !isSingleLine()) {
          i9 = getPaddingLeft();
          k = this.lineSpacing + n;
        } else {
          i9 = m;
        } 
        int i10 = i9 + i8 + view.getMeasuredWidth();
        n = view.getMeasuredHeight() + k;
        m = j;
        if (i10 > j)
          m = i10; 
        i8 = i9 + i8 + i7 + view.getMeasuredWidth() + this.itemSpacing;
        if (b == getChildCount() - 1) {
          j = m + i7;
          m = i8;
        } else {
          j = m;
          m = i8;
        } 
      } 
      b++;
    } 
    paramInt1 = getPaddingRight();
    paramInt2 = getPaddingBottom();
    setMeasuredDimension(getMeasuredDimension(i2, i5, j + paramInt1), getMeasuredDimension(i4, i3, n + paramInt2));
  }
  
  protected void setItemSpacing(int paramInt) {
    this.itemSpacing = paramInt;
  }
  
  protected void setLineSpacing(int paramInt) {
    this.lineSpacing = paramInt;
  }
  
  public void setSingleLine(boolean paramBoolean) {
    this.singleLine = paramBoolean;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\internal\FlowLayout.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */