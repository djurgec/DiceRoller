package androidx.constraintlayout.motion.widget;

import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.SharedValues;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class ViewTransitionController {
  private String TAG = "ViewTransitionController";
  
  ArrayList<ViewTransition.Animate> animations;
  
  private final MotionLayout mMotionLayout;
  
  private HashSet<View> mRelatedViews;
  
  ArrayList<ViewTransition.Animate> removeList = new ArrayList<>();
  
  private ArrayList<ViewTransition> viewTransitions = new ArrayList<>();
  
  public ViewTransitionController(MotionLayout paramMotionLayout) {
    this.mMotionLayout = paramMotionLayout;
  }
  
  private void listenForSharedVariable(final ViewTransition viewTransition, final boolean isSet) {
    final int listen_for_id = viewTransition.getSharedValueID();
    final int listen_for_value = viewTransition.getSharedValue();
    ConstraintLayout.getSharedValues().addListener(viewTransition.getSharedValueID(), new SharedValues.SharedValuesListener() {
          final ViewTransitionController this$0;
          
          final boolean val$isSet;
          
          final int val$listen_for_id;
          
          final int val$listen_for_value;
          
          final ViewTransition val$viewTransition;
          
          public void onNewValue(int param1Int1, int param1Int2, int param1Int3) {
            param1Int3 = viewTransition.getSharedValueCurrent();
            viewTransition.setSharedValueCurrent(param1Int2);
            if (listen_for_id == param1Int1 && param1Int3 != param1Int2)
              if (isSet) {
                if (listen_for_value == param1Int2) {
                  param1Int2 = ViewTransitionController.this.mMotionLayout.getChildCount();
                  for (param1Int1 = 0; param1Int1 < param1Int2; param1Int1++) {
                    View view = ViewTransitionController.this.mMotionLayout.getChildAt(param1Int1);
                    if (viewTransition.matchesView(view)) {
                      param1Int3 = ViewTransitionController.this.mMotionLayout.getCurrentState();
                      ConstraintSet constraintSet = ViewTransitionController.this.mMotionLayout.getConstraintSet(param1Int3);
                      ViewTransition viewTransition = viewTransition;
                      ViewTransitionController viewTransitionController = ViewTransitionController.this;
                      viewTransition.applyTransition(viewTransitionController, viewTransitionController.mMotionLayout, param1Int3, constraintSet, new View[] { view });
                    } 
                  } 
                } 
              } else if (listen_for_value != param1Int2) {
                param1Int2 = ViewTransitionController.this.mMotionLayout.getChildCount();
                for (param1Int1 = 0; param1Int1 < param1Int2; param1Int1++) {
                  View view = ViewTransitionController.this.mMotionLayout.getChildAt(param1Int1);
                  if (viewTransition.matchesView(view)) {
                    param1Int3 = ViewTransitionController.this.mMotionLayout.getCurrentState();
                    ConstraintSet constraintSet = ViewTransitionController.this.mMotionLayout.getConstraintSet(param1Int3);
                    ViewTransition viewTransition = viewTransition;
                    ViewTransitionController viewTransitionController = ViewTransitionController.this;
                    viewTransition.applyTransition(viewTransitionController, viewTransitionController.mMotionLayout, param1Int3, constraintSet, new View[] { view });
                  } 
                } 
              }  
          }
        });
  }
  
  private void viewTransition(ViewTransition paramViewTransition, View... paramVarArgs) {
    String str1;
    String str2;
    int i = this.mMotionLayout.getCurrentState();
    if (paramViewTransition.mViewTransitionMode != 2) {
      if (i == -1) {
        str2 = this.TAG;
        str1 = String.valueOf(this.mMotionLayout.toString());
        if (str1.length() != 0) {
          str1 = "No support for ViewTransition within transition yet. Currently: ".concat(str1);
        } else {
          str1 = new String("No support for ViewTransition within transition yet. Currently: ");
        } 
        Log.w(str2, str1);
        return;
      } 
      ConstraintSet constraintSet = this.mMotionLayout.getConstraintSet(i);
      if (constraintSet == null)
        return; 
      str1.applyTransition(this, this.mMotionLayout, i, constraintSet, (View[])str2);
    } else {
      str1.applyTransition(this, this.mMotionLayout, i, null, (View[])str2);
    } 
  }
  
  public void add(ViewTransition paramViewTransition) {
    this.viewTransitions.add(paramViewTransition);
    this.mRelatedViews = null;
    if (paramViewTransition.getStateTransition() == 4) {
      listenForSharedVariable(paramViewTransition, true);
    } else if (paramViewTransition.getStateTransition() == 5) {
      listenForSharedVariable(paramViewTransition, false);
    } 
  }
  
  void addAnimation(ViewTransition.Animate paramAnimate) {
    if (this.animations == null)
      this.animations = new ArrayList<>(); 
    this.animations.add(paramAnimate);
  }
  
  void animate() {
    ArrayList<ViewTransition.Animate> arrayList = this.animations;
    if (arrayList == null)
      return; 
    Iterator<ViewTransition.Animate> iterator = arrayList.iterator();
    while (iterator.hasNext())
      ((ViewTransition.Animate)iterator.next()).mutate(); 
    this.animations.removeAll(this.removeList);
    this.removeList.clear();
    if (this.animations.isEmpty())
      this.animations = null; 
  }
  
  boolean applyViewTransition(int paramInt, MotionController paramMotionController) {
    for (ViewTransition viewTransition : this.viewTransitions) {
      if (viewTransition.getId() == paramInt) {
        viewTransition.mKeyFrames.addAllFrames(paramMotionController);
        return true;
      } 
    } 
    return false;
  }
  
  void enableViewTransition(int paramInt, boolean paramBoolean) {
    for (ViewTransition viewTransition : this.viewTransitions) {
      if (viewTransition.getId() == paramInt) {
        viewTransition.setEnabled(paramBoolean);
        break;
      } 
    } 
  }
  
  void invalidate() {
    this.mMotionLayout.invalidate();
  }
  
  boolean isViewTransitionEnabled(int paramInt) {
    for (ViewTransition viewTransition : this.viewTransitions) {
      if (viewTransition.getId() == paramInt)
        return viewTransition.isEnabled(); 
    } 
    return false;
  }
  
  void remove(int paramInt) {
    ViewTransition viewTransition1;
    ViewTransition viewTransition2 = null;
    Iterator<ViewTransition> iterator = this.viewTransitions.iterator();
    while (true) {
      viewTransition1 = viewTransition2;
      if (iterator.hasNext()) {
        viewTransition1 = iterator.next();
        if (viewTransition1.getId() == paramInt)
          break; 
        continue;
      } 
      break;
    } 
    if (viewTransition1 != null) {
      this.mRelatedViews = null;
      this.viewTransitions.remove(viewTransition1);
    } 
  }
  
  void removeAnimation(ViewTransition.Animate paramAnimate) {
    this.removeList.add(paramAnimate);
  }
  
  void touchEvent(MotionEvent paramMotionEvent) {
    int j = this.mMotionLayout.getCurrentState();
    if (j == -1)
      return; 
    if (this.mRelatedViews == null) {
      this.mRelatedViews = new HashSet<>();
      for (ViewTransition viewTransition : this.viewTransitions) {
        int k = this.mMotionLayout.getChildCount();
        for (byte b = 0; b < k; b++) {
          View view = this.mMotionLayout.getChildAt(b);
          if (viewTransition.matchesView(view)) {
            view.getId();
            this.mRelatedViews.add(view);
          } 
        } 
      } 
    } 
    float f2 = paramMotionEvent.getX();
    float f1 = paramMotionEvent.getY();
    Rect rect = new Rect();
    int i = paramMotionEvent.getAction();
    ArrayList<ViewTransition.Animate> arrayList = this.animations;
    if (arrayList != null && !arrayList.isEmpty()) {
      Iterator<ViewTransition.Animate> iterator = this.animations.iterator();
      while (iterator.hasNext())
        ((ViewTransition.Animate)iterator.next()).reactTo(i, f2, f1); 
    } 
    switch (i) {
      default:
        return;
      case 0:
      case 1:
        break;
    } 
    ConstraintSet constraintSet = this.mMotionLayout.getConstraintSet(j);
    for (ViewTransition viewTransition : this.viewTransitions) {
      if (viewTransition.supports(i))
        for (View view : this.mRelatedViews) {
          if (!viewTransition.matchesView(view))
            continue; 
          view.getHitRect(rect);
          if (rect.contains((int)f2, (int)f1))
            viewTransition.applyTransition(this, this.mMotionLayout, j, constraintSet, new View[] { view }); 
        }  
    } 
  }
  
  void viewTransition(int paramInt, View... paramVarArgs) {
    ViewTransition viewTransition;
    View view = null;
    ArrayList<View> arrayList = new ArrayList();
    for (ViewTransition viewTransition1 : this.viewTransitions) {
      if (viewTransition1.getId() == paramInt) {
        ViewTransition viewTransition2 = viewTransition1;
        int i = paramVarArgs.length;
        for (byte b = 0; b < i; b++) {
          view = paramVarArgs[b];
          if (viewTransition1.checkTags(view))
            arrayList.add(view); 
        } 
        viewTransition = viewTransition2;
        if (!arrayList.isEmpty()) {
          viewTransition(viewTransition2, arrayList.<View>toArray(new View[0]));
          arrayList.clear();
          viewTransition = viewTransition2;
        } 
      } 
    } 
    if (viewTransition == null) {
      Log.e(this.TAG, " Could not find ViewTransition");
      return;
    } 
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\motion\widget\ViewTransitionController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */