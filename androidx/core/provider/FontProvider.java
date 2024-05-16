package androidx.core.provider;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.os.CancellationSignal;
import androidx.core.content.res.FontResourcesParserCompat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

class FontProvider {
  private static final Comparator<byte[]> sByteArrayComparator = new Comparator<byte[]>() {
      public int compare(byte[] param1ArrayOfbyte1, byte[] param1ArrayOfbyte2) {
        if (param1ArrayOfbyte1.length != param1ArrayOfbyte2.length)
          return param1ArrayOfbyte1.length - param1ArrayOfbyte2.length; 
        for (byte b = 0; b < param1ArrayOfbyte1.length; b++) {
          if (param1ArrayOfbyte1[b] != param1ArrayOfbyte2[b])
            return param1ArrayOfbyte1[b] - param1ArrayOfbyte2[b]; 
        } 
        return 0;
      }
    };
  
  private static List<byte[]> convertToByteArrayList(Signature[] paramArrayOfSignature) {
    ArrayList<byte[]> arrayList = new ArrayList();
    for (byte b = 0; b < paramArrayOfSignature.length; b++)
      arrayList.add(paramArrayOfSignature[b].toByteArray()); 
    return (List<byte[]>)arrayList;
  }
  
  private static boolean equalsByteArrayList(List<byte[]> paramList1, List<byte[]> paramList2) {
    if (paramList1.size() != paramList2.size())
      return false; 
    for (byte b = 0; b < paramList1.size(); b++) {
      if (!Arrays.equals(paramList1.get(b), paramList2.get(b)))
        return false; 
    } 
    return true;
  }
  
  private static List<List<byte[]>> getCertificates(FontRequest paramFontRequest, Resources paramResources) {
    return (paramFontRequest.getCertificates() != null) ? paramFontRequest.getCertificates() : FontResourcesParserCompat.readCerts(paramResources, paramFontRequest.getCertificatesArrayResId());
  }
  
  static FontsContractCompat.FontFamilyResult getFontFamilyResult(Context paramContext, FontRequest paramFontRequest, CancellationSignal paramCancellationSignal) throws PackageManager.NameNotFoundException {
    ProviderInfo providerInfo = getProvider(paramContext.getPackageManager(), paramFontRequest, paramContext.getResources());
    return (providerInfo == null) ? FontsContractCompat.FontFamilyResult.create(1, null) : FontsContractCompat.FontFamilyResult.create(0, query(paramContext, paramFontRequest, providerInfo.authority, paramCancellationSignal));
  }
  
  static ProviderInfo getProvider(PackageManager paramPackageManager, FontRequest paramFontRequest, Resources paramResources) throws PackageManager.NameNotFoundException {
    String str = paramFontRequest.getProviderAuthority();
    ProviderInfo providerInfo = paramPackageManager.resolveContentProvider(str, 0);
    if (providerInfo != null) {
      ArrayList<byte> arrayList;
      if (providerInfo.packageName.equals(paramFontRequest.getProviderPackage())) {
        List<byte[]> list = convertToByteArrayList((paramPackageManager.getPackageInfo(providerInfo.packageName, 64)).signatures);
        Collections.sort((List)list, (Comparator)sByteArrayComparator);
        List<List<byte[]>> list1 = getCertificates(paramFontRequest, paramResources);
        for (byte b = 0; b < list1.size(); b++) {
          arrayList = new ArrayList(list1.get(b));
          Collections.sort(arrayList, (Comparator)sByteArrayComparator);
          if (equalsByteArrayList(list, (List)arrayList))
            return providerInfo; 
        } 
        return null;
      } 
      throw new PackageManager.NameNotFoundException("Found content provider " + str + ", but package was not " + arrayList.getProviderPackage());
    } 
    throw new PackageManager.NameNotFoundException("No package found for authority: " + str);
  }
  
  static FontsContractCompat.FontInfo[] query(Context paramContext, FontRequest paramFontRequest, String paramString, CancellationSignal paramCancellationSignal) {
    // Byte code:
    //   0: new java/util/ArrayList
    //   3: dup
    //   4: invokespecial <init> : ()V
    //   7: astore #14
    //   9: new android/net/Uri$Builder
    //   12: dup
    //   13: invokespecial <init> : ()V
    //   16: ldc 'content'
    //   18: invokevirtual scheme : (Ljava/lang/String;)Landroid/net/Uri$Builder;
    //   21: aload_2
    //   22: invokevirtual authority : (Ljava/lang/String;)Landroid/net/Uri$Builder;
    //   25: invokevirtual build : ()Landroid/net/Uri;
    //   28: astore #16
    //   30: new android/net/Uri$Builder
    //   33: dup
    //   34: invokespecial <init> : ()V
    //   37: ldc 'content'
    //   39: invokevirtual scheme : (Ljava/lang/String;)Landroid/net/Uri$Builder;
    //   42: aload_2
    //   43: invokevirtual authority : (Ljava/lang/String;)Landroid/net/Uri$Builder;
    //   46: ldc 'file'
    //   48: invokevirtual appendPath : (Ljava/lang/String;)Landroid/net/Uri$Builder;
    //   51: invokevirtual build : ()Landroid/net/Uri;
    //   54: astore #17
    //   56: aconst_null
    //   57: astore #15
    //   59: aload #15
    //   61: astore_2
    //   62: bipush #7
    //   64: anewarray java/lang/String
    //   67: astore #18
    //   69: aload #18
    //   71: iconst_0
    //   72: ldc '_id'
    //   74: aastore
    //   75: aload #18
    //   77: iconst_1
    //   78: ldc 'file_id'
    //   80: aastore
    //   81: aload #18
    //   83: iconst_2
    //   84: ldc 'font_ttc_index'
    //   86: aastore
    //   87: aload #18
    //   89: iconst_3
    //   90: ldc 'font_variation_settings'
    //   92: aastore
    //   93: aload #18
    //   95: iconst_4
    //   96: ldc 'font_weight'
    //   98: aastore
    //   99: aload #18
    //   101: iconst_5
    //   102: ldc 'font_italic'
    //   104: aastore
    //   105: aload #18
    //   107: bipush #6
    //   109: ldc 'result_code'
    //   111: aastore
    //   112: aload #15
    //   114: astore_2
    //   115: getstatic android/os/Build$VERSION.SDK_INT : I
    //   118: bipush #16
    //   120: if_icmple -> 156
    //   123: aload #15
    //   125: astore_2
    //   126: aload_0
    //   127: invokevirtual getContentResolver : ()Landroid/content/ContentResolver;
    //   130: aload #16
    //   132: aload #18
    //   134: ldc 'query = ?'
    //   136: iconst_1
    //   137: anewarray java/lang/String
    //   140: dup
    //   141: iconst_0
    //   142: aload_1
    //   143: invokevirtual getQuery : ()Ljava/lang/String;
    //   146: aastore
    //   147: aconst_null
    //   148: aload_3
    //   149: invokevirtual query : (Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Landroid/os/CancellationSignal;)Landroid/database/Cursor;
    //   152: astore_0
    //   153: goto -> 185
    //   156: aload #15
    //   158: astore_2
    //   159: aload_0
    //   160: invokevirtual getContentResolver : ()Landroid/content/ContentResolver;
    //   163: aload #16
    //   165: aload #18
    //   167: ldc 'query = ?'
    //   169: iconst_1
    //   170: anewarray java/lang/String
    //   173: dup
    //   174: iconst_0
    //   175: aload_1
    //   176: invokevirtual getQuery : ()Ljava/lang/String;
    //   179: aastore
    //   180: aconst_null
    //   181: invokevirtual query : (Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   184: astore_0
    //   185: aload #14
    //   187: astore_1
    //   188: aload_0
    //   189: ifnull -> 467
    //   192: aload #14
    //   194: astore_1
    //   195: aload_0
    //   196: astore_2
    //   197: aload_0
    //   198: invokeinterface getCount : ()I
    //   203: ifle -> 467
    //   206: aload_0
    //   207: astore_2
    //   208: aload_0
    //   209: ldc 'result_code'
    //   211: invokeinterface getColumnIndex : (Ljava/lang/String;)I
    //   216: istore #5
    //   218: aload_0
    //   219: astore_2
    //   220: new java/util/ArrayList
    //   223: astore_1
    //   224: aload_0
    //   225: astore_2
    //   226: aload_1
    //   227: invokespecial <init> : ()V
    //   230: aload_0
    //   231: astore_2
    //   232: aload_0
    //   233: ldc '_id'
    //   235: invokeinterface getColumnIndex : (Ljava/lang/String;)I
    //   240: istore #4
    //   242: aload_0
    //   243: astore_2
    //   244: aload_0
    //   245: ldc 'file_id'
    //   247: invokeinterface getColumnIndex : (Ljava/lang/String;)I
    //   252: istore #9
    //   254: aload_0
    //   255: astore_2
    //   256: aload_0
    //   257: ldc 'font_ttc_index'
    //   259: invokeinterface getColumnIndex : (Ljava/lang/String;)I
    //   264: istore #11
    //   266: aload_0
    //   267: astore_2
    //   268: aload_0
    //   269: ldc 'font_weight'
    //   271: invokeinterface getColumnIndex : (Ljava/lang/String;)I
    //   276: istore #10
    //   278: aload_0
    //   279: astore_2
    //   280: aload_0
    //   281: ldc 'font_italic'
    //   283: invokeinterface getColumnIndex : (Ljava/lang/String;)I
    //   288: istore #12
    //   290: aload_0
    //   291: astore_2
    //   292: aload_0
    //   293: invokeinterface moveToNext : ()Z
    //   298: ifeq -> 467
    //   301: iload #5
    //   303: iconst_m1
    //   304: if_icmpeq -> 322
    //   307: aload_0
    //   308: astore_2
    //   309: aload_0
    //   310: iload #5
    //   312: invokeinterface getInt : (I)I
    //   317: istore #6
    //   319: goto -> 325
    //   322: iconst_0
    //   323: istore #6
    //   325: iload #11
    //   327: iconst_m1
    //   328: if_icmpeq -> 346
    //   331: aload_0
    //   332: astore_2
    //   333: aload_0
    //   334: iload #11
    //   336: invokeinterface getInt : (I)I
    //   341: istore #7
    //   343: goto -> 349
    //   346: iconst_0
    //   347: istore #7
    //   349: iload #9
    //   351: iconst_m1
    //   352: if_icmpne -> 374
    //   355: aload_0
    //   356: astore_2
    //   357: aload #16
    //   359: aload_0
    //   360: iload #4
    //   362: invokeinterface getLong : (I)J
    //   367: invokestatic withAppendedId : (Landroid/net/Uri;J)Landroid/net/Uri;
    //   370: astore_3
    //   371: goto -> 390
    //   374: aload_0
    //   375: astore_2
    //   376: aload #17
    //   378: aload_0
    //   379: iload #9
    //   381: invokeinterface getLong : (I)J
    //   386: invokestatic withAppendedId : (Landroid/net/Uri;J)Landroid/net/Uri;
    //   389: astore_3
    //   390: iload #10
    //   392: iconst_m1
    //   393: if_icmpeq -> 411
    //   396: aload_0
    //   397: astore_2
    //   398: aload_0
    //   399: iload #10
    //   401: invokeinterface getInt : (I)I
    //   406: istore #8
    //   408: goto -> 416
    //   411: sipush #400
    //   414: istore #8
    //   416: iload #12
    //   418: iconst_m1
    //   419: if_icmpeq -> 442
    //   422: aload_0
    //   423: astore_2
    //   424: aload_0
    //   425: iload #12
    //   427: invokeinterface getInt : (I)I
    //   432: iconst_1
    //   433: if_icmpne -> 442
    //   436: iconst_1
    //   437: istore #13
    //   439: goto -> 445
    //   442: iconst_0
    //   443: istore #13
    //   445: aload_0
    //   446: astore_2
    //   447: aload_1
    //   448: aload_3
    //   449: iload #7
    //   451: iload #8
    //   453: iload #13
    //   455: iload #6
    //   457: invokestatic create : (Landroid/net/Uri;IIZI)Landroidx/core/provider/FontsContractCompat$FontInfo;
    //   460: invokevirtual add : (Ljava/lang/Object;)Z
    //   463: pop
    //   464: goto -> 290
    //   467: aload_0
    //   468: ifnull -> 477
    //   471: aload_0
    //   472: invokeinterface close : ()V
    //   477: aload_1
    //   478: iconst_0
    //   479: anewarray androidx/core/provider/FontsContractCompat$FontInfo
    //   482: invokevirtual toArray : ([Ljava/lang/Object;)[Ljava/lang/Object;
    //   485: checkcast [Landroidx/core/provider/FontsContractCompat$FontInfo;
    //   488: areturn
    //   489: astore_0
    //   490: aload_2
    //   491: ifnull -> 500
    //   494: aload_2
    //   495: invokeinterface close : ()V
    //   500: aload_0
    //   501: athrow
    // Exception table:
    //   from	to	target	type
    //   62	69	489	finally
    //   115	123	489	finally
    //   126	153	489	finally
    //   159	185	489	finally
    //   197	206	489	finally
    //   208	218	489	finally
    //   220	224	489	finally
    //   226	230	489	finally
    //   232	242	489	finally
    //   244	254	489	finally
    //   256	266	489	finally
    //   268	278	489	finally
    //   280	290	489	finally
    //   292	301	489	finally
    //   309	319	489	finally
    //   333	343	489	finally
    //   357	371	489	finally
    //   376	390	489	finally
    //   398	408	489	finally
    //   424	436	489	finally
    //   447	464	489	finally
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\provider\FontProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */