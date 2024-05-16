package com.google.android.material.datepicker;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.Arrays;
import java.util.Calendar;

final class Month implements Comparable<Month>, Parcelable {
  public static final Parcelable.Creator<Month> CREATOR = new Parcelable.Creator<Month>() {
      public Month createFromParcel(Parcel param1Parcel) {
        return Month.create(param1Parcel.readInt(), param1Parcel.readInt());
      }
      
      public Month[] newArray(int param1Int) {
        return new Month[param1Int];
      }
    };
  
  final int daysInMonth;
  
  final int daysInWeek;
  
  private final Calendar firstOfMonth;
  
  private String longName;
  
  final int month;
  
  final long timeInMillis;
  
  final int year;
  
  private Month(Calendar paramCalendar) {
    paramCalendar.set(5, 1);
    paramCalendar = UtcDates.getDayCopy(paramCalendar);
    this.firstOfMonth = paramCalendar;
    this.month = paramCalendar.get(2);
    this.year = paramCalendar.get(1);
    this.daysInWeek = paramCalendar.getMaximum(7);
    this.daysInMonth = paramCalendar.getActualMaximum(5);
    this.timeInMillis = paramCalendar.getTimeInMillis();
  }
  
  static Month create(int paramInt1, int paramInt2) {
    Calendar calendar = UtcDates.getUtcCalendar();
    calendar.set(1, paramInt1);
    calendar.set(2, paramInt2);
    return new Month(calendar);
  }
  
  static Month create(long paramLong) {
    Calendar calendar = UtcDates.getUtcCalendar();
    calendar.setTimeInMillis(paramLong);
    return new Month(calendar);
  }
  
  static Month current() {
    return new Month(UtcDates.getTodayCalendar());
  }
  
  public int compareTo(Month paramMonth) {
    return this.firstOfMonth.compareTo(paramMonth.firstOfMonth);
  }
  
  int daysFromStartOfWeekToFirstOfMonth() {
    int j = this.firstOfMonth.get(7) - this.firstOfMonth.getFirstDayOfWeek();
    int i = j;
    if (j < 0)
      i = j + this.daysInWeek; 
    return i;
  }
  
  public int describeContents() {
    return 0;
  }
  
  public boolean equals(Object paramObject) {
    boolean bool = true;
    if (this == paramObject)
      return true; 
    if (!(paramObject instanceof Month))
      return false; 
    paramObject = paramObject;
    if (this.month != ((Month)paramObject).month || this.year != ((Month)paramObject).year)
      bool = false; 
    return bool;
  }
  
  long getDay(int paramInt) {
    Calendar calendar = UtcDates.getDayCopy(this.firstOfMonth);
    calendar.set(5, paramInt);
    return calendar.getTimeInMillis();
  }
  
  int getDayOfMonth(long paramLong) {
    Calendar calendar = UtcDates.getDayCopy(this.firstOfMonth);
    calendar.setTimeInMillis(paramLong);
    return calendar.get(5);
  }
  
  String getLongName(Context paramContext) {
    if (this.longName == null)
      this.longName = DateStrings.getYearMonth(paramContext, this.firstOfMonth.getTimeInMillis()); 
    return this.longName;
  }
  
  long getStableId() {
    return this.firstOfMonth.getTimeInMillis();
  }
  
  public int hashCode() {
    return Arrays.hashCode(new Object[] { Integer.valueOf(this.month), Integer.valueOf(this.year) });
  }
  
  Month monthsLater(int paramInt) {
    Calendar calendar = UtcDates.getDayCopy(this.firstOfMonth);
    calendar.add(2, paramInt);
    return new Month(calendar);
  }
  
  int monthsUntil(Month paramMonth) {
    if (this.firstOfMonth instanceof java.util.GregorianCalendar)
      return (paramMonth.year - this.year) * 12 + paramMonth.month - this.month; 
    throw new IllegalArgumentException("Only Gregorian calendars are supported.");
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt) {
    paramParcel.writeInt(this.year);
    paramParcel.writeInt(this.month);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\datepicker\Month.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */