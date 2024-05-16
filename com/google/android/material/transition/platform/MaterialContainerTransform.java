package com.google.android.material.transition.platform;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.transition.PathMotion;
import android.transition.Transition;
import android.transition.TransitionValues;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import androidx.core.util.Preconditions;
import androidx.core.view.ViewCompat;
import com.google.android.material.R;
import com.google.android.material.animation.AnimationUtils;
import com.google.android.material.internal.ViewUtils;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.android.material.shape.Shapeable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class MaterialContainerTransform extends Transition {
  private static final ProgressThresholdsGroup DEFAULT_ENTER_THRESHOLDS;
  
  private static final ProgressThresholdsGroup DEFAULT_ENTER_THRESHOLDS_ARC;
  
  private static final ProgressThresholdsGroup DEFAULT_RETURN_THRESHOLDS;
  
  private static final ProgressThresholdsGroup DEFAULT_RETURN_THRESHOLDS_ARC;
  
  private static final float ELEVATION_NOT_SET = -1.0F;
  
  public static final int FADE_MODE_CROSS = 2;
  
  public static final int FADE_MODE_IN = 0;
  
  public static final int FADE_MODE_OUT = 1;
  
  public static final int FADE_MODE_THROUGH = 3;
  
  public static final int FIT_MODE_AUTO = 0;
  
  public static final int FIT_MODE_HEIGHT = 2;
  
  public static final int FIT_MODE_WIDTH = 1;
  
  private static final String PROP_BOUNDS = "materialContainerTransition:bounds";
  
  private static final String PROP_SHAPE_APPEARANCE = "materialContainerTransition:shapeAppearance";
  
  private static final String TAG = MaterialContainerTransform.class.getSimpleName();
  
  public static final int TRANSITION_DIRECTION_AUTO = 0;
  
  public static final int TRANSITION_DIRECTION_ENTER = 1;
  
  public static final int TRANSITION_DIRECTION_RETURN = 2;
  
  private static final String[] TRANSITION_PROPS = new String[] { "materialContainerTransition:bounds", "materialContainerTransition:shapeAppearance" };
  
  private boolean appliedThemeValues;
  
  private int containerColor;
  
  private boolean drawDebugEnabled;
  
  private int drawingViewId;
  
  private boolean elevationShadowEnabled;
  
  private int endContainerColor;
  
  private float endElevation;
  
  private ShapeAppearanceModel endShapeAppearanceModel;
  
  private View endView;
  
  private int endViewId;
  
  private int fadeMode;
  
  private ProgressThresholds fadeProgressThresholds;
  
  private int fitMode;
  
  private boolean holdAtEndEnabled;
  
  private boolean pathMotionCustom;
  
  private ProgressThresholds scaleMaskProgressThresholds;
  
  private ProgressThresholds scaleProgressThresholds;
  
  private int scrimColor;
  
  private ProgressThresholds shapeMaskProgressThresholds;
  
  private int startContainerColor;
  
  private float startElevation;
  
  private ShapeAppearanceModel startShapeAppearanceModel;
  
  private View startView;
  
  private int startViewId;
  
  private int transitionDirection;
  
  static {
    DEFAULT_ENTER_THRESHOLDS = new ProgressThresholdsGroup(new ProgressThresholds(0.0F, 0.25F), new ProgressThresholds(0.0F, 1.0F), new ProgressThresholds(0.0F, 1.0F), new ProgressThresholds(0.0F, 0.75F));
    DEFAULT_RETURN_THRESHOLDS = new ProgressThresholdsGroup(new ProgressThresholds(0.6F, 0.9F), new ProgressThresholds(0.0F, 1.0F), new ProgressThresholds(0.0F, 0.9F), new ProgressThresholds(0.3F, 0.9F));
    DEFAULT_ENTER_THRESHOLDS_ARC = new ProgressThresholdsGroup(new ProgressThresholds(0.1F, 0.4F), new ProgressThresholds(0.1F, 1.0F), new ProgressThresholds(0.1F, 1.0F), new ProgressThresholds(0.1F, 0.9F));
    DEFAULT_RETURN_THRESHOLDS_ARC = new ProgressThresholdsGroup(new ProgressThresholds(0.6F, 0.9F), new ProgressThresholds(0.0F, 0.9F), new ProgressThresholds(0.0F, 0.9F), new ProgressThresholds(0.2F, 0.9F));
  }
  
  public MaterialContainerTransform() {
    boolean bool = false;
    this.drawDebugEnabled = false;
    this.holdAtEndEnabled = false;
    this.pathMotionCustom = false;
    this.appliedThemeValues = false;
    this.drawingViewId = 16908290;
    this.startViewId = -1;
    this.endViewId = -1;
    this.containerColor = 0;
    this.startContainerColor = 0;
    this.endContainerColor = 0;
    this.scrimColor = 1375731712;
    this.transitionDirection = 0;
    this.fadeMode = 0;
    this.fitMode = 0;
    if (Build.VERSION.SDK_INT >= 28)
      bool = true; 
    this.elevationShadowEnabled = bool;
    this.startElevation = -1.0F;
    this.endElevation = -1.0F;
  }
  
  public MaterialContainerTransform(Context paramContext, boolean paramBoolean) {
    boolean bool = false;
    this.drawDebugEnabled = false;
    this.holdAtEndEnabled = false;
    this.pathMotionCustom = false;
    this.appliedThemeValues = false;
    this.drawingViewId = 16908290;
    this.startViewId = -1;
    this.endViewId = -1;
    this.containerColor = 0;
    this.startContainerColor = 0;
    this.endContainerColor = 0;
    this.scrimColor = 1375731712;
    this.transitionDirection = 0;
    this.fadeMode = 0;
    this.fitMode = 0;
    if (Build.VERSION.SDK_INT >= 28)
      bool = true; 
    this.elevationShadowEnabled = bool;
    this.startElevation = -1.0F;
    this.endElevation = -1.0F;
    maybeApplyThemeValues(paramContext, paramBoolean);
    this.appliedThemeValues = true;
  }
  
  private ProgressThresholdsGroup buildThresholdsGroup(boolean paramBoolean) {
    PathMotion pathMotion = getPathMotion();
    return (pathMotion instanceof android.transition.ArcMotion || pathMotion instanceof MaterialArcMotion) ? getThresholdsOrDefault(paramBoolean, DEFAULT_ENTER_THRESHOLDS_ARC, DEFAULT_RETURN_THRESHOLDS_ARC) : getThresholdsOrDefault(paramBoolean, DEFAULT_ENTER_THRESHOLDS, DEFAULT_RETURN_THRESHOLDS);
  }
  
  private static RectF calculateDrawableBounds(View paramView1, View paramView2, float paramFloat1, float paramFloat2) {
    RectF rectF;
    if (paramView2 != null) {
      rectF = TransitionUtils.getLocationOnScreen(paramView2);
      rectF.offset(paramFloat1, paramFloat2);
      return rectF;
    } 
    return new RectF(0.0F, 0.0F, rectF.getWidth(), rectF.getHeight());
  }
  
  private static ShapeAppearanceModel captureShapeAppearance(View paramView, RectF paramRectF, ShapeAppearanceModel paramShapeAppearanceModel) {
    return TransitionUtils.convertToRelativeCornerSizes(getShapeAppearance(paramView, paramShapeAppearanceModel), paramRectF);
  }
  
  private static void captureValues(TransitionValues paramTransitionValues, View paramView, int paramInt, ShapeAppearanceModel paramShapeAppearanceModel) {
    if (paramInt != -1) {
      paramTransitionValues.view = TransitionUtils.findDescendantOrAncestorById(paramTransitionValues.view, paramInt);
    } else if (paramView != null) {
      paramTransitionValues.view = paramView;
    } else if (paramTransitionValues.view.getTag(R.id.mtrl_motion_snapshot_view) instanceof View) {
      paramView = (View)paramTransitionValues.view.getTag(R.id.mtrl_motion_snapshot_view);
      paramTransitionValues.view.setTag(R.id.mtrl_motion_snapshot_view, null);
      paramTransitionValues.view = paramView;
    } 
    View view = paramTransitionValues.view;
    if (ViewCompat.isLaidOut(view) || view.getWidth() != 0 || view.getHeight() != 0) {
      RectF rectF;
      if (view.getParent() == null) {
        rectF = TransitionUtils.getRelativeBounds(view);
      } else {
        rectF = TransitionUtils.getLocationOnScreen(view);
      } 
      paramTransitionValues.values.put("materialContainerTransition:bounds", rectF);
      paramTransitionValues.values.put("materialContainerTransition:shapeAppearance", captureShapeAppearance(view, rectF, paramShapeAppearanceModel));
    } 
  }
  
  private static float getElevationOrDefault(float paramFloat, View paramView) {
    if (paramFloat == -1.0F)
      paramFloat = ViewCompat.getElevation(paramView); 
    return paramFloat;
  }
  
  private static ShapeAppearanceModel getShapeAppearance(View paramView, ShapeAppearanceModel paramShapeAppearanceModel) {
    if (paramShapeAppearanceModel != null)
      return paramShapeAppearanceModel; 
    if (paramView.getTag(R.id.mtrl_motion_snapshot_view) instanceof ShapeAppearanceModel)
      return (ShapeAppearanceModel)paramView.getTag(R.id.mtrl_motion_snapshot_view); 
    Context context = paramView.getContext();
    int i = getTransitionShapeAppearanceResId(context);
    return (i != -1) ? ShapeAppearanceModel.builder(context, i, 0).build() : ((paramView instanceof Shapeable) ? ((Shapeable)paramView).getShapeAppearanceModel() : ShapeAppearanceModel.builder().build());
  }
  
  private ProgressThresholdsGroup getThresholdsOrDefault(boolean paramBoolean, ProgressThresholdsGroup paramProgressThresholdsGroup1, ProgressThresholdsGroup paramProgressThresholdsGroup2) {
    if (!paramBoolean)
      paramProgressThresholdsGroup1 = paramProgressThresholdsGroup2; 
    return new ProgressThresholdsGroup(TransitionUtils.<ProgressThresholds>defaultIfNull(this.fadeProgressThresholds, paramProgressThresholdsGroup1.fade), TransitionUtils.<ProgressThresholds>defaultIfNull(this.scaleProgressThresholds, paramProgressThresholdsGroup1.scale), TransitionUtils.<ProgressThresholds>defaultIfNull(this.scaleMaskProgressThresholds, paramProgressThresholdsGroup1.scaleMask), TransitionUtils.<ProgressThresholds>defaultIfNull(this.shapeMaskProgressThresholds, paramProgressThresholdsGroup1.shapeMask));
  }
  
  private static int getTransitionShapeAppearanceResId(Context paramContext) {
    TypedArray typedArray = paramContext.obtainStyledAttributes(new int[] { R.attr.transitionShapeAppearance });
    int i = typedArray.getResourceId(0, -1);
    typedArray.recycle();
    return i;
  }
  
  private boolean isEntering(RectF paramRectF1, RectF paramRectF2) {
    int i = this.transitionDirection;
    boolean bool = false;
    switch (i) {
      default:
        throw new IllegalArgumentException("Invalid transition direction: " + this.transitionDirection);
      case 2:
        return false;
      case 1:
        return true;
      case 0:
        break;
    } 
    if (TransitionUtils.calculateArea(paramRectF2) > TransitionUtils.calculateArea(paramRectF1))
      bool = true; 
    return bool;
  }
  
  private void maybeApplyThemeValues(Context paramContext, boolean paramBoolean) {
    int i;
    TransitionUtils.maybeApplyThemeInterpolator(this, paramContext, R.attr.motionEasingStandard, AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
    if (paramBoolean) {
      i = R.attr.motionDurationLong1;
    } else {
      i = R.attr.motionDurationMedium2;
    } 
    TransitionUtils.maybeApplyThemeDuration(this, paramContext, i);
    if (!this.pathMotionCustom)
      TransitionUtils.maybeApplyThemePath(this, paramContext, R.attr.motionPath); 
  }
  
  public void captureEndValues(TransitionValues paramTransitionValues) {
    captureValues(paramTransitionValues, this.endView, this.endViewId, this.endShapeAppearanceModel);
  }
  
  public void captureStartValues(TransitionValues paramTransitionValues) {
    captureValues(paramTransitionValues, this.startView, this.startViewId, this.startShapeAppearanceModel);
  }
  
  public Animator createAnimator(ViewGroup paramViewGroup, TransitionValues paramTransitionValues1, TransitionValues paramTransitionValues2) {
    View view1;
    final View drawingView;
    if (paramTransitionValues1 == null || paramTransitionValues2 == null)
      return null; 
    RectF rectF3 = (RectF)paramTransitionValues1.values.get("materialContainerTransition:bounds");
    ShapeAppearanceModel shapeAppearanceModel1 = (ShapeAppearanceModel)paramTransitionValues1.values.get("materialContainerTransition:shapeAppearance");
    if (rectF3 == null || shapeAppearanceModel1 == null) {
      Log.w(TAG, "Skipping due to null start bounds. Ensure start view is laid out and measured.");
      return null;
    } 
    RectF rectF2 = (RectF)paramTransitionValues2.values.get("materialContainerTransition:bounds");
    ShapeAppearanceModel shapeAppearanceModel2 = (ShapeAppearanceModel)paramTransitionValues2.values.get("materialContainerTransition:shapeAppearance");
    if (rectF2 == null || shapeAppearanceModel2 == null) {
      Log.w(TAG, "Skipping due to null end bounds. Ensure end view is laid out and measured.");
      return null;
    } 
    final View startView = paramTransitionValues1.view;
    final View endView = paramTransitionValues2.view;
    if (view4.getParent() != null) {
      view1 = view4;
    } else {
      view1 = view3;
    } 
    if (this.drawingViewId == view1.getId()) {
      view2 = (View)view1.getParent();
      View view = view1;
    } else {
      view2 = TransitionUtils.findAncestorById(view1, this.drawingViewId);
      paramTransitionValues2 = null;
    } 
    RectF rectF4 = TransitionUtils.getLocationOnScreen(view2);
    float f1 = -rectF4.left;
    float f2 = -rectF4.top;
    RectF rectF1 = calculateDrawableBounds(view2, (View)paramTransitionValues2, f1, f2);
    rectF3.offset(f1, f2);
    rectF2.offset(f1, f2);
    boolean bool = isEntering(rectF3, rectF2);
    if (!this.appliedThemeValues)
      maybeApplyThemeValues(view1.getContext(), bool); 
    final TransitionDrawable transitionDrawable = new TransitionDrawable(getPathMotion(), view3, rectF3, shapeAppearanceModel1, getElevationOrDefault(this.startElevation, view3), view4, rectF2, shapeAppearanceModel2, getElevationOrDefault(this.endElevation, view4), this.containerColor, this.startContainerColor, this.endContainerColor, this.scrimColor, bool, this.elevationShadowEnabled, FadeModeEvaluators.get(this.fadeMode, bool), FitModeEvaluators.get(this.fitMode, bool, rectF3, rectF2), buildThresholdsGroup(bool), this.drawDebugEnabled);
    transitionDrawable.setBounds(Math.round(rectF1.left), Math.round(rectF1.top), Math.round(rectF1.right), Math.round(rectF1.bottom));
    ValueAnimator valueAnimator = ValueAnimator.ofFloat(new float[] { 0.0F, 1.0F });
    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
          final MaterialContainerTransform this$0;
          
          final MaterialContainerTransform.TransitionDrawable val$transitionDrawable;
          
          public void onAnimationUpdate(ValueAnimator param1ValueAnimator) {
            transitionDrawable.setProgress(param1ValueAnimator.getAnimatedFraction());
          }
        });
    addListener(new TransitionListenerAdapter() {
          final MaterialContainerTransform this$0;
          
          final View val$drawingView;
          
          final View val$endView;
          
          final View val$startView;
          
          final MaterialContainerTransform.TransitionDrawable val$transitionDrawable;
          
          public void onTransitionEnd(Transition param1Transition) {
            MaterialContainerTransform.this.removeListener(this);
            if (MaterialContainerTransform.this.holdAtEndEnabled)
              return; 
            startView.setAlpha(1.0F);
            endView.setAlpha(1.0F);
            ViewUtils.getOverlay(drawingView).remove(transitionDrawable);
          }
          
          public void onTransitionStart(Transition param1Transition) {
            ViewUtils.getOverlay(drawingView).add(transitionDrawable);
            startView.setAlpha(0.0F);
            endView.setAlpha(0.0F);
          }
        });
    return (Animator)valueAnimator;
  }
  
  public int getContainerColor() {
    return this.containerColor;
  }
  
  public int getDrawingViewId() {
    return this.drawingViewId;
  }
  
  public int getEndContainerColor() {
    return this.endContainerColor;
  }
  
  public float getEndElevation() {
    return this.endElevation;
  }
  
  public ShapeAppearanceModel getEndShapeAppearanceModel() {
    return this.endShapeAppearanceModel;
  }
  
  public View getEndView() {
    return this.endView;
  }
  
  public int getEndViewId() {
    return this.endViewId;
  }
  
  public int getFadeMode() {
    return this.fadeMode;
  }
  
  public ProgressThresholds getFadeProgressThresholds() {
    return this.fadeProgressThresholds;
  }
  
  public int getFitMode() {
    return this.fitMode;
  }
  
  public ProgressThresholds getScaleMaskProgressThresholds() {
    return this.scaleMaskProgressThresholds;
  }
  
  public ProgressThresholds getScaleProgressThresholds() {
    return this.scaleProgressThresholds;
  }
  
  public int getScrimColor() {
    return this.scrimColor;
  }
  
  public ProgressThresholds getShapeMaskProgressThresholds() {
    return this.shapeMaskProgressThresholds;
  }
  
  public int getStartContainerColor() {
    return this.startContainerColor;
  }
  
  public float getStartElevation() {
    return this.startElevation;
  }
  
  public ShapeAppearanceModel getStartShapeAppearanceModel() {
    return this.startShapeAppearanceModel;
  }
  
  public View getStartView() {
    return this.startView;
  }
  
  public int getStartViewId() {
    return this.startViewId;
  }
  
  public int getTransitionDirection() {
    return this.transitionDirection;
  }
  
  public String[] getTransitionProperties() {
    return TRANSITION_PROPS;
  }
  
  public boolean isDrawDebugEnabled() {
    return this.drawDebugEnabled;
  }
  
  public boolean isElevationShadowEnabled() {
    return this.elevationShadowEnabled;
  }
  
  public boolean isHoldAtEndEnabled() {
    return this.holdAtEndEnabled;
  }
  
  public void setAllContainerColors(int paramInt) {
    this.containerColor = paramInt;
    this.startContainerColor = paramInt;
    this.endContainerColor = paramInt;
  }
  
  public void setContainerColor(int paramInt) {
    this.containerColor = paramInt;
  }
  
  public void setDrawDebugEnabled(boolean paramBoolean) {
    this.drawDebugEnabled = paramBoolean;
  }
  
  public void setDrawingViewId(int paramInt) {
    this.drawingViewId = paramInt;
  }
  
  public void setElevationShadowEnabled(boolean paramBoolean) {
    this.elevationShadowEnabled = paramBoolean;
  }
  
  public void setEndContainerColor(int paramInt) {
    this.endContainerColor = paramInt;
  }
  
  public void setEndElevation(float paramFloat) {
    this.endElevation = paramFloat;
  }
  
  public void setEndShapeAppearanceModel(ShapeAppearanceModel paramShapeAppearanceModel) {
    this.endShapeAppearanceModel = paramShapeAppearanceModel;
  }
  
  public void setEndView(View paramView) {
    this.endView = paramView;
  }
  
  public void setEndViewId(int paramInt) {
    this.endViewId = paramInt;
  }
  
  public void setFadeMode(int paramInt) {
    this.fadeMode = paramInt;
  }
  
  public void setFadeProgressThresholds(ProgressThresholds paramProgressThresholds) {
    this.fadeProgressThresholds = paramProgressThresholds;
  }
  
  public void setFitMode(int paramInt) {
    this.fitMode = paramInt;
  }
  
  public void setHoldAtEndEnabled(boolean paramBoolean) {
    this.holdAtEndEnabled = paramBoolean;
  }
  
  public void setPathMotion(PathMotion paramPathMotion) {
    super.setPathMotion(paramPathMotion);
    this.pathMotionCustom = true;
  }
  
  public void setScaleMaskProgressThresholds(ProgressThresholds paramProgressThresholds) {
    this.scaleMaskProgressThresholds = paramProgressThresholds;
  }
  
  public void setScaleProgressThresholds(ProgressThresholds paramProgressThresholds) {
    this.scaleProgressThresholds = paramProgressThresholds;
  }
  
  public void setScrimColor(int paramInt) {
    this.scrimColor = paramInt;
  }
  
  public void setShapeMaskProgressThresholds(ProgressThresholds paramProgressThresholds) {
    this.shapeMaskProgressThresholds = paramProgressThresholds;
  }
  
  public void setStartContainerColor(int paramInt) {
    this.startContainerColor = paramInt;
  }
  
  public void setStartElevation(float paramFloat) {
    this.startElevation = paramFloat;
  }
  
  public void setStartShapeAppearanceModel(ShapeAppearanceModel paramShapeAppearanceModel) {
    this.startShapeAppearanceModel = paramShapeAppearanceModel;
  }
  
  public void setStartView(View paramView) {
    this.startView = paramView;
  }
  
  public void setStartViewId(int paramInt) {
    this.startViewId = paramInt;
  }
  
  public void setTransitionDirection(int paramInt) {
    this.transitionDirection = paramInt;
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface FadeMode {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface FitMode {}
  
  public static class ProgressThresholds {
    private final float end;
    
    private final float start;
    
    public ProgressThresholds(float param1Float1, float param1Float2) {
      this.start = param1Float1;
      this.end = param1Float2;
    }
    
    public float getEnd() {
      return this.end;
    }
    
    public float getStart() {
      return this.start;
    }
  }
  
  private static class ProgressThresholdsGroup {
    private final MaterialContainerTransform.ProgressThresholds fade;
    
    private final MaterialContainerTransform.ProgressThresholds scale;
    
    private final MaterialContainerTransform.ProgressThresholds scaleMask;
    
    private final MaterialContainerTransform.ProgressThresholds shapeMask;
    
    private ProgressThresholdsGroup(MaterialContainerTransform.ProgressThresholds param1ProgressThresholds1, MaterialContainerTransform.ProgressThresholds param1ProgressThresholds2, MaterialContainerTransform.ProgressThresholds param1ProgressThresholds3, MaterialContainerTransform.ProgressThresholds param1ProgressThresholds4) {
      this.fade = param1ProgressThresholds1;
      this.scale = param1ProgressThresholds2;
      this.scaleMask = param1ProgressThresholds3;
      this.shapeMask = param1ProgressThresholds4;
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface TransitionDirection {}
  
  private static final class TransitionDrawable extends Drawable {
    private static final int COMPAT_SHADOW_COLOR = -7829368;
    
    private static final int SHADOW_COLOR = 754974720;
    
    private static final float SHADOW_DX_MULTIPLIER_ADJUSTMENT = 0.3F;
    
    private static final float SHADOW_DY_MULTIPLIER_ADJUSTMENT = 1.5F;
    
    private final MaterialShapeDrawable compatShadowDrawable;
    
    private final Paint containerPaint;
    
    private float currentElevation;
    
    private float currentElevationDy;
    
    private final RectF currentEndBounds;
    
    private final RectF currentEndBoundsMasked;
    
    private RectF currentMaskBounds;
    
    private final RectF currentStartBounds;
    
    private final RectF currentStartBoundsMasked;
    
    private final Paint debugPaint;
    
    private final Path debugPath;
    
    private final float displayHeight;
    
    private final float displayWidth;
    
    private final boolean drawDebugEnabled;
    
    private final boolean elevationShadowEnabled;
    
    private final RectF endBounds;
    
    private final Paint endContainerPaint;
    
    private final float endElevation;
    
    private final ShapeAppearanceModel endShapeAppearanceModel;
    
    private final View endView;
    
    private final boolean entering;
    
    private final FadeModeEvaluator fadeModeEvaluator;
    
    private FadeModeResult fadeModeResult;
    
    private final FitModeEvaluator fitModeEvaluator;
    
    private FitModeResult fitModeResult;
    
    private final MaskEvaluator maskEvaluator;
    
    private final float motionPathLength;
    
    private final PathMeasure motionPathMeasure;
    
    private final float[] motionPathPosition;
    
    private float progress;
    
    private final MaterialContainerTransform.ProgressThresholdsGroup progressThresholds;
    
    private final Paint scrimPaint;
    
    private final Paint shadowPaint;
    
    private final RectF startBounds;
    
    private final Paint startContainerPaint;
    
    private final float startElevation;
    
    private final ShapeAppearanceModel startShapeAppearanceModel;
    
    private final View startView;
    
    private TransitionDrawable(PathMotion param1PathMotion, View param1View1, RectF param1RectF1, ShapeAppearanceModel param1ShapeAppearanceModel1, float param1Float1, View param1View2, RectF param1RectF2, ShapeAppearanceModel param1ShapeAppearanceModel2, float param1Float2, int param1Int1, int param1Int2, int param1Int3, int param1Int4, boolean param1Boolean1, boolean param1Boolean2, FadeModeEvaluator param1FadeModeEvaluator, FitModeEvaluator param1FitModeEvaluator, MaterialContainerTransform.ProgressThresholdsGroup param1ProgressThresholdsGroup, boolean param1Boolean3) {
      Paint paint4 = new Paint();
      this.containerPaint = paint4;
      Paint paint3 = new Paint();
      this.startContainerPaint = paint3;
      Paint paint5 = new Paint();
      this.endContainerPaint = paint5;
      this.shadowPaint = new Paint();
      Paint paint2 = new Paint();
      this.scrimPaint = paint2;
      this.maskEvaluator = new MaskEvaluator();
      float[] arrayOfFloat = new float[2];
      this.motionPathPosition = arrayOfFloat;
      MaterialShapeDrawable materialShapeDrawable = new MaterialShapeDrawable();
      this.compatShadowDrawable = materialShapeDrawable;
      Paint paint1 = new Paint();
      this.debugPaint = paint1;
      this.debugPath = new Path();
      this.startView = param1View1;
      this.startBounds = param1RectF1;
      this.startShapeAppearanceModel = param1ShapeAppearanceModel1;
      this.startElevation = param1Float1;
      this.endView = param1View2;
      this.endBounds = param1RectF2;
      this.endShapeAppearanceModel = param1ShapeAppearanceModel2;
      this.endElevation = param1Float2;
      this.entering = param1Boolean1;
      this.elevationShadowEnabled = param1Boolean2;
      this.fadeModeEvaluator = param1FadeModeEvaluator;
      this.fitModeEvaluator = param1FitModeEvaluator;
      this.progressThresholds = param1ProgressThresholdsGroup;
      this.drawDebugEnabled = param1Boolean3;
      WindowManager windowManager = (WindowManager)param1View1.getContext().getSystemService("window");
      DisplayMetrics displayMetrics = new DisplayMetrics();
      windowManager.getDefaultDisplay().getMetrics(displayMetrics);
      this.displayWidth = displayMetrics.widthPixels;
      this.displayHeight = displayMetrics.heightPixels;
      paint4.setColor(param1Int1);
      paint3.setColor(param1Int2);
      paint5.setColor(param1Int3);
      materialShapeDrawable.setFillColor(ColorStateList.valueOf(0));
      materialShapeDrawable.setShadowCompatibilityMode(2);
      materialShapeDrawable.setShadowBitmapDrawingEnable(false);
      materialShapeDrawable.setShadowColor(-7829368);
      RectF rectF = new RectF(param1RectF1);
      this.currentStartBounds = rectF;
      this.currentStartBoundsMasked = new RectF(rectF);
      rectF = new RectF(rectF);
      this.currentEndBounds = rectF;
      this.currentEndBoundsMasked = new RectF(rectF);
      PointF pointF1 = getMotionPathPoint(param1RectF1);
      PointF pointF2 = getMotionPathPoint(param1RectF2);
      PathMeasure pathMeasure = new PathMeasure(param1PathMotion.getPath(pointF1.x, pointF1.y, pointF2.x, pointF2.y), false);
      this.motionPathMeasure = pathMeasure;
      this.motionPathLength = pathMeasure.getLength();
      arrayOfFloat[0] = param1RectF1.centerX();
      arrayOfFloat[1] = param1RectF1.top;
      paint2.setStyle(Paint.Style.FILL);
      paint2.setShader(TransitionUtils.createColorShader(param1Int4));
      paint1.setStyle(Paint.Style.STROKE);
      paint1.setStrokeWidth(10.0F);
      updateProgress(0.0F);
    }
    
    private static float calculateElevationDxMultiplier(RectF param1RectF, float param1Float) {
      return (param1RectF.centerX() / param1Float / 2.0F - 1.0F) * 0.3F;
    }
    
    private static float calculateElevationDyMultiplier(RectF param1RectF, float param1Float) {
      return param1RectF.centerY() / param1Float * 1.5F;
    }
    
    private void drawDebugCumulativePath(Canvas param1Canvas, RectF param1RectF, Path param1Path, int param1Int) {
      PointF pointF = getMotionPathPoint(param1RectF);
      if (this.progress == 0.0F) {
        param1Path.reset();
        param1Path.moveTo(pointF.x, pointF.y);
      } else {
        param1Path.lineTo(pointF.x, pointF.y);
        this.debugPaint.setColor(param1Int);
        param1Canvas.drawPath(param1Path, this.debugPaint);
      } 
    }
    
    private void drawDebugRect(Canvas param1Canvas, RectF param1RectF, int param1Int) {
      this.debugPaint.setColor(param1Int);
      param1Canvas.drawRect(param1RectF, this.debugPaint);
    }
    
    private void drawElevationShadow(Canvas param1Canvas) {
      param1Canvas.save();
      param1Canvas.clipPath(this.maskEvaluator.getPath(), Region.Op.DIFFERENCE);
      if (Build.VERSION.SDK_INT > 28) {
        drawElevationShadowWithPaintShadowLayer(param1Canvas);
      } else {
        drawElevationShadowWithMaterialShapeDrawable(param1Canvas);
      } 
      param1Canvas.restore();
    }
    
    private void drawElevationShadowWithMaterialShapeDrawable(Canvas param1Canvas) {
      this.compatShadowDrawable.setBounds((int)this.currentMaskBounds.left, (int)this.currentMaskBounds.top, (int)this.currentMaskBounds.right, (int)this.currentMaskBounds.bottom);
      this.compatShadowDrawable.setElevation(this.currentElevation);
      this.compatShadowDrawable.setShadowVerticalOffset((int)this.currentElevationDy);
      this.compatShadowDrawable.setShapeAppearanceModel(this.maskEvaluator.getCurrentShapeAppearanceModel());
      this.compatShadowDrawable.draw(param1Canvas);
    }
    
    private void drawElevationShadowWithPaintShadowLayer(Canvas param1Canvas) {
      ShapeAppearanceModel shapeAppearanceModel = this.maskEvaluator.getCurrentShapeAppearanceModel();
      if (shapeAppearanceModel.isRoundRect(this.currentMaskBounds)) {
        float f = shapeAppearanceModel.getTopLeftCornerSize().getCornerSize(this.currentMaskBounds);
        param1Canvas.drawRoundRect(this.currentMaskBounds, f, f, this.shadowPaint);
      } else {
        param1Canvas.drawPath(this.maskEvaluator.getPath(), this.shadowPaint);
      } 
    }
    
    private void drawEndView(Canvas param1Canvas) {
      maybeDrawContainerColor(param1Canvas, this.endContainerPaint);
      TransitionUtils.transform(param1Canvas, getBounds(), this.currentEndBounds.left, this.currentEndBounds.top, this.fitModeResult.endScale, this.fadeModeResult.endAlpha, new TransitionUtils.CanvasOperation() {
            final MaterialContainerTransform.TransitionDrawable this$0;
            
            public void run(Canvas param2Canvas) {
              MaterialContainerTransform.TransitionDrawable.this.endView.draw(param2Canvas);
            }
          });
    }
    
    private void drawStartView(Canvas param1Canvas) {
      maybeDrawContainerColor(param1Canvas, this.startContainerPaint);
      TransitionUtils.transform(param1Canvas, getBounds(), this.currentStartBounds.left, this.currentStartBounds.top, this.fitModeResult.startScale, this.fadeModeResult.startAlpha, new TransitionUtils.CanvasOperation() {
            final MaterialContainerTransform.TransitionDrawable this$0;
            
            public void run(Canvas param2Canvas) {
              MaterialContainerTransform.TransitionDrawable.this.startView.draw(param2Canvas);
            }
          });
    }
    
    private static PointF getMotionPathPoint(RectF param1RectF) {
      return new PointF(param1RectF.centerX(), param1RectF.top);
    }
    
    private void maybeDrawContainerColor(Canvas param1Canvas, Paint param1Paint) {
      if (param1Paint.getColor() != 0 && param1Paint.getAlpha() > 0)
        param1Canvas.drawRect(getBounds(), param1Paint); 
    }
    
    private void setProgress(float param1Float) {
      if (this.progress != param1Float)
        updateProgress(param1Float); 
    }
    
    private void updateProgress(float param1Float) {
      RectF rectF;
      this.progress = param1Float;
      Paint paint = this.scrimPaint;
      if (this.entering) {
        f1 = TransitionUtils.lerp(0.0F, 255.0F, param1Float);
      } else {
        f1 = TransitionUtils.lerp(255.0F, 0.0F, param1Float);
      } 
      paint.setAlpha((int)f1);
      this.motionPathMeasure.getPosTan(this.motionPathLength * param1Float, this.motionPathPosition, null);
      float[] arrayOfFloat = this.motionPathPosition;
      float f4 = arrayOfFloat[0];
      float f3 = arrayOfFloat[1];
      if (param1Float > 1.0F || param1Float < 0.0F) {
        if (param1Float > 1.0F) {
          f2 = 0.99F;
          f1 = (param1Float - 1.0F) / (1.0F - 0.99F);
        } else {
          f2 = 0.01F;
          f1 = param1Float / 0.01F * -1.0F;
        } 
        this.motionPathMeasure.getPosTan(this.motionPathLength * f2, arrayOfFloat, null);
        arrayOfFloat = this.motionPathPosition;
        f2 = arrayOfFloat[0];
        float f = arrayOfFloat[1];
        f2 = f4 + (f4 - f2) * f1;
        f1 = f3 + (f3 - f) * f1;
      } else {
        f2 = f4;
        f1 = f3;
      } 
      f4 = ((Float)Preconditions.checkNotNull(Float.valueOf(this.progressThresholds.scale.start))).floatValue();
      f3 = ((Float)Preconditions.checkNotNull(Float.valueOf(this.progressThresholds.scale.end))).floatValue();
      FitModeResult fitModeResult = this.fitModeEvaluator.evaluate(param1Float, f4, f3, this.startBounds.width(), this.startBounds.height(), this.endBounds.width(), this.endBounds.height());
      this.fitModeResult = fitModeResult;
      this.currentStartBounds.set(f2 - fitModeResult.currentStartWidth / 2.0F, f1, this.fitModeResult.currentStartWidth / 2.0F + f2, this.fitModeResult.currentStartHeight + f1);
      this.currentEndBounds.set(f2 - this.fitModeResult.currentEndWidth / 2.0F, f1, this.fitModeResult.currentEndWidth / 2.0F + f2, this.fitModeResult.currentEndHeight + f1);
      this.currentStartBoundsMasked.set(this.currentStartBounds);
      this.currentEndBoundsMasked.set(this.currentEndBounds);
      float f2 = ((Float)Preconditions.checkNotNull(Float.valueOf(this.progressThresholds.scaleMask.start))).floatValue();
      float f1 = ((Float)Preconditions.checkNotNull(Float.valueOf(this.progressThresholds.scaleMask.end))).floatValue();
      boolean bool = this.fitModeEvaluator.shouldMaskStartBounds(this.fitModeResult);
      if (bool) {
        rectF = this.currentStartBoundsMasked;
      } else {
        rectF = this.currentEndBoundsMasked;
      } 
      f1 = TransitionUtils.lerp(0.0F, 1.0F, f2, f1, param1Float);
      if (!bool)
        f1 = 1.0F - f1; 
      this.fitModeEvaluator.applyMask(rectF, f1, this.fitModeResult);
      this.currentMaskBounds = new RectF(Math.min(this.currentStartBoundsMasked.left, this.currentEndBoundsMasked.left), Math.min(this.currentStartBoundsMasked.top, this.currentEndBoundsMasked.top), Math.max(this.currentStartBoundsMasked.right, this.currentEndBoundsMasked.right), Math.max(this.currentStartBoundsMasked.bottom, this.currentEndBoundsMasked.bottom));
      this.maskEvaluator.evaluate(param1Float, this.startShapeAppearanceModel, this.endShapeAppearanceModel, this.currentStartBounds, this.currentStartBoundsMasked, this.currentEndBoundsMasked, this.progressThresholds.shapeMask);
      this.currentElevation = TransitionUtils.lerp(this.startElevation, this.endElevation, param1Float);
      f3 = calculateElevationDxMultiplier(this.currentMaskBounds, this.displayWidth);
      f2 = calculateElevationDyMultiplier(this.currentMaskBounds, this.displayHeight);
      f1 = this.currentElevation;
      f3 = (int)(f1 * f3);
      f2 = (int)(f1 * f2);
      this.currentElevationDy = f2;
      this.shadowPaint.setShadowLayer(f1, f3, f2, 754974720);
      f1 = ((Float)Preconditions.checkNotNull(Float.valueOf(this.progressThresholds.fade.start))).floatValue();
      f2 = ((Float)Preconditions.checkNotNull(Float.valueOf(this.progressThresholds.fade.end))).floatValue();
      this.fadeModeResult = this.fadeModeEvaluator.evaluate(param1Float, f1, f2, 0.35F);
      if (this.startContainerPaint.getColor() != 0)
        this.startContainerPaint.setAlpha(this.fadeModeResult.startAlpha); 
      if (this.endContainerPaint.getColor() != 0)
        this.endContainerPaint.setAlpha(this.fadeModeResult.endAlpha); 
      invalidateSelf();
    }
    
    public void draw(Canvas param1Canvas) {
      byte b;
      if (this.scrimPaint.getAlpha() > 0)
        param1Canvas.drawRect(getBounds(), this.scrimPaint); 
      if (this.drawDebugEnabled) {
        b = param1Canvas.save();
      } else {
        b = -1;
      } 
      if (this.elevationShadowEnabled && this.currentElevation > 0.0F)
        drawElevationShadow(param1Canvas); 
      this.maskEvaluator.clip(param1Canvas);
      maybeDrawContainerColor(param1Canvas, this.containerPaint);
      if (this.fadeModeResult.endOnTop) {
        drawStartView(param1Canvas);
        drawEndView(param1Canvas);
      } else {
        drawEndView(param1Canvas);
        drawStartView(param1Canvas);
      } 
      if (this.drawDebugEnabled) {
        param1Canvas.restoreToCount(b);
        drawDebugCumulativePath(param1Canvas, this.currentStartBounds, this.debugPath, -65281);
        drawDebugRect(param1Canvas, this.currentStartBoundsMasked, -256);
        drawDebugRect(param1Canvas, this.currentStartBounds, -16711936);
        drawDebugRect(param1Canvas, this.currentEndBoundsMasked, -16711681);
        drawDebugRect(param1Canvas, this.currentEndBounds, -16776961);
      } 
    }
    
    public int getOpacity() {
      return -3;
    }
    
    public void setAlpha(int param1Int) {
      throw new UnsupportedOperationException("Setting alpha on is not supported");
    }
    
    public void setColorFilter(ColorFilter param1ColorFilter) {
      throw new UnsupportedOperationException("Setting a color filter is not supported");
    }
  }
  
  class null implements TransitionUtils.CanvasOperation {
    final MaterialContainerTransform.TransitionDrawable this$0;
    
    public void run(Canvas param1Canvas) {
      this.this$0.startView.draw(param1Canvas);
    }
  }
  
  class null implements TransitionUtils.CanvasOperation {
    final MaterialContainerTransform.TransitionDrawable this$0;
    
    public void run(Canvas param1Canvas) {
      this.this$0.endView.draw(param1Canvas);
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\transition\platform\MaterialContainerTransform.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */