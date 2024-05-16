package androidx.core.content;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ProviderInfo;
import android.content.res.XmlResourceParser;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import org.xmlpull.v1.XmlPullParserException;

public class FileProvider extends ContentProvider {
  private static final String ATTR_NAME = "name";
  
  private static final String ATTR_PATH = "path";
  
  private static final String[] COLUMNS = new String[] { "_display_name", "_size" };
  
  private static final File DEVICE_ROOT = new File("/");
  
  private static final String DISPLAYNAME_FIELD = "displayName";
  
  private static final String META_DATA_FILE_PROVIDER_PATHS = "android.support.FILE_PROVIDER_PATHS";
  
  private static final String TAG_CACHE_PATH = "cache-path";
  
  private static final String TAG_EXTERNAL = "external-path";
  
  private static final String TAG_EXTERNAL_CACHE = "external-cache-path";
  
  private static final String TAG_EXTERNAL_FILES = "external-files-path";
  
  private static final String TAG_EXTERNAL_MEDIA = "external-media-path";
  
  private static final String TAG_FILES_PATH = "files-path";
  
  private static final String TAG_ROOT_PATH = "root-path";
  
  private static HashMap<String, PathStrategy> sCache = new HashMap<>();
  
  private PathStrategy mStrategy;
  
  private static File buildPath(File paramFile, String... paramVarArgs) {
    int i = paramVarArgs.length;
    byte b = 0;
    File file;
    for (file = paramFile; b < i; file = paramFile) {
      String str = paramVarArgs[b];
      paramFile = file;
      if (str != null)
        paramFile = new File(file, str); 
      b++;
    } 
    return file;
  }
  
  private static Object[] copyOf(Object[] paramArrayOfObject, int paramInt) {
    Object[] arrayOfObject = new Object[paramInt];
    System.arraycopy(paramArrayOfObject, 0, arrayOfObject, 0, paramInt);
    return arrayOfObject;
  }
  
  private static String[] copyOf(String[] paramArrayOfString, int paramInt) {
    String[] arrayOfString = new String[paramInt];
    System.arraycopy(paramArrayOfString, 0, arrayOfString, 0, paramInt);
    return arrayOfString;
  }
  
  private static PathStrategy getPathStrategy(Context paramContext, String paramString) {
    synchronized (sCache) {
      PathStrategy pathStrategy2 = sCache.get(paramString);
      PathStrategy pathStrategy1 = pathStrategy2;
      if (pathStrategy2 == null)
        try {
          pathStrategy1 = parsePathStrategy(paramContext, paramString);
          sCache.put(paramString, pathStrategy1);
        } catch (IOException iOException) {
          IllegalArgumentException illegalArgumentException = new IllegalArgumentException();
          this("Failed to parse android.support.FILE_PROVIDER_PATHS meta-data", iOException);
          throw illegalArgumentException;
        } catch (XmlPullParserException xmlPullParserException) {
          IllegalArgumentException illegalArgumentException = new IllegalArgumentException();
          this("Failed to parse android.support.FILE_PROVIDER_PATHS meta-data", (Throwable)xmlPullParserException);
          throw illegalArgumentException;
        }  
      return pathStrategy1;
    } 
  }
  
  public static Uri getUriForFile(Context paramContext, String paramString, File paramFile) {
    return getPathStrategy(paramContext, paramString).getUriForFile(paramFile);
  }
  
  public static Uri getUriForFile(Context paramContext, String paramString1, File paramFile, String paramString2) {
    return getUriForFile(paramContext, paramString1, paramFile).buildUpon().appendQueryParameter("displayName", paramString2).build();
  }
  
  private static int modeToMode(String paramString) {
    int i;
    if ("r".equals(paramString)) {
      i = 268435456;
    } else {
      if ("w".equals(paramString) || "wt".equals(paramString))
        return 738197504; 
      if ("wa".equals(paramString)) {
        i = 704643072;
      } else if ("rw".equals(paramString)) {
        i = 939524096;
      } else if ("rwt".equals(paramString)) {
        i = 1006632960;
      } else {
        throw new IllegalArgumentException("Invalid mode: " + paramString);
      } 
    } 
    return i;
  }
  
  private static PathStrategy parsePathStrategy(Context paramContext, String paramString) throws IOException, XmlPullParserException {
    File file;
    SimplePathStrategy simplePathStrategy = new SimplePathStrategy(paramString);
    ProviderInfo providerInfo = paramContext.getPackageManager().resolveContentProvider(paramString, 128);
    if (providerInfo != null) {
      XmlResourceParser xmlResourceParser = providerInfo.loadXmlMetaData(paramContext.getPackageManager(), "android.support.FILE_PROVIDER_PATHS");
      if (xmlResourceParser != null)
        while (true) {
          int i = xmlResourceParser.next();
          if (i != 1) {
            if (i == 2) {
              String str4 = xmlResourceParser.getName();
              String str3 = xmlResourceParser.getAttributeValue(null, "name");
              String str2 = xmlResourceParser.getAttributeValue(null, "path");
              paramString = null;
              providerInfo = null;
              String str1 = null;
              if ("root-path".equals(str4)) {
                file = DEVICE_ROOT;
              } else if ("files-path".equals(str4)) {
                file = paramContext.getFilesDir();
              } else if ("cache-path".equals(str4)) {
                file = paramContext.getCacheDir();
              } else if ("external-path".equals(str4)) {
                file = Environment.getExternalStorageDirectory();
              } else if ("external-files-path".equals(str4)) {
                File[] arrayOfFile = ContextCompat.getExternalFilesDirs(paramContext, null);
                paramString = str1;
                if (arrayOfFile.length > 0)
                  file = arrayOfFile[0]; 
              } else {
                File[] arrayOfFile;
                if ("external-cache-path".equals(str4)) {
                  arrayOfFile = ContextCompat.getExternalCacheDirs(paramContext);
                  if (arrayOfFile.length > 0)
                    file = arrayOfFile[0]; 
                } else if (Build.VERSION.SDK_INT >= 21) {
                  File[] arrayOfFile1 = arrayOfFile;
                  if ("external-media-path".equals(str4)) {
                    File[] arrayOfFile2 = paramContext.getExternalMediaDirs();
                    arrayOfFile1 = arrayOfFile;
                    if (arrayOfFile2.length > 0)
                      file = arrayOfFile2[0]; 
                  } 
                } 
              } 
              if (file != null)
                simplePathStrategy.addRoot(str3, buildPath(file, new String[] { str2 })); 
            } 
            continue;
          } 
          return simplePathStrategy;
        }  
      throw new IllegalArgumentException("Missing android.support.FILE_PROVIDER_PATHS meta-data");
    } 
    throw new IllegalArgumentException("Couldn't find meta-data for provider with authority " + file);
  }
  
  public void attachInfo(Context paramContext, ProviderInfo paramProviderInfo) {
    super.attachInfo(paramContext, paramProviderInfo);
    if (!paramProviderInfo.exported) {
      if (paramProviderInfo.grantUriPermissions) {
        this.mStrategy = getPathStrategy(paramContext, paramProviderInfo.authority.split(";")[0]);
        return;
      } 
      throw new SecurityException("Provider must grant uri permissions");
    } 
    throw new SecurityException("Provider must not be exported");
  }
  
  public int delete(Uri paramUri, String paramString, String[] paramArrayOfString) {
    return this.mStrategy.getFileForUri(paramUri).delete();
  }
  
  public String getType(Uri paramUri) {
    File file = this.mStrategy.getFileForUri(paramUri);
    int i = file.getName().lastIndexOf('.');
    if (i >= 0) {
      String str = file.getName().substring(i + 1);
      str = MimeTypeMap.getSingleton().getMimeTypeFromExtension(str);
      if (str != null)
        return str; 
    } 
    return "application/octet-stream";
  }
  
  public Uri insert(Uri paramUri, ContentValues paramContentValues) {
    throw new UnsupportedOperationException("No external inserts");
  }
  
  public boolean onCreate() {
    return true;
  }
  
  public ParcelFileDescriptor openFile(Uri paramUri, String paramString) throws FileNotFoundException {
    return ParcelFileDescriptor.open(this.mStrategy.getFileForUri(paramUri), modeToMode(paramString));
  }
  
  public Cursor query(Uri paramUri, String[] paramArrayOfString1, String paramString1, String[] paramArrayOfString2, String paramString2) {
    File file = this.mStrategy.getFileForUri(paramUri);
    paramString1 = paramUri.getQueryParameter("displayName");
    if (paramArrayOfString1 == null) {
      arrayOfString1 = COLUMNS;
    } else {
      arrayOfString1 = paramArrayOfString1;
    } 
    String[] arrayOfString2 = new String[arrayOfString1.length];
    Object[] arrayOfObject2 = new Object[arrayOfString1.length];
    int i = 0;
    int j = arrayOfString1.length;
    byte b = 0;
    while (b < j) {
      int k;
      String str = arrayOfString1[b];
      if ("_display_name".equals(str)) {
        arrayOfString2[i] = "_display_name";
        if (paramString1 == null) {
          str = file.getName();
        } else {
          str = paramString1;
        } 
        arrayOfObject2[i] = str;
        k = i + 1;
      } else {
        k = i;
        if ("_size".equals(str)) {
          arrayOfString2[i] = "_size";
          arrayOfObject2[i] = Long.valueOf(file.length());
          k = i + 1;
        } 
      } 
      b++;
      i = k;
    } 
    String[] arrayOfString1 = copyOf(arrayOfString2, i);
    Object[] arrayOfObject1 = copyOf(arrayOfObject2, i);
    MatrixCursor matrixCursor = new MatrixCursor(arrayOfString1, 1);
    matrixCursor.addRow(arrayOfObject1);
    return (Cursor)matrixCursor;
  }
  
  public int update(Uri paramUri, ContentValues paramContentValues, String paramString, String[] paramArrayOfString) {
    throw new UnsupportedOperationException("No external updates");
  }
  
  static interface PathStrategy {
    File getFileForUri(Uri param1Uri);
    
    Uri getUriForFile(File param1File);
  }
  
  static class SimplePathStrategy implements PathStrategy {
    private final String mAuthority;
    
    private final HashMap<String, File> mRoots = new HashMap<>();
    
    SimplePathStrategy(String param1String) {
      this.mAuthority = param1String;
    }
    
    void addRoot(String param1String, File param1File) {
      if (!TextUtils.isEmpty(param1String))
        try {
          File file = param1File.getCanonicalFile();
          this.mRoots.put(param1String, file);
          return;
        } catch (IOException iOException) {
          throw new IllegalArgumentException("Failed to resolve canonical path for " + param1File, iOException);
        }  
      throw new IllegalArgumentException("Name must not be empty");
    }
    
    public File getFileForUri(Uri param1Uri) {
      File file1;
      String str2 = param1Uri.getEncodedPath();
      int i = str2.indexOf('/', 1);
      String str1 = Uri.decode(str2.substring(1, i));
      str2 = Uri.decode(str2.substring(i + 1));
      File file2 = this.mRoots.get(str1);
      if (file2 != null) {
        file1 = new File(file2, str2);
        try {
          File file = file1.getCanonicalFile();
          if (file.getPath().startsWith(file2.getPath()))
            return file; 
          throw new SecurityException("Resolved path jumped beyond configured root");
        } catch (IOException iOException) {
          throw new IllegalArgumentException("Failed to resolve canonical path for " + file1);
        } 
      } 
      throw new IllegalArgumentException("Unable to find configured root for " + file1);
    }
    
    public Uri getUriForFile(File param1File) {
      // Byte code:
      //   0: aload_1
      //   1: invokevirtual getCanonicalPath : ()Ljava/lang/String;
      //   4: astore #4
      //   6: aconst_null
      //   7: astore_1
      //   8: aload_0
      //   9: getfield mRoots : Ljava/util/HashMap;
      //   12: invokevirtual entrySet : ()Ljava/util/Set;
      //   15: invokeinterface iterator : ()Ljava/util/Iterator;
      //   20: astore #5
      //   22: aload #5
      //   24: invokeinterface hasNext : ()Z
      //   29: ifeq -> 105
      //   32: aload #5
      //   34: invokeinterface next : ()Ljava/lang/Object;
      //   39: checkcast java/util/Map$Entry
      //   42: astore_3
      //   43: aload_3
      //   44: invokeinterface getValue : ()Ljava/lang/Object;
      //   49: checkcast java/io/File
      //   52: invokevirtual getPath : ()Ljava/lang/String;
      //   55: astore #6
      //   57: aload_1
      //   58: astore_2
      //   59: aload #4
      //   61: aload #6
      //   63: invokevirtual startsWith : (Ljava/lang/String;)Z
      //   66: ifeq -> 100
      //   69: aload_1
      //   70: ifnull -> 98
      //   73: aload_1
      //   74: astore_2
      //   75: aload #6
      //   77: invokevirtual length : ()I
      //   80: aload_1
      //   81: invokeinterface getValue : ()Ljava/lang/Object;
      //   86: checkcast java/io/File
      //   89: invokevirtual getPath : ()Ljava/lang/String;
      //   92: invokevirtual length : ()I
      //   95: if_icmple -> 100
      //   98: aload_3
      //   99: astore_2
      //   100: aload_2
      //   101: astore_1
      //   102: goto -> 22
      //   105: aload_1
      //   106: ifnull -> 223
      //   109: aload_1
      //   110: invokeinterface getValue : ()Ljava/lang/Object;
      //   115: checkcast java/io/File
      //   118: invokevirtual getPath : ()Ljava/lang/String;
      //   121: astore_2
      //   122: aload_2
      //   123: ldc '/'
      //   125: invokevirtual endsWith : (Ljava/lang/String;)Z
      //   128: ifeq -> 144
      //   131: aload #4
      //   133: aload_2
      //   134: invokevirtual length : ()I
      //   137: invokevirtual substring : (I)Ljava/lang/String;
      //   140: astore_2
      //   141: goto -> 156
      //   144: aload #4
      //   146: aload_2
      //   147: invokevirtual length : ()I
      //   150: iconst_1
      //   151: iadd
      //   152: invokevirtual substring : (I)Ljava/lang/String;
      //   155: astore_2
      //   156: new java/lang/StringBuilder
      //   159: dup
      //   160: invokespecial <init> : ()V
      //   163: aload_1
      //   164: invokeinterface getKey : ()Ljava/lang/Object;
      //   169: checkcast java/lang/String
      //   172: invokestatic encode : (Ljava/lang/String;)Ljava/lang/String;
      //   175: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   178: bipush #47
      //   180: invokevirtual append : (C)Ljava/lang/StringBuilder;
      //   183: aload_2
      //   184: ldc '/'
      //   186: invokestatic encode : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
      //   189: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   192: invokevirtual toString : ()Ljava/lang/String;
      //   195: astore_1
      //   196: new android/net/Uri$Builder
      //   199: dup
      //   200: invokespecial <init> : ()V
      //   203: ldc 'content'
      //   205: invokevirtual scheme : (Ljava/lang/String;)Landroid/net/Uri$Builder;
      //   208: aload_0
      //   209: getfield mAuthority : Ljava/lang/String;
      //   212: invokevirtual authority : (Ljava/lang/String;)Landroid/net/Uri$Builder;
      //   215: aload_1
      //   216: invokevirtual encodedPath : (Ljava/lang/String;)Landroid/net/Uri$Builder;
      //   219: invokevirtual build : ()Landroid/net/Uri;
      //   222: areturn
      //   223: new java/lang/IllegalArgumentException
      //   226: dup
      //   227: new java/lang/StringBuilder
      //   230: dup
      //   231: invokespecial <init> : ()V
      //   234: ldc 'Failed to find configured root that contains '
      //   236: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   239: aload #4
      //   241: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   244: invokevirtual toString : ()Ljava/lang/String;
      //   247: invokespecial <init> : (Ljava/lang/String;)V
      //   250: athrow
      //   251: astore_2
      //   252: new java/lang/IllegalArgumentException
      //   255: dup
      //   256: new java/lang/StringBuilder
      //   259: dup
      //   260: invokespecial <init> : ()V
      //   263: ldc 'Failed to resolve canonical path for '
      //   265: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   268: aload_1
      //   269: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
      //   272: invokevirtual toString : ()Ljava/lang/String;
      //   275: invokespecial <init> : (Ljava/lang/String;)V
      //   278: athrow
      // Exception table:
      //   from	to	target	type
      //   0	6	251	java/io/IOException
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\content\FileProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */