package com.google.android.material.datepicker;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.google.android.material.R;
import java.util.Calendar;
import java.util.Locale;

class DaysOfWeekAdapter extends BaseAdapter {
  private static final int CALENDAR_DAY_STYLE;
  
  private static final int NARROW_FORMAT = 4;
  
  private final Calendar calendar;
  
  private final int daysInWeek;
  
  private final int firstDayOfWeek;
  
  static {
    boolean bool;
    if (Build.VERSION.SDK_INT >= 26) {
      bool = true;
    } else {
      bool = true;
    } 
    CALENDAR_DAY_STYLE = bool;
  }
  
  public DaysOfWeekAdapter() {
    Calendar calendar = UtcDates.getUtcCalendar();
    this.calendar = calendar;
    this.daysInWeek = calendar.getMaximum(7);
    this.firstDayOfWeek = calendar.getFirstDayOfWeek();
  }
  
  private int positionToDayOfWeek(int paramInt) {
    int i = this.firstDayOfWeek + paramInt;
    int j = this.daysInWeek;
    paramInt = i;
    if (i > j)
      paramInt = i - j; 
    return paramInt;
  }
  
  public int getCount() {
    return this.daysInWeek;
  }
  
  public Integer getItem(int paramInt) {
    return (paramInt >= this.daysInWeek) ? null : Integer.valueOf(positionToDayOfWeek(paramInt));
  }
  
  public long getItemId(int paramInt) {
    return 0L;
  }
  
  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
    TextView textView = (TextView)paramView;
    if (paramView == null)
      textView = (TextView)LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.mtrl_calendar_day_of_week, paramViewGroup, false); 
    this.calendar.set(7, positionToDayOfWeek(paramInt));
    Locale locale = (textView.getResources().getConfiguration()).locale;
    textView.setText(this.calendar.getDisplayName(7, CALENDAR_DAY_STYLE, locale));
    textView.setContentDescription(String.format(paramViewGroup.getContext().getString(R.string.mtrl_picker_day_of_week_column_header), new Object[] { this.calendar.getDisplayName(7, 2, Locale.getDefault()) }));
    return (View)textView;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\datepicker\DaysOfWeekAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */