package androidx.constraintlayout.core.motion.utils;

import java.util.Arrays;

public class TypedBundle {
  private static final int INITIAL_BOOLEAN = 4;
  
  private static final int INITIAL_FLOAT = 10;
  
  private static final int INITIAL_INT = 10;
  
  private static final int INITIAL_STRING = 5;
  
  int mCountBoolean = 0;
  
  int mCountFloat = 0;
  
  int mCountInt = 0;
  
  int mCountString = 0;
  
  int[] mTypeBoolean = new int[4];
  
  int[] mTypeFloat = new int[10];
  
  int[] mTypeInt = new int[10];
  
  int[] mTypeString = new int[5];
  
  boolean[] mValueBoolean = new boolean[4];
  
  float[] mValueFloat = new float[10];
  
  int[] mValueInt = new int[10];
  
  String[] mValueString = new String[5];
  
  public void add(int paramInt, float paramFloat) {
    int i = this.mCountFloat;
    int[] arrayOfInt = this.mTypeFloat;
    if (i >= arrayOfInt.length) {
      this.mTypeFloat = Arrays.copyOf(arrayOfInt, arrayOfInt.length * 2);
      float[] arrayOfFloat1 = this.mValueFloat;
      this.mValueFloat = Arrays.copyOf(arrayOfFloat1, arrayOfFloat1.length * 2);
    } 
    arrayOfInt = this.mTypeFloat;
    i = this.mCountFloat;
    arrayOfInt[i] = paramInt;
    float[] arrayOfFloat = this.mValueFloat;
    this.mCountFloat = i + 1;
    arrayOfFloat[i] = paramFloat;
  }
  
  public void add(int paramInt1, int paramInt2) {
    int i = this.mCountInt;
    int[] arrayOfInt = this.mTypeInt;
    if (i >= arrayOfInt.length) {
      this.mTypeInt = Arrays.copyOf(arrayOfInt, arrayOfInt.length * 2);
      arrayOfInt = this.mValueInt;
      this.mValueInt = Arrays.copyOf(arrayOfInt, arrayOfInt.length * 2);
    } 
    arrayOfInt = this.mTypeInt;
    i = this.mCountInt;
    arrayOfInt[i] = paramInt1;
    arrayOfInt = this.mValueInt;
    this.mCountInt = i + 1;
    arrayOfInt[i] = paramInt2;
  }
  
  public void add(int paramInt, String paramString) {
    int i = this.mCountString;
    int[] arrayOfInt = this.mTypeString;
    if (i >= arrayOfInt.length) {
      this.mTypeString = Arrays.copyOf(arrayOfInt, arrayOfInt.length * 2);
      String[] arrayOfString1 = this.mValueString;
      this.mValueString = Arrays.<String>copyOf(arrayOfString1, arrayOfString1.length * 2);
    } 
    arrayOfInt = this.mTypeString;
    i = this.mCountString;
    arrayOfInt[i] = paramInt;
    String[] arrayOfString = this.mValueString;
    this.mCountString = i + 1;
    arrayOfString[i] = paramString;
  }
  
  public void add(int paramInt, boolean paramBoolean) {
    int i = this.mCountBoolean;
    int[] arrayOfInt = this.mTypeBoolean;
    if (i >= arrayOfInt.length) {
      this.mTypeBoolean = Arrays.copyOf(arrayOfInt, arrayOfInt.length * 2);
      boolean[] arrayOfBoolean1 = this.mValueBoolean;
      this.mValueBoolean = Arrays.copyOf(arrayOfBoolean1, arrayOfBoolean1.length * 2);
    } 
    arrayOfInt = this.mTypeBoolean;
    i = this.mCountBoolean;
    arrayOfInt[i] = paramInt;
    boolean[] arrayOfBoolean = this.mValueBoolean;
    this.mCountBoolean = i + 1;
    arrayOfBoolean[i] = paramBoolean;
  }
  
  public void addIfNotNull(int paramInt, String paramString) {
    if (paramString != null)
      add(paramInt, paramString); 
  }
  
  public void applyDelta(TypedBundle paramTypedBundle) {
    byte b;
    for (b = 0; b < this.mCountInt; b++)
      paramTypedBundle.add(this.mTypeInt[b], this.mValueInt[b]); 
    for (b = 0; b < this.mCountFloat; b++)
      paramTypedBundle.add(this.mTypeFloat[b], this.mValueFloat[b]); 
    for (b = 0; b < this.mCountString; b++)
      paramTypedBundle.add(this.mTypeString[b], this.mValueString[b]); 
    for (b = 0; b < this.mCountBoolean; b++)
      paramTypedBundle.add(this.mTypeBoolean[b], this.mValueBoolean[b]); 
  }
  
  public void applyDelta(TypedValues paramTypedValues) {
    byte b;
    for (b = 0; b < this.mCountInt; b++)
      paramTypedValues.setValue(this.mTypeInt[b], this.mValueInt[b]); 
    for (b = 0; b < this.mCountFloat; b++)
      paramTypedValues.setValue(this.mTypeFloat[b], this.mValueFloat[b]); 
    for (b = 0; b < this.mCountString; b++)
      paramTypedValues.setValue(this.mTypeString[b], this.mValueString[b]); 
    for (b = 0; b < this.mCountBoolean; b++)
      paramTypedValues.setValue(this.mTypeBoolean[b], this.mValueBoolean[b]); 
  }
  
  public void clear() {
    this.mCountBoolean = 0;
    this.mCountString = 0;
    this.mCountFloat = 0;
    this.mCountInt = 0;
  }
  
  public int getInteger(int paramInt) {
    for (byte b = 0; b < this.mCountInt; b++) {
      if (this.mTypeInt[b] == paramInt)
        return this.mValueInt[b]; 
    } 
    return -1;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\motio\\utils\TypedBundle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */