package androidx.activity.contextaware;

import android.content.Context;

public interface ContextAware {
  void addOnContextAvailableListener(OnContextAvailableListener paramOnContextAvailableListener);
  
  Context peekAvailableContext();
  
  void removeOnContextAvailableListener(OnContextAvailableListener paramOnContextAvailableListener);
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\activity\contextaware\ContextAware.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */