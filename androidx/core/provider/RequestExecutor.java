package androidx.core.provider;

import android.os.Handler;
import android.os.Process;
import androidx.core.util.Consumer;
import androidx.core.util.Preconditions;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

class RequestExecutor {
  static ThreadPoolExecutor createDefaultExecutor(String paramString, int paramInt1, int paramInt2) {
    DefaultThreadFactory defaultThreadFactory = new DefaultThreadFactory(paramString, paramInt1);
    ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(0, 1, paramInt2, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>(), defaultThreadFactory);
    threadPoolExecutor.allowCoreThreadTimeOut(true);
    return threadPoolExecutor;
  }
  
  static Executor createHandlerExecutor(Handler paramHandler) {
    return new HandlerExecutor(paramHandler);
  }
  
  static <T> void execute(Executor paramExecutor, Callable<T> paramCallable, Consumer<T> paramConsumer) {
    paramExecutor.execute(new ReplyRunnable<>(CalleeHandler.create(), paramCallable, paramConsumer));
  }
  
  static <T> T submit(ExecutorService paramExecutorService, Callable<T> paramCallable, int paramInt) throws InterruptedException {
    Future<T> future = paramExecutorService.submit(paramCallable);
    long l = paramInt;
    try {
      return future.get(l, TimeUnit.MILLISECONDS);
    } catch (ExecutionException executionException) {
      throw new RuntimeException(executionException);
    } catch (InterruptedException interruptedException) {
      throw interruptedException;
    } catch (TimeoutException timeoutException) {
      throw new InterruptedException("timeout");
    } 
  }
  
  private static class DefaultThreadFactory implements ThreadFactory {
    private int mPriority;
    
    private String mThreadName;
    
    DefaultThreadFactory(String param1String, int param1Int) {
      this.mThreadName = param1String;
      this.mPriority = param1Int;
    }
    
    public Thread newThread(Runnable param1Runnable) {
      return new ProcessPriorityThread(param1Runnable, this.mThreadName, this.mPriority);
    }
    
    private static class ProcessPriorityThread extends Thread {
      private final int mPriority;
      
      ProcessPriorityThread(Runnable param2Runnable, String param2String, int param2Int) {
        super(param2Runnable, param2String);
        this.mPriority = param2Int;
      }
      
      public void run() {
        Process.setThreadPriority(this.mPriority);
        super.run();
      }
    }
  }
  
  private static class ProcessPriorityThread extends Thread {
    private final int mPriority;
    
    ProcessPriorityThread(Runnable param1Runnable, String param1String, int param1Int) {
      super(param1Runnable, param1String);
      this.mPriority = param1Int;
    }
    
    public void run() {
      Process.setThreadPriority(this.mPriority);
      super.run();
    }
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
  
  private static class ReplyRunnable<T> implements Runnable {
    private Callable<T> mCallable;
    
    private Consumer<T> mConsumer;
    
    private Handler mHandler;
    
    ReplyRunnable(Handler param1Handler, Callable<T> param1Callable, Consumer<T> param1Consumer) {
      this.mCallable = param1Callable;
      this.mConsumer = param1Consumer;
      this.mHandler = param1Handler;
    }
    
    public void run() {
      try {
        T t = this.mCallable.call();
      } catch (Exception exception) {
        exception = null;
      } 
      final Consumer<T> consumer = this.mConsumer;
      this.mHandler.post(new Runnable() {
            final RequestExecutor.ReplyRunnable this$0;
            
            final Consumer val$consumer;
            
            final Object val$result;
            
            public void run() {
              consumer.accept(result);
            }
          });
    }
  }
  
  class null implements Runnable {
    final RequestExecutor.ReplyRunnable this$0;
    
    final Consumer val$consumer;
    
    final Object val$result;
    
    public void run() {
      consumer.accept(result);
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\provider\RequestExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */