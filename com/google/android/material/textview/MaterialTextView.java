package com.google.android.material.textview;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatTextView;
import com.google.android.material.R;
import com.google.android.material.resources.MaterialAttributes;
import com.google.android.material.resources.MaterialResources;
import com.google.android.material.theme.overlay.MaterialThemeOverlay;

public class MaterialTextView extends AppCompatTextView {
  public MaterialTextView(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public MaterialTextView(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, 16842884);
  }
  
  public MaterialTextView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public MaterialTextView(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2) {
    super(MaterialThemeOverlay.wrap(paramContext, paramAttributeSet, paramInt1, paramInt2), paramAttributeSet, paramInt1);
    Context context = getContext();
    if (canApplyTextAppearanceLineHeight(context)) {
      Resources.Theme theme = context.getTheme();
      if (!viewAttrsHasLineHeight(context, theme, paramAttributeSet, paramInt1, paramInt2)) {
        paramInt1 = findViewAppearanceResourceId(theme, paramAttributeSet, paramInt1, paramInt2);
        if (paramInt1 != -1)
          applyLineHeightFromViewAppearance(theme, paramInt1); 
      } 
    } 
  }
  
  private void applyLineHeightFromViewAppearance(Resources.Theme paramTheme, int paramInt) {
    TypedArray typedArray = paramTheme.obtainStyledAttributes(paramInt, R.styleable.MaterialTextAppearance);
    paramInt = readFirstAvailableDimension(getContext(), typedArray, new int[] { R.styleable.MaterialTextAppearance_android_lineHeight, R.styleable.MaterialTextAppearance_lineHeight });
    typedArray.recycle();
    if (paramInt >= 0)
      setLineHeight(paramInt); 
  }
  
  private static boolean canApplyTextAppearanceLineHeight(Context paramContext) {
    return MaterialAttributes.resolveBoolean(paramContext, R.attr.textAppearanceLineHeightEnabled, true);
  }
  
  private static int findViewAppearanceResourceId(Resources.Theme paramTheme, AttributeSet paramAttributeSet, int paramInt1, int paramInt2) {
    TypedArray typedArray = paramTheme.obtainStyledAttributes(paramAttributeSet, R.styleable.MaterialTextView, paramInt1, paramInt2);
    paramInt1 = typedArray.getResourceId(R.styleable.MaterialTextView_android_textAppearance, -1);
    typedArray.recycle();
    return paramInt1;
  }
  
  private static int readFirstAvailableDimension(Context paramContext, TypedArray paramTypedArray, int... paramVarArgs) {
    int i = -1;
    for (byte b = 0; b < paramVarArgs.length && i < 0; b++)
      i = MaterialResources.getDimensionPixelSize(paramContext, paramTypedArray, paramVarArgs[b], -1); 
    return i;
  }
  
  private static boolean viewAttrsHasLineHeight(Context paramContext, Resources.Theme paramTheme, AttributeSet paramAttributeSet, int paramInt1, int paramInt2) {
    TypedArray typedArray = paramTheme.obtainStyledAttributes(paramAttributeSet, R.styleable.MaterialTextView, paramInt1, paramInt2);
    paramInt1 = R.styleable.MaterialTextView_android_lineHeight;
    boolean bool = false;
    paramInt1 = readFirstAvailableDimension(paramContext, typedArray, new int[] { paramInt1, R.styleable.MaterialTextView_lineHeight });
    typedArray.recycle();
    if (paramInt1 != -1)
      bool = true; 
    return bool;
  }
  
  public void setTextAppearance(Context paramContext, int paramInt) {
    super.setTextAppearance(paramContext, paramInt);
    if (canApplyTextAppearanceLineHeight(paramContext))
      applyLineHeightFromViewAppearance(paramContext.getTheme(), paramInt); 
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\textview\MaterialTextView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */