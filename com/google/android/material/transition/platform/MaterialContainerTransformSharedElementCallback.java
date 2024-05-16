package com.google.android.material.transition.platform;

import android.app.Activity;
import android.app.SharedElementCallback;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Parcelable;
import android.transition.Transition;
import android.view.View;
import android.view.Window;
import androidx.core.graphics.BlendModeColorFilterCompat;
import androidx.core.graphics.BlendModeCompat;
import com.google.android.material.R;
import com.google.android.material.internal.ContextUtils;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.android.material.shape.Shapeable;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;

public class MaterialContainerTransformSharedElementCallback extends SharedElementCallback {
  private static WeakReference<View> capturedSharedElement;
  
  private boolean entering = true;
  
  private Rect returnEndBounds;
  
  private ShapeProvider shapeProvider = new ShapeableViewShapeProvider();
  
  private boolean sharedElementReenterTransitionEnabled = false;
  
  private boolean transparentWindowBackgroundEnabled = true;
  
  private static void removeWindowBackground(Window paramWindow) {
    paramWindow.getDecorView().getBackground().mutate().setColorFilter(BlendModeColorFilterCompat.createBlendModeColorFilterCompat(0, BlendModeCompat.CLEAR));
  }
  
  private static void restoreWindowBackground(Window paramWindow) {
    paramWindow.getDecorView().getBackground().mutate().clearColorFilter();
  }
  
  private void setUpEnterTransform(final Window window) {
    Transition transition = window.getSharedElementEnterTransition();
    if (transition instanceof MaterialContainerTransform) {
      transition = transition;
      if (!this.sharedElementReenterTransitionEnabled)
        window.setSharedElementReenterTransition(null); 
      if (this.transparentWindowBackgroundEnabled) {
        updateBackgroundFadeDuration(window, (MaterialContainerTransform)transition);
        transition.addListener(new TransitionListenerAdapter() {
              final MaterialContainerTransformSharedElementCallback this$0;
              
              final Window val$window;
              
              public void onTransitionEnd(Transition param1Transition) {
                MaterialContainerTransformSharedElementCallback.restoreWindowBackground(window);
              }
              
              public void onTransitionStart(Transition param1Transition) {
                MaterialContainerTransformSharedElementCallback.removeWindowBackground(window);
              }
            });
      } 
    } 
  }
  
  private void setUpReturnTransform(final Activity activity, final Window window) {
    Transition transition = window.getSharedElementReturnTransition();
    if (transition instanceof MaterialContainerTransform) {
      transition = transition;
      transition.setHoldAtEndEnabled(true);
      transition.addListener(new TransitionListenerAdapter() {
            final MaterialContainerTransformSharedElementCallback this$0;
            
            final Activity val$activity;
            
            public void onTransitionEnd(Transition param1Transition) {
              if (MaterialContainerTransformSharedElementCallback.capturedSharedElement != null) {
                View view = MaterialContainerTransformSharedElementCallback.capturedSharedElement.get();
                if (view != null) {
                  view.setAlpha(1.0F);
                  MaterialContainerTransformSharedElementCallback.access$202(null);
                } 
              } 
              activity.finish();
              activity.overridePendingTransition(0, 0);
            }
          });
      if (this.transparentWindowBackgroundEnabled) {
        updateBackgroundFadeDuration(window, (MaterialContainerTransform)transition);
        transition.addListener(new TransitionListenerAdapter() {
              final MaterialContainerTransformSharedElementCallback this$0;
              
              final Window val$window;
              
              public void onTransitionStart(Transition param1Transition) {
                MaterialContainerTransformSharedElementCallback.removeWindowBackground(window);
              }
            });
      } 
    } 
  }
  
  private static void updateBackgroundFadeDuration(Window paramWindow, MaterialContainerTransform paramMaterialContainerTransform) {
    if (paramMaterialContainerTransform.getDuration() >= 0L)
      paramWindow.setTransitionBackgroundFadeDuration(paramMaterialContainerTransform.getDuration()); 
  }
  
  public ShapeProvider getShapeProvider() {
    return this.shapeProvider;
  }
  
  public boolean isSharedElementReenterTransitionEnabled() {
    return this.sharedElementReenterTransitionEnabled;
  }
  
  public boolean isTransparentWindowBackgroundEnabled() {
    return this.transparentWindowBackgroundEnabled;
  }
  
  public Parcelable onCaptureSharedElementSnapshot(View paramView, Matrix paramMatrix, RectF paramRectF) {
    capturedSharedElement = new WeakReference<>(paramView);
    return super.onCaptureSharedElementSnapshot(paramView, paramMatrix, paramRectF);
  }
  
  public View onCreateSnapshotView(Context paramContext, Parcelable paramParcelable) {
    View view = super.onCreateSnapshotView(paramContext, paramParcelable);
    if (view != null) {
      WeakReference<View> weakReference = capturedSharedElement;
      if (weakReference != null && this.shapeProvider != null) {
        View view1 = weakReference.get();
        if (view1 != null) {
          ShapeAppearanceModel shapeAppearanceModel = this.shapeProvider.provideShape(view1);
          if (shapeAppearanceModel != null)
            view.setTag(R.id.mtrl_motion_snapshot_view, shapeAppearanceModel); 
        } 
      } 
    } 
    return view;
  }
  
  public void onMapSharedElements(List<String> paramList, Map<String, View> paramMap) {
    if (!paramList.isEmpty() && !paramMap.isEmpty()) {
      View view = paramMap.get(paramList.get(0));
      if (view != null) {
        Activity activity = ContextUtils.getActivity(view.getContext());
        if (activity != null) {
          Window window = activity.getWindow();
          if (this.entering) {
            setUpEnterTransform(window);
          } else {
            setUpReturnTransform(activity, window);
          } 
        } 
      } 
    } 
  }
  
  public void onSharedElementEnd(List<String> paramList, List<View> paramList1, List<View> paramList2) {
    if (!paramList1.isEmpty() && ((View)paramList1.get(0)).getTag(R.id.mtrl_motion_snapshot_view) instanceof View)
      ((View)paramList1.get(0)).setTag(R.id.mtrl_motion_snapshot_view, null); 
    if (!this.entering && !paramList1.isEmpty())
      this.returnEndBounds = TransitionUtils.getRelativeBoundsRect(paramList1.get(0)); 
    this.entering = false;
  }
  
  public void onSharedElementStart(List<String> paramList, List<View> paramList1, List<View> paramList2) {
    if (!paramList1.isEmpty() && !paramList2.isEmpty())
      ((View)paramList1.get(0)).setTag(R.id.mtrl_motion_snapshot_view, paramList2.get(0)); 
    if (!this.entering && !paramList1.isEmpty() && this.returnEndBounds != null) {
      View view = paramList1.get(0);
      view.measure(View.MeasureSpec.makeMeasureSpec(this.returnEndBounds.width(), 1073741824), View.MeasureSpec.makeMeasureSpec(this.returnEndBounds.height(), 1073741824));
      view.layout(this.returnEndBounds.left, this.returnEndBounds.top, this.returnEndBounds.right, this.returnEndBounds.bottom);
    } 
  }
  
  public void setShapeProvider(ShapeProvider paramShapeProvider) {
    this.shapeProvider = paramShapeProvider;
  }
  
  public void setSharedElementReenterTransitionEnabled(boolean paramBoolean) {
    this.sharedElementReenterTransitionEnabled = paramBoolean;
  }
  
  public void setTransparentWindowBackgroundEnabled(boolean paramBoolean) {
    this.transparentWindowBackgroundEnabled = paramBoolean;
  }
  
  public static interface ShapeProvider {
    ShapeAppearanceModel provideShape(View param1View);
  }
  
  public static class ShapeableViewShapeProvider implements ShapeProvider {
    public ShapeAppearanceModel provideShape(View param1View) {
      if (param1View instanceof Shapeable) {
        ShapeAppearanceModel shapeAppearanceModel = ((Shapeable)param1View).getShapeAppearanceModel();
      } else {
        param1View = null;
      } 
      return (ShapeAppearanceModel)param1View;
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\transition\platform\MaterialContainerTransformSharedElementCallback.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */