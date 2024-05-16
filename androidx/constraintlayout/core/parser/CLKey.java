package androidx.constraintlayout.core.parser;

import java.util.ArrayList;

public class CLKey extends CLContainer {
  private static ArrayList<String> sections;
  
  static {
    ArrayList<String> arrayList = new ArrayList();
    sections = arrayList;
    arrayList.add("ConstraintSets");
    sections.add("Variables");
    sections.add("Generate");
    sections.add("Transitions");
    sections.add("KeyFrames");
    sections.add("KeyAttributes");
    sections.add("KeyPositions");
    sections.add("KeyCycles");
  }
  
  public CLKey(char[] paramArrayOfchar) {
    super(paramArrayOfchar);
  }
  
  public static CLElement allocate(String paramString, CLElement paramCLElement) {
    CLKey cLKey = new CLKey(paramString.toCharArray());
    cLKey.setStart(0L);
    cLKey.setEnd((paramString.length() - 1));
    cLKey.set(paramCLElement);
    return cLKey;
  }
  
  public static CLElement allocate(char[] paramArrayOfchar) {
    return new CLKey(paramArrayOfchar);
  }
  
  public String getName() {
    return content();
  }
  
  public CLElement getValue() {
    return (this.mElements.size() > 0) ? this.mElements.get(0) : null;
  }
  
  public void set(CLElement paramCLElement) {
    if (this.mElements.size() > 0) {
      this.mElements.set(0, paramCLElement);
    } else {
      this.mElements.add(paramCLElement);
    } 
  }
  
  protected String toFormattedJSON(int paramInt1, int paramInt2) {
    StringBuilder stringBuilder = new StringBuilder(getDebugName());
    addIndent(stringBuilder, paramInt1);
    String str = content();
    if (this.mElements.size() > 0) {
      stringBuilder.append(str);
      stringBuilder.append(": ");
      if (sections.contains(str))
        paramInt2 = 3; 
      if (paramInt2 > 0) {
        stringBuilder.append(((CLElement)this.mElements.get(0)).toFormattedJSON(paramInt1, paramInt2 - 1));
      } else {
        str = ((CLElement)this.mElements.get(0)).toJSON();
        if (str.length() + paramInt1 < MAX_LINE) {
          stringBuilder.append(str);
        } else {
          stringBuilder.append(((CLElement)this.mElements.get(0)).toFormattedJSON(paramInt1, paramInt2 - 1));
        } 
      } 
      return stringBuilder.toString();
    } 
    return str + ": <> ";
  }
  
  protected String toJSON() {
    return (this.mElements.size() > 0) ? (getDebugName() + content() + ": " + ((CLElement)this.mElements.get(0)).toJSON()) : (getDebugName() + content() + ": <> ");
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\parser\CLKey.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */