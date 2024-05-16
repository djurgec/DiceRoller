package androidx.constraintlayout.core.motion;

import androidx.constraintlayout.core.motion.utils.Easing;
import androidx.constraintlayout.core.motion.utils.Rect;
import androidx.constraintlayout.core.motion.utils.SplineSet;
import androidx.constraintlayout.core.motion.utils.Utils;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;

class MotionConstrainedPoint implements Comparable<MotionConstrainedPoint> {
  static final int CARTESIAN = 2;
  
  public static final boolean DEBUG = false;
  
  static final int PERPENDICULAR = 1;
  
  public static final String TAG = "MotionPaths";
  
  static String[] names = new String[] { "position", "x", "y", "width", "height", "pathRotate" };
  
  private float alpha = 1.0F;
  
  private boolean applyElevation = false;
  
  private float elevation = 0.0F;
  
  private float height;
  
  private int mAnimateRelativeTo = -1;
  
  LinkedHashMap<String, CustomVariable> mCustomVariable = new LinkedHashMap<>();
  
  private int mDrawPath = 0;
  
  private Easing mKeyFrameEasing;
  
  int mMode = 0;
  
  private float mPathRotate = Float.NaN;
  
  private float mPivotX = Float.NaN;
  
  private float mPivotY = Float.NaN;
  
  private float mProgress = Float.NaN;
  
  double[] mTempDelta = new double[18];
  
  double[] mTempValue = new double[18];
  
  int mVisibilityMode = 0;
  
  private float position;
  
  private float rotation = 0.0F;
  
  private float rotationX = 0.0F;
  
  public float rotationY = 0.0F;
  
  private float scaleX = 1.0F;
  
  private float scaleY = 1.0F;
  
  private float translationX = 0.0F;
  
  private float translationY = 0.0F;
  
  private float translationZ = 0.0F;
  
  int visibility;
  
  private float width;
  
  private float x;
  
  private float y;
  
  private boolean diff(float paramFloat1, float paramFloat2) {
    boolean bool = Float.isNaN(paramFloat1);
    boolean bool1 = true;
    boolean bool2 = true;
    if (bool || Float.isNaN(paramFloat2)) {
      if (Float.isNaN(paramFloat1) == Float.isNaN(paramFloat2))
        bool1 = false; 
      return bool1;
    } 
    if (Math.abs(paramFloat1 - paramFloat2) > 1.0E-6F) {
      bool1 = bool2;
    } else {
      bool1 = false;
    } 
    return bool1;
  }
  
  public void addValues(HashMap<String, SplineSet> paramHashMap, int paramInt) {
    for (String str : paramHashMap.keySet()) {
      SplineSet splineSet = paramHashMap.get(str);
      byte b = -1;
      switch (str.hashCode()) {
        case 803192288:
          if (str.equals("pathRotate"))
            b = 6; 
          break;
        case 92909918:
          if (str.equals("alpha"))
            b = 0; 
          break;
        case -908189617:
          if (str.equals("scaleY"))
            b = 9; 
          break;
        case -908189618:
          if (str.equals("scaleX"))
            b = 8; 
          break;
        case -987906985:
          if (str.equals("pivotY"))
            b = 5; 
          break;
        case -987906986:
          if (str.equals("pivotX"))
            b = 4; 
          break;
        case -1001078227:
          if (str.equals("progress"))
            b = 7; 
          break;
        case -1225497655:
          if (str.equals("translationZ"))
            b = 12; 
          break;
        case -1225497656:
          if (str.equals("translationY"))
            b = 11; 
          break;
        case -1225497657:
          if (str.equals("translationX"))
            b = 10; 
          break;
        case -1249320804:
          if (str.equals("rotationZ"))
            b = 1; 
          break;
        case -1249320805:
          if (str.equals("rotationY"))
            b = 3; 
          break;
        case -1249320806:
          if (str.equals("rotationX"))
            b = 2; 
          break;
      } 
      float f1 = 1.0F;
      float f5 = 0.0F;
      float f6 = 0.0F;
      float f3 = 0.0F;
      float f9 = 0.0F;
      float f10 = 0.0F;
      float f8 = 0.0F;
      float f4 = 0.0F;
      float f2 = 0.0F;
      float f11 = 0.0F;
      float f7 = 0.0F;
      switch (b) {
        default:
          if (str.startsWith("CUSTOM")) {
            String str1 = str.split(",")[1];
            if (this.mCustomVariable.containsKey(str1)) {
              CustomVariable customVariable = this.mCustomVariable.get(str1);
              if (splineSet instanceof SplineSet.CustomSpline) {
                ((SplineSet.CustomSpline)splineSet).setPoint(paramInt, customVariable);
                continue;
              } 
              Utils.loge("MotionPaths", str + " ViewSpline not a CustomSet frame = " + paramInt + ", value" + customVariable.getValueToInterpolate() + splineSet);
            } 
            continue;
          } 
          break;
        case 12:
          if (Float.isNaN(this.translationZ)) {
            f1 = f7;
          } else {
            f1 = this.translationZ;
          } 
          splineSet.setPoint(paramInt, f1);
          continue;
        case 11:
          if (Float.isNaN(this.translationY)) {
            f1 = f5;
          } else {
            f1 = this.translationY;
          } 
          splineSet.setPoint(paramInt, f1);
          continue;
        case 10:
          if (Float.isNaN(this.translationX)) {
            f1 = f6;
          } else {
            f1 = this.translationX;
          } 
          splineSet.setPoint(paramInt, f1);
          continue;
        case 9:
          if (!Float.isNaN(this.scaleY))
            f1 = this.scaleY; 
          splineSet.setPoint(paramInt, f1);
          continue;
        case 8:
          if (!Float.isNaN(this.scaleX))
            f1 = this.scaleX; 
          splineSet.setPoint(paramInt, f1);
          continue;
        case 7:
          if (Float.isNaN(this.mProgress)) {
            f1 = f3;
          } else {
            f1 = this.mProgress;
          } 
          splineSet.setPoint(paramInt, f1);
          continue;
        case 6:
          if (Float.isNaN(this.mPathRotate)) {
            f1 = f9;
          } else {
            f1 = this.mPathRotate;
          } 
          splineSet.setPoint(paramInt, f1);
          continue;
        case 5:
          if (Float.isNaN(this.mPivotY)) {
            f1 = f10;
          } else {
            f1 = this.mPivotY;
          } 
          splineSet.setPoint(paramInt, f1);
          continue;
        case 4:
          if (Float.isNaN(this.mPivotX)) {
            f1 = f8;
          } else {
            f1 = this.mPivotX;
          } 
          splineSet.setPoint(paramInt, f1);
          continue;
        case 3:
          if (Float.isNaN(this.rotationY)) {
            f1 = f4;
          } else {
            f1 = this.rotationY;
          } 
          splineSet.setPoint(paramInt, f1);
          continue;
        case 2:
          if (Float.isNaN(this.rotationX)) {
            f1 = f2;
          } else {
            f1 = this.rotationX;
          } 
          splineSet.setPoint(paramInt, f1);
          continue;
        case 1:
          if (Float.isNaN(this.rotation)) {
            f1 = f11;
          } else {
            f1 = this.rotation;
          } 
          splineSet.setPoint(paramInt, f1);
          continue;
        case 0:
          if (!Float.isNaN(this.alpha))
            f1 = this.alpha; 
          splineSet.setPoint(paramInt, f1);
          continue;
      } 
      Utils.loge("MotionPaths", "UNKNOWN spline " + str);
    } 
  }
  
  public void applyParameters(MotionWidget paramMotionWidget) {
    float f;
    this.visibility = paramMotionWidget.getVisibility();
    if (paramMotionWidget.getVisibility() != 4) {
      f = 0.0F;
    } else {
      f = paramMotionWidget.getAlpha();
    } 
    this.alpha = f;
    this.applyElevation = false;
    this.rotation = paramMotionWidget.getRotationZ();
    this.rotationX = paramMotionWidget.getRotationX();
    this.rotationY = paramMotionWidget.getRotationY();
    this.scaleX = paramMotionWidget.getScaleX();
    this.scaleY = paramMotionWidget.getScaleY();
    this.mPivotX = paramMotionWidget.getPivotX();
    this.mPivotY = paramMotionWidget.getPivotY();
    this.translationX = paramMotionWidget.getTranslationX();
    this.translationY = paramMotionWidget.getTranslationY();
    this.translationZ = paramMotionWidget.getTranslationZ();
    for (String str : paramMotionWidget.getCustomAttributeNames()) {
      CustomVariable customVariable = paramMotionWidget.getCustomAttribute(str);
      if (customVariable != null && customVariable.isContinuous())
        this.mCustomVariable.put(str, customVariable); 
    } 
  }
  
  public int compareTo(MotionConstrainedPoint paramMotionConstrainedPoint) {
    return Float.compare(this.position, paramMotionConstrainedPoint.position);
  }
  
  void different(MotionConstrainedPoint paramMotionConstrainedPoint, HashSet<String> paramHashSet) {
    if (diff(this.alpha, paramMotionConstrainedPoint.alpha))
      paramHashSet.add("alpha"); 
    if (diff(this.elevation, paramMotionConstrainedPoint.elevation))
      paramHashSet.add("translationZ"); 
    int i = this.visibility;
    int j = paramMotionConstrainedPoint.visibility;
    if (i != j && this.mVisibilityMode == 0 && (i == 4 || j == 4))
      paramHashSet.add("alpha"); 
    if (diff(this.rotation, paramMotionConstrainedPoint.rotation))
      paramHashSet.add("rotationZ"); 
    if (!Float.isNaN(this.mPathRotate) || !Float.isNaN(paramMotionConstrainedPoint.mPathRotate))
      paramHashSet.add("pathRotate"); 
    if (!Float.isNaN(this.mProgress) || !Float.isNaN(paramMotionConstrainedPoint.mProgress))
      paramHashSet.add("progress"); 
    if (diff(this.rotationX, paramMotionConstrainedPoint.rotationX))
      paramHashSet.add("rotationX"); 
    if (diff(this.rotationY, paramMotionConstrainedPoint.rotationY))
      paramHashSet.add("rotationY"); 
    if (diff(this.mPivotX, paramMotionConstrainedPoint.mPivotX))
      paramHashSet.add("pivotX"); 
    if (diff(this.mPivotY, paramMotionConstrainedPoint.mPivotY))
      paramHashSet.add("pivotY"); 
    if (diff(this.scaleX, paramMotionConstrainedPoint.scaleX))
      paramHashSet.add("scaleX"); 
    if (diff(this.scaleY, paramMotionConstrainedPoint.scaleY))
      paramHashSet.add("scaleY"); 
    if (diff(this.translationX, paramMotionConstrainedPoint.translationX))
      paramHashSet.add("translationX"); 
    if (diff(this.translationY, paramMotionConstrainedPoint.translationY))
      paramHashSet.add("translationY"); 
    if (diff(this.translationZ, paramMotionConstrainedPoint.translationZ))
      paramHashSet.add("translationZ"); 
    if (diff(this.elevation, paramMotionConstrainedPoint.elevation))
      paramHashSet.add("elevation"); 
  }
  
  void different(MotionConstrainedPoint paramMotionConstrainedPoint, boolean[] paramArrayOfboolean, String[] paramArrayOfString) {
    int j = 0 + 1;
    paramArrayOfboolean[0] = paramArrayOfboolean[0] | diff(this.position, paramMotionConstrainedPoint.position);
    int i = j + 1;
    paramArrayOfboolean[j] = paramArrayOfboolean[j] | diff(this.x, paramMotionConstrainedPoint.x);
    j = i + 1;
    paramArrayOfboolean[i] = paramArrayOfboolean[i] | diff(this.y, paramMotionConstrainedPoint.y);
    i = j + 1;
    paramArrayOfboolean[j] = paramArrayOfboolean[j] | diff(this.width, paramMotionConstrainedPoint.width);
    paramArrayOfboolean[i] = paramArrayOfboolean[i] | diff(this.height, paramMotionConstrainedPoint.height);
  }
  
  void fillStandard(double[] paramArrayOfdouble, int[] paramArrayOfint) {
    float[] arrayOfFloat = new float[18];
    arrayOfFloat[0] = this.position;
    arrayOfFloat[1] = this.x;
    arrayOfFloat[2] = this.y;
    arrayOfFloat[3] = this.width;
    arrayOfFloat[4] = this.height;
    arrayOfFloat[5] = this.alpha;
    arrayOfFloat[6] = this.elevation;
    arrayOfFloat[7] = this.rotation;
    arrayOfFloat[8] = this.rotationX;
    arrayOfFloat[9] = this.rotationY;
    arrayOfFloat[10] = this.scaleX;
    arrayOfFloat[11] = this.scaleY;
    arrayOfFloat[12] = this.mPivotX;
    arrayOfFloat[13] = this.mPivotY;
    arrayOfFloat[14] = this.translationX;
    arrayOfFloat[15] = this.translationY;
    arrayOfFloat[16] = this.translationZ;
    arrayOfFloat[17] = this.mPathRotate;
    int i = 0;
    byte b = 0;
    while (b < paramArrayOfint.length) {
      int j = i;
      if (paramArrayOfint[b] < arrayOfFloat.length) {
        paramArrayOfdouble[i] = arrayOfFloat[paramArrayOfint[b]];
        j = i + 1;
      } 
      b++;
      i = j;
    } 
  }
  
  int getCustomData(String paramString, double[] paramArrayOfdouble, int paramInt) {
    CustomVariable customVariable = this.mCustomVariable.get(paramString);
    if (customVariable.numberOfInterpolatedValues() == 1) {
      paramArrayOfdouble[paramInt] = customVariable.getValueToInterpolate();
      return 1;
    } 
    int i = customVariable.numberOfInterpolatedValues();
    float[] arrayOfFloat = new float[i];
    customVariable.getValuesToInterpolate(arrayOfFloat);
    byte b = 0;
    while (b < i) {
      paramArrayOfdouble[paramInt] = arrayOfFloat[b];
      b++;
      paramInt++;
    } 
    return i;
  }
  
  int getCustomDataCount(String paramString) {
    return ((CustomVariable)this.mCustomVariable.get(paramString)).numberOfInterpolatedValues();
  }
  
  boolean hasCustomData(String paramString) {
    return this.mCustomVariable.containsKey(paramString);
  }
  
  void setBounds(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
    this.x = paramFloat1;
    this.y = paramFloat2;
    this.width = paramFloat3;
    this.height = paramFloat4;
  }
  
  public void setState(MotionWidget paramMotionWidget) {
    setBounds(paramMotionWidget.getX(), paramMotionWidget.getY(), paramMotionWidget.getWidth(), paramMotionWidget.getHeight());
    applyParameters(paramMotionWidget);
  }
  
  public void setState(Rect paramRect, MotionWidget paramMotionWidget, int paramInt, float paramFloat) {
    setBounds(paramRect.left, paramRect.top, paramRect.width(), paramRect.height());
    applyParameters(paramMotionWidget);
    this.mPivotX = Float.NaN;
    this.mPivotY = Float.NaN;
    switch (paramInt) {
      default:
        return;
      case 2:
        this.rotation = 90.0F + paramFloat;
      case 1:
        break;
    } 
    this.rotation = paramFloat - 90.0F;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\motion\MotionConstrainedPoint.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */