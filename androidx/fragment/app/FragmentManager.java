package androidx.fragment.app;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentSender;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.activity.OnBackPressedDispatcherOwner;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.ActivityResultRegistry;
import androidx.activity.result.ActivityResultRegistryOwner;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.collection.ArraySet;
import androidx.core.os.CancellationSignal;
import androidx.fragment.R;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class FragmentManager implements FragmentResultOwner {
  private static boolean DEBUG = false;
  
  private static final String EXTRA_CREATED_FILLIN_INTENT = "androidx.fragment.extra.ACTIVITY_OPTIONS_BUNDLE";
  
  public static final int POP_BACK_STACK_INCLUSIVE = 1;
  
  static final String TAG = "FragmentManager";
  
  static boolean USE_STATE_MANAGER = true;
  
  ArrayList<BackStackRecord> mBackStack;
  
  private ArrayList<OnBackStackChangedListener> mBackStackChangeListeners;
  
  private final AtomicInteger mBackStackIndex = new AtomicInteger();
  
  private FragmentContainer mContainer;
  
  private ArrayList<Fragment> mCreatedMenus;
  
  int mCurState = -1;
  
  private SpecialEffectsControllerFactory mDefaultSpecialEffectsControllerFactory = new SpecialEffectsControllerFactory() {
      final FragmentManager this$0;
      
      public SpecialEffectsController createController(ViewGroup param1ViewGroup) {
        return new DefaultSpecialEffectsController(param1ViewGroup);
      }
    };
  
  private boolean mDestroyed;
  
  private Runnable mExecCommit = new Runnable() {
      final FragmentManager this$0;
      
      public void run() {
        FragmentManager.this.execPendingActions(true);
      }
    };
  
  private boolean mExecutingActions;
  
  private Map<Fragment, HashSet<CancellationSignal>> mExitAnimationCancellationSignals = Collections.synchronizedMap(new HashMap<>());
  
  private FragmentFactory mFragmentFactory = null;
  
  private final FragmentStore mFragmentStore = new FragmentStore();
  
  private final FragmentTransition.Callback mFragmentTransitionCallback = new FragmentTransition.Callback() {
      final FragmentManager this$0;
      
      public void onComplete(Fragment param1Fragment, CancellationSignal param1CancellationSignal) {
        if (!param1CancellationSignal.isCanceled())
          FragmentManager.this.removeCancellationSignal(param1Fragment, param1CancellationSignal); 
      }
      
      public void onStart(Fragment param1Fragment, CancellationSignal param1CancellationSignal) {
        FragmentManager.this.addCancellationSignal(param1Fragment, param1CancellationSignal);
      }
    };
  
  private boolean mHavePendingDeferredStart;
  
  private FragmentHostCallback<?> mHost;
  
  private FragmentFactory mHostFragmentFactory = new FragmentFactory() {
      final FragmentManager this$0;
      
      public Fragment instantiate(ClassLoader param1ClassLoader, String param1String) {
        return FragmentManager.this.getHost().instantiate(FragmentManager.this.getHost().getContext(), param1String, null);
      }
    };
  
  ArrayDeque<LaunchedFragmentInfo> mLaunchedFragments = new ArrayDeque<>();
  
  private final FragmentLayoutInflaterFactory mLayoutInflaterFactory = new FragmentLayoutInflaterFactory(this);
  
  private final FragmentLifecycleCallbacksDispatcher mLifecycleCallbacksDispatcher = new FragmentLifecycleCallbacksDispatcher(this);
  
  private boolean mNeedMenuInvalidate;
  
  private FragmentManagerViewModel mNonConfig;
  
  private final CopyOnWriteArrayList<FragmentOnAttachListener> mOnAttachListeners = new CopyOnWriteArrayList<>();
  
  private final OnBackPressedCallback mOnBackPressedCallback = new OnBackPressedCallback(false) {
      final FragmentManager this$0;
      
      public void handleOnBackPressed() {
        FragmentManager.this.handleOnBackPressed();
      }
    };
  
  private OnBackPressedDispatcher mOnBackPressedDispatcher;
  
  private Fragment mParent;
  
  private final ArrayList<OpGenerator> mPendingActions = new ArrayList<>();
  
  private ArrayList<StartEnterTransitionListener> mPostponedTransactions;
  
  Fragment mPrimaryNav;
  
  private ActivityResultLauncher<String[]> mRequestPermissions;
  
  private final Map<String, LifecycleAwareResultListener> mResultListeners = Collections.synchronizedMap(new HashMap<>());
  
  private final Map<String, Bundle> mResults = Collections.synchronizedMap(new HashMap<>());
  
  private SpecialEffectsControllerFactory mSpecialEffectsControllerFactory = null;
  
  private ActivityResultLauncher<Intent> mStartActivityForResult;
  
  private ActivityResultLauncher<IntentSenderRequest> mStartIntentSenderForResult;
  
  private boolean mStateSaved;
  
  private boolean mStopped;
  
  private ArrayList<Fragment> mTmpAddedFragments;
  
  private ArrayList<Boolean> mTmpIsPop;
  
  private ArrayList<BackStackRecord> mTmpRecords;
  
  private void addAddedFragments(ArraySet<Fragment> paramArraySet) {
    int i = this.mCurState;
    if (i < 1)
      return; 
    i = Math.min(i, 5);
    for (Fragment fragment : this.mFragmentStore.getFragments()) {
      if (fragment.mState < i) {
        moveToState(fragment, i);
        if (fragment.mView != null && !fragment.mHidden && fragment.mIsNewlyAdded)
          paramArraySet.add(fragment); 
      } 
    } 
  }
  
  private void cancelExitAnimation(Fragment paramFragment) {
    HashSet hashSet = this.mExitAnimationCancellationSignals.get(paramFragment);
    if (hashSet != null) {
      Iterator<CancellationSignal> iterator = hashSet.iterator();
      while (iterator.hasNext())
        ((CancellationSignal)iterator.next()).cancel(); 
      hashSet.clear();
      destroyFragmentView(paramFragment);
      this.mExitAnimationCancellationSignals.remove(paramFragment);
    } 
  }
  
  private void checkStateLoss() {
    if (!isStateSaved())
      return; 
    throw new IllegalStateException("Can not perform this action after onSaveInstanceState");
  }
  
  private void cleanupExec() {
    this.mExecutingActions = false;
    this.mTmpIsPop.clear();
    this.mTmpRecords.clear();
  }
  
  private Set<SpecialEffectsController> collectAllSpecialEffectsController() {
    HashSet<SpecialEffectsController> hashSet = new HashSet();
    Iterator<FragmentStateManager> iterator = this.mFragmentStore.getActiveFragmentStateManagers().iterator();
    while (iterator.hasNext()) {
      ViewGroup viewGroup = (((FragmentStateManager)iterator.next()).getFragment()).mContainer;
      if (viewGroup != null)
        hashSet.add(SpecialEffectsController.getOrCreateController(viewGroup, getSpecialEffectsControllerFactory())); 
    } 
    return hashSet;
  }
  
  private Set<SpecialEffectsController> collectChangedControllers(ArrayList<BackStackRecord> paramArrayList, int paramInt1, int paramInt2) {
    HashSet<SpecialEffectsController> hashSet = new HashSet();
    while (paramInt1 < paramInt2) {
      Iterator<FragmentTransaction.Op> iterator = ((BackStackRecord)paramArrayList.get(paramInt1)).mOps.iterator();
      while (iterator.hasNext()) {
        Fragment fragment = ((FragmentTransaction.Op)iterator.next()).mFragment;
        if (fragment != null) {
          ViewGroup viewGroup = fragment.mContainer;
          if (viewGroup != null)
            hashSet.add(SpecialEffectsController.getOrCreateController(viewGroup, this)); 
        } 
      } 
      paramInt1++;
    } 
    return hashSet;
  }
  
  private void completeShowHideFragment(final Fragment fragment) {
    if (fragment.mView != null) {
      FragmentAnim.AnimationOrAnimator animationOrAnimator = FragmentAnim.loadAnimation(this.mHost.getContext(), fragment, fragment.mHidden ^ true, fragment.getPopDirection());
      if (animationOrAnimator != null && animationOrAnimator.animator != null) {
        animationOrAnimator.animator.setTarget(fragment.mView);
        if (fragment.mHidden) {
          if (fragment.isHideReplaced()) {
            fragment.setHideReplaced(false);
          } else {
            final ViewGroup container = fragment.mContainer;
            final View animatingView = fragment.mView;
            viewGroup.startViewTransition(view);
            animationOrAnimator.animator.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter() {
                  final FragmentManager this$0;
                  
                  final View val$animatingView;
                  
                  final ViewGroup val$container;
                  
                  final Fragment val$fragment;
                  
                  public void onAnimationEnd(Animator param1Animator) {
                    container.endViewTransition(animatingView);
                    param1Animator.removeListener((Animator.AnimatorListener)this);
                    if (fragment.mView != null && fragment.mHidden)
                      fragment.mView.setVisibility(8); 
                  }
                });
          } 
        } else {
          fragment.mView.setVisibility(0);
        } 
        animationOrAnimator.animator.start();
      } else {
        boolean bool;
        if (animationOrAnimator != null) {
          fragment.mView.startAnimation(animationOrAnimator.animation);
          animationOrAnimator.animation.start();
        } 
        if (fragment.mHidden && !fragment.isHideReplaced()) {
          bool = true;
        } else {
          bool = false;
        } 
        fragment.mView.setVisibility(bool);
        if (fragment.isHideReplaced())
          fragment.setHideReplaced(false); 
      } 
    } 
    invalidateMenuForFragment(fragment);
    fragment.mHiddenChanged = false;
    fragment.onHiddenChanged(fragment.mHidden);
  }
  
  private void destroyFragmentView(Fragment paramFragment) {
    paramFragment.performDestroyView();
    this.mLifecycleCallbacksDispatcher.dispatchOnFragmentViewDestroyed(paramFragment, false);
    paramFragment.mContainer = null;
    paramFragment.mView = null;
    paramFragment.mViewLifecycleOwner = null;
    paramFragment.mViewLifecycleOwnerLiveData.setValue(null);
    paramFragment.mInLayout = false;
  }
  
  private void dispatchParentPrimaryNavigationFragmentChanged(Fragment paramFragment) {
    if (paramFragment != null && paramFragment.equals(findActiveFragment(paramFragment.mWho)))
      paramFragment.performPrimaryNavigationFragmentChanged(); 
  }
  
  private void dispatchStateChange(int paramInt) {
    try {
      this.mExecutingActions = true;
      this.mFragmentStore.dispatchStateChange(paramInt);
      moveToState(paramInt, false);
      if (USE_STATE_MANAGER) {
        Iterator<SpecialEffectsController> iterator = collectAllSpecialEffectsController().iterator();
        while (iterator.hasNext())
          ((SpecialEffectsController)iterator.next()).forceCompleteAllOperations(); 
      } 
      this.mExecutingActions = false;
      return;
    } finally {
      this.mExecutingActions = false;
    } 
  }
  
  private void doPendingDeferredStart() {
    if (this.mHavePendingDeferredStart) {
      this.mHavePendingDeferredStart = false;
      startPendingDeferredFragments();
    } 
  }
  
  @Deprecated
  public static void enableDebugLogging(boolean paramBoolean) {
    DEBUG = paramBoolean;
  }
  
  public static void enableNewStateManager(boolean paramBoolean) {
    USE_STATE_MANAGER = paramBoolean;
  }
  
  private void endAnimatingAwayFragments() {
    if (USE_STATE_MANAGER) {
      Iterator<SpecialEffectsController> iterator = collectAllSpecialEffectsController().iterator();
      while (iterator.hasNext())
        ((SpecialEffectsController)iterator.next()).forceCompleteAllOperations(); 
    } else if (!this.mExitAnimationCancellationSignals.isEmpty()) {
      for (Fragment fragment : this.mExitAnimationCancellationSignals.keySet()) {
        cancelExitAnimation(fragment);
        moveToState(fragment);
      } 
    } 
  }
  
  private void ensureExecReady(boolean paramBoolean) {
    if (!this.mExecutingActions) {
      if (this.mHost == null) {
        if (this.mDestroyed)
          throw new IllegalStateException("FragmentManager has been destroyed"); 
        throw new IllegalStateException("FragmentManager has not been attached to a host.");
      } 
      if (Looper.myLooper() == this.mHost.getHandler().getLooper()) {
        if (!paramBoolean)
          checkStateLoss(); 
        if (this.mTmpRecords == null) {
          this.mTmpRecords = new ArrayList<>();
          this.mTmpIsPop = new ArrayList<>();
        } 
        this.mExecutingActions = true;
        try {
          executePostponedTransaction(null, null);
          return;
        } finally {
          this.mExecutingActions = false;
        } 
      } 
      throw new IllegalStateException("Must be called from main thread of fragment host");
    } 
    throw new IllegalStateException("FragmentManager is already executing transactions");
  }
  
  private static void executeOps(ArrayList<BackStackRecord> paramArrayList, ArrayList<Boolean> paramArrayList1, int paramInt1, int paramInt2) {
    while (paramInt1 < paramInt2) {
      BackStackRecord backStackRecord = paramArrayList.get(paramInt1);
      boolean bool1 = ((Boolean)paramArrayList1.get(paramInt1)).booleanValue();
      boolean bool = true;
      if (bool1) {
        backStackRecord.bumpBackStackNesting(-1);
        if (paramInt1 != paramInt2 - 1)
          bool = false; 
        backStackRecord.executePopOps(bool);
      } else {
        backStackRecord.bumpBackStackNesting(1);
        backStackRecord.executeOps();
      } 
      paramInt1++;
    } 
  }
  
  private void executeOpsTogether(ArrayList<BackStackRecord> paramArrayList, ArrayList<Boolean> paramArrayList1, int paramInt1, int paramInt2) {
    boolean bool1 = ((BackStackRecord)paramArrayList.get(paramInt1)).mReorderingAllowed;
    ArrayList<Fragment> arrayList = this.mTmpAddedFragments;
    if (arrayList == null) {
      this.mTmpAddedFragments = new ArrayList<>();
    } else {
      arrayList.clear();
    } 
    this.mTmpAddedFragments.addAll(this.mFragmentStore.getFragments());
    Fragment fragment = getPrimaryNavigationFragment();
    int i = paramInt1;
    boolean bool = false;
    while (true) {
      boolean bool2 = true;
      if (i < paramInt2) {
        BackStackRecord backStackRecord = paramArrayList.get(i);
        if (!((Boolean)paramArrayList1.get(i)).booleanValue()) {
          fragment = backStackRecord.expandOps(this.mTmpAddedFragments, fragment);
        } else {
          fragment = backStackRecord.trackAddedFragmentsInPop(this.mTmpAddedFragments, fragment);
        } 
        boolean bool3 = bool2;
        if (!bool)
          if (backStackRecord.mAddToBackStack) {
            bool3 = bool2;
          } else {
            bool3 = false;
          }  
        i++;
        bool = bool3;
        continue;
      } 
      this.mTmpAddedFragments.clear();
      if (!bool1 && this.mCurState >= 1)
        if (USE_STATE_MANAGER) {
          for (int j = paramInt1; j < paramInt2; j++) {
            Iterator<FragmentTransaction.Op> iterator = ((BackStackRecord)paramArrayList.get(j)).mOps.iterator();
            while (iterator.hasNext()) {
              Fragment fragment1 = ((FragmentTransaction.Op)iterator.next()).mFragment;
              if (fragment1 != null && fragment1.mFragmentManager != null) {
                FragmentStateManager fragmentStateManager = createOrGetFragmentStateManager(fragment1);
                this.mFragmentStore.makeActive(fragmentStateManager);
              } 
            } 
          } 
        } else {
          FragmentTransition.startTransitions(this.mHost.getContext(), this.mContainer, paramArrayList, paramArrayList1, paramInt1, paramInt2, false, this.mFragmentTransitionCallback);
        }  
      executeOps(paramArrayList, paramArrayList1, paramInt1, paramInt2);
      if (USE_STATE_MANAGER) {
        bool1 = ((Boolean)paramArrayList1.get(paramInt2 - 1)).booleanValue();
        int j;
        for (j = paramInt1; j < paramInt2; j++) {
          BackStackRecord backStackRecord = paramArrayList.get(j);
          if (bool1) {
            for (i = backStackRecord.mOps.size() - 1; i >= 0; i--) {
              fragment = ((FragmentTransaction.Op)backStackRecord.mOps.get(i)).mFragment;
              if (fragment != null)
                createOrGetFragmentStateManager(fragment).moveToExpectedState(); 
            } 
          } else {
            Iterator<FragmentTransaction.Op> iterator = backStackRecord.mOps.iterator();
            while (iterator.hasNext()) {
              Fragment fragment1 = ((FragmentTransaction.Op)iterator.next()).mFragment;
              if (fragment1 != null)
                createOrGetFragmentStateManager(fragment1).moveToExpectedState(); 
            } 
          } 
        } 
        moveToState(this.mCurState, true);
        for (SpecialEffectsController specialEffectsController : collectChangedControllers(paramArrayList, paramInt1, paramInt2)) {
          specialEffectsController.updateOperationDirection(bool1);
          specialEffectsController.markPostponedState();
          specialEffectsController.executePendingOperations();
        } 
        j = paramInt2;
        ArrayList<Boolean> arrayList1 = paramArrayList1;
      } else {
        int j;
        if (bool1) {
          ArraySet<Fragment> arraySet = new ArraySet();
          addAddedFragments(arraySet);
          j = postponePostponableTransactions(paramArrayList, paramArrayList1, paramInt1, paramInt2, arraySet);
          makeRemovedFragmentsInvisible(arraySet);
        } else {
          j = paramInt2;
        } 
        if (j != paramInt1 && bool1) {
          if (this.mCurState >= 1)
            FragmentTransition.startTransitions(this.mHost.getContext(), this.mContainer, paramArrayList, paramArrayList1, paramInt1, j, true, this.mFragmentTransitionCallback); 
          j = paramInt2;
          ArrayList<Boolean> arrayList1 = paramArrayList1;
          moveToState(this.mCurState, true);
        } else {
          ArrayList<Boolean> arrayList1 = paramArrayList1;
          j = paramInt2;
        } 
      } 
      while (paramInt1 < paramInt2) {
        BackStackRecord backStackRecord = paramArrayList.get(paramInt1);
        if (((Boolean)paramArrayList1.get(paramInt1)).booleanValue() && backStackRecord.mIndex >= 0)
          backStackRecord.mIndex = -1; 
        backStackRecord.runOnCommitRunnables();
        paramInt1++;
      } 
      if (bool)
        reportBackStackChanged(); 
      return;
    } 
  }
  
  private void executePostponedTransaction(ArrayList<BackStackRecord> paramArrayList, ArrayList<Boolean> paramArrayList1) {
    // Byte code:
    //   0: aload_0
    //   1: getfield mPostponedTransactions : Ljava/util/ArrayList;
    //   4: astore #7
    //   6: aload #7
    //   8: ifnonnull -> 16
    //   11: iconst_0
    //   12: istore_3
    //   13: goto -> 22
    //   16: aload #7
    //   18: invokevirtual size : ()I
    //   21: istore_3
    //   22: iconst_0
    //   23: istore #4
    //   25: iload_3
    //   26: istore #6
    //   28: iload #4
    //   30: iload #6
    //   32: if_icmpge -> 260
    //   35: aload_0
    //   36: getfield mPostponedTransactions : Ljava/util/ArrayList;
    //   39: iload #4
    //   41: invokevirtual get : (I)Ljava/lang/Object;
    //   44: checkcast androidx/fragment/app/FragmentManager$StartEnterTransitionListener
    //   47: astore #7
    //   49: aload_1
    //   50: ifnull -> 123
    //   53: aload #7
    //   55: getfield mIsBack : Z
    //   58: ifne -> 123
    //   61: aload_1
    //   62: aload #7
    //   64: getfield mRecord : Landroidx/fragment/app/BackStackRecord;
    //   67: invokevirtual indexOf : (Ljava/lang/Object;)I
    //   70: istore_3
    //   71: iload_3
    //   72: iconst_m1
    //   73: if_icmpeq -> 123
    //   76: aload_2
    //   77: ifnull -> 123
    //   80: aload_2
    //   81: iload_3
    //   82: invokevirtual get : (I)Ljava/lang/Object;
    //   85: checkcast java/lang/Boolean
    //   88: invokevirtual booleanValue : ()Z
    //   91: ifeq -> 123
    //   94: aload_0
    //   95: getfield mPostponedTransactions : Ljava/util/ArrayList;
    //   98: iload #4
    //   100: invokevirtual remove : (I)Ljava/lang/Object;
    //   103: pop
    //   104: iload #4
    //   106: iconst_1
    //   107: isub
    //   108: istore #5
    //   110: iload #6
    //   112: iconst_1
    //   113: isub
    //   114: istore_3
    //   115: aload #7
    //   117: invokevirtual cancelTransaction : ()V
    //   120: goto -> 248
    //   123: aload #7
    //   125: invokevirtual isReady : ()Z
    //   128: ifne -> 166
    //   131: iload #6
    //   133: istore_3
    //   134: iload #4
    //   136: istore #5
    //   138: aload_1
    //   139: ifnull -> 248
    //   142: iload #6
    //   144: istore_3
    //   145: iload #4
    //   147: istore #5
    //   149: aload #7
    //   151: getfield mRecord : Landroidx/fragment/app/BackStackRecord;
    //   154: aload_1
    //   155: iconst_0
    //   156: aload_1
    //   157: invokevirtual size : ()I
    //   160: invokevirtual interactsWith : (Ljava/util/ArrayList;II)Z
    //   163: ifeq -> 248
    //   166: aload_0
    //   167: getfield mPostponedTransactions : Ljava/util/ArrayList;
    //   170: iload #4
    //   172: invokevirtual remove : (I)Ljava/lang/Object;
    //   175: pop
    //   176: iload #4
    //   178: iconst_1
    //   179: isub
    //   180: istore #5
    //   182: iload #6
    //   184: iconst_1
    //   185: isub
    //   186: istore_3
    //   187: aload_1
    //   188: ifnull -> 243
    //   191: aload #7
    //   193: getfield mIsBack : Z
    //   196: ifne -> 243
    //   199: aload_1
    //   200: aload #7
    //   202: getfield mRecord : Landroidx/fragment/app/BackStackRecord;
    //   205: invokevirtual indexOf : (Ljava/lang/Object;)I
    //   208: istore #4
    //   210: iload #4
    //   212: iconst_m1
    //   213: if_icmpeq -> 243
    //   216: aload_2
    //   217: ifnull -> 243
    //   220: aload_2
    //   221: iload #4
    //   223: invokevirtual get : (I)Ljava/lang/Object;
    //   226: checkcast java/lang/Boolean
    //   229: invokevirtual booleanValue : ()Z
    //   232: ifeq -> 243
    //   235: aload #7
    //   237: invokevirtual cancelTransaction : ()V
    //   240: goto -> 248
    //   243: aload #7
    //   245: invokevirtual completeTransaction : ()V
    //   248: iload #5
    //   250: iconst_1
    //   251: iadd
    //   252: istore #4
    //   254: iload_3
    //   255: istore #6
    //   257: goto -> 28
    //   260: return
  }
  
  public static <F extends Fragment> F findFragment(View paramView) {
    Fragment fragment = findViewFragment(paramView);
    if (fragment != null)
      return (F)fragment; 
    throw new IllegalStateException("View " + paramView + " does not have a Fragment set");
  }
  
  static FragmentManager findFragmentManager(View paramView) {
    FragmentManager fragmentManager;
    Fragment fragment = findViewFragment(paramView);
    if (fragment != null) {
      if (fragment.isAdded()) {
        fragmentManager = fragment.getChildFragmentManager();
      } else {
        throw new IllegalStateException("The Fragment " + fragment + " that owns View " + fragmentManager + " has already been destroyed. Nested fragments should always use the child FragmentManager.");
      } 
    } else {
      FragmentActivity fragmentActivity1;
      Context context = fragmentManager.getContext();
      FragmentActivity fragmentActivity2 = null;
      while (true) {
        fragmentActivity1 = fragmentActivity2;
        if (context instanceof ContextWrapper) {
          if (context instanceof FragmentActivity) {
            fragmentActivity1 = (FragmentActivity)context;
            break;
          } 
          context = ((ContextWrapper)context).getBaseContext();
          continue;
        } 
        break;
      } 
      if (fragmentActivity1 != null)
        return fragmentActivity1.getSupportFragmentManager(); 
      throw new IllegalStateException("View " + fragmentManager + " is not within a subclass of FragmentActivity.");
    } 
    return fragmentManager;
  }
  
  private static Fragment findViewFragment(View paramView) {
    while (true) {
      View view = null;
      if (paramView != null) {
        Fragment fragment = getViewFragment(paramView);
        if (fragment != null)
          return fragment; 
        ViewParent viewParent = paramView.getParent();
        paramView = view;
        if (viewParent instanceof View)
          paramView = (View)viewParent; 
        continue;
      } 
      return null;
    } 
  }
  
  private void forcePostponedTransactions() {
    if (USE_STATE_MANAGER) {
      Iterator<SpecialEffectsController> iterator = collectAllSpecialEffectsController().iterator();
      while (iterator.hasNext())
        ((SpecialEffectsController)iterator.next()).forcePostponedExecutePendingOperations(); 
    } else if (this.mPostponedTransactions != null) {
      while (!this.mPostponedTransactions.isEmpty())
        ((StartEnterTransitionListener)this.mPostponedTransactions.remove(0)).completeTransaction(); 
    } 
  }
  
  private boolean generateOpsForPendingActions(ArrayList<BackStackRecord> paramArrayList, ArrayList<Boolean> paramArrayList1) {
    boolean bool = false;
    synchronized (this.mPendingActions) {
      if (this.mPendingActions.isEmpty())
        return false; 
      int i = this.mPendingActions.size();
      for (byte b = 0; b < i; b++)
        bool |= ((OpGenerator)this.mPendingActions.get(b)).generateOps(paramArrayList, paramArrayList1); 
      this.mPendingActions.clear();
      this.mHost.getHandler().removeCallbacks(this.mExecCommit);
      return bool;
    } 
  }
  
  private FragmentManagerViewModel getChildNonConfig(Fragment paramFragment) {
    return this.mNonConfig.getChildNonConfig(paramFragment);
  }
  
  private ViewGroup getFragmentContainer(Fragment paramFragment) {
    if (paramFragment.mContainer != null)
      return paramFragment.mContainer; 
    if (paramFragment.mContainerId <= 0)
      return null; 
    if (this.mContainer.onHasView()) {
      View view = this.mContainer.onFindViewById(paramFragment.mContainerId);
      if (view instanceof ViewGroup)
        return (ViewGroup)view; 
    } 
    return null;
  }
  
  static Fragment getViewFragment(View paramView) {
    Object object = paramView.getTag(R.id.fragment_container_view_tag);
    return (object instanceof Fragment) ? (Fragment)object : null;
  }
  
  static boolean isLoggingEnabled(int paramInt) {
    return (DEBUG || Log.isLoggable("FragmentManager", paramInt));
  }
  
  private boolean isMenuAvailable(Fragment paramFragment) {
    boolean bool;
    if ((paramFragment.mHasMenu && paramFragment.mMenuVisible) || paramFragment.mChildFragmentManager.checkForMenus()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private void makeRemovedFragmentsInvisible(ArraySet<Fragment> paramArraySet) {
    int i = paramArraySet.size();
    for (byte b = 0; b < i; b++) {
      Fragment fragment = (Fragment)paramArraySet.valueAt(b);
      if (!fragment.mAdded) {
        View view = fragment.requireView();
        fragment.mPostponedAlpha = view.getAlpha();
        view.setAlpha(0.0F);
      } 
    } 
  }
  
  private boolean popBackStackImmediate(String paramString, int paramInt1, int paramInt2) {
    execPendingActions(false);
    ensureExecReady(true);
    Fragment fragment = this.mPrimaryNav;
    if (fragment != null && paramInt1 < 0 && paramString == null && fragment.getChildFragmentManager().popBackStackImmediate())
      return true; 
    boolean bool = popBackStackState(this.mTmpRecords, this.mTmpIsPop, paramString, paramInt1, paramInt2);
    if (bool) {
      this.mExecutingActions = true;
      try {
        removeRedundantOperationsAndExecute(this.mTmpRecords, this.mTmpIsPop);
      } finally {
        cleanupExec();
      } 
    } 
    updateOnBackPressedCallbackEnabled();
    doPendingDeferredStart();
    this.mFragmentStore.burpActive();
    return bool;
  }
  
  private int postponePostponableTransactions(ArrayList<BackStackRecord> paramArrayList, ArrayList<Boolean> paramArrayList1, int paramInt1, int paramInt2, ArraySet<Fragment> paramArraySet) {
    int j = paramInt2;
    int i = paramInt2 - 1;
    while (i >= paramInt1) {
      boolean bool;
      BackStackRecord backStackRecord = paramArrayList.get(i);
      boolean bool1 = ((Boolean)paramArrayList1.get(i)).booleanValue();
      if (backStackRecord.isPostponed() && !backStackRecord.interactsWith(paramArrayList, i + 1, paramInt2)) {
        bool = true;
      } else {
        bool = false;
      } 
      int k = j;
      if (bool) {
        if (this.mPostponedTransactions == null)
          this.mPostponedTransactions = new ArrayList<>(); 
        StartEnterTransitionListener startEnterTransitionListener = new StartEnterTransitionListener(backStackRecord, bool1);
        this.mPostponedTransactions.add(startEnterTransitionListener);
        backStackRecord.setOnStartPostponedListener(startEnterTransitionListener);
        if (bool1) {
          backStackRecord.executeOps();
        } else {
          backStackRecord.executePopOps(false);
        } 
        k = j - 1;
        if (i != k) {
          paramArrayList.remove(i);
          paramArrayList.add(k, backStackRecord);
        } 
        addAddedFragments(paramArraySet);
      } 
      i--;
      j = k;
    } 
    return j;
  }
  
  private void removeRedundantOperationsAndExecute(ArrayList<BackStackRecord> paramArrayList, ArrayList<Boolean> paramArrayList1) {
    if (paramArrayList.isEmpty())
      return; 
    if (paramArrayList.size() == paramArrayList1.size()) {
      executePostponedTransaction(paramArrayList, paramArrayList1);
      int k = paramArrayList.size();
      int j = 0;
      int i = 0;
      while (i < k) {
        int n = j;
        int m = i;
        if (!((BackStackRecord)paramArrayList.get(i)).mReorderingAllowed) {
          if (j != i)
            executeOpsTogether(paramArrayList, paramArrayList1, j, i); 
          m = i + 1;
          j = m;
          if (((Boolean)paramArrayList1.get(i)).booleanValue())
            while (true) {
              j = m;
              if (m < k) {
                j = m;
                if (((Boolean)paramArrayList1.get(m)).booleanValue()) {
                  j = m;
                  if (!((BackStackRecord)paramArrayList.get(m)).mReorderingAllowed) {
                    m++;
                    continue;
                  } 
                } 
              } 
              break;
            }  
          executeOpsTogether(paramArrayList, paramArrayList1, i, j);
          n = j;
          m = j - 1;
        } 
        i = m + 1;
        j = n;
      } 
      if (j != k)
        executeOpsTogether(paramArrayList, paramArrayList1, j, k); 
      return;
    } 
    throw new IllegalStateException("Internal error with the back stack records");
  }
  
  private void reportBackStackChanged() {
    if (this.mBackStackChangeListeners != null)
      for (byte b = 0; b < this.mBackStackChangeListeners.size(); b++)
        ((OnBackStackChangedListener)this.mBackStackChangeListeners.get(b)).onBackStackChanged();  
  }
  
  static int reverseTransit(int paramInt) {
    boolean bool = false;
    switch (paramInt) {
      default:
        return bool;
      case 8194:
        return 4097;
      case 4099:
        return 4099;
      case 4097:
        break;
    } 
    return 8194;
  }
  
  private void setVisibleRemovingFragment(Fragment paramFragment) {
    ViewGroup viewGroup = getFragmentContainer(paramFragment);
    if (viewGroup != null && paramFragment.getEnterAnim() + paramFragment.getExitAnim() + paramFragment.getPopEnterAnim() + paramFragment.getPopExitAnim() > 0) {
      if (viewGroup.getTag(R.id.visible_removing_fragment_view_tag) == null)
        viewGroup.setTag(R.id.visible_removing_fragment_view_tag, paramFragment); 
      ((Fragment)viewGroup.getTag(R.id.visible_removing_fragment_view_tag)).setPopDirection(paramFragment.getPopDirection());
    } 
  }
  
  private void startPendingDeferredFragments() {
    Iterator<FragmentStateManager> iterator = this.mFragmentStore.getActiveFragmentStateManagers().iterator();
    while (iterator.hasNext())
      performPendingDeferredStart(iterator.next()); 
  }
  
  private void throwException(RuntimeException paramRuntimeException) {
    Log.e("FragmentManager", paramRuntimeException.getMessage());
    Log.e("FragmentManager", "Activity state:");
    PrintWriter printWriter = new PrintWriter(new LogWriter("FragmentManager"));
    FragmentHostCallback<?> fragmentHostCallback = this.mHost;
    if (fragmentHostCallback != null) {
      try {
        fragmentHostCallback.onDump("  ", (FileDescriptor)null, printWriter, new String[0]);
      } catch (Exception exception) {
        Log.e("FragmentManager", "Failed dumping state", exception);
      } 
    } else {
      try {
        dump("  ", null, (PrintWriter)exception, new String[0]);
      } catch (Exception exception1) {
        Log.e("FragmentManager", "Failed dumping state", exception1);
      } 
    } 
    throw paramRuntimeException;
  }
  
  private void updateOnBackPressedCallbackEnabled() {
    synchronized (this.mPendingActions) {
      boolean bool1 = this.mPendingActions.isEmpty();
      boolean bool = true;
      if (!bool1) {
        this.mOnBackPressedCallback.setEnabled(true);
        return;
      } 
      OnBackPressedCallback onBackPressedCallback = this.mOnBackPressedCallback;
      if (getBackStackEntryCount() <= 0 || !isPrimaryNavigation(this.mParent))
        bool = false; 
      onBackPressedCallback.setEnabled(bool);
      return;
    } 
  }
  
  void addBackStackState(BackStackRecord paramBackStackRecord) {
    if (this.mBackStack == null)
      this.mBackStack = new ArrayList<>(); 
    this.mBackStack.add(paramBackStackRecord);
  }
  
  void addCancellationSignal(Fragment paramFragment, CancellationSignal paramCancellationSignal) {
    if (this.mExitAnimationCancellationSignals.get(paramFragment) == null)
      this.mExitAnimationCancellationSignals.put(paramFragment, new HashSet<>()); 
    ((HashSet<CancellationSignal>)this.mExitAnimationCancellationSignals.get(paramFragment)).add(paramCancellationSignal);
  }
  
  FragmentStateManager addFragment(Fragment paramFragment) {
    if (isLoggingEnabled(2))
      Log.v("FragmentManager", "add: " + paramFragment); 
    FragmentStateManager fragmentStateManager = createOrGetFragmentStateManager(paramFragment);
    paramFragment.mFragmentManager = this;
    this.mFragmentStore.makeActive(fragmentStateManager);
    if (!paramFragment.mDetached) {
      this.mFragmentStore.addFragment(paramFragment);
      paramFragment.mRemoving = false;
      if (paramFragment.mView == null)
        paramFragment.mHiddenChanged = false; 
      if (isMenuAvailable(paramFragment))
        this.mNeedMenuInvalidate = true; 
    } 
    return fragmentStateManager;
  }
  
  public void addFragmentOnAttachListener(FragmentOnAttachListener paramFragmentOnAttachListener) {
    this.mOnAttachListeners.add(paramFragmentOnAttachListener);
  }
  
  public void addOnBackStackChangedListener(OnBackStackChangedListener paramOnBackStackChangedListener) {
    if (this.mBackStackChangeListeners == null)
      this.mBackStackChangeListeners = new ArrayList<>(); 
    this.mBackStackChangeListeners.add(paramOnBackStackChangedListener);
  }
  
  void addRetainedFragment(Fragment paramFragment) {
    this.mNonConfig.addRetainedFragment(paramFragment);
  }
  
  int allocBackStackIndex() {
    return this.mBackStackIndex.getAndIncrement();
  }
  
  void attachController(FragmentHostCallback<?> paramFragmentHostCallback, FragmentContainer paramFragmentContainer, final Fragment parent) {
    if (this.mHost == null) {
      this.mHost = paramFragmentHostCallback;
      this.mContainer = paramFragmentContainer;
      this.mParent = parent;
      if (parent != null) {
        addFragmentOnAttachListener(new FragmentOnAttachListener() {
              final FragmentManager this$0;
              
              final Fragment val$parent;
              
              public void onAttachFragment(FragmentManager param1FragmentManager, Fragment param1Fragment) {
                parent.onAttachFragment(param1Fragment);
              }
            });
      } else if (paramFragmentHostCallback instanceof FragmentOnAttachListener) {
        addFragmentOnAttachListener((FragmentOnAttachListener)paramFragmentHostCallback);
      } 
      if (this.mParent != null)
        updateOnBackPressedCallbackEnabled(); 
      if (paramFragmentHostCallback instanceof OnBackPressedDispatcherOwner) {
        Fragment fragment;
        OnBackPressedDispatcherOwner onBackPressedDispatcherOwner = (OnBackPressedDispatcherOwner)paramFragmentHostCallback;
        OnBackPressedDispatcher onBackPressedDispatcher = onBackPressedDispatcherOwner.getOnBackPressedDispatcher();
        this.mOnBackPressedDispatcher = onBackPressedDispatcher;
        if (parent != null)
          fragment = parent; 
        onBackPressedDispatcher.addCallback(fragment, this.mOnBackPressedCallback);
      } 
      if (parent != null) {
        this.mNonConfig = parent.mFragmentManager.getChildNonConfig(parent);
      } else if (paramFragmentHostCallback instanceof ViewModelStoreOwner) {
        this.mNonConfig = FragmentManagerViewModel.getInstance(((ViewModelStoreOwner)paramFragmentHostCallback).getViewModelStore());
      } else {
        this.mNonConfig = new FragmentManagerViewModel(false);
      } 
      this.mNonConfig.setIsStateSaved(isStateSaved());
      this.mFragmentStore.setNonConfig(this.mNonConfig);
      paramFragmentHostCallback = this.mHost;
      if (paramFragmentHostCallback instanceof ActivityResultRegistryOwner) {
        ActivityResultRegistry activityResultRegistry = ((ActivityResultRegistryOwner)paramFragmentHostCallback).getActivityResultRegistry();
        if (parent != null) {
          str = parent.mWho + ":";
        } else {
          str = "";
        } 
        String str = "FragmentManager:" + str;
        this.mStartActivityForResult = activityResultRegistry.register(str + "StartActivityForResult", (ActivityResultContract)new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
              final FragmentManager this$0;
              
              public void onActivityResult(ActivityResult param1ActivityResult) {
                FragmentManager.LaunchedFragmentInfo launchedFragmentInfo = FragmentManager.this.mLaunchedFragments.pollFirst();
                if (launchedFragmentInfo == null) {
                  Log.w("FragmentManager", "No Activities were started for result for " + this);
                  return;
                } 
                String str = launchedFragmentInfo.mWho;
                int i = launchedFragmentInfo.mRequestCode;
                Fragment fragment = FragmentManager.this.mFragmentStore.findFragmentByWho(str);
                if (fragment == null) {
                  Log.w("FragmentManager", "Activity result delivered for unknown Fragment " + str);
                  return;
                } 
                fragment.onActivityResult(i, param1ActivityResult.getResultCode(), param1ActivityResult.getData());
              }
            });
        this.mStartIntentSenderForResult = activityResultRegistry.register(str + "StartIntentSenderForResult", new FragmentIntentSenderContract(), new ActivityResultCallback<ActivityResult>() {
              final FragmentManager this$0;
              
              public void onActivityResult(ActivityResult param1ActivityResult) {
                FragmentManager.LaunchedFragmentInfo launchedFragmentInfo = FragmentManager.this.mLaunchedFragments.pollFirst();
                if (launchedFragmentInfo == null) {
                  Log.w("FragmentManager", "No IntentSenders were started for " + this);
                  return;
                } 
                String str = launchedFragmentInfo.mWho;
                int i = launchedFragmentInfo.mRequestCode;
                Fragment fragment = FragmentManager.this.mFragmentStore.findFragmentByWho(str);
                if (fragment == null) {
                  Log.w("FragmentManager", "Intent Sender result delivered for unknown Fragment " + str);
                  return;
                } 
                fragment.onActivityResult(i, param1ActivityResult.getResultCode(), param1ActivityResult.getData());
              }
            });
        this.mRequestPermissions = activityResultRegistry.register(str + "RequestPermissions", (ActivityResultContract)new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
              final FragmentManager this$0;
              
              public void onActivityResult(Map<String, Boolean> param1Map) {
                String[] arrayOfString = (String[])param1Map.keySet().toArray((Object[])new String[0]);
                ArrayList<Boolean> arrayList = new ArrayList(param1Map.values());
                int[] arrayOfInt = new int[arrayList.size()];
                int i;
                for (i = 0; i < arrayList.size(); i++) {
                  byte b;
                  if (((Boolean)arrayList.get(i)).booleanValue()) {
                    b = 0;
                  } else {
                    b = -1;
                  } 
                  arrayOfInt[i] = b;
                } 
                FragmentManager.LaunchedFragmentInfo launchedFragmentInfo = FragmentManager.this.mLaunchedFragments.pollFirst();
                if (launchedFragmentInfo == null) {
                  Log.w("FragmentManager", "No permissions were requested for " + this);
                  return;
                } 
                String str = launchedFragmentInfo.mWho;
                i = launchedFragmentInfo.mRequestCode;
                Fragment fragment = FragmentManager.this.mFragmentStore.findFragmentByWho(str);
                if (fragment == null) {
                  Log.w("FragmentManager", "Permission request result delivered for unknown Fragment " + str);
                  return;
                } 
                fragment.onRequestPermissionsResult(i, arrayOfString, arrayOfInt);
              }
            });
      } 
      return;
    } 
    throw new IllegalStateException("Already attached");
  }
  
  void attachFragment(Fragment paramFragment) {
    if (isLoggingEnabled(2))
      Log.v("FragmentManager", "attach: " + paramFragment); 
    if (paramFragment.mDetached) {
      paramFragment.mDetached = false;
      if (!paramFragment.mAdded) {
        this.mFragmentStore.addFragment(paramFragment);
        if (isLoggingEnabled(2))
          Log.v("FragmentManager", "add from attach: " + paramFragment); 
        if (isMenuAvailable(paramFragment))
          this.mNeedMenuInvalidate = true; 
      } 
    } 
  }
  
  public FragmentTransaction beginTransaction() {
    return new BackStackRecord(this);
  }
  
  boolean checkForMenus() {
    boolean bool = false;
    for (Fragment fragment : this.mFragmentStore.getActiveFragments()) {
      if (fragment != null)
        bool = isMenuAvailable(fragment); 
      if (bool)
        return true; 
    } 
    return false;
  }
  
  public final void clearFragmentResult(String paramString) {
    this.mResults.remove(paramString);
  }
  
  public final void clearFragmentResultListener(String paramString) {
    LifecycleAwareResultListener lifecycleAwareResultListener = this.mResultListeners.remove(paramString);
    if (lifecycleAwareResultListener != null)
      lifecycleAwareResultListener.removeObserver(); 
  }
  
  void completeExecute(BackStackRecord paramBackStackRecord, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3) {
    if (paramBoolean1) {
      paramBackStackRecord.executePopOps(paramBoolean3);
    } else {
      paramBackStackRecord.executeOps();
    } 
    ArrayList<BackStackRecord> arrayList1 = new ArrayList(1);
    ArrayList<Boolean> arrayList = new ArrayList(1);
    arrayList1.add(paramBackStackRecord);
    arrayList.add(Boolean.valueOf(paramBoolean1));
    if (paramBoolean2 && this.mCurState >= 1)
      FragmentTransition.startTransitions(this.mHost.getContext(), this.mContainer, arrayList1, arrayList, 0, 1, true, this.mFragmentTransitionCallback); 
    if (paramBoolean3)
      moveToState(this.mCurState, true); 
    for (Fragment fragment : this.mFragmentStore.getActiveFragments()) {
      if (fragment != null && fragment.mView != null && fragment.mIsNewlyAdded && paramBackStackRecord.interactsWith(fragment.mContainerId)) {
        if (fragment.mPostponedAlpha > 0.0F)
          fragment.mView.setAlpha(fragment.mPostponedAlpha); 
        if (paramBoolean3) {
          fragment.mPostponedAlpha = 0.0F;
          continue;
        } 
        fragment.mPostponedAlpha = -1.0F;
        fragment.mIsNewlyAdded = false;
      } 
    } 
  }
  
  FragmentStateManager createOrGetFragmentStateManager(Fragment paramFragment) {
    FragmentStateManager fragmentStateManager2 = this.mFragmentStore.getFragmentStateManager(paramFragment.mWho);
    if (fragmentStateManager2 != null)
      return fragmentStateManager2; 
    FragmentStateManager fragmentStateManager1 = new FragmentStateManager(this.mLifecycleCallbacksDispatcher, this.mFragmentStore, paramFragment);
    fragmentStateManager1.restoreState(this.mHost.getContext().getClassLoader());
    fragmentStateManager1.setFragmentManagerState(this.mCurState);
    return fragmentStateManager1;
  }
  
  void detachFragment(Fragment paramFragment) {
    if (isLoggingEnabled(2))
      Log.v("FragmentManager", "detach: " + paramFragment); 
    if (!paramFragment.mDetached) {
      paramFragment.mDetached = true;
      if (paramFragment.mAdded) {
        if (isLoggingEnabled(2))
          Log.v("FragmentManager", "remove from detach: " + paramFragment); 
        this.mFragmentStore.removeFragment(paramFragment);
        if (isMenuAvailable(paramFragment))
          this.mNeedMenuInvalidate = true; 
        setVisibleRemovingFragment(paramFragment);
      } 
    } 
  }
  
  void dispatchActivityCreated() {
    this.mStateSaved = false;
    this.mStopped = false;
    this.mNonConfig.setIsStateSaved(false);
    dispatchStateChange(4);
  }
  
  void dispatchAttach() {
    this.mStateSaved = false;
    this.mStopped = false;
    this.mNonConfig.setIsStateSaved(false);
    dispatchStateChange(0);
  }
  
  void dispatchConfigurationChanged(Configuration paramConfiguration) {
    for (Fragment fragment : this.mFragmentStore.getFragments()) {
      if (fragment != null)
        fragment.performConfigurationChanged(paramConfiguration); 
    } 
  }
  
  boolean dispatchContextItemSelected(MenuItem paramMenuItem) {
    if (this.mCurState < 1)
      return false; 
    for (Fragment fragment : this.mFragmentStore.getFragments()) {
      if (fragment != null && fragment.performContextItemSelected(paramMenuItem))
        return true; 
    } 
    return false;
  }
  
  void dispatchCreate() {
    this.mStateSaved = false;
    this.mStopped = false;
    this.mNonConfig.setIsStateSaved(false);
    dispatchStateChange(1);
  }
  
  boolean dispatchCreateOptionsMenu(Menu paramMenu, MenuInflater paramMenuInflater) {
    if (this.mCurState < 1)
      return false; 
    boolean bool = false;
    ArrayList<Fragment> arrayList = null;
    for (Fragment fragment : this.mFragmentStore.getFragments()) {
      boolean bool1 = bool;
      ArrayList<Fragment> arrayList1 = arrayList;
      if (fragment != null) {
        bool1 = bool;
        arrayList1 = arrayList;
        if (isParentMenuVisible(fragment)) {
          bool1 = bool;
          arrayList1 = arrayList;
          if (fragment.performCreateOptionsMenu(paramMenu, paramMenuInflater)) {
            bool1 = true;
            arrayList1 = arrayList;
            if (arrayList == null)
              arrayList1 = new ArrayList(); 
            arrayList1.add(fragment);
          } 
        } 
      } 
      bool = bool1;
      arrayList = arrayList1;
    } 
    if (this.mCreatedMenus != null)
      for (byte b = 0; b < this.mCreatedMenus.size(); b++) {
        Fragment fragment = this.mCreatedMenus.get(b);
        if (arrayList == null || !arrayList.contains(fragment))
          fragment.onDestroyOptionsMenu(); 
      }  
    this.mCreatedMenus = arrayList;
    return bool;
  }
  
  void dispatchDestroy() {
    this.mDestroyed = true;
    execPendingActions(true);
    endAnimatingAwayFragments();
    dispatchStateChange(-1);
    this.mHost = null;
    this.mContainer = null;
    this.mParent = null;
    if (this.mOnBackPressedDispatcher != null) {
      this.mOnBackPressedCallback.remove();
      this.mOnBackPressedDispatcher = null;
    } 
    ActivityResultLauncher<Intent> activityResultLauncher = this.mStartActivityForResult;
    if (activityResultLauncher != null) {
      activityResultLauncher.unregister();
      this.mStartIntentSenderForResult.unregister();
      this.mRequestPermissions.unregister();
    } 
  }
  
  void dispatchDestroyView() {
    dispatchStateChange(1);
  }
  
  void dispatchLowMemory() {
    for (Fragment fragment : this.mFragmentStore.getFragments()) {
      if (fragment != null)
        fragment.performLowMemory(); 
    } 
  }
  
  void dispatchMultiWindowModeChanged(boolean paramBoolean) {
    for (Fragment fragment : this.mFragmentStore.getFragments()) {
      if (fragment != null)
        fragment.performMultiWindowModeChanged(paramBoolean); 
    } 
  }
  
  void dispatchOnAttachFragment(Fragment paramFragment) {
    Iterator<FragmentOnAttachListener> iterator = this.mOnAttachListeners.iterator();
    while (iterator.hasNext())
      ((FragmentOnAttachListener)iterator.next()).onAttachFragment(this, paramFragment); 
  }
  
  boolean dispatchOptionsItemSelected(MenuItem paramMenuItem) {
    if (this.mCurState < 1)
      return false; 
    for (Fragment fragment : this.mFragmentStore.getFragments()) {
      if (fragment != null && fragment.performOptionsItemSelected(paramMenuItem))
        return true; 
    } 
    return false;
  }
  
  void dispatchOptionsMenuClosed(Menu paramMenu) {
    if (this.mCurState < 1)
      return; 
    for (Fragment fragment : this.mFragmentStore.getFragments()) {
      if (fragment != null)
        fragment.performOptionsMenuClosed(paramMenu); 
    } 
  }
  
  void dispatchPause() {
    dispatchStateChange(5);
  }
  
  void dispatchPictureInPictureModeChanged(boolean paramBoolean) {
    for (Fragment fragment : this.mFragmentStore.getFragments()) {
      if (fragment != null)
        fragment.performPictureInPictureModeChanged(paramBoolean); 
    } 
  }
  
  boolean dispatchPrepareOptionsMenu(Menu paramMenu) {
    if (this.mCurState < 1)
      return false; 
    boolean bool = false;
    for (Fragment fragment : this.mFragmentStore.getFragments()) {
      boolean bool1 = bool;
      if (fragment != null) {
        bool1 = bool;
        if (isParentMenuVisible(fragment)) {
          bool1 = bool;
          if (fragment.performPrepareOptionsMenu(paramMenu))
            bool1 = true; 
        } 
      } 
      bool = bool1;
    } 
    return bool;
  }
  
  void dispatchPrimaryNavigationFragmentChanged() {
    updateOnBackPressedCallbackEnabled();
    dispatchParentPrimaryNavigationFragmentChanged(this.mPrimaryNav);
  }
  
  void dispatchResume() {
    this.mStateSaved = false;
    this.mStopped = false;
    this.mNonConfig.setIsStateSaved(false);
    dispatchStateChange(7);
  }
  
  void dispatchStart() {
    this.mStateSaved = false;
    this.mStopped = false;
    this.mNonConfig.setIsStateSaved(false);
    dispatchStateChange(5);
  }
  
  void dispatchStop() {
    this.mStopped = true;
    this.mNonConfig.setIsStateSaved(true);
    dispatchStateChange(4);
  }
  
  void dispatchViewCreated() {
    dispatchStateChange(2);
  }
  
  public void dump(String paramString, FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString) {
    String str = paramString + "    ";
    this.mFragmentStore.dump(paramString, paramFileDescriptor, paramPrintWriter, paramArrayOfString);
    ArrayList<Fragment> arrayList1 = this.mCreatedMenus;
    if (arrayList1 != null) {
      int i = arrayList1.size();
      if (i > 0) {
        paramPrintWriter.print(paramString);
        paramPrintWriter.println("Fragments Created Menus:");
        for (byte b = 0; b < i; b++) {
          Fragment fragment = this.mCreatedMenus.get(b);
          paramPrintWriter.print(paramString);
          paramPrintWriter.print("  #");
          paramPrintWriter.print(b);
          paramPrintWriter.print(": ");
          paramPrintWriter.println(fragment.toString());
        } 
      } 
    } 
    ArrayList<BackStackRecord> arrayList = this.mBackStack;
    if (arrayList != null) {
      int i = arrayList.size();
      if (i > 0) {
        paramPrintWriter.print(paramString);
        paramPrintWriter.println("Back Stack:");
        for (byte b = 0; b < i; b++) {
          BackStackRecord backStackRecord = this.mBackStack.get(b);
          paramPrintWriter.print(paramString);
          paramPrintWriter.print("  #");
          paramPrintWriter.print(b);
          paramPrintWriter.print(": ");
          paramPrintWriter.println(backStackRecord.toString());
          backStackRecord.dump(str, paramPrintWriter);
        } 
      } 
    } 
    paramPrintWriter.print(paramString);
    paramPrintWriter.println("Back Stack Index: " + this.mBackStackIndex.get());
    synchronized (this.mPendingActions) {
      int i = this.mPendingActions.size();
      if (i > 0) {
        paramPrintWriter.print(paramString);
        paramPrintWriter.println("Pending Actions:");
        for (byte b = 0; b < i; b++) {
          OpGenerator opGenerator = this.mPendingActions.get(b);
          paramPrintWriter.print(paramString);
          paramPrintWriter.print("  #");
          paramPrintWriter.print(b);
          paramPrintWriter.print(": ");
          paramPrintWriter.println(opGenerator);
        } 
      } 
      paramPrintWriter.print(paramString);
      paramPrintWriter.println("FragmentManager misc state:");
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("  mHost=");
      paramPrintWriter.println(this.mHost);
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("  mContainer=");
      paramPrintWriter.println(this.mContainer);
      if (this.mParent != null) {
        paramPrintWriter.print(paramString);
        paramPrintWriter.print("  mParent=");
        paramPrintWriter.println(this.mParent);
      } 
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("  mCurState=");
      paramPrintWriter.print(this.mCurState);
      paramPrintWriter.print(" mStateSaved=");
      paramPrintWriter.print(this.mStateSaved);
      paramPrintWriter.print(" mStopped=");
      paramPrintWriter.print(this.mStopped);
      paramPrintWriter.print(" mDestroyed=");
      paramPrintWriter.println(this.mDestroyed);
      if (this.mNeedMenuInvalidate) {
        paramPrintWriter.print(paramString);
        paramPrintWriter.print("  mNeedMenuInvalidate=");
        paramPrintWriter.println(this.mNeedMenuInvalidate);
      } 
      return;
    } 
  }
  
  void enqueueAction(OpGenerator paramOpGenerator, boolean paramBoolean) {
    if (!paramBoolean) {
      if (this.mHost == null) {
        if (this.mDestroyed)
          throw new IllegalStateException("FragmentManager has been destroyed"); 
        throw new IllegalStateException("FragmentManager has not been attached to a host.");
      } 
      checkStateLoss();
    } 
    synchronized (this.mPendingActions) {
      IllegalStateException illegalStateException;
      if (this.mHost == null) {
        if (paramBoolean)
          return; 
        illegalStateException = new IllegalStateException();
        this("Activity has been destroyed");
        throw illegalStateException;
      } 
      this.mPendingActions.add(illegalStateException);
      scheduleCommit();
      return;
    } 
  }
  
  boolean execPendingActions(boolean paramBoolean) {
    ensureExecReady(paramBoolean);
    paramBoolean = false;
    while (generateOpsForPendingActions(this.mTmpRecords, this.mTmpIsPop)) {
      this.mExecutingActions = true;
      try {
        removeRedundantOperationsAndExecute(this.mTmpRecords, this.mTmpIsPop);
        cleanupExec();
      } finally {
        cleanupExec();
      } 
    } 
    updateOnBackPressedCallbackEnabled();
    doPendingDeferredStart();
    this.mFragmentStore.burpActive();
    return paramBoolean;
  }
  
  void execSingleAction(OpGenerator paramOpGenerator, boolean paramBoolean) {
    if (paramBoolean && (this.mHost == null || this.mDestroyed))
      return; 
    ensureExecReady(paramBoolean);
    if (paramOpGenerator.generateOps(this.mTmpRecords, this.mTmpIsPop)) {
      this.mExecutingActions = true;
      try {
        removeRedundantOperationsAndExecute(this.mTmpRecords, this.mTmpIsPop);
      } finally {
        cleanupExec();
      } 
    } 
    updateOnBackPressedCallbackEnabled();
    doPendingDeferredStart();
    this.mFragmentStore.burpActive();
  }
  
  public boolean executePendingTransactions() {
    boolean bool = execPendingActions(true);
    forcePostponedTransactions();
    return bool;
  }
  
  Fragment findActiveFragment(String paramString) {
    return this.mFragmentStore.findActiveFragment(paramString);
  }
  
  public Fragment findFragmentById(int paramInt) {
    return this.mFragmentStore.findFragmentById(paramInt);
  }
  
  public Fragment findFragmentByTag(String paramString) {
    return this.mFragmentStore.findFragmentByTag(paramString);
  }
  
  Fragment findFragmentByWho(String paramString) {
    return this.mFragmentStore.findFragmentByWho(paramString);
  }
  
  int getActiveFragmentCount() {
    return this.mFragmentStore.getActiveFragmentCount();
  }
  
  List<Fragment> getActiveFragments() {
    return this.mFragmentStore.getActiveFragments();
  }
  
  public BackStackEntry getBackStackEntryAt(int paramInt) {
    return this.mBackStack.get(paramInt);
  }
  
  public int getBackStackEntryCount() {
    boolean bool;
    ArrayList<BackStackRecord> arrayList = this.mBackStack;
    if (arrayList != null) {
      bool = arrayList.size();
    } else {
      bool = false;
    } 
    return bool;
  }
  
  FragmentContainer getContainer() {
    return this.mContainer;
  }
  
  public Fragment getFragment(Bundle paramBundle, String paramString) {
    String str = paramBundle.getString(paramString);
    if (str == null)
      return null; 
    Fragment fragment = findActiveFragment(str);
    if (fragment == null)
      throwException(new IllegalStateException("Fragment no longer exists for key " + paramString + ": unique id " + str)); 
    return fragment;
  }
  
  public FragmentFactory getFragmentFactory() {
    FragmentFactory fragmentFactory = this.mFragmentFactory;
    if (fragmentFactory != null)
      return fragmentFactory; 
    Fragment fragment = this.mParent;
    return (fragment != null) ? fragment.mFragmentManager.getFragmentFactory() : this.mHostFragmentFactory;
  }
  
  FragmentStore getFragmentStore() {
    return this.mFragmentStore;
  }
  
  public List<Fragment> getFragments() {
    return this.mFragmentStore.getFragments();
  }
  
  FragmentHostCallback<?> getHost() {
    return this.mHost;
  }
  
  LayoutInflater.Factory2 getLayoutInflaterFactory() {
    return this.mLayoutInflaterFactory;
  }
  
  FragmentLifecycleCallbacksDispatcher getLifecycleCallbacksDispatcher() {
    return this.mLifecycleCallbacksDispatcher;
  }
  
  Fragment getParent() {
    return this.mParent;
  }
  
  public Fragment getPrimaryNavigationFragment() {
    return this.mPrimaryNav;
  }
  
  SpecialEffectsControllerFactory getSpecialEffectsControllerFactory() {
    SpecialEffectsControllerFactory specialEffectsControllerFactory = this.mSpecialEffectsControllerFactory;
    if (specialEffectsControllerFactory != null)
      return specialEffectsControllerFactory; 
    Fragment fragment = this.mParent;
    return (fragment != null) ? fragment.mFragmentManager.getSpecialEffectsControllerFactory() : this.mDefaultSpecialEffectsControllerFactory;
  }
  
  ViewModelStore getViewModelStore(Fragment paramFragment) {
    return this.mNonConfig.getViewModelStore(paramFragment);
  }
  
  void handleOnBackPressed() {
    execPendingActions(true);
    if (this.mOnBackPressedCallback.isEnabled()) {
      popBackStackImmediate();
    } else {
      this.mOnBackPressedDispatcher.onBackPressed();
    } 
  }
  
  void hideFragment(Fragment paramFragment) {
    if (isLoggingEnabled(2))
      Log.v("FragmentManager", "hide: " + paramFragment); 
    if (!paramFragment.mHidden) {
      paramFragment.mHidden = true;
      paramFragment.mHiddenChanged = true ^ paramFragment.mHiddenChanged;
      setVisibleRemovingFragment(paramFragment);
    } 
  }
  
  void invalidateMenuForFragment(Fragment paramFragment) {
    if (paramFragment.mAdded && isMenuAvailable(paramFragment))
      this.mNeedMenuInvalidate = true; 
  }
  
  public boolean isDestroyed() {
    return this.mDestroyed;
  }
  
  boolean isParentMenuVisible(Fragment paramFragment) {
    return (paramFragment == null) ? true : paramFragment.isMenuVisible();
  }
  
  boolean isPrimaryNavigation(Fragment paramFragment) {
    boolean bool = true;
    if (paramFragment == null)
      return true; 
    FragmentManager fragmentManager = paramFragment.mFragmentManager;
    if (!paramFragment.equals(fragmentManager.getPrimaryNavigationFragment()) || !isPrimaryNavigation(fragmentManager.mParent))
      bool = false; 
    return bool;
  }
  
  boolean isStateAtLeast(int paramInt) {
    boolean bool;
    if (this.mCurState >= paramInt) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isStateSaved() {
    return (this.mStateSaved || this.mStopped);
  }
  
  void launchRequestPermissions(Fragment paramFragment, String[] paramArrayOfString, int paramInt) {
    LaunchedFragmentInfo launchedFragmentInfo;
    if (this.mRequestPermissions != null) {
      launchedFragmentInfo = new LaunchedFragmentInfo(paramFragment.mWho, paramInt);
      this.mLaunchedFragments.addLast(launchedFragmentInfo);
      this.mRequestPermissions.launch(paramArrayOfString);
    } else {
      this.mHost.onRequestPermissionsFromFragment((Fragment)launchedFragmentInfo, paramArrayOfString, paramInt);
    } 
  }
  
  void launchStartActivityForResult(Fragment paramFragment, Intent paramIntent, int paramInt, Bundle paramBundle) {
    LaunchedFragmentInfo launchedFragmentInfo;
    if (this.mStartActivityForResult != null) {
      launchedFragmentInfo = new LaunchedFragmentInfo(paramFragment.mWho, paramInt);
      this.mLaunchedFragments.addLast(launchedFragmentInfo);
      if (paramIntent != null && paramBundle != null)
        paramIntent.putExtra("androidx.activity.result.contract.extra.ACTIVITY_OPTIONS_BUNDLE", paramBundle); 
      this.mStartActivityForResult.launch(paramIntent);
    } else {
      this.mHost.onStartActivityFromFragment((Fragment)launchedFragmentInfo, paramIntent, paramInt, paramBundle);
    } 
  }
  
  void launchStartIntentSenderForResult(Fragment paramFragment, IntentSender paramIntentSender, int paramInt1, Intent paramIntent, int paramInt2, int paramInt3, int paramInt4, Bundle paramBundle) throws IntentSender.SendIntentException {
    IntentSenderRequest intentSenderRequest;
    LaunchedFragmentInfo launchedFragmentInfo;
    if (this.mStartIntentSenderForResult != null) {
      if (paramBundle != null) {
        if (paramIntent == null) {
          paramIntent = new Intent();
          paramIntent.putExtra("androidx.fragment.extra.ACTIVITY_OPTIONS_BUNDLE", true);
        } 
        if (isLoggingEnabled(2))
          Log.v("FragmentManager", "ActivityOptions " + paramBundle + " were added to fillInIntent " + paramIntent + " for fragment " + paramFragment); 
        paramIntent.putExtra("androidx.activity.result.contract.extra.ACTIVITY_OPTIONS_BUNDLE", paramBundle);
      } 
      intentSenderRequest = (new IntentSenderRequest.Builder(paramIntentSender)).setFillInIntent(paramIntent).setFlags(paramInt3, paramInt2).build();
      launchedFragmentInfo = new LaunchedFragmentInfo(paramFragment.mWho, paramInt1);
      this.mLaunchedFragments.addLast(launchedFragmentInfo);
      if (isLoggingEnabled(2))
        Log.v("FragmentManager", "Fragment " + paramFragment + "is launching an IntentSender for result "); 
      this.mStartIntentSenderForResult.launch(intentSenderRequest);
    } else {
      this.mHost.onStartIntentSenderFromFragment(paramFragment, (IntentSender)intentSenderRequest, paramInt1, (Intent)launchedFragmentInfo, paramInt2, paramInt3, paramInt4, paramBundle);
    } 
  }
  
  void moveFragmentToExpectedState(Fragment paramFragment) {
    if (!this.mFragmentStore.containsActiveFragment(paramFragment.mWho)) {
      if (isLoggingEnabled(3))
        Log.d("FragmentManager", "Ignoring moving " + paramFragment + " to state " + this.mCurState + "since it is not added to " + this); 
      return;
    } 
    moveToState(paramFragment);
    if (paramFragment.mView != null && paramFragment.mIsNewlyAdded && paramFragment.mContainer != null) {
      if (paramFragment.mPostponedAlpha > 0.0F)
        paramFragment.mView.setAlpha(paramFragment.mPostponedAlpha); 
      paramFragment.mPostponedAlpha = 0.0F;
      paramFragment.mIsNewlyAdded = false;
      FragmentAnim.AnimationOrAnimator animationOrAnimator = FragmentAnim.loadAnimation(this.mHost.getContext(), paramFragment, true, paramFragment.getPopDirection());
      if (animationOrAnimator != null)
        if (animationOrAnimator.animation != null) {
          paramFragment.mView.startAnimation(animationOrAnimator.animation);
        } else {
          animationOrAnimator.animator.setTarget(paramFragment.mView);
          animationOrAnimator.animator.start();
        }  
    } 
    if (paramFragment.mHiddenChanged)
      completeShowHideFragment(paramFragment); 
  }
  
  void moveToState(int paramInt, boolean paramBoolean) {
    if (this.mHost != null || paramInt == -1) {
      if (!paramBoolean && paramInt == this.mCurState)
        return; 
      this.mCurState = paramInt;
      if (USE_STATE_MANAGER) {
        this.mFragmentStore.moveToExpectedState();
      } else {
        Iterator<Fragment> iterator = this.mFragmentStore.getFragments().iterator();
        while (iterator.hasNext())
          moveFragmentToExpectedState(iterator.next()); 
        for (FragmentStateManager fragmentStateManager : this.mFragmentStore.getActiveFragmentStateManagers()) {
          Fragment fragment = fragmentStateManager.getFragment();
          if (!fragment.mIsNewlyAdded)
            moveFragmentToExpectedState(fragment); 
          if (fragment.mRemoving && !fragment.isInBackStack()) {
            paramInt = 1;
          } else {
            paramInt = 0;
          } 
          if (paramInt != 0)
            this.mFragmentStore.makeInactive(fragmentStateManager); 
        } 
      } 
      startPendingDeferredFragments();
      if (this.mNeedMenuInvalidate) {
        FragmentHostCallback<?> fragmentHostCallback = this.mHost;
        if (fragmentHostCallback != null && this.mCurState == 7) {
          fragmentHostCallback.onSupportInvalidateOptionsMenu();
          this.mNeedMenuInvalidate = false;
        } 
      } 
      return;
    } 
    throw new IllegalStateException("No activity");
  }
  
  void moveToState(Fragment paramFragment) {
    moveToState(paramFragment, this.mCurState);
  }
  
  void moveToState(Fragment paramFragment, int paramInt) {
    FragmentStateManager fragmentStateManager2 = this.mFragmentStore.getFragmentStateManager(paramFragment.mWho);
    FragmentStateManager fragmentStateManager1 = fragmentStateManager2;
    if (fragmentStateManager2 == null) {
      fragmentStateManager1 = new FragmentStateManager(this.mLifecycleCallbacksDispatcher, this.mFragmentStore, paramFragment);
      fragmentStateManager1.setFragmentManagerState(1);
    } 
    int i = paramInt;
    if (paramFragment.mFromLayout) {
      i = paramInt;
      if (paramFragment.mInLayout) {
        i = paramInt;
        if (paramFragment.mState == 2)
          i = Math.max(paramInt, 2); 
      } 
    } 
    int j = Math.min(i, fragmentStateManager1.computeExpectedState());
    if (paramFragment.mState <= j) {
      if (paramFragment.mState < j && !this.mExitAnimationCancellationSignals.isEmpty())
        cancelExitAnimation(paramFragment); 
      switch (paramFragment.mState) {
        case -1:
          if (j > -1)
            fragmentStateManager1.attach(); 
        case 0:
          if (j > 0)
            fragmentStateManager1.create(); 
        case 1:
          if (j > -1)
            fragmentStateManager1.ensureInflatedView(); 
          if (j > 1)
            fragmentStateManager1.createView(); 
        case 2:
          if (j > 2)
            fragmentStateManager1.activityCreated(); 
        case 4:
          if (j > 4)
            fragmentStateManager1.start(); 
        case 5:
          if (j > 5)
            fragmentStateManager1.resume(); 
          break;
      } 
      i = j;
    } else {
      i = j;
      if (paramFragment.mState > j) {
        paramInt = j;
        switch (paramFragment.mState) {
          default:
            i = j;
            break;
          case 7:
            if (j < 7)
              fragmentStateManager1.pause(); 
          case 5:
            if (j < 5)
              fragmentStateManager1.stop(); 
          case 4:
            if (j < 4) {
              if (isLoggingEnabled(3))
                Log.d("FragmentManager", "movefrom ACTIVITY_CREATED: " + paramFragment); 
              if (paramFragment.mView != null && this.mHost.onShouldSaveFragmentState(paramFragment) && paramFragment.mSavedViewState == null)
                fragmentStateManager1.saveViewState(); 
            } 
          case 2:
            if (j < 2) {
              FragmentStateManager fragmentStateManager = null;
              if (paramFragment.mView != null && paramFragment.mContainer != null) {
                paramFragment.mContainer.endViewTransition(paramFragment.mView);
                paramFragment.mView.clearAnimation();
                if (!paramFragment.isRemovingParent()) {
                  FragmentAnim.AnimationOrAnimator animationOrAnimator;
                  fragmentStateManager2 = fragmentStateManager;
                  if (this.mCurState > -1) {
                    fragmentStateManager2 = fragmentStateManager;
                    if (!this.mDestroyed) {
                      fragmentStateManager2 = fragmentStateManager;
                      if (paramFragment.mView.getVisibility() == 0) {
                        fragmentStateManager2 = fragmentStateManager;
                        if (paramFragment.mPostponedAlpha >= 0.0F)
                          animationOrAnimator = FragmentAnim.loadAnimation(this.mHost.getContext(), paramFragment, false, paramFragment.getPopDirection()); 
                      } 
                    } 
                  } 
                  paramFragment.mPostponedAlpha = 0.0F;
                  ViewGroup viewGroup = paramFragment.mContainer;
                  View view = paramFragment.mView;
                  if (animationOrAnimator != null)
                    FragmentAnim.animateRemoveFragment(paramFragment, animationOrAnimator, this.mFragmentTransitionCallback); 
                  viewGroup.removeView(view);
                  if (isLoggingEnabled(2))
                    Log.v("FragmentManager", "Removing view " + view + " for fragment " + paramFragment + " from container " + viewGroup); 
                  if (viewGroup != paramFragment.mContainer)
                    return; 
                } 
              } 
              if (this.mExitAnimationCancellationSignals.get(paramFragment) == null)
                fragmentStateManager1.destroyFragmentView(); 
            } 
          case 1:
            paramInt = j;
            if (j < 1)
              if (this.mExitAnimationCancellationSignals.get(paramFragment) != null) {
                paramInt = 1;
              } else {
                fragmentStateManager1.destroy();
                paramInt = j;
              }  
          case 0:
            i = paramInt;
            if (paramInt < 0) {
              fragmentStateManager1.detach();
              i = paramInt;
            } 
            break;
        } 
      } 
    } 
    if (paramFragment.mState != i) {
      if (isLoggingEnabled(3))
        Log.d("FragmentManager", "moveToState: Fragment state for " + paramFragment + " not updated inline; expected state " + i + " found " + paramFragment.mState); 
      paramFragment.mState = i;
    } 
  }
  
  void noteStateNotSaved() {
    if (this.mHost == null)
      return; 
    this.mStateSaved = false;
    this.mStopped = false;
    this.mNonConfig.setIsStateSaved(false);
    for (Fragment fragment : this.mFragmentStore.getFragments()) {
      if (fragment != null)
        fragment.noteStateNotSaved(); 
    } 
  }
  
  void onContainerAvailable(FragmentContainerView paramFragmentContainerView) {
    for (FragmentStateManager fragmentStateManager : this.mFragmentStore.getActiveFragmentStateManagers()) {
      Fragment fragment = fragmentStateManager.getFragment();
      if (fragment.mContainerId == paramFragmentContainerView.getId() && fragment.mView != null && fragment.mView.getParent() == null) {
        fragment.mContainer = (ViewGroup)paramFragmentContainerView;
        fragmentStateManager.addViewToContainer();
      } 
    } 
  }
  
  @Deprecated
  public FragmentTransaction openTransaction() {
    return beginTransaction();
  }
  
  void performPendingDeferredStart(FragmentStateManager paramFragmentStateManager) {
    Fragment fragment = paramFragmentStateManager.getFragment();
    if (fragment.mDeferStart) {
      if (this.mExecutingActions) {
        this.mHavePendingDeferredStart = true;
        return;
      } 
      fragment.mDeferStart = false;
      if (USE_STATE_MANAGER) {
        paramFragmentStateManager.moveToExpectedState();
      } else {
        moveToState(fragment);
      } 
    } 
  }
  
  public void popBackStack() {
    enqueueAction(new PopBackStackState(null, -1, 0), false);
  }
  
  public void popBackStack(int paramInt1, int paramInt2) {
    if (paramInt1 >= 0) {
      enqueueAction(new PopBackStackState(null, paramInt1, paramInt2), false);
      return;
    } 
    throw new IllegalArgumentException("Bad id: " + paramInt1);
  }
  
  public void popBackStack(String paramString, int paramInt) {
    enqueueAction(new PopBackStackState(paramString, -1, paramInt), false);
  }
  
  public boolean popBackStackImmediate() {
    return popBackStackImmediate(null, -1, 0);
  }
  
  public boolean popBackStackImmediate(int paramInt1, int paramInt2) {
    if (paramInt1 >= 0)
      return popBackStackImmediate(null, paramInt1, paramInt2); 
    throw new IllegalArgumentException("Bad id: " + paramInt1);
  }
  
  public boolean popBackStackImmediate(String paramString, int paramInt) {
    return popBackStackImmediate(paramString, -1, paramInt);
  }
  
  boolean popBackStackState(ArrayList<BackStackRecord> paramArrayList, ArrayList<Boolean> paramArrayList1, String paramString, int paramInt1, int paramInt2) {
    ArrayList<BackStackRecord> arrayList = this.mBackStack;
    if (arrayList == null)
      return false; 
    if (paramString == null && paramInt1 < 0 && (paramInt2 & 0x1) == 0) {
      paramInt1 = arrayList.size() - 1;
      if (paramInt1 < 0)
        return false; 
      paramArrayList.add(this.mBackStack.remove(paramInt1));
      paramArrayList1.add(Boolean.valueOf(true));
    } else {
      int i = -1;
      if (paramString != null || paramInt1 >= 0) {
        int j;
        for (j = arrayList.size() - 1; j >= 0; j--) {
          BackStackRecord backStackRecord = this.mBackStack.get(j);
          if ((paramString != null && paramString.equals(backStackRecord.getName())) || (paramInt1 >= 0 && paramInt1 == backStackRecord.mIndex))
            break; 
        } 
        if (j < 0)
          return false; 
        i = j;
        if ((paramInt2 & 0x1) != 0)
          for (paramInt2 = j - 1;; paramInt2--) {
            i = paramInt2;
            if (paramInt2 >= 0) {
              BackStackRecord backStackRecord = this.mBackStack.get(paramInt2);
              if (paramString == null || !paramString.equals(backStackRecord.getName())) {
                i = paramInt2;
                if (paramInt1 >= 0) {
                  i = paramInt2;
                  if (paramInt1 == backStackRecord.mIndex)
                    continue; 
                } 
                break;
              } 
              continue;
            } 
            break;
          }  
      } 
      if (i == this.mBackStack.size() - 1)
        return false; 
      for (paramInt1 = this.mBackStack.size() - 1; paramInt1 > i; paramInt1--) {
        paramArrayList.add(this.mBackStack.remove(paramInt1));
        paramArrayList1.add(Boolean.valueOf(true));
      } 
    } 
    return true;
  }
  
  public void putFragment(Bundle paramBundle, String paramString, Fragment paramFragment) {
    if (paramFragment.mFragmentManager != this)
      throwException(new IllegalStateException("Fragment " + paramFragment + " is not currently in the FragmentManager")); 
    paramBundle.putString(paramString, paramFragment.mWho);
  }
  
  public void registerFragmentLifecycleCallbacks(FragmentLifecycleCallbacks paramFragmentLifecycleCallbacks, boolean paramBoolean) {
    this.mLifecycleCallbacksDispatcher.registerFragmentLifecycleCallbacks(paramFragmentLifecycleCallbacks, paramBoolean);
  }
  
  void removeCancellationSignal(Fragment paramFragment, CancellationSignal paramCancellationSignal) {
    HashSet hashSet = this.mExitAnimationCancellationSignals.get(paramFragment);
    if (hashSet != null && hashSet.remove(paramCancellationSignal) && hashSet.isEmpty()) {
      this.mExitAnimationCancellationSignals.remove(paramFragment);
      if (paramFragment.mState < 5) {
        destroyFragmentView(paramFragment);
        moveToState(paramFragment);
      } 
    } 
  }
  
  void removeFragment(Fragment paramFragment) {
    if (isLoggingEnabled(2))
      Log.v("FragmentManager", "remove: " + paramFragment + " nesting=" + paramFragment.mBackStackNesting); 
    boolean bool = paramFragment.isInBackStack();
    if (!paramFragment.mDetached || (bool ^ true) != 0) {
      this.mFragmentStore.removeFragment(paramFragment);
      if (isMenuAvailable(paramFragment))
        this.mNeedMenuInvalidate = true; 
      paramFragment.mRemoving = true;
      setVisibleRemovingFragment(paramFragment);
    } 
  }
  
  public void removeFragmentOnAttachListener(FragmentOnAttachListener paramFragmentOnAttachListener) {
    this.mOnAttachListeners.remove(paramFragmentOnAttachListener);
  }
  
  public void removeOnBackStackChangedListener(OnBackStackChangedListener paramOnBackStackChangedListener) {
    ArrayList<OnBackStackChangedListener> arrayList = this.mBackStackChangeListeners;
    if (arrayList != null)
      arrayList.remove(paramOnBackStackChangedListener); 
  }
  
  void removeRetainedFragment(Fragment paramFragment) {
    this.mNonConfig.removeRetainedFragment(paramFragment);
  }
  
  void restoreAllState(Parcelable paramParcelable, FragmentManagerNonConfig paramFragmentManagerNonConfig) {
    if (this.mHost instanceof ViewModelStoreOwner)
      throwException(new IllegalStateException("You must use restoreSaveState when your FragmentHostCallback implements ViewModelStoreOwner")); 
    this.mNonConfig.restoreFromSnapshot(paramFragmentManagerNonConfig);
    restoreSaveState(paramParcelable);
  }
  
  void restoreSaveState(Parcelable paramParcelable) {
    if (paramParcelable == null)
      return; 
    FragmentManagerState fragmentManagerState = (FragmentManagerState)paramParcelable;
    if (fragmentManagerState.mActive == null)
      return; 
    this.mFragmentStore.resetActiveFragments();
    for (FragmentState fragmentState : fragmentManagerState.mActive) {
      if (fragmentState != null) {
        FragmentStateManager fragmentStateManager;
        Fragment fragment1 = this.mNonConfig.findRetainedFragmentByWho(fragmentState.mWho);
        if (fragment1 != null) {
          if (isLoggingEnabled(2))
            Log.v("FragmentManager", "restoreSaveState: re-attaching retained " + fragment1); 
          fragmentStateManager = new FragmentStateManager(this.mLifecycleCallbacksDispatcher, this.mFragmentStore, fragment1, fragmentState);
        } else {
          fragmentStateManager = new FragmentStateManager(this.mLifecycleCallbacksDispatcher, this.mFragmentStore, this.mHost.getContext().getClassLoader(), getFragmentFactory(), fragmentState);
        } 
        Fragment fragment2 = fragmentStateManager.getFragment();
        fragment2.mFragmentManager = this;
        if (isLoggingEnabled(2))
          Log.v("FragmentManager", "restoreSaveState: active (" + fragment2.mWho + "): " + fragment2); 
        fragmentStateManager.restoreState(this.mHost.getContext().getClassLoader());
        this.mFragmentStore.makeActive(fragmentStateManager);
        fragmentStateManager.setFragmentManagerState(this.mCurState);
      } 
    } 
    for (Fragment fragment : this.mNonConfig.getRetainedFragments()) {
      if (!this.mFragmentStore.containsActiveFragment(fragment.mWho)) {
        if (isLoggingEnabled(2))
          Log.v("FragmentManager", "Discarding retained Fragment " + fragment + " that was not found in the set of active Fragments " + fragmentManagerState.mActive); 
        this.mNonConfig.removeRetainedFragment(fragment);
        fragment.mFragmentManager = this;
        FragmentStateManager fragmentStateManager = new FragmentStateManager(this.mLifecycleCallbacksDispatcher, this.mFragmentStore, fragment);
        fragmentStateManager.setFragmentManagerState(1);
        fragmentStateManager.moveToExpectedState();
        fragment.mRemoving = true;
        fragmentStateManager.moveToExpectedState();
      } 
    } 
    this.mFragmentStore.restoreAddedFragments(fragmentManagerState.mAdded);
    if (fragmentManagerState.mBackStack != null) {
      this.mBackStack = new ArrayList<>(fragmentManagerState.mBackStack.length);
      for (byte b = 0; b < fragmentManagerState.mBackStack.length; b++) {
        BackStackRecord backStackRecord = fragmentManagerState.mBackStack[b].instantiate(this);
        if (isLoggingEnabled(2)) {
          Log.v("FragmentManager", "restoreAllState: back stack #" + b + " (index " + backStackRecord.mIndex + "): " + backStackRecord);
          PrintWriter printWriter = new PrintWriter(new LogWriter("FragmentManager"));
          backStackRecord.dump("  ", printWriter, false);
          printWriter.close();
        } 
        this.mBackStack.add(backStackRecord);
      } 
    } else {
      this.mBackStack = null;
    } 
    this.mBackStackIndex.set(fragmentManagerState.mBackStackIndex);
    if (fragmentManagerState.mPrimaryNavActiveWho != null) {
      Fragment fragment = findActiveFragment(fragmentManagerState.mPrimaryNavActiveWho);
      this.mPrimaryNav = fragment;
      dispatchParentPrimaryNavigationFragmentChanged(fragment);
    } 
    ArrayList<String> arrayList = fragmentManagerState.mResultKeys;
    if (arrayList != null)
      for (byte b = 0; b < arrayList.size(); b++) {
        Bundle bundle = fragmentManagerState.mResults.get(b);
        bundle.setClassLoader(this.mHost.getContext().getClassLoader());
        this.mResults.put(arrayList.get(b), bundle);
      }  
    this.mLaunchedFragments = new ArrayDeque<>(fragmentManagerState.mLaunchedFragments);
  }
  
  @Deprecated
  FragmentManagerNonConfig retainNonConfig() {
    if (this.mHost instanceof ViewModelStoreOwner)
      throwException(new IllegalStateException("You cannot use retainNonConfig when your FragmentHostCallback implements ViewModelStoreOwner.")); 
    return this.mNonConfig.getSnapshot();
  }
  
  Parcelable saveAllState() {
    forcePostponedTransactions();
    endAnimatingAwayFragments();
    execPendingActions(true);
    this.mStateSaved = true;
    this.mNonConfig.setIsStateSaved(true);
    ArrayList<FragmentState> arrayList1 = this.mFragmentStore.saveActiveFragments();
    if (arrayList1.isEmpty()) {
      if (isLoggingEnabled(2))
        Log.v("FragmentManager", "saveAllState: no fragments!"); 
      return null;
    } 
    ArrayList<String> arrayList = this.mFragmentStore.saveAddedFragments();
    BackStackState[] arrayOfBackStackState2 = null;
    ArrayList<BackStackRecord> arrayList2 = this.mBackStack;
    BackStackState[] arrayOfBackStackState1 = arrayOfBackStackState2;
    if (arrayList2 != null) {
      int i = arrayList2.size();
      arrayOfBackStackState1 = arrayOfBackStackState2;
      if (i > 0) {
        arrayOfBackStackState2 = new BackStackState[i];
        byte b = 0;
        while (true) {
          arrayOfBackStackState1 = arrayOfBackStackState2;
          if (b < i) {
            arrayOfBackStackState2[b] = new BackStackState(this.mBackStack.get(b));
            if (isLoggingEnabled(2))
              Log.v("FragmentManager", "saveAllState: adding back stack #" + b + ": " + this.mBackStack.get(b)); 
            b++;
            continue;
          } 
          break;
        } 
      } 
    } 
    FragmentManagerState fragmentManagerState = new FragmentManagerState();
    fragmentManagerState.mActive = arrayList1;
    fragmentManagerState.mAdded = arrayList;
    fragmentManagerState.mBackStack = arrayOfBackStackState1;
    fragmentManagerState.mBackStackIndex = this.mBackStackIndex.get();
    Fragment fragment = this.mPrimaryNav;
    if (fragment != null)
      fragmentManagerState.mPrimaryNavActiveWho = fragment.mWho; 
    fragmentManagerState.mResultKeys.addAll(this.mResults.keySet());
    fragmentManagerState.mResults.addAll(this.mResults.values());
    fragmentManagerState.mLaunchedFragments = new ArrayList<>(this.mLaunchedFragments);
    return fragmentManagerState;
  }
  
  public Fragment.SavedState saveFragmentInstanceState(Fragment paramFragment) {
    FragmentStateManager fragmentStateManager = this.mFragmentStore.getFragmentStateManager(paramFragment.mWho);
    if (fragmentStateManager == null || !fragmentStateManager.getFragment().equals(paramFragment))
      throwException(new IllegalStateException("Fragment " + paramFragment + " is not currently in the FragmentManager")); 
    return fragmentStateManager.saveInstanceState();
  }
  
  void scheduleCommit() {
    synchronized (this.mPendingActions) {
      boolean bool1;
      ArrayList<StartEnterTransitionListener> arrayList = this.mPostponedTransactions;
      boolean bool2 = false;
      if (arrayList != null && !arrayList.isEmpty()) {
        bool1 = true;
      } else {
        bool1 = false;
      } 
      if (this.mPendingActions.size() == 1)
        bool2 = true; 
      if (bool1 || bool2) {
        this.mHost.getHandler().removeCallbacks(this.mExecCommit);
        this.mHost.getHandler().post(this.mExecCommit);
        updateOnBackPressedCallbackEnabled();
      } 
      return;
    } 
  }
  
  void setExitAnimationOrder(Fragment paramFragment, boolean paramBoolean) {
    ViewGroup viewGroup = getFragmentContainer(paramFragment);
    if (viewGroup != null && viewGroup instanceof FragmentContainerView)
      ((FragmentContainerView)viewGroup).setDrawDisappearingViewsLast(paramBoolean ^ true); 
  }
  
  public void setFragmentFactory(FragmentFactory paramFragmentFactory) {
    this.mFragmentFactory = paramFragmentFactory;
  }
  
  public final void setFragmentResult(String paramString, Bundle paramBundle) {
    LifecycleAwareResultListener lifecycleAwareResultListener = this.mResultListeners.get(paramString);
    if (lifecycleAwareResultListener != null && lifecycleAwareResultListener.isAtLeast(Lifecycle.State.STARTED)) {
      lifecycleAwareResultListener.onFragmentResult(paramString, paramBundle);
    } else {
      this.mResults.put(paramString, paramBundle);
    } 
  }
  
  public final void setFragmentResultListener(final String requestKey, LifecycleOwner paramLifecycleOwner, final FragmentResultListener listener) {
    final Lifecycle lifecycle = paramLifecycleOwner.getLifecycle();
    if (lifecycle.getCurrentState() == Lifecycle.State.DESTROYED)
      return; 
    LifecycleEventObserver lifecycleEventObserver = new LifecycleEventObserver() {
        final FragmentManager this$0;
        
        final Lifecycle val$lifecycle;
        
        final FragmentResultListener val$listener;
        
        final String val$requestKey;
        
        public void onStateChanged(LifecycleOwner param1LifecycleOwner, Lifecycle.Event param1Event) {
          if (param1Event == Lifecycle.Event.ON_START) {
            Bundle bundle = (Bundle)FragmentManager.this.mResults.get(requestKey);
            if (bundle != null) {
              listener.onFragmentResult(requestKey, bundle);
              FragmentManager.this.clearFragmentResult(requestKey);
            } 
          } 
          if (param1Event == Lifecycle.Event.ON_DESTROY) {
            lifecycle.removeObserver((LifecycleObserver)this);
            FragmentManager.this.mResultListeners.remove(requestKey);
          } 
        }
      };
    lifecycle.addObserver((LifecycleObserver)lifecycleEventObserver);
    LifecycleAwareResultListener lifecycleAwareResultListener = this.mResultListeners.put(requestKey, new LifecycleAwareResultListener(lifecycle, listener, lifecycleEventObserver));
    if (lifecycleAwareResultListener != null)
      lifecycleAwareResultListener.removeObserver(); 
  }
  
  void setMaxLifecycle(Fragment paramFragment, Lifecycle.State paramState) {
    if (paramFragment.equals(findActiveFragment(paramFragment.mWho)) && (paramFragment.mHost == null || paramFragment.mFragmentManager == this)) {
      paramFragment.mMaxState = paramState;
      return;
    } 
    throw new IllegalArgumentException("Fragment " + paramFragment + " is not an active fragment of FragmentManager " + this);
  }
  
  void setPrimaryNavigationFragment(Fragment paramFragment) {
    if (paramFragment == null || (paramFragment.equals(findActiveFragment(paramFragment.mWho)) && (paramFragment.mHost == null || paramFragment.mFragmentManager == this))) {
      Fragment fragment = this.mPrimaryNav;
      this.mPrimaryNav = paramFragment;
      dispatchParentPrimaryNavigationFragmentChanged(fragment);
      dispatchParentPrimaryNavigationFragmentChanged(this.mPrimaryNav);
      return;
    } 
    throw new IllegalArgumentException("Fragment " + paramFragment + " is not an active fragment of FragmentManager " + this);
  }
  
  void setSpecialEffectsControllerFactory(SpecialEffectsControllerFactory paramSpecialEffectsControllerFactory) {
    this.mSpecialEffectsControllerFactory = paramSpecialEffectsControllerFactory;
  }
  
  void showFragment(Fragment paramFragment) {
    if (isLoggingEnabled(2))
      Log.v("FragmentManager", "show: " + paramFragment); 
    if (paramFragment.mHidden) {
      paramFragment.mHidden = false;
      paramFragment.mHiddenChanged ^= 0x1;
    } 
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder(128);
    stringBuilder.append("FragmentManager{");
    stringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
    stringBuilder.append(" in ");
    Fragment fragment = this.mParent;
    if (fragment != null) {
      stringBuilder.append(fragment.getClass().getSimpleName());
      stringBuilder.append("{");
      stringBuilder.append(Integer.toHexString(System.identityHashCode(this.mParent)));
      stringBuilder.append("}");
    } else {
      FragmentHostCallback<?> fragmentHostCallback = this.mHost;
      if (fragmentHostCallback != null) {
        stringBuilder.append(fragmentHostCallback.getClass().getSimpleName());
        stringBuilder.append("{");
        stringBuilder.append(Integer.toHexString(System.identityHashCode(this.mHost)));
        stringBuilder.append("}");
      } else {
        stringBuilder.append("null");
      } 
    } 
    stringBuilder.append("}}");
    return stringBuilder.toString();
  }
  
  public void unregisterFragmentLifecycleCallbacks(FragmentLifecycleCallbacks paramFragmentLifecycleCallbacks) {
    this.mLifecycleCallbacksDispatcher.unregisterFragmentLifecycleCallbacks(paramFragmentLifecycleCallbacks);
  }
  
  public static interface BackStackEntry {
    @Deprecated
    CharSequence getBreadCrumbShortTitle();
    
    @Deprecated
    int getBreadCrumbShortTitleRes();
    
    @Deprecated
    CharSequence getBreadCrumbTitle();
    
    @Deprecated
    int getBreadCrumbTitleRes();
    
    int getId();
    
    String getName();
  }
  
  static class FragmentIntentSenderContract extends ActivityResultContract<IntentSenderRequest, ActivityResult> {
    public Intent createIntent(Context param1Context, IntentSenderRequest param1IntentSenderRequest) {
      Intent intent1 = new Intent("androidx.activity.result.contract.action.INTENT_SENDER_REQUEST");
      Intent intent2 = param1IntentSenderRequest.getFillInIntent();
      IntentSenderRequest intentSenderRequest = param1IntentSenderRequest;
      if (intent2 != null) {
        Bundle bundle = intent2.getBundleExtra("androidx.activity.result.contract.extra.ACTIVITY_OPTIONS_BUNDLE");
        intentSenderRequest = param1IntentSenderRequest;
        if (bundle != null) {
          intent1.putExtra("androidx.activity.result.contract.extra.ACTIVITY_OPTIONS_BUNDLE", bundle);
          intent2.removeExtra("androidx.activity.result.contract.extra.ACTIVITY_OPTIONS_BUNDLE");
          intentSenderRequest = param1IntentSenderRequest;
          if (intent2.getBooleanExtra("androidx.fragment.extra.ACTIVITY_OPTIONS_BUNDLE", false))
            intentSenderRequest = (new IntentSenderRequest.Builder(param1IntentSenderRequest.getIntentSender())).setFillInIntent(null).setFlags(param1IntentSenderRequest.getFlagsValues(), param1IntentSenderRequest.getFlagsMask()).build(); 
        } 
      } 
      intent1.putExtra("androidx.activity.result.contract.extra.INTENT_SENDER_REQUEST", (Parcelable)intentSenderRequest);
      if (FragmentManager.isLoggingEnabled(2))
        Log.v("FragmentManager", "CreateIntent created the following intent: " + intent1); 
      return intent1;
    }
    
    public ActivityResult parseResult(int param1Int, Intent param1Intent) {
      return new ActivityResult(param1Int, param1Intent);
    }
  }
  
  public static abstract class FragmentLifecycleCallbacks {
    @Deprecated
    public void onFragmentActivityCreated(FragmentManager param1FragmentManager, Fragment param1Fragment, Bundle param1Bundle) {}
    
    public void onFragmentAttached(FragmentManager param1FragmentManager, Fragment param1Fragment, Context param1Context) {}
    
    public void onFragmentCreated(FragmentManager param1FragmentManager, Fragment param1Fragment, Bundle param1Bundle) {}
    
    public void onFragmentDestroyed(FragmentManager param1FragmentManager, Fragment param1Fragment) {}
    
    public void onFragmentDetached(FragmentManager param1FragmentManager, Fragment param1Fragment) {}
    
    public void onFragmentPaused(FragmentManager param1FragmentManager, Fragment param1Fragment) {}
    
    public void onFragmentPreAttached(FragmentManager param1FragmentManager, Fragment param1Fragment, Context param1Context) {}
    
    public void onFragmentPreCreated(FragmentManager param1FragmentManager, Fragment param1Fragment, Bundle param1Bundle) {}
    
    public void onFragmentResumed(FragmentManager param1FragmentManager, Fragment param1Fragment) {}
    
    public void onFragmentSaveInstanceState(FragmentManager param1FragmentManager, Fragment param1Fragment, Bundle param1Bundle) {}
    
    public void onFragmentStarted(FragmentManager param1FragmentManager, Fragment param1Fragment) {}
    
    public void onFragmentStopped(FragmentManager param1FragmentManager, Fragment param1Fragment) {}
    
    public void onFragmentViewCreated(FragmentManager param1FragmentManager, Fragment param1Fragment, View param1View, Bundle param1Bundle) {}
    
    public void onFragmentViewDestroyed(FragmentManager param1FragmentManager, Fragment param1Fragment) {}
  }
  
  static class LaunchedFragmentInfo implements Parcelable {
    public static final Parcelable.Creator<LaunchedFragmentInfo> CREATOR = new Parcelable.Creator<LaunchedFragmentInfo>() {
        public FragmentManager.LaunchedFragmentInfo createFromParcel(Parcel param2Parcel) {
          return new FragmentManager.LaunchedFragmentInfo(param2Parcel);
        }
        
        public FragmentManager.LaunchedFragmentInfo[] newArray(int param2Int) {
          return new FragmentManager.LaunchedFragmentInfo[param2Int];
        }
      };
    
    int mRequestCode;
    
    String mWho;
    
    LaunchedFragmentInfo(Parcel param1Parcel) {
      this.mWho = param1Parcel.readString();
      this.mRequestCode = param1Parcel.readInt();
    }
    
    LaunchedFragmentInfo(String param1String, int param1Int) {
      this.mWho = param1String;
      this.mRequestCode = param1Int;
    }
    
    public int describeContents() {
      return 0;
    }
    
    public void writeToParcel(Parcel param1Parcel, int param1Int) {
      param1Parcel.writeString(this.mWho);
      param1Parcel.writeInt(this.mRequestCode);
    }
  }
  
  class null implements Parcelable.Creator<LaunchedFragmentInfo> {
    public FragmentManager.LaunchedFragmentInfo createFromParcel(Parcel param1Parcel) {
      return new FragmentManager.LaunchedFragmentInfo(param1Parcel);
    }
    
    public FragmentManager.LaunchedFragmentInfo[] newArray(int param1Int) {
      return new FragmentManager.LaunchedFragmentInfo[param1Int];
    }
  }
  
  private static class LifecycleAwareResultListener implements FragmentResultListener {
    private final Lifecycle mLifecycle;
    
    private final FragmentResultListener mListener;
    
    private final LifecycleEventObserver mObserver;
    
    LifecycleAwareResultListener(Lifecycle param1Lifecycle, FragmentResultListener param1FragmentResultListener, LifecycleEventObserver param1LifecycleEventObserver) {
      this.mLifecycle = param1Lifecycle;
      this.mListener = param1FragmentResultListener;
      this.mObserver = param1LifecycleEventObserver;
    }
    
    public boolean isAtLeast(Lifecycle.State param1State) {
      return this.mLifecycle.getCurrentState().isAtLeast(param1State);
    }
    
    public void onFragmentResult(String param1String, Bundle param1Bundle) {
      this.mListener.onFragmentResult(param1String, param1Bundle);
    }
    
    public void removeObserver() {
      this.mLifecycle.removeObserver((LifecycleObserver)this.mObserver);
    }
  }
  
  public static interface OnBackStackChangedListener {
    void onBackStackChanged();
  }
  
  static interface OpGenerator {
    boolean generateOps(ArrayList<BackStackRecord> param1ArrayList, ArrayList<Boolean> param1ArrayList1);
  }
  
  private class PopBackStackState implements OpGenerator {
    final int mFlags;
    
    final int mId;
    
    final String mName;
    
    final FragmentManager this$0;
    
    PopBackStackState(String param1String, int param1Int1, int param1Int2) {
      this.mName = param1String;
      this.mId = param1Int1;
      this.mFlags = param1Int2;
    }
    
    public boolean generateOps(ArrayList<BackStackRecord> param1ArrayList, ArrayList<Boolean> param1ArrayList1) {
      return (FragmentManager.this.mPrimaryNav != null && this.mId < 0 && this.mName == null && FragmentManager.this.mPrimaryNav.getChildFragmentManager().popBackStackImmediate()) ? false : FragmentManager.this.popBackStackState(param1ArrayList, param1ArrayList1, this.mName, this.mId, this.mFlags);
    }
  }
  
  static class StartEnterTransitionListener implements Fragment.OnStartEnterTransitionListener {
    final boolean mIsBack;
    
    private int mNumPostponed;
    
    final BackStackRecord mRecord;
    
    StartEnterTransitionListener(BackStackRecord param1BackStackRecord, boolean param1Boolean) {
      this.mIsBack = param1Boolean;
      this.mRecord = param1BackStackRecord;
    }
    
    void cancelTransaction() {
      this.mRecord.mManager.completeExecute(this.mRecord, this.mIsBack, false, false);
    }
    
    void completeTransaction() {
      int i = this.mNumPostponed;
      boolean bool = false;
      if (i > 0) {
        i = 1;
      } else {
        i = 0;
      } 
      for (Fragment fragment : this.mRecord.mManager.getFragments()) {
        fragment.setOnStartEnterTransitionListener(null);
        if (i != 0 && fragment.isPostponed())
          fragment.startPostponedEnterTransition(); 
      } 
      FragmentManager fragmentManager = this.mRecord.mManager;
      BackStackRecord backStackRecord = this.mRecord;
      boolean bool1 = this.mIsBack;
      if (i == 0)
        bool = true; 
      fragmentManager.completeExecute(backStackRecord, bool1, bool, true);
    }
    
    public boolean isReady() {
      boolean bool;
      if (this.mNumPostponed == 0) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public void onStartEnterTransition() {
      int i = this.mNumPostponed - 1;
      this.mNumPostponed = i;
      if (i != 0)
        return; 
      this.mRecord.mManager.scheduleCommit();
    }
    
    public void startListening() {
      this.mNumPostponed++;
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\fragment\app\FragmentManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */