package com.google.android.material.transition;

import android.animation.TimeInterpolator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Build;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewParent;
import androidx.core.graphics.PathParser;
import androidx.core.view.animation.PathInterpolatorCompat;
import androidx.transition.PathMotion;
import androidx.transition.PatternPathMotion;
import androidx.transition.Transition;
import androidx.transition.TransitionSet;
import com.google.android.material.resources.MaterialAttributes;
import com.google.android.material.shape.AbsoluteCornerSize;
import com.google.android.material.shape.CornerSize;
import com.google.android.material.shape.RelativeCornerSize;
import com.google.android.material.shape.ShapeAppearanceModel;

class TransitionUtils {
  private static final String EASING_TYPE_CUBIC_BEZIER = "cubic-bezier";
  
  private static final String EASING_TYPE_FORMAT_END = ")";
  
  private static final String EASING_TYPE_FORMAT_START = "(";
  
  private static final String EASING_TYPE_PATH = "path";
  
  static final int NO_ATTR_RES_ID = 0;
  
  static final int NO_DURATION = -1;
  
  private static final int PATH_TYPE_ARC = 1;
  
  private static final int PATH_TYPE_LINEAR = 0;
  
  private static final RectF transformAlphaRectF = new RectF();
  
  static float calculateArea(RectF paramRectF) {
    return paramRectF.width() * paramRectF.height();
  }
  
  static ShapeAppearanceModel convertToRelativeCornerSizes(ShapeAppearanceModel paramShapeAppearanceModel, final RectF bounds) {
    return paramShapeAppearanceModel.withTransformedCornerSizes(new ShapeAppearanceModel.CornerSizeUnaryOperator() {
          final RectF val$bounds;
          
          public CornerSize apply(CornerSize param1CornerSize) {
            RelativeCornerSize relativeCornerSize;
            if (!(param1CornerSize instanceof RelativeCornerSize))
              relativeCornerSize = new RelativeCornerSize(param1CornerSize.getCornerSize(bounds) / bounds.height()); 
            return (CornerSize)relativeCornerSize;
          }
        });
  }
  
  static Shader createColorShader(int paramInt) {
    return (Shader)new LinearGradient(0.0F, 0.0F, 0.0F, 0.0F, paramInt, paramInt, Shader.TileMode.CLAMP);
  }
  
  static <T> T defaultIfNull(T paramT1, T paramT2) {
    if (paramT1 != null)
      paramT2 = paramT1; 
    return paramT2;
  }
  
  static View findAncestorById(View paramView, int paramInt) {
    String str = paramView.getResources().getResourceName(paramInt);
    while (paramView != null) {
      if (paramView.getId() == paramInt)
        return paramView; 
      ViewParent viewParent = paramView.getParent();
      if (viewParent instanceof View)
        View view = (View)viewParent; 
    } 
    throw new IllegalArgumentException(str + " is not a valid ancestor");
  }
  
  static View findDescendantOrAncestorById(View paramView, int paramInt) {
    View view = paramView.findViewById(paramInt);
    return (view != null) ? view : findAncestorById(paramView, paramInt);
  }
  
  private static float getControlPoint(String[] paramArrayOfString, int paramInt) {
    float f = Float.parseFloat(paramArrayOfString[paramInt]);
    if (f >= 0.0F && f <= 1.0F)
      return f; 
    throw new IllegalArgumentException("Motion easing control point value must be between 0 and 1; instead got: " + f);
  }
  
  private static String getEasingContent(String paramString1, String paramString2) {
    return paramString1.substring(paramString2.length() + "(".length(), paramString1.length() - ")".length());
  }
  
  static RectF getLocationOnScreen(View paramView) {
    int[] arrayOfInt = new int[2];
    paramView.getLocationOnScreen(arrayOfInt);
    int m = arrayOfInt[0];
    int j = arrayOfInt[1];
    int i = paramView.getWidth();
    int k = paramView.getHeight();
    return new RectF(m, j, (i + m), (k + j));
  }
  
  static RectF getRelativeBounds(View paramView) {
    return new RectF(paramView.getLeft(), paramView.getTop(), paramView.getRight(), paramView.getBottom());
  }
  
  static Rect getRelativeBoundsRect(View paramView) {
    return new Rect(paramView.getLeft(), paramView.getTop(), paramView.getRight(), paramView.getBottom());
  }
  
  private static boolean isEasingType(String paramString1, String paramString2) {
    boolean bool;
    if (paramString1.startsWith(paramString2 + "(") && paramString1.endsWith(")")) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private static boolean isShapeAppearanceSignificant(ShapeAppearanceModel paramShapeAppearanceModel, RectF paramRectF) {
    return (paramShapeAppearanceModel.getTopLeftCornerSize().getCornerSize(paramRectF) != 0.0F || paramShapeAppearanceModel.getTopRightCornerSize().getCornerSize(paramRectF) != 0.0F || paramShapeAppearanceModel.getBottomRightCornerSize().getCornerSize(paramRectF) != 0.0F || paramShapeAppearanceModel.getBottomLeftCornerSize().getCornerSize(paramRectF) != 0.0F);
  }
  
  static float lerp(float paramFloat1, float paramFloat2, float paramFloat3) {
    return (paramFloat2 - paramFloat1) * paramFloat3 + paramFloat1;
  }
  
  static float lerp(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5) {
    return lerp(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, false);
  }
  
  static float lerp(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, boolean paramBoolean) {
    return (paramBoolean && (paramFloat5 < 0.0F || paramFloat5 > 1.0F)) ? lerp(paramFloat1, paramFloat2, paramFloat5) : ((paramFloat5 < paramFloat3) ? paramFloat1 : ((paramFloat5 > paramFloat4) ? paramFloat2 : lerp(paramFloat1, paramFloat2, (paramFloat5 - paramFloat3) / (paramFloat4 - paramFloat3))));
  }
  
  static int lerp(int paramInt1, int paramInt2, float paramFloat1, float paramFloat2, float paramFloat3) {
    return (paramFloat3 < paramFloat1) ? paramInt1 : ((paramFloat3 > paramFloat2) ? paramInt2 : (int)lerp(paramInt1, paramInt2, (paramFloat3 - paramFloat1) / (paramFloat2 - paramFloat1)));
  }
  
  static ShapeAppearanceModel lerp(ShapeAppearanceModel paramShapeAppearanceModel1, ShapeAppearanceModel paramShapeAppearanceModel2, final RectF startBounds, final RectF endBounds, final float startFraction, final float endFraction, final float fraction) {
    return (fraction < startFraction) ? paramShapeAppearanceModel1 : ((fraction > endFraction) ? paramShapeAppearanceModel2 : transformCornerSizes(paramShapeAppearanceModel1, paramShapeAppearanceModel2, startBounds, new CornerSizeBinaryOperator() {
          final RectF val$endBounds;
          
          final float val$endFraction;
          
          final float val$fraction;
          
          final RectF val$startBounds;
          
          final float val$startFraction;
          
          public CornerSize apply(CornerSize param1CornerSize1, CornerSize param1CornerSize2) {
            return (CornerSize)new AbsoluteCornerSize(TransitionUtils.lerp(param1CornerSize1.getCornerSize(startBounds), param1CornerSize2.getCornerSize(endBounds), startFraction, endFraction, fraction));
          }
        }));
  }
  
  static void maybeAddTransition(TransitionSet paramTransitionSet, Transition paramTransition) {
    if (paramTransition != null)
      paramTransitionSet.addTransition(paramTransition); 
  }
  
  static boolean maybeApplyThemeDuration(Transition paramTransition, Context paramContext, int paramInt) {
    if (paramInt != 0 && paramTransition.getDuration() == -1L) {
      paramInt = MaterialAttributes.resolveInteger(paramContext, paramInt, -1);
      if (paramInt != -1) {
        paramTransition.setDuration(paramInt);
        return true;
      } 
    } 
    return false;
  }
  
  static boolean maybeApplyThemeInterpolator(Transition paramTransition, Context paramContext, int paramInt, TimeInterpolator paramTimeInterpolator) {
    if (paramInt != 0 && paramTransition.getInterpolator() == null) {
      paramTransition.setInterpolator(resolveThemeInterpolator(paramContext, paramInt, paramTimeInterpolator));
      return true;
    } 
    return false;
  }
  
  static boolean maybeApplyThemePath(Transition paramTransition, Context paramContext, int paramInt) {
    if (paramInt != 0) {
      PathMotion pathMotion = resolveThemePath(paramContext, paramInt);
      if (pathMotion != null) {
        paramTransition.setPathMotion(pathMotion);
        return true;
      } 
    } 
    return false;
  }
  
  static void maybeRemoveTransition(TransitionSet paramTransitionSet, Transition paramTransition) {
    if (paramTransition != null)
      paramTransitionSet.removeTransition(paramTransition); 
  }
  
  static TimeInterpolator resolveThemeInterpolator(Context paramContext, int paramInt, TimeInterpolator paramTimeInterpolator) {
    TypedValue typedValue = new TypedValue();
    if (paramContext.getTheme().resolveAttribute(paramInt, typedValue, true)) {
      if (typedValue.type == 3) {
        String[] arrayOfString;
        String str = String.valueOf(typedValue.string);
        if (isEasingType(str, "cubic-bezier")) {
          arrayOfString = getEasingContent(str, "cubic-bezier").split(",");
          if (arrayOfString.length == 4)
            return (TimeInterpolator)PathInterpolatorCompat.create(getControlPoint(arrayOfString, 0), getControlPoint(arrayOfString, 1), getControlPoint(arrayOfString, 2), getControlPoint(arrayOfString, 3)); 
          throw new IllegalArgumentException("Motion easing theme attribute must have 4 control points if using bezier curve format; instead got: " + arrayOfString.length);
        } 
        if (isEasingType((String)arrayOfString, "path"))
          return (TimeInterpolator)PathInterpolatorCompat.create(PathParser.createPathFromPathData(getEasingContent((String)arrayOfString, "path"))); 
        throw new IllegalArgumentException("Invalid motion easing type: " + arrayOfString);
      } 
      throw new IllegalArgumentException("Motion easing theme attribute must be a string");
    } 
    return paramTimeInterpolator;
  }
  
  static PathMotion resolveThemePath(Context paramContext, int paramInt) {
    TypedValue typedValue = new TypedValue();
    if (paramContext.getTheme().resolveAttribute(paramInt, typedValue, true)) {
      if (typedValue.type == 16) {
        paramInt = typedValue.data;
        if (paramInt == 0)
          return null; 
        if (paramInt == 1)
          return new MaterialArcMotion(); 
        throw new IllegalArgumentException("Invalid motion path type: " + paramInt);
      } 
      if (typedValue.type == 3)
        return (PathMotion)new PatternPathMotion(PathParser.createPathFromPathData(String.valueOf(typedValue.string))); 
      throw new IllegalArgumentException("Motion path theme attribute must either be an enum value or path data string");
    } 
    return null;
  }
  
  private static int saveLayerAlphaCompat(Canvas paramCanvas, Rect paramRect, int paramInt) {
    RectF rectF = transformAlphaRectF;
    rectF.set(paramRect);
    return (Build.VERSION.SDK_INT >= 21) ? paramCanvas.saveLayerAlpha(rectF, paramInt) : paramCanvas.saveLayerAlpha(rectF.left, rectF.top, rectF.right, rectF.bottom, paramInt, 31);
  }
  
  static void transform(Canvas paramCanvas, Rect paramRect, float paramFloat1, float paramFloat2, float paramFloat3, int paramInt, CanvasOperation paramCanvasOperation) {
    if (paramInt <= 0)
      return; 
    int i = paramCanvas.save();
    paramCanvas.translate(paramFloat1, paramFloat2);
    paramCanvas.scale(paramFloat3, paramFloat3);
    if (paramInt < 255)
      saveLayerAlphaCompat(paramCanvas, paramRect, paramInt); 
    paramCanvasOperation.run(paramCanvas);
    paramCanvas.restoreToCount(i);
  }
  
  static ShapeAppearanceModel transformCornerSizes(ShapeAppearanceModel paramShapeAppearanceModel1, ShapeAppearanceModel paramShapeAppearanceModel2, RectF paramRectF, CornerSizeBinaryOperator paramCornerSizeBinaryOperator) {
    ShapeAppearanceModel shapeAppearanceModel;
    if (isShapeAppearanceSignificant(paramShapeAppearanceModel1, paramRectF)) {
      shapeAppearanceModel = paramShapeAppearanceModel1;
    } else {
      shapeAppearanceModel = paramShapeAppearanceModel2;
    } 
    return shapeAppearanceModel.toBuilder().setTopLeftCornerSize(paramCornerSizeBinaryOperator.apply(paramShapeAppearanceModel1.getTopLeftCornerSize(), paramShapeAppearanceModel2.getTopLeftCornerSize())).setTopRightCornerSize(paramCornerSizeBinaryOperator.apply(paramShapeAppearanceModel1.getTopRightCornerSize(), paramShapeAppearanceModel2.getTopRightCornerSize())).setBottomLeftCornerSize(paramCornerSizeBinaryOperator.apply(paramShapeAppearanceModel1.getBottomLeftCornerSize(), paramShapeAppearanceModel2.getBottomLeftCornerSize())).setBottomRightCornerSize(paramCornerSizeBinaryOperator.apply(paramShapeAppearanceModel1.getBottomRightCornerSize(), paramShapeAppearanceModel2.getBottomRightCornerSize())).build();
  }
  
  static interface CanvasOperation {
    void run(Canvas param1Canvas);
  }
  
  static interface CornerSizeBinaryOperator {
    CornerSize apply(CornerSize param1CornerSize1, CornerSize param1CornerSize2);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\transition\TransitionUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */