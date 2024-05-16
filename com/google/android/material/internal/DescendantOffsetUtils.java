package com.google.android.material.internal;

import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

public class DescendantOffsetUtils {
  private static final ThreadLocal<Matrix> matrix = new ThreadLocal<>();
  
  private static final ThreadLocal<RectF> rectF = new ThreadLocal<>();
  
  public static void getDescendantRect(ViewGroup paramViewGroup, View paramView, Rect paramRect) {
    paramRect.set(0, 0, paramView.getWidth(), paramView.getHeight());
    offsetDescendantRect(paramViewGroup, paramView, paramRect);
  }
  
  private static void offsetDescendantMatrix(ViewParent paramViewParent, View paramView, Matrix paramMatrix) {
    ViewParent viewParent = paramView.getParent();
    if (viewParent instanceof View && viewParent != paramViewParent) {
      View view = (View)viewParent;
      offsetDescendantMatrix(paramViewParent, view, paramMatrix);
      paramMatrix.preTranslate(-view.getScrollX(), -view.getScrollY());
    } 
    paramMatrix.preTranslate(paramView.getLeft(), paramView.getTop());
    if (!paramView.getMatrix().isIdentity())
      paramMatrix.preConcat(paramView.getMatrix()); 
  }
  
  public static void offsetDescendantRect(ViewGroup paramViewGroup, View paramView, Rect paramRect) {
    ThreadLocal<Matrix> threadLocal1 = matrix;
    Matrix matrix = threadLocal1.get();
    if (matrix == null) {
      matrix = new Matrix();
      threadLocal1.set(matrix);
    } else {
      matrix.reset();
    } 
    offsetDescendantMatrix((ViewParent)paramViewGroup, paramView, matrix);
    ThreadLocal<RectF> threadLocal = rectF;
    RectF rectF2 = threadLocal.get();
    RectF rectF1 = rectF2;
    if (rectF2 == null) {
      rectF1 = new RectF();
      threadLocal.set(rectF1);
    } 
    rectF1.set(paramRect);
    matrix.mapRect(rectF1);
    paramRect.set((int)(rectF1.left + 0.5F), (int)(rectF1.top + 0.5F), (int)(rectF1.right + 0.5F), (int)(rectF1.bottom + 0.5F));
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\internal\DescendantOffsetUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */