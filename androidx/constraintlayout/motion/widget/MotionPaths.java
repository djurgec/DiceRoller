package androidx.constraintlayout.motion.widget;

import android.view.View;
import androidx.constraintlayout.core.motion.utils.Easing;
import androidx.constraintlayout.widget.ConstraintAttribute;
import androidx.constraintlayout.widget.ConstraintSet;
import java.util.Arrays;
import java.util.LinkedHashMap;

class MotionPaths implements Comparable<MotionPaths> {
  static final int CARTESIAN = 0;
  
  public static final boolean DEBUG = false;
  
  static final int OFF_HEIGHT = 4;
  
  static final int OFF_PATH_ROTATE = 5;
  
  static final int OFF_POSITION = 0;
  
  static final int OFF_WIDTH = 3;
  
  static final int OFF_X = 1;
  
  static final int OFF_Y = 2;
  
  public static final boolean OLD_WAY = false;
  
  static final int PERPENDICULAR = 1;
  
  static final int SCREEN = 2;
  
  public static final String TAG = "MotionPaths";
  
  static String[] names = new String[] { "position", "x", "y", "width", "height", "pathRotate" };
  
  LinkedHashMap<String, ConstraintAttribute> attributes = new LinkedHashMap<>();
  
  float height;
  
  int mAnimateCircleAngleTo;
  
  int mAnimateRelativeTo = Key.UNSET;
  
  int mDrawPath = 0;
  
  Easing mKeyFrameEasing;
  
  int mMode = 0;
  
  int mPathMotionArc = Key.UNSET;
  
  float mPathRotate = Float.NaN;
  
  float mProgress = Float.NaN;
  
  float mRelativeAngle = Float.NaN;
  
  MotionController mRelativeToController = null;
  
  double[] mTempDelta = new double[18];
  
  double[] mTempValue = new double[18];
  
  float position;
  
  float time;
  
  float width;
  
  float x;
  
  float y;
  
  public MotionPaths() {}
  
  public MotionPaths(int paramInt1, int paramInt2, KeyPosition paramKeyPosition, MotionPaths paramMotionPaths1, MotionPaths paramMotionPaths2) {
    if (paramMotionPaths1.mAnimateRelativeTo != Key.UNSET) {
      initPolar(paramInt1, paramInt2, paramKeyPosition, paramMotionPaths1, paramMotionPaths2);
      return;
    } 
    switch (paramKeyPosition.mPositionType) {
      default:
        initCartesian(paramKeyPosition, paramMotionPaths1, paramMotionPaths2);
        return;
      case 2:
        initScreen(paramInt1, paramInt2, paramKeyPosition, paramMotionPaths1, paramMotionPaths2);
        return;
      case 1:
        break;
    } 
    initPath(paramKeyPosition, paramMotionPaths1, paramMotionPaths2);
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
  
  public void applyParameters(ConstraintSet.Constraint paramConstraint) {
    this.mKeyFrameEasing = Easing.getInterpolator(paramConstraint.motion.mTransitionEasing);
    this.mPathMotionArc = paramConstraint.motion.mPathMotionArc;
    this.mAnimateRelativeTo = paramConstraint.motion.mAnimateRelativeTo;
    this.mPathRotate = paramConstraint.motion.mPathRotate;
    this.mDrawPath = paramConstraint.motion.mDrawPath;
    this.mAnimateCircleAngleTo = paramConstraint.motion.mAnimateCircleAngleTo;
    this.mProgress = paramConstraint.propertySet.mProgress;
    this.mRelativeAngle = paramConstraint.layout.circleAngle;
    for (String str : paramConstraint.mCustomConstraints.keySet()) {
      ConstraintAttribute constraintAttribute = (ConstraintAttribute)paramConstraint.mCustomConstraints.get(str);
      if (constraintAttribute != null && constraintAttribute.isContinuous())
        this.attributes.put(str, constraintAttribute); 
    } 
  }
  
  public int compareTo(MotionPaths paramMotionPaths) {
    return Float.compare(this.position, paramMotionPaths.position);
  }
  
  public void configureRelativeTo(MotionController paramMotionController) {
    paramMotionController.getPos(this.mProgress);
  }
  
  void different(MotionPaths paramMotionPaths, boolean[] paramArrayOfboolean, String[] paramArrayOfString, boolean paramBoolean) {
    boolean bool2 = diff(this.x, paramMotionPaths.x);
    boolean bool1 = diff(this.y, paramMotionPaths.y);
    int j = 0 + 1;
    paramArrayOfboolean[0] = paramArrayOfboolean[0] | diff(this.position, paramMotionPaths.position);
    int i = j + 1;
    paramArrayOfboolean[j] = paramArrayOfboolean[j] | bool2 | bool1 | paramBoolean;
    j = i + 1;
    paramArrayOfboolean[i] = paramArrayOfboolean[i] | bool2 | bool1 | paramBoolean;
    i = j + 1;
    paramArrayOfboolean[j] = paramArrayOfboolean[j] | diff(this.width, paramMotionPaths.width);
    paramArrayOfboolean[i] = paramArrayOfboolean[i] | diff(this.height, paramMotionPaths.height);
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
    MotionController motionController = this.mRelativeToController;
    if (motionController != null) {
      float[] arrayOfFloat = new float[2];
      motionController.getCenter(paramDouble, arrayOfFloat, new float[2]);
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
    MotionController motionController = this.mRelativeToController;
    if (motionController != null) {
      float[] arrayOfFloat1 = new float[2];
      float[] arrayOfFloat2 = new float[2];
      motionController.getCenter(paramDouble, arrayOfFloat1, arrayOfFloat2);
      f2 = arrayOfFloat1[0];
      f10 = arrayOfFloat1[1];
      f1 = arrayOfFloat2[0];
      f9 = arrayOfFloat2[1];
      f2 = (float)(f2 + f4 * Math.sin(f3) - (f6 / 2.0F));
      f4 = (float)(f10 - f4 * Math.cos(f3) - (f5 / 2.0F));
      f1 = (float)(f1 + f8 * Math.sin(f3) + Math.cos(f3) * f7);
      f7 = (float)(f9 - f8 * Math.cos(f3) + Math.sin(f3) * f7);
      f3 = f4;
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
    MotionController motionController = this.mRelativeToController;
    if (motionController != null) {
      float[] arrayOfFloat = new float[2];
      motionController.getCenter(paramDouble, arrayOfFloat, new float[2]);
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
    ConstraintAttribute constraintAttribute = this.attributes.get(paramString);
    if (constraintAttribute == null)
      return 0; 
    if (constraintAttribute.numberOfInterpolatedValues() == 1) {
      paramArrayOfdouble[paramInt] = constraintAttribute.getValueToInterpolate();
      return 1;
    } 
    int i = constraintAttribute.numberOfInterpolatedValues();
    float[] arrayOfFloat = new float[i];
    constraintAttribute.getValuesToInterpolate(arrayOfFloat);
    byte b = 0;
    while (b < i) {
      paramArrayOfdouble[paramInt] = arrayOfFloat[b];
      b++;
      paramInt++;
    } 
    return i;
  }
  
  int getCustomDataCount(String paramString) {
    ConstraintAttribute constraintAttribute = this.attributes.get(paramString);
    return (constraintAttribute == null) ? 0 : constraintAttribute.numberOfInterpolatedValues();
  }
  
  void getRect(int[] paramArrayOfint, double[] paramArrayOfdouble, float[] paramArrayOffloat, int paramInt) {
    float f2 = this.x;
    float f3 = this.y;
    float f7 = this.width;
    float f4 = this.height;
    boolean bool2 = false;
    boolean bool1 = false;
    int i;
    for (i = 0; i < paramArrayOfint.length; i++) {
      float f = (float)paramArrayOfdouble[i];
      switch (paramArrayOfint[i]) {
        case 4:
          f4 = f;
          break;
        case 3:
          f7 = f;
          break;
        case 2:
          f3 = f;
          break;
        case 1:
          f2 = f;
          break;
      } 
    } 
    MotionController motionController = this.mRelativeToController;
    if (motionController != null) {
      float f16 = motionController.getCenterX();
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
      f14 = (float)Math.sin(Math.toRadians(0.0F));
      f15 = (float)Math.cos(Math.toRadians(0.0F));
      f5 = xRotate(f14, f15, f10, f11, f4, f9);
      f13 = yRotate(f14, f15, f10, f11, f4, f9);
      f4 = xRotate(f14, f15, f10, f11, f3, f8);
      f9 = yRotate(f14, f15, f10, f11, f3, f8);
      f8 = xRotate(f14, f15, f10, f11, f2, f7);
      f12 = yRotate(f14, f15, f10, f11, f2, f7);
      f3 = xRotate(f14, f15, f10, f11, f1, f6);
      f6 = yRotate(f14, f15, f10, f11, f1, f6);
      f1 = f5;
      f7 = f13;
      f10 = f4;
      f4 = f9;
      f2 = f8;
      f11 = f12;
      f5 = f1;
      f9 = f7;
      f8 = f4;
      f7 = f11;
      f1 = f3;
    } else {
      f10 = f3;
      f5 = f4;
    } 
    i = paramInt + 1;
    paramArrayOffloat[paramInt] = f5 + 0.0F;
    paramInt = i + 1;
    paramArrayOffloat[i] = f9 + 0.0F;
    i = paramInt + 1;
    paramArrayOffloat[paramInt] = f10 + 0.0F;
    paramInt = i + 1;
    paramArrayOffloat[i] = f8 + 0.0F;
    i = paramInt + 1;
    paramArrayOffloat[paramInt] = f2 + 0.0F;
    paramInt = i + 1;
    paramArrayOffloat[i] = f7 + 0.0F;
    i = paramInt + 1;
    paramArrayOffloat[paramInt] = f1 + 0.0F;
    paramArrayOffloat[i] = f6 + 0.0F;
  }
  
  boolean hasCustomData(String paramString) {
    return this.attributes.containsKey(paramString);
  }
  
  void initCartesian(KeyPosition paramKeyPosition, MotionPaths paramMotionPaths1, MotionPaths paramMotionPaths2) {
    float f2;
    float f3;
    float f1 = paramKeyPosition.mFramePosition / 100.0F;
    this.time = f1;
    this.mDrawPath = paramKeyPosition.mDrawPath;
    if (Float.isNaN(paramKeyPosition.mPercentWidth)) {
      f2 = f1;
    } else {
      f2 = paramKeyPosition.mPercentWidth;
    } 
    if (Float.isNaN(paramKeyPosition.mPercentHeight)) {
      f3 = f1;
    } else {
      f3 = paramKeyPosition.mPercentHeight;
    } 
    float f12 = paramMotionPaths2.width;
    float f6 = paramMotionPaths1.width;
    float f8 = f12 - f6;
    float f14 = paramMotionPaths2.height;
    float f11 = paramMotionPaths1.height;
    float f7 = f14 - f11;
    this.position = this.time;
    float f4 = paramMotionPaths1.x;
    float f13 = f6 / 2.0F;
    float f5 = paramMotionPaths1.y;
    float f10 = f11 / 2.0F;
    float f9 = paramMotionPaths2.x;
    float f15 = f12 / 2.0F;
    f12 = paramMotionPaths2.y;
    f14 /= 2.0F;
    f9 = f9 + f15 - f4 + f13;
    f10 = f12 + f14 - f5 + f10;
    this.x = (int)(f4 + f9 * f1 - f8 * f2 / 2.0F);
    this.y = (int)(f5 + f10 * f1 - f7 * f3 / 2.0F);
    this.width = (int)(f6 + f8 * f2);
    this.height = (int)(f11 + f7 * f3);
    if (Float.isNaN(paramKeyPosition.mPercentX)) {
      f4 = f1;
    } else {
      f4 = paramKeyPosition.mPercentX;
    } 
    if (Float.isNaN(paramKeyPosition.mAltPercentY)) {
      f5 = 0.0F;
    } else {
      f5 = paramKeyPosition.mAltPercentY;
    } 
    if (!Float.isNaN(paramKeyPosition.mPercentY))
      f1 = paramKeyPosition.mPercentY; 
    if (Float.isNaN(paramKeyPosition.mAltPercentX)) {
      f6 = 0.0F;
    } else {
      f6 = paramKeyPosition.mAltPercentX;
    } 
    this.mMode = 0;
    this.x = (int)(paramMotionPaths1.x + f9 * f4 + f10 * f6 - f8 * f2 / 2.0F);
    this.y = (int)(paramMotionPaths1.y + f9 * f5 + f10 * f1 - f7 * f3 / 2.0F);
    this.mKeyFrameEasing = Easing.getInterpolator(paramKeyPosition.mTransitionEasing);
    this.mPathMotionArc = paramKeyPosition.mPathMotionArc;
  }
  
  void initPath(KeyPosition paramKeyPosition, MotionPaths paramMotionPaths1, MotionPaths paramMotionPaths2) {
    float f3;
    float f1 = paramKeyPosition.mFramePosition / 100.0F;
    this.time = f1;
    this.mDrawPath = paramKeyPosition.mDrawPath;
    if (Float.isNaN(paramKeyPosition.mPercentWidth)) {
      f2 = f1;
    } else {
      f2 = paramKeyPosition.mPercentWidth;
    } 
    if (Float.isNaN(paramKeyPosition.mPercentHeight)) {
      f3 = f1;
    } else {
      f3 = paramKeyPosition.mPercentHeight;
    } 
    float f6 = paramMotionPaths2.width - paramMotionPaths1.width;
    float f5 = paramMotionPaths2.height - paramMotionPaths1.height;
    this.position = this.time;
    if (!Float.isNaN(paramKeyPosition.mPercentX))
      f1 = paramKeyPosition.mPercentX; 
    float f9 = paramMotionPaths1.x;
    float f4 = paramMotionPaths1.width;
    float f15 = f4 / 2.0F;
    float f10 = paramMotionPaths1.y;
    float f8 = paramMotionPaths1.height;
    float f12 = f8 / 2.0F;
    float f14 = paramMotionPaths2.x;
    float f7 = paramMotionPaths2.width / 2.0F;
    float f13 = paramMotionPaths2.y;
    float f11 = paramMotionPaths2.height / 2.0F;
    f7 = f14 + f7 - f15 + f9;
    f11 = f13 + f11 - f10 + f12;
    this.x = (int)(f9 + f7 * f1 - f6 * f2 / 2.0F);
    this.y = (int)(f10 + f11 * f1 - f5 * f3 / 2.0F);
    this.width = (int)(f4 + f6 * f2);
    this.height = (int)(f8 + f5 * f3);
    if (Float.isNaN(paramKeyPosition.mPercentY)) {
      f4 = 0.0F;
    } else {
      f4 = paramKeyPosition.mPercentY;
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
    this.mKeyFrameEasing = Easing.getInterpolator(paramKeyPosition.mTransitionEasing);
    this.mPathMotionArc = paramKeyPosition.mPathMotionArc;
  }
  
  void initPolar(int paramInt1, int paramInt2, KeyPosition paramKeyPosition, MotionPaths paramMotionPaths1, MotionPaths paramMotionPaths2) {
    float f2;
    float f3;
    float f1 = paramKeyPosition.mFramePosition / 100.0F;
    this.time = f1;
    this.mDrawPath = paramKeyPosition.mDrawPath;
    this.mMode = paramKeyPosition.mPositionType;
    if (Float.isNaN(paramKeyPosition.mPercentWidth)) {
      f2 = f1;
    } else {
      f2 = paramKeyPosition.mPercentWidth;
    } 
    if (Float.isNaN(paramKeyPosition.mPercentHeight)) {
      f3 = f1;
    } else {
      f3 = paramKeyPosition.mPercentHeight;
    } 
    float f4 = paramMotionPaths2.width;
    float f5 = paramMotionPaths1.width;
    float f6 = paramMotionPaths2.height;
    float f7 = paramMotionPaths1.height;
    this.position = this.time;
    this.width = (int)(f5 + (f4 - f5) * f2);
    this.height = (int)(f7 + (f6 - f7) * f3);
    switch (paramKeyPosition.mPositionType) {
      default:
        if (Float.isNaN(paramKeyPosition.mPercentX)) {
          f2 = f1;
        } else {
          f2 = paramKeyPosition.mPercentX;
        } 
        f3 = paramMotionPaths2.x;
        f4 = paramMotionPaths1.x;
        this.x = f2 * (f3 - f4) + f4;
        if (!Float.isNaN(paramKeyPosition.mPercentY))
          f1 = paramKeyPosition.mPercentY; 
        f3 = paramMotionPaths2.y;
        f2 = paramMotionPaths1.y;
        this.y = f1 * (f3 - f2) + f2;
        this.mAnimateRelativeTo = paramMotionPaths1.mAnimateRelativeTo;
        this.mKeyFrameEasing = Easing.getInterpolator(paramKeyPosition.mTransitionEasing);
        this.mPathMotionArc = paramKeyPosition.mPathMotionArc;
        return;
      case 2:
        if (Float.isNaN(paramKeyPosition.mPercentX)) {
          f3 = paramMotionPaths2.x;
          f2 = paramMotionPaths1.x;
          f2 = (f3 - f2) * f1 + f2;
        } else {
          f2 = paramKeyPosition.mPercentX * Math.min(f3, f2);
        } 
        this.x = f2;
        if (Float.isNaN(paramKeyPosition.mPercentY)) {
          f2 = paramMotionPaths2.y;
          f3 = paramMotionPaths1.y;
          f1 = (f2 - f3) * f1 + f3;
        } else {
          f1 = paramKeyPosition.mPercentY;
        } 
        this.y = f1;
        break;
      case 1:
        if (Float.isNaN(paramKeyPosition.mPercentX)) {
          f2 = f1;
        } else {
          f2 = paramKeyPosition.mPercentX;
        } 
        f4 = paramMotionPaths2.x;
        f3 = paramMotionPaths1.x;
        this.x = f2 * (f4 - f3) + f3;
        if (!Float.isNaN(paramKeyPosition.mPercentY))
          f1 = paramKeyPosition.mPercentY; 
        f2 = paramMotionPaths2.y;
        f3 = paramMotionPaths1.y;
        this.y = f1 * (f2 - f3) + f3;
        break;
    } 
    this.mAnimateRelativeTo = paramMotionPaths1.mAnimateRelativeTo;
    this.mKeyFrameEasing = Easing.getInterpolator(paramKeyPosition.mTransitionEasing);
    this.mPathMotionArc = paramKeyPosition.mPathMotionArc;
  }
  
  void initScreen(int paramInt1, int paramInt2, KeyPosition paramKeyPosition, MotionPaths paramMotionPaths1, MotionPaths paramMotionPaths2) {
    float f2;
    float f3;
    float f1 = paramKeyPosition.mFramePosition / 100.0F;
    this.time = f1;
    this.mDrawPath = paramKeyPosition.mDrawPath;
    if (Float.isNaN(paramKeyPosition.mPercentWidth)) {
      f2 = f1;
    } else {
      f2 = paramKeyPosition.mPercentWidth;
    } 
    if (Float.isNaN(paramKeyPosition.mPercentHeight)) {
      f3 = f1;
    } else {
      f3 = paramKeyPosition.mPercentHeight;
    } 
    float f13 = paramMotionPaths2.width;
    float f6 = paramMotionPaths1.width;
    float f5 = f13 - f6;
    float f14 = paramMotionPaths2.height;
    float f7 = paramMotionPaths1.height;
    float f10 = f14 - f7;
    this.position = this.time;
    float f11 = paramMotionPaths1.x;
    float f9 = f6 / 2.0F;
    float f8 = paramMotionPaths1.y;
    float f4 = f7 / 2.0F;
    float f12 = paramMotionPaths2.x;
    f13 /= 2.0F;
    float f15 = paramMotionPaths2.y;
    f14 /= 2.0F;
    this.x = (int)(f11 + (f12 + f13 - f11 + f9) * f1 - f5 * f2 / 2.0F);
    this.y = (int)(f8 + (f15 + f14 - f8 + f4) * f1 - f10 * f3 / 2.0F);
    this.width = (int)(f6 + f5 * f2);
    this.height = (int)(f7 + f10 * f3);
    this.mMode = 2;
    if (!Float.isNaN(paramKeyPosition.mPercentX)) {
      paramInt1 = (int)(paramInt1 - this.width);
      this.x = (int)(paramKeyPosition.mPercentX * paramInt1);
    } 
    if (!Float.isNaN(paramKeyPosition.mPercentY)) {
      paramInt1 = (int)(paramInt2 - this.height);
      this.y = (int)(paramKeyPosition.mPercentY * paramInt1);
    } 
    this.mAnimateRelativeTo = this.mAnimateRelativeTo;
    this.mKeyFrameEasing = Easing.getInterpolator(paramKeyPosition.mTransitionEasing);
    this.mPathMotionArc = paramKeyPosition.mPathMotionArc;
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
    for (byte b = 0; b < paramArrayOfint.length; b++) {
      float f6 = (float)paramArrayOfdouble1[b];
      float f7 = (float)paramArrayOfdouble2[b];
      switch (paramArrayOfint[b]) {
        case 4:
          f2 = f6;
          break;
        case 3:
          f3 = f6;
          break;
        case 2:
          f4 = f6;
          break;
        case 1:
          f5 = f6;
          break;
      } 
    } 
    float f1 = f5 - 0.0F * f3 / 2.0F;
    f4 -= 0.0F * f2 / 2.0F;
    paramArrayOffloat[0] = (1.0F - paramFloat1) * f1 + (f1 + (0.0F + 1.0F) * f3) * paramFloat1 + 0.0F;
    paramArrayOffloat[1] = (1.0F - paramFloat2) * f4 + (f4 + (0.0F + 1.0F) * f2) * paramFloat2 + 0.0F;
  }
  
  void setView(float paramFloat, View paramView, int[] paramArrayOfint, double[] paramArrayOfdouble1, double[] paramArrayOfdouble2, double[] paramArrayOfdouble3, boolean paramBoolean) {
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
      int i = paramArrayOfint[paramArrayOfint.length - 1] + 1;
      this.mTempValue = new double[i];
      this.mTempDelta = new double[i];
    } 
    Arrays.fill(this.mTempValue, Double.NaN);
    byte b;
    for (b = 0; b < paramArrayOfint.length; b++) {
      this.mTempValue[paramArrayOfint[b]] = paramArrayOfdouble1[b];
      this.mTempDelta[paramArrayOfint[b]] = paramArrayOfdouble2[b];
    } 
    b = 0;
    while (true) {
      Object object;
      float f;
      double[] arrayOfDouble = this.mTempValue;
      if (b < arrayOfDouble.length) {
        Object object11;
        Object object12;
        Object object13;
        Object object14;
        Object object15;
        Object object16;
        Object object17;
        Object object18;
        Object object19;
        Object object20;
        boolean bool = Double.isNaN(arrayOfDouble[b]);
        double d = 0.0D;
        if (bool) {
          Object object21 = object2;
          Object object22 = object5;
          Object object23 = object4;
          Object object24 = object7;
          Object object25 = object6;
          Object object26 = object10;
          Object object28 = object9;
          Object object29 = object8;
          Object object27 = object3;
          Object object30 = object1;
          if (paramArrayOfdouble3 != null) {
            if (paramArrayOfdouble3[b] == 0.0D) {
              object21 = object2;
              object22 = object5;
              object23 = object4;
              object24 = object7;
              object25 = object6;
              object26 = object10;
              object28 = object9;
              object29 = object8;
              object27 = object3;
              object30 = object1;
              continue;
            } 
          } else {
            continue;
          } 
        } 
        if (paramArrayOfdouble3 != null)
          d = paramArrayOfdouble3[b]; 
        if (!Double.isNaN(this.mTempValue[b]))
          d = this.mTempValue[b] + d; 
        float f11 = (float)d;
        float f12 = (float)this.mTempDelta[b];
        switch (b) {
          default:
            object11 = object2;
            object12 = object5;
            object13 = object4;
            object14 = object7;
            object15 = object6;
            object16 = object10;
            object18 = object9;
            object19 = object8;
            object17 = object3;
            object20 = object1;
            break;
          case 5:
            object17 = object11;
            object11 = object2;
            object12 = object5;
            object13 = object4;
            object14 = object7;
            object15 = object6;
            object16 = object10;
            object18 = object9;
            object19 = object8;
            object20 = object1;
            break;
          case 4:
            object13 = object11;
            object18 = object12;
            object11 = object2;
            object12 = object5;
            object14 = object7;
            object15 = object6;
            object16 = object10;
            object19 = object8;
            object17 = object3;
            object20 = object1;
            break;
          case 3:
            object5 = object11;
            object16 = object12;
            object11 = object2;
            object12 = object5;
            object13 = object4;
            object14 = object7;
            object15 = object6;
            object18 = object9;
            object19 = object8;
            object17 = object3;
            object20 = object1;
            break;
          case 2:
            object15 = object12;
            object12 = object5;
            object13 = object4;
            object14 = object7;
            object16 = object10;
            object18 = object9;
            object19 = object8;
            object17 = object3;
            object20 = object1;
            break;
          case 1:
            object14 = object12;
            object20 = object11;
            object11 = object2;
            object12 = object5;
            object13 = object4;
            object15 = object6;
            object16 = object10;
            object18 = object9;
            object19 = object8;
            object17 = object3;
            break;
          case 0:
            object20 = object1;
            object17 = object3;
            object19 = object11;
            object18 = object9;
            object16 = object10;
            object15 = object6;
            object14 = object7;
            object13 = object4;
            object12 = object5;
            object11 = object2;
            break;
        } 
        continue;
      } 
      MotionController motionController = this.mRelativeToController;
      if (motionController != null) {
        float[] arrayOfFloat2 = new float[2];
        float[] arrayOfFloat1 = new float[2];
        motionController.getCenter(paramFloat, arrayOfFloat2, arrayOfFloat1);
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
          paramView.setRotation((float)(object3 + Math.toDegrees(Math.atan2(f12, f13)))); 
        f = f11;
      } else {
        if (!Float.isNaN(object3)) {
          paramFloat = object10 / 2.0F;
          float f11 = object9 / 2.0F;
          paramView.setRotation((float)(0.0F + object3 + Math.toDegrees(Math.atan2((object6 + f11), (object7 + paramFloat)))));
        } 
        object = object1;
      } 
      if (paramView instanceof FloatLayout) {
        ((FloatLayout)paramView).layout(object, f, object + object5, f + object4);
        return;
      } 
      int i = (int)(object + 0.5F);
      int i1 = (int)(f + 0.5F);
      int j = (int)(object + 0.5F + object5);
      int n = (int)(0.5F + f + object4);
      int m = j - i;
      int k = n - i1;
      if (m != paramView.getMeasuredWidth() || k != paramView.getMeasuredHeight()) {
        b = 1;
      } else {
        b = 0;
      } 
      if (b != 0 || paramBoolean)
        paramView.measure(View.MeasureSpec.makeMeasureSpec(m, 1073741824), View.MeasureSpec.makeMeasureSpec(k, 1073741824)); 
      paramView.layout(i, i1, j, n);
      return;
      b++;
      object2 = SYNTHETIC_LOCAL_VARIABLE_10;
      object5 = SYNTHETIC_LOCAL_VARIABLE_11;
      object4 = SYNTHETIC_LOCAL_VARIABLE_22;
      object7 = SYNTHETIC_LOCAL_VARIABLE_23;
      object6 = SYNTHETIC_LOCAL_VARIABLE_24;
      object10 = SYNTHETIC_LOCAL_VARIABLE_25;
      object9 = SYNTHETIC_LOCAL_VARIABLE_27;
      object8 = SYNTHETIC_LOCAL_VARIABLE_28;
      object3 = SYNTHETIC_LOCAL_VARIABLE_26;
      object1 = SYNTHETIC_LOCAL_VARIABLE_29;
    } 
  }
  
  public void setupRelative(MotionController paramMotionController, MotionPaths paramMotionPaths) {
    double d1 = (this.x + this.width / 2.0F - paramMotionPaths.x - paramMotionPaths.width / 2.0F);
    double d2 = (this.y + this.height / 2.0F - paramMotionPaths.y - paramMotionPaths.height / 2.0F);
    this.mRelativeToController = paramMotionController;
    this.x = (float)Math.hypot(d2, d1);
    if (Float.isNaN(this.mRelativeAngle)) {
      this.y = (float)(Math.atan2(d2, d1) + 1.5707963267948966D);
    } else {
      this.y = (float)Math.toRadians(this.mRelativeAngle);
    } 
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\motion\widget\MotionPaths.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */