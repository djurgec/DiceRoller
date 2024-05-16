package androidx.emoji2.text.flatbuffer;

import java.nio.ByteBuffer;

public abstract class Utf8 {
  private static Utf8 DEFAULT;
  
  public static Utf8 getDefault() {
    if (DEFAULT == null)
      DEFAULT = new Utf8Safe(); 
    return DEFAULT;
  }
  
  public static void setDefault(Utf8 paramUtf8) {
    DEFAULT = paramUtf8;
  }
  
  public abstract String decodeUtf8(ByteBuffer paramByteBuffer, int paramInt1, int paramInt2);
  
  public abstract void encodeUtf8(CharSequence paramCharSequence, ByteBuffer paramByteBuffer);
  
  public abstract int encodedLength(CharSequence paramCharSequence);
  
  static class DecodeUtil {
    static void handleFourBytes(byte param1Byte1, byte param1Byte2, byte param1Byte3, byte param1Byte4, char[] param1ArrayOfchar, int param1Int) throws IllegalArgumentException {
      if (!isNotTrailingByte(param1Byte2) && (param1Byte1 << 28) + param1Byte2 + 112 >> 30 == 0 && !isNotTrailingByte(param1Byte3) && !isNotTrailingByte(param1Byte4)) {
        int i = (param1Byte1 & 0x7) << 18 | trailingByteValue(param1Byte2) << 12 | trailingByteValue(param1Byte3) << 6 | trailingByteValue(param1Byte4);
        param1ArrayOfchar[param1Int] = highSurrogate(i);
        param1ArrayOfchar[param1Int + 1] = lowSurrogate(i);
        return;
      } 
      throw new IllegalArgumentException("Invalid UTF-8");
    }
    
    static void handleOneByte(byte param1Byte, char[] param1ArrayOfchar, int param1Int) {
      param1ArrayOfchar[param1Int] = (char)param1Byte;
    }
    
    static void handleThreeBytes(byte param1Byte1, byte param1Byte2, byte param1Byte3, char[] param1ArrayOfchar, int param1Int) throws IllegalArgumentException {
      if (!isNotTrailingByte(param1Byte2) && (param1Byte1 != -32 || param1Byte2 >= -96) && (param1Byte1 != -19 || param1Byte2 < -96) && !isNotTrailingByte(param1Byte3)) {
        param1ArrayOfchar[param1Int] = (char)((param1Byte1 & 0xF) << 12 | trailingByteValue(param1Byte2) << 6 | trailingByteValue(param1Byte3));
        return;
      } 
      throw new IllegalArgumentException("Invalid UTF-8");
    }
    
    static void handleTwoBytes(byte param1Byte1, byte param1Byte2, char[] param1ArrayOfchar, int param1Int) throws IllegalArgumentException {
      if (param1Byte1 >= -62) {
        if (!isNotTrailingByte(param1Byte2)) {
          param1ArrayOfchar[param1Int] = (char)((param1Byte1 & 0x1F) << 6 | trailingByteValue(param1Byte2));
          return;
        } 
        throw new IllegalArgumentException("Invalid UTF-8: Illegal trailing byte in 2 bytes utf");
      } 
      throw new IllegalArgumentException("Invalid UTF-8: Illegal leading byte in 2 bytes utf");
    }
    
    private static char highSurrogate(int param1Int) {
      return (char)((param1Int >>> 10) + 55232);
    }
    
    private static boolean isNotTrailingByte(byte param1Byte) {
      boolean bool;
      if (param1Byte > -65) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    static boolean isOneByte(byte param1Byte) {
      boolean bool;
      if (param1Byte >= 0) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    static boolean isThreeBytes(byte param1Byte) {
      boolean bool;
      if (param1Byte < -16) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    static boolean isTwoBytes(byte param1Byte) {
      boolean bool;
      if (param1Byte < -32) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    private static char lowSurrogate(int param1Int) {
      return (char)((param1Int & 0x3FF) + 56320);
    }
    
    private static int trailingByteValue(byte param1Byte) {
      return param1Byte & 0x3F;
    }
  }
  
  static class UnpairedSurrogateException extends IllegalArgumentException {
    UnpairedSurrogateException(int param1Int1, int param1Int2) {
      super("Unpaired surrogate at index " + param1Int1 + " of " + param1Int2);
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\emoji2\text\flatbuffer\Utf8.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */