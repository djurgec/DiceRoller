package androidx.recyclerview.widget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DiffUtil {
  private static final Comparator<Snake> SNAKE_COMPARATOR = new Comparator<Snake>() {
      public int compare(DiffUtil.Snake param1Snake1, DiffUtil.Snake param1Snake2) {
        int i = param1Snake1.x - param1Snake2.x;
        if (i == 0)
          i = param1Snake1.y - param1Snake2.y; 
        return i;
      }
    };
  
  public static DiffResult calculateDiff(Callback paramCallback) {
    return calculateDiff(paramCallback, true);
  }
  
  public static DiffResult calculateDiff(Callback paramCallback, boolean paramBoolean) {
    int j = paramCallback.getOldListSize();
    int i = paramCallback.getNewListSize();
    ArrayList<Snake> arrayList = new ArrayList();
    ArrayList<Range> arrayList1 = new ArrayList();
    arrayList1.add(new Range(0, j, 0, i));
    i = j + i + Math.abs(j - i);
    int[] arrayOfInt1 = new int[i * 2];
    int[] arrayOfInt2 = new int[i * 2];
    ArrayList<Range> arrayList2 = new ArrayList();
    while (!arrayList1.isEmpty()) {
      Range range = arrayList1.remove(arrayList1.size() - 1);
      Snake snake = diffPartial(paramCallback, range.oldListStart, range.oldListEnd, range.newListStart, range.newListEnd, arrayOfInt1, arrayOfInt2, i);
      if (snake != null) {
        Range range1;
        if (snake.size > 0)
          arrayList.add(snake); 
        snake.x += range.oldListStart;
        snake.y += range.newListStart;
        if (arrayList2.isEmpty()) {
          range1 = new Range();
        } else {
          range1 = arrayList2.remove(arrayList2.size() - 1);
        } 
        range1.oldListStart = range.oldListStart;
        range1.newListStart = range.newListStart;
        if (snake.reverse) {
          range1.oldListEnd = snake.x;
          range1.newListEnd = snake.y;
        } else if (snake.removal) {
          range1.oldListEnd = snake.x - 1;
          range1.newListEnd = snake.y;
        } else {
          range1.oldListEnd = snake.x;
          range1.newListEnd = snake.y - 1;
        } 
        arrayList1.add(range1);
        if (snake.reverse) {
          if (snake.removal) {
            range.oldListStart = snake.x + snake.size + 1;
            range.newListStart = snake.y + snake.size;
          } else {
            range.oldListStart = snake.x + snake.size;
            range.newListStart = snake.y + snake.size + 1;
          } 
        } else {
          range.oldListStart = snake.x + snake.size;
          range.newListStart = snake.y + snake.size;
        } 
        arrayList1.add(range);
        continue;
      } 
      arrayList2.add(range);
    } 
    Collections.sort(arrayList, SNAKE_COMPARATOR);
    return new DiffResult(paramCallback, arrayList, arrayOfInt1, arrayOfInt2, paramBoolean);
  }
  
  private static Snake diffPartial(Callback paramCallback, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfint1, int[] paramArrayOfint2, int paramInt5) {
    boolean bool;
    int i = paramInt2 - paramInt1;
    int j = paramInt4 - paramInt3;
    if (paramInt2 - paramInt1 < 1 || paramInt4 - paramInt3 < 1)
      return null; 
    int m = i - j;
    int k = (i + j + 1) / 2;
    Arrays.fill(paramArrayOfint1, paramInt5 - k - 1, paramInt5 + k + 1, 0);
    Arrays.fill(paramArrayOfint2, paramInt5 - k - 1 + m, paramInt5 + k + 1 + m, i);
    if (m % 2 != 0) {
      bool = true;
    } else {
      bool = false;
    } 
    byte b = 0;
    for (paramInt4 = i; b <= k; paramInt4 = paramInt2) {
      Snake snake;
      for (i = -b; i <= b; i += 2) {
        boolean bool1;
        if (i == -b || (i != b && paramArrayOfint1[paramInt5 + i - 1] < paramArrayOfint1[paramInt5 + i + 1])) {
          paramInt2 = paramArrayOfint1[paramInt5 + i + 1];
          bool1 = false;
        } else {
          paramInt2 = paramArrayOfint1[paramInt5 + i - 1] + 1;
          bool1 = true;
        } 
        for (int n = paramInt2 - i; paramInt2 < paramInt4 && n < j && paramCallback.areItemsTheSame(paramInt1 + paramInt2, paramInt3 + n); n++)
          paramInt2++; 
        paramArrayOfint1[paramInt5 + i] = paramInt2;
        if (bool && i >= m - b + 1 && i <= m + b - 1 && paramArrayOfint1[paramInt5 + i] >= paramArrayOfint2[paramInt5 + i]) {
          snake = new Snake();
          snake.x = paramArrayOfint2[paramInt5 + i];
          snake.y = snake.x - i;
          snake.size = paramArrayOfint1[paramInt5 + i] - paramArrayOfint2[paramInt5 + i];
          snake.removal = bool1;
          snake.reverse = false;
          return snake;
        } 
      } 
      i = -b;
      paramInt2 = paramInt4;
      while (i <= b) {
        boolean bool1;
        int i1 = i + m;
        if (i1 == b + m || (i1 != -b + m && paramArrayOfint2[paramInt5 + i1 - 1] < paramArrayOfint2[paramInt5 + i1 + 1])) {
          paramInt4 = paramArrayOfint2[paramInt5 + i1 - 1];
          bool1 = false;
        } else {
          paramInt4 = paramArrayOfint2[paramInt5 + i1 + 1] - 1;
          bool1 = true;
        } 
        for (int n = paramInt4 - i1; paramInt4 > 0 && n > 0 && snake.areItemsTheSame(paramInt1 + paramInt4 - 1, paramInt3 + n - 1); n--)
          paramInt4--; 
        paramArrayOfint2[paramInt5 + i1] = paramInt4;
        if (!bool && i + m >= -b && i + m <= b && paramArrayOfint1[paramInt5 + i1] >= paramArrayOfint2[paramInt5 + i1]) {
          snake = new Snake();
          snake.x = paramArrayOfint2[paramInt5 + i1];
          snake.y = snake.x - i1;
          snake.size = paramArrayOfint1[paramInt5 + i1] - paramArrayOfint2[paramInt5 + i1];
          snake.removal = bool1;
          snake.reverse = true;
          return snake;
        } 
        i += 2;
      } 
      b++;
    } 
    throw new IllegalStateException("DiffUtil hit an unexpected case while trying to calculate the optimal path. Please make sure your data is not changing during the diff calculation.");
  }
  
  public static abstract class Callback {
    public abstract boolean areContentsTheSame(int param1Int1, int param1Int2);
    
    public abstract boolean areItemsTheSame(int param1Int1, int param1Int2);
    
    public Object getChangePayload(int param1Int1, int param1Int2) {
      return null;
    }
    
    public abstract int getNewListSize();
    
    public abstract int getOldListSize();
  }
  
  public static class DiffResult {
    private static final int FLAG_CHANGED = 2;
    
    private static final int FLAG_IGNORE = 16;
    
    private static final int FLAG_MASK = 31;
    
    private static final int FLAG_MOVED_CHANGED = 4;
    
    private static final int FLAG_MOVED_NOT_CHANGED = 8;
    
    private static final int FLAG_NOT_CHANGED = 1;
    
    private static final int FLAG_OFFSET = 5;
    
    public static final int NO_POSITION = -1;
    
    private final DiffUtil.Callback mCallback;
    
    private final boolean mDetectMoves;
    
    private final int[] mNewItemStatuses;
    
    private final int mNewListSize;
    
    private final int[] mOldItemStatuses;
    
    private final int mOldListSize;
    
    private final List<DiffUtil.Snake> mSnakes;
    
    DiffResult(DiffUtil.Callback param1Callback, List<DiffUtil.Snake> param1List, int[] param1ArrayOfint1, int[] param1ArrayOfint2, boolean param1Boolean) {
      this.mSnakes = param1List;
      this.mOldItemStatuses = param1ArrayOfint1;
      this.mNewItemStatuses = param1ArrayOfint2;
      Arrays.fill(param1ArrayOfint1, 0);
      Arrays.fill(param1ArrayOfint2, 0);
      this.mCallback = param1Callback;
      this.mOldListSize = param1Callback.getOldListSize();
      this.mNewListSize = param1Callback.getNewListSize();
      this.mDetectMoves = param1Boolean;
      addRootSnake();
      findMatchingItems();
    }
    
    private void addRootSnake() {
      DiffUtil.Snake snake;
      if (this.mSnakes.isEmpty()) {
        snake = null;
      } else {
        snake = this.mSnakes.get(0);
      } 
      if (snake == null || snake.x != 0 || snake.y != 0) {
        snake = new DiffUtil.Snake();
        snake.x = 0;
        snake.y = 0;
        snake.removal = false;
        snake.size = 0;
        snake.reverse = false;
        this.mSnakes.add(0, snake);
      } 
    }
    
    private void dispatchAdditions(List<DiffUtil.PostponedUpdate> param1List, ListUpdateCallback param1ListUpdateCallback, int param1Int1, int param1Int2, int param1Int3) {
      if (!this.mDetectMoves) {
        param1ListUpdateCallback.onInserted(param1Int1, param1Int2);
        return;
      } 
      while (--param1Int2 >= 0) {
        int j;
        int[] arrayOfInt = this.mNewItemStatuses;
        int i = arrayOfInt[param1Int3 + param1Int2] & 0x1F;
        switch (i) {
          default:
            throw new IllegalStateException("unknown flag for pos " + (param1Int3 + param1Int2) + " " + Long.toBinaryString(i));
          case 16:
            param1List.add(new DiffUtil.PostponedUpdate(param1Int3 + param1Int2, param1Int1, false));
            break;
          case 4:
          case 8:
            j = arrayOfInt[param1Int3 + param1Int2] >> 5;
            param1ListUpdateCallback.onMoved((removePostponedUpdate(param1List, j, true)).currentPos, param1Int1);
            if (i == 4)
              param1ListUpdateCallback.onChanged(param1Int1, 1, this.mCallback.getChangePayload(j, param1Int3 + param1Int2)); 
            break;
          case 0:
            param1ListUpdateCallback.onInserted(param1Int1, 1);
            for (DiffUtil.PostponedUpdate postponedUpdate : param1List)
              postponedUpdate.currentPos++; 
            break;
        } 
        param1Int2--;
      } 
    }
    
    private void dispatchRemovals(List<DiffUtil.PostponedUpdate> param1List, ListUpdateCallback param1ListUpdateCallback, int param1Int1, int param1Int2, int param1Int3) {
      if (!this.mDetectMoves) {
        param1ListUpdateCallback.onRemoved(param1Int1, param1Int2);
        return;
      } 
      while (--param1Int2 >= 0) {
        int i;
        DiffUtil.PostponedUpdate postponedUpdate;
        int[] arrayOfInt = this.mOldItemStatuses;
        int j = arrayOfInt[param1Int3 + param1Int2] & 0x1F;
        switch (j) {
          default:
            throw new IllegalStateException("unknown flag for pos " + (param1Int3 + param1Int2) + " " + Long.toBinaryString(j));
          case 16:
            param1List.add(new DiffUtil.PostponedUpdate(param1Int3 + param1Int2, param1Int1 + param1Int2, true));
            break;
          case 4:
          case 8:
            i = arrayOfInt[param1Int3 + param1Int2] >> 5;
            postponedUpdate = removePostponedUpdate(param1List, i, false);
            param1ListUpdateCallback.onMoved(param1Int1 + param1Int2, postponedUpdate.currentPos - 1);
            if (j == 4)
              param1ListUpdateCallback.onChanged(postponedUpdate.currentPos - 1, 1, this.mCallback.getChangePayload(param1Int3 + param1Int2, i)); 
            break;
          case 0:
            param1ListUpdateCallback.onRemoved(param1Int1 + param1Int2, 1);
            for (DiffUtil.PostponedUpdate postponedUpdate1 : param1List)
              postponedUpdate1.currentPos--; 
            break;
        } 
        param1Int2--;
      } 
    }
    
    private void findAddition(int param1Int1, int param1Int2, int param1Int3) {
      if (this.mOldItemStatuses[param1Int1 - 1] != 0)
        return; 
      findMatchingItem(param1Int1, param1Int2, param1Int3, false);
    }
    
    private boolean findMatchingItem(int param1Int1, int param1Int2, int param1Int3, boolean param1Boolean) {
      int i;
      if (param1Boolean) {
        i = param1Int2 - 1;
        int k = param1Int1;
        int m = param1Int2 - 1;
        param1Int2 = k;
        k = m;
      } else {
        i = param1Int1 - 1;
        int m = param1Int1 - 1;
        int k = param1Int2;
        param1Int2 = m;
      } 
      int j = param1Int2;
      while (param1Int3 >= 0) {
        DiffUtil.Snake snake = this.mSnakes.get(param1Int3);
        int n = snake.x;
        int i1 = snake.size;
        int i2 = snake.y;
        int m = snake.size;
        param1Int2 = 8;
        if (param1Boolean) {
          for (k = j - 1; k >= n + i1; k--) {
            if (this.mCallback.areItemsTheSame(k, i)) {
              if (!this.mCallback.areContentsTheSame(k, i))
                param1Int2 = 4; 
              this.mNewItemStatuses[i] = k << 5 | 0x10;
              this.mOldItemStatuses[k] = i << 5 | param1Int2;
              return true;
            } 
          } 
        } else {
          while (--k >= i2 + m) {
            if (this.mCallback.areItemsTheSame(i, k)) {
              if (!this.mCallback.areContentsTheSame(i, k))
                param1Int2 = 4; 
              this.mOldItemStatuses[param1Int1 - 1] = k << 5 | 0x10;
              this.mNewItemStatuses[k] = param1Int1 - 1 << 5 | param1Int2;
              return true;
            } 
            k--;
          } 
        } 
        j = snake.x;
        int k = snake.y;
        param1Int3--;
      } 
      return false;
    }
    
    private void findMatchingItems() {
      int j = this.mOldListSize;
      int i = this.mNewListSize;
      for (int k = this.mSnakes.size() - 1; k >= 0; k--) {
        DiffUtil.Snake snake = this.mSnakes.get(k);
        int i1 = snake.x;
        int i2 = snake.size;
        int n = snake.y;
        int m = snake.size;
        if (this.mDetectMoves) {
          int i3;
          while (true) {
            i3 = i;
            if (j > i1 + i2) {
              findAddition(j, i, k);
              j--;
              continue;
            } 
            break;
          } 
          while (i3 > n + m) {
            findRemoval(j, i3, k);
            i3--;
          } 
        } 
        for (i = 0; i < snake.size; i++) {
          int i3 = snake.x + i;
          m = snake.y + i;
          if (this.mCallback.areContentsTheSame(i3, m)) {
            j = 1;
          } else {
            j = 2;
          } 
          this.mOldItemStatuses[i3] = m << 5 | j;
          this.mNewItemStatuses[m] = i3 << 5 | j;
        } 
        j = snake.x;
        i = snake.y;
      } 
    }
    
    private void findRemoval(int param1Int1, int param1Int2, int param1Int3) {
      if (this.mNewItemStatuses[param1Int2 - 1] != 0)
        return; 
      findMatchingItem(param1Int1, param1Int2, param1Int3, true);
    }
    
    private static DiffUtil.PostponedUpdate removePostponedUpdate(List<DiffUtil.PostponedUpdate> param1List, int param1Int, boolean param1Boolean) {
      for (int i = param1List.size() - 1; i >= 0; i--) {
        DiffUtil.PostponedUpdate postponedUpdate = param1List.get(i);
        if (postponedUpdate.posInOwnerList == param1Int && postponedUpdate.removal == param1Boolean) {
          param1List.remove(i);
          for (param1Int = i; param1Int < param1List.size(); param1Int++) {
            DiffUtil.PostponedUpdate postponedUpdate1 = param1List.get(param1Int);
            int j = postponedUpdate1.currentPos;
            if (param1Boolean) {
              i = 1;
            } else {
              i = -1;
            } 
            postponedUpdate1.currentPos = j + i;
          } 
          return postponedUpdate;
        } 
      } 
      return null;
    }
    
    public int convertNewPositionToOld(int param1Int) {
      if (param1Int >= 0 && param1Int < this.mNewListSize) {
        param1Int = this.mNewItemStatuses[param1Int];
        return ((param1Int & 0x1F) == 0) ? -1 : (param1Int >> 5);
      } 
      throw new IndexOutOfBoundsException("Index out of bounds - passed position = " + param1Int + ", new list size = " + this.mNewListSize);
    }
    
    public int convertOldPositionToNew(int param1Int) {
      if (param1Int >= 0 && param1Int < this.mOldListSize) {
        param1Int = this.mOldItemStatuses[param1Int];
        return ((param1Int & 0x1F) == 0) ? -1 : (param1Int >> 5);
      } 
      throw new IndexOutOfBoundsException("Index out of bounds - passed position = " + param1Int + ", old list size = " + this.mOldListSize);
    }
    
    public void dispatchUpdatesTo(ListUpdateCallback param1ListUpdateCallback) {
      if (param1ListUpdateCallback instanceof BatchingListUpdateCallback) {
        param1ListUpdateCallback = param1ListUpdateCallback;
      } else {
        param1ListUpdateCallback = new BatchingListUpdateCallback(param1ListUpdateCallback);
      } 
      ArrayList<DiffUtil.PostponedUpdate> arrayList = new ArrayList();
      int k = this.mOldListSize;
      int j = this.mNewListSize;
      for (int i = this.mSnakes.size(); --i >= 0; i--) {
        DiffUtil.Snake snake = this.mSnakes.get(i);
        int i1 = snake.size;
        int m = snake.x + i1;
        int n = snake.y + i1;
        if (m < k)
          dispatchRemovals(arrayList, param1ListUpdateCallback, m, k - m, m); 
        if (n < j)
          dispatchAdditions(arrayList, param1ListUpdateCallback, m, j - n, n); 
        for (j = i1 - 1; j >= 0; j--) {
          if ((this.mOldItemStatuses[snake.x + j] & 0x1F) == 2)
            param1ListUpdateCallback.onChanged(snake.x + j, 1, this.mCallback.getChangePayload(snake.x + j, snake.y + j)); 
        } 
        k = snake.x;
        j = snake.y;
      } 
      param1ListUpdateCallback.dispatchLastEvent();
    }
    
    public void dispatchUpdatesTo(RecyclerView.Adapter param1Adapter) {
      dispatchUpdatesTo(new AdapterListUpdateCallback(param1Adapter));
    }
    
    List<DiffUtil.Snake> getSnakes() {
      return this.mSnakes;
    }
  }
  
  public static abstract class ItemCallback<T> {
    public abstract boolean areContentsTheSame(T param1T1, T param1T2);
    
    public abstract boolean areItemsTheSame(T param1T1, T param1T2);
    
    public Object getChangePayload(T param1T1, T param1T2) {
      return null;
    }
  }
  
  private static class PostponedUpdate {
    int currentPos;
    
    int posInOwnerList;
    
    boolean removal;
    
    public PostponedUpdate(int param1Int1, int param1Int2, boolean param1Boolean) {
      this.posInOwnerList = param1Int1;
      this.currentPos = param1Int2;
      this.removal = param1Boolean;
    }
  }
  
  static class Range {
    int newListEnd;
    
    int newListStart;
    
    int oldListEnd;
    
    int oldListStart;
    
    public Range() {}
    
    public Range(int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
      this.oldListStart = param1Int1;
      this.oldListEnd = param1Int2;
      this.newListStart = param1Int3;
      this.newListEnd = param1Int4;
    }
  }
  
  static class Snake {
    boolean removal;
    
    boolean reverse;
    
    int size;
    
    int x;
    
    int y;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\recyclerview\widget\DiffUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */