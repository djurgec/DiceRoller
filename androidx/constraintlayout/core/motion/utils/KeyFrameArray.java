package androidx.constraintlayout.core.motion.utils;

import androidx.constraintlayout.core.motion.CustomAttribute;
import androidx.constraintlayout.core.motion.CustomVariable;
import java.io.PrintStream;
import java.util.Arrays;

public class KeyFrameArray {
  public static class CustomArray {
    private static final int EMPTY = 999;
    
    int count;
    
    int[] keys = new int[101];
    
    CustomAttribute[] values = new CustomAttribute[101];
    
    public CustomArray() {
      clear();
    }
    
    public void append(int param1Int, CustomAttribute param1CustomAttribute) {
      if (this.values[param1Int] != null)
        remove(param1Int); 
      this.values[param1Int] = param1CustomAttribute;
      int[] arrayOfInt = this.keys;
      int i = this.count;
      this.count = i + 1;
      arrayOfInt[i] = param1Int;
      Arrays.sort(arrayOfInt);
    }
    
    public void clear() {
      Arrays.fill(this.keys, 999);
      Arrays.fill((Object[])this.values, (Object)null);
      this.count = 0;
    }
    
    public void dump() {
      System.out.println("V: " + Arrays.toString(Arrays.copyOf(this.keys, this.count)));
      System.out.print("K: [");
      for (byte b = 0; b < this.count; b++) {
        String str;
        PrintStream printStream = System.out;
        StringBuilder stringBuilder = new StringBuilder();
        if (b == 0) {
          str = "";
        } else {
          str = ", ";
        } 
        printStream.print(stringBuilder.append(str).append(valueAt(b)).toString());
      } 
      System.out.println("]");
    }
    
    public int keyAt(int param1Int) {
      return this.keys[param1Int];
    }
    
    public void remove(int param1Int) {
      this.values[param1Int] = null;
      int i = 0;
      byte b = 0;
      while (true) {
        int j = this.count;
        if (b < j) {
          int[] arrayOfInt = this.keys;
          j = i;
          if (param1Int == arrayOfInt[b]) {
            arrayOfInt[b] = 999;
            j = i + 1;
          } 
          if (b != j)
            arrayOfInt[b] = arrayOfInt[j]; 
          i = j + 1;
          b++;
          continue;
        } 
        this.count = j - 1;
        return;
      } 
    }
    
    public int size() {
      return this.count;
    }
    
    public CustomAttribute valueAt(int param1Int) {
      return this.values[this.keys[param1Int]];
    }
  }
  
  public static class CustomVar {
    private static final int EMPTY = 999;
    
    int count;
    
    int[] keys = new int[101];
    
    CustomVariable[] values = new CustomVariable[101];
    
    public CustomVar() {
      clear();
    }
    
    public void append(int param1Int, CustomVariable param1CustomVariable) {
      if (this.values[param1Int] != null)
        remove(param1Int); 
      this.values[param1Int] = param1CustomVariable;
      int[] arrayOfInt = this.keys;
      int i = this.count;
      this.count = i + 1;
      arrayOfInt[i] = param1Int;
      Arrays.sort(arrayOfInt);
    }
    
    public void clear() {
      Arrays.fill(this.keys, 999);
      Arrays.fill((Object[])this.values, (Object)null);
      this.count = 0;
    }
    
    public void dump() {
      System.out.println("V: " + Arrays.toString(Arrays.copyOf(this.keys, this.count)));
      System.out.print("K: [");
      for (byte b = 0; b < this.count; b++) {
        String str;
        PrintStream printStream = System.out;
        StringBuilder stringBuilder = new StringBuilder();
        if (b == 0) {
          str = "";
        } else {
          str = ", ";
        } 
        printStream.print(stringBuilder.append(str).append(valueAt(b)).toString());
      } 
      System.out.println("]");
    }
    
    public int keyAt(int param1Int) {
      return this.keys[param1Int];
    }
    
    public void remove(int param1Int) {
      this.values[param1Int] = null;
      int i = 0;
      byte b = 0;
      while (true) {
        int j = this.count;
        if (b < j) {
          int[] arrayOfInt = this.keys;
          j = i;
          if (param1Int == arrayOfInt[b]) {
            arrayOfInt[b] = 999;
            j = i + 1;
          } 
          if (b != j)
            arrayOfInt[b] = arrayOfInt[j]; 
          i = j + 1;
          b++;
          continue;
        } 
        this.count = j - 1;
        return;
      } 
    }
    
    public int size() {
      return this.count;
    }
    
    public CustomVariable valueAt(int param1Int) {
      return this.values[this.keys[param1Int]];
    }
  }
  
  static class FloatArray {
    private static final int EMPTY = 999;
    
    int count;
    
    int[] keys = new int[101];
    
    float[][] values = new float[101][];
    
    public FloatArray() {
      clear();
    }
    
    public void append(int param1Int, float[] param1ArrayOffloat) {
      if (this.values[param1Int] != null)
        remove(param1Int); 
      this.values[param1Int] = param1ArrayOffloat;
      int[] arrayOfInt = this.keys;
      int i = this.count;
      this.count = i + 1;
      arrayOfInt[i] = param1Int;
      Arrays.sort(arrayOfInt);
    }
    
    public void clear() {
      Arrays.fill(this.keys, 999);
      Arrays.fill((Object[])this.values, (Object)null);
      this.count = 0;
    }
    
    public void dump() {
      System.out.println("V: " + Arrays.toString(Arrays.copyOf(this.keys, this.count)));
      System.out.print("K: [");
      for (byte b = 0; b < this.count; b++) {
        String str;
        PrintStream printStream = System.out;
        StringBuilder stringBuilder = new StringBuilder();
        if (b == 0) {
          str = "";
        } else {
          str = ", ";
        } 
        printStream.print(stringBuilder.append(str).append(Arrays.toString(valueAt(b))).toString());
      } 
      System.out.println("]");
    }
    
    public int keyAt(int param1Int) {
      return this.keys[param1Int];
    }
    
    public void remove(int param1Int) {
      this.values[param1Int] = null;
      int i = 0;
      byte b = 0;
      while (true) {
        int j = this.count;
        if (b < j) {
          int[] arrayOfInt = this.keys;
          j = i;
          if (param1Int == arrayOfInt[b]) {
            arrayOfInt[b] = 999;
            j = i + 1;
          } 
          if (b != j)
            arrayOfInt[b] = arrayOfInt[j]; 
          i = j + 1;
          b++;
          continue;
        } 
        this.count = j - 1;
        return;
      } 
    }
    
    public int size() {
      return this.count;
    }
    
    public float[] valueAt(int param1Int) {
      return this.values[this.keys[param1Int]];
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\motio\\utils\KeyFrameArray.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */