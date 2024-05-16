package androidx.core.provider;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import java.io.FileNotFoundException;
import java.util.List;

public final class DocumentsContractCompat {
  private static final String PATH_TREE = "tree";
  
  public static Uri buildChildDocumentsUri(String paramString1, String paramString2) {
    return (Build.VERSION.SDK_INT >= 21) ? DocumentsContractApi21Impl.buildChildDocumentsUri(paramString1, paramString2) : null;
  }
  
  public static Uri buildChildDocumentsUriUsingTree(Uri paramUri, String paramString) {
    return (Build.VERSION.SDK_INT >= 21) ? DocumentsContractApi21Impl.buildChildDocumentsUriUsingTree(paramUri, paramString) : null;
  }
  
  public static Uri buildDocumentUri(String paramString1, String paramString2) {
    return (Build.VERSION.SDK_INT >= 19) ? DocumentsContractApi19Impl.buildDocumentUri(paramString1, paramString2) : null;
  }
  
  public static Uri buildDocumentUriUsingTree(Uri paramUri, String paramString) {
    return (Build.VERSION.SDK_INT >= 21) ? DocumentsContractApi21Impl.buildDocumentUriUsingTree(paramUri, paramString) : null;
  }
  
  public static Uri buildTreeDocumentUri(String paramString1, String paramString2) {
    return (Build.VERSION.SDK_INT >= 21) ? DocumentsContractApi21Impl.buildTreeDocumentUri(paramString1, paramString2) : null;
  }
  
  public static Uri createDocument(ContentResolver paramContentResolver, Uri paramUri, String paramString1, String paramString2) throws FileNotFoundException {
    return (Build.VERSION.SDK_INT >= 21) ? DocumentsContractApi21Impl.createDocument(paramContentResolver, paramUri, paramString1, paramString2) : null;
  }
  
  public static String getDocumentId(Uri paramUri) {
    return (Build.VERSION.SDK_INT >= 19) ? DocumentsContractApi19Impl.getDocumentId(paramUri) : null;
  }
  
  public static String getTreeDocumentId(Uri paramUri) {
    return (Build.VERSION.SDK_INT >= 21) ? DocumentsContractApi21Impl.getTreeDocumentId(paramUri) : null;
  }
  
  public static boolean isDocumentUri(Context paramContext, Uri paramUri) {
    return (Build.VERSION.SDK_INT >= 19) ? DocumentsContractApi19Impl.isDocumentUri(paramContext, paramUri) : false;
  }
  
  public static boolean isTreeUri(Uri paramUri) {
    List list;
    int i = Build.VERSION.SDK_INT;
    boolean bool = false;
    if (i < 21)
      return false; 
    if (Build.VERSION.SDK_INT < 24) {
      list = paramUri.getPathSegments();
      boolean bool1 = bool;
      if (list.size() >= 2) {
        bool1 = bool;
        if ("tree".equals(list.get(0)))
          bool1 = true; 
      } 
      return bool1;
    } 
    return DocumentsContractApi24Impl.isTreeUri((Uri)list);
  }
  
  public static boolean removeDocument(ContentResolver paramContentResolver, Uri paramUri1, Uri paramUri2) throws FileNotFoundException {
    return (Build.VERSION.SDK_INT >= 24) ? DocumentsContractApi24Impl.removeDocument(paramContentResolver, paramUri1, paramUri2) : ((Build.VERSION.SDK_INT >= 19) ? DocumentsContractApi19Impl.deleteDocument(paramContentResolver, paramUri1) : false);
  }
  
  public static Uri renameDocument(ContentResolver paramContentResolver, Uri paramUri, String paramString) throws FileNotFoundException {
    return (Build.VERSION.SDK_INT >= 21) ? DocumentsContractApi21Impl.renameDocument(paramContentResolver, paramUri, paramString) : null;
  }
  
  public static final class DocumentCompat {
    public static final int FLAG_VIRTUAL_DOCUMENT = 512;
  }
  
  private static class DocumentsContractApi19Impl {
    public static Uri buildDocumentUri(String param1String1, String param1String2) {
      return DocumentsContract.buildDocumentUri(param1String1, param1String2);
    }
    
    static boolean deleteDocument(ContentResolver param1ContentResolver, Uri param1Uri) throws FileNotFoundException {
      return DocumentsContract.deleteDocument(param1ContentResolver, param1Uri);
    }
    
    static String getDocumentId(Uri param1Uri) {
      return DocumentsContract.getDocumentId(param1Uri);
    }
    
    static boolean isDocumentUri(Context param1Context, Uri param1Uri) {
      return DocumentsContract.isDocumentUri(param1Context, param1Uri);
    }
  }
  
  private static class DocumentsContractApi21Impl {
    static Uri buildChildDocumentsUri(String param1String1, String param1String2) {
      return DocumentsContract.buildChildDocumentsUri(param1String1, param1String2);
    }
    
    static Uri buildChildDocumentsUriUsingTree(Uri param1Uri, String param1String) {
      return DocumentsContract.buildChildDocumentsUriUsingTree(param1Uri, param1String);
    }
    
    static Uri buildDocumentUriUsingTree(Uri param1Uri, String param1String) {
      return DocumentsContract.buildDocumentUriUsingTree(param1Uri, param1String);
    }
    
    public static Uri buildTreeDocumentUri(String param1String1, String param1String2) {
      return DocumentsContract.buildTreeDocumentUri(param1String1, param1String2);
    }
    
    static Uri createDocument(ContentResolver param1ContentResolver, Uri param1Uri, String param1String1, String param1String2) throws FileNotFoundException {
      return DocumentsContract.createDocument(param1ContentResolver, param1Uri, param1String1, param1String2);
    }
    
    static String getTreeDocumentId(Uri param1Uri) {
      return DocumentsContract.getTreeDocumentId(param1Uri);
    }
    
    static Uri renameDocument(ContentResolver param1ContentResolver, Uri param1Uri, String param1String) throws FileNotFoundException {
      return DocumentsContract.renameDocument(param1ContentResolver, param1Uri, param1String);
    }
  }
  
  private static class DocumentsContractApi24Impl {
    static boolean isTreeUri(Uri param1Uri) {
      return DocumentsContract.isTreeUri(param1Uri);
    }
    
    static boolean removeDocument(ContentResolver param1ContentResolver, Uri param1Uri1, Uri param1Uri2) throws FileNotFoundException {
      return DocumentsContract.removeDocument(param1ContentResolver, param1Uri1, param1Uri2);
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\provider\DocumentsContractCompat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */