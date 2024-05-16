package androidx.core.content;

import android.content.LocusId;
import android.os.Build;
import androidx.core.util.Preconditions;

public final class LocusIdCompat {
  private final String mId;
  
  private final LocusId mWrapped;
  
  public LocusIdCompat(String paramString) {
    this.mId = (String)Preconditions.checkStringNotEmpty(paramString, "id cannot be empty");
    if (Build.VERSION.SDK_INT >= 29) {
      this.mWrapped = Api29Impl.create(paramString);
    } else {
      this.mWrapped = null;
    } 
  }
  
  private String getSanitizedId() {
    int i = this.mId.length();
    return i + "_chars";
  }
  
  public static LocusIdCompat toLocusIdCompat(LocusId paramLocusId) {
    Preconditions.checkNotNull(paramLocusId, "locusId cannot be null");
    return new LocusIdCompat((String)Preconditions.checkStringNotEmpty(Api29Impl.getId(paramLocusId), "id cannot be empty"));
  }
  
  public boolean equals(Object paramObject) {
    boolean bool = true;
    if (this == paramObject)
      return true; 
    if (paramObject == null)
      return false; 
    if (getClass() != paramObject.getClass())
      return false; 
    LocusIdCompat locusIdCompat = (LocusIdCompat)paramObject;
    paramObject = this.mId;
    if (paramObject == null) {
      if (locusIdCompat.mId != null)
        bool = false; 
      return bool;
    } 
    return paramObject.equals(locusIdCompat.mId);
  }
  
  public String getId() {
    return this.mId;
  }
  
  public int hashCode() {
    int i;
    String str = this.mId;
    if (str == null) {
      i = 0;
    } else {
      i = str.hashCode();
    } 
    return 1 * 31 + i;
  }
  
  public LocusId toLocusId() {
    return this.mWrapped;
  }
  
  public String toString() {
    return "LocusIdCompat[" + getSanitizedId() + "]";
  }
  
  private static class Api29Impl {
    static LocusId create(String param1String) {
      return new LocusId(param1String);
    }
    
    static String getId(LocusId param1LocusId) {
      return param1LocusId.getId();
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\content\LocusIdCompat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */