package com.google.android.material.internal;

import android.os.Build;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextDirectionHeuristic;
import android.text.TextDirectionHeuristics;
import android.text.TextPaint;
import android.text.TextUtils;
import androidx.core.util.Preconditions;
import java.lang.reflect.Constructor;

final class StaticLayoutBuilderCompat {
  static final int DEFAULT_HYPHENATION_FREQUENCY;
  
  static final float DEFAULT_LINE_SPACING_ADD = 0.0F;
  
  static final float DEFAULT_LINE_SPACING_MULTIPLIER = 1.0F;
  
  private static final String TEXT_DIRS_CLASS = "android.text.TextDirectionHeuristics";
  
  private static final String TEXT_DIR_CLASS = "android.text.TextDirectionHeuristic";
  
  private static final String TEXT_DIR_CLASS_LTR = "LTR";
  
  private static final String TEXT_DIR_CLASS_RTL = "RTL";
  
  private static Constructor<StaticLayout> constructor;
  
  private static boolean initialized;
  
  private static Object textDirection;
  
  private Layout.Alignment alignment;
  
  private TextUtils.TruncateAt ellipsize;
  
  private int end;
  
  private int hyphenationFrequency;
  
  private boolean includePad;
  
  private boolean isRtl;
  
  private float lineSpacingAdd;
  
  private float lineSpacingMultiplier;
  
  private int maxLines;
  
  private final TextPaint paint;
  
  private CharSequence source;
  
  private int start;
  
  private final int width;
  
  static {
    boolean bool;
    if (Build.VERSION.SDK_INT >= 23) {
      bool = true;
    } else {
      bool = false;
    } 
    DEFAULT_HYPHENATION_FREQUENCY = bool;
  }
  
  private StaticLayoutBuilderCompat(CharSequence paramCharSequence, TextPaint paramTextPaint, int paramInt) {
    this.source = paramCharSequence;
    this.paint = paramTextPaint;
    this.width = paramInt;
    this.start = 0;
    this.end = paramCharSequence.length();
    this.alignment = Layout.Alignment.ALIGN_NORMAL;
    this.maxLines = Integer.MAX_VALUE;
    this.lineSpacingAdd = 0.0F;
    this.lineSpacingMultiplier = 1.0F;
    this.hyphenationFrequency = DEFAULT_HYPHENATION_FREQUENCY;
    this.includePad = true;
    this.ellipsize = null;
  }
  
  private void createConstructorWithReflection() throws StaticLayoutBuilderCompatException {
    if (initialized)
      return; 
    try {
      boolean bool;
      Class<?> clazz;
      if (this.isRtl && Build.VERSION.SDK_INT >= 23) {
        bool = true;
      } else {
        bool = false;
      } 
      if (Build.VERSION.SDK_INT >= 18) {
        TextDirectionHeuristic textDirectionHeuristic;
        Class<TextDirectionHeuristic> clazz1 = TextDirectionHeuristic.class;
        if (bool) {
          textDirectionHeuristic = TextDirectionHeuristics.RTL;
        } else {
          textDirectionHeuristic = TextDirectionHeuristics.LTR;
        } 
        textDirection = textDirectionHeuristic;
        clazz = clazz1;
      } else {
        String str;
        ClassLoader classLoader = StaticLayoutBuilderCompat.class.getClassLoader();
        if (this.isRtl) {
          str = "RTL";
        } else {
          str = "LTR";
        } 
        Class<?> clazz1 = classLoader.loadClass("android.text.TextDirectionHeuristic");
        Class<?> clazz2 = classLoader.loadClass("android.text.TextDirectionHeuristics");
        textDirection = clazz2.getField(str).get(clazz2);
        clazz = clazz1;
      } 
      Constructor<StaticLayout> constructor = StaticLayout.class.getDeclaredConstructor(new Class[] { 
            CharSequence.class, int.class, int.class, TextPaint.class, int.class, Layout.Alignment.class, clazz, float.class, float.class, boolean.class, 
            TextUtils.TruncateAt.class, int.class, int.class });
      constructor = constructor;
      constructor.setAccessible(true);
      initialized = true;
      return;
    } catch (Exception exception) {
      throw new StaticLayoutBuilderCompatException(exception);
    } 
  }
  
  public static StaticLayoutBuilderCompat obtain(CharSequence paramCharSequence, TextPaint paramTextPaint, int paramInt) {
    return new StaticLayoutBuilderCompat(paramCharSequence, paramTextPaint, paramInt);
  }
  
  public StaticLayout build() throws StaticLayoutBuilderCompatException {
    TextUtils.TruncateAt truncateAt;
    if (this.source == null)
      this.source = ""; 
    int i = Math.max(0, this.width);
    CharSequence charSequence = this.source;
    if (this.maxLines == 1)
      charSequence = TextUtils.ellipsize(this.source, this.paint, i, this.ellipsize); 
    this.end = Math.min(charSequence.length(), this.end);
    if (Build.VERSION.SDK_INT >= 23) {
      TextDirectionHeuristic textDirectionHeuristic;
      if (this.isRtl && this.maxLines == 1)
        this.alignment = Layout.Alignment.ALIGN_OPPOSITE; 
      StaticLayout.Builder builder = StaticLayout.Builder.obtain(charSequence, this.start, this.end, this.paint, i);
      builder.setAlignment(this.alignment);
      builder.setIncludePad(this.includePad);
      if (this.isRtl) {
        textDirectionHeuristic = TextDirectionHeuristics.RTL;
      } else {
        textDirectionHeuristic = TextDirectionHeuristics.LTR;
      } 
      builder.setTextDirection(textDirectionHeuristic);
      truncateAt = this.ellipsize;
      if (truncateAt != null)
        builder.setEllipsize(truncateAt); 
      builder.setMaxLines(this.maxLines);
      float f = this.lineSpacingAdd;
      if (f != 0.0F || this.lineSpacingMultiplier != 1.0F)
        builder.setLineSpacing(f, this.lineSpacingMultiplier); 
      if (this.maxLines > 1)
        builder.setHyphenationFrequency(this.hyphenationFrequency); 
      return builder.build();
    } 
    createConstructorWithReflection();
    try {
      return ((Constructor<StaticLayout>)Preconditions.checkNotNull(constructor)).newInstance(new Object[] { 
            truncateAt, Integer.valueOf(this.start), Integer.valueOf(this.end), this.paint, Integer.valueOf(i), this.alignment, Preconditions.checkNotNull(textDirection), Float.valueOf(1.0F), Float.valueOf(0.0F), Boolean.valueOf(this.includePad), 
            null, Integer.valueOf(i), Integer.valueOf(this.maxLines) });
    } catch (Exception exception) {
      throw new StaticLayoutBuilderCompatException(exception);
    } 
  }
  
  public StaticLayoutBuilderCompat setAlignment(Layout.Alignment paramAlignment) {
    this.alignment = paramAlignment;
    return this;
  }
  
  public StaticLayoutBuilderCompat setEllipsize(TextUtils.TruncateAt paramTruncateAt) {
    this.ellipsize = paramTruncateAt;
    return this;
  }
  
  public StaticLayoutBuilderCompat setEnd(int paramInt) {
    this.end = paramInt;
    return this;
  }
  
  public StaticLayoutBuilderCompat setHyphenationFrequency(int paramInt) {
    this.hyphenationFrequency = paramInt;
    return this;
  }
  
  public StaticLayoutBuilderCompat setIncludePad(boolean paramBoolean) {
    this.includePad = paramBoolean;
    return this;
  }
  
  public StaticLayoutBuilderCompat setIsRtl(boolean paramBoolean) {
    this.isRtl = paramBoolean;
    return this;
  }
  
  public StaticLayoutBuilderCompat setLineSpacing(float paramFloat1, float paramFloat2) {
    this.lineSpacingAdd = paramFloat1;
    this.lineSpacingMultiplier = paramFloat2;
    return this;
  }
  
  public StaticLayoutBuilderCompat setMaxLines(int paramInt) {
    this.maxLines = paramInt;
    return this;
  }
  
  public StaticLayoutBuilderCompat setStart(int paramInt) {
    this.start = paramInt;
    return this;
  }
  
  static class StaticLayoutBuilderCompatException extends Exception {
    StaticLayoutBuilderCompatException(Throwable param1Throwable) {
      super("Error thrown initializing StaticLayout " + param1Throwable.getMessage(), param1Throwable);
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\internal\StaticLayoutBuilderCompat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */