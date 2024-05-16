package com.bumptech.glide.load.model;

import com.bumptech.glide.load.Encoder;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import java.io.File;
import java.io.InputStream;

public class StreamEncoder implements Encoder<InputStream> {
  private static final String TAG = "StreamEncoder";
  
  private final ArrayPool byteArrayPool;
  
  public StreamEncoder(ArrayPool paramArrayPool) {
    this.byteArrayPool = paramArrayPool;
  }
  
  public boolean encode(InputStream paramInputStream, File paramFile, Options paramOptions) {
    // Byte code:
    //   0: aload_0
    //   1: getfield byteArrayPool : Lcom/bumptech/glide/load/engine/bitmap_recycle/ArrayPool;
    //   4: ldc 65536
    //   6: ldc [B
    //   8: invokeinterface get : (ILjava/lang/Class;)Ljava/lang/Object;
    //   13: checkcast [B
    //   16: astore #12
    //   18: iconst_0
    //   19: istore #6
    //   21: iconst_0
    //   22: istore #7
    //   24: aconst_null
    //   25: astore #10
    //   27: aconst_null
    //   28: astore #9
    //   30: aload #9
    //   32: astore_3
    //   33: aload #10
    //   35: astore #8
    //   37: new java/io/FileOutputStream
    //   40: astore #11
    //   42: aload #9
    //   44: astore_3
    //   45: aload #10
    //   47: astore #8
    //   49: aload #11
    //   51: aload_2
    //   52: invokespecial <init> : (Ljava/io/File;)V
    //   55: aload #11
    //   57: astore_2
    //   58: aload_2
    //   59: astore_3
    //   60: aload_2
    //   61: astore #8
    //   63: aload_1
    //   64: aload #12
    //   66: invokevirtual read : ([B)I
    //   69: istore #4
    //   71: iload #4
    //   73: iconst_m1
    //   74: if_icmpeq -> 94
    //   77: aload_2
    //   78: astore_3
    //   79: aload_2
    //   80: astore #8
    //   82: aload_2
    //   83: aload #12
    //   85: iconst_0
    //   86: iload #4
    //   88: invokevirtual write : ([BII)V
    //   91: goto -> 58
    //   94: aload_2
    //   95: astore_3
    //   96: aload_2
    //   97: astore #8
    //   99: aload_2
    //   100: invokevirtual close : ()V
    //   103: iconst_1
    //   104: istore #5
    //   106: iconst_1
    //   107: istore #6
    //   109: aload_2
    //   110: invokevirtual close : ()V
    //   113: iload #6
    //   115: istore #5
    //   117: goto -> 171
    //   120: astore_1
    //   121: goto -> 179
    //   124: astore_1
    //   125: goto -> 193
    //   128: astore_1
    //   129: aload #8
    //   131: astore_3
    //   132: ldc 'StreamEncoder'
    //   134: iconst_3
    //   135: invokestatic isLoggable : (Ljava/lang/String;I)Z
    //   138: ifeq -> 153
    //   141: aload #8
    //   143: astore_3
    //   144: ldc 'StreamEncoder'
    //   146: ldc 'Failed to encode data onto the OutputStream'
    //   148: aload_1
    //   149: invokestatic d : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   152: pop
    //   153: iload #6
    //   155: istore #5
    //   157: aload #8
    //   159: ifnull -> 179
    //   162: aload #8
    //   164: invokevirtual close : ()V
    //   167: iload #7
    //   169: istore #5
    //   171: goto -> 179
    //   174: astore_1
    //   175: iload #6
    //   177: istore #5
    //   179: aload_0
    //   180: getfield byteArrayPool : Lcom/bumptech/glide/load/engine/bitmap_recycle/ArrayPool;
    //   183: aload #12
    //   185: invokeinterface put : (Ljava/lang/Object;)V
    //   190: iload #5
    //   192: ireturn
    //   193: aload_3
    //   194: ifnull -> 205
    //   197: aload_3
    //   198: invokevirtual close : ()V
    //   201: goto -> 205
    //   204: astore_2
    //   205: aload_0
    //   206: getfield byteArrayPool : Lcom/bumptech/glide/load/engine/bitmap_recycle/ArrayPool;
    //   209: aload #12
    //   211: invokeinterface put : (Ljava/lang/Object;)V
    //   216: aload_1
    //   217: athrow
    // Exception table:
    //   from	to	target	type
    //   37	42	128	java/io/IOException
    //   37	42	124	finally
    //   49	55	128	java/io/IOException
    //   49	55	124	finally
    //   63	71	128	java/io/IOException
    //   63	71	124	finally
    //   82	91	128	java/io/IOException
    //   82	91	124	finally
    //   99	103	128	java/io/IOException
    //   99	103	124	finally
    //   109	113	120	java/io/IOException
    //   132	141	124	finally
    //   144	153	124	finally
    //   162	167	174	java/io/IOException
    //   197	201	204	java/io/IOException
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\model\StreamEncoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */