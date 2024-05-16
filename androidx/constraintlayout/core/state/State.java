package androidx.constraintlayout.core.state;

import androidx.constraintlayout.core.state.helpers.AlignHorizontallyReference;
import androidx.constraintlayout.core.state.helpers.AlignVerticallyReference;
import androidx.constraintlayout.core.state.helpers.BarrierReference;
import androidx.constraintlayout.core.state.helpers.Facade;
import androidx.constraintlayout.core.state.helpers.GuidelineReference;
import androidx.constraintlayout.core.state.helpers.HorizontalChainReference;
import androidx.constraintlayout.core.state.helpers.VerticalChainReference;
import androidx.constraintlayout.core.widgets.ConstraintWidget;
import androidx.constraintlayout.core.widgets.ConstraintWidgetContainer;
import androidx.constraintlayout.core.widgets.HelperWidget;
import java.util.ArrayList;
import java.util.HashMap;

public class State {
  static final int CONSTRAINT_RATIO = 2;
  
  static final int CONSTRAINT_SPREAD = 0;
  
  static final int CONSTRAINT_WRAP = 1;
  
  public static final Integer PARENT = Integer.valueOf(0);
  
  static final int UNKNOWN = -1;
  
  protected HashMap<Object, HelperReference> mHelperReferences = new HashMap<>();
  
  public final ConstraintReference mParent;
  
  protected HashMap<Object, Reference> mReferences = new HashMap<>();
  
  HashMap<String, ArrayList<String>> mTags = new HashMap<>();
  
  private int numHelpers;
  
  public State() {
    ConstraintReference constraintReference = new ConstraintReference(this);
    this.mParent = constraintReference;
    this.numHelpers = 0;
    this.mReferences.put(PARENT, constraintReference);
  }
  
  private String createHelperKey() {
    StringBuilder stringBuilder = (new StringBuilder()).append("__HELPER_KEY_");
    int i = this.numHelpers;
    this.numHelpers = i + 1;
    return stringBuilder.append(i).append("__").toString();
  }
  
  public void apply(ConstraintWidgetContainer paramConstraintWidgetContainer) {
    paramConstraintWidgetContainer.removeAllChildren();
    this.mParent.getWidth().apply(this, (ConstraintWidget)paramConstraintWidgetContainer, 0);
    this.mParent.getHeight().apply(this, (ConstraintWidget)paramConstraintWidgetContainer, 1);
    for (Object object : this.mHelperReferences.keySet()) {
      HelperWidget helperWidget = ((HelperReference)this.mHelperReferences.get(object)).getHelperWidget();
      if (helperWidget != null) {
        Reference reference2 = this.mReferences.get(object);
        Reference reference1 = reference2;
        if (reference2 == null)
          reference1 = constraints(object); 
        reference1.setConstraintWidget((ConstraintWidget)helperWidget);
      } 
    } 
    for (Object object : this.mReferences.keySet()) {
      Reference reference = this.mReferences.get(object);
      if (reference != this.mParent && reference.getFacade() instanceof HelperReference) {
        HelperWidget helperWidget = ((HelperReference)reference.getFacade()).getHelperWidget();
        if (helperWidget != null) {
          Reference reference1 = this.mReferences.get(object);
          reference = reference1;
          if (reference1 == null)
            reference = constraints(object); 
          reference.setConstraintWidget((ConstraintWidget)helperWidget);
        } 
      } 
    } 
    for (Reference reference : this.mReferences.keySet()) {
      reference = this.mReferences.get(reference);
      if (reference != this.mParent) {
        ConstraintWidget constraintWidget = reference.getConstraintWidget();
        constraintWidget.setDebugName(reference.getKey().toString());
        constraintWidget.setParent(null);
        if (reference.getFacade() instanceof GuidelineReference)
          reference.apply(); 
        paramConstraintWidgetContainer.add(constraintWidget);
        continue;
      } 
      reference.setConstraintWidget((ConstraintWidget)paramConstraintWidgetContainer);
    } 
    for (HelperReference helperReference : this.mHelperReferences.keySet()) {
      helperReference = this.mHelperReferences.get(helperReference);
      if (helperReference.getHelperWidget() != null) {
        for (Reference reference : helperReference.mReferences) {
          reference = this.mReferences.get(reference);
          helperReference.getHelperWidget().add(reference.getConstraintWidget());
        } 
        helperReference.apply();
        continue;
      } 
      helperReference.apply();
    } 
    for (HelperWidget helperWidget : this.mReferences.keySet()) {
      Reference reference = this.mReferences.get(helperWidget);
      if (reference != this.mParent && reference.getFacade() instanceof HelperReference) {
        helperReference = (HelperReference)reference.getFacade();
        helperWidget = helperReference.getHelperWidget();
        if (helperWidget != null) {
          for (HelperReference helperReference : helperReference.mReferences) {
            Reference reference1 = this.mReferences.get(helperReference);
            if (reference1 != null) {
              helperWidget.add(reference1.getConstraintWidget());
              continue;
            } 
            if (helperReference instanceof Reference) {
              helperWidget.add(helperReference.getConstraintWidget());
              continue;
            } 
            System.out.println("couldn't find reference for " + helperReference);
          } 
          reference.apply();
        } 
      } 
    } 
    for (Object object : this.mReferences.keySet()) {
      Reference reference = this.mReferences.get(object);
      reference.apply();
      ConstraintWidget constraintWidget = reference.getConstraintWidget();
      if (constraintWidget != null && object != null)
        constraintWidget.stringId = object.toString(); 
    } 
  }
  
  public BarrierReference barrier(Object paramObject, Direction paramDirection) {
    paramObject = constraints(paramObject);
    if (paramObject.getFacade() == null || !(paramObject.getFacade() instanceof BarrierReference)) {
      BarrierReference barrierReference = new BarrierReference(this);
      barrierReference.setBarrierDirection(paramDirection);
      paramObject.setFacade((Facade)barrierReference);
    } 
    return (BarrierReference)paramObject.getFacade();
  }
  
  public AlignHorizontallyReference centerHorizontally(Object... paramVarArgs) {
    AlignHorizontallyReference alignHorizontallyReference = (AlignHorizontallyReference)helper(null, Helper.ALIGN_HORIZONTALLY);
    alignHorizontallyReference.add(paramVarArgs);
    return alignHorizontallyReference;
  }
  
  public AlignVerticallyReference centerVertically(Object... paramVarArgs) {
    AlignVerticallyReference alignVerticallyReference = (AlignVerticallyReference)helper(null, Helper.ALIGN_VERTICALLY);
    alignVerticallyReference.add(paramVarArgs);
    return alignVerticallyReference;
  }
  
  public ConstraintReference constraints(Object paramObject) {
    Reference reference2 = this.mReferences.get(paramObject);
    Reference reference1 = reference2;
    if (reference2 == null) {
      reference1 = createConstraintReference(paramObject);
      this.mReferences.put(paramObject, reference1);
      reference1.setKey(paramObject);
    } 
    return (reference1 instanceof ConstraintReference) ? (ConstraintReference)reference1 : null;
  }
  
  public int convertDimension(Object paramObject) {
    return (paramObject instanceof Float) ? ((Float)paramObject).intValue() : ((paramObject instanceof Integer) ? ((Integer)paramObject).intValue() : 0);
  }
  
  public ConstraintReference createConstraintReference(Object paramObject) {
    return new ConstraintReference(this);
  }
  
  public void directMapping() {
    for (Object object : this.mReferences.keySet()) {
      ConstraintReference constraintReference = constraints(object);
      if (!(constraintReference instanceof ConstraintReference))
        continue; 
      constraintReference.setView(object);
    } 
  }
  
  public ArrayList<String> getIdsForTag(String paramString) {
    return this.mTags.containsKey(paramString) ? this.mTags.get(paramString) : null;
  }
  
  public GuidelineReference guideline(Object paramObject, int paramInt) {
    ConstraintReference constraintReference = constraints(paramObject);
    if (constraintReference.getFacade() == null || !(constraintReference.getFacade() instanceof GuidelineReference)) {
      GuidelineReference guidelineReference = new GuidelineReference(this);
      guidelineReference.setOrientation(paramInt);
      guidelineReference.setKey(paramObject);
      constraintReference.setFacade((Facade)guidelineReference);
    } 
    return (GuidelineReference)constraintReference.getFacade();
  }
  
  public State height(Dimension paramDimension) {
    return setHeight(paramDimension);
  }
  
  public HelperReference helper(Object paramObject, Helper paramHelper) {
    Object object = paramObject;
    if (paramObject == null)
      object = createHelperKey(); 
    HelperReference helperReference = this.mHelperReferences.get(object);
    paramObject = helperReference;
    if (helperReference == null) {
      switch (paramHelper) {
        default:
          paramObject = new HelperReference(this, paramHelper);
          break;
        case null:
          paramObject = new BarrierReference(this);
          break;
        case null:
          paramObject = new AlignVerticallyReference(this);
          break;
        case null:
          paramObject = new AlignHorizontallyReference(this);
          break;
        case null:
          paramObject = new VerticalChainReference(this);
          break;
        case null:
          paramObject = new HorizontalChainReference(this);
          break;
      } 
      this.mHelperReferences.put(object, paramObject);
    } 
    return (HelperReference)paramObject;
  }
  
  public HorizontalChainReference horizontalChain() {
    return (HorizontalChainReference)helper(null, Helper.HORIZONTAL_CHAIN);
  }
  
  public HorizontalChainReference horizontalChain(Object... paramVarArgs) {
    HorizontalChainReference horizontalChainReference = (HorizontalChainReference)helper(null, Helper.HORIZONTAL_CHAIN);
    horizontalChainReference.add(paramVarArgs);
    return horizontalChainReference;
  }
  
  public GuidelineReference horizontalGuideline(Object paramObject) {
    return guideline(paramObject, 0);
  }
  
  public void map(Object paramObject1, Object paramObject2) {
    paramObject1 = constraints(paramObject1);
    if (paramObject1 instanceof ConstraintReference)
      ((ConstraintReference)paramObject1).setView(paramObject2); 
  }
  
  Reference reference(Object paramObject) {
    return this.mReferences.get(paramObject);
  }
  
  public void reset() {
    this.mHelperReferences.clear();
    this.mTags.clear();
  }
  
  public boolean sameFixedHeight(int paramInt) {
    return this.mParent.getHeight().equalsFixedValue(paramInt);
  }
  
  public boolean sameFixedWidth(int paramInt) {
    return this.mParent.getWidth().equalsFixedValue(paramInt);
  }
  
  public State setHeight(Dimension paramDimension) {
    this.mParent.setHeight(paramDimension);
    return this;
  }
  
  public void setTag(String paramString1, String paramString2) {
    ConstraintReference constraintReference = constraints(paramString1);
    if (constraintReference instanceof ConstraintReference) {
      ArrayList<String> arrayList;
      constraintReference.setTag(paramString2);
      if (!this.mTags.containsKey(paramString2)) {
        ArrayList<String> arrayList1 = new ArrayList();
        this.mTags.put(paramString2, arrayList1);
        arrayList = arrayList1;
      } else {
        arrayList = this.mTags.get(arrayList);
      } 
      arrayList.add(paramString1);
    } 
  }
  
  public State setWidth(Dimension paramDimension) {
    this.mParent.setWidth(paramDimension);
    return this;
  }
  
  public VerticalChainReference verticalChain() {
    return (VerticalChainReference)helper(null, Helper.VERTICAL_CHAIN);
  }
  
  public VerticalChainReference verticalChain(Object... paramVarArgs) {
    VerticalChainReference verticalChainReference = (VerticalChainReference)helper(null, Helper.VERTICAL_CHAIN);
    verticalChainReference.add(paramVarArgs);
    return verticalChainReference;
  }
  
  public GuidelineReference verticalGuideline(Object paramObject) {
    return guideline(paramObject, 1);
  }
  
  public State width(Dimension paramDimension) {
    return setWidth(paramDimension);
  }
  
  public enum Chain {
    PACKED, SPREAD, SPREAD_INSIDE;
    
    private static final Chain[] $VALUES;
    
    static {
      Chain chain2 = new Chain("SPREAD", 0);
      SPREAD = chain2;
      Chain chain3 = new Chain("SPREAD_INSIDE", 1);
      SPREAD_INSIDE = chain3;
      Chain chain1 = new Chain("PACKED", 2);
      PACKED = chain1;
      $VALUES = new Chain[] { chain2, chain3, chain1 };
    }
  }
  
  public enum Constraint {
    BASELINE_TO_BASELINE, BOTTOM_TO_BOTTOM, BOTTOM_TO_TOP, CENTER_HORIZONTALLY, CENTER_VERTICALLY, CIRCULAR_CONSTRAINT, END_TO_END, END_TO_START, LEFT_TO_LEFT, LEFT_TO_RIGHT, RIGHT_TO_LEFT, RIGHT_TO_RIGHT, START_TO_END, START_TO_START, TOP_TO_BOTTOM, TOP_TO_TOP;
    
    private static final Constraint[] $VALUES;
    
    static {
      Constraint constraint6 = new Constraint("LEFT_TO_LEFT", 0);
      LEFT_TO_LEFT = constraint6;
      Constraint constraint16 = new Constraint("LEFT_TO_RIGHT", 1);
      LEFT_TO_RIGHT = constraint16;
      Constraint constraint3 = new Constraint("RIGHT_TO_LEFT", 2);
      RIGHT_TO_LEFT = constraint3;
      Constraint constraint10 = new Constraint("RIGHT_TO_RIGHT", 3);
      RIGHT_TO_RIGHT = constraint10;
      Constraint constraint9 = new Constraint("START_TO_START", 4);
      START_TO_START = constraint9;
      Constraint constraint4 = new Constraint("START_TO_END", 5);
      START_TO_END = constraint4;
      Constraint constraint7 = new Constraint("END_TO_START", 6);
      END_TO_START = constraint7;
      Constraint constraint1 = new Constraint("END_TO_END", 7);
      END_TO_END = constraint1;
      Constraint constraint5 = new Constraint("TOP_TO_TOP", 8);
      TOP_TO_TOP = constraint5;
      Constraint constraint2 = new Constraint("TOP_TO_BOTTOM", 9);
      TOP_TO_BOTTOM = constraint2;
      Constraint constraint14 = new Constraint("BOTTOM_TO_TOP", 10);
      BOTTOM_TO_TOP = constraint14;
      Constraint constraint15 = new Constraint("BOTTOM_TO_BOTTOM", 11);
      BOTTOM_TO_BOTTOM = constraint15;
      Constraint constraint8 = new Constraint("BASELINE_TO_BASELINE", 12);
      BASELINE_TO_BASELINE = constraint8;
      Constraint constraint11 = new Constraint("CENTER_HORIZONTALLY", 13);
      CENTER_HORIZONTALLY = constraint11;
      Constraint constraint12 = new Constraint("CENTER_VERTICALLY", 14);
      CENTER_VERTICALLY = constraint12;
      Constraint constraint13 = new Constraint("CIRCULAR_CONSTRAINT", 15);
      CIRCULAR_CONSTRAINT = constraint13;
      $VALUES = new Constraint[] { 
          constraint6, constraint16, constraint3, constraint10, constraint9, constraint4, constraint7, constraint1, constraint5, constraint2, 
          constraint14, constraint15, constraint8, constraint11, constraint12, constraint13 };
    }
  }
  
  public enum Direction {
    BOTTOM, END, LEFT, RIGHT, START, TOP;
    
    private static final Direction[] $VALUES;
    
    static {
      Direction direction2 = new Direction("LEFT", 0);
      LEFT = direction2;
      Direction direction1 = new Direction("RIGHT", 1);
      RIGHT = direction1;
      Direction direction3 = new Direction("START", 2);
      START = direction3;
      Direction direction6 = new Direction("END", 3);
      END = direction6;
      Direction direction5 = new Direction("TOP", 4);
      TOP = direction5;
      Direction direction4 = new Direction("BOTTOM", 5);
      BOTTOM = direction4;
      $VALUES = new Direction[] { direction2, direction1, direction3, direction6, direction5, direction4 };
    }
  }
  
  public enum Helper {
    ALIGN_HORIZONTALLY, ALIGN_VERTICALLY, BARRIER, FLOW, HORIZONTAL_CHAIN, LAYER, VERTICAL_CHAIN;
    
    private static final Helper[] $VALUES;
    
    static {
      Helper helper7 = new Helper("HORIZONTAL_CHAIN", 0);
      HORIZONTAL_CHAIN = helper7;
      Helper helper4 = new Helper("VERTICAL_CHAIN", 1);
      VERTICAL_CHAIN = helper4;
      Helper helper1 = new Helper("ALIGN_HORIZONTALLY", 2);
      ALIGN_HORIZONTALLY = helper1;
      Helper helper5 = new Helper("ALIGN_VERTICALLY", 3);
      ALIGN_VERTICALLY = helper5;
      Helper helper2 = new Helper("BARRIER", 4);
      BARRIER = helper2;
      Helper helper6 = new Helper("LAYER", 5);
      LAYER = helper6;
      Helper helper3 = new Helper("FLOW", 6);
      FLOW = helper3;
      $VALUES = new Helper[] { helper7, helper4, helper1, helper5, helper2, helper6, helper3 };
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\core\state\State.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */