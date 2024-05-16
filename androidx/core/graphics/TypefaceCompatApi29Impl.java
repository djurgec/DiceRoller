package androidx.core.graphics;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.fonts.Font;
import android.graphics.fonts.FontFamily;
import android.graphics.fonts.FontStyle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import androidx.core.content.res.FontResourcesParserCompat;
import androidx.core.provider.FontsContractCompat;
import java.io.IOException;
import java.io.InputStream;

public class TypefaceCompatApi29Impl extends TypefaceCompatBaseImpl {
  public Typeface createFromFontFamilyFilesResourceEntry(Context paramContext, FontResourcesParserCompat.FontFamilyFilesResourceEntry paramFontFamilyFilesResourceEntry, Resources paramResources, int paramInt) {
    paramContext = null;
    try {
      FontResourcesParserCompat.FontFileResourceEntry[] arrayOfFontFileResourceEntry = paramFontFamilyFilesResourceEntry.getEntries();
      int i = arrayOfFontFileResourceEntry.length;
      boolean bool = false;
      char c = Character.MIN_VALUE;
      while (true) {
        FontFamily.Builder builder;
        boolean bool1 = true;
        if (c < i) {
          FontResourcesParserCompat.FontFileResourceEntry fontFileResourceEntry = arrayOfFontFileResourceEntry[c];
          try {
            Font.Builder builder1 = new Font.Builder();
            this(paramResources, fontFileResourceEntry.getResourceId());
            builder1 = builder1.setWeight(fontFileResourceEntry.getWeight());
            if (!fontFileResourceEntry.isItalic())
              bool1 = false; 
            Font font = builder1.setSlant(bool1).setTtcIndex(fontFileResourceEntry.getTtcIndex()).setFontVariationSettings(fontFileResourceEntry.getVariationSettings()).build();
            if (paramContext == null) {
              FontFamily.Builder builder2 = new FontFamily.Builder();
              this(font);
              builder = builder2;
            } else {
              builder.addFont(font);
            } 
          } catch (IOException iOException) {}
          c++;
          continue;
        } 
        if (builder == null)
          return null; 
        FontStyle fontStyle = new FontStyle();
        if ((paramInt & 0x1) != 0) {
          c = 'ʼ';
        } else {
          c = 'Ɛ';
        } 
        if ((paramInt & 0x2) != 0) {
          paramInt = 1;
        } else {
          paramInt = bool;
        } 
        this(c, paramInt);
        Typeface.CustomFallbackBuilder customFallbackBuilder = new Typeface.CustomFallbackBuilder();
        this(builder.build());
        return customFallbackBuilder.setStyle(fontStyle).build();
      } 
    } catch (Exception exception) {
      return null;
    } 
  }
  
  public Typeface createFromFontInfo(Context paramContext, CancellationSignal paramCancellationSignal, FontsContractCompat.FontInfo[] paramArrayOfFontInfo, int paramInt) {
    Context context = null;
    ContentResolver contentResolver = paramContext.getContentResolver();
    try {
      int i = paramArrayOfFontInfo.length;
      boolean bool = false;
      char c = Character.MIN_VALUE;
      paramContext = context;
      while (true) {
        FontFamily.Builder builder;
        boolean bool1 = true;
        if (c < i) {
          FontFamily.Builder builder1;
          FontsContractCompat.FontInfo fontInfo = paramArrayOfFontInfo[c];
          context = paramContext;
          try {
            ParcelFileDescriptor parcelFileDescriptor = contentResolver.openFileDescriptor(fontInfo.getUri(), "r", paramCancellationSignal);
            if (parcelFileDescriptor == null) {
              if (parcelFileDescriptor != null) {
                context = paramContext;
                parcelFileDescriptor.close();
              } 
            } else {
              try {
                Font.Builder builder2 = new Font.Builder();
                this(parcelFileDescriptor);
                builder2 = builder2.setWeight(fontInfo.getWeight());
                if (!fontInfo.isItalic())
                  bool1 = false; 
                Font font = builder2.setSlant(bool1).setTtcIndex(fontInfo.getTtcIndex()).build();
              } finally {
                if (parcelFileDescriptor != null)
                  try {
                    parcelFileDescriptor.close();
                  } finally {
                    parcelFileDescriptor = null;
                    FontFamily.Builder builder2 = builder;
                  }  
                builder1 = builder;
              } 
            } 
          } catch (IOException iOException) {
            builder = builder1;
          } 
          c++;
          continue;
        } 
        if (builder == null)
          return null; 
        FontStyle fontStyle = new FontStyle();
        if ((paramInt & 0x1) != 0) {
          c = 'ʼ';
        } else {
          c = 'Ɛ';
        } 
        if ((paramInt & 0x2) != 0) {
          paramInt = 1;
        } else {
          paramInt = bool;
        } 
        this(c, paramInt);
        Typeface.CustomFallbackBuilder customFallbackBuilder = new Typeface.CustomFallbackBuilder();
        this(builder.build());
        return customFallbackBuilder.setStyle(fontStyle).build();
      } 
    } catch (Exception exception) {
      return null;
    } 
  }
  
  protected Typeface createFromInputStream(Context paramContext, InputStream paramInputStream) {
    throw new RuntimeException("Do not use this function in API 29 or later.");
  }
  
  public Typeface createFromResourcesFontFile(Context paramContext, Resources paramResources, int paramInt1, String paramString, int paramInt2) {
    try {
      Font.Builder builder = new Font.Builder();
      this(paramResources, paramInt1);
      Font font = builder.build();
      FontFamily.Builder builder1 = new FontFamily.Builder();
      this(font);
      FontFamily fontFamily = builder1.build();
      Typeface.CustomFallbackBuilder customFallbackBuilder = new Typeface.CustomFallbackBuilder();
      this(fontFamily);
      return customFallbackBuilder.setStyle(font.getStyle()).build();
    } catch (Exception exception) {
      return null;
    } 
  }
  
  protected FontsContractCompat.FontInfo findBestInfo(FontsContractCompat.FontInfo[] paramArrayOfFontInfo, int paramInt) {
    throw new RuntimeException("Do not use this function in API 29 or later.");
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\graphics\TypefaceCompatApi29Impl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */