package androidx.constraintlayout.core.motion.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class Utils {
  private static int clamp(int paramInt) {
    paramInt = (paramInt & (paramInt >> 31 ^ 0xFFFFFFFF)) - 255;
    return (paramInt & paramInt >> 31) + 255;
  }
  
  public static void log(String paramString) {
    StackTraceElement stackTraceElement = (new Throwable()).getStackTrace()[1];
    String str2 = stackTraceElement.getMethodName();
    String str3 = (str2 + "                  ").substring(0, 17);
    str2 = "    ".substring(Integer.toString(stackTraceElement.getLineNumber()).length());
    String str1 = ".(" + stackTraceElement.getFileName() + ":" + stackTraceElement.getLineNumber() + ")" + str2 + str3;
    System.out.println(str1 + " " + paramString);
  }
  
  public static void log(String paramString1, String paramString2) {
    System.out.println(paramString1 + " : " + paramString2);
  }
  
  public static void logStack(String paramString, int paramInt) {
    StackTraceElement[] arrayOfStackTraceElement = (new Throwable()).getStackTrace();
    String str = " ";
    int i = Math.min(paramInt, arrayOfStackTraceElement.length - 1);
    for (paramInt = 1; paramInt <= i; paramInt++) {
      StackTraceElement stackTraceElement = arrayOfStackTraceElement[paramInt];
      String str1 = ".(" + arrayOfStackTraceElement[paramInt].getFileName() + ":" + arrayOfStackTraceElement[paramInt].getLineNumber() + ") " + arrayOfStackTraceElement[paramInt].getMethodName();
      str = str + " ";
      System.out.println(paramString + str + str1 + str);
    } 
  }
  
  public static void loge(String paramString1, String paramString2) {
    System.err.println(paramString1 + " : " + paramString2);
  }
  
  public static int rgbaTocColor(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
    int j = clamp((int)(paramFloat1 * 255.0F));
    int i = clamp((int)(paramFloat2 * 255.0F));
    int k = clamp((int)(paramFloat3 * 255.0F));
    return clamp((int)(255.0F * paramFloat4)) << 24 | j << 16 | i << 8 | k;
  }
  
  public static void socketSend(String paramString) {
    try {
      Socket socket = new Socket();
      this("127.0.0.1", 5327);
      OutputStream outputStream = socket.getOutputStream();
      outputStream.write(paramString.getBytes());
      outputStream.close();
    } catch (IOException iOException) {
      iOException.printStackTrace();
    } 
  }
  
  public int getInterpolatedColor(float[] paramArrayOffloat) {
    int j = clamp((int)((float)Math.pow(paramArrayOffloat[0], 0.45454545454545453D) * 255.0F));
    int i = clamp((int)((float)Math.pow(paramArrayOffloat[1], 0.45454545454545453D) * 255.0F));
    int k = clamp((int)((float)Math.pow(paramArrayOffloat[2], 0.45454545454545453D) * 255.0F));
    return clamp((int)(paramArrayOffloat[3] * 255.0F)) << 24 | j << 16 | i << 8 | k;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\motio\\utils\Utils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */