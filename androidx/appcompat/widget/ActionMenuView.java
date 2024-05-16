package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewDebug.ExportedProperty;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.appcompat.view.menu.MenuPresenter;
import androidx.appcompat.view.menu.MenuView;

public class ActionMenuView extends LinearLayoutCompat implements MenuBuilder.ItemInvoker, MenuView {
  static final int GENERATED_ITEM_PADDING = 4;
  
  static final int MIN_CELL_SIZE = 56;
  
  private static final String TAG = "ActionMenuView";
  
  private MenuPresenter.Callback mActionMenuPresenterCallback;
  
  private boolean mFormatItems;
  
  private int mFormatItemsWidth;
  
  private int mGeneratedItemPadding;
  
  private MenuBuilder mMenu;
  
  MenuBuilder.Callback mMenuBuilderCallback;
  
  private int mMinCellSize;
  
  OnMenuItemClickListener mOnMenuItemClickListener;
  
  private Context mPopupContext;
  
  private int mPopupTheme;
  
  private ActionMenuPresenter mPresenter;
  
  private boolean mReserveOverflow;
  
  public ActionMenuView(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public ActionMenuView(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    setBaselineAligned(false);
    float f = (paramContext.getResources().getDisplayMetrics()).density;
    this.mMinCellSize = (int)(56.0F * f);
    this.mGeneratedItemPadding = (int)(4.0F * f);
    this.mPopupContext = paramContext;
    this.mPopupTheme = 0;
  }
  
  static int measureChildForCells(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   4: checkcast androidx/appcompat/widget/ActionMenuView$LayoutParams
    //   7: astore #10
    //   9: iload_3
    //   10: invokestatic getSize : (I)I
    //   13: iload #4
    //   15: isub
    //   16: iload_3
    //   17: invokestatic getMode : (I)I
    //   20: invokestatic makeMeasureSpec : (II)I
    //   23: istore #6
    //   25: aload_0
    //   26: instanceof androidx/appcompat/view/menu/ActionMenuItemView
    //   29: ifeq -> 41
    //   32: aload_0
    //   33: checkcast androidx/appcompat/view/menu/ActionMenuItemView
    //   36: astore #9
    //   38: goto -> 44
    //   41: aconst_null
    //   42: astore #9
    //   44: iconst_0
    //   45: istore #8
    //   47: aload #9
    //   49: ifnull -> 66
    //   52: aload #9
    //   54: invokevirtual hasText : ()Z
    //   57: ifeq -> 66
    //   60: iconst_1
    //   61: istore #4
    //   63: goto -> 69
    //   66: iconst_0
    //   67: istore #4
    //   69: iconst_0
    //   70: istore #5
    //   72: iload #5
    //   74: istore_3
    //   75: iload_2
    //   76: ifle -> 146
    //   79: iload #4
    //   81: ifeq -> 92
    //   84: iload #5
    //   86: istore_3
    //   87: iload_2
    //   88: iconst_2
    //   89: if_icmplt -> 146
    //   92: aload_0
    //   93: iload_1
    //   94: iload_2
    //   95: imul
    //   96: ldc -2147483648
    //   98: invokestatic makeMeasureSpec : (II)I
    //   101: iload #6
    //   103: invokevirtual measure : (II)V
    //   106: aload_0
    //   107: invokevirtual getMeasuredWidth : ()I
    //   110: istore #5
    //   112: iload #5
    //   114: iload_1
    //   115: idiv
    //   116: istore_3
    //   117: iload_3
    //   118: istore_2
    //   119: iload #5
    //   121: iload_1
    //   122: irem
    //   123: ifeq -> 130
    //   126: iload_3
    //   127: iconst_1
    //   128: iadd
    //   129: istore_2
    //   130: iload_2
    //   131: istore_3
    //   132: iload #4
    //   134: ifeq -> 146
    //   137: iload_2
    //   138: istore_3
    //   139: iload_2
    //   140: iconst_2
    //   141: if_icmpge -> 146
    //   144: iconst_2
    //   145: istore_3
    //   146: iload #8
    //   148: istore #7
    //   150: aload #10
    //   152: getfield isOverflowButton : Z
    //   155: ifne -> 170
    //   158: iload #8
    //   160: istore #7
    //   162: iload #4
    //   164: ifeq -> 170
    //   167: iconst_1
    //   168: istore #7
    //   170: aload #10
    //   172: iload #7
    //   174: putfield expandable : Z
    //   177: aload #10
    //   179: iload_3
    //   180: putfield cellsUsed : I
    //   183: aload_0
    //   184: iload_3
    //   185: iload_1
    //   186: imul
    //   187: ldc 1073741824
    //   189: invokestatic makeMeasureSpec : (II)I
    //   192: iload #6
    //   194: invokevirtual measure : (II)V
    //   197: iload_3
    //   198: ireturn
  }
  
  private void onMeasureExactFormat(int paramInt1, int paramInt2) {
    boolean bool;
    int i6 = View.MeasureSpec.getMode(paramInt2);
    int j = View.MeasureSpec.getSize(paramInt1);
    int i8 = View.MeasureSpec.getSize(paramInt2);
    paramInt1 = getPaddingLeft();
    int i = getPaddingRight();
    int i4 = getPaddingTop() + getPaddingBottom();
    int i9 = getChildMeasureSpec(paramInt2, i4, -2);
    int i7 = j - paramInt1 + i;
    paramInt1 = this.mMinCellSize;
    int i2 = i7 / paramInt1;
    int n = i7 % paramInt1;
    if (i2 == 0) {
      setMeasuredDimension(i7, 0);
      return;
    } 
    int i11 = paramInt1 + n / i2;
    paramInt1 = i2;
    i = 0;
    int i1 = 0;
    paramInt2 = 0;
    int k = 0;
    long l = 0L;
    int i10 = getChildCount();
    j = 0;
    int i3 = 0;
    while (i3 < i10) {
      int i12;
      View view = getChildAt(i3);
      if (view.getVisibility() == 8) {
        i12 = j;
      } else {
        boolean bool1 = view instanceof ActionMenuItemView;
        i12 = j + 1;
        if (bool1) {
          j = this.mGeneratedItemPadding;
          view.setPadding(j, 0, j, 0);
        } 
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        layoutParams.expanded = false;
        layoutParams.extraPixels = 0;
        layoutParams.cellsUsed = 0;
        layoutParams.expandable = false;
        layoutParams.leftMargin = 0;
        layoutParams.rightMargin = 0;
        if (bool1 && ((ActionMenuItemView)view).hasText()) {
          bool1 = true;
        } else {
          bool1 = false;
        } 
        layoutParams.preventEdgeOffset = bool1;
        if (layoutParams.isOverflowButton) {
          j = 1;
        } else {
          j = paramInt1;
        } 
        bool = measureChildForCells(view, i11, j, i9, i4);
        i1 = Math.max(i1, bool);
        j = paramInt2;
        if (layoutParams.expandable)
          j = paramInt2 + 1; 
        if (layoutParams.isOverflowButton)
          k = 1; 
        paramInt1 -= bool;
        i = Math.max(i, view.getMeasuredHeight());
        if (bool == true) {
          long l1 = (1 << i3);
          l |= l1;
          paramInt2 = j;
        } else {
          paramInt2 = j;
        } 
      } 
      i3++;
      j = i12;
    } 
    if (k && j == 2) {
      bool = true;
    } else {
      bool = false;
    } 
    int m = 0;
    int i5 = paramInt1;
    paramInt1 = m;
    i2 = i7;
    i3 = i6;
    while (paramInt2 > 0 && i5 > 0) {
      i6 = Integer.MAX_VALUE;
      long l1 = 0L;
      m = 0;
      i7 = 0;
      while (i7 < i10) {
        int i12;
        long l2;
        LayoutParams layoutParams = (LayoutParams)getChildAt(i7).getLayoutParams();
        if (!layoutParams.expandable) {
          n = m;
          i12 = i6;
          l2 = l1;
        } else if (layoutParams.cellsUsed < i6) {
          i12 = layoutParams.cellsUsed;
          l2 = 1L << i7;
          n = 1;
        } else {
          n = m;
          i12 = i6;
          l2 = l1;
          if (layoutParams.cellsUsed == i6) {
            l2 = l1 | 1L << i7;
            n = m + 1;
            i12 = i6;
          } 
        } 
        i7++;
        m = n;
        i6 = i12;
        l1 = l2;
      } 
      l |= l1;
      if (m > i5)
        break; 
      paramInt1 = 0;
      while (paramInt1 < i10) {
        long l2;
        View view = getChildAt(paramInt1);
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if ((l1 & (1 << paramInt1)) == 0L) {
          n = i5;
          l2 = l;
          if (layoutParams.cellsUsed == i6 + 1) {
            l2 = l | (1 << paramInt1);
            n = i5;
          } 
        } else {
          if (bool && layoutParams.preventEdgeOffset && i5 == 1) {
            n = this.mGeneratedItemPadding;
            view.setPadding(n + i11, 0, n, 0);
          } 
          layoutParams.cellsUsed++;
          layoutParams.expanded = true;
          n = i5 - 1;
          l2 = l;
        } 
        paramInt1++;
        i5 = n;
        l = l2;
      } 
      paramInt1 = 1;
    } 
    if (!k && j == 1) {
      paramInt2 = 1;
    } else {
      paramInt2 = 0;
    } 
    if (i5 > 0 && l != 0L && (i5 < j - 1 || paramInt2 != 0 || i1 > 1)) {
      float f = Long.bitCount(l);
      if (paramInt2 == 0) {
        float f1;
        if ((l & 0x1L) != 0L) {
          f1 = f;
          if (!((LayoutParams)getChildAt(0).getLayoutParams()).preventEdgeOffset)
            f1 = f - 0.5F; 
        } else {
          f1 = f;
        } 
        f = f1;
        if ((l & (1 << i10 - 1)) != 0L) {
          f = f1;
          if (!((LayoutParams)getChildAt(i10 - 1).getLayoutParams()).preventEdgeOffset)
            f = f1 - 0.5F; 
        } 
      } 
      m = 0;
      if (f > 0.0F)
        m = (int)((i5 * i11) / f); 
      n = 0;
      for (k = paramInt1; n < i10; k = paramInt1) {
        if ((l & (1 << n)) == 0L) {
          paramInt1 = k;
        } else {
          View view = getChildAt(n);
          LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
          if (view instanceof ActionMenuItemView) {
            layoutParams.extraPixels = m;
            layoutParams.expanded = true;
            if (n == 0 && !layoutParams.preventEdgeOffset)
              layoutParams.leftMargin = -m / 2; 
            paramInt1 = 1;
          } else if (layoutParams.isOverflowButton) {
            layoutParams.extraPixels = m;
            layoutParams.expanded = true;
            layoutParams.rightMargin = -m / 2;
            paramInt1 = 1;
          } else {
            if (n != 0)
              layoutParams.leftMargin = m / 2; 
            paramInt1 = k;
            if (n != i10 - 1) {
              layoutParams.rightMargin = m / 2;
              paramInt1 = k;
            } 
          } 
        } 
        n++;
      } 
      paramInt1 = k;
    } 
    if (paramInt1 != 0)
      for (paramInt1 = 0; paramInt1 < i10; paramInt1++) {
        View view = getChildAt(paramInt1);
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (layoutParams.expanded)
          view.measure(View.MeasureSpec.makeMeasureSpec(layoutParams.cellsUsed * i11 + layoutParams.extraPixels, 1073741824), i9); 
      }  
    if (i3 != 1073741824) {
      paramInt1 = i;
    } else {
      paramInt1 = i8;
    } 
    setMeasuredDimension(i2, paramInt1);
  }
  
  protected boolean checkLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    return paramLayoutParams instanceof LayoutParams;
  }
  
  public void dismissPopupMenus() {
    ActionMenuPresenter actionMenuPresenter = this.mPresenter;
    if (actionMenuPresenter != null)
      actionMenuPresenter.dismissPopupMenus(); 
  }
  
  public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent) {
    return false;
  }
  
  protected LayoutParams generateDefaultLayoutParams() {
    LayoutParams layoutParams = new LayoutParams(-2, -2);
    layoutParams.gravity = 16;
    return layoutParams;
  }
  
  public LayoutParams generateLayoutParams(AttributeSet paramAttributeSet) {
    return new LayoutParams(getContext(), paramAttributeSet);
  }
  
  protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    if (paramLayoutParams != null) {
      LayoutParams layoutParams;
      if (paramLayoutParams instanceof LayoutParams) {
        layoutParams = new LayoutParams((LayoutParams)paramLayoutParams);
      } else {
        layoutParams = new LayoutParams((ViewGroup.LayoutParams)layoutParams);
      } 
      if (layoutParams.gravity <= 0)
        layoutParams.gravity = 16; 
      return layoutParams;
    } 
    return generateDefaultLayoutParams();
  }
  
  public LayoutParams generateOverflowButtonLayoutParams() {
    LayoutParams layoutParams = generateDefaultLayoutParams();
    layoutParams.isOverflowButton = true;
    return layoutParams;
  }
  
  public Menu getMenu() {
    if (this.mMenu == null) {
      Context context = getContext();
      MenuBuilder menuBuilder = new MenuBuilder(context);
      this.mMenu = menuBuilder;
      menuBuilder.setCallback(new MenuBuilderCallback());
      ActionMenuPresenter actionMenuPresenter1 = new ActionMenuPresenter(context);
      this.mPresenter = actionMenuPresenter1;
      actionMenuPresenter1.setReserveOverflow(true);
      ActionMenuPresenter actionMenuPresenter2 = this.mPresenter;
      MenuPresenter.Callback callback = this.mActionMenuPresenterCallback;
      if (callback == null)
        callback = new ActionMenuPresenterCallback(); 
      actionMenuPresenter2.setCallback(callback);
      this.mMenu.addMenuPresenter((MenuPresenter)this.mPresenter, this.mPopupContext);
      this.mPresenter.setMenuView(this);
    } 
    return (Menu)this.mMenu;
  }
  
  public Drawable getOverflowIcon() {
    getMenu();
    return this.mPresenter.getOverflowIcon();
  }
  
  public int getPopupTheme() {
    return this.mPopupTheme;
  }
  
  public int getWindowAnimations() {
    return 0;
  }
  
  protected boolean hasSupportDividerBeforeChildAt(int paramInt) {
    boolean bool;
    if (paramInt == 0)
      return false; 
    View view2 = getChildAt(paramInt - 1);
    View view1 = getChildAt(paramInt);
    int j = 0;
    int i = j;
    if (paramInt < getChildCount()) {
      i = j;
      if (view2 instanceof ActionMenuChildView)
        i = false | ((ActionMenuChildView)view2).needsDividerAfter(); 
    } 
    j = i;
    if (paramInt > 0) {
      j = i;
      if (view1 instanceof ActionMenuChildView)
        bool = i | ((ActionMenuChildView)view1).needsDividerBefore(); 
    } 
    return bool;
  }
  
  public boolean hideOverflowMenu() {
    boolean bool;
    ActionMenuPresenter actionMenuPresenter = this.mPresenter;
    if (actionMenuPresenter != null && actionMenuPresenter.hideOverflowMenu()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void initialize(MenuBuilder paramMenuBuilder) {
    this.mMenu = paramMenuBuilder;
  }
  
  public boolean invokeItem(MenuItemImpl paramMenuItemImpl) {
    return this.mMenu.performItemAction((MenuItem)paramMenuItemImpl, 0);
  }
  
  public boolean isOverflowMenuShowPending() {
    boolean bool;
    ActionMenuPresenter actionMenuPresenter = this.mPresenter;
    if (actionMenuPresenter != null && actionMenuPresenter.isOverflowMenuShowPending()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isOverflowMenuShowing() {
    boolean bool;
    ActionMenuPresenter actionMenuPresenter = this.mPresenter;
    if (actionMenuPresenter != null && actionMenuPresenter.isOverflowMenuShowing()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isOverflowReserved() {
    return this.mReserveOverflow;
  }
  
  public void onConfigurationChanged(Configuration paramConfiguration) {
    super.onConfigurationChanged(paramConfiguration);
    ActionMenuPresenter actionMenuPresenter = this.mPresenter;
    if (actionMenuPresenter != null) {
      actionMenuPresenter.updateMenuView(false);
      if (this.mPresenter.isOverflowMenuShowing()) {
        this.mPresenter.hideOverflowMenu();
        this.mPresenter.showOverflowMenu();
      } 
    } 
  }
  
  public void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    dismissPopupMenus();
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    if (!this.mFormatItems) {
      super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
      return;
    } 
    int i1 = getChildCount();
    int n = (paramInt4 - paramInt2) / 2;
    int k = getDividerWidth();
    paramInt4 = 0;
    paramInt2 = 0;
    int i = paramInt3 - paramInt1 - getPaddingRight() - getPaddingLeft();
    int m = 0;
    paramBoolean = ViewUtils.isLayoutRtl((View)this);
    int j;
    for (j = 0; j < i1; j++) {
      View view = getChildAt(j);
      if (view.getVisibility() != 8) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (layoutParams.isOverflowButton) {
          int i2;
          m = view.getMeasuredWidth();
          paramInt4 = m;
          if (hasSupportDividerBeforeChildAt(j))
            paramInt4 = m + k; 
          int i3 = view.getMeasuredHeight();
          if (paramBoolean) {
            m = getPaddingLeft() + layoutParams.leftMargin;
            i2 = m + paramInt4;
          } else {
            i2 = getWidth() - getPaddingRight() - layoutParams.rightMargin;
            m = i2 - paramInt4;
          } 
          int i4 = n - i3 / 2;
          view.layout(m, i4, i2, i4 + i3);
          i -= paramInt4;
          m = 1;
        } else {
          i -= view.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin;
          hasSupportDividerBeforeChildAt(j);
          paramInt2++;
        } 
      } 
    } 
    if (i1 == 1 && m == 0) {
      View view = getChildAt(0);
      paramInt2 = view.getMeasuredWidth();
      paramInt4 = view.getMeasuredHeight();
      paramInt1 = (paramInt3 - paramInt1) / 2 - paramInt2 / 2;
      paramInt3 = n - paramInt4 / 2;
      view.layout(paramInt1, paramInt3, paramInt1 + paramInt2, paramInt3 + paramInt4);
      return;
    } 
    paramInt1 = paramInt2 - (m ^ 0x1);
    if (paramInt1 > 0) {
      paramInt1 = i / paramInt1;
    } else {
      paramInt1 = 0;
    } 
    i = Math.max(0, paramInt1);
    if (paramBoolean) {
      paramInt3 = getWidth() - getPaddingRight();
      for (paramInt1 = 0; paramInt1 < i1; paramInt1++) {
        View view = getChildAt(paramInt1);
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (view.getVisibility() != 8 && !layoutParams.isOverflowButton) {
          k = paramInt3 - layoutParams.rightMargin;
          j = view.getMeasuredWidth();
          paramInt3 = view.getMeasuredHeight();
          m = n - paramInt3 / 2;
          view.layout(k - j, m, k, m + paramInt3);
          paramInt3 = k - layoutParams.leftMargin + j + i;
        } 
      } 
    } else {
      paramInt2 = getPaddingLeft();
      paramInt1 = 0;
      while (paramInt1 < i1) {
        View view = getChildAt(paramInt1);
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        paramInt3 = paramInt2;
        if (view.getVisibility() != 8)
          if (layoutParams.isOverflowButton) {
            paramInt3 = paramInt2;
          } else {
            paramInt3 = paramInt2 + layoutParams.leftMargin;
            paramInt2 = view.getMeasuredWidth();
            paramInt4 = view.getMeasuredHeight();
            j = n - paramInt4 / 2;
            view.layout(paramInt3, j, paramInt3 + paramInt2, j + paramInt4);
            paramInt3 += layoutParams.rightMargin + paramInt2 + i;
          }  
        paramInt1++;
        paramInt2 = paramInt3;
      } 
    } 
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    boolean bool1;
    boolean bool2 = this.mFormatItems;
    if (View.MeasureSpec.getMode(paramInt1) == 1073741824) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    this.mFormatItems = bool1;
    if (bool2 != bool1)
      this.mFormatItemsWidth = 0; 
    int i = View.MeasureSpec.getSize(paramInt1);
    if (this.mFormatItems) {
      MenuBuilder menuBuilder = this.mMenu;
      if (menuBuilder != null && i != this.mFormatItemsWidth) {
        this.mFormatItemsWidth = i;
        menuBuilder.onItemsChanged(true);
      } 
    } 
    int j = getChildCount();
    if (this.mFormatItems && j > 0) {
      onMeasureExactFormat(paramInt1, paramInt2);
    } else {
      for (i = 0; i < j; i++) {
        LayoutParams layoutParams = (LayoutParams)getChildAt(i).getLayoutParams();
        layoutParams.rightMargin = 0;
        layoutParams.leftMargin = 0;
      } 
      super.onMeasure(paramInt1, paramInt2);
    } 
  }
  
  public MenuBuilder peekMenu() {
    return this.mMenu;
  }
  
  public void setExpandedActionViewsExclusive(boolean paramBoolean) {
    this.mPresenter.setExpandedActionViewsExclusive(paramBoolean);
  }
  
  public void setMenuCallbacks(MenuPresenter.Callback paramCallback, MenuBuilder.Callback paramCallback1) {
    this.mActionMenuPresenterCallback = paramCallback;
    this.mMenuBuilderCallback = paramCallback1;
  }
  
  public void setOnMenuItemClickListener(OnMenuItemClickListener paramOnMenuItemClickListener) {
    this.mOnMenuItemClickListener = paramOnMenuItemClickListener;
  }
  
  public void setOverflowIcon(Drawable paramDrawable) {
    getMenu();
    this.mPresenter.setOverflowIcon(paramDrawable);
  }
  
  public void setOverflowReserved(boolean paramBoolean) {
    this.mReserveOverflow = paramBoolean;
  }
  
  public void setPopupTheme(int paramInt) {
    if (this.mPopupTheme != paramInt) {
      this.mPopupTheme = paramInt;
      if (paramInt == 0) {
        this.mPopupContext = getContext();
      } else {
        this.mPopupContext = (Context)new ContextThemeWrapper(getContext(), paramInt);
      } 
    } 
  }
  
  public void setPresenter(ActionMenuPresenter paramActionMenuPresenter) {
    this.mPresenter = paramActionMenuPresenter;
    paramActionMenuPresenter.setMenuView(this);
  }
  
  public boolean showOverflowMenu() {
    boolean bool;
    ActionMenuPresenter actionMenuPresenter = this.mPresenter;
    if (actionMenuPresenter != null && actionMenuPresenter.showOverflowMenu()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public static interface ActionMenuChildView {
    boolean needsDividerAfter();
    
    boolean needsDividerBefore();
  }
  
  private static class ActionMenuPresenterCallback implements MenuPresenter.Callback {
    public void onCloseMenu(MenuBuilder param1MenuBuilder, boolean param1Boolean) {}
    
    public boolean onOpenSubMenu(MenuBuilder param1MenuBuilder) {
      return false;
    }
  }
  
  public static class LayoutParams extends LinearLayoutCompat.LayoutParams {
    @ExportedProperty
    public int cellsUsed;
    
    @ExportedProperty
    public boolean expandable;
    
    boolean expanded;
    
    @ExportedProperty
    public int extraPixels;
    
    @ExportedProperty
    public boolean isOverflowButton;
    
    @ExportedProperty
    public boolean preventEdgeOffset;
    
    public LayoutParams(int param1Int1, int param1Int2) {
      super(param1Int1, param1Int2);
      this.isOverflowButton = false;
    }
    
    LayoutParams(int param1Int1, int param1Int2, boolean param1Boolean) {
      super(param1Int1, param1Int2);
      this.isOverflowButton = param1Boolean;
    }
    
    public LayoutParams(Context param1Context, AttributeSet param1AttributeSet) {
      super(param1Context, param1AttributeSet);
    }
    
    public LayoutParams(ViewGroup.LayoutParams param1LayoutParams) {
      super(param1LayoutParams);
    }
    
    public LayoutParams(LayoutParams param1LayoutParams) {
      super((ViewGroup.LayoutParams)param1LayoutParams);
      this.isOverflowButton = param1LayoutParams.isOverflowButton;
    }
  }
  
  private class MenuBuilderCallback implements MenuBuilder.Callback {
    final ActionMenuView this$0;
    
    public boolean onMenuItemSelected(MenuBuilder param1MenuBuilder, MenuItem param1MenuItem) {
      boolean bool;
      if (ActionMenuView.this.mOnMenuItemClickListener != null && ActionMenuView.this.mOnMenuItemClickListener.onMenuItemClick(param1MenuItem)) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public void onMenuModeChange(MenuBuilder param1MenuBuilder) {
      if (ActionMenuView.this.mMenuBuilderCallback != null)
        ActionMenuView.this.mMenuBuilderCallback.onMenuModeChange(param1MenuBuilder); 
    }
  }
  
  public static interface OnMenuItemClickListener {
    boolean onMenuItemClick(MenuItem param1MenuItem);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\appcompat\widget\ActionMenuView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */