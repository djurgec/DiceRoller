package com.bumptech.glide.request.target;

import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import com.bumptech.glide.R;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.util.Preconditions;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Deprecated
public abstract class ViewTarget<T extends View, Z> extends BaseTarget<Z> {
  private static final String TAG = "ViewTarget";
  
  private static boolean isTagUsedAtLeastOnce;
  
  private static int tagId = R.id.glide_custom_view_target_tag;
  
  private View.OnAttachStateChangeListener attachStateListener;
  
  private boolean isAttachStateListenerAdded;
  
  private boolean isClearedByUs;
  
  private final SizeDeterminer sizeDeterminer;
  
  protected final T view;
  
  public ViewTarget(T paramT) {
    this.view = (T)Preconditions.checkNotNull(paramT);
    this.sizeDeterminer = new SizeDeterminer((View)paramT);
  }
  
  @Deprecated
  public ViewTarget(T paramT, boolean paramBoolean) {
    this(paramT);
    if (paramBoolean)
      waitForLayout(); 
  }
  
  private Object getTag() {
    return this.view.getTag(tagId);
  }
  
  private void maybeAddAttachStateListener() {
    View.OnAttachStateChangeListener onAttachStateChangeListener = this.attachStateListener;
    if (onAttachStateChangeListener == null || this.isAttachStateListenerAdded)
      return; 
    this.view.addOnAttachStateChangeListener(onAttachStateChangeListener);
    this.isAttachStateListenerAdded = true;
  }
  
  private void maybeRemoveAttachStateListener() {
    View.OnAttachStateChangeListener onAttachStateChangeListener = this.attachStateListener;
    if (onAttachStateChangeListener == null || !this.isAttachStateListenerAdded)
      return; 
    this.view.removeOnAttachStateChangeListener(onAttachStateChangeListener);
    this.isAttachStateListenerAdded = false;
  }
  
  private void setTag(Object paramObject) {
    isTagUsedAtLeastOnce = true;
    this.view.setTag(tagId, paramObject);
  }
  
  @Deprecated
  public static void setTagId(int paramInt) {
    if (!isTagUsedAtLeastOnce) {
      tagId = paramInt;
      return;
    } 
    throw new IllegalArgumentException("You cannot set the tag id more than once or change the tag id after the first request has been made");
  }
  
  public final ViewTarget<T, Z> clearOnDetach() {
    if (this.attachStateListener != null)
      return this; 
    this.attachStateListener = new View.OnAttachStateChangeListener() {
        final ViewTarget this$0;
        
        public void onViewAttachedToWindow(View param1View) {
          ViewTarget.this.resumeMyRequest();
        }
        
        public void onViewDetachedFromWindow(View param1View) {
          ViewTarget.this.pauseMyRequest();
        }
      };
    maybeAddAttachStateListener();
    return this;
  }
  
  public Request getRequest() {
    Object object = getTag();
    Request request = null;
    if (object != null)
      if (object instanceof Request) {
        request = (Request)object;
      } else {
        throw new IllegalArgumentException("You must not call setTag() on a view Glide is targeting");
      }  
    return request;
  }
  
  public void getSize(SizeReadyCallback paramSizeReadyCallback) {
    this.sizeDeterminer.getSize(paramSizeReadyCallback);
  }
  
  public T getView() {
    return this.view;
  }
  
  public void onLoadCleared(Drawable paramDrawable) {
    super.onLoadCleared(paramDrawable);
    this.sizeDeterminer.clearCallbacksAndListener();
    if (!this.isClearedByUs)
      maybeRemoveAttachStateListener(); 
  }
  
  public void onLoadStarted(Drawable paramDrawable) {
    super.onLoadStarted(paramDrawable);
    maybeAddAttachStateListener();
  }
  
  void pauseMyRequest() {
    Request request = getRequest();
    if (request != null) {
      this.isClearedByUs = true;
      request.clear();
      this.isClearedByUs = false;
    } 
  }
  
  public void removeCallback(SizeReadyCallback paramSizeReadyCallback) {
    this.sizeDeterminer.removeCallback(paramSizeReadyCallback);
  }
  
  void resumeMyRequest() {
    Request request = getRequest();
    if (request != null && request.isCleared())
      request.begin(); 
  }
  
  public void setRequest(Request paramRequest) {
    setTag(paramRequest);
  }
  
  public String toString() {
    return "Target for: " + this.view;
  }
  
  public final ViewTarget<T, Z> waitForLayout() {
    this.sizeDeterminer.waitForLayout = true;
    return this;
  }
  
  static final class SizeDeterminer {
    private static final int PENDING_SIZE = 0;
    
    static Integer maxDisplayLength;
    
    private final List<SizeReadyCallback> cbs = new ArrayList<>();
    
    private SizeDeterminerLayoutListener layoutListener;
    
    private final View view;
    
    boolean waitForLayout;
    
    SizeDeterminer(View param1View) {
      this.view = param1View;
    }
    
    private static int getMaxDisplayLength(Context param1Context) {
      if (maxDisplayLength == null) {
        Display display = ((WindowManager)Preconditions.checkNotNull(param1Context.getSystemService("window"))).getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        maxDisplayLength = Integer.valueOf(Math.max(point.x, point.y));
      } 
      return maxDisplayLength.intValue();
    }
    
    private int getTargetDimen(int param1Int1, int param1Int2, int param1Int3) {
      int i = param1Int2 - param1Int3;
      if (i > 0)
        return i; 
      if (this.waitForLayout && this.view.isLayoutRequested())
        return 0; 
      param1Int1 -= param1Int3;
      if (param1Int1 > 0)
        return param1Int1; 
      if (!this.view.isLayoutRequested() && param1Int2 == -2) {
        if (Log.isLoggable("ViewTarget", 4))
          Log.i("ViewTarget", "Glide treats LayoutParams.WRAP_CONTENT as a request for an image the size of this device's screen dimensions. If you want to load the original image and are ok with the corresponding memory cost and OOMs (depending on the input size), use override(Target.SIZE_ORIGINAL). Otherwise, use LayoutParams.MATCH_PARENT, set layout_width and layout_height to fixed dimension, or use .override() with fixed dimensions."); 
        return getMaxDisplayLength(this.view.getContext());
      } 
      return 0;
    }
    
    private int getTargetHeight() {
      boolean bool;
      int i = this.view.getPaddingTop();
      int j = this.view.getPaddingBottom();
      ViewGroup.LayoutParams layoutParams = this.view.getLayoutParams();
      if (layoutParams != null) {
        bool = layoutParams.height;
      } else {
        bool = false;
      } 
      return getTargetDimen(this.view.getHeight(), bool, i + j);
    }
    
    private int getTargetWidth() {
      boolean bool;
      int i = this.view.getPaddingLeft();
      int j = this.view.getPaddingRight();
      ViewGroup.LayoutParams layoutParams = this.view.getLayoutParams();
      if (layoutParams != null) {
        bool = layoutParams.width;
      } else {
        bool = false;
      } 
      return getTargetDimen(this.view.getWidth(), bool, i + j);
    }
    
    private boolean isDimensionValid(int param1Int) {
      return (param1Int > 0 || param1Int == Integer.MIN_VALUE);
    }
    
    private boolean isViewStateAndSizeValid(int param1Int1, int param1Int2) {
      boolean bool;
      if (isDimensionValid(param1Int1) && isDimensionValid(param1Int2)) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    private void notifyCbs(int param1Int1, int param1Int2) {
      Iterator<?> iterator = (new ArrayList(this.cbs)).iterator();
      while (iterator.hasNext())
        ((SizeReadyCallback)iterator.next()).onSizeReady(param1Int1, param1Int2); 
    }
    
    void checkCurrentDimens() {
      if (this.cbs.isEmpty())
        return; 
      int j = getTargetWidth();
      int i = getTargetHeight();
      if (!isViewStateAndSizeValid(j, i))
        return; 
      notifyCbs(j, i);
      clearCallbacksAndListener();
    }
    
    void clearCallbacksAndListener() {
      ViewTreeObserver viewTreeObserver = this.view.getViewTreeObserver();
      if (viewTreeObserver.isAlive())
        viewTreeObserver.removeOnPreDrawListener(this.layoutListener); 
      this.layoutListener = null;
      this.cbs.clear();
    }
    
    void getSize(SizeReadyCallback param1SizeReadyCallback) {
      int i = getTargetWidth();
      int j = getTargetHeight();
      if (isViewStateAndSizeValid(i, j)) {
        param1SizeReadyCallback.onSizeReady(i, j);
        return;
      } 
      if (!this.cbs.contains(param1SizeReadyCallback))
        this.cbs.add(param1SizeReadyCallback); 
      if (this.layoutListener == null) {
        ViewTreeObserver viewTreeObserver = this.view.getViewTreeObserver();
        SizeDeterminerLayoutListener sizeDeterminerLayoutListener = new SizeDeterminerLayoutListener(this);
        this.layoutListener = sizeDeterminerLayoutListener;
        viewTreeObserver.addOnPreDrawListener(sizeDeterminerLayoutListener);
      } 
    }
    
    void removeCallback(SizeReadyCallback param1SizeReadyCallback) {
      this.cbs.remove(param1SizeReadyCallback);
    }
    
    private static final class SizeDeterminerLayoutListener implements ViewTreeObserver.OnPreDrawListener {
      private final WeakReference<ViewTarget.SizeDeterminer> sizeDeterminerRef;
      
      SizeDeterminerLayoutListener(ViewTarget.SizeDeterminer param2SizeDeterminer) {
        this.sizeDeterminerRef = new WeakReference<>(param2SizeDeterminer);
      }
      
      public boolean onPreDraw() {
        if (Log.isLoggable("ViewTarget", 2))
          Log.v("ViewTarget", "OnGlobalLayoutListener called attachStateListener=" + this); 
        ViewTarget.SizeDeterminer sizeDeterminer = this.sizeDeterminerRef.get();
        if (sizeDeterminer != null)
          sizeDeterminer.checkCurrentDimens(); 
        return true;
      }
    }
  }
  
  private static final class SizeDeterminerLayoutListener implements ViewTreeObserver.OnPreDrawListener {
    private final WeakReference<ViewTarget.SizeDeterminer> sizeDeterminerRef;
    
    SizeDeterminerLayoutListener(ViewTarget.SizeDeterminer param1SizeDeterminer) {
      this.sizeDeterminerRef = new WeakReference<>(param1SizeDeterminer);
    }
    
    public boolean onPreDraw() {
      if (Log.isLoggable("ViewTarget", 2))
        Log.v("ViewTarget", "OnGlobalLayoutListener called attachStateListener=" + this); 
      ViewTarget.SizeDeterminer sizeDeterminer = this.sizeDeterminerRef.get();
      if (sizeDeterminer != null)
        sizeDeterminer.checkCurrentDimens(); 
      return true;
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\request\target\ViewTarget.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */