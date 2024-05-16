package com.google.android.material.timepicker;

import android.os.Build;
import android.view.accessibility.AccessibilityManager;
import androidx.core.content.ContextCompat;
import com.google.android.material.R;

class TimePickerClockPresenter implements ClockHandView.OnRotateListener, TimePickerView.OnSelectionChange, TimePickerView.OnPeriodChangeListener, ClockHandView.OnActionUpListener, TimePickerPresenter {
  private static final int DEGREES_PER_HOUR = 30;
  
  private static final int DEGREES_PER_MINUTE = 6;
  
  private static final String[] HOUR_CLOCK_24_VALUES;
  
  private static final String[] HOUR_CLOCK_VALUES = new String[] { 
      "12", "1", "2", "3", "4", "5", "6", "7", "8", "9", 
      "10", "11" };
  
  private static final String[] MINUTE_CLOCK_VALUES;
  
  private boolean broadcasting = false;
  
  private float hourRotation;
  
  private float minuteRotation;
  
  private TimeModel time;
  
  private TimePickerView timePickerView;
  
  static {
    HOUR_CLOCK_24_VALUES = new String[] { 
        "00", "2", "4", "6", "8", "10", "12", "14", "16", "18", 
        "20", "22" };
    MINUTE_CLOCK_VALUES = new String[] { 
        "00", "5", "10", "15", "20", "25", "30", "35", "40", "45", 
        "50", "55" };
  }
  
  public TimePickerClockPresenter(TimePickerView paramTimePickerView, TimeModel paramTimeModel) {
    this.timePickerView = paramTimePickerView;
    this.time = paramTimeModel;
    initialize();
  }
  
  private int getDegreesPerHour() {
    byte b;
    if (this.time.format == 1) {
      b = 15;
    } else {
      b = 30;
    } 
    return b;
  }
  
  private String[] getHourClockValues() {
    String[] arrayOfString;
    if (this.time.format == 1) {
      arrayOfString = HOUR_CLOCK_24_VALUES;
    } else {
      arrayOfString = HOUR_CLOCK_VALUES;
    } 
    return arrayOfString;
  }
  
  private void performHapticFeedback(int paramInt1, int paramInt2) {
    if (this.time.minute != paramInt2 || this.time.hour != paramInt1) {
      if (Build.VERSION.SDK_INT >= 21) {
        paramInt1 = 4;
      } else {
        paramInt1 = 1;
      } 
      this.timePickerView.performHapticFeedback(paramInt1);
    } 
  }
  
  private void updateTime() {
    this.timePickerView.updateTime(this.time.period, this.time.getHourForDisplay(), this.time.minute);
  }
  
  private void updateValues() {
    updateValues(HOUR_CLOCK_VALUES, "%d");
    updateValues(HOUR_CLOCK_24_VALUES, "%d");
    updateValues(MINUTE_CLOCK_VALUES, "%02d");
  }
  
  private void updateValues(String[] paramArrayOfString, String paramString) {
    for (byte b = 0; b < paramArrayOfString.length; b++)
      paramArrayOfString[b] = TimeModel.formatText(this.timePickerView.getResources(), paramArrayOfString[b], paramString); 
  }
  
  public void hide() {
    this.timePickerView.setVisibility(8);
  }
  
  public void initialize() {
    if (this.time.format == 0)
      this.timePickerView.showToggle(); 
    this.timePickerView.addOnRotateListener(this);
    this.timePickerView.setOnSelectionChangeListener(this);
    this.timePickerView.setOnPeriodChangeListener(this);
    this.timePickerView.setOnActionUpListener(this);
    updateValues();
    invalidate();
  }
  
  public void invalidate() {
    int i = this.time.getHourForDisplay();
    this.hourRotation = (getDegreesPerHour() * i);
    this.minuteRotation = (this.time.minute * 6);
    setSelection(this.time.selection, false);
    updateTime();
  }
  
  public void onActionUp(float paramFloat, boolean paramBoolean) {
    this.broadcasting = true;
    int j = this.time.minute;
    int i = this.time.hour;
    if (this.time.selection == 10) {
      this.timePickerView.setHandRotation(this.hourRotation, false);
      if (!((AccessibilityManager)ContextCompat.getSystemService(this.timePickerView.getContext(), AccessibilityManager.class)).isTouchExplorationEnabled())
        setSelection(12, true); 
    } else {
      int k = Math.round(paramFloat);
      if (!paramBoolean) {
        k = (k + 15) / 30;
        this.time.setMinute(k * 5);
        this.minuteRotation = (this.time.minute * 6);
      } 
      this.timePickerView.setHandRotation(this.minuteRotation, paramBoolean);
    } 
    this.broadcasting = false;
    updateTime();
    performHapticFeedback(i, j);
  }
  
  public void onPeriodChange(int paramInt) {
    this.time.setPeriod(paramInt);
  }
  
  public void onRotate(float paramFloat, boolean paramBoolean) {
    if (this.broadcasting)
      return; 
    int i = this.time.hour;
    int k = this.time.minute;
    int j = Math.round(paramFloat);
    if (this.time.selection == 12) {
      this.time.setMinute((j + 3) / 6);
      this.minuteRotation = (float)Math.floor((this.time.minute * 6));
    } else {
      int m = getDegreesPerHour() / 2;
      this.time.setHour((j + m) / getDegreesPerHour());
      this.hourRotation = (this.time.getHourForDisplay() * getDegreesPerHour());
    } 
    if (!paramBoolean) {
      updateTime();
      performHapticFeedback(i, k);
    } 
  }
  
  public void onSelectionChanged(int paramInt) {
    setSelection(paramInt, true);
  }
  
  void setSelection(int paramInt, boolean paramBoolean) {
    float f;
    int i;
    boolean bool;
    String[] arrayOfString;
    if (paramInt == 12) {
      bool = true;
    } else {
      bool = false;
    } 
    this.timePickerView.setAnimateOnTouchUp(bool);
    this.time.selection = paramInt;
    TimePickerView timePickerView2 = this.timePickerView;
    if (bool) {
      arrayOfString = MINUTE_CLOCK_VALUES;
    } else {
      arrayOfString = getHourClockValues();
    } 
    if (bool) {
      i = R.string.material_minute_suffix;
    } else {
      i = R.string.material_hour_suffix;
    } 
    timePickerView2.setValues(arrayOfString, i);
    TimePickerView timePickerView1 = this.timePickerView;
    if (bool) {
      f = this.minuteRotation;
    } else {
      f = this.hourRotation;
    } 
    timePickerView1.setHandRotation(f, paramBoolean);
    this.timePickerView.setActiveSelection(paramInt);
    this.timePickerView.setMinuteHourDelegate(new ClickActionDelegate(this.timePickerView.getContext(), R.string.material_hour_selection));
    this.timePickerView.setHourClickDelegate(new ClickActionDelegate(this.timePickerView.getContext(), R.string.material_minute_selection));
  }
  
  public void show() {
    this.timePickerView.setVisibility(0);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\timepicker\TimePickerClockPresenter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */