package androidx.startup;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public final class InitializationProvider extends ContentProvider {
  public int delete(Uri paramUri, String paramString, String[] paramArrayOfString) {
    throw new IllegalStateException("Not allowed.");
  }
  
  public String getType(Uri paramUri) {
    throw new IllegalStateException("Not allowed.");
  }
  
  public Uri insert(Uri paramUri, ContentValues paramContentValues) {
    throw new IllegalStateException("Not allowed.");
  }
  
  public boolean onCreate() {
    Context context = getContext();
    if (context != null) {
      AppInitializer.getInstance(context).discoverAndInitialize();
      return true;
    } 
    throw new StartupException("Context cannot be null");
  }
  
  public Cursor query(Uri paramUri, String[] paramArrayOfString1, String paramString1, String[] paramArrayOfString2, String paramString2) {
    throw new IllegalStateException("Not allowed.");
  }
  
  public int update(Uri paramUri, ContentValues paramContentValues, String paramString, String[] paramArrayOfString) {
    throw new IllegalStateException("Not allowed.");
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\startup\InitializationProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */