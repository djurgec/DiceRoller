package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorSpace;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.util.DisplayMetrics;
import android.util.Log;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.ImageHeaderParser;
import com.bumptech.glide.load.Option;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.PreferredColorSpace;
import com.bumptech.glide.load.data.ParcelFileDescriptorRewinder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.util.LogTime;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.util.Util;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public final class Downsampler {
  public static final Option<Boolean> ALLOW_HARDWARE_CONFIG;
  
  public static final Option<DecodeFormat> DECODE_FORMAT = Option.memory("com.bumptech.glide.load.resource.bitmap.Downsampler.DecodeFormat", DecodeFormat.DEFAULT);
  
  @Deprecated
  public static final Option<DownsampleStrategy> DOWNSAMPLE_STRATEGY;
  
  private static final DecodeCallbacks EMPTY_CALLBACKS;
  
  public static final Option<Boolean> FIX_BITMAP_SIZE_TO_REQUESTED_DIMENSIONS;
  
  private static final String ICO_MIME_TYPE = "image/x-ico";
  
  private static final Set<String> NO_DOWNSAMPLE_PRE_N_MIME_TYPES;
  
  private static final Queue<BitmapFactory.Options> OPTIONS_QUEUE;
  
  public static final Option<PreferredColorSpace> PREFERRED_COLOR_SPACE = Option.memory("com.bumptech.glide.load.resource.bitmap.Downsampler.PreferredColorSpace", PreferredColorSpace.SRGB);
  
  static final String TAG = "Downsampler";
  
  private static final Set<ImageHeaderParser.ImageType> TYPES_THAT_USE_POOL_PRE_KITKAT;
  
  private static final String WBMP_MIME_TYPE = "image/vnd.wap.wbmp";
  
  private final BitmapPool bitmapPool;
  
  private final ArrayPool byteArrayPool;
  
  private final DisplayMetrics displayMetrics;
  
  private final HardwareConfigState hardwareConfigState = HardwareConfigState.getInstance();
  
  private final List<ImageHeaderParser> parsers;
  
  static {
    DOWNSAMPLE_STRATEGY = DownsampleStrategy.OPTION;
    Boolean bool = Boolean.valueOf(false);
    FIX_BITMAP_SIZE_TO_REQUESTED_DIMENSIONS = Option.memory("com.bumptech.glide.load.resource.bitmap.Downsampler.FixBitmapSize", bool);
    ALLOW_HARDWARE_CONFIG = Option.memory("com.bumptech.glide.load.resource.bitmap.Downsampler.AllowHardwareDecode", bool);
    NO_DOWNSAMPLE_PRE_N_MIME_TYPES = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(new String[] { "image/vnd.wap.wbmp", "image/x-ico" })));
    EMPTY_CALLBACKS = new DecodeCallbacks() {
        public void onDecodeComplete(BitmapPool param1BitmapPool, Bitmap param1Bitmap) {}
        
        public void onObtainBounds() {}
      };
    TYPES_THAT_USE_POOL_PRE_KITKAT = Collections.unmodifiableSet(EnumSet.of(ImageHeaderParser.ImageType.JPEG, ImageHeaderParser.ImageType.PNG_A, ImageHeaderParser.ImageType.PNG));
    OPTIONS_QUEUE = Util.createQueue(0);
  }
  
  public Downsampler(List<ImageHeaderParser> paramList, DisplayMetrics paramDisplayMetrics, BitmapPool paramBitmapPool, ArrayPool paramArrayPool) {
    this.parsers = paramList;
    this.displayMetrics = (DisplayMetrics)Preconditions.checkNotNull(paramDisplayMetrics);
    this.bitmapPool = (BitmapPool)Preconditions.checkNotNull(paramBitmapPool);
    this.byteArrayPool = (ArrayPool)Preconditions.checkNotNull(paramArrayPool);
  }
  
  private static int adjustTargetDensityForError(double paramDouble) {
    int j = getDensityMultiplier(paramDouble);
    int i = round(j * paramDouble);
    paramDouble /= (i / j);
    return round(i * paramDouble);
  }
  
  private void calculateConfig(ImageReader paramImageReader, DecodeFormat paramDecodeFormat, boolean paramBoolean1, boolean paramBoolean2, BitmapFactory.Options paramOptions, int paramInt1, int paramInt2) {
    Bitmap.Config config;
    if (this.hardwareConfigState.setHardwareConfigIfAllowed(paramInt1, paramInt2, paramOptions, paramBoolean1, paramBoolean2))
      return; 
    if (paramDecodeFormat == DecodeFormat.PREFER_ARGB_8888 || Build.VERSION.SDK_INT == 16) {
      paramOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
      return;
    } 
    paramBoolean2 = false;
    try {
      paramBoolean1 = paramImageReader.getImageType().hasAlpha();
    } catch (IOException iOException) {
      paramBoolean1 = paramBoolean2;
      if (Log.isLoggable("Downsampler", 3)) {
        Log.d("Downsampler", "Cannot determine whether the image has alpha or not from header, format " + paramDecodeFormat, iOException);
        paramBoolean1 = paramBoolean2;
      } 
    } 
    if (paramBoolean1) {
      config = Bitmap.Config.ARGB_8888;
    } else {
      config = Bitmap.Config.RGB_565;
    } 
    paramOptions.inPreferredConfig = config;
    if (paramOptions.inPreferredConfig == Bitmap.Config.RGB_565)
      paramOptions.inDither = true; 
  }
  
  private static void calculateScaling(ImageHeaderParser.ImageType paramImageType, ImageReader paramImageReader, DecodeCallbacks paramDecodeCallbacks, BitmapPool paramBitmapPool, DownsampleStrategy paramDownsampleStrategy, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, BitmapFactory.Options paramOptions) throws IOException {
    if (paramInt2 <= 0 || paramInt3 <= 0) {
      if (Log.isLoggable("Downsampler", 3))
        Log.d("Downsampler", "Unable to determine dimensions for: " + paramImageType + " with target [" + paramInt4 + "x" + paramInt5 + "]"); 
      return;
    } 
    int j = paramInt2;
    int i = paramInt3;
    if (isRotationRequired(paramInt1)) {
      j = paramInt3;
      i = paramInt2;
    } 
    float f = paramDownsampleStrategy.getScaleFactor(j, i, paramInt4, paramInt5);
    if (f > 0.0F) {
      DownsampleStrategy.SampleSizeRounding sampleSizeRounding = paramDownsampleStrategy.getSampleSizeRounding(j, i, paramInt4, paramInt5);
      if (sampleSizeRounding != null) {
        int k = round((j * f));
        int m = round((i * f));
        k = j / k;
        m = i / m;
        if (sampleSizeRounding == DownsampleStrategy.SampleSizeRounding.MEMORY) {
          k = Math.max(k, m);
        } else {
          k = Math.min(k, m);
        } 
        if (Build.VERSION.SDK_INT <= 23 && NO_DOWNSAMPLE_PRE_N_MIME_TYPES.contains(paramOptions.outMimeType)) {
          k = 1;
        } else {
          m = Math.max(1, Integer.highestOneBit(k));
          k = m;
          if (sampleSizeRounding == DownsampleStrategy.SampleSizeRounding.MEMORY) {
            k = m;
            if (m < 1.0F / f)
              k = m << 1; 
          } 
        } 
        paramOptions.inSampleSize = k;
        if (paramImageType == ImageHeaderParser.ImageType.JPEG) {
          int n = Math.min(k, 8);
          m = (int)Math.ceil((j / n));
          n = (int)Math.ceil((i / n));
          int i1 = k / 8;
          i = m;
          j = n;
          if (i1 > 0) {
            i = m / i1;
            j = n / i1;
          } 
        } else if (paramImageType == ImageHeaderParser.ImageType.PNG || paramImageType == ImageHeaderParser.ImageType.PNG_A) {
          m = (int)Math.floor((j / k));
          j = (int)Math.floor((i / k));
          i = m;
        } else if (paramImageType == ImageHeaderParser.ImageType.WEBP || paramImageType == ImageHeaderParser.ImageType.WEBP_A) {
          if (Build.VERSION.SDK_INT >= 24) {
            m = Math.round(j / k);
            j = Math.round(i / k);
            i = m;
          } else {
            m = (int)Math.floor((j / k));
            j = (int)Math.floor((i / k));
            i = m;
          } 
        } else if (j % k != 0 || i % k != 0) {
          int[] arrayOfInt = getDimensions(paramImageReader, paramOptions, paramDecodeCallbacks, paramBitmapPool);
          i = arrayOfInt[0];
          j = arrayOfInt[1];
        } else {
          j /= k;
          m = i / k;
          i = j;
          j = m;
        } 
        double d = paramDownsampleStrategy.getScaleFactor(i, j, paramInt4, paramInt5);
        if (Build.VERSION.SDK_INT >= 19) {
          paramOptions.inTargetDensity = adjustTargetDensityForError(d);
          paramOptions.inDensity = getDensityMultiplier(d);
        } 
        if (isScaling(paramOptions)) {
          paramOptions.inScaled = true;
        } else {
          paramOptions.inTargetDensity = 0;
          paramOptions.inDensity = 0;
        } 
        if (Log.isLoggable("Downsampler", 2))
          Log.v("Downsampler", "Calculate scaling, source: [" + paramInt2 + "x" + paramInt3 + "], degreesToRotate: " + paramInt1 + ", target: [" + paramInt4 + "x" + paramInt5 + "], power of two scaled: [" + i + "x" + j + "], exact scale factor: " + f + ", power of 2 sample size: " + k + ", adjusted scale factor: " + d + ", target density: " + paramOptions.inTargetDensity + ", density: " + paramOptions.inDensity); 
        return;
      } 
      throw new IllegalArgumentException("Cannot round with null rounding");
    } 
    throw new IllegalArgumentException("Cannot scale with factor: " + f + " from: " + paramDownsampleStrategy + ", source: [" + paramInt2 + "x" + paramInt3 + "], target: [" + paramInt4 + "x" + paramInt5 + "]");
  }
  
  private Resource<Bitmap> decode(ImageReader paramImageReader, int paramInt1, int paramInt2, Options paramOptions, DecodeCallbacks paramDecodeCallbacks) throws IOException {
    boolean bool;
    byte[] arrayOfByte = (byte[])this.byteArrayPool.get(65536, byte[].class);
    BitmapFactory.Options options = getDefaultOptions();
    options.inTempStorage = arrayOfByte;
    DecodeFormat decodeFormat = (DecodeFormat)paramOptions.get(DECODE_FORMAT);
    PreferredColorSpace preferredColorSpace = (PreferredColorSpace)paramOptions.get(PREFERRED_COLOR_SPACE);
    DownsampleStrategy downsampleStrategy = (DownsampleStrategy)paramOptions.get(DownsampleStrategy.OPTION);
    boolean bool1 = ((Boolean)paramOptions.get(FIX_BITMAP_SIZE_TO_REQUESTED_DIMENSIONS)).booleanValue();
    Option<Boolean> option = ALLOW_HARDWARE_CONFIG;
    if (paramOptions.get(option) != null && ((Boolean)paramOptions.get(option)).booleanValue()) {
      bool = true;
    } else {
      bool = false;
    } 
    try {
      return BitmapResource.obtain(decodeFromWrappedStreams(paramImageReader, options, downsampleStrategy, decodeFormat, preferredColorSpace, bool, paramInt1, paramInt2, bool1, paramDecodeCallbacks), this.bitmapPool);
    } finally {
      releaseOptions(options);
      this.byteArrayPool.put(arrayOfByte);
    } 
  }
  
  private Bitmap decodeFromWrappedStreams(ImageReader paramImageReader, BitmapFactory.Options paramOptions, DownsampleStrategy paramDownsampleStrategy, DecodeFormat paramDecodeFormat, PreferredColorSpace paramPreferredColorSpace, boolean paramBoolean1, int paramInt1, int paramInt2, boolean paramBoolean2, DecodeCallbacks paramDecodeCallbacks) throws IOException {
    Bitmap bitmap1;
    int i;
    int j;
    long l = LogTime.getLogTime();
    int[] arrayOfInt = getDimensions(paramImageReader, paramOptions, paramDecodeCallbacks, this.bitmapPool);
    boolean bool = false;
    int m = arrayOfInt[0];
    int k = arrayOfInt[1];
    String str = paramOptions.outMimeType;
    if (m == -1 || k == -1)
      paramBoolean1 = false; 
    int i1 = paramImageReader.getImageOrientation();
    int n = TransformationUtils.getExifOrientationDegrees(i1);
    boolean bool1 = TransformationUtils.isExifOrientationRequired(i1);
    if (paramInt1 == Integer.MIN_VALUE) {
      if (isRotationRequired(n)) {
        i = k;
      } else {
        i = m;
      } 
    } else {
      i = paramInt1;
    } 
    if (paramInt2 == Integer.MIN_VALUE) {
      if (isRotationRequired(n)) {
        j = m;
      } else {
        j = k;
      } 
    } else {
      j = paramInt2;
    } 
    ImageHeaderParser.ImageType imageType = paramImageReader.getImageType();
    calculateScaling(imageType, paramImageReader, paramDecodeCallbacks, this.bitmapPool, paramDownsampleStrategy, n, m, k, i, j, paramOptions);
    calculateConfig(paramImageReader, paramDecodeFormat, paramBoolean1, bool1, paramOptions, i, j);
    if (Build.VERSION.SDK_INT >= 19) {
      n = 1;
    } else {
      n = 0;
    } 
    if ((paramOptions.inSampleSize == 1 || n != 0) && shouldUsePool(imageType)) {
      if (m >= 0 && k >= 0 && paramBoolean2 && n != 0) {
        n = j;
      } else {
        float f;
        if (isScaling(paramOptions)) {
          f = paramOptions.inTargetDensity / paramOptions.inDensity;
        } else {
          f = 1.0F;
        } 
        n = paramOptions.inSampleSize;
        j = (int)Math.ceil((m / n));
        i = (int)Math.ceil((k / n));
        j = Math.round(j * f);
        i = Math.round(i * f);
        if (Log.isLoggable("Downsampler", 2))
          Log.v("Downsampler", "Calculated target [" + j + "x" + i + "] for source [" + m + "x" + k + "], sampleSize: " + n + ", targetDensity: " + paramOptions.inTargetDensity + ", density: " + paramOptions.inDensity + ", density multiplier: " + f); 
        n = i;
        i = j;
      } 
      if (i > 0 && n > 0)
        setInBitmap(paramOptions, this.bitmapPool, i, n); 
    } 
    if (Build.VERSION.SDK_INT >= 28) {
      ColorSpace.Named named;
      if (paramPreferredColorSpace == PreferredColorSpace.DISPLAY_P3 && paramOptions.outColorSpace != null && paramOptions.outColorSpace.isWideGamut()) {
        i = 1;
      } else {
        i = bool;
      } 
      if (i != 0) {
        named = ColorSpace.Named.DISPLAY_P3;
      } else {
        named = ColorSpace.Named.SRGB;
      } 
      paramOptions.inPreferredColorSpace = ColorSpace.get(named);
    } else if (Build.VERSION.SDK_INT >= 26) {
      paramOptions.inPreferredColorSpace = ColorSpace.get(ColorSpace.Named.SRGB);
    } 
    Bitmap bitmap2 = decodeStream(paramImageReader, paramOptions, paramDecodeCallbacks, this.bitmapPool);
    paramDecodeCallbacks.onDecodeComplete(this.bitmapPool, bitmap2);
    if (Log.isLoggable("Downsampler", 2))
      logDecode(m, k, str, paramOptions, bitmap2, paramInt1, paramInt2, l); 
    paramImageReader = null;
    if (bitmap2 != null) {
      bitmap2.setDensity(this.displayMetrics.densityDpi);
      Bitmap bitmap = TransformationUtils.rotateImageExif(this.bitmapPool, bitmap2, i1);
      bitmap1 = bitmap;
      if (!bitmap2.equals(bitmap)) {
        this.bitmapPool.put(bitmap2);
        bitmap1 = bitmap;
      } 
    } 
    return bitmap1;
  }
  
  private static Bitmap decodeStream(ImageReader paramImageReader, BitmapFactory.Options paramOptions, DecodeCallbacks paramDecodeCallbacks, BitmapPool paramBitmapPool) throws IOException {
    if (!paramOptions.inJustDecodeBounds) {
      paramDecodeCallbacks.onObtainBounds();
      paramImageReader.stopGrowingBuffers();
    } 
    int j = paramOptions.outWidth;
    int i = paramOptions.outHeight;
    String str = paramOptions.outMimeType;
    TransformationUtils.getBitmapDrawableLock().lock();
    try {
      Bitmap bitmap = paramImageReader.decodeBitmap(paramOptions);
      TransformationUtils.getBitmapDrawableLock().unlock();
      return bitmap;
    } catch (IllegalArgumentException illegalArgumentException) {
      IOException iOException = newIoExceptionForInBitmapAssertion(illegalArgumentException, j, i, str, paramOptions);
      if (Log.isLoggable("Downsampler", 3))
        Log.d("Downsampler", "Failed to decode with inBitmap, trying again without Bitmap re-use", iOException); 
      Bitmap bitmap = paramOptions.inBitmap;
      if (bitmap != null)
        try {
          paramBitmapPool.put(paramOptions.inBitmap);
          paramOptions.inBitmap = null;
          Bitmap bitmap1 = decodeStream(paramImageReader, paramOptions, paramDecodeCallbacks, paramBitmapPool);
          TransformationUtils.getBitmapDrawableLock().unlock();
          return bitmap1;
        } catch (IOException iOException1) {
          throw iOException;
        }  
      throw iOException;
    } finally {}
    TransformationUtils.getBitmapDrawableLock().unlock();
    throw paramImageReader;
  }
  
  private static String getBitmapString(Bitmap paramBitmap) {
    String str;
    if (paramBitmap == null)
      return null; 
    if (Build.VERSION.SDK_INT >= 19) {
      str = " (" + paramBitmap.getAllocationByteCount() + ")";
    } else {
      str = "";
    } 
    return "[" + paramBitmap.getWidth() + "x" + paramBitmap.getHeight() + "] " + paramBitmap.getConfig() + str;
  }
  
  private static BitmapFactory.Options getDefaultOptions() {
    // Byte code:
    //   0: ldc com/bumptech/glide/load/resource/bitmap/Downsampler
    //   2: monitorenter
    //   3: getstatic com/bumptech/glide/load/resource/bitmap/Downsampler.OPTIONS_QUEUE : Ljava/util/Queue;
    //   6: astore_0
    //   7: aload_0
    //   8: monitorenter
    //   9: aload_0
    //   10: invokeinterface poll : ()Ljava/lang/Object;
    //   15: checkcast android/graphics/BitmapFactory$Options
    //   18: astore_1
    //   19: aload_0
    //   20: monitorexit
    //   21: aload_1
    //   22: astore_0
    //   23: aload_1
    //   24: ifnonnull -> 39
    //   27: new android/graphics/BitmapFactory$Options
    //   30: astore_0
    //   31: aload_0
    //   32: invokespecial <init> : ()V
    //   35: aload_0
    //   36: invokestatic resetOptions : (Landroid/graphics/BitmapFactory$Options;)V
    //   39: ldc com/bumptech/glide/load/resource/bitmap/Downsampler
    //   41: monitorexit
    //   42: aload_0
    //   43: areturn
    //   44: astore_1
    //   45: aload_0
    //   46: monitorexit
    //   47: aload_1
    //   48: athrow
    //   49: astore_0
    //   50: ldc com/bumptech/glide/load/resource/bitmap/Downsampler
    //   52: monitorexit
    //   53: aload_0
    //   54: athrow
    // Exception table:
    //   from	to	target	type
    //   3	9	49	finally
    //   9	21	44	finally
    //   27	35	49	finally
    //   35	39	49	finally
    //   45	47	44	finally
    //   47	49	49	finally
  }
  
  private static int getDensityMultiplier(double paramDouble) {
    if (paramDouble > 1.0D)
      paramDouble = 1.0D / paramDouble; 
    return (int)Math.round(paramDouble * 2.147483647E9D);
  }
  
  private static int[] getDimensions(ImageReader paramImageReader, BitmapFactory.Options paramOptions, DecodeCallbacks paramDecodeCallbacks, BitmapPool paramBitmapPool) throws IOException {
    paramOptions.inJustDecodeBounds = true;
    decodeStream(paramImageReader, paramOptions, paramDecodeCallbacks, paramBitmapPool);
    paramOptions.inJustDecodeBounds = false;
    return new int[] { paramOptions.outWidth, paramOptions.outHeight };
  }
  
  private static String getInBitmapString(BitmapFactory.Options paramOptions) {
    return getBitmapString(paramOptions.inBitmap);
  }
  
  private static boolean isRotationRequired(int paramInt) {
    return (paramInt == 90 || paramInt == 270);
  }
  
  private static boolean isScaling(BitmapFactory.Options paramOptions) {
    boolean bool;
    if (paramOptions.inTargetDensity > 0 && paramOptions.inDensity > 0 && paramOptions.inTargetDensity != paramOptions.inDensity) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private static void logDecode(int paramInt1, int paramInt2, String paramString, BitmapFactory.Options paramOptions, Bitmap paramBitmap, int paramInt3, int paramInt4, long paramLong) {
    Log.v("Downsampler", "Decoded " + getBitmapString(paramBitmap) + " from [" + paramInt1 + "x" + paramInt2 + "] " + paramString + " with inBitmap " + getInBitmapString(paramOptions) + " for [" + paramInt3 + "x" + paramInt4 + "], sample size: " + paramOptions.inSampleSize + ", density: " + paramOptions.inDensity + ", target density: " + paramOptions.inTargetDensity + ", thread: " + Thread.currentThread().getName() + ", duration: " + LogTime.getElapsedMillis(paramLong));
  }
  
  private static IOException newIoExceptionForInBitmapAssertion(IllegalArgumentException paramIllegalArgumentException, int paramInt1, int paramInt2, String paramString, BitmapFactory.Options paramOptions) {
    return new IOException("Exception decoding bitmap, outWidth: " + paramInt1 + ", outHeight: " + paramInt2 + ", outMimeType: " + paramString + ", inBitmap: " + getInBitmapString(paramOptions), paramIllegalArgumentException);
  }
  
  private static void releaseOptions(BitmapFactory.Options paramOptions) {
    resetOptions(paramOptions);
    synchronized (OPTIONS_QUEUE) {
      null.offer(paramOptions);
      return;
    } 
  }
  
  private static void resetOptions(BitmapFactory.Options paramOptions) {
    paramOptions.inTempStorage = null;
    paramOptions.inDither = false;
    paramOptions.inScaled = false;
    paramOptions.inSampleSize = 1;
    paramOptions.inPreferredConfig = null;
    paramOptions.inJustDecodeBounds = false;
    paramOptions.inDensity = 0;
    paramOptions.inTargetDensity = 0;
    if (Build.VERSION.SDK_INT >= 26) {
      paramOptions.inPreferredColorSpace = null;
      paramOptions.outColorSpace = null;
      paramOptions.outConfig = null;
    } 
    paramOptions.outWidth = 0;
    paramOptions.outHeight = 0;
    paramOptions.outMimeType = null;
    paramOptions.inBitmap = null;
    paramOptions.inMutable = true;
  }
  
  private static int round(double paramDouble) {
    return (int)(0.5D + paramDouble);
  }
  
  private static void setInBitmap(BitmapFactory.Options paramOptions, BitmapPool paramBitmapPool, int paramInt1, int paramInt2) {
    Bitmap.Config config1 = null;
    if (Build.VERSION.SDK_INT >= 26) {
      if (paramOptions.inPreferredConfig == Bitmap.Config.HARDWARE)
        return; 
      config1 = paramOptions.outConfig;
    } 
    Bitmap.Config config2 = config1;
    if (config1 == null)
      config2 = paramOptions.inPreferredConfig; 
    paramOptions.inBitmap = paramBitmapPool.getDirty(paramInt1, paramInt2, config2);
  }
  
  private boolean shouldUsePool(ImageHeaderParser.ImageType paramImageType) {
    return (Build.VERSION.SDK_INT >= 19) ? true : TYPES_THAT_USE_POOL_PRE_KITKAT.contains(paramImageType);
  }
  
  public Resource<Bitmap> decode(ParcelFileDescriptor paramParcelFileDescriptor, int paramInt1, int paramInt2, Options paramOptions) throws IOException {
    return decode(new ImageReader.ParcelFileDescriptorImageReader(paramParcelFileDescriptor, this.parsers, this.byteArrayPool), paramInt1, paramInt2, paramOptions, EMPTY_CALLBACKS);
  }
  
  public Resource<Bitmap> decode(InputStream paramInputStream, int paramInt1, int paramInt2, Options paramOptions) throws IOException {
    return decode(paramInputStream, paramInt1, paramInt2, paramOptions, EMPTY_CALLBACKS);
  }
  
  public Resource<Bitmap> decode(InputStream paramInputStream, int paramInt1, int paramInt2, Options paramOptions, DecodeCallbacks paramDecodeCallbacks) throws IOException {
    return decode(new ImageReader.InputStreamImageReader(paramInputStream, this.parsers, this.byteArrayPool), paramInt1, paramInt2, paramOptions, paramDecodeCallbacks);
  }
  
  public boolean handles(ParcelFileDescriptor paramParcelFileDescriptor) {
    return ParcelFileDescriptorRewinder.isSupported();
  }
  
  public boolean handles(InputStream paramInputStream) {
    return true;
  }
  
  public boolean handles(ByteBuffer paramByteBuffer) {
    return true;
  }
  
  public static interface DecodeCallbacks {
    void onDecodeComplete(BitmapPool param1BitmapPool, Bitmap param1Bitmap) throws IOException;
    
    void onObtainBounds();
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\resource\bitmap\Downsampler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */