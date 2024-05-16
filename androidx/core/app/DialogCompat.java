package androidx.core.app;

import android.app.Dialog;
import android.os.Build;
import android.view.View;

public class DialogCompat {
  public static View requireViewById(Dialog paramDialog, int paramInt) {
    if (Build.VERSION.SDK_INT >= 28)
      return paramDialog.requireViewById(paramInt); 
    View view = paramDialog.findViewById(paramInt);
    if (view != null)
      return view; 
    throw new IllegalArgumentException("ID does not reference a View inside this Dialog");
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\app\DialogCompat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */