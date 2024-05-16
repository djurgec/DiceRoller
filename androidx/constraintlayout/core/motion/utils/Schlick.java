package androidx.constraintlayout.core.motion.utils;

public class Schlick extends Easing {
  private static final boolean DEBUG = false;
  
  double eps;
  
  double mS;
  
  double mT;
  
  Schlick(String paramString) {
    this.str = paramString;
    int i = paramString.indexOf('(');
    int j = paramString.indexOf(',', i);
    this.mS = Double.parseDouble(paramString.substring(i + 1, j).trim());
    this.mT = Double.parseDouble(paramString.substring(j + 1, paramString.indexOf(',', j + 1)).trim());
  }
  
  private double dfunc(double paramDouble) {
    double d1 = this.mT;
    if (paramDouble < d1) {
      double d = this.mS;
      return d * d1 * d1 / ((d1 - paramDouble) * d + paramDouble) * (d * (d1 - paramDouble) + paramDouble);
    } 
    double d2 = this.mS;
    return (d1 - 1.0D) * d2 * (d1 - 1.0D) / (-d2 * (d1 - paramDouble) - paramDouble + 1.0D) * (-d2 * (d1 - paramDouble) - paramDouble + 1.0D);
  }
  
  private double func(double paramDouble) {
    double d = this.mT;
    return (paramDouble < d) ? (d * paramDouble / (this.mS * (d - paramDouble) + paramDouble)) : ((1.0D - d) * (paramDouble - 1.0D) / (1.0D - paramDouble - this.mS * (d - paramDouble)));
  }
  
  public double get(double paramDouble) {
    return func(paramDouble);
  }
  
  public double getDiff(double paramDouble) {
    return dfunc(paramDouble);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\motio\\utils\Schlick.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */