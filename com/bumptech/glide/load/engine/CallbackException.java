package com.bumptech.glide.load.engine;

final class CallbackException extends RuntimeException {
  private static final long serialVersionUID = -7530898992688511851L;
  
  CallbackException(Throwable paramThrowable) {
    super("Unexpected exception thrown by non-Glide code", paramThrowable);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\engine\CallbackException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */