package androidx.core.os;

import java.util.Locale;

interface LocaleListInterface {
  Locale get(int paramInt);
  
  Locale getFirstMatch(String[] paramArrayOfString);
  
  Object getLocaleList();
  
  int indexOf(Locale paramLocale);
  
  boolean isEmpty();
  
  int size();
  
  String toLanguageTags();
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\os\LocaleListInterface.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */