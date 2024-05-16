package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.Movie;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import org.xmlpull.v1.XmlPullParserException;

public class VectorEnabledTintResources extends ResourcesWrapper {
  public static final int MAX_SDK_WHERE_REQUIRED = 20;
  
  private static boolean sCompatVectorFromResourcesEnabled = false;
  
  private final WeakReference<Context> mContextRef;
  
  public VectorEnabledTintResources(Context paramContext, Resources paramResources) {
    super(paramResources);
    this.mContextRef = new WeakReference<>(paramContext);
  }
  
  public static boolean isCompatVectorFromResourcesEnabled() {
    return sCompatVectorFromResourcesEnabled;
  }
  
  public static void setCompatVectorFromResourcesEnabled(boolean paramBoolean) {
    sCompatVectorFromResourcesEnabled = paramBoolean;
  }
  
  public static boolean shouldBeUsed() {
    boolean bool;
    if (isCompatVectorFromResourcesEnabled() && Build.VERSION.SDK_INT <= 20) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public Drawable getDrawable(int paramInt) throws Resources.NotFoundException {
    Context context = this.mContextRef.get();
    return (context != null) ? ResourceManagerInternal.get().onDrawableLoadedFromResources(context, this, paramInt) : getDrawableCanonical(paramInt);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\appcompat\widget\VectorEnabledTintResources.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */