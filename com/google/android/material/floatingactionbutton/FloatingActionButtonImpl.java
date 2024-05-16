package com.google.android.material.floatingactionbutton;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.FloatEvaluator;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.res.ColorStateList;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.util.Property;
import android.view.View;
import android.view.ViewTreeObserver;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.util.Preconditions;
import androidx.core.view.ViewCompat;
import com.google.android.material.R;
import com.google.android.material.animation.AnimationUtils;
import com.google.android.material.animation.AnimatorSetCompat;
import com.google.android.material.animation.ImageMatrixProperty;
import com.google.android.material.animation.MatrixEvaluator;
import com.google.android.material.animation.MotionSpec;
import com.google.android.material.internal.StateListAnimator;
import com.google.android.material.ripple.RippleDrawableCompat;
import com.google.android.material.ripple.RippleUtils;
import com.google.android.material.shadow.ShadowViewDelegate;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.MaterialShapeUtils;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.android.material.shape.Shapeable;
import java.util.ArrayList;
import java.util.Iterator;

class FloatingActionButtonImpl {
  static final int ANIM_STATE_HIDING = 1;
  
  static final int ANIM_STATE_NONE = 0;
  
  static final int ANIM_STATE_SHOWING = 2;
  
  static final long ELEVATION_ANIM_DELAY = 100L;
  
  static final long ELEVATION_ANIM_DURATION = 100L;
  
  static final TimeInterpolator ELEVATION_ANIM_INTERPOLATOR = AnimationUtils.FAST_OUT_LINEAR_IN_INTERPOLATOR;
  
  static final int[] EMPTY_STATE_SET;
  
  static final int[] ENABLED_STATE_SET;
  
  static final int[] FOCUSED_ENABLED_STATE_SET;
  
  private static final float HIDE_ICON_SCALE = 0.0F;
  
  private static final float HIDE_OPACITY = 0.0F;
  
  private static final float HIDE_SCALE = 0.0F;
  
  static final int[] HOVERED_ENABLED_STATE_SET;
  
  static final int[] HOVERED_FOCUSED_ENABLED_STATE_SET;
  
  static final int[] PRESSED_ENABLED_STATE_SET = new int[] { 16842919, 16842910 };
  
  static final float SHADOW_MULTIPLIER = 1.5F;
  
  private static final float SHOW_ICON_SCALE = 1.0F;
  
  private static final float SHOW_OPACITY = 1.0F;
  
  private static final float SHOW_SCALE = 1.0F;
  
  private int animState = 0;
  
  BorderDrawable borderDrawable;
  
  Drawable contentBackground;
  
  private Animator currentAnimator;
  
  private MotionSpec defaultHideMotionSpec;
  
  private MotionSpec defaultShowMotionSpec;
  
  float elevation;
  
  boolean ensureMinTouchTargetSize;
  
  private ArrayList<Animator.AnimatorListener> hideListeners;
  
  private MotionSpec hideMotionSpec;
  
  float hoveredFocusedTranslationZ;
  
  private float imageMatrixScale = 1.0F;
  
  private int maxImageSize;
  
  int minTouchTargetSize;
  
  private ViewTreeObserver.OnPreDrawListener preDrawListener;
  
  float pressedTranslationZ;
  
  Drawable rippleDrawable;
  
  private float rotation;
  
  boolean shadowPaddingEnabled = true;
  
  final ShadowViewDelegate shadowViewDelegate;
  
  ShapeAppearanceModel shapeAppearance;
  
  MaterialShapeDrawable shapeDrawable;
  
  private ArrayList<Animator.AnimatorListener> showListeners;
  
  private MotionSpec showMotionSpec;
  
  private final StateListAnimator stateListAnimator;
  
  private final Matrix tmpMatrix = new Matrix();
  
  private final Rect tmpRect = new Rect();
  
  private final RectF tmpRectF1 = new RectF();
  
  private final RectF tmpRectF2 = new RectF();
  
  private ArrayList<InternalTransformationCallback> transformationCallbacks;
  
  final FloatingActionButton view;
  
  static {
    HOVERED_FOCUSED_ENABLED_STATE_SET = new int[] { 16843623, 16842908, 16842910 };
    FOCUSED_ENABLED_STATE_SET = new int[] { 16842908, 16842910 };
    HOVERED_ENABLED_STATE_SET = new int[] { 16843623, 16842910 };
    ENABLED_STATE_SET = new int[] { 16842910 };
    EMPTY_STATE_SET = new int[0];
  }
  
  FloatingActionButtonImpl(FloatingActionButton paramFloatingActionButton, ShadowViewDelegate paramShadowViewDelegate) {
    this.view = paramFloatingActionButton;
    this.shadowViewDelegate = paramShadowViewDelegate;
    StateListAnimator stateListAnimator = new StateListAnimator();
    this.stateListAnimator = stateListAnimator;
    stateListAnimator.addState(PRESSED_ENABLED_STATE_SET, createElevationAnimator(new ElevateToPressedTranslationZAnimation()));
    stateListAnimator.addState(HOVERED_FOCUSED_ENABLED_STATE_SET, createElevationAnimator(new ElevateToHoveredFocusedTranslationZAnimation()));
    stateListAnimator.addState(FOCUSED_ENABLED_STATE_SET, createElevationAnimator(new ElevateToHoveredFocusedTranslationZAnimation()));
    stateListAnimator.addState(HOVERED_ENABLED_STATE_SET, createElevationAnimator(new ElevateToHoveredFocusedTranslationZAnimation()));
    stateListAnimator.addState(ENABLED_STATE_SET, createElevationAnimator(new ResetElevationAnimation()));
    stateListAnimator.addState(EMPTY_STATE_SET, createElevationAnimator(new DisabledElevationAnimation()));
    this.rotation = paramFloatingActionButton.getRotation();
  }
  
  private void calculateImageMatrixFromScale(float paramFloat, Matrix paramMatrix) {
    paramMatrix.reset();
    Drawable drawable = this.view.getDrawable();
    if (drawable != null && this.maxImageSize != 0) {
      RectF rectF2 = this.tmpRectF1;
      RectF rectF1 = this.tmpRectF2;
      rectF2.set(0.0F, 0.0F, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
      int i = this.maxImageSize;
      rectF1.set(0.0F, 0.0F, i, i);
      paramMatrix.setRectToRect(rectF2, rectF1, Matrix.ScaleToFit.CENTER);
      i = this.maxImageSize;
      paramMatrix.postScale(paramFloat, paramFloat, i / 2.0F, i / 2.0F);
    } 
  }
  
  private AnimatorSet createAnimator(MotionSpec paramMotionSpec, float paramFloat1, float paramFloat2, float paramFloat3) {
    ArrayList<ObjectAnimator> arrayList = new ArrayList();
    ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this.view, View.ALPHA, new float[] { paramFloat1 });
    paramMotionSpec.getTiming("opacity").apply((Animator)objectAnimator);
    arrayList.add(objectAnimator);
    objectAnimator = ObjectAnimator.ofFloat(this.view, View.SCALE_X, new float[] { paramFloat2 });
    paramMotionSpec.getTiming("scale").apply((Animator)objectAnimator);
    workAroundOreoBug(objectAnimator);
    arrayList.add(objectAnimator);
    objectAnimator = ObjectAnimator.ofFloat(this.view, View.SCALE_Y, new float[] { paramFloat2 });
    paramMotionSpec.getTiming("scale").apply((Animator)objectAnimator);
    workAroundOreoBug(objectAnimator);
    arrayList.add(objectAnimator);
    calculateImageMatrixFromScale(paramFloat3, this.tmpMatrix);
    objectAnimator = ObjectAnimator.ofObject(this.view, (Property)new ImageMatrixProperty(), (TypeEvaluator)new MatrixEvaluator() {
          final FloatingActionButtonImpl this$0;
          
          public Matrix evaluate(float param1Float, Matrix param1Matrix1, Matrix param1Matrix2) {
            FloatingActionButtonImpl.access$202(FloatingActionButtonImpl.this, param1Float);
            return super.evaluate(param1Float, param1Matrix1, param1Matrix2);
          }
        }(Object[])new Matrix[] { new Matrix(this.tmpMatrix) });
    paramMotionSpec.getTiming("iconScale").apply((Animator)objectAnimator);
    arrayList.add(objectAnimator);
    AnimatorSet animatorSet = new AnimatorSet();
    AnimatorSetCompat.playTogether(animatorSet, arrayList);
    return animatorSet;
  }
  
  private ValueAnimator createElevationAnimator(ShadowAnimatorImpl paramShadowAnimatorImpl) {
    ValueAnimator valueAnimator = new ValueAnimator();
    valueAnimator.setInterpolator(ELEVATION_ANIM_INTERPOLATOR);
    valueAnimator.setDuration(100L);
    valueAnimator.addListener((Animator.AnimatorListener)paramShadowAnimatorImpl);
    valueAnimator.addUpdateListener(paramShadowAnimatorImpl);
    valueAnimator.setFloatValues(new float[] { 0.0F, 1.0F });
    return valueAnimator;
  }
  
  private MotionSpec getDefaultHideMotionSpec() {
    if (this.defaultHideMotionSpec == null)
      this.defaultHideMotionSpec = MotionSpec.createFromResource(this.view.getContext(), R.animator.design_fab_hide_motion_spec); 
    return (MotionSpec)Preconditions.checkNotNull(this.defaultHideMotionSpec);
  }
  
  private MotionSpec getDefaultShowMotionSpec() {
    if (this.defaultShowMotionSpec == null)
      this.defaultShowMotionSpec = MotionSpec.createFromResource(this.view.getContext(), R.animator.design_fab_show_motion_spec); 
    return (MotionSpec)Preconditions.checkNotNull(this.defaultShowMotionSpec);
  }
  
  private ViewTreeObserver.OnPreDrawListener getOrCreatePreDrawListener() {
    if (this.preDrawListener == null)
      this.preDrawListener = new ViewTreeObserver.OnPreDrawListener() {
          final FloatingActionButtonImpl this$0;
          
          public boolean onPreDraw() {
            FloatingActionButtonImpl.this.onPreDraw();
            return true;
          }
        }; 
    return this.preDrawListener;
  }
  
  private boolean shouldAnimateVisibilityChange() {
    boolean bool;
    if (ViewCompat.isLaidOut((View)this.view) && !this.view.isInEditMode()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private void workAroundOreoBug(ObjectAnimator paramObjectAnimator) {
    if (Build.VERSION.SDK_INT != 26)
      return; 
    paramObjectAnimator.setEvaluator(new TypeEvaluator<Float>() {
          FloatEvaluator floatEvaluator = new FloatEvaluator();
          
          final FloatingActionButtonImpl this$0;
          
          public Float evaluate(float param1Float, Float param1Float1, Float param1Float2) {
            param1Float = this.floatEvaluator.evaluate(param1Float, param1Float1, param1Float2).floatValue();
            if (param1Float < 0.1F)
              param1Float = 0.0F; 
            return Float.valueOf(param1Float);
          }
        });
  }
  
  public void addOnHideAnimationListener(Animator.AnimatorListener paramAnimatorListener) {
    if (this.hideListeners == null)
      this.hideListeners = new ArrayList<>(); 
    this.hideListeners.add(paramAnimatorListener);
  }
  
  void addOnShowAnimationListener(Animator.AnimatorListener paramAnimatorListener) {
    if (this.showListeners == null)
      this.showListeners = new ArrayList<>(); 
    this.showListeners.add(paramAnimatorListener);
  }
  
  void addTransformationCallback(InternalTransformationCallback paramInternalTransformationCallback) {
    if (this.transformationCallbacks == null)
      this.transformationCallbacks = new ArrayList<>(); 
    this.transformationCallbacks.add(paramInternalTransformationCallback);
  }
  
  MaterialShapeDrawable createShapeDrawable() {
    return new MaterialShapeDrawable((ShapeAppearanceModel)Preconditions.checkNotNull(this.shapeAppearance));
  }
  
  final Drawable getContentBackground() {
    return this.contentBackground;
  }
  
  float getElevation() {
    return this.elevation;
  }
  
  boolean getEnsureMinTouchTargetSize() {
    return this.ensureMinTouchTargetSize;
  }
  
  final MotionSpec getHideMotionSpec() {
    return this.hideMotionSpec;
  }
  
  float getHoveredFocusedTranslationZ() {
    return this.hoveredFocusedTranslationZ;
  }
  
  void getPadding(Rect paramRect) {
    float f;
    if (this.ensureMinTouchTargetSize) {
      i = (this.minTouchTargetSize - this.view.getSizeDimension()) / 2;
    } else {
      i = 0;
    } 
    if (this.shadowPaddingEnabled) {
      f = getElevation() + this.pressedTranslationZ;
    } else {
      f = 0.0F;
    } 
    int j = Math.max(i, (int)Math.ceil(f));
    int i = Math.max(i, (int)Math.ceil((1.5F * f)));
    paramRect.set(j, i, j, i);
  }
  
  float getPressedTranslationZ() {
    return this.pressedTranslationZ;
  }
  
  final ShapeAppearanceModel getShapeAppearance() {
    return this.shapeAppearance;
  }
  
  final MotionSpec getShowMotionSpec() {
    return this.showMotionSpec;
  }
  
  void hide(final InternalVisibilityChangedListener listener, final boolean fromUser) {
    Iterator<Animator.AnimatorListener> iterator;
    if (isOrWillBeHidden())
      return; 
    Animator animator = this.currentAnimator;
    if (animator != null)
      animator.cancel(); 
    if (shouldAnimateVisibilityChange()) {
      MotionSpec motionSpec = this.hideMotionSpec;
      if (motionSpec == null)
        motionSpec = getDefaultHideMotionSpec(); 
      AnimatorSet animatorSet = createAnimator(motionSpec, 0.0F, 0.0F, 0.0F);
      animatorSet.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter() {
            private boolean cancelled;
            
            final FloatingActionButtonImpl this$0;
            
            final boolean val$fromUser;
            
            final FloatingActionButtonImpl.InternalVisibilityChangedListener val$listener;
            
            public void onAnimationCancel(Animator param1Animator) {
              this.cancelled = true;
            }
            
            public void onAnimationEnd(Animator param1Animator) {
              FloatingActionButtonImpl.access$002(FloatingActionButtonImpl.this, 0);
              FloatingActionButtonImpl.access$102(FloatingActionButtonImpl.this, null);
              if (!this.cancelled) {
                byte b;
                FloatingActionButton floatingActionButton = FloatingActionButtonImpl.this.view;
                boolean bool = fromUser;
                if (bool) {
                  b = 8;
                } else {
                  b = 4;
                } 
                floatingActionButton.internalSetVisibility(b, bool);
                FloatingActionButtonImpl.InternalVisibilityChangedListener internalVisibilityChangedListener = listener;
                if (internalVisibilityChangedListener != null)
                  internalVisibilityChangedListener.onHidden(); 
              } 
            }
            
            public void onAnimationStart(Animator param1Animator) {
              FloatingActionButtonImpl.this.view.internalSetVisibility(0, fromUser);
              FloatingActionButtonImpl.access$002(FloatingActionButtonImpl.this, 1);
              FloatingActionButtonImpl.access$102(FloatingActionButtonImpl.this, param1Animator);
              this.cancelled = false;
            }
          });
      ArrayList<Animator.AnimatorListener> arrayList = this.hideListeners;
      if (arrayList != null) {
        iterator = arrayList.iterator();
        while (iterator.hasNext())
          animatorSet.addListener(iterator.next()); 
      } 
      animatorSet.start();
    } else {
      byte b;
      FloatingActionButton floatingActionButton = this.view;
      if (fromUser) {
        b = 8;
      } else {
        b = 4;
      } 
      floatingActionButton.internalSetVisibility(b, fromUser);
      if (iterator != null)
        iterator.onHidden(); 
    } 
  }
  
  void initializeBackgroundDrawable(ColorStateList paramColorStateList1, PorterDuff.Mode paramMode, ColorStateList paramColorStateList2, int paramInt) {
    MaterialShapeDrawable materialShapeDrawable = createShapeDrawable();
    this.shapeDrawable = materialShapeDrawable;
    materialShapeDrawable.setTintList(paramColorStateList1);
    if (paramMode != null)
      this.shapeDrawable.setTintMode(paramMode); 
    this.shapeDrawable.setShadowColor(-12303292);
    this.shapeDrawable.initializeElevationOverlay(this.view.getContext());
    RippleDrawableCompat rippleDrawableCompat = new RippleDrawableCompat(this.shapeDrawable.getShapeAppearanceModel());
    rippleDrawableCompat.setTintList(RippleUtils.sanitizeRippleDrawableColor(paramColorStateList2));
    this.rippleDrawable = (Drawable)rippleDrawableCompat;
    this.contentBackground = (Drawable)new LayerDrawable(new Drawable[] { (Drawable)Preconditions.checkNotNull(this.shapeDrawable), (Drawable)rippleDrawableCompat });
  }
  
  boolean isOrWillBeHidden() {
    int i = this.view.getVisibility();
    boolean bool1 = false;
    boolean bool2 = false;
    if (i == 0) {
      bool1 = bool2;
      if (this.animState == 1)
        bool1 = true; 
      return bool1;
    } 
    if (this.animState != 2)
      bool1 = true; 
    return bool1;
  }
  
  boolean isOrWillBeShown() {
    int i = this.view.getVisibility();
    boolean bool1 = false;
    boolean bool2 = false;
    if (i != 0) {
      bool1 = bool2;
      if (this.animState == 2)
        bool1 = true; 
      return bool1;
    } 
    if (this.animState != 1)
      bool1 = true; 
    return bool1;
  }
  
  void jumpDrawableToCurrentState() {
    this.stateListAnimator.jumpToCurrentState();
  }
  
  void onAttachedToWindow() {
    MaterialShapeDrawable materialShapeDrawable = this.shapeDrawable;
    if (materialShapeDrawable != null)
      MaterialShapeUtils.setParentAbsoluteElevation((View)this.view, materialShapeDrawable); 
    if (requirePreDrawListener())
      this.view.getViewTreeObserver().addOnPreDrawListener(getOrCreatePreDrawListener()); 
  }
  
  void onCompatShadowChanged() {}
  
  void onDetachedFromWindow() {
    ViewTreeObserver viewTreeObserver = this.view.getViewTreeObserver();
    ViewTreeObserver.OnPreDrawListener onPreDrawListener = this.preDrawListener;
    if (onPreDrawListener != null) {
      viewTreeObserver.removeOnPreDrawListener(onPreDrawListener);
      this.preDrawListener = null;
    } 
  }
  
  void onDrawableStateChanged(int[] paramArrayOfint) {
    this.stateListAnimator.setState(paramArrayOfint);
  }
  
  void onElevationsChanged(float paramFloat1, float paramFloat2, float paramFloat3) {
    updatePadding();
    updateShapeElevation(paramFloat1);
  }
  
  void onPaddingUpdated(Rect paramRect) {
    Preconditions.checkNotNull(this.contentBackground, "Didn't initialize content background");
    if (shouldAddPadding()) {
      InsetDrawable insetDrawable = new InsetDrawable(this.contentBackground, paramRect.left, paramRect.top, paramRect.right, paramRect.bottom);
      this.shadowViewDelegate.setBackgroundDrawable((Drawable)insetDrawable);
    } else {
      this.shadowViewDelegate.setBackgroundDrawable(this.contentBackground);
    } 
  }
  
  void onPreDraw() {
    float f = this.view.getRotation();
    if (this.rotation != f) {
      this.rotation = f;
      updateFromViewRotation();
    } 
  }
  
  void onScaleChanged() {
    ArrayList<InternalTransformationCallback> arrayList = this.transformationCallbacks;
    if (arrayList != null) {
      Iterator<InternalTransformationCallback> iterator = arrayList.iterator();
      while (iterator.hasNext())
        ((InternalTransformationCallback)iterator.next()).onScaleChanged(); 
    } 
  }
  
  void onTranslationChanged() {
    ArrayList<InternalTransformationCallback> arrayList = this.transformationCallbacks;
    if (arrayList != null) {
      Iterator<InternalTransformationCallback> iterator = arrayList.iterator();
      while (iterator.hasNext())
        ((InternalTransformationCallback)iterator.next()).onTranslationChanged(); 
    } 
  }
  
  public void removeOnHideAnimationListener(Animator.AnimatorListener paramAnimatorListener) {
    ArrayList<Animator.AnimatorListener> arrayList = this.hideListeners;
    if (arrayList == null)
      return; 
    arrayList.remove(paramAnimatorListener);
  }
  
  void removeOnShowAnimationListener(Animator.AnimatorListener paramAnimatorListener) {
    ArrayList<Animator.AnimatorListener> arrayList = this.showListeners;
    if (arrayList == null)
      return; 
    arrayList.remove(paramAnimatorListener);
  }
  
  void removeTransformationCallback(InternalTransformationCallback paramInternalTransformationCallback) {
    ArrayList<InternalTransformationCallback> arrayList = this.transformationCallbacks;
    if (arrayList == null)
      return; 
    arrayList.remove(paramInternalTransformationCallback);
  }
  
  boolean requirePreDrawListener() {
    return true;
  }
  
  void setBackgroundTintList(ColorStateList paramColorStateList) {
    MaterialShapeDrawable materialShapeDrawable = this.shapeDrawable;
    if (materialShapeDrawable != null)
      materialShapeDrawable.setTintList(paramColorStateList); 
    BorderDrawable borderDrawable = this.borderDrawable;
    if (borderDrawable != null)
      borderDrawable.setBorderTint(paramColorStateList); 
  }
  
  void setBackgroundTintMode(PorterDuff.Mode paramMode) {
    MaterialShapeDrawable materialShapeDrawable = this.shapeDrawable;
    if (materialShapeDrawable != null)
      materialShapeDrawable.setTintMode(paramMode); 
  }
  
  final void setElevation(float paramFloat) {
    if (this.elevation != paramFloat) {
      this.elevation = paramFloat;
      onElevationsChanged(paramFloat, this.hoveredFocusedTranslationZ, this.pressedTranslationZ);
    } 
  }
  
  void setEnsureMinTouchTargetSize(boolean paramBoolean) {
    this.ensureMinTouchTargetSize = paramBoolean;
  }
  
  final void setHideMotionSpec(MotionSpec paramMotionSpec) {
    this.hideMotionSpec = paramMotionSpec;
  }
  
  final void setHoveredFocusedTranslationZ(float paramFloat) {
    if (this.hoveredFocusedTranslationZ != paramFloat) {
      this.hoveredFocusedTranslationZ = paramFloat;
      onElevationsChanged(this.elevation, paramFloat, this.pressedTranslationZ);
    } 
  }
  
  final void setImageMatrixScale(float paramFloat) {
    this.imageMatrixScale = paramFloat;
    Matrix matrix = this.tmpMatrix;
    calculateImageMatrixFromScale(paramFloat, matrix);
    this.view.setImageMatrix(matrix);
  }
  
  final void setMaxImageSize(int paramInt) {
    if (this.maxImageSize != paramInt) {
      this.maxImageSize = paramInt;
      updateImageMatrixScale();
    } 
  }
  
  void setMinTouchTargetSize(int paramInt) {
    this.minTouchTargetSize = paramInt;
  }
  
  final void setPressedTranslationZ(float paramFloat) {
    if (this.pressedTranslationZ != paramFloat) {
      this.pressedTranslationZ = paramFloat;
      onElevationsChanged(this.elevation, this.hoveredFocusedTranslationZ, paramFloat);
    } 
  }
  
  void setRippleColor(ColorStateList paramColorStateList) {
    Drawable drawable = this.rippleDrawable;
    if (drawable != null)
      DrawableCompat.setTintList(drawable, RippleUtils.sanitizeRippleDrawableColor(paramColorStateList)); 
  }
  
  void setShadowPaddingEnabled(boolean paramBoolean) {
    this.shadowPaddingEnabled = paramBoolean;
    updatePadding();
  }
  
  final void setShapeAppearance(ShapeAppearanceModel paramShapeAppearanceModel) {
    this.shapeAppearance = paramShapeAppearanceModel;
    MaterialShapeDrawable materialShapeDrawable = this.shapeDrawable;
    if (materialShapeDrawable != null)
      materialShapeDrawable.setShapeAppearanceModel(paramShapeAppearanceModel); 
    Drawable drawable = this.rippleDrawable;
    if (drawable instanceof Shapeable)
      ((Shapeable)drawable).setShapeAppearanceModel(paramShapeAppearanceModel); 
    drawable = this.borderDrawable;
    if (drawable != null)
      drawable.setShapeAppearanceModel(paramShapeAppearanceModel); 
  }
  
  final void setShowMotionSpec(MotionSpec paramMotionSpec) {
    this.showMotionSpec = paramMotionSpec;
  }
  
  boolean shouldAddPadding() {
    return true;
  }
  
  final boolean shouldExpandBoundsForA11y() {
    return (!this.ensureMinTouchTargetSize || this.view.getSizeDimension() >= this.minTouchTargetSize);
  }
  
  void show(final InternalVisibilityChangedListener listener, final boolean fromUser) {
    Iterator<Animator.AnimatorListener> iterator;
    if (isOrWillBeShown())
      return; 
    Animator animator = this.currentAnimator;
    if (animator != null)
      animator.cancel(); 
    if (shouldAnimateVisibilityChange()) {
      if (this.view.getVisibility() != 0) {
        this.view.setAlpha(0.0F);
        this.view.setScaleY(0.0F);
        this.view.setScaleX(0.0F);
        setImageMatrixScale(0.0F);
      } 
      MotionSpec motionSpec = this.showMotionSpec;
      if (motionSpec == null)
        motionSpec = getDefaultShowMotionSpec(); 
      AnimatorSet animatorSet = createAnimator(motionSpec, 1.0F, 1.0F, 1.0F);
      animatorSet.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter() {
            final FloatingActionButtonImpl this$0;
            
            final boolean val$fromUser;
            
            final FloatingActionButtonImpl.InternalVisibilityChangedListener val$listener;
            
            public void onAnimationEnd(Animator param1Animator) {
              FloatingActionButtonImpl.access$002(FloatingActionButtonImpl.this, 0);
              FloatingActionButtonImpl.access$102(FloatingActionButtonImpl.this, null);
              FloatingActionButtonImpl.InternalVisibilityChangedListener internalVisibilityChangedListener = listener;
              if (internalVisibilityChangedListener != null)
                internalVisibilityChangedListener.onShown(); 
            }
            
            public void onAnimationStart(Animator param1Animator) {
              FloatingActionButtonImpl.this.view.internalSetVisibility(0, fromUser);
              FloatingActionButtonImpl.access$002(FloatingActionButtonImpl.this, 2);
              FloatingActionButtonImpl.access$102(FloatingActionButtonImpl.this, param1Animator);
            }
          });
      ArrayList<Animator.AnimatorListener> arrayList = this.showListeners;
      if (arrayList != null) {
        iterator = arrayList.iterator();
        while (iterator.hasNext())
          animatorSet.addListener(iterator.next()); 
      } 
      animatorSet.start();
    } else {
      this.view.internalSetVisibility(0, fromUser);
      this.view.setAlpha(1.0F);
      this.view.setScaleY(1.0F);
      this.view.setScaleX(1.0F);
      setImageMatrixScale(1.0F);
      if (iterator != null)
        iterator.onShown(); 
    } 
  }
  
  void updateFromViewRotation() {
    if (Build.VERSION.SDK_INT == 19)
      if (this.rotation % 90.0F != 0.0F) {
        if (this.view.getLayerType() != 1)
          this.view.setLayerType(1, null); 
      } else if (this.view.getLayerType() != 0) {
        this.view.setLayerType(0, null);
      }  
    MaterialShapeDrawable materialShapeDrawable = this.shapeDrawable;
    if (materialShapeDrawable != null)
      materialShapeDrawable.setShadowCompatRotation((int)this.rotation); 
  }
  
  final void updateImageMatrixScale() {
    setImageMatrixScale(this.imageMatrixScale);
  }
  
  final void updatePadding() {
    Rect rect = this.tmpRect;
    getPadding(rect);
    onPaddingUpdated(rect);
    this.shadowViewDelegate.setShadowPadding(rect.left, rect.top, rect.right, rect.bottom);
  }
  
  void updateShapeElevation(float paramFloat) {
    MaterialShapeDrawable materialShapeDrawable = this.shapeDrawable;
    if (materialShapeDrawable != null)
      materialShapeDrawable.setElevation(paramFloat); 
  }
  
  private class DisabledElevationAnimation extends ShadowAnimatorImpl {
    final FloatingActionButtonImpl this$0;
    
    protected float getTargetShadowSize() {
      return 0.0F;
    }
  }
  
  private class ElevateToHoveredFocusedTranslationZAnimation extends ShadowAnimatorImpl {
    final FloatingActionButtonImpl this$0;
    
    protected float getTargetShadowSize() {
      return FloatingActionButtonImpl.this.elevation + FloatingActionButtonImpl.this.hoveredFocusedTranslationZ;
    }
  }
  
  private class ElevateToPressedTranslationZAnimation extends ShadowAnimatorImpl {
    final FloatingActionButtonImpl this$0;
    
    protected float getTargetShadowSize() {
      return FloatingActionButtonImpl.this.elevation + FloatingActionButtonImpl.this.pressedTranslationZ;
    }
  }
  
  static interface InternalTransformationCallback {
    void onScaleChanged();
    
    void onTranslationChanged();
  }
  
  static interface InternalVisibilityChangedListener {
    void onHidden();
    
    void onShown();
  }
  
  private class ResetElevationAnimation extends ShadowAnimatorImpl {
    final FloatingActionButtonImpl this$0;
    
    protected float getTargetShadowSize() {
      return FloatingActionButtonImpl.this.elevation;
    }
  }
  
  private abstract class ShadowAnimatorImpl extends AnimatorListenerAdapter implements ValueAnimator.AnimatorUpdateListener {
    private float shadowSizeEnd;
    
    private float shadowSizeStart;
    
    final FloatingActionButtonImpl this$0;
    
    private boolean validValues;
    
    private ShadowAnimatorImpl() {}
    
    protected abstract float getTargetShadowSize();
    
    public void onAnimationEnd(Animator param1Animator) {
      FloatingActionButtonImpl.this.updateShapeElevation((int)this.shadowSizeEnd);
      this.validValues = false;
    }
    
    public void onAnimationUpdate(ValueAnimator param1ValueAnimator) {
      if (!this.validValues) {
        float f1;
        if (FloatingActionButtonImpl.this.shapeDrawable == null) {
          f1 = 0.0F;
        } else {
          f1 = FloatingActionButtonImpl.this.shapeDrawable.getElevation();
        } 
        this.shadowSizeStart = f1;
        this.shadowSizeEnd = getTargetShadowSize();
        this.validValues = true;
      } 
      FloatingActionButtonImpl floatingActionButtonImpl = FloatingActionButtonImpl.this;
      float f = this.shadowSizeStart;
      floatingActionButtonImpl.updateShapeElevation((int)(f + (this.shadowSizeEnd - f) * param1ValueAnimator.getAnimatedFraction()));
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\floatingactionbutton\FloatingActionButtonImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */