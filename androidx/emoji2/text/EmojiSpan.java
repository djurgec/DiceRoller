package androidx.emoji2.text;

import android.graphics.Paint;
import android.text.style.ReplacementSpan;
import androidx.core.util.Preconditions;

public abstract class EmojiSpan extends ReplacementSpan {
  private short mHeight = -1;
  
  private final EmojiMetadata mMetadata;
  
  private float mRatio = 1.0F;
  
  private final Paint.FontMetricsInt mTmpFontMetrics = new Paint.FontMetricsInt();
  
  private short mWidth = -1;
  
  EmojiSpan(EmojiMetadata paramEmojiMetadata) {
    Preconditions.checkNotNull(paramEmojiMetadata, "metadata cannot be null");
    this.mMetadata = paramEmojiMetadata;
  }
  
  public final int getHeight() {
    return this.mHeight;
  }
  
  public final int getId() {
    return getMetadata().getId();
  }
  
  public final EmojiMetadata getMetadata() {
    return this.mMetadata;
  }
  
  final float getRatio() {
    return this.mRatio;
  }
  
  public int getSize(Paint paramPaint, CharSequence paramCharSequence, int paramInt1, int paramInt2, Paint.FontMetricsInt paramFontMetricsInt) {
    paramPaint.getFontMetricsInt(this.mTmpFontMetrics);
    this.mRatio = Math.abs(this.mTmpFontMetrics.descent - this.mTmpFontMetrics.ascent) * 1.0F / this.mMetadata.getHeight();
    this.mHeight = (short)(int)(this.mMetadata.getHeight() * this.mRatio);
    this.mWidth = (short)(int)(this.mMetadata.getWidth() * this.mRatio);
    if (paramFontMetricsInt != null) {
      paramFontMetricsInt.ascent = this.mTmpFontMetrics.ascent;
      paramFontMetricsInt.descent = this.mTmpFontMetrics.descent;
      paramFontMetricsInt.top = this.mTmpFontMetrics.top;
      paramFontMetricsInt.bottom = this.mTmpFontMetrics.bottom;
    } 
    return this.mWidth;
  }
  
  final int getWidth() {
    return this.mWidth;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\emoji2\text\EmojiSpan.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */