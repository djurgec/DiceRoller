package androidx.transition;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import androidx.core.view.ViewCompat;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

class ViewOverlayApi14 implements ViewOverlayImpl {
  protected OverlayViewGroup mOverlayViewGroup;
  
  ViewOverlayApi14(Context paramContext, ViewGroup paramViewGroup, View paramView) {
    this.mOverlayViewGroup = new OverlayViewGroup(paramContext, paramViewGroup, paramView, this);
  }
  
  static ViewOverlayApi14 createFrom(View paramView) {
    ViewGroup viewGroup = getContentView(paramView);
    if (viewGroup != null) {
      int i = viewGroup.getChildCount();
      for (byte b = 0; b < i; b++) {
        View view = viewGroup.getChildAt(b);
        if (view instanceof OverlayViewGroup)
          return ((OverlayViewGroup)view).mViewOverlay; 
      } 
      return new ViewGroupOverlayApi14(viewGroup.getContext(), viewGroup, paramView);
    } 
    return null;
  }
  
  static ViewGroup getContentView(View paramView) {
    while (paramView != null) {
      if (paramView.getId() == 16908290 && paramView instanceof ViewGroup)
        return (ViewGroup)paramView; 
      if (paramView.getParent() instanceof ViewGroup)
        ViewGroup viewGroup = (ViewGroup)paramView.getParent(); 
    } 
    return null;
  }
  
  public void add(Drawable paramDrawable) {
    this.mOverlayViewGroup.add(paramDrawable);
  }
  
  public void remove(Drawable paramDrawable) {
    this.mOverlayViewGroup.remove(paramDrawable);
  }
  
  static class OverlayViewGroup extends ViewGroup {
    static Method sInvalidateChildInParentFastMethod;
    
    private boolean mDisposed;
    
    ArrayList<Drawable> mDrawables = null;
    
    ViewGroup mHostView;
    
    View mRequestingView;
    
    ViewOverlayApi14 mViewOverlay;
    
    static {
      try {
        sInvalidateChildInParentFastMethod = ViewGroup.class.getDeclaredMethod("invalidateChildInParentFast", new Class[] { int.class, int.class, Rect.class });
      } catch (NoSuchMethodException noSuchMethodException) {}
    }
    
    OverlayViewGroup(Context param1Context, ViewGroup param1ViewGroup, View param1View, ViewOverlayApi14 param1ViewOverlayApi14) {
      super(param1Context);
      this.mHostView = param1ViewGroup;
      this.mRequestingView = param1View;
      setRight(param1ViewGroup.getWidth());
      setBottom(param1ViewGroup.getHeight());
      param1ViewGroup.addView((View)this);
      this.mViewOverlay = param1ViewOverlayApi14;
    }
    
    private void assertNotDisposed() {
      if (!this.mDisposed)
        return; 
      throw new IllegalStateException("This overlay was disposed already. Please use a new one via ViewGroupUtils.getOverlay()");
    }
    
    private void disposeIfEmpty() {
      if (getChildCount() == 0) {
        ArrayList<Drawable> arrayList = this.mDrawables;
        if (arrayList == null || arrayList.size() == 0) {
          this.mDisposed = true;
          this.mHostView.removeView((View)this);
        } 
      } 
    }
    
    private void getOffset(int[] param1ArrayOfint) {
      int[] arrayOfInt1 = new int[2];
      int[] arrayOfInt2 = new int[2];
      this.mHostView.getLocationOnScreen(arrayOfInt1);
      this.mRequestingView.getLocationOnScreen(arrayOfInt2);
      param1ArrayOfint[0] = arrayOfInt2[0] - arrayOfInt1[0];
      param1ArrayOfint[1] = arrayOfInt2[1] - arrayOfInt1[1];
    }
    
    public void add(Drawable param1Drawable) {
      assertNotDisposed();
      if (this.mDrawables == null)
        this.mDrawables = new ArrayList<>(); 
      if (!this.mDrawables.contains(param1Drawable)) {
        this.mDrawables.add(param1Drawable);
        invalidate(param1Drawable.getBounds());
        param1Drawable.setCallback((Drawable.Callback)this);
      } 
    }
    
    public void add(View param1View) {
      assertNotDisposed();
      if (param1View.getParent() instanceof ViewGroup) {
        ViewGroup viewGroup = (ViewGroup)param1View.getParent();
        if (viewGroup != this.mHostView && viewGroup.getParent() != null && ViewCompat.isAttachedToWindow((View)viewGroup)) {
          int[] arrayOfInt1 = new int[2];
          int[] arrayOfInt2 = new int[2];
          viewGroup.getLocationOnScreen(arrayOfInt1);
          this.mHostView.getLocationOnScreen(arrayOfInt2);
          ViewCompat.offsetLeftAndRight(param1View, arrayOfInt1[0] - arrayOfInt2[0]);
          ViewCompat.offsetTopAndBottom(param1View, arrayOfInt1[1] - arrayOfInt2[1]);
        } 
        viewGroup.removeView(param1View);
        if (param1View.getParent() != null)
          viewGroup.removeView(param1View); 
      } 
      addView(param1View);
    }
    
    protected void dispatchDraw(Canvas param1Canvas) {
      int[] arrayOfInt2 = new int[2];
      int[] arrayOfInt1 = new int[2];
      this.mHostView.getLocationOnScreen(arrayOfInt2);
      this.mRequestingView.getLocationOnScreen(arrayOfInt1);
      int i = 0;
      param1Canvas.translate((arrayOfInt1[0] - arrayOfInt2[0]), (arrayOfInt1[1] - arrayOfInt2[1]));
      param1Canvas.clipRect(new Rect(0, 0, this.mRequestingView.getWidth(), this.mRequestingView.getHeight()));
      super.dispatchDraw(param1Canvas);
      ArrayList<Drawable> arrayList = this.mDrawables;
      if (arrayList != null)
        i = arrayList.size(); 
      for (byte b = 0; b < i; b++)
        ((Drawable)this.mDrawables.get(b)).draw(param1Canvas); 
    }
    
    public boolean dispatchTouchEvent(MotionEvent param1MotionEvent) {
      return false;
    }
    
    public ViewParent invalidateChildInParent(int[] param1ArrayOfint, Rect param1Rect) {
      if (this.mHostView != null) {
        param1Rect.offset(param1ArrayOfint[0], param1ArrayOfint[1]);
        if (this.mHostView instanceof ViewGroup) {
          param1ArrayOfint[0] = 0;
          param1ArrayOfint[1] = 0;
          int[] arrayOfInt = new int[2];
          getOffset(arrayOfInt);
          param1Rect.offset(arrayOfInt[0], arrayOfInt[1]);
          return super.invalidateChildInParent(param1ArrayOfint, param1Rect);
        } 
        invalidate(param1Rect);
      } 
      return null;
    }
    
    protected ViewParent invalidateChildInParentFast(int param1Int1, int param1Int2, Rect param1Rect) {
      if (this.mHostView instanceof ViewGroup && sInvalidateChildInParentFastMethod != null)
        try {
          getOffset(new int[2]);
          sInvalidateChildInParentFastMethod.invoke(this.mHostView, new Object[] { Integer.valueOf(param1Int1), Integer.valueOf(param1Int2), param1Rect });
        } catch (IllegalAccessException illegalAccessException) {
          illegalAccessException.printStackTrace();
        } catch (InvocationTargetException invocationTargetException) {
          invocationTargetException.printStackTrace();
        }  
      return null;
    }
    
    public void invalidateDrawable(Drawable param1Drawable) {
      invalidate(param1Drawable.getBounds());
    }
    
    protected void onLayout(boolean param1Boolean, int param1Int1, int param1Int2, int param1Int3, int param1Int4) {}
    
    public void remove(Drawable param1Drawable) {
      ArrayList<Drawable> arrayList = this.mDrawables;
      if (arrayList != null) {
        arrayList.remove(param1Drawable);
        invalidate(param1Drawable.getBounds());
        param1Drawable.setCallback(null);
        disposeIfEmpty();
      } 
    }
    
    public void remove(View param1View) {
      removeView(param1View);
      disposeIfEmpty();
    }
    
    protected boolean verifyDrawable(Drawable param1Drawable) {
      if (!super.verifyDrawable(param1Drawable)) {
        ArrayList<Drawable> arrayList = this.mDrawables;
        return (arrayList != null && arrayList.contains(param1Drawable));
      } 
      return true;
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\transition\ViewOverlayApi14.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */