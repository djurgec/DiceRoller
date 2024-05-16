package androidx.coordinatorlayout.widget;

import androidx.collection.SimpleArrayMap;
import androidx.core.util.Pools;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public final class DirectedAcyclicGraph<T> {
  private final SimpleArrayMap<T, ArrayList<T>> mGraph = new SimpleArrayMap();
  
  private final Pools.Pool<ArrayList<T>> mListPool = (Pools.Pool<ArrayList<T>>)new Pools.SimplePool(10);
  
  private final ArrayList<T> mSortResult = new ArrayList<>();
  
  private final HashSet<T> mSortTmpMarked = new HashSet<>();
  
  private void dfs(T paramT, ArrayList<T> paramArrayList, HashSet<T> paramHashSet) {
    if (paramArrayList.contains(paramT))
      return; 
    if (!paramHashSet.contains(paramT)) {
      paramHashSet.add(paramT);
      ArrayList<T> arrayList = (ArrayList)this.mGraph.get(paramT);
      if (arrayList != null) {
        byte b = 0;
        int i = arrayList.size();
        while (b < i) {
          dfs(arrayList.get(b), paramArrayList, paramHashSet);
          b++;
        } 
      } 
      paramHashSet.remove(paramT);
      paramArrayList.add(paramT);
      return;
    } 
    throw new RuntimeException("This graph contains cyclic dependencies");
  }
  
  private ArrayList<T> getEmptyList() {
    ArrayList<T> arrayList2 = (ArrayList)this.mListPool.acquire();
    ArrayList<T> arrayList1 = arrayList2;
    if (arrayList2 == null)
      arrayList1 = new ArrayList(); 
    return arrayList1;
  }
  
  private void poolList(ArrayList<T> paramArrayList) {
    paramArrayList.clear();
    this.mListPool.release(paramArrayList);
  }
  
  public void addEdge(T paramT1, T paramT2) {
    if (this.mGraph.containsKey(paramT1) && this.mGraph.containsKey(paramT2)) {
      ArrayList<T> arrayList2 = (ArrayList)this.mGraph.get(paramT1);
      ArrayList<T> arrayList1 = arrayList2;
      if (arrayList2 == null) {
        arrayList1 = getEmptyList();
        this.mGraph.put(paramT1, arrayList1);
      } 
      arrayList1.add(paramT2);
      return;
    } 
    throw new IllegalArgumentException("All nodes must be present in the graph before being added as an edge");
  }
  
  public void addNode(T paramT) {
    if (!this.mGraph.containsKey(paramT))
      this.mGraph.put(paramT, null); 
  }
  
  public void clear() {
    byte b = 0;
    int i = this.mGraph.size();
    while (b < i) {
      ArrayList<T> arrayList = (ArrayList)this.mGraph.valueAt(b);
      if (arrayList != null)
        poolList(arrayList); 
      b++;
    } 
    this.mGraph.clear();
  }
  
  public boolean contains(T paramT) {
    return this.mGraph.containsKey(paramT);
  }
  
  public List getIncomingEdges(T paramT) {
    return (List)this.mGraph.get(paramT);
  }
  
  public List<T> getOutgoingEdges(T paramT) {
    ArrayList<Object> arrayList = null;
    byte b = 0;
    int i = this.mGraph.size();
    while (b < i) {
      ArrayList arrayList2 = (ArrayList)this.mGraph.valueAt(b);
      ArrayList<Object> arrayList1 = arrayList;
      if (arrayList2 != null) {
        arrayList1 = arrayList;
        if (arrayList2.contains(paramT)) {
          arrayList1 = arrayList;
          if (arrayList == null)
            arrayList1 = new ArrayList(); 
          arrayList1.add(this.mGraph.keyAt(b));
        } 
      } 
      b++;
      arrayList = arrayList1;
    } 
    return arrayList;
  }
  
  public ArrayList<T> getSortedList() {
    this.mSortResult.clear();
    this.mSortTmpMarked.clear();
    byte b = 0;
    int i = this.mGraph.size();
    while (b < i) {
      dfs((T)this.mGraph.keyAt(b), this.mSortResult, this.mSortTmpMarked);
      b++;
    } 
    return this.mSortResult;
  }
  
  public boolean hasOutgoingEdges(T paramT) {
    byte b = 0;
    int i = this.mGraph.size();
    while (b < i) {
      ArrayList arrayList = (ArrayList)this.mGraph.valueAt(b);
      if (arrayList != null && arrayList.contains(paramT))
        return true; 
      b++;
    } 
    return false;
  }
  
  int size() {
    return this.mGraph.size();
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\coordinatorlayout\widget\DirectedAcyclicGraph.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */