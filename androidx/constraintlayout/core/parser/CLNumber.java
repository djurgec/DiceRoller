package androidx.constraintlayout.core.parser;

public class CLNumber extends CLElement {
  float value = Float.NaN;
  
  public CLNumber(float paramFloat) {
    super(null);
    this.value = paramFloat;
  }
  
  public CLNumber(char[] paramArrayOfchar) {
    super(paramArrayOfchar);
  }
  
  public static CLElement allocate(char[] paramArrayOfchar) {
    return new CLNumber(paramArrayOfchar);
  }
  
  public float getFloat() {
    if (Float.isNaN(this.value))
      this.value = Float.parseFloat(content()); 
    return this.value;
  }
  
  public int getInt() {
    if (Float.isNaN(this.value))
      this.value = Integer.parseInt(content()); 
    return (int)this.value;
  }
  
  public boolean isInt() {
    boolean bool;
    float f = getFloat();
    if ((int)f == f) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void putValue(float paramFloat) {
    this.value = paramFloat;
  }
  
  protected String toFormattedJSON(int paramInt1, int paramInt2) {
    StringBuilder stringBuilder = new StringBuilder();
    addIndent(stringBuilder, paramInt1);
    float f = getFloat();
    paramInt1 = (int)f;
    if (paramInt1 == f) {
      stringBuilder.append(paramInt1);
    } else {
      stringBuilder.append(f);
    } 
    return stringBuilder.toString();
  }
  
  protected String toJSON() {
    float f = getFloat();
    int i = (int)f;
    return (i == f) ? ("" + i) : ("" + f);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\parser\CLNumber.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */