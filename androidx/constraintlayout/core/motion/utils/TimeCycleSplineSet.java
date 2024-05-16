package androidx.constraintlayout.core.motion.utils;

import androidx.constraintlayout.core.motion.CustomAttribute;
import androidx.constraintlayout.core.motion.CustomVariable;
import androidx.constraintlayout.core.motion.MotionWidget;
import java.text.DecimalFormat;

public abstract class TimeCycleSplineSet {
  protected static final int CURVE_OFFSET = 2;
  
  protected static final int CURVE_PERIOD = 1;
  
  protected static final int CURVE_VALUE = 0;
  
  private static final String TAG = "SplineSet";
  
  protected static float VAL_2PI = 6.2831855F;
  
  protected int count;
  
  protected float last_cycle = Float.NaN;
  
  protected long last_time;
  
  protected float[] mCache = new float[3];
  
  protected boolean mContinue = false;
  
  protected CurveFit mCurveFit;
  
  protected int[] mTimePoints = new int[10];
  
  protected String mType;
  
  protected float[][] mValues = new float[10][3];
  
  protected int mWaveShape = 0;
  
  protected float calcWave(float paramFloat) {
    switch (this.mWaveShape) {
      default:
        return (float)Math.sin((VAL_2PI * paramFloat));
      case 6:
        paramFloat = 1.0F - Math.abs(paramFloat * 4.0F % 4.0F - 2.0F);
        return 1.0F - paramFloat * paramFloat;
      case 5:
        return (float)Math.cos((VAL_2PI * paramFloat));
      case 4:
        return 1.0F - (paramFloat * 2.0F + 1.0F) % 2.0F;
      case 3:
        return (paramFloat * 2.0F + 1.0F) % 2.0F - 1.0F;
      case 2:
        return 1.0F - Math.abs(paramFloat);
      case 1:
        break;
    } 
    return Math.signum(VAL_2PI * paramFloat);
  }
  
  public CurveFit getCurveFit() {
    return this.mCurveFit;
  }
  
  public void setPoint(int paramInt1, float paramFloat1, float paramFloat2, int paramInt2, float paramFloat3) {
    int[] arrayOfInt = this.mTimePoints;
    int i = this.count;
    arrayOfInt[i] = paramInt1;
    float[][] arrayOfFloat = this.mValues;
    arrayOfFloat[i][0] = paramFloat1;
    arrayOfFloat[i][1] = paramFloat2;
    arrayOfFloat[i][2] = paramFloat3;
    this.mWaveShape = Math.max(this.mWaveShape, paramInt2);
    this.count++;
  }
  
  protected void setStartTime(long paramLong) {
    this.last_time = paramLong;
  }
  
  public void setType(String paramString) {
    this.mType = paramString;
  }
  
  public void setup(int paramInt) {
    int i = this.count;
    if (i == 0) {
      System.err.println("Error no points added to " + this.mType);
      return;
    } 
    Sort.doubleQuickSort(this.mTimePoints, this.mValues, 0, i - 1);
    i = 0;
    int j = 1;
    while (true) {
      int[] arrayOfInt = this.mTimePoints;
      if (j < arrayOfInt.length) {
        int k = i;
        if (arrayOfInt[j] != arrayOfInt[j - 1])
          k = i + 1; 
        j++;
        i = k;
        continue;
      } 
      j = i;
      if (i == 0)
        j = 1; 
      double[] arrayOfDouble1 = new double[j];
      double[][] arrayOfDouble = new double[j][3];
      j = 0;
      for (i = 0; i < this.count; i++) {
        if (i > 0) {
          int[] arrayOfInt1 = this.mTimePoints;
          if (arrayOfInt1[i] == arrayOfInt1[i - 1])
            continue; 
        } 
        arrayOfDouble1[j] = this.mTimePoints[i] * 0.01D;
        double[] arrayOfDouble2 = arrayOfDouble[j];
        float[][] arrayOfFloat = this.mValues;
        arrayOfDouble2[0] = arrayOfFloat[i][0];
        arrayOfDouble[j][1] = arrayOfFloat[i][1];
        arrayOfDouble[j][2] = arrayOfFloat[i][2];
        j++;
        continue;
      } 
      this.mCurveFit = CurveFit.get(paramInt, arrayOfDouble1, arrayOfDouble);
      return;
    } 
  }
  
  public String toString() {
    String str = this.mType;
    DecimalFormat decimalFormat = new DecimalFormat("##.##");
    for (byte b = 0; b < this.count; b++)
      str = str + "[" + this.mTimePoints[b] + " , " + decimalFormat.format(this.mValues[b]) + "] "; 
    return str;
  }
  
  public static class CustomSet extends TimeCycleSplineSet {
    String mAttributeName;
    
    float[] mCache;
    
    KeyFrameArray.CustomArray mConstraintAttributeList;
    
    float[] mTempValues;
    
    KeyFrameArray.FloatArray mWaveProperties = new KeyFrameArray.FloatArray();
    
    public CustomSet(String param1String, KeyFrameArray.CustomArray param1CustomArray) {
      this.mAttributeName = param1String.split(",")[1];
      this.mConstraintAttributeList = param1CustomArray;
    }
    
    public void setPoint(int param1Int1, float param1Float1, float param1Float2, int param1Int2, float param1Float3) {
      throw new RuntimeException("don't call for custom attribute call setPoint(pos, ConstraintAttribute,...)");
    }
    
    public void setPoint(int param1Int1, CustomAttribute param1CustomAttribute, float param1Float1, int param1Int2, float param1Float2) {
      this.mConstraintAttributeList.append(param1Int1, param1CustomAttribute);
      this.mWaveProperties.append(param1Int1, new float[] { param1Float1, param1Float2 });
      this.mWaveShape = Math.max(this.mWaveShape, param1Int2);
    }
    
    public boolean setProperty(MotionWidget param1MotionWidget, float param1Float, long param1Long, KeyCache param1KeyCache) {
      this.mCurveFit.getPos(param1Float, this.mTempValues);
      float[] arrayOfFloat = this.mTempValues;
      param1Float = arrayOfFloat[arrayOfFloat.length - 2];
      float f2 = arrayOfFloat[arrayOfFloat.length - 1];
      long l = this.last_time;
      if (Float.isNaN(this.last_cycle)) {
        this.last_cycle = param1KeyCache.getFloatValue(param1MotionWidget, this.mAttributeName, 0);
        if (Float.isNaN(this.last_cycle))
          this.last_cycle = 0.0F; 
      } 
      this.last_cycle = (float)((this.last_cycle + (param1Long - l) * 1.0E-9D * param1Float) % 1.0D);
      this.last_time = param1Long;
      float f1 = calcWave(this.last_cycle);
      this.mContinue = false;
      for (byte b = 0; b < this.mCache.length; b++) {
        boolean bool1;
        boolean bool2 = this.mContinue;
        if (this.mTempValues[b] != 0.0D) {
          bool1 = true;
        } else {
          bool1 = false;
        } 
        this.mContinue = bool2 | bool1;
        this.mCache[b] = this.mTempValues[b] * f1 + f2;
      } 
      this.mConstraintAttributeList.valueAt(0).setInterpolatedValue(param1MotionWidget, this.mCache);
      if (param1Float != 0.0F)
        this.mContinue = true; 
      return this.mContinue;
    }
    
    public void setup(int param1Int) {
      int i = this.mConstraintAttributeList.size();
      int j = this.mConstraintAttributeList.valueAt(0).numberOfInterpolatedValues();
      double[] arrayOfDouble = new double[i];
      this.mTempValues = new float[j + 2];
      this.mCache = new float[j];
      double[][] arrayOfDouble1 = new double[i][j + 2];
      byte b = 0;
      while (b < i) {
        int k = this.mConstraintAttributeList.keyAt(b);
        CustomAttribute customAttribute = this.mConstraintAttributeList.valueAt(b);
        float[] arrayOfFloat = this.mWaveProperties.valueAt(b);
        arrayOfDouble[b] = k * 0.01D;
        customAttribute.getValuesToInterpolate(this.mTempValues);
        k = 0;
        while (true) {
          float[] arrayOfFloat1 = this.mTempValues;
          if (k < arrayOfFloat1.length) {
            arrayOfDouble1[b][k] = arrayOfFloat1[k];
            k++;
            continue;
          } 
          arrayOfDouble1[b][j] = arrayOfFloat[0];
          arrayOfDouble1[b][j + 1] = arrayOfFloat[1];
          b++;
        } 
      } 
      this.mCurveFit = CurveFit.get(param1Int, arrayOfDouble, arrayOfDouble1);
    }
  }
  
  public static class CustomVarSet extends TimeCycleSplineSet {
    String mAttributeName;
    
    float[] mCache;
    
    KeyFrameArray.CustomVar mConstraintAttributeList;
    
    float[] mTempValues;
    
    KeyFrameArray.FloatArray mWaveProperties = new KeyFrameArray.FloatArray();
    
    public CustomVarSet(String param1String, KeyFrameArray.CustomVar param1CustomVar) {
      this.mAttributeName = param1String.split(",")[1];
      this.mConstraintAttributeList = param1CustomVar;
    }
    
    public void setPoint(int param1Int1, float param1Float1, float param1Float2, int param1Int2, float param1Float3) {
      throw new RuntimeException("don't call for custom attribute call setPoint(pos, ConstraintAttribute,...)");
    }
    
    public void setPoint(int param1Int1, CustomVariable param1CustomVariable, float param1Float1, int param1Int2, float param1Float2) {
      this.mConstraintAttributeList.append(param1Int1, param1CustomVariable);
      this.mWaveProperties.append(param1Int1, new float[] { param1Float1, param1Float2 });
      this.mWaveShape = Math.max(this.mWaveShape, param1Int2);
    }
    
    public boolean setProperty(MotionWidget param1MotionWidget, float param1Float, long param1Long, KeyCache param1KeyCache) {
      this.mCurveFit.getPos(param1Float, this.mTempValues);
      float[] arrayOfFloat = this.mTempValues;
      float f1 = arrayOfFloat[arrayOfFloat.length - 2];
      param1Float = arrayOfFloat[arrayOfFloat.length - 1];
      long l = this.last_time;
      if (Float.isNaN(this.last_cycle)) {
        this.last_cycle = param1KeyCache.getFloatValue(param1MotionWidget, this.mAttributeName, 0);
        if (Float.isNaN(this.last_cycle))
          this.last_cycle = 0.0F; 
      } 
      this.last_cycle = (float)((this.last_cycle + (param1Long - l) * 1.0E-9D * f1) % 1.0D);
      this.last_time = param1Long;
      float f2 = calcWave(this.last_cycle);
      this.mContinue = false;
      for (byte b = 0; b < this.mCache.length; b++) {
        boolean bool1;
        boolean bool2 = this.mContinue;
        if (this.mTempValues[b] != 0.0D) {
          bool1 = true;
        } else {
          bool1 = false;
        } 
        this.mContinue = bool2 | bool1;
        this.mCache[b] = this.mTempValues[b] * f2 + param1Float;
      } 
      this.mConstraintAttributeList.valueAt(0).setInterpolatedValue(param1MotionWidget, this.mCache);
      if (f1 != 0.0F)
        this.mContinue = true; 
      return this.mContinue;
    }
    
    public void setup(int param1Int) {
      int j = this.mConstraintAttributeList.size();
      int i = this.mConstraintAttributeList.valueAt(0).numberOfInterpolatedValues();
      double[] arrayOfDouble1 = new double[j];
      this.mTempValues = new float[i + 2];
      this.mCache = new float[i];
      double[][] arrayOfDouble = new double[j][i + 2];
      byte b = 0;
      while (b < j) {
        int k = this.mConstraintAttributeList.keyAt(b);
        CustomVariable customVariable = this.mConstraintAttributeList.valueAt(b);
        float[] arrayOfFloat = this.mWaveProperties.valueAt(b);
        arrayOfDouble1[b] = k * 0.01D;
        customVariable.getValuesToInterpolate(this.mTempValues);
        k = 0;
        while (true) {
          float[] arrayOfFloat1 = this.mTempValues;
          if (k < arrayOfFloat1.length) {
            arrayOfDouble[b][k] = arrayOfFloat1[k];
            k++;
            continue;
          } 
          arrayOfDouble[b][i] = arrayOfFloat[0];
          arrayOfDouble[b][i + 1] = arrayOfFloat[1];
          b++;
        } 
      } 
      this.mCurveFit = CurveFit.get(param1Int, arrayOfDouble1, arrayOfDouble);
    }
  }
  
  protected static class Sort {
    static void doubleQuickSort(int[] param1ArrayOfint, float[][] param1ArrayOffloat, int param1Int1, int param1Int2) {
      int[] arrayOfInt = new int[param1ArrayOfint.length + 10];
      int i = 0 + 1;
      arrayOfInt[0] = param1Int2;
      param1Int2 = i + 1;
      arrayOfInt[i] = param1Int1;
      param1Int1 = param1Int2;
      while (param1Int1 > 0) {
        int j = arrayOfInt[--param1Int1];
        param1Int2 = param1Int1 - 1;
        int k = arrayOfInt[param1Int2];
        param1Int1 = param1Int2;
        if (j < k) {
          i = partition(param1ArrayOfint, param1ArrayOffloat, j, k);
          param1Int1 = param1Int2 + 1;
          arrayOfInt[param1Int2] = i - 1;
          param1Int2 = param1Int1 + 1;
          arrayOfInt[param1Int1] = j;
          j = param1Int2 + 1;
          arrayOfInt[param1Int2] = k;
          param1Int1 = j + 1;
          arrayOfInt[j] = i + 1;
        } 
      } 
    }
    
    private static int partition(int[] param1ArrayOfint, float[][] param1ArrayOffloat, int param1Int1, int param1Int2) {
      int k = param1ArrayOfint[param1Int2];
      int i = param1Int1;
      int j;
      for (j = i; param1Int1 < param1Int2; j = i) {
        i = j;
        if (param1ArrayOfint[param1Int1] <= k) {
          swap(param1ArrayOfint, param1ArrayOffloat, j, param1Int1);
          i = j + 1;
        } 
        param1Int1++;
      } 
      swap(param1ArrayOfint, param1ArrayOffloat, j, param1Int2);
      return j;
    }
    
    private static void swap(int[] param1ArrayOfint, float[][] param1ArrayOffloat, int param1Int1, int param1Int2) {
      int i = param1ArrayOfint[param1Int1];
      param1ArrayOfint[param1Int1] = param1ArrayOfint[param1Int2];
      param1ArrayOfint[param1Int2] = i;
      float[] arrayOfFloat = param1ArrayOffloat[param1Int1];
      param1ArrayOffloat[param1Int1] = param1ArrayOffloat[param1Int2];
      param1ArrayOffloat[param1Int2] = arrayOfFloat;
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\motio\\utils\TimeCycleSplineSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */