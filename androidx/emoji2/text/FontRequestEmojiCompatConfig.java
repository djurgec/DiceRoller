package androidx.emoji2.text;

import android.content.Context;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Handler;
import android.os.SystemClock;
import androidx.core.provider.FontRequest;
import androidx.core.provider.FontsContractCompat;
import androidx.core.util.Preconditions;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

public class FontRequestEmojiCompatConfig extends EmojiCompat.Config {
  private static final FontProviderHelper DEFAULT_FONTS_CONTRACT = new FontProviderHelper();
  
  public FontRequestEmojiCompatConfig(Context paramContext, FontRequest paramFontRequest) {
    super(new FontRequestMetadataLoader(paramContext, paramFontRequest, DEFAULT_FONTS_CONTRACT));
  }
  
  public FontRequestEmojiCompatConfig(Context paramContext, FontRequest paramFontRequest, FontProviderHelper paramFontProviderHelper) {
    super(new FontRequestMetadataLoader(paramContext, paramFontRequest, paramFontProviderHelper));
  }
  
  @Deprecated
  public FontRequestEmojiCompatConfig setHandler(Handler paramHandler) {
    if (paramHandler == null)
      return this; 
    setLoadingExecutor(ConcurrencyHelpers.convertHandlerToExecutor(paramHandler));
    return this;
  }
  
  public FontRequestEmojiCompatConfig setLoadingExecutor(Executor paramExecutor) {
    ((FontRequestMetadataLoader)getMetadataRepoLoader()).setExecutor(paramExecutor);
    return this;
  }
  
  public FontRequestEmojiCompatConfig setRetryPolicy(RetryPolicy paramRetryPolicy) {
    ((FontRequestMetadataLoader)getMetadataRepoLoader()).setRetryPolicy(paramRetryPolicy);
    return this;
  }
  
  public static class ExponentialBackoffRetryPolicy extends RetryPolicy {
    private long mRetryOrigin;
    
    private final long mTotalMs;
    
    public ExponentialBackoffRetryPolicy(long param1Long) {
      this.mTotalMs = param1Long;
    }
    
    public long getRetryDelay() {
      if (this.mRetryOrigin == 0L) {
        this.mRetryOrigin = SystemClock.uptimeMillis();
        return 0L;
      } 
      long l = SystemClock.uptimeMillis() - this.mRetryOrigin;
      return (l > this.mTotalMs) ? -1L : Math.min(Math.max(l, 1000L), this.mTotalMs - l);
    }
  }
  
  public static class FontProviderHelper {
    public Typeface buildTypeface(Context param1Context, FontsContractCompat.FontInfo param1FontInfo) throws PackageManager.NameNotFoundException {
      return FontsContractCompat.buildTypeface(param1Context, null, new FontsContractCompat.FontInfo[] { param1FontInfo });
    }
    
    public FontsContractCompat.FontFamilyResult fetchFonts(Context param1Context, FontRequest param1FontRequest) throws PackageManager.NameNotFoundException {
      return FontsContractCompat.fetchFonts(param1Context, null, param1FontRequest);
    }
    
    public void registerObserver(Context param1Context, Uri param1Uri, ContentObserver param1ContentObserver) {
      param1Context.getContentResolver().registerContentObserver(param1Uri, false, param1ContentObserver);
    }
    
    public void unregisterObserver(Context param1Context, ContentObserver param1ContentObserver) {
      param1Context.getContentResolver().unregisterContentObserver(param1ContentObserver);
    }
  }
  
  private static class FontRequestMetadataLoader implements EmojiCompat.MetadataRepoLoader {
    private static final String S_TRACE_BUILD_TYPEFACE = "EmojiCompat.FontRequestEmojiCompatConfig.buildTypeface";
    
    EmojiCompat.MetadataRepoLoaderCallback mCallback;
    
    private final Context mContext;
    
    private Executor mExecutor;
    
    private final FontRequestEmojiCompatConfig.FontProviderHelper mFontProviderHelper;
    
    private final Object mLock = new Object();
    
    private Handler mMainHandler;
    
    private Runnable mMainHandlerLoadCallback;
    
    private ThreadPoolExecutor mMyThreadPoolExecutor;
    
    private ContentObserver mObserver;
    
    private final FontRequest mRequest;
    
    private FontRequestEmojiCompatConfig.RetryPolicy mRetryPolicy;
    
    FontRequestMetadataLoader(Context param1Context, FontRequest param1FontRequest, FontRequestEmojiCompatConfig.FontProviderHelper param1FontProviderHelper) {
      Preconditions.checkNotNull(param1Context, "Context cannot be null");
      Preconditions.checkNotNull(param1FontRequest, "FontRequest cannot be null");
      this.mContext = param1Context.getApplicationContext();
      this.mRequest = param1FontRequest;
      this.mFontProviderHelper = param1FontProviderHelper;
    }
    
    private void cleanUp() {
      synchronized (this.mLock) {
        this.mCallback = null;
        ContentObserver contentObserver = this.mObserver;
        if (contentObserver != null) {
          this.mFontProviderHelper.unregisterObserver(this.mContext, contentObserver);
          this.mObserver = null;
        } 
        Handler handler = this.mMainHandler;
        if (handler != null)
          handler.removeCallbacks(this.mMainHandlerLoadCallback); 
        this.mMainHandler = null;
        ThreadPoolExecutor threadPoolExecutor = this.mMyThreadPoolExecutor;
        if (threadPoolExecutor != null)
          threadPoolExecutor.shutdown(); 
        this.mExecutor = null;
        this.mMyThreadPoolExecutor = null;
        return;
      } 
    }
    
    private FontsContractCompat.FontInfo retrieveFontInfo() {
      try {
        FontsContractCompat.FontInfo[] arrayOfFontInfo;
        FontsContractCompat.FontFamilyResult fontFamilyResult = this.mFontProviderHelper.fetchFonts(this.mContext, this.mRequest);
        if (fontFamilyResult.getStatusCode() == 0) {
          arrayOfFontInfo = fontFamilyResult.getFonts();
          if (arrayOfFontInfo != null && arrayOfFontInfo.length != 0)
            return arrayOfFontInfo[0]; 
          throw new RuntimeException("fetchFonts failed (empty result)");
        } 
        throw new RuntimeException("fetchFonts failed (" + arrayOfFontInfo.getStatusCode() + ")");
      } catch (android.content.pm.PackageManager.NameNotFoundException nameNotFoundException) {
        throw new RuntimeException("provider not found", nameNotFoundException);
      } 
    }
    
    private void scheduleRetry(Uri param1Uri, long param1Long) {
      synchronized (this.mLock) {
        Handler handler2 = this.mMainHandler;
        Handler handler1 = handler2;
        if (handler2 == null) {
          handler1 = ConcurrencyHelpers.mainHandlerAsync();
          this.mMainHandler = handler1;
        } 
        if (this.mObserver == null) {
          ContentObserver contentObserver = new ContentObserver() {
              final FontRequestEmojiCompatConfig.FontRequestMetadataLoader this$0;
              
              public void onChange(boolean param2Boolean, Uri param2Uri) {
                FontRequestEmojiCompatConfig.FontRequestMetadataLoader.this.loadInternal();
              }
            };
          super(this, handler1);
          this.mObserver = contentObserver;
          this.mFontProviderHelper.registerObserver(this.mContext, param1Uri, contentObserver);
        } 
        if (this.mMainHandlerLoadCallback == null) {
          FontRequestEmojiCompatConfig$FontRequestMetadataLoader$$ExternalSyntheticLambda1 fontRequestEmojiCompatConfig$FontRequestMetadataLoader$$ExternalSyntheticLambda1 = new FontRequestEmojiCompatConfig$FontRequestMetadataLoader$$ExternalSyntheticLambda1();
          this(this);
          this.mMainHandlerLoadCallback = fontRequestEmojiCompatConfig$FontRequestMetadataLoader$$ExternalSyntheticLambda1;
        } 
        handler1.postDelayed(this.mMainHandlerLoadCallback, param1Long);
        return;
      } 
    }
    
    void createMetadata() {
      synchronized (this.mLock) {
        if (this.mCallback == null)
          return; 
        try {
          FontsContractCompat.FontInfo fontInfo = retrieveFontInfo();
          int i = fontInfo.getResultCode();
          if (i == 2)
            synchronized (this.mLock) {
              FontRequestEmojiCompatConfig.RetryPolicy retryPolicy = this.mRetryPolicy;
              if (retryPolicy != null) {
                long l = retryPolicy.getRetryDelay();
                if (l >= 0L)
                  return; 
              } 
            }  
        } finally {
          Exception exception = null;
        } 
        return;
      } 
    }
    
    public void load(EmojiCompat.MetadataRepoLoaderCallback param1MetadataRepoLoaderCallback) {
      Preconditions.checkNotNull(param1MetadataRepoLoaderCallback, "LoaderCallback cannot be null");
      synchronized (this.mLock) {
        this.mCallback = param1MetadataRepoLoaderCallback;
        loadInternal();
        return;
      } 
    }
    
    void loadInternal() {
      synchronized (this.mLock) {
        if (this.mCallback == null)
          return; 
        if (this.mExecutor == null) {
          ThreadPoolExecutor threadPoolExecutor = ConcurrencyHelpers.createBackgroundPriorityExecutor("emojiCompat");
          this.mMyThreadPoolExecutor = threadPoolExecutor;
          this.mExecutor = threadPoolExecutor;
        } 
        Executor executor = this.mExecutor;
        FontRequestEmojiCompatConfig$FontRequestMetadataLoader$$ExternalSyntheticLambda0 fontRequestEmojiCompatConfig$FontRequestMetadataLoader$$ExternalSyntheticLambda0 = new FontRequestEmojiCompatConfig$FontRequestMetadataLoader$$ExternalSyntheticLambda0();
        this(this);
        executor.execute(fontRequestEmojiCompatConfig$FontRequestMetadataLoader$$ExternalSyntheticLambda0);
        return;
      } 
    }
    
    public void setExecutor(Executor param1Executor) {
      synchronized (this.mLock) {
        this.mExecutor = param1Executor;
        return;
      } 
    }
    
    public void setRetryPolicy(FontRequestEmojiCompatConfig.RetryPolicy param1RetryPolicy) {
      synchronized (this.mLock) {
        this.mRetryPolicy = param1RetryPolicy;
        return;
      } 
    }
  }
  
  class null extends ContentObserver {
    final FontRequestEmojiCompatConfig.FontRequestMetadataLoader this$0;
    
    null(Handler param1Handler) {
      super(param1Handler);
    }
    
    public void onChange(boolean param1Boolean, Uri param1Uri) {
      this.this$0.loadInternal();
    }
  }
  
  public static abstract class RetryPolicy {
    public abstract long getRetryDelay();
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\emoji2\text\FontRequestEmojiCompatConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */