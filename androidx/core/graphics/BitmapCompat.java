package androidx.core.graphics;

import android.graphics.Bitmap;
import android.os.Build;

public final class BitmapCompat {
  public static int getAllocationByteCount(Bitmap paramBitmap) {
    return (Build.VERSION.SDK_INT >= 19) ? paramBitmap.getAllocationByteCount() : paramBitmap.getByteCount();
  }
  
  public static boolean hasMipMap(Bitmap paramBitmap) {
    return (Build.VERSION.SDK_INT >= 18) ? paramBitmap.hasMipMap() : false;
  }
  
  public static void setHasMipMap(Bitmap paramBitmap, boolean paramBoolean) {
    if (Build.VERSION.SDK_INT >= 18)
      paramBitmap.setHasMipMap(paramBoolean); 
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\graphics\BitmapCompat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */