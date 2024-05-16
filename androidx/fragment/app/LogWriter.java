package androidx.fragment.app;

import android.util.Log;
import java.io.Writer;

final class LogWriter extends Writer {
  private StringBuilder mBuilder = new StringBuilder(128);
  
  private final String mTag;
  
  LogWriter(String paramString) {
    this.mTag = paramString;
  }
  
  private void flushBuilder() {
    if (this.mBuilder.length() > 0) {
      Log.d(this.mTag, this.mBuilder.toString());
      StringBuilder stringBuilder = this.mBuilder;
      stringBuilder.delete(0, stringBuilder.length());
    } 
  }
  
  public void close() {
    flushBuilder();
  }
  
  public void flush() {
    flushBuilder();
  }
  
  public void write(char[] paramArrayOfchar, int paramInt1, int paramInt2) {
    for (byte b = 0; b < paramInt2; b++) {
      char c = paramArrayOfchar[paramInt1 + b];
      if (c == '\n') {
        flushBuilder();
      } else {
        this.mBuilder.append(c);
      } 
    } 
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\fragment\app\LogWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */