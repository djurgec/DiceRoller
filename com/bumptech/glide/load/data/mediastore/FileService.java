package com.bumptech.glide.load.data.mediastore;

import java.io.File;

class FileService {
  public boolean exists(File paramFile) {
    return paramFile.exists();
  }
  
  public File get(String paramString) {
    return new File(paramString);
  }
  
  public long length(File paramFile) {
    return paramFile.length();
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\data\mediastore\FileService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */