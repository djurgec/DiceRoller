package androidx.constraintlayout.core;

import java.util.Arrays;

public class ArrayLinkedVariables implements ArrayRow.ArrayRowVariables {
  private static final boolean DEBUG = false;
  
  private static final boolean FULL_NEW_CHECK = false;
  
  static final int NONE = -1;
  
  private static float epsilon = 0.001F;
  
  private int ROW_SIZE = 8;
  
  private SolverVariable candidate = null;
  
  int currentSize = 0;
  
  private int[] mArrayIndices = new int[8];
  
  private int[] mArrayNextIndices = new int[8];
  
  private float[] mArrayValues = new float[8];
  
  protected final Cache mCache;
  
  private boolean mDidFillOnce = false;
  
  private int mHead = -1;
  
  private int mLast = -1;
  
  private final ArrayRow mRow;
  
  ArrayLinkedVariables(ArrayRow paramArrayRow, Cache paramCache) {
    this.mRow = paramArrayRow;
    this.mCache = paramCache;
  }
  
  public void add(SolverVariable paramSolverVariable, float paramFloat, boolean paramBoolean) {
    float f = epsilon;
    if (paramFloat > -f && paramFloat < f)
      return; 
    if (this.mHead == -1) {
      this.mHead = 0;
      this.mArrayValues[0] = paramFloat;
      this.mArrayIndices[0] = paramSolverVariable.id;
      this.mArrayNextIndices[this.mHead] = -1;
      paramSolverVariable.usageInRowCount++;
      paramSolverVariable.addToRow(this.mRow);
      this.currentSize++;
      if (!this.mDidFillOnce) {
        int m = this.mLast + 1;
        this.mLast = m;
        arrayOfInt1 = this.mArrayIndices;
        if (m >= arrayOfInt1.length) {
          this.mDidFillOnce = true;
          this.mLast = arrayOfInt1.length - 1;
        } 
      } 
      return;
    } 
    int i = this.mHead;
    int k = -1;
    int j;
    for (j = 0; i != -1 && j < this.currentSize; j++) {
      if (this.mArrayIndices[i] == ((SolverVariable)arrayOfInt1).id) {
        float[] arrayOfFloat = this.mArrayValues;
        f = arrayOfFloat[i] + paramFloat;
        float f1 = epsilon;
        paramFloat = f;
        if (f > -f1) {
          paramFloat = f;
          if (f < f1)
            paramFloat = 0.0F; 
        } 
        arrayOfFloat[i] = paramFloat;
        if (paramFloat == 0.0F) {
          if (i == this.mHead) {
            this.mHead = this.mArrayNextIndices[i];
          } else {
            int[] arrayOfInt = this.mArrayNextIndices;
            arrayOfInt[k] = arrayOfInt[i];
          } 
          if (paramBoolean)
            arrayOfInt1.removeFromRow(this.mRow); 
          if (this.mDidFillOnce)
            this.mLast = i; 
          ((SolverVariable)arrayOfInt1).usageInRowCount--;
          this.currentSize--;
        } 
        return;
      } 
      if (this.mArrayIndices[i] < ((SolverVariable)arrayOfInt1).id)
        k = i; 
      i = this.mArrayNextIndices[i];
    } 
    j = this.mLast;
    i = j + 1;
    if (this.mDidFillOnce) {
      int[] arrayOfInt = this.mArrayIndices;
      if (arrayOfInt[j] == -1) {
        i = this.mLast;
      } else {
        i = arrayOfInt.length;
      } 
    } 
    int[] arrayOfInt2 = this.mArrayIndices;
    j = i;
    if (i >= arrayOfInt2.length) {
      j = i;
      if (this.currentSize < arrayOfInt2.length) {
        byte b = 0;
        while (true) {
          arrayOfInt2 = this.mArrayIndices;
          j = i;
          if (b < arrayOfInt2.length) {
            if (arrayOfInt2[b] == -1) {
              j = b;
              break;
            } 
            b++;
            continue;
          } 
          break;
        } 
      } 
    } 
    arrayOfInt2 = this.mArrayIndices;
    i = j;
    if (j >= arrayOfInt2.length) {
      i = arrayOfInt2.length;
      j = this.ROW_SIZE * 2;
      this.ROW_SIZE = j;
      this.mDidFillOnce = false;
      this.mLast = i - 1;
      this.mArrayValues = Arrays.copyOf(this.mArrayValues, j);
      this.mArrayIndices = Arrays.copyOf(this.mArrayIndices, this.ROW_SIZE);
      this.mArrayNextIndices = Arrays.copyOf(this.mArrayNextIndices, this.ROW_SIZE);
    } 
    this.mArrayIndices[i] = ((SolverVariable)arrayOfInt1).id;
    this.mArrayValues[i] = paramFloat;
    if (k != -1) {
      arrayOfInt2 = this.mArrayNextIndices;
      arrayOfInt2[i] = arrayOfInt2[k];
      arrayOfInt2[k] = i;
    } else {
      this.mArrayNextIndices[i] = this.mHead;
      this.mHead = i;
    } 
    ((SolverVariable)arrayOfInt1).usageInRowCount++;
    arrayOfInt1.addToRow(this.mRow);
    this.currentSize++;
    if (!this.mDidFillOnce)
      this.mLast++; 
    i = this.mLast;
    int[] arrayOfInt1 = this.mArrayIndices;
    if (i >= arrayOfInt1.length) {
      this.mDidFillOnce = true;
      this.mLast = arrayOfInt1.length - 1;
    } 
  }
  
  public final void clear() {
    int i = this.mHead;
    for (byte b = 0; i != -1 && b < this.currentSize; b++) {
      SolverVariable solverVariable = this.mCache.mIndexedVariables[this.mArrayIndices[i]];
      if (solverVariable != null)
        solverVariable.removeFromRow(this.mRow); 
      i = this.mArrayNextIndices[i];
    } 
    this.mHead = -1;
    this.mLast = -1;
    this.mDidFillOnce = false;
    this.currentSize = 0;
  }
  
  public boolean contains(SolverVariable paramSolverVariable) {
    if (this.mHead == -1)
      return false; 
    int i = this.mHead;
    for (byte b = 0; i != -1 && b < this.currentSize; b++) {
      if (this.mArrayIndices[i] == paramSolverVariable.id)
        return true; 
      i = this.mArrayNextIndices[i];
    } 
    return false;
  }
  
  public void display() {
    int i = this.currentSize;
    System.out.print("{ ");
    for (byte b = 0; b < i; b++) {
      SolverVariable solverVariable = getVariable(b);
      if (solverVariable != null)
        System.out.print(solverVariable + " = " + getVariableValue(b) + " "); 
    } 
    System.out.println(" }");
  }
  
  public void divideByAmount(float paramFloat) {
    int i = this.mHead;
    for (byte b = 0; i != -1 && b < this.currentSize; b++) {
      float[] arrayOfFloat = this.mArrayValues;
      arrayOfFloat[i] = arrayOfFloat[i] / paramFloat;
      i = this.mArrayNextIndices[i];
    } 
  }
  
  public final float get(SolverVariable paramSolverVariable) {
    int i = this.mHead;
    for (byte b = 0; i != -1 && b < this.currentSize; b++) {
      if (this.mArrayIndices[i] == paramSolverVariable.id)
        return this.mArrayValues[i]; 
      i = this.mArrayNextIndices[i];
    } 
    return 0.0F;
  }
  
  public int getCurrentSize() {
    return this.currentSize;
  }
  
  public int getHead() {
    return this.mHead;
  }
  
  public final int getId(int paramInt) {
    return this.mArrayIndices[paramInt];
  }
  
  public final int getNextIndice(int paramInt) {
    return this.mArrayNextIndices[paramInt];
  }
  
  SolverVariable getPivotCandidate() {
    // Byte code:
    //   0: aload_0
    //   1: getfield candidate : Landroidx/constraintlayout/core/SolverVariable;
    //   4: astore_3
    //   5: aload_3
    //   6: ifnonnull -> 102
    //   9: aload_0
    //   10: getfield mHead : I
    //   13: istore_2
    //   14: iconst_0
    //   15: istore_1
    //   16: aconst_null
    //   17: astore_3
    //   18: iload_2
    //   19: iconst_m1
    //   20: if_icmpeq -> 100
    //   23: iload_1
    //   24: aload_0
    //   25: getfield currentSize : I
    //   28: if_icmpge -> 100
    //   31: aload_3
    //   32: astore #4
    //   34: aload_0
    //   35: getfield mArrayValues : [F
    //   38: iload_2
    //   39: faload
    //   40: fconst_0
    //   41: fcmpg
    //   42: ifge -> 84
    //   45: aload_0
    //   46: getfield mCache : Landroidx/constraintlayout/core/Cache;
    //   49: getfield mIndexedVariables : [Landroidx/constraintlayout/core/SolverVariable;
    //   52: aload_0
    //   53: getfield mArrayIndices : [I
    //   56: iload_2
    //   57: iaload
    //   58: aaload
    //   59: astore #5
    //   61: aload_3
    //   62: ifnull -> 80
    //   65: aload_3
    //   66: astore #4
    //   68: aload_3
    //   69: getfield strength : I
    //   72: aload #5
    //   74: getfield strength : I
    //   77: if_icmpge -> 84
    //   80: aload #5
    //   82: astore #4
    //   84: aload_0
    //   85: getfield mArrayNextIndices : [I
    //   88: iload_2
    //   89: iaload
    //   90: istore_2
    //   91: iinc #1, 1
    //   94: aload #4
    //   96: astore_3
    //   97: goto -> 18
    //   100: aload_3
    //   101: areturn
    //   102: aload_3
    //   103: areturn
  }
  
  public final float getValue(int paramInt) {
    return this.mArrayValues[paramInt];
  }
  
  public SolverVariable getVariable(int paramInt) {
    int i = this.mHead;
    for (byte b = 0; i != -1 && b < this.currentSize; b++) {
      if (b == paramInt)
        return this.mCache.mIndexedVariables[this.mArrayIndices[i]]; 
      i = this.mArrayNextIndices[i];
    } 
    return null;
  }
  
  public float getVariableValue(int paramInt) {
    int i = this.mHead;
    for (byte b = 0; i != -1 && b < this.currentSize; b++) {
      if (b == paramInt)
        return this.mArrayValues[i]; 
      i = this.mArrayNextIndices[i];
    } 
    return 0.0F;
  }
  
  boolean hasAtLeastOnePositiveVariable() {
    int i = this.mHead;
    for (byte b = 0; i != -1 && b < this.currentSize; b++) {
      if (this.mArrayValues[i] > 0.0F)
        return true; 
      i = this.mArrayNextIndices[i];
    } 
    return false;
  }
  
  public int indexOf(SolverVariable paramSolverVariable) {
    if (this.mHead == -1)
      return -1; 
    int i = this.mHead;
    for (byte b = 0; i != -1 && b < this.currentSize; b++) {
      if (this.mArrayIndices[i] == paramSolverVariable.id)
        return i; 
      i = this.mArrayNextIndices[i];
    } 
    return -1;
  }
  
  public void invert() {
    int i = this.mHead;
    for (byte b = 0; i != -1 && b < this.currentSize; b++) {
      float[] arrayOfFloat = this.mArrayValues;
      arrayOfFloat[i] = arrayOfFloat[i] * -1.0F;
      i = this.mArrayNextIndices[i];
    } 
  }
  
  public final void put(SolverVariable paramSolverVariable, float paramFloat) {
    if (paramFloat == 0.0F) {
      remove(paramSolverVariable, true);
      return;
    } 
    if (this.mHead == -1) {
      this.mHead = 0;
      this.mArrayValues[0] = paramFloat;
      this.mArrayIndices[0] = paramSolverVariable.id;
      this.mArrayNextIndices[this.mHead] = -1;
      paramSolverVariable.usageInRowCount++;
      paramSolverVariable.addToRow(this.mRow);
      this.currentSize++;
      if (!this.mDidFillOnce) {
        int m = this.mLast + 1;
        this.mLast = m;
        arrayOfInt1 = this.mArrayIndices;
        if (m >= arrayOfInt1.length) {
          this.mDidFillOnce = true;
          this.mLast = arrayOfInt1.length - 1;
        } 
      } 
      return;
    } 
    int i = this.mHead;
    int k = -1;
    int j;
    for (j = 0; i != -1 && j < this.currentSize; j++) {
      if (this.mArrayIndices[i] == ((SolverVariable)arrayOfInt1).id) {
        this.mArrayValues[i] = paramFloat;
        return;
      } 
      if (this.mArrayIndices[i] < ((SolverVariable)arrayOfInt1).id)
        k = i; 
      i = this.mArrayNextIndices[i];
    } 
    j = this.mLast;
    i = j + 1;
    if (this.mDidFillOnce) {
      int[] arrayOfInt = this.mArrayIndices;
      if (arrayOfInt[j] == -1) {
        i = this.mLast;
      } else {
        i = arrayOfInt.length;
      } 
    } 
    int[] arrayOfInt2 = this.mArrayIndices;
    j = i;
    if (i >= arrayOfInt2.length) {
      j = i;
      if (this.currentSize < arrayOfInt2.length) {
        byte b = 0;
        while (true) {
          arrayOfInt2 = this.mArrayIndices;
          j = i;
          if (b < arrayOfInt2.length) {
            if (arrayOfInt2[b] == -1) {
              j = b;
              break;
            } 
            b++;
            continue;
          } 
          break;
        } 
      } 
    } 
    arrayOfInt2 = this.mArrayIndices;
    i = j;
    if (j >= arrayOfInt2.length) {
      i = arrayOfInt2.length;
      j = this.ROW_SIZE * 2;
      this.ROW_SIZE = j;
      this.mDidFillOnce = false;
      this.mLast = i - 1;
      this.mArrayValues = Arrays.copyOf(this.mArrayValues, j);
      this.mArrayIndices = Arrays.copyOf(this.mArrayIndices, this.ROW_SIZE);
      this.mArrayNextIndices = Arrays.copyOf(this.mArrayNextIndices, this.ROW_SIZE);
    } 
    this.mArrayIndices[i] = ((SolverVariable)arrayOfInt1).id;
    this.mArrayValues[i] = paramFloat;
    if (k != -1) {
      arrayOfInt2 = this.mArrayNextIndices;
      arrayOfInt2[i] = arrayOfInt2[k];
      arrayOfInt2[k] = i;
    } else {
      this.mArrayNextIndices[i] = this.mHead;
      this.mHead = i;
    } 
    ((SolverVariable)arrayOfInt1).usageInRowCount++;
    arrayOfInt1.addToRow(this.mRow);
    i = this.currentSize + 1;
    this.currentSize = i;
    if (!this.mDidFillOnce)
      this.mLast++; 
    int[] arrayOfInt1 = this.mArrayIndices;
    if (i >= arrayOfInt1.length)
      this.mDidFillOnce = true; 
    if (this.mLast >= arrayOfInt1.length) {
      this.mDidFillOnce = true;
      this.mLast = arrayOfInt1.length - 1;
    } 
  }
  
  public final float remove(SolverVariable paramSolverVariable, boolean paramBoolean) {
    if (this.candidate == paramSolverVariable)
      this.candidate = null; 
    if (this.mHead == -1)
      return 0.0F; 
    int i = this.mHead;
    int j = -1;
    for (byte b = 0; i != -1 && b < this.currentSize; b++) {
      if (this.mArrayIndices[i] == paramSolverVariable.id) {
        if (i == this.mHead) {
          this.mHead = this.mArrayNextIndices[i];
        } else {
          int[] arrayOfInt = this.mArrayNextIndices;
          arrayOfInt[j] = arrayOfInt[i];
        } 
        if (paramBoolean)
          paramSolverVariable.removeFromRow(this.mRow); 
        paramSolverVariable.usageInRowCount--;
        this.currentSize--;
        this.mArrayIndices[i] = -1;
        if (this.mDidFillOnce)
          this.mLast = i; 
        return this.mArrayValues[i];
      } 
      j = i;
      i = this.mArrayNextIndices[i];
    } 
    return 0.0F;
  }
  
  public int sizeInBytes() {
    return 0 + this.mArrayIndices.length * 4 * 3 + 36;
  }
  
  public String toString() {
    String str = "";
    int i = this.mHead;
    for (byte b = 0; i != -1 && b < this.currentSize; b++) {
      str = str + " -> ";
      str = str + this.mArrayValues[i] + " : ";
      str = str + this.mCache.mIndexedVariables[this.mArrayIndices[i]];
      i = this.mArrayNextIndices[i];
    } 
    return str;
  }
  
  public float use(ArrayRow paramArrayRow, boolean paramBoolean) {
    float f = get(paramArrayRow.variable);
    remove(paramArrayRow.variable, paramBoolean);
    ArrayRow.ArrayRowVariables arrayRowVariables = paramArrayRow.variables;
    int i = arrayRowVariables.getCurrentSize();
    for (byte b = 0; b < i; b++) {
      SolverVariable solverVariable = arrayRowVariables.getVariable(b);
      add(solverVariable, arrayRowVariables.get(solverVariable) * f, paramBoolean);
    } 
    return f;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\ArrayLinkedVariables.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */