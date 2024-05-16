package androidx.constraintlayout.core.parser;

public class CLParsingException extends Exception {
  private final String mElementClass;
  
  private final int mLineNumber;
  
  private final String mReason;
  
  public CLParsingException(String paramString, CLElement paramCLElement) {
    this.mReason = paramString;
    if (paramCLElement != null) {
      this.mElementClass = paramCLElement.getStrClass();
      this.mLineNumber = paramCLElement.getLine();
    } else {
      this.mElementClass = "unknown";
      this.mLineNumber = 0;
    } 
  }
  
  public String reason() {
    return this.mReason + " (" + this.mElementClass + " at line " + this.mLineNumber + ")";
  }
  
  public String toString() {
    return "CLParsingException (" + hashCode() + ") : " + reason();
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\parser\CLParsingException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */