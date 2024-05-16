package androidx.core.widget;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;

public interface TintableCompoundButton {
  ColorStateList getSupportButtonTintList();
  
  PorterDuff.Mode getSupportButtonTintMode();
  
  void setSupportButtonTintList(ColorStateList paramColorStateList);
  
  void setSupportButtonTintMode(PorterDuff.Mode paramMode);
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\widget\TintableCompoundButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */