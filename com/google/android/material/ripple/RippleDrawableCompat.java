package com.google.android.material.ripple;

import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import androidx.core.graphics.drawable.TintAwareDrawable;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.android.material.shape.Shapeable;

public class RippleDrawableCompat extends Drawable implements Shapeable, TintAwareDrawable {
  private RippleDrawableCompatState drawableState;
  
  private RippleDrawableCompat(RippleDrawableCompatState paramRippleDrawableCompatState) {
    this.drawableState = paramRippleDrawableCompatState;
  }
  
  public RippleDrawableCompat(ShapeAppearanceModel paramShapeAppearanceModel) {
    this(new RippleDrawableCompatState(new MaterialShapeDrawable(paramShapeAppearanceModel)));
  }
  
  public void draw(Canvas paramCanvas) {
    if (this.drawableState.shouldDrawDelegate)
      this.drawableState.delegate.draw(paramCanvas); 
  }
  
  public Drawable.ConstantState getConstantState() {
    return this.drawableState;
  }
  
  public int getOpacity() {
    return this.drawableState.delegate.getOpacity();
  }
  
  public ShapeAppearanceModel getShapeAppearanceModel() {
    return this.drawableState.delegate.getShapeAppearanceModel();
  }
  
  public boolean isStateful() {
    return true;
  }
  
  public RippleDrawableCompat mutate() {
    this.drawableState = new RippleDrawableCompatState(this.drawableState);
    return this;
  }
  
  protected void onBoundsChange(Rect paramRect) {
    super.onBoundsChange(paramRect);
    this.drawableState.delegate.setBounds(paramRect);
  }
  
  protected boolean onStateChange(int[] paramArrayOfint) {
    boolean bool1 = super.onStateChange(paramArrayOfint);
    if (this.drawableState.delegate.setState(paramArrayOfint))
      bool1 = true; 
    boolean bool2 = RippleUtils.shouldDrawRippleCompat(paramArrayOfint);
    if (this.drawableState.shouldDrawDelegate != bool2) {
      this.drawableState.shouldDrawDelegate = bool2;
      bool1 = true;
    } 
    return bool1;
  }
  
  public void setAlpha(int paramInt) {
    this.drawableState.delegate.setAlpha(paramInt);
  }
  
  public void setColorFilter(ColorFilter paramColorFilter) {
    this.drawableState.delegate.setColorFilter(paramColorFilter);
  }
  
  public void setShapeAppearanceModel(ShapeAppearanceModel paramShapeAppearanceModel) {
    this.drawableState.delegate.setShapeAppearanceModel(paramShapeAppearanceModel);
  }
  
  public void setTint(int paramInt) {
    this.drawableState.delegate.setTint(paramInt);
  }
  
  public void setTintList(ColorStateList paramColorStateList) {
    this.drawableState.delegate.setTintList(paramColorStateList);
  }
  
  public void setTintMode(PorterDuff.Mode paramMode) {
    this.drawableState.delegate.setTintMode(paramMode);
  }
  
  static final class RippleDrawableCompatState extends Drawable.ConstantState {
    MaterialShapeDrawable delegate;
    
    boolean shouldDrawDelegate;
    
    public RippleDrawableCompatState(RippleDrawableCompatState param1RippleDrawableCompatState) {
      this.delegate = (MaterialShapeDrawable)param1RippleDrawableCompatState.delegate.getConstantState().newDrawable();
      this.shouldDrawDelegate = param1RippleDrawableCompatState.shouldDrawDelegate;
    }
    
    public RippleDrawableCompatState(MaterialShapeDrawable param1MaterialShapeDrawable) {
      this.delegate = param1MaterialShapeDrawable;
      this.shouldDrawDelegate = false;
    }
    
    public int getChangingConfigurations() {
      return 0;
    }
    
    public RippleDrawableCompat newDrawable() {
      return new RippleDrawableCompat(new RippleDrawableCompatState(this));
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\ripple\RippleDrawableCompat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */