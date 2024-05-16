package androidx.constraintlayout.core.motion.parse;

import androidx.constraintlayout.core.motion.utils.TypedBundle;
import androidx.constraintlayout.core.parser.CLElement;
import androidx.constraintlayout.core.parser.CLKey;
import androidx.constraintlayout.core.parser.CLObject;
import androidx.constraintlayout.core.parser.CLParser;
import androidx.constraintlayout.core.parser.CLParsingException;
import java.io.PrintStream;

public class KeyParser {
  public static void main(String[] paramArrayOfString) {
    parseAttributes("{frame:22,\ntarget:'widget1',\neasing:'easeIn',\ncurveFit:'spline',\nprogress:0.3,\nalpha:0.2,\nelevation:0.7,\nrotationZ:23,\nrotationX:25.0,\nrotationY:27.0,\npivotX:15,\npivotY:17,\npivotTarget:'32',\npathRotate:23,\nscaleX:0.5,\nscaleY:0.7,\ntranslationX:5,\ntranslationY:7,\ntranslationZ:11,\n}");
  }
  
  private static TypedBundle parse(String paramString, Ids paramIds, DataType paramDataType) {
    TypedBundle typedBundle = new TypedBundle();
    try {
      CLObject cLObject = CLParser.parse(paramString);
      int i = cLObject.size();
      for (byte b = 0; b < i; b++) {
        StringBuilder stringBuilder;
        CLKey cLKey = (CLKey)cLObject.get(b);
        paramString = cLKey.content();
        CLElement cLElement = cLKey.getValue();
        int j = paramIds.get(paramString);
        if (j == -1) {
          PrintStream printStream = System.err;
          stringBuilder = new StringBuilder();
          this();
          printStream.println(stringBuilder.append("unknown type ").append(paramString).toString());
        } else {
          PrintStream printStream2;
          StringBuilder stringBuilder1;
          PrintStream printStream1;
          StringBuilder stringBuilder3;
          PrintStream printStream3;
          StringBuilder stringBuilder2;
          int k = paramDataType.get(j);
          switch (k) {
            case 8:
              typedBundle.add(j, stringBuilder.content());
              printStream2 = System.out;
              stringBuilder3 = new StringBuilder();
              this();
              printStream2.println(stringBuilder3.append("parse ").append(paramString).append(" STRING_MASK > ").append(stringBuilder.content()).toString());
              break;
            case 4:
              typedBundle.add(j, stringBuilder.getFloat());
              printStream3 = System.out;
              stringBuilder1 = new StringBuilder();
              this();
              printStream3.println(stringBuilder1.append("parse ").append(paramString).append(" FLOAT_MASK > ").append(stringBuilder.getFloat()).toString());
              break;
            case 2:
              typedBundle.add(j, stringBuilder.getInt());
              printStream1 = System.out;
              stringBuilder2 = new StringBuilder();
              this();
              printStream1.println(stringBuilder2.append("parse ").append(paramString).append(" INT_MASK > ").append(stringBuilder.getInt()).toString());
              break;
            case 1:
              typedBundle.add(j, cLObject.getBoolean(b));
              break;
          } 
        } 
      } 
    } catch (CLParsingException cLParsingException) {
      cLParsingException.printStackTrace();
    } 
    return typedBundle;
  }
  
  public static TypedBundle parseAttributes(String paramString) {
    return parse(paramString, KeyParser$$ExternalSyntheticLambda1.INSTANCE, KeyParser$$ExternalSyntheticLambda0.INSTANCE);
  }
  
  private static interface DataType {
    int get(int param1Int);
  }
  
  private static interface Ids {
    int get(String param1String);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\motion\parse\KeyParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */