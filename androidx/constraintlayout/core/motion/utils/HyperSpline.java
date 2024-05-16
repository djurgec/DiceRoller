package androidx.constraintlayout.core.motion.utils;

public class HyperSpline {
  double[][] mCtl;
  
  Cubic[][] mCurve;
  
  double[] mCurveLength;
  
  int mDimensionality;
  
  int mPoints;
  
  double mTotalLength;
  
  public HyperSpline() {}
  
  public HyperSpline(double[][] paramArrayOfdouble) {
    setup(paramArrayOfdouble);
  }
  
  static Cubic[] calcNaturalCubic(int paramInt, double[] paramArrayOfdouble) {
    double[] arrayOfDouble2 = new double[paramInt];
    double[] arrayOfDouble3 = new double[paramInt];
    double[] arrayOfDouble1 = new double[paramInt];
    int i = paramInt - 1;
    arrayOfDouble2[0] = 0.5D;
    for (paramInt = 1; paramInt < i; paramInt++)
      arrayOfDouble2[paramInt] = 1.0D / (4.0D - arrayOfDouble2[paramInt - 1]); 
    arrayOfDouble2[i] = 1.0D / (2.0D - arrayOfDouble2[i - 1]);
    arrayOfDouble3[0] = (paramArrayOfdouble[1] - paramArrayOfdouble[0]) * 3.0D * arrayOfDouble2[0];
    for (paramInt = 1; paramInt < i; paramInt++)
      arrayOfDouble3[paramInt] = ((paramArrayOfdouble[paramInt + 1] - paramArrayOfdouble[paramInt - 1]) * 3.0D - arrayOfDouble3[paramInt - 1]) * arrayOfDouble2[paramInt]; 
    arrayOfDouble3[i] = ((paramArrayOfdouble[i] - paramArrayOfdouble[i - 1]) * 3.0D - arrayOfDouble3[i - 1]) * arrayOfDouble2[i];
    arrayOfDouble1[i] = arrayOfDouble3[i];
    for (paramInt = i - 1; paramInt >= 0; paramInt--)
      arrayOfDouble1[paramInt] = arrayOfDouble3[paramInt] - arrayOfDouble2[paramInt] * arrayOfDouble1[paramInt + 1]; 
    Cubic[] arrayOfCubic = new Cubic[i];
    for (paramInt = 0; paramInt < i; paramInt++)
      arrayOfCubic[paramInt] = new Cubic((float)paramArrayOfdouble[paramInt], arrayOfDouble1[paramInt], (paramArrayOfdouble[paramInt + 1] - paramArrayOfdouble[paramInt]) * 3.0D - arrayOfDouble1[paramInt] * 2.0D - arrayOfDouble1[paramInt + 1], (paramArrayOfdouble[paramInt] - paramArrayOfdouble[paramInt + 1]) * 2.0D + arrayOfDouble1[paramInt] + arrayOfDouble1[paramInt + 1]); 
    return arrayOfCubic;
  }
  
  public double approxLength(Cubic[] paramArrayOfCubic) {
    double d1 = 0.0D;
    int i = paramArrayOfCubic.length;
    double[] arrayOfDouble = new double[paramArrayOfCubic.length];
    double d2 = 0.0D;
    while (d2 < 1.0D) {
      double d3 = 0.0D;
      for (i = 0; i < paramArrayOfCubic.length; i++) {
        double d6 = arrayOfDouble[i];
        double d5 = paramArrayOfCubic[i].eval(d2);
        arrayOfDouble[i] = d5;
        d5 = d6 - d5;
        d3 += d5 * d5;
      } 
      double d4 = d1;
      if (d2 > 0.0D)
        d4 = d1 + Math.sqrt(d3); 
      d2 += 0.1D;
      d1 = d4;
    } 
    d2 = 0.0D;
    for (i = 0; i < paramArrayOfCubic.length; i++) {
      double d3 = arrayOfDouble[i];
      double d4 = paramArrayOfCubic[i].eval(1.0D);
      arrayOfDouble[i] = d4;
      d3 -= d4;
      d2 += d3 * d3;
    } 
    return d1 + Math.sqrt(d2);
  }
  
  public double getPos(double paramDouble, int paramInt) {
    double[] arrayOfDouble;
    paramDouble = this.mTotalLength * paramDouble;
    byte b = 0;
    while (true) {
      arrayOfDouble = this.mCurveLength;
      if (b < arrayOfDouble.length - 1 && arrayOfDouble[b] < paramDouble) {
        paramDouble -= arrayOfDouble[b];
        b++;
        continue;
      } 
      break;
    } 
    return this.mCurve[paramInt][b].eval(paramDouble / arrayOfDouble[b]);
  }
  
  public void getPos(double paramDouble, double[] paramArrayOfdouble) {
    paramDouble = this.mTotalLength * paramDouble;
    byte b1 = 0;
    while (true) {
      double[] arrayOfDouble = this.mCurveLength;
      if (b1 < arrayOfDouble.length - 1 && arrayOfDouble[b1] < paramDouble) {
        paramDouble -= arrayOfDouble[b1];
        b1++;
        continue;
      } 
      break;
    } 
    for (byte b2 = 0; b2 < paramArrayOfdouble.length; b2++)
      paramArrayOfdouble[b2] = this.mCurve[b2][b1].eval(paramDouble / this.mCurveLength[b1]); 
  }
  
  public void getPos(double paramDouble, float[] paramArrayOffloat) {
    paramDouble = this.mTotalLength * paramDouble;
    byte b1 = 0;
    while (true) {
      double[] arrayOfDouble = this.mCurveLength;
      if (b1 < arrayOfDouble.length - 1 && arrayOfDouble[b1] < paramDouble) {
        paramDouble -= arrayOfDouble[b1];
        b1++;
        continue;
      } 
      break;
    } 
    for (byte b2 = 0; b2 < paramArrayOffloat.length; b2++)
      paramArrayOffloat[b2] = (float)this.mCurve[b2][b1].eval(paramDouble / this.mCurveLength[b1]); 
  }
  
  public void getVelocity(double paramDouble, double[] paramArrayOfdouble) {
    paramDouble = this.mTotalLength * paramDouble;
    byte b1 = 0;
    while (true) {
      double[] arrayOfDouble = this.mCurveLength;
      if (b1 < arrayOfDouble.length - 1 && arrayOfDouble[b1] < paramDouble) {
        paramDouble -= arrayOfDouble[b1];
        b1++;
        continue;
      } 
      break;
    } 
    for (byte b2 = 0; b2 < paramArrayOfdouble.length; b2++)
      paramArrayOfdouble[b2] = this.mCurve[b2][b1].vel(paramDouble / this.mCurveLength[b1]); 
  }
  
  public void setup(double[][] paramArrayOfdouble) {
    int i = (paramArrayOfdouble[0]).length;
    this.mDimensionality = i;
    int j = paramArrayOfdouble.length;
    this.mPoints = j;
    this.mCtl = new double[i][j];
    this.mCurve = new Cubic[this.mDimensionality][];
    for (i = 0; i < this.mDimensionality; i++) {
      for (j = 0; j < this.mPoints; j++)
        this.mCtl[i][j] = paramArrayOfdouble[j][i]; 
    } 
    i = 0;
    while (true) {
      j = this.mDimensionality;
      if (i < j) {
        Cubic[][] arrayOfCubic1 = this.mCurve;
        double[][] arrayOfDouble = this.mCtl;
        arrayOfCubic1[i] = calcNaturalCubic((arrayOfDouble[i]).length, arrayOfDouble[i]);
        i++;
        continue;
      } 
      this.mCurveLength = new double[this.mPoints - 1];
      this.mTotalLength = 0.0D;
      Cubic[] arrayOfCubic = new Cubic[j];
      for (i = 0; i < this.mCurveLength.length; i++) {
        for (j = 0; j < this.mDimensionality; j++)
          arrayOfCubic[j] = this.mCurve[j][i]; 
        double d2 = this.mTotalLength;
        double[] arrayOfDouble = this.mCurveLength;
        double d1 = approxLength(arrayOfCubic);
        arrayOfDouble[i] = d1;
        this.mTotalLength = d2 + d1;
      } 
      return;
    } 
  }
  
  public static class Cubic {
    double mA;
    
    double mB;
    
    double mC;
    
    double mD;
    
    public Cubic(double param1Double1, double param1Double2, double param1Double3, double param1Double4) {
      this.mA = param1Double1;
      this.mB = param1Double2;
      this.mC = param1Double3;
      this.mD = param1Double4;
    }
    
    public double eval(double param1Double) {
      return ((this.mD * param1Double + this.mC) * param1Double + this.mB) * param1Double + this.mA;
    }
    
    public double vel(double param1Double) {
      return (this.mD * 3.0D * param1Double + this.mC * 2.0D) * param1Double + this.mB;
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\motio\\utils\HyperSpline.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */