package androidx.core.view;

import android.os.Build;
import android.os.CancellationSignal;
import android.view.View;
import android.view.Window;
import android.view.WindowInsetsAnimationControlListener;
import android.view.WindowInsetsAnimationController;
import android.view.WindowInsetsController;
import android.view.animation.Interpolator;
import android.view.inputmethod.InputMethodManager;
import androidx.collection.SimpleArrayMap;

public final class WindowInsetsControllerCompat {
  public static final int BEHAVIOR_SHOW_BARS_BY_SWIPE = 1;
  
  public static final int BEHAVIOR_SHOW_BARS_BY_TOUCH = 0;
  
  public static final int BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE = 2;
  
  private final Impl mImpl;
  
  public WindowInsetsControllerCompat(Window paramWindow, View paramView) {
    if (Build.VERSION.SDK_INT >= 30) {
      this.mImpl = new Impl30(paramWindow, this);
    } else if (Build.VERSION.SDK_INT >= 26) {
      this.mImpl = new Impl26(paramWindow, paramView);
    } else if (Build.VERSION.SDK_INT >= 23) {
      this.mImpl = new Impl23(paramWindow, paramView);
    } else if (Build.VERSION.SDK_INT >= 20) {
      this.mImpl = new Impl20(paramWindow, paramView);
    } else {
      this.mImpl = new Impl();
    } 
  }
  
  private WindowInsetsControllerCompat(WindowInsetsController paramWindowInsetsController) {
    if (Build.VERSION.SDK_INT >= 30) {
      this.mImpl = new Impl30(paramWindowInsetsController, this);
    } else {
      this.mImpl = new Impl();
    } 
  }
  
  public static WindowInsetsControllerCompat toWindowInsetsControllerCompat(WindowInsetsController paramWindowInsetsController) {
    return new WindowInsetsControllerCompat(paramWindowInsetsController);
  }
  
  public void addOnControllableInsetsChangedListener(OnControllableInsetsChangedListener paramOnControllableInsetsChangedListener) {
    this.mImpl.addOnControllableInsetsChangedListener(paramOnControllableInsetsChangedListener);
  }
  
  public void controlWindowInsetsAnimation(int paramInt, long paramLong, Interpolator paramInterpolator, CancellationSignal paramCancellationSignal, WindowInsetsAnimationControlListenerCompat paramWindowInsetsAnimationControlListenerCompat) {
    this.mImpl.controlWindowInsetsAnimation(paramInt, paramLong, paramInterpolator, paramCancellationSignal, paramWindowInsetsAnimationControlListenerCompat);
  }
  
  public int getSystemBarsBehavior() {
    return this.mImpl.getSystemBarsBehavior();
  }
  
  public void hide(int paramInt) {
    this.mImpl.hide(paramInt);
  }
  
  public boolean isAppearanceLightNavigationBars() {
    return this.mImpl.isAppearanceLightNavigationBars();
  }
  
  public boolean isAppearanceLightStatusBars() {
    return this.mImpl.isAppearanceLightStatusBars();
  }
  
  public void removeOnControllableInsetsChangedListener(OnControllableInsetsChangedListener paramOnControllableInsetsChangedListener) {
    this.mImpl.removeOnControllableInsetsChangedListener(paramOnControllableInsetsChangedListener);
  }
  
  public void setAppearanceLightNavigationBars(boolean paramBoolean) {
    this.mImpl.setAppearanceLightNavigationBars(paramBoolean);
  }
  
  public void setAppearanceLightStatusBars(boolean paramBoolean) {
    this.mImpl.setAppearanceLightStatusBars(paramBoolean);
  }
  
  public void setSystemBarsBehavior(int paramInt) {
    this.mImpl.setSystemBarsBehavior(paramInt);
  }
  
  public void show(int paramInt) {
    this.mImpl.show(paramInt);
  }
  
  private static class Impl {
    void addOnControllableInsetsChangedListener(WindowInsetsControllerCompat.OnControllableInsetsChangedListener param1OnControllableInsetsChangedListener) {}
    
    void controlWindowInsetsAnimation(int param1Int, long param1Long, Interpolator param1Interpolator, CancellationSignal param1CancellationSignal, WindowInsetsAnimationControlListenerCompat param1WindowInsetsAnimationControlListenerCompat) {}
    
    int getSystemBarsBehavior() {
      return 0;
    }
    
    void hide(int param1Int) {}
    
    public boolean isAppearanceLightNavigationBars() {
      return false;
    }
    
    public boolean isAppearanceLightStatusBars() {
      return false;
    }
    
    void removeOnControllableInsetsChangedListener(WindowInsetsControllerCompat.OnControllableInsetsChangedListener param1OnControllableInsetsChangedListener) {}
    
    public void setAppearanceLightNavigationBars(boolean param1Boolean) {}
    
    public void setAppearanceLightStatusBars(boolean param1Boolean) {}
    
    void setSystemBarsBehavior(int param1Int) {}
    
    void show(int param1Int) {}
  }
  
  private static class Impl20 extends Impl {
    private final View mView;
    
    protected final Window mWindow;
    
    Impl20(Window param1Window, View param1View) {
      this.mWindow = param1Window;
      this.mView = param1View;
    }
    
    private void hideForType(int param1Int) {
      switch (param1Int) {
        default:
          return;
        case 8:
          ((InputMethodManager)this.mWindow.getContext().getSystemService("input_method")).hideSoftInputFromWindow(this.mWindow.getDecorView().getWindowToken(), 0);
        case 2:
          setSystemUiFlag(2);
          return;
        case 1:
          break;
      } 
      setSystemUiFlag(4);
    }
    
    private void showForType(int param1Int) {
      View view1;
      final View finalView;
      switch (param1Int) {
        default:
          return;
        case 8:
          view1 = this.mView;
          if (view1 != null && (view1.isInEditMode() || view1.onCheckIsTextEditor())) {
            view1.requestFocus();
          } else {
            view1 = this.mWindow.getCurrentFocus();
          } 
          view2 = view1;
          if (view1 == null)
            view2 = this.mWindow.findViewById(16908290); 
          if (view2 != null && view2.hasWindowFocus())
            view2.post(new Runnable() {
                  final WindowInsetsControllerCompat.Impl20 this$0;
                  
                  final View val$finalView;
                  
                  public void run() {
                    ((InputMethodManager)finalView.getContext().getSystemService("input_method")).showSoftInput(finalView, 0);
                  }
                }); 
        case 2:
          unsetSystemUiFlag(2);
          return;
        case 1:
          break;
      } 
      unsetSystemUiFlag(4);
      unsetWindowFlag(1024);
    }
    
    void addOnControllableInsetsChangedListener(WindowInsetsControllerCompat.OnControllableInsetsChangedListener param1OnControllableInsetsChangedListener) {}
    
    void controlWindowInsetsAnimation(int param1Int, long param1Long, Interpolator param1Interpolator, CancellationSignal param1CancellationSignal, WindowInsetsAnimationControlListenerCompat param1WindowInsetsAnimationControlListenerCompat) {}
    
    int getSystemBarsBehavior() {
      return 0;
    }
    
    void hide(int param1Int) {
      for (int i = 1; i <= 256; i <<= 1) {
        if ((param1Int & i) != 0)
          hideForType(i); 
      } 
    }
    
    void removeOnControllableInsetsChangedListener(WindowInsetsControllerCompat.OnControllableInsetsChangedListener param1OnControllableInsetsChangedListener) {}
    
    void setSystemBarsBehavior(int param1Int) {
      switch (param1Int) {
        default:
          return;
        case 2:
          unsetSystemUiFlag(2048);
          setSystemUiFlag(4096);
        case 1:
          unsetSystemUiFlag(4096);
          setSystemUiFlag(2048);
        case 0:
          break;
      } 
      unsetSystemUiFlag(6144);
    }
    
    protected void setSystemUiFlag(int param1Int) {
      View view = this.mWindow.getDecorView();
      view.setSystemUiVisibility(view.getSystemUiVisibility() | param1Int);
    }
    
    protected void setWindowFlag(int param1Int) {
      this.mWindow.addFlags(param1Int);
    }
    
    void show(int param1Int) {
      for (int i = 1; i <= 256; i <<= 1) {
        if ((param1Int & i) != 0)
          showForType(i); 
      } 
    }
    
    protected void unsetSystemUiFlag(int param1Int) {
      View view = this.mWindow.getDecorView();
      view.setSystemUiVisibility(view.getSystemUiVisibility() & (param1Int ^ 0xFFFFFFFF));
    }
    
    protected void unsetWindowFlag(int param1Int) {
      this.mWindow.clearFlags(param1Int);
    }
  }
  
  class null implements Runnable {
    final WindowInsetsControllerCompat.Impl20 this$0;
    
    final View val$finalView;
    
    public void run() {
      ((InputMethodManager)finalView.getContext().getSystemService("input_method")).showSoftInput(finalView, 0);
    }
  }
  
  private static class Impl23 extends Impl20 {
    Impl23(Window param1Window, View param1View) {
      super(param1Window, param1View);
    }
    
    public boolean isAppearanceLightStatusBars() {
      boolean bool;
      if ((this.mWindow.getDecorView().getSystemUiVisibility() & 0x2000) != 0) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public void setAppearanceLightStatusBars(boolean param1Boolean) {
      if (param1Boolean) {
        unsetWindowFlag(67108864);
        setWindowFlag(-2147483648);
        setSystemUiFlag(8192);
      } else {
        unsetSystemUiFlag(8192);
      } 
    }
  }
  
  private static class Impl26 extends Impl23 {
    Impl26(Window param1Window, View param1View) {
      super(param1Window, param1View);
    }
    
    public boolean isAppearanceLightNavigationBars() {
      boolean bool;
      if ((this.mWindow.getDecorView().getSystemUiVisibility() & 0x10) != 0) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public void setAppearanceLightNavigationBars(boolean param1Boolean) {
      if (param1Boolean) {
        unsetWindowFlag(134217728);
        setWindowFlag(-2147483648);
        setSystemUiFlag(16);
      } else {
        unsetSystemUiFlag(16);
      } 
    }
  }
  
  private static class Impl30 extends Impl {
    final WindowInsetsControllerCompat mCompatController;
    
    final WindowInsetsController mInsetsController;
    
    private final SimpleArrayMap<WindowInsetsControllerCompat.OnControllableInsetsChangedListener, WindowInsetsController.OnControllableInsetsChangedListener> mListeners = new SimpleArrayMap();
    
    protected Window mWindow;
    
    Impl30(Window param1Window, WindowInsetsControllerCompat param1WindowInsetsControllerCompat) {
      this(param1Window.getInsetsController(), param1WindowInsetsControllerCompat);
      this.mWindow = param1Window;
    }
    
    Impl30(WindowInsetsController param1WindowInsetsController, WindowInsetsControllerCompat param1WindowInsetsControllerCompat) {
      this.mInsetsController = param1WindowInsetsController;
      this.mCompatController = param1WindowInsetsControllerCompat;
    }
    
    void addOnControllableInsetsChangedListener(final WindowInsetsControllerCompat.OnControllableInsetsChangedListener listener) {
      if (this.mListeners.containsKey(listener))
        return; 
      WindowInsetsController.OnControllableInsetsChangedListener onControllableInsetsChangedListener = new WindowInsetsController.OnControllableInsetsChangedListener() {
          final WindowInsetsControllerCompat.Impl30 this$0;
          
          final WindowInsetsControllerCompat.OnControllableInsetsChangedListener val$listener;
          
          public void onControllableInsetsChanged(WindowInsetsController param2WindowInsetsController, int param2Int) {
            if (WindowInsetsControllerCompat.Impl30.this.mInsetsController == param2WindowInsetsController)
              listener.onControllableInsetsChanged(WindowInsetsControllerCompat.Impl30.this.mCompatController, param2Int); 
          }
        };
      this.mListeners.put(listener, onControllableInsetsChangedListener);
      this.mInsetsController.addOnControllableInsetsChangedListener(onControllableInsetsChangedListener);
    }
    
    void controlWindowInsetsAnimation(int param1Int, long param1Long, Interpolator param1Interpolator, CancellationSignal param1CancellationSignal, final WindowInsetsAnimationControlListenerCompat listener) {
      WindowInsetsAnimationControlListener windowInsetsAnimationControlListener = new WindowInsetsAnimationControlListener() {
          private WindowInsetsAnimationControllerCompat mCompatAnimController = null;
          
          final WindowInsetsControllerCompat.Impl30 this$0;
          
          final WindowInsetsAnimationControlListenerCompat val$listener;
          
          public void onCancelled(WindowInsetsAnimationController param2WindowInsetsAnimationController) {
            WindowInsetsAnimationControllerCompat windowInsetsAnimationControllerCompat;
            WindowInsetsAnimationControlListenerCompat windowInsetsAnimationControlListenerCompat = listener;
            if (param2WindowInsetsAnimationController == null) {
              param2WindowInsetsAnimationController = null;
            } else {
              windowInsetsAnimationControllerCompat = this.mCompatAnimController;
            } 
            windowInsetsAnimationControlListenerCompat.onCancelled(windowInsetsAnimationControllerCompat);
          }
          
          public void onFinished(WindowInsetsAnimationController param2WindowInsetsAnimationController) {
            listener.onFinished(this.mCompatAnimController);
          }
          
          public void onReady(WindowInsetsAnimationController param2WindowInsetsAnimationController, int param2Int) {
            WindowInsetsAnimationControllerCompat windowInsetsAnimationControllerCompat = new WindowInsetsAnimationControllerCompat(param2WindowInsetsAnimationController);
            this.mCompatAnimController = windowInsetsAnimationControllerCompat;
            listener.onReady(windowInsetsAnimationControllerCompat, param2Int);
          }
        };
      this.mInsetsController.controlWindowInsetsAnimation(param1Int, param1Long, param1Interpolator, param1CancellationSignal, windowInsetsAnimationControlListener);
    }
    
    int getSystemBarsBehavior() {
      return this.mInsetsController.getSystemBarsBehavior();
    }
    
    void hide(int param1Int) {
      this.mInsetsController.hide(param1Int);
    }
    
    public boolean isAppearanceLightNavigationBars() {
      boolean bool;
      if ((this.mInsetsController.getSystemBarsAppearance() & 0x10) != 0) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public boolean isAppearanceLightStatusBars() {
      boolean bool;
      if ((this.mInsetsController.getSystemBarsAppearance() & 0x8) != 0) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    void removeOnControllableInsetsChangedListener(WindowInsetsControllerCompat.OnControllableInsetsChangedListener param1OnControllableInsetsChangedListener) {
      WindowInsetsController.OnControllableInsetsChangedListener onControllableInsetsChangedListener = (WindowInsetsController.OnControllableInsetsChangedListener)this.mListeners.remove(param1OnControllableInsetsChangedListener);
      if (onControllableInsetsChangedListener != null)
        this.mInsetsController.removeOnControllableInsetsChangedListener(onControllableInsetsChangedListener); 
    }
    
    public void setAppearanceLightNavigationBars(boolean param1Boolean) {
      if (param1Boolean) {
        this.mInsetsController.setSystemBarsAppearance(16, 16);
      } else {
        this.mInsetsController.setSystemBarsAppearance(0, 16);
      } 
    }
    
    public void setAppearanceLightStatusBars(boolean param1Boolean) {
      if (param1Boolean) {
        if (this.mWindow != null)
          unsetSystemUiFlag(8192); 
        this.mInsetsController.setSystemBarsAppearance(8, 8);
      } else {
        this.mInsetsController.setSystemBarsAppearance(0, 8);
      } 
    }
    
    void setSystemBarsBehavior(int param1Int) {
      this.mInsetsController.setSystemBarsBehavior(param1Int);
    }
    
    void show(int param1Int) {
      this.mInsetsController.show(param1Int);
    }
    
    protected void unsetSystemUiFlag(int param1Int) {
      View view = this.mWindow.getDecorView();
      view.setSystemUiVisibility(view.getSystemUiVisibility() & (param1Int ^ 0xFFFFFFFF));
    }
  }
  
  class null implements WindowInsetsAnimationControlListener {
    private WindowInsetsAnimationControllerCompat mCompatAnimController = null;
    
    final WindowInsetsControllerCompat.Impl30 this$0;
    
    final WindowInsetsAnimationControlListenerCompat val$listener;
    
    public void onCancelled(WindowInsetsAnimationController param1WindowInsetsAnimationController) {
      WindowInsetsAnimationControllerCompat windowInsetsAnimationControllerCompat;
      WindowInsetsAnimationControlListenerCompat windowInsetsAnimationControlListenerCompat = listener;
      if (param1WindowInsetsAnimationController == null) {
        param1WindowInsetsAnimationController = null;
      } else {
        windowInsetsAnimationControllerCompat = this.mCompatAnimController;
      } 
      windowInsetsAnimationControlListenerCompat.onCancelled(windowInsetsAnimationControllerCompat);
    }
    
    public void onFinished(WindowInsetsAnimationController param1WindowInsetsAnimationController) {
      listener.onFinished(this.mCompatAnimController);
    }
    
    public void onReady(WindowInsetsAnimationController param1WindowInsetsAnimationController, int param1Int) {
      WindowInsetsAnimationControllerCompat windowInsetsAnimationControllerCompat = new WindowInsetsAnimationControllerCompat(param1WindowInsetsAnimationController);
      this.mCompatAnimController = windowInsetsAnimationControllerCompat;
      listener.onReady(windowInsetsAnimationControllerCompat, param1Int);
    }
  }
  
  class null implements WindowInsetsController.OnControllableInsetsChangedListener {
    final WindowInsetsControllerCompat.Impl30 this$0;
    
    final WindowInsetsControllerCompat.OnControllableInsetsChangedListener val$listener;
    
    public void onControllableInsetsChanged(WindowInsetsController param1WindowInsetsController, int param1Int) {
      if (this.this$0.mInsetsController == param1WindowInsetsController)
        listener.onControllableInsetsChanged(this.this$0.mCompatController, param1Int); 
    }
  }
  
  public static interface OnControllableInsetsChangedListener {
    void onControllableInsetsChanged(WindowInsetsControllerCompat param1WindowInsetsControllerCompat, int param1Int);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\view\WindowInsetsControllerCompat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */