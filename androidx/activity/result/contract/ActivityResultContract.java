package androidx.activity.result.contract;

import android.content.Context;
import android.content.Intent;

public abstract class ActivityResultContract<I, O> {
  public abstract Intent createIntent(Context paramContext, I paramI);
  
  public SynchronousResult<O> getSynchronousResult(Context paramContext, I paramI) {
    return null;
  }
  
  public abstract O parseResult(int paramInt, Intent paramIntent);
  
  public static final class SynchronousResult<T> {
    private final T mValue;
    
    public SynchronousResult(T param1T) {
      this.mValue = param1T;
    }
    
    public T getValue() {
      return this.mValue;
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\activity\result\contract\ActivityResultContract.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */