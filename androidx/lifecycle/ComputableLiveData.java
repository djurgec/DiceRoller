package androidx.lifecycle;

import androidx.arch.core.executor.ArchTaskExecutor;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class ComputableLiveData<T> {
  final AtomicBoolean mComputing = new AtomicBoolean(false);
  
  final Executor mExecutor;
  
  final AtomicBoolean mInvalid = new AtomicBoolean(true);
  
  final Runnable mInvalidationRunnable = new Runnable() {
      final ComputableLiveData this$0;
      
      public void run() {
        boolean bool = ComputableLiveData.this.mLiveData.hasActiveObservers();
        if (ComputableLiveData.this.mInvalid.compareAndSet(false, true) && bool)
          ComputableLiveData.this.mExecutor.execute(ComputableLiveData.this.mRefreshRunnable); 
      }
    };
  
  final LiveData<T> mLiveData;
  
  final Runnable mRefreshRunnable = new Runnable() {
      final ComputableLiveData this$0;
      
      public void run() {
        boolean bool;
        do {
          bool = false;
          boolean bool1 = false;
          if (!ComputableLiveData.this.mComputing.compareAndSet(false, true))
            continue; 
          null = null;
          bool = bool1;
          try {
            while (ComputableLiveData.this.mInvalid.compareAndSet(true, false)) {
              bool = true;
              null = (Exception)ComputableLiveData.this.compute();
            } 
            if (bool)
              ComputableLiveData.this.mLiveData.postValue(null); 
          } finally {
            ComputableLiveData.this.mComputing.set(false);
          } 
        } while (bool && ComputableLiveData.this.mInvalid.get());
      }
    };
  
  public ComputableLiveData() {
    this(ArchTaskExecutor.getIOThreadExecutor());
  }
  
  public ComputableLiveData(Executor paramExecutor) {
    this.mExecutor = paramExecutor;
    this.mLiveData = new LiveData<T>() {
        final ComputableLiveData this$0;
        
        protected void onActive() {
          ComputableLiveData.this.mExecutor.execute(ComputableLiveData.this.mRefreshRunnable);
        }
      };
  }
  
  protected abstract T compute();
  
  public LiveData<T> getLiveData() {
    return this.mLiveData;
  }
  
  public void invalidate() {
    ArchTaskExecutor.getInstance().executeOnMainThread(this.mInvalidationRunnable);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\lifecycle\ComputableLiveData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */