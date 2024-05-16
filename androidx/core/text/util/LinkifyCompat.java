package androidx.core.text.util;

import android.os.Build;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.webkit.WebView;
import android.widget.TextView;
import androidx.core.util.PatternsCompat;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class LinkifyCompat {
  private static final Comparator<LinkSpec> COMPARATOR;
  
  private static final String[] EMPTY_STRING = new String[0];
  
  static {
    COMPARATOR = new Comparator<LinkSpec>() {
        public int compare(LinkifyCompat.LinkSpec param1LinkSpec1, LinkifyCompat.LinkSpec param1LinkSpec2) {
          return (param1LinkSpec1.start < param1LinkSpec2.start) ? -1 : ((param1LinkSpec1.start > param1LinkSpec2.start) ? 1 : ((param1LinkSpec1.end < param1LinkSpec2.end) ? 1 : ((param1LinkSpec1.end > param1LinkSpec2.end) ? -1 : 0)));
        }
      };
  }
  
  private static void addLinkMovementMethod(TextView paramTextView) {
    if (!(paramTextView.getMovementMethod() instanceof LinkMovementMethod) && paramTextView.getLinksClickable())
      paramTextView.setMovementMethod(LinkMovementMethod.getInstance()); 
  }
  
  public static void addLinks(TextView paramTextView, Pattern paramPattern, String paramString) {
    if (shouldAddLinksFallbackToFramework()) {
      Linkify.addLinks(paramTextView, paramPattern, paramString);
      return;
    } 
    addLinks(paramTextView, paramPattern, paramString, (String[])null, (Linkify.MatchFilter)null, (Linkify.TransformFilter)null);
  }
  
  public static void addLinks(TextView paramTextView, Pattern paramPattern, String paramString, Linkify.MatchFilter paramMatchFilter, Linkify.TransformFilter paramTransformFilter) {
    if (shouldAddLinksFallbackToFramework()) {
      Linkify.addLinks(paramTextView, paramPattern, paramString, paramMatchFilter, paramTransformFilter);
      return;
    } 
    addLinks(paramTextView, paramPattern, paramString, (String[])null, paramMatchFilter, paramTransformFilter);
  }
  
  public static void addLinks(TextView paramTextView, Pattern paramPattern, String paramString, String[] paramArrayOfString, Linkify.MatchFilter paramMatchFilter, Linkify.TransformFilter paramTransformFilter) {
    if (shouldAddLinksFallbackToFramework()) {
      Linkify.addLinks(paramTextView, paramPattern, paramString, paramArrayOfString, paramMatchFilter, paramTransformFilter);
      return;
    } 
    SpannableString spannableString = SpannableString.valueOf(paramTextView.getText());
    if (addLinks((Spannable)spannableString, paramPattern, paramString, paramArrayOfString, paramMatchFilter, paramTransformFilter)) {
      paramTextView.setText((CharSequence)spannableString);
      addLinkMovementMethod(paramTextView);
    } 
  }
  
  public static boolean addLinks(Spannable paramSpannable, int paramInt) {
    if (shouldAddLinksFallbackToFramework())
      return Linkify.addLinks(paramSpannable, paramInt); 
    if (paramInt == 0)
      return false; 
    URLSpan[] arrayOfURLSpan = (URLSpan[])paramSpannable.getSpans(0, paramSpannable.length(), URLSpan.class);
    for (int i = arrayOfURLSpan.length - 1; i >= 0; i--)
      paramSpannable.removeSpan(arrayOfURLSpan[i]); 
    if ((paramInt & 0x4) != 0)
      Linkify.addLinks(paramSpannable, 4); 
    ArrayList<LinkSpec> arrayList = new ArrayList();
    if ((paramInt & 0x1) != 0) {
      Pattern pattern = PatternsCompat.AUTOLINK_WEB_URL;
      Linkify.MatchFilter matchFilter = Linkify.sUrlMatchFilter;
      gatherLinks(arrayList, paramSpannable, pattern, new String[] { "http://", "https://", "rtsp://" }, matchFilter, null);
    } 
    if ((paramInt & 0x2) != 0)
      gatherLinks(arrayList, paramSpannable, PatternsCompat.AUTOLINK_EMAIL_ADDRESS, new String[] { "mailto:" }, null, null); 
    if ((paramInt & 0x8) != 0)
      gatherMapLinks(arrayList, paramSpannable); 
    pruneOverlaps(arrayList, paramSpannable);
    if (arrayList.size() == 0)
      return false; 
    for (LinkSpec linkSpec : arrayList) {
      if (linkSpec.frameworkAddedSpan == null)
        applyLink(linkSpec.url, linkSpec.start, linkSpec.end, paramSpannable); 
    } 
    return true;
  }
  
  public static boolean addLinks(Spannable paramSpannable, Pattern paramPattern, String paramString) {
    return shouldAddLinksFallbackToFramework() ? Linkify.addLinks(paramSpannable, paramPattern, paramString) : addLinks(paramSpannable, paramPattern, paramString, (String[])null, (Linkify.MatchFilter)null, (Linkify.TransformFilter)null);
  }
  
  public static boolean addLinks(Spannable paramSpannable, Pattern paramPattern, String paramString, Linkify.MatchFilter paramMatchFilter, Linkify.TransformFilter paramTransformFilter) {
    return shouldAddLinksFallbackToFramework() ? Linkify.addLinks(paramSpannable, paramPattern, paramString, paramMatchFilter, paramTransformFilter) : addLinks(paramSpannable, paramPattern, paramString, (String[])null, paramMatchFilter, paramTransformFilter);
  }
  
  public static boolean addLinks(Spannable paramSpannable, Pattern paramPattern, String paramString, String[] paramArrayOfString, Linkify.MatchFilter paramMatchFilter, Linkify.TransformFilter paramTransformFilter) {
    // Byte code:
    //   0: invokestatic shouldAddLinksFallbackToFramework : ()Z
    //   3: ifeq -> 18
    //   6: aload_0
    //   7: aload_1
    //   8: aload_2
    //   9: aload_3
    //   10: aload #4
    //   12: aload #5
    //   14: invokestatic addLinks : (Landroid/text/Spannable;Ljava/util/regex/Pattern;Ljava/lang/String;[Ljava/lang/String;Landroid/text/util/Linkify$MatchFilter;Landroid/text/util/Linkify$TransformFilter;)Z
    //   17: ireturn
    //   18: aload_2
    //   19: astore #10
    //   21: aload_2
    //   22: ifnonnull -> 29
    //   25: ldc ''
    //   27: astore #10
    //   29: aload_3
    //   30: ifnull -> 41
    //   33: aload_3
    //   34: astore_2
    //   35: aload_3
    //   36: arraylength
    //   37: iconst_1
    //   38: if_icmpge -> 45
    //   41: getstatic androidx/core/text/util/LinkifyCompat.EMPTY_STRING : [Ljava/lang/String;
    //   44: astore_2
    //   45: aload_2
    //   46: arraylength
    //   47: iconst_1
    //   48: iadd
    //   49: anewarray java/lang/String
    //   52: astore #11
    //   54: aload #11
    //   56: iconst_0
    //   57: aload #10
    //   59: getstatic java/util/Locale.ROOT : Ljava/util/Locale;
    //   62: invokevirtual toLowerCase : (Ljava/util/Locale;)Ljava/lang/String;
    //   65: aastore
    //   66: iconst_0
    //   67: istore #6
    //   69: iload #6
    //   71: aload_2
    //   72: arraylength
    //   73: if_icmpge -> 113
    //   76: aload_2
    //   77: iload #6
    //   79: aaload
    //   80: astore_3
    //   81: aload_3
    //   82: ifnonnull -> 91
    //   85: ldc ''
    //   87: astore_3
    //   88: goto -> 99
    //   91: aload_3
    //   92: getstatic java/util/Locale.ROOT : Ljava/util/Locale;
    //   95: invokevirtual toLowerCase : (Ljava/util/Locale;)Ljava/lang/String;
    //   98: astore_3
    //   99: aload #11
    //   101: iload #6
    //   103: iconst_1
    //   104: iadd
    //   105: aload_3
    //   106: aastore
    //   107: iinc #6, 1
    //   110: goto -> 69
    //   113: iconst_0
    //   114: istore #8
    //   116: aload_1
    //   117: aload_0
    //   118: invokevirtual matcher : (Ljava/lang/CharSequence;)Ljava/util/regex/Matcher;
    //   121: astore_1
    //   122: aload_1
    //   123: invokevirtual find : ()Z
    //   126: ifeq -> 195
    //   129: aload_1
    //   130: invokevirtual start : ()I
    //   133: istore #6
    //   135: aload_1
    //   136: invokevirtual end : ()I
    //   139: istore #7
    //   141: iconst_1
    //   142: istore #9
    //   144: aload #4
    //   146: ifnull -> 163
    //   149: aload #4
    //   151: aload_0
    //   152: iload #6
    //   154: iload #7
    //   156: invokeinterface acceptMatch : (Ljava/lang/CharSequence;II)Z
    //   161: istore #9
    //   163: iload #9
    //   165: ifeq -> 192
    //   168: aload_1
    //   169: iconst_0
    //   170: invokevirtual group : (I)Ljava/lang/String;
    //   173: aload #11
    //   175: aload_1
    //   176: aload #5
    //   178: invokestatic makeUrl : (Ljava/lang/String;[Ljava/lang/String;Ljava/util/regex/Matcher;Landroid/text/util/Linkify$TransformFilter;)Ljava/lang/String;
    //   181: iload #6
    //   183: iload #7
    //   185: aload_0
    //   186: invokestatic applyLink : (Ljava/lang/String;IILandroid/text/Spannable;)V
    //   189: iconst_1
    //   190: istore #8
    //   192: goto -> 122
    //   195: iload #8
    //   197: ireturn
  }
  
  public static boolean addLinks(TextView paramTextView, int paramInt) {
    if (shouldAddLinksFallbackToFramework())
      return Linkify.addLinks(paramTextView, paramInt); 
    if (paramInt == 0)
      return false; 
    CharSequence charSequence = paramTextView.getText();
    if (charSequence instanceof Spannable) {
      if (addLinks((Spannable)charSequence, paramInt)) {
        addLinkMovementMethod(paramTextView);
        return true;
      } 
      return false;
    } 
    SpannableString spannableString = SpannableString.valueOf(charSequence);
    if (addLinks((Spannable)spannableString, paramInt)) {
      addLinkMovementMethod(paramTextView);
      paramTextView.setText((CharSequence)spannableString);
      return true;
    } 
    return false;
  }
  
  private static void applyLink(String paramString, int paramInt1, int paramInt2, Spannable paramSpannable) {
    paramSpannable.setSpan(new URLSpan(paramString), paramInt1, paramInt2, 33);
  }
  
  private static String findAddress(String paramString) {
    return (Build.VERSION.SDK_INT >= 28) ? WebView.findAddress(paramString) : FindAddress.findAddress(paramString);
  }
  
  private static void gatherLinks(ArrayList<LinkSpec> paramArrayList, Spannable paramSpannable, Pattern paramPattern, String[] paramArrayOfString, Linkify.MatchFilter paramMatchFilter, Linkify.TransformFilter paramTransformFilter) {
    Matcher matcher = paramPattern.matcher((CharSequence)paramSpannable);
    while (matcher.find()) {
      int i = matcher.start();
      int j = matcher.end();
      if (paramMatchFilter == null || paramMatchFilter.acceptMatch((CharSequence)paramSpannable, i, j)) {
        LinkSpec linkSpec = new LinkSpec();
        linkSpec.url = makeUrl(matcher.group(0), paramArrayOfString, matcher, paramTransformFilter);
        linkSpec.start = i;
        linkSpec.end = j;
        paramArrayList.add(linkSpec);
      } 
    } 
  }
  
  private static void gatherMapLinks(ArrayList<LinkSpec> paramArrayList, Spannable paramSpannable) {
    String str = paramSpannable.toString();
    int i = 0;
    try {
      while (true) {
        String str1 = findAddress(str);
        if (str1 != null) {
          int k = str.indexOf(str1);
          if (k < 0)
            break; 
          LinkSpec linkSpec = new LinkSpec();
          this();
          int j = k + str1.length();
          linkSpec.start = i + k;
          linkSpec.end = i + j;
          str = str.substring(j);
          i += j;
          try {
            str1 = URLEncoder.encode(str1, "UTF-8");
            StringBuilder stringBuilder = new StringBuilder();
            this();
            linkSpec.url = stringBuilder.append("geo:0,0?q=").append(str1).toString();
            paramArrayList.add(linkSpec);
          } catch (UnsupportedEncodingException unsupportedEncodingException) {}
          continue;
        } 
        break;
      } 
      return;
    } catch (UnsupportedOperationException unsupportedOperationException) {
      return;
    } 
  }
  
  private static String makeUrl(String paramString, String[] paramArrayOfString, Matcher paramMatcher, Linkify.TransformFilter paramTransformFilter) {
    boolean bool1;
    String str2 = paramString;
    if (paramTransformFilter != null)
      str2 = paramTransformFilter.transformUrl(paramMatcher, paramString); 
    boolean bool2 = false;
    byte b = 0;
    while (true) {
      bool1 = bool2;
      paramString = str2;
      if (b < paramArrayOfString.length) {
        if (str2.regionMatches(true, 0, paramArrayOfString[b], 0, paramArrayOfString[b].length())) {
          bool2 = true;
          bool1 = bool2;
          paramString = str2;
          if (!str2.regionMatches(false, 0, paramArrayOfString[b], 0, paramArrayOfString[b].length())) {
            paramString = paramArrayOfString[b] + str2.substring(paramArrayOfString[b].length());
            bool1 = bool2;
          } 
          break;
        } 
        b++;
        continue;
      } 
      break;
    } 
    String str1 = paramString;
    if (!bool1) {
      str1 = paramString;
      if (paramArrayOfString.length > 0)
        str1 = paramArrayOfString[0] + paramString; 
    } 
    return str1;
  }
  
  private static void pruneOverlaps(ArrayList<LinkSpec> paramArrayList, Spannable paramSpannable) {
    URLSpan[] arrayOfURLSpan = (URLSpan[])paramSpannable.getSpans(0, paramSpannable.length(), URLSpan.class);
    int i;
    for (i = 0; i < arrayOfURLSpan.length; i++) {
      LinkSpec linkSpec = new LinkSpec();
      linkSpec.frameworkAddedSpan = arrayOfURLSpan[i];
      linkSpec.start = paramSpannable.getSpanStart(arrayOfURLSpan[i]);
      linkSpec.end = paramSpannable.getSpanEnd(arrayOfURLSpan[i]);
      paramArrayList.add(linkSpec);
    } 
    Collections.sort(paramArrayList, COMPARATOR);
    int j = paramArrayList.size();
    for (byte b = 0; b < j - 1; b++) {
      LinkSpec linkSpec1 = paramArrayList.get(b);
      LinkSpec linkSpec2 = paramArrayList.get(b + 1);
      i = -1;
      if (linkSpec1.start <= linkSpec2.start && linkSpec1.end > linkSpec2.start) {
        if (linkSpec2.end <= linkSpec1.end) {
          i = b + 1;
        } else if (linkSpec1.end - linkSpec1.start > linkSpec2.end - linkSpec2.start) {
          i = b + 1;
        } else if (linkSpec1.end - linkSpec1.start < linkSpec2.end - linkSpec2.start) {
          i = b;
        } 
        if (i != -1) {
          URLSpan uRLSpan = ((LinkSpec)paramArrayList.get(i)).frameworkAddedSpan;
          if (uRLSpan != null)
            paramSpannable.removeSpan(uRLSpan); 
          paramArrayList.remove(i);
          j--;
          continue;
        } 
      } 
    } 
  }
  
  private static boolean shouldAddLinksFallbackToFramework() {
    boolean bool;
    if (Build.VERSION.SDK_INT >= 28) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private static class LinkSpec {
    int end;
    
    URLSpan frameworkAddedSpan;
    
    int start;
    
    String url;
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface LinkifyMask {}
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\tex\\util\LinkifyCompat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */