package androidx.recyclerview.widget;

import android.os.Handler;
import android.os.Looper;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;

public class AsyncListDiffer<T> {
  private static final Executor sMainThreadExecutor = new MainThreadExecutor();
  
  final AsyncDifferConfig<T> mConfig;
  
  private List<T> mList;
  
  private final List<ListListener<T>> mListeners = new CopyOnWriteArrayList<>();
  
  Executor mMainThreadExecutor;
  
  int mMaxScheduledGeneration;
  
  private List<T> mReadOnlyList = Collections.emptyList();
  
  private final ListUpdateCallback mUpdateCallback;
  
  public AsyncListDiffer(ListUpdateCallback paramListUpdateCallback, AsyncDifferConfig<T> paramAsyncDifferConfig) {
    this.mUpdateCallback = paramListUpdateCallback;
    this.mConfig = paramAsyncDifferConfig;
    if (paramAsyncDifferConfig.getMainThreadExecutor() != null) {
      this.mMainThreadExecutor = paramAsyncDifferConfig.getMainThreadExecutor();
    } else {
      this.mMainThreadExecutor = sMainThreadExecutor;
    } 
  }
  
  public AsyncListDiffer(RecyclerView.Adapter paramAdapter, DiffUtil.ItemCallback<T> paramItemCallback) {
    this(new AdapterListUpdateCallback(paramAdapter), (new AsyncDifferConfig.Builder<>(paramItemCallback)).build());
  }
  
  private void onCurrentListChanged(List<T> paramList, Runnable paramRunnable) {
    Iterator<ListListener<T>> iterator = this.mListeners.iterator();
    while (iterator.hasNext())
      ((ListListener<T>)iterator.next()).onCurrentListChanged(paramList, this.mReadOnlyList); 
    if (paramRunnable != null)
      paramRunnable.run(); 
  }
  
  public void addListListener(ListListener<T> paramListListener) {
    this.mListeners.add(paramListListener);
  }
  
  public List<T> getCurrentList() {
    return this.mReadOnlyList;
  }
  
  void latchList(List<T> paramList, DiffUtil.DiffResult paramDiffResult, Runnable paramRunnable) {
    List<T> list = this.mReadOnlyList;
    this.mList = paramList;
    this.mReadOnlyList = Collections.unmodifiableList(paramList);
    paramDiffResult.dispatchUpdatesTo(this.mUpdateCallback);
    onCurrentListChanged(list, paramRunnable);
  }
  
  public void removeListListener(ListListener<T> paramListListener) {
    this.mListeners.remove(paramListListener);
  }
  
  public void submitList(List<T> paramList) {
    submitList(paramList, null);
  }
  
  public void submitList(final List<T> newList, final Runnable commitCallback) {
    final int runGeneration = this.mMaxScheduledGeneration + 1;
    this.mMaxScheduledGeneration = i;
    final List<T> oldList = this.mList;
    if (newList == list1) {
      if (commitCallback != null)
        commitCallback.run(); 
      return;
    } 
    List<T> list2 = this.mReadOnlyList;
    if (newList == null) {
      i = list1.size();
      this.mList = null;
      this.mReadOnlyList = Collections.emptyList();
      this.mUpdateCallback.onRemoved(0, i);
      onCurrentListChanged(list2, commitCallback);
      return;
    } 
    if (list1 == null) {
      this.mList = newList;
      this.mReadOnlyList = Collections.unmodifiableList(newList);
      this.mUpdateCallback.onInserted(0, newList.size());
      onCurrentListChanged(list2, commitCallback);
      return;
    } 
    list1 = this.mList;
    this.mConfig.getBackgroundThreadExecutor().execute(new Runnable() {
          final AsyncListDiffer this$0;
          
          final Runnable val$commitCallback;
          
          final List val$newList;
          
          final List val$oldList;
          
          final int val$runGeneration;
          
          public void run() {
            final DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                  final AsyncListDiffer.null this$1;
                  
                  public boolean areContentsTheSame(int param2Int1, int param2Int2) {
                    Object object2 = oldList.get(param2Int1);
                    Object object1 = newList.get(param2Int2);
                    if (object2 != null && object1 != null)
                      return AsyncListDiffer.this.mConfig.getDiffCallback().areContentsTheSame(object2, object1); 
                    if (object2 == null && object1 == null)
                      return true; 
                    throw new AssertionError();
                  }
                  
                  public boolean areItemsTheSame(int param2Int1, int param2Int2) {
                    boolean bool;
                    Object object1 = oldList.get(param2Int1);
                    Object object2 = newList.get(param2Int2);
                    if (object1 != null && object2 != null)
                      return AsyncListDiffer.this.mConfig.getDiffCallback().areItemsTheSame(object1, object2); 
                    if (object1 == null && object2 == null) {
                      bool = true;
                    } else {
                      bool = false;
                    } 
                    return bool;
                  }
                  
                  public Object getChangePayload(int param2Int1, int param2Int2) {
                    Object object2 = oldList.get(param2Int1);
                    Object object1 = newList.get(param2Int2);
                    if (object2 != null && object1 != null)
                      return AsyncListDiffer.this.mConfig.getDiffCallback().getChangePayload(object2, object1); 
                    throw new AssertionError();
                  }
                  
                  public int getNewListSize() {
                    return newList.size();
                  }
                  
                  public int getOldListSize() {
                    return oldList.size();
                  }
                });
            AsyncListDiffer.this.mMainThreadExecutor.execute(new Runnable() {
                  final AsyncListDiffer.null this$1;
                  
                  final DiffUtil.DiffResult val$result;
                  
                  public void run() {
                    if (AsyncListDiffer.this.mMaxScheduledGeneration == runGeneration)
                      AsyncListDiffer.this.latchList(newList, result, commitCallback); 
                  }
                });
          }
        });
  }
  
  public static interface ListListener<T> {
    void onCurrentListChanged(List<T> param1List1, List<T> param1List2);
  }
  
  private static class MainThreadExecutor implements Executor {
    final Handler mHandler = new Handler(Looper.getMainLooper());
    
    public void execute(Runnable param1Runnable) {
      this.mHandler.post(param1Runnable);
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\recyclerview\widget\AsyncListDiffer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */