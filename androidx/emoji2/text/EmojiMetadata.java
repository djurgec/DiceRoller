package androidx.emoji2.text;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import androidx.emoji2.text.flatbuffer.MetadataItem;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class EmojiMetadata {
  public static final int HAS_GLYPH_ABSENT = 1;
  
  public static final int HAS_GLYPH_EXISTS = 2;
  
  public static final int HAS_GLYPH_UNKNOWN = 0;
  
  private static final ThreadLocal<MetadataItem> sMetadataItem = new ThreadLocal<>();
  
  private volatile int mHasGlyph = 0;
  
  private final int mIndex;
  
  private final MetadataRepo mMetadataRepo;
  
  EmojiMetadata(MetadataRepo paramMetadataRepo, int paramInt) {
    this.mMetadataRepo = paramMetadataRepo;
    this.mIndex = paramInt;
  }
  
  private MetadataItem getMetadataItem() {
    ThreadLocal<MetadataItem> threadLocal = sMetadataItem;
    MetadataItem metadataItem2 = threadLocal.get();
    MetadataItem metadataItem1 = metadataItem2;
    if (metadataItem2 == null) {
      metadataItem1 = new MetadataItem();
      threadLocal.set(metadataItem1);
    } 
    this.mMetadataRepo.getMetadataList().list(metadataItem1, this.mIndex);
    return metadataItem1;
  }
  
  public void draw(Canvas paramCanvas, float paramFloat1, float paramFloat2, Paint paramPaint) {
    Typeface typeface1 = this.mMetadataRepo.getTypeface();
    Typeface typeface2 = paramPaint.getTypeface();
    paramPaint.setTypeface(typeface1);
    int i = this.mIndex;
    paramCanvas.drawText(this.mMetadataRepo.getEmojiCharArray(), i * 2, 2, paramFloat1, paramFloat2, paramPaint);
    paramPaint.setTypeface(typeface2);
  }
  
  public int getCodepointAt(int paramInt) {
    return getMetadataItem().codepoints(paramInt);
  }
  
  public int getCodepointsLength() {
    return getMetadataItem().codepointsLength();
  }
  
  public short getCompatAdded() {
    return getMetadataItem().compatAdded();
  }
  
  public int getHasGlyph() {
    return this.mHasGlyph;
  }
  
  public short getHeight() {
    return getMetadataItem().height();
  }
  
  public int getId() {
    return getMetadataItem().id();
  }
  
  public short getSdkAdded() {
    return getMetadataItem().sdkAdded();
  }
  
  public Typeface getTypeface() {
    return this.mMetadataRepo.getTypeface();
  }
  
  public short getWidth() {
    return getMetadataItem().width();
  }
  
  public boolean isDefaultEmoji() {
    return getMetadataItem().emojiStyle();
  }
  
  public void resetHasGlyphCache() {
    this.mHasGlyph = 0;
  }
  
  public void setHasGlyph(boolean paramBoolean) {
    boolean bool;
    if (paramBoolean) {
      bool = true;
    } else {
      bool = true;
    } 
    this.mHasGlyph = bool;
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(super.toString());
    stringBuilder.append(", id:");
    stringBuilder.append(Integer.toHexString(getId()));
    stringBuilder.append(", codepoints:");
    int i = getCodepointsLength();
    for (byte b = 0; b < i; b++) {
      stringBuilder.append(Integer.toHexString(getCodepointAt(b)));
      stringBuilder.append(" ");
    } 
    return stringBuilder.toString();
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface HasGlyph {}
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\emoji2\text\EmojiMetadata.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */