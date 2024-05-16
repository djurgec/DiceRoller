package com.google.android.material.timepicker;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

interface TimePickerControls {
  void setActiveSelection(int paramInt);
  
  void setHandRotation(float paramFloat);
  
  void setValues(String[] paramArrayOfString, int paramInt);
  
  void updateTime(int paramInt1, int paramInt2, int paramInt3);
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface ActiveSelection {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface ClockPeriod {}
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\timepicker\TimePickerControls.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */