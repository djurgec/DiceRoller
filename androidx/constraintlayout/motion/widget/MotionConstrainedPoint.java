package androidx.constraintlayout.motion.widget;

import android.graphics.Rect;
import android.os.Build;
import android.util.Log;
import android.view.View;
import androidx.constraintlayout.core.motion.utils.Easing;
import androidx.constraintlayout.motion.utils.ViewSpline;
import androidx.constraintlayout.widget.ConstraintAttribute;
import androidx.constraintlayout.widget.ConstraintSet;
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
  
  LinkedHashMap<String, ConstraintAttribute> attributes = new LinkedHashMap<>();
  
  private float elevation = 0.0F;
  
  private float height;
  
  private int mAnimateRelativeTo = -1;
  
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
  
  public void addValues(HashMap<String, ViewSpline> paramHashMap, int paramInt) {
    for (String str1 : paramHashMap.keySet()) {
      String str2;
      ViewSpline viewSpline = paramHashMap.get(str1);
      byte b = -1;
      switch (str1.hashCode()) {
        case 92909918:
          if (str1.equals("alpha"))
            b = 0; 
          break;
        case 37232917:
          if (str1.equals("transitionPathRotate"))
            b = 7; 
          break;
        case -4379043:
          if (str1.equals("elevation"))
            b = 1; 
          break;
        case -40300674:
          if (str1.equals("rotation"))
            b = 2; 
          break;
        case -760884509:
          if (str1.equals("transformPivotY"))
            b = 6; 
          break;
        case -760884510:
          if (str1.equals("transformPivotX"))
            b = 5; 
          break;
        case -908189617:
          if (str1.equals("scaleY"))
            b = 10; 
          break;
        case -908189618:
          if (str1.equals("scaleX"))
            b = 9; 
          break;
        case -1001078227:
          if (str1.equals("progress"))
            b = 8; 
          break;
        case -1225497655:
          if (str1.equals("translationZ"))
            b = 13; 
          break;
        case -1225497656:
          if (str1.equals("translationY"))
            b = 12; 
          break;
        case -1225497657:
          if (str1.equals("translationX"))
            b = 11; 
          break;
        case -1249320805:
          if (str1.equals("rotationY"))
            b = 4; 
          break;
        case -1249320806:
          if (str1.equals("rotationX"))
            b = 3; 
          break;
      } 
      float f1 = 1.0F;
      float f8 = 0.0F;
      float f3 = 0.0F;
      float f4 = 0.0F;
      float f5 = 0.0F;
      float f9 = 0.0F;
      float f6 = 0.0F;
      float f7 = 0.0F;
      float f12 = 0.0F;
      float f10 = 0.0F;
      float f2 = 0.0F;
      float f11 = 0.0F;
      switch (b) {
        default:
          if (str1.startsWith("CUSTOM")) {
            String str = str1.split(",")[1];
            if (this.attributes.containsKey(str)) {
              ConstraintAttribute constraintAttribute = this.attributes.get(str);
              if (viewSpline instanceof ViewSpline.CustomSet) {
                ((ViewSpline.CustomSet)viewSpline).setPoint(paramInt, constraintAttribute);
                continue;
              } 
              f1 = constraintAttribute.getValueToInterpolate();
              str2 = String.valueOf(viewSpline);
              Log.e("MotionPaths", (new StringBuilder(String.valueOf(str1).length() + 69 + String.valueOf(str2).length())).append(str1).append(" ViewSpline not a CustomSet frame = ").append(paramInt).append(", value").append(f1).append(str2).toString());
            } 
            continue;
          } 
          break;
        case 13:
          if (Float.isNaN(this.translationZ)) {
            f1 = f11;
          } else {
            f1 = this.translationZ;
          } 
          str2.setPoint(paramInt, f1);
          continue;
        case 12:
          if (Float.isNaN(this.translationY)) {
            f1 = f8;
          } else {
            f1 = this.translationY;
          } 
          str2.setPoint(paramInt, f1);
          continue;
        case 11:
          if (Float.isNaN(this.translationX)) {
            f1 = f3;
          } else {
            f1 = this.translationX;
          } 
          str2.setPoint(paramInt, f1);
          continue;
        case 10:
          if (!Float.isNaN(this.scaleY))
            f1 = this.scaleY; 
          str2.setPoint(paramInt, f1);
          continue;
        case 9:
          if (!Float.isNaN(this.scaleX))
            f1 = this.scaleX; 
          str2.setPoint(paramInt, f1);
          continue;
        case 8:
          if (Float.isNaN(this.mProgress)) {
            f1 = f4;
          } else {
            f1 = this.mProgress;
          } 
          str2.setPoint(paramInt, f1);
          continue;
        case 7:
          if (Float.isNaN(this.mPathRotate)) {
            f1 = f5;
          } else {
            f1 = this.mPathRotate;
          } 
          str2.setPoint(paramInt, f1);
          continue;
        case 6:
          if (Float.isNaN(this.mPivotY)) {
            f1 = f9;
          } else {
            f1 = this.mPivotY;
          } 
          str2.setPoint(paramInt, f1);
          continue;
        case 5:
          if (Float.isNaN(this.mPivotX)) {
            f1 = f6;
          } else {
            f1 = this.mPivotX;
          } 
          str2.setPoint(paramInt, f1);
          continue;
        case 4:
          if (Float.isNaN(this.rotationY)) {
            f1 = f7;
          } else {
            f1 = this.rotationY;
          } 
          str2.setPoint(paramInt, f1);
          continue;
        case 3:
          if (Float.isNaN(this.rotationX)) {
            f1 = f12;
          } else {
            f1 = this.rotationX;
          } 
          str2.setPoint(paramInt, f1);
          continue;
        case 2:
          if (Float.isNaN(this.rotation)) {
            f1 = f10;
          } else {
            f1 = this.rotation;
          } 
          str2.setPoint(paramInt, f1);
          continue;
        case 1:
          if (Float.isNaN(this.elevation)) {
            f1 = f2;
          } else {
            f1 = this.elevation;
          } 
          str2.setPoint(paramInt, f1);
          continue;
        case 0:
          if (!Float.isNaN(this.alpha))
            f1 = this.alpha; 
          str2.setPoint(paramInt, f1);
          continue;
      } 
      str1 = String.valueOf(str1);
      if (str1.length() != 0) {
        str1 = "UNKNOWN spline ".concat(str1);
      } else {
        str1 = new String("UNKNOWN spline ");
      } 
      Log.e("MotionPaths", str1);
    } 
  }
  
  public void applyParameters(View paramView) {
    float f;
    this.visibility = paramView.getVisibility();
    if (paramView.getVisibility() != 0) {
      f = 0.0F;
    } else {
      f = paramView.getAlpha();
    } 
    this.alpha = f;
    this.applyElevation = false;
    if (Build.VERSION.SDK_INT >= 21)
      this.elevation = paramView.getElevation(); 
    this.rotation = paramView.getRotation();
    this.rotationX = paramView.getRotationX();
    this.rotationY = paramView.getRotationY();
    this.scaleX = paramView.getScaleX();
    this.scaleY = paramView.getScaleY();
    this.mPivotX = paramView.getPivotX();
    this.mPivotY = paramView.getPivotY();
    this.translationX = paramView.getTranslationX();
    this.translationY = paramView.getTranslationY();
    if (Build.VERSION.SDK_INT >= 21)
      this.translationZ = paramView.getTranslationZ(); 
  }
  
  public void applyParameters(ConstraintSet.Constraint paramConstraint) {
    float f;
    this.mVisibilityMode = paramConstraint.propertySet.mVisibilityMode;
    this.visibility = paramConstraint.propertySet.visibility;
    if (paramConstraint.propertySet.visibility != 0 && this.mVisibilityMode == 0) {
      f = 0.0F;
    } else {
      f = paramConstraint.propertySet.alpha;
    } 
    this.alpha = f;
    this.applyElevation = paramConstraint.transform.applyElevation;
    this.elevation = paramConstraint.transform.elevation;
    this.rotation = paramConstraint.transform.rotation;
    this.rotationX = paramConstraint.transform.rotationX;
    this.rotationY = paramConstraint.transform.rotationY;
    this.scaleX = paramConstraint.transform.scaleX;
    this.scaleY = paramConstraint.transform.scaleY;
    this.mPivotX = paramConstraint.transform.transformPivotX;
    this.mPivotY = paramConstraint.transform.transformPivotY;
    this.translationX = paramConstraint.transform.translationX;
    this.translationY = paramConstraint.transform.translationY;
    this.translationZ = paramConstraint.transform.translationZ;
    this.mKeyFrameEasing = Easing.getInterpolator(paramConstraint.motion.mTransitionEasing);
    this.mPathRotate = paramConstraint.motion.mPathRotate;
    this.mDrawPath = paramConstraint.motion.mDrawPath;
    this.mAnimateRelativeTo = paramConstraint.motion.mAnimateRelativeTo;
    this.mProgress = paramConstraint.propertySet.mProgress;
    for (String str : paramConstraint.mCustomConstraints.keySet()) {
      ConstraintAttribute constraintAttribute = (ConstraintAttribute)paramConstraint.mCustomConstraints.get(str);
      if (constraintAttribute.isContinuous())
        this.attributes.put(str, constraintAttribute); 
    } 
  }
  
  public int compareTo(MotionConstrainedPoint paramMotionConstrainedPoint) {
    return Float.compare(this.position, paramMotionConstrainedPoint.position);
  }
  
  void different(MotionConstrainedPoint paramMotionConstrainedPoint, HashSet<String> paramHashSet) {
    if (diff(this.alpha, paramMotionConstrainedPoint.alpha))
      paramHashSet.add("alpha"); 
    if (diff(this.elevation, paramMotionConstrainedPoint.elevation))
      paramHashSet.add("elevation"); 
    int i = this.visibility;
    int j = paramMotionConstrainedPoint.visibility;
    if (i != j && this.mVisibilityMode == 0 && (i == 0 || j == 0))
      paramHashSet.add("alpha"); 
    if (diff(this.rotation, paramMotionConstrainedPoint.rotation))
      paramHashSet.add("rotation"); 
    if (!Float.isNaN(this.mPathRotate) || !Float.isNaN(paramMotionConstrainedPoint.mPathRotate))
      paramHashSet.add("transitionPathRotate"); 
    if (!Float.isNaN(this.mProgress) || !Float.isNaN(paramMotionConstrainedPoint.mProgress))
      paramHashSet.add("progress"); 
    if (diff(this.rotationX, paramMotionConstrainedPoint.rotationX))
      paramHashSet.add("rotationX"); 
    if (diff(this.rotationY, paramMotionConstrainedPoint.rotationY))
      paramHashSet.add("rotationY"); 
    if (diff(this.mPivotX, paramMotionConstrainedPoint.mPivotX))
      paramHashSet.add("transformPivotX"); 
    if (diff(this.mPivotY, paramMotionConstrainedPoint.mPivotY))
      paramHashSet.add("transformPivotY"); 
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
    ConstraintAttribute constraintAttribute = this.attributes.get(paramString);
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
    return ((ConstraintAttribute)this.attributes.get(paramString)).numberOfInterpolatedValues();
  }
  
  boolean hasCustomData(String paramString) {
    return this.attributes.containsKey(paramString);
  }
  
  void setBounds(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
    this.x = paramFloat1;
    this.y = paramFloat2;
    this.width = paramFloat3;
    this.height = paramFloat4;
  }
  
  public void setState(Rect paramRect, View paramView, int paramInt, float paramFloat) {
    setBounds(paramRect.left, paramRect.top, paramRect.width(), paramRect.height());
    applyParameters(paramView);
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
  
  public void setState(Rect paramRect, ConstraintSet paramConstraintSet, int paramInt1, int paramInt2) {
    float f;
    setBounds(paramRect.left, paramRect.top, paramRect.width(), paramRect.height());
    applyParameters(paramConstraintSet.getParameters(paramInt2));
    switch (paramInt1) {
      default:
        return;
      case 2:
      case 4:
        f = this.rotation + 90.0F;
        this.rotation = f;
        if (f > 180.0F)
          this.rotation = f - 360.0F; 
      case 1:
      case 3:
        break;
    } 
    this.rotation -= 90.0F;
  }
  
  public void setState(View paramView) {
    setBounds(paramView.getX(), paramView.getY(), paramView.getWidth(), paramView.getHeight());
    applyParameters(paramView);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\motion\widget\MotionConstrainedPoint.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */