package androidx.constraintlayout.core.motion.key;

import androidx.constraintlayout.core.motion.CustomVariable;
import androidx.constraintlayout.core.motion.utils.KeyCycleOscillator;
import androidx.constraintlayout.core.motion.utils.SplineSet;
import androidx.constraintlayout.core.motion.utils.TypedValues;
import androidx.constraintlayout.core.motion.utils.Utils;
import java.util.HashMap;
import java.util.HashSet;

public class MotionKeyCycle extends MotionKey {
  public static final int KEY_TYPE = 4;
  
  static final String NAME = "KeyCycle";
  
  public static final int SHAPE_BOUNCE = 6;
  
  public static final int SHAPE_COS_WAVE = 5;
  
  public static final int SHAPE_REVERSE_SAW_WAVE = 4;
  
  public static final int SHAPE_SAW_WAVE = 3;
  
  public static final int SHAPE_SIN_WAVE = 0;
  
  public static final int SHAPE_SQUARE_WAVE = 1;
  
  public static final int SHAPE_TRIANGLE_WAVE = 2;
  
  private static final String TAG = "KeyCycle";
  
  public static final String WAVE_OFFSET = "waveOffset";
  
  public static final String WAVE_PERIOD = "wavePeriod";
  
  public static final String WAVE_PHASE = "wavePhase";
  
  public static final String WAVE_SHAPE = "waveShape";
  
  private float mAlpha = Float.NaN;
  
  private int mCurveFit = 0;
  
  private String mCustomWaveShape = null;
  
  private float mElevation = Float.NaN;
  
  private float mProgress = Float.NaN;
  
  private float mRotation = Float.NaN;
  
  private float mRotationX = Float.NaN;
  
  private float mRotationY = Float.NaN;
  
  private float mScaleX = Float.NaN;
  
  private float mScaleY = Float.NaN;
  
  private String mTransitionEasing = null;
  
  private float mTransitionPathRotate = Float.NaN;
  
  private float mTranslationX = Float.NaN;
  
  private float mTranslationY = Float.NaN;
  
  private float mTranslationZ = Float.NaN;
  
  private float mWaveOffset = 0.0F;
  
  private float mWavePeriod = Float.NaN;
  
  private float mWavePhase = 0.0F;
  
  private int mWaveShape = -1;
  
  public void addCycleValues(HashMap<String, KeyCycleOscillator> paramHashMap) {
    for (String str : paramHashMap.keySet()) {
      KeyCycleOscillator keyCycleOscillator2;
      if (str.startsWith("CUSTOM")) {
        String str1 = str.substring("CUSTOM".length() + 1);
        CustomVariable customVariable = this.mCustom.get(str1);
        if (customVariable == null || customVariable.getType() != 901)
          continue; 
        keyCycleOscillator2 = paramHashMap.get(str);
        if (keyCycleOscillator2 == null)
          continue; 
        keyCycleOscillator2.setPoint(this.mFramePosition, this.mWaveShape, this.mCustomWaveShape, -1, this.mWavePeriod, this.mWaveOffset, this.mWavePhase, customVariable.getValueToInterpolate(), customVariable);
        continue;
      } 
      float f = getValue((String)keyCycleOscillator2);
      if (Float.isNaN(f))
        continue; 
      KeyCycleOscillator keyCycleOscillator1 = paramHashMap.get(keyCycleOscillator2);
      if (keyCycleOscillator1 == null)
        continue; 
      keyCycleOscillator1.setPoint(this.mFramePosition, this.mWaveShape, this.mCustomWaveShape, -1, this.mWavePeriod, this.mWaveOffset, this.mWavePhase, f);
    } 
  }
  
  public void addValues(HashMap<String, SplineSet> paramHashMap) {}
  
  public MotionKey clone() {
    return null;
  }
  
  public void dump() {
    System.out.println("MotionKeyCycle{mWaveShape=" + this.mWaveShape + ", mWavePeriod=" + this.mWavePeriod + ", mWaveOffset=" + this.mWaveOffset + ", mWavePhase=" + this.mWavePhase + ", mRotation=" + this.mRotation + '}');
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
    byte b;
    switch (paramString.hashCode()) {
      default:
        b = -1;
        break;
      case 1941332754:
        if (paramString.equals("visibility")) {
          b = 1;
          break;
        } 
      case 1532805160:
        if (paramString.equals("waveShape")) {
          b = 17;
          break;
        } 
      case 803192288:
        if (paramString.equals("pathRotate")) {
          b = 14;
          break;
        } 
      case 579057826:
        if (paramString.equals("curveFit")) {
          b = 0;
          break;
        } 
      case 106629499:
        if (paramString.equals("phase")) {
          b = 18;
          break;
        } 
      case 92909918:
        if (paramString.equals("alpha")) {
          b = 2;
          break;
        } 
      case -908189617:
        if (paramString.equals("scaleY")) {
          b = 10;
          break;
        } 
      case -908189618:
        if (paramString.equals("scaleX")) {
          b = 9;
          break;
        } 
      case -987906985:
        if (paramString.equals("pivotY")) {
          b = 12;
          break;
        } 
      case -987906986:
        if (paramString.equals("pivotX")) {
          b = 11;
          break;
        } 
      case -991726143:
        if (paramString.equals("period")) {
          b = 16;
          break;
        } 
      case -1001078227:
        if (paramString.equals("progress")) {
          b = 13;
          break;
        } 
      case -1019779949:
        if (paramString.equals("offset")) {
          b = 19;
          break;
        } 
      case -1225497655:
        if (paramString.equals("translationZ")) {
          b = 5;
          break;
        } 
      case -1225497656:
        if (paramString.equals("translationY")) {
          b = 4;
          break;
        } 
      case -1225497657:
        if (paramString.equals("translationX")) {
          b = 3;
          break;
        } 
      case -1249320804:
        if (paramString.equals("rotationZ")) {
          b = 8;
          break;
        } 
      case -1249320805:
        if (paramString.equals("rotationY")) {
          b = 7;
          break;
        } 
      case -1249320806:
        if (paramString.equals("rotationX")) {
          b = 6;
          break;
        } 
      case -1310311125:
        if (paramString.equals("easing")) {
          b = 15;
          break;
        } 
      case -1581616630:
        if (paramString.equals("customWave")) {
          b = 20;
          break;
        } 
    } 
    switch (b) {
      default:
        return -1;
      case 20:
        return 422;
      case 19:
        return 424;
      case 18:
        return 425;
      case 17:
        return 421;
      case 16:
        return 423;
      case 15:
        return 420;
      case 14:
        return 416;
      case 13:
        return 315;
      case 12:
        return 314;
      case 11:
        return 313;
      case 10:
        return 312;
      case 9:
        return 311;
      case 8:
        return 310;
      case 7:
        return 309;
      case 6:
        return 308;
      case 5:
        return 306;
      case 4:
        return 305;
      case 3:
        return 304;
      case 2:
        return 403;
      case 1:
        return 402;
      case 0:
        break;
    } 
    return 401;
  }
  
  public float getValue(String paramString) {
    byte b;
    switch (paramString.hashCode()) {
      default:
        b = -1;
        break;
      case 803192288:
        if (paramString.equals("pathRotate")) {
          b = 5;
          break;
        } 
      case 106629499:
        if (paramString.equals("phase")) {
          b = 12;
          break;
        } 
      case 92909918:
        if (paramString.equals("alpha")) {
          b = 0;
          break;
        } 
      case -4379043:
        if (paramString.equals("elevation")) {
          b = 1;
          break;
        } 
      case -908189617:
        if (paramString.equals("scaleY")) {
          b = 7;
          break;
        } 
      case -908189618:
        if (paramString.equals("scaleX")) {
          b = 6;
          break;
        } 
      case -1001078227:
        if (paramString.equals("progress")) {
          b = 13;
          break;
        } 
      case -1019779949:
        if (paramString.equals("offset")) {
          b = 11;
          break;
        } 
      case -1225497655:
        if (paramString.equals("translationZ")) {
          b = 10;
          break;
        } 
      case -1225497656:
        if (paramString.equals("translationY")) {
          b = 9;
          break;
        } 
      case -1225497657:
        if (paramString.equals("translationX")) {
          b = 8;
          break;
        } 
      case -1249320804:
        if (paramString.equals("rotationZ")) {
          b = 2;
          break;
        } 
      case -1249320805:
        if (paramString.equals("rotationY")) {
          b = 4;
          break;
        } 
      case -1249320806:
        if (paramString.equals("rotationX")) {
          b = 3;
          break;
        } 
    } 
    switch (b) {
      default:
        return Float.NaN;
      case 13:
        return this.mProgress;
      case 12:
        return this.mWavePhase;
      case 11:
        return this.mWaveOffset;
      case 10:
        return this.mTranslationZ;
      case 9:
        return this.mTranslationY;
      case 8:
        return this.mTranslationX;
      case 7:
        return this.mScaleY;
      case 6:
        return this.mScaleX;
      case 5:
        return this.mTransitionPathRotate;
      case 4:
        return this.mRotationY;
      case 3:
        return this.mRotationX;
      case 2:
        return this.mRotation;
      case 1:
        return this.mElevation;
      case 0:
        break;
    } 
    return this.mAlpha;
  }
  
  public void printAttributes() {
    HashSet<String> hashSet = new HashSet();
    getAttributeNames(hashSet);
    Utils.log(" ------------- " + this.mFramePosition + " -------------");
    Utils.log("MotionKeyCycle{Shape=" + this.mWaveShape + ", Period=" + this.mWavePeriod + ", Offset=" + this.mWaveOffset + ", Phase=" + this.mWavePhase + '}');
    String[] arrayOfString = (String[])hashSet.toArray((Object[])new String[0]);
    for (byte b = 0; b < arrayOfString.length; b++) {
      TypedValues.Attributes.getId(arrayOfString[b]);
      Utils.log(arrayOfString[b] + ":" + getValue(arrayOfString[b]));
    } 
  }
  
  public boolean setValue(int paramInt, float paramFloat) {
    switch (paramInt) {
      default:
        return super.setValue(paramInt, paramFloat);
      case 425:
        this.mWavePhase = paramFloat;
        return true;
      case 424:
        this.mWaveOffset = paramFloat;
        return true;
      case 423:
        this.mWavePeriod = paramFloat;
        return true;
      case 416:
        this.mTransitionPathRotate = paramFloat;
        return true;
      case 403:
        this.mAlpha = paramFloat;
        return true;
      case 315:
        this.mProgress = paramFloat;
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
        break;
    } 
    this.mTranslationX = paramFloat;
    return true;
  }
  
  public boolean setValue(int paramInt1, int paramInt2) {
    switch (paramInt1) {
      default:
        return setValue(paramInt1, paramInt2) ? true : super.setValue(paramInt1, paramInt2);
      case 421:
        this.mWaveShape = paramInt2;
        return true;
      case 401:
        break;
    } 
    this.mCurveFit = paramInt2;
    return true;
  }
  
  public boolean setValue(int paramInt, String paramString) {
    switch (paramInt) {
      default:
        return super.setValue(paramInt, paramString);
      case 422:
        this.mCustomWaveShape = paramString;
        return true;
      case 420:
        break;
    } 
    this.mTransitionEasing = paramString;
    return true;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\motion\key\MotionKeyCycle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */