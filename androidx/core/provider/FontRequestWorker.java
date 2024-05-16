package androidx.core.provider;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import androidx.collection.LruCache;
import androidx.collection.SimpleArrayMap;
import androidx.core.graphics.TypefaceCompat;
import androidx.core.util.Consumer;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

class FontRequestWorker {
  private static final ExecutorService DEFAULT_EXECUTOR_SERVICE;
  
  static final Object LOCK;
  
  static final SimpleArrayMap<String, ArrayList<Consumer<TypefaceResult>>> PENDING_REPLIES;
  
  static final LruCache<String, Typeface> sTypefaceCache = new LruCache(16);
  
  static {
    DEFAULT_EXECUTOR_SERVICE = RequestExecutor.createDefaultExecutor("fonts-androidx", 10, 10000);
    LOCK = new Object();
    PENDING_REPLIES = new SimpleArrayMap();
  }
  
  private static String createCacheId(FontRequest paramFontRequest, int paramInt) {
    return paramFontRequest.getId() + "-" + paramInt;
  }
  
  private static int getFontFamilyResultStatus(FontsContractCompat.FontFamilyResult paramFontFamilyResult) {
    int i = paramFontFamilyResult.getStatusCode();
    byte b = -3;
    if (i != 0) {
      switch (paramFontFamilyResult.getStatusCode()) {
        default:
          return -3;
        case 1:
          break;
      } 
      return -2;
    } 
    FontsContractCompat.FontInfo[] arrayOfFontInfo = paramFontFamilyResult.getFonts();
    if (arrayOfFontInfo == null || arrayOfFontInfo.length == 0)
      return 1; 
    int j = arrayOfFontInfo.length;
    for (i = 0; i < j; i++) {
      int k = arrayOfFontInfo[i].getResultCode();
      if (k != 0) {
        if (k < 0) {
          i = b;
        } else {
          i = k;
        } 
        return i;
      } 
    } 
    return 0;
  }
  
  static TypefaceResult getFontSync(String paramString, Context paramContext, FontRequest paramFontRequest, int paramInt) {
    LruCache<String, Typeface> lruCache = sTypefaceCache;
    Typeface typeface = (Typeface)lruCache.get(paramString);
    if (typeface != null)
      return new TypefaceResult(typeface); 
    try {
      FontsContractCompat.FontFamilyResult fontFamilyResult = FontProvider.getFontFamilyResult(paramContext, paramFontRequest, null);
      int i = getFontFamilyResultStatus(fontFamilyResult);
      if (i != 0)
        return new TypefaceResult(i); 
      Typeface typeface1 = TypefaceCompat.createFromFontInfo(paramContext, null, fontFamilyResult.getFonts(), paramInt);
      if (typeface1 != null) {
        lruCache.put(paramString, typeface1);
        return new TypefaceResult(typeface1);
      } 
      return new TypefaceResult(-3);
    } catch (android.content.pm.PackageManager.NameNotFoundException nameNotFoundException) {
      return new TypefaceResult(-1);
    } 
  }
  
  static Typeface requestFontAsync(final Context context, final FontRequest request, final int style, Executor paramExecutor, final CallbackWithHandler callback) {
    final String id = createCacheId(request, style);
    Typeface typeface = (Typeface)sTypefaceCache.get(str);
    if (typeface != null) {
      callback.onTypefaceResult(new TypefaceResult(typeface));
      return typeface;
    } 
    Consumer<TypefaceResult> consumer = new Consumer<TypefaceResult>() {
        final CallbackWithHandler val$callback;
        
        public void accept(FontRequestWorker.TypefaceResult param1TypefaceResult) {
          FontRequestWorker.TypefaceResult typefaceResult = param1TypefaceResult;
          if (param1TypefaceResult == null)
            typefaceResult = new FontRequestWorker.TypefaceResult(-3); 
          callback.onTypefaceResult(typefaceResult);
        }
      };
    synchronized (LOCK) {
      Executor executor;
      SimpleArrayMap<String, ArrayList<Consumer<TypefaceResult>>> simpleArrayMap = PENDING_REPLIES;
      ArrayList<Consumer<TypefaceResult>> arrayList = (ArrayList)simpleArrayMap.get(str);
      if (arrayList != null) {
        arrayList.add(consumer);
        return null;
      } 
      arrayList = new ArrayList<>();
      this();
      arrayList.add(consumer);
      simpleArrayMap.put(str, arrayList);
      Callable<TypefaceResult> callable = new Callable<TypefaceResult>() {
          final Context val$context;
          
          final String val$id;
          
          final FontRequest val$request;
          
          final int val$style;
          
          public FontRequestWorker.TypefaceResult call() {
            try {
              return FontRequestWorker.getFontSync(id, context, request, style);
            } finally {
              Exception exception = null;
            } 
          }
        };
      if (paramExecutor == null) {
        executor = DEFAULT_EXECUTOR_SERVICE;
      } else {
        executor = paramExecutor;
      } 
      RequestExecutor.execute(executor, callable, new Consumer<TypefaceResult>() {
            final String val$id;
            
            public void accept(FontRequestWorker.TypefaceResult param1TypefaceResult) {
              synchronized (FontRequestWorker.LOCK) {
                ArrayList<Consumer> arrayList = (ArrayList)FontRequestWorker.PENDING_REPLIES.get(id);
                if (arrayList == null)
                  return; 
                FontRequestWorker.PENDING_REPLIES.remove(id);
                for (byte b = 0; b < arrayList.size(); b++)
                  ((Consumer)arrayList.get(b)).accept(param1TypefaceResult); 
                return;
              } 
            }
          });
      return null;
    } 
  }
  
  static Typeface requestFontSync(Context paramContext, final FontRequest request, CallbackWithHandler paramCallbackWithHandler, final int style, int paramInt2) {
    final TypefaceResult context;
    final String id = createCacheId(request, style);
    Typeface typeface = (Typeface)sTypefaceCache.get(str);
    if (typeface != null) {
      paramCallbackWithHandler.onTypefaceResult(new TypefaceResult(typeface));
      return typeface;
    } 
    if (paramInt2 == -1) {
      typefaceResult = getFontSync(str, paramContext, request, style);
      paramCallbackWithHandler.onTypefaceResult(typefaceResult);
      return typefaceResult.mTypeface;
    } 
    Callable<TypefaceResult> callable = new Callable<TypefaceResult>() {
        final Context val$context;
        
        final String val$id;
        
        final FontRequest val$request;
        
        final int val$style;
        
        public FontRequestWorker.TypefaceResult call() {
          return FontRequestWorker.getFontSync(id, context, request, style);
        }
      };
    try {
      TypefaceResult typefaceResult1 = RequestExecutor.<TypefaceResult>submit(DEFAULT_EXECUTOR_SERVICE, callable, paramInt2);
      paramCallbackWithHandler.onTypefaceResult(typefaceResult1);
      return typefaceResult1.mTypeface;
    } catch (InterruptedException interruptedException) {
      paramCallbackWithHandler.onTypefaceResult(new TypefaceResult(-3));
      return null;
    } 
  }
  
  static void resetTypefaceCache() {
    sTypefaceCache.evictAll();
  }
  
  static final class TypefaceResult {
    final int mResult;
    
    final Typeface mTypeface = null;
    
    TypefaceResult(int param1Int) {
      this.mResult = param1Int;
    }
    
    TypefaceResult(Typeface param1Typeface) {
      this.mResult = 0;
    }
    
    boolean isSuccess() {
      boolean bool;
      if (this.mResult == 0) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\provider\FontRequestWorker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */