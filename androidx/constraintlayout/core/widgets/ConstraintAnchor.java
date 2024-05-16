package androidx.constraintlayout.core.widgets;

import androidx.constraintlayout.core.Cache;
import androidx.constraintlayout.core.SolverVariable;
import androidx.constraintlayout.core.widgets.analyzer.Grouping;
import androidx.constraintlayout.core.widgets.analyzer.WidgetGroup;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class ConstraintAnchor {
  private static final boolean ALLOW_BINARY = false;
  
  private static final int UNSET_GONE_MARGIN = -2147483648;
  
  private HashSet<ConstraintAnchor> mDependents = null;
  
  private int mFinalValue;
  
  int mGoneMargin = Integer.MIN_VALUE;
  
  private boolean mHasFinalValue;
  
  public int mMargin = 0;
  
  public final ConstraintWidget mOwner;
  
  SolverVariable mSolverVariable;
  
  public ConstraintAnchor mTarget;
  
  public final Type mType;
  
  public ConstraintAnchor(ConstraintWidget paramConstraintWidget, Type paramType) {
    this.mOwner = paramConstraintWidget;
    this.mType = paramType;
  }
  
  private boolean isConnectionToMe(ConstraintWidget paramConstraintWidget, HashSet<ConstraintWidget> paramHashSet) {
    if (paramHashSet.contains(paramConstraintWidget))
      return false; 
    paramHashSet.add(paramConstraintWidget);
    if (paramConstraintWidget == getOwner())
      return true; 
    ArrayList<ConstraintAnchor> arrayList = paramConstraintWidget.getAnchors();
    byte b = 0;
    int i = arrayList.size();
    while (b < i) {
      ConstraintAnchor constraintAnchor = arrayList.get(b);
      if (constraintAnchor.isSimilarDimensionConnection(this) && constraintAnchor.isConnected() && isConnectionToMe(constraintAnchor.getTarget().getOwner(), paramHashSet))
        return true; 
      b++;
    } 
    return false;
  }
  
  public boolean connect(ConstraintAnchor paramConstraintAnchor, int paramInt) {
    return connect(paramConstraintAnchor, paramInt, -2147483648, false);
  }
  
  public boolean connect(ConstraintAnchor paramConstraintAnchor, int paramInt1, int paramInt2, boolean paramBoolean) {
    if (paramConstraintAnchor == null) {
      reset();
      return true;
    } 
    if (!paramBoolean && !isValidConnection(paramConstraintAnchor))
      return false; 
    this.mTarget = paramConstraintAnchor;
    if (paramConstraintAnchor.mDependents == null)
      paramConstraintAnchor.mDependents = new HashSet<>(); 
    HashSet<ConstraintAnchor> hashSet = this.mTarget.mDependents;
    if (hashSet != null)
      hashSet.add(this); 
    this.mMargin = paramInt1;
    this.mGoneMargin = paramInt2;
    return true;
  }
  
  public void copyFrom(ConstraintAnchor paramConstraintAnchor, HashMap<ConstraintWidget, ConstraintWidget> paramHashMap) {
    ConstraintAnchor constraintAnchor2 = this.mTarget;
    if (constraintAnchor2 != null) {
      HashSet<ConstraintAnchor> hashSet = constraintAnchor2.mDependents;
      if (hashSet != null)
        hashSet.remove(this); 
    } 
    constraintAnchor2 = paramConstraintAnchor.mTarget;
    if (constraintAnchor2 != null) {
      Type type = constraintAnchor2.getType();
      this.mTarget = ((ConstraintWidget)paramHashMap.get(paramConstraintAnchor.mTarget.mOwner)).getAnchor(type);
    } else {
      this.mTarget = null;
    } 
    ConstraintAnchor constraintAnchor1 = this.mTarget;
    if (constraintAnchor1 != null) {
      if (constraintAnchor1.mDependents == null)
        constraintAnchor1.mDependents = new HashSet<>(); 
      this.mTarget.mDependents.add(this);
    } 
    this.mMargin = paramConstraintAnchor.mMargin;
    this.mGoneMargin = paramConstraintAnchor.mGoneMargin;
  }
  
  public void findDependents(int paramInt, ArrayList<WidgetGroup> paramArrayList, WidgetGroup paramWidgetGroup) {
    HashSet<ConstraintAnchor> hashSet = this.mDependents;
    if (hashSet != null) {
      Iterator<ConstraintAnchor> iterator = hashSet.iterator();
      while (iterator.hasNext())
        Grouping.findDependents(((ConstraintAnchor)iterator.next()).mOwner, paramInt, paramArrayList, paramWidgetGroup); 
    } 
  }
  
  public HashSet<ConstraintAnchor> getDependents() {
    return this.mDependents;
  }
  
  public int getFinalValue() {
    return !this.mHasFinalValue ? 0 : this.mFinalValue;
  }
  
  public int getMargin() {
    if (this.mOwner.getVisibility() == 8)
      return 0; 
    if (this.mGoneMargin != Integer.MIN_VALUE) {
      ConstraintAnchor constraintAnchor = this.mTarget;
      if (constraintAnchor != null && constraintAnchor.mOwner.getVisibility() == 8)
        return this.mGoneMargin; 
    } 
    return this.mMargin;
  }
  
  public final ConstraintAnchor getOpposite() {
    switch (this.mType) {
      default:
        throw new AssertionError(this.mType.name());
      case null:
        return this.mOwner.mTop;
      case null:
        return this.mOwner.mBottom;
      case null:
        return this.mOwner.mLeft;
      case null:
        return this.mOwner.mRight;
      case null:
      case null:
      case null:
      case null:
      case null:
        break;
    } 
    return null;
  }
  
  public ConstraintWidget getOwner() {
    return this.mOwner;
  }
  
  public SolverVariable getSolverVariable() {
    return this.mSolverVariable;
  }
  
  public ConstraintAnchor getTarget() {
    return this.mTarget;
  }
  
  public Type getType() {
    return this.mType;
  }
  
  public boolean hasCenteredDependents() {
    HashSet<ConstraintAnchor> hashSet = this.mDependents;
    if (hashSet == null)
      return false; 
    Iterator<ConstraintAnchor> iterator = hashSet.iterator();
    while (iterator.hasNext()) {
      if (((ConstraintAnchor)iterator.next()).getOpposite().isConnected())
        return true; 
    } 
    return false;
  }
  
  public boolean hasDependents() {
    HashSet<ConstraintAnchor> hashSet = this.mDependents;
    boolean bool = false;
    if (hashSet == null)
      return false; 
    if (hashSet.size() > 0)
      bool = true; 
    return bool;
  }
  
  public boolean hasFinalValue() {
    return this.mHasFinalValue;
  }
  
  public boolean isConnected() {
    boolean bool;
    if (this.mTarget != null) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isConnectionAllowed(ConstraintWidget paramConstraintWidget) {
    if (isConnectionToMe(paramConstraintWidget, new HashSet<>()))
      return false; 
    ConstraintWidget constraintWidget = getOwner().getParent();
    return (constraintWidget == paramConstraintWidget) ? true : ((paramConstraintWidget.getParent() == constraintWidget));
  }
  
  public boolean isConnectionAllowed(ConstraintWidget paramConstraintWidget, ConstraintAnchor paramConstraintAnchor) {
    return isConnectionAllowed(paramConstraintWidget);
  }
  
  public boolean isSideAnchor() {
    switch (this.mType) {
      default:
        throw new AssertionError(this.mType.name());
      case null:
      case null:
      case null:
      case null:
        return true;
      case null:
      case null:
      case null:
      case null:
      case null:
        break;
    } 
    return false;
  }
  
  public boolean isSimilarDimensionConnection(ConstraintAnchor paramConstraintAnchor) {
    Type type1 = paramConstraintAnchor.getType();
    Type type2 = this.mType;
    boolean bool3 = true;
    boolean bool1 = true;
    boolean bool2 = true;
    if (type1 == type2)
      return true; 
    switch (this.mType) {
      default:
        throw new AssertionError(this.mType.name());
      case null:
        return false;
      case null:
      case null:
      case null:
      case null:
        bool1 = bool2;
        if (type1 != Type.TOP) {
          bool1 = bool2;
          if (type1 != Type.BOTTOM) {
            bool1 = bool2;
            if (type1 != Type.CENTER_Y)
              if (type1 == Type.BASELINE) {
                bool1 = bool2;
              } else {
                bool1 = false;
              }  
          } 
        } 
        return bool1;
      case null:
      case null:
      case null:
        bool1 = bool3;
        if (type1 != Type.LEFT) {
          bool1 = bool3;
          if (type1 != Type.RIGHT)
            if (type1 == Type.CENTER_X) {
              bool1 = bool3;
            } else {
              bool1 = false;
            }  
        } 
        return bool1;
      case null:
        break;
    } 
    if (type1 == Type.BASELINE)
      bool1 = false; 
    return bool1;
  }
  
  public boolean isValidConnection(ConstraintAnchor paramConstraintAnchor) {
    // Byte code:
    //   0: iconst_0
    //   1: istore #4
    //   3: iconst_0
    //   4: istore_3
    //   5: iconst_0
    //   6: istore #5
    //   8: aload_1
    //   9: ifnonnull -> 14
    //   12: iconst_0
    //   13: ireturn
    //   14: aload_1
    //   15: invokevirtual getType : ()Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   18: astore #6
    //   20: aload_0
    //   21: getfield mType : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   24: astore #7
    //   26: aload #6
    //   28: aload #7
    //   30: if_acmpne -> 65
    //   33: aload #7
    //   35: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.BASELINE : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   38: if_acmpne -> 63
    //   41: aload_1
    //   42: invokevirtual getOwner : ()Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   45: invokevirtual hasBaseline : ()Z
    //   48: ifeq -> 61
    //   51: aload_0
    //   52: invokevirtual getOwner : ()Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   55: invokevirtual hasBaseline : ()Z
    //   58: ifne -> 63
    //   61: iconst_0
    //   62: ireturn
    //   63: iconst_1
    //   64: ireturn
    //   65: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$1.$SwitchMap$androidx$constraintlayout$core$widgets$ConstraintAnchor$Type : [I
    //   68: aload_0
    //   69: getfield mType : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   72: invokevirtual ordinal : ()I
    //   75: iaload
    //   76: tableswitch default -> 128, 1 -> 286, 2 -> 227, 3 -> 227, 4 -> 168, 5 -> 168, 6 -> 145, 7 -> 143, 8 -> 143, 9 -> 143
    //   128: new java/lang/AssertionError
    //   131: dup
    //   132: aload_0
    //   133: getfield mType : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   136: invokevirtual name : ()Ljava/lang/String;
    //   139: invokespecial <init> : (Ljava/lang/Object;)V
    //   142: athrow
    //   143: iconst_0
    //   144: ireturn
    //   145: aload #6
    //   147: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.LEFT : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   150: if_acmpeq -> 166
    //   153: aload #6
    //   155: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.RIGHT : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   158: if_acmpne -> 164
    //   161: goto -> 166
    //   164: iconst_1
    //   165: ireturn
    //   166: iconst_0
    //   167: ireturn
    //   168: aload #6
    //   170: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.TOP : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   173: if_acmpeq -> 192
    //   176: aload #6
    //   178: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.BOTTOM : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   181: if_acmpne -> 187
    //   184: goto -> 192
    //   187: iconst_0
    //   188: istore_2
    //   189: goto -> 194
    //   192: iconst_1
    //   193: istore_2
    //   194: iload_2
    //   195: istore_3
    //   196: aload_1
    //   197: invokevirtual getOwner : ()Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   200: instanceof androidx/constraintlayout/core/widgets/Guideline
    //   203: ifeq -> 225
    //   206: iload_2
    //   207: ifne -> 221
    //   210: iload #5
    //   212: istore_2
    //   213: aload #6
    //   215: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.CENTER_Y : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   218: if_acmpne -> 223
    //   221: iconst_1
    //   222: istore_2
    //   223: iload_2
    //   224: istore_3
    //   225: iload_3
    //   226: ireturn
    //   227: aload #6
    //   229: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.LEFT : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   232: if_acmpeq -> 251
    //   235: aload #6
    //   237: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.RIGHT : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   240: if_acmpne -> 246
    //   243: goto -> 251
    //   246: iconst_0
    //   247: istore_2
    //   248: goto -> 253
    //   251: iconst_1
    //   252: istore_2
    //   253: iload_2
    //   254: istore_3
    //   255: aload_1
    //   256: invokevirtual getOwner : ()Landroidx/constraintlayout/core/widgets/ConstraintWidget;
    //   259: instanceof androidx/constraintlayout/core/widgets/Guideline
    //   262: ifeq -> 284
    //   265: iload_2
    //   266: ifne -> 280
    //   269: iload #4
    //   271: istore_2
    //   272: aload #6
    //   274: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.CENTER_X : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   277: if_acmpne -> 282
    //   280: iconst_1
    //   281: istore_2
    //   282: iload_2
    //   283: istore_3
    //   284: iload_3
    //   285: ireturn
    //   286: iload_3
    //   287: istore_2
    //   288: aload #6
    //   290: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.BASELINE : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   293: if_acmpeq -> 318
    //   296: iload_3
    //   297: istore_2
    //   298: aload #6
    //   300: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.CENTER_X : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   303: if_acmpeq -> 318
    //   306: iload_3
    //   307: istore_2
    //   308: aload #6
    //   310: getstatic androidx/constraintlayout/core/widgets/ConstraintAnchor$Type.CENTER_Y : Landroidx/constraintlayout/core/widgets/ConstraintAnchor$Type;
    //   313: if_acmpeq -> 318
    //   316: iconst_1
    //   317: istore_2
    //   318: iload_2
    //   319: ireturn
  }
  
  public boolean isVerticalAnchor() {
    switch (this.mType) {
      default:
        throw new AssertionError(this.mType.name());
      case null:
      case null:
      case null:
      case null:
      case null:
        return true;
      case null:
      case null:
      case null:
      case null:
        break;
    } 
    return false;
  }
  
  public void reset() {
    ConstraintAnchor constraintAnchor = this.mTarget;
    if (constraintAnchor != null) {
      HashSet<ConstraintAnchor> hashSet = constraintAnchor.mDependents;
      if (hashSet != null) {
        hashSet.remove(this);
        if (this.mTarget.mDependents.size() == 0)
          this.mTarget.mDependents = null; 
      } 
    } 
    this.mDependents = null;
    this.mTarget = null;
    this.mMargin = 0;
    this.mGoneMargin = Integer.MIN_VALUE;
    this.mHasFinalValue = false;
    this.mFinalValue = 0;
  }
  
  public void resetFinalResolution() {
    this.mHasFinalValue = false;
    this.mFinalValue = 0;
  }
  
  public void resetSolverVariable(Cache paramCache) {
    SolverVariable solverVariable = this.mSolverVariable;
    if (solverVariable == null) {
      this.mSolverVariable = new SolverVariable(SolverVariable.Type.UNRESTRICTED, null);
    } else {
      solverVariable.reset();
    } 
  }
  
  public void setFinalValue(int paramInt) {
    this.mFinalValue = paramInt;
    this.mHasFinalValue = true;
  }
  
  public void setGoneMargin(int paramInt) {
    if (isConnected())
      this.mGoneMargin = paramInt; 
  }
  
  public void setMargin(int paramInt) {
    if (isConnected())
      this.mMargin = paramInt; 
  }
  
  public String toString() {
    return this.mOwner.getDebugName() + ":" + this.mType.toString();
  }
  
  public enum Type {
    BASELINE, BOTTOM, CENTER, CENTER_X, CENTER_Y, LEFT, NONE, RIGHT, TOP;
    
    private static final Type[] $VALUES;
    
    static {
      Type type3 = new Type("NONE", 0);
      NONE = type3;
      Type type6 = new Type("LEFT", 1);
      LEFT = type6;
      Type type1 = new Type("TOP", 2);
      TOP = type1;
      Type type5 = new Type("RIGHT", 3);
      RIGHT = type5;
      Type type2 = new Type("BOTTOM", 4);
      BOTTOM = type2;
      Type type9 = new Type("BASELINE", 5);
      BASELINE = type9;
      Type type7 = new Type("CENTER", 6);
      CENTER = type7;
      Type type4 = new Type("CENTER_X", 7);
      CENTER_X = type4;
      Type type8 = new Type("CENTER_Y", 8);
      CENTER_Y = type8;
      $VALUES = new Type[] { type3, type6, type1, type5, type2, type9, type7, type4, type8 };
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\widgets\ConstraintAnchor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */