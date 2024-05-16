package androidx.constraintlayout.widget;

import android.util.SparseIntArray;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class SharedValues {
  public static final int UNSET = -1;
  
  private SparseIntArray mValues = new SparseIntArray();
  
  private HashMap<Integer, HashSet<WeakReference<SharedValuesListener>>> mValuesListeners = new HashMap<>();
  
  public void addListener(int paramInt, SharedValuesListener paramSharedValuesListener) {
    HashSet<WeakReference<SharedValuesListener>> hashSet2 = this.mValuesListeners.get(Integer.valueOf(paramInt));
    HashSet<WeakReference<SharedValuesListener>> hashSet1 = hashSet2;
    if (hashSet2 == null) {
      hashSet1 = new HashSet();
      this.mValuesListeners.put(Integer.valueOf(paramInt), hashSet1);
    } 
    hashSet1.add(new WeakReference<>(paramSharedValuesListener));
  }
  
  public void clearListeners() {
    this.mValuesListeners.clear();
  }
  
  public void fireNewValue(int paramInt1, int paramInt2) {
    boolean bool = false;
    int i = this.mValues.get(paramInt1, -1);
    if (i == paramInt2)
      return; 
    this.mValues.put(paramInt1, paramInt2);
    HashSet hashSet = this.mValuesListeners.get(Integer.valueOf(paramInt1));
    if (hashSet == null)
      return; 
    Iterator<WeakReference<SharedValuesListener>> iterator = hashSet.iterator();
    while (iterator.hasNext()) {
      SharedValuesListener sharedValuesListener = ((WeakReference<SharedValuesListener>)iterator.next()).get();
      if (sharedValuesListener != null) {
        sharedValuesListener.onNewValue(paramInt1, paramInt2, i);
        continue;
      } 
      bool = true;
    } 
    if (bool) {
      ArrayList<WeakReference<SharedValuesListener>> arrayList = new ArrayList();
      for (WeakReference<SharedValuesListener> weakReference : (Iterable<WeakReference<SharedValuesListener>>)hashSet) {
        if ((SharedValuesListener)weakReference.get() == null)
          arrayList.add(weakReference); 
      } 
      hashSet.removeAll(arrayList);
    } 
  }
  
  public int getValue(int paramInt) {
    return this.mValues.get(paramInt, -1);
  }
  
  public void removeListener(int paramInt, SharedValuesListener paramSharedValuesListener) {
    HashSet hashSet = this.mValuesListeners.get(Integer.valueOf(paramInt));
    if (hashSet == null)
      return; 
    ArrayList<WeakReference<SharedValuesListener>> arrayList = new ArrayList();
    for (WeakReference<SharedValuesListener> weakReference : (Iterable<WeakReference<SharedValuesListener>>)hashSet) {
      SharedValuesListener sharedValuesListener = weakReference.get();
      if (sharedValuesListener == null || sharedValuesListener == paramSharedValuesListener)
        arrayList.add(weakReference); 
    } 
    hashSet.removeAll(arrayList);
  }
  
  public void removeListener(SharedValuesListener paramSharedValuesListener) {
    Iterator<Integer> iterator = this.mValuesListeners.keySet().iterator();
    while (iterator.hasNext())
      removeListener(((Integer)iterator.next()).intValue(), paramSharedValuesListener); 
  }
  
  public static interface SharedValuesListener {
    void onNewValue(int param1Int1, int param1Int2, int param1Int3);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\widget\SharedValues.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */