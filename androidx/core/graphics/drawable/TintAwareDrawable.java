package androidx.core.graphics.drawable;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;

public interface TintAwareDrawable {
  void setTint(int paramInt);
  
  void setTintList(ColorStateList paramColorStateList);
  
  void setTintMode(PorterDuff.Mode paramMode);
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\graphics\drawable\TintAwareDrawable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */