package com.bumptech.glide.util.pool;

import android.util.Log;
import androidx.core.util.Pools;
import java.util.ArrayList;
import java.util.List;

public final class FactoryPools {
  private static final int DEFAULT_POOL_SIZE = 20;
  
  private static final Resetter<Object> EMPTY_RESETTER = new Resetter() {
      public void reset(Object param1Object) {}
    };
  
  private static final String TAG = "FactoryPools";
  
  private static <T extends Poolable> Pools.Pool<T> build(Pools.Pool<T> paramPool, Factory<T> paramFactory) {
    return build(paramPool, paramFactory, emptyResetter());
  }
  
  private static <T> Pools.Pool<T> build(Pools.Pool<T> paramPool, Factory<T> paramFactory, Resetter<T> paramResetter) {
    return new FactoryPool<>(paramPool, paramFactory, paramResetter);
  }
  
  private static <T> Resetter<T> emptyResetter() {
    return (Resetter)EMPTY_RESETTER;
  }
  
  public static <T extends Poolable> Pools.Pool<T> simple(int paramInt, Factory<T> paramFactory) {
    return build((Pools.Pool<T>)new Pools.SimplePool(paramInt), paramFactory);
  }
  
  public static <T extends Poolable> Pools.Pool<T> threadSafe(int paramInt, Factory<T> paramFactory) {
    return build((Pools.Pool<T>)new Pools.SynchronizedPool(paramInt), paramFactory);
  }
  
  public static <T> Pools.Pool<List<T>> threadSafeList() {
    return threadSafeList(20);
  }
  
  public static <T> Pools.Pool<List<T>> threadSafeList(int paramInt) {
    return build((Pools.Pool<List<T>>)new Pools.SynchronizedPool(paramInt), (Factory)new Factory<List<List<T>>>() {
          public List<T> create() {
            return new ArrayList<>();
          }
        },  (Resetter)new Resetter<List<List<T>>>() {
          public void reset(List<T> param1List) {
            param1List.clear();
          }
        });
  }
  
  public static interface Factory<T> {
    T create();
  }
  
  private static final class FactoryPool<T> implements Pools.Pool<T> {
    private final FactoryPools.Factory<T> factory;
    
    private final Pools.Pool<T> pool;
    
    private final FactoryPools.Resetter<T> resetter;
    
    FactoryPool(Pools.Pool<T> param1Pool, FactoryPools.Factory<T> param1Factory, FactoryPools.Resetter<T> param1Resetter) {
      this.pool = param1Pool;
      this.factory = param1Factory;
      this.resetter = param1Resetter;
    }
    
    public T acquire() {
      Object object2 = this.pool.acquire();
      Object object1 = object2;
      if (object2 == null) {
        object2 = this.factory.create();
        object1 = object2;
        if (Log.isLoggable("FactoryPools", 2)) {
          Log.v("FactoryPools", "Created new " + object2.getClass());
          object1 = object2;
        } 
      } 
      if (object1 instanceof FactoryPools.Poolable)
        ((FactoryPools.Poolable)object1).getVerifier().setRecycled(false); 
      return (T)object1;
    }
    
    public boolean release(T param1T) {
      if (param1T instanceof FactoryPools.Poolable)
        ((FactoryPools.Poolable)param1T).getVerifier().setRecycled(true); 
      this.resetter.reset(param1T);
      return this.pool.release(param1T);
    }
  }
  
  public static interface Poolable {
    StateVerifier getVerifier();
  }
  
  public static interface Resetter<T> {
    void reset(T param1T);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glid\\util\pool\FactoryPools.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */