package androidx.core.content;

import android.content.ContentProvider;
import android.content.Context;

public final class ContentProviderCompat {
  public static Context requireContext(ContentProvider paramContentProvider) {
    Context context = paramContentProvider.getContext();
    if (context != null)
      return context; 
    throw new IllegalStateException("Cannot find context from the provider.");
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\content\ContentProviderCompat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */