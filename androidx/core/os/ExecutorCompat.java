package androidx.core.os;

import android.os.Handler;
import androidx.core.util.Preconditions;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;

public final class ExecutorCompat {
  public static Executor create(Handler paramHandler) {
    return new HandlerExecutor(paramHandler);
  }
  
  private static class HandlerExecutor implements Executor {
    private final Handler mHandler;
    
    HandlerExecutor(Handler param1Handler) {
      this.mHandler = (Handler)Preconditions.checkNotNull(param1Handler);
    }
    
    public void execute(Runnable param1Runnable) {
      if (this.mHandler.post((Runnable)Preconditions.checkNotNull(param1Runnable)))
        return; 
      throw new RejectedExecutionException(this.mHandler + " is shutting down");
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\os\ExecutorCompat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */