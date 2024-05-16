package androidx.constraintlayout.core.motion.utils;

import androidx.constraintlayout.core.motion.MotionWidget;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public abstract class KeyCycleOscillator {
  private static final String TAG = "KeyCycleOscillator";
  
  private CurveFit mCurveFit;
  
  private CycleOscillator mCycleOscillator;
  
  private String mType;
  
  public int mVariesBy = 0;
  
  ArrayList<WavePoint> mWavePoints = new ArrayList<>();
  
  private int mWaveShape = 0;
  
  private String mWaveString = null;
  
  public static KeyCycleOscillator makeWidgetCycle(String paramString) {
    return (KeyCycleOscillator)(paramString.equals("pathRotate") ? new PathRotateSet(paramString) : new CoreSpline(paramString));
  }
  
  public float get(float paramFloat) {
    return (float)this.mCycleOscillator.getValues(paramFloat);
  }
  
  public CurveFit getCurveFit() {
    return this.mCurveFit;
  }
  
  public float getSlope(float paramFloat) {
    return (float)this.mCycleOscillator.getSlope(paramFloat);
  }
  
  protected void setCustom(Object paramObject) {}
  
  public void setPoint(int paramInt1, int paramInt2, String paramString, int paramInt3, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
    this.mWavePoints.add(new WavePoint(paramInt1, paramFloat1, paramFloat2, paramFloat3, paramFloat4));
    if (paramInt3 != -1)
      this.mVariesBy = paramInt3; 
    this.mWaveShape = paramInt2;
    this.mWaveString = paramString;
  }
  
  public void setPoint(int paramInt1, int paramInt2, String paramString, int paramInt3, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, Object paramObject) {
    this.mWavePoints.add(new WavePoint(paramInt1, paramFloat1, paramFloat2, paramFloat3, paramFloat4));
    if (paramInt3 != -1)
      this.mVariesBy = paramInt3; 
    this.mWaveShape = paramInt2;
    setCustom(paramObject);
    this.mWaveString = paramString;
  }
  
  public void setProperty(MotionWidget paramMotionWidget, float paramFloat) {}
  
  public void setType(String paramString) {
    this.mType = paramString;
  }
  
  public void setup(float paramFloat) {
    int i = this.mWavePoints.size();
    if (i == 0)
      return; 
    Collections.sort(this.mWavePoints, new Comparator<WavePoint>() {
          final KeyCycleOscillator this$0;
          
          public int compare(KeyCycleOscillator.WavePoint param1WavePoint1, KeyCycleOscillator.WavePoint param1WavePoint2) {
            return Integer.compare(param1WavePoint1.mPosition, param1WavePoint2.mPosition);
          }
        });
    double[] arrayOfDouble = new double[i];
    double[][] arrayOfDouble1 = new double[i][3];
    this.mCycleOscillator = new CycleOscillator(this.mWaveShape, this.mWaveString, this.mVariesBy, i);
    i = 0;
    for (WavePoint wavePoint : this.mWavePoints) {
      arrayOfDouble[i] = wavePoint.mPeriod * 0.01D;
      arrayOfDouble1[i][0] = wavePoint.mValue;
      arrayOfDouble1[i][1] = wavePoint.mOffset;
      arrayOfDouble1[i][2] = wavePoint.mPhase;
      this.mCycleOscillator.setPoint(i, wavePoint.mPosition, wavePoint.mPeriod, wavePoint.mOffset, wavePoint.mPhase, wavePoint.mValue);
      i++;
    } 
    this.mCycleOscillator.setup(paramFloat);
    this.mCurveFit = CurveFit.get(0, arrayOfDouble, arrayOfDouble1);
  }
  
  public String toString() {
    String str = this.mType;
    DecimalFormat decimalFormat = new DecimalFormat("##.##");
    for (WavePoint wavePoint : this.mWavePoints)
      str = str + "[" + wavePoint.mPosition + " , " + decimalFormat.format(wavePoint.mValue) + "] "; 
    return str;
  }
  
  public boolean variesByPath() {
    int i = this.mVariesBy;
    boolean bool = true;
    if (i != 1)
      bool = false; 
    return bool;
  }
  
  private static class CoreSpline extends KeyCycleOscillator {
    String type;
    
    int typeId;
    
    public CoreSpline(String param1String) {
      this.type = param1String;
      this.typeId = TypedValues.Cycle.getId(param1String);
    }
    
    public void setProperty(MotionWidget param1MotionWidget, float param1Float) {
      param1MotionWidget.setValue(this.typeId, get(param1Float));
    }
  }
  
  static class CycleOscillator {
    private static final String TAG = "CycleOscillator";
    
    static final int UNSET = -1;
    
    private final int OFFST;
    
    private final int PHASE;
    
    private final int VALUE;
    
    CurveFit mCurveFit;
    
    float[] mOffset;
    
    Oscillator mOscillator;
    
    float mPathLength;
    
    float[] mPeriod;
    
    float[] mPhase;
    
    double[] mPosition;
    
    float[] mScale;
    
    double[] mSplineSlopeCache;
    
    double[] mSplineValueCache;
    
    float[] mValues;
    
    private final int mVariesBy;
    
    int mWaveShape;
    
    CycleOscillator(int param1Int1, String param1String, int param1Int2, int param1Int3) {
      Oscillator oscillator = new Oscillator();
      this.mOscillator = oscillator;
      this.OFFST = 0;
      this.PHASE = 1;
      this.VALUE = 2;
      this.mWaveShape = param1Int1;
      this.mVariesBy = param1Int2;
      oscillator.setType(param1Int1, param1String);
      this.mValues = new float[param1Int3];
      this.mPosition = new double[param1Int3];
      this.mPeriod = new float[param1Int3];
      this.mOffset = new float[param1Int3];
      this.mPhase = new float[param1Int3];
      this.mScale = new float[param1Int3];
    }
    
    public double getLastPhase() {
      return this.mSplineValueCache[1];
    }
    
    public double getSlope(float param1Float) {
      CurveFit curveFit = this.mCurveFit;
      if (curveFit != null) {
        curveFit.getSlope(param1Float, this.mSplineSlopeCache);
        this.mCurveFit.getPos(param1Float, this.mSplineValueCache);
      } else {
        double[] arrayOfDouble1 = this.mSplineSlopeCache;
        arrayOfDouble1[0] = 0.0D;
        arrayOfDouble1[1] = 0.0D;
        arrayOfDouble1[2] = 0.0D;
      } 
      double d1 = this.mOscillator.getValue(param1Float, this.mSplineValueCache[1]);
      double d2 = this.mOscillator.getSlope(param1Float, this.mSplineValueCache[1], this.mSplineSlopeCache[1]);
      double[] arrayOfDouble = this.mSplineSlopeCache;
      return arrayOfDouble[0] + arrayOfDouble[2] * d1 + this.mSplineValueCache[2] * d2;
    }
    
    public double getValues(float param1Float) {
      CurveFit curveFit = this.mCurveFit;
      if (curveFit != null) {
        curveFit.getPos(param1Float, this.mSplineValueCache);
      } else {
        double[] arrayOfDouble1 = this.mSplineValueCache;
        arrayOfDouble1[0] = this.mOffset[0];
        arrayOfDouble1[1] = this.mPhase[0];
        arrayOfDouble1[2] = this.mValues[0];
      } 
      double[] arrayOfDouble = this.mSplineValueCache;
      double d1 = arrayOfDouble[0];
      double d2 = arrayOfDouble[1];
      d2 = this.mOscillator.getValue(param1Float, d2);
      return this.mSplineValueCache[2] * d2 + d1;
    }
    
    public void setPoint(int param1Int1, int param1Int2, float param1Float1, float param1Float2, float param1Float3, float param1Float4) {
      this.mPosition[param1Int1] = param1Int2 / 100.0D;
      this.mPeriod[param1Int1] = param1Float1;
      this.mOffset[param1Int1] = param1Float2;
      this.mPhase[param1Int1] = param1Float3;
      this.mValues[param1Int1] = param1Float4;
    }
    
    public void setup(float param1Float) {
      this.mPathLength = param1Float;
      double[][] arrayOfDouble = new double[this.mPosition.length][3];
      float[] arrayOfFloat = this.mValues;
      this.mSplineValueCache = new double[arrayOfFloat.length + 2];
      this.mSplineSlopeCache = new double[arrayOfFloat.length + 2];
      if (this.mPosition[0] > 0.0D)
        this.mOscillator.addPoint(0.0D, this.mPeriod[0]); 
      double[] arrayOfDouble1 = this.mPosition;
      int i = arrayOfDouble1.length - 1;
      if (arrayOfDouble1[i] < 1.0D)
        this.mOscillator.addPoint(1.0D, this.mPeriod[i]); 
      for (i = 0; i < arrayOfDouble.length; i++) {
        arrayOfDouble[i][0] = this.mOffset[i];
        arrayOfDouble[i][1] = this.mPhase[i];
        arrayOfDouble[i][2] = this.mValues[i];
        this.mOscillator.addPoint(this.mPosition[i], this.mPeriod[i]);
      } 
      this.mOscillator.normalize();
      arrayOfDouble1 = this.mPosition;
      if (arrayOfDouble1.length > 1) {
        this.mCurveFit = CurveFit.get(0, arrayOfDouble1, arrayOfDouble);
      } else {
        this.mCurveFit = null;
      } 
    }
  }
  
  private static class IntDoubleSort {
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
    
    static void sort(int[] param1ArrayOfint, float[] param1ArrayOffloat, int param1Int1, int param1Int2) {
      int[] arrayOfInt = new int[param1ArrayOfint.length + 10];
      int i = 0 + 1;
      arrayOfInt[0] = param1Int2;
      param1Int2 = i + 1;
      arrayOfInt[i] = param1Int1;
      param1Int1 = param1Int2;
      while (param1Int1 > 0) {
        int j = arrayOfInt[--param1Int1];
        param1Int2 = param1Int1 - 1;
        i = arrayOfInt[param1Int2];
        param1Int1 = param1Int2;
        if (j < i) {
          int k = partition(param1ArrayOfint, param1ArrayOffloat, j, i);
          param1Int1 = param1Int2 + 1;
          arrayOfInt[param1Int2] = k - 1;
          param1Int2 = param1Int1 + 1;
          arrayOfInt[param1Int1] = j;
          j = param1Int2 + 1;
          arrayOfInt[param1Int2] = i;
          param1Int1 = j + 1;
          arrayOfInt[j] = k + 1;
        } 
      } 
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
  
  private static class IntFloatFloatSort {
    private static int partition(int[] param1ArrayOfint, float[] param1ArrayOffloat1, float[] param1ArrayOffloat2, int param1Int1, int param1Int2) {
      int k = param1ArrayOfint[param1Int2];
      int i = param1Int1;
      int j;
      for (j = i; param1Int1 < param1Int2; j = i) {
        i = j;
        if (param1ArrayOfint[param1Int1] <= k) {
          swap(param1ArrayOfint, param1ArrayOffloat1, param1ArrayOffloat2, j, param1Int1);
          i = j + 1;
        } 
        param1Int1++;
      } 
      swap(param1ArrayOfint, param1ArrayOffloat1, param1ArrayOffloat2, j, param1Int2);
      return j;
    }
    
    static void sort(int[] param1ArrayOfint, float[] param1ArrayOffloat1, float[] param1ArrayOffloat2, int param1Int1, int param1Int2) {
      int[] arrayOfInt = new int[param1ArrayOfint.length + 10];
      int i = 0 + 1;
      arrayOfInt[0] = param1Int2;
      param1Int2 = i + 1;
      arrayOfInt[i] = param1Int1;
      param1Int1 = param1Int2;
      while (param1Int1 > 0) {
        int j = arrayOfInt[--param1Int1];
        param1Int2 = param1Int1 - 1;
        i = arrayOfInt[param1Int2];
        param1Int1 = param1Int2;
        if (j < i) {
          int k = partition(param1ArrayOfint, param1ArrayOffloat1, param1ArrayOffloat2, j, i);
          int m = param1Int2 + 1;
          arrayOfInt[param1Int2] = k - 1;
          param1Int1 = m + 1;
          arrayOfInt[m] = j;
          param1Int2 = param1Int1 + 1;
          arrayOfInt[param1Int1] = i;
          param1Int1 = param1Int2 + 1;
          arrayOfInt[param1Int2] = k + 1;
        } 
      } 
    }
    
    private static void swap(int[] param1ArrayOfint, float[] param1ArrayOffloat1, float[] param1ArrayOffloat2, int param1Int1, int param1Int2) {
      int i = param1ArrayOfint[param1Int1];
      param1ArrayOfint[param1Int1] = param1ArrayOfint[param1Int2];
      param1ArrayOfint[param1Int2] = i;
      float f = param1ArrayOffloat1[param1Int1];
      param1ArrayOffloat1[param1Int1] = param1ArrayOffloat1[param1Int2];
      param1ArrayOffloat1[param1Int2] = f;
      f = param1ArrayOffloat2[param1Int1];
      param1ArrayOffloat2[param1Int1] = param1ArrayOffloat2[param1Int2];
      param1ArrayOffloat2[param1Int2] = f;
    }
  }
  
  public static class PathRotateSet extends KeyCycleOscillator {
    String type;
    
    int typeId;
    
    public PathRotateSet(String param1String) {
      this.type = param1String;
      this.typeId = TypedValues.Cycle.getId(param1String);
    }
    
    public void setPathRotate(MotionWidget param1MotionWidget, float param1Float, double param1Double1, double param1Double2) {
      param1MotionWidget.setRotationZ(get(param1Float) + (float)Math.toDegrees(Math.atan2(param1Double2, param1Double1)));
    }
    
    public void setProperty(MotionWidget param1MotionWidget, float param1Float) {
      param1MotionWidget.setValue(this.typeId, get(param1Float));
    }
  }
  
  static class WavePoint {
    float mOffset;
    
    float mPeriod;
    
    float mPhase;
    
    int mPosition;
    
    float mValue;
    
    public WavePoint(int param1Int, float param1Float1, float param1Float2, float param1Float3, float param1Float4) {
      this.mPosition = param1Int;
      this.mValue = param1Float4;
      this.mOffset = param1Float2;
      this.mPeriod = param1Float1;
      this.mPhase = param1Float3;
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\motio\\utils\KeyCycleOscillator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */