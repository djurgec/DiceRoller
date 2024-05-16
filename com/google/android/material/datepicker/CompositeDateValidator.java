package com.google.android.material.datepicker;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.core.util.Preconditions;
import java.util.ArrayList;
import java.util.List;

public final class CompositeDateValidator implements CalendarConstraints.DateValidator {
  private static final Operator ALL_OPERATOR;
  
  private static final Operator ANY_OPERATOR = new Operator() {
      public int getId() {
        return 1;
      }
      
      public boolean isValid(List<CalendarConstraints.DateValidator> param1List, long param1Long) {
        for (CalendarConstraints.DateValidator dateValidator : param1List) {
          if (dateValidator != null && dateValidator.isValid(param1Long))
            return true; 
        } 
        return false;
      }
    };
  
  private static final int COMPARATOR_ALL_ID = 2;
  
  private static final int COMPARATOR_ANY_ID = 1;
  
  public static final Parcelable.Creator<CompositeDateValidator> CREATOR;
  
  private final Operator operator;
  
  private final List<CalendarConstraints.DateValidator> validators;
  
  static {
    ALL_OPERATOR = new Operator() {
        public int getId() {
          return 2;
        }
        
        public boolean isValid(List<CalendarConstraints.DateValidator> param1List, long param1Long) {
          for (CalendarConstraints.DateValidator dateValidator : param1List) {
            if (dateValidator != null && !dateValidator.isValid(param1Long))
              return false; 
          } 
          return true;
        }
      };
    CREATOR = new Parcelable.Creator<CompositeDateValidator>() {
        public CompositeDateValidator createFromParcel(Parcel param1Parcel) {
          CompositeDateValidator.Operator operator;
          ArrayList arrayList = param1Parcel.readArrayList(CalendarConstraints.DateValidator.class.getClassLoader());
          int i = param1Parcel.readInt();
          if (i == 2) {
            operator = CompositeDateValidator.ALL_OPERATOR;
          } else if (i == 1) {
            operator = CompositeDateValidator.ANY_OPERATOR;
          } else {
            operator = CompositeDateValidator.ALL_OPERATOR;
          } 
          return new CompositeDateValidator((List)Preconditions.checkNotNull(arrayList), operator);
        }
        
        public CompositeDateValidator[] newArray(int param1Int) {
          return new CompositeDateValidator[param1Int];
        }
      };
  }
  
  private CompositeDateValidator(List<CalendarConstraints.DateValidator> paramList, Operator paramOperator) {
    this.validators = paramList;
    this.operator = paramOperator;
  }
  
  public static CalendarConstraints.DateValidator allOf(List<CalendarConstraints.DateValidator> paramList) {
    return new CompositeDateValidator(paramList, ALL_OPERATOR);
  }
  
  public static CalendarConstraints.DateValidator anyOf(List<CalendarConstraints.DateValidator> paramList) {
    return new CompositeDateValidator(paramList, ANY_OPERATOR);
  }
  
  public int describeContents() {
    return 0;
  }
  
  public boolean equals(Object paramObject) {
    boolean bool = true;
    if (this == paramObject)
      return true; 
    if (!(paramObject instanceof CompositeDateValidator))
      return false; 
    paramObject = paramObject;
    if (!this.validators.equals(((CompositeDateValidator)paramObject).validators) || this.operator.getId() != ((CompositeDateValidator)paramObject).operator.getId())
      bool = false; 
    return bool;
  }
  
  public int hashCode() {
    return this.validators.hashCode();
  }
  
  public boolean isValid(long paramLong) {
    return this.operator.isValid(this.validators, paramLong);
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt) {
    paramParcel.writeList(this.validators);
    paramParcel.writeInt(this.operator.getId());
  }
  
  private static interface Operator {
    int getId();
    
    boolean isValid(List<CalendarConstraints.DateValidator> param1List, long param1Long);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\datepicker\CompositeDateValidator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */