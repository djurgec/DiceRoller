package androidx.core.content.pm;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.util.Log;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class ShortcutXmlParser {
  private static final String ATTR_SHORTCUT_ID = "shortcutId";
  
  private static final Object GET_INSTANCE_LOCK = new Object();
  
  private static final String META_DATA_APP_SHORTCUTS = "android.app.shortcuts";
  
  private static final String TAG = "ShortcutXmlParser";
  
  private static final String TAG_SHORTCUT = "shortcut";
  
  private static volatile ArrayList<String> sShortcutIds;
  
  private static String getAttributeValue(XmlPullParser paramXmlPullParser, String paramString) {
    String str2 = paramXmlPullParser.getAttributeValue("http://schemas.android.com/apk/res/android", paramString);
    String str1 = str2;
    if (str2 == null)
      str1 = paramXmlPullParser.getAttributeValue(null, paramString); 
    return str1;
  }
  
  public static List<String> getShortcutIds(Context paramContext) {
    if (sShortcutIds == null)
      synchronized (GET_INSTANCE_LOCK) {
        if (sShortcutIds == null) {
          ArrayList<String> arrayList = new ArrayList();
          this();
          sShortcutIds = arrayList;
          sShortcutIds.addAll(parseShortcutIds(paramContext));
        } 
      }  
    return sShortcutIds;
  }
  
  private static XmlResourceParser getXmlResourceParser(Context paramContext, ActivityInfo paramActivityInfo) {
    XmlResourceParser xmlResourceParser = paramActivityInfo.loadXmlMetaData(paramContext.getPackageManager(), "android.app.shortcuts");
    if (xmlResourceParser != null)
      return xmlResourceParser; 
    throw new IllegalArgumentException("Failed to open android.app.shortcuts meta-data resource of " + paramActivityInfo.name);
  }
  
  public static List<String> parseShortcutIds(XmlPullParser paramXmlPullParser) throws IOException, XmlPullParserException {
    ArrayList<String> arrayList = new ArrayList(1);
    while (true) {
      int i = paramXmlPullParser.next();
      if (i != 1 && (i != 3 || paramXmlPullParser.getDepth() > 0)) {
        int j = paramXmlPullParser.getDepth();
        String str = paramXmlPullParser.getName();
        if (i == 2 && j == 2 && "shortcut".equals(str)) {
          str = getAttributeValue(paramXmlPullParser, "shortcutId");
          if (str == null)
            continue; 
          arrayList.add(str);
        } 
        continue;
      } 
      break;
    } 
    return arrayList;
  }
  
  private static Set<String> parseShortcutIds(Context paramContext) {
    HashSet<String> hashSet = new HashSet();
    Intent intent = new Intent("android.intent.action.MAIN");
    intent.addCategory("android.intent.category.LAUNCHER");
    intent.setPackage(paramContext.getPackageName());
    List list = paramContext.getPackageManager().queryIntentActivities(intent, 128);
    if (list == null || list.size() == 0)
      return hashSet; 
    try {
      Iterator iterator = list.iterator();
      while (iterator.hasNext()) {
        ActivityInfo activityInfo = ((ResolveInfo)iterator.next()).activityInfo;
        Bundle bundle = activityInfo.metaData;
        if (bundle != null && bundle.containsKey("android.app.shortcuts")) {
          XmlResourceParser xmlResourceParser = getXmlResourceParser(paramContext, activityInfo);
          try {
            hashSet.addAll(parseShortcutIds((XmlPullParser)xmlResourceParser));
          } finally {
            if (xmlResourceParser != null)
              try {
                xmlResourceParser.close();
              } finally {
                iterator = null;
              }  
          } 
        } 
      } 
    } catch (Exception exception) {
      Log.e("ShortcutXmlParser", "Failed to parse the Xml resource: ", exception);
    } 
    return hashSet;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\content\pm\ShortcutXmlParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */