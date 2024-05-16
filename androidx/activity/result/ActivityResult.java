package androidx.activity.result;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

public final class ActivityResult implements Parcelable {
  public static final Parcelable.Creator<ActivityResult> CREATOR = new Parcelable.Creator<ActivityResult>() {
      public ActivityResult createFromParcel(Parcel param1Parcel) {
        return new ActivityResult(param1Parcel);
      }
      
      public ActivityResult[] newArray(int param1Int) {
        return new ActivityResult[param1Int];
      }
    };
  
  private final Intent mData;
  
  private final int mResultCode;
  
  public ActivityResult(int paramInt, Intent paramIntent) {
    this.mResultCode = paramInt;
    this.mData = paramIntent;
  }
  
  ActivityResult(Parcel paramParcel) {
    Intent intent;
    this.mResultCode = paramParcel.readInt();
    if (paramParcel.readInt() == 0) {
      paramParcel = null;
    } else {
      intent = (Intent)Intent.CREATOR.createFromParcel(paramParcel);
    } 
    this.mData = intent;
  }
  
  public static String resultCodeToString(int paramInt) {
    switch (paramInt) {
      default:
        return String.valueOf(paramInt);
      case 0:
        return "RESULT_CANCELED";
      case -1:
        break;
    } 
    return "RESULT_OK";
  }
  
  public int describeContents() {
    return 0;
  }
  
  public Intent getData() {
    return this.mData;
  }
  
  public int getResultCode() {
    return this.mResultCode;
  }
  
  public String toString() {
    return "ActivityResult{resultCode=" + resultCodeToString(this.mResultCode) + ", data=" + this.mData + '}';
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt) {
    boolean bool;
    paramParcel.writeInt(this.mResultCode);
    if (this.mData == null) {
      bool = false;
    } else {
      bool = true;
    } 
    paramParcel.writeInt(bool);
    Intent intent = this.mData;
    if (intent != null)
      intent.writeToParcel(paramParcel, paramInt); 
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\activity\result\ActivityResult.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */