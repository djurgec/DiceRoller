package androidx.appcompat.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import androidx.appcompat.R;
import androidx.appcompat.graphics.drawable.DrawableWrapper;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.core.widget.ListViewAutoScrollHelper;
import java.lang.reflect.Field;

class DropDownListView extends ListView {
  public static final int INVALID_POSITION = -1;
  
  public static final int NO_POSITION = -1;
  
  private ViewPropertyAnimatorCompat mClickAnimation;
  
  private boolean mDrawsInPressedState;
  
  private boolean mHijackFocus;
  
  private Field mIsChildViewEnabled;
  
  private boolean mListSelectionHidden;
  
  private int mMotionPosition;
  
  ResolveHoverRunnable mResolveHoverRunnable;
  
  private ListViewAutoScrollHelper mScrollHelper;
  
  private int mSelectionBottomPadding = 0;
  
  private int mSelectionLeftPadding = 0;
  
  private int mSelectionRightPadding = 0;
  
  private int mSelectionTopPadding = 0;
  
  private GateKeeperDrawable mSelector;
  
  private final Rect mSelectorRect = new Rect();
  
  DropDownListView(Context paramContext, boolean paramBoolean) {
    super(paramContext, null, R.attr.dropDownListViewStyle);
    this.mHijackFocus = paramBoolean;
    setCacheColorHint(0);
    try {
      Field field = AbsListView.class.getDeclaredField("mIsChildViewEnabled");
      this.mIsChildViewEnabled = field;
      field.setAccessible(true);
    } catch (NoSuchFieldException noSuchFieldException) {
      noSuchFieldException.printStackTrace();
    } 
  }
  
  private void clearPressedItem() {
    this.mDrawsInPressedState = false;
    setPressed(false);
    drawableStateChanged();
    View view = getChildAt(this.mMotionPosition - getFirstVisiblePosition());
    if (view != null)
      view.setPressed(false); 
    ViewPropertyAnimatorCompat viewPropertyAnimatorCompat = this.mClickAnimation;
    if (viewPropertyAnimatorCompat != null) {
      viewPropertyAnimatorCompat.cancel();
      this.mClickAnimation = null;
    } 
  }
  
  private void clickPressedItem(View paramView, int paramInt) {
    performItemClick(paramView, paramInt, getItemIdAtPosition(paramInt));
  }
  
  private void drawSelectorCompat(Canvas paramCanvas) {
    if (!this.mSelectorRect.isEmpty()) {
      Drawable drawable = getSelector();
      if (drawable != null) {
        drawable.setBounds(this.mSelectorRect);
        drawable.draw(paramCanvas);
      } 
    } 
  }
  
  private void positionSelectorCompat(int paramInt, View paramView) {
    Rect rect = this.mSelectorRect;
    rect.set(paramView.getLeft(), paramView.getTop(), paramView.getRight(), paramView.getBottom());
    rect.left -= this.mSelectionLeftPadding;
    rect.top -= this.mSelectionTopPadding;
    rect.right += this.mSelectionRightPadding;
    rect.bottom += this.mSelectionBottomPadding;
    try {
      boolean bool = this.mIsChildViewEnabled.getBoolean(this);
      if (paramView.isEnabled() != bool) {
        Field field = this.mIsChildViewEnabled;
        if (!bool) {
          bool = true;
        } else {
          bool = false;
        } 
        field.set(this, Boolean.valueOf(bool));
        if (paramInt != -1)
          refreshDrawableState(); 
      } 
    } catch (IllegalAccessException illegalAccessException) {
      illegalAccessException.printStackTrace();
    } 
  }
  
  private void positionSelectorLikeFocusCompat(int paramInt, View paramView) {
    boolean bool1;
    Drawable drawable = getSelector();
    boolean bool2 = true;
    if (drawable != null && paramInt != -1) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    if (bool1)
      drawable.setVisible(false, false); 
    positionSelectorCompat(paramInt, paramView);
    if (bool1) {
      Rect rect = this.mSelectorRect;
      float f2 = rect.exactCenterX();
      float f1 = rect.exactCenterY();
      if (getVisibility() != 0)
        bool2 = false; 
      drawable.setVisible(bool2, false);
      DrawableCompat.setHotspot(drawable, f2, f1);
    } 
  }
  
  private void positionSelectorLikeTouchCompat(int paramInt, View paramView, float paramFloat1, float paramFloat2) {
    positionSelectorLikeFocusCompat(paramInt, paramView);
    Drawable drawable = getSelector();
    if (drawable != null && paramInt != -1)
      DrawableCompat.setHotspot(drawable, paramFloat1, paramFloat2); 
  }
  
  private void setPressedItem(View paramView, int paramInt, float paramFloat1, float paramFloat2) {
    this.mDrawsInPressedState = true;
    if (Build.VERSION.SDK_INT >= 21)
      drawableHotspotChanged(paramFloat1, paramFloat2); 
    if (!isPressed())
      setPressed(true); 
    layoutChildren();
    int i = this.mMotionPosition;
    if (i != -1) {
      View view = getChildAt(i - getFirstVisiblePosition());
      if (view != null && view != paramView && view.isPressed())
        view.setPressed(false); 
    } 
    this.mMotionPosition = paramInt;
    float f2 = paramView.getLeft();
    float f1 = paramView.getTop();
    if (Build.VERSION.SDK_INT >= 21)
      paramView.drawableHotspotChanged(paramFloat1 - f2, paramFloat2 - f1); 
    if (!paramView.isPressed())
      paramView.setPressed(true); 
    positionSelectorLikeTouchCompat(paramInt, paramView, paramFloat1, paramFloat2);
    setSelectorEnabled(false);
    refreshDrawableState();
  }
  
  private void setSelectorEnabled(boolean paramBoolean) {
    GateKeeperDrawable gateKeeperDrawable = this.mSelector;
    if (gateKeeperDrawable != null)
      gateKeeperDrawable.setEnabled(paramBoolean); 
  }
  
  private boolean touchModeDrawsInPressedStateCompat() {
    return this.mDrawsInPressedState;
  }
  
  private void updateSelectorStateCompat() {
    Drawable drawable = getSelector();
    if (drawable != null && touchModeDrawsInPressedStateCompat() && isPressed())
      drawable.setState(getDrawableState()); 
  }
  
  protected void dispatchDraw(Canvas paramCanvas) {
    drawSelectorCompat(paramCanvas);
    super.dispatchDraw(paramCanvas);
  }
  
  protected void drawableStateChanged() {
    if (this.mResolveHoverRunnable != null)
      return; 
    super.drawableStateChanged();
    setSelectorEnabled(true);
    updateSelectorStateCompat();
  }
  
  public boolean hasFocus() {
    return (this.mHijackFocus || super.hasFocus());
  }
  
  public boolean hasWindowFocus() {
    return (this.mHijackFocus || super.hasWindowFocus());
  }
  
  public boolean isFocused() {
    return (this.mHijackFocus || super.isFocused());
  }
  
  public boolean isInTouchMode() {
    boolean bool;
    if ((this.mHijackFocus && this.mListSelectionHidden) || super.isInTouchMode()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public int lookForSelectablePosition(int paramInt, boolean paramBoolean) {
    ListAdapter listAdapter = getAdapter();
    if (listAdapter == null || isInTouchMode())
      return -1; 
    int i = listAdapter.getCount();
    if (!getAdapter().areAllItemsEnabled()) {
      int j;
      if (paramBoolean) {
        paramInt = Math.max(0, paramInt);
        while (true) {
          j = paramInt;
          if (paramInt < i) {
            j = paramInt;
            if (!listAdapter.isEnabled(paramInt)) {
              paramInt++;
              continue;
            } 
          } 
          break;
        } 
      } else {
        paramInt = Math.min(paramInt, i - 1);
        while (true) {
          j = paramInt;
          if (paramInt >= 0) {
            j = paramInt;
            if (!listAdapter.isEnabled(paramInt)) {
              paramInt--;
              continue;
            } 
          } 
          break;
        } 
      } 
      return (j < 0 || j >= i) ? -1 : j;
    } 
    return (paramInt < 0 || paramInt >= i) ? -1 : paramInt;
  }
  
  public int measureHeightOfChildrenCompat(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
    int k = getListPaddingTop();
    int j = getListPaddingBottom();
    int i = getDividerHeight();
    Drawable drawable = getDivider();
    ListAdapter listAdapter = getAdapter();
    if (listAdapter == null)
      return k + j; 
    paramInt3 = k + j;
    if (i <= 0 || drawable == null)
      i = 0; 
    paramInt2 = 0;
    drawable = null;
    int m = 0;
    int n = listAdapter.getCount();
    byte b = 0;
    while (b < n) {
      int i2 = listAdapter.getItemViewType(b);
      int i1 = m;
      if (i2 != m) {
        drawable = null;
        i1 = i2;
      } 
      View view2 = listAdapter.getView(b, (View)drawable, (ViewGroup)this);
      ViewGroup.LayoutParams layoutParams = view2.getLayoutParams();
      if (layoutParams == null) {
        layoutParams = generateDefaultLayoutParams();
        view2.setLayoutParams(layoutParams);
      } 
      if (layoutParams.height > 0) {
        m = View.MeasureSpec.makeMeasureSpec(layoutParams.height, 1073741824);
      } else {
        m = View.MeasureSpec.makeMeasureSpec(0, 0);
      } 
      view2.measure(paramInt1, m);
      view2.forceLayout();
      m = paramInt3;
      if (b > 0)
        m = paramInt3 + i; 
      paramInt3 = m + view2.getMeasuredHeight();
      if (paramInt3 >= paramInt4) {
        if (paramInt5 >= 0 && b > paramInt5 && paramInt2 > 0 && paramInt3 != paramInt4) {
          paramInt1 = paramInt2;
        } else {
          paramInt1 = paramInt4;
        } 
        return paramInt1;
      } 
      m = paramInt2;
      if (paramInt5 >= 0) {
        m = paramInt2;
        if (b >= paramInt5)
          m = paramInt3; 
      } 
      b++;
      paramInt2 = m;
      View view1 = view2;
      m = i1;
    } 
    return paramInt3;
  }
  
  protected void onDetachedFromWindow() {
    this.mResolveHoverRunnable = null;
    super.onDetachedFromWindow();
  }
  
  public boolean onForwardedEvent(MotionEvent paramMotionEvent, int paramInt) {
    int j;
    int k;
    View view;
    boolean bool3 = true;
    boolean bool2 = true;
    boolean bool1 = false;
    int i = paramMotionEvent.getActionMasked();
    switch (i) {
      default:
        bool2 = bool3;
        paramInt = bool1;
        break;
      case 3:
        bool2 = false;
        paramInt = bool1;
        break;
      case 1:
        bool2 = false;
      case 2:
        j = paramMotionEvent.findPointerIndex(paramInt);
        if (j < 0) {
          bool2 = false;
          paramInt = bool1;
          break;
        } 
        paramInt = (int)paramMotionEvent.getX(j);
        k = (int)paramMotionEvent.getY(j);
        j = pointToPosition(paramInt, k);
        if (j == -1) {
          paramInt = 1;
          break;
        } 
        view = getChildAt(j - getFirstVisiblePosition());
        setPressedItem(view, j, paramInt, k);
        bool3 = true;
        bool2 = bool3;
        paramInt = bool1;
        if (i == 1) {
          clickPressedItem(view, j);
          paramInt = bool1;
          bool2 = bool3;
        } 
        break;
    } 
    if (!bool2 || paramInt != 0)
      clearPressedItem(); 
    if (bool2) {
      if (this.mScrollHelper == null)
        this.mScrollHelper = new ListViewAutoScrollHelper(this); 
      this.mScrollHelper.setEnabled(true);
      this.mScrollHelper.onTouch((View)this, paramMotionEvent);
    } else {
      ListViewAutoScrollHelper listViewAutoScrollHelper = this.mScrollHelper;
      if (listViewAutoScrollHelper != null)
        listViewAutoScrollHelper.setEnabled(false); 
    } 
    return bool2;
  }
  
  public boolean onHoverEvent(MotionEvent paramMotionEvent) {
    if (Build.VERSION.SDK_INT < 26)
      return super.onHoverEvent(paramMotionEvent); 
    int i = paramMotionEvent.getActionMasked();
    if (i == 10 && this.mResolveHoverRunnable == null) {
      ResolveHoverRunnable resolveHoverRunnable = new ResolveHoverRunnable();
      this.mResolveHoverRunnable = resolveHoverRunnable;
      resolveHoverRunnable.post();
    } 
    boolean bool = super.onHoverEvent(paramMotionEvent);
    if (i == 9 || i == 7) {
      i = pointToPosition((int)paramMotionEvent.getX(), (int)paramMotionEvent.getY());
      if (i != -1 && i != getSelectedItemPosition()) {
        View view = getChildAt(i - getFirstVisiblePosition());
        if (view.isEnabled())
          setSelectionFromTop(i, view.getTop() - getTop()); 
        updateSelectorStateCompat();
      } 
      return bool;
    } 
    setSelection(-1);
    return bool;
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    switch (paramMotionEvent.getAction()) {
      case 0:
        this.mMotionPosition = pointToPosition((int)paramMotionEvent.getX(), (int)paramMotionEvent.getY());
        break;
    } 
    ResolveHoverRunnable resolveHoverRunnable = this.mResolveHoverRunnable;
    if (resolveHoverRunnable != null)
      resolveHoverRunnable.cancel(); 
    return super.onTouchEvent(paramMotionEvent);
  }
  
  void setListSelectionHidden(boolean paramBoolean) {
    this.mListSelectionHidden = paramBoolean;
  }
  
  public void setSelector(Drawable paramDrawable) {
    GateKeeperDrawable gateKeeperDrawable;
    if (paramDrawable != null) {
      gateKeeperDrawable = new GateKeeperDrawable(paramDrawable);
    } else {
      gateKeeperDrawable = null;
    } 
    this.mSelector = gateKeeperDrawable;
    super.setSelector((Drawable)gateKeeperDrawable);
    Rect rect = new Rect();
    if (paramDrawable != null)
      paramDrawable.getPadding(rect); 
    this.mSelectionLeftPadding = rect.left;
    this.mSelectionTopPadding = rect.top;
    this.mSelectionRightPadding = rect.right;
    this.mSelectionBottomPadding = rect.bottom;
  }
  
  private static class GateKeeperDrawable extends DrawableWrapper {
    private boolean mEnabled = true;
    
    GateKeeperDrawable(Drawable param1Drawable) {
      super(param1Drawable);
    }
    
    public void draw(Canvas param1Canvas) {
      if (this.mEnabled)
        super.draw(param1Canvas); 
    }
    
    void setEnabled(boolean param1Boolean) {
      this.mEnabled = param1Boolean;
    }
    
    public void setHotspot(float param1Float1, float param1Float2) {
      if (this.mEnabled)
        super.setHotspot(param1Float1, param1Float2); 
    }
    
    public void setHotspotBounds(int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
      if (this.mEnabled)
        super.setHotspotBounds(param1Int1, param1Int2, param1Int3, param1Int4); 
    }
    
    public boolean setState(int[] param1ArrayOfint) {
      return this.mEnabled ? super.setState(param1ArrayOfint) : false;
    }
    
    public boolean setVisible(boolean param1Boolean1, boolean param1Boolean2) {
      return this.mEnabled ? super.setVisible(param1Boolean1, param1Boolean2) : false;
    }
  }
  
  private class ResolveHoverRunnable implements Runnable {
    final DropDownListView this$0;
    
    public void cancel() {
      DropDownListView.this.mResolveHoverRunnable = null;
      DropDownListView.this.removeCallbacks(this);
    }
    
    public void post() {
      DropDownListView.this.post(this);
    }
    
    public void run() {
      DropDownListView.this.mResolveHoverRunnable = null;
      DropDownListView.this.drawableStateChanged();
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\appcompat\widget\DropDownListView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */