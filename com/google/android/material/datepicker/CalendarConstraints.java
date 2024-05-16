package com.google.android.material.datepicker;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.core.util.ObjectsCompat;
import java.util.Arrays;

public final class CalendarConstraints implements Parcelable {
  public static final Parcelable.Creator<CalendarConstraints> CREATOR = new Parcelable.Creator<CalendarConstraints>() {
      public CalendarConstraints createFromParcel(Parcel param1Parcel) {
        Month month2 = (Month)param1Parcel.readParcelable(Month.class.getClassLoader());
        Month month1 = (Month)param1Parcel.readParcelable(Month.class.getClassLoader());
        Month month3 = (Month)param1Parcel.readParcelable(Month.class.getClassLoader());
        return new CalendarConstraints(month2, month1, (CalendarConstraints.DateValidator)param1Parcel.readParcelable(CalendarConstraints.DateValidator.class.getClassLoader()), month3);
      }
      
      public CalendarConstraints[] newArray(int param1Int) {
        return new CalendarConstraints[param1Int];
      }
    };
  
  private final Month end;
  
  private final int monthSpan;
  
  private Month openAt;
  
  private final Month start;
  
  private final DateValidator validator;
  
  private final int yearSpan;
  
  private CalendarConstraints(Month paramMonth1, Month paramMonth2, DateValidator paramDateValidator, Month paramMonth3) {
    this.start = paramMonth1;
    this.end = paramMonth2;
    this.openAt = paramMonth3;
    this.validator = paramDateValidator;
    if (paramMonth3 == null || paramMonth1.compareTo(paramMonth3) <= 0) {
      if (paramMonth3 == null || paramMonth3.compareTo(paramMonth2) <= 0) {
        this.monthSpan = paramMonth1.monthsUntil(paramMonth2) + 1;
        this.yearSpan = paramMonth2.year - paramMonth1.year + 1;
        return;
      } 
      throw new IllegalArgumentException("current Month cannot be after end Month");
    } 
    throw new IllegalArgumentException("start Month cannot be after current Month");
  }
  
  Month clamp(Month paramMonth) {
    return (paramMonth.compareTo(this.start) < 0) ? this.start : ((paramMonth.compareTo(this.end) > 0) ? this.end : paramMonth);
  }
  
  public int describeContents() {
    return 0;
  }
  
  public boolean equals(Object paramObject) {
    boolean bool = true;
    if (this == paramObject)
      return true; 
    if (!(paramObject instanceof CalendarConstraints))
      return false; 
    paramObject = paramObject;
    if (!this.start.equals(((CalendarConstraints)paramObject).start) || !this.end.equals(((CalendarConstraints)paramObject).end) || !ObjectsCompat.equals(this.openAt, ((CalendarConstraints)paramObject).openAt) || !this.validator.equals(((CalendarConstraints)paramObject).validator))
      bool = false; 
    return bool;
  }
  
  public DateValidator getDateValidator() {
    return this.validator;
  }
  
  Month getEnd() {
    return this.end;
  }
  
  int getMonthSpan() {
    return this.monthSpan;
  }
  
  Month getOpenAt() {
    return this.openAt;
  }
  
  Month getStart() {
    return this.start;
  }
  
  int getYearSpan() {
    return this.yearSpan;
  }
  
  public int hashCode() {
    return Arrays.hashCode(new Object[] { this.start, this.end, this.openAt, this.validator });
  }
  
  boolean isWithinBounds(long paramLong) {
    Month month = this.start;
    null = true;
    if (month.getDay(1) <= paramLong) {
      month = this.end;
      if (paramLong <= month.getDay(month.daysInMonth))
        return null; 
    } 
    return false;
  }
  
  void setOpenAt(Month paramMonth) {
    this.openAt = paramMonth;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt) {
    paramParcel.writeParcelable(this.start, 0);
    paramParcel.writeParcelable(this.end, 0);
    paramParcel.writeParcelable(this.openAt, 0);
    paramParcel.writeParcelable(this.validator, 0);
  }
  
  public static final class Builder {
    private static final String DEEP_COPY_VALIDATOR_KEY = "DEEP_COPY_VALIDATOR_KEY";
    
    static final long DEFAULT_END = UtcDates.canonicalYearMonthDay((Month.create(2100, 11)).timeInMillis);
    
    static final long DEFAULT_START = UtcDates.canonicalYearMonthDay((Month.create(1900, 0)).timeInMillis);
    
    private long end = DEFAULT_END;
    
    private Long openAt;
    
    private long start = DEFAULT_START;
    
    private CalendarConstraints.DateValidator validator = DateValidatorPointForward.from(Long.MIN_VALUE);
    
    static {
    
    }
    
    public Builder() {}
    
    Builder(CalendarConstraints param1CalendarConstraints) {
      this.start = param1CalendarConstraints.start.timeInMillis;
      this.end = param1CalendarConstraints.end.timeInMillis;
      this.openAt = Long.valueOf(param1CalendarConstraints.openAt.timeInMillis);
      this.validator = param1CalendarConstraints.validator;
    }
    
    public CalendarConstraints build() {
      Month month1;
      Bundle bundle = new Bundle();
      bundle.putParcelable("DEEP_COPY_VALIDATOR_KEY", this.validator);
      Month month2 = Month.create(this.start);
      Month month3 = Month.create(this.end);
      CalendarConstraints.DateValidator dateValidator = (CalendarConstraints.DateValidator)bundle.getParcelable("DEEP_COPY_VALIDATOR_KEY");
      Long long_ = this.openAt;
      if (long_ == null) {
        long_ = null;
      } else {
        month1 = Month.create(long_.longValue());
      } 
      return new CalendarConstraints(month2, month3, dateValidator, month1);
    }
    
    public Builder setEnd(long param1Long) {
      this.end = param1Long;
      return this;
    }
    
    public Builder setOpenAt(long param1Long) {
      this.openAt = Long.valueOf(param1Long);
      return this;
    }
    
    public Builder setStart(long param1Long) {
      this.start = param1Long;
      return this;
    }
    
    public Builder setValidator(CalendarConstraints.DateValidator param1DateValidator) {
      this.validator = param1DateValidator;
      return this;
    }
  }
  
  public static interface DateValidator extends Parcelable {
    boolean isValid(long param1Long);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\datepicker\CalendarConstraints.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */