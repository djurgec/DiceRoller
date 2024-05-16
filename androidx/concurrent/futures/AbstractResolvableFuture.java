package androidx.concurrent.futures;

import com.google.common.util.concurrent.ListenableFuture;
import java.util.Locale;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.concurrent.locks.LockSupport;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AbstractResolvableFuture<V> implements ListenableFuture<V> {
  static {
    Exception exception = null;
    try {
      SafeAtomicHelper safeAtomicHelper = new SafeAtomicHelper();
      this(AtomicReferenceFieldUpdater.newUpdater(Waiter.class, Thread.class, "thread"), AtomicReferenceFieldUpdater.newUpdater(Waiter.class, Waiter.class, "next"), AtomicReferenceFieldUpdater.newUpdater(AbstractResolvableFuture.class, Waiter.class, "waiters"), AtomicReferenceFieldUpdater.newUpdater(AbstractResolvableFuture.class, Listener.class, "listeners"), AtomicReferenceFieldUpdater.newUpdater(AbstractResolvableFuture.class, Object.class, "value"));
    } finally {
      exception = null;
    } 
    if (exception != null)
      log.log(Level.SEVERE, "SafeAtomicHelper is broken!", exception); 
    NULL = new Object();
  }
  
  private void addDoneString(StringBuilder paramStringBuilder) {
    try {
      Object object = getUninterruptibly((Future<Object>)this);
      paramStringBuilder.append("SUCCESS, result=[").append(userObjectToString(object)).append("]");
    } catch (ExecutionException executionException) {
      paramStringBuilder.append("FAILURE, cause=[").append(executionException.getCause()).append("]");
    } catch (CancellationException cancellationException) {
      paramStringBuilder.append("CANCELLED");
    } catch (RuntimeException runtimeException) {
      paramStringBuilder.append("UNKNOWN, cause=[").append(runtimeException.getClass()).append(" thrown from get()]");
    } 
  }
  
  private static CancellationException cancellationExceptionWithCause(String paramString, Throwable paramThrowable) {
    CancellationException cancellationException = new CancellationException(paramString);
    cancellationException.initCause(paramThrowable);
    return cancellationException;
  }
  
  static <T> T checkNotNull(T paramT) {
    if (paramT != null)
      return paramT; 
    throw new NullPointerException();
  }
  
  private Listener clearListeners(Listener paramListener) {
    while (true) {
      Listener listener = this.listeners;
      if (ATOMIC_HELPER.casListeners(this, listener, Listener.TOMBSTONE)) {
        Listener listener1 = paramListener;
        for (paramListener = listener; paramListener != null; paramListener = listener) {
          listener = paramListener.next;
          paramListener.next = listener1;
          listener1 = paramListener;
        } 
        return listener1;
      } 
    } 
  }
  
  static void complete(AbstractResolvableFuture<?> paramAbstractResolvableFuture) {
    AbstractResolvableFuture abstractResolvableFuture1 = null;
    AbstractResolvableFuture<?> abstractResolvableFuture = paramAbstractResolvableFuture;
    paramAbstractResolvableFuture = abstractResolvableFuture1;
    label18: while (true) {
      abstractResolvableFuture.releaseWaiters();
      abstractResolvableFuture.afterDone();
      Listener listener = abstractResolvableFuture.clearListeners((Listener)paramAbstractResolvableFuture);
      while (true) {
        Listener listener1 = listener;
        if (listener1 != null) {
          AbstractResolvableFuture<V> abstractResolvableFuture2;
          listener = listener1.next;
          Runnable runnable = listener1.task;
          if (runnable instanceof SetFuture) {
            runnable = runnable;
            abstractResolvableFuture2 = ((SetFuture)runnable).owner;
            if (abstractResolvableFuture2.value == runnable) {
              Object object = getFutureValue(((SetFuture)runnable).future);
              if (ATOMIC_HELPER.casValue(abstractResolvableFuture2, runnable, object))
                continue label18; 
            } 
            continue;
          } 
          executeListener(runnable, ((Listener)abstractResolvableFuture2).executor);
          continue;
        } 
        break;
      } 
      break;
    } 
  }
  
  private static void executeListener(Runnable paramRunnable, Executor paramExecutor) {
    try {
      paramExecutor.execute(paramRunnable);
    } catch (RuntimeException runtimeException) {
      log.log(Level.SEVERE, "RuntimeException while executing runnable " + paramRunnable + " with executor " + paramExecutor, runtimeException);
    } 
  }
  
  private V getDoneValue(Object paramObject) throws ExecutionException {
    if (!(paramObject instanceof Cancellation)) {
      if (!(paramObject instanceof Failure))
        return (V)((paramObject == NULL) ? null : paramObject); 
      throw new ExecutionException(((Failure)paramObject).exception);
    } 
    throw cancellationExceptionWithCause("Task was cancelled.", ((Cancellation)paramObject).cause);
  }
  
  static Object getFutureValue(ListenableFuture<?> paramListenableFuture) {
    if (paramListenableFuture instanceof AbstractResolvableFuture) {
      Object object1 = ((AbstractResolvableFuture)paramListenableFuture).value;
      object = object1;
      if (object1 instanceof Cancellation) {
        Cancellation cancellation = (Cancellation)object1;
        object = object1;
        if (cancellation.wasInterrupted)
          if (cancellation.cause != null) {
            object = new Cancellation(false, cancellation.cause);
          } else {
            object = Cancellation.CAUSELESS_CANCELLED;
          }  
      } 
      return object;
    } 
    boolean bool = object.isCancelled();
    if (((GENERATE_CANCELLATION_CAUSES ^ true) & bool) != 0)
      return Cancellation.CAUSELESS_CANCELLED; 
    try {
      Object object1 = getUninterruptibly((Future<Object>)object);
      return object;
    } catch (ExecutionException object) {
      return new Failure(object.getCause());
    } catch (CancellationException cancellationException) {
      return !bool ? new Failure(new IllegalArgumentException("get() threw CancellationException, despite reporting isCancelled() == false: " + object, cancellationException)) : new Cancellation(false, cancellationException);
    } finally {
      object = null;
    } 
  }
  
  private static <V> V getUninterruptibly(Future<V> paramFuture) throws ExecutionException {
    boolean bool = false;
    while (true) {
      try {
        return paramFuture.get();
      } catch (InterruptedException interruptedException) {
      
      } finally {
        if (bool)
          Thread.currentThread().interrupt(); 
      } 
    } 
  }
  
  private void releaseWaiters() {
    Waiter waiter;
    do {
      waiter = this.waiters;
    } while (!ATOMIC_HELPER.casWaiters(this, waiter, Waiter.TOMBSTONE));
    while (waiter != null) {
      waiter.unpark();
      waiter = waiter.next;
    } 
  }
  
  private void removeWaiter(Waiter paramWaiter) {
    paramWaiter.thread = null;
    label23: while (true) {
      Waiter waiter1 = null;
      Waiter waiter2 = this.waiters;
      paramWaiter = waiter2;
      if (waiter2 == Waiter.TOMBSTONE)
        return; 
      while (paramWaiter != null) {
        Waiter waiter = paramWaiter.next;
        if (paramWaiter.thread != null) {
          waiter2 = paramWaiter;
        } else if (waiter1 != null) {
          waiter1.next = waiter;
          waiter2 = waiter1;
          if (waiter1.thread == null)
            continue label23; 
        } else {
          waiter2 = waiter1;
          if (!ATOMIC_HELPER.casWaiters(this, paramWaiter, waiter))
            continue label23; 
        } 
        paramWaiter = waiter;
        waiter1 = waiter2;
      } 
      break;
    } 
  }
  
  private String userObjectToString(Object paramObject) {
    return (paramObject == this) ? "this future" : String.valueOf(paramObject);
  }
  
  public final void addListener(Runnable paramRunnable, Executor paramExecutor) {
    checkNotNull(paramRunnable);
    checkNotNull(paramExecutor);
    Listener listener = this.listeners;
    if (listener != Listener.TOMBSTONE) {
      Listener listener1;
      Listener listener2 = new Listener(paramRunnable, paramExecutor);
      do {
        listener2.next = listener;
        if (ATOMIC_HELPER.casListeners(this, listener, listener2))
          return; 
        listener1 = this.listeners;
        listener = listener1;
      } while (listener1 != Listener.TOMBSTONE);
    } 
    executeListener(paramRunnable, paramExecutor);
  }
  
  protected void afterDone() {}
  
  public final boolean cancel(boolean paramBoolean) {
    boolean bool;
    Object object = this.value;
    boolean bool1 = false;
    boolean bool2 = false;
    if (object == null) {
      bool = true;
    } else {
      bool = false;
    } 
    if (bool | object instanceof SetFuture) {
      Cancellation cancellation;
      Object object1;
      if (GENERATE_CANCELLATION_CAUSES) {
        cancellation = new Cancellation(paramBoolean, new CancellationException("Future.cancel() was called."));
      } else if (paramBoolean) {
        cancellation = Cancellation.CAUSELESS_INTERRUPTED;
      } else {
        cancellation = Cancellation.CAUSELESS_CANCELLED;
      } 
      AbstractResolvableFuture<?> abstractResolvableFuture = this;
      bool1 = bool2;
      label41: do {
        while (ATOMIC_HELPER.casValue(abstractResolvableFuture, object, cancellation)) {
          boolean bool3 = true;
          bool2 = true;
          if (paramBoolean)
            abstractResolvableFuture.interruptTask(); 
          complete(abstractResolvableFuture);
          bool1 = bool3;
          if (object instanceof SetFuture) {
            object = ((SetFuture)object).future;
            if (object instanceof AbstractResolvableFuture) {
              abstractResolvableFuture = (AbstractResolvableFuture)object;
              object = abstractResolvableFuture.value;
              if (object == null) {
                bool = true;
              } else {
                bool = false;
              } 
              if (bool | object instanceof SetFuture) {
                bool1 = bool2;
                continue;
              } 
            } else {
              object.cancel(paramBoolean);
            } 
            bool1 = bool3;
            break label41;
          } 
          break label41;
        } 
        object1 = abstractResolvableFuture.value;
        object = object1;
      } while (object1 instanceof SetFuture);
    } 
    return bool1;
  }
  
  public final V get() throws InterruptedException, ExecutionException {
    if (!Thread.interrupted()) {
      boolean bool;
      Object object = this.value;
      if (object != null) {
        bool = true;
      } else {
        bool = false;
      } 
      if ((bool & (object instanceof SetFuture ^ true)) != 0)
        return getDoneValue(object); 
      object = this.waiters;
      if (object != Waiter.TOMBSTONE) {
        Waiter waiter1;
        Waiter waiter2 = new Waiter();
        do {
          waiter2.setNext((Waiter)object);
          if (ATOMIC_HELPER.casWaiters(this, (Waiter)object, waiter2))
            while (true) {
              LockSupport.park(this);
              if (!Thread.interrupted()) {
                object = this.value;
                if (object != null) {
                  bool = true;
                } else {
                  bool = false;
                } 
                if ((bool & (object instanceof SetFuture ^ true)) != 0)
                  return getDoneValue(object); 
                continue;
              } 
              removeWaiter(waiter2);
              throw new InterruptedException();
            }  
          waiter1 = this.waiters;
          object = waiter1;
        } while (waiter1 != Waiter.TOMBSTONE);
      } 
      return getDoneValue(this.value);
    } 
    throw new InterruptedException();
  }
  
  public final V get(long paramLong, TimeUnit paramTimeUnit) throws InterruptedException, TimeoutException, ExecutionException {
    long l = paramTimeUnit.toNanos(paramLong);
    if (!Thread.interrupted()) {
      Object object1;
      boolean bool;
      long l2;
      Object object2 = this.value;
      if (object2 != null) {
        bool = true;
      } else {
        bool = false;
      } 
      if ((bool & (object2 instanceof SetFuture ^ true)) != 0)
        return getDoneValue(object2); 
      if (l > 0L) {
        l2 = System.nanoTime() + l;
      } else {
        l2 = 0L;
      } 
      long l1 = l;
      if (l >= 1000L) {
        object2 = this.waiters;
        if (object2 != Waiter.TOMBSTONE) {
          Waiter waiter = new Waiter();
          while (true) {
            waiter.setNext((Waiter)object2);
            if (ATOMIC_HELPER.casWaiters(this, (Waiter)object2, waiter)) {
              l1 = l;
              while (true) {
                LockSupport.parkNanos(this, l1);
                if (!Thread.interrupted()) {
                  object2 = this.value;
                  if (object2 != null) {
                    bool = true;
                  } else {
                    bool = false;
                  } 
                  if ((bool & (object2 instanceof SetFuture ^ true)) != 0)
                    return getDoneValue(object2); 
                  l1 = l2 - System.nanoTime();
                  if (l1 < 1000L) {
                    removeWaiter(waiter);
                    break;
                  } 
                  continue;
                } 
                removeWaiter(waiter);
                throw new InterruptedException();
              } 
            } 
            object2 = this.waiters;
            if (object2 == Waiter.TOMBSTONE)
              // Byte code: goto -> 250 
          } 
        } else {
          return getDoneValue(this.value);
        } 
      } 
      while (l1 > 0L) {
        object2 = this.value;
        if (object2 != null) {
          bool = true;
        } else {
          bool = false;
        } 
        if ((bool & (object2 instanceof SetFuture ^ true)) != 0)
          return getDoneValue(object2); 
        if (!Thread.interrupted()) {
          l1 = l2 - System.nanoTime();
          continue;
        } 
        throw new InterruptedException();
      } 
      String str1 = toString();
      String str2 = paramTimeUnit.toString().toLowerCase(Locale.ROOT);
      object2 = "Waited " + paramLong + " " + paramTimeUnit.toString().toLowerCase(Locale.ROOT);
      if (l1 + 1000L < 0L) {
        object2 = object2 + " (plus ";
        l1 = -l1;
        paramLong = paramTimeUnit.convert(l1, TimeUnit.NANOSECONDS);
        l1 -= paramTimeUnit.toNanos(paramLong);
        if (paramLong == 0L || l1 > 1000L) {
          bool = true;
        } else {
          bool = false;
        } 
        object1 = object2;
        if (paramLong > 0L) {
          object1 = object2 + paramLong + " " + str2;
          if (bool)
            object1 = object1 + ","; 
          object1 = object1 + " ";
        } 
        object2 = object1;
        if (bool)
          object2 = object1 + l1 + " nanoseconds "; 
        object1 = object2 + "delay)";
      } else {
        object1 = object2;
      } 
      if (isDone())
        throw new TimeoutException(object1 + " but future completed as timeout expired"); 
      throw new TimeoutException(object1 + " for " + str1);
    } 
    throw new InterruptedException();
  }
  
  protected void interruptTask() {}
  
  public final boolean isCancelled() {
    return this.value instanceof Cancellation;
  }
  
  public final boolean isDone() {
    boolean bool;
    Object object = this.value;
    if (object != null) {
      bool = true;
    } else {
      bool = false;
    } 
    return (true ^ object instanceof SetFuture) & bool;
  }
  
  final void maybePropagateCancellationTo(Future<?> paramFuture) {
    boolean bool;
    if (paramFuture != null) {
      bool = true;
    } else {
      bool = false;
    } 
    if (bool & isCancelled())
      paramFuture.cancel(wasInterrupted()); 
  }
  
  protected String pendingToString() {
    Object object = this.value;
    return (object instanceof SetFuture) ? ("setFuture=[" + userObjectToString(((SetFuture)object).future) + "]") : ((this instanceof ScheduledFuture) ? ("remaining delay=[" + ((ScheduledFuture)this).getDelay(TimeUnit.MILLISECONDS) + " ms]") : null);
  }
  
  protected boolean set(V paramV) {
    if (paramV == null)
      paramV = (V)NULL; 
    if (ATOMIC_HELPER.casValue(this, null, paramV)) {
      complete(this);
      return true;
    } 
    return false;
  }
  
  protected boolean setException(Throwable paramThrowable) {
    Failure failure = new Failure(checkNotNull(paramThrowable));
    if (ATOMIC_HELPER.casValue(this, null, failure)) {
      complete(this);
      return true;
    } 
    return false;
  }
  
  protected boolean setFuture(ListenableFuture<? extends V> paramListenableFuture) {
    Object object1;
    checkNotNull(paramListenableFuture);
    Object object3 = this.value;
    Object object2 = object3;
    if (object3 == null) {
      if (paramListenableFuture.isDone()) {
        object1 = getFutureValue(paramListenableFuture);
        if (ATOMIC_HELPER.casValue(this, null, object1)) {
          complete(this);
          return true;
        } 
        return false;
      } 
      object2 = new SetFuture(this, (ListenableFuture<?>)object1);
      if (ATOMIC_HELPER.casValue(this, null, object2))
        try {
          object1.addListener((Runnable)object2, DirectExecutor.INSTANCE);
        } finally {
          object3 = null;
        }  
      object2 = this.value;
    } 
    if (object2 instanceof Cancellation)
      object1.cancel(((Cancellation)object2).wasInterrupted); 
    return false;
  }
  
  public String toString() {
    StringBuilder stringBuilder = (new StringBuilder()).append(super.toString()).append("[status=");
    if (isCancelled()) {
      stringBuilder.append("CANCELLED");
    } else if (isDone()) {
      addDoneString(stringBuilder);
    } else {
      String str;
      try {
        str = pendingToString();
      } catch (RuntimeException runtimeException) {
        str = "Exception thrown from implementation: " + runtimeException.getClass();
      } 
      if (str != null && !str.isEmpty()) {
        stringBuilder.append("PENDING, info=[").append(str).append("]");
      } else if (isDone()) {
        addDoneString(stringBuilder);
      } else {
        stringBuilder.append("PENDING");
      } 
    } 
    return stringBuilder.append("]").toString();
  }
  
  protected final boolean wasInterrupted() {
    boolean bool;
    Object object = this.value;
    if (object instanceof Cancellation && ((Cancellation)object).wasInterrupted) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  static {
    SynchronizedHelper synchronizedHelper;
  }
  
  static final AtomicHelper ATOMIC_HELPER;
  
  static final boolean GENERATE_CANCELLATION_CAUSES = Boolean.parseBoolean(System.getProperty("guava.concurrent.generate_cancellation_cause", "false"));
  
  private static final Object NULL;
  
  private static final long SPIN_THRESHOLD_NANOS = 1000L;
  
  private static final Logger log = Logger.getLogger(AbstractResolvableFuture.class.getName());
  
  volatile Listener listeners;
  
  volatile Object value;
  
  volatile Waiter waiters;
  
  private static abstract class AtomicHelper {
    private AtomicHelper() {}
    
    abstract boolean casListeners(AbstractResolvableFuture<?> param1AbstractResolvableFuture, AbstractResolvableFuture.Listener param1Listener1, AbstractResolvableFuture.Listener param1Listener2);
    
    abstract boolean casValue(AbstractResolvableFuture<?> param1AbstractResolvableFuture, Object param1Object1, Object param1Object2);
    
    abstract boolean casWaiters(AbstractResolvableFuture<?> param1AbstractResolvableFuture, AbstractResolvableFuture.Waiter param1Waiter1, AbstractResolvableFuture.Waiter param1Waiter2);
    
    abstract void putNext(AbstractResolvableFuture.Waiter param1Waiter1, AbstractResolvableFuture.Waiter param1Waiter2);
    
    abstract void putThread(AbstractResolvableFuture.Waiter param1Waiter, Thread param1Thread);
  }
  
  private static final class Cancellation {
    static final Cancellation CAUSELESS_CANCELLED;
    
    static final Cancellation CAUSELESS_INTERRUPTED;
    
    final Throwable cause;
    
    final boolean wasInterrupted;
    
    static {
      if (AbstractResolvableFuture.GENERATE_CANCELLATION_CAUSES) {
        CAUSELESS_CANCELLED = null;
        CAUSELESS_INTERRUPTED = null;
      } else {
        CAUSELESS_CANCELLED = new Cancellation(false, null);
        CAUSELESS_INTERRUPTED = new Cancellation(true, null);
      } 
    }
    
    Cancellation(boolean param1Boolean, Throwable param1Throwable) {
      this.wasInterrupted = param1Boolean;
      this.cause = param1Throwable;
    }
  }
  
  private static final class Failure {
    static final Failure FALLBACK_INSTANCE = new Failure(new Throwable("Failure occurred while trying to finish a future.") {
          public Throwable fillInStackTrace() {
            /* monitor enter ThisExpression{InnerObjectType{InnerObjectType{ObjectType{androidx/concurrent/futures/AbstractResolvableFuture}.Landroidx/concurrent/futures/AbstractResolvableFuture$Failure;}.Landroidx/concurrent/futures/AbstractResolvableFuture$Failure$1;}} */
            /* monitor exit ThisExpression{InnerObjectType{InnerObjectType{ObjectType{androidx/concurrent/futures/AbstractResolvableFuture}.Landroidx/concurrent/futures/AbstractResolvableFuture$Failure;}.Landroidx/concurrent/futures/AbstractResolvableFuture$Failure$1;}} */
            return this;
          }
        });
    
    final Throwable exception;
    
    Failure(Throwable param1Throwable) {
      this.exception = AbstractResolvableFuture.<Throwable>checkNotNull(param1Throwable);
    }
  }
  
  static final class null extends Throwable {
    null(String param1String) {
      super(param1String);
    }
    
    public Throwable fillInStackTrace() {
      /* monitor enter ThisExpression{InnerObjectType{InnerObjectType{ObjectType{androidx/concurrent/futures/AbstractResolvableFuture}.Landroidx/concurrent/futures/AbstractResolvableFuture$Failure;}.Landroidx/concurrent/futures/AbstractResolvableFuture$Failure$1;}} */
      /* monitor exit ThisExpression{InnerObjectType{InnerObjectType{ObjectType{androidx/concurrent/futures/AbstractResolvableFuture}.Landroidx/concurrent/futures/AbstractResolvableFuture$Failure;}.Landroidx/concurrent/futures/AbstractResolvableFuture$Failure$1;}} */
      return this;
    }
  }
  
  private static final class Listener {
    static final Listener TOMBSTONE = new Listener(null, null);
    
    final Executor executor;
    
    Listener next;
    
    final Runnable task;
    
    Listener(Runnable param1Runnable, Executor param1Executor) {
      this.task = param1Runnable;
      this.executor = param1Executor;
    }
  }
  
  private static final class SafeAtomicHelper extends AtomicHelper {
    final AtomicReferenceFieldUpdater<AbstractResolvableFuture, AbstractResolvableFuture.Listener> listenersUpdater;
    
    final AtomicReferenceFieldUpdater<AbstractResolvableFuture, Object> valueUpdater;
    
    final AtomicReferenceFieldUpdater<AbstractResolvableFuture.Waiter, AbstractResolvableFuture.Waiter> waiterNextUpdater;
    
    final AtomicReferenceFieldUpdater<AbstractResolvableFuture.Waiter, Thread> waiterThreadUpdater;
    
    final AtomicReferenceFieldUpdater<AbstractResolvableFuture, AbstractResolvableFuture.Waiter> waitersUpdater;
    
    SafeAtomicHelper(AtomicReferenceFieldUpdater<AbstractResolvableFuture.Waiter, Thread> param1AtomicReferenceFieldUpdater, AtomicReferenceFieldUpdater<AbstractResolvableFuture.Waiter, AbstractResolvableFuture.Waiter> param1AtomicReferenceFieldUpdater1, AtomicReferenceFieldUpdater<AbstractResolvableFuture, AbstractResolvableFuture.Waiter> param1AtomicReferenceFieldUpdater2, AtomicReferenceFieldUpdater<AbstractResolvableFuture, AbstractResolvableFuture.Listener> param1AtomicReferenceFieldUpdater3, AtomicReferenceFieldUpdater<AbstractResolvableFuture, Object> param1AtomicReferenceFieldUpdater4) {
      this.waiterThreadUpdater = param1AtomicReferenceFieldUpdater;
      this.waiterNextUpdater = param1AtomicReferenceFieldUpdater1;
      this.waitersUpdater = param1AtomicReferenceFieldUpdater2;
      this.listenersUpdater = param1AtomicReferenceFieldUpdater3;
      this.valueUpdater = param1AtomicReferenceFieldUpdater4;
    }
    
    boolean casListeners(AbstractResolvableFuture<?> param1AbstractResolvableFuture, AbstractResolvableFuture.Listener param1Listener1, AbstractResolvableFuture.Listener param1Listener2) {
      return this.listenersUpdater.compareAndSet(param1AbstractResolvableFuture, param1Listener1, param1Listener2);
    }
    
    boolean casValue(AbstractResolvableFuture<?> param1AbstractResolvableFuture, Object param1Object1, Object param1Object2) {
      return this.valueUpdater.compareAndSet(param1AbstractResolvableFuture, param1Object1, param1Object2);
    }
    
    boolean casWaiters(AbstractResolvableFuture<?> param1AbstractResolvableFuture, AbstractResolvableFuture.Waiter param1Waiter1, AbstractResolvableFuture.Waiter param1Waiter2) {
      return this.waitersUpdater.compareAndSet(param1AbstractResolvableFuture, param1Waiter1, param1Waiter2);
    }
    
    void putNext(AbstractResolvableFuture.Waiter param1Waiter1, AbstractResolvableFuture.Waiter param1Waiter2) {
      this.waiterNextUpdater.lazySet(param1Waiter1, param1Waiter2);
    }
    
    void putThread(AbstractResolvableFuture.Waiter param1Waiter, Thread param1Thread) {
      this.waiterThreadUpdater.lazySet(param1Waiter, param1Thread);
    }
  }
  
  private static final class SetFuture<V> implements Runnable {
    final ListenableFuture<? extends V> future;
    
    final AbstractResolvableFuture<V> owner;
    
    SetFuture(AbstractResolvableFuture<V> param1AbstractResolvableFuture, ListenableFuture<? extends V> param1ListenableFuture) {
      this.owner = param1AbstractResolvableFuture;
      this.future = param1ListenableFuture;
    }
    
    public void run() {
      if (this.owner.value != this)
        return; 
      Object object = AbstractResolvableFuture.getFutureValue(this.future);
      if (AbstractResolvableFuture.ATOMIC_HELPER.casValue(this.owner, this, object))
        AbstractResolvableFuture.complete(this.owner); 
    }
  }
  
  private static final class SynchronizedHelper extends AtomicHelper {
    boolean casListeners(AbstractResolvableFuture<?> param1AbstractResolvableFuture, AbstractResolvableFuture.Listener param1Listener1, AbstractResolvableFuture.Listener param1Listener2) {
      // Byte code:
      //   0: aload_1
      //   1: monitorenter
      //   2: aload_1
      //   3: getfield listeners : Landroidx/concurrent/futures/AbstractResolvableFuture$Listener;
      //   6: aload_2
      //   7: if_acmpne -> 19
      //   10: aload_1
      //   11: aload_3
      //   12: putfield listeners : Landroidx/concurrent/futures/AbstractResolvableFuture$Listener;
      //   15: aload_1
      //   16: monitorexit
      //   17: iconst_1
      //   18: ireturn
      //   19: aload_1
      //   20: monitorexit
      //   21: iconst_0
      //   22: ireturn
      //   23: astore_2
      //   24: aload_1
      //   25: monitorexit
      //   26: aload_2
      //   27: athrow
      // Exception table:
      //   from	to	target	type
      //   2	17	23	finally
      //   19	21	23	finally
      //   24	26	23	finally
    }
    
    boolean casValue(AbstractResolvableFuture<?> param1AbstractResolvableFuture, Object param1Object1, Object param1Object2) {
      // Byte code:
      //   0: aload_1
      //   1: monitorenter
      //   2: aload_1
      //   3: getfield value : Ljava/lang/Object;
      //   6: aload_2
      //   7: if_acmpne -> 19
      //   10: aload_1
      //   11: aload_3
      //   12: putfield value : Ljava/lang/Object;
      //   15: aload_1
      //   16: monitorexit
      //   17: iconst_1
      //   18: ireturn
      //   19: aload_1
      //   20: monitorexit
      //   21: iconst_0
      //   22: ireturn
      //   23: astore_2
      //   24: aload_1
      //   25: monitorexit
      //   26: aload_2
      //   27: athrow
      // Exception table:
      //   from	to	target	type
      //   2	17	23	finally
      //   19	21	23	finally
      //   24	26	23	finally
    }
    
    boolean casWaiters(AbstractResolvableFuture<?> param1AbstractResolvableFuture, AbstractResolvableFuture.Waiter param1Waiter1, AbstractResolvableFuture.Waiter param1Waiter2) {
      // Byte code:
      //   0: aload_1
      //   1: monitorenter
      //   2: aload_1
      //   3: getfield waiters : Landroidx/concurrent/futures/AbstractResolvableFuture$Waiter;
      //   6: aload_2
      //   7: if_acmpne -> 19
      //   10: aload_1
      //   11: aload_3
      //   12: putfield waiters : Landroidx/concurrent/futures/AbstractResolvableFuture$Waiter;
      //   15: aload_1
      //   16: monitorexit
      //   17: iconst_1
      //   18: ireturn
      //   19: aload_1
      //   20: monitorexit
      //   21: iconst_0
      //   22: ireturn
      //   23: astore_2
      //   24: aload_1
      //   25: monitorexit
      //   26: aload_2
      //   27: athrow
      // Exception table:
      //   from	to	target	type
      //   2	17	23	finally
      //   19	21	23	finally
      //   24	26	23	finally
    }
    
    void putNext(AbstractResolvableFuture.Waiter param1Waiter1, AbstractResolvableFuture.Waiter param1Waiter2) {
      param1Waiter1.next = param1Waiter2;
    }
    
    void putThread(AbstractResolvableFuture.Waiter param1Waiter, Thread param1Thread) {
      param1Waiter.thread = param1Thread;
    }
  }
  
  private static final class Waiter {
    static final Waiter TOMBSTONE = new Waiter(false);
    
    volatile Waiter next;
    
    volatile Thread thread;
    
    Waiter() {
      AbstractResolvableFuture.ATOMIC_HELPER.putThread(this, Thread.currentThread());
    }
    
    Waiter(boolean param1Boolean) {}
    
    void setNext(Waiter param1Waiter) {
      AbstractResolvableFuture.ATOMIC_HELPER.putNext(this, param1Waiter);
    }
    
    void unpark() {
      Thread thread = this.thread;
      if (thread != null) {
        this.thread = null;
        LockSupport.unpark(thread);
      } 
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\concurrent\futures\AbstractResolvableFuture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */