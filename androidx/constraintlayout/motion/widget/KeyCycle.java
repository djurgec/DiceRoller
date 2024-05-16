package androidx.constraintlayout.motion.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import androidx.constraintlayout.core.motion.utils.SplineSet;
import androidx.constraintlayout.motion.utils.ViewOscillator;
import androidx.constraintlayout.motion.utils.ViewSpline;
import androidx.constraintlayout.widget.ConstraintAttribute;
import androidx.constraintlayout.widget.R;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class KeyCycle extends Key {
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
  
  private int mWaveVariesBy = -1;
  
  public void addCycleValues(HashMap<String, ViewOscillator> paramHashMap) {
    for (String str : paramHashMap.keySet()) {
      if (str.startsWith("CUSTOM")) {
        String str1 = str.substring("CUSTOM".length() + 1);
        ConstraintAttribute constraintAttribute = this.mCustomConstraints.get(str1);
        if (constraintAttribute == null || constraintAttribute.getType() != ConstraintAttribute.AttributeType.FLOAT_TYPE)
          continue; 
        viewOscillator = paramHashMap.get(str);
        if (viewOscillator == null)
          continue; 
        viewOscillator.setPoint(this.mFramePosition, this.mWaveShape, this.mCustomWaveShape, this.mWaveVariesBy, this.mWavePeriod, this.mWaveOffset, this.mWavePhase, constraintAttribute.getValueToInterpolate(), constraintAttribute);
        continue;
      } 
      float f = getValue((String)viewOscillator);
      if (Float.isNaN(f))
        continue; 
      ViewOscillator viewOscillator = paramHashMap.get(viewOscillator);
      if (viewOscillator == null)
        continue; 
      viewOscillator.setPoint(this.mFramePosition, this.mWaveShape, this.mCustomWaveShape, this.mWaveVariesBy, this.mWavePeriod, this.mWaveOffset, this.mWavePhase, f);
    } 
  }
  
  public void addValues(HashMap<String, ViewSpline> paramHashMap) {
    int i = paramHashMap.size();
    Debug.logStack("KeyCycle", (new StringBuilder(22)).append("add ").append(i).append(" values").toString(), 2);
    Iterator<String> iterator = paramHashMap.keySet().iterator();
    while (true) {
      String str;
      if (iterator.hasNext()) {
        str = iterator.next();
        SplineSet splineSet = (SplineSet)paramHashMap.get(str);
        if (splineSet == null)
          continue; 
        i = -1;
        switch (str.hashCode()) {
          case 1530034690:
            if (str.equals("wavePhase"))
              i = 12; 
            break;
          case 156108012:
            if (str.equals("waveOffset"))
              i = 11; 
            break;
          case 92909918:
            if (str.equals("alpha"))
              i = 0; 
            break;
          case 37232917:
            if (str.equals("transitionPathRotate"))
              i = 5; 
            break;
          case -4379043:
            if (str.equals("elevation"))
              i = 1; 
            break;
          case -40300674:
            if (str.equals("rotation"))
              i = 2; 
            break;
          case -908189617:
            if (str.equals("scaleY"))
              i = 7; 
            break;
          case -908189618:
            if (str.equals("scaleX"))
              i = 6; 
            break;
          case -1001078227:
            if (str.equals("progress"))
              i = 13; 
            break;
          case -1225497655:
            if (str.equals("translationZ"))
              i = 10; 
            break;
          case -1225497656:
            if (str.equals("translationY"))
              i = 9; 
            break;
          case -1225497657:
            if (str.equals("translationX"))
              i = 8; 
            break;
          case -1249320805:
            if (str.equals("rotationY"))
              i = 4; 
            break;
          case -1249320806:
            if (str.equals("rotationX"))
              i = 3; 
            break;
        } 
        switch (i) {
          default:
            if (!str.startsWith("CUSTOM")) {
              str = String.valueOf(str);
              if (str.length() != 0) {
                str = "  UNKNOWN  ".concat(str);
                break;
              } 
              str = new String("  UNKNOWN  ");
              Log.v("WARNING KeyCycle", str);
            } 
            continue;
          case 13:
            splineSet.setPoint(this.mFramePosition, this.mProgress);
            continue;
          case 12:
            splineSet.setPoint(this.mFramePosition, this.mWavePhase);
            continue;
          case 11:
            splineSet.setPoint(this.mFramePosition, this.mWaveOffset);
            continue;
          case 10:
            splineSet.setPoint(this.mFramePosition, this.mTranslationZ);
            continue;
          case 9:
            splineSet.setPoint(this.mFramePosition, this.mTranslationY);
            continue;
          case 8:
            splineSet.setPoint(this.mFramePosition, this.mTranslationX);
            continue;
          case 7:
            splineSet.setPoint(this.mFramePosition, this.mScaleY);
            continue;
          case 6:
            splineSet.setPoint(this.mFramePosition, this.mScaleX);
            continue;
          case 5:
            splineSet.setPoint(this.mFramePosition, this.mTransitionPathRotate);
            continue;
          case 4:
            splineSet.setPoint(this.mFramePosition, this.mRotationY);
            continue;
          case 3:
            splineSet.setPoint(this.mFramePosition, this.mRotationX);
            continue;
          case 2:
            splineSet.setPoint(this.mFramePosition, this.mRotation);
            continue;
          case 1:
            splineSet.setPoint(this.mFramePosition, this.mElevation);
            continue;
          case 0:
            splineSet.setPoint(this.mFramePosition, this.mAlpha);
            continue;
        } 
      } else {
        break;
      } 
      Log.v("WARNING KeyCycle", str);
    } 
  }
  
  public Key clone() {
    return (new KeyCycle()).copy(this);
  }
  
  public Key copy(Key paramKey) {
    super.copy(paramKey);
    paramKey = paramKey;
    this.mTransitionEasing = ((KeyCycle)paramKey).mTransitionEasing;
    this.mCurveFit = ((KeyCycle)paramKey).mCurveFit;
    this.mWaveShape = ((KeyCycle)paramKey).mWaveShape;
    this.mCustomWaveShape = ((KeyCycle)paramKey).mCustomWaveShape;
    this.mWavePeriod = ((KeyCycle)paramKey).mWavePeriod;
    this.mWaveOffset = ((KeyCycle)paramKey).mWaveOffset;
    this.mWavePhase = ((KeyCycle)paramKey).mWavePhase;
    this.mProgress = ((KeyCycle)paramKey).mProgress;
    this.mWaveVariesBy = ((KeyCycle)paramKey).mWaveVariesBy;
    this.mAlpha = ((KeyCycle)paramKey).mAlpha;
    this.mElevation = ((KeyCycle)paramKey).mElevation;
    this.mRotation = ((KeyCycle)paramKey).mRotation;
    this.mTransitionPathRotate = ((KeyCycle)paramKey).mTransitionPathRotate;
    this.mRotationX = ((KeyCycle)paramKey).mRotationX;
    this.mRotationY = ((KeyCycle)paramKey).mRotationY;
    this.mScaleX = ((KeyCycle)paramKey).mScaleX;
    this.mScaleY = ((KeyCycle)paramKey).mScaleY;
    this.mTranslationX = ((KeyCycle)paramKey).mTranslationX;
    this.mTranslationY = ((KeyCycle)paramKey).mTranslationY;
    this.mTranslationZ = ((KeyCycle)paramKey).mTranslationZ;
    return this;
  }
  
  public void getAttributeNames(HashSet<String> paramHashSet) {
    if (!Float.isNaN(this.mAlpha))
      paramHashSet.add("alpha"); 
    if (!Float.isNaN(this.mElevation))
      paramHashSet.add("elevation"); 
    if (!Float.isNaN(this.mRotation))
      paramHashSet.add("rotation"); 
    if (!Float.isNaN(this.mRotationX))
      paramHashSet.add("rotationX"); 
    if (!Float.isNaN(this.mRotationY))
      paramHashSet.add("rotationY"); 
    if (!Float.isNaN(this.mScaleX))
      paramHashSet.add("scaleX"); 
    if (!Float.isNaN(this.mScaleY))
      paramHashSet.add("scaleY"); 
    if (!Float.isNaN(this.mTransitionPathRotate))
      paramHashSet.add("transitionPathRotate"); 
    if (!Float.isNaN(this.mTranslationX))
      paramHashSet.add("translationX"); 
    if (!Float.isNaN(this.mTranslationY))
      paramHashSet.add("translationY"); 
    if (!Float.isNaN(this.mTranslationZ))
      paramHashSet.add("translationZ"); 
    if (this.mCustomConstraints.size() > 0)
      for (String str2 : this.mCustomConstraints.keySet()) {
        String str1 = String.valueOf("CUSTOM,");
        str2 = String.valueOf(str2);
        if (str2.length() != 0) {
          str1 = str1.concat(str2);
        } else {
          str1 = new String(str1);
        } 
        paramHashSet.add(str1);
      }  
  }
  
  public float getValue(String paramString) {
    byte b;
    switch (paramString.hashCode()) {
      default:
        b = -1;
        break;
      case 1530034690:
        if (paramString.equals("wavePhase")) {
          b = 12;
          break;
        } 
      case 156108012:
        if (paramString.equals("waveOffset")) {
          b = 11;
          break;
        } 
      case 92909918:
        if (paramString.equals("alpha")) {
          b = 0;
          break;
        } 
      case 37232917:
        if (paramString.equals("transitionPathRotate")) {
          b = 5;
          break;
        } 
      case -4379043:
        if (paramString.equals("elevation")) {
          b = 1;
          break;
        } 
      case -40300674:
        if (paramString.equals("rotation")) {
          b = 2;
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
        if (!paramString.startsWith("CUSTOM")) {
          paramString = String.valueOf(paramString);
          if (paramString.length() != 0) {
            paramString = "  UNKNOWN  ".concat(paramString);
            break;
          } 
          paramString = new String("  UNKNOWN  ");
          Log.v("WARNING! KeyCycle", paramString);
        } 
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
        return this.mAlpha;
    } 
    Log.v("WARNING! KeyCycle", paramString);
  }
  
  public void load(Context paramContext, AttributeSet paramAttributeSet) {
    Loader.read(this, paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.KeyCycle));
  }
  
  public void setValue(String paramString, Object paramObject) {
    byte b;
    switch (paramString.hashCode()) {
      default:
        b = -1;
        break;
      case 1532805160:
        if (paramString.equals("waveShape")) {
          b = 17;
          break;
        } 
      case 1530034690:
        if (paramString.equals("wavePhase")) {
          b = 16;
          break;
        } 
      case 579057826:
        if (paramString.equals("curveFit")) {
          b = 1;
          break;
        } 
      case 184161818:
        if (paramString.equals("wavePeriod")) {
          b = 14;
          break;
        } 
      case 156108012:
        if (paramString.equals("waveOffset")) {
          b = 15;
          break;
        } 
      case 92909918:
        if (paramString.equals("alpha")) {
          b = 0;
          break;
        } 
      case 37232917:
        if (paramString.equals("transitionPathRotate")) {
          b = 10;
          break;
        } 
      case -4379043:
        if (paramString.equals("elevation")) {
          b = 2;
          break;
        } 
      case -40300674:
        if (paramString.equals("rotation")) {
          b = 4;
          break;
        } 
      case -908189617:
        if (paramString.equals("scaleY")) {
          b = 8;
          break;
        } 
      case -908189618:
        if (paramString.equals("scaleX")) {
          b = 7;
          break;
        } 
      case -1225497655:
        if (paramString.equals("translationZ")) {
          b = 13;
          break;
        } 
      case -1225497656:
        if (paramString.equals("translationY")) {
          b = 12;
          break;
        } 
      case -1225497657:
        if (paramString.equals("translationX")) {
          b = 11;
          break;
        } 
      case -1249320805:
        if (paramString.equals("rotationY")) {
          b = 6;
          break;
        } 
      case -1249320806:
        if (paramString.equals("rotationX")) {
          b = 5;
          break;
        } 
      case -1812823328:
        if (paramString.equals("transitionEasing")) {
          b = 9;
          break;
        } 
      case -1913008125:
        if (paramString.equals("motionProgress")) {
          b = 3;
          break;
        } 
    } 
    switch (b) {
      default:
        return;
      case 17:
        if (paramObject instanceof Integer) {
          this.mWaveShape = toInt(paramObject);
        } else {
          this.mWaveShape = 7;
          this.mCustomWaveShape = paramObject.toString();
        } 
      case 16:
        this.mWavePhase = toFloat(paramObject);
      case 15:
        this.mWaveOffset = toFloat(paramObject);
      case 14:
        this.mWavePeriod = toFloat(paramObject);
      case 13:
        this.mTranslationZ = toFloat(paramObject);
      case 12:
        this.mTranslationY = toFloat(paramObject);
      case 11:
        this.mTranslationX = toFloat(paramObject);
      case 10:
        this.mTransitionPathRotate = toFloat(paramObject);
      case 9:
        this.mTransitionEasing = paramObject.toString();
      case 8:
        this.mScaleY = toFloat(paramObject);
      case 7:
        this.mScaleX = toFloat(paramObject);
      case 6:
        this.mRotationY = toFloat(paramObject);
      case 5:
        this.mRotationX = toFloat(paramObject);
      case 4:
        this.mRotation = toFloat(paramObject);
      case 3:
        this.mProgress = toFloat(paramObject);
      case 2:
        this.mElevation = toFloat(paramObject);
      case 1:
        this.mCurveFit = toInt(paramObject);
      case 0:
        break;
    } 
    this.mAlpha = toFloat(paramObject);
  }
  
  private static class Loader {
    private static final int ANDROID_ALPHA = 9;
    
    private static final int ANDROID_ELEVATION = 10;
    
    private static final int ANDROID_ROTATION = 11;
    
    private static final int ANDROID_ROTATION_X = 12;
    
    private static final int ANDROID_ROTATION_Y = 13;
    
    private static final int ANDROID_SCALE_X = 15;
    
    private static final int ANDROID_SCALE_Y = 16;
    
    private static final int ANDROID_TRANSLATION_X = 17;
    
    private static final int ANDROID_TRANSLATION_Y = 18;
    
    private static final int ANDROID_TRANSLATION_Z = 19;
    
    private static final int CURVE_FIT = 4;
    
    private static final int FRAME_POSITION = 2;
    
    private static final int PROGRESS = 20;
    
    private static final int TARGET_ID = 1;
    
    private static final int TRANSITION_EASING = 3;
    
    private static final int TRANSITION_PATH_ROTATE = 14;
    
    private static final int WAVE_OFFSET = 7;
    
    private static final int WAVE_PERIOD = 6;
    
    private static final int WAVE_PHASE = 21;
    
    private static final int WAVE_SHAPE = 5;
    
    private static final int WAVE_VARIES_BY = 8;
    
    private static SparseIntArray mAttrMap;
    
    static {
      SparseIntArray sparseIntArray = new SparseIntArray();
      mAttrMap = sparseIntArray;
      sparseIntArray.append(R.styleable.KeyCycle_motionTarget, 1);
      mAttrMap.append(R.styleable.KeyCycle_framePosition, 2);
      mAttrMap.append(R.styleable.KeyCycle_transitionEasing, 3);
      mAttrMap.append(R.styleable.KeyCycle_curveFit, 4);
      mAttrMap.append(R.styleable.KeyCycle_waveShape, 5);
      mAttrMap.append(R.styleable.KeyCycle_wavePeriod, 6);
      mAttrMap.append(R.styleable.KeyCycle_waveOffset, 7);
      mAttrMap.append(R.styleable.KeyCycle_waveVariesBy, 8);
      mAttrMap.append(R.styleable.KeyCycle_android_alpha, 9);
      mAttrMap.append(R.styleable.KeyCycle_android_elevation, 10);
      mAttrMap.append(R.styleable.KeyCycle_android_rotation, 11);
      mAttrMap.append(R.styleable.KeyCycle_android_rotationX, 12);
      mAttrMap.append(R.styleable.KeyCycle_android_rotationY, 13);
      mAttrMap.append(R.styleable.KeyCycle_transitionPathRotate, 14);
      mAttrMap.append(R.styleable.KeyCycle_android_scaleX, 15);
      mAttrMap.append(R.styleable.KeyCycle_android_scaleY, 16);
      mAttrMap.append(R.styleable.KeyCycle_android_translationX, 17);
      mAttrMap.append(R.styleable.KeyCycle_android_translationY, 18);
      mAttrMap.append(R.styleable.KeyCycle_android_translationZ, 19);
      mAttrMap.append(R.styleable.KeyCycle_motionProgress, 20);
      mAttrMap.append(R.styleable.KeyCycle_wavePhase, 21);
    }
    
    private static void read(KeyCycle param1KeyCycle, TypedArray param1TypedArray) {
      int i = param1TypedArray.getIndexCount();
      for (byte b = 0; b < i; b++) {
        String str;
        int j = param1TypedArray.getIndex(b);
        switch (mAttrMap.get(j)) {
          default:
            str = Integer.toHexString(j);
            j = mAttrMap.get(j);
            Log.e("KeyCycle", (new StringBuilder(String.valueOf(str).length() + 33)).append("unused attribute 0x").append(str).append("   ").append(j).toString());
            break;
          case 21:
            KeyCycle.access$2002(param1KeyCycle, param1TypedArray.getFloat(j, param1KeyCycle.mWavePhase) / 360.0F);
            break;
          case 20:
            KeyCycle.access$1902(param1KeyCycle, param1TypedArray.getFloat(j, param1KeyCycle.mProgress));
            break;
          case 19:
            if (Build.VERSION.SDK_INT >= 21)
              KeyCycle.access$1802(param1KeyCycle, param1TypedArray.getDimension(j, param1KeyCycle.mTranslationZ)); 
            break;
          case 18:
            KeyCycle.access$1702(param1KeyCycle, param1TypedArray.getDimension(j, param1KeyCycle.mTranslationY));
            break;
          case 17:
            KeyCycle.access$1602(param1KeyCycle, param1TypedArray.getDimension(j, param1KeyCycle.mTranslationX));
            break;
          case 16:
            KeyCycle.access$1502(param1KeyCycle, param1TypedArray.getFloat(j, param1KeyCycle.mScaleY));
            break;
          case 15:
            KeyCycle.access$1402(param1KeyCycle, param1TypedArray.getFloat(j, param1KeyCycle.mScaleX));
            break;
          case 14:
            KeyCycle.access$1302(param1KeyCycle, param1TypedArray.getFloat(j, param1KeyCycle.mTransitionPathRotate));
            break;
          case 13:
            KeyCycle.access$1202(param1KeyCycle, param1TypedArray.getFloat(j, param1KeyCycle.mRotationY));
            break;
          case 12:
            KeyCycle.access$1102(param1KeyCycle, param1TypedArray.getFloat(j, param1KeyCycle.mRotationX));
            break;
          case 11:
            KeyCycle.access$1002(param1KeyCycle, param1TypedArray.getFloat(j, param1KeyCycle.mRotation));
            break;
          case 10:
            KeyCycle.access$902(param1KeyCycle, param1TypedArray.getDimension(j, param1KeyCycle.mElevation));
            break;
          case 9:
            KeyCycle.access$802(param1KeyCycle, param1TypedArray.getFloat(j, param1KeyCycle.mAlpha));
            break;
          case 8:
            KeyCycle.access$702(param1KeyCycle, param1TypedArray.getInt(j, param1KeyCycle.mWaveVariesBy));
            break;
          case 7:
            if ((param1TypedArray.peekValue(j)).type == 5) {
              KeyCycle.access$602(param1KeyCycle, param1TypedArray.getDimension(j, param1KeyCycle.mWaveOffset));
              break;
            } 
            KeyCycle.access$602(param1KeyCycle, param1TypedArray.getFloat(j, param1KeyCycle.mWaveOffset));
            break;
          case 6:
            KeyCycle.access$502(param1KeyCycle, param1TypedArray.getFloat(j, param1KeyCycle.mWavePeriod));
            break;
          case 5:
            if ((param1TypedArray.peekValue(j)).type == 3) {
              KeyCycle.access$302(param1KeyCycle, param1TypedArray.getString(j));
              KeyCycle.access$402(param1KeyCycle, 7);
              break;
            } 
            KeyCycle.access$402(param1KeyCycle, param1TypedArray.getInt(j, param1KeyCycle.mWaveShape));
            break;
          case 4:
            KeyCycle.access$202(param1KeyCycle, param1TypedArray.getInteger(j, param1KeyCycle.mCurveFit));
            break;
          case 3:
            KeyCycle.access$102(param1KeyCycle, param1TypedArray.getString(j));
            break;
          case 2:
            param1KeyCycle.mFramePosition = param1TypedArray.getInt(j, param1KeyCycle.mFramePosition);
            break;
          case 1:
            if (MotionLayout.IS_IN_EDIT_MODE) {
              param1KeyCycle.mTargetId = param1TypedArray.getResourceId(j, param1KeyCycle.mTargetId);
              if (param1KeyCycle.mTargetId == -1)
                param1KeyCycle.mTargetString = param1TypedArray.getString(j); 
              break;
            } 
            if ((param1TypedArray.peekValue(j)).type == 3) {
              param1KeyCycle.mTargetString = param1TypedArray.getString(j);
              break;
            } 
            param1KeyCycle.mTargetId = param1TypedArray.getResourceId(j, param1KeyCycle.mTargetId);
            break;
        } 
      } 
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\motion\widget\KeyCycle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */