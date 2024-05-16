package androidx.appcompat.widget;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Build;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class TintContextWrapper extends ContextWrapper {
  private static final Object CACHE_LOCK = new Object();
  
  private static ArrayList<WeakReference<TintContextWrapper>> sCache;
  
  private final Resources mResources;
  
  private final Resources.Theme mTheme;
  
  private TintContextWrapper(Context paramContext) {
    super(paramContext);
    if (VectorEnabledTintResources.shouldBeUsed()) {
      VectorEnabledTintResources vectorEnabledTintResources = new VectorEnabledTintResources((Context)this, paramContext.getResources());
      this.mResources = vectorEnabledTintResources;
      Resources.Theme theme = vectorEnabledTintResources.newTheme();
      this.mTheme = theme;
      theme.setTo(paramContext.getTheme());
    } else {
      this.mResources = new TintResources((Context)this, paramContext.getResources());
      this.mTheme = null;
    } 
  }
  
  private static boolean shouldWrap(Context paramContext) {
    boolean bool1 = paramContext instanceof TintContextWrapper;
    boolean bool = false;
    if (bool1 || paramContext.getResources() instanceof TintResources || paramContext.getResources() instanceof VectorEnabledTintResources)
      return false; 
    if (Build.VERSION.SDK_INT < 21 || VectorEnabledTintResources.shouldBeUsed())
      bool = true; 
    return bool;
  }
  
  public static Context wrap(Context paramContext) {
    if (shouldWrap(paramContext))
      synchronized (CACHE_LOCK) {
        ArrayList<WeakReference<TintContextWrapper>> arrayList1 = sCache;
        if (arrayList1 == null) {
          arrayList1 = new ArrayList<>();
          this();
          sCache = arrayList1;
        } else {
          int i;
          for (i = arrayList1.size() - 1; i >= 0; i--) {
            WeakReference weakReference1 = sCache.get(i);
            if (weakReference1 == null || weakReference1.get() == null)
              sCache.remove(i); 
          } 
          for (i = sCache.size() - 1; i >= 0; i--) {
            WeakReference<TintContextWrapper> weakReference1 = sCache.get(i);
            if (weakReference1 != null) {
              TintContextWrapper tintContextWrapper1 = weakReference1.get();
            } else {
              weakReference1 = null;
            } 
            if (weakReference1 != null && weakReference1.getBaseContext() == paramContext)
              return (Context)weakReference1; 
          } 
        } 
        TintContextWrapper tintContextWrapper = new TintContextWrapper();
        this(paramContext);
        ArrayList<WeakReference<TintContextWrapper>> arrayList2 = sCache;
        WeakReference<TintContextWrapper> weakReference = new WeakReference();
        this((T)tintContextWrapper);
        arrayList2.add(weakReference);
        return (Context)tintContextWrapper;
      }  
    return paramContext;
  }
  
  public AssetManager getAssets() {
    return this.mResources.getAssets();
  }
  
  public Resources getResources() {
    return this.mResources;
  }
  
  public Resources.Theme getTheme() {
    Resources.Theme theme2 = this.mTheme;
    Resources.Theme theme1 = theme2;
    if (theme2 == null)
      theme1 = super.getTheme(); 
    return theme1;
  }
  
  public void setTheme(int paramInt) {
    Resources.Theme theme = this.mTheme;
    if (theme == null) {
      super.setTheme(paramInt);
    } else {
      theme.applyStyle(paramInt, true);
    } 
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\appcompat\widget\TintContextWrapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */