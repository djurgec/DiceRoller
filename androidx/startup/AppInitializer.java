package androidx.startup;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.tracing.Trace;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class AppInitializer {
  private static final String SECTION_NAME = "Startup";
  
  private static volatile AppInitializer sInstance;
  
  private static final Object sLock = new Object();
  
  final Context mContext;
  
  final Set<Class<? extends Initializer<?>>> mDiscovered;
  
  final Map<Class<?>, Object> mInitialized;
  
  AppInitializer(Context paramContext) {
    this.mContext = paramContext.getApplicationContext();
    this.mDiscovered = new HashSet<>();
    this.mInitialized = new HashMap<>();
  }
  
  public static AppInitializer getInstance(Context paramContext) {
    if (sInstance == null)
      synchronized (sLock) {
        if (sInstance == null) {
          AppInitializer appInitializer = new AppInitializer();
          this(paramContext);
          sInstance = appInitializer;
        } 
      }  
    return sInstance;
  }
  
  void discoverAndInitialize() {
    Exception exception;
    try {
      Trace.beginSection("Startup");
      ComponentName componentName = new ComponentName();
      this(this.mContext.getPackageName(), InitializationProvider.class.getName());
      Bundle bundle = (this.mContext.getPackageManager().getProviderInfo(componentName, 128)).metaData;
      String str = this.mContext.getString(R.string.androidx_startup);
      if (bundle != null) {
        HashSet<Class<?>> hashSet = new HashSet();
        this();
        for (String str1 : bundle.keySet()) {
          if (str.equals(bundle.getString(str1, null))) {
            Class<?> clazz = Class.forName(str1);
            if (Initializer.class.isAssignableFrom(clazz)) {
              this.mDiscovered.add(clazz);
              doInitialize((Class)clazz, hashSet);
            } 
          } 
        } 
      } 
      Trace.endSection();
      return;
    } catch (android.content.pm.PackageManager.NameNotFoundException|ClassNotFoundException nameNotFoundException) {
      StartupException startupException = new StartupException();
      this((Throwable)nameNotFoundException);
      throw startupException;
    } finally {}
    Trace.endSection();
    throw exception;
  }
  
  <T> T doInitialize(Class<? extends Initializer<?>> paramClass, Set<Class<?>> paramSet) {
    // Byte code:
    //   0: getstatic androidx/startup/AppInitializer.sLock : Ljava/lang/Object;
    //   3: astore #5
    //   5: aload #5
    //   7: monitorenter
    //   8: invokestatic isEnabled : ()Z
    //   11: istore_3
    //   12: iload_3
    //   13: ifeq -> 23
    //   16: aload_1
    //   17: invokevirtual getSimpleName : ()Ljava/lang/String;
    //   20: invokestatic beginSection : (Ljava/lang/String;)V
    //   23: aload_2
    //   24: aload_1
    //   25: invokeinterface contains : (Ljava/lang/Object;)Z
    //   30: ifne -> 220
    //   33: aload_0
    //   34: getfield mInitialized : Ljava/util/Map;
    //   37: aload_1
    //   38: invokeinterface containsKey : (Ljava/lang/Object;)Z
    //   43: ifne -> 201
    //   46: aload_2
    //   47: aload_1
    //   48: invokeinterface add : (Ljava/lang/Object;)Z
    //   53: pop
    //   54: aload_1
    //   55: iconst_0
    //   56: anewarray java/lang/Class
    //   59: invokevirtual getDeclaredConstructor : ([Ljava/lang/Class;)Ljava/lang/reflect/Constructor;
    //   62: iconst_0
    //   63: anewarray java/lang/Object
    //   66: invokevirtual newInstance : ([Ljava/lang/Object;)Ljava/lang/Object;
    //   69: checkcast androidx/startup/Initializer
    //   72: astore #4
    //   74: aload #4
    //   76: invokeinterface dependencies : ()Ljava/util/List;
    //   81: astore #6
    //   83: aload #6
    //   85: invokeinterface isEmpty : ()Z
    //   90: ifne -> 149
    //   93: aload #6
    //   95: invokeinterface iterator : ()Ljava/util/Iterator;
    //   100: astore #7
    //   102: aload #7
    //   104: invokeinterface hasNext : ()Z
    //   109: ifeq -> 149
    //   112: aload #7
    //   114: invokeinterface next : ()Ljava/lang/Object;
    //   119: checkcast java/lang/Class
    //   122: astore #6
    //   124: aload_0
    //   125: getfield mInitialized : Ljava/util/Map;
    //   128: aload #6
    //   130: invokeinterface containsKey : (Ljava/lang/Object;)Z
    //   135: ifne -> 146
    //   138: aload_0
    //   139: aload #6
    //   141: aload_2
    //   142: invokevirtual doInitialize : (Ljava/lang/Class;Ljava/util/Set;)Ljava/lang/Object;
    //   145: pop
    //   146: goto -> 102
    //   149: aload #4
    //   151: aload_0
    //   152: getfield mContext : Landroid/content/Context;
    //   155: invokeinterface create : (Landroid/content/Context;)Ljava/lang/Object;
    //   160: astore #4
    //   162: aload_2
    //   163: aload_1
    //   164: invokeinterface remove : (Ljava/lang/Object;)Z
    //   169: pop
    //   170: aload_0
    //   171: getfield mInitialized : Ljava/util/Map;
    //   174: aload_1
    //   175: aload #4
    //   177: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   182: pop
    //   183: aload #4
    //   185: astore_1
    //   186: goto -> 212
    //   189: astore_2
    //   190: new androidx/startup/StartupException
    //   193: astore_1
    //   194: aload_1
    //   195: aload_2
    //   196: invokespecial <init> : (Ljava/lang/Throwable;)V
    //   199: aload_1
    //   200: athrow
    //   201: aload_0
    //   202: getfield mInitialized : Ljava/util/Map;
    //   205: aload_1
    //   206: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   211: astore_1
    //   212: invokestatic endSection : ()V
    //   215: aload #5
    //   217: monitorexit
    //   218: aload_1
    //   219: areturn
    //   220: ldc 'Cannot initialize %s. Cycle detected.'
    //   222: iconst_1
    //   223: anewarray java/lang/Object
    //   226: dup
    //   227: iconst_0
    //   228: aload_1
    //   229: invokevirtual getName : ()Ljava/lang/String;
    //   232: aastore
    //   233: invokestatic format : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   236: astore_1
    //   237: new java/lang/IllegalStateException
    //   240: astore_2
    //   241: aload_2
    //   242: aload_1
    //   243: invokespecial <init> : (Ljava/lang/String;)V
    //   246: aload_2
    //   247: athrow
    //   248: astore_1
    //   249: invokestatic endSection : ()V
    //   252: aload_1
    //   253: athrow
    //   254: astore_1
    //   255: aload #5
    //   257: monitorexit
    //   258: aload_1
    //   259: athrow
    // Exception table:
    //   from	to	target	type
    //   8	12	254	finally
    //   16	23	248	finally
    //   23	54	248	finally
    //   54	74	189	finally
    //   74	102	189	finally
    //   102	146	189	finally
    //   149	183	189	finally
    //   190	201	248	finally
    //   201	212	248	finally
    //   212	218	254	finally
    //   220	248	248	finally
    //   249	252	254	finally
    //   252	254	254	finally
    //   255	258	254	finally
  }
  
  public <T> T initializeComponent(Class<? extends Initializer<T>> paramClass) {
    return doInitialize(paramClass, new HashSet<>());
  }
  
  public boolean isEagerlyInitialized(Class<? extends Initializer<?>> paramClass) {
    return this.mDiscovered.contains(paramClass);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\startup\AppInitializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */