package androidx.core.graphics;

import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Build;

public class BlendModeColorFilterCompat {
  public static ColorFilter createBlendModeColorFilterCompat(int paramInt, BlendModeCompat paramBlendModeCompat) {
    BlendMode blendMode1;
    int i = Build.VERSION.SDK_INT;
    PorterDuff.Mode mode2 = null;
    BlendMode blendMode2 = null;
    if (i >= 29) {
      blendMode1 = BlendModeUtils.obtainBlendModeFromCompat(paramBlendModeCompat);
      if (blendMode1 != null) {
        BlendModeColorFilter blendModeColorFilter = new BlendModeColorFilter(paramInt, blendMode1);
      } else {
        blendMode1 = blendMode2;
      } 
      return (ColorFilter)blendMode1;
    } 
    PorterDuff.Mode mode1 = BlendModeUtils.obtainPorterDuffFromCompat((BlendModeCompat)blendMode1);
    if (mode1 != null) {
      PorterDuffColorFilter porterDuffColorFilter = new PorterDuffColorFilter(paramInt, mode1);
    } else {
      mode1 = mode2;
    } 
    return (ColorFilter)mode1;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\graphics\BlendModeColorFilterCompat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */