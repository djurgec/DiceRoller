package com.google.android.material.datepicker;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.core.util.Pair;
import androidx.core.util.Preconditions;
import com.google.android.material.R;
import com.google.android.material.internal.ManufacturerUtils;
import com.google.android.material.internal.ViewUtils;
import com.google.android.material.resources.MaterialAttributes;
import com.google.android.material.textfield.TextInputLayout;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;

public class RangeDateSelector implements DateSelector<Pair<Long, Long>> {
  public static final Parcelable.Creator<RangeDateSelector> CREATOR = new Parcelable.Creator<RangeDateSelector>() {
      public RangeDateSelector createFromParcel(Parcel param1Parcel) {
        RangeDateSelector rangeDateSelector = new RangeDateSelector();
        RangeDateSelector.access$302(rangeDateSelector, (Long)param1Parcel.readValue(Long.class.getClassLoader()));
        RangeDateSelector.access$402(rangeDateSelector, (Long)param1Parcel.readValue(Long.class.getClassLoader()));
        return rangeDateSelector;
      }
      
      public RangeDateSelector[] newArray(int param1Int) {
        return new RangeDateSelector[param1Int];
      }
    };
  
  private final String invalidRangeEndError = " ";
  
  private String invalidRangeStartError;
  
  private Long proposedTextEnd = null;
  
  private Long proposedTextStart = null;
  
  private Long selectedEndItem = null;
  
  private Long selectedStartItem = null;
  
  private void clearInvalidRange(TextInputLayout paramTextInputLayout1, TextInputLayout paramTextInputLayout2) {
    if (paramTextInputLayout1.getError() != null && this.invalidRangeStartError.contentEquals(paramTextInputLayout1.getError()))
      paramTextInputLayout1.setError(null); 
    if (paramTextInputLayout2.getError() != null && " ".contentEquals(paramTextInputLayout2.getError()))
      paramTextInputLayout2.setError(null); 
  }
  
  private boolean isValidRange(long paramLong1, long paramLong2) {
    boolean bool;
    if (paramLong1 <= paramLong2) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private void setInvalidRange(TextInputLayout paramTextInputLayout1, TextInputLayout paramTextInputLayout2) {
    paramTextInputLayout1.setError(this.invalidRangeStartError);
    paramTextInputLayout2.setError(" ");
  }
  
  private void updateIfValidTextProposal(TextInputLayout paramTextInputLayout1, TextInputLayout paramTextInputLayout2, OnSelectionChangedListener<Pair<Long, Long>> paramOnSelectionChangedListener) {
    Long long_ = this.proposedTextStart;
    if (long_ == null || this.proposedTextEnd == null) {
      clearInvalidRange(paramTextInputLayout1, paramTextInputLayout2);
      paramOnSelectionChangedListener.onIncompleteSelectionChanged();
      return;
    } 
    if (isValidRange(long_.longValue(), this.proposedTextEnd.longValue())) {
      this.selectedStartItem = this.proposedTextStart;
      this.selectedEndItem = this.proposedTextEnd;
      paramOnSelectionChangedListener.onSelectionChanged(getSelection());
    } else {
      setInvalidRange(paramTextInputLayout1, paramTextInputLayout2);
      paramOnSelectionChangedListener.onIncompleteSelectionChanged();
    } 
  }
  
  public int describeContents() {
    return 0;
  }
  
  public int getDefaultThemeResId(Context paramContext) {
    Resources resources = paramContext.getResources();
    DisplayMetrics displayMetrics = resources.getDisplayMetrics();
    int i = resources.getDimensionPixelSize(R.dimen.mtrl_calendar_maximum_default_fullscreen_minor_axis);
    if (Math.min(displayMetrics.widthPixels, displayMetrics.heightPixels) > i) {
      i = R.attr.materialCalendarTheme;
    } else {
      i = R.attr.materialCalendarFullscreenTheme;
    } 
    return MaterialAttributes.resolveOrThrow(paramContext, i, MaterialDatePicker.class.getCanonicalName());
  }
  
  public int getDefaultTitleResId() {
    return R.string.mtrl_picker_range_header_title;
  }
  
  public Collection<Long> getSelectedDays() {
    ArrayList<Long> arrayList = new ArrayList();
    Long long_ = this.selectedStartItem;
    if (long_ != null)
      arrayList.add(long_); 
    long_ = this.selectedEndItem;
    if (long_ != null)
      arrayList.add(long_); 
    return arrayList;
  }
  
  public Collection<Pair<Long, Long>> getSelectedRanges() {
    if (this.selectedStartItem == null || this.selectedEndItem == null)
      return new ArrayList<>(); 
    ArrayList<Pair> arrayList = new ArrayList();
    arrayList.add(new Pair(this.selectedStartItem, this.selectedEndItem));
    return (Collection)arrayList;
  }
  
  public Pair<Long, Long> getSelection() {
    return new Pair(this.selectedStartItem, this.selectedEndItem);
  }
  
  public String getSelectionDisplayString(Context paramContext) {
    Resources resources = paramContext.getResources();
    Long long_1 = this.selectedStartItem;
    if (long_1 == null && this.selectedEndItem == null)
      return resources.getString(R.string.mtrl_picker_range_header_unselected); 
    Long long_2 = this.selectedEndItem;
    if (long_2 == null)
      return resources.getString(R.string.mtrl_picker_range_header_only_start_selected, new Object[] { DateStrings.getDateString(this.selectedStartItem.longValue()) }); 
    if (long_1 == null)
      return resources.getString(R.string.mtrl_picker_range_header_only_end_selected, new Object[] { DateStrings.getDateString(this.selectedEndItem.longValue()) }); 
    Pair<String, String> pair = DateStrings.getDateRangeString(long_1, long_2);
    return resources.getString(R.string.mtrl_picker_range_header_selected, new Object[] { pair.first, pair.second });
  }
  
  public boolean isSelectionComplete() {
    boolean bool;
    Long long_ = this.selectedStartItem;
    if (long_ != null && this.selectedEndItem != null && isValidRange(long_.longValue(), this.selectedEndItem.longValue())) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public View onCreateTextInputView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle, CalendarConstraints paramCalendarConstraints, final OnSelectionChangedListener<Pair<Long, Long>> listener) {
    View view = paramLayoutInflater.inflate(R.layout.mtrl_picker_text_input_date_range, paramViewGroup, false);
    final TextInputLayout startTextInput = (TextInputLayout)view.findViewById(R.id.mtrl_picker_text_input_range_start);
    final TextInputLayout endTextInput = (TextInputLayout)view.findViewById(R.id.mtrl_picker_text_input_range_end);
    EditText editText1 = textInputLayout2.getEditText();
    EditText editText2 = textInputLayout1.getEditText();
    if (ManufacturerUtils.isDateInputKeyboardMissingSeparatorCharacters()) {
      editText1.setInputType(17);
      editText2.setInputType(17);
    } 
    this.invalidRangeStartError = view.getResources().getString(R.string.mtrl_picker_invalid_range);
    SimpleDateFormat simpleDateFormat = UtcDates.getTextInputFormat();
    Long long_ = this.selectedStartItem;
    if (long_ != null) {
      editText1.setText(simpleDateFormat.format(long_));
      this.proposedTextStart = this.selectedStartItem;
    } 
    long_ = this.selectedEndItem;
    if (long_ != null) {
      editText2.setText(simpleDateFormat.format(long_));
      this.proposedTextEnd = this.selectedEndItem;
    } 
    String str = UtcDates.getTextInputHint(view.getResources(), simpleDateFormat);
    textInputLayout2.setPlaceholderText(str);
    textInputLayout1.setPlaceholderText(str);
    editText1.addTextChangedListener((TextWatcher)new DateFormatTextWatcher(str, simpleDateFormat, textInputLayout2, paramCalendarConstraints) {
          final RangeDateSelector this$0;
          
          final TextInputLayout val$endTextInput;
          
          final OnSelectionChangedListener val$listener;
          
          final TextInputLayout val$startTextInput;
          
          void onInvalidDate() {
            RangeDateSelector.access$002(RangeDateSelector.this, null);
            RangeDateSelector.this.updateIfValidTextProposal(startTextInput, endTextInput, listener);
          }
          
          void onValidDate(Long param1Long) {
            RangeDateSelector.access$002(RangeDateSelector.this, param1Long);
            RangeDateSelector.this.updateIfValidTextProposal(startTextInput, endTextInput, listener);
          }
        });
    editText2.addTextChangedListener((TextWatcher)new DateFormatTextWatcher(str, simpleDateFormat, textInputLayout1, paramCalendarConstraints) {
          final RangeDateSelector this$0;
          
          final TextInputLayout val$endTextInput;
          
          final OnSelectionChangedListener val$listener;
          
          final TextInputLayout val$startTextInput;
          
          void onInvalidDate() {
            RangeDateSelector.access$202(RangeDateSelector.this, null);
            RangeDateSelector.this.updateIfValidTextProposal(startTextInput, endTextInput, listener);
          }
          
          void onValidDate(Long param1Long) {
            RangeDateSelector.access$202(RangeDateSelector.this, param1Long);
            RangeDateSelector.this.updateIfValidTextProposal(startTextInput, endTextInput, listener);
          }
        });
    ViewUtils.requestFocusAndShowKeyboard((View)editText1);
    return view;
  }
  
  public void select(long paramLong) {
    Long long_ = this.selectedStartItem;
    if (long_ == null) {
      this.selectedStartItem = Long.valueOf(paramLong);
    } else if (this.selectedEndItem == null && isValidRange(long_.longValue(), paramLong)) {
      this.selectedEndItem = Long.valueOf(paramLong);
    } else {
      this.selectedEndItem = null;
      this.selectedStartItem = Long.valueOf(paramLong);
    } 
  }
  
  public void setSelection(Pair<Long, Long> paramPair) {
    Long long_;
    if (paramPair.first != null && paramPair.second != null)
      Preconditions.checkArgument(isValidRange(((Long)paramPair.first).longValue(), ((Long)paramPair.second).longValue())); 
    Object object = paramPair.first;
    Pair pair = null;
    if (object == null) {
      object = null;
    } else {
      object = Long.valueOf(UtcDates.canonicalYearMonthDay(((Long)paramPair.first).longValue()));
    } 
    this.selectedStartItem = (Long)object;
    if (paramPair.second == null) {
      paramPair = pair;
    } else {
      long_ = Long.valueOf(UtcDates.canonicalYearMonthDay(((Long)paramPair.second).longValue()));
    } 
    this.selectedEndItem = long_;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt) {
    paramParcel.writeValue(this.selectedStartItem);
    paramParcel.writeValue(this.selectedEndItem);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\datepicker\RangeDateSelector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */