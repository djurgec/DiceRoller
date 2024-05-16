package androidx.constraintlayout.core.motion.utils;

import java.util.Arrays;

public class ArcCurveFit extends CurveFit {
  public static final int ARC_START_FLIP = 3;
  
  public static final int ARC_START_HORIZONTAL = 2;
  
  public static final int ARC_START_LINEAR = 0;
  
  public static final int ARC_START_VERTICAL = 1;
  
  private static final int START_HORIZONTAL = 2;
  
  private static final int START_LINEAR = 3;
  
  private static final int START_VERTICAL = 1;
  
  Arc[] mArcs;
  
  private boolean mExtrapolate = true;
  
  private final double[] mTime;
  
  public ArcCurveFit(int[] paramArrayOfint, double[] paramArrayOfdouble, double[][] paramArrayOfdouble1) {
    this.mTime = paramArrayOfdouble;
    this.mArcs = new Arc[paramArrayOfdouble.length - 1];
    byte b1 = 1;
    byte b2 = 1;
    byte b3 = 0;
    while (true) {
      Arc[] arrayOfArc = this.mArcs;
      if (b3 < arrayOfArc.length) {
        int i = paramArrayOfint[b3];
        byte b = 2;
        switch (i) {
          case 3:
            if (b2 == 1) {
              b1 = b;
            } else {
              b1 = 1;
            } 
            b2 = b1;
            break;
          case 2:
            b1 = 2;
            b2 = 2;
            break;
          case 1:
            b1 = 1;
            b2 = 1;
            break;
          case 0:
            b1 = 3;
            break;
        } 
        arrayOfArc[b3] = new Arc(b1, paramArrayOfdouble[b3], paramArrayOfdouble[b3 + 1], paramArrayOfdouble1[b3][0], paramArrayOfdouble1[b3][1], paramArrayOfdouble1[b3 + 1][0], paramArrayOfdouble1[b3 + 1][1]);
        b3++;
        continue;
      } 
      break;
    } 
  }
  
  public double getPos(double paramDouble, int paramInt) {
    double d;
    if (this.mExtrapolate) {
      if (paramDouble < (this.mArcs[0]).mTime1) {
        double d1 = (this.mArcs[0]).mTime1;
        paramDouble -= (this.mArcs[0]).mTime1;
        if ((this.mArcs[0]).linear)
          return (paramInt == 0) ? (this.mArcs[0].getLinearX(d1) + this.mArcs[0].getLinearDX(d1) * paramDouble) : (this.mArcs[0].getLinearY(d1) + this.mArcs[0].getLinearDY(d1) * paramDouble); 
        this.mArcs[0].setPoint(d1);
        return (paramInt == 0) ? (this.mArcs[0].getX() + this.mArcs[0].getDX() * paramDouble) : (this.mArcs[0].getY() + this.mArcs[0].getDY() * paramDouble);
      } 
      Arc[] arrayOfArc = this.mArcs;
      d = paramDouble;
      if (paramDouble > (arrayOfArc[arrayOfArc.length - 1]).mTime2) {
        arrayOfArc = this.mArcs;
        d = (arrayOfArc[arrayOfArc.length - 1]).mTime2;
        paramDouble -= d;
        arrayOfArc = this.mArcs;
        int i = arrayOfArc.length - 1;
        return (paramInt == 0) ? (arrayOfArc[i].getLinearX(d) + this.mArcs[i].getLinearDX(d) * paramDouble) : (arrayOfArc[i].getLinearY(d) + this.mArcs[i].getLinearDY(d) * paramDouble);
      } 
    } else if (paramDouble < (this.mArcs[0]).mTime1) {
      d = (this.mArcs[0]).mTime1;
    } else {
      Arc[] arrayOfArc = this.mArcs;
      d = paramDouble;
      if (paramDouble > (arrayOfArc[arrayOfArc.length - 1]).mTime2) {
        arrayOfArc = this.mArcs;
        d = (arrayOfArc[arrayOfArc.length - 1]).mTime2;
      } 
    } 
    byte b = 0;
    while (true) {
      Arc[] arrayOfArc = this.mArcs;
      if (b < arrayOfArc.length) {
        if (d <= (arrayOfArc[b]).mTime2) {
          if ((this.mArcs[b]).linear)
            return (paramInt == 0) ? this.mArcs[b].getLinearX(d) : this.mArcs[b].getLinearY(d); 
          this.mArcs[b].setPoint(d);
          return (paramInt == 0) ? this.mArcs[b].getX() : this.mArcs[b].getY();
        } 
        b++;
        continue;
      } 
      return Double.NaN;
    } 
  }
  
  public void getPos(double paramDouble, double[] paramArrayOfdouble) {
    double d;
    if (this.mExtrapolate) {
      if (paramDouble < (this.mArcs[0]).mTime1) {
        double d1 = (this.mArcs[0]).mTime1;
        paramDouble -= (this.mArcs[0]).mTime1;
        if ((this.mArcs[0]).linear) {
          paramArrayOfdouble[0] = this.mArcs[0].getLinearX(d1) + this.mArcs[0].getLinearDX(d1) * paramDouble;
          paramArrayOfdouble[1] = this.mArcs[0].getLinearY(d1) + this.mArcs[0].getLinearDY(d1) * paramDouble;
        } else {
          this.mArcs[0].setPoint(d1);
          paramArrayOfdouble[0] = this.mArcs[0].getX() + this.mArcs[0].getDX() * paramDouble;
          paramArrayOfdouble[1] = this.mArcs[0].getY() + this.mArcs[0].getDY() * paramDouble;
        } 
        return;
      } 
      Arc[] arrayOfArc = this.mArcs;
      d = paramDouble;
      if (paramDouble > (arrayOfArc[arrayOfArc.length - 1]).mTime2) {
        arrayOfArc = this.mArcs;
        d = (arrayOfArc[arrayOfArc.length - 1]).mTime2;
        double d1 = paramDouble - d;
        arrayOfArc = this.mArcs;
        int i = arrayOfArc.length - 1;
        if ((arrayOfArc[i]).linear) {
          paramArrayOfdouble[0] = this.mArcs[i].getLinearX(d) + this.mArcs[i].getLinearDX(d) * d1;
          paramArrayOfdouble[1] = this.mArcs[i].getLinearY(d) + this.mArcs[i].getLinearDY(d) * d1;
        } else {
          this.mArcs[i].setPoint(paramDouble);
          paramArrayOfdouble[0] = this.mArcs[i].getX() + this.mArcs[i].getDX() * d1;
          paramArrayOfdouble[1] = this.mArcs[i].getY() + this.mArcs[i].getDY() * d1;
        } 
        return;
      } 
    } else {
      double d1 = paramDouble;
      if (paramDouble < (this.mArcs[0]).mTime1)
        d1 = (this.mArcs[0]).mTime1; 
      Arc[] arrayOfArc = this.mArcs;
      d = d1;
      if (d1 > (arrayOfArc[arrayOfArc.length - 1]).mTime2) {
        arrayOfArc = this.mArcs;
        d = (arrayOfArc[arrayOfArc.length - 1]).mTime2;
      } 
    } 
    byte b = 0;
    while (true) {
      Arc[] arrayOfArc = this.mArcs;
      if (b < arrayOfArc.length) {
        if (d <= (arrayOfArc[b]).mTime2) {
          if ((this.mArcs[b]).linear) {
            paramArrayOfdouble[0] = this.mArcs[b].getLinearX(d);
            paramArrayOfdouble[1] = this.mArcs[b].getLinearY(d);
            return;
          } 
          this.mArcs[b].setPoint(d);
          paramArrayOfdouble[0] = this.mArcs[b].getX();
          paramArrayOfdouble[1] = this.mArcs[b].getY();
          return;
        } 
        b++;
        continue;
      } 
      break;
    } 
  }
  
  public void getPos(double paramDouble, float[] paramArrayOffloat) {
    double d;
    if (this.mExtrapolate) {
      if (paramDouble < (this.mArcs[0]).mTime1) {
        double d1 = (this.mArcs[0]).mTime1;
        paramDouble -= (this.mArcs[0]).mTime1;
        if ((this.mArcs[0]).linear) {
          paramArrayOffloat[0] = (float)(this.mArcs[0].getLinearX(d1) + this.mArcs[0].getLinearDX(d1) * paramDouble);
          paramArrayOffloat[1] = (float)(this.mArcs[0].getLinearY(d1) + this.mArcs[0].getLinearDY(d1) * paramDouble);
        } else {
          this.mArcs[0].setPoint(d1);
          paramArrayOffloat[0] = (float)(this.mArcs[0].getX() + this.mArcs[0].getDX() * paramDouble);
          paramArrayOffloat[1] = (float)(this.mArcs[0].getY() + this.mArcs[0].getDY() * paramDouble);
        } 
        return;
      } 
      Arc[] arrayOfArc = this.mArcs;
      d = paramDouble;
      if (paramDouble > (arrayOfArc[arrayOfArc.length - 1]).mTime2) {
        arrayOfArc = this.mArcs;
        d = (arrayOfArc[arrayOfArc.length - 1]).mTime2;
        double d1 = paramDouble - d;
        arrayOfArc = this.mArcs;
        int i = arrayOfArc.length - 1;
        if ((arrayOfArc[i]).linear) {
          paramArrayOffloat[0] = (float)(this.mArcs[i].getLinearX(d) + this.mArcs[i].getLinearDX(d) * d1);
          paramArrayOffloat[1] = (float)(this.mArcs[i].getLinearY(d) + this.mArcs[i].getLinearDY(d) * d1);
        } else {
          this.mArcs[i].setPoint(paramDouble);
          paramArrayOffloat[0] = (float)this.mArcs[i].getX();
          paramArrayOffloat[1] = (float)this.mArcs[i].getY();
        } 
        return;
      } 
    } else if (paramDouble < (this.mArcs[0]).mTime1) {
      d = (this.mArcs[0]).mTime1;
    } else {
      Arc[] arrayOfArc = this.mArcs;
      d = paramDouble;
      if (paramDouble > (arrayOfArc[arrayOfArc.length - 1]).mTime2) {
        arrayOfArc = this.mArcs;
        d = (arrayOfArc[arrayOfArc.length - 1]).mTime2;
      } 
    } 
    byte b = 0;
    while (true) {
      Arc[] arrayOfArc = this.mArcs;
      if (b < arrayOfArc.length) {
        if (d <= (arrayOfArc[b]).mTime2) {
          if ((this.mArcs[b]).linear) {
            paramArrayOffloat[0] = (float)this.mArcs[b].getLinearX(d);
            paramArrayOffloat[1] = (float)this.mArcs[b].getLinearY(d);
            return;
          } 
          this.mArcs[b].setPoint(d);
          paramArrayOffloat[0] = (float)this.mArcs[b].getX();
          paramArrayOffloat[1] = (float)this.mArcs[b].getY();
          return;
        } 
        b++;
        continue;
      } 
      break;
    } 
  }
  
  public double getSlope(double paramDouble, int paramInt) {
    double d = paramDouble;
    if (paramDouble < (this.mArcs[0]).mTime1)
      d = (this.mArcs[0]).mTime1; 
    Arc[] arrayOfArc = this.mArcs;
    paramDouble = d;
    if (d > (arrayOfArc[arrayOfArc.length - 1]).mTime2) {
      arrayOfArc = this.mArcs;
      paramDouble = (arrayOfArc[arrayOfArc.length - 1]).mTime2;
    } 
    byte b = 0;
    while (true) {
      arrayOfArc = this.mArcs;
      if (b < arrayOfArc.length) {
        if (paramDouble <= (arrayOfArc[b]).mTime2) {
          if ((this.mArcs[b]).linear)
            return (paramInt == 0) ? this.mArcs[b].getLinearDX(paramDouble) : this.mArcs[b].getLinearDY(paramDouble); 
          this.mArcs[b].setPoint(paramDouble);
          return (paramInt == 0) ? this.mArcs[b].getDX() : this.mArcs[b].getDY();
        } 
        b++;
        continue;
      } 
      return Double.NaN;
    } 
  }
  
  public void getSlope(double paramDouble, double[] paramArrayOfdouble) {
    double d;
    if (paramDouble < (this.mArcs[0]).mTime1) {
      d = (this.mArcs[0]).mTime1;
    } else {
      Arc[] arrayOfArc = this.mArcs;
      d = paramDouble;
      if (paramDouble > (arrayOfArc[arrayOfArc.length - 1]).mTime2) {
        arrayOfArc = this.mArcs;
        d = (arrayOfArc[arrayOfArc.length - 1]).mTime2;
      } 
    } 
    byte b = 0;
    while (true) {
      Arc[] arrayOfArc = this.mArcs;
      if (b < arrayOfArc.length) {
        if (d <= (arrayOfArc[b]).mTime2) {
          if ((this.mArcs[b]).linear) {
            paramArrayOfdouble[0] = this.mArcs[b].getLinearDX(d);
            paramArrayOfdouble[1] = this.mArcs[b].getLinearDY(d);
            return;
          } 
          this.mArcs[b].setPoint(d);
          paramArrayOfdouble[0] = this.mArcs[b].getDX();
          paramArrayOfdouble[1] = this.mArcs[b].getDY();
          return;
        } 
        b++;
        continue;
      } 
      break;
    } 
  }
  
  public double[] getTimePoints() {
    return this.mTime;
  }
  
  private static class Arc {
    private static final double EPSILON = 0.001D;
    
    private static final String TAG = "Arc";
    
    private static double[] ourPercent = new double[91];
    
    boolean linear;
    
    double mArcDistance;
    
    double mArcVelocity;
    
    double mEllipseA;
    
    double mEllipseB;
    
    double mEllipseCenterX;
    
    double mEllipseCenterY;
    
    double[] mLut;
    
    double mOneOverDeltaTime;
    
    double mTime1;
    
    double mTime2;
    
    double mTmpCosAngle;
    
    double mTmpSinAngle;
    
    boolean mVertical;
    
    double mX1;
    
    double mX2;
    
    double mY1;
    
    double mY2;
    
    Arc(int param1Int, double param1Double1, double param1Double2, double param1Double3, double param1Double4, double param1Double5, double param1Double6) {
      boolean bool1 = false;
      this.linear = false;
      boolean bool = true;
      if (param1Int == 1)
        bool1 = true; 
      this.mVertical = bool1;
      this.mTime1 = param1Double1;
      this.mTime2 = param1Double2;
      this.mOneOverDeltaTime = 1.0D / (param1Double2 - param1Double1);
      if (3 == param1Int)
        this.linear = true; 
      param1Double2 = param1Double5 - param1Double3;
      param1Double1 = param1Double6 - param1Double4;
      if (this.linear || Math.abs(param1Double2) < 0.001D || Math.abs(param1Double1) < 0.001D) {
        this.linear = true;
        this.mX1 = param1Double3;
        this.mX2 = param1Double5;
        this.mY1 = param1Double4;
        this.mY2 = param1Double6;
        param1Double3 = Math.hypot(param1Double1, param1Double2);
        this.mArcDistance = param1Double3;
        this.mArcVelocity = param1Double3 * this.mOneOverDeltaTime;
        param1Double4 = this.mTime2;
        param1Double3 = this.mTime1;
        this.mEllipseCenterX = param1Double2 / (param1Double4 - param1Double3);
        this.mEllipseCenterY = param1Double1 / (param1Double4 - param1Double3);
        return;
      } 
      this.mLut = new double[101];
      bool1 = this.mVertical;
      param1Int = bool;
      if (bool1)
        param1Int = -1; 
      this.mEllipseA = param1Int * param1Double2;
      if (bool1) {
        param1Int = 1;
      } else {
        param1Int = -1;
      } 
      this.mEllipseB = param1Int * param1Double1;
      if (bool1) {
        param1Double1 = param1Double5;
      } else {
        param1Double1 = param1Double3;
      } 
      this.mEllipseCenterX = param1Double1;
      if (bool1) {
        param1Double1 = param1Double4;
      } else {
        param1Double1 = param1Double6;
      } 
      this.mEllipseCenterY = param1Double1;
      buildTable(param1Double3, param1Double4, param1Double5, param1Double6);
      this.mArcVelocity = this.mArcDistance * this.mOneOverDeltaTime;
    }
    
    private void buildTable(double param1Double1, double param1Double2, double param1Double3, double param1Double4) {
      param1Double3 -= param1Double1;
      param1Double2 -= param1Double4;
      double d = 0.0D;
      param1Double4 = 0.0D;
      param1Double1 = 0.0D;
      byte b = 0;
      while (true) {
        double[] arrayOfDouble = ourPercent;
        if (b < arrayOfDouble.length) {
          double d2 = Math.toRadians(b * 90.0D / (arrayOfDouble.length - 1));
          double d1 = Math.sin(d2);
          double d3 = Math.cos(d2);
          d2 = param1Double3 * d1;
          d1 = param1Double2 * d3;
          if (b > 0) {
            param1Double1 = Math.hypot(d2 - d, d1 - param1Double4) + param1Double1;
            ourPercent[b] = param1Double1;
          } 
          d = d2;
          param1Double4 = d1;
          b++;
          continue;
        } 
        this.mArcDistance = param1Double1;
        b = 0;
        while (true) {
          arrayOfDouble = ourPercent;
          if (b < arrayOfDouble.length) {
            arrayOfDouble[b] = arrayOfDouble[b] / param1Double1;
            b++;
            continue;
          } 
          b = 0;
          while (true) {
            arrayOfDouble = this.mLut;
            if (b < arrayOfDouble.length) {
              param1Double1 = b / (arrayOfDouble.length - 1);
              int i = Arrays.binarySearch(ourPercent, param1Double1);
              if (i >= 0) {
                this.mLut[b] = i / (ourPercent.length - 1);
              } else if (i == -1) {
                this.mLut[b] = 0.0D;
              } else {
                int j = -i - 2;
                i = -i;
                param1Double2 = j;
                arrayOfDouble = ourPercent;
                param1Double1 = (param1Double2 + (param1Double1 - arrayOfDouble[j]) / (arrayOfDouble[i - 1] - arrayOfDouble[j])) / (arrayOfDouble.length - 1);
                this.mLut[b] = param1Double1;
              } 
              b++;
              continue;
            } 
            break;
          } 
          return;
        } 
        break;
      } 
    }
    
    double getDX() {
      double d1 = this.mEllipseA * this.mTmpCosAngle;
      double d3 = -this.mEllipseB;
      double d2 = this.mTmpSinAngle;
      d2 = this.mArcVelocity / Math.hypot(d1, d3 * d2);
      if (this.mVertical) {
        d1 = -d1 * d2;
      } else {
        d1 *= d2;
      } 
      return d1;
    }
    
    double getDY() {
      double d2 = this.mEllipseA;
      double d3 = this.mTmpCosAngle;
      double d1 = -this.mEllipseB * this.mTmpSinAngle;
      d2 = this.mArcVelocity / Math.hypot(d2 * d3, d1);
      if (this.mVertical) {
        d1 = -d1 * d2;
      } else {
        d1 *= d2;
      } 
      return d1;
    }
    
    public double getLinearDX(double param1Double) {
      return this.mEllipseCenterX;
    }
    
    public double getLinearDY(double param1Double) {
      return this.mEllipseCenterY;
    }
    
    public double getLinearX(double param1Double) {
      double d1 = this.mTime1;
      double d2 = this.mOneOverDeltaTime;
      double d3 = this.mX1;
      return d3 + (this.mX2 - d3) * (param1Double - d1) * d2;
    }
    
    public double getLinearY(double param1Double) {
      double d3 = this.mTime1;
      double d2 = this.mOneOverDeltaTime;
      double d1 = this.mY1;
      return d1 + (this.mY2 - d1) * (param1Double - d3) * d2;
    }
    
    double getX() {
      return this.mEllipseCenterX + this.mEllipseA * this.mTmpSinAngle;
    }
    
    double getY() {
      return this.mEllipseCenterY + this.mEllipseB * this.mTmpCosAngle;
    }
    
    double lookup(double param1Double) {
      if (param1Double <= 0.0D)
        return 0.0D; 
      if (param1Double >= 1.0D)
        return 1.0D; 
      double[] arrayOfDouble = this.mLut;
      param1Double = (arrayOfDouble.length - 1) * param1Double;
      int i = (int)param1Double;
      double d = (int)param1Double;
      return arrayOfDouble[i] + (arrayOfDouble[i + 1] - arrayOfDouble[i]) * (param1Double - d);
    }
    
    void setPoint(double param1Double) {
      if (this.mVertical) {
        param1Double = this.mTime2 - param1Double;
      } else {
        param1Double -= this.mTime1;
      } 
      param1Double = lookup(param1Double * this.mOneOverDeltaTime) * 1.5707963267948966D;
      this.mTmpSinAngle = Math.sin(param1Double);
      this.mTmpCosAngle = Math.cos(param1Double);
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\motio\\utils\ArcCurveFit.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */