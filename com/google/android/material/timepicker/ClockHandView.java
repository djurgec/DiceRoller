package com.google.android.material.timepicker;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import androidx.core.view.ViewCompat;
import com.google.android.material.R;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class ClockHandView extends View {
  private static final int ANIMATION_DURATION = 200;
  
  private boolean animatingOnTouchUp;
  
  private final float centerDotRadius;
  
  private boolean changedDuringTouch;
  
  private int circleRadius;
  
  private double degRad;
  
  private float downX;
  
  private float downY;
  
  private boolean isInTapRegion;
  
  private final List<OnRotateListener> listeners = new ArrayList<>();
  
  private OnActionUpListener onActionUpListener;
  
  private float originalDeg;
  
  private final Paint paint;
  
  private ValueAnimator rotationAnimator;
  
  private int scaledTouchSlop;
  
  private final RectF selectorBox;
  
  private final int selectorRadius;
  
  private final int selectorStrokeWidth;
  
  public ClockHandView(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public ClockHandView(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, R.attr.materialClockStyle);
  }
  
  public ClockHandView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    Paint paint = new Paint();
    this.paint = paint;
    this.selectorBox = new RectF();
    TypedArray typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.ClockHandView, paramInt, R.style.Widget_MaterialComponents_TimePicker_Clock);
    this.circleRadius = typedArray.getDimensionPixelSize(R.styleable.ClockHandView_materialCircleRadius, 0);
    this.selectorRadius = typedArray.getDimensionPixelSize(R.styleable.ClockHandView_selectorSize, 0);
    Resources resources = getResources();
    this.selectorStrokeWidth = resources.getDimensionPixelSize(R.dimen.material_clock_hand_stroke_width);
    this.centerDotRadius = resources.getDimensionPixelSize(R.dimen.material_clock_hand_center_dot_radius);
    paramInt = typedArray.getColor(R.styleable.ClockHandView_clockHandColor, 0);
    paint.setAntiAlias(true);
    paint.setColor(paramInt);
    setHandRotation(0.0F);
    this.scaledTouchSlop = ViewConfiguration.get(paramContext).getScaledTouchSlop();
    ViewCompat.setImportantForAccessibility(this, 2);
    typedArray.recycle();
  }
  
  private void drawSelector(Canvas paramCanvas) {
    int j = getHeight() / 2;
    int i = getWidth() / 2;
    float f2 = i;
    float f1 = this.circleRadius;
    float f4 = (float)Math.cos(this.degRad);
    float f3 = j;
    float f6 = this.circleRadius;
    float f5 = (float)Math.sin(this.degRad);
    this.paint.setStrokeWidth(0.0F);
    paramCanvas.drawCircle(f2 + f1 * f4, f3 + f6 * f5, this.selectorRadius, this.paint);
    double d1 = Math.sin(this.degRad);
    double d2 = Math.cos(this.degRad);
    f2 = (this.circleRadius - this.selectorRadius);
    f1 = ((int)(f2 * d2) + i);
    f2 = ((int)(f2 * d1) + j);
    this.paint.setStrokeWidth(this.selectorStrokeWidth);
    paramCanvas.drawLine(i, j, f1, f2, this.paint);
    paramCanvas.drawCircle(i, j, this.centerDotRadius, this.paint);
  }
  
  private int getDegreesFromXY(float paramFloat1, float paramFloat2) {
    int j = getWidth() / 2;
    int i = getHeight() / 2;
    double d = (paramFloat1 - j);
    j = (int)Math.toDegrees(Math.atan2((paramFloat2 - i), d)) + 90;
    i = j;
    if (j < 0)
      i = j + 360; 
    return i;
  }
  
  private Pair<Float, Float> getValuesForAnimation(float paramFloat) {
    float f3 = getHandRotation();
    float f2 = f3;
    float f1 = paramFloat;
    if (Math.abs(f3 - paramFloat) > 180.0F) {
      float f = paramFloat;
      if (f3 > 180.0F) {
        f = paramFloat;
        if (paramFloat < 180.0F)
          f = paramFloat + 360.0F; 
      } 
      f2 = f3;
      f1 = f;
      if (f3 < 180.0F) {
        f2 = f3;
        f1 = f;
        if (f > 180.0F) {
          f2 = f3 + 360.0F;
          f1 = f;
        } 
      } 
    } 
    return new Pair(Float.valueOf(f2), Float.valueOf(f1));
  }
  
  private boolean handleTouchInput(float paramFloat1, float paramFloat2, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3) {
    boolean bool1;
    int i = getDegreesFromXY(paramFloat1, paramFloat2);
    paramFloat2 = getHandRotation();
    paramFloat1 = i;
    boolean bool2 = false;
    if (paramFloat2 != paramFloat1) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    if (paramBoolean2 && bool1)
      return true; 
    if (bool1 || paramBoolean1) {
      paramFloat1 = i;
      paramBoolean1 = bool2;
      if (paramBoolean3) {
        paramBoolean1 = bool2;
        if (this.animatingOnTouchUp)
          paramBoolean1 = true; 
      } 
      setHandRotation(paramFloat1, paramBoolean1);
      return true;
    } 
    return false;
  }
  
  private void setHandRotationInternal(float paramFloat, boolean paramBoolean) {
    paramFloat %= 360.0F;
    this.originalDeg = paramFloat;
    this.degRad = Math.toRadians((paramFloat - 90.0F));
    int i = getHeight() / 2;
    float f2 = (getWidth() / 2) + this.circleRadius * (float)Math.cos(this.degRad);
    float f1 = i + this.circleRadius * (float)Math.sin(this.degRad);
    RectF rectF = this.selectorBox;
    i = this.selectorRadius;
    rectF.set(f2 - i, f1 - i, i + f2, i + f1);
    Iterator<OnRotateListener> iterator = this.listeners.iterator();
    while (iterator.hasNext())
      ((OnRotateListener)iterator.next()).onRotate(paramFloat, paramBoolean); 
    invalidate();
  }
  
  public void addOnRotateListener(OnRotateListener paramOnRotateListener) {
    this.listeners.add(paramOnRotateListener);
  }
  
  public RectF getCurrentSelectorBox() {
    return this.selectorBox;
  }
  
  public float getHandRotation() {
    return this.originalDeg;
  }
  
  public int getSelectorRadius() {
    return this.selectorRadius;
  }
  
  protected void onDraw(Canvas paramCanvas) {
    super.onDraw(paramCanvas);
    drawSelector(paramCanvas);
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
    setHandRotation(getHandRotation());
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    int i;
    int k;
    int j = paramMotionEvent.getActionMasked();
    boolean bool1 = false;
    boolean bool3 = false;
    boolean bool4 = false;
    boolean bool5 = false;
    float f2 = paramMotionEvent.getX();
    float f1 = paramMotionEvent.getY();
    boolean bool2 = false;
    switch (j) {
      default:
        bool2 = bool4;
        bool3 = bool5;
        break;
      case 1:
      case 2:
        k = (int)(f2 - this.downX);
        i = (int)(f1 - this.downY);
        if (k * k + i * i > this.scaledTouchSlop) {
          bool1 = true;
        } else {
          bool1 = false;
        } 
        this.isInTapRegion = bool1;
        bool1 = bool3;
        if (this.changedDuringTouch)
          bool1 = true; 
        if (j == 1)
          bool2 = true; 
        bool3 = bool2;
        bool2 = bool4;
        break;
      case 0:
        this.downX = f2;
        this.downY = f1;
        this.isInTapRegion = true;
        this.changedDuringTouch = false;
        bool2 = true;
        bool3 = bool5;
        break;
    } 
    bool4 = this.changedDuringTouch;
    bool1 = handleTouchInput(f2, f1, bool1, bool2, bool3) | bool4;
    this.changedDuringTouch = bool1;
    if (bool1 && bool3) {
      OnActionUpListener onActionUpListener = this.onActionUpListener;
      if (onActionUpListener != null)
        onActionUpListener.onActionUp(getDegreesFromXY(f2, f1), this.isInTapRegion); 
    } 
    return true;
  }
  
  public void setAnimateOnTouchUp(boolean paramBoolean) {
    this.animatingOnTouchUp = paramBoolean;
  }
  
  public void setCircleRadius(int paramInt) {
    this.circleRadius = paramInt;
    invalidate();
  }
  
  public void setHandRotation(float paramFloat) {
    setHandRotation(paramFloat, false);
  }
  
  public void setHandRotation(float paramFloat, boolean paramBoolean) {
    ValueAnimator valueAnimator2 = this.rotationAnimator;
    if (valueAnimator2 != null)
      valueAnimator2.cancel(); 
    if (!paramBoolean) {
      setHandRotationInternal(paramFloat, false);
      return;
    } 
    Pair<Float, Float> pair = getValuesForAnimation(paramFloat);
    ValueAnimator valueAnimator1 = ValueAnimator.ofFloat(new float[] { ((Float)pair.first).floatValue(), ((Float)pair.second).floatValue() });
    this.rotationAnimator = valueAnimator1;
    valueAnimator1.setDuration(200L);
    this.rotationAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
          final ClockHandView this$0;
          
          public void onAnimationUpdate(ValueAnimator param1ValueAnimator) {
            float f = ((Float)param1ValueAnimator.getAnimatedValue()).floatValue();
            ClockHandView.this.setHandRotationInternal(f, true);
          }
        });
    this.rotationAnimator.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter() {
          final ClockHandView this$0;
          
          public void onAnimationCancel(Animator param1Animator) {
            param1Animator.end();
          }
        });
    this.rotationAnimator.start();
  }
  
  public void setOnActionUpListener(OnActionUpListener paramOnActionUpListener) {
    this.onActionUpListener = paramOnActionUpListener;
  }
  
  public static interface OnActionUpListener {
    void onActionUp(float param1Float, boolean param1Boolean);
  }
  
  public static interface OnRotateListener {
    void onRotate(float param1Float, boolean param1Boolean);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\timepicker\ClockHandView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */