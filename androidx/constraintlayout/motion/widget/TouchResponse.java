package androidx.constraintlayout.motion.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import androidx.constraintlayout.widget.R;
import androidx.core.widget.NestedScrollView;
import org.xmlpull.v1.XmlPullParser;

class TouchResponse {
  public static final int COMPLETE_MODE_CONTINUOUS_VELOCITY = 0;
  
  public static final int COMPLETE_MODE_SPRING = 1;
  
  private static final boolean DEBUG = false;
  
  private static final float EPSILON = 1.0E-7F;
  
  static final int FLAG_DISABLE_POST_SCROLL = 1;
  
  static final int FLAG_DISABLE_SCROLL = 2;
  
  static final int FLAG_SUPPORT_SCROLL_UP = 4;
  
  private static final int SEC_TO_MILLISECONDS = 1000;
  
  private static final int SIDE_BOTTOM = 3;
  
  private static final int SIDE_END = 6;
  
  private static final int SIDE_LEFT = 1;
  
  private static final int SIDE_MIDDLE = 4;
  
  private static final int SIDE_RIGHT = 2;
  
  private static final int SIDE_START = 5;
  
  private static final int SIDE_TOP = 0;
  
  private static final String TAG = "TouchResponse";
  
  private static final float[][] TOUCH_DIRECTION;
  
  private static final int TOUCH_DOWN = 1;
  
  private static final int TOUCH_END = 5;
  
  private static final int TOUCH_LEFT = 2;
  
  private static final int TOUCH_RIGHT = 3;
  
  private static final float[][] TOUCH_SIDES;
  
  private static final int TOUCH_START = 4;
  
  private static final int TOUCH_UP = 0;
  
  private float[] mAnchorDpDt = new float[2];
  
  private int mAutoCompleteMode = 0;
  
  private float mDragScale = 1.0F;
  
  private boolean mDragStarted = false;
  
  private float mDragThreshold = 10.0F;
  
  private int mFlags = 0;
  
  boolean mIsRotateMode = false;
  
  private float mLastTouchX;
  
  private float mLastTouchY;
  
  private int mLimitBoundsTo = -1;
  
  private float mMaxAcceleration = 1.2F;
  
  private float mMaxVelocity = 4.0F;
  
  private final MotionLayout mMotionLayout;
  
  private boolean mMoveWhenScrollAtTop = true;
  
  private int mOnTouchUp = 0;
  
  float mRotateCenterX = 0.5F;
  
  float mRotateCenterY = 0.5F;
  
  private int mRotationCenterId = -1;
  
  private int mSpringBoundary = 0;
  
  private float mSpringDamping = 10.0F;
  
  private float mSpringMass = 1.0F;
  
  private float mSpringStiffness = Float.NaN;
  
  private float mSpringStopThreshold = Float.NaN;
  
  private int[] mTempLoc = new int[2];
  
  private int mTouchAnchorId = -1;
  
  private int mTouchAnchorSide = 0;
  
  private float mTouchAnchorX = 0.5F;
  
  private float mTouchAnchorY = 0.5F;
  
  private float mTouchDirectionX = 0.0F;
  
  private float mTouchDirectionY = 1.0F;
  
  private int mTouchRegionId = -1;
  
  private int mTouchSide = 0;
  
  static {
    float[] arrayOfFloat2 = { 0.0F, 0.5F };
    float[] arrayOfFloat4 = { 1.0F, 0.5F };
    float[] arrayOfFloat1 = { 0.5F, 1.0F };
    float[] arrayOfFloat3 = { 0.5F, 0.5F };
    float[] arrayOfFloat5 = { 0.0F, 0.5F };
    TOUCH_SIDES = new float[][] { { 0.5F, 0.0F }, arrayOfFloat2, arrayOfFloat4, arrayOfFloat1, arrayOfFloat3, arrayOfFloat5, { 1.0F, 0.5F } };
    arrayOfFloat2 = new float[] { 0.0F, -1.0F };
    arrayOfFloat3 = new float[] { -1.0F, 0.0F };
    arrayOfFloat1 = new float[] { -1.0F, 0.0F };
    TOUCH_DIRECTION = new float[][] { arrayOfFloat2, { 0.0F, 1.0F }, arrayOfFloat3, { 1.0F, 0.0F }, arrayOfFloat1, { 1.0F, 0.0F } };
  }
  
  TouchResponse(Context paramContext, MotionLayout paramMotionLayout, XmlPullParser paramXmlPullParser) {
    this.mMotionLayout = paramMotionLayout;
    fillFromAttributeList(paramContext, Xml.asAttributeSet(paramXmlPullParser));
  }
  
  public TouchResponse(MotionLayout paramMotionLayout, OnSwipe paramOnSwipe) {
    this.mMotionLayout = paramMotionLayout;
    this.mTouchAnchorId = paramOnSwipe.getTouchAnchorId();
    int i = paramOnSwipe.getTouchAnchorSide();
    this.mTouchAnchorSide = i;
    if (i != -1) {
      float[][] arrayOfFloat1 = TOUCH_SIDES;
      this.mTouchAnchorX = arrayOfFloat1[i][0];
      this.mTouchAnchorY = arrayOfFloat1[i][1];
    } 
    i = paramOnSwipe.getDragDirection();
    this.mTouchSide = i;
    float[][] arrayOfFloat = TOUCH_DIRECTION;
    if (i < arrayOfFloat.length) {
      this.mTouchDirectionX = arrayOfFloat[i][0];
      this.mTouchDirectionY = arrayOfFloat[i][1];
    } else {
      this.mTouchDirectionY = Float.NaN;
      this.mTouchDirectionX = Float.NaN;
      this.mIsRotateMode = true;
    } 
    this.mMaxVelocity = paramOnSwipe.getMaxVelocity();
    this.mMaxAcceleration = paramOnSwipe.getMaxAcceleration();
    this.mMoveWhenScrollAtTop = paramOnSwipe.getMoveWhenScrollAtTop();
    this.mDragScale = paramOnSwipe.getDragScale();
    this.mDragThreshold = paramOnSwipe.getDragThreshold();
    this.mTouchRegionId = paramOnSwipe.getTouchRegionId();
    this.mOnTouchUp = paramOnSwipe.getOnTouchUp();
    this.mFlags = paramOnSwipe.getNestedScrollFlags();
    this.mLimitBoundsTo = paramOnSwipe.getLimitBoundsTo();
    this.mRotationCenterId = paramOnSwipe.getRotationCenterId();
    this.mSpringBoundary = paramOnSwipe.getSpringBoundary();
    this.mSpringDamping = paramOnSwipe.getSpringDamping();
    this.mSpringMass = paramOnSwipe.getSpringMass();
    this.mSpringStiffness = paramOnSwipe.getSpringStiffness();
    this.mSpringStopThreshold = paramOnSwipe.getSpringStopThreshold();
    this.mAutoCompleteMode = paramOnSwipe.getAutoCompleteMode();
  }
  
  private void fill(TypedArray paramTypedArray) {
    int i = paramTypedArray.getIndexCount();
    for (byte b = 0; b < i; b++) {
      int j = paramTypedArray.getIndex(b);
      if (j == R.styleable.OnSwipe_touchAnchorId) {
        this.mTouchAnchorId = paramTypedArray.getResourceId(j, this.mTouchAnchorId);
      } else if (j == R.styleable.OnSwipe_touchAnchorSide) {
        j = paramTypedArray.getInt(j, this.mTouchAnchorSide);
        this.mTouchAnchorSide = j;
        float[][] arrayOfFloat = TOUCH_SIDES;
        this.mTouchAnchorX = arrayOfFloat[j][0];
        this.mTouchAnchorY = arrayOfFloat[j][1];
      } else if (j == R.styleable.OnSwipe_dragDirection) {
        j = paramTypedArray.getInt(j, this.mTouchSide);
        this.mTouchSide = j;
        float[][] arrayOfFloat = TOUCH_DIRECTION;
        if (j < arrayOfFloat.length) {
          this.mTouchDirectionX = arrayOfFloat[j][0];
          this.mTouchDirectionY = arrayOfFloat[j][1];
        } else {
          this.mTouchDirectionY = Float.NaN;
          this.mTouchDirectionX = Float.NaN;
          this.mIsRotateMode = true;
        } 
      } else if (j == R.styleable.OnSwipe_maxVelocity) {
        this.mMaxVelocity = paramTypedArray.getFloat(j, this.mMaxVelocity);
      } else if (j == R.styleable.OnSwipe_maxAcceleration) {
        this.mMaxAcceleration = paramTypedArray.getFloat(j, this.mMaxAcceleration);
      } else if (j == R.styleable.OnSwipe_moveWhenScrollAtTop) {
        this.mMoveWhenScrollAtTop = paramTypedArray.getBoolean(j, this.mMoveWhenScrollAtTop);
      } else if (j == R.styleable.OnSwipe_dragScale) {
        this.mDragScale = paramTypedArray.getFloat(j, this.mDragScale);
      } else if (j == R.styleable.OnSwipe_dragThreshold) {
        this.mDragThreshold = paramTypedArray.getFloat(j, this.mDragThreshold);
      } else if (j == R.styleable.OnSwipe_touchRegionId) {
        this.mTouchRegionId = paramTypedArray.getResourceId(j, this.mTouchRegionId);
      } else if (j == R.styleable.OnSwipe_onTouchUp) {
        this.mOnTouchUp = paramTypedArray.getInt(j, this.mOnTouchUp);
      } else if (j == R.styleable.OnSwipe_nestedScrollFlags) {
        this.mFlags = paramTypedArray.getInteger(j, 0);
      } else if (j == R.styleable.OnSwipe_limitBoundsTo) {
        this.mLimitBoundsTo = paramTypedArray.getResourceId(j, 0);
      } else if (j == R.styleable.OnSwipe_rotationCenterId) {
        this.mRotationCenterId = paramTypedArray.getResourceId(j, this.mRotationCenterId);
      } else if (j == R.styleable.OnSwipe_springDamping) {
        this.mSpringDamping = paramTypedArray.getFloat(j, this.mSpringDamping);
      } else if (j == R.styleable.OnSwipe_springMass) {
        this.mSpringMass = paramTypedArray.getFloat(j, this.mSpringMass);
      } else if (j == R.styleable.OnSwipe_springStiffness) {
        this.mSpringStiffness = paramTypedArray.getFloat(j, this.mSpringStiffness);
      } else if (j == R.styleable.OnSwipe_springStopThreshold) {
        this.mSpringStopThreshold = paramTypedArray.getFloat(j, this.mSpringStopThreshold);
      } else if (j == R.styleable.OnSwipe_springBoundary) {
        this.mSpringBoundary = paramTypedArray.getInt(j, this.mSpringBoundary);
      } else if (j == R.styleable.OnSwipe_autoCompleteMode) {
        this.mAutoCompleteMode = paramTypedArray.getInt(j, this.mAutoCompleteMode);
      } 
    } 
  }
  
  private void fillFromAttributeList(Context paramContext, AttributeSet paramAttributeSet) {
    TypedArray typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.OnSwipe);
    fill(typedArray);
    typedArray.recycle();
  }
  
  float dot(float paramFloat1, float paramFloat2) {
    return this.mTouchDirectionX * paramFloat1 + this.mTouchDirectionY * paramFloat2;
  }
  
  public int getAnchorId() {
    return this.mTouchAnchorId;
  }
  
  public int getAutoCompleteMode() {
    return this.mAutoCompleteMode;
  }
  
  public int getFlags() {
    return this.mFlags;
  }
  
  RectF getLimitBoundsTo(ViewGroup paramViewGroup, RectF paramRectF) {
    int i = this.mLimitBoundsTo;
    if (i == -1)
      return null; 
    View view = paramViewGroup.findViewById(i);
    if (view == null)
      return null; 
    paramRectF.set(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
    return paramRectF;
  }
  
  int getLimitBoundsToId() {
    return this.mLimitBoundsTo;
  }
  
  float getMaxAcceleration() {
    return this.mMaxAcceleration;
  }
  
  public float getMaxVelocity() {
    return this.mMaxVelocity;
  }
  
  boolean getMoveWhenScrollAtTop() {
    return this.mMoveWhenScrollAtTop;
  }
  
  float getProgressDirection(float paramFloat1, float paramFloat2) {
    float f = this.mMotionLayout.getProgress();
    this.mMotionLayout.getAnchorDpDt(this.mTouchAnchorId, f, this.mTouchAnchorX, this.mTouchAnchorY, this.mAnchorDpDt);
    f = this.mTouchDirectionX;
    if (f != 0.0F) {
      float[] arrayOfFloat = this.mAnchorDpDt;
      if (arrayOfFloat[0] == 0.0F)
        arrayOfFloat[0] = 1.0E-7F; 
      paramFloat1 = f * paramFloat1 / arrayOfFloat[0];
    } else {
      float[] arrayOfFloat = this.mAnchorDpDt;
      if (arrayOfFloat[1] == 0.0F)
        arrayOfFloat[1] = 1.0E-7F; 
      paramFloat1 = this.mTouchDirectionY * paramFloat2 / arrayOfFloat[1];
    } 
    return paramFloat1;
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
  
  RectF getTouchRegion(ViewGroup paramViewGroup, RectF paramRectF) {
    int i = this.mTouchRegionId;
    if (i == -1)
      return null; 
    View view = paramViewGroup.findViewById(i);
    if (view == null)
      return null; 
    paramRectF.set(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
    return paramRectF;
  }
  
  int getTouchRegionId() {
    return this.mTouchRegionId;
  }
  
  boolean isDragStarted() {
    return this.mDragStarted;
  }
  
  void processTouchEvent(MotionEvent paramMotionEvent, MotionLayout.MotionTracker paramMotionTracker, int paramInt, MotionScene paramMotionScene) {
    float[] arrayOfFloat;
    float f1;
    float f2;
    float f3;
    float f4;
    float f5;
    float f6;
    if (this.mIsRotateMode) {
      processTouchRotateEvent(paramMotionEvent, paramMotionTracker, paramInt, paramMotionScene);
      return;
    } 
    paramMotionTracker.addMovement(paramMotionEvent);
    switch (paramMotionEvent.getAction()) {
      default:
        return;
      case 2:
        f1 = paramMotionEvent.getRawY() - this.mLastTouchY;
        f3 = paramMotionEvent.getRawX() - this.mLastTouchX;
        if (Math.abs(this.mTouchDirectionX * f3 + this.mTouchDirectionY * f1) > this.mDragThreshold || this.mDragStarted) {
          float f7 = this.mMotionLayout.getProgress();
          if (!this.mDragStarted) {
            this.mDragStarted = true;
            this.mMotionLayout.setProgress(f7);
          } 
          paramInt = this.mTouchAnchorId;
          if (paramInt != -1) {
            this.mMotionLayout.getAnchorDpDt(paramInt, f7, this.mTouchAnchorX, this.mTouchAnchorY, this.mAnchorDpDt);
          } else {
            float f = Math.min(this.mMotionLayout.getWidth(), this.mMotionLayout.getHeight());
            float[] arrayOfFloat2 = this.mAnchorDpDt;
            arrayOfFloat2[1] = this.mTouchDirectionY * f;
            arrayOfFloat2[0] = this.mTouchDirectionX * f;
          } 
          float f8 = this.mTouchDirectionX;
          float[] arrayOfFloat1 = this.mAnchorDpDt;
          if (Math.abs((f8 * arrayOfFloat1[0] + this.mTouchDirectionY * arrayOfFloat1[1]) * this.mDragScale) < 0.01D) {
            arrayOfFloat1 = this.mAnchorDpDt;
            arrayOfFloat1[0] = 0.01F;
            arrayOfFloat1[1] = 0.01F;
          } 
          if (this.mTouchDirectionX != 0.0F) {
            f1 = f3 / this.mAnchorDpDt[0];
          } else {
            f1 /= this.mAnchorDpDt[1];
          } 
          f7 = Math.max(Math.min(f7 + f1, 1.0F), 0.0F);
          f1 = f7;
          if (this.mOnTouchUp == 6)
            f1 = Math.max(f7, 0.01F); 
          f7 = f1;
          if (this.mOnTouchUp == 7)
            f7 = Math.min(f1, 0.99F); 
          f1 = this.mMotionLayout.getProgress();
          if (f7 != f1) {
            if (f1 == 0.0F || f1 == 1.0F) {
              boolean bool;
              MotionLayout motionLayout = this.mMotionLayout;
              if (f1 == 0.0F) {
                bool = true;
              } else {
                bool = false;
              } 
              motionLayout.endTrigger(bool);
            } 
            this.mMotionLayout.setProgress(f7);
            paramMotionTracker.computeCurrentVelocity(1000);
            f7 = paramMotionTracker.getXVelocity();
            f1 = paramMotionTracker.getYVelocity();
            if (this.mTouchDirectionX != 0.0F) {
              f1 = f7 / this.mAnchorDpDt[0];
            } else {
              f1 /= this.mAnchorDpDt[1];
            } 
            this.mMotionLayout.mLastVelocity = f1;
          } else {
            this.mMotionLayout.mLastVelocity = 0.0F;
          } 
          this.mLastTouchX = paramMotionEvent.getRawX();
          this.mLastTouchY = paramMotionEvent.getRawY();
        } 
      case 1:
        this.mDragStarted = false;
        paramMotionTracker.computeCurrentVelocity(1000);
        f1 = paramMotionTracker.getXVelocity();
        f3 = paramMotionTracker.getYVelocity();
        f4 = this.mMotionLayout.getProgress();
        f2 = f4;
        paramInt = this.mTouchAnchorId;
        if (paramInt != -1) {
          this.mMotionLayout.getAnchorDpDt(paramInt, f2, this.mTouchAnchorX, this.mTouchAnchorY, this.mAnchorDpDt);
        } else {
          float f = Math.min(this.mMotionLayout.getWidth(), this.mMotionLayout.getHeight());
          float[] arrayOfFloat1 = this.mAnchorDpDt;
          arrayOfFloat1[1] = this.mTouchDirectionY * f;
          arrayOfFloat1[0] = this.mTouchDirectionX * f;
        } 
        f5 = this.mTouchDirectionX;
        arrayOfFloat = this.mAnchorDpDt;
        f6 = arrayOfFloat[0];
        f6 = this.mTouchDirectionY;
        f6 = arrayOfFloat[1];
        if (f5 != 0.0F) {
          f1 /= arrayOfFloat[0];
        } else {
          f1 = f3 / arrayOfFloat[1];
        } 
        f3 = f2;
        if (!Float.isNaN(f1))
          f3 = f2 + f1 / 3.0F; 
        if (f3 != 0.0F && f3 != 1.0F) {
          paramInt = this.mOnTouchUp;
          if (paramInt != 3) {
            if (f3 < 0.5D) {
              f3 = 0.0F;
            } else {
              f3 = 1.0F;
            } 
            f2 = f1;
            if (paramInt == 6) {
              f2 = f1;
              if (f4 + f1 < 0.0F)
                f2 = Math.abs(f1); 
              f3 = 1.0F;
            } 
            f1 = f2;
            if (this.mOnTouchUp == 7) {
              f1 = f2;
              if (f4 + f2 > 1.0F)
                f1 = -Math.abs(f2); 
              f3 = 0.0F;
            } 
            this.mMotionLayout.touchAnimateTo(this.mOnTouchUp, f3, f1);
            if (0.0F >= f4 || 1.0F <= f4)
              this.mMotionLayout.setState(MotionLayout.TransitionState.FINISHED); 
          } 
        } 
        if (0.0F >= f3 || 1.0F <= f3)
          this.mMotionLayout.setState(MotionLayout.TransitionState.FINISHED); 
      case 0:
        break;
    } 
    this.mLastTouchX = arrayOfFloat.getRawX();
    this.mLastTouchY = arrayOfFloat.getRawY();
    this.mDragStarted = false;
  }
  
  void processTouchRotateEvent(MotionEvent paramMotionEvent, MotionLayout.MotionTracker paramMotionTracker, int paramInt, MotionScene paramMotionScene) {
    float[] arrayOfFloat;
    double d;
    float f1;
    float f2;
    float f3;
    float f4;
    float f5;
    float f6;
    paramMotionTracker.addMovement(paramMotionEvent);
    switch (paramMotionEvent.getAction()) {
      default:
        return;
      case 2:
        paramMotionEvent.getRawY();
        f1 = this.mLastTouchY;
        paramMotionEvent.getRawX();
        f1 = this.mLastTouchX;
        f4 = this.mMotionLayout.getWidth() / 2.0F;
        f3 = this.mMotionLayout.getHeight() / 2.0F;
        paramInt = this.mRotationCenterId;
        if (paramInt != -1) {
          View view = this.mMotionLayout.findViewById(paramInt);
          this.mMotionLayout.getLocationOnScreen(this.mTempLoc);
          f1 = this.mTempLoc[0] + (view.getLeft() + view.getRight()) / 2.0F;
          f2 = this.mTempLoc[1] + (view.getTop() + view.getBottom()) / 2.0F;
        } else {
          paramInt = this.mTouchAnchorId;
          f1 = f4;
          f2 = f3;
          if (paramInt != -1) {
            MotionController motionController = this.mMotionLayout.getMotionController(paramInt);
            View view = this.mMotionLayout.findViewById(motionController.getAnimateRelativeTo());
            if (view == null) {
              Log.e("TouchResponse", "could not find view to animate to");
              f1 = f4;
              f2 = f3;
            } else {
              this.mMotionLayout.getLocationOnScreen(this.mTempLoc);
              f1 = this.mTempLoc[0] + (view.getLeft() + view.getRight()) / 2.0F;
              f2 = this.mTempLoc[1] + (view.getTop() + view.getBottom()) / 2.0F;
            } 
          } 
        } 
        f5 = paramMotionEvent.getRawX();
        f6 = paramMotionEvent.getRawY();
        d = Math.atan2((paramMotionEvent.getRawY() - f2), (paramMotionEvent.getRawX() - f1));
        f4 = (float)((d - Math.atan2((this.mLastTouchY - f2), (this.mLastTouchX - f1))) * 180.0D / Math.PI);
        if (f4 > 330.0F) {
          f3 = f4 - 360.0F;
        } else {
          f3 = f4;
          if (f4 < -330.0F)
            f3 = f4 + 360.0F; 
        } 
        if (Math.abs(f3) > 0.01D || this.mDragStarted) {
          f4 = this.mMotionLayout.getProgress();
          if (!this.mDragStarted) {
            this.mDragStarted = true;
            this.mMotionLayout.setProgress(f4);
          } 
          paramInt = this.mTouchAnchorId;
          if (paramInt != -1) {
            this.mMotionLayout.getAnchorDpDt(paramInt, f4, this.mTouchAnchorX, this.mTouchAnchorY, this.mAnchorDpDt);
            float[] arrayOfFloat1 = this.mAnchorDpDt;
            arrayOfFloat1[1] = (float)Math.toDegrees(arrayOfFloat1[1]);
          } else {
            this.mAnchorDpDt[1] = 360.0F;
          } 
          f4 = Math.max(Math.min(f4 + this.mDragScale * f3 / this.mAnchorDpDt[1], 1.0F), 0.0F);
          f3 = this.mMotionLayout.getProgress();
          if (f4 != f3) {
            if (f3 == 0.0F || f3 == 1.0F) {
              boolean bool;
              MotionLayout motionLayout = this.mMotionLayout;
              if (f3 == 0.0F) {
                bool = true;
              } else {
                bool = false;
              } 
              motionLayout.endTrigger(bool);
            } 
            this.mMotionLayout.setProgress(f4);
            paramMotionTracker.computeCurrentVelocity(1000);
            f4 = paramMotionTracker.getXVelocity();
            f3 = paramMotionTracker.getYVelocity();
            f1 = (float)(Math.hypot(f3, f4) * Math.sin(Math.atan2(f3, f4) - d) / Math.hypot((f5 - f1), (f6 - f2)));
            this.mMotionLayout.mLastVelocity = (float)Math.toDegrees(f1);
          } else {
            this.mMotionLayout.mLastVelocity = 0.0F;
          } 
          this.mLastTouchX = paramMotionEvent.getRawX();
          this.mLastTouchY = paramMotionEvent.getRawY();
        } 
      case 1:
        this.mDragStarted = false;
        paramMotionTracker.computeCurrentVelocity(16);
        f6 = paramMotionTracker.getXVelocity();
        f5 = paramMotionTracker.getYVelocity();
        f4 = this.mMotionLayout.getProgress();
        f3 = f4;
        f1 = this.mMotionLayout.getWidth() / 2.0F;
        f2 = this.mMotionLayout.getHeight() / 2.0F;
        paramInt = this.mRotationCenterId;
        if (paramInt != -1) {
          View view = this.mMotionLayout.findViewById(paramInt);
          this.mMotionLayout.getLocationOnScreen(this.mTempLoc);
          f1 = this.mTempLoc[0] + (view.getLeft() + view.getRight()) / 2.0F;
          f2 = this.mTempLoc[1] + (view.getTop() + view.getBottom()) / 2.0F;
        } else {
          paramInt = this.mTouchAnchorId;
          if (paramInt != -1) {
            MotionController motionController = this.mMotionLayout.getMotionController(paramInt);
            View view = this.mMotionLayout.findViewById(motionController.getAnimateRelativeTo());
            this.mMotionLayout.getLocationOnScreen(this.mTempLoc);
            f1 = this.mTempLoc[0] + (view.getLeft() + view.getRight()) / 2.0F;
            f2 = this.mTempLoc[1] + (view.getTop() + view.getBottom()) / 2.0F;
          } 
        } 
        f1 = paramMotionEvent.getRawX() - f1;
        f2 = paramMotionEvent.getRawY() - f2;
        d = Math.toDegrees(Math.atan2(f2, f1));
        paramInt = this.mTouchAnchorId;
        if (paramInt != -1) {
          this.mMotionLayout.getAnchorDpDt(paramInt, f3, this.mTouchAnchorX, this.mTouchAnchorY, this.mAnchorDpDt);
          arrayOfFloat = this.mAnchorDpDt;
          arrayOfFloat[1] = (float)Math.toDegrees(arrayOfFloat[1]);
        } else {
          this.mAnchorDpDt[1] = 360.0F;
        } 
        f2 = (float)(Math.toDegrees(Math.atan2((f5 + f2), (f6 + f1))) - d) * 62.5F;
        f1 = f3;
        if (!Float.isNaN(f2))
          f1 = f3 + f2 * 3.0F * this.mDragScale / this.mAnchorDpDt[1]; 
        if (f1 != 0.0F && f1 != 1.0F) {
          paramInt = this.mOnTouchUp;
          if (paramInt != 3) {
            f3 = this.mDragScale * f2 / this.mAnchorDpDt[1];
            if (f1 < 0.5D) {
              f2 = 0.0F;
            } else {
              f2 = 1.0F;
            } 
            f1 = f3;
            if (paramInt == 6) {
              f1 = f3;
              if (f4 + f3 < 0.0F)
                f1 = Math.abs(f3); 
              f2 = 1.0F;
            } 
            f3 = f2;
            f2 = f1;
            if (this.mOnTouchUp == 7) {
              f2 = f1;
              if (f4 + f1 > 1.0F)
                f2 = -Math.abs(f1); 
              f3 = 0.0F;
            } 
            this.mMotionLayout.touchAnimateTo(this.mOnTouchUp, f3, f2 * 3.0F);
            if (0.0F >= f4 || 1.0F <= f4)
              this.mMotionLayout.setState(MotionLayout.TransitionState.FINISHED); 
          } 
        } 
        if (0.0F >= f1 || 1.0F <= f1)
          this.mMotionLayout.setState(MotionLayout.TransitionState.FINISHED); 
      case 0:
        break;
    } 
    this.mLastTouchX = arrayOfFloat.getRawX();
    this.mLastTouchY = arrayOfFloat.getRawY();
    this.mDragStarted = false;
  }
  
  void scrollMove(float paramFloat1, float paramFloat2) {
    float f1 = this.mTouchDirectionX;
    f1 = this.mTouchDirectionY;
    f1 = this.mMotionLayout.getProgress();
    if (!this.mDragStarted) {
      this.mDragStarted = true;
      this.mMotionLayout.setProgress(f1);
    } 
    this.mMotionLayout.getAnchorDpDt(this.mTouchAnchorId, f1, this.mTouchAnchorX, this.mTouchAnchorY, this.mAnchorDpDt);
    float f2 = this.mTouchDirectionX;
    float[] arrayOfFloat = this.mAnchorDpDt;
    if (Math.abs(f2 * arrayOfFloat[0] + this.mTouchDirectionY * arrayOfFloat[1]) < 0.01D) {
      arrayOfFloat = this.mAnchorDpDt;
      arrayOfFloat[0] = 0.01F;
      arrayOfFloat[1] = 0.01F;
    } 
    f2 = this.mTouchDirectionX;
    if (f2 != 0.0F) {
      paramFloat1 = f2 * paramFloat1 / this.mAnchorDpDt[0];
    } else {
      paramFloat1 = this.mTouchDirectionY * paramFloat2 / this.mAnchorDpDt[1];
    } 
    paramFloat1 = Math.max(Math.min(f1 + paramFloat1, 1.0F), 0.0F);
    if (paramFloat1 != this.mMotionLayout.getProgress())
      this.mMotionLayout.setProgress(paramFloat1); 
  }
  
  void scrollUp(float paramFloat1, float paramFloat2) {
    boolean bool = false;
    this.mDragStarted = false;
    float f1 = this.mMotionLayout.getProgress();
    this.mMotionLayout.getAnchorDpDt(this.mTouchAnchorId, f1, this.mTouchAnchorX, this.mTouchAnchorY, this.mAnchorDpDt);
    float f3 = this.mTouchDirectionX;
    float[] arrayOfFloat = this.mAnchorDpDt;
    float f2 = arrayOfFloat[0];
    float f4 = this.mTouchDirectionY;
    f2 = arrayOfFloat[1];
    f2 = 0.0F;
    if (f3 != 0.0F) {
      paramFloat1 = f3 * paramFloat1 / arrayOfFloat[0];
    } else {
      paramFloat1 = f4 * paramFloat2 / arrayOfFloat[1];
    } 
    paramFloat2 = f1;
    if (!Float.isNaN(paramFloat1))
      paramFloat2 = f1 + paramFloat1 / 3.0F; 
    if (paramFloat2 != 0.0F) {
      boolean bool1;
      if (paramFloat2 != 1.0F) {
        bool1 = true;
      } else {
        bool1 = false;
      } 
      int i = this.mOnTouchUp;
      if (i != 3)
        bool = true; 
      if ((bool & bool1) != 0) {
        MotionLayout motionLayout = this.mMotionLayout;
        if (paramFloat2 < 0.5D) {
          paramFloat2 = f2;
        } else {
          paramFloat2 = 1.0F;
        } 
        motionLayout.touchAnimateTo(i, paramFloat2, paramFloat1);
      } 
    } 
  }
  
  public void setAnchorId(int paramInt) {
    this.mTouchAnchorId = paramInt;
  }
  
  void setAutoCompleteMode(int paramInt) {
    this.mAutoCompleteMode = paramInt;
  }
  
  void setDown(float paramFloat1, float paramFloat2) {
    this.mLastTouchX = paramFloat1;
    this.mLastTouchY = paramFloat2;
  }
  
  public void setMaxAcceleration(float paramFloat) {
    this.mMaxAcceleration = paramFloat;
  }
  
  public void setMaxVelocity(float paramFloat) {
    this.mMaxVelocity = paramFloat;
  }
  
  public void setRTL(boolean paramBoolean) {
    if (paramBoolean) {
      float[][] arrayOfFloat1 = TOUCH_DIRECTION;
      arrayOfFloat1[4] = arrayOfFloat1[3];
      arrayOfFloat1[5] = arrayOfFloat1[2];
      arrayOfFloat1 = TOUCH_SIDES;
      arrayOfFloat1[5] = arrayOfFloat1[2];
      arrayOfFloat1[6] = arrayOfFloat1[1];
    } else {
      float[][] arrayOfFloat1 = TOUCH_DIRECTION;
      arrayOfFloat1[4] = arrayOfFloat1[2];
      arrayOfFloat1[5] = arrayOfFloat1[3];
      arrayOfFloat1 = TOUCH_SIDES;
      arrayOfFloat1[5] = arrayOfFloat1[1];
      arrayOfFloat1[6] = arrayOfFloat1[2];
    } 
    float[][] arrayOfFloat = TOUCH_SIDES;
    int i = this.mTouchAnchorSide;
    this.mTouchAnchorX = arrayOfFloat[i][0];
    this.mTouchAnchorY = arrayOfFloat[i][1];
    i = this.mTouchSide;
    arrayOfFloat = TOUCH_DIRECTION;
    if (i >= arrayOfFloat.length)
      return; 
    this.mTouchDirectionX = arrayOfFloat[i][0];
    this.mTouchDirectionY = arrayOfFloat[i][1];
  }
  
  public void setTouchAnchorLocation(float paramFloat1, float paramFloat2) {
    this.mTouchAnchorX = paramFloat1;
    this.mTouchAnchorY = paramFloat2;
  }
  
  public void setTouchUpMode(int paramInt) {
    this.mOnTouchUp = paramInt;
  }
  
  void setUpTouchEvent(float paramFloat1, float paramFloat2) {
    this.mLastTouchX = paramFloat1;
    this.mLastTouchY = paramFloat2;
    this.mDragStarted = false;
  }
  
  void setupTouch() {
    View view = null;
    int i = this.mTouchAnchorId;
    if (i != -1) {
      View view1 = this.mMotionLayout.findViewById(i);
      view = view1;
      if (view1 == null) {
        String str = String.valueOf(Debug.getName(this.mMotionLayout.getContext(), this.mTouchAnchorId));
        if (str.length() != 0) {
          str = "cannot find TouchAnchorId @id/".concat(str);
        } else {
          str = new String("cannot find TouchAnchorId @id/");
        } 
        Log.e("TouchResponse", str);
        view = view1;
      } 
    } 
    if (view instanceof NestedScrollView) {
      NestedScrollView nestedScrollView = (NestedScrollView)view;
      nestedScrollView.setOnTouchListener(new View.OnTouchListener(this) {
            public boolean onTouch(View param1View, MotionEvent param1MotionEvent) {
              return false;
            }
          });
      nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener(this) {
            public void onScrollChange(NestedScrollView param1NestedScrollView, int param1Int1, int param1Int2, int param1Int3, int param1Int4) {}
          });
    } 
  }
  
  public String toString() {
    String str;
    if (Float.isNaN(this.mTouchDirectionX)) {
      str = "rotation";
    } else {
      float f1 = this.mTouchDirectionX;
      float f2 = this.mTouchDirectionY;
      str = (new StringBuilder(33)).append(f1).append(" , ").append(f2).toString();
    } 
    return str;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\motion\widget\TouchResponse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */