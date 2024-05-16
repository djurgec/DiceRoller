package com.google.android.material.datepicker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.google.android.material.R;
import java.util.Collection;
import java.util.Iterator;

class MonthAdapter extends BaseAdapter {
  static final int MAXIMUM_WEEKS = UtcDates.getUtcCalendar().getMaximum(4);
  
  final CalendarConstraints calendarConstraints;
  
  CalendarStyle calendarStyle;
  
  final DateSelector<?> dateSelector;
  
  final Month month;
  
  private Collection<Long> previouslySelectedDates;
  
  MonthAdapter(Month paramMonth, DateSelector<?> paramDateSelector, CalendarConstraints paramCalendarConstraints) {
    this.month = paramMonth;
    this.dateSelector = paramDateSelector;
    this.calendarConstraints = paramCalendarConstraints;
    this.previouslySelectedDates = paramDateSelector.getSelectedDays();
  }
  
  private void initializeStyles(Context paramContext) {
    if (this.calendarStyle == null)
      this.calendarStyle = new CalendarStyle(paramContext); 
  }
  
  private boolean isSelected(long paramLong) {
    Iterator<Long> iterator = this.dateSelector.getSelectedDays().iterator();
    while (iterator.hasNext()) {
      long l = ((Long)iterator.next()).longValue();
      if (UtcDates.canonicalYearMonthDay(paramLong) == UtcDates.canonicalYearMonthDay(l))
        return true; 
    } 
    return false;
  }
  
  private void updateSelectedState(TextView paramTextView, long paramLong) {
    CalendarItemStyle calendarItemStyle;
    if (paramTextView == null)
      return; 
    if (this.calendarConstraints.getDateValidator().isValid(paramLong)) {
      paramTextView.setEnabled(true);
      if (isSelected(paramLong)) {
        calendarItemStyle = this.calendarStyle.selectedDay;
      } else if (UtcDates.getTodayCalendar().getTimeInMillis() == paramLong) {
        calendarItemStyle = this.calendarStyle.todayDay;
      } else {
        calendarItemStyle = this.calendarStyle.day;
      } 
    } else {
      paramTextView.setEnabled(false);
      calendarItemStyle = this.calendarStyle.invalidDay;
    } 
    calendarItemStyle.styleItem(paramTextView);
  }
  
  private void updateSelectedStateForDate(MaterialCalendarGridView paramMaterialCalendarGridView, long paramLong) {
    if (Month.create(paramLong).equals(this.month)) {
      int i = this.month.getDayOfMonth(paramLong);
      updateSelectedState((TextView)paramMaterialCalendarGridView.getChildAt(paramMaterialCalendarGridView.getAdapter().dayToPosition(i) - paramMaterialCalendarGridView.getFirstVisiblePosition()), paramLong);
    } 
  }
  
  int dayToPosition(int paramInt) {
    return firstPositionInMonth() + paramInt - 1;
  }
  
  int firstPositionInMonth() {
    return this.month.daysFromStartOfWeekToFirstOfMonth();
  }
  
  public int getCount() {
    return this.month.daysInMonth + firstPositionInMonth();
  }
  
  public Long getItem(int paramInt) {
    return (paramInt < this.month.daysFromStartOfWeekToFirstOfMonth() || paramInt > lastPositionInMonth()) ? null : Long.valueOf(this.month.getDay(positionToDay(paramInt)));
  }
  
  public long getItemId(int paramInt) {
    return (paramInt / this.month.daysInWeek);
  }
  
  public TextView getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
    initializeStyles(paramViewGroup.getContext());
    TextView textView = (TextView)paramView;
    if (paramView == null)
      textView = (TextView)LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.mtrl_calendar_day, paramViewGroup, false); 
    int i = paramInt - firstPositionInMonth();
    if (i < 0 || i >= this.month.daysInMonth) {
      textView.setVisibility(8);
      textView.setEnabled(false);
    } else {
      i++;
      textView.setTag(this.month);
      textView.setText(String.format((textView.getResources().getConfiguration()).locale, "%d", new Object[] { Integer.valueOf(i) }));
      long l = this.month.getDay(i);
      if (this.month.year == (Month.current()).year) {
        textView.setContentDescription(DateStrings.getMonthDayOfWeekDay(l));
      } else {
        textView.setContentDescription(DateStrings.getYearMonthDayOfWeekDay(l));
      } 
      textView.setVisibility(0);
      textView.setEnabled(true);
    } 
    Long long_ = getItem(paramInt);
    if (long_ == null)
      return textView; 
    updateSelectedState(textView, long_.longValue());
    return textView;
  }
  
  public boolean hasStableIds() {
    return true;
  }
  
  boolean isFirstInRow(int paramInt) {
    boolean bool;
    if (paramInt % this.month.daysInWeek == 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  boolean isLastInRow(int paramInt) {
    boolean bool;
    if ((paramInt + 1) % this.month.daysInWeek == 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  int lastPositionInMonth() {
    return this.month.daysFromStartOfWeekToFirstOfMonth() + this.month.daysInMonth - 1;
  }
  
  int positionToDay(int paramInt) {
    return paramInt - this.month.daysFromStartOfWeekToFirstOfMonth() + 1;
  }
  
  public void updateSelectedStates(MaterialCalendarGridView paramMaterialCalendarGridView) {
    Iterator<Long> iterator = this.previouslySelectedDates.iterator();
    while (iterator.hasNext())
      updateSelectedStateForDate(paramMaterialCalendarGridView, ((Long)iterator.next()).longValue()); 
    DateSelector<?> dateSelector = this.dateSelector;
    if (dateSelector != null) {
      Iterator<Long> iterator1 = dateSelector.getSelectedDays().iterator();
      while (iterator1.hasNext())
        updateSelectedStateForDate(paramMaterialCalendarGridView, ((Long)iterator1.next()).longValue()); 
      this.previouslySelectedDates = this.dateSelector.getSelectedDays();
    } 
  }
  
  boolean withinMonth(int paramInt) {
    boolean bool;
    if (paramInt >= firstPositionInMonth() && paramInt <= lastPositionInMonth()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\datepicker\MonthAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */