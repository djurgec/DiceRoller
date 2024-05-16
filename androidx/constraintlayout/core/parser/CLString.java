package androidx.constraintlayout.core.parser;

public class CLString extends CLElement {
  public CLString(char[] paramArrayOfchar) {
    super(paramArrayOfchar);
  }
  
  public static CLElement allocate(char[] paramArrayOfchar) {
    return new CLString(paramArrayOfchar);
  }
  
  protected String toFormattedJSON(int paramInt1, int paramInt2) {
    StringBuilder stringBuilder = new StringBuilder();
    addIndent(stringBuilder, paramInt1);
    stringBuilder.append("'");
    stringBuilder.append(content());
    stringBuilder.append("'");
    return stringBuilder.toString();
  }
  
  protected String toJSON() {
    return "'" + content() + "'";
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\parser\CLString.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */