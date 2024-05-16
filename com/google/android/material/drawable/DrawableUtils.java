package com.google.android.material.drawable;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Xml;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public final class DrawableUtils {
  public static AttributeSet parseDrawableXml(Context paramContext, int paramInt, CharSequence paramCharSequence) {
    try {
      int i;
      XmlResourceParser xmlResourceParser = paramContext.getResources().getXml(paramInt);
      do {
        i = xmlResourceParser.next();
      } while (i != 2 && i != 1);
      if (i == 2) {
        if (TextUtils.equals(xmlResourceParser.getName(), paramCharSequence))
          return Xml.asAttributeSet((XmlPullParser)xmlResourceParser); 
        XmlPullParserException xmlPullParserException1 = new XmlPullParserException();
        StringBuilder stringBuilder = new StringBuilder();
        this();
        this(stringBuilder.append("Must have a <").append(paramCharSequence).append("> start tag").toString());
        throw xmlPullParserException1;
      } 
      XmlPullParserException xmlPullParserException = new XmlPullParserException();
      this("No start tag found");
      throw xmlPullParserException;
    } catch (XmlPullParserException|java.io.IOException xmlPullParserException) {
      Resources.NotFoundException notFoundException = new Resources.NotFoundException("Can't load badge resource ID #0x" + Integer.toHexString(paramInt));
      notFoundException.initCause((Throwable)xmlPullParserException);
      throw notFoundException;
    } 
  }
  
  public static void setRippleDrawableRadius(RippleDrawable paramRippleDrawable, int paramInt) {
    if (Build.VERSION.SDK_INT >= 23) {
      paramRippleDrawable.setRadius(paramInt);
    } else {
      try {
        RippleDrawable.class.getDeclaredMethod("setMaxRadius", new Class[] { int.class }).invoke(paramRippleDrawable, new Object[] { Integer.valueOf(paramInt) });
        return;
      } catch (NoSuchMethodException|java.lang.reflect.InvocationTargetException|IllegalAccessException noSuchMethodException) {
        throw new IllegalStateException("Couldn't set RippleDrawable radius", noSuchMethodException);
      } 
    } 
  }
  
  public static PorterDuffColorFilter updateTintFilter(Drawable paramDrawable, ColorStateList paramColorStateList, PorterDuff.Mode paramMode) {
    return (paramColorStateList == null || paramMode == null) ? null : new PorterDuffColorFilter(paramColorStateList.getColorForState(paramDrawable.getState(), 0), paramMode);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\drawable\DrawableUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */