package androidx.appcompat.app;

import android.util.AttributeSet;
import java.lang.ref.WeakReference;
import java.util.ArrayDeque;
import java.util.Deque;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

class LayoutIncludeDetector {
  private final Deque<WeakReference<XmlPullParser>> mXmlParserStack = new ArrayDeque<>();
  
  private static boolean isParserOutdated(XmlPullParser paramXmlPullParser) {
    boolean bool = true;
    if (paramXmlPullParser != null)
      try {
        if (paramXmlPullParser.getEventType() != 3) {
          int i = paramXmlPullParser.getEventType();
          if (i != 1)
            bool = false; 
        } 
      } catch (XmlPullParserException xmlPullParserException) {
        return true;
      }  
    return bool;
  }
  
  private static XmlPullParser popOutdatedAttrHolders(Deque<WeakReference<XmlPullParser>> paramDeque) {
    while (!paramDeque.isEmpty()) {
      XmlPullParser xmlPullParser = ((WeakReference<XmlPullParser>)paramDeque.peek()).get();
      if (isParserOutdated(xmlPullParser)) {
        paramDeque.pop();
        continue;
      } 
      return xmlPullParser;
    } 
    return null;
  }
  
  private static boolean shouldInheritContext(XmlPullParser paramXmlPullParser1, XmlPullParser paramXmlPullParser2) {
    if (paramXmlPullParser2 != null && paramXmlPullParser1 != paramXmlPullParser2)
      try {
        if (paramXmlPullParser2.getEventType() == 2)
          return "include".equals(paramXmlPullParser2.getName()); 
      } catch (XmlPullParserException xmlPullParserException) {} 
    return false;
  }
  
  boolean detect(AttributeSet paramAttributeSet) {
    if (paramAttributeSet instanceof XmlPullParser) {
      XmlPullParser xmlPullParser = (XmlPullParser)paramAttributeSet;
      if (xmlPullParser.getDepth() == 1) {
        XmlPullParser xmlPullParser1 = popOutdatedAttrHolders(this.mXmlParserStack);
        this.mXmlParserStack.push(new WeakReference<>(xmlPullParser));
        if (shouldInheritContext(xmlPullParser, xmlPullParser1))
          return true; 
      } 
    } 
    return false;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\appcompat\app\LayoutIncludeDetector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */