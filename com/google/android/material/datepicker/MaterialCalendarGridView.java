package com.google.android.material.datepicker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Adapter;
import android.widget.GridView;
import android.widget.ListAdapter;
import androidx.core.util.Pair;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import com.google.android.material.R;
import com.google.android.material.internal.ViewUtils;
import java.util.Calendar;

final class MaterialCalendarGridView extends GridView {
  private final Calendar dayCompute = UtcDates.getUtcCalendar();
  
  private final boolean nestedScrollable;
  
  public MaterialCalendarGridView(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public MaterialCalendarGridView(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public MaterialCalendarGridView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    if (MaterialDatePicker.isFullscreen(getContext())) {
      setNextFocusLeftId(R.id.cancel_button);
      setNextFocusRightId(R.id.confirm_button);
    } 
    this.nestedScrollable = MaterialDatePicker.isNestedScrollable(getContext());
    ViewCompat.setAccessibilityDelegate((View)this, new AccessibilityDelegateCompat() {
          final MaterialCalendarGridView this$0;
          
          public void onInitializeAccessibilityNodeInfo(View param1View, AccessibilityNodeInfoCompat param1AccessibilityNodeInfoCompat) {
            super.onInitializeAccessibilityNodeInfo(param1View, param1AccessibilityNodeInfoCompat);
            param1AccessibilityNodeInfoCompat.setCollectionInfo(null);
          }
        });
  }
  
  private void gainFocus(int paramInt, Rect paramRect) {
    if (paramInt == 33) {
      setSelection(getAdapter().lastPositionInMonth());
    } else if (paramInt == 130) {
      setSelection(getAdapter().firstPositionInMonth());
    } else {
      super.onFocusChanged(true, paramInt, paramRect);
    } 
  }
  
  private static int horizontalMidPoint(View paramView) {
    return paramView.getLeft() + paramView.getWidth() / 2;
  }
  
  private static boolean skipMonth(Long paramLong1, Long paramLong2, Long paramLong3, Long paramLong4) {
    boolean bool2 = true;
    if (paramLong1 == null || paramLong2 == null || paramLong3 == null || paramLong4 == null)
      return true; 
    boolean bool1 = bool2;
    if (paramLong3.longValue() <= paramLong2.longValue())
      if (paramLong4.longValue() < paramLong1.longValue()) {
        bool1 = bool2;
      } else {
        bool1 = false;
      }  
    return bool1;
  }
  
  public MonthAdapter getAdapter() {
    return (MonthAdapter)super.getAdapter();
  }
  
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    getAdapter().notifyDataSetChanged();
  }
  
  protected final void onDraw(Canvas paramCanvas) {
    super.onDraw(paramCanvas);
    MonthAdapter monthAdapter = getAdapter();
    DateSelector<?> dateSelector = monthAdapter.dateSelector;
    CalendarStyle calendarStyle = monthAdapter.calendarStyle;
    Long long_2 = monthAdapter.getItem(monthAdapter.firstPositionInMonth());
    Long long_1 = monthAdapter.getItem(monthAdapter.lastPositionInMonth());
    for (Pair<Long, Long> pair : dateSelector.getSelectedRanges()) {
      int i;
      int j;
      int k;
      int m;
      if (pair.first == null || pair.second == null)
        continue; 
      long l2 = ((Long)pair.first).longValue();
      long l1 = ((Long)pair.second).longValue();
      if (skipMonth(long_2, long_1, Long.valueOf(l2), Long.valueOf(l1)))
        continue; 
      boolean bool = ViewUtils.isLayoutRtl((View)this);
      if (l2 < long_2.longValue()) {
        m = monthAdapter.firstPositionInMonth();
        if (monthAdapter.isFirstInRow(m)) {
          i = 0;
        } else if (!bool) {
          i = getChildAt(m - 1).getRight();
        } else {
          i = getChildAt(m - 1).getLeft();
        } 
      } else {
        this.dayCompute.setTimeInMillis(l2);
        m = monthAdapter.dayToPosition(this.dayCompute.get(5));
        i = horizontalMidPoint(getChildAt(m));
      } 
      if (l1 > long_1.longValue()) {
        k = Math.min(monthAdapter.lastPositionInMonth(), getChildCount() - 1);
        if (monthAdapter.isLastInRow(k)) {
          j = getWidth();
        } else if (!bool) {
          j = getChildAt(k).getRight();
        } else {
          j = getChildAt(k).getLeft();
        } 
      } else {
        this.dayCompute.setTimeInMillis(l1);
        k = monthAdapter.dayToPosition(this.dayCompute.get(5));
        j = horizontalMidPoint(getChildAt(k));
      } 
      int i3 = (int)monthAdapter.getItemId(m);
      int n = (int)monthAdapter.getItemId(k);
      int i1 = i3;
      int i2 = k;
      while (i1 <= n) {
        int i9 = i1 * getNumColumns();
        int i4 = i9 + getNumColumns() - 1;
        View view = getChildAt(i9);
        int i7 = view.getTop();
        int i5 = calendarStyle.day.getTopInset();
        int i8 = view.getBottom();
        int i6 = calendarStyle.day.getBottomInset();
        if (!bool) {
          if (i9 > m) {
            k = 0;
          } else {
            k = i;
          } 
          if (i2 > i4) {
            i4 = getWidth();
          } else {
            i4 = j;
          } 
        } else {
          if (i2 > i4) {
            k = 0;
          } else {
            k = j;
          } 
          if (i9 > m) {
            i4 = getWidth();
          } else {
            i4 = i;
          } 
        } 
        paramCanvas.drawRect(k, (i7 + i5), i4, (i8 - i6), calendarStyle.rangeFill);
        i1++;
      } 
    } 
  }
  
  protected void onFocusChanged(boolean paramBoolean, int paramInt, Rect paramRect) {
    if (paramBoolean) {
      gainFocus(paramInt, paramRect);
    } else {
      super.onFocusChanged(false, paramInt, paramRect);
    } 
  }
  
  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
    if (!super.onKeyDown(paramInt, paramKeyEvent))
      return false; 
    if (getSelectedItemPosition() == -1 || getSelectedItemPosition() >= getAdapter().firstPositionInMonth())
      return true; 
    if (19 == paramInt) {
      setSelection(getAdapter().firstPositionInMonth());
      return true;
    } 
    return false;
  }
  
  public void onMeasure(int paramInt1, int paramInt2) {
    if (this.nestedScrollable) {
      super.onMeasure(paramInt1, View.MeasureSpec.makeMeasureSpec(16777215, -2147483648));
      (getLayoutParams()).height = getMeasuredHeight();
    } else {
      super.onMeasure(paramInt1, paramInt2);
    } 
  }
  
  public final void setAdapter(ListAdapter paramListAdapter) {
    if (paramListAdapter instanceof MonthAdapter) {
      super.setAdapter(paramListAdapter);
      return;
    } 
    throw new IllegalArgumentException(String.format("%1$s must have its Adapter set to a %2$s", new Object[] { MaterialCalendarGridView.class.getCanonicalName(), MonthAdapter.class.getCanonicalName() }));
  }
  
  public void setSelection(int paramInt) {
    if (paramInt < getAdapter().firstPositionInMonth()) {
      super.setSelection(getAdapter().firstPositionInMonth());
    } else {
      super.setSelection(paramInt);
    } 
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\datepicker\MaterialCalendarGridView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */