package androidx.constraintlayout.core.motion.utils;

import java.util.Arrays;

public class Easing {
  private static final String ACCELERATE = "cubic(0.4, 0.05, 0.8, 0.7)";
  
  private static final String ACCELERATE_NAME = "accelerate";
  
  private static final String ANTICIPATE = "cubic(0.36, 0, 0.66, -0.56)";
  
  private static final String ANTICIPATE_NAME = "anticipate";
  
  private static final String DECELERATE = "cubic(0.0, 0.0, 0.2, 0.95)";
  
  private static final String DECELERATE_NAME = "decelerate";
  
  private static final String LINEAR = "cubic(1, 1, 0, 0)";
  
  private static final String LINEAR_NAME = "linear";
  
  public static String[] NAMED_EASING;
  
  private static final String OVERSHOOT = "cubic(0.34, 1.56, 0.64, 1)";
  
  private static final String OVERSHOOT_NAME = "overshoot";
  
  private static final String STANDARD = "cubic(0.4, 0.0, 0.2, 1)";
  
  private static final String STANDARD_NAME = "standard";
  
  static Easing sDefault = new Easing();
  
  String str = "identity";
  
  static {
    NAMED_EASING = new String[] { "standard", "accelerate", "decelerate", "linear" };
  }
  
  public static Easing getInterpolator(String paramString) {
    if (paramString == null)
      return null; 
    if (paramString.startsWith("cubic"))
      return new CubicEasing(paramString); 
    if (paramString.startsWith("spline"))
      return new StepCurve(paramString); 
    if (paramString.startsWith("Schlick"))
      return new Schlick(paramString); 
    byte b = -1;
    switch (paramString.hashCode()) {
      case 1312628413:
        if (paramString.equals("standard"))
          b = 0; 
        break;
      case -749065269:
        if (paramString.equals("overshoot"))
          b = 5; 
        break;
      case -1102672091:
        if (paramString.equals("linear"))
          b = 3; 
        break;
      case -1197605014:
        if (paramString.equals("anticipate"))
          b = 4; 
        break;
      case -1263948740:
        if (paramString.equals("decelerate"))
          b = 2; 
        break;
      case -1354466595:
        if (paramString.equals("accelerate"))
          b = 1; 
        break;
    } 
    switch (b) {
      default:
        System.err.println("transitionEasing syntax error syntax:transitionEasing=\"cubic(1.0,0.5,0.0,0.6)\" or " + Arrays.toString((Object[])NAMED_EASING));
        return sDefault;
      case 5:
        return new CubicEasing("cubic(0.34, 1.56, 0.64, 1)");
      case 4:
        return new CubicEasing("cubic(0.36, 0, 0.66, -0.56)");
      case 3:
        return new CubicEasing("cubic(1, 1, 0, 0)");
      case 2:
        return new CubicEasing("cubic(0.0, 0.0, 0.2, 0.95)");
      case 1:
        return new CubicEasing("cubic(0.4, 0.05, 0.8, 0.7)");
      case 0:
        break;
    } 
    return new CubicEasing("cubic(0.4, 0.0, 0.2, 1)");
  }
  
  public double get(double paramDouble) {
    return paramDouble;
  }
  
  public double getDiff(double paramDouble) {
    return 1.0D;
  }
  
  public String toString() {
    return this.str;
  }
  
  static class CubicEasing extends Easing {
    private static double d_error = 1.0E-4D;
    
    private static double error = 0.01D;
    
    double x1;
    
    double x2;
    
    double y1;
    
    double y2;
    
    static {
    
    }
    
    public CubicEasing(double param1Double1, double param1Double2, double param1Double3, double param1Double4) {
      setup(param1Double1, param1Double2, param1Double3, param1Double4);
    }
    
    CubicEasing(String param1String) {
      this.str = param1String;
      int i = param1String.indexOf('(');
      int j = param1String.indexOf(',', i);
      this.x1 = Double.parseDouble(param1String.substring(i + 1, j).trim());
      i = param1String.indexOf(',', j + 1);
      this.y1 = Double.parseDouble(param1String.substring(j + 1, i).trim());
      j = param1String.indexOf(',', i + 1);
      this.x2 = Double.parseDouble(param1String.substring(i + 1, j).trim());
      this.y2 = Double.parseDouble(param1String.substring(j + 1, param1String.indexOf(')', j + 1)).trim());
    }
    
    private double getDiffX(double param1Double) {
      double d1 = 1.0D - param1Double;
      double d2 = this.x1;
      double d3 = this.x2;
      return d1 * 3.0D * d1 * d2 + 6.0D * d1 * param1Double * (d3 - d2) + 3.0D * param1Double * param1Double * (1.0D - d3);
    }
    
    private double getDiffY(double param1Double) {
      double d3 = 1.0D - param1Double;
      double d2 = this.y1;
      double d1 = this.y2;
      return d3 * 3.0D * d3 * d2 + 6.0D * d3 * param1Double * (d1 - d2) + 3.0D * param1Double * param1Double * (1.0D - d1);
    }
    
    private double getX(double param1Double) {
      double d = 1.0D - param1Double;
      return this.x1 * d * 3.0D * d * param1Double + this.x2 * 3.0D * d * param1Double * param1Double + param1Double * param1Double * param1Double;
    }
    
    private double getY(double param1Double) {
      double d = 1.0D - param1Double;
      return this.y1 * d * 3.0D * d * param1Double + this.y2 * 3.0D * d * param1Double * param1Double + param1Double * param1Double * param1Double;
    }
    
    public double get(double param1Double) {
      if (param1Double <= 0.0D)
        return 0.0D; 
      if (param1Double >= 1.0D)
        return 1.0D; 
      double d2 = 0.5D;
      double d1;
      for (d1 = 0.5D; d1 > error; d1 = d6) {
        double d7 = getX(d2);
        double d6 = d1 * 0.5D;
        if (d7 < param1Double) {
          d1 = d2 + d6;
        } else {
          d1 = d2 - d6;
        } 
        d2 = d1;
      } 
      double d4 = getX(d2 - d1);
      double d5 = getX(d2 + d1);
      double d3 = getY(d2 - d1);
      return (getY(d2 + d1) - d3) * (param1Double - d4) / (d5 - d4) + d3;
    }
    
    public double getDiff(double param1Double) {
      double d1 = 0.5D;
      double d2 = 0.5D;
      while (d2 > d_error) {
        double d = getX(d1);
        d2 *= 0.5D;
        if (d < param1Double) {
          d1 += d2;
          continue;
        } 
        d1 -= d2;
      } 
      param1Double = getX(d1 - d2);
      double d4 = getX(d1 + d2);
      double d3 = getY(d1 - d2);
      return (getY(d1 + d2) - d3) / (d4 - param1Double);
    }
    
    void setup(double param1Double1, double param1Double2, double param1Double3, double param1Double4) {
      this.x1 = param1Double1;
      this.y1 = param1Double2;
      this.x2 = param1Double3;
      this.y2 = param1Double4;
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\motio\\utils\Easing.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */