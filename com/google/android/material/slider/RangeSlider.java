package com.google.android.material.slider;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.AbsSavedState;
import android.view.KeyEvent;
import android.view.MotionEvent;
import com.google.android.material.R;
import com.google.android.material.internal.ThemeEnforcement;
import java.util.ArrayList;
import java.util.List;

public class RangeSlider extends BaseSlider<RangeSlider, RangeSlider.OnChangeListener, RangeSlider.OnSliderTouchListener> {
  private float minSeparation;
  
  private int separationUnit;
  
  public RangeSlider(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public RangeSlider(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, R.attr.sliderStyle);
  }
  
  public RangeSlider(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    TypedArray typedArray = ThemeEnforcement.obtainStyledAttributes(paramContext, paramAttributeSet, R.styleable.RangeSlider, paramInt, DEF_STYLE_RES, new int[0]);
    if (typedArray.hasValue(R.styleable.RangeSlider_values)) {
      paramInt = typedArray.getResourceId(R.styleable.RangeSlider_values, 0);
      setValues(convertToFloat(typedArray.getResources().obtainTypedArray(paramInt)));
    } 
    this.minSeparation = typedArray.getDimension(R.styleable.RangeSlider_minSeparation, 0.0F);
    typedArray.recycle();
  }
  
  private static List<Float> convertToFloat(TypedArray paramTypedArray) {
    ArrayList<Float> arrayList = new ArrayList();
    for (byte b = 0; b < paramTypedArray.length(); b++)
      arrayList.add(Float.valueOf(paramTypedArray.getFloat(b, -1.0F))); 
    return arrayList;
  }
  
  public float getMinSeparation() {
    return this.minSeparation;
  }
  
  public List<Float> getValues() {
    return super.getValues();
  }
  
  protected void onRestoreInstanceState(Parcelable paramParcelable) {
    RangeSliderState rangeSliderState = (RangeSliderState)paramParcelable;
    super.onRestoreInstanceState(rangeSliderState.getSuperState());
    this.minSeparation = rangeSliderState.minSeparation;
    int i = rangeSliderState.separationUnit;
    this.separationUnit = i;
    setSeparationUnit(i);
  }
  
  public Parcelable onSaveInstanceState() {
    RangeSliderState rangeSliderState = new RangeSliderState(super.onSaveInstanceState());
    RangeSliderState.access$002(rangeSliderState, this.minSeparation);
    RangeSliderState.access$102(rangeSliderState, this.separationUnit);
    return (Parcelable)rangeSliderState;
  }
  
  public void setMinSeparation(float paramFloat) {
    this.minSeparation = paramFloat;
    this.separationUnit = 0;
    setSeparationUnit(0);
  }
  
  public void setMinSeparationValue(float paramFloat) {
    this.minSeparation = paramFloat;
    this.separationUnit = 1;
    setSeparationUnit(1);
  }
  
  public void setValues(List<Float> paramList) {
    super.setValues(paramList);
  }
  
  public void setValues(Float... paramVarArgs) {
    super.setValues(paramVarArgs);
  }
  
  public static interface OnChangeListener extends BaseOnChangeListener<RangeSlider> {}
  
  public static interface OnSliderTouchListener extends BaseOnSliderTouchListener<RangeSlider> {}
  
  static class RangeSliderState extends AbsSavedState {
    public static final Parcelable.Creator<RangeSliderState> CREATOR = new Parcelable.Creator<RangeSliderState>() {
        public RangeSlider.RangeSliderState createFromParcel(Parcel param2Parcel) {
          return new RangeSlider.RangeSliderState(param2Parcel);
        }
        
        public RangeSlider.RangeSliderState[] newArray(int param2Int) {
          return new RangeSlider.RangeSliderState[param2Int];
        }
      };
    
    private float minSeparation;
    
    private int separationUnit;
    
    private RangeSliderState(Parcel param1Parcel) {
      super(param1Parcel.readParcelable(RangeSliderState.class.getClassLoader()));
      this.minSeparation = param1Parcel.readFloat();
      this.separationUnit = param1Parcel.readInt();
    }
    
    RangeSliderState(Parcelable param1Parcelable) {
      super(param1Parcelable);
    }
    
    public void writeToParcel(Parcel param1Parcel, int param1Int) {
      super.writeToParcel(param1Parcel, param1Int);
      param1Parcel.writeFloat(this.minSeparation);
      param1Parcel.writeInt(this.separationUnit);
    }
  }
  
  static final class null implements Parcelable.Creator<RangeSliderState> {
    public RangeSlider.RangeSliderState createFromParcel(Parcel param1Parcel) {
      return new RangeSlider.RangeSliderState(param1Parcel);
    }
    
    public RangeSlider.RangeSliderState[] newArray(int param1Int) {
      return new RangeSlider.RangeSliderState[param1Int];
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\slider\RangeSlider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */