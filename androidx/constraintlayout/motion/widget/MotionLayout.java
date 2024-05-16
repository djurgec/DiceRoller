package androidx.constraintlayout.motion.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.view.Display;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.TextView;
import androidx.constraintlayout.core.motion.utils.KeyCache;
import androidx.constraintlayout.core.widgets.Barrier;
import androidx.constraintlayout.core.widgets.ConstraintAnchor;
import androidx.constraintlayout.core.widgets.ConstraintWidget;
import androidx.constraintlayout.core.widgets.ConstraintWidgetContainer;
import androidx.constraintlayout.core.widgets.Flow;
import androidx.constraintlayout.core.widgets.Guideline;
import androidx.constraintlayout.core.widgets.Helper;
import androidx.constraintlayout.core.widgets.HelperWidget;
import androidx.constraintlayout.core.widgets.Placeholder;
import androidx.constraintlayout.core.widgets.VirtualLayout;
import androidx.constraintlayout.motion.utils.StopLogic;
import androidx.constraintlayout.motion.utils.ViewState;
import androidx.constraintlayout.widget.Barrier;
import androidx.constraintlayout.widget.ConstraintHelper;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Constraints;
import androidx.constraintlayout.widget.R;
import androidx.core.view.NestedScrollingParent3;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class MotionLayout extends ConstraintLayout implements NestedScrollingParent3 {
  private static final boolean DEBUG = false;
  
  public static final int DEBUG_SHOW_NONE = 0;
  
  public static final int DEBUG_SHOW_PATH = 2;
  
  public static final int DEBUG_SHOW_PROGRESS = 1;
  
  private static final float EPSILON = 1.0E-5F;
  
  public static boolean IS_IN_EDIT_MODE = false;
  
  static final int MAX_KEY_FRAMES = 50;
  
  static final String TAG = "MotionLayout";
  
  public static final int TOUCH_UP_COMPLETE = 0;
  
  public static final int TOUCH_UP_COMPLETE_TO_END = 2;
  
  public static final int TOUCH_UP_COMPLETE_TO_START = 1;
  
  public static final int TOUCH_UP_DECELERATE = 4;
  
  public static final int TOUCH_UP_DECELERATE_AND_COMPLETE = 5;
  
  public static final int TOUCH_UP_NEVER_TO_END = 7;
  
  public static final int TOUCH_UP_NEVER_TO_START = 6;
  
  public static final int TOUCH_UP_STOP = 3;
  
  public static final int VELOCITY_LAYOUT = 1;
  
  public static final int VELOCITY_POST_LAYOUT = 0;
  
  public static final int VELOCITY_STATIC_LAYOUT = 3;
  
  public static final int VELOCITY_STATIC_POST_LAYOUT = 2;
  
  boolean firstDown = true;
  
  private float lastPos;
  
  private float lastY;
  
  private long mAnimationStartTime = 0L;
  
  private int mBeginState = -1;
  
  private RectF mBoundsCheck = new RectF();
  
  int mCurrentState = -1;
  
  int mDebugPath = 0;
  
  private DecelerateInterpolator mDecelerateLogic = new DecelerateInterpolator();
  
  private ArrayList<MotionHelper> mDecoratorsHelpers = null;
  
  private boolean mDelayedApply = false;
  
  private DesignTool mDesignTool;
  
  DevModeDraw mDevModeDraw;
  
  private int mEndState = -1;
  
  int mEndWrapHeight;
  
  int mEndWrapWidth;
  
  HashMap<View, MotionController> mFrameArrayList = new HashMap<>();
  
  private int mFrames = 0;
  
  int mHeightMeasureMode;
  
  private boolean mInLayout = false;
  
  private boolean mInRotation = false;
  
  boolean mInTransition = false;
  
  boolean mIndirectTransition = false;
  
  private boolean mInteractionEnabled = true;
  
  Interpolator mInterpolator;
  
  private Matrix mInverseMatrix = null;
  
  boolean mIsAnimating = false;
  
  private boolean mKeepAnimating = false;
  
  private KeyCache mKeyCache = new KeyCache();
  
  private long mLastDrawTime = -1L;
  
  private float mLastFps = 0.0F;
  
  private int mLastHeightMeasureSpec = 0;
  
  int mLastLayoutHeight;
  
  int mLastLayoutWidth;
  
  float mLastVelocity = 0.0F;
  
  private int mLastWidthMeasureSpec = 0;
  
  private float mListenerPosition = 0.0F;
  
  private int mListenerState = 0;
  
  protected boolean mMeasureDuringTransition = false;
  
  Model mModel = new Model();
  
  private boolean mNeedsFireTransitionCompleted = false;
  
  int mOldHeight;
  
  int mOldWidth;
  
  private Runnable mOnComplete = null;
  
  private ArrayList<MotionHelper> mOnHideHelpers = null;
  
  private ArrayList<MotionHelper> mOnShowHelpers = null;
  
  float mPostInterpolationPosition;
  
  HashMap<View, ViewState> mPreRotate = new HashMap<>();
  
  private int mPreRotateHeight;
  
  private int mPreRotateWidth;
  
  private int mPreviouseRotation;
  
  Interpolator mProgressInterpolator = null;
  
  private View mRegionView = null;
  
  int mRotatMode = 0;
  
  MotionScene mScene;
  
  private int[] mScheduledTransitionTo = null;
  
  int mScheduledTransitions = 0;
  
  float mScrollTargetDT;
  
  float mScrollTargetDX;
  
  float mScrollTargetDY;
  
  long mScrollTargetTime;
  
  int mStartWrapHeight;
  
  int mStartWrapWidth;
  
  private StateCache mStateCache;
  
  private StopLogic mStopLogic = new StopLogic();
  
  Rect mTempRect = new Rect();
  
  private boolean mTemporalInterpolator = false;
  
  ArrayList<Integer> mTransitionCompleted = new ArrayList<>();
  
  private float mTransitionDuration = 1.0F;
  
  float mTransitionGoalPosition = 0.0F;
  
  private boolean mTransitionInstantly;
  
  float mTransitionLastPosition = 0.0F;
  
  private long mTransitionLastTime;
  
  private TransitionListener mTransitionListener;
  
  private CopyOnWriteArrayList<TransitionListener> mTransitionListeners = null;
  
  float mTransitionPosition = 0.0F;
  
  TransitionState mTransitionState = TransitionState.UNDEFINED;
  
  boolean mUndergoingMotion = false;
  
  int mWidthMeasureMode;
  
  public MotionLayout(Context paramContext) {
    super(paramContext);
    init((AttributeSet)null);
  }
  
  public MotionLayout(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    init(paramAttributeSet);
  }
  
  public MotionLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    init(paramAttributeSet);
  }
  
  private boolean callTransformedTouchEvent(View paramView, MotionEvent paramMotionEvent, float paramFloat1, float paramFloat2) {
    Matrix matrix = paramView.getMatrix();
    if (matrix.isIdentity()) {
      paramMotionEvent.offsetLocation(paramFloat1, paramFloat2);
      boolean bool1 = paramView.onTouchEvent(paramMotionEvent);
      paramMotionEvent.offsetLocation(-paramFloat1, -paramFloat2);
      return bool1;
    } 
    paramMotionEvent = MotionEvent.obtain(paramMotionEvent);
    paramMotionEvent.offsetLocation(paramFloat1, paramFloat2);
    if (this.mInverseMatrix == null)
      this.mInverseMatrix = new Matrix(); 
    matrix.invert(this.mInverseMatrix);
    paramMotionEvent.transform(this.mInverseMatrix);
    boolean bool = paramView.onTouchEvent(paramMotionEvent);
    paramMotionEvent.recycle();
    return bool;
  }
  
  private void checkStructure() {
    MotionScene motionScene = this.mScene;
    if (motionScene == null) {
      Log.e("MotionLayout", "CHECK: motion scene not set! set \"app:layoutDescription=\"@xml/file\"");
      return;
    } 
    int i = motionScene.getStartId();
    motionScene = this.mScene;
    checkStructure(i, motionScene.getConstraintSet(motionScene.getStartId()));
    SparseIntArray sparseIntArray2 = new SparseIntArray();
    SparseIntArray sparseIntArray1 = new SparseIntArray();
    for (MotionScene.Transition transition : this.mScene.getDefinedTransitions()) {
      if (transition == this.mScene.mCurrentTransition)
        Log.v("MotionLayout", "CHECK: CURRENT"); 
      checkStructure(transition);
      int j = transition.getStartConstraintSetId();
      i = transition.getEndConstraintSetId();
      String str2 = Debug.getName(getContext(), j);
      String str1 = Debug.getName(getContext(), i);
      if (sparseIntArray2.get(j) == i)
        Log.e("MotionLayout", (new StringBuilder(String.valueOf(str2).length() + 53 + String.valueOf(str1).length())).append("CHECK: two transitions with the same start and end ").append(str2).append("->").append(str1).toString()); 
      if (sparseIntArray1.get(i) == j)
        Log.e("MotionLayout", (new StringBuilder(String.valueOf(str2).length() + 43 + String.valueOf(str1).length())).append("CHECK: you can't have reverse transitions").append(str2).append("->").append(str1).toString()); 
      sparseIntArray2.put(j, i);
      sparseIntArray1.put(i, j);
      if (this.mScene.getConstraintSet(j) == null) {
        str1 = String.valueOf(str2);
        if (str1.length() != 0) {
          str1 = " no such constraintSetStart ".concat(str1);
        } else {
          str1 = new String(" no such constraintSetStart ");
        } 
        Log.e("MotionLayout", str1);
      } 
      if (this.mScene.getConstraintSet(i) == null) {
        str1 = String.valueOf(str2);
        if (str1.length() != 0) {
          str1 = " no such constraintSetEnd ".concat(str1);
        } else {
          str1 = new String(" no such constraintSetEnd ");
        } 
        Log.e("MotionLayout", str1);
      } 
    } 
  }
  
  private void checkStructure(int paramInt, ConstraintSet paramConstraintSet) {
    String str = Debug.getName(getContext(), paramInt);
    int i = getChildCount();
    for (paramInt = 0; paramInt < i; paramInt++) {
      View view = getChildAt(paramInt);
      int j = view.getId();
      if (j == -1) {
        String str1 = view.getClass().getName();
        Log.w("MotionLayout", (new StringBuilder(String.valueOf(str).length() + 45 + String.valueOf(str1).length())).append("CHECK: ").append(str).append(" ALL VIEWS SHOULD HAVE ID's ").append(str1).append(" does not!").toString());
      } 
      if (paramConstraintSet.getConstraint(j) == null) {
        String str1 = Debug.getName(view);
        Log.w("MotionLayout", (new StringBuilder(String.valueOf(str).length() + 27 + String.valueOf(str1).length())).append("CHECK: ").append(str).append(" NO CONSTRAINTS for ").append(str1).toString());
      } 
    } 
    int[] arrayOfInt = paramConstraintSet.getKnownIds();
    for (paramInt = 0; paramInt < arrayOfInt.length; paramInt++) {
      i = arrayOfInt[paramInt];
      String str1 = Debug.getName(getContext(), i);
      if (findViewById(arrayOfInt[paramInt]) == null)
        Log.w("MotionLayout", (new StringBuilder(String.valueOf(str).length() + 27 + String.valueOf(str1).length())).append("CHECK: ").append(str).append(" NO View matches id ").append(str1).toString()); 
      if (paramConstraintSet.getHeight(i) == -1)
        Log.w("MotionLayout", (new StringBuilder(String.valueOf(str).length() + 26 + String.valueOf(str1).length())).append("CHECK: ").append(str).append("(").append(str1).append(") no LAYOUT_HEIGHT").toString()); 
      if (paramConstraintSet.getWidth(i) == -1)
        Log.w("MotionLayout", (new StringBuilder(String.valueOf(str).length() + 26 + String.valueOf(str1).length())).append("CHECK: ").append(str).append("(").append(str1).append(") no LAYOUT_HEIGHT").toString()); 
    } 
  }
  
  private void checkStructure(MotionScene.Transition paramTransition) {
    if (paramTransition.getStartConstraintSetId() == paramTransition.getEndConstraintSetId())
      Log.e("MotionLayout", "CHECK: start and end constraint set should not be the same!"); 
  }
  
  private void computeCurrentPositions() {
    int i = getChildCount();
    for (byte b = 0; b < i; b++) {
      View view = getChildAt(b);
      MotionController motionController = this.mFrameArrayList.get(view);
      if (motionController != null)
        motionController.setStartCurrentState(view); 
    } 
  }
  
  private void debugPos() {
    for (byte b = 0; b < getChildCount(); b++) {
      View view = getChildAt(b);
      String str2 = Debug.getLocation();
      String str4 = Debug.getName((View)this);
      String str3 = Debug.getName(getContext(), this.mCurrentState);
      String str1 = Debug.getName(view);
      int j = view.getLeft();
      int i = view.getTop();
      Log.v("MotionLayout", (new StringBuilder(String.valueOf(str2).length() + 27 + String.valueOf(str4).length() + String.valueOf(str3).length() + String.valueOf(str1).length())).append(" ").append(str2).append(" ").append(str4).append(" ").append(str3).append(" ").append(str1).append(j).append(" ").append(i).toString());
    } 
  }
  
  private void evaluateLayout() {
    // Byte code:
    //   0: aload_0
    //   1: getfield mTransitionGoalPosition : F
    //   4: aload_0
    //   5: getfield mTransitionLastPosition : F
    //   8: fsub
    //   9: invokestatic signum : (F)F
    //   12: fstore_3
    //   13: aload_0
    //   14: invokevirtual getNanoTime : ()J
    //   17: lstore #6
    //   19: fconst_0
    //   20: fstore_1
    //   21: aload_0
    //   22: getfield mInterpolator : Landroid/view/animation/Interpolator;
    //   25: astore #8
    //   27: aload #8
    //   29: instanceof androidx/constraintlayout/motion/utils/StopLogic
    //   32: ifne -> 55
    //   35: lload #6
    //   37: aload_0
    //   38: getfield mTransitionLastTime : J
    //   41: lsub
    //   42: l2f
    //   43: fload_3
    //   44: fmul
    //   45: ldc_w 1.0E-9
    //   48: fmul
    //   49: aload_0
    //   50: getfield mTransitionDuration : F
    //   53: fdiv
    //   54: fstore_1
    //   55: aload_0
    //   56: getfield mTransitionLastPosition : F
    //   59: fload_1
    //   60: fadd
    //   61: fstore_1
    //   62: iconst_0
    //   63: istore #5
    //   65: aload_0
    //   66: getfield mTransitionInstantly : Z
    //   69: ifeq -> 77
    //   72: aload_0
    //   73: getfield mTransitionGoalPosition : F
    //   76: fstore_1
    //   77: fload_3
    //   78: fconst_0
    //   79: fcmpl
    //   80: ifle -> 92
    //   83: fload_1
    //   84: aload_0
    //   85: getfield mTransitionGoalPosition : F
    //   88: fcmpl
    //   89: ifge -> 119
    //   92: fload_1
    //   93: fstore_2
    //   94: iload #5
    //   96: istore #4
    //   98: fload_3
    //   99: fconst_0
    //   100: fcmpg
    //   101: ifgt -> 127
    //   104: fload_1
    //   105: fstore_2
    //   106: iload #5
    //   108: istore #4
    //   110: fload_1
    //   111: aload_0
    //   112: getfield mTransitionGoalPosition : F
    //   115: fcmpg
    //   116: ifgt -> 127
    //   119: aload_0
    //   120: getfield mTransitionGoalPosition : F
    //   123: fstore_2
    //   124: iconst_1
    //   125: istore #4
    //   127: fload_2
    //   128: fstore_1
    //   129: aload #8
    //   131: ifnull -> 180
    //   134: fload_2
    //   135: fstore_1
    //   136: iload #4
    //   138: ifne -> 180
    //   141: aload_0
    //   142: getfield mTemporalInterpolator : Z
    //   145: ifeq -> 171
    //   148: aload #8
    //   150: lload #6
    //   152: aload_0
    //   153: getfield mAnimationStartTime : J
    //   156: lsub
    //   157: l2f
    //   158: ldc_w 1.0E-9
    //   161: fmul
    //   162: invokeinterface getInterpolation : (F)F
    //   167: fstore_1
    //   168: goto -> 180
    //   171: aload #8
    //   173: fload_2
    //   174: invokeinterface getInterpolation : (F)F
    //   179: fstore_1
    //   180: fload_3
    //   181: fconst_0
    //   182: fcmpl
    //   183: ifle -> 195
    //   186: fload_1
    //   187: aload_0
    //   188: getfield mTransitionGoalPosition : F
    //   191: fcmpl
    //   192: ifge -> 214
    //   195: fload_1
    //   196: fstore_2
    //   197: fload_3
    //   198: fconst_0
    //   199: fcmpg
    //   200: ifgt -> 219
    //   203: fload_1
    //   204: fstore_2
    //   205: fload_1
    //   206: aload_0
    //   207: getfield mTransitionGoalPosition : F
    //   210: fcmpg
    //   211: ifgt -> 219
    //   214: aload_0
    //   215: getfield mTransitionGoalPosition : F
    //   218: fstore_2
    //   219: aload_0
    //   220: fload_2
    //   221: putfield mPostInterpolationPosition : F
    //   224: aload_0
    //   225: invokevirtual getChildCount : ()I
    //   228: istore #5
    //   230: aload_0
    //   231: invokevirtual getNanoTime : ()J
    //   234: lstore #6
    //   236: aload_0
    //   237: getfield mProgressInterpolator : Landroid/view/animation/Interpolator;
    //   240: astore #8
    //   242: aload #8
    //   244: ifnonnull -> 252
    //   247: fload_2
    //   248: fstore_1
    //   249: goto -> 261
    //   252: aload #8
    //   254: fload_2
    //   255: invokeinterface getInterpolation : (F)F
    //   260: fstore_1
    //   261: iconst_0
    //   262: istore #4
    //   264: iload #4
    //   266: iload #5
    //   268: if_icmpge -> 322
    //   271: aload_0
    //   272: iload #4
    //   274: invokevirtual getChildAt : (I)Landroid/view/View;
    //   277: astore #9
    //   279: aload_0
    //   280: getfield mFrameArrayList : Ljava/util/HashMap;
    //   283: aload #9
    //   285: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   288: checkcast androidx/constraintlayout/motion/widget/MotionController
    //   291: astore #8
    //   293: aload #8
    //   295: ifnull -> 316
    //   298: aload #8
    //   300: aload #9
    //   302: fload_1
    //   303: lload #6
    //   305: aload_0
    //   306: getfield mKeyCache : Landroidx/constraintlayout/core/motion/utils/KeyCache;
    //   309: invokevirtual interpolate : (Landroid/view/View;FJLandroidx/constraintlayout/core/motion/utils/KeyCache;)Z
    //   312: pop
    //   313: goto -> 316
    //   316: iinc #4, 1
    //   319: goto -> 264
    //   322: aload_0
    //   323: getfield mMeasureDuringTransition : Z
    //   326: ifeq -> 333
    //   329: aload_0
    //   330: invokevirtual requestLayout : ()V
    //   333: return
  }
  
  private void fireTransitionChange() {
    // Byte code:
    //   0: aload_0
    //   1: getfield mTransitionListener : Landroidx/constraintlayout/motion/widget/MotionLayout$TransitionListener;
    //   4: ifnonnull -> 23
    //   7: aload_0
    //   8: getfield mTransitionListeners : Ljava/util/concurrent/CopyOnWriteArrayList;
    //   11: astore_2
    //   12: aload_2
    //   13: ifnull -> 219
    //   16: aload_2
    //   17: invokevirtual isEmpty : ()Z
    //   20: ifne -> 219
    //   23: aload_0
    //   24: getfield mListenerPosition : F
    //   27: aload_0
    //   28: getfield mTransitionPosition : F
    //   31: fcmpl
    //   32: ifeq -> 219
    //   35: aload_0
    //   36: getfield mListenerState : I
    //   39: iconst_m1
    //   40: if_icmpeq -> 121
    //   43: aload_0
    //   44: getfield mTransitionListener : Landroidx/constraintlayout/motion/widget/MotionLayout$TransitionListener;
    //   47: astore_2
    //   48: aload_2
    //   49: ifnull -> 67
    //   52: aload_2
    //   53: aload_0
    //   54: aload_0
    //   55: getfield mBeginState : I
    //   58: aload_0
    //   59: getfield mEndState : I
    //   62: invokeinterface onTransitionStarted : (Landroidx/constraintlayout/motion/widget/MotionLayout;II)V
    //   67: aload_0
    //   68: getfield mTransitionListeners : Ljava/util/concurrent/CopyOnWriteArrayList;
    //   71: astore_2
    //   72: aload_2
    //   73: ifnull -> 116
    //   76: aload_2
    //   77: invokevirtual iterator : ()Ljava/util/Iterator;
    //   80: astore_2
    //   81: aload_2
    //   82: invokeinterface hasNext : ()Z
    //   87: ifeq -> 116
    //   90: aload_2
    //   91: invokeinterface next : ()Ljava/lang/Object;
    //   96: checkcast androidx/constraintlayout/motion/widget/MotionLayout$TransitionListener
    //   99: aload_0
    //   100: aload_0
    //   101: getfield mBeginState : I
    //   104: aload_0
    //   105: getfield mEndState : I
    //   108: invokeinterface onTransitionStarted : (Landroidx/constraintlayout/motion/widget/MotionLayout;II)V
    //   113: goto -> 81
    //   116: aload_0
    //   117: iconst_1
    //   118: putfield mIsAnimating : Z
    //   121: aload_0
    //   122: iconst_m1
    //   123: putfield mListenerState : I
    //   126: aload_0
    //   127: getfield mTransitionPosition : F
    //   130: fstore_1
    //   131: aload_0
    //   132: fload_1
    //   133: putfield mListenerPosition : F
    //   136: aload_0
    //   137: getfield mTransitionListener : Landroidx/constraintlayout/motion/widget/MotionLayout$TransitionListener;
    //   140: astore_2
    //   141: aload_2
    //   142: ifnull -> 161
    //   145: aload_2
    //   146: aload_0
    //   147: aload_0
    //   148: getfield mBeginState : I
    //   151: aload_0
    //   152: getfield mEndState : I
    //   155: fload_1
    //   156: invokeinterface onTransitionChange : (Landroidx/constraintlayout/motion/widget/MotionLayout;IIF)V
    //   161: aload_0
    //   162: getfield mTransitionListeners : Ljava/util/concurrent/CopyOnWriteArrayList;
    //   165: astore_2
    //   166: aload_2
    //   167: ifnull -> 214
    //   170: aload_2
    //   171: invokevirtual iterator : ()Ljava/util/Iterator;
    //   174: astore_2
    //   175: aload_2
    //   176: invokeinterface hasNext : ()Z
    //   181: ifeq -> 214
    //   184: aload_2
    //   185: invokeinterface next : ()Ljava/lang/Object;
    //   190: checkcast androidx/constraintlayout/motion/widget/MotionLayout$TransitionListener
    //   193: aload_0
    //   194: aload_0
    //   195: getfield mBeginState : I
    //   198: aload_0
    //   199: getfield mEndState : I
    //   202: aload_0
    //   203: getfield mTransitionPosition : F
    //   206: invokeinterface onTransitionChange : (Landroidx/constraintlayout/motion/widget/MotionLayout;IIF)V
    //   211: goto -> 175
    //   214: aload_0
    //   215: iconst_1
    //   216: putfield mIsAnimating : Z
    //   219: return
  }
  
  private void fireTransitionStarted(MotionLayout paramMotionLayout, int paramInt1, int paramInt2) {
    TransitionListener transitionListener = this.mTransitionListener;
    if (transitionListener != null)
      transitionListener.onTransitionStarted(this, paramInt1, paramInt2); 
    CopyOnWriteArrayList<TransitionListener> copyOnWriteArrayList = this.mTransitionListeners;
    if (copyOnWriteArrayList != null) {
      Iterator<TransitionListener> iterator = copyOnWriteArrayList.iterator();
      while (iterator.hasNext())
        ((TransitionListener)iterator.next()).onTransitionStarted(paramMotionLayout, paramInt1, paramInt2); 
    } 
  }
  
  private boolean handlesTouchEvent(float paramFloat1, float paramFloat2, View paramView, MotionEvent paramMotionEvent) {
    // Byte code:
    //   0: iconst_0
    //   1: istore #7
    //   3: iload #7
    //   5: istore #6
    //   7: aload_3
    //   8: instanceof android/view/ViewGroup
    //   11: ifeq -> 98
    //   14: aload_3
    //   15: checkcast android/view/ViewGroup
    //   18: astore #8
    //   20: aload #8
    //   22: invokevirtual getChildCount : ()I
    //   25: iconst_1
    //   26: isub
    //   27: istore #5
    //   29: iload #7
    //   31: istore #6
    //   33: iload #5
    //   35: iflt -> 98
    //   38: aload #8
    //   40: iload #5
    //   42: invokevirtual getChildAt : (I)Landroid/view/View;
    //   45: astore #9
    //   47: aload_0
    //   48: aload #9
    //   50: invokevirtual getLeft : ()I
    //   53: i2f
    //   54: fload_1
    //   55: fadd
    //   56: aload_3
    //   57: invokevirtual getScrollX : ()I
    //   60: i2f
    //   61: fsub
    //   62: aload #9
    //   64: invokevirtual getTop : ()I
    //   67: i2f
    //   68: fload_2
    //   69: fadd
    //   70: aload_3
    //   71: invokevirtual getScrollY : ()I
    //   74: i2f
    //   75: fsub
    //   76: aload #9
    //   78: aload #4
    //   80: invokespecial handlesTouchEvent : (FFLandroid/view/View;Landroid/view/MotionEvent;)Z
    //   83: ifeq -> 92
    //   86: iconst_1
    //   87: istore #6
    //   89: goto -> 98
    //   92: iinc #5, -1
    //   95: goto -> 29
    //   98: iload #6
    //   100: istore #7
    //   102: iload #6
    //   104: ifne -> 195
    //   107: aload_0
    //   108: getfield mBoundsCheck : Landroid/graphics/RectF;
    //   111: fload_1
    //   112: fload_2
    //   113: aload_3
    //   114: invokevirtual getRight : ()I
    //   117: i2f
    //   118: fload_1
    //   119: fadd
    //   120: aload_3
    //   121: invokevirtual getLeft : ()I
    //   124: i2f
    //   125: fsub
    //   126: aload_3
    //   127: invokevirtual getBottom : ()I
    //   130: i2f
    //   131: fload_2
    //   132: fadd
    //   133: aload_3
    //   134: invokevirtual getTop : ()I
    //   137: i2f
    //   138: fsub
    //   139: invokevirtual set : (FFFF)V
    //   142: aload #4
    //   144: invokevirtual getAction : ()I
    //   147: ifne -> 174
    //   150: iload #6
    //   152: istore #7
    //   154: aload_0
    //   155: getfield mBoundsCheck : Landroid/graphics/RectF;
    //   158: aload #4
    //   160: invokevirtual getX : ()F
    //   163: aload #4
    //   165: invokevirtual getY : ()F
    //   168: invokevirtual contains : (FF)Z
    //   171: ifeq -> 195
    //   174: iload #6
    //   176: istore #7
    //   178: aload_0
    //   179: aload_3
    //   180: aload #4
    //   182: fload_1
    //   183: fneg
    //   184: fload_2
    //   185: fneg
    //   186: invokespecial callTransformedTouchEvent : (Landroid/view/View;Landroid/view/MotionEvent;FF)Z
    //   189: ifeq -> 195
    //   192: iconst_1
    //   193: istore #7
    //   195: iload #7
    //   197: ireturn
  }
  
  private void init(AttributeSet paramAttributeSet) {
    IS_IN_EDIT_MODE = isInEditMode();
    if (paramAttributeSet != null) {
      TypedArray typedArray = getContext().obtainStyledAttributes(paramAttributeSet, R.styleable.MotionLayout);
      int i = typedArray.getIndexCount();
      boolean bool = true;
      byte b = 0;
      while (b < i) {
        boolean bool1;
        int j = typedArray.getIndex(b);
        if (j == R.styleable.MotionLayout_layoutDescription) {
          int k = typedArray.getResourceId(j, -1);
          this.mScene = new MotionScene(getContext(), this, k);
          bool1 = bool;
        } else if (j == R.styleable.MotionLayout_currentState) {
          this.mCurrentState = typedArray.getResourceId(j, -1);
          bool1 = bool;
        } else if (j == R.styleable.MotionLayout_motionProgress) {
          this.mTransitionGoalPosition = typedArray.getFloat(j, 0.0F);
          this.mInTransition = true;
          bool1 = bool;
        } else if (j == R.styleable.MotionLayout_applyMotionScene) {
          bool1 = typedArray.getBoolean(j, bool);
        } else {
          int k = R.styleable.MotionLayout_showPaths;
          byte b1 = 0;
          if (j == k) {
            bool1 = bool;
            if (this.mDebugPath == 0) {
              if (typedArray.getBoolean(j, false))
                b1 = 2; 
              this.mDebugPath = b1;
              bool1 = bool;
            } 
          } else {
            bool1 = bool;
            if (j == R.styleable.MotionLayout_motionDebug) {
              this.mDebugPath = typedArray.getInt(j, 0);
              bool1 = bool;
            } 
          } 
        } 
        b++;
        bool = bool1;
      } 
      typedArray.recycle();
      if (this.mScene == null)
        Log.e("MotionLayout", "WARNING NO app:layoutDescription tag"); 
      if (!bool)
        this.mScene = null; 
    } 
    if (this.mDebugPath != 0)
      checkStructure(); 
    if (this.mCurrentState == -1) {
      MotionScene motionScene = this.mScene;
      if (motionScene != null) {
        this.mCurrentState = motionScene.getStartId();
        this.mBeginState = this.mScene.getStartId();
        this.mEndState = this.mScene.getEndId();
      } 
    } 
  }
  
  private void processTransitionCompleted() {
    if (this.mTransitionListener == null) {
      CopyOnWriteArrayList<TransitionListener> copyOnWriteArrayList = this.mTransitionListeners;
      if (copyOnWriteArrayList == null || copyOnWriteArrayList.isEmpty())
        return; 
    } 
    this.mIsAnimating = false;
    for (Integer integer : this.mTransitionCompleted) {
      TransitionListener transitionListener = this.mTransitionListener;
      if (transitionListener != null)
        transitionListener.onTransitionCompleted(this, integer.intValue()); 
      CopyOnWriteArrayList<TransitionListener> copyOnWriteArrayList = this.mTransitionListeners;
      if (copyOnWriteArrayList != null) {
        Iterator<TransitionListener> iterator = copyOnWriteArrayList.iterator();
        while (iterator.hasNext())
          ((TransitionListener)iterator.next()).onTransitionCompleted(this, integer.intValue()); 
      } 
    } 
    this.mTransitionCompleted.clear();
  }
  
  private void setupMotionViews() {
    int m = getChildCount();
    this.mModel.build();
    boolean bool = true;
    this.mInTransition = true;
    SparseArray sparseArray = new SparseArray();
    int i;
    for (i = 0; i < m; i++) {
      View view = getChildAt(i);
      sparseArray.put(view.getId(), this.mFrameArrayList.get(view));
    } 
    int k = getWidth();
    int n = getHeight();
    int j = this.mScene.gatPathMotionArc();
    if (j != -1)
      for (i = 0; i < m; i++) {
        MotionController motionController = this.mFrameArrayList.get(getChildAt(i));
        if (motionController != null)
          motionController.setPathMotionArc(j); 
      }  
    SparseBooleanArray sparseBooleanArray = new SparseBooleanArray();
    int[] arrayOfInt = new int[this.mFrameArrayList.size()];
    byte b = 0;
    for (i = 0; b < m; i = j) {
      View view = getChildAt(b);
      MotionController motionController = this.mFrameArrayList.get(view);
      j = i;
      if (motionController.getAnimateRelativeTo() != -1) {
        sparseBooleanArray.put(motionController.getAnimateRelativeTo(), true);
        arrayOfInt[i] = motionController.getAnimateRelativeTo();
        j = i + 1;
      } 
      b++;
    } 
    if (this.mDecoratorsHelpers != null) {
      for (j = 0; j < i; j++) {
        MotionController motionController = this.mFrameArrayList.get(findViewById(arrayOfInt[j]));
        if (motionController != null)
          this.mScene.getKeyFrames(motionController); 
      } 
      Iterator<MotionHelper> iterator = this.mDecoratorsHelpers.iterator();
      while (iterator.hasNext())
        ((MotionHelper)iterator.next()).onPreSetup(this, this.mFrameArrayList); 
      for (j = 0; j < i; j++) {
        MotionController motionController = this.mFrameArrayList.get(findViewById(arrayOfInt[j]));
        if (motionController != null)
          motionController.setup(k, n, this.mTransitionDuration, getNanoTime()); 
      } 
    } else {
      for (j = 0; j < i; j++) {
        MotionController motionController = this.mFrameArrayList.get(findViewById(arrayOfInt[j]));
        if (motionController != null) {
          this.mScene.getKeyFrames(motionController);
          motionController.setup(k, n, this.mTransitionDuration, getNanoTime());
        } 
      } 
    } 
    for (i = 0; i < m; i++) {
      View view = getChildAt(i);
      MotionController motionController = this.mFrameArrayList.get(view);
      if (!sparseBooleanArray.get(view.getId()) && motionController != null) {
        this.mScene.getKeyFrames(motionController);
        motionController.setup(k, n, this.mTransitionDuration, getNanoTime());
      } 
    } 
    float f = this.mScene.getStaggered();
    if (f != 0.0F) {
      if (f < 0.0D) {
        i = bool;
      } else {
        i = 0;
      } 
      b = 0;
      float f2 = Math.abs(f);
      float f1 = Float.MAX_VALUE;
      f = -3.4028235E38F;
      j = 0;
      while (true) {
        if (j < m) {
          MotionController motionController = this.mFrameArrayList.get(getChildAt(j));
          if (!Float.isNaN(motionController.mMotionStagger)) {
            j = 1;
            break;
          } 
          float f4 = motionController.getFinalX();
          float f3 = motionController.getFinalY();
          if (i != 0) {
            f3 -= f4;
          } else {
            f3 += f4;
          } 
          f1 = Math.min(f1, f3);
          f = Math.max(f, f3);
          j++;
          continue;
        } 
        j = b;
        break;
      } 
      if (j != 0) {
        float f3 = Float.MAX_VALUE;
        f = -3.4028235E38F;
        j = 0;
        while (j < m) {
          MotionController motionController = this.mFrameArrayList.get(getChildAt(j));
          float f4 = f3;
          f1 = f;
          if (!Float.isNaN(motionController.mMotionStagger)) {
            f4 = Math.min(f3, motionController.mMotionStagger);
            f1 = Math.max(f, motionController.mMotionStagger);
          } 
          j++;
          f3 = f4;
          f = f1;
        } 
        b = 0;
        j = k;
        while (b < m) {
          MotionController motionController = this.mFrameArrayList.get(getChildAt(b));
          if (!Float.isNaN(motionController.mMotionStagger)) {
            motionController.mStaggerScale = 1.0F / (1.0F - f2);
            if (i != 0) {
              motionController.mStaggerOffset = f2 - (f - motionController.mMotionStagger) / (f - f3) * f2;
            } else {
              motionController.mStaggerOffset = f2 - (motionController.mMotionStagger - f3) * f2 / (f - f3);
            } 
          } 
          b++;
        } 
      } else {
        for (j = 0; j < m; j++) {
          MotionController motionController = this.mFrameArrayList.get(getChildAt(j));
          float f4 = motionController.getFinalX();
          float f3 = motionController.getFinalY();
          if (i != 0) {
            f3 -= f4;
          } else {
            f3 += f4;
          } 
          motionController.mStaggerScale = 1.0F / (1.0F - f2);
          motionController.mStaggerOffset = f2 - (f3 - f1) * f2 / (f - f1);
        } 
      } 
    } 
  }
  
  private Rect toRect(ConstraintWidget paramConstraintWidget) {
    this.mTempRect.top = paramConstraintWidget.getY();
    this.mTempRect.left = paramConstraintWidget.getX();
    this.mTempRect.right = paramConstraintWidget.getWidth() + this.mTempRect.left;
    this.mTempRect.bottom = paramConstraintWidget.getHeight() + this.mTempRect.top;
    return this.mTempRect;
  }
  
  private static boolean willJump(float paramFloat1, float paramFloat2, float paramFloat3) {
    boolean bool2 = true;
    boolean bool1 = true;
    if (paramFloat1 > 0.0F) {
      float f1 = paramFloat1 / paramFloat3;
      if (paramFloat2 + paramFloat1 * f1 - paramFloat3 * f1 * f1 / 2.0F <= 1.0F)
        bool1 = false; 
      return bool1;
    } 
    float f = -paramFloat1 / paramFloat3;
    if (paramFloat2 + paramFloat1 * f + paramFloat3 * f * f / 2.0F < 0.0F) {
      bool1 = bool2;
    } else {
      bool1 = false;
    } 
    return bool1;
  }
  
  public void addTransitionListener(TransitionListener paramTransitionListener) {
    if (this.mTransitionListeners == null)
      this.mTransitionListeners = new CopyOnWriteArrayList<>(); 
    this.mTransitionListeners.add(paramTransitionListener);
  }
  
  void animateTo(float paramFloat) {
    MotionScene motionScene = this.mScene;
    if (motionScene == null)
      return; 
    float f2 = this.mTransitionLastPosition;
    float f1 = this.mTransitionPosition;
    if (f2 != f1 && this.mTransitionInstantly)
      this.mTransitionLastPosition = f1; 
    if (this.mTransitionLastPosition == paramFloat)
      return; 
    this.mTemporalInterpolator = false;
    f1 = this.mTransitionLastPosition;
    this.mTransitionGoalPosition = paramFloat;
    this.mTransitionDuration = motionScene.getDuration() / 1000.0F;
    setProgress(this.mTransitionGoalPosition);
    this.mInterpolator = null;
    this.mProgressInterpolator = this.mScene.getInterpolator();
    this.mTransitionInstantly = false;
    this.mAnimationStartTime = getNanoTime();
    this.mInTransition = true;
    this.mTransitionPosition = f1;
    this.mTransitionLastPosition = f1;
    invalidate();
  }
  
  public boolean applyViewTransition(int paramInt, MotionController paramMotionController) {
    MotionScene motionScene = this.mScene;
    return (motionScene != null) ? motionScene.applyViewTransition(paramInt, paramMotionController) : false;
  }
  
  public ConstraintSet cloneConstraintSet(int paramInt) {
    MotionScene motionScene = this.mScene;
    if (motionScene == null)
      return null; 
    ConstraintSet constraintSet1 = motionScene.getConstraintSet(paramInt);
    ConstraintSet constraintSet2 = new ConstraintSet();
    constraintSet2.clone(constraintSet1);
    return constraintSet2;
  }
  
  void disableAutoTransition(boolean paramBoolean) {
    MotionScene motionScene = this.mScene;
    if (motionScene == null)
      return; 
    motionScene.disableAutoTransition(paramBoolean);
  }
  
  protected void dispatchDraw(Canvas paramCanvas) {
    ArrayList<MotionHelper> arrayList2 = this.mDecoratorsHelpers;
    if (arrayList2 != null) {
      Iterator<MotionHelper> iterator = arrayList2.iterator();
      while (iterator.hasNext())
        ((MotionHelper)iterator.next()).onPreDraw(paramCanvas); 
    } 
    evaluate(false);
    MotionScene motionScene = this.mScene;
    if (motionScene != null && motionScene.mViewTransitionController != null)
      this.mScene.mViewTransitionController.animate(); 
    super.dispatchDraw(paramCanvas);
    if (this.mScene == null)
      return; 
    if ((this.mDebugPath & 0x1) == 1 && !isInEditMode()) {
      this.mFrames++;
      long l1 = getNanoTime();
      long l2 = this.mLastDrawTime;
      if (l2 != -1L) {
        l2 = l1 - l2;
        if (l2 > 200000000L) {
          this.mLastFps = (int)(this.mFrames / (float)l2 * 1.0E-9F * 100.0F) / 100.0F;
          this.mFrames = 0;
          this.mLastDrawTime = l1;
        } 
      } else {
        this.mLastDrawTime = l1;
      } 
      Paint paint = new Paint();
      paint.setTextSize(42.0F);
      float f1 = (int)(getProgress() * 1000.0F) / 10.0F;
      float f2 = this.mLastFps;
      String str1 = Debug.getState(this, this.mBeginState);
      String str2 = String.valueOf((new StringBuilder(String.valueOf(str1).length() + 24)).append(f2).append(" fps ").append(str1).append(" -> ").toString());
      String str3 = Debug.getState(this, this.mEndState);
      int i = this.mCurrentState;
      if (i == -1) {
        str1 = "undefined";
      } else {
        str1 = Debug.getState(this, i);
      } 
      str1 = (new StringBuilder(String.valueOf(str2).length() + 36 + String.valueOf(str3).length() + String.valueOf(str1).length())).append(str2).append(str3).append(" (progress: ").append(f1).append(" ) state=").append(str1).toString();
      paint.setColor(-16777216);
      paramCanvas.drawText(str1, 11.0F, (getHeight() - 29), paint);
      paint.setColor(-7864184);
      paramCanvas.drawText(str1, 10.0F, (getHeight() - 30), paint);
    } 
    if (this.mDebugPath > 1) {
      if (this.mDevModeDraw == null)
        this.mDevModeDraw = new DevModeDraw(); 
      this.mDevModeDraw.draw(paramCanvas, this.mFrameArrayList, this.mScene.getDuration(), this.mDebugPath);
    } 
    ArrayList<MotionHelper> arrayList1 = this.mDecoratorsHelpers;
    if (arrayList1 != null) {
      Iterator<MotionHelper> iterator = arrayList1.iterator();
      while (iterator.hasNext())
        ((MotionHelper)iterator.next()).onPostDraw(paramCanvas); 
    } 
  }
  
  public void enableTransition(int paramInt, boolean paramBoolean) {
    MotionScene.Transition transition = getTransition(paramInt);
    if (paramBoolean) {
      transition.setEnabled(true);
      return;
    } 
    if (transition == this.mScene.mCurrentTransition)
      for (MotionScene.Transition transition1 : this.mScene.getTransitionsWithState(this.mCurrentState)) {
        if (transition1.isEnabled()) {
          this.mScene.mCurrentTransition = transition1;
          break;
        } 
      }  
    transition.setEnabled(false);
  }
  
  public void enableViewTransition(int paramInt, boolean paramBoolean) {
    MotionScene motionScene = this.mScene;
    if (motionScene != null)
      motionScene.enableViewTransition(paramInt, paramBoolean); 
  }
  
  void endTrigger(boolean paramBoolean) {
    int i = getChildCount();
    for (byte b = 0; b < i; b++) {
      View view = getChildAt(b);
      MotionController motionController = this.mFrameArrayList.get(view);
      if (motionController != null)
        motionController.endTrigger(paramBoolean); 
    } 
  }
  
  void evaluate(boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: getfield mTransitionLastTime : J
    //   4: ldc2_w -1
    //   7: lcmp
    //   8: ifne -> 19
    //   11: aload_0
    //   12: aload_0
    //   13: invokevirtual getNanoTime : ()J
    //   16: putfield mTransitionLastTime : J
    //   19: aload_0
    //   20: getfield mTransitionLastPosition : F
    //   23: fstore_2
    //   24: fload_2
    //   25: fconst_0
    //   26: fcmpl
    //   27: ifle -> 41
    //   30: fload_2
    //   31: fconst_1
    //   32: fcmpg
    //   33: ifge -> 41
    //   36: aload_0
    //   37: iconst_m1
    //   38: putfield mCurrentState : I
    //   41: iconst_0
    //   42: istore #8
    //   44: aload_0
    //   45: getfield mKeepAnimating : Z
    //   48: ifne -> 79
    //   51: iload #8
    //   53: istore #7
    //   55: aload_0
    //   56: getfield mInTransition : Z
    //   59: ifeq -> 1148
    //   62: iload_1
    //   63: ifne -> 79
    //   66: iload #8
    //   68: istore #7
    //   70: aload_0
    //   71: getfield mTransitionGoalPosition : F
    //   74: fload_2
    //   75: fcmpl
    //   76: ifeq -> 1148
    //   79: aload_0
    //   80: getfield mTransitionGoalPosition : F
    //   83: fload_2
    //   84: fsub
    //   85: invokestatic signum : (F)F
    //   88: fstore #5
    //   90: aload_0
    //   91: invokevirtual getNanoTime : ()J
    //   94: lstore #11
    //   96: fconst_0
    //   97: fstore #4
    //   99: aload_0
    //   100: getfield mInterpolator : Landroid/view/animation/Interpolator;
    //   103: astore #13
    //   105: aload #13
    //   107: instanceof androidx/constraintlayout/motion/widget/MotionInterpolator
    //   110: ifne -> 135
    //   113: lload #11
    //   115: aload_0
    //   116: getfield mTransitionLastTime : J
    //   119: lsub
    //   120: l2f
    //   121: fload #5
    //   123: fmul
    //   124: ldc_w 1.0E-9
    //   127: fmul
    //   128: aload_0
    //   129: getfield mTransitionDuration : F
    //   132: fdiv
    //   133: fstore #4
    //   135: aload_0
    //   136: getfield mTransitionLastPosition : F
    //   139: fload #4
    //   141: fadd
    //   142: fstore_3
    //   143: iconst_0
    //   144: istore #8
    //   146: aload_0
    //   147: getfield mTransitionInstantly : Z
    //   150: ifeq -> 158
    //   153: aload_0
    //   154: getfield mTransitionGoalPosition : F
    //   157: fstore_3
    //   158: fload #5
    //   160: fconst_0
    //   161: fcmpl
    //   162: ifle -> 174
    //   165: fload_3
    //   166: aload_0
    //   167: getfield mTransitionGoalPosition : F
    //   170: fcmpl
    //   171: ifge -> 202
    //   174: fload_3
    //   175: fstore_2
    //   176: iload #8
    //   178: istore #7
    //   180: fload #5
    //   182: fconst_0
    //   183: fcmpg
    //   184: ifgt -> 215
    //   187: fload_3
    //   188: fstore_2
    //   189: iload #8
    //   191: istore #7
    //   193: fload_3
    //   194: aload_0
    //   195: getfield mTransitionGoalPosition : F
    //   198: fcmpg
    //   199: ifgt -> 215
    //   202: aload_0
    //   203: getfield mTransitionGoalPosition : F
    //   206: fstore_2
    //   207: aload_0
    //   208: iconst_0
    //   209: putfield mInTransition : Z
    //   212: iconst_1
    //   213: istore #7
    //   215: aload_0
    //   216: fload_2
    //   217: putfield mTransitionLastPosition : F
    //   220: aload_0
    //   221: fload_2
    //   222: putfield mTransitionPosition : F
    //   225: aload_0
    //   226: lload #11
    //   228: putfield mTransitionLastTime : J
    //   231: aload #13
    //   233: ifnull -> 516
    //   236: iload #7
    //   238: ifne -> 516
    //   241: aload_0
    //   242: getfield mTemporalInterpolator : Z
    //   245: ifeq -> 447
    //   248: aload #13
    //   250: lload #11
    //   252: aload_0
    //   253: getfield mAnimationStartTime : J
    //   256: lsub
    //   257: l2f
    //   258: ldc_w 1.0E-9
    //   261: fmul
    //   262: invokeinterface getInterpolation : (F)F
    //   267: fstore #4
    //   269: aload_0
    //   270: getfield mInterpolator : Landroid/view/animation/Interpolator;
    //   273: astore #14
    //   275: aload_0
    //   276: getfield mStopLogic : Landroidx/constraintlayout/motion/utils/StopLogic;
    //   279: astore #13
    //   281: aload #14
    //   283: aload #13
    //   285: if_acmpne -> 308
    //   288: aload #13
    //   290: invokevirtual isStopped : ()Z
    //   293: ifeq -> 302
    //   296: iconst_2
    //   297: istore #7
    //   299: goto -> 305
    //   302: iconst_1
    //   303: istore #7
    //   305: goto -> 311
    //   308: iconst_0
    //   309: istore #7
    //   311: aload_0
    //   312: fload #4
    //   314: putfield mTransitionLastPosition : F
    //   317: aload_0
    //   318: lload #11
    //   320: putfield mTransitionLastTime : J
    //   323: aload_0
    //   324: getfield mInterpolator : Landroid/view/animation/Interpolator;
    //   327: astore #13
    //   329: fload #4
    //   331: fstore_2
    //   332: aload #13
    //   334: instanceof androidx/constraintlayout/motion/widget/MotionInterpolator
    //   337: ifeq -> 444
    //   340: aload #13
    //   342: checkcast androidx/constraintlayout/motion/widget/MotionInterpolator
    //   345: invokevirtual getVelocity : ()F
    //   348: fstore #6
    //   350: aload_0
    //   351: fload #6
    //   353: putfield mLastVelocity : F
    //   356: fload #6
    //   358: invokestatic abs : (F)F
    //   361: aload_0
    //   362: getfield mTransitionDuration : F
    //   365: fmul
    //   366: ldc 1.0E-5
    //   368: fcmpg
    //   369: ifgt -> 383
    //   372: iload #7
    //   374: iconst_2
    //   375: if_icmpne -> 383
    //   378: aload_0
    //   379: iconst_0
    //   380: putfield mInTransition : Z
    //   383: fload #4
    //   385: fstore_3
    //   386: fload #6
    //   388: fconst_0
    //   389: fcmpl
    //   390: ifle -> 415
    //   393: fload #4
    //   395: fstore_3
    //   396: fload #4
    //   398: fconst_1
    //   399: fcmpl
    //   400: iflt -> 415
    //   403: fconst_1
    //   404: fstore_3
    //   405: aload_0
    //   406: fconst_1
    //   407: putfield mTransitionLastPosition : F
    //   410: aload_0
    //   411: iconst_0
    //   412: putfield mInTransition : Z
    //   415: fload_3
    //   416: fstore_2
    //   417: fload #6
    //   419: fconst_0
    //   420: fcmpg
    //   421: ifge -> 444
    //   424: fload_3
    //   425: fstore_2
    //   426: fload_3
    //   427: fconst_0
    //   428: fcmpg
    //   429: ifgt -> 444
    //   432: fconst_0
    //   433: fstore_2
    //   434: aload_0
    //   435: fconst_0
    //   436: putfield mTransitionLastPosition : F
    //   439: aload_0
    //   440: iconst_0
    //   441: putfield mInTransition : Z
    //   444: goto -> 525
    //   447: aload #13
    //   449: fload_2
    //   450: invokeinterface getInterpolation : (F)F
    //   455: fstore_3
    //   456: aload_0
    //   457: getfield mInterpolator : Landroid/view/animation/Interpolator;
    //   460: astore #13
    //   462: aload #13
    //   464: instanceof androidx/constraintlayout/motion/widget/MotionInterpolator
    //   467: ifeq -> 485
    //   470: aload_0
    //   471: aload #13
    //   473: checkcast androidx/constraintlayout/motion/widget/MotionInterpolator
    //   476: invokevirtual getVelocity : ()F
    //   479: putfield mLastVelocity : F
    //   482: goto -> 508
    //   485: aload_0
    //   486: aload #13
    //   488: fload_2
    //   489: fload #4
    //   491: fadd
    //   492: invokeinterface getInterpolation : (F)F
    //   497: fload_3
    //   498: fsub
    //   499: fload #5
    //   501: fmul
    //   502: fload #4
    //   504: fdiv
    //   505: putfield mLastVelocity : F
    //   508: iconst_0
    //   509: istore #7
    //   511: fload_3
    //   512: fstore_2
    //   513: goto -> 525
    //   516: aload_0
    //   517: fload #4
    //   519: putfield mLastVelocity : F
    //   522: iconst_0
    //   523: istore #7
    //   525: iconst_0
    //   526: istore #9
    //   528: aload_0
    //   529: getfield mLastVelocity : F
    //   532: invokestatic abs : (F)F
    //   535: ldc 1.0E-5
    //   537: fcmpl
    //   538: ifle -> 548
    //   541: aload_0
    //   542: getstatic androidx/constraintlayout/motion/widget/MotionLayout$TransitionState.MOVING : Landroidx/constraintlayout/motion/widget/MotionLayout$TransitionState;
    //   545: invokevirtual setState : (Landroidx/constraintlayout/motion/widget/MotionLayout$TransitionState;)V
    //   548: fload_2
    //   549: fstore_3
    //   550: iload #7
    //   552: iconst_1
    //   553: if_icmpeq -> 637
    //   556: fload #5
    //   558: fconst_0
    //   559: fcmpl
    //   560: ifle -> 572
    //   563: fload_2
    //   564: aload_0
    //   565: getfield mTransitionGoalPosition : F
    //   568: fcmpl
    //   569: ifge -> 594
    //   572: fload_2
    //   573: fstore #4
    //   575: fload #5
    //   577: fconst_0
    //   578: fcmpg
    //   579: ifgt -> 605
    //   582: fload_2
    //   583: fstore #4
    //   585: fload_2
    //   586: aload_0
    //   587: getfield mTransitionGoalPosition : F
    //   590: fcmpg
    //   591: ifgt -> 605
    //   594: aload_0
    //   595: getfield mTransitionGoalPosition : F
    //   598: fstore #4
    //   600: aload_0
    //   601: iconst_0
    //   602: putfield mInTransition : Z
    //   605: fload #4
    //   607: fconst_1
    //   608: fcmpl
    //   609: ifge -> 622
    //   612: fload #4
    //   614: fstore_3
    //   615: fload #4
    //   617: fconst_0
    //   618: fcmpg
    //   619: ifgt -> 637
    //   622: aload_0
    //   623: iconst_0
    //   624: putfield mInTransition : Z
    //   627: aload_0
    //   628: getstatic androidx/constraintlayout/motion/widget/MotionLayout$TransitionState.FINISHED : Landroidx/constraintlayout/motion/widget/MotionLayout$TransitionState;
    //   631: invokevirtual setState : (Landroidx/constraintlayout/motion/widget/MotionLayout$TransitionState;)V
    //   634: fload #4
    //   636: fstore_3
    //   637: aload_0
    //   638: invokevirtual getChildCount : ()I
    //   641: istore #10
    //   643: aload_0
    //   644: iconst_0
    //   645: putfield mKeepAnimating : Z
    //   648: aload_0
    //   649: invokevirtual getNanoTime : ()J
    //   652: lstore #11
    //   654: aload_0
    //   655: fload_3
    //   656: putfield mPostInterpolationPosition : F
    //   659: aload_0
    //   660: getfield mProgressInterpolator : Landroid/view/animation/Interpolator;
    //   663: astore #13
    //   665: aload #13
    //   667: ifnonnull -> 675
    //   670: fload_3
    //   671: fstore_2
    //   672: goto -> 684
    //   675: aload #13
    //   677: fload_3
    //   678: invokeinterface getInterpolation : (F)F
    //   683: fstore_2
    //   684: aload_0
    //   685: getfield mProgressInterpolator : Landroid/view/animation/Interpolator;
    //   688: astore #13
    //   690: aload #13
    //   692: ifnull -> 736
    //   695: aload #13
    //   697: fload #5
    //   699: aload_0
    //   700: getfield mTransitionDuration : F
    //   703: fdiv
    //   704: fload_3
    //   705: fadd
    //   706: invokeinterface getInterpolation : (F)F
    //   711: fstore #4
    //   713: aload_0
    //   714: fload #4
    //   716: putfield mLastVelocity : F
    //   719: aload_0
    //   720: fload #4
    //   722: aload_0
    //   723: getfield mProgressInterpolator : Landroid/view/animation/Interpolator;
    //   726: fload_3
    //   727: invokeinterface getInterpolation : (F)F
    //   732: fsub
    //   733: putfield mLastVelocity : F
    //   736: iconst_0
    //   737: istore #8
    //   739: iload #8
    //   741: iload #10
    //   743: if_icmpge -> 807
    //   746: aload_0
    //   747: iload #8
    //   749: invokevirtual getChildAt : (I)Landroid/view/View;
    //   752: astore #14
    //   754: aload_0
    //   755: getfield mFrameArrayList : Ljava/util/HashMap;
    //   758: aload #14
    //   760: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   763: checkcast androidx/constraintlayout/motion/widget/MotionController
    //   766: astore #13
    //   768: aload #13
    //   770: ifnull -> 801
    //   773: aload_0
    //   774: getfield mKeepAnimating : Z
    //   777: istore_1
    //   778: aload_0
    //   779: aload #13
    //   781: aload #14
    //   783: fload_2
    //   784: lload #11
    //   786: aload_0
    //   787: getfield mKeyCache : Landroidx/constraintlayout/core/motion/utils/KeyCache;
    //   790: invokevirtual interpolate : (Landroid/view/View;FJLandroidx/constraintlayout/core/motion/utils/KeyCache;)Z
    //   793: iload_1
    //   794: ior
    //   795: putfield mKeepAnimating : Z
    //   798: goto -> 801
    //   801: iinc #8, 1
    //   804: goto -> 739
    //   807: fload #5
    //   809: fconst_0
    //   810: fcmpl
    //   811: ifle -> 823
    //   814: fload_3
    //   815: aload_0
    //   816: getfield mTransitionGoalPosition : F
    //   819: fcmpl
    //   820: ifge -> 839
    //   823: fload #5
    //   825: fconst_0
    //   826: fcmpg
    //   827: ifgt -> 845
    //   830: fload_3
    //   831: aload_0
    //   832: getfield mTransitionGoalPosition : F
    //   835: fcmpg
    //   836: ifgt -> 845
    //   839: iconst_1
    //   840: istore #7
    //   842: goto -> 848
    //   845: iconst_0
    //   846: istore #7
    //   848: aload_0
    //   849: getfield mKeepAnimating : Z
    //   852: ifne -> 874
    //   855: aload_0
    //   856: getfield mInTransition : Z
    //   859: ifne -> 874
    //   862: iload #7
    //   864: ifeq -> 874
    //   867: aload_0
    //   868: getstatic androidx/constraintlayout/motion/widget/MotionLayout$TransitionState.FINISHED : Landroidx/constraintlayout/motion/widget/MotionLayout$TransitionState;
    //   871: invokevirtual setState : (Landroidx/constraintlayout/motion/widget/MotionLayout$TransitionState;)V
    //   874: aload_0
    //   875: getfield mMeasureDuringTransition : Z
    //   878: ifeq -> 885
    //   881: aload_0
    //   882: invokevirtual requestLayout : ()V
    //   885: aload_0
    //   886: getfield mKeepAnimating : Z
    //   889: istore_1
    //   890: iload #7
    //   892: ifne -> 901
    //   895: iconst_1
    //   896: istore #7
    //   898: goto -> 904
    //   901: iconst_0
    //   902: istore #7
    //   904: aload_0
    //   905: iload_1
    //   906: iload #7
    //   908: ior
    //   909: putfield mKeepAnimating : Z
    //   912: iload #9
    //   914: istore #7
    //   916: fload_3
    //   917: fconst_0
    //   918: fcmpg
    //   919: ifgt -> 980
    //   922: aload_0
    //   923: getfield mBeginState : I
    //   926: istore #8
    //   928: iload #9
    //   930: istore #7
    //   932: iload #8
    //   934: iconst_m1
    //   935: if_icmpeq -> 980
    //   938: iload #9
    //   940: istore #7
    //   942: aload_0
    //   943: getfield mCurrentState : I
    //   946: iload #8
    //   948: if_icmpeq -> 980
    //   951: aload_0
    //   952: iload #8
    //   954: putfield mCurrentState : I
    //   957: aload_0
    //   958: getfield mScene : Landroidx/constraintlayout/motion/widget/MotionScene;
    //   961: iload #8
    //   963: invokevirtual getConstraintSet : (I)Landroidx/constraintlayout/widget/ConstraintSet;
    //   966: aload_0
    //   967: invokevirtual applyCustomAttributes : (Landroidx/constraintlayout/widget/ConstraintLayout;)V
    //   970: aload_0
    //   971: getstatic androidx/constraintlayout/motion/widget/MotionLayout$TransitionState.FINISHED : Landroidx/constraintlayout/motion/widget/MotionLayout$TransitionState;
    //   974: invokevirtual setState : (Landroidx/constraintlayout/motion/widget/MotionLayout$TransitionState;)V
    //   977: iconst_1
    //   978: istore #7
    //   980: iload #7
    //   982: istore #8
    //   984: fload_3
    //   985: f2d
    //   986: dconst_1
    //   987: dcmpl
    //   988: iflt -> 1043
    //   991: aload_0
    //   992: getfield mCurrentState : I
    //   995: istore #10
    //   997: aload_0
    //   998: getfield mEndState : I
    //   1001: istore #9
    //   1003: iload #7
    //   1005: istore #8
    //   1007: iload #10
    //   1009: iload #9
    //   1011: if_icmpeq -> 1043
    //   1014: aload_0
    //   1015: iload #9
    //   1017: putfield mCurrentState : I
    //   1020: aload_0
    //   1021: getfield mScene : Landroidx/constraintlayout/motion/widget/MotionScene;
    //   1024: iload #9
    //   1026: invokevirtual getConstraintSet : (I)Landroidx/constraintlayout/widget/ConstraintSet;
    //   1029: aload_0
    //   1030: invokevirtual applyCustomAttributes : (Landroidx/constraintlayout/widget/ConstraintLayout;)V
    //   1033: aload_0
    //   1034: getstatic androidx/constraintlayout/motion/widget/MotionLayout$TransitionState.FINISHED : Landroidx/constraintlayout/motion/widget/MotionLayout$TransitionState;
    //   1037: invokevirtual setState : (Landroidx/constraintlayout/motion/widget/MotionLayout$TransitionState;)V
    //   1040: iconst_1
    //   1041: istore #8
    //   1043: aload_0
    //   1044: getfield mKeepAnimating : Z
    //   1047: ifne -> 1096
    //   1050: aload_0
    //   1051: getfield mInTransition : Z
    //   1054: ifeq -> 1060
    //   1057: goto -> 1096
    //   1060: fload #5
    //   1062: fconst_0
    //   1063: fcmpl
    //   1064: ifle -> 1073
    //   1067: fload_3
    //   1068: fconst_1
    //   1069: fcmpl
    //   1070: ifeq -> 1086
    //   1073: fload #5
    //   1075: fconst_0
    //   1076: fcmpg
    //   1077: ifge -> 1100
    //   1080: fload_3
    //   1081: fconst_0
    //   1082: fcmpl
    //   1083: ifne -> 1100
    //   1086: aload_0
    //   1087: getstatic androidx/constraintlayout/motion/widget/MotionLayout$TransitionState.FINISHED : Landroidx/constraintlayout/motion/widget/MotionLayout$TransitionState;
    //   1090: invokevirtual setState : (Landroidx/constraintlayout/motion/widget/MotionLayout$TransitionState;)V
    //   1093: goto -> 1100
    //   1096: aload_0
    //   1097: invokevirtual invalidate : ()V
    //   1100: aload_0
    //   1101: getfield mKeepAnimating : Z
    //   1104: ifne -> 1144
    //   1107: aload_0
    //   1108: getfield mInTransition : Z
    //   1111: ifne -> 1144
    //   1114: fload #5
    //   1116: fconst_0
    //   1117: fcmpl
    //   1118: ifle -> 1127
    //   1121: fload_3
    //   1122: fconst_1
    //   1123: fcmpl
    //   1124: ifeq -> 1140
    //   1127: fload #5
    //   1129: fconst_0
    //   1130: fcmpg
    //   1131: ifge -> 1144
    //   1134: fload_3
    //   1135: fconst_0
    //   1136: fcmpl
    //   1137: ifne -> 1144
    //   1140: aload_0
    //   1141: invokevirtual onNewStateAttachHandlers : ()V
    //   1144: iload #8
    //   1146: istore #7
    //   1148: aload_0
    //   1149: getfield mTransitionLastPosition : F
    //   1152: fstore_2
    //   1153: fload_2
    //   1154: fconst_1
    //   1155: fcmpl
    //   1156: iflt -> 1194
    //   1159: aload_0
    //   1160: getfield mCurrentState : I
    //   1163: istore #9
    //   1165: aload_0
    //   1166: getfield mEndState : I
    //   1169: istore #8
    //   1171: iload #9
    //   1173: iload #8
    //   1175: if_icmpeq -> 1181
    //   1178: iconst_1
    //   1179: istore #7
    //   1181: aload_0
    //   1182: iload #8
    //   1184: putfield mCurrentState : I
    //   1187: iload #7
    //   1189: istore #8
    //   1191: goto -> 1236
    //   1194: iload #7
    //   1196: istore #8
    //   1198: fload_2
    //   1199: fconst_0
    //   1200: fcmpg
    //   1201: ifgt -> 1236
    //   1204: aload_0
    //   1205: getfield mCurrentState : I
    //   1208: istore #9
    //   1210: aload_0
    //   1211: getfield mBeginState : I
    //   1214: istore #8
    //   1216: iload #9
    //   1218: iload #8
    //   1220: if_icmpeq -> 1226
    //   1223: iconst_1
    //   1224: istore #7
    //   1226: aload_0
    //   1227: iload #8
    //   1229: putfield mCurrentState : I
    //   1232: iload #7
    //   1234: istore #8
    //   1236: aload_0
    //   1237: aload_0
    //   1238: getfield mNeedsFireTransitionCompleted : Z
    //   1241: iload #8
    //   1243: ior
    //   1244: putfield mNeedsFireTransitionCompleted : Z
    //   1247: iload #8
    //   1249: ifeq -> 1263
    //   1252: aload_0
    //   1253: getfield mInLayout : Z
    //   1256: ifne -> 1263
    //   1259: aload_0
    //   1260: invokevirtual requestLayout : ()V
    //   1263: aload_0
    //   1264: aload_0
    //   1265: getfield mTransitionLastPosition : F
    //   1268: putfield mTransitionPosition : F
    //   1271: return
  }
  
  protected void fireTransitionCompleted() {
    // Byte code:
    //   0: aload_0
    //   1: getfield mTransitionListener : Landroidx/constraintlayout/motion/widget/MotionLayout$TransitionListener;
    //   4: ifnonnull -> 23
    //   7: aload_0
    //   8: getfield mTransitionListeners : Ljava/util/concurrent/CopyOnWriteArrayList;
    //   11: astore_3
    //   12: aload_3
    //   13: ifnull -> 100
    //   16: aload_3
    //   17: invokevirtual isEmpty : ()Z
    //   20: ifne -> 100
    //   23: aload_0
    //   24: getfield mListenerState : I
    //   27: iconst_m1
    //   28: if_icmpne -> 100
    //   31: aload_0
    //   32: aload_0
    //   33: getfield mCurrentState : I
    //   36: putfield mListenerState : I
    //   39: iconst_m1
    //   40: istore_1
    //   41: aload_0
    //   42: getfield mTransitionCompleted : Ljava/util/ArrayList;
    //   45: invokevirtual isEmpty : ()Z
    //   48: ifne -> 73
    //   51: aload_0
    //   52: getfield mTransitionCompleted : Ljava/util/ArrayList;
    //   55: astore_3
    //   56: aload_3
    //   57: aload_3
    //   58: invokevirtual size : ()I
    //   61: iconst_1
    //   62: isub
    //   63: invokevirtual get : (I)Ljava/lang/Object;
    //   66: checkcast java/lang/Integer
    //   69: invokevirtual intValue : ()I
    //   72: istore_1
    //   73: aload_0
    //   74: getfield mCurrentState : I
    //   77: istore_2
    //   78: iload_1
    //   79: iload_2
    //   80: if_icmpeq -> 100
    //   83: iload_2
    //   84: iconst_m1
    //   85: if_icmpeq -> 100
    //   88: aload_0
    //   89: getfield mTransitionCompleted : Ljava/util/ArrayList;
    //   92: iload_2
    //   93: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   96: invokevirtual add : (Ljava/lang/Object;)Z
    //   99: pop
    //   100: aload_0
    //   101: invokespecial processTransitionCompleted : ()V
    //   104: aload_0
    //   105: getfield mOnComplete : Ljava/lang/Runnable;
    //   108: astore_3
    //   109: aload_3
    //   110: ifnull -> 119
    //   113: aload_3
    //   114: invokeinterface run : ()V
    //   119: aload_0
    //   120: getfield mScheduledTransitionTo : [I
    //   123: astore_3
    //   124: aload_3
    //   125: ifnull -> 168
    //   128: aload_0
    //   129: getfield mScheduledTransitions : I
    //   132: ifle -> 168
    //   135: aload_0
    //   136: aload_3
    //   137: iconst_0
    //   138: iaload
    //   139: invokevirtual transitionToState : (I)V
    //   142: aload_0
    //   143: getfield mScheduledTransitionTo : [I
    //   146: astore_3
    //   147: aload_3
    //   148: iconst_1
    //   149: aload_3
    //   150: iconst_0
    //   151: aload_3
    //   152: arraylength
    //   153: iconst_1
    //   154: isub
    //   155: invokestatic arraycopy : (Ljava/lang/Object;ILjava/lang/Object;II)V
    //   158: aload_0
    //   159: aload_0
    //   160: getfield mScheduledTransitions : I
    //   163: iconst_1
    //   164: isub
    //   165: putfield mScheduledTransitions : I
    //   168: return
  }
  
  public void fireTrigger(int paramInt, boolean paramBoolean, float paramFloat) {
    TransitionListener transitionListener = this.mTransitionListener;
    if (transitionListener != null)
      transitionListener.onTransitionTrigger(this, paramInt, paramBoolean, paramFloat); 
    CopyOnWriteArrayList<TransitionListener> copyOnWriteArrayList = this.mTransitionListeners;
    if (copyOnWriteArrayList != null) {
      Iterator<TransitionListener> iterator = copyOnWriteArrayList.iterator();
      while (iterator.hasNext())
        ((TransitionListener)iterator.next()).onTransitionTrigger(this, paramInt, paramBoolean, paramFloat); 
    } 
  }
  
  void getAnchorDpDt(int paramInt, float paramFloat1, float paramFloat2, float paramFloat3, float[] paramArrayOffloat) {
    HashMap<View, MotionController> hashMap = this.mFrameArrayList;
    View view = getViewById(paramInt);
    MotionController motionController = hashMap.get(view);
    if (motionController != null) {
      motionController.getDpDt(paramFloat1, paramFloat2, paramFloat3, paramArrayOffloat);
      paramFloat2 = view.getY();
      paramFloat3 = paramFloat1 - this.lastPos;
      float f = this.lastY;
      if (paramFloat3 != 0.0F)
        paramFloat3 = (paramFloat2 - f) / paramFloat3; 
      this.lastPos = paramFloat1;
      this.lastY = paramFloat2;
    } else {
      if (view == null) {
        str = (new StringBuilder(11)).append(paramInt).toString();
      } else {
        str = view.getContext().getResources().getResourceName(paramInt);
      } 
      String str = String.valueOf(str);
      if (str.length() != 0) {
        str = "WARNING could not find view id ".concat(str);
      } else {
        str = new String("WARNING could not find view id ");
      } 
      Log.w("MotionLayout", str);
    } 
  }
  
  public ConstraintSet getConstraintSet(int paramInt) {
    MotionScene motionScene = this.mScene;
    return (motionScene == null) ? null : motionScene.getConstraintSet(paramInt);
  }
  
  public int[] getConstraintSetIds() {
    MotionScene motionScene = this.mScene;
    return (motionScene == null) ? null : motionScene.getConstraintSetIds();
  }
  
  String getConstraintSetNames(int paramInt) {
    MotionScene motionScene = this.mScene;
    return (motionScene == null) ? null : motionScene.lookUpConstraintName(paramInt);
  }
  
  public int getCurrentState() {
    return this.mCurrentState;
  }
  
  public void getDebugMode(boolean paramBoolean) {
    boolean bool;
    if (paramBoolean) {
      bool = true;
    } else {
      bool = true;
    } 
    this.mDebugPath = bool;
    invalidate();
  }
  
  public ArrayList<MotionScene.Transition> getDefinedTransitions() {
    MotionScene motionScene = this.mScene;
    return (motionScene == null) ? null : motionScene.getDefinedTransitions();
  }
  
  public DesignTool getDesignTool() {
    if (this.mDesignTool == null)
      this.mDesignTool = new DesignTool(this); 
    return this.mDesignTool;
  }
  
  public int getEndState() {
    return this.mEndState;
  }
  
  MotionController getMotionController(int paramInt) {
    return this.mFrameArrayList.get(findViewById(paramInt));
  }
  
  protected long getNanoTime() {
    return System.nanoTime();
  }
  
  public float getProgress() {
    return this.mTransitionLastPosition;
  }
  
  public MotionScene getScene() {
    return this.mScene;
  }
  
  public int getStartState() {
    return this.mBeginState;
  }
  
  public float getTargetPosition() {
    return this.mTransitionGoalPosition;
  }
  
  public MotionScene.Transition getTransition(int paramInt) {
    return this.mScene.getTransitionById(paramInt);
  }
  
  public Bundle getTransitionState() {
    if (this.mStateCache == null)
      this.mStateCache = new StateCache(); 
    this.mStateCache.recordState();
    return this.mStateCache.getTransitionState();
  }
  
  public long getTransitionTimeMs() {
    MotionScene motionScene = this.mScene;
    if (motionScene != null)
      this.mTransitionDuration = motionScene.getDuration() / 1000.0F; 
    return (long)(this.mTransitionDuration * 1000.0F);
  }
  
  public float getVelocity() {
    return this.mLastVelocity;
  }
  
  public void getViewVelocity(View paramView, float paramFloat1, float paramFloat2, float[] paramArrayOffloat, int paramInt) {
    float f1 = this.mLastVelocity;
    float f2 = this.mTransitionLastPosition;
    if (this.mInterpolator != null) {
      float f = Math.signum(this.mTransitionGoalPosition - this.mTransitionLastPosition);
      f1 = this.mInterpolator.getInterpolation(this.mTransitionLastPosition + 1.0E-5F);
      f2 = this.mInterpolator.getInterpolation(this.mTransitionLastPosition);
      f1 = f * (f1 - f2) / 1.0E-5F / this.mTransitionDuration;
    } 
    Interpolator interpolator = this.mInterpolator;
    if (interpolator instanceof MotionInterpolator)
      f1 = ((MotionInterpolator)interpolator).getVelocity(); 
    MotionController motionController = this.mFrameArrayList.get(paramView);
    if ((paramInt & 0x1) == 0) {
      motionController.getPostLayoutDvDp(f2, paramView.getWidth(), paramView.getHeight(), paramFloat1, paramFloat2, paramArrayOffloat);
    } else {
      motionController.getDpDt(f2, paramFloat1, paramFloat2, paramArrayOffloat);
    } 
    if (paramInt < 2) {
      paramArrayOffloat[0] = paramArrayOffloat[0] * f1;
      paramArrayOffloat[1] = paramArrayOffloat[1] * f1;
    } 
  }
  
  public boolean isAttachedToWindow() {
    boolean bool;
    if (Build.VERSION.SDK_INT >= 19)
      return super.isAttachedToWindow(); 
    if (getWindowToken() != null) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isDelayedApplicationOfInitialState() {
    return this.mDelayedApply;
  }
  
  public boolean isInRotation() {
    return this.mInRotation;
  }
  
  public boolean isInteractionEnabled() {
    return this.mInteractionEnabled;
  }
  
  public boolean isViewTransitionEnabled(int paramInt) {
    MotionScene motionScene = this.mScene;
    return (motionScene != null) ? motionScene.isViewTransitionEnabled(paramInt) : false;
  }
  
  public void jumpToState(int paramInt) {
    if (!isAttachedToWindow())
      this.mCurrentState = paramInt; 
    if (this.mBeginState == paramInt) {
      setProgress(0.0F);
    } else if (this.mEndState == paramInt) {
      setProgress(1.0F);
    } else {
      setTransition(paramInt, paramInt);
    } 
  }
  
  public void loadLayoutDescription(int paramInt) {
    if (paramInt != 0) {
      try {
        MotionScene motionScene = new MotionScene();
        this(getContext(), this, paramInt);
        this.mScene = motionScene;
        if (this.mCurrentState == -1) {
          this.mCurrentState = motionScene.getStartId();
          this.mBeginState = this.mScene.getStartId();
          this.mEndState = this.mScene.getEndId();
        } 
        if (Build.VERSION.SDK_INT < 19 || isAttachedToWindow()) {
          try {
            if (Build.VERSION.SDK_INT >= 17) {
              Display display = getDisplay();
              if (display == null) {
                paramInt = 0;
              } else {
                paramInt = display.getRotation();
              } 
              this.mPreviouseRotation = paramInt;
            } 
            motionScene = this.mScene;
            if (motionScene != null) {
              ConstraintSet constraintSet = motionScene.getConstraintSet(this.mCurrentState);
              this.mScene.readFallback(this);
              ArrayList<MotionHelper> arrayList = this.mDecoratorsHelpers;
              if (arrayList != null) {
                Iterator<MotionHelper> iterator = arrayList.iterator();
                while (iterator.hasNext())
                  ((MotionHelper)iterator.next()).onFinishedMotionScene(this); 
              } 
              if (constraintSet != null)
                constraintSet.applyTo(this); 
              this.mBeginState = this.mCurrentState;
            } 
            onNewStateAttachHandlers();
            StateCache stateCache = this.mStateCache;
            if (stateCache != null) {
              Runnable runnable;
              if (this.mDelayedApply) {
                runnable = new Runnable() {
                    final MotionLayout this$0;
                    
                    public void run() {
                      MotionLayout.this.mStateCache.apply();
                    }
                  };
                super(this);
                post(runnable);
              } else {
                runnable.apply();
              } 
            } else {
              MotionScene motionScene1 = this.mScene;
              if (motionScene1 != null && motionScene1.mCurrentTransition != null && this.mScene.mCurrentTransition.getAutoTransition() == 4) {
                transitionToEnd();
                setState(TransitionState.SETUP);
                setState(TransitionState.MOVING);
              } 
            } 
          } catch (Exception exception) {
            IllegalArgumentException illegalArgumentException = new IllegalArgumentException();
            this("unable to parse MotionScene file", exception);
            throw illegalArgumentException;
          } 
          return;
        } 
        this.mScene = null;
      } catch (Exception exception) {
        throw new IllegalArgumentException("unable to parse MotionScene file", exception);
      } 
    } else {
      this.mScene = null;
    } 
  }
  
  int lookUpConstraintId(String paramString) {
    MotionScene motionScene = this.mScene;
    return (motionScene == null) ? 0 : motionScene.lookUpConstraintId(paramString);
  }
  
  protected MotionTracker obtainVelocityTracker() {
    return MyTracker.obtain();
  }
  
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    if (Build.VERSION.SDK_INT >= 17) {
      Display display = getDisplay();
      if (display != null)
        this.mPreviouseRotation = display.getRotation(); 
    } 
    MotionScene motionScene = this.mScene;
    if (motionScene != null) {
      int i = this.mCurrentState;
      if (i != -1) {
        ConstraintSet constraintSet = motionScene.getConstraintSet(i);
        this.mScene.readFallback(this);
        ArrayList<MotionHelper> arrayList = this.mDecoratorsHelpers;
        if (arrayList != null) {
          Iterator<MotionHelper> iterator = arrayList.iterator();
          while (iterator.hasNext())
            ((MotionHelper)iterator.next()).onFinishedMotionScene(this); 
        } 
        if (constraintSet != null)
          constraintSet.applyTo(this); 
        this.mBeginState = this.mCurrentState;
      } 
    } 
    onNewStateAttachHandlers();
    StateCache stateCache = this.mStateCache;
    if (stateCache != null) {
      if (this.mDelayedApply) {
        post(new Runnable() {
              final MotionLayout this$0;
              
              public void run() {
                MotionLayout.this.mStateCache.apply();
              }
            });
      } else {
        stateCache.apply();
      } 
    } else {
      MotionScene motionScene1 = this.mScene;
      if (motionScene1 != null && motionScene1.mCurrentTransition != null && this.mScene.mCurrentTransition.getAutoTransition() == 4) {
        transitionToEnd();
        setState(TransitionState.SETUP);
        setState(TransitionState.MOVING);
      } 
    } 
  }
  
  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent) {
    MotionScene motionScene = this.mScene;
    if (motionScene == null || !this.mInteractionEnabled)
      return false; 
    if (motionScene.mViewTransitionController != null)
      this.mScene.mViewTransitionController.touchEvent(paramMotionEvent); 
    MotionScene.Transition transition = this.mScene.mCurrentTransition;
    if (transition != null && transition.isEnabled()) {
      TouchResponse touchResponse = transition.getTouchResponse();
      if (touchResponse != null) {
        if (paramMotionEvent.getAction() == 0) {
          RectF rectF = touchResponse.getTouchRegion((ViewGroup)this, new RectF());
          if (rectF != null && !rectF.contains(paramMotionEvent.getX(), paramMotionEvent.getY()))
            return false; 
        } 
        int i = touchResponse.getTouchRegionId();
        if (i != -1) {
          View view = this.mRegionView;
          if (view == null || view.getId() != i)
            this.mRegionView = findViewById(i); 
          view = this.mRegionView;
          if (view != null) {
            this.mBoundsCheck.set(view.getLeft(), this.mRegionView.getTop(), this.mRegionView.getRight(), this.mRegionView.getBottom());
            if (this.mBoundsCheck.contains(paramMotionEvent.getX(), paramMotionEvent.getY()) && !handlesTouchEvent(this.mRegionView.getLeft(), this.mRegionView.getTop(), this.mRegionView, paramMotionEvent))
              return onTouchEvent(paramMotionEvent); 
          } 
        } 
      } 
    } 
    return false;
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    this.mInLayout = true;
    try {
      if (this.mScene == null) {
        super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
        return;
      } 
      paramInt1 = paramInt3 - paramInt1;
      paramInt2 = paramInt4 - paramInt2;
      if (this.mLastLayoutWidth != paramInt1 || this.mLastLayoutHeight != paramInt2) {
        rebuildScene();
        evaluate(true);
      } 
      this.mLastLayoutWidth = paramInt1;
      this.mLastLayoutHeight = paramInt2;
      this.mOldWidth = paramInt1;
      this.mOldHeight = paramInt2;
      return;
    } finally {
      this.mInLayout = false;
    } 
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    int i;
    if (this.mScene == null) {
      super.onMeasure(paramInt1, paramInt2);
      return;
    } 
    if (this.mLastWidthMeasureSpec != paramInt1 || this.mLastHeightMeasureSpec != paramInt2) {
      i = 1;
    } else {
      i = 0;
    } 
    if (this.mNeedsFireTransitionCompleted) {
      this.mNeedsFireTransitionCompleted = false;
      onNewStateAttachHandlers();
      processTransitionCompleted();
      i = 1;
    } 
    if (this.mDirtyHierarchy)
      i = 1; 
    this.mLastWidthMeasureSpec = paramInt1;
    this.mLastHeightMeasureSpec = paramInt2;
    int k = this.mScene.getStartId();
    int j = this.mScene.getEndId();
    boolean bool = true;
    if ((i || this.mModel.isNotConfiguredWith(k, j)) && this.mBeginState != -1) {
      super.onMeasure(paramInt1, paramInt2);
      this.mModel.initFrom(this.mLayoutWidget, this.mScene.getConstraintSet(k), this.mScene.getConstraintSet(j));
      this.mModel.reEvaluateState();
      this.mModel.setMeasuredId(k, j);
      j = 0;
    } else {
      j = bool;
      if (i) {
        super.onMeasure(paramInt1, paramInt2);
        j = bool;
      } 
    } 
    if (this.mMeasureDuringTransition || j != 0) {
      paramInt2 = getPaddingTop();
      i = getPaddingBottom();
      j = getPaddingLeft();
      paramInt1 = getPaddingRight();
      paramInt1 = this.mLayoutWidget.getWidth() + j + paramInt1;
      paramInt2 = this.mLayoutWidget.getHeight() + paramInt2 + i;
      i = this.mWidthMeasureMode;
      if (i == Integer.MIN_VALUE || i == 0) {
        paramInt1 = this.mStartWrapWidth;
        paramInt1 = (int)(paramInt1 + this.mPostInterpolationPosition * (this.mEndWrapWidth - paramInt1));
        requestLayout();
      } 
      i = this.mHeightMeasureMode;
      if (i == Integer.MIN_VALUE || i == 0) {
        paramInt2 = this.mStartWrapHeight;
        paramInt2 = (int)(paramInt2 + this.mPostInterpolationPosition * (this.mEndWrapHeight - paramInt2));
        requestLayout();
      } 
      setMeasuredDimension(paramInt1, paramInt2);
    } 
    evaluateLayout();
  }
  
  public boolean onNestedFling(View paramView, float paramFloat1, float paramFloat2, boolean paramBoolean) {
    return false;
  }
  
  public boolean onNestedPreFling(View paramView, float paramFloat1, float paramFloat2) {
    return false;
  }
  
  public void onNestedPreScroll(final View target, int paramInt1, int paramInt2, int[] paramArrayOfint, int paramInt3) {
    MotionScene motionScene = this.mScene;
    if (motionScene == null)
      return; 
    MotionScene.Transition transition = motionScene.mCurrentTransition;
    if (transition == null || !transition.isEnabled())
      return; 
    if (transition.isEnabled()) {
      TouchResponse touchResponse = transition.getTouchResponse();
      if (touchResponse != null) {
        paramInt3 = touchResponse.getTouchRegionId();
        if (paramInt3 != -1 && target.getId() != paramInt3)
          return; 
      } 
    } 
    if (motionScene.getMoveWhenScrollAtTop()) {
      TouchResponse touchResponse = transition.getTouchResponse();
      byte b = -1;
      paramInt3 = b;
      if (touchResponse != null) {
        paramInt3 = b;
        if ((touchResponse.getFlags() & 0x4) != 0)
          paramInt3 = paramInt2; 
      } 
      float f1 = this.mTransitionPosition;
      if ((f1 == 1.0F || f1 == 0.0F) && target.canScrollVertically(paramInt3))
        return; 
    } 
    if (transition.getTouchResponse() != null && (transition.getTouchResponse().getFlags() & 0x1) != 0) {
      float f1 = motionScene.getProgressDirection(paramInt1, paramInt2);
      float f2 = this.mTransitionLastPosition;
      if ((f2 <= 0.0F && f1 < 0.0F) || (f2 >= 1.0F && f1 > 0.0F)) {
        if (Build.VERSION.SDK_INT >= 21) {
          target.setNestedScrollingEnabled(false);
          target.post(new Runnable(this) {
                final View val$target;
                
                public void run() {
                  target.setNestedScrollingEnabled(true);
                }
              });
        } 
        return;
      } 
    } 
    float f = this.mTransitionPosition;
    long l = getNanoTime();
    this.mScrollTargetDX = paramInt1;
    this.mScrollTargetDY = paramInt2;
    this.mScrollTargetDT = (float)((l - this.mScrollTargetTime) * 1.0E-9D);
    this.mScrollTargetTime = l;
    motionScene.processScrollMove(paramInt1, paramInt2);
    if (f != this.mTransitionPosition) {
      paramArrayOfint[0] = paramInt1;
      paramArrayOfint[1] = paramInt2;
    } 
    evaluate(false);
    if (paramArrayOfint[0] != 0 || paramArrayOfint[1] != 0)
      this.mUndergoingMotion = true; 
  }
  
  public void onNestedScroll(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {}
  
  public void onNestedScroll(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int[] paramArrayOfint) {
    if (this.mUndergoingMotion || paramInt1 != 0 || paramInt2 != 0) {
      paramArrayOfint[0] = paramArrayOfint[0] + paramInt3;
      paramArrayOfint[1] = paramArrayOfint[1] + paramInt4;
    } 
    this.mUndergoingMotion = false;
  }
  
  public void onNestedScrollAccepted(View paramView1, View paramView2, int paramInt1, int paramInt2) {
    this.mScrollTargetTime = getNanoTime();
    this.mScrollTargetDT = 0.0F;
    this.mScrollTargetDX = 0.0F;
    this.mScrollTargetDY = 0.0F;
  }
  
  void onNewStateAttachHandlers() {
    MotionScene motionScene = this.mScene;
    if (motionScene == null)
      return; 
    if (motionScene.autoTransition(this, this.mCurrentState)) {
      requestLayout();
      return;
    } 
    int i = this.mCurrentState;
    if (i != -1)
      this.mScene.addOnClickListeners(this, i); 
    if (this.mScene.supportTouch())
      this.mScene.setupTouch(); 
  }
  
  public void onRtlPropertiesChanged(int paramInt) {
    MotionScene motionScene = this.mScene;
    if (motionScene != null)
      motionScene.setRtl(isRtl()); 
  }
  
  public boolean onStartNestedScroll(View paramView1, View paramView2, int paramInt1, int paramInt2) {
    MotionScene motionScene = this.mScene;
    return !(motionScene == null || motionScene.mCurrentTransition == null || this.mScene.mCurrentTransition.getTouchResponse() == null || (this.mScene.mCurrentTransition.getTouchResponse().getFlags() & 0x2) != 0);
  }
  
  public void onStopNestedScroll(View paramView, int paramInt) {
    MotionScene motionScene = this.mScene;
    if (motionScene != null) {
      float f = this.mScrollTargetDT;
      if (f != 0.0F) {
        motionScene.processScrollUp(this.mScrollTargetDX / f, this.mScrollTargetDY / f);
        return;
      } 
    } 
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    MotionScene motionScene = this.mScene;
    if (motionScene != null && this.mInteractionEnabled && motionScene.supportTouch()) {
      MotionScene.Transition transition = this.mScene.mCurrentTransition;
      if (transition != null && !transition.isEnabled())
        return super.onTouchEvent(paramMotionEvent); 
      this.mScene.processTouchEvent(paramMotionEvent, getCurrentState(), this);
      return this.mScene.mCurrentTransition.isTransitionFlag(4) ? this.mScene.mCurrentTransition.getTouchResponse().isDragStarted() : true;
    } 
    return super.onTouchEvent(paramMotionEvent);
  }
  
  public void onViewAdded(View paramView) {
    super.onViewAdded(paramView);
    if (paramView instanceof MotionHelper) {
      MotionHelper motionHelper = (MotionHelper)paramView;
      if (this.mTransitionListeners == null)
        this.mTransitionListeners = new CopyOnWriteArrayList<>(); 
      this.mTransitionListeners.add(motionHelper);
      if (motionHelper.isUsedOnShow()) {
        if (this.mOnShowHelpers == null)
          this.mOnShowHelpers = new ArrayList<>(); 
        this.mOnShowHelpers.add(motionHelper);
      } 
      if (motionHelper.isUseOnHide()) {
        if (this.mOnHideHelpers == null)
          this.mOnHideHelpers = new ArrayList<>(); 
        this.mOnHideHelpers.add(motionHelper);
      } 
      if (motionHelper.isDecorator()) {
        if (this.mDecoratorsHelpers == null)
          this.mDecoratorsHelpers = new ArrayList<>(); 
        this.mDecoratorsHelpers.add(motionHelper);
      } 
    } 
  }
  
  public void onViewRemoved(View paramView) {
    super.onViewRemoved(paramView);
    ArrayList<MotionHelper> arrayList = this.mOnShowHelpers;
    if (arrayList != null)
      arrayList.remove(paramView); 
    arrayList = this.mOnHideHelpers;
    if (arrayList != null)
      arrayList.remove(paramView); 
  }
  
  protected void parseLayoutDescription(int paramInt) {
    this.mConstraintLayoutSpec = null;
  }
  
  @Deprecated
  public void rebuildMotion() {
    Log.e("MotionLayout", "This method is deprecated. Please call rebuildScene() instead.");
    rebuildScene();
  }
  
  public void rebuildScene() {
    this.mModel.reEvaluateState();
    invalidate();
  }
  
  public boolean removeTransitionListener(TransitionListener paramTransitionListener) {
    CopyOnWriteArrayList<TransitionListener> copyOnWriteArrayList = this.mTransitionListeners;
    return (copyOnWriteArrayList == null) ? false : copyOnWriteArrayList.remove(paramTransitionListener);
  }
  
  public void requestLayout() {
    if (!this.mMeasureDuringTransition && this.mCurrentState == -1) {
      MotionScene motionScene = this.mScene;
      if (motionScene != null && motionScene.mCurrentTransition != null) {
        int i = this.mScene.mCurrentTransition.getLayoutDuringTransition();
        if (i == 0)
          return; 
        if (i == 2) {
          int j = getChildCount();
          for (i = 0; i < j; i++) {
            View view = getChildAt(i);
            ((MotionController)this.mFrameArrayList.get(view)).remeasure();
          } 
          return;
        } 
      } 
    } 
    super.requestLayout();
  }
  
  public void rotateTo(int paramInt1, int paramInt2) {
    byte b = 1;
    this.mInRotation = true;
    this.mPreRotateWidth = getWidth();
    this.mPreRotateHeight = getHeight();
    int i = getDisplay().getRotation();
    if ((i + 1) % 4 <= (this.mPreviouseRotation + 1) % 4)
      b = 2; 
    this.mRotatMode = b;
    this.mPreviouseRotation = i;
    i = getChildCount();
    for (b = 0; b < i; b++) {
      View view = getChildAt(b);
      ViewState viewState2 = this.mPreRotate.get(view);
      ViewState viewState1 = viewState2;
      if (viewState2 == null) {
        viewState1 = new ViewState();
        this.mPreRotate.put(view, viewState1);
      } 
      viewState1.getState(view);
    } 
    this.mBeginState = -1;
    this.mEndState = paramInt1;
    this.mScene.setTransition(-1, paramInt1);
    this.mModel.initFrom(this.mLayoutWidget, null, this.mScene.getConstraintSet(this.mEndState));
    this.mTransitionPosition = 0.0F;
    this.mTransitionLastPosition = 0.0F;
    invalidate();
    transitionToEnd(new Runnable() {
          final MotionLayout this$0;
          
          public void run() {
            MotionLayout.access$302(MotionLayout.this, false);
          }
        });
    if (paramInt2 > 0)
      this.mTransitionDuration = paramInt2 / 1000.0F; 
  }
  
  public void scheduleTransitionTo(int paramInt) {
    if (getCurrentState() == -1) {
      transitionToState(paramInt);
    } else {
      int[] arrayOfInt = this.mScheduledTransitionTo;
      if (arrayOfInt == null) {
        this.mScheduledTransitionTo = new int[4];
      } else if (arrayOfInt.length <= this.mScheduledTransitions) {
        this.mScheduledTransitionTo = Arrays.copyOf(arrayOfInt, arrayOfInt.length * 2);
      } 
      arrayOfInt = this.mScheduledTransitionTo;
      int i = this.mScheduledTransitions;
      this.mScheduledTransitions = i + 1;
      arrayOfInt[i] = paramInt;
    } 
  }
  
  public void setDebugMode(int paramInt) {
    this.mDebugPath = paramInt;
    invalidate();
  }
  
  public void setDelayedApplicationOfInitialState(boolean paramBoolean) {
    this.mDelayedApply = paramBoolean;
  }
  
  public void setInteractionEnabled(boolean paramBoolean) {
    this.mInteractionEnabled = paramBoolean;
  }
  
  public void setInterpolatedProgress(float paramFloat) {
    if (this.mScene != null) {
      setState(TransitionState.MOVING);
      Interpolator interpolator = this.mScene.getInterpolator();
      if (interpolator != null) {
        setProgress(interpolator.getInterpolation(paramFloat));
        return;
      } 
    } 
    setProgress(paramFloat);
  }
  
  public void setOnHide(float paramFloat) {
    ArrayList<MotionHelper> arrayList = this.mOnHideHelpers;
    if (arrayList != null) {
      int i = arrayList.size();
      for (byte b = 0; b < i; b++)
        ((MotionHelper)this.mOnHideHelpers.get(b)).setProgress(paramFloat); 
    } 
  }
  
  public void setOnShow(float paramFloat) {
    ArrayList<MotionHelper> arrayList = this.mOnShowHelpers;
    if (arrayList != null) {
      int i = arrayList.size();
      for (byte b = 0; b < i; b++)
        ((MotionHelper)this.mOnShowHelpers.get(b)).setProgress(paramFloat); 
    } 
  }
  
  public void setProgress(float paramFloat) {
    if (paramFloat < 0.0F || paramFloat > 1.0F)
      Log.w("MotionLayout", "Warning! Progress is defined for values between 0.0 and 1.0 inclusive"); 
    if (!isAttachedToWindow()) {
      if (this.mStateCache == null)
        this.mStateCache = new StateCache(); 
      this.mStateCache.setProgress(paramFloat);
      return;
    } 
    if (paramFloat <= 0.0F) {
      if (this.mTransitionLastPosition == 1.0F && this.mCurrentState == this.mEndState)
        setState(TransitionState.MOVING); 
      this.mCurrentState = this.mBeginState;
      if (this.mTransitionLastPosition == 0.0F)
        setState(TransitionState.FINISHED); 
    } else if (paramFloat >= 1.0F) {
      if (this.mTransitionLastPosition == 0.0F && this.mCurrentState == this.mBeginState)
        setState(TransitionState.MOVING); 
      this.mCurrentState = this.mEndState;
      if (this.mTransitionLastPosition == 1.0F)
        setState(TransitionState.FINISHED); 
    } else {
      this.mCurrentState = -1;
      setState(TransitionState.MOVING);
    } 
    if (this.mScene == null)
      return; 
    this.mTransitionInstantly = true;
    this.mTransitionGoalPosition = paramFloat;
    this.mTransitionPosition = paramFloat;
    this.mTransitionLastTime = -1L;
    this.mAnimationStartTime = -1L;
    this.mInterpolator = null;
    this.mInTransition = true;
    invalidate();
  }
  
  public void setProgress(float paramFloat1, float paramFloat2) {
    if (!isAttachedToWindow()) {
      if (this.mStateCache == null)
        this.mStateCache = new StateCache(); 
      this.mStateCache.setProgress(paramFloat1);
      this.mStateCache.setVelocity(paramFloat2);
      return;
    } 
    setProgress(paramFloat1);
    setState(TransitionState.MOVING);
    this.mLastVelocity = paramFloat2;
    float f1 = 0.0F;
    float f2 = 0.0F;
    if (paramFloat2 != 0.0F) {
      paramFloat1 = f2;
      if (paramFloat2 > 0.0F)
        paramFloat1 = 1.0F; 
      animateTo(paramFloat1);
    } else if (paramFloat1 != 0.0F && paramFloat1 != 1.0F) {
      paramFloat2 = f1;
      if (paramFloat1 > 0.5F)
        paramFloat2 = 1.0F; 
      animateTo(paramFloat2);
    } 
  }
  
  public void setScene(MotionScene paramMotionScene) {
    this.mScene = paramMotionScene;
    paramMotionScene.setRtl(isRtl());
    rebuildScene();
  }
  
  void setStartState(int paramInt) {
    if (!isAttachedToWindow()) {
      if (this.mStateCache == null)
        this.mStateCache = new StateCache(); 
      this.mStateCache.setStartState(paramInt);
      this.mStateCache.setEndState(paramInt);
      return;
    } 
    this.mCurrentState = paramInt;
  }
  
  public void setState(int paramInt1, int paramInt2, int paramInt3) {
    setState(TransitionState.SETUP);
    this.mCurrentState = paramInt1;
    this.mBeginState = -1;
    this.mEndState = -1;
    if (this.mConstraintLayoutSpec != null) {
      this.mConstraintLayoutSpec.updateConstraints(paramInt1, paramInt2, paramInt3);
    } else {
      MotionScene motionScene = this.mScene;
      if (motionScene != null)
        motionScene.getConstraintSet(paramInt1).applyTo(this); 
    } 
  }
  
  void setState(TransitionState paramTransitionState) {
    if (paramTransitionState == TransitionState.FINISHED && this.mCurrentState == -1)
      return; 
    TransitionState transitionState = this.mTransitionState;
    this.mTransitionState = paramTransitionState;
    if (transitionState == TransitionState.MOVING && paramTransitionState == TransitionState.MOVING)
      fireTransitionChange(); 
    switch (transitionState) {
      default:
        return;
      case null:
        if (paramTransitionState == TransitionState.FINISHED)
          fireTransitionCompleted(); 
      case null:
      case null:
        break;
    } 
    if (paramTransitionState == TransitionState.MOVING)
      fireTransitionChange(); 
    if (paramTransitionState == TransitionState.FINISHED)
      fireTransitionCompleted(); 
  }
  
  public void setTransition(int paramInt) {
    if (this.mScene != null) {
      MotionScene.Transition transition = getTransition(paramInt);
      paramInt = this.mCurrentState;
      this.mBeginState = transition.getStartConstraintSetId();
      this.mEndState = transition.getEndConstraintSetId();
      if (!isAttachedToWindow()) {
        if (this.mStateCache == null)
          this.mStateCache = new StateCache(); 
        this.mStateCache.setStartState(this.mBeginState);
        this.mStateCache.setEndState(this.mEndState);
        return;
      } 
      float f1 = Float.NaN;
      paramInt = this.mCurrentState;
      if (paramInt == this.mBeginState) {
        f1 = 0.0F;
      } else if (paramInt == this.mEndState) {
        f1 = 1.0F;
      } 
      this.mScene.setTransition(transition);
      this.mModel.initFrom(this.mLayoutWidget, this.mScene.getConstraintSet(this.mBeginState), this.mScene.getConstraintSet(this.mEndState));
      rebuildScene();
      float f3 = this.mTransitionLastPosition;
      float f2 = 0.0F;
      if (f3 != f1)
        if (f1 == 0.0F) {
          endTrigger(true);
          this.mScene.getConstraintSet(this.mBeginState).applyTo(this);
        } else if (f1 == 1.0F) {
          endTrigger(false);
          this.mScene.getConstraintSet(this.mEndState).applyTo(this);
        }  
      if (!Float.isNaN(f1))
        f2 = f1; 
      this.mTransitionLastPosition = f2;
      if (Float.isNaN(f1)) {
        Log.v("MotionLayout", String.valueOf(Debug.getLocation()).concat(" transitionToStart "));
        transitionToStart();
      } else {
        setProgress(f1);
      } 
    } 
  }
  
  public void setTransition(int paramInt1, int paramInt2) {
    if (!isAttachedToWindow()) {
      if (this.mStateCache == null)
        this.mStateCache = new StateCache(); 
      this.mStateCache.setStartState(paramInt1);
      this.mStateCache.setEndState(paramInt2);
      return;
    } 
    MotionScene motionScene = this.mScene;
    if (motionScene != null) {
      this.mBeginState = paramInt1;
      this.mEndState = paramInt2;
      motionScene.setTransition(paramInt1, paramInt2);
      this.mModel.initFrom(this.mLayoutWidget, this.mScene.getConstraintSet(paramInt1), this.mScene.getConstraintSet(paramInt2));
      rebuildScene();
      this.mTransitionLastPosition = 0.0F;
      transitionToStart();
    } 
  }
  
  protected void setTransition(MotionScene.Transition paramTransition) {
    long l;
    this.mScene.setTransition(paramTransition);
    setState(TransitionState.SETUP);
    if (this.mCurrentState == this.mScene.getEndId()) {
      this.mTransitionLastPosition = 1.0F;
      this.mTransitionPosition = 1.0F;
      this.mTransitionGoalPosition = 1.0F;
    } else {
      this.mTransitionLastPosition = 0.0F;
      this.mTransitionPosition = 0.0F;
      this.mTransitionGoalPosition = 0.0F;
    } 
    if (paramTransition.isTransitionFlag(1)) {
      l = -1L;
    } else {
      l = getNanoTime();
    } 
    this.mTransitionLastTime = l;
    int i = this.mScene.getStartId();
    int j = this.mScene.getEndId();
    if (i == this.mBeginState && j == this.mEndState)
      return; 
    this.mBeginState = i;
    this.mEndState = j;
    this.mScene.setTransition(i, j);
    this.mModel.initFrom(this.mLayoutWidget, this.mScene.getConstraintSet(this.mBeginState), this.mScene.getConstraintSet(this.mEndState));
    this.mModel.setMeasuredId(this.mBeginState, this.mEndState);
    this.mModel.reEvaluateState();
    rebuildScene();
  }
  
  public void setTransitionDuration(int paramInt) {
    MotionScene motionScene = this.mScene;
    if (motionScene == null) {
      Log.e("MotionLayout", "MotionScene not defined");
      return;
    } 
    motionScene.setDuration(paramInt);
  }
  
  public void setTransitionListener(TransitionListener paramTransitionListener) {
    this.mTransitionListener = paramTransitionListener;
  }
  
  public void setTransitionState(Bundle paramBundle) {
    if (this.mStateCache == null)
      this.mStateCache = new StateCache(); 
    this.mStateCache.setTransitionState(paramBundle);
    if (isAttachedToWindow())
      this.mStateCache.apply(); 
  }
  
  public String toString() {
    Context context = getContext();
    String str1 = Debug.getName(context, this.mBeginState);
    String str2 = Debug.getName(context, this.mEndState);
    float f1 = this.mTransitionLastPosition;
    float f2 = this.mLastVelocity;
    return (new StringBuilder(String.valueOf(str1).length() + 47 + String.valueOf(str2).length())).append(str1).append("->").append(str2).append(" (pos:").append(f1).append(" Dpos/Dt:").append(f2).toString();
  }
  
  public void touchAnimateTo(int paramInt, float paramFloat1, float paramFloat2) {
    if (this.mScene == null)
      return; 
    if (this.mTransitionLastPosition == paramFloat1)
      return; 
    this.mTemporalInterpolator = true;
    this.mAnimationStartTime = getNanoTime();
    this.mTransitionDuration = this.mScene.getDuration() / 1000.0F;
    this.mTransitionGoalPosition = paramFloat1;
    this.mInTransition = true;
    switch (paramInt) {
      case 5:
        if (willJump(paramFloat2, this.mTransitionLastPosition, this.mScene.getMaxAcceleration())) {
          this.mDecelerateLogic.config(paramFloat2, this.mTransitionLastPosition, this.mScene.getMaxAcceleration());
          this.mInterpolator = this.mDecelerateLogic;
          break;
        } 
        this.mStopLogic.config(this.mTransitionLastPosition, paramFloat1, paramFloat2, this.mTransitionDuration, this.mScene.getMaxAcceleration(), this.mScene.getMaxVelocity());
        this.mLastVelocity = 0.0F;
        paramInt = this.mCurrentState;
        this.mTransitionGoalPosition = paramFloat1;
        this.mCurrentState = paramInt;
        this.mInterpolator = (Interpolator)this.mStopLogic;
        break;
      case 4:
        this.mDecelerateLogic.config(paramFloat2, this.mTransitionLastPosition, this.mScene.getMaxAcceleration());
        this.mInterpolator = this.mDecelerateLogic;
        break;
      case 0:
      case 1:
      case 2:
      case 6:
      case 7:
        if (paramInt == 1 || paramInt == 7) {
          paramFloat1 = 0.0F;
        } else if (paramInt == 2 || paramInt == 6) {
          paramFloat1 = 1.0F;
        } 
        if (this.mScene.getAutoCompleteMode() == 0) {
          this.mStopLogic.config(this.mTransitionLastPosition, paramFloat1, paramFloat2, this.mTransitionDuration, this.mScene.getMaxAcceleration(), this.mScene.getMaxVelocity());
        } else {
          this.mStopLogic.springConfig(this.mTransitionLastPosition, paramFloat1, paramFloat2, this.mScene.getSpringMass(), this.mScene.getSpringStiffiness(), this.mScene.getSpringDamping(), this.mScene.getSpringStopThreshold(), this.mScene.getSpringBoundary());
        } 
        paramInt = this.mCurrentState;
        this.mTransitionGoalPosition = paramFloat1;
        this.mCurrentState = paramInt;
        this.mInterpolator = (Interpolator)this.mStopLogic;
        break;
    } 
    this.mTransitionInstantly = false;
    this.mAnimationStartTime = getNanoTime();
    invalidate();
  }
  
  public void touchSpringTo(float paramFloat1, float paramFloat2) {
    if (this.mScene == null)
      return; 
    if (this.mTransitionLastPosition == paramFloat1)
      return; 
    this.mTemporalInterpolator = true;
    this.mAnimationStartTime = getNanoTime();
    this.mTransitionDuration = this.mScene.getDuration() / 1000.0F;
    this.mTransitionGoalPosition = paramFloat1;
    this.mInTransition = true;
    this.mStopLogic.springConfig(this.mTransitionLastPosition, paramFloat1, paramFloat2, this.mScene.getSpringMass(), this.mScene.getSpringStiffiness(), this.mScene.getSpringDamping(), this.mScene.getSpringStopThreshold(), this.mScene.getSpringBoundary());
    int i = this.mCurrentState;
    this.mTransitionGoalPosition = paramFloat1;
    this.mCurrentState = i;
    this.mInterpolator = (Interpolator)this.mStopLogic;
    this.mTransitionInstantly = false;
    this.mAnimationStartTime = getNanoTime();
    invalidate();
  }
  
  public void transitionToEnd() {
    animateTo(1.0F);
    this.mOnComplete = null;
  }
  
  public void transitionToEnd(Runnable paramRunnable) {
    animateTo(1.0F);
    this.mOnComplete = paramRunnable;
  }
  
  public void transitionToStart() {
    animateTo(0.0F);
  }
  
  public void transitionToState(int paramInt) {
    if (!isAttachedToWindow()) {
      if (this.mStateCache == null)
        this.mStateCache = new StateCache(); 
      this.mStateCache.setEndState(paramInt);
      return;
    } 
    transitionToState(paramInt, -1, -1);
  }
  
  public void transitionToState(int paramInt1, int paramInt2) {
    if (!isAttachedToWindow()) {
      if (this.mStateCache == null)
        this.mStateCache = new StateCache(); 
      this.mStateCache.setEndState(paramInt1);
      return;
    } 
    transitionToState(paramInt1, -1, -1, paramInt2);
  }
  
  public void transitionToState(int paramInt1, int paramInt2, int paramInt3) {
    transitionToState(paramInt1, paramInt2, paramInt3, -1);
  }
  
  public void transitionToState(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    MotionScene motionScene = this.mScene;
    if (motionScene != null && motionScene.mStateSet != null) {
      paramInt2 = this.mScene.mStateSet.convertToConstraintSet(this.mCurrentState, paramInt1, paramInt2, paramInt3);
      if (paramInt2 != -1)
        paramInt1 = paramInt2; 
    } 
    paramInt2 = this.mCurrentState;
    if (paramInt2 == paramInt1)
      return; 
    if (this.mBeginState == paramInt1) {
      animateTo(0.0F);
      if (paramInt4 > 0)
        this.mTransitionDuration = paramInt4 / 1000.0F; 
      return;
    } 
    if (this.mEndState == paramInt1) {
      animateTo(1.0F);
      if (paramInt4 > 0)
        this.mTransitionDuration = paramInt4 / 1000.0F; 
      return;
    } 
    this.mEndState = paramInt1;
    if (paramInt2 != -1) {
      setTransition(paramInt2, paramInt1);
      animateTo(1.0F);
      this.mTransitionLastPosition = 0.0F;
      transitionToEnd();
      if (paramInt4 > 0)
        this.mTransitionDuration = paramInt4 / 1000.0F; 
      return;
    } 
    this.mTemporalInterpolator = false;
    this.mTransitionGoalPosition = 1.0F;
    this.mTransitionPosition = 0.0F;
    this.mTransitionLastPosition = 0.0F;
    this.mTransitionLastTime = getNanoTime();
    this.mAnimationStartTime = getNanoTime();
    this.mTransitionInstantly = false;
    this.mInterpolator = null;
    if (paramInt4 == -1)
      this.mTransitionDuration = this.mScene.getDuration() / 1000.0F; 
    this.mBeginState = -1;
    this.mScene.setTransition(-1, this.mEndState);
    SparseArray sparseArray = new SparseArray();
    if (paramInt4 == 0) {
      this.mTransitionDuration = this.mScene.getDuration() / 1000.0F;
    } else if (paramInt4 > 0) {
      this.mTransitionDuration = paramInt4 / 1000.0F;
    } 
    paramInt3 = getChildCount();
    this.mFrameArrayList.clear();
    for (paramInt2 = 0; paramInt2 < paramInt3; paramInt2++) {
      View view = getChildAt(paramInt2);
      MotionController motionController = new MotionController(view);
      this.mFrameArrayList.put(view, motionController);
      sparseArray.put(view.getId(), this.mFrameArrayList.get(view));
    } 
    this.mInTransition = true;
    this.mModel.initFrom(this.mLayoutWidget, null, this.mScene.getConstraintSet(paramInt1));
    rebuildScene();
    this.mModel.build();
    computeCurrentPositions();
    paramInt2 = getWidth();
    paramInt4 = getHeight();
    if (this.mDecoratorsHelpers != null) {
      for (paramInt1 = 0; paramInt1 < paramInt3; paramInt1++) {
        MotionController motionController = this.mFrameArrayList.get(getChildAt(paramInt1));
        if (motionController != null)
          this.mScene.getKeyFrames(motionController); 
      } 
      Iterator<MotionHelper> iterator = this.mDecoratorsHelpers.iterator();
      while (iterator.hasNext())
        ((MotionHelper)iterator.next()).onPreSetup(this, this.mFrameArrayList); 
      for (paramInt1 = 0; paramInt1 < paramInt3; paramInt1++) {
        MotionController motionController = this.mFrameArrayList.get(getChildAt(paramInt1));
        if (motionController != null)
          motionController.setup(paramInt2, paramInt4, this.mTransitionDuration, getNanoTime()); 
      } 
    } else {
      for (paramInt1 = 0; paramInt1 < paramInt3; paramInt1++) {
        MotionController motionController = this.mFrameArrayList.get(getChildAt(paramInt1));
        if (motionController != null) {
          this.mScene.getKeyFrames(motionController);
          motionController.setup(paramInt2, paramInt4, this.mTransitionDuration, getNanoTime());
        } 
      } 
    } 
    float f = this.mScene.getStaggered();
    if (f != 0.0F) {
      float f2 = Float.MAX_VALUE;
      float f1 = -3.4028235E38F;
      for (paramInt1 = 0; paramInt1 < paramInt3; paramInt1++) {
        MotionController motionController = this.mFrameArrayList.get(getChildAt(paramInt1));
        float f4 = motionController.getFinalX();
        float f3 = motionController.getFinalY();
        f2 = Math.min(f2, f3 + f4);
        f1 = Math.max(f1, f3 + f4);
      } 
      for (paramInt1 = 0; paramInt1 < paramInt3; paramInt1++) {
        MotionController motionController = this.mFrameArrayList.get(getChildAt(paramInt1));
        float f4 = motionController.getFinalX();
        float f3 = motionController.getFinalY();
        motionController.mStaggerScale = 1.0F / (1.0F - f);
        motionController.mStaggerOffset = f - (f4 + f3 - f2) * f / (f1 - f2);
      } 
    } 
    this.mTransitionPosition = 0.0F;
    this.mTransitionLastPosition = 0.0F;
    this.mInTransition = true;
    invalidate();
  }
  
  public void updateState() {
    this.mModel.initFrom(this.mLayoutWidget, this.mScene.getConstraintSet(this.mBeginState), this.mScene.getConstraintSet(this.mEndState));
    rebuildScene();
  }
  
  public void updateState(int paramInt, ConstraintSet paramConstraintSet) {
    MotionScene motionScene = this.mScene;
    if (motionScene != null)
      motionScene.setConstraintSet(paramInt, paramConstraintSet); 
    updateState();
    if (this.mCurrentState == paramInt)
      paramConstraintSet.applyTo(this); 
  }
  
  public void updateStateAnimate(int paramInt1, ConstraintSet paramConstraintSet, int paramInt2) {
    if (this.mScene == null)
      return; 
    if (this.mCurrentState == paramInt1) {
      updateState(R.id.view_transition, getConstraintSet(paramInt1));
      setState(R.id.view_transition, -1, -1);
      updateState(paramInt1, paramConstraintSet);
      MotionScene.Transition transition = new MotionScene.Transition(-1, this.mScene, R.id.view_transition, paramInt1);
      transition.setDuration(paramInt2);
      setTransition(transition);
      transitionToEnd();
    } 
  }
  
  public void viewTransition(int paramInt, View... paramVarArgs) {
    MotionScene motionScene = this.mScene;
    if (motionScene != null) {
      motionScene.viewTransition(paramInt, paramVarArgs);
    } else {
      Log.e("MotionLayout", " no motionScene");
    } 
  }
  
  class DecelerateInterpolator extends MotionInterpolator {
    float currentP = 0.0F;
    
    float initalV = 0.0F;
    
    float maxA;
    
    final MotionLayout this$0;
    
    public void config(float param1Float1, float param1Float2, float param1Float3) {
      this.initalV = param1Float1;
      this.currentP = param1Float2;
      this.maxA = param1Float3;
    }
    
    public float getInterpolation(float param1Float) {
      float f2 = this.initalV;
      if (f2 > 0.0F) {
        float f6 = this.maxA;
        float f5 = param1Float;
        if (f2 / f6 < param1Float)
          f5 = f2 / f6; 
        MotionLayout.this.mLastVelocity = f2 - f6 * f5;
        param1Float = this.initalV;
        f2 = this.maxA * f5 * f5 / 2.0F;
        return this.currentP + param1Float * f5 - f2;
      } 
      float f4 = -f2;
      float f3 = this.maxA;
      float f1 = param1Float;
      if (f4 / f3 < param1Float)
        f1 = -f2 / f3; 
      MotionLayout.this.mLastVelocity = f2 + f3 * f1;
      f2 = this.initalV;
      param1Float = this.maxA * f1 * f1 / 2.0F;
      return this.currentP + f2 * f1 + param1Float;
    }
    
    public float getVelocity() {
      return MotionLayout.this.mLastVelocity;
    }
  }
  
  private class DevModeDraw {
    private static final int DEBUG_PATH_TICKS_PER_MS = 16;
    
    final int DIAMOND_SIZE = 10;
    
    final int GRAPH_COLOR = -13391360;
    
    final int KEYFRAME_COLOR = -2067046;
    
    final int RED_COLOR = -21965;
    
    final int SHADOW_COLOR = 1996488704;
    
    Rect mBounds = new Rect();
    
    DashPathEffect mDashPathEffect;
    
    Paint mFillPaint;
    
    int mKeyFrameCount;
    
    float[] mKeyFramePoints;
    
    Paint mPaint;
    
    Paint mPaintGraph;
    
    Paint mPaintKeyframes;
    
    Path mPath;
    
    int[] mPathMode;
    
    float[] mPoints;
    
    boolean mPresentationMode = false;
    
    private float[] mRectangle;
    
    int mShadowTranslate = 1;
    
    Paint mTextPaint;
    
    final MotionLayout this$0;
    
    public DevModeDraw() {
      Paint paint2 = new Paint();
      this.mPaint = paint2;
      paint2.setAntiAlias(true);
      this.mPaint.setColor(-21965);
      this.mPaint.setStrokeWidth(2.0F);
      this.mPaint.setStyle(Paint.Style.STROKE);
      paint2 = new Paint();
      this.mPaintKeyframes = paint2;
      paint2.setAntiAlias(true);
      this.mPaintKeyframes.setColor(-2067046);
      this.mPaintKeyframes.setStrokeWidth(2.0F);
      this.mPaintKeyframes.setStyle(Paint.Style.STROKE);
      paint2 = new Paint();
      this.mPaintGraph = paint2;
      paint2.setAntiAlias(true);
      this.mPaintGraph.setColor(-13391360);
      this.mPaintGraph.setStrokeWidth(2.0F);
      this.mPaintGraph.setStyle(Paint.Style.STROKE);
      paint2 = new Paint();
      this.mTextPaint = paint2;
      paint2.setAntiAlias(true);
      this.mTextPaint.setColor(-13391360);
      this.mTextPaint.setTextSize((MotionLayout.this.getContext().getResources().getDisplayMetrics()).density * 12.0F);
      this.mRectangle = new float[8];
      Paint paint1 = new Paint();
      this.mFillPaint = paint1;
      paint1.setAntiAlias(true);
      DashPathEffect dashPathEffect = new DashPathEffect(new float[] { 4.0F, 8.0F }, 0.0F);
      this.mDashPathEffect = dashPathEffect;
      this.mPaintGraph.setPathEffect((PathEffect)dashPathEffect);
      this.mKeyFramePoints = new float[100];
      this.mPathMode = new int[50];
      if (this.mPresentationMode) {
        this.mPaint.setStrokeWidth(8.0F);
        this.mFillPaint.setStrokeWidth(8.0F);
        this.mPaintKeyframes.setStrokeWidth(8.0F);
        this.mShadowTranslate = 4;
      } 
    }
    
    private void drawBasicPath(Canvas param1Canvas) {
      param1Canvas.drawLines(this.mPoints, this.mPaint);
    }
    
    private void drawPathAsConfigured(Canvas param1Canvas) {
      boolean bool2 = false;
      boolean bool1 = false;
      for (byte b = 0; b < this.mKeyFrameCount; b++) {
        int[] arrayOfInt = this.mPathMode;
        if (arrayOfInt[b] == 1)
          bool2 = true; 
        if (arrayOfInt[b] == 0)
          bool1 = true; 
      } 
      if (bool2)
        drawPathRelative(param1Canvas); 
      if (bool1)
        drawPathCartesian(param1Canvas); 
    }
    
    private void drawPathCartesian(Canvas param1Canvas) {
      float[] arrayOfFloat = this.mPoints;
      float f3 = arrayOfFloat[0];
      float f4 = arrayOfFloat[1];
      float f2 = arrayOfFloat[arrayOfFloat.length - 2];
      float f1 = arrayOfFloat[arrayOfFloat.length - 1];
      param1Canvas.drawLine(Math.min(f3, f2), Math.max(f4, f1), Math.max(f3, f2), Math.max(f4, f1), this.mPaintGraph);
      param1Canvas.drawLine(Math.min(f3, f2), Math.min(f4, f1), Math.min(f3, f2), Math.max(f4, f1), this.mPaintGraph);
    }
    
    private void drawPathCartesianTicks(Canvas param1Canvas, float param1Float1, float param1Float2) {
      float[] arrayOfFloat = this.mPoints;
      float f5 = arrayOfFloat[0];
      float f3 = arrayOfFloat[1];
      float f9 = arrayOfFloat[arrayOfFloat.length - 2];
      float f2 = arrayOfFloat[arrayOfFloat.length - 1];
      float f7 = Math.min(f5, f9);
      float f4 = Math.max(f3, f2);
      float f8 = param1Float1 - Math.min(f5, f9);
      float f1 = Math.max(f3, f2) - param1Float2;
      float f6 = (int)((f8 * 100.0F / Math.abs(f9 - f5)) + 0.5D) / 100.0F;
      String str = (new StringBuilder(15)).append(f6).toString();
      getTextBounds(str, this.mTextPaint);
      param1Canvas.drawText(str, f8 / 2.0F - (this.mBounds.width() / 2) + f7, param1Float2 - 20.0F, this.mTextPaint);
      param1Canvas.drawLine(param1Float1, param1Float2, Math.min(f5, f9), param1Float2, this.mPaintGraph);
      f5 = (int)((f1 * 100.0F / Math.abs(f2 - f3)) + 0.5D) / 100.0F;
      str = (new StringBuilder(15)).append(f5).toString();
      getTextBounds(str, this.mTextPaint);
      param1Canvas.drawText(str, param1Float1 + 5.0F, f4 - f1 / 2.0F - (this.mBounds.height() / 2), this.mTextPaint);
      param1Canvas.drawLine(param1Float1, param1Float2, param1Float1, Math.max(f3, f2), this.mPaintGraph);
    }
    
    private void drawPathRelative(Canvas param1Canvas) {
      float[] arrayOfFloat = this.mPoints;
      param1Canvas.drawLine(arrayOfFloat[0], arrayOfFloat[1], arrayOfFloat[arrayOfFloat.length - 2], arrayOfFloat[arrayOfFloat.length - 1], this.mPaintGraph);
    }
    
    private void drawPathRelativeTicks(Canvas param1Canvas, float param1Float1, float param1Float2) {
      float[] arrayOfFloat = this.mPoints;
      float f6 = arrayOfFloat[0];
      float f5 = arrayOfFloat[1];
      float f2 = arrayOfFloat[arrayOfFloat.length - 2];
      float f4 = arrayOfFloat[arrayOfFloat.length - 1];
      float f1 = (float)Math.hypot((f6 - f2), (f5 - f4));
      float f3 = ((param1Float1 - f6) * (f2 - f6) + (param1Float2 - f5) * (f4 - f5)) / f1 * f1;
      f2 = f6 + (f2 - f6) * f3;
      f3 = f5 + (f4 - f5) * f3;
      Path path = new Path();
      path.moveTo(param1Float1, param1Float2);
      path.lineTo(f2, f3);
      f4 = (float)Math.hypot((f2 - param1Float1), (f3 - param1Float2));
      f1 = (int)(f4 * 100.0F / f1) / 100.0F;
      String str = (new StringBuilder(15)).append(f1).toString();
      getTextBounds(str, this.mTextPaint);
      param1Canvas.drawTextOnPath(str, path, f4 / 2.0F - (this.mBounds.width() / 2), -20.0F, this.mTextPaint);
      param1Canvas.drawLine(param1Float1, param1Float2, f2, f3, this.mPaintGraph);
    }
    
    private void drawPathScreenTicks(Canvas param1Canvas, float param1Float1, float param1Float2, int param1Int1, int param1Int2) {
      float f = (int)(((param1Float1 - (param1Int1 / 2)) * 100.0F / (MotionLayout.this.getWidth() - param1Int1)) + 0.5D) / 100.0F;
      String str = (new StringBuilder(15)).append(f).toString();
      getTextBounds(str, this.mTextPaint);
      param1Canvas.drawText(str, param1Float1 / 2.0F - (this.mBounds.width() / 2) + 0.0F, param1Float2 - 20.0F, this.mTextPaint);
      param1Canvas.drawLine(param1Float1, param1Float2, Math.min(0.0F, 1.0F), param1Float2, this.mPaintGraph);
      f = (int)(((param1Float2 - (param1Int2 / 2)) * 100.0F / (MotionLayout.this.getHeight() - param1Int2)) + 0.5D) / 100.0F;
      str = (new StringBuilder(15)).append(f).toString();
      getTextBounds(str, this.mTextPaint);
      param1Canvas.drawText(str, param1Float1 + 5.0F, 0.0F - param1Float2 / 2.0F - (this.mBounds.height() / 2), this.mTextPaint);
      param1Canvas.drawLine(param1Float1, param1Float2, param1Float1, Math.max(0.0F, 1.0F), this.mPaintGraph);
    }
    
    private void drawRectangle(Canvas param1Canvas, MotionController param1MotionController) {
      this.mPath.reset();
      for (byte b = 0; b <= 50; b++) {
        param1MotionController.buildRect(b / 50, this.mRectangle, 0);
        Path path2 = this.mPath;
        float[] arrayOfFloat1 = this.mRectangle;
        path2.moveTo(arrayOfFloat1[0], arrayOfFloat1[1]);
        path2 = this.mPath;
        arrayOfFloat1 = this.mRectangle;
        path2.lineTo(arrayOfFloat1[2], arrayOfFloat1[3]);
        Path path1 = this.mPath;
        float[] arrayOfFloat2 = this.mRectangle;
        path1.lineTo(arrayOfFloat2[4], arrayOfFloat2[5]);
        path1 = this.mPath;
        arrayOfFloat2 = this.mRectangle;
        path1.lineTo(arrayOfFloat2[6], arrayOfFloat2[7]);
        this.mPath.close();
      } 
      this.mPaint.setColor(1140850688);
      param1Canvas.translate(2.0F, 2.0F);
      param1Canvas.drawPath(this.mPath, this.mPaint);
      param1Canvas.translate(-2.0F, -2.0F);
      this.mPaint.setColor(-65536);
      param1Canvas.drawPath(this.mPath, this.mPaint);
    }
    
    private void drawTicks(Canvas param1Canvas, int param1Int1, int param1Int2, MotionController param1MotionController) {
      boolean bool1;
      boolean bool2;
      if (param1MotionController.mView != null) {
        bool1 = param1MotionController.mView.getWidth();
        bool2 = param1MotionController.mView.getHeight();
      } else {
        bool1 = false;
        bool2 = false;
      } 
      for (byte b = 1; b < param1Int2 - 1; b++) {
        if (param1Int1 != 4 || this.mPathMode[b - 1] != 0) {
          float[] arrayOfFloat1 = this.mKeyFramePoints;
          float f1 = arrayOfFloat1[b * 2];
          float f2 = arrayOfFloat1[b * 2 + 1];
          this.mPath.reset();
          this.mPath.moveTo(f1, f2 + 10.0F);
          this.mPath.lineTo(f1 + 10.0F, f2);
          this.mPath.lineTo(f1, f2 - 10.0F);
          this.mPath.lineTo(f1 - 10.0F, f2);
          this.mPath.close();
          param1MotionController.getKeyFrame(b - 1);
          if (param1Int1 == 4) {
            int[] arrayOfInt = this.mPathMode;
            if (arrayOfInt[b - 1] == 1) {
              drawPathRelativeTicks(param1Canvas, f1 - 0.0F, f2 - 0.0F);
            } else if (arrayOfInt[b - 1] == 0) {
              drawPathCartesianTicks(param1Canvas, f1 - 0.0F, f2 - 0.0F);
            } else if (arrayOfInt[b - 1] == 2) {
              drawPathScreenTicks(param1Canvas, f1 - 0.0F, f2 - 0.0F, bool1, bool2);
            } 
            param1Canvas.drawPath(this.mPath, this.mFillPaint);
          } 
          if (param1Int1 == 2)
            drawPathRelativeTicks(param1Canvas, f1 - 0.0F, f2 - 0.0F); 
          if (param1Int1 == 3)
            drawPathCartesianTicks(param1Canvas, f1 - 0.0F, f2 - 0.0F); 
          if (param1Int1 == 6)
            drawPathScreenTicks(param1Canvas, f1 - 0.0F, f2 - 0.0F, bool1, bool2); 
          if (0.0F != 0.0F || 0.0F != 0.0F) {
            drawTranslation(param1Canvas, f1 - 0.0F, f2 - 0.0F, f1, f2);
          } else {
            param1Canvas.drawPath(this.mPath, this.mFillPaint);
          } 
        } 
      } 
      float[] arrayOfFloat = this.mPoints;
      if (arrayOfFloat.length > 1) {
        param1Canvas.drawCircle(arrayOfFloat[0], arrayOfFloat[1], 8.0F, this.mPaintKeyframes);
        arrayOfFloat = this.mPoints;
        param1Canvas.drawCircle(arrayOfFloat[arrayOfFloat.length - 2], arrayOfFloat[arrayOfFloat.length - 1], 8.0F, this.mPaintKeyframes);
      } 
    }
    
    private void drawTranslation(Canvas param1Canvas, float param1Float1, float param1Float2, float param1Float3, float param1Float4) {
      param1Canvas.drawRect(param1Float1, param1Float2, param1Float3, param1Float4, this.mPaintGraph);
      param1Canvas.drawLine(param1Float1, param1Float2, param1Float3, param1Float4, this.mPaintGraph);
    }
    
    public void draw(Canvas param1Canvas, HashMap<View, MotionController> param1HashMap, int param1Int1, int param1Int2) {
      if (param1HashMap == null || param1HashMap.size() == 0)
        return; 
      param1Canvas.save();
      if (!MotionLayout.this.isInEditMode() && (param1Int2 & 0x1) == 2) {
        String str = MotionLayout.this.getContext().getResources().getResourceName(MotionLayout.this.mEndState);
        float f = MotionLayout.this.getProgress();
        str = (new StringBuilder(String.valueOf(str).length() + 16)).append(str).append(":").append(f).toString();
        param1Canvas.drawText(str, 10.0F, (MotionLayout.this.getHeight() - 30), this.mTextPaint);
        param1Canvas.drawText(str, 11.0F, (MotionLayout.this.getHeight() - 29), this.mPaint);
      } 
      for (MotionController motionController : param1HashMap.values()) {
        int j = motionController.getDrawPath();
        int i = j;
        if (param1Int2 > 0) {
          i = j;
          if (j == 0)
            i = 1; 
        } 
        if (i == 0)
          continue; 
        this.mKeyFrameCount = motionController.buildKeyFrames(this.mKeyFramePoints, this.mPathMode);
        if (i >= 1) {
          j = param1Int1 / 16;
          float[] arrayOfFloat = this.mPoints;
          if (arrayOfFloat == null || arrayOfFloat.length != j * 2) {
            this.mPoints = new float[j * 2];
            this.mPath = new Path();
          } 
          int k = this.mShadowTranslate;
          param1Canvas.translate(k, k);
          this.mPaint.setColor(1996488704);
          this.mFillPaint.setColor(1996488704);
          this.mPaintKeyframes.setColor(1996488704);
          this.mPaintGraph.setColor(1996488704);
          motionController.buildPath(this.mPoints, j);
          drawAll(param1Canvas, i, this.mKeyFrameCount, motionController);
          this.mPaint.setColor(-21965);
          this.mPaintKeyframes.setColor(-2067046);
          this.mFillPaint.setColor(-2067046);
          this.mPaintGraph.setColor(-13391360);
          j = this.mShadowTranslate;
          param1Canvas.translate(-j, -j);
          drawAll(param1Canvas, i, this.mKeyFrameCount, motionController);
          if (i == 5)
            drawRectangle(param1Canvas, motionController); 
        } 
      } 
      param1Canvas.restore();
    }
    
    public void drawAll(Canvas param1Canvas, int param1Int1, int param1Int2, MotionController param1MotionController) {
      if (param1Int1 == 4)
        drawPathAsConfigured(param1Canvas); 
      if (param1Int1 == 2)
        drawPathRelative(param1Canvas); 
      if (param1Int1 == 3)
        drawPathCartesian(param1Canvas); 
      drawBasicPath(param1Canvas);
      drawTicks(param1Canvas, param1Int1, param1Int2, param1MotionController);
    }
    
    void getTextBounds(String param1String, Paint param1Paint) {
      param1Paint.getTextBounds(param1String, 0, param1String.length(), this.mBounds);
    }
  }
  
  class Model {
    ConstraintSet mEnd = null;
    
    int mEndId;
    
    ConstraintWidgetContainer mLayoutEnd = new ConstraintWidgetContainer();
    
    ConstraintWidgetContainer mLayoutStart = new ConstraintWidgetContainer();
    
    ConstraintSet mStart = null;
    
    int mStartId;
    
    final MotionLayout this$0;
    
    private void computeStartEndSize(int param1Int1, int param1Int2) {
      int i = MotionLayout.this.getOptimizationLevel();
      if (MotionLayout.this.mCurrentState == MotionLayout.this.getStartState()) {
        int j;
        int k;
        MotionLayout motionLayout = MotionLayout.this;
        ConstraintWidgetContainer constraintWidgetContainer = this.mLayoutEnd;
        ConstraintSet constraintSet2 = this.mEnd;
        if (constraintSet2 == null || constraintSet2.mRotate == 0) {
          j = param1Int1;
        } else {
          j = param1Int2;
        } 
        constraintSet2 = this.mEnd;
        if (constraintSet2 == null || constraintSet2.mRotate == 0) {
          k = param1Int2;
        } else {
          k = param1Int1;
        } 
        motionLayout.resolveSystem(constraintWidgetContainer, i, j, k);
        ConstraintSet constraintSet1 = this.mStart;
        if (constraintSet1 != null) {
          MotionLayout motionLayout1 = MotionLayout.this;
          ConstraintWidgetContainer constraintWidgetContainer1 = this.mLayoutStart;
          if (constraintSet1.mRotate == 0) {
            j = param1Int1;
          } else {
            j = param1Int2;
          } 
          if (this.mStart.mRotate == 0)
            param1Int1 = param1Int2; 
          motionLayout1.resolveSystem(constraintWidgetContainer1, i, j, param1Int1);
        } 
      } else {
        int j;
        ConstraintSet constraintSet1 = this.mStart;
        if (constraintSet1 != null) {
          int k;
          MotionLayout motionLayout1 = MotionLayout.this;
          ConstraintWidgetContainer constraintWidgetContainer1 = this.mLayoutStart;
          if (constraintSet1.mRotate == 0) {
            j = param1Int1;
          } else {
            j = param1Int2;
          } 
          if (this.mStart.mRotate == 0) {
            k = param1Int2;
          } else {
            k = param1Int1;
          } 
          motionLayout1.resolveSystem(constraintWidgetContainer1, i, j, k);
        } 
        MotionLayout motionLayout = MotionLayout.this;
        ConstraintWidgetContainer constraintWidgetContainer = this.mLayoutEnd;
        ConstraintSet constraintSet2 = this.mEnd;
        if (constraintSet2 == null || constraintSet2.mRotate == 0) {
          j = param1Int1;
        } else {
          j = param1Int2;
        } 
        constraintSet2 = this.mEnd;
        if (constraintSet2 == null || constraintSet2.mRotate == 0)
          param1Int1 = param1Int2; 
        motionLayout.resolveSystem(constraintWidgetContainer, i, j, param1Int1);
      } 
    }
    
    private void debugLayout(String param1String, ConstraintWidgetContainer param1ConstraintWidgetContainer) {
      String str1 = Debug.getName((View)param1ConstraintWidgetContainer.getCompanionWidget());
      String str2 = (new StringBuilder(String.valueOf(param1String).length() + 1 + String.valueOf(str1).length())).append(param1String).append(" ").append(str1).toString();
      param1String = String.valueOf(param1ConstraintWidgetContainer);
      Log.v("MotionLayout", (new StringBuilder(String.valueOf(str2).length() + 12 + String.valueOf(param1String).length())).append(str2).append("  ========= ").append(param1String).toString());
      int i = param1ConstraintWidgetContainer.getChildren().size();
      for (byte b = 0; b < i; b++) {
        String str5 = (new StringBuilder(String.valueOf(str2).length() + 14)).append(str2).append("[").append(b).append("] ").toString();
        ConstraintWidget constraintWidget = param1ConstraintWidgetContainer.getChildren().get(b);
        String str4 = String.valueOf("");
        ConstraintAnchor constraintAnchor = constraintWidget.mTop.mTarget;
        str1 = "_";
        if (constraintAnchor != null) {
          str3 = "T";
        } else {
          str3 = "_";
        } 
        String str3 = String.valueOf(str3);
        if (str3.length() != 0) {
          str3 = str4.concat(str3);
        } else {
          str3 = new String(str4);
        } 
        str4 = String.valueOf(str3);
        if (constraintWidget.mBottom.mTarget != null) {
          str3 = "B";
        } else {
          str3 = "_";
        } 
        str3 = String.valueOf(str3);
        if (str3.length() != 0) {
          str3 = str4.concat(str3);
        } else {
          str3 = new String(str4);
        } 
        str4 = String.valueOf(str3);
        if (constraintWidget.mLeft.mTarget != null) {
          str3 = "L";
        } else {
          str3 = "_";
        } 
        str3 = String.valueOf(str3);
        if (str3.length() != 0) {
          str3 = str4.concat(str3);
        } else {
          str3 = new String(str4);
        } 
        str4 = String.valueOf(str3);
        str3 = str1;
        if (constraintWidget.mRight.mTarget != null)
          str3 = "R"; 
        str3 = String.valueOf(str3);
        if (str3.length() != 0) {
          str3 = str4.concat(str3);
        } else {
          str3 = new String(str4);
        } 
        View view = (View)constraintWidget.getCompanionWidget();
        str4 = Debug.getName(view);
        str1 = str4;
        if (view instanceof TextView) {
          str1 = String.valueOf(str4);
          str4 = String.valueOf(((TextView)view).getText());
          str1 = (new StringBuilder(String.valueOf(str1).length() + 2 + String.valueOf(str4).length())).append(str1).append("(").append(str4).append(")").toString();
        } 
        str4 = String.valueOf(constraintWidget);
        Log.v("MotionLayout", (new StringBuilder(String.valueOf(str5).length() + 4 + String.valueOf(str1).length() + String.valueOf(str4).length() + String.valueOf(str3).length())).append(str5).append("  ").append(str1).append(" ").append(str4).append(" ").append(str3).toString());
      } 
      Log.v("MotionLayout", String.valueOf(str2).concat(" done. "));
    }
    
    private void debugLayoutParam(String param1String, ConstraintLayout.LayoutParams param1LayoutParams) {
      String str3 = String.valueOf(" ");
      if (param1LayoutParams.startToStart != -1) {
        str2 = "SS";
      } else {
        str2 = "__";
      } 
      String str2 = String.valueOf(str2);
      if (str2.length() != 0) {
        str2 = str3.concat(str2);
      } else {
        str2 = new String(str3);
      } 
      String str4 = String.valueOf(str2);
      int i = param1LayoutParams.startToEnd;
      str3 = "|__";
      if (i != -1) {
        str2 = "|SE";
      } else {
        str2 = "|__";
      } 
      str2 = String.valueOf(str2);
      if (str2.length() != 0) {
        str2 = str4.concat(str2);
      } else {
        str2 = new String(str4);
      } 
      str4 = String.valueOf(str2);
      if (param1LayoutParams.endToStart != -1) {
        str2 = "|ES";
      } else {
        str2 = "|__";
      } 
      str2 = String.valueOf(str2);
      if (str2.length() != 0) {
        str2 = str4.concat(str2);
      } else {
        str2 = new String(str4);
      } 
      str4 = String.valueOf(str2);
      if (param1LayoutParams.endToEnd != -1) {
        str2 = "|EE";
      } else {
        str2 = "|__";
      } 
      str2 = String.valueOf(str2);
      if (str2.length() != 0) {
        str2 = str4.concat(str2);
      } else {
        str2 = new String(str4);
      } 
      str4 = String.valueOf(str2);
      if (param1LayoutParams.leftToLeft != -1) {
        str2 = "|LL";
      } else {
        str2 = "|__";
      } 
      str2 = String.valueOf(str2);
      if (str2.length() != 0) {
        str2 = str4.concat(str2);
      } else {
        str2 = new String(str4);
      } 
      str4 = String.valueOf(str2);
      if (param1LayoutParams.leftToRight != -1) {
        str2 = "|LR";
      } else {
        str2 = "|__";
      } 
      str2 = String.valueOf(str2);
      if (str2.length() != 0) {
        str2 = str4.concat(str2);
      } else {
        str2 = new String(str4);
      } 
      str4 = String.valueOf(str2);
      if (param1LayoutParams.rightToLeft != -1) {
        str2 = "|RL";
      } else {
        str2 = "|__";
      } 
      str2 = String.valueOf(str2);
      if (str2.length() != 0) {
        str2 = str4.concat(str2);
      } else {
        str2 = new String(str4);
      } 
      str4 = String.valueOf(str2);
      if (param1LayoutParams.rightToRight != -1) {
        str2 = "|RR";
      } else {
        str2 = "|__";
      } 
      str2 = String.valueOf(str2);
      if (str2.length() != 0) {
        str2 = str4.concat(str2);
      } else {
        str2 = new String(str4);
      } 
      str4 = String.valueOf(str2);
      if (param1LayoutParams.topToTop != -1) {
        str2 = "|TT";
      } else {
        str2 = "|__";
      } 
      str2 = String.valueOf(str2);
      if (str2.length() != 0) {
        str2 = str4.concat(str2);
      } else {
        str2 = new String(str4);
      } 
      str4 = String.valueOf(str2);
      if (param1LayoutParams.topToBottom != -1) {
        str2 = "|TB";
      } else {
        str2 = "|__";
      } 
      str2 = String.valueOf(str2);
      if (str2.length() != 0) {
        str2 = str4.concat(str2);
      } else {
        str2 = new String(str4);
      } 
      str4 = String.valueOf(str2);
      if (param1LayoutParams.bottomToTop != -1) {
        str2 = "|BT";
      } else {
        str2 = "|__";
      } 
      str2 = String.valueOf(str2);
      if (str2.length() != 0) {
        str2 = str4.concat(str2);
      } else {
        str2 = new String(str4);
      } 
      str4 = String.valueOf(str2);
      str2 = str3;
      if (param1LayoutParams.bottomToBottom != -1)
        str2 = "|BB"; 
      String str1 = String.valueOf(str2);
      if (str1.length() != 0) {
        str1 = str4.concat(str1);
      } else {
        str1 = new String(str4);
      } 
      param1String = String.valueOf(param1String);
      str1 = String.valueOf(str1);
      if (str1.length() != 0) {
        param1String = param1String.concat(str1);
      } else {
        param1String = new String(param1String);
      } 
      Log.v("MotionLayout", param1String);
    }
    
    private void debugWidget(String param1String, ConstraintWidget param1ConstraintWidget) {
      String str6 = String.valueOf(" ");
      ConstraintAnchor constraintAnchor2 = param1ConstraintWidget.mTop.mTarget;
      String str5 = "T";
      String str4 = "__";
      if (constraintAnchor2 != null) {
        if (param1ConstraintWidget.mTop.mTarget.mType == ConstraintAnchor.Type.TOP) {
          str3 = "T";
        } else {
          str3 = "B";
        } 
        str3 = String.valueOf(str3);
        if (str3.length() != 0) {
          str3 = "T".concat(str3);
        } else {
          str3 = new String("T");
        } 
      } else {
        str3 = "__";
      } 
      String str3 = String.valueOf(str3);
      if (str3.length() != 0) {
        str3 = str6.concat(str3);
      } else {
        str3 = new String(str6);
      } 
      str6 = String.valueOf(str3);
      if (param1ConstraintWidget.mBottom.mTarget != null) {
        if (param1ConstraintWidget.mBottom.mTarget.mType == ConstraintAnchor.Type.TOP) {
          str3 = str5;
        } else {
          str3 = "B";
        } 
        str3 = String.valueOf(str3);
        if (str3.length() != 0) {
          str3 = "B".concat(str3);
        } else {
          str3 = new String("B");
        } 
      } else {
        str3 = "__";
      } 
      str3 = String.valueOf(str3);
      if (str3.length() != 0) {
        str3 = str6.concat(str3);
      } else {
        str3 = new String(str6);
      } 
      str6 = String.valueOf(str3);
      ConstraintAnchor constraintAnchor1 = param1ConstraintWidget.mLeft.mTarget;
      str5 = "L";
      if (constraintAnchor1 != null) {
        if (param1ConstraintWidget.mLeft.mTarget.mType == ConstraintAnchor.Type.LEFT) {
          str2 = "L";
        } else {
          str2 = "R";
        } 
        str2 = String.valueOf(str2);
        if (str2.length() != 0) {
          str2 = "L".concat(str2);
        } else {
          str2 = new String("L");
        } 
      } else {
        str2 = "__";
      } 
      String str2 = String.valueOf(str2);
      if (str2.length() != 0) {
        str2 = str6.concat(str2);
      } else {
        str2 = new String(str6);
      } 
      str6 = String.valueOf(str2);
      str2 = str4;
      if (param1ConstraintWidget.mRight.mTarget != null) {
        if (param1ConstraintWidget.mRight.mTarget.mType == ConstraintAnchor.Type.LEFT) {
          str2 = str5;
        } else {
          str2 = "R";
        } 
        str2 = String.valueOf(str2);
        if (str2.length() != 0) {
          str2 = "R".concat(str2);
        } else {
          str2 = new String("R");
        } 
      } 
      str2 = String.valueOf(str2);
      if (str2.length() != 0) {
        str2 = str6.concat(str2);
      } else {
        str2 = new String(str6);
      } 
      String str1 = String.valueOf(param1ConstraintWidget);
      Log.v("MotionLayout", (new StringBuilder(String.valueOf(param1String).length() + 6 + String.valueOf(str2).length() + String.valueOf(str1).length())).append(param1String).append(str2).append(" ---  ").append(str1).toString());
    }
    
    private void setupConstraintWidget(ConstraintWidgetContainer param1ConstraintWidgetContainer, ConstraintSet param1ConstraintSet) {
      SparseArray sparseArray = new SparseArray();
      Constraints.LayoutParams layoutParams = new Constraints.LayoutParams(-2, -2);
      sparseArray.clear();
      sparseArray.put(0, param1ConstraintWidgetContainer);
      sparseArray.put(MotionLayout.this.getId(), param1ConstraintWidgetContainer);
      if (param1ConstraintSet != null && param1ConstraintSet.mRotate != 0) {
        MotionLayout motionLayout = MotionLayout.this;
        motionLayout.resolveSystem(this.mLayoutEnd, motionLayout.getOptimizationLevel(), View.MeasureSpec.makeMeasureSpec(MotionLayout.this.getHeight(), 1073741824), View.MeasureSpec.makeMeasureSpec(MotionLayout.this.getWidth(), 1073741824));
      } 
      for (ConstraintWidget constraintWidget : param1ConstraintWidgetContainer.getChildren())
        sparseArray.put(((View)constraintWidget.getCompanionWidget()).getId(), constraintWidget); 
      for (ConstraintWidget constraintWidget : param1ConstraintWidgetContainer.getChildren()) {
        View view = (View)constraintWidget.getCompanionWidget();
        param1ConstraintSet.applyToLayoutParams(view.getId(), (ConstraintLayout.LayoutParams)layoutParams);
        constraintWidget.setWidth(param1ConstraintSet.getWidth(view.getId()));
        constraintWidget.setHeight(param1ConstraintSet.getHeight(view.getId()));
        if (view instanceof ConstraintHelper) {
          param1ConstraintSet.applyToHelper((ConstraintHelper)view, constraintWidget, (ConstraintLayout.LayoutParams)layoutParams, sparseArray);
          if (view instanceof Barrier)
            ((Barrier)view).validateParams(); 
        } 
        if (Build.VERSION.SDK_INT >= 17) {
          layoutParams.resolveLayoutDirection(MotionLayout.this.getLayoutDirection());
        } else {
          layoutParams.resolveLayoutDirection(0);
        } 
        MotionLayout.this.applyConstraintsFromLayoutParams(false, view, constraintWidget, (ConstraintLayout.LayoutParams)layoutParams, sparseArray);
        if (param1ConstraintSet.getVisibilityMode(view.getId()) == 1) {
          constraintWidget.setVisibility(view.getVisibility());
          continue;
        } 
        constraintWidget.setVisibility(param1ConstraintSet.getVisibility(view.getId()));
      } 
      for (ConstraintWidget constraintWidget : param1ConstraintWidgetContainer.getChildren()) {
        if (constraintWidget instanceof VirtualLayout) {
          ConstraintHelper constraintHelper = (ConstraintHelper)constraintWidget.getCompanionWidget();
          Helper helper = (Helper)constraintWidget;
          constraintHelper.updatePreLayout(param1ConstraintWidgetContainer, helper, sparseArray);
          ((VirtualLayout)helper).captureWidgets();
        } 
      } 
    }
    
    public void build() {
      int i = MotionLayout.this.getChildCount();
      MotionLayout.this.mFrameArrayList.clear();
      SparseArray sparseArray = new SparseArray();
      int[] arrayOfInt = new int[i];
      byte b;
      for (b = 0; b < i; b++) {
        View view = MotionLayout.this.getChildAt(b);
        MotionController motionController = new MotionController(view);
        int j = view.getId();
        arrayOfInt[b] = j;
        sparseArray.put(j, motionController);
        MotionLayout.this.mFrameArrayList.put(view, motionController);
      } 
      for (b = 0; b < i; b++) {
        View view = MotionLayout.this.getChildAt(b);
        MotionController motionController = MotionLayout.this.mFrameArrayList.get(view);
        if (motionController != null) {
          if (this.mStart != null) {
            ConstraintWidget constraintWidget = getWidget(this.mLayoutStart, view);
            if (constraintWidget != null) {
              motionController.setStartState(MotionLayout.this.toRect(constraintWidget), this.mStart, MotionLayout.this.getWidth(), MotionLayout.this.getHeight());
            } else if (MotionLayout.this.mDebugPath != 0) {
              String str2 = Debug.getLocation();
              String str3 = Debug.getName(view);
              String str1 = view.getClass().getName();
              Log.e("MotionLayout", (new StringBuilder(String.valueOf(str2).length() + 18 + String.valueOf(str3).length() + String.valueOf(str1).length())).append(str2).append("no widget for  ").append(str3).append(" (").append(str1).append(")").toString());
            } 
          } else if (MotionLayout.this.mInRotation) {
            motionController.setStartState(MotionLayout.this.mPreRotate.get(view), view, MotionLayout.this.mRotatMode, MotionLayout.this.mPreRotateWidth, MotionLayout.this.mPreRotateHeight);
          } 
          if (this.mEnd != null) {
            ConstraintWidget constraintWidget = getWidget(this.mLayoutEnd, view);
            if (constraintWidget != null) {
              motionController.setEndState(MotionLayout.this.toRect(constraintWidget), this.mEnd, MotionLayout.this.getWidth(), MotionLayout.this.getHeight());
            } else if (MotionLayout.this.mDebugPath != 0) {
              String str3 = Debug.getLocation();
              String str2 = Debug.getName(view);
              String str1 = view.getClass().getName();
              Log.e("MotionLayout", (new StringBuilder(String.valueOf(str3).length() + 18 + String.valueOf(str2).length() + String.valueOf(str1).length())).append(str3).append("no widget for  ").append(str2).append(" (").append(str1).append(")").toString());
            } 
          } 
        } 
      } 
      for (b = 0; b < i; b++) {
        MotionController motionController = (MotionController)sparseArray.get(arrayOfInt[b]);
        int j = motionController.getAnimateRelativeTo();
        if (j != -1)
          motionController.setupRelative((MotionController)sparseArray.get(j)); 
      } 
    }
    
    void copy(ConstraintWidgetContainer param1ConstraintWidgetContainer1, ConstraintWidgetContainer param1ConstraintWidgetContainer2) {
      ArrayList arrayList = param1ConstraintWidgetContainer1.getChildren();
      HashMap<Object, Object> hashMap = new HashMap<>();
      hashMap.put(param1ConstraintWidgetContainer1, param1ConstraintWidgetContainer2);
      param1ConstraintWidgetContainer2.getChildren().clear();
      param1ConstraintWidgetContainer2.copy((ConstraintWidget)param1ConstraintWidgetContainer1, hashMap);
      for (ConstraintWidget constraintWidget2 : arrayList) {
        ConstraintWidget constraintWidget1;
        if (constraintWidget2 instanceof Barrier) {
          Barrier barrier = new Barrier();
        } else if (constraintWidget2 instanceof Guideline) {
          Guideline guideline = new Guideline();
        } else if (constraintWidget2 instanceof Flow) {
          Flow flow = new Flow();
        } else if (constraintWidget2 instanceof Placeholder) {
          Placeholder placeholder = new Placeholder();
        } else if (constraintWidget2 instanceof Helper) {
          HelperWidget helperWidget = new HelperWidget();
        } else {
          constraintWidget1 = new ConstraintWidget();
        } 
        param1ConstraintWidgetContainer2.add(constraintWidget1);
        hashMap.put(constraintWidget2, constraintWidget1);
      } 
      for (ConstraintWidget constraintWidget : arrayList)
        ((ConstraintWidget)hashMap.get(constraintWidget)).copy(constraintWidget, hashMap); 
    }
    
    ConstraintWidget getWidget(ConstraintWidgetContainer param1ConstraintWidgetContainer, View param1View) {
      if (param1ConstraintWidgetContainer.getCompanionWidget() == param1View)
        return (ConstraintWidget)param1ConstraintWidgetContainer; 
      ArrayList<ConstraintWidget> arrayList = param1ConstraintWidgetContainer.getChildren();
      int i = arrayList.size();
      for (byte b = 0; b < i; b++) {
        ConstraintWidget constraintWidget = arrayList.get(b);
        if (constraintWidget.getCompanionWidget() == param1View)
          return constraintWidget; 
      } 
      return null;
    }
    
    void initFrom(ConstraintWidgetContainer param1ConstraintWidgetContainer, ConstraintSet param1ConstraintSet1, ConstraintSet param1ConstraintSet2) {
      this.mStart = param1ConstraintSet1;
      this.mEnd = param1ConstraintSet2;
      this.mLayoutStart = new ConstraintWidgetContainer();
      this.mLayoutEnd = new ConstraintWidgetContainer();
      this.mLayoutStart.setMeasurer(MotionLayout.this.mLayoutWidget.getMeasurer());
      this.mLayoutEnd.setMeasurer(MotionLayout.this.mLayoutWidget.getMeasurer());
      this.mLayoutStart.removeAllChildren();
      this.mLayoutEnd.removeAllChildren();
      copy(MotionLayout.this.mLayoutWidget, this.mLayoutStart);
      copy(MotionLayout.this.mLayoutWidget, this.mLayoutEnd);
      if (MotionLayout.this.mTransitionLastPosition > 0.5D) {
        if (param1ConstraintSet1 != null)
          setupConstraintWidget(this.mLayoutStart, param1ConstraintSet1); 
        setupConstraintWidget(this.mLayoutEnd, param1ConstraintSet2);
      } else {
        setupConstraintWidget(this.mLayoutEnd, param1ConstraintSet2);
        if (param1ConstraintSet1 != null)
          setupConstraintWidget(this.mLayoutStart, param1ConstraintSet1); 
      } 
      this.mLayoutStart.setRtl(MotionLayout.this.isRtl());
      this.mLayoutStart.updateHierarchy();
      this.mLayoutEnd.setRtl(MotionLayout.this.isRtl());
      this.mLayoutEnd.updateHierarchy();
      ViewGroup.LayoutParams layoutParams = MotionLayout.this.getLayoutParams();
      if (layoutParams != null) {
        if (layoutParams.width == -2) {
          this.mLayoutStart.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.WRAP_CONTENT);
          this.mLayoutEnd.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.WRAP_CONTENT);
        } 
        if (layoutParams.height == -2) {
          this.mLayoutStart.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.WRAP_CONTENT);
          this.mLayoutEnd.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.WRAP_CONTENT);
        } 
      } 
    }
    
    public boolean isNotConfiguredWith(int param1Int1, int param1Int2) {
      return (param1Int1 != this.mStartId || param1Int2 != this.mEndId);
    }
    
    public void measure(int param1Int1, int param1Int2) {
      boolean bool1;
      boolean bool2;
      int k = View.MeasureSpec.getMode(param1Int1);
      int m = View.MeasureSpec.getMode(param1Int2);
      MotionLayout.this.mWidthMeasureMode = k;
      MotionLayout.this.mHeightMeasureMode = m;
      MotionLayout.this.getOptimizationLevel();
      computeStartEndSize(param1Int1, param1Int2);
      int j = 1;
      int i = j;
      if (MotionLayout.this.getParent() instanceof MotionLayout) {
        i = j;
        if (k == 1073741824) {
          i = j;
          if (m == 1073741824)
            i = 0; 
        } 
      } 
      if (i) {
        computeStartEndSize(param1Int1, param1Int2);
        MotionLayout.this.mStartWrapWidth = this.mLayoutStart.getWidth();
        MotionLayout.this.mStartWrapHeight = this.mLayoutStart.getHeight();
        MotionLayout.this.mEndWrapWidth = this.mLayoutEnd.getWidth();
        MotionLayout.this.mEndWrapHeight = this.mLayoutEnd.getHeight();
        MotionLayout motionLayout = MotionLayout.this;
        if (motionLayout.mStartWrapWidth != MotionLayout.this.mEndWrapWidth || MotionLayout.this.mStartWrapHeight != MotionLayout.this.mEndWrapHeight) {
          bool1 = true;
        } else {
          bool1 = false;
        } 
        motionLayout.mMeasureDuringTransition = bool1;
      } 
      i = MotionLayout.this.mStartWrapWidth;
      j = MotionLayout.this.mStartWrapHeight;
      if (MotionLayout.this.mWidthMeasureMode == Integer.MIN_VALUE || MotionLayout.this.mWidthMeasureMode == 0)
        i = (int)(MotionLayout.this.mStartWrapWidth + MotionLayout.this.mPostInterpolationPosition * (MotionLayout.this.mEndWrapWidth - MotionLayout.this.mStartWrapWidth)); 
      if (MotionLayout.this.mHeightMeasureMode == Integer.MIN_VALUE || MotionLayout.this.mHeightMeasureMode == 0)
        j = (int)(MotionLayout.this.mStartWrapHeight + MotionLayout.this.mPostInterpolationPosition * (MotionLayout.this.mEndWrapHeight - MotionLayout.this.mStartWrapHeight)); 
      if (this.mLayoutStart.isWidthMeasuredTooSmall() || this.mLayoutEnd.isWidthMeasuredTooSmall()) {
        bool1 = true;
      } else {
        bool1 = false;
      } 
      if (this.mLayoutStart.isHeightMeasuredTooSmall() || this.mLayoutEnd.isHeightMeasuredTooSmall()) {
        bool2 = true;
      } else {
        bool2 = false;
      } 
      MotionLayout.this.resolveMeasuredDimension(param1Int1, param1Int2, i, j, bool1, bool2);
    }
    
    public void reEvaluateState() {
      measure(MotionLayout.this.mLastWidthMeasureSpec, MotionLayout.this.mLastHeightMeasureSpec);
      MotionLayout.this.setupMotionViews();
    }
    
    public void setMeasuredId(int param1Int1, int param1Int2) {
      this.mStartId = param1Int1;
      this.mEndId = param1Int2;
    }
  }
  
  protected static interface MotionTracker {
    void addMovement(MotionEvent param1MotionEvent);
    
    void clear();
    
    void computeCurrentVelocity(int param1Int);
    
    void computeCurrentVelocity(int param1Int, float param1Float);
    
    float getXVelocity();
    
    float getXVelocity(int param1Int);
    
    float getYVelocity();
    
    float getYVelocity(int param1Int);
    
    void recycle();
  }
  
  private static class MyTracker implements MotionTracker {
    private static MyTracker me = new MyTracker();
    
    VelocityTracker tracker;
    
    public static MyTracker obtain() {
      me.tracker = VelocityTracker.obtain();
      return me;
    }
    
    public void addMovement(MotionEvent param1MotionEvent) {
      VelocityTracker velocityTracker = this.tracker;
      if (velocityTracker != null)
        velocityTracker.addMovement(param1MotionEvent); 
    }
    
    public void clear() {
      VelocityTracker velocityTracker = this.tracker;
      if (velocityTracker != null)
        velocityTracker.clear(); 
    }
    
    public void computeCurrentVelocity(int param1Int) {
      VelocityTracker velocityTracker = this.tracker;
      if (velocityTracker != null)
        velocityTracker.computeCurrentVelocity(param1Int); 
    }
    
    public void computeCurrentVelocity(int param1Int, float param1Float) {
      VelocityTracker velocityTracker = this.tracker;
      if (velocityTracker != null)
        velocityTracker.computeCurrentVelocity(param1Int, param1Float); 
    }
    
    public float getXVelocity() {
      VelocityTracker velocityTracker = this.tracker;
      return (velocityTracker != null) ? velocityTracker.getXVelocity() : 0.0F;
    }
    
    public float getXVelocity(int param1Int) {
      VelocityTracker velocityTracker = this.tracker;
      return (velocityTracker != null) ? velocityTracker.getXVelocity(param1Int) : 0.0F;
    }
    
    public float getYVelocity() {
      VelocityTracker velocityTracker = this.tracker;
      return (velocityTracker != null) ? velocityTracker.getYVelocity() : 0.0F;
    }
    
    public float getYVelocity(int param1Int) {
      return (this.tracker != null) ? getYVelocity(param1Int) : 0.0F;
    }
    
    public void recycle() {
      VelocityTracker velocityTracker = this.tracker;
      if (velocityTracker != null) {
        velocityTracker.recycle();
        this.tracker = null;
      } 
    }
  }
  
  class StateCache {
    final String KeyEndState = "motion.EndState";
    
    final String KeyProgress = "motion.progress";
    
    final String KeyStartState = "motion.StartState";
    
    final String KeyVelocity = "motion.velocity";
    
    int endState = -1;
    
    float mProgress = Float.NaN;
    
    float mVelocity = Float.NaN;
    
    int startState = -1;
    
    final MotionLayout this$0;
    
    void apply() {
      int i = this.startState;
      if (i != -1 || this.endState != -1) {
        if (i == -1) {
          MotionLayout.this.transitionToState(this.endState);
        } else {
          int j = this.endState;
          if (j == -1) {
            MotionLayout.this.setState(i, -1, -1);
          } else {
            MotionLayout.this.setTransition(i, j);
          } 
        } 
        MotionLayout.this.setState(MotionLayout.TransitionState.SETUP);
      } 
      if (Float.isNaN(this.mVelocity)) {
        if (Float.isNaN(this.mProgress))
          return; 
        MotionLayout.this.setProgress(this.mProgress);
        return;
      } 
      MotionLayout.this.setProgress(this.mProgress, this.mVelocity);
      this.mProgress = Float.NaN;
      this.mVelocity = Float.NaN;
      this.startState = -1;
      this.endState = -1;
    }
    
    public Bundle getTransitionState() {
      Bundle bundle = new Bundle();
      bundle.putFloat("motion.progress", this.mProgress);
      bundle.putFloat("motion.velocity", this.mVelocity);
      bundle.putInt("motion.StartState", this.startState);
      bundle.putInt("motion.EndState", this.endState);
      return bundle;
    }
    
    public void recordState() {
      this.endState = MotionLayout.this.mEndState;
      this.startState = MotionLayout.this.mBeginState;
      this.mVelocity = MotionLayout.this.getVelocity();
      this.mProgress = MotionLayout.this.getProgress();
    }
    
    public void setEndState(int param1Int) {
      this.endState = param1Int;
    }
    
    public void setProgress(float param1Float) {
      this.mProgress = param1Float;
    }
    
    public void setStartState(int param1Int) {
      this.startState = param1Int;
    }
    
    public void setTransitionState(Bundle param1Bundle) {
      this.mProgress = param1Bundle.getFloat("motion.progress");
      this.mVelocity = param1Bundle.getFloat("motion.velocity");
      this.startState = param1Bundle.getInt("motion.StartState");
      this.endState = param1Bundle.getInt("motion.EndState");
    }
    
    public void setVelocity(float param1Float) {
      this.mVelocity = param1Float;
    }
  }
  
  public static interface TransitionListener {
    void onTransitionChange(MotionLayout param1MotionLayout, int param1Int1, int param1Int2, float param1Float);
    
    void onTransitionCompleted(MotionLayout param1MotionLayout, int param1Int);
    
    void onTransitionStarted(MotionLayout param1MotionLayout, int param1Int1, int param1Int2);
    
    void onTransitionTrigger(MotionLayout param1MotionLayout, int param1Int, boolean param1Boolean, float param1Float);
  }
  
  enum TransitionState {
    FINISHED, MOVING, SETUP, UNDEFINED;
    
    private static final TransitionState[] $VALUES;
    
    static {
      MOVING = new TransitionState("MOVING", 2);
      FINISHED = new TransitionState("FINISHED", 3);
      $VALUES = $values();
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\motion\widget\MotionLayout.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */