package androidx.lifecycle;

import java.util.HashMap;
import java.util.Map;

public class MethodCallsLogger {
  private Map<String, Integer> mCalledMethods = new HashMap<>();
  
  public boolean approveCall(String paramString, int paramInt) {
    int i;
    boolean bool1;
    Integer integer = this.mCalledMethods.get(paramString);
    boolean bool2 = false;
    if (integer != null) {
      i = integer.intValue();
    } else {
      i = 0;
    } 
    if ((i & paramInt) != 0) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    this.mCalledMethods.put(paramString, Integer.valueOf(i | paramInt));
    if (!bool1)
      bool2 = true; 
    return bool2;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\lifecycle\MethodCallsLogger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */