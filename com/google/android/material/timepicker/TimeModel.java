package com.google.android.material.timepicker;

import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.Arrays;

class TimeModel implements Parcelable {
  public static final Parcelable.Creator<TimeModel> CREATOR = new Parcelable.Creator<TimeModel>() {
      public TimeModel createFromParcel(Parcel param1Parcel) {
        return new TimeModel(param1Parcel);
      }
      
      public TimeModel[] newArray(int param1Int) {
        return new TimeModel[param1Int];
      }
    };
  
  public static final String NUMBER_FORMAT = "%d";
  
  public static final String ZERO_LEADING_NUMBER_FORMAT = "%02d";
  
  final int format;
  
  int hour;
  
  private final MaxInputValidator hourInputValidator;
  
  int minute;
  
  private final MaxInputValidator minuteInputValidator;
  
  int period;
  
  int selection;
  
  public TimeModel() {
    this(0);
  }
  
  public TimeModel(int paramInt) {
    this(0, 0, 10, paramInt);
  }
  
  public TimeModel(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    this.hour = paramInt1;
    this.minute = paramInt2;
    this.selection = paramInt3;
    this.format = paramInt4;
    this.period = getPeriod(paramInt1);
    this.minuteInputValidator = new MaxInputValidator(59);
    if (paramInt4 == 1) {
      paramInt1 = 24;
    } else {
      paramInt1 = 12;
    } 
    this.hourInputValidator = new MaxInputValidator(paramInt1);
  }
  
  protected TimeModel(Parcel paramParcel) {
    this(paramParcel.readInt(), paramParcel.readInt(), paramParcel.readInt(), paramParcel.readInt());
  }
  
  public static String formatText(Resources paramResources, CharSequence paramCharSequence) {
    return formatText(paramResources, paramCharSequence, "%02d");
  }
  
  public static String formatText(Resources paramResources, CharSequence paramCharSequence, String paramString) {
    return String.format((paramResources.getConfiguration()).locale, paramString, new Object[] { Integer.valueOf(Integer.parseInt(String.valueOf(paramCharSequence))) });
  }
  
  private static int getPeriod(int paramInt) {
    if (paramInt >= 12) {
      paramInt = 1;
    } else {
      paramInt = 0;
    } 
    return paramInt;
  }
  
  public int describeContents() {
    return 0;
  }
  
  public boolean equals(Object paramObject) {
    boolean bool = true;
    if (this == paramObject)
      return true; 
    if (!(paramObject instanceof TimeModel))
      return false; 
    paramObject = paramObject;
    if (this.hour != ((TimeModel)paramObject).hour || this.minute != ((TimeModel)paramObject).minute || this.format != ((TimeModel)paramObject).format || this.selection != ((TimeModel)paramObject).selection)
      bool = false; 
    return bool;
  }
  
  public int getHourForDisplay() {
    if (this.format == 1)
      return this.hour % 24; 
    int i = this.hour;
    return (i % 12 == 0) ? 12 : ((this.period == 1) ? (i - 12) : i);
  }
  
  public MaxInputValidator getHourInputValidator() {
    return this.hourInputValidator;
  }
  
  public MaxInputValidator getMinuteInputValidator() {
    return this.minuteInputValidator;
  }
  
  public int hashCode() {
    return Arrays.hashCode(new Object[] { Integer.valueOf(this.format), Integer.valueOf(this.hour), Integer.valueOf(this.minute), Integer.valueOf(this.selection) });
  }
  
  public void setHour(int paramInt) {
    byte b;
    if (this.format == 1) {
      this.hour = paramInt;
      return;
    } 
    if (this.period == 1) {
      b = 12;
    } else {
      b = 0;
    } 
    this.hour = paramInt % 12 + b;
  }
  
  public void setHourOfDay(int paramInt) {
    this.period = getPeriod(paramInt);
    this.hour = paramInt;
  }
  
  public void setMinute(int paramInt) {
    this.minute = paramInt % 60;
  }
  
  public void setPeriod(int paramInt) {
    if (paramInt != this.period) {
      this.period = paramInt;
      int i = this.hour;
      if (i < 12 && paramInt == 1) {
        this.hour = i + 12;
      } else if (i >= 12 && paramInt == 0) {
        this.hour = i - 12;
      } 
    } 
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt) {
    paramParcel.writeInt(this.hour);
    paramParcel.writeInt(this.minute);
    paramParcel.writeInt(this.selection);
    paramParcel.writeInt(this.format);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\timepicker\TimeModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */