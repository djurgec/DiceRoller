package com.google.android.material.button;

import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.view.View;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ViewCompat;
import com.google.android.material.R;
import com.google.android.material.color.MaterialColors;
import com.google.android.material.internal.ViewUtils;
import com.google.android.material.resources.MaterialResources;
import com.google.android.material.ripple.RippleDrawableCompat;
import com.google.android.material.ripple.RippleUtils;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.android.material.shape.Shapeable;

class MaterialButtonHelper {
  private static final boolean IS_LOLLIPOP;
  
  private boolean backgroundOverwritten = false;
  
  private ColorStateList backgroundTint;
  
  private PorterDuff.Mode backgroundTintMode;
  
  private boolean checkable;
  
  private int cornerRadius;
  
  private boolean cornerRadiusSet = false;
  
  private int elevation;
  
  private int insetBottom;
  
  private int insetLeft;
  
  private int insetRight;
  
  private int insetTop;
  
  private Drawable maskDrawable;
  
  private final MaterialButton materialButton;
  
  private ColorStateList rippleColor;
  
  private LayerDrawable rippleDrawable;
  
  private ShapeAppearanceModel shapeAppearanceModel;
  
  private boolean shouldDrawSurfaceColorStroke = false;
  
  private ColorStateList strokeColor;
  
  private int strokeWidth;
  
  static {
    boolean bool;
    if (Build.VERSION.SDK_INT >= 21) {
      bool = true;
    } else {
      bool = false;
    } 
    IS_LOLLIPOP = bool;
  }
  
  MaterialButtonHelper(MaterialButton paramMaterialButton, ShapeAppearanceModel paramShapeAppearanceModel) {
    this.materialButton = paramMaterialButton;
    this.shapeAppearanceModel = paramShapeAppearanceModel;
  }
  
  private Drawable createBackground() {
    boolean bool;
    RippleDrawable rippleDrawable;
    MaterialShapeDrawable materialShapeDrawable1 = new MaterialShapeDrawable(this.shapeAppearanceModel);
    materialShapeDrawable1.initializeElevationOverlay(this.materialButton.getContext());
    DrawableCompat.setTintList((Drawable)materialShapeDrawable1, this.backgroundTint);
    PorterDuff.Mode mode = this.backgroundTintMode;
    if (mode != null)
      DrawableCompat.setTintMode((Drawable)materialShapeDrawable1, mode); 
    materialShapeDrawable1.setStroke(this.strokeWidth, this.strokeColor);
    MaterialShapeDrawable materialShapeDrawable2 = new MaterialShapeDrawable(this.shapeAppearanceModel);
    materialShapeDrawable2.setTint(0);
    float f = this.strokeWidth;
    if (this.shouldDrawSurfaceColorStroke) {
      bool = MaterialColors.getColor((View)this.materialButton, R.attr.colorSurface);
    } else {
      bool = false;
    } 
    materialShapeDrawable2.setStroke(f, bool);
    if (IS_LOLLIPOP) {
      MaterialShapeDrawable materialShapeDrawable = new MaterialShapeDrawable(this.shapeAppearanceModel);
      this.maskDrawable = (Drawable)materialShapeDrawable;
      DrawableCompat.setTint((Drawable)materialShapeDrawable, -1);
      rippleDrawable = new RippleDrawable(RippleUtils.sanitizeRippleDrawableColor(this.rippleColor), (Drawable)wrapDrawableWithInset((Drawable)new LayerDrawable(new Drawable[] { (Drawable)materialShapeDrawable2, (Drawable)materialShapeDrawable1 }, )), this.maskDrawable);
      this.rippleDrawable = (LayerDrawable)rippleDrawable;
      return (Drawable)rippleDrawable;
    } 
    RippleDrawableCompat rippleDrawableCompat = new RippleDrawableCompat(this.shapeAppearanceModel);
    this.maskDrawable = (Drawable)rippleDrawableCompat;
    DrawableCompat.setTintList((Drawable)rippleDrawableCompat, RippleUtils.sanitizeRippleDrawableColor(this.rippleColor));
    LayerDrawable layerDrawable = new LayerDrawable(new Drawable[] { (Drawable)materialShapeDrawable2, (Drawable)rippleDrawable, this.maskDrawable });
    this.rippleDrawable = layerDrawable;
    return (Drawable)wrapDrawableWithInset((Drawable)layerDrawable);
  }
  
  private MaterialShapeDrawable getMaterialShapeDrawable(boolean paramBoolean) {
    LayerDrawable layerDrawable = this.rippleDrawable;
    if (layerDrawable != null && layerDrawable.getNumberOfLayers() > 0) {
      if (IS_LOLLIPOP) {
        layerDrawable = (LayerDrawable)((InsetDrawable)this.rippleDrawable.getDrawable(0)).getDrawable();
        return (MaterialShapeDrawable)layerDrawable.getDrawable(paramBoolean ^ true);
      } 
      return (MaterialShapeDrawable)this.rippleDrawable.getDrawable(paramBoolean ^ true);
    } 
    return null;
  }
  
  private MaterialShapeDrawable getSurfaceColorStrokeDrawable() {
    return getMaterialShapeDrawable(true);
  }
  
  private void setVerticalInsets(int paramInt1, int paramInt2) {
    int i1 = ViewCompat.getPaddingStart((View)this.materialButton);
    int k = this.materialButton.getPaddingTop();
    int m = ViewCompat.getPaddingEnd((View)this.materialButton);
    int n = this.materialButton.getPaddingBottom();
    int i = this.insetTop;
    int j = this.insetBottom;
    this.insetBottom = paramInt2;
    this.insetTop = paramInt1;
    if (!this.backgroundOverwritten)
      updateBackground(); 
    ViewCompat.setPaddingRelative((View)this.materialButton, i1, k + paramInt1 - i, m, n + paramInt2 - j);
  }
  
  private void updateBackground() {
    this.materialButton.setInternalBackground(createBackground());
    MaterialShapeDrawable materialShapeDrawable = getMaterialShapeDrawable();
    if (materialShapeDrawable != null)
      materialShapeDrawable.setElevation(this.elevation); 
  }
  
  private void updateButtonShape(ShapeAppearanceModel paramShapeAppearanceModel) {
    if (getMaterialShapeDrawable() != null)
      getMaterialShapeDrawable().setShapeAppearanceModel(paramShapeAppearanceModel); 
    if (getSurfaceColorStrokeDrawable() != null)
      getSurfaceColorStrokeDrawable().setShapeAppearanceModel(paramShapeAppearanceModel); 
    if (getMaskDrawable() != null)
      getMaskDrawable().setShapeAppearanceModel(paramShapeAppearanceModel); 
  }
  
  private void updateStroke() {
    MaterialShapeDrawable materialShapeDrawable1 = getMaterialShapeDrawable();
    MaterialShapeDrawable materialShapeDrawable2 = getSurfaceColorStrokeDrawable();
    if (materialShapeDrawable1 != null) {
      materialShapeDrawable1.setStroke(this.strokeWidth, this.strokeColor);
      if (materialShapeDrawable2 != null) {
        boolean bool;
        float f = this.strokeWidth;
        if (this.shouldDrawSurfaceColorStroke) {
          bool = MaterialColors.getColor((View)this.materialButton, R.attr.colorSurface);
        } else {
          bool = false;
        } 
        materialShapeDrawable2.setStroke(f, bool);
      } 
    } 
  }
  
  private InsetDrawable wrapDrawableWithInset(Drawable paramDrawable) {
    return new InsetDrawable(paramDrawable, this.insetLeft, this.insetTop, this.insetRight, this.insetBottom);
  }
  
  int getCornerRadius() {
    return this.cornerRadius;
  }
  
  public int getInsetBottom() {
    return this.insetBottom;
  }
  
  public int getInsetTop() {
    return this.insetTop;
  }
  
  public Shapeable getMaskDrawable() {
    LayerDrawable layerDrawable = this.rippleDrawable;
    return (layerDrawable != null && layerDrawable.getNumberOfLayers() > 1) ? ((this.rippleDrawable.getNumberOfLayers() > 2) ? (Shapeable)this.rippleDrawable.getDrawable(2) : (Shapeable)this.rippleDrawable.getDrawable(1)) : null;
  }
  
  MaterialShapeDrawable getMaterialShapeDrawable() {
    return getMaterialShapeDrawable(false);
  }
  
  ColorStateList getRippleColor() {
    return this.rippleColor;
  }
  
  ShapeAppearanceModel getShapeAppearanceModel() {
    return this.shapeAppearanceModel;
  }
  
  ColorStateList getStrokeColor() {
    return this.strokeColor;
  }
  
  int getStrokeWidth() {
    return this.strokeWidth;
  }
  
  ColorStateList getSupportBackgroundTintList() {
    return this.backgroundTint;
  }
  
  PorterDuff.Mode getSupportBackgroundTintMode() {
    return this.backgroundTintMode;
  }
  
  boolean isBackgroundOverwritten() {
    return this.backgroundOverwritten;
  }
  
  boolean isCheckable() {
    return this.checkable;
  }
  
  void loadFromAttributes(TypedArray paramTypedArray) {
    this.insetLeft = paramTypedArray.getDimensionPixelOffset(R.styleable.MaterialButton_android_insetLeft, 0);
    this.insetRight = paramTypedArray.getDimensionPixelOffset(R.styleable.MaterialButton_android_insetRight, 0);
    this.insetTop = paramTypedArray.getDimensionPixelOffset(R.styleable.MaterialButton_android_insetTop, 0);
    this.insetBottom = paramTypedArray.getDimensionPixelOffset(R.styleable.MaterialButton_android_insetBottom, 0);
    if (paramTypedArray.hasValue(R.styleable.MaterialButton_cornerRadius)) {
      int n = paramTypedArray.getDimensionPixelSize(R.styleable.MaterialButton_cornerRadius, -1);
      this.cornerRadius = n;
      setShapeAppearanceModel(this.shapeAppearanceModel.withCornerSize(n));
      this.cornerRadiusSet = true;
    } 
    this.strokeWidth = paramTypedArray.getDimensionPixelSize(R.styleable.MaterialButton_strokeWidth, 0);
    this.backgroundTintMode = ViewUtils.parseTintMode(paramTypedArray.getInt(R.styleable.MaterialButton_backgroundTintMode, -1), PorterDuff.Mode.SRC_IN);
    this.backgroundTint = MaterialResources.getColorStateList(this.materialButton.getContext(), paramTypedArray, R.styleable.MaterialButton_backgroundTint);
    this.strokeColor = MaterialResources.getColorStateList(this.materialButton.getContext(), paramTypedArray, R.styleable.MaterialButton_strokeColor);
    this.rippleColor = MaterialResources.getColorStateList(this.materialButton.getContext(), paramTypedArray, R.styleable.MaterialButton_rippleColor);
    this.checkable = paramTypedArray.getBoolean(R.styleable.MaterialButton_android_checkable, false);
    this.elevation = paramTypedArray.getDimensionPixelSize(R.styleable.MaterialButton_elevation, 0);
    int i = ViewCompat.getPaddingStart((View)this.materialButton);
    int m = this.materialButton.getPaddingTop();
    int j = ViewCompat.getPaddingEnd((View)this.materialButton);
    int k = this.materialButton.getPaddingBottom();
    if (paramTypedArray.hasValue(R.styleable.MaterialButton_android_background)) {
      setBackgroundOverwritten();
    } else {
      updateBackground();
    } 
    ViewCompat.setPaddingRelative((View)this.materialButton, this.insetLeft + i, this.insetTop + m, this.insetRight + j, this.insetBottom + k);
  }
  
  void setBackgroundColor(int paramInt) {
    if (getMaterialShapeDrawable() != null)
      getMaterialShapeDrawable().setTint(paramInt); 
  }
  
  void setBackgroundOverwritten() {
    this.backgroundOverwritten = true;
    this.materialButton.setSupportBackgroundTintList(this.backgroundTint);
    this.materialButton.setSupportBackgroundTintMode(this.backgroundTintMode);
  }
  
  void setCheckable(boolean paramBoolean) {
    this.checkable = paramBoolean;
  }
  
  void setCornerRadius(int paramInt) {
    if (!this.cornerRadiusSet || this.cornerRadius != paramInt) {
      this.cornerRadius = paramInt;
      this.cornerRadiusSet = true;
      setShapeAppearanceModel(this.shapeAppearanceModel.withCornerSize(paramInt));
    } 
  }
  
  public void setInsetBottom(int paramInt) {
    setVerticalInsets(this.insetTop, paramInt);
  }
  
  public void setInsetTop(int paramInt) {
    setVerticalInsets(paramInt, this.insetBottom);
  }
  
  void setRippleColor(ColorStateList paramColorStateList) {
    if (this.rippleColor != paramColorStateList) {
      this.rippleColor = paramColorStateList;
      boolean bool = IS_LOLLIPOP;
      if (bool && this.materialButton.getBackground() instanceof RippleDrawable) {
        ((RippleDrawable)this.materialButton.getBackground()).setColor(RippleUtils.sanitizeRippleDrawableColor(paramColorStateList));
      } else if (!bool && this.materialButton.getBackground() instanceof RippleDrawableCompat) {
        ((RippleDrawableCompat)this.materialButton.getBackground()).setTintList(RippleUtils.sanitizeRippleDrawableColor(paramColorStateList));
      } 
    } 
  }
  
  void setShapeAppearanceModel(ShapeAppearanceModel paramShapeAppearanceModel) {
    this.shapeAppearanceModel = paramShapeAppearanceModel;
    updateButtonShape(paramShapeAppearanceModel);
  }
  
  void setShouldDrawSurfaceColorStroke(boolean paramBoolean) {
    this.shouldDrawSurfaceColorStroke = paramBoolean;
    updateStroke();
  }
  
  void setStrokeColor(ColorStateList paramColorStateList) {
    if (this.strokeColor != paramColorStateList) {
      this.strokeColor = paramColorStateList;
      updateStroke();
    } 
  }
  
  void setStrokeWidth(int paramInt) {
    if (this.strokeWidth != paramInt) {
      this.strokeWidth = paramInt;
      updateStroke();
    } 
  }
  
  void setSupportBackgroundTintList(ColorStateList paramColorStateList) {
    if (this.backgroundTint != paramColorStateList) {
      this.backgroundTint = paramColorStateList;
      if (getMaterialShapeDrawable() != null)
        DrawableCompat.setTintList((Drawable)getMaterialShapeDrawable(), this.backgroundTint); 
    } 
  }
  
  void setSupportBackgroundTintMode(PorterDuff.Mode paramMode) {
    if (this.backgroundTintMode != paramMode) {
      this.backgroundTintMode = paramMode;
      if (getMaterialShapeDrawable() != null && this.backgroundTintMode != null)
        DrawableCompat.setTintMode((Drawable)getMaterialShapeDrawable(), this.backgroundTintMode); 
    } 
  }
  
  void updateMaskBounds(int paramInt1, int paramInt2) {
    Drawable drawable = this.maskDrawable;
    if (drawable != null)
      drawable.setBounds(this.insetLeft, this.insetTop, paramInt2 - this.insetRight, paramInt1 - this.insetBottom); 
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\button\MaterialButtonHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */