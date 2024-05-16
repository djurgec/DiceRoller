package com.google.android.material.internal;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseIntArray;

public class ParcelableSparseIntArray extends SparseIntArray implements Parcelable {
  public static final Parcelable.Creator<ParcelableSparseIntArray> CREATOR = new Parcelable.Creator<ParcelableSparseIntArray>() {
      public ParcelableSparseIntArray createFromParcel(Parcel param1Parcel) {
        int i = param1Parcel.readInt();
        ParcelableSparseIntArray parcelableSparseIntArray = new ParcelableSparseIntArray(i);
        int[] arrayOfInt1 = new int[i];
        int[] arrayOfInt2 = new int[i];
        param1Parcel.readIntArray(arrayOfInt1);
        param1Parcel.readIntArray(arrayOfInt2);
        for (byte b = 0; b < i; b++)
          parcelableSparseIntArray.put(arrayOfInt1[b], arrayOfInt2[b]); 
        return parcelableSparseIntArray;
      }
      
      public ParcelableSparseIntArray[] newArray(int param1Int) {
        return new ParcelableSparseIntArray[param1Int];
      }
    };
  
  public ParcelableSparseIntArray() {}
  
  public ParcelableSparseIntArray(int paramInt) {
    super(paramInt);
  }
  
  public ParcelableSparseIntArray(SparseIntArray paramSparseIntArray) {
    for (byte b = 0; b < paramSparseIntArray.size(); b++)
      put(paramSparseIntArray.keyAt(b), paramSparseIntArray.valueAt(b)); 
  }
  
  public int describeContents() {
    return 0;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt) {
    int[] arrayOfInt2 = new int[size()];
    int[] arrayOfInt1 = new int[size()];
    for (paramInt = 0; paramInt < size(); paramInt++) {
      arrayOfInt2[paramInt] = keyAt(paramInt);
      arrayOfInt1[paramInt] = valueAt(paramInt);
    } 
    paramParcel.writeInt(size());
    paramParcel.writeIntArray(arrayOfInt2);
    paramParcel.writeIntArray(arrayOfInt1);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\internal\ParcelableSparseIntArray.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */