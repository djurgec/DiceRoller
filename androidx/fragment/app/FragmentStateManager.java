package androidx.fragment.app;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import androidx.core.view.ViewCompat;
import androidx.fragment.R;
import androidx.lifecycle.Lifecycle;

class FragmentStateManager {
  private static final String TAG = "FragmentManager";
  
  private static final String TARGET_REQUEST_CODE_STATE_TAG = "android:target_req_state";
  
  private static final String TARGET_STATE_TAG = "android:target_state";
  
  private static final String USER_VISIBLE_HINT_TAG = "android:user_visible_hint";
  
  private static final String VIEW_REGISTRY_STATE_TAG = "android:view_registry_state";
  
  private static final String VIEW_STATE_TAG = "android:view_state";
  
  private final FragmentLifecycleCallbacksDispatcher mDispatcher;
  
  private final Fragment mFragment;
  
  private int mFragmentManagerState = -1;
  
  private final FragmentStore mFragmentStore;
  
  private boolean mMovingToState = false;
  
  FragmentStateManager(FragmentLifecycleCallbacksDispatcher paramFragmentLifecycleCallbacksDispatcher, FragmentStore paramFragmentStore, Fragment paramFragment) {
    this.mDispatcher = paramFragmentLifecycleCallbacksDispatcher;
    this.mFragmentStore = paramFragmentStore;
    this.mFragment = paramFragment;
  }
  
  FragmentStateManager(FragmentLifecycleCallbacksDispatcher paramFragmentLifecycleCallbacksDispatcher, FragmentStore paramFragmentStore, Fragment paramFragment, FragmentState paramFragmentState) {
    this.mDispatcher = paramFragmentLifecycleCallbacksDispatcher;
    this.mFragmentStore = paramFragmentStore;
    this.mFragment = paramFragment;
    paramFragment.mSavedViewState = null;
    paramFragment.mSavedViewRegistryState = null;
    paramFragment.mBackStackNesting = 0;
    paramFragment.mInLayout = false;
    paramFragment.mAdded = false;
    if (paramFragment.mTarget != null) {
      String str = paramFragment.mTarget.mWho;
    } else {
      paramFragmentLifecycleCallbacksDispatcher = null;
    } 
    paramFragment.mTargetWho = (String)paramFragmentLifecycleCallbacksDispatcher;
    paramFragment.mTarget = null;
    if (paramFragmentState.mSavedFragmentState != null) {
      paramFragment.mSavedFragmentState = paramFragmentState.mSavedFragmentState;
    } else {
      paramFragment.mSavedFragmentState = new Bundle();
    } 
  }
  
  FragmentStateManager(FragmentLifecycleCallbacksDispatcher paramFragmentLifecycleCallbacksDispatcher, FragmentStore paramFragmentStore, ClassLoader paramClassLoader, FragmentFactory paramFragmentFactory, FragmentState paramFragmentState) {
    this.mDispatcher = paramFragmentLifecycleCallbacksDispatcher;
    this.mFragmentStore = paramFragmentStore;
    Fragment fragment = paramFragmentFactory.instantiate(paramClassLoader, paramFragmentState.mClassName);
    this.mFragment = fragment;
    if (paramFragmentState.mArguments != null)
      paramFragmentState.mArguments.setClassLoader(paramClassLoader); 
    fragment.setArguments(paramFragmentState.mArguments);
    fragment.mWho = paramFragmentState.mWho;
    fragment.mFromLayout = paramFragmentState.mFromLayout;
    fragment.mRestored = true;
    fragment.mFragmentId = paramFragmentState.mFragmentId;
    fragment.mContainerId = paramFragmentState.mContainerId;
    fragment.mTag = paramFragmentState.mTag;
    fragment.mRetainInstance = paramFragmentState.mRetainInstance;
    fragment.mRemoving = paramFragmentState.mRemoving;
    fragment.mDetached = paramFragmentState.mDetached;
    fragment.mHidden = paramFragmentState.mHidden;
    fragment.mMaxState = Lifecycle.State.values()[paramFragmentState.mMaxLifecycleState];
    if (paramFragmentState.mSavedFragmentState != null) {
      fragment.mSavedFragmentState = paramFragmentState.mSavedFragmentState;
    } else {
      fragment.mSavedFragmentState = new Bundle();
    } 
    if (FragmentManager.isLoggingEnabled(2))
      Log.v("FragmentManager", "Instantiated fragment " + fragment); 
  }
  
  private boolean isFragmentViewChild(View paramView) {
    if (paramView == this.mFragment.mView)
      return true; 
    for (ViewParent viewParent = paramView.getParent(); viewParent != null; viewParent = viewParent.getParent()) {
      if (viewParent == this.mFragment.mView)
        return true; 
    } 
    return false;
  }
  
  private Bundle saveBasicState() {
    Bundle bundle2 = new Bundle();
    this.mFragment.performSaveInstanceState(bundle2);
    this.mDispatcher.dispatchOnFragmentSaveInstanceState(this.mFragment, bundle2, false);
    Bundle bundle1 = bundle2;
    if (bundle2.isEmpty())
      bundle1 = null; 
    if (this.mFragment.mView != null)
      saveViewState(); 
    bundle2 = bundle1;
    if (this.mFragment.mSavedViewState != null) {
      bundle2 = bundle1;
      if (bundle1 == null)
        bundle2 = new Bundle(); 
      bundle2.putSparseParcelableArray("android:view_state", this.mFragment.mSavedViewState);
    } 
    bundle1 = bundle2;
    if (this.mFragment.mSavedViewRegistryState != null) {
      bundle1 = bundle2;
      if (bundle2 == null)
        bundle1 = new Bundle(); 
      bundle1.putBundle("android:view_registry_state", this.mFragment.mSavedViewRegistryState);
    } 
    bundle2 = bundle1;
    if (!this.mFragment.mUserVisibleHint) {
      bundle2 = bundle1;
      if (bundle1 == null)
        bundle2 = new Bundle(); 
      bundle2.putBoolean("android:user_visible_hint", this.mFragment.mUserVisibleHint);
    } 
    return bundle2;
  }
  
  void activityCreated() {
    if (FragmentManager.isLoggingEnabled(3))
      Log.d("FragmentManager", "moveto ACTIVITY_CREATED: " + this.mFragment); 
    Fragment fragment1 = this.mFragment;
    fragment1.performActivityCreated(fragment1.mSavedFragmentState);
    FragmentLifecycleCallbacksDispatcher fragmentLifecycleCallbacksDispatcher = this.mDispatcher;
    Fragment fragment2 = this.mFragment;
    fragmentLifecycleCallbacksDispatcher.dispatchOnFragmentActivityCreated(fragment2, fragment2.mSavedFragmentState, false);
  }
  
  void addViewToContainer() {
    int i = this.mFragmentStore.findFragmentIndexInContainer(this.mFragment);
    this.mFragment.mContainer.addView(this.mFragment.mView, i);
  }
  
  void attach() {
    FragmentStateManager fragmentStateManager;
    if (FragmentManager.isLoggingEnabled(3))
      Log.d("FragmentManager", "moveto ATTACHED: " + this.mFragment); 
    if (this.mFragment.mTarget != null) {
      fragmentStateManager = this.mFragmentStore.getFragmentStateManager(this.mFragment.mTarget.mWho);
      if (fragmentStateManager != null) {
        Fragment fragment1 = this.mFragment;
        fragment1.mTargetWho = fragment1.mTarget.mWho;
        this.mFragment.mTarget = null;
      } else {
        throw new IllegalStateException("Fragment " + this.mFragment + " declared target fragment " + this.mFragment.mTarget + " that does not belong to this FragmentManager!");
      } 
    } else if (this.mFragment.mTargetWho != null) {
      fragmentStateManager = this.mFragmentStore.getFragmentStateManager(this.mFragment.mTargetWho);
      if (fragmentStateManager == null)
        throw new IllegalStateException("Fragment " + this.mFragment + " declared target fragment " + this.mFragment.mTargetWho + " that does not belong to this FragmentManager!"); 
    } else {
      fragmentStateManager = null;
    } 
    if (fragmentStateManager != null && (FragmentManager.USE_STATE_MANAGER || (fragmentStateManager.getFragment()).mState < 1))
      fragmentStateManager.moveToExpectedState(); 
    Fragment fragment = this.mFragment;
    fragment.mHost = fragment.mFragmentManager.getHost();
    fragment = this.mFragment;
    fragment.mParentFragment = fragment.mFragmentManager.getParent();
    this.mDispatcher.dispatchOnFragmentPreAttached(this.mFragment, false);
    this.mFragment.performAttach();
    this.mDispatcher.dispatchOnFragmentAttached(this.mFragment, false);
  }
  
  int computeExpectedState() {
    if (this.mFragment.mFragmentManager == null)
      return this.mFragment.mState; 
    int j = this.mFragmentManagerState;
    switch (this.mFragment.mMaxState) {
      default:
        j = Math.min(j, -1);
        break;
      case null:
        j = Math.min(j, 0);
        break;
      case null:
        j = Math.min(j, 1);
        break;
      case null:
        j = Math.min(j, 5);
        break;
      case null:
        break;
    } 
    int i = j;
    if (this.mFragment.mFromLayout)
      if (this.mFragment.mInLayout) {
        j = Math.max(this.mFragmentManagerState, 2);
        i = j;
        if (this.mFragment.mView != null) {
          i = j;
          if (this.mFragment.mView.getParent() == null)
            i = Math.min(j, 2); 
        } 
      } else if (this.mFragmentManagerState < 4) {
        i = Math.min(j, this.mFragment.mState);
      } else {
        i = Math.min(j, 1);
      }  
    j = i;
    if (!this.mFragment.mAdded)
      j = Math.min(i, 1); 
    SpecialEffectsController.Operation.LifecycleImpact lifecycleImpact2 = null;
    SpecialEffectsController.Operation.LifecycleImpact lifecycleImpact1 = lifecycleImpact2;
    if (FragmentManager.USE_STATE_MANAGER) {
      lifecycleImpact1 = lifecycleImpact2;
      if (this.mFragment.mContainer != null)
        lifecycleImpact1 = SpecialEffectsController.getOrCreateController(this.mFragment.mContainer, this.mFragment.getParentFragmentManager()).getAwaitingCompletionLifecycleImpact(this); 
    } 
    if (lifecycleImpact1 == SpecialEffectsController.Operation.LifecycleImpact.ADDING) {
      i = Math.min(j, 6);
    } else if (lifecycleImpact1 == SpecialEffectsController.Operation.LifecycleImpact.REMOVING) {
      i = Math.max(j, 3);
    } else {
      i = j;
      if (this.mFragment.mRemoving)
        if (this.mFragment.isInBackStack()) {
          i = Math.min(j, 1);
        } else {
          i = Math.min(j, -1);
        }  
    } 
    j = i;
    if (this.mFragment.mDeferStart) {
      j = i;
      if (this.mFragment.mState < 5)
        j = Math.min(i, 4); 
    } 
    if (FragmentManager.isLoggingEnabled(2))
      Log.v("FragmentManager", "computeExpectedState() of " + j + " for " + this.mFragment); 
    return j;
  }
  
  void create() {
    if (FragmentManager.isLoggingEnabled(3))
      Log.d("FragmentManager", "moveto CREATED: " + this.mFragment); 
    if (!this.mFragment.mIsCreated) {
      FragmentLifecycleCallbacksDispatcher fragmentLifecycleCallbacksDispatcher = this.mDispatcher;
      Fragment fragment = this.mFragment;
      fragmentLifecycleCallbacksDispatcher.dispatchOnFragmentPreCreated(fragment, fragment.mSavedFragmentState, false);
      fragment = this.mFragment;
      fragment.performCreate(fragment.mSavedFragmentState);
      fragmentLifecycleCallbacksDispatcher = this.mDispatcher;
      fragment = this.mFragment;
      fragmentLifecycleCallbacksDispatcher.dispatchOnFragmentCreated(fragment, fragment.mSavedFragmentState, false);
    } else {
      Fragment fragment = this.mFragment;
      fragment.restoreChildFragmentState(fragment.mSavedFragmentState);
      this.mFragment.mState = 1;
    } 
  }
  
  void createView() {
    String str;
    if (this.mFragment.mFromLayout)
      return; 
    if (FragmentManager.isLoggingEnabled(3))
      Log.d("FragmentManager", "moveto CREATE_VIEW: " + this.mFragment); 
    Fragment fragment1 = this.mFragment;
    LayoutInflater layoutInflater = fragment1.performGetLayoutInflater(fragment1.mSavedFragmentState);
    fragment1 = null;
    if (this.mFragment.mContainer != null) {
      ViewGroup viewGroup = this.mFragment.mContainer;
    } else if (this.mFragment.mContainerId != 0) {
      if (this.mFragment.mContainerId != -1) {
        ViewGroup viewGroup2 = (ViewGroup)this.mFragment.mFragmentManager.getContainer().onFindViewById(this.mFragment.mContainerId);
        ViewGroup viewGroup1 = viewGroup2;
        if (viewGroup2 == null)
          if (this.mFragment.mRestored) {
            viewGroup1 = viewGroup2;
          } else {
            try {
              str = this.mFragment.getResources().getResourceName(this.mFragment.mContainerId);
            } catch (android.content.res.Resources.NotFoundException notFoundException) {
              str = "unknown";
            } 
            throw new IllegalArgumentException("No view found for id 0x" + Integer.toHexString(this.mFragment.mContainerId) + " (" + str + ") for fragment " + this.mFragment);
          }  
      } else {
        throw new IllegalArgumentException("Cannot create fragment " + this.mFragment + " for a container view with no id");
      } 
    } 
    this.mFragment.mContainer = (ViewGroup)str;
    Fragment fragment2 = this.mFragment;
    fragment2.performCreateView(layoutInflater, (ViewGroup)str, fragment2.mSavedFragmentState);
    if (this.mFragment.mView != null) {
      View view = this.mFragment.mView;
      boolean bool = false;
      view.setSaveFromParentEnabled(false);
      this.mFragment.mView.setTag(R.id.fragment_container_view_tag, this.mFragment);
      if (str != null)
        addViewToContainer(); 
      if (this.mFragment.mHidden)
        this.mFragment.mView.setVisibility(8); 
      if (ViewCompat.isAttachedToWindow(this.mFragment.mView)) {
        ViewCompat.requestApplyInsets(this.mFragment.mView);
      } else {
        final View fragmentView = this.mFragment.mView;
        view1.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
              final FragmentStateManager this$0;
              
              final View val$fragmentView;
              
              public void onViewAttachedToWindow(View param1View) {
                fragmentView.removeOnAttachStateChangeListener(this);
                ViewCompat.requestApplyInsets(fragmentView);
              }
              
              public void onViewDetachedFromWindow(View param1View) {}
            });
      } 
      this.mFragment.performViewCreated();
      FragmentLifecycleCallbacksDispatcher fragmentLifecycleCallbacksDispatcher = this.mDispatcher;
      Fragment fragment = this.mFragment;
      fragmentLifecycleCallbacksDispatcher.dispatchOnFragmentViewCreated(fragment, fragment.mView, this.mFragment.mSavedFragmentState, false);
      int i = this.mFragment.mView.getVisibility();
      float f = this.mFragment.mView.getAlpha();
      if (FragmentManager.USE_STATE_MANAGER) {
        this.mFragment.setPostOnViewCreatedAlpha(f);
        if (this.mFragment.mContainer != null && i == 0) {
          final View fragmentView = this.mFragment.mView.findFocus();
          if (view1 != null) {
            this.mFragment.setFocusedView(view1);
            if (FragmentManager.isLoggingEnabled(2))
              Log.v("FragmentManager", "requestFocus: Saved focused view " + view1 + " for Fragment " + this.mFragment); 
          } 
          this.mFragment.mView.setAlpha(0.0F);
        } 
      } else {
        Fragment fragment3 = this.mFragment;
        boolean bool1 = bool;
        if (i == 0) {
          bool1 = bool;
          if (fragment3.mContainer != null)
            bool1 = true; 
        } 
        fragment3.mIsNewlyAdded = bool1;
      } 
    } 
    this.mFragment.mState = 2;
  }
  
  void destroy() {
    boolean bool1;
    boolean bool2;
    if (FragmentManager.isLoggingEnabled(3))
      Log.d("FragmentManager", "movefrom CREATED: " + this.mFragment); 
    if (this.mFragment.mRemoving && !this.mFragment.isInBackStack()) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    if (bool1 || this.mFragmentStore.getNonConfig().shouldDestroy(this.mFragment)) {
      bool2 = true;
    } else {
      bool2 = false;
    } 
    if (bool2) {
      boolean bool;
      FragmentHostCallback<?> fragmentHostCallback = this.mFragment.mHost;
      if (fragmentHostCallback instanceof androidx.lifecycle.ViewModelStoreOwner) {
        bool = this.mFragmentStore.getNonConfig().isCleared();
      } else if (fragmentHostCallback.getContext() instanceof Activity) {
        bool = true ^ ((Activity)fragmentHostCallback.getContext()).isChangingConfigurations();
      } else {
        bool = true;
      } 
      if (bool1 || bool)
        this.mFragmentStore.getNonConfig().clearNonConfigState(this.mFragment); 
      this.mFragment.performDestroy();
      this.mDispatcher.dispatchOnFragmentDestroyed(this.mFragment, false);
      for (FragmentStateManager fragmentStateManager : this.mFragmentStore.getActiveFragmentStateManagers()) {
        if (fragmentStateManager != null) {
          Fragment fragment = fragmentStateManager.getFragment();
          if (this.mFragment.mWho.equals(fragment.mTargetWho)) {
            fragment.mTarget = this.mFragment;
            fragment.mTargetWho = null;
          } 
        } 
      } 
      if (this.mFragment.mTargetWho != null) {
        Fragment fragment = this.mFragment;
        fragment.mTarget = this.mFragmentStore.findActiveFragment(fragment.mTargetWho);
      } 
      this.mFragmentStore.makeInactive(this);
    } else {
      if (this.mFragment.mTargetWho != null) {
        Fragment fragment = this.mFragmentStore.findActiveFragment(this.mFragment.mTargetWho);
        if (fragment != null && fragment.mRetainInstance)
          this.mFragment.mTarget = fragment; 
      } 
      this.mFragment.mState = 0;
    } 
  }
  
  void destroyFragmentView() {
    if (FragmentManager.isLoggingEnabled(3))
      Log.d("FragmentManager", "movefrom CREATE_VIEW: " + this.mFragment); 
    if (this.mFragment.mContainer != null && this.mFragment.mView != null)
      this.mFragment.mContainer.removeView(this.mFragment.mView); 
    this.mFragment.performDestroyView();
    this.mDispatcher.dispatchOnFragmentViewDestroyed(this.mFragment, false);
    this.mFragment.mContainer = null;
    this.mFragment.mView = null;
    this.mFragment.mViewLifecycleOwner = null;
    this.mFragment.mViewLifecycleOwnerLiveData.setValue(null);
    this.mFragment.mInLayout = false;
  }
  
  void detach() {
    if (FragmentManager.isLoggingEnabled(3))
      Log.d("FragmentManager", "movefrom ATTACHED: " + this.mFragment); 
    this.mFragment.performDetach();
    FragmentLifecycleCallbacksDispatcher fragmentLifecycleCallbacksDispatcher = this.mDispatcher;
    Fragment fragment = this.mFragment;
    boolean bool2 = false;
    fragmentLifecycleCallbacksDispatcher.dispatchOnFragmentDetached(fragment, false);
    this.mFragment.mState = -1;
    this.mFragment.mHost = null;
    this.mFragment.mParentFragment = null;
    this.mFragment.mFragmentManager = null;
    boolean bool1 = bool2;
    if (this.mFragment.mRemoving) {
      bool1 = bool2;
      if (!this.mFragment.isInBackStack())
        bool1 = true; 
    } 
    if (bool1 || this.mFragmentStore.getNonConfig().shouldDestroy(this.mFragment)) {
      if (FragmentManager.isLoggingEnabled(3))
        Log.d("FragmentManager", "initState called for fragment: " + this.mFragment); 
      this.mFragment.initState();
    } 
  }
  
  void ensureInflatedView() {
    if (this.mFragment.mFromLayout && this.mFragment.mInLayout && !this.mFragment.mPerformedCreateView) {
      if (FragmentManager.isLoggingEnabled(3))
        Log.d("FragmentManager", "moveto CREATE_VIEW: " + this.mFragment); 
      Fragment fragment = this.mFragment;
      fragment.performCreateView(fragment.performGetLayoutInflater(fragment.mSavedFragmentState), null, this.mFragment.mSavedFragmentState);
      if (this.mFragment.mView != null) {
        this.mFragment.mView.setSaveFromParentEnabled(false);
        this.mFragment.mView.setTag(R.id.fragment_container_view_tag, this.mFragment);
        if (this.mFragment.mHidden)
          this.mFragment.mView.setVisibility(8); 
        this.mFragment.performViewCreated();
        FragmentLifecycleCallbacksDispatcher fragmentLifecycleCallbacksDispatcher = this.mDispatcher;
        Fragment fragment1 = this.mFragment;
        fragmentLifecycleCallbacksDispatcher.dispatchOnFragmentViewCreated(fragment1, fragment1.mView, this.mFragment.mSavedFragmentState, false);
        this.mFragment.mState = 2;
      } 
    } 
  }
  
  Fragment getFragment() {
    return this.mFragment;
  }
  
  void moveToExpectedState() {
    if (this.mMovingToState) {
      if (FragmentManager.isLoggingEnabled(2))
        Log.v("FragmentManager", "Ignoring re-entrant call to moveToExpectedState() for " + getFragment()); 
      return;
    } 
    try {
      this.mMovingToState = true;
      while (true) {
        int i = computeExpectedState();
        if (i != this.mFragment.mState) {
          if (i > this.mFragment.mState) {
            switch (this.mFragment.mState + 1) {
              default:
                continue;
              case 7:
                resume();
                continue;
              case 6:
                this.mFragment.mState = 6;
                continue;
              case 5:
                start();
                continue;
              case 4:
                if (this.mFragment.mView != null && this.mFragment.mContainer != null) {
                  SpecialEffectsController specialEffectsController = SpecialEffectsController.getOrCreateController(this.mFragment.mContainer, this.mFragment.getParentFragmentManager());
                  i = this.mFragment.mView.getVisibility();
                  specialEffectsController.enqueueAdd(SpecialEffectsController.Operation.State.from(i), this);
                } 
                this.mFragment.mState = 4;
                continue;
              case 3:
                activityCreated();
                continue;
              case 2:
                ensureInflatedView();
                createView();
                continue;
              case 1:
                create();
                continue;
              case 0:
                break;
            } 
            attach();
            continue;
          } 
          switch (this.mFragment.mState - 1) {
            default:
              continue;
            case 6:
              pause();
              continue;
            case 5:
              this.mFragment.mState = 5;
              continue;
            case 4:
              stop();
              continue;
            case 3:
              if (FragmentManager.isLoggingEnabled(3)) {
                StringBuilder stringBuilder = new StringBuilder();
                this();
                Log.d("FragmentManager", stringBuilder.append("movefrom ACTIVITY_CREATED: ").append(this.mFragment).toString());
              } 
              if (this.mFragment.mView != null && this.mFragment.mSavedViewState == null)
                saveViewState(); 
              if (this.mFragment.mView != null && this.mFragment.mContainer != null)
                SpecialEffectsController.getOrCreateController(this.mFragment.mContainer, this.mFragment.getParentFragmentManager()).enqueueRemove(this); 
              this.mFragment.mState = 3;
              continue;
            case 2:
              this.mFragment.mInLayout = false;
              this.mFragment.mState = 2;
              continue;
            case 1:
              destroyFragmentView();
              this.mFragment.mState = 1;
              continue;
            case 0:
              destroy();
              continue;
            case -1:
              break;
          } 
          detach();
          continue;
        } 
        if (FragmentManager.USE_STATE_MANAGER && this.mFragment.mHiddenChanged) {
          if (this.mFragment.mView != null && this.mFragment.mContainer != null) {
            SpecialEffectsController specialEffectsController = SpecialEffectsController.getOrCreateController(this.mFragment.mContainer, this.mFragment.getParentFragmentManager());
            if (this.mFragment.mHidden) {
              specialEffectsController.enqueueHide(this);
            } else {
              specialEffectsController.enqueueShow(this);
            } 
          } 
          if (this.mFragment.mFragmentManager != null)
            this.mFragment.mFragmentManager.invalidateMenuForFragment(this.mFragment); 
          this.mFragment.mHiddenChanged = false;
          Fragment fragment = this.mFragment;
          fragment.onHiddenChanged(fragment.mHidden);
        } 
        return;
      } 
    } finally {
      this.mMovingToState = false;
    } 
  }
  
  void pause() {
    if (FragmentManager.isLoggingEnabled(3))
      Log.d("FragmentManager", "movefrom RESUMED: " + this.mFragment); 
    this.mFragment.performPause();
    this.mDispatcher.dispatchOnFragmentPaused(this.mFragment, false);
  }
  
  void restoreState(ClassLoader paramClassLoader) {
    if (this.mFragment.mSavedFragmentState == null)
      return; 
    this.mFragment.mSavedFragmentState.setClassLoader(paramClassLoader);
    Fragment fragment = this.mFragment;
    fragment.mSavedViewState = fragment.mSavedFragmentState.getSparseParcelableArray("android:view_state");
    fragment = this.mFragment;
    fragment.mSavedViewRegistryState = fragment.mSavedFragmentState.getBundle("android:view_registry_state");
    fragment = this.mFragment;
    fragment.mTargetWho = fragment.mSavedFragmentState.getString("android:target_state");
    if (this.mFragment.mTargetWho != null) {
      fragment = this.mFragment;
      fragment.mTargetRequestCode = fragment.mSavedFragmentState.getInt("android:target_req_state", 0);
    } 
    if (this.mFragment.mSavedUserVisibleHint != null) {
      fragment = this.mFragment;
      fragment.mUserVisibleHint = fragment.mSavedUserVisibleHint.booleanValue();
      this.mFragment.mSavedUserVisibleHint = null;
    } else {
      fragment = this.mFragment;
      fragment.mUserVisibleHint = fragment.mSavedFragmentState.getBoolean("android:user_visible_hint", true);
    } 
    if (!this.mFragment.mUserVisibleHint)
      this.mFragment.mDeferStart = true; 
  }
  
  void resume() {
    if (FragmentManager.isLoggingEnabled(3))
      Log.d("FragmentManager", "moveto RESUMED: " + this.mFragment); 
    View view = this.mFragment.getFocusedView();
    if (view != null && isFragmentViewChild(view)) {
      boolean bool = view.requestFocus();
      if (FragmentManager.isLoggingEnabled(2)) {
        String str;
        StringBuilder stringBuilder = (new StringBuilder()).append("requestFocus: Restoring focused view ").append(view).append(" ");
        if (bool) {
          str = "succeeded";
        } else {
          str = "failed";
        } 
        Log.v("FragmentManager", stringBuilder.append(str).append(" on Fragment ").append(this.mFragment).append(" resulting in focused view ").append(this.mFragment.mView.findFocus()).toString());
      } 
    } 
    this.mFragment.setFocusedView(null);
    this.mFragment.performResume();
    this.mDispatcher.dispatchOnFragmentResumed(this.mFragment, false);
    this.mFragment.mSavedFragmentState = null;
    this.mFragment.mSavedViewState = null;
    this.mFragment.mSavedViewRegistryState = null;
  }
  
  Fragment.SavedState saveInstanceState() {
    int i = this.mFragment.mState;
    Fragment.SavedState savedState = null;
    if (i > -1) {
      Bundle bundle = saveBasicState();
      if (bundle != null)
        savedState = new Fragment.SavedState(bundle); 
      return savedState;
    } 
    return null;
  }
  
  FragmentState saveState() {
    FragmentState fragmentState = new FragmentState(this.mFragment);
    if (this.mFragment.mState > -1 && fragmentState.mSavedFragmentState == null) {
      fragmentState.mSavedFragmentState = saveBasicState();
      if (this.mFragment.mTargetWho != null) {
        if (fragmentState.mSavedFragmentState == null)
          fragmentState.mSavedFragmentState = new Bundle(); 
        fragmentState.mSavedFragmentState.putString("android:target_state", this.mFragment.mTargetWho);
        if (this.mFragment.mTargetRequestCode != 0)
          fragmentState.mSavedFragmentState.putInt("android:target_req_state", this.mFragment.mTargetRequestCode); 
      } 
    } else {
      fragmentState.mSavedFragmentState = this.mFragment.mSavedFragmentState;
    } 
    return fragmentState;
  }
  
  void saveViewState() {
    if (this.mFragment.mView == null)
      return; 
    SparseArray<Parcelable> sparseArray = new SparseArray();
    this.mFragment.mView.saveHierarchyState(sparseArray);
    if (sparseArray.size() > 0)
      this.mFragment.mSavedViewState = sparseArray; 
    Bundle bundle = new Bundle();
    this.mFragment.mViewLifecycleOwner.performSave(bundle);
    if (!bundle.isEmpty())
      this.mFragment.mSavedViewRegistryState = bundle; 
  }
  
  void setFragmentManagerState(int paramInt) {
    this.mFragmentManagerState = paramInt;
  }
  
  void start() {
    if (FragmentManager.isLoggingEnabled(3))
      Log.d("FragmentManager", "moveto STARTED: " + this.mFragment); 
    this.mFragment.performStart();
    this.mDispatcher.dispatchOnFragmentStarted(this.mFragment, false);
  }
  
  void stop() {
    if (FragmentManager.isLoggingEnabled(3))
      Log.d("FragmentManager", "movefrom STARTED: " + this.mFragment); 
    this.mFragment.performStop();
    this.mDispatcher.dispatchOnFragmentStopped(this.mFragment, false);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\fragment\app\FragmentStateManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */