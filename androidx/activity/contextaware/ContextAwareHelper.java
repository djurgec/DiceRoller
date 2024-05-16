package androidx.activity.contextaware;

import android.content.Context;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public final class ContextAwareHelper {
  private volatile Context mContext;
  
  private final Set<OnContextAvailableListener> mListeners = new CopyOnWriteArraySet<>();
  
  public void addOnContextAvailableListener(OnContextAvailableListener paramOnContextAvailableListener) {
    if (this.mContext != null)
      paramOnContextAvailableListener.onContextAvailable(this.mContext); 
    this.mListeners.add(paramOnContextAvailableListener);
  }
  
  public void clearAvailableContext() {
    this.mContext = null;
  }
  
  public void dispatchOnContextAvailable(Context paramContext) {
    this.mContext = paramContext;
    Iterator<OnContextAvailableListener> iterator = this.mListeners.iterator();
    while (iterator.hasNext())
      ((OnContextAvailableListener)iterator.next()).onContextAvailable(paramContext); 
  }
  
  public Context peekAvailableContext() {
    return this.mContext;
  }
  
  public void removeOnContextAvailableListener(OnContextAvailableListener paramOnContextAvailableListener) {
    this.mListeners.remove(paramOnContextAvailableListener);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\activity\contextaware\ContextAwareHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */