package androidx.core.view;

import android.graphics.Rect;
import android.os.Build;
import android.view.DisplayCutout;
import androidx.core.graphics.Insets;
import androidx.core.os.BuildCompat;
import androidx.core.util.ObjectsCompat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class DisplayCutoutCompat {
  private final Object mDisplayCutout;
  
  public DisplayCutoutCompat(Rect paramRect, List<Rect> paramList) {
    this(paramRect);
  }
  
  public DisplayCutoutCompat(Insets paramInsets1, Rect paramRect1, Rect paramRect2, Rect paramRect3, Rect paramRect4, Insets paramInsets2) {
    this(constructDisplayCutout(paramInsets1, paramRect1, paramRect2, paramRect3, paramRect4, paramInsets2));
  }
  
  private DisplayCutoutCompat(Object paramObject) {
    this.mDisplayCutout = paramObject;
  }
  
  private static DisplayCutout constructDisplayCutout(Insets paramInsets1, Rect paramRect1, Rect paramRect2, Rect paramRect3, Rect paramRect4, Insets paramInsets2) {
    if (BuildCompat.isAtLeastR())
      return new DisplayCutout(paramInsets1.toPlatformInsets(), paramRect1, paramRect2, paramRect3, paramRect4, paramInsets2.toPlatformInsets()); 
    if (Build.VERSION.SDK_INT >= 29)
      return new DisplayCutout(paramInsets1.toPlatformInsets(), paramRect1, paramRect2, paramRect3, paramRect4); 
    if (Build.VERSION.SDK_INT >= 28) {
      Rect rect = new Rect(paramInsets1.left, paramInsets1.top, paramInsets1.right, paramInsets1.bottom);
      ArrayList<Rect> arrayList = new ArrayList();
      if (paramRect1 != null)
        arrayList.add(paramRect1); 
      if (paramRect2 != null)
        arrayList.add(paramRect2); 
      if (paramRect3 != null)
        arrayList.add(paramRect3); 
      if (paramRect4 != null)
        arrayList.add(paramRect4); 
      return new DisplayCutout(rect, arrayList);
    } 
    return null;
  }
  
  static DisplayCutoutCompat wrap(Object paramObject) {
    if (paramObject == null) {
      paramObject = null;
    } else {
      paramObject = new DisplayCutoutCompat(paramObject);
    } 
    return (DisplayCutoutCompat)paramObject;
  }
  
  public boolean equals(Object paramObject) {
    if (this == paramObject)
      return true; 
    if (paramObject == null || getClass() != paramObject.getClass())
      return false; 
    paramObject = paramObject;
    return ObjectsCompat.equals(this.mDisplayCutout, ((DisplayCutoutCompat)paramObject).mDisplayCutout);
  }
  
  public List<Rect> getBoundingRects() {
    return (Build.VERSION.SDK_INT >= 28) ? ((DisplayCutout)this.mDisplayCutout).getBoundingRects() : Collections.emptyList();
  }
  
  public int getSafeInsetBottom() {
    return (Build.VERSION.SDK_INT >= 28) ? ((DisplayCutout)this.mDisplayCutout).getSafeInsetBottom() : 0;
  }
  
  public int getSafeInsetLeft() {
    return (Build.VERSION.SDK_INT >= 28) ? ((DisplayCutout)this.mDisplayCutout).getSafeInsetLeft() : 0;
  }
  
  public int getSafeInsetRight() {
    return (Build.VERSION.SDK_INT >= 28) ? ((DisplayCutout)this.mDisplayCutout).getSafeInsetRight() : 0;
  }
  
  public int getSafeInsetTop() {
    return (Build.VERSION.SDK_INT >= 28) ? ((DisplayCutout)this.mDisplayCutout).getSafeInsetTop() : 0;
  }
  
  public Insets getWaterfallInsets() {
    return BuildCompat.isAtLeastR() ? Insets.toCompatInsets(((DisplayCutout)this.mDisplayCutout).getWaterfallInsets()) : Insets.NONE;
  }
  
  public int hashCode() {
    int i;
    Object object = this.mDisplayCutout;
    if (object == null) {
      i = 0;
    } else {
      i = object.hashCode();
    } 
    return i;
  }
  
  public String toString() {
    return "DisplayCutoutCompat{" + this.mDisplayCutout + "}";
  }
  
  DisplayCutout unwrap() {
    return (DisplayCutout)this.mDisplayCutout;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\view\DisplayCutoutCompat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */