package androidx.core.content.pm;

import android.app.Person;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ShortcutInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.os.UserHandle;
import android.text.TextUtils;
import androidx.core.app.Person;
import androidx.core.content.LocusIdCompat;
import androidx.core.graphics.drawable.IconCompat;
import androidx.core.net.UriCompat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ShortcutInfoCompat {
  private static final String EXTRA_LOCUS_ID = "extraLocusId";
  
  private static final String EXTRA_LONG_LIVED = "extraLongLived";
  
  private static final String EXTRA_PERSON_ = "extraPerson_";
  
  private static final String EXTRA_PERSON_COUNT = "extraPersonCount";
  
  private static final String EXTRA_SLICE_URI = "extraSliceUri";
  
  ComponentName mActivity;
  
  Set<String> mCategories;
  
  Context mContext;
  
  CharSequence mDisabledMessage;
  
  int mDisabledReason;
  
  PersistableBundle mExtras;
  
  boolean mHasKeyFieldsOnly;
  
  IconCompat mIcon;
  
  String mId;
  
  Intent[] mIntents;
  
  boolean mIsAlwaysBadged;
  
  boolean mIsCached;
  
  boolean mIsDeclaredInManifest;
  
  boolean mIsDynamic;
  
  boolean mIsEnabled = true;
  
  boolean mIsImmutable;
  
  boolean mIsLongLived;
  
  boolean mIsPinned;
  
  CharSequence mLabel;
  
  long mLastChangedTimestamp;
  
  LocusIdCompat mLocusId;
  
  CharSequence mLongLabel;
  
  String mPackageName;
  
  Person[] mPersons;
  
  int mRank;
  
  UserHandle mUser;
  
  private PersistableBundle buildLegacyExtrasBundle() {
    if (this.mExtras == null)
      this.mExtras = new PersistableBundle(); 
    Person[] arrayOfPerson = this.mPersons;
    if (arrayOfPerson != null && arrayOfPerson.length > 0) {
      this.mExtras.putInt("extraPersonCount", arrayOfPerson.length);
      for (byte b = 0; b < this.mPersons.length; b++)
        this.mExtras.putPersistableBundle("extraPerson_" + (b + 1), this.mPersons[b].toPersistableBundle()); 
    } 
    LocusIdCompat locusIdCompat = this.mLocusId;
    if (locusIdCompat != null)
      this.mExtras.putString("extraLocusId", locusIdCompat.getId()); 
    this.mExtras.putBoolean("extraLongLived", this.mIsLongLived);
    return this.mExtras;
  }
  
  static List<ShortcutInfoCompat> fromShortcuts(Context paramContext, List<ShortcutInfo> paramList) {
    ArrayList<ShortcutInfoCompat> arrayList = new ArrayList(paramList.size());
    Iterator<ShortcutInfo> iterator = paramList.iterator();
    while (iterator.hasNext())
      arrayList.add((new Builder(paramContext, iterator.next())).build()); 
    return arrayList;
  }
  
  static LocusIdCompat getLocusId(ShortcutInfo paramShortcutInfo) {
    return (Build.VERSION.SDK_INT >= 29) ? ((paramShortcutInfo.getLocusId() == null) ? null : LocusIdCompat.toLocusIdCompat(paramShortcutInfo.getLocusId())) : getLocusIdFromExtra(paramShortcutInfo.getExtras());
  }
  
  private static LocusIdCompat getLocusIdFromExtra(PersistableBundle paramPersistableBundle) {
    LocusIdCompat locusIdCompat;
    String str2 = null;
    if (paramPersistableBundle == null)
      return null; 
    String str1 = paramPersistableBundle.getString("extraLocusId");
    if (str1 == null) {
      str1 = str2;
    } else {
      locusIdCompat = new LocusIdCompat(str1);
    } 
    return locusIdCompat;
  }
  
  static boolean getLongLivedFromExtra(PersistableBundle paramPersistableBundle) {
    return (paramPersistableBundle == null || !paramPersistableBundle.containsKey("extraLongLived")) ? false : paramPersistableBundle.getBoolean("extraLongLived");
  }
  
  static Person[] getPersonsFromExtra(PersistableBundle paramPersistableBundle) {
    if (paramPersistableBundle == null || !paramPersistableBundle.containsKey("extraPersonCount"))
      return null; 
    int i = paramPersistableBundle.getInt("extraPersonCount");
    Person[] arrayOfPerson = new Person[i];
    for (byte b = 0; b < i; b++)
      arrayOfPerson[b] = Person.fromPersistableBundle(paramPersistableBundle.getPersistableBundle("extraPerson_" + (b + 1))); 
    return arrayOfPerson;
  }
  
  Intent addToIntent(Intent paramIntent) {
    Intent[] arrayOfIntent = this.mIntents;
    paramIntent.putExtra("android.intent.extra.shortcut.INTENT", (Parcelable)arrayOfIntent[arrayOfIntent.length - 1]).putExtra("android.intent.extra.shortcut.NAME", this.mLabel.toString());
    if (this.mIcon != null) {
      Drawable drawable;
      ComponentName componentName = null;
      Intent[] arrayOfIntent1 = null;
      if (this.mIsAlwaysBadged) {
        Intent[] arrayOfIntent2;
        PackageManager packageManager = this.mContext.getPackageManager();
        componentName = this.mActivity;
        arrayOfIntent = arrayOfIntent1;
        if (componentName != null)
          try {
            Drawable drawable1 = packageManager.getActivityIcon(componentName);
          } catch (android.content.pm.PackageManager.NameNotFoundException nameNotFoundException) {
            arrayOfIntent2 = arrayOfIntent1;
          }  
        Intent[] arrayOfIntent3 = arrayOfIntent2;
        if (arrayOfIntent2 == null)
          drawable = this.mContext.getApplicationInfo().loadIcon(packageManager); 
      } 
      this.mIcon.addToShortcutIntent(paramIntent, drawable, this.mContext);
    } 
    return paramIntent;
  }
  
  public ComponentName getActivity() {
    return this.mActivity;
  }
  
  public Set<String> getCategories() {
    return this.mCategories;
  }
  
  public CharSequence getDisabledMessage() {
    return this.mDisabledMessage;
  }
  
  public int getDisabledReason() {
    return this.mDisabledReason;
  }
  
  public PersistableBundle getExtras() {
    return this.mExtras;
  }
  
  public IconCompat getIcon() {
    return this.mIcon;
  }
  
  public String getId() {
    return this.mId;
  }
  
  public Intent getIntent() {
    Intent[] arrayOfIntent = this.mIntents;
    return arrayOfIntent[arrayOfIntent.length - 1];
  }
  
  public Intent[] getIntents() {
    Intent[] arrayOfIntent = this.mIntents;
    return Arrays.<Intent>copyOf(arrayOfIntent, arrayOfIntent.length);
  }
  
  public long getLastChangedTimestamp() {
    return this.mLastChangedTimestamp;
  }
  
  public LocusIdCompat getLocusId() {
    return this.mLocusId;
  }
  
  public CharSequence getLongLabel() {
    return this.mLongLabel;
  }
  
  public String getPackage() {
    return this.mPackageName;
  }
  
  public int getRank() {
    return this.mRank;
  }
  
  public CharSequence getShortLabel() {
    return this.mLabel;
  }
  
  public UserHandle getUserHandle() {
    return this.mUser;
  }
  
  public boolean hasKeyFieldsOnly() {
    return this.mHasKeyFieldsOnly;
  }
  
  public boolean isCached() {
    return this.mIsCached;
  }
  
  public boolean isDeclaredInManifest() {
    return this.mIsDeclaredInManifest;
  }
  
  public boolean isDynamic() {
    return this.mIsDynamic;
  }
  
  public boolean isEnabled() {
    return this.mIsEnabled;
  }
  
  public boolean isImmutable() {
    return this.mIsImmutable;
  }
  
  public boolean isPinned() {
    return this.mIsPinned;
  }
  
  public ShortcutInfo toShortcutInfo() {
    ShortcutInfo.Builder builder = (new ShortcutInfo.Builder(this.mContext, this.mId)).setShortLabel(this.mLabel).setIntents(this.mIntents);
    IconCompat iconCompat = this.mIcon;
    if (iconCompat != null)
      builder.setIcon(iconCompat.toIcon(this.mContext)); 
    if (!TextUtils.isEmpty(this.mLongLabel))
      builder.setLongLabel(this.mLongLabel); 
    if (!TextUtils.isEmpty(this.mDisabledMessage))
      builder.setDisabledMessage(this.mDisabledMessage); 
    ComponentName componentName = this.mActivity;
    if (componentName != null)
      builder.setActivity(componentName); 
    Set<String> set = this.mCategories;
    if (set != null)
      builder.setCategories(set); 
    builder.setRank(this.mRank);
    PersistableBundle persistableBundle = this.mExtras;
    if (persistableBundle != null)
      builder.setExtras(persistableBundle); 
    if (Build.VERSION.SDK_INT >= 29) {
      Person[] arrayOfPerson = this.mPersons;
      if (arrayOfPerson != null && arrayOfPerson.length > 0) {
        Person[] arrayOfPerson1 = new Person[arrayOfPerson.length];
        for (byte b = 0; b < arrayOfPerson1.length; b++)
          arrayOfPerson1[b] = this.mPersons[b].toAndroidPerson(); 
        builder.setPersons(arrayOfPerson1);
      } 
      LocusIdCompat locusIdCompat = this.mLocusId;
      if (locusIdCompat != null)
        builder.setLocusId(locusIdCompat.toLocusId()); 
      builder.setLongLived(this.mIsLongLived);
    } else {
      builder.setExtras(buildLegacyExtrasBundle());
    } 
    return builder.build();
  }
  
  public static class Builder {
    private Map<String, Map<String, List<String>>> mCapabilityBindingParams;
    
    private Set<String> mCapabilityBindings;
    
    private final ShortcutInfoCompat mInfo;
    
    private boolean mIsConversation;
    
    private Uri mSliceUri;
    
    public Builder(Context param1Context, ShortcutInfo param1ShortcutInfo) {
      ShortcutInfoCompat shortcutInfoCompat = new ShortcutInfoCompat();
      this.mInfo = shortcutInfoCompat;
      shortcutInfoCompat.mContext = param1Context;
      shortcutInfoCompat.mId = param1ShortcutInfo.getId();
      shortcutInfoCompat.mPackageName = param1ShortcutInfo.getPackage();
      Intent[] arrayOfIntent = param1ShortcutInfo.getIntents();
      shortcutInfoCompat.mIntents = Arrays.<Intent>copyOf(arrayOfIntent, arrayOfIntent.length);
      shortcutInfoCompat.mActivity = param1ShortcutInfo.getActivity();
      shortcutInfoCompat.mLabel = param1ShortcutInfo.getShortLabel();
      shortcutInfoCompat.mLongLabel = param1ShortcutInfo.getLongLabel();
      shortcutInfoCompat.mDisabledMessage = param1ShortcutInfo.getDisabledMessage();
      if (Build.VERSION.SDK_INT >= 28) {
        shortcutInfoCompat.mDisabledReason = param1ShortcutInfo.getDisabledReason();
      } else {
        byte b;
        if (param1ShortcutInfo.isEnabled()) {
          b = 0;
        } else {
          b = 3;
        } 
        shortcutInfoCompat.mDisabledReason = b;
      } 
      shortcutInfoCompat.mCategories = param1ShortcutInfo.getCategories();
      shortcutInfoCompat.mPersons = ShortcutInfoCompat.getPersonsFromExtra(param1ShortcutInfo.getExtras());
      shortcutInfoCompat.mUser = param1ShortcutInfo.getUserHandle();
      shortcutInfoCompat.mLastChangedTimestamp = param1ShortcutInfo.getLastChangedTimestamp();
      if (Build.VERSION.SDK_INT >= 30)
        shortcutInfoCompat.mIsCached = param1ShortcutInfo.isCached(); 
      shortcutInfoCompat.mIsDynamic = param1ShortcutInfo.isDynamic();
      shortcutInfoCompat.mIsPinned = param1ShortcutInfo.isPinned();
      shortcutInfoCompat.mIsDeclaredInManifest = param1ShortcutInfo.isDeclaredInManifest();
      shortcutInfoCompat.mIsImmutable = param1ShortcutInfo.isImmutable();
      shortcutInfoCompat.mIsEnabled = param1ShortcutInfo.isEnabled();
      shortcutInfoCompat.mHasKeyFieldsOnly = param1ShortcutInfo.hasKeyFieldsOnly();
      shortcutInfoCompat.mLocusId = ShortcutInfoCompat.getLocusId(param1ShortcutInfo);
      shortcutInfoCompat.mRank = param1ShortcutInfo.getRank();
      shortcutInfoCompat.mExtras = param1ShortcutInfo.getExtras();
    }
    
    public Builder(Context param1Context, String param1String) {
      ShortcutInfoCompat shortcutInfoCompat = new ShortcutInfoCompat();
      this.mInfo = shortcutInfoCompat;
      shortcutInfoCompat.mContext = param1Context;
      shortcutInfoCompat.mId = param1String;
    }
    
    public Builder(ShortcutInfoCompat param1ShortcutInfoCompat) {
      ShortcutInfoCompat shortcutInfoCompat = new ShortcutInfoCompat();
      this.mInfo = shortcutInfoCompat;
      shortcutInfoCompat.mContext = param1ShortcutInfoCompat.mContext;
      shortcutInfoCompat.mId = param1ShortcutInfoCompat.mId;
      shortcutInfoCompat.mPackageName = param1ShortcutInfoCompat.mPackageName;
      shortcutInfoCompat.mIntents = Arrays.<Intent>copyOf(param1ShortcutInfoCompat.mIntents, param1ShortcutInfoCompat.mIntents.length);
      shortcutInfoCompat.mActivity = param1ShortcutInfoCompat.mActivity;
      shortcutInfoCompat.mLabel = param1ShortcutInfoCompat.mLabel;
      shortcutInfoCompat.mLongLabel = param1ShortcutInfoCompat.mLongLabel;
      shortcutInfoCompat.mDisabledMessage = param1ShortcutInfoCompat.mDisabledMessage;
      shortcutInfoCompat.mDisabledReason = param1ShortcutInfoCompat.mDisabledReason;
      shortcutInfoCompat.mIcon = param1ShortcutInfoCompat.mIcon;
      shortcutInfoCompat.mIsAlwaysBadged = param1ShortcutInfoCompat.mIsAlwaysBadged;
      shortcutInfoCompat.mUser = param1ShortcutInfoCompat.mUser;
      shortcutInfoCompat.mLastChangedTimestamp = param1ShortcutInfoCompat.mLastChangedTimestamp;
      shortcutInfoCompat.mIsCached = param1ShortcutInfoCompat.mIsCached;
      shortcutInfoCompat.mIsDynamic = param1ShortcutInfoCompat.mIsDynamic;
      shortcutInfoCompat.mIsPinned = param1ShortcutInfoCompat.mIsPinned;
      shortcutInfoCompat.mIsDeclaredInManifest = param1ShortcutInfoCompat.mIsDeclaredInManifest;
      shortcutInfoCompat.mIsImmutable = param1ShortcutInfoCompat.mIsImmutable;
      shortcutInfoCompat.mIsEnabled = param1ShortcutInfoCompat.mIsEnabled;
      shortcutInfoCompat.mLocusId = param1ShortcutInfoCompat.mLocusId;
      shortcutInfoCompat.mIsLongLived = param1ShortcutInfoCompat.mIsLongLived;
      shortcutInfoCompat.mHasKeyFieldsOnly = param1ShortcutInfoCompat.mHasKeyFieldsOnly;
      shortcutInfoCompat.mRank = param1ShortcutInfoCompat.mRank;
      if (param1ShortcutInfoCompat.mPersons != null)
        shortcutInfoCompat.mPersons = Arrays.<Person>copyOf(param1ShortcutInfoCompat.mPersons, param1ShortcutInfoCompat.mPersons.length); 
      if (param1ShortcutInfoCompat.mCategories != null)
        shortcutInfoCompat.mCategories = new HashSet<>(param1ShortcutInfoCompat.mCategories); 
      if (param1ShortcutInfoCompat.mExtras != null)
        shortcutInfoCompat.mExtras = param1ShortcutInfoCompat.mExtras; 
    }
    
    public Builder addCapabilityBinding(String param1String) {
      if (this.mCapabilityBindings == null)
        this.mCapabilityBindings = new HashSet<>(); 
      this.mCapabilityBindings.add(param1String);
      return this;
    }
    
    public Builder addCapabilityBinding(String param1String1, String param1String2, List<String> param1List) {
      addCapabilityBinding(param1String1);
      if (!param1List.isEmpty()) {
        if (this.mCapabilityBindingParams == null)
          this.mCapabilityBindingParams = new HashMap<>(); 
        if (this.mCapabilityBindingParams.get(param1String1) == null)
          this.mCapabilityBindingParams.put(param1String1, new HashMap<>()); 
        ((Map<String, List<String>>)this.mCapabilityBindingParams.get(param1String1)).put(param1String2, param1List);
      } 
      return this;
    }
    
    public ShortcutInfoCompat build() {
      if (!TextUtils.isEmpty(this.mInfo.mLabel)) {
        if (this.mInfo.mIntents != null && this.mInfo.mIntents.length != 0) {
          if (this.mIsConversation) {
            if (this.mInfo.mLocusId == null)
              this.mInfo.mLocusId = new LocusIdCompat(this.mInfo.mId); 
            this.mInfo.mIsLongLived = true;
          } 
          if (this.mCapabilityBindings != null) {
            if (this.mInfo.mCategories == null)
              this.mInfo.mCategories = new HashSet<>(); 
            this.mInfo.mCategories.addAll(this.mCapabilityBindings);
          } 
          if (Build.VERSION.SDK_INT >= 21) {
            if (this.mCapabilityBindingParams != null) {
              if (this.mInfo.mExtras == null)
                this.mInfo.mExtras = new PersistableBundle(); 
              for (String str : this.mCapabilityBindingParams.keySet()) {
                Map map = this.mCapabilityBindingParams.get(str);
                Set set = map.keySet();
                this.mInfo.mExtras.putStringArray(str, (String[])set.toArray((Object[])new String[0]));
                for (String str1 : map.keySet()) {
                  List list = (List)map.get(str1);
                  PersistableBundle persistableBundle = this.mInfo.mExtras;
                  String str2 = str + "/" + str1;
                  String[] arrayOfString = new String[0];
                  if (list != null)
                    arrayOfString = (String[])list.toArray((Object[])arrayOfString); 
                  persistableBundle.putStringArray(str2, arrayOfString);
                } 
              } 
            } 
            if (this.mSliceUri != null) {
              if (this.mInfo.mExtras == null)
                this.mInfo.mExtras = new PersistableBundle(); 
              this.mInfo.mExtras.putString("extraSliceUri", UriCompat.toSafeString(this.mSliceUri));
            } 
          } 
          return this.mInfo;
        } 
        throw new IllegalArgumentException("Shortcut must have an intent");
      } 
      throw new IllegalArgumentException("Shortcut must have a non-empty label");
    }
    
    public Builder setActivity(ComponentName param1ComponentName) {
      this.mInfo.mActivity = param1ComponentName;
      return this;
    }
    
    public Builder setAlwaysBadged() {
      this.mInfo.mIsAlwaysBadged = true;
      return this;
    }
    
    public Builder setCategories(Set<String> param1Set) {
      this.mInfo.mCategories = param1Set;
      return this;
    }
    
    public Builder setDisabledMessage(CharSequence param1CharSequence) {
      this.mInfo.mDisabledMessage = param1CharSequence;
      return this;
    }
    
    public Builder setExtras(PersistableBundle param1PersistableBundle) {
      this.mInfo.mExtras = param1PersistableBundle;
      return this;
    }
    
    public Builder setIcon(IconCompat param1IconCompat) {
      this.mInfo.mIcon = param1IconCompat;
      return this;
    }
    
    public Builder setIntent(Intent param1Intent) {
      return setIntents(new Intent[] { param1Intent });
    }
    
    public Builder setIntents(Intent[] param1ArrayOfIntent) {
      this.mInfo.mIntents = param1ArrayOfIntent;
      return this;
    }
    
    public Builder setIsConversation() {
      this.mIsConversation = true;
      return this;
    }
    
    public Builder setLocusId(LocusIdCompat param1LocusIdCompat) {
      this.mInfo.mLocusId = param1LocusIdCompat;
      return this;
    }
    
    public Builder setLongLabel(CharSequence param1CharSequence) {
      this.mInfo.mLongLabel = param1CharSequence;
      return this;
    }
    
    @Deprecated
    public Builder setLongLived() {
      this.mInfo.mIsLongLived = true;
      return this;
    }
    
    public Builder setLongLived(boolean param1Boolean) {
      this.mInfo.mIsLongLived = param1Boolean;
      return this;
    }
    
    public Builder setPerson(Person param1Person) {
      return setPersons(new Person[] { param1Person });
    }
    
    public Builder setPersons(Person[] param1ArrayOfPerson) {
      this.mInfo.mPersons = param1ArrayOfPerson;
      return this;
    }
    
    public Builder setRank(int param1Int) {
      this.mInfo.mRank = param1Int;
      return this;
    }
    
    public Builder setShortLabel(CharSequence param1CharSequence) {
      this.mInfo.mLabel = param1CharSequence;
      return this;
    }
    
    public Builder setSliceUri(Uri param1Uri) {
      this.mSliceUri = param1Uri;
      return this;
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\content\pm\ShortcutInfoCompat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */