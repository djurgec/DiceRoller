package androidx.core.graphics;

import android.graphics.Path;
import android.graphics.PointF;
import java.util.ArrayList;
import java.util.Collection;

public final class PathUtils {
  public static Collection<PathSegment> flatten(Path paramPath) {
    return flatten(paramPath, 0.5F);
  }
  
  public static Collection<PathSegment> flatten(Path paramPath, float paramFloat) {
    float[] arrayOfFloat = paramPath.approximate(paramFloat);
    int i = arrayOfFloat.length / 3;
    ArrayList<PathSegment> arrayList = new ArrayList(i);
    for (byte b = 1; b < i; b++) {
      int j = b * 3;
      int k = (b - 1) * 3;
      float f4 = arrayOfFloat[j];
      float f3 = arrayOfFloat[j + 1];
      float f1 = arrayOfFloat[j + 2];
      paramFloat = arrayOfFloat[k];
      float f2 = arrayOfFloat[k + 1];
      float f5 = arrayOfFloat[k + 2];
      if (f4 != paramFloat && (f3 != f2 || f1 != f5))
        arrayList.add(new PathSegment(new PointF(f2, f5), paramFloat, new PointF(f3, f1), f4)); 
    } 
    return arrayList;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\graphics\PathUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */