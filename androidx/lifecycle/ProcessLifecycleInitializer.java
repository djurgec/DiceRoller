package androidx.lifecycle;

import android.content.Context;
import androidx.startup.Initializer;
import java.util.Collections;
import java.util.List;

public final class ProcessLifecycleInitializer implements Initializer<LifecycleOwner> {
  public LifecycleOwner create(Context paramContext) {
    LifecycleDispatcher.init(paramContext);
    ProcessLifecycleOwner.init(paramContext);
    return ProcessLifecycleOwner.get();
  }
  
  public List<Class<? extends Initializer<?>>> dependencies() {
    return Collections.emptyList();
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\lifecycle\ProcessLifecycleInitializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */