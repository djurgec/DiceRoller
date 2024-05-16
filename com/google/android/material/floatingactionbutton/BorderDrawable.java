package com.google.android.material.floatingactionbutton;

import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import androidx.core.graphics.ColorUtils;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.android.material.shape.ShapeAppearancePathProvider;

class BorderDrawable extends Drawable {
  private static final float DRAW_STROKE_WIDTH_MULTIPLE = 1.3333F;
  
  private ColorStateList borderTint;
  
  float borderWidth;
  
  private int bottomInnerStrokeColor;
  
  private int bottomOuterStrokeColor;
  
  private final RectF boundsRectF = new RectF();
  
  private int currentBorderTintColor;
  
  private boolean invalidateShader = true;
  
  private final Paint paint;
  
  private final ShapeAppearancePathProvider pathProvider = ShapeAppearancePathProvider.getInstance();
  
  private final Rect rect = new Rect();
  
  private final RectF rectF = new RectF();
  
  private ShapeAppearanceModel shapeAppearanceModel;
  
  private final Path shapePath = new Path();
  
  private final BorderState state = new BorderState();
  
  private int topInnerStrokeColor;
  
  private int topOuterStrokeColor;
  
  BorderDrawable(ShapeAppearanceModel paramShapeAppearanceModel) {
    this.shapeAppearanceModel = paramShapeAppearanceModel;
    Paint paint = new Paint(1);
    this.paint = paint;
    paint.setStyle(Paint.Style.STROKE);
  }
  
  private Shader createGradientShader() {
    Rect rect = this.rect;
    copyBounds(rect);
    float f2 = this.borderWidth / rect.height();
    int j = ColorUtils.compositeColors(this.topOuterStrokeColor, this.currentBorderTintColor);
    int n = ColorUtils.compositeColors(this.topInnerStrokeColor, this.currentBorderTintColor);
    int i1 = ColorUtils.compositeColors(ColorUtils.setAlphaComponent(this.topInnerStrokeColor, 0), this.currentBorderTintColor);
    int m = ColorUtils.compositeColors(ColorUtils.setAlphaComponent(this.bottomInnerStrokeColor, 0), this.currentBorderTintColor);
    int k = ColorUtils.compositeColors(this.bottomInnerStrokeColor, this.currentBorderTintColor);
    int i = ColorUtils.compositeColors(this.bottomOuterStrokeColor, this.currentBorderTintColor);
    float f1 = rect.top;
    float f3 = rect.bottom;
    Shader.TileMode tileMode = Shader.TileMode.CLAMP;
    return (Shader)new LinearGradient(0.0F, f1, 0.0F, f3, new int[] { j, n, i1, m, k, i }, new float[] { 0.0F, f2, 0.5F, 0.5F, 1.0F - f2, 1.0F }, tileMode);
  }
  
  public void draw(Canvas paramCanvas) {
    if (this.invalidateShader) {
      this.paint.setShader(createGradientShader());
      this.invalidateShader = false;
    } 
    float f2 = this.paint.getStrokeWidth() / 2.0F;
    copyBounds(this.rect);
    this.rectF.set(this.rect);
    float f1 = Math.min(this.shapeAppearanceModel.getTopLeftCornerSize().getCornerSize(getBoundsAsRectF()), this.rectF.width() / 2.0F);
    if (this.shapeAppearanceModel.isRoundRect(getBoundsAsRectF())) {
      this.rectF.inset(f2, f2);
      paramCanvas.drawRoundRect(this.rectF, f1, f1, this.paint);
    } 
  }
  
  protected RectF getBoundsAsRectF() {
    this.boundsRectF.set(getBounds());
    return this.boundsRectF;
  }
  
  public Drawable.ConstantState getConstantState() {
    return this.state;
  }
  
  public int getOpacity() {
    byte b;
    if (this.borderWidth > 0.0F) {
      b = -3;
    } else {
      b = -2;
    } 
    return b;
  }
  
  public void getOutline(Outline paramOutline) {
    if (this.shapeAppearanceModel.isRoundRect(getBoundsAsRectF())) {
      float f = this.shapeAppearanceModel.getTopLeftCornerSize().getCornerSize(getBoundsAsRectF());
      paramOutline.setRoundRect(getBounds(), f);
      return;
    } 
    copyBounds(this.rect);
    this.rectF.set(this.rect);
    this.pathProvider.calculatePath(this.shapeAppearanceModel, 1.0F, this.rectF, this.shapePath);
    if (this.shapePath.isConvex())
      paramOutline.setConvexPath(this.shapePath); 
  }
  
  public boolean getPadding(Rect paramRect) {
    if (this.shapeAppearanceModel.isRoundRect(getBoundsAsRectF())) {
      int i = Math.round(this.borderWidth);
      paramRect.set(i, i, i, i);
    } 
    return true;
  }
  
  public ShapeAppearanceModel getShapeAppearanceModel() {
    return this.shapeAppearanceModel;
  }
  
  public boolean isStateful() {
    boolean bool;
    ColorStateList colorStateList = this.borderTint;
    if ((colorStateList != null && colorStateList.isStateful()) || super.isStateful()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  protected void onBoundsChange(Rect paramRect) {
    this.invalidateShader = true;
  }
  
  protected boolean onStateChange(int[] paramArrayOfint) {
    ColorStateList colorStateList = this.borderTint;
    if (colorStateList != null) {
      int i = colorStateList.getColorForState(paramArrayOfint, this.currentBorderTintColor);
      if (i != this.currentBorderTintColor) {
        this.invalidateShader = true;
        this.currentBorderTintColor = i;
      } 
    } 
    if (this.invalidateShader)
      invalidateSelf(); 
    return this.invalidateShader;
  }
  
  public void setAlpha(int paramInt) {
    this.paint.setAlpha(paramInt);
    invalidateSelf();
  }
  
  void setBorderTint(ColorStateList paramColorStateList) {
    if (paramColorStateList != null)
      this.currentBorderTintColor = paramColorStateList.getColorForState(getState(), this.currentBorderTintColor); 
    this.borderTint = paramColorStateList;
    this.invalidateShader = true;
    invalidateSelf();
  }
  
  public void setBorderWidth(float paramFloat) {
    if (this.borderWidth != paramFloat) {
      this.borderWidth = paramFloat;
      this.paint.setStrokeWidth(1.3333F * paramFloat);
      this.invalidateShader = true;
      invalidateSelf();
    } 
  }
  
  public void setColorFilter(ColorFilter paramColorFilter) {
    this.paint.setColorFilter(paramColorFilter);
    invalidateSelf();
  }
  
  void setGradientColors(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    this.topOuterStrokeColor = paramInt1;
    this.topInnerStrokeColor = paramInt2;
    this.bottomOuterStrokeColor = paramInt3;
    this.bottomInnerStrokeColor = paramInt4;
  }
  
  public void setShapeAppearanceModel(ShapeAppearanceModel paramShapeAppearanceModel) {
    this.shapeAppearanceModel = paramShapeAppearanceModel;
    invalidateSelf();
  }
  
  private class BorderState extends Drawable.ConstantState {
    final BorderDrawable this$0;
    
    private BorderState() {}
    
    public int getChangingConfigurations() {
      return 0;
    }
    
    public Drawable newDrawable() {
      return BorderDrawable.this;
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\floatingactionbutton\BorderDrawable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */