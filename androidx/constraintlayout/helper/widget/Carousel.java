package androidx.constraintlayout.helper.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import androidx.constraintlayout.motion.widget.MotionHelper;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.constraintlayout.motion.widget.MotionScene;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.R;
import java.util.ArrayList;
import java.util.Iterator;

public class Carousel extends MotionHelper {
  private static final boolean DEBUG = false;
  
  private static final String TAG = "Carousel";
  
  public static final int TOUCH_UP_CARRY_ON = 2;
  
  public static final int TOUCH_UP_IMMEDIATE_STOP = 1;
  
  private int backwardTransition = -1;
  
  private float dampening = 0.9F;
  
  private int emptyViewBehavior = 4;
  
  private int firstViewReference = -1;
  
  private int forwardTransition = -1;
  
  private boolean infiniteCarousel = false;
  
  private Adapter mAdapter = null;
  
  private int mAnimateTargetDelay = 200;
  
  private int mIndex = 0;
  
  int mLastStartId = -1;
  
  private final ArrayList<View> mList = new ArrayList<>();
  
  private MotionLayout mMotionLayout;
  
  private int mPreviousIndex = 0;
  
  private int mTargetIndex = -1;
  
  Runnable mUpdateRunnable = new Runnable() {
      final Carousel this$0;
      
      public void run() {
        Carousel.this.mMotionLayout.setProgress(0.0F);
        Carousel.this.updateItems();
        Carousel.this.mAdapter.onNewItem(Carousel.this.mIndex);
        float f = Carousel.this.mMotionLayout.getVelocity();
        if (Carousel.this.touchUpMode == 2 && f > Carousel.this.velocityThreshold && Carousel.this.mIndex < Carousel.this.mAdapter.count() - 1) {
          float f1 = Carousel.this.dampening;
          if (Carousel.this.mIndex == 0 && Carousel.this.mPreviousIndex > Carousel.this.mIndex)
            return; 
          if (Carousel.this.mIndex == Carousel.this.mAdapter.count() - 1 && Carousel.this.mPreviousIndex < Carousel.this.mIndex)
            return; 
          Carousel.this.mMotionLayout.post(new Runnable() {
                final Carousel.null this$1;
                
                final float val$v;
                
                public void run() {
                  Carousel.this.mMotionLayout.touchAnimateTo(5, 1.0F, v);
                }
              });
        } 
      }
    };
  
  private int nextState = -1;
  
  private int previousState = -1;
  
  private int startIndex = 0;
  
  private int touchUpMode = 1;
  
  private float velocityThreshold = 2.0F;
  
  public Carousel(Context paramContext) {
    super(paramContext);
  }
  
  public Carousel(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    init(paramContext, paramAttributeSet);
  }
  
  public Carousel(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    init(paramContext, paramAttributeSet);
  }
  
  private void enableAllTransitions(boolean paramBoolean) {
    Iterator<MotionScene.Transition> iterator = this.mMotionLayout.getDefinedTransitions().iterator();
    while (iterator.hasNext())
      ((MotionScene.Transition)iterator.next()).setEnabled(paramBoolean); 
  }
  
  private boolean enableTransition(int paramInt, boolean paramBoolean) {
    if (paramInt == -1)
      return false; 
    MotionLayout motionLayout = this.mMotionLayout;
    if (motionLayout == null)
      return false; 
    MotionScene.Transition transition = motionLayout.getTransition(paramInt);
    if (transition == null)
      return false; 
    if (paramBoolean == transition.isEnabled())
      return false; 
    transition.setEnabled(paramBoolean);
    return true;
  }
  
  private void init(Context paramContext, AttributeSet paramAttributeSet) {
    if (paramAttributeSet != null) {
      TypedArray typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.Carousel);
      int i = typedArray.getIndexCount();
      for (byte b = 0; b < i; b++) {
        int j = typedArray.getIndex(b);
        if (j == R.styleable.Carousel_carousel_firstView) {
          this.firstViewReference = typedArray.getResourceId(j, this.firstViewReference);
        } else if (j == R.styleable.Carousel_carousel_backwardTransition) {
          this.backwardTransition = typedArray.getResourceId(j, this.backwardTransition);
        } else if (j == R.styleable.Carousel_carousel_forwardTransition) {
          this.forwardTransition = typedArray.getResourceId(j, this.forwardTransition);
        } else if (j == R.styleable.Carousel_carousel_emptyViewsBehavior) {
          this.emptyViewBehavior = typedArray.getInt(j, this.emptyViewBehavior);
        } else if (j == R.styleable.Carousel_carousel_previousState) {
          this.previousState = typedArray.getResourceId(j, this.previousState);
        } else if (j == R.styleable.Carousel_carousel_nextState) {
          this.nextState = typedArray.getResourceId(j, this.nextState);
        } else if (j == R.styleable.Carousel_carousel_touchUp_dampeningFactor) {
          this.dampening = typedArray.getFloat(j, this.dampening);
        } else if (j == R.styleable.Carousel_carousel_touchUpMode) {
          this.touchUpMode = typedArray.getInt(j, this.touchUpMode);
        } else if (j == R.styleable.Carousel_carousel_touchUp_velocityThreshold) {
          this.velocityThreshold = typedArray.getFloat(j, this.velocityThreshold);
        } else if (j == R.styleable.Carousel_carousel_infinite) {
          this.infiniteCarousel = typedArray.getBoolean(j, this.infiniteCarousel);
        } 
      } 
      typedArray.recycle();
    } 
  }
  
  private void updateItems() {
    Adapter adapter = this.mAdapter;
    if (adapter == null)
      return; 
    if (this.mMotionLayout == null)
      return; 
    if (adapter.count() == 0)
      return; 
    int j = this.mList.size();
    for (byte b = 0; b < j; b++) {
      View view = this.mList.get(b);
      int k = this.mIndex + b - this.startIndex;
      if (this.infiniteCarousel) {
        if (k < 0) {
          int m = this.emptyViewBehavior;
          if (m != 4) {
            updateViewVisibility(view, m);
          } else {
            updateViewVisibility(view, 0);
          } 
          if (k % this.mAdapter.count() == 0) {
            this.mAdapter.populate(view, 0);
          } else {
            Adapter adapter1 = this.mAdapter;
            adapter1.populate(view, adapter1.count() + k % this.mAdapter.count());
          } 
        } else if (k >= this.mAdapter.count()) {
          int m;
          if (k == this.mAdapter.count()) {
            m = 0;
          } else {
            m = k;
            if (k > this.mAdapter.count())
              m = k % this.mAdapter.count(); 
          } 
          k = this.emptyViewBehavior;
          if (k != 4) {
            updateViewVisibility(view, k);
          } else {
            updateViewVisibility(view, 0);
          } 
          this.mAdapter.populate(view, m);
        } else {
          updateViewVisibility(view, 0);
          this.mAdapter.populate(view, k);
        } 
      } else if (k < 0) {
        updateViewVisibility(view, this.emptyViewBehavior);
      } else if (k >= this.mAdapter.count()) {
        updateViewVisibility(view, this.emptyViewBehavior);
      } else {
        updateViewVisibility(view, 0);
        this.mAdapter.populate(view, k);
      } 
    } 
    int i = this.mTargetIndex;
    if (i != -1 && i != this.mIndex) {
      this.mMotionLayout.post(new Carousel$$ExternalSyntheticLambda0(this));
    } else if (i == this.mIndex) {
      this.mTargetIndex = -1;
    } 
    if (this.backwardTransition == -1 || this.forwardTransition == -1) {
      Log.w("Carousel", "No backward or forward transitions defined for Carousel!");
      return;
    } 
    if (this.infiniteCarousel)
      return; 
    i = this.mAdapter.count();
    if (this.mIndex == 0) {
      enableTransition(this.backwardTransition, false);
    } else {
      enableTransition(this.backwardTransition, true);
      this.mMotionLayout.setTransition(this.backwardTransition);
    } 
    if (this.mIndex == i - 1) {
      enableTransition(this.forwardTransition, false);
    } else {
      enableTransition(this.forwardTransition, true);
      this.mMotionLayout.setTransition(this.forwardTransition);
    } 
  }
  
  private boolean updateViewVisibility(int paramInt1, View paramView, int paramInt2) {
    ConstraintSet constraintSet = this.mMotionLayout.getConstraintSet(paramInt1);
    if (constraintSet == null)
      return false; 
    ConstraintSet.Constraint constraint = constraintSet.getConstraint(paramView.getId());
    if (constraint == null)
      return false; 
    constraint.propertySet.mVisibilityMode = 1;
    paramView.setVisibility(paramInt2);
    return true;
  }
  
  private boolean updateViewVisibility(View paramView, int paramInt) {
    MotionLayout motionLayout = this.mMotionLayout;
    if (motionLayout == null)
      return false; 
    boolean bool = false;
    int[] arrayOfInt = motionLayout.getConstraintSetIds();
    for (byte b = 0; b < arrayOfInt.length; b++)
      bool |= updateViewVisibility(arrayOfInt[b], paramView, paramInt); 
    return bool;
  }
  
  public int getCount() {
    Adapter adapter = this.mAdapter;
    return (adapter != null) ? adapter.count() : 0;
  }
  
  public int getCurrentIndex() {
    return this.mIndex;
  }
  
  public void jumpToIndex(int paramInt) {
    this.mIndex = Math.max(0, Math.min(getCount() - 1, paramInt));
    refresh();
  }
  
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    if (getParent() instanceof MotionLayout) {
      MotionLayout motionLayout = (MotionLayout)getParent();
      for (byte b = 0; b < this.mCount; b++) {
        int i = this.mIds[b];
        View view = motionLayout.getViewById(i);
        if (this.firstViewReference == i)
          this.startIndex = b; 
        this.mList.add(view);
      } 
      this.mMotionLayout = motionLayout;
      if (this.touchUpMode == 2) {
        MotionScene.Transition transition = motionLayout.getTransition(this.forwardTransition);
        if (transition != null)
          transition.setOnTouchUp(5); 
        transition = this.mMotionLayout.getTransition(this.backwardTransition);
        if (transition != null)
          transition.setOnTouchUp(5); 
      } 
      updateItems();
      return;
    } 
  }
  
  public void onTransitionChange(MotionLayout paramMotionLayout, int paramInt1, int paramInt2, float paramFloat) {
    this.mLastStartId = paramInt1;
  }
  
  public void onTransitionCompleted(MotionLayout paramMotionLayout, int paramInt) {
    int i = this.mIndex;
    this.mPreviousIndex = i;
    if (paramInt == this.nextState) {
      this.mIndex = i + 1;
    } else if (paramInt == this.previousState) {
      this.mIndex = i - 1;
    } 
    if (this.infiniteCarousel) {
      if (this.mIndex >= this.mAdapter.count())
        this.mIndex = 0; 
      if (this.mIndex < 0)
        this.mIndex = this.mAdapter.count() - 1; 
    } else {
      if (this.mIndex >= this.mAdapter.count())
        this.mIndex = this.mAdapter.count() - 1; 
      if (this.mIndex < 0)
        this.mIndex = 0; 
    } 
    if (this.mPreviousIndex != this.mIndex)
      this.mMotionLayout.post(this.mUpdateRunnable); 
  }
  
  public void refresh() {
    int i = this.mList.size();
    for (byte b = 0; b < i; b++) {
      View view = this.mList.get(b);
      if (this.mAdapter.count() == 0) {
        updateViewVisibility(view, this.emptyViewBehavior);
      } else {
        updateViewVisibility(view, 0);
      } 
    } 
    this.mMotionLayout.rebuildScene();
    updateItems();
  }
  
  public void setAdapter(Adapter paramAdapter) {
    this.mAdapter = paramAdapter;
  }
  
  public void transitionToIndex(int paramInt1, int paramInt2) {
    this.mTargetIndex = Math.max(0, Math.min(getCount() - 1, paramInt1));
    paramInt2 = Math.max(0, paramInt2);
    this.mAnimateTargetDelay = paramInt2;
    this.mMotionLayout.setTransitionDuration(paramInt2);
    if (paramInt1 < this.mIndex) {
      this.mMotionLayout.transitionToState(this.previousState, this.mAnimateTargetDelay);
    } else {
      this.mMotionLayout.transitionToState(this.nextState, this.mAnimateTargetDelay);
    } 
  }
  
  public static interface Adapter {
    int count();
    
    void onNewItem(int param1Int);
    
    void populate(View param1View, int param1Int);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\helper\widget\Carousel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */