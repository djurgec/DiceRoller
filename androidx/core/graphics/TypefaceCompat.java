package androidx.core.graphics;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Build;
import android.os.CancellationSignal;
import android.os.Handler;
import androidx.collection.LruCache;
import androidx.core.content.res.FontResourcesParserCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.provider.FontsContractCompat;

public class TypefaceCompat {
  private static final LruCache<String, Typeface> sTypefaceCache = new LruCache(16);
  
  private static final TypefaceCompatBaseImpl sTypefaceCompatImpl;
  
  public static void clearCache() {
    sTypefaceCache.evictAll();
  }
  
  public static Typeface create(Context paramContext, Typeface paramTypeface, int paramInt) {
    if (paramContext != null) {
      if (Build.VERSION.SDK_INT < 21) {
        Typeface typeface = getBestFontFromFamily(paramContext, paramTypeface, paramInt);
        if (typeface != null)
          return typeface; 
      } 
      return Typeface.create(paramTypeface, paramInt);
    } 
    throw new IllegalArgumentException("Context cannot be null");
  }
  
  public static Typeface createFromFontInfo(Context paramContext, CancellationSignal paramCancellationSignal, FontsContractCompat.FontInfo[] paramArrayOfFontInfo, int paramInt) {
    return sTypefaceCompatImpl.createFromFontInfo(paramContext, paramCancellationSignal, paramArrayOfFontInfo, paramInt);
  }
  
  public static Typeface createFromResourcesFamilyXml(Context paramContext, FontResourcesParserCompat.FamilyResourceEntry paramFamilyResourceEntry, Resources paramResources, int paramInt1, int paramInt2, ResourcesCompat.FontCallback paramFontCallback, Handler paramHandler, boolean paramBoolean) {
    Typeface typeface;
    FontResourcesParserCompat.ProviderResourceEntry providerResourceEntry;
    ResourcesCallbackAdapter resourcesCallbackAdapter;
    if (paramFamilyResourceEntry instanceof FontResourcesParserCompat.ProviderResourceEntry) {
      byte b;
      boolean bool;
      providerResourceEntry = (FontResourcesParserCompat.ProviderResourceEntry)paramFamilyResourceEntry;
      Typeface typeface1 = getSystemFontFamily(providerResourceEntry.getSystemFontFamilyName());
      if (typeface1 != null) {
        if (paramFontCallback != null)
          paramFontCallback.callbackSuccessAsync(typeface1, paramHandler); 
        return typeface1;
      } 
      if (paramBoolean) {
        if (providerResourceEntry.getFetchStrategy() == 0) {
          bool = true;
        } else {
          bool = false;
        } 
      } else if (paramFontCallback == null) {
        bool = true;
      } else {
        bool = false;
      } 
      if (paramBoolean) {
        b = providerResourceEntry.getTimeout();
      } else {
        b = -1;
      } 
      paramHandler = ResourcesCompat.FontCallback.getHandler(paramHandler);
      resourcesCallbackAdapter = new ResourcesCallbackAdapter(paramFontCallback);
      typeface = FontsContractCompat.requestFont(paramContext, providerResourceEntry.getRequest(), paramInt2, bool, b, paramHandler, resourcesCallbackAdapter);
    } else {
      Typeface typeface1 = sTypefaceCompatImpl.createFromFontFamilyFilesResourceEntry((Context)typeface, (FontResourcesParserCompat.FontFamilyFilesResourceEntry)providerResourceEntry, paramResources, paramInt2);
      typeface = typeface1;
      if (resourcesCallbackAdapter != null)
        if (typeface1 != null) {
          resourcesCallbackAdapter.callbackSuccessAsync(typeface1, paramHandler);
          typeface = typeface1;
        } else {
          resourcesCallbackAdapter.callbackFailAsync(-3, paramHandler);
          typeface = typeface1;
        }  
    } 
    if (typeface != null)
      sTypefaceCache.put(createResourceUid(paramResources, paramInt1, paramInt2), typeface); 
    return typeface;
  }
  
  public static Typeface createFromResourcesFontFile(Context paramContext, Resources paramResources, int paramInt1, String paramString, int paramInt2) {
    Typeface typeface = sTypefaceCompatImpl.createFromResourcesFontFile(paramContext, paramResources, paramInt1, paramString, paramInt2);
    if (typeface != null) {
      String str = createResourceUid(paramResources, paramInt1, paramInt2);
      sTypefaceCache.put(str, typeface);
    } 
    return typeface;
  }
  
  private static String createResourceUid(Resources paramResources, int paramInt1, int paramInt2) {
    return paramResources.getResourcePackageName(paramInt1) + "-" + paramInt1 + "-" + paramInt2;
  }
  
  public static Typeface findFromCache(Resources paramResources, int paramInt1, int paramInt2) {
    return (Typeface)sTypefaceCache.get(createResourceUid(paramResources, paramInt1, paramInt2));
  }
  
  private static Typeface getBestFontFromFamily(Context paramContext, Typeface paramTypeface, int paramInt) {
    TypefaceCompatBaseImpl typefaceCompatBaseImpl = sTypefaceCompatImpl;
    FontResourcesParserCompat.FontFamilyFilesResourceEntry fontFamilyFilesResourceEntry = typefaceCompatBaseImpl.getFontFamily(paramTypeface);
    return (fontFamilyFilesResourceEntry == null) ? null : typefaceCompatBaseImpl.createFromFontFamilyFilesResourceEntry(paramContext, fontFamilyFilesResourceEntry, paramContext.getResources(), paramInt);
  }
  
  private static Typeface getSystemFontFamily(String paramString) {
    Typeface typeface1;
    String str = null;
    if (paramString == null || paramString.isEmpty())
      return null; 
    Typeface typeface2 = Typeface.create(paramString, 0);
    Typeface typeface3 = Typeface.create(Typeface.DEFAULT, 0);
    paramString = str;
    if (typeface2 != null) {
      paramString = str;
      if (!typeface2.equals(typeface3))
        typeface1 = typeface2; 
    } 
    return typeface1;
  }
  
  static {
    if (Build.VERSION.SDK_INT >= 29) {
      sTypefaceCompatImpl = new TypefaceCompatApi29Impl();
    } else if (Build.VERSION.SDK_INT >= 28) {
      sTypefaceCompatImpl = new TypefaceCompatApi28Impl();
    } else if (Build.VERSION.SDK_INT >= 26) {
      sTypefaceCompatImpl = new TypefaceCompatApi26Impl();
    } else if (Build.VERSION.SDK_INT >= 24 && TypefaceCompatApi24Impl.isUsable()) {
      sTypefaceCompatImpl = new TypefaceCompatApi24Impl();
    } else if (Build.VERSION.SDK_INT >= 21) {
      sTypefaceCompatImpl = new TypefaceCompatApi21Impl();
    } else {
      sTypefaceCompatImpl = new TypefaceCompatBaseImpl();
    } 
  }
  
  public static class ResourcesCallbackAdapter extends FontsContractCompat.FontRequestCallback {
    private ResourcesCompat.FontCallback mFontCallback;
    
    public ResourcesCallbackAdapter(ResourcesCompat.FontCallback param1FontCallback) {
      this.mFontCallback = param1FontCallback;
    }
    
    public void onTypefaceRequestFailed(int param1Int) {
      ResourcesCompat.FontCallback fontCallback = this.mFontCallback;
      if (fontCallback != null)
        fontCallback.onFontRetrievalFailed(param1Int); 
    }
    
    public void onTypefaceRetrieved(Typeface param1Typeface) {
      ResourcesCompat.FontCallback fontCallback = this.mFontCallback;
      if (fontCallback != null)
        fontCallback.onFontRetrieved(param1Typeface); 
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\graphics\TypefaceCompat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */