package androidx.fragment.app;

import android.util.Log;
import androidx.lifecycle.Lifecycle;
import java.io.PrintWriter;
import java.util.ArrayList;

final class BackStackRecord extends FragmentTransaction implements FragmentManager.BackStackEntry, FragmentManager.OpGenerator {
  private static final String TAG = "FragmentManager";
  
  boolean mCommitted;
  
  int mIndex;
  
  final FragmentManager mManager;
  
  BackStackRecord(FragmentManager paramFragmentManager) {
    super(fragmentFactory, classLoader);
    ClassLoader classLoader;
    this.mIndex = -1;
    this.mManager = paramFragmentManager;
  }
  
  private static boolean isFragmentPostponed(FragmentTransaction.Op paramOp) {
    boolean bool;
    Fragment fragment = paramOp.mFragment;
    if (fragment != null && fragment.mAdded && fragment.mView != null && !fragment.mDetached && !fragment.mHidden && fragment.isPostponed()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  void bumpBackStackNesting(int paramInt) {
    if (!this.mAddToBackStack)
      return; 
    if (FragmentManager.isLoggingEnabled(2))
      Log.v("FragmentManager", "Bump nesting in " + this + " by " + paramInt); 
    int i = this.mOps.size();
    for (byte b = 0; b < i; b++) {
      FragmentTransaction.Op op = this.mOps.get(b);
      if (op.mFragment != null) {
        Fragment fragment = op.mFragment;
        fragment.mBackStackNesting += paramInt;
        if (FragmentManager.isLoggingEnabled(2))
          Log.v("FragmentManager", "Bump nesting of " + op.mFragment + " to " + op.mFragment.mBackStackNesting); 
      } 
    } 
  }
  
  public int commit() {
    return commitInternal(false);
  }
  
  public int commitAllowingStateLoss() {
    return commitInternal(true);
  }
  
  int commitInternal(boolean paramBoolean) {
    if (!this.mCommitted) {
      if (FragmentManager.isLoggingEnabled(2)) {
        Log.v("FragmentManager", "Commit: " + this);
        PrintWriter printWriter = new PrintWriter(new LogWriter("FragmentManager"));
        dump("  ", printWriter);
        printWriter.close();
      } 
      this.mCommitted = true;
      if (this.mAddToBackStack) {
        this.mIndex = this.mManager.allocBackStackIndex();
      } else {
        this.mIndex = -1;
      } 
      this.mManager.enqueueAction(this, paramBoolean);
      return this.mIndex;
    } 
    throw new IllegalStateException("commit already called");
  }
  
  public void commitNow() {
    disallowAddToBackStack();
    this.mManager.execSingleAction(this, false);
  }
  
  public void commitNowAllowingStateLoss() {
    disallowAddToBackStack();
    this.mManager.execSingleAction(this, true);
  }
  
  public FragmentTransaction detach(Fragment paramFragment) {
    if (paramFragment.mFragmentManager == null || paramFragment.mFragmentManager == this.mManager)
      return super.detach(paramFragment); 
    throw new IllegalStateException("Cannot detach Fragment attached to a different FragmentManager. Fragment " + paramFragment.toString() + " is already attached to a FragmentManager.");
  }
  
  void doAddOp(int paramInt1, Fragment paramFragment, String paramString, int paramInt2) {
    super.doAddOp(paramInt1, paramFragment, paramString, paramInt2);
    paramFragment.mFragmentManager = this.mManager;
  }
  
  public void dump(String paramString, PrintWriter paramPrintWriter) {
    dump(paramString, paramPrintWriter, true);
  }
  
  public void dump(String paramString, PrintWriter paramPrintWriter, boolean paramBoolean) {
    if (paramBoolean) {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mName=");
      paramPrintWriter.print(this.mName);
      paramPrintWriter.print(" mIndex=");
      paramPrintWriter.print(this.mIndex);
      paramPrintWriter.print(" mCommitted=");
      paramPrintWriter.println(this.mCommitted);
      if (this.mTransition != 0) {
        paramPrintWriter.print(paramString);
        paramPrintWriter.print("mTransition=#");
        paramPrintWriter.print(Integer.toHexString(this.mTransition));
      } 
      if (this.mEnterAnim != 0 || this.mExitAnim != 0) {
        paramPrintWriter.print(paramString);
        paramPrintWriter.print("mEnterAnim=#");
        paramPrintWriter.print(Integer.toHexString(this.mEnterAnim));
        paramPrintWriter.print(" mExitAnim=#");
        paramPrintWriter.println(Integer.toHexString(this.mExitAnim));
      } 
      if (this.mPopEnterAnim != 0 || this.mPopExitAnim != 0) {
        paramPrintWriter.print(paramString);
        paramPrintWriter.print("mPopEnterAnim=#");
        paramPrintWriter.print(Integer.toHexString(this.mPopEnterAnim));
        paramPrintWriter.print(" mPopExitAnim=#");
        paramPrintWriter.println(Integer.toHexString(this.mPopExitAnim));
      } 
      if (this.mBreadCrumbTitleRes != 0 || this.mBreadCrumbTitleText != null) {
        paramPrintWriter.print(paramString);
        paramPrintWriter.print("mBreadCrumbTitleRes=#");
        paramPrintWriter.print(Integer.toHexString(this.mBreadCrumbTitleRes));
        paramPrintWriter.print(" mBreadCrumbTitleText=");
        paramPrintWriter.println(this.mBreadCrumbTitleText);
      } 
      if (this.mBreadCrumbShortTitleRes != 0 || this.mBreadCrumbShortTitleText != null) {
        paramPrintWriter.print(paramString);
        paramPrintWriter.print("mBreadCrumbShortTitleRes=#");
        paramPrintWriter.print(Integer.toHexString(this.mBreadCrumbShortTitleRes));
        paramPrintWriter.print(" mBreadCrumbShortTitleText=");
        paramPrintWriter.println(this.mBreadCrumbShortTitleText);
      } 
    } 
    if (!this.mOps.isEmpty()) {
      paramPrintWriter.print(paramString);
      paramPrintWriter.println("Operations:");
      int i = this.mOps.size();
      for (byte b = 0; b < i; b++) {
        String str;
        FragmentTransaction.Op op = this.mOps.get(b);
        switch (op.mCmd) {
          default:
            str = "cmd=" + op.mCmd;
            break;
          case 10:
            str = "OP_SET_MAX_LIFECYCLE";
            break;
          case 9:
            str = "UNSET_PRIMARY_NAV";
            break;
          case 8:
            str = "SET_PRIMARY_NAV";
            break;
          case 7:
            str = "ATTACH";
            break;
          case 6:
            str = "DETACH";
            break;
          case 5:
            str = "SHOW";
            break;
          case 4:
            str = "HIDE";
            break;
          case 3:
            str = "REMOVE";
            break;
          case 2:
            str = "REPLACE";
            break;
          case 1:
            str = "ADD";
            break;
          case 0:
            str = "NULL";
            break;
        } 
        paramPrintWriter.print(paramString);
        paramPrintWriter.print("  Op #");
        paramPrintWriter.print(b);
        paramPrintWriter.print(": ");
        paramPrintWriter.print(str);
        paramPrintWriter.print(" ");
        paramPrintWriter.println(op.mFragment);
        if (paramBoolean) {
          if (op.mEnterAnim != 0 || op.mExitAnim != 0) {
            paramPrintWriter.print(paramString);
            paramPrintWriter.print("enterAnim=#");
            paramPrintWriter.print(Integer.toHexString(op.mEnterAnim));
            paramPrintWriter.print(" exitAnim=#");
            paramPrintWriter.println(Integer.toHexString(op.mExitAnim));
          } 
          if (op.mPopEnterAnim != 0 || op.mPopExitAnim != 0) {
            paramPrintWriter.print(paramString);
            paramPrintWriter.print("popEnterAnim=#");
            paramPrintWriter.print(Integer.toHexString(op.mPopEnterAnim));
            paramPrintWriter.print(" popExitAnim=#");
            paramPrintWriter.println(Integer.toHexString(op.mPopExitAnim));
          } 
        } 
      } 
    } 
  }
  
  void executeOps() {
    int i = this.mOps.size();
    for (byte b = 0; b < i; b++) {
      FragmentTransaction.Op op = this.mOps.get(b);
      Fragment fragment = op.mFragment;
      if (fragment != null) {
        fragment.setPopDirection(false);
        fragment.setNextTransition(this.mTransition);
        fragment.setSharedElementNames(this.mSharedElementSourceNames, this.mSharedElementTargetNames);
      } 
      switch (op.mCmd) {
        default:
          throw new IllegalArgumentException("Unknown cmd: " + op.mCmd);
        case 10:
          this.mManager.setMaxLifecycle(fragment, op.mCurrentMaxState);
          break;
        case 9:
          this.mManager.setPrimaryNavigationFragment(null);
          break;
        case 8:
          this.mManager.setPrimaryNavigationFragment(fragment);
          break;
        case 7:
          fragment.setAnimations(op.mEnterAnim, op.mExitAnim, op.mPopEnterAnim, op.mPopExitAnim);
          this.mManager.setExitAnimationOrder(fragment, false);
          this.mManager.attachFragment(fragment);
          break;
        case 6:
          fragment.setAnimations(op.mEnterAnim, op.mExitAnim, op.mPopEnterAnim, op.mPopExitAnim);
          this.mManager.detachFragment(fragment);
          break;
        case 5:
          fragment.setAnimations(op.mEnterAnim, op.mExitAnim, op.mPopEnterAnim, op.mPopExitAnim);
          this.mManager.setExitAnimationOrder(fragment, false);
          this.mManager.showFragment(fragment);
          break;
        case 4:
          fragment.setAnimations(op.mEnterAnim, op.mExitAnim, op.mPopEnterAnim, op.mPopExitAnim);
          this.mManager.hideFragment(fragment);
          break;
        case 3:
          fragment.setAnimations(op.mEnterAnim, op.mExitAnim, op.mPopEnterAnim, op.mPopExitAnim);
          this.mManager.removeFragment(fragment);
          break;
        case 1:
          fragment.setAnimations(op.mEnterAnim, op.mExitAnim, op.mPopEnterAnim, op.mPopExitAnim);
          this.mManager.setExitAnimationOrder(fragment, false);
          this.mManager.addFragment(fragment);
          break;
      } 
      if (!this.mReorderingAllowed && op.mCmd != 1 && fragment != null && !FragmentManager.USE_STATE_MANAGER)
        this.mManager.moveFragmentToExpectedState(fragment); 
    } 
    if (!this.mReorderingAllowed && !FragmentManager.USE_STATE_MANAGER) {
      FragmentManager fragmentManager = this.mManager;
      fragmentManager.moveToState(fragmentManager.mCurState, true);
    } 
  }
  
  void executePopOps(boolean paramBoolean) {
    for (int i = this.mOps.size() - 1; i >= 0; i--) {
      FragmentTransaction.Op op = this.mOps.get(i);
      Fragment fragment = op.mFragment;
      if (fragment != null) {
        fragment.setPopDirection(true);
        fragment.setNextTransition(FragmentManager.reverseTransit(this.mTransition));
        fragment.setSharedElementNames(this.mSharedElementTargetNames, this.mSharedElementSourceNames);
      } 
      switch (op.mCmd) {
        default:
          throw new IllegalArgumentException("Unknown cmd: " + op.mCmd);
        case 10:
          this.mManager.setMaxLifecycle(fragment, op.mOldMaxState);
          break;
        case 9:
          this.mManager.setPrimaryNavigationFragment(fragment);
          break;
        case 8:
          this.mManager.setPrimaryNavigationFragment(null);
          break;
        case 7:
          fragment.setAnimations(op.mEnterAnim, op.mExitAnim, op.mPopEnterAnim, op.mPopExitAnim);
          this.mManager.setExitAnimationOrder(fragment, true);
          this.mManager.detachFragment(fragment);
          break;
        case 6:
          fragment.setAnimations(op.mEnterAnim, op.mExitAnim, op.mPopEnterAnim, op.mPopExitAnim);
          this.mManager.attachFragment(fragment);
          break;
        case 5:
          fragment.setAnimations(op.mEnterAnim, op.mExitAnim, op.mPopEnterAnim, op.mPopExitAnim);
          this.mManager.setExitAnimationOrder(fragment, true);
          this.mManager.hideFragment(fragment);
          break;
        case 4:
          fragment.setAnimations(op.mEnterAnim, op.mExitAnim, op.mPopEnterAnim, op.mPopExitAnim);
          this.mManager.showFragment(fragment);
          break;
        case 3:
          fragment.setAnimations(op.mEnterAnim, op.mExitAnim, op.mPopEnterAnim, op.mPopExitAnim);
          this.mManager.addFragment(fragment);
          break;
        case 1:
          fragment.setAnimations(op.mEnterAnim, op.mExitAnim, op.mPopEnterAnim, op.mPopExitAnim);
          this.mManager.setExitAnimationOrder(fragment, true);
          this.mManager.removeFragment(fragment);
          break;
      } 
      if (!this.mReorderingAllowed && op.mCmd != 3 && fragment != null && !FragmentManager.USE_STATE_MANAGER)
        this.mManager.moveFragmentToExpectedState(fragment); 
    } 
    if (!this.mReorderingAllowed && paramBoolean && !FragmentManager.USE_STATE_MANAGER) {
      FragmentManager fragmentManager = this.mManager;
      fragmentManager.moveToState(fragmentManager.mCurState, true);
    } 
  }
  
  Fragment expandOps(ArrayList<Fragment> paramArrayList, Fragment paramFragment) {
    int i = 0;
    Fragment fragment;
    for (fragment = paramFragment; i < this.mOps.size(); fragment = paramFragment) {
      int j;
      int k;
      int m;
      Fragment fragment1;
      FragmentTransaction.Op op = this.mOps.get(i);
      switch (op.mCmd) {
        default:
          j = i;
          paramFragment = fragment;
          break;
        case 8:
          this.mOps.add(i, new FragmentTransaction.Op(9, fragment));
          j = i + 1;
          paramFragment = op.mFragment;
          break;
        case 3:
        case 6:
          paramArrayList.remove(op.mFragment);
          j = i;
          paramFragment = fragment;
          if (op.mFragment == fragment) {
            this.mOps.add(i, new FragmentTransaction.Op(9, op.mFragment));
            j = i + 1;
            paramFragment = null;
          } 
          break;
        case 2:
          fragment1 = op.mFragment;
          m = fragment1.mContainerId;
          k = 0;
          j = paramArrayList.size() - 1;
          for (paramFragment = fragment; j >= 0; paramFragment = fragment) {
            Fragment fragment2 = paramArrayList.get(j);
            int i1 = i;
            int n = k;
            fragment = paramFragment;
            if (fragment2.mContainerId == m)
              if (fragment2 == fragment1) {
                n = 1;
                i1 = i;
                fragment = paramFragment;
              } else {
                n = i;
                fragment = paramFragment;
                if (fragment2 == paramFragment) {
                  this.mOps.add(i, new FragmentTransaction.Op(9, fragment2));
                  n = i + 1;
                  fragment = null;
                } 
                FragmentTransaction.Op op1 = new FragmentTransaction.Op(3, fragment2);
                op1.mEnterAnim = op.mEnterAnim;
                op1.mPopEnterAnim = op.mPopEnterAnim;
                op1.mExitAnim = op.mExitAnim;
                op1.mPopExitAnim = op.mPopExitAnim;
                this.mOps.add(n, op1);
                paramArrayList.remove(fragment2);
                i1 = n + 1;
                n = k;
              }  
            j--;
            i = i1;
            k = n;
          } 
          if (k != 0) {
            this.mOps.remove(i);
            i--;
          } else {
            op.mCmd = 1;
            paramArrayList.add(fragment1);
          } 
          j = i;
          break;
        case 1:
        case 7:
          paramArrayList.add(op.mFragment);
          paramFragment = fragment;
          j = i;
          break;
      } 
      i = j + 1;
    } 
    return fragment;
  }
  
  public boolean generateOps(ArrayList<BackStackRecord> paramArrayList, ArrayList<Boolean> paramArrayList1) {
    if (FragmentManager.isLoggingEnabled(2))
      Log.v("FragmentManager", "Run: " + this); 
    paramArrayList.add(this);
    paramArrayList1.add(Boolean.valueOf(false));
    if (this.mAddToBackStack)
      this.mManager.addBackStackState(this); 
    return true;
  }
  
  public CharSequence getBreadCrumbShortTitle() {
    return (this.mBreadCrumbShortTitleRes != 0) ? this.mManager.getHost().getContext().getText(this.mBreadCrumbShortTitleRes) : this.mBreadCrumbShortTitleText;
  }
  
  public int getBreadCrumbShortTitleRes() {
    return this.mBreadCrumbShortTitleRes;
  }
  
  public CharSequence getBreadCrumbTitle() {
    return (this.mBreadCrumbTitleRes != 0) ? this.mManager.getHost().getContext().getText(this.mBreadCrumbTitleRes) : this.mBreadCrumbTitleText;
  }
  
  public int getBreadCrumbTitleRes() {
    return this.mBreadCrumbTitleRes;
  }
  
  public int getId() {
    return this.mIndex;
  }
  
  public String getName() {
    return this.mName;
  }
  
  public FragmentTransaction hide(Fragment paramFragment) {
    if (paramFragment.mFragmentManager == null || paramFragment.mFragmentManager == this.mManager)
      return super.hide(paramFragment); 
    throw new IllegalStateException("Cannot hide Fragment attached to a different FragmentManager. Fragment " + paramFragment.toString() + " is already attached to a FragmentManager.");
  }
  
  boolean interactsWith(int paramInt) {
    int i = this.mOps.size();
    byte b = 0;
    while (true) {
      int j = 0;
      if (b < i) {
        FragmentTransaction.Op op = this.mOps.get(b);
        if (op.mFragment != null)
          j = op.mFragment.mContainerId; 
        if (j != 0 && j == paramInt)
          return true; 
        b++;
        continue;
      } 
      return false;
    } 
  }
  
  boolean interactsWith(ArrayList<BackStackRecord> paramArrayList, int paramInt1, int paramInt2) {
    if (paramInt2 == paramInt1)
      return false; 
    int i = this.mOps.size();
    byte b1 = -1;
    byte b = 0;
    while (b < i) {
      byte b2;
      FragmentTransaction.Op op = this.mOps.get(b);
      if (op.mFragment != null) {
        b2 = op.mFragment.mContainerId;
      } else {
        b2 = 0;
      } 
      byte b3 = b1;
      if (b2) {
        b3 = b1;
        if (b2 != b1) {
          b1 = b2;
          int j = paramInt1;
          while (true) {
            b3 = b1;
            if (j < paramInt2) {
              BackStackRecord backStackRecord = paramArrayList.get(j);
              int k = backStackRecord.mOps.size();
              for (b3 = 0; b3 < k; b3++) {
                byte b4;
                FragmentTransaction.Op op1 = backStackRecord.mOps.get(b3);
                if (op1.mFragment != null) {
                  b4 = op1.mFragment.mContainerId;
                } else {
                  b4 = 0;
                } 
                if (b4 == b2)
                  return true; 
              } 
              j++;
              continue;
            } 
            break;
          } 
        } 
      } 
      b++;
      b1 = b3;
    } 
    return false;
  }
  
  public boolean isEmpty() {
    return this.mOps.isEmpty();
  }
  
  boolean isPostponed() {
    for (byte b = 0; b < this.mOps.size(); b++) {
      if (isFragmentPostponed(this.mOps.get(b)))
        return true; 
    } 
    return false;
  }
  
  public FragmentTransaction remove(Fragment paramFragment) {
    if (paramFragment.mFragmentManager == null || paramFragment.mFragmentManager == this.mManager)
      return super.remove(paramFragment); 
    throw new IllegalStateException("Cannot remove Fragment attached to a different FragmentManager. Fragment " + paramFragment.toString() + " is already attached to a FragmentManager.");
  }
  
  public void runOnCommitRunnables() {
    if (this.mCommitRunnables != null) {
      for (byte b = 0; b < this.mCommitRunnables.size(); b++)
        ((Runnable)this.mCommitRunnables.get(b)).run(); 
      this.mCommitRunnables = null;
    } 
  }
  
  public FragmentTransaction setMaxLifecycle(Fragment paramFragment, Lifecycle.State paramState) {
    if (paramFragment.mFragmentManager == this.mManager) {
      if (paramState != Lifecycle.State.INITIALIZED || paramFragment.mState <= -1) {
        if (paramState != Lifecycle.State.DESTROYED)
          return super.setMaxLifecycle(paramFragment, paramState); 
        throw new IllegalArgumentException("Cannot set maximum Lifecycle to " + paramState + ". Use remove() to remove the fragment from the FragmentManager and trigger its destruction.");
      } 
      throw new IllegalArgumentException("Cannot set maximum Lifecycle to " + paramState + " after the Fragment has been created");
    } 
    throw new IllegalArgumentException("Cannot setMaxLifecycle for Fragment not attached to FragmentManager " + this.mManager);
  }
  
  void setOnStartPostponedListener(Fragment.OnStartEnterTransitionListener paramOnStartEnterTransitionListener) {
    for (byte b = 0; b < this.mOps.size(); b++) {
      FragmentTransaction.Op op = this.mOps.get(b);
      if (isFragmentPostponed(op))
        op.mFragment.setOnStartEnterTransitionListener(paramOnStartEnterTransitionListener); 
    } 
  }
  
  public FragmentTransaction setPrimaryNavigationFragment(Fragment paramFragment) {
    if (paramFragment == null || paramFragment.mFragmentManager == null || paramFragment.mFragmentManager == this.mManager)
      return super.setPrimaryNavigationFragment(paramFragment); 
    throw new IllegalStateException("Cannot setPrimaryNavigation for Fragment attached to a different FragmentManager. Fragment " + paramFragment.toString() + " is already attached to a FragmentManager.");
  }
  
  public FragmentTransaction show(Fragment paramFragment) {
    if (paramFragment.mFragmentManager == null || paramFragment.mFragmentManager == this.mManager)
      return super.show(paramFragment); 
    throw new IllegalStateException("Cannot show Fragment attached to a different FragmentManager. Fragment " + paramFragment.toString() + " is already attached to a FragmentManager.");
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder(128);
    stringBuilder.append("BackStackEntry{");
    stringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
    if (this.mIndex >= 0) {
      stringBuilder.append(" #");
      stringBuilder.append(this.mIndex);
    } 
    if (this.mName != null) {
      stringBuilder.append(" ");
      stringBuilder.append(this.mName);
    } 
    stringBuilder.append("}");
    return stringBuilder.toString();
  }
  
  Fragment trackAddedFragmentsInPop(ArrayList<Fragment> paramArrayList, Fragment paramFragment) {
    for (int i = this.mOps.size() - 1; i >= 0; i--) {
      FragmentTransaction.Op op = this.mOps.get(i);
      switch (op.mCmd) {
        case 10:
          op.mCurrentMaxState = op.mOldMaxState;
          break;
        case 9:
          paramFragment = op.mFragment;
          break;
        case 8:
          paramFragment = null;
          break;
        case 3:
        case 6:
          paramArrayList.add(op.mFragment);
          break;
        case 1:
        case 7:
          paramArrayList.remove(op.mFragment);
          break;
      } 
    } 
    return paramFragment;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\fragment\app\BackStackRecord.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */