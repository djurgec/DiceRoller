package androidx.core.provider;

import android.util.Base64;
import androidx.core.util.Preconditions;
import java.util.List;

public final class FontRequest {
  private final List<List<byte[]>> mCertificates;
  
  private final int mCertificatesArray;
  
  private final String mIdentifier;
  
  private final String mProviderAuthority;
  
  private final String mProviderPackage;
  
  private final String mQuery;
  
  public FontRequest(String paramString1, String paramString2, String paramString3, int paramInt) {
    boolean bool;
    this.mProviderAuthority = (String)Preconditions.checkNotNull(paramString1);
    this.mProviderPackage = (String)Preconditions.checkNotNull(paramString2);
    this.mQuery = (String)Preconditions.checkNotNull(paramString3);
    this.mCertificates = null;
    if (paramInt != 0) {
      bool = true;
    } else {
      bool = false;
    } 
    Preconditions.checkArgument(bool);
    this.mCertificatesArray = paramInt;
    this.mIdentifier = createIdentifier(paramString1, paramString2, paramString3);
  }
  
  public FontRequest(String paramString1, String paramString2, String paramString3, List<List<byte[]>> paramList) {
    this.mProviderAuthority = (String)Preconditions.checkNotNull(paramString1);
    this.mProviderPackage = (String)Preconditions.checkNotNull(paramString2);
    this.mQuery = (String)Preconditions.checkNotNull(paramString3);
    this.mCertificates = (List<List<byte[]>>)Preconditions.checkNotNull(paramList);
    this.mCertificatesArray = 0;
    this.mIdentifier = createIdentifier(paramString1, paramString2, paramString3);
  }
  
  private String createIdentifier(String paramString1, String paramString2, String paramString3) {
    return paramString1 + "-" + paramString2 + "-" + paramString3;
  }
  
  public List<List<byte[]>> getCertificates() {
    return this.mCertificates;
  }
  
  public int getCertificatesArrayResId() {
    return this.mCertificatesArray;
  }
  
  String getId() {
    return this.mIdentifier;
  }
  
  @Deprecated
  public String getIdentifier() {
    return this.mIdentifier;
  }
  
  public String getProviderAuthority() {
    return this.mProviderAuthority;
  }
  
  public String getProviderPackage() {
    return this.mProviderPackage;
  }
  
  public String getQuery() {
    return this.mQuery;
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("FontRequest {mProviderAuthority: " + this.mProviderAuthority + ", mProviderPackage: " + this.mProviderPackage + ", mQuery: " + this.mQuery + ", mCertificates:");
    for (byte b = 0; b < this.mCertificates.size(); b++) {
      stringBuilder.append(" [");
      List<byte[]> list = this.mCertificates.get(b);
      for (byte b1 = 0; b1 < list.size(); b1++) {
        stringBuilder.append(" \"");
        stringBuilder.append(Base64.encodeToString(list.get(b1), 0));
        stringBuilder.append("\"");
      } 
      stringBuilder.append(" ]");
    } 
    stringBuilder.append("}");
    stringBuilder.append("mCertificatesArray: " + this.mCertificatesArray);
    return stringBuilder.toString();
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\provider\FontRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */