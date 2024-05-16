package androidx.constraintlayout.core.motion.key;

import androidx.constraintlayout.core.motion.CustomVariable;
import androidx.constraintlayout.core.motion.utils.SplineSet;
import androidx.constraintlayout.core.motion.utils.TypedValues;
import java.util.HashMap;
import java.util.HashSet;

public abstract class MotionKey implements TypedValues {
  public static final String ALPHA = "alpha";
  
  public static final String CUSTOM = "CUSTOM";
  
  public static final String ELEVATION = "elevation";
  
  public static final String ROTATION = "rotationZ";
  
  public static final String ROTATION_X = "rotationX";
  
  public static final String SCALE_X = "scaleX";
  
  public static final String SCALE_Y = "scaleY";
  
  public static final String TRANSITION_PATH_ROTATE = "transitionPathRotate";
  
  public static final String TRANSLATION_X = "translationX";
  
  public static final String TRANSLATION_Y = "translationY";
  
  public static int UNSET = -1;
  
  public static final String VISIBILITY = "visibility";
  
  public HashMap<String, CustomVariable> mCustom;
  
  public int mFramePosition;
  
  int mTargetId;
  
  String mTargetString;
  
  public int mType;
  
  public MotionKey() {
    int i = UNSET;
    this.mFramePosition = i;
    this.mTargetId = i;
    this.mTargetString = null;
  }
  
  public abstract void addValues(HashMap<String, SplineSet> paramHashMap);
  
  public abstract MotionKey clone();
  
  public MotionKey copy(MotionKey paramMotionKey) {
    this.mFramePosition = paramMotionKey.mFramePosition;
    this.mTargetId = paramMotionKey.mTargetId;
    this.mTargetString = paramMotionKey.mTargetString;
    this.mType = paramMotionKey.mType;
    return this;
  }
  
  public abstract void getAttributeNames(HashSet<String> paramHashSet);
  
  public int getFramePosition() {
    return this.mFramePosition;
  }
  
  boolean matches(String paramString) {
    String str = this.mTargetString;
    return (str == null || paramString == null) ? false : paramString.matches(str);
  }
  
  public void setCustomAttribute(String paramString, int paramInt, float paramFloat) {
    this.mCustom.put(paramString, new CustomVariable(paramString, paramInt, paramFloat));
  }
  
  public void setCustomAttribute(String paramString, int paramInt1, int paramInt2) {
    this.mCustom.put(paramString, new CustomVariable(paramString, paramInt1, paramInt2));
  }
  
  public void setCustomAttribute(String paramString1, int paramInt, String paramString2) {
    this.mCustom.put(paramString1, new CustomVariable(paramString1, paramInt, paramString2));
  }
  
  public void setCustomAttribute(String paramString, int paramInt, boolean paramBoolean) {
    this.mCustom.put(paramString, new CustomVariable(paramString, paramInt, paramBoolean));
  }
  
  public void setFramePosition(int paramInt) {
    this.mFramePosition = paramInt;
  }
  
  public void setInterpolation(HashMap<String, Integer> paramHashMap) {}
  
  public boolean setValue(int paramInt, float paramFloat) {
    return false;
  }
  
  public boolean setValue(int paramInt1, int paramInt2) {
    switch (paramInt1) {
      default:
        return false;
      case 100:
        break;
    } 
    this.mFramePosition = paramInt2;
    return true;
  }
  
  public boolean setValue(int paramInt, String paramString) {
    switch (paramInt) {
      default:
        return false;
      case 101:
        break;
    } 
    this.mTargetString = paramString;
    return true;
  }
  
  public boolean setValue(int paramInt, boolean paramBoolean) {
    return false;
  }
  
  public MotionKey setViewId(int paramInt) {
    this.mTargetId = paramInt;
    return this;
  }
  
  boolean toBoolean(Object paramObject) {
    boolean bool;
    if (paramObject instanceof Boolean) {
      bool = ((Boolean)paramObject).booleanValue();
    } else {
      bool = Boolean.parseBoolean(paramObject.toString());
    } 
    return bool;
  }
  
  float toFloat(Object paramObject) {
    float f;
    if (paramObject instanceof Float) {
      f = ((Float)paramObject).floatValue();
    } else {
      f = Float.parseFloat(paramObject.toString());
    } 
    return f;
  }
  
  int toInt(Object paramObject) {
    int i;
    if (paramObject instanceof Integer) {
      i = ((Integer)paramObject).intValue();
    } else {
      i = Integer.parseInt(paramObject.toString());
    } 
    return i;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\motion\key\MotionKey.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */