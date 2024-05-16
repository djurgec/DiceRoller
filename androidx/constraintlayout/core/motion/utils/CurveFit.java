package androidx.constraintlayout.core.motion.utils;

public abstract class CurveFit {
  public static final int CONSTANT = 2;
  
  public static final int LINEAR = 1;
  
  public static final int SPLINE = 0;
  
  public static CurveFit get(int paramInt, double[] paramArrayOfdouble, double[][] paramArrayOfdouble1) {
    if (paramArrayOfdouble.length == 1)
      paramInt = 2; 
    switch (paramInt) {
      default:
        return new LinearCurveFit(paramArrayOfdouble, paramArrayOfdouble1);
      case 2:
        return new Constant(paramArrayOfdouble[0], paramArrayOfdouble1[0]);
      case 0:
        break;
    } 
    return new MonotonicCurveFit(paramArrayOfdouble, paramArrayOfdouble1);
  }
  
  public static CurveFit getArc(int[] paramArrayOfint, double[] paramArrayOfdouble, double[][] paramArrayOfdouble1) {
    return new ArcCurveFit(paramArrayOfint, paramArrayOfdouble, paramArrayOfdouble1);
  }
  
  public abstract double getPos(double paramDouble, int paramInt);
  
  public abstract void getPos(double paramDouble, double[] paramArrayOfdouble);
  
  public abstract void getPos(double paramDouble, float[] paramArrayOffloat);
  
  public abstract double getSlope(double paramDouble, int paramInt);
  
  public abstract void getSlope(double paramDouble, double[] paramArrayOfdouble);
  
  public abstract double[] getTimePoints();
  
  static class Constant extends CurveFit {
    double mTime;
    
    double[] mValue;
    
    Constant(double param1Double, double[] param1ArrayOfdouble) {
      this.mTime = param1Double;
      this.mValue = param1ArrayOfdouble;
    }
    
    public double getPos(double param1Double, int param1Int) {
      return this.mValue[param1Int];
    }
    
    public void getPos(double param1Double, double[] param1ArrayOfdouble) {
      double[] arrayOfDouble = this.mValue;
      System.arraycopy(arrayOfDouble, 0, param1ArrayOfdouble, 0, arrayOfDouble.length);
    }
    
    public void getPos(double param1Double, float[] param1ArrayOffloat) {
      byte b = 0;
      while (true) {
        double[] arrayOfDouble = this.mValue;
        if (b < arrayOfDouble.length) {
          param1ArrayOffloat[b] = (float)arrayOfDouble[b];
          b++;
          continue;
        } 
        break;
      } 
    }
    
    public double getSlope(double param1Double, int param1Int) {
      return 0.0D;
    }
    
    public void getSlope(double param1Double, double[] param1ArrayOfdouble) {
      for (byte b = 0; b < this.mValue.length; b++)
        param1ArrayOfdouble[b] = 0.0D; 
    }
    
    public double[] getTimePoints() {
      return new double[] { this.mTime };
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\motio\\utils\CurveFit.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */