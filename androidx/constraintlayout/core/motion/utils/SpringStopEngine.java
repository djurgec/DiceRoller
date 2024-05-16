package androidx.constraintlayout.core.motion.utils;

public class SpringStopEngine implements StopEngine {
  private static final double UNSET = 1.7976931348623157E308D;
  
  private int mBoundaryMode = 0;
  
  double mDamping = 0.5D;
  
  private boolean mInitialized = false;
  
  private float mLastTime;
  
  private double mLastVelocity;
  
  private float mMass;
  
  private float mPos;
  
  private double mStiffness;
  
  private float mStopThreshold;
  
  private double mTargetPos;
  
  private float mV;
  
  private void compute(double paramDouble) {
    double d1 = this.mStiffness;
    double d2 = this.mDamping;
    int i = (int)(9.0D / Math.sqrt(this.mStiffness / this.mMass) * paramDouble * 4.0D + 1.0D);
    double d3 = paramDouble / i;
    byte b = 0;
    paramDouble = d2;
    while (b < i) {
      float f1 = this.mPos;
      double d4 = f1;
      d2 = this.mTargetPos;
      double d5 = -d1;
      float f2 = this.mV;
      double d6 = f2;
      float f3 = this.mMass;
      d4 = (d5 * (d4 - d2) - d6 * paramDouble) / f3;
      d4 = f2 + d4 * d3 / 2.0D;
      d5 = (-(f1 + d3 * d4 / 2.0D - d2) * d1 - d4 * paramDouble) / f3 * d3;
      d2 = f2;
      d4 = d5 / 2.0D;
      f2 = (float)(f2 + d5);
      this.mV = f2;
      f1 = (float)(f1 + (d2 + d4) * d3);
      this.mPos = f1;
      int j = this.mBoundaryMode;
      if (j > 0) {
        if (f1 < 0.0F && (j & 0x1) == 1) {
          this.mPos = -f1;
          this.mV = -f2;
        } 
        f1 = this.mPos;
        if (f1 > 1.0F && (j & 0x2) == 2) {
          this.mPos = 2.0F - f1;
          this.mV = -this.mV;
        } 
      } 
      b++;
    } 
  }
  
  public String debug(String paramString, float paramFloat) {
    return null;
  }
  
  public float getAcceleration() {
    double d1 = this.mStiffness;
    double d2 = this.mDamping;
    double d3 = this.mPos;
    double d4 = this.mTargetPos;
    return (float)(-d1 * (d3 - d4) - this.mV * d2) / this.mMass;
  }
  
  public float getInterpolation(float paramFloat) {
    compute((paramFloat - this.mLastTime));
    this.mLastTime = paramFloat;
    return this.mPos;
  }
  
  public float getVelocity() {
    return 0.0F;
  }
  
  public float getVelocity(float paramFloat) {
    return this.mV;
  }
  
  public boolean isStopped() {
    boolean bool;
    double d1 = this.mPos - this.mTargetPos;
    double d3 = this.mStiffness;
    double d2 = this.mV;
    if (Math.sqrt((d2 * d2 * this.mMass + d3 * d1 * d1) / d3) <= this.mStopThreshold) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  void log(String paramString) {
    StackTraceElement stackTraceElement = (new Throwable()).getStackTrace()[1];
    String str = ".(" + stackTraceElement.getFileName() + ":" + stackTraceElement.getLineNumber() + ") " + stackTraceElement.getMethodName() + "() ";
    System.out.println(str + paramString);
  }
  
  public void springConfig(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, int paramInt) {
    this.mTargetPos = paramFloat2;
    this.mDamping = paramFloat6;
    this.mInitialized = false;
    this.mPos = paramFloat1;
    this.mLastVelocity = paramFloat3;
    this.mStiffness = paramFloat5;
    this.mMass = paramFloat4;
    this.mStopThreshold = paramFloat7;
    this.mBoundaryMode = paramInt;
    this.mLastTime = 0.0F;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\motio\\utils\SpringStopEngine.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */