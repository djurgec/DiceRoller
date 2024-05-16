package androidx.appcompat.resources;

import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public final class Compatibility {
  public static class Api15Impl {
    public static void getValueForDensity(Resources param1Resources, int param1Int1, int param1Int2, TypedValue param1TypedValue, boolean param1Boolean) {
      param1Resources.getValueForDensity(param1Int1, param1Int2, param1TypedValue, param1Boolean);
    }
  }
  
  public static class Api18Impl {
    public static void setAutoCancel(ObjectAnimator param1ObjectAnimator, boolean param1Boolean) {
      param1ObjectAnimator.setAutoCancel(param1Boolean);
    }
  }
  
  public static class Api21Impl {
    public static Drawable createFromXmlInner(Resources param1Resources, XmlPullParser param1XmlPullParser, AttributeSet param1AttributeSet, Resources.Theme param1Theme) throws IOException, XmlPullParserException {
      return Drawable.createFromXmlInner(param1Resources, param1XmlPullParser, param1AttributeSet, param1Theme);
    }
    
    public static int getChangingConfigurations(TypedArray param1TypedArray) {
      return param1TypedArray.getChangingConfigurations();
    }
    
    public static void inflate(Drawable param1Drawable, Resources param1Resources, XmlPullParser param1XmlPullParser, AttributeSet param1AttributeSet, Resources.Theme param1Theme) throws IOException, XmlPullParserException {
      param1Drawable.inflate(param1Resources, param1XmlPullParser, param1AttributeSet, param1Theme);
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\appcompat\resources\Compatibility.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */