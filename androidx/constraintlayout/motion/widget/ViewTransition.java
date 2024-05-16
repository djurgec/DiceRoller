package androidx.constraintlayout.motion.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.Log;
import android.util.TypedValue;
import android.util.Xml;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import androidx.constraintlayout.core.motion.utils.Easing;
import androidx.constraintlayout.core.motion.utils.KeyCache;
import androidx.constraintlayout.widget.ConstraintAttribute;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.R;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class ViewTransition {
  static final int ANTICIPATE = 6;
  
  static final int BOUNCE = 4;
  
  public static final String CONSTRAINT_OVERRIDE = "ConstraintOverride";
  
  public static final String CUSTOM_ATTRIBUTE = "CustomAttribute";
  
  public static final String CUSTOM_METHOD = "CustomMethod";
  
  static final int EASE_IN = 1;
  
  static final int EASE_IN_OUT = 0;
  
  static final int EASE_OUT = 2;
  
  private static final int INTERPOLATOR_REFERENCE_ID = -2;
  
  public static final String KEY_FRAME_SET_TAG = "KeyFrameSet";
  
  static final int LINEAR = 3;
  
  public static final int ONSTATE_ACTION_DOWN = 1;
  
  public static final int ONSTATE_ACTION_DOWN_UP = 3;
  
  public static final int ONSTATE_ACTION_UP = 2;
  
  public static final int ONSTATE_SHARED_VALUE_SET = 4;
  
  public static final int ONSTATE_SHARED_VALUE_UNSET = 5;
  
  static final int OVERSHOOT = 5;
  
  private static final int SPLINE_STRING = -1;
  
  private static String TAG = "ViewTransition";
  
  private static final int UNSET = -1;
  
  static final int VIEWTRANSITIONMODE_ALLSTATES = 1;
  
  static final int VIEWTRANSITIONMODE_CURRENTSTATE = 0;
  
  static final int VIEWTRANSITIONMODE_NOSTATE = 2;
  
  public static final String VIEW_TRANSITION_TAG = "ViewTransition";
  
  private int mClearsTag = -1;
  
  ConstraintSet.Constraint mConstraintDelta;
  
  Context mContext;
  
  private int mDefaultInterpolator = 0;
  
  private int mDefaultInterpolatorID = -1;
  
  private String mDefaultInterpolatorString = null;
  
  private boolean mDisabled = false;
  
  private int mDuration = -1;
  
  private int mId;
  
  private int mIfTagNotSet = -1;
  
  private int mIfTagSet = -1;
  
  KeyFrames mKeyFrames;
  
  private int mOnStateTransition = -1;
  
  private int mPathMotionArc = 0;
  
  private int mSetsTag = -1;
  
  private int mSharedValueCurrent = -1;
  
  private int mSharedValueID = -1;
  
  private int mSharedValueTarget = -1;
  
  private int mTargetId;
  
  private String mTargetString;
  
  private int mUpDuration = -1;
  
  int mViewTransitionMode;
  
  ConstraintSet set;
  
  ViewTransition(Context paramContext, XmlPullParser paramXmlPullParser) {
    this.mContext = paramContext;
    try {
      int i = paramXmlPullParser.getEventType();
      while (true) {
        int j = 1;
        if (i != 1) {
          KeyFrames keyFrames;
          String str1;
          String str2;
          StringBuilder stringBuilder1;
          String str3;
          StringBuilder stringBuilder2;
          switch (i) {
            case 3:
              if ("ViewTransition".equals(paramXmlPullParser.getName()))
                return; 
              break;
            case 2:
              str2 = paramXmlPullParser.getName();
              switch (str2.hashCode()) {
                default:
                  i = -1;
                  break;
                case 1791837707:
                  if (str2.equals("CustomAttribute")) {
                    i = 3;
                    break;
                  } 
                case 366511058:
                  if (str2.equals("CustomMethod")) {
                    i = 4;
                    break;
                  } 
                case 61998586:
                  if (str2.equals("ViewTransition")) {
                    i = 0;
                    break;
                  } 
                case -1239391468:
                  if (str2.equals("KeyFrameSet")) {
                    i = j;
                    break;
                  } 
                case -1962203927:
                  if (str2.equals("ConstraintOverride")) {
                    i = 2;
                    break;
                  } 
              } 
              switch (i) {
                default:
                  str3 = TAG;
                  break;
                case 3:
                case 4:
                  ConstraintAttribute.parse(paramContext, paramXmlPullParser, this.mConstraintDelta.mCustomConstraints);
                  break;
                case 2:
                  this.mConstraintDelta = ConstraintSet.buildDelta(paramContext, paramXmlPullParser);
                  break;
                case 1:
                  keyFrames = new KeyFrames();
                  this(paramContext, paramXmlPullParser);
                  this.mKeyFrames = keyFrames;
                  break;
                case 0:
                  parseViewTransitionTags(paramContext, paramXmlPullParser);
                  break;
              } 
              str1 = Debug.getLoc();
              i = String.valueOf(str1).length();
              j = String.valueOf(str2).length();
              stringBuilder2 = new StringBuilder();
              this(i + 13 + j);
              Log.e(str3, stringBuilder2.append(str1).append(" unknown tag ").append(str2).toString());
              str1 = TAG;
              i = paramXmlPullParser.getLineNumber();
              stringBuilder1 = new StringBuilder();
              this(16);
              Log.e(str1, stringBuilder1.append(".xml:").append(i).toString());
              break;
          } 
          i = paramXmlPullParser.next();
          continue;
        } 
        break;
      } 
    } catch (XmlPullParserException xmlPullParserException) {
      xmlPullParserException.printStackTrace();
    } catch (IOException iOException) {
      iOException.printStackTrace();
    } 
  }
  
  private void parseViewTransitionTags(Context paramContext, XmlPullParser paramXmlPullParser) {
    TypedArray typedArray = paramContext.obtainStyledAttributes(Xml.asAttributeSet(paramXmlPullParser), R.styleable.ViewTransition);
    int i = typedArray.getIndexCount();
    for (byte b = 0; b < i; b++) {
      int j = typedArray.getIndex(b);
      if (j == R.styleable.ViewTransition_android_id) {
        this.mId = typedArray.getResourceId(j, this.mId);
      } else if (j == R.styleable.ViewTransition_motionTarget) {
        if (MotionLayout.IS_IN_EDIT_MODE) {
          int k = typedArray.getResourceId(j, this.mTargetId);
          this.mTargetId = k;
          if (k == -1)
            this.mTargetString = typedArray.getString(j); 
        } else if ((typedArray.peekValue(j)).type == 3) {
          this.mTargetString = typedArray.getString(j);
        } else {
          this.mTargetId = typedArray.getResourceId(j, this.mTargetId);
        } 
      } else if (j == R.styleable.ViewTransition_onStateTransition) {
        this.mOnStateTransition = typedArray.getInt(j, this.mOnStateTransition);
      } else if (j == R.styleable.ViewTransition_transitionDisable) {
        this.mDisabled = typedArray.getBoolean(j, this.mDisabled);
      } else if (j == R.styleable.ViewTransition_pathMotionArc) {
        this.mPathMotionArc = typedArray.getInt(j, this.mPathMotionArc);
      } else if (j == R.styleable.ViewTransition_duration) {
        this.mDuration = typedArray.getInt(j, this.mDuration);
      } else if (j == R.styleable.ViewTransition_upDuration) {
        this.mUpDuration = typedArray.getInt(j, this.mUpDuration);
      } else if (j == R.styleable.ViewTransition_viewTransitionMode) {
        this.mViewTransitionMode = typedArray.getInt(j, this.mViewTransitionMode);
      } else if (j == R.styleable.ViewTransition_motionInterpolator) {
        TypedValue typedValue = typedArray.peekValue(j);
        if (typedValue.type == 1) {
          j = typedArray.getResourceId(j, -1);
          this.mDefaultInterpolatorID = j;
          if (j != -1)
            this.mDefaultInterpolator = -2; 
        } else if (typedValue.type == 3) {
          String str = typedArray.getString(j);
          this.mDefaultInterpolatorString = str;
          if (str != null && str.indexOf("/") > 0) {
            this.mDefaultInterpolatorID = typedArray.getResourceId(j, -1);
            this.mDefaultInterpolator = -2;
          } else {
            this.mDefaultInterpolator = -1;
          } 
        } else {
          this.mDefaultInterpolator = typedArray.getInteger(j, this.mDefaultInterpolator);
        } 
      } else if (j == R.styleable.ViewTransition_setsTag) {
        this.mSetsTag = typedArray.getResourceId(j, this.mSetsTag);
      } else if (j == R.styleable.ViewTransition_clearsTag) {
        this.mClearsTag = typedArray.getResourceId(j, this.mClearsTag);
      } else if (j == R.styleable.ViewTransition_ifTagSet) {
        this.mIfTagSet = typedArray.getResourceId(j, this.mIfTagSet);
      } else if (j == R.styleable.ViewTransition_ifTagNotSet) {
        this.mIfTagNotSet = typedArray.getResourceId(j, this.mIfTagNotSet);
      } else if (j == R.styleable.ViewTransition_SharedValueId) {
        this.mSharedValueID = typedArray.getResourceId(j, this.mSharedValueID);
      } else if (j == R.styleable.ViewTransition_SharedValue) {
        this.mSharedValueTarget = typedArray.getInteger(j, this.mSharedValueTarget);
      } 
    } 
    typedArray.recycle();
  }
  
  private void updateTransition(MotionScene.Transition paramTransition, View paramView) {
    int i = this.mDuration;
    if (i != -1)
      paramTransition.setDuration(i); 
    paramTransition.setPathMotionArc(this.mPathMotionArc);
    paramTransition.setInterpolatorInfo(this.mDefaultInterpolator, this.mDefaultInterpolatorString, this.mDefaultInterpolatorID);
    i = paramView.getId();
    KeyFrames keyFrames = this.mKeyFrames;
    if (keyFrames != null) {
      ArrayList<Key> arrayList = keyFrames.getKeyFramesForView(-1);
      keyFrames = new KeyFrames();
      Iterator<Key> iterator = arrayList.iterator();
      while (iterator.hasNext())
        keyFrames.addKey(((Key)iterator.next()).clone().setViewId(i)); 
      paramTransition.addKeyFrame(keyFrames);
    } 
  }
  
  void applyIndependentTransition(ViewTransitionController paramViewTransitionController, MotionLayout paramMotionLayout, View paramView) {
    MotionController motionController = new MotionController(paramView);
    motionController.setBothStates(paramView);
    this.mKeyFrames.addAllFrames(motionController);
    motionController.setup(paramMotionLayout.getWidth(), paramMotionLayout.getHeight(), this.mDuration, System.nanoTime());
    new Animate(paramViewTransitionController, motionController, this.mDuration, this.mUpDuration, this.mOnStateTransition, getInterpolator(paramMotionLayout.getContext()), this.mSetsTag, this.mClearsTag);
  }
  
  void applyTransition(ViewTransitionController paramViewTransitionController, MotionLayout paramMotionLayout, int paramInt, ConstraintSet paramConstraintSet, View... paramVarArgs) {
    if (this.mDisabled)
      return; 
    int i = this.mViewTransitionMode;
    if (i == 2) {
      applyIndependentTransition(paramViewTransitionController, paramMotionLayout, paramVarArgs[0]);
      return;
    } 
    if (i == 1) {
      int[] arrayOfInt = paramMotionLayout.getConstraintSetIds();
      for (i = 0; i < arrayOfInt.length; i++) {
        int k = arrayOfInt[i];
        if (k != paramInt) {
          ConstraintSet constraintSet1 = paramMotionLayout.getConstraintSet(k);
          int m = paramVarArgs.length;
          for (k = 0; k < m; k++) {
            ConstraintSet.Constraint constraint2 = constraintSet1.getConstraint(paramVarArgs[k].getId());
            ConstraintSet.Constraint constraint1 = this.mConstraintDelta;
            if (constraint1 != null) {
              constraint1.applyDelta(constraint2);
              constraint2.mCustomConstraints.putAll(this.mConstraintDelta.mCustomConstraints);
            } 
          } 
        } 
      } 
    } 
    ConstraintSet constraintSet = new ConstraintSet();
    constraintSet.clone(paramConstraintSet);
    int j = paramVarArgs.length;
    for (i = 0; i < j; i++) {
      ConstraintSet.Constraint constraint1 = constraintSet.getConstraint(paramVarArgs[i].getId());
      ConstraintSet.Constraint constraint2 = this.mConstraintDelta;
      if (constraint2 != null) {
        constraint2.applyDelta(constraint1);
        constraint1.mCustomConstraints.putAll(this.mConstraintDelta.mCustomConstraints);
      } 
    } 
    paramMotionLayout.updateState(paramInt, constraintSet);
    paramMotionLayout.updateState(R.id.view_transition, paramConstraintSet);
    paramMotionLayout.setState(R.id.view_transition, -1, -1);
    MotionScene.Transition transition = new MotionScene.Transition(-1, paramMotionLayout.mScene, R.id.view_transition, paramInt);
    i = paramVarArgs.length;
    for (paramInt = 0; paramInt < i; paramInt++)
      updateTransition(transition, paramVarArgs[paramInt]); 
    paramMotionLayout.setTransition(transition);
    paramMotionLayout.transitionToEnd(new ViewTransition$$ExternalSyntheticLambda0(this, paramVarArgs));
  }
  
  boolean checkTags(View paramView) {
    int i = this.mIfTagSet;
    boolean bool2 = false;
    if (i == -1 || paramView.getTag(i) != null) {
      i = 1;
    } else {
      i = 0;
    } 
    int j = this.mIfTagNotSet;
    if (j == -1 || paramView.getTag(j) == null) {
      j = 1;
    } else {
      j = 0;
    } 
    boolean bool1 = bool2;
    if (i != 0) {
      bool1 = bool2;
      if (j != 0)
        bool1 = true; 
    } 
    return bool1;
  }
  
  int getId() {
    return this.mId;
  }
  
  Interpolator getInterpolator(Context paramContext) {
    switch (this.mDefaultInterpolator) {
      default:
        return null;
      case 6:
        return (Interpolator)new AnticipateInterpolator();
      case 5:
        return (Interpolator)new OvershootInterpolator();
      case 4:
        return (Interpolator)new BounceInterpolator();
      case 3:
        return null;
      case 2:
        return (Interpolator)new DecelerateInterpolator();
      case 1:
        return (Interpolator)new AccelerateInterpolator();
      case 0:
        return (Interpolator)new AccelerateDecelerateInterpolator();
      case -1:
        return new Interpolator(this) {
            final Easing val$easing;
            
            public float getInterpolation(float param1Float) {
              return (float)easing.get(param1Float);
            }
          };
      case -2:
        break;
    } 
    return AnimationUtils.loadInterpolator(paramContext, this.mDefaultInterpolatorID);
  }
  
  public int getSharedValue() {
    return this.mSharedValueTarget;
  }
  
  public int getSharedValueCurrent() {
    return this.mSharedValueCurrent;
  }
  
  public int getSharedValueID() {
    return this.mSharedValueID;
  }
  
  public int getStateTransition() {
    return this.mOnStateTransition;
  }
  
  boolean isEnabled() {
    return this.mDisabled ^ true;
  }
  
  boolean matchesView(View paramView) {
    if (paramView == null)
      return false; 
    if (this.mTargetId == -1 && this.mTargetString == null)
      return false; 
    if (!checkTags(paramView))
      return false; 
    if (paramView.getId() == this.mTargetId)
      return true; 
    if (this.mTargetString == null)
      return false; 
    if (paramView.getLayoutParams() instanceof ConstraintLayout.LayoutParams) {
      String str = ((ConstraintLayout.LayoutParams)paramView.getLayoutParams()).constraintTag;
      if (str != null && str.matches(this.mTargetString))
        return true; 
    } 
    return false;
  }
  
  void setEnabled(boolean paramBoolean) {
    this.mDisabled = paramBoolean ^ true;
  }
  
  void setId(int paramInt) {
    this.mId = paramInt;
  }
  
  public void setSharedValue(int paramInt) {
    this.mSharedValueTarget = paramInt;
  }
  
  public void setSharedValueCurrent(int paramInt) {
    this.mSharedValueCurrent = paramInt;
  }
  
  public void setSharedValueID(int paramInt) {
    this.mSharedValueID = paramInt;
  }
  
  public void setStateTransition(int paramInt) {
    this.mOnStateTransition = paramInt;
  }
  
  boolean supports(int paramInt) {
    int i = this.mOnStateTransition;
    boolean bool1 = false;
    boolean bool2 = false;
    boolean bool3 = false;
    if (i == 1) {
      bool1 = bool3;
      if (paramInt == 0)
        bool1 = true; 
      return bool1;
    } 
    if (i == 2) {
      if (paramInt == 1)
        bool1 = true; 
      return bool1;
    } 
    if (i == 3) {
      bool1 = bool2;
      if (paramInt == 0)
        bool1 = true; 
      return bool1;
    } 
    return false;
  }
  
  public String toString() {
    String str = Debug.getName(this.mContext, this.mId);
    return (new StringBuilder(String.valueOf(str).length() + 16)).append("ViewTransition(").append(str).append(")").toString();
  }
  
  static class Animate {
    boolean hold_at_100;
    
    KeyCache mCache;
    
    private final int mClearsTag;
    
    float mDpositionDt;
    
    int mDuration;
    
    Interpolator mInterpolator;
    
    long mLastRender;
    
    MotionController mMC;
    
    float mPosition;
    
    private final int mSetsTag;
    
    long mStart;
    
    Rect mTempRec;
    
    int mUpDuration;
    
    ViewTransitionController mVtController;
    
    boolean reverse;
    
    Animate(ViewTransitionController param1ViewTransitionController, MotionController param1MotionController, int param1Int1, int param1Int2, int param1Int3, Interpolator param1Interpolator, int param1Int4, int param1Int5) {
      float f;
      this.mCache = new KeyCache();
      this.reverse = false;
      this.mTempRec = new Rect();
      this.hold_at_100 = false;
      this.mVtController = param1ViewTransitionController;
      this.mMC = param1MotionController;
      this.mDuration = param1Int1;
      this.mUpDuration = param1Int2;
      long l = System.nanoTime();
      this.mStart = l;
      this.mLastRender = l;
      this.mVtController.addAnimation(this);
      this.mInterpolator = param1Interpolator;
      this.mSetsTag = param1Int4;
      this.mClearsTag = param1Int5;
      if (param1Int3 == 3)
        this.hold_at_100 = true; 
      if (param1Int1 == 0) {
        f = Float.MAX_VALUE;
      } else {
        f = 1.0F / param1Int1;
      } 
      this.mDpositionDt = f;
      mutate();
    }
    
    void mutate() {
      if (this.reverse) {
        mutateReverse();
      } else {
        mutateForward();
      } 
    }
    
    void mutateForward() {
      long l2 = System.nanoTime();
      long l1 = this.mLastRender;
      this.mLastRender = l2;
      float f = this.mPosition + (float)((l2 - l1) * 1.0E-6D) * this.mDpositionDt;
      this.mPosition = f;
      if (f >= 1.0F)
        this.mPosition = 1.0F; 
      Interpolator interpolator = this.mInterpolator;
      if (interpolator == null) {
        f = this.mPosition;
      } else {
        f = interpolator.getInterpolation(this.mPosition);
      } 
      MotionController motionController = this.mMC;
      boolean bool = motionController.interpolate(motionController.mView, f, l2, this.mCache);
      if (this.mPosition >= 1.0F) {
        if (this.mSetsTag != -1)
          this.mMC.getView().setTag(this.mSetsTag, Long.valueOf(System.nanoTime())); 
        if (this.mClearsTag != -1)
          this.mMC.getView().setTag(this.mClearsTag, null); 
        if (!this.hold_at_100)
          this.mVtController.removeAnimation(this); 
      } 
      if (this.mPosition < 1.0F || bool)
        this.mVtController.invalidate(); 
    }
    
    void mutateReverse() {
      long l1 = System.nanoTime();
      long l2 = this.mLastRender;
      this.mLastRender = l1;
      float f = this.mPosition - (float)((l1 - l2) * 1.0E-6D) * this.mDpositionDt;
      this.mPosition = f;
      if (f < 0.0F)
        this.mPosition = 0.0F; 
      Interpolator interpolator = this.mInterpolator;
      if (interpolator == null) {
        f = this.mPosition;
      } else {
        f = interpolator.getInterpolation(this.mPosition);
      } 
      MotionController motionController = this.mMC;
      boolean bool = motionController.interpolate(motionController.mView, f, l1, this.mCache);
      if (this.mPosition <= 0.0F) {
        if (this.mSetsTag != -1)
          this.mMC.getView().setTag(this.mSetsTag, Long.valueOf(System.nanoTime())); 
        if (this.mClearsTag != -1)
          this.mMC.getView().setTag(this.mClearsTag, null); 
        this.mVtController.removeAnimation(this);
      } 
      if (this.mPosition > 0.0F || bool)
        this.mVtController.invalidate(); 
    }
    
    public void reactTo(int param1Int, float param1Float1, float param1Float2) {
      switch (param1Int) {
        default:
          return;
        case 2:
          this.mMC.getView().getHitRect(this.mTempRec);
          if (!this.mTempRec.contains((int)param1Float1, (int)param1Float2) && !this.reverse)
            reverse(true); 
        case 1:
          break;
      } 
      if (!this.reverse)
        reverse(true); 
    }
    
    void reverse(boolean param1Boolean) {
      this.reverse = param1Boolean;
      if (param1Boolean) {
        int i = this.mUpDuration;
        if (i != -1) {
          float f;
          if (i == 0) {
            f = Float.MAX_VALUE;
          } else {
            f = 1.0F / i;
          } 
          this.mDpositionDt = f;
        } 
      } 
      this.mVtController.invalidate();
      this.mLastRender = System.nanoTime();
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\motion\widget\ViewTransition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */