package androidx.core.graphics;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.util.Log;
import androidx.core.content.res.FontResourcesParserCompat;
import androidx.core.provider.FontsContractCompat;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

class TypefaceCompatApi21Impl extends TypefaceCompatBaseImpl {
  private static final String ADD_FONT_WEIGHT_STYLE_METHOD = "addFontWeightStyle";
  
  private static final String CREATE_FROM_FAMILIES_WITH_DEFAULT_METHOD = "createFromFamiliesWithDefault";
  
  private static final String FONT_FAMILY_CLASS = "android.graphics.FontFamily";
  
  private static final String TAG = "TypefaceCompatApi21Impl";
  
  private static Method sAddFontWeightStyle;
  
  private static Method sCreateFromFamiliesWithDefault;
  
  private static Class<?> sFontFamily;
  
  private static Constructor<?> sFontFamilyCtor;
  
  private static boolean sHasInitBeenCalled = false;
  
  private static boolean addFontWeightStyle(Object paramObject, String paramString, int paramInt, boolean paramBoolean) {
    init();
    try {
      return ((Boolean)sAddFontWeightStyle.invoke(paramObject, new Object[] { paramString, Integer.valueOf(paramInt), Boolean.valueOf(paramBoolean) })).booleanValue();
    } catch (IllegalAccessException|java.lang.reflect.InvocationTargetException illegalAccessException) {
      throw new RuntimeException(illegalAccessException);
    } 
  }
  
  private static Typeface createFromFamiliesWithDefault(Object paramObject) {
    init();
    try {
      Object object = Array.newInstance(sFontFamily, 1);
      Array.set(object, 0, paramObject);
      return (Typeface)sCreateFromFamiliesWithDefault.invoke(null, new Object[] { object });
    } catch (IllegalAccessException|java.lang.reflect.InvocationTargetException illegalAccessException) {
      throw new RuntimeException(illegalAccessException);
    } 
  }
  
  private File getFile(ParcelFileDescriptor paramParcelFileDescriptor) {
    try {
      StringBuilder stringBuilder = new StringBuilder();
      this();
      String str = Os.readlink(stringBuilder.append("/proc/self/fd/").append(paramParcelFileDescriptor.getFd()).toString());
      return OsConstants.S_ISREG((Os.stat(str)).st_mode) ? new File(str) : null;
    } catch (ErrnoException errnoException) {
      return null;
    } 
  }
  
  private static void init() {
    Method method;
    Class clazz;
    Constructor constructor;
    if (sHasInitBeenCalled)
      return; 
    sHasInitBeenCalled = true;
    try {
      clazz = Class.forName("android.graphics.FontFamily");
      constructor = clazz.getConstructor(new Class[0]);
      method = clazz.getMethod("addFontWeightStyle", new Class[] { String.class, int.class, boolean.class });
      Method method1 = Typeface.class.getMethod("createFromFamiliesWithDefault", new Class[] { Array.newInstance(clazz, 1).getClass() });
    } catch (ClassNotFoundException|NoSuchMethodException classNotFoundException) {
      Log.e("TypefaceCompatApi21Impl", classNotFoundException.getClass().getName(), classNotFoundException);
      clazz = null;
      constructor = null;
      method = null;
      classNotFoundException = null;
    } 
    sFontFamilyCtor = constructor;
    sFontFamily = clazz;
    sAddFontWeightStyle = method;
    sCreateFromFamiliesWithDefault = (Method)classNotFoundException;
  }
  
  private static Object newFamily() {
    init();
    try {
      return sFontFamilyCtor.newInstance(new Object[0]);
    } catch (IllegalAccessException|InstantiationException|java.lang.reflect.InvocationTargetException illegalAccessException) {
      throw new RuntimeException(illegalAccessException);
    } 
  }
  
  public Typeface createFromFontFamilyFilesResourceEntry(Context paramContext, FontResourcesParserCompat.FontFamilyFilesResourceEntry paramFontFamilyFilesResourceEntry, Resources paramResources, int paramInt) {
    Object object = newFamily();
    FontResourcesParserCompat.FontFileResourceEntry[] arrayOfFontFileResourceEntry = paramFontFamilyFilesResourceEntry.getEntries();
    int i = arrayOfFontFileResourceEntry.length;
    paramInt = 0;
    while (paramInt < i) {
      FontResourcesParserCompat.FontFileResourceEntry fontFileResourceEntry = arrayOfFontFileResourceEntry[paramInt];
      File file = TypefaceCompatUtil.getTempFile(paramContext);
      if (file == null)
        return null; 
      try {
        boolean bool = TypefaceCompatUtil.copyToFile(file, paramResources, fontFileResourceEntry.getResourceId());
        if (!bool)
          return null; 
        bool = addFontWeightStyle(object, file.getPath(), fontFileResourceEntry.getWeight(), fontFileResourceEntry.isItalic());
        if (!bool)
          return null; 
        file.delete();
      } catch (RuntimeException runtimeException) {
        return null;
      } finally {
        file.delete();
      } 
    } 
    return createFromFamiliesWithDefault(object);
  }
  
  public Typeface createFromFontInfo(Context paramContext, CancellationSignal paramCancellationSignal, FontsContractCompat.FontInfo[] paramArrayOfFontInfo, int paramInt) {
    if (paramArrayOfFontInfo.length < 1)
      return null; 
    FontsContractCompat.FontInfo fontInfo = findBestInfo(paramArrayOfFontInfo, paramInt);
    ContentResolver contentResolver = paramContext.getContentResolver();
    try {
      ParcelFileDescriptor parcelFileDescriptor = contentResolver.openFileDescriptor(fontInfo.getUri(), "r", paramCancellationSignal);
      if (parcelFileDescriptor == null) {
        if (parcelFileDescriptor != null)
          parcelFileDescriptor.close(); 
        return null;
      } 
      try {
        FileInputStream fileInputStream;
        File file = getFile(parcelFileDescriptor);
        if (file == null || !file.canRead()) {
          fileInputStream = new FileInputStream();
          this(parcelFileDescriptor.getFileDescriptor());
          try {
            return createFromInputStream(paramContext, fileInputStream);
          } finally {
            try {
              fileInputStream.close();
            } finally {
              fileInputStream = null;
            } 
          } 
        } 
        return Typeface.createFromFile((File)fileInputStream);
      } finally {
        if (parcelFileDescriptor != null)
          try {
            parcelFileDescriptor.close();
          } finally {
            parcelFileDescriptor = null;
          }  
      } 
    } catch (IOException iOException) {
      return null;
    } 
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\graphics\TypefaceCompatApi21Impl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */