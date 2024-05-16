package androidx.core.view;

import android.content.ClipData;
import android.content.ClipDescription;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.ContentInfo;
import androidx.core.util.Preconditions;
import androidx.core.util.Predicate;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public final class ContentInfoCompat {
  public static final int FLAG_CONVERT_TO_PLAIN_TEXT = 1;
  
  public static final int SOURCE_APP = 0;
  
  public static final int SOURCE_AUTOFILL = 4;
  
  public static final int SOURCE_CLIPBOARD = 1;
  
  public static final int SOURCE_DRAG_AND_DROP = 3;
  
  public static final int SOURCE_INPUT_METHOD = 2;
  
  public static final int SOURCE_PROCESS_TEXT = 5;
  
  private final Compat mCompat;
  
  ContentInfoCompat(Compat paramCompat) {
    this.mCompat = paramCompat;
  }
  
  static ClipData buildClipData(ClipDescription paramClipDescription, List<ClipData.Item> paramList) {
    ClipData clipData = new ClipData(new ClipDescription(paramClipDescription), paramList.get(0));
    for (byte b = 1; b < paramList.size(); b++)
      clipData.addItem(paramList.get(b)); 
    return clipData;
  }
  
  static String flagsToString(int paramInt) {
    return ((paramInt & 0x1) != 0) ? "FLAG_CONVERT_TO_PLAIN_TEXT" : String.valueOf(paramInt);
  }
  
  static Pair<ClipData, ClipData> partition(ClipData paramClipData, Predicate<ClipData.Item> paramPredicate) {
    ArrayList<ClipData.Item> arrayList2 = null;
    ArrayList<ClipData.Item> arrayList1 = null;
    for (byte b = 0; b < paramClipData.getItemCount(); b++) {
      ClipData.Item item = paramClipData.getItemAt(b);
      if (paramPredicate.test(item)) {
        if (arrayList2 == null)
          arrayList2 = new ArrayList(); 
        arrayList2.add(item);
      } else {
        if (arrayList1 == null)
          arrayList1 = new ArrayList(); 
        arrayList1.add(item);
      } 
    } 
    return (arrayList2 == null) ? Pair.create(null, paramClipData) : ((arrayList1 == null) ? Pair.create(paramClipData, null) : Pair.create(buildClipData(paramClipData.getDescription(), arrayList2), buildClipData(paramClipData.getDescription(), arrayList1)));
  }
  
  public static Pair<ContentInfo, ContentInfo> partition(ContentInfo paramContentInfo, Predicate<ClipData.Item> paramPredicate) {
    return Api31Impl.partition(paramContentInfo, paramPredicate);
  }
  
  static String sourceToString(int paramInt) {
    switch (paramInt) {
      default:
        return String.valueOf(paramInt);
      case 5:
        return "SOURCE_PROCESS_TEXT";
      case 4:
        return "SOURCE_AUTOFILL";
      case 3:
        return "SOURCE_DRAG_AND_DROP";
      case 2:
        return "SOURCE_INPUT_METHOD";
      case 1:
        return "SOURCE_CLIPBOARD";
      case 0:
        break;
    } 
    return "SOURCE_APP";
  }
  
  public static ContentInfoCompat toContentInfoCompat(ContentInfo paramContentInfo) {
    return new ContentInfoCompat(new Compat31Impl(paramContentInfo));
  }
  
  public ClipData getClip() {
    return this.mCompat.getClip();
  }
  
  public Bundle getExtras() {
    return this.mCompat.getExtras();
  }
  
  public int getFlags() {
    return this.mCompat.getFlags();
  }
  
  public Uri getLinkUri() {
    return this.mCompat.getLinkUri();
  }
  
  public int getSource() {
    return this.mCompat.getSource();
  }
  
  public Pair<ContentInfoCompat, ContentInfoCompat> partition(Predicate<ClipData.Item> paramPredicate) {
    ClipData clipData = this.mCompat.getClip();
    int i = clipData.getItemCount();
    ContentInfoCompat contentInfoCompat = null;
    if (i == 1) {
      boolean bool = paramPredicate.test(clipData.getItemAt(0));
      if (bool) {
        ContentInfoCompat contentInfoCompat1 = this;
      } else {
        paramPredicate = null;
      } 
      if (!bool)
        contentInfoCompat = this; 
      return Pair.create(paramPredicate, contentInfoCompat);
    } 
    Pair<ClipData, ClipData> pair = partition(clipData, paramPredicate);
    return (pair.first == null) ? Pair.create(null, this) : ((pair.second == null) ? Pair.create(this, null) : Pair.create((new Builder(this)).setClip((ClipData)pair.first).build(), (new Builder(this)).setClip((ClipData)pair.second).build()));
  }
  
  public ContentInfo toContentInfo() {
    return this.mCompat.getWrapped();
  }
  
  public String toString() {
    return this.mCompat.toString();
  }
  
  private static final class Api31Impl {
    public static Pair<ContentInfo, ContentInfo> partition(ContentInfo param1ContentInfo, Predicate<ClipData.Item> param1Predicate) {
      ClipData clipData = param1ContentInfo.getClip();
      int i = clipData.getItemCount();
      ContentInfo contentInfo = null;
      if (i == 1) {
        boolean bool = param1Predicate.test(clipData.getItemAt(0));
        if (bool) {
          ContentInfo contentInfo1 = param1ContentInfo;
        } else {
          param1Predicate = null;
        } 
        if (bool)
          param1ContentInfo = contentInfo; 
        return Pair.create(param1Predicate, param1ContentInfo);
      } 
      Objects.requireNonNull(param1Predicate);
      Pair<ClipData, ClipData> pair = ContentInfoCompat.partition(clipData, new ContentInfoCompat$Api31Impl$$ExternalSyntheticLambda0(param1Predicate));
      return (pair.first == null) ? Pair.create(null, param1ContentInfo) : ((pair.second == null) ? Pair.create(param1ContentInfo, null) : Pair.create((new ContentInfo.Builder(param1ContentInfo)).setClip((ClipData)pair.first).build(), (new ContentInfo.Builder(param1ContentInfo)).setClip((ClipData)pair.second).build()));
    }
  }
  
  public static final class Builder {
    private final ContentInfoCompat.BuilderCompat mBuilderCompat;
    
    public Builder(ClipData param1ClipData, int param1Int) {
      if (Build.VERSION.SDK_INT >= 31) {
        this.mBuilderCompat = new ContentInfoCompat.BuilderCompat31Impl(param1ClipData, param1Int);
      } else {
        this.mBuilderCompat = new ContentInfoCompat.BuilderCompatImpl(param1ClipData, param1Int);
      } 
    }
    
    public Builder(ContentInfoCompat param1ContentInfoCompat) {
      if (Build.VERSION.SDK_INT >= 31) {
        this.mBuilderCompat = new ContentInfoCompat.BuilderCompat31Impl(param1ContentInfoCompat);
      } else {
        this.mBuilderCompat = new ContentInfoCompat.BuilderCompatImpl(param1ContentInfoCompat);
      } 
    }
    
    public ContentInfoCompat build() {
      return this.mBuilderCompat.build();
    }
    
    public Builder setClip(ClipData param1ClipData) {
      this.mBuilderCompat.setClip(param1ClipData);
      return this;
    }
    
    public Builder setExtras(Bundle param1Bundle) {
      this.mBuilderCompat.setExtras(param1Bundle);
      return this;
    }
    
    public Builder setFlags(int param1Int) {
      this.mBuilderCompat.setFlags(param1Int);
      return this;
    }
    
    public Builder setLinkUri(Uri param1Uri) {
      this.mBuilderCompat.setLinkUri(param1Uri);
      return this;
    }
    
    public Builder setSource(int param1Int) {
      this.mBuilderCompat.setSource(param1Int);
      return this;
    }
  }
  
  private static interface BuilderCompat {
    ContentInfoCompat build();
    
    void setClip(ClipData param1ClipData);
    
    void setExtras(Bundle param1Bundle);
    
    void setFlags(int param1Int);
    
    void setLinkUri(Uri param1Uri);
    
    void setSource(int param1Int);
  }
  
  private static final class BuilderCompat31Impl implements BuilderCompat {
    private final ContentInfo.Builder mPlatformBuilder;
    
    BuilderCompat31Impl(ClipData param1ClipData, int param1Int) {
      this.mPlatformBuilder = new ContentInfo.Builder(param1ClipData, param1Int);
    }
    
    BuilderCompat31Impl(ContentInfoCompat param1ContentInfoCompat) {
      this.mPlatformBuilder = new ContentInfo.Builder(param1ContentInfoCompat.toContentInfo());
    }
    
    public ContentInfoCompat build() {
      return new ContentInfoCompat(new ContentInfoCompat.Compat31Impl(this.mPlatformBuilder.build()));
    }
    
    public void setClip(ClipData param1ClipData) {
      this.mPlatformBuilder.setClip(param1ClipData);
    }
    
    public void setExtras(Bundle param1Bundle) {
      this.mPlatformBuilder.setExtras(param1Bundle);
    }
    
    public void setFlags(int param1Int) {
      this.mPlatformBuilder.setFlags(param1Int);
    }
    
    public void setLinkUri(Uri param1Uri) {
      this.mPlatformBuilder.setLinkUri(param1Uri);
    }
    
    public void setSource(int param1Int) {
      this.mPlatformBuilder.setSource(param1Int);
    }
  }
  
  private static final class BuilderCompatImpl implements BuilderCompat {
    ClipData mClip;
    
    Bundle mExtras;
    
    int mFlags;
    
    Uri mLinkUri;
    
    int mSource;
    
    BuilderCompatImpl(ClipData param1ClipData, int param1Int) {
      this.mClip = param1ClipData;
      this.mSource = param1Int;
    }
    
    BuilderCompatImpl(ContentInfoCompat param1ContentInfoCompat) {
      this.mClip = param1ContentInfoCompat.getClip();
      this.mSource = param1ContentInfoCompat.getSource();
      this.mFlags = param1ContentInfoCompat.getFlags();
      this.mLinkUri = param1ContentInfoCompat.getLinkUri();
      this.mExtras = param1ContentInfoCompat.getExtras();
    }
    
    public ContentInfoCompat build() {
      return new ContentInfoCompat(new ContentInfoCompat.CompatImpl(this));
    }
    
    public void setClip(ClipData param1ClipData) {
      this.mClip = param1ClipData;
    }
    
    public void setExtras(Bundle param1Bundle) {
      this.mExtras = param1Bundle;
    }
    
    public void setFlags(int param1Int) {
      this.mFlags = param1Int;
    }
    
    public void setLinkUri(Uri param1Uri) {
      this.mLinkUri = param1Uri;
    }
    
    public void setSource(int param1Int) {
      this.mSource = param1Int;
    }
  }
  
  private static interface Compat {
    ClipData getClip();
    
    Bundle getExtras();
    
    int getFlags();
    
    Uri getLinkUri();
    
    int getSource();
    
    ContentInfo getWrapped();
  }
  
  private static final class Compat31Impl implements Compat {
    private final ContentInfo mWrapped;
    
    Compat31Impl(ContentInfo param1ContentInfo) {
      this.mWrapped = (ContentInfo)Preconditions.checkNotNull(param1ContentInfo);
    }
    
    public ClipData getClip() {
      return this.mWrapped.getClip();
    }
    
    public Bundle getExtras() {
      return this.mWrapped.getExtras();
    }
    
    public int getFlags() {
      return this.mWrapped.getFlags();
    }
    
    public Uri getLinkUri() {
      return this.mWrapped.getLinkUri();
    }
    
    public int getSource() {
      return this.mWrapped.getSource();
    }
    
    public ContentInfo getWrapped() {
      return this.mWrapped;
    }
    
    public String toString() {
      return "ContentInfoCompat{" + this.mWrapped + "}";
    }
  }
  
  private static final class CompatImpl implements Compat {
    private final ClipData mClip;
    
    private final Bundle mExtras;
    
    private final int mFlags;
    
    private final Uri mLinkUri;
    
    private final int mSource;
    
    CompatImpl(ContentInfoCompat.BuilderCompatImpl param1BuilderCompatImpl) {
      this.mClip = (ClipData)Preconditions.checkNotNull(param1BuilderCompatImpl.mClip);
      this.mSource = Preconditions.checkArgumentInRange(param1BuilderCompatImpl.mSource, 0, 5, "source");
      this.mFlags = Preconditions.checkFlagsArgument(param1BuilderCompatImpl.mFlags, 1);
      this.mLinkUri = param1BuilderCompatImpl.mLinkUri;
      this.mExtras = param1BuilderCompatImpl.mExtras;
    }
    
    public ClipData getClip() {
      return this.mClip;
    }
    
    public Bundle getExtras() {
      return this.mExtras;
    }
    
    public int getFlags() {
      return this.mFlags;
    }
    
    public Uri getLinkUri() {
      return this.mLinkUri;
    }
    
    public int getSource() {
      return this.mSource;
    }
    
    public ContentInfo getWrapped() {
      return null;
    }
    
    public String toString() {
      String str1;
      StringBuilder stringBuilder = (new StringBuilder()).append("ContentInfoCompat{clip=").append(this.mClip.getDescription()).append(", source=").append(ContentInfoCompat.sourceToString(this.mSource)).append(", flags=").append(ContentInfoCompat.flagsToString(this.mFlags));
      Uri uri = this.mLinkUri;
      String str2 = "";
      if (uri == null) {
        str1 = "";
      } else {
        str1 = ", hasLinkUri(" + this.mLinkUri.toString().length() + ")";
      } 
      stringBuilder = stringBuilder.append(str1);
      if (this.mExtras == null) {
        str1 = str2;
      } else {
        str1 = ", hasExtras";
      } 
      return stringBuilder.append(str1).append("}").toString();
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface Flags {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface Source {}
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\view\ContentInfoCompat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */