package androidx.constraintlayout.core.motion;

public class CustomVariable {
  private static final String TAG = "TransitionLayout";
  
  boolean mBooleanValue;
  
  private float mFloatValue = Float.NaN;
  
  private int mIntegerValue = Integer.MIN_VALUE;
  
  String mName;
  
  private String mStringValue = null;
  
  private int mType;
  
  public CustomVariable(CustomVariable paramCustomVariable) {
    this.mName = paramCustomVariable.mName;
    this.mType = paramCustomVariable.mType;
    this.mIntegerValue = paramCustomVariable.mIntegerValue;
    this.mFloatValue = paramCustomVariable.mFloatValue;
    this.mStringValue = paramCustomVariable.mStringValue;
    this.mBooleanValue = paramCustomVariable.mBooleanValue;
  }
  
  public CustomVariable(CustomVariable paramCustomVariable, Object paramObject) {
    this.mName = paramCustomVariable.mName;
    this.mType = paramCustomVariable.mType;
    setValue(paramObject);
  }
  
  public CustomVariable(String paramString, int paramInt) {
    this.mName = paramString;
    this.mType = paramInt;
  }
  
  public CustomVariable(String paramString, int paramInt, float paramFloat) {
    this.mName = paramString;
    this.mType = paramInt;
    this.mFloatValue = paramFloat;
  }
  
  public CustomVariable(String paramString, int paramInt1, int paramInt2) {
    this.mName = paramString;
    this.mType = paramInt1;
    if (paramInt1 == 901) {
      this.mFloatValue = paramInt2;
    } else {
      this.mIntegerValue = paramInt2;
    } 
  }
  
  public CustomVariable(String paramString, int paramInt, Object paramObject) {
    this.mName = paramString;
    this.mType = paramInt;
    setValue(paramObject);
  }
  
  public CustomVariable(String paramString1, int paramInt, String paramString2) {
    this.mName = paramString1;
    this.mType = paramInt;
    this.mStringValue = paramString2;
  }
  
  public CustomVariable(String paramString, int paramInt, boolean paramBoolean) {
    this.mName = paramString;
    this.mType = paramInt;
    this.mBooleanValue = paramBoolean;
  }
  
  private static int clamp(int paramInt) {
    paramInt = (paramInt & (paramInt >> 31 ^ 0xFFFFFFFF)) - 255;
    return (paramInt & paramInt >> 31) + 255;
  }
  
  public static String colorString(int paramInt) {
    String str = "00000000" + Integer.toHexString(paramInt);
    return "#" + str.substring(str.length() - 8);
  }
  
  public static int hsvToRgb(float paramFloat1, float paramFloat2, float paramFloat3) {
    int k = (int)(paramFloat1 * 6.0F);
    paramFloat1 = 6.0F * paramFloat1 - k;
    int m = (int)(paramFloat3 * 255.0F * (1.0F - paramFloat2) + 0.5F);
    int n = (int)(paramFloat3 * 255.0F * (1.0F - paramFloat1 * paramFloat2) + 0.5F);
    int i = (int)(paramFloat3 * 255.0F * (1.0F - (1.0F - paramFloat1) * paramFloat2) + 0.5F);
    int j = (int)(255.0F * paramFloat3 + 0.5F);
    switch (k) {
      default:
        return 0;
      case 5:
        return 0xFF000000 | (j << 16) + (m << 8) + n;
      case 4:
        return 0xFF000000 | (i << 16) + (m << 8) + j;
      case 3:
        return 0xFF000000 | (m << 16) + (n << 8) + j;
      case 2:
        return 0xFF000000 | (m << 16) + (j << 8) + i;
      case 1:
        return 0xFF000000 | (n << 16) + (j << 8) + m;
      case 0:
        break;
    } 
    return 0xFF000000 | (j << 16) + (i << 8) + m;
  }
  
  public static int rgbaTocColor(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
    int j = clamp((int)(paramFloat1 * 255.0F));
    int i = clamp((int)(paramFloat2 * 255.0F));
    int k = clamp((int)(paramFloat3 * 255.0F));
    return clamp((int)(255.0F * paramFloat4)) << 24 | j << 16 | i << 8 | k;
  }
  
  public void applyToWidget(MotionWidget paramMotionWidget) {
    int i = this.mType;
    switch (i) {
      default:
        return;
      case 904:
        paramMotionWidget.setCustomAttribute(this.mName, i, this.mBooleanValue);
      case 903:
        paramMotionWidget.setCustomAttribute(this.mName, i, this.mStringValue);
      case 901:
      case 905:
        paramMotionWidget.setCustomAttribute(this.mName, i, this.mFloatValue);
      case 900:
      case 902:
      case 906:
        break;
    } 
    paramMotionWidget.setCustomAttribute(this.mName, i, this.mIntegerValue);
  }
  
  public CustomVariable copy() {
    return new CustomVariable(this);
  }
  
  public boolean diff(CustomVariable paramCustomVariable) {
    boolean bool3 = false;
    boolean bool6 = false;
    boolean bool4 = false;
    boolean bool1 = false;
    boolean bool5 = false;
    boolean bool2 = false;
    if (paramCustomVariable != null) {
      int i = this.mType;
      if (i == paramCustomVariable.mType) {
        switch (i) {
          default:
            return false;
          case 905:
            bool1 = bool2;
            if (this.mFloatValue == paramCustomVariable.mFloatValue)
              bool1 = true; 
            return bool1;
          case 904:
            bool1 = bool3;
            if (this.mBooleanValue == paramCustomVariable.mBooleanValue)
              bool1 = true; 
            return bool1;
          case 903:
            bool1 = bool6;
            if (this.mIntegerValue == paramCustomVariable.mIntegerValue)
              bool1 = true; 
            return bool1;
          case 902:
            bool1 = bool4;
            if (this.mIntegerValue == paramCustomVariable.mIntegerValue)
              bool1 = true; 
            return bool1;
          case 901:
            if (this.mFloatValue == paramCustomVariable.mFloatValue)
              bool1 = true; 
            return bool1;
          case 900:
          case 906:
            break;
        } 
        bool1 = bool5;
        if (this.mIntegerValue == paramCustomVariable.mIntegerValue)
          bool1 = true; 
        return bool1;
      } 
    } 
    return false;
  }
  
  public boolean getBooleanValue() {
    return this.mBooleanValue;
  }
  
  public int getColorValue() {
    return this.mIntegerValue;
  }
  
  public float getFloatValue() {
    return this.mFloatValue;
  }
  
  public int getIntegerValue() {
    return this.mIntegerValue;
  }
  
  public int getInterpolatedColor(float[] paramArrayOffloat) {
    int j = clamp((int)((float)Math.pow(paramArrayOffloat[0], 0.45454545454545453D) * 255.0F));
    int i = clamp((int)((float)Math.pow(paramArrayOffloat[1], 0.45454545454545453D) * 255.0F));
    int k = clamp((int)((float)Math.pow(paramArrayOffloat[2], 0.45454545454545453D) * 255.0F));
    return clamp((int)(paramArrayOffloat[3] * 255.0F)) << 24 | j << 16 | i << 8 | k;
  }
  
  public String getName() {
    return this.mName;
  }
  
  public String getStringValue() {
    return this.mStringValue;
  }
  
  public int getType() {
    return this.mType;
  }
  
  public float getValueToInterpolate() {
    float f;
    switch (this.mType) {
      default:
        return Float.NaN;
      case 905:
        return this.mFloatValue;
      case 904:
        if (this.mBooleanValue) {
          f = 1.0F;
        } else {
          f = 0.0F;
        } 
        return f;
      case 903:
        throw new RuntimeException("Cannot interpolate String");
      case 902:
        throw new RuntimeException("Color does not have a single color to interpolate");
      case 901:
        return this.mFloatValue;
      case 900:
        break;
    } 
    return this.mIntegerValue;
  }
  
  public void getValuesToInterpolate(float[] paramArrayOffloat) {
    float f1;
    float f2;
    float f3;
    int i;
    switch (this.mType) {
      default:
        return;
      case 905:
        paramArrayOffloat[0] = this.mFloatValue;
      case 904:
        if (this.mBooleanValue) {
          f1 = 1.0F;
        } else {
          f1 = 0.0F;
        } 
        paramArrayOffloat[0] = f1;
      case 903:
        throw new RuntimeException("Cannot interpolate String");
      case 902:
        i = this.mIntegerValue;
        f2 = (float)Math.pow(((i >> 16 & 0xFF) / 255.0F), 2.2D);
        f3 = (float)Math.pow(((i >> 8 & 0xFF) / 255.0F), 2.2D);
        f1 = (float)Math.pow(((i & 0xFF) / 255.0F), 2.2D);
        paramArrayOffloat[0] = f2;
        paramArrayOffloat[1] = f3;
        paramArrayOffloat[2] = f1;
        paramArrayOffloat[3] = (i >> 24 & 0xFF) / 255.0F;
      case 901:
        paramArrayOffloat[0] = this.mFloatValue;
      case 900:
        break;
    } 
    paramArrayOffloat[0] = this.mIntegerValue;
  }
  
  public boolean isContinuous() {
    switch (this.mType) {
      default:
        return true;
      case 903:
      case 904:
      case 906:
        break;
    } 
    return false;
  }
  
  public int numberOfInterpolatedValues() {
    switch (this.mType) {
      default:
        return 1;
      case 902:
        break;
    } 
    return 4;
  }
  
  public void setBooleanValue(boolean paramBoolean) {
    this.mBooleanValue = paramBoolean;
  }
  
  public void setFloatValue(float paramFloat) {
    this.mFloatValue = paramFloat;
  }
  
  public void setIntValue(int paramInt) {
    this.mIntegerValue = paramInt;
  }
  
  public void setInterpolatedValue(MotionWidget paramMotionWidget, float[] paramArrayOffloat) {
    int j;
    int k;
    int m;
    String str;
    int i = this.mType;
    boolean bool = true;
    switch (i) {
      default:
        return;
      case 904:
        str = this.mName;
        if (paramArrayOffloat[0] <= 0.5F)
          bool = false; 
        paramMotionWidget.setCustomAttribute(str, i, bool);
      case 903:
      case 906:
        throw new RuntimeException("unable to interpolate " + this.mName);
      case 902:
        m = clamp((int)((float)Math.pow(paramArrayOffloat[0], 0.45454545454545453D) * 255.0F));
        k = clamp((int)((float)Math.pow(paramArrayOffloat[1], 0.45454545454545453D) * 255.0F));
        j = clamp((int)((float)Math.pow(paramArrayOffloat[2], 0.45454545454545453D) * 255.0F));
        i = clamp((int)(paramArrayOffloat[3] * 255.0F));
        paramMotionWidget.setCustomAttribute(this.mName, this.mType, i << 24 | m << 16 | k << 8 | j);
      case 901:
      case 905:
        paramMotionWidget.setCustomAttribute(this.mName, i, paramArrayOffloat[0]);
      case 900:
        break;
    } 
    paramMotionWidget.setCustomAttribute(this.mName, i, (int)paramArrayOffloat[0]);
  }
  
  public void setStringValue(String paramString) {
    this.mStringValue = paramString;
  }
  
  public void setValue(Object paramObject) {
    switch (this.mType) {
      default:
        return;
      case 905:
        this.mFloatValue = ((Float)paramObject).floatValue();
      case 904:
        this.mBooleanValue = ((Boolean)paramObject).booleanValue();
      case 903:
        this.mStringValue = (String)paramObject;
      case 902:
        this.mIntegerValue = ((Integer)paramObject).intValue();
      case 901:
        this.mFloatValue = ((Float)paramObject).floatValue();
      case 900:
      case 906:
        break;
    } 
    this.mIntegerValue = ((Integer)paramObject).intValue();
  }
  
  public void setValue(float[] paramArrayOffloat) {
    float f1;
    float f2;
    float f3;
    int j;
    int k;
    int i = this.mType;
    boolean bool = true;
    switch (i) {
      default:
        return;
      case 904:
        if (paramArrayOffloat[0] <= 0.5D)
          bool = false; 
        this.mBooleanValue = bool;
      case 903:
        throw new RuntimeException("Cannot interpolate String");
      case 902:
        f2 = paramArrayOffloat[0];
        f3 = paramArrayOffloat[1];
        f1 = paramArrayOffloat[2];
        j = Math.round((float)Math.pow(f2, 0.5D) * 255.0F);
        i = Math.round((float)Math.pow(f3, 0.5D) * 255.0F);
        k = Math.round((float)Math.pow(f1, 0.5D) * 255.0F);
        this.mIntegerValue = (Math.round(paramArrayOffloat[3] * 255.0F) & 0xFF) << 24 | (j & 0xFF) << 16 | (i & 0xFF) << 8 | k & 0xFF;
      case 901:
      case 905:
        this.mFloatValue = paramArrayOffloat[0];
      case 900:
      case 906:
        break;
    } 
    this.mIntegerValue = (int)paramArrayOffloat[0];
  }
  
  public String toString() {
    String str = this.mName + ':';
    switch (this.mType) {
      default:
        return str + "????";
      case 905:
        return str + this.mFloatValue;
      case 904:
        return str + Boolean.valueOf(this.mBooleanValue);
      case 903:
        return str + this.mStringValue;
      case 902:
        return str + colorString(this.mIntegerValue);
      case 901:
        return str + this.mFloatValue;
      case 900:
        break;
    } 
    return str + this.mIntegerValue;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\motion\CustomVariable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */