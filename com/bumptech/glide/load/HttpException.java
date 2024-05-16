package com.bumptech.glide.load;

import java.io.IOException;

public final class HttpException extends IOException {
  public static final int UNKNOWN = -1;
  
  private static final long serialVersionUID = 1L;
  
  private final int statusCode;
  
  public HttpException(int paramInt) {
    this("Http request failed", paramInt);
  }
  
  @Deprecated
  public HttpException(String paramString) {
    this(paramString, -1);
  }
  
  public HttpException(String paramString, int paramInt) {
    this(paramString, paramInt, null);
  }
  
  public HttpException(String paramString, int paramInt, Throwable paramThrowable) {
    super(paramString + ", status code: " + paramInt, paramThrowable);
    this.statusCode = paramInt;
  }
  
  public int getStatusCode() {
    return this.statusCode;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\HttpException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */