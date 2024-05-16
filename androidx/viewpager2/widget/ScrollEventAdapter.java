package androidx.viewpager2.widget;

import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Locale;

final class ScrollEventAdapter extends RecyclerView.OnScrollListener {
  private static final int NO_POSITION = -1;
  
  private static final int STATE_IDLE = 0;
  
  private static final int STATE_IN_PROGRESS_FAKE_DRAG = 4;
  
  private static final int STATE_IN_PROGRESS_IMMEDIATE_SCROLL = 3;
  
  private static final int STATE_IN_PROGRESS_MANUAL_DRAG = 1;
  
  private static final int STATE_IN_PROGRESS_SMOOTH_SCROLL = 2;
  
  private int mAdapterState;
  
  private ViewPager2.OnPageChangeCallback mCallback;
  
  private boolean mDataSetChangeHappened;
  
  private boolean mDispatchSelected;
  
  private int mDragStartPosition;
  
  private boolean mFakeDragging;
  
  private final LinearLayoutManager mLayoutManager;
  
  private final RecyclerView mRecyclerView;
  
  private boolean mScrollHappened;
  
  private int mScrollState;
  
  private ScrollEventValues mScrollValues;
  
  private int mTarget;
  
  private final ViewPager2 mViewPager;
  
  ScrollEventAdapter(ViewPager2 paramViewPager2) {
    this.mViewPager = paramViewPager2;
    RecyclerView recyclerView = paramViewPager2.mRecyclerView;
    this.mRecyclerView = recyclerView;
    this.mLayoutManager = (LinearLayoutManager)recyclerView.getLayoutManager();
    this.mScrollValues = new ScrollEventValues();
    resetState();
  }
  
  private void dispatchScrolled(int paramInt1, float paramFloat, int paramInt2) {
    ViewPager2.OnPageChangeCallback onPageChangeCallback = this.mCallback;
    if (onPageChangeCallback != null)
      onPageChangeCallback.onPageScrolled(paramInt1, paramFloat, paramInt2); 
  }
  
  private void dispatchSelected(int paramInt) {
    ViewPager2.OnPageChangeCallback onPageChangeCallback = this.mCallback;
    if (onPageChangeCallback != null)
      onPageChangeCallback.onPageSelected(paramInt); 
  }
  
  private void dispatchStateChanged(int paramInt) {
    if (this.mAdapterState == 3 && this.mScrollState == 0)
      return; 
    if (this.mScrollState == paramInt)
      return; 
    this.mScrollState = paramInt;
    ViewPager2.OnPageChangeCallback onPageChangeCallback = this.mCallback;
    if (onPageChangeCallback != null)
      onPageChangeCallback.onPageScrollStateChanged(paramInt); 
  }
  
  private int getPosition() {
    return this.mLayoutManager.findFirstVisibleItemPosition();
  }
  
  private boolean isInAnyDraggingState() {
    int i = this.mAdapterState;
    boolean bool2 = true;
    boolean bool1 = bool2;
    if (i != 1)
      if (i == 4) {
        bool1 = bool2;
      } else {
        bool1 = false;
      }  
    return bool1;
  }
  
  private void resetState() {
    this.mAdapterState = 0;
    this.mScrollState = 0;
    this.mScrollValues.reset();
    this.mDragStartPosition = -1;
    this.mTarget = -1;
    this.mDispatchSelected = false;
    this.mScrollHappened = false;
    this.mFakeDragging = false;
    this.mDataSetChangeHappened = false;
  }
  
  private void startDrag(boolean paramBoolean) {
    this.mFakeDragging = paramBoolean;
    if (paramBoolean) {
      i = 4;
    } else {
      i = 1;
    } 
    this.mAdapterState = i;
    int i = this.mTarget;
    if (i != -1) {
      this.mDragStartPosition = i;
      this.mTarget = -1;
    } else if (this.mDragStartPosition == -1) {
      this.mDragStartPosition = getPosition();
    } 
    dispatchStateChanged(1);
  }
  
  private void updateScrollEventValues() {
    float f;
    ScrollEventValues scrollEventValues = this.mScrollValues;
    scrollEventValues.mPosition = this.mLayoutManager.findFirstVisibleItemPosition();
    if (scrollEventValues.mPosition == -1) {
      scrollEventValues.reset();
      return;
    } 
    View view = this.mLayoutManager.findViewByPosition(scrollEventValues.mPosition);
    if (view == null) {
      scrollEventValues.reset();
      return;
    } 
    int i3 = this.mLayoutManager.getLeftDecorationWidth(view);
    int i2 = this.mLayoutManager.getRightDecorationWidth(view);
    int i1 = this.mLayoutManager.getTopDecorationHeight(view);
    int n = this.mLayoutManager.getBottomDecorationHeight(view);
    ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
    int k = i3;
    int m = i2;
    int j = i1;
    int i = n;
    if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
      ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams)layoutParams;
      k = i3 + marginLayoutParams.leftMargin;
      m = i2 + marginLayoutParams.rightMargin;
      j = i1 + marginLayoutParams.topMargin;
      i = n + marginLayoutParams.bottomMargin;
    } 
    i1 = view.getHeight();
    i2 = view.getWidth();
    if (this.mLayoutManager.getOrientation() == 0) {
      n = 1;
    } else {
      n = 0;
    } 
    if (n != 0) {
      m = i2 + k + m;
      k = view.getLeft() - k - this.mRecyclerView.getPaddingLeft();
      j = m;
      i = k;
      if (this.mViewPager.isRtl()) {
        i = -k;
        j = m;
      } 
    } else {
      k = i1 + j + i;
      i = view.getTop() - j - this.mRecyclerView.getPaddingTop();
      j = k;
    } 
    scrollEventValues.mOffsetPx = -i;
    if (scrollEventValues.mOffsetPx < 0) {
      if ((new AnimateLayoutChangeDetector(this.mLayoutManager)).mayHaveInterferingAnimations())
        throw new IllegalStateException("Page(s) contain a ViewGroup with a LayoutTransition (or animateLayoutChanges=\"true\"), which interferes with the scrolling animation. Make sure to call getLayoutTransition().setAnimateParentHierarchy(false) on all ViewGroups with a LayoutTransition before an animation is started."); 
      throw new IllegalStateException(String.format(Locale.US, "Page can only be offset by a positive amount, not by %d", new Object[] { Integer.valueOf(scrollEventValues.mOffsetPx) }));
    } 
    if (j == 0) {
      f = 0.0F;
    } else {
      f = scrollEventValues.mOffsetPx / j;
    } 
    scrollEventValues.mOffset = f;
  }
  
  double getRelativeScrollPosition() {
    updateScrollEventValues();
    return this.mScrollValues.mPosition + this.mScrollValues.mOffset;
  }
  
  int getScrollState() {
    return this.mScrollState;
  }
  
  boolean isDragging() {
    int i = this.mScrollState;
    boolean bool = true;
    if (i != 1)
      bool = false; 
    return bool;
  }
  
  boolean isFakeDragging() {
    return this.mFakeDragging;
  }
  
  boolean isIdle() {
    boolean bool;
    if (this.mScrollState == 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  void notifyBeginFakeDrag() {
    this.mAdapterState = 4;
    startDrag(true);
  }
  
  void notifyDataSetChangeHappened() {
    this.mDataSetChangeHappened = true;
  }
  
  void notifyEndFakeDrag() {
    if (isDragging() && !this.mFakeDragging)
      return; 
    this.mFakeDragging = false;
    updateScrollEventValues();
    if (this.mScrollValues.mOffsetPx == 0) {
      if (this.mScrollValues.mPosition != this.mDragStartPosition)
        dispatchSelected(this.mScrollValues.mPosition); 
      dispatchStateChanged(0);
      resetState();
    } else {
      dispatchStateChanged(2);
    } 
  }
  
  void notifyProgrammaticScroll(int paramInt, boolean paramBoolean) {
    if (paramBoolean) {
      b = 2;
    } else {
      b = 3;
    } 
    this.mAdapterState = b;
    byte b = 0;
    this.mFakeDragging = false;
    if (this.mTarget != paramInt)
      b = 1; 
    this.mTarget = paramInt;
    dispatchStateChanged(2);
    if (b != 0)
      dispatchSelected(paramInt); 
  }
  
  public void onScrollStateChanged(RecyclerView paramRecyclerView, int paramInt) {
    if ((this.mAdapterState != 1 || this.mScrollState != 1) && paramInt == 1) {
      startDrag(false);
      return;
    } 
    if (isInAnyDraggingState() && paramInt == 2) {
      if (this.mScrollHappened) {
        dispatchStateChanged(2);
        this.mDispatchSelected = true;
      } 
      return;
    } 
    if (isInAnyDraggingState() && paramInt == 0) {
      boolean bool = false;
      updateScrollEventValues();
      if (!this.mScrollHappened) {
        if (this.mScrollValues.mPosition != -1)
          dispatchScrolled(this.mScrollValues.mPosition, 0.0F, 0); 
        bool = true;
      } else if (this.mScrollValues.mOffsetPx == 0) {
        boolean bool1 = true;
        bool = bool1;
        if (this.mDragStartPosition != this.mScrollValues.mPosition) {
          dispatchSelected(this.mScrollValues.mPosition);
          bool = bool1;
        } 
      } 
      if (bool) {
        dispatchStateChanged(0);
        resetState();
      } 
    } 
    if (this.mAdapterState == 2 && paramInt == 0 && this.mDataSetChangeHappened) {
      updateScrollEventValues();
      if (this.mScrollValues.mOffsetPx == 0) {
        if (this.mTarget != this.mScrollValues.mPosition) {
          if (this.mScrollValues.mPosition == -1) {
            paramInt = 0;
          } else {
            paramInt = this.mScrollValues.mPosition;
          } 
          dispatchSelected(paramInt);
        } 
        dispatchStateChanged(0);
        resetState();
      } 
    } 
  }
  
  public void onScrolled(RecyclerView paramRecyclerView, int paramInt1, int paramInt2) {
    // Byte code:
    //   0: aload_0
    //   1: iconst_1
    //   2: putfield mScrollHappened : Z
    //   5: aload_0
    //   6: invokespecial updateScrollEventValues : ()V
    //   9: aload_0
    //   10: getfield mDispatchSelected : Z
    //   13: ifeq -> 120
    //   16: aload_0
    //   17: iconst_0
    //   18: putfield mDispatchSelected : Z
    //   21: iload_3
    //   22: ifgt -> 62
    //   25: iload_3
    //   26: ifne -> 57
    //   29: iload_2
    //   30: ifge -> 39
    //   33: iconst_1
    //   34: istore #4
    //   36: goto -> 42
    //   39: iconst_0
    //   40: istore #4
    //   42: iload #4
    //   44: aload_0
    //   45: getfield mViewPager : Landroidx/viewpager2/widget/ViewPager2;
    //   48: invokevirtual isRtl : ()Z
    //   51: if_icmpne -> 57
    //   54: goto -> 62
    //   57: iconst_0
    //   58: istore_2
    //   59: goto -> 64
    //   62: iconst_1
    //   63: istore_2
    //   64: iload_2
    //   65: ifeq -> 91
    //   68: aload_0
    //   69: getfield mScrollValues : Landroidx/viewpager2/widget/ScrollEventAdapter$ScrollEventValues;
    //   72: getfield mOffsetPx : I
    //   75: ifeq -> 91
    //   78: aload_0
    //   79: getfield mScrollValues : Landroidx/viewpager2/widget/ScrollEventAdapter$ScrollEventValues;
    //   82: getfield mPosition : I
    //   85: iconst_1
    //   86: iadd
    //   87: istore_2
    //   88: goto -> 99
    //   91: aload_0
    //   92: getfield mScrollValues : Landroidx/viewpager2/widget/ScrollEventAdapter$ScrollEventValues;
    //   95: getfield mPosition : I
    //   98: istore_2
    //   99: aload_0
    //   100: iload_2
    //   101: putfield mTarget : I
    //   104: aload_0
    //   105: getfield mDragStartPosition : I
    //   108: iload_2
    //   109: if_icmpeq -> 153
    //   112: aload_0
    //   113: iload_2
    //   114: invokespecial dispatchSelected : (I)V
    //   117: goto -> 153
    //   120: aload_0
    //   121: getfield mAdapterState : I
    //   124: ifne -> 153
    //   127: aload_0
    //   128: getfield mScrollValues : Landroidx/viewpager2/widget/ScrollEventAdapter$ScrollEventValues;
    //   131: getfield mPosition : I
    //   134: istore_2
    //   135: iload_2
    //   136: iconst_m1
    //   137: if_icmpne -> 145
    //   140: iconst_0
    //   141: istore_2
    //   142: goto -> 145
    //   145: aload_0
    //   146: iload_2
    //   147: invokespecial dispatchSelected : (I)V
    //   150: goto -> 153
    //   153: aload_0
    //   154: getfield mScrollValues : Landroidx/viewpager2/widget/ScrollEventAdapter$ScrollEventValues;
    //   157: getfield mPosition : I
    //   160: iconst_m1
    //   161: if_icmpne -> 169
    //   164: iconst_0
    //   165: istore_2
    //   166: goto -> 177
    //   169: aload_0
    //   170: getfield mScrollValues : Landroidx/viewpager2/widget/ScrollEventAdapter$ScrollEventValues;
    //   173: getfield mPosition : I
    //   176: istore_2
    //   177: aload_0
    //   178: iload_2
    //   179: aload_0
    //   180: getfield mScrollValues : Landroidx/viewpager2/widget/ScrollEventAdapter$ScrollEventValues;
    //   183: getfield mOffset : F
    //   186: aload_0
    //   187: getfield mScrollValues : Landroidx/viewpager2/widget/ScrollEventAdapter$ScrollEventValues;
    //   190: getfield mOffsetPx : I
    //   193: invokespecial dispatchScrolled : (IFI)V
    //   196: aload_0
    //   197: getfield mScrollValues : Landroidx/viewpager2/widget/ScrollEventAdapter$ScrollEventValues;
    //   200: getfield mPosition : I
    //   203: istore_2
    //   204: aload_0
    //   205: getfield mTarget : I
    //   208: istore_3
    //   209: iload_2
    //   210: iload_3
    //   211: if_icmpeq -> 219
    //   214: iload_3
    //   215: iconst_m1
    //   216: if_icmpne -> 246
    //   219: aload_0
    //   220: getfield mScrollValues : Landroidx/viewpager2/widget/ScrollEventAdapter$ScrollEventValues;
    //   223: getfield mOffsetPx : I
    //   226: ifne -> 246
    //   229: aload_0
    //   230: getfield mScrollState : I
    //   233: iconst_1
    //   234: if_icmpeq -> 246
    //   237: aload_0
    //   238: iconst_0
    //   239: invokespecial dispatchStateChanged : (I)V
    //   242: aload_0
    //   243: invokespecial resetState : ()V
    //   246: return
  }
  
  void setOnPageChangeCallback(ViewPager2.OnPageChangeCallback paramOnPageChangeCallback) {
    this.mCallback = paramOnPageChangeCallback;
  }
  
  private static final class ScrollEventValues {
    float mOffset;
    
    int mOffsetPx;
    
    int mPosition;
    
    void reset() {
      this.mPosition = -1;
      this.mOffset = 0.0F;
      this.mOffsetPx = 0;
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\viewpager2\widget\ScrollEventAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */