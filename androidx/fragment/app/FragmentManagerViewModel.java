package androidx.fragment.app;

import android.util.Log;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

final class FragmentManagerViewModel extends ViewModel {
  private static final ViewModelProvider.Factory FACTORY = new ViewModelProvider.Factory() {
      public <T extends ViewModel> T create(Class<T> param1Class) {
        return (T)new FragmentManagerViewModel(true);
      }
    };
  
  private static final String TAG = "FragmentManager";
  
  private final HashMap<String, FragmentManagerViewModel> mChildNonConfigs = new HashMap<>();
  
  private boolean mHasBeenCleared = false;
  
  private boolean mHasSavedSnapshot = false;
  
  private boolean mIsStateSaved = false;
  
  private final HashMap<String, Fragment> mRetainedFragments = new HashMap<>();
  
  private final boolean mStateAutomaticallySaved;
  
  private final HashMap<String, ViewModelStore> mViewModelStores = new HashMap<>();
  
  FragmentManagerViewModel(boolean paramBoolean) {
    this.mStateAutomaticallySaved = paramBoolean;
  }
  
  static FragmentManagerViewModel getInstance(ViewModelStore paramViewModelStore) {
    return (FragmentManagerViewModel)(new ViewModelProvider(paramViewModelStore, FACTORY)).get(FragmentManagerViewModel.class);
  }
  
  void addRetainedFragment(Fragment paramFragment) {
    if (this.mIsStateSaved) {
      if (FragmentManager.isLoggingEnabled(2))
        Log.v("FragmentManager", "Ignoring addRetainedFragment as the state is already saved"); 
      return;
    } 
    if (this.mRetainedFragments.containsKey(paramFragment.mWho))
      return; 
    this.mRetainedFragments.put(paramFragment.mWho, paramFragment);
    if (FragmentManager.isLoggingEnabled(2))
      Log.v("FragmentManager", "Updating retained Fragments: Added " + paramFragment); 
  }
  
  void clearNonConfigState(Fragment paramFragment) {
    if (FragmentManager.isLoggingEnabled(3))
      Log.d("FragmentManager", "Clearing non-config state for " + paramFragment); 
    FragmentManagerViewModel fragmentManagerViewModel = this.mChildNonConfigs.get(paramFragment.mWho);
    if (fragmentManagerViewModel != null) {
      fragmentManagerViewModel.onCleared();
      this.mChildNonConfigs.remove(paramFragment.mWho);
    } 
    ViewModelStore viewModelStore = this.mViewModelStores.get(paramFragment.mWho);
    if (viewModelStore != null) {
      viewModelStore.clear();
      this.mViewModelStores.remove(paramFragment.mWho);
    } 
  }
  
  public boolean equals(Object paramObject) {
    boolean bool = true;
    if (this == paramObject)
      return true; 
    if (paramObject == null || getClass() != paramObject.getClass())
      return false; 
    paramObject = paramObject;
    if (!this.mRetainedFragments.equals(((FragmentManagerViewModel)paramObject).mRetainedFragments) || !this.mChildNonConfigs.equals(((FragmentManagerViewModel)paramObject).mChildNonConfigs) || !this.mViewModelStores.equals(((FragmentManagerViewModel)paramObject).mViewModelStores))
      bool = false; 
    return bool;
  }
  
  Fragment findRetainedFragmentByWho(String paramString) {
    return this.mRetainedFragments.get(paramString);
  }
  
  FragmentManagerViewModel getChildNonConfig(Fragment paramFragment) {
    FragmentManagerViewModel fragmentManagerViewModel2 = this.mChildNonConfigs.get(paramFragment.mWho);
    FragmentManagerViewModel fragmentManagerViewModel1 = fragmentManagerViewModel2;
    if (fragmentManagerViewModel2 == null) {
      fragmentManagerViewModel1 = new FragmentManagerViewModel(this.mStateAutomaticallySaved);
      this.mChildNonConfigs.put(paramFragment.mWho, fragmentManagerViewModel1);
    } 
    return fragmentManagerViewModel1;
  }
  
  Collection<Fragment> getRetainedFragments() {
    return new ArrayList<>(this.mRetainedFragments.values());
  }
  
  @Deprecated
  FragmentManagerNonConfig getSnapshot() {
    if (this.mRetainedFragments.isEmpty() && this.mChildNonConfigs.isEmpty() && this.mViewModelStores.isEmpty())
      return null; 
    HashMap<Object, Object> hashMap = new HashMap<>();
    for (Map.Entry<String, FragmentManagerViewModel> entry : this.mChildNonConfigs.entrySet()) {
      FragmentManagerNonConfig fragmentManagerNonConfig = ((FragmentManagerViewModel)entry.getValue()).getSnapshot();
      if (fragmentManagerNonConfig != null)
        hashMap.put(entry.getKey(), fragmentManagerNonConfig); 
    } 
    this.mHasSavedSnapshot = true;
    return (this.mRetainedFragments.isEmpty() && hashMap.isEmpty() && this.mViewModelStores.isEmpty()) ? null : new FragmentManagerNonConfig(new ArrayList<>(this.mRetainedFragments.values()), (Map)hashMap, new HashMap<>(this.mViewModelStores));
  }
  
  ViewModelStore getViewModelStore(Fragment paramFragment) {
    ViewModelStore viewModelStore2 = this.mViewModelStores.get(paramFragment.mWho);
    ViewModelStore viewModelStore1 = viewModelStore2;
    if (viewModelStore2 == null) {
      viewModelStore1 = new ViewModelStore();
      this.mViewModelStores.put(paramFragment.mWho, viewModelStore1);
    } 
    return viewModelStore1;
  }
  
  public int hashCode() {
    return (this.mRetainedFragments.hashCode() * 31 + this.mChildNonConfigs.hashCode()) * 31 + this.mViewModelStores.hashCode();
  }
  
  boolean isCleared() {
    return this.mHasBeenCleared;
  }
  
  protected void onCleared() {
    if (FragmentManager.isLoggingEnabled(3))
      Log.d("FragmentManager", "onCleared called for " + this); 
    this.mHasBeenCleared = true;
  }
  
  void removeRetainedFragment(Fragment paramFragment) {
    boolean bool;
    if (this.mIsStateSaved) {
      if (FragmentManager.isLoggingEnabled(2))
        Log.v("FragmentManager", "Ignoring removeRetainedFragment as the state is already saved"); 
      return;
    } 
    if (this.mRetainedFragments.remove(paramFragment.mWho) != null) {
      bool = true;
    } else {
      bool = false;
    } 
    if (bool && FragmentManager.isLoggingEnabled(2))
      Log.v("FragmentManager", "Updating retained Fragments: Removed " + paramFragment); 
  }
  
  @Deprecated
  void restoreFromSnapshot(FragmentManagerNonConfig paramFragmentManagerNonConfig) {
    this.mRetainedFragments.clear();
    this.mChildNonConfigs.clear();
    this.mViewModelStores.clear();
    if (paramFragmentManagerNonConfig != null) {
      Collection<Fragment> collection = paramFragmentManagerNonConfig.getFragments();
      if (collection != null)
        for (Fragment fragment : collection) {
          if (fragment != null)
            this.mRetainedFragments.put(fragment.mWho, fragment); 
        }  
      Map<String, FragmentManagerNonConfig> map1 = paramFragmentManagerNonConfig.getChildNonConfigs();
      if (map1 != null)
        for (Map.Entry<String, FragmentManagerNonConfig> entry : map1.entrySet()) {
          FragmentManagerViewModel fragmentManagerViewModel = new FragmentManagerViewModel(this.mStateAutomaticallySaved);
          fragmentManagerViewModel.restoreFromSnapshot((FragmentManagerNonConfig)entry.getValue());
          this.mChildNonConfigs.put((String)entry.getKey(), fragmentManagerViewModel);
        }  
      Map<String, ViewModelStore> map = paramFragmentManagerNonConfig.getViewModelStores();
      if (map != null)
        this.mViewModelStores.putAll(map); 
    } 
    this.mHasSavedSnapshot = false;
  }
  
  void setIsStateSaved(boolean paramBoolean) {
    this.mIsStateSaved = paramBoolean;
  }
  
  boolean shouldDestroy(Fragment paramFragment) {
    return !this.mRetainedFragments.containsKey(paramFragment.mWho) ? true : (this.mStateAutomaticallySaved ? this.mHasBeenCleared : (this.mHasSavedSnapshot ^ true));
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder("FragmentManagerViewModel{");
    stringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
    stringBuilder.append("} Fragments (");
    Iterator<String> iterator = this.mRetainedFragments.values().iterator();
    while (iterator.hasNext()) {
      stringBuilder.append(iterator.next());
      if (iterator.hasNext())
        stringBuilder.append(", "); 
    } 
    stringBuilder.append(") Child Non Config (");
    iterator = this.mChildNonConfigs.keySet().iterator();
    while (iterator.hasNext()) {
      stringBuilder.append(iterator.next());
      if (iterator.hasNext())
        stringBuilder.append(", "); 
    } 
    stringBuilder.append(") ViewModelStores (");
    iterator = this.mViewModelStores.keySet().iterator();
    while (iterator.hasNext()) {
      stringBuilder.append(iterator.next());
      if (iterator.hasNext())
        stringBuilder.append(", "); 
    } 
    stringBuilder.append(')');
    return stringBuilder.toString();
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\fragment\app\FragmentManagerViewModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */