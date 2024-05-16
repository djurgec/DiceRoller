package com.google.android.material.internal;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseBooleanArray;

public class ParcelableSparseBooleanArray extends SparseBooleanArray implements Parcelable {
  public static final Parcelable.Creator<ParcelableSparseBooleanArray> CREATOR = new Parcelable.Creator<ParcelableSparseBooleanArray>() {
      public ParcelableSparseBooleanArray createFromParcel(Parcel param1Parcel) {
        int i = param1Parcel.readInt();
        ParcelableSparseBooleanArray parcelableSparseBooleanArray = new ParcelableSparseBooleanArray(i);
        int[] arrayOfInt = new int[i];
        boolean[] arrayOfBoolean = new boolean[i];
        param1Parcel.readIntArray(arrayOfInt);
        param1Parcel.readBooleanArray(arrayOfBoolean);
        for (byte b = 0; b < i; b++)
          parcelableSparseBooleanArray.put(arrayOfInt[b], arrayOfBoolean[b]); 
        return parcelableSparseBooleanArray;
      }
      
      public ParcelableSparseBooleanArray[] newArray(int param1Int) {
        return new ParcelableSparseBooleanArray[param1Int];
      }
    };
  
  public ParcelableSparseBooleanArray() {}
  
  public ParcelableSparseBooleanArray(int paramInt) {
    super(paramInt);
  }
  
  public ParcelableSparseBooleanArray(SparseBooleanArray paramSparseBooleanArray) {
    super(paramSparseBooleanArray.size());
    for (byte b = 0; b < paramSparseBooleanArray.size(); b++)
      put(paramSparseBooleanArray.keyAt(b), paramSparseBooleanArray.valueAt(b)); 
  }
  
  public int describeContents() {
    return 0;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt) {
    int[] arrayOfInt = new int[size()];
    boolean[] arrayOfBoolean = new boolean[size()];
    for (paramInt = 0; paramInt < size(); paramInt++) {
      arrayOfInt[paramInt] = keyAt(paramInt);
      arrayOfBoolean[paramInt] = valueAt(paramInt);
    } 
    paramParcel.writeInt(size());
    paramParcel.writeIntArray(arrayOfInt);
    paramParcel.writeBooleanArray(arrayOfBoolean);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\internal\ParcelableSparseBooleanArray.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */