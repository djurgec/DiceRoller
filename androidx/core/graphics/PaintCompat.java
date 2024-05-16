package androidx.core.graphics;

import android.graphics.BlendMode;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Xfermode;
import android.os.Build;
import androidx.core.util.Pair;

public final class PaintCompat {
  private static final String EM_STRING = "m";
  
  private static final String TOFU_STRING = "󟿽";
  
  private static final ThreadLocal<Pair<Rect, Rect>> sRectThreadLocal = new ThreadLocal<>();
  
  public static boolean hasGlyph(Paint paramPaint, String paramString) {
    if (Build.VERSION.SDK_INT >= 23)
      return paramPaint.hasGlyph(paramString); 
    int i = paramString.length();
    if (i == 1 && Character.isWhitespace(paramString.charAt(0)))
      return true; 
    float f2 = paramPaint.measureText("󟿽");
    float f1 = paramPaint.measureText("m");
    float f3 = paramPaint.measureText(paramString);
    if (f3 == 0.0F)
      return false; 
    if (paramString.codePointCount(0, paramString.length()) > 1) {
      if (f3 > 2.0F * f1)
        return false; 
      f1 = 0.0F;
      int j;
      for (j = 0; j < i; j += k) {
        int k = Character.charCount(paramString.codePointAt(j));
        f1 += paramPaint.measureText(paramString, j, j + k);
      } 
      if (f3 >= f1)
        return false; 
    } 
    if (f3 != f2)
      return true; 
    Pair<Rect, Rect> pair = obtainEmptyRects();
    paramPaint.getTextBounds("󟿽", 0, "󟿽".length(), (Rect)pair.first);
    paramPaint.getTextBounds(paramString, 0, i, (Rect)pair.second);
    return true ^ ((Rect)pair.first).equals(pair.second);
  }
  
  private static Pair<Rect, Rect> obtainEmptyRects() {
    ThreadLocal<Pair<Rect, Rect>> threadLocal = sRectThreadLocal;
    Pair<Rect, Rect> pair = threadLocal.get();
    if (pair == null) {
      pair = new Pair(new Rect(), new Rect());
      threadLocal.set(pair);
    } else {
      ((Rect)pair.first).setEmpty();
      ((Rect)pair.second).setEmpty();
    } 
    return pair;
  }
  
  public static boolean setBlendMode(Paint paramPaint, BlendModeCompat paramBlendModeCompat) {
    int i = Build.VERSION.SDK_INT;
    boolean bool = true;
    BlendMode blendMode1 = null;
    BlendMode blendMode2 = null;
    if (i >= 29) {
      blendMode1 = blendMode2;
      if (paramBlendModeCompat != null)
        blendMode1 = BlendModeUtils.obtainBlendModeFromCompat(paramBlendModeCompat); 
      paramPaint.setBlendMode(blendMode1);
      return true;
    } 
    if (paramBlendModeCompat != null) {
      PorterDuffXfermode porterDuffXfermode;
      PorterDuff.Mode mode = BlendModeUtils.obtainPorterDuffFromCompat(paramBlendModeCompat);
      BlendMode blendMode = blendMode1;
      if (mode != null)
        porterDuffXfermode = new PorterDuffXfermode(mode); 
      paramPaint.setXfermode((Xfermode)porterDuffXfermode);
      if (mode == null)
        bool = false; 
      return bool;
    } 
    paramPaint.setXfermode(null);
    return true;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\graphics\PaintCompat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */