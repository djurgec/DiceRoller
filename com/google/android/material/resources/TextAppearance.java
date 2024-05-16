package com.google.android.material.resources;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;
import android.text.TextPaint;
import android.util.Log;
import androidx.core.content.res.ResourcesCompat;
import com.google.android.material.R;

public class TextAppearance {
  private static final String TAG = "TextAppearance";
  
  private static final int TYPEFACE_MONOSPACE = 3;
  
  private static final int TYPEFACE_SANS = 1;
  
  private static final int TYPEFACE_SERIF = 2;
  
  private Typeface font;
  
  public final String fontFamily;
  
  private final int fontFamilyResourceId;
  
  private boolean fontResolved = false;
  
  public final boolean hasLetterSpacing;
  
  public final float letterSpacing;
  
  public final ColorStateList shadowColor;
  
  public final float shadowDx;
  
  public final float shadowDy;
  
  public final float shadowRadius;
  
  public final boolean textAllCaps;
  
  public final ColorStateList textColor;
  
  public final ColorStateList textColorHint;
  
  public final ColorStateList textColorLink;
  
  public float textSize;
  
  public final int textStyle;
  
  public final int typeface;
  
  public TextAppearance(Context paramContext, int paramInt) {
    TypedArray typedArray = paramContext.obtainStyledAttributes(paramInt, R.styleable.TextAppearance);
    this.textSize = typedArray.getDimension(R.styleable.TextAppearance_android_textSize, 0.0F);
    this.textColor = MaterialResources.getColorStateList(paramContext, typedArray, R.styleable.TextAppearance_android_textColor);
    this.textColorHint = MaterialResources.getColorStateList(paramContext, typedArray, R.styleable.TextAppearance_android_textColorHint);
    this.textColorLink = MaterialResources.getColorStateList(paramContext, typedArray, R.styleable.TextAppearance_android_textColorLink);
    this.textStyle = typedArray.getInt(R.styleable.TextAppearance_android_textStyle, 0);
    this.typeface = typedArray.getInt(R.styleable.TextAppearance_android_typeface, 1);
    int i = MaterialResources.getIndexWithValue(typedArray, R.styleable.TextAppearance_fontFamily, R.styleable.TextAppearance_android_fontFamily);
    this.fontFamilyResourceId = typedArray.getResourceId(i, 0);
    this.fontFamily = typedArray.getString(i);
    this.textAllCaps = typedArray.getBoolean(R.styleable.TextAppearance_textAllCaps, false);
    this.shadowColor = MaterialResources.getColorStateList(paramContext, typedArray, R.styleable.TextAppearance_android_shadowColor);
    this.shadowDx = typedArray.getFloat(R.styleable.TextAppearance_android_shadowDx, 0.0F);
    this.shadowDy = typedArray.getFloat(R.styleable.TextAppearance_android_shadowDy, 0.0F);
    this.shadowRadius = typedArray.getFloat(R.styleable.TextAppearance_android_shadowRadius, 0.0F);
    typedArray.recycle();
    if (Build.VERSION.SDK_INT >= 21) {
      TypedArray typedArray1 = paramContext.obtainStyledAttributes(paramInt, R.styleable.MaterialTextAppearance);
      this.hasLetterSpacing = typedArray1.hasValue(R.styleable.MaterialTextAppearance_android_letterSpacing);
      this.letterSpacing = typedArray1.getFloat(R.styleable.MaterialTextAppearance_android_letterSpacing, 0.0F);
      typedArray1.recycle();
    } else {
      this.hasLetterSpacing = false;
      this.letterSpacing = 0.0F;
    } 
  }
  
  private void createFallbackFont() {
    if (this.font == null) {
      String str = this.fontFamily;
      if (str != null)
        this.font = Typeface.create(str, this.textStyle); 
    } 
    if (this.font == null) {
      switch (this.typeface) {
        default:
          this.font = Typeface.DEFAULT;
          break;
        case 3:
          this.font = Typeface.MONOSPACE;
          break;
        case 2:
          this.font = Typeface.SERIF;
          break;
        case 1:
          this.font = Typeface.SANS_SERIF;
          break;
      } 
      this.font = Typeface.create(this.font, this.textStyle);
    } 
  }
  
  private boolean shouldLoadFontSynchronously(Context paramContext) {
    boolean bool1 = TextAppearanceConfig.shouldLoadFontSynchronously();
    boolean bool = true;
    if (bool1)
      return true; 
    int i = this.fontFamilyResourceId;
    if (i != 0) {
      Typeface typeface = ResourcesCompat.getCachedFont(paramContext, i);
    } else {
      paramContext = null;
    } 
    if (paramContext == null)
      bool = false; 
    return bool;
  }
  
  public Typeface getFallbackFont() {
    createFallbackFont();
    return this.font;
  }
  
  public Typeface getFont(Context paramContext) {
    if (this.fontResolved)
      return this.font; 
    if (!paramContext.isRestricted())
      try {
        Typeface typeface = ResourcesCompat.getFont(paramContext, this.fontFamilyResourceId);
        this.font = typeface;
        if (typeface != null)
          this.font = Typeface.create(typeface, this.textStyle); 
      } catch (UnsupportedOperationException|android.content.res.Resources.NotFoundException unsupportedOperationException) {
      
      } catch (Exception exception) {
        Log.d("TextAppearance", "Error loading font " + this.fontFamily, exception);
      }  
    createFallbackFont();
    this.fontResolved = true;
    return this.font;
  }
  
  public void getFontAsync(Context paramContext, final TextPaint textPaint, final TextAppearanceFontCallback callback) {
    updateTextPaintMeasureState(textPaint, getFallbackFont());
    getFontAsync(paramContext, new TextAppearanceFontCallback() {
          final TextAppearance this$0;
          
          final TextAppearanceFontCallback val$callback;
          
          final TextPaint val$textPaint;
          
          public void onFontRetrievalFailed(int param1Int) {
            callback.onFontRetrievalFailed(param1Int);
          }
          
          public void onFontRetrieved(Typeface param1Typeface, boolean param1Boolean) {
            TextAppearance.this.updateTextPaintMeasureState(textPaint, param1Typeface);
            callback.onFontRetrieved(param1Typeface, param1Boolean);
          }
        });
  }
  
  public void getFontAsync(Context paramContext, TextAppearanceFontCallback paramTextAppearanceFontCallback) {
    if (shouldLoadFontSynchronously(paramContext)) {
      getFont(paramContext);
    } else {
      createFallbackFont();
    } 
    int i = this.fontFamilyResourceId;
    if (i == 0)
      this.fontResolved = true; 
    if (this.fontResolved) {
      paramTextAppearanceFontCallback.onFontRetrieved(this.font, true);
      return;
    } 
    try {
      ResourcesCompat.FontCallback fontCallback = new ResourcesCompat.FontCallback() {
          final TextAppearance this$0;
          
          final TextAppearanceFontCallback val$callback;
          
          public void onFontRetrievalFailed(int param1Int) {
            TextAppearance.access$102(TextAppearance.this, true);
            callback.onFontRetrievalFailed(param1Int);
          }
          
          public void onFontRetrieved(Typeface param1Typeface) {
            TextAppearance textAppearance = TextAppearance.this;
            TextAppearance.access$002(textAppearance, Typeface.create(param1Typeface, textAppearance.textStyle));
            TextAppearance.access$102(TextAppearance.this, true);
            callback.onFontRetrieved(TextAppearance.this.font, false);
          }
        };
      super(this, paramTextAppearanceFontCallback);
      ResourcesCompat.getFont(paramContext, i, fontCallback, null);
    } catch (android.content.res.Resources.NotFoundException notFoundException) {
      this.fontResolved = true;
      paramTextAppearanceFontCallback.onFontRetrievalFailed(1);
    } catch (Exception exception) {
      Log.d("TextAppearance", "Error loading font " + this.fontFamily, exception);
      this.fontResolved = true;
      paramTextAppearanceFontCallback.onFontRetrievalFailed(-3);
    } 
  }
  
  public void updateDrawState(Context paramContext, TextPaint paramTextPaint, TextAppearanceFontCallback paramTextAppearanceFontCallback) {
    int i;
    updateMeasureState(paramContext, paramTextPaint, paramTextAppearanceFontCallback);
    ColorStateList colorStateList = this.textColor;
    if (colorStateList != null) {
      i = colorStateList.getColorForState(paramTextPaint.drawableState, this.textColor.getDefaultColor());
    } else {
      i = -16777216;
    } 
    paramTextPaint.setColor(i);
    float f3 = this.shadowRadius;
    float f1 = this.shadowDx;
    float f2 = this.shadowDy;
    colorStateList = this.shadowColor;
    if (colorStateList != null) {
      i = colorStateList.getColorForState(paramTextPaint.drawableState, this.shadowColor.getDefaultColor());
    } else {
      i = 0;
    } 
    paramTextPaint.setShadowLayer(f3, f1, f2, i);
  }
  
  public void updateMeasureState(Context paramContext, TextPaint paramTextPaint, TextAppearanceFontCallback paramTextAppearanceFontCallback) {
    if (shouldLoadFontSynchronously(paramContext)) {
      updateTextPaintMeasureState(paramTextPaint, getFont(paramContext));
    } else {
      getFontAsync(paramContext, paramTextPaint, paramTextAppearanceFontCallback);
    } 
  }
  
  public void updateTextPaintMeasureState(TextPaint paramTextPaint, Typeface paramTypeface) {
    float f;
    boolean bool;
    paramTextPaint.setTypeface(paramTypeface);
    int i = this.textStyle & (paramTypeface.getStyle() ^ 0xFFFFFFFF);
    if ((i & 0x1) != 0) {
      bool = true;
    } else {
      bool = false;
    } 
    paramTextPaint.setFakeBoldText(bool);
    if ((i & 0x2) != 0) {
      f = -0.25F;
    } else {
      f = 0.0F;
    } 
    paramTextPaint.setTextSkewX(f);
    paramTextPaint.setTextSize(this.textSize);
    if (Build.VERSION.SDK_INT >= 21 && this.hasLetterSpacing)
      paramTextPaint.setLetterSpacing(this.letterSpacing); 
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\resources\TextAppearance.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */