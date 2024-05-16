package androidx.constraintlayout.core.motion.utils;

public class VelocityMatrix {
  private static String TAG = "VelocityMatrix";
  
  float mDRotate;
  
  float mDScaleX;
  
  float mDScaleY;
  
  float mDTranslateX;
  
  float mDTranslateY;
  
  float mRotate;
  
  public void applyTransform(float paramFloat1, float paramFloat2, int paramInt1, int paramInt2, float[] paramArrayOffloat) {
    float f2 = paramArrayOffloat[0];
    float f1 = paramArrayOffloat[1];
    paramFloat1 = (paramFloat1 - 0.5F) * 2.0F;
    paramFloat2 = (paramFloat2 - 0.5F) * 2.0F;
    float f4 = this.mDTranslateX;
    float f5 = this.mDTranslateY;
    float f7 = this.mDScaleX;
    float f8 = this.mDScaleY;
    float f9 = (float)Math.toRadians(this.mRotate);
    float f3 = (float)Math.toRadians(this.mDRotate);
    float f6 = (float)((-paramInt1 * paramFloat1) * Math.sin(f9) - (paramInt2 * paramFloat2) * Math.cos(f9));
    f9 = (float)((paramInt1 * paramFloat1) * Math.cos(f9) - (paramInt2 * paramFloat2) * Math.sin(f9));
    paramArrayOffloat[0] = f2 + f4 + f7 * paramFloat1 + f6 * f3;
    paramArrayOffloat[1] = f1 + f5 + f8 * paramFloat2 + f9 * f3;
  }
  
  public void clear() {
    this.mDRotate = 0.0F;
    this.mDTranslateY = 0.0F;
    this.mDTranslateX = 0.0F;
    this.mDScaleY = 0.0F;
    this.mDScaleX = 0.0F;
  }
  
  public void setRotationVelocity(KeyCycleOscillator paramKeyCycleOscillator, float paramFloat) {
    if (paramKeyCycleOscillator != null)
      this.mDRotate = paramKeyCycleOscillator.getSlope(paramFloat); 
  }
  
  public void setRotationVelocity(SplineSet paramSplineSet, float paramFloat) {
    if (paramSplineSet != null) {
      this.mDRotate = paramSplineSet.getSlope(paramFloat);
      this.mRotate = paramSplineSet.get(paramFloat);
    } 
  }
  
  public void setScaleVelocity(KeyCycleOscillator paramKeyCycleOscillator1, KeyCycleOscillator paramKeyCycleOscillator2, float paramFloat) {
    if (paramKeyCycleOscillator1 != null)
      this.mDScaleX = paramKeyCycleOscillator1.getSlope(paramFloat); 
    if (paramKeyCycleOscillator2 != null)
      this.mDScaleY = paramKeyCycleOscillator2.getSlope(paramFloat); 
  }
  
  public void setScaleVelocity(SplineSet paramSplineSet1, SplineSet paramSplineSet2, float paramFloat) {
    if (paramSplineSet1 != null)
      this.mDScaleX = paramSplineSet1.getSlope(paramFloat); 
    if (paramSplineSet2 != null)
      this.mDScaleY = paramSplineSet2.getSlope(paramFloat); 
  }
  
  public void setTranslationVelocity(KeyCycleOscillator paramKeyCycleOscillator1, KeyCycleOscillator paramKeyCycleOscillator2, float paramFloat) {
    if (paramKeyCycleOscillator1 != null)
      this.mDTranslateX = paramKeyCycleOscillator1.getSlope(paramFloat); 
    if (paramKeyCycleOscillator2 != null)
      this.mDTranslateY = paramKeyCycleOscillator2.getSlope(paramFloat); 
  }
  
  public void setTranslationVelocity(SplineSet paramSplineSet1, SplineSet paramSplineSet2, float paramFloat) {
    if (paramSplineSet1 != null)
      this.mDTranslateX = paramSplineSet1.getSlope(paramFloat); 
    if (paramSplineSet2 != null)
      this.mDTranslateY = paramSplineSet2.getSlope(paramFloat); 
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\motio\\utils\VelocityMatrix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */