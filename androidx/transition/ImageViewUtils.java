package androidx.transition;

import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.widget.ImageView;
import java.lang.reflect.Field;

class ImageViewUtils {
  private static Field sDrawMatrixField;
  
  private static boolean sDrawMatrixFieldFetched;
  
  private static boolean sTryHiddenAnimateTransform = true;
  
  static void animateTransform(ImageView paramImageView, Matrix paramMatrix) {
    if (Build.VERSION.SDK_INT >= 29) {
      paramImageView.animateTransform(paramMatrix);
    } else {
      Drawable drawable;
      if (paramMatrix == null) {
        drawable = paramImageView.getDrawable();
        if (drawable != null) {
          drawable.setBounds(0, 0, paramImageView.getWidth() - paramImageView.getPaddingLeft() - paramImageView.getPaddingRight(), paramImageView.getHeight() - paramImageView.getPaddingTop() - paramImageView.getPaddingBottom());
          paramImageView.invalidate();
        } 
      } else if (Build.VERSION.SDK_INT >= 21) {
        hiddenAnimateTransform(paramImageView, (Matrix)drawable);
      } else {
        Drawable drawable1 = paramImageView.getDrawable();
        if (drawable1 != null) {
          Matrix matrix;
          drawable1.setBounds(0, 0, drawable1.getIntrinsicWidth(), drawable1.getIntrinsicHeight());
          drawable1 = null;
          Drawable drawable2 = null;
          fetchDrawMatrixField();
          Field field = sDrawMatrixField;
          if (field != null) {
            drawable1 = drawable2;
            try {
              Matrix matrix1 = (Matrix)field.get(paramImageView);
              matrix = matrix1;
              if (matrix1 == null) {
                matrix = matrix1;
                Matrix matrix2 = new Matrix();
                matrix = matrix1;
                this();
                matrix1 = matrix2;
                matrix = matrix1;
                sDrawMatrixField.set(paramImageView, matrix1);
                matrix = matrix1;
              } 
            } catch (IllegalAccessException illegalAccessException) {}
          } 
          if (matrix != null)
            matrix.set((Matrix)drawable); 
          paramImageView.invalidate();
        } 
      } 
    } 
  }
  
  private static void fetchDrawMatrixField() {
    if (!sDrawMatrixFieldFetched) {
      try {
        Field field = ImageView.class.getDeclaredField("mDrawMatrix");
        sDrawMatrixField = field;
        field.setAccessible(true);
      } catch (NoSuchFieldException noSuchFieldException) {}
      sDrawMatrixFieldFetched = true;
    } 
  }
  
  private static void hiddenAnimateTransform(ImageView paramImageView, Matrix paramMatrix) {
    if (sTryHiddenAnimateTransform)
      try {
        paramImageView.animateTransform(paramMatrix);
      } catch (NoSuchMethodError noSuchMethodError) {
        sTryHiddenAnimateTransform = false;
      }  
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\transition\ImageViewUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */