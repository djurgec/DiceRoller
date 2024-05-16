package androidx.fragment.app;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

final class FragmentManagerState implements Parcelable {
  public static final Parcelable.Creator<FragmentManagerState> CREATOR = new Parcelable.Creator<FragmentManagerState>() {
      public FragmentManagerState createFromParcel(Parcel param1Parcel) {
        return new FragmentManagerState(param1Parcel);
      }
      
      public FragmentManagerState[] newArray(int param1Int) {
        return new FragmentManagerState[param1Int];
      }
    };
  
  ArrayList<FragmentState> mActive;
  
  ArrayList<String> mAdded;
  
  BackStackState[] mBackStack;
  
  int mBackStackIndex;
  
  ArrayList<FragmentManager.LaunchedFragmentInfo> mLaunchedFragments;
  
  String mPrimaryNavActiveWho = null;
  
  ArrayList<String> mResultKeys = new ArrayList<>();
  
  ArrayList<Bundle> mResults = new ArrayList<>();
  
  public FragmentManagerState() {}
  
  public FragmentManagerState(Parcel paramParcel) {
    this.mActive = paramParcel.createTypedArrayList(FragmentState.CREATOR);
    this.mAdded = paramParcel.createStringArrayList();
    this.mBackStack = (BackStackState[])paramParcel.createTypedArray(BackStackState.CREATOR);
    this.mBackStackIndex = paramParcel.readInt();
    this.mPrimaryNavActiveWho = paramParcel.readString();
    this.mResultKeys = paramParcel.createStringArrayList();
    this.mResults = paramParcel.createTypedArrayList(Bundle.CREATOR);
    this.mLaunchedFragments = paramParcel.createTypedArrayList(FragmentManager.LaunchedFragmentInfo.CREATOR);
  }
  
  public int describeContents() {
    return 0;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt) {
    paramParcel.writeTypedList(this.mActive);
    paramParcel.writeStringList(this.mAdded);
    paramParcel.writeTypedArray((Parcelable[])this.mBackStack, paramInt);
    paramParcel.writeInt(this.mBackStackIndex);
    paramParcel.writeString(this.mPrimaryNavActiveWho);
    paramParcel.writeStringList(this.mResultKeys);
    paramParcel.writeTypedList(this.mResults);
    paramParcel.writeTypedList(this.mLaunchedFragments);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\fragment\app\FragmentManagerState.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */