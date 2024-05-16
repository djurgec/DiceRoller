package androidx.constraintlayout.core;

import java.util.Arrays;
import java.util.HashSet;

public class SolverVariable implements Comparable<SolverVariable> {
  private static final boolean INTERNAL_DEBUG = false;
  
  static final int MAX_STRENGTH = 9;
  
  public static final int STRENGTH_BARRIER = 6;
  
  public static final int STRENGTH_CENTERING = 7;
  
  public static final int STRENGTH_EQUALITY = 5;
  
  public static final int STRENGTH_FIXED = 8;
  
  public static final int STRENGTH_HIGH = 3;
  
  public static final int STRENGTH_HIGHEST = 4;
  
  public static final int STRENGTH_LOW = 1;
  
  public static final int STRENGTH_MEDIUM = 2;
  
  public static final int STRENGTH_NONE = 0;
  
  private static final boolean VAR_USE_HASH = false;
  
  private static int uniqueConstantId;
  
  private static int uniqueErrorId;
  
  private static int uniqueId;
  
  private static int uniqueSlackId = 1;
  
  private static int uniqueUnrestrictedId;
  
  public float computedValue;
  
  int definitionId = -1;
  
  float[] goalStrengthVector = new float[9];
  
  public int id = -1;
  
  public boolean inGoal;
  
  HashSet<ArrayRow> inRows = null;
  
  public boolean isFinalValue = false;
  
  boolean isSynonym = false;
  
  ArrayRow[] mClientEquations = new ArrayRow[16];
  
  int mClientEquationsCount = 0;
  
  private String mName;
  
  Type mType;
  
  public int strength = 0;
  
  float[] strengthVector = new float[9];
  
  int synonym = -1;
  
  float synonymDelta = 0.0F;
  
  public int usageInRowCount = 0;
  
  static {
    uniqueErrorId = 1;
    uniqueUnrestrictedId = 1;
    uniqueConstantId = 1;
    uniqueId = 1;
  }
  
  public SolverVariable(Type paramType, String paramString) {
    this.mType = paramType;
  }
  
  public SolverVariable(String paramString, Type paramType) {
    this.mName = paramString;
    this.mType = paramType;
  }
  
  private static String getUniqueName(Type paramType, String paramString) {
    if (paramString != null)
      return paramString + uniqueErrorId; 
    switch (paramType) {
      default:
        throw new AssertionError(paramType.name());
      case null:
        stringBuilder = (new StringBuilder()).append("V");
        i = uniqueId + 1;
        uniqueId = i;
        return stringBuilder.append(i).toString();
      case null:
        stringBuilder = (new StringBuilder()).append("e");
        i = uniqueErrorId + 1;
        uniqueErrorId = i;
        return stringBuilder.append(i).toString();
      case null:
        stringBuilder = (new StringBuilder()).append("S");
        i = uniqueSlackId + 1;
        uniqueSlackId = i;
        return stringBuilder.append(i).toString();
      case null:
        stringBuilder = (new StringBuilder()).append("C");
        i = uniqueConstantId + 1;
        uniqueConstantId = i;
        return stringBuilder.append(i).toString();
      case null:
        break;
    } 
    StringBuilder stringBuilder = (new StringBuilder()).append("U");
    int i = uniqueUnrestrictedId + 1;
    uniqueUnrestrictedId = i;
    return stringBuilder.append(i).toString();
  }
  
  static void increaseErrorId() {
    uniqueErrorId++;
  }
  
  public final void addToRow(ArrayRow paramArrayRow) {
    int i = 0;
    while (true) {
      int j = this.mClientEquationsCount;
      if (i < j) {
        if (this.mClientEquations[i] == paramArrayRow)
          return; 
        i++;
        continue;
      } 
      ArrayRow[] arrayOfArrayRow = this.mClientEquations;
      if (j >= arrayOfArrayRow.length)
        this.mClientEquations = Arrays.<ArrayRow>copyOf(arrayOfArrayRow, arrayOfArrayRow.length * 2); 
      arrayOfArrayRow = this.mClientEquations;
      i = this.mClientEquationsCount;
      arrayOfArrayRow[i] = paramArrayRow;
      this.mClientEquationsCount = i + 1;
      return;
    } 
  }
  
  void clearStrengths() {
    for (byte b = 0; b < 9; b++)
      this.strengthVector[b] = 0.0F; 
  }
  
  public int compareTo(SolverVariable paramSolverVariable) {
    return this.id - paramSolverVariable.id;
  }
  
  public String getName() {
    return this.mName;
  }
  
  public final void removeFromRow(ArrayRow paramArrayRow) {
    int i = this.mClientEquationsCount;
    for (byte b = 0; b < i; b++) {
      if (this.mClientEquations[b] == paramArrayRow) {
        while (b < i - 1) {
          ArrayRow[] arrayOfArrayRow = this.mClientEquations;
          arrayOfArrayRow[b] = arrayOfArrayRow[b + 1];
          b++;
        } 
        this.mClientEquationsCount--;
        return;
      } 
    } 
  }
  
  public void reset() {
    this.mName = null;
    this.mType = Type.UNKNOWN;
    this.strength = 0;
    this.id = -1;
    this.definitionId = -1;
    this.computedValue = 0.0F;
    this.isFinalValue = false;
    this.isSynonym = false;
    this.synonym = -1;
    this.synonymDelta = 0.0F;
    int i = this.mClientEquationsCount;
    for (byte b = 0; b < i; b++)
      this.mClientEquations[b] = null; 
    this.mClientEquationsCount = 0;
    this.usageInRowCount = 0;
    this.inGoal = false;
    Arrays.fill(this.goalStrengthVector, 0.0F);
  }
  
  public void setFinalValue(LinearSystem paramLinearSystem, float paramFloat) {
    this.computedValue = paramFloat;
    this.isFinalValue = true;
    this.isSynonym = false;
    this.synonym = -1;
    this.synonymDelta = 0.0F;
    int i = this.mClientEquationsCount;
    this.definitionId = -1;
    for (byte b = 0; b < i; b++)
      this.mClientEquations[b].updateFromFinalVariable(paramLinearSystem, this, false); 
    this.mClientEquationsCount = 0;
  }
  
  public void setName(String paramString) {
    this.mName = paramString;
  }
  
  public void setSynonym(LinearSystem paramLinearSystem, SolverVariable paramSolverVariable, float paramFloat) {
    this.isSynonym = true;
    this.synonym = paramSolverVariable.id;
    this.synonymDelta = paramFloat;
    int i = this.mClientEquationsCount;
    this.definitionId = -1;
    for (byte b = 0; b < i; b++)
      this.mClientEquations[b].updateFromSynonymVariable(paramLinearSystem, this, false); 
    this.mClientEquationsCount = 0;
    paramLinearSystem.displayReadableRows();
  }
  
  public void setType(Type paramType, String paramString) {
    this.mType = paramType;
  }
  
  String strengthsToString() {
    String str2 = this + "[";
    boolean bool1 = false;
    boolean bool2 = true;
    for (byte b = 0; b < this.strengthVector.length; b++) {
      String str = str2 + this.strengthVector[b];
      float[] arrayOfFloat = this.strengthVector;
      if (arrayOfFloat[b] > 0.0F) {
        bool1 = false;
      } else if (arrayOfFloat[b] < 0.0F) {
        bool1 = true;
      } 
      if (arrayOfFloat[b] != 0.0F)
        bool2 = false; 
      if (b < arrayOfFloat.length - 1) {
        String str3 = str + ", ";
      } else {
        str2 = str + "] ";
      } 
    } 
    String str1 = str2;
    if (bool1)
      str1 = str2 + " (-)"; 
    str2 = str1;
    if (bool2)
      str2 = str1 + " (*)"; 
    return str2;
  }
  
  public String toString() {
    String str;
    if (this.mName != null) {
      str = "" + this.mName;
    } else {
      str = "" + this.id;
    } 
    return str;
  }
  
  public final void updateReferencesWithNewDefinition(LinearSystem paramLinearSystem, ArrayRow paramArrayRow) {
    int i = this.mClientEquationsCount;
    for (byte b = 0; b < i; b++)
      this.mClientEquations[b].updateFromRow(paramLinearSystem, paramArrayRow, false); 
    this.mClientEquationsCount = 0;
  }
  
  public enum Type {
    CONSTANT, ERROR, SLACK, UNKNOWN, UNRESTRICTED;
    
    private static final Type[] $VALUES;
    
    static {
      Type type1 = new Type("UNRESTRICTED", 0);
      UNRESTRICTED = type1;
      Type type3 = new Type("CONSTANT", 1);
      CONSTANT = type3;
      Type type5 = new Type("SLACK", 2);
      SLACK = type5;
      Type type4 = new Type("ERROR", 3);
      ERROR = type4;
      Type type2 = new Type("UNKNOWN", 4);
      UNKNOWN = type2;
      $VALUES = new Type[] { type1, type3, type5, type4, type2 };
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\SolverVariable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */