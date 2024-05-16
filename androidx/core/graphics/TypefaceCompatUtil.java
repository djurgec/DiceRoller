package androidx.core.graphics;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.os.Process;
import android.os.StrictMode;
import android.util.Log;
import androidx.core.provider.FontsContractCompat;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TypefaceCompatUtil {
  private static final String CACHE_FILE_PREFIX = ".font";
  
  private static final String TAG = "TypefaceCompatUtil";
  
  public static void closeQuietly(Closeable paramCloseable) {
    if (paramCloseable != null)
      try {
        paramCloseable.close();
      } catch (IOException iOException) {} 
  }
  
  public static ByteBuffer copyToDirectBuffer(Context paramContext, Resources paramResources, int paramInt) {
    File file = getTempFile(paramContext);
    if (file == null)
      return null; 
    try {
      boolean bool = copyToFile(file, paramResources, paramInt);
      if (!bool)
        return null; 
      return mmap(file);
    } finally {
      file.delete();
    } 
  }
  
  public static boolean copyToFile(File paramFile, Resources paramResources, int paramInt) {
    InputStream inputStream = null;
    try {
      InputStream inputStream1 = paramResources.openRawResource(paramInt);
      inputStream = inputStream1;
      return copyToFile(paramFile, inputStream1);
    } finally {
      closeQuietly(inputStream);
    } 
  }
  
  public static boolean copyToFile(File paramFile, InputStream paramInputStream) {
    FileOutputStream fileOutputStream4 = null;
    FileOutputStream fileOutputStream3 = null;
    StrictMode.ThreadPolicy threadPolicy = StrictMode.allowThreadDiskWrites();
    FileOutputStream fileOutputStream1 = fileOutputStream3;
    FileOutputStream fileOutputStream2 = fileOutputStream4;
    try {
      FileOutputStream fileOutputStream6 = new FileOutputStream();
      fileOutputStream1 = fileOutputStream3;
      fileOutputStream2 = fileOutputStream4;
      this(paramFile, false);
      FileOutputStream fileOutputStream5 = fileOutputStream6;
      fileOutputStream1 = fileOutputStream5;
      fileOutputStream2 = fileOutputStream5;
      byte[] arrayOfByte = new byte[1024];
      while (true) {
        fileOutputStream1 = fileOutputStream5;
        fileOutputStream2 = fileOutputStream5;
        int i = paramInputStream.read(arrayOfByte);
        if (i != -1) {
          fileOutputStream1 = fileOutputStream5;
          fileOutputStream2 = fileOutputStream5;
          fileOutputStream5.write(arrayOfByte, 0, i);
          continue;
        } 
        closeQuietly(fileOutputStream5);
        StrictMode.setThreadPolicy(threadPolicy);
        return true;
      } 
    } catch (IOException iOException) {
      fileOutputStream1 = fileOutputStream2;
      StringBuilder stringBuilder = new StringBuilder();
      fileOutputStream1 = fileOutputStream2;
      this();
      fileOutputStream1 = fileOutputStream2;
      Log.e("TypefaceCompatUtil", stringBuilder.append("Error copying resource contents to temp file: ").append(iOException.getMessage()).toString());
      closeQuietly(fileOutputStream2);
      StrictMode.setThreadPolicy(threadPolicy);
      return false;
    } finally {}
    closeQuietly(fileOutputStream1);
    StrictMode.setThreadPolicy(threadPolicy);
    throw paramFile;
  }
  
  public static File getTempFile(Context paramContext) {
    File file = paramContext.getCacheDir();
    if (file == null)
      return null; 
    String str = ".font" + Process.myPid() + "-" + Process.myTid() + "-";
    for (byte b = 0; b < 100; b++) {
      File file1 = new File(file, str + b);
      try {
        boolean bool = file1.createNewFile();
        if (bool)
          return file1; 
      } catch (IOException iOException) {}
    } 
    return null;
  }
  
  public static ByteBuffer mmap(Context paramContext, CancellationSignal paramCancellationSignal, Uri paramUri) {
    ContentResolver contentResolver = paramContext.getContentResolver();
    try {
      ParcelFileDescriptor parcelFileDescriptor = contentResolver.openFileDescriptor(paramUri, "r", paramCancellationSignal);
      if (parcelFileDescriptor == null) {
        if (parcelFileDescriptor != null)
          parcelFileDescriptor.close(); 
        return null;
      } 
      try {
        FileInputStream fileInputStream = new FileInputStream();
        this(parcelFileDescriptor.getFileDescriptor());
      } finally {
        if (parcelFileDescriptor != null)
          try {
            parcelFileDescriptor.close();
          } finally {
            parcelFileDescriptor = null;
          }  
      } 
    } catch (IOException iOException) {
      return null;
    } 
  }
  
  private static ByteBuffer mmap(File paramFile) {
    try {
      FileInputStream fileInputStream = new FileInputStream();
      this(paramFile);
      try {
        FileChannel fileChannel = fileInputStream.getChannel();
        long l = fileChannel.size();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, 0L, l);
      } finally {
        try {
          fileInputStream.close();
        } finally {
          fileInputStream = null;
        } 
      } 
    } catch (IOException iOException) {
      return null;
    } 
  }
  
  public static Map<Uri, ByteBuffer> readFontInfoIntoByteBuffer(Context paramContext, FontsContractCompat.FontInfo[] paramArrayOfFontInfo, CancellationSignal paramCancellationSignal) {
    HashMap<Object, Object> hashMap = new HashMap<>();
    int i = paramArrayOfFontInfo.length;
    for (byte b = 0; b < i; b++) {
      FontsContractCompat.FontInfo fontInfo = paramArrayOfFontInfo[b];
      if (fontInfo.getResultCode() == 0) {
        Uri uri = fontInfo.getUri();
        if (!hashMap.containsKey(uri))
          hashMap.put(uri, mmap(paramContext, paramCancellationSignal, uri)); 
      } 
    } 
    return (Map)Collections.unmodifiableMap(hashMap);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\graphics\TypefaceCompatUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */