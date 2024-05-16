package androidx.startup;

import android.content.Context;
import java.util.List;

public interface Initializer<T> {
  T create(Context paramContext);
  
  List<Class<? extends Initializer<?>>> dependencies();
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\startup\Initializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */