package com.google.android.material.floatingactionbutton;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.view.View;
import androidx.core.content.ContextCompat;
import androidx.core.util.Preconditions;
import com.google.android.material.R;
import com.google.android.material.ripple.RippleUtils;
import com.google.android.material.shadow.ShadowViewDelegate;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;
import java.util.ArrayList;

class FloatingActionButtonImplLollipop extends FloatingActionButtonImpl {
  FloatingActionButtonImplLollipop(FloatingActionButton paramFloatingActionButton, ShadowViewDelegate paramShadowViewDelegate) {
    super(paramFloatingActionButton, paramShadowViewDelegate);
  }
  
  private Animator createElevationAnimator(float paramFloat1, float paramFloat2) {
    AnimatorSet animatorSet = new AnimatorSet();
    animatorSet.play((Animator)ObjectAnimator.ofFloat(this.view, "elevation", new float[] { paramFloat1 }).setDuration(0L)).with((Animator)ObjectAnimator.ofFloat(this.view, View.TRANSLATION_Z, new float[] { paramFloat2 }).setDuration(100L));
    animatorSet.setInterpolator(ELEVATION_ANIM_INTERPOLATOR);
    return (Animator)animatorSet;
  }
  
  BorderDrawable createBorderDrawable(int paramInt, ColorStateList paramColorStateList) {
    Context context = this.view.getContext();
    BorderDrawable borderDrawable = new BorderDrawable((ShapeAppearanceModel)Preconditions.checkNotNull(this.shapeAppearance));
    borderDrawable.setGradientColors(ContextCompat.getColor(context, R.color.design_fab_stroke_top_outer_color), ContextCompat.getColor(context, R.color.design_fab_stroke_top_inner_color), ContextCompat.getColor(context, R.color.design_fab_stroke_end_inner_color), ContextCompat.getColor(context, R.color.design_fab_stroke_end_outer_color));
    borderDrawable.setBorderWidth(paramInt);
    borderDrawable.setBorderTint(paramColorStateList);
    return borderDrawable;
  }
  
  MaterialShapeDrawable createShapeDrawable() {
    return new AlwaysStatefulMaterialShapeDrawable((ShapeAppearanceModel)Preconditions.checkNotNull(this.shapeAppearance));
  }
  
  public float getElevation() {
    return this.view.getElevation();
  }
  
  void getPadding(Rect paramRect) {
    if (this.shadowViewDelegate.isCompatPaddingEnabled()) {
      super.getPadding(paramRect);
    } else if (!shouldExpandBoundsForA11y()) {
      int i = (this.minTouchTargetSize - this.view.getSizeDimension()) / 2;
      paramRect.set(i, i, i, i);
    } else {
      paramRect.set(0, 0, 0, 0);
    } 
  }
  
  void initializeBackgroundDrawable(ColorStateList paramColorStateList1, PorterDuff.Mode paramMode, ColorStateList paramColorStateList2, int paramInt) {
    MaterialShapeDrawable materialShapeDrawable;
    this.shapeDrawable = createShapeDrawable();
    this.shapeDrawable.setTintList(paramColorStateList1);
    if (paramMode != null)
      this.shapeDrawable.setTintMode(paramMode); 
    this.shapeDrawable.initializeElevationOverlay(this.view.getContext());
    if (paramInt > 0) {
      this.borderDrawable = createBorderDrawable(paramInt, paramColorStateList1);
      LayerDrawable layerDrawable = new LayerDrawable(new Drawable[] { (Drawable)Preconditions.checkNotNull(this.borderDrawable), (Drawable)Preconditions.checkNotNull(this.shapeDrawable) });
    } else {
      this.borderDrawable = null;
      materialShapeDrawable = this.shapeDrawable;
    } 
    this.rippleDrawable = (Drawable)new RippleDrawable(RippleUtils.sanitizeRippleDrawableColor(paramColorStateList2), (Drawable)materialShapeDrawable, null);
    this.contentBackground = this.rippleDrawable;
  }
  
  void jumpDrawableToCurrentState() {}
  
  void onCompatShadowChanged() {
    updatePadding();
  }
  
  void onDrawableStateChanged(int[] paramArrayOfint) {
    if (Build.VERSION.SDK_INT == 21)
      if (this.view.isEnabled()) {
        this.view.setElevation(this.elevation);
        if (this.view.isPressed()) {
          this.view.setTranslationZ(this.pressedTranslationZ);
        } else {
          if (this.view.isFocused() || this.view.isHovered()) {
            this.view.setTranslationZ(this.hoveredFocusedTranslationZ);
            return;
          } 
          this.view.setTranslationZ(0.0F);
        } 
      } else {
        this.view.setElevation(0.0F);
        this.view.setTranslationZ(0.0F);
      }  
  }
  
  void onElevationsChanged(float paramFloat1, float paramFloat2, float paramFloat3) {
    if (Build.VERSION.SDK_INT == 21) {
      this.view.refreshDrawableState();
    } else {
      StateListAnimator stateListAnimator = new StateListAnimator();
      stateListAnimator.addState(PRESSED_ENABLED_STATE_SET, createElevationAnimator(paramFloat1, paramFloat3));
      stateListAnimator.addState(HOVERED_FOCUSED_ENABLED_STATE_SET, createElevationAnimator(paramFloat1, paramFloat2));
      stateListAnimator.addState(FOCUSED_ENABLED_STATE_SET, createElevationAnimator(paramFloat1, paramFloat2));
      stateListAnimator.addState(HOVERED_ENABLED_STATE_SET, createElevationAnimator(paramFloat1, paramFloat2));
      AnimatorSet animatorSet = new AnimatorSet();
      ArrayList<ObjectAnimator> arrayList = new ArrayList();
      arrayList.add(ObjectAnimator.ofFloat(this.view, "elevation", new float[] { paramFloat1 }).setDuration(0L));
      if (Build.VERSION.SDK_INT >= 22 && Build.VERSION.SDK_INT <= 24)
        arrayList.add(ObjectAnimator.ofFloat(this.view, View.TRANSLATION_Z, new float[] { this.view.getTranslationZ() }).setDuration(100L)); 
      arrayList.add(ObjectAnimator.ofFloat(this.view, View.TRANSLATION_Z, new float[] { 0.0F }).setDuration(100L));
      animatorSet.playSequentially(arrayList.<Animator>toArray(new Animator[0]));
      animatorSet.setInterpolator(ELEVATION_ANIM_INTERPOLATOR);
      stateListAnimator.addState(ENABLED_STATE_SET, (Animator)animatorSet);
      stateListAnimator.addState(EMPTY_STATE_SET, createElevationAnimator(0.0F, 0.0F));
      this.view.setStateListAnimator(stateListAnimator);
    } 
    if (shouldAddPadding())
      updatePadding(); 
  }
  
  boolean requirePreDrawListener() {
    return false;
  }
  
  void setRippleColor(ColorStateList paramColorStateList) {
    if (this.rippleDrawable instanceof RippleDrawable) {
      ((RippleDrawable)this.rippleDrawable).setColor(RippleUtils.sanitizeRippleDrawableColor(paramColorStateList));
    } else {
      super.setRippleColor(paramColorStateList);
    } 
  }
  
  boolean shouldAddPadding() {
    return (this.shadowViewDelegate.isCompatPaddingEnabled() || !shouldExpandBoundsForA11y());
  }
  
  void updateFromViewRotation() {}
  
  static class AlwaysStatefulMaterialShapeDrawable extends MaterialShapeDrawable {
    AlwaysStatefulMaterialShapeDrawable(ShapeAppearanceModel param1ShapeAppearanceModel) {
      super(param1ShapeAppearanceModel);
    }
    
    public boolean isStateful() {
      return true;
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\floatingactionbutton\FloatingActionButtonImplLollipop.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */