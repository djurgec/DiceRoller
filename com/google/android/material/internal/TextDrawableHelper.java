package com.google.android.material.internal;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextPaint;
import com.google.android.material.resources.TextAppearance;
import com.google.android.material.resources.TextAppearanceFontCallback;
import java.lang.ref.WeakReference;

public class TextDrawableHelper {
  private WeakReference<TextDrawableDelegate> delegate = new WeakReference<>(null);
  
  private final TextAppearanceFontCallback fontCallback = new TextAppearanceFontCallback() {
      final TextDrawableHelper this$0;
      
      public void onFontRetrievalFailed(int param1Int) {
        TextDrawableHelper.access$002(TextDrawableHelper.this, true);
        TextDrawableHelper.TextDrawableDelegate textDrawableDelegate = TextDrawableHelper.this.delegate.get();
        if (textDrawableDelegate != null)
          textDrawableDelegate.onTextSizeChange(); 
      }
      
      public void onFontRetrieved(Typeface param1Typeface, boolean param1Boolean) {
        if (param1Boolean)
          return; 
        TextDrawableHelper.access$002(TextDrawableHelper.this, true);
        TextDrawableHelper.TextDrawableDelegate textDrawableDelegate = TextDrawableHelper.this.delegate.get();
        if (textDrawableDelegate != null)
          textDrawableDelegate.onTextSizeChange(); 
      }
    };
  
  private TextAppearance textAppearance;
  
  private final TextPaint textPaint = new TextPaint(1);
  
  private float textWidth;
  
  private boolean textWidthDirty = true;
  
  public TextDrawableHelper(TextDrawableDelegate paramTextDrawableDelegate) {
    setDelegate(paramTextDrawableDelegate);
  }
  
  private float calculateTextWidth(CharSequence paramCharSequence) {
    return (paramCharSequence == null) ? 0.0F : this.textPaint.measureText(paramCharSequence, 0, paramCharSequence.length());
  }
  
  public TextAppearance getTextAppearance() {
    return this.textAppearance;
  }
  
  public TextPaint getTextPaint() {
    return this.textPaint;
  }
  
  public float getTextWidth(String paramString) {
    if (!this.textWidthDirty)
      return this.textWidth; 
    float f = calculateTextWidth(paramString);
    this.textWidth = f;
    this.textWidthDirty = false;
    return f;
  }
  
  public boolean isTextWidthDirty() {
    return this.textWidthDirty;
  }
  
  public void setDelegate(TextDrawableDelegate paramTextDrawableDelegate) {
    this.delegate = new WeakReference<>(paramTextDrawableDelegate);
  }
  
  public void setTextAppearance(TextAppearance paramTextAppearance, Context paramContext) {
    if (this.textAppearance != paramTextAppearance) {
      this.textAppearance = paramTextAppearance;
      if (paramTextAppearance != null) {
        paramTextAppearance.updateMeasureState(paramContext, this.textPaint, this.fontCallback);
        TextDrawableDelegate textDrawableDelegate1 = this.delegate.get();
        if (textDrawableDelegate1 != null)
          this.textPaint.drawableState = textDrawableDelegate1.getState(); 
        paramTextAppearance.updateDrawState(paramContext, this.textPaint, this.fontCallback);
        this.textWidthDirty = true;
      } 
      TextDrawableDelegate textDrawableDelegate = this.delegate.get();
      if (textDrawableDelegate != null) {
        textDrawableDelegate.onTextSizeChange();
        textDrawableDelegate.onStateChange(textDrawableDelegate.getState());
      } 
    } 
  }
  
  public void setTextWidthDirty(boolean paramBoolean) {
    this.textWidthDirty = paramBoolean;
  }
  
  public void updateTextPaintDrawState(Context paramContext) {
    this.textAppearance.updateDrawState(paramContext, this.textPaint, this.fontCallback);
  }
  
  public static interface TextDrawableDelegate {
    int[] getState();
    
    boolean onStateChange(int[] param1ArrayOfint);
    
    void onTextSizeChange();
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\internal\TextDrawableHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */