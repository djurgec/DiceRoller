package com.bumptech.glide.module;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@Deprecated
public final class ManifestParser {
  private static final String GLIDE_MODULE_VALUE = "GlideModule";
  
  private static final String TAG = "ManifestParser";
  
  private final Context context;
  
  public ManifestParser(Context paramContext) {
    this.context = paramContext;
  }
  
  private static GlideModule parseModule(String paramString) {
    try {
      NoSuchMethodException noSuchMethodException1;
      Class<?> clazz = Class.forName(paramString);
      noSuchMethodException2 = null;
      paramString = null;
      try {
        String str = (String)clazz.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
        paramString = str;
      } catch (InstantiationException instantiationException) {
        throwInstantiateGlideModuleException(clazz, instantiationException);
      } catch (IllegalAccessException illegalAccessException) {
        throwInstantiateGlideModuleException(clazz, illegalAccessException);
      } catch (NoSuchMethodException noSuchMethodException2) {
        throwInstantiateGlideModuleException(clazz, noSuchMethodException2);
      } catch (InvocationTargetException invocationTargetException) {
        throwInstantiateGlideModuleException(clazz, invocationTargetException);
        noSuchMethodException1 = noSuchMethodException2;
      } 
      if (noSuchMethodException1 instanceof GlideModule)
        return (GlideModule)noSuchMethodException1; 
      throw new RuntimeException("Expected instanceof GlideModule, but found: " + noSuchMethodException1);
    } catch (ClassNotFoundException classNotFoundException) {
      throw new IllegalArgumentException("Unable to find GlideModule implementation", classNotFoundException);
    } 
  }
  
  private static void throwInstantiateGlideModuleException(Class<?> paramClass, Exception paramException) {
    throw new RuntimeException("Unable to instantiate GlideModule implementation for " + paramClass, paramException);
  }
  
  public List<GlideModule> parse() {
    if (Log.isLoggable("ManifestParser", 3))
      Log.d("ManifestParser", "Loading Glide modules"); 
    ArrayList<GlideModule> arrayList = new ArrayList();
    try {
      ApplicationInfo applicationInfo = this.context.getPackageManager().getApplicationInfo(this.context.getPackageName(), 128);
      if (applicationInfo.metaData == null) {
        if (Log.isLoggable("ManifestParser", 3))
          Log.d("ManifestParser", "Got null app info metadata"); 
        return arrayList;
      } 
      if (Log.isLoggable("ManifestParser", 2)) {
        StringBuilder stringBuilder = new StringBuilder();
        this();
        Log.v("ManifestParser", stringBuilder.append("Got app info metadata: ").append(applicationInfo.metaData).toString());
      } 
      for (String str : applicationInfo.metaData.keySet()) {
        if ("GlideModule".equals(applicationInfo.metaData.get(str))) {
          arrayList.add(parseModule(str));
          if (Log.isLoggable("ManifestParser", 3)) {
            StringBuilder stringBuilder = new StringBuilder();
            this();
            Log.d("ManifestParser", stringBuilder.append("Loaded Glide module: ").append(str).toString());
          } 
        } 
      } 
      if (Log.isLoggable("ManifestParser", 3))
        Log.d("ManifestParser", "Finished loading Glide modules"); 
      return arrayList;
    } catch (android.content.pm.PackageManager.NameNotFoundException nameNotFoundException) {
      throw new RuntimeException("Unable to find metadata to parse GlideModules", nameNotFoundException);
    } 
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\module\ManifestParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */