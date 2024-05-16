package androidx.arch.core.executor;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class DefaultTaskExecutor extends TaskExecutor {
  private final ExecutorService mDiskIO = Executors.newFixedThreadPool(4, new ThreadFactory() {
        private static final String THREAD_NAME_STEM = "arch_disk_io_%d";
        
        private final AtomicInteger mThreadId = new AtomicInteger(0);
        
        final DefaultTaskExecutor this$0;
        
        public Thread newThread(Runnable param1Runnable) {
          param1Runnable = new Thread(param1Runnable);
          param1Runnable.setName(String.format("arch_disk_io_%d", new Object[] { Integer.valueOf(this.mThreadId.getAndIncrement()) }));
          return (Thread)param1Runnable;
        }
      });
  
  private final Object mLock = new Object();
  
  private volatile Handler mMainHandler;
  
  private static Handler createAsync(Looper paramLooper) {
    if (Build.VERSION.SDK_INT >= 28)
      return Handler.createAsync(paramLooper); 
    if (Build.VERSION.SDK_INT >= 16)
      try {
        return Handler.class.getDeclaredConstructor(new Class[] { Looper.class, Handler.Callback.class, boolean.class }).newInstance(new Object[] { paramLooper, null, Boolean.valueOf(true) });
      } catch (IllegalAccessException illegalAccessException) {
      
      } catch (InstantiationException instantiationException) {
      
      } catch (NoSuchMethodException noSuchMethodException) {
      
      } catch (InvocationTargetException invocationTargetException) {
        return new Handler(paramLooper);
      }  
    return new Handler(paramLooper);
  }
  
  public void executeOnDiskIO(Runnable paramRunnable) {
    this.mDiskIO.execute(paramRunnable);
  }
  
  public boolean isMainThread() {
    boolean bool;
    if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void postToMainThread(Runnable paramRunnable) {
    if (this.mMainHandler == null)
      synchronized (this.mLock) {
        if (this.mMainHandler == null)
          this.mMainHandler = createAsync(Looper.getMainLooper()); 
      }  
    this.mMainHandler.post(paramRunnable);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\arch\core\executor\DefaultTaskExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */