package com.bumptech.glide.load.resource;

import android.graphics.ColorSpace;
import android.graphics.ImageDecoder;
import android.os.Build;
import android.util.Log;
import android.util.Size;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.PreferredColorSpace;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy;
import com.bumptech.glide.load.resource.bitmap.Downsampler;
import com.bumptech.glide.load.resource.bitmap.HardwareConfigState;
import java.io.IOException;

public abstract class ImageDecoderResourceDecoder<T> implements ResourceDecoder<ImageDecoder.Source, T> {
  private static final String TAG = "ImageDecoder";
  
  final HardwareConfigState hardwareConfigState = HardwareConfigState.getInstance();
  
  protected abstract Resource<T> decode(ImageDecoder.Source paramSource, int paramInt1, int paramInt2, ImageDecoder.OnHeaderDecodedListener paramOnHeaderDecodedListener) throws IOException;
  
  public final Resource<T> decode(ImageDecoder.Source paramSource, final int requestedWidth, final int requestedHeight, Options paramOptions) throws IOException {
    final boolean isHardwareConfigAllowed;
    final DecodeFormat decodeFormat = (DecodeFormat)paramOptions.get(Downsampler.DECODE_FORMAT);
    final DownsampleStrategy strategy = (DownsampleStrategy)paramOptions.get(DownsampleStrategy.OPTION);
    if (paramOptions.get(Downsampler.ALLOW_HARDWARE_CONFIG) != null && ((Boolean)paramOptions.get(Downsampler.ALLOW_HARDWARE_CONFIG)).booleanValue()) {
      bool = true;
    } else {
      bool = false;
    } 
    return decode(paramSource, requestedWidth, requestedHeight, new ImageDecoder.OnHeaderDecodedListener() {
          final ImageDecoderResourceDecoder this$0;
          
          final DecodeFormat val$decodeFormat;
          
          final boolean val$isHardwareConfigAllowed;
          
          final PreferredColorSpace val$preferredColorSpace;
          
          final int val$requestedHeight;
          
          final int val$requestedWidth;
          
          final DownsampleStrategy val$strategy;
          
          public void onHeaderDecoded(ImageDecoder param1ImageDecoder, ImageDecoder.ImageInfo param1ImageInfo, ImageDecoder.Source param1Source) {
            HardwareConfigState hardwareConfigState = ImageDecoderResourceDecoder.this.hardwareConfigState;
            int i = requestedWidth;
            int j = requestedHeight;
            boolean bool1 = isHardwareConfigAllowed;
            boolean bool = false;
            if (hardwareConfigState.isHardwareConfigAllowed(i, j, bool1, false)) {
              param1ImageDecoder.setAllocator(3);
            } else {
              param1ImageDecoder.setAllocator(1);
            } 
            if (decodeFormat == DecodeFormat.PREFER_RGB_565)
              param1ImageDecoder.setMemorySizePolicy(0); 
            param1ImageDecoder.setOnPartialImageListener(new ImageDecoder.OnPartialImageListener() {
                  final ImageDecoderResourceDecoder.null this$1;
                  
                  public boolean onPartialImage(ImageDecoder.DecodeException param2DecodeException) {
                    return false;
                  }
                });
            Size size = param1ImageInfo.getSize();
            i = requestedWidth;
            if (requestedWidth == Integer.MIN_VALUE)
              i = size.getWidth(); 
            j = requestedHeight;
            if (requestedHeight == Integer.MIN_VALUE)
              j = size.getHeight(); 
            float f = strategy.getScaleFactor(size.getWidth(), size.getHeight(), i, j);
            i = Math.round(size.getWidth() * f);
            j = Math.round(size.getHeight() * f);
            if (Log.isLoggable("ImageDecoder", 2))
              Log.v("ImageDecoder", "Resizing from [" + size.getWidth() + "x" + size.getHeight() + "] to [" + i + "x" + j + "] scaleFactor: " + f); 
            param1ImageDecoder.setTargetSize(i, j);
            if (Build.VERSION.SDK_INT >= 28) {
              ColorSpace.Named named;
              if (preferredColorSpace == PreferredColorSpace.DISPLAY_P3 && param1ImageInfo.getColorSpace() != null && param1ImageInfo.getColorSpace().isWideGamut()) {
                i = 1;
              } else {
                i = bool;
              } 
              if (i != 0) {
                named = ColorSpace.Named.DISPLAY_P3;
              } else {
                named = ColorSpace.Named.SRGB;
              } 
              param1ImageDecoder.setTargetColorSpace(ColorSpace.get(named));
            } else if (Build.VERSION.SDK_INT >= 26) {
              param1ImageDecoder.setTargetColorSpace(ColorSpace.get(ColorSpace.Named.SRGB));
            } 
          }
        });
  }
  
  public final boolean handles(ImageDecoder.Source paramSource, Options paramOptions) {
    return true;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\resource\ImageDecoderResourceDecoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */