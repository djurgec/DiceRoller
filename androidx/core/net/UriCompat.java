package androidx.core.net;

import android.net.Uri;

public final class UriCompat {
  public static String toSafeString(Uri paramUri) {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual getScheme : ()Ljava/lang/String;
    //   4: astore #5
    //   6: aload_0
    //   7: invokevirtual getSchemeSpecificPart : ()Ljava/lang/String;
    //   10: astore #4
    //   12: aload #4
    //   14: astore_3
    //   15: aload #5
    //   17: ifnull -> 319
    //   20: aload #5
    //   22: ldc 'tel'
    //   24: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   27: ifne -> 224
    //   30: aload #5
    //   32: ldc 'sip'
    //   34: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   37: ifne -> 224
    //   40: aload #5
    //   42: ldc 'sms'
    //   44: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   47: ifne -> 224
    //   50: aload #5
    //   52: ldc 'smsto'
    //   54: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   57: ifne -> 224
    //   60: aload #5
    //   62: ldc 'mailto'
    //   64: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   67: ifne -> 224
    //   70: aload #5
    //   72: ldc 'nfc'
    //   74: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   77: ifeq -> 83
    //   80: goto -> 224
    //   83: aload #5
    //   85: ldc 'http'
    //   87: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   90: ifne -> 126
    //   93: aload #5
    //   95: ldc 'https'
    //   97: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   100: ifne -> 126
    //   103: aload #5
    //   105: ldc 'ftp'
    //   107: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   110: ifne -> 126
    //   113: aload #4
    //   115: astore_3
    //   116: aload #5
    //   118: ldc 'rtsp'
    //   120: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   123: ifeq -> 319
    //   126: new java/lang/StringBuilder
    //   129: dup
    //   130: invokespecial <init> : ()V
    //   133: ldc '//'
    //   135: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   138: astore #6
    //   140: aload_0
    //   141: invokevirtual getHost : ()Ljava/lang/String;
    //   144: astore_3
    //   145: ldc ''
    //   147: astore #4
    //   149: aload_3
    //   150: ifnull -> 161
    //   153: aload_0
    //   154: invokevirtual getHost : ()Ljava/lang/String;
    //   157: astore_3
    //   158: goto -> 164
    //   161: ldc ''
    //   163: astore_3
    //   164: aload #6
    //   166: aload_3
    //   167: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   170: astore #6
    //   172: aload #4
    //   174: astore_3
    //   175: aload_0
    //   176: invokevirtual getPort : ()I
    //   179: iconst_m1
    //   180: if_icmpeq -> 206
    //   183: new java/lang/StringBuilder
    //   186: dup
    //   187: invokespecial <init> : ()V
    //   190: ldc ':'
    //   192: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   195: aload_0
    //   196: invokevirtual getPort : ()I
    //   199: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   202: invokevirtual toString : ()Ljava/lang/String;
    //   205: astore_3
    //   206: aload #6
    //   208: aload_3
    //   209: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   212: ldc '/...'
    //   214: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   217: invokevirtual toString : ()Ljava/lang/String;
    //   220: astore_3
    //   221: goto -> 319
    //   224: new java/lang/StringBuilder
    //   227: dup
    //   228: bipush #64
    //   230: invokespecial <init> : (I)V
    //   233: astore_0
    //   234: aload_0
    //   235: aload #5
    //   237: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   240: pop
    //   241: aload_0
    //   242: bipush #58
    //   244: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   247: pop
    //   248: aload #4
    //   250: ifnull -> 314
    //   253: iconst_0
    //   254: istore_2
    //   255: iload_2
    //   256: aload #4
    //   258: invokevirtual length : ()I
    //   261: if_icmpge -> 314
    //   264: aload #4
    //   266: iload_2
    //   267: invokevirtual charAt : (I)C
    //   270: istore_1
    //   271: iload_1
    //   272: bipush #45
    //   274: if_icmpeq -> 302
    //   277: iload_1
    //   278: bipush #64
    //   280: if_icmpeq -> 302
    //   283: iload_1
    //   284: bipush #46
    //   286: if_icmpne -> 292
    //   289: goto -> 302
    //   292: aload_0
    //   293: bipush #120
    //   295: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   298: pop
    //   299: goto -> 308
    //   302: aload_0
    //   303: iload_1
    //   304: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   307: pop
    //   308: iinc #2, 1
    //   311: goto -> 255
    //   314: aload_0
    //   315: invokevirtual toString : ()Ljava/lang/String;
    //   318: areturn
    //   319: new java/lang/StringBuilder
    //   322: dup
    //   323: bipush #64
    //   325: invokespecial <init> : (I)V
    //   328: astore_0
    //   329: aload #5
    //   331: ifnull -> 348
    //   334: aload_0
    //   335: aload #5
    //   337: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   340: pop
    //   341: aload_0
    //   342: bipush #58
    //   344: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   347: pop
    //   348: aload_3
    //   349: ifnull -> 358
    //   352: aload_0
    //   353: aload_3
    //   354: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   357: pop
    //   358: aload_0
    //   359: invokevirtual toString : ()Ljava/lang/String;
    //   362: areturn
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\net\UriCompat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */