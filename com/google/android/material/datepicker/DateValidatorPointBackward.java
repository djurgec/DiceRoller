package com.google.android.material.datepicker;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Arrays;

public class DateValidatorPointBackward implements CalendarConstraints.DateValidator {
  public static final Parcelable.Creator<DateValidatorPointBackward> CREATOR = new Parcelable.Creator<DateValidatorPointBackward>() {
      public DateValidatorPointBackward createFromParcel(Parcel param1Parcel) {
        return new DateValidatorPointBackward(param1Parcel.readLong());
      }
      
      public DateValidatorPointBackward[] newArray(int param1Int) {
        return new DateValidatorPointBackward[param1Int];
      }
    };
  
  private final long point;
  
  private DateValidatorPointBackward(long paramLong) {
    this.point = paramLong;
  }
  
  public static DateValidatorPointBackward before(long paramLong) {
    return new DateValidatorPointBackward(paramLong);
  }
  
  public static DateValidatorPointBackward now() {
    return before(UtcDates.getTodayCalendar().getTimeInMillis());
  }
  
  public int describeContents() {
    return 0;
  }
  
  public boolean equals(Object paramObject) {
    boolean bool = true;
    if (this == paramObject)
      return true; 
    if (!(paramObject instanceof DateValidatorPointBackward))
      return false; 
    paramObject = paramObject;
    if (this.point != ((DateValidatorPointBackward)paramObject).point)
      bool = false; 
    return bool;
  }
  
  public int hashCode() {
    return Arrays.hashCode(new Object[] { Long.valueOf(this.point) });
  }
  
  public boolean isValid(long paramLong) {
    boolean bool;
    if (paramLong <= this.point) {
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


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\datepicker\DateValidatorPointBackward.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */