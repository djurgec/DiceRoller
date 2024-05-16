package androidx.constraintlayout.core.state;

import androidx.constraintlayout.core.motion.CustomVariable;
import androidx.constraintlayout.core.parser.CLElement;
import androidx.constraintlayout.core.parser.CLKey;
import androidx.constraintlayout.core.parser.CLObject;
import androidx.constraintlayout.core.parser.CLParsingException;
import androidx.constraintlayout.core.widgets.ConstraintAnchor;
import androidx.constraintlayout.core.widgets.ConstraintWidget;
import java.util.HashMap;
import java.util.Set;

public class WidgetFrame {
  private static final boolean OLD_SYSTEM = true;
  
  public static float phone_orientation = Float.NaN;
  
  public float alpha = Float.NaN;
  
  public int bottom = 0;
  
  public float interpolatedPos = Float.NaN;
  
  public int left = 0;
  
  public final HashMap<String, CustomVariable> mCustom = new HashMap<>();
  
  public String name = null;
  
  public float pivotX = Float.NaN;
  
  public float pivotY = Float.NaN;
  
  public int right = 0;
  
  public float rotationX = Float.NaN;
  
  public float rotationY = Float.NaN;
  
  public float rotationZ = Float.NaN;
  
  public float scaleX = Float.NaN;
  
  public float scaleY = Float.NaN;
  
  public int top = 0;
  
  public float translationX = Float.NaN;
  
  public float translationY = Float.NaN;
  
  public float translationZ = Float.NaN;
  
  public int visibility = 0;
  
  public ConstraintWidget widget = null;
  
  public WidgetFrame() {}
  
  public WidgetFrame(WidgetFrame paramWidgetFrame) {
    this.widget = paramWidgetFrame.widget;
    this.left = paramWidgetFrame.left;
    this.top = paramWidgetFrame.top;
    this.right = paramWidgetFrame.right;
    this.bottom = paramWidgetFrame.bottom;
    updateAttributes(paramWidgetFrame);
  }
  
  public WidgetFrame(ConstraintWidget paramConstraintWidget) {
    this.widget = paramConstraintWidget;
  }
  
  private static void add(StringBuilder paramStringBuilder, String paramString, float paramFloat) {
    if (Float.isNaN(paramFloat))
      return; 
    paramStringBuilder.append(paramString);
    paramStringBuilder.append(": ");
    paramStringBuilder.append(paramFloat);
    paramStringBuilder.append(",\n");
  }
  
  private static void add(StringBuilder paramStringBuilder, String paramString, int paramInt) {
    paramStringBuilder.append(paramString);
    paramStringBuilder.append(": ");
    paramStringBuilder.append(paramInt);
    paramStringBuilder.append(",\n");
  }
  
  private static float interpolate(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
    boolean bool2 = Float.isNaN(paramFloat1);
    boolean bool1 = Float.isNaN(paramFloat2);
    if (bool2 && bool1)
      return Float.NaN; 
    if (bool2)
      paramFloat1 = paramFloat3; 
    if (bool1)
      paramFloat2 = paramFloat3; 
    return (paramFloat2 - paramFloat1) * paramFloat4 + paramFloat1;
  }
  
  public static void interpolate(int paramInt1, int paramInt2, WidgetFrame paramWidgetFrame1, WidgetFrame paramWidgetFrame2, WidgetFrame paramWidgetFrame3, Transition paramTransition, float paramFloat) {
    float f2;
    int i6 = (int)(paramFloat * 100.0F);
    int i1 = paramWidgetFrame2.left;
    int n = paramWidgetFrame2.top;
    int i5 = paramWidgetFrame3.left;
    int i4 = paramWidgetFrame3.top;
    int i = paramWidgetFrame2.right;
    int i3 = paramWidgetFrame2.left;
    int m = paramWidgetFrame2.bottom;
    int i2 = paramWidgetFrame2.top;
    int j = paramWidgetFrame3.right - paramWidgetFrame3.left;
    int k = paramWidgetFrame3.bottom - paramWidgetFrame3.top;
    float f4 = paramFloat;
    float f1 = paramWidgetFrame2.alpha;
    float f3 = paramWidgetFrame3.alpha;
    if (paramWidgetFrame2.visibility == 8) {
      i1 = (int)(i1 - j / 2.0F);
      n = (int)(n - k / 2.0F);
      m = k;
      if (Float.isNaN(f1)) {
        i = j;
        f1 = 0.0F;
      } else {
        i = j;
      } 
    } else {
      i -= i3;
      m -= i2;
    } 
    i2 = paramWidgetFrame3.visibility;
    if (i2 == 8) {
      int i7 = (int)(i5 - i / 2.0F);
      int i8 = (int)(i4 - m / 2.0F);
      i5 = i;
      i4 = m;
      i3 = i4;
      k = i7;
      j = i8;
      f2 = f3;
      i2 = i5;
      if (Float.isNaN(f3)) {
        f2 = 0.0F;
        i3 = i4;
        k = i7;
        j = i8;
        i2 = i5;
      } 
    } else {
      i3 = k;
      i2 = j;
      f2 = f3;
      j = i4;
      k = i5;
    } 
    f3 = f1;
    if (Float.isNaN(f1)) {
      f3 = f1;
      if (!Float.isNaN(f2))
        f3 = 1.0F; 
    } 
    f1 = f2;
    if (!Float.isNaN(f3)) {
      f1 = f2;
      if (Float.isNaN(f2))
        f1 = 1.0F; 
    } 
    if (paramWidgetFrame2.visibility == 4) {
      f2 = 0.0F;
    } else {
      f2 = f3;
    } 
    if (paramWidgetFrame3.visibility == 4)
      f1 = 0.0F; 
    if (paramWidgetFrame1.widget != null && paramTransition.hasPositionKeyframes()) {
      Transition.KeyPosition keyPosition3 = paramTransition.findPreviousPosition(paramWidgetFrame1.widget.stringId, i6);
      Transition.KeyPosition keyPosition2 = paramTransition.findNextPosition(paramWidgetFrame1.widget.stringId, i6);
      Transition.KeyPosition keyPosition1 = keyPosition2;
      if (keyPosition3 == keyPosition2)
        keyPosition1 = null; 
      i5 = 100;
      if (keyPosition3 != null) {
        i1 = (int)(keyPosition3.x * paramInt1);
        f3 = keyPosition3.y;
        n = (int)(f3 * paramInt2);
        i4 = keyPosition3.frame;
      } else {
        i4 = 0;
      } 
      if (keyPosition1 != null) {
        paramInt1 = (int)(keyPosition1.x * paramInt1);
        j = (int)(keyPosition1.y * paramInt2);
        paramInt2 = keyPosition1.frame;
      } else {
        paramInt1 = k;
        paramInt2 = i5;
      } 
      f4 = (100.0F * paramFloat - i4) / (paramInt2 - i4);
      paramInt2 = paramInt1;
      paramInt1 = n;
    } else {
      paramInt1 = n;
      paramInt2 = k;
    } 
    paramWidgetFrame1.widget = paramWidgetFrame2.widget;
    paramInt2 = (int)(i1 + (paramInt2 - i1) * f4);
    paramWidgetFrame1.left = paramInt2;
    paramInt1 = (int)(paramInt1 + (j - paramInt1) * f4);
    paramWidgetFrame1.top = paramInt1;
    i = (int)((1.0F - paramFloat) * i + i2 * paramFloat);
    j = (int)((1.0F - paramFloat) * m + i3 * paramFloat);
    paramWidgetFrame1.right = paramInt2 + i;
    paramWidgetFrame1.bottom = paramInt1 + j;
    paramWidgetFrame1.pivotX = interpolate(paramWidgetFrame2.pivotX, paramWidgetFrame3.pivotX, 0.5F, paramFloat);
    paramWidgetFrame1.pivotY = interpolate(paramWidgetFrame2.pivotY, paramWidgetFrame3.pivotY, 0.5F, paramFloat);
    paramWidgetFrame1.rotationX = interpolate(paramWidgetFrame2.rotationX, paramWidgetFrame3.rotationX, 0.0F, paramFloat);
    paramWidgetFrame1.rotationY = interpolate(paramWidgetFrame2.rotationY, paramWidgetFrame3.rotationY, 0.0F, paramFloat);
    paramWidgetFrame1.rotationZ = interpolate(paramWidgetFrame2.rotationZ, paramWidgetFrame3.rotationZ, 0.0F, paramFloat);
    paramWidgetFrame1.scaleX = interpolate(paramWidgetFrame2.scaleX, paramWidgetFrame3.scaleX, 1.0F, paramFloat);
    paramWidgetFrame1.scaleY = interpolate(paramWidgetFrame2.scaleY, paramWidgetFrame3.scaleY, 1.0F, paramFloat);
    paramWidgetFrame1.translationX = interpolate(paramWidgetFrame2.translationX, paramWidgetFrame3.translationX, 0.0F, paramFloat);
    paramWidgetFrame1.translationY = interpolate(paramWidgetFrame2.translationY, paramWidgetFrame3.translationY, 0.0F, paramFloat);
    paramWidgetFrame1.translationZ = interpolate(paramWidgetFrame2.translationZ, paramWidgetFrame3.translationZ, 0.0F, paramFloat);
    paramWidgetFrame1.alpha = interpolate(f2, f1, 1.0F, paramFloat);
    Set<String> set = paramWidgetFrame3.mCustom.keySet();
    paramWidgetFrame1.mCustom.clear();
    for (String str : set) {
      if (paramWidgetFrame2.mCustom.containsKey(str)) {
        CustomVariable customVariable3 = paramWidgetFrame2.mCustom.get(str);
        CustomVariable customVariable1 = paramWidgetFrame3.mCustom.get(str);
        CustomVariable customVariable2 = new CustomVariable(customVariable3);
        paramWidgetFrame1.mCustom.put(str, customVariable2);
        if (customVariable3.numberOfInterpolatedValues() == 1) {
          customVariable2.setValue(Float.valueOf(interpolate(customVariable3.getValueToInterpolate(), customVariable1.getValueToInterpolate(), 0.0F, paramFloat)));
          continue;
        } 
        paramInt1 = customVariable3.numberOfInterpolatedValues();
        float[] arrayOfFloat2 = new float[paramInt1];
        float[] arrayOfFloat1 = new float[paramInt1];
        customVariable3.getValuesToInterpolate(arrayOfFloat2);
        customVariable1.getValuesToInterpolate(arrayOfFloat1);
        for (paramInt2 = 0; paramInt2 < paramInt1; paramInt2++) {
          arrayOfFloat2[paramInt2] = interpolate(arrayOfFloat2[paramInt2], arrayOfFloat1[paramInt2], 0.0F, paramFloat);
          customVariable2.setValue(arrayOfFloat2);
        } 
      } 
    } 
  }
  
  private void serializeAnchor(StringBuilder paramStringBuilder, ConstraintAnchor.Type paramType) {
    ConstraintAnchor constraintAnchor = this.widget.getAnchor(paramType);
    if (constraintAnchor == null || constraintAnchor.mTarget == null)
      return; 
    paramStringBuilder.append("Anchor");
    paramStringBuilder.append(paramType.name());
    paramStringBuilder.append(": ['");
    String str = (constraintAnchor.mTarget.getOwner()).stringId;
    if (str == null)
      str = "#PARENT"; 
    paramStringBuilder.append(str);
    paramStringBuilder.append("', '");
    paramStringBuilder.append(constraintAnchor.mTarget.getType().name());
    paramStringBuilder.append("', '");
    paramStringBuilder.append(constraintAnchor.mMargin);
    paramStringBuilder.append("'],\n");
  }
  
  public void addCustomColor(String paramString, int paramInt) {
    setCustomAttribute(paramString, 902, paramInt);
  }
  
  public void addCustomFloat(String paramString, float paramFloat) {
    setCustomAttribute(paramString, 901, paramFloat);
  }
  
  public float centerX() {
    int i = this.left;
    return i + (this.right - i) / 2.0F;
  }
  
  public float centerY() {
    int i = this.top;
    return i + (this.bottom - i) / 2.0F;
  }
  
  public CustomVariable getCustomAttribute(String paramString) {
    return this.mCustom.get(paramString);
  }
  
  public Set<String> getCustomAttributeNames() {
    return this.mCustom.keySet();
  }
  
  public int getCustomColor(String paramString) {
    return this.mCustom.containsKey(paramString) ? ((CustomVariable)this.mCustom.get(paramString)).getColorValue() : -21880;
  }
  
  public float getCustomFloat(String paramString) {
    return this.mCustom.containsKey(paramString) ? ((CustomVariable)this.mCustom.get(paramString)).getFloatValue() : Float.NaN;
  }
  
  public int height() {
    return Math.max(0, this.bottom - this.top);
  }
  
  public boolean isDefaultTransform() {
    boolean bool;
    if (Float.isNaN(this.rotationX) && Float.isNaN(this.rotationY) && Float.isNaN(this.rotationZ) && Float.isNaN(this.translationX) && Float.isNaN(this.translationY) && Float.isNaN(this.translationZ) && Float.isNaN(this.scaleX) && Float.isNaN(this.scaleY) && Float.isNaN(this.alpha)) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  void logv(String paramString) {
    StackTraceElement stackTraceElement = (new Throwable()).getStackTrace()[1];
    String str = ".(" + stackTraceElement.getFileName() + ":" + stackTraceElement.getLineNumber() + ") " + stackTraceElement.getMethodName();
    str = str + " " + (hashCode() % 1000);
    if (this.widget != null) {
      str = str + "/" + (this.widget.hashCode() % 1000);
    } else {
      str = str + "/NULL";
    } 
    System.out.println(str + " " + paramString);
  }
  
  void parseCustom(CLElement paramCLElement) throws CLParsingException {
    CLObject cLObject = (CLObject)paramCLElement;
    int i = cLObject.size();
    for (byte b = 0; b < i; b++) {
      CLKey cLKey = (CLKey)cLObject.get(b);
      cLKey.content();
      paramCLElement = cLKey.getValue();
      String str = paramCLElement.content();
      if (str.matches("#[0-9a-fA-F]+")) {
        int j = Integer.parseInt(str.substring(1), 16);
        setCustomAttribute(cLKey.content(), 902, j);
      } else if (paramCLElement instanceof androidx.constraintlayout.core.parser.CLNumber) {
        setCustomAttribute(cLKey.content(), 901, paramCLElement.getFloat());
      } else {
        setCustomAttribute(cLKey.content(), 903, str);
      } 
    } 
  }
  
  void printCustomAttributes() {
    StackTraceElement stackTraceElement = (new Throwable()).getStackTrace()[1];
    String str = ".(" + stackTraceElement.getFileName() + ":" + stackTraceElement.getLineNumber() + ") " + stackTraceElement.getMethodName();
    str = str + " " + (hashCode() % 1000);
    if (this.widget != null) {
      str = str + "/" + (this.widget.hashCode() % 1000) + " ";
    } else {
      str = str + "/NULL ";
    } 
    HashMap<String, CustomVariable> hashMap = this.mCustom;
    if (hashMap != null)
      for (String str1 : hashMap.keySet())
        System.out.println(str + ((CustomVariable)this.mCustom.get(str1)).toString());  
  }
  
  public StringBuilder serialize(StringBuilder paramStringBuilder) {
    return serialize(paramStringBuilder, false);
  }
  
  public StringBuilder serialize(StringBuilder paramStringBuilder, boolean paramBoolean) {
    paramStringBuilder.append("{\n");
    add(paramStringBuilder, "left", this.left);
    add(paramStringBuilder, "top", this.top);
    add(paramStringBuilder, "right", this.right);
    add(paramStringBuilder, "bottom", this.bottom);
    add(paramStringBuilder, "pivotX", this.pivotX);
    add(paramStringBuilder, "pivotY", this.pivotY);
    add(paramStringBuilder, "rotationX", this.rotationX);
    add(paramStringBuilder, "rotationY", this.rotationY);
    add(paramStringBuilder, "rotationZ", this.rotationZ);
    add(paramStringBuilder, "translationX", this.translationX);
    add(paramStringBuilder, "translationY", this.translationY);
    add(paramStringBuilder, "translationZ", this.translationZ);
    add(paramStringBuilder, "scaleX", this.scaleX);
    add(paramStringBuilder, "scaleY", this.scaleY);
    add(paramStringBuilder, "alpha", this.alpha);
    add(paramStringBuilder, "visibility", this.left);
    add(paramStringBuilder, "interpolatedPos", this.interpolatedPos);
    if (this.widget != null) {
      ConstraintAnchor.Type[] arrayOfType = ConstraintAnchor.Type.values();
      int i = arrayOfType.length;
      for (byte b = 0; b < i; b++)
        serializeAnchor(paramStringBuilder, arrayOfType[b]); 
    } 
    if (paramBoolean)
      add(paramStringBuilder, "phone_orientation", phone_orientation); 
    if (paramBoolean)
      add(paramStringBuilder, "phone_orientation", phone_orientation); 
    if (this.mCustom.size() != 0) {
      paramStringBuilder.append("custom : {\n");
      for (String str : this.mCustom.keySet()) {
        CustomVariable customVariable = this.mCustom.get(str);
        paramStringBuilder.append(str);
        paramStringBuilder.append(": ");
        switch (customVariable.getType()) {
          default:
            continue;
          case 904:
            paramStringBuilder.append("'");
            paramStringBuilder.append(customVariable.getBooleanValue());
            paramStringBuilder.append("',\n");
            continue;
          case 903:
            paramStringBuilder.append("'");
            paramStringBuilder.append(customVariable.getStringValue());
            paramStringBuilder.append("',\n");
            continue;
          case 902:
            paramStringBuilder.append("'");
            paramStringBuilder.append(CustomVariable.colorString(customVariable.getIntegerValue()));
            paramStringBuilder.append("',\n");
            continue;
          case 901:
          case 905:
            paramStringBuilder.append(customVariable.getFloatValue());
            paramStringBuilder.append(",\n");
            continue;
          case 900:
            break;
        } 
        paramStringBuilder.append(customVariable.getIntegerValue());
        paramStringBuilder.append(",\n");
      } 
      paramStringBuilder.append("}\n");
    } 
    paramStringBuilder.append("}\n");
    return paramStringBuilder;
  }
  
  public void setCustomAttribute(String paramString, int paramInt, float paramFloat) {
    if (this.mCustom.containsKey(paramString)) {
      ((CustomVariable)this.mCustom.get(paramString)).setFloatValue(paramFloat);
    } else {
      this.mCustom.put(paramString, new CustomVariable(paramString, paramInt, paramFloat));
    } 
  }
  
  public void setCustomAttribute(String paramString, int paramInt1, int paramInt2) {
    if (this.mCustom.containsKey(paramString)) {
      ((CustomVariable)this.mCustom.get(paramString)).setIntValue(paramInt2);
    } else {
      this.mCustom.put(paramString, new CustomVariable(paramString, paramInt1, paramInt2));
    } 
  }
  
  public void setCustomAttribute(String paramString1, int paramInt, String paramString2) {
    if (this.mCustom.containsKey(paramString1)) {
      ((CustomVariable)this.mCustom.get(paramString1)).setStringValue(paramString2);
    } else {
      this.mCustom.put(paramString1, new CustomVariable(paramString1, paramInt, paramString2));
    } 
  }
  
  public void setCustomAttribute(String paramString, int paramInt, boolean paramBoolean) {
    if (this.mCustom.containsKey(paramString)) {
      ((CustomVariable)this.mCustom.get(paramString)).setBooleanValue(paramBoolean);
    } else {
      this.mCustom.put(paramString, new CustomVariable(paramString, paramInt, paramBoolean));
    } 
  }
  
  public boolean setValue(String paramString, CLElement paramCLElement) throws CLParsingException {
    byte b;
    switch (paramString.hashCode()) {
      default:
        b = -1;
        break;
      case 642850769:
        if (paramString.equals("interpolatedPos")) {
          b = 11;
          break;
        } 
      case 108511772:
        if (paramString.equals("right")) {
          b = 15;
          break;
        } 
      case 92909918:
        if (paramString.equals("alpha")) {
          b = 10;
          break;
        } 
      case 3317767:
        if (paramString.equals("left")) {
          b = 14;
          break;
        } 
      case 115029:
        if (paramString.equals("top")) {
          b = 13;
          break;
        } 
      case -908189617:
        if (paramString.equals("scaleY")) {
          b = 9;
          break;
        } 
      case -908189618:
        if (paramString.equals("scaleX")) {
          b = 8;
          break;
        } 
      case -987906985:
        if (paramString.equals("pivotY")) {
          b = 1;
          break;
        } 
      case -987906986:
        if (paramString.equals("pivotX")) {
          b = 0;
          break;
        } 
      case -1225497655:
        if (paramString.equals("translationZ")) {
          b = 7;
          break;
        } 
      case -1225497656:
        if (paramString.equals("translationY")) {
          b = 6;
          break;
        } 
      case -1225497657:
        if (paramString.equals("translationX")) {
          b = 5;
          break;
        } 
      case -1249320804:
        if (paramString.equals("rotationZ")) {
          b = 4;
          break;
        } 
      case -1249320805:
        if (paramString.equals("rotationY")) {
          b = 3;
          break;
        } 
      case -1249320806:
        if (paramString.equals("rotationX")) {
          b = 2;
          break;
        } 
      case -1349088399:
        if (paramString.equals("custom")) {
          b = 17;
          break;
        } 
      case -1383228885:
        if (paramString.equals("bottom")) {
          b = 16;
          break;
        } 
      case -1881940865:
        if (paramString.equals("phone_orientation")) {
          b = 12;
          break;
        } 
    } 
    switch (b) {
      default:
        return false;
      case 17:
        parseCustom(paramCLElement);
        return true;
      case 16:
        this.bottom = paramCLElement.getInt();
        return true;
      case 15:
        this.right = paramCLElement.getInt();
        return true;
      case 14:
        this.left = paramCLElement.getInt();
        return true;
      case 13:
        this.top = paramCLElement.getInt();
        return true;
      case 12:
        phone_orientation = paramCLElement.getFloat();
        return true;
      case 11:
        this.interpolatedPos = paramCLElement.getFloat();
        return true;
      case 10:
        this.alpha = paramCLElement.getFloat();
        return true;
      case 9:
        this.scaleY = paramCLElement.getFloat();
        return true;
      case 8:
        this.scaleX = paramCLElement.getFloat();
        return true;
      case 7:
        this.translationZ = paramCLElement.getFloat();
        return true;
      case 6:
        this.translationY = paramCLElement.getFloat();
        return true;
      case 5:
        this.translationX = paramCLElement.getFloat();
        return true;
      case 4:
        this.rotationZ = paramCLElement.getFloat();
        return true;
      case 3:
        this.rotationY = paramCLElement.getFloat();
        return true;
      case 2:
        this.rotationX = paramCLElement.getFloat();
        return true;
      case 1:
        this.pivotY = paramCLElement.getFloat();
        return true;
      case 0:
        break;
    } 
    this.pivotX = paramCLElement.getFloat();
    return true;
  }
  
  public WidgetFrame update() {
    ConstraintWidget constraintWidget = this.widget;
    if (constraintWidget != null) {
      this.left = constraintWidget.getLeft();
      this.top = this.widget.getTop();
      this.right = this.widget.getRight();
      this.bottom = this.widget.getBottom();
      updateAttributes(this.widget.frame);
    } 
    return this;
  }
  
  public WidgetFrame update(ConstraintWidget paramConstraintWidget) {
    if (paramConstraintWidget == null)
      return this; 
    this.widget = paramConstraintWidget;
    update();
    return this;
  }
  
  public void updateAttributes(WidgetFrame paramWidgetFrame) {
    this.pivotX = paramWidgetFrame.pivotX;
    this.pivotY = paramWidgetFrame.pivotY;
    this.rotationX = paramWidgetFrame.rotationX;
    this.rotationY = paramWidgetFrame.rotationY;
    this.rotationZ = paramWidgetFrame.rotationZ;
    this.translationX = paramWidgetFrame.translationX;
    this.translationY = paramWidgetFrame.translationY;
    this.translationZ = paramWidgetFrame.translationZ;
    this.scaleX = paramWidgetFrame.scaleX;
    this.scaleY = paramWidgetFrame.scaleY;
    this.alpha = paramWidgetFrame.alpha;
    this.visibility = paramWidgetFrame.visibility;
    this.mCustom.clear();
    if (paramWidgetFrame != null)
      for (CustomVariable customVariable : paramWidgetFrame.mCustom.values())
        this.mCustom.put(customVariable.getName(), customVariable.copy());  
  }
  
  public int width() {
    return Math.max(0, this.right - this.left);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\state\WidgetFrame.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */