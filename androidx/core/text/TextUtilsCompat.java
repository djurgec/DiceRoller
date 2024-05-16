package androidx.core.text;

import android.os.Build;
import android.text.TextUtils;
import java.util.Locale;

public final class TextUtilsCompat {
  private static final String ARAB_SCRIPT_SUBTAG = "Arab";
  
  private static final String HEBR_SCRIPT_SUBTAG = "Hebr";
  
  private static final Locale ROOT = new Locale("", "");
  
  private static int getLayoutDirectionFromFirstChar(Locale paramLocale) {
    switch (Character.getDirectionality(paramLocale.getDisplayName(paramLocale).charAt(0))) {
      default:
        return 0;
      case 1:
      case 2:
        break;
    } 
    return 1;
  }
  
  public static int getLayoutDirectionFromLocale(Locale paramLocale) {
    if (Build.VERSION.SDK_INT >= 17)
      return TextUtils.getLayoutDirectionFromLocale(paramLocale); 
    if (paramLocale != null && !paramLocale.equals(ROOT)) {
      String str = ICUCompat.maximizeAndGetScript(paramLocale);
      if (str == null)
        return getLayoutDirectionFromFirstChar(paramLocale); 
      if (str.equalsIgnoreCase("Arab") || str.equalsIgnoreCase("Hebr"))
        return 1; 
    } 
    return 0;
  }
  
  public static String htmlEncode(String paramString) {
    if (Build.VERSION.SDK_INT >= 17)
      return TextUtils.htmlEncode(paramString); 
    StringBuilder stringBuilder = new StringBuilder();
    for (byte b = 0; b < paramString.length(); b++) {
      char c = paramString.charAt(b);
      switch (c) {
        default:
          stringBuilder.append(c);
          break;
        case '>':
          stringBuilder.append("&gt;");
          break;
        case '<':
          stringBuilder.append("&lt;");
          break;
        case '\'':
          stringBuilder.append("&#39;");
          break;
        case '&':
          stringBuilder.append("&amp;");
          break;
        case '"':
          stringBuilder.append("&quot;");
          break;
      } 
    } 
    return stringBuilder.toString();
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\text\TextUtilsCompat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */