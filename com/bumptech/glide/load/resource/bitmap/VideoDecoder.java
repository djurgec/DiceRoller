package com.bumptech.glide.load.resource.bitmap;

import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.MediaDataSource;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import com.bumptech.glide.load.Option;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;

public class VideoDecoder<T> implements ResourceDecoder<T, Bitmap> {
  private static final MediaMetadataRetrieverFactory DEFAULT_FACTORY;
  
  public static final long DEFAULT_FRAME = -1L;
  
  static final int DEFAULT_FRAME_OPTION = 2;
  
  public static final Option<Integer> FRAME_OPTION;
  
  private static final String TAG = "VideoDecoder";
  
  public static final Option<Long> TARGET_FRAME = Option.disk("com.bumptech.glide.load.resource.bitmap.VideoBitmapDecode.TargetFrame", Long.valueOf(-1L), new Option.CacheKeyUpdater<Long>() {
        private final ByteBuffer buffer = ByteBuffer.allocate(8);
        
        public void update(byte[] param1ArrayOfbyte, Long param1Long, MessageDigest param1MessageDigest) {
          param1MessageDigest.update(param1ArrayOfbyte);
          synchronized (this.buffer) {
            this.buffer.position(0);
            param1MessageDigest.update(this.buffer.putLong(param1Long.longValue()).array());
            return;
          } 
        }
      });
  
  private final BitmapPool bitmapPool;
  
  private final MediaMetadataRetrieverFactory factory;
  
  private final MediaMetadataRetrieverInitializer<T> initializer;
  
  static {
    FRAME_OPTION = Option.disk("com.bumptech.glide.load.resource.bitmap.VideoBitmapDecode.FrameOption", Integer.valueOf(2), new Option.CacheKeyUpdater<Integer>() {
          private final ByteBuffer buffer = ByteBuffer.allocate(4);
          
          public void update(byte[] param1ArrayOfbyte, Integer param1Integer, MessageDigest param1MessageDigest) {
            if (param1Integer == null)
              return; 
            param1MessageDigest.update(param1ArrayOfbyte);
            synchronized (this.buffer) {
              this.buffer.position(0);
              param1MessageDigest.update(this.buffer.putInt(param1Integer.intValue()).array());
              return;
            } 
          }
        });
    DEFAULT_FACTORY = new MediaMetadataRetrieverFactory();
  }
  
  VideoDecoder(BitmapPool paramBitmapPool, MediaMetadataRetrieverInitializer<T> paramMediaMetadataRetrieverInitializer) {
    this(paramBitmapPool, paramMediaMetadataRetrieverInitializer, DEFAULT_FACTORY);
  }
  
  VideoDecoder(BitmapPool paramBitmapPool, MediaMetadataRetrieverInitializer<T> paramMediaMetadataRetrieverInitializer, MediaMetadataRetrieverFactory paramMediaMetadataRetrieverFactory) {
    this.bitmapPool = paramBitmapPool;
    this.initializer = paramMediaMetadataRetrieverInitializer;
    this.factory = paramMediaMetadataRetrieverFactory;
  }
  
  public static ResourceDecoder<AssetFileDescriptor, Bitmap> asset(BitmapPool paramBitmapPool) {
    return new VideoDecoder<>(paramBitmapPool, new AssetFileDescriptorInitializer());
  }
  
  public static ResourceDecoder<ByteBuffer, Bitmap> byteBuffer(BitmapPool paramBitmapPool) {
    return new VideoDecoder<>(paramBitmapPool, new ByteBufferInitializer());
  }
  
  private static Bitmap decodeFrame(MediaMetadataRetriever paramMediaMetadataRetriever, long paramLong, int paramInt1, int paramInt2, int paramInt3, DownsampleStrategy paramDownsampleStrategy) {
    Bitmap bitmap3 = null;
    Bitmap bitmap2 = bitmap3;
    if (Build.VERSION.SDK_INT >= 27) {
      bitmap2 = bitmap3;
      if (paramInt2 != Integer.MIN_VALUE) {
        bitmap2 = bitmap3;
        if (paramInt3 != Integer.MIN_VALUE) {
          bitmap2 = bitmap3;
          if (paramDownsampleStrategy != DownsampleStrategy.NONE)
            bitmap2 = decodeScaledFrame(paramMediaMetadataRetriever, paramLong, paramInt1, paramInt2, paramInt3, paramDownsampleStrategy); 
        } 
      } 
    } 
    Bitmap bitmap1 = bitmap2;
    if (bitmap2 == null)
      bitmap1 = decodeOriginalFrame(paramMediaMetadataRetriever, paramLong, paramInt1); 
    if (bitmap1 != null)
      return bitmap1; 
    throw new VideoDecoderException();
  }
  
  private static Bitmap decodeOriginalFrame(MediaMetadataRetriever paramMediaMetadataRetriever, long paramLong, int paramInt) {
    return paramMediaMetadataRetriever.getFrameAtTime(paramLong, paramInt);
  }
  
  private static Bitmap decodeScaledFrame(MediaMetadataRetriever paramMediaMetadataRetriever, long paramLong, int paramInt1, int paramInt2, int paramInt3, DownsampleStrategy paramDownsampleStrategy) {
    try {
      int i = Integer.parseInt(paramMediaMetadataRetriever.extractMetadata(18));
      int j = Integer.parseInt(paramMediaMetadataRetriever.extractMetadata(19));
      int k = Integer.parseInt(paramMediaMetadataRetriever.extractMetadata(24));
      if (k == 90 || k == 270) {
        k = i;
        i = j;
      } else {
        k = j;
      } 
      try {
        float f = paramDownsampleStrategy.getScaleFactor(i, k, paramInt2, paramInt3);
        return paramMediaMetadataRetriever.getScaledFrameAtTime(paramLong, paramInt1, Math.round(i * f), Math.round(k * f));
      } finally {}
    } finally {}
    if (Log.isLoggable("VideoDecoder", 3))
      Log.d("VideoDecoder", "Exception trying to decode a scaled frame on oreo+, falling back to a fullsize frame", (Throwable)paramMediaMetadataRetriever); 
    return null;
  }
  
  public static ResourceDecoder<ParcelFileDescriptor, Bitmap> parcel(BitmapPool paramBitmapPool) {
    return new VideoDecoder<>(paramBitmapPool, new ParcelFileDescriptorInitializer());
  }
  
  public Resource<Bitmap> decode(T paramT, int paramInt1, int paramInt2, Options paramOptions) throws IOException {
    long l = ((Long)paramOptions.get(TARGET_FRAME)).longValue();
    if (l >= 0L || l == -1L) {
      Integer integer = (Integer)paramOptions.get(FRAME_OPTION);
      if (integer == null)
        integer = Integer.valueOf(2); 
      DownsampleStrategy downsampleStrategy = (DownsampleStrategy)paramOptions.get(DownsampleStrategy.OPTION);
      if (downsampleStrategy == null)
        downsampleStrategy = DownsampleStrategy.DEFAULT; 
      MediaMetadataRetriever mediaMetadataRetriever = this.factory.build();
      try {
        MediaMetadataRetrieverInitializer<T> mediaMetadataRetrieverInitializer = this.initializer;
        try {
          mediaMetadataRetrieverInitializer.initialize(mediaMetadataRetriever, paramT);
          Bitmap bitmap = decodeFrame(mediaMetadataRetriever, l, integer.intValue(), paramInt1, paramInt2, downsampleStrategy);
          mediaMetadataRetriever.release();
          return BitmapResource.obtain(bitmap, this.bitmapPool);
        } finally {}
      } finally {}
      mediaMetadataRetriever.release();
      throw paramT;
    } 
    throw new IllegalArgumentException("Requested frame must be non-negative, or DEFAULT_FRAME, given: " + l);
  }
  
  public boolean handles(T paramT, Options paramOptions) {
    return true;
  }
  
  private static final class AssetFileDescriptorInitializer implements MediaMetadataRetrieverInitializer<AssetFileDescriptor> {
    private AssetFileDescriptorInitializer() {}
    
    public void initialize(MediaMetadataRetriever param1MediaMetadataRetriever, AssetFileDescriptor param1AssetFileDescriptor) {
      param1MediaMetadataRetriever.setDataSource(param1AssetFileDescriptor.getFileDescriptor(), param1AssetFileDescriptor.getStartOffset(), param1AssetFileDescriptor.getLength());
    }
  }
  
  static final class ByteBufferInitializer implements MediaMetadataRetrieverInitializer<ByteBuffer> {
    public void initialize(MediaMetadataRetriever param1MediaMetadataRetriever, final ByteBuffer data) {
      param1MediaMetadataRetriever.setDataSource(new MediaDataSource() {
            final VideoDecoder.ByteBufferInitializer this$0;
            
            final ByteBuffer val$data;
            
            public void close() {}
            
            public long getSize() {
              return data.limit();
            }
            
            public int readAt(long param2Long, byte[] param2ArrayOfbyte, int param2Int1, int param2Int2) {
              if (param2Long >= data.limit())
                return -1; 
              data.position((int)param2Long);
              param2Int2 = Math.min(param2Int2, data.remaining());
              data.get(param2ArrayOfbyte, param2Int1, param2Int2);
              return param2Int2;
            }
          });
    }
  }
  
  class null extends MediaDataSource {
    final VideoDecoder.ByteBufferInitializer this$0;
    
    final ByteBuffer val$data;
    
    public void close() {}
    
    public long getSize() {
      return data.limit();
    }
    
    public int readAt(long param1Long, byte[] param1ArrayOfbyte, int param1Int1, int param1Int2) {
      if (param1Long >= data.limit())
        return -1; 
      data.position((int)param1Long);
      param1Int2 = Math.min(param1Int2, data.remaining());
      data.get(param1ArrayOfbyte, param1Int1, param1Int2);
      return param1Int2;
    }
  }
  
  static class MediaMetadataRetrieverFactory {
    public MediaMetadataRetriever build() {
      return new MediaMetadataRetriever();
    }
  }
  
  static interface MediaMetadataRetrieverInitializer<T> {
    void initialize(MediaMetadataRetriever param1MediaMetadataRetriever, T param1T);
  }
  
  static final class ParcelFileDescriptorInitializer implements MediaMetadataRetrieverInitializer<ParcelFileDescriptor> {
    public void initialize(MediaMetadataRetriever param1MediaMetadataRetriever, ParcelFileDescriptor param1ParcelFileDescriptor) {
      param1MediaMetadataRetriever.setDataSource(param1ParcelFileDescriptor.getFileDescriptor());
    }
  }
  
  private static final class VideoDecoderException extends RuntimeException {
    private static final long serialVersionUID = -2556382523004027815L;
    
    VideoDecoderException() {
      super("MediaMetadataRetriever failed to retrieve a frame without throwing, check the adb logs for .*MetadataRetriever.* prior to this exception for details");
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\resource\bitmap\VideoDecoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */