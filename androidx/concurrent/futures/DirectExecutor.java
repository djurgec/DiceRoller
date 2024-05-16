package androidx.concurrent.futures;

import java.util.concurrent.Executor;

enum DirectExecutor implements Executor {
  INSTANCE;
  
  private static final DirectExecutor[] $VALUES;
  
  static {
    DirectExecutor directExecutor = new DirectExecutor("INSTANCE", 0);
    INSTANCE = directExecutor;
    $VALUES = new DirectExecutor[] { directExecutor };
  }
  
  public void execute(Runnable paramRunnable) {
    paramRunnable.run();
  }
  
  public String toString() {
    return "DirectExecutor";
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\concurrent\futures\DirectExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */