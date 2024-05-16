package com.bumptech.glide.util;

public class MultiClassKey {
  private Class<?> first;
  
  private Class<?> second;
  
  private Class<?> third;
  
  public MultiClassKey() {}
  
  public MultiClassKey(Class<?> paramClass1, Class<?> paramClass2) {
    set(paramClass1, paramClass2);
  }
  
  public MultiClassKey(Class<?> paramClass1, Class<?> paramClass2, Class<?> paramClass3) {
    set(paramClass1, paramClass2, paramClass3);
  }
  
  public boolean equals(Object paramObject) {
    if (this == paramObject)
      return true; 
    if (paramObject == null || getClass() != paramObject.getClass())
      return false; 
    paramObject = paramObject;
    return !this.first.equals(((MultiClassKey)paramObject).first) ? false : (!this.second.equals(((MultiClassKey)paramObject).second) ? false : (!!Util.bothNullOrEqual(this.third, ((MultiClassKey)paramObject).third)));
  }
  
  public int hashCode() {
    byte b;
    int i = this.first.hashCode();
    int j = this.second.hashCode();
    Class<?> clazz = this.third;
    if (clazz != null) {
      b = clazz.hashCode();
    } else {
      b = 0;
    } 
    return (i * 31 + j) * 31 + b;
  }
  
  public void set(Class<?> paramClass1, Class<?> paramClass2) {
    set(paramClass1, paramClass2, null);
  }
  
  public void set(Class<?> paramClass1, Class<?> paramClass2, Class<?> paramClass3) {
    this.first = paramClass1;
    this.second = paramClass2;
    this.third = paramClass3;
  }
  
  public String toString() {
    return "MultiClassKey{first=" + this.first + ", second=" + this.second + '}';
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glid\\util\MultiClassKey.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */