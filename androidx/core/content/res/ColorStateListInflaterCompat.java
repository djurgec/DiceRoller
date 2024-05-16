package androidx.core.content.res;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.util.StateSet;
import android.util.TypedValue;
import android.util.Xml;
import androidx.core.R;
import androidx.core.math.MathUtils;
import androidx.core.os.BuildCompat;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public final class ColorStateListInflaterCompat {
  private static final ThreadLocal<TypedValue> sTempTypedValue = new ThreadLocal<>();
  
  public static ColorStateList createFromXml(Resources paramResources, XmlPullParser paramXmlPullParser, Resources.Theme paramTheme) throws XmlPullParserException, IOException {
    int i;
    AttributeSet attributeSet = Xml.asAttributeSet(paramXmlPullParser);
    while (true) {
      i = paramXmlPullParser.next();
      if (i != 2 && i != 1)
        continue; 
      break;
    } 
    if (i == 2)
      return createFromXmlInner(paramResources, paramXmlPullParser, attributeSet, paramTheme); 
    throw new XmlPullParserException("No start tag found");
  }
  
  public static ColorStateList createFromXmlInner(Resources paramResources, XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet, Resources.Theme paramTheme) throws XmlPullParserException, IOException {
    String str = paramXmlPullParser.getName();
    if (str.equals("selector"))
      return inflate(paramResources, paramXmlPullParser, paramAttributeSet, paramTheme); 
    throw new XmlPullParserException(paramXmlPullParser.getPositionDescription() + ": invalid color state list tag " + str);
  }
  
  private static TypedValue getTypedValue() {
    ThreadLocal<TypedValue> threadLocal = sTempTypedValue;
    TypedValue typedValue2 = threadLocal.get();
    TypedValue typedValue1 = typedValue2;
    if (typedValue2 == null) {
      typedValue1 = new TypedValue();
      threadLocal.set(typedValue1);
    } 
    return typedValue1;
  }
  
  public static ColorStateList inflate(Resources paramResources, int paramInt, Resources.Theme paramTheme) {
    try {
      return createFromXml(paramResources, (XmlPullParser)paramResources.getXml(paramInt), paramTheme);
    } catch (Exception exception) {
      Log.e("CSLCompat", "Failed to inflate ColorStateList.", exception);
      return null;
    } 
  }
  
  private static ColorStateList inflate(Resources paramResources, XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet, Resources.Theme paramTheme) throws XmlPullParserException, IOException {
    int i = paramXmlPullParser.getDepth() + 1;
    int[][] arrayOfInt3 = new int[20][];
    int[] arrayOfInt4 = new int[arrayOfInt3.length];
    byte b = 0;
    while (true) {
      int j = paramXmlPullParser.next();
      int k = j;
      if (j != 1) {
        j = paramXmlPullParser.getDepth();
        if (j >= i || k != 3) {
          float f2;
          if (k != 2 || j > i || !paramXmlPullParser.getName().equals("item"))
            continue; 
          TypedArray typedArray = obtainAttributes(paramResources, paramTheme, paramAttributeSet, R.styleable.ColorStateListItem);
          j = typedArray.getResourceId(R.styleable.ColorStateListItem_android_color, -1);
          if (j != -1 && !isColorInt(paramResources, j)) {
            try {
              j = createFromXml(paramResources, (XmlPullParser)paramResources.getXml(j), paramTheme).getDefaultColor();
            } catch (Exception exception) {
              j = typedArray.getColor(R.styleable.ColorStateListItem_android_color, -65281);
            } 
          } else {
            j = typedArray.getColor(R.styleable.ColorStateListItem_android_color, -65281);
          } 
          float f1 = 1.0F;
          if (typedArray.hasValue(R.styleable.ColorStateListItem_android_alpha)) {
            f1 = typedArray.getFloat(R.styleable.ColorStateListItem_android_alpha, 1.0F);
          } else if (typedArray.hasValue(R.styleable.ColorStateListItem_alpha)) {
            f1 = typedArray.getFloat(R.styleable.ColorStateListItem_alpha, 1.0F);
          } 
          if (BuildCompat.isAtLeastS() && typedArray.hasValue(R.styleable.ColorStateListItem_android_lStar)) {
            f2 = typedArray.getFloat(R.styleable.ColorStateListItem_android_lStar, -1.0F);
          } else {
            f2 = typedArray.getFloat(R.styleable.ColorStateListItem_lStar, -1.0F);
          } 
          typedArray.recycle();
          int m = 0;
          int n = paramAttributeSet.getAttributeCount();
          int[] arrayOfInt = new int[n];
          byte b1 = 0;
          while (b1 < n) {
            int i2 = paramAttributeSet.getAttributeNameResource(b1);
            int i1 = m;
            if (i2 != 16843173) {
              i1 = m;
              if (i2 != 16843551) {
                i1 = m;
                if (i2 != R.attr.alpha) {
                  i1 = m;
                  if (i2 != R.attr.lStar) {
                    if (paramAttributeSet.getAttributeBooleanValue(b1, false)) {
                      i1 = i2;
                    } else {
                      i1 = -i2;
                    } 
                    arrayOfInt[m] = i1;
                    i1 = m + 1;
                  } 
                } 
              } 
            } 
            b1++;
            m = i1;
          } 
          arrayOfInt = StateSet.trimStateSet(arrayOfInt, m);
          arrayOfInt4 = GrowingArrayUtils.append(arrayOfInt4, b, modulateColorAlpha(j, f1, f2));
          arrayOfInt3 = GrowingArrayUtils.<int[]>append(arrayOfInt3, b, arrayOfInt);
          b++;
          continue;
        } 
      } 
      break;
    } 
    int[] arrayOfInt2 = new int[b];
    int[][] arrayOfInt1 = new int[b][];
    System.arraycopy(arrayOfInt4, 0, arrayOfInt2, 0, b);
    System.arraycopy(arrayOfInt3, 0, arrayOfInt1, 0, b);
    return new ColorStateList(arrayOfInt1, arrayOfInt2);
  }
  
  private static boolean isColorInt(Resources paramResources, int paramInt) {
    TypedValue typedValue = getTypedValue();
    boolean bool = true;
    paramResources.getValue(paramInt, typedValue, true);
    if (typedValue.type < 28 || typedValue.type > 31)
      bool = false; 
    return bool;
  }
  
  private static int modulateColorAlpha(int paramInt, float paramFloat1, float paramFloat2) {
    boolean bool;
    if (paramFloat2 >= 0.0F && paramFloat2 <= 100.0F) {
      bool = true;
    } else {
      bool = false;
    } 
    if (paramFloat1 == 1.0F && !bool)
      return paramInt; 
    int j = MathUtils.clamp((int)(Color.alpha(paramInt) * paramFloat1 + 0.5F), 0, 255);
    int i = paramInt;
    if (bool) {
      CamColor camColor = CamColor.fromColor(paramInt);
      i = CamColor.toColor(camColor.getHue(), camColor.getChroma(), paramFloat2);
    } 
    return 0xFFFFFF & i | j << 24;
  }
  
  private static TypedArray obtainAttributes(Resources paramResources, Resources.Theme paramTheme, AttributeSet paramAttributeSet, int[] paramArrayOfint) {
    TypedArray typedArray;
    if (paramTheme == null) {
      typedArray = paramResources.obtainAttributes(paramAttributeSet, paramArrayOfint);
    } else {
      typedArray = paramTheme.obtainStyledAttributes(paramAttributeSet, paramArrayOfint, 0, 0);
    } 
    return typedArray;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\content\res\ColorStateListInflaterCompat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */