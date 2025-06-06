package androidx.constraintlayout.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.util.SparseArray;
import androidx.constraintlayout.core.widgets.ConstraintWidget;
import androidx.constraintlayout.core.widgets.ConstraintWidgetContainer;
import androidx.constraintlayout.core.widgets.Helper;
import androidx.constraintlayout.core.widgets.HelperWidget;

public class Barrier extends ConstraintHelper {
  public static final int BOTTOM = 3;
  
  public static final int END = 6;
  
  public static final int LEFT = 0;
  
  public static final int RIGHT = 1;
  
  public static final int START = 5;
  
  public static final int TOP = 2;
  
  private androidx.constraintlayout.core.widgets.Barrier mBarrier;
  
  private int mIndicatedType;
  
  private int mResolvedType;
  
  public Barrier(Context paramContext) {
    super(paramContext);
    setVisibility(8);
  }
  
  public Barrier(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    setVisibility(8);
  }
  
  public Barrier(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    setVisibility(8);
  }
  
  private void updateType(ConstraintWidget paramConstraintWidget, int paramInt, boolean paramBoolean) {
    this.mResolvedType = paramInt;
    if (Build.VERSION.SDK_INT < 17) {
      paramInt = this.mIndicatedType;
      if (paramInt == 5) {
        this.mResolvedType = 0;
      } else if (paramInt == 6) {
        this.mResolvedType = 1;
      } 
    } else if (paramBoolean) {
      paramInt = this.mIndicatedType;
      if (paramInt == 5) {
        this.mResolvedType = 1;
      } else if (paramInt == 6) {
        this.mResolvedType = 0;
      } 
    } else {
      paramInt = this.mIndicatedType;
      if (paramInt == 5) {
        this.mResolvedType = 0;
      } else if (paramInt == 6) {
        this.mResolvedType = 1;
      } 
    } 
    if (paramConstraintWidget instanceof androidx.constraintlayout.core.widgets.Barrier)
      ((androidx.constraintlayout.core.widgets.Barrier)paramConstraintWidget).setBarrierType(this.mResolvedType); 
  }
  
  @Deprecated
  public boolean allowsGoneWidget() {
    return this.mBarrier.getAllowsGoneWidget();
  }
  
  public boolean getAllowsGoneWidget() {
    return this.mBarrier.getAllowsGoneWidget();
  }
  
  public int getMargin() {
    return this.mBarrier.getMargin();
  }
  
  public int getType() {
    return this.mIndicatedType;
  }
  
  protected void init(AttributeSet paramAttributeSet) {
    super.init(paramAttributeSet);
    this.mBarrier = new androidx.constraintlayout.core.widgets.Barrier();
    if (paramAttributeSet != null) {
      TypedArray typedArray = getContext().obtainStyledAttributes(paramAttributeSet, R.styleable.ConstraintLayout_Layout);
      int i = typedArray.getIndexCount();
      for (byte b = 0; b < i; b++) {
        int j = typedArray.getIndex(b);
        if (j == R.styleable.ConstraintLayout_Layout_barrierDirection) {
          setType(typedArray.getInt(j, 0));
        } else if (j == R.styleable.ConstraintLayout_Layout_barrierAllowsGoneWidgets) {
          this.mBarrier.setAllowsGoneWidget(typedArray.getBoolean(j, true));
        } else if (j == R.styleable.ConstraintLayout_Layout_barrierMargin) {
          j = typedArray.getDimensionPixelSize(j, 0);
          this.mBarrier.setMargin(j);
        } 
      } 
      typedArray.recycle();
    } 
    this.mHelperWidget = (Helper)this.mBarrier;
    validateParams();
  }
  
  public void loadParameters(ConstraintSet.Constraint paramConstraint, HelperWidget paramHelperWidget, ConstraintLayout.LayoutParams paramLayoutParams, SparseArray<ConstraintWidget> paramSparseArray) {
    super.loadParameters(paramConstraint, paramHelperWidget, paramLayoutParams, paramSparseArray);
    if (paramHelperWidget instanceof androidx.constraintlayout.core.widgets.Barrier) {
      androidx.constraintlayout.core.widgets.Barrier barrier = (androidx.constraintlayout.core.widgets.Barrier)paramHelperWidget;
      boolean bool = ((ConstraintWidgetContainer)paramHelperWidget.getParent()).isRtl();
      updateType((ConstraintWidget)barrier, paramConstraint.layout.mBarrierDirection, bool);
      barrier.setAllowsGoneWidget(paramConstraint.layout.mBarrierAllowsGoneWidgets);
      barrier.setMargin(paramConstraint.layout.mBarrierMargin);
    } 
  }
  
  public void resolveRtl(ConstraintWidget paramConstraintWidget, boolean paramBoolean) {
    updateType(paramConstraintWidget, this.mIndicatedType, paramBoolean);
  }
  
  public void setAllowsGoneWidget(boolean paramBoolean) {
    this.mBarrier.setAllowsGoneWidget(paramBoolean);
  }
  
  public void setDpMargin(int paramInt) {
    float f = (getResources().getDisplayMetrics()).density;
    paramInt = (int)(paramInt * f + 0.5F);
    this.mBarrier.setMargin(paramInt);
  }
  
  public void setMargin(int paramInt) {
    this.mBarrier.setMargin(paramInt);
  }
  
  public void setType(int paramInt) {
    this.mIndicatedType = paramInt;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\widget\Barrier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */