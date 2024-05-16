package androidx.recyclerview.widget;

import android.animation.LayoutTransition;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.Observable;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.Display;
import android.view.FocusFinder;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.Interpolator;
import android.widget.EdgeEffect;
import android.widget.OverScroller;
import androidx.core.os.TraceCompat;
import androidx.core.util.Preconditions;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.MotionEventCompat;
import androidx.core.view.NestedScrollingChild2;
import androidx.core.view.NestedScrollingChild3;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.core.view.ScrollingView;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewConfigurationCompat;
import androidx.core.view.accessibility.AccessibilityEventCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.widget.EdgeEffectCompat;
import androidx.customview.view.AbsSavedState;
import androidx.recyclerview.R;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecyclerView extends ViewGroup implements ScrollingView, NestedScrollingChild2, NestedScrollingChild3 {
  static {
    if (Build.VERSION.SDK_INT == 18 || Build.VERSION.SDK_INT == 19 || Build.VERSION.SDK_INT == 20) {
      bool = true;
    } else {
      bool = false;
    } 
    FORCE_INVALIDATE_DISPLAY_LIST = bool;
    if (Build.VERSION.SDK_INT >= 23) {
      bool = true;
    } else {
      bool = false;
    } 
    ALLOW_SIZE_IN_UNSPECIFIED_SPEC = bool;
    if (Build.VERSION.SDK_INT >= 16) {
      bool = true;
    } else {
      bool = false;
    } 
    POST_UPDATES_ON_ANIMATION = bool;
    if (Build.VERSION.SDK_INT >= 21) {
      bool = true;
    } else {
      bool = false;
    } 
    ALLOW_THREAD_GAP_WORK = bool;
    if (Build.VERSION.SDK_INT <= 15) {
      bool = true;
    } else {
      bool = false;
    } 
    FORCE_ABS_FOCUS_SEARCH_DIRECTION = bool;
    if (Build.VERSION.SDK_INT <= 15) {
      bool = true;
    } else {
      bool = false;
    } 
    IGNORE_DETACHED_FOCUSED_CHILD = bool;
    LAYOUT_MANAGER_CONSTRUCTOR_SIGNATURE = new Class[] { Context.class, AttributeSet.class, int.class, int.class };
    sQuinticInterpolator = new Interpolator() {
        public float getInterpolation(float param1Float) {
          param1Float--;
          return param1Float * param1Float * param1Float * param1Float * param1Float + 1.0F;
        }
      };
  }
  
  public RecyclerView(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public RecyclerView(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, R.attr.recyclerViewStyle);
  }
  
  public RecyclerView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    GapWorker.LayoutPrefetchRegistryImpl layoutPrefetchRegistryImpl;
    this.mObserver = new RecyclerViewDataObserver();
    this.mRecycler = new Recycler();
    this.mViewInfoStore = new ViewInfoStore();
    this.mUpdateChildViewsRunnable = new Runnable() {
        final RecyclerView this$0;
        
        public void run() {
          if (!RecyclerView.this.mFirstLayoutComplete || RecyclerView.this.isLayoutRequested())
            return; 
          if (!RecyclerView.this.mIsAttached) {
            RecyclerView.this.requestLayout();
            return;
          } 
          if (RecyclerView.this.mLayoutSuppressed) {
            RecyclerView.this.mLayoutWasDefered = true;
            return;
          } 
          RecyclerView.this.consumePendingUpdateOperations();
        }
      };
    this.mTempRect = new Rect();
    this.mTempRect2 = new Rect();
    this.mTempRectF = new RectF();
    this.mItemDecorations = new ArrayList<>();
    this.mOnItemTouchListeners = new ArrayList<>();
    this.mInterceptRequestLayoutDepth = 0;
    this.mDataSetHasChangedAfterLayout = false;
    this.mDispatchItemsChangedEvent = false;
    this.mLayoutOrScrollCounter = 0;
    this.mDispatchScrollCounter = 0;
    this.mEdgeEffectFactory = new EdgeEffectFactory();
    this.mItemAnimator = new DefaultItemAnimator();
    this.mScrollState = 0;
    this.mScrollPointerId = -1;
    this.mScaledHorizontalScrollFactor = Float.MIN_VALUE;
    this.mScaledVerticalScrollFactor = Float.MIN_VALUE;
    this.mPreserveFocusAfterLayout = true;
    this.mViewFlinger = new ViewFlinger();
    if (ALLOW_THREAD_GAP_WORK) {
      layoutPrefetchRegistryImpl = new GapWorker.LayoutPrefetchRegistryImpl();
    } else {
      layoutPrefetchRegistryImpl = null;
    } 
    this.mPrefetchRegistry = layoutPrefetchRegistryImpl;
    this.mState = new State();
    this.mItemsAddedOrRemoved = false;
    this.mItemsChanged = false;
    this.mItemAnimatorListener = new ItemAnimatorRestoreListener();
    this.mPostedAnimatorRunner = false;
    this.mMinMaxLayoutPositions = new int[2];
    this.mScrollOffset = new int[2];
    this.mNestedOffsets = new int[2];
    this.mReusableIntPair = new int[2];
    this.mPendingAccessibilityImportanceChange = new ArrayList<>();
    this.mItemAnimatorRunner = new Runnable() {
        final RecyclerView this$0;
        
        public void run() {
          if (RecyclerView.this.mItemAnimator != null)
            RecyclerView.this.mItemAnimator.runPendingAnimations(); 
          RecyclerView.this.mPostedAnimatorRunner = false;
        }
      };
    this.mViewInfoProcessCallback = new ViewInfoStore.ProcessCallback() {
        final RecyclerView this$0;
        
        public void processAppeared(RecyclerView.ViewHolder param1ViewHolder, RecyclerView.ItemAnimator.ItemHolderInfo param1ItemHolderInfo1, RecyclerView.ItemAnimator.ItemHolderInfo param1ItemHolderInfo2) {
          RecyclerView.this.animateAppearance(param1ViewHolder, param1ItemHolderInfo1, param1ItemHolderInfo2);
        }
        
        public void processDisappeared(RecyclerView.ViewHolder param1ViewHolder, RecyclerView.ItemAnimator.ItemHolderInfo param1ItemHolderInfo1, RecyclerView.ItemAnimator.ItemHolderInfo param1ItemHolderInfo2) {
          RecyclerView.this.mRecycler.unscrapView(param1ViewHolder);
          RecyclerView.this.animateDisappearance(param1ViewHolder, param1ItemHolderInfo1, param1ItemHolderInfo2);
        }
        
        public void processPersistent(RecyclerView.ViewHolder param1ViewHolder, RecyclerView.ItemAnimator.ItemHolderInfo param1ItemHolderInfo1, RecyclerView.ItemAnimator.ItemHolderInfo param1ItemHolderInfo2) {
          param1ViewHolder.setIsRecyclable(false);
          if (RecyclerView.this.mDataSetHasChangedAfterLayout) {
            if (RecyclerView.this.mItemAnimator.animateChange(param1ViewHolder, param1ViewHolder, param1ItemHolderInfo1, param1ItemHolderInfo2))
              RecyclerView.this.postAnimationRunner(); 
          } else if (RecyclerView.this.mItemAnimator.animatePersistence(param1ViewHolder, param1ItemHolderInfo1, param1ItemHolderInfo2)) {
            RecyclerView.this.postAnimationRunner();
          } 
        }
        
        public void unused(RecyclerView.ViewHolder param1ViewHolder) {
          RecyclerView.this.mLayout.removeAndRecycleView(param1ViewHolder.itemView, RecyclerView.this.mRecycler);
        }
      };
    setScrollContainer(true);
    setFocusableInTouchMode(true);
    ViewConfiguration viewConfiguration = ViewConfiguration.get(paramContext);
    this.mTouchSlop = viewConfiguration.getScaledTouchSlop();
    this.mScaledHorizontalScrollFactor = ViewConfigurationCompat.getScaledHorizontalScrollFactor(viewConfiguration, paramContext);
    this.mScaledVerticalScrollFactor = ViewConfigurationCompat.getScaledVerticalScrollFactor(viewConfiguration, paramContext);
    this.mMinFlingVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
    this.mMaxFlingVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
    if (getOverScrollMode() == 2) {
      bool = true;
    } else {
      bool = false;
    } 
    setWillNotDraw(bool);
    this.mItemAnimator.setListener(this.mItemAnimatorListener);
    initAdapterManager();
    initChildrenHelper();
    initAutofill();
    if (ViewCompat.getImportantForAccessibility((View)this) == 0)
      ViewCompat.setImportantForAccessibility((View)this, 1); 
    this.mAccessibilityManager = (AccessibilityManager)getContext().getSystemService("accessibility");
    setAccessibilityDelegateCompat(new RecyclerViewAccessibilityDelegate(this));
    TypedArray typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.RecyclerView, paramInt, 0);
    if (Build.VERSION.SDK_INT >= 29)
      saveAttributeDataForStyleable(paramContext, R.styleable.RecyclerView, paramAttributeSet, typedArray, paramInt, 0); 
    String str = typedArray.getString(R.styleable.RecyclerView_layoutManager);
    if (typedArray.getInt(R.styleable.RecyclerView_android_descendantFocusability, -1) == -1)
      setDescendantFocusability(262144); 
    this.mClipToPadding = typedArray.getBoolean(R.styleable.RecyclerView_android_clipToPadding, true);
    boolean bool = typedArray.getBoolean(R.styleable.RecyclerView_fastScrollEnabled, false);
    this.mEnableFastScroller = bool;
    if (bool)
      initFastScroller((StateListDrawable)typedArray.getDrawable(R.styleable.RecyclerView_fastScrollVerticalThumbDrawable), typedArray.getDrawable(R.styleable.RecyclerView_fastScrollVerticalTrackDrawable), (StateListDrawable)typedArray.getDrawable(R.styleable.RecyclerView_fastScrollHorizontalThumbDrawable), typedArray.getDrawable(R.styleable.RecyclerView_fastScrollHorizontalTrackDrawable)); 
    typedArray.recycle();
    createLayoutManager(paramContext, str, paramAttributeSet, paramInt, 0);
    bool = true;
    if (Build.VERSION.SDK_INT >= 21) {
      int[] arrayOfInt = NESTED_SCROLLING_ATTRS;
      TypedArray typedArray1 = paramContext.obtainStyledAttributes(paramAttributeSet, arrayOfInt, paramInt, 0);
      if (Build.VERSION.SDK_INT >= 29)
        saveAttributeDataForStyleable(paramContext, arrayOfInt, paramAttributeSet, typedArray1, paramInt, 0); 
      bool = typedArray1.getBoolean(0, true);
      typedArray1.recycle();
    } 
    setNestedScrollingEnabled(bool);
  }
  
  private void addAnimatingView(ViewHolder paramViewHolder) {
    boolean bool;
    View view = paramViewHolder.itemView;
    if (view.getParent() == this) {
      bool = true;
    } else {
      bool = false;
    } 
    this.mRecycler.unscrapView(getChildViewHolder(view));
    if (paramViewHolder.isTmpDetached()) {
      this.mChildHelper.attachViewToParent(view, -1, view.getLayoutParams(), true);
    } else if (!bool) {
      this.mChildHelper.addView(view, true);
    } else {
      this.mChildHelper.hide(view);
    } 
  }
  
  private void animateChange(ViewHolder paramViewHolder1, ViewHolder paramViewHolder2, ItemAnimator.ItemHolderInfo paramItemHolderInfo1, ItemAnimator.ItemHolderInfo paramItemHolderInfo2, boolean paramBoolean1, boolean paramBoolean2) {
    paramViewHolder1.setIsRecyclable(false);
    if (paramBoolean1)
      addAnimatingView(paramViewHolder1); 
    if (paramViewHolder1 != paramViewHolder2) {
      if (paramBoolean2)
        addAnimatingView(paramViewHolder2); 
      paramViewHolder1.mShadowedHolder = paramViewHolder2;
      addAnimatingView(paramViewHolder1);
      this.mRecycler.unscrapView(paramViewHolder1);
      paramViewHolder2.setIsRecyclable(false);
      paramViewHolder2.mShadowingHolder = paramViewHolder1;
    } 
    if (this.mItemAnimator.animateChange(paramViewHolder1, paramViewHolder2, paramItemHolderInfo1, paramItemHolderInfo2))
      postAnimationRunner(); 
  }
  
  private void cancelScroll() {
    resetScroll();
    setScrollState(0);
  }
  
  static void clearNestedRecyclerViewIfNotNested(ViewHolder paramViewHolder) {
    if (paramViewHolder.mNestedRecyclerView != null) {
      View view = (View)paramViewHolder.mNestedRecyclerView.get();
      while (view != null) {
        if (view == paramViewHolder.itemView)
          return; 
        ViewParent viewParent = view.getParent();
        if (viewParent instanceof View) {
          View view1 = (View)viewParent;
          continue;
        } 
        viewParent = null;
      } 
      paramViewHolder.mNestedRecyclerView = null;
    } 
  }
  
  private void createLayoutManager(Context paramContext, String paramString, AttributeSet paramAttributeSet, int paramInt1, int paramInt2) {
    if (paramString != null) {
      paramString = paramString.trim();
      if (!paramString.isEmpty()) {
        String str = getFullClassName(paramContext, paramString);
        try {
          Object[] arrayOfObject;
          StringBuilder stringBuilder;
          if (isInEditMode()) {
            classLoader = getClass().getClassLoader();
          } else {
            classLoader = paramContext.getClassLoader();
          } 
          Class<? extends LayoutManager> clazz = Class.forName(str, false, classLoader).asSubclass(LayoutManager.class);
          ClassLoader classLoader = null;
          try {
            Constructor<? extends LayoutManager> constructor2 = clazz.getConstructor(LAYOUT_MANAGER_CONSTRUCTOR_SIGNATURE);
            arrayOfObject = new Object[] { paramContext, paramAttributeSet, Integer.valueOf(paramInt1), Integer.valueOf(paramInt2) };
            Constructor<? extends LayoutManager> constructor1 = constructor2;
          } catch (NoSuchMethodException noSuchMethodException1) {
            try {
              Constructor<? extends LayoutManager> constructor = clazz.getConstructor(new Class[0]);
              constructor.setAccessible(true);
              setLayoutManager(constructor.newInstance(arrayOfObject));
            } catch (NoSuchMethodException noSuchMethodException) {
              noSuchMethodException.initCause(noSuchMethodException1);
              IllegalStateException illegalStateException = new IllegalStateException();
              stringBuilder = new StringBuilder();
              this();
              this(stringBuilder.append(paramAttributeSet.getPositionDescription()).append(": Error creating LayoutManager ").append(str).toString(), noSuchMethodException);
              throw illegalStateException;
            } 
          } 
          noSuchMethodException.setAccessible(true);
          setLayoutManager(noSuchMethodException.newInstance((Object[])stringBuilder));
        } catch (ClassNotFoundException classNotFoundException) {
          throw new IllegalStateException(paramAttributeSet.getPositionDescription() + ": Unable to find LayoutManager " + str, classNotFoundException);
        } catch (InvocationTargetException invocationTargetException) {
          throw new IllegalStateException(paramAttributeSet.getPositionDescription() + ": Could not instantiate the LayoutManager: " + str, invocationTargetException);
        } catch (InstantiationException instantiationException) {
          throw new IllegalStateException(paramAttributeSet.getPositionDescription() + ": Could not instantiate the LayoutManager: " + str, instantiationException);
        } catch (IllegalAccessException illegalAccessException) {
          throw new IllegalStateException(paramAttributeSet.getPositionDescription() + ": Cannot access non-public constructor " + str, illegalAccessException);
        } catch (ClassCastException classCastException) {
          throw new IllegalStateException(paramAttributeSet.getPositionDescription() + ": Class is not a LayoutManager " + str, classCastException);
        } 
      } 
    } 
  }
  
  private boolean didChildRangeChange(int paramInt1, int paramInt2) {
    findMinMaxChildLayoutPositions(this.mMinMaxLayoutPositions);
    int[] arrayOfInt = this.mMinMaxLayoutPositions;
    boolean bool = false;
    if (arrayOfInt[0] != paramInt1 || arrayOfInt[1] != paramInt2)
      bool = true; 
    return bool;
  }
  
  private void dispatchContentChangedIfNecessary() {
    int i = this.mEatenAccessibilityChangeFlags;
    this.mEatenAccessibilityChangeFlags = 0;
    if (i != 0 && isAccessibilityEnabled()) {
      AccessibilityEvent accessibilityEvent = AccessibilityEvent.obtain();
      accessibilityEvent.setEventType(2048);
      AccessibilityEventCompat.setContentChangeTypes(accessibilityEvent, i);
      sendAccessibilityEventUnchecked(accessibilityEvent);
    } 
  }
  
  private void dispatchLayoutStep1() {
    State state = this.mState;
    boolean bool = true;
    state.assertLayoutStep(1);
    fillRemainingScrollValues(this.mState);
    this.mState.mIsMeasuring = false;
    startInterceptRequestLayout();
    this.mViewInfoStore.clear();
    onEnterLayoutOrScroll();
    processAdapterUpdatesAndSetAnimationFlags();
    saveFocusInfo();
    state = this.mState;
    if (!state.mRunSimpleAnimations || !this.mItemsChanged)
      bool = false; 
    state.mTrackOldChangeHolders = bool;
    this.mItemsChanged = false;
    this.mItemsAddedOrRemoved = false;
    state = this.mState;
    state.mInPreLayout = state.mRunPredictiveAnimations;
    this.mState.mItemCount = this.mAdapter.getItemCount();
    findMinMaxChildLayoutPositions(this.mMinMaxLayoutPositions);
    if (this.mState.mRunSimpleAnimations) {
      int i = this.mChildHelper.getChildCount();
      for (byte b = 0; b < i; b++) {
        ViewHolder viewHolder = getChildViewHolderInt(this.mChildHelper.getChildAt(b));
        if (!viewHolder.shouldIgnore() && (!viewHolder.isInvalid() || this.mAdapter.hasStableIds())) {
          ItemAnimator.ItemHolderInfo itemHolderInfo = this.mItemAnimator.recordPreLayoutInformation(this.mState, viewHolder, ItemAnimator.buildAdapterChangeFlagsForAnimations(viewHolder), viewHolder.getUnmodifiedPayloads());
          this.mViewInfoStore.addToPreLayout(viewHolder, itemHolderInfo);
          if (this.mState.mTrackOldChangeHolders && viewHolder.isUpdated() && !viewHolder.isRemoved() && !viewHolder.shouldIgnore() && !viewHolder.isInvalid()) {
            long l = getChangedHolderKey(viewHolder);
            this.mViewInfoStore.addToOldChangeHolders(l, viewHolder);
          } 
        } 
      } 
    } 
    if (this.mState.mRunPredictiveAnimations) {
      saveOldPositions();
      bool = this.mState.mStructureChanged;
      this.mState.mStructureChanged = false;
      this.mLayout.onLayoutChildren(this.mRecycler, this.mState);
      this.mState.mStructureChanged = bool;
      for (byte b = 0; b < this.mChildHelper.getChildCount(); b++) {
        ViewHolder viewHolder = getChildViewHolderInt(this.mChildHelper.getChildAt(b));
        if (!viewHolder.shouldIgnore() && !this.mViewInfoStore.isInPreLayout(viewHolder)) {
          int j = ItemAnimator.buildAdapterChangeFlagsForAnimations(viewHolder);
          bool = viewHolder.hasAnyOfTheFlags(8192);
          int i = j;
          if (!bool)
            i = j | 0x1000; 
          ItemAnimator.ItemHolderInfo itemHolderInfo = this.mItemAnimator.recordPreLayoutInformation(this.mState, viewHolder, i, viewHolder.getUnmodifiedPayloads());
          if (bool) {
            recordAnimationInfoIfBouncedHiddenView(viewHolder, itemHolderInfo);
          } else {
            this.mViewInfoStore.addToAppearedInPreLayoutHolders(viewHolder, itemHolderInfo);
          } 
        } 
      } 
      clearOldPositions();
    } else {
      clearOldPositions();
    } 
    onExitLayoutOrScroll();
    stopInterceptRequestLayout(false);
    this.mState.mLayoutStep = 2;
  }
  
  private void dispatchLayoutStep2() {
    boolean bool;
    startInterceptRequestLayout();
    onEnterLayoutOrScroll();
    this.mState.assertLayoutStep(6);
    this.mAdapterHelper.consumeUpdatesInOnePass();
    this.mState.mItemCount = this.mAdapter.getItemCount();
    this.mState.mDeletedInvisibleItemCountSincePreviousLayout = 0;
    this.mState.mInPreLayout = false;
    this.mLayout.onLayoutChildren(this.mRecycler, this.mState);
    this.mState.mStructureChanged = false;
    this.mPendingSavedState = null;
    State state = this.mState;
    if (state.mRunSimpleAnimations && this.mItemAnimator != null) {
      bool = true;
    } else {
      bool = false;
    } 
    state.mRunSimpleAnimations = bool;
    this.mState.mLayoutStep = 4;
    onExitLayoutOrScroll();
    stopInterceptRequestLayout(false);
  }
  
  private void dispatchLayoutStep3() {
    this.mState.assertLayoutStep(4);
    startInterceptRequestLayout();
    onEnterLayoutOrScroll();
    this.mState.mLayoutStep = 1;
    if (this.mState.mRunSimpleAnimations) {
      for (int i = this.mChildHelper.getChildCount() - 1; i >= 0; i--) {
        ViewHolder viewHolder = getChildViewHolderInt(this.mChildHelper.getChildAt(i));
        if (!viewHolder.shouldIgnore()) {
          long l = getChangedHolderKey(viewHolder);
          ItemAnimator.ItemHolderInfo itemHolderInfo = this.mItemAnimator.recordPostLayoutInformation(this.mState, viewHolder);
          ViewHolder viewHolder1 = this.mViewInfoStore.getFromOldChangeHolders(l);
          if (viewHolder1 != null && !viewHolder1.shouldIgnore()) {
            boolean bool1 = this.mViewInfoStore.isDisappearing(viewHolder1);
            boolean bool2 = this.mViewInfoStore.isDisappearing(viewHolder);
            if (bool1 && viewHolder1 == viewHolder) {
              this.mViewInfoStore.addToPostLayout(viewHolder, itemHolderInfo);
            } else {
              ItemAnimator.ItemHolderInfo itemHolderInfo1 = this.mViewInfoStore.popFromPreLayout(viewHolder1);
              this.mViewInfoStore.addToPostLayout(viewHolder, itemHolderInfo);
              itemHolderInfo = this.mViewInfoStore.popFromPostLayout(viewHolder);
              if (itemHolderInfo1 == null) {
                handleMissingPreInfoForChangeError(l, viewHolder, viewHolder1);
              } else {
                animateChange(viewHolder1, viewHolder, itemHolderInfo1, itemHolderInfo, bool1, bool2);
              } 
            } 
          } else {
            this.mViewInfoStore.addToPostLayout(viewHolder, itemHolderInfo);
          } 
        } 
      } 
      this.mViewInfoStore.process(this.mViewInfoProcessCallback);
    } 
    this.mLayout.removeAndRecycleScrapInt(this.mRecycler);
    State state = this.mState;
    state.mPreviousLayoutItemCount = state.mItemCount;
    this.mDataSetHasChangedAfterLayout = false;
    this.mDispatchItemsChangedEvent = false;
    this.mState.mRunSimpleAnimations = false;
    this.mState.mRunPredictiveAnimations = false;
    this.mLayout.mRequestedSimpleAnimations = false;
    if (this.mRecycler.mChangedScrap != null)
      this.mRecycler.mChangedScrap.clear(); 
    if (this.mLayout.mPrefetchMaxObservedInInitialPrefetch) {
      this.mLayout.mPrefetchMaxCountObserved = 0;
      this.mLayout.mPrefetchMaxObservedInInitialPrefetch = false;
      this.mRecycler.updateViewCacheSize();
    } 
    this.mLayout.onLayoutCompleted(this.mState);
    onExitLayoutOrScroll();
    stopInterceptRequestLayout(false);
    this.mViewInfoStore.clear();
    int[] arrayOfInt = this.mMinMaxLayoutPositions;
    if (didChildRangeChange(arrayOfInt[0], arrayOfInt[1]))
      dispatchOnScrolled(0, 0); 
    recoverFocusFromState();
    resetFocusInfo();
  }
  
  private boolean dispatchToOnItemTouchListeners(MotionEvent paramMotionEvent) {
    OnItemTouchListener onItemTouchListener = this.mInterceptingOnItemTouchListener;
    if (onItemTouchListener == null)
      return (paramMotionEvent.getAction() == 0) ? false : findInterceptingOnItemTouchListener(paramMotionEvent); 
    onItemTouchListener.onTouchEvent(this, paramMotionEvent);
    int i = paramMotionEvent.getAction();
    if (i == 3 || i == 1)
      this.mInterceptingOnItemTouchListener = null; 
    return true;
  }
  
  private boolean findInterceptingOnItemTouchListener(MotionEvent paramMotionEvent) {
    int i = paramMotionEvent.getAction();
    int j = this.mOnItemTouchListeners.size();
    for (byte b = 0; b < j; b++) {
      OnItemTouchListener onItemTouchListener = this.mOnItemTouchListeners.get(b);
      if (onItemTouchListener.onInterceptTouchEvent(this, paramMotionEvent) && i != 3) {
        this.mInterceptingOnItemTouchListener = onItemTouchListener;
        return true;
      } 
    } 
    return false;
  }
  
  private void findMinMaxChildLayoutPositions(int[] paramArrayOfint) {
    int k = this.mChildHelper.getChildCount();
    if (k == 0) {
      paramArrayOfint[0] = -1;
      paramArrayOfint[1] = -1;
      return;
    } 
    int i = Integer.MAX_VALUE;
    int j = Integer.MIN_VALUE;
    byte b = 0;
    while (b < k) {
      int m;
      ViewHolder viewHolder = getChildViewHolderInt(this.mChildHelper.getChildAt(b));
      if (viewHolder.shouldIgnore()) {
        m = j;
      } else {
        int i1 = viewHolder.getLayoutPosition();
        int n = i;
        if (i1 < i)
          n = i1; 
        i = n;
        m = j;
        if (i1 > j) {
          m = i1;
          i = n;
        } 
      } 
      b++;
      j = m;
    } 
    paramArrayOfint[0] = i;
    paramArrayOfint[1] = j;
  }
  
  static RecyclerView findNestedRecyclerView(View paramView) {
    if (!(paramView instanceof ViewGroup))
      return null; 
    if (paramView instanceof RecyclerView)
      return (RecyclerView)paramView; 
    ViewGroup viewGroup = (ViewGroup)paramView;
    int i = viewGroup.getChildCount();
    for (byte b = 0; b < i; b++) {
      RecyclerView recyclerView = findNestedRecyclerView(viewGroup.getChildAt(b));
      if (recyclerView != null)
        return recyclerView; 
    } 
    return null;
  }
  
  private View findNextViewToFocus() {
    if (this.mState.mFocusedItemPosition != -1) {
      i = this.mState.mFocusedItemPosition;
    } else {
      i = 0;
    } 
    int k = this.mState.getItemCount();
    for (int j = i; j < k; j++) {
      ViewHolder viewHolder = findViewHolderForAdapterPosition(j);
      if (viewHolder == null)
        break; 
      if (viewHolder.itemView.hasFocusable())
        return viewHolder.itemView; 
    } 
    for (int i = Math.min(k, i) - 1; i >= 0; i--) {
      ViewHolder viewHolder = findViewHolderForAdapterPosition(i);
      if (viewHolder == null)
        return null; 
      if (viewHolder.itemView.hasFocusable())
        return viewHolder.itemView; 
    } 
    return null;
  }
  
  static ViewHolder getChildViewHolderInt(View paramView) {
    return (paramView == null) ? null : ((LayoutParams)paramView.getLayoutParams()).mViewHolder;
  }
  
  static void getDecoratedBoundsWithMarginsInt(View paramView, Rect paramRect) {
    LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
    Rect rect = layoutParams.mDecorInsets;
    paramRect.set(paramView.getLeft() - rect.left - layoutParams.leftMargin, paramView.getTop() - rect.top - layoutParams.topMargin, paramView.getRight() + rect.right + layoutParams.rightMargin, paramView.getBottom() + rect.bottom + layoutParams.bottomMargin);
  }
  
  private int getDeepestFocusedViewWithId(View paramView) {
    int i = paramView.getId();
    while (!paramView.isFocused() && paramView instanceof ViewGroup && paramView.hasFocus()) {
      paramView = ((ViewGroup)paramView).getFocusedChild();
      if (paramView.getId() != -1)
        i = paramView.getId(); 
    } 
    return i;
  }
  
  private String getFullClassName(Context paramContext, String paramString) {
    return (paramString.charAt(0) == '.') ? (paramContext.getPackageName() + paramString) : (paramString.contains(".") ? paramString : (RecyclerView.class.getPackage().getName() + '.' + paramString));
  }
  
  private NestedScrollingChildHelper getScrollingChildHelper() {
    if (this.mScrollingChildHelper == null)
      this.mScrollingChildHelper = new NestedScrollingChildHelper((View)this); 
    return this.mScrollingChildHelper;
  }
  
  private void handleMissingPreInfoForChangeError(long paramLong, ViewHolder paramViewHolder1, ViewHolder paramViewHolder2) {
    Adapter adapter;
    int i = this.mChildHelper.getChildCount();
    for (byte b = 0; b < i; b++) {
      ViewHolder viewHolder = getChildViewHolderInt(this.mChildHelper.getChildAt(b));
      if (viewHolder != paramViewHolder1 && getChangedHolderKey(viewHolder) == paramLong) {
        adapter = this.mAdapter;
        if (adapter != null && adapter.hasStableIds())
          throw new IllegalStateException("Two different ViewHolders have the same stable ID. Stable IDs in your adapter MUST BE unique and SHOULD NOT change.\n ViewHolder 1:" + viewHolder + " \n View Holder 2:" + paramViewHolder1 + exceptionLabel()); 
        throw new IllegalStateException("Two different ViewHolders have the same change ID. This might happen due to inconsistent Adapter update events or if the LayoutManager lays out the same View multiple times.\n ViewHolder 1:" + viewHolder + " \n View Holder 2:" + paramViewHolder1 + exceptionLabel());
      } 
    } 
    Log.e("RecyclerView", "Problem while matching changed view holders with the newones. The pre-layout information for the change holder " + adapter + " cannot be found but it is necessary for " + paramViewHolder1 + exceptionLabel());
  }
  
  private boolean hasUpdatedView() {
    int i = this.mChildHelper.getChildCount();
    for (byte b = 0; b < i; b++) {
      ViewHolder viewHolder = getChildViewHolderInt(this.mChildHelper.getChildAt(b));
      if (viewHolder != null && !viewHolder.shouldIgnore() && viewHolder.isUpdated())
        return true; 
    } 
    return false;
  }
  
  private void initAutofill() {
    if (ViewCompat.getImportantForAutofill((View)this) == 0)
      ViewCompat.setImportantForAutofill((View)this, 8); 
  }
  
  private void initChildrenHelper() {
    this.mChildHelper = new ChildHelper(new ChildHelper.Callback() {
          final RecyclerView this$0;
          
          public void addView(View param1View, int param1Int) {
            RecyclerView.this.addView(param1View, param1Int);
            RecyclerView.this.dispatchChildAttached(param1View);
          }
          
          public void attachViewToParent(View param1View, int param1Int, ViewGroup.LayoutParams param1LayoutParams) {
            RecyclerView.ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(param1View);
            if (viewHolder != null)
              if (viewHolder.isTmpDetached() || viewHolder.shouldIgnore()) {
                viewHolder.clearTmpDetachFlag();
              } else {
                throw new IllegalArgumentException("Called attach on a child which is not detached: " + viewHolder + RecyclerView.this.exceptionLabel());
              }  
            RecyclerView.this.attachViewToParent(param1View, param1Int, param1LayoutParams);
          }
          
          public void detachViewFromParent(int param1Int) {
            View view = getChildAt(param1Int);
            if (view != null) {
              RecyclerView.ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(view);
              if (viewHolder != null)
                if (!viewHolder.isTmpDetached() || viewHolder.shouldIgnore()) {
                  viewHolder.addFlags(256);
                } else {
                  throw new IllegalArgumentException("called detach on an already detached child " + viewHolder + RecyclerView.this.exceptionLabel());
                }  
            } 
            RecyclerView.this.detachViewFromParent(param1Int);
          }
          
          public View getChildAt(int param1Int) {
            return RecyclerView.this.getChildAt(param1Int);
          }
          
          public int getChildCount() {
            return RecyclerView.this.getChildCount();
          }
          
          public RecyclerView.ViewHolder getChildViewHolder(View param1View) {
            return RecyclerView.getChildViewHolderInt(param1View);
          }
          
          public int indexOfChild(View param1View) {
            return RecyclerView.this.indexOfChild(param1View);
          }
          
          public void onEnteredHiddenState(View param1View) {
            RecyclerView.ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(param1View);
            if (viewHolder != null)
              viewHolder.onEnteredHiddenState(RecyclerView.this); 
          }
          
          public void onLeftHiddenState(View param1View) {
            RecyclerView.ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(param1View);
            if (viewHolder != null)
              viewHolder.onLeftHiddenState(RecyclerView.this); 
          }
          
          public void removeAllViews() {
            int i = getChildCount();
            for (byte b = 0; b < i; b++) {
              View view = getChildAt(b);
              RecyclerView.this.dispatchChildDetached(view);
              view.clearAnimation();
            } 
            RecyclerView.this.removeAllViews();
          }
          
          public void removeViewAt(int param1Int) {
            View view = RecyclerView.this.getChildAt(param1Int);
            if (view != null) {
              RecyclerView.this.dispatchChildDetached(view);
              view.clearAnimation();
            } 
            RecyclerView.this.removeViewAt(param1Int);
          }
        });
  }
  
  private boolean isPreferredNextFocus(View paramView1, View paramView2, int paramInt) {
    // Byte code:
    //   0: iconst_0
    //   1: istore #8
    //   3: iconst_0
    //   4: istore #11
    //   6: iconst_0
    //   7: istore #12
    //   9: iconst_0
    //   10: istore #9
    //   12: iconst_0
    //   13: istore #10
    //   15: iconst_0
    //   16: istore #13
    //   18: aload_2
    //   19: ifnull -> 587
    //   22: aload_2
    //   23: aload_0
    //   24: if_acmpne -> 30
    //   27: goto -> 587
    //   30: aload_0
    //   31: aload_2
    //   32: invokevirtual findContainingItemView : (Landroid/view/View;)Landroid/view/View;
    //   35: ifnonnull -> 40
    //   38: iconst_0
    //   39: ireturn
    //   40: aload_1
    //   41: ifnonnull -> 46
    //   44: iconst_1
    //   45: ireturn
    //   46: aload_0
    //   47: aload_1
    //   48: invokevirtual findContainingItemView : (Landroid/view/View;)Landroid/view/View;
    //   51: ifnonnull -> 56
    //   54: iconst_1
    //   55: ireturn
    //   56: aload_0
    //   57: getfield mTempRect : Landroid/graphics/Rect;
    //   60: iconst_0
    //   61: iconst_0
    //   62: aload_1
    //   63: invokevirtual getWidth : ()I
    //   66: aload_1
    //   67: invokevirtual getHeight : ()I
    //   70: invokevirtual set : (IIII)V
    //   73: aload_0
    //   74: getfield mTempRect2 : Landroid/graphics/Rect;
    //   77: iconst_0
    //   78: iconst_0
    //   79: aload_2
    //   80: invokevirtual getWidth : ()I
    //   83: aload_2
    //   84: invokevirtual getHeight : ()I
    //   87: invokevirtual set : (IIII)V
    //   90: aload_0
    //   91: aload_1
    //   92: aload_0
    //   93: getfield mTempRect : Landroid/graphics/Rect;
    //   96: invokevirtual offsetDescendantRectToMyCoords : (Landroid/view/View;Landroid/graphics/Rect;)V
    //   99: aload_0
    //   100: aload_2
    //   101: aload_0
    //   102: getfield mTempRect2 : Landroid/graphics/Rect;
    //   105: invokevirtual offsetDescendantRectToMyCoords : (Landroid/view/View;Landroid/graphics/Rect;)V
    //   108: aload_0
    //   109: getfield mLayout : Landroidx/recyclerview/widget/RecyclerView$LayoutManager;
    //   112: invokevirtual getLayoutDirection : ()I
    //   115: iconst_1
    //   116: if_icmpne -> 125
    //   119: iconst_m1
    //   120: istore #6
    //   122: goto -> 128
    //   125: iconst_1
    //   126: istore #6
    //   128: iconst_0
    //   129: istore #5
    //   131: aload_0
    //   132: getfield mTempRect : Landroid/graphics/Rect;
    //   135: getfield left : I
    //   138: aload_0
    //   139: getfield mTempRect2 : Landroid/graphics/Rect;
    //   142: getfield left : I
    //   145: if_icmplt -> 165
    //   148: aload_0
    //   149: getfield mTempRect : Landroid/graphics/Rect;
    //   152: getfield right : I
    //   155: aload_0
    //   156: getfield mTempRect2 : Landroid/graphics/Rect;
    //   159: getfield left : I
    //   162: if_icmpgt -> 188
    //   165: aload_0
    //   166: getfield mTempRect : Landroid/graphics/Rect;
    //   169: getfield right : I
    //   172: aload_0
    //   173: getfield mTempRect2 : Landroid/graphics/Rect;
    //   176: getfield right : I
    //   179: if_icmpge -> 188
    //   182: iconst_1
    //   183: istore #4
    //   185: goto -> 250
    //   188: aload_0
    //   189: getfield mTempRect : Landroid/graphics/Rect;
    //   192: getfield right : I
    //   195: aload_0
    //   196: getfield mTempRect2 : Landroid/graphics/Rect;
    //   199: getfield right : I
    //   202: if_icmpgt -> 226
    //   205: iload #5
    //   207: istore #4
    //   209: aload_0
    //   210: getfield mTempRect : Landroid/graphics/Rect;
    //   213: getfield left : I
    //   216: aload_0
    //   217: getfield mTempRect2 : Landroid/graphics/Rect;
    //   220: getfield right : I
    //   223: if_icmplt -> 250
    //   226: iload #5
    //   228: istore #4
    //   230: aload_0
    //   231: getfield mTempRect : Landroid/graphics/Rect;
    //   234: getfield left : I
    //   237: aload_0
    //   238: getfield mTempRect2 : Landroid/graphics/Rect;
    //   241: getfield left : I
    //   244: if_icmple -> 250
    //   247: iconst_m1
    //   248: istore #4
    //   250: iconst_0
    //   251: istore #7
    //   253: aload_0
    //   254: getfield mTempRect : Landroid/graphics/Rect;
    //   257: getfield top : I
    //   260: aload_0
    //   261: getfield mTempRect2 : Landroid/graphics/Rect;
    //   264: getfield top : I
    //   267: if_icmplt -> 287
    //   270: aload_0
    //   271: getfield mTempRect : Landroid/graphics/Rect;
    //   274: getfield bottom : I
    //   277: aload_0
    //   278: getfield mTempRect2 : Landroid/graphics/Rect;
    //   281: getfield top : I
    //   284: if_icmpgt -> 310
    //   287: aload_0
    //   288: getfield mTempRect : Landroid/graphics/Rect;
    //   291: getfield bottom : I
    //   294: aload_0
    //   295: getfield mTempRect2 : Landroid/graphics/Rect;
    //   298: getfield bottom : I
    //   301: if_icmpge -> 310
    //   304: iconst_1
    //   305: istore #5
    //   307: goto -> 372
    //   310: aload_0
    //   311: getfield mTempRect : Landroid/graphics/Rect;
    //   314: getfield bottom : I
    //   317: aload_0
    //   318: getfield mTempRect2 : Landroid/graphics/Rect;
    //   321: getfield bottom : I
    //   324: if_icmpgt -> 348
    //   327: iload #7
    //   329: istore #5
    //   331: aload_0
    //   332: getfield mTempRect : Landroid/graphics/Rect;
    //   335: getfield top : I
    //   338: aload_0
    //   339: getfield mTempRect2 : Landroid/graphics/Rect;
    //   342: getfield bottom : I
    //   345: if_icmplt -> 372
    //   348: iload #7
    //   350: istore #5
    //   352: aload_0
    //   353: getfield mTempRect : Landroid/graphics/Rect;
    //   356: getfield top : I
    //   359: aload_0
    //   360: getfield mTempRect2 : Landroid/graphics/Rect;
    //   363: getfield top : I
    //   366: if_icmple -> 372
    //   369: iconst_m1
    //   370: istore #5
    //   372: iload_3
    //   373: lookupswitch default -> 432, 1 -> 555, 2 -> 523, 17 -> 508, 33 -> 493, 66 -> 482, 130 -> 467
    //   432: new java/lang/IllegalArgumentException
    //   435: dup
    //   436: new java/lang/StringBuilder
    //   439: dup
    //   440: invokespecial <init> : ()V
    //   443: ldc_w 'Invalid direction: '
    //   446: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   449: iload_3
    //   450: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   453: aload_0
    //   454: invokevirtual exceptionLabel : ()Ljava/lang/String;
    //   457: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   460: invokevirtual toString : ()Ljava/lang/String;
    //   463: invokespecial <init> : (Ljava/lang/String;)V
    //   466: athrow
    //   467: iload #13
    //   469: istore #8
    //   471: iload #5
    //   473: ifle -> 479
    //   476: iconst_1
    //   477: istore #8
    //   479: iload #8
    //   481: ireturn
    //   482: iload #4
    //   484: ifle -> 490
    //   487: iconst_1
    //   488: istore #8
    //   490: iload #8
    //   492: ireturn
    //   493: iload #11
    //   495: istore #8
    //   497: iload #5
    //   499: ifge -> 505
    //   502: iconst_1
    //   503: istore #8
    //   505: iload #8
    //   507: ireturn
    //   508: iload #12
    //   510: istore #8
    //   512: iload #4
    //   514: ifge -> 520
    //   517: iconst_1
    //   518: istore #8
    //   520: iload #8
    //   522: ireturn
    //   523: iload #5
    //   525: ifgt -> 549
    //   528: iload #9
    //   530: istore #8
    //   532: iload #5
    //   534: ifne -> 552
    //   537: iload #9
    //   539: istore #8
    //   541: iload #4
    //   543: iload #6
    //   545: imul
    //   546: iflt -> 552
    //   549: iconst_1
    //   550: istore #8
    //   552: iload #8
    //   554: ireturn
    //   555: iload #5
    //   557: iflt -> 581
    //   560: iload #10
    //   562: istore #8
    //   564: iload #5
    //   566: ifne -> 584
    //   569: iload #10
    //   571: istore #8
    //   573: iload #4
    //   575: iload #6
    //   577: imul
    //   578: ifgt -> 584
    //   581: iconst_1
    //   582: istore #8
    //   584: iload #8
    //   586: ireturn
    //   587: iconst_0
    //   588: ireturn
  }
  
  private void onPointerUp(MotionEvent paramMotionEvent) {
    int i = paramMotionEvent.getActionIndex();
    if (paramMotionEvent.getPointerId(i) == this.mScrollPointerId) {
      if (i == 0) {
        i = 1;
      } else {
        i = 0;
      } 
      this.mScrollPointerId = paramMotionEvent.getPointerId(i);
      int j = (int)(paramMotionEvent.getX(i) + 0.5F);
      this.mLastTouchX = j;
      this.mInitialTouchX = j;
      i = (int)(paramMotionEvent.getY(i) + 0.5F);
      this.mLastTouchY = i;
      this.mInitialTouchY = i;
    } 
  }
  
  private boolean predictiveItemAnimationsEnabled() {
    boolean bool;
    if (this.mItemAnimator != null && this.mLayout.supportsPredictiveItemAnimations()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private void processAdapterUpdatesAndSetAnimationFlags() {
    boolean bool1;
    if (this.mDataSetHasChangedAfterLayout) {
      this.mAdapterHelper.reset();
      if (this.mDispatchItemsChangedEvent)
        this.mLayout.onItemsChanged(this); 
    } 
    if (predictiveItemAnimationsEnabled()) {
      this.mAdapterHelper.preProcess();
    } else {
      this.mAdapterHelper.consumeUpdatesInOnePass();
    } 
    boolean bool = this.mItemsAddedOrRemoved;
    boolean bool2 = false;
    if (bool || this.mItemsChanged) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    State state = this.mState;
    if (this.mFirstLayoutComplete && this.mItemAnimator != null && (this.mDataSetHasChangedAfterLayout || bool1 || this.mLayout.mRequestedSimpleAnimations) && (!this.mDataSetHasChangedAfterLayout || this.mAdapter.hasStableIds())) {
      bool = true;
    } else {
      bool = false;
    } 
    state.mRunSimpleAnimations = bool;
    state = this.mState;
    if (state.mRunSimpleAnimations && bool1 && !this.mDataSetHasChangedAfterLayout && predictiveItemAnimationsEnabled()) {
      bool = true;
    } else {
      bool = bool2;
    } 
    state.mRunPredictiveAnimations = bool;
  }
  
  private void pullGlows(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
    boolean bool = false;
    if (paramFloat2 < 0.0F) {
      ensureLeftGlow();
      EdgeEffectCompat.onPull(this.mLeftGlow, -paramFloat2 / getWidth(), 1.0F - paramFloat3 / getHeight());
      bool = true;
    } else if (paramFloat2 > 0.0F) {
      ensureRightGlow();
      EdgeEffectCompat.onPull(this.mRightGlow, paramFloat2 / getWidth(), paramFloat3 / getHeight());
      bool = true;
    } 
    if (paramFloat4 < 0.0F) {
      ensureTopGlow();
      EdgeEffectCompat.onPull(this.mTopGlow, -paramFloat4 / getHeight(), paramFloat1 / getWidth());
      bool = true;
    } else if (paramFloat4 > 0.0F) {
      ensureBottomGlow();
      EdgeEffectCompat.onPull(this.mBottomGlow, paramFloat4 / getHeight(), 1.0F - paramFloat1 / getWidth());
      bool = true;
    } 
    if (bool || paramFloat2 != 0.0F || paramFloat4 != 0.0F)
      ViewCompat.postInvalidateOnAnimation((View)this); 
  }
  
  private void recoverFocusFromState() {
    View view;
    if (!this.mPreserveFocusAfterLayout || this.mAdapter == null || !hasFocus() || getDescendantFocusability() == 393216 || (getDescendantFocusability() == 131072 && isFocused()))
      return; 
    if (!isFocused()) {
      view = getFocusedChild();
      if (IGNORE_DETACHED_FOCUSED_CHILD && (view.getParent() == null || !view.hasFocus())) {
        if (this.mChildHelper.getChildCount() == 0) {
          requestFocus();
          return;
        } 
      } else if (!this.mChildHelper.isHidden(view)) {
        return;
      } 
    } 
    ViewHolder viewHolder2 = null;
    ViewHolder viewHolder1 = viewHolder2;
    if (this.mState.mFocusedItemId != -1L) {
      viewHolder1 = viewHolder2;
      if (this.mAdapter.hasStableIds())
        viewHolder1 = findViewHolderForItemId(this.mState.mFocusedItemId); 
    } 
    viewHolder2 = null;
    if (viewHolder1 == null || this.mChildHelper.isHidden(viewHolder1.itemView) || !viewHolder1.itemView.hasFocusable()) {
      viewHolder1 = viewHolder2;
      if (this.mChildHelper.getChildCount() > 0)
        view = findNextViewToFocus(); 
    } else {
      view = ((ViewHolder)view).itemView;
    } 
    if (view != null) {
      View view1 = view;
      if (this.mState.mFocusedSubChildId != -1L) {
        View view2 = view.findViewById(this.mState.mFocusedSubChildId);
        view1 = view;
        if (view2 != null) {
          view1 = view;
          if (view2.isFocusable())
            view1 = view2; 
        } 
      } 
      view1.requestFocus();
    } 
  }
  
  private void releaseGlows() {
    boolean bool2 = false;
    EdgeEffect edgeEffect = this.mLeftGlow;
    if (edgeEffect != null) {
      edgeEffect.onRelease();
      bool2 = this.mLeftGlow.isFinished();
    } 
    edgeEffect = this.mTopGlow;
    boolean bool1 = bool2;
    if (edgeEffect != null) {
      edgeEffect.onRelease();
      bool1 = bool2 | this.mTopGlow.isFinished();
    } 
    edgeEffect = this.mRightGlow;
    bool2 = bool1;
    if (edgeEffect != null) {
      edgeEffect.onRelease();
      bool2 = bool1 | this.mRightGlow.isFinished();
    } 
    edgeEffect = this.mBottomGlow;
    bool1 = bool2;
    if (edgeEffect != null) {
      edgeEffect.onRelease();
      bool1 = bool2 | this.mBottomGlow.isFinished();
    } 
    if (bool1)
      ViewCompat.postInvalidateOnAnimation((View)this); 
  }
  
  private void requestChildOnScreen(View paramView1, View paramView2) {
    boolean bool;
    View view;
    if (paramView2 != null) {
      view = paramView2;
    } else {
      view = paramView1;
    } 
    this.mTempRect.set(0, 0, view.getWidth(), view.getHeight());
    ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
    if (layoutParams instanceof LayoutParams) {
      LayoutParams layoutParams1 = (LayoutParams)layoutParams;
      if (!layoutParams1.mInsetsDirty) {
        Rect rect1 = layoutParams1.mDecorInsets;
        Rect rect2 = this.mTempRect;
        rect2.left -= rect1.left;
        rect2 = this.mTempRect;
        rect2.right += rect1.right;
        rect2 = this.mTempRect;
        rect2.top -= rect1.top;
        rect2 = this.mTempRect;
        rect2.bottom += rect1.bottom;
      } 
    } 
    if (paramView2 != null) {
      offsetDescendantRectToMyCoords(paramView2, this.mTempRect);
      offsetRectIntoDescendantCoords(paramView1, this.mTempRect);
    } 
    LayoutManager layoutManager = this.mLayout;
    Rect rect = this.mTempRect;
    boolean bool1 = this.mFirstLayoutComplete;
    if (paramView2 == null) {
      bool = true;
    } else {
      bool = false;
    } 
    layoutManager.requestChildRectangleOnScreen(this, paramView1, rect, bool1 ^ true, bool);
  }
  
  private void resetFocusInfo() {
    this.mState.mFocusedItemId = -1L;
    this.mState.mFocusedItemPosition = -1;
    this.mState.mFocusedSubChildId = -1;
  }
  
  private void resetScroll() {
    VelocityTracker velocityTracker = this.mVelocityTracker;
    if (velocityTracker != null)
      velocityTracker.clear(); 
    stopNestedScroll(0);
    releaseGlows();
  }
  
  private void saveFocusInfo() {
    ViewHolder viewHolder;
    View view2 = null;
    View view1 = view2;
    if (this.mPreserveFocusAfterLayout) {
      view1 = view2;
      if (hasFocus()) {
        view1 = view2;
        if (this.mAdapter != null)
          view1 = getFocusedChild(); 
      } 
    } 
    if (view1 == null) {
      view1 = null;
    } else {
      viewHolder = findContainingViewHolder(view1);
    } 
    if (viewHolder == null) {
      resetFocusInfo();
    } else {
      int i;
      long l;
      State state = this.mState;
      if (this.mAdapter.hasStableIds()) {
        l = viewHolder.getItemId();
      } else {
        l = -1L;
      } 
      state.mFocusedItemId = l;
      state = this.mState;
      if (this.mDataSetHasChangedAfterLayout) {
        i = -1;
      } else if (viewHolder.isRemoved()) {
        i = viewHolder.mOldPosition;
      } else {
        i = viewHolder.getAdapterPosition();
      } 
      state.mFocusedItemPosition = i;
      this.mState.mFocusedSubChildId = getDeepestFocusedViewWithId(viewHolder.itemView);
    } 
  }
  
  private void setAdapterInternal(Adapter paramAdapter, boolean paramBoolean1, boolean paramBoolean2) {
    Adapter adapter = this.mAdapter;
    if (adapter != null) {
      adapter.unregisterAdapterDataObserver(this.mObserver);
      this.mAdapter.onDetachedFromRecyclerView(this);
    } 
    if (!paramBoolean1 || paramBoolean2)
      removeAndRecycleViews(); 
    this.mAdapterHelper.reset();
    adapter = this.mAdapter;
    this.mAdapter = paramAdapter;
    if (paramAdapter != null) {
      paramAdapter.registerAdapterDataObserver(this.mObserver);
      paramAdapter.onAttachedToRecyclerView(this);
    } 
    LayoutManager layoutManager = this.mLayout;
    if (layoutManager != null)
      layoutManager.onAdapterChanged(adapter, this.mAdapter); 
    this.mRecycler.onAdapterChanged(adapter, this.mAdapter, paramBoolean1);
    this.mState.mStructureChanged = true;
  }
  
  private void stopScrollersInternal() {
    this.mViewFlinger.stop();
    LayoutManager layoutManager = this.mLayout;
    if (layoutManager != null)
      layoutManager.stopSmoothScroller(); 
  }
  
  void absorbGlows(int paramInt1, int paramInt2) {
    if (paramInt1 < 0) {
      ensureLeftGlow();
      if (this.mLeftGlow.isFinished())
        this.mLeftGlow.onAbsorb(-paramInt1); 
    } else if (paramInt1 > 0) {
      ensureRightGlow();
      if (this.mRightGlow.isFinished())
        this.mRightGlow.onAbsorb(paramInt1); 
    } 
    if (paramInt2 < 0) {
      ensureTopGlow();
      if (this.mTopGlow.isFinished())
        this.mTopGlow.onAbsorb(-paramInt2); 
    } else if (paramInt2 > 0) {
      ensureBottomGlow();
      if (this.mBottomGlow.isFinished())
        this.mBottomGlow.onAbsorb(paramInt2); 
    } 
    if (paramInt1 != 0 || paramInt2 != 0)
      ViewCompat.postInvalidateOnAnimation((View)this); 
  }
  
  public void addFocusables(ArrayList<View> paramArrayList, int paramInt1, int paramInt2) {
    LayoutManager layoutManager = this.mLayout;
    if (layoutManager == null || !layoutManager.onAddFocusables(this, paramArrayList, paramInt1, paramInt2))
      super.addFocusables(paramArrayList, paramInt1, paramInt2); 
  }
  
  public void addItemDecoration(ItemDecoration paramItemDecoration) {
    addItemDecoration(paramItemDecoration, -1);
  }
  
  public void addItemDecoration(ItemDecoration paramItemDecoration, int paramInt) {
    LayoutManager layoutManager = this.mLayout;
    if (layoutManager != null)
      layoutManager.assertNotInLayoutOrScroll("Cannot add item decoration during a scroll  or layout"); 
    if (this.mItemDecorations.isEmpty())
      setWillNotDraw(false); 
    if (paramInt < 0) {
      this.mItemDecorations.add(paramItemDecoration);
    } else {
      this.mItemDecorations.add(paramInt, paramItemDecoration);
    } 
    markItemDecorInsetsDirty();
    requestLayout();
  }
  
  public void addOnChildAttachStateChangeListener(OnChildAttachStateChangeListener paramOnChildAttachStateChangeListener) {
    if (this.mOnChildAttachStateListeners == null)
      this.mOnChildAttachStateListeners = new ArrayList<>(); 
    this.mOnChildAttachStateListeners.add(paramOnChildAttachStateChangeListener);
  }
  
  public void addOnItemTouchListener(OnItemTouchListener paramOnItemTouchListener) {
    this.mOnItemTouchListeners.add(paramOnItemTouchListener);
  }
  
  public void addOnScrollListener(OnScrollListener paramOnScrollListener) {
    if (this.mScrollListeners == null)
      this.mScrollListeners = new ArrayList<>(); 
    this.mScrollListeners.add(paramOnScrollListener);
  }
  
  void animateAppearance(ViewHolder paramViewHolder, ItemAnimator.ItemHolderInfo paramItemHolderInfo1, ItemAnimator.ItemHolderInfo paramItemHolderInfo2) {
    paramViewHolder.setIsRecyclable(false);
    if (this.mItemAnimator.animateAppearance(paramViewHolder, paramItemHolderInfo1, paramItemHolderInfo2))
      postAnimationRunner(); 
  }
  
  void animateDisappearance(ViewHolder paramViewHolder, ItemAnimator.ItemHolderInfo paramItemHolderInfo1, ItemAnimator.ItemHolderInfo paramItemHolderInfo2) {
    addAnimatingView(paramViewHolder);
    paramViewHolder.setIsRecyclable(false);
    if (this.mItemAnimator.animateDisappearance(paramViewHolder, paramItemHolderInfo1, paramItemHolderInfo2))
      postAnimationRunner(); 
  }
  
  void assertInLayoutOrScroll(String paramString) {
    if (!isComputingLayout()) {
      if (paramString == null)
        throw new IllegalStateException("Cannot call this method unless RecyclerView is computing a layout or scrolling" + exceptionLabel()); 
      throw new IllegalStateException(paramString + exceptionLabel());
    } 
  }
  
  void assertNotInLayoutOrScroll(String paramString) {
    if (isComputingLayout()) {
      if (paramString == null)
        throw new IllegalStateException("Cannot call this method while RecyclerView is computing a layout or scrolling" + exceptionLabel()); 
      throw new IllegalStateException(paramString);
    } 
    if (this.mDispatchScrollCounter > 0)
      Log.w("RecyclerView", "Cannot call this method in a scroll callback. Scroll callbacks mightbe run during a measure & layout pass where you cannot change theRecyclerView data. Any method call that might change the structureof the RecyclerView or the adapter contents should be postponed tothe next frame.", new IllegalStateException("" + exceptionLabel())); 
  }
  
  boolean canReuseUpdatedViewHolder(ViewHolder paramViewHolder) {
    ItemAnimator itemAnimator = this.mItemAnimator;
    return (itemAnimator == null || itemAnimator.canReuseUpdatedViewHolder(paramViewHolder, paramViewHolder.getUnmodifiedPayloads()));
  }
  
  protected boolean checkLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    boolean bool;
    if (paramLayoutParams instanceof LayoutParams && this.mLayout.checkLayoutParams((LayoutParams)paramLayoutParams)) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  void clearOldPositions() {
    int i = this.mChildHelper.getUnfilteredChildCount();
    for (byte b = 0; b < i; b++) {
      ViewHolder viewHolder = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(b));
      if (!viewHolder.shouldIgnore())
        viewHolder.clearOldPosition(); 
    } 
    this.mRecycler.clearOldPositions();
  }
  
  public void clearOnChildAttachStateChangeListeners() {
    List<OnChildAttachStateChangeListener> list = this.mOnChildAttachStateListeners;
    if (list != null)
      list.clear(); 
  }
  
  public void clearOnScrollListeners() {
    List<OnScrollListener> list = this.mScrollListeners;
    if (list != null)
      list.clear(); 
  }
  
  public int computeHorizontalScrollExtent() {
    LayoutManager layoutManager = this.mLayout;
    int i = 0;
    if (layoutManager == null)
      return 0; 
    if (layoutManager.canScrollHorizontally())
      i = this.mLayout.computeHorizontalScrollExtent(this.mState); 
    return i;
  }
  
  public int computeHorizontalScrollOffset() {
    LayoutManager layoutManager = this.mLayout;
    int i = 0;
    if (layoutManager == null)
      return 0; 
    if (layoutManager.canScrollHorizontally())
      i = this.mLayout.computeHorizontalScrollOffset(this.mState); 
    return i;
  }
  
  public int computeHorizontalScrollRange() {
    LayoutManager layoutManager = this.mLayout;
    int i = 0;
    if (layoutManager == null)
      return 0; 
    if (layoutManager.canScrollHorizontally())
      i = this.mLayout.computeHorizontalScrollRange(this.mState); 
    return i;
  }
  
  public int computeVerticalScrollExtent() {
    LayoutManager layoutManager = this.mLayout;
    int i = 0;
    if (layoutManager == null)
      return 0; 
    if (layoutManager.canScrollVertically())
      i = this.mLayout.computeVerticalScrollExtent(this.mState); 
    return i;
  }
  
  public int computeVerticalScrollOffset() {
    LayoutManager layoutManager = this.mLayout;
    int i = 0;
    if (layoutManager == null)
      return 0; 
    if (layoutManager.canScrollVertically())
      i = this.mLayout.computeVerticalScrollOffset(this.mState); 
    return i;
  }
  
  public int computeVerticalScrollRange() {
    LayoutManager layoutManager = this.mLayout;
    int i = 0;
    if (layoutManager == null)
      return 0; 
    if (layoutManager.canScrollVertically())
      i = this.mLayout.computeVerticalScrollRange(this.mState); 
    return i;
  }
  
  void considerReleasingGlowsOnScroll(int paramInt1, int paramInt2) {
    boolean bool2 = false;
    EdgeEffect edgeEffect = this.mLeftGlow;
    boolean bool1 = bool2;
    if (edgeEffect != null) {
      bool1 = bool2;
      if (!edgeEffect.isFinished()) {
        bool1 = bool2;
        if (paramInt1 > 0) {
          this.mLeftGlow.onRelease();
          bool1 = this.mLeftGlow.isFinished();
        } 
      } 
    } 
    edgeEffect = this.mRightGlow;
    bool2 = bool1;
    if (edgeEffect != null) {
      bool2 = bool1;
      if (!edgeEffect.isFinished()) {
        bool2 = bool1;
        if (paramInt1 < 0) {
          this.mRightGlow.onRelease();
          bool2 = bool1 | this.mRightGlow.isFinished();
        } 
      } 
    } 
    edgeEffect = this.mTopGlow;
    bool1 = bool2;
    if (edgeEffect != null) {
      bool1 = bool2;
      if (!edgeEffect.isFinished()) {
        bool1 = bool2;
        if (paramInt2 > 0) {
          this.mTopGlow.onRelease();
          bool1 = bool2 | this.mTopGlow.isFinished();
        } 
      } 
    } 
    edgeEffect = this.mBottomGlow;
    bool2 = bool1;
    if (edgeEffect != null) {
      bool2 = bool1;
      if (!edgeEffect.isFinished()) {
        bool2 = bool1;
        if (paramInt2 < 0) {
          this.mBottomGlow.onRelease();
          bool2 = bool1 | this.mBottomGlow.isFinished();
        } 
      } 
    } 
    if (bool2)
      ViewCompat.postInvalidateOnAnimation((View)this); 
  }
  
  void consumePendingUpdateOperations() {
    if (!this.mFirstLayoutComplete || this.mDataSetHasChangedAfterLayout) {
      TraceCompat.beginSection("RV FullInvalidate");
      dispatchLayout();
      TraceCompat.endSection();
      return;
    } 
    if (!this.mAdapterHelper.hasPendingUpdates())
      return; 
    if (this.mAdapterHelper.hasAnyUpdateTypes(4) && !this.mAdapterHelper.hasAnyUpdateTypes(11)) {
      TraceCompat.beginSection("RV PartialInvalidate");
      startInterceptRequestLayout();
      onEnterLayoutOrScroll();
      this.mAdapterHelper.preProcess();
      if (!this.mLayoutWasDefered)
        if (hasUpdatedView()) {
          dispatchLayout();
        } else {
          this.mAdapterHelper.consumePostponedUpdates();
        }  
      stopInterceptRequestLayout(true);
      onExitLayoutOrScroll();
      TraceCompat.endSection();
    } else if (this.mAdapterHelper.hasPendingUpdates()) {
      TraceCompat.beginSection("RV FullInvalidate");
      dispatchLayout();
      TraceCompat.endSection();
    } 
  }
  
  void defaultOnMeasure(int paramInt1, int paramInt2) {
    paramInt1 = LayoutManager.chooseSize(paramInt1, getPaddingLeft() + getPaddingRight(), ViewCompat.getMinimumWidth((View)this));
    setMeasuredDimension(paramInt1, LayoutManager.chooseSize(paramInt2, getPaddingTop() + getPaddingBottom(), ViewCompat.getMinimumHeight((View)this)));
  }
  
  void dispatchChildAttached(View paramView) {
    ViewHolder viewHolder = getChildViewHolderInt(paramView);
    onChildAttachedToWindow(paramView);
    Adapter<ViewHolder> adapter = this.mAdapter;
    if (adapter != null && viewHolder != null)
      adapter.onViewAttachedToWindow(viewHolder); 
    List<OnChildAttachStateChangeListener> list = this.mOnChildAttachStateListeners;
    if (list != null)
      for (int i = list.size() - 1; i >= 0; i--)
        ((OnChildAttachStateChangeListener)this.mOnChildAttachStateListeners.get(i)).onChildViewAttachedToWindow(paramView);  
  }
  
  void dispatchChildDetached(View paramView) {
    ViewHolder viewHolder = getChildViewHolderInt(paramView);
    onChildDetachedFromWindow(paramView);
    Adapter<ViewHolder> adapter = this.mAdapter;
    if (adapter != null && viewHolder != null)
      adapter.onViewDetachedFromWindow(viewHolder); 
    List<OnChildAttachStateChangeListener> list = this.mOnChildAttachStateListeners;
    if (list != null)
      for (int i = list.size() - 1; i >= 0; i--)
        ((OnChildAttachStateChangeListener)this.mOnChildAttachStateListeners.get(i)).onChildViewDetachedFromWindow(paramView);  
  }
  
  void dispatchLayout() {
    if (this.mAdapter == null) {
      Log.e("RecyclerView", "No adapter attached; skipping layout");
      return;
    } 
    if (this.mLayout == null) {
      Log.e("RecyclerView", "No layout manager attached; skipping layout");
      return;
    } 
    this.mState.mIsMeasuring = false;
    if (this.mState.mLayoutStep == 1) {
      dispatchLayoutStep1();
      this.mLayout.setExactMeasureSpecsFrom(this);
      dispatchLayoutStep2();
    } else if (this.mAdapterHelper.hasUpdates() || this.mLayout.getWidth() != getWidth() || this.mLayout.getHeight() != getHeight()) {
      this.mLayout.setExactMeasureSpecsFrom(this);
      dispatchLayoutStep2();
    } else {
      this.mLayout.setExactMeasureSpecsFrom(this);
    } 
    dispatchLayoutStep3();
  }
  
  public boolean dispatchNestedFling(float paramFloat1, float paramFloat2, boolean paramBoolean) {
    return getScrollingChildHelper().dispatchNestedFling(paramFloat1, paramFloat2, paramBoolean);
  }
  
  public boolean dispatchNestedPreFling(float paramFloat1, float paramFloat2) {
    return getScrollingChildHelper().dispatchNestedPreFling(paramFloat1, paramFloat2);
  }
  
  public boolean dispatchNestedPreScroll(int paramInt1, int paramInt2, int[] paramArrayOfint1, int[] paramArrayOfint2) {
    return getScrollingChildHelper().dispatchNestedPreScroll(paramInt1, paramInt2, paramArrayOfint1, paramArrayOfint2);
  }
  
  public boolean dispatchNestedPreScroll(int paramInt1, int paramInt2, int[] paramArrayOfint1, int[] paramArrayOfint2, int paramInt3) {
    return getScrollingChildHelper().dispatchNestedPreScroll(paramInt1, paramInt2, paramArrayOfint1, paramArrayOfint2, paramInt3);
  }
  
  public final void dispatchNestedScroll(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfint1, int paramInt5, int[] paramArrayOfint2) {
    getScrollingChildHelper().dispatchNestedScroll(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfint1, paramInt5, paramArrayOfint2);
  }
  
  public boolean dispatchNestedScroll(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfint) {
    return getScrollingChildHelper().dispatchNestedScroll(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfint);
  }
  
  public boolean dispatchNestedScroll(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfint, int paramInt5) {
    return getScrollingChildHelper().dispatchNestedScroll(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfint, paramInt5);
  }
  
  void dispatchOnScrollStateChanged(int paramInt) {
    LayoutManager layoutManager = this.mLayout;
    if (layoutManager != null)
      layoutManager.onScrollStateChanged(paramInt); 
    onScrollStateChanged(paramInt);
    OnScrollListener onScrollListener = this.mScrollListener;
    if (onScrollListener != null)
      onScrollListener.onScrollStateChanged(this, paramInt); 
    List<OnScrollListener> list = this.mScrollListeners;
    if (list != null)
      for (int i = list.size() - 1; i >= 0; i--)
        ((OnScrollListener)this.mScrollListeners.get(i)).onScrollStateChanged(this, paramInt);  
  }
  
  void dispatchOnScrolled(int paramInt1, int paramInt2) {
    this.mDispatchScrollCounter++;
    int i = getScrollX();
    int j = getScrollY();
    onScrollChanged(i, j, i - paramInt1, j - paramInt2);
    onScrolled(paramInt1, paramInt2);
    OnScrollListener onScrollListener = this.mScrollListener;
    if (onScrollListener != null)
      onScrollListener.onScrolled(this, paramInt1, paramInt2); 
    List<OnScrollListener> list = this.mScrollListeners;
    if (list != null)
      for (i = list.size() - 1; i >= 0; i--)
        ((OnScrollListener)this.mScrollListeners.get(i)).onScrolled(this, paramInt1, paramInt2);  
    this.mDispatchScrollCounter--;
  }
  
  void dispatchPendingImportantForAccessibilityChanges() {
    for (int i = this.mPendingAccessibilityImportanceChange.size() - 1; i >= 0; i--) {
      ViewHolder viewHolder = this.mPendingAccessibilityImportanceChange.get(i);
      if (viewHolder.itemView.getParent() == this && !viewHolder.shouldIgnore()) {
        int j = viewHolder.mPendingAccessibilityState;
        if (j != -1) {
          ViewCompat.setImportantForAccessibility(viewHolder.itemView, j);
          viewHolder.mPendingAccessibilityState = -1;
        } 
      } 
    } 
    this.mPendingAccessibilityImportanceChange.clear();
  }
  
  public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent) {
    onPopulateAccessibilityEvent(paramAccessibilityEvent);
    return true;
  }
  
  protected void dispatchRestoreInstanceState(SparseArray<Parcelable> paramSparseArray) {
    dispatchThawSelfOnly(paramSparseArray);
  }
  
  protected void dispatchSaveInstanceState(SparseArray<Parcelable> paramSparseArray) {
    dispatchFreezeSelfOnly(paramSparseArray);
  }
  
  public void draw(Canvas paramCanvas) {
    super.draw(paramCanvas);
    int j = this.mItemDecorations.size();
    int i;
    for (i = 0; i < j; i++)
      ((ItemDecoration)this.mItemDecorations.get(i)).onDrawOver(paramCanvas, this, this.mState); 
    i = 0;
    EdgeEffect edgeEffect = this.mLeftGlow;
    boolean bool = true;
    j = i;
    if (edgeEffect != null) {
      j = i;
      if (!edgeEffect.isFinished()) {
        j = paramCanvas.save();
        if (this.mClipToPadding) {
          i = getPaddingBottom();
        } else {
          i = 0;
        } 
        paramCanvas.rotate(270.0F);
        paramCanvas.translate((-getHeight() + i), 0.0F);
        edgeEffect = this.mLeftGlow;
        if (edgeEffect != null && edgeEffect.draw(paramCanvas)) {
          i = 1;
        } else {
          i = 0;
        } 
        paramCanvas.restoreToCount(j);
        j = i;
      } 
    } 
    edgeEffect = this.mTopGlow;
    i = j;
    if (edgeEffect != null) {
      i = j;
      if (!edgeEffect.isFinished()) {
        int k = paramCanvas.save();
        if (this.mClipToPadding)
          paramCanvas.translate(getPaddingLeft(), getPaddingTop()); 
        edgeEffect = this.mTopGlow;
        if (edgeEffect != null && edgeEffect.draw(paramCanvas)) {
          i = 1;
        } else {
          i = 0;
        } 
        i = j | i;
        paramCanvas.restoreToCount(k);
      } 
    } 
    edgeEffect = this.mRightGlow;
    j = i;
    if (edgeEffect != null) {
      j = i;
      if (!edgeEffect.isFinished()) {
        int k = paramCanvas.save();
        int m = getWidth();
        if (this.mClipToPadding) {
          j = getPaddingTop();
        } else {
          j = 0;
        } 
        paramCanvas.rotate(90.0F);
        paramCanvas.translate(-j, -m);
        edgeEffect = this.mRightGlow;
        if (edgeEffect != null && edgeEffect.draw(paramCanvas)) {
          j = 1;
        } else {
          j = 0;
        } 
        j = i | j;
        paramCanvas.restoreToCount(k);
      } 
    } 
    edgeEffect = this.mBottomGlow;
    i = j;
    if (edgeEffect != null) {
      i = j;
      if (!edgeEffect.isFinished()) {
        int k = paramCanvas.save();
        paramCanvas.rotate(180.0F);
        if (this.mClipToPadding) {
          paramCanvas.translate((-getWidth() + getPaddingRight()), (-getHeight() + getPaddingBottom()));
        } else {
          paramCanvas.translate(-getWidth(), -getHeight());
        } 
        edgeEffect = this.mBottomGlow;
        if (edgeEffect != null && edgeEffect.draw(paramCanvas)) {
          i = bool;
        } else {
          i = 0;
        } 
        i = j | i;
        paramCanvas.restoreToCount(k);
      } 
    } 
    j = i;
    if (i == 0) {
      j = i;
      if (this.mItemAnimator != null) {
        j = i;
        if (this.mItemDecorations.size() > 0) {
          j = i;
          if (this.mItemAnimator.isRunning())
            j = 1; 
        } 
      } 
    } 
    if (j != 0)
      ViewCompat.postInvalidateOnAnimation((View)this); 
  }
  
  public boolean drawChild(Canvas paramCanvas, View paramView, long paramLong) {
    return super.drawChild(paramCanvas, paramView, paramLong);
  }
  
  void ensureBottomGlow() {
    if (this.mBottomGlow != null)
      return; 
    EdgeEffect edgeEffect = this.mEdgeEffectFactory.createEdgeEffect(this, 3);
    this.mBottomGlow = edgeEffect;
    if (this.mClipToPadding) {
      edgeEffect.setSize(getMeasuredWidth() - getPaddingLeft() - getPaddingRight(), getMeasuredHeight() - getPaddingTop() - getPaddingBottom());
    } else {
      edgeEffect.setSize(getMeasuredWidth(), getMeasuredHeight());
    } 
  }
  
  void ensureLeftGlow() {
    if (this.mLeftGlow != null)
      return; 
    EdgeEffect edgeEffect = this.mEdgeEffectFactory.createEdgeEffect(this, 0);
    this.mLeftGlow = edgeEffect;
    if (this.mClipToPadding) {
      edgeEffect.setSize(getMeasuredHeight() - getPaddingTop() - getPaddingBottom(), getMeasuredWidth() - getPaddingLeft() - getPaddingRight());
    } else {
      edgeEffect.setSize(getMeasuredHeight(), getMeasuredWidth());
    } 
  }
  
  void ensureRightGlow() {
    if (this.mRightGlow != null)
      return; 
    EdgeEffect edgeEffect = this.mEdgeEffectFactory.createEdgeEffect(this, 2);
    this.mRightGlow = edgeEffect;
    if (this.mClipToPadding) {
      edgeEffect.setSize(getMeasuredHeight() - getPaddingTop() - getPaddingBottom(), getMeasuredWidth() - getPaddingLeft() - getPaddingRight());
    } else {
      edgeEffect.setSize(getMeasuredHeight(), getMeasuredWidth());
    } 
  }
  
  void ensureTopGlow() {
    if (this.mTopGlow != null)
      return; 
    EdgeEffect edgeEffect = this.mEdgeEffectFactory.createEdgeEffect(this, 1);
    this.mTopGlow = edgeEffect;
    if (this.mClipToPadding) {
      edgeEffect.setSize(getMeasuredWidth() - getPaddingLeft() - getPaddingRight(), getMeasuredHeight() - getPaddingTop() - getPaddingBottom());
    } else {
      edgeEffect.setSize(getMeasuredWidth(), getMeasuredHeight());
    } 
  }
  
  String exceptionLabel() {
    return " " + toString() + ", adapter:" + this.mAdapter + ", layout:" + this.mLayout + ", context:" + getContext();
  }
  
  final void fillRemainingScrollValues(State paramState) {
    if (getScrollState() == 2) {
      OverScroller overScroller = this.mViewFlinger.mOverScroller;
      paramState.mRemainingScrollHorizontal = overScroller.getFinalX() - overScroller.getCurrX();
      paramState.mRemainingScrollVertical = overScroller.getFinalY() - overScroller.getCurrY();
    } else {
      paramState.mRemainingScrollHorizontal = 0;
      paramState.mRemainingScrollVertical = 0;
    } 
  }
  
  public View findChildViewUnder(float paramFloat1, float paramFloat2) {
    for (int i = this.mChildHelper.getChildCount() - 1; i >= 0; i--) {
      View view = this.mChildHelper.getChildAt(i);
      float f2 = view.getTranslationX();
      float f1 = view.getTranslationY();
      if (paramFloat1 >= view.getLeft() + f2 && paramFloat1 <= view.getRight() + f2 && paramFloat2 >= view.getTop() + f1 && paramFloat2 <= view.getBottom() + f1)
        return view; 
    } 
    return null;
  }
  
  public View findContainingItemView(View paramView) {
    ViewParent viewParent;
    for (viewParent = paramView.getParent(); viewParent != null && viewParent != this && viewParent instanceof View; viewParent = paramView.getParent())
      paramView = (View)viewParent; 
    if (viewParent != this)
      paramView = null; 
    return paramView;
  }
  
  public ViewHolder findContainingViewHolder(View paramView) {
    ViewHolder viewHolder;
    paramView = findContainingItemView(paramView);
    if (paramView == null) {
      paramView = null;
    } else {
      viewHolder = getChildViewHolder(paramView);
    } 
    return viewHolder;
  }
  
  public ViewHolder findViewHolderForAdapterPosition(int paramInt) {
    if (this.mDataSetHasChangedAfterLayout)
      return null; 
    int i = this.mChildHelper.getUnfilteredChildCount();
    ViewHolder viewHolder = null;
    byte b = 0;
    while (b < i) {
      ViewHolder viewHolder2 = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(b));
      ViewHolder viewHolder1 = viewHolder;
      if (viewHolder2 != null) {
        viewHolder1 = viewHolder;
        if (!viewHolder2.isRemoved()) {
          viewHolder1 = viewHolder;
          if (getAdapterPositionFor(viewHolder2) == paramInt)
            if (this.mChildHelper.isHidden(viewHolder2.itemView)) {
              viewHolder1 = viewHolder2;
            } else {
              return viewHolder2;
            }  
        } 
      } 
      b++;
      viewHolder = viewHolder1;
    } 
    return viewHolder;
  }
  
  public ViewHolder findViewHolderForItemId(long paramLong) {
    ViewHolder viewHolder;
    Adapter adapter = this.mAdapter;
    if (adapter == null || !adapter.hasStableIds())
      return null; 
    int i = this.mChildHelper.getUnfilteredChildCount();
    adapter = null;
    byte b = 0;
    while (b < i) {
      ViewHolder viewHolder1;
      ViewHolder viewHolder2 = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(b));
      Adapter adapter1 = adapter;
      if (viewHolder2 != null) {
        adapter1 = adapter;
        if (!viewHolder2.isRemoved()) {
          adapter1 = adapter;
          if (viewHolder2.getItemId() == paramLong)
            if (this.mChildHelper.isHidden(viewHolder2.itemView)) {
              viewHolder1 = viewHolder2;
            } else {
              return viewHolder2;
            }  
        } 
      } 
      b++;
      viewHolder = viewHolder1;
    } 
    return viewHolder;
  }
  
  public ViewHolder findViewHolderForLayoutPosition(int paramInt) {
    return findViewHolderForPosition(paramInt, false);
  }
  
  @Deprecated
  public ViewHolder findViewHolderForPosition(int paramInt) {
    return findViewHolderForPosition(paramInt, false);
  }
  
  ViewHolder findViewHolderForPosition(int paramInt, boolean paramBoolean) {
    int i = this.mChildHelper.getUnfilteredChildCount();
    Object object = null;
    byte b = 0;
    while (b < i) {
      ViewHolder viewHolder = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(b));
      Object object1 = object;
      if (viewHolder != null) {
        object1 = object;
        if (!viewHolder.isRemoved()) {
          if (paramBoolean) {
            if (viewHolder.mPosition != paramInt) {
              object1 = object;
              continue;
            } 
          } else if (viewHolder.getLayoutPosition() != paramInt) {
            object1 = object;
            continue;
          } 
          if (this.mChildHelper.isHidden(viewHolder.itemView)) {
            object1 = viewHolder;
          } else {
            return viewHolder;
          } 
        } 
      } 
      continue;
      b++;
      object = SYNTHETIC_LOCAL_VARIABLE_5;
    } 
    return (ViewHolder)object;
  }
  
  public boolean fling(int paramInt1, int paramInt2) {
    // Byte code:
    //   0: aload_0
    //   1: getfield mLayout : Landroidx/recyclerview/widget/RecyclerView$LayoutManager;
    //   4: astore #8
    //   6: aload #8
    //   8: ifnonnull -> 22
    //   11: ldc 'RecyclerView'
    //   13: ldc_w 'Cannot fling without a LayoutManager set. Call setLayoutManager with a non-null argument.'
    //   16: invokestatic e : (Ljava/lang/String;Ljava/lang/String;)I
    //   19: pop
    //   20: iconst_0
    //   21: ireturn
    //   22: aload_0
    //   23: getfield mLayoutSuppressed : Z
    //   26: ifeq -> 31
    //   29: iconst_0
    //   30: ireturn
    //   31: aload #8
    //   33: invokevirtual canScrollHorizontally : ()Z
    //   36: istore #6
    //   38: aload_0
    //   39: getfield mLayout : Landroidx/recyclerview/widget/RecyclerView$LayoutManager;
    //   42: invokevirtual canScrollVertically : ()Z
    //   45: istore #7
    //   47: iload #6
    //   49: ifeq -> 65
    //   52: iload_1
    //   53: istore_3
    //   54: iload_1
    //   55: invokestatic abs : (I)I
    //   58: aload_0
    //   59: getfield mMinFlingVelocity : I
    //   62: if_icmpge -> 67
    //   65: iconst_0
    //   66: istore_3
    //   67: iload #7
    //   69: ifeq -> 86
    //   72: iload_2
    //   73: istore #4
    //   75: iload_2
    //   76: invokestatic abs : (I)I
    //   79: aload_0
    //   80: getfield mMinFlingVelocity : I
    //   83: if_icmpge -> 89
    //   86: iconst_0
    //   87: istore #4
    //   89: iload_3
    //   90: ifne -> 100
    //   93: iload #4
    //   95: ifne -> 100
    //   98: iconst_0
    //   99: ireturn
    //   100: aload_0
    //   101: iload_3
    //   102: i2f
    //   103: iload #4
    //   105: i2f
    //   106: invokevirtual dispatchNestedPreFling : (FF)Z
    //   109: ifne -> 248
    //   112: iload #6
    //   114: ifne -> 131
    //   117: iload #7
    //   119: ifeq -> 125
    //   122: goto -> 131
    //   125: iconst_0
    //   126: istore #5
    //   128: goto -> 134
    //   131: iconst_1
    //   132: istore #5
    //   134: aload_0
    //   135: iload_3
    //   136: i2f
    //   137: iload #4
    //   139: i2f
    //   140: iload #5
    //   142: invokevirtual dispatchNestedFling : (FFZ)Z
    //   145: pop
    //   146: aload_0
    //   147: getfield mOnFlingListener : Landroidx/recyclerview/widget/RecyclerView$OnFlingListener;
    //   150: astore #8
    //   152: aload #8
    //   154: ifnull -> 170
    //   157: aload #8
    //   159: iload_3
    //   160: iload #4
    //   162: invokevirtual onFling : (II)Z
    //   165: ifeq -> 170
    //   168: iconst_1
    //   169: ireturn
    //   170: iload #5
    //   172: ifeq -> 248
    //   175: iconst_0
    //   176: istore_1
    //   177: iload #6
    //   179: ifeq -> 186
    //   182: iconst_0
    //   183: iconst_1
    //   184: ior
    //   185: istore_1
    //   186: iload_1
    //   187: istore_2
    //   188: iload #7
    //   190: ifeq -> 197
    //   193: iload_1
    //   194: iconst_2
    //   195: ior
    //   196: istore_2
    //   197: aload_0
    //   198: iload_2
    //   199: iconst_1
    //   200: invokevirtual startNestedScroll : (II)Z
    //   203: pop
    //   204: aload_0
    //   205: getfield mMaxFlingVelocity : I
    //   208: istore_1
    //   209: iload_1
    //   210: ineg
    //   211: iload_3
    //   212: iload_1
    //   213: invokestatic min : (II)I
    //   216: invokestatic max : (II)I
    //   219: istore_1
    //   220: aload_0
    //   221: getfield mMaxFlingVelocity : I
    //   224: istore_2
    //   225: iload_2
    //   226: ineg
    //   227: iload #4
    //   229: iload_2
    //   230: invokestatic min : (II)I
    //   233: invokestatic max : (II)I
    //   236: istore_2
    //   237: aload_0
    //   238: getfield mViewFlinger : Landroidx/recyclerview/widget/RecyclerView$ViewFlinger;
    //   241: iload_1
    //   242: iload_2
    //   243: invokevirtual fling : (II)V
    //   246: iconst_1
    //   247: ireturn
    //   248: iconst_0
    //   249: ireturn
  }
  
  public View focusSearch(View paramView, int paramInt) {
    int i;
    int j;
    View view1;
    View view2 = this.mLayout.onInterceptFocusSearch(paramView, paramInt);
    if (view2 != null)
      return view2; 
    Adapter adapter = this.mAdapter;
    boolean bool = true;
    if (adapter != null && this.mLayout != null && !isComputingLayout() && !this.mLayoutSuppressed) {
      i = 1;
    } else {
      i = 0;
    } 
    FocusFinder focusFinder = FocusFinder.getInstance();
    if (i && (paramInt == 2 || paramInt == 1)) {
      int k = 0;
      i = paramInt;
      if (this.mLayout.canScrollVertically()) {
        byte b;
        if (paramInt == 2) {
          b = 130;
        } else {
          b = 33;
        } 
        if (focusFinder.findNextFocus(this, paramView, b) == null) {
          i = 1;
        } else {
          i = 0;
        } 
        int n = i;
        k = n;
        i = paramInt;
        if (FORCE_ABS_FOCUS_SEARCH_DIRECTION) {
          i = b;
          k = n;
        } 
      } 
      int m = k;
      j = i;
      if (k == 0) {
        m = k;
        j = i;
        if (this.mLayout.canScrollHorizontally()) {
          if (this.mLayout.getLayoutDirection() == 1) {
            paramInt = 1;
          } else {
            paramInt = 0;
          } 
          if (i == 2) {
            j = 1;
          } else {
            j = 0;
          } 
          if ((j ^ paramInt) != 0) {
            paramInt = 66;
          } else {
            paramInt = 17;
          } 
          if (focusFinder.findNextFocus(this, paramView, paramInt) == null) {
            j = bool;
          } else {
            j = 0;
          } 
          k = j;
          m = k;
          j = i;
          if (FORCE_ABS_FOCUS_SEARCH_DIRECTION) {
            j = paramInt;
            m = k;
          } 
        } 
      } 
      if (m != 0) {
        consumePendingUpdateOperations();
        if (findContainingItemView(paramView) == null)
          return null; 
        startInterceptRequestLayout();
        this.mLayout.onFocusSearchFailed(paramView, j, this.mRecycler, this.mState);
        stopInterceptRequestLayout(false);
      } 
      view1 = focusFinder.findNextFocus(this, paramView, j);
    } else {
      View view = view1.findNextFocus(this, paramView, paramInt);
      view1 = view;
      j = paramInt;
      if (view == null) {
        view1 = view;
        j = paramInt;
        if (i != 0) {
          consumePendingUpdateOperations();
          if (findContainingItemView(paramView) == null)
            return null; 
          startInterceptRequestLayout();
          view1 = this.mLayout.onFocusSearchFailed(paramView, paramInt, this.mRecycler, this.mState);
          stopInterceptRequestLayout(false);
          j = paramInt;
        } 
      } 
    } 
    if (view1 != null && !view1.hasFocusable()) {
      if (getFocusedChild() == null)
        return super.focusSearch(paramView, j); 
      requestChildOnScreen(view1, (View)null);
      return paramView;
    } 
    if (!isPreferredNextFocus(paramView, view1, j))
      view1 = super.focusSearch(paramView, j); 
    return view1;
  }
  
  protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
    LayoutManager layoutManager = this.mLayout;
    if (layoutManager != null)
      return (ViewGroup.LayoutParams)layoutManager.generateDefaultLayoutParams(); 
    throw new IllegalStateException("RecyclerView has no LayoutManager" + exceptionLabel());
  }
  
  public ViewGroup.LayoutParams generateLayoutParams(AttributeSet paramAttributeSet) {
    LayoutManager layoutManager = this.mLayout;
    if (layoutManager != null)
      return (ViewGroup.LayoutParams)layoutManager.generateLayoutParams(getContext(), paramAttributeSet); 
    throw new IllegalStateException("RecyclerView has no LayoutManager" + exceptionLabel());
  }
  
  protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    LayoutManager layoutManager = this.mLayout;
    if (layoutManager != null)
      return (ViewGroup.LayoutParams)layoutManager.generateLayoutParams(paramLayoutParams); 
    throw new IllegalStateException("RecyclerView has no LayoutManager" + exceptionLabel());
  }
  
  public CharSequence getAccessibilityClassName() {
    return "androidx.recyclerview.widget.RecyclerView";
  }
  
  public Adapter getAdapter() {
    return this.mAdapter;
  }
  
  int getAdapterPositionFor(ViewHolder paramViewHolder) {
    return (paramViewHolder.hasAnyOfTheFlags(524) || !paramViewHolder.isBound()) ? -1 : this.mAdapterHelper.applyPendingUpdatesToPosition(paramViewHolder.mPosition);
  }
  
  public int getBaseline() {
    LayoutManager layoutManager = this.mLayout;
    return (layoutManager != null) ? layoutManager.getBaseline() : super.getBaseline();
  }
  
  long getChangedHolderKey(ViewHolder paramViewHolder) {
    long l;
    if (this.mAdapter.hasStableIds()) {
      l = paramViewHolder.getItemId();
    } else {
      l = paramViewHolder.mPosition;
    } 
    return l;
  }
  
  public int getChildAdapterPosition(View paramView) {
    byte b;
    ViewHolder viewHolder = getChildViewHolderInt(paramView);
    if (viewHolder != null) {
      b = viewHolder.getAdapterPosition();
    } else {
      b = -1;
    } 
    return b;
  }
  
  protected int getChildDrawingOrder(int paramInt1, int paramInt2) {
    ChildDrawingOrderCallback childDrawingOrderCallback = this.mChildDrawingOrderCallback;
    return (childDrawingOrderCallback == null) ? super.getChildDrawingOrder(paramInt1, paramInt2) : childDrawingOrderCallback.onGetChildDrawingOrder(paramInt1, paramInt2);
  }
  
  public long getChildItemId(View paramView) {
    Adapter adapter = this.mAdapter;
    long l = -1L;
    if (adapter == null || !adapter.hasStableIds())
      return -1L; 
    ViewHolder viewHolder = getChildViewHolderInt(paramView);
    if (viewHolder != null)
      l = viewHolder.getItemId(); 
    return l;
  }
  
  public int getChildLayoutPosition(View paramView) {
    byte b;
    ViewHolder viewHolder = getChildViewHolderInt(paramView);
    if (viewHolder != null) {
      b = viewHolder.getLayoutPosition();
    } else {
      b = -1;
    } 
    return b;
  }
  
  @Deprecated
  public int getChildPosition(View paramView) {
    return getChildAdapterPosition(paramView);
  }
  
  public ViewHolder getChildViewHolder(View paramView) {
    ViewParent viewParent = paramView.getParent();
    if (viewParent == null || viewParent == this)
      return getChildViewHolderInt(paramView); 
    throw new IllegalArgumentException("View " + paramView + " is not a direct child of " + this);
  }
  
  public boolean getClipToPadding() {
    return this.mClipToPadding;
  }
  
  public RecyclerViewAccessibilityDelegate getCompatAccessibilityDelegate() {
    return this.mAccessibilityDelegate;
  }
  
  public void getDecoratedBoundsWithMargins(View paramView, Rect paramRect) {
    getDecoratedBoundsWithMarginsInt(paramView, paramRect);
  }
  
  public EdgeEffectFactory getEdgeEffectFactory() {
    return this.mEdgeEffectFactory;
  }
  
  public ItemAnimator getItemAnimator() {
    return this.mItemAnimator;
  }
  
  Rect getItemDecorInsetsForChild(View paramView) {
    LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
    if (!layoutParams.mInsetsDirty)
      return layoutParams.mDecorInsets; 
    if (this.mState.isPreLayout() && (layoutParams.isItemChanged() || layoutParams.isViewInvalid()))
      return layoutParams.mDecorInsets; 
    Rect rect = layoutParams.mDecorInsets;
    rect.set(0, 0, 0, 0);
    int i = this.mItemDecorations.size();
    for (byte b = 0; b < i; b++) {
      this.mTempRect.set(0, 0, 0, 0);
      ((ItemDecoration)this.mItemDecorations.get(b)).getItemOffsets(this.mTempRect, paramView, this, this.mState);
      rect.left += this.mTempRect.left;
      rect.top += this.mTempRect.top;
      rect.right += this.mTempRect.right;
      rect.bottom += this.mTempRect.bottom;
    } 
    layoutParams.mInsetsDirty = false;
    return rect;
  }
  
  public ItemDecoration getItemDecorationAt(int paramInt) {
    int i = getItemDecorationCount();
    if (paramInt >= 0 && paramInt < i)
      return this.mItemDecorations.get(paramInt); 
    throw new IndexOutOfBoundsException(paramInt + " is an invalid index for size " + i);
  }
  
  public int getItemDecorationCount() {
    return this.mItemDecorations.size();
  }
  
  public LayoutManager getLayoutManager() {
    return this.mLayout;
  }
  
  public int getMaxFlingVelocity() {
    return this.mMaxFlingVelocity;
  }
  
  public int getMinFlingVelocity() {
    return this.mMinFlingVelocity;
  }
  
  long getNanoTime() {
    return ALLOW_THREAD_GAP_WORK ? System.nanoTime() : 0L;
  }
  
  public OnFlingListener getOnFlingListener() {
    return this.mOnFlingListener;
  }
  
  public boolean getPreserveFocusAfterLayout() {
    return this.mPreserveFocusAfterLayout;
  }
  
  public RecycledViewPool getRecycledViewPool() {
    return this.mRecycler.getRecycledViewPool();
  }
  
  public int getScrollState() {
    return this.mScrollState;
  }
  
  public boolean hasFixedSize() {
    return this.mHasFixedSize;
  }
  
  public boolean hasNestedScrollingParent() {
    return getScrollingChildHelper().hasNestedScrollingParent();
  }
  
  public boolean hasNestedScrollingParent(int paramInt) {
    return getScrollingChildHelper().hasNestedScrollingParent(paramInt);
  }
  
  public boolean hasPendingAdapterUpdates() {
    return (!this.mFirstLayoutComplete || this.mDataSetHasChangedAfterLayout || this.mAdapterHelper.hasPendingUpdates());
  }
  
  void initAdapterManager() {
    this.mAdapterHelper = new AdapterHelper(new AdapterHelper.Callback() {
          final RecyclerView this$0;
          
          void dispatchUpdate(AdapterHelper.UpdateOp param1UpdateOp) {
            switch (param1UpdateOp.cmd) {
              default:
                return;
              case 8:
                RecyclerView.this.mLayout.onItemsMoved(RecyclerView.this, param1UpdateOp.positionStart, param1UpdateOp.itemCount, 1);
              case 4:
                RecyclerView.this.mLayout.onItemsUpdated(RecyclerView.this, param1UpdateOp.positionStart, param1UpdateOp.itemCount, param1UpdateOp.payload);
              case 2:
                RecyclerView.this.mLayout.onItemsRemoved(RecyclerView.this, param1UpdateOp.positionStart, param1UpdateOp.itemCount);
              case 1:
                break;
            } 
            RecyclerView.this.mLayout.onItemsAdded(RecyclerView.this, param1UpdateOp.positionStart, param1UpdateOp.itemCount);
          }
          
          public RecyclerView.ViewHolder findViewHolder(int param1Int) {
            RecyclerView.ViewHolder viewHolder = RecyclerView.this.findViewHolderForPosition(param1Int, true);
            return (viewHolder == null) ? null : (RecyclerView.this.mChildHelper.isHidden(viewHolder.itemView) ? null : viewHolder);
          }
          
          public void markViewHoldersUpdated(int param1Int1, int param1Int2, Object param1Object) {
            RecyclerView.this.viewRangeUpdate(param1Int1, param1Int2, param1Object);
            RecyclerView.this.mItemsChanged = true;
          }
          
          public void offsetPositionsForAdd(int param1Int1, int param1Int2) {
            RecyclerView.this.offsetPositionRecordsForInsert(param1Int1, param1Int2);
            RecyclerView.this.mItemsAddedOrRemoved = true;
          }
          
          public void offsetPositionsForMove(int param1Int1, int param1Int2) {
            RecyclerView.this.offsetPositionRecordsForMove(param1Int1, param1Int2);
            RecyclerView.this.mItemsAddedOrRemoved = true;
          }
          
          public void offsetPositionsForRemovingInvisible(int param1Int1, int param1Int2) {
            RecyclerView.this.offsetPositionRecordsForRemove(param1Int1, param1Int2, true);
            RecyclerView.this.mItemsAddedOrRemoved = true;
            RecyclerView.State state = RecyclerView.this.mState;
            state.mDeletedInvisibleItemCountSincePreviousLayout += param1Int2;
          }
          
          public void offsetPositionsForRemovingLaidOutOrNewView(int param1Int1, int param1Int2) {
            RecyclerView.this.offsetPositionRecordsForRemove(param1Int1, param1Int2, false);
            RecyclerView.this.mItemsAddedOrRemoved = true;
          }
          
          public void onDispatchFirstPass(AdapterHelper.UpdateOp param1UpdateOp) {
            dispatchUpdate(param1UpdateOp);
          }
          
          public void onDispatchSecondPass(AdapterHelper.UpdateOp param1UpdateOp) {
            dispatchUpdate(param1UpdateOp);
          }
        });
  }
  
  void initFastScroller(StateListDrawable paramStateListDrawable1, Drawable paramDrawable1, StateListDrawable paramStateListDrawable2, Drawable paramDrawable2) {
    if (paramStateListDrawable1 != null && paramDrawable1 != null && paramStateListDrawable2 != null && paramDrawable2 != null) {
      Resources resources = getContext().getResources();
      new FastScroller(this, paramStateListDrawable1, paramDrawable1, paramStateListDrawable2, paramDrawable2, resources.getDimensionPixelSize(R.dimen.fastscroll_default_thickness), resources.getDimensionPixelSize(R.dimen.fastscroll_minimum_range), resources.getDimensionPixelOffset(R.dimen.fastscroll_margin));
      return;
    } 
    throw new IllegalArgumentException("Trying to set fast scroller without both required drawables." + exceptionLabel());
  }
  
  void invalidateGlows() {
    this.mBottomGlow = null;
    this.mTopGlow = null;
    this.mRightGlow = null;
    this.mLeftGlow = null;
  }
  
  public void invalidateItemDecorations() {
    if (this.mItemDecorations.size() == 0)
      return; 
    LayoutManager layoutManager = this.mLayout;
    if (layoutManager != null)
      layoutManager.assertNotInLayoutOrScroll("Cannot invalidate item decorations during a scroll or layout"); 
    markItemDecorInsetsDirty();
    requestLayout();
  }
  
  boolean isAccessibilityEnabled() {
    boolean bool;
    AccessibilityManager accessibilityManager = this.mAccessibilityManager;
    if (accessibilityManager != null && accessibilityManager.isEnabled()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isAnimating() {
    boolean bool;
    ItemAnimator itemAnimator = this.mItemAnimator;
    if (itemAnimator != null && itemAnimator.isRunning()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isAttachedToWindow() {
    return this.mIsAttached;
  }
  
  public boolean isComputingLayout() {
    boolean bool;
    if (this.mLayoutOrScrollCounter > 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  @Deprecated
  public boolean isLayoutFrozen() {
    return isLayoutSuppressed();
  }
  
  public final boolean isLayoutSuppressed() {
    return this.mLayoutSuppressed;
  }
  
  public boolean isNestedScrollingEnabled() {
    return getScrollingChildHelper().isNestedScrollingEnabled();
  }
  
  void jumpToPositionForSmoothScroller(int paramInt) {
    if (this.mLayout == null)
      return; 
    setScrollState(2);
    this.mLayout.scrollToPosition(paramInt);
    awakenScrollBars();
  }
  
  void markItemDecorInsetsDirty() {
    int i = this.mChildHelper.getUnfilteredChildCount();
    for (byte b = 0; b < i; b++)
      ((LayoutParams)this.mChildHelper.getUnfilteredChildAt(b).getLayoutParams()).mInsetsDirty = true; 
    this.mRecycler.markItemDecorInsetsDirty();
  }
  
  void markKnownViewsInvalid() {
    int i = this.mChildHelper.getUnfilteredChildCount();
    for (byte b = 0; b < i; b++) {
      ViewHolder viewHolder = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(b));
      if (viewHolder != null && !viewHolder.shouldIgnore())
        viewHolder.addFlags(6); 
    } 
    markItemDecorInsetsDirty();
    this.mRecycler.markKnownViewsInvalid();
  }
  
  public void offsetChildrenHorizontal(int paramInt) {
    int i = this.mChildHelper.getChildCount();
    for (byte b = 0; b < i; b++)
      this.mChildHelper.getChildAt(b).offsetLeftAndRight(paramInt); 
  }
  
  public void offsetChildrenVertical(int paramInt) {
    int i = this.mChildHelper.getChildCount();
    for (byte b = 0; b < i; b++)
      this.mChildHelper.getChildAt(b).offsetTopAndBottom(paramInt); 
  }
  
  void offsetPositionRecordsForInsert(int paramInt1, int paramInt2) {
    int i = this.mChildHelper.getUnfilteredChildCount();
    for (byte b = 0; b < i; b++) {
      ViewHolder viewHolder = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(b));
      if (viewHolder != null && !viewHolder.shouldIgnore() && viewHolder.mPosition >= paramInt1) {
        viewHolder.offsetPosition(paramInt2, false);
        this.mState.mStructureChanged = true;
      } 
    } 
    this.mRecycler.offsetPositionRecordsForInsert(paramInt1, paramInt2);
    requestLayout();
  }
  
  void offsetPositionRecordsForMove(int paramInt1, int paramInt2) {
    boolean bool;
    int i;
    int j;
    int k = this.mChildHelper.getUnfilteredChildCount();
    if (paramInt1 < paramInt2) {
      i = paramInt1;
      j = paramInt2;
      bool = true;
    } else {
      i = paramInt2;
      j = paramInt1;
      bool = true;
    } 
    for (byte b = 0; b < k; b++) {
      ViewHolder viewHolder = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(b));
      if (viewHolder != null && viewHolder.mPosition >= i && viewHolder.mPosition <= j) {
        if (viewHolder.mPosition == paramInt1) {
          viewHolder.offsetPosition(paramInt2 - paramInt1, false);
        } else {
          viewHolder.offsetPosition(bool, false);
        } 
        this.mState.mStructureChanged = true;
      } 
    } 
    this.mRecycler.offsetPositionRecordsForMove(paramInt1, paramInt2);
    requestLayout();
  }
  
  void offsetPositionRecordsForRemove(int paramInt1, int paramInt2, boolean paramBoolean) {
    int i = this.mChildHelper.getUnfilteredChildCount();
    for (byte b = 0; b < i; b++) {
      ViewHolder viewHolder = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(b));
      if (viewHolder != null && !viewHolder.shouldIgnore())
        if (viewHolder.mPosition >= paramInt1 + paramInt2) {
          viewHolder.offsetPosition(-paramInt2, paramBoolean);
          this.mState.mStructureChanged = true;
        } else if (viewHolder.mPosition >= paramInt1) {
          viewHolder.flagRemovedAndOffsetPosition(paramInt1 - 1, -paramInt2, paramBoolean);
          this.mState.mStructureChanged = true;
        }  
    } 
    this.mRecycler.offsetPositionRecordsForRemove(paramInt1, paramInt2, paramBoolean);
    requestLayout();
  }
  
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    this.mLayoutOrScrollCounter = 0;
    boolean bool = true;
    this.mIsAttached = true;
    if (!this.mFirstLayoutComplete || isLayoutRequested())
      bool = false; 
    this.mFirstLayoutComplete = bool;
    LayoutManager layoutManager = this.mLayout;
    if (layoutManager != null)
      layoutManager.dispatchAttachedToWindow(this); 
    this.mPostedAnimatorRunner = false;
    if (ALLOW_THREAD_GAP_WORK) {
      GapWorker gapWorker = GapWorker.sGapWorker.get();
      this.mGapWorker = gapWorker;
      if (gapWorker == null) {
        this.mGapWorker = new GapWorker();
        Display display = ViewCompat.getDisplay((View)this);
        float f2 = 60.0F;
        float f1 = f2;
        if (!isInEditMode()) {
          f1 = f2;
          if (display != null) {
            float f = display.getRefreshRate();
            f1 = f2;
            if (f >= 30.0F)
              f1 = f; 
          } 
        } 
        this.mGapWorker.mFrameIntervalNs = (long)(1.0E9F / f1);
        GapWorker.sGapWorker.set(this.mGapWorker);
      } 
      this.mGapWorker.add(this);
    } 
  }
  
  public void onChildAttachedToWindow(View paramView) {}
  
  public void onChildDetachedFromWindow(View paramView) {}
  
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    ItemAnimator itemAnimator = this.mItemAnimator;
    if (itemAnimator != null)
      itemAnimator.endAnimations(); 
    stopScroll();
    this.mIsAttached = false;
    LayoutManager layoutManager = this.mLayout;
    if (layoutManager != null)
      layoutManager.dispatchDetachedFromWindow(this, this.mRecycler); 
    this.mPendingAccessibilityImportanceChange.clear();
    removeCallbacks(this.mItemAnimatorRunner);
    this.mViewInfoStore.onDetach();
    if (ALLOW_THREAD_GAP_WORK) {
      GapWorker gapWorker = this.mGapWorker;
      if (gapWorker != null) {
        gapWorker.remove(this);
        this.mGapWorker = null;
      } 
    } 
  }
  
  public void onDraw(Canvas paramCanvas) {
    super.onDraw(paramCanvas);
    int i = this.mItemDecorations.size();
    for (byte b = 0; b < i; b++)
      ((ItemDecoration)this.mItemDecorations.get(b)).onDraw(paramCanvas, this, this.mState); 
  }
  
  void onEnterLayoutOrScroll() {
    this.mLayoutOrScrollCounter++;
  }
  
  void onExitLayoutOrScroll() {
    onExitLayoutOrScroll(true);
  }
  
  void onExitLayoutOrScroll(boolean paramBoolean) {
    int i = this.mLayoutOrScrollCounter - 1;
    this.mLayoutOrScrollCounter = i;
    if (i < 1) {
      this.mLayoutOrScrollCounter = 0;
      if (paramBoolean) {
        dispatchContentChangedIfNecessary();
        dispatchPendingImportantForAccessibilityChanges();
      } 
    } 
  }
  
  public boolean onGenericMotionEvent(MotionEvent paramMotionEvent) {
    if (this.mLayout == null)
      return false; 
    if (this.mLayoutSuppressed)
      return false; 
    if (paramMotionEvent.getAction() == 8) {
      float f1;
      float f2;
      if ((paramMotionEvent.getSource() & 0x2) != 0) {
        if (this.mLayout.canScrollVertically()) {
          f2 = -paramMotionEvent.getAxisValue(9);
        } else {
          f2 = 0.0F;
        } 
        if (this.mLayout.canScrollHorizontally()) {
          f1 = paramMotionEvent.getAxisValue(10);
        } else {
          f1 = 0.0F;
        } 
      } else if ((paramMotionEvent.getSource() & 0x400000) != 0) {
        f1 = paramMotionEvent.getAxisValue(26);
        if (this.mLayout.canScrollVertically()) {
          f2 = -f1;
          f1 = 0.0F;
        } else if (this.mLayout.canScrollHorizontally()) {
          f2 = 0.0F;
        } else {
          f2 = 0.0F;
          f1 = 0.0F;
        } 
      } else {
        f2 = 0.0F;
        f1 = 0.0F;
      } 
      if (f2 != 0.0F || f1 != 0.0F)
        scrollByInternal((int)(this.mScaledHorizontalScrollFactor * f1), (int)(this.mScaledVerticalScrollFactor * f2), paramMotionEvent); 
    } 
    return false;
  }
  
  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent) {
    int[] arrayOfInt;
    int k;
    int m;
    boolean bool1 = this.mLayoutSuppressed;
    boolean bool = false;
    if (bool1)
      return false; 
    this.mInterceptingOnItemTouchListener = null;
    if (findInterceptingOnItemTouchListener(paramMotionEvent)) {
      cancelScroll();
      return true;
    } 
    LayoutManager layoutManager = this.mLayout;
    if (layoutManager == null)
      return false; 
    bool1 = layoutManager.canScrollHorizontally();
    boolean bool2 = this.mLayout.canScrollVertically();
    if (this.mVelocityTracker == null)
      this.mVelocityTracker = VelocityTracker.obtain(); 
    this.mVelocityTracker.addMovement(paramMotionEvent);
    int j = paramMotionEvent.getActionMasked();
    int i = paramMotionEvent.getActionIndex();
    switch (j) {
      case 6:
        onPointerUp(paramMotionEvent);
        break;
      case 5:
        this.mScrollPointerId = paramMotionEvent.getPointerId(i);
        j = (int)(paramMotionEvent.getX(i) + 0.5F);
        this.mLastTouchX = j;
        this.mInitialTouchX = j;
        i = (int)(paramMotionEvent.getY(i) + 0.5F);
        this.mLastTouchY = i;
        this.mInitialTouchY = i;
        break;
      case 3:
        cancelScroll();
        break;
      case 2:
        i = paramMotionEvent.findPointerIndex(this.mScrollPointerId);
        if (i < 0) {
          Log.e("RecyclerView", "Error processing scroll; pointer index for id " + this.mScrollPointerId + " not found. Did any MotionEvents get skipped?");
          return false;
        } 
        m = (int)(paramMotionEvent.getX(i) + 0.5F);
        k = (int)(paramMotionEvent.getY(i) + 0.5F);
        if (this.mScrollState != 1) {
          int i1 = this.mInitialTouchX;
          int n = this.mInitialTouchY;
          j = 0;
          i = j;
          if (bool1) {
            i = j;
            if (Math.abs(m - i1) > this.mTouchSlop) {
              this.mLastTouchX = m;
              i = 1;
            } 
          } 
          j = i;
          if (bool2) {
            j = i;
            if (Math.abs(k - n) > this.mTouchSlop) {
              this.mLastTouchY = k;
              j = 1;
            } 
          } 
          if (j != 0)
            setScrollState(1); 
        } 
        break;
      case 1:
        this.mVelocityTracker.clear();
        stopNestedScroll(0);
        break;
      case 0:
        if (this.mIgnoreMotionEventTillDown)
          this.mIgnoreMotionEventTillDown = false; 
        this.mScrollPointerId = paramMotionEvent.getPointerId(0);
        i = (int)(paramMotionEvent.getX() + 0.5F);
        this.mLastTouchX = i;
        this.mInitialTouchX = i;
        i = (int)(paramMotionEvent.getY() + 0.5F);
        this.mLastTouchY = i;
        this.mInitialTouchY = i;
        if (this.mScrollState == 2) {
          getParent().requestDisallowInterceptTouchEvent(true);
          setScrollState(1);
          stopNestedScroll(1);
        } 
        arrayOfInt = this.mNestedOffsets;
        arrayOfInt[1] = 0;
        arrayOfInt[0] = 0;
        i = 0;
        if (bool1)
          i = false | true; 
        j = i;
        if (bool2)
          j = i | 0x2; 
        startNestedScroll(j, 0);
        break;
    } 
    if (this.mScrollState == 1)
      bool = true; 
    return bool;
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    TraceCompat.beginSection("RV OnLayout");
    dispatchLayout();
    TraceCompat.endSection();
    this.mFirstLayoutComplete = true;
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    LayoutManager layoutManager = this.mLayout;
    if (layoutManager == null) {
      defaultOnMeasure(paramInt1, paramInt2);
      return;
    } 
    boolean bool1 = layoutManager.isAutoMeasureEnabled();
    boolean bool = false;
    if (bool1) {
      int j = View.MeasureSpec.getMode(paramInt1);
      int i = View.MeasureSpec.getMode(paramInt2);
      this.mLayout.onMeasure(this.mRecycler, this.mState, paramInt1, paramInt2);
      boolean bool2 = bool;
      if (j == 1073741824) {
        bool2 = bool;
        if (i == 1073741824)
          bool2 = true; 
      } 
      if (bool2 || this.mAdapter == null)
        return; 
      if (this.mState.mLayoutStep == 1)
        dispatchLayoutStep1(); 
      this.mLayout.setMeasureSpecs(paramInt1, paramInt2);
      this.mState.mIsMeasuring = true;
      dispatchLayoutStep2();
      this.mLayout.setMeasuredDimensionFromChildren(paramInt1, paramInt2);
      if (this.mLayout.shouldMeasureTwice()) {
        this.mLayout.setMeasureSpecs(View.MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 1073741824), View.MeasureSpec.makeMeasureSpec(getMeasuredHeight(), 1073741824));
        this.mState.mIsMeasuring = true;
        dispatchLayoutStep2();
        this.mLayout.setMeasuredDimensionFromChildren(paramInt1, paramInt2);
      } 
    } else {
      if (this.mHasFixedSize) {
        this.mLayout.onMeasure(this.mRecycler, this.mState, paramInt1, paramInt2);
        return;
      } 
      if (this.mAdapterUpdateDuringMeasure) {
        startInterceptRequestLayout();
        onEnterLayoutOrScroll();
        processAdapterUpdatesAndSetAnimationFlags();
        onExitLayoutOrScroll();
        if (this.mState.mRunPredictiveAnimations) {
          this.mState.mInPreLayout = true;
        } else {
          this.mAdapterHelper.consumeUpdatesInOnePass();
          this.mState.mInPreLayout = false;
        } 
        this.mAdapterUpdateDuringMeasure = false;
        stopInterceptRequestLayout(false);
      } else if (this.mState.mRunPredictiveAnimations) {
        setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight());
        return;
      } 
      Adapter adapter = this.mAdapter;
      if (adapter != null) {
        this.mState.mItemCount = adapter.getItemCount();
      } else {
        this.mState.mItemCount = 0;
      } 
      startInterceptRequestLayout();
      this.mLayout.onMeasure(this.mRecycler, this.mState, paramInt1, paramInt2);
      stopInterceptRequestLayout(false);
      this.mState.mInPreLayout = false;
    } 
  }
  
  protected boolean onRequestFocusInDescendants(int paramInt, Rect paramRect) {
    return isComputingLayout() ? false : super.onRequestFocusInDescendants(paramInt, paramRect);
  }
  
  protected void onRestoreInstanceState(Parcelable paramParcelable) {
    if (!(paramParcelable instanceof SavedState)) {
      super.onRestoreInstanceState(paramParcelable);
      return;
    } 
    SavedState savedState = (SavedState)paramParcelable;
    this.mPendingSavedState = savedState;
    super.onRestoreInstanceState(savedState.getSuperState());
    if (this.mLayout != null && this.mPendingSavedState.mLayoutState != null)
      this.mLayout.onRestoreInstanceState(this.mPendingSavedState.mLayoutState); 
  }
  
  protected Parcelable onSaveInstanceState() {
    SavedState savedState1 = new SavedState(super.onSaveInstanceState());
    SavedState savedState2 = this.mPendingSavedState;
    if (savedState2 != null) {
      savedState1.copyFrom(savedState2);
    } else {
      LayoutManager layoutManager = this.mLayout;
      if (layoutManager != null) {
        savedState1.mLayoutState = layoutManager.onSaveInstanceState();
      } else {
        savedState1.mLayoutState = null;
      } 
    } 
    return (Parcelable)savedState1;
  }
  
  public void onScrollStateChanged(int paramInt) {}
  
  public void onScrolled(int paramInt1, int paramInt2) {}
  
  protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    if (paramInt1 != paramInt3 || paramInt2 != paramInt4)
      invalidateGlows(); 
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    GapWorker gapWorker;
    float f1;
    float f2;
    int k;
    int m;
    int n;
    int i1;
    boolean bool3 = this.mLayoutSuppressed;
    boolean bool2 = false;
    if (bool3 || this.mIgnoreMotionEventTillDown)
      return false; 
    if (dispatchToOnItemTouchListeners(paramMotionEvent)) {
      cancelScroll();
      return true;
    } 
    LayoutManager layoutManager = this.mLayout;
    if (layoutManager == null)
      return false; 
    bool3 = layoutManager.canScrollHorizontally();
    boolean bool4 = this.mLayout.canScrollVertically();
    if (this.mVelocityTracker == null)
      this.mVelocityTracker = VelocityTracker.obtain(); 
    boolean bool1 = false;
    int j = paramMotionEvent.getActionMasked();
    int i = paramMotionEvent.getActionIndex();
    if (j == 0) {
      int[] arrayOfInt1 = this.mNestedOffsets;
      arrayOfInt1[1] = 0;
      arrayOfInt1[0] = 0;
    } 
    MotionEvent motionEvent = MotionEvent.obtain(paramMotionEvent);
    int[] arrayOfInt = this.mNestedOffsets;
    motionEvent.offsetLocation(arrayOfInt[0], arrayOfInt[1]);
    switch (j) {
      default:
        i = bool1;
        break;
      case 6:
        onPointerUp(paramMotionEvent);
        i = bool1;
        break;
      case 5:
        this.mScrollPointerId = paramMotionEvent.getPointerId(i);
        j = (int)(paramMotionEvent.getX(i) + 0.5F);
        this.mLastTouchX = j;
        this.mInitialTouchX = j;
        i = (int)(paramMotionEvent.getY(i) + 0.5F);
        this.mLastTouchY = i;
        this.mInitialTouchY = i;
        i = bool1;
        break;
      case 3:
        cancelScroll();
        i = bool1;
        break;
      case 2:
        i = paramMotionEvent.findPointerIndex(this.mScrollPointerId);
        if (i < 0) {
          Log.e("RecyclerView", "Error processing scroll; pointer index for id " + this.mScrollPointerId + " not found. Did any MotionEvents get skipped?");
          return false;
        } 
        n = (int)(paramMotionEvent.getX(i) + 0.5F);
        i1 = (int)(paramMotionEvent.getY(i) + 0.5F);
        k = this.mLastTouchX - n;
        m = this.mLastTouchY - i1;
        if (this.mScrollState != 1) {
          int i2 = 0;
          i = k;
          j = i2;
          if (bool3) {
            if (k > 0) {
              k = Math.max(0, k - this.mTouchSlop);
            } else {
              k = Math.min(0, this.mTouchSlop + k);
            } 
            i = k;
            j = i2;
            if (k != 0) {
              j = 1;
              i = k;
            } 
          } 
          k = m;
          i2 = j;
          if (bool4) {
            if (m > 0) {
              m = Math.max(0, m - this.mTouchSlop);
            } else {
              m = Math.min(0, this.mTouchSlop + m);
            } 
            k = m;
            i2 = j;
            if (m != 0) {
              i2 = 1;
              k = m;
            } 
          } 
          if (i2 != 0)
            setScrollState(1); 
          j = i;
          i = k;
        } else {
          j = k;
          i = m;
        } 
        if (this.mScrollState == 1) {
          arrayOfInt = this.mReusableIntPair;
          arrayOfInt[0] = 0;
          arrayOfInt[1] = 0;
          if (bool3) {
            k = j;
          } else {
            k = 0;
          } 
          if (bool4) {
            m = i;
          } else {
            m = 0;
          } 
          if (dispatchNestedPreScroll(k, m, arrayOfInt, this.mScrollOffset, 0)) {
            arrayOfInt = this.mReusableIntPair;
            int i2 = arrayOfInt[0];
            k = arrayOfInt[1];
            int[] arrayOfInt1 = this.mNestedOffsets;
            m = arrayOfInt1[0];
            arrayOfInt = this.mScrollOffset;
            arrayOfInt1[0] = m + arrayOfInt[0];
            arrayOfInt1[1] = arrayOfInt1[1] + arrayOfInt[1];
            getParent().requestDisallowInterceptTouchEvent(true);
            j -= i2;
            k = i - k;
            i = j;
            j = k;
          } else {
            k = j;
            j = i;
            i = k;
          } 
          arrayOfInt = this.mScrollOffset;
          this.mLastTouchX = n - arrayOfInt[0];
          this.mLastTouchY = i1 - arrayOfInt[1];
          if (bool3) {
            k = i;
          } else {
            k = 0;
          } 
          m = bool2;
          if (bool4)
            m = j; 
          if (scrollByInternal(k, m, paramMotionEvent))
            getParent().requestDisallowInterceptTouchEvent(true); 
          gapWorker = this.mGapWorker;
          if (gapWorker != null && (i != 0 || j != 0))
            gapWorker.postFromTraversal(this, i, j); 
        } 
        i = bool1;
        break;
      case 1:
        this.mVelocityTracker.addMovement(motionEvent);
        i = 1;
        this.mVelocityTracker.computeCurrentVelocity(1000, this.mMaxFlingVelocity);
        if (bool3) {
          f1 = -this.mVelocityTracker.getXVelocity(this.mScrollPointerId);
        } else {
          f1 = 0.0F;
        } 
        if (bool4) {
          f2 = -this.mVelocityTracker.getYVelocity(this.mScrollPointerId);
        } else {
          f2 = 0.0F;
        } 
        if ((f1 == 0.0F && f2 == 0.0F) || !fling((int)f1, (int)f2))
          setScrollState(0); 
        resetScroll();
        break;
      case 0:
        this.mScrollPointerId = gapWorker.getPointerId(0);
        i = (int)(gapWorker.getX() + 0.5F);
        this.mLastTouchX = i;
        this.mInitialTouchX = i;
        i = (int)(gapWorker.getY() + 0.5F);
        this.mLastTouchY = i;
        this.mInitialTouchY = i;
        i = 0;
        if (bool3)
          i = false | true; 
        j = i;
        if (bool4)
          j = i | 0x2; 
        startNestedScroll(j, 0);
        i = bool1;
        break;
    } 
    if (i == 0)
      this.mVelocityTracker.addMovement(motionEvent); 
    motionEvent.recycle();
    return true;
  }
  
  void postAnimationRunner() {
    if (!this.mPostedAnimatorRunner && this.mIsAttached) {
      ViewCompat.postOnAnimation((View)this, this.mItemAnimatorRunner);
      this.mPostedAnimatorRunner = true;
    } 
  }
  
  void processDataSetCompletelyChanged(boolean paramBoolean) {
    this.mDispatchItemsChangedEvent |= paramBoolean;
    this.mDataSetHasChangedAfterLayout = true;
    markKnownViewsInvalid();
  }
  
  void recordAnimationInfoIfBouncedHiddenView(ViewHolder paramViewHolder, ItemAnimator.ItemHolderInfo paramItemHolderInfo) {
    paramViewHolder.setFlags(0, 8192);
    if (this.mState.mTrackOldChangeHolders && paramViewHolder.isUpdated() && !paramViewHolder.isRemoved() && !paramViewHolder.shouldIgnore()) {
      long l = getChangedHolderKey(paramViewHolder);
      this.mViewInfoStore.addToOldChangeHolders(l, paramViewHolder);
    } 
    this.mViewInfoStore.addToPreLayout(paramViewHolder, paramItemHolderInfo);
  }
  
  void removeAndRecycleViews() {
    ItemAnimator itemAnimator = this.mItemAnimator;
    if (itemAnimator != null)
      itemAnimator.endAnimations(); 
    LayoutManager layoutManager = this.mLayout;
    if (layoutManager != null) {
      layoutManager.removeAndRecycleAllViews(this.mRecycler);
      this.mLayout.removeAndRecycleScrapInt(this.mRecycler);
    } 
    this.mRecycler.clear();
  }
  
  boolean removeAnimatingView(View paramView) {
    startInterceptRequestLayout();
    boolean bool = this.mChildHelper.removeViewIfHidden(paramView);
    if (bool) {
      ViewHolder viewHolder = getChildViewHolderInt(paramView);
      this.mRecycler.unscrapView(viewHolder);
      this.mRecycler.recycleViewHolderInternal(viewHolder);
    } 
    stopInterceptRequestLayout(bool ^ true);
    return bool;
  }
  
  protected void removeDetachedView(View paramView, boolean paramBoolean) {
    ViewHolder viewHolder = getChildViewHolderInt(paramView);
    if (viewHolder != null)
      if (viewHolder.isTmpDetached()) {
        viewHolder.clearTmpDetachFlag();
      } else if (!viewHolder.shouldIgnore()) {
        throw new IllegalArgumentException("Called removeDetachedView with a view which is not flagged as tmp detached." + viewHolder + exceptionLabel());
      }  
    paramView.clearAnimation();
    dispatchChildDetached(paramView);
    super.removeDetachedView(paramView, paramBoolean);
  }
  
  public void removeItemDecoration(ItemDecoration paramItemDecoration) {
    LayoutManager layoutManager = this.mLayout;
    if (layoutManager != null)
      layoutManager.assertNotInLayoutOrScroll("Cannot remove item decoration during a scroll  or layout"); 
    this.mItemDecorations.remove(paramItemDecoration);
    if (this.mItemDecorations.isEmpty()) {
      boolean bool;
      if (getOverScrollMode() == 2) {
        bool = true;
      } else {
        bool = false;
      } 
      setWillNotDraw(bool);
    } 
    markItemDecorInsetsDirty();
    requestLayout();
  }
  
  public void removeItemDecorationAt(int paramInt) {
    int i = getItemDecorationCount();
    if (paramInt >= 0 && paramInt < i) {
      removeItemDecoration(getItemDecorationAt(paramInt));
      return;
    } 
    throw new IndexOutOfBoundsException(paramInt + " is an invalid index for size " + i);
  }
  
  public void removeOnChildAttachStateChangeListener(OnChildAttachStateChangeListener paramOnChildAttachStateChangeListener) {
    List<OnChildAttachStateChangeListener> list = this.mOnChildAttachStateListeners;
    if (list == null)
      return; 
    list.remove(paramOnChildAttachStateChangeListener);
  }
  
  public void removeOnItemTouchListener(OnItemTouchListener paramOnItemTouchListener) {
    this.mOnItemTouchListeners.remove(paramOnItemTouchListener);
    if (this.mInterceptingOnItemTouchListener == paramOnItemTouchListener)
      this.mInterceptingOnItemTouchListener = null; 
  }
  
  public void removeOnScrollListener(OnScrollListener paramOnScrollListener) {
    List<OnScrollListener> list = this.mScrollListeners;
    if (list != null)
      list.remove(paramOnScrollListener); 
  }
  
  void repositionShadowingViews() {
    int i = this.mChildHelper.getChildCount();
    for (byte b = 0; b < i; b++) {
      View view = this.mChildHelper.getChildAt(b);
      ViewHolder viewHolder = getChildViewHolder(view);
      if (viewHolder != null && viewHolder.mShadowingHolder != null) {
        View view1 = viewHolder.mShadowingHolder.itemView;
        int k = view.getLeft();
        int j = view.getTop();
        if (k != view1.getLeft() || j != view1.getTop())
          view1.layout(k, j, view1.getWidth() + k, view1.getHeight() + j); 
      } 
    } 
  }
  
  public void requestChildFocus(View paramView1, View paramView2) {
    if (!this.mLayout.onRequestChildFocus(this, this.mState, paramView1, paramView2) && paramView2 != null)
      requestChildOnScreen(paramView1, paramView2); 
    super.requestChildFocus(paramView1, paramView2);
  }
  
  public boolean requestChildRectangleOnScreen(View paramView, Rect paramRect, boolean paramBoolean) {
    return this.mLayout.requestChildRectangleOnScreen(this, paramView, paramRect, paramBoolean);
  }
  
  public void requestDisallowInterceptTouchEvent(boolean paramBoolean) {
    int i = this.mOnItemTouchListeners.size();
    for (byte b = 0; b < i; b++)
      ((OnItemTouchListener)this.mOnItemTouchListeners.get(b)).onRequestDisallowInterceptTouchEvent(paramBoolean); 
    super.requestDisallowInterceptTouchEvent(paramBoolean);
  }
  
  public void requestLayout() {
    if (this.mInterceptRequestLayoutDepth == 0 && !this.mLayoutSuppressed) {
      super.requestLayout();
    } else {
      this.mLayoutWasDefered = true;
    } 
  }
  
  void saveOldPositions() {
    int i = this.mChildHelper.getUnfilteredChildCount();
    for (byte b = 0; b < i; b++) {
      ViewHolder viewHolder = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(b));
      if (!viewHolder.shouldIgnore())
        viewHolder.saveOldPosition(); 
    } 
  }
  
  public void scrollBy(int paramInt1, int paramInt2) {
    LayoutManager layoutManager = this.mLayout;
    if (layoutManager == null) {
      Log.e("RecyclerView", "Cannot scroll without a LayoutManager set. Call setLayoutManager with a non-null argument.");
      return;
    } 
    if (this.mLayoutSuppressed)
      return; 
    boolean bool2 = layoutManager.canScrollHorizontally();
    boolean bool1 = this.mLayout.canScrollVertically();
    if (bool2 || bool1) {
      int i = 0;
      if (!bool2)
        paramInt1 = 0; 
      if (bool1)
        i = paramInt2; 
      scrollByInternal(paramInt1, i, (MotionEvent)null);
    } 
  }
  
  boolean scrollByInternal(int paramInt1, int paramInt2, MotionEvent paramMotionEvent) {
    boolean bool1;
    boolean bool2;
    byte b1;
    byte b2;
    boolean bool3;
    consumePendingUpdateOperations();
    Adapter adapter = this.mAdapter;
    boolean bool5 = true;
    if (adapter != null) {
      int[] arrayOfInt = this.mReusableIntPair;
      arrayOfInt[0] = 0;
      arrayOfInt[1] = 0;
      scrollStep(paramInt1, paramInt2, arrayOfInt);
      arrayOfInt = this.mReusableIntPair;
      bool2 = arrayOfInt[0];
      bool1 = arrayOfInt[1];
      b2 = paramInt1 - bool2;
      b1 = paramInt2 - bool1;
    } else {
      b2 = 0;
      b1 = 0;
      bool2 = false;
      bool1 = false;
    } 
    if (!this.mItemDecorations.isEmpty())
      invalidate(); 
    int[] arrayOfInt1 = this.mReusableIntPair;
    arrayOfInt1[0] = 0;
    arrayOfInt1[1] = 0;
    dispatchNestedScroll(bool2, bool1, b2, b1, this.mScrollOffset, 0, arrayOfInt1);
    arrayOfInt1 = this.mReusableIntPair;
    int j = arrayOfInt1[0];
    int i = arrayOfInt1[1];
    if (arrayOfInt1[0] != 0 || arrayOfInt1[1] != 0) {
      bool3 = true;
    } else {
      bool3 = false;
    } 
    int k = this.mLastTouchX;
    int[] arrayOfInt2 = this.mScrollOffset;
    this.mLastTouchX = k - arrayOfInt2[0];
    this.mLastTouchY -= arrayOfInt2[1];
    arrayOfInt1 = this.mNestedOffsets;
    arrayOfInt1[0] = arrayOfInt1[0] + arrayOfInt2[0];
    arrayOfInt1[1] = arrayOfInt1[1] + arrayOfInt2[1];
    if (getOverScrollMode() != 2) {
      if (paramMotionEvent != null && !MotionEventCompat.isFromSource(paramMotionEvent, 8194))
        pullGlows(paramMotionEvent.getX(), (b2 - j), paramMotionEvent.getY(), (b1 - i)); 
      considerReleasingGlowsOnScroll(paramInt1, paramInt2);
    } 
    if (bool2 || bool1)
      dispatchOnScrolled(bool2, bool1); 
    if (!awakenScrollBars())
      invalidate(); 
    boolean bool4 = bool5;
    if (!bool3) {
      bool4 = bool5;
      if (!bool2)
        if (bool1) {
          bool4 = bool5;
        } else {
          bool4 = false;
        }  
    } 
    return bool4;
  }
  
  void scrollStep(int paramInt1, int paramInt2, int[] paramArrayOfint) {
    startInterceptRequestLayout();
    onEnterLayoutOrScroll();
    TraceCompat.beginSection("RV Scroll");
    fillRemainingScrollValues(this.mState);
    int i = 0;
    boolean bool = false;
    if (paramInt1 != 0)
      i = this.mLayout.scrollHorizontallyBy(paramInt1, this.mRecycler, this.mState); 
    paramInt1 = bool;
    if (paramInt2 != 0)
      paramInt1 = this.mLayout.scrollVerticallyBy(paramInt2, this.mRecycler, this.mState); 
    TraceCompat.endSection();
    repositionShadowingViews();
    onExitLayoutOrScroll();
    stopInterceptRequestLayout(false);
    if (paramArrayOfint != null) {
      paramArrayOfint[0] = i;
      paramArrayOfint[1] = paramInt1;
    } 
  }
  
  public void scrollTo(int paramInt1, int paramInt2) {
    Log.w("RecyclerView", "RecyclerView does not support scrolling to an absolute position. Use scrollToPosition instead");
  }
  
  public void scrollToPosition(int paramInt) {
    if (this.mLayoutSuppressed)
      return; 
    stopScroll();
    LayoutManager layoutManager = this.mLayout;
    if (layoutManager == null) {
      Log.e("RecyclerView", "Cannot scroll to position a LayoutManager set. Call setLayoutManager with a non-null argument.");
      return;
    } 
    layoutManager.scrollToPosition(paramInt);
    awakenScrollBars();
  }
  
  public void sendAccessibilityEventUnchecked(AccessibilityEvent paramAccessibilityEvent) {
    if (shouldDeferAccessibilityEvent(paramAccessibilityEvent))
      return; 
    super.sendAccessibilityEventUnchecked(paramAccessibilityEvent);
  }
  
  public void setAccessibilityDelegateCompat(RecyclerViewAccessibilityDelegate paramRecyclerViewAccessibilityDelegate) {
    this.mAccessibilityDelegate = paramRecyclerViewAccessibilityDelegate;
    ViewCompat.setAccessibilityDelegate((View)this, paramRecyclerViewAccessibilityDelegate);
  }
  
  public void setAdapter(Adapter paramAdapter) {
    setLayoutFrozen(false);
    setAdapterInternal(paramAdapter, false, true);
    processDataSetCompletelyChanged(false);
    requestLayout();
  }
  
  public void setChildDrawingOrderCallback(ChildDrawingOrderCallback paramChildDrawingOrderCallback) {
    boolean bool;
    if (paramChildDrawingOrderCallback == this.mChildDrawingOrderCallback)
      return; 
    this.mChildDrawingOrderCallback = paramChildDrawingOrderCallback;
    if (paramChildDrawingOrderCallback != null) {
      bool = true;
    } else {
      bool = false;
    } 
    setChildrenDrawingOrderEnabled(bool);
  }
  
  boolean setChildImportantForAccessibilityInternal(ViewHolder paramViewHolder, int paramInt) {
    if (isComputingLayout()) {
      paramViewHolder.mPendingAccessibilityState = paramInt;
      this.mPendingAccessibilityImportanceChange.add(paramViewHolder);
      return false;
    } 
    ViewCompat.setImportantForAccessibility(paramViewHolder.itemView, paramInt);
    return true;
  }
  
  public void setClipToPadding(boolean paramBoolean) {
    if (paramBoolean != this.mClipToPadding)
      invalidateGlows(); 
    this.mClipToPadding = paramBoolean;
    super.setClipToPadding(paramBoolean);
    if (this.mFirstLayoutComplete)
      requestLayout(); 
  }
  
  public void setEdgeEffectFactory(EdgeEffectFactory paramEdgeEffectFactory) {
    Preconditions.checkNotNull(paramEdgeEffectFactory);
    this.mEdgeEffectFactory = paramEdgeEffectFactory;
    invalidateGlows();
  }
  
  public void setHasFixedSize(boolean paramBoolean) {
    this.mHasFixedSize = paramBoolean;
  }
  
  public void setItemAnimator(ItemAnimator paramItemAnimator) {
    ItemAnimator itemAnimator = this.mItemAnimator;
    if (itemAnimator != null) {
      itemAnimator.endAnimations();
      this.mItemAnimator.setListener(null);
    } 
    this.mItemAnimator = paramItemAnimator;
    if (paramItemAnimator != null)
      paramItemAnimator.setListener(this.mItemAnimatorListener); 
  }
  
  public void setItemViewCacheSize(int paramInt) {
    this.mRecycler.setViewCacheSize(paramInt);
  }
  
  @Deprecated
  public void setLayoutFrozen(boolean paramBoolean) {
    suppressLayout(paramBoolean);
  }
  
  public void setLayoutManager(LayoutManager paramLayoutManager) {
    if (paramLayoutManager == this.mLayout)
      return; 
    stopScroll();
    if (this.mLayout != null) {
      ItemAnimator itemAnimator = this.mItemAnimator;
      if (itemAnimator != null)
        itemAnimator.endAnimations(); 
      this.mLayout.removeAndRecycleAllViews(this.mRecycler);
      this.mLayout.removeAndRecycleScrapInt(this.mRecycler);
      this.mRecycler.clear();
      if (this.mIsAttached)
        this.mLayout.dispatchDetachedFromWindow(this, this.mRecycler); 
      this.mLayout.setRecyclerView(null);
      this.mLayout = null;
    } else {
      this.mRecycler.clear();
    } 
    this.mChildHelper.removeAllViewsUnfiltered();
    this.mLayout = paramLayoutManager;
    if (paramLayoutManager != null)
      if (paramLayoutManager.mRecyclerView == null) {
        this.mLayout.setRecyclerView(this);
        if (this.mIsAttached)
          this.mLayout.dispatchAttachedToWindow(this); 
      } else {
        throw new IllegalArgumentException("LayoutManager " + paramLayoutManager + " is already attached to a RecyclerView:" + paramLayoutManager.mRecyclerView.exceptionLabel());
      }  
    this.mRecycler.updateViewCacheSize();
    requestLayout();
  }
  
  @Deprecated
  public void setLayoutTransition(LayoutTransition paramLayoutTransition) {
    if (Build.VERSION.SDK_INT < 18) {
      if (paramLayoutTransition == null) {
        suppressLayout(false);
        return;
      } 
      if (paramLayoutTransition.getAnimator(0) == null && paramLayoutTransition.getAnimator(1) == null && paramLayoutTransition.getAnimator(2) == null && paramLayoutTransition.getAnimator(3) == null && paramLayoutTransition.getAnimator(4) == null) {
        suppressLayout(true);
        return;
      } 
    } 
    if (paramLayoutTransition == null) {
      super.setLayoutTransition(null);
      return;
    } 
    throw new IllegalArgumentException("Providing a LayoutTransition into RecyclerView is not supported. Please use setItemAnimator() instead for animating changes to the items in this RecyclerView");
  }
  
  public void setNestedScrollingEnabled(boolean paramBoolean) {
    getScrollingChildHelper().setNestedScrollingEnabled(paramBoolean);
  }
  
  public void setOnFlingListener(OnFlingListener paramOnFlingListener) {
    this.mOnFlingListener = paramOnFlingListener;
  }
  
  @Deprecated
  public void setOnScrollListener(OnScrollListener paramOnScrollListener) {
    this.mScrollListener = paramOnScrollListener;
  }
  
  public void setPreserveFocusAfterLayout(boolean paramBoolean) {
    this.mPreserveFocusAfterLayout = paramBoolean;
  }
  
  public void setRecycledViewPool(RecycledViewPool paramRecycledViewPool) {
    this.mRecycler.setRecycledViewPool(paramRecycledViewPool);
  }
  
  public void setRecyclerListener(RecyclerListener paramRecyclerListener) {
    this.mRecyclerListener = paramRecyclerListener;
  }
  
  void setScrollState(int paramInt) {
    if (paramInt == this.mScrollState)
      return; 
    this.mScrollState = paramInt;
    if (paramInt != 2)
      stopScrollersInternal(); 
    dispatchOnScrollStateChanged(paramInt);
  }
  
  public void setScrollingTouchSlop(int paramInt) {
    ViewConfiguration viewConfiguration = ViewConfiguration.get(getContext());
    switch (paramInt) {
      default:
        Log.w("RecyclerView", "setScrollingTouchSlop(): bad argument constant " + paramInt + "; using default value");
        break;
      case 1:
        this.mTouchSlop = viewConfiguration.getScaledPagingTouchSlop();
        return;
      case 0:
        break;
    } 
    this.mTouchSlop = viewConfiguration.getScaledTouchSlop();
  }
  
  public void setViewCacheExtension(ViewCacheExtension paramViewCacheExtension) {
    this.mRecycler.setViewCacheExtension(paramViewCacheExtension);
  }
  
  boolean shouldDeferAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent) {
    if (isComputingLayout()) {
      int i = 0;
      if (paramAccessibilityEvent != null)
        i = AccessibilityEventCompat.getContentChangeTypes(paramAccessibilityEvent); 
      int j = i;
      if (i == 0)
        j = 0; 
      this.mEatenAccessibilityChangeFlags |= j;
      return true;
    } 
    return false;
  }
  
  public void smoothScrollBy(int paramInt1, int paramInt2) {
    smoothScrollBy(paramInt1, paramInt2, (Interpolator)null);
  }
  
  public void smoothScrollBy(int paramInt1, int paramInt2, Interpolator paramInterpolator) {
    smoothScrollBy(paramInt1, paramInt2, paramInterpolator, -2147483648);
  }
  
  public void smoothScrollBy(int paramInt1, int paramInt2, Interpolator paramInterpolator, int paramInt3) {
    smoothScrollBy(paramInt1, paramInt2, paramInterpolator, paramInt3, false);
  }
  
  void smoothScrollBy(int paramInt1, int paramInt2, Interpolator paramInterpolator, int paramInt3, boolean paramBoolean) {
    LayoutManager layoutManager = this.mLayout;
    if (layoutManager == null) {
      Log.e("RecyclerView", "Cannot smooth scroll without a LayoutManager set. Call setLayoutManager with a non-null argument.");
      return;
    } 
    if (this.mLayoutSuppressed)
      return; 
    int i = paramInt1;
    if (!layoutManager.canScrollHorizontally())
      i = 0; 
    if (!this.mLayout.canScrollVertically())
      paramInt2 = 0; 
    if (i != 0 || paramInt2 != 0) {
      if (paramInt3 == Integer.MIN_VALUE || paramInt3 > 0) {
        paramInt1 = 1;
      } else {
        paramInt1 = 0;
      } 
      if (paramInt1 != 0) {
        if (paramBoolean) {
          paramInt1 = 0;
          if (i != 0)
            paramInt1 = false | true; 
          int j = paramInt1;
          if (paramInt2 != 0)
            j = paramInt1 | 0x2; 
          startNestedScroll(j, 1);
        } 
        this.mViewFlinger.smoothScrollBy(i, paramInt2, paramInt3, paramInterpolator);
      } else {
        scrollBy(i, paramInt2);
      } 
    } 
  }
  
  public void smoothScrollToPosition(int paramInt) {
    if (this.mLayoutSuppressed)
      return; 
    LayoutManager layoutManager = this.mLayout;
    if (layoutManager == null) {
      Log.e("RecyclerView", "Cannot smooth scroll without a LayoutManager set. Call setLayoutManager with a non-null argument.");
      return;
    } 
    layoutManager.smoothScrollToPosition(this, this.mState, paramInt);
  }
  
  void startInterceptRequestLayout() {
    int i = this.mInterceptRequestLayoutDepth + 1;
    this.mInterceptRequestLayoutDepth = i;
    if (i == 1 && !this.mLayoutSuppressed)
      this.mLayoutWasDefered = false; 
  }
  
  public boolean startNestedScroll(int paramInt) {
    return getScrollingChildHelper().startNestedScroll(paramInt);
  }
  
  public boolean startNestedScroll(int paramInt1, int paramInt2) {
    return getScrollingChildHelper().startNestedScroll(paramInt1, paramInt2);
  }
  
  void stopInterceptRequestLayout(boolean paramBoolean) {
    if (this.mInterceptRequestLayoutDepth < 1)
      this.mInterceptRequestLayoutDepth = 1; 
    if (!paramBoolean && !this.mLayoutSuppressed)
      this.mLayoutWasDefered = false; 
    if (this.mInterceptRequestLayoutDepth == 1) {
      if (paramBoolean && this.mLayoutWasDefered && !this.mLayoutSuppressed && this.mLayout != null && this.mAdapter != null)
        dispatchLayout(); 
      if (!this.mLayoutSuppressed)
        this.mLayoutWasDefered = false; 
    } 
    this.mInterceptRequestLayoutDepth--;
  }
  
  public void stopNestedScroll() {
    getScrollingChildHelper().stopNestedScroll();
  }
  
  public void stopNestedScroll(int paramInt) {
    getScrollingChildHelper().stopNestedScroll(paramInt);
  }
  
  public void stopScroll() {
    setScrollState(0);
    stopScrollersInternal();
  }
  
  public final void suppressLayout(boolean paramBoolean) {
    if (paramBoolean != this.mLayoutSuppressed) {
      assertNotInLayoutOrScroll("Do not suppressLayout in layout or scroll");
      if (!paramBoolean) {
        this.mLayoutSuppressed = false;
        if (this.mLayoutWasDefered && this.mLayout != null && this.mAdapter != null)
          requestLayout(); 
        this.mLayoutWasDefered = false;
      } else {
        long l = SystemClock.uptimeMillis();
        onTouchEvent(MotionEvent.obtain(l, l, 3, 0.0F, 0.0F, 0));
        this.mLayoutSuppressed = true;
        this.mIgnoreMotionEventTillDown = true;
        stopScroll();
      } 
    } 
  }
  
  public void swapAdapter(Adapter paramAdapter, boolean paramBoolean) {
    setLayoutFrozen(false);
    setAdapterInternal(paramAdapter, true, paramBoolean);
    processDataSetCompletelyChanged(true);
    requestLayout();
  }
  
  void viewRangeUpdate(int paramInt1, int paramInt2, Object paramObject) {
    int i = this.mChildHelper.getUnfilteredChildCount();
    for (byte b = 0; b < i; b++) {
      View view = this.mChildHelper.getUnfilteredChildAt(b);
      ViewHolder viewHolder = getChildViewHolderInt(view);
      if (viewHolder != null && !viewHolder.shouldIgnore() && viewHolder.mPosition >= paramInt1 && viewHolder.mPosition < paramInt1 + paramInt2) {
        viewHolder.addFlags(2);
        viewHolder.addChangePayload(paramObject);
        ((LayoutParams)view.getLayoutParams()).mInsetsDirty = true;
      } 
    } 
    this.mRecycler.viewRangeUpdate(paramInt1, paramInt2);
  }
  
  static {
    boolean bool;
  }
  
  static final boolean ALLOW_SIZE_IN_UNSPECIFIED_SPEC;
  
  static final boolean ALLOW_THREAD_GAP_WORK;
  
  static final boolean DEBUG = false;
  
  static final int DEFAULT_ORIENTATION = 1;
  
  static final boolean DISPATCH_TEMP_DETACH = false;
  
  private static final boolean FORCE_ABS_FOCUS_SEARCH_DIRECTION;
  
  static final boolean FORCE_INVALIDATE_DISPLAY_LIST;
  
  static final long FOREVER_NS = 9223372036854775807L;
  
  public static final int HORIZONTAL = 0;
  
  private static final boolean IGNORE_DETACHED_FOCUSED_CHILD;
  
  private static final int INVALID_POINTER = -1;
  
  public static final int INVALID_TYPE = -1;
  
  private static final Class<?>[] LAYOUT_MANAGER_CONSTRUCTOR_SIGNATURE;
  
  static final int MAX_SCROLL_DURATION = 2000;
  
  private static final int[] NESTED_SCROLLING_ATTRS = new int[] { 16843830 };
  
  public static final long NO_ID = -1L;
  
  public static final int NO_POSITION = -1;
  
  static final boolean POST_UPDATES_ON_ANIMATION;
  
  public static final int SCROLL_STATE_DRAGGING = 1;
  
  public static final int SCROLL_STATE_IDLE = 0;
  
  public static final int SCROLL_STATE_SETTLING = 2;
  
  static final String TAG = "RecyclerView";
  
  public static final int TOUCH_SLOP_DEFAULT = 0;
  
  public static final int TOUCH_SLOP_PAGING = 1;
  
  static final String TRACE_BIND_VIEW_TAG = "RV OnBindView";
  
  static final String TRACE_CREATE_VIEW_TAG = "RV CreateView";
  
  private static final String TRACE_HANDLE_ADAPTER_UPDATES_TAG = "RV PartialInvalidate";
  
  static final String TRACE_NESTED_PREFETCH_TAG = "RV Nested Prefetch";
  
  private static final String TRACE_ON_DATA_SET_CHANGE_LAYOUT_TAG = "RV FullInvalidate";
  
  private static final String TRACE_ON_LAYOUT_TAG = "RV OnLayout";
  
  static final String TRACE_PREFETCH_TAG = "RV Prefetch";
  
  static final String TRACE_SCROLL_TAG = "RV Scroll";
  
  public static final int UNDEFINED_DURATION = -2147483648;
  
  static final boolean VERBOSE_TRACING = false;
  
  public static final int VERTICAL = 1;
  
  static final Interpolator sQuinticInterpolator;
  
  RecyclerViewAccessibilityDelegate mAccessibilityDelegate;
  
  private final AccessibilityManager mAccessibilityManager;
  
  Adapter mAdapter;
  
  AdapterHelper mAdapterHelper;
  
  boolean mAdapterUpdateDuringMeasure;
  
  private EdgeEffect mBottomGlow;
  
  private ChildDrawingOrderCallback mChildDrawingOrderCallback;
  
  ChildHelper mChildHelper;
  
  boolean mClipToPadding;
  
  boolean mDataSetHasChangedAfterLayout;
  
  boolean mDispatchItemsChangedEvent;
  
  private int mDispatchScrollCounter;
  
  private int mEatenAccessibilityChangeFlags;
  
  private EdgeEffectFactory mEdgeEffectFactory;
  
  boolean mEnableFastScroller;
  
  boolean mFirstLayoutComplete;
  
  GapWorker mGapWorker;
  
  boolean mHasFixedSize;
  
  private boolean mIgnoreMotionEventTillDown;
  
  private int mInitialTouchX;
  
  private int mInitialTouchY;
  
  private int mInterceptRequestLayoutDepth;
  
  private OnItemTouchListener mInterceptingOnItemTouchListener;
  
  boolean mIsAttached;
  
  ItemAnimator mItemAnimator;
  
  private ItemAnimator.ItemAnimatorListener mItemAnimatorListener;
  
  private Runnable mItemAnimatorRunner;
  
  final ArrayList<ItemDecoration> mItemDecorations;
  
  boolean mItemsAddedOrRemoved;
  
  boolean mItemsChanged;
  
  private int mLastTouchX;
  
  private int mLastTouchY;
  
  LayoutManager mLayout;
  
  private int mLayoutOrScrollCounter;
  
  boolean mLayoutSuppressed;
  
  boolean mLayoutWasDefered;
  
  private EdgeEffect mLeftGlow;
  
  private final int mMaxFlingVelocity;
  
  private final int mMinFlingVelocity;
  
  private final int[] mMinMaxLayoutPositions;
  
  private final int[] mNestedOffsets;
  
  private final RecyclerViewDataObserver mObserver;
  
  private List<OnChildAttachStateChangeListener> mOnChildAttachStateListeners;
  
  private OnFlingListener mOnFlingListener;
  
  private final ArrayList<OnItemTouchListener> mOnItemTouchListeners;
  
  final List<ViewHolder> mPendingAccessibilityImportanceChange;
  
  private SavedState mPendingSavedState;
  
  boolean mPostedAnimatorRunner;
  
  GapWorker.LayoutPrefetchRegistryImpl mPrefetchRegistry;
  
  private boolean mPreserveFocusAfterLayout;
  
  final Recycler mRecycler;
  
  RecyclerListener mRecyclerListener;
  
  final int[] mReusableIntPair;
  
  private EdgeEffect mRightGlow;
  
  private float mScaledHorizontalScrollFactor;
  
  private float mScaledVerticalScrollFactor;
  
  private OnScrollListener mScrollListener;
  
  private List<OnScrollListener> mScrollListeners;
  
  private final int[] mScrollOffset;
  
  private int mScrollPointerId;
  
  private int mScrollState;
  
  private NestedScrollingChildHelper mScrollingChildHelper;
  
  final State mState;
  
  final Rect mTempRect;
  
  private final Rect mTempRect2;
  
  final RectF mTempRectF;
  
  private EdgeEffect mTopGlow;
  
  private int mTouchSlop;
  
  final Runnable mUpdateChildViewsRunnable;
  
  private VelocityTracker mVelocityTracker;
  
  final ViewFlinger mViewFlinger;
  
  private final ViewInfoStore.ProcessCallback mViewInfoProcessCallback;
  
  final ViewInfoStore mViewInfoStore;
  
  public static abstract class Adapter<VH extends ViewHolder> {
    private boolean mHasStableIds = false;
    
    private final RecyclerView.AdapterDataObservable mObservable = new RecyclerView.AdapterDataObservable();
    
    public final void bindViewHolder(VH param1VH, int param1Int) {
      ((RecyclerView.ViewHolder)param1VH).mPosition = param1Int;
      if (hasStableIds())
        ((RecyclerView.ViewHolder)param1VH).mItemId = getItemId(param1Int); 
      param1VH.setFlags(1, 519);
      TraceCompat.beginSection("RV OnBindView");
      onBindViewHolder(param1VH, param1Int, param1VH.getUnmodifiedPayloads());
      param1VH.clearPayload();
      ViewGroup.LayoutParams layoutParams = ((RecyclerView.ViewHolder)param1VH).itemView.getLayoutParams();
      if (layoutParams instanceof RecyclerView.LayoutParams)
        ((RecyclerView.LayoutParams)layoutParams).mInsetsDirty = true; 
      TraceCompat.endSection();
    }
    
    public final VH createViewHolder(ViewGroup param1ViewGroup, int param1Int) {
      try {
        TraceCompat.beginSection("RV CreateView");
        param1ViewGroup = (ViewGroup)onCreateViewHolder(param1ViewGroup, param1Int);
        if (((RecyclerView.ViewHolder)param1ViewGroup).itemView.getParent() == null) {
          ((RecyclerView.ViewHolder)param1ViewGroup).mItemViewType = param1Int;
          return (VH)param1ViewGroup;
        } 
        IllegalStateException illegalStateException = new IllegalStateException();
        this("ViewHolder views must not be attached when created. Ensure that you are not passing 'true' to the attachToRoot parameter of LayoutInflater.inflate(..., boolean attachToRoot)");
        throw illegalStateException;
      } finally {
        TraceCompat.endSection();
      } 
    }
    
    public abstract int getItemCount();
    
    public long getItemId(int param1Int) {
      return -1L;
    }
    
    public int getItemViewType(int param1Int) {
      return 0;
    }
    
    public final boolean hasObservers() {
      return this.mObservable.hasObservers();
    }
    
    public final boolean hasStableIds() {
      return this.mHasStableIds;
    }
    
    public final void notifyDataSetChanged() {
      this.mObservable.notifyChanged();
    }
    
    public final void notifyItemChanged(int param1Int) {
      this.mObservable.notifyItemRangeChanged(param1Int, 1);
    }
    
    public final void notifyItemChanged(int param1Int, Object param1Object) {
      this.mObservable.notifyItemRangeChanged(param1Int, 1, param1Object);
    }
    
    public final void notifyItemInserted(int param1Int) {
      this.mObservable.notifyItemRangeInserted(param1Int, 1);
    }
    
    public final void notifyItemMoved(int param1Int1, int param1Int2) {
      this.mObservable.notifyItemMoved(param1Int1, param1Int2);
    }
    
    public final void notifyItemRangeChanged(int param1Int1, int param1Int2) {
      this.mObservable.notifyItemRangeChanged(param1Int1, param1Int2);
    }
    
    public final void notifyItemRangeChanged(int param1Int1, int param1Int2, Object param1Object) {
      this.mObservable.notifyItemRangeChanged(param1Int1, param1Int2, param1Object);
    }
    
    public final void notifyItemRangeInserted(int param1Int1, int param1Int2) {
      this.mObservable.notifyItemRangeInserted(param1Int1, param1Int2);
    }
    
    public final void notifyItemRangeRemoved(int param1Int1, int param1Int2) {
      this.mObservable.notifyItemRangeRemoved(param1Int1, param1Int2);
    }
    
    public final void notifyItemRemoved(int param1Int) {
      this.mObservable.notifyItemRangeRemoved(param1Int, 1);
    }
    
    public void onAttachedToRecyclerView(RecyclerView param1RecyclerView) {}
    
    public abstract void onBindViewHolder(VH param1VH, int param1Int);
    
    public void onBindViewHolder(VH param1VH, int param1Int, List<Object> param1List) {
      onBindViewHolder(param1VH, param1Int);
    }
    
    public abstract VH onCreateViewHolder(ViewGroup param1ViewGroup, int param1Int);
    
    public void onDetachedFromRecyclerView(RecyclerView param1RecyclerView) {}
    
    public boolean onFailedToRecycleView(VH param1VH) {
      return false;
    }
    
    public void onViewAttachedToWindow(VH param1VH) {}
    
    public void onViewDetachedFromWindow(VH param1VH) {}
    
    public void onViewRecycled(VH param1VH) {}
    
    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver param1AdapterDataObserver) {
      this.mObservable.registerObserver(param1AdapterDataObserver);
    }
    
    public void setHasStableIds(boolean param1Boolean) {
      if (!hasObservers()) {
        this.mHasStableIds = param1Boolean;
        return;
      } 
      throw new IllegalStateException("Cannot change whether this adapter has stable IDs while the adapter has registered observers.");
    }
    
    public void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver param1AdapterDataObserver) {
      this.mObservable.unregisterObserver(param1AdapterDataObserver);
    }
  }
  
  static class AdapterDataObservable extends Observable<AdapterDataObserver> {
    public boolean hasObservers() {
      return this.mObservers.isEmpty() ^ true;
    }
    
    public void notifyChanged() {
      for (int i = this.mObservers.size() - 1; i >= 0; i--)
        ((RecyclerView.AdapterDataObserver)this.mObservers.get(i)).onChanged(); 
    }
    
    public void notifyItemMoved(int param1Int1, int param1Int2) {
      for (int i = this.mObservers.size() - 1; i >= 0; i--)
        ((RecyclerView.AdapterDataObserver)this.mObservers.get(i)).onItemRangeMoved(param1Int1, param1Int2, 1); 
    }
    
    public void notifyItemRangeChanged(int param1Int1, int param1Int2) {
      notifyItemRangeChanged(param1Int1, param1Int2, (Object)null);
    }
    
    public void notifyItemRangeChanged(int param1Int1, int param1Int2, Object param1Object) {
      for (int i = this.mObservers.size() - 1; i >= 0; i--)
        ((RecyclerView.AdapterDataObserver)this.mObservers.get(i)).onItemRangeChanged(param1Int1, param1Int2, param1Object); 
    }
    
    public void notifyItemRangeInserted(int param1Int1, int param1Int2) {
      for (int i = this.mObservers.size() - 1; i >= 0; i--)
        ((RecyclerView.AdapterDataObserver)this.mObservers.get(i)).onItemRangeInserted(param1Int1, param1Int2); 
    }
    
    public void notifyItemRangeRemoved(int param1Int1, int param1Int2) {
      for (int i = this.mObservers.size() - 1; i >= 0; i--)
        ((RecyclerView.AdapterDataObserver)this.mObservers.get(i)).onItemRangeRemoved(param1Int1, param1Int2); 
    }
  }
  
  public static abstract class AdapterDataObserver {
    public void onChanged() {}
    
    public void onItemRangeChanged(int param1Int1, int param1Int2) {}
    
    public void onItemRangeChanged(int param1Int1, int param1Int2, Object param1Object) {
      onItemRangeChanged(param1Int1, param1Int2);
    }
    
    public void onItemRangeInserted(int param1Int1, int param1Int2) {}
    
    public void onItemRangeMoved(int param1Int1, int param1Int2, int param1Int3) {}
    
    public void onItemRangeRemoved(int param1Int1, int param1Int2) {}
  }
  
  public static interface ChildDrawingOrderCallback {
    int onGetChildDrawingOrder(int param1Int1, int param1Int2);
  }
  
  public static class EdgeEffectFactory {
    public static final int DIRECTION_BOTTOM = 3;
    
    public static final int DIRECTION_LEFT = 0;
    
    public static final int DIRECTION_RIGHT = 2;
    
    public static final int DIRECTION_TOP = 1;
    
    protected EdgeEffect createEdgeEffect(RecyclerView param1RecyclerView, int param1Int) {
      return new EdgeEffect(param1RecyclerView.getContext());
    }
    
    @Retention(RetentionPolicy.SOURCE)
    public static @interface EdgeDirection {}
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface EdgeDirection {}
  
  public static abstract class ItemAnimator {
    public static final int FLAG_APPEARED_IN_PRE_LAYOUT = 4096;
    
    public static final int FLAG_CHANGED = 2;
    
    public static final int FLAG_INVALIDATED = 4;
    
    public static final int FLAG_MOVED = 2048;
    
    public static final int FLAG_REMOVED = 8;
    
    private long mAddDuration = 120L;
    
    private long mChangeDuration = 250L;
    
    private ArrayList<ItemAnimatorFinishedListener> mFinishedListeners = new ArrayList<>();
    
    private ItemAnimatorListener mListener = null;
    
    private long mMoveDuration = 250L;
    
    private long mRemoveDuration = 120L;
    
    static int buildAdapterChangeFlagsForAnimations(RecyclerView.ViewHolder param1ViewHolder) {
      int j = param1ViewHolder.mFlags & 0xE;
      if (param1ViewHolder.isInvalid())
        return 4; 
      int i = j;
      if ((j & 0x4) == 0) {
        int k = param1ViewHolder.getOldPosition();
        int m = param1ViewHolder.getAdapterPosition();
        i = j;
        if (k != -1) {
          i = j;
          if (m != -1) {
            i = j;
            if (k != m)
              i = j | 0x800; 
          } 
        } 
      } 
      return i;
    }
    
    public abstract boolean animateAppearance(RecyclerView.ViewHolder param1ViewHolder, ItemHolderInfo param1ItemHolderInfo1, ItemHolderInfo param1ItemHolderInfo2);
    
    public abstract boolean animateChange(RecyclerView.ViewHolder param1ViewHolder1, RecyclerView.ViewHolder param1ViewHolder2, ItemHolderInfo param1ItemHolderInfo1, ItemHolderInfo param1ItemHolderInfo2);
    
    public abstract boolean animateDisappearance(RecyclerView.ViewHolder param1ViewHolder, ItemHolderInfo param1ItemHolderInfo1, ItemHolderInfo param1ItemHolderInfo2);
    
    public abstract boolean animatePersistence(RecyclerView.ViewHolder param1ViewHolder, ItemHolderInfo param1ItemHolderInfo1, ItemHolderInfo param1ItemHolderInfo2);
    
    public boolean canReuseUpdatedViewHolder(RecyclerView.ViewHolder param1ViewHolder) {
      return true;
    }
    
    public boolean canReuseUpdatedViewHolder(RecyclerView.ViewHolder param1ViewHolder, List<Object> param1List) {
      return canReuseUpdatedViewHolder(param1ViewHolder);
    }
    
    public final void dispatchAnimationFinished(RecyclerView.ViewHolder param1ViewHolder) {
      onAnimationFinished(param1ViewHolder);
      ItemAnimatorListener itemAnimatorListener = this.mListener;
      if (itemAnimatorListener != null)
        itemAnimatorListener.onAnimationFinished(param1ViewHolder); 
    }
    
    public final void dispatchAnimationStarted(RecyclerView.ViewHolder param1ViewHolder) {
      onAnimationStarted(param1ViewHolder);
    }
    
    public final void dispatchAnimationsFinished() {
      int i = this.mFinishedListeners.size();
      for (byte b = 0; b < i; b++)
        ((ItemAnimatorFinishedListener)this.mFinishedListeners.get(b)).onAnimationsFinished(); 
      this.mFinishedListeners.clear();
    }
    
    public abstract void endAnimation(RecyclerView.ViewHolder param1ViewHolder);
    
    public abstract void endAnimations();
    
    public long getAddDuration() {
      return this.mAddDuration;
    }
    
    public long getChangeDuration() {
      return this.mChangeDuration;
    }
    
    public long getMoveDuration() {
      return this.mMoveDuration;
    }
    
    public long getRemoveDuration() {
      return this.mRemoveDuration;
    }
    
    public abstract boolean isRunning();
    
    public final boolean isRunning(ItemAnimatorFinishedListener param1ItemAnimatorFinishedListener) {
      boolean bool = isRunning();
      if (param1ItemAnimatorFinishedListener != null)
        if (!bool) {
          param1ItemAnimatorFinishedListener.onAnimationsFinished();
        } else {
          this.mFinishedListeners.add(param1ItemAnimatorFinishedListener);
        }  
      return bool;
    }
    
    public ItemHolderInfo obtainHolderInfo() {
      return new ItemHolderInfo();
    }
    
    public void onAnimationFinished(RecyclerView.ViewHolder param1ViewHolder) {}
    
    public void onAnimationStarted(RecyclerView.ViewHolder param1ViewHolder) {}
    
    public ItemHolderInfo recordPostLayoutInformation(RecyclerView.State param1State, RecyclerView.ViewHolder param1ViewHolder) {
      return obtainHolderInfo().setFrom(param1ViewHolder);
    }
    
    public ItemHolderInfo recordPreLayoutInformation(RecyclerView.State param1State, RecyclerView.ViewHolder param1ViewHolder, int param1Int, List<Object> param1List) {
      return obtainHolderInfo().setFrom(param1ViewHolder);
    }
    
    public abstract void runPendingAnimations();
    
    public void setAddDuration(long param1Long) {
      this.mAddDuration = param1Long;
    }
    
    public void setChangeDuration(long param1Long) {
      this.mChangeDuration = param1Long;
    }
    
    void setListener(ItemAnimatorListener param1ItemAnimatorListener) {
      this.mListener = param1ItemAnimatorListener;
    }
    
    public void setMoveDuration(long param1Long) {
      this.mMoveDuration = param1Long;
    }
    
    public void setRemoveDuration(long param1Long) {
      this.mRemoveDuration = param1Long;
    }
    
    @Retention(RetentionPolicy.SOURCE)
    public static @interface AdapterChanges {}
    
    public static interface ItemAnimatorFinishedListener {
      void onAnimationsFinished();
    }
    
    static interface ItemAnimatorListener {
      void onAnimationFinished(RecyclerView.ViewHolder param2ViewHolder);
    }
    
    public static class ItemHolderInfo {
      public int bottom;
      
      public int changeFlags;
      
      public int left;
      
      public int right;
      
      public int top;
      
      public ItemHolderInfo setFrom(RecyclerView.ViewHolder param2ViewHolder) {
        return setFrom(param2ViewHolder, 0);
      }
      
      public ItemHolderInfo setFrom(RecyclerView.ViewHolder param2ViewHolder, int param2Int) {
        View view = param2ViewHolder.itemView;
        this.left = view.getLeft();
        this.top = view.getTop();
        this.right = view.getRight();
        this.bottom = view.getBottom();
        return this;
      }
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface AdapterChanges {}
  
  public static interface ItemAnimatorFinishedListener {
    void onAnimationsFinished();
  }
  
  static interface ItemAnimatorListener {
    void onAnimationFinished(RecyclerView.ViewHolder param1ViewHolder);
  }
  
  public static class ItemHolderInfo {
    public int bottom;
    
    public int changeFlags;
    
    public int left;
    
    public int right;
    
    public int top;
    
    public ItemHolderInfo setFrom(RecyclerView.ViewHolder param1ViewHolder) {
      return setFrom(param1ViewHolder, 0);
    }
    
    public ItemHolderInfo setFrom(RecyclerView.ViewHolder param1ViewHolder, int param1Int) {
      View view = param1ViewHolder.itemView;
      this.left = view.getLeft();
      this.top = view.getTop();
      this.right = view.getRight();
      this.bottom = view.getBottom();
      return this;
    }
  }
  
  private class ItemAnimatorRestoreListener implements ItemAnimator.ItemAnimatorListener {
    final RecyclerView this$0;
    
    public void onAnimationFinished(RecyclerView.ViewHolder param1ViewHolder) {
      param1ViewHolder.setIsRecyclable(true);
      if (param1ViewHolder.mShadowedHolder != null && param1ViewHolder.mShadowingHolder == null)
        param1ViewHolder.mShadowedHolder = null; 
      param1ViewHolder.mShadowingHolder = null;
      if (!param1ViewHolder.shouldBeKeptAsChild() && !RecyclerView.this.removeAnimatingView(param1ViewHolder.itemView) && param1ViewHolder.isTmpDetached())
        RecyclerView.this.removeDetachedView(param1ViewHolder.itemView, false); 
    }
  }
  
  public static abstract class ItemDecoration {
    @Deprecated
    public void getItemOffsets(Rect param1Rect, int param1Int, RecyclerView param1RecyclerView) {
      param1Rect.set(0, 0, 0, 0);
    }
    
    public void getItemOffsets(Rect param1Rect, View param1View, RecyclerView param1RecyclerView, RecyclerView.State param1State) {
      getItemOffsets(param1Rect, ((RecyclerView.LayoutParams)param1View.getLayoutParams()).getViewLayoutPosition(), param1RecyclerView);
    }
    
    @Deprecated
    public void onDraw(Canvas param1Canvas, RecyclerView param1RecyclerView) {}
    
    public void onDraw(Canvas param1Canvas, RecyclerView param1RecyclerView, RecyclerView.State param1State) {
      onDraw(param1Canvas, param1RecyclerView);
    }
    
    @Deprecated
    public void onDrawOver(Canvas param1Canvas, RecyclerView param1RecyclerView) {}
    
    public void onDrawOver(Canvas param1Canvas, RecyclerView param1RecyclerView, RecyclerView.State param1State) {
      onDrawOver(param1Canvas, param1RecyclerView);
    }
  }
  
  public static abstract class LayoutManager {
    boolean mAutoMeasure;
    
    ChildHelper mChildHelper;
    
    private int mHeight;
    
    private int mHeightMode;
    
    ViewBoundsCheck mHorizontalBoundCheck;
    
    private final ViewBoundsCheck.Callback mHorizontalBoundCheckCallback;
    
    boolean mIsAttachedToWindow;
    
    private boolean mItemPrefetchEnabled;
    
    private boolean mMeasurementCacheEnabled;
    
    int mPrefetchMaxCountObserved;
    
    boolean mPrefetchMaxObservedInInitialPrefetch;
    
    RecyclerView mRecyclerView;
    
    boolean mRequestedSimpleAnimations;
    
    RecyclerView.SmoothScroller mSmoothScroller;
    
    ViewBoundsCheck mVerticalBoundCheck;
    
    private final ViewBoundsCheck.Callback mVerticalBoundCheckCallback;
    
    private int mWidth;
    
    private int mWidthMode;
    
    public LayoutManager() {
      ViewBoundsCheck.Callback callback1 = new ViewBoundsCheck.Callback() {
          final RecyclerView.LayoutManager this$0;
          
          public View getChildAt(int param2Int) {
            return RecyclerView.LayoutManager.this.getChildAt(param2Int);
          }
          
          public int getChildEnd(View param2View) {
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)param2View.getLayoutParams();
            return RecyclerView.LayoutManager.this.getDecoratedRight(param2View) + layoutParams.rightMargin;
          }
          
          public int getChildStart(View param2View) {
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)param2View.getLayoutParams();
            return RecyclerView.LayoutManager.this.getDecoratedLeft(param2View) - layoutParams.leftMargin;
          }
          
          public int getParentEnd() {
            return RecyclerView.LayoutManager.this.getWidth() - RecyclerView.LayoutManager.this.getPaddingRight();
          }
          
          public int getParentStart() {
            return RecyclerView.LayoutManager.this.getPaddingLeft();
          }
        };
      this.mHorizontalBoundCheckCallback = callback1;
      ViewBoundsCheck.Callback callback2 = new ViewBoundsCheck.Callback() {
          final RecyclerView.LayoutManager this$0;
          
          public View getChildAt(int param2Int) {
            return RecyclerView.LayoutManager.this.getChildAt(param2Int);
          }
          
          public int getChildEnd(View param2View) {
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)param2View.getLayoutParams();
            return RecyclerView.LayoutManager.this.getDecoratedBottom(param2View) + layoutParams.bottomMargin;
          }
          
          public int getChildStart(View param2View) {
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)param2View.getLayoutParams();
            return RecyclerView.LayoutManager.this.getDecoratedTop(param2View) - layoutParams.topMargin;
          }
          
          public int getParentEnd() {
            return RecyclerView.LayoutManager.this.getHeight() - RecyclerView.LayoutManager.this.getPaddingBottom();
          }
          
          public int getParentStart() {
            return RecyclerView.LayoutManager.this.getPaddingTop();
          }
        };
      this.mVerticalBoundCheckCallback = callback2;
      this.mHorizontalBoundCheck = new ViewBoundsCheck(callback1);
      this.mVerticalBoundCheck = new ViewBoundsCheck(callback2);
      this.mRequestedSimpleAnimations = false;
      this.mIsAttachedToWindow = false;
      this.mAutoMeasure = false;
      this.mMeasurementCacheEnabled = true;
      this.mItemPrefetchEnabled = true;
    }
    
    private void addViewInt(View param1View, int param1Int, boolean param1Boolean) {
      RecyclerView.ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(param1View);
      if (param1Boolean || viewHolder.isRemoved()) {
        this.mRecyclerView.mViewInfoStore.addToDisappearedInLayout(viewHolder);
      } else {
        this.mRecyclerView.mViewInfoStore.removeFromDisappearedInLayout(viewHolder);
      } 
      RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)param1View.getLayoutParams();
      if (viewHolder.wasReturnedFromScrap() || viewHolder.isScrap()) {
        if (viewHolder.isScrap()) {
          viewHolder.unScrap();
        } else {
          viewHolder.clearReturnedFromScrapFlag();
        } 
        this.mChildHelper.attachViewToParent(param1View, param1Int, param1View.getLayoutParams(), false);
      } else if (param1View.getParent() == this.mRecyclerView) {
        int j = this.mChildHelper.indexOfChild(param1View);
        int i = param1Int;
        if (param1Int == -1)
          i = this.mChildHelper.getChildCount(); 
        if (j != -1) {
          if (j != i)
            this.mRecyclerView.mLayout.moveView(j, i); 
        } else {
          throw new IllegalStateException("Added View has RecyclerView as parent but view is not a real child. Unfiltered index:" + this.mRecyclerView.indexOfChild(param1View) + this.mRecyclerView.exceptionLabel());
        } 
      } else {
        this.mChildHelper.addView(param1View, param1Int, false);
        layoutParams.mInsetsDirty = true;
        RecyclerView.SmoothScroller smoothScroller = this.mSmoothScroller;
        if (smoothScroller != null && smoothScroller.isRunning())
          this.mSmoothScroller.onChildAttachedToWindow(param1View); 
      } 
      if (layoutParams.mPendingInvalidate) {
        viewHolder.itemView.invalidate();
        layoutParams.mPendingInvalidate = false;
      } 
    }
    
    public static int chooseSize(int param1Int1, int param1Int2, int param1Int3) {
      int i = View.MeasureSpec.getMode(param1Int1);
      param1Int1 = View.MeasureSpec.getSize(param1Int1);
      switch (i) {
        default:
          return Math.max(param1Int2, param1Int3);
        case 1073741824:
          return param1Int1;
        case -2147483648:
          break;
      } 
      return Math.min(param1Int1, Math.max(param1Int2, param1Int3));
    }
    
    private void detachViewInternal(int param1Int, View param1View) {
      this.mChildHelper.detachViewFromParent(param1Int);
    }
    
    public static int getChildMeasureSpec(int param1Int1, int param1Int2, int param1Int3, int param1Int4, boolean param1Boolean) {
      int i = Math.max(0, param1Int1 - param1Int3);
      param1Int3 = 0;
      boolean bool2 = false;
      boolean bool1 = false;
      param1Int1 = 0;
      if (param1Boolean) {
        if (param1Int4 >= 0) {
          param1Int3 = param1Int4;
          param1Int1 = 1073741824;
        } else if (param1Int4 == -1) {
          switch (param1Int2) {
            default:
              param1Int3 = bool2;
              return View.MeasureSpec.makeMeasureSpec(param1Int3, param1Int1);
            case 0:
              param1Int3 = 0;
              param1Int1 = 0;
              return View.MeasureSpec.makeMeasureSpec(param1Int3, param1Int1);
            case 1073741824:
            case -2147483648:
              break;
          } 
          param1Int3 = i;
          param1Int1 = param1Int2;
        } else {
          param1Int1 = bool1;
          if (param1Int4 == -2) {
            param1Int3 = 0;
            param1Int1 = 0;
          } 
        } 
      } else if (param1Int4 >= 0) {
        param1Int3 = param1Int4;
        param1Int1 = 1073741824;
      } else if (param1Int4 == -1) {
        param1Int3 = i;
        param1Int1 = param1Int2;
      } else {
        param1Int1 = bool1;
        if (param1Int4 == -2) {
          param1Int3 = i;
          if (param1Int2 == Integer.MIN_VALUE || param1Int2 == 1073741824) {
            param1Int1 = Integer.MIN_VALUE;
            return View.MeasureSpec.makeMeasureSpec(param1Int3, param1Int1);
          } 
          param1Int1 = 0;
        } 
      } 
      return View.MeasureSpec.makeMeasureSpec(param1Int3, param1Int1);
    }
    
    @Deprecated
    public static int getChildMeasureSpec(int param1Int1, int param1Int2, int param1Int3, boolean param1Boolean) {
      int i = Math.max(0, param1Int1 - param1Int2);
      param1Int1 = 0;
      param1Int2 = 0;
      if (param1Boolean) {
        if (param1Int3 >= 0) {
          param1Int1 = param1Int3;
          param1Int2 = 1073741824;
        } else {
          param1Int1 = 0;
          param1Int2 = 0;
        } 
      } else if (param1Int3 >= 0) {
        param1Int1 = param1Int3;
        param1Int2 = 1073741824;
      } else if (param1Int3 == -1) {
        param1Int1 = i;
        param1Int2 = 1073741824;
      } else if (param1Int3 == -2) {
        param1Int1 = i;
        param1Int2 = Integer.MIN_VALUE;
      } 
      return View.MeasureSpec.makeMeasureSpec(param1Int1, param1Int2);
    }
    
    private int[] getChildRectangleOnScreenScrollAmount(View param1View, Rect param1Rect) {
      int i2 = getPaddingLeft();
      int m = getPaddingTop();
      int i5 = getWidth() - getPaddingRight();
      int i1 = getHeight();
      int i6 = getPaddingBottom();
      int i4 = param1View.getLeft() + param1Rect.left - param1View.getScrollX();
      int n = param1View.getTop() + param1Rect.top - param1View.getScrollY();
      int i3 = param1Rect.width() + i4;
      int i7 = param1Rect.height();
      int k = Math.min(0, i4 - i2);
      int j = Math.min(0, n - m);
      int i = Math.max(0, i3 - i5);
      i1 = Math.max(0, i7 + n - i1 - i6);
      if (getLayoutDirection() == 1) {
        if (i == 0)
          i = Math.max(k, i3 - i5); 
      } else if (k != 0) {
        i = k;
      } else {
        i = Math.min(i4 - i2, i);
      } 
      if (j == 0)
        j = Math.min(n - m, i1); 
      return new int[] { i, j };
    }
    
    public static Properties getProperties(Context param1Context, AttributeSet param1AttributeSet, int param1Int1, int param1Int2) {
      Properties properties = new Properties();
      TypedArray typedArray = param1Context.obtainStyledAttributes(param1AttributeSet, R.styleable.RecyclerView, param1Int1, param1Int2);
      properties.orientation = typedArray.getInt(R.styleable.RecyclerView_android_orientation, 1);
      properties.spanCount = typedArray.getInt(R.styleable.RecyclerView_spanCount, 1);
      properties.reverseLayout = typedArray.getBoolean(R.styleable.RecyclerView_reverseLayout, false);
      properties.stackFromEnd = typedArray.getBoolean(R.styleable.RecyclerView_stackFromEnd, false);
      typedArray.recycle();
      return properties;
    }
    
    private boolean isFocusedChildVisibleAfterScrolling(RecyclerView param1RecyclerView, int param1Int1, int param1Int2) {
      View view = param1RecyclerView.getFocusedChild();
      if (view == null)
        return false; 
      int j = getPaddingLeft();
      int i1 = getPaddingTop();
      int k = getWidth();
      int m = getPaddingRight();
      int i = getHeight();
      int n = getPaddingBottom();
      Rect rect = this.mRecyclerView.mTempRect;
      getDecoratedBoundsWithMargins(view, rect);
      return !(rect.left - param1Int1 >= k - m || rect.right - param1Int1 <= j || rect.top - param1Int2 >= i - n || rect.bottom - param1Int2 <= i1);
    }
    
    private static boolean isMeasurementUpToDate(int param1Int1, int param1Int2, int param1Int3) {
      int i = View.MeasureSpec.getMode(param1Int2);
      param1Int2 = View.MeasureSpec.getSize(param1Int2);
      boolean bool2 = false;
      boolean bool1 = false;
      if (param1Int3 > 0 && param1Int1 != param1Int3)
        return false; 
      switch (i) {
        default:
          return false;
        case 1073741824:
          if (param1Int2 == param1Int1)
            bool1 = true; 
          return bool1;
        case 0:
          return true;
        case -2147483648:
          break;
      } 
      bool1 = bool2;
      if (param1Int2 >= param1Int1)
        bool1 = true; 
      return bool1;
    }
    
    private void scrapOrRecycleView(RecyclerView.Recycler param1Recycler, int param1Int, View param1View) {
      RecyclerView.ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(param1View);
      if (viewHolder.shouldIgnore())
        return; 
      if (viewHolder.isInvalid() && !viewHolder.isRemoved() && !this.mRecyclerView.mAdapter.hasStableIds()) {
        removeViewAt(param1Int);
        param1Recycler.recycleViewHolderInternal(viewHolder);
      } else {
        detachViewAt(param1Int);
        param1Recycler.scrapView(param1View);
        this.mRecyclerView.mViewInfoStore.onViewDetached(viewHolder);
      } 
    }
    
    public void addDisappearingView(View param1View) {
      addDisappearingView(param1View, -1);
    }
    
    public void addDisappearingView(View param1View, int param1Int) {
      addViewInt(param1View, param1Int, true);
    }
    
    public void addView(View param1View) {
      addView(param1View, -1);
    }
    
    public void addView(View param1View, int param1Int) {
      addViewInt(param1View, param1Int, false);
    }
    
    public void assertInLayoutOrScroll(String param1String) {
      RecyclerView recyclerView = this.mRecyclerView;
      if (recyclerView != null)
        recyclerView.assertInLayoutOrScroll(param1String); 
    }
    
    public void assertNotInLayoutOrScroll(String param1String) {
      RecyclerView recyclerView = this.mRecyclerView;
      if (recyclerView != null)
        recyclerView.assertNotInLayoutOrScroll(param1String); 
    }
    
    public void attachView(View param1View) {
      attachView(param1View, -1);
    }
    
    public void attachView(View param1View, int param1Int) {
      attachView(param1View, param1Int, (RecyclerView.LayoutParams)param1View.getLayoutParams());
    }
    
    public void attachView(View param1View, int param1Int, RecyclerView.LayoutParams param1LayoutParams) {
      RecyclerView.ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(param1View);
      if (viewHolder.isRemoved()) {
        this.mRecyclerView.mViewInfoStore.addToDisappearedInLayout(viewHolder);
      } else {
        this.mRecyclerView.mViewInfoStore.removeFromDisappearedInLayout(viewHolder);
      } 
      this.mChildHelper.attachViewToParent(param1View, param1Int, (ViewGroup.LayoutParams)param1LayoutParams, viewHolder.isRemoved());
    }
    
    public void calculateItemDecorationsForChild(View param1View, Rect param1Rect) {
      RecyclerView recyclerView = this.mRecyclerView;
      if (recyclerView == null) {
        param1Rect.set(0, 0, 0, 0);
        return;
      } 
      param1Rect.set(recyclerView.getItemDecorInsetsForChild(param1View));
    }
    
    public boolean canScrollHorizontally() {
      return false;
    }
    
    public boolean canScrollVertically() {
      return false;
    }
    
    public boolean checkLayoutParams(RecyclerView.LayoutParams param1LayoutParams) {
      boolean bool;
      if (param1LayoutParams != null) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public void collectAdjacentPrefetchPositions(int param1Int1, int param1Int2, RecyclerView.State param1State, LayoutPrefetchRegistry param1LayoutPrefetchRegistry) {}
    
    public void collectInitialPrefetchPositions(int param1Int, LayoutPrefetchRegistry param1LayoutPrefetchRegistry) {}
    
    public int computeHorizontalScrollExtent(RecyclerView.State param1State) {
      return 0;
    }
    
    public int computeHorizontalScrollOffset(RecyclerView.State param1State) {
      return 0;
    }
    
    public int computeHorizontalScrollRange(RecyclerView.State param1State) {
      return 0;
    }
    
    public int computeVerticalScrollExtent(RecyclerView.State param1State) {
      return 0;
    }
    
    public int computeVerticalScrollOffset(RecyclerView.State param1State) {
      return 0;
    }
    
    public int computeVerticalScrollRange(RecyclerView.State param1State) {
      return 0;
    }
    
    public void detachAndScrapAttachedViews(RecyclerView.Recycler param1Recycler) {
      for (int i = getChildCount() - 1; i >= 0; i--)
        scrapOrRecycleView(param1Recycler, i, getChildAt(i)); 
    }
    
    public void detachAndScrapView(View param1View, RecyclerView.Recycler param1Recycler) {
      scrapOrRecycleView(param1Recycler, this.mChildHelper.indexOfChild(param1View), param1View);
    }
    
    public void detachAndScrapViewAt(int param1Int, RecyclerView.Recycler param1Recycler) {
      scrapOrRecycleView(param1Recycler, param1Int, getChildAt(param1Int));
    }
    
    public void detachView(View param1View) {
      int i = this.mChildHelper.indexOfChild(param1View);
      if (i >= 0)
        detachViewInternal(i, param1View); 
    }
    
    public void detachViewAt(int param1Int) {
      detachViewInternal(param1Int, getChildAt(param1Int));
    }
    
    void dispatchAttachedToWindow(RecyclerView param1RecyclerView) {
      this.mIsAttachedToWindow = true;
      onAttachedToWindow(param1RecyclerView);
    }
    
    void dispatchDetachedFromWindow(RecyclerView param1RecyclerView, RecyclerView.Recycler param1Recycler) {
      this.mIsAttachedToWindow = false;
      onDetachedFromWindow(param1RecyclerView, param1Recycler);
    }
    
    public void endAnimation(View param1View) {
      if (this.mRecyclerView.mItemAnimator != null)
        this.mRecyclerView.mItemAnimator.endAnimation(RecyclerView.getChildViewHolderInt(param1View)); 
    }
    
    public View findContainingItemView(View param1View) {
      RecyclerView recyclerView = this.mRecyclerView;
      if (recyclerView == null)
        return null; 
      param1View = recyclerView.findContainingItemView(param1View);
      return (param1View == null) ? null : (this.mChildHelper.isHidden(param1View) ? null : param1View);
    }
    
    public View findViewByPosition(int param1Int) {
      int i = getChildCount();
      for (byte b = 0; b < i; b++) {
        View view = getChildAt(b);
        RecyclerView.ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(view);
        if (viewHolder != null && viewHolder.getLayoutPosition() == param1Int && !viewHolder.shouldIgnore() && (this.mRecyclerView.mState.isPreLayout() || !viewHolder.isRemoved()))
          return view; 
      } 
      return null;
    }
    
    public abstract RecyclerView.LayoutParams generateDefaultLayoutParams();
    
    public RecyclerView.LayoutParams generateLayoutParams(Context param1Context, AttributeSet param1AttributeSet) {
      return new RecyclerView.LayoutParams(param1Context, param1AttributeSet);
    }
    
    public RecyclerView.LayoutParams generateLayoutParams(ViewGroup.LayoutParams param1LayoutParams) {
      return (param1LayoutParams instanceof RecyclerView.LayoutParams) ? new RecyclerView.LayoutParams((RecyclerView.LayoutParams)param1LayoutParams) : ((param1LayoutParams instanceof ViewGroup.MarginLayoutParams) ? new RecyclerView.LayoutParams((ViewGroup.MarginLayoutParams)param1LayoutParams) : new RecyclerView.LayoutParams(param1LayoutParams));
    }
    
    public int getBaseline() {
      return -1;
    }
    
    public int getBottomDecorationHeight(View param1View) {
      return ((RecyclerView.LayoutParams)param1View.getLayoutParams()).mDecorInsets.bottom;
    }
    
    public View getChildAt(int param1Int) {
      ChildHelper childHelper = this.mChildHelper;
      if (childHelper != null) {
        View view = childHelper.getChildAt(param1Int);
      } else {
        childHelper = null;
      } 
      return (View)childHelper;
    }
    
    public int getChildCount() {
      boolean bool;
      ChildHelper childHelper = this.mChildHelper;
      if (childHelper != null) {
        bool = childHelper.getChildCount();
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public boolean getClipToPadding() {
      boolean bool;
      RecyclerView recyclerView = this.mRecyclerView;
      if (recyclerView != null && recyclerView.mClipToPadding) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public int getColumnCountForAccessibility(RecyclerView.Recycler param1Recycler, RecyclerView.State param1State) {
      RecyclerView recyclerView = this.mRecyclerView;
      int i = 1;
      if (recyclerView == null || recyclerView.mAdapter == null)
        return 1; 
      if (canScrollHorizontally())
        i = this.mRecyclerView.mAdapter.getItemCount(); 
      return i;
    }
    
    public int getDecoratedBottom(View param1View) {
      return param1View.getBottom() + getBottomDecorationHeight(param1View);
    }
    
    public void getDecoratedBoundsWithMargins(View param1View, Rect param1Rect) {
      RecyclerView.getDecoratedBoundsWithMarginsInt(param1View, param1Rect);
    }
    
    public int getDecoratedLeft(View param1View) {
      return param1View.getLeft() - getLeftDecorationWidth(param1View);
    }
    
    public int getDecoratedMeasuredHeight(View param1View) {
      Rect rect = ((RecyclerView.LayoutParams)param1View.getLayoutParams()).mDecorInsets;
      return param1View.getMeasuredHeight() + rect.top + rect.bottom;
    }
    
    public int getDecoratedMeasuredWidth(View param1View) {
      Rect rect = ((RecyclerView.LayoutParams)param1View.getLayoutParams()).mDecorInsets;
      return param1View.getMeasuredWidth() + rect.left + rect.right;
    }
    
    public int getDecoratedRight(View param1View) {
      return param1View.getRight() + getRightDecorationWidth(param1View);
    }
    
    public int getDecoratedTop(View param1View) {
      return param1View.getTop() - getTopDecorationHeight(param1View);
    }
    
    public View getFocusedChild() {
      RecyclerView recyclerView = this.mRecyclerView;
      if (recyclerView == null)
        return null; 
      View view = recyclerView.getFocusedChild();
      return (view == null || this.mChildHelper.isHidden(view)) ? null : view;
    }
    
    public int getHeight() {
      return this.mHeight;
    }
    
    public int getHeightMode() {
      return this.mHeightMode;
    }
    
    public int getItemCount() {
      boolean bool;
      RecyclerView recyclerView = this.mRecyclerView;
      if (recyclerView != null) {
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
      } else {
        recyclerView = null;
      } 
      if (recyclerView != null) {
        bool = recyclerView.getItemCount();
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public int getItemViewType(View param1View) {
      return RecyclerView.getChildViewHolderInt(param1View).getItemViewType();
    }
    
    public int getLayoutDirection() {
      return ViewCompat.getLayoutDirection((View)this.mRecyclerView);
    }
    
    public int getLeftDecorationWidth(View param1View) {
      return ((RecyclerView.LayoutParams)param1View.getLayoutParams()).mDecorInsets.left;
    }
    
    public int getMinimumHeight() {
      return ViewCompat.getMinimumHeight((View)this.mRecyclerView);
    }
    
    public int getMinimumWidth() {
      return ViewCompat.getMinimumWidth((View)this.mRecyclerView);
    }
    
    public int getPaddingBottom() {
      boolean bool;
      RecyclerView recyclerView = this.mRecyclerView;
      if (recyclerView != null) {
        bool = recyclerView.getPaddingBottom();
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public int getPaddingEnd() {
      boolean bool;
      RecyclerView recyclerView = this.mRecyclerView;
      if (recyclerView != null) {
        bool = ViewCompat.getPaddingEnd((View)recyclerView);
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public int getPaddingLeft() {
      boolean bool;
      RecyclerView recyclerView = this.mRecyclerView;
      if (recyclerView != null) {
        bool = recyclerView.getPaddingLeft();
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public int getPaddingRight() {
      boolean bool;
      RecyclerView recyclerView = this.mRecyclerView;
      if (recyclerView != null) {
        bool = recyclerView.getPaddingRight();
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public int getPaddingStart() {
      boolean bool;
      RecyclerView recyclerView = this.mRecyclerView;
      if (recyclerView != null) {
        bool = ViewCompat.getPaddingStart((View)recyclerView);
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public int getPaddingTop() {
      boolean bool;
      RecyclerView recyclerView = this.mRecyclerView;
      if (recyclerView != null) {
        bool = recyclerView.getPaddingTop();
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public int getPosition(View param1View) {
      return ((RecyclerView.LayoutParams)param1View.getLayoutParams()).getViewLayoutPosition();
    }
    
    public int getRightDecorationWidth(View param1View) {
      return ((RecyclerView.LayoutParams)param1View.getLayoutParams()).mDecorInsets.right;
    }
    
    public int getRowCountForAccessibility(RecyclerView.Recycler param1Recycler, RecyclerView.State param1State) {
      RecyclerView recyclerView = this.mRecyclerView;
      int i = 1;
      if (recyclerView == null || recyclerView.mAdapter == null)
        return 1; 
      if (canScrollVertically())
        i = this.mRecyclerView.mAdapter.getItemCount(); 
      return i;
    }
    
    public int getSelectionModeForAccessibility(RecyclerView.Recycler param1Recycler, RecyclerView.State param1State) {
      return 0;
    }
    
    public int getTopDecorationHeight(View param1View) {
      return ((RecyclerView.LayoutParams)param1View.getLayoutParams()).mDecorInsets.top;
    }
    
    public void getTransformedBoundingBox(View param1View, boolean param1Boolean, Rect param1Rect) {
      if (param1Boolean) {
        Rect rect = ((RecyclerView.LayoutParams)param1View.getLayoutParams()).mDecorInsets;
        param1Rect.set(-rect.left, -rect.top, param1View.getWidth() + rect.right, param1View.getHeight() + rect.bottom);
      } else {
        param1Rect.set(0, 0, param1View.getWidth(), param1View.getHeight());
      } 
      if (this.mRecyclerView != null) {
        Matrix matrix = param1View.getMatrix();
        if (matrix != null && !matrix.isIdentity()) {
          RectF rectF = this.mRecyclerView.mTempRectF;
          rectF.set(param1Rect);
          matrix.mapRect(rectF);
          param1Rect.set((int)Math.floor(rectF.left), (int)Math.floor(rectF.top), (int)Math.ceil(rectF.right), (int)Math.ceil(rectF.bottom));
        } 
      } 
      param1Rect.offset(param1View.getLeft(), param1View.getTop());
    }
    
    public int getWidth() {
      return this.mWidth;
    }
    
    public int getWidthMode() {
      return this.mWidthMode;
    }
    
    boolean hasFlexibleChildInBothOrientations() {
      int i = getChildCount();
      for (byte b = 0; b < i; b++) {
        ViewGroup.LayoutParams layoutParams = getChildAt(b).getLayoutParams();
        if (layoutParams.width < 0 && layoutParams.height < 0)
          return true; 
      } 
      return false;
    }
    
    public boolean hasFocus() {
      boolean bool;
      RecyclerView recyclerView = this.mRecyclerView;
      if (recyclerView != null && recyclerView.hasFocus()) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public void ignoreView(View param1View) {
      ViewParent viewParent = param1View.getParent();
      RecyclerView recyclerView = this.mRecyclerView;
      if (viewParent == recyclerView && recyclerView.indexOfChild(param1View) != -1) {
        RecyclerView.ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(param1View);
        viewHolder.addFlags(128);
        this.mRecyclerView.mViewInfoStore.removeViewHolder(viewHolder);
        return;
      } 
      throw new IllegalArgumentException("View should be fully attached to be ignored" + this.mRecyclerView.exceptionLabel());
    }
    
    public boolean isAttachedToWindow() {
      return this.mIsAttachedToWindow;
    }
    
    public boolean isAutoMeasureEnabled() {
      return this.mAutoMeasure;
    }
    
    public boolean isFocused() {
      boolean bool;
      RecyclerView recyclerView = this.mRecyclerView;
      if (recyclerView != null && recyclerView.isFocused()) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public final boolean isItemPrefetchEnabled() {
      return this.mItemPrefetchEnabled;
    }
    
    public boolean isLayoutHierarchical(RecyclerView.Recycler param1Recycler, RecyclerView.State param1State) {
      return false;
    }
    
    public boolean isMeasurementCacheEnabled() {
      return this.mMeasurementCacheEnabled;
    }
    
    public boolean isSmoothScrolling() {
      boolean bool;
      RecyclerView.SmoothScroller smoothScroller = this.mSmoothScroller;
      if (smoothScroller != null && smoothScroller.isRunning()) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public boolean isViewPartiallyVisible(View param1View, boolean param1Boolean1, boolean param1Boolean2) {
      param1Boolean2 = this.mHorizontalBoundCheck.isViewWithinBoundFlags(param1View, 24579);
      boolean bool = true;
      if (param1Boolean2 && this.mVerticalBoundCheck.isViewWithinBoundFlags(param1View, 24579)) {
        param1Boolean2 = true;
      } else {
        param1Boolean2 = false;
      } 
      if (param1Boolean1)
        return param1Boolean2; 
      if (!param1Boolean2) {
        param1Boolean1 = bool;
      } else {
        param1Boolean1 = false;
      } 
      return param1Boolean1;
    }
    
    public void layoutDecorated(View param1View, int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
      Rect rect = ((RecyclerView.LayoutParams)param1View.getLayoutParams()).mDecorInsets;
      param1View.layout(rect.left + param1Int1, rect.top + param1Int2, param1Int3 - rect.right, param1Int4 - rect.bottom);
    }
    
    public void layoutDecoratedWithMargins(View param1View, int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
      RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)param1View.getLayoutParams();
      Rect rect = layoutParams.mDecorInsets;
      param1View.layout(rect.left + param1Int1 + layoutParams.leftMargin, rect.top + param1Int2 + layoutParams.topMargin, param1Int3 - rect.right - layoutParams.rightMargin, param1Int4 - rect.bottom - layoutParams.bottomMargin);
    }
    
    public void measureChild(View param1View, int param1Int1, int param1Int2) {
      RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)param1View.getLayoutParams();
      Rect rect = this.mRecyclerView.getItemDecorInsetsForChild(param1View);
      int k = rect.left;
      int m = rect.right;
      int j = rect.top;
      int i = rect.bottom;
      param1Int1 = getChildMeasureSpec(getWidth(), getWidthMode(), getPaddingLeft() + getPaddingRight() + param1Int1 + k + m, layoutParams.width, canScrollHorizontally());
      param1Int2 = getChildMeasureSpec(getHeight(), getHeightMode(), getPaddingTop() + getPaddingBottom() + param1Int2 + j + i, layoutParams.height, canScrollVertically());
      if (shouldMeasureChild(param1View, param1Int1, param1Int2, layoutParams))
        param1View.measure(param1Int1, param1Int2); 
    }
    
    public void measureChildWithMargins(View param1View, int param1Int1, int param1Int2) {
      RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)param1View.getLayoutParams();
      Rect rect = this.mRecyclerView.getItemDecorInsetsForChild(param1View);
      int m = rect.left;
      int k = rect.right;
      int j = rect.top;
      int i = rect.bottom;
      param1Int1 = getChildMeasureSpec(getWidth(), getWidthMode(), getPaddingLeft() + getPaddingRight() + layoutParams.leftMargin + layoutParams.rightMargin + param1Int1 + m + k, layoutParams.width, canScrollHorizontally());
      param1Int2 = getChildMeasureSpec(getHeight(), getHeightMode(), getPaddingTop() + getPaddingBottom() + layoutParams.topMargin + layoutParams.bottomMargin + param1Int2 + j + i, layoutParams.height, canScrollVertically());
      if (shouldMeasureChild(param1View, param1Int1, param1Int2, layoutParams))
        param1View.measure(param1Int1, param1Int2); 
    }
    
    public void moveView(int param1Int1, int param1Int2) {
      View view = getChildAt(param1Int1);
      if (view != null) {
        detachViewAt(param1Int1);
        attachView(view, param1Int2);
        return;
      } 
      throw new IllegalArgumentException("Cannot move a child from non-existing index:" + param1Int1 + this.mRecyclerView.toString());
    }
    
    public void offsetChildrenHorizontal(int param1Int) {
      RecyclerView recyclerView = this.mRecyclerView;
      if (recyclerView != null)
        recyclerView.offsetChildrenHorizontal(param1Int); 
    }
    
    public void offsetChildrenVertical(int param1Int) {
      RecyclerView recyclerView = this.mRecyclerView;
      if (recyclerView != null)
        recyclerView.offsetChildrenVertical(param1Int); 
    }
    
    public void onAdapterChanged(RecyclerView.Adapter param1Adapter1, RecyclerView.Adapter param1Adapter2) {}
    
    public boolean onAddFocusables(RecyclerView param1RecyclerView, ArrayList<View> param1ArrayList, int param1Int1, int param1Int2) {
      return false;
    }
    
    public void onAttachedToWindow(RecyclerView param1RecyclerView) {}
    
    @Deprecated
    public void onDetachedFromWindow(RecyclerView param1RecyclerView) {}
    
    public void onDetachedFromWindow(RecyclerView param1RecyclerView, RecyclerView.Recycler param1Recycler) {
      onDetachedFromWindow(param1RecyclerView);
    }
    
    public View onFocusSearchFailed(View param1View, int param1Int, RecyclerView.Recycler param1Recycler, RecyclerView.State param1State) {
      return null;
    }
    
    public void onInitializeAccessibilityEvent(AccessibilityEvent param1AccessibilityEvent) {
      onInitializeAccessibilityEvent(this.mRecyclerView.mRecycler, this.mRecyclerView.mState, param1AccessibilityEvent);
    }
    
    public void onInitializeAccessibilityEvent(RecyclerView.Recycler param1Recycler, RecyclerView.State param1State, AccessibilityEvent param1AccessibilityEvent) {
      RecyclerView recyclerView = this.mRecyclerView;
      if (recyclerView == null || param1AccessibilityEvent == null)
        return; 
      boolean bool = true;
      if (!recyclerView.canScrollVertically(1) && !this.mRecyclerView.canScrollVertically(-1) && !this.mRecyclerView.canScrollHorizontally(-1) && !this.mRecyclerView.canScrollHorizontally(1))
        bool = false; 
      param1AccessibilityEvent.setScrollable(bool);
      if (this.mRecyclerView.mAdapter != null)
        param1AccessibilityEvent.setItemCount(this.mRecyclerView.mAdapter.getItemCount()); 
    }
    
    void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfoCompat param1AccessibilityNodeInfoCompat) {
      onInitializeAccessibilityNodeInfo(this.mRecyclerView.mRecycler, this.mRecyclerView.mState, param1AccessibilityNodeInfoCompat);
    }
    
    public void onInitializeAccessibilityNodeInfo(RecyclerView.Recycler param1Recycler, RecyclerView.State param1State, AccessibilityNodeInfoCompat param1AccessibilityNodeInfoCompat) {
      if (this.mRecyclerView.canScrollVertically(-1) || this.mRecyclerView.canScrollHorizontally(-1)) {
        param1AccessibilityNodeInfoCompat.addAction(8192);
        param1AccessibilityNodeInfoCompat.setScrollable(true);
      } 
      if (this.mRecyclerView.canScrollVertically(1) || this.mRecyclerView.canScrollHorizontally(1)) {
        param1AccessibilityNodeInfoCompat.addAction(4096);
        param1AccessibilityNodeInfoCompat.setScrollable(true);
      } 
      param1AccessibilityNodeInfoCompat.setCollectionInfo(AccessibilityNodeInfoCompat.CollectionInfoCompat.obtain(getRowCountForAccessibility(param1Recycler, param1State), getColumnCountForAccessibility(param1Recycler, param1State), isLayoutHierarchical(param1Recycler, param1State), getSelectionModeForAccessibility(param1Recycler, param1State)));
    }
    
    void onInitializeAccessibilityNodeInfoForItem(View param1View, AccessibilityNodeInfoCompat param1AccessibilityNodeInfoCompat) {
      RecyclerView.ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(param1View);
      if (viewHolder != null && !viewHolder.isRemoved() && !this.mChildHelper.isHidden(viewHolder.itemView))
        onInitializeAccessibilityNodeInfoForItem(this.mRecyclerView.mRecycler, this.mRecyclerView.mState, param1View, param1AccessibilityNodeInfoCompat); 
    }
    
    public void onInitializeAccessibilityNodeInfoForItem(RecyclerView.Recycler param1Recycler, RecyclerView.State param1State, View param1View, AccessibilityNodeInfoCompat param1AccessibilityNodeInfoCompat) {
      boolean bool;
      boolean bool1 = canScrollVertically();
      int i = 0;
      if (bool1) {
        bool = getPosition(param1View);
      } else {
        bool = false;
      } 
      if (canScrollHorizontally())
        i = getPosition(param1View); 
      param1AccessibilityNodeInfoCompat.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(bool, 1, i, 1, false, false));
    }
    
    public View onInterceptFocusSearch(View param1View, int param1Int) {
      return null;
    }
    
    public void onItemsAdded(RecyclerView param1RecyclerView, int param1Int1, int param1Int2) {}
    
    public void onItemsChanged(RecyclerView param1RecyclerView) {}
    
    public void onItemsMoved(RecyclerView param1RecyclerView, int param1Int1, int param1Int2, int param1Int3) {}
    
    public void onItemsRemoved(RecyclerView param1RecyclerView, int param1Int1, int param1Int2) {}
    
    public void onItemsUpdated(RecyclerView param1RecyclerView, int param1Int1, int param1Int2) {}
    
    public void onItemsUpdated(RecyclerView param1RecyclerView, int param1Int1, int param1Int2, Object param1Object) {
      onItemsUpdated(param1RecyclerView, param1Int1, param1Int2);
    }
    
    public void onLayoutChildren(RecyclerView.Recycler param1Recycler, RecyclerView.State param1State) {
      Log.e("RecyclerView", "You must override onLayoutChildren(Recycler recycler, State state) ");
    }
    
    public void onLayoutCompleted(RecyclerView.State param1State) {}
    
    public void onMeasure(RecyclerView.Recycler param1Recycler, RecyclerView.State param1State, int param1Int1, int param1Int2) {
      this.mRecyclerView.defaultOnMeasure(param1Int1, param1Int2);
    }
    
    @Deprecated
    public boolean onRequestChildFocus(RecyclerView param1RecyclerView, View param1View1, View param1View2) {
      return (isSmoothScrolling() || param1RecyclerView.isComputingLayout());
    }
    
    public boolean onRequestChildFocus(RecyclerView param1RecyclerView, RecyclerView.State param1State, View param1View1, View param1View2) {
      return onRequestChildFocus(param1RecyclerView, param1View1, param1View2);
    }
    
    public void onRestoreInstanceState(Parcelable param1Parcelable) {}
    
    public Parcelable onSaveInstanceState() {
      return null;
    }
    
    public void onScrollStateChanged(int param1Int) {}
    
    void onSmoothScrollerStopped(RecyclerView.SmoothScroller param1SmoothScroller) {
      if (this.mSmoothScroller == param1SmoothScroller)
        this.mSmoothScroller = null; 
    }
    
    boolean performAccessibilityAction(int param1Int, Bundle param1Bundle) {
      return performAccessibilityAction(this.mRecyclerView.mRecycler, this.mRecyclerView.mState, param1Int, param1Bundle);
    }
    
    public boolean performAccessibilityAction(RecyclerView.Recycler param1Recycler, RecyclerView.State param1State, int param1Int, Bundle param1Bundle) {
      RecyclerView recyclerView = this.mRecyclerView;
      if (recyclerView == null)
        return false; 
      boolean bool1 = false;
      boolean bool2 = false;
      int i = 0;
      int j = 0;
      switch (param1Int) {
        default:
          param1Int = bool2;
          break;
        case 8192:
          if (recyclerView.canScrollVertically(-1))
            i = -(getHeight() - getPaddingTop() - getPaddingBottom()); 
          param1Int = i;
          if (this.mRecyclerView.canScrollHorizontally(-1)) {
            j = -(getWidth() - getPaddingLeft() - getPaddingRight());
            param1Int = i;
          } 
          break;
        case 4096:
          i = bool1;
          if (recyclerView.canScrollVertically(1))
            i = getHeight() - getPaddingTop() - getPaddingBottom(); 
          param1Int = i;
          if (this.mRecyclerView.canScrollHorizontally(1)) {
            j = getWidth() - getPaddingLeft() - getPaddingRight();
            param1Int = i;
          } 
          break;
      } 
      if (param1Int == 0 && j == 0)
        return false; 
      this.mRecyclerView.smoothScrollBy(j, param1Int, (Interpolator)null, -2147483648, true);
      return true;
    }
    
    boolean performAccessibilityActionForItem(View param1View, int param1Int, Bundle param1Bundle) {
      return performAccessibilityActionForItem(this.mRecyclerView.mRecycler, this.mRecyclerView.mState, param1View, param1Int, param1Bundle);
    }
    
    public boolean performAccessibilityActionForItem(RecyclerView.Recycler param1Recycler, RecyclerView.State param1State, View param1View, int param1Int, Bundle param1Bundle) {
      return false;
    }
    
    public void postOnAnimation(Runnable param1Runnable) {
      RecyclerView recyclerView = this.mRecyclerView;
      if (recyclerView != null)
        ViewCompat.postOnAnimation((View)recyclerView, param1Runnable); 
    }
    
    public void removeAllViews() {
      for (int i = getChildCount() - 1; i >= 0; i--)
        this.mChildHelper.removeViewAt(i); 
    }
    
    public void removeAndRecycleAllViews(RecyclerView.Recycler param1Recycler) {
      for (int i = getChildCount() - 1; i >= 0; i--) {
        if (!RecyclerView.getChildViewHolderInt(getChildAt(i)).shouldIgnore())
          removeAndRecycleViewAt(i, param1Recycler); 
      } 
    }
    
    void removeAndRecycleScrapInt(RecyclerView.Recycler param1Recycler) {
      int j = param1Recycler.getScrapCount();
      for (int i = j - 1; i >= 0; i--) {
        View view = param1Recycler.getScrapViewAt(i);
        RecyclerView.ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(view);
        if (!viewHolder.shouldIgnore()) {
          viewHolder.setIsRecyclable(false);
          if (viewHolder.isTmpDetached())
            this.mRecyclerView.removeDetachedView(view, false); 
          if (this.mRecyclerView.mItemAnimator != null)
            this.mRecyclerView.mItemAnimator.endAnimation(viewHolder); 
          viewHolder.setIsRecyclable(true);
          param1Recycler.quickRecycleScrapView(view);
        } 
      } 
      param1Recycler.clearScrap();
      if (j > 0)
        this.mRecyclerView.invalidate(); 
    }
    
    public void removeAndRecycleView(View param1View, RecyclerView.Recycler param1Recycler) {
      removeView(param1View);
      param1Recycler.recycleView(param1View);
    }
    
    public void removeAndRecycleViewAt(int param1Int, RecyclerView.Recycler param1Recycler) {
      View view = getChildAt(param1Int);
      removeViewAt(param1Int);
      param1Recycler.recycleView(view);
    }
    
    public boolean removeCallbacks(Runnable param1Runnable) {
      RecyclerView recyclerView = this.mRecyclerView;
      return (recyclerView != null) ? recyclerView.removeCallbacks(param1Runnable) : false;
    }
    
    public void removeDetachedView(View param1View) {
      this.mRecyclerView.removeDetachedView(param1View, false);
    }
    
    public void removeView(View param1View) {
      this.mChildHelper.removeView(param1View);
    }
    
    public void removeViewAt(int param1Int) {
      if (getChildAt(param1Int) != null)
        this.mChildHelper.removeViewAt(param1Int); 
    }
    
    public boolean requestChildRectangleOnScreen(RecyclerView param1RecyclerView, View param1View, Rect param1Rect, boolean param1Boolean) {
      return requestChildRectangleOnScreen(param1RecyclerView, param1View, param1Rect, param1Boolean, false);
    }
    
    public boolean requestChildRectangleOnScreen(RecyclerView param1RecyclerView, View param1View, Rect param1Rect, boolean param1Boolean1, boolean param1Boolean2) {
      int[] arrayOfInt = getChildRectangleOnScreenScrollAmount(param1View, param1Rect);
      int j = arrayOfInt[0];
      int i = arrayOfInt[1];
      if ((!param1Boolean2 || isFocusedChildVisibleAfterScrolling(param1RecyclerView, j, i)) && (j != 0 || i != 0)) {
        if (param1Boolean1) {
          param1RecyclerView.scrollBy(j, i);
        } else {
          param1RecyclerView.smoothScrollBy(j, i);
        } 
        return true;
      } 
      return false;
    }
    
    public void requestLayout() {
      RecyclerView recyclerView = this.mRecyclerView;
      if (recyclerView != null)
        recyclerView.requestLayout(); 
    }
    
    public void requestSimpleAnimationsInNextLayout() {
      this.mRequestedSimpleAnimations = true;
    }
    
    public int scrollHorizontallyBy(int param1Int, RecyclerView.Recycler param1Recycler, RecyclerView.State param1State) {
      return 0;
    }
    
    public void scrollToPosition(int param1Int) {}
    
    public int scrollVerticallyBy(int param1Int, RecyclerView.Recycler param1Recycler, RecyclerView.State param1State) {
      return 0;
    }
    
    @Deprecated
    public void setAutoMeasureEnabled(boolean param1Boolean) {
      this.mAutoMeasure = param1Boolean;
    }
    
    void setExactMeasureSpecsFrom(RecyclerView param1RecyclerView) {
      setMeasureSpecs(View.MeasureSpec.makeMeasureSpec(param1RecyclerView.getWidth(), 1073741824), View.MeasureSpec.makeMeasureSpec(param1RecyclerView.getHeight(), 1073741824));
    }
    
    public final void setItemPrefetchEnabled(boolean param1Boolean) {
      if (param1Boolean != this.mItemPrefetchEnabled) {
        this.mItemPrefetchEnabled = param1Boolean;
        this.mPrefetchMaxCountObserved = 0;
        RecyclerView recyclerView = this.mRecyclerView;
        if (recyclerView != null)
          recyclerView.mRecycler.updateViewCacheSize(); 
      } 
    }
    
    void setMeasureSpecs(int param1Int1, int param1Int2) {
      this.mWidth = View.MeasureSpec.getSize(param1Int1);
      param1Int1 = View.MeasureSpec.getMode(param1Int1);
      this.mWidthMode = param1Int1;
      if (param1Int1 == 0 && !RecyclerView.ALLOW_SIZE_IN_UNSPECIFIED_SPEC)
        this.mWidth = 0; 
      this.mHeight = View.MeasureSpec.getSize(param1Int2);
      param1Int1 = View.MeasureSpec.getMode(param1Int2);
      this.mHeightMode = param1Int1;
      if (param1Int1 == 0 && !RecyclerView.ALLOW_SIZE_IN_UNSPECIFIED_SPEC)
        this.mHeight = 0; 
    }
    
    public void setMeasuredDimension(int param1Int1, int param1Int2) {
      this.mRecyclerView.setMeasuredDimension(param1Int1, param1Int2);
    }
    
    public void setMeasuredDimension(Rect param1Rect, int param1Int1, int param1Int2) {
      int j = param1Rect.width();
      int i = getPaddingLeft();
      int m = getPaddingRight();
      int k = param1Rect.height();
      int i1 = getPaddingTop();
      int n = getPaddingBottom();
      setMeasuredDimension(chooseSize(param1Int1, j + i + m, getMinimumWidth()), chooseSize(param1Int2, k + i1 + n, getMinimumHeight()));
    }
    
    void setMeasuredDimensionFromChildren(int param1Int1, int param1Int2) {
      int n = getChildCount();
      if (n == 0) {
        this.mRecyclerView.defaultOnMeasure(param1Int1, param1Int2);
        return;
      } 
      int m = Integer.MAX_VALUE;
      int j = Integer.MAX_VALUE;
      int k = Integer.MIN_VALUE;
      int i = Integer.MIN_VALUE;
      byte b = 0;
      while (b < n) {
        View view = getChildAt(b);
        Rect rect = this.mRecyclerView.mTempRect;
        getDecoratedBoundsWithMargins(view, rect);
        int i1 = m;
        if (rect.left < m)
          i1 = rect.left; 
        int i2 = k;
        if (rect.right > k)
          i2 = rect.right; 
        k = j;
        if (rect.top < j)
          k = rect.top; 
        int i3 = i;
        if (rect.bottom > i)
          i3 = rect.bottom; 
        b++;
        m = i1;
        j = k;
        k = i2;
        i = i3;
      } 
      this.mRecyclerView.mTempRect.set(m, j, k, i);
      setMeasuredDimension(this.mRecyclerView.mTempRect, param1Int1, param1Int2);
    }
    
    public void setMeasurementCacheEnabled(boolean param1Boolean) {
      this.mMeasurementCacheEnabled = param1Boolean;
    }
    
    void setRecyclerView(RecyclerView param1RecyclerView) {
      if (param1RecyclerView == null) {
        this.mRecyclerView = null;
        this.mChildHelper = null;
        this.mWidth = 0;
        this.mHeight = 0;
      } else {
        this.mRecyclerView = param1RecyclerView;
        this.mChildHelper = param1RecyclerView.mChildHelper;
        this.mWidth = param1RecyclerView.getWidth();
        this.mHeight = param1RecyclerView.getHeight();
      } 
      this.mWidthMode = 1073741824;
      this.mHeightMode = 1073741824;
    }
    
    boolean shouldMeasureChild(View param1View, int param1Int1, int param1Int2, RecyclerView.LayoutParams param1LayoutParams) {
      return (param1View.isLayoutRequested() || !this.mMeasurementCacheEnabled || !isMeasurementUpToDate(param1View.getWidth(), param1Int1, param1LayoutParams.width) || !isMeasurementUpToDate(param1View.getHeight(), param1Int2, param1LayoutParams.height));
    }
    
    boolean shouldMeasureTwice() {
      return false;
    }
    
    boolean shouldReMeasureChild(View param1View, int param1Int1, int param1Int2, RecyclerView.LayoutParams param1LayoutParams) {
      return (!this.mMeasurementCacheEnabled || !isMeasurementUpToDate(param1View.getMeasuredWidth(), param1Int1, param1LayoutParams.width) || !isMeasurementUpToDate(param1View.getMeasuredHeight(), param1Int2, param1LayoutParams.height));
    }
    
    public void smoothScrollToPosition(RecyclerView param1RecyclerView, RecyclerView.State param1State, int param1Int) {
      Log.e("RecyclerView", "You must override smoothScrollToPosition to support smooth scrolling");
    }
    
    public void startSmoothScroll(RecyclerView.SmoothScroller param1SmoothScroller) {
      RecyclerView.SmoothScroller smoothScroller = this.mSmoothScroller;
      if (smoothScroller != null && param1SmoothScroller != smoothScroller && smoothScroller.isRunning())
        this.mSmoothScroller.stop(); 
      this.mSmoothScroller = param1SmoothScroller;
      param1SmoothScroller.start(this.mRecyclerView, this);
    }
    
    public void stopIgnoringView(View param1View) {
      RecyclerView.ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(param1View);
      viewHolder.stopIgnoring();
      viewHolder.resetInternal();
      viewHolder.addFlags(4);
    }
    
    void stopSmoothScroller() {
      RecyclerView.SmoothScroller smoothScroller = this.mSmoothScroller;
      if (smoothScroller != null)
        smoothScroller.stop(); 
    }
    
    public boolean supportsPredictiveItemAnimations() {
      return false;
    }
    
    public static interface LayoutPrefetchRegistry {
      void addPosition(int param2Int1, int param2Int2);
    }
    
    public static class Properties {
      public int orientation;
      
      public boolean reverseLayout;
      
      public int spanCount;
      
      public boolean stackFromEnd;
    }
  }
  
  class null implements ViewBoundsCheck.Callback {
    final RecyclerView.LayoutManager this$0;
    
    public View getChildAt(int param1Int) {
      return this.this$0.getChildAt(param1Int);
    }
    
    public int getChildEnd(View param1View) {
      RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)param1View.getLayoutParams();
      return this.this$0.getDecoratedRight(param1View) + layoutParams.rightMargin;
    }
    
    public int getChildStart(View param1View) {
      RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)param1View.getLayoutParams();
      return this.this$0.getDecoratedLeft(param1View) - layoutParams.leftMargin;
    }
    
    public int getParentEnd() {
      return this.this$0.getWidth() - this.this$0.getPaddingRight();
    }
    
    public int getParentStart() {
      return this.this$0.getPaddingLeft();
    }
  }
  
  class null implements ViewBoundsCheck.Callback {
    final RecyclerView.LayoutManager this$0;
    
    public View getChildAt(int param1Int) {
      return this.this$0.getChildAt(param1Int);
    }
    
    public int getChildEnd(View param1View) {
      RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)param1View.getLayoutParams();
      return this.this$0.getDecoratedBottom(param1View) + layoutParams.bottomMargin;
    }
    
    public int getChildStart(View param1View) {
      RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)param1View.getLayoutParams();
      return this.this$0.getDecoratedTop(param1View) - layoutParams.topMargin;
    }
    
    public int getParentEnd() {
      return this.this$0.getHeight() - this.this$0.getPaddingBottom();
    }
    
    public int getParentStart() {
      return this.this$0.getPaddingTop();
    }
  }
  
  public static interface LayoutPrefetchRegistry {
    void addPosition(int param1Int1, int param1Int2);
  }
  
  public static class Properties {
    public int orientation;
    
    public boolean reverseLayout;
    
    public int spanCount;
    
    public boolean stackFromEnd;
  }
  
  public static class LayoutParams extends ViewGroup.MarginLayoutParams {
    final Rect mDecorInsets = new Rect();
    
    boolean mInsetsDirty = true;
    
    boolean mPendingInvalidate = false;
    
    RecyclerView.ViewHolder mViewHolder;
    
    public LayoutParams(int param1Int1, int param1Int2) {
      super(param1Int1, param1Int2);
    }
    
    public LayoutParams(Context param1Context, AttributeSet param1AttributeSet) {
      super(param1Context, param1AttributeSet);
    }
    
    public LayoutParams(ViewGroup.LayoutParams param1LayoutParams) {
      super(param1LayoutParams);
    }
    
    public LayoutParams(ViewGroup.MarginLayoutParams param1MarginLayoutParams) {
      super(param1MarginLayoutParams);
    }
    
    public LayoutParams(LayoutParams param1LayoutParams) {
      super((ViewGroup.LayoutParams)param1LayoutParams);
    }
    
    public int getViewAdapterPosition() {
      return this.mViewHolder.getAdapterPosition();
    }
    
    public int getViewLayoutPosition() {
      return this.mViewHolder.getLayoutPosition();
    }
    
    @Deprecated
    public int getViewPosition() {
      return this.mViewHolder.getPosition();
    }
    
    public boolean isItemChanged() {
      return this.mViewHolder.isUpdated();
    }
    
    public boolean isItemRemoved() {
      return this.mViewHolder.isRemoved();
    }
    
    public boolean isViewInvalid() {
      return this.mViewHolder.isInvalid();
    }
    
    public boolean viewNeedsUpdate() {
      return this.mViewHolder.needsUpdate();
    }
  }
  
  public static interface OnChildAttachStateChangeListener {
    void onChildViewAttachedToWindow(View param1View);
    
    void onChildViewDetachedFromWindow(View param1View);
  }
  
  public static abstract class OnFlingListener {
    public abstract boolean onFling(int param1Int1, int param1Int2);
  }
  
  public static interface OnItemTouchListener {
    boolean onInterceptTouchEvent(RecyclerView param1RecyclerView, MotionEvent param1MotionEvent);
    
    void onRequestDisallowInterceptTouchEvent(boolean param1Boolean);
    
    void onTouchEvent(RecyclerView param1RecyclerView, MotionEvent param1MotionEvent);
  }
  
  public static abstract class OnScrollListener {
    public void onScrollStateChanged(RecyclerView param1RecyclerView, int param1Int) {}
    
    public void onScrolled(RecyclerView param1RecyclerView, int param1Int1, int param1Int2) {}
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface Orientation {}
  
  public static class RecycledViewPool {
    private static final int DEFAULT_MAX_SCRAP = 5;
    
    private int mAttachCount = 0;
    
    SparseArray<ScrapData> mScrap = new SparseArray();
    
    private ScrapData getScrapDataForType(int param1Int) {
      ScrapData scrapData2 = (ScrapData)this.mScrap.get(param1Int);
      ScrapData scrapData1 = scrapData2;
      if (scrapData2 == null) {
        scrapData1 = new ScrapData();
        this.mScrap.put(param1Int, scrapData1);
      } 
      return scrapData1;
    }
    
    void attach() {
      this.mAttachCount++;
    }
    
    public void clear() {
      for (byte b = 0; b < this.mScrap.size(); b++)
        ((ScrapData)this.mScrap.valueAt(b)).mScrapHeap.clear(); 
    }
    
    void detach() {
      this.mAttachCount--;
    }
    
    void factorInBindTime(int param1Int, long param1Long) {
      ScrapData scrapData = getScrapDataForType(param1Int);
      scrapData.mBindRunningAverageNs = runningAverage(scrapData.mBindRunningAverageNs, param1Long);
    }
    
    void factorInCreateTime(int param1Int, long param1Long) {
      ScrapData scrapData = getScrapDataForType(param1Int);
      scrapData.mCreateRunningAverageNs = runningAverage(scrapData.mCreateRunningAverageNs, param1Long);
    }
    
    public RecyclerView.ViewHolder getRecycledView(int param1Int) {
      ScrapData scrapData = (ScrapData)this.mScrap.get(param1Int);
      if (scrapData != null && !scrapData.mScrapHeap.isEmpty()) {
        ArrayList<RecyclerView.ViewHolder> arrayList = scrapData.mScrapHeap;
        for (param1Int = arrayList.size() - 1; param1Int >= 0; param1Int--) {
          if (!((RecyclerView.ViewHolder)arrayList.get(param1Int)).isAttachedToTransitionOverlay())
            return arrayList.remove(param1Int); 
        } 
      } 
      return null;
    }
    
    public int getRecycledViewCount(int param1Int) {
      return (getScrapDataForType(param1Int)).mScrapHeap.size();
    }
    
    void onAdapterChanged(RecyclerView.Adapter param1Adapter1, RecyclerView.Adapter param1Adapter2, boolean param1Boolean) {
      if (param1Adapter1 != null)
        detach(); 
      if (!param1Boolean && this.mAttachCount == 0)
        clear(); 
      if (param1Adapter2 != null)
        attach(); 
    }
    
    public void putRecycledView(RecyclerView.ViewHolder param1ViewHolder) {
      int i = param1ViewHolder.getItemViewType();
      ArrayList<RecyclerView.ViewHolder> arrayList = (getScrapDataForType(i)).mScrapHeap;
      if (((ScrapData)this.mScrap.get(i)).mMaxScrap <= arrayList.size())
        return; 
      param1ViewHolder.resetInternal();
      arrayList.add(param1ViewHolder);
    }
    
    long runningAverage(long param1Long1, long param1Long2) {
      return (param1Long1 == 0L) ? param1Long2 : (param1Long1 / 4L * 3L + param1Long2 / 4L);
    }
    
    public void setMaxRecycledViews(int param1Int1, int param1Int2) {
      ScrapData scrapData = getScrapDataForType(param1Int1);
      scrapData.mMaxScrap = param1Int2;
      ArrayList<RecyclerView.ViewHolder> arrayList = scrapData.mScrapHeap;
      while (arrayList.size() > param1Int2)
        arrayList.remove(arrayList.size() - 1); 
    }
    
    int size() {
      int i = 0;
      byte b = 0;
      while (b < this.mScrap.size()) {
        ArrayList<RecyclerView.ViewHolder> arrayList = ((ScrapData)this.mScrap.valueAt(b)).mScrapHeap;
        int j = i;
        if (arrayList != null)
          j = i + arrayList.size(); 
        b++;
        i = j;
      } 
      return i;
    }
    
    boolean willBindInTime(int param1Int, long param1Long1, long param1Long2) {
      long l = (getScrapDataForType(param1Int)).mBindRunningAverageNs;
      return (l == 0L || param1Long1 + l < param1Long2);
    }
    
    boolean willCreateInTime(int param1Int, long param1Long1, long param1Long2) {
      long l = (getScrapDataForType(param1Int)).mCreateRunningAverageNs;
      return (l == 0L || param1Long1 + l < param1Long2);
    }
    
    static class ScrapData {
      long mBindRunningAverageNs = 0L;
      
      long mCreateRunningAverageNs = 0L;
      
      int mMaxScrap = 5;
      
      final ArrayList<RecyclerView.ViewHolder> mScrapHeap = new ArrayList<>();
    }
  }
  
  static class ScrapData {
    long mBindRunningAverageNs = 0L;
    
    long mCreateRunningAverageNs = 0L;
    
    int mMaxScrap = 5;
    
    final ArrayList<RecyclerView.ViewHolder> mScrapHeap = new ArrayList<>();
  }
  
  public final class Recycler {
    static final int DEFAULT_CACHE_SIZE = 2;
    
    final ArrayList<RecyclerView.ViewHolder> mAttachedScrap;
    
    final ArrayList<RecyclerView.ViewHolder> mCachedViews;
    
    ArrayList<RecyclerView.ViewHolder> mChangedScrap;
    
    RecyclerView.RecycledViewPool mRecyclerPool;
    
    private int mRequestedCacheMax;
    
    private final List<RecyclerView.ViewHolder> mUnmodifiableAttachedScrap;
    
    private RecyclerView.ViewCacheExtension mViewCacheExtension;
    
    int mViewCacheMax;
    
    final RecyclerView this$0;
    
    public Recycler() {
      ArrayList<RecyclerView.ViewHolder> arrayList = new ArrayList();
      this.mAttachedScrap = arrayList;
      this.mChangedScrap = null;
      this.mCachedViews = new ArrayList<>();
      this.mUnmodifiableAttachedScrap = Collections.unmodifiableList(arrayList);
      this.mRequestedCacheMax = 2;
      this.mViewCacheMax = 2;
    }
    
    private void attachAccessibilityDelegateOnBind(RecyclerView.ViewHolder param1ViewHolder) {
      if (RecyclerView.this.isAccessibilityEnabled()) {
        View view = param1ViewHolder.itemView;
        if (ViewCompat.getImportantForAccessibility(view) == 0)
          ViewCompat.setImportantForAccessibility(view, 1); 
        if (RecyclerView.this.mAccessibilityDelegate == null)
          return; 
        AccessibilityDelegateCompat accessibilityDelegateCompat = RecyclerView.this.mAccessibilityDelegate.getItemDelegate();
        if (accessibilityDelegateCompat instanceof RecyclerViewAccessibilityDelegate.ItemDelegate)
          ((RecyclerViewAccessibilityDelegate.ItemDelegate)accessibilityDelegateCompat).saveOriginalDelegate(view); 
        ViewCompat.setAccessibilityDelegate(view, accessibilityDelegateCompat);
      } 
    }
    
    private void invalidateDisplayListInt(ViewGroup param1ViewGroup, boolean param1Boolean) {
      int i;
      for (i = param1ViewGroup.getChildCount() - 1; i >= 0; i--) {
        View view = param1ViewGroup.getChildAt(i);
        if (view instanceof ViewGroup)
          invalidateDisplayListInt((ViewGroup)view, true); 
      } 
      if (!param1Boolean)
        return; 
      if (param1ViewGroup.getVisibility() == 4) {
        param1ViewGroup.setVisibility(0);
        param1ViewGroup.setVisibility(4);
      } else {
        i = param1ViewGroup.getVisibility();
        param1ViewGroup.setVisibility(4);
        param1ViewGroup.setVisibility(i);
      } 
    }
    
    private void invalidateDisplayListInt(RecyclerView.ViewHolder param1ViewHolder) {
      if (param1ViewHolder.itemView instanceof ViewGroup)
        invalidateDisplayListInt((ViewGroup)param1ViewHolder.itemView, false); 
    }
    
    private boolean tryBindViewHolderByDeadline(RecyclerView.ViewHolder param1ViewHolder, int param1Int1, int param1Int2, long param1Long) {
      param1ViewHolder.mOwnerRecyclerView = RecyclerView.this;
      int i = param1ViewHolder.getItemViewType();
      long l = RecyclerView.this.getNanoTime();
      if (param1Long != Long.MAX_VALUE && !this.mRecyclerPool.willBindInTime(i, l, param1Long))
        return false; 
      RecyclerView.this.mAdapter.bindViewHolder(param1ViewHolder, param1Int1);
      param1Long = RecyclerView.this.getNanoTime();
      this.mRecyclerPool.factorInBindTime(param1ViewHolder.getItemViewType(), param1Long - l);
      attachAccessibilityDelegateOnBind(param1ViewHolder);
      if (RecyclerView.this.mState.isPreLayout())
        param1ViewHolder.mPreLayoutPosition = param1Int2; 
      return true;
    }
    
    void addViewHolderToRecycledViewPool(RecyclerView.ViewHolder param1ViewHolder, boolean param1Boolean) {
      RecyclerView.clearNestedRecyclerViewIfNotNested(param1ViewHolder);
      View view = param1ViewHolder.itemView;
      if (RecyclerView.this.mAccessibilityDelegate != null) {
        AccessibilityDelegateCompat accessibilityDelegateCompat2 = RecyclerView.this.mAccessibilityDelegate.getItemDelegate();
        AccessibilityDelegateCompat accessibilityDelegateCompat1 = null;
        if (accessibilityDelegateCompat2 instanceof RecyclerViewAccessibilityDelegate.ItemDelegate)
          accessibilityDelegateCompat1 = ((RecyclerViewAccessibilityDelegate.ItemDelegate)accessibilityDelegateCompat2).getAndRemoveOriginalDelegateForItem(view); 
        ViewCompat.setAccessibilityDelegate(view, accessibilityDelegateCompat1);
      } 
      if (param1Boolean)
        dispatchViewRecycled(param1ViewHolder); 
      param1ViewHolder.mOwnerRecyclerView = null;
      getRecycledViewPool().putRecycledView(param1ViewHolder);
    }
    
    public void bindViewToPosition(View param1View, int param1Int) {
      RecyclerView.ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(param1View);
      if (viewHolder != null) {
        int i = RecyclerView.this.mAdapterHelper.findPositionOffset(param1Int);
        if (i >= 0 && i < RecyclerView.this.mAdapter.getItemCount()) {
          RecyclerView.LayoutParams layoutParams;
          tryBindViewHolderByDeadline(viewHolder, i, param1Int, Long.MAX_VALUE);
          ViewGroup.LayoutParams layoutParams1 = viewHolder.itemView.getLayoutParams();
          if (layoutParams1 == null) {
            layoutParams = (RecyclerView.LayoutParams)RecyclerView.this.generateDefaultLayoutParams();
            viewHolder.itemView.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
          } else if (!RecyclerView.this.checkLayoutParams((ViewGroup.LayoutParams)layoutParams)) {
            layoutParams = (RecyclerView.LayoutParams)RecyclerView.this.generateLayoutParams((ViewGroup.LayoutParams)layoutParams);
            viewHolder.itemView.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
          } else {
            layoutParams = layoutParams;
          } 
          boolean bool = true;
          layoutParams.mInsetsDirty = true;
          layoutParams.mViewHolder = viewHolder;
          if (viewHolder.itemView.getParent() != null)
            bool = false; 
          layoutParams.mPendingInvalidate = bool;
          return;
        } 
        throw new IndexOutOfBoundsException("Inconsistency detected. Invalid item position " + param1Int + "(offset:" + i + ").state:" + RecyclerView.this.mState.getItemCount() + RecyclerView.this.exceptionLabel());
      } 
      throw new IllegalArgumentException("The view does not have a ViewHolder. You cannot pass arbitrary views to this method, they should be created by the Adapter" + RecyclerView.this.exceptionLabel());
    }
    
    public void clear() {
      this.mAttachedScrap.clear();
      recycleAndClearCachedViews();
    }
    
    void clearOldPositions() {
      int i = this.mCachedViews.size();
      byte b;
      for (b = 0; b < i; b++)
        ((RecyclerView.ViewHolder)this.mCachedViews.get(b)).clearOldPosition(); 
      i = this.mAttachedScrap.size();
      for (b = 0; b < i; b++)
        ((RecyclerView.ViewHolder)this.mAttachedScrap.get(b)).clearOldPosition(); 
      ArrayList<RecyclerView.ViewHolder> arrayList = this.mChangedScrap;
      if (arrayList != null) {
        i = arrayList.size();
        for (b = 0; b < i; b++)
          ((RecyclerView.ViewHolder)this.mChangedScrap.get(b)).clearOldPosition(); 
      } 
    }
    
    void clearScrap() {
      this.mAttachedScrap.clear();
      ArrayList<RecyclerView.ViewHolder> arrayList = this.mChangedScrap;
      if (arrayList != null)
        arrayList.clear(); 
    }
    
    public int convertPreLayoutPositionToPostLayout(int param1Int) {
      if (param1Int >= 0 && param1Int < RecyclerView.this.mState.getItemCount())
        return !RecyclerView.this.mState.isPreLayout() ? param1Int : RecyclerView.this.mAdapterHelper.findPositionOffset(param1Int); 
      throw new IndexOutOfBoundsException("invalid position " + param1Int + ". State item count is " + RecyclerView.this.mState.getItemCount() + RecyclerView.this.exceptionLabel());
    }
    
    void dispatchViewRecycled(RecyclerView.ViewHolder param1ViewHolder) {
      if (RecyclerView.this.mRecyclerListener != null)
        RecyclerView.this.mRecyclerListener.onViewRecycled(param1ViewHolder); 
      if (RecyclerView.this.mAdapter != null)
        RecyclerView.this.mAdapter.onViewRecycled(param1ViewHolder); 
      if (RecyclerView.this.mState != null)
        RecyclerView.this.mViewInfoStore.removeViewHolder(param1ViewHolder); 
    }
    
    RecyclerView.ViewHolder getChangedScrapViewForPosition(int param1Int) {
      ArrayList<RecyclerView.ViewHolder> arrayList = this.mChangedScrap;
      if (arrayList != null) {
        int i = arrayList.size();
        if (i != 0) {
          for (byte b = 0; b < i; b++) {
            RecyclerView.ViewHolder viewHolder = this.mChangedScrap.get(b);
            if (!viewHolder.wasReturnedFromScrap() && viewHolder.getLayoutPosition() == param1Int) {
              viewHolder.addFlags(32);
              return viewHolder;
            } 
          } 
          if (RecyclerView.this.mAdapter.hasStableIds()) {
            param1Int = RecyclerView.this.mAdapterHelper.findPositionOffset(param1Int);
            if (param1Int > 0 && param1Int < RecyclerView.this.mAdapter.getItemCount()) {
              long l = RecyclerView.this.mAdapter.getItemId(param1Int);
              for (param1Int = 0; param1Int < i; param1Int++) {
                RecyclerView.ViewHolder viewHolder = this.mChangedScrap.get(param1Int);
                if (!viewHolder.wasReturnedFromScrap() && viewHolder.getItemId() == l) {
                  viewHolder.addFlags(32);
                  return viewHolder;
                } 
              } 
            } 
          } 
          return null;
        } 
      } 
      return null;
    }
    
    RecyclerView.RecycledViewPool getRecycledViewPool() {
      if (this.mRecyclerPool == null)
        this.mRecyclerPool = new RecyclerView.RecycledViewPool(); 
      return this.mRecyclerPool;
    }
    
    int getScrapCount() {
      return this.mAttachedScrap.size();
    }
    
    public List<RecyclerView.ViewHolder> getScrapList() {
      return this.mUnmodifiableAttachedScrap;
    }
    
    RecyclerView.ViewHolder getScrapOrCachedViewForId(long param1Long, int param1Int, boolean param1Boolean) {
      int i;
      for (i = this.mAttachedScrap.size() - 1; i >= 0; i--) {
        RecyclerView.ViewHolder viewHolder = this.mAttachedScrap.get(i);
        if (viewHolder.getItemId() == param1Long && !viewHolder.wasReturnedFromScrap()) {
          if (param1Int == viewHolder.getItemViewType()) {
            viewHolder.addFlags(32);
            if (viewHolder.isRemoved() && !RecyclerView.this.mState.isPreLayout())
              viewHolder.setFlags(2, 14); 
            return viewHolder;
          } 
          if (!param1Boolean) {
            this.mAttachedScrap.remove(i);
            RecyclerView.this.removeDetachedView(viewHolder.itemView, false);
            quickRecycleScrapView(viewHolder.itemView);
          } 
        } 
      } 
      for (i = this.mCachedViews.size() - 1; i >= 0; i--) {
        RecyclerView.ViewHolder viewHolder = this.mCachedViews.get(i);
        if (viewHolder.getItemId() == param1Long && !viewHolder.isAttachedToTransitionOverlay()) {
          if (param1Int == viewHolder.getItemViewType()) {
            if (!param1Boolean)
              this.mCachedViews.remove(i); 
            return viewHolder;
          } 
          if (!param1Boolean) {
            recycleCachedViewAt(i);
            return null;
          } 
        } 
      } 
      return null;
    }
    
    RecyclerView.ViewHolder getScrapOrHiddenOrCachedHolderForPosition(int param1Int, boolean param1Boolean) {
      int i = this.mAttachedScrap.size();
      byte b;
      for (b = 0; b < i; b++) {
        RecyclerView.ViewHolder viewHolder = this.mAttachedScrap.get(b);
        if (!viewHolder.wasReturnedFromScrap() && viewHolder.getLayoutPosition() == param1Int && !viewHolder.isInvalid() && (RecyclerView.this.mState.mInPreLayout || !viewHolder.isRemoved())) {
          viewHolder.addFlags(32);
          return viewHolder;
        } 
      } 
      if (!param1Boolean) {
        View view = RecyclerView.this.mChildHelper.findHiddenNonRemovedView(param1Int);
        if (view != null) {
          RecyclerView.ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(view);
          RecyclerView.this.mChildHelper.unhide(view);
          param1Int = RecyclerView.this.mChildHelper.indexOfChild(view);
          if (param1Int != -1) {
            RecyclerView.this.mChildHelper.detachViewFromParent(param1Int);
            scrapView(view);
            viewHolder.addFlags(8224);
            return viewHolder;
          } 
          throw new IllegalStateException("layout index should not be -1 after unhiding a view:" + viewHolder + RecyclerView.this.exceptionLabel());
        } 
      } 
      i = this.mCachedViews.size();
      for (b = 0; b < i; b++) {
        RecyclerView.ViewHolder viewHolder = this.mCachedViews.get(b);
        if (!viewHolder.isInvalid() && viewHolder.getLayoutPosition() == param1Int && !viewHolder.isAttachedToTransitionOverlay()) {
          if (!param1Boolean)
            this.mCachedViews.remove(b); 
          return viewHolder;
        } 
      } 
      return null;
    }
    
    View getScrapViewAt(int param1Int) {
      return ((RecyclerView.ViewHolder)this.mAttachedScrap.get(param1Int)).itemView;
    }
    
    public View getViewForPosition(int param1Int) {
      return getViewForPosition(param1Int, false);
    }
    
    View getViewForPosition(int param1Int, boolean param1Boolean) {
      return (tryGetViewHolderForPositionByDeadline(param1Int, param1Boolean, Long.MAX_VALUE)).itemView;
    }
    
    void markItemDecorInsetsDirty() {
      int i = this.mCachedViews.size();
      for (byte b = 0; b < i; b++) {
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)((RecyclerView.ViewHolder)this.mCachedViews.get(b)).itemView.getLayoutParams();
        if (layoutParams != null)
          layoutParams.mInsetsDirty = true; 
      } 
    }
    
    void markKnownViewsInvalid() {
      int i = this.mCachedViews.size();
      for (byte b = 0; b < i; b++) {
        RecyclerView.ViewHolder viewHolder = this.mCachedViews.get(b);
        if (viewHolder != null) {
          viewHolder.addFlags(6);
          viewHolder.addChangePayload(null);
        } 
      } 
      if (RecyclerView.this.mAdapter == null || !RecyclerView.this.mAdapter.hasStableIds())
        recycleAndClearCachedViews(); 
    }
    
    void offsetPositionRecordsForInsert(int param1Int1, int param1Int2) {
      int i = this.mCachedViews.size();
      for (byte b = 0; b < i; b++) {
        RecyclerView.ViewHolder viewHolder = this.mCachedViews.get(b);
        if (viewHolder != null && viewHolder.mPosition >= param1Int1)
          viewHolder.offsetPosition(param1Int2, true); 
      } 
    }
    
    void offsetPositionRecordsForMove(int param1Int1, int param1Int2) {
      boolean bool;
      int i;
      int j;
      if (param1Int1 < param1Int2) {
        i = param1Int1;
        j = param1Int2;
        bool = true;
      } else {
        i = param1Int2;
        j = param1Int1;
        bool = true;
      } 
      int k = this.mCachedViews.size();
      for (byte b = 0; b < k; b++) {
        RecyclerView.ViewHolder viewHolder = this.mCachedViews.get(b);
        if (viewHolder != null && viewHolder.mPosition >= i && viewHolder.mPosition <= j)
          if (viewHolder.mPosition == param1Int1) {
            viewHolder.offsetPosition(param1Int2 - param1Int1, false);
          } else {
            viewHolder.offsetPosition(bool, false);
          }  
      } 
    }
    
    void offsetPositionRecordsForRemove(int param1Int1, int param1Int2, boolean param1Boolean) {
      for (int i = this.mCachedViews.size() - 1; i >= 0; i--) {
        RecyclerView.ViewHolder viewHolder = this.mCachedViews.get(i);
        if (viewHolder != null)
          if (viewHolder.mPosition >= param1Int1 + param1Int2) {
            viewHolder.offsetPosition(-param1Int2, param1Boolean);
          } else if (viewHolder.mPosition >= param1Int1) {
            viewHolder.addFlags(8);
            recycleCachedViewAt(i);
          }  
      } 
    }
    
    void onAdapterChanged(RecyclerView.Adapter param1Adapter1, RecyclerView.Adapter param1Adapter2, boolean param1Boolean) {
      clear();
      getRecycledViewPool().onAdapterChanged(param1Adapter1, param1Adapter2, param1Boolean);
    }
    
    void quickRecycleScrapView(View param1View) {
      RecyclerView.ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(param1View);
      viewHolder.mScrapContainer = null;
      viewHolder.mInChangeScrap = false;
      viewHolder.clearReturnedFromScrapFlag();
      recycleViewHolderInternal(viewHolder);
    }
    
    void recycleAndClearCachedViews() {
      for (int i = this.mCachedViews.size() - 1; i >= 0; i--)
        recycleCachedViewAt(i); 
      this.mCachedViews.clear();
      if (RecyclerView.ALLOW_THREAD_GAP_WORK)
        RecyclerView.this.mPrefetchRegistry.clearPrefetchPositions(); 
    }
    
    void recycleCachedViewAt(int param1Int) {
      addViewHolderToRecycledViewPool(this.mCachedViews.get(param1Int), true);
      this.mCachedViews.remove(param1Int);
    }
    
    public void recycleView(View param1View) {
      RecyclerView.ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(param1View);
      if (viewHolder.isTmpDetached())
        RecyclerView.this.removeDetachedView(param1View, false); 
      if (viewHolder.isScrap()) {
        viewHolder.unScrap();
      } else if (viewHolder.wasReturnedFromScrap()) {
        viewHolder.clearReturnedFromScrapFlag();
      } 
      recycleViewHolderInternal(viewHolder);
      if (RecyclerView.this.mItemAnimator != null && !viewHolder.isRecyclable())
        RecyclerView.this.mItemAnimator.endAnimation(viewHolder); 
    }
    
    void recycleViewHolderInternal(RecyclerView.ViewHolder param1ViewHolder) {
      // Byte code:
      //   0: aload_1
      //   1: invokevirtual isScrap : ()Z
      //   4: istore #8
      //   6: iconst_0
      //   7: istore #7
      //   9: iload #8
      //   11: ifne -> 386
      //   14: aload_1
      //   15: getfield itemView : Landroid/view/View;
      //   18: invokevirtual getParent : ()Landroid/view/ViewParent;
      //   21: ifnull -> 27
      //   24: goto -> 386
      //   27: aload_1
      //   28: invokevirtual isTmpDetached : ()Z
      //   31: ifne -> 348
      //   34: aload_1
      //   35: invokevirtual shouldIgnore : ()Z
      //   38: ifne -> 314
      //   41: aload_1
      //   42: invokevirtual doesTransientStatePreventRecycling : ()Z
      //   45: istore #7
      //   47: aload_0
      //   48: getfield this$0 : Landroidx/recyclerview/widget/RecyclerView;
      //   51: getfield mAdapter : Landroidx/recyclerview/widget/RecyclerView$Adapter;
      //   54: ifnull -> 81
      //   57: iload #7
      //   59: ifeq -> 81
      //   62: aload_0
      //   63: getfield this$0 : Landroidx/recyclerview/widget/RecyclerView;
      //   66: getfield mAdapter : Landroidx/recyclerview/widget/RecyclerView$Adapter;
      //   69: aload_1
      //   70: invokevirtual onFailedToRecycleView : (Landroidx/recyclerview/widget/RecyclerView$ViewHolder;)Z
      //   73: ifeq -> 81
      //   76: iconst_1
      //   77: istore_2
      //   78: goto -> 83
      //   81: iconst_0
      //   82: istore_2
      //   83: iconst_0
      //   84: istore_3
      //   85: iconst_0
      //   86: istore #6
      //   88: iconst_0
      //   89: istore #5
      //   91: iload_2
      //   92: ifne -> 106
      //   95: iload #5
      //   97: istore #4
      //   99: aload_1
      //   100: invokevirtual isRecyclable : ()Z
      //   103: ifeq -> 283
      //   106: iload #6
      //   108: istore_2
      //   109: aload_0
      //   110: getfield mViewCacheMax : I
      //   113: ifle -> 262
      //   116: iload #6
      //   118: istore_2
      //   119: aload_1
      //   120: sipush #526
      //   123: invokevirtual hasAnyOfTheFlags : (I)Z
      //   126: ifne -> 262
      //   129: aload_0
      //   130: getfield mCachedViews : Ljava/util/ArrayList;
      //   133: invokevirtual size : ()I
      //   136: istore_3
      //   137: iload_3
      //   138: istore_2
      //   139: iload_3
      //   140: aload_0
      //   141: getfield mViewCacheMax : I
      //   144: if_icmplt -> 162
      //   147: iload_3
      //   148: istore_2
      //   149: iload_3
      //   150: ifle -> 162
      //   153: aload_0
      //   154: iconst_0
      //   155: invokevirtual recycleCachedViewAt : (I)V
      //   158: iload_3
      //   159: iconst_1
      //   160: isub
      //   161: istore_2
      //   162: iload_2
      //   163: istore_3
      //   164: iload_3
      //   165: istore #4
      //   167: getstatic androidx/recyclerview/widget/RecyclerView.ALLOW_THREAD_GAP_WORK : Z
      //   170: ifeq -> 250
      //   173: iload_3
      //   174: istore #4
      //   176: iload_2
      //   177: ifle -> 250
      //   180: iload_3
      //   181: istore #4
      //   183: aload_0
      //   184: getfield this$0 : Landroidx/recyclerview/widget/RecyclerView;
      //   187: getfield mPrefetchRegistry : Landroidx/recyclerview/widget/GapWorker$LayoutPrefetchRegistryImpl;
      //   190: aload_1
      //   191: getfield mPosition : I
      //   194: invokevirtual lastPrefetchIncludedPosition : (I)Z
      //   197: ifne -> 250
      //   200: iinc #2, -1
      //   203: iload_2
      //   204: iflt -> 245
      //   207: aload_0
      //   208: getfield mCachedViews : Ljava/util/ArrayList;
      //   211: iload_2
      //   212: invokevirtual get : (I)Ljava/lang/Object;
      //   215: checkcast androidx/recyclerview/widget/RecyclerView$ViewHolder
      //   218: getfield mPosition : I
      //   221: istore_3
      //   222: aload_0
      //   223: getfield this$0 : Landroidx/recyclerview/widget/RecyclerView;
      //   226: getfield mPrefetchRegistry : Landroidx/recyclerview/widget/GapWorker$LayoutPrefetchRegistryImpl;
      //   229: iload_3
      //   230: invokevirtual lastPrefetchIncludedPosition : (I)Z
      //   233: ifne -> 239
      //   236: goto -> 245
      //   239: iinc #2, -1
      //   242: goto -> 203
      //   245: iload_2
      //   246: iconst_1
      //   247: iadd
      //   248: istore #4
      //   250: aload_0
      //   251: getfield mCachedViews : Ljava/util/ArrayList;
      //   254: iload #4
      //   256: aload_1
      //   257: invokevirtual add : (ILjava/lang/Object;)V
      //   260: iconst_1
      //   261: istore_2
      //   262: iload_2
      //   263: istore_3
      //   264: iload #5
      //   266: istore #4
      //   268: iload_2
      //   269: ifne -> 283
      //   272: aload_0
      //   273: aload_1
      //   274: iconst_1
      //   275: invokevirtual addViewHolderToRecycledViewPool : (Landroidx/recyclerview/widget/RecyclerView$ViewHolder;Z)V
      //   278: iconst_1
      //   279: istore #4
      //   281: iload_2
      //   282: istore_3
      //   283: aload_0
      //   284: getfield this$0 : Landroidx/recyclerview/widget/RecyclerView;
      //   287: getfield mViewInfoStore : Landroidx/recyclerview/widget/ViewInfoStore;
      //   290: aload_1
      //   291: invokevirtual removeViewHolder : (Landroidx/recyclerview/widget/RecyclerView$ViewHolder;)V
      //   294: iload_3
      //   295: ifne -> 313
      //   298: iload #4
      //   300: ifne -> 313
      //   303: iload #7
      //   305: ifeq -> 313
      //   308: aload_1
      //   309: aconst_null
      //   310: putfield mOwnerRecyclerView : Landroidx/recyclerview/widget/RecyclerView;
      //   313: return
      //   314: new java/lang/IllegalArgumentException
      //   317: dup
      //   318: new java/lang/StringBuilder
      //   321: dup
      //   322: invokespecial <init> : ()V
      //   325: ldc_w 'Trying to recycle an ignored view holder. You should first call stopIgnoringView(view) before calling recycle.'
      //   328: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   331: aload_0
      //   332: getfield this$0 : Landroidx/recyclerview/widget/RecyclerView;
      //   335: invokevirtual exceptionLabel : ()Ljava/lang/String;
      //   338: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   341: invokevirtual toString : ()Ljava/lang/String;
      //   344: invokespecial <init> : (Ljava/lang/String;)V
      //   347: athrow
      //   348: new java/lang/IllegalArgumentException
      //   351: dup
      //   352: new java/lang/StringBuilder
      //   355: dup
      //   356: invokespecial <init> : ()V
      //   359: ldc_w 'Tmp detached view should be removed from RecyclerView before it can be recycled: '
      //   362: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   365: aload_1
      //   366: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
      //   369: aload_0
      //   370: getfield this$0 : Landroidx/recyclerview/widget/RecyclerView;
      //   373: invokevirtual exceptionLabel : ()Ljava/lang/String;
      //   376: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   379: invokevirtual toString : ()Ljava/lang/String;
      //   382: invokespecial <init> : (Ljava/lang/String;)V
      //   385: athrow
      //   386: new java/lang/StringBuilder
      //   389: dup
      //   390: invokespecial <init> : ()V
      //   393: ldc_w 'Scrapped or attached views may not be recycled. isScrap:'
      //   396: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   399: aload_1
      //   400: invokevirtual isScrap : ()Z
      //   403: invokevirtual append : (Z)Ljava/lang/StringBuilder;
      //   406: ldc_w ' isAttached:'
      //   409: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   412: astore #9
      //   414: aload_1
      //   415: getfield itemView : Landroid/view/View;
      //   418: invokevirtual getParent : ()Landroid/view/ViewParent;
      //   421: ifnull -> 427
      //   424: iconst_1
      //   425: istore #7
      //   427: new java/lang/IllegalArgumentException
      //   430: dup
      //   431: aload #9
      //   433: iload #7
      //   435: invokevirtual append : (Z)Ljava/lang/StringBuilder;
      //   438: aload_0
      //   439: getfield this$0 : Landroidx/recyclerview/widget/RecyclerView;
      //   442: invokevirtual exceptionLabel : ()Ljava/lang/String;
      //   445: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   448: invokevirtual toString : ()Ljava/lang/String;
      //   451: invokespecial <init> : (Ljava/lang/String;)V
      //   454: athrow
    }
    
    void scrapView(View param1View) {
      RecyclerView.ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(param1View);
      if (viewHolder.hasAnyOfTheFlags(12) || !viewHolder.isUpdated() || RecyclerView.this.canReuseUpdatedViewHolder(viewHolder)) {
        if (!viewHolder.isInvalid() || viewHolder.isRemoved() || RecyclerView.this.mAdapter.hasStableIds()) {
          viewHolder.setScrapContainer(this, false);
          this.mAttachedScrap.add(viewHolder);
          return;
        } 
        throw new IllegalArgumentException("Called scrap view with an invalid view. Invalid views cannot be reused from scrap, they should rebound from recycler pool." + RecyclerView.this.exceptionLabel());
      } 
      if (this.mChangedScrap == null)
        this.mChangedScrap = new ArrayList<>(); 
      viewHolder.setScrapContainer(this, true);
      this.mChangedScrap.add(viewHolder);
    }
    
    void setRecycledViewPool(RecyclerView.RecycledViewPool param1RecycledViewPool) {
      RecyclerView.RecycledViewPool recycledViewPool = this.mRecyclerPool;
      if (recycledViewPool != null)
        recycledViewPool.detach(); 
      this.mRecyclerPool = param1RecycledViewPool;
      if (param1RecycledViewPool != null && RecyclerView.this.getAdapter() != null)
        this.mRecyclerPool.attach(); 
    }
    
    void setViewCacheExtension(RecyclerView.ViewCacheExtension param1ViewCacheExtension) {
      this.mViewCacheExtension = param1ViewCacheExtension;
    }
    
    public void setViewCacheSize(int param1Int) {
      this.mRequestedCacheMax = param1Int;
      updateViewCacheSize();
    }
    
    RecyclerView.ViewHolder tryGetViewHolderForPositionByDeadline(int param1Int, boolean param1Boolean, long param1Long) {
      if (param1Int >= 0 && param1Int < RecyclerView.this.mState.getItemCount()) {
        RecyclerView recyclerView;
        RecyclerView.LayoutParams layoutParams;
        int j = 0;
        RecyclerView.ViewHolder viewHolder2 = null;
        boolean bool1 = RecyclerView.this.mState.isPreLayout();
        boolean bool = true;
        if (bool1) {
          byte b;
          viewHolder2 = getChangedScrapViewForPosition(param1Int);
          if (viewHolder2 != null) {
            b = 1;
          } else {
            b = 0;
          } 
          j = b;
        } 
        int i = j;
        RecyclerView.ViewHolder viewHolder1 = viewHolder2;
        if (viewHolder2 == null) {
          viewHolder2 = getScrapOrHiddenOrCachedHolderForPosition(param1Int, param1Boolean);
          i = j;
          viewHolder1 = viewHolder2;
          if (viewHolder2 != null)
            if (!validateViewHolderForOffsetPosition(viewHolder2)) {
              if (!param1Boolean) {
                viewHolder2.addFlags(4);
                if (viewHolder2.isScrap()) {
                  RecyclerView.this.removeDetachedView(viewHolder2.itemView, false);
                  viewHolder2.unScrap();
                } else if (viewHolder2.wasReturnedFromScrap()) {
                  viewHolder2.clearReturnedFromScrapFlag();
                } 
                recycleViewHolderInternal(viewHolder2);
              } 
              viewHolder1 = null;
              i = j;
            } else {
              i = 1;
              viewHolder1 = viewHolder2;
            }  
        } 
        if (viewHolder1 == null) {
          int k = RecyclerView.this.mAdapterHelper.findPositionOffset(param1Int);
          if (k >= 0 && k < RecyclerView.this.mAdapter.getItemCount()) {
            RecyclerView recyclerView1;
            int m = RecyclerView.this.mAdapter.getItemViewType(k);
            j = i;
            viewHolder2 = viewHolder1;
            if (RecyclerView.this.mAdapter.hasStableIds()) {
              viewHolder1 = getScrapOrCachedViewForId(RecyclerView.this.mAdapter.getItemId(k), m, param1Boolean);
              j = i;
              viewHolder2 = viewHolder1;
              if (viewHolder1 != null) {
                viewHolder1.mPosition = k;
                j = 1;
                viewHolder2 = viewHolder1;
              } 
            } 
            viewHolder1 = viewHolder2;
            if (viewHolder2 == null) {
              RecyclerView.ViewCacheExtension viewCacheExtension = this.mViewCacheExtension;
              viewHolder1 = viewHolder2;
              if (viewCacheExtension != null) {
                View view = viewCacheExtension.getViewForPositionAndType(this, param1Int, m);
                viewHolder1 = viewHolder2;
                if (view != null) {
                  viewHolder1 = RecyclerView.this.getChildViewHolder(view);
                  if (viewHolder1 != null) {
                    if (viewHolder1.shouldIgnore())
                      throw new IllegalArgumentException("getViewForPositionAndType returned a view that is ignored. You must call stopIgnoring before returning this view." + RecyclerView.this.exceptionLabel()); 
                  } else {
                    throw new IllegalArgumentException("getViewForPositionAndType returned a view which does not have a ViewHolder" + RecyclerView.this.exceptionLabel());
                  } 
                } 
              } 
            } 
            viewHolder2 = viewHolder1;
            if (viewHolder1 == null) {
              viewHolder1 = getRecycledViewPool().getRecycledView(m);
              viewHolder2 = viewHolder1;
              if (viewHolder1 != null) {
                viewHolder1.resetInternal();
                viewHolder2 = viewHolder1;
                if (RecyclerView.FORCE_INVALIDATE_DISPLAY_LIST) {
                  invalidateDisplayListInt(viewHolder1);
                  viewHolder2 = viewHolder1;
                } 
              } 
            } 
            if (viewHolder2 == null) {
              long l2 = RecyclerView.this.getNanoTime();
              if (param1Long != Long.MAX_VALUE && !this.mRecyclerPool.willCreateInTime(m, l2, param1Long))
                return null; 
              viewHolder1 = RecyclerView.this.mAdapter.createViewHolder(RecyclerView.this, m);
              if (RecyclerView.ALLOW_THREAD_GAP_WORK) {
                recyclerView1 = RecyclerView.findNestedRecyclerView(viewHolder1.itemView);
                if (recyclerView1 != null)
                  viewHolder1.mNestedRecyclerView = new WeakReference<>(recyclerView1); 
              } 
              long l1 = RecyclerView.this.getNanoTime();
              this.mRecyclerPool.factorInCreateTime(m, l1 - l2);
              i = j;
            } else {
              i = j;
              recyclerView = recyclerView1;
            } 
          } else {
            throw new IndexOutOfBoundsException("Inconsistency detected. Invalid item position " + param1Int + "(offset:" + k + ").state:" + RecyclerView.this.mState.getItemCount() + RecyclerView.this.exceptionLabel());
          } 
        } 
        if (i && !RecyclerView.this.mState.isPreLayout() && recyclerView.hasAnyOfTheFlags(8192)) {
          recyclerView.setFlags(0, 8192);
          if (RecyclerView.this.mState.mRunSimpleAnimations) {
            j = RecyclerView.ItemAnimator.buildAdapterChangeFlagsForAnimations((RecyclerView.ViewHolder)recyclerView);
            RecyclerView.ItemAnimator.ItemHolderInfo itemHolderInfo = RecyclerView.this.mItemAnimator.recordPreLayoutInformation(RecyclerView.this.mState, (RecyclerView.ViewHolder)recyclerView, j | 0x1000, recyclerView.getUnmodifiedPayloads());
            RecyclerView.this.recordAnimationInfoIfBouncedHiddenView((RecyclerView.ViewHolder)recyclerView, itemHolderInfo);
          } 
        } 
        param1Boolean = false;
        if (RecyclerView.this.mState.isPreLayout() && recyclerView.isBound()) {
          ((RecyclerView.ViewHolder)recyclerView).mPreLayoutPosition = param1Int;
        } else if (!recyclerView.isBound() || recyclerView.needsUpdate() || recyclerView.isInvalid()) {
          param1Boolean = tryBindViewHolderByDeadline((RecyclerView.ViewHolder)recyclerView, RecyclerView.this.mAdapterHelper.findPositionOffset(param1Int), param1Int, param1Long);
        } 
        ViewGroup.LayoutParams layoutParams1 = ((RecyclerView.ViewHolder)recyclerView).itemView.getLayoutParams();
        if (layoutParams1 == null) {
          layoutParams = (RecyclerView.LayoutParams)RecyclerView.this.generateDefaultLayoutParams();
          ((RecyclerView.ViewHolder)recyclerView).itemView.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
        } else if (!RecyclerView.this.checkLayoutParams((ViewGroup.LayoutParams)layoutParams)) {
          layoutParams = (RecyclerView.LayoutParams)RecyclerView.this.generateLayoutParams((ViewGroup.LayoutParams)layoutParams);
          ((RecyclerView.ViewHolder)recyclerView).itemView.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
        } else {
          layoutParams = layoutParams;
        } 
        layoutParams.mViewHolder = (RecyclerView.ViewHolder)recyclerView;
        if (i != 0 && param1Boolean) {
          param1Boolean = bool;
        } else {
          param1Boolean = false;
        } 
        layoutParams.mPendingInvalidate = param1Boolean;
        return (RecyclerView.ViewHolder)recyclerView;
      } 
      throw new IndexOutOfBoundsException("Invalid item position " + param1Int + "(" + param1Int + "). Item count:" + RecyclerView.this.mState.getItemCount() + RecyclerView.this.exceptionLabel());
    }
    
    void unscrapView(RecyclerView.ViewHolder param1ViewHolder) {
      if (param1ViewHolder.mInChangeScrap) {
        this.mChangedScrap.remove(param1ViewHolder);
      } else {
        this.mAttachedScrap.remove(param1ViewHolder);
      } 
      param1ViewHolder.mScrapContainer = null;
      param1ViewHolder.mInChangeScrap = false;
      param1ViewHolder.clearReturnedFromScrapFlag();
    }
    
    void updateViewCacheSize() {
      if (RecyclerView.this.mLayout != null) {
        i = RecyclerView.this.mLayout.mPrefetchMaxCountObserved;
      } else {
        i = 0;
      } 
      this.mViewCacheMax = this.mRequestedCacheMax + i;
      for (int i = this.mCachedViews.size() - 1; i >= 0 && this.mCachedViews.size() > this.mViewCacheMax; i--)
        recycleCachedViewAt(i); 
    }
    
    boolean validateViewHolderForOffsetPosition(RecyclerView.ViewHolder param1ViewHolder) {
      if (param1ViewHolder.isRemoved())
        return RecyclerView.this.mState.isPreLayout(); 
      if (param1ViewHolder.mPosition >= 0 && param1ViewHolder.mPosition < RecyclerView.this.mAdapter.getItemCount()) {
        boolean bool1 = RecyclerView.this.mState.isPreLayout();
        boolean bool = false;
        if (!bool1 && RecyclerView.this.mAdapter.getItemViewType(param1ViewHolder.mPosition) != param1ViewHolder.getItemViewType())
          return false; 
        if (RecyclerView.this.mAdapter.hasStableIds()) {
          if (param1ViewHolder.getItemId() == RecyclerView.this.mAdapter.getItemId(param1ViewHolder.mPosition))
            bool = true; 
          return bool;
        } 
        return true;
      } 
      throw new IndexOutOfBoundsException("Inconsistency detected. Invalid view holder adapter position" + param1ViewHolder + RecyclerView.this.exceptionLabel());
    }
    
    void viewRangeUpdate(int param1Int1, int param1Int2) {
      for (int i = this.mCachedViews.size() - 1; i >= 0; i--) {
        RecyclerView.ViewHolder viewHolder = this.mCachedViews.get(i);
        if (viewHolder != null) {
          int j = viewHolder.mPosition;
          if (j >= param1Int1 && j < param1Int1 + param1Int2) {
            viewHolder.addFlags(2);
            recycleCachedViewAt(i);
          } 
        } 
      } 
    }
  }
  
  public static interface RecyclerListener {
    void onViewRecycled(RecyclerView.ViewHolder param1ViewHolder);
  }
  
  private class RecyclerViewDataObserver extends AdapterDataObserver {
    final RecyclerView this$0;
    
    public void onChanged() {
      RecyclerView.this.assertNotInLayoutOrScroll((String)null);
      RecyclerView.this.mState.mStructureChanged = true;
      RecyclerView.this.processDataSetCompletelyChanged(true);
      if (!RecyclerView.this.mAdapterHelper.hasPendingUpdates())
        RecyclerView.this.requestLayout(); 
    }
    
    public void onItemRangeChanged(int param1Int1, int param1Int2, Object param1Object) {
      RecyclerView.this.assertNotInLayoutOrScroll((String)null);
      if (RecyclerView.this.mAdapterHelper.onItemRangeChanged(param1Int1, param1Int2, param1Object))
        triggerUpdateProcessor(); 
    }
    
    public void onItemRangeInserted(int param1Int1, int param1Int2) {
      RecyclerView.this.assertNotInLayoutOrScroll((String)null);
      if (RecyclerView.this.mAdapterHelper.onItemRangeInserted(param1Int1, param1Int2))
        triggerUpdateProcessor(); 
    }
    
    public void onItemRangeMoved(int param1Int1, int param1Int2, int param1Int3) {
      RecyclerView.this.assertNotInLayoutOrScroll((String)null);
      if (RecyclerView.this.mAdapterHelper.onItemRangeMoved(param1Int1, param1Int2, param1Int3))
        triggerUpdateProcessor(); 
    }
    
    public void onItemRangeRemoved(int param1Int1, int param1Int2) {
      RecyclerView.this.assertNotInLayoutOrScroll((String)null);
      if (RecyclerView.this.mAdapterHelper.onItemRangeRemoved(param1Int1, param1Int2))
        triggerUpdateProcessor(); 
    }
    
    void triggerUpdateProcessor() {
      if (RecyclerView.POST_UPDATES_ON_ANIMATION && RecyclerView.this.mHasFixedSize && RecyclerView.this.mIsAttached) {
        RecyclerView recyclerView = RecyclerView.this;
        ViewCompat.postOnAnimation((View)recyclerView, recyclerView.mUpdateChildViewsRunnable);
      } else {
        RecyclerView.this.mAdapterUpdateDuringMeasure = true;
        RecyclerView.this.requestLayout();
      } 
    }
  }
  
  public static class SavedState extends AbsSavedState {
    public static final Parcelable.Creator<SavedState> CREATOR = (Parcelable.Creator<SavedState>)new Parcelable.ClassLoaderCreator<SavedState>() {
        public RecyclerView.SavedState createFromParcel(Parcel param2Parcel) {
          return new RecyclerView.SavedState(param2Parcel, null);
        }
        
        public RecyclerView.SavedState createFromParcel(Parcel param2Parcel, ClassLoader param2ClassLoader) {
          return new RecyclerView.SavedState(param2Parcel, param2ClassLoader);
        }
        
        public RecyclerView.SavedState[] newArray(int param2Int) {
          return new RecyclerView.SavedState[param2Int];
        }
      };
    
    Parcelable mLayoutState;
    
    SavedState(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      super(param1Parcel, param1ClassLoader);
      if (param1ClassLoader == null)
        param1ClassLoader = RecyclerView.LayoutManager.class.getClassLoader(); 
      this.mLayoutState = param1Parcel.readParcelable(param1ClassLoader);
    }
    
    SavedState(Parcelable param1Parcelable) {
      super(param1Parcelable);
    }
    
    void copyFrom(SavedState param1SavedState) {
      this.mLayoutState = param1SavedState.mLayoutState;
    }
    
    public void writeToParcel(Parcel param1Parcel, int param1Int) {
      super.writeToParcel(param1Parcel, param1Int);
      param1Parcel.writeParcelable(this.mLayoutState, 0);
    }
  }
  
  static final class null implements Parcelable.ClassLoaderCreator<SavedState> {
    public RecyclerView.SavedState createFromParcel(Parcel param1Parcel) {
      return new RecyclerView.SavedState(param1Parcel, null);
    }
    
    public RecyclerView.SavedState createFromParcel(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      return new RecyclerView.SavedState(param1Parcel, param1ClassLoader);
    }
    
    public RecyclerView.SavedState[] newArray(int param1Int) {
      return new RecyclerView.SavedState[param1Int];
    }
  }
  
  public static class SimpleOnItemTouchListener implements OnItemTouchListener {
    public boolean onInterceptTouchEvent(RecyclerView param1RecyclerView, MotionEvent param1MotionEvent) {
      return false;
    }
    
    public void onRequestDisallowInterceptTouchEvent(boolean param1Boolean) {}
    
    public void onTouchEvent(RecyclerView param1RecyclerView, MotionEvent param1MotionEvent) {}
  }
  
  public static abstract class SmoothScroller {
    private RecyclerView.LayoutManager mLayoutManager;
    
    private boolean mPendingInitialRun;
    
    private RecyclerView mRecyclerView;
    
    private final Action mRecyclingAction = new Action(0, 0);
    
    private boolean mRunning;
    
    private boolean mStarted;
    
    private int mTargetPosition = -1;
    
    private View mTargetView;
    
    public PointF computeScrollVectorForPosition(int param1Int) {
      RecyclerView.LayoutManager layoutManager = getLayoutManager();
      if (layoutManager instanceof ScrollVectorProvider)
        return ((ScrollVectorProvider)layoutManager).computeScrollVectorForPosition(param1Int); 
      Log.w("RecyclerView", "You should override computeScrollVectorForPosition when the LayoutManager does not implement " + ScrollVectorProvider.class.getCanonicalName());
      return null;
    }
    
    public View findViewByPosition(int param1Int) {
      return this.mRecyclerView.mLayout.findViewByPosition(param1Int);
    }
    
    public int getChildCount() {
      return this.mRecyclerView.mLayout.getChildCount();
    }
    
    public int getChildPosition(View param1View) {
      return this.mRecyclerView.getChildLayoutPosition(param1View);
    }
    
    public RecyclerView.LayoutManager getLayoutManager() {
      return this.mLayoutManager;
    }
    
    public int getTargetPosition() {
      return this.mTargetPosition;
    }
    
    @Deprecated
    public void instantScrollToPosition(int param1Int) {
      this.mRecyclerView.scrollToPosition(param1Int);
    }
    
    public boolean isPendingInitialRun() {
      return this.mPendingInitialRun;
    }
    
    public boolean isRunning() {
      return this.mRunning;
    }
    
    protected void normalize(PointF param1PointF) {
      float f = (float)Math.sqrt((param1PointF.x * param1PointF.x + param1PointF.y * param1PointF.y));
      param1PointF.x /= f;
      param1PointF.y /= f;
    }
    
    void onAnimation(int param1Int1, int param1Int2) {
      RecyclerView recyclerView = this.mRecyclerView;
      if (this.mTargetPosition == -1 || recyclerView == null)
        stop(); 
      if (this.mPendingInitialRun && this.mTargetView == null && this.mLayoutManager != null) {
        PointF pointF = computeScrollVectorForPosition(this.mTargetPosition);
        if (pointF != null && (pointF.x != 0.0F || pointF.y != 0.0F))
          recyclerView.scrollStep((int)Math.signum(pointF.x), (int)Math.signum(pointF.y), (int[])null); 
      } 
      this.mPendingInitialRun = false;
      View view = this.mTargetView;
      if (view != null)
        if (getChildPosition(view) == this.mTargetPosition) {
          onTargetFound(this.mTargetView, recyclerView.mState, this.mRecyclingAction);
          this.mRecyclingAction.runIfNecessary(recyclerView);
          stop();
        } else {
          Log.e("RecyclerView", "Passed over target position while smooth scrolling.");
          this.mTargetView = null;
        }  
      if (this.mRunning) {
        onSeekTargetStep(param1Int1, param1Int2, recyclerView.mState, this.mRecyclingAction);
        boolean bool = this.mRecyclingAction.hasJumpTarget();
        this.mRecyclingAction.runIfNecessary(recyclerView);
        if (bool && this.mRunning) {
          this.mPendingInitialRun = true;
          recyclerView.mViewFlinger.postOnAnimation();
        } 
      } 
    }
    
    protected void onChildAttachedToWindow(View param1View) {
      if (getChildPosition(param1View) == getTargetPosition())
        this.mTargetView = param1View; 
    }
    
    protected abstract void onSeekTargetStep(int param1Int1, int param1Int2, RecyclerView.State param1State, Action param1Action);
    
    protected abstract void onStart();
    
    protected abstract void onStop();
    
    protected abstract void onTargetFound(View param1View, RecyclerView.State param1State, Action param1Action);
    
    public void setTargetPosition(int param1Int) {
      this.mTargetPosition = param1Int;
    }
    
    void start(RecyclerView param1RecyclerView, RecyclerView.LayoutManager param1LayoutManager) {
      param1RecyclerView.mViewFlinger.stop();
      if (this.mStarted)
        Log.w("RecyclerView", "An instance of " + getClass().getSimpleName() + " was started more than once. Each instance of" + getClass().getSimpleName() + " is intended to only be used once. You should create a new instance for each use."); 
      this.mRecyclerView = param1RecyclerView;
      this.mLayoutManager = param1LayoutManager;
      if (this.mTargetPosition != -1) {
        param1RecyclerView.mState.mTargetPosition = this.mTargetPosition;
        this.mRunning = true;
        this.mPendingInitialRun = true;
        this.mTargetView = findViewByPosition(getTargetPosition());
        onStart();
        this.mRecyclerView.mViewFlinger.postOnAnimation();
        this.mStarted = true;
        return;
      } 
      throw new IllegalArgumentException("Invalid target position");
    }
    
    protected final void stop() {
      if (!this.mRunning)
        return; 
      this.mRunning = false;
      onStop();
      this.mRecyclerView.mState.mTargetPosition = -1;
      this.mTargetView = null;
      this.mTargetPosition = -1;
      this.mPendingInitialRun = false;
      this.mLayoutManager.onSmoothScrollerStopped(this);
      this.mLayoutManager = null;
      this.mRecyclerView = null;
    }
    
    public static class Action {
      public static final int UNDEFINED_DURATION = -2147483648;
      
      private boolean mChanged = false;
      
      private int mConsecutiveUpdates = 0;
      
      private int mDuration;
      
      private int mDx;
      
      private int mDy;
      
      private Interpolator mInterpolator;
      
      private int mJumpToPosition = -1;
      
      public Action(int param2Int1, int param2Int2) {
        this(param2Int1, param2Int2, -2147483648, null);
      }
      
      public Action(int param2Int1, int param2Int2, int param2Int3) {
        this(param2Int1, param2Int2, param2Int3, null);
      }
      
      public Action(int param2Int1, int param2Int2, int param2Int3, Interpolator param2Interpolator) {
        this.mDx = param2Int1;
        this.mDy = param2Int2;
        this.mDuration = param2Int3;
        this.mInterpolator = param2Interpolator;
      }
      
      private void validate() {
        if (this.mInterpolator == null || this.mDuration >= 1) {
          if (this.mDuration >= 1)
            return; 
          throw new IllegalStateException("Scroll duration must be a positive number");
        } 
        throw new IllegalStateException("If you provide an interpolator, you must set a positive duration");
      }
      
      public int getDuration() {
        return this.mDuration;
      }
      
      public int getDx() {
        return this.mDx;
      }
      
      public int getDy() {
        return this.mDy;
      }
      
      public Interpolator getInterpolator() {
        return this.mInterpolator;
      }
      
      boolean hasJumpTarget() {
        boolean bool;
        if (this.mJumpToPosition >= 0) {
          bool = true;
        } else {
          bool = false;
        } 
        return bool;
      }
      
      public void jumpTo(int param2Int) {
        this.mJumpToPosition = param2Int;
      }
      
      void runIfNecessary(RecyclerView param2RecyclerView) {
        if (this.mJumpToPosition >= 0) {
          int i = this.mJumpToPosition;
          this.mJumpToPosition = -1;
          param2RecyclerView.jumpToPositionForSmoothScroller(i);
          this.mChanged = false;
          return;
        } 
        if (this.mChanged) {
          validate();
          param2RecyclerView.mViewFlinger.smoothScrollBy(this.mDx, this.mDy, this.mDuration, this.mInterpolator);
          int i = this.mConsecutiveUpdates + 1;
          this.mConsecutiveUpdates = i;
          if (i > 10)
            Log.e("RecyclerView", "Smooth Scroll action is being updated too frequently. Make sure you are not changing it unless necessary"); 
          this.mChanged = false;
        } else {
          this.mConsecutiveUpdates = 0;
        } 
      }
      
      public void setDuration(int param2Int) {
        this.mChanged = true;
        this.mDuration = param2Int;
      }
      
      public void setDx(int param2Int) {
        this.mChanged = true;
        this.mDx = param2Int;
      }
      
      public void setDy(int param2Int) {
        this.mChanged = true;
        this.mDy = param2Int;
      }
      
      public void setInterpolator(Interpolator param2Interpolator) {
        this.mChanged = true;
        this.mInterpolator = param2Interpolator;
      }
      
      public void update(int param2Int1, int param2Int2, int param2Int3, Interpolator param2Interpolator) {
        this.mDx = param2Int1;
        this.mDy = param2Int2;
        this.mDuration = param2Int3;
        this.mInterpolator = param2Interpolator;
        this.mChanged = true;
      }
    }
    
    public static interface ScrollVectorProvider {
      PointF computeScrollVectorForPosition(int param2Int);
    }
  }
  
  public static class Action {
    public static final int UNDEFINED_DURATION = -2147483648;
    
    private boolean mChanged = false;
    
    private int mConsecutiveUpdates = 0;
    
    private int mDuration;
    
    private int mDx;
    
    private int mDy;
    
    private Interpolator mInterpolator;
    
    private int mJumpToPosition = -1;
    
    public Action(int param1Int1, int param1Int2) {
      this(param1Int1, param1Int2, -2147483648, null);
    }
    
    public Action(int param1Int1, int param1Int2, int param1Int3) {
      this(param1Int1, param1Int2, param1Int3, null);
    }
    
    public Action(int param1Int1, int param1Int2, int param1Int3, Interpolator param1Interpolator) {
      this.mDx = param1Int1;
      this.mDy = param1Int2;
      this.mDuration = param1Int3;
      this.mInterpolator = param1Interpolator;
    }
    
    private void validate() {
      if (this.mInterpolator == null || this.mDuration >= 1) {
        if (this.mDuration >= 1)
          return; 
        throw new IllegalStateException("Scroll duration must be a positive number");
      } 
      throw new IllegalStateException("If you provide an interpolator, you must set a positive duration");
    }
    
    public int getDuration() {
      return this.mDuration;
    }
    
    public int getDx() {
      return this.mDx;
    }
    
    public int getDy() {
      return this.mDy;
    }
    
    public Interpolator getInterpolator() {
      return this.mInterpolator;
    }
    
    boolean hasJumpTarget() {
      boolean bool;
      if (this.mJumpToPosition >= 0) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public void jumpTo(int param1Int) {
      this.mJumpToPosition = param1Int;
    }
    
    void runIfNecessary(RecyclerView param1RecyclerView) {
      if (this.mJumpToPosition >= 0) {
        int i = this.mJumpToPosition;
        this.mJumpToPosition = -1;
        param1RecyclerView.jumpToPositionForSmoothScroller(i);
        this.mChanged = false;
        return;
      } 
      if (this.mChanged) {
        validate();
        param1RecyclerView.mViewFlinger.smoothScrollBy(this.mDx, this.mDy, this.mDuration, this.mInterpolator);
        int i = this.mConsecutiveUpdates + 1;
        this.mConsecutiveUpdates = i;
        if (i > 10)
          Log.e("RecyclerView", "Smooth Scroll action is being updated too frequently. Make sure you are not changing it unless necessary"); 
        this.mChanged = false;
      } else {
        this.mConsecutiveUpdates = 0;
      } 
    }
    
    public void setDuration(int param1Int) {
      this.mChanged = true;
      this.mDuration = param1Int;
    }
    
    public void setDx(int param1Int) {
      this.mChanged = true;
      this.mDx = param1Int;
    }
    
    public void setDy(int param1Int) {
      this.mChanged = true;
      this.mDy = param1Int;
    }
    
    public void setInterpolator(Interpolator param1Interpolator) {
      this.mChanged = true;
      this.mInterpolator = param1Interpolator;
    }
    
    public void update(int param1Int1, int param1Int2, int param1Int3, Interpolator param1Interpolator) {
      this.mDx = param1Int1;
      this.mDy = param1Int2;
      this.mDuration = param1Int3;
      this.mInterpolator = param1Interpolator;
      this.mChanged = true;
    }
  }
  
  public static interface ScrollVectorProvider {
    PointF computeScrollVectorForPosition(int param1Int);
  }
  
  public static class State {
    static final int STEP_ANIMATIONS = 4;
    
    static final int STEP_LAYOUT = 2;
    
    static final int STEP_START = 1;
    
    private SparseArray<Object> mData;
    
    int mDeletedInvisibleItemCountSincePreviousLayout = 0;
    
    long mFocusedItemId;
    
    int mFocusedItemPosition;
    
    int mFocusedSubChildId;
    
    boolean mInPreLayout = false;
    
    boolean mIsMeasuring = false;
    
    int mItemCount = 0;
    
    int mLayoutStep = 1;
    
    int mPreviousLayoutItemCount = 0;
    
    int mRemainingScrollHorizontal;
    
    int mRemainingScrollVertical;
    
    boolean mRunPredictiveAnimations = false;
    
    boolean mRunSimpleAnimations = false;
    
    boolean mStructureChanged = false;
    
    int mTargetPosition = -1;
    
    boolean mTrackOldChangeHolders = false;
    
    void assertLayoutStep(int param1Int) {
      if ((this.mLayoutStep & param1Int) != 0)
        return; 
      throw new IllegalStateException("Layout state should be one of " + Integer.toBinaryString(param1Int) + " but it is " + Integer.toBinaryString(this.mLayoutStep));
    }
    
    public boolean didStructureChange() {
      return this.mStructureChanged;
    }
    
    public <T> T get(int param1Int) {
      SparseArray<Object> sparseArray = this.mData;
      return (T)((sparseArray == null) ? null : sparseArray.get(param1Int));
    }
    
    public int getItemCount() {
      int i;
      if (this.mInPreLayout) {
        i = this.mPreviousLayoutItemCount - this.mDeletedInvisibleItemCountSincePreviousLayout;
      } else {
        i = this.mItemCount;
      } 
      return i;
    }
    
    public int getRemainingScrollHorizontal() {
      return this.mRemainingScrollHorizontal;
    }
    
    public int getRemainingScrollVertical() {
      return this.mRemainingScrollVertical;
    }
    
    public int getTargetScrollPosition() {
      return this.mTargetPosition;
    }
    
    public boolean hasTargetScrollPosition() {
      boolean bool;
      if (this.mTargetPosition != -1) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public boolean isMeasuring() {
      return this.mIsMeasuring;
    }
    
    public boolean isPreLayout() {
      return this.mInPreLayout;
    }
    
    void prepareForNestedPrefetch(RecyclerView.Adapter param1Adapter) {
      this.mLayoutStep = 1;
      this.mItemCount = param1Adapter.getItemCount();
      this.mInPreLayout = false;
      this.mTrackOldChangeHolders = false;
      this.mIsMeasuring = false;
    }
    
    public void put(int param1Int, Object param1Object) {
      if (this.mData == null)
        this.mData = new SparseArray(); 
      this.mData.put(param1Int, param1Object);
    }
    
    public void remove(int param1Int) {
      SparseArray<Object> sparseArray = this.mData;
      if (sparseArray == null)
        return; 
      sparseArray.remove(param1Int);
    }
    
    public String toString() {
      return "State{mTargetPosition=" + this.mTargetPosition + ", mData=" + this.mData + ", mItemCount=" + this.mItemCount + ", mIsMeasuring=" + this.mIsMeasuring + ", mPreviousLayoutItemCount=" + this.mPreviousLayoutItemCount + ", mDeletedInvisibleItemCountSincePreviousLayout=" + this.mDeletedInvisibleItemCountSincePreviousLayout + ", mStructureChanged=" + this.mStructureChanged + ", mInPreLayout=" + this.mInPreLayout + ", mRunSimpleAnimations=" + this.mRunSimpleAnimations + ", mRunPredictiveAnimations=" + this.mRunPredictiveAnimations + '}';
    }
    
    public boolean willRunPredictiveAnimations() {
      return this.mRunPredictiveAnimations;
    }
    
    public boolean willRunSimpleAnimations() {
      return this.mRunSimpleAnimations;
    }
  }
  
  public static abstract class ViewCacheExtension {
    public abstract View getViewForPositionAndType(RecyclerView.Recycler param1Recycler, int param1Int1, int param1Int2);
  }
  
  class ViewFlinger implements Runnable {
    private boolean mEatRunOnAnimationRequest = false;
    
    Interpolator mInterpolator = RecyclerView.sQuinticInterpolator;
    
    private int mLastFlingX;
    
    private int mLastFlingY;
    
    OverScroller mOverScroller = new OverScroller(RecyclerView.this.getContext(), RecyclerView.sQuinticInterpolator);
    
    private boolean mReSchedulePostAnimationCallback = false;
    
    final RecyclerView this$0;
    
    private int computeScrollDuration(int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
      boolean bool;
      int j = Math.abs(param1Int1);
      int i = Math.abs(param1Int2);
      if (j > i) {
        bool = true;
      } else {
        bool = false;
      } 
      param1Int3 = (int)Math.sqrt((param1Int3 * param1Int3 + param1Int4 * param1Int4));
      param1Int2 = (int)Math.sqrt((param1Int1 * param1Int1 + param1Int2 * param1Int2));
      RecyclerView recyclerView = RecyclerView.this;
      if (bool) {
        param1Int1 = recyclerView.getWidth();
      } else {
        param1Int1 = recyclerView.getHeight();
      } 
      param1Int4 = param1Int1 / 2;
      float f3 = Math.min(1.0F, param1Int2 * 1.0F / param1Int1);
      float f2 = param1Int4;
      float f1 = param1Int4;
      f3 = distanceInfluenceForSnapDuration(f3);
      if (param1Int3 > 0) {
        param1Int1 = Math.round(Math.abs((f2 + f1 * f3) / param1Int3) * 1000.0F) * 4;
      } else {
        if (bool) {
          param1Int2 = j;
        } else {
          param1Int2 = i;
        } 
        param1Int1 = (int)((param1Int2 / param1Int1 + 1.0F) * 300.0F);
      } 
      return Math.min(param1Int1, 2000);
    }
    
    private float distanceInfluenceForSnapDuration(float param1Float) {
      return (float)Math.sin(((param1Float - 0.5F) * 0.47123894F));
    }
    
    private void internalPostOnAnimation() {
      RecyclerView.this.removeCallbacks(this);
      ViewCompat.postOnAnimation((View)RecyclerView.this, this);
    }
    
    public void fling(int param1Int1, int param1Int2) {
      RecyclerView.this.setScrollState(2);
      this.mLastFlingY = 0;
      this.mLastFlingX = 0;
      if (this.mInterpolator != RecyclerView.sQuinticInterpolator) {
        this.mInterpolator = RecyclerView.sQuinticInterpolator;
        this.mOverScroller = new OverScroller(RecyclerView.this.getContext(), RecyclerView.sQuinticInterpolator);
      } 
      this.mOverScroller.fling(0, 0, param1Int1, param1Int2, -2147483648, 2147483647, -2147483648, 2147483647);
      postOnAnimation();
    }
    
    void postOnAnimation() {
      if (this.mEatRunOnAnimationRequest) {
        this.mReSchedulePostAnimationCallback = true;
      } else {
        internalPostOnAnimation();
      } 
    }
    
    public void run() {
      if (RecyclerView.this.mLayout == null) {
        stop();
        return;
      } 
      this.mReSchedulePostAnimationCallback = false;
      this.mEatRunOnAnimationRequest = true;
      RecyclerView.this.consumePendingUpdateOperations();
      OverScroller overScroller = this.mOverScroller;
      if (overScroller.computeScrollOffset()) {
        int i = overScroller.getCurrX();
        int j = overScroller.getCurrY();
        int i1 = i - this.mLastFlingX;
        int n = j - this.mLastFlingY;
        this.mLastFlingX = i;
        this.mLastFlingY = j;
        int k = 0;
        int m = 0;
        RecyclerView.this.mReusableIntPair[0] = 0;
        RecyclerView.this.mReusableIntPair[1] = 0;
        RecyclerView recyclerView = RecyclerView.this;
        j = i1;
        i = n;
        if (recyclerView.dispatchNestedPreScroll(i1, n, recyclerView.mReusableIntPair, (int[])null, 1)) {
          j = i1 - RecyclerView.this.mReusableIntPair[0];
          i = n - RecyclerView.this.mReusableIntPair[1];
        } 
        if (RecyclerView.this.getOverScrollMode() != 2)
          RecyclerView.this.considerReleasingGlowsOnScroll(j, i); 
        i1 = j;
        n = i;
        if (RecyclerView.this.mAdapter != null) {
          RecyclerView.this.mReusableIntPair[0] = 0;
          RecyclerView.this.mReusableIntPair[1] = 0;
          recyclerView = RecyclerView.this;
          recyclerView.scrollStep(j, i, recyclerView.mReusableIntPair);
          int i2 = RecyclerView.this.mReusableIntPair[0];
          int i3 = RecyclerView.this.mReusableIntPair[1];
          j -= i2;
          i -= i3;
          RecyclerView.SmoothScroller smoothScroller2 = RecyclerView.this.mLayout.mSmoothScroller;
          i1 = j;
          n = i;
          k = i2;
          m = i3;
          if (smoothScroller2 != null) {
            i1 = j;
            n = i;
            k = i2;
            m = i3;
            if (!smoothScroller2.isPendingInitialRun()) {
              i1 = j;
              n = i;
              k = i2;
              m = i3;
              if (smoothScroller2.isRunning()) {
                k = RecyclerView.this.mState.getItemCount();
                if (k == 0) {
                  smoothScroller2.stop();
                  i1 = j;
                  n = i;
                  k = i2;
                  m = i3;
                } else if (smoothScroller2.getTargetPosition() >= k) {
                  smoothScroller2.setTargetPosition(k - 1);
                  smoothScroller2.onAnimation(i2, i3);
                  i1 = j;
                  n = i;
                  k = i2;
                  m = i3;
                } else {
                  smoothScroller2.onAnimation(i2, i3);
                  m = i3;
                  k = i2;
                  n = i;
                  i1 = j;
                } 
              } 
            } 
          } 
        } 
        if (!RecyclerView.this.mItemDecorations.isEmpty())
          RecyclerView.this.invalidate(); 
        RecyclerView.this.mReusableIntPair[0] = 0;
        RecyclerView.this.mReusableIntPair[1] = 0;
        recyclerView = RecyclerView.this;
        recyclerView.dispatchNestedScroll(k, m, i1, n, (int[])null, 1, recyclerView.mReusableIntPair);
        i1 -= RecyclerView.this.mReusableIntPair[0];
        n -= RecyclerView.this.mReusableIntPair[1];
        if (k != 0 || m != 0)
          RecyclerView.this.dispatchOnScrolled(k, m); 
        if (!RecyclerView.this.awakenScrollBars())
          RecyclerView.this.invalidate(); 
        if (overScroller.getCurrX() == overScroller.getFinalX()) {
          i = 1;
        } else {
          i = 0;
        } 
        if (overScroller.getCurrY() == overScroller.getFinalY()) {
          j = 1;
        } else {
          j = 0;
        } 
        if (overScroller.isFinished() || ((i != 0 || i1 != 0) && (j != 0 || n != 0))) {
          i = 1;
        } else {
          i = 0;
        } 
        RecyclerView.SmoothScroller smoothScroller1 = RecyclerView.this.mLayout.mSmoothScroller;
        if (smoothScroller1 != null && smoothScroller1.isPendingInitialRun()) {
          j = 1;
        } else {
          j = 0;
        } 
        if (j == 0 && i != 0) {
          if (RecyclerView.this.getOverScrollMode() != 2) {
            j = (int)overScroller.getCurrVelocity();
            if (i1 < 0) {
              i = -j;
            } else if (i1 > 0) {
              i = j;
            } else {
              i = 0;
            } 
            if (n < 0) {
              j = -j;
            } else if (n <= 0) {
              j = 0;
            } 
            RecyclerView.this.absorbGlows(i, j);
          } 
          if (RecyclerView.ALLOW_THREAD_GAP_WORK)
            RecyclerView.this.mPrefetchRegistry.clearPrefetchPositions(); 
        } else {
          postOnAnimation();
          if (RecyclerView.this.mGapWorker != null)
            RecyclerView.this.mGapWorker.postFromTraversal(RecyclerView.this, k, m); 
        } 
      } 
      RecyclerView.SmoothScroller smoothScroller = RecyclerView.this.mLayout.mSmoothScroller;
      if (smoothScroller != null && smoothScroller.isPendingInitialRun())
        smoothScroller.onAnimation(0, 0); 
      this.mEatRunOnAnimationRequest = false;
      if (this.mReSchedulePostAnimationCallback) {
        internalPostOnAnimation();
      } else {
        RecyclerView.this.setScrollState(0);
        RecyclerView.this.stopNestedScroll(1);
      } 
    }
    
    public void smoothScrollBy(int param1Int1, int param1Int2, int param1Int3, Interpolator param1Interpolator) {
      int i = param1Int3;
      if (param1Int3 == Integer.MIN_VALUE)
        i = computeScrollDuration(param1Int1, param1Int2, 0, 0); 
      Interpolator interpolator = param1Interpolator;
      if (param1Interpolator == null)
        interpolator = RecyclerView.sQuinticInterpolator; 
      if (this.mInterpolator != interpolator) {
        this.mInterpolator = interpolator;
        this.mOverScroller = new OverScroller(RecyclerView.this.getContext(), interpolator);
      } 
      this.mLastFlingY = 0;
      this.mLastFlingX = 0;
      RecyclerView.this.setScrollState(2);
      this.mOverScroller.startScroll(0, 0, param1Int1, param1Int2, i);
      if (Build.VERSION.SDK_INT < 23)
        this.mOverScroller.computeScrollOffset(); 
      postOnAnimation();
    }
    
    public void stop() {
      RecyclerView.this.removeCallbacks(this);
      this.mOverScroller.abortAnimation();
    }
  }
  
  public static abstract class ViewHolder {
    static final int FLAG_ADAPTER_FULLUPDATE = 1024;
    
    static final int FLAG_ADAPTER_POSITION_UNKNOWN = 512;
    
    static final int FLAG_APPEARED_IN_PRE_LAYOUT = 4096;
    
    static final int FLAG_BOUNCED_FROM_HIDDEN_LIST = 8192;
    
    static final int FLAG_BOUND = 1;
    
    static final int FLAG_IGNORE = 128;
    
    static final int FLAG_INVALID = 4;
    
    static final int FLAG_MOVED = 2048;
    
    static final int FLAG_NOT_RECYCLABLE = 16;
    
    static final int FLAG_REMOVED = 8;
    
    static final int FLAG_RETURNED_FROM_SCRAP = 32;
    
    static final int FLAG_TMP_DETACHED = 256;
    
    static final int FLAG_UPDATE = 2;
    
    private static final List<Object> FULLUPDATE_PAYLOADS = Collections.emptyList();
    
    static final int PENDING_ACCESSIBILITY_STATE_NOT_SET = -1;
    
    public final View itemView;
    
    int mFlags;
    
    boolean mInChangeScrap = false;
    
    private int mIsRecyclableCount = 0;
    
    long mItemId = -1L;
    
    int mItemViewType = -1;
    
    WeakReference<RecyclerView> mNestedRecyclerView;
    
    int mOldPosition = -1;
    
    RecyclerView mOwnerRecyclerView;
    
    List<Object> mPayloads = null;
    
    int mPendingAccessibilityState = -1;
    
    int mPosition = -1;
    
    int mPreLayoutPosition = -1;
    
    RecyclerView.Recycler mScrapContainer = null;
    
    ViewHolder mShadowedHolder = null;
    
    ViewHolder mShadowingHolder = null;
    
    List<Object> mUnmodifiedPayloads = null;
    
    private int mWasImportantForAccessibilityBeforeHidden = 0;
    
    public ViewHolder(View param1View) {
      if (param1View != null) {
        this.itemView = param1View;
        return;
      } 
      throw new IllegalArgumentException("itemView may not be null");
    }
    
    private void createPayloadsIfNeeded() {
      if (this.mPayloads == null) {
        ArrayList<Object> arrayList = new ArrayList();
        this.mPayloads = arrayList;
        this.mUnmodifiedPayloads = Collections.unmodifiableList(arrayList);
      } 
    }
    
    void addChangePayload(Object param1Object) {
      if (param1Object == null) {
        addFlags(1024);
      } else if ((0x400 & this.mFlags) == 0) {
        createPayloadsIfNeeded();
        this.mPayloads.add(param1Object);
      } 
    }
    
    void addFlags(int param1Int) {
      this.mFlags |= param1Int;
    }
    
    void clearOldPosition() {
      this.mOldPosition = -1;
      this.mPreLayoutPosition = -1;
    }
    
    void clearPayload() {
      List<Object> list = this.mPayloads;
      if (list != null)
        list.clear(); 
      this.mFlags &= 0xFFFFFBFF;
    }
    
    void clearReturnedFromScrapFlag() {
      this.mFlags &= 0xFFFFFFDF;
    }
    
    void clearTmpDetachFlag() {
      this.mFlags &= 0xFFFFFEFF;
    }
    
    boolean doesTransientStatePreventRecycling() {
      boolean bool;
      if ((this.mFlags & 0x10) == 0 && ViewCompat.hasTransientState(this.itemView)) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    void flagRemovedAndOffsetPosition(int param1Int1, int param1Int2, boolean param1Boolean) {
      addFlags(8);
      offsetPosition(param1Int2, param1Boolean);
      this.mPosition = param1Int1;
    }
    
    public final int getAdapterPosition() {
      RecyclerView recyclerView = this.mOwnerRecyclerView;
      return (recyclerView == null) ? -1 : recyclerView.getAdapterPositionFor(this);
    }
    
    public final long getItemId() {
      return this.mItemId;
    }
    
    public final int getItemViewType() {
      return this.mItemViewType;
    }
    
    public final int getLayoutPosition() {
      int j = this.mPreLayoutPosition;
      int i = j;
      if (j == -1)
        i = this.mPosition; 
      return i;
    }
    
    public final int getOldPosition() {
      return this.mOldPosition;
    }
    
    @Deprecated
    public final int getPosition() {
      int j = this.mPreLayoutPosition;
      int i = j;
      if (j == -1)
        i = this.mPosition; 
      return i;
    }
    
    List<Object> getUnmodifiedPayloads() {
      if ((this.mFlags & 0x400) == 0) {
        List<Object> list = this.mPayloads;
        return (list == null || list.size() == 0) ? FULLUPDATE_PAYLOADS : this.mUnmodifiedPayloads;
      } 
      return FULLUPDATE_PAYLOADS;
    }
    
    boolean hasAnyOfTheFlags(int param1Int) {
      boolean bool;
      if ((this.mFlags & param1Int) != 0) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    boolean isAdapterPositionUnknown() {
      return ((this.mFlags & 0x200) != 0 || isInvalid());
    }
    
    boolean isAttachedToTransitionOverlay() {
      boolean bool;
      if (this.itemView.getParent() != null && this.itemView.getParent() != this.mOwnerRecyclerView) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    boolean isBound() {
      int i = this.mFlags;
      boolean bool = true;
      if ((i & 0x1) == 0)
        bool = false; 
      return bool;
    }
    
    boolean isInvalid() {
      boolean bool;
      if ((this.mFlags & 0x4) != 0) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public final boolean isRecyclable() {
      boolean bool;
      if ((this.mFlags & 0x10) == 0 && !ViewCompat.hasTransientState(this.itemView)) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    boolean isRemoved() {
      boolean bool;
      if ((this.mFlags & 0x8) != 0) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    boolean isScrap() {
      boolean bool;
      if (this.mScrapContainer != null) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    boolean isTmpDetached() {
      boolean bool;
      if ((this.mFlags & 0x100) != 0) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    boolean isUpdated() {
      boolean bool;
      if ((this.mFlags & 0x2) != 0) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    boolean needsUpdate() {
      boolean bool;
      if ((this.mFlags & 0x2) != 0) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    void offsetPosition(int param1Int, boolean param1Boolean) {
      if (this.mOldPosition == -1)
        this.mOldPosition = this.mPosition; 
      if (this.mPreLayoutPosition == -1)
        this.mPreLayoutPosition = this.mPosition; 
      if (param1Boolean)
        this.mPreLayoutPosition += param1Int; 
      this.mPosition += param1Int;
      if (this.itemView.getLayoutParams() != null)
        ((RecyclerView.LayoutParams)this.itemView.getLayoutParams()).mInsetsDirty = true; 
    }
    
    void onEnteredHiddenState(RecyclerView param1RecyclerView) {
      int i = this.mPendingAccessibilityState;
      if (i != -1) {
        this.mWasImportantForAccessibilityBeforeHidden = i;
      } else {
        this.mWasImportantForAccessibilityBeforeHidden = ViewCompat.getImportantForAccessibility(this.itemView);
      } 
      param1RecyclerView.setChildImportantForAccessibilityInternal(this, 4);
    }
    
    void onLeftHiddenState(RecyclerView param1RecyclerView) {
      param1RecyclerView.setChildImportantForAccessibilityInternal(this, this.mWasImportantForAccessibilityBeforeHidden);
      this.mWasImportantForAccessibilityBeforeHidden = 0;
    }
    
    void resetInternal() {
      this.mFlags = 0;
      this.mPosition = -1;
      this.mOldPosition = -1;
      this.mItemId = -1L;
      this.mPreLayoutPosition = -1;
      this.mIsRecyclableCount = 0;
      this.mShadowedHolder = null;
      this.mShadowingHolder = null;
      clearPayload();
      this.mWasImportantForAccessibilityBeforeHidden = 0;
      this.mPendingAccessibilityState = -1;
      RecyclerView.clearNestedRecyclerViewIfNotNested(this);
    }
    
    void saveOldPosition() {
      if (this.mOldPosition == -1)
        this.mOldPosition = this.mPosition; 
    }
    
    void setFlags(int param1Int1, int param1Int2) {
      this.mFlags = this.mFlags & (param1Int2 ^ 0xFFFFFFFF) | param1Int1 & param1Int2;
    }
    
    public final void setIsRecyclable(boolean param1Boolean) {
      int i = this.mIsRecyclableCount;
      if (param1Boolean) {
        i--;
      } else {
        i++;
      } 
      this.mIsRecyclableCount = i;
      if (i < 0) {
        this.mIsRecyclableCount = 0;
        Log.e("View", "isRecyclable decremented below 0: unmatched pair of setIsRecyable() calls for " + this);
      } else if (!param1Boolean && i == 1) {
        this.mFlags |= 0x10;
      } else if (param1Boolean && i == 0) {
        this.mFlags &= 0xFFFFFFEF;
      } 
    }
    
    void setScrapContainer(RecyclerView.Recycler param1Recycler, boolean param1Boolean) {
      this.mScrapContainer = param1Recycler;
      this.mInChangeScrap = param1Boolean;
    }
    
    boolean shouldBeKeptAsChild() {
      boolean bool;
      if ((this.mFlags & 0x10) != 0) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    boolean shouldIgnore() {
      boolean bool;
      if ((this.mFlags & 0x80) != 0) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    void stopIgnoring() {
      this.mFlags &= 0xFFFFFF7F;
    }
    
    public String toString() {
      String str;
      if (getClass().isAnonymousClass()) {
        str = "ViewHolder";
      } else {
        str = getClass().getSimpleName();
      } 
      StringBuilder stringBuilder = new StringBuilder(str + "{" + Integer.toHexString(hashCode()) + " position=" + this.mPosition + " id=" + this.mItemId + ", oldPos=" + this.mOldPosition + ", pLpos:" + this.mPreLayoutPosition);
      if (isScrap()) {
        StringBuilder stringBuilder1 = stringBuilder.append(" scrap ");
        if (this.mInChangeScrap) {
          str = "[changeScrap]";
        } else {
          str = "[attachedScrap]";
        } 
        stringBuilder1.append(str);
      } 
      if (isInvalid())
        stringBuilder.append(" invalid"); 
      if (!isBound())
        stringBuilder.append(" unbound"); 
      if (needsUpdate())
        stringBuilder.append(" update"); 
      if (isRemoved())
        stringBuilder.append(" removed"); 
      if (shouldIgnore())
        stringBuilder.append(" ignored"); 
      if (isTmpDetached())
        stringBuilder.append(" tmpDetached"); 
      if (!isRecyclable())
        stringBuilder.append(" not recyclable(" + this.mIsRecyclableCount + ")"); 
      if (isAdapterPositionUnknown())
        stringBuilder.append(" undefined adapter position"); 
      if (this.itemView.getParent() == null)
        stringBuilder.append(" no parent"); 
      stringBuilder.append("}");
      return stringBuilder.toString();
    }
    
    void unScrap() {
      this.mScrapContainer.unscrapView(this);
    }
    
    boolean wasReturnedFromScrap() {
      boolean bool;
      if ((this.mFlags & 0x20) != 0) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\recyclerview\widget\RecyclerView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */