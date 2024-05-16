package androidx.loader.content;

import android.os.Binder;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

abstract class ModernAsyncTask<Params, Progress, Result> {
  private static final int CORE_POOL_SIZE = 5;
  
  private static final int KEEP_ALIVE = 1;
  
  private static final String LOG_TAG = "AsyncTask";
  
  private static final int MAXIMUM_POOL_SIZE = 128;
  
  private static final int MESSAGE_POST_PROGRESS = 2;
  
  private static final int MESSAGE_POST_RESULT = 1;
  
  public static final Executor THREAD_POOL_EXECUTOR;
  
  private static volatile Executor sDefaultExecutor;
  
  private static InternalHandler sHandler;
  
  private static final BlockingQueue<Runnable> sPoolWorkQueue;
  
  private static final ThreadFactory sThreadFactory;
  
  final AtomicBoolean mCancelled = new AtomicBoolean();
  
  private final FutureTask<Result> mFuture;
  
  private volatile Status mStatus = Status.PENDING;
  
  final AtomicBoolean mTaskInvoked = new AtomicBoolean();
  
  private final WorkerRunnable<Params, Result> mWorker;
  
  static {
    ThreadFactory threadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);
        
        public Thread newThread(Runnable param1Runnable) {
          return new Thread(param1Runnable, "ModernAsyncTask #" + this.mCount.getAndIncrement());
        }
      };
    sThreadFactory = threadFactory;
    LinkedBlockingQueue<Runnable> linkedBlockingQueue = new LinkedBlockingQueue(10);
    sPoolWorkQueue = linkedBlockingQueue;
    ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 128, 1L, TimeUnit.SECONDS, linkedBlockingQueue, threadFactory);
    THREAD_POOL_EXECUTOR = threadPoolExecutor;
    sDefaultExecutor = threadPoolExecutor;
  }
  
  ModernAsyncTask() {
    WorkerRunnable<Params, Result> workerRunnable = new WorkerRunnable<Params, Result>() {
        final ModernAsyncTask this$0;
        
        public Result call() throws Exception {
          ModernAsyncTask.this.mTaskInvoked.set(true);
          Object object2 = null;
          Object object1 = object2;
          try {
            Process.setThreadPriority(10);
            object1 = object2;
            object2 = ModernAsyncTask.this.doInBackground((Object[])this.mParams);
            object1 = object2;
            Binder.flushPendingCommands();
            return (Result)object2;
          } finally {
            object2 = null;
          } 
        }
      };
    this.mWorker = workerRunnable;
    this.mFuture = new FutureTask<Result>(workerRunnable) {
        final ModernAsyncTask this$0;
        
        protected void done() {
          try {
            Result result = get();
            ModernAsyncTask.this.postResultIfNotInvoked(result);
          } catch (InterruptedException interruptedException) {
          
          } catch (ExecutionException executionException) {
            throw new RuntimeException("An error occurred while executing doInBackground()", executionException.getCause());
          } catch (CancellationException cancellationException) {
          
          } finally {
            Exception exception = null;
          } 
        }
      };
  }
  
  public static void execute(Runnable paramRunnable) {
    sDefaultExecutor.execute(paramRunnable);
  }
  
  private static Handler getHandler() {
    // Byte code:
    //   0: ldc androidx/loader/content/ModernAsyncTask
    //   2: monitorenter
    //   3: getstatic androidx/loader/content/ModernAsyncTask.sHandler : Landroidx/loader/content/ModernAsyncTask$InternalHandler;
    //   6: ifnonnull -> 21
    //   9: new androidx/loader/content/ModernAsyncTask$InternalHandler
    //   12: astore_0
    //   13: aload_0
    //   14: invokespecial <init> : ()V
    //   17: aload_0
    //   18: putstatic androidx/loader/content/ModernAsyncTask.sHandler : Landroidx/loader/content/ModernAsyncTask$InternalHandler;
    //   21: getstatic androidx/loader/content/ModernAsyncTask.sHandler : Landroidx/loader/content/ModernAsyncTask$InternalHandler;
    //   24: astore_0
    //   25: ldc androidx/loader/content/ModernAsyncTask
    //   27: monitorexit
    //   28: aload_0
    //   29: areturn
    //   30: astore_0
    //   31: ldc androidx/loader/content/ModernAsyncTask
    //   33: monitorexit
    //   34: aload_0
    //   35: athrow
    // Exception table:
    //   from	to	target	type
    //   3	21	30	finally
    //   21	28	30	finally
    //   31	34	30	finally
  }
  
  public static void setDefaultExecutor(Executor paramExecutor) {
    sDefaultExecutor = paramExecutor;
  }
  
  public final boolean cancel(boolean paramBoolean) {
    this.mCancelled.set(true);
    return this.mFuture.cancel(paramBoolean);
  }
  
  protected abstract Result doInBackground(Params... paramVarArgs);
  
  public final ModernAsyncTask<Params, Progress, Result> execute(Params... paramVarArgs) {
    return executeOnExecutor(sDefaultExecutor, paramVarArgs);
  }
  
  public final ModernAsyncTask<Params, Progress, Result> executeOnExecutor(Executor paramExecutor, Params... paramVarArgs) {
    if (this.mStatus != Status.PENDING) {
      switch (this.mStatus) {
        default:
          throw new IllegalStateException("We should never reach this state");
        case null:
          throw new IllegalStateException("Cannot execute task: the task has already been executed (a task can be executed only once)");
        case null:
          break;
      } 
      throw new IllegalStateException("Cannot execute task: the task is already running.");
    } 
    this.mStatus = Status.RUNNING;
    onPreExecute();
    this.mWorker.mParams = paramVarArgs;
    paramExecutor.execute(this.mFuture);
    return this;
  }
  
  void finish(Result paramResult) {
    if (isCancelled()) {
      onCancelled(paramResult);
    } else {
      onPostExecute(paramResult);
    } 
    this.mStatus = Status.FINISHED;
  }
  
  public final Result get() throws InterruptedException, ExecutionException {
    return this.mFuture.get();
  }
  
  public final Result get(long paramLong, TimeUnit paramTimeUnit) throws InterruptedException, ExecutionException, TimeoutException {
    return this.mFuture.get(paramLong, paramTimeUnit);
  }
  
  public final Status getStatus() {
    return this.mStatus;
  }
  
  public final boolean isCancelled() {
    return this.mCancelled.get();
  }
  
  protected void onCancelled() {}
  
  protected void onCancelled(Result paramResult) {
    onCancelled();
  }
  
  protected void onPostExecute(Result paramResult) {}
  
  protected void onPreExecute() {}
  
  protected void onProgressUpdate(Progress... paramVarArgs) {}
  
  Result postResult(Result paramResult) {
    getHandler().obtainMessage(1, new AsyncTaskResult(this, new Object[] { paramResult })).sendToTarget();
    return paramResult;
  }
  
  void postResultIfNotInvoked(Result paramResult) {
    if (!this.mTaskInvoked.get())
      postResult(paramResult); 
  }
  
  protected final void publishProgress(Progress... paramVarArgs) {
    if (!isCancelled())
      getHandler().obtainMessage(2, new AsyncTaskResult<>(this, paramVarArgs)).sendToTarget(); 
  }
  
  private static class AsyncTaskResult<Data> {
    final Data[] mData;
    
    final ModernAsyncTask mTask;
    
    AsyncTaskResult(ModernAsyncTask param1ModernAsyncTask, Data... param1VarArgs) {
      this.mTask = param1ModernAsyncTask;
      this.mData = param1VarArgs;
    }
  }
  
  private static class InternalHandler extends Handler {
    InternalHandler() {
      super(Looper.getMainLooper());
    }
    
    public void handleMessage(Message param1Message) {
      ModernAsyncTask.AsyncTaskResult asyncTaskResult = (ModernAsyncTask.AsyncTaskResult)param1Message.obj;
      switch (param1Message.what) {
        default:
          return;
        case 2:
          asyncTaskResult.mTask.onProgressUpdate((Object[])asyncTaskResult.mData);
        case 1:
          break;
      } 
      asyncTaskResult.mTask.finish(asyncTaskResult.mData[0]);
    }
  }
  
  public enum Status {
    FINISHED, PENDING, RUNNING;
    
    private static final Status[] $VALUES;
    
    static {
      Status status1 = new Status("PENDING", 0);
      PENDING = status1;
      Status status3 = new Status("RUNNING", 1);
      RUNNING = status3;
      Status status2 = new Status("FINISHED", 2);
      FINISHED = status2;
      $VALUES = new Status[] { status1, status3, status2 };
    }
  }
  
  private static abstract class WorkerRunnable<Params, Result> implements Callable<Result> {
    Params[] mParams;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\loader\content\ModernAsyncTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */