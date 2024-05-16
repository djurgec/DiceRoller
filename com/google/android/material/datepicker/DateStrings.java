package com.google.android.material.datepicker;

import android.content.Context;
import android.os.Build;
import android.text.format.DateUtils;
import androidx.core.util.Pair;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

class DateStrings {
  static Pair<String, String> getDateRangeString(Long paramLong1, Long paramLong2) {
    return getDateRangeString(paramLong1, paramLong2, null);
  }
  
  static Pair<String, String> getDateRangeString(Long paramLong1, Long paramLong2, SimpleDateFormat paramSimpleDateFormat) {
    Date date1;
    Date date2;
    if (paramLong1 == null && paramLong2 == null)
      return Pair.create(null, null); 
    if (paramLong1 == null)
      return Pair.create(null, getDateString(paramLong2.longValue(), paramSimpleDateFormat)); 
    if (paramLong2 == null)
      return Pair.create(getDateString(paramLong1.longValue(), paramSimpleDateFormat), null); 
    Calendar calendar1 = UtcDates.getTodayCalendar();
    Calendar calendar2 = UtcDates.getUtcCalendar();
    calendar2.setTimeInMillis(paramLong1.longValue());
    Calendar calendar3 = UtcDates.getUtcCalendar();
    calendar3.setTimeInMillis(paramLong2.longValue());
    if (paramSimpleDateFormat != null) {
      date1 = new Date(paramLong1.longValue());
      date2 = new Date(paramLong2.longValue());
      return Pair.create(paramSimpleDateFormat.format(date1), paramSimpleDateFormat.format(date2));
    } 
    return (calendar2.get(1) == calendar3.get(1)) ? ((calendar2.get(1) == calendar1.get(1)) ? Pair.create(getMonthDay(date1.longValue(), Locale.getDefault()), getMonthDay(date2.longValue(), Locale.getDefault())) : Pair.create(getMonthDay(date1.longValue(), Locale.getDefault()), getYearMonthDay(date2.longValue(), Locale.getDefault()))) : Pair.create(getYearMonthDay(date1.longValue(), Locale.getDefault()), getYearMonthDay(date2.longValue(), Locale.getDefault()));
  }
  
  static String getDateString(long paramLong) {
    return getDateString(paramLong, null);
  }
  
  static String getDateString(long paramLong, SimpleDateFormat paramSimpleDateFormat) {
    Calendar calendar1 = UtcDates.getTodayCalendar();
    Calendar calendar2 = UtcDates.getUtcCalendar();
    calendar2.setTimeInMillis(paramLong);
    return (paramSimpleDateFormat != null) ? paramSimpleDateFormat.format(new Date(paramLong)) : ((calendar1.get(1) == calendar2.get(1)) ? getMonthDay(paramLong) : getYearMonthDay(paramLong));
  }
  
  static String getMonthDay(long paramLong) {
    return getMonthDay(paramLong, Locale.getDefault());
  }
  
  static String getMonthDay(long paramLong, Locale paramLocale) {
    return (Build.VERSION.SDK_INT >= 24) ? UtcDates.getAbbrMonthDayFormat(paramLocale).format(new Date(paramLong)) : UtcDates.getMediumNoYear(paramLocale).format(new Date(paramLong));
  }
  
  static String getMonthDayOfWeekDay(long paramLong) {
    return getMonthDayOfWeekDay(paramLong, Locale.getDefault());
  }
  
  static String getMonthDayOfWeekDay(long paramLong, Locale paramLocale) {
    return (Build.VERSION.SDK_INT >= 24) ? UtcDates.getAbbrMonthWeekdayDayFormat(paramLocale).format(new Date(paramLong)) : UtcDates.getFullFormat(paramLocale).format(new Date(paramLong));
  }
  
  static String getYearMonth(Context paramContext, long paramLong) {
    return DateUtils.formatDateTime(paramContext, paramLong - TimeZone.getDefault().getOffset(paramLong), 36);
  }
  
  static String getYearMonthDay(long paramLong) {
    return getYearMonthDay(paramLong, Locale.getDefault());
  }
  
  static String getYearMonthDay(long paramLong, Locale paramLocale) {
    return (Build.VERSION.SDK_INT >= 24) ? UtcDates.getYearAbbrMonthDayFormat(paramLocale).format(new Date(paramLong)) : UtcDates.getMediumFormat(paramLocale).format(new Date(paramLong));
  }
  
  static String getYearMonthDayOfWeekDay(long paramLong) {
    return getYearMonthDayOfWeekDay(paramLong, Locale.getDefault());
  }
  
  static String getYearMonthDayOfWeekDay(long paramLong, Locale paramLocale) {
    return (Build.VERSION.SDK_INT >= 24) ? UtcDates.getYearAbbrMonthWeekdayDayFormat(paramLocale).format(new Date(paramLong)) : UtcDates.getFullFormat(paramLocale).format(new Date(paramLong));
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\datepicker\DateStrings.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */