package androidx.core.view;

import android.graphics.Insets;
import android.os.Build;
import android.view.WindowInsetsAnimationController;
import androidx.core.graphics.Insets;

public final class WindowInsetsAnimationControllerCompat {
  private final Impl mImpl;
  
  WindowInsetsAnimationControllerCompat() {
    if (Build.VERSION.SDK_INT < 30) {
      this.mImpl = new Impl();
      return;
    } 
    throw new UnsupportedOperationException("On API 30+, the constructor taking a " + WindowInsetsAnimationController.class.getSimpleName() + " as parameter");
  }
  
  WindowInsetsAnimationControllerCompat(WindowInsetsAnimationController paramWindowInsetsAnimationController) {
    this.mImpl = new Impl30(paramWindowInsetsAnimationController);
  }
  
  public void finish(boolean paramBoolean) {
    this.mImpl.finish(paramBoolean);
  }
  
  public float getCurrentAlpha() {
    return this.mImpl.getCurrentAlpha();
  }
  
  public float getCurrentFraction() {
    return this.mImpl.getCurrentFraction();
  }
  
  public Insets getCurrentInsets() {
    return this.mImpl.getCurrentInsets();
  }
  
  public Insets getHiddenStateInsets() {
    return this.mImpl.getHiddenStateInsets();
  }
  
  public Insets getShownStateInsets() {
    return this.mImpl.getShownStateInsets();
  }
  
  public int getTypes() {
    return this.mImpl.getTypes();
  }
  
  public boolean isCancelled() {
    return this.mImpl.isCancelled();
  }
  
  public boolean isFinished() {
    return this.mImpl.isFinished();
  }
  
  public boolean isReady() {
    boolean bool;
    if (!isFinished() && !isCancelled()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void setInsetsAndAlpha(Insets paramInsets, float paramFloat1, float paramFloat2) {
    this.mImpl.setInsetsAndAlpha(paramInsets, paramFloat1, paramFloat2);
  }
  
  private static class Impl {
    void finish(boolean param1Boolean) {}
    
    public float getCurrentAlpha() {
      return 0.0F;
    }
    
    public float getCurrentFraction() {
      return 0.0F;
    }
    
    public Insets getCurrentInsets() {
      return Insets.NONE;
    }
    
    public Insets getHiddenStateInsets() {
      return Insets.NONE;
    }
    
    public Insets getShownStateInsets() {
      return Insets.NONE;
    }
    
    public int getTypes() {
      return 0;
    }
    
    boolean isCancelled() {
      return true;
    }
    
    boolean isFinished() {
      return false;
    }
    
    public boolean isReady() {
      return false;
    }
    
    public void setInsetsAndAlpha(Insets param1Insets, float param1Float1, float param1Float2) {}
  }
  
  private static class Impl30 extends Impl {
    private final WindowInsetsAnimationController mController;
    
    Impl30(WindowInsetsAnimationController param1WindowInsetsAnimationController) {
      this.mController = param1WindowInsetsAnimationController;
    }
    
    void finish(boolean param1Boolean) {
      this.mController.finish(param1Boolean);
    }
    
    public float getCurrentAlpha() {
      return this.mController.getCurrentAlpha();
    }
    
    public float getCurrentFraction() {
      return this.mController.getCurrentFraction();
    }
    
    public Insets getCurrentInsets() {
      return Insets.toCompatInsets(this.mController.getCurrentInsets());
    }
    
    public Insets getHiddenStateInsets() {
      return Insets.toCompatInsets(this.mController.getHiddenStateInsets());
    }
    
    public Insets getShownStateInsets() {
      return Insets.toCompatInsets(this.mController.getShownStateInsets());
    }
    
    public int getTypes() {
      return this.mController.getTypes();
    }
    
    boolean isCancelled() {
      return this.mController.isCancelled();
    }
    
    boolean isFinished() {
      return this.mController.isFinished();
    }
    
    public boolean isReady() {
      return this.mController.isReady();
    }
    
    public void setInsetsAndAlpha(Insets param1Insets, float param1Float1, float param1Float2) {
      Insets insets;
      WindowInsetsAnimationController windowInsetsAnimationController = this.mController;
      if (param1Insets == null) {
        param1Insets = null;
      } else {
        insets = param1Insets.toPlatformInsets();
      } 
      windowInsetsAnimationController.setInsetsAndAlpha(insets, param1Float1, param1Float2);
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\view\WindowInsetsAnimationControllerCompat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */