package com.google.android.material.datepicker;

import android.content.Context;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.Iterator;

public final class MaterialTextInputPicker<S> extends PickerFragment<S> {
  private static final String CALENDAR_CONSTRAINTS_KEY = "CALENDAR_CONSTRAINTS_KEY";
  
  private static final String DATE_SELECTOR_KEY = "DATE_SELECTOR_KEY";
  
  private static final String THEME_RES_ID_KEY = "THEME_RES_ID_KEY";
  
  private CalendarConstraints calendarConstraints;
  
  private DateSelector<S> dateSelector;
  
  private int themeResId;
  
  static <T> MaterialTextInputPicker<T> newInstance(DateSelector<T> paramDateSelector, int paramInt, CalendarConstraints paramCalendarConstraints) {
    MaterialTextInputPicker<T> materialTextInputPicker = new MaterialTextInputPicker();
    Bundle bundle = new Bundle();
    bundle.putInt("THEME_RES_ID_KEY", paramInt);
    bundle.putParcelable("DATE_SELECTOR_KEY", paramDateSelector);
    bundle.putParcelable("CALENDAR_CONSTRAINTS_KEY", paramCalendarConstraints);
    materialTextInputPicker.setArguments(bundle);
    return materialTextInputPicker;
  }
  
  public DateSelector<S> getDateSelector() {
    DateSelector<S> dateSelector = this.dateSelector;
    if (dateSelector != null)
      return dateSelector; 
    throw new IllegalStateException("dateSelector should not be null. Use MaterialTextInputPicker#newInstance() to create this fragment with a DateSelector, and call this method after the fragment has been created.");
  }
  
  public void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    if (paramBundle == null)
      paramBundle = getArguments(); 
    this.themeResId = paramBundle.getInt("THEME_RES_ID_KEY");
    this.dateSelector = (DateSelector<S>)paramBundle.getParcelable("DATE_SELECTOR_KEY");
    this.calendarConstraints = (CalendarConstraints)paramBundle.getParcelable("CALENDAR_CONSTRAINTS_KEY");
  }
  
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
    paramLayoutInflater = paramLayoutInflater.cloneInContext((Context)new ContextThemeWrapper(getContext(), this.themeResId));
    return this.dateSelector.onCreateTextInputView(paramLayoutInflater, paramViewGroup, paramBundle, this.calendarConstraints, new OnSelectionChangedListener<S>() {
          final MaterialTextInputPicker this$0;
          
          public void onIncompleteSelectionChanged() {
            Iterator<OnSelectionChangedListener> iterator = MaterialTextInputPicker.this.onSelectionChangedListeners.iterator();
            while (iterator.hasNext())
              ((OnSelectionChangedListener)iterator.next()).onIncompleteSelectionChanged(); 
          }
          
          public void onSelectionChanged(S param1S) {
            Iterator<OnSelectionChangedListener<S>> iterator = MaterialTextInputPicker.this.onSelectionChangedListeners.iterator();
            while (iterator.hasNext())
              ((OnSelectionChangedListener<S>)iterator.next()).onSelectionChanged(param1S); 
          }
        });
  }
  
  public void onSaveInstanceState(Bundle paramBundle) {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putInt("THEME_RES_ID_KEY", this.themeResId);
    paramBundle.putParcelable("DATE_SELECTOR_KEY", this.dateSelector);
    paramBundle.putParcelable("CALENDAR_CONSTRAINTS_KEY", this.calendarConstraints);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\datepicker\MaterialTextInputPicker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */