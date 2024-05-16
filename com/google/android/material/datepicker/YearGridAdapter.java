package com.google.android.material.datepicker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.R;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;

class YearGridAdapter extends RecyclerView.Adapter<YearGridAdapter.ViewHolder> {
  private final MaterialCalendar<?> materialCalendar;
  
  YearGridAdapter(MaterialCalendar<?> paramMaterialCalendar) {
    this.materialCalendar = paramMaterialCalendar;
  }
  
  private View.OnClickListener createYearClickListener(final int year) {
    return new View.OnClickListener() {
        final YearGridAdapter this$0;
        
        final int val$year;
        
        public void onClick(View param1View) {
          Month month = Month.create(year, (YearGridAdapter.this.materialCalendar.getCurrentMonth()).month);
          month = YearGridAdapter.this.materialCalendar.getCalendarConstraints().clamp(month);
          YearGridAdapter.this.materialCalendar.setCurrentMonth(month);
          YearGridAdapter.this.materialCalendar.setSelector(MaterialCalendar.CalendarSelector.DAY);
        }
      };
  }
  
  public int getItemCount() {
    return this.materialCalendar.getCalendarConstraints().getYearSpan();
  }
  
  int getPositionForYear(int paramInt) {
    return paramInt - (this.materialCalendar.getCalendarConstraints().getStart()).year;
  }
  
  int getYearForPosition(int paramInt) {
    return (this.materialCalendar.getCalendarConstraints().getStart()).year + paramInt;
  }
  
  public void onBindViewHolder(ViewHolder paramViewHolder, int paramInt) {
    CalendarItemStyle calendarItemStyle;
    paramInt = getYearForPosition(paramInt);
    String str = paramViewHolder.textView.getContext().getString(R.string.mtrl_picker_navigate_to_year_description);
    paramViewHolder.textView.setText(String.format(Locale.getDefault(), "%d", new Object[] { Integer.valueOf(paramInt) }));
    paramViewHolder.textView.setContentDescription(String.format(str, new Object[] { Integer.valueOf(paramInt) }));
    CalendarStyle calendarStyle = this.materialCalendar.getCalendarStyle();
    Calendar calendar = UtcDates.getTodayCalendar();
    if (calendar.get(1) == paramInt) {
      calendarItemStyle = calendarStyle.todayYear;
    } else {
      calendarItemStyle = calendarStyle.year;
    } 
    Iterator<Long> iterator = this.materialCalendar.getDateSelector().getSelectedDays().iterator();
    while (iterator.hasNext()) {
      calendar.setTimeInMillis(((Long)iterator.next()).longValue());
      if (calendar.get(1) == paramInt)
        calendarItemStyle = calendarStyle.selectedYear; 
    } 
    calendarItemStyle.styleItem(paramViewHolder.textView);
    paramViewHolder.textView.setOnClickListener(createYearClickListener(paramInt));
  }
  
  public ViewHolder onCreateViewHolder(ViewGroup paramViewGroup, int paramInt) {
    return new ViewHolder((TextView)LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.mtrl_calendar_year, paramViewGroup, false));
  }
  
  public static class ViewHolder extends RecyclerView.ViewHolder {
    final TextView textView;
    
    ViewHolder(TextView param1TextView) {
      super((View)param1TextView);
      this.textView = param1TextView;
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\datepicker\YearGridAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */