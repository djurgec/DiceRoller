package androidx.core.content.res;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public final class ComplexColorCompat {
  private static final String LOG_TAG = "ComplexColorCompat";
  
  private int mColor;
  
  private final ColorStateList mColorStateList;
  
  private final Shader mShader;
  
  private ComplexColorCompat(Shader paramShader, ColorStateList paramColorStateList, int paramInt) {
    this.mShader = paramShader;
    this.mColorStateList = paramColorStateList;
    this.mColor = paramInt;
  }
  
  private static ComplexColorCompat createFromXml(Resources paramResources, int paramInt, Resources.Theme paramTheme) throws IOException, XmlPullParserException {
    int i;
    XmlResourceParser xmlResourceParser = paramResources.getXml(paramInt);
    AttributeSet attributeSet = Xml.asAttributeSet((XmlPullParser)xmlResourceParser);
    while (true) {
      i = xmlResourceParser.next();
      paramInt = 1;
      if (i != 2 && i != 1)
        continue; 
      break;
    } 
    if (i == 2) {
      String str = xmlResourceParser.getName();
      switch (str.hashCode()) {
        default:
          paramInt = -1;
          break;
        case 1191572447:
          if (str.equals("selector")) {
            paramInt = 0;
            break;
          } 
        case 89650992:
          if (str.equals("gradient"))
            break; 
      } 
      switch (paramInt) {
        default:
          throw new XmlPullParserException(xmlResourceParser.getPositionDescription() + ": unsupported complex color tag " + str);
        case 1:
          return from(GradientColorInflaterCompat.createFromXmlInner(paramResources, (XmlPullParser)xmlResourceParser, attributeSet, paramTheme));
        case 0:
          break;
      } 
      return from(ColorStateListInflaterCompat.createFromXmlInner(paramResources, (XmlPullParser)xmlResourceParser, attributeSet, paramTheme));
    } 
    throw new XmlPullParserException("No start tag found");
  }
  
  static ComplexColorCompat from(int paramInt) {
    return new ComplexColorCompat(null, null, paramInt);
  }
  
  static ComplexColorCompat from(ColorStateList paramColorStateList) {
    return new ComplexColorCompat(null, paramColorStateList, paramColorStateList.getDefaultColor());
  }
  
  static ComplexColorCompat from(Shader paramShader) {
    return new ComplexColorCompat(paramShader, null, 0);
  }
  
  public static ComplexColorCompat inflate(Resources paramResources, int paramInt, Resources.Theme paramTheme) {
    try {
      return createFromXml(paramResources, paramInt, paramTheme);
    } catch (Exception exception) {
      Log.e("ComplexColorCompat", "Failed to inflate ComplexColor.", exception);
      return null;
    } 
  }
  
  public int getColor() {
    return this.mColor;
  }
  
  public Shader getShader() {
    return this.mShader;
  }
  
  public boolean isGradient() {
    boolean bool;
    if (this.mShader != null) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isStateful() {
    if (this.mShader == null) {
      ColorStateList colorStateList = this.mColorStateList;
      if (colorStateList != null && colorStateList.isStateful())
        return true; 
    } 
    return false;
  }
  
  public boolean onStateChanged(int[] paramArrayOfint) {
    boolean bool2 = false;
    boolean bool1 = bool2;
    if (isStateful()) {
      ColorStateList colorStateList = this.mColorStateList;
      int i = colorStateList.getColorForState(paramArrayOfint, colorStateList.getDefaultColor());
      bool1 = bool2;
      if (i != this.mColor) {
        bool1 = true;
        this.mColor = i;
      } 
    } 
    return bool1;
  }
  
  public void setColor(int paramInt) {
    this.mColor = paramInt;
  }
  
  public boolean willDraw() {
    return (isGradient() || this.mColor != 0);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\content\res\ComplexColorCompat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */