package androidx.core.util;

import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class AtomicFile {
  private static final String LOG_TAG = "AtomicFile";
  
  private final File mBaseName;
  
  private final File mLegacyBackupName;
  
  private final File mNewName;
  
  public AtomicFile(File paramFile) {
    this.mBaseName = paramFile;
    this.mNewName = new File(paramFile.getPath() + ".new");
    this.mLegacyBackupName = new File(paramFile.getPath() + ".bak");
  }
  
  private static void rename(File paramFile1, File paramFile2) {
    if (paramFile2.isDirectory() && !paramFile2.delete())
      Log.e("AtomicFile", "Failed to delete file which is a directory " + paramFile2); 
    if (!paramFile1.renameTo(paramFile2))
      Log.e("AtomicFile", "Failed to rename " + paramFile1 + " to " + paramFile2); 
  }
  
  private static boolean sync(FileOutputStream paramFileOutputStream) {
    try {
      paramFileOutputStream.getFD().sync();
      return true;
    } catch (IOException iOException) {
      return false;
    } 
  }
  
  public void delete() {
    this.mBaseName.delete();
    this.mNewName.delete();
    this.mLegacyBackupName.delete();
  }
  
  public void failWrite(FileOutputStream paramFileOutputStream) {
    if (paramFileOutputStream == null)
      return; 
    if (!sync(paramFileOutputStream))
      Log.e("AtomicFile", "Failed to sync file output stream"); 
    try {
      paramFileOutputStream.close();
    } catch (IOException iOException) {
      Log.e("AtomicFile", "Failed to close file output stream", iOException);
    } 
    if (!this.mNewName.delete())
      Log.e("AtomicFile", "Failed to delete new file " + this.mNewName); 
  }
  
  public void finishWrite(FileOutputStream paramFileOutputStream) {
    if (paramFileOutputStream == null)
      return; 
    if (!sync(paramFileOutputStream))
      Log.e("AtomicFile", "Failed to sync file output stream"); 
    try {
      paramFileOutputStream.close();
    } catch (IOException iOException) {
      Log.e("AtomicFile", "Failed to close file output stream", iOException);
    } 
    rename(this.mNewName, this.mBaseName);
  }
  
  public File getBaseFile() {
    return this.mBaseName;
  }
  
  public FileInputStream openRead() throws FileNotFoundException {
    if (this.mLegacyBackupName.exists())
      rename(this.mLegacyBackupName, this.mBaseName); 
    if (this.mNewName.exists() && this.mBaseName.exists() && !this.mNewName.delete())
      Log.e("AtomicFile", "Failed to delete outdated new file " + this.mNewName); 
    return new FileInputStream(this.mBaseName);
  }
  
  public byte[] readFully() throws IOException {
    FileInputStream fileInputStream = openRead();
    int i = 0;
    try {
    
    } finally {
      fileInputStream.close();
    } 
  }
  
  public FileOutputStream startWrite() throws IOException {
    if (this.mLegacyBackupName.exists())
      rename(this.mLegacyBackupName, this.mBaseName); 
    try {
      return new FileOutputStream(this.mNewName);
    } catch (FileNotFoundException fileNotFoundException) {
      if (this.mNewName.getParentFile().mkdirs())
        try {
          return new FileOutputStream(this.mNewName);
        } catch (FileNotFoundException fileNotFoundException1) {
          throw new IOException("Failed to create new file " + this.mNewName, fileNotFoundException1);
        }  
      throw new IOException("Failed to create directory for " + this.mNewName);
    } 
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\cor\\util\AtomicFile.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */