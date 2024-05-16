package com.bumptech.glide.load.data;

import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import java.io.IOException;

public final class ParcelFileDescriptorRewinder implements DataRewinder<ParcelFileDescriptor> {
  private final InternalRewinder rewinder;
  
  public ParcelFileDescriptorRewinder(ParcelFileDescriptor paramParcelFileDescriptor) {
    this.rewinder = new InternalRewinder(paramParcelFileDescriptor);
  }
  
  public static boolean isSupported() {
    boolean bool;
    if (Build.VERSION.SDK_INT >= 21) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void cleanup() {}
  
  public ParcelFileDescriptor rewindAndGet() throws IOException {
    return this.rewinder.rewind();
  }
  
  public static final class Factory implements DataRewinder.Factory<ParcelFileDescriptor> {
    public DataRewinder<ParcelFileDescriptor> build(ParcelFileDescriptor param1ParcelFileDescriptor) {
      return new ParcelFileDescriptorRewinder(param1ParcelFileDescriptor);
    }
    
    public Class<ParcelFileDescriptor> getDataClass() {
      return ParcelFileDescriptor.class;
    }
  }
  
  private static final class InternalRewinder {
    private final ParcelFileDescriptor parcelFileDescriptor;
    
    InternalRewinder(ParcelFileDescriptor param1ParcelFileDescriptor) {
      this.parcelFileDescriptor = param1ParcelFileDescriptor;
    }
    
    ParcelFileDescriptor rewind() throws IOException {
      try {
        Os.lseek(this.parcelFileDescriptor.getFileDescriptor(), 0L, OsConstants.SEEK_SET);
        return this.parcelFileDescriptor;
      } catch (ErrnoException errnoException) {
        throw new IOException(errnoException);
      } 
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\data\ParcelFileDescriptorRewinder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */