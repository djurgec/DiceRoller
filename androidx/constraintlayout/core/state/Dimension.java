package androidx.constraintlayout.core.state;

import androidx.constraintlayout.core.widgets.ConstraintWidget;

public class Dimension {
  public static final Object FIXED_DIMENSION = new Object();
  
  public static final Object PARENT_DIMENSION;
  
  public static final Object PERCENT_DIMENSION;
  
  public static final Object RATIO_DIMENSION;
  
  public static final Object SPREAD_DIMENSION;
  
  public static final Object WRAP_DIMENSION = new Object();
  
  private final int WRAP_CONTENT = -2;
  
  Object mInitialValue = WRAP_DIMENSION;
  
  boolean mIsSuggested = false;
  
  int mMax = Integer.MAX_VALUE;
  
  int mMin = 0;
  
  float mPercent = 1.0F;
  
  String mRatioString = null;
  
  int mValue = 0;
  
  static {
    SPREAD_DIMENSION = new Object();
    PARENT_DIMENSION = new Object();
    PERCENT_DIMENSION = new Object();
    RATIO_DIMENSION = new Object();
  }
  
  private Dimension() {}
  
  private Dimension(Object paramObject) {
    this.mInitialValue = paramObject;
  }
  
  public static Dimension Fixed(int paramInt) {
    Dimension dimension = new Dimension(FIXED_DIMENSION);
    dimension.fixed(paramInt);
    return dimension;
  }
  
  public static Dimension Fixed(Object paramObject) {
    Dimension dimension = new Dimension(FIXED_DIMENSION);
    dimension.fixed(paramObject);
    return dimension;
  }
  
  public static Dimension Parent() {
    return new Dimension(PARENT_DIMENSION);
  }
  
  public static Dimension Percent(Object paramObject, float paramFloat) {
    Dimension dimension = new Dimension(PERCENT_DIMENSION);
    dimension.percent(paramObject, paramFloat);
    return dimension;
  }
  
  public static Dimension Ratio(String paramString) {
    Dimension dimension = new Dimension(RATIO_DIMENSION);
    dimension.ratio(paramString);
    return dimension;
  }
  
  public static Dimension Spread() {
    return new Dimension(SPREAD_DIMENSION);
  }
  
  public static Dimension Suggested(int paramInt) {
    Dimension dimension = new Dimension();
    dimension.suggested(paramInt);
    return dimension;
  }
  
  public static Dimension Suggested(Object paramObject) {
    Dimension dimension = new Dimension();
    dimension.suggested(paramObject);
    return dimension;
  }
  
  public static Dimension Wrap() {
    return new Dimension(WRAP_DIMENSION);
  }
  
  public void apply(State paramState, ConstraintWidget paramConstraintWidget, int paramInt) {
    String str = this.mRatioString;
    if (str != null)
      paramConstraintWidget.setDimensionRatio(str); 
    if (paramInt == 0) {
      if (this.mIsSuggested) {
        paramConstraintWidget.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT);
        paramInt = 0;
        Object object = this.mInitialValue;
        if (object == WRAP_DIMENSION) {
          paramInt = 1;
        } else if (object == PERCENT_DIMENSION) {
          paramInt = 2;
        } 
        paramConstraintWidget.setHorizontalMatchStyle(paramInt, this.mMin, this.mMax, this.mPercent);
      } else {
        paramInt = this.mMin;
        if (paramInt > 0)
          paramConstraintWidget.setMinWidth(paramInt); 
        paramInt = this.mMax;
        if (paramInt < Integer.MAX_VALUE)
          paramConstraintWidget.setMaxWidth(paramInt); 
        Object object = this.mInitialValue;
        if (object == WRAP_DIMENSION) {
          paramConstraintWidget.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.WRAP_CONTENT);
        } else if (object == PARENT_DIMENSION) {
          paramConstraintWidget.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.MATCH_PARENT);
        } else if (object == null) {
          paramConstraintWidget.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
          paramConstraintWidget.setWidth(this.mValue);
        } 
      } 
    } else if (this.mIsSuggested) {
      paramConstraintWidget.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT);
      paramInt = 0;
      Object object = this.mInitialValue;
      if (object == WRAP_DIMENSION) {
        paramInt = 1;
      } else if (object == PERCENT_DIMENSION) {
        paramInt = 2;
      } 
      paramConstraintWidget.setVerticalMatchStyle(paramInt, this.mMin, this.mMax, this.mPercent);
    } else {
      paramInt = this.mMin;
      if (paramInt > 0)
        paramConstraintWidget.setMinHeight(paramInt); 
      paramInt = this.mMax;
      if (paramInt < Integer.MAX_VALUE)
        paramConstraintWidget.setMaxHeight(paramInt); 
      Object object = this.mInitialValue;
      if (object == WRAP_DIMENSION) {
        paramConstraintWidget.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.WRAP_CONTENT);
      } else if (object == PARENT_DIMENSION) {
        paramConstraintWidget.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.MATCH_PARENT);
      } else if (object == null) {
        paramConstraintWidget.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
        paramConstraintWidget.setHeight(this.mValue);
      } 
    } 
  }
  
  public boolean equalsFixedValue(int paramInt) {
    return (this.mInitialValue == null && this.mValue == paramInt);
  }
  
  public Dimension fixed(int paramInt) {
    this.mInitialValue = null;
    this.mValue = paramInt;
    return this;
  }
  
  public Dimension fixed(Object paramObject) {
    this.mInitialValue = paramObject;
    if (paramObject instanceof Integer) {
      this.mValue = ((Integer)paramObject).intValue();
      this.mInitialValue = null;
    } 
    return this;
  }
  
  int getValue() {
    return this.mValue;
  }
  
  public Dimension max(int paramInt) {
    if (this.mMax >= 0)
      this.mMax = paramInt; 
    return this;
  }
  
  public Dimension max(Object paramObject) {
    Object object = WRAP_DIMENSION;
    if (paramObject == object && this.mIsSuggested) {
      this.mInitialValue = object;
      this.mMax = Integer.MAX_VALUE;
    } 
    return this;
  }
  
  public Dimension min(int paramInt) {
    if (paramInt >= 0)
      this.mMin = paramInt; 
    return this;
  }
  
  public Dimension min(Object paramObject) {
    if (paramObject == WRAP_DIMENSION)
      this.mMin = -2; 
    return this;
  }
  
  public Dimension percent(Object paramObject, float paramFloat) {
    this.mPercent = paramFloat;
    return this;
  }
  
  public Dimension ratio(String paramString) {
    this.mRatioString = paramString;
    return this;
  }
  
  void setValue(int paramInt) {
    this.mIsSuggested = false;
    this.mInitialValue = null;
    this.mValue = paramInt;
  }
  
  public Dimension suggested(int paramInt) {
    this.mIsSuggested = true;
    return this;
  }
  
  public Dimension suggested(Object paramObject) {
    this.mInitialValue = paramObject;
    this.mIsSuggested = true;
    return this;
  }
  
  public enum Type {
    FIXED, MATCH_CONSTRAINT, MATCH_PARENT, WRAP;
    
    private static final Type[] $VALUES;
    
    static {
      Type type2 = new Type("FIXED", 0);
      FIXED = type2;
      Type type3 = new Type("WRAP", 1);
      WRAP = type3;
      Type type1 = new Type("MATCH_PARENT", 2);
      MATCH_PARENT = type1;
      Type type4 = new Type("MATCH_CONSTRAINT", 3);
      MATCH_CONSTRAINT = type4;
      $VALUES = new Type[] { type2, type3, type1, type4 };
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\state\Dimension.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */