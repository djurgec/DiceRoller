package androidx.fragment.app;

import android.animation.Animator;
import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentSender;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultCaller;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.ActivityResultRegistry;
import androidx.activity.result.ActivityResultRegistryOwner;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.arch.core.util.Function;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.app.SharedElementCallback;
import androidx.core.view.LayoutInflaterCompat;
import androidx.lifecycle.HasDefaultViewModelProviderFactory;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.lifecycle.ViewTreeLifecycleOwner;
import androidx.lifecycle.ViewTreeViewModelStoreOwner;
import androidx.loader.app.LoaderManager;
import androidx.savedstate.SavedStateRegistry;
import androidx.savedstate.SavedStateRegistryController;
import androidx.savedstate.SavedStateRegistryOwner;
import androidx.savedstate.ViewTreeSavedStateRegistryOwner;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Fragment implements ComponentCallbacks, View.OnCreateContextMenuListener, LifecycleOwner, ViewModelStoreOwner, HasDefaultViewModelProviderFactory, SavedStateRegistryOwner, ActivityResultCaller {
  static final int ACTIVITY_CREATED = 4;
  
  static final int ATTACHED = 0;
  
  static final int AWAITING_ENTER_EFFECTS = 6;
  
  static final int AWAITING_EXIT_EFFECTS = 3;
  
  static final int CREATED = 1;
  
  static final int INITIALIZING = -1;
  
  static final int RESUMED = 7;
  
  static final int STARTED = 5;
  
  static final Object USE_DEFAULT_TRANSITION = new Object();
  
  static final int VIEW_CREATED = 2;
  
  boolean mAdded;
  
  AnimationInfo mAnimationInfo;
  
  Bundle mArguments;
  
  int mBackStackNesting;
  
  private boolean mCalled;
  
  FragmentManager mChildFragmentManager = new FragmentManagerImpl();
  
  ViewGroup mContainer;
  
  int mContainerId;
  
  private int mContentLayoutId;
  
  ViewModelProvider.Factory mDefaultFactory;
  
  boolean mDeferStart;
  
  boolean mDetached;
  
  int mFragmentId;
  
  FragmentManager mFragmentManager;
  
  boolean mFromLayout;
  
  boolean mHasMenu;
  
  boolean mHidden;
  
  boolean mHiddenChanged;
  
  FragmentHostCallback<?> mHost;
  
  boolean mInLayout;
  
  boolean mIsCreated;
  
  boolean mIsNewlyAdded;
  
  private Boolean mIsPrimaryNavigationFragment = null;
  
  LayoutInflater mLayoutInflater;
  
  LifecycleRegistry mLifecycleRegistry;
  
  Lifecycle.State mMaxState = Lifecycle.State.RESUMED;
  
  boolean mMenuVisible = true;
  
  private final AtomicInteger mNextLocalRequestCode = new AtomicInteger();
  
  private final ArrayList<OnPreAttachedListener> mOnPreAttachedListeners = new ArrayList<>();
  
  Fragment mParentFragment;
  
  boolean mPerformedCreateView;
  
  float mPostponedAlpha;
  
  Runnable mPostponedDurationRunnable = new Runnable() {
      final Fragment this$0;
      
      public void run() {
        Fragment.this.startPostponedEnterTransition();
      }
    };
  
  boolean mRemoving;
  
  boolean mRestored;
  
  boolean mRetainInstance;
  
  boolean mRetainInstanceChangedWhileDetached;
  
  Bundle mSavedFragmentState;
  
  SavedStateRegistryController mSavedStateRegistryController;
  
  Boolean mSavedUserVisibleHint;
  
  Bundle mSavedViewRegistryState;
  
  SparseArray<Parcelable> mSavedViewState;
  
  int mState = -1;
  
  String mTag;
  
  Fragment mTarget;
  
  int mTargetRequestCode;
  
  String mTargetWho = null;
  
  boolean mUserVisibleHint = true;
  
  View mView;
  
  FragmentViewLifecycleOwner mViewLifecycleOwner;
  
  MutableLiveData<LifecycleOwner> mViewLifecycleOwnerLiveData = new MutableLiveData();
  
  String mWho = UUID.randomUUID().toString();
  
  public Fragment() {
    initLifecycle();
  }
  
  public Fragment(int paramInt) {
    this();
    this.mContentLayoutId = paramInt;
  }
  
  private AnimationInfo ensureAnimationInfo() {
    if (this.mAnimationInfo == null)
      this.mAnimationInfo = new AnimationInfo(); 
    return this.mAnimationInfo;
  }
  
  private int getMinimumMaxLifecycleState() {
    return (this.mMaxState == Lifecycle.State.INITIALIZED || this.mParentFragment == null) ? this.mMaxState.ordinal() : Math.min(this.mMaxState.ordinal(), this.mParentFragment.getMinimumMaxLifecycleState());
  }
  
  private void initLifecycle() {
    this.mLifecycleRegistry = new LifecycleRegistry(this);
    this.mSavedStateRegistryController = SavedStateRegistryController.create(this);
    this.mDefaultFactory = null;
  }
  
  @Deprecated
  public static Fragment instantiate(Context paramContext, String paramString) {
    return instantiate(paramContext, paramString, null);
  }
  
  @Deprecated
  public static Fragment instantiate(Context paramContext, String paramString, Bundle paramBundle) {
    try {
      Fragment fragment = FragmentFactory.loadFragmentClass(paramContext.getClassLoader(), paramString).getConstructor(new Class[0]).newInstance(new Object[0]);
      if (paramBundle != null) {
        paramBundle.setClassLoader(fragment.getClass().getClassLoader());
        fragment.setArguments(paramBundle);
      } 
      return fragment;
    } catch (InstantiationException instantiationException) {
      throw new InstantiationException("Unable to instantiate fragment " + paramString + ": make sure class name exists, is public, and has an empty constructor that is public", instantiationException);
    } catch (IllegalAccessException illegalAccessException) {
      throw new InstantiationException("Unable to instantiate fragment " + paramString + ": make sure class name exists, is public, and has an empty constructor that is public", illegalAccessException);
    } catch (NoSuchMethodException noSuchMethodException) {
      throw new InstantiationException("Unable to instantiate fragment " + paramString + ": could not find Fragment constructor", noSuchMethodException);
    } catch (InvocationTargetException invocationTargetException) {
      throw new InstantiationException("Unable to instantiate fragment " + paramString + ": calling Fragment constructor caused an exception", invocationTargetException);
    } 
  }
  
  private <I, O> ActivityResultLauncher<I> prepareCallInternal(final ActivityResultContract<I, O> contract, final Function<Void, ActivityResultRegistry> registryProvider, final ActivityResultCallback<O> callback) {
    if (this.mState <= 1) {
      final AtomicReference ref = new AtomicReference();
      registerOnPreAttachListener(new OnPreAttachedListener() {
            final Fragment this$0;
            
            final ActivityResultCallback val$callback;
            
            final ActivityResultContract val$contract;
            
            final AtomicReference val$ref;
            
            final Function val$registryProvider;
            
            void onPreAttached() {
              String str = Fragment.this.generateActivityResultKey();
              ActivityResultRegistry activityResultRegistry = (ActivityResultRegistry)registryProvider.apply(null);
              ref.set(activityResultRegistry.register(str, Fragment.this, contract, callback));
            }
          });
      return new ActivityResultLauncher<I>() {
          final Fragment this$0;
          
          final ActivityResultContract val$contract;
          
          final AtomicReference val$ref;
          
          public ActivityResultContract<I, ?> getContract() {
            return contract;
          }
          
          public void launch(I param1I, ActivityOptionsCompat param1ActivityOptionsCompat) {
            ActivityResultLauncher activityResultLauncher = ref.get();
            if (activityResultLauncher != null) {
              activityResultLauncher.launch(param1I, param1ActivityOptionsCompat);
              return;
            } 
            throw new IllegalStateException("Operation cannot be started before fragment is in created state");
          }
          
          public void unregister() {
            ActivityResultLauncher activityResultLauncher = ref.getAndSet(null);
            if (activityResultLauncher != null)
              activityResultLauncher.unregister(); 
          }
        };
    } 
    throw new IllegalStateException("Fragment " + this + " is attempting to registerForActivityResult after being created. Fragments must call registerForActivityResult() before they are created (i.e. initialization, onAttach(), or onCreate()).");
  }
  
  private void registerOnPreAttachListener(OnPreAttachedListener paramOnPreAttachedListener) {
    if (this.mState >= 0) {
      paramOnPreAttachedListener.onPreAttached();
    } else {
      this.mOnPreAttachedListeners.add(paramOnPreAttachedListener);
    } 
  }
  
  private void restoreViewState() {
    if (FragmentManager.isLoggingEnabled(3))
      Log.d("FragmentManager", "moveto RESTORE_VIEW_STATE: " + this); 
    if (this.mView != null)
      restoreViewState(this.mSavedFragmentState); 
    this.mSavedFragmentState = null;
  }
  
  void callStartTransitionListener(boolean paramBoolean) {
    OnStartEnterTransitionListener onStartEnterTransitionListener;
    AnimationInfo animationInfo = this.mAnimationInfo;
    if (animationInfo == null) {
      animationInfo = null;
    } else {
      animationInfo.mEnterTransitionPostponed = false;
      onStartEnterTransitionListener = this.mAnimationInfo.mStartEnterTransitionListener;
      this.mAnimationInfo.mStartEnterTransitionListener = null;
    } 
    if (onStartEnterTransitionListener != null) {
      onStartEnterTransitionListener.onStartEnterTransition();
    } else if (FragmentManager.USE_STATE_MANAGER && this.mView != null) {
      ViewGroup viewGroup = this.mContainer;
      if (viewGroup != null) {
        FragmentManager fragmentManager = this.mFragmentManager;
        if (fragmentManager != null) {
          final SpecialEffectsController controller = SpecialEffectsController.getOrCreateController(viewGroup, fragmentManager);
          specialEffectsController.markPostponedState();
          if (paramBoolean) {
            this.mHost.getHandler().post(new Runnable() {
                  final Fragment this$0;
                  
                  final SpecialEffectsController val$controller;
                  
                  public void run() {
                    controller.executePendingOperations();
                  }
                });
          } else {
            specialEffectsController.executePendingOperations();
          } 
        } 
      } 
    } 
  }
  
  FragmentContainer createFragmentContainer() {
    return new FragmentContainer() {
        final Fragment this$0;
        
        public View onFindViewById(int param1Int) {
          if (Fragment.this.mView != null)
            return Fragment.this.mView.findViewById(param1Int); 
          throw new IllegalStateException("Fragment " + Fragment.this + " does not have a view");
        }
        
        public boolean onHasView() {
          boolean bool;
          if (Fragment.this.mView != null) {
            bool = true;
          } else {
            bool = false;
          } 
          return bool;
        }
      };
  }
  
  public void dump(String paramString, FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString) {
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("mFragmentId=#");
    paramPrintWriter.print(Integer.toHexString(this.mFragmentId));
    paramPrintWriter.print(" mContainerId=#");
    paramPrintWriter.print(Integer.toHexString(this.mContainerId));
    paramPrintWriter.print(" mTag=");
    paramPrintWriter.println(this.mTag);
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("mState=");
    paramPrintWriter.print(this.mState);
    paramPrintWriter.print(" mWho=");
    paramPrintWriter.print(this.mWho);
    paramPrintWriter.print(" mBackStackNesting=");
    paramPrintWriter.println(this.mBackStackNesting);
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("mAdded=");
    paramPrintWriter.print(this.mAdded);
    paramPrintWriter.print(" mRemoving=");
    paramPrintWriter.print(this.mRemoving);
    paramPrintWriter.print(" mFromLayout=");
    paramPrintWriter.print(this.mFromLayout);
    paramPrintWriter.print(" mInLayout=");
    paramPrintWriter.println(this.mInLayout);
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("mHidden=");
    paramPrintWriter.print(this.mHidden);
    paramPrintWriter.print(" mDetached=");
    paramPrintWriter.print(this.mDetached);
    paramPrintWriter.print(" mMenuVisible=");
    paramPrintWriter.print(this.mMenuVisible);
    paramPrintWriter.print(" mHasMenu=");
    paramPrintWriter.println(this.mHasMenu);
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("mRetainInstance=");
    paramPrintWriter.print(this.mRetainInstance);
    paramPrintWriter.print(" mUserVisibleHint=");
    paramPrintWriter.println(this.mUserVisibleHint);
    if (this.mFragmentManager != null) {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mFragmentManager=");
      paramPrintWriter.println(this.mFragmentManager);
    } 
    if (this.mHost != null) {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mHost=");
      paramPrintWriter.println(this.mHost);
    } 
    if (this.mParentFragment != null) {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mParentFragment=");
      paramPrintWriter.println(this.mParentFragment);
    } 
    if (this.mArguments != null) {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mArguments=");
      paramPrintWriter.println(this.mArguments);
    } 
    if (this.mSavedFragmentState != null) {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mSavedFragmentState=");
      paramPrintWriter.println(this.mSavedFragmentState);
    } 
    if (this.mSavedViewState != null) {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mSavedViewState=");
      paramPrintWriter.println(this.mSavedViewState);
    } 
    if (this.mSavedViewRegistryState != null) {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mSavedViewRegistryState=");
      paramPrintWriter.println(this.mSavedViewRegistryState);
    } 
    Fragment fragment = getTargetFragment();
    if (fragment != null) {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mTarget=");
      paramPrintWriter.print(fragment);
      paramPrintWriter.print(" mTargetRequestCode=");
      paramPrintWriter.println(this.mTargetRequestCode);
    } 
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("mPopDirection=");
    paramPrintWriter.println(getPopDirection());
    if (getEnterAnim() != 0) {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("getEnterAnim=");
      paramPrintWriter.println(getEnterAnim());
    } 
    if (getExitAnim() != 0) {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("getExitAnim=");
      paramPrintWriter.println(getExitAnim());
    } 
    if (getPopEnterAnim() != 0) {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("getPopEnterAnim=");
      paramPrintWriter.println(getPopEnterAnim());
    } 
    if (getPopExitAnim() != 0) {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("getPopExitAnim=");
      paramPrintWriter.println(getPopExitAnim());
    } 
    if (this.mContainer != null) {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mContainer=");
      paramPrintWriter.println(this.mContainer);
    } 
    if (this.mView != null) {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mView=");
      paramPrintWriter.println(this.mView);
    } 
    if (getAnimatingAway() != null) {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mAnimatingAway=");
      paramPrintWriter.println(getAnimatingAway());
    } 
    if (getContext() != null)
      LoaderManager.getInstance(this).dump(paramString, paramFileDescriptor, paramPrintWriter, paramArrayOfString); 
    paramPrintWriter.print(paramString);
    paramPrintWriter.println("Child " + this.mChildFragmentManager + ":");
    this.mChildFragmentManager.dump(paramString + "  ", paramFileDescriptor, paramPrintWriter, paramArrayOfString);
  }
  
  public final boolean equals(Object paramObject) {
    return super.equals(paramObject);
  }
  
  Fragment findFragmentByWho(String paramString) {
    return paramString.equals(this.mWho) ? this : this.mChildFragmentManager.findFragmentByWho(paramString);
  }
  
  String generateActivityResultKey() {
    return "fragment_" + this.mWho + "_rq#" + this.mNextLocalRequestCode.getAndIncrement();
  }
  
  public final FragmentActivity getActivity() {
    FragmentActivity fragmentActivity;
    FragmentHostCallback<?> fragmentHostCallback = this.mHost;
    if (fragmentHostCallback == null) {
      fragmentHostCallback = null;
    } else {
      fragmentActivity = (FragmentActivity)fragmentHostCallback.getActivity();
    } 
    return fragmentActivity;
  }
  
  public boolean getAllowEnterTransitionOverlap() {
    AnimationInfo animationInfo = this.mAnimationInfo;
    return (animationInfo == null || animationInfo.mAllowEnterTransitionOverlap == null) ? true : this.mAnimationInfo.mAllowEnterTransitionOverlap.booleanValue();
  }
  
  public boolean getAllowReturnTransitionOverlap() {
    AnimationInfo animationInfo = this.mAnimationInfo;
    return (animationInfo == null || animationInfo.mAllowReturnTransitionOverlap == null) ? true : this.mAnimationInfo.mAllowReturnTransitionOverlap.booleanValue();
  }
  
  View getAnimatingAway() {
    AnimationInfo animationInfo = this.mAnimationInfo;
    return (animationInfo == null) ? null : animationInfo.mAnimatingAway;
  }
  
  Animator getAnimator() {
    AnimationInfo animationInfo = this.mAnimationInfo;
    return (animationInfo == null) ? null : animationInfo.mAnimator;
  }
  
  public final Bundle getArguments() {
    return this.mArguments;
  }
  
  public final FragmentManager getChildFragmentManager() {
    if (this.mHost != null)
      return this.mChildFragmentManager; 
    throw new IllegalStateException("Fragment " + this + " has not been attached yet.");
  }
  
  public Context getContext() {
    Context context;
    FragmentHostCallback<?> fragmentHostCallback = this.mHost;
    if (fragmentHostCallback == null) {
      fragmentHostCallback = null;
    } else {
      context = fragmentHostCallback.getContext();
    } 
    return context;
  }
  
  public ViewModelProvider.Factory getDefaultViewModelProviderFactory() {
    if (this.mFragmentManager != null) {
      if (this.mDefaultFactory == null) {
        Application application1;
        Application application2 = null;
        Context context = requireContext().getApplicationContext();
        while (true) {
          application1 = application2;
          if (context instanceof ContextWrapper) {
            if (context instanceof Application) {
              application1 = (Application)context;
              break;
            } 
            context = ((ContextWrapper)context).getBaseContext();
            continue;
          } 
          break;
        } 
        if (application1 == null && FragmentManager.isLoggingEnabled(3))
          Log.d("FragmentManager", "Could not find Application instance from Context " + requireContext().getApplicationContext() + ", you will not be able to use AndroidViewModel with the default ViewModelProvider.Factory"); 
        this.mDefaultFactory = (ViewModelProvider.Factory)new SavedStateViewModelFactory(application1, this, getArguments());
      } 
      return this.mDefaultFactory;
    } 
    throw new IllegalStateException("Can't access ViewModels from detached fragment");
  }
  
  int getEnterAnim() {
    AnimationInfo animationInfo = this.mAnimationInfo;
    return (animationInfo == null) ? 0 : animationInfo.mEnterAnim;
  }
  
  public Object getEnterTransition() {
    AnimationInfo animationInfo = this.mAnimationInfo;
    return (animationInfo == null) ? null : animationInfo.mEnterTransition;
  }
  
  SharedElementCallback getEnterTransitionCallback() {
    AnimationInfo animationInfo = this.mAnimationInfo;
    return (animationInfo == null) ? null : animationInfo.mEnterTransitionCallback;
  }
  
  int getExitAnim() {
    AnimationInfo animationInfo = this.mAnimationInfo;
    return (animationInfo == null) ? 0 : animationInfo.mExitAnim;
  }
  
  public Object getExitTransition() {
    AnimationInfo animationInfo = this.mAnimationInfo;
    return (animationInfo == null) ? null : animationInfo.mExitTransition;
  }
  
  SharedElementCallback getExitTransitionCallback() {
    AnimationInfo animationInfo = this.mAnimationInfo;
    return (animationInfo == null) ? null : animationInfo.mExitTransitionCallback;
  }
  
  View getFocusedView() {
    AnimationInfo animationInfo = this.mAnimationInfo;
    return (animationInfo == null) ? null : animationInfo.mFocusedView;
  }
  
  @Deprecated
  public final FragmentManager getFragmentManager() {
    return this.mFragmentManager;
  }
  
  public final Object getHost() {
    FragmentHostCallback<?> fragmentHostCallback = this.mHost;
    if (fragmentHostCallback == null) {
      fragmentHostCallback = null;
    } else {
      fragmentHostCallback = (FragmentHostCallback<?>)fragmentHostCallback.onGetHost();
    } 
    return fragmentHostCallback;
  }
  
  public final int getId() {
    return this.mFragmentId;
  }
  
  public final LayoutInflater getLayoutInflater() {
    LayoutInflater layoutInflater = this.mLayoutInflater;
    return (layoutInflater == null) ? performGetLayoutInflater(null) : layoutInflater;
  }
  
  @Deprecated
  public LayoutInflater getLayoutInflater(Bundle paramBundle) {
    FragmentHostCallback<?> fragmentHostCallback = this.mHost;
    if (fragmentHostCallback != null) {
      LayoutInflater layoutInflater = fragmentHostCallback.onGetLayoutInflater();
      LayoutInflaterCompat.setFactory2(layoutInflater, this.mChildFragmentManager.getLayoutInflaterFactory());
      return layoutInflater;
    } 
    throw new IllegalStateException("onGetLayoutInflater() cannot be executed until the Fragment is attached to the FragmentManager.");
  }
  
  public Lifecycle getLifecycle() {
    return (Lifecycle)this.mLifecycleRegistry;
  }
  
  @Deprecated
  public LoaderManager getLoaderManager() {
    return LoaderManager.getInstance(this);
  }
  
  int getNextTransition() {
    AnimationInfo animationInfo = this.mAnimationInfo;
    return (animationInfo == null) ? 0 : animationInfo.mNextTransition;
  }
  
  public final Fragment getParentFragment() {
    return this.mParentFragment;
  }
  
  public final FragmentManager getParentFragmentManager() {
    FragmentManager fragmentManager = this.mFragmentManager;
    if (fragmentManager != null)
      return fragmentManager; 
    throw new IllegalStateException("Fragment " + this + " not associated with a fragment manager.");
  }
  
  boolean getPopDirection() {
    AnimationInfo animationInfo = this.mAnimationInfo;
    return (animationInfo == null) ? false : animationInfo.mIsPop;
  }
  
  int getPopEnterAnim() {
    AnimationInfo animationInfo = this.mAnimationInfo;
    return (animationInfo == null) ? 0 : animationInfo.mPopEnterAnim;
  }
  
  int getPopExitAnim() {
    AnimationInfo animationInfo = this.mAnimationInfo;
    return (animationInfo == null) ? 0 : animationInfo.mPopExitAnim;
  }
  
  float getPostOnViewCreatedAlpha() {
    AnimationInfo animationInfo = this.mAnimationInfo;
    return (animationInfo == null) ? 1.0F : animationInfo.mPostOnViewCreatedAlpha;
  }
  
  public Object getReenterTransition() {
    Object object = this.mAnimationInfo;
    if (object == null)
      return null; 
    if (((AnimationInfo)object).mReenterTransition == USE_DEFAULT_TRANSITION) {
      Object object1 = getExitTransition();
    } else {
      object = this.mAnimationInfo.mReenterTransition;
    } 
    return object;
  }
  
  public final Resources getResources() {
    return requireContext().getResources();
  }
  
  @Deprecated
  public final boolean getRetainInstance() {
    return this.mRetainInstance;
  }
  
  public Object getReturnTransition() {
    Object object = this.mAnimationInfo;
    if (object == null)
      return null; 
    if (((AnimationInfo)object).mReturnTransition == USE_DEFAULT_TRANSITION) {
      Object object1 = getEnterTransition();
    } else {
      object = this.mAnimationInfo.mReturnTransition;
    } 
    return object;
  }
  
  public final SavedStateRegistry getSavedStateRegistry() {
    return this.mSavedStateRegistryController.getSavedStateRegistry();
  }
  
  public Object getSharedElementEnterTransition() {
    AnimationInfo animationInfo = this.mAnimationInfo;
    return (animationInfo == null) ? null : animationInfo.mSharedElementEnterTransition;
  }
  
  public Object getSharedElementReturnTransition() {
    Object object = this.mAnimationInfo;
    if (object == null)
      return null; 
    if (((AnimationInfo)object).mSharedElementReturnTransition == USE_DEFAULT_TRANSITION) {
      Object object1 = getSharedElementEnterTransition();
    } else {
      object = this.mAnimationInfo.mSharedElementReturnTransition;
    } 
    return object;
  }
  
  ArrayList<String> getSharedElementSourceNames() {
    AnimationInfo animationInfo = this.mAnimationInfo;
    return (animationInfo == null || animationInfo.mSharedElementSourceNames == null) ? new ArrayList<>() : this.mAnimationInfo.mSharedElementSourceNames;
  }
  
  ArrayList<String> getSharedElementTargetNames() {
    AnimationInfo animationInfo = this.mAnimationInfo;
    return (animationInfo == null || animationInfo.mSharedElementTargetNames == null) ? new ArrayList<>() : this.mAnimationInfo.mSharedElementTargetNames;
  }
  
  public final String getString(int paramInt) {
    return getResources().getString(paramInt);
  }
  
  public final String getString(int paramInt, Object... paramVarArgs) {
    return getResources().getString(paramInt, paramVarArgs);
  }
  
  public final String getTag() {
    return this.mTag;
  }
  
  @Deprecated
  public final Fragment getTargetFragment() {
    Fragment fragment = this.mTarget;
    if (fragment != null)
      return fragment; 
    FragmentManager fragmentManager = this.mFragmentManager;
    if (fragmentManager != null) {
      String str = this.mTargetWho;
      if (str != null)
        return fragmentManager.findActiveFragment(str); 
    } 
    return null;
  }
  
  @Deprecated
  public final int getTargetRequestCode() {
    return this.mTargetRequestCode;
  }
  
  public final CharSequence getText(int paramInt) {
    return getResources().getText(paramInt);
  }
  
  @Deprecated
  public boolean getUserVisibleHint() {
    return this.mUserVisibleHint;
  }
  
  public View getView() {
    return this.mView;
  }
  
  public LifecycleOwner getViewLifecycleOwner() {
    FragmentViewLifecycleOwner fragmentViewLifecycleOwner = this.mViewLifecycleOwner;
    if (fragmentViewLifecycleOwner != null)
      return (LifecycleOwner)fragmentViewLifecycleOwner; 
    throw new IllegalStateException("Can't access the Fragment View's LifecycleOwner when getView() is null i.e., before onCreateView() or after onDestroyView()");
  }
  
  public LiveData<LifecycleOwner> getViewLifecycleOwnerLiveData() {
    return (LiveData<LifecycleOwner>)this.mViewLifecycleOwnerLiveData;
  }
  
  public ViewModelStore getViewModelStore() {
    if (this.mFragmentManager != null) {
      if (getMinimumMaxLifecycleState() != Lifecycle.State.INITIALIZED.ordinal())
        return this.mFragmentManager.getViewModelStore(this); 
      throw new IllegalStateException("Calling getViewModelStore() before a Fragment reaches onCreate() when using setMaxLifecycle(INITIALIZED) is not supported");
    } 
    throw new IllegalStateException("Can't access ViewModels from detached fragment");
  }
  
  public final boolean hasOptionsMenu() {
    return this.mHasMenu;
  }
  
  public final int hashCode() {
    return super.hashCode();
  }
  
  void initState() {
    initLifecycle();
    this.mWho = UUID.randomUUID().toString();
    this.mAdded = false;
    this.mRemoving = false;
    this.mFromLayout = false;
    this.mInLayout = false;
    this.mRestored = false;
    this.mBackStackNesting = 0;
    this.mFragmentManager = null;
    this.mChildFragmentManager = new FragmentManagerImpl();
    this.mHost = null;
    this.mFragmentId = 0;
    this.mContainerId = 0;
    this.mTag = null;
    this.mHidden = false;
    this.mDetached = false;
  }
  
  public final boolean isAdded() {
    boolean bool;
    if (this.mHost != null && this.mAdded) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public final boolean isDetached() {
    return this.mDetached;
  }
  
  public final boolean isHidden() {
    return this.mHidden;
  }
  
  boolean isHideReplaced() {
    AnimationInfo animationInfo = this.mAnimationInfo;
    return (animationInfo == null) ? false : animationInfo.mIsHideReplaced;
  }
  
  final boolean isInBackStack() {
    boolean bool;
    if (this.mBackStackNesting > 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public final boolean isInLayout() {
    return this.mInLayout;
  }
  
  public final boolean isMenuVisible() {
    if (this.mMenuVisible) {
      FragmentManager fragmentManager = this.mFragmentManager;
      if (fragmentManager == null || fragmentManager.isParentMenuVisible(this.mParentFragment))
        return true; 
    } 
    return false;
  }
  
  boolean isPostponed() {
    AnimationInfo animationInfo = this.mAnimationInfo;
    return (animationInfo == null) ? false : animationInfo.mEnterTransitionPostponed;
  }
  
  public final boolean isRemoving() {
    return this.mRemoving;
  }
  
  final boolean isRemovingParent() {
    boolean bool;
    Fragment fragment = getParentFragment();
    if (fragment != null && (fragment.isRemoving() || fragment.isRemovingParent())) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public final boolean isResumed() {
    boolean bool;
    if (this.mState >= 7) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public final boolean isStateSaved() {
    FragmentManager fragmentManager = this.mFragmentManager;
    return (fragmentManager == null) ? false : fragmentManager.isStateSaved();
  }
  
  public final boolean isVisible() {
    if (isAdded() && !isHidden()) {
      View view = this.mView;
      if (view != null && view.getWindowToken() != null && this.mView.getVisibility() == 0)
        return true; 
    } 
    return false;
  }
  
  void noteStateNotSaved() {
    this.mChildFragmentManager.noteStateNotSaved();
  }
  
  @Deprecated
  public void onActivityCreated(Bundle paramBundle) {
    this.mCalled = true;
  }
  
  @Deprecated
  public void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent) {
    if (FragmentManager.isLoggingEnabled(2))
      Log.v("FragmentManager", "Fragment " + this + " received the following in onActivityResult(): requestCode: " + paramInt1 + " resultCode: " + paramInt2 + " data: " + paramIntent); 
  }
  
  @Deprecated
  public void onAttach(Activity paramActivity) {
    this.mCalled = true;
  }
  
  public void onAttach(Context paramContext) {
    Activity activity;
    this.mCalled = true;
    FragmentHostCallback<?> fragmentHostCallback = this.mHost;
    if (fragmentHostCallback == null) {
      fragmentHostCallback = null;
    } else {
      activity = fragmentHostCallback.getActivity();
    } 
    if (activity != null) {
      this.mCalled = false;
      onAttach(activity);
    } 
  }
  
  @Deprecated
  public void onAttachFragment(Fragment paramFragment) {}
  
  public void onConfigurationChanged(Configuration paramConfiguration) {
    this.mCalled = true;
  }
  
  public boolean onContextItemSelected(MenuItem paramMenuItem) {
    return false;
  }
  
  public void onCreate(Bundle paramBundle) {
    this.mCalled = true;
    restoreChildFragmentState(paramBundle);
    if (!this.mChildFragmentManager.isStateAtLeast(1))
      this.mChildFragmentManager.dispatchCreate(); 
  }
  
  public Animation onCreateAnimation(int paramInt1, boolean paramBoolean, int paramInt2) {
    return null;
  }
  
  public Animator onCreateAnimator(int paramInt1, boolean paramBoolean, int paramInt2) {
    return null;
  }
  
  public void onCreateContextMenu(ContextMenu paramContextMenu, View paramView, ContextMenu.ContextMenuInfo paramContextMenuInfo) {
    requireActivity().onCreateContextMenu(paramContextMenu, paramView, paramContextMenuInfo);
  }
  
  public void onCreateOptionsMenu(Menu paramMenu, MenuInflater paramMenuInflater) {}
  
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
    int i = this.mContentLayoutId;
    return (i != 0) ? paramLayoutInflater.inflate(i, paramViewGroup, false) : null;
  }
  
  public void onDestroy() {
    this.mCalled = true;
  }
  
  public void onDestroyOptionsMenu() {}
  
  public void onDestroyView() {
    this.mCalled = true;
  }
  
  public void onDetach() {
    this.mCalled = true;
  }
  
  public LayoutInflater onGetLayoutInflater(Bundle paramBundle) {
    return getLayoutInflater(paramBundle);
  }
  
  public void onHiddenChanged(boolean paramBoolean) {}
  
  @Deprecated
  public void onInflate(Activity paramActivity, AttributeSet paramAttributeSet, Bundle paramBundle) {
    this.mCalled = true;
  }
  
  public void onInflate(Context paramContext, AttributeSet paramAttributeSet, Bundle paramBundle) {
    Activity activity;
    this.mCalled = true;
    FragmentHostCallback<?> fragmentHostCallback = this.mHost;
    if (fragmentHostCallback == null) {
      fragmentHostCallback = null;
    } else {
      activity = fragmentHostCallback.getActivity();
    } 
    if (activity != null) {
      this.mCalled = false;
      onInflate(activity, paramAttributeSet, paramBundle);
    } 
  }
  
  public void onLowMemory() {
    this.mCalled = true;
  }
  
  public void onMultiWindowModeChanged(boolean paramBoolean) {}
  
  public boolean onOptionsItemSelected(MenuItem paramMenuItem) {
    return false;
  }
  
  public void onOptionsMenuClosed(Menu paramMenu) {}
  
  public void onPause() {
    this.mCalled = true;
  }
  
  public void onPictureInPictureModeChanged(boolean paramBoolean) {}
  
  public void onPrepareOptionsMenu(Menu paramMenu) {}
  
  public void onPrimaryNavigationFragmentChanged(boolean paramBoolean) {}
  
  @Deprecated
  public void onRequestPermissionsResult(int paramInt, String[] paramArrayOfString, int[] paramArrayOfint) {}
  
  public void onResume() {
    this.mCalled = true;
  }
  
  public void onSaveInstanceState(Bundle paramBundle) {}
  
  public void onStart() {
    this.mCalled = true;
  }
  
  public void onStop() {
    this.mCalled = true;
  }
  
  public void onViewCreated(View paramView, Bundle paramBundle) {}
  
  public void onViewStateRestored(Bundle paramBundle) {
    this.mCalled = true;
  }
  
  void performActivityCreated(Bundle paramBundle) {
    this.mChildFragmentManager.noteStateNotSaved();
    this.mState = 3;
    this.mCalled = false;
    onActivityCreated(paramBundle);
    if (this.mCalled) {
      restoreViewState();
      this.mChildFragmentManager.dispatchActivityCreated();
      return;
    } 
    throw new SuperNotCalledException("Fragment " + this + " did not call through to super.onActivityCreated()");
  }
  
  void performAttach() {
    Iterator<OnPreAttachedListener> iterator = this.mOnPreAttachedListeners.iterator();
    while (iterator.hasNext())
      ((OnPreAttachedListener)iterator.next()).onPreAttached(); 
    this.mOnPreAttachedListeners.clear();
    this.mChildFragmentManager.attachController(this.mHost, createFragmentContainer(), this);
    this.mState = 0;
    this.mCalled = false;
    onAttach(this.mHost.getContext());
    if (this.mCalled) {
      this.mFragmentManager.dispatchOnAttachFragment(this);
      this.mChildFragmentManager.dispatchAttach();
      return;
    } 
    throw new SuperNotCalledException("Fragment " + this + " did not call through to super.onAttach()");
  }
  
  void performConfigurationChanged(Configuration paramConfiguration) {
    onConfigurationChanged(paramConfiguration);
    this.mChildFragmentManager.dispatchConfigurationChanged(paramConfiguration);
  }
  
  boolean performContextItemSelected(MenuItem paramMenuItem) {
    return !this.mHidden ? (onContextItemSelected(paramMenuItem) ? true : this.mChildFragmentManager.dispatchContextItemSelected(paramMenuItem)) : false;
  }
  
  void performCreate(Bundle paramBundle) {
    this.mChildFragmentManager.noteStateNotSaved();
    this.mState = 1;
    this.mCalled = false;
    if (Build.VERSION.SDK_INT >= 19)
      this.mLifecycleRegistry.addObserver((LifecycleObserver)new LifecycleEventObserver() {
            final Fragment this$0;
            
            public void onStateChanged(LifecycleOwner param1LifecycleOwner, Lifecycle.Event param1Event) {
              if (param1Event == Lifecycle.Event.ON_STOP && Fragment.this.mView != null)
                Fragment.this.mView.cancelPendingInputEvents(); 
            }
          }); 
    this.mSavedStateRegistryController.performRestore(paramBundle);
    onCreate(paramBundle);
    this.mIsCreated = true;
    if (this.mCalled) {
      this.mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
      return;
    } 
    throw new SuperNotCalledException("Fragment " + this + " did not call through to super.onCreate()");
  }
  
  boolean performCreateOptionsMenu(Menu paramMenu, MenuInflater paramMenuInflater) {
    boolean bool2 = false;
    boolean bool1 = false;
    if (!this.mHidden) {
      boolean bool = bool1;
      if (this.mHasMenu) {
        bool = bool1;
        if (this.mMenuVisible) {
          bool = true;
          onCreateOptionsMenu(paramMenu, paramMenuInflater);
        } 
      } 
      bool2 = bool | this.mChildFragmentManager.dispatchCreateOptionsMenu(paramMenu, paramMenuInflater);
    } 
    return bool2;
  }
  
  void performCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
    this.mChildFragmentManager.noteStateNotSaved();
    this.mPerformedCreateView = true;
    this.mViewLifecycleOwner = new FragmentViewLifecycleOwner(this, getViewModelStore());
    View view = onCreateView(paramLayoutInflater, paramViewGroup, paramBundle);
    this.mView = view;
    if (view != null) {
      this.mViewLifecycleOwner.initialize();
      ViewTreeLifecycleOwner.set(this.mView, (LifecycleOwner)this.mViewLifecycleOwner);
      ViewTreeViewModelStoreOwner.set(this.mView, this.mViewLifecycleOwner);
      ViewTreeSavedStateRegistryOwner.set(this.mView, this.mViewLifecycleOwner);
      this.mViewLifecycleOwnerLiveData.setValue(this.mViewLifecycleOwner);
    } else {
      if (!this.mViewLifecycleOwner.isInitialized()) {
        this.mViewLifecycleOwner = null;
        return;
      } 
      throw new IllegalStateException("Called getViewLifecycleOwner() but onCreateView() returned null");
    } 
  }
  
  void performDestroy() {
    this.mChildFragmentManager.dispatchDestroy();
    this.mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY);
    this.mState = 0;
    this.mCalled = false;
    this.mIsCreated = false;
    onDestroy();
    if (this.mCalled)
      return; 
    throw new SuperNotCalledException("Fragment " + this + " did not call through to super.onDestroy()");
  }
  
  void performDestroyView() {
    this.mChildFragmentManager.dispatchDestroyView();
    if (this.mView != null && this.mViewLifecycleOwner.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.CREATED))
      this.mViewLifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY); 
    this.mState = 1;
    this.mCalled = false;
    onDestroyView();
    if (this.mCalled) {
      LoaderManager.getInstance(this).markForRedelivery();
      this.mPerformedCreateView = false;
      return;
    } 
    throw new SuperNotCalledException("Fragment " + this + " did not call through to super.onDestroyView()");
  }
  
  void performDetach() {
    this.mState = -1;
    this.mCalled = false;
    onDetach();
    this.mLayoutInflater = null;
    if (this.mCalled) {
      if (!this.mChildFragmentManager.isDestroyed()) {
        this.mChildFragmentManager.dispatchDestroy();
        this.mChildFragmentManager = new FragmentManagerImpl();
      } 
      return;
    } 
    throw new SuperNotCalledException("Fragment " + this + " did not call through to super.onDetach()");
  }
  
  LayoutInflater performGetLayoutInflater(Bundle paramBundle) {
    LayoutInflater layoutInflater = onGetLayoutInflater(paramBundle);
    this.mLayoutInflater = layoutInflater;
    return layoutInflater;
  }
  
  void performLowMemory() {
    onLowMemory();
    this.mChildFragmentManager.dispatchLowMemory();
  }
  
  void performMultiWindowModeChanged(boolean paramBoolean) {
    onMultiWindowModeChanged(paramBoolean);
    this.mChildFragmentManager.dispatchMultiWindowModeChanged(paramBoolean);
  }
  
  boolean performOptionsItemSelected(MenuItem paramMenuItem) {
    return !this.mHidden ? ((this.mHasMenu && this.mMenuVisible && onOptionsItemSelected(paramMenuItem)) ? true : this.mChildFragmentManager.dispatchOptionsItemSelected(paramMenuItem)) : false;
  }
  
  void performOptionsMenuClosed(Menu paramMenu) {
    if (!this.mHidden) {
      if (this.mHasMenu && this.mMenuVisible)
        onOptionsMenuClosed(paramMenu); 
      this.mChildFragmentManager.dispatchOptionsMenuClosed(paramMenu);
    } 
  }
  
  void performPause() {
    this.mChildFragmentManager.dispatchPause();
    if (this.mView != null)
      this.mViewLifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE); 
    this.mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE);
    this.mState = 6;
    this.mCalled = false;
    onPause();
    if (this.mCalled)
      return; 
    throw new SuperNotCalledException("Fragment " + this + " did not call through to super.onPause()");
  }
  
  void performPictureInPictureModeChanged(boolean paramBoolean) {
    onPictureInPictureModeChanged(paramBoolean);
    this.mChildFragmentManager.dispatchPictureInPictureModeChanged(paramBoolean);
  }
  
  boolean performPrepareOptionsMenu(Menu paramMenu) {
    boolean bool2 = false;
    boolean bool1 = false;
    if (!this.mHidden) {
      boolean bool = bool1;
      if (this.mHasMenu) {
        bool = bool1;
        if (this.mMenuVisible) {
          bool = true;
          onPrepareOptionsMenu(paramMenu);
        } 
      } 
      bool2 = bool | this.mChildFragmentManager.dispatchPrepareOptionsMenu(paramMenu);
    } 
    return bool2;
  }
  
  void performPrimaryNavigationFragmentChanged() {
    boolean bool = this.mFragmentManager.isPrimaryNavigation(this);
    Boolean bool1 = this.mIsPrimaryNavigationFragment;
    if (bool1 == null || bool1.booleanValue() != bool) {
      this.mIsPrimaryNavigationFragment = Boolean.valueOf(bool);
      onPrimaryNavigationFragmentChanged(bool);
      this.mChildFragmentManager.dispatchPrimaryNavigationFragmentChanged();
    } 
  }
  
  void performResume() {
    this.mChildFragmentManager.noteStateNotSaved();
    this.mChildFragmentManager.execPendingActions(true);
    this.mState = 7;
    this.mCalled = false;
    onResume();
    if (this.mCalled) {
      this.mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME);
      if (this.mView != null)
        this.mViewLifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_RESUME); 
      this.mChildFragmentManager.dispatchResume();
      return;
    } 
    throw new SuperNotCalledException("Fragment " + this + " did not call through to super.onResume()");
  }
  
  void performSaveInstanceState(Bundle paramBundle) {
    onSaveInstanceState(paramBundle);
    this.mSavedStateRegistryController.performSave(paramBundle);
    Parcelable parcelable = this.mChildFragmentManager.saveAllState();
    if (parcelable != null)
      paramBundle.putParcelable("android:support:fragments", parcelable); 
  }
  
  void performStart() {
    this.mChildFragmentManager.noteStateNotSaved();
    this.mChildFragmentManager.execPendingActions(true);
    this.mState = 5;
    this.mCalled = false;
    onStart();
    if (this.mCalled) {
      this.mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START);
      if (this.mView != null)
        this.mViewLifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_START); 
      this.mChildFragmentManager.dispatchStart();
      return;
    } 
    throw new SuperNotCalledException("Fragment " + this + " did not call through to super.onStart()");
  }
  
  void performStop() {
    this.mChildFragmentManager.dispatchStop();
    if (this.mView != null)
      this.mViewLifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_STOP); 
    this.mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP);
    this.mState = 4;
    this.mCalled = false;
    onStop();
    if (this.mCalled)
      return; 
    throw new SuperNotCalledException("Fragment " + this + " did not call through to super.onStop()");
  }
  
  void performViewCreated() {
    onViewCreated(this.mView, this.mSavedFragmentState);
    this.mChildFragmentManager.dispatchViewCreated();
  }
  
  public void postponeEnterTransition() {
    (ensureAnimationInfo()).mEnterTransitionPostponed = true;
  }
  
  public final void postponeEnterTransition(long paramLong, TimeUnit paramTimeUnit) {
    Handler handler;
    (ensureAnimationInfo()).mEnterTransitionPostponed = true;
    FragmentManager fragmentManager = this.mFragmentManager;
    if (fragmentManager != null) {
      handler = fragmentManager.getHost().getHandler();
    } else {
      handler = new Handler(Looper.getMainLooper());
    } 
    handler.removeCallbacks(this.mPostponedDurationRunnable);
    handler.postDelayed(this.mPostponedDurationRunnable, paramTimeUnit.toMillis(paramLong));
  }
  
  public final <I, O> ActivityResultLauncher<I> registerForActivityResult(ActivityResultContract<I, O> paramActivityResultContract, ActivityResultCallback<O> paramActivityResultCallback) {
    return prepareCallInternal(paramActivityResultContract, new Function<Void, ActivityResultRegistry>() {
          final Fragment this$0;
          
          public ActivityResultRegistry apply(Void param1Void) {
            return (Fragment.this.mHost instanceof ActivityResultRegistryOwner) ? ((ActivityResultRegistryOwner)Fragment.this.mHost).getActivityResultRegistry() : Fragment.this.requireActivity().getActivityResultRegistry();
          }
        },  paramActivityResultCallback);
  }
  
  public final <I, O> ActivityResultLauncher<I> registerForActivityResult(ActivityResultContract<I, O> paramActivityResultContract, final ActivityResultRegistry registry, ActivityResultCallback<O> paramActivityResultCallback) {
    return prepareCallInternal(paramActivityResultContract, new Function<Void, ActivityResultRegistry>() {
          final Fragment this$0;
          
          final ActivityResultRegistry val$registry;
          
          public ActivityResultRegistry apply(Void param1Void) {
            return registry;
          }
        },  paramActivityResultCallback);
  }
  
  public void registerForContextMenu(View paramView) {
    paramView.setOnCreateContextMenuListener(this);
  }
  
  @Deprecated
  public final void requestPermissions(String[] paramArrayOfString, int paramInt) {
    if (this.mHost != null) {
      getParentFragmentManager().launchRequestPermissions(this, paramArrayOfString, paramInt);
      return;
    } 
    throw new IllegalStateException("Fragment " + this + " not attached to Activity");
  }
  
  public final FragmentActivity requireActivity() {
    FragmentActivity fragmentActivity = getActivity();
    if (fragmentActivity != null)
      return fragmentActivity; 
    throw new IllegalStateException("Fragment " + this + " not attached to an activity.");
  }
  
  public final Bundle requireArguments() {
    Bundle bundle = getArguments();
    if (bundle != null)
      return bundle; 
    throw new IllegalStateException("Fragment " + this + " does not have any arguments.");
  }
  
  public final Context requireContext() {
    Context context = getContext();
    if (context != null)
      return context; 
    throw new IllegalStateException("Fragment " + this + " not attached to a context.");
  }
  
  @Deprecated
  public final FragmentManager requireFragmentManager() {
    return getParentFragmentManager();
  }
  
  public final Object requireHost() {
    Object object = getHost();
    if (object != null)
      return object; 
    throw new IllegalStateException("Fragment " + this + " not attached to a host.");
  }
  
  public final Fragment requireParentFragment() {
    Fragment fragment = getParentFragment();
    if (fragment == null) {
      if (getContext() == null)
        throw new IllegalStateException("Fragment " + this + " is not attached to any Fragment or host"); 
      throw new IllegalStateException("Fragment " + this + " is not a child Fragment, it is directly attached to " + getContext());
    } 
    return fragment;
  }
  
  public final View requireView() {
    View view = getView();
    if (view != null)
      return view; 
    throw new IllegalStateException("Fragment " + this + " did not return a View from onCreateView() or this was called before onCreateView().");
  }
  
  void restoreChildFragmentState(Bundle paramBundle) {
    if (paramBundle != null) {
      Parcelable parcelable = paramBundle.getParcelable("android:support:fragments");
      if (parcelable != null) {
        this.mChildFragmentManager.restoreSaveState(parcelable);
        this.mChildFragmentManager.dispatchCreate();
      } 
    } 
  }
  
  final void restoreViewState(Bundle paramBundle) {
    SparseArray<Parcelable> sparseArray = this.mSavedViewState;
    if (sparseArray != null) {
      this.mView.restoreHierarchyState(sparseArray);
      this.mSavedViewState = null;
    } 
    if (this.mView != null) {
      this.mViewLifecycleOwner.performRestore(this.mSavedViewRegistryState);
      this.mSavedViewRegistryState = null;
    } 
    this.mCalled = false;
    onViewStateRestored(paramBundle);
    if (this.mCalled) {
      if (this.mView != null)
        this.mViewLifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_CREATE); 
      return;
    } 
    throw new SuperNotCalledException("Fragment " + this + " did not call through to super.onViewStateRestored()");
  }
  
  public void setAllowEnterTransitionOverlap(boolean paramBoolean) {
    (ensureAnimationInfo()).mAllowEnterTransitionOverlap = Boolean.valueOf(paramBoolean);
  }
  
  public void setAllowReturnTransitionOverlap(boolean paramBoolean) {
    (ensureAnimationInfo()).mAllowReturnTransitionOverlap = Boolean.valueOf(paramBoolean);
  }
  
  void setAnimatingAway(View paramView) {
    (ensureAnimationInfo()).mAnimatingAway = paramView;
  }
  
  void setAnimations(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    if (this.mAnimationInfo == null && paramInt1 == 0 && paramInt2 == 0 && paramInt3 == 0 && paramInt4 == 0)
      return; 
    (ensureAnimationInfo()).mEnterAnim = paramInt1;
    (ensureAnimationInfo()).mExitAnim = paramInt2;
    (ensureAnimationInfo()).mPopEnterAnim = paramInt3;
    (ensureAnimationInfo()).mPopExitAnim = paramInt4;
  }
  
  void setAnimator(Animator paramAnimator) {
    (ensureAnimationInfo()).mAnimator = paramAnimator;
  }
  
  public void setArguments(Bundle paramBundle) {
    if (this.mFragmentManager == null || !isStateSaved()) {
      this.mArguments = paramBundle;
      return;
    } 
    throw new IllegalStateException("Fragment already added and state has been saved");
  }
  
  public void setEnterSharedElementCallback(SharedElementCallback paramSharedElementCallback) {
    (ensureAnimationInfo()).mEnterTransitionCallback = paramSharedElementCallback;
  }
  
  public void setEnterTransition(Object paramObject) {
    (ensureAnimationInfo()).mEnterTransition = paramObject;
  }
  
  public void setExitSharedElementCallback(SharedElementCallback paramSharedElementCallback) {
    (ensureAnimationInfo()).mExitTransitionCallback = paramSharedElementCallback;
  }
  
  public void setExitTransition(Object paramObject) {
    (ensureAnimationInfo()).mExitTransition = paramObject;
  }
  
  void setFocusedView(View paramView) {
    (ensureAnimationInfo()).mFocusedView = paramView;
  }
  
  public void setHasOptionsMenu(boolean paramBoolean) {
    if (this.mHasMenu != paramBoolean) {
      this.mHasMenu = paramBoolean;
      if (isAdded() && !isHidden())
        this.mHost.onSupportInvalidateOptionsMenu(); 
    } 
  }
  
  void setHideReplaced(boolean paramBoolean) {
    (ensureAnimationInfo()).mIsHideReplaced = paramBoolean;
  }
  
  public void setInitialSavedState(SavedState paramSavedState) {
    if (this.mFragmentManager == null) {
      if (paramSavedState != null && paramSavedState.mState != null) {
        Bundle bundle = paramSavedState.mState;
      } else {
        paramSavedState = null;
      } 
      this.mSavedFragmentState = (Bundle)paramSavedState;
      return;
    } 
    throw new IllegalStateException("Fragment already added");
  }
  
  public void setMenuVisibility(boolean paramBoolean) {
    if (this.mMenuVisible != paramBoolean) {
      this.mMenuVisible = paramBoolean;
      if (this.mHasMenu && isAdded() && !isHidden())
        this.mHost.onSupportInvalidateOptionsMenu(); 
    } 
  }
  
  void setNextTransition(int paramInt) {
    if (this.mAnimationInfo == null && paramInt == 0)
      return; 
    ensureAnimationInfo();
    this.mAnimationInfo.mNextTransition = paramInt;
  }
  
  void setOnStartEnterTransitionListener(OnStartEnterTransitionListener paramOnStartEnterTransitionListener) {
    ensureAnimationInfo();
    if (paramOnStartEnterTransitionListener == this.mAnimationInfo.mStartEnterTransitionListener)
      return; 
    if (paramOnStartEnterTransitionListener == null || this.mAnimationInfo.mStartEnterTransitionListener == null) {
      if (this.mAnimationInfo.mEnterTransitionPostponed)
        this.mAnimationInfo.mStartEnterTransitionListener = paramOnStartEnterTransitionListener; 
      if (paramOnStartEnterTransitionListener != null)
        paramOnStartEnterTransitionListener.startListening(); 
      return;
    } 
    throw new IllegalStateException("Trying to set a replacement startPostponedEnterTransition on " + this);
  }
  
  void setPopDirection(boolean paramBoolean) {
    if (this.mAnimationInfo == null)
      return; 
    (ensureAnimationInfo()).mIsPop = paramBoolean;
  }
  
  void setPostOnViewCreatedAlpha(float paramFloat) {
    (ensureAnimationInfo()).mPostOnViewCreatedAlpha = paramFloat;
  }
  
  public void setReenterTransition(Object paramObject) {
    (ensureAnimationInfo()).mReenterTransition = paramObject;
  }
  
  @Deprecated
  public void setRetainInstance(boolean paramBoolean) {
    this.mRetainInstance = paramBoolean;
    FragmentManager fragmentManager = this.mFragmentManager;
    if (fragmentManager != null) {
      if (paramBoolean) {
        fragmentManager.addRetainedFragment(this);
      } else {
        fragmentManager.removeRetainedFragment(this);
      } 
    } else {
      this.mRetainInstanceChangedWhileDetached = true;
    } 
  }
  
  public void setReturnTransition(Object paramObject) {
    (ensureAnimationInfo()).mReturnTransition = paramObject;
  }
  
  public void setSharedElementEnterTransition(Object paramObject) {
    (ensureAnimationInfo()).mSharedElementEnterTransition = paramObject;
  }
  
  void setSharedElementNames(ArrayList<String> paramArrayList1, ArrayList<String> paramArrayList2) {
    ensureAnimationInfo();
    this.mAnimationInfo.mSharedElementSourceNames = paramArrayList1;
    this.mAnimationInfo.mSharedElementTargetNames = paramArrayList2;
  }
  
  public void setSharedElementReturnTransition(Object paramObject) {
    (ensureAnimationInfo()).mSharedElementReturnTransition = paramObject;
  }
  
  @Deprecated
  public void setTargetFragment(Fragment paramFragment, int paramInt) {
    FragmentManager fragmentManager1;
    FragmentManager fragmentManager2 = this.mFragmentManager;
    if (paramFragment != null) {
      fragmentManager1 = paramFragment.mFragmentManager;
    } else {
      fragmentManager1 = null;
    } 
    if (fragmentManager2 == null || fragmentManager1 == null || fragmentManager2 == fragmentManager1) {
      Fragment fragment = paramFragment;
      while (fragment != null) {
        if (!fragment.equals(this)) {
          fragment = fragment.getTargetFragment();
          continue;
        } 
        throw new IllegalArgumentException("Setting " + paramFragment + " as the target of " + this + " would create a target cycle");
      } 
      if (paramFragment == null) {
        this.mTargetWho = null;
        this.mTarget = null;
      } else if (this.mFragmentManager != null && paramFragment.mFragmentManager != null) {
        this.mTargetWho = paramFragment.mWho;
        this.mTarget = null;
      } else {
        this.mTargetWho = null;
        this.mTarget = paramFragment;
      } 
      this.mTargetRequestCode = paramInt;
      return;
    } 
    throw new IllegalArgumentException("Fragment " + paramFragment + " must share the same FragmentManager to be set as a target fragment");
  }
  
  @Deprecated
  public void setUserVisibleHint(boolean paramBoolean) {
    boolean bool;
    if (!this.mUserVisibleHint && paramBoolean && this.mState < 5 && this.mFragmentManager != null && isAdded() && this.mIsCreated) {
      FragmentManager fragmentManager = this.mFragmentManager;
      fragmentManager.performPendingDeferredStart(fragmentManager.createOrGetFragmentStateManager(this));
    } 
    this.mUserVisibleHint = paramBoolean;
    if (this.mState < 5 && !paramBoolean) {
      bool = true;
    } else {
      bool = false;
    } 
    this.mDeferStart = bool;
    if (this.mSavedFragmentState != null)
      this.mSavedUserVisibleHint = Boolean.valueOf(paramBoolean); 
  }
  
  public boolean shouldShowRequestPermissionRationale(String paramString) {
    FragmentHostCallback<?> fragmentHostCallback = this.mHost;
    return (fragmentHostCallback != null) ? fragmentHostCallback.onShouldShowRequestPermissionRationale(paramString) : false;
  }
  
  public void startActivity(Intent paramIntent) {
    startActivity(paramIntent, null);
  }
  
  public void startActivity(Intent paramIntent, Bundle paramBundle) {
    FragmentHostCallback<?> fragmentHostCallback = this.mHost;
    if (fragmentHostCallback != null) {
      fragmentHostCallback.onStartActivityFromFragment(this, paramIntent, -1, paramBundle);
      return;
    } 
    throw new IllegalStateException("Fragment " + this + " not attached to Activity");
  }
  
  @Deprecated
  public void startActivityForResult(Intent paramIntent, int paramInt) {
    startActivityForResult(paramIntent, paramInt, null);
  }
  
  @Deprecated
  public void startActivityForResult(Intent paramIntent, int paramInt, Bundle paramBundle) {
    if (this.mHost != null) {
      getParentFragmentManager().launchStartActivityForResult(this, paramIntent, paramInt, paramBundle);
      return;
    } 
    throw new IllegalStateException("Fragment " + this + " not attached to Activity");
  }
  
  @Deprecated
  public void startIntentSenderForResult(IntentSender paramIntentSender, int paramInt1, Intent paramIntent, int paramInt2, int paramInt3, int paramInt4, Bundle paramBundle) throws IntentSender.SendIntentException {
    if (this.mHost != null) {
      if (FragmentManager.isLoggingEnabled(2))
        Log.v("FragmentManager", "Fragment " + this + " received the following in startIntentSenderForResult() requestCode: " + paramInt1 + " IntentSender: " + paramIntentSender + " fillInIntent: " + paramIntent + " options: " + paramBundle); 
      getParentFragmentManager().launchStartIntentSenderForResult(this, paramIntentSender, paramInt1, paramIntent, paramInt2, paramInt3, paramInt4, paramBundle);
      return;
    } 
    throw new IllegalStateException("Fragment " + this + " not attached to Activity");
  }
  
  public void startPostponedEnterTransition() {
    if (this.mAnimationInfo == null || !(ensureAnimationInfo()).mEnterTransitionPostponed)
      return; 
    if (this.mHost == null) {
      (ensureAnimationInfo()).mEnterTransitionPostponed = false;
    } else if (Looper.myLooper() != this.mHost.getHandler().getLooper()) {
      this.mHost.getHandler().postAtFrontOfQueue(new Runnable() {
            final Fragment this$0;
            
            public void run() {
              Fragment.this.callStartTransitionListener(false);
            }
          });
    } else {
      callStartTransitionListener(true);
    } 
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder(128);
    stringBuilder.append(getClass().getSimpleName());
    stringBuilder.append("{");
    stringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
    stringBuilder.append("}");
    stringBuilder.append(" (");
    stringBuilder.append(this.mWho);
    if (this.mFragmentId != 0) {
      stringBuilder.append(" id=0x");
      stringBuilder.append(Integer.toHexString(this.mFragmentId));
    } 
    if (this.mTag != null) {
      stringBuilder.append(" tag=");
      stringBuilder.append(this.mTag);
    } 
    stringBuilder.append(")");
    return stringBuilder.toString();
  }
  
  public void unregisterForContextMenu(View paramView) {
    paramView.setOnCreateContextMenuListener(null);
  }
  
  static class AnimationInfo {
    Boolean mAllowEnterTransitionOverlap;
    
    Boolean mAllowReturnTransitionOverlap;
    
    View mAnimatingAway;
    
    Animator mAnimator;
    
    int mEnterAnim;
    
    Object mEnterTransition = null;
    
    SharedElementCallback mEnterTransitionCallback = null;
    
    boolean mEnterTransitionPostponed;
    
    int mExitAnim;
    
    Object mExitTransition = null;
    
    SharedElementCallback mExitTransitionCallback = null;
    
    View mFocusedView = null;
    
    boolean mIsHideReplaced;
    
    boolean mIsPop;
    
    int mNextTransition;
    
    int mPopEnterAnim;
    
    int mPopExitAnim;
    
    float mPostOnViewCreatedAlpha = 1.0F;
    
    Object mReenterTransition = Fragment.USE_DEFAULT_TRANSITION;
    
    Object mReturnTransition = Fragment.USE_DEFAULT_TRANSITION;
    
    Object mSharedElementEnterTransition = null;
    
    Object mSharedElementReturnTransition = Fragment.USE_DEFAULT_TRANSITION;
    
    ArrayList<String> mSharedElementSourceNames;
    
    ArrayList<String> mSharedElementTargetNames;
    
    Fragment.OnStartEnterTransitionListener mStartEnterTransitionListener;
  }
  
  public static class InstantiationException extends RuntimeException {
    public InstantiationException(String param1String, Exception param1Exception) {
      super(param1String, param1Exception);
    }
  }
  
  private static abstract class OnPreAttachedListener {
    private OnPreAttachedListener() {}
    
    abstract void onPreAttached();
  }
  
  static interface OnStartEnterTransitionListener {
    void onStartEnterTransition();
    
    void startListening();
  }
  
  public static class SavedState implements Parcelable {
    public static final Parcelable.Creator<SavedState> CREATOR = (Parcelable.Creator<SavedState>)new Parcelable.ClassLoaderCreator<SavedState>() {
        public Fragment.SavedState createFromParcel(Parcel param2Parcel) {
          return new Fragment.SavedState(param2Parcel, null);
        }
        
        public Fragment.SavedState createFromParcel(Parcel param2Parcel, ClassLoader param2ClassLoader) {
          return new Fragment.SavedState(param2Parcel, param2ClassLoader);
        }
        
        public Fragment.SavedState[] newArray(int param2Int) {
          return new Fragment.SavedState[param2Int];
        }
      };
    
    final Bundle mState;
    
    SavedState(Bundle param1Bundle) {
      this.mState = param1Bundle;
    }
    
    SavedState(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      Bundle bundle = param1Parcel.readBundle();
      this.mState = bundle;
      if (param1ClassLoader != null && bundle != null)
        bundle.setClassLoader(param1ClassLoader); 
    }
    
    public int describeContents() {
      return 0;
    }
    
    public void writeToParcel(Parcel param1Parcel, int param1Int) {
      param1Parcel.writeBundle(this.mState);
    }
  }
  
  class null implements Parcelable.ClassLoaderCreator<SavedState> {
    public Fragment.SavedState createFromParcel(Parcel param1Parcel) {
      return new Fragment.SavedState(param1Parcel, null);
    }
    
    public Fragment.SavedState createFromParcel(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      return new Fragment.SavedState(param1Parcel, param1ClassLoader);
    }
    
    public Fragment.SavedState[] newArray(int param1Int) {
      return new Fragment.SavedState[param1Int];
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\fragment\app\Fragment.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */