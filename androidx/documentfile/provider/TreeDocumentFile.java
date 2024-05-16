package androidx.documentfile.provider;

import android.content.Context;
import android.net.Uri;
import android.provider.DocumentsContract;

class TreeDocumentFile extends DocumentFile {
  private Context mContext;
  
  private Uri mUri;
  
  TreeDocumentFile(DocumentFile paramDocumentFile, Context paramContext, Uri paramUri) {
    super(paramDocumentFile);
    this.mContext = paramContext;
    this.mUri = paramUri;
  }
  
  private static void closeQuietly(AutoCloseable paramAutoCloseable) {
    if (paramAutoCloseable != null)
      try {
        paramAutoCloseable.close();
      } catch (RuntimeException runtimeException) {
        throw runtimeException;
      } catch (Exception exception) {} 
  }
  
  private static Uri createFile(Context paramContext, Uri paramUri, String paramString1, String paramString2) {
    try {
      return DocumentsContract.createDocument(paramContext.getContentResolver(), paramUri, paramString1, paramString2);
    } catch (Exception exception) {
      return null;
    } 
  }
  
  public boolean canRead() {
    return DocumentsContractApi19.canRead(this.mContext, this.mUri);
  }
  
  public boolean canWrite() {
    return DocumentsContractApi19.canWrite(this.mContext, this.mUri);
  }
  
  public DocumentFile createDirectory(String paramString) {
    Uri uri = createFile(this.mContext, this.mUri, "vnd.android.document/directory", paramString);
    if (uri != null) {
      TreeDocumentFile treeDocumentFile = new TreeDocumentFile(this, this.mContext, uri);
    } else {
      uri = null;
    } 
    return (DocumentFile)uri;
  }
  
  public DocumentFile createFile(String paramString1, String paramString2) {
    Uri uri = createFile(this.mContext, this.mUri, paramString1, paramString2);
    if (uri != null) {
      TreeDocumentFile treeDocumentFile = new TreeDocumentFile(this, this.mContext, uri);
    } else {
      uri = null;
    } 
    return (DocumentFile)uri;
  }
  
  public boolean delete() {
    try {
      return DocumentsContract.deleteDocument(this.mContext.getContentResolver(), this.mUri);
    } catch (Exception exception) {
      return false;
    } 
  }
  
  public boolean exists() {
    return DocumentsContractApi19.exists(this.mContext, this.mUri);
  }
  
  public String getName() {
    return DocumentsContractApi19.getName(this.mContext, this.mUri);
  }
  
  public String getType() {
    return DocumentsContractApi19.getType(this.mContext, this.mUri);
  }
  
  public Uri getUri() {
    return this.mUri;
  }
  
  public boolean isDirectory() {
    return DocumentsContractApi19.isDirectory(this.mContext, this.mUri);
  }
  
  public boolean isFile() {
    return DocumentsContractApi19.isFile(this.mContext, this.mUri);
  }
  
  public boolean isVirtual() {
    return DocumentsContractApi19.isVirtual(this.mContext, this.mUri);
  }
  
  public long lastModified() {
    return DocumentsContractApi19.lastModified(this.mContext, this.mUri);
  }
  
  public long length() {
    return DocumentsContractApi19.length(this.mContext, this.mUri);
  }
  
  public DocumentFile[] listFiles() {
    // Byte code:
    //   0: aload_0
    //   1: getfield mContext : Landroid/content/Context;
    //   4: invokevirtual getContentResolver : ()Landroid/content/ContentResolver;
    //   7: astore #4
    //   9: aload_0
    //   10: getfield mUri : Landroid/net/Uri;
    //   13: astore_2
    //   14: aload_2
    //   15: aload_2
    //   16: invokestatic getDocumentId : (Landroid/net/Uri;)Ljava/lang/String;
    //   19: invokestatic buildChildDocumentsUriUsingTree : (Landroid/net/Uri;Ljava/lang/String;)Landroid/net/Uri;
    //   22: astore #6
    //   24: new java/util/ArrayList
    //   27: dup
    //   28: invokespecial <init> : ()V
    //   31: astore #5
    //   33: aconst_null
    //   34: astore_2
    //   35: aconst_null
    //   36: astore_3
    //   37: aload #4
    //   39: aload #6
    //   41: iconst_1
    //   42: anewarray java/lang/String
    //   45: dup
    //   46: iconst_0
    //   47: ldc 'document_id'
    //   49: aastore
    //   50: aconst_null
    //   51: aconst_null
    //   52: aconst_null
    //   53: invokevirtual query : (Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   56: astore #4
    //   58: aload #4
    //   60: astore_3
    //   61: aload #4
    //   63: astore_2
    //   64: aload #4
    //   66: invokeinterface moveToNext : ()Z
    //   71: ifeq -> 114
    //   74: aload #4
    //   76: astore_3
    //   77: aload #4
    //   79: astore_2
    //   80: aload #4
    //   82: iconst_0
    //   83: invokeinterface getString : (I)Ljava/lang/String;
    //   88: astore #6
    //   90: aload #4
    //   92: astore_3
    //   93: aload #4
    //   95: astore_2
    //   96: aload #5
    //   98: aload_0
    //   99: getfield mUri : Landroid/net/Uri;
    //   102: aload #6
    //   104: invokestatic buildDocumentUriUsingTree : (Landroid/net/Uri;Ljava/lang/String;)Landroid/net/Uri;
    //   107: invokevirtual add : (Ljava/lang/Object;)Z
    //   110: pop
    //   111: goto -> 58
    //   114: aload #4
    //   116: astore_2
    //   117: aload_2
    //   118: invokestatic closeQuietly : (Ljava/lang/AutoCloseable;)V
    //   121: goto -> 170
    //   124: astore_2
    //   125: goto -> 227
    //   128: astore #4
    //   130: aload_2
    //   131: astore_3
    //   132: new java/lang/StringBuilder
    //   135: astore #6
    //   137: aload_2
    //   138: astore_3
    //   139: aload #6
    //   141: invokespecial <init> : ()V
    //   144: aload_2
    //   145: astore_3
    //   146: ldc 'DocumentFile'
    //   148: aload #6
    //   150: ldc 'Failed query: '
    //   152: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   155: aload #4
    //   157: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   160: invokevirtual toString : ()Ljava/lang/String;
    //   163: invokestatic w : (Ljava/lang/String;Ljava/lang/String;)I
    //   166: pop
    //   167: goto -> 117
    //   170: aload #5
    //   172: aload #5
    //   174: invokevirtual size : ()I
    //   177: anewarray android/net/Uri
    //   180: invokevirtual toArray : ([Ljava/lang/Object;)[Ljava/lang/Object;
    //   183: checkcast [Landroid/net/Uri;
    //   186: astore_3
    //   187: aload_3
    //   188: arraylength
    //   189: anewarray androidx/documentfile/provider/DocumentFile
    //   192: astore_2
    //   193: iconst_0
    //   194: istore_1
    //   195: iload_1
    //   196: aload_3
    //   197: arraylength
    //   198: if_icmpge -> 225
    //   201: aload_2
    //   202: iload_1
    //   203: new androidx/documentfile/provider/TreeDocumentFile
    //   206: dup
    //   207: aload_0
    //   208: aload_0
    //   209: getfield mContext : Landroid/content/Context;
    //   212: aload_3
    //   213: iload_1
    //   214: aaload
    //   215: invokespecial <init> : (Landroidx/documentfile/provider/DocumentFile;Landroid/content/Context;Landroid/net/Uri;)V
    //   218: aastore
    //   219: iinc #1, 1
    //   222: goto -> 195
    //   225: aload_2
    //   226: areturn
    //   227: aload_3
    //   228: invokestatic closeQuietly : (Ljava/lang/AutoCloseable;)V
    //   231: aload_2
    //   232: athrow
    // Exception table:
    //   from	to	target	type
    //   37	58	128	java/lang/Exception
    //   37	58	124	finally
    //   64	74	128	java/lang/Exception
    //   64	74	124	finally
    //   80	90	128	java/lang/Exception
    //   80	90	124	finally
    //   96	111	128	java/lang/Exception
    //   96	111	124	finally
    //   132	137	124	finally
    //   139	144	124	finally
    //   146	167	124	finally
  }
  
  public boolean renameTo(String paramString) {
    try {
      Uri uri = DocumentsContract.renameDocument(this.mContext.getContentResolver(), this.mUri, paramString);
      if (uri != null) {
        this.mUri = uri;
        return true;
      } 
      return false;
    } catch (Exception exception) {
      return false;
    } 
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\documentfile\provider\TreeDocumentFile.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */