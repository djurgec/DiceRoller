package androidx.constraintlayout.core.parser;

public class CLElement {
  protected static int BASE_INDENT;
  
  protected static int MAX_LINE = 80;
  
  protected long end = Long.MAX_VALUE;
  
  private int line;
  
  protected CLContainer mContainer;
  
  private final char[] mContent;
  
  protected long start = -1L;
  
  static {
    BASE_INDENT = 2;
  }
  
  public CLElement(char[] paramArrayOfchar) {
    this.mContent = paramArrayOfchar;
  }
  
  protected void addIndent(StringBuilder paramStringBuilder, int paramInt) {
    for (byte b = 0; b < paramInt; b++)
      paramStringBuilder.append(' '); 
  }
  
  public String content() {
    String str = new String(this.mContent);
    long l2 = this.end;
    if (l2 != Long.MAX_VALUE) {
      long l = this.start;
      if (l2 >= l)
        return str.substring((int)l, (int)l2 + 1); 
    } 
    long l1 = this.start;
    return str.substring((int)l1, (int)l1 + 1);
  }
  
  public CLElement getContainer() {
    return this.mContainer;
  }
  
  protected String getDebugName() {
    return CLParser.DEBUG ? (getStrClass() + " -> ") : "";
  }
  
  public long getEnd() {
    return this.end;
  }
  
  public float getFloat() {
    return (this instanceof CLNumber) ? ((CLNumber)this).getFloat() : Float.NaN;
  }
  
  public int getInt() {
    return (this instanceof CLNumber) ? ((CLNumber)this).getInt() : 0;
  }
  
  public int getLine() {
    return this.line;
  }
  
  public long getStart() {
    return this.start;
  }
  
  protected String getStrClass() {
    String str = getClass().toString();
    return str.substring(str.lastIndexOf('.') + 1);
  }
  
  public boolean isDone() {
    boolean bool;
    if (this.end != Long.MAX_VALUE) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isStarted() {
    boolean bool;
    if (this.start > -1L) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean notStarted() {
    boolean bool;
    if (this.start == -1L) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void setContainer(CLContainer paramCLContainer) {
    this.mContainer = paramCLContainer;
  }
  
  public void setEnd(long paramLong) {
    if (this.end != Long.MAX_VALUE)
      return; 
    this.end = paramLong;
    if (CLParser.DEBUG)
      System.out.println("closing " + hashCode() + " -> " + this); 
    CLContainer cLContainer = this.mContainer;
    if (cLContainer != null)
      cLContainer.add(this); 
  }
  
  public void setLine(int paramInt) {
    this.line = paramInt;
  }
  
  public void setStart(long paramLong) {
    this.start = paramLong;
  }
  
  protected String toFormattedJSON(int paramInt1, int paramInt2) {
    return "";
  }
  
  protected String toJSON() {
    return "";
  }
  
  public String toString() {
    long l2 = this.start;
    long l1 = this.end;
    if (l2 > l1 || l1 == Long.MAX_VALUE)
      return getClass() + " (INVALID, " + this.start + "-" + this.end + ")"; 
    String str = (new String(this.mContent)).substring((int)this.start, (int)this.end + 1);
    return getStrClass() + " (" + this.start + " : " + this.end + ") <<" + str + ">>";
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\parser\CLElement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */