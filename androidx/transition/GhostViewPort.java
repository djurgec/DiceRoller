package androidx.transition;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import androidx.core.view.ViewCompat;

class GhostViewPort extends ViewGroup implements GhostView {
  private Matrix mMatrix;
  
  private final ViewTreeObserver.OnPreDrawListener mOnPreDrawListener = new ViewTreeObserver.OnPreDrawListener() {
      final GhostViewPort this$0;
      
      public boolean onPreDraw() {
        ViewCompat.postInvalidateOnAnimation((View)GhostViewPort.this);
        if (GhostViewPort.this.mStartParent != null && GhostViewPort.this.mStartView != null) {
          GhostViewPort.this.mStartParent.endViewTransition(GhostViewPort.this.mStartView);
          ViewCompat.postInvalidateOnAnimation((View)GhostViewPort.this.mStartParent);
          GhostViewPort.this.mStartParent = null;
          GhostViewPort.this.mStartView = null;
        } 
        return true;
      }
    };
  
  int mReferences;
  
  ViewGroup mStartParent;
  
  View mStartView;
  
  final View mView;
  
  GhostViewPort(View paramView) {
    super(paramView.getContext());
    this.mView = paramView;
    setWillNotDraw(false);
    setLayerType(2, null);
  }
  
  static GhostViewPort addGhost(View paramView, ViewGroup paramViewGroup, Matrix paramMatrix) {
    if (paramView.getParent() instanceof ViewGroup) {
      Matrix matrix1;
      GhostViewPort ghostViewPort1;
      Matrix matrix2;
      GhostViewHolder ghostViewHolder = GhostViewHolder.getHolder(paramViewGroup);
      GhostViewPort ghostViewPort3 = getGhostView(paramView);
      byte b = 0;
      GhostViewPort ghostViewPort2 = ghostViewPort3;
      int i = b;
      if (ghostViewPort3 != null) {
        GhostViewHolder ghostViewHolder1 = (GhostViewHolder)ghostViewPort3.getParent();
        ghostViewPort2 = ghostViewPort3;
        i = b;
        if (ghostViewHolder1 != ghostViewHolder) {
          i = ghostViewPort3.mReferences;
          ghostViewHolder1.removeView((View)ghostViewPort3);
          ghostViewPort2 = null;
        } 
      } 
      if (ghostViewPort2 == null) {
        GhostViewHolder ghostViewHolder1;
        matrix2 = paramMatrix;
        if (paramMatrix == null) {
          matrix2 = new Matrix();
          calculateMatrix(paramView, paramViewGroup, matrix2);
        } 
        ghostViewPort1 = new GhostViewPort(paramView);
        ghostViewPort1.setMatrix(matrix2);
        if (ghostViewHolder == null) {
          ghostViewHolder1 = new GhostViewHolder(paramViewGroup);
        } else {
          ghostViewHolder.popToOverlayTop();
          ghostViewHolder1 = ghostViewHolder;
        } 
        copySize((View)paramViewGroup, (View)ghostViewHolder1);
        copySize((View)paramViewGroup, (View)ghostViewPort1);
        ghostViewHolder1.addGhostView(ghostViewPort1);
        ghostViewPort1.mReferences = i;
        GhostViewPort ghostViewPort = ghostViewPort1;
      } else {
        matrix1 = matrix2;
        if (ghostViewPort1 != null) {
          matrix2.setMatrix((Matrix)ghostViewPort1);
          matrix1 = matrix2;
        } 
      } 
      ((GhostViewPort)matrix1).mReferences++;
      return (GhostViewPort)matrix1;
    } 
    throw new IllegalArgumentException("Ghosted views must be parented by a ViewGroup");
  }
  
  static void calculateMatrix(View paramView, ViewGroup paramViewGroup, Matrix paramMatrix) {
    ViewGroup viewGroup = (ViewGroup)paramView.getParent();
    paramMatrix.reset();
    ViewUtils.transformMatrixToGlobal((View)viewGroup, paramMatrix);
    paramMatrix.preTranslate(-viewGroup.getScrollX(), -viewGroup.getScrollY());
    ViewUtils.transformMatrixToLocal((View)paramViewGroup, paramMatrix);
  }
  
  static void copySize(View paramView1, View paramView2) {
    ViewUtils.setLeftTopRightBottom(paramView2, paramView2.getLeft(), paramView2.getTop(), paramView2.getLeft() + paramView1.getWidth(), paramView2.getTop() + paramView1.getHeight());
  }
  
  static GhostViewPort getGhostView(View paramView) {
    return (GhostViewPort)paramView.getTag(R.id.ghost_view);
  }
  
  static void removeGhost(View paramView) {
    GhostViewPort ghostViewPort = getGhostView(paramView);
    if (ghostViewPort != null) {
      int i = ghostViewPort.mReferences - 1;
      ghostViewPort.mReferences = i;
      if (i <= 0)
        ((GhostViewHolder)ghostViewPort.getParent()).removeView((View)ghostViewPort); 
    } 
  }
  
  static void setGhostView(View paramView, GhostViewPort paramGhostViewPort) {
    paramView.setTag(R.id.ghost_view, paramGhostViewPort);
  }
  
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    setGhostView(this.mView, this);
    this.mView.getViewTreeObserver().addOnPreDrawListener(this.mOnPreDrawListener);
    ViewUtils.setTransitionVisibility(this.mView, 4);
    if (this.mView.getParent() != null)
      ((View)this.mView.getParent()).invalidate(); 
  }
  
  protected void onDetachedFromWindow() {
    this.mView.getViewTreeObserver().removeOnPreDrawListener(this.mOnPreDrawListener);
    ViewUtils.setTransitionVisibility(this.mView, 0);
    setGhostView(this.mView, (GhostViewPort)null);
    if (this.mView.getParent() != null)
      ((View)this.mView.getParent()).invalidate(); 
    super.onDetachedFromWindow();
  }
  
  protected void onDraw(Canvas paramCanvas) {
    CanvasUtils.enableZ(paramCanvas, true);
    paramCanvas.setMatrix(this.mMatrix);
    ViewUtils.setTransitionVisibility(this.mView, 0);
    this.mView.invalidate();
    ViewUtils.setTransitionVisibility(this.mView, 4);
    drawChild(paramCanvas, this.mView, getDrawingTime());
    CanvasUtils.enableZ(paramCanvas, false);
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {}
  
  public void reserveEndViewTransition(ViewGroup paramViewGroup, View paramView) {
    this.mStartParent = paramViewGroup;
    this.mStartView = paramView;
  }
  
  void setMatrix(Matrix paramMatrix) {
    this.mMatrix = paramMatrix;
  }
  
  public void setVisibility(int paramInt) {
    super.setVisibility(paramInt);
    if (getGhostView(this.mView) == this) {
      if (paramInt == 0) {
        paramInt = 4;
      } else {
        paramInt = 0;
      } 
      ViewUtils.setTransitionVisibility(this.mView, paramInt);
    } 
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\transition\GhostViewPort.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */