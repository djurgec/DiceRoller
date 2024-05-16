package com.google.android.material.animation;

import android.util.Property;
import android.view.ViewGroup;
import com.google.android.material.R;

public class ChildrenAlphaProperty extends Property<ViewGroup, Float> {
  public static final Property<ViewGroup, Float> CHILDREN_ALPHA = new ChildrenAlphaProperty("childrenAlpha");
  
  private ChildrenAlphaProperty(String paramString) {
    super(Float.class, paramString);
  }
  
  public Float get(ViewGroup paramViewGroup) {
    Float float_ = (Float)paramViewGroup.getTag(R.id.mtrl_internal_children_alpha_tag);
    return (float_ != null) ? float_ : Float.valueOf(1.0F);
  }
  
  public void set(ViewGroup paramViewGroup, Float paramFloat) {
    float f = paramFloat.floatValue();
    paramViewGroup.setTag(R.id.mtrl_internal_children_alpha_tag, Float.valueOf(f));
    byte b = 0;
    int i = paramViewGroup.getChildCount();
    while (b < i) {
      paramViewGroup.getChildAt(b).setAlpha(f);
      b++;
    } 
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\animation\ChildrenAlphaProperty.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */