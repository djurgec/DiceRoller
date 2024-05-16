package androidx.constraintlayout.core.parser;

public class CLToken extends CLElement {
  int index = 0;
  
  char[] tokenFalse = "false".toCharArray();
  
  char[] tokenNull = "null".toCharArray();
  
  char[] tokenTrue = "true".toCharArray();
  
  Type type = Type.UNKNOWN;
  
  public CLToken(char[] paramArrayOfchar) {
    super(paramArrayOfchar);
  }
  
  public static CLElement allocate(char[] paramArrayOfchar) {
    return new CLToken(paramArrayOfchar);
  }
  
  public boolean getBoolean() throws CLParsingException {
    if (this.type == Type.TRUE)
      return true; 
    if (this.type == Type.FALSE)
      return false; 
    throw new CLParsingException("this token is not a boolean: <" + content() + ">", this);
  }
  
  public Type getType() {
    return this.type;
  }
  
  public boolean isNull() throws CLParsingException {
    if (this.type == Type.NULL)
      return true; 
    throw new CLParsingException("this token is not a null: <" + content() + ">", this);
  }
  
  protected String toFormattedJSON(int paramInt1, int paramInt2) {
    StringBuilder stringBuilder = new StringBuilder();
    addIndent(stringBuilder, paramInt1);
    stringBuilder.append(content());
    return stringBuilder.toString();
  }
  
  protected String toJSON() {
    return CLParser.DEBUG ? ("<" + content() + ">") : content();
  }
  
  public boolean validate(char paramChar, long paramLong) {
    boolean bool4 = false;
    int i = null.$SwitchMap$androidx$constraintlayout$core$parser$CLToken$Type[this.type.ordinal()];
    boolean bool3 = false;
    boolean bool2 = false;
    boolean bool1 = false;
    switch (i) {
      default:
        bool1 = bool4;
        this.index++;
        return bool1;
      case 4:
        arrayOfChar = this.tokenTrue;
        i = this.index;
        if (arrayOfChar[i] == paramChar) {
          this.type = Type.TRUE;
          bool1 = true;
        } else if (this.tokenFalse[i] == paramChar) {
          this.type = Type.FALSE;
          bool1 = true;
        } else {
          bool1 = bool4;
          if (this.tokenNull[i] == paramChar) {
            this.type = Type.NULL;
            bool1 = true;
          } 
        } 
        this.index++;
        return bool1;
      case 3:
        arrayOfChar = this.tokenNull;
        i = this.index;
        if (arrayOfChar[i] == paramChar)
          bool1 = true; 
        bool2 = bool1;
        bool1 = bool2;
        if (bool2) {
          bool1 = bool2;
          if (i + 1 == arrayOfChar.length) {
            setEnd(paramLong);
            bool1 = bool2;
          } 
        } 
        this.index++;
        return bool1;
      case 2:
        arrayOfChar = this.tokenFalse;
        i = this.index;
        bool1 = bool3;
        if (arrayOfChar[i] == paramChar)
          bool1 = true; 
        bool2 = bool1;
        bool1 = bool2;
        if (bool2) {
          bool1 = bool2;
          if (i + 1 == arrayOfChar.length) {
            setEnd(paramLong);
            bool1 = bool2;
          } 
        } 
        this.index++;
        return bool1;
      case 1:
        break;
    } 
    char[] arrayOfChar = this.tokenTrue;
    i = this.index;
    bool1 = bool2;
    if (arrayOfChar[i] == paramChar)
      bool1 = true; 
    bool2 = bool1;
    bool1 = bool2;
    if (bool2) {
      bool1 = bool2;
      if (i + 1 == arrayOfChar.length) {
        setEnd(paramLong);
        bool1 = bool2;
      } 
    } 
    this.index++;
    return bool1;
  }
  
  enum Type {
    FALSE, NULL, TRUE, UNKNOWN;
    
    private static final Type[] $VALUES;
    
    static {
      Type type4 = new Type("UNKNOWN", 0);
      UNKNOWN = type4;
      Type type2 = new Type("TRUE", 1);
      TRUE = type2;
      Type type3 = new Type("FALSE", 2);
      FALSE = type3;
      Type type1 = new Type("NULL", 3);
      NULL = type1;
      $VALUES = new Type[] { type4, type2, type3, type1 };
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\parser\CLToken.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */