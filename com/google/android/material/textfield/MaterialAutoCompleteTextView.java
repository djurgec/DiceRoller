package com.google.android.material.textfield;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityManager;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.widget.ListPopupWindow;
import com.google.android.material.R;
import com.google.android.material.internal.ManufacturerUtils;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.theme.overlay.MaterialThemeOverlay;

public class MaterialAutoCompleteTextView extends AppCompatAutoCompleteTextView {
  private static final int MAX_ITEMS_MEASURED = 15;
  
  private final AccessibilityManager accessibilityManager;
  
  private final ListPopupWindow modalListPopup;
  
  private final Rect tempRect = new Rect();
  
  public MaterialAutoCompleteTextView(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public MaterialAutoCompleteTextView(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, R.attr.autoCompleteTextViewStyle);
  }
  
  public MaterialAutoCompleteTextView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(MaterialThemeOverlay.wrap(paramContext, paramAttributeSet, paramInt, 0), paramAttributeSet, paramInt);
    paramContext = getContext();
    TypedArray typedArray = ThemeEnforcement.obtainStyledAttributes(paramContext, paramAttributeSet, R.styleable.MaterialAutoCompleteTextView, paramInt, R.style.Widget_AppCompat_AutoCompleteTextView, new int[0]);
    if (typedArray.hasValue(R.styleable.MaterialAutoCompleteTextView_android_inputType) && typedArray.getInt(R.styleable.MaterialAutoCompleteTextView_android_inputType, 0) == 0)
      setKeyListener(null); 
    this.accessibilityManager = (AccessibilityManager)paramContext.getSystemService("accessibility");
    ListPopupWindow listPopupWindow = new ListPopupWindow(paramContext);
    this.modalListPopup = listPopupWindow;
    listPopupWindow.setModal(true);
    listPopupWindow.setAnchorView((View)this);
    listPopupWindow.setInputMethodMode(2);
    listPopupWindow.setAdapter(getAdapter());
    listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
          final MaterialAutoCompleteTextView this$0;
          
          public void onItemClick(AdapterView<?> param1AdapterView, View param1View, int param1Int, long param1Long) {
            // Byte code:
            //   0: iload_3
            //   1: ifge -> 18
            //   4: aload_0
            //   5: getfield this$0 : Lcom/google/android/material/textfield/MaterialAutoCompleteTextView;
            //   8: invokestatic access$000 : (Lcom/google/android/material/textfield/MaterialAutoCompleteTextView;)Landroidx/appcompat/widget/ListPopupWindow;
            //   11: invokevirtual getSelectedItem : ()Ljava/lang/Object;
            //   14: astore_1
            //   15: goto -> 32
            //   18: aload_0
            //   19: getfield this$0 : Lcom/google/android/material/textfield/MaterialAutoCompleteTextView;
            //   22: invokevirtual getAdapter : ()Landroid/widget/ListAdapter;
            //   25: iload_3
            //   26: invokeinterface getItem : (I)Ljava/lang/Object;
            //   31: astore_1
            //   32: aload_0
            //   33: getfield this$0 : Lcom/google/android/material/textfield/MaterialAutoCompleteTextView;
            //   36: aload_1
            //   37: invokestatic access$100 : (Lcom/google/android/material/textfield/MaterialAutoCompleteTextView;Ljava/lang/Object;)V
            //   40: aload_0
            //   41: getfield this$0 : Lcom/google/android/material/textfield/MaterialAutoCompleteTextView;
            //   44: invokevirtual getOnItemClickListener : ()Landroid/widget/AdapterView$OnItemClickListener;
            //   47: astore_1
            //   48: aload_1
            //   49: ifnull -> 119
            //   52: aload_2
            //   53: ifnull -> 63
            //   56: iload_3
            //   57: istore #6
            //   59: iload_3
            //   60: ifge -> 98
            //   63: aload_0
            //   64: getfield this$0 : Lcom/google/android/material/textfield/MaterialAutoCompleteTextView;
            //   67: invokestatic access$000 : (Lcom/google/android/material/textfield/MaterialAutoCompleteTextView;)Landroidx/appcompat/widget/ListPopupWindow;
            //   70: invokevirtual getSelectedView : ()Landroid/view/View;
            //   73: astore_2
            //   74: aload_0
            //   75: getfield this$0 : Lcom/google/android/material/textfield/MaterialAutoCompleteTextView;
            //   78: invokestatic access$000 : (Lcom/google/android/material/textfield/MaterialAutoCompleteTextView;)Landroidx/appcompat/widget/ListPopupWindow;
            //   81: invokevirtual getSelectedItemPosition : ()I
            //   84: istore #6
            //   86: aload_0
            //   87: getfield this$0 : Lcom/google/android/material/textfield/MaterialAutoCompleteTextView;
            //   90: invokestatic access$000 : (Lcom/google/android/material/textfield/MaterialAutoCompleteTextView;)Landroidx/appcompat/widget/ListPopupWindow;
            //   93: invokevirtual getSelectedItemId : ()J
            //   96: lstore #4
            //   98: aload_1
            //   99: aload_0
            //   100: getfield this$0 : Lcom/google/android/material/textfield/MaterialAutoCompleteTextView;
            //   103: invokestatic access$000 : (Lcom/google/android/material/textfield/MaterialAutoCompleteTextView;)Landroidx/appcompat/widget/ListPopupWindow;
            //   106: invokevirtual getListView : ()Landroid/widget/ListView;
            //   109: aload_2
            //   110: iload #6
            //   112: lload #4
            //   114: invokeinterface onItemClick : (Landroid/widget/AdapterView;Landroid/view/View;IJ)V
            //   119: aload_0
            //   120: getfield this$0 : Lcom/google/android/material/textfield/MaterialAutoCompleteTextView;
            //   123: invokestatic access$000 : (Lcom/google/android/material/textfield/MaterialAutoCompleteTextView;)Landroidx/appcompat/widget/ListPopupWindow;
            //   126: invokevirtual dismiss : ()V
            //   129: return
          }
        });
    typedArray.recycle();
  }
  
  private TextInputLayout findTextInputLayoutAncestor() {
    for (ViewParent viewParent = getParent(); viewParent != null; viewParent = viewParent.getParent()) {
      if (viewParent instanceof TextInputLayout)
        return (TextInputLayout)viewParent; 
    } 
    return null;
  }
  
  private int measureContentWidth() {
    ListAdapter listAdapter = getAdapter();
    TextInputLayout textInputLayout = findTextInputLayoutAncestor();
    if (listAdapter == null || textInputLayout == null)
      return 0; 
    int i = 0;
    View view = null;
    int k = 0;
    int m = View.MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 0);
    int n = View.MeasureSpec.makeMeasureSpec(getMeasuredHeight(), 0);
    int j = Math.max(0, this.modalListPopup.getSelectedItemPosition());
    int i1 = Math.min(listAdapter.getCount(), j + 15);
    j = Math.max(0, i1 - 15);
    while (j < i1) {
      int i3 = listAdapter.getItemViewType(j);
      int i2 = k;
      if (i3 != k) {
        i2 = i3;
        view = null;
      } 
      view = listAdapter.getView(j, view, (ViewGroup)textInputLayout);
      if (view.getLayoutParams() == null)
        view.setLayoutParams(new ViewGroup.LayoutParams(-2, -2)); 
      view.measure(m, n);
      i = Math.max(i, view.getMeasuredWidth());
      j++;
      k = i2;
    } 
    Drawable drawable = this.modalListPopup.getBackground();
    j = i;
    if (drawable != null) {
      drawable.getPadding(this.tempRect);
      j = i + this.tempRect.left + this.tempRect.right;
    } 
    return j + textInputLayout.getEndIconView().getMeasuredWidth();
  }
  
  private <T extends ListAdapter & android.widget.Filterable> void updateText(Object paramObject) {
    if (Build.VERSION.SDK_INT >= 17) {
      setText(convertSelectionToString(paramObject), false);
    } else {
      ListAdapter listAdapter = getAdapter();
      setAdapter((ListAdapter)null);
      setText(convertSelectionToString(paramObject));
      setAdapter(listAdapter);
    } 
  }
  
  public CharSequence getHint() {
    TextInputLayout textInputLayout = findTextInputLayoutAncestor();
    return (textInputLayout != null && textInputLayout.isProvidingHint()) ? textInputLayout.getHint() : super.getHint();
  }
  
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    TextInputLayout textInputLayout = findTextInputLayoutAncestor();
    if (textInputLayout != null && textInputLayout.isProvidingHint() && super.getHint() == null && ManufacturerUtils.isMeizuDevice())
      setHint(""); 
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    super.onMeasure(paramInt1, paramInt2);
    if (View.MeasureSpec.getMode(paramInt1) == Integer.MIN_VALUE) {
      paramInt2 = getMeasuredWidth();
      setMeasuredDimension(Math.min(Math.max(paramInt2, measureContentWidth()), View.MeasureSpec.getSize(paramInt1)), getMeasuredHeight());
    } 
  }
  
  public <T extends ListAdapter & android.widget.Filterable> void setAdapter(T paramT) {
    super.setAdapter((ListAdapter)paramT);
    this.modalListPopup.setAdapter(getAdapter());
  }
  
  public void showDropDown() {
    AccessibilityManager accessibilityManager = this.accessibilityManager;
    if (accessibilityManager != null && accessibilityManager.isTouchExplorationEnabled()) {
      this.modalListPopup.show();
    } else {
      super.showDropDown();
    } 
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\textfield\MaterialAutoCompleteTextView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */