package com.bumptech.glide.load.engine.cache;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

final class DiskCacheWriteLocker {
  private final Map<String, WriteLock> locks = new HashMap<>();
  
  private final WriteLockPool writeLockPool = new WriteLockPool();
  
  void acquire(String paramString) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield locks : Ljava/util/Map;
    //   6: aload_1
    //   7: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   12: checkcast com/bumptech/glide/load/engine/cache/DiskCacheWriteLocker$WriteLock
    //   15: astore_3
    //   16: aload_3
    //   17: astore_2
    //   18: aload_3
    //   19: ifnonnull -> 42
    //   22: aload_0
    //   23: getfield writeLockPool : Lcom/bumptech/glide/load/engine/cache/DiskCacheWriteLocker$WriteLockPool;
    //   26: invokevirtual obtain : ()Lcom/bumptech/glide/load/engine/cache/DiskCacheWriteLocker$WriteLock;
    //   29: astore_2
    //   30: aload_0
    //   31: getfield locks : Ljava/util/Map;
    //   34: aload_1
    //   35: aload_2
    //   36: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   41: pop
    //   42: aload_2
    //   43: aload_2
    //   44: getfield interestedThreads : I
    //   47: iconst_1
    //   48: iadd
    //   49: putfield interestedThreads : I
    //   52: aload_0
    //   53: monitorexit
    //   54: aload_2
    //   55: getfield lock : Ljava/util/concurrent/locks/Lock;
    //   58: invokeinterface lock : ()V
    //   63: return
    //   64: astore_1
    //   65: aload_0
    //   66: monitorexit
    //   67: aload_1
    //   68: athrow
    // Exception table:
    //   from	to	target	type
    //   2	16	64	finally
    //   22	42	64	finally
    //   42	54	64	finally
    //   65	67	64	finally
  }
  
  void release(String paramString) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield locks : Ljava/util/Map;
    //   6: aload_1
    //   7: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   12: invokestatic checkNotNull : (Ljava/lang/Object;)Ljava/lang/Object;
    //   15: checkcast com/bumptech/glide/load/engine/cache/DiskCacheWriteLocker$WriteLock
    //   18: astore_2
    //   19: aload_2
    //   20: getfield interestedThreads : I
    //   23: iconst_1
    //   24: if_icmplt -> 145
    //   27: aload_2
    //   28: aload_2
    //   29: getfield interestedThreads : I
    //   32: iconst_1
    //   33: isub
    //   34: putfield interestedThreads : I
    //   37: aload_2
    //   38: getfield interestedThreads : I
    //   41: ifne -> 133
    //   44: aload_0
    //   45: getfield locks : Ljava/util/Map;
    //   48: aload_1
    //   49: invokeinterface remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   54: checkcast com/bumptech/glide/load/engine/cache/DiskCacheWriteLocker$WriteLock
    //   57: astore #5
    //   59: aload #5
    //   61: aload_2
    //   62: invokevirtual equals : (Ljava/lang/Object;)Z
    //   65: ifeq -> 80
    //   68: aload_0
    //   69: getfield writeLockPool : Lcom/bumptech/glide/load/engine/cache/DiskCacheWriteLocker$WriteLockPool;
    //   72: aload #5
    //   74: invokevirtual offer : (Lcom/bumptech/glide/load/engine/cache/DiskCacheWriteLocker$WriteLock;)V
    //   77: goto -> 133
    //   80: new java/lang/IllegalStateException
    //   83: astore_3
    //   84: new java/lang/StringBuilder
    //   87: astore #4
    //   89: aload #4
    //   91: invokespecial <init> : ()V
    //   94: aload_3
    //   95: aload #4
    //   97: ldc 'Removed the wrong lock, expected to remove: '
    //   99: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   102: aload_2
    //   103: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   106: ldc ', but actually removed: '
    //   108: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   111: aload #5
    //   113: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   116: ldc ', safeKey: '
    //   118: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   121: aload_1
    //   122: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   125: invokevirtual toString : ()Ljava/lang/String;
    //   128: invokespecial <init> : (Ljava/lang/String;)V
    //   131: aload_3
    //   132: athrow
    //   133: aload_0
    //   134: monitorexit
    //   135: aload_2
    //   136: getfield lock : Ljava/util/concurrent/locks/Lock;
    //   139: invokeinterface unlock : ()V
    //   144: return
    //   145: new java/lang/IllegalStateException
    //   148: astore_3
    //   149: new java/lang/StringBuilder
    //   152: astore #4
    //   154: aload #4
    //   156: invokespecial <init> : ()V
    //   159: aload_3
    //   160: aload #4
    //   162: ldc 'Cannot release a lock that is not held, safeKey: '
    //   164: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   167: aload_1
    //   168: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   171: ldc ', interestedThreads: '
    //   173: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   176: aload_2
    //   177: getfield interestedThreads : I
    //   180: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   183: invokevirtual toString : ()Ljava/lang/String;
    //   186: invokespecial <init> : (Ljava/lang/String;)V
    //   189: aload_3
    //   190: athrow
    //   191: astore_1
    //   192: aload_0
    //   193: monitorexit
    //   194: aload_1
    //   195: athrow
    // Exception table:
    //   from	to	target	type
    //   2	77	191	finally
    //   80	133	191	finally
    //   133	135	191	finally
    //   145	191	191	finally
    //   192	194	191	finally
  }
  
  private static class WriteLock {
    int interestedThreads;
    
    final Lock lock = new ReentrantLock();
  }
  
  private static class WriteLockPool {
    private static final int MAX_POOL_SIZE = 10;
    
    private final Queue<DiskCacheWriteLocker.WriteLock> pool = new ArrayDeque<>();
    
    DiskCacheWriteLocker.WriteLock obtain() {
      Queue<DiskCacheWriteLocker.WriteLock> queue;
      DiskCacheWriteLocker.WriteLock writeLock;
      synchronized (this.pool) {
        DiskCacheWriteLocker.WriteLock writeLock1 = this.pool.poll();
        writeLock = writeLock1;
        if (writeLock1 == null)
          writeLock = new DiskCacheWriteLocker.WriteLock(); 
        return writeLock;
      } 
    }
    
    void offer(DiskCacheWriteLocker.WriteLock param1WriteLock) {
      synchronized (this.pool) {
        if (this.pool.size() < 10)
          this.pool.offer(param1WriteLock); 
        return;
      } 
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\engine\cache\DiskCacheWriteLocker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */