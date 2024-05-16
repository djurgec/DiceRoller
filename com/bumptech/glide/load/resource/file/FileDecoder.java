package com.bumptech.glide.load.resource.file;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import java.io.File;
import java.io.IOException;

public class FileDecoder implements ResourceDecoder<File, File> {
  public Resource<File> decode(File paramFile, int paramInt1, int paramInt2, Options paramOptions) {
    return (Resource<File>)new FileResource(paramFile);
  }
  
  public boolean handles(File paramFile, Options paramOptions) {
    return true;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\resource\file\FileDecoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */