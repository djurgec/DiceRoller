package androidx.constraintlayout.core.motion.utils;

public class LinearCurveFit extends CurveFit {
  private static final String TAG = "LinearCurveFit";
  
  private boolean mExtrapolate = true;
  
  double[] mSlopeTemp;
  
  private double[] mT;
  
  private double mTotalLength = Double.NaN;
  
  private double[][] mY;
  
  public LinearCurveFit(double[] paramArrayOfdouble, double[][] paramArrayOfdouble1) {
    int i = paramArrayOfdouble.length;
    i = (paramArrayOfdouble1[0]).length;
    this.mSlopeTemp = new double[i];
    this.mT = paramArrayOfdouble;
    this.mY = paramArrayOfdouble1;
    if (i > 2) {
      double d3 = 0.0D;
      double d2 = 0.0D;
      double d1 = 0.0D;
      for (byte b = 0; b < paramArrayOfdouble.length; b++) {
        double d5 = paramArrayOfdouble1[b][0];
        double d4 = paramArrayOfdouble1[b][0];
        if (b > 0)
          d3 += Math.hypot(d5 - d2, d4 - d1); 
        d2 = d5;
        d1 = d4;
      } 
      this.mTotalLength = 0.0D;
    } 
  }
  
  private double getLength2D(double paramDouble) {
    if (Double.isNaN(this.mTotalLength))
      return 0.0D; 
    double[] arrayOfDouble = this.mT;
    int i = arrayOfDouble.length;
    if (paramDouble <= arrayOfDouble[0])
      return 0.0D; 
    if (paramDouble >= arrayOfDouble[i - 1])
      return this.mTotalLength; 
    double d2 = 0.0D;
    double d3 = 0.0D;
    double d1 = 0.0D;
    byte b = 0;
    while (b < i - 1) {
      double[][] arrayOfDouble2 = this.mY;
      double d5 = arrayOfDouble2[b][0];
      double d6 = arrayOfDouble2[b][1];
      if (b > 0) {
        d1 = d2 + Math.hypot(d5 - d3, d6 - d1);
      } else {
        d1 = d2;
      } 
      d3 = d5;
      double d4 = d6;
      double[] arrayOfDouble1 = this.mT;
      if (paramDouble == arrayOfDouble1[b])
        return d1; 
      if (paramDouble < arrayOfDouble1[b + 1]) {
        d2 = arrayOfDouble1[b + 1];
        d4 = arrayOfDouble1[b];
        d4 = (paramDouble - arrayOfDouble1[b]) / (d2 - d4);
        double[][] arrayOfDouble3 = this.mY;
        paramDouble = arrayOfDouble3[b][0];
        d2 = arrayOfDouble3[b + 1][0];
        return d1 + Math.hypot(d6 - (1.0D - d4) * arrayOfDouble3[b][1] + arrayOfDouble3[b + 1][1] * d4, d5 - (1.0D - d4) * paramDouble + d2 * d4);
      } 
      b++;
      d2 = d1;
      d1 = d4;
    } 
    return 0.0D;
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
        double d2 = arrayOfDouble[b + 1];
        double d1 = arrayOfDouble[b];
        paramDouble = (paramDouble - arrayOfDouble[b]) / (d2 - d1);
        double[][] arrayOfDouble1 = this.mY;
        return (1.0D - paramDouble) * arrayOfDouble1[b][paramInt] + arrayOfDouble1[b + 1][paramInt] * paramDouble;
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
        double d1 = arrayOfDouble[b + 1];
        double d2 = arrayOfDouble[b];
        paramDouble = (paramDouble - arrayOfDouble[b]) / (d1 - d2);
        for (byte b1 = 0; b1 < i; b1++) {
          double[][] arrayOfDouble1 = this.mY;
          paramArrayOfdouble[b1] = (1.0D - paramDouble) * arrayOfDouble1[b][b1] + arrayOfDouble1[b + 1][b1] * paramDouble;
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
        double d1 = arrayOfDouble[b + 1];
        double d2 = arrayOfDouble[b];
        paramDouble = (paramDouble - arrayOfDouble[b]) / (d1 - d2);
        for (byte b1 = 0; b1 < i; b1++) {
          double[][] arrayOfDouble1 = this.mY;
          paramArrayOffloat[b1] = (float)((1.0D - paramDouble) * arrayOfDouble1[b][b1] + arrayOfDouble1[b + 1][b1] * paramDouble);
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
        double d = arrayOfDouble[b + 1] - arrayOfDouble[b];
        paramDouble = (paramDouble - arrayOfDouble[b]) / d;
        double[][] arrayOfDouble1 = this.mY;
        paramDouble = arrayOfDouble1[b][paramInt];
        return (arrayOfDouble1[b + 1][paramInt] - paramDouble) / d;
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
        double d = arrayOfDouble[b + 1] - arrayOfDouble[b];
        paramDouble = (paramDouble - arrayOfDouble[b]) / d;
        for (i = 0; i < j; i++) {
          double[][] arrayOfDouble1 = this.mY;
          paramDouble = arrayOfDouble1[b][i];
          paramArrayOfdouble[i] = (arrayOfDouble1[b + 1][i] - paramDouble) / d;
        } 
        break;
      } 
    } 
  }
  
  public double[] getTimePoints() {
    return this.mT;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\motio\\utils\LinearCurveFit.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */