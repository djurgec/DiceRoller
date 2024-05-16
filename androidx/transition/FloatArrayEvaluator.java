package androidx.transition;

import android.animation.TypeEvaluator;

class FloatArrayEvaluator implements TypeEvaluator<float[]> {
  private float[] mArray;
  
  FloatArrayEvaluator(float[] paramArrayOffloat) {
    this.mArray = paramArrayOffloat;
  }
  
  public float[] evaluate(float paramFloat, float[] paramArrayOffloat1, float[] paramArrayOffloat2) {
    float[] arrayOfFloat2 = this.mArray;
    float[] arrayOfFloat1 = arrayOfFloat2;
    if (arrayOfFloat2 == null)
      arrayOfFloat1 = new float[paramArrayOffloat1.length]; 
    for (byte b = 0; b < arrayOfFloat1.length; b++) {
      float f = paramArrayOffloat1[b];
      arrayOfFloat1[b] = (paramArrayOffloat2[b] - f) * paramFloat + f;
    } 
    return arrayOfFloat1;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\transition\FloatArrayEvaluator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */