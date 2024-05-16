package androidx.core.content.pm;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.IconCompat;
import androidx.core.util.Preconditions;
import java.io.InputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ShortcutManagerCompat {
  static final String ACTION_INSTALL_SHORTCUT = "com.android.launcher.action.INSTALL_SHORTCUT";
  
  private static final int DEFAULT_MAX_ICON_DIMENSION_DP = 96;
  
  private static final int DEFAULT_MAX_ICON_DIMENSION_LOWRAM_DP = 48;
  
  public static final String EXTRA_SHORTCUT_ID = "android.intent.extra.shortcut.ID";
  
  public static final int FLAG_MATCH_CACHED = 8;
  
  public static final int FLAG_MATCH_DYNAMIC = 2;
  
  public static final int FLAG_MATCH_MANIFEST = 1;
  
  public static final int FLAG_MATCH_PINNED = 4;
  
  static final String INSTALL_SHORTCUT_PERMISSION = "com.android.launcher.permission.INSTALL_SHORTCUT";
  
  private static final String SHORTCUT_LISTENER_INTENT_FILTER_ACTION = "androidx.core.content.pm.SHORTCUT_LISTENER";
  
  private static final String SHORTCUT_LISTENER_META_DATA_KEY = "androidx.core.content.pm.shortcut_listener_impl";
  
  private static volatile List<ShortcutInfoChangeListener> sShortcutInfoChangeListeners;
  
  private static volatile ShortcutInfoCompatSaver<?> sShortcutInfoCompatSaver = null;
  
  static {
    sShortcutInfoChangeListeners = null;
  }
  
  public static boolean addDynamicShortcuts(Context paramContext, List<ShortcutInfoCompat> paramList) {
    if (Build.VERSION.SDK_INT <= 29)
      convertUriIconsToBitmapIcons(paramContext, paramList); 
    if (Build.VERSION.SDK_INT >= 25) {
      ArrayList<ShortcutInfo> arrayList = new ArrayList();
      Iterator<ShortcutInfoCompat> iterator1 = paramList.iterator();
      while (iterator1.hasNext())
        arrayList.add(((ShortcutInfoCompat)iterator1.next()).toShortcutInfo()); 
      if (!((ShortcutManager)paramContext.getSystemService(ShortcutManager.class)).addDynamicShortcuts(arrayList))
        return false; 
    } 
    getShortcutInfoSaverInstance(paramContext).addShortcuts(paramList);
    Iterator<ShortcutInfoChangeListener> iterator = getShortcutInfoListeners(paramContext).iterator();
    while (iterator.hasNext())
      ((ShortcutInfoChangeListener)iterator.next()).onShortcutAdded(paramList); 
    return true;
  }
  
  static boolean convertUriIconToBitmapIcon(Context paramContext, ShortcutInfoCompat paramShortcutInfoCompat) {
    IconCompat iconCompat;
    if (paramShortcutInfoCompat.mIcon == null)
      return false; 
    int i = paramShortcutInfoCompat.mIcon.mType;
    if (i != 6 && i != 4)
      return true; 
    InputStream inputStream = paramShortcutInfoCompat.mIcon.getUriInputStream(paramContext);
    if (inputStream == null)
      return false; 
    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
    if (bitmap == null)
      return false; 
    if (i == 6) {
      iconCompat = IconCompat.createWithAdaptiveBitmap(bitmap);
    } else {
      iconCompat = IconCompat.createWithBitmap((Bitmap)iconCompat);
    } 
    paramShortcutInfoCompat.mIcon = iconCompat;
    return true;
  }
  
  static void convertUriIconsToBitmapIcons(Context paramContext, List<ShortcutInfoCompat> paramList) {
    for (ShortcutInfoCompat shortcutInfoCompat : new ArrayList(paramList)) {
      if (!convertUriIconToBitmapIcon(paramContext, shortcutInfoCompat))
        paramList.remove(shortcutInfoCompat); 
    } 
  }
  
  public static Intent createShortcutResultIntent(Context paramContext, ShortcutInfoCompat paramShortcutInfoCompat) {
    Intent intent2 = null;
    if (Build.VERSION.SDK_INT >= 26)
      intent2 = ((ShortcutManager)paramContext.getSystemService(ShortcutManager.class)).createShortcutResultIntent(paramShortcutInfoCompat.toShortcutInfo()); 
    Intent intent1 = intent2;
    if (intent2 == null)
      intent1 = new Intent(); 
    return paramShortcutInfoCompat.addToIntent(intent1);
  }
  
  public static void disableShortcuts(Context paramContext, List<String> paramList, CharSequence paramCharSequence) {
    if (Build.VERSION.SDK_INT >= 25)
      ((ShortcutManager)paramContext.getSystemService(ShortcutManager.class)).disableShortcuts(paramList, paramCharSequence); 
    getShortcutInfoSaverInstance(paramContext).removeShortcuts(paramList);
    Iterator<ShortcutInfoChangeListener> iterator = getShortcutInfoListeners(paramContext).iterator();
    while (iterator.hasNext())
      ((ShortcutInfoChangeListener)iterator.next()).onShortcutRemoved(paramList); 
  }
  
  public static void enableShortcuts(Context paramContext, List<ShortcutInfoCompat> paramList) {
    if (Build.VERSION.SDK_INT >= 25) {
      ArrayList<String> arrayList = new ArrayList(paramList.size());
      Iterator<ShortcutInfoCompat> iterator1 = paramList.iterator();
      while (iterator1.hasNext())
        arrayList.add(((ShortcutInfoCompat)iterator1.next()).mId); 
      ((ShortcutManager)paramContext.getSystemService(ShortcutManager.class)).enableShortcuts(arrayList);
    } 
    getShortcutInfoSaverInstance(paramContext).addShortcuts(paramList);
    Iterator<ShortcutInfoChangeListener> iterator = getShortcutInfoListeners(paramContext).iterator();
    while (iterator.hasNext())
      ((ShortcutInfoChangeListener)iterator.next()).onShortcutAdded(paramList); 
  }
  
  public static List<ShortcutInfoCompat> getDynamicShortcuts(Context paramContext) {
    if (Build.VERSION.SDK_INT >= 25) {
      List list = ((ShortcutManager)paramContext.getSystemService(ShortcutManager.class)).getDynamicShortcuts();
      ArrayList<ShortcutInfoCompat> arrayList = new ArrayList(list.size());
      Iterator<ShortcutInfo> iterator = list.iterator();
      while (iterator.hasNext())
        arrayList.add((new ShortcutInfoCompat.Builder(paramContext, iterator.next())).build()); 
      return arrayList;
    } 
    try {
      return getShortcutInfoSaverInstance(paramContext).getShortcuts();
    } catch (Exception exception) {
      return new ArrayList<>();
    } 
  }
  
  private static int getIconDimensionInternal(Context paramContext, boolean paramBoolean) {
    float f;
    ActivityManager activityManager = (ActivityManager)paramContext.getSystemService("activity");
    if (Build.VERSION.SDK_INT < 19 || activityManager == null || activityManager.isLowRamDevice()) {
      i = 1;
    } else {
      i = 0;
    } 
    if (i) {
      i = 48;
    } else {
      i = 96;
    } 
    int i = Math.max(1, i);
    DisplayMetrics displayMetrics = paramContext.getResources().getDisplayMetrics();
    if (paramBoolean) {
      f = displayMetrics.xdpi;
    } else {
      f = displayMetrics.ydpi;
    } 
    f /= 160.0F;
    return (int)(i * f);
  }
  
  public static int getIconMaxHeight(Context paramContext) {
    Preconditions.checkNotNull(paramContext);
    return (Build.VERSION.SDK_INT >= 25) ? ((ShortcutManager)paramContext.getSystemService(ShortcutManager.class)).getIconMaxHeight() : getIconDimensionInternal(paramContext, false);
  }
  
  public static int getIconMaxWidth(Context paramContext) {
    Preconditions.checkNotNull(paramContext);
    return (Build.VERSION.SDK_INT >= 25) ? ((ShortcutManager)paramContext.getSystemService(ShortcutManager.class)).getIconMaxWidth() : getIconDimensionInternal(paramContext, true);
  }
  
  public static int getMaxShortcutCountPerActivity(Context paramContext) {
    Preconditions.checkNotNull(paramContext);
    return (Build.VERSION.SDK_INT >= 25) ? ((ShortcutManager)paramContext.getSystemService(ShortcutManager.class)).getMaxShortcutCountPerActivity() : 5;
  }
  
  static List<ShortcutInfoChangeListener> getShortcutInfoChangeListeners() {
    return sShortcutInfoChangeListeners;
  }
  
  private static String getShortcutInfoCompatWithLowestRank(List<ShortcutInfoCompat> paramList) {
    String str;
    int i = -1;
    List list = null;
    Iterator<ShortcutInfoCompat> iterator = paramList.iterator();
    paramList = list;
    while (iterator.hasNext()) {
      ShortcutInfoCompat shortcutInfoCompat = iterator.next();
      int j = i;
      if (shortcutInfoCompat.getRank() > i) {
        str = shortcutInfoCompat.getId();
        j = shortcutInfoCompat.getRank();
      } 
      i = j;
    } 
    return str;
  }
  
  private static List<ShortcutInfoChangeListener> getShortcutInfoListeners(Context paramContext) {
    if (sShortcutInfoChangeListeners == null) {
      ArrayList<ShortcutInfoChangeListener> arrayList = new ArrayList();
      if (Build.VERSION.SDK_INT >= 21) {
        PackageManager packageManager = paramContext.getPackageManager();
        Intent intent = new Intent("androidx.core.content.pm.SHORTCUT_LISTENER");
        intent.setPackage(paramContext.getPackageName());
        Iterator iterator = packageManager.queryIntentActivities(intent, 128).iterator();
        while (iterator.hasNext()) {
          ActivityInfo activityInfo = ((ResolveInfo)iterator.next()).activityInfo;
          if (activityInfo == null)
            continue; 
          Bundle bundle = activityInfo.metaData;
          if (bundle == null)
            continue; 
          String str = bundle.getString("androidx.core.content.pm.shortcut_listener_impl");
          if (str == null)
            continue; 
          try {
            arrayList.add((ShortcutInfoChangeListener)Class.forName(str, false, ShortcutManagerCompat.class.getClassLoader()).getMethod("getInstance", new Class[] { Context.class }).invoke(null, new Object[] { paramContext }));
          } catch (Exception exception) {}
        } 
      } 
      if (sShortcutInfoChangeListeners == null)
        sShortcutInfoChangeListeners = arrayList; 
    } 
    return sShortcutInfoChangeListeners;
  }
  
  private static ShortcutInfoCompatSaver<?> getShortcutInfoSaverInstance(Context paramContext) {
    if (sShortcutInfoCompatSaver == null) {
      if (Build.VERSION.SDK_INT >= 23)
        try {
          sShortcutInfoCompatSaver = (ShortcutInfoCompatSaver)Class.forName("androidx.sharetarget.ShortcutInfoCompatSaverImpl", false, ShortcutManagerCompat.class.getClassLoader()).getMethod("getInstance", new Class[] { Context.class }).invoke(null, new Object[] { paramContext });
        } catch (Exception exception) {} 
      if (sShortcutInfoCompatSaver == null)
        sShortcutInfoCompatSaver = new ShortcutInfoCompatSaver.NoopImpl(); 
    } 
    return sShortcutInfoCompatSaver;
  }
  
  public static List<ShortcutInfoCompat> getShortcuts(Context paramContext, int paramInt) {
    if (Build.VERSION.SDK_INT >= 30)
      return ShortcutInfoCompat.fromShortcuts(paramContext, ((ShortcutManager)paramContext.getSystemService(ShortcutManager.class)).getShortcuts(paramInt)); 
    if (Build.VERSION.SDK_INT >= 25) {
      ShortcutManager shortcutManager = (ShortcutManager)paramContext.getSystemService(ShortcutManager.class);
      ArrayList<ShortcutInfo> arrayList = new ArrayList();
      if ((paramInt & 0x1) != 0)
        arrayList.addAll(shortcutManager.getManifestShortcuts()); 
      if ((paramInt & 0x2) != 0)
        arrayList.addAll(shortcutManager.getDynamicShortcuts()); 
      if ((paramInt & 0x4) != 0)
        arrayList.addAll(shortcutManager.getPinnedShortcuts()); 
      return ShortcutInfoCompat.fromShortcuts(paramContext, arrayList);
    } 
    if ((paramInt & 0x2) != 0)
      try {
        return getShortcutInfoSaverInstance(paramContext).getShortcuts();
      } catch (Exception exception) {} 
    return Collections.emptyList();
  }
  
  public static boolean isRateLimitingActive(Context paramContext) {
    boolean bool;
    Preconditions.checkNotNull(paramContext);
    if (Build.VERSION.SDK_INT >= 25)
      return ((ShortcutManager)paramContext.getSystemService(ShortcutManager.class)).isRateLimitingActive(); 
    if (getShortcuts(paramContext, 3).size() == getMaxShortcutCountPerActivity(paramContext)) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public static boolean isRequestPinShortcutSupported(Context paramContext) {
    if (Build.VERSION.SDK_INT >= 26)
      return ((ShortcutManager)paramContext.getSystemService(ShortcutManager.class)).isRequestPinShortcutSupported(); 
    if (ContextCompat.checkSelfPermission(paramContext, "com.android.launcher.permission.INSTALL_SHORTCUT") != 0)
      return false; 
    Iterator iterator = paramContext.getPackageManager().queryBroadcastReceivers(new Intent("com.android.launcher.action.INSTALL_SHORTCUT"), 0).iterator();
    while (iterator.hasNext()) {
      String str = ((ResolveInfo)iterator.next()).activityInfo.permission;
      if (TextUtils.isEmpty(str) || "com.android.launcher.permission.INSTALL_SHORTCUT".equals(str))
        return true; 
    } 
    return false;
  }
  
  public static boolean pushDynamicShortcut(Context paramContext, ShortcutInfoCompat paramShortcutInfoCompat) {
    Preconditions.checkNotNull(paramContext);
    Preconditions.checkNotNull(paramShortcutInfoCompat);
    int i = getMaxShortcutCountPerActivity(paramContext);
    if (i == 0)
      return false; 
    if (Build.VERSION.SDK_INT <= 29)
      convertUriIconToBitmapIcon(paramContext, paramShortcutInfoCompat); 
    if (Build.VERSION.SDK_INT >= 30) {
      ((ShortcutManager)paramContext.getSystemService(ShortcutManager.class)).pushDynamicShortcut(paramShortcutInfoCompat.toShortcutInfo());
    } else if (Build.VERSION.SDK_INT >= 25) {
      ShortcutManager shortcutManager = (ShortcutManager)paramContext.getSystemService(ShortcutManager.class);
      if (shortcutManager.isRateLimitingActive())
        return false; 
      List<ShortcutInfo> list = shortcutManager.getDynamicShortcuts();
      if (list.size() >= i)
        shortcutManager.removeDynamicShortcuts(Arrays.asList(new String[] { Api25Impl.getShortcutInfoWithLowestRank(list) })); 
      shortcutManager.addDynamicShortcuts(Arrays.asList(new ShortcutInfo[] { paramShortcutInfoCompat.toShortcutInfo() }));
    } 
    ShortcutInfoCompatSaver<?> shortcutInfoCompatSaver = getShortcutInfoSaverInstance(paramContext);
    try {
      List<ShortcutInfoCompat> list = shortcutInfoCompatSaver.getShortcuts();
      if (list.size() >= i)
        shortcutInfoCompatSaver.removeShortcuts(Arrays.asList(new String[] { getShortcutInfoCompatWithLowestRank(list) })); 
      shortcutInfoCompatSaver.addShortcuts(Arrays.asList(new ShortcutInfoCompat[] { paramShortcutInfoCompat }));
      return true;
    } catch (Exception exception) {
      return false;
    } finally {
      Iterator<ShortcutInfoChangeListener> iterator = getShortcutInfoListeners(paramContext).iterator();
      while (iterator.hasNext())
        ((ShortcutInfoChangeListener)iterator.next()).onShortcutAdded(Collections.singletonList(paramShortcutInfoCompat)); 
      reportShortcutUsed(paramContext, paramShortcutInfoCompat.getId());
    } 
  }
  
  public static void removeAllDynamicShortcuts(Context paramContext) {
    if (Build.VERSION.SDK_INT >= 25)
      ((ShortcutManager)paramContext.getSystemService(ShortcutManager.class)).removeAllDynamicShortcuts(); 
    getShortcutInfoSaverInstance(paramContext).removeAllShortcuts();
    Iterator<ShortcutInfoChangeListener> iterator = getShortcutInfoListeners(paramContext).iterator();
    while (iterator.hasNext())
      ((ShortcutInfoChangeListener)iterator.next()).onAllShortcutsRemoved(); 
  }
  
  public static void removeDynamicShortcuts(Context paramContext, List<String> paramList) {
    if (Build.VERSION.SDK_INT >= 25)
      ((ShortcutManager)paramContext.getSystemService(ShortcutManager.class)).removeDynamicShortcuts(paramList); 
    getShortcutInfoSaverInstance(paramContext).removeShortcuts(paramList);
    Iterator<ShortcutInfoChangeListener> iterator = getShortcutInfoListeners(paramContext).iterator();
    while (iterator.hasNext())
      ((ShortcutInfoChangeListener)iterator.next()).onShortcutRemoved(paramList); 
  }
  
  public static void removeLongLivedShortcuts(Context paramContext, List<String> paramList) {
    if (Build.VERSION.SDK_INT < 30) {
      removeDynamicShortcuts(paramContext, paramList);
      return;
    } 
    ((ShortcutManager)paramContext.getSystemService(ShortcutManager.class)).removeLongLivedShortcuts(paramList);
    getShortcutInfoSaverInstance(paramContext).removeShortcuts(paramList);
    Iterator<ShortcutInfoChangeListener> iterator = getShortcutInfoListeners(paramContext).iterator();
    while (iterator.hasNext())
      ((ShortcutInfoChangeListener)iterator.next()).onShortcutRemoved(paramList); 
  }
  
  public static void reportShortcutUsed(Context paramContext, String paramString) {
    Preconditions.checkNotNull(paramContext);
    Preconditions.checkNotNull(paramString);
    if (Build.VERSION.SDK_INT >= 25)
      ((ShortcutManager)paramContext.getSystemService(ShortcutManager.class)).reportShortcutUsed(paramString); 
    Iterator<ShortcutInfoChangeListener> iterator = getShortcutInfoListeners(paramContext).iterator();
    while (iterator.hasNext())
      ((ShortcutInfoChangeListener)iterator.next()).onShortcutUsageReported(Collections.singletonList(paramString)); 
  }
  
  public static boolean requestPinShortcut(Context paramContext, ShortcutInfoCompat paramShortcutInfoCompat, final IntentSender callback) {
    if (Build.VERSION.SDK_INT >= 26)
      return ((ShortcutManager)paramContext.getSystemService(ShortcutManager.class)).requestPinShortcut(paramShortcutInfoCompat.toShortcutInfo(), callback); 
    if (!isRequestPinShortcutSupported(paramContext))
      return false; 
    Intent intent = paramShortcutInfoCompat.addToIntent(new Intent("com.android.launcher.action.INSTALL_SHORTCUT"));
    if (callback == null) {
      paramContext.sendBroadcast(intent);
      return true;
    } 
    paramContext.sendOrderedBroadcast(intent, null, new BroadcastReceiver() {
          final IntentSender val$callback;
          
          public void onReceive(Context param1Context, Intent param1Intent) {
            try {
              callback.sendIntent(param1Context, 0, null, null, null);
            } catch (android.content.IntentSender.SendIntentException sendIntentException) {}
          }
        }null, -1, null, null);
    return true;
  }
  
  public static boolean setDynamicShortcuts(Context paramContext, List<ShortcutInfoCompat> paramList) {
    Preconditions.checkNotNull(paramContext);
    Preconditions.checkNotNull(paramList);
    if (Build.VERSION.SDK_INT >= 25) {
      ArrayList<ShortcutInfo> arrayList = new ArrayList(paramList.size());
      Iterator<ShortcutInfoCompat> iterator = paramList.iterator();
      while (iterator.hasNext())
        arrayList.add(((ShortcutInfoCompat)iterator.next()).toShortcutInfo()); 
      if (!((ShortcutManager)paramContext.getSystemService(ShortcutManager.class)).setDynamicShortcuts(arrayList))
        return false; 
    } 
    getShortcutInfoSaverInstance(paramContext).removeAllShortcuts();
    getShortcutInfoSaverInstance(paramContext).addShortcuts(paramList);
    for (ShortcutInfoChangeListener shortcutInfoChangeListener : getShortcutInfoListeners(paramContext)) {
      shortcutInfoChangeListener.onAllShortcutsRemoved();
      shortcutInfoChangeListener.onShortcutAdded(paramList);
    } 
    return true;
  }
  
  static void setShortcutInfoChangeListeners(List<ShortcutInfoChangeListener> paramList) {
    sShortcutInfoChangeListeners = paramList;
  }
  
  static void setShortcutInfoCompatSaver(ShortcutInfoCompatSaver<Void> paramShortcutInfoCompatSaver) {
    sShortcutInfoCompatSaver = paramShortcutInfoCompatSaver;
  }
  
  public static boolean updateShortcuts(Context paramContext, List<ShortcutInfoCompat> paramList) {
    if (Build.VERSION.SDK_INT <= 29)
      convertUriIconsToBitmapIcons(paramContext, paramList); 
    if (Build.VERSION.SDK_INT >= 25) {
      ArrayList<ShortcutInfo> arrayList = new ArrayList();
      Iterator<ShortcutInfoCompat> iterator1 = paramList.iterator();
      while (iterator1.hasNext())
        arrayList.add(((ShortcutInfoCompat)iterator1.next()).toShortcutInfo()); 
      if (!((ShortcutManager)paramContext.getSystemService(ShortcutManager.class)).updateShortcuts(arrayList))
        return false; 
    } 
    getShortcutInfoSaverInstance(paramContext).addShortcuts(paramList);
    Iterator<ShortcutInfoChangeListener> iterator = getShortcutInfoListeners(paramContext).iterator();
    while (iterator.hasNext())
      ((ShortcutInfoChangeListener)iterator.next()).onShortcutUpdated(paramList); 
    return true;
  }
  
  private static class Api25Impl {
    static String getShortcutInfoWithLowestRank(List<ShortcutInfo> param1List) {
      String str;
      int i = -1;
      List list = null;
      Iterator<ShortcutInfo> iterator = param1List.iterator();
      param1List = list;
      while (iterator.hasNext()) {
        ShortcutInfo shortcutInfo = iterator.next();
        int j = i;
        if (shortcutInfo.getRank() > i) {
          str = shortcutInfo.getId();
          j = shortcutInfo.getRank();
        } 
        i = j;
      } 
      return str;
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface ShortcutMatchFlags {}
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\content\pm\ShortcutManagerCompat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */