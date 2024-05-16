package androidx.fragment.app;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import androidx.lifecycle.Lifecycle;
import java.util.ArrayList;

final class BackStackState implements Parcelable {
  public static final Parcelable.Creator<BackStackState> CREATOR = new Parcelable.Creator<BackStackState>() {
      public BackStackState createFromParcel(Parcel param1Parcel) {
        return new BackStackState(param1Parcel);
      }
      
      public BackStackState[] newArray(int param1Int) {
        return new BackStackState[param1Int];
      }
    };
  
  private static final String TAG = "FragmentManager";
  
  final int mBreadCrumbShortTitleRes;
  
  final CharSequence mBreadCrumbShortTitleText;
  
  final int mBreadCrumbTitleRes;
  
  final CharSequence mBreadCrumbTitleText;
  
  final int[] mCurrentMaxLifecycleStates;
  
  final ArrayList<String> mFragmentWhos;
  
  final int mIndex;
  
  final String mName;
  
  final int[] mOldMaxLifecycleStates;
  
  final int[] mOps;
  
  final boolean mReorderingAllowed;
  
  final ArrayList<String> mSharedElementSourceNames;
  
  final ArrayList<String> mSharedElementTargetNames;
  
  final int mTransition;
  
  public BackStackState(Parcel paramParcel) {
    boolean bool;
    this.mOps = paramParcel.createIntArray();
    this.mFragmentWhos = paramParcel.createStringArrayList();
    this.mOldMaxLifecycleStates = paramParcel.createIntArray();
    this.mCurrentMaxLifecycleStates = paramParcel.createIntArray();
    this.mTransition = paramParcel.readInt();
    this.mName = paramParcel.readString();
    this.mIndex = paramParcel.readInt();
    this.mBreadCrumbTitleRes = paramParcel.readInt();
    this.mBreadCrumbTitleText = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel);
    this.mBreadCrumbShortTitleRes = paramParcel.readInt();
    this.mBreadCrumbShortTitleText = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel);
    this.mSharedElementSourceNames = paramParcel.createStringArrayList();
    this.mSharedElementTargetNames = paramParcel.createStringArrayList();
    if (paramParcel.readInt() != 0) {
      bool = true;
    } else {
      bool = false;
    } 
    this.mReorderingAllowed = bool;
  }
  
  public BackStackState(BackStackRecord paramBackStackRecord) {
    int i = paramBackStackRecord.mOps.size();
    this.mOps = new int[i * 5];
    if (paramBackStackRecord.mAddToBackStack) {
      this.mFragmentWhos = new ArrayList<>(i);
      this.mOldMaxLifecycleStates = new int[i];
      this.mCurrentMaxLifecycleStates = new int[i];
      int j = 0;
      byte b = 0;
      while (b < i) {
        FragmentTransaction.Op op = paramBackStackRecord.mOps.get(b);
        int[] arrayOfInt = this.mOps;
        int m = j + 1;
        arrayOfInt[j] = op.mCmd;
        ArrayList<String> arrayList = this.mFragmentWhos;
        if (op.mFragment != null) {
          String str = op.mFragment.mWho;
        } else {
          arrayOfInt = null;
        } 
        arrayList.add(arrayOfInt);
        arrayOfInt = this.mOps;
        int k = m + 1;
        arrayOfInt[m] = op.mEnterAnim;
        arrayOfInt = this.mOps;
        j = k + 1;
        arrayOfInt[k] = op.mExitAnim;
        arrayOfInt = this.mOps;
        k = j + 1;
        arrayOfInt[j] = op.mPopEnterAnim;
        this.mOps[k] = op.mPopExitAnim;
        this.mOldMaxLifecycleStates[b] = op.mOldMaxState.ordinal();
        this.mCurrentMaxLifecycleStates[b] = op.mCurrentMaxState.ordinal();
        b++;
        j = k + 1;
      } 
      this.mTransition = paramBackStackRecord.mTransition;
      this.mName = paramBackStackRecord.mName;
      this.mIndex = paramBackStackRecord.mIndex;
      this.mBreadCrumbTitleRes = paramBackStackRecord.mBreadCrumbTitleRes;
      this.mBreadCrumbTitleText = paramBackStackRecord.mBreadCrumbTitleText;
      this.mBreadCrumbShortTitleRes = paramBackStackRecord.mBreadCrumbShortTitleRes;
      this.mBreadCrumbShortTitleText = paramBackStackRecord.mBreadCrumbShortTitleText;
      this.mSharedElementSourceNames = paramBackStackRecord.mSharedElementSourceNames;
      this.mSharedElementTargetNames = paramBackStackRecord.mSharedElementTargetNames;
      this.mReorderingAllowed = paramBackStackRecord.mReorderingAllowed;
      return;
    } 
    throw new IllegalStateException("Not on back stack");
  }
  
  public int describeContents() {
    return 0;
  }
  
  public BackStackRecord instantiate(FragmentManager paramFragmentManager) {
    BackStackRecord backStackRecord = new BackStackRecord(paramFragmentManager);
    int i = 0;
    byte b = 0;
    while (i < this.mOps.length) {
      FragmentTransaction.Op op = new FragmentTransaction.Op();
      int[] arrayOfInt2 = this.mOps;
      int j = i + 1;
      op.mCmd = arrayOfInt2[i];
      if (FragmentManager.isLoggingEnabled(2))
        Log.v("FragmentManager", "Instantiate " + backStackRecord + " op #" + b + " base fragment #" + this.mOps[j]); 
      String str = this.mFragmentWhos.get(b);
      if (str != null) {
        op.mFragment = paramFragmentManager.findActiveFragment(str);
      } else {
        op.mFragment = null;
      } 
      op.mOldMaxState = Lifecycle.State.values()[this.mOldMaxLifecycleStates[b]];
      op.mCurrentMaxState = Lifecycle.State.values()[this.mCurrentMaxLifecycleStates[b]];
      int[] arrayOfInt1 = this.mOps;
      int k = j + 1;
      op.mEnterAnim = arrayOfInt1[j];
      arrayOfInt1 = this.mOps;
      i = k + 1;
      op.mExitAnim = arrayOfInt1[k];
      arrayOfInt1 = this.mOps;
      j = i + 1;
      op.mPopEnterAnim = arrayOfInt1[i];
      op.mPopExitAnim = this.mOps[j];
      backStackRecord.mEnterAnim = op.mEnterAnim;
      backStackRecord.mExitAnim = op.mExitAnim;
      backStackRecord.mPopEnterAnim = op.mPopEnterAnim;
      backStackRecord.mPopExitAnim = op.mPopExitAnim;
      backStackRecord.addOp(op);
      b++;
      i = j + 1;
    } 
    backStackRecord.mTransition = this.mTransition;
    backStackRecord.mName = this.mName;
    backStackRecord.mIndex = this.mIndex;
    backStackRecord.mAddToBackStack = true;
    backStackRecord.mBreadCrumbTitleRes = this.mBreadCrumbTitleRes;
    backStackRecord.mBreadCrumbTitleText = this.mBreadCrumbTitleText;
    backStackRecord.mBreadCrumbShortTitleRes = this.mBreadCrumbShortTitleRes;
    backStackRecord.mBreadCrumbShortTitleText = this.mBreadCrumbShortTitleText;
    backStackRecord.mSharedElementSourceNames = this.mSharedElementSourceNames;
    backStackRecord.mSharedElementTargetNames = this.mSharedElementTargetNames;
    backStackRecord.mReorderingAllowed = this.mReorderingAllowed;
    backStackRecord.bumpBackStackNesting(1);
    return backStackRecord;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt) {
    paramParcel.writeIntArray(this.mOps);
    paramParcel.writeStringList(this.mFragmentWhos);
    paramParcel.writeIntArray(this.mOldMaxLifecycleStates);
    paramParcel.writeIntArray(this.mCurrentMaxLifecycleStates);
    paramParcel.writeInt(this.mTransition);
    paramParcel.writeString(this.mName);
    paramParcel.writeInt(this.mIndex);
    paramParcel.writeInt(this.mBreadCrumbTitleRes);
    TextUtils.writeToParcel(this.mBreadCrumbTitleText, paramParcel, 0);
    paramParcel.writeInt(this.mBreadCrumbShortTitleRes);
    TextUtils.writeToParcel(this.mBreadCrumbShortTitleText, paramParcel, 0);
    paramParcel.writeStringList(this.mSharedElementSourceNames);
    paramParcel.writeStringList(this.mSharedElementTargetNames);
    paramParcel.writeInt(this.mReorderingAllowed);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\fragment\app\BackStackState.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */