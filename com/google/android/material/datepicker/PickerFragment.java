package com.google.android.material.datepicker;

import androidx.fragment.app.Fragment;
import java.util.LinkedHashSet;

abstract class PickerFragment<S> extends Fragment {
  protected final LinkedHashSet<OnSelectionChangedListener<S>> onSelectionChangedListeners = new LinkedHashSet<>();
  
  boolean addOnSelectionChangedListener(OnSelectionChangedListener<S> paramOnSelectionChangedListener) {
    return this.onSelectionChangedListeners.add(paramOnSelectionChangedListener);
  }
  
  void clearOnSelectionChangedListeners() {
    this.onSelectionChangedListeners.clear();
  }
  
  abstract DateSelector<S> getDateSelector();
  
  boolean removeOnSelectionChangedListener(OnSelectionChangedListener<S> paramOnSelectionChangedListener) {
    return this.onSelectionChangedListeners.remove(paramOnSelectionChangedListener);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\datepicker\PickerFragment.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */