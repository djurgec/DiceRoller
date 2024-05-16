package com.google.android.material.slider;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityManager;
import android.widget.SeekBar;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.math.MathUtils;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.customview.widget.ExploreByTouchHelper;
import com.google.android.material.R;
import com.google.android.material.animation.AnimationUtils;
import com.google.android.material.drawable.DrawableUtils;
import com.google.android.material.internal.DescendantOffsetUtils;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.internal.ViewOverlayImpl;
import com.google.android.material.internal.ViewUtils;
import com.google.android.material.resources.MaterialResources;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.android.material.theme.overlay.MaterialThemeOverlay;
import com.google.android.material.tooltip.TooltipDrawable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

abstract class BaseSlider<S extends BaseSlider<S, L, T>, L extends BaseOnChangeListener<S>, T extends BaseOnSliderTouchListener<S>> extends View {
  static final int DEF_STYLE_RES;
  
  private static final String EXCEPTION_ILLEGAL_DISCRETE_VALUE = "Value(%s) must be equal to valueFrom(%s) plus a multiple of stepSize(%s) when using stepSize(%s)";
  
  private static final String EXCEPTION_ILLEGAL_STEP_SIZE = "The stepSize(%s) must be 0, or a factor of the valueFrom(%s)-valueTo(%s) range";
  
  private static final String EXCEPTION_ILLEGAL_VALUE = "Slider value(%s) must be greater or equal to valueFrom(%s), and lower or equal to valueTo(%s)";
  
  private static final String EXCEPTION_ILLEGAL_VALUE_FROM = "valueFrom(%s) must be smaller than valueTo(%s)";
  
  private static final String EXCEPTION_ILLEGAL_VALUE_TO = "valueTo(%s) must be greater than valueFrom(%s)";
  
  private static final int HALO_ALPHA = 63;
  
  private static final long LABEL_ANIMATION_ENTER_DURATION = 83L;
  
  private static final long LABEL_ANIMATION_EXIT_DURATION = 117L;
  
  private static final String TAG = BaseSlider.class.getSimpleName();
  
  private static final double THRESHOLD = 1.0E-4D;
  
  private static final int TIMEOUT_SEND_ACCESSIBILITY_EVENT = 200;
  
  static final int UNIT_PX = 0;
  
  static final int UNIT_VALUE = 1;
  
  private static final String WARNING_FLOATING_POINT_ERRROR = "Floating point value used for %s(%s). Using floats can have rounding errors which may result in incorrect values. Instead, consider using integers with a custom LabelFormatter to display the  value correctly.";
  
  private AccessibilityEventSender accessibilityEventSender;
  
  private final AccessibilityHelper accessibilityHelper;
  
  private final AccessibilityManager accessibilityManager;
  
  private int activeThumbIdx = -1;
  
  private final Paint activeTicksPaint;
  
  private final Paint activeTrackPaint;
  
  private final List<L> changeListeners = new ArrayList<>();
  
  private int defaultThumbRadius;
  
  private boolean dirtyConfig;
  
  private int focusedThumbIdx = -1;
  
  private boolean forceDrawCompatHalo;
  
  private LabelFormatter formatter;
  
  private ColorStateList haloColor;
  
  private final Paint haloPaint;
  
  private int haloRadius;
  
  private final Paint inactiveTicksPaint;
  
  private final Paint inactiveTrackPaint;
  
  private boolean isLongPress = false;
  
  private int labelBehavior;
  
  private final TooltipDrawableFactory labelMaker;
  
  private int labelPadding;
  
  private final List<TooltipDrawable> labels = new ArrayList<>();
  
  private boolean labelsAreAnimatedIn = false;
  
  private ValueAnimator labelsInAnimator;
  
  private ValueAnimator labelsOutAnimator;
  
  private MotionEvent lastEvent;
  
  private int minTrackSidePadding;
  
  private final int scaledTouchSlop;
  
  private int separationUnit;
  
  private float stepSize = 0.0F;
  
  private final MaterialShapeDrawable thumbDrawable;
  
  private boolean thumbIsPressed = false;
  
  private final Paint thumbPaint;
  
  private int thumbRadius;
  
  private ColorStateList tickColorActive;
  
  private ColorStateList tickColorInactive;
  
  private boolean tickVisible = true;
  
  private float[] ticksCoordinates;
  
  private float touchDownX;
  
  private final List<T> touchListeners = new ArrayList<>();
  
  private float touchPosition;
  
  private ColorStateList trackColorActive;
  
  private ColorStateList trackColorInactive;
  
  private int trackHeight;
  
  private int trackSidePadding;
  
  private int trackTop;
  
  private int trackWidth;
  
  private float valueFrom;
  
  private float valueTo;
  
  private ArrayList<Float> values = new ArrayList<>();
  
  private int widgetHeight;
  
  static {
    DEF_STYLE_RES = R.style.Widget_MaterialComponents_Slider;
  }
  
  public BaseSlider(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public BaseSlider(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, R.attr.sliderStyle);
  }
  
  public BaseSlider(Context paramContext, final AttributeSet attrs, final int defStyleAttr) {
    super(MaterialThemeOverlay.wrap(paramContext, attrs, defStyleAttr, DEF_STYLE_RES), attrs, defStyleAttr);
    MaterialShapeDrawable materialShapeDrawable = new MaterialShapeDrawable();
    this.thumbDrawable = materialShapeDrawable;
    this.separationUnit = 0;
    paramContext = getContext();
    Paint paint = new Paint();
    this.inactiveTrackPaint = paint;
    paint.setStyle(Paint.Style.STROKE);
    paint.setStrokeCap(Paint.Cap.ROUND);
    paint = new Paint();
    this.activeTrackPaint = paint;
    paint.setStyle(Paint.Style.STROKE);
    paint.setStrokeCap(Paint.Cap.ROUND);
    paint = new Paint(1);
    this.thumbPaint = paint;
    paint.setStyle(Paint.Style.FILL);
    paint.setXfermode((Xfermode)new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    paint = new Paint(1);
    this.haloPaint = paint;
    paint.setStyle(Paint.Style.FILL);
    paint = new Paint();
    this.inactiveTicksPaint = paint;
    paint.setStyle(Paint.Style.STROKE);
    paint.setStrokeCap(Paint.Cap.ROUND);
    paint = new Paint();
    this.activeTicksPaint = paint;
    paint.setStyle(Paint.Style.STROKE);
    paint.setStrokeCap(Paint.Cap.ROUND);
    loadResources(paramContext.getResources());
    this.labelMaker = new TooltipDrawableFactory() {
        final BaseSlider this$0;
        
        final AttributeSet val$attrs;
        
        final int val$defStyleAttr;
        
        public TooltipDrawable createTooltipDrawable() {
          TypedArray typedArray = ThemeEnforcement.obtainStyledAttributes(BaseSlider.this.getContext(), attrs, R.styleable.Slider, defStyleAttr, BaseSlider.DEF_STYLE_RES, new int[0]);
          TooltipDrawable tooltipDrawable = BaseSlider.parseLabelDrawable(BaseSlider.this.getContext(), typedArray);
          typedArray.recycle();
          return tooltipDrawable;
        }
      };
    processAttributes(paramContext, attrs, defStyleAttr);
    setFocusable(true);
    setClickable(true);
    materialShapeDrawable.setShadowCompatibilityMode(2);
    this.scaledTouchSlop = ViewConfiguration.get(paramContext).getScaledTouchSlop();
    AccessibilityHelper accessibilityHelper = new AccessibilityHelper(this);
    this.accessibilityHelper = accessibilityHelper;
    ViewCompat.setAccessibilityDelegate(this, (AccessibilityDelegateCompat)accessibilityHelper);
    this.accessibilityManager = (AccessibilityManager)getContext().getSystemService("accessibility");
  }
  
  private void attachLabelToContentView(TooltipDrawable paramTooltipDrawable) {
    paramTooltipDrawable.setRelativeToView((View)ViewUtils.getContentView(this));
  }
  
  private Float calculateIncrementForKey(int paramInt) {
    float f;
    if (this.isLongPress) {
      f = calculateStepIncrement(20);
    } else {
      f = calculateStepIncrement();
    } 
    switch (paramInt) {
      default:
        return null;
      case 70:
      case 81:
        return Float.valueOf(f);
      case 69:
        return Float.valueOf(-f);
      case 22:
        if (isRtl())
          f = -f; 
        return Float.valueOf(f);
      case 21:
        break;
    } 
    if (!isRtl())
      f = -f; 
    return Float.valueOf(f);
  }
  
  private float calculateStepIncrement() {
    float f2 = this.stepSize;
    float f1 = f2;
    if (f2 == 0.0F)
      f1 = 1.0F; 
    return f1;
  }
  
  private float calculateStepIncrement(int paramInt) {
    float f1 = calculateStepIncrement();
    float f2 = (this.valueTo - this.valueFrom) / f1;
    return (f2 <= paramInt) ? f1 : (Math.round(f2 / paramInt) * f1);
  }
  
  private int calculateTop() {
    int j = this.trackTop;
    int k = this.labelBehavior;
    int i = 0;
    if (k == 1)
      i = ((TooltipDrawable)this.labels.get(0)).getIntrinsicHeight(); 
    return j + i;
  }
  
  private ValueAnimator createLabelAnimator(boolean paramBoolean) {
    long l;
    ValueAnimator valueAnimator1;
    TimeInterpolator timeInterpolator;
    float f2 = 0.0F;
    if (paramBoolean) {
      f1 = 0.0F;
    } else {
      f1 = 1.0F;
    } 
    if (paramBoolean) {
      valueAnimator1 = this.labelsOutAnimator;
    } else {
      valueAnimator1 = this.labelsInAnimator;
    } 
    float f3 = getAnimatorCurrentValueOrDefault(valueAnimator1, f1);
    float f1 = f2;
    if (paramBoolean)
      f1 = 1.0F; 
    ValueAnimator valueAnimator2 = ValueAnimator.ofFloat(new float[] { f3, f1 });
    if (paramBoolean) {
      l = 83L;
    } else {
      l = 117L;
    } 
    valueAnimator2.setDuration(l);
    if (paramBoolean) {
      timeInterpolator = AnimationUtils.DECELERATE_INTERPOLATOR;
    } else {
      timeInterpolator = AnimationUtils.FAST_OUT_LINEAR_IN_INTERPOLATOR;
    } 
    valueAnimator2.setInterpolator(timeInterpolator);
    valueAnimator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
          final BaseSlider this$0;
          
          public void onAnimationUpdate(ValueAnimator param1ValueAnimator) {
            float f = ((Float)param1ValueAnimator.getAnimatedValue()).floatValue();
            Iterator<TooltipDrawable> iterator = BaseSlider.this.labels.iterator();
            while (iterator.hasNext())
              ((TooltipDrawable)iterator.next()).setRevealFraction(f); 
            ViewCompat.postInvalidateOnAnimation(BaseSlider.this);
          }
        });
    return valueAnimator2;
  }
  
  private void createLabelPool() {
    if (this.labels.size() > this.values.size()) {
      List<TooltipDrawable> list = this.labels.subList(this.values.size(), this.labels.size());
      for (TooltipDrawable tooltipDrawable : list) {
        if (ViewCompat.isAttachedToWindow(this))
          detachLabelFromContentView(tooltipDrawable); 
      } 
      list.clear();
    } 
    while (this.labels.size() < this.values.size()) {
      TooltipDrawable tooltipDrawable = this.labelMaker.createTooltipDrawable();
      this.labels.add(tooltipDrawable);
      if (ViewCompat.isAttachedToWindow(this))
        attachLabelToContentView(tooltipDrawable); 
    } 
    int i = this.labels.size();
    boolean bool = true;
    if (i == 1)
      bool = false; 
    Iterator<TooltipDrawable> iterator = this.labels.iterator();
    while (iterator.hasNext())
      ((TooltipDrawable)iterator.next()).setStrokeWidth(bool); 
  }
  
  private void detachLabelFromContentView(TooltipDrawable paramTooltipDrawable) {
    ViewOverlayImpl viewOverlayImpl = ViewUtils.getContentViewOverlay(this);
    if (viewOverlayImpl != null) {
      viewOverlayImpl.remove((Drawable)paramTooltipDrawable);
      paramTooltipDrawable.detachView((View)ViewUtils.getContentView(this));
    } 
  }
  
  private float dimenToValue(float paramFloat) {
    if (paramFloat == 0.0F)
      return 0.0F; 
    paramFloat = (paramFloat - this.trackSidePadding) / this.trackWidth;
    float f = this.valueFrom;
    return paramFloat * (f - this.valueTo) + f;
  }
  
  private void dispatchOnChangedFromUser(int paramInt) {
    Iterator<L> iterator = this.changeListeners.iterator();
    while (iterator.hasNext())
      ((BaseOnChangeListener<BaseSlider>)iterator.next()).onValueChange(this, ((Float)this.values.get(paramInt)).floatValue(), true); 
    AccessibilityManager accessibilityManager = this.accessibilityManager;
    if (accessibilityManager != null && accessibilityManager.isEnabled())
      scheduleAccessibilityEventSender(paramInt); 
  }
  
  private void dispatchOnChangedProgramatically() {
    for (BaseOnChangeListener<BaseSlider> baseOnChangeListener : this.changeListeners) {
      Iterator<Float> iterator = this.values.iterator();
      while (iterator.hasNext())
        baseOnChangeListener.onValueChange(this, ((Float)iterator.next()).floatValue(), false); 
    } 
  }
  
  private void drawActiveTrack(Canvas paramCanvas, int paramInt1, int paramInt2) {
    float[] arrayOfFloat = getActiveRange();
    int i = this.trackSidePadding;
    float f1 = i;
    float f3 = arrayOfFloat[1];
    float f2 = paramInt1;
    paramCanvas.drawLine(i + arrayOfFloat[0] * paramInt1, paramInt2, f1 + f3 * f2, paramInt2, this.activeTrackPaint);
  }
  
  private void drawInactiveTrack(Canvas paramCanvas, int paramInt1, int paramInt2) {
    float[] arrayOfFloat = getActiveRange();
    int i = this.trackSidePadding;
    float f = i + arrayOfFloat[1] * paramInt1;
    if (f < (i + paramInt1))
      paramCanvas.drawLine(f, paramInt2, (i + paramInt1), paramInt2, this.inactiveTrackPaint); 
    i = this.trackSidePadding;
    f = i + arrayOfFloat[0] * paramInt1;
    if (f > i)
      paramCanvas.drawLine(i, paramInt2, f, paramInt2, this.inactiveTrackPaint); 
  }
  
  private void drawThumbs(Canvas paramCanvas, int paramInt1, int paramInt2) {
    if (!isEnabled())
      for (Float float_ : this.values)
        paramCanvas.drawCircle(this.trackSidePadding + normalizeValue(float_.floatValue()) * paramInt1, paramInt2, this.thumbRadius, this.thumbPaint);  
    for (Float float_ : this.values) {
      paramCanvas.save();
      int j = this.trackSidePadding;
      int k = (int)(normalizeValue(float_.floatValue()) * paramInt1);
      int i = this.thumbRadius;
      paramCanvas.translate((j + k - i), (paramInt2 - i));
      this.thumbDrawable.draw(paramCanvas);
      paramCanvas.restore();
    } 
  }
  
  private void ensureLabelsAdded() {
    if (this.labelBehavior == 2)
      return; 
    if (!this.labelsAreAnimatedIn) {
      this.labelsAreAnimatedIn = true;
      ValueAnimator valueAnimator = createLabelAnimator(true);
      this.labelsInAnimator = valueAnimator;
      this.labelsOutAnimator = null;
      valueAnimator.start();
    } 
    Iterator<TooltipDrawable> iterator = this.labels.iterator();
    for (byte b = 0; b < this.values.size() && iterator.hasNext(); b++) {
      if (b != this.focusedThumbIdx)
        setValueForLabel(iterator.next(), ((Float)this.values.get(b)).floatValue()); 
    } 
    if (iterator.hasNext()) {
      setValueForLabel(iterator.next(), ((Float)this.values.get(this.focusedThumbIdx)).floatValue());
      return;
    } 
    throw new IllegalStateException(String.format("Not enough labels(%d) to display all the values(%d)", new Object[] { Integer.valueOf(this.labels.size()), Integer.valueOf(this.values.size()) }));
  }
  
  private void ensureLabelsRemoved() {
    if (this.labelsAreAnimatedIn) {
      this.labelsAreAnimatedIn = false;
      ValueAnimator valueAnimator = createLabelAnimator(false);
      this.labelsOutAnimator = valueAnimator;
      this.labelsInAnimator = null;
      valueAnimator.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter() {
            final BaseSlider this$0;
            
            public void onAnimationEnd(Animator param1Animator) {
              super.onAnimationEnd(param1Animator);
              for (TooltipDrawable tooltipDrawable : BaseSlider.this.labels)
                ViewUtils.getContentViewOverlay(BaseSlider.this).remove((Drawable)tooltipDrawable); 
            }
          });
      this.labelsOutAnimator.start();
    } 
  }
  
  private void focusThumbOnFocusGained(int paramInt) {
    switch (paramInt) {
      default:
        return;
      case 66:
        moveFocusInAbsoluteDirection(-2147483648);
      case 17:
        moveFocusInAbsoluteDirection(2147483647);
      case 2:
        moveFocus(-2147483648);
      case 1:
        break;
    } 
    moveFocus(2147483647);
  }
  
  private String formatValue(float paramFloat) {
    String str;
    if (hasLabelFormatter())
      return this.formatter.getFormattedValue(paramFloat); 
    if ((int)paramFloat == paramFloat) {
      str = "%.0f";
    } else {
      str = "%.2f";
    } 
    return String.format(str, new Object[] { Float.valueOf(paramFloat) });
  }
  
  private float[] getActiveRange() {
    float[] arrayOfFloat;
    float f2 = ((Float)Collections.<Float>max(getValues())).floatValue();
    float f1 = ((Float)Collections.<Float>min(getValues())).floatValue();
    if (this.values.size() == 1)
      f1 = this.valueFrom; 
    f1 = normalizeValue(f1);
    f2 = normalizeValue(f2);
    if (isRtl()) {
      arrayOfFloat = new float[2];
      arrayOfFloat[0] = f2;
      arrayOfFloat[1] = f1;
    } else {
      arrayOfFloat = new float[2];
      arrayOfFloat[0] = f1;
      arrayOfFloat[1] = f2;
    } 
    return arrayOfFloat;
  }
  
  private static float getAnimatorCurrentValueOrDefault(ValueAnimator paramValueAnimator, float paramFloat) {
    if (paramValueAnimator != null && paramValueAnimator.isRunning()) {
      paramFloat = ((Float)paramValueAnimator.getAnimatedValue()).floatValue();
      paramValueAnimator.cancel();
      return paramFloat;
    } 
    return paramFloat;
  }
  
  private float getClampedValue(int paramInt, float paramFloat) {
    float f2 = this.stepSize;
    float f1 = 0.0F;
    if (f2 == 0.0F)
      f1 = getMinSeparation(); 
    if (this.separationUnit == 0)
      f1 = dimenToValue(f1); 
    f2 = f1;
    if (isRtl())
      f2 = -f1; 
    if (paramInt + 1 >= this.values.size()) {
      f1 = this.valueTo;
    } else {
      f1 = ((Float)this.values.get(paramInt + 1)).floatValue() - f2;
    } 
    if (paramInt - 1 < 0) {
      f2 = this.valueFrom;
    } else {
      f2 = ((Float)this.values.get(paramInt - 1)).floatValue() + f2;
    } 
    return MathUtils.clamp(paramFloat, f2, f1);
  }
  
  private int getColorForState(ColorStateList paramColorStateList) {
    return paramColorStateList.getColorForState(getDrawableState(), paramColorStateList.getDefaultColor());
  }
  
  private float getValueOfTouchPosition() {
    double d2 = snapPosition(this.touchPosition);
    double d1 = d2;
    if (isRtl())
      d1 = 1.0D - d2; 
    float f2 = this.valueTo;
    float f1 = this.valueFrom;
    return (float)((f2 - f1) * d1 + f1);
  }
  
  private float getValueOfTouchPositionAbsolute() {
    float f2 = this.touchPosition;
    float f1 = f2;
    if (isRtl())
      f1 = 1.0F - f2; 
    f2 = this.valueTo;
    float f3 = this.valueFrom;
    return (f2 - f3) * f1 + f3;
  }
  
  private void invalidateTrack() {
    this.inactiveTrackPaint.setStrokeWidth(this.trackHeight);
    this.activeTrackPaint.setStrokeWidth(this.trackHeight);
    this.inactiveTicksPaint.setStrokeWidth(this.trackHeight / 2.0F);
    this.activeTicksPaint.setStrokeWidth(this.trackHeight / 2.0F);
  }
  
  private boolean isInVerticalScrollingContainer() {
    ViewParent viewParent = getParent();
    while (true) {
      boolean bool1 = viewParent instanceof ViewGroup;
      boolean bool = false;
      if (bool1) {
        ViewGroup viewGroup = (ViewGroup)viewParent;
        if (viewGroup.canScrollVertically(1) || viewGroup.canScrollVertically(-1))
          bool = true; 
        if (bool && viewGroup.shouldDelayChildPressedState())
          return true; 
        viewParent = viewParent.getParent();
        continue;
      } 
      return false;
    } 
  }
  
  private void loadResources(Resources paramResources) {
    this.widgetHeight = paramResources.getDimensionPixelSize(R.dimen.mtrl_slider_widget_height);
    int i = paramResources.getDimensionPixelOffset(R.dimen.mtrl_slider_track_side_padding);
    this.minTrackSidePadding = i;
    this.trackSidePadding = i;
    this.defaultThumbRadius = paramResources.getDimensionPixelSize(R.dimen.mtrl_slider_thumb_radius);
    this.trackTop = paramResources.getDimensionPixelOffset(R.dimen.mtrl_slider_track_top);
    this.labelPadding = paramResources.getDimensionPixelSize(R.dimen.mtrl_slider_label_padding);
  }
  
  private void maybeCalculateTicksCoordinates() {
    if (this.stepSize <= 0.0F)
      return; 
    validateConfigurationIfDirty();
    int i = Math.min((int)((this.valueTo - this.valueFrom) / this.stepSize + 1.0F), this.trackWidth / this.trackHeight * 2 + 1);
    float[] arrayOfFloat = this.ticksCoordinates;
    if (arrayOfFloat == null || arrayOfFloat.length != i * 2)
      this.ticksCoordinates = new float[i * 2]; 
    float f = this.trackWidth / (i - 1);
    for (byte b = 0; b < i * 2; b += 2) {
      arrayOfFloat = this.ticksCoordinates;
      arrayOfFloat[b] = this.trackSidePadding + (b / 2) * f;
      arrayOfFloat[b + 1] = calculateTop();
    } 
  }
  
  private void maybeDrawHalo(Canvas paramCanvas, int paramInt1, int paramInt2) {
    if (shouldDrawCompatHalo()) {
      paramInt1 = (int)(this.trackSidePadding + normalizeValue(((Float)this.values.get(this.focusedThumbIdx)).floatValue()) * paramInt1);
      if (Build.VERSION.SDK_INT < 28) {
        int i = this.haloRadius;
        paramCanvas.clipRect((paramInt1 - i), (paramInt2 - i), (paramInt1 + i), (i + paramInt2), Region.Op.UNION);
      } 
      paramCanvas.drawCircle(paramInt1, paramInt2, this.haloRadius, this.haloPaint);
    } 
  }
  
  private void maybeDrawTicks(Canvas paramCanvas) {
    if (!this.tickVisible || this.stepSize <= 0.0F)
      return; 
    float[] arrayOfFloat = getActiveRange();
    int i = pivotIndex(this.ticksCoordinates, arrayOfFloat[0]);
    int j = pivotIndex(this.ticksCoordinates, arrayOfFloat[1]);
    paramCanvas.drawPoints(this.ticksCoordinates, 0, i * 2, this.inactiveTicksPaint);
    paramCanvas.drawPoints(this.ticksCoordinates, i * 2, j * 2 - i * 2, this.activeTicksPaint);
    arrayOfFloat = this.ticksCoordinates;
    paramCanvas.drawPoints(arrayOfFloat, j * 2, arrayOfFloat.length - j * 2, this.inactiveTicksPaint);
  }
  
  private void maybeIncreaseTrackSidePadding() {
    int i = Math.max(this.thumbRadius - this.defaultThumbRadius, 0);
    this.trackSidePadding = this.minTrackSidePadding + i;
    if (ViewCompat.isLaidOut(this))
      updateTrackWidth(getWidth()); 
  }
  
  private boolean moveFocus(int paramInt) {
    int i = this.focusedThumbIdx;
    paramInt = (int)MathUtils.clamp(i + paramInt, 0L, (this.values.size() - 1));
    this.focusedThumbIdx = paramInt;
    if (paramInt == i)
      return false; 
    if (this.activeThumbIdx != -1)
      this.activeThumbIdx = paramInt; 
    updateHaloHotspot();
    postInvalidate();
    return true;
  }
  
  private boolean moveFocusInAbsoluteDirection(int paramInt) {
    int i = paramInt;
    if (isRtl()) {
      if (paramInt == Integer.MIN_VALUE) {
        paramInt = Integer.MAX_VALUE;
      } else {
        paramInt = -paramInt;
      } 
      i = paramInt;
    } 
    return moveFocus(i);
  }
  
  private float normalizeValue(float paramFloat) {
    float f = this.valueFrom;
    paramFloat = (paramFloat - f) / (this.valueTo - f);
    return isRtl() ? (1.0F - paramFloat) : paramFloat;
  }
  
  private Boolean onKeyDownNoActiveThumb(int paramInt, KeyEvent paramKeyEvent) {
    Boolean bool = Boolean.valueOf(true);
    switch (paramInt) {
      default:
        return null;
      case 70:
      case 81:
        moveFocus(1);
        return bool;
      case 69:
        moveFocus(-1);
        return bool;
      case 61:
        return paramKeyEvent.hasNoModifiers() ? Boolean.valueOf(moveFocus(1)) : (paramKeyEvent.isShiftPressed() ? Boolean.valueOf(moveFocus(-1)) : Boolean.valueOf(false));
      case 23:
      case 66:
        this.activeThumbIdx = this.focusedThumbIdx;
        postInvalidate();
        return bool;
      case 22:
        moveFocusInAbsoluteDirection(1);
        return bool;
      case 21:
        break;
    } 
    moveFocusInAbsoluteDirection(-1);
    return bool;
  }
  
  private void onStartTrackingTouch() {
    Iterator<T> iterator = this.touchListeners.iterator();
    while (iterator.hasNext())
      ((BaseOnSliderTouchListener<BaseSlider>)iterator.next()).onStartTrackingTouch(this); 
  }
  
  private void onStopTrackingTouch() {
    Iterator<T> iterator = this.touchListeners.iterator();
    while (iterator.hasNext())
      ((BaseOnSliderTouchListener<BaseSlider>)iterator.next()).onStopTrackingTouch(this); 
  }
  
  private static TooltipDrawable parseLabelDrawable(Context paramContext, TypedArray paramTypedArray) {
    return TooltipDrawable.createFromAttributes(paramContext, null, 0, paramTypedArray.getResourceId(R.styleable.Slider_labelStyle, R.style.Widget_MaterialComponents_Tooltip));
  }
  
  private static int pivotIndex(float[] paramArrayOffloat, float paramFloat) {
    return Math.round((paramArrayOffloat.length / 2 - 1) * paramFloat);
  }
  
  private void processAttributes(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    ColorStateList colorStateList1;
    int i;
    TypedArray typedArray = ThemeEnforcement.obtainStyledAttributes(paramContext, paramAttributeSet, R.styleable.Slider, paramInt, DEF_STYLE_RES, new int[0]);
    this.valueFrom = typedArray.getFloat(R.styleable.Slider_android_valueFrom, 0.0F);
    this.valueTo = typedArray.getFloat(R.styleable.Slider_android_valueTo, 1.0F);
    setValues(new Float[] { Float.valueOf(this.valueFrom) });
    this.stepSize = typedArray.getFloat(R.styleable.Slider_android_stepSize, 0.0F);
    boolean bool = typedArray.hasValue(R.styleable.Slider_trackColor);
    if (bool) {
      paramInt = R.styleable.Slider_trackColor;
    } else {
      paramInt = R.styleable.Slider_trackColorInactive;
    } 
    if (bool) {
      i = R.styleable.Slider_trackColor;
    } else {
      i = R.styleable.Slider_trackColorActive;
    } 
    ColorStateList colorStateList2 = MaterialResources.getColorStateList(paramContext, typedArray, paramInt);
    if (colorStateList2 == null)
      colorStateList2 = AppCompatResources.getColorStateList(paramContext, R.color.material_slider_inactive_track_color); 
    setTrackInactiveTintList(colorStateList2);
    colorStateList2 = MaterialResources.getColorStateList(paramContext, typedArray, i);
    if (colorStateList2 == null)
      colorStateList2 = AppCompatResources.getColorStateList(paramContext, R.color.material_slider_active_track_color); 
    setTrackActiveTintList(colorStateList2);
    colorStateList2 = MaterialResources.getColorStateList(paramContext, typedArray, R.styleable.Slider_thumbColor);
    this.thumbDrawable.setFillColor(colorStateList2);
    if (typedArray.hasValue(R.styleable.Slider_thumbStrokeColor))
      setThumbStrokeColor(MaterialResources.getColorStateList(paramContext, typedArray, R.styleable.Slider_thumbStrokeColor)); 
    setThumbStrokeWidth(typedArray.getDimension(R.styleable.Slider_thumbStrokeWidth, 0.0F));
    colorStateList2 = MaterialResources.getColorStateList(paramContext, typedArray, R.styleable.Slider_haloColor);
    if (colorStateList2 == null)
      colorStateList2 = AppCompatResources.getColorStateList(paramContext, R.color.material_slider_halo_color); 
    setHaloTintList(colorStateList2);
    this.tickVisible = typedArray.getBoolean(R.styleable.Slider_tickVisible, true);
    bool = typedArray.hasValue(R.styleable.Slider_tickColor);
    if (bool) {
      paramInt = R.styleable.Slider_tickColor;
    } else {
      paramInt = R.styleable.Slider_tickColorInactive;
    } 
    if (bool) {
      i = R.styleable.Slider_tickColor;
    } else {
      i = R.styleable.Slider_tickColorActive;
    } 
    colorStateList2 = MaterialResources.getColorStateList(paramContext, typedArray, paramInt);
    if (colorStateList2 == null)
      colorStateList2 = AppCompatResources.getColorStateList(paramContext, R.color.material_slider_inactive_tick_marks_color); 
    setTickInactiveTintList(colorStateList2);
    colorStateList2 = MaterialResources.getColorStateList(paramContext, typedArray, i);
    if (colorStateList2 != null) {
      colorStateList1 = colorStateList2;
    } else {
      colorStateList1 = AppCompatResources.getColorStateList((Context)colorStateList1, R.color.material_slider_active_tick_marks_color);
    } 
    setTickActiveTintList(colorStateList1);
    setThumbRadius(typedArray.getDimensionPixelSize(R.styleable.Slider_thumbRadius, 0));
    setHaloRadius(typedArray.getDimensionPixelSize(R.styleable.Slider_haloRadius, 0));
    setThumbElevation(typedArray.getDimension(R.styleable.Slider_thumbElevation, 0.0F));
    setTrackHeight(typedArray.getDimensionPixelSize(R.styleable.Slider_trackHeight, 0));
    this.labelBehavior = typedArray.getInt(R.styleable.Slider_labelBehavior, 0);
    if (!typedArray.getBoolean(R.styleable.Slider_android_enabled, true))
      setEnabled(false); 
    typedArray.recycle();
  }
  
  private void scheduleAccessibilityEventSender(int paramInt) {
    AccessibilityEventSender accessibilityEventSender = this.accessibilityEventSender;
    if (accessibilityEventSender == null) {
      this.accessibilityEventSender = new AccessibilityEventSender();
    } else {
      removeCallbacks(accessibilityEventSender);
    } 
    this.accessibilityEventSender.setVirtualViewId(paramInt);
    postDelayed(this.accessibilityEventSender, 200L);
  }
  
  private void setValueForLabel(TooltipDrawable paramTooltipDrawable, float paramFloat) {
    paramTooltipDrawable.setText(formatValue(paramFloat));
    int i = this.trackSidePadding + (int)(normalizeValue(paramFloat) * this.trackWidth) - paramTooltipDrawable.getIntrinsicWidth() / 2;
    int j = calculateTop() - this.labelPadding + this.thumbRadius;
    paramTooltipDrawable.setBounds(i, j - paramTooltipDrawable.getIntrinsicHeight(), paramTooltipDrawable.getIntrinsicWidth() + i, j);
    Rect rect = new Rect(paramTooltipDrawable.getBounds());
    DescendantOffsetUtils.offsetDescendantRect(ViewUtils.getContentView(this), this, rect);
    paramTooltipDrawable.setBounds(rect);
    ViewUtils.getContentViewOverlay(this).add((Drawable)paramTooltipDrawable);
  }
  
  private void setValuesInternal(ArrayList<Float> paramArrayList) {
    if (!paramArrayList.isEmpty()) {
      Collections.sort(paramArrayList);
      if (this.values.size() == paramArrayList.size() && this.values.equals(paramArrayList))
        return; 
      this.values = paramArrayList;
      this.dirtyConfig = true;
      this.focusedThumbIdx = 0;
      updateHaloHotspot();
      createLabelPool();
      dispatchOnChangedProgramatically();
      postInvalidate();
      return;
    } 
    throw new IllegalArgumentException("At least one value must be set");
  }
  
  private boolean shouldDrawCompatHalo() {
    return (this.forceDrawCompatHalo || Build.VERSION.SDK_INT < 21 || !(getBackground() instanceof RippleDrawable));
  }
  
  private boolean snapActiveThumbToValue(float paramFloat) {
    return snapThumbToValue(this.activeThumbIdx, paramFloat);
  }
  
  private double snapPosition(float paramFloat) {
    float f = this.stepSize;
    if (f > 0.0F) {
      int i = (int)((this.valueTo - this.valueFrom) / f);
      return Math.round(i * paramFloat) / i;
    } 
    return paramFloat;
  }
  
  private boolean snapThumbToValue(int paramInt, float paramFloat) {
    if (Math.abs(paramFloat - ((Float)this.values.get(paramInt)).floatValue()) < 1.0E-4D)
      return false; 
    paramFloat = getClampedValue(paramInt, paramFloat);
    this.values.set(paramInt, Float.valueOf(paramFloat));
    this.focusedThumbIdx = paramInt;
    dispatchOnChangedFromUser(paramInt);
    return true;
  }
  
  private boolean snapTouchPosition() {
    return snapActiveThumbToValue(getValueOfTouchPosition());
  }
  
  private void updateHaloHotspot() {
    if (!shouldDrawCompatHalo() && getMeasuredWidth() > 0) {
      Drawable drawable = getBackground();
      if (drawable instanceof RippleDrawable) {
        int i = (int)(normalizeValue(((Float)this.values.get(this.focusedThumbIdx)).floatValue()) * this.trackWidth + this.trackSidePadding);
        int k = calculateTop();
        int j = this.haloRadius;
        DrawableCompat.setHotspotBounds(drawable, i - j, k - j, i + j, j + k);
      } 
    } 
  }
  
  private void updateTrackWidth(int paramInt) {
    this.trackWidth = Math.max(paramInt - this.trackSidePadding * 2, 0);
    maybeCalculateTicksCoordinates();
  }
  
  private void validateConfigurationIfDirty() {
    if (this.dirtyConfig) {
      validateValueFrom();
      validateValueTo();
      validateStepSize();
      validateValues();
      warnAboutFloatingPointError();
      this.dirtyConfig = false;
    } 
  }
  
  private void validateStepSize() {
    if (this.stepSize <= 0.0F || valueLandsOnTick(this.valueTo))
      return; 
    throw new IllegalStateException(String.format("The stepSize(%s) must be 0, or a factor of the valueFrom(%s)-valueTo(%s) range", new Object[] { Float.toString(this.stepSize), Float.toString(this.valueFrom), Float.toString(this.valueTo) }));
  }
  
  private void validateValueFrom() {
    if (this.valueFrom < this.valueTo)
      return; 
    throw new IllegalStateException(String.format("valueFrom(%s) must be smaller than valueTo(%s)", new Object[] { Float.toString(this.valueFrom), Float.toString(this.valueTo) }));
  }
  
  private void validateValueTo() {
    if (this.valueTo > this.valueFrom)
      return; 
    throw new IllegalStateException(String.format("valueTo(%s) must be greater than valueFrom(%s)", new Object[] { Float.toString(this.valueTo), Float.toString(this.valueFrom) }));
  }
  
  private void validateValues() {
    for (Float float_ : this.values) {
      if (float_.floatValue() >= this.valueFrom && float_.floatValue() <= this.valueTo) {
        if (this.stepSize <= 0.0F || valueLandsOnTick(float_.floatValue()))
          continue; 
        throw new IllegalStateException(String.format("Value(%s) must be equal to valueFrom(%s) plus a multiple of stepSize(%s) when using stepSize(%s)", new Object[] { Float.toString(float_.floatValue()), Float.toString(this.valueFrom), Float.toString(this.stepSize), Float.toString(this.stepSize) }));
      } 
      throw new IllegalStateException(String.format("Slider value(%s) must be greater or equal to valueFrom(%s), and lower or equal to valueTo(%s)", new Object[] { Float.toString(float_.floatValue()), Float.toString(this.valueFrom), Float.toString(this.valueTo) }));
    } 
  }
  
  private boolean valueLandsOnTick(float paramFloat) {
    boolean bool;
    double d = (new BigDecimal(Float.toString(paramFloat))).subtract(new BigDecimal(Float.toString(this.valueFrom))).divide(new BigDecimal(Float.toString(this.stepSize)), MathContext.DECIMAL64).doubleValue();
    if (Math.abs(Math.round(d) - d) < 1.0E-4D) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private float valueToX(float paramFloat) {
    return normalizeValue(paramFloat) * this.trackWidth + this.trackSidePadding;
  }
  
  private void warnAboutFloatingPointError() {
    float f = this.stepSize;
    if (f == 0.0F)
      return; 
    if ((int)f != f)
      Log.w(TAG, String.format("Floating point value used for %s(%s). Using floats can have rounding errors which may result in incorrect values. Instead, consider using integers with a custom LabelFormatter to display the  value correctly.", new Object[] { "stepSize", Float.valueOf(f) })); 
    f = this.valueFrom;
    if ((int)f != f)
      Log.w(TAG, String.format("Floating point value used for %s(%s). Using floats can have rounding errors which may result in incorrect values. Instead, consider using integers with a custom LabelFormatter to display the  value correctly.", new Object[] { "valueFrom", Float.valueOf(f) })); 
    f = this.valueTo;
    if ((int)f != f)
      Log.w(TAG, String.format("Floating point value used for %s(%s). Using floats can have rounding errors which may result in incorrect values. Instead, consider using integers with a custom LabelFormatter to display the  value correctly.", new Object[] { "valueTo", Float.valueOf(f) })); 
  }
  
  public void addOnChangeListener(L paramL) {
    this.changeListeners.add(paramL);
  }
  
  public void addOnSliderTouchListener(T paramT) {
    this.touchListeners.add(paramT);
  }
  
  public void clearOnChangeListeners() {
    this.changeListeners.clear();
  }
  
  public void clearOnSliderTouchListeners() {
    this.touchListeners.clear();
  }
  
  public boolean dispatchHoverEvent(MotionEvent paramMotionEvent) {
    return (this.accessibilityHelper.dispatchHoverEvent(paramMotionEvent) || super.dispatchHoverEvent(paramMotionEvent));
  }
  
  public boolean dispatchKeyEvent(KeyEvent paramKeyEvent) {
    return super.dispatchKeyEvent(paramKeyEvent);
  }
  
  protected void drawableStateChanged() {
    super.drawableStateChanged();
    this.inactiveTrackPaint.setColor(getColorForState(this.trackColorInactive));
    this.activeTrackPaint.setColor(getColorForState(this.trackColorActive));
    this.inactiveTicksPaint.setColor(getColorForState(this.tickColorInactive));
    this.activeTicksPaint.setColor(getColorForState(this.tickColorActive));
    for (TooltipDrawable tooltipDrawable : this.labels) {
      if (tooltipDrawable.isStateful())
        tooltipDrawable.setState(getDrawableState()); 
    } 
    if (this.thumbDrawable.isStateful())
      this.thumbDrawable.setState(getDrawableState()); 
    this.haloPaint.setColor(getColorForState(this.haloColor));
    this.haloPaint.setAlpha(63);
  }
  
  void forceDrawCompatHalo(boolean paramBoolean) {
    this.forceDrawCompatHalo = paramBoolean;
  }
  
  public CharSequence getAccessibilityClassName() {
    return SeekBar.class.getName();
  }
  
  final int getAccessibilityFocusedVirtualViewId() {
    return this.accessibilityHelper.getAccessibilityFocusedVirtualViewId();
  }
  
  public int getActiveThumbIndex() {
    return this.activeThumbIdx;
  }
  
  public int getFocusedThumbIndex() {
    return this.focusedThumbIdx;
  }
  
  public int getHaloRadius() {
    return this.haloRadius;
  }
  
  public ColorStateList getHaloTintList() {
    return this.haloColor;
  }
  
  public int getLabelBehavior() {
    return this.labelBehavior;
  }
  
  protected float getMinSeparation() {
    return 0.0F;
  }
  
  public float getStepSize() {
    return this.stepSize;
  }
  
  public float getThumbElevation() {
    return this.thumbDrawable.getElevation();
  }
  
  public int getThumbRadius() {
    return this.thumbRadius;
  }
  
  public ColorStateList getThumbStrokeColor() {
    return this.thumbDrawable.getStrokeColor();
  }
  
  public float getThumbStrokeWidth() {
    return this.thumbDrawable.getStrokeWidth();
  }
  
  public ColorStateList getThumbTintList() {
    return this.thumbDrawable.getFillColor();
  }
  
  public ColorStateList getTickActiveTintList() {
    return this.tickColorActive;
  }
  
  public ColorStateList getTickInactiveTintList() {
    return this.tickColorInactive;
  }
  
  public ColorStateList getTickTintList() {
    if (this.tickColorInactive.equals(this.tickColorActive))
      return this.tickColorActive; 
    throw new IllegalStateException("The inactive and active ticks are different colors. Use the getTickColorInactive() and getTickColorActive() methods instead.");
  }
  
  public ColorStateList getTrackActiveTintList() {
    return this.trackColorActive;
  }
  
  public int getTrackHeight() {
    return this.trackHeight;
  }
  
  public ColorStateList getTrackInactiveTintList() {
    return this.trackColorInactive;
  }
  
  public int getTrackSidePadding() {
    return this.trackSidePadding;
  }
  
  public ColorStateList getTrackTintList() {
    if (this.trackColorInactive.equals(this.trackColorActive))
      return this.trackColorActive; 
    throw new IllegalStateException("The inactive and active parts of the track are different colors. Use the getInactiveTrackColor() and getActiveTrackColor() methods instead.");
  }
  
  public int getTrackWidth() {
    return this.trackWidth;
  }
  
  public float getValueFrom() {
    return this.valueFrom;
  }
  
  public float getValueTo() {
    return this.valueTo;
  }
  
  List<Float> getValues() {
    return new ArrayList<>(this.values);
  }
  
  public boolean hasLabelFormatter() {
    boolean bool;
    if (this.formatter != null) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  final boolean isRtl() {
    int i = ViewCompat.getLayoutDirection(this);
    boolean bool = true;
    if (i != 1)
      bool = false; 
    return bool;
  }
  
  public boolean isTickVisible() {
    return this.tickVisible;
  }
  
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    Iterator<TooltipDrawable> iterator = this.labels.iterator();
    while (iterator.hasNext())
      attachLabelToContentView(iterator.next()); 
  }
  
  protected void onDetachedFromWindow() {
    AccessibilityEventSender accessibilityEventSender = this.accessibilityEventSender;
    if (accessibilityEventSender != null)
      removeCallbacks(accessibilityEventSender); 
    this.labelsAreAnimatedIn = false;
    Iterator<TooltipDrawable> iterator = this.labels.iterator();
    while (iterator.hasNext())
      detachLabelFromContentView(iterator.next()); 
    super.onDetachedFromWindow();
  }
  
  protected void onDraw(Canvas paramCanvas) {
    if (this.dirtyConfig) {
      validateConfigurationIfDirty();
      maybeCalculateTicksCoordinates();
    } 
    super.onDraw(paramCanvas);
    int i = calculateTop();
    drawInactiveTrack(paramCanvas, this.trackWidth, i);
    if (((Float)Collections.<Float>max(getValues())).floatValue() > this.valueFrom)
      drawActiveTrack(paramCanvas, this.trackWidth, i); 
    maybeDrawTicks(paramCanvas);
    if ((this.thumbIsPressed || isFocused()) && isEnabled()) {
      maybeDrawHalo(paramCanvas, this.trackWidth, i);
      if (this.activeThumbIdx != -1)
        ensureLabelsAdded(); 
    } 
    drawThumbs(paramCanvas, this.trackWidth, i);
  }
  
  protected void onFocusChanged(boolean paramBoolean, int paramInt, Rect paramRect) {
    super.onFocusChanged(paramBoolean, paramInt, paramRect);
    if (!paramBoolean) {
      this.activeThumbIdx = -1;
      ensureLabelsRemoved();
      this.accessibilityHelper.clearKeyboardFocusForVirtualView(this.focusedThumbIdx);
    } else {
      focusThumbOnFocusGained(paramInt);
      this.accessibilityHelper.requestKeyboardFocusForVirtualView(this.focusedThumbIdx);
    } 
  }
  
  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
    if (!isEnabled())
      return super.onKeyDown(paramInt, paramKeyEvent); 
    if (this.values.size() == 1)
      this.activeThumbIdx = 0; 
    if (this.activeThumbIdx == -1) {
      boolean bool;
      Boolean bool1 = onKeyDownNoActiveThumb(paramInt, paramKeyEvent);
      if (bool1 != null) {
        bool = bool1.booleanValue();
      } else {
        bool = super.onKeyDown(paramInt, paramKeyEvent);
      } 
      return bool;
    } 
    this.isLongPress |= paramKeyEvent.isLongPress();
    Float float_ = calculateIncrementForKey(paramInt);
    if (float_ != null) {
      if (snapActiveThumbToValue(((Float)this.values.get(this.activeThumbIdx)).floatValue() + float_.floatValue())) {
        updateHaloHotspot();
        postInvalidate();
      } 
      return true;
    } 
    switch (paramInt) {
      default:
        return super.onKeyDown(paramInt, paramKeyEvent);
      case 61:
        return paramKeyEvent.hasNoModifiers() ? moveFocus(1) : (paramKeyEvent.isShiftPressed() ? moveFocus(-1) : false);
      case 23:
      case 66:
        break;
    } 
    this.activeThumbIdx = -1;
    ensureLabelsRemoved();
    postInvalidate();
    return true;
  }
  
  public boolean onKeyUp(int paramInt, KeyEvent paramKeyEvent) {
    this.isLongPress = false;
    return super.onKeyUp(paramInt, paramKeyEvent);
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    int i = this.widgetHeight;
    int j = this.labelBehavior;
    paramInt2 = 0;
    if (j == 1)
      paramInt2 = ((TooltipDrawable)this.labels.get(0)).getIntrinsicHeight(); 
    super.onMeasure(paramInt1, View.MeasureSpec.makeMeasureSpec(i + paramInt2, 1073741824));
  }
  
  protected void onRestoreInstanceState(Parcelable paramParcelable) {
    SliderState sliderState = (SliderState)paramParcelable;
    super.onRestoreInstanceState(sliderState.getSuperState());
    this.valueFrom = sliderState.valueFrom;
    this.valueTo = sliderState.valueTo;
    setValuesInternal(sliderState.values);
    this.stepSize = sliderState.stepSize;
    if (sliderState.hasFocus)
      requestFocus(); 
    dispatchOnChangedProgramatically();
  }
  
  protected Parcelable onSaveInstanceState() {
    SliderState sliderState = new SliderState(super.onSaveInstanceState());
    sliderState.valueFrom = this.valueFrom;
    sliderState.valueTo = this.valueTo;
    sliderState.values = new ArrayList<>(this.values);
    sliderState.stepSize = this.stepSize;
    sliderState.hasFocus = hasFocus();
    return (Parcelable)sliderState;
  }
  
  protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    updateTrackWidth(paramInt1);
    updateHaloHotspot();
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    MotionEvent motionEvent;
    if (!isEnabled())
      return false; 
    float f1 = paramMotionEvent.getX();
    float f2 = (f1 - this.trackSidePadding) / this.trackWidth;
    this.touchPosition = f2;
    f2 = Math.max(0.0F, f2);
    this.touchPosition = f2;
    this.touchPosition = Math.min(1.0F, f2);
    switch (paramMotionEvent.getActionMasked()) {
      default:
        setPressed(this.thumbIsPressed);
        this.lastEvent = MotionEvent.obtain(paramMotionEvent);
        return true;
      case 2:
        if (!this.thumbIsPressed) {
          if (isInVerticalScrollingContainer() && Math.abs(f1 - this.touchDownX) < this.scaledTouchSlop)
            return false; 
          getParent().requestDisallowInterceptTouchEvent(true);
          onStartTrackingTouch();
        } 
        if (pickActiveThumb()) {
          this.thumbIsPressed = true;
          snapTouchPosition();
          updateHaloHotspot();
          invalidate();
        } 
      case 1:
        this.thumbIsPressed = false;
        motionEvent = this.lastEvent;
        if (motionEvent != null && motionEvent.getActionMasked() == 0 && Math.abs(this.lastEvent.getX() - paramMotionEvent.getX()) <= this.scaledTouchSlop && Math.abs(this.lastEvent.getY() - paramMotionEvent.getY()) <= this.scaledTouchSlop && pickActiveThumb())
          onStartTrackingTouch(); 
        if (this.activeThumbIdx != -1) {
          snapTouchPosition();
          this.activeThumbIdx = -1;
          onStopTrackingTouch();
        } 
        ensureLabelsRemoved();
        invalidate();
      case 0:
        break;
    } 
    this.touchDownX = f1;
    if (isInVerticalScrollingContainer());
    getParent().requestDisallowInterceptTouchEvent(true);
    if (!pickActiveThumb());
    requestFocus();
    this.thumbIsPressed = true;
    snapTouchPosition();
    updateHaloHotspot();
    invalidate();
    onStartTrackingTouch();
  }
  
  protected boolean pickActiveThumb() {
    int i = this.activeThumbIdx;
    boolean bool = true;
    if (i != -1)
      return true; 
    float f2 = getValueOfTouchPositionAbsolute();
    float f3 = valueToX(f2);
    this.activeThumbIdx = 0;
    float f1 = Math.abs(((Float)this.values.get(0)).floatValue() - f2);
    i = 1;
    while (i < this.values.size()) {
      float f4;
      boolean bool1;
      float f5 = Math.abs(((Float)this.values.get(i)).floatValue() - f2);
      float f6 = valueToX(((Float)this.values.get(i)).floatValue());
      if (Float.compare(f5, f1) > 1)
        break; 
      if (isRtl() ? (f6 - f3 > 0.0F) : (f6 - f3 < 0.0F)) {
        bool1 = true;
      } else {
        bool1 = false;
      } 
      if (Float.compare(f5, f1) < 0) {
        f4 = f5;
        this.activeThumbIdx = i;
      } else {
        f4 = f1;
        if (Float.compare(f5, f1) == 0) {
          if (Math.abs(f6 - f3) < this.scaledTouchSlop) {
            this.activeThumbIdx = -1;
            return false;
          } 
          f4 = f1;
          if (bool1) {
            f4 = f5;
            this.activeThumbIdx = i;
          } 
        } 
      } 
      i++;
      f1 = f4;
    } 
    if (this.activeThumbIdx == -1)
      bool = false; 
    return bool;
  }
  
  public void removeOnChangeListener(L paramL) {
    this.changeListeners.remove(paramL);
  }
  
  public void removeOnSliderTouchListener(T paramT) {
    this.touchListeners.remove(paramT);
  }
  
  protected void setActiveThumbIndex(int paramInt) {
    this.activeThumbIdx = paramInt;
  }
  
  public void setEnabled(boolean paramBoolean) {
    byte b;
    super.setEnabled(paramBoolean);
    if (paramBoolean) {
      b = 0;
    } else {
      b = 2;
    } 
    setLayerType(b, null);
  }
  
  public void setFocusedThumbIndex(int paramInt) {
    if (paramInt >= 0 && paramInt < this.values.size()) {
      this.focusedThumbIdx = paramInt;
      this.accessibilityHelper.requestKeyboardFocusForVirtualView(paramInt);
      postInvalidate();
      return;
    } 
    throw new IllegalArgumentException("index out of range");
  }
  
  public void setHaloRadius(int paramInt) {
    if (paramInt == this.haloRadius)
      return; 
    this.haloRadius = paramInt;
    Drawable drawable = getBackground();
    if (!shouldDrawCompatHalo() && drawable instanceof RippleDrawable) {
      DrawableUtils.setRippleDrawableRadius((RippleDrawable)drawable, this.haloRadius);
      return;
    } 
    postInvalidate();
  }
  
  public void setHaloRadiusResource(int paramInt) {
    setHaloRadius(getResources().getDimensionPixelSize(paramInt));
  }
  
  public void setHaloTintList(ColorStateList paramColorStateList) {
    if (paramColorStateList.equals(this.haloColor))
      return; 
    this.haloColor = paramColorStateList;
    Drawable drawable = getBackground();
    if (!shouldDrawCompatHalo() && drawable instanceof RippleDrawable) {
      ((RippleDrawable)drawable).setColor(paramColorStateList);
      return;
    } 
    this.haloPaint.setColor(getColorForState(paramColorStateList));
    this.haloPaint.setAlpha(63);
    invalidate();
  }
  
  public void setLabelBehavior(int paramInt) {
    if (this.labelBehavior != paramInt) {
      this.labelBehavior = paramInt;
      requestLayout();
    } 
  }
  
  public void setLabelFormatter(LabelFormatter paramLabelFormatter) {
    this.formatter = paramLabelFormatter;
  }
  
  protected void setSeparationUnit(int paramInt) {
    this.separationUnit = paramInt;
  }
  
  public void setStepSize(float paramFloat) {
    if (paramFloat >= 0.0F) {
      if (this.stepSize != paramFloat) {
        this.stepSize = paramFloat;
        this.dirtyConfig = true;
        postInvalidate();
      } 
      return;
    } 
    throw new IllegalArgumentException(String.format("The stepSize(%s) must be 0, or a factor of the valueFrom(%s)-valueTo(%s) range", new Object[] { Float.toString(paramFloat), Float.toString(this.valueFrom), Float.toString(this.valueTo) }));
  }
  
  public void setThumbElevation(float paramFloat) {
    this.thumbDrawable.setElevation(paramFloat);
  }
  
  public void setThumbElevationResource(int paramInt) {
    setThumbElevation(getResources().getDimension(paramInt));
  }
  
  public void setThumbRadius(int paramInt) {
    if (paramInt == this.thumbRadius)
      return; 
    this.thumbRadius = paramInt;
    maybeIncreaseTrackSidePadding();
    this.thumbDrawable.setShapeAppearanceModel(ShapeAppearanceModel.builder().setAllCorners(0, this.thumbRadius).build());
    MaterialShapeDrawable materialShapeDrawable = this.thumbDrawable;
    paramInt = this.thumbRadius;
    materialShapeDrawable.setBounds(0, 0, paramInt * 2, paramInt * 2);
    postInvalidate();
  }
  
  public void setThumbRadiusResource(int paramInt) {
    setThumbRadius(getResources().getDimensionPixelSize(paramInt));
  }
  
  public void setThumbStrokeColor(ColorStateList paramColorStateList) {
    this.thumbDrawable.setStrokeColor(paramColorStateList);
    postInvalidate();
  }
  
  public void setThumbStrokeColorResource(int paramInt) {
    if (paramInt != 0)
      setThumbStrokeColor(AppCompatResources.getColorStateList(getContext(), paramInt)); 
  }
  
  public void setThumbStrokeWidth(float paramFloat) {
    this.thumbDrawable.setStrokeWidth(paramFloat);
    postInvalidate();
  }
  
  public void setThumbStrokeWidthResource(int paramInt) {
    if (paramInt != 0)
      setThumbStrokeWidth(getResources().getDimension(paramInt)); 
  }
  
  public void setThumbTintList(ColorStateList paramColorStateList) {
    if (paramColorStateList.equals(this.thumbDrawable.getFillColor()))
      return; 
    this.thumbDrawable.setFillColor(paramColorStateList);
    invalidate();
  }
  
  public void setTickActiveTintList(ColorStateList paramColorStateList) {
    if (paramColorStateList.equals(this.tickColorActive))
      return; 
    this.tickColorActive = paramColorStateList;
    this.activeTicksPaint.setColor(getColorForState(paramColorStateList));
    invalidate();
  }
  
  public void setTickInactiveTintList(ColorStateList paramColorStateList) {
    if (paramColorStateList.equals(this.tickColorInactive))
      return; 
    this.tickColorInactive = paramColorStateList;
    this.inactiveTicksPaint.setColor(getColorForState(paramColorStateList));
    invalidate();
  }
  
  public void setTickTintList(ColorStateList paramColorStateList) {
    setTickInactiveTintList(paramColorStateList);
    setTickActiveTintList(paramColorStateList);
  }
  
  public void setTickVisible(boolean paramBoolean) {
    if (this.tickVisible != paramBoolean) {
      this.tickVisible = paramBoolean;
      postInvalidate();
    } 
  }
  
  public void setTrackActiveTintList(ColorStateList paramColorStateList) {
    if (paramColorStateList.equals(this.trackColorActive))
      return; 
    this.trackColorActive = paramColorStateList;
    this.activeTrackPaint.setColor(getColorForState(paramColorStateList));
    invalidate();
  }
  
  public void setTrackHeight(int paramInt) {
    if (this.trackHeight != paramInt) {
      this.trackHeight = paramInt;
      invalidateTrack();
      postInvalidate();
    } 
  }
  
  public void setTrackInactiveTintList(ColorStateList paramColorStateList) {
    if (paramColorStateList.equals(this.trackColorInactive))
      return; 
    this.trackColorInactive = paramColorStateList;
    this.inactiveTrackPaint.setColor(getColorForState(paramColorStateList));
    invalidate();
  }
  
  public void setTrackTintList(ColorStateList paramColorStateList) {
    setTrackInactiveTintList(paramColorStateList);
    setTrackActiveTintList(paramColorStateList);
  }
  
  public void setValueFrom(float paramFloat) {
    this.valueFrom = paramFloat;
    this.dirtyConfig = true;
    postInvalidate();
  }
  
  public void setValueTo(float paramFloat) {
    this.valueTo = paramFloat;
    this.dirtyConfig = true;
    postInvalidate();
  }
  
  void setValues(List<Float> paramList) {
    setValuesInternal(new ArrayList<>(paramList));
  }
  
  void setValues(Float... paramVarArgs) {
    ArrayList<? super Float> arrayList = new ArrayList();
    Collections.addAll(arrayList, paramVarArgs);
    setValuesInternal((ArrayList)arrayList);
  }
  
  void updateBoundsForVirturalViewId(int paramInt, Rect paramRect) {
    int j = this.trackSidePadding + (int)(normalizeValue(((Float)getValues().get(paramInt)).floatValue()) * this.trackWidth);
    int i = calculateTop();
    paramInt = this.thumbRadius;
    paramRect.set(j - paramInt, i - paramInt, j + paramInt, paramInt + i);
  }
  
  private class AccessibilityEventSender implements Runnable {
    final BaseSlider this$0;
    
    int virtualViewId = -1;
    
    private AccessibilityEventSender() {}
    
    public void run() {
      BaseSlider.this.accessibilityHelper.sendEventForVirtualView(this.virtualViewId, 4);
    }
    
    void setVirtualViewId(int param1Int) {
      this.virtualViewId = param1Int;
    }
  }
  
  private static class AccessibilityHelper extends ExploreByTouchHelper {
    private final BaseSlider<?, ?, ?> slider;
    
    Rect virtualViewBounds = new Rect();
    
    AccessibilityHelper(BaseSlider<?, ?, ?> param1BaseSlider) {
      super(param1BaseSlider);
      this.slider = param1BaseSlider;
    }
    
    private String startOrEndDescription(int param1Int) {
      return (param1Int == this.slider.getValues().size() - 1) ? this.slider.getContext().getString(R.string.material_slider_range_end) : ((param1Int == 0) ? this.slider.getContext().getString(R.string.material_slider_range_start) : "");
    }
    
    protected int getVirtualViewAt(float param1Float1, float param1Float2) {
      for (byte b = 0; b < this.slider.getValues().size(); b++) {
        this.slider.updateBoundsForVirturalViewId(b, this.virtualViewBounds);
        if (this.virtualViewBounds.contains((int)param1Float1, (int)param1Float2))
          return b; 
      } 
      return -1;
    }
    
    protected void getVisibleVirtualViews(List<Integer> param1List) {
      for (byte b = 0; b < this.slider.getValues().size(); b++)
        param1List.add(Integer.valueOf(b)); 
    }
    
    protected boolean onPerformActionForVirtualView(int param1Int1, int param1Int2, Bundle param1Bundle) {
      if (!this.slider.isEnabled())
        return false; 
      switch (param1Int2) {
        default:
          return false;
        case 16908349:
          if (param1Bundle == null || !param1Bundle.containsKey("android.view.accessibility.action.ARGUMENT_PROGRESS_VALUE"))
            return false; 
          f1 = param1Bundle.getFloat("android.view.accessibility.action.ARGUMENT_PROGRESS_VALUE");
          if (this.slider.snapThumbToValue(param1Int1, f1)) {
            this.slider.updateHaloHotspot();
            this.slider.postInvalidate();
            invalidateVirtualView(param1Int1);
            return true;
          } 
          return false;
        case 4096:
        case 8192:
          break;
      } 
      float f2 = this.slider.calculateStepIncrement(20);
      float f1 = f2;
      if (param1Int2 == 8192)
        f1 = -f2; 
      f2 = f1;
      if (this.slider.isRtl())
        f2 = -f1; 
      List<Float> list = this.slider.getValues();
      f1 = MathUtils.clamp(((Float)list.get(param1Int1)).floatValue() + f2, this.slider.getValueFrom(), this.slider.getValueTo());
      if (this.slider.snapThumbToValue(param1Int1, f1)) {
        this.slider.updateHaloHotspot();
        this.slider.postInvalidate();
        invalidateVirtualView(param1Int1);
        return true;
      } 
      return false;
    }
    
    protected void onPopulateNodeForVirtualView(int param1Int, AccessibilityNodeInfoCompat param1AccessibilityNodeInfoCompat) {
      param1AccessibilityNodeInfoCompat.addAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_SET_PROGRESS);
      List<Float> list = this.slider.getValues();
      float f2 = ((Float)list.get(param1Int)).floatValue();
      float f3 = this.slider.getValueFrom();
      float f1 = this.slider.getValueTo();
      if (this.slider.isEnabled()) {
        if (f2 > f3)
          param1AccessibilityNodeInfoCompat.addAction(8192); 
        if (f2 < f1)
          param1AccessibilityNodeInfoCompat.addAction(4096); 
      } 
      param1AccessibilityNodeInfoCompat.setRangeInfo(AccessibilityNodeInfoCompat.RangeInfoCompat.obtain(1, f3, f1, f2));
      param1AccessibilityNodeInfoCompat.setClassName(SeekBar.class.getName());
      StringBuilder stringBuilder = new StringBuilder();
      if (this.slider.getContentDescription() != null)
        stringBuilder.append(this.slider.getContentDescription()).append(","); 
      if (list.size() > 1) {
        stringBuilder.append(startOrEndDescription(param1Int));
        stringBuilder.append(this.slider.formatValue(f2));
      } 
      param1AccessibilityNodeInfoCompat.setContentDescription(stringBuilder.toString());
      this.slider.updateBoundsForVirturalViewId(param1Int, this.virtualViewBounds);
      param1AccessibilityNodeInfoCompat.setBoundsInParent(this.virtualViewBounds);
    }
  }
  
  static class SliderState extends View.BaseSavedState {
    public static final Parcelable.Creator<SliderState> CREATOR = new Parcelable.Creator<SliderState>() {
        public BaseSlider.SliderState createFromParcel(Parcel param2Parcel) {
          return new BaseSlider.SliderState(param2Parcel);
        }
        
        public BaseSlider.SliderState[] newArray(int param2Int) {
          return new BaseSlider.SliderState[param2Int];
        }
      };
    
    boolean hasFocus;
    
    float stepSize;
    
    float valueFrom;
    
    float valueTo;
    
    ArrayList<Float> values;
    
    private SliderState(Parcel param1Parcel) {
      super(param1Parcel);
      this.valueFrom = param1Parcel.readFloat();
      this.valueTo = param1Parcel.readFloat();
      ArrayList<Float> arrayList = new ArrayList();
      this.values = arrayList;
      param1Parcel.readList(arrayList, Float.class.getClassLoader());
      this.stepSize = param1Parcel.readFloat();
      this.hasFocus = param1Parcel.createBooleanArray()[0];
    }
    
    SliderState(Parcelable param1Parcelable) {
      super(param1Parcelable);
    }
    
    public void writeToParcel(Parcel param1Parcel, int param1Int) {
      super.writeToParcel(param1Parcel, param1Int);
      param1Parcel.writeFloat(this.valueFrom);
      param1Parcel.writeFloat(this.valueTo);
      param1Parcel.writeList(this.values);
      param1Parcel.writeFloat(this.stepSize);
      param1Parcel.writeBooleanArray(new boolean[] { this.hasFocus });
    }
  }
  
  static final class null implements Parcelable.Creator<SliderState> {
    public BaseSlider.SliderState createFromParcel(Parcel param1Parcel) {
      return new BaseSlider.SliderState(param1Parcel);
    }
    
    public BaseSlider.SliderState[] newArray(int param1Int) {
      return new BaseSlider.SliderState[param1Int];
    }
  }
  
  private static interface TooltipDrawableFactory {
    TooltipDrawable createTooltipDrawable();
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\slider\BaseSlider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */