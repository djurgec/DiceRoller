package androidx.core.content.pm;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.pm.SigningInfo;
import android.os.Build;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class PackageInfoCompat {
  private static boolean byteArrayContains(byte[][] paramArrayOfbyte, byte[] paramArrayOfbyte1) {
    int i = paramArrayOfbyte.length;
    for (byte b = 0; b < i; b++) {
      if (Arrays.equals(paramArrayOfbyte1, paramArrayOfbyte[b]))
        return true; 
    } 
    return false;
  }
  
  private static byte[] computeSHA256Digest(byte[] paramArrayOfbyte) {
    try {
      return MessageDigest.getInstance("SHA256").digest(paramArrayOfbyte);
    } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
      throw new RuntimeException("Device doesn't support SHA256 cert checking", noSuchAlgorithmException);
    } 
  }
  
  public static long getLongVersionCode(PackageInfo paramPackageInfo) {
    return (Build.VERSION.SDK_INT >= 28) ? paramPackageInfo.getLongVersionCode() : paramPackageInfo.versionCode;
  }
  
  public static List<Signature> getSignatures(PackageManager paramPackageManager, String paramString) throws PackageManager.NameNotFoundException {
    Signature[] arrayOfSignature;
    if (Build.VERSION.SDK_INT >= 28) {
      SigningInfo signingInfo = (paramPackageManager.getPackageInfo(paramString, 134217728)).signingInfo;
      if (Api28Impl.hasMultipleSigners(signingInfo)) {
        arrayOfSignature = Api28Impl.getApkContentsSigners(signingInfo);
      } else {
        arrayOfSignature = Api28Impl.getSigningCertificateHistory((SigningInfo)arrayOfSignature);
      } 
    } else {
      arrayOfSignature = (arrayOfSignature.getPackageInfo(paramString, 64)).signatures;
    } 
    return (arrayOfSignature == null) ? Collections.emptyList() : Arrays.asList(arrayOfSignature);
  }
  
  public static boolean hasSignatures(PackageManager paramPackageManager, String paramString, Map<byte[], Integer> paramMap, boolean paramBoolean) throws PackageManager.NameNotFoundException {
    byte[][] arrayOfByte;
    if (paramMap.isEmpty())
      return false; 
    Set<byte> set = paramMap.keySet();
    for (byte[] arrayOfByte2 : set) {
      if (arrayOfByte2 != null) {
        Integer integer = paramMap.get(arrayOfByte2);
        if (integer != null) {
          switch (integer.intValue()) {
            case 0:
            case 1:
              break;
          } 
          continue;
        } 
        throw new IllegalArgumentException("Type must be specified for cert when verifying " + paramString);
      } 
      throw new IllegalArgumentException("Cert byte array cannot be null when verifying " + paramString);
    } 
    List<Signature> list = getSignatures(paramPackageManager, paramString);
    if (!paramBoolean && Build.VERSION.SDK_INT >= 28) {
      for (byte[] arrayOfByte1 : set) {
        if (!Api28Impl.hasSigningCertificate(paramPackageManager, paramString, arrayOfByte1, ((Integer)paramMap.get(arrayOfByte1)).intValue()))
          return false; 
      } 
      return true;
    } 
    if (arrayOfByte1.size() == 0 || paramMap.size() > arrayOfByte1.size() || (paramBoolean && paramMap.size() != arrayOfByte1.size()))
      return false; 
    paramBoolean = paramMap.containsValue(Integer.valueOf(1));
    paramPackageManager = null;
    if (paramBoolean) {
      byte[][] arrayOfByte2 = new byte[arrayOfByte1.size()][];
      byte b = 0;
      while (true) {
        arrayOfByte = arrayOfByte2;
        if (b < arrayOfByte1.size()) {
          arrayOfByte2[b] = computeSHA256Digest(((Signature)arrayOfByte1.get(b)).toByteArray());
          b++;
          continue;
        } 
        break;
      } 
    } 
    Iterator<byte> iterator = set.iterator();
    if (iterator.hasNext()) {
      byte[] arrayOfByte2 = (byte[])iterator.next();
      Integer integer = paramMap.get(arrayOfByte2);
      switch (integer.intValue()) {
        default:
          throw new IllegalArgumentException("Unsupported certificate type " + integer);
        case 1:
          return !!byteArrayContains(arrayOfByte, arrayOfByte2);
        case 0:
          break;
      } 
      return !!arrayOfByte1.contains(new Signature(arrayOfByte2));
    } 
    return false;
  }
  
  private static class Api28Impl {
    static Signature[] getApkContentsSigners(SigningInfo param1SigningInfo) {
      return param1SigningInfo.getApkContentsSigners();
    }
    
    static Signature[] getSigningCertificateHistory(SigningInfo param1SigningInfo) {
      return param1SigningInfo.getSigningCertificateHistory();
    }
    
    static boolean hasMultipleSigners(SigningInfo param1SigningInfo) {
      return param1SigningInfo.hasMultipleSigners();
    }
    
    static boolean hasSigningCertificate(PackageManager param1PackageManager, String param1String, byte[] param1ArrayOfbyte, int param1Int) {
      return param1PackageManager.hasSigningCertificate(param1String, param1ArrayOfbyte, param1Int);
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\content\pm\PackageInfoCompat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */