package androidx.recyclerview.widget;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

public class SortedList<T> {
  private static final int CAPACITY_GROWTH = 10;
  
  private static final int DELETION = 2;
  
  private static final int INSERTION = 1;
  
  public static final int INVALID_POSITION = -1;
  
  private static final int LOOKUP = 4;
  
  private static final int MIN_CAPACITY = 10;
  
  private BatchedCallback mBatchedCallback;
  
  private Callback mCallback;
  
  T[] mData;
  
  private int mNewDataStart;
  
  private T[] mOldData;
  
  private int mOldDataSize;
  
  private int mOldDataStart;
  
  private int mSize;
  
  private final Class<T> mTClass;
  
  public SortedList(Class<T> paramClass, Callback<T> paramCallback) {
    this(paramClass, paramCallback, 10);
  }
  
  public SortedList(Class<T> paramClass, Callback<T> paramCallback, int paramInt) {
    this.mTClass = paramClass;
    this.mData = (T[])Array.newInstance(paramClass, paramInt);
    this.mCallback = paramCallback;
    this.mSize = 0;
  }
  
  private int add(T paramT, boolean paramBoolean) {
    int i;
    int j = findIndexOf(paramT, this.mData, 0, this.mSize, 1);
    if (j == -1) {
      i = 0;
    } else {
      i = j;
      if (j < this.mSize) {
        T t = this.mData[j];
        i = j;
        if (this.mCallback.areItemsTheSame(t, paramT)) {
          if (this.mCallback.areContentsTheSame(t, paramT)) {
            this.mData[j] = paramT;
            return j;
          } 
          this.mData[j] = paramT;
          Callback<T> callback = this.mCallback;
          callback.onChanged(j, 1, callback.getChangePayload(t, paramT));
          return j;
        } 
      } 
    } 
    addToData(i, paramT);
    if (paramBoolean)
      this.mCallback.onInserted(i, 1); 
    return i;
  }
  
  private void addAllInternal(T[] paramArrayOfT) {
    if (paramArrayOfT.length < 1)
      return; 
    int i = sortAndDedup(paramArrayOfT);
    if (this.mSize == 0) {
      this.mData = paramArrayOfT;
      this.mSize = i;
      this.mCallback.onInserted(0, i);
    } else {
      merge(paramArrayOfT, i);
    } 
  }
  
  private void addToData(int paramInt, T paramT) {
    int i = this.mSize;
    if (paramInt <= i) {
      T[] arrayOfT = this.mData;
      if (i == arrayOfT.length) {
        arrayOfT = (T[])Array.newInstance(this.mTClass, arrayOfT.length + 10);
        System.arraycopy(this.mData, 0, arrayOfT, 0, paramInt);
        arrayOfT[paramInt] = paramT;
        System.arraycopy(this.mData, paramInt, arrayOfT, paramInt + 1, this.mSize - paramInt);
        this.mData = arrayOfT;
      } else {
        System.arraycopy(arrayOfT, paramInt, arrayOfT, paramInt + 1, i - paramInt);
        this.mData[paramInt] = paramT;
      } 
      this.mSize++;
      return;
    } 
    throw new IndexOutOfBoundsException("cannot add item to " + paramInt + " because size is " + this.mSize);
  }
  
  private T[] copyArray(T[] paramArrayOfT) {
    Object[] arrayOfObject = (Object[])Array.newInstance(this.mTClass, paramArrayOfT.length);
    System.arraycopy(paramArrayOfT, 0, arrayOfObject, 0, paramArrayOfT.length);
    return (T[])arrayOfObject;
  }
  
  private int findIndexOf(T paramT, T[] paramArrayOfT, int paramInt1, int paramInt2, int paramInt3) {
    while (true) {
      int i = -1;
      if (paramInt1 < paramInt2) {
        i = (paramInt1 + paramInt2) / 2;
        T t = paramArrayOfT[i];
        int j = this.mCallback.compare(t, paramT);
        if (j < 0) {
          paramInt1 = i + 1;
          continue;
        } 
        if (j == 0) {
          if (this.mCallback.areItemsTheSame(t, paramT))
            return i; 
          paramInt1 = linearEqualitySearch(paramT, i, paramInt1, paramInt2);
          if (paramInt3 == 1) {
            if (paramInt1 == -1)
              paramInt1 = i; 
            return paramInt1;
          } 
          return paramInt1;
        } 
        paramInt2 = i;
        continue;
      } 
      paramInt2 = i;
      if (paramInt3 == 1)
        paramInt2 = paramInt1; 
      return paramInt2;
    } 
  }
  
  private int findSameItem(T paramT, T[] paramArrayOfT, int paramInt1, int paramInt2) {
    while (paramInt1 < paramInt2) {
      if (this.mCallback.areItemsTheSame(paramArrayOfT[paramInt1], paramT))
        return paramInt1; 
      paramInt1++;
    } 
    return -1;
  }
  
  private int linearEqualitySearch(T paramT, int paramInt1, int paramInt2, int paramInt3) {
    for (int i = paramInt1 - 1; i >= paramInt2; i--) {
      T t = this.mData[i];
      if (this.mCallback.compare(t, paramT) != 0)
        break; 
      if (this.mCallback.areItemsTheSame(t, paramT))
        return i; 
    } 
    while (++paramInt1 < paramInt3) {
      T t = this.mData[paramInt1];
      if (this.mCallback.compare(t, paramT) != 0)
        break; 
      if (this.mCallback.areItemsTheSame(t, paramT))
        return paramInt1; 
      paramInt1++;
    } 
    return -1;
  }
  
  private void merge(T[] paramArrayOfT, int paramInt) {
    int j = this.mCallback instanceof BatchedCallback ^ true;
    if (j != 0)
      beginBatchedUpdates(); 
    this.mOldData = this.mData;
    this.mOldDataStart = 0;
    int i = this.mSize;
    this.mOldDataSize = i;
    this.mData = (T[])Array.newInstance(this.mTClass, i + paramInt + 10);
    this.mNewDataStart = 0;
    i = 0;
    while (true) {
      int k = this.mOldDataStart;
      int m = this.mOldDataSize;
      if (k < m || i < paramInt)
        if (k == m) {
          paramInt -= i;
          System.arraycopy(paramArrayOfT, i, this.mData, this.mNewDataStart, paramInt);
          i = this.mNewDataStart + paramInt;
          this.mNewDataStart = i;
          this.mSize += paramInt;
          this.mCallback.onInserted(i - paramInt, paramInt);
        } else if (i == paramInt) {
          paramInt = m - k;
          System.arraycopy(this.mOldData, k, this.mData, this.mNewDataStart, paramInt);
          this.mNewDataStart += paramInt;
        } else {
          T[] arrayOfT1;
          T t1 = this.mOldData[k];
          T t2 = paramArrayOfT[i];
          k = this.mCallback.compare(t1, t2);
          if (k > 0) {
            arrayOfT1 = this.mData;
            m = this.mNewDataStart;
            k = m + 1;
            this.mNewDataStart = k;
            arrayOfT1[m] = t2;
            this.mSize++;
            i++;
            this.mCallback.onInserted(k - 1, 1);
            continue;
          } 
          if (k == 0 && this.mCallback.areItemsTheSame(arrayOfT1, (T[])t2)) {
            T[] arrayOfT = this.mData;
            k = this.mNewDataStart;
            this.mNewDataStart = k + 1;
            arrayOfT[k] = t2;
            k = i + 1;
            this.mOldDataStart++;
            i = k;
            if (!this.mCallback.areContentsTheSame(arrayOfT1, (T[])t2)) {
              Callback<T[]> callback = this.mCallback;
              callback.onChanged(this.mNewDataStart - 1, 1, callback.getChangePayload(arrayOfT1, (T[])t2));
              i = k;
            } 
            continue;
          } 
          T[] arrayOfT2 = this.mData;
          k = this.mNewDataStart;
          this.mNewDataStart = k + 1;
          arrayOfT2[k] = (T)arrayOfT1;
          this.mOldDataStart++;
          continue;
        }  
      this.mOldData = null;
      if (j != 0)
        endBatchedUpdates(); 
      return;
    } 
  }
  
  private boolean remove(T paramT, boolean paramBoolean) {
    int i = findIndexOf(paramT, this.mData, 0, this.mSize, 2);
    if (i == -1)
      return false; 
    removeItemAtIndex(i, paramBoolean);
    return true;
  }
  
  private void removeItemAtIndex(int paramInt, boolean paramBoolean) {
    T[] arrayOfT = this.mData;
    System.arraycopy(arrayOfT, paramInt + 1, arrayOfT, paramInt, this.mSize - paramInt - 1);
    int i = this.mSize - 1;
    this.mSize = i;
    this.mData[i] = null;
    if (paramBoolean)
      this.mCallback.onRemoved(paramInt, 1); 
  }
  
  private void replaceAllInsert(T paramT) {
    T[] arrayOfT = this.mData;
    int i = this.mNewDataStart;
    arrayOfT[i] = paramT;
    this.mNewDataStart = ++i;
    this.mSize++;
    this.mCallback.onInserted(i - 1, 1);
  }
  
  private void replaceAllInternal(T[] paramArrayOfT) {
    int i = this.mCallback instanceof BatchedCallback ^ true;
    if (i != 0)
      beginBatchedUpdates(); 
    this.mOldDataStart = 0;
    this.mOldDataSize = this.mSize;
    this.mOldData = this.mData;
    this.mNewDataStart = 0;
    int j = sortAndDedup(paramArrayOfT);
    this.mData = (T[])Array.newInstance(this.mTClass, j);
    while (true) {
      int k = this.mNewDataStart;
      if (k < j || this.mOldDataStart < this.mOldDataSize) {
        int m = this.mOldDataStart;
        int n = this.mOldDataSize;
        if (m >= n) {
          m = this.mNewDataStart;
          j -= k;
          System.arraycopy(paramArrayOfT, m, this.mData, m, j);
          this.mNewDataStart += j;
          this.mSize += j;
          this.mCallback.onInserted(m, j);
        } else if (k >= j) {
          j = n - m;
          this.mSize -= j;
          this.mCallback.onRemoved(k, j);
        } else {
          T t1 = this.mOldData[m];
          T t2 = paramArrayOfT[k];
          k = this.mCallback.compare(t1, t2);
          if (k < 0) {
            replaceAllRemove();
            continue;
          } 
          if (k > 0) {
            replaceAllInsert(t2);
            continue;
          } 
          if (!this.mCallback.areItemsTheSame(t1, t2)) {
            replaceAllRemove();
            replaceAllInsert(t2);
            continue;
          } 
          T[] arrayOfT = this.mData;
          k = this.mNewDataStart;
          arrayOfT[k] = t2;
          this.mOldDataStart++;
          this.mNewDataStart = k + 1;
          if (!this.mCallback.areContentsTheSame(t1, t2)) {
            Callback<T> callback = this.mCallback;
            callback.onChanged(this.mNewDataStart - 1, 1, callback.getChangePayload(t1, t2));
          } 
          continue;
        } 
      } 
      this.mOldData = null;
      if (i != 0)
        endBatchedUpdates(); 
      return;
    } 
  }
  
  private void replaceAllRemove() {
    this.mSize--;
    this.mOldDataStart++;
    this.mCallback.onRemoved(this.mNewDataStart, 1);
  }
  
  private int sortAndDedup(T[] paramArrayOfT) {
    if (paramArrayOfT.length == 0)
      return 0; 
    Arrays.sort(paramArrayOfT, this.mCallback);
    byte b3 = 0;
    byte b1 = 1;
    for (byte b2 = 1; b2 < paramArrayOfT.length; b2++) {
      T t = paramArrayOfT[b2];
      if (this.mCallback.compare(paramArrayOfT[b3], t) == 0) {
        int i = findSameItem(t, paramArrayOfT, b3, b1);
        if (i != -1) {
          paramArrayOfT[i] = t;
        } else {
          if (b1 != b2)
            paramArrayOfT[b1] = t; 
          b1++;
        } 
      } else {
        if (b1 != b2)
          paramArrayOfT[b1] = t; 
        b3 = b1;
        b1++;
      } 
    } 
    return b1;
  }
  
  private void throwIfInMutationOperation() {
    if (this.mOldData == null)
      return; 
    throw new IllegalStateException("Data cannot be mutated in the middle of a batch update operation such as addAll or replaceAll.");
  }
  
  public int add(T paramT) {
    throwIfInMutationOperation();
    return add(paramT, true);
  }
  
  public void addAll(Collection<T> paramCollection) {
    addAll(paramCollection.toArray((T[])Array.newInstance(this.mTClass, paramCollection.size())), true);
  }
  
  public void addAll(T... paramVarArgs) {
    addAll(paramVarArgs, false);
  }
  
  public void addAll(T[] paramArrayOfT, boolean paramBoolean) {
    throwIfInMutationOperation();
    if (paramArrayOfT.length == 0)
      return; 
    if (paramBoolean) {
      addAllInternal(paramArrayOfT);
    } else {
      addAllInternal(copyArray(paramArrayOfT));
    } 
  }
  
  public void beginBatchedUpdates() {
    throwIfInMutationOperation();
    if (this.mCallback instanceof BatchedCallback)
      return; 
    if (this.mBatchedCallback == null)
      this.mBatchedCallback = new BatchedCallback(this.mCallback); 
    this.mCallback = this.mBatchedCallback;
  }
  
  public void clear() {
    throwIfInMutationOperation();
    if (this.mSize == 0)
      return; 
    int i = this.mSize;
    Arrays.fill((Object[])this.mData, 0, i, (Object)null);
    this.mSize = 0;
    this.mCallback.onRemoved(0, i);
  }
  
  public void endBatchedUpdates() {
    throwIfInMutationOperation();
    Callback callback = this.mCallback;
    if (callback instanceof BatchedCallback)
      ((BatchedCallback)callback).dispatchLastEvent(); 
    callback = this.mCallback;
    BatchedCallback batchedCallback = this.mBatchedCallback;
    if (callback == batchedCallback)
      this.mCallback = batchedCallback.mWrappedCallback; 
  }
  
  public T get(int paramInt) throws IndexOutOfBoundsException {
    if (paramInt < this.mSize && paramInt >= 0) {
      T[] arrayOfT = this.mOldData;
      if (arrayOfT != null) {
        int i = this.mNewDataStart;
        if (paramInt >= i)
          return arrayOfT[paramInt - i + this.mOldDataStart]; 
      } 
      return this.mData[paramInt];
    } 
    throw new IndexOutOfBoundsException("Asked to get item at " + paramInt + " but size is " + this.mSize);
  }
  
  public int indexOf(T paramT) {
    if (this.mOldData != null) {
      int i = findIndexOf(paramT, this.mData, 0, this.mNewDataStart, 4);
      if (i != -1)
        return i; 
      i = findIndexOf(paramT, this.mOldData, this.mOldDataStart, this.mOldDataSize, 4);
      return (i != -1) ? (i - this.mOldDataStart + this.mNewDataStart) : -1;
    } 
    return findIndexOf(paramT, this.mData, 0, this.mSize, 4);
  }
  
  public void recalculatePositionOfItemAt(int paramInt) {
    throwIfInMutationOperation();
    T t = get(paramInt);
    removeItemAtIndex(paramInt, false);
    int i = add(t, false);
    if (paramInt != i)
      this.mCallback.onMoved(paramInt, i); 
  }
  
  public boolean remove(T paramT) {
    throwIfInMutationOperation();
    return remove(paramT, true);
  }
  
  public T removeItemAt(int paramInt) {
    throwIfInMutationOperation();
    T t = get(paramInt);
    removeItemAtIndex(paramInt, true);
    return t;
  }
  
  public void replaceAll(Collection<T> paramCollection) {
    replaceAll(paramCollection.toArray((T[])Array.newInstance(this.mTClass, paramCollection.size())), true);
  }
  
  public void replaceAll(T... paramVarArgs) {
    replaceAll(paramVarArgs, false);
  }
  
  public void replaceAll(T[] paramArrayOfT, boolean paramBoolean) {
    throwIfInMutationOperation();
    if (paramBoolean) {
      replaceAllInternal(paramArrayOfT);
    } else {
      replaceAllInternal(copyArray(paramArrayOfT));
    } 
  }
  
  public int size() {
    return this.mSize;
  }
  
  public void updateItemAt(int paramInt, T paramT) {
    throwIfInMutationOperation();
    T t = get(paramInt);
    if (t == paramT || !this.mCallback.areContentsTheSame(t, paramT)) {
      i = 1;
    } else {
      i = 0;
    } 
    if (t != paramT && this.mCallback.compare(t, paramT) == 0) {
      this.mData[paramInt] = paramT;
      if (i) {
        Callback<T> callback = this.mCallback;
        callback.onChanged(paramInt, 1, callback.getChangePayload(t, paramT));
      } 
      return;
    } 
    if (i) {
      Callback<T> callback = this.mCallback;
      callback.onChanged(paramInt, 1, callback.getChangePayload(t, paramT));
    } 
    removeItemAtIndex(paramInt, false);
    int i = add(paramT, false);
    if (paramInt != i)
      this.mCallback.onMoved(paramInt, i); 
  }
  
  public static class BatchedCallback<T2> extends Callback<T2> {
    private final BatchingListUpdateCallback mBatchingListUpdateCallback;
    
    final SortedList.Callback<T2> mWrappedCallback;
    
    public BatchedCallback(SortedList.Callback<T2> param1Callback) {
      this.mWrappedCallback = param1Callback;
      this.mBatchingListUpdateCallback = new BatchingListUpdateCallback(param1Callback);
    }
    
    public boolean areContentsTheSame(T2 param1T21, T2 param1T22) {
      return this.mWrappedCallback.areContentsTheSame(param1T21, param1T22);
    }
    
    public boolean areItemsTheSame(T2 param1T21, T2 param1T22) {
      return this.mWrappedCallback.areItemsTheSame(param1T21, param1T22);
    }
    
    public int compare(T2 param1T21, T2 param1T22) {
      return this.mWrappedCallback.compare(param1T21, param1T22);
    }
    
    public void dispatchLastEvent() {
      this.mBatchingListUpdateCallback.dispatchLastEvent();
    }
    
    public Object getChangePayload(T2 param1T21, T2 param1T22) {
      return this.mWrappedCallback.getChangePayload(param1T21, param1T22);
    }
    
    public void onChanged(int param1Int1, int param1Int2) {
      this.mBatchingListUpdateCallback.onChanged(param1Int1, param1Int2, null);
    }
    
    public void onChanged(int param1Int1, int param1Int2, Object param1Object) {
      this.mBatchingListUpdateCallback.onChanged(param1Int1, param1Int2, param1Object);
    }
    
    public void onInserted(int param1Int1, int param1Int2) {
      this.mBatchingListUpdateCallback.onInserted(param1Int1, param1Int2);
    }
    
    public void onMoved(int param1Int1, int param1Int2) {
      this.mBatchingListUpdateCallback.onMoved(param1Int1, param1Int2);
    }
    
    public void onRemoved(int param1Int1, int param1Int2) {
      this.mBatchingListUpdateCallback.onRemoved(param1Int1, param1Int2);
    }
  }
  
  public static abstract class Callback<T2> implements Comparator<T2>, ListUpdateCallback {
    public abstract boolean areContentsTheSame(T2 param1T21, T2 param1T22);
    
    public abstract boolean areItemsTheSame(T2 param1T21, T2 param1T22);
    
    public abstract int compare(T2 param1T21, T2 param1T22);
    
    public Object getChangePayload(T2 param1T21, T2 param1T22) {
      return null;
    }
    
    public abstract void onChanged(int param1Int1, int param1Int2);
    
    public void onChanged(int param1Int1, int param1Int2, Object param1Object) {
      onChanged(param1Int1, param1Int2);
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\recyclerview\widget\SortedList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */