package com.bumptech.glide.load.resource.bitmap;

import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class RecyclableBufferedInputStream extends FilterInputStream {
  private volatile byte[] buf;
  
  private final ArrayPool byteArrayPool;
  
  private int count;
  
  private int marklimit;
  
  private int markpos = -1;
  
  private int pos;
  
  public RecyclableBufferedInputStream(InputStream paramInputStream, ArrayPool paramArrayPool) {
    this(paramInputStream, paramArrayPool, 65536);
  }
  
  RecyclableBufferedInputStream(InputStream paramInputStream, ArrayPool paramArrayPool, int paramInt) {
    super(paramInputStream);
    this.byteArrayPool = paramArrayPool;
    this.buf = (byte[])paramArrayPool.get(paramInt, byte[].class);
  }
  
  private int fillbuf(InputStream paramInputStream, byte[] paramArrayOfbyte) throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: getfield markpos : I
    //   4: istore #4
    //   6: iload #4
    //   8: iconst_m1
    //   9: if_icmpeq -> 210
    //   12: aload_0
    //   13: getfield pos : I
    //   16: istore_3
    //   17: aload_0
    //   18: getfield marklimit : I
    //   21: istore #5
    //   23: iload_3
    //   24: iload #4
    //   26: isub
    //   27: iload #5
    //   29: if_icmplt -> 35
    //   32: goto -> 210
    //   35: iload #4
    //   37: ifne -> 126
    //   40: iload #5
    //   42: aload_2
    //   43: arraylength
    //   44: if_icmple -> 126
    //   47: aload_0
    //   48: getfield count : I
    //   51: aload_2
    //   52: arraylength
    //   53: if_icmpne -> 126
    //   56: aload_2
    //   57: arraylength
    //   58: iconst_2
    //   59: imul
    //   60: istore #4
    //   62: iload #4
    //   64: istore_3
    //   65: iload #4
    //   67: iload #5
    //   69: if_icmple -> 77
    //   72: aload_0
    //   73: getfield marklimit : I
    //   76: istore_3
    //   77: aload_0
    //   78: getfield byteArrayPool : Lcom/bumptech/glide/load/engine/bitmap_recycle/ArrayPool;
    //   81: iload_3
    //   82: ldc [B
    //   84: invokeinterface get : (ILjava/lang/Class;)Ljava/lang/Object;
    //   89: checkcast [B
    //   92: astore #6
    //   94: aload_2
    //   95: iconst_0
    //   96: aload #6
    //   98: iconst_0
    //   99: aload_2
    //   100: arraylength
    //   101: invokestatic arraycopy : (Ljava/lang/Object;ILjava/lang/Object;II)V
    //   104: aload_0
    //   105: aload #6
    //   107: putfield buf : [B
    //   110: aload_0
    //   111: getfield byteArrayPool : Lcom/bumptech/glide/load/engine/bitmap_recycle/ArrayPool;
    //   114: aload_2
    //   115: invokeinterface put : (Ljava/lang/Object;)V
    //   120: aload #6
    //   122: astore_2
    //   123: goto -> 147
    //   126: aload_2
    //   127: astore #6
    //   129: iload #4
    //   131: ifle -> 120
    //   134: aload_2
    //   135: iload #4
    //   137: aload_2
    //   138: iconst_0
    //   139: aload_2
    //   140: arraylength
    //   141: iload #4
    //   143: isub
    //   144: invokestatic arraycopy : (Ljava/lang/Object;ILjava/lang/Object;II)V
    //   147: aload_0
    //   148: getfield pos : I
    //   151: aload_0
    //   152: getfield markpos : I
    //   155: isub
    //   156: istore_3
    //   157: aload_0
    //   158: iload_3
    //   159: putfield pos : I
    //   162: aload_0
    //   163: iconst_0
    //   164: putfield markpos : I
    //   167: aload_0
    //   168: iconst_0
    //   169: putfield count : I
    //   172: aload_1
    //   173: aload_2
    //   174: iload_3
    //   175: aload_2
    //   176: arraylength
    //   177: iload_3
    //   178: isub
    //   179: invokevirtual read : ([BII)I
    //   182: istore #4
    //   184: aload_0
    //   185: getfield pos : I
    //   188: istore_3
    //   189: iload #4
    //   191: ifgt -> 197
    //   194: goto -> 202
    //   197: iload_3
    //   198: iload #4
    //   200: iadd
    //   201: istore_3
    //   202: aload_0
    //   203: iload_3
    //   204: putfield count : I
    //   207: iload #4
    //   209: ireturn
    //   210: aload_1
    //   211: aload_2
    //   212: invokevirtual read : ([B)I
    //   215: istore_3
    //   216: iload_3
    //   217: ifle -> 235
    //   220: aload_0
    //   221: iconst_m1
    //   222: putfield markpos : I
    //   225: aload_0
    //   226: iconst_0
    //   227: putfield pos : I
    //   230: aload_0
    //   231: iload_3
    //   232: putfield count : I
    //   235: iload_3
    //   236: ireturn
  }
  
  private static IOException streamClosed() throws IOException {
    throw new IOException("BufferedInputStream is closed");
  }
  
  public int available() throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield in : Ljava/io/InputStream;
    //   6: astore #4
    //   8: aload_0
    //   9: getfield buf : [B
    //   12: ifnull -> 44
    //   15: aload #4
    //   17: ifnull -> 44
    //   20: aload_0
    //   21: getfield count : I
    //   24: istore_1
    //   25: aload_0
    //   26: getfield pos : I
    //   29: istore_2
    //   30: aload #4
    //   32: invokevirtual available : ()I
    //   35: istore_3
    //   36: aload_0
    //   37: monitorexit
    //   38: iload_1
    //   39: iload_2
    //   40: isub
    //   41: iload_3
    //   42: iadd
    //   43: ireturn
    //   44: invokestatic streamClosed : ()Ljava/io/IOException;
    //   47: athrow
    //   48: astore #4
    //   50: aload_0
    //   51: monitorexit
    //   52: aload #4
    //   54: athrow
    // Exception table:
    //   from	to	target	type
    //   2	15	48	finally
    //   20	36	48	finally
    //   44	48	48	finally
  }
  
  public void close() throws IOException {
    if (this.buf != null) {
      this.byteArrayPool.put(this.buf);
      this.buf = null;
    } 
    InputStream inputStream = this.in;
    this.in = null;
    if (inputStream != null)
      inputStream.close(); 
  }
  
  public void fixMarkLimit() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_0
    //   4: getfield buf : [B
    //   7: arraylength
    //   8: putfield marklimit : I
    //   11: aload_0
    //   12: monitorexit
    //   13: return
    //   14: astore_1
    //   15: aload_0
    //   16: monitorexit
    //   17: aload_1
    //   18: athrow
    // Exception table:
    //   from	to	target	type
    //   2	11	14	finally
  }
  
  public void mark(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_0
    //   4: getfield marklimit : I
    //   7: iload_1
    //   8: invokestatic max : (II)I
    //   11: putfield marklimit : I
    //   14: aload_0
    //   15: aload_0
    //   16: getfield pos : I
    //   19: putfield markpos : I
    //   22: aload_0
    //   23: monitorexit
    //   24: return
    //   25: astore_2
    //   26: aload_0
    //   27: monitorexit
    //   28: aload_2
    //   29: athrow
    // Exception table:
    //   from	to	target	type
    //   2	22	25	finally
  }
  
  public boolean markSupported() {
    return true;
  }
  
  public int read() throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield buf : [B
    //   6: astore #4
    //   8: aload_0
    //   9: getfield in : Ljava/io/InputStream;
    //   12: astore_3
    //   13: aload #4
    //   15: ifnull -> 117
    //   18: aload_3
    //   19: ifnull -> 117
    //   22: aload_0
    //   23: getfield pos : I
    //   26: aload_0
    //   27: getfield count : I
    //   30: if_icmplt -> 50
    //   33: aload_0
    //   34: aload_3
    //   35: aload #4
    //   37: invokespecial fillbuf : (Ljava/io/InputStream;[B)I
    //   40: istore_1
    //   41: iload_1
    //   42: iconst_m1
    //   43: if_icmpne -> 50
    //   46: aload_0
    //   47: monitorexit
    //   48: iconst_m1
    //   49: ireturn
    //   50: aload #4
    //   52: astore_3
    //   53: aload #4
    //   55: aload_0
    //   56: getfield buf : [B
    //   59: if_acmpeq -> 78
    //   62: aload_0
    //   63: getfield buf : [B
    //   66: astore_3
    //   67: aload_3
    //   68: ifnull -> 74
    //   71: goto -> 78
    //   74: invokestatic streamClosed : ()Ljava/io/IOException;
    //   77: athrow
    //   78: aload_0
    //   79: getfield count : I
    //   82: istore_1
    //   83: aload_0
    //   84: getfield pos : I
    //   87: istore_2
    //   88: iload_1
    //   89: iload_2
    //   90: isub
    //   91: ifle -> 113
    //   94: aload_0
    //   95: iload_2
    //   96: iconst_1
    //   97: iadd
    //   98: putfield pos : I
    //   101: aload_3
    //   102: iload_2
    //   103: baload
    //   104: istore_1
    //   105: aload_0
    //   106: monitorexit
    //   107: iload_1
    //   108: sipush #255
    //   111: iand
    //   112: ireturn
    //   113: aload_0
    //   114: monitorexit
    //   115: iconst_m1
    //   116: ireturn
    //   117: invokestatic streamClosed : ()Ljava/io/IOException;
    //   120: athrow
    //   121: astore_3
    //   122: aload_0
    //   123: monitorexit
    //   124: aload_3
    //   125: athrow
    // Exception table:
    //   from	to	target	type
    //   2	13	121	finally
    //   22	41	121	finally
    //   53	67	121	finally
    //   74	78	121	finally
    //   78	88	121	finally
    //   94	101	121	finally
    //   117	121	121	finally
  }
  
  public int read(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield buf : [B
    //   6: astore #8
    //   8: aload #8
    //   10: ifnull -> 380
    //   13: iload_3
    //   14: ifne -> 21
    //   17: aload_0
    //   18: monitorexit
    //   19: iconst_0
    //   20: ireturn
    //   21: aload_0
    //   22: getfield in : Ljava/io/InputStream;
    //   25: astore #10
    //   27: aload #10
    //   29: ifnull -> 376
    //   32: aload_0
    //   33: getfield pos : I
    //   36: istore #5
    //   38: aload_0
    //   39: getfield count : I
    //   42: istore #4
    //   44: iload #5
    //   46: iload #4
    //   48: if_icmpge -> 139
    //   51: iload #4
    //   53: iload #5
    //   55: isub
    //   56: iload_3
    //   57: if_icmplt -> 66
    //   60: iload_3
    //   61: istore #4
    //   63: goto -> 73
    //   66: iload #4
    //   68: iload #5
    //   70: isub
    //   71: istore #4
    //   73: aload #8
    //   75: iload #5
    //   77: aload_1
    //   78: iload_2
    //   79: iload #4
    //   81: invokestatic arraycopy : (Ljava/lang/Object;ILjava/lang/Object;II)V
    //   84: aload_0
    //   85: aload_0
    //   86: getfield pos : I
    //   89: iload #4
    //   91: iadd
    //   92: putfield pos : I
    //   95: iload #4
    //   97: iload_3
    //   98: if_icmpeq -> 134
    //   101: aload #10
    //   103: invokevirtual available : ()I
    //   106: istore #5
    //   108: iload #5
    //   110: ifne -> 116
    //   113: goto -> 134
    //   116: iload_2
    //   117: iload #4
    //   119: iadd
    //   120: istore #5
    //   122: iload_3
    //   123: iload #4
    //   125: isub
    //   126: istore_2
    //   127: iload #5
    //   129: istore #4
    //   131: goto -> 148
    //   134: aload_0
    //   135: monitorexit
    //   136: iload #4
    //   138: ireturn
    //   139: iload_3
    //   140: istore #5
    //   142: iload_2
    //   143: istore #4
    //   145: iload #5
    //   147: istore_2
    //   148: aload_0
    //   149: getfield markpos : I
    //   152: istore #5
    //   154: iconst_m1
    //   155: istore #6
    //   157: iload #5
    //   159: iconst_m1
    //   160: if_icmpne -> 209
    //   163: iload_2
    //   164: aload #8
    //   166: arraylength
    //   167: if_icmplt -> 209
    //   170: aload #10
    //   172: aload_1
    //   173: iload #4
    //   175: iload_2
    //   176: invokevirtual read : ([BII)I
    //   179: istore #7
    //   181: iload #7
    //   183: istore #5
    //   185: iload #7
    //   187: iconst_m1
    //   188: if_icmpne -> 335
    //   191: iload_2
    //   192: iload_3
    //   193: if_icmpne -> 199
    //   196: goto -> 204
    //   199: iload_3
    //   200: iload_2
    //   201: isub
    //   202: istore #6
    //   204: aload_0
    //   205: monitorexit
    //   206: iload #6
    //   208: ireturn
    //   209: aload_0
    //   210: aload #10
    //   212: aload #8
    //   214: invokespecial fillbuf : (Ljava/io/InputStream;[B)I
    //   217: istore #5
    //   219: iload #5
    //   221: iconst_m1
    //   222: if_icmpne -> 243
    //   225: iload_2
    //   226: iload_3
    //   227: if_icmpne -> 233
    //   230: goto -> 238
    //   233: iload_3
    //   234: iload_2
    //   235: isub
    //   236: istore #6
    //   238: aload_0
    //   239: monitorexit
    //   240: iload #6
    //   242: ireturn
    //   243: aload #8
    //   245: astore #9
    //   247: aload #8
    //   249: aload_0
    //   250: getfield buf : [B
    //   253: if_acmpeq -> 274
    //   256: aload_0
    //   257: getfield buf : [B
    //   260: astore #9
    //   262: aload #9
    //   264: ifnull -> 270
    //   267: goto -> 274
    //   270: invokestatic streamClosed : ()Ljava/io/IOException;
    //   273: athrow
    //   274: aload_0
    //   275: getfield count : I
    //   278: istore #5
    //   280: aload_0
    //   281: getfield pos : I
    //   284: istore #6
    //   286: iload #5
    //   288: iload #6
    //   290: isub
    //   291: iload_2
    //   292: if_icmplt -> 301
    //   295: iload_2
    //   296: istore #5
    //   298: goto -> 308
    //   301: iload #5
    //   303: iload #6
    //   305: isub
    //   306: istore #5
    //   308: aload #9
    //   310: iload #6
    //   312: aload_1
    //   313: iload #4
    //   315: iload #5
    //   317: invokestatic arraycopy : (Ljava/lang/Object;ILjava/lang/Object;II)V
    //   320: aload_0
    //   321: aload_0
    //   322: getfield pos : I
    //   325: iload #5
    //   327: iadd
    //   328: putfield pos : I
    //   331: aload #9
    //   333: astore #8
    //   335: iload_2
    //   336: iload #5
    //   338: isub
    //   339: istore_2
    //   340: iload_2
    //   341: ifne -> 348
    //   344: aload_0
    //   345: monitorexit
    //   346: iload_3
    //   347: ireturn
    //   348: aload #10
    //   350: invokevirtual available : ()I
    //   353: istore #6
    //   355: iload #6
    //   357: ifne -> 366
    //   360: aload_0
    //   361: monitorexit
    //   362: iload_3
    //   363: iload_2
    //   364: isub
    //   365: ireturn
    //   366: iload #4
    //   368: iload #5
    //   370: iadd
    //   371: istore #4
    //   373: goto -> 148
    //   376: invokestatic streamClosed : ()Ljava/io/IOException;
    //   379: athrow
    //   380: invokestatic streamClosed : ()Ljava/io/IOException;
    //   383: athrow
    //   384: astore_1
    //   385: aload_0
    //   386: monitorexit
    //   387: aload_1
    //   388: athrow
    // Exception table:
    //   from	to	target	type
    //   2	8	384	finally
    //   21	27	384	finally
    //   32	44	384	finally
    //   73	95	384	finally
    //   101	108	384	finally
    //   148	154	384	finally
    //   163	181	384	finally
    //   209	219	384	finally
    //   247	262	384	finally
    //   270	274	384	finally
    //   274	286	384	finally
    //   308	331	384	finally
    //   348	355	384	finally
    //   376	380	384	finally
    //   380	384	384	finally
  }
  
  public void release() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield buf : [B
    //   6: ifnull -> 27
    //   9: aload_0
    //   10: getfield byteArrayPool : Lcom/bumptech/glide/load/engine/bitmap_recycle/ArrayPool;
    //   13: aload_0
    //   14: getfield buf : [B
    //   17: invokeinterface put : (Ljava/lang/Object;)V
    //   22: aload_0
    //   23: aconst_null
    //   24: putfield buf : [B
    //   27: aload_0
    //   28: monitorexit
    //   29: return
    //   30: astore_1
    //   31: aload_0
    //   32: monitorexit
    //   33: aload_1
    //   34: athrow
    // Exception table:
    //   from	to	target	type
    //   2	27	30	finally
  }
  
  public void reset() throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield buf : [B
    //   6: ifnull -> 73
    //   9: aload_0
    //   10: getfield markpos : I
    //   13: istore_1
    //   14: iconst_m1
    //   15: iload_1
    //   16: if_icmpeq -> 27
    //   19: aload_0
    //   20: iload_1
    //   21: putfield pos : I
    //   24: aload_0
    //   25: monitorexit
    //   26: return
    //   27: new com/bumptech/glide/load/resource/bitmap/RecyclableBufferedInputStream$InvalidMarkException
    //   30: astore_2
    //   31: new java/lang/StringBuilder
    //   34: astore_3
    //   35: aload_3
    //   36: invokespecial <init> : ()V
    //   39: aload_2
    //   40: aload_3
    //   41: ldc 'Mark has been invalidated, pos: '
    //   43: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   46: aload_0
    //   47: getfield pos : I
    //   50: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   53: ldc ' markLimit: '
    //   55: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   58: aload_0
    //   59: getfield marklimit : I
    //   62: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   65: invokevirtual toString : ()Ljava/lang/String;
    //   68: invokespecial <init> : (Ljava/lang/String;)V
    //   71: aload_2
    //   72: athrow
    //   73: new java/io/IOException
    //   76: astore_2
    //   77: aload_2
    //   78: ldc 'Stream is closed'
    //   80: invokespecial <init> : (Ljava/lang/String;)V
    //   83: aload_2
    //   84: athrow
    //   85: astore_2
    //   86: aload_0
    //   87: monitorexit
    //   88: aload_2
    //   89: athrow
    // Exception table:
    //   from	to	target	type
    //   2	14	85	finally
    //   19	24	85	finally
    //   27	73	85	finally
    //   73	85	85	finally
  }
  
  public long skip(long paramLong) throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: lload_1
    //   3: lconst_1
    //   4: lcmp
    //   5: ifge -> 12
    //   8: aload_0
    //   9: monitorexit
    //   10: lconst_0
    //   11: lreturn
    //   12: aload_0
    //   13: getfield buf : [B
    //   16: astore #10
    //   18: aload #10
    //   20: ifnull -> 215
    //   23: aload_0
    //   24: getfield in : Ljava/io/InputStream;
    //   27: astore #9
    //   29: aload #9
    //   31: ifnull -> 211
    //   34: aload_0
    //   35: getfield count : I
    //   38: istore #4
    //   40: aload_0
    //   41: getfield pos : I
    //   44: istore_3
    //   45: iload #4
    //   47: iload_3
    //   48: isub
    //   49: i2l
    //   50: lload_1
    //   51: lcmp
    //   52: iflt -> 68
    //   55: aload_0
    //   56: iload_3
    //   57: i2l
    //   58: lload_1
    //   59: ladd
    //   60: l2i
    //   61: putfield pos : I
    //   64: aload_0
    //   65: monitorexit
    //   66: lload_1
    //   67: lreturn
    //   68: iload #4
    //   70: i2l
    //   71: iload_3
    //   72: i2l
    //   73: lsub
    //   74: lstore #5
    //   76: aload_0
    //   77: iload #4
    //   79: putfield pos : I
    //   82: aload_0
    //   83: getfield markpos : I
    //   86: iconst_m1
    //   87: if_icmpeq -> 183
    //   90: lload_1
    //   91: aload_0
    //   92: getfield marklimit : I
    //   95: i2l
    //   96: lcmp
    //   97: ifgt -> 183
    //   100: aload_0
    //   101: aload #9
    //   103: aload #10
    //   105: invokespecial fillbuf : (Ljava/io/InputStream;[B)I
    //   108: istore_3
    //   109: iload_3
    //   110: iconst_m1
    //   111: if_icmpne -> 119
    //   114: aload_0
    //   115: monitorexit
    //   116: lload #5
    //   118: lreturn
    //   119: aload_0
    //   120: getfield count : I
    //   123: istore #4
    //   125: aload_0
    //   126: getfield pos : I
    //   129: istore_3
    //   130: iload #4
    //   132: iload_3
    //   133: isub
    //   134: i2l
    //   135: lload_1
    //   136: lload #5
    //   138: lsub
    //   139: lcmp
    //   140: iflt -> 159
    //   143: aload_0
    //   144: iload_3
    //   145: i2l
    //   146: lload_1
    //   147: ladd
    //   148: lload #5
    //   150: lsub
    //   151: l2i
    //   152: putfield pos : I
    //   155: aload_0
    //   156: monitorexit
    //   157: lload_1
    //   158: lreturn
    //   159: iload #4
    //   161: i2l
    //   162: lstore_1
    //   163: iload_3
    //   164: i2l
    //   165: lstore #7
    //   167: aload_0
    //   168: iload #4
    //   170: putfield pos : I
    //   173: aload_0
    //   174: monitorexit
    //   175: lload_1
    //   176: lload #5
    //   178: ladd
    //   179: lload #7
    //   181: lsub
    //   182: lreturn
    //   183: aload #9
    //   185: lload_1
    //   186: lload #5
    //   188: lsub
    //   189: invokevirtual skip : (J)J
    //   192: lstore_1
    //   193: lload_1
    //   194: lconst_0
    //   195: lcmp
    //   196: ifle -> 204
    //   199: aload_0
    //   200: iconst_m1
    //   201: putfield markpos : I
    //   204: aload_0
    //   205: monitorexit
    //   206: lload #5
    //   208: lload_1
    //   209: ladd
    //   210: lreturn
    //   211: invokestatic streamClosed : ()Ljava/io/IOException;
    //   214: athrow
    //   215: invokestatic streamClosed : ()Ljava/io/IOException;
    //   218: athrow
    //   219: astore #9
    //   221: aload_0
    //   222: monitorexit
    //   223: aload #9
    //   225: athrow
    // Exception table:
    //   from	to	target	type
    //   12	18	219	finally
    //   23	29	219	finally
    //   34	45	219	finally
    //   55	64	219	finally
    //   76	109	219	finally
    //   119	130	219	finally
    //   143	155	219	finally
    //   167	173	219	finally
    //   183	193	219	finally
    //   199	204	219	finally
    //   211	215	219	finally
    //   215	219	219	finally
  }
  
  static class InvalidMarkException extends IOException {
    private static final long serialVersionUID = -4338378848813561757L;
    
    InvalidMarkException(String param1String) {
      super(param1String);
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\resource\bitmap\RecyclableBufferedInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */