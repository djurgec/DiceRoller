package androidx.activity.result;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Parcel;
import android.os.Parcelable;

public final class IntentSenderRequest implements Parcelable {
  public static final Parcelable.Creator<IntentSenderRequest> CREATOR = new Parcelable.Creator<IntentSenderRequest>() {
      public IntentSenderRequest createFromParcel(Parcel param1Parcel) {
        return new IntentSenderRequest(param1Parcel);
      }
      
      public IntentSenderRequest[] newArray(int param1Int) {
        return new IntentSenderRequest[param1Int];
      }
    };
  
  private final Intent mFillInIntent;
  
  private final int mFlagsMask;
  
  private final int mFlagsValues;
  
  private final IntentSender mIntentSender;
  
  IntentSenderRequest(IntentSender paramIntentSender, Intent paramIntent, int paramInt1, int paramInt2) {
    this.mIntentSender = paramIntentSender;
    this.mFillInIntent = paramIntent;
    this.mFlagsMask = paramInt1;
    this.mFlagsValues = paramInt2;
  }
  
  IntentSenderRequest(Parcel paramParcel) {
    this.mIntentSender = (IntentSender)paramParcel.readParcelable(IntentSender.class.getClassLoader());
    this.mFillInIntent = (Intent)paramParcel.readParcelable(Intent.class.getClassLoader());
    this.mFlagsMask = paramParcel.readInt();
    this.mFlagsValues = paramParcel.readInt();
  }
  
  public int describeContents() {
    return 0;
  }
  
  public Intent getFillInIntent() {
    return this.mFillInIntent;
  }
  
  public int getFlagsMask() {
    return this.mFlagsMask;
  }
  
  public int getFlagsValues() {
    return this.mFlagsValues;
  }
  
  public IntentSender getIntentSender() {
    return this.mIntentSender;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt) {
    paramParcel.writeParcelable((Parcelable)this.mIntentSender, paramInt);
    paramParcel.writeParcelable((Parcelable)this.mFillInIntent, paramInt);
    paramParcel.writeInt(this.mFlagsMask);
    paramParcel.writeInt(this.mFlagsValues);
  }
  
  public static final class Builder {
    private Intent mFillInIntent;
    
    private int mFlagsMask;
    
    private int mFlagsValues;
    
    private IntentSender mIntentSender;
    
    public Builder(PendingIntent param1PendingIntent) {
      this(param1PendingIntent.getIntentSender());
    }
    
    public Builder(IntentSender param1IntentSender) {
      this.mIntentSender = param1IntentSender;
    }
    
    public IntentSenderRequest build() {
      return new IntentSenderRequest(this.mIntentSender, this.mFillInIntent, this.mFlagsMask, this.mFlagsValues);
    }
    
    public Builder setFillInIntent(Intent param1Intent) {
      this.mFillInIntent = param1Intent;
      return this;
    }
    
    public Builder setFlags(int param1Int1, int param1Int2) {
      this.mFlagsValues = param1Int1;
      this.mFlagsMask = param1Int2;
      return this;
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\activity\result\IntentSenderRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */