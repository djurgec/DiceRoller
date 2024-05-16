package com.google.android.material.datepicker;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Arrays;

public class DateValidatorPointForward implements CalendarConstraints.DateValidator {
  public static final Parcelable.Creator<DateValidatorPointForward> CREATOR = new Parcelable.Creator<DateValidatorPointForward>() {
      public DateValidatorPointForward createFromParcel(Parcel param1Parcel) {
        return new DateValidatorPointForward(param1Parcel.readLong());
      }
      
      public DateValidatorPointForward[] newArray(int param1Int) {
        return new DateValidatorPointForward[param1Int];
      }
    };
  
  private final long point;
  
  private DateValidatorPointForward(long paramLong) {
    this.point = paramLong;
  }
  
  public static DateValidatorPointForward from(long paramLong) {
    return new DateValidatorPointForward(paramLong);
  }
  
  public static DateValidatorPointForward now() {
    return from(UtcDates.getTodayCalendar().getTimeInMillis());
  }
  
  public int describeContents() {
    return 0;
  }
  
  public boolean equals(Object paramObject) {
    boolean bool = true;
    if (this == paramObject)
      return true; 
    if (!(paramObject instanceof DateValidatorPointForward))
      return false; 
    paramObject = paramObject;
    if (this.point != ((DateValidatorPointForward)paramObject).point)
      bool = false; 
    return bool;
  }
  
  public int hashCode() {
    return Arrays.hashCode(new Object[] { Long.valueOf(this.point) });
  }
  
  public boolean isValid(long paramLong) {
    boolean bool;
    if (paramLong >= this.point) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt) {
    paramParcel.writeLong(this.point);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\datepicker\DateValidatorPointForward.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */