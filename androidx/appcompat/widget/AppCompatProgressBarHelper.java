package androidx.appcompat.widget;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Shader;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ProgressBar;
import androidx.core.graphics.drawable.WrappedDrawable;

class AppCompatProgressBarHelper {
  private static final int[] TINT_ATTRS = new int[] { 16843067, 16843068 };
  
  private Bitmap mSampleTile;
  
  private final ProgressBar mView;
  
  AppCompatProgressBarHelper(ProgressBar paramProgressBar) {
    this.mView = paramProgressBar;
  }
  
  private Shape getDrawableShape() {
    return (Shape)new RoundRectShape(new float[] { 5.0F, 5.0F, 5.0F, 5.0F, 5.0F, 5.0F, 5.0F, 5.0F }, null, null);
  }
  
  private Drawable tileifyIndeterminate(Drawable paramDrawable) {
    AnimationDrawable animationDrawable;
    Drawable drawable = paramDrawable;
    if (paramDrawable instanceof AnimationDrawable) {
      AnimationDrawable animationDrawable1 = (AnimationDrawable)paramDrawable;
      int i = animationDrawable1.getNumberOfFrames();
      animationDrawable = new AnimationDrawable();
      animationDrawable.setOneShot(animationDrawable1.isOneShot());
      for (byte b = 0; b < i; b++) {
        paramDrawable = tileify(animationDrawable1.getFrame(b), true);
        paramDrawable.setLevel(10000);
        animationDrawable.addFrame(paramDrawable, animationDrawable1.getDuration(b));
      } 
      animationDrawable.setLevel(10000);
    } 
    return (Drawable)animationDrawable;
  }
  
  Bitmap getSampleTile() {
    return this.mSampleTile;
  }
  
  void loadFromAttributes(AttributeSet paramAttributeSet, int paramInt) {
    TintTypedArray tintTypedArray = TintTypedArray.obtainStyledAttributes(this.mView.getContext(), paramAttributeSet, TINT_ATTRS, paramInt, 0);
    Drawable drawable = tintTypedArray.getDrawableIfKnown(0);
    if (drawable != null)
      this.mView.setIndeterminateDrawable(tileifyIndeterminate(drawable)); 
    drawable = tintTypedArray.getDrawableIfKnown(1);
    if (drawable != null)
      this.mView.setProgressDrawable(tileify(drawable, false)); 
    tintTypedArray.recycle();
  }
  
  Drawable tileify(Drawable paramDrawable, boolean paramBoolean) {
    ClipDrawable clipDrawable;
    if (paramDrawable instanceof WrappedDrawable) {
      Drawable drawable = ((WrappedDrawable)paramDrawable).getWrappedDrawable();
      if (drawable != null) {
        drawable = tileify(drawable, paramBoolean);
        ((WrappedDrawable)paramDrawable).setWrappedDrawable(drawable);
      } 
    } else {
      LayerDrawable layerDrawable;
      if (paramDrawable instanceof LayerDrawable) {
        layerDrawable = (LayerDrawable)paramDrawable;
        int i = layerDrawable.getNumberOfLayers();
        Drawable[] arrayOfDrawable = new Drawable[i];
        byte b;
        for (b = 0; b < i; b++) {
          int j = layerDrawable.getId(b);
          Drawable drawable = layerDrawable.getDrawable(b);
          if (j == 16908301 || j == 16908303) {
            paramBoolean = true;
          } else {
            paramBoolean = false;
          } 
          arrayOfDrawable[b] = tileify(drawable, paramBoolean);
        } 
        LayerDrawable layerDrawable1 = new LayerDrawable(arrayOfDrawable);
        for (b = 0; b < i; b++) {
          layerDrawable1.setId(b, layerDrawable.getId(b));
          if (Build.VERSION.SDK_INT >= 23)
            Api23Impl.transferLayerProperties(layerDrawable, layerDrawable1, b); 
        } 
        return (Drawable)layerDrawable1;
      } 
      if (layerDrawable instanceof BitmapDrawable) {
        BitmapDrawable bitmapDrawable = (BitmapDrawable)layerDrawable;
        Bitmap bitmap = bitmapDrawable.getBitmap();
        if (this.mSampleTile == null)
          this.mSampleTile = bitmap; 
        ShapeDrawable shapeDrawable = new ShapeDrawable(getDrawableShape());
        BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.CLAMP);
        shapeDrawable.getPaint().setShader((Shader)bitmapShader);
        shapeDrawable.getPaint().setColorFilter(bitmapDrawable.getPaint().getColorFilter());
        if (paramBoolean)
          clipDrawable = new ClipDrawable((Drawable)shapeDrawable, 3, 1); 
        return (Drawable)clipDrawable;
      } 
    } 
    return (Drawable)clipDrawable;
  }
  
  private static class Api23Impl {
    public static void transferLayerProperties(LayerDrawable param1LayerDrawable1, LayerDrawable param1LayerDrawable2, int param1Int) {
      param1LayerDrawable2.setLayerGravity(param1Int, param1LayerDrawable1.getLayerGravity(param1Int));
      param1LayerDrawable2.setLayerWidth(param1Int, param1LayerDrawable1.getLayerWidth(param1Int));
      param1LayerDrawable2.setLayerHeight(param1Int, param1LayerDrawable1.getLayerHeight(param1Int));
      param1LayerDrawable2.setLayerInsetLeft(param1Int, param1LayerDrawable1.getLayerInsetLeft(param1Int));
      param1LayerDrawable2.setLayerInsetRight(param1Int, param1LayerDrawable1.getLayerInsetRight(param1Int));
      param1LayerDrawable2.setLayerInsetTop(param1Int, param1LayerDrawable1.getLayerInsetTop(param1Int));
      param1LayerDrawable2.setLayerInsetBottom(param1Int, param1LayerDrawable1.getLayerInsetBottom(param1Int));
      param1LayerDrawable2.setLayerInsetStart(param1Int, param1LayerDrawable1.getLayerInsetStart(param1Int));
      param1LayerDrawable2.setLayerInsetEnd(param1Int, param1LayerDrawable1.getLayerInsetEnd(param1Int));
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\appcompat\widget\AppCompatProgressBarHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */