package androidx.core.net;

import android.net.Uri;
import androidx.core.util.Preconditions;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public final class MailTo {
  private static final String BCC = "bcc";
  
  private static final String BODY = "body";
  
  private static final String CC = "cc";
  
  private static final String MAILTO = "mailto";
  
  public static final String MAILTO_SCHEME = "mailto:";
  
  private static final String SUBJECT = "subject";
  
  private static final String TO = "to";
  
  private HashMap<String, String> mHeaders = new HashMap<>();
  
  public static boolean isMailTo(Uri paramUri) {
    boolean bool;
    if (paramUri != null && "mailto".equals(paramUri.getScheme())) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public static boolean isMailTo(String paramString) {
    boolean bool;
    if (paramString != null && paramString.startsWith("mailto:")) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public static MailTo parse(Uri paramUri) throws ParseException {
    return parse(paramUri.toString());
  }
  
  public static MailTo parse(String paramString) throws ParseException {
    Preconditions.checkNotNull(paramString);
    if (isMailTo(paramString)) {
      int i = paramString.indexOf('#');
      String str1 = paramString;
      if (i != -1)
        str1 = paramString.substring(0, i); 
      i = str1.indexOf('?');
      if (i == -1) {
        paramString = Uri.decode(str1.substring("mailto:".length()));
        str1 = null;
      } else {
        paramString = Uri.decode(str1.substring("mailto:".length(), i));
        str1 = str1.substring(i + 1);
      } 
      MailTo mailTo = new MailTo();
      if (str1 != null) {
        String[] arrayOfString = str1.split("&");
        int j = arrayOfString.length;
        for (i = 0; i < j; i++) {
          String[] arrayOfString1 = arrayOfString[i].split("=", 2);
          if (arrayOfString1.length != 0) {
            String str = Uri.decode(arrayOfString1[0]).toLowerCase(Locale.ROOT);
            if (arrayOfString1.length > 1) {
              String str3 = Uri.decode(arrayOfString1[1]);
            } else {
              arrayOfString1 = null;
            } 
            mailTo.mHeaders.put(str, (String)arrayOfString1);
          } 
        } 
      } 
      String str2 = mailTo.getTo();
      str1 = paramString;
      if (str2 != null)
        str1 = paramString + ", " + str2; 
      mailTo.mHeaders.put("to", str1);
      return mailTo;
    } 
    throw new ParseException("Not a mailto scheme");
  }
  
  public String getBcc() {
    return this.mHeaders.get("bcc");
  }
  
  public String getBody() {
    return this.mHeaders.get("body");
  }
  
  public String getCc() {
    return this.mHeaders.get("cc");
  }
  
  public Map<String, String> getHeaders() {
    return this.mHeaders;
  }
  
  public String getSubject() {
    return this.mHeaders.get("subject");
  }
  
  public String getTo() {
    return this.mHeaders.get("to");
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder("mailto:");
    stringBuilder.append('?');
    for (Map.Entry<String, String> entry : this.mHeaders.entrySet()) {
      stringBuilder.append(Uri.encode((String)entry.getKey()));
      stringBuilder.append('=');
      stringBuilder.append(Uri.encode((String)entry.getValue()));
      stringBuilder.append('&');
    } 
    return stringBuilder.toString();
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\net\MailTo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */