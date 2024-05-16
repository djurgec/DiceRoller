package com.bumptech.glide;

public enum MemoryCategory {
  HIGH, LOW, NORMAL;
  
  private static final MemoryCategory[] $VALUES;
  
  private final float multiplier;
  
  static {
    MemoryCategory memoryCategory1 = new MemoryCategory("LOW", 0, 0.5F);
    LOW = memoryCategory1;
    MemoryCategory memoryCategory2 = new MemoryCategory("NORMAL", 1, 1.0F);
    NORMAL = memoryCategory2;
    MemoryCategory memoryCategory3 = new MemoryCategory("HIGH", 2, 1.5F);
    HIGH = memoryCategory3;
    $VALUES = new MemoryCategory[] { memoryCategory1, memoryCategory2, memoryCategory3 };
  }
  
  MemoryCategory(float paramFloat) {
    this.multiplier = paramFloat;
  }
  
  public float getMultiplier() {
    return this.multiplier;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\MemoryCategory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */