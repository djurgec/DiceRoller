package androidx.emoji2.text;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.os.Build;
import android.util.Log;
import androidx.core.provider.FontRequest;
import androidx.core.util.Preconditions;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class DefaultEmojiCompatConfig {
  public static FontRequestEmojiCompatConfig create(Context paramContext) {
    return (FontRequestEmojiCompatConfig)(new DefaultEmojiCompatConfigFactory(null)).create(paramContext);
  }
  
  public static class DefaultEmojiCompatConfigFactory {
    private static final String DEFAULT_EMOJI_QUERY = "emojicompat-emoji-font";
    
    private static final String INTENT_LOAD_EMOJI_FONT = "androidx.content.action.LOAD_EMOJI_FONT";
    
    private static final String TAG = "emoji2.text.DefaultEmojiConfig";
    
    private final DefaultEmojiCompatConfig.DefaultEmojiCompatConfigHelper mHelper;
    
    public DefaultEmojiCompatConfigFactory(DefaultEmojiCompatConfig.DefaultEmojiCompatConfigHelper param1DefaultEmojiCompatConfigHelper) {
      if (param1DefaultEmojiCompatConfigHelper == null)
        param1DefaultEmojiCompatConfigHelper = getHelperForApi(); 
      this.mHelper = param1DefaultEmojiCompatConfigHelper;
    }
    
    private EmojiCompat.Config configOrNull(Context param1Context, FontRequest param1FontRequest) {
      return (param1FontRequest == null) ? null : new FontRequestEmojiCompatConfig(param1Context, param1FontRequest);
    }
    
    private List<List<byte[]>> convertToByteArray(Signature[] param1ArrayOfSignature) {
      ArrayList<byte[]> arrayList = new ArrayList();
      int i = param1ArrayOfSignature.length;
      for (byte b = 0; b < i; b++)
        arrayList.add(param1ArrayOfSignature[b].toByteArray()); 
      return (List)Collections.singletonList(arrayList);
    }
    
    private FontRequest generateFontRequestFrom(ProviderInfo param1ProviderInfo, PackageManager param1PackageManager) throws PackageManager.NameNotFoundException {
      String str2 = param1ProviderInfo.authority;
      String str1 = param1ProviderInfo.packageName;
      return new FontRequest(str2, str1, "emojicompat-emoji-font", convertToByteArray(this.mHelper.getSigningSignatures(param1PackageManager, str1)));
    }
    
    private static DefaultEmojiCompatConfig.DefaultEmojiCompatConfigHelper getHelperForApi() {
      return (Build.VERSION.SDK_INT >= 28) ? new DefaultEmojiCompatConfig.DefaultEmojiCompatConfigHelper_API28() : ((Build.VERSION.SDK_INT >= 19) ? new DefaultEmojiCompatConfig.DefaultEmojiCompatConfigHelper_API19() : new DefaultEmojiCompatConfig.DefaultEmojiCompatConfigHelper());
    }
    
    private boolean hasFlagSystem(ProviderInfo param1ProviderInfo) {
      boolean bool = true;
      if (param1ProviderInfo == null || param1ProviderInfo.applicationInfo == null || (param1ProviderInfo.applicationInfo.flags & 0x1) != 1)
        bool = false; 
      return bool;
    }
    
    private ProviderInfo queryDefaultInstalledContentProvider(PackageManager param1PackageManager) {
      for (ResolveInfo resolveInfo : this.mHelper.queryIntentContentProviders(param1PackageManager, new Intent("androidx.content.action.LOAD_EMOJI_FONT"), 0)) {
        ProviderInfo providerInfo = this.mHelper.getProviderInfo(resolveInfo);
        if (hasFlagSystem(providerInfo))
          return providerInfo; 
      } 
      return null;
    }
    
    public EmojiCompat.Config create(Context param1Context) {
      return configOrNull(param1Context, queryForDefaultFontRequest(param1Context));
    }
    
    FontRequest queryForDefaultFontRequest(Context param1Context) {
      PackageManager packageManager = param1Context.getPackageManager();
      Preconditions.checkNotNull(packageManager, "Package manager required to locate emoji font provider");
      ProviderInfo providerInfo = queryDefaultInstalledContentProvider(packageManager);
      if (providerInfo == null)
        return null; 
      try {
        return generateFontRequestFrom(providerInfo, packageManager);
      } catch (android.content.pm.PackageManager.NameNotFoundException nameNotFoundException) {
        Log.wtf("emoji2.text.DefaultEmojiConfig", (Throwable)nameNotFoundException);
        return null;
      } 
    }
  }
  
  public static class DefaultEmojiCompatConfigHelper {
    public ProviderInfo getProviderInfo(ResolveInfo param1ResolveInfo) {
      throw new IllegalStateException("Unable to get provider info prior to API 19");
    }
    
    public Signature[] getSigningSignatures(PackageManager param1PackageManager, String param1String) throws PackageManager.NameNotFoundException {
      return (param1PackageManager.getPackageInfo(param1String, 64)).signatures;
    }
    
    public List<ResolveInfo> queryIntentContentProviders(PackageManager param1PackageManager, Intent param1Intent, int param1Int) {
      return Collections.emptyList();
    }
  }
  
  public static class DefaultEmojiCompatConfigHelper_API19 extends DefaultEmojiCompatConfigHelper {
    public ProviderInfo getProviderInfo(ResolveInfo param1ResolveInfo) {
      return param1ResolveInfo.providerInfo;
    }
    
    public List<ResolveInfo> queryIntentContentProviders(PackageManager param1PackageManager, Intent param1Intent, int param1Int) {
      return param1PackageManager.queryIntentContentProviders(param1Intent, param1Int);
    }
  }
  
  public static class DefaultEmojiCompatConfigHelper_API28 extends DefaultEmojiCompatConfigHelper_API19 {
    public Signature[] getSigningSignatures(PackageManager param1PackageManager, String param1String) throws PackageManager.NameNotFoundException {
      return (param1PackageManager.getPackageInfo(param1String, 64)).signatures;
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\emoji2\text\DefaultEmojiCompatConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */