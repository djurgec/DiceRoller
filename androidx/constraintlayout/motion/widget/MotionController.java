package androidx.constraintlayout.motion.widget;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import androidx.constraintlayout.core.motion.utils.CurveFit;
import androidx.constraintlayout.core.motion.utils.Easing;
import androidx.constraintlayout.core.motion.utils.KeyCache;
import androidx.constraintlayout.core.motion.utils.KeyCycleOscillator;
import androidx.constraintlayout.core.motion.utils.SplineSet;
import androidx.constraintlayout.core.motion.utils.VelocityMatrix;
import androidx.constraintlayout.motion.utils.ViewOscillator;
import androidx.constraintlayout.motion.utils.ViewSpline;
import androidx.constraintlayout.motion.utils.ViewState;
import androidx.constraintlayout.motion.utils.ViewTimeCycle;
import androidx.constraintlayout.widget.ConstraintAttribute;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class MotionController {
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
  
  private HashMap<String, ViewSpline> mAttributesMap;
  
  String mConstraintTag;
  
  float mCurrentCenterX;
  
  float mCurrentCenterY;
  
  private int mCurveFitType = -1;
  
  private HashMap<String, ViewOscillator> mCycleMap;
  
  private MotionPaths mEndMotionPath = new MotionPaths();
  
  private MotionConstrainedPoint mEndPoint = new MotionConstrainedPoint();
  
  boolean mForceMeasure = false;
  
  int mId;
  
  private double[] mInterpolateData;
  
  private int[] mInterpolateVariables;
  
  private double[] mInterpolateVelocity;
  
  private ArrayList<Key> mKeyList = new ArrayList<>();
  
  private KeyTrigger[] mKeyTriggers;
  
  private ArrayList<MotionPaths> mMotionPaths = new ArrayList<>();
  
  float mMotionStagger = Float.NaN;
  
  private boolean mNoMovement = false;
  
  private int mPathMotionArc = Key.UNSET;
  
  private Interpolator mQuantizeMotionInterpolator = null;
  
  private float mQuantizeMotionPhase = Float.NaN;
  
  private int mQuantizeMotionSteps = Key.UNSET;
  
  private CurveFit[] mSpline;
  
  float mStaggerOffset = 0.0F;
  
  float mStaggerScale = 1.0F;
  
  private MotionPaths mStartMotionPath = new MotionPaths();
  
  private MotionConstrainedPoint mStartPoint = new MotionConstrainedPoint();
  
  Rect mTempRect = new Rect();
  
  private HashMap<String, ViewTimeCycle> mTimeCycleAttributesMap;
  
  private int mTransformPivotTarget = Key.UNSET;
  
  private View mTransformPivotView = null;
  
  private float[] mValuesBuff = new float[4];
  
  private float[] mVelocity = new float[1];
  
  View mView;
  
  MotionController(View paramView) {
    setView(paramView);
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
      float f5 = f2;
      float f4 = paramFloat;
      if (motionPaths.mKeyFrameEasing != null)
        if (motionPaths.time < f1) {
          easing1 = motionPaths.mKeyFrameEasing;
          f5 = motionPaths.time;
          f4 = paramFloat;
        } else {
          easing1 = easing;
          f5 = f2;
          f4 = paramFloat;
          if (Float.isNaN(paramFloat)) {
            f4 = motionPaths.time;
            f5 = f2;
            easing1 = easing;
          } 
        }  
      easing = easing1;
      f2 = f5;
      paramFloat = f4;
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
  
  private static Interpolator getInterpolator(Context paramContext, int paramInt1, String paramString, int paramInt2) {
    switch (paramInt1) {
      default:
        return null;
      case 5:
        return (Interpolator)new OvershootInterpolator();
      case 4:
        return (Interpolator)new BounceInterpolator();
      case 3:
        return null;
      case 2:
        return (Interpolator)new DecelerateInterpolator();
      case 1:
        return (Interpolator)new AccelerateInterpolator();
      case 0:
        return (Interpolator)new AccelerateDecelerateInterpolator();
      case -1:
        return new Interpolator(Easing.getInterpolator(paramString)) {
            final Easing val$easing;
            
            public float getInterpolation(float param1Float) {
              return (float)easing.get(param1Float);
            }
          };
      case -2:
        break;
    } 
    return AnimationUtils.loadInterpolator(paramContext, paramInt2);
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
        float f6 = f3;
        Easing easing1 = easing;
        float f7 = f4;
        if (motionPaths.mKeyFrameEasing != null)
          if (motionPaths.time < f5) {
            easing1 = motionPaths.mKeyFrameEasing;
            f7 = motionPaths.time;
            f6 = f3;
          } else {
            f6 = f3;
            easing1 = easing;
            f7 = f4;
            if (Float.isNaN(f3)) {
              f6 = motionPaths.time;
              f7 = f4;
              easing1 = easing;
            } 
          }  
        f3 = f6;
        easing = easing1;
        f4 = f7;
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
    int i = Collections.binarySearch((List)this.mMotionPaths, paramMotionPaths);
    if (i == 0) {
      float f = paramMotionPaths.position;
      Log.e("MotionController", (new StringBuilder(52)).append(" KeyPath position \"").append(f).append("\" outside of range").toString());
    } 
    this.mMotionPaths.add(-i - 1, paramMotionPaths);
  }
  
  private void readView(MotionPaths paramMotionPaths) {
    paramMotionPaths.setBounds((int)this.mView.getX(), (int)this.mView.getY(), this.mView.getWidth(), this.mView.getHeight());
  }
  
  public void addKey(Key paramKey) {
    this.mKeyList.add(paramKey);
  }
  
  void addKeys(ArrayList<Key> paramArrayList) {
    this.mKeyList.addAll(paramArrayList);
  }
  
  void buildBounds(float[] paramArrayOffloat, int paramInt) {
    float f = 1.0F / (paramInt - 1);
    HashMap<String, ViewSpline> hashMap1 = this.mAttributesMap;
    if (hashMap1 == null) {
      hashMap1 = null;
    } else {
      SplineSet splineSet = (SplineSet)hashMap1.get("translationX");
    } 
    HashMap<String, ViewSpline> hashMap2 = this.mAttributesMap;
    if (hashMap2 != null)
      SplineSet splineSet = (SplineSet)hashMap2.get("translationY"); 
    HashMap<String, ViewOscillator> hashMap = this.mCycleMap;
    if (hashMap != null)
      ViewOscillator viewOscillator = hashMap.get("translationX"); 
    hashMap = this.mCycleMap;
    if (hashMap != null)
      ViewOscillator viewOscillator = hashMap.get("translationY"); 
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
        f4 = f2;
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
  
  int buildKeyFrames(float[] paramArrayOffloat, int[] paramArrayOfint) {
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
        this.mStartMotionPath.getCenter(arrayOfDouble[b1], this.mInterpolateVariables, this.mInterpolateData, paramArrayOffloat, b2);
        b2 += true;
      } 
      return b2 / 2;
    } 
    return 0;
  }
  
  void buildPath(float[] paramArrayOffloat, int paramInt) {
    SplineSet splineSet1;
    SplineSet splineSet2;
    ViewOscillator viewOscillator1;
    float f = 1.0F / (paramInt - 1);
    HashMap<String, ViewSpline> hashMap1 = this.mAttributesMap;
    ViewOscillator viewOscillator2 = null;
    if (hashMap1 == null) {
      hashMap1 = null;
    } else {
      splineSet1 = (SplineSet)hashMap1.get("translationX");
    } 
    HashMap<String, ViewSpline> hashMap2 = this.mAttributesMap;
    if (hashMap2 == null) {
      hashMap2 = null;
    } else {
      splineSet2 = (SplineSet)hashMap2.get("translationY");
    } 
    HashMap<String, ViewOscillator> hashMap3 = this.mCycleMap;
    if (hashMap3 == null) {
      hashMap3 = null;
    } else {
      viewOscillator1 = hashMap3.get("translationX");
    } 
    HashMap<String, ViewOscillator> hashMap4 = this.mCycleMap;
    if (hashMap4 != null)
      viewOscillator2 = hashMap4.get("translationY"); 
    for (byte b = 0; b < paramInt; b++) {
      float f1;
      float f2 = b * f;
      float f4 = this.mStaggerScale;
      if (f4 != 1.0F) {
        float f5 = this.mStaggerOffset;
        f1 = f2;
        if (f2 < f5)
          f1 = 0.0F; 
        if (f1 > f5 && f1 < 1.0D)
          f1 = Math.min((f1 - f5) * f4, 1.0F); 
      } else {
        f1 = f2;
      } 
      double d = f1;
      Easing easing = this.mStartMotionPath.mKeyFrameEasing;
      Iterator<MotionPaths> iterator = this.mMotionPaths.iterator();
      float f3 = 0.0F;
      f2 = Float.NaN;
      while (iterator.hasNext()) {
        MotionPaths motionPaths = iterator.next();
        f4 = f2;
        Easing easing1 = easing;
        float f5 = f3;
        if (motionPaths.mKeyFrameEasing != null)
          if (motionPaths.time < f1) {
            easing1 = motionPaths.mKeyFrameEasing;
            f5 = motionPaths.time;
            f4 = f2;
          } else {
            f4 = f2;
            easing1 = easing;
            f5 = f3;
            if (Float.isNaN(f2)) {
              f4 = motionPaths.time;
              f5 = f3;
              easing1 = easing;
            } 
          }  
        f2 = f4;
        easing = easing1;
        f3 = f5;
      } 
      if (easing != null) {
        f4 = f2;
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
      if (viewOscillator1 != null) {
        int i = b * 2;
        paramArrayOffloat[i] = paramArrayOffloat[i] + viewOscillator1.get(f1);
      } else if (splineSet1 != null) {
        int i = b * 2;
        paramArrayOffloat[i] = paramArrayOffloat[i] + splineSet1.get(f1);
      } 
      if (viewOscillator2 != null) {
        int i = b * 2 + 1;
        paramArrayOffloat[i] = paramArrayOffloat[i] + viewOscillator2.get(f1);
      } else if (splineSet2 != null) {
        int i = b * 2 + 1;
        paramArrayOffloat[i] = paramArrayOffloat[i] + splineSet2.get(f1);
      } 
    } 
  }
  
  void buildRect(float paramFloat, float[] paramArrayOffloat, int paramInt) {
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
  
  void endTrigger(boolean paramBoolean) {
    if ("button".equals(Debug.getName(this.mView)) && this.mKeyTriggers != null) {
      byte b = 0;
      while (true) {
        KeyTrigger[] arrayOfKeyTrigger = this.mKeyTriggers;
        if (b < arrayOfKeyTrigger.length) {
          float f;
          KeyTrigger keyTrigger = arrayOfKeyTrigger[b];
          if (paramBoolean) {
            f = -100.0F;
          } else {
            f = 100.0F;
          } 
          keyTrigger.conditionallyFire(f, this.mView);
          b++;
          continue;
        } 
        break;
      } 
    } 
  }
  
  public int getAnimateRelativeTo() {
    return this.mStartMotionPath.mAnimateRelativeTo;
  }
  
  int getAttributeValues(String paramString, float[] paramArrayOffloat, int paramInt) {
    float f = 1.0F / (paramInt - 1);
    SplineSet splineSet = (SplineSet)this.mAttributesMap.get(paramString);
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
    float f5 = this.mEndMotionPath.x - this.mStartMotionPath.x;
    float f3 = this.mEndMotionPath.y - this.mStartMotionPath.y;
    f1 = this.mEndMotionPath.width;
    float f4 = this.mStartMotionPath.width;
    float f2 = this.mEndMotionPath.height;
    paramFloat1 = this.mStartMotionPath.height;
    paramArrayOffloat[0] = (1.0F - paramFloat2) * f5 + (f5 + f1 - f4) * paramFloat2;
    paramArrayOffloat[1] = (1.0F - paramFloat3) * f3 + (f3 + f2 - paramFloat1) * paramFloat3;
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
  
  MotionPaths getKeyFrame(int paramInt) {
    return this.mMotionPaths.get(paramInt);
  }
  
  public int getKeyFrameInfo(int paramInt, int[] paramArrayOfint) {
    byte b = 0;
    int i = 0;
    float[] arrayOfFloat = new float[2];
    Iterator<Key> iterator = this.mKeyList.iterator();
    while (true) {
      int j = i;
      if (iterator.hasNext()) {
        Key key = iterator.next();
        if (key.mType != paramInt && paramInt == -1) {
          i = j;
          continue;
        } 
        paramArrayOfint[j] = 0;
        i = j + 1;
        paramArrayOfint[i] = key.mType;
        paramArrayOfint[++i] = key.mFramePosition;
        float f = key.mFramePosition / 100.0F;
        this.mSpline[0].getPos(f, this.mInterpolateData);
        this.mStartMotionPath.getCenter(f, this.mInterpolateVariables, this.mInterpolateData, arrayOfFloat, 0);
        paramArrayOfint[++i] = Float.floatToIntBits(arrayOfFloat[0]);
        int k = i + 1;
        paramArrayOfint[k] = Float.floatToIntBits(arrayOfFloat[1]);
        i = k;
        if (key instanceof KeyPosition) {
          key = key;
          i = k + 1;
          paramArrayOfint[i] = ((KeyPosition)key).mPositionType;
          paramArrayOfint[++i] = Float.floatToIntBits(((KeyPosition)key).mPercentX);
          paramArrayOfint[++i] = Float.floatToIntBits(((KeyPosition)key).mPercentY);
        } 
        paramArrayOfint[j] = ++i - j;
        b++;
        continue;
      } 
      return b;
    } 
  }
  
  float getKeyFrameParameter(int paramInt, float paramFloat1, float paramFloat2) {
    float f3 = this.mEndMotionPath.x - this.mStartMotionPath.x;
    float f1 = this.mEndMotionPath.y - this.mStartMotionPath.y;
    float f6 = this.mStartMotionPath.x;
    float f7 = this.mStartMotionPath.width / 2.0F;
    float f5 = this.mStartMotionPath.y;
    float f4 = this.mStartMotionPath.height / 2.0F;
    float f2 = (float)Math.hypot(f3, f1);
    if (f2 < 1.0E-7D)
      return Float.NaN; 
    paramFloat1 -= f6 + f7;
    f4 = paramFloat2 - f5 + f4;
    if ((float)Math.hypot(paramFloat1, f4) == 0.0F)
      return 0.0F; 
    paramFloat2 = paramFloat1 * f3 + f4 * f1;
    switch (paramInt) {
      default:
        return 0.0F;
      case 5:
        return f4 / f1;
      case 4:
        return paramFloat1 / f1;
      case 3:
        return f4 / f3;
      case 2:
        return paramFloat1 / f3;
      case 1:
        return (float)Math.sqrt((f2 * f2 - paramFloat2 * paramFloat2));
      case 0:
        break;
    } 
    return paramFloat2 / f2;
  }
  
  public int getKeyFramePositions(int[] paramArrayOfint, float[] paramArrayOffloat) {
    byte b = 0;
    boolean bool = false;
    for (Key key : this.mKeyList) {
      paramArrayOfint[b] = key.mFramePosition + key.mType * 1000;
      float f = key.mFramePosition / 100.0F;
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
  
  KeyPositionBase getPositionKeyframe(int paramInt1, int paramInt2, float paramFloat1, float paramFloat2) {
    RectF rectF1 = new RectF();
    rectF1.left = this.mStartMotionPath.x;
    rectF1.top = this.mStartMotionPath.y;
    rectF1.right = rectF1.left + this.mStartMotionPath.width;
    rectF1.bottom = rectF1.top + this.mStartMotionPath.height;
    RectF rectF2 = new RectF();
    rectF2.left = this.mEndMotionPath.x;
    rectF2.top = this.mEndMotionPath.y;
    rectF2.right = rectF2.left + this.mEndMotionPath.width;
    rectF2.bottom = rectF2.top + this.mEndMotionPath.height;
    for (Key key : this.mKeyList) {
      if (key instanceof KeyPositionBase && ((KeyPositionBase)key).intersects(paramInt1, paramInt2, rectF1, rectF2, paramFloat1, paramFloat2))
        return (KeyPositionBase)key; 
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
    ViewOscillator viewOscillator1;
    ViewOscillator viewOscillator2;
    ViewOscillator viewOscillator3;
    ViewOscillator viewOscillator4;
    float f4 = getAdjustedPosition(paramFloat1, this.mVelocity);
    HashMap<String, ViewSpline> hashMap1 = this.mAttributesMap;
    ViewOscillator viewOscillator5 = null;
    if (hashMap1 == null) {
      hashMap1 = null;
    } else {
      splineSet1 = (SplineSet)hashMap1.get("translationX");
    } 
    HashMap<String, ViewSpline> hashMap2 = this.mAttributesMap;
    if (hashMap2 == null) {
      hashMap2 = null;
    } else {
      splineSet2 = (SplineSet)hashMap2.get("translationY");
    } 
    HashMap<String, ViewSpline> hashMap3 = this.mAttributesMap;
    if (hashMap3 == null) {
      hashMap3 = null;
    } else {
      splineSet3 = (SplineSet)hashMap3.get("rotation");
    } 
    HashMap<String, ViewSpline> hashMap4 = this.mAttributesMap;
    if (hashMap4 == null) {
      hashMap4 = null;
    } else {
      splineSet4 = (SplineSet)hashMap4.get("scaleX");
    } 
    HashMap<String, ViewSpline> hashMap5 = this.mAttributesMap;
    if (hashMap5 == null) {
      hashMap5 = null;
    } else {
      splineSet5 = (SplineSet)hashMap5.get("scaleY");
    } 
    HashMap<String, ViewOscillator> hashMap6 = this.mCycleMap;
    if (hashMap6 == null) {
      hashMap6 = null;
    } else {
      viewOscillator1 = hashMap6.get("translationX");
    } 
    HashMap<String, ViewOscillator> hashMap7 = this.mCycleMap;
    if (hashMap7 == null) {
      hashMap7 = null;
    } else {
      viewOscillator2 = hashMap7.get("translationY");
    } 
    HashMap<String, ViewOscillator> hashMap8 = this.mCycleMap;
    if (hashMap8 == null) {
      hashMap8 = null;
    } else {
      viewOscillator3 = hashMap8.get("rotation");
    } 
    HashMap<String, ViewOscillator> hashMap9 = this.mCycleMap;
    if (hashMap9 == null) {
      hashMap9 = null;
    } else {
      viewOscillator4 = hashMap9.get("scaleX");
    } 
    HashMap<String, ViewOscillator> hashMap10 = this.mCycleMap;
    if (hashMap10 != null)
      viewOscillator5 = hashMap10.get("scaleY"); 
    VelocityMatrix velocityMatrix = new VelocityMatrix();
    velocityMatrix.clear();
    velocityMatrix.setRotationVelocity(splineSet3, f4);
    velocityMatrix.setTranslationVelocity(splineSet1, splineSet2, f4);
    velocityMatrix.setScaleVelocity(splineSet4, splineSet5, f4);
    velocityMatrix.setRotationVelocity((KeyCycleOscillator)viewOscillator3, f4);
    velocityMatrix.setTranslationVelocity((KeyCycleOscillator)viewOscillator1, (KeyCycleOscillator)viewOscillator2, f4);
    velocityMatrix.setScaleVelocity((KeyCycleOscillator)viewOscillator4, (KeyCycleOscillator)viewOscillator5, f4);
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
    float f3 = this.mEndMotionPath.x - this.mStartMotionPath.x;
    float f1 = this.mEndMotionPath.y - this.mStartMotionPath.y;
    paramFloat1 = this.mEndMotionPath.width;
    float f6 = this.mStartMotionPath.width;
    float f5 = this.mEndMotionPath.height;
    float f2 = this.mStartMotionPath.height;
    paramArrayOffloat[0] = (1.0F - paramFloat2) * f3 + (f3 + paramFloat1 - f6) * paramFloat2;
    paramArrayOffloat[1] = (1.0F - paramFloat3) * f1 + (f1 + f5 - f2) * paramFloat3;
    velocityMatrix.clear();
    velocityMatrix.setRotationVelocity(splineSet3, f4);
    velocityMatrix.setTranslationVelocity((SplineSet)arrayOfDouble, splineSet2, f4);
    velocityMatrix.setScaleVelocity(splineSet4, splineSet5, f4);
    velocityMatrix.setRotationVelocity((KeyCycleOscillator)viewOscillator3, f4);
    velocityMatrix.setTranslationVelocity((KeyCycleOscillator)viewOscillator1, (KeyCycleOscillator)viewOscillator2, f4);
    velocityMatrix.setScaleVelocity((KeyCycleOscillator)viewOscillator4, (KeyCycleOscillator)viewOscillator5, f4);
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
  
  public View getView() {
    return this.mView;
  }
  
  boolean interpolate(View paramView, float paramFloat, long paramLong, KeyCache paramKeyCache) {
    // Byte code:
    //   0: aload_0
    //   1: fload_2
    //   2: aconst_null
    //   3: invokespecial getAdjustedPosition : (F[F)F
    //   6: fstore_2
    //   7: aload_0
    //   8: getfield mQuantizeMotionSteps : I
    //   11: getstatic androidx/constraintlayout/motion/widget/Key.UNSET : I
    //   14: if_icmpeq -> 122
    //   17: fconst_1
    //   18: aload_0
    //   19: getfield mQuantizeMotionSteps : I
    //   22: i2f
    //   23: fdiv
    //   24: fstore #8
    //   26: fload_2
    //   27: fload #8
    //   29: fdiv
    //   30: f2d
    //   31: invokestatic floor : (D)D
    //   34: d2f
    //   35: fstore #7
    //   37: fload_2
    //   38: fload #8
    //   40: frem
    //   41: fload #8
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
    //   70: getfield mQuantizeMotionInterpolator : Landroid/view/animation/Interpolator;
    //   73: astore #20
    //   75: aload #20
    //   77: ifnull -> 92
    //   80: aload #20
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
    //   109: fload #8
    //   111: fmul
    //   112: fload #7
    //   114: fload #8
    //   116: fmul
    //   117: fadd
    //   118: fstore_2
    //   119: goto -> 122
    //   122: aload_0
    //   123: getfield mAttributesMap : Ljava/util/HashMap;
    //   126: astore #20
    //   128: aload #20
    //   130: ifnull -> 173
    //   133: aload #20
    //   135: invokevirtual values : ()Ljava/util/Collection;
    //   138: invokeinterface iterator : ()Ljava/util/Iterator;
    //   143: astore #20
    //   145: aload #20
    //   147: invokeinterface hasNext : ()Z
    //   152: ifeq -> 173
    //   155: aload #20
    //   157: invokeinterface next : ()Ljava/lang/Object;
    //   162: checkcast androidx/constraintlayout/motion/utils/ViewSpline
    //   165: aload_1
    //   166: fload_2
    //   167: invokevirtual setProperty : (Landroid/view/View;F)V
    //   170: goto -> 145
    //   173: aload_0
    //   174: getfield mTimeCycleAttributesMap : Ljava/util/HashMap;
    //   177: astore #20
    //   179: aload #20
    //   181: ifnull -> 263
    //   184: aload #20
    //   186: invokevirtual values : ()Ljava/util/Collection;
    //   189: invokeinterface iterator : ()Ljava/util/Iterator;
    //   194: astore #21
    //   196: iconst_0
    //   197: istore #18
    //   199: aconst_null
    //   200: astore #20
    //   202: aload #21
    //   204: invokeinterface hasNext : ()Z
    //   209: ifeq -> 260
    //   212: aload #21
    //   214: invokeinterface next : ()Ljava/lang/Object;
    //   219: checkcast androidx/constraintlayout/motion/utils/ViewTimeCycle
    //   222: astore #22
    //   224: aload #22
    //   226: instanceof androidx/constraintlayout/motion/utils/ViewTimeCycle$PathRotate
    //   229: ifeq -> 242
    //   232: aload #22
    //   234: checkcast androidx/constraintlayout/motion/utils/ViewTimeCycle$PathRotate
    //   237: astore #20
    //   239: goto -> 202
    //   242: iload #18
    //   244: aload #22
    //   246: aload_1
    //   247: fload_2
    //   248: lload_3
    //   249: aload #5
    //   251: invokevirtual setProperty : (Landroid/view/View;FJLandroidx/constraintlayout/core/motion/utils/KeyCache;)Z
    //   254: ior
    //   255: istore #18
    //   257: goto -> 202
    //   260: goto -> 269
    //   263: iconst_0
    //   264: istore #18
    //   266: aconst_null
    //   267: astore #20
    //   269: aload_0
    //   270: getfield mSpline : [Landroidx/constraintlayout/core/motion/utils/CurveFit;
    //   273: astore #21
    //   275: aload #21
    //   277: ifnull -> 854
    //   280: aload #21
    //   282: iconst_0
    //   283: aaload
    //   284: fload_2
    //   285: f2d
    //   286: aload_0
    //   287: getfield mInterpolateData : [D
    //   290: invokevirtual getPos : (D[D)V
    //   293: aload_0
    //   294: getfield mSpline : [Landroidx/constraintlayout/core/motion/utils/CurveFit;
    //   297: iconst_0
    //   298: aaload
    //   299: fload_2
    //   300: f2d
    //   301: aload_0
    //   302: getfield mInterpolateVelocity : [D
    //   305: invokevirtual getSlope : (D[D)V
    //   308: aload_0
    //   309: getfield mArcSpline : Landroidx/constraintlayout/core/motion/utils/CurveFit;
    //   312: astore #22
    //   314: aload #22
    //   316: ifnull -> 353
    //   319: aload_0
    //   320: getfield mInterpolateData : [D
    //   323: astore #21
    //   325: aload #21
    //   327: arraylength
    //   328: ifle -> 353
    //   331: aload #22
    //   333: fload_2
    //   334: f2d
    //   335: aload #21
    //   337: invokevirtual getPos : (D[D)V
    //   340: aload_0
    //   341: getfield mArcSpline : Landroidx/constraintlayout/core/motion/utils/CurveFit;
    //   344: fload_2
    //   345: f2d
    //   346: aload_0
    //   347: getfield mInterpolateVelocity : [D
    //   350: invokevirtual getSlope : (D[D)V
    //   353: aload_0
    //   354: getfield mNoMovement : Z
    //   357: ifne -> 391
    //   360: aload_0
    //   361: getfield mStartMotionPath : Landroidx/constraintlayout/motion/widget/MotionPaths;
    //   364: fload_2
    //   365: aload_1
    //   366: aload_0
    //   367: getfield mInterpolateVariables : [I
    //   370: aload_0
    //   371: getfield mInterpolateData : [D
    //   374: aload_0
    //   375: getfield mInterpolateVelocity : [D
    //   378: aconst_null
    //   379: aload_0
    //   380: getfield mForceMeasure : Z
    //   383: invokevirtual setView : (FLandroid/view/View;[I[D[D[DZ)V
    //   386: aload_0
    //   387: iconst_0
    //   388: putfield mForceMeasure : Z
    //   391: aload_0
    //   392: getfield mTransformPivotTarget : I
    //   395: getstatic androidx/constraintlayout/motion/widget/Key.UNSET : I
    //   398: if_icmpeq -> 531
    //   401: aload_0
    //   402: getfield mTransformPivotView : Landroid/view/View;
    //   405: ifnonnull -> 426
    //   408: aload_0
    //   409: aload_1
    //   410: invokevirtual getParent : ()Landroid/view/ViewParent;
    //   413: checkcast android/view/View
    //   416: aload_0
    //   417: getfield mTransformPivotTarget : I
    //   420: invokevirtual findViewById : (I)Landroid/view/View;
    //   423: putfield mTransformPivotView : Landroid/view/View;
    //   426: aload_0
    //   427: getfield mTransformPivotView : Landroid/view/View;
    //   430: astore #21
    //   432: aload #21
    //   434: ifnull -> 531
    //   437: aload #21
    //   439: invokevirtual getTop : ()I
    //   442: aload_0
    //   443: getfield mTransformPivotView : Landroid/view/View;
    //   446: invokevirtual getBottom : ()I
    //   449: iadd
    //   450: i2f
    //   451: fconst_2
    //   452: fdiv
    //   453: fstore #8
    //   455: aload_0
    //   456: getfield mTransformPivotView : Landroid/view/View;
    //   459: invokevirtual getLeft : ()I
    //   462: aload_0
    //   463: getfield mTransformPivotView : Landroid/view/View;
    //   466: invokevirtual getRight : ()I
    //   469: iadd
    //   470: i2f
    //   471: fconst_2
    //   472: fdiv
    //   473: fstore #9
    //   475: aload_1
    //   476: invokevirtual getRight : ()I
    //   479: aload_1
    //   480: invokevirtual getLeft : ()I
    //   483: isub
    //   484: ifle -> 531
    //   487: aload_1
    //   488: invokevirtual getBottom : ()I
    //   491: aload_1
    //   492: invokevirtual getTop : ()I
    //   495: isub
    //   496: ifle -> 531
    //   499: aload_1
    //   500: invokevirtual getLeft : ()I
    //   503: i2f
    //   504: fstore #6
    //   506: aload_1
    //   507: invokevirtual getTop : ()I
    //   510: i2f
    //   511: fstore #7
    //   513: aload_1
    //   514: fload #9
    //   516: fload #6
    //   518: fsub
    //   519: invokevirtual setPivotX : (F)V
    //   522: aload_1
    //   523: fload #8
    //   525: fload #7
    //   527: fsub
    //   528: invokevirtual setPivotY : (F)V
    //   531: aload_0
    //   532: getfield mAttributesMap : Ljava/util/HashMap;
    //   535: astore #21
    //   537: aload #21
    //   539: ifnull -> 621
    //   542: aload #21
    //   544: invokevirtual values : ()Ljava/util/Collection;
    //   547: invokeinterface iterator : ()Ljava/util/Iterator;
    //   552: astore #23
    //   554: aload #23
    //   556: invokeinterface hasNext : ()Z
    //   561: ifeq -> 621
    //   564: aload #23
    //   566: invokeinterface next : ()Ljava/lang/Object;
    //   571: checkcast androidx/constraintlayout/core/motion/utils/SplineSet
    //   574: astore #21
    //   576: aload #21
    //   578: instanceof androidx/constraintlayout/motion/utils/ViewSpline$PathRotate
    //   581: ifeq -> 618
    //   584: aload_0
    //   585: getfield mInterpolateVelocity : [D
    //   588: astore #22
    //   590: aload #22
    //   592: arraylength
    //   593: iconst_1
    //   594: if_icmple -> 618
    //   597: aload #21
    //   599: checkcast androidx/constraintlayout/motion/utils/ViewSpline$PathRotate
    //   602: aload_1
    //   603: fload_2
    //   604: aload #22
    //   606: iconst_0
    //   607: daload
    //   608: aload #22
    //   610: iconst_1
    //   611: daload
    //   612: invokevirtual setPathRotate : (Landroid/view/View;FDD)V
    //   615: goto -> 618
    //   618: goto -> 554
    //   621: aload #20
    //   623: ifnull -> 658
    //   626: aload_0
    //   627: getfield mInterpolateVelocity : [D
    //   630: astore #21
    //   632: iload #18
    //   634: aload #20
    //   636: aload_1
    //   637: aload #5
    //   639: fload_2
    //   640: lload_3
    //   641: aload #21
    //   643: iconst_0
    //   644: daload
    //   645: aload #21
    //   647: iconst_1
    //   648: daload
    //   649: invokevirtual setPathRotate : (Landroid/view/View;Landroidx/constraintlayout/core/motion/utils/KeyCache;FJDD)Z
    //   652: ior
    //   653: istore #18
    //   655: goto -> 658
    //   658: iconst_1
    //   659: istore #14
    //   661: aload_0
    //   662: getfield mSpline : [Landroidx/constraintlayout/core/motion/utils/CurveFit;
    //   665: astore #5
    //   667: iload #14
    //   669: aload #5
    //   671: arraylength
    //   672: if_icmpge -> 725
    //   675: aload #5
    //   677: iload #14
    //   679: aaload
    //   680: fload_2
    //   681: f2d
    //   682: aload_0
    //   683: getfield mValuesBuff : [F
    //   686: invokevirtual getPos : (D[F)V
    //   689: aload_0
    //   690: getfield mStartMotionPath : Landroidx/constraintlayout/motion/widget/MotionPaths;
    //   693: getfield attributes : Ljava/util/LinkedHashMap;
    //   696: aload_0
    //   697: getfield mAttributeNames : [Ljava/lang/String;
    //   700: iload #14
    //   702: iconst_1
    //   703: isub
    //   704: aaload
    //   705: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   708: checkcast androidx/constraintlayout/widget/ConstraintAttribute
    //   711: aload_1
    //   712: aload_0
    //   713: getfield mValuesBuff : [F
    //   716: invokevirtual setInterpolatedValue : (Landroid/view/View;[F)V
    //   719: iinc #14, 1
    //   722: goto -> 661
    //   725: aload_0
    //   726: getfield mStartPoint : Landroidx/constraintlayout/motion/widget/MotionConstrainedPoint;
    //   729: getfield mVisibilityMode : I
    //   732: ifne -> 803
    //   735: fload_2
    //   736: fconst_0
    //   737: fcmpg
    //   738: ifgt -> 755
    //   741: aload_1
    //   742: aload_0
    //   743: getfield mStartPoint : Landroidx/constraintlayout/motion/widget/MotionConstrainedPoint;
    //   746: getfield visibility : I
    //   749: invokevirtual setVisibility : (I)V
    //   752: goto -> 803
    //   755: fload_2
    //   756: fconst_1
    //   757: fcmpl
    //   758: iflt -> 775
    //   761: aload_1
    //   762: aload_0
    //   763: getfield mEndPoint : Landroidx/constraintlayout/motion/widget/MotionConstrainedPoint;
    //   766: getfield visibility : I
    //   769: invokevirtual setVisibility : (I)V
    //   772: goto -> 803
    //   775: aload_0
    //   776: getfield mEndPoint : Landroidx/constraintlayout/motion/widget/MotionConstrainedPoint;
    //   779: getfield visibility : I
    //   782: aload_0
    //   783: getfield mStartPoint : Landroidx/constraintlayout/motion/widget/MotionConstrainedPoint;
    //   786: getfield visibility : I
    //   789: if_icmpeq -> 800
    //   792: aload_1
    //   793: iconst_0
    //   794: invokevirtual setVisibility : (I)V
    //   797: goto -> 803
    //   800: goto -> 803
    //   803: iload #18
    //   805: istore #19
    //   807: aload_0
    //   808: getfield mKeyTriggers : [Landroidx/constraintlayout/motion/widget/KeyTrigger;
    //   811: ifnull -> 1116
    //   814: iconst_0
    //   815: istore #14
    //   817: aload_0
    //   818: getfield mKeyTriggers : [Landroidx/constraintlayout/motion/widget/KeyTrigger;
    //   821: astore #5
    //   823: iload #14
    //   825: aload #5
    //   827: arraylength
    //   828: if_icmpge -> 847
    //   831: aload #5
    //   833: iload #14
    //   835: aaload
    //   836: fload_2
    //   837: aload_1
    //   838: invokevirtual conditionallyFire : (FLandroid/view/View;)V
    //   841: iinc #14, 1
    //   844: goto -> 817
    //   847: iload #18
    //   849: istore #19
    //   851: goto -> 1116
    //   854: aload_0
    //   855: getfield mStartMotionPath : Landroidx/constraintlayout/motion/widget/MotionPaths;
    //   858: getfield x : F
    //   861: aload_0
    //   862: getfield mEndMotionPath : Landroidx/constraintlayout/motion/widget/MotionPaths;
    //   865: getfield x : F
    //   868: aload_0
    //   869: getfield mStartMotionPath : Landroidx/constraintlayout/motion/widget/MotionPaths;
    //   872: getfield x : F
    //   875: fsub
    //   876: fload_2
    //   877: fmul
    //   878: fadd
    //   879: fstore #13
    //   881: aload_0
    //   882: getfield mStartMotionPath : Landroidx/constraintlayout/motion/widget/MotionPaths;
    //   885: getfield y : F
    //   888: aload_0
    //   889: getfield mEndMotionPath : Landroidx/constraintlayout/motion/widget/MotionPaths;
    //   892: getfield y : F
    //   895: aload_0
    //   896: getfield mStartMotionPath : Landroidx/constraintlayout/motion/widget/MotionPaths;
    //   899: getfield y : F
    //   902: fsub
    //   903: fload_2
    //   904: fmul
    //   905: fadd
    //   906: fstore #8
    //   908: aload_0
    //   909: getfield mStartMotionPath : Landroidx/constraintlayout/motion/widget/MotionPaths;
    //   912: getfield width : F
    //   915: fstore #10
    //   917: aload_0
    //   918: getfield mEndMotionPath : Landroidx/constraintlayout/motion/widget/MotionPaths;
    //   921: getfield width : F
    //   924: fstore #6
    //   926: aload_0
    //   927: getfield mStartMotionPath : Landroidx/constraintlayout/motion/widget/MotionPaths;
    //   930: getfield width : F
    //   933: fstore #7
    //   935: aload_0
    //   936: getfield mStartMotionPath : Landroidx/constraintlayout/motion/widget/MotionPaths;
    //   939: getfield height : F
    //   942: fstore #12
    //   944: aload_0
    //   945: getfield mEndMotionPath : Landroidx/constraintlayout/motion/widget/MotionPaths;
    //   948: getfield height : F
    //   951: fstore #9
    //   953: aload_0
    //   954: getfield mStartMotionPath : Landroidx/constraintlayout/motion/widget/MotionPaths;
    //   957: getfield height : F
    //   960: fstore #11
    //   962: fload #13
    //   964: ldc_w 0.5
    //   967: fadd
    //   968: f2i
    //   969: istore #15
    //   971: fload #8
    //   973: ldc_w 0.5
    //   976: fadd
    //   977: f2i
    //   978: istore #17
    //   980: fload #13
    //   982: ldc_w 0.5
    //   985: fadd
    //   986: fload #10
    //   988: fload #6
    //   990: fload #7
    //   992: fsub
    //   993: fload_2
    //   994: fmul
    //   995: fadd
    //   996: fadd
    //   997: f2i
    //   998: istore #14
    //   1000: ldc_w 0.5
    //   1003: fload #8
    //   1005: fadd
    //   1006: fload #12
    //   1008: fload #9
    //   1010: fload #11
    //   1012: fsub
    //   1013: fload_2
    //   1014: fmul
    //   1015: fadd
    //   1016: fadd
    //   1017: f2i
    //   1018: istore #16
    //   1020: aload_0
    //   1021: getfield mEndMotionPath : Landroidx/constraintlayout/motion/widget/MotionPaths;
    //   1024: getfield width : F
    //   1027: aload_0
    //   1028: getfield mStartMotionPath : Landroidx/constraintlayout/motion/widget/MotionPaths;
    //   1031: getfield width : F
    //   1034: fcmpl
    //   1035: ifne -> 1069
    //   1038: aload_0
    //   1039: getfield mEndMotionPath : Landroidx/constraintlayout/motion/widget/MotionPaths;
    //   1042: getfield height : F
    //   1045: aload_0
    //   1046: getfield mStartMotionPath : Landroidx/constraintlayout/motion/widget/MotionPaths;
    //   1049: getfield height : F
    //   1052: fcmpl
    //   1053: ifne -> 1069
    //   1056: aload_0
    //   1057: getfield mForceMeasure : Z
    //   1060: ifeq -> 1066
    //   1063: goto -> 1069
    //   1066: goto -> 1100
    //   1069: aload_1
    //   1070: iload #14
    //   1072: iload #15
    //   1074: isub
    //   1075: ldc_w 1073741824
    //   1078: invokestatic makeMeasureSpec : (II)I
    //   1081: iload #16
    //   1083: iload #17
    //   1085: isub
    //   1086: ldc_w 1073741824
    //   1089: invokestatic makeMeasureSpec : (II)I
    //   1092: invokevirtual measure : (II)V
    //   1095: aload_0
    //   1096: iconst_0
    //   1097: putfield mForceMeasure : Z
    //   1100: aload_1
    //   1101: iload #15
    //   1103: iload #17
    //   1105: iload #14
    //   1107: iload #16
    //   1109: invokevirtual layout : (IIII)V
    //   1112: iload #18
    //   1114: istore #19
    //   1116: aload_0
    //   1117: getfield mCycleMap : Ljava/util/HashMap;
    //   1120: astore #5
    //   1122: aload #5
    //   1124: ifnull -> 1210
    //   1127: aload #5
    //   1129: invokevirtual values : ()Ljava/util/Collection;
    //   1132: invokeinterface iterator : ()Ljava/util/Iterator;
    //   1137: astore #5
    //   1139: aload #5
    //   1141: invokeinterface hasNext : ()Z
    //   1146: ifeq -> 1210
    //   1149: aload #5
    //   1151: invokeinterface next : ()Ljava/lang/Object;
    //   1156: checkcast androidx/constraintlayout/motion/utils/ViewOscillator
    //   1159: astore #20
    //   1161: aload #20
    //   1163: instanceof androidx/constraintlayout/motion/utils/ViewOscillator$PathRotateSet
    //   1166: ifeq -> 1200
    //   1169: aload #20
    //   1171: checkcast androidx/constraintlayout/motion/utils/ViewOscillator$PathRotateSet
    //   1174: astore #20
    //   1176: aload_0
    //   1177: getfield mInterpolateVelocity : [D
    //   1180: astore #21
    //   1182: aload #20
    //   1184: aload_1
    //   1185: fload_2
    //   1186: aload #21
    //   1188: iconst_0
    //   1189: daload
    //   1190: aload #21
    //   1192: iconst_1
    //   1193: daload
    //   1194: invokevirtual setPathRotate : (Landroid/view/View;FDD)V
    //   1197: goto -> 1207
    //   1200: aload #20
    //   1202: aload_1
    //   1203: fload_2
    //   1204: invokevirtual setProperty : (Landroid/view/View;F)V
    //   1207: goto -> 1139
    //   1210: iload #19
    //   1212: ireturn
  }
  
  String name() {
    return this.mView.getContext().getResources().getResourceEntryName(this.mView.getId());
  }
  
  void positionKeyframe(View paramView, KeyPositionBase paramKeyPositionBase, float paramFloat1, float paramFloat2, String[] paramArrayOfString, float[] paramArrayOffloat) {
    RectF rectF1 = new RectF();
    rectF1.left = this.mStartMotionPath.x;
    rectF1.top = this.mStartMotionPath.y;
    rectF1.right = rectF1.left + this.mStartMotionPath.width;
    rectF1.bottom = rectF1.top + this.mStartMotionPath.height;
    RectF rectF2 = new RectF();
    rectF2.left = this.mEndMotionPath.x;
    rectF2.top = this.mEndMotionPath.y;
    rectF2.right = rectF2.left + this.mEndMotionPath.width;
    rectF2.bottom = rectF2.top + this.mEndMotionPath.height;
    paramKeyPositionBase.positionAttributes(paramView, rectF1, rectF2, paramFloat1, paramFloat2, paramArrayOfString, paramArrayOffloat);
  }
  
  public void remeasure() {
    this.mForceMeasure = true;
  }
  
  void rotate(Rect paramRect1, Rect paramRect2, int paramInt1, int paramInt2, int paramInt3) {
    int i;
    int j;
    switch (paramInt1) {
      default:
        return;
      case 4:
        paramInt1 = paramRect1.left;
        i = paramRect1.right;
        j = paramRect1.bottom;
        paramInt3 = paramRect1.top;
        paramRect2.left = paramInt2 - (paramRect1.width() + j + paramInt3) / 2;
        paramRect2.top = (paramInt1 + i - paramRect1.height()) / 2;
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
        paramInt3 = paramRect1.left;
        i = paramRect1.right;
        paramInt1 = paramRect1.top;
        j = paramRect1.bottom;
        paramRect2.left = paramInt2 - (paramRect1.width() + paramInt1 + j) / 2;
        paramRect2.top = (paramInt3 + i - paramRect1.height()) / 2;
        paramRect2.right = paramRect2.left + paramRect1.width();
        paramRect2.bottom = paramRect2.top + paramRect1.height();
      case 1:
        break;
    } 
    paramInt1 = paramRect1.left;
    paramInt2 = paramRect1.right;
    paramRect2.left = (paramRect1.top + paramRect1.bottom - paramRect1.width()) / 2;
    paramRect2.top = paramInt3 - (paramRect1.height() + paramInt1 + paramInt2) / 2;
    paramRect2.right = paramRect2.left + paramRect1.width();
    paramRect2.bottom = paramRect2.top + paramRect1.height();
  }
  
  void setBothStates(View paramView) {
    this.mStartMotionPath.time = 0.0F;
    this.mStartMotionPath.position = 0.0F;
    this.mNoMovement = true;
    this.mStartMotionPath.setBounds(paramView.getX(), paramView.getY(), paramView.getWidth(), paramView.getHeight());
    this.mEndMotionPath.setBounds(paramView.getX(), paramView.getY(), paramView.getWidth(), paramView.getHeight());
    this.mStartPoint.setState(paramView);
    this.mEndPoint.setState(paramView);
  }
  
  public void setDrawPath(int paramInt) {
    this.mStartMotionPath.mDrawPath = paramInt;
  }
  
  void setEndState(Rect paramRect, ConstraintSet paramConstraintSet, int paramInt1, int paramInt2) {
    int i = paramConstraintSet.mRotate;
    Rect rect = paramRect;
    if (i != 0) {
      rotate(paramRect, this.mTempRect, i, paramInt1, paramInt2);
      rect = this.mTempRect;
    } 
    this.mEndMotionPath.time = 1.0F;
    this.mEndMotionPath.position = 1.0F;
    readView(this.mEndMotionPath);
    this.mEndMotionPath.setBounds(rect.left, rect.top, rect.width(), rect.height());
    this.mEndMotionPath.applyParameters(paramConstraintSet.getParameters(this.mId));
    this.mEndPoint.setState(rect, paramConstraintSet, i, this.mId);
  }
  
  public void setPathMotionArc(int paramInt) {
    this.mPathMotionArc = paramInt;
  }
  
  void setStartCurrentState(View paramView) {
    this.mStartMotionPath.time = 0.0F;
    this.mStartMotionPath.position = 0.0F;
    this.mStartMotionPath.setBounds(paramView.getX(), paramView.getY(), paramView.getWidth(), paramView.getHeight());
    this.mStartPoint.setState(paramView);
  }
  
  void setStartState(Rect paramRect, ConstraintSet paramConstraintSet, int paramInt1, int paramInt2) {
    int i = paramConstraintSet.mRotate;
    if (i != 0)
      rotate(paramRect, this.mTempRect, i, paramInt1, paramInt2); 
    this.mStartMotionPath.time = 0.0F;
    this.mStartMotionPath.position = 0.0F;
    readView(this.mStartMotionPath);
    this.mStartMotionPath.setBounds(paramRect.left, paramRect.top, paramRect.width(), paramRect.height());
    ConstraintSet.Constraint constraint = paramConstraintSet.getParameters(this.mId);
    this.mStartMotionPath.applyParameters(constraint);
    this.mMotionStagger = constraint.motion.mMotionStagger;
    this.mStartPoint.setState(paramRect, paramConstraintSet, i, this.mId);
    this.mTransformPivotTarget = constraint.transform.transformPivotTarget;
    this.mQuantizeMotionSteps = constraint.motion.mQuantizeMotionSteps;
    this.mQuantizeMotionPhase = constraint.motion.mQuantizeMotionPhase;
    this.mQuantizeMotionInterpolator = getInterpolator(this.mView.getContext(), constraint.motion.mQuantizeInterpolatorType, constraint.motion.mQuantizeInterpolatorString, constraint.motion.mQuantizeInterpolatorID);
  }
  
  public void setStartState(ViewState paramViewState, View paramView, int paramInt1, int paramInt2, int paramInt3) {
    int i;
    int j;
    int k;
    this.mStartMotionPath.time = 0.0F;
    this.mStartMotionPath.position = 0.0F;
    Rect rect = new Rect();
    switch (paramInt1) {
      case 2:
        i = paramViewState.left;
        paramInt2 = paramViewState.right;
        j = paramViewState.top;
        k = paramViewState.bottom;
        rect.left = paramInt3 - (paramViewState.width() + j + k) / 2;
        rect.top = (i + paramInt2 - paramViewState.height()) / 2;
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
    this.mStartPoint.setState(rect, paramView, paramInt1, paramViewState.rotation);
  }
  
  public void setTransformPivotTarget(int paramInt) {
    this.mTransformPivotTarget = paramInt;
    this.mTransformPivotView = null;
  }
  
  public void setView(View paramView) {
    this.mView = paramView;
    this.mId = paramView.getId();
    ViewGroup.LayoutParams layoutParams = paramView.getLayoutParams();
    if (layoutParams instanceof ConstraintLayout.LayoutParams)
      this.mConstraintTag = ((ConstraintLayout.LayoutParams)layoutParams).getConstraintTag(); 
  }
  
  public void setup(int paramInt1, int paramInt2, float paramFloat, long paramLong) {
    ArrayList<KeyTrigger> arrayList1;
    ArrayList<KeyTrigger> arrayList2;
    HashSet<String> hashSet2 = new HashSet();
    HashSet<String> hashSet4 = new HashSet();
    HashSet<String> hashSet3 = new HashSet();
    HashSet<String> hashSet5 = new HashSet();
    HashMap<Object, Object> hashMap = new HashMap<>();
    KeyPosition keyPosition2 = null;
    KeyPosition keyPosition1 = null;
    if (this.mPathMotionArc != Key.UNSET)
      this.mStartMotionPath.mPathMotionArc = this.mPathMotionArc; 
    this.mStartPoint.different(this.mEndPoint, hashSet3);
    ArrayList<Key> arrayList = this.mKeyList;
    if (arrayList != null) {
      for (Key key : arrayList) {
        if (key instanceof KeyPosition) {
          keyPosition2 = (KeyPosition)key;
          insertKey(new MotionPaths(paramInt1, paramInt2, keyPosition2, this.mStartMotionPath, this.mEndMotionPath));
          if (keyPosition2.mCurveFit != Key.UNSET)
            this.mCurveFitType = keyPosition2.mCurveFit; 
          continue;
        } 
        if (key instanceof KeyCycle) {
          key.getAttributeNames(hashSet5);
          continue;
        } 
        if (key instanceof KeyTimeCycle) {
          key.getAttributeNames(hashSet4);
          continue;
        } 
        if (key instanceof KeyTrigger) {
          keyPosition2 = keyPosition1;
          if (keyPosition1 == null)
            arrayList2 = new ArrayList(); 
          arrayList2.add((KeyTrigger)key);
          arrayList1 = arrayList2;
          continue;
        } 
        key.setInterpolation((HashMap)hashMap);
        key.getAttributeNames(hashSet3);
      } 
    } else {
      arrayList1 = arrayList2;
    } 
    if (arrayList1 != null)
      this.mKeyTriggers = arrayList1.<KeyTrigger>toArray(new KeyTrigger[0]); 
    if (!hashSet3.isEmpty()) {
      this.mAttributesMap = new HashMap<>();
      for (String str : hashSet3) {
        ViewSpline viewSpline;
        if (str.startsWith("CUSTOM,")) {
          SparseArray sparseArray = new SparseArray();
          String str1 = str.split(",")[1];
          for (Key key : this.mKeyList) {
            if (key.mCustomConstraints == null)
              continue; 
            ConstraintAttribute constraintAttribute = key.mCustomConstraints.get(str1);
            if (constraintAttribute != null)
              sparseArray.append(key.mFramePosition, constraintAttribute); 
          } 
          viewSpline = ViewSpline.makeCustomSpline(str, sparseArray);
        } else {
          viewSpline = ViewSpline.makeSpline(str);
        } 
        if (viewSpline == null)
          continue; 
        viewSpline.setType(str);
        this.mAttributesMap.put(str, viewSpline);
      } 
      ArrayList<Key> arrayList3 = this.mKeyList;
      if (arrayList3 != null)
        for (Key key : arrayList3) {
          if (key instanceof KeyAttributes)
            key.addValues(this.mAttributesMap); 
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
        SplineSet splineSet = (SplineSet)this.mAttributesMap.get(str);
        if (splineSet != null)
          splineSet.setup(paramInt1); 
      } 
    } 
    if (!hashSet4.isEmpty()) {
      if (this.mTimeCycleAttributesMap == null)
        this.mTimeCycleAttributesMap = new HashMap<>(); 
      for (String str : hashSet4) {
        ViewTimeCycle viewTimeCycle;
        if (this.mTimeCycleAttributesMap.containsKey(str))
          continue; 
        if (str.startsWith("CUSTOM,")) {
          SparseArray sparseArray = new SparseArray();
          String str1 = str.split(",")[1];
          for (Key key : this.mKeyList) {
            if (key.mCustomConstraints == null)
              continue; 
            ConstraintAttribute constraintAttribute = key.mCustomConstraints.get(str1);
            if (constraintAttribute != null)
              sparseArray.append(key.mFramePosition, constraintAttribute); 
          } 
          viewTimeCycle = ViewTimeCycle.makeCustomSpline(str, sparseArray);
        } else {
          viewTimeCycle = ViewTimeCycle.makeSpline(str, paramLong);
        } 
        if (viewTimeCycle == null)
          continue; 
        viewTimeCycle.setType(str);
        this.mTimeCycleAttributesMap.put(str, viewTimeCycle);
      } 
      ArrayList<Key> arrayList3 = this.mKeyList;
      if (arrayList3 != null)
        for (Key key : arrayList3) {
          if (key instanceof KeyTimeCycle)
            ((KeyTimeCycle)key).addTimeValues(this.mTimeCycleAttributesMap); 
        }  
      for (String str : this.mTimeCycleAttributesMap.keySet()) {
        paramInt1 = 0;
        if (hashMap.containsKey(str))
          paramInt1 = ((Integer)hashMap.get(str)).intValue(); 
        ((ViewTimeCycle)this.mTimeCycleAttributesMap.get(str)).setup(paramInt1);
      } 
    } 
    MotionPaths[] arrayOfMotionPaths = new MotionPaths[this.mMotionPaths.size() + 2];
    paramInt1 = 1;
    arrayOfMotionPaths[0] = this.mStartMotionPath;
    arrayOfMotionPaths[arrayOfMotionPaths.length - 1] = this.mEndMotionPath;
    if (this.mMotionPaths.size() > 0 && this.mCurveFitType == -1)
      this.mCurveFitType = 0; 
    Iterator<MotionPaths> iterator = this.mMotionPaths.iterator();
    while (iterator.hasNext()) {
      arrayOfMotionPaths[paramInt1] = iterator.next();
      paramInt1++;
    } 
    int i = 18;
    hashSet2 = new HashSet();
    for (String str : this.mEndMotionPath.attributes.keySet()) {
      if (this.mStartMotionPath.attributes.containsKey(str)) {
        String str1 = String.valueOf(str);
        if (str1.length() != 0) {
          str1 = "CUSTOM,".concat(str1);
        } else {
          str1 = new String("CUSTOM,");
        } 
        if (!hashSet3.contains(str1))
          hashSet2.add(str); 
      } 
    } 
    String[] arrayOfString = (String[])hashSet2.toArray((Object[])new String[0]);
    this.mAttributeNames = arrayOfString;
    this.mAttributeInterpolatorCount = new int[arrayOfString.length];
    paramInt1 = 0;
    HashSet<String> hashSet1 = hashSet4;
    while (true) {
      boolean bool;
      String[] arrayOfString1 = this.mAttributeNames;
      if (paramInt1 < arrayOfString1.length) {
        String str = arrayOfString1[paramInt1];
        this.mAttributeInterpolatorCount[paramInt1] = 0;
        for (paramInt2 = 0; paramInt2 < arrayOfMotionPaths.length; paramInt2++) {
          if ((arrayOfMotionPaths[paramInt2]).attributes.containsKey(str)) {
            ConstraintAttribute constraintAttribute = (arrayOfMotionPaths[paramInt2]).attributes.get(str);
            if (constraintAttribute != null) {
              int[] arrayOfInt = this.mAttributeInterpolatorCount;
              arrayOfInt[paramInt1] = arrayOfInt[paramInt1] + constraintAttribute.numberOfInterpolatedValues();
              break;
            } 
          } 
        } 
        paramInt1++;
        continue;
      } 
      if ((arrayOfMotionPaths[0]).mPathMotionArc != Key.UNSET) {
        bool = true;
      } else {
        bool = false;
      } 
      boolean[] arrayOfBoolean2 = new boolean[this.mAttributeNames.length + 18];
      paramInt1 = 1;
      hashSet1 = hashSet3;
      while (paramInt1 < arrayOfMotionPaths.length) {
        arrayOfMotionPaths[paramInt1].different(arrayOfMotionPaths[paramInt1 - 1], arrayOfBoolean2, this.mAttributeNames, bool);
        paramInt1++;
      } 
      paramInt2 = 0;
      paramInt1 = 1;
      while (paramInt1 < arrayOfBoolean2.length) {
        int k = paramInt2;
        if (arrayOfBoolean2[paramInt1])
          k = paramInt2 + 1; 
        paramInt1++;
        paramInt2 = k;
      } 
      this.mInterpolateVariables = new int[paramInt2];
      int j = Math.max(2, paramInt2);
      this.mInterpolateData = new double[j];
      this.mInterpolateVelocity = new double[j];
      paramInt1 = 0;
      paramInt2 = 1;
      while (paramInt2 < arrayOfBoolean2.length) {
        int k = paramInt1;
        if (arrayOfBoolean2[paramInt2]) {
          this.mInterpolateVariables[paramInt1] = paramInt2;
          k = paramInt1 + 1;
        } 
        paramInt2++;
        paramInt1 = k;
      } 
      double[][] arrayOfDouble1 = new double[arrayOfMotionPaths.length][this.mInterpolateVariables.length];
      double[] arrayOfDouble = new double[arrayOfMotionPaths.length];
      paramInt2 = 0;
      boolean[] arrayOfBoolean1 = arrayOfBoolean2;
      HashMap<Object, Object> hashMap1 = hashMap;
      while (paramInt2 < arrayOfMotionPaths.length) {
        arrayOfMotionPaths[paramInt2].fillStandard(arrayOfDouble1[paramInt2], this.mInterpolateVariables);
        arrayOfDouble[paramInt2] = (arrayOfMotionPaths[paramInt2]).time;
        paramInt2++;
      } 
      byte b = 0;
      paramInt2 = j;
      paramInt1 = i;
      while (true) {
        int[] arrayOfInt = this.mInterpolateVariables;
        if (b < arrayOfInt.length) {
          i = arrayOfInt[b];
          if (i < MotionPaths.names.length) {
            String str = String.valueOf(MotionPaths.names[this.mInterpolateVariables[b]]).concat(" [");
            for (j = 0; j < arrayOfMotionPaths.length; j++) {
              str = String.valueOf(str);
              double d = arrayOfDouble1[j][b];
              str = (new StringBuilder(String.valueOf(str).length() + 24)).append(str).append(d).toString();
            } 
            i = paramInt1;
            paramInt1 = paramInt2;
            paramInt2 = i;
          } else {
            i = paramInt1;
            paramInt1 = paramInt2;
            paramInt2 = i;
          } 
          b++;
          i = paramInt1;
          paramInt1 = paramInt2;
          paramInt2 = i;
          continue;
        } 
        this.mSpline = new CurveFit[this.mAttributeNames.length + 1];
        paramInt1 = 0;
        HashSet<String> hashSet = hashSet2;
        while (true) {
          String[] arrayOfString2 = this.mAttributeNames;
          if (paramInt1 < arrayOfString2.length) {
            b = 0;
            hashMap1 = null;
            hashSet2 = null;
            String str = arrayOfString2[paramInt1];
            for (paramInt2 = 0; paramInt2 < arrayOfMotionPaths.length; paramInt2++) {
              if (arrayOfMotionPaths[paramInt2].hasCustomData(str)) {
                if (hashMap1 == null) {
                  arrayOfDouble3 = new double[arrayOfMotionPaths.length];
                  arrayOfDouble2 = new double[arrayOfMotionPaths.length][arrayOfMotionPaths[paramInt2].getCustomDataCount(str)];
                } 
                arrayOfDouble3[b] = (arrayOfMotionPaths[paramInt2]).time;
                arrayOfMotionPaths[paramInt2].getCustomData(str, arrayOfDouble2[b], 0);
                b++;
              } 
            } 
            double[] arrayOfDouble3 = Arrays.copyOf(arrayOfDouble3, b);
            double[][] arrayOfDouble2 = Arrays.<double[]>copyOf(arrayOfDouble2, b);
            this.mSpline[paramInt1 + 1] = CurveFit.get(this.mCurveFitType, arrayOfDouble3, arrayOfDouble2);
            paramInt1++;
            continue;
          } 
          this.mSpline[0] = CurveFit.get(this.mCurveFitType, arrayOfDouble, arrayOfDouble1);
          if ((arrayOfMotionPaths[0]).mPathMotionArc != Key.UNSET) {
            paramInt2 = arrayOfMotionPaths.length;
            int[] arrayOfInt1 = new int[paramInt2];
            double[] arrayOfDouble3 = new double[paramInt2];
            double[][] arrayOfDouble2 = new double[paramInt2][2];
            for (paramInt1 = 0; paramInt1 < paramInt2; paramInt1++) {
              arrayOfInt1[paramInt1] = (arrayOfMotionPaths[paramInt1]).mPathMotionArc;
              arrayOfDouble3[paramInt1] = (arrayOfMotionPaths[paramInt1]).time;
              arrayOfDouble2[paramInt1][0] = (arrayOfMotionPaths[paramInt1]).x;
              arrayOfDouble2[paramInt1][1] = (arrayOfMotionPaths[paramInt1]).y;
            } 
            this.mArcSpline = CurveFit.getArc(arrayOfInt1, arrayOfDouble3, arrayOfDouble2);
          } 
          float f = Float.NaN;
          this.mCycleMap = new HashMap<>();
          if (this.mKeyList != null) {
            for (String str : hashSet5) {
              ViewOscillator viewOscillator = ViewOscillator.makeSpline(str);
              if (viewOscillator == null)
                continue; 
              paramFloat = f;
              if (viewOscillator.variesByPath()) {
                paramFloat = f;
                if (Float.isNaN(f))
                  paramFloat = getPreCycleDistance(); 
              } 
              viewOscillator.setType(str);
              this.mCycleMap.put(str, viewOscillator);
              f = paramFloat;
            } 
            for (Key key : this.mKeyList) {
              if (key instanceof KeyCycle)
                ((KeyCycle)key).addCycleValues(this.mCycleMap); 
            } 
            Iterator<ViewOscillator> iterator1 = this.mCycleMap.values().iterator();
            while (iterator1.hasNext())
              ((ViewOscillator)iterator1.next()).setup(f); 
          } 
          return;
        } 
        break;
      } 
      break;
    } 
  }
  
  public void setupRelative(MotionController paramMotionController) {
    this.mStartMotionPath.setupRelative(paramMotionController, paramMotionController.mStartMotionPath);
    this.mEndMotionPath.setupRelative(paramMotionController, paramMotionController.mEndMotionPath);
  }
  
  public String toString() {
    float f2 = this.mStartMotionPath.x;
    float f4 = this.mStartMotionPath.y;
    float f1 = this.mEndMotionPath.x;
    float f3 = this.mEndMotionPath.y;
    return (new StringBuilder(88)).append(" start: x: ").append(f2).append(" y: ").append(f4).append(" end: x: ").append(f1).append(" y: ").append(f3).toString();
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\motion\widget\MotionController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */