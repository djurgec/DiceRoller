package androidx.constraintlayout.core.motion.key;

import androidx.constraintlayout.core.motion.CustomVariable;
import androidx.constraintlayout.core.motion.utils.SplineSet;
import androidx.constraintlayout.core.motion.utils.TypedValues;
import java.util.HashMap;
import java.util.HashSet;

public class MotionKeyAttributes extends MotionKey {
  private static final boolean DEBUG = false;
  
  public static final int KEY_TYPE = 1;
  
  static final String NAME = "KeyAttribute";
  
  private static final String TAG = "KeyAttributes";
  
  private float mAlpha = Float.NaN;
  
  private int mCurveFit = -1;
  
  private float mElevation = Float.NaN;
  
  private float mPivotX = Float.NaN;
  
  private float mPivotY = Float.NaN;
  
  private float mProgress = Float.NaN;
  
  private float mRotation = Float.NaN;
  
  private float mRotationX = Float.NaN;
  
  private float mRotationY = Float.NaN;
  
  private float mScaleX = Float.NaN;
  
  private float mScaleY = Float.NaN;
  
  private String mTransitionEasing;
  
  private float mTransitionPathRotate = Float.NaN;
  
  private float mTranslationX = Float.NaN;
  
  private float mTranslationY = Float.NaN;
  
  private float mTranslationZ = Float.NaN;
  
  private int mVisibility = 0;
  
  private float getFloatValue(int paramInt) {
    switch (paramInt) {
      default:
        return Float.NaN;
      case 316:
        return this.mTransitionPathRotate;
      case 315:
        return this.mProgress;
      case 314:
        return this.mPivotY;
      case 313:
        return this.mPivotX;
      case 312:
        return this.mScaleY;
      case 311:
        return this.mScaleX;
      case 310:
        return this.mRotation;
      case 309:
        return this.mRotationY;
      case 308:
        return this.mRotationX;
      case 307:
        return this.mElevation;
      case 306:
        return this.mTranslationZ;
      case 305:
        return this.mTranslationY;
      case 304:
        return this.mTranslationX;
      case 303:
        return this.mAlpha;
      case 100:
        break;
    } 
    return this.mFramePosition;
  }
  
  public void addValues(HashMap<String, SplineSet> paramHashMap) {
    for (String str : paramHashMap.keySet()) {
      CustomVariable customVariable;
      SplineSet splineSet = paramHashMap.get(str);
      if (splineSet == null)
        continue; 
      boolean bool = str.startsWith("CUSTOM");
      byte b = 1;
      if (bool) {
        str = str.substring("CUSTOM".length() + 1);
        customVariable = this.mCustom.get(str);
        if (customVariable != null)
          ((SplineSet.CustomSpline)splineSet).setPoint(this.mFramePosition, customVariable); 
        continue;
      } 
      switch (customVariable.hashCode()) {
        default:
          b = -1;
          break;
        case 803192288:
          if (customVariable.equals("pathRotate")) {
            b = 7;
            break;
          } 
        case 92909918:
          if (customVariable.equals("alpha")) {
            b = 0;
            break;
          } 
        case -4379043:
          if (customVariable.equals("elevation"))
            break; 
        case -908189617:
          if (customVariable.equals("scaleY")) {
            b = 9;
            break;
          } 
        case -908189618:
          if (customVariable.equals("scaleX")) {
            b = 8;
            break;
          } 
        case -987906985:
          if (customVariable.equals("pivotY")) {
            b = 6;
            break;
          } 
        case -987906986:
          if (customVariable.equals("pivotX")) {
            b = 5;
            break;
          } 
        case -1001078227:
          if (customVariable.equals("progress")) {
            b = 13;
            break;
          } 
        case -1225497655:
          if (customVariable.equals("translationZ")) {
            b = 12;
            break;
          } 
        case -1225497656:
          if (customVariable.equals("translationY")) {
            b = 11;
            break;
          } 
        case -1225497657:
          if (customVariable.equals("translationX")) {
            b = 10;
            break;
          } 
        case -1249320804:
          if (customVariable.equals("rotationZ")) {
            b = 2;
            break;
          } 
        case -1249320805:
          if (customVariable.equals("rotationY")) {
            b = 4;
            break;
          } 
        case -1249320806:
          if (customVariable.equals("rotationX")) {
            b = 3;
            break;
          } 
      } 
      switch (b) {
        default:
          System.err.println("not supported by KeyAttributes " + customVariable);
          continue;
        case 13:
          if (!Float.isNaN(this.mProgress))
            splineSet.setPoint(this.mFramePosition, this.mProgress); 
          continue;
        case 12:
          if (!Float.isNaN(this.mTranslationZ))
            splineSet.setPoint(this.mFramePosition, this.mTranslationZ); 
          continue;
        case 11:
          if (!Float.isNaN(this.mTranslationY))
            splineSet.setPoint(this.mFramePosition, this.mTranslationY); 
          continue;
        case 10:
          if (!Float.isNaN(this.mTranslationX))
            splineSet.setPoint(this.mFramePosition, this.mTranslationX); 
          continue;
        case 9:
          if (!Float.isNaN(this.mScaleY))
            splineSet.setPoint(this.mFramePosition, this.mScaleY); 
          continue;
        case 8:
          if (!Float.isNaN(this.mScaleX))
            splineSet.setPoint(this.mFramePosition, this.mScaleX); 
          continue;
        case 7:
          if (!Float.isNaN(this.mTransitionPathRotate))
            splineSet.setPoint(this.mFramePosition, this.mTransitionPathRotate); 
          continue;
        case 6:
          if (!Float.isNaN(this.mRotationY))
            splineSet.setPoint(this.mFramePosition, this.mPivotY); 
          continue;
        case 5:
          if (!Float.isNaN(this.mRotationX))
            splineSet.setPoint(this.mFramePosition, this.mPivotX); 
          continue;
        case 4:
          if (!Float.isNaN(this.mRotationY))
            splineSet.setPoint(this.mFramePosition, this.mRotationY); 
          continue;
        case 3:
          if (!Float.isNaN(this.mRotationX))
            splineSet.setPoint(this.mFramePosition, this.mRotationX); 
          continue;
        case 2:
          if (!Float.isNaN(this.mRotation))
            splineSet.setPoint(this.mFramePosition, this.mRotation); 
          continue;
        case 1:
          if (!Float.isNaN(this.mElevation))
            splineSet.setPoint(this.mFramePosition, this.mElevation); 
          continue;
        case 0:
          break;
      } 
      if (!Float.isNaN(this.mAlpha))
        splineSet.setPoint(this.mFramePosition, this.mAlpha); 
    } 
  }
  
  public MotionKey clone() {
    return null;
  }
  
  public void getAttributeNames(HashSet<String> paramHashSet) {
    if (!Float.isNaN(this.mAlpha))
      paramHashSet.add("alpha"); 
    if (!Float.isNaN(this.mElevation))
      paramHashSet.add("elevation"); 
    if (!Float.isNaN(this.mRotation))
      paramHashSet.add("rotationZ"); 
    if (!Float.isNaN(this.mRotationX))
      paramHashSet.add("rotationX"); 
    if (!Float.isNaN(this.mRotationY))
      paramHashSet.add("rotationY"); 
    if (!Float.isNaN(this.mPivotX))
      paramHashSet.add("pivotX"); 
    if (!Float.isNaN(this.mPivotY))
      paramHashSet.add("pivotY"); 
    if (!Float.isNaN(this.mTranslationX))
      paramHashSet.add("translationX"); 
    if (!Float.isNaN(this.mTranslationY))
      paramHashSet.add("translationY"); 
    if (!Float.isNaN(this.mTranslationZ))
      paramHashSet.add("translationZ"); 
    if (!Float.isNaN(this.mTransitionPathRotate))
      paramHashSet.add("pathRotate"); 
    if (!Float.isNaN(this.mScaleX))
      paramHashSet.add("scaleX"); 
    if (!Float.isNaN(this.mScaleY))
      paramHashSet.add("scaleY"); 
    if (!Float.isNaN(this.mProgress))
      paramHashSet.add("progress"); 
    if (this.mCustom.size() > 0)
      for (String str : this.mCustom.keySet())
        paramHashSet.add("CUSTOM," + str);  
  }
  
  public int getCurveFit() {
    return this.mCurveFit;
  }
  
  public int getId(String paramString) {
    return TypedValues.Attributes.getId(paramString);
  }
  
  public void printAttributes() {
    HashSet<String> hashSet = new HashSet();
    getAttributeNames(hashSet);
    System.out.println(" ------------- " + this.mFramePosition + " -------------");
    String[] arrayOfString = (String[])hashSet.toArray((Object[])new String[0]);
    for (byte b = 0; b < arrayOfString.length; b++) {
      int i = TypedValues.Attributes.getId(arrayOfString[b]);
      System.out.println(arrayOfString[b] + ":" + getFloatValue(i));
    } 
  }
  
  public void setInterpolation(HashMap<String, Integer> paramHashMap) {
    if (!Float.isNaN(this.mAlpha))
      paramHashMap.put("alpha", Integer.valueOf(this.mCurveFit)); 
    if (!Float.isNaN(this.mElevation))
      paramHashMap.put("elevation", Integer.valueOf(this.mCurveFit)); 
    if (!Float.isNaN(this.mRotation))
      paramHashMap.put("rotationZ", Integer.valueOf(this.mCurveFit)); 
    if (!Float.isNaN(this.mRotationX))
      paramHashMap.put("rotationX", Integer.valueOf(this.mCurveFit)); 
    if (!Float.isNaN(this.mRotationY))
      paramHashMap.put("rotationY", Integer.valueOf(this.mCurveFit)); 
    if (!Float.isNaN(this.mPivotX))
      paramHashMap.put("pivotX", Integer.valueOf(this.mCurveFit)); 
    if (!Float.isNaN(this.mPivotY))
      paramHashMap.put("pivotY", Integer.valueOf(this.mCurveFit)); 
    if (!Float.isNaN(this.mTranslationX))
      paramHashMap.put("translationX", Integer.valueOf(this.mCurveFit)); 
    if (!Float.isNaN(this.mTranslationY))
      paramHashMap.put("translationY", Integer.valueOf(this.mCurveFit)); 
    if (!Float.isNaN(this.mTranslationZ))
      paramHashMap.put("translationZ", Integer.valueOf(this.mCurveFit)); 
    if (!Float.isNaN(this.mTransitionPathRotate))
      paramHashMap.put("pathRotate", Integer.valueOf(this.mCurveFit)); 
    if (!Float.isNaN(this.mScaleX))
      paramHashMap.put("scaleX", Integer.valueOf(this.mCurveFit)); 
    if (!Float.isNaN(this.mScaleY))
      paramHashMap.put("scaleY", Integer.valueOf(this.mCurveFit)); 
    if (!Float.isNaN(this.mProgress))
      paramHashMap.put("progress", Integer.valueOf(this.mCurveFit)); 
    if (this.mCustom.size() > 0)
      for (String str : this.mCustom.keySet())
        paramHashMap.put("CUSTOM," + str, Integer.valueOf(this.mCurveFit));  
  }
  
  public boolean setValue(int paramInt, float paramFloat) {
    switch (paramInt) {
      default:
        return super.setValue(paramInt, paramFloat);
      case 316:
        this.mTransitionPathRotate = paramFloat;
        return true;
      case 315:
        this.mProgress = paramFloat;
        return true;
      case 314:
        this.mPivotY = paramFloat;
        return true;
      case 313:
        this.mPivotX = paramFloat;
        return true;
      case 312:
        this.mScaleY = paramFloat;
        return true;
      case 311:
        this.mScaleX = paramFloat;
        return true;
      case 310:
        this.mRotation = paramFloat;
        return true;
      case 309:
        this.mRotationY = paramFloat;
        return true;
      case 308:
        this.mRotationX = paramFloat;
        return true;
      case 307:
        this.mElevation = paramFloat;
        return true;
      case 306:
        this.mTranslationZ = paramFloat;
        return true;
      case 305:
        this.mTranslationY = paramFloat;
        return true;
      case 304:
        this.mTranslationX = paramFloat;
        return true;
      case 303:
        this.mAlpha = paramFloat;
        return true;
      case 100:
        break;
    } 
    this.mTransitionPathRotate = paramFloat;
    return true;
  }
  
  public boolean setValue(int paramInt1, int paramInt2) {
    switch (paramInt1) {
      default:
        return !setValue(paramInt1, paramInt2) ? super.setValue(paramInt1, paramInt2) : true;
      case 302:
        this.mVisibility = paramInt2;
        return true;
      case 301:
        this.mCurveFit = paramInt2;
        return true;
      case 100:
        break;
    } 
    this.mFramePosition = paramInt2;
    return true;
  }
  
  public boolean setValue(int paramInt, String paramString) {
    switch (paramInt) {
      default:
        return super.setValue(paramInt, paramString);
      case 317:
        this.mTransitionEasing = paramString;
        return true;
      case 101:
        break;
    } 
    this.mTargetString = paramString;
    return true;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\motion\key\MotionKeyAttributes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */