package com.google.android.material.timepicker;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.LocaleList;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import com.google.android.material.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.internal.TextWatcherAdapter;
import com.google.android.material.textfield.TextInputLayout;
import java.util.Arrays;

class ChipTextInputComboView extends FrameLayout implements Checkable {
  private final Chip chip;
  
  private final EditText editText;
  
  private TextView label;
  
  private final TextInputLayout textInputLayout;
  
  private TextWatcher watcher;
  
  public ChipTextInputComboView(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public ChipTextInputComboView(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public ChipTextInputComboView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    LayoutInflater layoutInflater = LayoutInflater.from(paramContext);
    Chip chip = (Chip)layoutInflater.inflate(R.layout.material_time_chip, (ViewGroup)this, false);
    this.chip = chip;
    TextInputLayout textInputLayout = (TextInputLayout)layoutInflater.inflate(R.layout.material_time_input, (ViewGroup)this, false);
    this.textInputLayout = textInputLayout;
    EditText editText = textInputLayout.getEditText();
    this.editText = editText;
    editText.setVisibility(4);
    TextFormatter textFormatter = new TextFormatter();
    this.watcher = (TextWatcher)textFormatter;
    editText.addTextChangedListener((TextWatcher)textFormatter);
    updateHintLocales();
    addView((View)chip);
    addView((View)textInputLayout);
    this.label = (TextView)findViewById(R.id.material_label);
    editText.setSaveEnabled(false);
  }
  
  private String formatText(CharSequence paramCharSequence) {
    return TimeModel.formatText(getResources(), paramCharSequence);
  }
  
  private void updateHintLocales() {
    if (Build.VERSION.SDK_INT >= 24) {
      LocaleList localeList = getContext().getResources().getConfiguration().getLocales();
      this.editText.setImeHintLocales(localeList);
    } 
  }
  
  public void addInputFilter(InputFilter paramInputFilter) {
    InputFilter[] arrayOfInputFilter2 = this.editText.getFilters();
    InputFilter[] arrayOfInputFilter1 = Arrays.<InputFilter>copyOf(arrayOfInputFilter2, arrayOfInputFilter2.length + 1);
    arrayOfInputFilter1[arrayOfInputFilter2.length] = paramInputFilter;
    this.editText.setFilters(arrayOfInputFilter1);
  }
  
  public TextInputLayout getTextInput() {
    return this.textInputLayout;
  }
  
  public boolean isChecked() {
    return this.chip.isChecked();
  }
  
  protected void onConfigurationChanged(Configuration paramConfiguration) {
    super.onConfigurationChanged(paramConfiguration);
    updateHintLocales();
  }
  
  public void setChecked(boolean paramBoolean) {
    this.chip.setChecked(paramBoolean);
    EditText editText = this.editText;
    boolean bool = false;
    if (paramBoolean) {
      b = 0;
    } else {
      b = 4;
    } 
    editText.setVisibility(b);
    Chip chip = this.chip;
    byte b = bool;
    if (paramBoolean)
      b = 8; 
    chip.setVisibility(b);
    if (isChecked()) {
      this.editText.requestFocus();
      if (!TextUtils.isEmpty((CharSequence)this.editText.getText())) {
        EditText editText1 = this.editText;
        editText1.setSelection(editText1.getText().length());
      } 
    } 
  }
  
  public void setChipDelegate(AccessibilityDelegateCompat paramAccessibilityDelegateCompat) {
    ViewCompat.setAccessibilityDelegate((View)this.chip, paramAccessibilityDelegateCompat);
  }
  
  public void setCursorVisible(boolean paramBoolean) {
    this.editText.setCursorVisible(paramBoolean);
  }
  
  public void setHelperText(CharSequence paramCharSequence) {
    this.label.setText(paramCharSequence);
  }
  
  public void setOnClickListener(View.OnClickListener paramOnClickListener) {
    this.chip.setOnClickListener(paramOnClickListener);
  }
  
  public void setTag(int paramInt, Object paramObject) {
    this.chip.setTag(paramInt, paramObject);
  }
  
  public void setText(CharSequence paramCharSequence) {
    this.chip.setText(formatText(paramCharSequence));
    if (!TextUtils.isEmpty((CharSequence)this.editText.getText())) {
      this.editText.removeTextChangedListener(this.watcher);
      this.editText.setText(null);
      this.editText.addTextChangedListener(this.watcher);
    } 
  }
  
  public void toggle() {
    this.chip.toggle();
  }
  
  private class TextFormatter extends TextWatcherAdapter {
    private static final String DEFAULT_TEXT = "00";
    
    final ChipTextInputComboView this$0;
    
    private TextFormatter() {}
    
    public void afterTextChanged(Editable param1Editable) {
      if (TextUtils.isEmpty((CharSequence)param1Editable)) {
        ChipTextInputComboView.this.chip.setText(ChipTextInputComboView.this.formatText("00"));
        return;
      } 
      ChipTextInputComboView.this.chip.setText(ChipTextInputComboView.this.formatText((CharSequence)param1Editable));
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\timepicker\ChipTextInputComboView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */