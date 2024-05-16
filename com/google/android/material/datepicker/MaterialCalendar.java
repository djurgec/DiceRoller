package com.google.android.material.datepicker;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;
import androidx.core.util.Pair;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.R;
import com.google.android.material.button.MaterialButton;
import java.util.Calendar;
import java.util.Iterator;

public final class MaterialCalendar<S> extends PickerFragment<S> {
  private static final String CALENDAR_CONSTRAINTS_KEY = "CALENDAR_CONSTRAINTS_KEY";
  
  private static final String CURRENT_MONTH_KEY = "CURRENT_MONTH_KEY";
  
  private static final String GRID_SELECTOR_KEY = "GRID_SELECTOR_KEY";
  
  static final Object MONTHS_VIEW_GROUP_TAG = "MONTHS_VIEW_GROUP_TAG";
  
  static final Object NAVIGATION_NEXT_TAG;
  
  static final Object NAVIGATION_PREV_TAG = "NAVIGATION_PREV_TAG";
  
  static final Object SELECTOR_TOGGLE_TAG;
  
  private static final int SMOOTH_SCROLL_MAX = 3;
  
  private static final String THEME_RES_ID_KEY = "THEME_RES_ID_KEY";
  
  private CalendarConstraints calendarConstraints;
  
  private CalendarSelector calendarSelector;
  
  private CalendarStyle calendarStyle;
  
  private Month current;
  
  private DateSelector<S> dateSelector;
  
  private View dayFrame;
  
  private RecyclerView recyclerView;
  
  private int themeResId;
  
  private View yearFrame;
  
  private RecyclerView yearSelector;
  
  static {
    NAVIGATION_NEXT_TAG = "NAVIGATION_NEXT_TAG";
    SELECTOR_TOGGLE_TAG = "SELECTOR_TOGGLE_TAG";
  }
  
  private void addActionsToMonthNavigation(View paramView, final MonthsPagerAdapter monthsPagerAdapter) {
    final MaterialButton monthDropSelect = (MaterialButton)paramView.findViewById(R.id.month_navigation_fragment_toggle);
    materialButton2.setTag(SELECTOR_TOGGLE_TAG);
    ViewCompat.setAccessibilityDelegate((View)materialButton2, new AccessibilityDelegateCompat() {
          final MaterialCalendar this$0;
          
          public void onInitializeAccessibilityNodeInfo(View param1View, AccessibilityNodeInfoCompat param1AccessibilityNodeInfoCompat) {
            String str;
            super.onInitializeAccessibilityNodeInfo(param1View, param1AccessibilityNodeInfoCompat);
            if (MaterialCalendar.this.dayFrame.getVisibility() == 0) {
              str = MaterialCalendar.this.getString(R.string.mtrl_picker_toggle_to_year_selection);
            } else {
              str = MaterialCalendar.this.getString(R.string.mtrl_picker_toggle_to_day_selection);
            } 
            param1AccessibilityNodeInfoCompat.setHintText(str);
          }
        });
    MaterialButton materialButton1 = (MaterialButton)paramView.findViewById(R.id.month_navigation_previous);
    materialButton1.setTag(NAVIGATION_PREV_TAG);
    MaterialButton materialButton3 = (MaterialButton)paramView.findViewById(R.id.month_navigation_next);
    materialButton3.setTag(NAVIGATION_NEXT_TAG);
    this.yearFrame = paramView.findViewById(R.id.mtrl_calendar_year_selector_frame);
    this.dayFrame = paramView.findViewById(R.id.mtrl_calendar_day_selector_frame);
    setSelector(CalendarSelector.DAY);
    materialButton2.setText(this.current.getLongName(paramView.getContext()));
    this.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
          final MaterialCalendar this$0;
          
          final MaterialButton val$monthDropSelect;
          
          final MonthsPagerAdapter val$monthsPagerAdapter;
          
          public void onScrollStateChanged(RecyclerView param1RecyclerView, int param1Int) {
            if (param1Int == 0) {
              CharSequence charSequence = monthDropSelect.getText();
              if (Build.VERSION.SDK_INT >= 16) {
                param1RecyclerView.announceForAccessibility(charSequence);
              } else {
                param1RecyclerView.sendAccessibilityEvent(2048);
              } 
            } 
          }
          
          public void onScrolled(RecyclerView param1RecyclerView, int param1Int1, int param1Int2) {
            if (param1Int1 < 0) {
              param1Int1 = MaterialCalendar.this.getLayoutManager().findFirstVisibleItemPosition();
            } else {
              param1Int1 = MaterialCalendar.this.getLayoutManager().findLastVisibleItemPosition();
            } 
            MaterialCalendar.access$602(MaterialCalendar.this, monthsPagerAdapter.getPageMonth(param1Int1));
            monthDropSelect.setText(monthsPagerAdapter.getPageTitle(param1Int1));
          }
        });
    materialButton2.setOnClickListener(new View.OnClickListener() {
          final MaterialCalendar this$0;
          
          public void onClick(View param1View) {
            MaterialCalendar.this.toggleVisibleSelector();
          }
        });
    materialButton3.setOnClickListener(new View.OnClickListener() {
          final MaterialCalendar this$0;
          
          final MonthsPagerAdapter val$monthsPagerAdapter;
          
          public void onClick(View param1View) {
            int i = MaterialCalendar.this.getLayoutManager().findFirstVisibleItemPosition();
            if (i + 1 < MaterialCalendar.this.recyclerView.getAdapter().getItemCount())
              MaterialCalendar.this.setCurrentMonth(monthsPagerAdapter.getPageMonth(i + 1)); 
          }
        });
    materialButton1.setOnClickListener(new View.OnClickListener() {
          final MaterialCalendar this$0;
          
          final MonthsPagerAdapter val$monthsPagerAdapter;
          
          public void onClick(View param1View) {
            int i = MaterialCalendar.this.getLayoutManager().findLastVisibleItemPosition();
            if (i - 1 >= 0)
              MaterialCalendar.this.setCurrentMonth(monthsPagerAdapter.getPageMonth(i - 1)); 
          }
        });
  }
  
  private RecyclerView.ItemDecoration createItemDecoration() {
    return new RecyclerView.ItemDecoration() {
        private final Calendar endItem = UtcDates.getUtcCalendar();
        
        private final Calendar startItem = UtcDates.getUtcCalendar();
        
        final MaterialCalendar this$0;
        
        public void onDraw(Canvas param1Canvas, RecyclerView param1RecyclerView, RecyclerView.State param1State) {
          if (!(param1RecyclerView.getAdapter() instanceof YearGridAdapter) || !(param1RecyclerView.getLayoutManager() instanceof GridLayoutManager))
            return; 
          YearGridAdapter yearGridAdapter = (YearGridAdapter)param1RecyclerView.getAdapter();
          GridLayoutManager gridLayoutManager = (GridLayoutManager)param1RecyclerView.getLayoutManager();
          for (Pair<Long, Long> pair : MaterialCalendar.this.dateSelector.getSelectedRanges()) {
            if (pair.first == null || pair.second == null)
              continue; 
            this.startItem.setTimeInMillis(((Long)pair.first).longValue());
            this.endItem.setTimeInMillis(((Long)pair.second).longValue());
            int i = yearGridAdapter.getPositionForYear(this.startItem.get(1));
            int j = yearGridAdapter.getPositionForYear(this.endItem.get(1));
            View view1 = gridLayoutManager.findViewByPosition(i);
            View view2 = gridLayoutManager.findViewByPosition(j);
            int k = i / gridLayoutManager.getSpanCount();
            int m = j / gridLayoutManager.getSpanCount();
            for (j = k; j <= m; j++) {
              View view = gridLayoutManager.findViewByPosition(gridLayoutManager.getSpanCount() * j);
              if (view != null) {
                boolean bool;
                int n;
                int i2 = view.getTop();
                int i3 = MaterialCalendar.this.calendarStyle.year.getTopInset();
                int i1 = view.getBottom();
                int i4 = MaterialCalendar.this.calendarStyle.year.getBottomInset();
                if (j == k) {
                  bool = view1.getLeft() + view1.getWidth() / 2;
                } else {
                  bool = false;
                } 
                if (j == m) {
                  n = view2.getLeft() + view2.getWidth() / 2;
                } else {
                  n = param1RecyclerView.getWidth();
                } 
                param1Canvas.drawRect(bool, (i2 + i3), n, (i1 - i4), MaterialCalendar.this.calendarStyle.rangeFill);
              } 
            } 
          } 
        }
      };
  }
  
  static int getDayHeight(Context paramContext) {
    return paramContext.getResources().getDimensionPixelSize(R.dimen.mtrl_calendar_day_height);
  }
  
  public static <T> MaterialCalendar<T> newInstance(DateSelector<T> paramDateSelector, int paramInt, CalendarConstraints paramCalendarConstraints) {
    MaterialCalendar<T> materialCalendar = new MaterialCalendar();
    Bundle bundle = new Bundle();
    bundle.putInt("THEME_RES_ID_KEY", paramInt);
    bundle.putParcelable("GRID_SELECTOR_KEY", paramDateSelector);
    bundle.putParcelable("CALENDAR_CONSTRAINTS_KEY", paramCalendarConstraints);
    bundle.putParcelable("CURRENT_MONTH_KEY", paramCalendarConstraints.getOpenAt());
    materialCalendar.setArguments(bundle);
    return materialCalendar;
  }
  
  private void postSmoothRecyclerViewScroll(final int position) {
    this.recyclerView.post(new Runnable() {
          final MaterialCalendar this$0;
          
          final int val$position;
          
          public void run() {
            MaterialCalendar.this.recyclerView.smoothScrollToPosition(position);
          }
        });
  }
  
  public boolean addOnSelectionChangedListener(OnSelectionChangedListener<S> paramOnSelectionChangedListener) {
    return super.addOnSelectionChangedListener(paramOnSelectionChangedListener);
  }
  
  CalendarConstraints getCalendarConstraints() {
    return this.calendarConstraints;
  }
  
  CalendarStyle getCalendarStyle() {
    return this.calendarStyle;
  }
  
  Month getCurrentMonth() {
    return this.current;
  }
  
  public DateSelector<S> getDateSelector() {
    return this.dateSelector;
  }
  
  LinearLayoutManager getLayoutManager() {
    return (LinearLayoutManager)this.recyclerView.getLayoutManager();
  }
  
  public void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    if (paramBundle == null)
      paramBundle = getArguments(); 
    this.themeResId = paramBundle.getInt("THEME_RES_ID_KEY");
    this.dateSelector = (DateSelector<S>)paramBundle.getParcelable("GRID_SELECTOR_KEY");
    this.calendarConstraints = (CalendarConstraints)paramBundle.getParcelable("CALENDAR_CONSTRAINTS_KEY");
    this.current = (Month)paramBundle.getParcelable("CURRENT_MONTH_KEY");
  }
  
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
    int j;
    ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(getContext(), this.themeResId);
    this.calendarStyle = new CalendarStyle((Context)contextThemeWrapper);
    paramLayoutInflater = paramLayoutInflater.cloneInContext((Context)contextThemeWrapper);
    Month month = this.calendarConstraints.getStart();
    if (MaterialDatePicker.isFullscreen((Context)contextThemeWrapper)) {
      j = R.layout.mtrl_calendar_vertical;
      i = 1;
    } else {
      j = R.layout.mtrl_calendar_horizontal;
      i = 0;
    } 
    View view = paramLayoutInflater.inflate(j, paramViewGroup, false);
    GridView gridView = (GridView)view.findViewById(R.id.mtrl_calendar_days_of_week);
    ViewCompat.setAccessibilityDelegate((View)gridView, new AccessibilityDelegateCompat() {
          final MaterialCalendar this$0;
          
          public void onInitializeAccessibilityNodeInfo(View param1View, AccessibilityNodeInfoCompat param1AccessibilityNodeInfoCompat) {
            super.onInitializeAccessibilityNodeInfo(param1View, param1AccessibilityNodeInfoCompat);
            param1AccessibilityNodeInfoCompat.setCollectionInfo(null);
          }
        });
    gridView.setAdapter((ListAdapter)new DaysOfWeekAdapter());
    gridView.setNumColumns(month.daysInWeek);
    gridView.setEnabled(false);
    this.recyclerView = (RecyclerView)view.findViewById(R.id.mtrl_calendar_months);
    SmoothCalendarLayoutManager smoothCalendarLayoutManager = new SmoothCalendarLayoutManager(getContext(), i, false) {
        final MaterialCalendar this$0;
        
        final int val$orientation;
        
        protected void calculateExtraLayoutSpace(RecyclerView.State param1State, int[] param1ArrayOfint) {
          if (orientation == 0) {
            param1ArrayOfint[0] = MaterialCalendar.this.recyclerView.getWidth();
            param1ArrayOfint[1] = MaterialCalendar.this.recyclerView.getWidth();
          } else {
            param1ArrayOfint[0] = MaterialCalendar.this.recyclerView.getHeight();
            param1ArrayOfint[1] = MaterialCalendar.this.recyclerView.getHeight();
          } 
        }
      };
    this.recyclerView.setLayoutManager((RecyclerView.LayoutManager)smoothCalendarLayoutManager);
    this.recyclerView.setTag(MONTHS_VIEW_GROUP_TAG);
    MonthsPagerAdapter monthsPagerAdapter = new MonthsPagerAdapter((Context)contextThemeWrapper, this.dateSelector, this.calendarConstraints, new OnDayClickListener() {
          final MaterialCalendar this$0;
          
          public void onDayClick(long param1Long) {
            if (MaterialCalendar.this.calendarConstraints.getDateValidator().isValid(param1Long)) {
              MaterialCalendar.this.dateSelector.select(param1Long);
              Iterator<OnSelectionChangedListener> iterator = MaterialCalendar.this.onSelectionChangedListeners.iterator();
              while (iterator.hasNext())
                ((OnSelectionChangedListener)iterator.next()).onSelectionChanged(MaterialCalendar.this.dateSelector.getSelection()); 
              MaterialCalendar.this.recyclerView.getAdapter().notifyDataSetChanged();
              if (MaterialCalendar.this.yearSelector != null)
                MaterialCalendar.this.yearSelector.getAdapter().notifyDataSetChanged(); 
            } 
          }
        });
    this.recyclerView.setAdapter(monthsPagerAdapter);
    final int orientation = contextThemeWrapper.getResources().getInteger(R.integer.mtrl_calendar_year_selector_span);
    RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.mtrl_calendar_year_selector_frame);
    this.yearSelector = recyclerView;
    if (recyclerView != null) {
      recyclerView.setHasFixedSize(true);
      this.yearSelector.setLayoutManager((RecyclerView.LayoutManager)new GridLayoutManager((Context)contextThemeWrapper, i, 1, false));
      this.yearSelector.setAdapter(new YearGridAdapter(this));
      this.yearSelector.addItemDecoration(createItemDecoration());
    } 
    if (view.findViewById(R.id.month_navigation_fragment_toggle) != null)
      addActionsToMonthNavigation(view, monthsPagerAdapter); 
    if (!MaterialDatePicker.isFullscreen((Context)contextThemeWrapper))
      (new PagerSnapHelper()).attachToRecyclerView(this.recyclerView); 
    this.recyclerView.scrollToPosition(monthsPagerAdapter.getPosition(this.current));
    return view;
  }
  
  public void onSaveInstanceState(Bundle paramBundle) {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putInt("THEME_RES_ID_KEY", this.themeResId);
    paramBundle.putParcelable("GRID_SELECTOR_KEY", this.dateSelector);
    paramBundle.putParcelable("CALENDAR_CONSTRAINTS_KEY", this.calendarConstraints);
    paramBundle.putParcelable("CURRENT_MONTH_KEY", this.current);
  }
  
  void setCurrentMonth(Month paramMonth) {
    MonthsPagerAdapter monthsPagerAdapter = (MonthsPagerAdapter)this.recyclerView.getAdapter();
    int j = monthsPagerAdapter.getPosition(paramMonth);
    int k = j - monthsPagerAdapter.getPosition(this.current);
    int i = Math.abs(k);
    boolean bool = true;
    if (i > 3) {
      i = 1;
    } else {
      i = 0;
    } 
    if (k <= 0)
      bool = false; 
    this.current = paramMonth;
    if (i != 0 && bool) {
      this.recyclerView.scrollToPosition(j - 3);
      postSmoothRecyclerViewScroll(j);
    } else if (i != 0) {
      this.recyclerView.scrollToPosition(j + 3);
      postSmoothRecyclerViewScroll(j);
    } else {
      postSmoothRecyclerViewScroll(j);
    } 
  }
  
  void setSelector(CalendarSelector paramCalendarSelector) {
    this.calendarSelector = paramCalendarSelector;
    if (paramCalendarSelector == CalendarSelector.YEAR) {
      this.yearSelector.getLayoutManager().scrollToPosition(((YearGridAdapter)this.yearSelector.getAdapter()).getPositionForYear(this.current.year));
      this.yearFrame.setVisibility(0);
      this.dayFrame.setVisibility(8);
    } else if (paramCalendarSelector == CalendarSelector.DAY) {
      this.yearFrame.setVisibility(8);
      this.dayFrame.setVisibility(0);
      setCurrentMonth(this.current);
    } 
  }
  
  void toggleVisibleSelector() {
    if (this.calendarSelector == CalendarSelector.YEAR) {
      setSelector(CalendarSelector.DAY);
    } else if (this.calendarSelector == CalendarSelector.DAY) {
      setSelector(CalendarSelector.YEAR);
    } 
  }
  
  enum CalendarSelector {
    DAY, YEAR;
    
    private static final CalendarSelector[] $VALUES;
    
    static {
      CalendarSelector calendarSelector1 = new CalendarSelector("DAY", 0);
      DAY = calendarSelector1;
      CalendarSelector calendarSelector2 = new CalendarSelector("YEAR", 1);
      YEAR = calendarSelector2;
      $VALUES = new CalendarSelector[] { calendarSelector1, calendarSelector2 };
    }
  }
  
  static interface OnDayClickListener {
    void onDayClick(long param1Long);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\datepicker\MaterialCalendar.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */