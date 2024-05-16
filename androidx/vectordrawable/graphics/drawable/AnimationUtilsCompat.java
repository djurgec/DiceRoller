package androidx.vectordrawable.graphics.drawable;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import androidx.interpolator.view.animation.FastOutLinearInInterpolator;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class AnimationUtilsCompat {
  private static Interpolator createInterpolatorFromXml(Context paramContext, Resources paramResources, Resources.Theme paramTheme, XmlPullParser paramXmlPullParser) throws XmlPullParserException, IOException {
    PathInterpolatorCompat pathInterpolatorCompat;
    paramResources = null;
    int i = paramXmlPullParser.getDepth();
    while (true) {
      int j = paramXmlPullParser.next();
      if ((j != 3 || paramXmlPullParser.getDepth() > i) && j != 1) {
        LinearInterpolator linearInterpolator;
        AccelerateInterpolator accelerateInterpolator;
        DecelerateInterpolator decelerateInterpolator;
        AccelerateDecelerateInterpolator accelerateDecelerateInterpolator;
        CycleInterpolator cycleInterpolator;
        AnticipateInterpolator anticipateInterpolator;
        OvershootInterpolator overshootInterpolator;
        AnticipateOvershootInterpolator anticipateOvershootInterpolator;
        BounceInterpolator bounceInterpolator;
        if (j != 2)
          continue; 
        AttributeSet attributeSet = Xml.asAttributeSet(paramXmlPullParser);
        String str = paramXmlPullParser.getName();
        if (str.equals("linearInterpolator")) {
          linearInterpolator = new LinearInterpolator();
          continue;
        } 
        if (linearInterpolator.equals("accelerateInterpolator")) {
          accelerateInterpolator = new AccelerateInterpolator(paramContext, attributeSet);
          continue;
        } 
        if (accelerateInterpolator.equals("decelerateInterpolator")) {
          decelerateInterpolator = new DecelerateInterpolator(paramContext, attributeSet);
          continue;
        } 
        if (decelerateInterpolator.equals("accelerateDecelerateInterpolator")) {
          accelerateDecelerateInterpolator = new AccelerateDecelerateInterpolator();
          continue;
        } 
        if (accelerateDecelerateInterpolator.equals("cycleInterpolator")) {
          cycleInterpolator = new CycleInterpolator(paramContext, attributeSet);
          continue;
        } 
        if (cycleInterpolator.equals("anticipateInterpolator")) {
          anticipateInterpolator = new AnticipateInterpolator(paramContext, attributeSet);
          continue;
        } 
        if (anticipateInterpolator.equals("overshootInterpolator")) {
          overshootInterpolator = new OvershootInterpolator(paramContext, attributeSet);
          continue;
        } 
        if (overshootInterpolator.equals("anticipateOvershootInterpolator")) {
          anticipateOvershootInterpolator = new AnticipateOvershootInterpolator(paramContext, attributeSet);
          continue;
        } 
        if (anticipateOvershootInterpolator.equals("bounceInterpolator")) {
          bounceInterpolator = new BounceInterpolator();
          continue;
        } 
        if (bounceInterpolator.equals("pathInterpolator")) {
          pathInterpolatorCompat = new PathInterpolatorCompat(paramContext, attributeSet, paramXmlPullParser);
          continue;
        } 
        throw new RuntimeException("Unknown interpolator name: " + paramXmlPullParser.getName());
      } 
      break;
    } 
    return pathInterpolatorCompat;
  }
  
  public static Interpolator loadInterpolator(Context paramContext, int paramInt) throws Resources.NotFoundException {
    if (Build.VERSION.SDK_INT >= 21)
      return AnimationUtils.loadInterpolator(paramContext, paramInt); 
    XmlResourceParser xmlResourceParser2 = null;
    XmlResourceParser xmlResourceParser3 = null;
    XmlResourceParser xmlResourceParser1 = null;
    if (paramInt == 17563663) {
      try {
        FastOutLinearInInterpolator fastOutLinearInInterpolator = new FastOutLinearInInterpolator();
        if (false)
          throw new NullPointerException(); 
        return (Interpolator)fastOutLinearInInterpolator;
      } catch (XmlPullParserException xmlPullParserException) {
      
      } catch (IOException iOException) {
      
      } finally {}
    } else {
      LinearOutSlowInInterpolator linearOutSlowInInterpolator;
      if (paramInt == 17563661) {
        FastOutSlowInInterpolator fastOutSlowInInterpolator = new FastOutSlowInInterpolator();
        if (false)
          throw new NullPointerException(); 
        return (Interpolator)fastOutSlowInInterpolator;
      } 
      if (paramInt == 17563662) {
        linearOutSlowInInterpolator = new LinearOutSlowInInterpolator();
        if (false)
          throw new NullPointerException(); 
        return (Interpolator)linearOutSlowInInterpolator;
      } 
      XmlResourceParser xmlResourceParser = linearOutSlowInInterpolator.getResources().getAnimation(paramInt);
      xmlResourceParser1 = xmlResourceParser;
      xmlResourceParser2 = xmlResourceParser;
      xmlResourceParser3 = xmlResourceParser;
      interpolator = createInterpolatorFromXml((Context)linearOutSlowInInterpolator, linearOutSlowInInterpolator.getResources(), linearOutSlowInInterpolator.getTheme(), (XmlPullParser)xmlResourceParser);
      if (xmlResourceParser != null)
        xmlResourceParser.close(); 
      return interpolator;
    } 
    xmlResourceParser1 = xmlResourceParser2;
    Resources.NotFoundException notFoundException = new Resources.NotFoundException();
    xmlResourceParser1 = xmlResourceParser2;
    StringBuilder stringBuilder = new StringBuilder();
    xmlResourceParser1 = xmlResourceParser2;
    this();
    Interpolator interpolator;
    xmlResourceParser1 = xmlResourceParser2;
    this(stringBuilder.append("Can't load animation resource ID #0x").append(Integer.toHexString(paramInt)).toString());
    xmlResourceParser1 = xmlResourceParser2;
    notFoundException.initCause((Throwable)interpolator);
    xmlResourceParser1 = xmlResourceParser2;
    throw notFoundException;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\vectordrawable\graphics\drawable\AnimationUtilsCompat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */