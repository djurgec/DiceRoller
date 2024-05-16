package com.google.android.material.imageview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatImageView;
import com.google.android.material.R;
import com.google.android.material.resources.MaterialResources;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.android.material.shape.ShapeAppearancePathProvider;
import com.google.android.material.shape.Shapeable;
import com.google.android.material.theme.overlay.MaterialThemeOverlay;

public class ShapeableImageView extends AppCompatImageView implements Shapeable {
  private static final int DEF_STYLE_RES = R.style.Widget_MaterialComponents_ShapeableImageView;
  
  private static final int UNDEFINED_PADDING = -2147483648;
  
  private final Paint borderPaint;
  
  private int bottomContentPadding;
  
  private final Paint clearPaint;
  
  private final RectF destination;
  
  private int endContentPadding;
  
  private boolean hasAdjustedPaddingAfterLayoutDirectionResolved = false;
  
  private int leftContentPadding;
  
  private Path maskPath;
  
  private final RectF maskRect;
  
  private final Path path = new Path();
  
  private final ShapeAppearancePathProvider pathProvider = ShapeAppearancePathProvider.getInstance();
  
  private int rightContentPadding;
  
  private MaterialShapeDrawable shadowDrawable;
  
  private ShapeAppearanceModel shapeAppearanceModel;
  
  private int startContentPadding;
  
  private ColorStateList strokeColor;
  
  private float strokeWidth;
  
  private int topContentPadding;
  
  public ShapeableImageView(Context paramContext) {
    this(paramContext, (AttributeSet)null, 0);
  }
  
  public ShapeableImageView(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public ShapeableImageView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(MaterialThemeOverlay.wrap(paramContext, paramAttributeSet, paramInt, j), paramAttributeSet, paramInt);
    paramContext = getContext();
    Paint paint2 = new Paint();
    this.clearPaint = paint2;
    paint2.setAntiAlias(true);
    paint2.setColor(-1);
    paint2.setXfermode((Xfermode)new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
    this.destination = new RectF();
    this.maskRect = new RectF();
    this.maskPath = new Path();
    TypedArray typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.ShapeableImageView, paramInt, j);
    this.strokeColor = MaterialResources.getColorStateList(paramContext, typedArray, R.styleable.ShapeableImageView_strokeColor);
    this.strokeWidth = typedArray.getDimensionPixelSize(R.styleable.ShapeableImageView_strokeWidth, 0);
    int i = typedArray.getDimensionPixelSize(R.styleable.ShapeableImageView_contentPadding, 0);
    this.leftContentPadding = i;
    this.topContentPadding = i;
    this.rightContentPadding = i;
    this.bottomContentPadding = i;
    this.leftContentPadding = typedArray.getDimensionPixelSize(R.styleable.ShapeableImageView_contentPaddingLeft, i);
    this.topContentPadding = typedArray.getDimensionPixelSize(R.styleable.ShapeableImageView_contentPaddingTop, i);
    this.rightContentPadding = typedArray.getDimensionPixelSize(R.styleable.ShapeableImageView_contentPaddingRight, i);
    this.bottomContentPadding = typedArray.getDimensionPixelSize(R.styleable.ShapeableImageView_contentPaddingBottom, i);
    this.startContentPadding = typedArray.getDimensionPixelSize(R.styleable.ShapeableImageView_contentPaddingStart, -2147483648);
    this.endContentPadding = typedArray.getDimensionPixelSize(R.styleable.ShapeableImageView_contentPaddingEnd, -2147483648);
    typedArray.recycle();
    Paint paint1 = new Paint();
    this.borderPaint = paint1;
    paint1.setStyle(Paint.Style.STROKE);
    paint1.setAntiAlias(true);
    this.shapeAppearanceModel = ShapeAppearanceModel.builder(paramContext, paramAttributeSet, paramInt, j).build();
    if (Build.VERSION.SDK_INT >= 21)
      setOutlineProvider(new OutlineProvider()); 
  }
  
  private void drawStroke(Canvas paramCanvas) {
    if (this.strokeColor == null)
      return; 
    this.borderPaint.setStrokeWidth(this.strokeWidth);
    int i = this.strokeColor.getColorForState(getDrawableState(), this.strokeColor.getDefaultColor());
    if (this.strokeWidth > 0.0F && i != 0) {
      this.borderPaint.setColor(i);
      paramCanvas.drawPath(this.path, this.borderPaint);
    } 
  }
  
  private boolean isContentPaddingRelative() {
    return (this.startContentPadding != Integer.MIN_VALUE || this.endContentPadding != Integer.MIN_VALUE);
  }
  
  private boolean isRtl() {
    int i = Build.VERSION.SDK_INT;
    boolean bool = true;
    if (i < 17 || getLayoutDirection() != 1)
      bool = false; 
    return bool;
  }
  
  private void updateShapeMask(int paramInt1, int paramInt2) {
    this.destination.set(getPaddingLeft(), getPaddingTop(), (paramInt1 - getPaddingRight()), (paramInt2 - getPaddingBottom()));
    this.pathProvider.calculatePath(this.shapeAppearanceModel, 1.0F, this.destination, this.path);
    this.maskPath.rewind();
    this.maskPath.addPath(this.path);
    this.maskRect.set(0.0F, 0.0F, paramInt1, paramInt2);
    this.maskPath.addRect(this.maskRect, Path.Direction.CCW);
  }
  
  public int getContentPaddingBottom() {
    return this.bottomContentPadding;
  }
  
  public final int getContentPaddingEnd() {
    int i = this.endContentPadding;
    if (i != Integer.MIN_VALUE)
      return i; 
    if (isRtl()) {
      i = this.leftContentPadding;
    } else {
      i = this.rightContentPadding;
    } 
    return i;
  }
  
  public int getContentPaddingLeft() {
    if (isContentPaddingRelative()) {
      if (isRtl()) {
        int i = this.endContentPadding;
        if (i != Integer.MIN_VALUE)
          return i; 
      } 
      if (!isRtl()) {
        int i = this.startContentPadding;
        if (i != Integer.MIN_VALUE)
          return i; 
      } 
    } 
    return this.leftContentPadding;
  }
  
  public int getContentPaddingRight() {
    if (isContentPaddingRelative()) {
      if (isRtl()) {
        int i = this.startContentPadding;
        if (i != Integer.MIN_VALUE)
          return i; 
      } 
      if (!isRtl()) {
        int i = this.endContentPadding;
        if (i != Integer.MIN_VALUE)
          return i; 
      } 
    } 
    return this.rightContentPadding;
  }
  
  public final int getContentPaddingStart() {
    int i = this.startContentPadding;
    if (i != Integer.MIN_VALUE)
      return i; 
    if (isRtl()) {
      i = this.rightContentPadding;
    } else {
      i = this.leftContentPadding;
    } 
    return i;
  }
  
  public int getContentPaddingTop() {
    return this.topContentPadding;
  }
  
  public int getPaddingBottom() {
    return super.getPaddingBottom() - getContentPaddingBottom();
  }
  
  public int getPaddingEnd() {
    return super.getPaddingEnd() - getContentPaddingEnd();
  }
  
  public int getPaddingLeft() {
    return super.getPaddingLeft() - getContentPaddingLeft();
  }
  
  public int getPaddingRight() {
    return super.getPaddingRight() - getContentPaddingRight();
  }
  
  public int getPaddingStart() {
    return super.getPaddingStart() - getContentPaddingStart();
  }
  
  public int getPaddingTop() {
    return super.getPaddingTop() - getContentPaddingTop();
  }
  
  public ShapeAppearanceModel getShapeAppearanceModel() {
    return this.shapeAppearanceModel;
  }
  
  public ColorStateList getStrokeColor() {
    return this.strokeColor;
  }
  
  public float getStrokeWidth() {
    return this.strokeWidth;
  }
  
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    setLayerType(2, null);
  }
  
  protected void onDetachedFromWindow() {
    setLayerType(0, null);
    super.onDetachedFromWindow();
  }
  
  protected void onDraw(Canvas paramCanvas) {
    super.onDraw(paramCanvas);
    paramCanvas.drawPath(this.maskPath, this.clearPaint);
    drawStroke(paramCanvas);
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    super.onMeasure(paramInt1, paramInt2);
    if (this.hasAdjustedPaddingAfterLayoutDirectionResolved)
      return; 
    if (Build.VERSION.SDK_INT > 19 && !isLayoutDirectionResolved())
      return; 
    this.hasAdjustedPaddingAfterLayoutDirectionResolved = true;
    if (Build.VERSION.SDK_INT >= 21 && (isPaddingRelative() || isContentPaddingRelative())) {
      setPaddingRelative(super.getPaddingStart(), super.getPaddingTop(), super.getPaddingEnd(), super.getPaddingBottom());
      return;
    } 
    setPadding(super.getPaddingLeft(), super.getPaddingTop(), super.getPaddingRight(), super.getPaddingBottom());
  }
  
  protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    updateShapeMask(paramInt1, paramInt2);
  }
  
  public void setContentPadding(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    this.startContentPadding = Integer.MIN_VALUE;
    this.endContentPadding = Integer.MIN_VALUE;
    super.setPadding(super.getPaddingLeft() - this.leftContentPadding + paramInt1, super.getPaddingTop() - this.topContentPadding + paramInt2, super.getPaddingRight() - this.rightContentPadding + paramInt3, super.getPaddingBottom() - this.bottomContentPadding + paramInt4);
    this.leftContentPadding = paramInt1;
    this.topContentPadding = paramInt2;
    this.rightContentPadding = paramInt3;
    this.bottomContentPadding = paramInt4;
  }
  
  public void setContentPaddingRelative(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    int i;
    super.setPaddingRelative(super.getPaddingStart() - getContentPaddingStart() + paramInt1, super.getPaddingTop() - this.topContentPadding + paramInt2, super.getPaddingEnd() - getContentPaddingEnd() + paramInt3, super.getPaddingBottom() - this.bottomContentPadding + paramInt4);
    if (isRtl()) {
      i = paramInt3;
    } else {
      i = paramInt1;
    } 
    this.leftContentPadding = i;
    this.topContentPadding = paramInt2;
    if (!isRtl())
      paramInt1 = paramInt3; 
    this.rightContentPadding = paramInt1;
    this.bottomContentPadding = paramInt4;
  }
  
  public void setPadding(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super.setPadding(getContentPaddingLeft() + paramInt1, getContentPaddingTop() + paramInt2, getContentPaddingRight() + paramInt3, getContentPaddingBottom() + paramInt4);
  }
  
  public void setPaddingRelative(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super.setPaddingRelative(getContentPaddingStart() + paramInt1, getContentPaddingTop() + paramInt2, getContentPaddingEnd() + paramInt3, getContentPaddingBottom() + paramInt4);
  }
  
  public void setShapeAppearanceModel(ShapeAppearanceModel paramShapeAppearanceModel) {
    this.shapeAppearanceModel = paramShapeAppearanceModel;
    MaterialShapeDrawable materialShapeDrawable = this.shadowDrawable;
    if (materialShapeDrawable != null)
      materialShapeDrawable.setShapeAppearanceModel(paramShapeAppearanceModel); 
    updateShapeMask(getWidth(), getHeight());
    invalidate();
    if (Build.VERSION.SDK_INT >= 21)
      invalidateOutline(); 
  }
  
  public void setStrokeColor(ColorStateList paramColorStateList) {
    this.strokeColor = paramColorStateList;
    invalidate();
  }
  
  public void setStrokeColorResource(int paramInt) {
    setStrokeColor(AppCompatResources.getColorStateList(getContext(), paramInt));
  }
  
  public void setStrokeWidth(float paramFloat) {
    if (this.strokeWidth != paramFloat) {
      this.strokeWidth = paramFloat;
      invalidate();
    } 
  }
  
  public void setStrokeWidthResource(int paramInt) {
    setStrokeWidth(getResources().getDimensionPixelSize(paramInt));
  }
  
  class OutlineProvider extends ViewOutlineProvider {
    private final Rect rect = new Rect();
    
    final ShapeableImageView this$0;
    
    public void getOutline(View param1View, Outline param1Outline) {
      if (ShapeableImageView.this.shapeAppearanceModel == null)
        return; 
      if (ShapeableImageView.this.shadowDrawable == null)
        ShapeableImageView.access$102(ShapeableImageView.this, new MaterialShapeDrawable(ShapeableImageView.this.shapeAppearanceModel)); 
      ShapeableImageView.this.destination.round(this.rect);
      ShapeableImageView.this.shadowDrawable.setBounds(this.rect);
      ShapeableImageView.this.shadowDrawable.getOutline(param1Outline);
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\imageview\ShapeableImageView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */