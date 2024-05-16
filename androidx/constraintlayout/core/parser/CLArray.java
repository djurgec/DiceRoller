package androidx.constraintlayout.core.parser;

public class CLArray extends CLContainer {
  public CLArray(char[] paramArrayOfchar) {
    super(paramArrayOfchar);
  }
  
  public static CLElement allocate(char[] paramArrayOfchar) {
    return new CLArray(paramArrayOfchar);
  }
  
  protected String toFormattedJSON(int paramInt1, int paramInt2) {
    StringBuilder stringBuilder = new StringBuilder();
    String str = toJSON();
    if (paramInt2 <= 0 && str.length() + paramInt1 < MAX_LINE) {
      stringBuilder.append(str);
    } else {
      stringBuilder.append("[\n");
      boolean bool = true;
      for (CLElement cLElement : this.mElements) {
        if (!bool) {
          stringBuilder.append(",\n");
        } else {
          bool = false;
        } 
        addIndent(stringBuilder, BASE_INDENT + paramInt1);
        stringBuilder.append(cLElement.toFormattedJSON(BASE_INDENT + paramInt1, paramInt2 - 1));
      } 
      stringBuilder.append("\n");
      addIndent(stringBuilder, paramInt1);
      stringBuilder.append("]");
    } 
    return stringBuilder.toString();
  }
  
  protected String toJSON() {
    StringBuilder stringBuilder = new StringBuilder(getDebugName() + "[");
    boolean bool = true;
    for (byte b = 0; b < this.mElements.size(); b++) {
      if (!bool) {
        stringBuilder.append(", ");
      } else {
        bool = false;
      } 
      stringBuilder.append(((CLElement)this.mElements.get(b)).toJSON());
    } 
    return stringBuilder + "]";
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\parser\CLArray.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */