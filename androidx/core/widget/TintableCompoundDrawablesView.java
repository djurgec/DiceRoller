package androidx.core.widget;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;

public interface TintableCompoundDrawablesView {
  ColorStateList getSupportCompoundDrawablesTintList();
  
  PorterDuff.Mode getSupportCompoundDrawablesTintMode();
  
  void setSupportCompoundDrawablesTintList(ColorStateList paramColorStateList);
  
  void setSupportCompoundDrawablesTintMode(PorterDuff.Mode paramMode);
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\widget\TintableCompoundDrawablesView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */