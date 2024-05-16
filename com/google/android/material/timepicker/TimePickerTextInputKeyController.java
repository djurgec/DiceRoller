package com.google.android.material.timepicker;

import android.text.Editable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.google.android.material.textfield.TextInputLayout;

class TimePickerTextInputKeyController implements TextView.OnEditorActionListener, View.OnKeyListener {
  private final ChipTextInputComboView hourLayoutComboView;
  
  private boolean keyListenerRunning = false;
  
  private final ChipTextInputComboView minuteLayoutComboView;
  
  private final TimeModel time;
  
  TimePickerTextInputKeyController(ChipTextInputComboView paramChipTextInputComboView1, ChipTextInputComboView paramChipTextInputComboView2, TimeModel paramTimeModel) {
    this.hourLayoutComboView = paramChipTextInputComboView1;
    this.minuteLayoutComboView = paramChipTextInputComboView2;
    this.time = paramTimeModel;
  }
  
  private void moveSelection(int paramInt) {
    boolean bool1;
    ChipTextInputComboView chipTextInputComboView = this.minuteLayoutComboView;
    boolean bool2 = true;
    if (paramInt == 12) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    chipTextInputComboView.setChecked(bool1);
    chipTextInputComboView = this.hourLayoutComboView;
    if (paramInt == 10) {
      bool1 = bool2;
    } else {
      bool1 = false;
    } 
    chipTextInputComboView.setChecked(bool1);
    this.time.selection = paramInt;
  }
  
  private boolean onHourKeyPress(int paramInt, KeyEvent paramKeyEvent, EditText paramEditText) {
    Editable editable = paramEditText.getText();
    if (editable == null)
      return false; 
    if (paramInt >= 7 && paramInt <= 16 && paramKeyEvent.getAction() == 1 && paramEditText.getSelectionStart() == 2 && editable.length() == 2) {
      paramInt = 1;
    } else {
      paramInt = 0;
    } 
    if (paramInt != 0) {
      moveSelection(12);
      return true;
    } 
    return false;
  }
  
  private boolean onMinuteKeyPress(int paramInt, KeyEvent paramKeyEvent, EditText paramEditText) {
    if (paramInt == 67 && paramKeyEvent.getAction() == 0 && TextUtils.isEmpty((CharSequence)paramEditText.getText())) {
      paramInt = 1;
    } else {
      paramInt = 0;
    } 
    if (paramInt != 0) {
      moveSelection(10);
      return true;
    } 
    return false;
  }
  
  public void bind() {
    TextInputLayout textInputLayout2 = this.hourLayoutComboView.getTextInput();
    TextInputLayout textInputLayout1 = this.minuteLayoutComboView.getTextInput();
    EditText editText2 = textInputLayout2.getEditText();
    EditText editText1 = textInputLayout1.getEditText();
    editText2.setImeOptions(268435461);
    editText1.setImeOptions(268435462);
    editText2.setOnEditorActionListener(this);
    editText2.setOnKeyListener(this);
    editText1.setOnKeyListener(this);
  }
  
  public boolean onEditorAction(TextView paramTextView, int paramInt, KeyEvent paramKeyEvent) {
    boolean bool;
    if (paramInt == 5) {
      bool = true;
    } else {
      bool = false;
    } 
    if (bool)
      moveSelection(12); 
    return bool;
  }
  
  public boolean onKey(View paramView, int paramInt, KeyEvent paramKeyEvent) {
    boolean bool;
    if (this.keyListenerRunning)
      return false; 
    this.keyListenerRunning = true;
    EditText editText = (EditText)paramView;
    if (this.time.selection == 12) {
      bool = onMinuteKeyPress(paramInt, paramKeyEvent, editText);
    } else {
      bool = onHourKeyPress(paramInt, paramKeyEvent, editText);
    } 
    this.keyListenerRunning = false;
    return bool;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\timepicker\TimePickerTextInputKeyController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */