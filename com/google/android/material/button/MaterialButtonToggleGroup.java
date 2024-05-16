package com.google.android.material.button;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.LinearLayout;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.MarginLayoutParamsCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import com.google.android.material.R;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.internal.ViewUtils;
import com.google.android.material.shape.AbsoluteCornerSize;
import com.google.android.material.shape.CornerSize;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.android.material.theme.overlay.MaterialThemeOverlay;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.TreeMap;

public class MaterialButtonToggleGroup extends LinearLayout {
  private static final int DEF_STYLE_RES;
  
  private static final String LOG_TAG = MaterialButtonToggleGroup.class.getSimpleName();
  
  private int checkedId;
  
  private final CheckedStateTracker checkedStateTracker = new CheckedStateTracker();
  
  private Integer[] childOrder;
  
  private final Comparator<MaterialButton> childOrderComparator = new Comparator<MaterialButton>() {
      final MaterialButtonToggleGroup this$0;
      
      public int compare(MaterialButton param1MaterialButton1, MaterialButton param1MaterialButton2) {
        int i = Boolean.valueOf(param1MaterialButton1.isChecked()).compareTo(Boolean.valueOf(param1MaterialButton2.isChecked()));
        if (i != 0)
          return i; 
        i = Boolean.valueOf(param1MaterialButton1.isPressed()).compareTo(Boolean.valueOf(param1MaterialButton2.isPressed()));
        return (i != 0) ? i : Integer.valueOf(MaterialButtonToggleGroup.this.indexOfChild((View)param1MaterialButton1)).compareTo(Integer.valueOf(MaterialButtonToggleGroup.this.indexOfChild((View)param1MaterialButton2)));
      }
    };
  
  private final LinkedHashSet<OnButtonCheckedListener> onButtonCheckedListeners = new LinkedHashSet<>();
  
  private final List<CornerData> originalCornerData = new ArrayList<>();
  
  private final PressedStateTracker pressedStateTracker = new PressedStateTracker();
  
  private boolean selectionRequired;
  
  private boolean singleSelection;
  
  private boolean skipCheckedStateTracker = false;
  
  static {
    DEF_STYLE_RES = R.style.Widget_MaterialComponents_MaterialButtonToggleGroup;
  }
  
  public MaterialButtonToggleGroup(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public MaterialButtonToggleGroup(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, R.attr.materialButtonToggleGroupStyle);
  }
  
  public MaterialButtonToggleGroup(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(MaterialThemeOverlay.wrap(paramContext, paramAttributeSet, paramInt, i), paramAttributeSet, paramInt);
    TypedArray typedArray = ThemeEnforcement.obtainStyledAttributes(getContext(), paramAttributeSet, R.styleable.MaterialButtonToggleGroup, paramInt, i, new int[0]);
    setSingleSelection(typedArray.getBoolean(R.styleable.MaterialButtonToggleGroup_singleSelection, false));
    this.checkedId = typedArray.getResourceId(R.styleable.MaterialButtonToggleGroup_checkedButton, -1);
    this.selectionRequired = typedArray.getBoolean(R.styleable.MaterialButtonToggleGroup_selectionRequired, false);
    setChildrenDrawingOrderEnabled(true);
    typedArray.recycle();
    ViewCompat.setImportantForAccessibility((View)this, 1);
  }
  
  private void adjustChildMarginsAndUpdateLayout() {
    int j = getFirstVisibleChildIndex();
    if (j == -1)
      return; 
    for (int i = j + 1; i < getChildCount(); i++) {
      MaterialButton materialButton1 = getChildButton(i);
      MaterialButton materialButton2 = getChildButton(i - 1);
      int k = Math.min(materialButton1.getStrokeWidth(), materialButton2.getStrokeWidth());
      LinearLayout.LayoutParams layoutParams = buildLayoutParams((View)materialButton1);
      if (getOrientation() == 0) {
        MarginLayoutParamsCompat.setMarginEnd((ViewGroup.MarginLayoutParams)layoutParams, 0);
        MarginLayoutParamsCompat.setMarginStart((ViewGroup.MarginLayoutParams)layoutParams, -k);
        layoutParams.topMargin = 0;
      } else {
        layoutParams.bottomMargin = 0;
        layoutParams.topMargin = -k;
        MarginLayoutParamsCompat.setMarginStart((ViewGroup.MarginLayoutParams)layoutParams, 0);
      } 
      materialButton1.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
    } 
    resetChildMargins(j);
  }
  
  private LinearLayout.LayoutParams buildLayoutParams(View paramView) {
    ViewGroup.LayoutParams layoutParams = paramView.getLayoutParams();
    return (layoutParams instanceof LinearLayout.LayoutParams) ? (LinearLayout.LayoutParams)layoutParams : new LinearLayout.LayoutParams(layoutParams.width, layoutParams.height);
  }
  
  private void checkForced(int paramInt, boolean paramBoolean) {
    MaterialButton materialButton = (MaterialButton)findViewById(paramInt);
    if (materialButton != null)
      materialButton.setChecked(paramBoolean); 
  }
  
  private void dispatchOnButtonChecked(int paramInt, boolean paramBoolean) {
    Iterator<OnButtonCheckedListener> iterator = this.onButtonCheckedListeners.iterator();
    while (iterator.hasNext())
      ((OnButtonCheckedListener)iterator.next()).onButtonChecked(this, paramInt, paramBoolean); 
  }
  
  private MaterialButton getChildButton(int paramInt) {
    return (MaterialButton)getChildAt(paramInt);
  }
  
  private int getFirstVisibleChildIndex() {
    int i = getChildCount();
    for (byte b = 0; b < i; b++) {
      if (isChildVisible(b))
        return b; 
    } 
    return -1;
  }
  
  private int getIndexWithinVisibleButtons(View paramView) {
    if (!(paramView instanceof MaterialButton))
      return -1; 
    int i = 0;
    byte b = 0;
    while (b < getChildCount()) {
      if (getChildAt(b) == paramView)
        return i; 
      int j = i;
      if (getChildAt(b) instanceof MaterialButton) {
        j = i;
        if (isChildVisible(b))
          j = i + 1; 
      } 
      b++;
      i = j;
    } 
    return -1;
  }
  
  private int getLastVisibleChildIndex() {
    for (int i = getChildCount() - 1; i >= 0; i--) {
      if (isChildVisible(i))
        return i; 
    } 
    return -1;
  }
  
  private CornerData getNewCornerData(int paramInt1, int paramInt2, int paramInt3) {
    boolean bool;
    CornerData cornerData = this.originalCornerData.get(paramInt1);
    if (paramInt2 == paramInt3)
      return cornerData; 
    if (getOrientation() == 0) {
      bool = true;
    } else {
      bool = false;
    } 
    if (paramInt1 == paramInt2) {
      if (bool) {
        cornerData = CornerData.start(cornerData, (View)this);
      } else {
        cornerData = CornerData.top(cornerData);
      } 
      return cornerData;
    } 
    if (paramInt1 == paramInt3) {
      if (bool) {
        cornerData = CornerData.end(cornerData, (View)this);
      } else {
        cornerData = CornerData.bottom(cornerData);
      } 
      return cornerData;
    } 
    return null;
  }
  
  private int getVisibleButtonCount() {
    int i = 0;
    byte b = 0;
    while (b < getChildCount()) {
      int j = i;
      if (getChildAt(b) instanceof MaterialButton) {
        j = i;
        if (isChildVisible(b))
          j = i + 1; 
      } 
      b++;
      i = j;
    } 
    return i;
  }
  
  private boolean isChildVisible(int paramInt) {
    boolean bool;
    if (getChildAt(paramInt).getVisibility() != 8) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private void resetChildMargins(int paramInt) {
    if (getChildCount() == 0 || paramInt == -1)
      return; 
    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)getChildButton(paramInt).getLayoutParams();
    if (getOrientation() == 1) {
      layoutParams.topMargin = 0;
      layoutParams.bottomMargin = 0;
      return;
    } 
    MarginLayoutParamsCompat.setMarginEnd((ViewGroup.MarginLayoutParams)layoutParams, 0);
    MarginLayoutParamsCompat.setMarginStart((ViewGroup.MarginLayoutParams)layoutParams, 0);
    layoutParams.leftMargin = 0;
    layoutParams.rightMargin = 0;
  }
  
  private void setCheckedId(int paramInt) {
    this.checkedId = paramInt;
    dispatchOnButtonChecked(paramInt, true);
  }
  
  private void setCheckedStateForView(int paramInt, boolean paramBoolean) {
    View view = findViewById(paramInt);
    if (view instanceof MaterialButton) {
      this.skipCheckedStateTracker = true;
      ((MaterialButton)view).setChecked(paramBoolean);
      this.skipCheckedStateTracker = false;
    } 
  }
  
  private void setGeneratedIdIfNeeded(MaterialButton paramMaterialButton) {
    if (paramMaterialButton.getId() == -1)
      paramMaterialButton.setId(ViewCompat.generateViewId()); 
  }
  
  private void setupButtonChild(MaterialButton paramMaterialButton) {
    paramMaterialButton.setMaxLines(1);
    paramMaterialButton.setEllipsize(TextUtils.TruncateAt.END);
    paramMaterialButton.setCheckable(true);
    paramMaterialButton.addOnCheckedChangeListener(this.checkedStateTracker);
    paramMaterialButton.setOnPressedChangeListenerInternal(this.pressedStateTracker);
    paramMaterialButton.setShouldDrawSurfaceColorStroke(true);
  }
  
  private static void updateBuilderWithCornerData(ShapeAppearanceModel.Builder paramBuilder, CornerData paramCornerData) {
    if (paramCornerData == null) {
      paramBuilder.setAllCornerSizes(0.0F);
      return;
    } 
    paramBuilder.setTopLeftCornerSize(paramCornerData.topLeft).setBottomLeftCornerSize(paramCornerData.bottomLeft).setTopRightCornerSize(paramCornerData.topRight).setBottomRightCornerSize(paramCornerData.bottomRight);
  }
  
  private boolean updateCheckedStates(int paramInt, boolean paramBoolean) {
    List<Integer> list = getCheckedButtonIds();
    if (this.selectionRequired && list.isEmpty()) {
      setCheckedStateForView(paramInt, true);
      this.checkedId = paramInt;
      return false;
    } 
    if (paramBoolean && this.singleSelection) {
      list.remove(Integer.valueOf(paramInt));
      Iterator<Integer> iterator = list.iterator();
      while (iterator.hasNext()) {
        paramInt = ((Integer)iterator.next()).intValue();
        setCheckedStateForView(paramInt, false);
        dispatchOnButtonChecked(paramInt, false);
      } 
    } 
    return true;
  }
  
  private void updateChildOrder() {
    TreeMap<MaterialButton, Object> treeMap = new TreeMap<>(this.childOrderComparator);
    int i = getChildCount();
    for (byte b = 0; b < i; b++)
      treeMap.put(getChildButton(b), Integer.valueOf(b)); 
    this.childOrder = (Integer[])treeMap.values().toArray((Object[])new Integer[0]);
  }
  
  public void addOnButtonCheckedListener(OnButtonCheckedListener paramOnButtonCheckedListener) {
    this.onButtonCheckedListeners.add(paramOnButtonCheckedListener);
  }
  
  public void addView(View paramView, int paramInt, ViewGroup.LayoutParams paramLayoutParams) {
    if (!(paramView instanceof MaterialButton)) {
      Log.e(LOG_TAG, "Child views must be of type MaterialButton.");
      return;
    } 
    super.addView(paramView, paramInt, paramLayoutParams);
    MaterialButton materialButton = (MaterialButton)paramView;
    setGeneratedIdIfNeeded(materialButton);
    setupButtonChild(materialButton);
    if (materialButton.isChecked()) {
      updateCheckedStates(materialButton.getId(), true);
      setCheckedId(materialButton.getId());
    } 
    ShapeAppearanceModel shapeAppearanceModel = materialButton.getShapeAppearanceModel();
    this.originalCornerData.add(new CornerData(shapeAppearanceModel.getTopLeftCornerSize(), shapeAppearanceModel.getBottomLeftCornerSize(), shapeAppearanceModel.getTopRightCornerSize(), shapeAppearanceModel.getBottomRightCornerSize()));
    ViewCompat.setAccessibilityDelegate((View)materialButton, new AccessibilityDelegateCompat() {
          final MaterialButtonToggleGroup this$0;
          
          public void onInitializeAccessibilityNodeInfo(View param1View, AccessibilityNodeInfoCompat param1AccessibilityNodeInfoCompat) {
            super.onInitializeAccessibilityNodeInfo(param1View, param1AccessibilityNodeInfoCompat);
            param1AccessibilityNodeInfoCompat.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(0, 1, MaterialButtonToggleGroup.this.getIndexWithinVisibleButtons(param1View), 1, false, ((MaterialButton)param1View).isChecked()));
          }
        });
  }
  
  public void check(int paramInt) {
    if (paramInt == this.checkedId)
      return; 
    checkForced(paramInt, true);
  }
  
  public void clearChecked() {
    this.skipCheckedStateTracker = true;
    for (byte b = 0; b < getChildCount(); b++) {
      MaterialButton materialButton = getChildButton(b);
      materialButton.setChecked(false);
      dispatchOnButtonChecked(materialButton.getId(), false);
    } 
    this.skipCheckedStateTracker = false;
    setCheckedId(-1);
  }
  
  public void clearOnButtonCheckedListeners() {
    this.onButtonCheckedListeners.clear();
  }
  
  protected void dispatchDraw(Canvas paramCanvas) {
    updateChildOrder();
    super.dispatchDraw(paramCanvas);
  }
  
  public CharSequence getAccessibilityClassName() {
    return MaterialButtonToggleGroup.class.getName();
  }
  
  public int getCheckedButtonId() {
    byte b;
    if (this.singleSelection) {
      b = this.checkedId;
    } else {
      b = -1;
    } 
    return b;
  }
  
  public List<Integer> getCheckedButtonIds() {
    ArrayList<Integer> arrayList = new ArrayList();
    for (byte b = 0; b < getChildCount(); b++) {
      MaterialButton materialButton = getChildButton(b);
      if (materialButton.isChecked())
        arrayList.add(Integer.valueOf(materialButton.getId())); 
    } 
    return arrayList;
  }
  
  protected int getChildDrawingOrder(int paramInt1, int paramInt2) {
    Integer[] arrayOfInteger = this.childOrder;
    if (arrayOfInteger == null || paramInt2 >= arrayOfInteger.length) {
      Log.w(LOG_TAG, "Child order wasn't updated");
      return paramInt2;
    } 
    return arrayOfInteger[paramInt2].intValue();
  }
  
  public boolean isSelectionRequired() {
    return this.selectionRequired;
  }
  
  public boolean isSingleSelection() {
    return this.singleSelection;
  }
  
  protected void onFinishInflate() {
    super.onFinishInflate();
    int i = this.checkedId;
    if (i != -1)
      checkForced(i, true); 
  }
  
  public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo paramAccessibilityNodeInfo) {
    byte b;
    super.onInitializeAccessibilityNodeInfo(paramAccessibilityNodeInfo);
    AccessibilityNodeInfoCompat accessibilityNodeInfoCompat = AccessibilityNodeInfoCompat.wrap(paramAccessibilityNodeInfo);
    int i = getVisibleButtonCount();
    if (isSingleSelection()) {
      b = 1;
    } else {
      b = 2;
    } 
    accessibilityNodeInfoCompat.setCollectionInfo(AccessibilityNodeInfoCompat.CollectionInfoCompat.obtain(1, i, false, b));
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    updateChildShapes();
    adjustChildMarginsAndUpdateLayout();
    super.onMeasure(paramInt1, paramInt2);
  }
  
  public void onViewRemoved(View paramView) {
    super.onViewRemoved(paramView);
    if (paramView instanceof MaterialButton) {
      ((MaterialButton)paramView).removeOnCheckedChangeListener(this.checkedStateTracker);
      ((MaterialButton)paramView).setOnPressedChangeListenerInternal((MaterialButton.OnPressedChangeListener)null);
    } 
    int i = indexOfChild(paramView);
    if (i >= 0)
      this.originalCornerData.remove(i); 
    updateChildShapes();
    adjustChildMarginsAndUpdateLayout();
  }
  
  public void removeOnButtonCheckedListener(OnButtonCheckedListener paramOnButtonCheckedListener) {
    this.onButtonCheckedListeners.remove(paramOnButtonCheckedListener);
  }
  
  public void setSelectionRequired(boolean paramBoolean) {
    this.selectionRequired = paramBoolean;
  }
  
  public void setSingleSelection(int paramInt) {
    setSingleSelection(getResources().getBoolean(paramInt));
  }
  
  public void setSingleSelection(boolean paramBoolean) {
    if (this.singleSelection != paramBoolean) {
      this.singleSelection = paramBoolean;
      clearChecked();
    } 
  }
  
  public void uncheck(int paramInt) {
    checkForced(paramInt, false);
  }
  
  void updateChildShapes() {
    int j = getChildCount();
    int k = getFirstVisibleChildIndex();
    int i = getLastVisibleChildIndex();
    for (byte b = 0; b < j; b++) {
      MaterialButton materialButton = getChildButton(b);
      if (materialButton.getVisibility() != 8) {
        ShapeAppearanceModel.Builder builder = materialButton.getShapeAppearanceModel().toBuilder();
        updateBuilderWithCornerData(builder, getNewCornerData(b, k, i));
        materialButton.setShapeAppearanceModel(builder.build());
      } 
    } 
  }
  
  private class CheckedStateTracker implements MaterialButton.OnCheckedChangeListener {
    final MaterialButtonToggleGroup this$0;
    
    private CheckedStateTracker() {}
    
    public void onCheckedChanged(MaterialButton param1MaterialButton, boolean param1Boolean) {
      if (MaterialButtonToggleGroup.this.skipCheckedStateTracker)
        return; 
      if (MaterialButtonToggleGroup.this.singleSelection) {
        byte b;
        MaterialButtonToggleGroup materialButtonToggleGroup = MaterialButtonToggleGroup.this;
        if (param1Boolean) {
          b = param1MaterialButton.getId();
        } else {
          b = -1;
        } 
        MaterialButtonToggleGroup.access$502(materialButtonToggleGroup, b);
      } 
      if (MaterialButtonToggleGroup.this.updateCheckedStates(param1MaterialButton.getId(), param1Boolean))
        MaterialButtonToggleGroup.this.dispatchOnButtonChecked(param1MaterialButton.getId(), param1MaterialButton.isChecked()); 
      MaterialButtonToggleGroup.this.invalidate();
    }
  }
  
  private static class CornerData {
    private static final CornerSize noCorner = (CornerSize)new AbsoluteCornerSize(0.0F);
    
    CornerSize bottomLeft;
    
    CornerSize bottomRight;
    
    CornerSize topLeft;
    
    CornerSize topRight;
    
    CornerData(CornerSize param1CornerSize1, CornerSize param1CornerSize2, CornerSize param1CornerSize3, CornerSize param1CornerSize4) {
      this.topLeft = param1CornerSize1;
      this.topRight = param1CornerSize3;
      this.bottomRight = param1CornerSize4;
      this.bottomLeft = param1CornerSize2;
    }
    
    public static CornerData bottom(CornerData param1CornerData) {
      CornerSize cornerSize = noCorner;
      return new CornerData(cornerSize, param1CornerData.bottomLeft, cornerSize, param1CornerData.bottomRight);
    }
    
    public static CornerData end(CornerData param1CornerData, View param1View) {
      if (ViewUtils.isLayoutRtl(param1View)) {
        param1CornerData = left(param1CornerData);
      } else {
        param1CornerData = right(param1CornerData);
      } 
      return param1CornerData;
    }
    
    public static CornerData left(CornerData param1CornerData) {
      CornerSize cornerSize2 = param1CornerData.topLeft;
      CornerSize cornerSize3 = param1CornerData.bottomLeft;
      CornerSize cornerSize1 = noCorner;
      return new CornerData(cornerSize2, cornerSize3, cornerSize1, cornerSize1);
    }
    
    public static CornerData right(CornerData param1CornerData) {
      CornerSize cornerSize = noCorner;
      return new CornerData(cornerSize, cornerSize, param1CornerData.topRight, param1CornerData.bottomRight);
    }
    
    public static CornerData start(CornerData param1CornerData, View param1View) {
      if (ViewUtils.isLayoutRtl(param1View)) {
        param1CornerData = right(param1CornerData);
      } else {
        param1CornerData = left(param1CornerData);
      } 
      return param1CornerData;
    }
    
    public static CornerData top(CornerData param1CornerData) {
      CornerSize cornerSize2 = param1CornerData.topLeft;
      CornerSize cornerSize1 = noCorner;
      return new CornerData(cornerSize2, cornerSize1, param1CornerData.topRight, cornerSize1);
    }
  }
  
  public static interface OnButtonCheckedListener {
    void onButtonChecked(MaterialButtonToggleGroup param1MaterialButtonToggleGroup, int param1Int, boolean param1Boolean);
  }
  
  private class PressedStateTracker implements MaterialButton.OnPressedChangeListener {
    final MaterialButtonToggleGroup this$0;
    
    private PressedStateTracker() {}
    
    public void onPressedChanged(MaterialButton param1MaterialButton, boolean param1Boolean) {
      MaterialButtonToggleGroup.this.invalidate();
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\button\MaterialButtonToggleGroup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */