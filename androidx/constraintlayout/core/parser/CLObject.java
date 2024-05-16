package androidx.constraintlayout.core.parser;

import java.util.Iterator;

public class CLObject extends CLContainer implements Iterable<CLKey> {
  public CLObject(char[] paramArrayOfchar) {
    super(paramArrayOfchar);
  }
  
  public static CLObject allocate(char[] paramArrayOfchar) {
    return new CLObject(paramArrayOfchar);
  }
  
  public Iterator iterator() {
    return new CLObjectIterator(this);
  }
  
  public String toFormattedJSON() {
    return toFormattedJSON(0, 0);
  }
  
  public String toFormattedJSON(int paramInt1, int paramInt2) {
    StringBuilder stringBuilder = new StringBuilder(getDebugName());
    stringBuilder.append("{\n");
    boolean bool = true;
    for (CLElement cLElement : this.mElements) {
      if (!bool) {
        stringBuilder.append(",\n");
      } else {
        bool = false;
      } 
      stringBuilder.append(cLElement.toFormattedJSON(BASE_INDENT + paramInt1, paramInt2 - 1));
    } 
    stringBuilder.append("\n");
    addIndent(stringBuilder, paramInt1);
    stringBuilder.append("}");
    return stringBuilder.toString();
  }
  
  public String toJSON() {
    StringBuilder stringBuilder = new StringBuilder(getDebugName() + "{ ");
    boolean bool = true;
    for (CLElement cLElement : this.mElements) {
      if (!bool) {
        stringBuilder.append(", ");
      } else {
        bool = false;
      } 
      stringBuilder.append(cLElement.toJSON());
    } 
    stringBuilder.append(" }");
    return stringBuilder.toString();
  }
  
  private class CLObjectIterator implements Iterator {
    int index = 0;
    
    CLObject myObject;
    
    final CLObject this$0;
    
    public CLObjectIterator(CLObject param1CLObject1) {
      this.myObject = param1CLObject1;
    }
    
    public boolean hasNext() {
      boolean bool;
      if (this.index < this.myObject.size()) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public Object next() {
      CLKey cLKey = (CLKey)this.myObject.mElements.get(this.index);
      this.index++;
      return cLKey;
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\parser\CLObject.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */