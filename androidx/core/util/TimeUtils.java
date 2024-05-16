package androidx.core.util;

import java.io.PrintWriter;

public final class TimeUtils {
  public static final int HUNDRED_DAY_FIELD_LEN = 19;
  
  private static final int SECONDS_PER_DAY = 86400;
  
  private static final int SECONDS_PER_HOUR = 3600;
  
  private static final int SECONDS_PER_MINUTE = 60;
  
  private static char[] sFormatStr;
  
  private static final Object sFormatSync = new Object();
  
  static {
    sFormatStr = new char[24];
  }
  
  private static int accumField(int paramInt1, int paramInt2, boolean paramBoolean, int paramInt3) {
    return (paramInt1 > 99 || (paramBoolean && paramInt3 >= 3)) ? (paramInt2 + 3) : ((paramInt1 > 9 || (paramBoolean && paramInt3 >= 2)) ? (paramInt2 + 2) : ((paramBoolean || paramInt1 > 0) ? (paramInt2 + 1) : 0));
  }
  
  public static void formatDuration(long paramLong1, long paramLong2, PrintWriter paramPrintWriter) {
    if (paramLong1 == 0L) {
      paramPrintWriter.print("--");
      return;
    } 
    formatDuration(paramLong1 - paramLong2, paramPrintWriter, 0);
  }
  
  public static void formatDuration(long paramLong, PrintWriter paramPrintWriter) {
    formatDuration(paramLong, paramPrintWriter, 0);
  }
  
  public static void formatDuration(long paramLong, PrintWriter paramPrintWriter, int paramInt) {
    synchronized (sFormatSync) {
      paramInt = formatDurationLocked(paramLong, paramInt);
      String str = new String();
      this(sFormatStr, 0, paramInt);
      paramPrintWriter.print(str);
      return;
    } 
  }
  
  public static void formatDuration(long paramLong, StringBuilder paramStringBuilder) {
    synchronized (sFormatSync) {
      int i = formatDurationLocked(paramLong, 0);
      paramStringBuilder.append(sFormatStr, 0, i);
      return;
    } 
  }
  
  private static int formatDurationLocked(long paramLong, int paramInt) {
    byte b1;
    int j;
    boolean bool1;
    boolean bool2;
    if (sFormatStr.length < paramInt)
      sFormatStr = new char[paramInt]; 
    char[] arrayOfChar = sFormatStr;
    if (paramLong == 0L) {
      while (paramInt - 1 < 0)
        arrayOfChar[0] = ' '; 
      arrayOfChar[0] = '0';
      return 0 + 1;
    } 
    if (paramLong > 0L) {
      b1 = 43;
    } else {
      paramLong = -paramLong;
      b1 = 45;
    } 
    int n = (int)(paramLong % 1000L);
    int i = (int)Math.floor((paramLong / 1000L));
    if (i > 86400) {
      k = i / 86400;
      i -= 86400 * k;
    } else {
      k = 0;
    } 
    if (i > 3600) {
      bool1 = i / 3600;
      i -= bool1 * 3600;
    } else {
      bool1 = false;
    } 
    if (i > 60) {
      bool2 = i / 60;
      j = i - bool2 * 60;
    } else {
      bool2 = false;
      j = i;
    } 
    int m = 0;
    boolean bool3 = false;
    byte b3 = 3;
    boolean bool4 = false;
    if (paramInt != 0) {
      i = accumField(k, 1, false, 0);
      if (i > 0)
        bool4 = true; 
      i += accumField(bool1, 1, bool4, 2);
      if (i > 0) {
        bool4 = true;
      } else {
        bool4 = false;
      } 
      i += accumField(bool2, 1, bool4, 2);
      if (i > 0) {
        bool4 = true;
      } else {
        bool4 = false;
      } 
      int i1 = i + accumField(j, 1, bool4, 2);
      if (i1 > 0) {
        i = 3;
      } else {
        i = 0;
      } 
      i1 += accumField(n, 2, true, i) + 1;
      i = bool3;
      while (true) {
        m = i;
        if (i1 < paramInt) {
          arrayOfChar[i] = ' ';
          i++;
          i1++;
          continue;
        } 
        break;
      } 
    } 
    arrayOfChar[m] = b1;
    m++;
    if (paramInt != 0) {
      paramInt = 1;
    } else {
      paramInt = 0;
    } 
    boolean bool5 = true;
    byte b2 = 2;
    int k = printField(arrayOfChar, k, 'd', m, false, 0);
    if (k != m) {
      bool4 = true;
    } else {
      bool4 = false;
    } 
    if (paramInt != 0) {
      i = 2;
    } else {
      i = 0;
    } 
    k = printField(arrayOfChar, bool1, 'h', k, bool4, i);
    if (k != m) {
      bool4 = true;
    } else {
      bool4 = false;
    } 
    if (paramInt != 0) {
      i = 2;
    } else {
      i = 0;
    } 
    k = printField(arrayOfChar, bool2, 'm', k, bool4, i);
    if (k != m) {
      bool4 = bool5;
    } else {
      bool4 = false;
    } 
    if (paramInt != 0) {
      i = b2;
    } else {
      i = 0;
    } 
    i = printField(arrayOfChar, j, 's', k, bool4, i);
    if (paramInt != 0 && i != m) {
      paramInt = b3;
    } else {
      paramInt = 0;
    } 
    paramInt = printField(arrayOfChar, n, 'm', i, true, paramInt);
    arrayOfChar[paramInt] = 's';
    return paramInt + 1;
  }
  
  private static int printField(char[] paramArrayOfchar, int paramInt1, char paramChar, int paramInt2, boolean paramBoolean, int paramInt3) {
    // Byte code:
    //   0: iload #4
    //   2: ifne -> 12
    //   5: iload_3
    //   6: istore #6
    //   8: iload_1
    //   9: ifle -> 145
    //   12: iload #4
    //   14: ifeq -> 23
    //   17: iload #5
    //   19: iconst_3
    //   20: if_icmpge -> 35
    //   23: iload_1
    //   24: istore #6
    //   26: iload_3
    //   27: istore #7
    //   29: iload_1
    //   30: bipush #99
    //   32: if_icmple -> 64
    //   35: iload_1
    //   36: bipush #100
    //   38: idiv
    //   39: istore #6
    //   41: aload_0
    //   42: iload_3
    //   43: iload #6
    //   45: bipush #48
    //   47: iadd
    //   48: i2c
    //   49: castore
    //   50: iload_3
    //   51: iconst_1
    //   52: iadd
    //   53: istore #7
    //   55: iload_1
    //   56: iload #6
    //   58: bipush #100
    //   60: imul
    //   61: isub
    //   62: istore #6
    //   64: iload #4
    //   66: ifeq -> 75
    //   69: iload #5
    //   71: iconst_2
    //   72: if_icmpge -> 95
    //   75: iload #6
    //   77: bipush #9
    //   79: if_icmpgt -> 95
    //   82: iload #6
    //   84: istore #5
    //   86: iload #7
    //   88: istore_1
    //   89: iload_3
    //   90: iload #7
    //   92: if_icmpeq -> 124
    //   95: iload #6
    //   97: bipush #10
    //   99: idiv
    //   100: istore_3
    //   101: aload_0
    //   102: iload #7
    //   104: iload_3
    //   105: bipush #48
    //   107: iadd
    //   108: i2c
    //   109: castore
    //   110: iload #7
    //   112: iconst_1
    //   113: iadd
    //   114: istore_1
    //   115: iload #6
    //   117: iload_3
    //   118: bipush #10
    //   120: imul
    //   121: isub
    //   122: istore #5
    //   124: aload_0
    //   125: iload_1
    //   126: iload #5
    //   128: bipush #48
    //   130: iadd
    //   131: i2c
    //   132: castore
    //   133: iinc #1, 1
    //   136: aload_0
    //   137: iload_1
    //   138: iload_2
    //   139: castore
    //   140: iload_1
    //   141: iconst_1
    //   142: iadd
    //   143: istore #6
    //   145: iload #6
    //   147: ireturn
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\cor\\util\TimeUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */