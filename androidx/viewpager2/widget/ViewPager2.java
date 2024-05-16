package androidx.viewpager2.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.view.accessibility.AccessibilityViewCommand;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.R;
import androidx.viewpager2.adapter.StatefulAdapter;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class ViewPager2 extends ViewGroup {
  public static final int OFFSCREEN_PAGE_LIMIT_DEFAULT = -1;
  
  public static final int ORIENTATION_HORIZONTAL = 0;
  
  public static final int ORIENTATION_VERTICAL = 1;
  
  public static final int SCROLL_STATE_DRAGGING = 1;
  
  public static final int SCROLL_STATE_IDLE = 0;
  
  public static final int SCROLL_STATE_SETTLING = 2;
  
  static boolean sFeatureEnhancedA11yEnabled = true;
  
  AccessibilityProvider mAccessibilityProvider;
  
  int mCurrentItem;
  
  private RecyclerView.AdapterDataObserver mCurrentItemDataSetChangeObserver = new DataSetChangeObserver() {
      final ViewPager2 this$0;
      
      public void onChanged() {
        ViewPager2.this.mCurrentItemDirty = true;
        ViewPager2.this.mScrollEventAdapter.notifyDataSetChangeHappened();
      }
    };
  
  boolean mCurrentItemDirty = false;
  
  private CompositeOnPageChangeCallback mExternalPageChangeCallbacks = new CompositeOnPageChangeCallback(3);
  
  private FakeDrag mFakeDragger;
  
  private LinearLayoutManager mLayoutManager;
  
  private int mOffscreenPageLimit = -1;
  
  private CompositeOnPageChangeCallback mPageChangeEventDispatcher;
  
  private PageTransformerAdapter mPageTransformerAdapter;
  
  private PagerSnapHelper mPagerSnapHelper;
  
  private Parcelable mPendingAdapterState;
  
  private int mPendingCurrentItem = -1;
  
  RecyclerView mRecyclerView;
  
  private RecyclerView.ItemAnimator mSavedItemAnimator = null;
  
  private boolean mSavedItemAnimatorPresent = false;
  
  ScrollEventAdapter mScrollEventAdapter;
  
  private final Rect mTmpChildRect = new Rect();
  
  private final Rect mTmpContainerRect = new Rect();
  
  private boolean mUserInputEnabled = true;
  
  public ViewPager2(Context paramContext) {
    super(paramContext);
    initialize(paramContext, (AttributeSet)null);
  }
  
  public ViewPager2(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    initialize(paramContext, paramAttributeSet);
  }
  
  public ViewPager2(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    initialize(paramContext, paramAttributeSet);
  }
  
  public ViewPager2(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2) {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    initialize(paramContext, paramAttributeSet);
  }
  
  private RecyclerView.OnChildAttachStateChangeListener enforceChildFillListener() {
    return new RecyclerView.OnChildAttachStateChangeListener() {
        final ViewPager2 this$0;
        
        public void onChildViewAttachedToWindow(View param1View) {
          RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)param1View.getLayoutParams();
          if (layoutParams.width == -1 && layoutParams.height == -1)
            return; 
          throw new IllegalStateException("Pages must fill the whole ViewPager2 (use match_parent)");
        }
        
        public void onChildViewDetachedFromWindow(View param1View) {}
      };
  }
  
  private void initialize(Context paramContext, AttributeSet paramAttributeSet) {
    BasicAccessibilityProvider basicAccessibilityProvider;
    if (sFeatureEnhancedA11yEnabled) {
      PageAwareAccessibilityProvider pageAwareAccessibilityProvider = new PageAwareAccessibilityProvider();
    } else {
      basicAccessibilityProvider = new BasicAccessibilityProvider();
    } 
    this.mAccessibilityProvider = basicAccessibilityProvider;
    RecyclerViewImpl recyclerViewImpl = new RecyclerViewImpl(paramContext);
    this.mRecyclerView = recyclerViewImpl;
    recyclerViewImpl.setId(ViewCompat.generateViewId());
    this.mRecyclerView.setDescendantFocusability(131072);
    LinearLayoutManagerImpl linearLayoutManagerImpl = new LinearLayoutManagerImpl(paramContext);
    this.mLayoutManager = linearLayoutManagerImpl;
    this.mRecyclerView.setLayoutManager((RecyclerView.LayoutManager)linearLayoutManagerImpl);
    this.mRecyclerView.setScrollingTouchSlop(1);
    setOrientation(paramContext, paramAttributeSet);
    this.mRecyclerView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
    this.mRecyclerView.addOnChildAttachStateChangeListener(enforceChildFillListener());
    this.mScrollEventAdapter = new ScrollEventAdapter(this);
    this.mFakeDragger = new FakeDrag(this, this.mScrollEventAdapter, this.mRecyclerView);
    PagerSnapHelperImpl pagerSnapHelperImpl = new PagerSnapHelperImpl();
    this.mPagerSnapHelper = pagerSnapHelperImpl;
    pagerSnapHelperImpl.attachToRecyclerView(this.mRecyclerView);
    this.mRecyclerView.addOnScrollListener(this.mScrollEventAdapter);
    CompositeOnPageChangeCallback compositeOnPageChangeCallback = new CompositeOnPageChangeCallback(3);
    this.mPageChangeEventDispatcher = compositeOnPageChangeCallback;
    this.mScrollEventAdapter.setOnPageChangeCallback(compositeOnPageChangeCallback);
    OnPageChangeCallback onPageChangeCallback1 = new OnPageChangeCallback() {
        final ViewPager2 this$0;
        
        public void onPageScrollStateChanged(int param1Int) {
          if (param1Int == 0)
            ViewPager2.this.updateCurrentItem(); 
        }
        
        public void onPageSelected(int param1Int) {
          if (ViewPager2.this.mCurrentItem != param1Int) {
            ViewPager2.this.mCurrentItem = param1Int;
            ViewPager2.this.mAccessibilityProvider.onSetNewCurrentItem();
          } 
        }
      };
    OnPageChangeCallback onPageChangeCallback2 = new OnPageChangeCallback() {
        final ViewPager2 this$0;
        
        public void onPageSelected(int param1Int) {
          ViewPager2.this.clearFocus();
          if (ViewPager2.this.hasFocus())
            ViewPager2.this.mRecyclerView.requestFocus(2); 
        }
      };
    this.mPageChangeEventDispatcher.addOnPageChangeCallback(onPageChangeCallback1);
    this.mPageChangeEventDispatcher.addOnPageChangeCallback(onPageChangeCallback2);
    this.mAccessibilityProvider.onInitialize(this.mPageChangeEventDispatcher, this.mRecyclerView);
    this.mPageChangeEventDispatcher.addOnPageChangeCallback(this.mExternalPageChangeCallbacks);
    onPageChangeCallback1 = new PageTransformerAdapter(this.mLayoutManager);
    this.mPageTransformerAdapter = (PageTransformerAdapter)onPageChangeCallback1;
    this.mPageChangeEventDispatcher.addOnPageChangeCallback(onPageChangeCallback1);
    RecyclerView recyclerView = this.mRecyclerView;
    attachViewToParent((View)recyclerView, 0, recyclerView.getLayoutParams());
  }
  
  private void registerCurrentItemDataSetTracker(RecyclerView.Adapter<?> paramAdapter) {
    if (paramAdapter != null)
      paramAdapter.registerAdapterDataObserver(this.mCurrentItemDataSetChangeObserver); 
  }
  
  private void restorePendingState() {
    if (this.mPendingCurrentItem == -1)
      return; 
    RecyclerView.Adapter adapter = getAdapter();
    if (adapter == null)
      return; 
    Parcelable parcelable = this.mPendingAdapterState;
    if (parcelable != null) {
      if (adapter instanceof StatefulAdapter)
        ((StatefulAdapter)adapter).restoreState(parcelable); 
      this.mPendingAdapterState = null;
    } 
    int i = Math.max(0, Math.min(this.mPendingCurrentItem, adapter.getItemCount() - 1));
    this.mCurrentItem = i;
    this.mPendingCurrentItem = -1;
    this.mRecyclerView.scrollToPosition(i);
    this.mAccessibilityProvider.onRestorePendingState();
  }
  
  private void setOrientation(Context paramContext, AttributeSet paramAttributeSet) {
    TypedArray typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.ViewPager2);
    if (Build.VERSION.SDK_INT >= 29)
      saveAttributeDataForStyleable(paramContext, R.styleable.ViewPager2, paramAttributeSet, typedArray, 0, 0); 
    try {
      setOrientation(typedArray.getInt(R.styleable.ViewPager2_android_orientation, 0));
      return;
    } finally {
      typedArray.recycle();
    } 
  }
  
  private void unregisterCurrentItemDataSetTracker(RecyclerView.Adapter<?> paramAdapter) {
    if (paramAdapter != null)
      paramAdapter.unregisterAdapterDataObserver(this.mCurrentItemDataSetChangeObserver); 
  }
  
  public void addItemDecoration(RecyclerView.ItemDecoration paramItemDecoration) {
    this.mRecyclerView.addItemDecoration(paramItemDecoration);
  }
  
  public void addItemDecoration(RecyclerView.ItemDecoration paramItemDecoration, int paramInt) {
    this.mRecyclerView.addItemDecoration(paramItemDecoration, paramInt);
  }
  
  public boolean beginFakeDrag() {
    return this.mFakeDragger.beginFakeDrag();
  }
  
  public boolean canScrollHorizontally(int paramInt) {
    return this.mRecyclerView.canScrollHorizontally(paramInt);
  }
  
  public boolean canScrollVertically(int paramInt) {
    return this.mRecyclerView.canScrollVertically(paramInt);
  }
  
  protected void dispatchRestoreInstanceState(SparseArray<Parcelable> paramSparseArray) {
    Parcelable parcelable = (Parcelable)paramSparseArray.get(getId());
    if (parcelable instanceof SavedState) {
      int i = ((SavedState)parcelable).mRecyclerViewId;
      paramSparseArray.put(this.mRecyclerView.getId(), paramSparseArray.get(i));
      paramSparseArray.remove(i);
    } 
    super.dispatchRestoreInstanceState(paramSparseArray);
    restorePendingState();
  }
  
  public boolean endFakeDrag() {
    return this.mFakeDragger.endFakeDrag();
  }
  
  public boolean fakeDragBy(float paramFloat) {
    return this.mFakeDragger.fakeDragBy(paramFloat);
  }
  
  public CharSequence getAccessibilityClassName() {
    return this.mAccessibilityProvider.handlesGetAccessibilityClassName() ? this.mAccessibilityProvider.onGetAccessibilityClassName() : super.getAccessibilityClassName();
  }
  
  public RecyclerView.Adapter getAdapter() {
    return this.mRecyclerView.getAdapter();
  }
  
  public int getCurrentItem() {
    return this.mCurrentItem;
  }
  
  public RecyclerView.ItemDecoration getItemDecorationAt(int paramInt) {
    return this.mRecyclerView.getItemDecorationAt(paramInt);
  }
  
  public int getItemDecorationCount() {
    return this.mRecyclerView.getItemDecorationCount();
  }
  
  public int getOffscreenPageLimit() {
    return this.mOffscreenPageLimit;
  }
  
  public int getOrientation() {
    return this.mLayoutManager.getOrientation();
  }
  
  int getPageSize() {
    int i;
    RecyclerView recyclerView = this.mRecyclerView;
    if (getOrientation() == 0) {
      i = recyclerView.getWidth() - recyclerView.getPaddingLeft() - recyclerView.getPaddingRight();
    } else {
      i = recyclerView.getHeight() - recyclerView.getPaddingTop() - recyclerView.getPaddingBottom();
    } 
    return i;
  }
  
  public int getScrollState() {
    return this.mScrollEventAdapter.getScrollState();
  }
  
  public void invalidateItemDecorations() {
    this.mRecyclerView.invalidateItemDecorations();
  }
  
  public boolean isFakeDragging() {
    return this.mFakeDragger.isFakeDragging();
  }
  
  boolean isRtl() {
    int i = this.mLayoutManager.getLayoutDirection();
    boolean bool = true;
    if (i != 1)
      bool = false; 
    return bool;
  }
  
  public boolean isUserInputEnabled() {
    return this.mUserInputEnabled;
  }
  
  public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo paramAccessibilityNodeInfo) {
    super.onInitializeAccessibilityNodeInfo(paramAccessibilityNodeInfo);
    this.mAccessibilityProvider.onInitializeAccessibilityNodeInfo(paramAccessibilityNodeInfo);
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    int i = this.mRecyclerView.getMeasuredWidth();
    int j = this.mRecyclerView.getMeasuredHeight();
    this.mTmpContainerRect.left = getPaddingLeft();
    this.mTmpContainerRect.right = paramInt3 - paramInt1 - getPaddingRight();
    this.mTmpContainerRect.top = getPaddingTop();
    this.mTmpContainerRect.bottom = paramInt4 - paramInt2 - getPaddingBottom();
    Gravity.apply(8388659, i, j, this.mTmpContainerRect, this.mTmpChildRect);
    this.mRecyclerView.layout(this.mTmpChildRect.left, this.mTmpChildRect.top, this.mTmpChildRect.right, this.mTmpChildRect.bottom);
    if (this.mCurrentItemDirty)
      updateCurrentItem(); 
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    measureChild((View)this.mRecyclerView, paramInt1, paramInt2);
    int n = this.mRecyclerView.getMeasuredWidth();
    int j = this.mRecyclerView.getMeasuredHeight();
    int i = this.mRecyclerView.getMeasuredState();
    int i1 = getPaddingLeft();
    int i2 = getPaddingRight();
    int m = getPaddingTop();
    int k = getPaddingBottom();
    n = Math.max(n + i1 + i2, getSuggestedMinimumWidth());
    j = Math.max(j + m + k, getSuggestedMinimumHeight());
    setMeasuredDimension(resolveSizeAndState(n, paramInt1, i), resolveSizeAndState(j, paramInt2, i << 16));
  }
  
  protected void onRestoreInstanceState(Parcelable paramParcelable) {
    if (!(paramParcelable instanceof SavedState)) {
      super.onRestoreInstanceState(paramParcelable);
      return;
    } 
    SavedState savedState = (SavedState)paramParcelable;
    super.onRestoreInstanceState(savedState.getSuperState());
    this.mPendingCurrentItem = savedState.mCurrentItem;
    this.mPendingAdapterState = savedState.mAdapterState;
  }
  
  protected Parcelable onSaveInstanceState() {
    SavedState savedState = new SavedState(super.onSaveInstanceState());
    savedState.mRecyclerViewId = this.mRecyclerView.getId();
    int j = this.mPendingCurrentItem;
    int i = j;
    if (j == -1)
      i = this.mCurrentItem; 
    savedState.mCurrentItem = i;
    Parcelable parcelable = this.mPendingAdapterState;
    if (parcelable != null) {
      savedState.mAdapterState = parcelable;
    } else {
      RecyclerView.Adapter adapter = this.mRecyclerView.getAdapter();
      if (adapter instanceof StatefulAdapter)
        savedState.mAdapterState = ((StatefulAdapter)adapter).saveState(); 
    } 
    return (Parcelable)savedState;
  }
  
  public void onViewAdded(View paramView) {
    throw new IllegalStateException(getClass().getSimpleName() + " does not support direct child views");
  }
  
  public boolean performAccessibilityAction(int paramInt, Bundle paramBundle) {
    return this.mAccessibilityProvider.handlesPerformAccessibilityAction(paramInt, paramBundle) ? this.mAccessibilityProvider.onPerformAccessibilityAction(paramInt, paramBundle) : super.performAccessibilityAction(paramInt, paramBundle);
  }
  
  public void registerOnPageChangeCallback(OnPageChangeCallback paramOnPageChangeCallback) {
    this.mExternalPageChangeCallbacks.addOnPageChangeCallback(paramOnPageChangeCallback);
  }
  
  public void removeItemDecoration(RecyclerView.ItemDecoration paramItemDecoration) {
    this.mRecyclerView.removeItemDecoration(paramItemDecoration);
  }
  
  public void removeItemDecorationAt(int paramInt) {
    this.mRecyclerView.removeItemDecorationAt(paramInt);
  }
  
  public void requestTransform() {
    if (this.mPageTransformerAdapter.getPageTransformer() == null)
      return; 
    double d = this.mScrollEventAdapter.getRelativeScrollPosition();
    int j = (int)d;
    float f = (float)(d - j);
    int i = Math.round(getPageSize() * f);
    this.mPageTransformerAdapter.onPageScrolled(j, f, i);
  }
  
  public void setAdapter(RecyclerView.Adapter<?> paramAdapter) {
    RecyclerView.Adapter<?> adapter = this.mRecyclerView.getAdapter();
    this.mAccessibilityProvider.onDetachAdapter(adapter);
    unregisterCurrentItemDataSetTracker(adapter);
    this.mRecyclerView.setAdapter(paramAdapter);
    this.mCurrentItem = 0;
    restorePendingState();
    this.mAccessibilityProvider.onAttachAdapter(paramAdapter);
    registerCurrentItemDataSetTracker(paramAdapter);
  }
  
  public void setCurrentItem(int paramInt) {
    setCurrentItem(paramInt, true);
  }
  
  public void setCurrentItem(int paramInt, boolean paramBoolean) {
    if (!isFakeDragging()) {
      setCurrentItemInternal(paramInt, paramBoolean);
      return;
    } 
    throw new IllegalStateException("Cannot change current item when ViewPager2 is fake dragging");
  }
  
  void setCurrentItemInternal(int paramInt, boolean paramBoolean) {
    RecyclerView.Adapter adapter = getAdapter();
    if (adapter == null) {
      if (this.mPendingCurrentItem != -1)
        this.mPendingCurrentItem = Math.max(paramInt, 0); 
      return;
    } 
    if (adapter.getItemCount() <= 0)
      return; 
    int i = Math.min(Math.max(paramInt, 0), adapter.getItemCount() - 1);
    if (i == this.mCurrentItem && this.mScrollEventAdapter.isIdle())
      return; 
    paramInt = this.mCurrentItem;
    if (i == paramInt && paramBoolean)
      return; 
    double d = paramInt;
    this.mCurrentItem = i;
    this.mAccessibilityProvider.onSetNewCurrentItem();
    if (!this.mScrollEventAdapter.isIdle())
      d = this.mScrollEventAdapter.getRelativeScrollPosition(); 
    this.mScrollEventAdapter.notifyProgrammaticScroll(i, paramBoolean);
    if (!paramBoolean) {
      this.mRecyclerView.scrollToPosition(i);
      return;
    } 
    if (Math.abs(i - d) > 3.0D) {
      RecyclerView recyclerView = this.mRecyclerView;
      if (i > d) {
        paramInt = i - 3;
      } else {
        paramInt = i + 3;
      } 
      recyclerView.scrollToPosition(paramInt);
      this.mRecyclerView.post(new SmoothScrollToPosition(i, this.mRecyclerView));
    } else {
      this.mRecyclerView.smoothScrollToPosition(i);
    } 
  }
  
  public void setLayoutDirection(int paramInt) {
    super.setLayoutDirection(paramInt);
    this.mAccessibilityProvider.onSetLayoutDirection();
  }
  
  public void setOffscreenPageLimit(int paramInt) {
    if (paramInt >= 1 || paramInt == -1) {
      this.mOffscreenPageLimit = paramInt;
      this.mRecyclerView.requestLayout();
      return;
    } 
    throw new IllegalArgumentException("Offscreen page limit must be OFFSCREEN_PAGE_LIMIT_DEFAULT or a number > 0");
  }
  
  public void setOrientation(int paramInt) {
    this.mLayoutManager.setOrientation(paramInt);
    this.mAccessibilityProvider.onSetOrientation();
  }
  
  public void setPageTransformer(PageTransformer paramPageTransformer) {
    if (paramPageTransformer != null) {
      if (!this.mSavedItemAnimatorPresent) {
        this.mSavedItemAnimator = this.mRecyclerView.getItemAnimator();
        this.mSavedItemAnimatorPresent = true;
      } 
      this.mRecyclerView.setItemAnimator(null);
    } else if (this.mSavedItemAnimatorPresent) {
      this.mRecyclerView.setItemAnimator(this.mSavedItemAnimator);
      this.mSavedItemAnimator = null;
      this.mSavedItemAnimatorPresent = false;
    } 
    if (paramPageTransformer == this.mPageTransformerAdapter.getPageTransformer())
      return; 
    this.mPageTransformerAdapter.setPageTransformer(paramPageTransformer);
    requestTransform();
  }
  
  public void setUserInputEnabled(boolean paramBoolean) {
    this.mUserInputEnabled = paramBoolean;
    this.mAccessibilityProvider.onSetUserInputEnabled();
  }
  
  void snapToPage() {
    View view = this.mPagerSnapHelper.findSnapView((RecyclerView.LayoutManager)this.mLayoutManager);
    if (view == null)
      return; 
    int[] arrayOfInt = this.mPagerSnapHelper.calculateDistanceToFinalSnap((RecyclerView.LayoutManager)this.mLayoutManager, view);
    if (arrayOfInt[0] != 0 || arrayOfInt[1] != 0)
      this.mRecyclerView.smoothScrollBy(arrayOfInt[0], arrayOfInt[1]); 
  }
  
  public void unregisterOnPageChangeCallback(OnPageChangeCallback paramOnPageChangeCallback) {
    this.mExternalPageChangeCallbacks.removeOnPageChangeCallback(paramOnPageChangeCallback);
  }
  
  void updateCurrentItem() {
    PagerSnapHelper pagerSnapHelper = this.mPagerSnapHelper;
    if (pagerSnapHelper != null) {
      View view = pagerSnapHelper.findSnapView((RecyclerView.LayoutManager)this.mLayoutManager);
      if (view == null)
        return; 
      int i = this.mLayoutManager.getPosition(view);
      if (i != this.mCurrentItem && getScrollState() == 0)
        this.mPageChangeEventDispatcher.onPageSelected(i); 
      this.mCurrentItemDirty = false;
      return;
    } 
    throw new IllegalStateException("Design assumption violated.");
  }
  
  private abstract class AccessibilityProvider {
    final ViewPager2 this$0;
    
    private AccessibilityProvider() {}
    
    boolean handlesGetAccessibilityClassName() {
      return false;
    }
    
    boolean handlesLmPerformAccessibilityAction(int param1Int) {
      return false;
    }
    
    boolean handlesPerformAccessibilityAction(int param1Int, Bundle param1Bundle) {
      return false;
    }
    
    boolean handlesRvGetAccessibilityClassName() {
      return false;
    }
    
    void onAttachAdapter(RecyclerView.Adapter<?> param1Adapter) {}
    
    void onDetachAdapter(RecyclerView.Adapter<?> param1Adapter) {}
    
    String onGetAccessibilityClassName() {
      throw new IllegalStateException("Not implemented.");
    }
    
    void onInitialize(CompositeOnPageChangeCallback param1CompositeOnPageChangeCallback, RecyclerView param1RecyclerView) {}
    
    void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo param1AccessibilityNodeInfo) {}
    
    void onLmInitializeAccessibilityNodeInfo(AccessibilityNodeInfoCompat param1AccessibilityNodeInfoCompat) {}
    
    boolean onLmPerformAccessibilityAction(int param1Int) {
      throw new IllegalStateException("Not implemented.");
    }
    
    boolean onPerformAccessibilityAction(int param1Int, Bundle param1Bundle) {
      throw new IllegalStateException("Not implemented.");
    }
    
    void onRestorePendingState() {}
    
    CharSequence onRvGetAccessibilityClassName() {
      throw new IllegalStateException("Not implemented.");
    }
    
    void onRvInitializeAccessibilityEvent(AccessibilityEvent param1AccessibilityEvent) {}
    
    void onSetLayoutDirection() {}
    
    void onSetNewCurrentItem() {}
    
    void onSetOrientation() {}
    
    void onSetUserInputEnabled() {}
  }
  
  class BasicAccessibilityProvider extends AccessibilityProvider {
    final ViewPager2 this$0;
    
    public boolean handlesLmPerformAccessibilityAction(int param1Int) {
      boolean bool;
      if ((param1Int == 8192 || param1Int == 4096) && !ViewPager2.this.isUserInputEnabled()) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public boolean handlesRvGetAccessibilityClassName() {
      return true;
    }
    
    public void onLmInitializeAccessibilityNodeInfo(AccessibilityNodeInfoCompat param1AccessibilityNodeInfoCompat) {
      if (!ViewPager2.this.isUserInputEnabled()) {
        param1AccessibilityNodeInfoCompat.removeAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_SCROLL_BACKWARD);
        param1AccessibilityNodeInfoCompat.removeAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_SCROLL_FORWARD);
        param1AccessibilityNodeInfoCompat.setScrollable(false);
      } 
    }
    
    public boolean onLmPerformAccessibilityAction(int param1Int) {
      if (handlesLmPerformAccessibilityAction(param1Int))
        return false; 
      throw new IllegalStateException();
    }
    
    public CharSequence onRvGetAccessibilityClassName() {
      if (handlesRvGetAccessibilityClassName())
        return "androidx.viewpager.widget.ViewPager"; 
      throw new IllegalStateException();
    }
  }
  
  private static abstract class DataSetChangeObserver extends RecyclerView.AdapterDataObserver {
    private DataSetChangeObserver() {}
    
    public abstract void onChanged();
    
    public final void onItemRangeChanged(int param1Int1, int param1Int2) {
      onChanged();
    }
    
    public final void onItemRangeChanged(int param1Int1, int param1Int2, Object param1Object) {
      onChanged();
    }
    
    public final void onItemRangeInserted(int param1Int1, int param1Int2) {
      onChanged();
    }
    
    public final void onItemRangeMoved(int param1Int1, int param1Int2, int param1Int3) {
      onChanged();
    }
    
    public final void onItemRangeRemoved(int param1Int1, int param1Int2) {
      onChanged();
    }
  }
  
  private class LinearLayoutManagerImpl extends LinearLayoutManager {
    final ViewPager2 this$0;
    
    LinearLayoutManagerImpl(Context param1Context) {
      super(param1Context);
    }
    
    protected void calculateExtraLayoutSpace(RecyclerView.State param1State, int[] param1ArrayOfint) {
      int i = ViewPager2.this.getOffscreenPageLimit();
      if (i == -1) {
        super.calculateExtraLayoutSpace(param1State, param1ArrayOfint);
        return;
      } 
      i = ViewPager2.this.getPageSize() * i;
      param1ArrayOfint[0] = i;
      param1ArrayOfint[1] = i;
    }
    
    public void onInitializeAccessibilityNodeInfo(RecyclerView.Recycler param1Recycler, RecyclerView.State param1State, AccessibilityNodeInfoCompat param1AccessibilityNodeInfoCompat) {
      super.onInitializeAccessibilityNodeInfo(param1Recycler, param1State, param1AccessibilityNodeInfoCompat);
      ViewPager2.this.mAccessibilityProvider.onLmInitializeAccessibilityNodeInfo(param1AccessibilityNodeInfoCompat);
    }
    
    public boolean performAccessibilityAction(RecyclerView.Recycler param1Recycler, RecyclerView.State param1State, int param1Int, Bundle param1Bundle) {
      return ViewPager2.this.mAccessibilityProvider.handlesLmPerformAccessibilityAction(param1Int) ? ViewPager2.this.mAccessibilityProvider.onLmPerformAccessibilityAction(param1Int) : super.performAccessibilityAction(param1Recycler, param1State, param1Int, param1Bundle);
    }
    
    public boolean requestChildRectangleOnScreen(RecyclerView param1RecyclerView, View param1View, Rect param1Rect, boolean param1Boolean1, boolean param1Boolean2) {
      return false;
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface OffscreenPageLimit {}
  
  public static abstract class OnPageChangeCallback {
    public void onPageScrollStateChanged(int param1Int) {}
    
    public void onPageScrolled(int param1Int1, float param1Float, int param1Int2) {}
    
    public void onPageSelected(int param1Int) {}
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface Orientation {}
  
  class PageAwareAccessibilityProvider extends AccessibilityProvider {
    private final AccessibilityViewCommand mActionPageBackward = new AccessibilityViewCommand() {
        final ViewPager2.PageAwareAccessibilityProvider this$1;
        
        public boolean perform(View param2View, AccessibilityViewCommand.CommandArguments param2CommandArguments) {
          ViewPager2 viewPager2 = (ViewPager2)param2View;
          ViewPager2.PageAwareAccessibilityProvider.this.setCurrentItemFromAccessibilityCommand(viewPager2.getCurrentItem() - 1);
          return true;
        }
      };
    
    private final AccessibilityViewCommand mActionPageForward = new AccessibilityViewCommand() {
        final ViewPager2.PageAwareAccessibilityProvider this$1;
        
        public boolean perform(View param2View, AccessibilityViewCommand.CommandArguments param2CommandArguments) {
          ViewPager2 viewPager2 = (ViewPager2)param2View;
          ViewPager2.PageAwareAccessibilityProvider.this.setCurrentItemFromAccessibilityCommand(viewPager2.getCurrentItem() + 1);
          return true;
        }
      };
    
    private RecyclerView.AdapterDataObserver mAdapterDataObserver;
    
    final ViewPager2 this$0;
    
    private void addCollectionInfo(AccessibilityNodeInfo param1AccessibilityNodeInfo) {
      byte b2 = 0;
      byte b1 = 0;
      int j = b2;
      int i = b1;
      if (ViewPager2.this.getAdapter() != null)
        if (ViewPager2.this.getOrientation() == 1) {
          j = ViewPager2.this.getAdapter().getItemCount();
          i = b1;
        } else {
          i = ViewPager2.this.getAdapter().getItemCount();
          j = b2;
        }  
      AccessibilityNodeInfoCompat accessibilityNodeInfoCompat = AccessibilityNodeInfoCompat.wrap(param1AccessibilityNodeInfo);
      accessibilityNodeInfoCompat.setCollectionInfo(AccessibilityNodeInfoCompat.CollectionInfoCompat.obtain(j, i, false, 0));
    }
    
    private void addScrollActions(AccessibilityNodeInfo param1AccessibilityNodeInfo) {
      RecyclerView.Adapter adapter = ViewPager2.this.getAdapter();
      if (adapter == null)
        return; 
      int i = adapter.getItemCount();
      if (i == 0 || !ViewPager2.this.isUserInputEnabled())
        return; 
      if (ViewPager2.this.mCurrentItem > 0)
        param1AccessibilityNodeInfo.addAction(8192); 
      if (ViewPager2.this.mCurrentItem < i - 1)
        param1AccessibilityNodeInfo.addAction(4096); 
      param1AccessibilityNodeInfo.setScrollable(true);
    }
    
    public boolean handlesGetAccessibilityClassName() {
      return true;
    }
    
    public boolean handlesPerformAccessibilityAction(int param1Int, Bundle param1Bundle) {
      return (param1Int == 8192 || param1Int == 4096);
    }
    
    public void onAttachAdapter(RecyclerView.Adapter<?> param1Adapter) {
      updatePageAccessibilityActions();
      if (param1Adapter != null)
        param1Adapter.registerAdapterDataObserver(this.mAdapterDataObserver); 
    }
    
    public void onDetachAdapter(RecyclerView.Adapter<?> param1Adapter) {
      if (param1Adapter != null)
        param1Adapter.unregisterAdapterDataObserver(this.mAdapterDataObserver); 
    }
    
    public String onGetAccessibilityClassName() {
      if (handlesGetAccessibilityClassName())
        return "androidx.viewpager.widget.ViewPager"; 
      throw new IllegalStateException();
    }
    
    public void onInitialize(CompositeOnPageChangeCallback param1CompositeOnPageChangeCallback, RecyclerView param1RecyclerView) {
      ViewCompat.setImportantForAccessibility((View)param1RecyclerView, 2);
      this.mAdapterDataObserver = new ViewPager2.DataSetChangeObserver() {
          final ViewPager2.PageAwareAccessibilityProvider this$1;
          
          public void onChanged() {
            ViewPager2.PageAwareAccessibilityProvider.this.updatePageAccessibilityActions();
          }
        };
      if (ViewCompat.getImportantForAccessibility((View)ViewPager2.this) == 0)
        ViewCompat.setImportantForAccessibility((View)ViewPager2.this, 1); 
    }
    
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo param1AccessibilityNodeInfo) {
      addCollectionInfo(param1AccessibilityNodeInfo);
      if (Build.VERSION.SDK_INT >= 16)
        addScrollActions(param1AccessibilityNodeInfo); 
    }
    
    public boolean onPerformAccessibilityAction(int param1Int, Bundle param1Bundle) {
      if (handlesPerformAccessibilityAction(param1Int, param1Bundle)) {
        if (param1Int == 8192) {
          param1Int = ViewPager2.this.getCurrentItem() - 1;
        } else {
          param1Int = ViewPager2.this.getCurrentItem() + 1;
        } 
        setCurrentItemFromAccessibilityCommand(param1Int);
        return true;
      } 
      throw new IllegalStateException();
    }
    
    public void onRestorePendingState() {
      updatePageAccessibilityActions();
    }
    
    public void onRvInitializeAccessibilityEvent(AccessibilityEvent param1AccessibilityEvent) {
      param1AccessibilityEvent.setSource((View)ViewPager2.this);
      param1AccessibilityEvent.setClassName(onGetAccessibilityClassName());
    }
    
    public void onSetLayoutDirection() {
      updatePageAccessibilityActions();
    }
    
    public void onSetNewCurrentItem() {
      updatePageAccessibilityActions();
    }
    
    public void onSetOrientation() {
      updatePageAccessibilityActions();
    }
    
    public void onSetUserInputEnabled() {
      updatePageAccessibilityActions();
      if (Build.VERSION.SDK_INT < 21)
        ViewPager2.this.sendAccessibilityEvent(2048); 
    }
    
    void setCurrentItemFromAccessibilityCommand(int param1Int) {
      if (ViewPager2.this.isUserInputEnabled())
        ViewPager2.this.setCurrentItemInternal(param1Int, true); 
    }
    
    void updatePageAccessibilityActions() {
      ViewPager2 viewPager2 = ViewPager2.this;
      int i = 16908360;
      ViewCompat.removeAccessibilityAction((View)viewPager2, 16908360);
      ViewCompat.removeAccessibilityAction((View)viewPager2, 16908361);
      ViewCompat.removeAccessibilityAction((View)viewPager2, 16908358);
      ViewCompat.removeAccessibilityAction((View)viewPager2, 16908359);
      if (ViewPager2.this.getAdapter() == null)
        return; 
      int j = ViewPager2.this.getAdapter().getItemCount();
      if (j == 0)
        return; 
      if (!ViewPager2.this.isUserInputEnabled())
        return; 
      if (ViewPager2.this.getOrientation() == 0) {
        int k;
        boolean bool = ViewPager2.this.isRtl();
        if (bool) {
          k = 16908360;
        } else {
          k = 16908361;
        } 
        if (bool)
          i = 16908361; 
        if (ViewPager2.this.mCurrentItem < j - 1)
          ViewCompat.replaceAccessibilityAction((View)viewPager2, new AccessibilityNodeInfoCompat.AccessibilityActionCompat(k, null), null, this.mActionPageForward); 
        if (ViewPager2.this.mCurrentItem > 0)
          ViewCompat.replaceAccessibilityAction((View)viewPager2, new AccessibilityNodeInfoCompat.AccessibilityActionCompat(i, null), null, this.mActionPageBackward); 
      } else {
        if (ViewPager2.this.mCurrentItem < j - 1)
          ViewCompat.replaceAccessibilityAction((View)viewPager2, new AccessibilityNodeInfoCompat.AccessibilityActionCompat(16908359, null), null, this.mActionPageForward); 
        if (ViewPager2.this.mCurrentItem > 0)
          ViewCompat.replaceAccessibilityAction((View)viewPager2, new AccessibilityNodeInfoCompat.AccessibilityActionCompat(16908358, null), null, this.mActionPageBackward); 
      } 
    }
  }
  
  class null implements AccessibilityViewCommand {
    final ViewPager2.PageAwareAccessibilityProvider this$1;
    
    public boolean perform(View param1View, AccessibilityViewCommand.CommandArguments param1CommandArguments) {
      ViewPager2 viewPager2 = (ViewPager2)param1View;
      this.this$1.setCurrentItemFromAccessibilityCommand(viewPager2.getCurrentItem() + 1);
      return true;
    }
  }
  
  class null implements AccessibilityViewCommand {
    final ViewPager2.PageAwareAccessibilityProvider this$1;
    
    public boolean perform(View param1View, AccessibilityViewCommand.CommandArguments param1CommandArguments) {
      ViewPager2 viewPager2 = (ViewPager2)param1View;
      this.this$1.setCurrentItemFromAccessibilityCommand(viewPager2.getCurrentItem() - 1);
      return true;
    }
  }
  
  class null extends DataSetChangeObserver {
    final ViewPager2.PageAwareAccessibilityProvider this$1;
    
    public void onChanged() {
      this.this$1.updatePageAccessibilityActions();
    }
  }
  
  public static interface PageTransformer {
    void transformPage(View param1View, float param1Float);
  }
  
  private class PagerSnapHelperImpl extends PagerSnapHelper {
    final ViewPager2 this$0;
    
    public View findSnapView(RecyclerView.LayoutManager param1LayoutManager) {
      View view;
      if (ViewPager2.this.isFakeDragging()) {
        param1LayoutManager = null;
      } else {
        view = super.findSnapView(param1LayoutManager);
      } 
      return view;
    }
  }
  
  private class RecyclerViewImpl extends RecyclerView {
    final ViewPager2 this$0;
    
    RecyclerViewImpl(Context param1Context) {
      super(param1Context);
    }
    
    public CharSequence getAccessibilityClassName() {
      return ViewPager2.this.mAccessibilityProvider.handlesRvGetAccessibilityClassName() ? ViewPager2.this.mAccessibilityProvider.onRvGetAccessibilityClassName() : super.getAccessibilityClassName();
    }
    
    public void onInitializeAccessibilityEvent(AccessibilityEvent param1AccessibilityEvent) {
      super.onInitializeAccessibilityEvent(param1AccessibilityEvent);
      param1AccessibilityEvent.setFromIndex(ViewPager2.this.mCurrentItem);
      param1AccessibilityEvent.setToIndex(ViewPager2.this.mCurrentItem);
      ViewPager2.this.mAccessibilityProvider.onRvInitializeAccessibilityEvent(param1AccessibilityEvent);
    }
    
    public boolean onInterceptTouchEvent(MotionEvent param1MotionEvent) {
      boolean bool;
      if (ViewPager2.this.isUserInputEnabled() && super.onInterceptTouchEvent(param1MotionEvent)) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public boolean onTouchEvent(MotionEvent param1MotionEvent) {
      boolean bool;
      if (ViewPager2.this.isUserInputEnabled() && super.onTouchEvent(param1MotionEvent)) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
  }
  
  static class SavedState extends View.BaseSavedState {
    public static final Parcelable.Creator<SavedState> CREATOR = (Parcelable.Creator<SavedState>)new Parcelable.ClassLoaderCreator<SavedState>() {
        public ViewPager2.SavedState createFromParcel(Parcel param2Parcel) {
          return createFromParcel(param2Parcel, (ClassLoader)null);
        }
        
        public ViewPager2.SavedState createFromParcel(Parcel param2Parcel, ClassLoader param2ClassLoader) {
          ViewPager2.SavedState savedState;
          if (Build.VERSION.SDK_INT >= 24) {
            savedState = new ViewPager2.SavedState(param2Parcel, param2ClassLoader);
          } else {
            savedState = new ViewPager2.SavedState((Parcel)savedState);
          } 
          return savedState;
        }
        
        public ViewPager2.SavedState[] newArray(int param2Int) {
          return new ViewPager2.SavedState[param2Int];
        }
      };
    
    Parcelable mAdapterState;
    
    int mCurrentItem;
    
    int mRecyclerViewId;
    
    SavedState(Parcel param1Parcel) {
      super(param1Parcel);
      readValues(param1Parcel, null);
    }
    
    SavedState(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      super(param1Parcel, param1ClassLoader);
      readValues(param1Parcel, param1ClassLoader);
    }
    
    SavedState(Parcelable param1Parcelable) {
      super(param1Parcelable);
    }
    
    private void readValues(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      this.mRecyclerViewId = param1Parcel.readInt();
      this.mCurrentItem = param1Parcel.readInt();
      this.mAdapterState = param1Parcel.readParcelable(param1ClassLoader);
    }
    
    public void writeToParcel(Parcel param1Parcel, int param1Int) {
      super.writeToParcel(param1Parcel, param1Int);
      param1Parcel.writeInt(this.mRecyclerViewId);
      param1Parcel.writeInt(this.mCurrentItem);
      param1Parcel.writeParcelable(this.mAdapterState, param1Int);
    }
  }
  
  static final class null implements Parcelable.ClassLoaderCreator<SavedState> {
    public ViewPager2.SavedState createFromParcel(Parcel param1Parcel) {
      return createFromParcel(param1Parcel, (ClassLoader)null);
    }
    
    public ViewPager2.SavedState createFromParcel(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      ViewPager2.SavedState savedState;
      if (Build.VERSION.SDK_INT >= 24) {
        savedState = new ViewPager2.SavedState(param1Parcel, param1ClassLoader);
      } else {
        savedState = new ViewPager2.SavedState((Parcel)savedState);
      } 
      return savedState;
    }
    
    public ViewPager2.SavedState[] newArray(int param1Int) {
      return new ViewPager2.SavedState[param1Int];
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface ScrollState {}
  
  private static class SmoothScrollToPosition implements Runnable {
    private final int mPosition;
    
    private final RecyclerView mRecyclerView;
    
    SmoothScrollToPosition(int param1Int, RecyclerView param1RecyclerView) {
      this.mPosition = param1Int;
      this.mRecyclerView = param1RecyclerView;
    }
    
    public void run() {
      this.mRecyclerView.smoothScrollToPosition(this.mPosition);
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\viewpager2\widget\ViewPager2.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */