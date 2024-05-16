package androidx.recyclerview.widget;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

class MessageThreadUtil<T> implements ThreadUtil<T> {
  public ThreadUtil.BackgroundCallback<T> getBackgroundProxy(final ThreadUtil.BackgroundCallback<T> callback) {
    return new ThreadUtil.BackgroundCallback<T>() {
        static final int LOAD_TILE = 3;
        
        static final int RECYCLE_TILE = 4;
        
        static final int REFRESH = 1;
        
        static final int UPDATE_RANGE = 2;
        
        private Runnable mBackgroundRunnable = new Runnable() {
            final MessageThreadUtil.null this$1;
            
            public void run() {
              while (true) {
                TileList.Tile tile;
                MessageThreadUtil.SyncQueueItem syncQueueItem = MessageThreadUtil.null.this.mQueue.next();
                if (syncQueueItem == null) {
                  MessageThreadUtil.null.this.mBackgroundRunning.set(false);
                  return;
                } 
                switch (syncQueueItem.what) {
                  default:
                    Log.e("ThreadUtil", "Unsupported message, what=" + syncQueueItem.what);
                    continue;
                  case 4:
                    tile = (TileList.Tile)syncQueueItem.data;
                    callback.recycleTile(tile);
                    continue;
                  case 3:
                    callback.loadTile(((MessageThreadUtil.SyncQueueItem)tile).arg1, ((MessageThreadUtil.SyncQueueItem)tile).arg2);
                    continue;
                  case 2:
                    MessageThreadUtil.null.this.mQueue.removeMessages(2);
                    MessageThreadUtil.null.this.mQueue.removeMessages(3);
                    callback.updateRange(((MessageThreadUtil.SyncQueueItem)tile).arg1, ((MessageThreadUtil.SyncQueueItem)tile).arg2, ((MessageThreadUtil.SyncQueueItem)tile).arg3, ((MessageThreadUtil.SyncQueueItem)tile).arg4, ((MessageThreadUtil.SyncQueueItem)tile).arg5);
                    continue;
                  case 1:
                    break;
                } 
                MessageThreadUtil.null.this.mQueue.removeMessages(1);
                callback.refresh(((MessageThreadUtil.SyncQueueItem)tile).arg1);
              } 
            }
          };
        
        AtomicBoolean mBackgroundRunning = new AtomicBoolean(false);
        
        private final Executor mExecutor = AsyncTask.THREAD_POOL_EXECUTOR;
        
        final MessageThreadUtil.MessageQueue mQueue = new MessageThreadUtil.MessageQueue();
        
        final MessageThreadUtil this$0;
        
        final ThreadUtil.BackgroundCallback val$callback;
        
        private void maybeExecuteBackgroundRunnable() {
          if (this.mBackgroundRunning.compareAndSet(false, true))
            this.mExecutor.execute(this.mBackgroundRunnable); 
        }
        
        private void sendMessage(MessageThreadUtil.SyncQueueItem param1SyncQueueItem) {
          this.mQueue.sendMessage(param1SyncQueueItem);
          maybeExecuteBackgroundRunnable();
        }
        
        private void sendMessageAtFrontOfQueue(MessageThreadUtil.SyncQueueItem param1SyncQueueItem) {
          this.mQueue.sendMessageAtFrontOfQueue(param1SyncQueueItem);
          maybeExecuteBackgroundRunnable();
        }
        
        public void loadTile(int param1Int1, int param1Int2) {
          sendMessage(MessageThreadUtil.SyncQueueItem.obtainMessage(3, param1Int1, param1Int2));
        }
        
        public void recycleTile(TileList.Tile<T> param1Tile) {
          sendMessage(MessageThreadUtil.SyncQueueItem.obtainMessage(4, 0, param1Tile));
        }
        
        public void refresh(int param1Int) {
          sendMessageAtFrontOfQueue(MessageThreadUtil.SyncQueueItem.obtainMessage(1, param1Int, (Object)null));
        }
        
        public void updateRange(int param1Int1, int param1Int2, int param1Int3, int param1Int4, int param1Int5) {
          sendMessageAtFrontOfQueue(MessageThreadUtil.SyncQueueItem.obtainMessage(2, param1Int1, param1Int2, param1Int3, param1Int4, param1Int5, null));
        }
      };
  }
  
  public ThreadUtil.MainThreadCallback<T> getMainThreadProxy(final ThreadUtil.MainThreadCallback<T> callback) {
    return new ThreadUtil.MainThreadCallback<T>() {
        static final int ADD_TILE = 2;
        
        static final int REMOVE_TILE = 3;
        
        static final int UPDATE_ITEM_COUNT = 1;
        
        private final Handler mMainThreadHandler = new Handler(Looper.getMainLooper());
        
        private Runnable mMainThreadRunnable = new Runnable() {
            final MessageThreadUtil.null this$1;
            
            public void run() {
              for (MessageThreadUtil.SyncQueueItem syncQueueItem = MessageThreadUtil.null.this.mQueue.next(); syncQueueItem != null; syncQueueItem = MessageThreadUtil.null.this.mQueue.next()) {
                TileList.Tile tile;
                switch (syncQueueItem.what) {
                  default:
                    Log.e("ThreadUtil", "Unsupported message, what=" + syncQueueItem.what);
                    break;
                  case 3:
                    callback.removeTile(syncQueueItem.arg1, syncQueueItem.arg2);
                    break;
                  case 2:
                    tile = (TileList.Tile)syncQueueItem.data;
                    callback.addTile(syncQueueItem.arg1, tile);
                    break;
                  case 1:
                    callback.updateItemCount(syncQueueItem.arg1, syncQueueItem.arg2);
                    break;
                } 
              } 
            }
          };
        
        final MessageThreadUtil.MessageQueue mQueue = new MessageThreadUtil.MessageQueue();
        
        final MessageThreadUtil this$0;
        
        final ThreadUtil.MainThreadCallback val$callback;
        
        private void sendMessage(MessageThreadUtil.SyncQueueItem param1SyncQueueItem) {
          this.mQueue.sendMessage(param1SyncQueueItem);
          this.mMainThreadHandler.post(this.mMainThreadRunnable);
        }
        
        public void addTile(int param1Int, TileList.Tile<T> param1Tile) {
          sendMessage(MessageThreadUtil.SyncQueueItem.obtainMessage(2, param1Int, param1Tile));
        }
        
        public void removeTile(int param1Int1, int param1Int2) {
          sendMessage(MessageThreadUtil.SyncQueueItem.obtainMessage(3, param1Int1, param1Int2));
        }
        
        public void updateItemCount(int param1Int1, int param1Int2) {
          sendMessage(MessageThreadUtil.SyncQueueItem.obtainMessage(1, param1Int1, param1Int2));
        }
      };
  }
  
  static class MessageQueue {
    private MessageThreadUtil.SyncQueueItem mRoot;
    
    MessageThreadUtil.SyncQueueItem next() {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: getfield mRoot : Landroidx/recyclerview/widget/MessageThreadUtil$SyncQueueItem;
      //   6: astore_1
      //   7: aload_1
      //   8: ifnonnull -> 15
      //   11: aload_0
      //   12: monitorexit
      //   13: aconst_null
      //   14: areturn
      //   15: aload_0
      //   16: aload_1
      //   17: getfield next : Landroidx/recyclerview/widget/MessageThreadUtil$SyncQueueItem;
      //   20: putfield mRoot : Landroidx/recyclerview/widget/MessageThreadUtil$SyncQueueItem;
      //   23: aload_0
      //   24: monitorexit
      //   25: aload_1
      //   26: areturn
      //   27: astore_1
      //   28: aload_0
      //   29: monitorexit
      //   30: aload_1
      //   31: athrow
      // Exception table:
      //   from	to	target	type
      //   2	7	27	finally
      //   15	23	27	finally
    }
    
    void removeMessages(int param1Int) {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: getfield mRoot : Landroidx/recyclerview/widget/MessageThreadUtil$SyncQueueItem;
      //   6: astore_2
      //   7: aload_2
      //   8: ifnull -> 39
      //   11: aload_2
      //   12: getfield what : I
      //   15: iload_1
      //   16: if_icmpne -> 39
      //   19: aload_0
      //   20: getfield mRoot : Landroidx/recyclerview/widget/MessageThreadUtil$SyncQueueItem;
      //   23: astore_2
      //   24: aload_0
      //   25: aload_2
      //   26: getfield next : Landroidx/recyclerview/widget/MessageThreadUtil$SyncQueueItem;
      //   29: putfield mRoot : Landroidx/recyclerview/widget/MessageThreadUtil$SyncQueueItem;
      //   32: aload_2
      //   33: invokevirtual recycle : ()V
      //   36: goto -> 2
      //   39: aload_0
      //   40: getfield mRoot : Landroidx/recyclerview/widget/MessageThreadUtil$SyncQueueItem;
      //   43: astore_3
      //   44: aload_3
      //   45: ifnull -> 92
      //   48: aload_3
      //   49: getfield next : Landroidx/recyclerview/widget/MessageThreadUtil$SyncQueueItem;
      //   52: astore_2
      //   53: aload_2
      //   54: ifnull -> 92
      //   57: aload_2
      //   58: getfield next : Landroidx/recyclerview/widget/MessageThreadUtil$SyncQueueItem;
      //   61: astore #4
      //   63: aload_2
      //   64: getfield what : I
      //   67: iload_1
      //   68: if_icmpne -> 84
      //   71: aload_3
      //   72: aload #4
      //   74: putfield next : Landroidx/recyclerview/widget/MessageThreadUtil$SyncQueueItem;
      //   77: aload_2
      //   78: invokevirtual recycle : ()V
      //   81: goto -> 86
      //   84: aload_2
      //   85: astore_3
      //   86: aload #4
      //   88: astore_2
      //   89: goto -> 53
      //   92: aload_0
      //   93: monitorexit
      //   94: return
      //   95: astore_2
      //   96: aload_0
      //   97: monitorexit
      //   98: aload_2
      //   99: athrow
      // Exception table:
      //   from	to	target	type
      //   2	7	95	finally
      //   11	36	95	finally
      //   39	44	95	finally
      //   48	53	95	finally
      //   57	81	95	finally
    }
    
    void sendMessage(MessageThreadUtil.SyncQueueItem param1SyncQueueItem) {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: getfield mRoot : Landroidx/recyclerview/widget/MessageThreadUtil$SyncQueueItem;
      //   6: astore_2
      //   7: aload_2
      //   8: ifnonnull -> 19
      //   11: aload_0
      //   12: aload_1
      //   13: putfield mRoot : Landroidx/recyclerview/widget/MessageThreadUtil$SyncQueueItem;
      //   16: aload_0
      //   17: monitorexit
      //   18: return
      //   19: aload_2
      //   20: getfield next : Landroidx/recyclerview/widget/MessageThreadUtil$SyncQueueItem;
      //   23: ifnull -> 34
      //   26: aload_2
      //   27: getfield next : Landroidx/recyclerview/widget/MessageThreadUtil$SyncQueueItem;
      //   30: astore_2
      //   31: goto -> 19
      //   34: aload_2
      //   35: aload_1
      //   36: putfield next : Landroidx/recyclerview/widget/MessageThreadUtil$SyncQueueItem;
      //   39: aload_0
      //   40: monitorexit
      //   41: return
      //   42: astore_1
      //   43: aload_0
      //   44: monitorexit
      //   45: aload_1
      //   46: athrow
      // Exception table:
      //   from	to	target	type
      //   2	7	42	finally
      //   11	16	42	finally
      //   19	31	42	finally
      //   34	39	42	finally
    }
    
    void sendMessageAtFrontOfQueue(MessageThreadUtil.SyncQueueItem param1SyncQueueItem) {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_1
      //   3: aload_0
      //   4: getfield mRoot : Landroidx/recyclerview/widget/MessageThreadUtil$SyncQueueItem;
      //   7: putfield next : Landroidx/recyclerview/widget/MessageThreadUtil$SyncQueueItem;
      //   10: aload_0
      //   11: aload_1
      //   12: putfield mRoot : Landroidx/recyclerview/widget/MessageThreadUtil$SyncQueueItem;
      //   15: aload_0
      //   16: monitorexit
      //   17: return
      //   18: astore_1
      //   19: aload_0
      //   20: monitorexit
      //   21: aload_1
      //   22: athrow
      // Exception table:
      //   from	to	target	type
      //   2	15	18	finally
    }
  }
  
  static class SyncQueueItem {
    private static SyncQueueItem sPool;
    
    private static final Object sPoolLock = new Object();
    
    public int arg1;
    
    public int arg2;
    
    public int arg3;
    
    public int arg4;
    
    public int arg5;
    
    public Object data;
    
    SyncQueueItem next;
    
    public int what;
    
    static SyncQueueItem obtainMessage(int param1Int1, int param1Int2, int param1Int3) {
      return obtainMessage(param1Int1, param1Int2, param1Int3, 0, 0, 0, null);
    }
    
    static SyncQueueItem obtainMessage(int param1Int1, int param1Int2, int param1Int3, int param1Int4, int param1Int5, int param1Int6, Object param1Object) {
      synchronized (sPoolLock) {
        SyncQueueItem syncQueueItem = sPool;
        if (syncQueueItem == null) {
          syncQueueItem = new SyncQueueItem();
          this();
        } else {
          sPool = syncQueueItem.next;
          syncQueueItem.next = null;
        } 
        syncQueueItem.what = param1Int1;
        syncQueueItem.arg1 = param1Int2;
        syncQueueItem.arg2 = param1Int3;
        syncQueueItem.arg3 = param1Int4;
        syncQueueItem.arg4 = param1Int5;
        syncQueueItem.arg5 = param1Int6;
        syncQueueItem.data = param1Object;
        return syncQueueItem;
      } 
    }
    
    static SyncQueueItem obtainMessage(int param1Int1, int param1Int2, Object param1Object) {
      return obtainMessage(param1Int1, param1Int2, 0, 0, 0, 0, param1Object);
    }
    
    void recycle() {
      this.next = null;
      this.arg5 = 0;
      this.arg4 = 0;
      this.arg3 = 0;
      this.arg2 = 0;
      this.arg1 = 0;
      this.what = 0;
      this.data = null;
      synchronized (sPoolLock) {
        SyncQueueItem syncQueueItem = sPool;
        if (syncQueueItem != null)
          this.next = syncQueueItem; 
        sPool = this;
        return;
      } 
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\recyclerview\widget\MessageThreadUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */