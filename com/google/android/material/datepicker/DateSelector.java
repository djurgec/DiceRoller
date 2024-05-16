package com.google.android.material.datepicker;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.util.Pair;
import java.util.Collection;

public interface DateSelector<S> extends Parcelable {
  int getDefaultThemeResId(Context paramContext);
  
  int getDefaultTitleResId();
  
  Collection<Long> getSelectedDays();
  
  Collection<Pair<Long, Long>> getSelectedRanges();
  
  S getSelection();
  
  String getSelectionDisplayString(Context paramContext);
  
  boolean isSelectionComplete();
  
  View onCreateTextInputView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle, CalendarConstraints paramCalendarConstraints, OnSelectionChangedListener<S> paramOnSelectionChangedListener);
  
  void select(long paramLong);
  
  void setSelection(S paramS);
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\datepicker\DateSelector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */