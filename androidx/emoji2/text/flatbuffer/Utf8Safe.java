package androidx.emoji2.text.flatbuffer;

import java.nio.ByteBuffer;

public final class Utf8Safe extends Utf8 {
  private static int computeEncodedLength(CharSequence paramCharSequence) {
    int i;
    int k;
    int n = paramCharSequence.length();
    int m = n;
    int j = 0;
    while (true) {
      i = m;
      k = j;
      if (j < n) {
        i = m;
        k = j;
        if (paramCharSequence.charAt(j) < '') {
          j++;
          continue;
        } 
      } 
      break;
    } 
    while (true) {
      j = i;
      if (k < n) {
        j = paramCharSequence.charAt(k);
        if (j < 2048) {
          i += 127 - j >>> 31;
          k++;
          continue;
        } 
        j = i + encodedLengthGeneral(paramCharSequence, k);
      } 
      break;
    } 
    if (j >= n)
      return j; 
    throw new IllegalArgumentException("UTF-8 length does not fit in int: " + (j + 4294967296L));
  }
  
  public static String decodeUtf8Array(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
    if ((paramInt1 | paramInt2 | paramArrayOfbyte.length - paramInt1 - paramInt2) >= 0) {
      int i = paramInt1 + paramInt2;
      char[] arrayOfChar = new char[paramInt2];
      for (paramInt2 = 0; paramInt1 < i; paramInt2++) {
        byte b = paramArrayOfbyte[paramInt1];
        if (!Utf8.DecodeUtil.isOneByte(b))
          break; 
        paramInt1++;
        Utf8.DecodeUtil.handleOneByte(b, arrayOfChar, paramInt2);
      } 
      while (paramInt1 < i) {
        int j = paramInt1 + 1;
        byte b = paramArrayOfbyte[paramInt1];
        if (Utf8.DecodeUtil.isOneByte(b)) {
          paramInt1 = paramInt2 + 1;
          Utf8.DecodeUtil.handleOneByte(b, arrayOfChar, paramInt2);
          paramInt2 = j;
          while (paramInt2 < i) {
            byte b1 = paramArrayOfbyte[paramInt2];
            if (!Utf8.DecodeUtil.isOneByte(b1))
              break; 
            paramInt2++;
            Utf8.DecodeUtil.handleOneByte(b1, arrayOfChar, paramInt1);
            paramInt1++;
          } 
          j = paramInt1;
          paramInt1 = paramInt2;
          paramInt2 = j;
          continue;
        } 
        if (Utf8.DecodeUtil.isTwoBytes(b)) {
          if (j < i) {
            Utf8.DecodeUtil.handleTwoBytes(b, paramArrayOfbyte[j], arrayOfChar, paramInt2);
            paramInt1 = j + 1;
            paramInt2++;
            continue;
          } 
          throw new IllegalArgumentException("Invalid UTF-8");
        } 
        if (Utf8.DecodeUtil.isThreeBytes(b)) {
          if (j < i - 1) {
            paramInt1 = j + 1;
            Utf8.DecodeUtil.handleThreeBytes(b, paramArrayOfbyte[j], paramArrayOfbyte[paramInt1], arrayOfChar, paramInt2);
            paramInt1++;
            paramInt2++;
            continue;
          } 
          throw new IllegalArgumentException("Invalid UTF-8");
        } 
        if (j < i - 2) {
          paramInt1 = j + 1;
          byte b1 = paramArrayOfbyte[j];
          j = paramInt1 + 1;
          Utf8.DecodeUtil.handleFourBytes(b, b1, paramArrayOfbyte[paramInt1], paramArrayOfbyte[j], arrayOfChar, paramInt2);
          paramInt1 = j + 1;
          paramInt2 = paramInt2 + 1 + 1;
          continue;
        } 
        throw new IllegalArgumentException("Invalid UTF-8");
      } 
      return new String(arrayOfChar, 0, paramInt2);
    } 
    throw new ArrayIndexOutOfBoundsException(String.format("buffer length=%d, index=%d, size=%d", new Object[] { Integer.valueOf(paramArrayOfbyte.length), Integer.valueOf(paramInt1), Integer.valueOf(paramInt2) }));
  }
  
  public static String decodeUtf8Buffer(ByteBuffer paramByteBuffer, int paramInt1, int paramInt2) {
    if ((paramInt1 | paramInt2 | paramByteBuffer.limit() - paramInt1 - paramInt2) >= 0) {
      int j = paramInt1 + paramInt2;
      char[] arrayOfChar = new char[paramInt2];
      for (paramInt2 = 0; paramInt1 < j; paramInt2++) {
        byte b = paramByteBuffer.get(paramInt1);
        if (!Utf8.DecodeUtil.isOneByte(b))
          break; 
        paramInt1++;
        Utf8.DecodeUtil.handleOneByte(b, arrayOfChar, paramInt2);
      } 
      int i = paramInt2;
      paramInt2 = paramInt1;
      paramInt1 = i;
      while (paramInt2 < j) {
        i = paramInt2 + 1;
        byte b = paramByteBuffer.get(paramInt2);
        if (Utf8.DecodeUtil.isOneByte(b)) {
          paramInt2 = paramInt1 + 1;
          Utf8.DecodeUtil.handleOneByte(b, arrayOfChar, paramInt1);
          paramInt1 = paramInt2;
          paramInt2 = i;
          while (paramInt2 < j) {
            byte b1 = paramByteBuffer.get(paramInt2);
            if (!Utf8.DecodeUtil.isOneByte(b1))
              break; 
            paramInt2++;
            Utf8.DecodeUtil.handleOneByte(b1, arrayOfChar, paramInt1);
            paramInt1++;
          } 
          continue;
        } 
        if (Utf8.DecodeUtil.isTwoBytes(b)) {
          if (i < j) {
            Utf8.DecodeUtil.handleTwoBytes(b, paramByteBuffer.get(i), arrayOfChar, paramInt1);
            paramInt2 = i + 1;
            paramInt1++;
            continue;
          } 
          throw new IllegalArgumentException("Invalid UTF-8");
        } 
        if (Utf8.DecodeUtil.isThreeBytes(b)) {
          if (i < j - 1) {
            paramInt2 = i + 1;
            Utf8.DecodeUtil.handleThreeBytes(b, paramByteBuffer.get(i), paramByteBuffer.get(paramInt2), arrayOfChar, paramInt1);
            paramInt2++;
            paramInt1++;
            continue;
          } 
          throw new IllegalArgumentException("Invalid UTF-8");
        } 
        if (i < j - 2) {
          paramInt2 = i + 1;
          byte b1 = paramByteBuffer.get(i);
          i = paramInt2 + 1;
          Utf8.DecodeUtil.handleFourBytes(b, b1, paramByteBuffer.get(paramInt2), paramByteBuffer.get(i), arrayOfChar, paramInt1);
          paramInt2 = i + 1;
          paramInt1 = paramInt1 + 1 + 1;
          continue;
        } 
        throw new IllegalArgumentException("Invalid UTF-8");
      } 
      return new String(arrayOfChar, 0, paramInt1);
    } 
    throw new ArrayIndexOutOfBoundsException(String.format("buffer limit=%d, index=%d, limit=%d", new Object[] { Integer.valueOf(paramByteBuffer.limit()), Integer.valueOf(paramInt1), Integer.valueOf(paramInt2) }));
  }
  
  private static int encodeUtf8Array(CharSequence paramCharSequence, byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
    int j = paramCharSequence.length();
    int i = 0;
    int k = paramInt1 + paramInt2;
    paramInt2 = i;
    while (paramInt2 < j && paramInt2 + paramInt1 < k) {
      i = paramCharSequence.charAt(paramInt2);
      if (i < 128) {
        paramArrayOfbyte[paramInt1 + paramInt2] = (byte)i;
        paramInt2++;
      } 
    } 
    if (paramInt2 == j)
      return paramInt1 + j; 
    paramInt1 += paramInt2;
    while (paramInt2 < j) {
      char c = paramCharSequence.charAt(paramInt2);
      if (c < '' && paramInt1 < k) {
        paramArrayOfbyte[paramInt1] = (byte)c;
        paramInt1++;
      } else if (c < 'ࠀ' && paramInt1 <= k - 2) {
        i = paramInt1 + 1;
        paramArrayOfbyte[paramInt1] = (byte)(c >>> 6 | 0x3C0);
        paramInt1 = i + 1;
        paramArrayOfbyte[i] = (byte)(c & 0x3F | 0x80);
      } else if ((c < '?' || '?' < c) && paramInt1 <= k - 3) {
        i = paramInt1 + 1;
        paramArrayOfbyte[paramInt1] = (byte)(c >>> 12 | 0x1E0);
        paramInt1 = i + 1;
        paramArrayOfbyte[i] = (byte)(c >>> 6 & 0x3F | 0x80);
        paramArrayOfbyte[paramInt1] = (byte)(c & 0x3F | 0x80);
        paramInt1++;
      } else if (paramInt1 <= k - 4) {
        i = paramInt2;
        if (paramInt2 + 1 != paramCharSequence.length()) {
          char c1 = paramCharSequence.charAt(++paramInt2);
          i = paramInt2;
          if (Character.isSurrogatePair(c, c1)) {
            i = Character.toCodePoint(c, c1);
            int m = paramInt1 + 1;
            paramArrayOfbyte[paramInt1] = (byte)(i >>> 18 | 0xF0);
            paramInt1 = m + 1;
            paramArrayOfbyte[m] = (byte)(i >>> 12 & 0x3F | 0x80);
            m = paramInt1 + 1;
            paramArrayOfbyte[paramInt1] = (byte)(i >>> 6 & 0x3F | 0x80);
            paramInt1 = m + 1;
            paramArrayOfbyte[m] = (byte)(i & 0x3F | 0x80);
          } else {
            throw new UnpairedSurrogateException(i - 1, j);
          } 
        } else {
          throw new UnpairedSurrogateException(i - 1, j);
        } 
      } else {
        if ('?' <= c && c <= '?' && (paramInt2 + 1 == paramCharSequence.length() || !Character.isSurrogatePair(c, paramCharSequence.charAt(paramInt2 + 1))))
          throw new UnpairedSurrogateException(paramInt2, j); 
        throw new ArrayIndexOutOfBoundsException("Failed writing " + c + " at index " + paramInt1);
      } 
      paramInt2++;
    } 
    return paramInt1;
  }
  
  private static void encodeUtf8Buffer(CharSequence paramCharSequence, ByteBuffer paramByteBuffer) {
    int k = paramCharSequence.length();
    int j = paramByteBuffer.position();
    byte b1 = 0;
    while (b1 < k) {
      int m = j;
      byte b = b1;
      try {
        char c = paramCharSequence.charAt(b1);
        if (c < '') {
          m = j;
          b = b1;
          paramByteBuffer.put(j + b1, (byte)c);
          b1++;
        } 
      } catch (IndexOutOfBoundsException indexOutOfBoundsException) {
        b1 = b;
        // Byte code: goto -> 624
      } 
    } 
    if (b1 == k) {
      int m = j;
      byte b = b1;
      paramByteBuffer.position(j + b1);
      return;
    } 
    for (j += b1; b1 < k; j++) {
      int n = j;
      int m = b1;
      char c = paramCharSequence.charAt(b1);
      if (c < '') {
        n = j;
        m = b1;
        paramByteBuffer.put(j, (byte)c);
        continue;
      } 
      if (c < 'ࠀ') {
        n = j + 1;
        byte b = (byte)(c >>> 6 | 0xC0);
        m = n;
        try {
          paramByteBuffer.put(j, b);
          m = n;
          paramByteBuffer.put(n, (byte)(c & 0x3F | 0x80));
          j = n;
        } catch (IndexOutOfBoundsException indexOutOfBoundsException) {
          n = m;
        } 
        continue;
      } 
      if (c < '?' || '?' < c) {
        int i2 = j + 1;
        byte b = (byte)(c >>> 12 | 0xE0);
        m = i2;
        paramByteBuffer.put(j, b);
        j = i2 + 1;
        b = (byte)(c >>> 6 & 0x3F | 0x80);
        n = j;
        m = b1;
        paramByteBuffer.put(i2, b);
        n = j;
        m = b1;
        paramByteBuffer.put(j, (byte)(c & 0x3F | 0x80));
        continue;
      } 
      int i1 = b1;
      if (b1 + 1 != k) {
        b1++;
        n = j;
        m = b1;
        char c1 = paramCharSequence.charAt(b1);
        n = j;
        m = b1;
        i1 = b1;
        if (Character.isSurrogatePair(c, c1)) {
          n = j;
          m = b1;
          int i2 = Character.toCodePoint(c, c1);
          i1 = j + 1;
          byte b = (byte)(i2 >>> 18 | 0xF0);
          m = i1;
          try {
            paramByteBuffer.put(j, b);
            j = i1 + 1;
            b = (byte)(i2 >>> 12 & 0x3F | 0x80);
            n = j;
            m = b1;
            paramByteBuffer.put(i1, b);
            n = j + 1;
            b = (byte)(i2 >>> 6 & 0x3F | 0x80);
            m = n;
            paramByteBuffer.put(j, b);
            m = n;
            paramByteBuffer.put(n, (byte)(i2 & 0x3F | 0x80));
            j = n;
          } catch (IndexOutOfBoundsException indexOutOfBoundsException) {
            n = m;
            // Byte code: goto -> 624
          } 
          continue;
        } 
      } 
      n = j;
      m = i1;
      UnpairedSurrogateException unpairedSurrogateException = new UnpairedSurrogateException();
      n = j;
      m = i1;
      this(i1, k);
      n = j;
      m = i1;
      throw unpairedSurrogateException;
      b1++;
    } 
    int i = j;
    byte b2 = b1;
    paramByteBuffer.position(j);
  }
  
  private static int encodedLengthGeneral(CharSequence paramCharSequence, int paramInt) {
    int j = paramCharSequence.length();
    int i = 0;
    while (paramInt < j) {
      int k;
      char c = paramCharSequence.charAt(paramInt);
      if (c < 'ࠀ') {
        i += 127 - c >>> 31;
        k = paramInt;
      } else {
        int m = i + 2;
        i = m;
        k = paramInt;
        if ('?' <= c) {
          i = m;
          k = paramInt;
          if (c <= '?')
            if (Character.codePointAt(paramCharSequence, paramInt) >= 65536) {
              k = paramInt + 1;
              i = m;
            } else {
              throw new UnpairedSurrogateException(paramInt, j);
            }  
        } 
      } 
      paramInt = k + 1;
    } 
    return i;
  }
  
  public String decodeUtf8(ByteBuffer paramByteBuffer, int paramInt1, int paramInt2) throws IllegalArgumentException {
    return paramByteBuffer.hasArray() ? decodeUtf8Array(paramByteBuffer.array(), paramByteBuffer.arrayOffset() + paramInt1, paramInt2) : decodeUtf8Buffer(paramByteBuffer, paramInt1, paramInt2);
  }
  
  public void encodeUtf8(CharSequence paramCharSequence, ByteBuffer paramByteBuffer) {
    if (paramByteBuffer.hasArray()) {
      int i = paramByteBuffer.arrayOffset();
      paramByteBuffer.position(encodeUtf8Array(paramCharSequence, paramByteBuffer.array(), paramByteBuffer.position() + i, paramByteBuffer.remaining()) - i);
    } else {
      encodeUtf8Buffer(paramCharSequence, paramByteBuffer);
    } 
  }
  
  public int encodedLength(CharSequence paramCharSequence) {
    return computeEncodedLength(paramCharSequence);
  }
  
  static class UnpairedSurrogateException extends IllegalArgumentException {
    UnpairedSurrogateException(int param1Int1, int param1Int2) {
      super("Unpaired surrogate at index " + param1Int1 + " of " + param1Int2);
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\emoji2\text\flatbuffer\Utf8Safe.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */