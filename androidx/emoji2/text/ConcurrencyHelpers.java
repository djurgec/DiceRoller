package androidx.emoji2.text;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

class ConcurrencyHelpers {
  private static final int FONT_LOAD_TIMEOUT_SECONDS = 15;
  
  @Deprecated
  static Executor convertHandlerToExecutor(Handler paramHandler) {
    Objects.requireNonNull(paramHandler);
    return new ConcurrencyHelpers$$ExternalSyntheticLambda0(paramHandler);
  }
  
  static ThreadPoolExecutor createBackgroundPriorityExecutor(String paramString) {
    ConcurrencyHelpers$$ExternalSyntheticLambda1 concurrencyHelpers$$ExternalSyntheticLambda1 = new ConcurrencyHelpers$$ExternalSyntheticLambda1(paramString);
    ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(0, 1, 15L, TimeUnit.SECONDS, new LinkedBlockingDeque<>(), concurrencyHelpers$$ExternalSyntheticLambda1);
    threadPoolExecutor.allowCoreThreadTimeOut(true);
    return threadPoolExecutor;
  }
  
  static Handler mainHandlerAsync() {
    return (Build.VERSION.SDK_INT >= 28) ? Handler28Impl.createAsync(Looper.getMainLooper()) : new Handler(Looper.getMainLooper());
  }
  
  static class Handler28Impl {
    public static Handler createAsync(Looper param1Looper) {
      return Handler.createAsync(param1Looper);
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\emoji2\text\ConcurrencyHelpers.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */