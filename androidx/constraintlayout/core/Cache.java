package androidx.constraintlayout.core;

public class Cache {
  Pools.Pool<ArrayRow> arrayRowPool = new Pools.SimplePool<>(256);
  
  SolverVariable[] mIndexedVariables = new SolverVariable[32];
  
  Pools.Pool<ArrayRow> optimizedArrayRowPool = new Pools.SimplePool<>(256);
  
  Pools.Pool<SolverVariable> solverVariablePool = new Pools.SimplePool<>(256);
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\Cache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */