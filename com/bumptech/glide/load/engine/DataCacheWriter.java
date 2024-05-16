package com.bumptech.glide.load.engine;

import com.bumptech.glide.load.Encoder;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.engine.cache.DiskCache;
import java.io.File;

class DataCacheWriter<DataType> implements DiskCache.Writer {
  private final DataType data;
  
  private final Encoder<DataType> encoder;
  
  private final Options options;
  
  DataCacheWriter(Encoder<DataType> paramEncoder, DataType paramDataType, Options paramOptions) {
    this.encoder = paramEncoder;
    this.data = paramDataType;
    this.options = paramOptions;
  }
  
  public boolean write(File paramFile) {
    return this.encoder.encode(this.data, paramFile, this.options);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\engine\DataCacheWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */