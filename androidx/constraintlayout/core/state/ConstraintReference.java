package androidx.constraintlayout.core.state;

import androidx.constraintlayout.core.state.helpers.Facade;
import androidx.constraintlayout.core.widgets.ConstraintAnchor;
import androidx.constraintlayout.core.widgets.ConstraintWidget;
import java.util.ArrayList;
import java.util.HashMap;

public class ConstraintReference implements Reference {
  private Object key;
  
  float mAlpha = Float.NaN;
  
  Object mBaselineToBaseline = null;
  
  protected Object mBottomToBottom = null;
  
  protected Object mBottomToTop = null;
  
  private float mCircularAngle;
  
  Object mCircularConstraint = null;
  
  private float mCircularDistance;
  
  private ConstraintWidget mConstraintWidget;
  
  private HashMap<String, Integer> mCustomColors = new HashMap<>();
  
  private HashMap<String, Float> mCustomFloats = new HashMap<>();
  
  protected Object mEndToEnd = null;
  
  protected Object mEndToStart = null;
  
  Facade mFacade = null;
  
  float mHorizontalBias = 0.5F;
  
  int mHorizontalChainStyle = 0;
  
  Dimension mHorizontalDimension = Dimension.Fixed(Dimension.WRAP_DIMENSION);
  
  State.Constraint mLast = null;
  
  Object mLeftToLeft = null;
  
  Object mLeftToRight = null;
  
  int mMarginBottom = 0;
  
  int mMarginBottomGone = 0;
  
  protected int mMarginEnd = 0;
  
  int mMarginEndGone = 0;
  
  int mMarginLeft = 0;
  
  int mMarginLeftGone = 0;
  
  int mMarginRight = 0;
  
  int mMarginRightGone = 0;
  
  protected int mMarginStart = 0;
  
  int mMarginStartGone = 0;
  
  int mMarginTop = 0;
  
  int mMarginTopGone = 0;
  
  float mPivotX = Float.NaN;
  
  float mPivotY = Float.NaN;
  
  Object mRightToLeft = null;
  
  Object mRightToRight = null;
  
  float mRotationX = Float.NaN;
  
  float mRotationY = Float.NaN;
  
  float mRotationZ = Float.NaN;
  
  float mScaleX = Float.NaN;
  
  float mScaleY = Float.NaN;
  
  protected Object mStartToEnd = null;
  
  protected Object mStartToStart = null;
  
  final State mState;
  
  String mTag = null;
  
  protected Object mTopToBottom = null;
  
  protected Object mTopToTop = null;
  
  float mTranslationX = Float.NaN;
  
  float mTranslationY = Float.NaN;
  
  float mTranslationZ = Float.NaN;
  
  float mVerticalBias = 0.5F;
  
  int mVerticalChainStyle = 0;
  
  Dimension mVerticalDimension = Dimension.Fixed(Dimension.WRAP_DIMENSION);
  
  private Object mView;
  
  int mVisibility = 0;
  
  public ConstraintReference(State paramState) {
    this.mState = paramState;
  }
  
  private void applyConnection(ConstraintWidget paramConstraintWidget, Object paramObject, State.Constraint paramConstraint) {
    paramObject = getTarget(paramObject);
    if (paramObject == null)
      return; 
    int i = null.$SwitchMap$androidx$constraintlayout$core$state$State$Constraint[paramConstraint.ordinal()];
    switch (paramConstraint) {
      default:
        return;
      case null:
        paramConstraintWidget.connectCircularConstraint((ConstraintWidget)paramObject, this.mCircularAngle, (int)this.mCircularDistance);
      case null:
        paramConstraintWidget.immediateConnect(ConstraintAnchor.Type.BASELINE, (ConstraintWidget)paramObject, ConstraintAnchor.Type.BASELINE, 0, 0);
      case null:
        paramConstraintWidget.getAnchor(ConstraintAnchor.Type.BOTTOM).connect(paramObject.getAnchor(ConstraintAnchor.Type.BOTTOM), this.mMarginBottom, this.mMarginBottomGone, false);
      case null:
        paramConstraintWidget.getAnchor(ConstraintAnchor.Type.BOTTOM).connect(paramObject.getAnchor(ConstraintAnchor.Type.TOP), this.mMarginBottom, this.mMarginBottomGone, false);
      case null:
        paramConstraintWidget.getAnchor(ConstraintAnchor.Type.TOP).connect(paramObject.getAnchor(ConstraintAnchor.Type.BOTTOM), this.mMarginTop, this.mMarginTopGone, false);
      case null:
        paramConstraintWidget.getAnchor(ConstraintAnchor.Type.TOP).connect(paramObject.getAnchor(ConstraintAnchor.Type.TOP), this.mMarginTop, this.mMarginTopGone, false);
      case null:
        paramConstraintWidget.getAnchor(ConstraintAnchor.Type.RIGHT).connect(paramObject.getAnchor(ConstraintAnchor.Type.RIGHT), this.mMarginEnd, this.mMarginEndGone, false);
      case null:
        paramConstraintWidget.getAnchor(ConstraintAnchor.Type.RIGHT).connect(paramObject.getAnchor(ConstraintAnchor.Type.LEFT), this.mMarginEnd, this.mMarginEndGone, false);
      case null:
        paramConstraintWidget.getAnchor(ConstraintAnchor.Type.LEFT).connect(paramObject.getAnchor(ConstraintAnchor.Type.RIGHT), this.mMarginStart, this.mMarginStartGone, false);
      case null:
        paramConstraintWidget.getAnchor(ConstraintAnchor.Type.LEFT).connect(paramObject.getAnchor(ConstraintAnchor.Type.LEFT), this.mMarginStart, this.mMarginStartGone, false);
      case null:
        paramConstraintWidget.getAnchor(ConstraintAnchor.Type.RIGHT).connect(paramObject.getAnchor(ConstraintAnchor.Type.RIGHT), this.mMarginRight, this.mMarginRightGone, false);
      case null:
        paramConstraintWidget.getAnchor(ConstraintAnchor.Type.RIGHT).connect(paramObject.getAnchor(ConstraintAnchor.Type.LEFT), this.mMarginRight, this.mMarginRightGone, false);
      case null:
        paramConstraintWidget.getAnchor(ConstraintAnchor.Type.LEFT).connect(paramObject.getAnchor(ConstraintAnchor.Type.RIGHT), this.mMarginLeft, this.mMarginLeftGone, false);
      case null:
        break;
    } 
    paramConstraintWidget.getAnchor(ConstraintAnchor.Type.LEFT).connect(paramObject.getAnchor(ConstraintAnchor.Type.LEFT), this.mMarginLeft, this.mMarginLeftGone, false);
  }
  
  private void dereference() {
    this.mLeftToLeft = get(this.mLeftToLeft);
    this.mLeftToRight = get(this.mLeftToRight);
    this.mRightToLeft = get(this.mRightToLeft);
    this.mRightToRight = get(this.mRightToRight);
    this.mStartToStart = get(this.mStartToStart);
    this.mStartToEnd = get(this.mStartToEnd);
    this.mEndToStart = get(this.mEndToStart);
    this.mEndToEnd = get(this.mEndToEnd);
    this.mTopToTop = get(this.mTopToTop);
    this.mTopToBottom = get(this.mTopToBottom);
    this.mBottomToTop = get(this.mBottomToTop);
    this.mBottomToBottom = get(this.mBottomToBottom);
    this.mBaselineToBaseline = get(this.mBaselineToBaseline);
  }
  
  private Object get(Object paramObject) {
    return (paramObject == null) ? null : (!(paramObject instanceof ConstraintReference) ? this.mState.reference(paramObject) : paramObject);
  }
  
  private ConstraintWidget getTarget(Object paramObject) {
    return (paramObject instanceof Reference) ? ((Reference)paramObject).getConstraintWidget() : null;
  }
  
  public void addCustomColor(String paramString, int paramInt) {
    this.mCustomColors.put(paramString, Integer.valueOf(paramInt));
  }
  
  public void addCustomFloat(String paramString, float paramFloat) {
    if (this.mCustomFloats == null)
      this.mCustomFloats = new HashMap<>(); 
    this.mCustomFloats.put(paramString, Float.valueOf(paramFloat));
  }
  
  public ConstraintReference alpha(float paramFloat) {
    this.mAlpha = paramFloat;
    return this;
  }
  
  public void apply() {
    if (this.mConstraintWidget == null)
      return; 
    Facade facade = this.mFacade;
    if (facade != null)
      facade.apply(); 
    this.mHorizontalDimension.apply(this.mState, this.mConstraintWidget, 0);
    this.mVerticalDimension.apply(this.mState, this.mConstraintWidget, 1);
    dereference();
    applyConnection(this.mConstraintWidget, this.mLeftToLeft, State.Constraint.LEFT_TO_LEFT);
    applyConnection(this.mConstraintWidget, this.mLeftToRight, State.Constraint.LEFT_TO_RIGHT);
    applyConnection(this.mConstraintWidget, this.mRightToLeft, State.Constraint.RIGHT_TO_LEFT);
    applyConnection(this.mConstraintWidget, this.mRightToRight, State.Constraint.RIGHT_TO_RIGHT);
    applyConnection(this.mConstraintWidget, this.mStartToStart, State.Constraint.START_TO_START);
    applyConnection(this.mConstraintWidget, this.mStartToEnd, State.Constraint.START_TO_END);
    applyConnection(this.mConstraintWidget, this.mEndToStart, State.Constraint.END_TO_START);
    applyConnection(this.mConstraintWidget, this.mEndToEnd, State.Constraint.END_TO_END);
    applyConnection(this.mConstraintWidget, this.mTopToTop, State.Constraint.TOP_TO_TOP);
    applyConnection(this.mConstraintWidget, this.mTopToBottom, State.Constraint.TOP_TO_BOTTOM);
    applyConnection(this.mConstraintWidget, this.mBottomToTop, State.Constraint.BOTTOM_TO_TOP);
    applyConnection(this.mConstraintWidget, this.mBottomToBottom, State.Constraint.BOTTOM_TO_BOTTOM);
    applyConnection(this.mConstraintWidget, this.mBaselineToBaseline, State.Constraint.BASELINE_TO_BASELINE);
    applyConnection(this.mConstraintWidget, this.mCircularConstraint, State.Constraint.CIRCULAR_CONSTRAINT);
    int i = this.mHorizontalChainStyle;
    if (i != 0)
      this.mConstraintWidget.setHorizontalChainStyle(i); 
    i = this.mVerticalChainStyle;
    if (i != 0)
      this.mConstraintWidget.setVerticalChainStyle(i); 
    this.mConstraintWidget.setHorizontalBiasPercent(this.mHorizontalBias);
    this.mConstraintWidget.setVerticalBiasPercent(this.mVerticalBias);
    this.mConstraintWidget.frame.pivotX = this.mPivotX;
    this.mConstraintWidget.frame.pivotY = this.mPivotY;
    this.mConstraintWidget.frame.rotationX = this.mRotationX;
    this.mConstraintWidget.frame.rotationY = this.mRotationY;
    this.mConstraintWidget.frame.rotationZ = this.mRotationZ;
    this.mConstraintWidget.frame.translationX = this.mTranslationX;
    this.mConstraintWidget.frame.translationY = this.mTranslationY;
    this.mConstraintWidget.frame.translationZ = this.mTranslationZ;
    this.mConstraintWidget.frame.scaleX = this.mScaleX;
    this.mConstraintWidget.frame.scaleY = this.mScaleY;
    this.mConstraintWidget.frame.alpha = this.mAlpha;
    this.mConstraintWidget.frame.visibility = this.mVisibility;
    this.mConstraintWidget.setVisibility(this.mVisibility);
    HashMap<String, Integer> hashMap1 = this.mCustomColors;
    if (hashMap1 != null)
      for (String str : hashMap1.keySet()) {
        Integer integer = this.mCustomColors.get(str);
        this.mConstraintWidget.frame.setCustomAttribute(str, 902, integer.intValue());
      }  
    HashMap<String, Float> hashMap = this.mCustomFloats;
    if (hashMap != null)
      for (String str : hashMap.keySet()) {
        float f = ((Float)this.mCustomFloats.get(str)).floatValue();
        this.mConstraintWidget.frame.setCustomAttribute(str, 901, f);
      }  
  }
  
  public ConstraintReference baseline() {
    this.mLast = State.Constraint.BASELINE_TO_BASELINE;
    return this;
  }
  
  public ConstraintReference baselineToBaseline(Object paramObject) {
    this.mLast = State.Constraint.BASELINE_TO_BASELINE;
    this.mBaselineToBaseline = paramObject;
    return this;
  }
  
  public ConstraintReference bias(float paramFloat) {
    if (this.mLast == null)
      return this; 
    switch (this.mLast) {
      default:
        return this;
      case null:
      case null:
      case null:
      case null:
      case null:
        this.mVerticalBias = paramFloat;
      case null:
      case null:
      case null:
      case null:
      case null:
      case null:
      case null:
      case null:
      case null:
        break;
    } 
    this.mHorizontalBias = paramFloat;
  }
  
  public ConstraintReference bottom() {
    if (this.mBottomToTop != null) {
      this.mLast = State.Constraint.BOTTOM_TO_TOP;
    } else {
      this.mLast = State.Constraint.BOTTOM_TO_BOTTOM;
    } 
    return this;
  }
  
  public ConstraintReference bottomToBottom(Object paramObject) {
    this.mLast = State.Constraint.BOTTOM_TO_BOTTOM;
    this.mBottomToBottom = paramObject;
    return this;
  }
  
  public ConstraintReference bottomToTop(Object paramObject) {
    this.mLast = State.Constraint.BOTTOM_TO_TOP;
    this.mBottomToTop = paramObject;
    return this;
  }
  
  public ConstraintReference centerHorizontally(Object paramObject) {
    paramObject = get(paramObject);
    this.mStartToStart = paramObject;
    this.mEndToEnd = paramObject;
    this.mLast = State.Constraint.CENTER_HORIZONTALLY;
    this.mHorizontalBias = 0.5F;
    return this;
  }
  
  public ConstraintReference centerVertically(Object paramObject) {
    paramObject = get(paramObject);
    this.mTopToTop = paramObject;
    this.mBottomToBottom = paramObject;
    this.mLast = State.Constraint.CENTER_VERTICALLY;
    this.mVerticalBias = 0.5F;
    return this;
  }
  
  public ConstraintReference circularConstraint(Object paramObject, float paramFloat1, float paramFloat2) {
    this.mCircularConstraint = get(paramObject);
    this.mCircularAngle = paramFloat1;
    this.mCircularDistance = paramFloat2;
    this.mLast = State.Constraint.CIRCULAR_CONSTRAINT;
    return this;
  }
  
  public ConstraintReference clear() {
    if (this.mLast != null) {
      switch (this.mLast) {
        default:
          return this;
        case null:
          this.mCircularConstraint = null;
        case null:
          this.mBaselineToBaseline = null;
        case null:
        case null:
          this.mBottomToTop = null;
          this.mBottomToBottom = null;
          this.mMarginBottom = 0;
          this.mMarginBottomGone = 0;
        case null:
        case null:
          this.mTopToTop = null;
          this.mTopToBottom = null;
          this.mMarginTop = 0;
          this.mMarginTopGone = 0;
        case null:
        case null:
          this.mEndToStart = null;
          this.mEndToEnd = null;
          this.mMarginEnd = 0;
          this.mMarginEndGone = 0;
        case null:
        case null:
          this.mStartToStart = null;
          this.mStartToEnd = null;
          this.mMarginStart = 0;
          this.mMarginStartGone = 0;
        case null:
        case null:
          this.mRightToLeft = null;
          this.mRightToRight = null;
          this.mMarginRight = 0;
          this.mMarginRightGone = 0;
        case null:
        case null:
          break;
      } 
      this.mLeftToLeft = null;
      this.mLeftToRight = null;
      this.mMarginLeft = 0;
      this.mMarginLeftGone = 0;
    } 
    this.mLeftToLeft = null;
    this.mLeftToRight = null;
    this.mMarginLeft = 0;
    this.mRightToLeft = null;
    this.mRightToRight = null;
    this.mMarginRight = 0;
    this.mStartToStart = null;
    this.mStartToEnd = null;
    this.mMarginStart = 0;
    this.mEndToStart = null;
    this.mEndToEnd = null;
    this.mMarginEnd = 0;
    this.mTopToTop = null;
    this.mTopToBottom = null;
    this.mMarginTop = 0;
    this.mBottomToTop = null;
    this.mBottomToBottom = null;
    this.mMarginBottom = 0;
    this.mBaselineToBaseline = null;
    this.mCircularConstraint = null;
    this.mHorizontalBias = 0.5F;
    this.mVerticalBias = 0.5F;
    this.mMarginLeftGone = 0;
    this.mMarginRightGone = 0;
    this.mMarginStartGone = 0;
    this.mMarginEndGone = 0;
    this.mMarginTopGone = 0;
    this.mMarginBottomGone = 0;
  }
  
  public ConstraintReference clearHorizontal() {
    start().clear();
    end().clear();
    left().clear();
    right().clear();
    return this;
  }
  
  public ConstraintReference clearVertical() {
    top().clear();
    baseline().clear();
    bottom().clear();
    return this;
  }
  
  public ConstraintWidget createConstraintWidget() {
    return new ConstraintWidget(getWidth().getValue(), getHeight().getValue());
  }
  
  public ConstraintReference end() {
    if (this.mEndToStart != null) {
      this.mLast = State.Constraint.END_TO_START;
    } else {
      this.mLast = State.Constraint.END_TO_END;
    } 
    return this;
  }
  
  public ConstraintReference endToEnd(Object paramObject) {
    this.mLast = State.Constraint.END_TO_END;
    this.mEndToEnd = paramObject;
    return this;
  }
  
  public ConstraintReference endToStart(Object paramObject) {
    this.mLast = State.Constraint.END_TO_START;
    this.mEndToStart = paramObject;
    return this;
  }
  
  public float getAlpha() {
    return this.mAlpha;
  }
  
  public ConstraintWidget getConstraintWidget() {
    if (this.mConstraintWidget == null) {
      ConstraintWidget constraintWidget = createConstraintWidget();
      this.mConstraintWidget = constraintWidget;
      constraintWidget.setCompanionWidget(this.mView);
    } 
    return this.mConstraintWidget;
  }
  
  public Facade getFacade() {
    return this.mFacade;
  }
  
  public Dimension getHeight() {
    return this.mVerticalDimension;
  }
  
  public int getHorizontalChainStyle() {
    return this.mHorizontalChainStyle;
  }
  
  public Object getKey() {
    return this.key;
  }
  
  public float getPivotX() {
    return this.mPivotX;
  }
  
  public float getPivotY() {
    return this.mPivotY;
  }
  
  public float getRotationX() {
    return this.mRotationX;
  }
  
  public float getRotationY() {
    return this.mRotationY;
  }
  
  public float getRotationZ() {
    return this.mRotationZ;
  }
  
  public float getScaleX() {
    return this.mScaleX;
  }
  
  public float getScaleY() {
    return this.mScaleY;
  }
  
  public String getTag() {
    return this.mTag;
  }
  
  public float getTranslationX() {
    return this.mTranslationX;
  }
  
  public float getTranslationY() {
    return this.mTranslationY;
  }
  
  public float getTranslationZ() {
    return this.mTranslationZ;
  }
  
  public int getVerticalChainStyle(int paramInt) {
    return this.mVerticalChainStyle;
  }
  
  public Object getView() {
    return this.mView;
  }
  
  public Dimension getWidth() {
    return this.mHorizontalDimension;
  }
  
  public ConstraintReference height(Dimension paramDimension) {
    return setHeight(paramDimension);
  }
  
  public ConstraintReference horizontalBias(float paramFloat) {
    this.mHorizontalBias = paramFloat;
    return this;
  }
  
  public ConstraintReference left() {
    if (this.mLeftToLeft != null) {
      this.mLast = State.Constraint.LEFT_TO_LEFT;
    } else {
      this.mLast = State.Constraint.LEFT_TO_RIGHT;
    } 
    return this;
  }
  
  public ConstraintReference leftToLeft(Object paramObject) {
    this.mLast = State.Constraint.LEFT_TO_LEFT;
    this.mLeftToLeft = paramObject;
    return this;
  }
  
  public ConstraintReference leftToRight(Object paramObject) {
    this.mLast = State.Constraint.LEFT_TO_RIGHT;
    this.mLeftToRight = paramObject;
    return this;
  }
  
  public ConstraintReference margin(int paramInt) {
    if (this.mLast != null) {
      switch (this.mLast) {
        default:
          return this;
        case null:
          this.mCircularDistance = paramInt;
        case null:
          return this;
        case null:
        case null:
          this.mMarginBottom = paramInt;
        case null:
        case null:
          this.mMarginTop = paramInt;
        case null:
        case null:
          this.mMarginEnd = paramInt;
        case null:
        case null:
          this.mMarginStart = paramInt;
        case null:
        case null:
          this.mMarginRight = paramInt;
        case null:
        case null:
          break;
      } 
      this.mMarginLeft = paramInt;
    } 
    this.mMarginLeft = paramInt;
    this.mMarginRight = paramInt;
    this.mMarginStart = paramInt;
    this.mMarginEnd = paramInt;
    this.mMarginTop = paramInt;
    this.mMarginBottom = paramInt;
  }
  
  public ConstraintReference margin(Object paramObject) {
    return margin(this.mState.convertDimension(paramObject));
  }
  
  public ConstraintReference marginGone(int paramInt) {
    if (this.mLast != null) {
      switch (this.mLast) {
        default:
          return this;
        case null:
          return this;
        case null:
        case null:
          this.mMarginBottomGone = paramInt;
        case null:
        case null:
          this.mMarginTopGone = paramInt;
        case null:
        case null:
          this.mMarginEndGone = paramInt;
        case null:
        case null:
          this.mMarginStartGone = paramInt;
        case null:
        case null:
          this.mMarginRightGone = paramInt;
        case null:
        case null:
          break;
      } 
      this.mMarginLeftGone = paramInt;
    } 
    this.mMarginLeftGone = paramInt;
    this.mMarginRightGone = paramInt;
    this.mMarginStartGone = paramInt;
    this.mMarginEndGone = paramInt;
    this.mMarginTopGone = paramInt;
    this.mMarginBottomGone = paramInt;
  }
  
  public ConstraintReference pivotX(float paramFloat) {
    this.mPivotX = paramFloat;
    return this;
  }
  
  public ConstraintReference pivotY(float paramFloat) {
    this.mPivotY = paramFloat;
    return this;
  }
  
  public ConstraintReference right() {
    if (this.mRightToLeft != null) {
      this.mLast = State.Constraint.RIGHT_TO_LEFT;
    } else {
      this.mLast = State.Constraint.RIGHT_TO_RIGHT;
    } 
    return this;
  }
  
  public ConstraintReference rightToLeft(Object paramObject) {
    this.mLast = State.Constraint.RIGHT_TO_LEFT;
    this.mRightToLeft = paramObject;
    return this;
  }
  
  public ConstraintReference rightToRight(Object paramObject) {
    this.mLast = State.Constraint.RIGHT_TO_RIGHT;
    this.mRightToRight = paramObject;
    return this;
  }
  
  public ConstraintReference rotationX(float paramFloat) {
    this.mRotationX = paramFloat;
    return this;
  }
  
  public ConstraintReference rotationY(float paramFloat) {
    this.mRotationY = paramFloat;
    return this;
  }
  
  public ConstraintReference rotationZ(float paramFloat) {
    this.mRotationZ = paramFloat;
    return this;
  }
  
  public ConstraintReference scaleX(float paramFloat) {
    this.mScaleX = paramFloat;
    return this;
  }
  
  public ConstraintReference scaleY(float paramFloat) {
    this.mScaleY = paramFloat;
    return this;
  }
  
  public void setConstraintWidget(ConstraintWidget paramConstraintWidget) {
    if (paramConstraintWidget == null)
      return; 
    this.mConstraintWidget = paramConstraintWidget;
    paramConstraintWidget.setCompanionWidget(this.mView);
  }
  
  public void setFacade(Facade paramFacade) {
    this.mFacade = paramFacade;
    if (paramFacade != null)
      setConstraintWidget(paramFacade.getConstraintWidget()); 
  }
  
  public ConstraintReference setHeight(Dimension paramDimension) {
    this.mVerticalDimension = paramDimension;
    return this;
  }
  
  public void setHorizontalChainStyle(int paramInt) {
    this.mHorizontalChainStyle = paramInt;
  }
  
  public void setKey(Object paramObject) {
    this.key = paramObject;
  }
  
  public void setTag(String paramString) {
    this.mTag = paramString;
  }
  
  public void setVerticalChainStyle(int paramInt) {
    this.mVerticalChainStyle = paramInt;
  }
  
  public void setView(Object paramObject) {
    this.mView = paramObject;
    ConstraintWidget constraintWidget = this.mConstraintWidget;
    if (constraintWidget != null)
      constraintWidget.setCompanionWidget(paramObject); 
  }
  
  public ConstraintReference setWidth(Dimension paramDimension) {
    this.mHorizontalDimension = paramDimension;
    return this;
  }
  
  public ConstraintReference start() {
    if (this.mStartToStart != null) {
      this.mLast = State.Constraint.START_TO_START;
    } else {
      this.mLast = State.Constraint.START_TO_END;
    } 
    return this;
  }
  
  public ConstraintReference startToEnd(Object paramObject) {
    this.mLast = State.Constraint.START_TO_END;
    this.mStartToEnd = paramObject;
    return this;
  }
  
  public ConstraintReference startToStart(Object paramObject) {
    this.mLast = State.Constraint.START_TO_START;
    this.mStartToStart = paramObject;
    return this;
  }
  
  public ConstraintReference top() {
    if (this.mTopToTop != null) {
      this.mLast = State.Constraint.TOP_TO_TOP;
    } else {
      this.mLast = State.Constraint.TOP_TO_BOTTOM;
    } 
    return this;
  }
  
  public ConstraintReference topToBottom(Object paramObject) {
    this.mLast = State.Constraint.TOP_TO_BOTTOM;
    this.mTopToBottom = paramObject;
    return this;
  }
  
  public ConstraintReference topToTop(Object paramObject) {
    this.mLast = State.Constraint.TOP_TO_TOP;
    this.mTopToTop = paramObject;
    return this;
  }
  
  public ConstraintReference translationX(float paramFloat) {
    this.mTranslationX = paramFloat;
    return this;
  }
  
  public ConstraintReference translationY(float paramFloat) {
    this.mTranslationY = paramFloat;
    return this;
  }
  
  public ConstraintReference translationZ(float paramFloat) {
    this.mTranslationZ = paramFloat;
    return this;
  }
  
  public void validate() throws IncorrectConstraintException {
    ArrayList<String> arrayList = new ArrayList();
    if (this.mLeftToLeft != null && this.mLeftToRight != null)
      arrayList.add("LeftToLeft and LeftToRight both defined"); 
    if (this.mRightToLeft != null && this.mRightToRight != null)
      arrayList.add("RightToLeft and RightToRight both defined"); 
    if (this.mStartToStart != null && this.mStartToEnd != null)
      arrayList.add("StartToStart and StartToEnd both defined"); 
    if (this.mEndToStart != null && this.mEndToEnd != null)
      arrayList.add("EndToStart and EndToEnd both defined"); 
    if ((this.mLeftToLeft != null || this.mLeftToRight != null || this.mRightToLeft != null || this.mRightToRight != null) && (this.mStartToStart != null || this.mStartToEnd != null || this.mEndToStart != null || this.mEndToEnd != null))
      arrayList.add("Both left/right and start/end constraints defined"); 
    if (arrayList.size() <= 0)
      return; 
    throw new IncorrectConstraintException(arrayList);
  }
  
  public ConstraintReference verticalBias(float paramFloat) {
    this.mVerticalBias = paramFloat;
    return this;
  }
  
  public ConstraintReference visibility(int paramInt) {
    this.mVisibility = paramInt;
    return this;
  }
  
  public ConstraintReference width(Dimension paramDimension) {
    return setWidth(paramDimension);
  }
  
  public static interface ConstraintReferenceFactory {
    ConstraintReference create(State param1State);
  }
  
  static class IncorrectConstraintException extends Exception {
    private final ArrayList<String> mErrors;
    
    public IncorrectConstraintException(ArrayList<String> param1ArrayList) {
      this.mErrors = param1ArrayList;
    }
    
    public ArrayList<String> getErrors() {
      return this.mErrors;
    }
    
    public String toString() {
      return "IncorrectConstraintException: " + this.mErrors.toString();
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\state\ConstraintReference.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */