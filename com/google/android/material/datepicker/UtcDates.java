package com.google.android.material.datepicker;

import android.content.res.Resources;
import android.icu.text.DateFormat;
import android.icu.util.TimeZone;
import com.google.android.material.R;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicReference;

class UtcDates {
  static final String UTC = "UTC";
  
  static AtomicReference<TimeSource> timeSourceRef = new AtomicReference<>();
  
  static long canonicalYearMonthDay(long paramLong) {
    Calendar calendar = getUtcCalendar();
    calendar.setTimeInMillis(paramLong);
    return getDayCopy(calendar).getTimeInMillis();
  }
  
  private static int findCharactersInDateFormatPattern(String paramString1, String paramString2, int paramInt1, int paramInt2) {
    while (paramInt2 >= 0 && paramInt2 < paramString1.length() && paramString2.indexOf(paramString1.charAt(paramInt2)) == -1) {
      int i = paramInt2;
      if (paramString1.charAt(paramInt2) == '\'') {
        paramInt2 += paramInt1;
        while (true) {
          i = paramInt2;
          if (paramInt2 >= 0) {
            i = paramInt2;
            if (paramInt2 < paramString1.length()) {
              i = paramInt2;
              if (paramString1.charAt(paramInt2) != '\'') {
                paramInt2 += paramInt1;
                continue;
              } 
            } 
          } 
          break;
        } 
      } 
      paramInt2 = i + paramInt1;
    } 
    return paramInt2;
  }
  
  static DateFormat getAbbrMonthDayFormat(Locale paramLocale) {
    return getAndroidFormat("MMMd", paramLocale);
  }
  
  static DateFormat getAbbrMonthWeekdayDayFormat(Locale paramLocale) {
    return getAndroidFormat("MMMEd", paramLocale);
  }
  
  private static DateFormat getAndroidFormat(String paramString, Locale paramLocale) {
    DateFormat dateFormat = DateFormat.getInstanceForSkeleton(paramString, paramLocale);
    dateFormat.setTimeZone(getUtcAndroidTimeZone());
    return dateFormat;
  }
  
  static Calendar getDayCopy(Calendar paramCalendar) {
    Calendar calendar = getUtcCalendarOf(paramCalendar);
    paramCalendar = getUtcCalendar();
    paramCalendar.set(calendar.get(1), calendar.get(2), calendar.get(5));
    return paramCalendar;
  }
  
  private static DateFormat getFormat(int paramInt, Locale paramLocale) {
    DateFormat dateFormat = DateFormat.getDateInstance(paramInt, paramLocale);
    dateFormat.setTimeZone(getTimeZone());
    return dateFormat;
  }
  
  static DateFormat getFullFormat() {
    return getFullFormat(Locale.getDefault());
  }
  
  static DateFormat getFullFormat(Locale paramLocale) {
    return getFormat(0, paramLocale);
  }
  
  static DateFormat getMediumFormat() {
    return getMediumFormat(Locale.getDefault());
  }
  
  static DateFormat getMediumFormat(Locale paramLocale) {
    return getFormat(2, paramLocale);
  }
  
  static DateFormat getMediumNoYear() {
    return getMediumNoYear(Locale.getDefault());
  }
  
  static DateFormat getMediumNoYear(Locale paramLocale) {
    SimpleDateFormat simpleDateFormat = (SimpleDateFormat)getMediumFormat(paramLocale);
    simpleDateFormat.applyPattern(removeYearFromDateFormatPattern(simpleDateFormat.toPattern()));
    return simpleDateFormat;
  }
  
  static SimpleDateFormat getSimpleFormat(String paramString) {
    return getSimpleFormat(paramString, Locale.getDefault());
  }
  
  private static SimpleDateFormat getSimpleFormat(String paramString, Locale paramLocale) {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(paramString, paramLocale);
    simpleDateFormat.setTimeZone(getTimeZone());
    return simpleDateFormat;
  }
  
  static SimpleDateFormat getTextInputFormat() {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(((SimpleDateFormat)DateFormat.getDateInstance(3, Locale.getDefault())).toLocalizedPattern().replaceAll("\\s+", ""), Locale.getDefault());
    simpleDateFormat.setTimeZone(getTimeZone());
    simpleDateFormat.setLenient(false);
    return simpleDateFormat;
  }
  
  static String getTextInputHint(Resources paramResources, SimpleDateFormat paramSimpleDateFormat) {
    String str1 = paramSimpleDateFormat.toLocalizedPattern();
    String str2 = paramResources.getString(R.string.mtrl_picker_text_input_year_abbr);
    String str3 = paramResources.getString(R.string.mtrl_picker_text_input_month_abbr);
    return str1.replaceAll("d", paramResources.getString(R.string.mtrl_picker_text_input_day_abbr)).replaceAll("M", str3).replaceAll("y", str2);
  }
  
  static TimeSource getTimeSource() {
    TimeSource timeSource = timeSourceRef.get();
    if (timeSource == null)
      timeSource = TimeSource.system(); 
    return timeSource;
  }
  
  private static TimeZone getTimeZone() {
    return TimeZone.getTimeZone("UTC");
  }
  
  static Calendar getTodayCalendar() {
    Calendar calendar = getTimeSource().now();
    calendar.set(11, 0);
    calendar.set(12, 0);
    calendar.set(13, 0);
    calendar.set(14, 0);
    calendar.setTimeZone(getTimeZone());
    return calendar;
  }
  
  private static TimeZone getUtcAndroidTimeZone() {
    return TimeZone.getTimeZone("UTC");
  }
  
  static Calendar getUtcCalendar() {
    return getUtcCalendarOf(null);
  }
  
  static Calendar getUtcCalendarOf(Calendar paramCalendar) {
    Calendar calendar = Calendar.getInstance(getTimeZone());
    if (paramCalendar == null) {
      calendar.clear();
    } else {
      calendar.setTimeInMillis(paramCalendar.getTimeInMillis());
    } 
    return calendar;
  }
  
  static DateFormat getYearAbbrMonthDayFormat(Locale paramLocale) {
    return getAndroidFormat("yMMMd", paramLocale);
  }
  
  static DateFormat getYearAbbrMonthWeekdayDayFormat(Locale paramLocale) {
    return getAndroidFormat("yMMMEd", paramLocale);
  }
  
  private static String removeYearFromDateFormatPattern(String paramString) {
    int i = findCharactersInDateFormatPattern(paramString, "yY", 1, 0);
    if (i >= paramString.length())
      return paramString; 
    String str = "EMd";
    int j = findCharactersInDateFormatPattern(paramString, "EMd", 1, i);
    if (j < paramString.length())
      str = "EMd" + ","; 
    return paramString.replace(paramString.substring(findCharactersInDateFormatPattern(paramString, str, -1, i) + 1, j), " ").trim();
  }
  
  static void setTimeSource(TimeSource paramTimeSource) {
    timeSourceRef.set(paramTimeSource);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\datepicker\UtcDates.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */