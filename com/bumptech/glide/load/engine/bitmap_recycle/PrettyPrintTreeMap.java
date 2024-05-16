package com.bumptech.glide.load.engine.bitmap_recycle;

import java.util.Map;
import java.util.TreeMap;

class PrettyPrintTreeMap<K, V> extends TreeMap<K, V> {
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("( ");
    for (Map.Entry<K, V> entry : entrySet())
      stringBuilder.append('{').append(entry.getKey()).append(':').append(entry.getValue()).append("}, "); 
    if (!isEmpty())
      stringBuilder.replace(stringBuilder.length() - 2, stringBuilder.length(), ""); 
    return stringBuilder.append(" )").toString();
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\engine\bitmap_recycle\PrettyPrintTreeMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */