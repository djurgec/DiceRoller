package androidx.constraintlayout.core.motion;

import androidx.constraintlayout.core.motion.key.MotionKey;
import androidx.constraintlayout.core.motion.key.MotionKeyCycle;
import androidx.constraintlayout.core.motion.key.MotionKeyPosition;
import androidx.constraintlayout.core.motion.key.MotionKeyTimeCycle;
import androidx.constraintlayout.core.motion.key.MotionKeyTrigger;
import androidx.constraintlayout.core.motion.utils.CurveFit;
import androidx.constraintlayout.core.motion.utils.DifferentialInterpolator;
import androidx.constraintlayout.core.motion.utils.Easing;
import androidx.constraintlayout.core.motion.utils.FloatRect;
import androidx.constraintlayout.core.motion.utils.KeyCache;
import androidx.constraintlayout.core.motion.utils.KeyCycleOscillator;
import androidx.constraintlayout.core.motion.utils.KeyFrameArray;
import androidx.constraintlayout.core.motion.utils.Rect;
import androidx.constraintlayout.core.motion.utils.SplineSet;
import androidx.constraintlayout.core.motion.utils.TimeCycleSplineSet;
import androidx.constraintlayout.core.motion.utils.Utils;
import androidx.constraintlayout.core.motion.utils.VelocityMatrix;
import androidx.constraintlayout.core.motion.utils.ViewState;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class Motion {
  static final int BOUNCE = 4;
  
  private static final boolean DEBUG = false;
  
  public static final int DRAW_PATH_AS_CONFIGURED = 4;
  
  public static final int DRAW_PATH_BASIC = 1;
  
  public static final int DRAW_PATH_CARTESIAN = 3;
  
  public static final int DRAW_PATH_NONE = 0;
  
  public static final int DRAW_PATH_RECTANGLE = 5;
  
  public static final int DRAW_PATH_RELATIVE = 2;
  
  public static final int DRAW_PATH_SCREEN = 6;
  
  static final int EASE_IN = 1;
  
  static final int EASE_IN_OUT = 0;
  
  static final int EASE_OUT = 2;
  
  private static final boolean FAVOR_FIXED_SIZE_VIEWS = false;
  
  public static final int HORIZONTAL_PATH_X = 2;
  
  public static final int HORIZONTAL_PATH_Y = 3;
  
  private static final int INTERPOLATOR_REFERENCE_ID = -2;
  
  private static final int INTERPOLATOR_UNDEFINED = -3;
  
  static final int LINEAR = 3;
  
  static final int OVERSHOOT = 5;
  
  public static final int PATH_PERCENT = 0;
  
  public static final int PATH_PERPENDICULAR = 1;
  
  public static final int ROTATION_LEFT = 2;
  
  public static final int ROTATION_RIGHT = 1;
  
  private static final int SPLINE_STRING = -1;
  
  private static final String TAG = "MotionController";
  
  public static final int VERTICAL_PATH_X = 4;
  
  public static final int VERTICAL_PATH_Y = 5;
  
  private int MAX_DIMENSION = 4;
  
  String[] attributeTable;
  
  private CurveFit mArcSpline;
  
  private int[] mAttributeInterpolatorCount;
  
  private String[] mAttributeNames;
  
  private HashMap<String, SplineSet> mAttributesMap;
  
  String mConstraintTag;
  
  float mCurrentCenterX;
  
  float mCurrentCenterY;
  
  private int mCurveFitType = -1;
  
  private HashMap<String, KeyCycleOscillator> mCycleMap;
  
  private MotionPaths mEndMotionPath = new MotionPaths();
  
  private MotionConstrainedPoint mEndPoint = new MotionConstrainedPoint();
  
  int mId;
  
  private double[] mInterpolateData;
  
  private int[] mInterpolateVariables;
  
  private double[] mInterpolateVelocity;
  
  private ArrayList<MotionKey> mKeyList = new ArrayList<>();
  
  private MotionKeyTrigger[] mKeyTriggers;
  
  private ArrayList<MotionPaths> mMotionPaths = new ArrayList<>();
  
  float mMotionStagger = Float.NaN;
  
  private boolean mNoMovement = false;
  
  private int mPathMotionArc = -1;
  
  private DifferentialInterpolator mQuantizeMotionInterpolator = null;
  
  private float mQuantizeMotionPhase = Float.NaN;
  
  private int mQuantizeMotionSteps = -1;
  
  private CurveFit[] mSpline;
  
  float mStaggerOffset = 0.0F;
  
  float mStaggerScale = 1.0F;
  
  private MotionPaths mStartMotionPath = new MotionPaths();
  
  private MotionConstrainedPoint mStartPoint = new MotionConstrainedPoint();
  
  Rect mTempRect = new Rect();
  
  private HashMap<String, TimeCycleSplineSet> mTimeCycleAttributesMap;
  
  private int mTransformPivotTarget = -1;
  
  private MotionWidget mTransformPivotView = null;
  
  private float[] mValuesBuff = new float[4];
  
  private float[] mVelocity = new float[1];
  
  MotionWidget mView;
  
  public Motion(MotionWidget paramMotionWidget) {
    setView(paramMotionWidget);
  }
  
  private float getAdjustedPosition(float paramFloat, float[] paramArrayOffloat) {
    float f1;
    if (paramArrayOffloat != null) {
      paramArrayOffloat[0] = 1.0F;
      f1 = paramFloat;
    } else {
      float f = this.mStaggerScale;
      f1 = paramFloat;
      if (f != 1.0D) {
        float f5 = this.mStaggerOffset;
        float f4 = paramFloat;
        if (paramFloat < f5)
          f4 = 0.0F; 
        f1 = f4;
        if (f4 > f5) {
          f1 = f4;
          if (f4 < 1.0D)
            f1 = Math.min((f4 - f5) * f, 1.0F); 
        } 
      } 
    } 
    float f3 = f1;
    Easing easing = this.mStartMotionPath.mKeyFrameEasing;
    float f2 = 0.0F;
    paramFloat = Float.NaN;
    for (MotionPaths motionPaths : this.mMotionPaths) {
      Easing easing1 = easing;
      float f4 = f2;
      float f5 = paramFloat;
      if (motionPaths.mKeyFrameEasing != null)
        if (motionPaths.time < f1) {
          easing1 = motionPaths.mKeyFrameEasing;
          f4 = motionPaths.time;
          f5 = paramFloat;
        } else {
          easing1 = easing;
          f4 = f2;
          f5 = paramFloat;
          if (Float.isNaN(paramFloat)) {
            f5 = motionPaths.time;
            f4 = f2;
            easing1 = easing;
          } 
        }  
      easing = easing1;
      f2 = f4;
      paramFloat = f5;
    } 
    if (easing != null) {
      f3 = paramFloat;
      if (Float.isNaN(paramFloat))
        f3 = 1.0F; 
      f1 = (f1 - f2) / (f3 - f2);
      paramFloat = (f3 - f2) * (float)easing.get(f1) + f2;
      f3 = paramFloat;
      if (paramArrayOffloat != null) {
        paramArrayOffloat[0] = (float)easing.getDiff(f1);
        f3 = paramFloat;
      } 
    } 
    return f3;
  }
  
  private static DifferentialInterpolator getInterpolator(int paramInt1, String paramString, int paramInt2) {
    switch (paramInt1) {
      default:
        return null;
      case -1:
        break;
    } 
    return new DifferentialInterpolator(Easing.getInterpolator(paramString)) {
        float mX;
        
        final Easing val$easing;
        
        public float getInterpolation(float param1Float) {
          this.mX = param1Float;
          return (float)easing.get(param1Float);
        }
        
        public float getVelocity() {
          return (float)easing.getDiff(this.mX);
        }
      };
  }
  
  private float getPreCycleDistance() {
    byte b1 = 100;
    float[] arrayOfFloat = new float[2];
    float f2 = 1.0F / (100 - 1);
    float f1 = 0.0F;
    double d1 = 0.0D;
    double d2 = 0.0D;
    byte b2 = 0;
    while (b2 < b1) {
      float f5 = b2 * f2;
      double d = f5;
      Easing easing = this.mStartMotionPath.mKeyFrameEasing;
      Iterator<MotionPaths> iterator = this.mMotionPaths.iterator();
      float f4 = 0.0F;
      float f3 = Float.NaN;
      while (iterator.hasNext()) {
        MotionPaths motionPaths = iterator.next();
        float f7 = f3;
        Easing easing1 = easing;
        float f6 = f4;
        if (motionPaths.mKeyFrameEasing != null)
          if (motionPaths.time < f5) {
            easing1 = motionPaths.mKeyFrameEasing;
            f6 = motionPaths.time;
            f7 = f3;
          } else {
            f7 = f3;
            easing1 = easing;
            f6 = f4;
            if (Float.isNaN(f3)) {
              f7 = motionPaths.time;
              f6 = f4;
              easing1 = easing;
            } 
          }  
        f3 = f7;
        easing = easing1;
        f4 = f6;
      } 
      if (easing != null) {
        float f = f3;
        if (Float.isNaN(f3))
          f = 1.0F; 
        d = ((f - f4) * (float)easing.get(((f5 - f4) / (f - f4))) + f4);
        f3 = f;
      } 
      this.mSpline[0].getPos(d, this.mInterpolateData);
      this.mStartMotionPath.getCenter(d, this.mInterpolateVariables, this.mInterpolateData, arrayOfFloat, 0);
      f3 = f1;
      if (b2 > 0)
        f3 = (float)(f1 + Math.hypot(d2 - arrayOfFloat[1], d1 - arrayOfFloat[0])); 
      d1 = arrayOfFloat[0];
      d2 = arrayOfFloat[1];
      b2++;
      f1 = f3;
    } 
    return f1;
  }
  
  private void insertKey(MotionPaths paramMotionPaths) {
    MotionPaths motionPaths = null;
    for (MotionPaths motionPaths1 : this.mMotionPaths) {
      if (paramMotionPaths.position == motionPaths1.position)
        motionPaths = motionPaths1; 
    } 
    if (motionPaths != null)
      this.mMotionPaths.remove(motionPaths); 
    int i = Collections.binarySearch((List)this.mMotionPaths, paramMotionPaths);
    if (i == 0)
      Utils.loge("MotionController", " KeyPath position \"" + paramMotionPaths.position + "\" outside of range"); 
    this.mMotionPaths.add(-i - 1, paramMotionPaths);
  }
  
  private void readView(MotionPaths paramMotionPaths) {
    paramMotionPaths.setBounds(this.mView.getX(), this.mView.getY(), this.mView.getWidth(), this.mView.getHeight());
  }
  
  public void addKey(MotionKey paramMotionKey) {
    this.mKeyList.add(paramMotionKey);
  }
  
  void addKeys(ArrayList<MotionKey> paramArrayList) {
    this.mKeyList.addAll(paramArrayList);
  }
  
  void buildBounds(float[] paramArrayOffloat, int paramInt) {
    float f = 1.0F / (paramInt - 1);
    HashMap<String, SplineSet> hashMap1 = this.mAttributesMap;
    if (hashMap1 == null) {
      hashMap1 = null;
    } else {
      SplineSet splineSet = hashMap1.get("translationX");
    } 
    HashMap<String, SplineSet> hashMap2 = this.mAttributesMap;
    if (hashMap2 != null)
      SplineSet splineSet = hashMap2.get("translationY"); 
    HashMap<String, KeyCycleOscillator> hashMap = this.mCycleMap;
    if (hashMap != null)
      KeyCycleOscillator keyCycleOscillator = hashMap.get("translationX"); 
    hashMap = this.mCycleMap;
    if (hashMap != null)
      KeyCycleOscillator keyCycleOscillator = hashMap.get("translationY"); 
    for (byte b = 0; b < paramInt; b++) {
      float f3 = b * f;
      float f4 = this.mStaggerScale;
      float f1 = f3;
      if (f4 != 1.0F) {
        float f6 = this.mStaggerOffset;
        float f5 = f3;
        if (f3 < f6)
          f5 = 0.0F; 
        f1 = f5;
        if (f5 > f6) {
          f1 = f5;
          if (f5 < 1.0D)
            f1 = Math.min((f5 - f6) * f4, 1.0F); 
        } 
      } 
      double d = f1;
      Easing easing = this.mStartMotionPath.mKeyFrameEasing;
      f3 = 0.0F;
      float f2 = Float.NaN;
      for (MotionPaths motionPaths : this.mMotionPaths) {
        Easing easing1 = easing;
        float f5 = f3;
        f4 = f2;
        if (motionPaths.mKeyFrameEasing != null)
          if (motionPaths.time < f1) {
            easing1 = motionPaths.mKeyFrameEasing;
            f5 = motionPaths.time;
            f4 = f2;
          } else {
            easing1 = easing;
            f5 = f3;
            f4 = f2;
            if (Float.isNaN(f2)) {
              f4 = motionPaths.time;
              f5 = f3;
              easing1 = easing;
            } 
          }  
        easing = easing1;
        f3 = f5;
        f2 = f4;
      } 
      if (easing != null) {
        float f5 = f2;
        if (Float.isNaN(f2))
          f5 = 1.0F; 
        d = ((f5 - f3) * (float)easing.get(((f1 - f3) / (f5 - f3))) + f3);
      } 
      this.mSpline[0].getPos(d, this.mInterpolateData);
      CurveFit curveFit = this.mArcSpline;
      if (curveFit != null) {
        double[] arrayOfDouble = this.mInterpolateData;
        if (arrayOfDouble.length > 0)
          curveFit.getPos(d, arrayOfDouble); 
      } 
      this.mStartMotionPath.getBounds(this.mInterpolateVariables, this.mInterpolateData, paramArrayOffloat, b * 2);
    } 
  }
  
  int buildKeyBounds(float[] paramArrayOffloat, int[] paramArrayOfint) {
    if (paramArrayOffloat != null) {
      byte b1 = 0;
      double[] arrayOfDouble = this.mSpline[0].getTimePoints();
      if (paramArrayOfint != null) {
        Iterator<MotionPaths> iterator = this.mMotionPaths.iterator();
        while (iterator.hasNext()) {
          paramArrayOfint[b1] = ((MotionPaths)iterator.next()).mMode;
          b1++;
        } 
      } 
      byte b2 = 0;
      for (b1 = 0; b1 < arrayOfDouble.length; b1++) {
        this.mSpline[0].getPos(arrayOfDouble[b1], this.mInterpolateData);
        this.mStartMotionPath.getBounds(this.mInterpolateVariables, this.mInterpolateData, paramArrayOffloat, b2);
        b2 += true;
      } 
      return b2 / 2;
    } 
    return 0;
  }
  
  public int buildKeyFrames(float[] paramArrayOffloat, int[] paramArrayOfint1, int[] paramArrayOfint2) {
    if (paramArrayOffloat != null) {
      byte b1 = 0;
      double[] arrayOfDouble = this.mSpline[0].getTimePoints();
      if (paramArrayOfint1 != null) {
        Iterator<MotionPaths> iterator = this.mMotionPaths.iterator();
        while (iterator.hasNext()) {
          paramArrayOfint1[b1] = ((MotionPaths)iterator.next()).mMode;
          b1++;
        } 
      } 
      b1 = 0;
      if (paramArrayOfint2 != null) {
        Iterator<MotionPaths> iterator = this.mMotionPaths.iterator();
        while (iterator.hasNext()) {
          paramArrayOfint2[b1] = (int)(((MotionPaths)iterator.next()).position * 100.0F);
          b1++;
        } 
      } 
      byte b2 = 0;
      for (b1 = 0; b1 < arrayOfDouble.length; b1++) {
        this.mSpline[0].getPos(arrayOfDouble[b1], this.mInterpolateData);
        this.mStartMotionPath.getCenter(arrayOfDouble[b1], this.mInterpolateVariables, this.mInterpolateData, paramArrayOffloat, b2);
        b2 += true;
      } 
      return b2 / 2;
    } 
    return 0;
  }
  
  public void buildPath(float[] paramArrayOffloat, int paramInt) {
    SplineSet splineSet1;
    SplineSet splineSet2;
    KeyCycleOscillator keyCycleOscillator1;
    float f = 1.0F / (paramInt - 1);
    HashMap<String, SplineSet> hashMap1 = this.mAttributesMap;
    KeyCycleOscillator keyCycleOscillator2 = null;
    if (hashMap1 == null) {
      hashMap1 = null;
    } else {
      splineSet1 = hashMap1.get("translationX");
    } 
    HashMap<String, SplineSet> hashMap2 = this.mAttributesMap;
    if (hashMap2 == null) {
      hashMap2 = null;
    } else {
      splineSet2 = hashMap2.get("translationY");
    } 
    HashMap<String, KeyCycleOscillator> hashMap3 = this.mCycleMap;
    if (hashMap3 == null) {
      hashMap3 = null;
    } else {
      keyCycleOscillator1 = hashMap3.get("translationX");
    } 
    HashMap<String, KeyCycleOscillator> hashMap4 = this.mCycleMap;
    if (hashMap4 != null)
      keyCycleOscillator2 = hashMap4.get("translationY"); 
    for (byte b = 0; b < paramInt; b++) {
      float f1;
      float f2 = b * f;
      float f3 = this.mStaggerScale;
      if (f3 != 1.0F) {
        float f4 = this.mStaggerOffset;
        f1 = f2;
        if (f2 < f4)
          f1 = 0.0F; 
        if (f1 > f4 && f1 < 1.0D)
          f1 = Math.min((f1 - f4) * f3, 1.0F); 
      } else {
        f1 = f2;
      } 
      double d = f1;
      Easing easing = this.mStartMotionPath.mKeyFrameEasing;
      Iterator<MotionPaths> iterator = this.mMotionPaths.iterator();
      f3 = 0.0F;
      f2 = Float.NaN;
      while (iterator.hasNext()) {
        MotionPaths motionPaths = iterator.next();
        float f5 = f2;
        Easing easing1 = easing;
        float f4 = f3;
        if (motionPaths.mKeyFrameEasing != null)
          if (motionPaths.time < f1) {
            easing1 = motionPaths.mKeyFrameEasing;
            f4 = motionPaths.time;
            f5 = f2;
          } else {
            f5 = f2;
            easing1 = easing;
            f4 = f3;
            if (Float.isNaN(f2)) {
              f5 = motionPaths.time;
              f4 = f3;
              easing1 = easing;
            } 
          }  
        f2 = f5;
        easing = easing1;
        f3 = f4;
      } 
      if (easing != null) {
        float f4 = f2;
        if (Float.isNaN(f2))
          f4 = 1.0F; 
        d = ((f4 - f3) * (float)easing.get(((f1 - f3) / (f4 - f3))) + f3);
      } 
      this.mSpline[0].getPos(d, this.mInterpolateData);
      CurveFit curveFit = this.mArcSpline;
      if (curveFit != null) {
        double[] arrayOfDouble = this.mInterpolateData;
        if (arrayOfDouble.length > 0)
          curveFit.getPos(d, arrayOfDouble); 
      } 
      this.mStartMotionPath.getCenter(d, this.mInterpolateVariables, this.mInterpolateData, paramArrayOffloat, b * 2);
      if (keyCycleOscillator1 != null) {
        int i = b * 2;
        paramArrayOffloat[i] = paramArrayOffloat[i] + keyCycleOscillator1.get(f1);
      } else if (splineSet1 != null) {
        int i = b * 2;
        paramArrayOffloat[i] = paramArrayOffloat[i] + splineSet1.get(f1);
      } 
      if (keyCycleOscillator2 != null) {
        int i = b * 2 + 1;
        paramArrayOffloat[i] = paramArrayOffloat[i] + keyCycleOscillator2.get(f1);
      } else if (splineSet2 != null) {
        int i = b * 2 + 1;
        paramArrayOffloat[i] = paramArrayOffloat[i] + splineSet2.get(f1);
      } 
    } 
  }
  
  public void buildRect(float paramFloat, float[] paramArrayOffloat, int paramInt) {
    paramFloat = getAdjustedPosition(paramFloat, null);
    this.mSpline[0].getPos(paramFloat, this.mInterpolateData);
    this.mStartMotionPath.getRect(this.mInterpolateVariables, this.mInterpolateData, paramArrayOffloat, paramInt);
  }
  
  void buildRectangles(float[] paramArrayOffloat, int paramInt) {
    float f = 1.0F / (paramInt - 1);
    for (byte b = 0; b < paramInt; b++) {
      float f1 = getAdjustedPosition(b * f, null);
      this.mSpline[0].getPos(f1, this.mInterpolateData);
      this.mStartMotionPath.getRect(this.mInterpolateVariables, this.mInterpolateData, paramArrayOffloat, b * 8);
    } 
  }
  
  void endTrigger(boolean paramBoolean) {}
  
  public int getAnimateRelativeTo() {
    return this.mStartMotionPath.mAnimateRelativeTo;
  }
  
  int getAttributeValues(String paramString, float[] paramArrayOffloat, int paramInt) {
    float f = 1.0F / (paramInt - 1);
    SplineSet splineSet = this.mAttributesMap.get(paramString);
    if (splineSet == null)
      return -1; 
    for (paramInt = 0; paramInt < paramArrayOffloat.length; paramInt++)
      paramArrayOffloat[paramInt] = splineSet.get((paramInt / (paramArrayOffloat.length - 1))); 
    return paramArrayOffloat.length;
  }
  
  public void getCenter(double paramDouble, float[] paramArrayOffloat1, float[] paramArrayOffloat2) {
    double[] arrayOfDouble1 = new double[4];
    double[] arrayOfDouble2 = new double[4];
    int[] arrayOfInt = new int[4];
    this.mSpline[0].getPos(paramDouble, arrayOfDouble1);
    this.mSpline[0].getSlope(paramDouble, arrayOfDouble2);
    Arrays.fill(paramArrayOffloat2, 0.0F);
    this.mStartMotionPath.getCenter(paramDouble, this.mInterpolateVariables, arrayOfDouble1, paramArrayOffloat1, arrayOfDouble2, paramArrayOffloat2);
  }
  
  public float getCenterX() {
    return this.mCurrentCenterX;
  }
  
  public float getCenterY() {
    return this.mCurrentCenterY;
  }
  
  void getDpDt(float paramFloat1, float paramFloat2, float paramFloat3, float[] paramArrayOffloat) {
    float f1 = getAdjustedPosition(paramFloat1, this.mVelocity);
    CurveFit[] arrayOfCurveFit = this.mSpline;
    if (arrayOfCurveFit != null) {
      arrayOfCurveFit[0].getSlope(f1, this.mInterpolateVelocity);
      this.mSpline[0].getPos(f1, this.mInterpolateData);
      paramFloat1 = this.mVelocity[0];
      byte b = 0;
      while (true) {
        double[] arrayOfDouble = this.mInterpolateVelocity;
        if (b < arrayOfDouble.length) {
          arrayOfDouble[b] = arrayOfDouble[b] * paramFloat1;
          b++;
          continue;
        } 
        CurveFit curveFit = this.mArcSpline;
        if (curveFit != null) {
          arrayOfDouble = this.mInterpolateData;
          if (arrayOfDouble.length > 0) {
            curveFit.getPos(f1, arrayOfDouble);
            this.mArcSpline.getSlope(f1, this.mInterpolateVelocity);
            this.mStartMotionPath.setDpDt(paramFloat2, paramFloat3, paramArrayOffloat, this.mInterpolateVariables, this.mInterpolateVelocity, this.mInterpolateData);
          } 
          return;
        } 
        this.mStartMotionPath.setDpDt(paramFloat2, paramFloat3, paramArrayOffloat, this.mInterpolateVariables, arrayOfDouble, this.mInterpolateData);
        return;
      } 
    } 
    float f4 = this.mEndMotionPath.x - this.mStartMotionPath.x;
    f1 = this.mEndMotionPath.y - this.mStartMotionPath.y;
    float f5 = this.mEndMotionPath.width;
    float f2 = this.mStartMotionPath.width;
    float f3 = this.mEndMotionPath.height;
    paramFloat1 = this.mStartMotionPath.height;
    paramArrayOffloat[0] = (1.0F - paramFloat2) * f4 + (f4 + f5 - f2) * paramFloat2;
    paramArrayOffloat[1] = (1.0F - paramFloat3) * f1 + (f1 + f3 - paramFloat1) * paramFloat3;
  }
  
  public int getDrawPath() {
    int i = this.mStartMotionPath.mDrawPath;
    Iterator<MotionPaths> iterator = this.mMotionPaths.iterator();
    while (iterator.hasNext())
      i = Math.max(i, ((MotionPaths)iterator.next()).mDrawPath); 
    return Math.max(i, this.mEndMotionPath.mDrawPath);
  }
  
  public float getFinalHeight() {
    return this.mEndMotionPath.height;
  }
  
  public float getFinalWidth() {
    return this.mEndMotionPath.width;
  }
  
  public float getFinalX() {
    return this.mEndMotionPath.x;
  }
  
  public float getFinalY() {
    return this.mEndMotionPath.y;
  }
  
  public MotionPaths getKeyFrame(int paramInt) {
    return this.mMotionPaths.get(paramInt);
  }
  
  public int getKeyFrameInfo(int paramInt, int[] paramArrayOfint) {
    byte b = 0;
    int i = 0;
    float[] arrayOfFloat = new float[2];
    Iterator<MotionKey> iterator = this.mKeyList.iterator();
    while (true) {
      int j = i;
      if (iterator.hasNext()) {
        MotionKey motionKey = iterator.next();
        if (motionKey.mType != paramInt && paramInt == -1) {
          i = j;
          continue;
        } 
        paramArrayOfint[j] = 0;
        i = j + 1;
        paramArrayOfint[i] = motionKey.mType;
        paramArrayOfint[++i] = motionKey.mFramePosition;
        float f = motionKey.mFramePosition / 100.0F;
        this.mSpline[0].getPos(f, this.mInterpolateData);
        this.mStartMotionPath.getCenter(f, this.mInterpolateVariables, this.mInterpolateData, arrayOfFloat, 0);
        paramArrayOfint[++i] = Float.floatToIntBits(arrayOfFloat[0]);
        int k = i + 1;
        paramArrayOfint[k] = Float.floatToIntBits(arrayOfFloat[1]);
        i = k;
        if (motionKey instanceof MotionKeyPosition) {
          MotionKeyPosition motionKeyPosition = (MotionKeyPosition)motionKey;
          i = k + 1;
          paramArrayOfint[i] = motionKeyPosition.mPositionType;
          paramArrayOfint[++i] = Float.floatToIntBits(motionKeyPosition.mPercentX);
          paramArrayOfint[++i] = Float.floatToIntBits(motionKeyPosition.mPercentY);
        } 
        paramArrayOfint[j] = ++i - j;
        b++;
        continue;
      } 
      return b;
    } 
  }
  
  float getKeyFrameParameter(int paramInt, float paramFloat1, float paramFloat2) {
    float f1 = this.mEndMotionPath.x - this.mStartMotionPath.x;
    float f3 = this.mEndMotionPath.y - this.mStartMotionPath.y;
    float f6 = this.mStartMotionPath.x;
    float f7 = this.mStartMotionPath.width / 2.0F;
    float f5 = this.mStartMotionPath.y;
    float f4 = this.mStartMotionPath.height / 2.0F;
    float f2 = (float)Math.hypot(f1, f3);
    if (f2 < 1.0E-7D)
      return Float.NaN; 
    paramFloat1 -= f6 + f7;
    paramFloat2 -= f5 + f4;
    if ((float)Math.hypot(paramFloat1, paramFloat2) == 0.0F)
      return 0.0F; 
    f4 = paramFloat1 * f1 + paramFloat2 * f3;
    switch (paramInt) {
      default:
        return 0.0F;
      case 5:
        return paramFloat2 / f3;
      case 4:
        return paramFloat1 / f3;
      case 3:
        return paramFloat2 / f1;
      case 2:
        return paramFloat1 / f1;
      case 1:
        return (float)Math.sqrt((f2 * f2 - f4 * f4));
      case 0:
        break;
    } 
    return f4 / f2;
  }
  
  public int getKeyFramePositions(int[] paramArrayOfint, float[] paramArrayOffloat) {
    byte b = 0;
    boolean bool = false;
    for (MotionKey motionKey : this.mKeyList) {
      paramArrayOfint[b] = motionKey.mFramePosition + motionKey.mType * 1000;
      float f = motionKey.mFramePosition / 100.0F;
      this.mSpline[0].getPos(f, this.mInterpolateData);
      this.mStartMotionPath.getCenter(f, this.mInterpolateVariables, this.mInterpolateData, paramArrayOffloat, bool);
      bool += true;
      b++;
    } 
    return b;
  }
  
  double[] getPos(double paramDouble) {
    this.mSpline[0].getPos(paramDouble, this.mInterpolateData);
    CurveFit curveFit = this.mArcSpline;
    if (curveFit != null) {
      double[] arrayOfDouble = this.mInterpolateData;
      if (arrayOfDouble.length > 0)
        curveFit.getPos(paramDouble, arrayOfDouble); 
    } 
    return this.mInterpolateData;
  }
  
  MotionKeyPosition getPositionKeyframe(int paramInt1, int paramInt2, float paramFloat1, float paramFloat2) {
    FloatRect floatRect1 = new FloatRect();
    floatRect1.left = this.mStartMotionPath.x;
    floatRect1.top = this.mStartMotionPath.y;
    floatRect1.right = floatRect1.left + this.mStartMotionPath.width;
    floatRect1.bottom = floatRect1.top + this.mStartMotionPath.height;
    FloatRect floatRect2 = new FloatRect();
    floatRect2.left = this.mEndMotionPath.x;
    floatRect2.top = this.mEndMotionPath.y;
    floatRect2.right = floatRect2.left + this.mEndMotionPath.width;
    floatRect2.bottom = floatRect2.top + this.mEndMotionPath.height;
    for (MotionKey motionKey : this.mKeyList) {
      if (motionKey instanceof MotionKeyPosition && ((MotionKeyPosition)motionKey).intersects(paramInt1, paramInt2, floatRect1, floatRect2, paramFloat1, paramFloat2))
        return (MotionKeyPosition)motionKey; 
    } 
    return null;
  }
  
  void getPostLayoutDvDp(float paramFloat1, int paramInt1, int paramInt2, float paramFloat2, float paramFloat3, float[] paramArrayOffloat) {
    SplineSet splineSet1;
    double[] arrayOfDouble;
    SplineSet splineSet2;
    SplineSet splineSet3;
    SplineSet splineSet4;
    SplineSet splineSet5;
    KeyCycleOscillator keyCycleOscillator1;
    KeyCycleOscillator keyCycleOscillator2;
    KeyCycleOscillator keyCycleOscillator3;
    KeyCycleOscillator keyCycleOscillator4;
    float f4 = getAdjustedPosition(paramFloat1, this.mVelocity);
    HashMap<String, SplineSet> hashMap1 = this.mAttributesMap;
    KeyCycleOscillator keyCycleOscillator5 = null;
    if (hashMap1 == null) {
      hashMap1 = null;
    } else {
      splineSet1 = hashMap1.get("translationX");
    } 
    HashMap<String, SplineSet> hashMap2 = this.mAttributesMap;
    if (hashMap2 == null) {
      hashMap2 = null;
    } else {
      splineSet2 = hashMap2.get("translationY");
    } 
    HashMap<String, SplineSet> hashMap3 = this.mAttributesMap;
    if (hashMap3 == null) {
      hashMap3 = null;
    } else {
      splineSet3 = hashMap3.get("rotationZ");
    } 
    HashMap<String, SplineSet> hashMap4 = this.mAttributesMap;
    if (hashMap4 == null) {
      hashMap4 = null;
    } else {
      splineSet4 = hashMap4.get("scaleX");
    } 
    HashMap<String, SplineSet> hashMap5 = this.mAttributesMap;
    if (hashMap5 == null) {
      hashMap5 = null;
    } else {
      splineSet5 = hashMap5.get("scaleY");
    } 
    HashMap<String, KeyCycleOscillator> hashMap6 = this.mCycleMap;
    if (hashMap6 == null) {
      hashMap6 = null;
    } else {
      keyCycleOscillator1 = hashMap6.get("translationX");
    } 
    HashMap<String, KeyCycleOscillator> hashMap7 = this.mCycleMap;
    if (hashMap7 == null) {
      hashMap7 = null;
    } else {
      keyCycleOscillator2 = hashMap7.get("translationY");
    } 
    HashMap<String, KeyCycleOscillator> hashMap8 = this.mCycleMap;
    if (hashMap8 == null) {
      hashMap8 = null;
    } else {
      keyCycleOscillator3 = hashMap8.get("rotationZ");
    } 
    HashMap<String, KeyCycleOscillator> hashMap9 = this.mCycleMap;
    if (hashMap9 == null) {
      hashMap9 = null;
    } else {
      keyCycleOscillator4 = hashMap9.get("scaleX");
    } 
    HashMap<String, KeyCycleOscillator> hashMap10 = this.mCycleMap;
    if (hashMap10 != null)
      keyCycleOscillator5 = hashMap10.get("scaleY"); 
    VelocityMatrix velocityMatrix = new VelocityMatrix();
    velocityMatrix.clear();
    velocityMatrix.setRotationVelocity(splineSet3, f4);
    velocityMatrix.setTranslationVelocity(splineSet1, splineSet2, f4);
    velocityMatrix.setScaleVelocity(splineSet4, splineSet5, f4);
    velocityMatrix.setRotationVelocity(keyCycleOscillator3, f4);
    velocityMatrix.setTranslationVelocity(keyCycleOscillator1, keyCycleOscillator2, f4);
    velocityMatrix.setScaleVelocity(keyCycleOscillator4, keyCycleOscillator5, f4);
    CurveFit curveFit = this.mArcSpline;
    if (curveFit != null) {
      arrayOfDouble = this.mInterpolateData;
      if (arrayOfDouble.length > 0) {
        curveFit.getPos(f4, arrayOfDouble);
        this.mArcSpline.getSlope(f4, this.mInterpolateVelocity);
        this.mStartMotionPath.setDpDt(paramFloat2, paramFloat3, paramArrayOffloat, this.mInterpolateVariables, this.mInterpolateVelocity, this.mInterpolateData);
      } 
      velocityMatrix.applyTransform(paramFloat2, paramFloat3, paramInt1, paramInt2, paramArrayOffloat);
      return;
    } 
    if (this.mSpline != null) {
      paramFloat1 = getAdjustedPosition(f4, this.mVelocity);
      this.mSpline[0].getSlope(paramFloat1, this.mInterpolateVelocity);
      this.mSpline[0].getPos(paramFloat1, this.mInterpolateData);
      paramFloat1 = this.mVelocity[0];
      byte b = 0;
      while (true) {
        arrayOfDouble = this.mInterpolateVelocity;
        if (b < arrayOfDouble.length) {
          arrayOfDouble[b] = arrayOfDouble[b] * paramFloat1;
          b++;
          continue;
        } 
        this.mStartMotionPath.setDpDt(paramFloat2, paramFloat3, paramArrayOffloat, this.mInterpolateVariables, arrayOfDouble, this.mInterpolateData);
        velocityMatrix.applyTransform(paramFloat2, paramFloat3, paramInt1, paramInt2, paramArrayOffloat);
        return;
      } 
    } 
    float f1 = this.mEndMotionPath.x - this.mStartMotionPath.x;
    float f5 = this.mEndMotionPath.y - this.mStartMotionPath.y;
    float f6 = this.mEndMotionPath.width;
    float f2 = this.mStartMotionPath.width;
    paramFloat1 = this.mEndMotionPath.height;
    float f3 = this.mStartMotionPath.height;
    paramArrayOffloat[0] = (1.0F - paramFloat2) * f1 + (f1 + f6 - f2) * paramFloat2;
    paramArrayOffloat[1] = (1.0F - paramFloat3) * f5 + (f5 + paramFloat1 - f3) * paramFloat3;
    velocityMatrix.clear();
    velocityMatrix.setRotationVelocity(splineSet3, f4);
    velocityMatrix.setTranslationVelocity((SplineSet)arrayOfDouble, splineSet2, f4);
    velocityMatrix.setScaleVelocity(splineSet4, splineSet5, f4);
    velocityMatrix.setRotationVelocity(keyCycleOscillator3, f4);
    velocityMatrix.setTranslationVelocity(keyCycleOscillator1, keyCycleOscillator2, f4);
    velocityMatrix.setScaleVelocity(keyCycleOscillator4, keyCycleOscillator5, f4);
    velocityMatrix.applyTransform(paramFloat2, paramFloat3, paramInt1, paramInt2, paramArrayOffloat);
  }
  
  public float getStartHeight() {
    return this.mStartMotionPath.height;
  }
  
  public float getStartWidth() {
    return this.mStartMotionPath.width;
  }
  
  public float getStartX() {
    return this.mStartMotionPath.x;
  }
  
  public float getStartY() {
    return this.mStartMotionPath.y;
  }
  
  public int getTransformPivotTarget() {
    return this.mTransformPivotTarget;
  }
  
  public MotionWidget getView() {
    return this.mView;
  }
  
  public boolean interpolate(MotionWidget paramMotionWidget, float paramFloat, long paramLong, KeyCache paramKeyCache) {
    // Byte code:
    //   0: aload_0
    //   1: fload_2
    //   2: aconst_null
    //   3: invokespecial getAdjustedPosition : (F[F)F
    //   6: fstore_2
    //   7: aload_0
    //   8: getfield mQuantizeMotionSteps : I
    //   11: istore #14
    //   13: iload #14
    //   15: iconst_m1
    //   16: if_icmpeq -> 122
    //   19: fconst_1
    //   20: iload #14
    //   22: i2f
    //   23: fdiv
    //   24: fstore #7
    //   26: fload_2
    //   27: fload #7
    //   29: fdiv
    //   30: f2d
    //   31: invokestatic floor : (D)D
    //   34: d2f
    //   35: fstore #8
    //   37: fload_2
    //   38: fload #7
    //   40: frem
    //   41: fload #7
    //   43: fdiv
    //   44: fstore #6
    //   46: fload #6
    //   48: fstore_2
    //   49: aload_0
    //   50: getfield mQuantizeMotionPhase : F
    //   53: invokestatic isNaN : (F)Z
    //   56: ifne -> 69
    //   59: aload_0
    //   60: getfield mQuantizeMotionPhase : F
    //   63: fload #6
    //   65: fadd
    //   66: fconst_1
    //   67: frem
    //   68: fstore_2
    //   69: aload_0
    //   70: getfield mQuantizeMotionInterpolator : Landroidx/constraintlayout/core/motion/utils/DifferentialInterpolator;
    //   73: astore #5
    //   75: aload #5
    //   77: ifnull -> 92
    //   80: aload #5
    //   82: fload_2
    //   83: invokeinterface getInterpolation : (F)F
    //   88: fstore_2
    //   89: goto -> 108
    //   92: fload_2
    //   93: f2d
    //   94: ldc2_w 0.5
    //   97: dcmpl
    //   98: ifle -> 106
    //   101: fconst_1
    //   102: fstore_2
    //   103: goto -> 108
    //   106: fconst_0
    //   107: fstore_2
    //   108: fload_2
    //   109: fload #7
    //   111: fmul
    //   112: fload #8
    //   114: fload #7
    //   116: fmul
    //   117: fadd
    //   118: fstore_2
    //   119: goto -> 122
    //   122: aload_0
    //   123: getfield mAttributesMap : Ljava/util/HashMap;
    //   126: astore #5
    //   128: aload #5
    //   130: ifnull -> 173
    //   133: aload #5
    //   135: invokevirtual values : ()Ljava/util/Collection;
    //   138: invokeinterface iterator : ()Ljava/util/Iterator;
    //   143: astore #5
    //   145: aload #5
    //   147: invokeinterface hasNext : ()Z
    //   152: ifeq -> 173
    //   155: aload #5
    //   157: invokeinterface next : ()Ljava/lang/Object;
    //   162: checkcast androidx/constraintlayout/core/motion/utils/SplineSet
    //   165: aload_1
    //   166: fload_2
    //   167: invokevirtual setProperty : (Landroidx/constraintlayout/core/motion/utils/TypedValues;F)V
    //   170: goto -> 145
    //   173: aload_0
    //   174: getfield mSpline : [Landroidx/constraintlayout/core/motion/utils/CurveFit;
    //   177: astore #5
    //   179: aload #5
    //   181: ifnull -> 603
    //   184: aload #5
    //   186: iconst_0
    //   187: aaload
    //   188: fload_2
    //   189: f2d
    //   190: aload_0
    //   191: getfield mInterpolateData : [D
    //   194: invokevirtual getPos : (D[D)V
    //   197: aload_0
    //   198: getfield mSpline : [Landroidx/constraintlayout/core/motion/utils/CurveFit;
    //   201: iconst_0
    //   202: aaload
    //   203: fload_2
    //   204: f2d
    //   205: aload_0
    //   206: getfield mInterpolateVelocity : [D
    //   209: invokevirtual getSlope : (D[D)V
    //   212: aload_0
    //   213: getfield mArcSpline : Landroidx/constraintlayout/core/motion/utils/CurveFit;
    //   216: astore #18
    //   218: aload #18
    //   220: ifnull -> 257
    //   223: aload_0
    //   224: getfield mInterpolateData : [D
    //   227: astore #5
    //   229: aload #5
    //   231: arraylength
    //   232: ifle -> 257
    //   235: aload #18
    //   237: fload_2
    //   238: f2d
    //   239: aload #5
    //   241: invokevirtual getPos : (D[D)V
    //   244: aload_0
    //   245: getfield mArcSpline : Landroidx/constraintlayout/core/motion/utils/CurveFit;
    //   248: fload_2
    //   249: f2d
    //   250: aload_0
    //   251: getfield mInterpolateVelocity : [D
    //   254: invokevirtual getSlope : (D[D)V
    //   257: aload_0
    //   258: getfield mNoMovement : Z
    //   261: ifne -> 286
    //   264: aload_0
    //   265: getfield mStartMotionPath : Landroidx/constraintlayout/core/motion/MotionPaths;
    //   268: fload_2
    //   269: aload_1
    //   270: aload_0
    //   271: getfield mInterpolateVariables : [I
    //   274: aload_0
    //   275: getfield mInterpolateData : [D
    //   278: aload_0
    //   279: getfield mInterpolateVelocity : [D
    //   282: aconst_null
    //   283: invokevirtual setView : (FLandroidx/constraintlayout/core/motion/MotionWidget;[I[D[D[D)V
    //   286: aload_0
    //   287: getfield mTransformPivotTarget : I
    //   290: iconst_m1
    //   291: if_icmpeq -> 421
    //   294: aload_0
    //   295: getfield mTransformPivotView : Landroidx/constraintlayout/core/motion/MotionWidget;
    //   298: ifnonnull -> 316
    //   301: aload_0
    //   302: aload_1
    //   303: invokevirtual getParent : ()Landroidx/constraintlayout/core/motion/MotionWidget;
    //   306: aload_0
    //   307: getfield mTransformPivotTarget : I
    //   310: invokevirtual findViewById : (I)Landroidx/constraintlayout/core/motion/MotionWidget;
    //   313: putfield mTransformPivotView : Landroidx/constraintlayout/core/motion/MotionWidget;
    //   316: aload_0
    //   317: getfield mTransformPivotView : Landroidx/constraintlayout/core/motion/MotionWidget;
    //   320: astore #5
    //   322: aload #5
    //   324: ifnull -> 421
    //   327: aload #5
    //   329: invokevirtual getTop : ()I
    //   332: aload_0
    //   333: getfield mTransformPivotView : Landroidx/constraintlayout/core/motion/MotionWidget;
    //   336: invokevirtual getBottom : ()I
    //   339: iadd
    //   340: i2f
    //   341: fconst_2
    //   342: fdiv
    //   343: fstore #8
    //   345: aload_0
    //   346: getfield mTransformPivotView : Landroidx/constraintlayout/core/motion/MotionWidget;
    //   349: invokevirtual getLeft : ()I
    //   352: aload_0
    //   353: getfield mTransformPivotView : Landroidx/constraintlayout/core/motion/MotionWidget;
    //   356: invokevirtual getRight : ()I
    //   359: iadd
    //   360: i2f
    //   361: fconst_2
    //   362: fdiv
    //   363: fstore #6
    //   365: aload_1
    //   366: invokevirtual getRight : ()I
    //   369: aload_1
    //   370: invokevirtual getLeft : ()I
    //   373: isub
    //   374: ifle -> 421
    //   377: aload_1
    //   378: invokevirtual getBottom : ()I
    //   381: aload_1
    //   382: invokevirtual getTop : ()I
    //   385: isub
    //   386: ifle -> 421
    //   389: aload_1
    //   390: invokevirtual getLeft : ()I
    //   393: i2f
    //   394: fstore #9
    //   396: aload_1
    //   397: invokevirtual getTop : ()I
    //   400: i2f
    //   401: fstore #7
    //   403: aload_1
    //   404: fload #6
    //   406: fload #9
    //   408: fsub
    //   409: invokevirtual setPivotX : (F)V
    //   412: aload_1
    //   413: fload #8
    //   415: fload #7
    //   417: fsub
    //   418: invokevirtual setPivotY : (F)V
    //   421: iconst_1
    //   422: istore #14
    //   424: aload_0
    //   425: getfield mSpline : [Landroidx/constraintlayout/core/motion/utils/CurveFit;
    //   428: astore #5
    //   430: iload #14
    //   432: aload #5
    //   434: arraylength
    //   435: if_icmpge -> 488
    //   438: aload #5
    //   440: iload #14
    //   442: aaload
    //   443: fload_2
    //   444: f2d
    //   445: aload_0
    //   446: getfield mValuesBuff : [F
    //   449: invokevirtual getPos : (D[F)V
    //   452: aload_0
    //   453: getfield mStartMotionPath : Landroidx/constraintlayout/core/motion/MotionPaths;
    //   456: getfield customAttributes : Ljava/util/HashMap;
    //   459: aload_0
    //   460: getfield mAttributeNames : [Ljava/lang/String;
    //   463: iload #14
    //   465: iconst_1
    //   466: isub
    //   467: aaload
    //   468: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   471: checkcast androidx/constraintlayout/core/motion/CustomVariable
    //   474: aload_1
    //   475: aload_0
    //   476: getfield mValuesBuff : [F
    //   479: invokevirtual setInterpolatedValue : (Landroidx/constraintlayout/core/motion/MotionWidget;[F)V
    //   482: iinc #14, 1
    //   485: goto -> 424
    //   488: aload_0
    //   489: getfield mStartPoint : Landroidx/constraintlayout/core/motion/MotionConstrainedPoint;
    //   492: getfield mVisibilityMode : I
    //   495: ifne -> 560
    //   498: fload_2
    //   499: fconst_0
    //   500: fcmpg
    //   501: ifgt -> 518
    //   504: aload_1
    //   505: aload_0
    //   506: getfield mStartPoint : Landroidx/constraintlayout/core/motion/MotionConstrainedPoint;
    //   509: getfield visibility : I
    //   512: invokevirtual setVisibility : (I)V
    //   515: goto -> 560
    //   518: fload_2
    //   519: fconst_1
    //   520: fcmpl
    //   521: iflt -> 538
    //   524: aload_1
    //   525: aload_0
    //   526: getfield mEndPoint : Landroidx/constraintlayout/core/motion/MotionConstrainedPoint;
    //   529: getfield visibility : I
    //   532: invokevirtual setVisibility : (I)V
    //   535: goto -> 560
    //   538: aload_0
    //   539: getfield mEndPoint : Landroidx/constraintlayout/core/motion/MotionConstrainedPoint;
    //   542: getfield visibility : I
    //   545: aload_0
    //   546: getfield mStartPoint : Landroidx/constraintlayout/core/motion/MotionConstrainedPoint;
    //   549: getfield visibility : I
    //   552: if_icmpeq -> 560
    //   555: aload_1
    //   556: iconst_4
    //   557: invokevirtual setVisibility : (I)V
    //   560: aload_0
    //   561: getfield mKeyTriggers : [Landroidx/constraintlayout/core/motion/key/MotionKeyTrigger;
    //   564: ifnull -> 781
    //   567: iconst_0
    //   568: istore #14
    //   570: aload_0
    //   571: getfield mKeyTriggers : [Landroidx/constraintlayout/core/motion/key/MotionKeyTrigger;
    //   574: astore #5
    //   576: iload #14
    //   578: aload #5
    //   580: arraylength
    //   581: if_icmpge -> 600
    //   584: aload #5
    //   586: iload #14
    //   588: aaload
    //   589: fload_2
    //   590: aload_1
    //   591: invokevirtual conditionallyFire : (FLandroidx/constraintlayout/core/motion/MotionWidget;)V
    //   594: iinc #14, 1
    //   597: goto -> 570
    //   600: goto -> 781
    //   603: aload_0
    //   604: getfield mStartMotionPath : Landroidx/constraintlayout/core/motion/MotionPaths;
    //   607: getfield x : F
    //   610: aload_0
    //   611: getfield mEndMotionPath : Landroidx/constraintlayout/core/motion/MotionPaths;
    //   614: getfield x : F
    //   617: aload_0
    //   618: getfield mStartMotionPath : Landroidx/constraintlayout/core/motion/MotionPaths;
    //   621: getfield x : F
    //   624: fsub
    //   625: fload_2
    //   626: fmul
    //   627: fadd
    //   628: fstore #10
    //   630: aload_0
    //   631: getfield mStartMotionPath : Landroidx/constraintlayout/core/motion/MotionPaths;
    //   634: getfield y : F
    //   637: aload_0
    //   638: getfield mEndMotionPath : Landroidx/constraintlayout/core/motion/MotionPaths;
    //   641: getfield y : F
    //   644: aload_0
    //   645: getfield mStartMotionPath : Landroidx/constraintlayout/core/motion/MotionPaths;
    //   648: getfield y : F
    //   651: fsub
    //   652: fload_2
    //   653: fmul
    //   654: fadd
    //   655: fstore #13
    //   657: aload_0
    //   658: getfield mStartMotionPath : Landroidx/constraintlayout/core/motion/MotionPaths;
    //   661: getfield width : F
    //   664: fstore #9
    //   666: aload_0
    //   667: getfield mEndMotionPath : Landroidx/constraintlayout/core/motion/MotionPaths;
    //   670: getfield width : F
    //   673: fstore #11
    //   675: aload_0
    //   676: getfield mStartMotionPath : Landroidx/constraintlayout/core/motion/MotionPaths;
    //   679: getfield width : F
    //   682: fstore #8
    //   684: aload_0
    //   685: getfield mStartMotionPath : Landroidx/constraintlayout/core/motion/MotionPaths;
    //   688: getfield height : F
    //   691: fstore #12
    //   693: aload_0
    //   694: getfield mEndMotionPath : Landroidx/constraintlayout/core/motion/MotionPaths;
    //   697: getfield height : F
    //   700: fstore #6
    //   702: aload_0
    //   703: getfield mStartMotionPath : Landroidx/constraintlayout/core/motion/MotionPaths;
    //   706: getfield height : F
    //   709: fstore #7
    //   711: fload #10
    //   713: ldc_w 0.5
    //   716: fadd
    //   717: f2i
    //   718: istore #15
    //   720: fload #13
    //   722: ldc_w 0.5
    //   725: fadd
    //   726: f2i
    //   727: istore #16
    //   729: fload #10
    //   731: ldc_w 0.5
    //   734: fadd
    //   735: fload #9
    //   737: fload #11
    //   739: fload #8
    //   741: fsub
    //   742: fload_2
    //   743: fmul
    //   744: fadd
    //   745: fadd
    //   746: f2i
    //   747: istore #17
    //   749: ldc_w 0.5
    //   752: fload #13
    //   754: fadd
    //   755: fload #12
    //   757: fload #6
    //   759: fload #7
    //   761: fsub
    //   762: fload_2
    //   763: fmul
    //   764: fadd
    //   765: fadd
    //   766: f2i
    //   767: istore #14
    //   769: aload_1
    //   770: iload #15
    //   772: iload #16
    //   774: iload #17
    //   776: iload #14
    //   778: invokevirtual layout : (IIII)V
    //   781: aload_0
    //   782: getfield mCycleMap : Ljava/util/HashMap;
    //   785: astore #5
    //   787: aload #5
    //   789: ifnull -> 875
    //   792: aload #5
    //   794: invokevirtual values : ()Ljava/util/Collection;
    //   797: invokeinterface iterator : ()Ljava/util/Iterator;
    //   802: astore #5
    //   804: aload #5
    //   806: invokeinterface hasNext : ()Z
    //   811: ifeq -> 875
    //   814: aload #5
    //   816: invokeinterface next : ()Ljava/lang/Object;
    //   821: checkcast androidx/constraintlayout/core/motion/utils/KeyCycleOscillator
    //   824: astore #18
    //   826: aload #18
    //   828: instanceof androidx/constraintlayout/core/motion/utils/KeyCycleOscillator$PathRotateSet
    //   831: ifeq -> 865
    //   834: aload #18
    //   836: checkcast androidx/constraintlayout/core/motion/utils/KeyCycleOscillator$PathRotateSet
    //   839: astore #19
    //   841: aload_0
    //   842: getfield mInterpolateVelocity : [D
    //   845: astore #18
    //   847: aload #19
    //   849: aload_1
    //   850: fload_2
    //   851: aload #18
    //   853: iconst_0
    //   854: daload
    //   855: aload #18
    //   857: iconst_1
    //   858: daload
    //   859: invokevirtual setPathRotate : (Landroidx/constraintlayout/core/motion/MotionWidget;FDD)V
    //   862: goto -> 872
    //   865: aload #18
    //   867: aload_1
    //   868: fload_2
    //   869: invokevirtual setProperty : (Landroidx/constraintlayout/core/motion/MotionWidget;F)V
    //   872: goto -> 804
    //   875: iconst_0
    //   876: ireturn
  }
  
  String name() {
    return this.mView.getName();
  }
  
  void positionKeyframe(MotionWidget paramMotionWidget, MotionKeyPosition paramMotionKeyPosition, float paramFloat1, float paramFloat2, String[] paramArrayOfString, float[] paramArrayOffloat) {
    FloatRect floatRect1 = new FloatRect();
    floatRect1.left = this.mStartMotionPath.x;
    floatRect1.top = this.mStartMotionPath.y;
    floatRect1.right = floatRect1.left + this.mStartMotionPath.width;
    floatRect1.bottom = floatRect1.top + this.mStartMotionPath.height;
    FloatRect floatRect2 = new FloatRect();
    floatRect2.left = this.mEndMotionPath.x;
    floatRect2.top = this.mEndMotionPath.y;
    floatRect2.right = floatRect2.left + this.mEndMotionPath.width;
    floatRect2.bottom = floatRect2.top + this.mEndMotionPath.height;
    paramMotionKeyPosition.positionAttributes(paramMotionWidget, floatRect1, floatRect2, paramFloat1, paramFloat2, paramArrayOfString, paramArrayOffloat);
  }
  
  void rotate(Rect paramRect1, Rect paramRect2, int paramInt1, int paramInt2, int paramInt3) {
    int i;
    int j;
    switch (paramInt1) {
      default:
        return;
      case 4:
        j = paramRect1.left;
        paramInt3 = paramRect1.right;
        i = paramRect1.bottom;
        paramInt1 = paramRect1.top;
        paramRect2.left = paramInt2 - (paramRect1.width() + i + paramInt1) / 2;
        paramRect2.top = (j + paramInt3 - paramRect1.height()) / 2;
        paramRect2.right = paramRect2.left + paramRect1.width();
        paramRect2.bottom = paramRect2.top + paramRect1.height();
      case 3:
        paramInt1 = paramRect1.left + paramRect1.right;
        paramInt2 = paramRect1.top;
        paramInt2 = paramRect1.bottom;
        paramRect2.left = paramRect1.height() / 2 + paramRect1.top - paramInt1 / 2;
        paramRect2.top = paramInt3 - (paramRect1.height() + paramInt1) / 2;
        paramRect2.right = paramRect2.left + paramRect1.width();
        paramRect2.bottom = paramRect2.top + paramRect1.height();
      case 2:
        i = paramRect1.left;
        paramInt1 = paramRect1.right;
        paramInt3 = paramRect1.top;
        j = paramRect1.bottom;
        paramRect2.left = paramInt2 - (paramRect1.width() + paramInt3 + j) / 2;
        paramRect2.top = (i + paramInt1 - paramRect1.height()) / 2;
        paramRect2.right = paramRect2.left + paramRect1.width();
        paramRect2.bottom = paramRect2.top + paramRect1.height();
      case 1:
        break;
    } 
    paramInt2 = paramRect1.left;
    paramInt1 = paramRect1.right;
    paramRect2.left = (paramRect1.top + paramRect1.bottom - paramRect1.width()) / 2;
    paramRect2.top = paramInt3 - (paramRect1.height() + paramInt2 + paramInt1) / 2;
    paramRect2.right = paramRect2.left + paramRect1.width();
    paramRect2.bottom = paramRect2.top + paramRect1.height();
  }
  
  void setBothStates(MotionWidget paramMotionWidget) {
    this.mStartMotionPath.time = 0.0F;
    this.mStartMotionPath.position = 0.0F;
    this.mNoMovement = true;
    this.mStartMotionPath.setBounds(paramMotionWidget.getX(), paramMotionWidget.getY(), paramMotionWidget.getWidth(), paramMotionWidget.getHeight());
    this.mEndMotionPath.setBounds(paramMotionWidget.getX(), paramMotionWidget.getY(), paramMotionWidget.getWidth(), paramMotionWidget.getHeight());
    this.mStartPoint.setState(paramMotionWidget);
    this.mEndPoint.setState(paramMotionWidget);
  }
  
  public void setDrawPath(int paramInt) {
    this.mStartMotionPath.mDrawPath = paramInt;
  }
  
  public void setEnd(MotionWidget paramMotionWidget) {
    this.mEndMotionPath.time = 1.0F;
    this.mEndMotionPath.position = 1.0F;
    readView(this.mEndMotionPath);
    this.mEndMotionPath.setBounds(paramMotionWidget.getLeft(), paramMotionWidget.getTop(), paramMotionWidget.getWidth(), paramMotionWidget.getHeight());
    this.mEndMotionPath.applyParameters(paramMotionWidget);
    this.mEndPoint.setState(paramMotionWidget);
  }
  
  public void setPathMotionArc(int paramInt) {
    this.mPathMotionArc = paramInt;
  }
  
  public void setStart(MotionWidget paramMotionWidget) {
    this.mStartMotionPath.time = 0.0F;
    this.mStartMotionPath.position = 0.0F;
    this.mStartMotionPath.setBounds(paramMotionWidget.getX(), paramMotionWidget.getY(), paramMotionWidget.getWidth(), paramMotionWidget.getHeight());
    this.mStartMotionPath.applyParameters(paramMotionWidget);
    this.mStartPoint.setState(paramMotionWidget);
  }
  
  public void setStartState(ViewState paramViewState, MotionWidget paramMotionWidget, int paramInt1, int paramInt2, int paramInt3) {
    int i;
    int j;
    int k;
    this.mStartMotionPath.time = 0.0F;
    this.mStartMotionPath.position = 0.0F;
    Rect rect = new Rect();
    switch (paramInt1) {
      case 2:
        j = paramViewState.left;
        paramInt2 = paramViewState.right;
        k = paramViewState.top;
        i = paramViewState.bottom;
        rect.left = paramInt3 - (paramViewState.width() + k + i) / 2;
        rect.top = (j + paramInt2 - paramViewState.height()) / 2;
        rect.right = rect.left + paramViewState.width();
        rect.bottom = rect.top + paramViewState.height();
        break;
      case 1:
        i = paramViewState.left;
        paramInt3 = paramViewState.right;
        rect.left = (paramViewState.top + paramViewState.bottom - paramViewState.width()) / 2;
        rect.top = paramInt2 - (paramViewState.height() + i + paramInt3) / 2;
        rect.right = rect.left + paramViewState.width();
        rect.bottom = rect.top + paramViewState.height();
        break;
    } 
    this.mStartMotionPath.setBounds(rect.left, rect.top, rect.width(), rect.height());
    this.mStartPoint.setState(rect, paramMotionWidget, paramInt1, paramViewState.rotation);
  }
  
  public void setTransformPivotTarget(int paramInt) {
    this.mTransformPivotTarget = paramInt;
    this.mTransformPivotView = null;
  }
  
  public void setView(MotionWidget paramMotionWidget) {
    this.mView = paramMotionWidget;
  }
  
  public void setup(int paramInt1, int paramInt2, float paramFloat, long paramLong) {
    ArrayList<MotionKeyTrigger> arrayList1;
    ArrayList<MotionKeyTrigger> arrayList2;
    HashSet<String> hashSet1 = new HashSet();
    HashSet hashSet3 = new HashSet();
    HashSet<String> hashSet2 = new HashSet();
    HashSet hashSet4 = new HashSet();
    HashMap<Object, Object> hashMap = new HashMap<>();
    MotionKeyPosition motionKeyPosition2 = null;
    MotionKeyPosition motionKeyPosition1 = null;
    int i = this.mPathMotionArc;
    if (i != -1)
      this.mStartMotionPath.mPathMotionArc = i; 
    this.mStartPoint.different(this.mEndPoint, hashSet2);
    ArrayList<MotionKey> arrayList = this.mKeyList;
    if (arrayList != null) {
      for (MotionKey motionKey : arrayList) {
        if (motionKey instanceof MotionKeyPosition) {
          motionKeyPosition2 = (MotionKeyPosition)motionKey;
          insertKey(new MotionPaths(paramInt1, paramInt2, motionKeyPosition2, this.mStartMotionPath, this.mEndMotionPath));
          if (motionKeyPosition2.mCurveFit != -1)
            this.mCurveFitType = motionKeyPosition2.mCurveFit; 
          continue;
        } 
        if (motionKey instanceof MotionKeyCycle) {
          motionKey.getAttributeNames(hashSet4);
          continue;
        } 
        if (motionKey instanceof MotionKeyTimeCycle) {
          motionKey.getAttributeNames(hashSet3);
          continue;
        } 
        if (motionKey instanceof MotionKeyTrigger) {
          motionKeyPosition2 = motionKeyPosition1;
          if (motionKeyPosition1 == null)
            arrayList2 = new ArrayList(); 
          arrayList2.add((MotionKeyTrigger)motionKey);
          arrayList1 = arrayList2;
          continue;
        } 
        motionKey.setInterpolation(hashMap);
        motionKey.getAttributeNames(hashSet2);
      } 
    } else {
      arrayList1 = arrayList2;
    } 
    if (arrayList1 != null)
      this.mKeyTriggers = arrayList1.<MotionKeyTrigger>toArray(new MotionKeyTrigger[0]); 
    if (!hashSet2.isEmpty()) {
      this.mAttributesMap = new HashMap<>();
      for (String str : hashSet2) {
        SplineSet splineSet;
        if (str.startsWith("CUSTOM,")) {
          KeyFrameArray.CustomVar customVar = new KeyFrameArray.CustomVar();
          String str1 = str.split(",")[1];
          for (MotionKey motionKey : this.mKeyList) {
            if (motionKey.mCustom == null)
              continue; 
            CustomVariable customVariable = (CustomVariable)motionKey.mCustom.get(str1);
            if (customVariable != null)
              customVar.append(motionKey.mFramePosition, customVariable); 
          } 
          splineSet = SplineSet.makeCustomSplineSet(str, customVar);
        } else {
          splineSet = SplineSet.makeSpline(str, paramLong);
        } 
        if (splineSet == null)
          continue; 
        splineSet.setType(str);
        this.mAttributesMap.put(str, splineSet);
      } 
      ArrayList<MotionKey> arrayList3 = this.mKeyList;
      if (arrayList3 != null)
        for (MotionKey motionKey : arrayList3) {
          if (motionKey instanceof androidx.constraintlayout.core.motion.key.MotionKeyAttributes)
            motionKey.addValues(this.mAttributesMap); 
        }  
      this.mStartPoint.addValues(this.mAttributesMap, 0);
      this.mEndPoint.addValues(this.mAttributesMap, 100);
      for (String str : this.mAttributesMap.keySet()) {
        paramInt2 = 0;
        paramInt1 = paramInt2;
        if (hashMap.containsKey(str)) {
          Integer integer = (Integer)hashMap.get(str);
          paramInt1 = paramInt2;
          if (integer != null)
            paramInt1 = integer.intValue(); 
        } 
        SplineSet splineSet = this.mAttributesMap.get(str);
        if (splineSet != null)
          splineSet.setup(paramInt1); 
      } 
    } 
    if (!hashSet3.isEmpty()) {
      if (this.mTimeCycleAttributesMap == null)
        this.mTimeCycleAttributesMap = new HashMap<>(); 
      Iterator<String> iterator1 = hashSet3.iterator();
      HashSet hashSet = hashSet3;
      while (iterator1.hasNext()) {
        SplineSet splineSet;
        String str = iterator1.next();
        if (this.mTimeCycleAttributesMap.containsKey(str))
          continue; 
        boolean bool = false;
        if (str.startsWith("CUSTOM,")) {
          KeyFrameArray.CustomVar customVar = new KeyFrameArray.CustomVar();
          String str1 = str.split(",")[1];
          for (MotionKey motionKey : this.mKeyList) {
            if (motionKey.mCustom == null)
              continue; 
            CustomVariable customVariable = (CustomVariable)motionKey.mCustom.get(str1);
            if (customVariable != null)
              customVar.append(motionKey.mFramePosition, customVariable); 
          } 
          splineSet = SplineSet.makeCustomSplineSet(str, customVar);
        } else {
          splineSet = SplineSet.makeSpline(str, paramLong);
        } 
        if (splineSet == null)
          continue; 
        splineSet.setType(str);
      } 
      ArrayList<MotionKey> arrayList3 = this.mKeyList;
      if (arrayList3 != null)
        for (MotionKey motionKey : arrayList3) {
          if (motionKey instanceof MotionKeyTimeCycle)
            ((MotionKeyTimeCycle)motionKey).addTimeValues(this.mTimeCycleAttributesMap); 
        }  
      for (String str : this.mTimeCycleAttributesMap.keySet()) {
        paramInt1 = 0;
        if (hashMap.containsKey(str))
          paramInt1 = ((Integer)hashMap.get(str)).intValue(); 
        ((TimeCycleSplineSet)this.mTimeCycleAttributesMap.get(str)).setup(paramInt1);
      } 
    } 
    MotionPaths[] arrayOfMotionPaths = new MotionPaths[this.mMotionPaths.size() + 2];
    paramInt1 = 1;
    arrayOfMotionPaths[0] = this.mStartMotionPath;
    arrayOfMotionPaths[arrayOfMotionPaths.length - 1] = this.mEndMotionPath;
    if (this.mMotionPaths.size() > 0 && this.mCurveFitType == MotionKey.UNSET)
      this.mCurveFitType = 0; 
    Iterator<MotionPaths> iterator = this.mMotionPaths.iterator();
    while (iterator.hasNext()) {
      arrayOfMotionPaths[paramInt1] = iterator.next();
      paramInt1++;
    } 
    byte b = 18;
    hashSet1 = new HashSet();
    for (String str : this.mEndMotionPath.customAttributes.keySet()) {
      if (this.mStartMotionPath.customAttributes.containsKey(str) && !hashSet2.contains("CUSTOM," + str))
        hashSet1.add(str); 
    } 
    String[] arrayOfString = (String[])hashSet1.toArray((Object[])new String[0]);
    this.mAttributeNames = arrayOfString;
    this.mAttributeInterpolatorCount = new int[arrayOfString.length];
    paramInt1 = 0;
    while (true) {
      boolean bool;
      arrayOfString = this.mAttributeNames;
      if (paramInt1 < arrayOfString.length) {
        String str = arrayOfString[paramInt1];
        this.mAttributeInterpolatorCount[paramInt1] = 0;
        for (paramInt2 = 0; paramInt2 < arrayOfMotionPaths.length; paramInt2++) {
          if ((arrayOfMotionPaths[paramInt2]).customAttributes.containsKey(str)) {
            CustomVariable customVariable = (arrayOfMotionPaths[paramInt2]).customAttributes.get(str);
            if (customVariable != null) {
              int[] arrayOfInt = this.mAttributeInterpolatorCount;
              arrayOfInt[paramInt1] = arrayOfInt[paramInt1] + customVariable.numberOfInterpolatedValues();
              break;
            } 
          } 
        } 
        paramInt1++;
        continue;
      } 
      if ((arrayOfMotionPaths[0]).mPathMotionArc != -1) {
        bool = true;
      } else {
        bool = false;
      } 
      boolean[] arrayOfBoolean = new boolean[this.mAttributeNames.length + 18];
      for (paramInt1 = 1; paramInt1 < arrayOfMotionPaths.length; paramInt1++)
        arrayOfMotionPaths[paramInt1].different(arrayOfMotionPaths[paramInt1 - 1], arrayOfBoolean, this.mAttributeNames, bool); 
      i = 0;
      paramInt1 = 1;
      while (paramInt1 < arrayOfBoolean.length) {
        paramInt2 = i;
        if (arrayOfBoolean[paramInt1])
          paramInt2 = i + 1; 
        paramInt1++;
        i = paramInt2;
      } 
      this.mInterpolateVariables = new int[i];
      int j = Math.max(2, i);
      this.mInterpolateData = new double[j];
      this.mInterpolateVelocity = new double[j];
      paramInt1 = 0;
      paramInt2 = 1;
      while (paramInt2 < arrayOfBoolean.length) {
        i = paramInt1;
        if (arrayOfBoolean[paramInt2]) {
          this.mInterpolateVariables[paramInt1] = paramInt2;
          i = paramInt1 + 1;
        } 
        paramInt2++;
        paramInt1 = i;
      } 
      double[][] arrayOfDouble = new double[arrayOfMotionPaths.length][this.mInterpolateVariables.length];
      double[] arrayOfDouble1 = new double[arrayOfMotionPaths.length];
      paramInt2 = 0;
      HashSet<String> hashSet = hashSet2;
      while (paramInt2 < arrayOfMotionPaths.length) {
        arrayOfMotionPaths[paramInt2].fillStandard(arrayOfDouble[paramInt2], this.mInterpolateVariables);
        arrayOfDouble1[paramInt2] = (arrayOfMotionPaths[paramInt2]).time;
        paramInt2++;
      } 
      paramInt2 = 0;
      paramInt1 = j;
      HashMap<Object, Object> hashMap1 = hashMap;
      while (true) {
        int[] arrayOfInt = this.mInterpolateVariables;
        if (paramInt2 < arrayOfInt.length) {
          if (arrayOfInt[paramInt2] < MotionPaths.names.length) {
            String str = MotionPaths.names[this.mInterpolateVariables[paramInt2]] + " [";
            for (i = 0; i < arrayOfMotionPaths.length; i++)
              str = str + arrayOfDouble[i][paramInt2]; 
          } 
          paramInt2++;
          continue;
        } 
        this.mSpline = new CurveFit[this.mAttributeNames.length + 1];
        paramInt2 = 0;
        boolean[] arrayOfBoolean1 = arrayOfBoolean;
        HashSet<String> hashSet5 = hashSet1;
        paramInt1 = b;
        while (true) {
          String[] arrayOfString1 = this.mAttributeNames;
          if (paramInt2 < arrayOfString1.length) {
            b = 0;
            double[][] arrayOfDouble2 = (double[][])null;
            hashMap = null;
            String str = arrayOfString1[paramInt2];
            for (i = 0; i < arrayOfMotionPaths.length; i++) {
              if (arrayOfMotionPaths[i].hasCustomData(str)) {
                if (arrayOfDouble2 == null) {
                  arrayOfDouble3 = new double[arrayOfMotionPaths.length];
                  arrayOfDouble2 = new double[arrayOfMotionPaths.length][arrayOfMotionPaths[i].getCustomDataCount(str)];
                } 
                arrayOfDouble3[b] = (arrayOfMotionPaths[i]).time;
                arrayOfMotionPaths[i].getCustomData(str, arrayOfDouble2[b], 0);
                b++;
              } 
            } 
            double[] arrayOfDouble3 = Arrays.copyOf(arrayOfDouble3, b);
            arrayOfDouble2 = Arrays.<double[]>copyOf(arrayOfDouble2, b);
            this.mSpline[paramInt2 + 1] = CurveFit.get(this.mCurveFitType, arrayOfDouble3, arrayOfDouble2);
            paramInt2++;
            continue;
          } 
          this.mSpline[0] = CurveFit.get(this.mCurveFitType, arrayOfDouble1, arrayOfDouble);
          if ((arrayOfMotionPaths[0]).mPathMotionArc != -1) {
            paramInt2 = arrayOfMotionPaths.length;
            int[] arrayOfInt1 = new int[paramInt2];
            double[] arrayOfDouble2 = new double[paramInt2];
            double[][] arrayOfDouble3 = new double[paramInt2][2];
            for (paramInt1 = 0; paramInt1 < paramInt2; paramInt1++) {
              arrayOfInt1[paramInt1] = (arrayOfMotionPaths[paramInt1]).mPathMotionArc;
              arrayOfDouble2[paramInt1] = (arrayOfMotionPaths[paramInt1]).time;
              arrayOfDouble3[paramInt1][0] = (arrayOfMotionPaths[paramInt1]).x;
              arrayOfDouble3[paramInt1][1] = (arrayOfMotionPaths[paramInt1]).y;
            } 
            this.mArcSpline = CurveFit.getArc(arrayOfInt1, arrayOfDouble2, arrayOfDouble3);
          } 
          float f = Float.NaN;
          this.mCycleMap = new HashMap<>();
          if (this.mKeyList != null) {
            for (String str : hashSet4) {
              KeyCycleOscillator keyCycleOscillator = KeyCycleOscillator.makeWidgetCycle(str);
              if (keyCycleOscillator == null)
                continue; 
              paramFloat = f;
              if (keyCycleOscillator.variesByPath()) {
                paramFloat = f;
                if (Float.isNaN(f))
                  paramFloat = getPreCycleDistance(); 
              } 
              keyCycleOscillator.setType(str);
              this.mCycleMap.put(str, keyCycleOscillator);
              f = paramFloat;
            } 
            for (MotionKey motionKey : this.mKeyList) {
              if (motionKey instanceof MotionKeyCycle)
                ((MotionKeyCycle)motionKey).addCycleValues(this.mCycleMap); 
            } 
            Iterator<KeyCycleOscillator> iterator1 = this.mCycleMap.values().iterator();
            while (iterator1.hasNext())
              ((KeyCycleOscillator)iterator1.next()).setup(f); 
          } 
          return;
        } 
        break;
      } 
      break;
    } 
  }
  
  public void setupRelative(Motion paramMotion) {
    this.mStartMotionPath.setupRelative(paramMotion, paramMotion.mStartMotionPath);
    this.mEndMotionPath.setupRelative(paramMotion, paramMotion.mEndMotionPath);
  }
  
  public String toString() {
    return " start: x: " + this.mStartMotionPath.x + " y: " + this.mStartMotionPath.y + " end: x: " + this.mEndMotionPath.x + " y: " + this.mEndMotionPath.y;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\motion\Motion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */