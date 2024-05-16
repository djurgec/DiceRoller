package com.bumptech.glide.util.pool;

public final class GlideTrace {
  private static final int MAX_LENGTH = 127;
  
  private static final boolean TRACING_ENABLED = false;
  
  public static void beginSection(String paramString) {}
  
  public static void beginSectionFormat(String paramString, Object paramObject) {}
  
  public static void beginSectionFormat(String paramString, Object paramObject1, Object paramObject2) {}
  
  public static void beginSectionFormat(String paramString, Object paramObject1, Object paramObject2, Object paramObject3) {}
  
  public static void endSection() {}
  
  private static String truncateTag(String paramString) {
    return (paramString.length() > 127) ? paramString.substring(0, 126) : paramString;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glid\\util\pool\GlideTrace.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */