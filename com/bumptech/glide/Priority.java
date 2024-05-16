package com.bumptech.glide;

public enum Priority {
  HIGH, IMMEDIATE, LOW, NORMAL;
  
  private static final Priority[] $VALUES;
  
  static {
    Priority priority2 = new Priority("IMMEDIATE", 0);
    IMMEDIATE = priority2;
    Priority priority1 = new Priority("HIGH", 1);
    HIGH = priority1;
    Priority priority4 = new Priority("NORMAL", 2);
    NORMAL = priority4;
    Priority priority3 = new Priority("LOW", 3);
    LOW = priority3;
    $VALUES = new Priority[] { priority2, priority1, priority4, priority3 };
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\Priority.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */