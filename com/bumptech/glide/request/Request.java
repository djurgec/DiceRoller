package com.bumptech.glide.request;

public interface Request {
  void begin();
  
  void clear();
  
  boolean isAnyResourceSet();
  
  boolean isCleared();
  
  boolean isComplete();
  
  boolean isEquivalentTo(Request paramRequest);
  
  boolean isRunning();
  
  void pause();
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\request\Request.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */