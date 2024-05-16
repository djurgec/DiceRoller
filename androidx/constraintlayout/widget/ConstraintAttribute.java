package androidx.constraintlayout.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.TypedValue;
import android.util.Xml;
import android.view.View;
import androidx.constraintlayout.motion.widget.Debug;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import org.xmlpull.v1.XmlPullParser;

public class ConstraintAttribute {
  private static final String TAG = "TransitionLayout";
  
  boolean mBooleanValue;
  
  private int mColorValue;
  
  private float mFloatValue;
  
  private int mIntegerValue;
  
  private boolean mMethod = false;
  
  String mName;
  
  private String mStringValue;
  
  private AttributeType mType;
  
  public ConstraintAttribute(ConstraintAttribute paramConstraintAttribute, Object paramObject) {
    this.mName = paramConstraintAttribute.mName;
    this.mType = paramConstraintAttribute.mType;
    setValue(paramObject);
  }
  
  public ConstraintAttribute(String paramString, AttributeType paramAttributeType) {
    this.mName = paramString;
    this.mType = paramAttributeType;
  }
  
  public ConstraintAttribute(String paramString, AttributeType paramAttributeType, Object paramObject, boolean paramBoolean) {
    this.mName = paramString;
    this.mType = paramAttributeType;
    this.mMethod = paramBoolean;
    setValue(paramObject);
  }
  
  private static int clamp(int paramInt) {
    paramInt = (paramInt & (paramInt >> 31 ^ 0xFFFFFFFF)) - 255;
    return (paramInt & paramInt >> 31) + 255;
  }
  
  public static HashMap<String, ConstraintAttribute> extractAttributes(HashMap<String, ConstraintAttribute> paramHashMap, View paramView) {
    HashMap<Object, Object> hashMap = new HashMap<>();
    Class<?> clazz = paramView.getClass();
    for (String str : paramHashMap.keySet()) {
      ConstraintAttribute constraintAttribute = paramHashMap.get(str);
      try {
        if (str.equals("BackgroundColor")) {
          int i = ((ColorDrawable)paramView.getBackground()).getColor();
          ConstraintAttribute constraintAttribute2 = new ConstraintAttribute();
          this(constraintAttribute, Integer.valueOf(i));
          hashMap.put(str, constraintAttribute2);
          continue;
        } 
        String str1 = String.valueOf(str);
        if (str1.length() != 0) {
          str1 = "getMap".concat(str1);
        } else {
          str1 = new String("getMap");
        } 
        Object object = clazz.getMethod(str1, new Class[0]).invoke(paramView, new Object[0]);
        ConstraintAttribute constraintAttribute1 = new ConstraintAttribute();
        this(constraintAttribute, object);
        hashMap.put(str, constraintAttribute1);
      } catch (NoSuchMethodException noSuchMethodException) {
        noSuchMethodException.printStackTrace();
      } catch (IllegalAccessException illegalAccessException) {
        illegalAccessException.printStackTrace();
      } catch (InvocationTargetException invocationTargetException) {
        invocationTargetException.printStackTrace();
      } 
    } 
    return (HashMap)hashMap;
  }
  
  public static void parse(Context paramContext, XmlPullParser paramXmlPullParser, HashMap<String, ConstraintAttribute> paramHashMap) {
    Integer integer;
    TypedArray typedArray = paramContext.obtainStyledAttributes(Xml.asAttributeSet(paramXmlPullParser), R.styleable.CustomAttribute);
    String str = null;
    boolean bool = false;
    XmlPullParser xmlPullParser = null;
    AttributeType attributeType = null;
    int i = typedArray.getIndexCount();
    byte b = 0;
    while (b < i) {
      Integer integer1;
      boolean bool1;
      AttributeType attributeType1;
      String str1;
      int j = typedArray.getIndex(b);
      if (j == R.styleable.CustomAttribute_attributeName) {
        str = typedArray.getString(j);
        str1 = str;
        bool1 = bool;
        paramXmlPullParser = xmlPullParser;
        attributeType1 = attributeType;
        if (str != null) {
          str1 = str;
          bool1 = bool;
          paramXmlPullParser = xmlPullParser;
          attributeType1 = attributeType;
          if (str.length() > 0) {
            char c = Character.toUpperCase(str.charAt(0));
            String str2 = str.substring(1);
            str1 = (new StringBuilder(String.valueOf(str2).length() + 1)).append(c).append(str2).toString();
            bool1 = bool;
            XmlPullParser xmlPullParser1 = xmlPullParser;
            attributeType1 = attributeType;
          } 
        } 
      } else if (j == R.styleable.CustomAttribute_methodName) {
        bool1 = true;
        str1 = typedArray.getString(j);
        paramXmlPullParser = xmlPullParser;
        attributeType1 = attributeType;
      } else if (j == R.styleable.CustomAttribute_customBoolean) {
        Boolean bool2 = Boolean.valueOf(typedArray.getBoolean(j, false));
        attributeType1 = AttributeType.BOOLEAN_TYPE;
        str1 = str;
        bool1 = bool;
      } else if (j == R.styleable.CustomAttribute_customColorValue) {
        attributeType1 = AttributeType.COLOR_TYPE;
        integer1 = Integer.valueOf(typedArray.getColor(j, 0));
        str1 = str;
        bool1 = bool;
      } else if (j == R.styleable.CustomAttribute_customColorDrawableValue) {
        attributeType1 = AttributeType.COLOR_DRAWABLE_TYPE;
        integer1 = Integer.valueOf(typedArray.getColor(j, 0));
        str1 = str;
        bool1 = bool;
      } else if (j == R.styleable.CustomAttribute_customPixelDimension) {
        attributeType1 = AttributeType.DIMENSION_TYPE;
        Float float_ = Float.valueOf(TypedValue.applyDimension(1, typedArray.getDimension(j, 0.0F), paramContext.getResources().getDisplayMetrics()));
        str1 = str;
        bool1 = bool;
      } else if (j == R.styleable.CustomAttribute_customDimension) {
        attributeType1 = AttributeType.DIMENSION_TYPE;
        Float float_ = Float.valueOf(typedArray.getDimension(j, 0.0F));
        str1 = str;
        bool1 = bool;
      } else if (j == R.styleable.CustomAttribute_customFloatValue) {
        attributeType1 = AttributeType.FLOAT_TYPE;
        Float float_ = Float.valueOf(typedArray.getFloat(j, Float.NaN));
        str1 = str;
        bool1 = bool;
      } else if (j == R.styleable.CustomAttribute_customIntegerValue) {
        attributeType1 = AttributeType.INT_TYPE;
        integer1 = Integer.valueOf(typedArray.getInteger(j, -1));
        str1 = str;
        bool1 = bool;
      } else if (j == R.styleable.CustomAttribute_customStringValue) {
        attributeType1 = AttributeType.STRING_TYPE;
        String str2 = typedArray.getString(j);
        str1 = str;
        bool1 = bool;
      } else {
        str1 = str;
        bool1 = bool;
        paramXmlPullParser = xmlPullParser;
        attributeType1 = attributeType;
        if (j == R.styleable.CustomAttribute_customReference) {
          attributeType1 = AttributeType.REFERENCE_TYPE;
          int m = typedArray.getResourceId(j, -1);
          int k = m;
          if (m == -1)
            k = typedArray.getInt(j, -1); 
          integer1 = Integer.valueOf(k);
          bool1 = bool;
          str1 = str;
        } 
      } 
      b++;
      str = str1;
      bool = bool1;
      integer = integer1;
      attributeType = attributeType1;
    } 
    if (str != null && integer != null)
      paramHashMap.put(str, new ConstraintAttribute(str, attributeType, integer, bool)); 
    typedArray.recycle();
  }
  
  public static void setAttributes(View paramView, HashMap<String, ConstraintAttribute> paramHashMap) {
    Class<?> clazz = paramView.getClass();
    for (String str3 : paramHashMap.keySet()) {
      ConstraintAttribute constraintAttribute = paramHashMap.get(str3);
      String str2 = str3;
      String str1 = str2;
      if (!constraintAttribute.mMethod) {
        str1 = String.valueOf(str2);
        if (str1.length() != 0) {
          str1 = "set".concat(str1);
        } else {
          str1 = new String("set");
        } 
      } 
      try {
        ColorDrawable colorDrawable;
        Method method;
        switch (constraintAttribute.mType) {
          default:
            continue;
          case null:
            clazz.getMethod(str1, new Class[] { float.class }).invoke(paramView, new Object[] { Float.valueOf(constraintAttribute.mFloatValue) });
            continue;
          case null:
            clazz.getMethod(str1, new Class[] { float.class }).invoke(paramView, new Object[] { Float.valueOf(constraintAttribute.mFloatValue) });
            continue;
          case null:
            clazz.getMethod(str1, new Class[] { int.class }).invoke(paramView, new Object[] { Integer.valueOf(constraintAttribute.mIntegerValue) });
            continue;
          case null:
            method = clazz.getMethod(str1, new Class[] { Drawable.class });
            colorDrawable = new ColorDrawable();
            this();
            colorDrawable.setColor(constraintAttribute.mColorValue);
            method.invoke(paramView, new Object[] { colorDrawable });
            continue;
          case null:
            clazz.getMethod(str1, new Class[] { int.class }).invoke(paramView, new Object[] { Integer.valueOf(constraintAttribute.mColorValue) });
            continue;
          case null:
            clazz.getMethod(str1, new Class[] { CharSequence.class }).invoke(paramView, new Object[] { constraintAttribute.mStringValue });
            continue;
          case null:
            clazz.getMethod(str1, new Class[] { boolean.class }).invoke(paramView, new Object[] { Boolean.valueOf(constraintAttribute.mBooleanValue) });
            continue;
          case null:
            break;
        } 
        clazz.getMethod(str1, new Class[] { int.class }).invoke(paramView, new Object[] { Integer.valueOf(constraintAttribute.mIntegerValue) });
      } catch (NoSuchMethodException noSuchMethodException) {
        Log.e("TransitionLayout", noSuchMethodException.getMessage());
        String str = clazz.getName();
        Log.e("TransitionLayout", (new StringBuilder(String.valueOf(str3).length() + 34 + String.valueOf(str).length())).append(" Custom Attribute \"").append(str3).append("\" not found on ").append(str).toString());
        str = clazz.getName();
        Log.e("TransitionLayout", (new StringBuilder(String.valueOf(str).length() + 20 + String.valueOf(str1).length())).append(str).append(" must have a method ").append(str1).toString());
      } catch (IllegalAccessException illegalAccessException) {
        str1 = clazz.getName();
        Log.e("TransitionLayout", (new StringBuilder(String.valueOf(str3).length() + 34 + String.valueOf(str1).length())).append(" Custom Attribute \"").append(str3).append("\" not found on ").append(str1).toString());
        illegalAccessException.printStackTrace();
      } catch (InvocationTargetException invocationTargetException) {
        str1 = clazz.getName();
        Log.e("TransitionLayout", (new StringBuilder(String.valueOf(str3).length() + 34 + String.valueOf(str1).length())).append(" Custom Attribute \"").append(str3).append("\" not found on ").append(str1).toString());
        invocationTargetException.printStackTrace();
      } 
    } 
  }
  
  public void applyCustom(View paramView) {
    Class<?> clazz = paramView.getClass();
    String str3 = this.mName;
    String str2 = str3;
    String str1 = str2;
    if (!this.mMethod) {
      str1 = String.valueOf(str2);
      if (str1.length() != 0) {
        str1 = "set".concat(str1);
      } else {
        str1 = new String("set");
      } 
    } 
    try {
      ColorDrawable colorDrawable;
      Method method;
      switch (this.mType) {
        default:
          return;
        case null:
          clazz.getMethod(str1, new Class[] { float.class }).invoke(paramView, new Object[] { Float.valueOf(this.mFloatValue) });
        case null:
          clazz.getMethod(str1, new Class[] { float.class }).invoke(paramView, new Object[] { Float.valueOf(this.mFloatValue) });
        case null:
          method = clazz.getMethod(str1, new Class[] { Drawable.class });
          colorDrawable = new ColorDrawable();
          this();
          colorDrawable.setColor(this.mColorValue);
          method.invoke(paramView, new Object[] { colorDrawable });
        case null:
          clazz.getMethod(str1, new Class[] { int.class }).invoke(paramView, new Object[] { Integer.valueOf(this.mColorValue) });
        case null:
          clazz.getMethod(str1, new Class[] { CharSequence.class }).invoke(paramView, new Object[] { this.mStringValue });
        case null:
          clazz.getMethod(str1, new Class[] { boolean.class }).invoke(paramView, new Object[] { Boolean.valueOf(this.mBooleanValue) });
        case null:
        case null:
          break;
      } 
      clazz.getMethod(str1, new Class[] { int.class }).invoke(paramView, new Object[] { Integer.valueOf(this.mIntegerValue) });
    } catch (NoSuchMethodException noSuchMethodException) {
      Log.e("TransitionLayout", noSuchMethodException.getMessage());
      String str = clazz.getName();
      Log.e("TransitionLayout", (new StringBuilder(String.valueOf(str3).length() + 34 + String.valueOf(str).length())).append(" Custom Attribute \"").append(str3).append("\" not found on ").append(str).toString());
      str = clazz.getName();
      Log.e("TransitionLayout", (new StringBuilder(String.valueOf(str).length() + 20 + String.valueOf(str1).length())).append(str).append(" must have a method ").append(str1).toString());
    } catch (IllegalAccessException illegalAccessException) {
      str1 = clazz.getName();
      Log.e("TransitionLayout", (new StringBuilder(String.valueOf(str3).length() + 34 + String.valueOf(str1).length())).append(" Custom Attribute \"").append(str3).append("\" not found on ").append(str1).toString());
      illegalAccessException.printStackTrace();
    } catch (InvocationTargetException invocationTargetException) {
      str1 = clazz.getName();
      Log.e("TransitionLayout", (new StringBuilder(String.valueOf(str3).length() + 34 + String.valueOf(str1).length())).append(" Custom Attribute \"").append(str3).append("\" not found on ").append(str1).toString());
      invocationTargetException.printStackTrace();
    } 
  }
  
  public boolean diff(ConstraintAttribute paramConstraintAttribute) {
    boolean bool6 = false;
    boolean bool2 = false;
    boolean bool4 = false;
    boolean bool3 = false;
    boolean bool1 = false;
    boolean bool5 = false;
    if (paramConstraintAttribute == null || this.mType != paramConstraintAttribute.mType)
      return false; 
    switch (this.mType) {
      default:
        return false;
      case null:
        bool1 = bool5;
        if (this.mFloatValue == paramConstraintAttribute.mFloatValue)
          bool1 = true; 
        return bool1;
      case null:
        bool1 = bool6;
        if (this.mFloatValue == paramConstraintAttribute.mFloatValue)
          bool1 = true; 
        return bool1;
      case null:
      case null:
        bool1 = bool2;
        if (this.mColorValue == paramConstraintAttribute.mColorValue)
          bool1 = true; 
        return bool1;
      case null:
        bool1 = bool4;
        if (this.mIntegerValue == paramConstraintAttribute.mIntegerValue)
          bool1 = true; 
        return bool1;
      case null:
        bool1 = bool3;
        if (this.mBooleanValue == paramConstraintAttribute.mBooleanValue)
          bool1 = true; 
        return bool1;
      case null:
      case null:
        break;
    } 
    if (this.mIntegerValue == paramConstraintAttribute.mIntegerValue)
      bool1 = true; 
    return bool1;
  }
  
  public AttributeType getType() {
    return this.mType;
  }
  
  public float getValueToInterpolate() {
    float f;
    switch (this.mType) {
      default:
        return Float.NaN;
      case null:
        return this.mFloatValue;
      case null:
        return this.mFloatValue;
      case null:
        return this.mIntegerValue;
      case null:
      case null:
        throw new RuntimeException("Color does not have a single color to interpolate");
      case null:
        throw new RuntimeException("Cannot interpolate String");
      case null:
        break;
    } 
    if (this.mBooleanValue) {
      f = 1.0F;
    } else {
      f = 0.0F;
    } 
    return f;
  }
  
  public void getValuesToInterpolate(float[] paramArrayOffloat) {
    float f1;
    float f2;
    float f3;
    int i;
    switch (this.mType) {
      default:
        return;
      case null:
        paramArrayOffloat[0] = this.mFloatValue;
      case null:
        paramArrayOffloat[0] = this.mFloatValue;
      case null:
        paramArrayOffloat[0] = this.mIntegerValue;
      case null:
      case null:
        i = this.mColorValue;
        f2 = (float)Math.pow(((i >> 16 & 0xFF) / 255.0F), 2.2D);
        f1 = (float)Math.pow(((i >> 8 & 0xFF) / 255.0F), 2.2D);
        f3 = (float)Math.pow(((i & 0xFF) / 255.0F), 2.2D);
        paramArrayOffloat[0] = f2;
        paramArrayOffloat[1] = f1;
        paramArrayOffloat[2] = f3;
        paramArrayOffloat[3] = (i >> 24 & 0xFF) / 255.0F;
      case null:
        throw new RuntimeException("Color does not have a single color to interpolate");
      case null:
        break;
    } 
    if (this.mBooleanValue) {
      f1 = 1.0F;
    } else {
      f1 = 0.0F;
    } 
    paramArrayOffloat[0] = f1;
  }
  
  public boolean isContinuous() {
    switch (this.mType) {
      default:
        return true;
      case null:
      case null:
      case null:
        break;
    } 
    return false;
  }
  
  public int numberOfInterpolatedValues() {
    switch (this.mType) {
      default:
        return 1;
      case null:
      case null:
        break;
    } 
    return 4;
  }
  
  public void setColorValue(int paramInt) {
    this.mColorValue = paramInt;
  }
  
  public void setFloatValue(float paramFloat) {
    this.mFloatValue = paramFloat;
  }
  
  public void setIntValue(int paramInt) {
    this.mIntegerValue = paramInt;
  }
  
  public void setInterpolatedValue(View paramView, float[] paramArrayOffloat) {
    String str1;
    Class<?> clazz = paramView.getClass();
    String str2 = String.valueOf(this.mName);
    if (str2.length() != 0) {
      str2 = "set".concat(str2);
    } else {
      str2 = new String("set");
    } 
    try {
      ColorDrawable colorDrawable;
      String str;
      int i;
      int j;
      int k;
      int m;
      boolean bool;
      Method method2;
      RuntimeException runtimeException;
      switch (this.mType) {
        default:
          return;
        case null:
          clazz.getMethod(str2, new Class[] { float.class }).invoke(paramView, new Object[] { Float.valueOf(paramArrayOffloat[0]) });
        case null:
          clazz.getMethod(str2, new Class[] { float.class }).invoke(paramView, new Object[] { Float.valueOf(paramArrayOffloat[0]) });
        case null:
          clazz.getMethod(str2, new Class[] { int.class }).invoke(paramView, new Object[] { Integer.valueOf((int)paramArrayOffloat[0]) });
        case null:
          method2 = clazz.getMethod(str2, new Class[] { Drawable.class });
          k = clamp((int)((float)Math.pow(paramArrayOffloat[0], 0.45454545454545453D) * 255.0F));
          j = clamp((int)((float)Math.pow(paramArrayOffloat[1], 0.45454545454545453D) * 255.0F));
          m = clamp((int)((float)Math.pow(paramArrayOffloat[2], 0.45454545454545453D) * 255.0F));
          i = clamp((int)(paramArrayOffloat[3] * 255.0F));
          colorDrawable = new ColorDrawable();
          this();
          colorDrawable.setColor(i << 24 | k << 16 | j << 8 | m);
          method2.invoke(paramView, new Object[] { colorDrawable });
        case null:
          method2 = method2.getMethod(str2, new Class[] { int.class });
          j = clamp((int)((float)Math.pow(colorDrawable[0], 0.45454545454545453D) * 255.0F));
          k = clamp((int)((float)Math.pow(colorDrawable[1], 0.45454545454545453D) * 255.0F));
          i = clamp((int)((float)Math.pow(colorDrawable[2], 0.45454545454545453D) * 255.0F));
          method2.invoke(paramView, new Object[] { Integer.valueOf(clamp((int)(colorDrawable[3] * 255.0F)) << 24 | j << 16 | k << 8 | i) });
        case null:
          runtimeException = new RuntimeException();
          str = String.valueOf(this.mName);
          if (str.length() != 0) {
            str = "unable to interpolate strings ".concat(str);
          } else {
            str = new String("unable to interpolate strings ");
          } 
          this(str);
          throw runtimeException;
        case null:
          break;
      } 
      Method method1 = runtimeException.getMethod(str2, new Class[] { boolean.class });
      if (str[0] > 0.5F) {
        bool = true;
      } else {
        bool = false;
      } 
      method1.invoke(paramView, new Object[] { Boolean.valueOf(bool) });
    } catch (NoSuchMethodException noSuchMethodException) {
      str1 = Debug.getName(paramView);
      Log.e("TransitionLayout", (new StringBuilder(String.valueOf(str2).length() + 21 + String.valueOf(str1).length())).append("no method ").append(str2).append(" on View \"").append(str1).append("\"").toString());
      noSuchMethodException.printStackTrace();
    } catch (IllegalAccessException illegalAccessException) {
      str1 = Debug.getName((View)str1);
      Log.e("TransitionLayout", (new StringBuilder(String.valueOf(str2).length() + 32 + String.valueOf(str1).length())).append("cannot access method ").append(str2).append(" on View \"").append(str1).append("\"").toString());
      illegalAccessException.printStackTrace();
    } catch (InvocationTargetException invocationTargetException) {
      invocationTargetException.printStackTrace();
    } 
  }
  
  public void setStringValue(String paramString) {
    this.mStringValue = paramString;
  }
  
  public void setValue(Object paramObject) {
    switch (this.mType) {
      default:
        return;
      case null:
        this.mFloatValue = ((Float)paramObject).floatValue();
      case null:
        this.mFloatValue = ((Float)paramObject).floatValue();
      case null:
      case null:
        this.mColorValue = ((Integer)paramObject).intValue();
      case null:
        this.mStringValue = (String)paramObject;
      case null:
        this.mBooleanValue = ((Boolean)paramObject).booleanValue();
      case null:
      case null:
        break;
    } 
    this.mIntegerValue = ((Integer)paramObject).intValue();
  }
  
  public void setValue(float[] paramArrayOffloat) {
    int i = null.$SwitchMap$androidx$constraintlayout$widget$ConstraintAttribute$AttributeType[this.mType.ordinal()];
    boolean bool = false;
    switch (i) {
      default:
        return;
      case 8:
        this.mFloatValue = paramArrayOffloat[0];
      case 7:
        this.mFloatValue = paramArrayOffloat[0];
      case 4:
      case 5:
        i = Color.HSVToColor(paramArrayOffloat);
        this.mColorValue = i;
        this.mColorValue = i & 0xFFFFFF | clamp((int)(paramArrayOffloat[3] * 255.0F)) << 24;
      case 3:
        throw new RuntimeException("Color does not have a single color to interpolate");
      case 2:
        if (paramArrayOffloat[0] > 0.5D)
          bool = true; 
        this.mBooleanValue = bool;
      case 1:
      case 6:
        break;
    } 
    this.mIntegerValue = (int)paramArrayOffloat[0];
  }
  
  public enum AttributeType {
    BOOLEAN_TYPE, COLOR_DRAWABLE_TYPE, COLOR_TYPE, DIMENSION_TYPE, FLOAT_TYPE, INT_TYPE, REFERENCE_TYPE, STRING_TYPE;
    
    private static final AttributeType[] $VALUES;
    
    static {
      COLOR_TYPE = new AttributeType("COLOR_TYPE", 2);
      COLOR_DRAWABLE_TYPE = new AttributeType("COLOR_DRAWABLE_TYPE", 3);
      STRING_TYPE = new AttributeType("STRING_TYPE", 4);
      BOOLEAN_TYPE = new AttributeType("BOOLEAN_TYPE", 5);
      DIMENSION_TYPE = new AttributeType("DIMENSION_TYPE", 6);
      REFERENCE_TYPE = new AttributeType("REFERENCE_TYPE", 7);
      $VALUES = $values();
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\widget\ConstraintAttribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */