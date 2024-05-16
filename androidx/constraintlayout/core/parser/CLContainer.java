package androidx.constraintlayout.core.parser;

import java.util.ArrayList;

public class CLContainer extends CLElement {
  ArrayList<CLElement> mElements = new ArrayList<>();
  
  public CLContainer(char[] paramArrayOfchar) {
    super(paramArrayOfchar);
  }
  
  public static CLElement allocate(char[] paramArrayOfchar) {
    return new CLContainer(paramArrayOfchar);
  }
  
  public void add(CLElement paramCLElement) {
    this.mElements.add(paramCLElement);
    if (CLParser.DEBUG)
      System.out.println("added element " + paramCLElement + " to " + this); 
  }
  
  public CLElement get(int paramInt) throws CLParsingException {
    if (paramInt >= 0 && paramInt < this.mElements.size())
      return this.mElements.get(paramInt); 
    throw new CLParsingException("no element at index " + paramInt, this);
  }
  
  public CLElement get(String paramString) throws CLParsingException {
    for (CLKey cLKey : this.mElements) {
      if (cLKey.content().equals(paramString))
        return cLKey.getValue(); 
    } 
    throw new CLParsingException("no element for key <" + paramString + ">", this);
  }
  
  public CLArray getArray(int paramInt) throws CLParsingException {
    CLElement cLElement = get(paramInt);
    if (cLElement instanceof CLArray)
      return (CLArray)cLElement; 
    throw new CLParsingException("no array at index " + paramInt, this);
  }
  
  public CLArray getArray(String paramString) throws CLParsingException {
    CLElement cLElement = get(paramString);
    if (cLElement instanceof CLArray)
      return (CLArray)cLElement; 
    throw new CLParsingException("no array found for key <" + paramString + ">, found [" + cLElement.getStrClass() + "] : " + cLElement, this);
  }
  
  public CLArray getArrayOrNull(String paramString) {
    CLElement cLElement = getOrNull(paramString);
    return (cLElement instanceof CLArray) ? (CLArray)cLElement : null;
  }
  
  public boolean getBoolean(int paramInt) throws CLParsingException {
    CLElement cLElement = get(paramInt);
    if (cLElement instanceof CLToken)
      return ((CLToken)cLElement).getBoolean(); 
    throw new CLParsingException("no boolean at index " + paramInt, this);
  }
  
  public boolean getBoolean(String paramString) throws CLParsingException {
    CLElement cLElement = get(paramString);
    if (cLElement instanceof CLToken)
      return ((CLToken)cLElement).getBoolean(); 
    throw new CLParsingException("no boolean found for key <" + paramString + ">, found [" + cLElement.getStrClass() + "] : " + cLElement, this);
  }
  
  public float getFloat(int paramInt) throws CLParsingException {
    CLElement cLElement = get(paramInt);
    if (cLElement != null)
      return cLElement.getFloat(); 
    throw new CLParsingException("no float at index " + paramInt, this);
  }
  
  public float getFloat(String paramString) throws CLParsingException {
    CLElement cLElement = get(paramString);
    if (cLElement != null)
      return cLElement.getFloat(); 
    throw new CLParsingException("no float found for key <" + paramString + ">, found [" + cLElement.getStrClass() + "] : " + cLElement, this);
  }
  
  public float getFloatOrNaN(String paramString) {
    CLElement cLElement = getOrNull(paramString);
    return (cLElement instanceof CLNumber) ? cLElement.getFloat() : Float.NaN;
  }
  
  public int getInt(int paramInt) throws CLParsingException {
    CLElement cLElement = get(paramInt);
    if (cLElement != null)
      return cLElement.getInt(); 
    throw new CLParsingException("no int at index " + paramInt, this);
  }
  
  public int getInt(String paramString) throws CLParsingException {
    CLElement cLElement = get(paramString);
    if (cLElement != null)
      return cLElement.getInt(); 
    throw new CLParsingException("no int found for key <" + paramString + ">, found [" + cLElement.getStrClass() + "] : " + cLElement, this);
  }
  
  public CLObject getObject(int paramInt) throws CLParsingException {
    CLElement cLElement = get(paramInt);
    if (cLElement instanceof CLObject)
      return (CLObject)cLElement; 
    throw new CLParsingException("no object at index " + paramInt, this);
  }
  
  public CLObject getObject(String paramString) throws CLParsingException {
    CLElement cLElement = get(paramString);
    if (cLElement instanceof CLObject)
      return (CLObject)cLElement; 
    throw new CLParsingException("no object found for key <" + paramString + ">, found [" + cLElement.getStrClass() + "] : " + cLElement, this);
  }
  
  public CLObject getObjectOrNull(String paramString) {
    CLElement cLElement = getOrNull(paramString);
    return (cLElement instanceof CLObject) ? (CLObject)cLElement : null;
  }
  
  public CLElement getOrNull(int paramInt) {
    return (paramInt >= 0 && paramInt < this.mElements.size()) ? this.mElements.get(paramInt) : null;
  }
  
  public CLElement getOrNull(String paramString) {
    for (CLKey cLKey : this.mElements) {
      if (cLKey.content().equals(paramString))
        return cLKey.getValue(); 
    } 
    return null;
  }
  
  public String getString(int paramInt) throws CLParsingException {
    CLElement cLElement = get(paramInt);
    if (cLElement instanceof CLString)
      return cLElement.content(); 
    throw new CLParsingException("no string at index " + paramInt, this);
  }
  
  public String getString(String paramString) throws CLParsingException {
    CLElement cLElement = get(paramString);
    if (cLElement instanceof CLString)
      return cLElement.content(); 
    String str = null;
    if (cLElement != null)
      str = cLElement.getStrClass(); 
    throw new CLParsingException("no string found for key <" + paramString + ">, found [" + str + "] : " + cLElement, this);
  }
  
  public String getStringOrNull(int paramInt) {
    CLElement cLElement = getOrNull(paramInt);
    return (cLElement instanceof CLString) ? cLElement.content() : null;
  }
  
  public String getStringOrNull(String paramString) {
    CLElement cLElement = getOrNull(paramString);
    return (cLElement instanceof CLString) ? cLElement.content() : null;
  }
  
  public boolean has(String paramString) {
    for (CLElement cLElement : this.mElements) {
      if (cLElement instanceof CLKey && ((CLKey)cLElement).content().equals(paramString))
        return true; 
    } 
    return false;
  }
  
  public ArrayList<String> names() {
    ArrayList<String> arrayList = new ArrayList();
    for (CLElement cLElement : this.mElements) {
      if (cLElement instanceof CLKey)
        arrayList.add(((CLKey)cLElement).content()); 
    } 
    return arrayList;
  }
  
  public void put(String paramString, CLElement paramCLElement) {
    for (CLKey cLKey1 : this.mElements) {
      if (cLKey1.content().equals(paramString)) {
        cLKey1.set(paramCLElement);
        return;
      } 
    } 
    CLKey cLKey = (CLKey)CLKey.allocate(paramString, paramCLElement);
    this.mElements.add(cLKey);
  }
  
  public void putNumber(String paramString, float paramFloat) {
    put(paramString, new CLNumber(paramFloat));
  }
  
  public void remove(String paramString) {
    ArrayList<CLElement> arrayList = new ArrayList();
    for (CLElement cLElement : this.mElements) {
      if (((CLKey)cLElement).content().equals(paramString))
        arrayList.add(cLElement); 
    } 
    for (CLElement cLElement : arrayList)
      this.mElements.remove(cLElement); 
  }
  
  public int size() {
    return this.mElements.size();
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    for (CLElement cLElement : this.mElements) {
      if (stringBuilder.length() > 0)
        stringBuilder.append("; "); 
      stringBuilder.append(cLElement);
    } 
    return super.toString() + " = <" + stringBuilder + " >";
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\parser\CLContainer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */