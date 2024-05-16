package androidx.core.os;

import android.os.Build;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;

final class LocaleListCompatWrapper implements LocaleListInterface {
  private static final Locale EN_LATN;
  
  private static final Locale LOCALE_AR_XB;
  
  private static final Locale LOCALE_EN_XA;
  
  private static final Locale[] sEmptyList = new Locale[0];
  
  private final Locale[] mList;
  
  private final String mStringRepresentation;
  
  static {
    LOCALE_EN_XA = new Locale("en", "XA");
    LOCALE_AR_XB = new Locale("ar", "XB");
    EN_LATN = LocaleListCompat.forLanguageTagCompat("en-Latn");
  }
  
  LocaleListCompatWrapper(Locale... paramVarArgs) {
    if (paramVarArgs.length == 0) {
      this.mList = sEmptyList;
      this.mStringRepresentation = "";
    } else {
      ArrayList<Locale> arrayList = new ArrayList();
      HashSet<Locale> hashSet = new HashSet();
      StringBuilder stringBuilder = new StringBuilder();
      byte b = 0;
      while (b < paramVarArgs.length) {
        Locale locale = paramVarArgs[b];
        if (locale != null) {
          if (!hashSet.contains(locale)) {
            locale = (Locale)locale.clone();
            arrayList.add(locale);
            toLanguageTag(stringBuilder, locale);
            if (b < paramVarArgs.length - 1)
              stringBuilder.append(','); 
            hashSet.add(locale);
          } 
          b++;
          continue;
        } 
        throw new NullPointerException("list[" + b + "] is null");
      } 
      this.mList = arrayList.<Locale>toArray(new Locale[arrayList.size()]);
      this.mStringRepresentation = stringBuilder.toString();
    } 
  }
  
  private Locale computeFirstMatch(Collection<String> paramCollection, boolean paramBoolean) {
    Locale locale;
    int i = computeFirstMatchIndex(paramCollection, paramBoolean);
    if (i == -1) {
      paramCollection = null;
    } else {
      locale = this.mList[i];
    } 
    return locale;
  }
  
  private int computeFirstMatchIndex(Collection<String> paramCollection, boolean paramBoolean) {
    Locale[] arrayOfLocale = this.mList;
    if (arrayOfLocale.length == 1)
      return 0; 
    if (arrayOfLocale.length == 0)
      return -1; 
    int j = Integer.MAX_VALUE;
    int i = j;
    if (paramBoolean) {
      int k = findFirstMatchIndex(EN_LATN);
      if (k == 0)
        return 0; 
      i = j;
      if (k < Integer.MAX_VALUE)
        i = k; 
    } 
    Iterator<String> iterator = paramCollection.iterator();
    for (j = i; iterator.hasNext(); j = i) {
      int k = findFirstMatchIndex(LocaleListCompat.forLanguageTagCompat(iterator.next()));
      if (k == 0)
        return 0; 
      i = j;
      if (k < j)
        i = k; 
    } 
    return (j == Integer.MAX_VALUE) ? 0 : j;
  }
  
  private int findFirstMatchIndex(Locale paramLocale) {
    byte b = 0;
    while (true) {
      Locale[] arrayOfLocale = this.mList;
      if (b < arrayOfLocale.length) {
        if (matchScore(paramLocale, arrayOfLocale[b]) > 0)
          return b; 
        b++;
        continue;
      } 
      return Integer.MAX_VALUE;
    } 
  }
  
  private static String getLikelyScript(Locale paramLocale) {
    if (Build.VERSION.SDK_INT >= 21) {
      String str = paramLocale.getScript();
      return !str.isEmpty() ? str : "";
    } 
    return "";
  }
  
  private static boolean isPseudoLocale(Locale paramLocale) {
    return (LOCALE_EN_XA.equals(paramLocale) || LOCALE_AR_XB.equals(paramLocale));
  }
  
  private static int matchScore(Locale paramLocale1, Locale paramLocale2) {
    boolean bool1 = paramLocale1.equals(paramLocale2);
    boolean bool = true;
    if (bool1)
      return 1; 
    if (!paramLocale1.getLanguage().equals(paramLocale2.getLanguage()))
      return 0; 
    if (isPseudoLocale(paramLocale1) || isPseudoLocale(paramLocale2))
      return 0; 
    String str = getLikelyScript(paramLocale1);
    if (str.isEmpty()) {
      String str1 = paramLocale1.getCountry();
      if (!str1.isEmpty() && !str1.equals(paramLocale2.getCountry()))
        bool = false; 
      return bool;
    } 
    return str.equals(getLikelyScript(paramLocale2));
  }
  
  static void toLanguageTag(StringBuilder paramStringBuilder, Locale paramLocale) {
    paramStringBuilder.append(paramLocale.getLanguage());
    String str = paramLocale.getCountry();
    if (str != null && !str.isEmpty()) {
      paramStringBuilder.append('-');
      paramStringBuilder.append(paramLocale.getCountry());
    } 
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (!(paramObject instanceof LocaleListCompatWrapper))
      return false; 
    Locale[] arrayOfLocale = ((LocaleListCompatWrapper)paramObject).mList;
    if (this.mList.length != arrayOfLocale.length)
      return false; 
    byte b = 0;
    while (true) {
      paramObject = this.mList;
      if (b < paramObject.length) {
        if (!paramObject[b].equals(arrayOfLocale[b]))
          return false; 
        b++;
        continue;
      } 
      return true;
    } 
  }
  
  public Locale get(int paramInt) {
    if (paramInt >= 0) {
      Locale[] arrayOfLocale = this.mList;
      if (paramInt < arrayOfLocale.length)
        return arrayOfLocale[paramInt]; 
    } 
    return null;
  }
  
  public Locale getFirstMatch(String[] paramArrayOfString) {
    return computeFirstMatch(Arrays.asList(paramArrayOfString), false);
  }
  
  public Object getLocaleList() {
    return null;
  }
  
  public int hashCode() {
    int i = 1;
    byte b = 0;
    while (true) {
      Locale[] arrayOfLocale = this.mList;
      if (b < arrayOfLocale.length) {
        i = i * 31 + arrayOfLocale[b].hashCode();
        b++;
        continue;
      } 
      return i;
    } 
  }
  
  public int indexOf(Locale paramLocale) {
    byte b = 0;
    while (true) {
      Locale[] arrayOfLocale = this.mList;
      if (b < arrayOfLocale.length) {
        if (arrayOfLocale[b].equals(paramLocale))
          return b; 
        b++;
        continue;
      } 
      return -1;
    } 
  }
  
  public boolean isEmpty() {
    boolean bool;
    if (this.mList.length == 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public int size() {
    return this.mList.length;
  }
  
  public String toLanguageTags() {
    return this.mStringRepresentation;
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("[");
    byte b = 0;
    while (true) {
      Locale[] arrayOfLocale = this.mList;
      if (b < arrayOfLocale.length) {
        stringBuilder.append(arrayOfLocale[b]);
        if (b < this.mList.length - 1)
          stringBuilder.append(','); 
        b++;
        continue;
      } 
      stringBuilder.append("]");
      return stringBuilder.toString();
    } 
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\os\LocaleListCompatWrapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */