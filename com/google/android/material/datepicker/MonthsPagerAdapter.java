package com.google.android.material.datepicker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.R;

class MonthsPagerAdapter extends RecyclerView.Adapter<MonthsPagerAdapter.ViewHolder> {
  private final CalendarConstraints calendarConstraints;
  
  private final Context context;
  
  private final DateSelector<?> dateSelector;
  
  private final int itemHeight;
  
  private final MaterialCalendar.OnDayClickListener onDayClickListener;
  
  MonthsPagerAdapter(Context paramContext, DateSelector<?> paramDateSelector, CalendarConstraints paramCalendarConstraints, MaterialCalendar.OnDayClickListener paramOnDayClickListener) {
    Month month2 = paramCalendarConstraints.getStart();
    Month month1 = paramCalendarConstraints.getEnd();
    Month month3 = paramCalendarConstraints.getOpenAt();
    if (month2.compareTo(month3) <= 0) {
      if (month3.compareTo(month1) <= 0) {
        byte b;
        int j = MonthAdapter.MAXIMUM_WEEKS;
        int i = MaterialCalendar.getDayHeight(paramContext);
        if (MaterialDatePicker.isFullscreen(paramContext)) {
          b = MaterialCalendar.getDayHeight(paramContext);
        } else {
          b = 0;
        } 
        this.context = paramContext;
        this.itemHeight = j * i + b;
        this.calendarConstraints = paramCalendarConstraints;
        this.dateSelector = paramDateSelector;
        this.onDayClickListener = paramOnDayClickListener;
        setHasStableIds(true);
        return;
      } 
      throw new IllegalArgumentException("currentPage cannot be after lastPage");
    } 
    throw new IllegalArgumentException("firstPage cannot be after currentPage");
  }
  
  public int getItemCount() {
    return this.calendarConstraints.getMonthSpan();
  }
  
  public long getItemId(int paramInt) {
    return this.calendarConstraints.getStart().monthsLater(paramInt).getStableId();
  }
  
  Month getPageMonth(int paramInt) {
    return this.calendarConstraints.getStart().monthsLater(paramInt);
  }
  
  CharSequence getPageTitle(int paramInt) {
    return getPageMonth(paramInt).getLongName(this.context);
  }
  
  int getPosition(Month paramMonth) {
    return this.calendarConstraints.getStart().monthsUntil(paramMonth);
  }
  
  public void onBindViewHolder(ViewHolder paramViewHolder, int paramInt) {
    Month month = this.calendarConstraints.getStart().monthsLater(paramInt);
    paramViewHolder.monthTitle.setText(month.getLongName(paramViewHolder.itemView.getContext()));
    final MaterialCalendarGridView monthGrid = (MaterialCalendarGridView)paramViewHolder.monthGrid.findViewById(R.id.month_grid);
    if (materialCalendarGridView.getAdapter() != null && month.equals((materialCalendarGridView.getAdapter()).month)) {
      materialCalendarGridView.invalidate();
      materialCalendarGridView.getAdapter().updateSelectedStates(materialCalendarGridView);
    } else {
      MonthAdapter monthAdapter = new MonthAdapter(month, this.dateSelector, this.calendarConstraints);
      materialCalendarGridView.setNumColumns(month.daysInWeek);
      materialCalendarGridView.setAdapter((ListAdapter)monthAdapter);
    } 
    materialCalendarGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
          final MonthsPagerAdapter this$0;
          
          final MaterialCalendarGridView val$monthGrid;
          
          public void onItemClick(AdapterView<?> param1AdapterView, View param1View, int param1Int, long param1Long) {
            if (monthGrid.getAdapter().withinMonth(param1Int))
              MonthsPagerAdapter.this.onDayClickListener.onDayClick(monthGrid.getAdapter().getItem(param1Int).longValue()); 
          }
        });
  }
  
  public ViewHolder onCreateViewHolder(ViewGroup paramViewGroup, int paramInt) {
    LinearLayout linearLayout = (LinearLayout)LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.mtrl_calendar_month_labeled, paramViewGroup, false);
    if (MaterialDatePicker.isFullscreen(paramViewGroup.getContext())) {
      linearLayout.setLayoutParams((ViewGroup.LayoutParams)new RecyclerView.LayoutParams(-1, this.itemHeight));
      return new ViewHolder(linearLayout, true);
    } 
    return new ViewHolder(linearLayout, false);
  }
  
  public static class ViewHolder extends RecyclerView.ViewHolder {
    final MaterialCalendarGridView monthGrid;
    
    final TextView monthTitle;
    
    ViewHolder(LinearLayout param1LinearLayout, boolean param1Boolean) {
      super((View)param1LinearLayout);
      TextView textView = (TextView)param1LinearLayout.findViewById(R.id.month_title);
      this.monthTitle = textView;
      ViewCompat.setAccessibilityHeading((View)textView, true);
      this.monthGrid = (MaterialCalendarGridView)param1LinearLayout.findViewById(R.id.month_grid);
      if (!param1Boolean)
        textView.setVisibility(8); 
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\datepicker\MonthsPagerAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */