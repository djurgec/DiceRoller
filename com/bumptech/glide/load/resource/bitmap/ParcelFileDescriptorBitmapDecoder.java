package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import android.os.ParcelFileDescriptor;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import java.io.IOException;

public final class ParcelFileDescriptorBitmapDecoder implements ResourceDecoder<ParcelFileDescriptor, Bitmap> {
  private final Downsampler downsampler;
  
  public ParcelFileDescriptorBitmapDecoder(Downsampler paramDownsampler) {
    this.downsampler = paramDownsampler;
  }
  
  public Resource<Bitmap> decode(ParcelFileDescriptor paramParcelFileDescriptor, int paramInt1, int paramInt2, Options paramOptions) throws IOException {
    return this.downsampler.decode(paramParcelFileDescriptor, paramInt1, paramInt2, paramOptions);
  }
  
  public boolean handles(ParcelFileDescriptor paramParcelFileDescriptor, Options paramOptions) {
    return this.downsampler.handles(paramParcelFileDescriptor);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\resource\bitmap\ParcelFileDescriptorBitmapDecoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */