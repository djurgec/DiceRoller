package androidx.constraintlayout.core;

import java.util.Arrays;
import java.util.Comparator;

public class PriorityGoalRow extends ArrayRow {
  private static final boolean DEBUG = false;
  
  static final int NOT_FOUND = -1;
  
  private static final float epsilon = 1.0E-4F;
  
  private int TABLE_SIZE = 128;
  
  GoalVariableAccessor accessor = new GoalVariableAccessor(this);
  
  private SolverVariable[] arrayGoals = new SolverVariable[128];
  
  Cache mCache;
  
  private int numGoals = 0;
  
  private SolverVariable[] sortArray = new SolverVariable[128];
  
  public PriorityGoalRow(Cache paramCache) {
    super(paramCache);
    this.mCache = paramCache;
  }
  
  private final void addToGoal(SolverVariable paramSolverVariable) {
    int i = this.numGoals;
    SolverVariable[] arrayOfSolverVariable = this.arrayGoals;
    if (i + 1 > arrayOfSolverVariable.length) {
      arrayOfSolverVariable = Arrays.<SolverVariable>copyOf(arrayOfSolverVariable, arrayOfSolverVariable.length * 2);
      this.arrayGoals = arrayOfSolverVariable;
      this.sortArray = Arrays.<SolverVariable>copyOf(arrayOfSolverVariable, arrayOfSolverVariable.length * 2);
    } 
    arrayOfSolverVariable = this.arrayGoals;
    i = this.numGoals;
    arrayOfSolverVariable[i] = paramSolverVariable;
    this.numGoals = ++i;
    if (i > 1 && (arrayOfSolverVariable[i - 1]).id > paramSolverVariable.id) {
      i = 0;
      while (true) {
        int j = this.numGoals;
        if (i < j) {
          this.sortArray[i] = this.arrayGoals[i];
          i++;
          continue;
        } 
        Arrays.sort(this.sortArray, 0, j, new Comparator<SolverVariable>() {
              final PriorityGoalRow this$0;
              
              public int compare(SolverVariable param1SolverVariable1, SolverVariable param1SolverVariable2) {
                return param1SolverVariable1.id - param1SolverVariable2.id;
              }
            });
        for (i = 0; i < this.numGoals; i++)
          this.arrayGoals[i] = this.sortArray[i]; 
        break;
      } 
    } 
    paramSolverVariable.inGoal = true;
    paramSolverVariable.addToRow(this);
  }
  
  private final void removeGoal(SolverVariable paramSolverVariable) {
    for (byte b = 0; b < this.numGoals; b++) {
      if (this.arrayGoals[b] == paramSolverVariable)
        while (true) {
          int i = this.numGoals;
          if (b < i - 1) {
            SolverVariable[] arrayOfSolverVariable = this.arrayGoals;
            arrayOfSolverVariable[b] = arrayOfSolverVariable[b + 1];
            b++;
            continue;
          } 
          this.numGoals = i - 1;
          paramSolverVariable.inGoal = false;
          return;
        }  
    } 
  }
  
  public void addError(SolverVariable paramSolverVariable) {
    this.accessor.init(paramSolverVariable);
    this.accessor.reset();
    paramSolverVariable.goalStrengthVector[paramSolverVariable.strength] = 1.0F;
    addToGoal(paramSolverVariable);
  }
  
  public void clear() {
    this.numGoals = 0;
    this.constantValue = 0.0F;
  }
  
  public SolverVariable getPivotCandidate(LinearSystem paramLinearSystem, boolean[] paramArrayOfboolean) {
    byte b1 = -1;
    byte b = 0;
    while (b < this.numGoals) {
      byte b2;
      SolverVariable solverVariable = this.arrayGoals[b];
      if (paramArrayOfboolean[solverVariable.id]) {
        b2 = b1;
      } else {
        this.accessor.init(solverVariable);
        if (b1 == -1) {
          b2 = b1;
          if (this.accessor.isNegative())
            b2 = b; 
        } else {
          b2 = b1;
          if (this.accessor.isSmallerThan(this.arrayGoals[b1]))
            b2 = b; 
        } 
      } 
      b++;
      b1 = b2;
    } 
    return (b1 == -1) ? null : this.arrayGoals[b1];
  }
  
  public boolean isEmpty() {
    boolean bool;
    if (this.numGoals == 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public String toString() {
    String str = "" + " goal -> (" + this.constantValue + ") : ";
    for (byte b = 0; b < this.numGoals; b++) {
      SolverVariable solverVariable = this.arrayGoals[b];
      this.accessor.init(solverVariable);
      str = str + this.accessor + " ";
    } 
    return str;
  }
  
  public void updateFromRow(LinearSystem paramLinearSystem, ArrayRow paramArrayRow, boolean paramBoolean) {
    SolverVariable solverVariable = paramArrayRow.variable;
    if (solverVariable == null)
      return; 
    ArrayRow.ArrayRowVariables arrayRowVariables = paramArrayRow.variables;
    int i = arrayRowVariables.getCurrentSize();
    for (byte b = 0; b < i; b++) {
      SolverVariable solverVariable1 = arrayRowVariables.getVariable(b);
      float f = arrayRowVariables.getVariableValue(b);
      this.accessor.init(solverVariable1);
      if (this.accessor.addToGoal(solverVariable, f))
        addToGoal(solverVariable1); 
      this.constantValue += paramArrayRow.constantValue * f;
    } 
    removeGoal(solverVariable);
  }
  
  class GoalVariableAccessor {
    PriorityGoalRow row;
    
    final PriorityGoalRow this$0;
    
    SolverVariable variable;
    
    public GoalVariableAccessor(PriorityGoalRow param1PriorityGoalRow1) {
      this.row = param1PriorityGoalRow1;
    }
    
    public void add(SolverVariable param1SolverVariable) {
      for (byte b = 0; b < 9; b++) {
        float[] arrayOfFloat = this.variable.goalStrengthVector;
        arrayOfFloat[b] = arrayOfFloat[b] + param1SolverVariable.goalStrengthVector[b];
        if (Math.abs(this.variable.goalStrengthVector[b]) < 1.0E-4F)
          this.variable.goalStrengthVector[b] = 0.0F; 
      } 
    }
    
    public boolean addToGoal(SolverVariable param1SolverVariable, float param1Float) {
      if (this.variable.inGoal) {
        boolean bool = true;
        for (byte b1 = 0; b1 < 9; b1++) {
          float[] arrayOfFloat = this.variable.goalStrengthVector;
          arrayOfFloat[b1] = arrayOfFloat[b1] + param1SolverVariable.goalStrengthVector[b1] * param1Float;
          if (Math.abs(this.variable.goalStrengthVector[b1]) < 1.0E-4F) {
            this.variable.goalStrengthVector[b1] = 0.0F;
          } else {
            bool = false;
          } 
        } 
        if (bool)
          PriorityGoalRow.this.removeGoal(this.variable); 
        return false;
      } 
      for (byte b = 0; b < 9; b++) {
        float f = param1SolverVariable.goalStrengthVector[b];
        if (f != 0.0F) {
          float f1 = param1Float * f;
          f = f1;
          if (Math.abs(f1) < 1.0E-4F)
            f = 0.0F; 
          this.variable.goalStrengthVector[b] = f;
        } else {
          this.variable.goalStrengthVector[b] = 0.0F;
        } 
      } 
      return true;
    }
    
    public void init(SolverVariable param1SolverVariable) {
      this.variable = param1SolverVariable;
    }
    
    public final boolean isNegative() {
      for (byte b = 8; b >= 0; b--) {
        float f = this.variable.goalStrengthVector[b];
        if (f > 0.0F)
          return false; 
        if (f < 0.0F)
          return true; 
      } 
      return false;
    }
    
    public final boolean isNull() {
      for (byte b = 0; b < 9; b++) {
        if (this.variable.goalStrengthVector[b] != 0.0F)
          return false; 
      } 
      return true;
    }
    
    public final boolean isSmallerThan(SolverVariable param1SolverVariable) {
      byte b = 8;
      while (b >= 0) {
        float f2 = param1SolverVariable.goalStrengthVector[b];
        float f1 = this.variable.goalStrengthVector[b];
        if (f1 == f2) {
          b--;
          continue;
        } 
        return (f1 < f2);
      } 
      return false;
    }
    
    public void reset() {
      Arrays.fill(this.variable.goalStrengthVector, 0.0F);
    }
    
    public String toString() {
      String str1 = "[ ";
      String str2 = str1;
      if (this.variable != null) {
        byte b = 0;
        while (true) {
          str2 = str1;
          if (b < 9) {
            str1 = str1 + this.variable.goalStrengthVector[b] + " ";
            b++;
            continue;
          } 
          break;
        } 
      } 
      return str2 + "] " + this.variable;
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\PriorityGoalRow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */