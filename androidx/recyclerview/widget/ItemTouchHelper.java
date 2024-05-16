package androidx.recyclerview.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.view.animation.Interpolator;
import androidx.core.view.GestureDetectorCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.R;
import java.util.ArrayList;
import java.util.List;

public class ItemTouchHelper extends RecyclerView.ItemDecoration implements RecyclerView.OnChildAttachStateChangeListener {
  static final int ACTION_MODE_DRAG_MASK = 16711680;
  
  private static final int ACTION_MODE_IDLE_MASK = 255;
  
  static final int ACTION_MODE_SWIPE_MASK = 65280;
  
  public static final int ACTION_STATE_DRAG = 2;
  
  public static final int ACTION_STATE_IDLE = 0;
  
  public static final int ACTION_STATE_SWIPE = 1;
  
  private static final int ACTIVE_POINTER_ID_NONE = -1;
  
  public static final int ANIMATION_TYPE_DRAG = 8;
  
  public static final int ANIMATION_TYPE_SWIPE_CANCEL = 4;
  
  public static final int ANIMATION_TYPE_SWIPE_SUCCESS = 2;
  
  private static final boolean DEBUG = false;
  
  static final int DIRECTION_FLAG_COUNT = 8;
  
  public static final int DOWN = 2;
  
  public static final int END = 32;
  
  public static final int LEFT = 4;
  
  private static final int PIXELS_PER_SECOND = 1000;
  
  public static final int RIGHT = 8;
  
  public static final int START = 16;
  
  private static final String TAG = "ItemTouchHelper";
  
  public static final int UP = 1;
  
  private int mActionState = 0;
  
  int mActivePointerId = -1;
  
  Callback mCallback;
  
  private RecyclerView.ChildDrawingOrderCallback mChildDrawingOrderCallback = null;
  
  private List<Integer> mDistances;
  
  private long mDragScrollStartTimeInMs;
  
  float mDx;
  
  float mDy;
  
  GestureDetectorCompat mGestureDetector;
  
  float mInitialTouchX;
  
  float mInitialTouchY;
  
  private ItemTouchHelperGestureListener mItemTouchHelperGestureListener;
  
  private float mMaxSwipeVelocity;
  
  private final RecyclerView.OnItemTouchListener mOnItemTouchListener = new RecyclerView.OnItemTouchListener() {
      final ItemTouchHelper this$0;
      
      public boolean onInterceptTouchEvent(RecyclerView param1RecyclerView, MotionEvent param1MotionEvent) {
        ItemTouchHelper.this.mGestureDetector.onTouchEvent(param1MotionEvent);
        int i = param1MotionEvent.getActionMasked();
        boolean bool = true;
        if (i == 0) {
          ItemTouchHelper.this.mActivePointerId = param1MotionEvent.getPointerId(0);
          ItemTouchHelper.this.mInitialTouchX = param1MotionEvent.getX();
          ItemTouchHelper.this.mInitialTouchY = param1MotionEvent.getY();
          ItemTouchHelper.this.obtainVelocityTracker();
          if (ItemTouchHelper.this.mSelected == null) {
            ItemTouchHelper.RecoverAnimation recoverAnimation = ItemTouchHelper.this.findAnimation(param1MotionEvent);
            if (recoverAnimation != null) {
              ItemTouchHelper itemTouchHelper2 = ItemTouchHelper.this;
              itemTouchHelper2.mInitialTouchX -= recoverAnimation.mX;
              itemTouchHelper2 = ItemTouchHelper.this;
              itemTouchHelper2.mInitialTouchY -= recoverAnimation.mY;
              ItemTouchHelper.this.endRecoverAnimation(recoverAnimation.mViewHolder, true);
              if (ItemTouchHelper.this.mPendingCleanup.remove(recoverAnimation.mViewHolder.itemView))
                ItemTouchHelper.this.mCallback.clearView(ItemTouchHelper.this.mRecyclerView, recoverAnimation.mViewHolder); 
              ItemTouchHelper.this.select(recoverAnimation.mViewHolder, recoverAnimation.mActionState);
              ItemTouchHelper itemTouchHelper1 = ItemTouchHelper.this;
              itemTouchHelper1.updateDxDy(param1MotionEvent, itemTouchHelper1.mSelectedFlags, 0);
            } 
          } 
        } else if (i == 3 || i == 1) {
          ItemTouchHelper.this.mActivePointerId = -1;
          ItemTouchHelper.this.select(null, 0);
        } else if (ItemTouchHelper.this.mActivePointerId != -1) {
          int j = param1MotionEvent.findPointerIndex(ItemTouchHelper.this.mActivePointerId);
          if (j >= 0)
            ItemTouchHelper.this.checkSelectForSwipe(i, param1MotionEvent, j); 
        } 
        if (ItemTouchHelper.this.mVelocityTracker != null)
          ItemTouchHelper.this.mVelocityTracker.addMovement(param1MotionEvent); 
        if (ItemTouchHelper.this.mSelected == null)
          bool = false; 
        return bool;
      }
      
      public void onRequestDisallowInterceptTouchEvent(boolean param1Boolean) {
        if (!param1Boolean)
          return; 
        ItemTouchHelper.this.select(null, 0);
      }
      
      public void onTouchEvent(RecyclerView param1RecyclerView, MotionEvent param1MotionEvent) {
        ItemTouchHelper.this.mGestureDetector.onTouchEvent(param1MotionEvent);
        if (ItemTouchHelper.this.mVelocityTracker != null)
          ItemTouchHelper.this.mVelocityTracker.addMovement(param1MotionEvent); 
        if (ItemTouchHelper.this.mActivePointerId == -1)
          return; 
        int j = param1MotionEvent.getActionMasked();
        int i = param1MotionEvent.findPointerIndex(ItemTouchHelper.this.mActivePointerId);
        if (i >= 0)
          ItemTouchHelper.this.checkSelectForSwipe(j, param1MotionEvent, i); 
        RecyclerView.ViewHolder viewHolder = ItemTouchHelper.this.mSelected;
        if (viewHolder == null)
          return; 
        boolean bool = false;
        switch (j) {
          default:
            return;
          case 6:
            i = param1MotionEvent.getActionIndex();
            if (param1MotionEvent.getPointerId(i) == ItemTouchHelper.this.mActivePointerId) {
              if (i == 0)
                bool = true; 
              ItemTouchHelper.this.mActivePointerId = param1MotionEvent.getPointerId(bool);
              ItemTouchHelper itemTouchHelper = ItemTouchHelper.this;
              itemTouchHelper.updateDxDy(param1MotionEvent, itemTouchHelper.mSelectedFlags, i);
            } 
          case 3:
            if (ItemTouchHelper.this.mVelocityTracker != null)
              ItemTouchHelper.this.mVelocityTracker.clear(); 
            break;
          case 2:
            if (i >= 0) {
              ItemTouchHelper itemTouchHelper = ItemTouchHelper.this;
              itemTouchHelper.updateDxDy(param1MotionEvent, itemTouchHelper.mSelectedFlags, i);
              ItemTouchHelper.this.moveIfNecessary(viewHolder);
              ItemTouchHelper.this.mRecyclerView.removeCallbacks(ItemTouchHelper.this.mScrollRunnable);
              ItemTouchHelper.this.mScrollRunnable.run();
              ItemTouchHelper.this.mRecyclerView.invalidate();
            } 
          case 1:
            break;
        } 
        ItemTouchHelper.this.select(null, 0);
        ItemTouchHelper.this.mActivePointerId = -1;
      }
    };
  
  View mOverdrawChild = null;
  
  int mOverdrawChildPosition = -1;
  
  final List<View> mPendingCleanup = new ArrayList<>();
  
  List<RecoverAnimation> mRecoverAnimations = new ArrayList<>();
  
  RecyclerView mRecyclerView;
  
  final Runnable mScrollRunnable = new Runnable() {
      final ItemTouchHelper this$0;
      
      public void run() {
        if (ItemTouchHelper.this.mSelected != null && ItemTouchHelper.this.scrollIfNecessary()) {
          if (ItemTouchHelper.this.mSelected != null) {
            ItemTouchHelper itemTouchHelper = ItemTouchHelper.this;
            itemTouchHelper.moveIfNecessary(itemTouchHelper.mSelected);
          } 
          ItemTouchHelper.this.mRecyclerView.removeCallbacks(ItemTouchHelper.this.mScrollRunnable);
          ViewCompat.postOnAnimation((View)ItemTouchHelper.this.mRecyclerView, this);
        } 
      }
    };
  
  RecyclerView.ViewHolder mSelected = null;
  
  int mSelectedFlags;
  
  private float mSelectedStartX;
  
  private float mSelectedStartY;
  
  private int mSlop;
  
  private List<RecyclerView.ViewHolder> mSwapTargets;
  
  private float mSwipeEscapeVelocity;
  
  private final float[] mTmpPosition = new float[2];
  
  private Rect mTmpRect;
  
  VelocityTracker mVelocityTracker;
  
  public ItemTouchHelper(Callback paramCallback) {
    this.mCallback = paramCallback;
  }
  
  private void addChildDrawingOrderCallback() {
    if (Build.VERSION.SDK_INT >= 21)
      return; 
    if (this.mChildDrawingOrderCallback == null)
      this.mChildDrawingOrderCallback = new RecyclerView.ChildDrawingOrderCallback() {
          final ItemTouchHelper this$0;
          
          public int onGetChildDrawingOrder(int param1Int1, int param1Int2) {
            if (ItemTouchHelper.this.mOverdrawChild == null)
              return param1Int2; 
            int j = ItemTouchHelper.this.mOverdrawChildPosition;
            int i = j;
            if (j == -1) {
              i = ItemTouchHelper.this.mRecyclerView.indexOfChild(ItemTouchHelper.this.mOverdrawChild);
              ItemTouchHelper.this.mOverdrawChildPosition = i;
            } 
            if (param1Int2 == param1Int1 - 1)
              return i; 
            if (param1Int2 >= i)
              param1Int2++; 
            return param1Int2;
          }
        }; 
    this.mRecyclerView.setChildDrawingOrderCallback(this.mChildDrawingOrderCallback);
  }
  
  private int checkHorizontalSwipe(RecyclerView.ViewHolder paramViewHolder, int paramInt) {
    if ((paramInt & 0xC) != 0) {
      byte b1;
      float f1 = this.mDx;
      byte b2 = 8;
      if (f1 > 0.0F) {
        b1 = 8;
      } else {
        b1 = 4;
      } 
      VelocityTracker velocityTracker = this.mVelocityTracker;
      if (velocityTracker != null && this.mActivePointerId > -1) {
        velocityTracker.computeCurrentVelocity(1000, this.mCallback.getSwipeVelocityThreshold(this.mMaxSwipeVelocity));
        float f = this.mVelocityTracker.getXVelocity(this.mActivePointerId);
        f1 = this.mVelocityTracker.getYVelocity(this.mActivePointerId);
        if (f <= 0.0F)
          b2 = 4; 
        f = Math.abs(f);
        if ((b2 & paramInt) != 0 && b1 == b2 && f >= this.mCallback.getSwipeEscapeVelocity(this.mSwipeEscapeVelocity) && f > Math.abs(f1))
          return b2; 
      } 
      float f2 = this.mRecyclerView.getWidth();
      f1 = this.mCallback.getSwipeThreshold(paramViewHolder);
      if ((paramInt & b1) != 0 && Math.abs(this.mDx) > f2 * f1)
        return b1; 
    } 
    return 0;
  }
  
  private int checkVerticalSwipe(RecyclerView.ViewHolder paramViewHolder, int paramInt) {
    if ((paramInt & 0x3) != 0) {
      byte b1;
      float f1 = this.mDy;
      byte b2 = 2;
      if (f1 > 0.0F) {
        b1 = 2;
      } else {
        b1 = 1;
      } 
      VelocityTracker velocityTracker = this.mVelocityTracker;
      if (velocityTracker != null && this.mActivePointerId > -1) {
        velocityTracker.computeCurrentVelocity(1000, this.mCallback.getSwipeVelocityThreshold(this.mMaxSwipeVelocity));
        f1 = this.mVelocityTracker.getXVelocity(this.mActivePointerId);
        float f = this.mVelocityTracker.getYVelocity(this.mActivePointerId);
        if (f <= 0.0F)
          b2 = 1; 
        f = Math.abs(f);
        if ((b2 & paramInt) != 0 && b2 == b1 && f >= this.mCallback.getSwipeEscapeVelocity(this.mSwipeEscapeVelocity) && f > Math.abs(f1))
          return b2; 
      } 
      float f2 = this.mRecyclerView.getHeight();
      f1 = this.mCallback.getSwipeThreshold(paramViewHolder);
      if ((paramInt & b1) != 0 && Math.abs(this.mDy) > f2 * f1)
        return b1; 
    } 
    return 0;
  }
  
  private void destroyCallbacks() {
    this.mRecyclerView.removeItemDecoration(this);
    this.mRecyclerView.removeOnItemTouchListener(this.mOnItemTouchListener);
    this.mRecyclerView.removeOnChildAttachStateChangeListener(this);
    for (int i = this.mRecoverAnimations.size() - 1; i >= 0; i--) {
      RecoverAnimation recoverAnimation = this.mRecoverAnimations.get(0);
      this.mCallback.clearView(this.mRecyclerView, recoverAnimation.mViewHolder);
    } 
    this.mRecoverAnimations.clear();
    this.mOverdrawChild = null;
    this.mOverdrawChildPosition = -1;
    releaseVelocityTracker();
    stopGestureDetection();
  }
  
  private List<RecyclerView.ViewHolder> findSwapTargets(RecyclerView.ViewHolder paramViewHolder) {
    List<RecyclerView.ViewHolder> list = this.mSwapTargets;
    if (list == null) {
      this.mSwapTargets = new ArrayList<>();
      this.mDistances = new ArrayList<>();
    } else {
      list.clear();
      this.mDistances.clear();
    } 
    int i = this.mCallback.getBoundingBoxMargin();
    int j = Math.round(this.mSelectedStartX + this.mDx) - i;
    int m = Math.round(this.mSelectedStartY + this.mDy) - i;
    int i2 = paramViewHolder.itemView.getWidth() + j + i * 2;
    int n = paramViewHolder.itemView.getHeight() + m + i * 2;
    int k = (j + i2) / 2;
    int i1 = (m + n) / 2;
    RecyclerView.LayoutManager layoutManager = this.mRecyclerView.getLayoutManager();
    int i3 = layoutManager.getChildCount();
    for (byte b = 0; b < i3; b++) {
      View view = layoutManager.getChildAt(b);
      if (view != paramViewHolder.itemView && view.getBottom() >= m && view.getTop() <= n && view.getRight() >= j && view.getLeft() <= i2) {
        RecyclerView.ViewHolder viewHolder = this.mRecyclerView.getChildViewHolder(view);
        if (this.mCallback.canDropOver(this.mRecyclerView, this.mSelected, viewHolder)) {
          int i5 = Math.abs(k - (view.getLeft() + view.getRight()) / 2);
          int i4 = Math.abs(i1 - (view.getTop() + view.getBottom()) / 2);
          int i7 = i5 * i5 + i4 * i4;
          int i6 = this.mSwapTargets.size();
          i5 = 0;
          for (i4 = 0; i4 < i6 && i7 > ((Integer)this.mDistances.get(i4)).intValue(); i4++)
            i5++; 
          this.mSwapTargets.add(i5, viewHolder);
          this.mDistances.add(i5, Integer.valueOf(i7));
        } 
      } 
    } 
    return this.mSwapTargets;
  }
  
  private RecyclerView.ViewHolder findSwipedView(MotionEvent paramMotionEvent) {
    RecyclerView.LayoutManager layoutManager = this.mRecyclerView.getLayoutManager();
    int i = this.mActivePointerId;
    if (i == -1)
      return null; 
    i = paramMotionEvent.findPointerIndex(i);
    float f4 = paramMotionEvent.getX(i);
    float f3 = this.mInitialTouchX;
    float f2 = paramMotionEvent.getY(i);
    float f1 = this.mInitialTouchY;
    f3 = Math.abs(f4 - f3);
    f1 = Math.abs(f2 - f1);
    i = this.mSlop;
    if (f3 < i && f1 < i)
      return null; 
    if (f3 > f1 && layoutManager.canScrollHorizontally())
      return null; 
    if (f1 > f3 && layoutManager.canScrollVertically())
      return null; 
    View view = findChildView(paramMotionEvent);
    return (view == null) ? null : this.mRecyclerView.getChildViewHolder(view);
  }
  
  private void getSelectedDxDy(float[] paramArrayOffloat) {
    if ((this.mSelectedFlags & 0xC) != 0) {
      paramArrayOffloat[0] = this.mSelectedStartX + this.mDx - this.mSelected.itemView.getLeft();
    } else {
      paramArrayOffloat[0] = this.mSelected.itemView.getTranslationX();
    } 
    if ((this.mSelectedFlags & 0x3) != 0) {
      paramArrayOffloat[1] = this.mSelectedStartY + this.mDy - this.mSelected.itemView.getTop();
    } else {
      paramArrayOffloat[1] = this.mSelected.itemView.getTranslationY();
    } 
  }
  
  private static boolean hitTest(View paramView, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
    boolean bool;
    if (paramFloat1 >= paramFloat3 && paramFloat1 <= paramView.getWidth() + paramFloat3 && paramFloat2 >= paramFloat4 && paramFloat2 <= paramView.getHeight() + paramFloat4) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private void releaseVelocityTracker() {
    VelocityTracker velocityTracker = this.mVelocityTracker;
    if (velocityTracker != null) {
      velocityTracker.recycle();
      this.mVelocityTracker = null;
    } 
  }
  
  private void setupCallbacks() {
    this.mSlop = ViewConfiguration.get(this.mRecyclerView.getContext()).getScaledTouchSlop();
    this.mRecyclerView.addItemDecoration(this);
    this.mRecyclerView.addOnItemTouchListener(this.mOnItemTouchListener);
    this.mRecyclerView.addOnChildAttachStateChangeListener(this);
    startGestureDetection();
  }
  
  private void startGestureDetection() {
    this.mItemTouchHelperGestureListener = new ItemTouchHelperGestureListener();
    this.mGestureDetector = new GestureDetectorCompat(this.mRecyclerView.getContext(), (GestureDetector.OnGestureListener)this.mItemTouchHelperGestureListener);
  }
  
  private void stopGestureDetection() {
    ItemTouchHelperGestureListener itemTouchHelperGestureListener = this.mItemTouchHelperGestureListener;
    if (itemTouchHelperGestureListener != null) {
      itemTouchHelperGestureListener.doNotReactToLongPress();
      this.mItemTouchHelperGestureListener = null;
    } 
    if (this.mGestureDetector != null)
      this.mGestureDetector = null; 
  }
  
  private int swipeIfNecessary(RecyclerView.ViewHolder paramViewHolder) {
    if (this.mActionState == 2)
      return 0; 
    int i = this.mCallback.getMovementFlags(this.mRecyclerView, paramViewHolder);
    int j = (this.mCallback.convertToAbsoluteDirection(i, ViewCompat.getLayoutDirection((View)this.mRecyclerView)) & 0xFF00) >> 8;
    if (j == 0)
      return 0; 
    i = (0xFF00 & i) >> 8;
    if (Math.abs(this.mDx) > Math.abs(this.mDy)) {
      int k = checkHorizontalSwipe(paramViewHolder, j);
      if (k > 0)
        return ((i & k) == 0) ? Callback.convertToRelativeDirection(k, ViewCompat.getLayoutDirection((View)this.mRecyclerView)) : k; 
      i = checkVerticalSwipe(paramViewHolder, j);
      if (i > 0)
        return i; 
    } else {
      int k = checkVerticalSwipe(paramViewHolder, j);
      if (k > 0)
        return k; 
      j = checkHorizontalSwipe(paramViewHolder, j);
      if (j > 0)
        return ((i & j) == 0) ? Callback.convertToRelativeDirection(j, ViewCompat.getLayoutDirection((View)this.mRecyclerView)) : j; 
    } 
    return 0;
  }
  
  public void attachToRecyclerView(RecyclerView paramRecyclerView) {
    RecyclerView recyclerView = this.mRecyclerView;
    if (recyclerView == paramRecyclerView)
      return; 
    if (recyclerView != null)
      destroyCallbacks(); 
    this.mRecyclerView = paramRecyclerView;
    if (paramRecyclerView != null) {
      Resources resources = paramRecyclerView.getResources();
      this.mSwipeEscapeVelocity = resources.getDimension(R.dimen.item_touch_helper_swipe_escape_velocity);
      this.mMaxSwipeVelocity = resources.getDimension(R.dimen.item_touch_helper_swipe_escape_max_velocity);
      setupCallbacks();
    } 
  }
  
  void checkSelectForSwipe(int paramInt1, MotionEvent paramMotionEvent, int paramInt2) {
    if (this.mSelected != null || paramInt1 != 2 || this.mActionState == 2 || !this.mCallback.isItemViewSwipeEnabled())
      return; 
    if (this.mRecyclerView.getScrollState() == 1)
      return; 
    RecyclerView.ViewHolder viewHolder = findSwipedView(paramMotionEvent);
    if (viewHolder == null)
      return; 
    paramInt1 = (0xFF00 & this.mCallback.getAbsoluteMovementFlags(this.mRecyclerView, viewHolder)) >> 8;
    if (paramInt1 == 0)
      return; 
    float f1 = paramMotionEvent.getX(paramInt2);
    float f2 = paramMotionEvent.getY(paramInt2);
    f1 -= this.mInitialTouchX;
    f2 -= this.mInitialTouchY;
    float f4 = Math.abs(f1);
    float f3 = Math.abs(f2);
    paramInt2 = this.mSlop;
    if (f4 < paramInt2 && f3 < paramInt2)
      return; 
    if (f4 > f3) {
      if (f1 < 0.0F && (paramInt1 & 0x4) == 0)
        return; 
      if (f1 > 0.0F && (paramInt1 & 0x8) == 0)
        return; 
    } else {
      if (f2 < 0.0F && (paramInt1 & 0x1) == 0)
        return; 
      if (f2 > 0.0F && (paramInt1 & 0x2) == 0)
        return; 
    } 
    this.mDy = 0.0F;
    this.mDx = 0.0F;
    this.mActivePointerId = paramMotionEvent.getPointerId(0);
    select(viewHolder, 1);
  }
  
  void endRecoverAnimation(RecyclerView.ViewHolder paramViewHolder, boolean paramBoolean) {
    for (int i = this.mRecoverAnimations.size() - 1; i >= 0; i--) {
      RecoverAnimation recoverAnimation = this.mRecoverAnimations.get(i);
      if (recoverAnimation.mViewHolder == paramViewHolder) {
        recoverAnimation.mOverridden |= paramBoolean;
        if (!recoverAnimation.mEnded)
          recoverAnimation.cancel(); 
        this.mRecoverAnimations.remove(i);
        return;
      } 
    } 
  }
  
  RecoverAnimation findAnimation(MotionEvent paramMotionEvent) {
    if (this.mRecoverAnimations.isEmpty())
      return null; 
    View view = findChildView(paramMotionEvent);
    for (int i = this.mRecoverAnimations.size() - 1; i >= 0; i--) {
      RecoverAnimation recoverAnimation = this.mRecoverAnimations.get(i);
      if (recoverAnimation.mViewHolder.itemView == view)
        return recoverAnimation; 
    } 
    return null;
  }
  
  View findChildView(MotionEvent paramMotionEvent) {
    float f2 = paramMotionEvent.getX();
    float f1 = paramMotionEvent.getY();
    RecyclerView.ViewHolder viewHolder = this.mSelected;
    if (viewHolder != null) {
      View view = viewHolder.itemView;
      if (hitTest(view, f2, f1, this.mSelectedStartX + this.mDx, this.mSelectedStartY + this.mDy))
        return view; 
    } 
    for (int i = this.mRecoverAnimations.size() - 1; i >= 0; i--) {
      RecoverAnimation recoverAnimation = this.mRecoverAnimations.get(i);
      View view = recoverAnimation.mViewHolder.itemView;
      if (hitTest(view, f2, f1, recoverAnimation.mX, recoverAnimation.mY))
        return view; 
    } 
    return this.mRecyclerView.findChildViewUnder(f2, f1);
  }
  
  public void getItemOffsets(Rect paramRect, View paramView, RecyclerView paramRecyclerView, RecyclerView.State paramState) {
    paramRect.setEmpty();
  }
  
  boolean hasRunningRecoverAnim() {
    int i = this.mRecoverAnimations.size();
    for (byte b = 0; b < i; b++) {
      if (!((RecoverAnimation)this.mRecoverAnimations.get(b)).mEnded)
        return true; 
    } 
    return false;
  }
  
  void moveIfNecessary(RecyclerView.ViewHolder paramViewHolder) {
    if (this.mRecyclerView.isLayoutRequested())
      return; 
    if (this.mActionState != 2)
      return; 
    float f = this.mCallback.getMoveThreshold(paramViewHolder);
    int m = (int)(this.mSelectedStartX + this.mDx);
    int i = (int)(this.mSelectedStartY + this.mDy);
    if (Math.abs(i - paramViewHolder.itemView.getTop()) < paramViewHolder.itemView.getHeight() * f && Math.abs(m - paramViewHolder.itemView.getLeft()) < paramViewHolder.itemView.getWidth() * f)
      return; 
    List<RecyclerView.ViewHolder> list = findSwapTargets(paramViewHolder);
    if (list.size() == 0)
      return; 
    RecyclerView.ViewHolder viewHolder = this.mCallback.chooseDropTarget(paramViewHolder, list, m, i);
    if (viewHolder == null) {
      this.mSwapTargets.clear();
      this.mDistances.clear();
      return;
    } 
    int j = viewHolder.getAdapterPosition();
    int k = paramViewHolder.getAdapterPosition();
    if (this.mCallback.onMove(this.mRecyclerView, paramViewHolder, viewHolder))
      this.mCallback.onMoved(this.mRecyclerView, paramViewHolder, k, viewHolder, j, m, i); 
  }
  
  void obtainVelocityTracker() {
    VelocityTracker velocityTracker = this.mVelocityTracker;
    if (velocityTracker != null)
      velocityTracker.recycle(); 
    this.mVelocityTracker = VelocityTracker.obtain();
  }
  
  public void onChildViewAttachedToWindow(View paramView) {}
  
  public void onChildViewDetachedFromWindow(View paramView) {
    removeChildDrawingOrderCallbackIfNecessary(paramView);
    RecyclerView.ViewHolder viewHolder2 = this.mRecyclerView.getChildViewHolder(paramView);
    if (viewHolder2 == null)
      return; 
    RecyclerView.ViewHolder viewHolder1 = this.mSelected;
    if (viewHolder1 != null && viewHolder2 == viewHolder1) {
      select(null, 0);
    } else {
      endRecoverAnimation(viewHolder2, false);
      if (this.mPendingCleanup.remove(viewHolder2.itemView))
        this.mCallback.clearView(this.mRecyclerView, viewHolder2); 
    } 
  }
  
  public void onDraw(Canvas paramCanvas, RecyclerView paramRecyclerView, RecyclerView.State paramState) {
    this.mOverdrawChildPosition = -1;
    float f2 = 0.0F;
    float f1 = 0.0F;
    if (this.mSelected != null) {
      getSelectedDxDy(this.mTmpPosition);
      float[] arrayOfFloat = this.mTmpPosition;
      f2 = arrayOfFloat[0];
      f1 = arrayOfFloat[1];
    } 
    this.mCallback.onDraw(paramCanvas, paramRecyclerView, this.mSelected, this.mRecoverAnimations, this.mActionState, f2, f1);
  }
  
  public void onDrawOver(Canvas paramCanvas, RecyclerView paramRecyclerView, RecyclerView.State paramState) {
    float f1 = 0.0F;
    float f2 = 0.0F;
    if (this.mSelected != null) {
      getSelectedDxDy(this.mTmpPosition);
      float[] arrayOfFloat = this.mTmpPosition;
      f1 = arrayOfFloat[0];
      f2 = arrayOfFloat[1];
    } 
    this.mCallback.onDrawOver(paramCanvas, paramRecyclerView, this.mSelected, this.mRecoverAnimations, this.mActionState, f1, f2);
  }
  
  void postDispatchSwipe(final RecoverAnimation anim, final int swipeDir) {
    this.mRecyclerView.post(new Runnable() {
          final ItemTouchHelper this$0;
          
          final ItemTouchHelper.RecoverAnimation val$anim;
          
          final int val$swipeDir;
          
          public void run() {
            if (ItemTouchHelper.this.mRecyclerView != null && ItemTouchHelper.this.mRecyclerView.isAttachedToWindow() && !anim.mOverridden && anim.mViewHolder.getAdapterPosition() != -1) {
              RecyclerView.ItemAnimator itemAnimator = ItemTouchHelper.this.mRecyclerView.getItemAnimator();
              if ((itemAnimator == null || !itemAnimator.isRunning(null)) && !ItemTouchHelper.this.hasRunningRecoverAnim()) {
                ItemTouchHelper.this.mCallback.onSwiped(anim.mViewHolder, swipeDir);
              } else {
                ItemTouchHelper.this.mRecyclerView.post(this);
              } 
            } 
          }
        });
  }
  
  void removeChildDrawingOrderCallbackIfNecessary(View paramView) {
    if (paramView == this.mOverdrawChild) {
      this.mOverdrawChild = null;
      if (this.mChildDrawingOrderCallback != null)
        this.mRecyclerView.setChildDrawingOrderCallback((RecyclerView.ChildDrawingOrderCallback)null); 
    } 
  }
  
  boolean scrollIfNecessary() {
    if (this.mSelected == null) {
      this.mDragScrollStartTimeInMs = Long.MIN_VALUE;
      return false;
    } 
    long l2 = System.currentTimeMillis();
    long l1 = this.mDragScrollStartTimeInMs;
    if (l1 == Long.MIN_VALUE) {
      l1 = 0L;
    } else {
      l1 = l2 - l1;
    } 
    RecyclerView.LayoutManager layoutManager = this.mRecyclerView.getLayoutManager();
    if (this.mTmpRect == null)
      this.mTmpRect = new Rect(); 
    int j = 0;
    int k = 0;
    layoutManager.calculateItemDecorationsForChild(this.mSelected.itemView, this.mTmpRect);
    int i = j;
    if (layoutManager.canScrollHorizontally()) {
      int m = (int)(this.mSelectedStartX + this.mDx);
      i = m - this.mTmpRect.left - this.mRecyclerView.getPaddingLeft();
      float f = this.mDx;
      if (f >= 0.0F || i >= 0) {
        i = j;
        if (f > 0.0F) {
          m = this.mSelected.itemView.getWidth() + m + this.mTmpRect.right - this.mRecyclerView.getWidth() - this.mRecyclerView.getPaddingRight();
          i = j;
          if (m > 0)
            i = m; 
        } 
      } 
    } 
    j = k;
    if (layoutManager.canScrollVertically()) {
      int m = (int)(this.mSelectedStartY + this.mDy);
      j = m - this.mTmpRect.top - this.mRecyclerView.getPaddingTop();
      float f = this.mDy;
      if (f >= 0.0F || j >= 0) {
        j = k;
        if (f > 0.0F) {
          m = this.mSelected.itemView.getHeight() + m + this.mTmpRect.bottom - this.mRecyclerView.getHeight() - this.mRecyclerView.getPaddingBottom();
          j = k;
          if (m > 0)
            j = m; 
        } 
      } 
    } 
    k = i;
    if (i != 0)
      k = this.mCallback.interpolateOutOfBoundsScroll(this.mRecyclerView, this.mSelected.itemView.getWidth(), i, this.mRecyclerView.getWidth(), l1); 
    i = j;
    if (j != 0)
      i = this.mCallback.interpolateOutOfBoundsScroll(this.mRecyclerView, this.mSelected.itemView.getHeight(), j, this.mRecyclerView.getHeight(), l1); 
    if (k != 0 || i != 0) {
      if (this.mDragScrollStartTimeInMs == Long.MIN_VALUE)
        this.mDragScrollStartTimeInMs = l2; 
      this.mRecyclerView.scrollBy(k, i);
      return true;
    } 
    this.mDragScrollStartTimeInMs = Long.MIN_VALUE;
    return false;
  }
  
  void select(RecyclerView.ViewHolder paramViewHolder, int paramInt) {
    if (paramViewHolder == this.mSelected && paramInt == this.mActionState)
      return; 
    this.mDragScrollStartTimeInMs = Long.MIN_VALUE;
    int k = this.mActionState;
    endRecoverAnimation(paramViewHolder, true);
    this.mActionState = paramInt;
    if (paramInt == 2)
      if (paramViewHolder != null) {
        this.mOverdrawChild = paramViewHolder.itemView;
        addChildDrawingOrderCallback();
      } else {
        throw new IllegalArgumentException("Must pass a ViewHolder when dragging");
      }  
    int i = 0;
    final int swipeDir = 0;
    if (this.mSelected != null) {
      RecoverAnimation recoverAnimation;
      final RecyclerView.ViewHolder prevSelected = this.mSelected;
      if (viewHolder.itemView.getParent() != null) {
        float f1;
        float f2;
        if (k == 2) {
          j = 0;
        } else {
          j = swipeIfNecessary(viewHolder);
        } 
        releaseVelocityTracker();
        switch (j) {
          default:
            f1 = 0.0F;
            f2 = 0.0F;
            break;
          case 4:
          case 8:
          case 16:
          case 32:
            f3 = Math.signum(this.mDx);
            f2 = this.mRecyclerView.getWidth();
            f1 = 0.0F;
            f2 = f3 * f2;
            break;
          case 1:
          case 2:
            f1 = Math.signum(this.mDy) * this.mRecyclerView.getHeight();
            f2 = 0.0F;
            break;
        } 
        if (k == 2) {
          i = 8;
        } else if (j > 0) {
          i = 2;
        } else {
          i = 4;
        } 
        getSelectedDxDy(this.mTmpPosition);
        float[] arrayOfFloat = this.mTmpPosition;
        float f4 = arrayOfFloat[0];
        float f3 = arrayOfFloat[1];
        recoverAnimation = new RecoverAnimation(viewHolder, i, k, f4, f3, f2, f1) {
            final ItemTouchHelper this$0;
            
            final RecyclerView.ViewHolder val$prevSelected;
            
            final int val$swipeDir;
            
            public void onAnimationEnd(Animator param1Animator) {
              super.onAnimationEnd(param1Animator);
              if (this.mOverridden)
                return; 
              if (swipeDir <= 0) {
                ItemTouchHelper.this.mCallback.clearView(ItemTouchHelper.this.mRecyclerView, prevSelected);
              } else {
                ItemTouchHelper.this.mPendingCleanup.add(prevSelected.itemView);
                this.mIsPendingCleanup = true;
                int i = swipeDir;
                if (i > 0)
                  ItemTouchHelper.this.postDispatchSwipe(this, i); 
              } 
              if (ItemTouchHelper.this.mOverdrawChild == prevSelected.itemView)
                ItemTouchHelper.this.removeChildDrawingOrderCallbackIfNecessary(prevSelected.itemView); 
            }
          };
        recoverAnimation.setDuration(this.mCallback.getAnimationDuration(this.mRecyclerView, i, f2 - f4, f1 - f3));
        this.mRecoverAnimations.add(recoverAnimation);
        recoverAnimation.start();
        i = 1;
      } else {
        removeChildDrawingOrderCallbackIfNecessary(((RecyclerView.ViewHolder)recoverAnimation).itemView);
        this.mCallback.clearView(this.mRecyclerView, (RecyclerView.ViewHolder)recoverAnimation);
        i = j;
      } 
      this.mSelected = null;
    } 
    if (paramViewHolder != null) {
      this.mSelectedFlags = (this.mCallback.getAbsoluteMovementFlags(this.mRecyclerView, paramViewHolder) & (1 << paramInt * 8 + 8) - 1) >> this.mActionState * 8;
      this.mSelectedStartX = paramViewHolder.itemView.getLeft();
      this.mSelectedStartY = paramViewHolder.itemView.getTop();
      this.mSelected = paramViewHolder;
      if (paramInt == 2)
        paramViewHolder.itemView.performHapticFeedback(0); 
    } 
    ViewParent viewParent = this.mRecyclerView.getParent();
    if (viewParent != null) {
      boolean bool;
      if (this.mSelected != null) {
        bool = true;
      } else {
        bool = false;
      } 
      viewParent.requestDisallowInterceptTouchEvent(bool);
    } 
    if (i == 0)
      this.mRecyclerView.getLayoutManager().requestSimpleAnimationsInNextLayout(); 
    this.mCallback.onSelectedChanged(this.mSelected, this.mActionState);
    this.mRecyclerView.invalidate();
  }
  
  public void startDrag(RecyclerView.ViewHolder paramViewHolder) {
    if (!this.mCallback.hasDragFlag(this.mRecyclerView, paramViewHolder)) {
      Log.e("ItemTouchHelper", "Start drag has been called but dragging is not enabled");
      return;
    } 
    if (paramViewHolder.itemView.getParent() != this.mRecyclerView) {
      Log.e("ItemTouchHelper", "Start drag has been called with a view holder which is not a child of the RecyclerView which is controlled by this ItemTouchHelper.");
      return;
    } 
    obtainVelocityTracker();
    this.mDy = 0.0F;
    this.mDx = 0.0F;
    select(paramViewHolder, 2);
  }
  
  public void startSwipe(RecyclerView.ViewHolder paramViewHolder) {
    if (!this.mCallback.hasSwipeFlag(this.mRecyclerView, paramViewHolder)) {
      Log.e("ItemTouchHelper", "Start swipe has been called but swiping is not enabled");
      return;
    } 
    if (paramViewHolder.itemView.getParent() != this.mRecyclerView) {
      Log.e("ItemTouchHelper", "Start swipe has been called with a view holder which is not a child of the RecyclerView controlled by this ItemTouchHelper.");
      return;
    } 
    obtainVelocityTracker();
    this.mDy = 0.0F;
    this.mDx = 0.0F;
    select(paramViewHolder, 1);
  }
  
  void updateDxDy(MotionEvent paramMotionEvent, int paramInt1, int paramInt2) {
    float f2 = paramMotionEvent.getX(paramInt2);
    float f1 = paramMotionEvent.getY(paramInt2);
    f2 -= this.mInitialTouchX;
    this.mDx = f2;
    this.mDy = f1 - this.mInitialTouchY;
    if ((paramInt1 & 0x4) == 0)
      this.mDx = Math.max(0.0F, f2); 
    if ((paramInt1 & 0x8) == 0)
      this.mDx = Math.min(0.0F, this.mDx); 
    if ((paramInt1 & 0x1) == 0)
      this.mDy = Math.max(0.0F, this.mDy); 
    if ((paramInt1 & 0x2) == 0)
      this.mDy = Math.min(0.0F, this.mDy); 
  }
  
  public static abstract class Callback {
    private static final int ABS_HORIZONTAL_DIR_FLAGS = 789516;
    
    public static final int DEFAULT_DRAG_ANIMATION_DURATION = 200;
    
    public static final int DEFAULT_SWIPE_ANIMATION_DURATION = 250;
    
    private static final long DRAG_SCROLL_ACCELERATION_LIMIT_TIME_MS = 2000L;
    
    static final int RELATIVE_DIR_FLAGS = 3158064;
    
    private static final Interpolator sDragScrollInterpolator = new Interpolator() {
        public float getInterpolation(float param2Float) {
          return param2Float * param2Float * param2Float * param2Float * param2Float;
        }
      };
    
    private static final Interpolator sDragViewScrollCapInterpolator = new Interpolator() {
        public float getInterpolation(float param2Float) {
          param2Float--;
          return param2Float * param2Float * param2Float * param2Float * param2Float + 1.0F;
        }
      };
    
    private int mCachedMaxScrollSpeed = -1;
    
    public static int convertToRelativeDirection(int param1Int1, int param1Int2) {
      int i = param1Int1 & 0xC0C0C;
      if (i == 0)
        return param1Int1; 
      param1Int1 &= i ^ 0xFFFFFFFF;
      return (param1Int2 == 0) ? (param1Int1 | i << 2) : (param1Int1 | i << 1 & 0xFFF3F3F3 | (0xC0C0C & i << 1) << 2);
    }
    
    public static ItemTouchUIUtil getDefaultUIUtil() {
      return ItemTouchUIUtilImpl.INSTANCE;
    }
    
    private int getMaxDragScroll(RecyclerView param1RecyclerView) {
      if (this.mCachedMaxScrollSpeed == -1)
        this.mCachedMaxScrollSpeed = param1RecyclerView.getResources().getDimensionPixelSize(R.dimen.item_touch_helper_max_drag_scroll_per_frame); 
      return this.mCachedMaxScrollSpeed;
    }
    
    public static int makeFlag(int param1Int1, int param1Int2) {
      return param1Int2 << param1Int1 * 8;
    }
    
    public static int makeMovementFlags(int param1Int1, int param1Int2) {
      return makeFlag(0, param1Int2 | param1Int1) | makeFlag(1, param1Int2) | makeFlag(2, param1Int1);
    }
    
    public boolean canDropOver(RecyclerView param1RecyclerView, RecyclerView.ViewHolder param1ViewHolder1, RecyclerView.ViewHolder param1ViewHolder2) {
      return true;
    }
    
    public RecyclerView.ViewHolder chooseDropTarget(RecyclerView.ViewHolder param1ViewHolder, List<RecyclerView.ViewHolder> param1List, int param1Int1, int param1Int2) {
      int j = param1ViewHolder.itemView.getWidth();
      int i1 = param1ViewHolder.itemView.getHeight();
      RecyclerView.ViewHolder viewHolder = null;
      int i = -1;
      int n = param1Int1 - param1ViewHolder.itemView.getLeft();
      int k = param1Int2 - param1ViewHolder.itemView.getTop();
      int m = param1List.size();
      for (byte b = 0; b < m; b++) {
        RecyclerView.ViewHolder viewHolder1 = param1List.get(b);
        RecyclerView.ViewHolder viewHolder2 = viewHolder;
        int i2 = i;
        if (n > 0) {
          int i3 = viewHolder1.itemView.getRight() - param1Int1 + j;
          viewHolder2 = viewHolder;
          i2 = i;
          if (i3 < 0) {
            viewHolder2 = viewHolder;
            i2 = i;
            if (viewHolder1.itemView.getRight() > param1ViewHolder.itemView.getRight()) {
              i3 = Math.abs(i3);
              viewHolder2 = viewHolder;
              i2 = i;
              if (i3 > i) {
                i2 = i3;
                viewHolder2 = viewHolder1;
              } 
            } 
          } 
        } 
        viewHolder = viewHolder2;
        i = i2;
        if (n < 0) {
          int i3 = viewHolder1.itemView.getLeft() - param1Int1;
          viewHolder = viewHolder2;
          i = i2;
          if (i3 > 0) {
            viewHolder = viewHolder2;
            i = i2;
            if (viewHolder1.itemView.getLeft() < param1ViewHolder.itemView.getLeft()) {
              i3 = Math.abs(i3);
              viewHolder = viewHolder2;
              i = i2;
              if (i3 > i2) {
                i = i3;
                viewHolder = viewHolder1;
              } 
            } 
          } 
        } 
        viewHolder2 = viewHolder;
        i2 = i;
        if (k < 0) {
          int i3 = viewHolder1.itemView.getTop() - param1Int2;
          viewHolder2 = viewHolder;
          i2 = i;
          if (i3 > 0) {
            viewHolder2 = viewHolder;
            i2 = i;
            if (viewHolder1.itemView.getTop() < param1ViewHolder.itemView.getTop()) {
              i3 = Math.abs(i3);
              viewHolder2 = viewHolder;
              i2 = i;
              if (i3 > i) {
                i2 = i3;
                viewHolder2 = viewHolder1;
              } 
            } 
          } 
        } 
        viewHolder = viewHolder2;
        i = i2;
        if (k > 0) {
          int i3 = viewHolder1.itemView.getBottom() - param1Int2 + i1;
          viewHolder = viewHolder2;
          i = i2;
          if (i3 < 0) {
            viewHolder = viewHolder2;
            i = i2;
            if (viewHolder1.itemView.getBottom() > param1ViewHolder.itemView.getBottom()) {
              i3 = Math.abs(i3);
              viewHolder = viewHolder2;
              i = i2;
              if (i3 > i2) {
                i = i3;
                viewHolder = viewHolder1;
              } 
            } 
          } 
        } 
      } 
      return viewHolder;
    }
    
    public void clearView(RecyclerView param1RecyclerView, RecyclerView.ViewHolder param1ViewHolder) {
      ItemTouchUIUtilImpl.INSTANCE.clearView(param1ViewHolder.itemView);
    }
    
    public int convertToAbsoluteDirection(int param1Int1, int param1Int2) {
      int i = param1Int1 & 0x303030;
      if (i == 0)
        return param1Int1; 
      param1Int1 &= i ^ 0xFFFFFFFF;
      return (param1Int2 == 0) ? (param1Int1 | i >> 2) : (param1Int1 | i >> 1 & 0xFFCFCFCF | (0x303030 & i >> 1) >> 2);
    }
    
    final int getAbsoluteMovementFlags(RecyclerView param1RecyclerView, RecyclerView.ViewHolder param1ViewHolder) {
      return convertToAbsoluteDirection(getMovementFlags(param1RecyclerView, param1ViewHolder), ViewCompat.getLayoutDirection((View)param1RecyclerView));
    }
    
    public long getAnimationDuration(RecyclerView param1RecyclerView, int param1Int, float param1Float1, float param1Float2) {
      long l;
      RecyclerView.ItemAnimator itemAnimator = param1RecyclerView.getItemAnimator();
      if (itemAnimator == null) {
        if (param1Int == 8) {
          l = 200L;
        } else {
          l = 250L;
        } 
        return l;
      } 
      if (param1Int == 8) {
        l = itemAnimator.getMoveDuration();
      } else {
        l = itemAnimator.getRemoveDuration();
      } 
      return l;
    }
    
    public int getBoundingBoxMargin() {
      return 0;
    }
    
    public float getMoveThreshold(RecyclerView.ViewHolder param1ViewHolder) {
      return 0.5F;
    }
    
    public abstract int getMovementFlags(RecyclerView param1RecyclerView, RecyclerView.ViewHolder param1ViewHolder);
    
    public float getSwipeEscapeVelocity(float param1Float) {
      return param1Float;
    }
    
    public float getSwipeThreshold(RecyclerView.ViewHolder param1ViewHolder) {
      return 0.5F;
    }
    
    public float getSwipeVelocityThreshold(float param1Float) {
      return param1Float;
    }
    
    boolean hasDragFlag(RecyclerView param1RecyclerView, RecyclerView.ViewHolder param1ViewHolder) {
      boolean bool;
      if ((0xFF0000 & getAbsoluteMovementFlags(param1RecyclerView, param1ViewHolder)) != 0) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    boolean hasSwipeFlag(RecyclerView param1RecyclerView, RecyclerView.ViewHolder param1ViewHolder) {
      boolean bool;
      if ((0xFF00 & getAbsoluteMovementFlags(param1RecyclerView, param1ViewHolder)) != 0) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public int interpolateOutOfBoundsScroll(RecyclerView param1RecyclerView, int param1Int1, int param1Int2, int param1Int3, long param1Long) {
      param1Int3 = getMaxDragScroll(param1RecyclerView);
      int j = Math.abs(param1Int2);
      int i = (int)Math.signum(param1Int2);
      float f = Math.min(1.0F, j * 1.0F / param1Int1);
      param1Int1 = (int)((i * param1Int3) * sDragViewScrollCapInterpolator.getInterpolation(f));
      if (param1Long > 2000L) {
        f = 1.0F;
      } else {
        f = (float)param1Long / 2000.0F;
      } 
      param1Int1 = (int)(param1Int1 * sDragScrollInterpolator.getInterpolation(f));
      if (param1Int1 == 0) {
        if (param1Int2 > 0) {
          param1Int1 = 1;
        } else {
          param1Int1 = -1;
        } 
        return param1Int1;
      } 
      return param1Int1;
    }
    
    public boolean isItemViewSwipeEnabled() {
      return true;
    }
    
    public boolean isLongPressDragEnabled() {
      return true;
    }
    
    public void onChildDraw(Canvas param1Canvas, RecyclerView param1RecyclerView, RecyclerView.ViewHolder param1ViewHolder, float param1Float1, float param1Float2, int param1Int, boolean param1Boolean) {
      ItemTouchUIUtilImpl.INSTANCE.onDraw(param1Canvas, param1RecyclerView, param1ViewHolder.itemView, param1Float1, param1Float2, param1Int, param1Boolean);
    }
    
    public void onChildDrawOver(Canvas param1Canvas, RecyclerView param1RecyclerView, RecyclerView.ViewHolder param1ViewHolder, float param1Float1, float param1Float2, int param1Int, boolean param1Boolean) {
      ItemTouchUIUtilImpl.INSTANCE.onDrawOver(param1Canvas, param1RecyclerView, param1ViewHolder.itemView, param1Float1, param1Float2, param1Int, param1Boolean);
    }
    
    void onDraw(Canvas param1Canvas, RecyclerView param1RecyclerView, RecyclerView.ViewHolder param1ViewHolder, List<ItemTouchHelper.RecoverAnimation> param1List, int param1Int, float param1Float1, float param1Float2) {
      int j = param1List.size();
      int i;
      for (i = 0; i < j; i++) {
        ItemTouchHelper.RecoverAnimation recoverAnimation = param1List.get(i);
        recoverAnimation.update();
        int k = param1Canvas.save();
        onChildDraw(param1Canvas, param1RecyclerView, recoverAnimation.mViewHolder, recoverAnimation.mX, recoverAnimation.mY, recoverAnimation.mActionState, false);
        param1Canvas.restoreToCount(k);
      } 
      if (param1ViewHolder != null) {
        i = param1Canvas.save();
        onChildDraw(param1Canvas, param1RecyclerView, param1ViewHolder, param1Float1, param1Float2, param1Int, true);
        param1Canvas.restoreToCount(i);
      } 
    }
    
    void onDrawOver(Canvas param1Canvas, RecyclerView param1RecyclerView, RecyclerView.ViewHolder param1ViewHolder, List<ItemTouchHelper.RecoverAnimation> param1List, int param1Int, float param1Float1, float param1Float2) {
      int j = param1List.size();
      int i;
      for (i = 0; i < j; i++) {
        ItemTouchHelper.RecoverAnimation recoverAnimation = param1List.get(i);
        int k = param1Canvas.save();
        onChildDrawOver(param1Canvas, param1RecyclerView, recoverAnimation.mViewHolder, recoverAnimation.mX, recoverAnimation.mY, recoverAnimation.mActionState, false);
        param1Canvas.restoreToCount(k);
      } 
      if (param1ViewHolder != null) {
        i = param1Canvas.save();
        onChildDrawOver(param1Canvas, param1RecyclerView, param1ViewHolder, param1Float1, param1Float2, param1Int, true);
        param1Canvas.restoreToCount(i);
      } 
      i = 0;
      for (param1Int = j - 1; param1Int >= 0; param1Int--) {
        ItemTouchHelper.RecoverAnimation recoverAnimation = param1List.get(param1Int);
        if (recoverAnimation.mEnded && !recoverAnimation.mIsPendingCleanup) {
          param1List.remove(param1Int);
        } else if (!recoverAnimation.mEnded) {
          i = 1;
        } 
      } 
      if (i != 0)
        param1RecyclerView.invalidate(); 
    }
    
    public abstract boolean onMove(RecyclerView param1RecyclerView, RecyclerView.ViewHolder param1ViewHolder1, RecyclerView.ViewHolder param1ViewHolder2);
    
    public void onMoved(RecyclerView param1RecyclerView, RecyclerView.ViewHolder param1ViewHolder1, int param1Int1, RecyclerView.ViewHolder param1ViewHolder2, int param1Int2, int param1Int3, int param1Int4) {
      RecyclerView.LayoutManager layoutManager = param1RecyclerView.getLayoutManager();
      if (layoutManager instanceof ItemTouchHelper.ViewDropHandler) {
        ((ItemTouchHelper.ViewDropHandler)layoutManager).prepareForDrop(param1ViewHolder1.itemView, param1ViewHolder2.itemView, param1Int3, param1Int4);
        return;
      } 
      if (layoutManager.canScrollHorizontally()) {
        if (layoutManager.getDecoratedLeft(param1ViewHolder2.itemView) <= param1RecyclerView.getPaddingLeft())
          param1RecyclerView.scrollToPosition(param1Int2); 
        if (layoutManager.getDecoratedRight(param1ViewHolder2.itemView) >= param1RecyclerView.getWidth() - param1RecyclerView.getPaddingRight())
          param1RecyclerView.scrollToPosition(param1Int2); 
      } 
      if (layoutManager.canScrollVertically()) {
        if (layoutManager.getDecoratedTop(param1ViewHolder2.itemView) <= param1RecyclerView.getPaddingTop())
          param1RecyclerView.scrollToPosition(param1Int2); 
        if (layoutManager.getDecoratedBottom(param1ViewHolder2.itemView) >= param1RecyclerView.getHeight() - param1RecyclerView.getPaddingBottom())
          param1RecyclerView.scrollToPosition(param1Int2); 
      } 
    }
    
    public void onSelectedChanged(RecyclerView.ViewHolder param1ViewHolder, int param1Int) {
      if (param1ViewHolder != null)
        ItemTouchUIUtilImpl.INSTANCE.onSelected(param1ViewHolder.itemView); 
    }
    
    public abstract void onSwiped(RecyclerView.ViewHolder param1ViewHolder, int param1Int);
  }
  
  static final class null implements Interpolator {
    public float getInterpolation(float param1Float) {
      return param1Float * param1Float * param1Float * param1Float * param1Float;
    }
  }
  
  static final class null implements Interpolator {
    public float getInterpolation(float param1Float) {
      param1Float--;
      return param1Float * param1Float * param1Float * param1Float * param1Float + 1.0F;
    }
  }
  
  private class ItemTouchHelperGestureListener extends GestureDetector.SimpleOnGestureListener {
    private boolean mShouldReactToLongPress = true;
    
    final ItemTouchHelper this$0;
    
    void doNotReactToLongPress() {
      this.mShouldReactToLongPress = false;
    }
    
    public boolean onDown(MotionEvent param1MotionEvent) {
      return true;
    }
    
    public void onLongPress(MotionEvent param1MotionEvent) {
      if (!this.mShouldReactToLongPress)
        return; 
      View view = ItemTouchHelper.this.findChildView(param1MotionEvent);
      if (view != null) {
        RecyclerView.ViewHolder viewHolder = ItemTouchHelper.this.mRecyclerView.getChildViewHolder(view);
        if (viewHolder != null) {
          if (!ItemTouchHelper.this.mCallback.hasDragFlag(ItemTouchHelper.this.mRecyclerView, viewHolder))
            return; 
          if (param1MotionEvent.getPointerId(0) == ItemTouchHelper.this.mActivePointerId) {
            int i = param1MotionEvent.findPointerIndex(ItemTouchHelper.this.mActivePointerId);
            float f1 = param1MotionEvent.getX(i);
            float f2 = param1MotionEvent.getY(i);
            ItemTouchHelper.this.mInitialTouchX = f1;
            ItemTouchHelper.this.mInitialTouchY = f2;
            ItemTouchHelper itemTouchHelper = ItemTouchHelper.this;
            itemTouchHelper.mDy = 0.0F;
            itemTouchHelper.mDx = 0.0F;
            if (ItemTouchHelper.this.mCallback.isLongPressDragEnabled())
              ItemTouchHelper.this.select(viewHolder, 2); 
          } 
        } 
      } 
    }
  }
  
  private static class RecoverAnimation implements Animator.AnimatorListener {
    final int mActionState;
    
    final int mAnimationType;
    
    boolean mEnded = false;
    
    private float mFraction;
    
    boolean mIsPendingCleanup;
    
    boolean mOverridden = false;
    
    final float mStartDx;
    
    final float mStartDy;
    
    final float mTargetX;
    
    final float mTargetY;
    
    private final ValueAnimator mValueAnimator;
    
    final RecyclerView.ViewHolder mViewHolder;
    
    float mX;
    
    float mY;
    
    RecoverAnimation(RecyclerView.ViewHolder param1ViewHolder, int param1Int1, int param1Int2, float param1Float1, float param1Float2, float param1Float3, float param1Float4) {
      this.mActionState = param1Int2;
      this.mAnimationType = param1Int1;
      this.mViewHolder = param1ViewHolder;
      this.mStartDx = param1Float1;
      this.mStartDy = param1Float2;
      this.mTargetX = param1Float3;
      this.mTargetY = param1Float4;
      ValueAnimator valueAnimator = ValueAnimator.ofFloat(new float[] { 0.0F, 1.0F });
      this.mValueAnimator = valueAnimator;
      valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            final ItemTouchHelper.RecoverAnimation this$0;
            
            public void onAnimationUpdate(ValueAnimator param2ValueAnimator) {
              ItemTouchHelper.RecoverAnimation.this.setFraction(param2ValueAnimator.getAnimatedFraction());
            }
          });
      valueAnimator.setTarget(param1ViewHolder.itemView);
      valueAnimator.addListener(this);
      setFraction(0.0F);
    }
    
    public void cancel() {
      this.mValueAnimator.cancel();
    }
    
    public void onAnimationCancel(Animator param1Animator) {
      setFraction(1.0F);
    }
    
    public void onAnimationEnd(Animator param1Animator) {
      if (!this.mEnded)
        this.mViewHolder.setIsRecyclable(true); 
      this.mEnded = true;
    }
    
    public void onAnimationRepeat(Animator param1Animator) {}
    
    public void onAnimationStart(Animator param1Animator) {}
    
    public void setDuration(long param1Long) {
      this.mValueAnimator.setDuration(param1Long);
    }
    
    public void setFraction(float param1Float) {
      this.mFraction = param1Float;
    }
    
    public void start() {
      this.mViewHolder.setIsRecyclable(false);
      this.mValueAnimator.start();
    }
    
    public void update() {
      float f1 = this.mStartDx;
      float f2 = this.mTargetX;
      if (f1 == f2) {
        this.mX = this.mViewHolder.itemView.getTranslationX();
      } else {
        this.mX = f1 + this.mFraction * (f2 - f1);
      } 
      f1 = this.mStartDy;
      f2 = this.mTargetY;
      if (f1 == f2) {
        this.mY = this.mViewHolder.itemView.getTranslationY();
      } else {
        this.mY = f1 + this.mFraction * (f2 - f1);
      } 
    }
  }
  
  class null implements ValueAnimator.AnimatorUpdateListener {
    final ItemTouchHelper.RecoverAnimation this$0;
    
    public void onAnimationUpdate(ValueAnimator param1ValueAnimator) {
      this.this$0.setFraction(param1ValueAnimator.getAnimatedFraction());
    }
  }
  
  public static abstract class SimpleCallback extends Callback {
    private int mDefaultDragDirs;
    
    private int mDefaultSwipeDirs;
    
    public SimpleCallback(int param1Int1, int param1Int2) {
      this.mDefaultSwipeDirs = param1Int2;
      this.mDefaultDragDirs = param1Int1;
    }
    
    public int getDragDirs(RecyclerView param1RecyclerView, RecyclerView.ViewHolder param1ViewHolder) {
      return this.mDefaultDragDirs;
    }
    
    public int getMovementFlags(RecyclerView param1RecyclerView, RecyclerView.ViewHolder param1ViewHolder) {
      return makeMovementFlags(getDragDirs(param1RecyclerView, param1ViewHolder), getSwipeDirs(param1RecyclerView, param1ViewHolder));
    }
    
    public int getSwipeDirs(RecyclerView param1RecyclerView, RecyclerView.ViewHolder param1ViewHolder) {
      return this.mDefaultSwipeDirs;
    }
    
    public void setDefaultDragDirs(int param1Int) {
      this.mDefaultDragDirs = param1Int;
    }
    
    public void setDefaultSwipeDirs(int param1Int) {
      this.mDefaultSwipeDirs = param1Int;
    }
  }
  
  public static interface ViewDropHandler {
    void prepareForDrop(View param1View1, View param1View2, int param1Int1, int param1Int2);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\recyclerview\widget\ItemTouchHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */