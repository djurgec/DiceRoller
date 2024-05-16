package androidx.constraintlayout.motion.widget;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.CharBuffer;

public class Debug {
  public static void dumpLayoutParams(ViewGroup.LayoutParams paramLayoutParams, String paramString) {
    StackTraceElement stackTraceElement = (new Throwable()).getStackTrace()[1];
    String str2 = stackTraceElement.getFileName();
    int i = stackTraceElement.getLineNumber();
    paramString = (new StringBuilder(String.valueOf(str2).length() + 18 + String.valueOf(paramString).length())).append(".(").append(str2).append(":").append(i).append(") ").append(paramString).append("  ").toString();
    PrintStream printStream2 = System.out;
    str2 = paramLayoutParams.getClass().getName();
    printStream2.println((new StringBuilder(String.valueOf(paramString).length() + 28 + String.valueOf(str2).length())).append(" >>>>>>>>>>>>>>>>>>. dump ").append(paramString).append("  ").append(str2).toString());
    Field[] arrayOfField = paramLayoutParams.getClass().getFields();
    for (i = 0; i < arrayOfField.length; i++) {
      Field field = arrayOfField[i];
      try {
        Object object = field.get(paramLayoutParams);
        String str = field.getName();
        if (str.contains("To") && !object.toString().equals("-1")) {
          PrintStream printStream = System.out;
          object = String.valueOf(object);
          int j = String.valueOf(paramString).length();
          int m = String.valueOf(str).length();
          int k = String.valueOf(object).length();
          StringBuilder stringBuilder = new StringBuilder();
          this(j + 8 + m + k);
          printStream.println(stringBuilder.append(paramString).append("       ").append(str).append(" ").append((String)object).toString());
        } 
      } catch (IllegalAccessException illegalAccessException) {}
    } 
    PrintStream printStream1 = System.out;
    String str1 = String.valueOf(paramString);
    if (str1.length() != 0) {
      str1 = " <<<<<<<<<<<<<<<<< dump ".concat(str1);
    } else {
      str1 = new String(" <<<<<<<<<<<<<<<<< dump ");
    } 
    printStream1.println(str1);
  }
  
  public static void dumpLayoutParams(ViewGroup paramViewGroup, String paramString) {
    StackTraceElement stackTraceElement = (new Throwable()).getStackTrace()[1];
    String str2 = stackTraceElement.getFileName();
    int i = stackTraceElement.getLineNumber();
    String str1 = (new StringBuilder(String.valueOf(str2).length() + 18 + String.valueOf(paramString).length())).append(".(").append(str2).append(":").append(i).append(") ").append(paramString).append("  ").toString();
    int j = paramViewGroup.getChildCount();
    System.out.println((new StringBuilder(String.valueOf(paramString).length() + 21)).append(paramString).append(" children ").append(j).toString());
    for (i = 0; i < j; i++) {
      View view = paramViewGroup.getChildAt(i);
      PrintStream printStream = System.out;
      paramString = getName(view);
      printStream.println((new StringBuilder(String.valueOf(str1).length() + 5 + String.valueOf(paramString).length())).append(str1).append("     ").append(paramString).toString());
      ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
      Field[] arrayOfField = layoutParams.getClass().getFields();
      for (byte b = 0; b < arrayOfField.length; b++) {
        Field field = arrayOfField[b];
        try {
          Object object = field.get(layoutParams);
          if (field.getName().contains("To") && !object.toString().equals("-1")) {
            PrintStream printStream1 = System.out;
            String str3 = field.getName();
            String str4 = String.valueOf(object);
            int k = String.valueOf(str1).length();
            int n = String.valueOf(str3).length();
            int m = String.valueOf(str4).length();
            object = new StringBuilder();
            super(k + 8 + n + m);
            printStream1.println(object.append(str1).append("       ").append(str3).append(" ").append(str4).toString());
          } 
        } catch (IllegalAccessException illegalAccessException) {}
      } 
    } 
  }
  
  public static void dumpPoc(Object paramObject) {
    StackTraceElement stackTraceElement = (new Throwable()).getStackTrace()[1];
    String str1 = stackTraceElement.getFileName();
    int i = stackTraceElement.getLineNumber();
    str1 = (new StringBuilder(String.valueOf(str1).length() + 15)).append(".(").append(str1).append(":").append(i).append(")").toString();
    Class<?> clazz = paramObject.getClass();
    PrintStream printStream = System.out;
    String str3 = clazz.getName();
    printStream.println((new StringBuilder(String.valueOf(str1).length() + 35 + String.valueOf(str3).length())).append(str1).append("------------- ").append(str3).append(" --------------------").toString());
    Field[] arrayOfField = clazz.getFields();
    for (i = 0; i < arrayOfField.length; i++) {
      Field field = arrayOfField[i];
      try {
        Object object = field.get(paramObject);
        if (field.getName().startsWith("layout_constraint") && (!(object instanceof Integer) || !object.toString().equals("-1")) && (!(object instanceof Integer) || !object.toString().equals("0")) && (!(object instanceof Float) || !object.toString().equals("1.0")) && (!(object instanceof Float) || !object.toString().equals("0.5"))) {
          printStream = System.out;
          String str4 = field.getName();
          String str5 = String.valueOf(object);
          int j = String.valueOf(str1).length();
          int m = String.valueOf(str4).length();
          int k = String.valueOf(str5).length();
          object = new StringBuilder();
          super(j + 5 + m + k);
          printStream.println(object.append(str1).append("    ").append(str4).append(" ").append(str5).toString());
        } 
      } catch (IllegalAccessException illegalAccessException) {}
    } 
    paramObject = System.out;
    String str2 = clazz.getSimpleName();
    paramObject.println((new StringBuilder(String.valueOf(str1).length() + 35 + String.valueOf(str2).length())).append(str1).append("------------- ").append(str2).append(" --------------------").toString());
  }
  
  public static String getActionType(MotionEvent paramMotionEvent) {
    int i = paramMotionEvent.getAction();
    Field[] arrayOfField = MotionEvent.class.getFields();
    for (byte b = 0; b < arrayOfField.length; b++) {
      Field field = arrayOfField[b];
      try {
        if (Modifier.isStatic(field.getModifiers()) && field.getType().equals(int.class) && field.getInt(null) == i)
          return field.getName(); 
      } catch (IllegalAccessException illegalAccessException) {}
    } 
    return "---";
  }
  
  public static String getCallFrom(int paramInt) {
    StackTraceElement stackTraceElement = (new Throwable()).getStackTrace()[paramInt + 2];
    String str = stackTraceElement.getFileName();
    paramInt = stackTraceElement.getLineNumber();
    return (new StringBuilder(String.valueOf(str).length() + 15)).append(".(").append(str).append(":").append(paramInt).append(")").toString();
  }
  
  public static String getLoc() {
    StackTraceElement stackTraceElement = (new Throwable()).getStackTrace()[1];
    String str1 = stackTraceElement.getFileName();
    int i = stackTraceElement.getLineNumber();
    String str2 = stackTraceElement.getMethodName();
    return (new StringBuilder(String.valueOf(str1).length() + 18 + String.valueOf(str2).length())).append(".(").append(str1).append(":").append(i).append(") ").append(str2).append("()").toString();
  }
  
  public static String getLocation() {
    StackTraceElement stackTraceElement = (new Throwable()).getStackTrace()[1];
    String str = stackTraceElement.getFileName();
    int i = stackTraceElement.getLineNumber();
    return (new StringBuilder(String.valueOf(str).length() + 15)).append(".(").append(str).append(":").append(i).append(")").toString();
  }
  
  public static String getLocation2() {
    StackTraceElement stackTraceElement = (new Throwable()).getStackTrace()[2];
    String str = stackTraceElement.getFileName();
    int i = stackTraceElement.getLineNumber();
    return (new StringBuilder(String.valueOf(str).length() + 15)).append(".(").append(str).append(":").append(i).append(")").toString();
  }
  
  public static String getName(Context paramContext, int paramInt) {
    if (paramInt != -1)
      try {
        return paramContext.getResources().getResourceEntryName(paramInt);
      } catch (Exception exception) {
        return (new StringBuilder(12)).append("?").append(paramInt).toString();
      }  
    return "UNKNOWN";
  }
  
  public static String getName(Context paramContext, int[] paramArrayOfint) {
    try {
      int i = paramArrayOfint.length;
      StringBuilder stringBuilder = new StringBuilder();
      this(12);
      String str = stringBuilder.append(i).append("[").toString();
      for (i = 0; i < paramArrayOfint.length; i++) {
        String str1 = String.valueOf(str);
        if (i == 0) {
          str = "";
        } else {
          str = " ";
        } 
        str = String.valueOf(str);
        if (str.length() != 0) {
          str = str1.concat(str);
        } else {
          str = new String(str1);
        } 
        try {
          str1 = paramContext.getResources().getResourceEntryName(paramArrayOfint[i]);
        } catch (android.content.res.Resources.NotFoundException notFoundException) {
          int j = paramArrayOfint[i];
          StringBuilder stringBuilder1 = new StringBuilder();
          this(14);
          str1 = stringBuilder1.append("? ").append(j).append(" ").toString();
        } 
        str = String.valueOf(str);
        str1 = String.valueOf(str1);
        if (str1.length() != 0) {
          str = str.concat(str1);
        } else {
          str = new String(str);
        } 
      } 
      return String.valueOf(str).concat("]");
    } catch (Exception exception) {
      Log.v("DEBUG", exception.toString());
      return "UNKNOWN";
    } 
  }
  
  public static String getName(View paramView) {
    try {
      return paramView.getContext().getResources().getResourceEntryName(paramView.getId());
    } catch (Exception exception) {
      return "UNKNOWN";
    } 
  }
  
  public static String getState(MotionLayout paramMotionLayout, int paramInt) {
    return getState(paramMotionLayout, paramInt, -1);
  }
  
  public static String getState(MotionLayout paramMotionLayout, int paramInt1, int paramInt2) {
    if (paramInt1 == -1)
      return "UNDEFINED"; 
    String str2 = paramMotionLayout.getContext().getResources().getResourceEntryName(paramInt1);
    String str1 = str2;
    if (paramInt2 != -1) {
      String str = str2;
      if (str2.length() > paramInt2)
        str = str2.replaceAll("([^_])[aeiou]+", "$1"); 
      str1 = str;
      if (str.length() > paramInt2) {
        paramInt1 = str.replaceAll("[^_]", "").length();
        str1 = str;
        if (paramInt1 > 0)
          str1 = str.replaceAll(String.valueOf(CharBuffer.allocate((str.length() - paramInt2) / paramInt1).toString().replace(false, '.')).concat("_"), "_"); 
      } 
    } 
    return str1;
  }
  
  public static void logStack(String paramString1, String paramString2, int paramInt) {
    StackTraceElement[] arrayOfStackTraceElement = (new Throwable()).getStackTrace();
    String str = " ";
    int i = Math.min(paramInt, arrayOfStackTraceElement.length - 1);
    for (paramInt = 1; paramInt <= i; paramInt++) {
      StackTraceElement stackTraceElement = arrayOfStackTraceElement[paramInt];
      String str1 = arrayOfStackTraceElement[paramInt].getFileName();
      int j = arrayOfStackTraceElement[paramInt].getLineNumber();
      String str2 = arrayOfStackTraceElement[paramInt].getMethodName();
      str1 = (new StringBuilder(String.valueOf(str1).length() + 16 + String.valueOf(str2).length())).append(".(").append(str1).append(":").append(j).append(") ").append(str2).toString();
      str = String.valueOf(str).concat(" ");
      Log.v(paramString1, (new StringBuilder(String.valueOf(paramString2).length() + String.valueOf(str).length() + String.valueOf(str1).length() + String.valueOf(str).length())).append(paramString2).append(str).append(str1).append(str).toString());
    } 
  }
  
  public static void printStack(String paramString, int paramInt) {
    StackTraceElement[] arrayOfStackTraceElement = (new Throwable()).getStackTrace();
    String str = " ";
    int i = Math.min(paramInt, arrayOfStackTraceElement.length - 1);
    for (paramInt = 1; paramInt <= i; paramInt++) {
      StackTraceElement stackTraceElement = arrayOfStackTraceElement[paramInt];
      String str1 = arrayOfStackTraceElement[paramInt].getFileName();
      int j = arrayOfStackTraceElement[paramInt].getLineNumber();
      str1 = (new StringBuilder(String.valueOf(str1).length() + 16)).append(".(").append(str1).append(":").append(j).append(") ").toString();
      str = String.valueOf(str).concat(" ");
      System.out.println((new StringBuilder(String.valueOf(paramString).length() + String.valueOf(str).length() + String.valueOf(str1).length() + String.valueOf(str).length())).append(paramString).append(str).append(str1).append(str).toString());
    } 
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\motion\widget\Debug.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */