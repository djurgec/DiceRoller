package androidx.constraintlayout.core;

import java.util.ArrayList;

public class ArrayRow implements LinearSystem.Row {
  private static final boolean DEBUG = false;
  
  private static final boolean FULL_NEW_CHECK = false;
  
  float constantValue = 0.0F;
  
  boolean isSimpleDefinition = false;
  
  boolean used = false;
  
  SolverVariable variable = null;
  
  public ArrayRowVariables variables;
  
  ArrayList<SolverVariable> variablesToUpdate = new ArrayList<>();
  
  public ArrayRow() {}
  
  public ArrayRow(Cache paramCache) {
    this.variables = new ArrayLinkedVariables(this, paramCache);
  }
  
  private boolean isNew(SolverVariable paramSolverVariable, LinearSystem paramLinearSystem) {
    int i = paramSolverVariable.usageInRowCount;
    boolean bool = true;
    if (i > 1)
      bool = false; 
    return bool;
  }
  
  private SolverVariable pickPivotInVariables(boolean[] paramArrayOfboolean, SolverVariable paramSolverVariable) {
    // Byte code:
    //   0: fconst_0
    //   1: fstore #6
    //   3: aconst_null
    //   4: astore #11
    //   6: aconst_null
    //   7: astore #10
    //   9: fconst_0
    //   10: fstore #5
    //   12: aload_0
    //   13: getfield variables : Landroidx/constraintlayout/core/ArrayRow$ArrayRowVariables;
    //   16: invokeinterface getCurrentSize : ()I
    //   21: istore #9
    //   23: iconst_0
    //   24: istore #8
    //   26: iload #8
    //   28: iload #9
    //   30: if_icmpge -> 350
    //   33: aload_0
    //   34: getfield variables : Landroidx/constraintlayout/core/ArrayRow$ArrayRowVariables;
    //   37: iload #8
    //   39: invokeinterface getVariableValue : (I)F
    //   44: fstore #4
    //   46: fload #6
    //   48: fstore_3
    //   49: aload #11
    //   51: astore #12
    //   53: aload #10
    //   55: astore #14
    //   57: fload #5
    //   59: fstore #7
    //   61: fload #4
    //   63: fconst_0
    //   64: fcmpg
    //   65: ifge -> 329
    //   68: aload_0
    //   69: getfield variables : Landroidx/constraintlayout/core/ArrayRow$ArrayRowVariables;
    //   72: iload #8
    //   74: invokeinterface getVariable : (I)Landroidx/constraintlayout/core/SolverVariable;
    //   79: astore #13
    //   81: aload_1
    //   82: ifnull -> 110
    //   85: fload #6
    //   87: fstore_3
    //   88: aload #11
    //   90: astore #12
    //   92: aload #10
    //   94: astore #14
    //   96: fload #5
    //   98: fstore #7
    //   100: aload_1
    //   101: aload #13
    //   103: getfield id : I
    //   106: baload
    //   107: ifne -> 329
    //   110: fload #6
    //   112: fstore_3
    //   113: aload #11
    //   115: astore #12
    //   117: aload #10
    //   119: astore #14
    //   121: fload #5
    //   123: fstore #7
    //   125: aload #13
    //   127: aload_2
    //   128: if_acmpeq -> 329
    //   131: iconst_1
    //   132: ifeq -> 213
    //   135: aload #13
    //   137: getfield mType : Landroidx/constraintlayout/core/SolverVariable$Type;
    //   140: getstatic androidx/constraintlayout/core/SolverVariable$Type.SLACK : Landroidx/constraintlayout/core/SolverVariable$Type;
    //   143: if_acmpeq -> 172
    //   146: fload #6
    //   148: fstore_3
    //   149: aload #11
    //   151: astore #12
    //   153: aload #10
    //   155: astore #14
    //   157: fload #5
    //   159: fstore #7
    //   161: aload #13
    //   163: getfield mType : Landroidx/constraintlayout/core/SolverVariable$Type;
    //   166: getstatic androidx/constraintlayout/core/SolverVariable$Type.ERROR : Landroidx/constraintlayout/core/SolverVariable$Type;
    //   169: if_acmpne -> 329
    //   172: fload #6
    //   174: fstore_3
    //   175: aload #11
    //   177: astore #12
    //   179: aload #10
    //   181: astore #14
    //   183: fload #5
    //   185: fstore #7
    //   187: fload #4
    //   189: fload #6
    //   191: fcmpg
    //   192: ifge -> 329
    //   195: fload #4
    //   197: fstore_3
    //   198: aload #13
    //   200: astore #12
    //   202: aload #10
    //   204: astore #14
    //   206: fload #5
    //   208: fstore #7
    //   210: goto -> 329
    //   213: aload #13
    //   215: getfield mType : Landroidx/constraintlayout/core/SolverVariable$Type;
    //   218: getstatic androidx/constraintlayout/core/SolverVariable$Type.SLACK : Landroidx/constraintlayout/core/SolverVariable$Type;
    //   221: if_acmpne -> 265
    //   224: fload #6
    //   226: fstore_3
    //   227: aload #11
    //   229: astore #12
    //   231: aload #10
    //   233: astore #14
    //   235: fload #5
    //   237: fstore #7
    //   239: fload #4
    //   241: fload #5
    //   243: fcmpg
    //   244: ifge -> 329
    //   247: fload #6
    //   249: fstore_3
    //   250: aload #11
    //   252: astore #12
    //   254: aload #13
    //   256: astore #14
    //   258: fload #4
    //   260: fstore #7
    //   262: goto -> 329
    //   265: fload #6
    //   267: fstore_3
    //   268: aload #11
    //   270: astore #12
    //   272: aload #10
    //   274: astore #14
    //   276: fload #5
    //   278: fstore #7
    //   280: aload #13
    //   282: getfield mType : Landroidx/constraintlayout/core/SolverVariable$Type;
    //   285: getstatic androidx/constraintlayout/core/SolverVariable$Type.ERROR : Landroidx/constraintlayout/core/SolverVariable$Type;
    //   288: if_acmpne -> 329
    //   291: fload #6
    //   293: fstore_3
    //   294: aload #11
    //   296: astore #12
    //   298: aload #10
    //   300: astore #14
    //   302: fload #5
    //   304: fstore #7
    //   306: fload #4
    //   308: fload #6
    //   310: fcmpg
    //   311: ifge -> 329
    //   314: fload #4
    //   316: fstore_3
    //   317: aload #13
    //   319: astore #12
    //   321: fload #5
    //   323: fstore #7
    //   325: aload #10
    //   327: astore #14
    //   329: iinc #8, 1
    //   332: fload_3
    //   333: fstore #6
    //   335: aload #12
    //   337: astore #11
    //   339: aload #14
    //   341: astore #10
    //   343: fload #7
    //   345: fstore #5
    //   347: goto -> 26
    //   350: iconst_1
    //   351: ifeq -> 357
    //   354: aload #11
    //   356: areturn
    //   357: aload #11
    //   359: ifnull -> 368
    //   362: aload #11
    //   364: astore_1
    //   365: goto -> 371
    //   368: aload #10
    //   370: astore_1
    //   371: aload_1
    //   372: areturn
  }
  
  public ArrayRow addError(LinearSystem paramLinearSystem, int paramInt) {
    this.variables.put(paramLinearSystem.createErrorVariable(paramInt, "ep"), 1.0F);
    this.variables.put(paramLinearSystem.createErrorVariable(paramInt, "em"), -1.0F);
    return this;
  }
  
  public void addError(SolverVariable paramSolverVariable) {
    float f = 1.0F;
    if (paramSolverVariable.strength == 1) {
      f = 1.0F;
    } else if (paramSolverVariable.strength == 2) {
      f = 1000.0F;
    } else if (paramSolverVariable.strength == 3) {
      f = 1000000.0F;
    } else if (paramSolverVariable.strength == 4) {
      f = 1.0E9F;
    } else if (paramSolverVariable.strength == 5) {
      f = 1.0E12F;
    } 
    this.variables.put(paramSolverVariable, f);
  }
  
  ArrayRow addSingleError(SolverVariable paramSolverVariable, int paramInt) {
    this.variables.put(paramSolverVariable, paramInt);
    return this;
  }
  
  boolean chooseSubject(LinearSystem paramLinearSystem) {
    boolean bool = false;
    SolverVariable solverVariable = chooseSubjectInVariables(paramLinearSystem);
    if (solverVariable == null) {
      bool = true;
    } else {
      pivot(solverVariable);
    } 
    if (this.variables.getCurrentSize() == 0)
      this.isSimpleDefinition = true; 
    return bool;
  }
  
  SolverVariable chooseSubjectInVariables(LinearSystem paramLinearSystem) {
    SolverVariable solverVariable2 = null;
    SolverVariable solverVariable1 = null;
    float f2 = 0.0F;
    float f1 = 0.0F;
    boolean bool1 = false;
    boolean bool = false;
    int i = this.variables.getCurrentSize();
    byte b = 0;
    while (b < i) {
      float f4;
      float f5;
      boolean bool2;
      boolean bool3;
      SolverVariable solverVariable4;
      SolverVariable solverVariable5;
      float f3 = this.variables.getVariableValue(b);
      SolverVariable solverVariable3 = this.variables.getVariable(b);
      if (solverVariable3.mType == SolverVariable.Type.UNRESTRICTED) {
        if (solverVariable1 == null) {
          solverVariable5 = solverVariable3;
          bool2 = isNew(solverVariable3, paramLinearSystem);
          solverVariable4 = solverVariable2;
          f4 = f3;
          f5 = f1;
          bool3 = bool;
        } else if (f2 > f3) {
          solverVariable5 = solverVariable3;
          bool2 = isNew(solverVariable3, paramLinearSystem);
          solverVariable4 = solverVariable2;
          f4 = f3;
          f5 = f1;
          bool3 = bool;
        } else {
          solverVariable4 = solverVariable2;
          solverVariable5 = solverVariable1;
          f4 = f2;
          f5 = f1;
          bool2 = bool1;
          bool3 = bool;
          if (!bool1) {
            solverVariable4 = solverVariable2;
            solverVariable5 = solverVariable1;
            f4 = f2;
            f5 = f1;
            bool2 = bool1;
            bool3 = bool;
            if (isNew(solverVariable3, paramLinearSystem)) {
              bool2 = true;
              solverVariable4 = solverVariable2;
              solverVariable5 = solverVariable3;
              f4 = f3;
              f5 = f1;
              bool3 = bool;
            } 
          } 
        } 
      } else {
        solverVariable4 = solverVariable2;
        solverVariable5 = solverVariable1;
        f4 = f2;
        f5 = f1;
        bool2 = bool1;
        bool3 = bool;
        if (solverVariable1 == null) {
          solverVariable4 = solverVariable2;
          solverVariable5 = solverVariable1;
          f4 = f2;
          f5 = f1;
          bool2 = bool1;
          bool3 = bool;
          if (f3 < 0.0F)
            if (solverVariable2 == null) {
              solverVariable4 = solverVariable3;
              bool3 = isNew(solverVariable3, paramLinearSystem);
              solverVariable5 = solverVariable1;
              f4 = f2;
              f5 = f3;
              bool2 = bool1;
            } else if (f1 > f3) {
              solverVariable4 = solverVariable3;
              bool3 = isNew(solverVariable3, paramLinearSystem);
              solverVariable5 = solverVariable1;
              f4 = f2;
              f5 = f3;
              bool2 = bool1;
            } else {
              solverVariable4 = solverVariable2;
              solverVariable5 = solverVariable1;
              f4 = f2;
              f5 = f1;
              bool2 = bool1;
              bool3 = bool;
              if (!bool) {
                solverVariable4 = solverVariable2;
                solverVariable5 = solverVariable1;
                f4 = f2;
                f5 = f1;
                bool2 = bool1;
                bool3 = bool;
                if (isNew(solverVariable3, paramLinearSystem)) {
                  bool3 = true;
                  bool2 = bool1;
                  f5 = f3;
                  f4 = f2;
                  solverVariable5 = solverVariable1;
                  solverVariable4 = solverVariable3;
                } 
              } 
            }  
        } 
      } 
      b++;
      solverVariable2 = solverVariable4;
      solverVariable1 = solverVariable5;
      f2 = f4;
      f1 = f5;
      bool1 = bool2;
      bool = bool3;
    } 
    return (solverVariable1 != null) ? solverVariable1 : solverVariable2;
  }
  
  public void clear() {
    this.variables.clear();
    this.variable = null;
    this.constantValue = 0.0F;
  }
  
  ArrayRow createRowCentering(SolverVariable paramSolverVariable1, SolverVariable paramSolverVariable2, int paramInt1, float paramFloat, SolverVariable paramSolverVariable3, SolverVariable paramSolverVariable4, int paramInt2) {
    if (paramSolverVariable2 == paramSolverVariable3) {
      this.variables.put(paramSolverVariable1, 1.0F);
      this.variables.put(paramSolverVariable4, 1.0F);
      this.variables.put(paramSolverVariable2, -2.0F);
      return this;
    } 
    if (paramFloat == 0.5F) {
      this.variables.put(paramSolverVariable1, 1.0F);
      this.variables.put(paramSolverVariable2, -1.0F);
      this.variables.put(paramSolverVariable3, -1.0F);
      this.variables.put(paramSolverVariable4, 1.0F);
      if (paramInt1 > 0 || paramInt2 > 0)
        this.constantValue = (-paramInt1 + paramInt2); 
    } else if (paramFloat <= 0.0F) {
      this.variables.put(paramSolverVariable1, -1.0F);
      this.variables.put(paramSolverVariable2, 1.0F);
      this.constantValue = paramInt1;
    } else if (paramFloat >= 1.0F) {
      this.variables.put(paramSolverVariable4, -1.0F);
      this.variables.put(paramSolverVariable3, 1.0F);
      this.constantValue = -paramInt2;
    } else {
      this.variables.put(paramSolverVariable1, (1.0F - paramFloat) * 1.0F);
      this.variables.put(paramSolverVariable2, (1.0F - paramFloat) * -1.0F);
      this.variables.put(paramSolverVariable3, -1.0F * paramFloat);
      this.variables.put(paramSolverVariable4, paramFloat * 1.0F);
      if (paramInt1 > 0 || paramInt2 > 0)
        this.constantValue = -paramInt1 * (1.0F - paramFloat) + paramInt2 * paramFloat; 
    } 
    return this;
  }
  
  ArrayRow createRowDefinition(SolverVariable paramSolverVariable, int paramInt) {
    this.variable = paramSolverVariable;
    paramSolverVariable.computedValue = paramInt;
    this.constantValue = paramInt;
    this.isSimpleDefinition = true;
    return this;
  }
  
  ArrayRow createRowDimensionPercent(SolverVariable paramSolverVariable1, SolverVariable paramSolverVariable2, float paramFloat) {
    this.variables.put(paramSolverVariable1, -1.0F);
    this.variables.put(paramSolverVariable2, paramFloat);
    return this;
  }
  
  public ArrayRow createRowDimensionRatio(SolverVariable paramSolverVariable1, SolverVariable paramSolverVariable2, SolverVariable paramSolverVariable3, SolverVariable paramSolverVariable4, float paramFloat) {
    this.variables.put(paramSolverVariable1, -1.0F);
    this.variables.put(paramSolverVariable2, 1.0F);
    this.variables.put(paramSolverVariable3, paramFloat);
    this.variables.put(paramSolverVariable4, -paramFloat);
    return this;
  }
  
  public ArrayRow createRowEqualDimension(float paramFloat1, float paramFloat2, float paramFloat3, SolverVariable paramSolverVariable1, int paramInt1, SolverVariable paramSolverVariable2, int paramInt2, SolverVariable paramSolverVariable3, int paramInt3, SolverVariable paramSolverVariable4, int paramInt4) {
    if (paramFloat2 == 0.0F || paramFloat1 == paramFloat3) {
      this.constantValue = (-paramInt1 - paramInt2 + paramInt3 + paramInt4);
      this.variables.put(paramSolverVariable1, 1.0F);
      this.variables.put(paramSolverVariable2, -1.0F);
      this.variables.put(paramSolverVariable4, 1.0F);
      this.variables.put(paramSolverVariable3, -1.0F);
      return this;
    } 
    paramFloat1 = paramFloat1 / paramFloat2 / paramFloat3 / paramFloat2;
    this.constantValue = (-paramInt1 - paramInt2) + paramInt3 * paramFloat1 + paramInt4 * paramFloat1;
    this.variables.put(paramSolverVariable1, 1.0F);
    this.variables.put(paramSolverVariable2, -1.0F);
    this.variables.put(paramSolverVariable4, paramFloat1);
    this.variables.put(paramSolverVariable3, -paramFloat1);
    return this;
  }
  
  public ArrayRow createRowEqualMatchDimensions(float paramFloat1, float paramFloat2, float paramFloat3, SolverVariable paramSolverVariable1, SolverVariable paramSolverVariable2, SolverVariable paramSolverVariable3, SolverVariable paramSolverVariable4) {
    this.constantValue = 0.0F;
    if (paramFloat2 == 0.0F || paramFloat1 == paramFloat3) {
      this.variables.put(paramSolverVariable1, 1.0F);
      this.variables.put(paramSolverVariable2, -1.0F);
      this.variables.put(paramSolverVariable4, 1.0F);
      this.variables.put(paramSolverVariable3, -1.0F);
      return this;
    } 
    if (paramFloat1 == 0.0F) {
      this.variables.put(paramSolverVariable1, 1.0F);
      this.variables.put(paramSolverVariable2, -1.0F);
    } else if (paramFloat3 == 0.0F) {
      this.variables.put(paramSolverVariable3, 1.0F);
      this.variables.put(paramSolverVariable4, -1.0F);
    } else {
      paramFloat1 = paramFloat1 / paramFloat2 / paramFloat3 / paramFloat2;
      this.variables.put(paramSolverVariable1, 1.0F);
      this.variables.put(paramSolverVariable2, -1.0F);
      this.variables.put(paramSolverVariable4, paramFloat1);
      this.variables.put(paramSolverVariable3, -paramFloat1);
    } 
    return this;
  }
  
  public ArrayRow createRowEquals(SolverVariable paramSolverVariable, int paramInt) {
    if (paramInt < 0) {
      this.constantValue = (paramInt * -1);
      this.variables.put(paramSolverVariable, 1.0F);
    } else {
      this.constantValue = paramInt;
      this.variables.put(paramSolverVariable, -1.0F);
    } 
    return this;
  }
  
  public ArrayRow createRowEquals(SolverVariable paramSolverVariable1, SolverVariable paramSolverVariable2, int paramInt) {
    int i = 0;
    boolean bool = false;
    if (paramInt != 0) {
      int j = paramInt;
      paramInt = bool;
      i = j;
      if (j < 0) {
        i = j * -1;
        paramInt = 1;
      } 
      this.constantValue = i;
      i = paramInt;
    } 
    if (i == 0) {
      this.variables.put(paramSolverVariable1, -1.0F);
      this.variables.put(paramSolverVariable2, 1.0F);
    } else {
      this.variables.put(paramSolverVariable1, 1.0F);
      this.variables.put(paramSolverVariable2, -1.0F);
    } 
    return this;
  }
  
  public ArrayRow createRowGreaterThan(SolverVariable paramSolverVariable1, int paramInt, SolverVariable paramSolverVariable2) {
    this.constantValue = paramInt;
    this.variables.put(paramSolverVariable1, -1.0F);
    return this;
  }
  
  public ArrayRow createRowGreaterThan(SolverVariable paramSolverVariable1, SolverVariable paramSolverVariable2, SolverVariable paramSolverVariable3, int paramInt) {
    int i = 0;
    boolean bool = false;
    if (paramInt != 0) {
      int j = paramInt;
      paramInt = bool;
      i = j;
      if (j < 0) {
        i = j * -1;
        paramInt = 1;
      } 
      this.constantValue = i;
      i = paramInt;
    } 
    if (i == 0) {
      this.variables.put(paramSolverVariable1, -1.0F);
      this.variables.put(paramSolverVariable2, 1.0F);
      this.variables.put(paramSolverVariable3, 1.0F);
    } else {
      this.variables.put(paramSolverVariable1, 1.0F);
      this.variables.put(paramSolverVariable2, -1.0F);
      this.variables.put(paramSolverVariable3, -1.0F);
    } 
    return this;
  }
  
  public ArrayRow createRowLowerThan(SolverVariable paramSolverVariable1, SolverVariable paramSolverVariable2, SolverVariable paramSolverVariable3, int paramInt) {
    int i = 0;
    boolean bool = false;
    if (paramInt != 0) {
      int j = paramInt;
      paramInt = bool;
      i = j;
      if (j < 0) {
        i = j * -1;
        paramInt = 1;
      } 
      this.constantValue = i;
      i = paramInt;
    } 
    if (i == 0) {
      this.variables.put(paramSolverVariable1, -1.0F);
      this.variables.put(paramSolverVariable2, 1.0F);
      this.variables.put(paramSolverVariable3, -1.0F);
    } else {
      this.variables.put(paramSolverVariable1, 1.0F);
      this.variables.put(paramSolverVariable2, -1.0F);
      this.variables.put(paramSolverVariable3, 1.0F);
    } 
    return this;
  }
  
  public ArrayRow createRowWithAngle(SolverVariable paramSolverVariable1, SolverVariable paramSolverVariable2, SolverVariable paramSolverVariable3, SolverVariable paramSolverVariable4, float paramFloat) {
    this.variables.put(paramSolverVariable3, 0.5F);
    this.variables.put(paramSolverVariable4, 0.5F);
    this.variables.put(paramSolverVariable1, -0.5F);
    this.variables.put(paramSolverVariable2, -0.5F);
    this.constantValue = -paramFloat;
    return this;
  }
  
  void ensurePositiveConstant() {
    float f = this.constantValue;
    if (f < 0.0F) {
      this.constantValue = f * -1.0F;
      this.variables.invert();
    } 
  }
  
  public SolverVariable getKey() {
    return this.variable;
  }
  
  public SolverVariable getPivotCandidate(LinearSystem paramLinearSystem, boolean[] paramArrayOfboolean) {
    return pickPivotInVariables(paramArrayOfboolean, null);
  }
  
  boolean hasKeyVariable() {
    boolean bool;
    SolverVariable solverVariable = this.variable;
    if (solverVariable != null && (solverVariable.mType == SolverVariable.Type.UNRESTRICTED || this.constantValue >= 0.0F)) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  boolean hasVariable(SolverVariable paramSolverVariable) {
    return this.variables.contains(paramSolverVariable);
  }
  
  public void initFromRow(LinearSystem.Row paramRow) {
    if (paramRow instanceof ArrayRow) {
      paramRow = paramRow;
      this.variable = null;
      this.variables.clear();
      for (byte b = 0; b < ((ArrayRow)paramRow).variables.getCurrentSize(); b++) {
        SolverVariable solverVariable = ((ArrayRow)paramRow).variables.getVariable(b);
        float f = ((ArrayRow)paramRow).variables.getVariableValue(b);
        this.variables.add(solverVariable, f, true);
      } 
    } 
  }
  
  public boolean isEmpty() {
    boolean bool;
    if (this.variable == null && this.constantValue == 0.0F && this.variables.getCurrentSize() == 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public SolverVariable pickPivot(SolverVariable paramSolverVariable) {
    return pickPivotInVariables(null, paramSolverVariable);
  }
  
  void pivot(SolverVariable paramSolverVariable) {
    SolverVariable solverVariable = this.variable;
    if (solverVariable != null) {
      this.variables.put(solverVariable, -1.0F);
      this.variable.definitionId = -1;
      this.variable = null;
    } 
    float f = this.variables.remove(paramSolverVariable, true) * -1.0F;
    this.variable = paramSolverVariable;
    if (f == 1.0F)
      return; 
    this.constantValue /= f;
    this.variables.divideByAmount(f);
  }
  
  public void reset() {
    this.variable = null;
    this.variables.clear();
    this.constantValue = 0.0F;
    this.isSimpleDefinition = false;
  }
  
  int sizeInBytes() {
    int i = 0;
    if (this.variable != null)
      i = 0 + 4; 
    return i + 4 + 4 + this.variables.sizeInBytes();
  }
  
  String toReadableString() {
    if (this.variable == null) {
      str1 = "" + "0";
    } else {
      str1 = "" + this.variable;
    } 
    String str2 = str1 + " = ";
    boolean bool = false;
    String str1 = str2;
    if (this.constantValue != 0.0F) {
      str1 = str2 + this.constantValue;
      bool = true;
    } 
    int i = this.variables.getCurrentSize();
    for (byte b = 0; b < i; b++) {
      SolverVariable solverVariable = this.variables.getVariable(b);
      if (solverVariable != null) {
        float f = this.variables.getVariableValue(b);
        if (f != 0.0F) {
          float f1;
          String str3;
          String str4 = solverVariable.toString();
          if (!bool) {
            str3 = str1;
            f1 = f;
            if (f < 0.0F) {
              str3 = str1 + "- ";
              f1 = f * -1.0F;
            } 
          } else if (f > 0.0F) {
            str3 = str1 + " + ";
            f1 = f;
          } else {
            str3 = str1 + " - ";
            f1 = f * -1.0F;
          } 
          if (f1 == 1.0F) {
            str1 = str3 + str4;
          } else {
            str1 = str3 + f1 + " " + str4;
          } 
          bool = true;
        } 
      } 
    } 
    str2 = str1;
    if (!bool)
      str2 = str1 + "0.0"; 
    return str2;
  }
  
  public String toString() {
    return toReadableString();
  }
  
  public void updateFromFinalVariable(LinearSystem paramLinearSystem, SolverVariable paramSolverVariable, boolean paramBoolean) {
    if (paramSolverVariable == null || !paramSolverVariable.isFinalValue)
      return; 
    float f = this.variables.get(paramSolverVariable);
    this.constantValue += paramSolverVariable.computedValue * f;
    this.variables.remove(paramSolverVariable, paramBoolean);
    if (paramBoolean)
      paramSolverVariable.removeFromRow(this); 
    if (LinearSystem.SIMPLIFY_SYNONYMS && this.variables.getCurrentSize() == 0) {
      this.isSimpleDefinition = true;
      paramLinearSystem.hasSimpleDefinition = true;
    } 
  }
  
  public void updateFromRow(LinearSystem paramLinearSystem, ArrayRow paramArrayRow, boolean paramBoolean) {
    float f = this.variables.use(paramArrayRow, paramBoolean);
    this.constantValue += paramArrayRow.constantValue * f;
    if (paramBoolean)
      paramArrayRow.variable.removeFromRow(this); 
    if (LinearSystem.SIMPLIFY_SYNONYMS && this.variable != null && this.variables.getCurrentSize() == 0) {
      this.isSimpleDefinition = true;
      paramLinearSystem.hasSimpleDefinition = true;
    } 
  }
  
  public void updateFromSynonymVariable(LinearSystem paramLinearSystem, SolverVariable paramSolverVariable, boolean paramBoolean) {
    if (paramSolverVariable == null || !paramSolverVariable.isSynonym)
      return; 
    float f = this.variables.get(paramSolverVariable);
    this.constantValue += paramSolverVariable.synonymDelta * f;
    this.variables.remove(paramSolverVariable, paramBoolean);
    if (paramBoolean)
      paramSolverVariable.removeFromRow(this); 
    this.variables.add(paramLinearSystem.mCache.mIndexedVariables[paramSolverVariable.synonym], f, paramBoolean);
    if (LinearSystem.SIMPLIFY_SYNONYMS && this.variables.getCurrentSize() == 0) {
      this.isSimpleDefinition = true;
      paramLinearSystem.hasSimpleDefinition = true;
    } 
  }
  
  public void updateFromSystem(LinearSystem paramLinearSystem) {
    if (paramLinearSystem.mRows.length == 0)
      return; 
    for (boolean bool = false; !bool; bool = true) {
      int i = this.variables.getCurrentSize();
      byte b;
      for (b = 0; b < i; b++) {
        SolverVariable solverVariable = this.variables.getVariable(b);
        if (solverVariable.definitionId != -1 || solverVariable.isFinalValue || solverVariable.isSynonym)
          this.variablesToUpdate.add(solverVariable); 
      } 
      i = this.variablesToUpdate.size();
      if (i > 0) {
        for (b = 0; b < i; b++) {
          SolverVariable solverVariable = this.variablesToUpdate.get(b);
          if (solverVariable.isFinalValue) {
            updateFromFinalVariable(paramLinearSystem, solverVariable, true);
          } else if (solverVariable.isSynonym) {
            updateFromSynonymVariable(paramLinearSystem, solverVariable, true);
          } else {
            updateFromRow(paramLinearSystem, paramLinearSystem.mRows[solverVariable.definitionId], true);
          } 
        } 
        this.variablesToUpdate.clear();
        continue;
      } 
    } 
    if (LinearSystem.SIMPLIFY_SYNONYMS && this.variable != null && this.variables.getCurrentSize() == 0) {
      this.isSimpleDefinition = true;
      paramLinearSystem.hasSimpleDefinition = true;
    } 
  }
  
  public static interface ArrayRowVariables {
    void add(SolverVariable param1SolverVariable, float param1Float, boolean param1Boolean);
    
    void clear();
    
    boolean contains(SolverVariable param1SolverVariable);
    
    void display();
    
    void divideByAmount(float param1Float);
    
    float get(SolverVariable param1SolverVariable);
    
    int getCurrentSize();
    
    SolverVariable getVariable(int param1Int);
    
    float getVariableValue(int param1Int);
    
    int indexOf(SolverVariable param1SolverVariable);
    
    void invert();
    
    void put(SolverVariable param1SolverVariable, float param1Float);
    
    float remove(SolverVariable param1SolverVariable, boolean param1Boolean);
    
    int sizeInBytes();
    
    float use(ArrayRow param1ArrayRow, boolean param1Boolean);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\ArrayRow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */