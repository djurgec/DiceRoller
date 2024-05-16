package androidx.core.os;

import androidx.core.util.ObjectsCompat;

public class OperationCanceledException extends RuntimeException {
  public OperationCanceledException() {
    this(null);
  }
  
  public OperationCanceledException(String paramString) {
    super(ObjectsCompat.toString(paramString, "The operation has been canceled."));
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\os\OperationCanceledException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */