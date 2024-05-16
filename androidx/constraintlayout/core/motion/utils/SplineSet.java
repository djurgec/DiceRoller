package androidx.constraintlayout.core.motion.utils;

import androidx.constraintlayout.core.motion.CustomAttribute;
import androidx.constraintlayout.core.motion.CustomVariable;
import androidx.constraintlayout.core.motion.MotionWidget;
import androidx.constraintlayout.core.state.WidgetFrame;
import java.text.DecimalFormat;
import java.util.Arrays;

public abstract class SplineSet {
  private static final String TAG = "SplineSet";
  
  private int count;
  
  protected CurveFit mCurveFit;
  
  protected int[] mTimePoints = new int[10];
  
  private String mType;
  
  protected float[] mValues = new float[10];
  
  public static SplineSet makeCustomSpline(String paramString, KeyFrameArray.CustomArray paramCustomArray) {
    return new CustomSet(paramString, paramCustomArray);
  }
  
  public static SplineSet makeCustomSplineSet(String paramString, KeyFrameArray.CustomVar paramCustomVar) {
    return new CustomSpline(paramString, paramCustomVar);
  }
  
  public static SplineSet makeSpline(String paramString, long paramLong) {
    return new CoreSpline(paramString, paramLong);
  }
  
  public float get(float paramFloat) {
    return (float)this.mCurveFit.getPos(paramFloat, 0);
  }
  
  public CurveFit getCurveFit() {
    return this.mCurveFit;
  }
  
  public float getSlope(float paramFloat) {
    return (float)this.mCurveFit.getSlope(paramFloat, 0);
  }
  
  public void setPoint(int paramInt, float paramFloat) {
    int[] arrayOfInt = this.mTimePoints;
    if (arrayOfInt.length < this.count + 1) {
      this.mTimePoints = Arrays.copyOf(arrayOfInt, arrayOfInt.length * 2);
      float[] arrayOfFloat = this.mValues;
      this.mValues = Arrays.copyOf(arrayOfFloat, arrayOfFloat.length * 2);
    } 
    arrayOfInt = this.mTimePoints;
    int i = this.count;
    arrayOfInt[i] = paramInt;
    this.mValues[i] = paramFloat;
    this.count = i + 1;
  }
  
  public void setProperty(TypedValues paramTypedValues, float paramFloat) {
    paramTypedValues.setValue(TypedValues.Attributes.getId(this.mType), get(paramFloat));
  }
  
  public void setType(String paramString) {
    this.mType = paramString;
  }
  
  public void setup(int paramInt) {
    int i = this.count;
    if (i == 0)
      return; 
    Sort.doubleQuickSort(this.mTimePoints, this.mValues, 0, i - 1);
    int j = 1;
    i = 1;
    while (i < this.count) {
      int[] arrayOfInt = this.mTimePoints;
      int k = j;
      if (arrayOfInt[i - 1] != arrayOfInt[i])
        k = j + 1; 
      i++;
      j = k;
    } 
    double[] arrayOfDouble1 = new double[j];
    double[][] arrayOfDouble = new double[j][1];
    byte b = 0;
    for (i = 0; i < this.count; i++) {
      if (i > 0) {
        int[] arrayOfInt = this.mTimePoints;
        if (arrayOfInt[i] == arrayOfInt[i - 1])
          continue; 
      } 
      arrayOfDouble1[b] = this.mTimePoints[i] * 0.01D;
      arrayOfDouble[b][0] = this.mValues[i];
      b++;
      continue;
    } 
    this.mCurveFit = CurveFit.get(paramInt, arrayOfDouble1, arrayOfDouble);
  }
  
  public String toString() {
    String str = this.mType;
    DecimalFormat decimalFormat = new DecimalFormat("##.##");
    for (byte b = 0; b < this.count; b++)
      str = str + "[" + this.mTimePoints[b] + " , " + decimalFormat.format(this.mValues[b]) + "] "; 
    return str;
  }
  
  private static class CoreSpline extends SplineSet {
    long start;
    
    String type;
    
    public CoreSpline(String param1String, long param1Long) {
      this.type = param1String;
      this.start = param1Long;
    }
    
    public void setProperty(TypedValues param1TypedValues, float param1Float) {
      param1TypedValues.setValue(param1TypedValues.getId(this.type), get(param1Float));
    }
  }
  
  public static class CustomSet extends SplineSet {
    String mAttributeName;
    
    KeyFrameArray.CustomArray mConstraintAttributeList;
    
    float[] mTempValues;
    
    public CustomSet(String param1String, KeyFrameArray.CustomArray param1CustomArray) {
      this.mAttributeName = param1String.split(",")[1];
      this.mConstraintAttributeList = param1CustomArray;
    }
    
    public void setPoint(int param1Int, float param1Float) {
      throw new RuntimeException("don't call for custom attribute call setPoint(pos, ConstraintAttribute)");
    }
    
    public void setPoint(int param1Int, CustomAttribute param1CustomAttribute) {
      this.mConstraintAttributeList.append(param1Int, param1CustomAttribute);
    }
    
    public void setProperty(WidgetFrame param1WidgetFrame, float param1Float) {
      this.mCurveFit.getPos(param1Float, this.mTempValues);
      this.mConstraintAttributeList.valueAt(0).setInterpolatedValue(param1WidgetFrame, this.mTempValues);
    }
    
    public void setup(int param1Int) {
      int j = this.mConstraintAttributeList.size();
      int i = this.mConstraintAttributeList.valueAt(0).numberOfInterpolatedValues();
      double[] arrayOfDouble = new double[j];
      this.mTempValues = new float[i];
      double[][] arrayOfDouble1 = new double[j][i];
      i = 0;
      while (i < j) {
        int k = this.mConstraintAttributeList.keyAt(i);
        CustomAttribute customAttribute = this.mConstraintAttributeList.valueAt(i);
        arrayOfDouble[i] = k * 0.01D;
        customAttribute.getValuesToInterpolate(this.mTempValues);
        k = 0;
        while (true) {
          float[] arrayOfFloat = this.mTempValues;
          if (k < arrayOfFloat.length) {
            arrayOfDouble1[i][k] = arrayOfFloat[k];
            k++;
            continue;
          } 
          i++;
        } 
      } 
      this.mCurveFit = CurveFit.get(param1Int, arrayOfDouble, arrayOfDouble1);
    }
  }
  
  public static class CustomSpline extends SplineSet {
    String mAttributeName;
    
    KeyFrameArray.CustomVar mConstraintAttributeList;
    
    float[] mTempValues;
    
    public CustomSpline(String param1String, KeyFrameArray.CustomVar param1CustomVar) {
      this.mAttributeName = param1String.split(",")[1];
      this.mConstraintAttributeList = param1CustomVar;
    }
    
    public void setPoint(int param1Int, float param1Float) {
      throw new RuntimeException("don't call for custom attribute call setPoint(pos, ConstraintAttribute)");
    }
    
    public void setPoint(int param1Int, CustomVariable param1CustomVariable) {
      this.mConstraintAttributeList.append(param1Int, param1CustomVariable);
    }
    
    public void setProperty(MotionWidget param1MotionWidget, float param1Float) {
      this.mCurveFit.getPos(param1Float, this.mTempValues);
      this.mConstraintAttributeList.valueAt(0).setInterpolatedValue(param1MotionWidget, this.mTempValues);
    }
    
    public void setProperty(TypedValues param1TypedValues, float param1Float) {
      setProperty((MotionWidget)param1TypedValues, param1Float);
    }
    
    public void setup(int param1Int) {
      int j = this.mConstraintAttributeList.size();
      int i = this.mConstraintAttributeList.valueAt(0).numberOfInterpolatedValues();
      double[] arrayOfDouble1 = new double[j];
      this.mTempValues = new float[i];
      double[][] arrayOfDouble = new double[j][i];
      i = 0;
      while (i < j) {
        int k = this.mConstraintAttributeList.keyAt(i);
        CustomVariable customVariable = this.mConstraintAttributeList.valueAt(i);
        arrayOfDouble1[i] = k * 0.01D;
        customVariable.getValuesToInterpolate(this.mTempValues);
        k = 0;
        while (true) {
          float[] arrayOfFloat = this.mTempValues;
          if (k < arrayOfFloat.length) {
            arrayOfDouble[i][k] = arrayOfFloat[k];
            k++;
            continue;
          } 
          i++;
        } 
      } 
      this.mCurveFit = CurveFit.get(param1Int, arrayOfDouble1, arrayOfDouble);
    }
  }
  
  private static class Sort {
    static void doubleQuickSort(int[] param1ArrayOfint, float[] param1ArrayOffloat, int param1Int1, int param1Int2) {
      int[] arrayOfInt = new int[param1ArrayOfint.length + 10];
      int i = 0 + 1;
      arrayOfInt[0] = param1Int2;
      param1Int2 = i + 1;
      arrayOfInt[i] = param1Int1;
      param1Int1 = param1Int2;
      while (param1Int1 > 0) {
        i = arrayOfInt[--param1Int1];
        param1Int2 = param1Int1 - 1;
        int j = arrayOfInt[param1Int2];
        param1Int1 = param1Int2;
        if (i < j) {
          int k = partition(param1ArrayOfint, param1ArrayOffloat, i, j);
          int m = param1Int2 + 1;
          arrayOfInt[param1Int2] = k - 1;
          param1Int1 = m + 1;
          arrayOfInt[m] = i;
          param1Int2 = param1Int1 + 1;
          arrayOfInt[param1Int1] = j;
          param1Int1 = param1Int2 + 1;
          arrayOfInt[param1Int2] = k + 1;
        } 
      } 
    }
    
    private static int partition(int[] param1ArrayOfint, float[] param1ArrayOffloat, int param1Int1, int param1Int2) {
      int j = param1ArrayOfint[param1Int2];
      int i;
      for (i = param1Int1; param1Int1 < param1Int2; i = k) {
        int k = i;
        if (param1ArrayOfint[param1Int1] <= j) {
          swap(param1ArrayOfint, param1ArrayOffloat, i, param1Int1);
          k = i + 1;
        } 
        param1Int1++;
      } 
      swap(param1ArrayOfint, param1ArrayOffloat, i, param1Int2);
      return i;
    }
    
    private static void swap(int[] param1ArrayOfint, float[] param1ArrayOffloat, int param1Int1, int param1Int2) {
      int i = param1ArrayOfint[param1Int1];
      param1ArrayOfint[param1Int1] = param1ArrayOfint[param1Int2];
      param1ArrayOfint[param1Int2] = i;
      float f = param1ArrayOffloat[param1Int1];
      param1ArrayOffloat[param1Int1] = param1ArrayOffloat[param1Int2];
      param1ArrayOffloat[param1Int2] = f;
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\motio\\utils\SplineSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */