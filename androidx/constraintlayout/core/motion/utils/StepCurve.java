package androidx.constraintlayout.core.motion.utils;

import java.util.Arrays;

public class StepCurve extends Easing {
  private static final boolean DEBUG = false;
  
  MonotonicCurveFit mCurveFit;
  
  StepCurve(String paramString) {
    this.str = paramString;
    double[] arrayOfDouble = new double[this.str.length() / 2];
    int j = paramString.indexOf('(') + 1;
    int i = paramString.indexOf(',', j);
    byte b = 0;
    while (i != -1) {
      arrayOfDouble[b] = Double.parseDouble(paramString.substring(j, i).trim());
      j = i + 1;
      i = j;
      int k = paramString.indexOf(',', j);
      b++;
      j = i;
      i = k;
    } 
    arrayOfDouble[b] = Double.parseDouble(paramString.substring(j, paramString.indexOf(')', j)).trim());
    this.mCurveFit = genSpline(Arrays.copyOf(arrayOfDouble, b + 1));
  }
  
  private static MonotonicCurveFit genSpline(String paramString) {
    String[] arrayOfString = paramString.split("\\s+");
    double[] arrayOfDouble = new double[arrayOfString.length];
    for (byte b = 0; b < arrayOfDouble.length; b++)
      arrayOfDouble[b] = Double.parseDouble(arrayOfString[b]); 
    return genSpline(arrayOfDouble);
  }
  
  private static MonotonicCurveFit genSpline(double[] paramArrayOfdouble) {
    int i = paramArrayOfdouble.length * 3 - 2;
    int j = paramArrayOfdouble.length - 1;
    double d = 1.0D / j;
    double[][] arrayOfDouble1 = new double[i][1];
    double[] arrayOfDouble = new double[i];
    for (i = 0; i < paramArrayOfdouble.length; i++) {
      double d1 = paramArrayOfdouble[i];
      arrayOfDouble1[i + j][0] = d1;
      arrayOfDouble[i + j] = i * d;
      if (i > 0) {
        arrayOfDouble1[j * 2 + i][0] = d1 + 1.0D;
        arrayOfDouble[j * 2 + i] = i * d + 1.0D;
        arrayOfDouble1[i - 1][0] = d1 - 1.0D - d;
        arrayOfDouble[i - 1] = i * d - 1.0D - d;
      } 
    } 
    MonotonicCurveFit monotonicCurveFit = new MonotonicCurveFit(arrayOfDouble, arrayOfDouble1);
    System.out.println(" 0 " + monotonicCurveFit.getPos(0.0D, 0));
    System.out.println(" 1 " + monotonicCurveFit.getPos(1.0D, 0));
    return monotonicCurveFit;
  }
  
  public double get(double paramDouble) {
    return this.mCurveFit.getPos(paramDouble, 0);
  }
  
  public double getDiff(double paramDouble) {
    return this.mCurveFit.getSlope(paramDouble, 0);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\motio\\utils\StepCurve.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */