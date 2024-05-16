package com.google.android.material.internal;

import android.animation.TimeInterpolator;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import androidx.core.math.MathUtils;
import androidx.core.text.TextDirectionHeuristicCompat;
import androidx.core.text.TextDirectionHeuristicsCompat;
import androidx.core.util.Preconditions;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import com.google.android.material.animation.AnimationUtils;
import com.google.android.material.resources.CancelableFontCallback;
import com.google.android.material.resources.TextAppearance;
import com.google.android.material.resources.TextAppearanceFontCallback;

public final class CollapsingTextHelper {
  private static final boolean DEBUG_DRAW = false;
  
  private static final Paint DEBUG_DRAW_PAINT;
  
  private static final String ELLIPSIS_NORMAL = "…";
  
  private static final float FADE_MODE_THRESHOLD_FRACTION_RELATIVE = 0.5F;
  
  private static final String TAG = "CollapsingTextHelper";
  
  private static final boolean USE_SCALING_TEXTURE;
  
  private boolean boundsChanged;
  
  private final Rect collapsedBounds;
  
  private float collapsedDrawX;
  
  private float collapsedDrawY;
  
  private CancelableFontCallback collapsedFontCallback;
  
  private float collapsedLetterSpacing;
  
  private ColorStateList collapsedShadowColor;
  
  private float collapsedShadowDx;
  
  private float collapsedShadowDy;
  
  private float collapsedShadowRadius;
  
  private float collapsedTextBlend;
  
  private ColorStateList collapsedTextColor;
  
  private int collapsedTextGravity = 16;
  
  private float collapsedTextSize = 15.0F;
  
  private Typeface collapsedTypeface;
  
  private final RectF currentBounds;
  
  private float currentDrawX;
  
  private float currentDrawY;
  
  private int currentOffsetY;
  
  private float currentTextSize;
  
  private Typeface currentTypeface;
  
  private boolean drawTitle;
  
  private final Rect expandedBounds;
  
  private float expandedDrawX;
  
  private float expandedDrawY;
  
  private float expandedFirstLineDrawX;
  
  private CancelableFontCallback expandedFontCallback;
  
  private float expandedFraction;
  
  private float expandedLetterSpacing;
  
  private ColorStateList expandedShadowColor;
  
  private float expandedShadowDx;
  
  private float expandedShadowDy;
  
  private float expandedShadowRadius;
  
  private float expandedTextBlend;
  
  private ColorStateList expandedTextColor;
  
  private int expandedTextGravity = 16;
  
  private float expandedTextSize = 15.0F;
  
  private Bitmap expandedTitleTexture;
  
  private Typeface expandedTypeface;
  
  private boolean fadeModeEnabled;
  
  private float fadeModeStartFraction;
  
  private float fadeModeThresholdFraction;
  
  private int hyphenationFrequency = StaticLayoutBuilderCompat.DEFAULT_HYPHENATION_FREQUENCY;
  
  private boolean isRtl;
  
  private boolean isRtlTextDirectionHeuristicsEnabled = true;
  
  private float lineSpacingAdd = 0.0F;
  
  private float lineSpacingMultiplier = 1.0F;
  
  private int maxLines = 1;
  
  private TimeInterpolator positionInterpolator;
  
  private float scale;
  
  private int[] state;
  
  private CharSequence text;
  
  private StaticLayout textLayout;
  
  private final TextPaint textPaint;
  
  private TimeInterpolator textSizeInterpolator;
  
  private CharSequence textToDraw;
  
  private CharSequence textToDrawCollapsed;
  
  private Paint texturePaint;
  
  private final TextPaint tmpPaint;
  
  private boolean useTexture;
  
  private final View view;
  
  static {
    boolean bool;
    if (Build.VERSION.SDK_INT < 18) {
      bool = true;
    } else {
      bool = false;
    } 
    USE_SCALING_TEXTURE = bool;
    DEBUG_DRAW_PAINT = null;
  }
  
  public CollapsingTextHelper(View paramView) {
    this.view = paramView;
    TextPaint textPaint = new TextPaint(129);
    this.textPaint = textPaint;
    this.tmpPaint = new TextPaint((Paint)textPaint);
    this.collapsedBounds = new Rect();
    this.expandedBounds = new Rect();
    this.currentBounds = new RectF();
    this.fadeModeThresholdFraction = calculateFadeModeThresholdFraction();
  }
  
  private static int blendColors(int paramInt1, int paramInt2, float paramFloat) {
    float f2 = 1.0F - paramFloat;
    float f3 = Color.alpha(paramInt1);
    float f4 = Color.alpha(paramInt2);
    float f8 = Color.red(paramInt1);
    float f5 = Color.red(paramInt2);
    float f6 = Color.green(paramInt1);
    float f9 = Color.green(paramInt2);
    float f7 = Color.blue(paramInt1);
    float f1 = Color.blue(paramInt2);
    return Color.argb((int)(f3 * f2 + f4 * paramFloat), (int)(f8 * f2 + f5 * paramFloat), (int)(f6 * f2 + f9 * paramFloat), (int)(f7 * f2 + f1 * paramFloat));
  }
  
  private void calculateBaseOffsets(boolean paramBoolean) {
    float f1;
    float f2;
    float f3;
    float f5 = this.currentTextSize;
    calculateUsingTextSize(this.collapsedTextSize, paramBoolean);
    CharSequence charSequence2 = this.textToDraw;
    if (charSequence2 != null) {
      StaticLayout staticLayout = this.textLayout;
      if (staticLayout != null)
        this.textToDrawCollapsed = TextUtils.ellipsize(charSequence2, this.textPaint, staticLayout.getWidth(), TextUtils.TruncateAt.END); 
    } 
    charSequence2 = this.textToDrawCollapsed;
    float f4 = 0.0F;
    if (charSequence2 != null) {
      f1 = this.textPaint.measureText(charSequence2, 0, charSequence2.length());
    } else {
      f1 = 0.0F;
    } 
    int i = GravityCompat.getAbsoluteGravity(this.collapsedTextGravity, this.isRtl);
    switch (i & 0x70) {
      default:
        f2 = (this.textPaint.descent() - this.textPaint.ascent()) / 2.0F;
        this.collapsedDrawY = this.collapsedBounds.centerY() - f2;
        break;
      case 80:
        this.collapsedDrawY = this.collapsedBounds.bottom + this.textPaint.ascent();
        break;
      case 48:
        this.collapsedDrawY = this.collapsedBounds.top;
        break;
    } 
    switch (i & 0x800007) {
      default:
        this.collapsedDrawX = this.collapsedBounds.left;
        break;
      case 5:
        this.collapsedDrawX = this.collapsedBounds.right - f1;
        break;
      case 1:
        this.collapsedDrawX = this.collapsedBounds.centerX() - f1 / 2.0F;
        break;
    } 
    calculateUsingTextSize(this.expandedTextSize, paramBoolean);
    StaticLayout staticLayout2 = this.textLayout;
    if (staticLayout2 != null) {
      f2 = staticLayout2.getHeight();
    } else {
      f2 = 0.0F;
    } 
    CharSequence charSequence1 = this.textToDraw;
    if (charSequence1 != null) {
      f1 = this.textPaint.measureText(charSequence1, 0, charSequence1.length());
    } else {
      f1 = 0.0F;
    } 
    StaticLayout staticLayout1 = this.textLayout;
    if (staticLayout1 != null && this.maxLines > 1) {
      f3 = staticLayout1.getWidth();
    } else {
      f3 = f1;
    } 
    staticLayout1 = this.textLayout;
    if (staticLayout1 != null) {
      if (this.maxLines > 1) {
        f1 = staticLayout1.getLineStart(0);
      } else {
        f1 = staticLayout1.getLineLeft(0);
      } 
    } else {
      f1 = f4;
    } 
    this.expandedFirstLineDrawX = f1;
    i = GravityCompat.getAbsoluteGravity(this.expandedTextGravity, this.isRtl);
    switch (i & 0x70) {
      default:
        f1 = f2 / 2.0F;
        this.expandedDrawY = this.expandedBounds.centerY() - f1;
        break;
      case 80:
        this.expandedDrawY = this.expandedBounds.bottom - f2 + this.textPaint.descent();
        break;
      case 48:
        this.expandedDrawY = this.expandedBounds.top;
        break;
    } 
    switch (i & 0x800007) {
      default:
        this.expandedDrawX = this.expandedBounds.left;
        break;
      case 5:
        this.expandedDrawX = this.expandedBounds.right - f3;
        break;
      case 1:
        this.expandedDrawX = this.expandedBounds.centerX() - f3 / 2.0F;
        break;
    } 
    clearTexture();
    setInterpolatedTextSize(f5);
  }
  
  private void calculateCurrentOffsets() {
    calculateOffsets(this.expandedFraction);
  }
  
  private float calculateFadeModeTextAlpha(float paramFloat) {
    float f = this.fadeModeThresholdFraction;
    return (paramFloat <= f) ? AnimationUtils.lerp(1.0F, 0.0F, this.fadeModeStartFraction, f, paramFloat) : AnimationUtils.lerp(0.0F, 1.0F, f, 1.0F, paramFloat);
  }
  
  private float calculateFadeModeThresholdFraction() {
    float f = this.fadeModeStartFraction;
    return f + (1.0F - f) * 0.5F;
  }
  
  private boolean calculateIsRtl(CharSequence paramCharSequence) {
    boolean bool = isDefaultIsRtl();
    if (this.isRtlTextDirectionHeuristicsEnabled)
      bool = isTextDirectionHeuristicsIsRtl(paramCharSequence, bool); 
    return bool;
  }
  
  private void calculateOffsets(float paramFloat) {
    float f;
    interpolateBounds(paramFloat);
    if (this.fadeModeEnabled) {
      if (paramFloat < this.fadeModeThresholdFraction) {
        f = 0.0F;
        this.currentDrawX = this.expandedDrawX;
        this.currentDrawY = this.expandedDrawY;
        setInterpolatedTextSize(this.expandedTextSize);
      } else {
        f = 1.0F;
        this.currentDrawX = this.collapsedDrawX;
        this.currentDrawY = this.collapsedDrawY - Math.max(0, this.currentOffsetY);
        setInterpolatedTextSize(this.collapsedTextSize);
      } 
    } else {
      f = paramFloat;
      this.currentDrawX = lerp(this.expandedDrawX, this.collapsedDrawX, paramFloat, this.positionInterpolator);
      this.currentDrawY = lerp(this.expandedDrawY, this.collapsedDrawY, paramFloat, this.positionInterpolator);
      setInterpolatedTextSize(lerp(this.expandedTextSize, this.collapsedTextSize, paramFloat, this.textSizeInterpolator));
    } 
    setCollapsedTextBlend(1.0F - lerp(0.0F, 1.0F, 1.0F - paramFloat, AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR));
    setExpandedTextBlend(lerp(1.0F, 0.0F, paramFloat, AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR));
    if (this.collapsedTextColor != this.expandedTextColor) {
      this.textPaint.setColor(blendColors(getCurrentExpandedTextColor(), getCurrentCollapsedTextColor(), f));
    } else {
      this.textPaint.setColor(getCurrentCollapsedTextColor());
    } 
    if (Build.VERSION.SDK_INT >= 21) {
      f = this.collapsedLetterSpacing;
      float f1 = this.expandedLetterSpacing;
      if (f != f1) {
        this.textPaint.setLetterSpacing(lerp(f1, f, paramFloat, AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR));
      } else {
        this.textPaint.setLetterSpacing(f);
      } 
    } 
    this.textPaint.setShadowLayer(lerp(this.expandedShadowRadius, this.collapsedShadowRadius, paramFloat, null), lerp(this.expandedShadowDx, this.collapsedShadowDx, paramFloat, null), lerp(this.expandedShadowDy, this.collapsedShadowDy, paramFloat, null), blendColors(getCurrentColor(this.expandedShadowColor), getCurrentColor(this.collapsedShadowColor), paramFloat));
    if (this.fadeModeEnabled) {
      int i = (int)(calculateFadeModeTextAlpha(paramFloat) * 255.0F);
      this.textPaint.setAlpha(i);
    } 
    ViewCompat.postInvalidateOnAnimation(this.view);
  }
  
  private void calculateUsingTextSize(float paramFloat) {
    calculateUsingTextSize(paramFloat, false);
  }
  
  private void calculateUsingTextSize(float paramFloat, boolean paramBoolean) {
    if (this.text == null)
      return; 
    float f2 = this.collapsedBounds.width();
    float f1 = this.expandedBounds.width();
    int j = 0;
    int i = 0;
    if (isClose(paramFloat, this.collapsedTextSize)) {
      f1 = this.collapsedTextSize;
      this.scale = 1.0F;
      Typeface typeface1 = this.currentTypeface;
      Typeface typeface2 = this.collapsedTypeface;
      if (typeface1 != typeface2) {
        this.currentTypeface = typeface2;
        i = 1;
      } 
      paramFloat = f2;
    } else {
      float f = this.expandedTextSize;
      Typeface typeface2 = this.currentTypeface;
      Typeface typeface1 = this.expandedTypeface;
      i = j;
      if (typeface2 != typeface1) {
        this.currentTypeface = typeface1;
        i = 1;
      } 
      if (isClose(paramFloat, this.expandedTextSize)) {
        this.scale = 1.0F;
      } else {
        this.scale = paramFloat / this.expandedTextSize;
      } 
      paramFloat = this.collapsedTextSize / this.expandedTextSize;
      if (paramBoolean) {
        paramFloat = f1;
        f1 = f;
      } else {
        if (f1 * paramFloat > f2) {
          paramFloat = Math.min(f2 / paramFloat, f1);
        } else {
          paramFloat = f1;
        } 
        f1 = f;
      } 
    } 
    paramBoolean = false;
    byte b = 1;
    j = i;
    if (paramFloat > 0.0F) {
      if (this.currentTextSize != f1 || this.boundsChanged || i) {
        i = 1;
      } else {
        i = 0;
      } 
      this.currentTextSize = f1;
      this.boundsChanged = false;
      j = i;
    } 
    if (this.textToDraw == null || j) {
      this.textPaint.setTextSize(this.currentTextSize);
      this.textPaint.setTypeface(this.currentTypeface);
      TextPaint textPaint = this.textPaint;
      if (this.scale != 1.0F)
        paramBoolean = true; 
      textPaint.setLinearText(paramBoolean);
      this.isRtl = calculateIsRtl(this.text);
      i = b;
      if (shouldDrawMultiline())
        i = this.maxLines; 
      StaticLayout staticLayout = createStaticLayout(i, paramFloat, this.isRtl);
      this.textLayout = staticLayout;
      this.textToDraw = staticLayout.getText();
    } 
  }
  
  private void clearTexture() {
    Bitmap bitmap = this.expandedTitleTexture;
    if (bitmap != null) {
      bitmap.recycle();
      this.expandedTitleTexture = null;
    } 
  }
  
  private StaticLayout createStaticLayout(int paramInt, float paramFloat, boolean paramBoolean) {
    StaticLayout staticLayout = null;
    try {
      StaticLayout staticLayout1 = StaticLayoutBuilderCompat.obtain(this.text, this.textPaint, (int)paramFloat).setEllipsize(TextUtils.TruncateAt.END).setIsRtl(paramBoolean).setAlignment(Layout.Alignment.ALIGN_NORMAL).setIncludePad(false).setMaxLines(paramInt).setLineSpacing(this.lineSpacingAdd, this.lineSpacingMultiplier).setHyphenationFrequency(this.hyphenationFrequency).build();
      staticLayout = staticLayout1;
    } catch (StaticLayoutBuilderCompatException staticLayoutBuilderCompatException) {
      Log.e("CollapsingTextHelper", staticLayoutBuilderCompatException.getCause().getMessage(), staticLayoutBuilderCompatException);
    } 
    return (StaticLayout)Preconditions.checkNotNull(staticLayout);
  }
  
  private void drawMultilineTransition(Canvas paramCanvas, float paramFloat1, float paramFloat2) {
    int j = this.textPaint.getAlpha();
    paramCanvas.translate(paramFloat1, paramFloat2);
    this.textPaint.setAlpha((int)(this.expandedTextBlend * j));
    this.textLayout.draw(paramCanvas);
    this.textPaint.setAlpha((int)(this.collapsedTextBlend * j));
    int i = this.textLayout.getLineBaseline(0);
    CharSequence charSequence = this.textToDrawCollapsed;
    paramCanvas.drawText(charSequence, 0, charSequence.length(), 0.0F, i, (Paint)this.textPaint);
    if (!this.fadeModeEnabled) {
      String str = this.textToDrawCollapsed.toString().trim();
      charSequence = str;
      if (str.endsWith("…"))
        charSequence = str.substring(0, str.length() - 1); 
      this.textPaint.setAlpha(j);
      paramCanvas.drawText((String)charSequence, 0, Math.min(this.textLayout.getLineEnd(0), charSequence.length()), 0.0F, i, (Paint)this.textPaint);
    } 
  }
  
  private void ensureExpandedTexture() {
    if (this.expandedTitleTexture != null || this.expandedBounds.isEmpty() || TextUtils.isEmpty(this.textToDraw))
      return; 
    calculateOffsets(0.0F);
    int i = this.textLayout.getWidth();
    int j = this.textLayout.getHeight();
    if (i <= 0 || j <= 0)
      return; 
    this.expandedTitleTexture = Bitmap.createBitmap(i, j, Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(this.expandedTitleTexture);
    this.textLayout.draw(canvas);
    if (this.texturePaint == null)
      this.texturePaint = new Paint(3); 
  }
  
  private float getCollapsedTextLeftBound(int paramInt1, int paramInt2) {
    float f;
    if (paramInt2 == 17 || (paramInt2 & 0x7) == 1)
      return paramInt1 / 2.0F - calculateCollapsedTextWidth() / 2.0F; 
    if ((paramInt2 & 0x800005) == 8388613 || (paramInt2 & 0x5) == 5) {
      if (this.isRtl) {
        f = this.collapsedBounds.left;
      } else {
        f = this.collapsedBounds.right - calculateCollapsedTextWidth();
      } 
      return f;
    } 
    if (this.isRtl) {
      f = this.collapsedBounds.right - calculateCollapsedTextWidth();
    } else {
      f = this.collapsedBounds.left;
    } 
    return f;
  }
  
  private float getCollapsedTextRightBound(RectF paramRectF, int paramInt1, int paramInt2) {
    float f;
    if (paramInt2 == 17 || (paramInt2 & 0x7) == 1)
      return paramInt1 / 2.0F + calculateCollapsedTextWidth() / 2.0F; 
    if ((paramInt2 & 0x800005) == 8388613 || (paramInt2 & 0x5) == 5) {
      if (this.isRtl) {
        f = paramRectF.left + calculateCollapsedTextWidth();
      } else {
        f = this.collapsedBounds.right;
      } 
      return f;
    } 
    if (this.isRtl) {
      f = this.collapsedBounds.right;
    } else {
      f = paramRectF.left + calculateCollapsedTextWidth();
    } 
    return f;
  }
  
  private int getCurrentColor(ColorStateList paramColorStateList) {
    if (paramColorStateList == null)
      return 0; 
    int[] arrayOfInt = this.state;
    return (arrayOfInt != null) ? paramColorStateList.getColorForState(arrayOfInt, 0) : paramColorStateList.getDefaultColor();
  }
  
  private int getCurrentExpandedTextColor() {
    return getCurrentColor(this.expandedTextColor);
  }
  
  private void getTextPaintCollapsed(TextPaint paramTextPaint) {
    paramTextPaint.setTextSize(this.collapsedTextSize);
    paramTextPaint.setTypeface(this.collapsedTypeface);
    if (Build.VERSION.SDK_INT >= 21)
      paramTextPaint.setLetterSpacing(this.collapsedLetterSpacing); 
  }
  
  private void getTextPaintExpanded(TextPaint paramTextPaint) {
    paramTextPaint.setTextSize(this.expandedTextSize);
    paramTextPaint.setTypeface(this.expandedTypeface);
    if (Build.VERSION.SDK_INT >= 21)
      paramTextPaint.setLetterSpacing(this.expandedLetterSpacing); 
  }
  
  private void interpolateBounds(float paramFloat) {
    if (this.fadeModeEnabled) {
      Rect rect;
      RectF rectF = this.currentBounds;
      if (paramFloat < this.fadeModeThresholdFraction) {
        rect = this.expandedBounds;
      } else {
        rect = this.collapsedBounds;
      } 
      rectF.set(rect);
    } else {
      this.currentBounds.left = lerp(this.expandedBounds.left, this.collapsedBounds.left, paramFloat, this.positionInterpolator);
      this.currentBounds.top = lerp(this.expandedDrawY, this.collapsedDrawY, paramFloat, this.positionInterpolator);
      this.currentBounds.right = lerp(this.expandedBounds.right, this.collapsedBounds.right, paramFloat, this.positionInterpolator);
      this.currentBounds.bottom = lerp(this.expandedBounds.bottom, this.collapsedBounds.bottom, paramFloat, this.positionInterpolator);
    } 
  }
  
  private static boolean isClose(float paramFloat1, float paramFloat2) {
    boolean bool;
    if (Math.abs(paramFloat1 - paramFloat2) < 0.001F) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private boolean isDefaultIsRtl() {
    int i = ViewCompat.getLayoutDirection(this.view);
    boolean bool = true;
    if (i != 1)
      bool = false; 
    return bool;
  }
  
  private boolean isTextDirectionHeuristicsIsRtl(CharSequence paramCharSequence, boolean paramBoolean) {
    TextDirectionHeuristicCompat textDirectionHeuristicCompat;
    if (paramBoolean) {
      textDirectionHeuristicCompat = TextDirectionHeuristicsCompat.FIRSTSTRONG_RTL;
    } else {
      textDirectionHeuristicCompat = TextDirectionHeuristicsCompat.FIRSTSTRONG_LTR;
    } 
    return textDirectionHeuristicCompat.isRtl(paramCharSequence, 0, paramCharSequence.length());
  }
  
  private static float lerp(float paramFloat1, float paramFloat2, float paramFloat3, TimeInterpolator paramTimeInterpolator) {
    float f = paramFloat3;
    if (paramTimeInterpolator != null)
      f = paramTimeInterpolator.getInterpolation(paramFloat3); 
    return AnimationUtils.lerp(paramFloat1, paramFloat2, f);
  }
  
  private static boolean rectEquals(Rect paramRect, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    boolean bool;
    if (paramRect.left == paramInt1 && paramRect.top == paramInt2 && paramRect.right == paramInt3 && paramRect.bottom == paramInt4) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private void setCollapsedTextBlend(float paramFloat) {
    this.collapsedTextBlend = paramFloat;
    ViewCompat.postInvalidateOnAnimation(this.view);
  }
  
  private boolean setCollapsedTypefaceInternal(Typeface paramTypeface) {
    CancelableFontCallback cancelableFontCallback = this.collapsedFontCallback;
    if (cancelableFontCallback != null)
      cancelableFontCallback.cancel(); 
    if (this.collapsedTypeface != paramTypeface) {
      this.collapsedTypeface = paramTypeface;
      return true;
    } 
    return false;
  }
  
  private void setExpandedTextBlend(float paramFloat) {
    this.expandedTextBlend = paramFloat;
    ViewCompat.postInvalidateOnAnimation(this.view);
  }
  
  private boolean setExpandedTypefaceInternal(Typeface paramTypeface) {
    CancelableFontCallback cancelableFontCallback = this.expandedFontCallback;
    if (cancelableFontCallback != null)
      cancelableFontCallback.cancel(); 
    if (this.expandedTypeface != paramTypeface) {
      this.expandedTypeface = paramTypeface;
      return true;
    } 
    return false;
  }
  
  private void setInterpolatedTextSize(float paramFloat) {
    boolean bool;
    calculateUsingTextSize(paramFloat);
    if (USE_SCALING_TEXTURE && this.scale != 1.0F) {
      bool = true;
    } else {
      bool = false;
    } 
    this.useTexture = bool;
    if (bool)
      ensureExpandedTexture(); 
    ViewCompat.postInvalidateOnAnimation(this.view);
  }
  
  private boolean shouldDrawMultiline() {
    int i = this.maxLines;
    boolean bool = true;
    if (i <= 1 || (this.isRtl && !this.fadeModeEnabled) || this.useTexture)
      bool = false; 
    return bool;
  }
  
  public float calculateCollapsedTextWidth() {
    if (this.text == null)
      return 0.0F; 
    getTextPaintCollapsed(this.tmpPaint);
    TextPaint textPaint = this.tmpPaint;
    CharSequence charSequence = this.text;
    return textPaint.measureText(charSequence, 0, charSequence.length());
  }
  
  public void draw(Canvas paramCanvas) {
    int i = paramCanvas.save();
    if (this.textToDraw != null && this.drawTitle) {
      float f1;
      int j = this.maxLines;
      boolean bool = true;
      if (j > 1) {
        f1 = this.textLayout.getLineStart(0);
      } else {
        f1 = this.textLayout.getLineLeft(0);
      } 
      float f3 = this.currentDrawX;
      float f6 = this.expandedFirstLineDrawX;
      this.textPaint.setTextSize(this.currentTextSize);
      float f5 = this.currentDrawX;
      float f2 = this.currentDrawY;
      if (!this.useTexture || this.expandedTitleTexture == null)
        bool = false; 
      float f4 = this.scale;
      if (f4 != 1.0F && !this.fadeModeEnabled)
        paramCanvas.scale(f4, f4, f5, f2); 
      if (bool) {
        paramCanvas.drawBitmap(this.expandedTitleTexture, f5, f2, this.texturePaint);
        paramCanvas.restoreToCount(i);
        return;
      } 
      if (shouldDrawMultiline() && (!this.fadeModeEnabled || this.expandedFraction > this.fadeModeThresholdFraction)) {
        drawMultilineTransition(paramCanvas, f3 + f1 - f6 * 2.0F, f2);
      } else {
        paramCanvas.translate(f5, f2);
        this.textLayout.draw(paramCanvas);
      } 
      paramCanvas.restoreToCount(i);
    } 
  }
  
  public void getCollapsedTextActualBounds(RectF paramRectF, int paramInt1, int paramInt2) {
    this.isRtl = calculateIsRtl(this.text);
    paramRectF.left = getCollapsedTextLeftBound(paramInt1, paramInt2);
    paramRectF.top = this.collapsedBounds.top;
    paramRectF.right = getCollapsedTextRightBound(paramRectF, paramInt1, paramInt2);
    paramRectF.bottom = this.collapsedBounds.top + getCollapsedTextHeight();
  }
  
  public ColorStateList getCollapsedTextColor() {
    return this.collapsedTextColor;
  }
  
  public int getCollapsedTextGravity() {
    return this.collapsedTextGravity;
  }
  
  public float getCollapsedTextHeight() {
    getTextPaintCollapsed(this.tmpPaint);
    return -this.tmpPaint.ascent();
  }
  
  public float getCollapsedTextSize() {
    return this.collapsedTextSize;
  }
  
  public Typeface getCollapsedTypeface() {
    Typeface typeface = this.collapsedTypeface;
    if (typeface == null)
      typeface = Typeface.DEFAULT; 
    return typeface;
  }
  
  public int getCurrentCollapsedTextColor() {
    return getCurrentColor(this.collapsedTextColor);
  }
  
  public ColorStateList getExpandedTextColor() {
    return this.expandedTextColor;
  }
  
  public float getExpandedTextFullHeight() {
    getTextPaintExpanded(this.tmpPaint);
    return -this.tmpPaint.ascent() + this.tmpPaint.descent();
  }
  
  public int getExpandedTextGravity() {
    return this.expandedTextGravity;
  }
  
  public float getExpandedTextHeight() {
    getTextPaintExpanded(this.tmpPaint);
    return -this.tmpPaint.ascent();
  }
  
  public float getExpandedTextSize() {
    return this.expandedTextSize;
  }
  
  public Typeface getExpandedTypeface() {
    Typeface typeface = this.expandedTypeface;
    if (typeface == null)
      typeface = Typeface.DEFAULT; 
    return typeface;
  }
  
  public float getExpansionFraction() {
    return this.expandedFraction;
  }
  
  public float getFadeModeThresholdFraction() {
    return this.fadeModeThresholdFraction;
  }
  
  public int getHyphenationFrequency() {
    return this.hyphenationFrequency;
  }
  
  public int getLineCount() {
    boolean bool;
    StaticLayout staticLayout = this.textLayout;
    if (staticLayout != null) {
      bool = staticLayout.getLineCount();
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public float getLineSpacingAdd() {
    return this.textLayout.getSpacingAdd();
  }
  
  public float getLineSpacingMultiplier() {
    return this.textLayout.getSpacingMultiplier();
  }
  
  public int getMaxLines() {
    return this.maxLines;
  }
  
  public CharSequence getText() {
    return this.text;
  }
  
  public boolean isRtlTextDirectionHeuristicsEnabled() {
    return this.isRtlTextDirectionHeuristicsEnabled;
  }
  
  public final boolean isStateful() {
    ColorStateList colorStateList = this.collapsedTextColor;
    if (colorStateList == null || !colorStateList.isStateful()) {
      colorStateList = this.expandedTextColor;
      return (colorStateList != null && colorStateList.isStateful());
    } 
    return true;
  }
  
  void onBoundsChanged() {
    boolean bool;
    if (this.collapsedBounds.width() > 0 && this.collapsedBounds.height() > 0 && this.expandedBounds.width() > 0 && this.expandedBounds.height() > 0) {
      bool = true;
    } else {
      bool = false;
    } 
    this.drawTitle = bool;
  }
  
  public void recalculate() {
    recalculate(false);
  }
  
  public void recalculate(boolean paramBoolean) {
    if ((this.view.getHeight() > 0 && this.view.getWidth() > 0) || paramBoolean) {
      calculateBaseOffsets(paramBoolean);
      calculateCurrentOffsets();
    } 
  }
  
  public void setCollapsedBounds(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    if (!rectEquals(this.collapsedBounds, paramInt1, paramInt2, paramInt3, paramInt4)) {
      this.collapsedBounds.set(paramInt1, paramInt2, paramInt3, paramInt4);
      this.boundsChanged = true;
      onBoundsChanged();
    } 
  }
  
  public void setCollapsedBounds(Rect paramRect) {
    setCollapsedBounds(paramRect.left, paramRect.top, paramRect.right, paramRect.bottom);
  }
  
  public void setCollapsedTextAppearance(int paramInt) {
    TextAppearance textAppearance = new TextAppearance(this.view.getContext(), paramInt);
    if (textAppearance.textColor != null)
      this.collapsedTextColor = textAppearance.textColor; 
    if (textAppearance.textSize != 0.0F)
      this.collapsedTextSize = textAppearance.textSize; 
    if (textAppearance.shadowColor != null)
      this.collapsedShadowColor = textAppearance.shadowColor; 
    this.collapsedShadowDx = textAppearance.shadowDx;
    this.collapsedShadowDy = textAppearance.shadowDy;
    this.collapsedShadowRadius = textAppearance.shadowRadius;
    this.collapsedLetterSpacing = textAppearance.letterSpacing;
    CancelableFontCallback cancelableFontCallback = this.collapsedFontCallback;
    if (cancelableFontCallback != null)
      cancelableFontCallback.cancel(); 
    this.collapsedFontCallback = new CancelableFontCallback(new CancelableFontCallback.ApplyFont() {
          final CollapsingTextHelper this$0;
          
          public void apply(Typeface param1Typeface) {
            CollapsingTextHelper.this.setCollapsedTypeface(param1Typeface);
          }
        },  textAppearance.getFallbackFont());
    textAppearance.getFontAsync(this.view.getContext(), (TextAppearanceFontCallback)this.collapsedFontCallback);
    recalculate();
  }
  
  public void setCollapsedTextColor(ColorStateList paramColorStateList) {
    if (this.collapsedTextColor != paramColorStateList) {
      this.collapsedTextColor = paramColorStateList;
      recalculate();
    } 
  }
  
  public void setCollapsedTextGravity(int paramInt) {
    if (this.collapsedTextGravity != paramInt) {
      this.collapsedTextGravity = paramInt;
      recalculate();
    } 
  }
  
  public void setCollapsedTextSize(float paramFloat) {
    if (this.collapsedTextSize != paramFloat) {
      this.collapsedTextSize = paramFloat;
      recalculate();
    } 
  }
  
  public void setCollapsedTypeface(Typeface paramTypeface) {
    if (setCollapsedTypefaceInternal(paramTypeface))
      recalculate(); 
  }
  
  public void setCurrentOffsetY(int paramInt) {
    this.currentOffsetY = paramInt;
  }
  
  public void setExpandedBounds(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    if (!rectEquals(this.expandedBounds, paramInt1, paramInt2, paramInt3, paramInt4)) {
      this.expandedBounds.set(paramInt1, paramInt2, paramInt3, paramInt4);
      this.boundsChanged = true;
      onBoundsChanged();
    } 
  }
  
  public void setExpandedBounds(Rect paramRect) {
    setExpandedBounds(paramRect.left, paramRect.top, paramRect.right, paramRect.bottom);
  }
  
  public void setExpandedTextAppearance(int paramInt) {
    TextAppearance textAppearance = new TextAppearance(this.view.getContext(), paramInt);
    if (textAppearance.textColor != null)
      this.expandedTextColor = textAppearance.textColor; 
    if (textAppearance.textSize != 0.0F)
      this.expandedTextSize = textAppearance.textSize; 
    if (textAppearance.shadowColor != null)
      this.expandedShadowColor = textAppearance.shadowColor; 
    this.expandedShadowDx = textAppearance.shadowDx;
    this.expandedShadowDy = textAppearance.shadowDy;
    this.expandedShadowRadius = textAppearance.shadowRadius;
    this.expandedLetterSpacing = textAppearance.letterSpacing;
    CancelableFontCallback cancelableFontCallback = this.expandedFontCallback;
    if (cancelableFontCallback != null)
      cancelableFontCallback.cancel(); 
    this.expandedFontCallback = new CancelableFontCallback(new CancelableFontCallback.ApplyFont() {
          final CollapsingTextHelper this$0;
          
          public void apply(Typeface param1Typeface) {
            CollapsingTextHelper.this.setExpandedTypeface(param1Typeface);
          }
        },  textAppearance.getFallbackFont());
    textAppearance.getFontAsync(this.view.getContext(), (TextAppearanceFontCallback)this.expandedFontCallback);
    recalculate();
  }
  
  public void setExpandedTextColor(ColorStateList paramColorStateList) {
    if (this.expandedTextColor != paramColorStateList) {
      this.expandedTextColor = paramColorStateList;
      recalculate();
    } 
  }
  
  public void setExpandedTextGravity(int paramInt) {
    if (this.expandedTextGravity != paramInt) {
      this.expandedTextGravity = paramInt;
      recalculate();
    } 
  }
  
  public void setExpandedTextSize(float paramFloat) {
    if (this.expandedTextSize != paramFloat) {
      this.expandedTextSize = paramFloat;
      recalculate();
    } 
  }
  
  public void setExpandedTypeface(Typeface paramTypeface) {
    if (setExpandedTypefaceInternal(paramTypeface))
      recalculate(); 
  }
  
  public void setExpansionFraction(float paramFloat) {
    paramFloat = MathUtils.clamp(paramFloat, 0.0F, 1.0F);
    if (paramFloat != this.expandedFraction) {
      this.expandedFraction = paramFloat;
      calculateCurrentOffsets();
    } 
  }
  
  public void setFadeModeEnabled(boolean paramBoolean) {
    this.fadeModeEnabled = paramBoolean;
  }
  
  public void setFadeModeStartFraction(float paramFloat) {
    this.fadeModeStartFraction = paramFloat;
    this.fadeModeThresholdFraction = calculateFadeModeThresholdFraction();
  }
  
  public void setHyphenationFrequency(int paramInt) {
    this.hyphenationFrequency = paramInt;
  }
  
  public void setLineSpacingAdd(float paramFloat) {
    this.lineSpacingAdd = paramFloat;
  }
  
  public void setLineSpacingMultiplier(float paramFloat) {
    this.lineSpacingMultiplier = paramFloat;
  }
  
  public void setMaxLines(int paramInt) {
    if (paramInt != this.maxLines) {
      this.maxLines = paramInt;
      clearTexture();
      recalculate();
    } 
  }
  
  public void setPositionInterpolator(TimeInterpolator paramTimeInterpolator) {
    this.positionInterpolator = paramTimeInterpolator;
    recalculate();
  }
  
  public void setRtlTextDirectionHeuristicsEnabled(boolean paramBoolean) {
    this.isRtlTextDirectionHeuristicsEnabled = paramBoolean;
  }
  
  public final boolean setState(int[] paramArrayOfint) {
    this.state = paramArrayOfint;
    if (isStateful()) {
      recalculate();
      return true;
    } 
    return false;
  }
  
  public void setText(CharSequence paramCharSequence) {
    if (paramCharSequence == null || !TextUtils.equals(this.text, paramCharSequence)) {
      this.text = paramCharSequence;
      this.textToDraw = null;
      clearTexture();
      recalculate();
    } 
  }
  
  public void setTextSizeInterpolator(TimeInterpolator paramTimeInterpolator) {
    this.textSizeInterpolator = paramTimeInterpolator;
    recalculate();
  }
  
  public void setTypefaces(Typeface paramTypeface) {
    boolean bool2 = setCollapsedTypefaceInternal(paramTypeface);
    boolean bool1 = setExpandedTypefaceInternal(paramTypeface);
    if (bool2 || bool1)
      recalculate(); 
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\internal\CollapsingTextHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */