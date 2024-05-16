package androidx.recyclerview.widget;

import androidx.core.util.Pools;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class AdapterHelper implements OpReorderer.Callback {
  private static final boolean DEBUG = false;
  
  static final int POSITION_TYPE_INVISIBLE = 0;
  
  static final int POSITION_TYPE_NEW_OR_LAID_OUT = 1;
  
  private static final String TAG = "AHT";
  
  final Callback mCallback;
  
  final boolean mDisableRecycler;
  
  private int mExistingUpdateTypes = 0;
  
  Runnable mOnItemProcessedCallback;
  
  final OpReorderer mOpReorderer;
  
  final ArrayList<UpdateOp> mPendingUpdates = new ArrayList<>();
  
  final ArrayList<UpdateOp> mPostponedList = new ArrayList<>();
  
  private Pools.Pool<UpdateOp> mUpdateOpPool = (Pools.Pool<UpdateOp>)new Pools.SimplePool(30);
  
  AdapterHelper(Callback paramCallback) {
    this(paramCallback, false);
  }
  
  AdapterHelper(Callback paramCallback, boolean paramBoolean) {
    this.mCallback = paramCallback;
    this.mDisableRecycler = paramBoolean;
    this.mOpReorderer = new OpReorderer(this);
  }
  
  private void applyAdd(UpdateOp paramUpdateOp) {
    postponeAndUpdateViewHolders(paramUpdateOp);
  }
  
  private void applyMove(UpdateOp paramUpdateOp) {
    postponeAndUpdateViewHolders(paramUpdateOp);
  }
  
  private void applyRemove(UpdateOp paramUpdateOp) {
    int m = paramUpdateOp.positionStart;
    int k = 0;
    int j = paramUpdateOp.positionStart + paramUpdateOp.itemCount;
    byte b = -1;
    for (int i = paramUpdateOp.positionStart; i < j; i = n) {
      byte b1 = 0;
      int n = 0;
      if (this.mCallback.findViewHolder(i) != null || canFindInPreLayout(i)) {
        if (b == 0) {
          dispatchAndUpdateViewHolders(obtainUpdateOp(2, m, k, null));
          b1 = 1;
        } 
        b = 1;
        n = b1;
        b1 = b;
      } else {
        if (b == 1) {
          postponeAndUpdateViewHolders(obtainUpdateOp(2, m, k, null));
          n = 1;
        } 
        b1 = 0;
      } 
      if (n != 0) {
        n = i - k;
        j -= k;
        i = 1;
      } else {
        k++;
        n = i;
        i = k;
      } 
      n++;
      k = i;
      b = b1;
    } 
    UpdateOp updateOp = paramUpdateOp;
    if (k != paramUpdateOp.itemCount) {
      recycleUpdateOp(paramUpdateOp);
      updateOp = obtainUpdateOp(2, m, k, null);
    } 
    if (b == 0) {
      dispatchAndUpdateViewHolders(updateOp);
    } else {
      postponeAndUpdateViewHolders(updateOp);
    } 
  }
  
  private void applyUpdate(UpdateOp paramUpdateOp) {
    int j = paramUpdateOp.positionStart;
    int k = 0;
    int i1 = paramUpdateOp.positionStart;
    int n = paramUpdateOp.itemCount;
    int m = -1;
    int i = paramUpdateOp.positionStart;
    while (i < i1 + n) {
      int i2;
      int i3;
      if (this.mCallback.findViewHolder(i) != null || canFindInPreLayout(i)) {
        int i4 = j;
        i3 = k;
        if (m == 0) {
          dispatchAndUpdateViewHolders(obtainUpdateOp(4, j, k, paramUpdateOp.payload));
          i3 = 0;
          i4 = i;
        } 
        i2 = 1;
        j = i4;
      } else {
        i3 = j;
        i2 = k;
        if (m == 1) {
          postponeAndUpdateViewHolders(obtainUpdateOp(4, j, k, paramUpdateOp.payload));
          i2 = 0;
          i3 = i;
        } 
        k = 0;
        j = i3;
        i3 = i2;
        i2 = k;
      } 
      k = i3 + 1;
      i++;
      m = i2;
    } 
    Object object = paramUpdateOp;
    if (k != paramUpdateOp.itemCount) {
      object = paramUpdateOp.payload;
      recycleUpdateOp(paramUpdateOp);
      object = obtainUpdateOp(4, j, k, object);
    } 
    if (m == 0) {
      dispatchAndUpdateViewHolders((UpdateOp)object);
    } else {
      postponeAndUpdateViewHolders((UpdateOp)object);
    } 
  }
  
  private boolean canFindInPreLayout(int paramInt) {
    int i = this.mPostponedList.size();
    for (byte b = 0; b < i; b++) {
      UpdateOp updateOp = this.mPostponedList.get(b);
      if (updateOp.cmd == 8) {
        if (findPositionOffset(updateOp.itemCount, b + 1) == paramInt)
          return true; 
      } else if (updateOp.cmd == 1) {
        int m = updateOp.positionStart;
        int k = updateOp.itemCount;
        for (int j = updateOp.positionStart; j < m + k; j++) {
          if (findPositionOffset(j, b + 1) == paramInt)
            return true; 
        } 
      } 
    } 
    return false;
  }
  
  private void dispatchAndUpdateViewHolders(UpdateOp paramUpdateOp) {
    if (paramUpdateOp.cmd != 1 && paramUpdateOp.cmd != 8) {
      byte b1;
      int j = updatePositionWithPostponed(paramUpdateOp.positionStart, paramUpdateOp.cmd);
      int k = 1;
      int i = paramUpdateOp.positionStart;
      switch (paramUpdateOp.cmd) {
        default:
          throw new IllegalArgumentException("op should be remove or update." + paramUpdateOp);
        case 4:
          b1 = 1;
          break;
        case 2:
          b1 = 0;
          break;
      } 
      byte b2 = 1;
      while (b2 < paramUpdateOp.itemCount) {
        int n = updatePositionWithPostponed(paramUpdateOp.positionStart + b1 * b2, paramUpdateOp.cmd);
        int m = 0;
        int i1 = paramUpdateOp.cmd;
        byte b3 = 0;
        byte b4 = 0;
        switch (i1) {
          case 4:
            m = b4;
            if (n == j + 1)
              m = 1; 
            break;
          case 2:
            m = b3;
            if (n == j)
              m = 1; 
            break;
        } 
        if (m) {
          m = k + 1;
        } else {
          UpdateOp updateOp = obtainUpdateOp(paramUpdateOp.cmd, j, k, paramUpdateOp.payload);
          dispatchFirstPassAndUpdateViewHolders(updateOp, i);
          recycleUpdateOp(updateOp);
          m = i;
          if (paramUpdateOp.cmd == 4)
            m = i + k; 
          j = n;
          k = 1;
          i = m;
          m = k;
        } 
        b2++;
        k = m;
      } 
      Object object = paramUpdateOp.payload;
      recycleUpdateOp(paramUpdateOp);
      if (k > 0) {
        paramUpdateOp = obtainUpdateOp(paramUpdateOp.cmd, j, k, object);
        dispatchFirstPassAndUpdateViewHolders(paramUpdateOp, i);
        recycleUpdateOp(paramUpdateOp);
      } 
      return;
    } 
    throw new IllegalArgumentException("should not dispatch add or move for pre layout");
  }
  
  private void postponeAndUpdateViewHolders(UpdateOp paramUpdateOp) {
    this.mPostponedList.add(paramUpdateOp);
    switch (paramUpdateOp.cmd) {
      default:
        throw new IllegalArgumentException("Unknown update op type for " + paramUpdateOp);
      case 8:
        this.mCallback.offsetPositionsForMove(paramUpdateOp.positionStart, paramUpdateOp.itemCount);
        return;
      case 4:
        this.mCallback.markViewHoldersUpdated(paramUpdateOp.positionStart, paramUpdateOp.itemCount, paramUpdateOp.payload);
        return;
      case 2:
        this.mCallback.offsetPositionsForRemovingLaidOutOrNewView(paramUpdateOp.positionStart, paramUpdateOp.itemCount);
        return;
      case 1:
        break;
    } 
    this.mCallback.offsetPositionsForAdd(paramUpdateOp.positionStart, paramUpdateOp.itemCount);
  }
  
  private int updatePositionWithPostponed(int paramInt1, int paramInt2) {
    int j = this.mPostponedList.size() - 1;
    int i;
    for (i = paramInt1; j >= 0; i = paramInt1) {
      UpdateOp updateOp = this.mPostponedList.get(j);
      if (updateOp.cmd == 8) {
        int k;
        if (updateOp.positionStart < updateOp.itemCount) {
          paramInt1 = updateOp.positionStart;
          k = updateOp.itemCount;
        } else {
          paramInt1 = updateOp.itemCount;
          k = updateOp.positionStart;
        } 
        if (i >= paramInt1 && i <= k) {
          if (paramInt1 == updateOp.positionStart) {
            if (paramInt2 == 1) {
              updateOp.itemCount++;
            } else if (paramInt2 == 2) {
              updateOp.itemCount--;
            } 
            paramInt1 = i + 1;
          } else {
            if (paramInt2 == 1) {
              updateOp.positionStart++;
            } else if (paramInt2 == 2) {
              updateOp.positionStart--;
            } 
            paramInt1 = i - 1;
          } 
        } else {
          paramInt1 = i;
          if (i < updateOp.positionStart)
            if (paramInt2 == 1) {
              updateOp.positionStart++;
              updateOp.itemCount++;
              paramInt1 = i;
            } else {
              paramInt1 = i;
              if (paramInt2 == 2) {
                updateOp.positionStart--;
                updateOp.itemCount--;
                paramInt1 = i;
              } 
            }  
        } 
      } else if (updateOp.positionStart <= i) {
        if (updateOp.cmd == 1) {
          paramInt1 = i - updateOp.itemCount;
        } else {
          paramInt1 = i;
          if (updateOp.cmd == 2)
            paramInt1 = i + updateOp.itemCount; 
        } 
      } else if (paramInt2 == 1) {
        updateOp.positionStart++;
        paramInt1 = i;
      } else {
        paramInt1 = i;
        if (paramInt2 == 2) {
          updateOp.positionStart--;
          paramInt1 = i;
        } 
      } 
      j--;
    } 
    for (paramInt1 = this.mPostponedList.size() - 1; paramInt1 >= 0; paramInt1--) {
      UpdateOp updateOp = this.mPostponedList.get(paramInt1);
      if (updateOp.cmd == 8) {
        if (updateOp.itemCount == updateOp.positionStart || updateOp.itemCount < 0) {
          this.mPostponedList.remove(paramInt1);
          recycleUpdateOp(updateOp);
        } 
      } else if (updateOp.itemCount <= 0) {
        this.mPostponedList.remove(paramInt1);
        recycleUpdateOp(updateOp);
      } 
    } 
    return i;
  }
  
  AdapterHelper addUpdateOp(UpdateOp... paramVarArgs) {
    Collections.addAll(this.mPendingUpdates, paramVarArgs);
    return this;
  }
  
  public int applyPendingUpdatesToPosition(int paramInt) {
    int j = this.mPendingUpdates.size();
    byte b = 0;
    int i;
    for (i = paramInt; b < j; i = paramInt) {
      int k;
      UpdateOp updateOp = this.mPendingUpdates.get(b);
      switch (updateOp.cmd) {
        default:
          paramInt = i;
          break;
        case 8:
          if (updateOp.positionStart == i) {
            paramInt = updateOp.itemCount;
            break;
          } 
          k = i;
          if (updateOp.positionStart < i)
            k = i - 1; 
          paramInt = k;
          if (updateOp.itemCount <= k)
            paramInt = k + 1; 
          break;
        case 2:
          paramInt = i;
          if (updateOp.positionStart <= i) {
            if (updateOp.positionStart + updateOp.itemCount > i)
              return -1; 
            paramInt = i - updateOp.itemCount;
          } 
          break;
        case 1:
          paramInt = i;
          if (updateOp.positionStart <= i)
            paramInt = i + updateOp.itemCount; 
          break;
      } 
      b++;
    } 
    return i;
  }
  
  void consumePostponedUpdates() {
    int i = this.mPostponedList.size();
    for (byte b = 0; b < i; b++)
      this.mCallback.onDispatchSecondPass(this.mPostponedList.get(b)); 
    recycleUpdateOpsAndClearList(this.mPostponedList);
    this.mExistingUpdateTypes = 0;
  }
  
  void consumeUpdatesInOnePass() {
    consumePostponedUpdates();
    int i = this.mPendingUpdates.size();
    for (byte b = 0; b < i; b++) {
      UpdateOp updateOp = this.mPendingUpdates.get(b);
      switch (updateOp.cmd) {
        case 8:
          this.mCallback.onDispatchSecondPass(updateOp);
          this.mCallback.offsetPositionsForMove(updateOp.positionStart, updateOp.itemCount);
          break;
        case 4:
          this.mCallback.onDispatchSecondPass(updateOp);
          this.mCallback.markViewHoldersUpdated(updateOp.positionStart, updateOp.itemCount, updateOp.payload);
          break;
        case 2:
          this.mCallback.onDispatchSecondPass(updateOp);
          this.mCallback.offsetPositionsForRemovingInvisible(updateOp.positionStart, updateOp.itemCount);
          break;
        case 1:
          this.mCallback.onDispatchSecondPass(updateOp);
          this.mCallback.offsetPositionsForAdd(updateOp.positionStart, updateOp.itemCount);
          break;
      } 
      Runnable runnable = this.mOnItemProcessedCallback;
      if (runnable != null)
        runnable.run(); 
    } 
    recycleUpdateOpsAndClearList(this.mPendingUpdates);
    this.mExistingUpdateTypes = 0;
  }
  
  void dispatchFirstPassAndUpdateViewHolders(UpdateOp paramUpdateOp, int paramInt) {
    this.mCallback.onDispatchFirstPass(paramUpdateOp);
    switch (paramUpdateOp.cmd) {
      default:
        throw new IllegalArgumentException("only remove and update ops can be dispatched in first pass");
      case 4:
        this.mCallback.markViewHoldersUpdated(paramInt, paramUpdateOp.itemCount, paramUpdateOp.payload);
        return;
      case 2:
        break;
    } 
    this.mCallback.offsetPositionsForRemovingInvisible(paramInt, paramUpdateOp.itemCount);
  }
  
  int findPositionOffset(int paramInt) {
    return findPositionOffset(paramInt, 0);
  }
  
  int findPositionOffset(int paramInt1, int paramInt2) {
    int j = this.mPostponedList.size();
    int i = paramInt2;
    for (paramInt2 = paramInt1; i < j; paramInt2 = paramInt1) {
      UpdateOp updateOp = this.mPostponedList.get(i);
      if (updateOp.cmd == 8) {
        if (updateOp.positionStart == paramInt2) {
          paramInt1 = updateOp.itemCount;
        } else {
          int k = paramInt2;
          if (updateOp.positionStart < paramInt2)
            k = paramInt2 - 1; 
          paramInt1 = k;
          if (updateOp.itemCount <= k)
            paramInt1 = k + 1; 
        } 
      } else {
        paramInt1 = paramInt2;
        if (updateOp.positionStart <= paramInt2)
          if (updateOp.cmd == 2) {
            if (paramInt2 < updateOp.positionStart + updateOp.itemCount)
              return -1; 
            paramInt1 = paramInt2 - updateOp.itemCount;
          } else {
            paramInt1 = paramInt2;
            if (updateOp.cmd == 1)
              paramInt1 = paramInt2 + updateOp.itemCount; 
          }  
      } 
      i++;
    } 
    return paramInt2;
  }
  
  boolean hasAnyUpdateTypes(int paramInt) {
    boolean bool;
    if ((this.mExistingUpdateTypes & paramInt) != 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  boolean hasPendingUpdates() {
    boolean bool;
    if (this.mPendingUpdates.size() > 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  boolean hasUpdates() {
    boolean bool;
    if (!this.mPostponedList.isEmpty() && !this.mPendingUpdates.isEmpty()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public UpdateOp obtainUpdateOp(int paramInt1, int paramInt2, int paramInt3, Object paramObject) {
    UpdateOp updateOp = (UpdateOp)this.mUpdateOpPool.acquire();
    if (updateOp == null) {
      paramObject = new UpdateOp(paramInt1, paramInt2, paramInt3, paramObject);
    } else {
      updateOp.cmd = paramInt1;
      updateOp.positionStart = paramInt2;
      updateOp.itemCount = paramInt3;
      updateOp.payload = paramObject;
      paramObject = updateOp;
    } 
    return (UpdateOp)paramObject;
  }
  
  boolean onItemRangeChanged(int paramInt1, int paramInt2, Object paramObject) {
    boolean bool = false;
    if (paramInt2 < 1)
      return false; 
    this.mPendingUpdates.add(obtainUpdateOp(4, paramInt1, paramInt2, paramObject));
    this.mExistingUpdateTypes |= 0x4;
    if (this.mPendingUpdates.size() == 1)
      bool = true; 
    return bool;
  }
  
  boolean onItemRangeInserted(int paramInt1, int paramInt2) {
    boolean bool = false;
    if (paramInt2 < 1)
      return false; 
    this.mPendingUpdates.add(obtainUpdateOp(1, paramInt1, paramInt2, null));
    this.mExistingUpdateTypes |= 0x1;
    if (this.mPendingUpdates.size() == 1)
      bool = true; 
    return bool;
  }
  
  boolean onItemRangeMoved(int paramInt1, int paramInt2, int paramInt3) {
    boolean bool = false;
    if (paramInt1 == paramInt2)
      return false; 
    if (paramInt3 == 1) {
      this.mPendingUpdates.add(obtainUpdateOp(8, paramInt1, paramInt2, null));
      this.mExistingUpdateTypes |= 0x8;
      if (this.mPendingUpdates.size() == 1)
        bool = true; 
      return bool;
    } 
    throw new IllegalArgumentException("Moving more than 1 item is not supported yet");
  }
  
  boolean onItemRangeRemoved(int paramInt1, int paramInt2) {
    boolean bool = false;
    if (paramInt2 < 1)
      return false; 
    this.mPendingUpdates.add(obtainUpdateOp(2, paramInt1, paramInt2, null));
    this.mExistingUpdateTypes |= 0x2;
    if (this.mPendingUpdates.size() == 1)
      bool = true; 
    return bool;
  }
  
  void preProcess() {
    this.mOpReorderer.reorderOps(this.mPendingUpdates);
    int i = this.mPendingUpdates.size();
    for (byte b = 0; b < i; b++) {
      UpdateOp updateOp = this.mPendingUpdates.get(b);
      switch (updateOp.cmd) {
        case 8:
          applyMove(updateOp);
          break;
        case 4:
          applyUpdate(updateOp);
          break;
        case 2:
          applyRemove(updateOp);
          break;
        case 1:
          applyAdd(updateOp);
          break;
      } 
      Runnable runnable = this.mOnItemProcessedCallback;
      if (runnable != null)
        runnable.run(); 
    } 
    this.mPendingUpdates.clear();
  }
  
  public void recycleUpdateOp(UpdateOp paramUpdateOp) {
    if (!this.mDisableRecycler) {
      paramUpdateOp.payload = null;
      this.mUpdateOpPool.release(paramUpdateOp);
    } 
  }
  
  void recycleUpdateOpsAndClearList(List<UpdateOp> paramList) {
    int i = paramList.size();
    for (byte b = 0; b < i; b++)
      recycleUpdateOp(paramList.get(b)); 
    paramList.clear();
  }
  
  void reset() {
    recycleUpdateOpsAndClearList(this.mPendingUpdates);
    recycleUpdateOpsAndClearList(this.mPostponedList);
    this.mExistingUpdateTypes = 0;
  }
  
  static interface Callback {
    RecyclerView.ViewHolder findViewHolder(int param1Int);
    
    void markViewHoldersUpdated(int param1Int1, int param1Int2, Object param1Object);
    
    void offsetPositionsForAdd(int param1Int1, int param1Int2);
    
    void offsetPositionsForMove(int param1Int1, int param1Int2);
    
    void offsetPositionsForRemovingInvisible(int param1Int1, int param1Int2);
    
    void offsetPositionsForRemovingLaidOutOrNewView(int param1Int1, int param1Int2);
    
    void onDispatchFirstPass(AdapterHelper.UpdateOp param1UpdateOp);
    
    void onDispatchSecondPass(AdapterHelper.UpdateOp param1UpdateOp);
  }
  
  static class UpdateOp {
    static final int ADD = 1;
    
    static final int MOVE = 8;
    
    static final int POOL_SIZE = 30;
    
    static final int REMOVE = 2;
    
    static final int UPDATE = 4;
    
    int cmd;
    
    int itemCount;
    
    Object payload;
    
    int positionStart;
    
    UpdateOp(int param1Int1, int param1Int2, int param1Int3, Object param1Object) {
      this.cmd = param1Int1;
      this.positionStart = param1Int2;
      this.itemCount = param1Int3;
      this.payload = param1Object;
    }
    
    String cmdToString() {
      switch (this.cmd) {
        default:
          return "??";
        case 8:
          return "mv";
        case 4:
          return "up";
        case 2:
          return "rm";
        case 1:
          break;
      } 
      return "add";
    }
    
    public boolean equals(Object param1Object) {
      if (this == param1Object)
        return true; 
      if (param1Object == null || getClass() != param1Object.getClass())
        return false; 
      param1Object = param1Object;
      int i = this.cmd;
      if (i != ((UpdateOp)param1Object).cmd)
        return false; 
      if (i == 8 && Math.abs(this.itemCount - this.positionStart) == 1 && this.itemCount == ((UpdateOp)param1Object).positionStart && this.positionStart == ((UpdateOp)param1Object).itemCount)
        return true; 
      if (this.itemCount != ((UpdateOp)param1Object).itemCount)
        return false; 
      if (this.positionStart != ((UpdateOp)param1Object).positionStart)
        return false; 
      Object object = this.payload;
      if (object != null) {
        if (!object.equals(((UpdateOp)param1Object).payload))
          return false; 
      } else if (((UpdateOp)param1Object).payload != null) {
        return false;
      } 
      return true;
    }
    
    public int hashCode() {
      return (this.cmd * 31 + this.positionStart) * 31 + this.itemCount;
    }
    
    public String toString() {
      return Integer.toHexString(System.identityHashCode(this)) + "[" + cmdToString() + ",s:" + this.positionStart + "c:" + this.itemCount + ",p:" + this.payload + "]";
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\recyclerview\widget\AdapterHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */