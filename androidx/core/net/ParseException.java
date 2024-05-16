package androidx.core.net;

public class ParseException extends RuntimeException {
  public final String response;
  
  ParseException(String paramString) {
    super(paramString);
    this.response = paramString;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\net\ParseException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */