package androidx.constraintlayout.utils.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Layout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewOutlineProvider;
import androidx.appcompat.R;
import androidx.constraintlayout.motion.widget.Debug;
import androidx.constraintlayout.motion.widget.FloatLayout;
import androidx.constraintlayout.widget.R;

public class MotionLabel extends View implements FloatLayout {
  private static final int MONOSPACE = 3;
  
  private static final int SANS = 1;
  
  private static final int SERIF = 2;
  
  static String TAG = "MotionLabel";
  
  private boolean mAutoSize = false;
  
  private int mAutoSizeTextType = 0;
  
  float mBackgroundPanX = Float.NaN;
  
  float mBackgroundPanY = Float.NaN;
  
  private float mBaseTextSize = Float.NaN;
  
  private float mDeltaLeft;
  
  private float mFloatHeight;
  
  private float mFloatWidth;
  
  private String mFontFamily;
  
  private int mGravity = 8388659;
  
  private Layout mLayout;
  
  boolean mNotBuilt = true;
  
  Matrix mOutlinePositionMatrix;
  
  private int mPaddingBottom = 1;
  
  private int mPaddingLeft = 1;
  
  private int mPaddingRight = 1;
  
  private int mPaddingTop = 1;
  
  TextPaint mPaint = new TextPaint();
  
  Path mPath = new Path();
  
  RectF mRect;
  
  float mRotate = Float.NaN;
  
  private float mRound = Float.NaN;
  
  private float mRoundPercent = 0.0F;
  
  private int mStyleIndex;
  
  Paint mTempPaint;
  
  Rect mTempRect;
  
  private String mText = "Hello World";
  
  private Drawable mTextBackground;
  
  private Bitmap mTextBackgroundBitmap;
  
  private Rect mTextBounds = new Rect();
  
  private int mTextFillColor = 65535;
  
  private int mTextOutlineColor = 65535;
  
  private float mTextOutlineThickness = 0.0F;
  
  private float mTextPanX = 0.0F;
  
  private float mTextPanY = 0.0F;
  
  private BitmapShader mTextShader;
  
  private Matrix mTextShaderMatrix;
  
  private float mTextSize = 48.0F;
  
  private int mTextureEffect = 0;
  
  private float mTextureHeight = Float.NaN;
  
  private float mTextureWidth = Float.NaN;
  
  private CharSequence mTransformed;
  
  private int mTypefaceIndex;
  
  private boolean mUseOutline = false;
  
  ViewOutlineProvider mViewOutlineProvider;
  
  float mZoom = Float.NaN;
  
  Paint paintCache = new Paint();
  
  float paintTextSize;
  
  public MotionLabel(Context paramContext) {
    super(paramContext);
    init(paramContext, (AttributeSet)null);
  }
  
  public MotionLabel(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    init(paramContext, paramAttributeSet);
  }
  
  public MotionLabel(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    init(paramContext, paramAttributeSet);
  }
  
  private void adjustTexture(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
    if (this.mTextShaderMatrix == null)
      return; 
    this.mFloatWidth = paramFloat3 - paramFloat1;
    this.mFloatHeight = paramFloat4 - paramFloat2;
    updateShaderMatrix();
  }
  
  private float getHorizontalOffset() {
    float f1;
    float f2;
    if (Float.isNaN(this.mBaseTextSize)) {
      f1 = 1.0F;
    } else {
      f1 = this.mTextSize / this.mBaseTextSize;
    } 
    TextPaint textPaint = this.mPaint;
    String str = this.mText;
    float f3 = textPaint.measureText(str, 0, str.length());
    if (Float.isNaN(this.mFloatWidth)) {
      f2 = getMeasuredWidth();
    } else {
      f2 = this.mFloatWidth;
    } 
    return (f2 - getPaddingLeft() - getPaddingRight() - f3 * f1) * (this.mTextPanX + 1.0F) / 2.0F;
  }
  
  private float getVerticalOffset() {
    float f1;
    float f2;
    if (Float.isNaN(this.mBaseTextSize)) {
      f1 = 1.0F;
    } else {
      f1 = this.mTextSize / this.mBaseTextSize;
    } 
    Paint.FontMetrics fontMetrics = this.mPaint.getFontMetrics();
    if (Float.isNaN(this.mFloatHeight)) {
      f2 = getMeasuredHeight();
    } else {
      f2 = this.mFloatHeight;
    } 
    return (f2 - getPaddingTop() - getPaddingBottom() - (fontMetrics.descent - fontMetrics.ascent) * f1) * (1.0F - this.mTextPanY) / 2.0F - fontMetrics.ascent * f1;
  }
  
  private void init(Context paramContext, AttributeSet paramAttributeSet) {
    setUpTheme(paramContext, paramAttributeSet);
    if (paramAttributeSet != null) {
      TypedArray typedArray = getContext().obtainStyledAttributes(paramAttributeSet, R.styleable.MotionLabel);
      int i = typedArray.getIndexCount();
      for (byte b = 0; b < i; b++) {
        int j = typedArray.getIndex(b);
        if (j == R.styleable.MotionLabel_android_text) {
          setText(typedArray.getText(j));
        } else if (j == R.styleable.MotionLabel_android_fontFamily) {
          this.mFontFamily = typedArray.getString(j);
        } else if (j == R.styleable.MotionLabel_scaleFromTextSize) {
          this.mBaseTextSize = typedArray.getDimensionPixelSize(j, (int)this.mBaseTextSize);
        } else if (j == R.styleable.MotionLabel_android_textSize) {
          this.mTextSize = typedArray.getDimensionPixelSize(j, (int)this.mTextSize);
        } else if (j == R.styleable.MotionLabel_android_textStyle) {
          this.mStyleIndex = typedArray.getInt(j, this.mStyleIndex);
        } else if (j == R.styleable.MotionLabel_android_typeface) {
          this.mTypefaceIndex = typedArray.getInt(j, this.mTypefaceIndex);
        } else if (j == R.styleable.MotionLabel_android_textColor) {
          this.mTextFillColor = typedArray.getColor(j, this.mTextFillColor);
        } else if (j == R.styleable.MotionLabel_borderRound) {
          this.mRound = typedArray.getDimension(j, this.mRound);
          if (Build.VERSION.SDK_INT >= 21)
            setRound(this.mRound); 
        } else if (j == R.styleable.MotionLabel_borderRoundPercent) {
          this.mRoundPercent = typedArray.getFloat(j, this.mRoundPercent);
          if (Build.VERSION.SDK_INT >= 21)
            setRoundPercent(this.mRoundPercent); 
        } else if (j == R.styleable.MotionLabel_android_gravity) {
          setGravity(typedArray.getInt(j, -1));
        } else if (j == R.styleable.MotionLabel_android_autoSizeTextType) {
          this.mAutoSizeTextType = typedArray.getInt(j, 0);
        } else if (j == R.styleable.MotionLabel_textOutlineColor) {
          this.mTextOutlineColor = typedArray.getInt(j, this.mTextOutlineColor);
          this.mUseOutline = true;
        } else if (j == R.styleable.MotionLabel_textOutlineThickness) {
          this.mTextOutlineThickness = typedArray.getDimension(j, this.mTextOutlineThickness);
          this.mUseOutline = true;
        } else if (j == R.styleable.MotionLabel_textBackground) {
          this.mTextBackground = typedArray.getDrawable(j);
          this.mUseOutline = true;
        } else if (j == R.styleable.MotionLabel_textBackgroundPanX) {
          this.mBackgroundPanX = typedArray.getFloat(j, this.mBackgroundPanX);
        } else if (j == R.styleable.MotionLabel_textBackgroundPanY) {
          this.mBackgroundPanY = typedArray.getFloat(j, this.mBackgroundPanY);
        } else if (j == R.styleable.MotionLabel_textPanX) {
          this.mTextPanX = typedArray.getFloat(j, this.mTextPanX);
        } else if (j == R.styleable.MotionLabel_textPanY) {
          this.mTextPanY = typedArray.getFloat(j, this.mTextPanY);
        } else if (j == R.styleable.MotionLabel_textBackgroundRotate) {
          this.mRotate = typedArray.getFloat(j, this.mRotate);
        } else if (j == R.styleable.MotionLabel_textBackgroundZoom) {
          this.mZoom = typedArray.getFloat(j, this.mZoom);
        } else if (j == R.styleable.MotionLabel_textureHeight) {
          this.mTextureHeight = typedArray.getDimension(j, this.mTextureHeight);
        } else if (j == R.styleable.MotionLabel_textureWidth) {
          this.mTextureWidth = typedArray.getDimension(j, this.mTextureWidth);
        } else if (j == R.styleable.MotionLabel_textureEffect) {
          this.mTextureEffect = typedArray.getInt(j, this.mTextureEffect);
        } 
      } 
      typedArray.recycle();
    } 
    setupTexture();
    setupPath();
  }
  
  private void setTypefaceFromAttrs(String paramString, int paramInt1, int paramInt2) {
    Typeface typeface1;
    TextPaint textPaint;
    Typeface typeface2 = null;
    if (paramString != null) {
      Typeface typeface = Typeface.create(paramString, paramInt2);
      typeface2 = typeface;
      if (typeface != null) {
        setTypeface(typeface);
        return;
      } 
    } 
    switch (paramInt1) {
      default:
        typeface1 = typeface2;
        break;
      case 3:
        typeface1 = Typeface.MONOSPACE;
        break;
      case 2:
        typeface1 = Typeface.SERIF;
        break;
      case 1:
        typeface1 = Typeface.SANS_SERIF;
        break;
    } 
    float f = 0.0F;
    boolean bool = false;
    if (paramInt2 > 0) {
      if (typeface1 == null) {
        typeface1 = Typeface.defaultFromStyle(paramInt2);
      } else {
        typeface1 = Typeface.create(typeface1, paramInt2);
      } 
      setTypeface(typeface1);
      if (typeface1 != null) {
        paramInt1 = typeface1.getStyle();
      } else {
        paramInt1 = 0;
      } 
      paramInt1 = (paramInt1 ^ 0xFFFFFFFF) & paramInt2;
      textPaint = this.mPaint;
      if ((paramInt1 & 0x1) != 0)
        bool = true; 
      textPaint.setFakeBoldText(bool);
      textPaint = this.mPaint;
      if ((paramInt1 & 0x2) != 0)
        f = -0.25F; 
      textPaint.setTextSkewX(f);
    } else {
      this.mPaint.setFakeBoldText(false);
      this.mPaint.setTextSkewX(0.0F);
      setTypeface((Typeface)textPaint);
    } 
  }
  
  private void setUpTheme(Context paramContext, AttributeSet paramAttributeSet) {
    TypedValue typedValue = new TypedValue();
    paramContext.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
    TextPaint textPaint = this.mPaint;
    int i = typedValue.data;
    this.mTextFillColor = i;
    textPaint.setColor(i);
  }
  
  private void setupTexture() {
    if (this.mTextBackground != null) {
      this.mTextShaderMatrix = new Matrix();
      int j = this.mTextBackground.getIntrinsicWidth();
      int m = this.mTextBackground.getIntrinsicHeight();
      int k = 128;
      int i = j;
      if (j <= 0) {
        j = getWidth();
        i = j;
        if (j == 0)
          if (Float.isNaN(this.mTextureWidth)) {
            i = 128;
          } else {
            i = (int)this.mTextureWidth;
          }  
      } 
      j = m;
      if (m <= 0) {
        m = getHeight();
        j = m;
        if (m == 0)
          if (Float.isNaN(this.mTextureHeight)) {
            j = k;
          } else {
            j = (int)this.mTextureHeight;
          }  
      } 
      m = i;
      k = j;
      if (this.mTextureEffect != 0) {
        m = i / 2;
        k = j / 2;
      } 
      this.mTextBackgroundBitmap = Bitmap.createBitmap(m, k, Bitmap.Config.ARGB_8888);
      Canvas canvas = new Canvas(this.mTextBackgroundBitmap);
      this.mTextBackground.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
      this.mTextBackground.setFilterBitmap(true);
      this.mTextBackground.draw(canvas);
      if (this.mTextureEffect != 0)
        this.mTextBackgroundBitmap = blur(this.mTextBackgroundBitmap, 4); 
      this.mTextShader = new BitmapShader(this.mTextBackgroundBitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
    } 
  }
  
  private void updateShaderMatrix() {
    float f1;
    float f2;
    float f4;
    float f5;
    boolean bool = Float.isNaN(this.mBackgroundPanX);
    float f3 = 0.0F;
    if (bool) {
      f1 = 0.0F;
    } else {
      f1 = this.mBackgroundPanX;
    } 
    if (Float.isNaN(this.mBackgroundPanY)) {
      f2 = 0.0F;
    } else {
      f2 = this.mBackgroundPanY;
    } 
    if (Float.isNaN(this.mZoom)) {
      f6 = 1.0F;
    } else {
      f6 = this.mZoom;
    } 
    if (!Float.isNaN(this.mRotate))
      f3 = this.mRotate; 
    this.mTextShaderMatrix.reset();
    float f8 = this.mTextBackgroundBitmap.getWidth();
    float f9 = this.mTextBackgroundBitmap.getHeight();
    if (Float.isNaN(this.mTextureWidth)) {
      f4 = this.mFloatWidth;
    } else {
      f4 = this.mTextureWidth;
    } 
    if (Float.isNaN(this.mTextureHeight)) {
      f5 = this.mFloatHeight;
    } else {
      f5 = this.mTextureHeight;
    } 
    if (f8 * f5 < f9 * f4) {
      f7 = f4 / f8;
    } else {
      f7 = f5 / f9;
    } 
    float f10 = f7 * f6;
    this.mTextShaderMatrix.postScale(f10, f10);
    float f7 = f4 - f10 * f8;
    float f6 = f5 - f10 * f9;
    if (!Float.isNaN(this.mTextureHeight))
      f6 = this.mTextureHeight / 2.0F; 
    if (!Float.isNaN(this.mTextureWidth))
      f7 = this.mTextureWidth / 2.0F; 
    this.mTextShaderMatrix.postTranslate((f1 * f7 + f4 - f10 * f8) * 0.5F, (f2 * f6 + f5 - f10 * f9) * 0.5F);
    this.mTextShaderMatrix.postRotate(f3, f4 / 2.0F, f5 / 2.0F);
    this.mTextShader.setLocalMatrix(this.mTextShaderMatrix);
  }
  
  Bitmap blur(Bitmap paramBitmap, int paramInt) {
    System.nanoTime();
    int j = paramBitmap.getWidth();
    int i = paramBitmap.getHeight();
    int k = j / 2;
    j = i / 2;
    paramBitmap = Bitmap.createScaledBitmap(paramBitmap, k, j, true);
    for (i = 0; i < paramInt && k >= 32 && j >= 32; i++) {
      k /= 2;
      j /= 2;
      paramBitmap = Bitmap.createScaledBitmap(paramBitmap, k, j, true);
    } 
    return paramBitmap;
  }
  
  void buildShape(float paramFloat) {
    if (!this.mUseOutline && paramFloat == 1.0F)
      return; 
    this.mPath.reset();
    String str = this.mText;
    int i = str.length();
    this.mPaint.getTextBounds(str, 0, i, this.mTextBounds);
    this.mPaint.getTextPath(str, 0, i, 0.0F, 0.0F, this.mPath);
    if (paramFloat != 1.0F) {
      String str1 = TAG;
      str = Debug.getLoc();
      Log.v(str1, (new StringBuilder(String.valueOf(str).length() + 22)).append(str).append(" scale ").append(paramFloat).toString());
      Matrix matrix = new Matrix();
      matrix.postScale(paramFloat, paramFloat);
      this.mPath.transform(matrix);
    } 
    Rect rect = this.mTextBounds;
    rect.right--;
    rect = this.mTextBounds;
    rect.left++;
    rect = this.mTextBounds;
    rect.bottom++;
    rect = this.mTextBounds;
    rect.top--;
    RectF rectF = new RectF();
    rectF.bottom = getHeight();
    rectF.right = getWidth();
    this.mNotBuilt = false;
  }
  
  public float getRound() {
    return this.mRound;
  }
  
  public float getRoundPercent() {
    return this.mRoundPercent;
  }
  
  public float getScaleFromTextSize() {
    return this.mBaseTextSize;
  }
  
  public float getTextBackgroundPanX() {
    return this.mBackgroundPanX;
  }
  
  public float getTextBackgroundPanY() {
    return this.mBackgroundPanY;
  }
  
  public float getTextBackgroundRotate() {
    return this.mRotate;
  }
  
  public float getTextBackgroundZoom() {
    return this.mZoom;
  }
  
  public int getTextOutlineColor() {
    return this.mTextOutlineColor;
  }
  
  public float getTextPanX() {
    return this.mTextPanX;
  }
  
  public float getTextPanY() {
    return this.mTextPanY;
  }
  
  public float getTextureHeight() {
    return this.mTextureHeight;
  }
  
  public float getTextureWidth() {
    return this.mTextureWidth;
  }
  
  public Typeface getTypeface() {
    return this.mPaint.getTypeface();
  }
  
  public void layout(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
    this.mDeltaLeft = paramFloat1 - (int)(paramFloat1 + 0.5F);
    int i = (int)(paramFloat3 + 0.5F) - (int)(paramFloat1 + 0.5F);
    int j = (int)(paramFloat4 + 0.5F) - (int)(paramFloat2 + 0.5F);
    this.mFloatWidth = paramFloat3 - paramFloat1;
    this.mFloatHeight = paramFloat4 - paramFloat2;
    adjustTexture(paramFloat1, paramFloat2, paramFloat3, paramFloat4);
    if (getMeasuredHeight() != j || getMeasuredWidth() != i) {
      measure(View.MeasureSpec.makeMeasureSpec(i, 1073741824), View.MeasureSpec.makeMeasureSpec(j, 1073741824));
      super.layout((int)(paramFloat1 + 0.5F), (int)(paramFloat2 + 0.5F), (int)(paramFloat3 + 0.5F), (int)(0.5F + paramFloat4));
    } else {
      super.layout((int)(paramFloat1 + 0.5F), (int)(paramFloat2 + 0.5F), (int)(paramFloat3 + 0.5F), (int)(0.5F + paramFloat4));
    } 
    if (this.mAutoSize) {
      if (this.mTempRect == null) {
        this.mTempPaint = new Paint();
        this.mTempRect = new Rect();
        this.mTempPaint.set((Paint)this.mPaint);
        this.paintTextSize = this.mTempPaint.getTextSize();
      } 
      this.mFloatWidth = paramFloat3 - paramFloat1;
      this.mFloatHeight = paramFloat4 - paramFloat2;
      Paint paint = this.mTempPaint;
      String str = this.mText;
      paint.getTextBounds(str, 0, str.length(), this.mTempRect);
      i = this.mTempRect.width();
      float f = this.mTempRect.height() * 1.3F;
      paramFloat1 = paramFloat3 - paramFloat1 - this.mPaddingRight - this.mPaddingLeft;
      paramFloat2 = paramFloat4 - paramFloat2 - this.mPaddingBottom - this.mPaddingTop;
      if (i * paramFloat2 > f * paramFloat1) {
        this.mPaint.setTextSize(this.paintTextSize * paramFloat1 / i);
      } else {
        this.mPaint.setTextSize(this.paintTextSize * paramFloat2 / f);
      } 
      if (this.mUseOutline || !Float.isNaN(this.mBaseTextSize)) {
        if (Float.isNaN(this.mBaseTextSize)) {
          paramFloat1 = 1.0F;
        } else {
          paramFloat1 = this.mTextSize / this.mBaseTextSize;
        } 
        buildShape(paramFloat1);
      } 
    } 
  }
  
  public void layout(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    float f1;
    super.layout(paramInt1, paramInt2, paramInt3, paramInt4);
    boolean bool = Float.isNaN(this.mBaseTextSize);
    if (bool) {
      f1 = 1.0F;
    } else {
      f1 = this.mTextSize / this.mBaseTextSize;
    } 
    this.mFloatWidth = (paramInt3 - paramInt1);
    this.mFloatHeight = (paramInt4 - paramInt2);
    float f2 = f1;
    if (this.mAutoSize) {
      if (this.mTempRect == null) {
        this.mTempPaint = new Paint();
        this.mTempRect = new Rect();
        this.mTempPaint.set((Paint)this.mPaint);
        this.paintTextSize = this.mTempPaint.getTextSize();
      } 
      Paint paint = this.mTempPaint;
      String str = this.mText;
      paint.getTextBounds(str, 0, str.length(), this.mTempRect);
      int i = this.mTempRect.width();
      int j = (int)(this.mTempRect.height() * 1.3F);
      float f = this.mFloatWidth - this.mPaddingRight - this.mPaddingLeft;
      f2 = this.mFloatHeight - this.mPaddingBottom - this.mPaddingTop;
      if (bool) {
        if (i * f2 > j * f) {
          this.mPaint.setTextSize(this.paintTextSize * f / i);
          f2 = f1;
        } else {
          this.mPaint.setTextSize(this.paintTextSize * f2 / j);
          f2 = f1;
        } 
      } else {
        if (i * f2 > j * f) {
          f1 = f / i;
        } else {
          f1 = f2 / j;
        } 
        f2 = f1;
      } 
    } 
    if (this.mUseOutline || !bool) {
      adjustTexture(paramInt1, paramInt2, paramInt3, paramInt4);
      buildShape(f2);
    } 
  }
  
  protected void onDraw(Canvas paramCanvas) {
    float f;
    if (Float.isNaN(this.mBaseTextSize)) {
      f = 1.0F;
    } else {
      f = this.mTextSize / this.mBaseTextSize;
    } 
    super.onDraw(paramCanvas);
    if (!this.mUseOutline && f == 1.0F) {
      float f1 = this.mPaddingLeft;
      f = getHorizontalOffset();
      float f2 = this.mPaddingTop;
      float f3 = getVerticalOffset();
      paramCanvas.drawText(this.mText, this.mDeltaLeft + f1 + f, f2 + f3, (Paint)this.mPaint);
      return;
    } 
    if (this.mNotBuilt)
      buildShape(f); 
    if (this.mOutlinePositionMatrix == null)
      this.mOutlinePositionMatrix = new Matrix(); 
    if (this.mUseOutline) {
      this.paintCache.set((Paint)this.mPaint);
      this.mOutlinePositionMatrix.reset();
      float f2 = this.mPaddingLeft + getHorizontalOffset();
      float f1 = this.mPaddingTop + getVerticalOffset();
      this.mOutlinePositionMatrix.postTranslate(f2, f1);
      this.mOutlinePositionMatrix.preScale(f, f);
      this.mPath.transform(this.mOutlinePositionMatrix);
      if (this.mTextShader != null) {
        this.mPaint.setFilterBitmap(true);
        this.mPaint.setShader((Shader)this.mTextShader);
      } else {
        this.mPaint.setColor(this.mTextFillColor);
      } 
      this.mPaint.setStyle(Paint.Style.FILL);
      this.mPaint.setStrokeWidth(this.mTextOutlineThickness);
      paramCanvas.drawPath(this.mPath, (Paint)this.mPaint);
      if (this.mTextShader != null)
        this.mPaint.setShader(null); 
      this.mPaint.setColor(this.mTextOutlineColor);
      this.mPaint.setStyle(Paint.Style.STROKE);
      this.mPaint.setStrokeWidth(this.mTextOutlineThickness);
      paramCanvas.drawPath(this.mPath, (Paint)this.mPaint);
      this.mOutlinePositionMatrix.reset();
      this.mOutlinePositionMatrix.postTranslate(-f2, -f1);
      this.mPath.transform(this.mOutlinePositionMatrix);
      this.mPaint.set(this.paintCache);
    } else {
      float f1 = this.mPaddingLeft + getHorizontalOffset();
      f = this.mPaddingTop + getVerticalOffset();
      this.mOutlinePositionMatrix.reset();
      this.mOutlinePositionMatrix.preTranslate(f1, f);
      this.mPath.transform(this.mOutlinePositionMatrix);
      this.mPaint.setColor(this.mTextFillColor);
      this.mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
      this.mPaint.setStrokeWidth(this.mTextOutlineThickness);
      paramCanvas.drawPath(this.mPath, (Paint)this.mPaint);
      this.mOutlinePositionMatrix.reset();
      this.mOutlinePositionMatrix.preTranslate(-f1, -f);
      this.mPath.transform(this.mOutlinePositionMatrix);
    } 
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    int j;
    int i = View.MeasureSpec.getMode(paramInt1);
    int k = View.MeasureSpec.getMode(paramInt2);
    paramInt1 = View.MeasureSpec.getSize(paramInt1);
    paramInt2 = View.MeasureSpec.getSize(paramInt2);
    this.mAutoSize = false;
    this.mPaddingLeft = getPaddingLeft();
    this.mPaddingRight = getPaddingRight();
    this.mPaddingTop = getPaddingTop();
    this.mPaddingBottom = getPaddingBottom();
    if (i != 1073741824 || k != 1073741824) {
      TextPaint textPaint = this.mPaint;
      String str = this.mText;
      textPaint.getTextBounds(str, 0, str.length(), this.mTextBounds);
      if (i != 1073741824)
        paramInt1 = (int)(this.mTextBounds.width() + 0.99999F); 
      int m = paramInt1 + this.mPaddingLeft + this.mPaddingRight;
      j = m;
      i = paramInt2;
      if (k != 1073741824) {
        paramInt1 = (int)(this.mPaint.getFontMetricsInt(null) + 0.99999F);
        if (k == Integer.MIN_VALUE)
          paramInt1 = Math.min(paramInt2, paramInt1); 
        i = paramInt1 + this.mPaddingTop + this.mPaddingBottom;
        j = m;
      } 
    } else {
      j = paramInt1;
      i = paramInt2;
      if (this.mAutoSizeTextType != 0) {
        this.mAutoSize = true;
        j = paramInt1;
        i = paramInt2;
      } 
    } 
    setMeasuredDimension(j, i);
  }
  
  public void setGravity(int paramInt) {
    // Byte code:
    //   0: iload_1
    //   1: istore_2
    //   2: iload_1
    //   3: ldc_w 8388615
    //   6: iand
    //   7: ifne -> 16
    //   10: iload_1
    //   11: ldc_w 8388611
    //   14: ior
    //   15: istore_2
    //   16: iload_2
    //   17: istore_1
    //   18: iload_2
    //   19: bipush #112
    //   21: iand
    //   22: ifne -> 30
    //   25: iload_2
    //   26: bipush #48
    //   28: ior
    //   29: istore_1
    //   30: aload_0
    //   31: getfield mGravity : I
    //   34: istore_2
    //   35: iload_1
    //   36: ldc_w 8388615
    //   39: iand
    //   40: iload_2
    //   41: ldc_w 8388615
    //   44: iand
    //   45: if_icmpeq -> 48
    //   48: iload_1
    //   49: iload_2
    //   50: if_icmpeq -> 57
    //   53: aload_0
    //   54: invokevirtual invalidate : ()V
    //   57: aload_0
    //   58: iload_1
    //   59: putfield mGravity : I
    //   62: iload_1
    //   63: bipush #112
    //   65: iand
    //   66: lookupswitch default -> 92, 48 -> 108, 80 -> 100
    //   92: aload_0
    //   93: fconst_0
    //   94: putfield mTextPanY : F
    //   97: goto -> 115
    //   100: aload_0
    //   101: fconst_1
    //   102: putfield mTextPanY : F
    //   105: goto -> 115
    //   108: aload_0
    //   109: ldc_w -1.0
    //   112: putfield mTextPanY : F
    //   115: ldc_w 8388615
    //   118: iload_1
    //   119: iand
    //   120: lookupswitch default -> 164, 3 -> 180, 5 -> 172, 8388611 -> 180, 8388613 -> 172
    //   164: aload_0
    //   165: fconst_0
    //   166: putfield mTextPanX : F
    //   169: goto -> 187
    //   172: aload_0
    //   173: fconst_1
    //   174: putfield mTextPanX : F
    //   177: goto -> 187
    //   180: aload_0
    //   181: ldc_w -1.0
    //   184: putfield mTextPanX : F
    //   187: return
  }
  
  public void setRound(float paramFloat) {
    boolean bool;
    if (Float.isNaN(paramFloat)) {
      this.mRound = paramFloat;
      paramFloat = this.mRoundPercent;
      this.mRoundPercent = -1.0F;
      setRoundPercent(paramFloat);
      return;
    } 
    if (this.mRound != paramFloat) {
      bool = true;
    } else {
      bool = false;
    } 
    this.mRound = paramFloat;
    if (paramFloat != 0.0F) {
      if (this.mPath == null)
        this.mPath = new Path(); 
      if (this.mRect == null)
        this.mRect = new RectF(); 
      if (Build.VERSION.SDK_INT >= 21) {
        if (this.mViewOutlineProvider == null) {
          ViewOutlineProvider viewOutlineProvider = new ViewOutlineProvider() {
              final MotionLabel this$0;
              
              public void getOutline(View param1View, Outline param1Outline) {
                param1Outline.setRoundRect(0, 0, MotionLabel.this.getWidth(), MotionLabel.this.getHeight(), MotionLabel.this.mRound);
              }
            };
          this.mViewOutlineProvider = viewOutlineProvider;
          setOutlineProvider(viewOutlineProvider);
        } 
        setClipToOutline(true);
      } 
      int i = getWidth();
      int j = getHeight();
      this.mRect.set(0.0F, 0.0F, i, j);
      this.mPath.reset();
      Path path = this.mPath;
      RectF rectF = this.mRect;
      paramFloat = this.mRound;
      path.addRoundRect(rectF, paramFloat, paramFloat, Path.Direction.CW);
    } else if (Build.VERSION.SDK_INT >= 21) {
      setClipToOutline(false);
    } 
    if (bool && Build.VERSION.SDK_INT >= 21)
      invalidateOutline(); 
  }
  
  public void setRoundPercent(float paramFloat) {
    boolean bool;
    if (this.mRoundPercent != paramFloat) {
      bool = true;
    } else {
      bool = false;
    } 
    this.mRoundPercent = paramFloat;
    if (paramFloat != 0.0F) {
      if (this.mPath == null)
        this.mPath = new Path(); 
      if (this.mRect == null)
        this.mRect = new RectF(); 
      if (Build.VERSION.SDK_INT >= 21) {
        if (this.mViewOutlineProvider == null) {
          ViewOutlineProvider viewOutlineProvider = new ViewOutlineProvider() {
              final MotionLabel this$0;
              
              public void getOutline(View param1View, Outline param1Outline) {
                int j = MotionLabel.this.getWidth();
                int i = MotionLabel.this.getHeight();
                param1Outline.setRoundRect(0, 0, j, i, Math.min(j, i) * MotionLabel.this.mRoundPercent / 2.0F);
              }
            };
          this.mViewOutlineProvider = viewOutlineProvider;
          setOutlineProvider(viewOutlineProvider);
        } 
        setClipToOutline(true);
      } 
      int j = getWidth();
      int i = getHeight();
      paramFloat = Math.min(j, i) * this.mRoundPercent / 2.0F;
      this.mRect.set(0.0F, 0.0F, j, i);
      this.mPath.reset();
      this.mPath.addRoundRect(this.mRect, paramFloat, paramFloat, Path.Direction.CW);
    } else if (Build.VERSION.SDK_INT >= 21) {
      setClipToOutline(false);
    } 
    if (bool && Build.VERSION.SDK_INT >= 21)
      invalidateOutline(); 
  }
  
  public void setScaleFromTextSize(float paramFloat) {
    this.mBaseTextSize = paramFloat;
  }
  
  public void setText(CharSequence paramCharSequence) {
    this.mText = paramCharSequence.toString();
    invalidate();
  }
  
  public void setTextBackgroundPanX(float paramFloat) {
    this.mBackgroundPanX = paramFloat;
    updateShaderMatrix();
    invalidate();
  }
  
  public void setTextBackgroundPanY(float paramFloat) {
    this.mBackgroundPanY = paramFloat;
    updateShaderMatrix();
    invalidate();
  }
  
  public void setTextBackgroundRotate(float paramFloat) {
    this.mRotate = paramFloat;
    updateShaderMatrix();
    invalidate();
  }
  
  public void setTextBackgroundZoom(float paramFloat) {
    this.mZoom = paramFloat;
    updateShaderMatrix();
    invalidate();
  }
  
  public void setTextFillColor(int paramInt) {
    this.mTextFillColor = paramInt;
    invalidate();
  }
  
  public void setTextOutlineColor(int paramInt) {
    this.mTextOutlineColor = paramInt;
    this.mUseOutline = true;
    invalidate();
  }
  
  public void setTextOutlineThickness(float paramFloat) {
    this.mTextOutlineThickness = paramFloat;
    this.mUseOutline = true;
    if (Float.isNaN(paramFloat)) {
      this.mTextOutlineThickness = 1.0F;
      this.mUseOutline = false;
    } 
    invalidate();
  }
  
  public void setTextPanX(float paramFloat) {
    this.mTextPanX = paramFloat;
    invalidate();
  }
  
  public void setTextPanY(float paramFloat) {
    this.mTextPanY = paramFloat;
    invalidate();
  }
  
  public void setTextSize(float paramFloat) {
    this.mTextSize = paramFloat;
    String str1 = TAG;
    String str2 = Debug.getLoc();
    float f = this.mBaseTextSize;
    Log.v(str1, (new StringBuilder(String.valueOf(str2).length() + 35)).append(str2).append("  ").append(paramFloat).append(" / ").append(f).toString());
    TextPaint textPaint = this.mPaint;
    if (!Float.isNaN(this.mBaseTextSize))
      paramFloat = this.mBaseTextSize; 
    textPaint.setTextSize(paramFloat);
    if (Float.isNaN(this.mBaseTextSize)) {
      paramFloat = 1.0F;
    } else {
      paramFloat = this.mTextSize / this.mBaseTextSize;
    } 
    buildShape(paramFloat);
    requestLayout();
    invalidate();
  }
  
  public void setTextureHeight(float paramFloat) {
    this.mTextureHeight = paramFloat;
    updateShaderMatrix();
    invalidate();
  }
  
  public void setTextureWidth(float paramFloat) {
    this.mTextureWidth = paramFloat;
    updateShaderMatrix();
    invalidate();
  }
  
  public void setTypeface(Typeface paramTypeface) {
    if (this.mPaint.getTypeface() != paramTypeface) {
      this.mPaint.setTypeface(paramTypeface);
      if (this.mLayout != null) {
        this.mLayout = null;
        requestLayout();
        invalidate();
      } 
    } 
  }
  
  void setupPath() {
    this.mPaddingLeft = getPaddingLeft();
    this.mPaddingRight = getPaddingRight();
    this.mPaddingTop = getPaddingTop();
    this.mPaddingBottom = getPaddingBottom();
    setTypefaceFromAttrs(this.mFontFamily, this.mTypefaceIndex, this.mStyleIndex);
    this.mPaint.setColor(this.mTextFillColor);
    this.mPaint.setStrokeWidth(this.mTextOutlineThickness);
    this.mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    this.mPaint.setFlags(128);
    setTextSize(this.mTextSize);
    this.mPaint.setAntiAlias(true);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayou\\utils\widget\MotionLabel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */