package androidx.core.content.res;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import androidx.core.graphics.TypefaceCompat;
import androidx.core.util.ObjectsCompat;
import androidx.core.util.Preconditions;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.WeakHashMap;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public final class ResourcesCompat {
  public static final int ID_NULL = 0;
  
  private static final String TAG = "ResourcesCompat";
  
  private static final Object sColorStateCacheLock;
  
  private static final WeakHashMap<ColorStateListCacheKey, SparseArray<ColorStateListCacheEntry>> sColorStateCaches;
  
  private static final ThreadLocal<TypedValue> sTempTypedValue = new ThreadLocal<>();
  
  static {
    sColorStateCaches = new WeakHashMap<>(0);
    sColorStateCacheLock = new Object();
  }
  
  private static void addColorStateListToCache(ColorStateListCacheKey paramColorStateListCacheKey, int paramInt, ColorStateList paramColorStateList) {
    synchronized (sColorStateCacheLock) {
      WeakHashMap<ColorStateListCacheKey, SparseArray<ColorStateListCacheEntry>> weakHashMap = sColorStateCaches;
      SparseArray<ColorStateListCacheEntry> sparseArray2 = weakHashMap.get(paramColorStateListCacheKey);
      SparseArray<ColorStateListCacheEntry> sparseArray1 = sparseArray2;
      if (sparseArray2 == null) {
        sparseArray1 = new SparseArray();
        this();
        weakHashMap.put(paramColorStateListCacheKey, sparseArray1);
      } 
      ColorStateListCacheEntry colorStateListCacheEntry = new ColorStateListCacheEntry();
      this(paramColorStateList, paramColorStateListCacheKey.mResources.getConfiguration());
      sparseArray1.append(paramInt, colorStateListCacheEntry);
      return;
    } 
  }
  
  private static ColorStateList getCachedColorStateList(ColorStateListCacheKey paramColorStateListCacheKey, int paramInt) {
    synchronized (sColorStateCacheLock) {
      SparseArray sparseArray = sColorStateCaches.get(paramColorStateListCacheKey);
      if (sparseArray != null && sparseArray.size() > 0) {
        ColorStateListCacheEntry colorStateListCacheEntry = (ColorStateListCacheEntry)sparseArray.get(paramInt);
        if (colorStateListCacheEntry != null) {
          if (colorStateListCacheEntry.mConfiguration.equals(paramColorStateListCacheKey.mResources.getConfiguration()))
            return colorStateListCacheEntry.mValue; 
          sparseArray.remove(paramInt);
        } 
      } 
      return null;
    } 
  }
  
  public static Typeface getCachedFont(Context paramContext, int paramInt) throws Resources.NotFoundException {
    return paramContext.isRestricted() ? null : loadFont(paramContext, paramInt, new TypedValue(), 0, null, null, false, true);
  }
  
  public static int getColor(Resources paramResources, int paramInt, Resources.Theme paramTheme) throws Resources.NotFoundException {
    return (Build.VERSION.SDK_INT >= 23) ? paramResources.getColor(paramInt, paramTheme) : paramResources.getColor(paramInt);
  }
  
  public static ColorStateList getColorStateList(Resources paramResources, int paramInt, Resources.Theme paramTheme) throws Resources.NotFoundException {
    ColorStateListCacheKey colorStateListCacheKey = new ColorStateListCacheKey(paramResources, paramTheme);
    ColorStateList colorStateList = getCachedColorStateList(colorStateListCacheKey, paramInt);
    if (colorStateList != null)
      return colorStateList; 
    colorStateList = inflateColorStateList(paramResources, paramInt, paramTheme);
    if (colorStateList != null) {
      addColorStateListToCache(colorStateListCacheKey, paramInt, colorStateList);
      return colorStateList;
    } 
    return (Build.VERSION.SDK_INT >= 23) ? Api23Impl.getColorStateList(paramResources, paramInt, paramTheme) : paramResources.getColorStateList(paramInt);
  }
  
  public static Drawable getDrawable(Resources paramResources, int paramInt, Resources.Theme paramTheme) throws Resources.NotFoundException {
    return (Build.VERSION.SDK_INT >= 21) ? paramResources.getDrawable(paramInt, paramTheme) : paramResources.getDrawable(paramInt);
  }
  
  public static Drawable getDrawableForDensity(Resources paramResources, int paramInt1, int paramInt2, Resources.Theme paramTheme) throws Resources.NotFoundException {
    return (Build.VERSION.SDK_INT >= 21) ? paramResources.getDrawableForDensity(paramInt1, paramInt2, paramTheme) : ((Build.VERSION.SDK_INT >= 15) ? paramResources.getDrawableForDensity(paramInt1, paramInt2) : paramResources.getDrawable(paramInt1));
  }
  
  public static float getFloat(Resources paramResources, int paramInt) {
    if (Build.VERSION.SDK_INT >= 29)
      return ImplApi29.getFloat(paramResources, paramInt); 
    TypedValue typedValue = getTypedValue();
    paramResources.getValue(paramInt, typedValue, true);
    if (typedValue.type == 4)
      return typedValue.getFloat(); 
    throw new Resources.NotFoundException("Resource ID #0x" + Integer.toHexString(paramInt) + " type #0x" + Integer.toHexString(typedValue.type) + " is not valid");
  }
  
  public static Typeface getFont(Context paramContext, int paramInt) throws Resources.NotFoundException {
    return paramContext.isRestricted() ? null : loadFont(paramContext, paramInt, new TypedValue(), 0, null, null, false, false);
  }
  
  public static Typeface getFont(Context paramContext, int paramInt1, TypedValue paramTypedValue, int paramInt2, FontCallback paramFontCallback) throws Resources.NotFoundException {
    return paramContext.isRestricted() ? null : loadFont(paramContext, paramInt1, paramTypedValue, paramInt2, paramFontCallback, null, true, false);
  }
  
  public static void getFont(Context paramContext, int paramInt, FontCallback paramFontCallback, Handler paramHandler) throws Resources.NotFoundException {
    Preconditions.checkNotNull(paramFontCallback);
    if (paramContext.isRestricted()) {
      paramFontCallback.callbackFailAsync(-4, paramHandler);
      return;
    } 
    loadFont(paramContext, paramInt, new TypedValue(), 0, paramFontCallback, paramHandler, false, false);
  }
  
  private static TypedValue getTypedValue() {
    ThreadLocal<TypedValue> threadLocal = sTempTypedValue;
    TypedValue typedValue2 = threadLocal.get();
    TypedValue typedValue1 = typedValue2;
    if (typedValue2 == null) {
      typedValue1 = new TypedValue();
      threadLocal.set(typedValue1);
    } 
    return typedValue1;
  }
  
  private static ColorStateList inflateColorStateList(Resources paramResources, int paramInt, Resources.Theme paramTheme) {
    if (isColorInt(paramResources, paramInt))
      return null; 
    XmlResourceParser xmlResourceParser = paramResources.getXml(paramInt);
    try {
      return ColorStateListInflaterCompat.createFromXml(paramResources, (XmlPullParser)xmlResourceParser, paramTheme);
    } catch (Exception exception) {
      Log.w("ResourcesCompat", "Failed to inflate ColorStateList, leaving it to the framework", exception);
      return null;
    } 
  }
  
  private static boolean isColorInt(Resources paramResources, int paramInt) {
    TypedValue typedValue = getTypedValue();
    boolean bool = true;
    paramResources.getValue(paramInt, typedValue, true);
    if (typedValue.type < 28 || typedValue.type > 31)
      bool = false; 
    return bool;
  }
  
  private static Typeface loadFont(Context paramContext, int paramInt1, TypedValue paramTypedValue, int paramInt2, FontCallback paramFontCallback, Handler paramHandler, boolean paramBoolean1, boolean paramBoolean2) {
    Resources resources = paramContext.getResources();
    resources.getValue(paramInt1, paramTypedValue, true);
    Typeface typeface = loadFont(paramContext, resources, paramTypedValue, paramInt1, paramInt2, paramFontCallback, paramHandler, paramBoolean1, paramBoolean2);
    if (typeface != null || paramFontCallback != null || paramBoolean2)
      return typeface; 
    throw new Resources.NotFoundException("Font resource ID #0x" + Integer.toHexString(paramInt1) + " could not be retrieved.");
  }
  
  private static Typeface loadFont(Context paramContext, Resources paramResources, TypedValue paramTypedValue, int paramInt1, int paramInt2, FontCallback paramFontCallback, Handler paramHandler, boolean paramBoolean1, boolean paramBoolean2) {
    String str;
    if (paramTypedValue.string != null) {
      str = paramTypedValue.string.toString();
      if (!str.startsWith("res/")) {
        if (paramFontCallback != null)
          paramFontCallback.callbackFailAsync(-3, paramHandler); 
        return null;
      } 
      Typeface typeface = TypefaceCompat.findFromCache(paramResources, paramInt1, paramInt2);
      if (typeface != null) {
        if (paramFontCallback != null)
          paramFontCallback.callbackSuccessAsync(typeface, paramHandler); 
        return typeface;
      } 
      if (paramBoolean2)
        return null; 
      try {
        paramBoolean2 = str.toLowerCase().endsWith(".xml");
        if (paramBoolean2) {
          try {
            XmlResourceParser xmlResourceParser = paramResources.getXml(paramInt1);
            FontResourcesParserCompat.FamilyResourceEntry familyResourceEntry = FontResourcesParserCompat.parse((XmlPullParser)xmlResourceParser, paramResources);
            if (familyResourceEntry == null) {
              try {
                Log.e("ResourcesCompat", "Failed to find font-family tag");
                if (paramFontCallback != null)
                  paramFontCallback.callbackFailAsync(-3, paramHandler); 
                return null;
              } catch (XmlPullParserException xmlPullParserException) {
              
              } catch (IOException null) {}
            } else {
              try {
                return TypefaceCompat.createFromResourcesFamilyXml((Context)iOException, familyResourceEntry, paramResources, paramInt1, paramInt2, paramFontCallback, paramHandler, paramBoolean1);
              } catch (XmlPullParserException xmlPullParserException) {
              
              } catch (IOException iOException1) {}
            } 
          } catch (XmlPullParserException xmlPullParserException) {
          
          } catch (IOException null) {}
        } else {
          try {
            Typeface typeface1 = TypefaceCompat.createFromResourcesFontFile((Context)iOException, paramResources, paramInt1, str, paramInt2);
            if (paramFontCallback != null)
              if (typeface1 != null) {
                try {
                  paramFontCallback.callbackSuccessAsync(typeface1, paramHandler);
                } catch (XmlPullParserException xmlPullParserException) {
                
                } catch (IOException iOException1) {}
              } else {
                paramFontCallback.callbackFailAsync(-3, paramHandler);
              }  
            return (Typeface)iOException1;
          } catch (XmlPullParserException xmlPullParserException) {
          
          } catch (IOException iOException1) {}
        } 
      } catch (XmlPullParserException xmlPullParserException) {
      
      } catch (IOException iOException) {
        Log.e("ResourcesCompat", "Failed to read xml resource " + str, iOException);
      } 
      Log.e("ResourcesCompat", "Failed to parse xml resource " + str, iOException);
      if (paramFontCallback != null)
        paramFontCallback.callbackFailAsync(-3, paramHandler); 
      return null;
    } 
    throw new Resources.NotFoundException("Resource \"" + paramResources.getResourceName(paramInt1) + "\" (" + Integer.toHexString(paramInt1) + ") is not a Font: " + str);
  }
  
  static class Api23Impl {
    static ColorStateList getColorStateList(Resources param1Resources, int param1Int, Resources.Theme param1Theme) {
      return param1Resources.getColorStateList(param1Int, param1Theme);
    }
  }
  
  private static class ColorStateListCacheEntry {
    final Configuration mConfiguration;
    
    final ColorStateList mValue;
    
    ColorStateListCacheEntry(ColorStateList param1ColorStateList, Configuration param1Configuration) {
      this.mValue = param1ColorStateList;
      this.mConfiguration = param1Configuration;
    }
  }
  
  private static final class ColorStateListCacheKey {
    final Resources mResources;
    
    final Resources.Theme mTheme;
    
    ColorStateListCacheKey(Resources param1Resources, Resources.Theme param1Theme) {
      this.mResources = param1Resources;
      this.mTheme = param1Theme;
    }
    
    public boolean equals(Object param1Object) {
      boolean bool = true;
      if (this == param1Object)
        return true; 
      if (param1Object == null || getClass() != param1Object.getClass())
        return false; 
      param1Object = param1Object;
      if (!this.mResources.equals(((ColorStateListCacheKey)param1Object).mResources) || !ObjectsCompat.equals(this.mTheme, ((ColorStateListCacheKey)param1Object).mTheme))
        bool = false; 
      return bool;
    }
    
    public int hashCode() {
      return ObjectsCompat.hash(new Object[] { this.mResources, this.mTheme });
    }
  }
  
  public static abstract class FontCallback {
    public static Handler getHandler(Handler param1Handler) {
      if (param1Handler == null)
        param1Handler = new Handler(Looper.getMainLooper()); 
      return param1Handler;
    }
    
    public final void callbackFailAsync(final int reason, Handler param1Handler) {
      getHandler(param1Handler).post(new Runnable() {
            final ResourcesCompat.FontCallback this$0;
            
            final int val$reason;
            
            public void run() {
              ResourcesCompat.FontCallback.this.onFontRetrievalFailed(reason);
            }
          });
    }
    
    public final void callbackSuccessAsync(final Typeface typeface, Handler param1Handler) {
      getHandler(param1Handler).post(new Runnable() {
            final ResourcesCompat.FontCallback this$0;
            
            final Typeface val$typeface;
            
            public void run() {
              ResourcesCompat.FontCallback.this.onFontRetrieved(typeface);
            }
          });
    }
    
    public abstract void onFontRetrievalFailed(int param1Int);
    
    public abstract void onFontRetrieved(Typeface param1Typeface);
  }
  
  class null implements Runnable {
    final ResourcesCompat.FontCallback this$0;
    
    final Typeface val$typeface;
    
    public void run() {
      this.this$0.onFontRetrieved(typeface);
    }
  }
  
  class null implements Runnable {
    final ResourcesCompat.FontCallback this$0;
    
    final int val$reason;
    
    public void run() {
      this.this$0.onFontRetrievalFailed(reason);
    }
  }
  
  static class ImplApi29 {
    static float getFloat(Resources param1Resources, int param1Int) {
      return param1Resources.getFloat(param1Int);
    }
  }
  
  public static final class ThemeCompat {
    public static void rebase(Resources.Theme param1Theme) {
      if (Build.VERSION.SDK_INT >= 29) {
        ImplApi29.rebase(param1Theme);
      } else if (Build.VERSION.SDK_INT >= 23) {
        ImplApi23.rebase(param1Theme);
      } 
    }
    
    static class ImplApi23 {
      private static Method sRebaseMethod;
      
      private static boolean sRebaseMethodFetched;
      
      private static final Object sRebaseMethodLock = new Object();
      
      static void rebase(Resources.Theme param2Theme) {
        synchronized (sRebaseMethodLock) {
          boolean bool = sRebaseMethodFetched;
          if (!bool) {
            try {
              Method method1 = Resources.Theme.class.getDeclaredMethod("rebase", new Class[0]);
              sRebaseMethod = method1;
              method1.setAccessible(true);
            } catch (NoSuchMethodException noSuchMethodException) {
              Log.i("ResourcesCompat", "Failed to retrieve rebase() method", noSuchMethodException);
            } 
            sRebaseMethodFetched = true;
          } 
          Method method = sRebaseMethod;
          if (method != null)
            try {
              method.invoke(param2Theme, new Object[0]);
            } catch (IllegalAccessException|java.lang.reflect.InvocationTargetException illegalAccessException) {
              Log.i("ResourcesCompat", "Failed to invoke rebase() method via reflection", illegalAccessException);
              sRebaseMethod = null;
            }  
          return;
        } 
      }
    }
    
    static class ImplApi29 {
      static void rebase(Resources.Theme param2Theme) {
        param2Theme.rebase();
      }
    }
  }
  
  static class ImplApi23 {
    private static Method sRebaseMethod;
    
    private static boolean sRebaseMethodFetched;
    
    private static final Object sRebaseMethodLock = new Object();
    
    static void rebase(Resources.Theme param1Theme) {
      synchronized (sRebaseMethodLock) {
        boolean bool = sRebaseMethodFetched;
        if (!bool) {
          try {
            Method method1 = Resources.Theme.class.getDeclaredMethod("rebase", new Class[0]);
            sRebaseMethod = method1;
            method1.setAccessible(true);
          } catch (NoSuchMethodException noSuchMethodException) {
            Log.i("ResourcesCompat", "Failed to retrieve rebase() method", noSuchMethodException);
          } 
          sRebaseMethodFetched = true;
        } 
        Method method = sRebaseMethod;
        if (method != null)
          try {
            method.invoke(param1Theme, new Object[0]);
          } catch (IllegalAccessException|java.lang.reflect.InvocationTargetException illegalAccessException) {
            Log.i("ResourcesCompat", "Failed to invoke rebase() method via reflection", illegalAccessException);
            sRebaseMethod = null;
          }  
        return;
      } 
    }
  }
  
  static class ImplApi29 {
    static void rebase(Resources.Theme param1Theme) {
      param1Theme.rebase();
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\content\res\ResourcesCompat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */