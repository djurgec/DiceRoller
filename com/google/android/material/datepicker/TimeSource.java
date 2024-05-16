package com.google.android.material.datepicker;

import java.util.Calendar;
import java.util.TimeZone;

class TimeSource {
  private static final TimeSource SYSTEM_TIME_SOURCE = new TimeSource(null, null);
  
  private final Long fixedTimeMs;
  
  private final TimeZone fixedTimeZone;
  
  private TimeSource(Long paramLong, TimeZone paramTimeZone) {
    this.fixedTimeMs = paramLong;
    this.fixedTimeZone = paramTimeZone;
  }
  
  static TimeSource fixed(long paramLong) {
    return new TimeSource(Long.valueOf(paramLong), null);
  }
  
  static TimeSource fixed(long paramLong, TimeZone paramTimeZone) {
    return new TimeSource(Long.valueOf(paramLong), paramTimeZone);
  }
  
  static TimeSource system() {
    return SYSTEM_TIME_SOURCE;
  }
  
  Calendar now() {
    return now(this.fixedTimeZone);
  }
  
  Calendar now(TimeZone paramTimeZone) {
    Calendar calendar;
    if (paramTimeZone == null) {
      calendar = Calendar.getInstance();
    } else {
      calendar = Calendar.getInstance((TimeZone)calendar);
    } 
    Long long_ = this.fixedTimeMs;
    if (long_ != null)
      calendar.setTimeInMillis(long_.longValue()); 
    return calendar;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\datepicker\TimeSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */