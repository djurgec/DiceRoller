package com.bumptech.glide.load.resource.drawable;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import java.io.IOException;
import java.util.List;

public class ResourceDrawableDecoder implements ResourceDecoder<Uri, Drawable> {
  private static final String ANDROID_PACKAGE_NAME = "android";
  
  private static final int ID_PATH_SEGMENTS = 1;
  
  private static final int MISSING_RESOURCE_ID = 0;
  
  private static final int NAME_PATH_SEGMENT_INDEX = 1;
  
  private static final int NAME_URI_PATH_SEGMENTS = 2;
  
  private static final int RESOURCE_ID_SEGMENT_INDEX = 0;
  
  private static final int TYPE_PATH_SEGMENT_INDEX = 0;
  
  private final Context context;
  
  public ResourceDrawableDecoder(Context paramContext) {
    this.context = paramContext.getApplicationContext();
  }
  
  private Context findContextForPackage(Uri paramUri, String paramString) {
    if (paramString.equals(this.context.getPackageName()))
      return this.context; 
    try {
      return this.context.createPackageContext(paramString, 0);
    } catch (android.content.pm.PackageManager.NameNotFoundException nameNotFoundException) {
      if (paramString.contains(this.context.getPackageName()))
        return this.context; 
      throw new IllegalArgumentException("Failed to obtain context or unrecognized Uri format for: " + paramUri, nameNotFoundException);
    } 
  }
  
  private int findResourceIdFromResourceIdUri(Uri paramUri) {
    List<String> list = paramUri.getPathSegments();
    try {
      return Integer.parseInt(list.get(0));
    } catch (NumberFormatException numberFormatException) {
      throw new IllegalArgumentException("Unrecognized Uri format: " + paramUri, numberFormatException);
    } 
  }
  
  private int findResourceIdFromTypeAndNameResourceUri(Context paramContext, Uri paramUri) {
    List<String> list = paramUri.getPathSegments();
    String str1 = paramUri.getAuthority();
    String str2 = list.get(0);
    String str3 = list.get(1);
    int j = paramContext.getResources().getIdentifier(str3, str2, str1);
    int i = j;
    if (j == 0)
      i = Resources.getSystem().getIdentifier(str3, str2, "android"); 
    if (i != 0)
      return i; 
    throw new IllegalArgumentException("Failed to find resource id for: " + paramUri);
  }
  
  private int findResourceIdFromUri(Context paramContext, Uri paramUri) {
    List list = paramUri.getPathSegments();
    if (list.size() == 2)
      return findResourceIdFromTypeAndNameResourceUri(paramContext, paramUri); 
    if (list.size() == 1)
      return findResourceIdFromResourceIdUri(paramUri); 
    throw new IllegalArgumentException("Unrecognized Uri format: " + paramUri);
  }
  
  public Resource<Drawable> decode(Uri paramUri, int paramInt1, int paramInt2, Options paramOptions) {
    Context context = findContextForPackage(paramUri, paramUri.getAuthority());
    paramInt1 = findResourceIdFromUri(context, paramUri);
    return NonOwnedDrawableResource.newInstance(DrawableDecoderCompat.getDrawable(this.context, context, paramInt1));
  }
  
  public boolean handles(Uri paramUri, Options paramOptions) {
    return paramUri.getScheme().equals("android.resource");
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\bumptech\glide\load\resource\drawable\ResourceDrawableDecoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */