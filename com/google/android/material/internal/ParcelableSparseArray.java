package com.google.android.material.internal;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseArray;

public class ParcelableSparseArray extends SparseArray<Parcelable> implements Parcelable {
  public static final Parcelable.Creator<ParcelableSparseArray> CREATOR = (Parcelable.Creator<ParcelableSparseArray>)new Parcelable.ClassLoaderCreator<ParcelableSparseArray>() {
      public ParcelableSparseArray createFromParcel(Parcel param1Parcel) {
        return new ParcelableSparseArray(param1Parcel, null);
      }
      
      public ParcelableSparseArray createFromParcel(Parcel param1Parcel, ClassLoader param1ClassLoader) {
        return new ParcelableSparseArray(param1Parcel, param1ClassLoader);
      }
      
      public ParcelableSparseArray[] newArray(int param1Int) {
        return new ParcelableSparseArray[param1Int];
      }
    };
  
  public ParcelableSparseArray() {}
  
  public ParcelableSparseArray(Parcel paramParcel, ClassLoader paramClassLoader) {
    int i = paramParcel.readInt();
    int[] arrayOfInt = new int[i];
    paramParcel.readIntArray(arrayOfInt);
    Parcelable[] arrayOfParcelable = paramParcel.readParcelableArray(paramClassLoader);
    for (byte b = 0; b < i; b++)
      put(arrayOfInt[b], arrayOfParcelable[b]); 
  }
  
  public int describeContents() {
    return 0;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt) {
    int i = size();
    int[] arrayOfInt = new int[i];
    Parcelable[] arrayOfParcelable = new Parcelable[i];
    for (byte b = 0; b < i; b++) {
      arrayOfInt[b] = keyAt(b);
      arrayOfParcelable[b] = (Parcelable)valueAt(b);
    } 
    paramParcel.writeInt(i);
    paramParcel.writeIntArray(arrayOfInt);
    paramParcel.writeParcelableArray(arrayOfParcelable, paramInt);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\internal\ParcelableSparseArray.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */