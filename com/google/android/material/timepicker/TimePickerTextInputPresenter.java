package com.google.android.material.timepicker;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import com.google.android.material.R;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.color.MaterialColors;
import com.google.android.material.internal.TextWatcherAdapter;
import java.lang.reflect.Field;
import java.util.Locale;

class TimePickerTextInputPresenter implements TimePickerView.OnSelectionChange, TimePickerPresenter {
  private final TimePickerTextInputKeyController controller;
  
  private final EditText hourEditText;
  
  private final ChipTextInputComboView hourTextInput;
  
  private final TextWatcher hourTextWatcher = (TextWatcher)new TextWatcherAdapter() {
      final TimePickerTextInputPresenter this$0;
      
      public void afterTextChanged(Editable param1Editable) {
        try {
          if (TextUtils.isEmpty((CharSequence)param1Editable)) {
            TimePickerTextInputPresenter.this.time.setHour(0);
            return;
          } 
          int i = Integer.parseInt(param1Editable.toString());
          TimePickerTextInputPresenter.this.time.setHour(i);
        } catch (NumberFormatException numberFormatException) {}
      }
    };
  
  private final EditText minuteEditText;
  
  private final ChipTextInputComboView minuteTextInput;
  
  private final TextWatcher minuteTextWatcher = (TextWatcher)new TextWatcherAdapter() {
      final TimePickerTextInputPresenter this$0;
      
      public void afterTextChanged(Editable param1Editable) {
        try {
          if (TextUtils.isEmpty((CharSequence)param1Editable)) {
            TimePickerTextInputPresenter.this.time.setMinute(0);
            return;
          } 
          int i = Integer.parseInt(param1Editable.toString());
          TimePickerTextInputPresenter.this.time.setMinute(i);
        } catch (NumberFormatException numberFormatException) {}
      }
    };
  
  private final TimeModel time;
  
  private final LinearLayout timePickerView;
  
  private MaterialButtonToggleGroup toggle;
  
  public TimePickerTextInputPresenter(LinearLayout paramLinearLayout, TimeModel paramTimeModel) {
    this.timePickerView = paramLinearLayout;
    this.time = paramTimeModel;
    Resources resources = paramLinearLayout.getResources();
    ChipTextInputComboView chipTextInputComboView2 = (ChipTextInputComboView)paramLinearLayout.findViewById(R.id.material_minute_text_input);
    this.minuteTextInput = chipTextInputComboView2;
    ChipTextInputComboView chipTextInputComboView1 = (ChipTextInputComboView)paramLinearLayout.findViewById(R.id.material_hour_text_input);
    this.hourTextInput = chipTextInputComboView1;
    TextView textView2 = (TextView)chipTextInputComboView2.findViewById(R.id.material_label);
    TextView textView1 = (TextView)chipTextInputComboView1.findViewById(R.id.material_label);
    textView2.setText(resources.getString(R.string.material_timepicker_minute));
    textView1.setText(resources.getString(R.string.material_timepicker_hour));
    chipTextInputComboView2.setTag(R.id.selection_type, Integer.valueOf(12));
    chipTextInputComboView1.setTag(R.id.selection_type, Integer.valueOf(10));
    if (paramTimeModel.format == 0)
      setupPeriodToggle(); 
    View.OnClickListener onClickListener = new View.OnClickListener() {
        final TimePickerTextInputPresenter this$0;
        
        public void onClick(View param1View) {
          TimePickerTextInputPresenter.this.onSelectionChanged(((Integer)param1View.getTag(R.id.selection_type)).intValue());
        }
      };
    chipTextInputComboView1.setOnClickListener(onClickListener);
    chipTextInputComboView2.setOnClickListener(onClickListener);
    chipTextInputComboView1.addInputFilter(paramTimeModel.getHourInputValidator());
    chipTextInputComboView2.addInputFilter(paramTimeModel.getMinuteInputValidator());
    EditText editText1 = chipTextInputComboView1.getTextInput().getEditText();
    this.hourEditText = editText1;
    EditText editText2 = chipTextInputComboView2.getTextInput().getEditText();
    this.minuteEditText = editText2;
    if (Build.VERSION.SDK_INT < 21) {
      int i = MaterialColors.getColor((View)paramLinearLayout, R.attr.colorPrimary);
      setCursorDrawableColor(editText1, i);
      setCursorDrawableColor(editText2, i);
    } 
    this.controller = new TimePickerTextInputKeyController(chipTextInputComboView1, chipTextInputComboView2, paramTimeModel);
    chipTextInputComboView1.setChipDelegate(new ClickActionDelegate(paramLinearLayout.getContext(), R.string.material_hour_selection));
    chipTextInputComboView2.setChipDelegate(new ClickActionDelegate(paramLinearLayout.getContext(), R.string.material_minute_selection));
    initialize();
  }
  
  private void addTextWatchers() {
    this.hourEditText.addTextChangedListener(this.hourTextWatcher);
    this.minuteEditText.addTextChangedListener(this.minuteTextWatcher);
  }
  
  private void removeTextWatchers() {
    this.hourEditText.removeTextChangedListener(this.hourTextWatcher);
    this.minuteEditText.removeTextChangedListener(this.minuteTextWatcher);
  }
  
  private static void setCursorDrawableColor(EditText paramEditText, int paramInt) {
    try {
      Context context = paramEditText.getContext();
      Field field = TextView.class.getDeclaredField("mCursorDrawableRes");
      field.setAccessible(true);
      int i = field.getInt(paramEditText);
      field = TextView.class.getDeclaredField("mEditor");
      field.setAccessible(true);
      Object object = field.get(paramEditText);
      field = object.getClass().getDeclaredField("mCursorDrawable");
      field.setAccessible(true);
      Drawable drawable = AppCompatResources.getDrawable(context, i);
      drawable.setColorFilter(paramInt, PorterDuff.Mode.SRC_IN);
      field.set(object, new Drawable[] { drawable, drawable });
    } finally {}
  }
  
  private void setTime(TimeModel paramTimeModel) {
    removeTextWatchers();
    Locale locale = (this.timePickerView.getResources().getConfiguration()).locale;
    String str2 = String.format(locale, "%02d", new Object[] { Integer.valueOf(paramTimeModel.minute) });
    String str1 = String.format(locale, "%02d", new Object[] { Integer.valueOf(paramTimeModel.getHourForDisplay()) });
    this.minuteTextInput.setText(str2);
    this.hourTextInput.setText(str1);
    addTextWatchers();
    updateSelection();
  }
  
  private void setupPeriodToggle() {
    MaterialButtonToggleGroup materialButtonToggleGroup = (MaterialButtonToggleGroup)this.timePickerView.findViewById(R.id.material_clock_period_toggle);
    this.toggle = materialButtonToggleGroup;
    materialButtonToggleGroup.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
          final TimePickerTextInputPresenter this$0;
          
          public void onButtonChecked(MaterialButtonToggleGroup param1MaterialButtonToggleGroup, int param1Int, boolean param1Boolean) {
            if (param1Int == R.id.material_clock_period_pm_button) {
              param1Int = 1;
            } else {
              param1Int = 0;
            } 
            TimePickerTextInputPresenter.this.time.setPeriod(param1Int);
          }
        });
    this.toggle.setVisibility(0);
    updateSelection();
  }
  
  private void updateSelection() {
    int i;
    MaterialButtonToggleGroup materialButtonToggleGroup = this.toggle;
    if (materialButtonToggleGroup == null)
      return; 
    if (this.time.period == 0) {
      i = R.id.material_clock_period_am_button;
    } else {
      i = R.id.material_clock_period_pm_button;
    } 
    materialButtonToggleGroup.check(i);
  }
  
  public void clearCheck() {
    this.minuteTextInput.setChecked(false);
    this.hourTextInput.setChecked(false);
  }
  
  public void hide() {
    View view = this.timePickerView.getFocusedChild();
    if (view == null) {
      this.timePickerView.setVisibility(8);
      return;
    } 
    InputMethodManager inputMethodManager = (InputMethodManager)ContextCompat.getSystemService(this.timePickerView.getContext(), InputMethodManager.class);
    if (inputMethodManager != null)
      inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0); 
    this.timePickerView.setVisibility(8);
  }
  
  public void initialize() {
    addTextWatchers();
    setTime(this.time);
    this.controller.bind();
  }
  
  public void invalidate() {
    setTime(this.time);
  }
  
  public void onSelectionChanged(int paramInt) {
    boolean bool1;
    this.time.selection = paramInt;
    ChipTextInputComboView chipTextInputComboView = this.minuteTextInput;
    boolean bool2 = true;
    if (paramInt == 12) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    chipTextInputComboView.setChecked(bool1);
    chipTextInputComboView = this.hourTextInput;
    if (paramInt == 10) {
      bool1 = bool2;
    } else {
      bool1 = false;
    } 
    chipTextInputComboView.setChecked(bool1);
    updateSelection();
  }
  
  public void resetChecked() {
    boolean bool1;
    ChipTextInputComboView chipTextInputComboView = this.minuteTextInput;
    int i = this.time.selection;
    boolean bool2 = true;
    if (i == 12) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    chipTextInputComboView.setChecked(bool1);
    chipTextInputComboView = this.hourTextInput;
    if (this.time.selection == 10) {
      bool1 = bool2;
    } else {
      bool1 = false;
    } 
    chipTextInputComboView.setChecked(bool1);
  }
  
  public void show() {
    this.timePickerView.setVisibility(0);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\timepicker\TimePickerTextInputPresenter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */