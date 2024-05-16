package androidx.concurrent.futures;

import com.google.common.util.concurrent.ListenableFuture;
import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public final class CallbackToFutureAdapter {
  public static <T> ListenableFuture<T> getFuture(Resolver<T> paramResolver) {
    Completer<?> completer = new Completer();
    SafeFuture<?> safeFuture = new SafeFuture(completer);
    completer.future = safeFuture;
    completer.tag = paramResolver.getClass();
    try {
      Object object = paramResolver.attachCompleter((Completer)completer);
      if (object != null)
        completer.tag = object; 
    } catch (Exception exception) {
      safeFuture.setException(exception);
    } 
    return (ListenableFuture)safeFuture;
  }
  
  public static final class Completer<T> {
    private boolean attemptedSetting;
    
    private ResolvableFuture<Void> cancellationFuture = ResolvableFuture.create();
    
    CallbackToFutureAdapter.SafeFuture<T> future;
    
    Object tag;
    
    private void setCompletedNormally() {
      this.tag = null;
      this.future = null;
      this.cancellationFuture = null;
    }
    
    public void addCancellationListener(Runnable param1Runnable, Executor param1Executor) {
      ResolvableFuture<Void> resolvableFuture = this.cancellationFuture;
      if (resolvableFuture != null)
        resolvableFuture.addListener(param1Runnable, param1Executor); 
    }
    
    protected void finalize() {
      CallbackToFutureAdapter.SafeFuture<T> safeFuture = this.future;
      if (safeFuture != null && !safeFuture.isDone())
        safeFuture.setException(new CallbackToFutureAdapter.FutureGarbageCollectedException("The completer object was garbage collected - this future would otherwise never complete. The tag was: " + this.tag)); 
      if (!this.attemptedSetting) {
        ResolvableFuture<Void> resolvableFuture = this.cancellationFuture;
        if (resolvableFuture != null)
          resolvableFuture.set(null); 
      } 
    }
    
    void fireCancellationListeners() {
      this.tag = null;
      this.future = null;
      this.cancellationFuture.set(null);
    }
    
    public boolean set(T param1T) {
      boolean bool = true;
      this.attemptedSetting = true;
      CallbackToFutureAdapter.SafeFuture<T> safeFuture = this.future;
      if (safeFuture == null || !safeFuture.set(param1T))
        bool = false; 
      if (bool)
        setCompletedNormally(); 
      return bool;
    }
    
    public boolean setCancelled() {
      boolean bool = true;
      this.attemptedSetting = true;
      CallbackToFutureAdapter.SafeFuture<T> safeFuture = this.future;
      if (safeFuture == null || !safeFuture.cancelWithoutNotifyingCompleter(true))
        bool = false; 
      if (bool)
        setCompletedNormally(); 
      return bool;
    }
    
    public boolean setException(Throwable param1Throwable) {
      boolean bool = true;
      this.attemptedSetting = true;
      CallbackToFutureAdapter.SafeFuture<T> safeFuture = this.future;
      if (safeFuture == null || !safeFuture.setException(param1Throwable))
        bool = false; 
      if (bool)
        setCompletedNormally(); 
      return bool;
    }
  }
  
  static final class FutureGarbageCollectedException extends Throwable {
    FutureGarbageCollectedException(String param1String) {
      super(param1String);
    }
    
    public Throwable fillInStackTrace() {
      /* monitor enter ThisExpression{InnerObjectType{ObjectType{androidx/concurrent/futures/CallbackToFutureAdapter}.Landroidx/concurrent/futures/CallbackToFutureAdapter$FutureGarbageCollectedException;}} */
      /* monitor exit ThisExpression{InnerObjectType{ObjectType{androidx/concurrent/futures/CallbackToFutureAdapter}.Landroidx/concurrent/futures/CallbackToFutureAdapter$FutureGarbageCollectedException;}} */
      return this;
    }
  }
  
  public static interface Resolver<T> {
    Object attachCompleter(CallbackToFutureAdapter.Completer<T> param1Completer) throws Exception;
  }
  
  private static final class SafeFuture<T> implements ListenableFuture<T> {
    final WeakReference<CallbackToFutureAdapter.Completer<T>> completerWeakReference;
    
    private final AbstractResolvableFuture<T> delegate = new AbstractResolvableFuture<T>() {
        final CallbackToFutureAdapter.SafeFuture this$0;
        
        protected String pendingToString() {
          CallbackToFutureAdapter.Completer completer = CallbackToFutureAdapter.SafeFuture.this.completerWeakReference.get();
          return (completer == null) ? "Completer object has been garbage collected, future will fail soon" : ("tag=[" + completer.tag + "]");
        }
      };
    
    SafeFuture(CallbackToFutureAdapter.Completer<T> param1Completer) {
      this.completerWeakReference = new WeakReference<>(param1Completer);
    }
    
    public void addListener(Runnable param1Runnable, Executor param1Executor) {
      this.delegate.addListener(param1Runnable, param1Executor);
    }
    
    public boolean cancel(boolean param1Boolean) {
      CallbackToFutureAdapter.Completer completer = this.completerWeakReference.get();
      param1Boolean = this.delegate.cancel(param1Boolean);
      if (param1Boolean && completer != null)
        completer.fireCancellationListeners(); 
      return param1Boolean;
    }
    
    boolean cancelWithoutNotifyingCompleter(boolean param1Boolean) {
      return this.delegate.cancel(param1Boolean);
    }
    
    public T get() throws InterruptedException, ExecutionException {
      return this.delegate.get();
    }
    
    public T get(long param1Long, TimeUnit param1TimeUnit) throws InterruptedException, ExecutionException, TimeoutException {
      return this.delegate.get(param1Long, param1TimeUnit);
    }
    
    public boolean isCancelled() {
      return this.delegate.isCancelled();
    }
    
    public boolean isDone() {
      return this.delegate.isDone();
    }
    
    boolean set(T param1T) {
      return this.delegate.set(param1T);
    }
    
    boolean setException(Throwable param1Throwable) {
      return this.delegate.setException(param1Throwable);
    }
    
    public String toString() {
      return this.delegate.toString();
    }
  }
  
  class null extends AbstractResolvableFuture<T> {
    final CallbackToFutureAdapter.SafeFuture this$0;
    
    protected String pendingToString() {
      CallbackToFutureAdapter.Completer completer = this.this$0.completerWeakReference.get();
      return (completer == null) ? "Completer object has been garbage collected, future will fail soon" : ("tag=[" + completer.tag + "]");
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\concurrent\futures\CallbackToFutureAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */