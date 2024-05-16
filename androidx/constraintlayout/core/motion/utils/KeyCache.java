package androidx.constraintlayout.core.motion.utils;

import java.util.Arrays;
import java.util.HashMap;

public class KeyCache {
  HashMap<Object, HashMap<String, float[]>> map = new HashMap<>();
  
  public float getFloatValue(Object paramObject, String paramString, int paramInt) {
    if (!this.map.containsKey(paramObject))
      return Float.NaN; 
    paramObject = this.map.get(paramObject);
    if (paramObject == null || !paramObject.containsKey(paramString))
      return Float.NaN; 
    paramObject = paramObject.get(paramString);
    return (paramObject == null) ? Float.NaN : ((paramObject.length > paramInt) ? paramObject[paramInt] : Float.NaN);
  }
  
  public void setFloatValue(Object paramObject, String paramString, int paramInt, float paramFloat) {
    if (!this.map.containsKey(paramObject)) {
      HashMap<Object, Object> hashMap = new HashMap<>();
      float[] arrayOfFloat = new float[paramInt + 1];
      arrayOfFloat[paramInt] = paramFloat;
      hashMap.put(paramString, arrayOfFloat);
      this.map.put(paramObject, hashMap);
    } else {
      HashMap<Object, Object> hashMap2 = (HashMap)this.map.get(paramObject);
      HashMap<Object, Object> hashMap1 = hashMap2;
      if (hashMap2 == null)
        hashMap1 = new HashMap<>(); 
      if (!hashMap1.containsKey(paramString)) {
        float[] arrayOfFloat = new float[paramInt + 1];
        arrayOfFloat[paramInt] = paramFloat;
        hashMap1.put(paramString, arrayOfFloat);
        this.map.put(paramObject, hashMap1);
      } else {
        float[] arrayOfFloat = (float[])hashMap1.get(paramString);
        paramObject = arrayOfFloat;
        if (arrayOfFloat == null)
          paramObject = new float[0]; 
        Object object = paramObject;
        if (paramObject.length <= paramInt)
          object = Arrays.copyOf((float[])paramObject, paramInt + 1); 
        object[paramInt] = paramFloat;
        hashMap1.put(paramString, object);
      } 
    } 
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\motio\\utils\KeyCache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */