package com.google.android.material.datepicker;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.core.util.Pair;
import com.google.android.material.R;
import com.google.android.material.internal.ManufacturerUtils;
import com.google.android.material.internal.ViewUtils;
import com.google.android.material.resources.MaterialAttributes;
import com.google.android.material.textfield.TextInputLayout;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;

public class SingleDateSelector implements DateSelector<Long> {
  public static final Parcelable.Creator<SingleDateSelector> CREATOR = new Parcelable.Creator<SingleDateSelector>() {
      public SingleDateSelector createFromParcel(Parcel param1Parcel) {
        SingleDateSelector singleDateSelector = new SingleDateSelector();
        SingleDateSelector.access$102(singleDateSelector, (Long)param1Parcel.readValue(Long.class.getClassLoader()));
        return singleDateSelector;
      }
      
      public SingleDateSelector[] newArray(int param1Int) {
        return new SingleDateSelector[param1Int];
      }
    };
  
  private Long selectedItem;
  
  private void clearSelection() {
    this.selectedItem = null;
  }
  
  public int describeContents() {
    return 0;
  }
  
  public int getDefaultThemeResId(Context paramContext) {
    return MaterialAttributes.resolveOrThrow(paramContext, R.attr.materialCalendarTheme, MaterialDatePicker.class.getCanonicalName());
  }
  
  public int getDefaultTitleResId() {
    return R.string.mtrl_picker_date_header_title;
  }
  
  public Collection<Long> getSelectedDays() {
    ArrayList<Long> arrayList = new ArrayList();
    Long long_ = this.selectedItem;
    if (long_ != null)
      arrayList.add(long_); 
    return arrayList;
  }
  
  public Collection<Pair<Long, Long>> getSelectedRanges() {
    return new ArrayList<>();
  }
  
  public Long getSelection() {
    return this.selectedItem;
  }
  
  public String getSelectionDisplayString(Context paramContext) {
    Resources resources = paramContext.getResources();
    Long long_ = this.selectedItem;
    if (long_ == null)
      return resources.getString(R.string.mtrl_picker_date_header_unselected); 
    String str = DateStrings.getYearMonthDay(long_.longValue());
    return resources.getString(R.string.mtrl_picker_date_header_selected, new Object[] { str });
  }
  
  public boolean isSelectionComplete() {
    boolean bool;
    if (this.selectedItem != null) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public View onCreateTextInputView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle, CalendarConstraints paramCalendarConstraints, final OnSelectionChangedListener<Long> listener) {
    View view = paramLayoutInflater.inflate(R.layout.mtrl_picker_text_input_date, paramViewGroup, false);
    TextInputLayout textInputLayout = (TextInputLayout)view.findViewById(R.id.mtrl_picker_text_input_date);
    EditText editText = textInputLayout.getEditText();
    if (ManufacturerUtils.isDateInputKeyboardMissingSeparatorCharacters())
      editText.setInputType(17); 
    SimpleDateFormat simpleDateFormat = UtcDates.getTextInputFormat();
    String str = UtcDates.getTextInputHint(view.getResources(), simpleDateFormat);
    textInputLayout.setPlaceholderText(str);
    Long long_ = this.selectedItem;
    if (long_ != null)
      editText.setText(simpleDateFormat.format(long_)); 
    editText.addTextChangedListener((TextWatcher)new DateFormatTextWatcher(str, simpleDateFormat, textInputLayout, paramCalendarConstraints) {
          final SingleDateSelector this$0;
          
          final OnSelectionChangedListener val$listener;
          
          void onInvalidDate() {
            listener.onIncompleteSelectionChanged();
          }
          
          void onValidDate(Long param1Long) {
            if (param1Long == null) {
              SingleDateSelector.this.clearSelection();
            } else {
              SingleDateSelector.this.select(param1Long.longValue());
            } 
            listener.onSelectionChanged(SingleDateSelector.this.getSelection());
          }
        });
    ViewUtils.requestFocusAndShowKeyboard((View)editText);
    return view;
  }
  
  public void select(long paramLong) {
    this.selectedItem = Long.valueOf(paramLong);
  }
  
  public void setSelection(Long paramLong) {
    if (paramLong == null) {
      paramLong = null;
    } else {
      paramLong = Long.valueOf(UtcDates.canonicalYearMonthDay(paramLong.longValue()));
    } 
    this.selectedItem = paramLong;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt) {
    paramParcel.writeValue(this.selectedItem);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\datepicker\SingleDateSelector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */