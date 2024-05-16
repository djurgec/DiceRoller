package androidx.cardview.widget;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

class CardViewApi17Impl extends CardViewBaseImpl {
  public void initStatic() {
    RoundRectDrawableWithShadow.sRoundRectHelper = new RoundRectDrawableWithShadow.RoundRectHelper() {
        final CardViewApi17Impl this$0;
        
        public void drawRoundRect(Canvas param1Canvas, RectF param1RectF, float param1Float, Paint param1Paint) {
          param1Canvas.drawRoundRect(param1RectF, param1Float, param1Float, param1Paint);
        }
      };
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\cardview\widget\CardViewApi17Impl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */