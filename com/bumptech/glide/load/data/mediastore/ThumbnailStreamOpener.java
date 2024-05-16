package com.bumptech.glide.load.data.mediastore;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import com.bumptech.glide.load.ImageHeaderParser;
import com.bumptech.glide.load.ImageHeaderParserUtils;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

class ThumbnailStreamOpener {
  private static final FileService DEFAULT_SERVICE = new FileService();
  
  private static final String TAG = "ThumbStreamOpener";
  
  private final ArrayPool byteArrayPool;
  
  private final ContentResolver contentResolver;
  
  private final List<ImageHeaderParser> parsers;
  
  private final ThumbnailQuery query;
  
  private final FileService service;
  
  ThumbnailStreamOpener(List<ImageHeaderParser> paramList, FileService paramFileService, ThumbnailQuery paramThumbnailQuery, ArrayPool paramArrayPool, ContentResolver paramContentResolver) {
    this.service = paramFileService;
    this.query = paramThumbnailQuery;
    this.byteArrayPool = paramArrayPool;
    this.contentResolver = paramContentResolver;
    this.parsers = paramList;
  }
  
  ThumbnailStreamOpener(List<ImageHeaderParser> paramList, ThumbnailQuery paramThumbnailQuery, ArrayPool paramArrayPool, ContentResolver paramContentResolver) {
    this(paramList, DEFAULT_SERVICE, paramThumbnailQuery, paramArrayPool, paramContentResolver);
  }
  
  private String getPath(Uri paramUri) {
    Cursor cursor2 = null;
    Cursor cursor1 = null;
    try {
      Cursor cursor = this.query.query(paramUri);
      if (cursor != null) {
        cursor1 = cursor;
        cursor2 = cursor;
        if (cursor.moveToFirst()) {
          cursor1 = cursor;
          cursor2 = cursor;
          String str = cursor.getString(0);
          if (cursor != null)
            cursor.close(); 
          return str;
        } 
      } 
      if (cursor != null)
        cursor.close(); 
      return null;
    } catch (SecurityException securityException) {
      cursor1 = cursor2;
      if (Log.isLoggable("ThumbStreamOpener", 3)) {
        cursor1 = cursor2;
        StringBuilder stringBuilder = new StringBuilder();
        cursor1 = cursor2;
        this();
        cursor1 = cursor2;
        Log.d("ThumbStreamOpener", stringBuilder.append("Failed to query for thumbnail for Uri: ").append(paramUri).toString(), securityException);
      } 
      if (cursor2 != null)
        cursor2.close(); 
      return null;
    } finally {}
    if (cursor1 != null)
      cursor1.close(); 
    throw paramUri;
  }
  
  private boolean isValid(File paramFile) {
    boolean bool;
    if (this.service.exists(paramFile) && 0L < this.service.length(paramFile)) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  int getOrientation(Uri paramUri) {
    InputStream inputStream2 = null;
    InputStream inputStream1 = null;
    try {
      InputStream inputStream = this.contentResolver.openInputStream(paramUri);
      inputStream1 = inputStream;
      inputStream2 = inputStream;
      int i = ImageHeaderParserUtils.getOrientation(this.parsers, inputStream, this.byteArrayPool);
      if (inputStream != null)
        try {
          inputStream.close();
        } catch (IOException iOException) {} 
      return i;
    } catch (IOException|NullPointerException iOException1) {
      inputStream1 = inputStream2;
      if (Log.isLoggable("ThumbStreamOpener", 3)) {
        inputStream1 = inputStream2;
        StringBuilder stringBuilder = new StringBuilder();
        inputStream1 = inputStream2;
        this();
        inputStream1 = inputStream2;
        Log.d("ThumbStreamOpener", stringBuilder.append("Failed to open uri: ").append(iOException).toString(), iOException1);
      } 
      if (inputStream2 != null)
        try {
          inputStream2.close();
        } catch (IOException iOException2) {} 
      return -1;
    } finally {}
    if (inputStream1 != null)
      try {
        inputStream1.close();
      } catch (IOException iOException1) {} 
    throw iOException;
  }
  
  public InputStream open(Uri paramUri) throws FileNotFoundException {
    String str = getPath(paramUri);
    if (TextUtils.isEmpty(str))
      return null; 
    File file = this.service.get(str);
    if (!isValid(file))
      return null; 
    Uri uri = Uri.fromFile(file);
    try {
      return this.contentResolver.openInputStream(uri);
    } catch (NullPointerException nullPointerException) {
      throw (FileNotFoundException)(new FileNotFoundException("NPE opening uri: " + paramUri + " -> " + uri)).initCause(nullPointerException);
    } 
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\data\mediastore\ThumbnailStreamOpener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */