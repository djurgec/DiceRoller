package com.google.android.material.floatingactionbutton;

import android.animation.Animator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.appcompat.widget.AppCompatDrawableManager;
import androidx.appcompat.widget.AppCompatImageHelper;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.util.Preconditions;
import androidx.core.view.TintableBackgroundView;
import androidx.core.view.ViewCompat;
import androidx.core.widget.TintableImageSourceView;
import com.google.android.material.R;
import com.google.android.material.animation.MotionSpec;
import com.google.android.material.animation.TransformationCallback;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.expandable.ExpandableTransformationWidget;
import com.google.android.material.expandable.ExpandableWidget;
import com.google.android.material.expandable.ExpandableWidgetHelper;
import com.google.android.material.internal.DescendantOffsetUtils;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.internal.ViewUtils;
import com.google.android.material.internal.VisibilityAwareImageButton;
import com.google.android.material.resources.MaterialResources;
import com.google.android.material.shadow.ShadowViewDelegate;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.android.material.shape.Shapeable;
import com.google.android.material.stateful.ExtendableSavedState;
import com.google.android.material.theme.overlay.MaterialThemeOverlay;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

public class FloatingActionButton extends VisibilityAwareImageButton implements TintableBackgroundView, TintableImageSourceView, ExpandableTransformationWidget, Shapeable, CoordinatorLayout.AttachedBehavior {
  private static final int AUTO_MINI_LARGEST_SCREEN_WIDTH = 470;
  
  private static final int DEF_STYLE_RES = R.style.Widget_Design_FloatingActionButton;
  
  private static final String EXPANDABLE_WIDGET_HELPER_KEY = "expandableWidgetHelper";
  
  private static final String LOG_TAG = "FloatingActionButton";
  
  public static final int NO_CUSTOM_SIZE = 0;
  
  public static final int SIZE_AUTO = -1;
  
  public static final int SIZE_MINI = 1;
  
  public static final int SIZE_NORMAL = 0;
  
  private ColorStateList backgroundTint;
  
  private PorterDuff.Mode backgroundTintMode;
  
  private int borderWidth;
  
  boolean compatPadding;
  
  private int customSize;
  
  private final ExpandableWidgetHelper expandableWidgetHelper;
  
  private final AppCompatImageHelper imageHelper;
  
  private PorterDuff.Mode imageMode;
  
  private int imagePadding;
  
  private ColorStateList imageTint;
  
  private FloatingActionButtonImpl impl;
  
  private int maxImageSize;
  
  private ColorStateList rippleColor;
  
  final Rect shadowPadding = new Rect();
  
  private int size;
  
  private final Rect touchArea = new Rect();
  
  public FloatingActionButton(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public FloatingActionButton(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, R.attr.floatingActionButtonStyle);
  }
  
  public FloatingActionButton(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(MaterialThemeOverlay.wrap(paramContext, paramAttributeSet, paramInt, j), paramAttributeSet, paramInt);
    Context context = getContext();
    TypedArray typedArray = ThemeEnforcement.obtainStyledAttributes(context, paramAttributeSet, R.styleable.FloatingActionButton, paramInt, j, new int[0]);
    this.backgroundTint = MaterialResources.getColorStateList(context, typedArray, R.styleable.FloatingActionButton_backgroundTint);
    this.backgroundTintMode = ViewUtils.parseTintMode(typedArray.getInt(R.styleable.FloatingActionButton_backgroundTintMode, -1), null);
    this.rippleColor = MaterialResources.getColorStateList(context, typedArray, R.styleable.FloatingActionButton_rippleColor);
    this.size = typedArray.getInt(R.styleable.FloatingActionButton_fabSize, -1);
    this.customSize = typedArray.getDimensionPixelSize(R.styleable.FloatingActionButton_fabCustomSize, 0);
    this.borderWidth = typedArray.getDimensionPixelSize(R.styleable.FloatingActionButton_borderWidth, 0);
    float f3 = typedArray.getDimension(R.styleable.FloatingActionButton_elevation, 0.0F);
    float f2 = typedArray.getDimension(R.styleable.FloatingActionButton_hoveredFocusedTranslationZ, 0.0F);
    float f1 = typedArray.getDimension(R.styleable.FloatingActionButton_pressedTranslationZ, 0.0F);
    this.compatPadding = typedArray.getBoolean(R.styleable.FloatingActionButton_useCompatPadding, false);
    int i = getResources().getDimensionPixelSize(R.dimen.mtrl_fab_min_touch_target);
    this.maxImageSize = typedArray.getDimensionPixelSize(R.styleable.FloatingActionButton_maxImageSize, 0);
    MotionSpec motionSpec2 = MotionSpec.createFromAttribute(context, typedArray, R.styleable.FloatingActionButton_showMotionSpec);
    MotionSpec motionSpec1 = MotionSpec.createFromAttribute(context, typedArray, R.styleable.FloatingActionButton_hideMotionSpec);
    ShapeAppearanceModel shapeAppearanceModel = ShapeAppearanceModel.builder(context, paramAttributeSet, paramInt, j, ShapeAppearanceModel.PILL).build();
    boolean bool = typedArray.getBoolean(R.styleable.FloatingActionButton_ensureMinTouchTargetSize, false);
    setEnabled(typedArray.getBoolean(R.styleable.FloatingActionButton_android_enabled, true));
    typedArray.recycle();
    AppCompatImageHelper appCompatImageHelper = new AppCompatImageHelper((ImageView)this);
    this.imageHelper = appCompatImageHelper;
    appCompatImageHelper.loadFromAttributes(paramAttributeSet, paramInt);
    this.expandableWidgetHelper = new ExpandableWidgetHelper((ExpandableWidget)this);
    getImpl().setShapeAppearance(shapeAppearanceModel);
    getImpl().initializeBackgroundDrawable(this.backgroundTint, this.backgroundTintMode, this.rippleColor, this.borderWidth);
    getImpl().setMinTouchTargetSize(i);
    getImpl().setElevation(f3);
    getImpl().setHoveredFocusedTranslationZ(f2);
    getImpl().setPressedTranslationZ(f1);
    getImpl().setMaxImageSize(this.maxImageSize);
    getImpl().setShowMotionSpec(motionSpec2);
    getImpl().setHideMotionSpec(motionSpec1);
    getImpl().setEnsureMinTouchTargetSize(bool);
    setScaleType(ImageView.ScaleType.MATRIX);
  }
  
  private FloatingActionButtonImpl createImpl() {
    return (Build.VERSION.SDK_INT >= 21) ? new FloatingActionButtonImplLollipop(this, new ShadowDelegateImpl()) : new FloatingActionButtonImpl(this, new ShadowDelegateImpl());
  }
  
  private FloatingActionButtonImpl getImpl() {
    if (this.impl == null)
      this.impl = createImpl(); 
    return this.impl;
  }
  
  private int getSizeDimension(int paramInt) {
    int i = this.customSize;
    if (i != 0)
      return i; 
    Resources resources = getResources();
    switch (paramInt) {
      default:
        return resources.getDimensionPixelSize(R.dimen.design_fab_size_normal);
      case 1:
        return resources.getDimensionPixelSize(R.dimen.design_fab_size_mini);
      case -1:
        break;
    } 
    if (Math.max((resources.getConfiguration()).screenWidthDp, (resources.getConfiguration()).screenHeightDp) < 470) {
      paramInt = getSizeDimension(1);
    } else {
      paramInt = getSizeDimension(0);
    } 
    return paramInt;
  }
  
  private void offsetRectWithShadow(Rect paramRect) {
    paramRect.left += this.shadowPadding.left;
    paramRect.top += this.shadowPadding.top;
    paramRect.right -= this.shadowPadding.right;
    paramRect.bottom -= this.shadowPadding.bottom;
  }
  
  private void onApplySupportImageTint() {
    Drawable drawable = getDrawable();
    if (drawable == null)
      return; 
    ColorStateList colorStateList = this.imageTint;
    if (colorStateList == null) {
      DrawableCompat.clearColorFilter(drawable);
      return;
    } 
    int i = colorStateList.getColorForState(getDrawableState(), 0);
    PorterDuff.Mode mode2 = this.imageMode;
    PorterDuff.Mode mode1 = mode2;
    if (mode2 == null)
      mode1 = PorterDuff.Mode.SRC_IN; 
    drawable.mutate().setColorFilter((ColorFilter)AppCompatDrawableManager.getPorterDuffColorFilter(i, mode1));
  }
  
  private static int resolveAdjustedSize(int paramInt1, int paramInt2) {
    int i = View.MeasureSpec.getMode(paramInt2);
    paramInt2 = View.MeasureSpec.getSize(paramInt2);
    switch (i) {
      default:
        throw new IllegalArgumentException();
      case 1073741824:
        paramInt1 = paramInt2;
      case 0:
        return paramInt1;
      case -2147483648:
        break;
    } 
    paramInt1 = Math.min(paramInt1, paramInt2);
  }
  
  private FloatingActionButtonImpl.InternalVisibilityChangedListener wrapOnVisibilityChangedListener(final OnVisibilityChangedListener listener) {
    return (listener == null) ? null : new FloatingActionButtonImpl.InternalVisibilityChangedListener() {
        final FloatingActionButton this$0;
        
        final FloatingActionButton.OnVisibilityChangedListener val$listener;
        
        public void onHidden() {
          listener.onHidden(FloatingActionButton.this);
        }
        
        public void onShown() {
          listener.onShown(FloatingActionButton.this);
        }
      };
  }
  
  public void addOnHideAnimationListener(Animator.AnimatorListener paramAnimatorListener) {
    getImpl().addOnHideAnimationListener(paramAnimatorListener);
  }
  
  public void addOnShowAnimationListener(Animator.AnimatorListener paramAnimatorListener) {
    getImpl().addOnShowAnimationListener(paramAnimatorListener);
  }
  
  public void addTransformationCallback(TransformationCallback<? extends FloatingActionButton> paramTransformationCallback) {
    getImpl().addTransformationCallback(new TransformationCallbackWrapper<>(paramTransformationCallback));
  }
  
  public void clearCustomSize() {
    setCustomSize(0);
  }
  
  protected void drawableStateChanged() {
    super.drawableStateChanged();
    getImpl().onDrawableStateChanged(getDrawableState());
  }
  
  public ColorStateList getBackgroundTintList() {
    return this.backgroundTint;
  }
  
  public PorterDuff.Mode getBackgroundTintMode() {
    return this.backgroundTintMode;
  }
  
  public CoordinatorLayout.Behavior<FloatingActionButton> getBehavior() {
    return new Behavior();
  }
  
  public float getCompatElevation() {
    return getImpl().getElevation();
  }
  
  public float getCompatHoveredFocusedTranslationZ() {
    return getImpl().getHoveredFocusedTranslationZ();
  }
  
  public float getCompatPressedTranslationZ() {
    return getImpl().getPressedTranslationZ();
  }
  
  public Drawable getContentBackground() {
    return getImpl().getContentBackground();
  }
  
  @Deprecated
  public boolean getContentRect(Rect paramRect) {
    if (ViewCompat.isLaidOut((View)this)) {
      paramRect.set(0, 0, getWidth(), getHeight());
      offsetRectWithShadow(paramRect);
      return true;
    } 
    return false;
  }
  
  public int getCustomSize() {
    return this.customSize;
  }
  
  public int getExpandedComponentIdHint() {
    return this.expandableWidgetHelper.getExpandedComponentIdHint();
  }
  
  public MotionSpec getHideMotionSpec() {
    return getImpl().getHideMotionSpec();
  }
  
  public void getMeasuredContentRect(Rect paramRect) {
    paramRect.set(0, 0, getMeasuredWidth(), getMeasuredHeight());
    offsetRectWithShadow(paramRect);
  }
  
  @Deprecated
  public int getRippleColor() {
    boolean bool;
    ColorStateList colorStateList = this.rippleColor;
    if (colorStateList != null) {
      bool = colorStateList.getDefaultColor();
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public ColorStateList getRippleColorStateList() {
    return this.rippleColor;
  }
  
  public ShapeAppearanceModel getShapeAppearanceModel() {
    return (ShapeAppearanceModel)Preconditions.checkNotNull(getImpl().getShapeAppearance());
  }
  
  public MotionSpec getShowMotionSpec() {
    return getImpl().getShowMotionSpec();
  }
  
  public int getSize() {
    return this.size;
  }
  
  int getSizeDimension() {
    return getSizeDimension(this.size);
  }
  
  public ColorStateList getSupportBackgroundTintList() {
    return getBackgroundTintList();
  }
  
  public PorterDuff.Mode getSupportBackgroundTintMode() {
    return getBackgroundTintMode();
  }
  
  public ColorStateList getSupportImageTintList() {
    return this.imageTint;
  }
  
  public PorterDuff.Mode getSupportImageTintMode() {
    return this.imageMode;
  }
  
  public boolean getUseCompatPadding() {
    return this.compatPadding;
  }
  
  public void hide() {
    hide((OnVisibilityChangedListener)null);
  }
  
  public void hide(OnVisibilityChangedListener paramOnVisibilityChangedListener) {
    hide(paramOnVisibilityChangedListener, true);
  }
  
  void hide(OnVisibilityChangedListener paramOnVisibilityChangedListener, boolean paramBoolean) {
    getImpl().hide(wrapOnVisibilityChangedListener(paramOnVisibilityChangedListener), paramBoolean);
  }
  
  public boolean isExpanded() {
    return this.expandableWidgetHelper.isExpanded();
  }
  
  public boolean isOrWillBeHidden() {
    return getImpl().isOrWillBeHidden();
  }
  
  public boolean isOrWillBeShown() {
    return getImpl().isOrWillBeShown();
  }
  
  public void jumpDrawablesToCurrentState() {
    super.jumpDrawablesToCurrentState();
    getImpl().jumpDrawableToCurrentState();
  }
  
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    getImpl().onAttachedToWindow();
  }
  
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    getImpl().onDetachedFromWindow();
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    int i = getSizeDimension();
    this.imagePadding = (i - this.maxImageSize) / 2;
    getImpl().updatePadding();
    paramInt1 = Math.min(resolveAdjustedSize(i, paramInt1), resolveAdjustedSize(i, paramInt2));
    setMeasuredDimension(this.shadowPadding.left + paramInt1 + this.shadowPadding.right, this.shadowPadding.top + paramInt1 + this.shadowPadding.bottom);
  }
  
  protected void onRestoreInstanceState(Parcelable paramParcelable) {
    if (!(paramParcelable instanceof ExtendableSavedState)) {
      super.onRestoreInstanceState(paramParcelable);
      return;
    } 
    ExtendableSavedState extendableSavedState = (ExtendableSavedState)paramParcelable;
    super.onRestoreInstanceState(extendableSavedState.getSuperState());
    this.expandableWidgetHelper.onRestoreInstanceState((Bundle)Preconditions.checkNotNull(extendableSavedState.extendableStates.get("expandableWidgetHelper")));
  }
  
  protected Parcelable onSaveInstanceState() {
    Bundle bundle;
    Parcelable parcelable2 = super.onSaveInstanceState();
    Parcelable parcelable1 = parcelable2;
    if (parcelable2 == null)
      bundle = new Bundle(); 
    ExtendableSavedState extendableSavedState = new ExtendableSavedState((Parcelable)bundle);
    extendableSavedState.extendableStates.put("expandableWidgetHelper", this.expandableWidgetHelper.onSaveInstanceState());
    return (Parcelable)extendableSavedState;
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    return (paramMotionEvent.getAction() == 0 && getContentRect(this.touchArea) && !this.touchArea.contains((int)paramMotionEvent.getX(), (int)paramMotionEvent.getY())) ? false : super.onTouchEvent(paramMotionEvent);
  }
  
  public void removeOnHideAnimationListener(Animator.AnimatorListener paramAnimatorListener) {
    getImpl().removeOnHideAnimationListener(paramAnimatorListener);
  }
  
  public void removeOnShowAnimationListener(Animator.AnimatorListener paramAnimatorListener) {
    getImpl().removeOnShowAnimationListener(paramAnimatorListener);
  }
  
  public void removeTransformationCallback(TransformationCallback<? extends FloatingActionButton> paramTransformationCallback) {
    getImpl().removeTransformationCallback(new TransformationCallbackWrapper<>(paramTransformationCallback));
  }
  
  public void setBackgroundColor(int paramInt) {
    Log.i("FloatingActionButton", "Setting a custom background is not supported.");
  }
  
  public void setBackgroundDrawable(Drawable paramDrawable) {
    Log.i("FloatingActionButton", "Setting a custom background is not supported.");
  }
  
  public void setBackgroundResource(int paramInt) {
    Log.i("FloatingActionButton", "Setting a custom background is not supported.");
  }
  
  public void setBackgroundTintList(ColorStateList paramColorStateList) {
    if (this.backgroundTint != paramColorStateList) {
      this.backgroundTint = paramColorStateList;
      getImpl().setBackgroundTintList(paramColorStateList);
    } 
  }
  
  public void setBackgroundTintMode(PorterDuff.Mode paramMode) {
    if (this.backgroundTintMode != paramMode) {
      this.backgroundTintMode = paramMode;
      getImpl().setBackgroundTintMode(paramMode);
    } 
  }
  
  public void setCompatElevation(float paramFloat) {
    getImpl().setElevation(paramFloat);
  }
  
  public void setCompatElevationResource(int paramInt) {
    setCompatElevation(getResources().getDimension(paramInt));
  }
  
  public void setCompatHoveredFocusedTranslationZ(float paramFloat) {
    getImpl().setHoveredFocusedTranslationZ(paramFloat);
  }
  
  public void setCompatHoveredFocusedTranslationZResource(int paramInt) {
    setCompatHoveredFocusedTranslationZ(getResources().getDimension(paramInt));
  }
  
  public void setCompatPressedTranslationZ(float paramFloat) {
    getImpl().setPressedTranslationZ(paramFloat);
  }
  
  public void setCompatPressedTranslationZResource(int paramInt) {
    setCompatPressedTranslationZ(getResources().getDimension(paramInt));
  }
  
  public void setCustomSize(int paramInt) {
    if (paramInt >= 0) {
      if (paramInt != this.customSize) {
        this.customSize = paramInt;
        requestLayout();
      } 
      return;
    } 
    throw new IllegalArgumentException("Custom size must be non-negative");
  }
  
  public void setElevation(float paramFloat) {
    super.setElevation(paramFloat);
    getImpl().updateShapeElevation(paramFloat);
  }
  
  public void setEnsureMinTouchTargetSize(boolean paramBoolean) {
    if (paramBoolean != getImpl().getEnsureMinTouchTargetSize()) {
      getImpl().setEnsureMinTouchTargetSize(paramBoolean);
      requestLayout();
    } 
  }
  
  public boolean setExpanded(boolean paramBoolean) {
    return this.expandableWidgetHelper.setExpanded(paramBoolean);
  }
  
  public void setExpandedComponentIdHint(int paramInt) {
    this.expandableWidgetHelper.setExpandedComponentIdHint(paramInt);
  }
  
  public void setHideMotionSpec(MotionSpec paramMotionSpec) {
    getImpl().setHideMotionSpec(paramMotionSpec);
  }
  
  public void setHideMotionSpecResource(int paramInt) {
    setHideMotionSpec(MotionSpec.createFromResource(getContext(), paramInt));
  }
  
  public void setImageDrawable(Drawable paramDrawable) {
    if (getDrawable() != paramDrawable) {
      super.setImageDrawable(paramDrawable);
      getImpl().updateImageMatrixScale();
      if (this.imageTint != null)
        onApplySupportImageTint(); 
    } 
  }
  
  public void setImageResource(int paramInt) {
    this.imageHelper.setImageResource(paramInt);
    onApplySupportImageTint();
  }
  
  public void setRippleColor(int paramInt) {
    setRippleColor(ColorStateList.valueOf(paramInt));
  }
  
  public void setRippleColor(ColorStateList paramColorStateList) {
    if (this.rippleColor != paramColorStateList) {
      this.rippleColor = paramColorStateList;
      getImpl().setRippleColor(this.rippleColor);
    } 
  }
  
  public void setScaleX(float paramFloat) {
    super.setScaleX(paramFloat);
    getImpl().onScaleChanged();
  }
  
  public void setScaleY(float paramFloat) {
    super.setScaleY(paramFloat);
    getImpl().onScaleChanged();
  }
  
  public void setShadowPaddingEnabled(boolean paramBoolean) {
    getImpl().setShadowPaddingEnabled(paramBoolean);
  }
  
  public void setShapeAppearanceModel(ShapeAppearanceModel paramShapeAppearanceModel) {
    getImpl().setShapeAppearance(paramShapeAppearanceModel);
  }
  
  public void setShowMotionSpec(MotionSpec paramMotionSpec) {
    getImpl().setShowMotionSpec(paramMotionSpec);
  }
  
  public void setShowMotionSpecResource(int paramInt) {
    setShowMotionSpec(MotionSpec.createFromResource(getContext(), paramInt));
  }
  
  public void setSize(int paramInt) {
    this.customSize = 0;
    if (paramInt != this.size) {
      this.size = paramInt;
      requestLayout();
    } 
  }
  
  public void setSupportBackgroundTintList(ColorStateList paramColorStateList) {
    setBackgroundTintList(paramColorStateList);
  }
  
  public void setSupportBackgroundTintMode(PorterDuff.Mode paramMode) {
    setBackgroundTintMode(paramMode);
  }
  
  public void setSupportImageTintList(ColorStateList paramColorStateList) {
    if (this.imageTint != paramColorStateList) {
      this.imageTint = paramColorStateList;
      onApplySupportImageTint();
    } 
  }
  
  public void setSupportImageTintMode(PorterDuff.Mode paramMode) {
    if (this.imageMode != paramMode) {
      this.imageMode = paramMode;
      onApplySupportImageTint();
    } 
  }
  
  public void setTranslationX(float paramFloat) {
    super.setTranslationX(paramFloat);
    getImpl().onTranslationChanged();
  }
  
  public void setTranslationY(float paramFloat) {
    super.setTranslationY(paramFloat);
    getImpl().onTranslationChanged();
  }
  
  public void setTranslationZ(float paramFloat) {
    super.setTranslationZ(paramFloat);
    getImpl().onTranslationChanged();
  }
  
  public void setUseCompatPadding(boolean paramBoolean) {
    if (this.compatPadding != paramBoolean) {
      this.compatPadding = paramBoolean;
      getImpl().onCompatShadowChanged();
    } 
  }
  
  public void setVisibility(int paramInt) {
    super.setVisibility(paramInt);
  }
  
  public boolean shouldEnsureMinTouchTargetSize() {
    return getImpl().getEnsureMinTouchTargetSize();
  }
  
  public void show() {
    show((OnVisibilityChangedListener)null);
  }
  
  public void show(OnVisibilityChangedListener paramOnVisibilityChangedListener) {
    show(paramOnVisibilityChangedListener, true);
  }
  
  void show(OnVisibilityChangedListener paramOnVisibilityChangedListener, boolean paramBoolean) {
    getImpl().show(wrapOnVisibilityChangedListener(paramOnVisibilityChangedListener), paramBoolean);
  }
  
  protected static class BaseBehavior<T extends FloatingActionButton> extends CoordinatorLayout.Behavior<T> {
    private static final boolean AUTO_HIDE_DEFAULT = true;
    
    private boolean autoHideEnabled;
    
    private FloatingActionButton.OnVisibilityChangedListener internalAutoHideListener;
    
    private Rect tmpRect;
    
    public BaseBehavior() {
      this.autoHideEnabled = true;
    }
    
    public BaseBehavior(Context param1Context, AttributeSet param1AttributeSet) {
      super(param1Context, param1AttributeSet);
      TypedArray typedArray = param1Context.obtainStyledAttributes(param1AttributeSet, R.styleable.FloatingActionButton_Behavior_Layout);
      this.autoHideEnabled = typedArray.getBoolean(R.styleable.FloatingActionButton_Behavior_Layout_behavior_autoHide, true);
      typedArray.recycle();
    }
    
    private static boolean isBottomSheet(View param1View) {
      ViewGroup.LayoutParams layoutParams = param1View.getLayoutParams();
      return (layoutParams instanceof CoordinatorLayout.LayoutParams) ? (((CoordinatorLayout.LayoutParams)layoutParams).getBehavior() instanceof com.google.android.material.bottomsheet.BottomSheetBehavior) : false;
    }
    
    private void offsetIfNeeded(CoordinatorLayout param1CoordinatorLayout, FloatingActionButton param1FloatingActionButton) {
      Rect rect = param1FloatingActionButton.shadowPadding;
      if (rect != null && rect.centerX() > 0 && rect.centerY() > 0) {
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams)param1FloatingActionButton.getLayoutParams();
        int j = 0;
        int i = 0;
        if (param1FloatingActionButton.getRight() >= param1CoordinatorLayout.getWidth() - layoutParams.rightMargin) {
          i = rect.right;
        } else if (param1FloatingActionButton.getLeft() <= layoutParams.leftMargin) {
          i = -rect.left;
        } 
        if (param1FloatingActionButton.getBottom() >= param1CoordinatorLayout.getHeight() - layoutParams.bottomMargin) {
          j = rect.bottom;
        } else if (param1FloatingActionButton.getTop() <= layoutParams.topMargin) {
          j = -rect.top;
        } 
        if (j != 0)
          ViewCompat.offsetTopAndBottom((View)param1FloatingActionButton, j); 
        if (i != 0)
          ViewCompat.offsetLeftAndRight((View)param1FloatingActionButton, i); 
      } 
    }
    
    private boolean shouldUpdateVisibility(View param1View, FloatingActionButton param1FloatingActionButton) {
      CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams)param1FloatingActionButton.getLayoutParams();
      return !this.autoHideEnabled ? false : ((layoutParams.getAnchorId() != param1View.getId()) ? false : (!(param1FloatingActionButton.getUserSetVisibility() != 0)));
    }
    
    private boolean updateFabVisibilityForAppBarLayout(CoordinatorLayout param1CoordinatorLayout, AppBarLayout param1AppBarLayout, FloatingActionButton param1FloatingActionButton) {
      if (!shouldUpdateVisibility((View)param1AppBarLayout, param1FloatingActionButton))
        return false; 
      if (this.tmpRect == null)
        this.tmpRect = new Rect(); 
      Rect rect = this.tmpRect;
      DescendantOffsetUtils.getDescendantRect((ViewGroup)param1CoordinatorLayout, (View)param1AppBarLayout, rect);
      if (rect.bottom <= param1AppBarLayout.getMinimumHeightForVisibleOverlappingContent()) {
        param1FloatingActionButton.hide(this.internalAutoHideListener, false);
      } else {
        param1FloatingActionButton.show(this.internalAutoHideListener, false);
      } 
      return true;
    }
    
    private boolean updateFabVisibilityForBottomSheet(View param1View, FloatingActionButton param1FloatingActionButton) {
      if (!shouldUpdateVisibility(param1View, param1FloatingActionButton))
        return false; 
      CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams)param1FloatingActionButton.getLayoutParams();
      if (param1View.getTop() < param1FloatingActionButton.getHeight() / 2 + layoutParams.topMargin) {
        param1FloatingActionButton.hide(this.internalAutoHideListener, false);
      } else {
        param1FloatingActionButton.show(this.internalAutoHideListener, false);
      } 
      return true;
    }
    
    public boolean getInsetDodgeRect(CoordinatorLayout param1CoordinatorLayout, FloatingActionButton param1FloatingActionButton, Rect param1Rect) {
      Rect rect = param1FloatingActionButton.shadowPadding;
      param1Rect.set(param1FloatingActionButton.getLeft() + rect.left, param1FloatingActionButton.getTop() + rect.top, param1FloatingActionButton.getRight() - rect.right, param1FloatingActionButton.getBottom() - rect.bottom);
      return true;
    }
    
    public boolean isAutoHideEnabled() {
      return this.autoHideEnabled;
    }
    
    public void onAttachedToLayoutParams(CoordinatorLayout.LayoutParams param1LayoutParams) {
      if (param1LayoutParams.dodgeInsetEdges == 0)
        param1LayoutParams.dodgeInsetEdges = 80; 
    }
    
    public boolean onDependentViewChanged(CoordinatorLayout param1CoordinatorLayout, FloatingActionButton param1FloatingActionButton, View param1View) {
      if (param1View instanceof AppBarLayout) {
        updateFabVisibilityForAppBarLayout(param1CoordinatorLayout, (AppBarLayout)param1View, param1FloatingActionButton);
      } else if (isBottomSheet(param1View)) {
        updateFabVisibilityForBottomSheet(param1View, param1FloatingActionButton);
      } 
      return false;
    }
    
    public boolean onLayoutChild(CoordinatorLayout param1CoordinatorLayout, FloatingActionButton param1FloatingActionButton, int param1Int) {
      List<View> list = param1CoordinatorLayout.getDependencies((View)param1FloatingActionButton);
      byte b = 0;
      int i = list.size();
      while (b < i) {
        View view = list.get(b);
        if ((view instanceof AppBarLayout) ? updateFabVisibilityForAppBarLayout(param1CoordinatorLayout, (AppBarLayout)view, param1FloatingActionButton) : (isBottomSheet(view) && updateFabVisibilityForBottomSheet(view, param1FloatingActionButton)))
          break; 
        b++;
      } 
      param1CoordinatorLayout.onLayoutChild((View)param1FloatingActionButton, param1Int);
      offsetIfNeeded(param1CoordinatorLayout, param1FloatingActionButton);
      return true;
    }
    
    public void setAutoHideEnabled(boolean param1Boolean) {
      this.autoHideEnabled = param1Boolean;
    }
    
    public void setInternalAutoHideListener(FloatingActionButton.OnVisibilityChangedListener param1OnVisibilityChangedListener) {
      this.internalAutoHideListener = param1OnVisibilityChangedListener;
    }
  }
  
  public static class Behavior extends BaseBehavior<FloatingActionButton> {
    public Behavior() {}
    
    public Behavior(Context param1Context, AttributeSet param1AttributeSet) {
      super(param1Context, param1AttributeSet);
    }
  }
  
  public static abstract class OnVisibilityChangedListener {
    public void onHidden(FloatingActionButton param1FloatingActionButton) {}
    
    public void onShown(FloatingActionButton param1FloatingActionButton) {}
  }
  
  private class ShadowDelegateImpl implements ShadowViewDelegate {
    final FloatingActionButton this$0;
    
    public float getRadius() {
      return FloatingActionButton.this.getSizeDimension() / 2.0F;
    }
    
    public boolean isCompatPaddingEnabled() {
      return FloatingActionButton.this.compatPadding;
    }
    
    public void setBackgroundDrawable(Drawable param1Drawable) {
      if (param1Drawable != null)
        FloatingActionButton.this.setBackgroundDrawable(param1Drawable); 
    }
    
    public void setShadowPadding(int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
      FloatingActionButton.this.shadowPadding.set(param1Int1, param1Int2, param1Int3, param1Int4);
      FloatingActionButton floatingActionButton = FloatingActionButton.this;
      floatingActionButton.setPadding(floatingActionButton.imagePadding + param1Int1, FloatingActionButton.this.imagePadding + param1Int2, FloatingActionButton.this.imagePadding + param1Int3, FloatingActionButton.this.imagePadding + param1Int4);
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface Size {}
  
  class TransformationCallbackWrapper<T extends FloatingActionButton> implements FloatingActionButtonImpl.InternalTransformationCallback {
    private final TransformationCallback<T> listener;
    
    final FloatingActionButton this$0;
    
    TransformationCallbackWrapper(TransformationCallback<T> param1TransformationCallback) {
      this.listener = param1TransformationCallback;
    }
    
    public boolean equals(Object param1Object) {
      boolean bool;
      if (param1Object instanceof TransformationCallbackWrapper && ((TransformationCallbackWrapper)param1Object).listener.equals(this.listener)) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public int hashCode() {
      return this.listener.hashCode();
    }
    
    public void onScaleChanged() {
      this.listener.onScaleChanged((View)FloatingActionButton.this);
    }
    
    public void onTranslationChanged() {
      this.listener.onTranslationChanged((View)FloatingActionButton.this);
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\floatingactionbutton\FloatingActionButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */