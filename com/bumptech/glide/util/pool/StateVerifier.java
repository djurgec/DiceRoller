package com.bumptech.glide.util.pool;

public abstract class StateVerifier {
  private static final boolean DEBUG = false;
  
  private StateVerifier() {}
  
  public static StateVerifier newInstance() {
    return new DefaultStateVerifier();
  }
  
  abstract void setRecycled(boolean paramBoolean);
  
  public abstract void throwIfRecycled();
  
  private static class DebugStateVerifier extends StateVerifier {
    private volatile RuntimeException recycledAtStackTraceException;
    
    void setRecycled(boolean param1Boolean) {
      if (param1Boolean) {
        this.recycledAtStackTraceException = new RuntimeException("Released");
      } else {
        this.recycledAtStackTraceException = null;
      } 
    }
    
    public void throwIfRecycled() {
      if (this.recycledAtStackTraceException == null)
        return; 
      throw new IllegalStateException("Already released", this.recycledAtStackTraceException);
    }
  }
  
  private static class DefaultStateVerifier extends StateVerifier {
    private volatile boolean isReleased;
    
    public void setRecycled(boolean param1Boolean) {
      this.isReleased = param1Boolean;
    }
    
    public void throwIfRecycled() {
      if (!this.isReleased)
        return; 
      throw new IllegalStateException("Already released");
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glid\\util\pool\StateVerifier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */