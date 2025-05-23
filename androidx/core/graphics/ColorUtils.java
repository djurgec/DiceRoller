package androidx.core.graphics;

import android.graphics.Color;
import java.util.Objects;

public final class ColorUtils {
  private static final int MIN_ALPHA_SEARCH_MAX_ITERATIONS = 10;
  
  private static final int MIN_ALPHA_SEARCH_PRECISION = 1;
  
  private static final ThreadLocal<double[]> TEMP_ARRAY = (ThreadLocal)new ThreadLocal<>();
  
  private static final double XYZ_EPSILON = 0.008856D;
  
  private static final double XYZ_KAPPA = 903.3D;
  
  private static final double XYZ_WHITE_REFERENCE_X = 95.047D;
  
  private static final double XYZ_WHITE_REFERENCE_Y = 100.0D;
  
  private static final double XYZ_WHITE_REFERENCE_Z = 108.883D;
  
  public static int HSLToColor(float[] paramArrayOffloat) {
    float f1 = paramArrayOffloat[0];
    float f2 = paramArrayOffloat[1];
    float f3 = paramArrayOffloat[2];
    f2 = (1.0F - Math.abs(f3 * 2.0F - 1.0F)) * f2;
    f3 -= 0.5F * f2;
    float f4 = (1.0F - Math.abs(f1 / 60.0F % 2.0F - 1.0F)) * f2;
    int m = (int)f1 / 60;
    int i = 0;
    int j = 0;
    int k = 0;
    switch (m) {
      default:
        return Color.rgb(constrain(i, 0, 255), constrain(j, 0, 255), constrain(k, 0, 255));
      case 5:
      case 6:
        i = Math.round((f2 + f3) * 255.0F);
        j = Math.round(f3 * 255.0F);
        k = Math.round((f4 + f3) * 255.0F);
      case 4:
        i = Math.round((f4 + f3) * 255.0F);
        j = Math.round(f3 * 255.0F);
        k = Math.round((f2 + f3) * 255.0F);
      case 3:
        i = Math.round(f3 * 255.0F);
        j = Math.round((f4 + f3) * 255.0F);
        k = Math.round((f2 + f3) * 255.0F);
      case 2:
        i = Math.round(f3 * 255.0F);
        j = Math.round((f2 + f3) * 255.0F);
        k = Math.round((f4 + f3) * 255.0F);
      case 1:
        i = Math.round((f4 + f3) * 255.0F);
        j = Math.round((f2 + f3) * 255.0F);
        k = Math.round(255.0F * f3);
      case 0:
        break;
    } 
    i = Math.round((f2 + f3) * 255.0F);
    j = Math.round((f4 + f3) * 255.0F);
    k = Math.round(255.0F * f3);
  }
  
  public static int LABToColor(double paramDouble1, double paramDouble2, double paramDouble3) {
    double[] arrayOfDouble = getTempDouble3Array();
    LABToXYZ(paramDouble1, paramDouble2, paramDouble3, arrayOfDouble);
    return XYZToColor(arrayOfDouble[0], arrayOfDouble[1], arrayOfDouble[2]);
  }
  
  public static void LABToXYZ(double paramDouble1, double paramDouble2, double paramDouble3, double[] paramArrayOfdouble) {
    double d2 = (paramDouble1 + 16.0D) / 116.0D;
    double d3 = paramDouble2 / 500.0D + d2;
    double d1 = d2 - paramDouble3 / 200.0D;
    paramDouble2 = Math.pow(d3, 3.0D);
    if (paramDouble2 <= 0.008856D)
      paramDouble2 = (d3 * 116.0D - 16.0D) / 903.3D; 
    if (paramDouble1 > 7.9996247999999985D) {
      paramDouble1 = Math.pow(d2, 3.0D);
    } else {
      paramDouble1 /= 903.3D;
    } 
    paramDouble3 = Math.pow(d1, 3.0D);
    if (paramDouble3 <= 0.008856D)
      paramDouble3 = (116.0D * d1 - 16.0D) / 903.3D; 
    paramArrayOfdouble[0] = 95.047D * paramDouble2;
    paramArrayOfdouble[1] = 100.0D * paramDouble1;
    paramArrayOfdouble[2] = 108.883D * paramDouble3;
  }
  
  public static void RGBToHSL(int paramInt1, int paramInt2, int paramInt3, float[] paramArrayOffloat) {
    float f1 = paramInt1 / 255.0F;
    float f5 = paramInt2 / 255.0F;
    float f3 = paramInt3 / 255.0F;
    float f7 = Math.max(f1, Math.max(f5, f3));
    float f6 = Math.min(f1, Math.min(f5, f3));
    float f2 = f7 - f6;
    float f4 = (f7 + f6) / 2.0F;
    if (f7 == f6) {
      f1 = 0.0F;
      f2 = 0.0F;
    } else {
      if (f7 == f1) {
        f1 = (f5 - f3) / f2 % 6.0F;
      } else if (f7 == f5) {
        f1 = (f3 - f1) / f2 + 2.0F;
      } else {
        f1 = (f1 - f5) / f2 + 4.0F;
      } 
      f3 = f2 / (1.0F - Math.abs(2.0F * f4 - 1.0F));
      f2 = f1;
      f1 = f3;
    } 
    f3 = 60.0F * f2 % 360.0F;
    f2 = f3;
    if (f3 < 0.0F)
      f2 = f3 + 360.0F; 
    paramArrayOffloat[0] = constrain(f2, 0.0F, 360.0F);
    paramArrayOffloat[1] = constrain(f1, 0.0F, 1.0F);
    paramArrayOffloat[2] = constrain(f4, 0.0F, 1.0F);
  }
  
  public static void RGBToLAB(int paramInt1, int paramInt2, int paramInt3, double[] paramArrayOfdouble) {
    RGBToXYZ(paramInt1, paramInt2, paramInt3, paramArrayOfdouble);
    XYZToLAB(paramArrayOfdouble[0], paramArrayOfdouble[1], paramArrayOfdouble[2], paramArrayOfdouble);
  }
  
  public static void RGBToXYZ(int paramInt1, int paramInt2, int paramInt3, double[] paramArrayOfdouble) {
    if (paramArrayOfdouble.length == 3) {
      double d1 = paramInt1 / 255.0D;
      if (d1 < 0.04045D) {
        d1 /= 12.92D;
      } else {
        d1 = Math.pow((d1 + 0.055D) / 1.055D, 2.4D);
      } 
      double d2 = paramInt2 / 255.0D;
      if (d2 < 0.04045D) {
        d2 /= 12.92D;
      } else {
        d2 = Math.pow((d2 + 0.055D) / 1.055D, 2.4D);
      } 
      double d3 = paramInt3 / 255.0D;
      if (d3 < 0.04045D) {
        d3 /= 12.92D;
      } else {
        d3 = Math.pow((0.055D + d3) / 1.055D, 2.4D);
      } 
      paramArrayOfdouble[0] = (0.4124D * d1 + 0.3576D * d2 + 0.1805D * d3) * 100.0D;
      paramArrayOfdouble[1] = (0.2126D * d1 + 0.7152D * d2 + 0.0722D * d3) * 100.0D;
      paramArrayOfdouble[2] = (0.0193D * d1 + 0.1192D * d2 + 0.9505D * d3) * 100.0D;
      return;
    } 
    throw new IllegalArgumentException("outXyz must have a length of 3.");
  }
  
  public static int XYZToColor(double paramDouble1, double paramDouble2, double paramDouble3) {
    double d2 = (3.2406D * paramDouble1 + -1.5372D * paramDouble2 + -0.4986D * paramDouble3) / 100.0D;
    double d1 = (-0.9689D * paramDouble1 + 1.8758D * paramDouble2 + 0.0415D * paramDouble3) / 100.0D;
    paramDouble3 = (0.0557D * paramDouble1 + -0.204D * paramDouble2 + 1.057D * paramDouble3) / 100.0D;
    if (d2 > 0.0031308D) {
      paramDouble1 = Math.pow(d2, 0.4166666666666667D) * 1.055D - 0.055D;
    } else {
      paramDouble1 = d2 * 12.92D;
    } 
    if (d1 > 0.0031308D) {
      paramDouble2 = Math.pow(d1, 0.4166666666666667D) * 1.055D - 0.055D;
    } else {
      paramDouble2 = d1 * 12.92D;
    } 
    if (paramDouble3 > 0.0031308D) {
      paramDouble3 = Math.pow(paramDouble3, 0.4166666666666667D) * 1.055D - 0.055D;
    } else {
      paramDouble3 *= 12.92D;
    } 
    return Color.rgb(constrain((int)Math.round(paramDouble1 * 255.0D), 0, 255), constrain((int)Math.round(paramDouble2 * 255.0D), 0, 255), constrain((int)Math.round(255.0D * paramDouble3), 0, 255));
  }
  
  public static void XYZToLAB(double paramDouble1, double paramDouble2, double paramDouble3, double[] paramArrayOfdouble) {
    if (paramArrayOfdouble.length == 3) {
      paramDouble1 = pivotXyzComponent(paramDouble1 / 95.047D);
      paramDouble2 = pivotXyzComponent(paramDouble2 / 100.0D);
      paramDouble3 = pivotXyzComponent(paramDouble3 / 108.883D);
      paramArrayOfdouble[0] = Math.max(0.0D, 116.0D * paramDouble2 - 16.0D);
      paramArrayOfdouble[1] = (paramDouble1 - paramDouble2) * 500.0D;
      paramArrayOfdouble[2] = (paramDouble2 - paramDouble3) * 200.0D;
      return;
    } 
    throw new IllegalArgumentException("outLab must have a length of 3.");
  }
  
  public static int blendARGB(int paramInt1, int paramInt2, float paramFloat) {
    float f6 = 1.0F - paramFloat;
    float f2 = Color.alpha(paramInt1);
    float f7 = Color.alpha(paramInt2);
    float f3 = Color.red(paramInt1);
    float f9 = Color.red(paramInt2);
    float f4 = Color.green(paramInt1);
    float f5 = Color.green(paramInt2);
    float f8 = Color.blue(paramInt1);
    float f1 = Color.blue(paramInt2);
    return Color.argb((int)(f2 * f6 + f7 * paramFloat), (int)(f3 * f6 + f9 * paramFloat), (int)(f4 * f6 + f5 * paramFloat), (int)(f8 * f6 + f1 * paramFloat));
  }
  
  public static void blendHSL(float[] paramArrayOffloat1, float[] paramArrayOffloat2, float paramFloat, float[] paramArrayOffloat3) {
    if (paramArrayOffloat3.length == 3) {
      float f = 1.0F - paramFloat;
      paramArrayOffloat3[0] = circularInterpolate(paramArrayOffloat1[0], paramArrayOffloat2[0], paramFloat);
      paramArrayOffloat3[1] = paramArrayOffloat1[1] * f + paramArrayOffloat2[1] * paramFloat;
      paramArrayOffloat3[2] = paramArrayOffloat1[2] * f + paramArrayOffloat2[2] * paramFloat;
      return;
    } 
    throw new IllegalArgumentException("result must have a length of 3.");
  }
  
  public static void blendLAB(double[] paramArrayOfdouble1, double[] paramArrayOfdouble2, double paramDouble, double[] paramArrayOfdouble3) {
    if (paramArrayOfdouble3.length == 3) {
      double d = 1.0D - paramDouble;
      paramArrayOfdouble3[0] = paramArrayOfdouble1[0] * d + paramArrayOfdouble2[0] * paramDouble;
      paramArrayOfdouble3[1] = paramArrayOfdouble1[1] * d + paramArrayOfdouble2[1] * paramDouble;
      paramArrayOfdouble3[2] = paramArrayOfdouble1[2] * d + paramArrayOfdouble2[2] * paramDouble;
      return;
    } 
    throw new IllegalArgumentException("outResult must have a length of 3.");
  }
  
  public static double calculateContrast(int paramInt1, int paramInt2) {
    if (Color.alpha(paramInt2) == 255) {
      int i = paramInt1;
      if (Color.alpha(paramInt1) < 255)
        i = compositeColors(paramInt1, paramInt2); 
      double d1 = calculateLuminance(i) + 0.05D;
      double d2 = calculateLuminance(paramInt2) + 0.05D;
      return Math.max(d1, d2) / Math.min(d1, d2);
    } 
    throw new IllegalArgumentException("background can not be translucent: #" + Integer.toHexString(paramInt2));
  }
  
  public static double calculateLuminance(int paramInt) {
    double[] arrayOfDouble = getTempDouble3Array();
    colorToXYZ(paramInt, arrayOfDouble);
    return arrayOfDouble[1] / 100.0D;
  }
  
  public static int calculateMinimumAlpha(int paramInt1, int paramInt2, float paramFloat) {
    if (Color.alpha(paramInt2) == 255) {
      if (calculateContrast(setAlphaComponent(paramInt1, 255), paramInt2) < paramFloat)
        return -1; 
      byte b = 0;
      int j = 0;
      int i = 255;
      while (b <= 10 && i - j > 1) {
        int k = (j + i) / 2;
        if (calculateContrast(setAlphaComponent(paramInt1, k), paramInt2) < paramFloat) {
          j = k;
        } else {
          i = k;
        } 
        b++;
      } 
      return i;
    } 
    throw new IllegalArgumentException("background can not be translucent: #" + Integer.toHexString(paramInt2));
  }
  
  static float circularInterpolate(float paramFloat1, float paramFloat2, float paramFloat3) {
    float f2 = paramFloat1;
    float f1 = paramFloat2;
    if (Math.abs(paramFloat2 - paramFloat1) > 180.0F)
      if (paramFloat2 > paramFloat1) {
        f2 = paramFloat1 + 360.0F;
        f1 = paramFloat2;
      } else {
        f1 = paramFloat2 + 360.0F;
        f2 = paramFloat1;
      }  
    return ((f1 - f2) * paramFloat3 + f2) % 360.0F;
  }
  
  public static void colorToHSL(int paramInt, float[] paramArrayOffloat) {
    RGBToHSL(Color.red(paramInt), Color.green(paramInt), Color.blue(paramInt), paramArrayOffloat);
  }
  
  public static void colorToLAB(int paramInt, double[] paramArrayOfdouble) {
    RGBToLAB(Color.red(paramInt), Color.green(paramInt), Color.blue(paramInt), paramArrayOfdouble);
  }
  
  public static void colorToXYZ(int paramInt, double[] paramArrayOfdouble) {
    RGBToXYZ(Color.red(paramInt), Color.green(paramInt), Color.blue(paramInt), paramArrayOfdouble);
  }
  
  private static int compositeAlpha(int paramInt1, int paramInt2) {
    return 255 - (255 - paramInt2) * (255 - paramInt1) / 255;
  }
  
  public static int compositeColors(int paramInt1, int paramInt2) {
    int i = Color.alpha(paramInt2);
    int j = Color.alpha(paramInt1);
    int k = compositeAlpha(j, i);
    return Color.argb(k, compositeComponent(Color.red(paramInt1), j, Color.red(paramInt2), i, k), compositeComponent(Color.green(paramInt1), j, Color.green(paramInt2), i, k), compositeComponent(Color.blue(paramInt1), j, Color.blue(paramInt2), i, k));
  }
  
  public static Color compositeColors(Color paramColor1, Color paramColor2) {
    if (Objects.equals(paramColor1.getModel(), paramColor2.getModel())) {
      if (!Objects.equals(paramColor2.getColorSpace(), paramColor1.getColorSpace()))
        paramColor1 = paramColor1.convert(paramColor2.getColorSpace()); 
      float[] arrayOfFloat2 = paramColor1.getComponents();
      float[] arrayOfFloat1 = paramColor2.getComponents();
      float f4 = paramColor1.alpha();
      float f3 = paramColor2.alpha() * (1.0F - f4);
      int i = paramColor2.getComponentCount() - 1;
      arrayOfFloat1[i] = f4 + f3;
      float f2 = f4;
      float f1 = f3;
      if (arrayOfFloat1[i] > 0.0F) {
        f2 = f4 / arrayOfFloat1[i];
        f1 = f3 / arrayOfFloat1[i];
      } 
      for (byte b = 0; b < i; b++)
        arrayOfFloat1[b] = arrayOfFloat2[b] * f2 + arrayOfFloat1[b] * f1; 
      return Color.valueOf(arrayOfFloat1, paramColor2.getColorSpace());
    } 
    throw new IllegalArgumentException("Color models must match (" + paramColor1.getModel() + " vs. " + paramColor2.getModel() + ")");
  }
  
  private static int compositeComponent(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
    return (paramInt5 == 0) ? 0 : ((paramInt1 * 255 * paramInt2 + paramInt3 * paramInt4 * (255 - paramInt2)) / paramInt5 * 255);
  }
  
  private static float constrain(float paramFloat1, float paramFloat2, float paramFloat3) {
    if (paramFloat1 < paramFloat2) {
      paramFloat1 = paramFloat2;
    } else if (paramFloat1 > paramFloat3) {
      paramFloat1 = paramFloat3;
    } 
    return paramFloat1;
  }
  
  private static int constrain(int paramInt1, int paramInt2, int paramInt3) {
    if (paramInt1 < paramInt2) {
      paramInt1 = paramInt2;
    } else if (paramInt1 > paramInt3) {
      paramInt1 = paramInt3;
    } 
    return paramInt1;
  }
  
  public static double distanceEuclidean(double[] paramArrayOfdouble1, double[] paramArrayOfdouble2) {
    return Math.sqrt(Math.pow(paramArrayOfdouble1[0] - paramArrayOfdouble2[0], 2.0D) + Math.pow(paramArrayOfdouble1[1] - paramArrayOfdouble2[1], 2.0D) + Math.pow(paramArrayOfdouble1[2] - paramArrayOfdouble2[2], 2.0D));
  }
  
  private static double[] getTempDouble3Array() {
    ThreadLocal<double[]> threadLocal = TEMP_ARRAY;
    double[] arrayOfDouble2 = threadLocal.get();
    double[] arrayOfDouble1 = arrayOfDouble2;
    if (arrayOfDouble2 == null) {
      arrayOfDouble1 = new double[3];
      threadLocal.set(arrayOfDouble1);
    } 
    return arrayOfDouble1;
  }
  
  private static double pivotXyzComponent(double paramDouble) {
    if (paramDouble > 0.008856D) {
      paramDouble = Math.pow(paramDouble, 0.3333333333333333D);
    } else {
      paramDouble = (903.3D * paramDouble + 16.0D) / 116.0D;
    } 
    return paramDouble;
  }
  
  public static int setAlphaComponent(int paramInt1, int paramInt2) {
    if (paramInt2 >= 0 && paramInt2 <= 255)
      return 0xFFFFFF & paramInt1 | paramInt2 << 24; 
    throw new IllegalArgumentException("alpha must be between 0 and 255.");
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\graphics\ColorUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */