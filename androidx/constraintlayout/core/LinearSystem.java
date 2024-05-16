package androidx.constraintlayout.core;

import androidx.constraintlayout.core.widgets.ConstraintAnchor;
import androidx.constraintlayout.core.widgets.ConstraintWidget;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashMap;

public class LinearSystem {
  public static long ARRAY_ROW_CREATION = 0L;
  
  public static final boolean DEBUG = false;
  
  private static final boolean DEBUG_CONSTRAINTS = false;
  
  public static final boolean FULL_DEBUG = false;
  
  public static final boolean MEASURE = false;
  
  public static long OPTIMIZED_ARRAY_ROW_CREATION;
  
  public static boolean OPTIMIZED_ENGINE;
  
  private static int POOL_SIZE;
  
  public static boolean SIMPLIFY_SYNONYMS;
  
  public static boolean SKIP_COLUMNS;
  
  public static boolean USE_BASIC_SYNONYMS;
  
  public static boolean USE_DEPENDENCY_ORDERING = false;
  
  public static boolean USE_SYNONYMS;
  
  public static Metrics sMetrics;
  
  private int TABLE_SIZE = 32;
  
  public boolean graphOptimizer = false;
  
  public boolean hasSimpleDefinition = false;
  
  private boolean[] mAlreadyTestedCandidates = new boolean[32];
  
  final Cache mCache;
  
  private Row mGoal;
  
  private int mMaxColumns = 32;
  
  private int mMaxRows = 32;
  
  int mNumColumns = 1;
  
  int mNumRows = 0;
  
  private SolverVariable[] mPoolVariables = new SolverVariable[POOL_SIZE];
  
  private int mPoolVariablesCount = 0;
  
  ArrayRow[] mRows = null;
  
  private Row mTempGoal;
  
  private HashMap<String, SolverVariable> mVariables = null;
  
  int mVariablesID = 0;
  
  public boolean newgraphOptimizer = false;
  
  static {
    USE_BASIC_SYNONYMS = true;
    SIMPLIFY_SYNONYMS = true;
    USE_SYNONYMS = true;
    SKIP_COLUMNS = true;
    OPTIMIZED_ENGINE = false;
    POOL_SIZE = 1000;
    ARRAY_ROW_CREATION = 0L;
    OPTIMIZED_ARRAY_ROW_CREATION = 0L;
  }
  
  public LinearSystem() {
    this.mRows = new ArrayRow[32];
    releaseRows();
    Cache cache = new Cache();
    this.mCache = cache;
    this.mGoal = new PriorityGoalRow(cache);
    if (OPTIMIZED_ENGINE) {
      this.mTempGoal = new ValuesRow(cache);
    } else {
      this.mTempGoal = new ArrayRow(cache);
    } 
  }
  
  private SolverVariable acquireSolverVariable(SolverVariable.Type paramType, String paramString) {
    SolverVariable solverVariable1;
    SolverVariable solverVariable2 = this.mCache.solverVariablePool.acquire();
    if (solverVariable2 == null) {
      solverVariable2 = new SolverVariable(paramType, paramString);
      solverVariable2.setType(paramType, paramString);
      solverVariable1 = solverVariable2;
    } else {
      solverVariable2.reset();
      solverVariable2.setType((SolverVariable.Type)solverVariable1, paramString);
      solverVariable1 = solverVariable2;
    } 
    int j = this.mPoolVariablesCount;
    int i = POOL_SIZE;
    if (j >= i) {
      i *= 2;
      POOL_SIZE = i;
      this.mPoolVariables = Arrays.<SolverVariable>copyOf(this.mPoolVariables, i);
    } 
    SolverVariable[] arrayOfSolverVariable = this.mPoolVariables;
    i = this.mPoolVariablesCount;
    this.mPoolVariablesCount = i + 1;
    arrayOfSolverVariable[i] = solverVariable1;
    return solverVariable1;
  }
  
  private void addError(ArrayRow paramArrayRow) {
    paramArrayRow.addError(this, 0);
  }
  
  private final void addRow(ArrayRow paramArrayRow) {
    if (SIMPLIFY_SYNONYMS && paramArrayRow.isSimpleDefinition) {
      paramArrayRow.variable.setFinalValue(this, paramArrayRow.constantValue);
    } else {
      this.mRows[this.mNumRows] = paramArrayRow;
      paramArrayRow.variable.definitionId = this.mNumRows;
      this.mNumRows++;
      paramArrayRow.variable.updateReferencesWithNewDefinition(this, paramArrayRow);
    } 
    if (SIMPLIFY_SYNONYMS && this.hasSimpleDefinition) {
      for (int i = 0; i < this.mNumRows; i = j + 1) {
        if (this.mRows[i] == null)
          System.out.println("WTF"); 
        ArrayRow[] arrayOfArrayRow = this.mRows;
        int j = i;
        if (arrayOfArrayRow[i] != null) {
          j = i;
          if ((arrayOfArrayRow[i]).isSimpleDefinition) {
            ArrayRow arrayRow = this.mRows[i];
            arrayRow.variable.setFinalValue(this, arrayRow.constantValue);
            if (OPTIMIZED_ENGINE) {
              this.mCache.optimizedArrayRowPool.release(arrayRow);
            } else {
              this.mCache.arrayRowPool.release(arrayRow);
            } 
            this.mRows[i] = null;
            int k = i + 1;
            j = i + 1;
            while (true) {
              int m = this.mNumRows;
              if (j < m) {
                ArrayRow[] arrayOfArrayRow1 = this.mRows;
                arrayOfArrayRow1[j - 1] = arrayOfArrayRow1[j];
                if ((arrayOfArrayRow1[j - 1]).variable.definitionId == j)
                  (this.mRows[j - 1]).variable.definitionId = j - 1; 
                k = j;
                j++;
                continue;
              } 
              if (k < m)
                this.mRows[k] = null; 
              this.mNumRows = m - 1;
              j = i - 1;
              break;
            } 
          } 
        } 
      } 
      this.hasSimpleDefinition = false;
    } 
  }
  
  private void addSingleError(ArrayRow paramArrayRow, int paramInt) {
    addSingleError(paramArrayRow, paramInt, 0);
  }
  
  private void computeValues() {
    for (byte b = 0; b < this.mNumRows; b++) {
      ArrayRow arrayRow = this.mRows[b];
      arrayRow.variable.computedValue = arrayRow.constantValue;
    } 
  }
  
  public static ArrayRow createRowDimensionPercent(LinearSystem paramLinearSystem, SolverVariable paramSolverVariable1, SolverVariable paramSolverVariable2, float paramFloat) {
    return paramLinearSystem.createRow().createRowDimensionPercent(paramSolverVariable1, paramSolverVariable2, paramFloat);
  }
  
  private SolverVariable createVariable(String paramString, SolverVariable.Type paramType) {
    Metrics metrics = sMetrics;
    if (metrics != null)
      metrics.variables++; 
    if (this.mNumColumns + 1 >= this.mMaxColumns)
      increaseTableSize(); 
    SolverVariable solverVariable = acquireSolverVariable(paramType, null);
    solverVariable.setName(paramString);
    int i = this.mVariablesID + 1;
    this.mVariablesID = i;
    this.mNumColumns++;
    solverVariable.id = i;
    if (this.mVariables == null)
      this.mVariables = new HashMap<>(); 
    this.mVariables.put(paramString, solverVariable);
    this.mCache.mIndexedVariables[this.mVariablesID] = solverVariable;
    return solverVariable;
  }
  
  private void displayRows() {
    displaySolverVariables();
    String str = "";
    for (byte b = 0; b < this.mNumRows; b++) {
      str = str + this.mRows[b];
      str = str + "\n";
    } 
    str = str + this.mGoal + "\n";
    System.out.println(str);
  }
  
  private void displaySolverVariables() {
    String str = "Display Rows (" + this.mNumRows + "x" + this.mNumColumns + ")\n";
    System.out.println(str);
  }
  
  private int enforceBFS(Row paramRow) throws Exception {
    byte b1;
    byte b2 = 0;
    byte b3 = 0;
    int i = 0;
    while (true) {
      b1 = b3;
      if (i < this.mNumRows) {
        if ((this.mRows[i]).variable.mType != SolverVariable.Type.UNRESTRICTED && (this.mRows[i]).constantValue < 0.0F) {
          b1 = 1;
          break;
        } 
        i++;
        continue;
      } 
      break;
    } 
    if (b1) {
      boolean bool = false;
      for (i = 0; !bool; i = j) {
        Object object;
        Metrics metrics = sMetrics;
        if (metrics != null)
          metrics.bfs++; 
        int j = i + 1;
        float f = Float.MAX_VALUE;
        b2 = 0;
        b3 = -1;
        byte b = -1;
        i = 0;
        while (i < this.mNumRows) {
          float f1;
          byte b4;
          byte b5;
          Object object1;
          ArrayRow arrayRow = this.mRows[i];
          if (arrayRow.variable.mType == SolverVariable.Type.UNRESTRICTED) {
            f1 = f;
            b4 = b2;
            b5 = b3;
            byte b6 = b;
          } else {
            byte b6;
            if (arrayRow.isSimpleDefinition) {
              f1 = f;
              b4 = b2;
              b5 = b3;
              b6 = b;
            } else {
              Object object2;
              if (arrayRow.constantValue < 0.0F) {
                if (SKIP_COLUMNS) {
                  Object object3;
                  byte b7;
                  byte b8;
                  int k = arrayRow.variables.getCurrentSize();
                  b5 = 0;
                  while (b5 < k) {
                    SolverVariable solverVariable = arrayRow.variables.getVariable(b5);
                    float f2 = arrayRow.variables.get(solverVariable);
                    if (f2 <= 0.0F) {
                      f1 = f;
                      Object object6 = object3;
                      b6 = b3;
                      Object object5 = object2;
                      b4 = b1;
                      continue;
                    } 
                    b4 = 0;
                    byte b9 = b3;
                    Object object4 = object3;
                    b7 = b4;
                    while (true) {
                      f1 = f;
                      byte b10 = b8;
                      b6 = b9;
                      Object object5 = object2;
                      b4 = b1;
                      b7++;
                      b8 = b4;
                    } 
                    continue;
                    b5++;
                    b1 = b4;
                    f = f1;
                    object3 = SYNTHETIC_LOCAL_VARIABLE_16;
                    b3 = b6;
                    object2 = SYNTHETIC_LOCAL_VARIABLE_15;
                  } 
                  f1 = f;
                  b4 = b7;
                  b5 = b8;
                  object1 = object2;
                } else {
                  byte b8 = b1;
                  byte b7 = 1;
                  while (true) {
                    f1 = f;
                    b4 = b2;
                    b5 = b3;
                    object1 = object2;
                    b1 = b8;
                    if (b7 < this.mNumColumns) {
                      SolverVariable solverVariable = this.mCache.mIndexedVariables[b7];
                      float f2 = arrayRow.variables.get(solverVariable);
                      if (f2 <= 0.0F) {
                        f1 = f;
                        b5 = b2;
                        b4 = b3;
                        object1 = object2;
                        continue;
                      } 
                      b1 = 0;
                      while (true) {
                        f1 = f;
                        b5 = b2;
                        b4 = b3;
                        object1 = object2;
                        b1++;
                        b2 = b4;
                      } 
                      continue;
                    } 
                    break;
                    b7++;
                    f = f1;
                    b2 = b5;
                    b3 = b4;
                    object2 = object1;
                  } 
                } 
              } else {
                object1 = object2;
                b5 = b3;
                b4 = b2;
                f1 = f;
              } 
            } 
          } 
          i++;
          f = f1;
          b2 = b4;
          b3 = b5;
          object = object1;
        } 
        if (b3 != -1) {
          ArrayRow arrayRow = this.mRows[b3];
          arrayRow.variable.definitionId = -1;
          metrics = sMetrics;
          if (metrics != null)
            metrics.pivots++; 
          arrayRow.pivot(this.mCache.mIndexedVariables[object]);
          arrayRow.variable.definitionId = b3;
          arrayRow.variable.updateReferencesWithNewDefinition(this, arrayRow);
        } else {
          bool = true;
        } 
        if (j > this.mNumColumns / 2)
          bool = true; 
      } 
    } else {
      i = b2;
    } 
    return i;
  }
  
  private String getDisplaySize(int paramInt) {
    int i = paramInt * 4 / 1024 / 1024;
    if (i > 0)
      return "" + i + " Mb"; 
    i = paramInt * 4 / 1024;
    return (i > 0) ? ("" + i + " Kb") : ("" + (paramInt * 4) + " bytes");
  }
  
  private String getDisplayStrength(int paramInt) {
    return (paramInt == 1) ? "LOW" : ((paramInt == 2) ? "MEDIUM" : ((paramInt == 3) ? "HIGH" : ((paramInt == 4) ? "HIGHEST" : ((paramInt == 5) ? "EQUALITY" : ((paramInt == 8) ? "FIXED" : ((paramInt == 6) ? "BARRIER" : "NONE"))))));
  }
  
  public static Metrics getMetrics() {
    return sMetrics;
  }
  
  private void increaseTableSize() {
    int i = this.TABLE_SIZE * 2;
    this.TABLE_SIZE = i;
    this.mRows = Arrays.<ArrayRow>copyOf(this.mRows, i);
    Cache cache = this.mCache;
    cache.mIndexedVariables = Arrays.<SolverVariable>copyOf(cache.mIndexedVariables, this.TABLE_SIZE);
    i = this.TABLE_SIZE;
    this.mAlreadyTestedCandidates = new boolean[i];
    this.mMaxColumns = i;
    this.mMaxRows = i;
    Metrics metrics = sMetrics;
    if (metrics != null) {
      metrics.tableSizeIncrease++;
      metrics = sMetrics;
      metrics.maxTableSize = Math.max(metrics.maxTableSize, this.TABLE_SIZE);
      metrics = sMetrics;
      metrics.lastTableSize = metrics.maxTableSize;
    } 
  }
  
  private final int optimize(Row paramRow, boolean paramBoolean) {
    int i;
    int j;
    Metrics metrics = sMetrics;
    if (metrics != null)
      metrics.optimize++; 
    int m = 0;
    int k = 0;
    byte b = 0;
    while (true) {
      i = m;
      j = k;
      if (b < this.mNumColumns) {
        this.mAlreadyTestedCandidates[b] = false;
        b++;
        continue;
      } 
      break;
    } 
    while (!i) {
      metrics = sMetrics;
      if (metrics != null)
        metrics.iterations++; 
      m = j + 1;
      if (m >= this.mNumColumns * 2)
        return m; 
      if (paramRow.getKey() != null)
        this.mAlreadyTestedCandidates[(paramRow.getKey()).id] = true; 
      SolverVariable solverVariable = paramRow.getPivotCandidate(this, this.mAlreadyTestedCandidates);
      if (solverVariable != null) {
        if (this.mAlreadyTestedCandidates[solverVariable.id])
          return m; 
        this.mAlreadyTestedCandidates[solverVariable.id] = true;
      } 
      if (solverVariable != null) {
        float f = Float.MAX_VALUE;
        k = -1;
        b = 0;
        while (b < this.mNumRows) {
          float f1;
          ArrayRow arrayRow = this.mRows[b];
          if (arrayRow.variable.mType == SolverVariable.Type.UNRESTRICTED) {
            f1 = f;
            j = k;
          } else if (arrayRow.isSimpleDefinition) {
            f1 = f;
            j = k;
          } else {
            f1 = f;
            j = k;
            if (arrayRow.hasVariable(solverVariable)) {
              float f2 = arrayRow.variables.get(solverVariable);
              f1 = f;
              j = k;
              if (f2 < 0.0F) {
                f2 = -arrayRow.constantValue / f2;
                f1 = f;
                j = k;
                if (f2 < f) {
                  f1 = f2;
                  j = b;
                } 
              } 
            } 
          } 
          b++;
          f = f1;
          k = j;
        } 
        if (k > -1) {
          ArrayRow arrayRow = this.mRows[k];
          arrayRow.variable.definitionId = -1;
          Metrics metrics1 = sMetrics;
          if (metrics1 != null)
            metrics1.pivots++; 
          arrayRow.pivot(solverVariable);
          arrayRow.variable.definitionId = k;
          arrayRow.variable.updateReferencesWithNewDefinition(this, arrayRow);
        } 
      } else {
        i = 1;
      } 
      j = m;
    } 
    return j;
  }
  
  private void releaseRows() {
    if (OPTIMIZED_ENGINE) {
      for (byte b = 0; b < this.mNumRows; b++) {
        ArrayRow arrayRow = this.mRows[b];
        if (arrayRow != null)
          this.mCache.optimizedArrayRowPool.release(arrayRow); 
        this.mRows[b] = null;
      } 
    } else {
      for (byte b = 0; b < this.mNumRows; b++) {
        ArrayRow arrayRow = this.mRows[b];
        if (arrayRow != null)
          this.mCache.arrayRowPool.release(arrayRow); 
        this.mRows[b] = null;
      } 
    } 
  }
  
  public void addCenterPoint(ConstraintWidget paramConstraintWidget1, ConstraintWidget paramConstraintWidget2, float paramFloat, int paramInt) {
    SolverVariable solverVariable3 = createObjectVariable(paramConstraintWidget1.getAnchor(ConstraintAnchor.Type.LEFT));
    SolverVariable solverVariable5 = createObjectVariable(paramConstraintWidget1.getAnchor(ConstraintAnchor.Type.TOP));
    SolverVariable solverVariable4 = createObjectVariable(paramConstraintWidget1.getAnchor(ConstraintAnchor.Type.RIGHT));
    SolverVariable solverVariable7 = createObjectVariable(paramConstraintWidget1.getAnchor(ConstraintAnchor.Type.BOTTOM));
    SolverVariable solverVariable1 = createObjectVariable(paramConstraintWidget2.getAnchor(ConstraintAnchor.Type.LEFT));
    SolverVariable solverVariable8 = createObjectVariable(paramConstraintWidget2.getAnchor(ConstraintAnchor.Type.TOP));
    SolverVariable solverVariable6 = createObjectVariable(paramConstraintWidget2.getAnchor(ConstraintAnchor.Type.RIGHT));
    SolverVariable solverVariable2 = createObjectVariable(paramConstraintWidget2.getAnchor(ConstraintAnchor.Type.BOTTOM));
    ArrayRow arrayRow2 = createRow();
    arrayRow2.createRowWithAngle(solverVariable5, solverVariable7, solverVariable8, solverVariable2, (float)(Math.sin(paramFloat) * paramInt));
    addConstraint(arrayRow2);
    ArrayRow arrayRow1 = createRow();
    arrayRow1.createRowWithAngle(solverVariable3, solverVariable4, solverVariable1, solverVariable6, (float)(Math.cos(paramFloat) * paramInt));
    addConstraint(arrayRow1);
  }
  
  public void addCentering(SolverVariable paramSolverVariable1, SolverVariable paramSolverVariable2, int paramInt1, float paramFloat, SolverVariable paramSolverVariable3, SolverVariable paramSolverVariable4, int paramInt2, int paramInt3) {
    ArrayRow arrayRow = createRow();
    arrayRow.createRowCentering(paramSolverVariable1, paramSolverVariable2, paramInt1, paramFloat, paramSolverVariable3, paramSolverVariable4, paramInt2);
    if (paramInt3 != 8)
      arrayRow.addError(this, paramInt3); 
    addConstraint(arrayRow);
  }
  
  public void addConstraint(ArrayRow paramArrayRow) {
    if (paramArrayRow == null)
      return; 
    Metrics metrics = sMetrics;
    if (metrics != null) {
      metrics.constraints++;
      if (paramArrayRow.isSimpleDefinition) {
        metrics = sMetrics;
        metrics.simpleconstraints++;
      } 
    } 
    if (this.mNumRows + 1 >= this.mMaxRows || this.mNumColumns + 1 >= this.mMaxColumns)
      increaseTableSize(); 
    boolean bool1 = false;
    boolean bool2 = false;
    if (!paramArrayRow.isSimpleDefinition) {
      paramArrayRow.updateFromSystem(this);
      if (paramArrayRow.isEmpty())
        return; 
      paramArrayRow.ensurePositiveConstant();
      bool1 = bool2;
      if (paramArrayRow.chooseSubject(this)) {
        SolverVariable solverVariable = createExtraVariable();
        paramArrayRow.variable = solverVariable;
        int i = this.mNumRows;
        addRow(paramArrayRow);
        bool1 = bool2;
        if (this.mNumRows == i + 1) {
          bool2 = true;
          this.mTempGoal.initFromRow(paramArrayRow);
          optimize(this.mTempGoal, true);
          bool1 = bool2;
          if (solverVariable.definitionId == -1) {
            if (paramArrayRow.variable == solverVariable) {
              SolverVariable solverVariable1 = paramArrayRow.pickPivot(solverVariable);
              if (solverVariable1 != null) {
                Metrics metrics1 = sMetrics;
                if (metrics1 != null)
                  metrics1.pivots++; 
                paramArrayRow.pivot(solverVariable1);
              } 
            } 
            if (!paramArrayRow.isSimpleDefinition)
              paramArrayRow.variable.updateReferencesWithNewDefinition(this, paramArrayRow); 
            if (OPTIMIZED_ENGINE) {
              this.mCache.optimizedArrayRowPool.release(paramArrayRow);
            } else {
              this.mCache.arrayRowPool.release(paramArrayRow);
            } 
            this.mNumRows--;
            bool1 = bool2;
          } 
        } 
      } 
      if (!paramArrayRow.hasKeyVariable())
        return; 
    } 
    if (!bool1)
      addRow(paramArrayRow); 
  }
  
  public ArrayRow addEquality(SolverVariable paramSolverVariable1, SolverVariable paramSolverVariable2, int paramInt1, int paramInt2) {
    if (USE_BASIC_SYNONYMS && paramInt2 == 8 && paramSolverVariable2.isFinalValue && paramSolverVariable1.definitionId == -1) {
      paramSolverVariable1.setFinalValue(this, paramSolverVariable2.computedValue + paramInt1);
      return null;
    } 
    ArrayRow arrayRow = createRow();
    arrayRow.createRowEquals(paramSolverVariable1, paramSolverVariable2, paramInt1);
    if (paramInt2 != 8)
      arrayRow.addError(this, paramInt2); 
    addConstraint(arrayRow);
    return arrayRow;
  }
  
  public void addEquality(SolverVariable paramSolverVariable, int paramInt) {
    if (USE_BASIC_SYNONYMS && paramSolverVariable.definitionId == -1) {
      paramSolverVariable.setFinalValue(this, paramInt);
      for (byte b = 0; b < this.mVariablesID + 1; b++) {
        SolverVariable solverVariable = this.mCache.mIndexedVariables[b];
        if (solverVariable != null && solverVariable.isSynonym && solverVariable.synonym == paramSolverVariable.id)
          solverVariable.setFinalValue(this, paramInt + solverVariable.synonymDelta); 
      } 
      return;
    } 
    int i = paramSolverVariable.definitionId;
    if (paramSolverVariable.definitionId != -1) {
      ArrayRow arrayRow = this.mRows[i];
      if (arrayRow.isSimpleDefinition) {
        arrayRow.constantValue = paramInt;
      } else if (arrayRow.variables.getCurrentSize() == 0) {
        arrayRow.isSimpleDefinition = true;
        arrayRow.constantValue = paramInt;
      } else {
        arrayRow = createRow();
        arrayRow.createRowEquals(paramSolverVariable, paramInt);
        addConstraint(arrayRow);
      } 
    } else {
      ArrayRow arrayRow = createRow();
      arrayRow.createRowDefinition(paramSolverVariable, paramInt);
      addConstraint(arrayRow);
    } 
  }
  
  public void addGreaterBarrier(SolverVariable paramSolverVariable1, SolverVariable paramSolverVariable2, int paramInt, boolean paramBoolean) {
    ArrayRow arrayRow = createRow();
    SolverVariable solverVariable = createSlackVariable();
    solverVariable.strength = 0;
    arrayRow.createRowGreaterThan(paramSolverVariable1, paramSolverVariable2, solverVariable, paramInt);
    addConstraint(arrayRow);
  }
  
  public void addGreaterThan(SolverVariable paramSolverVariable1, SolverVariable paramSolverVariable2, int paramInt1, int paramInt2) {
    ArrayRow arrayRow = createRow();
    SolverVariable solverVariable = createSlackVariable();
    solverVariable.strength = 0;
    arrayRow.createRowGreaterThan(paramSolverVariable1, paramSolverVariable2, solverVariable, paramInt1);
    if (paramInt2 != 8)
      addSingleError(arrayRow, (int)(-1.0F * arrayRow.variables.get(solverVariable)), paramInt2); 
    addConstraint(arrayRow);
  }
  
  public void addLowerBarrier(SolverVariable paramSolverVariable1, SolverVariable paramSolverVariable2, int paramInt, boolean paramBoolean) {
    ArrayRow arrayRow = createRow();
    SolverVariable solverVariable = createSlackVariable();
    solverVariable.strength = 0;
    arrayRow.createRowLowerThan(paramSolverVariable1, paramSolverVariable2, solverVariable, paramInt);
    addConstraint(arrayRow);
  }
  
  public void addLowerThan(SolverVariable paramSolverVariable1, SolverVariable paramSolverVariable2, int paramInt1, int paramInt2) {
    ArrayRow arrayRow = createRow();
    SolverVariable solverVariable = createSlackVariable();
    solverVariable.strength = 0;
    arrayRow.createRowLowerThan(paramSolverVariable1, paramSolverVariable2, solverVariable, paramInt1);
    if (paramInt2 != 8)
      addSingleError(arrayRow, (int)(-1.0F * arrayRow.variables.get(solverVariable)), paramInt2); 
    addConstraint(arrayRow);
  }
  
  public void addRatio(SolverVariable paramSolverVariable1, SolverVariable paramSolverVariable2, SolverVariable paramSolverVariable3, SolverVariable paramSolverVariable4, float paramFloat, int paramInt) {
    ArrayRow arrayRow = createRow();
    arrayRow.createRowDimensionRatio(paramSolverVariable1, paramSolverVariable2, paramSolverVariable3, paramSolverVariable4, paramFloat);
    if (paramInt != 8)
      arrayRow.addError(this, paramInt); 
    addConstraint(arrayRow);
  }
  
  void addSingleError(ArrayRow paramArrayRow, int paramInt1, int paramInt2) {
    paramArrayRow.addSingleError(createErrorVariable(paramInt2, null), paramInt1);
  }
  
  public void addSynonym(SolverVariable paramSolverVariable1, SolverVariable paramSolverVariable2, int paramInt) {
    if (paramSolverVariable1.definitionId == -1 && paramInt == 0) {
      SolverVariable solverVariable = paramSolverVariable2;
      int i = paramInt;
      if (paramSolverVariable2.isSynonym) {
        i = (int)(paramInt + paramSolverVariable2.synonymDelta);
        solverVariable = this.mCache.mIndexedVariables[paramSolverVariable2.synonym];
      } 
      if (paramSolverVariable1.isSynonym) {
        paramInt = (int)(i - paramSolverVariable1.synonymDelta);
        paramSolverVariable1 = this.mCache.mIndexedVariables[paramSolverVariable1.synonym];
      } else {
        paramSolverVariable1.setSynonym(this, solverVariable, 0.0F);
      } 
    } else {
      addEquality(paramSolverVariable1, paramSolverVariable2, paramInt, 8);
    } 
  }
  
  final void cleanupRows() {
    for (int i = 0;; i = j + 1) {
      int j;
      if (i < this.mNumRows) {
        ArrayRow arrayRow = this.mRows[i];
        if (arrayRow.variables.getCurrentSize() == 0)
          arrayRow.isSimpleDefinition = true; 
        j = i;
        if (arrayRow.isSimpleDefinition) {
          arrayRow.variable.computedValue = arrayRow.constantValue;
          arrayRow.variable.removeFromRow(arrayRow);
          j = i;
          while (true) {
            int k = this.mNumRows;
            if (j < k - 1) {
              ArrayRow[] arrayOfArrayRow = this.mRows;
              arrayOfArrayRow[j] = arrayOfArrayRow[j + 1];
              j++;
              continue;
            } 
            this.mRows[k - 1] = null;
            this.mNumRows = k - 1;
            j = i - 1;
            if (OPTIMIZED_ENGINE) {
              this.mCache.optimizedArrayRowPool.release(arrayRow);
              break;
            } 
            this.mCache.arrayRowPool.release(arrayRow);
            i = j + 1;
          } 
        } 
      } else {
        break;
      } 
    } 
  }
  
  public SolverVariable createErrorVariable(int paramInt, String paramString) {
    Metrics metrics = sMetrics;
    if (metrics != null)
      metrics.errors++; 
    if (this.mNumColumns + 1 >= this.mMaxColumns)
      increaseTableSize(); 
    SolverVariable solverVariable = acquireSolverVariable(SolverVariable.Type.ERROR, paramString);
    int i = this.mVariablesID + 1;
    this.mVariablesID = i;
    this.mNumColumns++;
    solverVariable.id = i;
    solverVariable.strength = paramInt;
    this.mCache.mIndexedVariables[this.mVariablesID] = solverVariable;
    this.mGoal.addError(solverVariable);
    return solverVariable;
  }
  
  public SolverVariable createExtraVariable() {
    Metrics metrics = sMetrics;
    if (metrics != null)
      metrics.extravariables++; 
    if (this.mNumColumns + 1 >= this.mMaxColumns)
      increaseTableSize(); 
    SolverVariable solverVariable = acquireSolverVariable(SolverVariable.Type.SLACK, null);
    int i = this.mVariablesID + 1;
    this.mVariablesID = i;
    this.mNumColumns++;
    solverVariable.id = i;
    this.mCache.mIndexedVariables[this.mVariablesID] = solverVariable;
    return solverVariable;
  }
  
  public SolverVariable createObjectVariable(Object paramObject) {
    SolverVariable solverVariable;
    if (paramObject == null)
      return null; 
    if (this.mNumColumns + 1 >= this.mMaxColumns)
      increaseTableSize(); 
    null = null;
    if (paramObject instanceof ConstraintAnchor) {
      null = ((ConstraintAnchor)paramObject).getSolverVariable();
      solverVariable = null;
      if (null == null) {
        ((ConstraintAnchor)paramObject).resetSolverVariable(this.mCache);
        solverVariable = ((ConstraintAnchor)paramObject).getSolverVariable();
      } 
      if (solverVariable.id != -1 && solverVariable.id <= this.mVariablesID) {
        null = solverVariable;
        if (this.mCache.mIndexedVariables[solverVariable.id] == null) {
          if (solverVariable.id != -1)
            solverVariable.reset(); 
          int j = this.mVariablesID + 1;
          this.mVariablesID = j;
          this.mNumColumns++;
          solverVariable.id = j;
          solverVariable.mType = SolverVariable.Type.UNRESTRICTED;
          this.mCache.mIndexedVariables[this.mVariablesID] = solverVariable;
          return solverVariable;
        } 
        return null;
      } 
    } else {
      return null;
    } 
    if (solverVariable.id != -1)
      solverVariable.reset(); 
    int i = this.mVariablesID + 1;
    this.mVariablesID = i;
    this.mNumColumns++;
    solverVariable.id = i;
    solverVariable.mType = SolverVariable.Type.UNRESTRICTED;
    this.mCache.mIndexedVariables[this.mVariablesID] = solverVariable;
    return solverVariable;
  }
  
  public ArrayRow createRow() {
    ArrayRow arrayRow;
    if (OPTIMIZED_ENGINE) {
      arrayRow = this.mCache.optimizedArrayRowPool.acquire();
      if (arrayRow == null) {
        arrayRow = new ValuesRow(this.mCache);
        OPTIMIZED_ARRAY_ROW_CREATION++;
      } else {
        arrayRow.reset();
      } 
    } else {
      arrayRow = this.mCache.arrayRowPool.acquire();
      if (arrayRow == null) {
        arrayRow = new ArrayRow(this.mCache);
        ARRAY_ROW_CREATION++;
      } else {
        arrayRow.reset();
      } 
    } 
    SolverVariable.increaseErrorId();
    return arrayRow;
  }
  
  public SolverVariable createSlackVariable() {
    Metrics metrics = sMetrics;
    if (metrics != null)
      metrics.slackvariables++; 
    if (this.mNumColumns + 1 >= this.mMaxColumns)
      increaseTableSize(); 
    SolverVariable solverVariable = acquireSolverVariable(SolverVariable.Type.SLACK, null);
    int i = this.mVariablesID + 1;
    this.mVariablesID = i;
    this.mNumColumns++;
    solverVariable.id = i;
    this.mCache.mIndexedVariables[this.mVariablesID] = solverVariable;
    return solverVariable;
  }
  
  public void displayReadableRows() {
    displaySolverVariables();
    String str1 = " num vars " + this.mVariablesID + "\n";
    byte b = 0;
    while (b < this.mVariablesID + 1) {
      SolverVariable solverVariable = this.mCache.mIndexedVariables[b];
      String str = str1;
      if (solverVariable != null) {
        str = str1;
        if (solverVariable.isFinalValue)
          str = str1 + " $[" + b + "] => " + solverVariable + " = " + solverVariable.computedValue + "\n"; 
      } 
      b++;
      str1 = str;
    } 
    str1 = str1 + "\n";
    b = 0;
    while (b < this.mVariablesID + 1) {
      SolverVariable solverVariable = this.mCache.mIndexedVariables[b];
      String str = str1;
      if (solverVariable != null) {
        str = str1;
        if (solverVariable.isSynonym) {
          SolverVariable solverVariable1 = this.mCache.mIndexedVariables[solverVariable.synonym];
          str = str1 + " ~[" + b + "] => " + solverVariable + " = " + solverVariable1 + " + " + solverVariable.synonymDelta + "\n";
        } 
      } 
      b++;
      str1 = str;
    } 
    str1 = str1 + "\n\n #  ";
    for (b = 0; b < this.mNumRows; b++) {
      str1 = str1 + this.mRows[b].toReadableString();
      str1 = str1 + "\n #  ";
    } 
    String str2 = str1;
    if (this.mGoal != null)
      str2 = str1 + "Goal: " + this.mGoal + "\n"; 
    System.out.println(str2);
  }
  
  void displaySystemInformation() {
    int i = 0;
    int j = 0;
    while (j < this.TABLE_SIZE) {
      ArrayRow[] arrayOfArrayRow = this.mRows;
      int m = i;
      if (arrayOfArrayRow[j] != null)
        m = i + arrayOfArrayRow[j].sizeInBytes(); 
      j++;
      i = m;
    } 
    int k = 0;
    j = 0;
    while (j < this.mNumRows) {
      ArrayRow[] arrayOfArrayRow = this.mRows;
      int m = k;
      if (arrayOfArrayRow[j] != null)
        m = k + arrayOfArrayRow[j].sizeInBytes(); 
      j++;
      k = m;
    } 
    PrintStream printStream = System.out;
    StringBuilder stringBuilder = (new StringBuilder()).append("Linear System -> Table size: ").append(this.TABLE_SIZE).append(" (");
    j = this.TABLE_SIZE;
    printStream.println(stringBuilder.append(getDisplaySize(j * j)).append(") -- row sizes: ").append(getDisplaySize(i)).append(", actual size: ").append(getDisplaySize(k)).append(" rows: ").append(this.mNumRows).append("/").append(this.mMaxRows).append(" cols: ").append(this.mNumColumns).append("/").append(this.mMaxColumns).append(" ").append(0).append(" occupied cells, ").append(getDisplaySize(0)).toString());
  }
  
  public void displayVariablesReadableRows() {
    displaySolverVariables();
    String str2 = "";
    byte b = 0;
    while (b < this.mNumRows) {
      String str = str2;
      if ((this.mRows[b]).variable.mType == SolverVariable.Type.UNRESTRICTED) {
        str = str2 + this.mRows[b].toReadableString();
        str = str + "\n";
      } 
      b++;
      str2 = str;
    } 
    String str1 = str2 + this.mGoal + "\n";
    System.out.println(str1);
  }
  
  public void fillMetrics(Metrics paramMetrics) {
    sMetrics = paramMetrics;
  }
  
  public Cache getCache() {
    return this.mCache;
  }
  
  Row getGoal() {
    return this.mGoal;
  }
  
  public int getMemoryUsed() {
    int i = 0;
    byte b = 0;
    while (b < this.mNumRows) {
      ArrayRow[] arrayOfArrayRow = this.mRows;
      int j = i;
      if (arrayOfArrayRow[b] != null)
        j = i + arrayOfArrayRow[b].sizeInBytes(); 
      b++;
      i = j;
    } 
    return i;
  }
  
  public int getNumEquations() {
    return this.mNumRows;
  }
  
  public int getNumVariables() {
    return this.mVariablesID;
  }
  
  public int getObjectVariableValue(Object paramObject) {
    paramObject = ((ConstraintAnchor)paramObject).getSolverVariable();
    return (paramObject != null) ? (int)(((SolverVariable)paramObject).computedValue + 0.5F) : 0;
  }
  
  ArrayRow getRow(int paramInt) {
    return this.mRows[paramInt];
  }
  
  float getValueFor(String paramString) {
    SolverVariable solverVariable = getVariable(paramString, SolverVariable.Type.UNRESTRICTED);
    return (solverVariable == null) ? 0.0F : solverVariable.computedValue;
  }
  
  SolverVariable getVariable(String paramString, SolverVariable.Type paramType) {
    if (this.mVariables == null)
      this.mVariables = new HashMap<>(); 
    SolverVariable solverVariable2 = this.mVariables.get(paramString);
    SolverVariable solverVariable1 = solverVariable2;
    if (solverVariable2 == null)
      solverVariable1 = createVariable(paramString, paramType); 
    return solverVariable1;
  }
  
  public void minimize() throws Exception {
    Metrics metrics = sMetrics;
    if (metrics != null)
      metrics.minimize++; 
    if (this.mGoal.isEmpty()) {
      computeValues();
      return;
    } 
    if (this.graphOptimizer || this.newgraphOptimizer) {
      boolean bool1;
      metrics = sMetrics;
      if (metrics != null)
        metrics.graphOptimizer++; 
      boolean bool2 = true;
      byte b = 0;
      while (true) {
        bool1 = bool2;
        if (b < this.mNumRows) {
          if (!(this.mRows[b]).isSimpleDefinition) {
            bool1 = false;
            break;
          } 
          b++;
          continue;
        } 
        break;
      } 
      if (!bool1) {
        minimizeGoal(this.mGoal);
      } else {
        metrics = sMetrics;
        if (metrics != null)
          metrics.fullySolved++; 
        computeValues();
      } 
      return;
    } 
    minimizeGoal(this.mGoal);
  }
  
  void minimizeGoal(Row paramRow) throws Exception {
    Metrics metrics = sMetrics;
    if (metrics != null) {
      metrics.minimizeGoal++;
      metrics = sMetrics;
      metrics.maxVariables = Math.max(metrics.maxVariables, this.mNumColumns);
      metrics = sMetrics;
      metrics.maxRows = Math.max(metrics.maxRows, this.mNumRows);
    } 
    enforceBFS(paramRow);
    optimize(paramRow, false);
    computeValues();
  }
  
  public void removeRow(ArrayRow paramArrayRow) {
    if (paramArrayRow.isSimpleDefinition && paramArrayRow.variable != null) {
      if (paramArrayRow.variable.definitionId != -1) {
        int i = paramArrayRow.variable.definitionId;
        while (true) {
          int j = this.mNumRows;
          if (i < j - 1) {
            SolverVariable solverVariable = (this.mRows[i + 1]).variable;
            if (solverVariable.definitionId == i + 1)
              solverVariable.definitionId = i; 
            ArrayRow[] arrayOfArrayRow = this.mRows;
            arrayOfArrayRow[i] = arrayOfArrayRow[i + 1];
            i++;
            continue;
          } 
          this.mNumRows = j - 1;
          break;
        } 
      } 
      if (!paramArrayRow.variable.isFinalValue)
        paramArrayRow.variable.setFinalValue(this, paramArrayRow.constantValue); 
      if (OPTIMIZED_ENGINE) {
        this.mCache.optimizedArrayRowPool.release(paramArrayRow);
      } else {
        this.mCache.arrayRowPool.release(paramArrayRow);
      } 
    } 
  }
  
  public void reset() {
    byte b;
    for (b = 0; b < this.mCache.mIndexedVariables.length; b++) {
      SolverVariable solverVariable = this.mCache.mIndexedVariables[b];
      if (solverVariable != null)
        solverVariable.reset(); 
    } 
    this.mCache.solverVariablePool.releaseAll(this.mPoolVariables, this.mPoolVariablesCount);
    this.mPoolVariablesCount = 0;
    Arrays.fill((Object[])this.mCache.mIndexedVariables, (Object)null);
    HashMap<String, SolverVariable> hashMap = this.mVariables;
    if (hashMap != null)
      hashMap.clear(); 
    this.mVariablesID = 0;
    this.mGoal.clear();
    this.mNumColumns = 1;
    for (b = 0; b < this.mNumRows; b++) {
      ArrayRow[] arrayOfArrayRow = this.mRows;
      if (arrayOfArrayRow[b] != null)
        (arrayOfArrayRow[b]).used = false; 
    } 
    releaseRows();
    this.mNumRows = 0;
    if (OPTIMIZED_ENGINE) {
      this.mTempGoal = new ValuesRow(this.mCache);
    } else {
      this.mTempGoal = new ArrayRow(this.mCache);
    } 
  }
  
  static interface Row {
    void addError(SolverVariable param1SolverVariable);
    
    void clear();
    
    SolverVariable getKey();
    
    SolverVariable getPivotCandidate(LinearSystem param1LinearSystem, boolean[] param1ArrayOfboolean);
    
    void initFromRow(Row param1Row);
    
    boolean isEmpty();
    
    void updateFromFinalVariable(LinearSystem param1LinearSystem, SolverVariable param1SolverVariable, boolean param1Boolean);
    
    void updateFromRow(LinearSystem param1LinearSystem, ArrayRow param1ArrayRow, boolean param1Boolean);
    
    void updateFromSystem(LinearSystem param1LinearSystem);
  }
  
  class ValuesRow extends ArrayRow {
    final LinearSystem this$0;
    
    public ValuesRow(Cache param1Cache) {
      this.variables = new SolverVariableValues(this, param1Cache);
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\LinearSystem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */