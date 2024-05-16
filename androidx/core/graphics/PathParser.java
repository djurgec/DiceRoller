package androidx.core.graphics;

import android.graphics.Path;
import android.util.Log;
import java.util.ArrayList;

public class PathParser {
  private static final String LOGTAG = "PathParser";
  
  private static void addNode(ArrayList<PathDataNode> paramArrayList, char paramChar, float[] paramArrayOffloat) {
    paramArrayList.add(new PathDataNode(paramChar, paramArrayOffloat));
  }
  
  public static boolean canMorph(PathDataNode[] paramArrayOfPathDataNode1, PathDataNode[] paramArrayOfPathDataNode2) {
    if (paramArrayOfPathDataNode1 == null || paramArrayOfPathDataNode2 == null)
      return false; 
    if (paramArrayOfPathDataNode1.length != paramArrayOfPathDataNode2.length)
      return false; 
    for (byte b = 0; b < paramArrayOfPathDataNode1.length; b++) {
      if ((paramArrayOfPathDataNode1[b]).mType != (paramArrayOfPathDataNode2[b]).mType || (paramArrayOfPathDataNode1[b]).mParams.length != (paramArrayOfPathDataNode2[b]).mParams.length)
        return false; 
    } 
    return true;
  }
  
  static float[] copyOfRange(float[] paramArrayOffloat, int paramInt1, int paramInt2) {
    if (paramInt1 <= paramInt2) {
      int i = paramArrayOffloat.length;
      if (paramInt1 >= 0 && paramInt1 <= i) {
        paramInt2 -= paramInt1;
        i = Math.min(paramInt2, i - paramInt1);
        float[] arrayOfFloat = new float[paramInt2];
        System.arraycopy(paramArrayOffloat, paramInt1, arrayOfFloat, 0, i);
        return arrayOfFloat;
      } 
      throw new ArrayIndexOutOfBoundsException();
    } 
    throw new IllegalArgumentException();
  }
  
  public static PathDataNode[] createNodesFromPathData(String paramString) {
    if (paramString == null)
      return null; 
    int i = 0;
    int j = 1;
    ArrayList<PathDataNode> arrayList = new ArrayList();
    while (j < paramString.length()) {
      j = nextStart(paramString, j);
      String str = paramString.substring(i, j).trim();
      if (str.length() > 0) {
        float[] arrayOfFloat = getFloats(str);
        addNode(arrayList, str.charAt(0), arrayOfFloat);
      } 
      i = j;
      j++;
    } 
    if (j - i == 1 && i < paramString.length())
      addNode(arrayList, paramString.charAt(i), new float[0]); 
    return arrayList.<PathDataNode>toArray(new PathDataNode[arrayList.size()]);
  }
  
  public static Path createPathFromPathData(String paramString) {
    Path path = new Path();
    PathDataNode[] arrayOfPathDataNode = createNodesFromPathData(paramString);
    if (arrayOfPathDataNode != null)
      try {
        PathDataNode.nodesToPath(arrayOfPathDataNode, path);
        return path;
      } catch (RuntimeException runtimeException) {
        throw new RuntimeException("Error in parsing " + paramString, runtimeException);
      }  
    return null;
  }
  
  public static PathDataNode[] deepCopyNodes(PathDataNode[] paramArrayOfPathDataNode) {
    if (paramArrayOfPathDataNode == null)
      return null; 
    PathDataNode[] arrayOfPathDataNode = new PathDataNode[paramArrayOfPathDataNode.length];
    for (byte b = 0; b < paramArrayOfPathDataNode.length; b++)
      arrayOfPathDataNode[b] = new PathDataNode(paramArrayOfPathDataNode[b]); 
    return arrayOfPathDataNode;
  }
  
  private static void extract(String paramString, int paramInt, ExtractFloatResult paramExtractFloatResult) {
    int i = paramInt;
    boolean bool2 = false;
    paramExtractFloatResult.mEndWithNegOrDot = false;
    boolean bool1 = false;
    boolean bool3;
    for (bool3 = false; i < paramString.length(); bool3 = bool5) {
      boolean bool4;
      boolean bool5;
      boolean bool6;
      boolean bool7 = false;
      switch (paramString.charAt(i)) {
        default:
          bool4 = bool2;
          bool6 = bool1;
          bool5 = bool7;
          break;
        case 'E':
        case 'e':
          bool5 = true;
          bool4 = bool2;
          bool6 = bool1;
          break;
        case '.':
          if (!bool1) {
            bool6 = true;
            bool4 = bool2;
            bool5 = bool7;
            break;
          } 
          bool4 = true;
          paramExtractFloatResult.mEndWithNegOrDot = true;
          bool6 = bool1;
          bool5 = bool7;
          break;
        case '-':
          bool4 = bool2;
          bool6 = bool1;
          bool5 = bool7;
          if (i != paramInt) {
            bool4 = bool2;
            bool6 = bool1;
            bool5 = bool7;
            if (!bool3) {
              bool4 = true;
              paramExtractFloatResult.mEndWithNegOrDot = true;
              bool6 = bool1;
              bool5 = bool7;
            } 
          } 
          break;
        case ' ':
        case ',':
          bool4 = true;
          bool5 = bool7;
          bool6 = bool1;
          break;
      } 
      if (bool4)
        break; 
      i++;
      bool2 = bool4;
      bool1 = bool6;
    } 
    paramExtractFloatResult.mEndPosition = i;
  }
  
  private static float[] getFloats(String paramString) {
    if (paramString.charAt(0) == 'z' || paramString.charAt(0) == 'Z')
      return new float[0]; 
    try {
      float[] arrayOfFloat = new float[paramString.length()];
      int j = 0;
      int i = 1;
      ExtractFloatResult extractFloatResult = new ExtractFloatResult();
      this();
      int k = paramString.length();
      while (i < k) {
        extract(paramString, i, extractFloatResult);
        int n = extractFloatResult.mEndPosition;
        int m = j;
        if (i < n) {
          arrayOfFloat[j] = Float.parseFloat(paramString.substring(i, n));
          m = j + 1;
        } 
        if (extractFloatResult.mEndWithNegOrDot) {
          i = n;
          j = m;
          continue;
        } 
        i = n + 1;
        j = m;
      } 
      return copyOfRange(arrayOfFloat, 0, j);
    } catch (NumberFormatException numberFormatException) {
      throw new RuntimeException("error in parsing \"" + paramString + "\"", numberFormatException);
    } 
  }
  
  public static boolean interpolatePathDataNodes(PathDataNode[] paramArrayOfPathDataNode1, PathDataNode[] paramArrayOfPathDataNode2, PathDataNode[] paramArrayOfPathDataNode3, float paramFloat) {
    if (paramArrayOfPathDataNode1 != null && paramArrayOfPathDataNode2 != null && paramArrayOfPathDataNode3 != null) {
      if (paramArrayOfPathDataNode1.length == paramArrayOfPathDataNode2.length && paramArrayOfPathDataNode2.length == paramArrayOfPathDataNode3.length) {
        if (!canMorph(paramArrayOfPathDataNode2, paramArrayOfPathDataNode3))
          return false; 
        for (byte b = 0; b < paramArrayOfPathDataNode1.length; b++)
          paramArrayOfPathDataNode1[b].interpolatePathDataNode(paramArrayOfPathDataNode2[b], paramArrayOfPathDataNode3[b], paramFloat); 
        return true;
      } 
      throw new IllegalArgumentException("The nodes to be interpolated and resulting nodes must have the same length");
    } 
    throw new IllegalArgumentException("The nodes to be interpolated and resulting nodes cannot be null");
  }
  
  private static int nextStart(String paramString, int paramInt) {
    while (paramInt < paramString.length()) {
      char c = paramString.charAt(paramInt);
      if (((c - 65) * (c - 90) <= 0 || (c - 97) * (c - 122) <= 0) && c != 'e' && c != 'E')
        return paramInt; 
      paramInt++;
    } 
    return paramInt;
  }
  
  public static void updateNodes(PathDataNode[] paramArrayOfPathDataNode1, PathDataNode[] paramArrayOfPathDataNode2) {
    for (byte b = 0; b < paramArrayOfPathDataNode2.length; b++) {
      (paramArrayOfPathDataNode1[b]).mType = (paramArrayOfPathDataNode2[b]).mType;
      for (byte b1 = 0; b1 < (paramArrayOfPathDataNode2[b]).mParams.length; b1++)
        (paramArrayOfPathDataNode1[b]).mParams[b1] = (paramArrayOfPathDataNode2[b]).mParams[b1]; 
    } 
  }
  
  private static class ExtractFloatResult {
    int mEndPosition;
    
    boolean mEndWithNegOrDot;
  }
  
  public static class PathDataNode {
    public float[] mParams;
    
    public char mType;
    
    PathDataNode(char param1Char, float[] param1ArrayOffloat) {
      this.mType = param1Char;
      this.mParams = param1ArrayOffloat;
    }
    
    PathDataNode(PathDataNode param1PathDataNode) {
      this.mType = param1PathDataNode.mType;
      float[] arrayOfFloat = param1PathDataNode.mParams;
      this.mParams = PathParser.copyOfRange(arrayOfFloat, 0, arrayOfFloat.length);
    }
    
    private static void addCommand(Path param1Path, float[] param1ArrayOffloat1, char param1Char1, char param1Char2, float[] param1ArrayOffloat2) {
      byte b;
      Path path = param1Path;
      float f4 = param1ArrayOffloat1[0];
      float f3 = param1ArrayOffloat1[1];
      float f5 = param1ArrayOffloat1[2];
      float f6 = param1ArrayOffloat1[3];
      float f2 = param1ArrayOffloat1[4];
      float f1 = param1ArrayOffloat1[5];
      switch (param1Char2) {
        default:
          b = 2;
          break;
        case 'Z':
        case 'z':
          param1Path.close();
          f4 = f2;
          f3 = f1;
          f5 = f2;
          f6 = f1;
          path.moveTo(f4, f3);
          b = 2;
          break;
        case 'Q':
        case 'S':
        case 'q':
        case 's':
          b = 4;
          break;
        case 'L':
        case 'M':
        case 'T':
        case 'l':
        case 'm':
        case 't':
          b = 2;
          break;
        case 'H':
        case 'V':
        case 'h':
        case 'v':
          b = 1;
          break;
        case 'C':
        case 'c':
          b = 6;
          break;
        case 'A':
        case 'a':
          b = 7;
          break;
      } 
      int i = 0;
      float f8 = f5;
      float f7 = f6;
      f5 = f2;
      f2 = f3;
      f6 = f1;
      f1 = f4;
      while (i < param1ArrayOffloat2.length) {
        float f;
        boolean bool1;
        boolean bool2;
        switch (param1Char2) {
          default:
            f3 = f8;
            f4 = f7;
            break;
          case 'v':
            path.rLineTo(0.0F, param1ArrayOffloat2[i + 0]);
            f2 += param1ArrayOffloat2[i + 0];
            f3 = f8;
            f4 = f7;
            break;
          case 't':
            f4 = 0.0F;
            f3 = 0.0F;
            if (param1Char1 == 'q' || param1Char1 == 't' || param1Char1 == 'Q' || param1Char1 == 'T') {
              f4 = f1 - f8;
              f3 = f2 - f7;
            } 
            path.rQuadTo(f4, f3, param1ArrayOffloat2[i + 0], param1ArrayOffloat2[i + 1]);
            f7 = f1 + param1ArrayOffloat2[i + 0];
            f8 = f2 + param1ArrayOffloat2[i + 1];
            f4 = f1 + f4;
            f = f2 + f3;
            f2 = f8;
            f1 = f7;
            f3 = f4;
            f4 = f;
            break;
          case 's':
            if (param1Char1 == 'c' || param1Char1 == 's' || param1Char1 == 'C' || param1Char1 == 'S') {
              f3 = f1 - f8;
              f4 = f2 - f7;
            } else {
              f3 = 0.0F;
              f4 = 0.0F;
            } 
            param1Path.rCubicTo(f3, f4, param1ArrayOffloat2[i + 0], param1ArrayOffloat2[i + 1], param1ArrayOffloat2[i + 2], param1ArrayOffloat2[i + 3]);
            f4 = param1ArrayOffloat2[i + 0];
            f7 = param1ArrayOffloat2[i + 1];
            f3 = f1 + param1ArrayOffloat2[i + 2];
            f8 = param1ArrayOffloat2[i + 3];
            f4 += f1;
            f7 = f2 + f7;
            f2 = f8 + f2;
            f1 = f3;
            f3 = f4;
            f4 = f7;
            break;
          case 'q':
            path.rQuadTo(param1ArrayOffloat2[i + 0], param1ArrayOffloat2[i + 1], param1ArrayOffloat2[i + 2], param1ArrayOffloat2[i + 3]);
            f4 = param1ArrayOffloat2[i + 0];
            f7 = param1ArrayOffloat2[i + 1];
            f3 = f1 + param1ArrayOffloat2[i + 2];
            f8 = param1ArrayOffloat2[i + 3];
            f4 += f1;
            f7 = f2 + f7;
            f2 = f8 + f2;
            f1 = f3;
            f3 = f4;
            f4 = f7;
            break;
          case 'm':
            f1 += param1ArrayOffloat2[i + 0];
            f2 += param1ArrayOffloat2[i + 1];
            if (i > 0) {
              path.rLineTo(param1ArrayOffloat2[i + 0], param1ArrayOffloat2[i + 1]);
              f3 = f8;
              f4 = f7;
              break;
            } 
            path.rMoveTo(param1ArrayOffloat2[i + 0], param1ArrayOffloat2[i + 1]);
            f5 = f1;
            f6 = f2;
            f3 = f8;
            f4 = f7;
            break;
          case 'l':
            path.rLineTo(param1ArrayOffloat2[i + 0], param1ArrayOffloat2[i + 1]);
            f1 += param1ArrayOffloat2[i + 0];
            f2 += param1ArrayOffloat2[i + 1];
            f3 = f8;
            f4 = f7;
            break;
          case 'h':
            path.rLineTo(param1ArrayOffloat2[i + 0], 0.0F);
            f1 += param1ArrayOffloat2[i + 0];
            f3 = f8;
            f4 = f7;
            break;
          case 'c':
            param1Path.rCubicTo(param1ArrayOffloat2[i + 0], param1ArrayOffloat2[i + 1], param1ArrayOffloat2[i + 2], param1ArrayOffloat2[i + 3], param1ArrayOffloat2[i + 4], param1ArrayOffloat2[i + 5]);
            f4 = param1ArrayOffloat2[i + 2];
            f7 = param1ArrayOffloat2[i + 3];
            f3 = f1 + param1ArrayOffloat2[i + 4];
            f8 = param1ArrayOffloat2[i + 5];
            f4 += f1;
            f7 = f2 + f7;
            f2 = f8 + f2;
            f1 = f3;
            f3 = f4;
            f4 = f7;
            break;
          case 'a':
            f4 = param1ArrayOffloat2[i + 5];
            f3 = param1ArrayOffloat2[i + 6];
            f7 = param1ArrayOffloat2[i + 0];
            f8 = param1ArrayOffloat2[i + 1];
            f = param1ArrayOffloat2[i + 2];
            if (param1ArrayOffloat2[i + 3] != 0.0F) {
              bool1 = true;
            } else {
              bool1 = false;
            } 
            if (param1ArrayOffloat2[i + 4] != 0.0F) {
              bool2 = true;
            } else {
              bool2 = false;
            } 
            drawArc(param1Path, f1, f2, f4 + f1, f3 + f2, f7, f8, f, bool1, bool2);
            f1 += param1ArrayOffloat2[i + 5];
            f2 += param1ArrayOffloat2[i + 6];
            path = param1Path;
            f3 = f1;
            f4 = f2;
            break;
          case 'V':
            f2 = param1ArrayOffloat2[i + 0];
            path = param1Path;
            path.lineTo(f1, f2);
            f2 = param1ArrayOffloat2[i + 0];
            f3 = f8;
            f4 = f7;
            break;
          case 'T':
            f4 = f1;
            f3 = f2;
            if (param1Char1 == 'q' || param1Char1 == 't' || param1Char1 == 'Q' || param1Char1 == 'T') {
              f4 = f1 * 2.0F - f8;
              f3 = f2 * 2.0F - f7;
            } 
            path.quadTo(f4, f3, param1ArrayOffloat2[i + 0], param1ArrayOffloat2[i + 1]);
            f1 = param1ArrayOffloat2[i + 0];
            f2 = param1ArrayOffloat2[i + 1];
            f7 = f3;
            f3 = f4;
            f4 = f7;
            break;
          case 'S':
            if (param1Char1 == 'c' || param1Char1 == 's' || param1Char1 == 'C' || param1Char1 == 'S') {
              f1 = f1 * 2.0F - f8;
              f2 = f2 * 2.0F - f7;
            } 
            param1Path.cubicTo(f1, f2, param1ArrayOffloat2[i + 0], param1ArrayOffloat2[i + 1], param1ArrayOffloat2[i + 2], param1ArrayOffloat2[i + 3]);
            f3 = param1ArrayOffloat2[i + 0];
            f4 = param1ArrayOffloat2[i + 1];
            f1 = param1ArrayOffloat2[i + 2];
            f2 = param1ArrayOffloat2[i + 3];
            break;
          case 'Q':
            path.quadTo(param1ArrayOffloat2[i + 0], param1ArrayOffloat2[i + 1], param1ArrayOffloat2[i + 2], param1ArrayOffloat2[i + 3]);
            f3 = param1ArrayOffloat2[i + 0];
            f4 = param1ArrayOffloat2[i + 1];
            f1 = param1ArrayOffloat2[i + 2];
            f2 = param1ArrayOffloat2[i + 3];
            break;
          case 'M':
            f2 = param1ArrayOffloat2[i + 0];
            f1 = param1ArrayOffloat2[i + 1];
            if (i > 0) {
              path.lineTo(param1ArrayOffloat2[i + 0], param1ArrayOffloat2[i + 1]);
              f3 = f2;
              f2 = f1;
              f1 = f3;
              f3 = f8;
              f4 = f7;
              break;
            } 
            path.moveTo(param1ArrayOffloat2[i + 0], param1ArrayOffloat2[i + 1]);
            f3 = f2;
            f4 = f1;
            f5 = f2;
            f6 = f1;
            f2 = f4;
            f1 = f3;
            f3 = f8;
            f4 = f7;
            break;
          case 'L':
            path.lineTo(param1ArrayOffloat2[i + 0], param1ArrayOffloat2[i + 1]);
            f1 = param1ArrayOffloat2[i + 0];
            f2 = param1ArrayOffloat2[i + 1];
            f3 = f8;
            f4 = f7;
            break;
          case 'H':
            path.lineTo(param1ArrayOffloat2[i + 0], f2);
            f1 = param1ArrayOffloat2[i + 0];
            f3 = f8;
            f4 = f7;
            break;
          case 'C':
            param1Path.cubicTo(param1ArrayOffloat2[i + 0], param1ArrayOffloat2[i + 1], param1ArrayOffloat2[i + 2], param1ArrayOffloat2[i + 3], param1ArrayOffloat2[i + 4], param1ArrayOffloat2[i + 5]);
            f1 = param1ArrayOffloat2[i + 4];
            f2 = param1ArrayOffloat2[i + 5];
            f3 = param1ArrayOffloat2[i + 2];
            f4 = param1ArrayOffloat2[i + 3];
            break;
          case 'A':
            f = param1ArrayOffloat2[i + 5];
            f7 = param1ArrayOffloat2[i + 6];
            f4 = param1ArrayOffloat2[i + 0];
            f8 = param1ArrayOffloat2[i + 1];
            f3 = param1ArrayOffloat2[i + 2];
            if (param1ArrayOffloat2[i + 3] != 0.0F) {
              bool1 = true;
            } else {
              bool1 = false;
            } 
            if (param1ArrayOffloat2[i + 4] != 0.0F) {
              bool2 = true;
            } else {
              bool2 = false;
            } 
            drawArc(param1Path, f1, f2, f, f7, f4, f8, f3, bool1, bool2);
            f3 = param1ArrayOffloat2[i + 5];
            f4 = param1ArrayOffloat2[i + 6];
            f1 = f3;
            f2 = f4;
            break;
        } 
        param1Char1 = param1Char2;
        i += b;
        f8 = f3;
        f7 = f4;
      } 
      param1ArrayOffloat1[0] = f1;
      param1ArrayOffloat1[1] = f2;
      param1ArrayOffloat1[2] = f8;
      param1ArrayOffloat1[3] = f7;
      param1ArrayOffloat1[4] = f5;
      param1ArrayOffloat1[5] = f6;
    }
    
    private static void arcToBezier(Path param1Path, double param1Double1, double param1Double2, double param1Double3, double param1Double4, double param1Double5, double param1Double6, double param1Double7, double param1Double8, double param1Double9) {
      int i = (int)Math.ceil(Math.abs(param1Double9 * 4.0D / Math.PI));
      double d6 = Math.cos(param1Double7);
      param1Double7 = Math.sin(param1Double7);
      double d7 = Math.cos(param1Double8);
      double d8 = Math.sin(param1Double8);
      double d2 = -param1Double3;
      double d1 = -param1Double3 * param1Double7 * d8 + param1Double4 * d6 * d7;
      double d3 = param1Double9 / i;
      byte b = 0;
      param1Double9 = param1Double5;
      d2 = d2 * d6 * d8 - param1Double4 * param1Double7 * d7;
      double d5 = param1Double8;
      double d4 = param1Double6;
      param1Double8 = d8;
      param1Double6 = d7;
      param1Double5 = param1Double7;
      param1Double7 = d6;
      while (b < i) {
        double d10 = d5 + d3;
        double d11 = Math.sin(d10);
        d7 = Math.cos(d10);
        double d9 = param1Double1 + param1Double3 * param1Double7 * d7 - param1Double4 * param1Double5 * d11;
        d8 = param1Double2 + param1Double3 * param1Double5 * d7 + param1Double4 * param1Double7 * d11;
        d6 = -param1Double3 * param1Double7 * d11 - param1Double4 * param1Double5 * d7;
        d7 = -param1Double3 * param1Double5 * d11 + param1Double4 * param1Double7 * d7;
        d11 = Math.tan((d10 - d5) / 2.0D);
        d5 = Math.sin(d10 - d5) * (Math.sqrt(d11 * 3.0D * d11 + 4.0D) - 1.0D) / 3.0D;
        param1Path.rLineTo(0.0F, 0.0F);
        param1Path.cubicTo((float)(param1Double9 + d5 * d2), (float)(d4 + d5 * d1), (float)(d9 - d5 * d6), (float)(d8 - d5 * d7), (float)d9, (float)d8);
        d5 = d10;
        param1Double9 = d9;
        d4 = d8;
        d2 = d6;
        d1 = d7;
        b++;
      } 
    }
    
    private static void drawArc(Path param1Path, float param1Float1, float param1Float2, float param1Float3, float param1Float4, float param1Float5, float param1Float6, float param1Float7, boolean param1Boolean1, boolean param1Boolean2) {
      double d6 = Math.toRadians(param1Float7);
      double d5 = Math.cos(d6);
      double d7 = Math.sin(d6);
      double d8 = (param1Float1 * d5 + param1Float2 * d7) / param1Float5;
      double d9 = (-param1Float1 * d7 + param1Float2 * d5) / param1Float6;
      double d1 = (param1Float3 * d5 + param1Float4 * d7) / param1Float5;
      double d4 = (-param1Float3 * d7 + param1Float4 * d5) / param1Float6;
      double d11 = d8 - d1;
      double d10 = d9 - d4;
      double d3 = (d8 + d1) / 2.0D;
      double d2 = (d9 + d4) / 2.0D;
      double d12 = d11 * d11 + d10 * d10;
      if (d12 == 0.0D) {
        Log.w("PathParser", " Points are coincident");
        return;
      } 
      double d13 = 1.0D / d12 - 0.25D;
      if (d13 < 0.0D) {
        Log.w("PathParser", "Points are too far apart " + d12);
        float f = (float)(Math.sqrt(d12) / 1.99999D);
        drawArc(param1Path, param1Float1, param1Float2, param1Float3, param1Float4, param1Float5 * f, param1Float6 * f, param1Float7, param1Boolean1, param1Boolean2);
        return;
      } 
      d12 = Math.sqrt(d13);
      d11 = d12 * d11;
      d10 = d12 * d10;
      if (param1Boolean1 == param1Boolean2) {
        d3 -= d10;
        d2 += d11;
      } else {
        d3 += d10;
        d2 -= d11;
      } 
      d8 = Math.atan2(d9 - d2, d8 - d3);
      d4 = Math.atan2(d4 - d2, d1 - d3) - d8;
      if (d4 >= 0.0D) {
        param1Boolean1 = true;
      } else {
        param1Boolean1 = false;
      } 
      d1 = d4;
      if (param1Boolean2 != param1Boolean1)
        if (d4 > 0.0D) {
          d1 = d4 - 6.283185307179586D;
        } else {
          d1 = d4 + 6.283185307179586D;
        }  
      d3 *= param1Float5;
      d2 = param1Float6 * d2;
      arcToBezier(param1Path, d3 * d5 - d2 * d7, d3 * d7 + d2 * d5, param1Float5, param1Float6, param1Float1, param1Float2, d6, d8, d1);
    }
    
    public static void nodesToPath(PathDataNode[] param1ArrayOfPathDataNode, Path param1Path) {
      float[] arrayOfFloat = new float[6];
      char c = 'm';
      for (byte b = 0; b < param1ArrayOfPathDataNode.length; b++) {
        addCommand(param1Path, arrayOfFloat, c, (param1ArrayOfPathDataNode[b]).mType, (param1ArrayOfPathDataNode[b]).mParams);
        c = (param1ArrayOfPathDataNode[b]).mType;
      } 
    }
    
    public void interpolatePathDataNode(PathDataNode param1PathDataNode1, PathDataNode param1PathDataNode2, float param1Float) {
      this.mType = param1PathDataNode1.mType;
      byte b = 0;
      while (true) {
        float[] arrayOfFloat = param1PathDataNode1.mParams;
        if (b < arrayOfFloat.length) {
          this.mParams[b] = arrayOfFloat[b] * (1.0F - param1Float) + param1PathDataNode2.mParams[b] * param1Float;
          b++;
          continue;
        } 
        break;
      } 
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\graphics\PathParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */