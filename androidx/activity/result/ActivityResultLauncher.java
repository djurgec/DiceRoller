package androidx.activity.result;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.core.app.ActivityOptionsCompat;

public abstract class ActivityResultLauncher<I> {
  public abstract ActivityResultContract<I, ?> getContract();
  
  public void launch(I paramI) {
    launch(paramI, null);
  }
  
  public abstract void launch(I paramI, ActivityOptionsCompat paramActivityOptionsCompat);
  
  public abstract void unregister();
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\activity\result\ActivityResultLauncher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */