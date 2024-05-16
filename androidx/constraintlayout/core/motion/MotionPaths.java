package androidx.constraintlayout.core.motion;

import androidx.constraintlayout.core.motion.key.MotionKeyPosition;
import androidx.constraintlayout.core.motion.utils.Easing;
import java.util.Arrays;
import java.util.HashMap;

public class MotionPaths implements Comparable<MotionPaths> {
  public static final int CARTESIAN = 0;
  
  public static final boolean DEBUG = false;
  
  static final int OFF_HEIGHT = 4;
  
  static final int OFF_PATH_ROTATE = 5;
  
  static final int OFF_POSITION = 0;
  
  static final int OFF_WIDTH = 3;
  
  static final int OFF_X = 1;
  
  static final int OFF_Y = 2;
  
  public static final boolean OLD_WAY = false;
  
  public static final int PERPENDICULAR = 1;
  
  public static final int SCREEN = 2;
  
  public static final String TAG = "MotionPaths";
  
  static String[] names = new String[] { "position", "x", "y", "width", "height", "pathRotate" };
  
  HashMap<String, CustomVariable> customAttributes = new HashMap<>();
  
  float height;
  
  int mAnimateCircleAngleTo;
  
  int mAnimateRelativeTo = -1;
  
  int mDrawPath = 0;
  
  Easing mKeyFrameEasing;
  
  int mMode = 0;
  
  int mPathMotionArc = -1;
  
  float mPathRotate = Float.NaN;
  
  float mProgress = Float.NaN;
  
  float mRelativeAngle = Float.NaN;
  
  Motion mRelativeToController = null;
  
  double[] mTempDelta = new double[18];
  
  double[] mTempValue = new double[18];
  
  float position;
  
  float time;
  
  float width;
  
  float x;
  
  float y;
  
  public MotionPaths() {}
  
  public MotionPaths(int paramInt1, int paramInt2, MotionKeyPosition paramMotionKeyPosition, MotionPaths paramMotionPaths1, MotionPaths paramMotionPaths2) {
    if (paramMotionPaths1.mAnimateRelativeTo != -1) {
      initPolar(paramInt1, paramInt2, paramMotionKeyPosition, paramMotionPaths1, paramMotionPaths2);
      return;
    } 
    switch (paramMotionKeyPosition.mPositionType) {
      default:
        initCartesian(paramMotionKeyPosition, paramMotionPaths1, paramMotionPaths2);
        return;
      case 2:
        initScreen(paramInt1, paramInt2, paramMotionKeyPosition, paramMotionPaths1, paramMotionPaths2);
        return;
      case 1:
        break;
    } 
    initPath(paramMotionKeyPosition, paramMotionPaths1, paramMotionPaths2);
  }
  
  private boolean diff(float paramFloat1, float paramFloat2) {
    boolean bool = Float.isNaN(paramFloat1);
    boolean bool2 = true;
    boolean bool1 = true;
    if (bool || Float.isNaN(paramFloat2)) {
      if (Float.isNaN(paramFloat1) != Float.isNaN(paramFloat2)) {
        bool1 = bool2;
      } else {
        bool1 = false;
      } 
      return bool1;
    } 
    if (Math.abs(paramFloat1 - paramFloat2) <= 1.0E-6F)
      bool1 = false; 
    return bool1;
  }
  
  private static final float xRotate(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6) {
    return (paramFloat5 - paramFloat3) * paramFloat2 - (paramFloat6 - paramFloat4) * paramFloat1 + paramFloat3;
  }
  
  private static final float yRotate(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6) {
    return (paramFloat5 - paramFloat3) * paramFloat1 + (paramFloat6 - paramFloat4) * paramFloat2 + paramFloat4;
  }
  
  public void applyParameters(MotionWidget paramMotionWidget) {
    this.mKeyFrameEasing = Easing.getInterpolator(paramMotionWidget.motion.mTransitionEasing);
    this.mPathMotionArc = paramMotionWidget.motion.mPathMotionArc;
    this.mAnimateRelativeTo = paramMotionWidget.motion.mAnimateRelativeTo;
    this.mPathRotate = paramMotionWidget.motion.mPathRotate;
    this.mDrawPath = paramMotionWidget.motion.mDrawPath;
    this.mAnimateCircleAngleTo = paramMotionWidget.motion.mAnimateCircleAngleTo;
    this.mProgress = paramMotionWidget.propertySet.mProgress;
    this.mRelativeAngle = 0.0F;
    for (String str : paramMotionWidget.getCustomAttributeNames()) {
      CustomVariable customVariable = paramMotionWidget.getCustomAttribute(str);
      if (customVariable != null && customVariable.isContinuous())
        this.customAttributes.put(str, customVariable); 
    } 
  }
  
  public int compareTo(MotionPaths paramMotionPaths) {
    return Float.compare(this.position, paramMotionPaths.position);
  }
  
  public void configureRelativeTo(Motion paramMotion) {
    paramMotion.getPos(this.mProgress);
  }
  
  void different(MotionPaths paramMotionPaths, boolean[] paramArrayOfboolean, String[] paramArrayOfString, boolean paramBoolean) {
    boolean bool1 = diff(this.x, paramMotionPaths.x);
    boolean bool2 = diff(this.y, paramMotionPaths.y);
    int i = 0 + 1;
    paramArrayOfboolean[0] = paramArrayOfboolean[0] | diff(this.position, paramMotionPaths.position);
    int j = i + 1;
    paramArrayOfboolean[i] = paramArrayOfboolean[i] | bool1 | bool2 | paramBoolean;
    i = j + 1;
    paramArrayOfboolean[j] = paramArrayOfboolean[j] | bool1 | bool2 | paramBoolean;
    j = i + 1;
    paramArrayOfboolean[i] = paramArrayOfboolean[i] | diff(this.width, paramMotionPaths.width);
    paramArrayOfboolean[j] = paramArrayOfboolean[j] | diff(this.height, paramMotionPaths.height);
  }
  
  void fillStandard(double[] paramArrayOfdouble, int[] paramArrayOfint) {
    float[] arrayOfFloat = new float[6];
    arrayOfFloat[0] = this.position;
    arrayOfFloat[1] = this.x;
    arrayOfFloat[2] = this.y;
    arrayOfFloat[3] = this.width;
    arrayOfFloat[4] = this.height;
    arrayOfFloat[5] = this.mPathRotate;
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
  
  void getBounds(int[] paramArrayOfint, double[] paramArrayOfdouble, float[] paramArrayOffloat, int paramInt) {
    float f1 = this.x;
    f1 = this.y;
    float f2 = this.width;
    f1 = this.height;
    for (byte b = 0; b < paramArrayOfint.length; b++) {
      float f = (float)paramArrayOfdouble[b];
      switch (paramArrayOfint[b]) {
        case 4:
          f1 = f;
          break;
        case 3:
          f2 = f;
          break;
      } 
    } 
    paramArrayOffloat[paramInt] = f2;
    paramArrayOffloat[paramInt + 1] = f1;
  }
  
  void getCenter(double paramDouble, int[] paramArrayOfint, double[] paramArrayOfdouble, float[] paramArrayOffloat, int paramInt) {
    float f2 = this.x;
    float f1 = this.y;
    float f4 = this.width;
    float f3 = this.height;
    for (byte b = 0; b < paramArrayOfint.length; b++) {
      float f = (float)paramArrayOfdouble[b];
      switch (paramArrayOfint[b]) {
        case 4:
          f3 = f;
          break;
        case 3:
          f4 = f;
          break;
        case 2:
          f1 = f;
          break;
        case 1:
          f2 = f;
          break;
      } 
    } 
    Motion motion = this.mRelativeToController;
    if (motion != null) {
      float[] arrayOfFloat = new float[2];
      motion.getCenter(paramDouble, arrayOfFloat, new float[2]);
      float f5 = arrayOfFloat[0];
      float f6 = arrayOfFloat[1];
      f5 = (float)(f5 + f2 * Math.sin(f1) - (f4 / 2.0F));
      f1 = (float)(f6 - f2 * Math.cos(f1) - (f3 / 2.0F));
      f2 = f5;
    } 
    paramArrayOffloat[paramInt] = f4 / 2.0F + f2 + 0.0F;
    paramArrayOffloat[paramInt + 1] = f3 / 2.0F + f1 + 0.0F;
  }
  
  void getCenter(double paramDouble, int[] paramArrayOfint, double[] paramArrayOfdouble1, float[] paramArrayOffloat1, double[] paramArrayOfdouble2, float[] paramArrayOffloat2) {
    float f4 = this.x;
    float f3 = this.y;
    float f6 = this.width;
    float f5 = this.height;
    float f8 = 0.0F;
    float f7 = 0.0F;
    float f10 = 0.0F;
    float f9 = 0.0F;
    for (byte b = 0; b < paramArrayOfint.length; b++) {
      float f12 = (float)paramArrayOfdouble1[b];
      float f11 = (float)paramArrayOfdouble2[b];
      switch (paramArrayOfint[b]) {
        case 4:
          f5 = f12;
          f9 = f11;
          break;
        case 3:
          f6 = f12;
          f10 = f11;
          break;
        case 2:
          f3 = f12;
          f7 = f11;
          break;
        case 1:
          f8 = f11;
          f4 = f12;
          break;
      } 
    } 
    float f1 = f10 / 2.0F + f8;
    float f2 = f9 / 2.0F + f7;
    Motion motion = this.mRelativeToController;
    if (motion != null) {
      float[] arrayOfFloat2 = new float[2];
      float[] arrayOfFloat1 = new float[2];
      motion.getCenter(paramDouble, arrayOfFloat2, arrayOfFloat1);
      f2 = arrayOfFloat2[0];
      f1 = arrayOfFloat2[1];
      f10 = arrayOfFloat1[0];
      f9 = arrayOfFloat1[1];
      f2 = (float)(f2 + f4 * Math.sin(f3) - (f6 / 2.0F));
      f1 = (float)(f1 - f4 * Math.cos(f3) - (f5 / 2.0F));
      f4 = (float)(f10 + f8 * Math.sin(f3) + Math.cos(f3) * f7);
      f7 = (float)(f9 - f8 * Math.cos(f3) + Math.sin(f3) * f7);
      f3 = f1;
      f1 = f4;
      f4 = f2;
      f2 = f7;
    } 
    paramArrayOffloat1[0] = f6 / 2.0F + f4 + 0.0F;
    paramArrayOffloat1[1] = f5 / 2.0F + f3 + 0.0F;
    paramArrayOffloat2[0] = f1;
    paramArrayOffloat2[1] = f2;
  }
  
  void getCenterVelocity(double paramDouble, int[] paramArrayOfint, double[] paramArrayOfdouble, float[] paramArrayOffloat, int paramInt) {
    float f1 = this.x;
    float f2 = this.y;
    float f4 = this.width;
    float f3 = this.height;
    for (byte b = 0; b < paramArrayOfint.length; b++) {
      float f = (float)paramArrayOfdouble[b];
      switch (paramArrayOfint[b]) {
        case 4:
          f3 = f;
          break;
        case 3:
          f4 = f;
          break;
        case 2:
          f2 = f;
          break;
        case 1:
          f1 = f;
          break;
      } 
    } 
    Motion motion = this.mRelativeToController;
    if (motion != null) {
      float[] arrayOfFloat = new float[2];
      motion.getCenter(paramDouble, arrayOfFloat, new float[2]);
      float f5 = arrayOfFloat[0];
      float f6 = arrayOfFloat[1];
      f5 = (float)(f5 + f1 * Math.sin(f2) - (f4 / 2.0F));
      f2 = (float)(f6 - f1 * Math.cos(f2) - (f3 / 2.0F));
      f1 = f5;
    } 
    paramArrayOffloat[paramInt] = f4 / 2.0F + f1 + 0.0F;
    paramArrayOffloat[paramInt + 1] = f3 / 2.0F + f2 + 0.0F;
  }
  
  int getCustomData(String paramString, double[] paramArrayOfdouble, int paramInt) {
    CustomVariable customVariable = this.customAttributes.get(paramString);
    if (customVariable == null)
      return 0; 
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
    CustomVariable customVariable = this.customAttributes.get(paramString);
    return (customVariable == null) ? 0 : customVariable.numberOfInterpolatedValues();
  }
  
  void getRect(int[] paramArrayOfint, double[] paramArrayOfdouble, float[] paramArrayOffloat, int paramInt) {
    float f2 = this.x;
    float f3 = this.y;
    float f7 = this.width;
    float f4 = this.height;
    boolean bool2 = false;
    boolean bool1 = false;
    int i = 0;
    while (i < paramArrayOfint.length) {
      float f = (float)paramArrayOfdouble[i];
      switch (paramArrayOfint[i]) {
        default:
          f = f4;
          break;
        case 4:
          break;
        case 3:
          f7 = f;
          f = f4;
          break;
        case 2:
          f3 = f;
          f = f4;
          break;
        case 1:
          f2 = f;
          f = f4;
          break;
        case 0:
          f = f4;
          break;
      } 
      i++;
      f4 = f;
    } 
    Motion motion = this.mRelativeToController;
    if (motion != null) {
      float f16 = motion.getCenterX();
      float f17 = this.mRelativeToController.getCenterY();
      f16 = (float)(f16 + f2 * Math.sin(f3) - (f7 / 2.0F));
      f3 = (float)(f17 - f2 * Math.cos(f3) - (f4 / 2.0F));
      f2 = f16;
    } 
    float f6 = f2;
    float f5 = f3;
    float f15 = f2 + f7;
    float f14 = f5;
    float f9 = f15;
    float f13 = f3 + f4;
    float f8 = f6;
    float f12 = f13;
    float f10 = f6 + f7 / 2.0F;
    float f11 = f5 + f4 / 2.0F;
    if (!Float.isNaN(Float.NaN))
      f10 = f6 + (f15 - f6) * Float.NaN; 
    if (!Float.isNaN(Float.NaN))
      f11 = f5 + (f13 - f5) * Float.NaN; 
    f4 = f6;
    f3 = f15;
    f2 = f9;
    float f1 = f8;
    if (1.0F != 1.0F) {
      f1 = (f6 + f15) / 2.0F;
      f4 = (f6 - f1) * 1.0F + f1;
      f3 = (f15 - f1) * 1.0F + f1;
      f2 = (f9 - f1) * 1.0F + f1;
      f1 = (f8 - f1) * 1.0F + f1;
    } 
    f9 = f5;
    f8 = f14;
    f7 = f13;
    f6 = f12;
    if (1.0F != 1.0F) {
      f6 = (f5 + f13) / 2.0F;
      f9 = (f5 - f6) * 1.0F + f6;
      f8 = (f14 - f6) * 1.0F + f6;
      f7 = (f13 - f6) * 1.0F + f6;
      f6 = (f12 - f6) * 1.0F + f6;
    } 
    if (0.0F != 0.0F) {
      f13 = (float)Math.sin(Math.toRadians(0.0F));
      f14 = (float)Math.cos(Math.toRadians(0.0F));
      f5 = xRotate(f13, f14, f10, f11, f4, f9);
      f9 = yRotate(f13, f14, f10, f11, f4, f9);
      f4 = xRotate(f13, f14, f10, f11, f3, f8);
      f12 = yRotate(f13, f14, f10, f11, f3, f8);
      f3 = xRotate(f13, f14, f10, f11, f2, f7);
      f8 = yRotate(f13, f14, f10, f11, f2, f7);
      f7 = xRotate(f13, f14, f10, f11, f1, f6);
      f10 = yRotate(f13, f14, f10, f11, f1, f6);
      f6 = f5;
      f1 = f4;
      f2 = f12;
      f4 = f3;
      f5 = f8;
      f3 = f10;
      f8 = f2;
      f2 = f4;
      f11 = f7;
      f10 = f3;
    } else {
      f10 = f6;
      f11 = f1;
      f5 = f7;
      f1 = f3;
      f6 = f4;
    } 
    i = paramInt + 1;
    paramArrayOffloat[paramInt] = f6 + 0.0F;
    paramInt = i + 1;
    paramArrayOffloat[i] = f9 + 0.0F;
    i = paramInt + 1;
    paramArrayOffloat[paramInt] = f1 + 0.0F;
    paramInt = i + 1;
    paramArrayOffloat[i] = f8 + 0.0F;
    i = paramInt + 1;
    paramArrayOffloat[paramInt] = f2 + 0.0F;
    paramInt = i + 1;
    paramArrayOffloat[i] = f5 + 0.0F;
    i = paramInt + 1;
    paramArrayOffloat[paramInt] = f11 + 0.0F;
    paramArrayOffloat[i] = f10 + 0.0F;
  }
  
  boolean hasCustomData(String paramString) {
    return this.customAttributes.containsKey(paramString);
  }
  
  void initCartesian(MotionKeyPosition paramMotionKeyPosition, MotionPaths paramMotionPaths1, MotionPaths paramMotionPaths2) {
    float f2;
    float f3;
    float f1 = paramMotionKeyPosition.mFramePosition / 100.0F;
    this.time = f1;
    this.mDrawPath = paramMotionKeyPosition.mDrawPath;
    if (Float.isNaN(paramMotionKeyPosition.mPercentWidth)) {
      f2 = f1;
    } else {
      f2 = paramMotionKeyPosition.mPercentWidth;
    } 
    if (Float.isNaN(paramMotionKeyPosition.mPercentHeight)) {
      f3 = f1;
    } else {
      f3 = paramMotionKeyPosition.mPercentHeight;
    } 
    float f12 = paramMotionPaths2.width;
    float f6 = paramMotionPaths1.width;
    float f8 = f12 - f6;
    float f15 = paramMotionPaths2.height;
    float f4 = paramMotionPaths1.height;
    float f7 = f15 - f4;
    this.position = this.time;
    float f11 = paramMotionPaths1.x;
    float f9 = f6 / 2.0F;
    float f5 = paramMotionPaths1.y;
    float f10 = f4 / 2.0F;
    float f13 = paramMotionPaths2.x;
    float f14 = f12 / 2.0F;
    f12 = paramMotionPaths2.y;
    f15 /= 2.0F;
    f9 = f13 + f14 - f11 + f9;
    f10 = f12 + f15 - f5 + f10;
    this.x = (int)(f11 + f9 * f1 - f8 * f2 / 2.0F);
    this.y = (int)(f5 + f10 * f1 - f7 * f3 / 2.0F);
    this.width = (int)(f6 + f8 * f2);
    this.height = (int)(f4 + f7 * f3);
    if (Float.isNaN(paramMotionKeyPosition.mPercentX)) {
      f4 = f1;
    } else {
      f4 = paramMotionKeyPosition.mPercentX;
    } 
    if (Float.isNaN(paramMotionKeyPosition.mAltPercentY)) {
      f5 = 0.0F;
    } else {
      f5 = paramMotionKeyPosition.mAltPercentY;
    } 
    if (!Float.isNaN(paramMotionKeyPosition.mPercentY))
      f1 = paramMotionKeyPosition.mPercentY; 
    if (Float.isNaN(paramMotionKeyPosition.mAltPercentX)) {
      f6 = 0.0F;
    } else {
      f6 = paramMotionKeyPosition.mAltPercentX;
    } 
    this.mMode = 0;
    this.x = (int)(paramMotionPaths1.x + f9 * f4 + f10 * f6 - f8 * f2 / 2.0F);
    this.y = (int)(paramMotionPaths1.y + f9 * f5 + f10 * f1 - f7 * f3 / 2.0F);
    this.mKeyFrameEasing = Easing.getInterpolator(paramMotionKeyPosition.mTransitionEasing);
    this.mPathMotionArc = paramMotionKeyPosition.mPathMotionArc;
  }
  
  void initPath(MotionKeyPosition paramMotionKeyPosition, MotionPaths paramMotionPaths1, MotionPaths paramMotionPaths2) {
    float f3;
    float f1 = paramMotionKeyPosition.mFramePosition / 100.0F;
    this.time = f1;
    this.mDrawPath = paramMotionKeyPosition.mDrawPath;
    if (Float.isNaN(paramMotionKeyPosition.mPercentWidth)) {
      f2 = f1;
    } else {
      f2 = paramMotionKeyPosition.mPercentWidth;
    } 
    if (Float.isNaN(paramMotionKeyPosition.mPercentHeight)) {
      f3 = f1;
    } else {
      f3 = paramMotionKeyPosition.mPercentHeight;
    } 
    float f6 = paramMotionPaths2.width - paramMotionPaths1.width;
    float f5 = paramMotionPaths2.height - paramMotionPaths1.height;
    this.position = this.time;
    if (!Float.isNaN(paramMotionKeyPosition.mPercentX))
      f1 = paramMotionKeyPosition.mPercentX; 
    float f8 = paramMotionPaths1.x;
    float f10 = paramMotionPaths1.width;
    float f7 = f10 / 2.0F;
    float f4 = paramMotionPaths1.y;
    float f9 = paramMotionPaths1.height;
    float f11 = f9 / 2.0F;
    float f15 = paramMotionPaths2.x;
    float f14 = paramMotionPaths2.width / 2.0F;
    float f12 = paramMotionPaths2.y;
    float f13 = paramMotionPaths2.height / 2.0F;
    f7 = f15 + f14 - f7 + f8;
    f11 = f12 + f13 - f4 + f11;
    this.x = (int)(f8 + f7 * f1 - f6 * f2 / 2.0F);
    this.y = (int)(f4 + f11 * f1 - f5 * f3 / 2.0F);
    this.width = (int)(f10 + f6 * f2);
    this.height = (int)(f9 + f5 * f3);
    if (Float.isNaN(paramMotionKeyPosition.mPercentY)) {
      f4 = 0.0F;
    } else {
      f4 = paramMotionKeyPosition.mPercentY;
    } 
    f8 = -f11;
    this.mMode = 1;
    float f2 = (int)(paramMotionPaths1.x + f7 * f1 - f6 * f2 / 2.0F);
    this.x = f2;
    f1 = (int)(paramMotionPaths1.y + f11 * f1 - f5 * f3 / 2.0F);
    this.y = f1;
    this.x = f2 + f8 * f4;
    this.y = f1 + f7 * f4;
    this.mAnimateRelativeTo = this.mAnimateRelativeTo;
    this.mKeyFrameEasing = Easing.getInterpolator(paramMotionKeyPosition.mTransitionEasing);
    this.mPathMotionArc = paramMotionKeyPosition.mPathMotionArc;
  }
  
  void initPolar(int paramInt1, int paramInt2, MotionKeyPosition paramMotionKeyPosition, MotionPaths paramMotionPaths1, MotionPaths paramMotionPaths2) {
    float f2;
    float f3;
    float f1 = paramMotionKeyPosition.mFramePosition / 100.0F;
    this.time = f1;
    this.mDrawPath = paramMotionKeyPosition.mDrawPath;
    this.mMode = paramMotionKeyPosition.mPositionType;
    if (Float.isNaN(paramMotionKeyPosition.mPercentWidth)) {
      f2 = f1;
    } else {
      f2 = paramMotionKeyPosition.mPercentWidth;
    } 
    if (Float.isNaN(paramMotionKeyPosition.mPercentHeight)) {
      f3 = f1;
    } else {
      f3 = paramMotionKeyPosition.mPercentHeight;
    } 
    float f6 = paramMotionPaths2.width;
    float f7 = paramMotionPaths1.width;
    float f4 = paramMotionPaths2.height;
    float f5 = paramMotionPaths1.height;
    this.position = this.time;
    this.width = (int)(f7 + (f6 - f7) * f2);
    this.height = (int)(f5 + (f4 - f5) * f3);
    switch (paramMotionKeyPosition.mPositionType) {
      default:
        if (Float.isNaN(paramMotionKeyPosition.mPercentX)) {
          f2 = f1;
        } else {
          f2 = paramMotionKeyPosition.mPercentX;
        } 
        f4 = paramMotionPaths2.x;
        f3 = paramMotionPaths1.x;
        this.x = f2 * (f4 - f3) + f3;
        if (!Float.isNaN(paramMotionKeyPosition.mPercentY))
          f1 = paramMotionKeyPosition.mPercentY; 
        f3 = paramMotionPaths2.y;
        f2 = paramMotionPaths1.y;
        this.y = f1 * (f3 - f2) + f2;
        this.mAnimateRelativeTo = paramMotionPaths1.mAnimateRelativeTo;
        this.mKeyFrameEasing = Easing.getInterpolator(paramMotionKeyPosition.mTransitionEasing);
        this.mPathMotionArc = paramMotionKeyPosition.mPathMotionArc;
        return;
      case 2:
        if (Float.isNaN(paramMotionKeyPosition.mPercentX)) {
          f2 = paramMotionPaths2.x;
          f3 = paramMotionPaths1.x;
          f2 = (f2 - f3) * f1 + f3;
        } else {
          f2 = paramMotionKeyPosition.mPercentX * Math.min(f3, f2);
        } 
        this.x = f2;
        if (Float.isNaN(paramMotionKeyPosition.mPercentY)) {
          f2 = paramMotionPaths2.y;
          f3 = paramMotionPaths1.y;
          f1 = (f2 - f3) * f1 + f3;
        } else {
          f1 = paramMotionKeyPosition.mPercentY;
        } 
        this.y = f1;
        break;
      case 1:
        if (Float.isNaN(paramMotionKeyPosition.mPercentX)) {
          f2 = f1;
        } else {
          f2 = paramMotionKeyPosition.mPercentX;
        } 
        f4 = paramMotionPaths2.x;
        f3 = paramMotionPaths1.x;
        this.x = f2 * (f4 - f3) + f3;
        if (!Float.isNaN(paramMotionKeyPosition.mPercentY))
          f1 = paramMotionKeyPosition.mPercentY; 
        f3 = paramMotionPaths2.y;
        f2 = paramMotionPaths1.y;
        this.y = f1 * (f3 - f2) + f2;
        break;
    } 
    this.mAnimateRelativeTo = paramMotionPaths1.mAnimateRelativeTo;
    this.mKeyFrameEasing = Easing.getInterpolator(paramMotionKeyPosition.mTransitionEasing);
    this.mPathMotionArc = paramMotionKeyPosition.mPathMotionArc;
  }
  
  void initScreen(int paramInt1, int paramInt2, MotionKeyPosition paramMotionKeyPosition, MotionPaths paramMotionPaths1, MotionPaths paramMotionPaths2) {
    float f2;
    float f3;
    float f1 = paramMotionKeyPosition.mFramePosition / 100.0F;
    this.time = f1;
    this.mDrawPath = paramMotionKeyPosition.mDrawPath;
    if (Float.isNaN(paramMotionKeyPosition.mPercentWidth)) {
      f2 = f1;
    } else {
      f2 = paramMotionKeyPosition.mPercentWidth;
    } 
    if (Float.isNaN(paramMotionKeyPosition.mPercentHeight)) {
      f3 = f1;
    } else {
      f3 = paramMotionKeyPosition.mPercentHeight;
    } 
    float f13 = paramMotionPaths2.width;
    float f9 = paramMotionPaths1.width;
    float f7 = f13 - f9;
    float f15 = paramMotionPaths2.height;
    float f6 = paramMotionPaths1.height;
    float f10 = f15 - f6;
    this.position = this.time;
    float f8 = paramMotionPaths1.x;
    float f5 = f9 / 2.0F;
    float f12 = paramMotionPaths1.y;
    float f11 = f6 / 2.0F;
    float f4 = paramMotionPaths2.x;
    f13 /= 2.0F;
    float f14 = paramMotionPaths2.y;
    f15 /= 2.0F;
    this.x = (int)(f8 + (f4 + f13 - f8 + f5) * f1 - f7 * f2 / 2.0F);
    this.y = (int)(f12 + (f14 + f15 - f12 + f11) * f1 - f10 * f3 / 2.0F);
    this.width = (int)(f9 + f7 * f2);
    this.height = (int)(f6 + f10 * f3);
    this.mMode = 2;
    if (!Float.isNaN(paramMotionKeyPosition.mPercentX)) {
      paramInt1 = (int)(paramInt1 - this.width);
      this.x = (int)(paramMotionKeyPosition.mPercentX * paramInt1);
    } 
    if (!Float.isNaN(paramMotionKeyPosition.mPercentY)) {
      paramInt1 = (int)(paramInt2 - this.height);
      this.y = (int)(paramMotionKeyPosition.mPercentY * paramInt1);
    } 
    this.mAnimateRelativeTo = this.mAnimateRelativeTo;
    this.mKeyFrameEasing = Easing.getInterpolator(paramMotionKeyPosition.mTransitionEasing);
    this.mPathMotionArc = paramMotionKeyPosition.mPathMotionArc;
  }
  
  void setBounds(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
    this.x = paramFloat1;
    this.y = paramFloat2;
    this.width = paramFloat3;
    this.height = paramFloat4;
  }
  
  void setDpDt(float paramFloat1, float paramFloat2, float[] paramArrayOffloat, int[] paramArrayOfint, double[] paramArrayOfdouble1, double[] paramArrayOfdouble2) {
    float f5 = 0.0F;
    float f4 = 0.0F;
    float f3 = 0.0F;
    float f2 = 0.0F;
    byte b = 0;
    while (b < paramArrayOfint.length) {
      float f6 = (float)paramArrayOfdouble1[b];
      float f7 = (float)paramArrayOfdouble2[b];
      switch (paramArrayOfint[b]) {
        default:
          f6 = f2;
          break;
        case 4:
          break;
        case 3:
          f3 = f6;
          f6 = f2;
          break;
        case 2:
          f4 = f6;
          f6 = f2;
          break;
        case 1:
          f5 = f6;
          f6 = f2;
          break;
        case 0:
          f6 = f2;
          break;
      } 
      b++;
      f2 = f6;
    } 
    float f1 = f5 - 0.0F * f3 / 2.0F;
    f4 -= 0.0F * f2 / 2.0F;
    paramArrayOffloat[0] = (1.0F - paramFloat1) * f1 + (f1 + (0.0F + 1.0F) * f3) * paramFloat1 + 0.0F;
    paramArrayOffloat[1] = (1.0F - paramFloat2) * f4 + (f4 + (0.0F + 1.0F) * f2) * paramFloat2 + 0.0F;
  }
  
  void setView(float paramFloat, MotionWidget paramMotionWidget, int[] paramArrayOfint, double[] paramArrayOfdouble1, double[] paramArrayOfdouble2, double[] paramArrayOfdouble3) {
    Object object1;
    Object object2;
    Object object3;
    Object object4;
    Object object5;
    Object object6;
    Object object7;
    Object object8;
    Object object9;
    Object object10;
    float f1 = this.x;
    float f2 = this.y;
    float f5 = this.width;
    float f4 = this.height;
    float f7 = 0.0F;
    float f6 = 0.0F;
    float f10 = 0.0F;
    float f9 = 0.0F;
    float f8 = 0.0F;
    float f3 = Float.NaN;
    if (paramArrayOfint.length != 0 && this.mTempValue.length <= paramArrayOfint[paramArrayOfint.length - 1]) {
      int j = paramArrayOfint[paramArrayOfint.length - 1] + 1;
      this.mTempValue = new double[j];
      this.mTempDelta = new double[j];
    } 
    Arrays.fill(this.mTempValue, Double.NaN);
    int i;
    for (i = 0; i < paramArrayOfint.length; i++) {
      this.mTempValue[paramArrayOfint[i]] = paramArrayOfdouble1[i];
      this.mTempDelta[paramArrayOfint[i]] = paramArrayOfdouble2[i];
    } 
    i = 0;
    while (true) {
      Object object11;
      Object object12;
      double[] arrayOfDouble = this.mTempValue;
      if (i < arrayOfDouble.length) {
        Object object13;
        Object object14;
        Object object15;
        Object object16;
        Object object17;
        Object object18;
        Object object19;
        Object object20;
        Object object21;
        boolean bool = Double.isNaN(arrayOfDouble[i]);
        double d = 0.0D;
        if (bool) {
          Object object22 = object2;
          Object object23 = object5;
          Object object24 = object4;
          Object object25 = object7;
          Object object26 = object6;
          Object object27 = object10;
          Object object30 = object9;
          Object object28 = object8;
          Object object29 = object3;
          Object object31 = object1;
          if (paramArrayOfdouble3 != null) {
            if (paramArrayOfdouble3[i] == 0.0D) {
              object22 = object2;
              object23 = object5;
              object24 = object4;
              object25 = object7;
              object26 = object6;
              object27 = object10;
              object30 = object9;
              object28 = object8;
              object29 = object3;
              object31 = object1;
              continue;
            } 
          } else {
            continue;
          } 
        } 
        if (paramArrayOfdouble3 != null)
          d = paramArrayOfdouble3[i]; 
        if (!Double.isNaN(this.mTempValue[i]))
          d = this.mTempValue[i] + d; 
        float f11 = (float)d;
        float f12 = (float)this.mTempDelta[i];
        switch (i) {
          default:
            object12 = object2;
            object13 = object5;
            object14 = object4;
            object15 = object7;
            object16 = object6;
            object17 = object10;
            object20 = object9;
            object18 = object8;
            object19 = object3;
            object21 = object1;
            break;
          case 5:
            object19 = object12;
            object12 = object2;
            object13 = object5;
            object14 = object4;
            object15 = object7;
            object16 = object6;
            object17 = object10;
            object20 = object9;
            object18 = object8;
            object21 = object1;
            break;
          case 4:
            object14 = object12;
            object20 = object13;
            object12 = object2;
            object13 = object5;
            object15 = object7;
            object16 = object6;
            object17 = object10;
            object18 = object8;
            object19 = object3;
            object21 = object1;
            break;
          case 3:
            object5 = object12;
            object17 = object13;
            object12 = object2;
            object13 = object5;
            object14 = object4;
            object15 = object7;
            object16 = object6;
            object20 = object9;
            object18 = object8;
            object19 = object3;
            object21 = object1;
            break;
          case 2:
            object16 = object13;
            object13 = object5;
            object14 = object4;
            object15 = object7;
            object17 = object10;
            object20 = object9;
            object18 = object8;
            object19 = object3;
            object21 = object1;
            break;
          case 1:
            object15 = object13;
            object21 = object12;
            object12 = object2;
            object13 = object5;
            object14 = object4;
            object16 = object6;
            object17 = object10;
            object20 = object9;
            object18 = object8;
            object19 = object3;
            break;
          case 0:
            object21 = object1;
            object19 = object3;
            object18 = object12;
            object20 = object9;
            object17 = object10;
            object16 = object6;
            object15 = object7;
            object14 = object4;
            object13 = object5;
            object12 = object2;
            break;
        } 
        continue;
      } 
      Motion motion = this.mRelativeToController;
      if (motion != null) {
        float[] arrayOfFloat2 = new float[2];
        float[] arrayOfFloat1 = new float[2];
        motion.getCenter(paramFloat, arrayOfFloat2, arrayOfFloat1);
        paramFloat = arrayOfFloat2[0];
        float f11 = arrayOfFloat2[1];
        float f13 = arrayOfFloat1[0];
        float f12 = arrayOfFloat1[1];
        paramFloat = (float)(paramFloat + object1 * Math.sin(object2) - (object5 / 2.0F));
        f11 = (float)(f11 - object1 * Math.cos(object2) - (object4 / 2.0F));
        f13 = (float)(f13 + object7 * Math.sin(object2) + object1 * Math.cos(object2) * object6);
        f12 = (float)(f12 - object7 * Math.cos(object2) + object1 * Math.sin(object2) * object6);
        if (paramArrayOfdouble2.length >= 2) {
          paramArrayOfdouble2[0] = f13;
          paramArrayOfdouble2[1] = f12;
        } 
        if (!Float.isNaN(object3))
          paramMotionWidget.setRotationZ((float)(object3 + Math.toDegrees(Math.atan2(f12, f13)))); 
      } else {
        if (!Float.isNaN(object3)) {
          paramFloat = object10 / 2.0F;
          float f = object9 / 2.0F;
          paramMotionWidget.setRotationZ((float)(0.0F + object3 + Math.toDegrees(Math.atan2((object6 + f), (object7 + paramFloat)))));
        } 
        object12 = object2;
        object11 = object1;
      } 
      i = (int)(object11 + 0.5F);
      int k = (int)(object12 + 0.5F);
      int j = (int)(object11 + 0.5F + object5);
      int m = (int)(0.5F + object12 + object4);
      paramMotionWidget.layout(i, k, j, m);
      return;
      i++;
      object2 = SYNTHETIC_LOCAL_VARIABLE_9;
      object5 = SYNTHETIC_LOCAL_VARIABLE_10;
      object4 = SYNTHETIC_LOCAL_VARIABLE_21;
      object7 = SYNTHETIC_LOCAL_VARIABLE_22;
      object6 = SYNTHETIC_LOCAL_VARIABLE_23;
      object10 = SYNTHETIC_LOCAL_VARIABLE_24;
      object9 = SYNTHETIC_LOCAL_VARIABLE_27;
      object8 = SYNTHETIC_LOCAL_VARIABLE_25;
      object3 = SYNTHETIC_LOCAL_VARIABLE_26;
      object1 = SYNTHETIC_LOCAL_VARIABLE_28;
    } 
  }
  
  public void setupRelative(Motion paramMotion, MotionPaths paramMotionPaths) {
    double d1 = (this.x + this.width / 2.0F - paramMotionPaths.x - paramMotionPaths.width / 2.0F);
    double d2 = (this.y + this.height / 2.0F - paramMotionPaths.y - paramMotionPaths.height / 2.0F);
    this.mRelativeToController = paramMotion;
    this.x = (float)Math.hypot(d2, d1);
    if (Float.isNaN(this.mRelativeAngle)) {
      this.y = (float)(Math.atan2(d2, d1) + 1.5707963267948966D);
    } else {
      this.y = (float)Math.toRadians(this.mRelativeAngle);
    } 
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\motion\MotionPaths.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */