package androidx.constraintlayout.core.motion;

import androidx.constraintlayout.core.motion.utils.Utils;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

public class CustomAttribute {
  private static final String TAG = "TransitionLayout";
  
  boolean mBooleanValue;
  
  private int mColorValue;
  
  private float mFloatValue;
  
  private int mIntegerValue;
  
  private boolean mMethod = false;
  
  String mName;
  
  private String mStringValue;
  
  private AttributeType mType;
  
  public CustomAttribute(CustomAttribute paramCustomAttribute, Object paramObject) {
    this.mName = paramCustomAttribute.mName;
    this.mType = paramCustomAttribute.mType;
    setValue(paramObject);
  }
  
  public CustomAttribute(String paramString, AttributeType paramAttributeType) {
    this.mName = paramString;
    this.mType = paramAttributeType;
  }
  
  public CustomAttribute(String paramString, AttributeType paramAttributeType, Object paramObject, boolean paramBoolean) {
    this.mName = paramString;
    this.mType = paramAttributeType;
    this.mMethod = paramBoolean;
    setValue(paramObject);
  }
  
  private static int clamp(int paramInt) {
    paramInt = (paramInt & (paramInt >> 31 ^ 0xFFFFFFFF)) - 255;
    return (paramInt & paramInt >> 31) + 255;
  }
  
  public static HashMap<String, CustomAttribute> extractAttributes(HashMap<String, CustomAttribute> paramHashMap, Object paramObject) {
    HashMap<Object, Object> hashMap = new HashMap<>();
    Class<?> clazz = paramObject.getClass();
    for (String str : paramHashMap.keySet()) {
      CustomAttribute customAttribute = paramHashMap.get(str);
      try {
        StringBuilder stringBuilder = new StringBuilder();
        this();
        Object object = clazz.getMethod(stringBuilder.append("getMap").append(str).toString(), new Class[0]).invoke(paramObject, new Object[0]);
        CustomAttribute customAttribute1 = new CustomAttribute();
        this(customAttribute, object);
        hashMap.put(str, customAttribute1);
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
  
  public static int hsvToRgb(float paramFloat1, float paramFloat2, float paramFloat3) {
    int i = (int)(paramFloat1 * 6.0F);
    paramFloat1 = 6.0F * paramFloat1 - i;
    int j = (int)(paramFloat3 * 255.0F * (1.0F - paramFloat2) + 0.5F);
    int m = (int)(paramFloat3 * 255.0F * (1.0F - paramFloat1 * paramFloat2) + 0.5F);
    int k = (int)(paramFloat3 * 255.0F * (1.0F - (1.0F - paramFloat1) * paramFloat2) + 0.5F);
    int n = (int)(255.0F * paramFloat3 + 0.5F);
    switch (i) {
      default:
        return 0;
      case 5:
        return 0xFF000000 | (n << 16) + (j << 8) + m;
      case 4:
        return 0xFF000000 | (k << 16) + (j << 8) + n;
      case 3:
        return 0xFF000000 | (j << 16) + (m << 8) + n;
      case 2:
        return 0xFF000000 | (j << 16) + (n << 8) + k;
      case 1:
        return 0xFF000000 | (m << 16) + (n << 8) + j;
      case 0:
        break;
    } 
    return 0xFF000000 | (n << 16) + (k << 8) + j;
  }
  
  public static void setAttributes(Object paramObject, HashMap<String, CustomAttribute> paramHashMap) {
    Class<?> clazz = paramObject.getClass();
    for (String str3 : paramHashMap.keySet()) {
      CustomAttribute customAttribute = paramHashMap.get(str3);
      String str2 = str3;
      String str1 = str2;
      if (!customAttribute.mMethod)
        str1 = "set" + str2; 
      try {
        switch (customAttribute.mType) {
          default:
            continue;
          case null:
            clazz.getMethod(str1, new Class[] { float.class }).invoke(paramObject, new Object[] { Float.valueOf(customAttribute.mFloatValue) });
            continue;
          case null:
            clazz.getMethod(str1, new Class[] { float.class }).invoke(paramObject, new Object[] { Float.valueOf(customAttribute.mFloatValue) });
            continue;
          case null:
            clazz.getMethod(str1, new Class[] { int.class }).invoke(paramObject, new Object[] { Integer.valueOf(customAttribute.mIntegerValue) });
            continue;
          case null:
            clazz.getMethod(str1, new Class[] { int.class }).invoke(paramObject, new Object[] { Integer.valueOf(customAttribute.mColorValue) });
            continue;
          case null:
            clazz.getMethod(str1, new Class[] { CharSequence.class }).invoke(paramObject, new Object[] { customAttribute.mStringValue });
            continue;
          case null:
            clazz.getMethod(str1, new Class[] { boolean.class }).invoke(paramObject, new Object[] { Boolean.valueOf(customAttribute.mBooleanValue) });
            continue;
          case null:
            break;
        } 
        clazz.getMethod(str1, new Class[] { int.class }).invoke(paramObject, new Object[] { Integer.valueOf(customAttribute.mIntegerValue) });
      } catch (NoSuchMethodException noSuchMethodException) {
        Utils.loge("TransitionLayout", noSuchMethodException.getMessage());
        Utils.loge("TransitionLayout", " Custom Attribute \"" + str3 + "\" not found on " + clazz.getName());
        Utils.loge("TransitionLayout", clazz.getName() + " must have a method " + str1);
      } catch (IllegalAccessException illegalAccessException) {
        Utils.loge("TransitionLayout", " Custom Attribute \"" + str3 + "\" not found on " + clazz.getName());
        illegalAccessException.printStackTrace();
      } catch (InvocationTargetException invocationTargetException) {
        Utils.loge("TransitionLayout", " Custom Attribute \"" + str3 + "\" not found on " + clazz.getName());
        invocationTargetException.printStackTrace();
      } 
    } 
  }
  
  public void applyCustom(Object paramObject) {
    Class<?> clazz = paramObject.getClass();
    String str3 = this.mName;
    String str2 = str3;
    String str1 = str2;
    if (!this.mMethod)
      str1 = "set" + str2; 
    try {
      switch (this.mType) {
        default:
          return;
        case null:
          clazz.getMethod(str1, new Class[] { float.class }).invoke(paramObject, new Object[] { Float.valueOf(this.mFloatValue) });
        case null:
          clazz.getMethod(str1, new Class[] { float.class }).invoke(paramObject, new Object[] { Float.valueOf(this.mFloatValue) });
        case null:
          clazz.getMethod(str1, new Class[] { int.class }).invoke(paramObject, new Object[] { Integer.valueOf(this.mColorValue) });
        case null:
          clazz.getMethod(str1, new Class[] { CharSequence.class }).invoke(paramObject, new Object[] { this.mStringValue });
        case null:
          clazz.getMethod(str1, new Class[] { boolean.class }).invoke(paramObject, new Object[] { Boolean.valueOf(this.mBooleanValue) });
        case null:
        case null:
          break;
      } 
      clazz.getMethod(str1, new Class[] { int.class }).invoke(paramObject, new Object[] { Integer.valueOf(this.mIntegerValue) });
    } catch (NoSuchMethodException noSuchMethodException) {
      Utils.loge("TransitionLayout", noSuchMethodException.getMessage());
      Utils.loge("TransitionLayout", " Custom Attribute \"" + str3 + "\" not found on " + clazz.getName());
      Utils.loge("TransitionLayout", clazz.getName() + " must have a method " + str1);
    } catch (IllegalAccessException illegalAccessException) {
      Utils.loge("TransitionLayout", " Custom Attribute \"" + str3 + "\" not found on " + clazz.getName());
      illegalAccessException.printStackTrace();
    } catch (InvocationTargetException invocationTargetException) {
      Utils.loge("TransitionLayout", " Custom Attribute \"" + str3 + "\" not found on " + clazz.getName());
      invocationTargetException.printStackTrace();
    } 
  }
  
  public boolean diff(CustomAttribute paramCustomAttribute) {
    boolean bool3 = false;
    boolean bool2 = false;
    boolean bool1 = false;
    boolean bool4 = false;
    boolean bool5 = false;
    boolean bool6 = false;
    if (paramCustomAttribute == null || this.mType != paramCustomAttribute.mType)
      return false; 
    switch (this.mType) {
      default:
        return false;
      case null:
        bool1 = bool6;
        if (this.mFloatValue == paramCustomAttribute.mFloatValue)
          bool1 = true; 
        return bool1;
      case null:
        bool1 = bool3;
        if (this.mFloatValue == paramCustomAttribute.mFloatValue)
          bool1 = true; 
        return bool1;
      case null:
      case null:
        bool1 = bool2;
        if (this.mColorValue == paramCustomAttribute.mColorValue)
          bool1 = true; 
        return bool1;
      case null:
        if (this.mIntegerValue == paramCustomAttribute.mIntegerValue)
          bool1 = true; 
        return bool1;
      case null:
        bool1 = bool4;
        if (this.mBooleanValue == paramCustomAttribute.mBooleanValue)
          bool1 = true; 
        return bool1;
      case null:
      case null:
        break;
    } 
    bool1 = bool5;
    if (this.mIntegerValue == paramCustomAttribute.mIntegerValue)
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
        f3 = (float)Math.pow(((i >> 16 & 0xFF) / 255.0F), 2.2D);
        f1 = (float)Math.pow(((i >> 8 & 0xFF) / 255.0F), 2.2D);
        f2 = (float)Math.pow(((i & 0xFF) / 255.0F), 2.2D);
        paramArrayOffloat[0] = f3;
        paramArrayOffloat[1] = f1;
        paramArrayOffloat[2] = f2;
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
  
  public void setInterpolatedValue(Object paramObject, float[] paramArrayOffloat) {
    Class<?> clazz = paramObject.getClass();
    String str = "set" + this.mName;
    try {
      StringBuilder stringBuilder;
      int i;
      int j;
      int k;
      Method method2;
      RuntimeException runtimeException;
      switch (this.mType) {
        default:
          return;
        case null:
          clazz.getMethod(str, new Class[] { float.class }).invoke(paramObject, new Object[] { Float.valueOf(paramArrayOffloat[0]) });
        case null:
          clazz.getMethod(str, new Class[] { float.class }).invoke(paramObject, new Object[] { Float.valueOf(paramArrayOffloat[0]) });
        case null:
          clazz.getMethod(str, new Class[] { int.class }).invoke(paramObject, new Object[] { Integer.valueOf((int)paramArrayOffloat[0]) });
        case null:
          method2 = clazz.getMethod(str, new Class[] { int.class });
          i = clamp((int)((float)Math.pow(paramArrayOffloat[0], 0.45454545454545453D) * 255.0F));
          k = clamp((int)((float)Math.pow(paramArrayOffloat[1], 0.45454545454545453D) * 255.0F));
          j = clamp((int)((float)Math.pow(paramArrayOffloat[2], 0.45454545454545453D) * 255.0F));
          method2.invoke(paramObject, new Object[] { Integer.valueOf(clamp((int)(paramArrayOffloat[3] * 255.0F)) << 24 | i << 16 | k << 8 | j) });
        case null:
          runtimeException = new RuntimeException();
          stringBuilder = new StringBuilder();
          this();
          this(stringBuilder.append("unable to interpolate strings ").append(this.mName).toString());
          throw runtimeException;
        case null:
          break;
      } 
      Method method1 = runtimeException.getMethod(str, new Class[] { boolean.class });
      boolean bool = true;
      if (stringBuilder[0] <= 0.5F)
        bool = false; 
      method1.invoke(paramObject, new Object[] { Boolean.valueOf(bool) });
    } catch (NoSuchMethodException noSuchMethodException) {
      Utils.loge("TransitionLayout", "no method " + str + " on View \"" + paramObject.getClass().getName() + "\"");
      noSuchMethodException.printStackTrace();
    } catch (IllegalAccessException illegalAccessException) {
      Utils.loge("TransitionLayout", "cannot access method " + str + " on View \"" + paramObject.getClass().getName() + "\"");
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
    int i = null.$SwitchMap$androidx$constraintlayout$core$motion$CustomAttribute$AttributeType[this.mType.ordinal()];
    boolean bool = true;
    switch (i) {
      default:
        return;
      case 8:
        this.mFloatValue = paramArrayOffloat[0];
      case 7:
        this.mFloatValue = paramArrayOffloat[0];
      case 4:
      case 5:
        i = hsvToRgb(paramArrayOffloat[0], paramArrayOffloat[1], paramArrayOffloat[2]);
        this.mColorValue = i;
        this.mColorValue = i & 0xFFFFFF | clamp((int)(paramArrayOffloat[3] * 255.0F)) << 24;
      case 3:
        throw new RuntimeException("Color does not have a single color to interpolate");
      case 2:
        if (paramArrayOffloat[0] <= 0.5D)
          bool = false; 
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
      AttributeType attributeType1 = new AttributeType("INT_TYPE", 0);
      INT_TYPE = attributeType1;
      AttributeType attributeType2 = new AttributeType("FLOAT_TYPE", 1);
      FLOAT_TYPE = attributeType2;
      AttributeType attributeType8 = new AttributeType("COLOR_TYPE", 2);
      COLOR_TYPE = attributeType8;
      AttributeType attributeType3 = new AttributeType("COLOR_DRAWABLE_TYPE", 3);
      COLOR_DRAWABLE_TYPE = attributeType3;
      AttributeType attributeType7 = new AttributeType("STRING_TYPE", 4);
      STRING_TYPE = attributeType7;
      AttributeType attributeType4 = new AttributeType("BOOLEAN_TYPE", 5);
      BOOLEAN_TYPE = attributeType4;
      AttributeType attributeType6 = new AttributeType("DIMENSION_TYPE", 6);
      DIMENSION_TYPE = attributeType6;
      AttributeType attributeType5 = new AttributeType("REFERENCE_TYPE", 7);
      REFERENCE_TYPE = attributeType5;
      $VALUES = new AttributeType[] { attributeType1, attributeType2, attributeType8, attributeType3, attributeType7, attributeType4, attributeType6, attributeType5 };
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\motion\CustomAttribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */