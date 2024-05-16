package androidx.core.widget;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;

public interface TintableCheckedTextView {
  ColorStateList getSupportCheckMarkTintList();
  
  PorterDuff.Mode getSupportCheckMarkTintMode();
  
  void setSupportCheckMarkTintList(ColorStateList paramColorStateList);
  
  void setSupportCheckMarkTintMode(PorterDuff.Mode paramMode);
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\widget\TintableCheckedTextView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */