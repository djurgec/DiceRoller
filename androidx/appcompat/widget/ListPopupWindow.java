package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import androidx.appcompat.R;
import androidx.appcompat.view.menu.ShowableListMenu;
import androidx.core.view.ViewCompat;
import androidx.core.widget.PopupWindowCompat;
import java.lang.reflect.Method;

public class ListPopupWindow implements ShowableListMenu {
  private static final boolean DEBUG = false;
  
  static final int EXPAND_LIST_TIMEOUT = 250;
  
  public static final int INPUT_METHOD_FROM_FOCUSABLE = 0;
  
  public static final int INPUT_METHOD_NEEDED = 1;
  
  public static final int INPUT_METHOD_NOT_NEEDED = 2;
  
  public static final int MATCH_PARENT = -1;
  
  public static final int POSITION_PROMPT_ABOVE = 0;
  
  public static final int POSITION_PROMPT_BELOW = 1;
  
  private static final String TAG = "ListPopupWindow";
  
  public static final int WRAP_CONTENT = -2;
  
  private static Method sGetMaxAvailableHeightMethod;
  
  private static Method sSetClipToWindowEnabledMethod;
  
  private static Method sSetEpicenterBoundsMethod;
  
  private ListAdapter mAdapter;
  
  private Context mContext;
  
  private boolean mDropDownAlwaysVisible = false;
  
  private View mDropDownAnchorView;
  
  private int mDropDownGravity = 0;
  
  private int mDropDownHeight = -2;
  
  private int mDropDownHorizontalOffset;
  
  DropDownListView mDropDownList;
  
  private Drawable mDropDownListHighlight;
  
  private int mDropDownVerticalOffset;
  
  private boolean mDropDownVerticalOffsetSet;
  
  private int mDropDownWidth = -2;
  
  private int mDropDownWindowLayoutType = 1002;
  
  private Rect mEpicenterBounds;
  
  private boolean mForceIgnoreOutsideTouch = false;
  
  final Handler mHandler;
  
  private final ListSelectorHider mHideSelector = new ListSelectorHider();
  
  private AdapterView.OnItemClickListener mItemClickListener;
  
  private AdapterView.OnItemSelectedListener mItemSelectedListener;
  
  int mListItemExpandMaximum = Integer.MAX_VALUE;
  
  private boolean mModal;
  
  private DataSetObserver mObserver;
  
  private boolean mOverlapAnchor;
  
  private boolean mOverlapAnchorSet;
  
  PopupWindow mPopup;
  
  private int mPromptPosition = 0;
  
  private View mPromptView;
  
  final ResizePopupRunnable mResizePopupRunnable = new ResizePopupRunnable();
  
  private final PopupScrollListener mScrollListener = new PopupScrollListener();
  
  private Runnable mShowDropDownRunnable;
  
  private final Rect mTempRect = new Rect();
  
  private final PopupTouchInterceptor mTouchInterceptor = new PopupTouchInterceptor();
  
  static {
    if (Build.VERSION.SDK_INT <= 28) {
      try {
        sSetClipToWindowEnabledMethod = PopupWindow.class.getDeclaredMethod("setClipToScreenEnabled", new Class[] { boolean.class });
      } catch (NoSuchMethodException noSuchMethodException) {
        Log.i("ListPopupWindow", "Could not find method setClipToScreenEnabled() on PopupWindow. Oh well.");
      } 
      try {
        sSetEpicenterBoundsMethod = PopupWindow.class.getDeclaredMethod("setEpicenterBounds", new Class[] { Rect.class });
      } catch (NoSuchMethodException noSuchMethodException) {
        Log.i("ListPopupWindow", "Could not find method setEpicenterBounds(Rect) on PopupWindow. Oh well.");
      } 
    } 
    if (Build.VERSION.SDK_INT <= 23)
      try {
        sGetMaxAvailableHeightMethod = PopupWindow.class.getDeclaredMethod("getMaxAvailableHeight", new Class[] { View.class, int.class, boolean.class });
      } catch (NoSuchMethodException noSuchMethodException) {
        Log.i("ListPopupWindow", "Could not find method getMaxAvailableHeight(View, int, boolean) on PopupWindow. Oh well.");
      }  
  }
  
  public ListPopupWindow(Context paramContext) {
    this(paramContext, null, R.attr.listPopupWindowStyle);
  }
  
  public ListPopupWindow(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, R.attr.listPopupWindowStyle);
  }
  
  public ListPopupWindow(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public ListPopupWindow(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2) {
    this.mContext = paramContext;
    this.mHandler = new Handler(paramContext.getMainLooper());
    TypedArray typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.ListPopupWindow, paramInt1, paramInt2);
    this.mDropDownHorizontalOffset = typedArray.getDimensionPixelOffset(R.styleable.ListPopupWindow_android_dropDownHorizontalOffset, 0);
    int i = typedArray.getDimensionPixelOffset(R.styleable.ListPopupWindow_android_dropDownVerticalOffset, 0);
    this.mDropDownVerticalOffset = i;
    if (i != 0)
      this.mDropDownVerticalOffsetSet = true; 
    typedArray.recycle();
    AppCompatPopupWindow appCompatPopupWindow = new AppCompatPopupWindow(paramContext, paramAttributeSet, paramInt1, paramInt2);
    this.mPopup = appCompatPopupWindow;
    appCompatPopupWindow.setInputMethodMode(1);
  }
  
  private int buildDropDown() {
    byte b;
    int i = 0;
    int j = 0;
    DropDownListView dropDownListView = this.mDropDownList;
    boolean bool = false;
    if (dropDownListView == null) {
      LinearLayout linearLayout;
      Context context = this.mContext;
      this.mShowDropDownRunnable = new Runnable() {
          final ListPopupWindow this$0;
          
          public void run() {
            View view = ListPopupWindow.this.getAnchorView();
            if (view != null && view.getWindowToken() != null)
              ListPopupWindow.this.show(); 
          }
        };
      dropDownListView = createDropDownListView(context, this.mModal ^ true);
      this.mDropDownList = dropDownListView;
      Drawable drawable1 = this.mDropDownListHighlight;
      if (drawable1 != null)
        dropDownListView.setSelector(drawable1); 
      this.mDropDownList.setAdapter(this.mAdapter);
      this.mDropDownList.setOnItemClickListener(this.mItemClickListener);
      this.mDropDownList.setFocusable(true);
      this.mDropDownList.setFocusableInTouchMode(true);
      this.mDropDownList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            final ListPopupWindow this$0;
            
            public void onItemSelected(AdapterView<?> param1AdapterView, View param1View, int param1Int, long param1Long) {
              if (param1Int != -1) {
                DropDownListView dropDownListView = ListPopupWindow.this.mDropDownList;
                if (dropDownListView != null)
                  dropDownListView.setListSelectionHidden(false); 
              } 
            }
            
            public void onNothingSelected(AdapterView<?> param1AdapterView) {}
          });
      this.mDropDownList.setOnScrollListener(this.mScrollListener);
      AdapterView.OnItemSelectedListener onItemSelectedListener = this.mItemSelectedListener;
      if (onItemSelectedListener != null)
        this.mDropDownList.setOnItemSelectedListener(onItemSelectedListener); 
      DropDownListView dropDownListView2 = this.mDropDownList;
      View view = this.mPromptView;
      i = j;
      DropDownListView dropDownListView1 = dropDownListView2;
      if (view != null) {
        linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(1);
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-1, 0, 1.0F);
        switch (this.mPromptPosition) {
          default:
            Log.e("ListPopupWindow", "Invalid hint position " + this.mPromptPosition);
            break;
          case 1:
            linearLayout.addView((View)dropDownListView2, (ViewGroup.LayoutParams)layoutParams2);
            linearLayout.addView(view);
            break;
          case 0:
            linearLayout.addView(view);
            linearLayout.addView((View)dropDownListView2, (ViewGroup.LayoutParams)layoutParams2);
            break;
        } 
        if (this.mDropDownWidth >= 0) {
          i = Integer.MIN_VALUE;
          j = this.mDropDownWidth;
        } else {
          i = 0;
          j = 0;
        } 
        view.measure(View.MeasureSpec.makeMeasureSpec(j, i), 0);
        LinearLayout.LayoutParams layoutParams1 = (LinearLayout.LayoutParams)view.getLayoutParams();
        b = view.getMeasuredHeight();
        j = layoutParams1.topMargin;
        i = layoutParams1.bottomMargin;
        i = b + j + i;
      } 
      this.mPopup.setContentView((View)linearLayout);
    } else {
      ViewGroup viewGroup = (ViewGroup)this.mPopup.getContentView();
      View view = this.mPromptView;
      if (view != null) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)view.getLayoutParams();
        i = view.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin;
      } 
    } 
    Drawable drawable = this.mPopup.getBackground();
    if (drawable != null) {
      drawable.getPadding(this.mTempRect);
      j = this.mTempRect.top + this.mTempRect.bottom;
      b = j;
      if (!this.mDropDownVerticalOffsetSet) {
        this.mDropDownVerticalOffset = -this.mTempRect.top;
        b = j;
      } 
    } else {
      this.mTempRect.setEmpty();
      b = 0;
    } 
    if (this.mPopup.getInputMethodMode() == 2)
      bool = true; 
    int k = getMaxAvailableHeight(getAnchorView(), this.mDropDownVerticalOffset, bool);
    if (this.mDropDownAlwaysVisible || this.mDropDownHeight == -1)
      return k + b; 
    j = this.mDropDownWidth;
    switch (j) {
      default:
        j = View.MeasureSpec.makeMeasureSpec(j, 1073741824);
        break;
      case -1:
        j = View.MeasureSpec.makeMeasureSpec((this.mContext.getResources().getDisplayMetrics()).widthPixels - this.mTempRect.left + this.mTempRect.right, 1073741824);
        break;
      case -2:
        j = View.MeasureSpec.makeMeasureSpec((this.mContext.getResources().getDisplayMetrics()).widthPixels - this.mTempRect.left + this.mTempRect.right, -2147483648);
        break;
    } 
    k = this.mDropDownList.measureHeightOfChildrenCompat(j, 0, -1, k - i, -1);
    j = i;
    if (k > 0)
      j = i + b + this.mDropDownList.getPaddingTop() + this.mDropDownList.getPaddingBottom(); 
    return k + j;
  }
  
  private int getMaxAvailableHeight(View paramView, int paramInt, boolean paramBoolean) {
    if (Build.VERSION.SDK_INT <= 23) {
      Method method = sGetMaxAvailableHeightMethod;
      if (method != null)
        try {
          return ((Integer)method.invoke(this.mPopup, new Object[] { paramView, Integer.valueOf(paramInt), Boolean.valueOf(paramBoolean) })).intValue();
        } catch (Exception exception) {
          Log.i("ListPopupWindow", "Could not call getMaxAvailableHeightMethod(View, int, boolean) on PopupWindow. Using the public version.");
        }  
      return this.mPopup.getMaxAvailableHeight(paramView, paramInt);
    } 
    return this.mPopup.getMaxAvailableHeight(paramView, paramInt, paramBoolean);
  }
  
  private static boolean isConfirmKey(int paramInt) {
    return (paramInt == 66 || paramInt == 23);
  }
  
  private void removePromptView() {
    View view = this.mPromptView;
    if (view != null) {
      ViewParent viewParent = view.getParent();
      if (viewParent instanceof ViewGroup)
        ((ViewGroup)viewParent).removeView(this.mPromptView); 
    } 
  }
  
  private void setPopupClipToScreenEnabled(boolean paramBoolean) {
    if (Build.VERSION.SDK_INT <= 28) {
      Method method = sSetClipToWindowEnabledMethod;
      if (method != null)
        try {
          method.invoke(this.mPopup, new Object[] { Boolean.valueOf(paramBoolean) });
        } catch (Exception exception) {
          Log.i("ListPopupWindow", "Could not call setClipToScreenEnabled() on PopupWindow. Oh well.");
        }  
    } else {
      this.mPopup.setIsClippedToScreen(paramBoolean);
    } 
  }
  
  public void clearListSelection() {
    DropDownListView dropDownListView = this.mDropDownList;
    if (dropDownListView != null) {
      dropDownListView.setListSelectionHidden(true);
      dropDownListView.requestLayout();
    } 
  }
  
  public View.OnTouchListener createDragToOpenListener(View paramView) {
    return new ForwardingListener(paramView) {
        final ListPopupWindow this$0;
        
        public ListPopupWindow getPopup() {
          return ListPopupWindow.this;
        }
      };
  }
  
  DropDownListView createDropDownListView(Context paramContext, boolean paramBoolean) {
    return new DropDownListView(paramContext, paramBoolean);
  }
  
  public void dismiss() {
    this.mPopup.dismiss();
    removePromptView();
    this.mPopup.setContentView(null);
    this.mDropDownList = null;
    this.mHandler.removeCallbacks(this.mResizePopupRunnable);
  }
  
  public View getAnchorView() {
    return this.mDropDownAnchorView;
  }
  
  public int getAnimationStyle() {
    return this.mPopup.getAnimationStyle();
  }
  
  public Drawable getBackground() {
    return this.mPopup.getBackground();
  }
  
  public Rect getEpicenterBounds() {
    Rect rect;
    if (this.mEpicenterBounds != null) {
      rect = new Rect(this.mEpicenterBounds);
    } else {
      rect = null;
    } 
    return rect;
  }
  
  public int getHeight() {
    return this.mDropDownHeight;
  }
  
  public int getHorizontalOffset() {
    return this.mDropDownHorizontalOffset;
  }
  
  public int getInputMethodMode() {
    return this.mPopup.getInputMethodMode();
  }
  
  public ListView getListView() {
    return this.mDropDownList;
  }
  
  public int getPromptPosition() {
    return this.mPromptPosition;
  }
  
  public Object getSelectedItem() {
    return !isShowing() ? null : this.mDropDownList.getSelectedItem();
  }
  
  public long getSelectedItemId() {
    return !isShowing() ? Long.MIN_VALUE : this.mDropDownList.getSelectedItemId();
  }
  
  public int getSelectedItemPosition() {
    return !isShowing() ? -1 : this.mDropDownList.getSelectedItemPosition();
  }
  
  public View getSelectedView() {
    return !isShowing() ? null : this.mDropDownList.getSelectedView();
  }
  
  public int getSoftInputMode() {
    return this.mPopup.getSoftInputMode();
  }
  
  public int getVerticalOffset() {
    return !this.mDropDownVerticalOffsetSet ? 0 : this.mDropDownVerticalOffset;
  }
  
  public int getWidth() {
    return this.mDropDownWidth;
  }
  
  public boolean isDropDownAlwaysVisible() {
    return this.mDropDownAlwaysVisible;
  }
  
  public boolean isInputMethodNotNeeded() {
    boolean bool;
    if (this.mPopup.getInputMethodMode() == 2) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isModal() {
    return this.mModal;
  }
  
  public boolean isShowing() {
    return this.mPopup.isShowing();
  }
  
  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
    if (isShowing() && paramInt != 62 && (this.mDropDownList.getSelectedItemPosition() >= 0 || !isConfirmKey(paramInt))) {
      int k = this.mDropDownList.getSelectedItemPosition();
      int m = this.mPopup.isAboveAnchor() ^ true;
      ListAdapter listAdapter = this.mAdapter;
      int j = Integer.MAX_VALUE;
      int i = Integer.MIN_VALUE;
      if (listAdapter != null) {
        boolean bool = listAdapter.areAllItemsEnabled();
        if (bool) {
          i = 0;
        } else {
          i = this.mDropDownList.lookForSelectablePosition(0, true);
        } 
        j = i;
        if (bool) {
          i = listAdapter.getCount() - 1;
        } else {
          i = this.mDropDownList.lookForSelectablePosition(listAdapter.getCount() - 1, false);
        } 
      } 
      if ((m != 0 && paramInt == 19 && k <= j) || (m == 0 && paramInt == 20 && k >= i)) {
        clearListSelection();
        this.mPopup.setInputMethodMode(1);
        show();
        return true;
      } 
      this.mDropDownList.setListSelectionHidden(false);
      if (this.mDropDownList.onKeyDown(paramInt, paramKeyEvent)) {
        this.mPopup.setInputMethodMode(2);
        this.mDropDownList.requestFocusFromTouch();
        show();
        switch (paramInt) {
          default:
            return false;
          case 19:
          case 20:
          case 23:
          case 66:
            break;
        } 
        return true;
      } 
      if (m != 0 && paramInt == 20)
        if (k == i)
          return true;  
      if (m == 0 && paramInt == 19 && k == j)
        return true; 
    } 
  }
  
  public boolean onKeyPreIme(int paramInt, KeyEvent paramKeyEvent) {
    if (paramInt == 4 && isShowing()) {
      KeyEvent.DispatcherState dispatcherState;
      View view = this.mDropDownAnchorView;
      if (paramKeyEvent.getAction() == 0 && paramKeyEvent.getRepeatCount() == 0) {
        dispatcherState = view.getKeyDispatcherState();
        if (dispatcherState != null)
          dispatcherState.startTracking(paramKeyEvent, this); 
        return true;
      } 
      if (paramKeyEvent.getAction() == 1) {
        dispatcherState = dispatcherState.getKeyDispatcherState();
        if (dispatcherState != null)
          dispatcherState.handleUpEvent(paramKeyEvent); 
        if (paramKeyEvent.isTracking() && !paramKeyEvent.isCanceled()) {
          dismiss();
          return true;
        } 
      } 
    } 
    return false;
  }
  
  public boolean onKeyUp(int paramInt, KeyEvent paramKeyEvent) {
    if (isShowing() && this.mDropDownList.getSelectedItemPosition() >= 0) {
      boolean bool = this.mDropDownList.onKeyUp(paramInt, paramKeyEvent);
      if (bool && isConfirmKey(paramInt))
        dismiss(); 
      return bool;
    } 
    return false;
  }
  
  public boolean performItemClick(int paramInt) {
    if (isShowing()) {
      if (this.mItemClickListener != null) {
        DropDownListView dropDownListView = this.mDropDownList;
        View view = dropDownListView.getChildAt(paramInt - dropDownListView.getFirstVisiblePosition());
        ListAdapter listAdapter = dropDownListView.getAdapter();
        this.mItemClickListener.onItemClick((AdapterView)dropDownListView, view, paramInt, listAdapter.getItemId(paramInt));
      } 
      return true;
    } 
    return false;
  }
  
  public void postShow() {
    this.mHandler.post(this.mShowDropDownRunnable);
  }
  
  public void setAdapter(ListAdapter paramListAdapter) {
    DataSetObserver dataSetObserver = this.mObserver;
    if (dataSetObserver == null) {
      this.mObserver = new PopupDataSetObserver();
    } else {
      ListAdapter listAdapter = this.mAdapter;
      if (listAdapter != null)
        listAdapter.unregisterDataSetObserver(dataSetObserver); 
    } 
    this.mAdapter = paramListAdapter;
    if (paramListAdapter != null)
      paramListAdapter.registerDataSetObserver(this.mObserver); 
    DropDownListView dropDownListView = this.mDropDownList;
    if (dropDownListView != null)
      dropDownListView.setAdapter(this.mAdapter); 
  }
  
  public void setAnchorView(View paramView) {
    this.mDropDownAnchorView = paramView;
  }
  
  public void setAnimationStyle(int paramInt) {
    this.mPopup.setAnimationStyle(paramInt);
  }
  
  public void setBackgroundDrawable(Drawable paramDrawable) {
    this.mPopup.setBackgroundDrawable(paramDrawable);
  }
  
  public void setContentWidth(int paramInt) {
    Drawable drawable = this.mPopup.getBackground();
    if (drawable != null) {
      drawable.getPadding(this.mTempRect);
      this.mDropDownWidth = this.mTempRect.left + this.mTempRect.right + paramInt;
    } else {
      setWidth(paramInt);
    } 
  }
  
  public void setDropDownAlwaysVisible(boolean paramBoolean) {
    this.mDropDownAlwaysVisible = paramBoolean;
  }
  
  public void setDropDownGravity(int paramInt) {
    this.mDropDownGravity = paramInt;
  }
  
  public void setEpicenterBounds(Rect paramRect) {
    if (paramRect != null) {
      paramRect = new Rect(paramRect);
    } else {
      paramRect = null;
    } 
    this.mEpicenterBounds = paramRect;
  }
  
  public void setForceIgnoreOutsideTouch(boolean paramBoolean) {
    this.mForceIgnoreOutsideTouch = paramBoolean;
  }
  
  public void setHeight(int paramInt) {
    if (paramInt >= 0 || -2 == paramInt || -1 == paramInt) {
      this.mDropDownHeight = paramInt;
      return;
    } 
    throw new IllegalArgumentException("Invalid height. Must be a positive value, MATCH_PARENT, or WRAP_CONTENT.");
  }
  
  public void setHorizontalOffset(int paramInt) {
    this.mDropDownHorizontalOffset = paramInt;
  }
  
  public void setInputMethodMode(int paramInt) {
    this.mPopup.setInputMethodMode(paramInt);
  }
  
  void setListItemExpandMax(int paramInt) {
    this.mListItemExpandMaximum = paramInt;
  }
  
  public void setListSelector(Drawable paramDrawable) {
    this.mDropDownListHighlight = paramDrawable;
  }
  
  public void setModal(boolean paramBoolean) {
    this.mModal = paramBoolean;
    this.mPopup.setFocusable(paramBoolean);
  }
  
  public void setOnDismissListener(PopupWindow.OnDismissListener paramOnDismissListener) {
    this.mPopup.setOnDismissListener(paramOnDismissListener);
  }
  
  public void setOnItemClickListener(AdapterView.OnItemClickListener paramOnItemClickListener) {
    this.mItemClickListener = paramOnItemClickListener;
  }
  
  public void setOnItemSelectedListener(AdapterView.OnItemSelectedListener paramOnItemSelectedListener) {
    this.mItemSelectedListener = paramOnItemSelectedListener;
  }
  
  public void setOverlapAnchor(boolean paramBoolean) {
    this.mOverlapAnchorSet = true;
    this.mOverlapAnchor = paramBoolean;
  }
  
  public void setPromptPosition(int paramInt) {
    this.mPromptPosition = paramInt;
  }
  
  public void setPromptView(View paramView) {
    boolean bool = isShowing();
    if (bool)
      removePromptView(); 
    this.mPromptView = paramView;
    if (bool)
      show(); 
  }
  
  public void setSelection(int paramInt) {
    DropDownListView dropDownListView = this.mDropDownList;
    if (isShowing() && dropDownListView != null) {
      dropDownListView.setListSelectionHidden(false);
      dropDownListView.setSelection(paramInt);
      if (dropDownListView.getChoiceMode() != 0)
        dropDownListView.setItemChecked(paramInt, true); 
    } 
  }
  
  public void setSoftInputMode(int paramInt) {
    this.mPopup.setSoftInputMode(paramInt);
  }
  
  public void setVerticalOffset(int paramInt) {
    this.mDropDownVerticalOffset = paramInt;
    this.mDropDownVerticalOffsetSet = true;
  }
  
  public void setWidth(int paramInt) {
    this.mDropDownWidth = paramInt;
  }
  
  public void setWindowLayoutType(int paramInt) {
    this.mDropDownWindowLayoutType = paramInt;
  }
  
  public void show() {
    int i = buildDropDown();
    boolean bool1 = isInputMethodNotNeeded();
    PopupWindowCompat.setWindowLayoutType(this.mPopup, this.mDropDownWindowLayoutType);
    boolean bool2 = this.mPopup.isShowing();
    boolean bool = true;
    if (bool2) {
      if (!ViewCompat.isAttachedToWindow(getAnchorView()))
        return; 
      int j = this.mDropDownWidth;
      if (j == -1) {
        j = -1;
      } else if (j == -2) {
        j = getAnchorView().getWidth();
      } else {
        j = this.mDropDownWidth;
      } 
      int k = this.mDropDownHeight;
      if (k == -1) {
        if (!bool1)
          i = -1; 
        if (bool1) {
          PopupWindow popupWindow = this.mPopup;
          if (this.mDropDownWidth == -1) {
            k = -1;
          } else {
            k = 0;
          } 
          popupWindow.setWidth(k);
          this.mPopup.setHeight(0);
        } else {
          PopupWindow popupWindow = this.mPopup;
          if (this.mDropDownWidth == -1) {
            k = -1;
          } else {
            k = 0;
          } 
          popupWindow.setWidth(k);
          this.mPopup.setHeight(-1);
        } 
      } else if (k != -2) {
        i = this.mDropDownHeight;
      } 
      PopupWindow popupWindow1 = this.mPopup;
      if (this.mForceIgnoreOutsideTouch || this.mDropDownAlwaysVisible)
        bool = false; 
      popupWindow1.setOutsideTouchable(bool);
      PopupWindow popupWindow2 = this.mPopup;
      View view = getAnchorView();
      int m = this.mDropDownHorizontalOffset;
      k = this.mDropDownVerticalOffset;
      if (j < 0)
        j = -1; 
      if (i < 0)
        i = -1; 
      popupWindow2.update(view, m, k, j, i);
    } else {
      int j = this.mDropDownWidth;
      if (j == -1) {
        j = -1;
      } else if (j == -2) {
        j = getAnchorView().getWidth();
      } else {
        j = this.mDropDownWidth;
      } 
      int k = this.mDropDownHeight;
      if (k == -1) {
        i = -1;
      } else if (k != -2) {
        i = this.mDropDownHeight;
      } 
      this.mPopup.setWidth(j);
      this.mPopup.setHeight(i);
      setPopupClipToScreenEnabled(true);
      PopupWindow popupWindow = this.mPopup;
      if (!this.mForceIgnoreOutsideTouch && !this.mDropDownAlwaysVisible) {
        bool = true;
      } else {
        bool = false;
      } 
      popupWindow.setOutsideTouchable(bool);
      this.mPopup.setTouchInterceptor(this.mTouchInterceptor);
      if (this.mOverlapAnchorSet)
        PopupWindowCompat.setOverlapAnchor(this.mPopup, this.mOverlapAnchor); 
      if (Build.VERSION.SDK_INT <= 28) {
        Method method = sSetEpicenterBoundsMethod;
        if (method != null)
          try {
            method.invoke(this.mPopup, new Object[] { this.mEpicenterBounds });
          } catch (Exception exception) {
            Log.e("ListPopupWindow", "Could not invoke setEpicenterBounds on PopupWindow", exception);
          }  
      } else {
        this.mPopup.setEpicenterBounds(this.mEpicenterBounds);
      } 
      PopupWindowCompat.showAsDropDown(this.mPopup, getAnchorView(), this.mDropDownHorizontalOffset, this.mDropDownVerticalOffset, this.mDropDownGravity);
      this.mDropDownList.setSelection(-1);
      if (!this.mModal || this.mDropDownList.isInTouchMode())
        clearListSelection(); 
      if (!this.mModal)
        this.mHandler.post(this.mHideSelector); 
    } 
  }
  
  private class ListSelectorHider implements Runnable {
    final ListPopupWindow this$0;
    
    public void run() {
      ListPopupWindow.this.clearListSelection();
    }
  }
  
  private class PopupDataSetObserver extends DataSetObserver {
    final ListPopupWindow this$0;
    
    public void onChanged() {
      if (ListPopupWindow.this.isShowing())
        ListPopupWindow.this.show(); 
    }
    
    public void onInvalidated() {
      ListPopupWindow.this.dismiss();
    }
  }
  
  private class PopupScrollListener implements AbsListView.OnScrollListener {
    final ListPopupWindow this$0;
    
    public void onScroll(AbsListView param1AbsListView, int param1Int1, int param1Int2, int param1Int3) {}
    
    public void onScrollStateChanged(AbsListView param1AbsListView, int param1Int) {
      if (param1Int == 1 && !ListPopupWindow.this.isInputMethodNotNeeded() && ListPopupWindow.this.mPopup.getContentView() != null) {
        ListPopupWindow.this.mHandler.removeCallbacks(ListPopupWindow.this.mResizePopupRunnable);
        ListPopupWindow.this.mResizePopupRunnable.run();
      } 
    }
  }
  
  private class PopupTouchInterceptor implements View.OnTouchListener {
    final ListPopupWindow this$0;
    
    public boolean onTouch(View param1View, MotionEvent param1MotionEvent) {
      int k = param1MotionEvent.getAction();
      int i = (int)param1MotionEvent.getX();
      int j = (int)param1MotionEvent.getY();
      if (k == 0 && ListPopupWindow.this.mPopup != null && ListPopupWindow.this.mPopup.isShowing() && i >= 0 && i < ListPopupWindow.this.mPopup.getWidth() && j >= 0 && j < ListPopupWindow.this.mPopup.getHeight()) {
        ListPopupWindow.this.mHandler.postDelayed(ListPopupWindow.this.mResizePopupRunnable, 250L);
      } else if (k == 1) {
        ListPopupWindow.this.mHandler.removeCallbacks(ListPopupWindow.this.mResizePopupRunnable);
      } 
      return false;
    }
  }
  
  private class ResizePopupRunnable implements Runnable {
    final ListPopupWindow this$0;
    
    public void run() {
      if (ListPopupWindow.this.mDropDownList != null && ViewCompat.isAttachedToWindow((View)ListPopupWindow.this.mDropDownList) && ListPopupWindow.this.mDropDownList.getCount() > ListPopupWindow.this.mDropDownList.getChildCount() && ListPopupWindow.this.mDropDownList.getChildCount() <= ListPopupWindow.this.mListItemExpandMaximum) {
        ListPopupWindow.this.mPopup.setInputMethodMode(2);
        ListPopupWindow.this.show();
      } 
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\appcompat\widget\ListPopupWindow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */