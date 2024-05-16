package androidx.transition;

import android.graphics.Canvas;
import android.os.Build;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class CanvasUtils {
  private static Method sInorderBarrierMethod;
  
  private static boolean sOrderMethodsFetched;
  
  private static Method sReorderBarrierMethod;
  
  static void enableZ(Canvas paramCanvas, boolean paramBoolean) {
    if (Build.VERSION.SDK_INT >= 21)
      if (Build.VERSION.SDK_INT >= 29) {
        if (paramBoolean) {
          paramCanvas.enableZ();
        } else {
          paramCanvas.disableZ();
        } 
      } else {
        if (Build.VERSION.SDK_INT != 28) {
          if (!sOrderMethodsFetched) {
            try {
              Method method = Canvas.class.getDeclaredMethod("insertReorderBarrier", new Class[0]);
              sReorderBarrierMethod = method;
              method.setAccessible(true);
              method = Canvas.class.getDeclaredMethod("insertInorderBarrier", new Class[0]);
              sInorderBarrierMethod = method;
              method.setAccessible(true);
            } catch (NoSuchMethodException noSuchMethodException) {}
            sOrderMethodsFetched = true;
          } 
          if (paramBoolean)
            try {
              Method method = sReorderBarrierMethod;
              if (method != null)
                method.invoke(paramCanvas, new Object[0]); 
            } catch (IllegalAccessException illegalAccessException) {
            
            } catch (InvocationTargetException invocationTargetException) {} 
          if (!paramBoolean) {
            Method method = sInorderBarrierMethod;
            if (method != null)
              method.invoke(invocationTargetException, new Object[0]); 
          } 
          return;
        } 
        throw new IllegalStateException("This method doesn't work on Pie!");
      }  
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\transition\CanvasUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */