package androidx.emoji2.text;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;

public final class TypefaceEmojiSpan extends EmojiSpan {
  private static Paint sDebugPaint;
  
  public TypefaceEmojiSpan(EmojiMetadata paramEmojiMetadata) {
    super(paramEmojiMetadata);
  }
  
  private static Paint getDebugPaint() {
    if (sDebugPaint == null) {
      TextPaint textPaint = new TextPaint();
      sDebugPaint = (Paint)textPaint;
      textPaint.setColor(EmojiCompat.get().getEmojiSpanIndicatorColor());
      sDebugPaint.setStyle(Paint.Style.FILL);
    } 
    return sDebugPaint;
  }
  
  public void draw(Canvas paramCanvas, CharSequence paramCharSequence, int paramInt1, int paramInt2, float paramFloat, int paramInt3, int paramInt4, int paramInt5, Paint paramPaint) {
    if (EmojiCompat.get().isEmojiSpanIndicatorEnabled())
      paramCanvas.drawRect(paramFloat, paramInt3, paramFloat + getWidth(), paramInt5, getDebugPaint()); 
    getMetadata().draw(paramCanvas, paramFloat, paramInt4, paramPaint);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\emoji2\text\TypefaceEmojiSpan.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */