package com.bumptech.glide.disklrucache;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.nio.charset.Charset;

final class Util {
  static final Charset US_ASCII = Charset.forName("US-ASCII");
  
  static final Charset UTF_8 = Charset.forName("UTF-8");
  
  static void closeQuietly(Closeable paramCloseable) {
    if (paramCloseable != null)
      try {
        paramCloseable.close();
      } catch (RuntimeException runtimeException) {
        throw runtimeException;
      } catch (Exception exception) {} 
  }
  
  static void deleteContents(File paramFile) throws IOException {
    File[] arrayOfFile = paramFile.listFiles();
    if (arrayOfFile != null) {
      int i = arrayOfFile.length;
      byte b = 0;
      while (b < i) {
        paramFile = arrayOfFile[b];
        if (paramFile.isDirectory())
          deleteContents(paramFile); 
        if (paramFile.delete()) {
          b++;
          continue;
        } 
        throw new IOException("failed to delete file: " + paramFile);
      } 
      return;
    } 
    throw new IOException("not a readable directory: " + paramFile);
  }
  
  static String readFully(Reader paramReader) throws IOException {
    try {
      StringWriter stringWriter = new StringWriter();
      this();
      char[] arrayOfChar = new char[1024];
      while (true) {
        int i = paramReader.read(arrayOfChar);
        if (i != -1) {
          stringWriter.write(arrayOfChar, 0, i);
          continue;
        } 
        return stringWriter.toString();
      } 
    } finally {
      paramReader.close();
    } 
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\disklrucache\Util.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */