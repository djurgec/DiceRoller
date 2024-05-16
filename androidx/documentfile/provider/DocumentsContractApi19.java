package androidx.documentfile.provider;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.text.TextUtils;
import android.util.Log;

class DocumentsContractApi19 {
  private static final int FLAG_VIRTUAL_DOCUMENT = 512;
  
  private static final String TAG = "DocumentFile";
  
  public static boolean canRead(Context paramContext, Uri paramUri) {
    return (paramContext.checkCallingOrSelfUriPermission(paramUri, 1) != 0) ? false : (!TextUtils.isEmpty(getRawType(paramContext, paramUri)));
  }
  
  public static boolean canWrite(Context paramContext, Uri paramUri) {
    if (paramContext.checkCallingOrSelfUriPermission(paramUri, 2) != 0)
      return false; 
    String str = getRawType(paramContext, paramUri);
    int i = queryForInt(paramContext, paramUri, "flags", 0);
    return TextUtils.isEmpty(str) ? false : (((i & 0x4) != 0) ? true : (("vnd.android.document/directory".equals(str) && (i & 0x8) != 0) ? true : ((!TextUtils.isEmpty(str) && (i & 0x2) != 0))));
  }
  
  private static void closeQuietly(AutoCloseable paramAutoCloseable) {
    if (paramAutoCloseable != null)
      try {
        paramAutoCloseable.close();
      } catch (RuntimeException runtimeException) {
        throw runtimeException;
      } catch (Exception exception) {} 
  }
  
  public static boolean exists(Context paramContext, Uri paramUri) {
    Cursor cursor1;
    ContentResolver contentResolver = paramContext.getContentResolver();
    Cursor cursor2 = null;
    paramContext = null;
    boolean bool = false;
    try {
      Cursor cursor = contentResolver.query(paramUri, new String[] { "document_id" }, null, null, null);
      cursor1 = cursor;
      cursor2 = cursor;
      int i = cursor.getCount();
      if (i > 0)
        bool = true; 
      closeQuietly((AutoCloseable)cursor);
      return bool;
    } catch (Exception exception) {
      cursor1 = cursor2;
      StringBuilder stringBuilder = new StringBuilder();
      cursor1 = cursor2;
      this();
      cursor1 = cursor2;
      Log.w("DocumentFile", stringBuilder.append("Failed query: ").append(exception).toString());
      closeQuietly((AutoCloseable)cursor2);
      return false;
    } finally {}
    closeQuietly((AutoCloseable)cursor1);
    throw paramUri;
  }
  
  public static long getFlags(Context paramContext, Uri paramUri) {
    return queryForLong(paramContext, paramUri, "flags", 0L);
  }
  
  public static String getName(Context paramContext, Uri paramUri) {
    return queryForString(paramContext, paramUri, "_display_name", null);
  }
  
  private static String getRawType(Context paramContext, Uri paramUri) {
    return queryForString(paramContext, paramUri, "mime_type", null);
  }
  
  public static String getType(Context paramContext, Uri paramUri) {
    String str = getRawType(paramContext, paramUri);
    return "vnd.android.document/directory".equals(str) ? null : str;
  }
  
  public static boolean isDirectory(Context paramContext, Uri paramUri) {
    return "vnd.android.document/directory".equals(getRawType(paramContext, paramUri));
  }
  
  public static boolean isFile(Context paramContext, Uri paramUri) {
    String str = getRawType(paramContext, paramUri);
    return !("vnd.android.document/directory".equals(str) || TextUtils.isEmpty(str));
  }
  
  public static boolean isVirtual(Context paramContext, Uri paramUri) {
    boolean bool1 = DocumentsContract.isDocumentUri(paramContext, paramUri);
    boolean bool = false;
    if (!bool1)
      return false; 
    if ((getFlags(paramContext, paramUri) & 0x200L) != 0L)
      bool = true; 
    return bool;
  }
  
  public static long lastModified(Context paramContext, Uri paramUri) {
    return queryForLong(paramContext, paramUri, "last_modified", 0L);
  }
  
  public static long length(Context paramContext, Uri paramUri) {
    return queryForLong(paramContext, paramUri, "_size", 0L);
  }
  
  private static int queryForInt(Context paramContext, Uri paramUri, String paramString, int paramInt) {
    return (int)queryForLong(paramContext, paramUri, paramString, paramInt);
  }
  
  private static long queryForLong(Context paramContext, Uri paramUri, String paramString, long paramLong) {
    Cursor cursor1;
    ContentResolver contentResolver = paramContext.getContentResolver();
    Cursor cursor2 = null;
    paramContext = null;
    try {
      Cursor cursor = contentResolver.query(paramUri, new String[] { paramString }, null, null, null);
      cursor1 = cursor;
      cursor2 = cursor;
      if (cursor.moveToFirst()) {
        cursor1 = cursor;
        cursor2 = cursor;
        if (!cursor.isNull(0)) {
          cursor1 = cursor;
          cursor2 = cursor;
          long l = cursor.getLong(0);
          closeQuietly((AutoCloseable)cursor);
          return l;
        } 
      } 
      closeQuietly((AutoCloseable)cursor);
      return paramLong;
    } catch (Exception exception) {
      cursor1 = cursor2;
      StringBuilder stringBuilder = new StringBuilder();
      cursor1 = cursor2;
      this();
      cursor1 = cursor2;
      Log.w("DocumentFile", stringBuilder.append("Failed query: ").append(exception).toString());
      closeQuietly((AutoCloseable)cursor2);
      return paramLong;
    } finally {}
    closeQuietly((AutoCloseable)cursor1);
    throw paramUri;
  }
  
  private static String queryForString(Context paramContext, Uri paramUri, String paramString1, String paramString2) {
    Cursor cursor1;
    ContentResolver contentResolver = paramContext.getContentResolver();
    Cursor cursor2 = null;
    paramContext = null;
    try {
      Cursor cursor = contentResolver.query(paramUri, new String[] { paramString1 }, null, null, null);
      cursor1 = cursor;
      cursor2 = cursor;
      if (cursor.moveToFirst()) {
        cursor1 = cursor;
        cursor2 = cursor;
        if (!cursor.isNull(0)) {
          cursor1 = cursor;
          cursor2 = cursor;
          paramString1 = cursor.getString(0);
          closeQuietly((AutoCloseable)cursor);
          return paramString1;
        } 
      } 
      closeQuietly((AutoCloseable)cursor);
      return paramString2;
    } catch (Exception exception) {
      cursor1 = cursor2;
      StringBuilder stringBuilder = new StringBuilder();
      cursor1 = cursor2;
      this();
      cursor1 = cursor2;
      Log.w("DocumentFile", stringBuilder.append("Failed query: ").append(exception).toString());
      closeQuietly((AutoCloseable)cursor2);
      return paramString2;
    } finally {}
    closeQuietly((AutoCloseable)cursor1);
    throw paramUri;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\documentfile\provider\DocumentsContractApi19.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */