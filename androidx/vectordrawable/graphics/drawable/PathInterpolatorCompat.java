package androidx.vectordrawable.graphics.drawable;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.animation.Interpolator;
import androidx.core.content.res.TypedArrayUtils;
import androidx.core.graphics.PathParser;
import org.xmlpull.v1.XmlPullParser;

public class PathInterpolatorCompat implements Interpolator {
  public static final double EPSILON = 1.0E-5D;
  
  public static final int MAX_NUM_POINTS = 3000;
  
  private static final float PRECISION = 0.002F;
  
  private float[] mX;
  
  private float[] mY;
  
  public PathInterpolatorCompat(Context paramContext, AttributeSet paramAttributeSet, XmlPullParser paramXmlPullParser) {
    this(paramContext.getResources(), paramContext.getTheme(), paramAttributeSet, paramXmlPullParser);
  }
  
  public PathInterpolatorCompat(Resources paramResources, Resources.Theme paramTheme, AttributeSet paramAttributeSet, XmlPullParser paramXmlPullParser) {
    TypedArray typedArray = TypedArrayUtils.obtainAttributes(paramResources, paramTheme, paramAttributeSet, AndroidResources.STYLEABLE_PATH_INTERPOLATOR);
    parseInterpolatorFromTypeArray(typedArray, paramXmlPullParser);
    typedArray.recycle();
  }
  
  private void initCubic(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
    Path path = new Path();
    path.moveTo(0.0F, 0.0F);
    path.cubicTo(paramFloat1, paramFloat2, paramFloat3, paramFloat4, 1.0F, 1.0F);
    initPath(path);
  }
  
  private void initPath(Path paramPath) {
    PathMeasure pathMeasure = new PathMeasure(paramPath, false);
    float f = pathMeasure.getLength();
    int i = Math.min(3000, (int)(f / 0.002F) + 1);
    if (i > 0) {
      this.mX = new float[i];
      this.mY = new float[i];
      float[] arrayOfFloat = new float[2];
      byte b;
      for (b = 0; b < i; b++) {
        pathMeasure.getPosTan(b * f / (i - 1), arrayOfFloat, null);
        this.mX[b] = arrayOfFloat[0];
        this.mY[b] = arrayOfFloat[1];
      } 
      if (Math.abs(this.mX[0]) <= 1.0E-5D && Math.abs(this.mY[0]) <= 1.0E-5D && Math.abs(this.mX[i - 1] - 1.0F) <= 1.0E-5D && Math.abs(this.mY[i - 1] - 1.0F) <= 1.0E-5D) {
        f = 0.0F;
        byte b1 = 0;
        b = 0;
        while (b < i) {
          arrayOfFloat = this.mX;
          float f1 = arrayOfFloat[b1];
          if (f1 >= f) {
            arrayOfFloat[b] = f1;
            f = f1;
            b++;
            b1++;
            continue;
          } 
          throw new IllegalArgumentException("The Path cannot loop back on itself, x :" + f1);
        } 
        if (!pathMeasure.nextContour())
          return; 
        throw new IllegalArgumentException("The Path should be continuous, can't have 2+ contours");
      } 
      throw new IllegalArgumentException("The Path must start at (0,0) and end at (1,1) start: " + this.mX[0] + "," + this.mY[0] + " end:" + this.mX[i - 1] + "," + this.mY[i - 1]);
    } 
    throw new IllegalArgumentException("The Path has a invalid length " + f);
  }
  
  private void initQuad(float paramFloat1, float paramFloat2) {
    Path path = new Path();
    path.moveTo(0.0F, 0.0F);
    path.quadTo(paramFloat1, paramFloat2, 1.0F, 1.0F);
    initPath(path);
  }
  
  private void parseInterpolatorFromTypeArray(TypedArray paramTypedArray, XmlPullParser paramXmlPullParser) {
    String str;
    Path path;
    if (TypedArrayUtils.hasAttribute(paramXmlPullParser, "pathData")) {
      str = TypedArrayUtils.getNamedString(paramTypedArray, paramXmlPullParser, "pathData", 4);
      path = PathParser.createPathFromPathData(str);
      if (path != null) {
        initPath(path);
      } else {
        throw new InflateException("The path is null, which is created from " + str);
      } 
    } else {
      if (TypedArrayUtils.hasAttribute((XmlPullParser)path, "controlX1")) {
        if (TypedArrayUtils.hasAttribute((XmlPullParser)path, "controlY1")) {
          float f1 = TypedArrayUtils.getNamedFloat((TypedArray)str, (XmlPullParser)path, "controlX1", 0, 0.0F);
          float f2 = TypedArrayUtils.getNamedFloat((TypedArray)str, (XmlPullParser)path, "controlY1", 1, 0.0F);
          boolean bool = TypedArrayUtils.hasAttribute((XmlPullParser)path, "controlX2");
          if (bool == TypedArrayUtils.hasAttribute((XmlPullParser)path, "controlY2")) {
            if (!bool) {
              initQuad(f1, f2);
            } else {
              initCubic(f1, f2, TypedArrayUtils.getNamedFloat((TypedArray)str, (XmlPullParser)path, "controlX2", 2, 0.0F), TypedArrayUtils.getNamedFloat((TypedArray)str, (XmlPullParser)path, "controlY2", 3, 0.0F));
            } 
            return;
          } 
          throw new InflateException("pathInterpolator requires both controlX2 and controlY2 for cubic Beziers.");
        } 
        throw new InflateException("pathInterpolator requires the controlY1 attribute");
      } 
      throw new InflateException("pathInterpolator requires the controlX1 attribute");
    } 
  }
  
  public float getInterpolation(float paramFloat) {
    if (paramFloat <= 0.0F)
      return 0.0F; 
    if (paramFloat >= 1.0F)
      return 1.0F; 
    int j = 0;
    int i = this.mX.length - 1;
    while (i - j > 1) {
      int k = (j + i) / 2;
      if (paramFloat < this.mX[k]) {
        i = k;
        continue;
      } 
      j = k;
    } 
    float[] arrayOfFloat = this.mX;
    float f = arrayOfFloat[i] - arrayOfFloat[j];
    if (f == 0.0F)
      return this.mY[j]; 
    paramFloat = (paramFloat - arrayOfFloat[j]) / f;
    arrayOfFloat = this.mY;
    f = arrayOfFloat[j];
    return (arrayOfFloat[i] - f) * paramFloat + f;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\vectordrawable\graphics\drawable\PathInterpolatorCompat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */