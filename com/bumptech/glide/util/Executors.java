package com.bumptech.glide.util;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public final class Executors {
  private static final Executor DIRECT_EXECUTOR;
  
  private static final Executor MAIN_THREAD_EXECUTOR = new Executor() {
      public void execute(Runnable param1Runnable) {
        Util.postOnUiThread(param1Runnable);
      }
    };
  
  static {
    DIRECT_EXECUTOR = new Executor() {
        public void execute(Runnable param1Runnable) {
          param1Runnable.run();
        }
      };
  }
  
  public static Executor directExecutor() {
    return DIRECT_EXECUTOR;
  }
  
  public static Executor mainThreadExecutor() {
    return MAIN_THREAD_EXECUTOR;
  }
  
  public static void shutdownAndAwaitTermination(ExecutorService paramExecutorService) {
    paramExecutorService.shutdownNow();
    try {
      if (!paramExecutorService.awaitTermination(5L, TimeUnit.SECONDS)) {
        paramExecutorService.shutdownNow();
        if (!paramExecutorService.awaitTermination(5L, TimeUnit.SECONDS)) {
          RuntimeException runtimeException = new RuntimeException();
          this("Failed to shutdown");
          throw runtimeException;
        } 
      } 
      return;
    } catch (InterruptedException interruptedException) {
      paramExecutorService.shutdownNow();
      Thread.currentThread().interrupt();
      throw new RuntimeException(interruptedException);
    } 
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glid\\util\Executors.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */