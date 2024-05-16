package com.bumptech.glide.util;

import androidx.collection.ArrayMap;
import androidx.collection.SimpleArrayMap;

public final class CachedHashCodeArrayMap<K, V> extends ArrayMap<K, V> {
  private int hashCode;
  
  public void clear() {
    this.hashCode = 0;
    super.clear();
  }
  
  public int hashCode() {
    if (this.hashCode == 0)
      this.hashCode = super.hashCode(); 
    return this.hashCode;
  }
  
  public V put(K paramK, V paramV) {
    this.hashCode = 0;
    return (V)super.put(paramK, paramV);
  }
  
  public void putAll(SimpleArrayMap<? extends K, ? extends V> paramSimpleArrayMap) {
    this.hashCode = 0;
    super.putAll(paramSimpleArrayMap);
  }
  
  public V removeAt(int paramInt) {
    this.hashCode = 0;
    return (V)super.removeAt(paramInt);
  }
  
  public V setValueAt(int paramInt, V paramV) {
    this.hashCode = 0;
    return (V)super.setValueAt(paramInt, paramV);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glid\\util\CachedHashCodeArrayMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */