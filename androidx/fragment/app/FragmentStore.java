package androidx.fragment.app;

import android.util.Log;
import android.view.ViewGroup;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

class FragmentStore {
  private static final String TAG = "FragmentManager";
  
  private final HashMap<String, FragmentStateManager> mActive = new HashMap<>();
  
  private final ArrayList<Fragment> mAdded = new ArrayList<>();
  
  private FragmentManagerViewModel mNonConfig;
  
  void addFragment(Fragment paramFragment) {
    if (!this.mAdded.contains(paramFragment))
      synchronized (this.mAdded) {
        this.mAdded.add(paramFragment);
        paramFragment.mAdded = true;
        return;
      }  
    throw new IllegalStateException("Fragment already added: " + paramFragment);
  }
  
  void burpActive() {
    this.mActive.values().removeAll(Collections.singleton(null));
  }
  
  boolean containsActiveFragment(String paramString) {
    boolean bool;
    if (this.mActive.get(paramString) != null) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  void dispatchStateChange(int paramInt) {
    for (FragmentStateManager fragmentStateManager : this.mActive.values()) {
      if (fragmentStateManager != null)
        fragmentStateManager.setFragmentManagerState(paramInt); 
    } 
  }
  
  void dump(String paramString, FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString) {
    String str = paramString + "    ";
    if (!this.mActive.isEmpty()) {
      paramPrintWriter.print(paramString);
      paramPrintWriter.println("Active Fragments:");
      for (FragmentStateManager fragmentStateManager : this.mActive.values()) {
        paramPrintWriter.print(paramString);
        if (fragmentStateManager != null) {
          Fragment fragment = fragmentStateManager.getFragment();
          paramPrintWriter.println(fragment);
          fragment.dump(str, paramFileDescriptor, paramPrintWriter, paramArrayOfString);
          continue;
        } 
        paramPrintWriter.println("null");
      } 
    } 
    int i = this.mAdded.size();
    if (i > 0) {
      paramPrintWriter.print(paramString);
      paramPrintWriter.println("Added Fragments:");
      for (byte b = 0; b < i; b++) {
        Fragment fragment = this.mAdded.get(b);
        paramPrintWriter.print(paramString);
        paramPrintWriter.print("  #");
        paramPrintWriter.print(b);
        paramPrintWriter.print(": ");
        paramPrintWriter.println(fragment.toString());
      } 
    } 
  }
  
  Fragment findActiveFragment(String paramString) {
    FragmentStateManager fragmentStateManager = this.mActive.get(paramString);
    return (fragmentStateManager != null) ? fragmentStateManager.getFragment() : null;
  }
  
  Fragment findFragmentById(int paramInt) {
    for (int i = this.mAdded.size() - 1; i >= 0; i--) {
      Fragment fragment = this.mAdded.get(i);
      if (fragment != null && fragment.mFragmentId == paramInt)
        return fragment; 
    } 
    for (FragmentStateManager fragmentStateManager : this.mActive.values()) {
      if (fragmentStateManager != null) {
        Fragment fragment = fragmentStateManager.getFragment();
        if (fragment.mFragmentId == paramInt)
          return fragment; 
      } 
    } 
    return null;
  }
  
  Fragment findFragmentByTag(String paramString) {
    if (paramString != null)
      for (int i = this.mAdded.size() - 1; i >= 0; i--) {
        Fragment fragment = this.mAdded.get(i);
        if (fragment != null && paramString.equals(fragment.mTag))
          return fragment; 
      }  
    if (paramString != null)
      for (FragmentStateManager fragmentStateManager : this.mActive.values()) {
        if (fragmentStateManager != null) {
          Fragment fragment = fragmentStateManager.getFragment();
          if (paramString.equals(fragment.mTag))
            return fragment; 
        } 
      }  
    return null;
  }
  
  Fragment findFragmentByWho(String paramString) {
    for (FragmentStateManager fragmentStateManager : this.mActive.values()) {
      if (fragmentStateManager != null) {
        Fragment fragment = fragmentStateManager.getFragment().findFragmentByWho(paramString);
        if (fragment != null)
          return fragment; 
      } 
    } 
    return null;
  }
  
  int findFragmentIndexInContainer(Fragment paramFragment) {
    ViewGroup viewGroup = paramFragment.mContainer;
    if (viewGroup == null)
      return -1; 
    int j = this.mAdded.indexOf(paramFragment);
    int i;
    for (i = j - 1; i >= 0; i--) {
      paramFragment = this.mAdded.get(i);
      if (paramFragment.mContainer == viewGroup && paramFragment.mView != null)
        return viewGroup.indexOfChild(paramFragment.mView) + 1; 
    } 
    for (i = j + 1; i < this.mAdded.size(); i++) {
      paramFragment = this.mAdded.get(i);
      if (paramFragment.mContainer == viewGroup && paramFragment.mView != null)
        return viewGroup.indexOfChild(paramFragment.mView); 
    } 
    return -1;
  }
  
  int getActiveFragmentCount() {
    return this.mActive.size();
  }
  
  List<FragmentStateManager> getActiveFragmentStateManagers() {
    ArrayList<FragmentStateManager> arrayList = new ArrayList();
    for (FragmentStateManager fragmentStateManager : this.mActive.values()) {
      if (fragmentStateManager != null)
        arrayList.add(fragmentStateManager); 
    } 
    return arrayList;
  }
  
  List<Fragment> getActiveFragments() {
    ArrayList<Fragment> arrayList = new ArrayList();
    for (FragmentStateManager fragmentStateManager : this.mActive.values()) {
      if (fragmentStateManager != null) {
        arrayList.add(fragmentStateManager.getFragment());
        continue;
      } 
      arrayList.add(null);
    } 
    return arrayList;
  }
  
  FragmentStateManager getFragmentStateManager(String paramString) {
    return this.mActive.get(paramString);
  }
  
  List<Fragment> getFragments() {
    if (this.mAdded.isEmpty())
      return Collections.emptyList(); 
    synchronized (this.mAdded) {
      ArrayList<Fragment> arrayList = new ArrayList();
      this((Collection)this.mAdded);
      return arrayList;
    } 
  }
  
  FragmentManagerViewModel getNonConfig() {
    return this.mNonConfig;
  }
  
  void makeActive(FragmentStateManager paramFragmentStateManager) {
    Fragment fragment = paramFragmentStateManager.getFragment();
    if (containsActiveFragment(fragment.mWho))
      return; 
    this.mActive.put(fragment.mWho, paramFragmentStateManager);
    if (fragment.mRetainInstanceChangedWhileDetached) {
      if (fragment.mRetainInstance) {
        this.mNonConfig.addRetainedFragment(fragment);
      } else {
        this.mNonConfig.removeRetainedFragment(fragment);
      } 
      fragment.mRetainInstanceChangedWhileDetached = false;
    } 
    if (FragmentManager.isLoggingEnabled(2))
      Log.v("FragmentManager", "Added fragment to active set " + fragment); 
  }
  
  void makeInactive(FragmentStateManager paramFragmentStateManager) {
    Fragment fragment = paramFragmentStateManager.getFragment();
    if (fragment.mRetainInstance)
      this.mNonConfig.removeRetainedFragment(fragment); 
    if ((FragmentStateManager)this.mActive.put(fragment.mWho, null) == null)
      return; 
    if (FragmentManager.isLoggingEnabled(2))
      Log.v("FragmentManager", "Removed fragment from active set " + fragment); 
  }
  
  void moveToExpectedState() {
    for (Fragment fragment : this.mAdded) {
      FragmentStateManager fragmentStateManager = this.mActive.get(fragment.mWho);
      if (fragmentStateManager != null)
        fragmentStateManager.moveToExpectedState(); 
    } 
    for (FragmentStateManager fragmentStateManager : this.mActive.values()) {
      if (fragmentStateManager != null) {
        boolean bool;
        fragmentStateManager.moveToExpectedState();
        Fragment fragment = fragmentStateManager.getFragment();
        if (fragment.mRemoving && !fragment.isInBackStack()) {
          bool = true;
        } else {
          bool = false;
        } 
        if (bool)
          makeInactive(fragmentStateManager); 
      } 
    } 
  }
  
  void removeFragment(Fragment paramFragment) {
    synchronized (this.mAdded) {
      this.mAdded.remove(paramFragment);
      paramFragment.mAdded = false;
      return;
    } 
  }
  
  void resetActiveFragments() {
    this.mActive.clear();
  }
  
  void restoreAddedFragments(List<String> paramList) {
    this.mAdded.clear();
    if (paramList != null)
      for (String str : paramList) {
        Fragment fragment = findActiveFragment(str);
        if (fragment != null) {
          if (FragmentManager.isLoggingEnabled(2))
            Log.v("FragmentManager", "restoreSaveState: added (" + str + "): " + fragment); 
          addFragment(fragment);
          continue;
        } 
        throw new IllegalStateException("No instantiated fragment for (" + str + ")");
      }  
  }
  
  ArrayList<FragmentState> saveActiveFragments() {
    ArrayList<FragmentState> arrayList = new ArrayList(this.mActive.size());
    for (FragmentStateManager fragmentStateManager : this.mActive.values()) {
      if (fragmentStateManager != null) {
        Fragment fragment = fragmentStateManager.getFragment();
        FragmentState fragmentState = fragmentStateManager.saveState();
        arrayList.add(fragmentState);
        if (FragmentManager.isLoggingEnabled(2))
          Log.v("FragmentManager", "Saved state of " + fragment + ": " + fragmentState.mSavedFragmentState); 
      } 
    } 
    return arrayList;
  }
  
  ArrayList<String> saveAddedFragments() {
    synchronized (this.mAdded) {
      if (this.mAdded.isEmpty())
        return null; 
      ArrayList<String> arrayList = new ArrayList();
      this(this.mAdded.size());
      for (Fragment fragment : this.mAdded) {
        arrayList.add(fragment.mWho);
        if (FragmentManager.isLoggingEnabled(2)) {
          StringBuilder stringBuilder = new StringBuilder();
          this();
          Log.v("FragmentManager", stringBuilder.append("saveAllState: adding fragment (").append(fragment.mWho).append("): ").append(fragment).toString());
        } 
      } 
      return arrayList;
    } 
  }
  
  void setNonConfig(FragmentManagerViewModel paramFragmentManagerViewModel) {
    this.mNonConfig = paramFragmentManagerViewModel;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\fragment\app\FragmentStore.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */