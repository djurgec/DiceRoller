package androidx.collection;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class ArrayMap<K, V> extends SimpleArrayMap<K, V> implements Map<K, V> {
  MapCollections<K, V> mCollections;
  
  public ArrayMap() {}
  
  public ArrayMap(int paramInt) {
    super(paramInt);
  }
  
  public ArrayMap(SimpleArrayMap<K, V> paramSimpleArrayMap) {
    super(paramSimpleArrayMap);
  }
  
  private MapCollections<K, V> getCollection() {
    if (this.mCollections == null)
      this.mCollections = new MapCollections<K, V>() {
          final ArrayMap this$0;
          
          protected void colClear() {
            ArrayMap.this.clear();
          }
          
          protected Object colGetEntry(int param1Int1, int param1Int2) {
            return ArrayMap.this.mArray[(param1Int1 << 1) + param1Int2];
          }
          
          protected Map<K, V> colGetMap() {
            return ArrayMap.this;
          }
          
          protected int colGetSize() {
            return ArrayMap.this.mSize;
          }
          
          protected int colIndexOfKey(Object param1Object) {
            return ArrayMap.this.indexOfKey(param1Object);
          }
          
          protected int colIndexOfValue(Object param1Object) {
            return ArrayMap.this.indexOfValue(param1Object);
          }
          
          protected void colPut(K param1K, V param1V) {
            ArrayMap.this.put(param1K, param1V);
          }
          
          protected void colRemoveAt(int param1Int) {
            ArrayMap.this.removeAt(param1Int);
          }
          
          protected V colSetValue(int param1Int, V param1V) {
            return (V)ArrayMap.this.setValueAt(param1Int, param1V);
          }
        }; 
    return this.mCollections;
  }
  
  public boolean containsAll(Collection<?> paramCollection) {
    return MapCollections.containsAllHelper(this, paramCollection);
  }
  
  public Set<Map.Entry<K, V>> entrySet() {
    return getCollection().getEntrySet();
  }
  
  public Set<K> keySet() {
    return getCollection().getKeySet();
  }
  
  public void putAll(Map<? extends K, ? extends V> paramMap) {
    ensureCapacity(this.mSize + paramMap.size());
    for (Map.Entry<? extends K, ? extends V> entry : paramMap.entrySet())
      put((K)entry.getKey(), (V)entry.getValue()); 
  }
  
  public boolean removeAll(Collection<?> paramCollection) {
    return MapCollections.removeAllHelper(this, paramCollection);
  }
  
  public boolean retainAll(Collection<?> paramCollection) {
    return MapCollections.retainAllHelper(this, paramCollection);
  }
  
  public Collection<V> values() {
    return getCollection().getValues();
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\collection\ArrayMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */