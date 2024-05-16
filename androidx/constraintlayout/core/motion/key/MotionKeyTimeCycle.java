package androidx.constraintlayout.core.motion.key;

import androidx.constraintlayout.core.motion.CustomVariable;
import androidx.constraintlayout.core.motion.utils.SplineSet;
import androidx.constraintlayout.core.motion.utils.TimeCycleSplineSet;
import androidx.constraintlayout.core.motion.utils.TypedValues;
import androidx.constraintlayout.core.motion.utils.Utils;
import java.util.HashMap;
import java.util.HashSet;

public class MotionKeyTimeCycle extends MotionKey {
  public static final int KEY_TYPE = 3;
  
  static final String NAME = "KeyTimeCycle";
  
  private static final String TAG = "KeyTimeCycle";
  
  private float mAlpha = Float.NaN;
  
  private int mCurveFit = -1;
  
  private String mCustomWaveShape = null;
  
  private float mElevation = Float.NaN;
  
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
  
  private float mWaveOffset = 0.0F;
  
  private float mWavePeriod = Float.NaN;
  
  private int mWaveShape = 0;
  
  public void addTimeValues(HashMap<String, TimeCycleSplineSet> paramHashMap) {
    for (String str : paramHashMap.keySet()) {
      CustomVariable customVariable;
      TimeCycleSplineSet timeCycleSplineSet = paramHashMap.get(str);
      if (timeCycleSplineSet == null)
        continue; 
      boolean bool = str.startsWith("CUSTOM");
      byte b = 1;
      if (bool) {
        str = str.substring("CUSTOM".length() + 1);
        customVariable = this.mCustom.get(str);
        if (customVariable != null)
          ((TimeCycleSplineSet.CustomVarSet)timeCycleSplineSet).setPoint(this.mFramePosition, customVariable, this.mWavePeriod, this.mWaveShape, this.mWaveOffset); 
        continue;
      } 
      switch (customVariable.hashCode()) {
        default:
          b = -1;
          break;
        case 803192288:
          if (customVariable.equals("pathRotate")) {
            b = 4;
            break;
          } 
        case 92909918:
          if (customVariable.equals("alpha")) {
            b = 0;
            break;
          } 
        case -4379043:
          if (customVariable.equals("elevation")) {
            b = 10;
            break;
          } 
        case -908189617:
          if (customVariable.equals("scaleY")) {
            b = 6;
            break;
          } 
        case -908189618:
          if (customVariable.equals("scaleX")) {
            b = 5;
            break;
          } 
        case -1001078227:
          if (customVariable.equals("progress")) {
            b = 11;
            break;
          } 
        case -1225497655:
          if (customVariable.equals("translationZ")) {
            b = 9;
            break;
          } 
        case -1225497656:
          if (customVariable.equals("translationY")) {
            b = 8;
            break;
          } 
        case -1225497657:
          if (customVariable.equals("translationX")) {
            b = 7;
            break;
          } 
        case -1249320804:
          if (customVariable.equals("rotationZ")) {
            b = 3;
            break;
          } 
        case -1249320805:
          if (customVariable.equals("rotationY")) {
            b = 2;
            break;
          } 
        case -1249320806:
          if (customVariable.equals("rotationX"))
            break; 
      } 
      switch (b) {
        default:
          Utils.loge("KeyTimeCycles", "UNKNOWN addValues \"" + customVariable + "\"");
          continue;
        case 11:
          if (!Float.isNaN(this.mProgress))
            timeCycleSplineSet.setPoint(this.mFramePosition, this.mProgress, this.mWavePeriod, this.mWaveShape, this.mWaveOffset); 
          continue;
        case 10:
          if (!Float.isNaN(this.mTranslationZ))
            timeCycleSplineSet.setPoint(this.mFramePosition, this.mTranslationZ, this.mWavePeriod, this.mWaveShape, this.mWaveOffset); 
          continue;
        case 9:
          if (!Float.isNaN(this.mTranslationZ))
            timeCycleSplineSet.setPoint(this.mFramePosition, this.mTranslationZ, this.mWavePeriod, this.mWaveShape, this.mWaveOffset); 
          continue;
        case 8:
          if (!Float.isNaN(this.mTranslationY))
            timeCycleSplineSet.setPoint(this.mFramePosition, this.mTranslationY, this.mWavePeriod, this.mWaveShape, this.mWaveOffset); 
          continue;
        case 7:
          if (!Float.isNaN(this.mTranslationX))
            timeCycleSplineSet.setPoint(this.mFramePosition, this.mTranslationX, this.mWavePeriod, this.mWaveShape, this.mWaveOffset); 
          continue;
        case 6:
          if (!Float.isNaN(this.mScaleY))
            timeCycleSplineSet.setPoint(this.mFramePosition, this.mScaleY, this.mWavePeriod, this.mWaveShape, this.mWaveOffset); 
          continue;
        case 5:
          if (!Float.isNaN(this.mScaleX))
            timeCycleSplineSet.setPoint(this.mFramePosition, this.mScaleX, this.mWavePeriod, this.mWaveShape, this.mWaveOffset); 
          continue;
        case 4:
          if (!Float.isNaN(this.mTransitionPathRotate))
            timeCycleSplineSet.setPoint(this.mFramePosition, this.mTransitionPathRotate, this.mWavePeriod, this.mWaveShape, this.mWaveOffset); 
          continue;
        case 3:
          if (!Float.isNaN(this.mRotation))
            timeCycleSplineSet.setPoint(this.mFramePosition, this.mRotation, this.mWavePeriod, this.mWaveShape, this.mWaveOffset); 
          continue;
        case 2:
          if (!Float.isNaN(this.mRotationY))
            timeCycleSplineSet.setPoint(this.mFramePosition, this.mRotationY, this.mWavePeriod, this.mWaveShape, this.mWaveOffset); 
          continue;
        case 1:
          if (!Float.isNaN(this.mRotationX))
            timeCycleSplineSet.setPoint(this.mFramePosition, this.mRotationX, this.mWavePeriod, this.mWaveShape, this.mWaveOffset); 
          continue;
        case 0:
          break;
      } 
      if (!Float.isNaN(this.mAlpha))
        timeCycleSplineSet.setPoint(this.mFramePosition, this.mAlpha, this.mWavePeriod, this.mWaveShape, this.mWaveOffset); 
    } 
  }
  
  public void addValues(HashMap<String, SplineSet> paramHashMap) {}
  
  public MotionKey clone() {
    return (new MotionKeyTimeCycle()).copy(this);
  }
  
  public MotionKeyTimeCycle copy(MotionKey paramMotionKey) {
    super.copy(paramMotionKey);
    paramMotionKey = paramMotionKey;
    this.mTransitionEasing = ((MotionKeyTimeCycle)paramMotionKey).mTransitionEasing;
    this.mCurveFit = ((MotionKeyTimeCycle)paramMotionKey).mCurveFit;
    this.mWaveShape = ((MotionKeyTimeCycle)paramMotionKey).mWaveShape;
    this.mWavePeriod = ((MotionKeyTimeCycle)paramMotionKey).mWavePeriod;
    this.mWaveOffset = ((MotionKeyTimeCycle)paramMotionKey).mWaveOffset;
    this.mProgress = ((MotionKeyTimeCycle)paramMotionKey).mProgress;
    this.mAlpha = ((MotionKeyTimeCycle)paramMotionKey).mAlpha;
    this.mElevation = ((MotionKeyTimeCycle)paramMotionKey).mElevation;
    this.mRotation = ((MotionKeyTimeCycle)paramMotionKey).mRotation;
    this.mTransitionPathRotate = ((MotionKeyTimeCycle)paramMotionKey).mTransitionPathRotate;
    this.mRotationX = ((MotionKeyTimeCycle)paramMotionKey).mRotationX;
    this.mRotationY = ((MotionKeyTimeCycle)paramMotionKey).mRotationY;
    this.mScaleX = ((MotionKeyTimeCycle)paramMotionKey).mScaleX;
    this.mScaleY = ((MotionKeyTimeCycle)paramMotionKey).mScaleY;
    this.mTranslationX = ((MotionKeyTimeCycle)paramMotionKey).mTranslationX;
    this.mTranslationY = ((MotionKeyTimeCycle)paramMotionKey).mTranslationY;
    this.mTranslationZ = ((MotionKeyTimeCycle)paramMotionKey).mTranslationZ;
    return this;
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
    if (!Float.isNaN(this.mScaleX))
      paramHashSet.add("scaleX"); 
    if (!Float.isNaN(this.mScaleY))
      paramHashSet.add("scaleY"); 
    if (!Float.isNaN(this.mTransitionPathRotate))
      paramHashSet.add("pathRotate"); 
    if (!Float.isNaN(this.mTranslationX))
      paramHashSet.add("translationX"); 
    if (!Float.isNaN(this.mTranslationY))
      paramHashSet.add("translationY"); 
    if (!Float.isNaN(this.mTranslationZ))
      paramHashSet.add("translationZ"); 
    if (this.mCustom.size() > 0)
      for (String str : this.mCustom.keySet())
        paramHashSet.add("CUSTOM," + str);  
  }
  
  public int getId(String paramString) {
    return TypedValues.Cycle.getId(paramString);
  }
  
  public boolean setValue(int paramInt, float paramFloat) {
    switch (paramInt) {
      default:
        return super.setValue(paramInt, paramFloat);
      case 424:
        this.mWaveOffset = toFloat(Float.valueOf(paramFloat));
        return true;
      case 423:
        this.mWavePeriod = toFloat(Float.valueOf(paramFloat));
        return true;
      case 416:
        this.mTransitionPathRotate = toFloat(Float.valueOf(paramFloat));
        return true;
      case 403:
        this.mAlpha = paramFloat;
        return true;
      case 401:
        this.mCurveFit = toInt(Float.valueOf(paramFloat));
        return true;
      case 315:
        this.mProgress = toFloat(Float.valueOf(paramFloat));
        return true;
      case 312:
        this.mScaleY = toFloat(Float.valueOf(paramFloat));
        return true;
      case 311:
        this.mScaleX = toFloat(Float.valueOf(paramFloat));
        return true;
      case 310:
        this.mRotation = toFloat(Float.valueOf(paramFloat));
        return true;
      case 309:
        this.mRotationY = toFloat(Float.valueOf(paramFloat));
        return true;
      case 308:
        this.mRotationX = toFloat(Float.valueOf(paramFloat));
        return true;
      case 307:
        this.mElevation = toFloat(Float.valueOf(paramFloat));
        return true;
      case 306:
        this.mTranslationZ = toFloat(Float.valueOf(paramFloat));
        return true;
      case 305:
        this.mTranslationY = toFloat(Float.valueOf(paramFloat));
        return true;
      case 304:
        break;
    } 
    this.mTranslationX = toFloat(Float.valueOf(paramFloat));
    return true;
  }
  
  public boolean setValue(int paramInt1, int paramInt2) {
    switch (paramInt1) {
      default:
        return super.setValue(paramInt1, paramInt2);
      case 421:
        this.mWaveShape = paramInt2;
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
      case 421:
        this.mWaveShape = 7;
        this.mCustomWaveShape = paramString;
        return true;
      case 420:
        break;
    } 
    this.mTransitionEasing = paramString;
    return true;
  }
  
  public boolean setValue(int paramInt, boolean paramBoolean) {
    return super.setValue(paramInt, paramBoolean);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\motion\key\MotionKeyTimeCycle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */