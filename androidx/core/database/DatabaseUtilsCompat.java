package androidx.core.database;

import android.text.TextUtils;

@Deprecated
public final class DatabaseUtilsCompat {
  @Deprecated
  public static String[] appendSelectionArgs(String[] paramArrayOfString1, String[] paramArrayOfString2) {
    if (paramArrayOfString1 == null || paramArrayOfString1.length == 0)
      return paramArrayOfString2; 
    String[] arrayOfString = new String[paramArrayOfString1.length + paramArrayOfString2.length];
    System.arraycopy(paramArrayOfString1, 0, arrayOfString, 0, paramArrayOfString1.length);
    System.arraycopy(paramArrayOfString2, 0, arrayOfString, paramArrayOfString1.length, paramArrayOfString2.length);
    return arrayOfString;
  }
  
  @Deprecated
  public static String concatenateWhere(String paramString1, String paramString2) {
    return TextUtils.isEmpty(paramString1) ? paramString2 : (TextUtils.isEmpty(paramString2) ? paramString1 : ("(" + paramString1 + ") AND (" + paramString2 + ")"));
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\database\DatabaseUtilsCompat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */