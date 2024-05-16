package androidx.appcompat.graphics.drawable;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import androidx.appcompat.R;
import androidx.core.graphics.drawable.DrawableCompat;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class DrawerArrowDrawable extends Drawable {
  public static final int ARROW_DIRECTION_END = 3;
  
  public static final int ARROW_DIRECTION_LEFT = 0;
  
  public static final int ARROW_DIRECTION_RIGHT = 1;
  
  public static final int ARROW_DIRECTION_START = 2;
  
  private static final float ARROW_HEAD_ANGLE = (float)Math.toRadians(45.0D);
  
  private float mArrowHeadLength;
  
  private float mArrowShaftLength;
  
  private float mBarGap;
  
  private float mBarLength;
  
  private int mDirection;
  
  private float mMaxCutForBarSize;
  
  private final Paint mPaint;
  
  private final Path mPath;
  
  private float mProgress;
  
  private final int mSize;
  
  private boolean mSpin;
  
  private boolean mVerticalMirror;
  
  public DrawerArrowDrawable(Context paramContext) {
    Paint paint = new Paint();
    this.mPaint = paint;
    this.mPath = new Path();
    this.mVerticalMirror = false;
    this.mDirection = 2;
    paint.setStyle(Paint.Style.STROKE);
    paint.setStrokeJoin(Paint.Join.MITER);
    paint.setStrokeCap(Paint.Cap.BUTT);
    paint.setAntiAlias(true);
    TypedArray typedArray = paramContext.getTheme().obtainStyledAttributes(null, R.styleable.DrawerArrowToggle, R.attr.drawerArrowStyle, R.style.Base_Widget_AppCompat_DrawerArrowToggle);
    setColor(typedArray.getColor(R.styleable.DrawerArrowToggle_color, 0));
    setBarThickness(typedArray.getDimension(R.styleable.DrawerArrowToggle_thickness, 0.0F));
    setSpinEnabled(typedArray.getBoolean(R.styleable.DrawerArrowToggle_spinBars, true));
    setGapSize(Math.round(typedArray.getDimension(R.styleable.DrawerArrowToggle_gapBetweenBars, 0.0F)));
    this.mSize = typedArray.getDimensionPixelSize(R.styleable.DrawerArrowToggle_drawableSize, 0);
    this.mBarLength = Math.round(typedArray.getDimension(R.styleable.DrawerArrowToggle_barLength, 0.0F));
    this.mArrowHeadLength = Math.round(typedArray.getDimension(R.styleable.DrawerArrowToggle_arrowHeadLength, 0.0F));
    this.mArrowShaftLength = typedArray.getDimension(R.styleable.DrawerArrowToggle_arrowShaftLength, 0.0F);
    typedArray.recycle();
  }
  
  private static float lerp(float paramFloat1, float paramFloat2, float paramFloat3) {
    return (paramFloat2 - paramFloat1) * paramFloat3 + paramFloat1;
  }
  
  public void draw(Canvas paramCanvas) {
    Rect rect = getBounds();
    int i = this.mDirection;
    boolean bool1 = false;
    boolean bool2 = false;
    switch (i) {
      default:
        if (DrawableCompat.getLayoutDirection(this) == 1)
          bool1 = true; 
        break;
      case 3:
        bool1 = bool2;
        if (DrawableCompat.getLayoutDirection(this) == 0)
          bool1 = true; 
        break;
      case 1:
        bool1 = true;
        break;
      case 0:
        bool1 = false;
        break;
    } 
    float f1 = this.mArrowHeadLength;
    f1 = (float)Math.sqrt((f1 * f1 * 2.0F));
    float f5 = lerp(this.mBarLength, f1, this.mProgress);
    float f4 = lerp(this.mBarLength, this.mArrowShaftLength, this.mProgress);
    float f3 = Math.round(lerp(0.0F, this.mMaxCutForBarSize, this.mProgress));
    float f6 = lerp(0.0F, ARROW_HEAD_ANGLE, this.mProgress);
    if (bool1) {
      f1 = 0.0F;
    } else {
      f1 = -180.0F;
    } 
    if (bool1) {
      f2 = 180.0F;
    } else {
      f2 = 0.0F;
    } 
    f1 = lerp(f1, f2, this.mProgress);
    float f2 = (float)Math.round(f5 * Math.cos(f6));
    f6 = (float)Math.round(f5 * Math.sin(f6));
    this.mPath.rewind();
    f5 = lerp(this.mBarGap + this.mPaint.getStrokeWidth(), -this.mMaxCutForBarSize, this.mProgress);
    float f7 = -f4 / 2.0F;
    this.mPath.moveTo(f7 + f3, 0.0F);
    this.mPath.rLineTo(f4 - f3 * 2.0F, 0.0F);
    this.mPath.moveTo(f7, f5);
    this.mPath.rLineTo(f2, f6);
    this.mPath.moveTo(f7, -f5);
    this.mPath.rLineTo(f2, -f6);
    this.mPath.close();
    paramCanvas.save();
    f2 = this.mPaint.getStrokeWidth();
    f4 = rect.height();
    f3 = this.mBarGap;
    f4 = ((int)(f4 - 3.0F * f2 - 2.0F * f3) / 4 * 2);
    paramCanvas.translate(rect.centerX(), f4 + 1.5F * f2 + f3);
    if (this.mSpin) {
      if (this.mVerticalMirror ^ bool1) {
        byte b = -1;
      } else {
        bool1 = true;
      } 
      paramCanvas.rotate(bool1 * f1);
    } else if (bool1) {
      paramCanvas.rotate(180.0F);
    } 
    paramCanvas.drawPath(this.mPath, this.mPaint);
    paramCanvas.restore();
  }
  
  public float getArrowHeadLength() {
    return this.mArrowHeadLength;
  }
  
  public float getArrowShaftLength() {
    return this.mArrowShaftLength;
  }
  
  public float getBarLength() {
    return this.mBarLength;
  }
  
  public float getBarThickness() {
    return this.mPaint.getStrokeWidth();
  }
  
  public int getColor() {
    return this.mPaint.getColor();
  }
  
  public int getDirection() {
    return this.mDirection;
  }
  
  public float getGapSize() {
    return this.mBarGap;
  }
  
  public int getIntrinsicHeight() {
    return this.mSize;
  }
  
  public int getIntrinsicWidth() {
    return this.mSize;
  }
  
  public int getOpacity() {
    return -3;
  }
  
  public final Paint getPaint() {
    return this.mPaint;
  }
  
  public float getProgress() {
    return this.mProgress;
  }
  
  public boolean isSpinEnabled() {
    return this.mSpin;
  }
  
  public void setAlpha(int paramInt) {
    if (paramInt != this.mPaint.getAlpha()) {
      this.mPaint.setAlpha(paramInt);
      invalidateSelf();
    } 
  }
  
  public void setArrowHeadLength(float paramFloat) {
    if (this.mArrowHeadLength != paramFloat) {
      this.mArrowHeadLength = paramFloat;
      invalidateSelf();
    } 
  }
  
  public void setArrowShaftLength(float paramFloat) {
    if (this.mArrowShaftLength != paramFloat) {
      this.mArrowShaftLength = paramFloat;
      invalidateSelf();
    } 
  }
  
  public void setBarLength(float paramFloat) {
    if (this.mBarLength != paramFloat) {
      this.mBarLength = paramFloat;
      invalidateSelf();
    } 
  }
  
  public void setBarThickness(float paramFloat) {
    if (this.mPaint.getStrokeWidth() != paramFloat) {
      this.mPaint.setStrokeWidth(paramFloat);
      this.mMaxCutForBarSize = (float)((paramFloat / 2.0F) * Math.cos(ARROW_HEAD_ANGLE));
      invalidateSelf();
    } 
  }
  
  public void setColor(int paramInt) {
    if (paramInt != this.mPaint.getColor()) {
      this.mPaint.setColor(paramInt);
      invalidateSelf();
    } 
  }
  
  public void setColorFilter(ColorFilter paramColorFilter) {
    this.mPaint.setColorFilter(paramColorFilter);
    invalidateSelf();
  }
  
  public void setDirection(int paramInt) {
    if (paramInt != this.mDirection) {
      this.mDirection = paramInt;
      invalidateSelf();
    } 
  }
  
  public void setGapSize(float paramFloat) {
    if (paramFloat != this.mBarGap) {
      this.mBarGap = paramFloat;
      invalidateSelf();
    } 
  }
  
  public void setProgress(float paramFloat) {
    if (this.mProgress != paramFloat) {
      this.mProgress = paramFloat;
      invalidateSelf();
    } 
  }
  
  public void setSpinEnabled(boolean paramBoolean) {
    if (this.mSpin != paramBoolean) {
      this.mSpin = paramBoolean;
      invalidateSelf();
    } 
  }
  
  public void setVerticalMirror(boolean paramBoolean) {
    if (this.mVerticalMirror != paramBoolean) {
      this.mVerticalMirror = paramBoolean;
      invalidateSelf();
    } 
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface ArrowDirection {}
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\appcompat\graphics\drawable\DrawerArrowDrawable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */