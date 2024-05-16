package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import java.lang.ref.WeakReference;

class TintResources extends ResourcesWrapper {
  private final WeakReference<Context> mContextRef;
  
  public TintResources(Context paramContext, Resources paramResources) {
    super(paramResources);
    this.mContextRef = new WeakReference<>(paramContext);
  }
  
  public Drawable getDrawable(int paramInt) throws Resources.NotFoundException {
    Drawable drawable = getDrawableCanonical(paramInt);
    Context context = this.mContextRef.get();
    if (drawable != null && context != null)
      ResourceManagerInternal.get().tintDrawableUsingColorFilter(context, paramInt, drawable); 
    return drawable;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\appcompat\widget\TintResources.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */