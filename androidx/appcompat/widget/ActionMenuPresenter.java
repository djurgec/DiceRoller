package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.SparseBooleanArray;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.R;
import androidx.appcompat.view.ActionBarPolicy;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.appcompat.view.menu.BaseMenuPresenter;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.view.menu.MenuPresenter;
import androidx.appcompat.view.menu.MenuView;
import androidx.appcompat.view.menu.ShowableListMenu;
import androidx.appcompat.view.menu.SubMenuBuilder;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ActionProvider;
import java.util.ArrayList;

class ActionMenuPresenter extends BaseMenuPresenter implements ActionProvider.SubUiVisibilityListener {
  private static final String TAG = "ActionMenuPresenter";
  
  private final SparseBooleanArray mActionButtonGroups = new SparseBooleanArray();
  
  ActionButtonSubmenu mActionButtonPopup;
  
  private int mActionItemWidthLimit;
  
  private boolean mExpandedActionViewsExclusive;
  
  private int mMaxItems;
  
  private boolean mMaxItemsSet;
  
  private int mMinCellSize;
  
  int mOpenSubMenuId;
  
  OverflowMenuButton mOverflowButton;
  
  OverflowPopup mOverflowPopup;
  
  private Drawable mPendingOverflowIcon;
  
  private boolean mPendingOverflowIconSet;
  
  private ActionMenuPopupCallback mPopupCallback;
  
  final PopupPresenterCallback mPopupPresenterCallback = new PopupPresenterCallback();
  
  OpenOverflowRunnable mPostedOpenRunnable;
  
  private boolean mReserveOverflow;
  
  private boolean mReserveOverflowSet;
  
  private boolean mStrictWidthLimit;
  
  private int mWidthLimit;
  
  private boolean mWidthLimitSet;
  
  public ActionMenuPresenter(Context paramContext) {
    super(paramContext, R.layout.abc_action_menu_layout, R.layout.abc_action_menu_item_layout);
  }
  
  private View findViewForItem(MenuItem paramMenuItem) {
    ViewGroup viewGroup = (ViewGroup)this.mMenuView;
    if (viewGroup == null)
      return null; 
    int i = viewGroup.getChildCount();
    for (byte b = 0; b < i; b++) {
      View view = viewGroup.getChildAt(b);
      if (view instanceof MenuView.ItemView && ((MenuView.ItemView)view).getItemData() == paramMenuItem)
        return view; 
    } 
    return null;
  }
  
  public void bindItemView(MenuItemImpl paramMenuItemImpl, MenuView.ItemView paramItemView) {
    paramItemView.initialize(paramMenuItemImpl, 0);
    ActionMenuView actionMenuView = (ActionMenuView)this.mMenuView;
    ActionMenuItemView actionMenuItemView = (ActionMenuItemView)paramItemView;
    actionMenuItemView.setItemInvoker(actionMenuView);
    if (this.mPopupCallback == null)
      this.mPopupCallback = new ActionMenuPopupCallback(); 
    actionMenuItemView.setPopupCallback(this.mPopupCallback);
  }
  
  public boolean dismissPopupMenus() {
    return hideOverflowMenu() | hideSubMenus();
  }
  
  public boolean filterLeftoverView(ViewGroup paramViewGroup, int paramInt) {
    return (paramViewGroup.getChildAt(paramInt) == this.mOverflowButton) ? false : super.filterLeftoverView(paramViewGroup, paramInt);
  }
  
  public boolean flagActionItems() {
    // Byte code:
    //   0: aload_0
    //   1: getfield mMenu : Landroidx/appcompat/view/menu/MenuBuilder;
    //   4: ifnull -> 25
    //   7: aload_0
    //   8: getfield mMenu : Landroidx/appcompat/view/menu/MenuBuilder;
    //   11: invokevirtual getVisibleItems : ()Ljava/util/ArrayList;
    //   14: astore #15
    //   16: aload #15
    //   18: invokevirtual size : ()I
    //   21: istore_3
    //   22: goto -> 30
    //   25: aconst_null
    //   26: astore #15
    //   28: iconst_0
    //   29: istore_3
    //   30: aload_0
    //   31: getfield mMaxItems : I
    //   34: istore_1
    //   35: aload_0
    //   36: getfield mActionItemWidthLimit : I
    //   39: istore #9
    //   41: iconst_0
    //   42: iconst_0
    //   43: invokestatic makeMeasureSpec : (II)I
    //   46: istore #11
    //   48: aload_0
    //   49: getfield mMenuView : Landroidx/appcompat/view/menu/MenuView;
    //   52: checkcast android/view/ViewGroup
    //   55: astore #16
    //   57: iconst_0
    //   58: istore #4
    //   60: iconst_0
    //   61: istore #6
    //   63: iconst_0
    //   64: istore #8
    //   66: iconst_0
    //   67: istore #5
    //   69: iconst_0
    //   70: istore_2
    //   71: iload_2
    //   72: iload_3
    //   73: if_icmpge -> 151
    //   76: aload #15
    //   78: iload_2
    //   79: invokevirtual get : (I)Ljava/lang/Object;
    //   82: checkcast androidx/appcompat/view/menu/MenuItemImpl
    //   85: astore #17
    //   87: aload #17
    //   89: invokevirtual requiresActionButton : ()Z
    //   92: ifeq -> 101
    //   95: iinc #4, 1
    //   98: goto -> 118
    //   101: aload #17
    //   103: invokevirtual requestsActionButton : ()Z
    //   106: ifeq -> 115
    //   109: iinc #6, 1
    //   112: goto -> 118
    //   115: iconst_1
    //   116: istore #5
    //   118: iload_1
    //   119: istore #7
    //   121: aload_0
    //   122: getfield mExpandedActionViewsExclusive : Z
    //   125: ifeq -> 142
    //   128: iload_1
    //   129: istore #7
    //   131: aload #17
    //   133: invokevirtual isActionViewExpanded : ()Z
    //   136: ifeq -> 142
    //   139: iconst_0
    //   140: istore #7
    //   142: iinc #2, 1
    //   145: iload #7
    //   147: istore_1
    //   148: goto -> 71
    //   151: iload_1
    //   152: istore_2
    //   153: aload_0
    //   154: getfield mReserveOverflow : Z
    //   157: ifeq -> 180
    //   160: iload #5
    //   162: ifne -> 176
    //   165: iload_1
    //   166: istore_2
    //   167: iload #4
    //   169: iload #6
    //   171: iadd
    //   172: iload_1
    //   173: if_icmple -> 180
    //   176: iload_1
    //   177: iconst_1
    //   178: isub
    //   179: istore_2
    //   180: iload_2
    //   181: iload #4
    //   183: isub
    //   184: istore #10
    //   186: aload_0
    //   187: getfield mActionButtonGroups : Landroid/util/SparseBooleanArray;
    //   190: astore #17
    //   192: aload #17
    //   194: invokevirtual clear : ()V
    //   197: iconst_0
    //   198: istore #6
    //   200: iconst_0
    //   201: istore_2
    //   202: aload_0
    //   203: getfield mStrictWidthLimit : Z
    //   206: ifeq -> 229
    //   209: aload_0
    //   210: getfield mMinCellSize : I
    //   213: istore_1
    //   214: iload #9
    //   216: iload_1
    //   217: idiv
    //   218: istore_2
    //   219: iload_1
    //   220: iload #9
    //   222: iload_1
    //   223: irem
    //   224: iload_2
    //   225: idiv
    //   226: iadd
    //   227: istore #6
    //   229: iconst_0
    //   230: istore #7
    //   232: iload #8
    //   234: istore_1
    //   235: iload #4
    //   237: istore #8
    //   239: iload #9
    //   241: istore #5
    //   243: iload #10
    //   245: istore #4
    //   247: iload_3
    //   248: istore #9
    //   250: iload #7
    //   252: iload #9
    //   254: if_icmpge -> 714
    //   257: aload #15
    //   259: iload #7
    //   261: invokevirtual get : (I)Ljava/lang/Object;
    //   264: checkcast androidx/appcompat/view/menu/MenuItemImpl
    //   267: astore #18
    //   269: aload #18
    //   271: invokevirtual requiresActionButton : ()Z
    //   274: ifeq -> 375
    //   277: aload_0
    //   278: aload #18
    //   280: aconst_null
    //   281: aload #16
    //   283: invokevirtual getItemView : (Landroidx/appcompat/view/menu/MenuItemImpl;Landroid/view/View;Landroid/view/ViewGroup;)Landroid/view/View;
    //   286: astore #19
    //   288: aload_0
    //   289: getfield mStrictWidthLimit : Z
    //   292: ifeq -> 312
    //   295: iload_2
    //   296: aload #19
    //   298: iload #6
    //   300: iload_2
    //   301: iload #11
    //   303: iconst_0
    //   304: invokestatic measureChildForCells : (Landroid/view/View;IIII)I
    //   307: isub
    //   308: istore_2
    //   309: goto -> 321
    //   312: aload #19
    //   314: iload #11
    //   316: iload #11
    //   318: invokevirtual measure : (II)V
    //   321: aload #19
    //   323: invokevirtual getMeasuredWidth : ()I
    //   326: istore #10
    //   328: iload #5
    //   330: iload #10
    //   332: isub
    //   333: istore #5
    //   335: iload_1
    //   336: istore_3
    //   337: iload_1
    //   338: ifne -> 344
    //   341: iload #10
    //   343: istore_3
    //   344: aload #18
    //   346: invokevirtual getGroupId : ()I
    //   349: istore_1
    //   350: iload_1
    //   351: ifeq -> 364
    //   354: aload #17
    //   356: iload_1
    //   357: iconst_1
    //   358: invokevirtual put : (IZ)V
    //   361: goto -> 364
    //   364: aload #18
    //   366: iconst_1
    //   367: invokevirtual setIsActionButton : (Z)V
    //   370: iload_3
    //   371: istore_1
    //   372: goto -> 708
    //   375: aload #18
    //   377: invokevirtual requestsActionButton : ()Z
    //   380: ifeq -> 702
    //   383: aload #18
    //   385: invokevirtual getGroupId : ()I
    //   388: istore #12
    //   390: aload #17
    //   392: iload #12
    //   394: invokevirtual get : (I)Z
    //   397: istore #14
    //   399: iload #4
    //   401: ifgt -> 409
    //   404: iload #14
    //   406: ifeq -> 431
    //   409: iload #5
    //   411: ifle -> 431
    //   414: aload_0
    //   415: getfield mStrictWidthLimit : Z
    //   418: ifeq -> 425
    //   421: iload_2
    //   422: ifle -> 431
    //   425: iconst_1
    //   426: istore #13
    //   428: goto -> 434
    //   431: iconst_0
    //   432: istore #13
    //   434: iload #13
    //   436: ifeq -> 569
    //   439: aload_0
    //   440: aload #18
    //   442: aconst_null
    //   443: aload #16
    //   445: invokevirtual getItemView : (Landroidx/appcompat/view/menu/MenuItemImpl;Landroid/view/View;Landroid/view/ViewGroup;)Landroid/view/View;
    //   448: astore #19
    //   450: aload_0
    //   451: getfield mStrictWidthLimit : Z
    //   454: ifeq -> 486
    //   457: aload #19
    //   459: iload #6
    //   461: iload_2
    //   462: iload #11
    //   464: iconst_0
    //   465: invokestatic measureChildForCells : (Landroid/view/View;IIII)I
    //   468: istore_3
    //   469: iload_2
    //   470: iload_3
    //   471: isub
    //   472: istore_2
    //   473: iload_3
    //   474: ifne -> 483
    //   477: iconst_0
    //   478: istore #13
    //   480: goto -> 483
    //   483: goto -> 495
    //   486: aload #19
    //   488: iload #11
    //   490: iload #11
    //   492: invokevirtual measure : (II)V
    //   495: aload #19
    //   497: invokevirtual getMeasuredWidth : ()I
    //   500: istore #10
    //   502: iload #5
    //   504: iload #10
    //   506: isub
    //   507: istore #5
    //   509: iload_1
    //   510: istore_3
    //   511: iload_1
    //   512: ifne -> 518
    //   515: iload #10
    //   517: istore_3
    //   518: aload_0
    //   519: getfield mStrictWidthLimit : Z
    //   522: ifeq -> 546
    //   525: iload #5
    //   527: iflt -> 535
    //   530: iconst_1
    //   531: istore_1
    //   532: goto -> 537
    //   535: iconst_0
    //   536: istore_1
    //   537: iload_1
    //   538: iload #13
    //   540: iand
    //   541: istore #13
    //   543: goto -> 571
    //   546: iload #5
    //   548: iload_3
    //   549: iadd
    //   550: ifle -> 558
    //   553: iconst_1
    //   554: istore_1
    //   555: goto -> 560
    //   558: iconst_0
    //   559: istore_1
    //   560: iload_1
    //   561: iload #13
    //   563: iand
    //   564: istore #13
    //   566: goto -> 571
    //   569: iload_1
    //   570: istore_3
    //   571: iload #4
    //   573: istore_1
    //   574: iload #13
    //   576: ifeq -> 595
    //   579: iload #12
    //   581: ifeq -> 595
    //   584: aload #17
    //   586: iload #12
    //   588: iconst_1
    //   589: invokevirtual put : (IZ)V
    //   592: goto -> 677
    //   595: iload #14
    //   597: ifeq -> 677
    //   600: aload #17
    //   602: iload #12
    //   604: iconst_0
    //   605: invokevirtual put : (IZ)V
    //   608: iconst_0
    //   609: istore #10
    //   611: iload #10
    //   613: iload #7
    //   615: if_icmpge -> 674
    //   618: aload #15
    //   620: iload #10
    //   622: invokevirtual get : (I)Ljava/lang/Object;
    //   625: checkcast androidx/appcompat/view/menu/MenuItemImpl
    //   628: astore #19
    //   630: iload_1
    //   631: istore #4
    //   633: aload #19
    //   635: invokevirtual getGroupId : ()I
    //   638: iload #12
    //   640: if_icmpne -> 665
    //   643: iload_1
    //   644: istore #4
    //   646: aload #19
    //   648: invokevirtual isActionButton : ()Z
    //   651: ifeq -> 659
    //   654: iload_1
    //   655: iconst_1
    //   656: iadd
    //   657: istore #4
    //   659: aload #19
    //   661: iconst_0
    //   662: invokevirtual setIsActionButton : (Z)V
    //   665: iinc #10, 1
    //   668: iload #4
    //   670: istore_1
    //   671: goto -> 611
    //   674: goto -> 677
    //   677: iload_1
    //   678: istore #4
    //   680: iload #13
    //   682: ifeq -> 690
    //   685: iload_1
    //   686: iconst_1
    //   687: isub
    //   688: istore #4
    //   690: aload #18
    //   692: iload #13
    //   694: invokevirtual setIsActionButton : (Z)V
    //   697: iload_3
    //   698: istore_1
    //   699: goto -> 708
    //   702: aload #18
    //   704: iconst_0
    //   705: invokevirtual setIsActionButton : (Z)V
    //   708: iinc #7, 1
    //   711: goto -> 250
    //   714: iconst_1
    //   715: ireturn
  }
  
  public View getItemView(MenuItemImpl paramMenuItemImpl, View paramView, ViewGroup paramViewGroup) {
    boolean bool;
    View view = paramMenuItemImpl.getActionView();
    if (view == null || paramMenuItemImpl.hasCollapsibleActionView())
      view = super.getItemView(paramMenuItemImpl, paramView, paramViewGroup); 
    if (paramMenuItemImpl.isActionViewExpanded()) {
      bool = true;
    } else {
      bool = false;
    } 
    view.setVisibility(bool);
    ActionMenuView actionMenuView = (ActionMenuView)paramViewGroup;
    ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
    if (!actionMenuView.checkLayoutParams(layoutParams))
      view.setLayoutParams((ViewGroup.LayoutParams)actionMenuView.generateLayoutParams(layoutParams)); 
    return view;
  }
  
  public MenuView getMenuView(ViewGroup paramViewGroup) {
    MenuView menuView2 = this.mMenuView;
    MenuView menuView1 = super.getMenuView(paramViewGroup);
    if (menuView2 != menuView1)
      ((ActionMenuView)menuView1).setPresenter(this); 
    return menuView1;
  }
  
  public Drawable getOverflowIcon() {
    OverflowMenuButton overflowMenuButton = this.mOverflowButton;
    return (overflowMenuButton != null) ? overflowMenuButton.getDrawable() : (this.mPendingOverflowIconSet ? this.mPendingOverflowIcon : null);
  }
  
  public boolean hideOverflowMenu() {
    if (this.mPostedOpenRunnable != null && this.mMenuView != null) {
      ((View)this.mMenuView).removeCallbacks(this.mPostedOpenRunnable);
      this.mPostedOpenRunnable = null;
      return true;
    } 
    OverflowPopup overflowPopup = this.mOverflowPopup;
    if (overflowPopup != null) {
      overflowPopup.dismiss();
      return true;
    } 
    return false;
  }
  
  public boolean hideSubMenus() {
    ActionButtonSubmenu actionButtonSubmenu = this.mActionButtonPopup;
    if (actionButtonSubmenu != null) {
      actionButtonSubmenu.dismiss();
      return true;
    } 
    return false;
  }
  
  public void initForMenu(Context paramContext, MenuBuilder paramMenuBuilder) {
    super.initForMenu(paramContext, paramMenuBuilder);
    Resources resources = paramContext.getResources();
    ActionBarPolicy actionBarPolicy = ActionBarPolicy.get(paramContext);
    if (!this.mReserveOverflowSet)
      this.mReserveOverflow = actionBarPolicy.showsOverflowMenuButton(); 
    if (!this.mWidthLimitSet)
      this.mWidthLimit = actionBarPolicy.getEmbeddedMenuWidthLimit(); 
    if (!this.mMaxItemsSet)
      this.mMaxItems = actionBarPolicy.getMaxActionButtons(); 
    int i = this.mWidthLimit;
    if (this.mReserveOverflow) {
      if (this.mOverflowButton == null) {
        OverflowMenuButton overflowMenuButton = new OverflowMenuButton(this.mSystemContext);
        this.mOverflowButton = overflowMenuButton;
        if (this.mPendingOverflowIconSet) {
          overflowMenuButton.setImageDrawable(this.mPendingOverflowIcon);
          this.mPendingOverflowIcon = null;
          this.mPendingOverflowIconSet = false;
        } 
        int j = View.MeasureSpec.makeMeasureSpec(0, 0);
        this.mOverflowButton.measure(j, j);
      } 
      i -= this.mOverflowButton.getMeasuredWidth();
    } else {
      this.mOverflowButton = null;
    } 
    this.mActionItemWidthLimit = i;
    this.mMinCellSize = (int)((resources.getDisplayMetrics()).density * 56.0F);
  }
  
  public boolean isOverflowMenuShowPending() {
    return (this.mPostedOpenRunnable != null || isOverflowMenuShowing());
  }
  
  public boolean isOverflowMenuShowing() {
    boolean bool;
    OverflowPopup overflowPopup = this.mOverflowPopup;
    if (overflowPopup != null && overflowPopup.isShowing()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isOverflowReserved() {
    return this.mReserveOverflow;
  }
  
  public void onCloseMenu(MenuBuilder paramMenuBuilder, boolean paramBoolean) {
    dismissPopupMenus();
    super.onCloseMenu(paramMenuBuilder, paramBoolean);
  }
  
  public void onConfigurationChanged(Configuration paramConfiguration) {
    if (!this.mMaxItemsSet)
      this.mMaxItems = ActionBarPolicy.get(this.mContext).getMaxActionButtons(); 
    if (this.mMenu != null)
      this.mMenu.onItemsChanged(true); 
  }
  
  public void onRestoreInstanceState(Parcelable paramParcelable) {
    if (!(paramParcelable instanceof SavedState))
      return; 
    paramParcelable = paramParcelable;
    if (((SavedState)paramParcelable).openSubMenuId > 0) {
      MenuItem menuItem = this.mMenu.findItem(((SavedState)paramParcelable).openSubMenuId);
      if (menuItem != null)
        onSubMenuSelected((SubMenuBuilder)menuItem.getSubMenu()); 
    } 
  }
  
  public Parcelable onSaveInstanceState() {
    SavedState savedState = new SavedState();
    savedState.openSubMenuId = this.mOpenSubMenuId;
    return savedState;
  }
  
  public boolean onSubMenuSelected(SubMenuBuilder paramSubMenuBuilder) {
    boolean bool1;
    if (!paramSubMenuBuilder.hasVisibleItems())
      return false; 
    SubMenuBuilder subMenuBuilder;
    for (subMenuBuilder = paramSubMenuBuilder; subMenuBuilder.getParentMenu() != this.mMenu; subMenuBuilder = (SubMenuBuilder)subMenuBuilder.getParentMenu());
    View view = findViewForItem(subMenuBuilder.getItem());
    if (view == null)
      return false; 
    this.mOpenSubMenuId = paramSubMenuBuilder.getItem().getItemId();
    boolean bool2 = false;
    int i = paramSubMenuBuilder.size();
    byte b = 0;
    while (true) {
      bool1 = bool2;
      if (b < i) {
        MenuItem menuItem = paramSubMenuBuilder.getItem(b);
        if (menuItem.isVisible() && menuItem.getIcon() != null) {
          bool1 = true;
          break;
        } 
        b++;
        continue;
      } 
      break;
    } 
    ActionButtonSubmenu actionButtonSubmenu = new ActionButtonSubmenu(this.mContext, paramSubMenuBuilder, view);
    this.mActionButtonPopup = actionButtonSubmenu;
    actionButtonSubmenu.setForceShowIcon(bool1);
    this.mActionButtonPopup.show();
    super.onSubMenuSelected(paramSubMenuBuilder);
    return true;
  }
  
  public void onSubUiVisibilityChanged(boolean paramBoolean) {
    if (paramBoolean) {
      super.onSubMenuSelected(null);
    } else if (this.mMenu != null) {
      this.mMenu.close(false);
    } 
  }
  
  public void setExpandedActionViewsExclusive(boolean paramBoolean) {
    this.mExpandedActionViewsExclusive = paramBoolean;
  }
  
  public void setItemLimit(int paramInt) {
    this.mMaxItems = paramInt;
    this.mMaxItemsSet = true;
  }
  
  public void setMenuView(ActionMenuView paramActionMenuView) {
    this.mMenuView = paramActionMenuView;
    paramActionMenuView.initialize(this.mMenu);
  }
  
  public void setOverflowIcon(Drawable paramDrawable) {
    OverflowMenuButton overflowMenuButton = this.mOverflowButton;
    if (overflowMenuButton != null) {
      overflowMenuButton.setImageDrawable(paramDrawable);
    } else {
      this.mPendingOverflowIconSet = true;
      this.mPendingOverflowIcon = paramDrawable;
    } 
  }
  
  public void setReserveOverflow(boolean paramBoolean) {
    this.mReserveOverflow = paramBoolean;
    this.mReserveOverflowSet = true;
  }
  
  public void setWidthLimit(int paramInt, boolean paramBoolean) {
    this.mWidthLimit = paramInt;
    this.mStrictWidthLimit = paramBoolean;
    this.mWidthLimitSet = true;
  }
  
  public boolean shouldIncludeItem(int paramInt, MenuItemImpl paramMenuItemImpl) {
    return paramMenuItemImpl.isActionButton();
  }
  
  public boolean showOverflowMenu() {
    if (this.mReserveOverflow && !isOverflowMenuShowing() && this.mMenu != null && this.mMenuView != null && this.mPostedOpenRunnable == null && !this.mMenu.getNonActionItems().isEmpty()) {
      this.mPostedOpenRunnable = new OpenOverflowRunnable(new OverflowPopup(this.mContext, this.mMenu, (View)this.mOverflowButton, true));
      ((View)this.mMenuView).post(this.mPostedOpenRunnable);
      return true;
    } 
    return false;
  }
  
  public void updateMenuView(boolean paramBoolean) {
    ArrayList<MenuItemImpl> arrayList;
    super.updateMenuView(paramBoolean);
    ((View)this.mMenuView).requestLayout();
    if (this.mMenu != null) {
      ArrayList<MenuItemImpl> arrayList1 = this.mMenu.getActionItems();
      int k = arrayList1.size();
      for (byte b = 0; b < k; b++) {
        arrayList = (ArrayList<MenuItemImpl>)((MenuItemImpl)arrayList1.get(b)).getSupportActionProvider();
        if (arrayList != null)
          arrayList.setSubUiVisibilityListener(this); 
      } 
    } 
    if (this.mMenu != null) {
      arrayList = this.mMenu.getNonActionItems();
    } else {
      arrayList = null;
    } 
    int j = 0;
    int i = j;
    if (this.mReserveOverflow) {
      i = j;
      if (arrayList != null) {
        j = arrayList.size();
        i = 0;
        if (j == 1) {
          i = ((MenuItemImpl)arrayList.get(0)).isActionViewExpanded() ^ true;
        } else if (j > 0) {
          i = 1;
        } 
      } 
    } 
    if (i != 0) {
      if (this.mOverflowButton == null)
        this.mOverflowButton = new OverflowMenuButton(this.mSystemContext); 
      ViewGroup viewGroup = (ViewGroup)this.mOverflowButton.getParent();
      if (viewGroup != this.mMenuView) {
        if (viewGroup != null)
          viewGroup.removeView((View)this.mOverflowButton); 
        viewGroup = (ActionMenuView)this.mMenuView;
        viewGroup.addView((View)this.mOverflowButton, (ViewGroup.LayoutParams)viewGroup.generateOverflowButtonLayoutParams());
      } 
    } else {
      OverflowMenuButton overflowMenuButton = this.mOverflowButton;
      if (overflowMenuButton != null && overflowMenuButton.getParent() == this.mMenuView)
        ((ViewGroup)this.mMenuView).removeView((View)this.mOverflowButton); 
    } 
    ((ActionMenuView)this.mMenuView).setOverflowReserved(this.mReserveOverflow);
  }
  
  private class ActionButtonSubmenu extends MenuPopupHelper {
    final ActionMenuPresenter this$0;
    
    public ActionButtonSubmenu(Context param1Context, SubMenuBuilder param1SubMenuBuilder, View param1View) {
      super(param1Context, (MenuBuilder)param1SubMenuBuilder, param1View, false, R.attr.actionOverflowMenuStyle);
      if (!((MenuItemImpl)param1SubMenuBuilder.getItem()).isActionButton()) {
        ActionMenuPresenter.OverflowMenuButton overflowMenuButton;
        if (ActionMenuPresenter.this.mOverflowButton == null) {
          View view = (View)ActionMenuPresenter.this.mMenuView;
        } else {
          overflowMenuButton = ActionMenuPresenter.this.mOverflowButton;
        } 
        setAnchorView((View)overflowMenuButton);
      } 
      setPresenterCallback(ActionMenuPresenter.this.mPopupPresenterCallback);
    }
    
    protected void onDismiss() {
      ActionMenuPresenter.this.mActionButtonPopup = null;
      ActionMenuPresenter.this.mOpenSubMenuId = 0;
      super.onDismiss();
    }
  }
  
  private class ActionMenuPopupCallback extends ActionMenuItemView.PopupCallback {
    final ActionMenuPresenter this$0;
    
    public ShowableListMenu getPopup() {
      ShowableListMenu showableListMenu;
      if (ActionMenuPresenter.this.mActionButtonPopup != null) {
        showableListMenu = (ShowableListMenu)ActionMenuPresenter.this.mActionButtonPopup.getPopup();
      } else {
        showableListMenu = null;
      } 
      return showableListMenu;
    }
  }
  
  private class OpenOverflowRunnable implements Runnable {
    private ActionMenuPresenter.OverflowPopup mPopup;
    
    final ActionMenuPresenter this$0;
    
    public OpenOverflowRunnable(ActionMenuPresenter.OverflowPopup param1OverflowPopup) {
      this.mPopup = param1OverflowPopup;
    }
    
    public void run() {
      if (ActionMenuPresenter.this.mMenu != null)
        ActionMenuPresenter.this.mMenu.changeMenuMode(); 
      View view = (View)ActionMenuPresenter.this.mMenuView;
      if (view != null && view.getWindowToken() != null && this.mPopup.tryShow())
        ActionMenuPresenter.this.mOverflowPopup = this.mPopup; 
      ActionMenuPresenter.this.mPostedOpenRunnable = null;
    }
  }
  
  private class OverflowMenuButton extends AppCompatImageView implements ActionMenuView.ActionMenuChildView {
    final ActionMenuPresenter this$0;
    
    public OverflowMenuButton(Context param1Context) {
      super(param1Context, (AttributeSet)null, R.attr.actionOverflowButtonStyle);
      setClickable(true);
      setFocusable(true);
      setVisibility(0);
      setEnabled(true);
      TooltipCompat.setTooltipText((View)this, getContentDescription());
      setOnTouchListener(new ForwardingListener((View)this) {
            final ActionMenuPresenter.OverflowMenuButton this$1;
            
            final ActionMenuPresenter val$this$0;
            
            public ShowableListMenu getPopup() {
              return (ShowableListMenu)((ActionMenuPresenter.this.mOverflowPopup == null) ? null : ActionMenuPresenter.this.mOverflowPopup.getPopup());
            }
            
            public boolean onForwardingStarted() {
              ActionMenuPresenter.this.showOverflowMenu();
              return true;
            }
            
            public boolean onForwardingStopped() {
              if (ActionMenuPresenter.this.mPostedOpenRunnable != null)
                return false; 
              ActionMenuPresenter.this.hideOverflowMenu();
              return true;
            }
          });
    }
    
    public boolean needsDividerAfter() {
      return false;
    }
    
    public boolean needsDividerBefore() {
      return false;
    }
    
    public boolean performClick() {
      if (super.performClick())
        return true; 
      playSoundEffect(0);
      ActionMenuPresenter.this.showOverflowMenu();
      return true;
    }
    
    protected boolean setFrame(int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
      boolean bool = super.setFrame(param1Int1, param1Int2, param1Int3, param1Int4);
      Drawable drawable2 = getDrawable();
      Drawable drawable1 = getBackground();
      if (drawable2 != null && drawable1 != null) {
        int k = getWidth();
        param1Int4 = getHeight();
        param1Int1 = Math.max(k, param1Int4) / 2;
        int i = getPaddingLeft();
        int j = getPaddingRight();
        param1Int2 = getPaddingTop();
        param1Int3 = getPaddingBottom();
        i = (k + i - j) / 2;
        param1Int2 = (param1Int4 + param1Int2 - param1Int3) / 2;
        DrawableCompat.setHotspotBounds(drawable1, i - param1Int1, param1Int2 - param1Int1, i + param1Int1, param1Int2 + param1Int1);
      } 
      return bool;
    }
  }
  
  class null extends ForwardingListener {
    final ActionMenuPresenter.OverflowMenuButton this$1;
    
    final ActionMenuPresenter val$this$0;
    
    null(View param1View) {
      super(param1View);
    }
    
    public ShowableListMenu getPopup() {
      return (ShowableListMenu)((ActionMenuPresenter.this.mOverflowPopup == null) ? null : ActionMenuPresenter.this.mOverflowPopup.getPopup());
    }
    
    public boolean onForwardingStarted() {
      ActionMenuPresenter.this.showOverflowMenu();
      return true;
    }
    
    public boolean onForwardingStopped() {
      if (ActionMenuPresenter.this.mPostedOpenRunnable != null)
        return false; 
      ActionMenuPresenter.this.hideOverflowMenu();
      return true;
    }
  }
  
  private class OverflowPopup extends MenuPopupHelper {
    final ActionMenuPresenter this$0;
    
    public OverflowPopup(Context param1Context, MenuBuilder param1MenuBuilder, View param1View, boolean param1Boolean) {
      super(param1Context, param1MenuBuilder, param1View, param1Boolean, R.attr.actionOverflowMenuStyle);
      setGravity(8388613);
      setPresenterCallback(ActionMenuPresenter.this.mPopupPresenterCallback);
    }
    
    protected void onDismiss() {
      if (ActionMenuPresenter.this.mMenu != null)
        ActionMenuPresenter.this.mMenu.close(); 
      ActionMenuPresenter.this.mOverflowPopup = null;
      super.onDismiss();
    }
  }
  
  private class PopupPresenterCallback implements MenuPresenter.Callback {
    final ActionMenuPresenter this$0;
    
    public void onCloseMenu(MenuBuilder param1MenuBuilder, boolean param1Boolean) {
      if (param1MenuBuilder instanceof SubMenuBuilder)
        param1MenuBuilder.getRootMenu().close(false); 
      MenuPresenter.Callback callback = ActionMenuPresenter.this.getCallback();
      if (callback != null)
        callback.onCloseMenu(param1MenuBuilder, param1Boolean); 
    }
    
    public boolean onOpenSubMenu(MenuBuilder param1MenuBuilder) {
      MenuBuilder menuBuilder = ActionMenuPresenter.this.mMenu;
      boolean bool = false;
      if (param1MenuBuilder == menuBuilder)
        return false; 
      ActionMenuPresenter.this.mOpenSubMenuId = ((SubMenuBuilder)param1MenuBuilder).getItem().getItemId();
      MenuPresenter.Callback callback = ActionMenuPresenter.this.getCallback();
      if (callback != null)
        bool = callback.onOpenSubMenu(param1MenuBuilder); 
      return bool;
    }
  }
  
  private static class SavedState implements Parcelable {
    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
        public ActionMenuPresenter.SavedState createFromParcel(Parcel param2Parcel) {
          return new ActionMenuPresenter.SavedState(param2Parcel);
        }
        
        public ActionMenuPresenter.SavedState[] newArray(int param2Int) {
          return new ActionMenuPresenter.SavedState[param2Int];
        }
      };
    
    public int openSubMenuId;
    
    SavedState() {}
    
    SavedState(Parcel param1Parcel) {
      this.openSubMenuId = param1Parcel.readInt();
    }
    
    public int describeContents() {
      return 0;
    }
    
    public void writeToParcel(Parcel param1Parcel, int param1Int) {
      param1Parcel.writeInt(this.openSubMenuId);
    }
  }
  
  class null implements Parcelable.Creator<SavedState> {
    public ActionMenuPresenter.SavedState createFromParcel(Parcel param1Parcel) {
      return new ActionMenuPresenter.SavedState(param1Parcel);
    }
    
    public ActionMenuPresenter.SavedState[] newArray(int param1Int) {
      return new ActionMenuPresenter.SavedState[param1Int];
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\appcompat\widget\ActionMenuPresenter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */