package androidx.constraintlayout.core.motion.utils;

public class StopLogicEngine implements StopEngine {
  private static final float EPSILON = 1.0E-5F;
  
  private boolean mBackwards = false;
  
  private boolean mDone = false;
  
  private float mLastPosition;
  
  private int mNumberOfStages;
  
  private float mStage1Duration;
  
  private float mStage1EndPosition;
  
  private float mStage1Velocity;
  
  private float mStage2Duration;
  
  private float mStage2EndPosition;
  
  private float mStage2Velocity;
  
  private float mStage3Duration;
  
  private float mStage3EndPosition;
  
  private float mStage3Velocity;
  
  private float mStartPosition;
  
  private String mType;
  
  private float calcY(float paramFloat) {
    this.mDone = false;
    float f1 = this.mStage1Duration;
    if (paramFloat <= f1) {
      float f = this.mStage1Velocity;
      return f * paramFloat + (this.mStage2Velocity - f) * paramFloat * paramFloat / f1 * 2.0F;
    } 
    int i = this.mNumberOfStages;
    if (i == 1)
      return this.mStage1EndPosition; 
    f1 = paramFloat - f1;
    paramFloat = this.mStage2Duration;
    if (f1 < paramFloat) {
      float f3 = this.mStage1EndPosition;
      float f4 = this.mStage2Velocity;
      return f3 + f4 * f1 + (this.mStage3Velocity - f4) * f1 * f1 / paramFloat * 2.0F;
    } 
    if (i == 2)
      return this.mStage2EndPosition; 
    paramFloat = f1 - paramFloat;
    float f2 = this.mStage3Duration;
    if (paramFloat <= f2) {
      float f = this.mStage2EndPosition;
      f1 = this.mStage3Velocity;
      return f + f1 * paramFloat - f1 * paramFloat * paramFloat / f2 * 2.0F;
    } 
    this.mDone = true;
    return this.mStage3EndPosition;
  }
  
  private void setup(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5) {
    this.mDone = false;
    if (paramFloat1 == 0.0F)
      paramFloat1 = 1.0E-4F; 
    this.mStage1Velocity = paramFloat1;
    float f1 = paramFloat1 / paramFloat3;
    float f2 = f1 * paramFloat1 / 2.0F;
    if (paramFloat1 < 0.0F) {
      paramFloat5 = (float)Math.sqrt((paramFloat3 * (paramFloat2 - -paramFloat1 / paramFloat3 * paramFloat1 / 2.0F)));
      if (paramFloat5 < paramFloat4) {
        this.mType = "backward accelerate, decelerate";
        this.mNumberOfStages = 2;
        this.mStage1Velocity = paramFloat1;
        this.mStage2Velocity = paramFloat5;
        this.mStage3Velocity = 0.0F;
        paramFloat4 = (paramFloat5 - paramFloat1) / paramFloat3;
        this.mStage1Duration = paramFloat4;
        this.mStage2Duration = paramFloat5 / paramFloat3;
        this.mStage1EndPosition = (paramFloat1 + paramFloat5) * paramFloat4 / 2.0F;
        this.mStage2EndPosition = paramFloat2;
        this.mStage3EndPosition = paramFloat2;
        return;
      } 
      this.mType = "backward accelerate cruse decelerate";
      this.mNumberOfStages = 3;
      this.mStage1Velocity = paramFloat1;
      this.mStage2Velocity = paramFloat4;
      this.mStage3Velocity = paramFloat4;
      paramFloat5 = (paramFloat4 - paramFloat1) / paramFloat3;
      this.mStage1Duration = paramFloat5;
      paramFloat3 = paramFloat4 / paramFloat3;
      this.mStage3Duration = paramFloat3;
      paramFloat1 = (paramFloat1 + paramFloat4) * paramFloat5 / 2.0F;
      paramFloat3 = paramFloat4 * paramFloat3 / 2.0F;
      this.mStage2Duration = (paramFloat2 - paramFloat1 - paramFloat3) / paramFloat4;
      this.mStage1EndPosition = paramFloat1;
      this.mStage2EndPosition = paramFloat2 - paramFloat3;
      this.mStage3EndPosition = paramFloat2;
      return;
    } 
    if (f2 >= paramFloat2) {
      this.mType = "hard stop";
      paramFloat3 = 2.0F * paramFloat2 / paramFloat1;
      this.mNumberOfStages = 1;
      this.mStage1Velocity = paramFloat1;
      this.mStage2Velocity = 0.0F;
      this.mStage1EndPosition = paramFloat2;
      this.mStage1Duration = paramFloat3;
      return;
    } 
    float f3 = paramFloat2 - f2;
    f2 = f3 / paramFloat1;
    if (f2 + f1 < paramFloat5) {
      this.mType = "cruse decelerate";
      this.mNumberOfStages = 2;
      this.mStage1Velocity = paramFloat1;
      this.mStage2Velocity = paramFloat1;
      this.mStage3Velocity = 0.0F;
      this.mStage1EndPosition = f3;
      this.mStage2EndPosition = paramFloat2;
      this.mStage1Duration = f2;
      this.mStage2Duration = paramFloat1 / paramFloat3;
      return;
    } 
    paramFloat5 = (float)Math.sqrt((paramFloat3 * paramFloat2 + paramFloat1 * paramFloat1 / 2.0F));
    this.mStage1Duration = (paramFloat5 - paramFloat1) / paramFloat3;
    this.mStage2Duration = paramFloat5 / paramFloat3;
    if (paramFloat5 < paramFloat4) {
      this.mType = "accelerate decelerate";
      this.mNumberOfStages = 2;
      this.mStage1Velocity = paramFloat1;
      this.mStage2Velocity = paramFloat5;
      this.mStage3Velocity = 0.0F;
      paramFloat4 = (paramFloat5 - paramFloat1) / paramFloat3;
      this.mStage1Duration = paramFloat4;
      this.mStage2Duration = paramFloat5 / paramFloat3;
      this.mStage1EndPosition = (paramFloat1 + paramFloat5) * paramFloat4 / 2.0F;
      this.mStage2EndPosition = paramFloat2;
      return;
    } 
    this.mType = "accelerate cruse decelerate";
    this.mNumberOfStages = 3;
    this.mStage1Velocity = paramFloat1;
    this.mStage2Velocity = paramFloat4;
    this.mStage3Velocity = paramFloat4;
    paramFloat5 = (paramFloat4 - paramFloat1) / paramFloat3;
    this.mStage1Duration = paramFloat5;
    paramFloat3 = paramFloat4 / paramFloat3;
    this.mStage3Duration = paramFloat3;
    paramFloat1 = (paramFloat1 + paramFloat4) * paramFloat5 / 2.0F;
    paramFloat3 = paramFloat4 * paramFloat3 / 2.0F;
    this.mStage2Duration = (paramFloat2 - paramFloat1 - paramFloat3) / paramFloat4;
    this.mStage1EndPosition = paramFloat1;
    this.mStage2EndPosition = paramFloat2 - paramFloat3;
    this.mStage3EndPosition = paramFloat2;
  }
  
  public void config(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6) {
    boolean bool = false;
    this.mDone = false;
    this.mStartPosition = paramFloat1;
    if (paramFloat1 > paramFloat2)
      bool = true; 
    this.mBackwards = bool;
    if (bool) {
      setup(-paramFloat3, paramFloat1 - paramFloat2, paramFloat5, paramFloat6, paramFloat4);
    } else {
      setup(paramFloat3, paramFloat2 - paramFloat1, paramFloat5, paramFloat6, paramFloat4);
    } 
  }
  
  public String debug(String paramString, float paramFloat) {
    String str1 = paramString + " ===== " + this.mType + "\n";
    StringBuilder stringBuilder = (new StringBuilder()).append(str1).append(paramString);
    if (this.mBackwards) {
      str1 = "backwards";
    } else {
      str1 = "forward ";
    } 
    str1 = stringBuilder.append(str1).append(" time = ").append(paramFloat).append("  stages ").append(this.mNumberOfStages).append("\n").toString();
    String str2 = str1 + paramString + " dur " + this.mStage1Duration + " vel " + this.mStage1Velocity + " pos " + this.mStage1EndPosition + "\n";
    str1 = str2;
    if (this.mNumberOfStages > 1)
      str1 = str2 + paramString + " dur " + this.mStage2Duration + " vel " + this.mStage2Velocity + " pos " + this.mStage2EndPosition + "\n"; 
    str2 = str1;
    if (this.mNumberOfStages > 2)
      str2 = str1 + paramString + " dur " + this.mStage3Duration + " vel " + this.mStage3Velocity + " pos " + this.mStage3EndPosition + "\n"; 
    float f = this.mStage1Duration;
    if (paramFloat <= f)
      return str2 + paramString + "stage 0\n"; 
    int i = this.mNumberOfStages;
    if (i == 1)
      return str2 + paramString + "end stage 0\n"; 
    f = paramFloat - f;
    paramFloat = this.mStage2Duration;
    return (f < paramFloat) ? (str2 + paramString + " stage 1\n") : ((i == 2) ? (str2 + paramString + "end stage 1\n") : ((f - paramFloat < this.mStage3Duration) ? (str2 + paramString + " stage 2\n") : (str2 + paramString + " end stage 2\n")));
  }
  
  public float getInterpolation(float paramFloat) {
    float f = calcY(paramFloat);
    this.mLastPosition = paramFloat;
    if (this.mBackwards) {
      paramFloat = this.mStartPosition - f;
    } else {
      paramFloat = this.mStartPosition + f;
    } 
    return paramFloat;
  }
  
  public float getVelocity() {
    float f;
    if (this.mBackwards) {
      f = -getVelocity(this.mLastPosition);
    } else {
      f = getVelocity(this.mLastPosition);
    } 
    return f;
  }
  
  public float getVelocity(float paramFloat) {
    float f2 = this.mStage1Duration;
    if (paramFloat <= f2) {
      float f = this.mStage1Velocity;
      return f + (this.mStage2Velocity - f) * paramFloat / f2;
    } 
    int i = this.mNumberOfStages;
    if (i == 1)
      return 0.0F; 
    f2 = paramFloat - f2;
    paramFloat = this.mStage2Duration;
    if (f2 < paramFloat) {
      float f = this.mStage2Velocity;
      return f + (this.mStage3Velocity - f) * f2 / paramFloat;
    } 
    if (i == 2)
      return this.mStage2EndPosition; 
    paramFloat = f2 - paramFloat;
    float f1 = this.mStage3Duration;
    if (paramFloat < f1) {
      f2 = this.mStage3Velocity;
      return f2 - f2 * paramFloat / f1;
    } 
    return this.mStage3EndPosition;
  }
  
  public boolean isStopped() {
    boolean bool;
    if (getVelocity() < 1.0E-5F && Math.abs(this.mStage3EndPosition - this.mLastPosition) < 1.0E-5F) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\motio\\utils\StopLogicEngine.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */