package androidx.constraintlayout.motion.widget;

public class OnSwipe {
  public static final int COMPLETE_MODE_CONTINUOUS_VELOCITY = 0;
  
  public static final int COMPLETE_MODE_SPRING = 1;
  
  public static final int DRAG_ANTICLOCKWISE = 7;
  
  public static final int DRAG_CLOCKWISE = 6;
  
  public static final int DRAG_DOWN = 1;
  
  public static final int DRAG_END = 5;
  
  public static final int DRAG_LEFT = 2;
  
  public static final int DRAG_RIGHT = 3;
  
  public static final int DRAG_START = 4;
  
  public static final int DRAG_UP = 0;
  
  public static final int FLAG_DISABLE_POST_SCROLL = 1;
  
  public static final int FLAG_DISABLE_SCROLL = 2;
  
  public static final int ON_UP_AUTOCOMPLETE = 0;
  
  public static final int ON_UP_AUTOCOMPLETE_TO_END = 2;
  
  public static final int ON_UP_AUTOCOMPLETE_TO_START = 1;
  
  public static final int ON_UP_DECELERATE = 4;
  
  public static final int ON_UP_DECELERATE_AND_COMPLETE = 5;
  
  public static final int ON_UP_NEVER_TO_END = 7;
  
  public static final int ON_UP_NEVER_TO_START = 6;
  
  public static final int ON_UP_STOP = 3;
  
  public static final int SIDE_BOTTOM = 3;
  
  public static final int SIDE_END = 6;
  
  public static final int SIDE_LEFT = 1;
  
  public static final int SIDE_MIDDLE = 4;
  
  public static final int SIDE_RIGHT = 2;
  
  public static final int SIDE_START = 5;
  
  public static final int SIDE_TOP = 0;
  
  public static final int SPRING_BOUNDARY_BOUNCEBOTH = 3;
  
  public static final int SPRING_BOUNDARY_BOUNCEEND = 2;
  
  public static final int SPRING_BOUNDARY_BOUNCESTART = 1;
  
  public static final int SPRING_BOUNDARY_OVERSHOOT = 0;
  
  private int mAutoCompleteMode = 0;
  
  private int mDragDirection = 0;
  
  private float mDragScale = 1.0F;
  
  private float mDragThreshold = 10.0F;
  
  private int mFlags = 0;
  
  private int mLimitBoundsTo = -1;
  
  private float mMaxAcceleration = 1.2F;
  
  private float mMaxVelocity = 4.0F;
  
  private boolean mMoveWhenScrollAtTop = true;
  
  private int mOnTouchUp = 0;
  
  private int mRotationCenterId = -1;
  
  private int mSpringBoundary = 0;
  
  private float mSpringDamping = Float.NaN;
  
  private float mSpringMass = 1.0F;
  
  private float mSpringStiffness = Float.NaN;
  
  private float mSpringStopThreshold = Float.NaN;
  
  private int mTouchAnchorId = -1;
  
  private int mTouchAnchorSide = 0;
  
  private int mTouchRegionId = -1;
  
  public int getAutoCompleteMode() {
    return this.mAutoCompleteMode;
  }
  
  public int getDragDirection() {
    return this.mDragDirection;
  }
  
  public float getDragScale() {
    return this.mDragScale;
  }
  
  public float getDragThreshold() {
    return this.mDragThreshold;
  }
  
  public int getLimitBoundsTo() {
    return this.mLimitBoundsTo;
  }
  
  public float getMaxAcceleration() {
    return this.mMaxAcceleration;
  }
  
  public float getMaxVelocity() {
    return this.mMaxVelocity;
  }
  
  public boolean getMoveWhenScrollAtTop() {
    return this.mMoveWhenScrollAtTop;
  }
  
  public int getNestedScrollFlags() {
    return this.mFlags;
  }
  
  public int getOnTouchUp() {
    return this.mOnTouchUp;
  }
  
  public int getRotationCenterId() {
    return this.mRotationCenterId;
  }
  
  public int getSpringBoundary() {
    return this.mSpringBoundary;
  }
  
  public float getSpringDamping() {
    return this.mSpringDamping;
  }
  
  public float getSpringMass() {
    return this.mSpringMass;
  }
  
  public float getSpringStiffness() {
    return this.mSpringStiffness;
  }
  
  public float getSpringStopThreshold() {
    return this.mSpringStopThreshold;
  }
  
  public int getTouchAnchorId() {
    return this.mTouchAnchorId;
  }
  
  public int getTouchAnchorSide() {
    return this.mTouchAnchorSide;
  }
  
  public int getTouchRegionId() {
    return this.mTouchRegionId;
  }
  
  public void setAutoCompleteMode(int paramInt) {
    this.mAutoCompleteMode = paramInt;
  }
  
  public OnSwipe setDragDirection(int paramInt) {
    this.mDragDirection = paramInt;
    return this;
  }
  
  public OnSwipe setDragScale(int paramInt) {
    this.mDragScale = paramInt;
    return this;
  }
  
  public OnSwipe setDragThreshold(int paramInt) {
    this.mDragThreshold = paramInt;
    return this;
  }
  
  public OnSwipe setLimitBoundsTo(int paramInt) {
    this.mLimitBoundsTo = paramInt;
    return this;
  }
  
  public OnSwipe setMaxAcceleration(int paramInt) {
    this.mMaxAcceleration = paramInt;
    return this;
  }
  
  public OnSwipe setMaxVelocity(int paramInt) {
    this.mMaxVelocity = paramInt;
    return this;
  }
  
  public OnSwipe setMoveWhenScrollAtTop(boolean paramBoolean) {
    this.mMoveWhenScrollAtTop = paramBoolean;
    return this;
  }
  
  public OnSwipe setNestedScrollFlags(int paramInt) {
    this.mFlags = paramInt;
    return this;
  }
  
  public OnSwipe setOnTouchUp(int paramInt) {
    this.mOnTouchUp = paramInt;
    return this;
  }
  
  public OnSwipe setRotateCenter(int paramInt) {
    this.mRotationCenterId = paramInt;
    return this;
  }
  
  public OnSwipe setSpringBoundary(int paramInt) {
    this.mSpringBoundary = paramInt;
    return this;
  }
  
  public OnSwipe setSpringDamping(float paramFloat) {
    this.mSpringDamping = paramFloat;
    return this;
  }
  
  public OnSwipe setSpringMass(float paramFloat) {
    this.mSpringMass = paramFloat;
    return this;
  }
  
  public OnSwipe setSpringStiffness(float paramFloat) {
    this.mSpringStiffness = paramFloat;
    return this;
  }
  
  public OnSwipe setSpringStopThreshold(float paramFloat) {
    this.mSpringStopThreshold = paramFloat;
    return this;
  }
  
  public OnSwipe setTouchAnchorId(int paramInt) {
    this.mTouchAnchorId = paramInt;
    return this;
  }
  
  public OnSwipe setTouchAnchorSide(int paramInt) {
    this.mTouchAnchorSide = paramInt;
    return this;
  }
  
  public OnSwipe setTouchRegionId(int paramInt) {
    this.mTouchRegionId = paramInt;
    return this;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\motion\widget\OnSwipe.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */