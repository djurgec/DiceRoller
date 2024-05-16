package androidx.core.graphics.drawable;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Shader;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.util.Preconditions;
import androidx.versionedparcelable.CustomVersionedParcelable;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;

public class IconCompat extends CustomVersionedParcelable {
  private static final float ADAPTIVE_ICON_INSET_FACTOR = 0.25F;
  
  private static final int AMBIENT_SHADOW_ALPHA = 30;
  
  private static final float BLUR_FACTOR = 0.010416667F;
  
  static final PorterDuff.Mode DEFAULT_TINT_MODE = PorterDuff.Mode.SRC_IN;
  
  private static final float DEFAULT_VIEW_PORT_SCALE = 0.6666667F;
  
  static final String EXTRA_INT1 = "int1";
  
  static final String EXTRA_INT2 = "int2";
  
  static final String EXTRA_OBJ = "obj";
  
  static final String EXTRA_STRING1 = "string1";
  
  static final String EXTRA_TINT_LIST = "tint_list";
  
  static final String EXTRA_TINT_MODE = "tint_mode";
  
  static final String EXTRA_TYPE = "type";
  
  private static final float ICON_DIAMETER_FACTOR = 0.9166667F;
  
  private static final int KEY_SHADOW_ALPHA = 61;
  
  private static final float KEY_SHADOW_OFFSET_FACTOR = 0.020833334F;
  
  private static final String TAG = "IconCompat";
  
  public static final int TYPE_ADAPTIVE_BITMAP = 5;
  
  public static final int TYPE_BITMAP = 1;
  
  public static final int TYPE_DATA = 3;
  
  public static final int TYPE_RESOURCE = 2;
  
  public static final int TYPE_UNKNOWN = -1;
  
  public static final int TYPE_URI = 4;
  
  public static final int TYPE_URI_ADAPTIVE_BITMAP = 6;
  
  public byte[] mData = null;
  
  public int mInt1 = 0;
  
  public int mInt2 = 0;
  
  Object mObj1;
  
  public Parcelable mParcelable = null;
  
  public String mString1;
  
  public ColorStateList mTintList = null;
  
  PorterDuff.Mode mTintMode = DEFAULT_TINT_MODE;
  
  public String mTintModeStr = null;
  
  public int mType = -1;
  
  public IconCompat() {}
  
  private IconCompat(int paramInt) {
    this.mType = paramInt;
  }
  
  public static IconCompat createFromBundle(Bundle paramBundle) {
    int i = paramBundle.getInt("type");
    IconCompat iconCompat = new IconCompat(i);
    iconCompat.mInt1 = paramBundle.getInt("int1");
    iconCompat.mInt2 = paramBundle.getInt("int2");
    iconCompat.mString1 = paramBundle.getString("string1");
    if (paramBundle.containsKey("tint_list"))
      iconCompat.mTintList = (ColorStateList)paramBundle.getParcelable("tint_list"); 
    if (paramBundle.containsKey("tint_mode"))
      iconCompat.mTintMode = PorterDuff.Mode.valueOf(paramBundle.getString("tint_mode")); 
    switch (i) {
      default:
        Log.w("IconCompat", "Unknown type " + i);
        return null;
      case 3:
        iconCompat.mObj1 = paramBundle.getByteArray("obj");
        return iconCompat;
      case 2:
      case 4:
      case 6:
        iconCompat.mObj1 = paramBundle.getString("obj");
        return iconCompat;
      case -1:
      case 1:
      case 5:
        break;
    } 
    iconCompat.mObj1 = paramBundle.getParcelable("obj");
    return iconCompat;
  }
  
  public static IconCompat createFromIcon(Context paramContext, Icon paramIcon) {
    IconCompat iconCompat;
    Preconditions.checkNotNull(paramIcon);
    switch (getType(paramIcon)) {
      default:
        iconCompat = new IconCompat(-1);
        iconCompat.mObj1 = paramIcon;
        return iconCompat;
      case 6:
        return createWithAdaptiveBitmapContentUri(getUri(paramIcon));
      case 4:
        return createWithContentUri(getUri(paramIcon));
      case 2:
        break;
    } 
    String str = getResPackage(paramIcon);
    try {
      return createWithResource(getResources((Context)iconCompat, str), str, getResId(paramIcon));
    } catch (android.content.res.Resources.NotFoundException notFoundException) {
      throw new IllegalArgumentException("Icon resource cannot be found");
    } 
  }
  
  public static IconCompat createFromIcon(Icon paramIcon) {
    IconCompat iconCompat;
    Preconditions.checkNotNull(paramIcon);
    switch (getType(paramIcon)) {
      default:
        iconCompat = new IconCompat(-1);
        iconCompat.mObj1 = paramIcon;
        return iconCompat;
      case 6:
        return createWithAdaptiveBitmapContentUri(getUri(paramIcon));
      case 4:
        return createWithContentUri(getUri(paramIcon));
      case 2:
        break;
    } 
    return createWithResource(null, getResPackage(paramIcon), getResId(paramIcon));
  }
  
  public static IconCompat createFromIconOrNullIfZeroResId(Icon paramIcon) {
    return (getType(paramIcon) == 2 && getResId(paramIcon) == 0) ? null : createFromIcon(paramIcon);
  }
  
  static Bitmap createLegacyIconFromAdaptiveIcon(Bitmap paramBitmap, boolean paramBoolean) {
    int i = (int)(Math.min(paramBitmap.getWidth(), paramBitmap.getHeight()) * 0.6666667F);
    Bitmap bitmap = Bitmap.createBitmap(i, i, Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(bitmap);
    Paint paint = new Paint(3);
    float f2 = i * 0.5F;
    float f1 = 0.9166667F * f2;
    if (paramBoolean) {
      float f = i * 0.010416667F;
      paint.setColor(0);
      paint.setShadowLayer(f, 0.0F, i * 0.020833334F, 1023410176);
      canvas.drawCircle(f2, f2, f1, paint);
      paint.setShadowLayer(f, 0.0F, 0.0F, 503316480);
      canvas.drawCircle(f2, f2, f1, paint);
      paint.clearShadowLayer();
    } 
    paint.setColor(-16777216);
    BitmapShader bitmapShader = new BitmapShader(paramBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
    Matrix matrix = new Matrix();
    matrix.setTranslate((-(paramBitmap.getWidth() - i) / 2), (-(paramBitmap.getHeight() - i) / 2));
    bitmapShader.setLocalMatrix(matrix);
    paint.setShader((Shader)bitmapShader);
    canvas.drawCircle(f2, f2, f1, paint);
    canvas.setBitmap(null);
    return bitmap;
  }
  
  public static IconCompat createWithAdaptiveBitmap(Bitmap paramBitmap) {
    if (paramBitmap != null) {
      IconCompat iconCompat = new IconCompat(5);
      iconCompat.mObj1 = paramBitmap;
      return iconCompat;
    } 
    throw new IllegalArgumentException("Bitmap must not be null.");
  }
  
  public static IconCompat createWithAdaptiveBitmapContentUri(Uri paramUri) {
    if (paramUri != null)
      return createWithAdaptiveBitmapContentUri(paramUri.toString()); 
    throw new IllegalArgumentException("Uri must not be null.");
  }
  
  public static IconCompat createWithAdaptiveBitmapContentUri(String paramString) {
    if (paramString != null) {
      IconCompat iconCompat = new IconCompat(6);
      iconCompat.mObj1 = paramString;
      return iconCompat;
    } 
    throw new IllegalArgumentException("Uri must not be null.");
  }
  
  public static IconCompat createWithBitmap(Bitmap paramBitmap) {
    if (paramBitmap != null) {
      IconCompat iconCompat = new IconCompat(1);
      iconCompat.mObj1 = paramBitmap;
      return iconCompat;
    } 
    throw new IllegalArgumentException("Bitmap must not be null.");
  }
  
  public static IconCompat createWithContentUri(Uri paramUri) {
    if (paramUri != null)
      return createWithContentUri(paramUri.toString()); 
    throw new IllegalArgumentException("Uri must not be null.");
  }
  
  public static IconCompat createWithContentUri(String paramString) {
    if (paramString != null) {
      IconCompat iconCompat = new IconCompat(4);
      iconCompat.mObj1 = paramString;
      return iconCompat;
    } 
    throw new IllegalArgumentException("Uri must not be null.");
  }
  
  public static IconCompat createWithData(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
    if (paramArrayOfbyte != null) {
      IconCompat iconCompat = new IconCompat(3);
      iconCompat.mObj1 = paramArrayOfbyte;
      iconCompat.mInt1 = paramInt1;
      iconCompat.mInt2 = paramInt2;
      return iconCompat;
    } 
    throw new IllegalArgumentException("Data must not be null.");
  }
  
  public static IconCompat createWithResource(Context paramContext, int paramInt) {
    if (paramContext != null)
      return createWithResource(paramContext.getResources(), paramContext.getPackageName(), paramInt); 
    throw new IllegalArgumentException("Context must not be null.");
  }
  
  public static IconCompat createWithResource(Resources paramResources, String paramString, int paramInt) {
    if (paramString != null) {
      if (paramInt != 0) {
        IconCompat iconCompat = new IconCompat(2);
        iconCompat.mInt1 = paramInt;
        if (paramResources != null) {
          try {
            iconCompat.mObj1 = paramResources.getResourceName(paramInt);
          } catch (android.content.res.Resources.NotFoundException notFoundException) {
            throw new IllegalArgumentException("Icon resource cannot be found");
          } 
        } else {
          iconCompat.mObj1 = paramString;
        } 
        iconCompat.mString1 = paramString;
        return iconCompat;
      } 
      throw new IllegalArgumentException("Drawable resource ID must not be 0");
    } 
    throw new IllegalArgumentException("Package must not be null.");
  }
  
  private static int getResId(Icon paramIcon) {
    if (Build.VERSION.SDK_INT >= 28)
      return paramIcon.getResId(); 
    try {
      return ((Integer)paramIcon.getClass().getMethod("getResId", new Class[0]).invoke(paramIcon, new Object[0])).intValue();
    } catch (IllegalAccessException illegalAccessException) {
      Log.e("IconCompat", "Unable to get icon resource", illegalAccessException);
      return 0;
    } catch (InvocationTargetException invocationTargetException) {
      Log.e("IconCompat", "Unable to get icon resource", invocationTargetException);
      return 0;
    } catch (NoSuchMethodException noSuchMethodException) {
      Log.e("IconCompat", "Unable to get icon resource", noSuchMethodException);
      return 0;
    } 
  }
  
  private static String getResPackage(Icon paramIcon) {
    if (Build.VERSION.SDK_INT >= 28)
      return paramIcon.getResPackage(); 
    try {
      return (String)paramIcon.getClass().getMethod("getResPackage", new Class[0]).invoke(paramIcon, new Object[0]);
    } catch (IllegalAccessException illegalAccessException) {
      Log.e("IconCompat", "Unable to get icon package", illegalAccessException);
      return null;
    } catch (InvocationTargetException invocationTargetException) {
      Log.e("IconCompat", "Unable to get icon package", invocationTargetException);
      return null;
    } catch (NoSuchMethodException noSuchMethodException) {
      Log.e("IconCompat", "Unable to get icon package", noSuchMethodException);
      return null;
    } 
  }
  
  private static Resources getResources(Context paramContext, String paramString) {
    if ("android".equals(paramString))
      return Resources.getSystem(); 
    PackageManager packageManager = paramContext.getPackageManager();
    try {
      ApplicationInfo applicationInfo = packageManager.getApplicationInfo(paramString, 8192);
      return (applicationInfo != null) ? packageManager.getResourcesForApplication(applicationInfo) : null;
    } catch (android.content.pm.PackageManager.NameNotFoundException nameNotFoundException) {
      Log.e("IconCompat", String.format("Unable to find pkg=%s for icon", new Object[] { paramString }), (Throwable)nameNotFoundException);
      return null;
    } 
  }
  
  private static int getType(Icon paramIcon) {
    if (Build.VERSION.SDK_INT >= 28)
      return paramIcon.getType(); 
    try {
      return ((Integer)paramIcon.getClass().getMethod("getType", new Class[0]).invoke(paramIcon, new Object[0])).intValue();
    } catch (IllegalAccessException illegalAccessException) {
      Log.e("IconCompat", "Unable to get icon type " + paramIcon, illegalAccessException);
      return -1;
    } catch (InvocationTargetException invocationTargetException) {
      Log.e("IconCompat", "Unable to get icon type " + paramIcon, invocationTargetException);
      return -1;
    } catch (NoSuchMethodException noSuchMethodException) {
      Log.e("IconCompat", "Unable to get icon type " + paramIcon, noSuchMethodException);
      return -1;
    } 
  }
  
  private static Uri getUri(Icon paramIcon) {
    if (Build.VERSION.SDK_INT >= 28)
      return paramIcon.getUri(); 
    try {
      return (Uri)paramIcon.getClass().getMethod("getUri", new Class[0]).invoke(paramIcon, new Object[0]);
    } catch (IllegalAccessException illegalAccessException) {
      Log.e("IconCompat", "Unable to get icon uri", illegalAccessException);
      return null;
    } catch (InvocationTargetException invocationTargetException) {
      Log.e("IconCompat", "Unable to get icon uri", invocationTargetException);
      return null;
    } catch (NoSuchMethodException noSuchMethodException) {
      Log.e("IconCompat", "Unable to get icon uri", noSuchMethodException);
      return null;
    } 
  }
  
  private Drawable loadDrawableInner(Context paramContext) {
    InputStream inputStream;
    String str1;
    Resources resources;
    String str2;
    switch (this.mType) {
      default:
        return null;
      case 6:
        inputStream = getUriInputStream(paramContext);
        if (inputStream != null)
          return (Drawable)((Build.VERSION.SDK_INT >= 26) ? new AdaptiveIconDrawable(null, (Drawable)new BitmapDrawable(paramContext.getResources(), BitmapFactory.decodeStream(inputStream))) : new BitmapDrawable(paramContext.getResources(), createLegacyIconFromAdaptiveIcon(BitmapFactory.decodeStream(inputStream), false))); 
      case 5:
        return (Drawable)new BitmapDrawable(paramContext.getResources(), createLegacyIconFromAdaptiveIcon((Bitmap)this.mObj1, false));
      case 4:
        inputStream = getUriInputStream(paramContext);
        if (inputStream != null)
          return (Drawable)new BitmapDrawable(paramContext.getResources(), BitmapFactory.decodeStream(inputStream)); 
      case 3:
        return (Drawable)new BitmapDrawable(paramContext.getResources(), BitmapFactory.decodeByteArray((byte[])this.mObj1, this.mInt1, this.mInt2));
      case 2:
        str2 = getResPackage();
        str1 = str2;
        if (TextUtils.isEmpty(str2))
          str1 = paramContext.getPackageName(); 
        resources = getResources(paramContext, str1);
        try {
          return ResourcesCompat.getDrawable(resources, this.mInt1, paramContext.getTheme());
        } catch (RuntimeException runtimeException) {
          Log.e("IconCompat", String.format("Unable to load resource 0x%08x from pkg=%s", new Object[] { Integer.valueOf(this.mInt1), this.mObj1 }), runtimeException);
        } 
      case 1:
        break;
    } 
    return (Drawable)new BitmapDrawable(runtimeException.getResources(), (Bitmap)this.mObj1);
  }
  
  private static String typeToString(int paramInt) {
    switch (paramInt) {
      default:
        return "UNKNOWN";
      case 6:
        return "URI_MASKABLE";
      case 5:
        return "BITMAP_MASKABLE";
      case 4:
        return "URI";
      case 3:
        return "DATA";
      case 2:
        return "RESOURCE";
      case 1:
        break;
    } 
    return "BITMAP";
  }
  
  public void addToShortcutIntent(Intent paramIntent, Drawable paramDrawable, Context paramContext) {
    Bitmap bitmap1;
    Bitmap bitmap2;
    checkResource(paramContext);
    switch (this.mType) {
      default:
        throw new IllegalArgumentException("Icon type not supported for intent shortcuts");
      case 5:
        bitmap1 = createLegacyIconFromAdaptiveIcon((Bitmap)this.mObj1, true);
        break;
      case 2:
        try {
          Bitmap bitmap;
          Context context = bitmap1.createPackageContext(getResPackage(), 0);
          if (paramDrawable == null) {
            paramIntent.putExtra("android.intent.extra.shortcut.ICON_RESOURCE", (Parcelable)Intent.ShortcutIconResource.fromContext(context, this.mInt1));
            return;
          } 
          Drawable drawable = ContextCompat.getDrawable(context, this.mInt1);
          if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            int i = ((ActivityManager)context.getSystemService("activity")).getLauncherLargeIconSize();
            bitmap = Bitmap.createBitmap(i, i, Bitmap.Config.ARGB_8888);
          } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
          } 
          drawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
          Canvas canvas = new Canvas();
          this(bitmap);
          drawable.draw(canvas);
        } catch (android.content.pm.PackageManager.NameNotFoundException nameNotFoundException) {
          throw new IllegalArgumentException("Can't find package " + this.mObj1, nameNotFoundException);
        } 
        break;
      case 1:
        bitmap2 = (Bitmap)this.mObj1;
        bitmap1 = bitmap2;
        if (paramDrawable != null)
          bitmap1 = bitmap2.copy(bitmap2.getConfig(), true); 
        break;
    } 
    if (paramDrawable != null) {
      int i = bitmap1.getWidth();
      int j = bitmap1.getHeight();
      paramDrawable.setBounds(i / 2, j / 2, i, j);
      paramDrawable.draw(new Canvas(bitmap1));
    } 
    nameNotFoundException.putExtra("android.intent.extra.shortcut.ICON", (Parcelable)bitmap1);
  }
  
  public void checkResource(Context paramContext) {
    if (this.mType == 2) {
      Object object = this.mObj1;
      if (object != null) {
        String str1 = (String)object;
        if (!str1.contains(":"))
          return; 
        String str2 = str1.split(":", -1)[1];
        object = str2.split("/", -1)[0];
        String str3 = str2.split("/", -1)[1];
        str2 = str1.split(":", -1)[0];
        if ("0_resource_name_obfuscated".equals(str3)) {
          Log.i("IconCompat", "Found obfuscated resource, not trying to update resource id for it");
          return;
        } 
        String str4 = getResPackage();
        int i = getResources(paramContext, str4).getIdentifier(str3, (String)object, str2);
        if (this.mInt1 != i) {
          Log.i("IconCompat", "Id has changed for " + str4 + " " + str1);
          this.mInt1 = i;
        } 
      } 
    } 
  }
  
  public Bitmap getBitmap() {
    if (this.mType == -1 && Build.VERSION.SDK_INT >= 23) {
      Object object = this.mObj1;
      return (object instanceof Bitmap) ? (Bitmap)object : null;
    } 
    int i = this.mType;
    if (i == 1)
      return (Bitmap)this.mObj1; 
    if (i == 5)
      return createLegacyIconFromAdaptiveIcon((Bitmap)this.mObj1, true); 
    throw new IllegalStateException("called getBitmap() on " + this);
  }
  
  public int getResId() {
    if (this.mType == -1 && Build.VERSION.SDK_INT >= 23)
      return getResId((Icon)this.mObj1); 
    if (this.mType == 2)
      return this.mInt1; 
    throw new IllegalStateException("called getResId() on " + this);
  }
  
  public String getResPackage() {
    if (this.mType == -1 && Build.VERSION.SDK_INT >= 23)
      return getResPackage((Icon)this.mObj1); 
    if (this.mType == 2)
      return TextUtils.isEmpty(this.mString1) ? ((String)this.mObj1).split(":", -1)[0] : this.mString1; 
    throw new IllegalStateException("called getResPackage() on " + this);
  }
  
  public int getType() {
    return (this.mType == -1 && Build.VERSION.SDK_INT >= 23) ? getType((Icon)this.mObj1) : this.mType;
  }
  
  public Uri getUri() {
    if (this.mType == -1 && Build.VERSION.SDK_INT >= 23)
      return getUri((Icon)this.mObj1); 
    int i = this.mType;
    if (i == 4 || i == 6)
      return Uri.parse((String)this.mObj1); 
    throw new IllegalStateException("called getUri() on " + this);
  }
  
  public InputStream getUriInputStream(Context paramContext) {
    Uri uri = getUri();
    String str = uri.getScheme();
    if ("content".equals(str) || "file".equals(str)) {
      try {
        return paramContext.getContentResolver().openInputStream(uri);
      } catch (Exception exception) {
        Log.w("IconCompat", "Unable to load image from URI: " + uri, exception);
      } 
      return null;
    } 
    try {
      File file = new File();
      this((String)this.mObj1);
      return new FileInputStream(file);
    } catch (FileNotFoundException fileNotFoundException) {
      Log.w("IconCompat", "Unable to load image from path: " + uri, fileNotFoundException);
    } 
    return null;
  }
  
  public Drawable loadDrawable(Context paramContext) {
    checkResource(paramContext);
    if (Build.VERSION.SDK_INT >= 23)
      return toIcon(paramContext).loadDrawable(paramContext); 
    Drawable drawable = loadDrawableInner(paramContext);
    if (drawable != null && (this.mTintList != null || this.mTintMode != DEFAULT_TINT_MODE)) {
      drawable.mutate();
      DrawableCompat.setTintList(drawable, this.mTintList);
      DrawableCompat.setTintMode(drawable, this.mTintMode);
    } 
    return drawable;
  }
  
  public void onPostParceling() {
    String str;
    this.mTintMode = PorterDuff.Mode.valueOf(this.mTintModeStr);
    switch (this.mType) {
      default:
        return;
      case 3:
        this.mObj1 = this.mData;
      case 2:
      case 4:
      case 6:
        str = new String(this.mData, Charset.forName("UTF-16"));
        this.mObj1 = str;
        if (this.mType == 2 && this.mString1 == null)
          this.mString1 = str.split(":", -1)[0]; 
      case 1:
      case 5:
        parcelable = this.mParcelable;
        if (parcelable != null) {
          this.mObj1 = parcelable;
        } else {
          byte[] arrayOfByte = this.mData;
          this.mObj1 = arrayOfByte;
          this.mType = 3;
          this.mInt1 = 0;
          this.mInt2 = arrayOfByte.length;
        } 
      case -1:
        break;
    } 
    Parcelable parcelable = this.mParcelable;
    if (parcelable != null)
      this.mObj1 = parcelable; 
    throw new IllegalArgumentException("Invalid icon");
  }
  
  public void onPreParceling(boolean paramBoolean) {
    this.mTintModeStr = this.mTintMode.name();
    switch (this.mType) {
      default:
        return;
      case 4:
      case 6:
        this.mData = this.mObj1.toString().getBytes(Charset.forName("UTF-16"));
      case 3:
        this.mData = (byte[])this.mObj1;
      case 2:
        this.mData = ((String)this.mObj1).getBytes(Charset.forName("UTF-16"));
      case 1:
      case 5:
        if (paramBoolean) {
          Bitmap bitmap = (Bitmap)this.mObj1;
          ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
          bitmap.compress(Bitmap.CompressFormat.PNG, 90, byteArrayOutputStream);
          this.mData = byteArrayOutputStream.toByteArray();
        } else {
          this.mParcelable = (Parcelable)this.mObj1;
        } 
      case -1:
        break;
    } 
    if (!paramBoolean)
      this.mParcelable = (Parcelable)this.mObj1; 
    throw new IllegalArgumentException("Can't serialize Icon created with IconCompat#createFromIcon");
  }
  
  public IconCompat setTint(int paramInt) {
    return setTintList(ColorStateList.valueOf(paramInt));
  }
  
  public IconCompat setTintList(ColorStateList paramColorStateList) {
    this.mTintList = paramColorStateList;
    return this;
  }
  
  public IconCompat setTintMode(PorterDuff.Mode paramMode) {
    this.mTintMode = paramMode;
    return this;
  }
  
  public Bundle toBundle() {
    Bundle bundle = new Bundle();
    switch (this.mType) {
      default:
        throw new IllegalArgumentException("Invalid icon");
      case 3:
        bundle.putByteArray("obj", (byte[])this.mObj1);
        break;
      case 2:
      case 4:
      case 6:
        bundle.putString("obj", (String)this.mObj1);
        break;
      case 1:
      case 5:
        bundle.putParcelable("obj", (Parcelable)this.mObj1);
        break;
      case -1:
        bundle.putParcelable("obj", (Parcelable)this.mObj1);
        break;
    } 
    bundle.putInt("type", this.mType);
    bundle.putInt("int1", this.mInt1);
    bundle.putInt("int2", this.mInt2);
    bundle.putString("string1", this.mString1);
    ColorStateList colorStateList = this.mTintList;
    if (colorStateList != null)
      bundle.putParcelable("tint_list", (Parcelable)colorStateList); 
    PorterDuff.Mode mode = this.mTintMode;
    if (mode != DEFAULT_TINT_MODE)
      bundle.putString("tint_mode", mode.name()); 
    return bundle;
  }
  
  @Deprecated
  public Icon toIcon() {
    return toIcon(null);
  }
  
  public Icon toIcon(Context paramContext) {
    // Byte code:
    //   0: aload_0
    //   1: getfield mType : I
    //   4: tableswitch default -> 52, -1 -> 329, 0 -> 52, 1 -> 283, 2 -> 268, 3 -> 246, 4 -> 232, 5 -> 192, 6 -> 63
    //   52: new java/lang/IllegalArgumentException
    //   55: dup
    //   56: ldc_w 'Unknown type'
    //   59: invokespecial <init> : (Ljava/lang/String;)V
    //   62: athrow
    //   63: getstatic android/os/Build$VERSION.SDK_INT : I
    //   66: bipush #30
    //   68: if_icmplt -> 82
    //   71: aload_0
    //   72: invokevirtual getUri : ()Landroid/net/Uri;
    //   75: invokestatic createWithAdaptiveBitmapContentUri : (Landroid/net/Uri;)Landroid/graphics/drawable/Icon;
    //   78: astore_1
    //   79: goto -> 294
    //   82: aload_1
    //   83: ifnull -> 161
    //   86: aload_0
    //   87: aload_1
    //   88: invokevirtual getUriInputStream : (Landroid/content/Context;)Ljava/io/InputStream;
    //   91: astore_1
    //   92: aload_1
    //   93: ifnull -> 130
    //   96: getstatic android/os/Build$VERSION.SDK_INT : I
    //   99: bipush #26
    //   101: if_icmplt -> 115
    //   104: aload_1
    //   105: invokestatic decodeStream : (Ljava/io/InputStream;)Landroid/graphics/Bitmap;
    //   108: invokestatic createWithAdaptiveBitmap : (Landroid/graphics/Bitmap;)Landroid/graphics/drawable/Icon;
    //   111: astore_1
    //   112: goto -> 294
    //   115: aload_1
    //   116: invokestatic decodeStream : (Ljava/io/InputStream;)Landroid/graphics/Bitmap;
    //   119: iconst_0
    //   120: invokestatic createLegacyIconFromAdaptiveIcon : (Landroid/graphics/Bitmap;Z)Landroid/graphics/Bitmap;
    //   123: invokestatic createWithBitmap : (Landroid/graphics/Bitmap;)Landroid/graphics/drawable/Icon;
    //   126: astore_1
    //   127: goto -> 294
    //   130: new java/lang/IllegalStateException
    //   133: dup
    //   134: new java/lang/StringBuilder
    //   137: dup
    //   138: invokespecial <init> : ()V
    //   141: ldc_w 'Cannot load adaptive icon from uri: '
    //   144: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   147: aload_0
    //   148: invokevirtual getUri : ()Landroid/net/Uri;
    //   151: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   154: invokevirtual toString : ()Ljava/lang/String;
    //   157: invokespecial <init> : (Ljava/lang/String;)V
    //   160: athrow
    //   161: new java/lang/IllegalArgumentException
    //   164: dup
    //   165: new java/lang/StringBuilder
    //   168: dup
    //   169: invokespecial <init> : ()V
    //   172: ldc_w 'Context is required to resolve the file uri of the icon: '
    //   175: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   178: aload_0
    //   179: invokevirtual getUri : ()Landroid/net/Uri;
    //   182: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   185: invokevirtual toString : ()Ljava/lang/String;
    //   188: invokespecial <init> : (Ljava/lang/String;)V
    //   191: athrow
    //   192: getstatic android/os/Build$VERSION.SDK_INT : I
    //   195: bipush #26
    //   197: if_icmplt -> 214
    //   200: aload_0
    //   201: getfield mObj1 : Ljava/lang/Object;
    //   204: checkcast android/graphics/Bitmap
    //   207: invokestatic createWithAdaptiveBitmap : (Landroid/graphics/Bitmap;)Landroid/graphics/drawable/Icon;
    //   210: astore_1
    //   211: goto -> 294
    //   214: aload_0
    //   215: getfield mObj1 : Ljava/lang/Object;
    //   218: checkcast android/graphics/Bitmap
    //   221: iconst_0
    //   222: invokestatic createLegacyIconFromAdaptiveIcon : (Landroid/graphics/Bitmap;Z)Landroid/graphics/Bitmap;
    //   225: invokestatic createWithBitmap : (Landroid/graphics/Bitmap;)Landroid/graphics/drawable/Icon;
    //   228: astore_1
    //   229: goto -> 294
    //   232: aload_0
    //   233: getfield mObj1 : Ljava/lang/Object;
    //   236: checkcast java/lang/String
    //   239: invokestatic createWithContentUri : (Ljava/lang/String;)Landroid/graphics/drawable/Icon;
    //   242: astore_1
    //   243: goto -> 294
    //   246: aload_0
    //   247: getfield mObj1 : Ljava/lang/Object;
    //   250: checkcast [B
    //   253: aload_0
    //   254: getfield mInt1 : I
    //   257: aload_0
    //   258: getfield mInt2 : I
    //   261: invokestatic createWithData : ([BII)Landroid/graphics/drawable/Icon;
    //   264: astore_1
    //   265: goto -> 294
    //   268: aload_0
    //   269: invokevirtual getResPackage : ()Ljava/lang/String;
    //   272: aload_0
    //   273: getfield mInt1 : I
    //   276: invokestatic createWithResource : (Ljava/lang/String;I)Landroid/graphics/drawable/Icon;
    //   279: astore_1
    //   280: goto -> 294
    //   283: aload_0
    //   284: getfield mObj1 : Ljava/lang/Object;
    //   287: checkcast android/graphics/Bitmap
    //   290: invokestatic createWithBitmap : (Landroid/graphics/Bitmap;)Landroid/graphics/drawable/Icon;
    //   293: astore_1
    //   294: aload_0
    //   295: getfield mTintList : Landroid/content/res/ColorStateList;
    //   298: astore_2
    //   299: aload_2
    //   300: ifnull -> 309
    //   303: aload_1
    //   304: aload_2
    //   305: invokevirtual setTintList : (Landroid/content/res/ColorStateList;)Landroid/graphics/drawable/Icon;
    //   308: pop
    //   309: aload_0
    //   310: getfield mTintMode : Landroid/graphics/PorterDuff$Mode;
    //   313: astore_2
    //   314: aload_2
    //   315: getstatic androidx/core/graphics/drawable/IconCompat.DEFAULT_TINT_MODE : Landroid/graphics/PorterDuff$Mode;
    //   318: if_acmpeq -> 327
    //   321: aload_1
    //   322: aload_2
    //   323: invokevirtual setTintMode : (Landroid/graphics/PorterDuff$Mode;)Landroid/graphics/drawable/Icon;
    //   326: pop
    //   327: aload_1
    //   328: areturn
    //   329: aload_0
    //   330: getfield mObj1 : Ljava/lang/Object;
    //   333: checkcast android/graphics/drawable/Icon
    //   336: areturn
  }
  
  public String toString() {
    if (this.mType == -1)
      return String.valueOf(this.mObj1); 
    StringBuilder stringBuilder = (new StringBuilder("Icon(typ=")).append(typeToString(this.mType));
    switch (this.mType) {
      case 4:
      case 6:
        stringBuilder.append(" uri=").append(this.mObj1);
        break;
      case 3:
        stringBuilder.append(" len=").append(this.mInt1);
        if (this.mInt2 != 0)
          stringBuilder.append(" off=").append(this.mInt2); 
        break;
      case 2:
        stringBuilder.append(" pkg=").append(this.mString1).append(" id=").append(String.format("0x%08x", new Object[] { Integer.valueOf(getResId()) }));
        break;
      case 1:
      case 5:
        stringBuilder.append(" size=").append(((Bitmap)this.mObj1).getWidth()).append("x").append(((Bitmap)this.mObj1).getHeight());
        break;
    } 
    if (this.mTintList != null) {
      stringBuilder.append(" tint=");
      stringBuilder.append(this.mTintList);
    } 
    if (this.mTintMode != DEFAULT_TINT_MODE)
      stringBuilder.append(" mode=").append(this.mTintMode); 
    stringBuilder.append(")");
    return stringBuilder.toString();
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface IconType {}
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\graphics\drawable\IconCompat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */