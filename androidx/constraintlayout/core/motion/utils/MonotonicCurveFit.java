package androidx.constraintlayout.core.motion.utils;

import java.util.Arrays;

public class MonotonicCurveFit extends CurveFit {
  private static final String TAG = "MonotonicCurveFit";
  
  private boolean mExtrapolate = true;
  
  double[] mSlopeTemp;
  
  private double[] mT;
  
  private double[][] mTangent;
  
  private double[][] mY;
  
  public MonotonicCurveFit(double[] paramArrayOfdouble, double[][] paramArrayOfdouble1) {
    int i = paramArrayOfdouble.length;
    int j = (paramArrayOfdouble1[0]).length;
    this.mSlopeTemp = new double[j];
    double[][] arrayOfDouble2 = new double[i - 1][j];
    double[][] arrayOfDouble1 = new double[i][j];
    byte b;
    for (b = 0; b < j; b++) {
      for (byte b1 = 0; b1 < i - 1; b1++) {
        double d2 = paramArrayOfdouble[b1 + 1];
        double d1 = paramArrayOfdouble[b1];
        arrayOfDouble2[b1][b] = (paramArrayOfdouble1[b1 + 1][b] - paramArrayOfdouble1[b1][b]) / (d2 - d1);
        if (b1 == 0) {
          arrayOfDouble1[b1][b] = arrayOfDouble2[b1][b];
        } else {
          arrayOfDouble1[b1][b] = (arrayOfDouble2[b1 - 1][b] + arrayOfDouble2[b1][b]) * 0.5D;
        } 
      } 
      arrayOfDouble1[i - 1][b] = arrayOfDouble2[i - 2][b];
    } 
    for (b = 0; b < i - 1; b++) {
      for (byte b1 = 0; b1 < j; b1++) {
        if (arrayOfDouble2[b][b1] == 0.0D) {
          arrayOfDouble1[b][b1] = 0.0D;
          arrayOfDouble1[b + 1][b1] = 0.0D;
        } else {
          double d2 = arrayOfDouble1[b][b1] / arrayOfDouble2[b][b1];
          double d1 = arrayOfDouble1[b + 1][b1] / arrayOfDouble2[b][b1];
          double d3 = Math.hypot(d2, d1);
          if (d3 > 9.0D) {
            d3 = 3.0D / d3;
            arrayOfDouble1[b][b1] = d3 * d2 * arrayOfDouble2[b][b1];
            arrayOfDouble1[b + 1][b1] = d3 * d1 * arrayOfDouble2[b][b1];
          } 
        } 
      } 
    } 
    this.mT = paramArrayOfdouble;
    this.mY = paramArrayOfdouble1;
    this.mTangent = arrayOfDouble1;
  }
  
  public static MonotonicCurveFit buildWave(String paramString) {
    double[] arrayOfDouble = new double[paramString.length() / 2];
    int i = paramString.indexOf('(') + 1;
    int j = paramString.indexOf(',', i);
    byte b;
    for (b = 0; j != -1; b++) {
      arrayOfDouble[b] = Double.parseDouble(paramString.substring(i, j).trim());
      i = ++j;
      j = paramString.indexOf(',', j);
    } 
    arrayOfDouble[b] = Double.parseDouble(paramString.substring(i, paramString.indexOf(')', i)).trim());
    return buildWave(Arrays.copyOf(arrayOfDouble, b + 1));
  }
  
  private static MonotonicCurveFit buildWave(double[] paramArrayOfdouble) {
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
    return new MonotonicCurveFit(arrayOfDouble, arrayOfDouble1);
  }
  
  private static double diff(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6) {
    double d = paramDouble2 * paramDouble2;
    return -6.0D * d * paramDouble4 + paramDouble2 * 6.0D * paramDouble4 + d * 6.0D * paramDouble3 - 6.0D * paramDouble2 * paramDouble3 + paramDouble1 * 3.0D * paramDouble6 * d + 3.0D * paramDouble1 * paramDouble5 * d - 2.0D * paramDouble1 * paramDouble6 * paramDouble2 - 4.0D * paramDouble1 * paramDouble5 * paramDouble2 + paramDouble1 * paramDouble5;
  }
  
  private static double interpolate(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6) {
    double d2 = paramDouble2 * paramDouble2;
    double d1 = d2 * paramDouble2;
    return -2.0D * d1 * paramDouble4 + d2 * 3.0D * paramDouble4 + d1 * 2.0D * paramDouble3 - 3.0D * d2 * paramDouble3 + paramDouble3 + paramDouble1 * paramDouble6 * d1 + paramDouble1 * paramDouble5 * d1 - paramDouble1 * paramDouble6 * d2 - paramDouble1 * 2.0D * paramDouble5 * d2 + paramDouble1 * paramDouble5 * paramDouble2;
  }
  
  public double getPos(double paramDouble, int paramInt) {
    double[] arrayOfDouble = this.mT;
    int i = arrayOfDouble.length;
    if (this.mExtrapolate) {
      if (paramDouble <= arrayOfDouble[0])
        return this.mY[0][paramInt] + (paramDouble - arrayOfDouble[0]) * getSlope(arrayOfDouble[0], paramInt); 
      if (paramDouble >= arrayOfDouble[i - 1])
        return this.mY[i - 1][paramInt] + (paramDouble - arrayOfDouble[i - 1]) * getSlope(arrayOfDouble[i - 1], paramInt); 
    } else {
      if (paramDouble <= arrayOfDouble[0])
        return this.mY[0][paramInt]; 
      if (paramDouble >= arrayOfDouble[i - 1])
        return this.mY[i - 1][paramInt]; 
    } 
    for (byte b = 0; b < i - 1; b++) {
      arrayOfDouble = this.mT;
      if (paramDouble == arrayOfDouble[b])
        return this.mY[b][paramInt]; 
      if (paramDouble < arrayOfDouble[b + 1]) {
        double d1 = arrayOfDouble[b + 1] - arrayOfDouble[b];
        double d3 = (paramDouble - arrayOfDouble[b]) / d1;
        double[][] arrayOfDouble1 = this.mY;
        double d2 = arrayOfDouble1[b][paramInt];
        paramDouble = arrayOfDouble1[b + 1][paramInt];
        arrayOfDouble1 = this.mTangent;
        return interpolate(d1, d3, d2, paramDouble, arrayOfDouble1[b][paramInt], arrayOfDouble1[b + 1][paramInt]);
      } 
    } 
    return 0.0D;
  }
  
  public void getPos(double paramDouble, double[] paramArrayOfdouble) {
    double[] arrayOfDouble = this.mT;
    int j = arrayOfDouble.length;
    int i = (this.mY[0]).length;
    if (this.mExtrapolate) {
      if (paramDouble <= arrayOfDouble[0]) {
        getSlope(arrayOfDouble[0], this.mSlopeTemp);
        for (byte b1 = 0; b1 < i; b1++)
          paramArrayOfdouble[b1] = this.mY[0][b1] + (paramDouble - this.mT[0]) * this.mSlopeTemp[b1]; 
        return;
      } 
      if (paramDouble >= arrayOfDouble[j - 1]) {
        getSlope(arrayOfDouble[j - 1], this.mSlopeTemp);
        for (byte b1 = 0; b1 < i; b1++)
          paramArrayOfdouble[b1] = this.mY[j - 1][b1] + (paramDouble - this.mT[j - 1]) * this.mSlopeTemp[b1]; 
        return;
      } 
    } else {
      if (paramDouble <= arrayOfDouble[0]) {
        for (byte b1 = 0; b1 < i; b1++)
          paramArrayOfdouble[b1] = this.mY[0][b1]; 
        return;
      } 
      if (paramDouble >= arrayOfDouble[j - 1]) {
        for (byte b1 = 0; b1 < i; b1++)
          paramArrayOfdouble[b1] = this.mY[j - 1][b1]; 
        return;
      } 
    } 
    for (byte b = 0; b < j - 1; b++) {
      if (paramDouble == this.mT[b])
        for (byte b1 = 0; b1 < i; b1++)
          paramArrayOfdouble[b1] = this.mY[b][b1];  
      arrayOfDouble = this.mT;
      if (paramDouble < arrayOfDouble[b + 1]) {
        double d1 = arrayOfDouble[b + 1] - arrayOfDouble[b];
        double d2 = (paramDouble - arrayOfDouble[b]) / d1;
        for (byte b1 = 0; b1 < i; b1++) {
          double[][] arrayOfDouble1 = this.mY;
          paramDouble = arrayOfDouble1[b][b1];
          double d = arrayOfDouble1[b + 1][b1];
          arrayOfDouble1 = this.mTangent;
          paramArrayOfdouble[b1] = interpolate(d1, d2, paramDouble, d, arrayOfDouble1[b][b1], arrayOfDouble1[b + 1][b1]);
        } 
        return;
      } 
    } 
  }
  
  public void getPos(double paramDouble, float[] paramArrayOffloat) {
    double[] arrayOfDouble = this.mT;
    int j = arrayOfDouble.length;
    int i = (this.mY[0]).length;
    if (this.mExtrapolate) {
      if (paramDouble <= arrayOfDouble[0]) {
        getSlope(arrayOfDouble[0], this.mSlopeTemp);
        for (byte b1 = 0; b1 < i; b1++)
          paramArrayOffloat[b1] = (float)(this.mY[0][b1] + (paramDouble - this.mT[0]) * this.mSlopeTemp[b1]); 
        return;
      } 
      if (paramDouble >= arrayOfDouble[j - 1]) {
        getSlope(arrayOfDouble[j - 1], this.mSlopeTemp);
        for (byte b1 = 0; b1 < i; b1++)
          paramArrayOffloat[b1] = (float)(this.mY[j - 1][b1] + (paramDouble - this.mT[j - 1]) * this.mSlopeTemp[b1]); 
        return;
      } 
    } else {
      if (paramDouble <= arrayOfDouble[0]) {
        for (byte b1 = 0; b1 < i; b1++)
          paramArrayOffloat[b1] = (float)this.mY[0][b1]; 
        return;
      } 
      if (paramDouble >= arrayOfDouble[j - 1]) {
        for (byte b1 = 0; b1 < i; b1++)
          paramArrayOffloat[b1] = (float)this.mY[j - 1][b1]; 
        return;
      } 
    } 
    for (byte b = 0; b < j - 1; b++) {
      if (paramDouble == this.mT[b])
        for (byte b1 = 0; b1 < i; b1++)
          paramArrayOffloat[b1] = (float)this.mY[b][b1];  
      arrayOfDouble = this.mT;
      if (paramDouble < arrayOfDouble[b + 1]) {
        double d = arrayOfDouble[b + 1] - arrayOfDouble[b];
        paramDouble = (paramDouble - arrayOfDouble[b]) / d;
        for (byte b1 = 0; b1 < i; b1++) {
          double[][] arrayOfDouble1 = this.mY;
          double d1 = arrayOfDouble1[b][b1];
          double d2 = arrayOfDouble1[b + 1][b1];
          arrayOfDouble1 = this.mTangent;
          paramArrayOffloat[b1] = (float)interpolate(d, paramDouble, d1, d2, arrayOfDouble1[b][b1], arrayOfDouble1[b + 1][b1]);
        } 
        return;
      } 
    } 
  }
  
  public double getSlope(double paramDouble, int paramInt) {
    double[] arrayOfDouble = this.mT;
    int i = arrayOfDouble.length;
    if (paramDouble < arrayOfDouble[0]) {
      paramDouble = arrayOfDouble[0];
    } else if (paramDouble >= arrayOfDouble[i - 1]) {
      paramDouble = arrayOfDouble[i - 1];
    } 
    for (byte b = 0; b < i - 1; b++) {
      arrayOfDouble = this.mT;
      if (paramDouble <= arrayOfDouble[b + 1]) {
        double d1 = arrayOfDouble[b + 1] - arrayOfDouble[b];
        paramDouble = (paramDouble - arrayOfDouble[b]) / d1;
        double[][] arrayOfDouble1 = this.mY;
        double d3 = arrayOfDouble1[b][paramInt];
        double d2 = arrayOfDouble1[b + 1][paramInt];
        arrayOfDouble1 = this.mTangent;
        return diff(d1, paramDouble, d3, d2, arrayOfDouble1[b][paramInt], arrayOfDouble1[b + 1][paramInt]) / d1;
      } 
    } 
    return 0.0D;
  }
  
  public void getSlope(double paramDouble, double[] paramArrayOfdouble) {
    double[] arrayOfDouble = this.mT;
    int i = arrayOfDouble.length;
    int j = (this.mY[0]).length;
    if (paramDouble <= arrayOfDouble[0]) {
      paramDouble = arrayOfDouble[0];
    } else if (paramDouble >= arrayOfDouble[i - 1]) {
      paramDouble = arrayOfDouble[i - 1];
    } 
    for (byte b = 0; b < i - 1; b++) {
      arrayOfDouble = this.mT;
      if (paramDouble <= arrayOfDouble[b + 1]) {
        double d1 = arrayOfDouble[b + 1] - arrayOfDouble[b];
        double d2 = (paramDouble - arrayOfDouble[b]) / d1;
        for (i = 0; i < j; i++) {
          double[][] arrayOfDouble1 = this.mY;
          paramDouble = arrayOfDouble1[b][i];
          double d = arrayOfDouble1[b + 1][i];
          arrayOfDouble1 = this.mTangent;
          paramArrayOfdouble[i] = diff(d1, d2, paramDouble, d, arrayOfDouble1[b][i], arrayOfDouble1[b + 1][i]) / d1;
        } 
        break;
      } 
    } 
  }
  
  public double[] getTimePoints() {
    return this.mT;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\motio\\utils\MonotonicCurveFit.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */