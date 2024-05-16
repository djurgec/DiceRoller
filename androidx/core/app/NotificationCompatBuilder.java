package androidx.core.app;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.content.Context;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.SparseArray;
import android.widget.RemoteViews;
import androidx.collection.ArraySet;
import androidx.core.graphics.drawable.IconCompat;
import androidx.core.os.BuildCompat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

class NotificationCompatBuilder implements NotificationBuilderWithBuilderAccessor {
  private final List<Bundle> mActionExtrasList;
  
  private RemoteViews mBigContentView;
  
  private final Notification.Builder mBuilder;
  
  private final NotificationCompat.Builder mBuilderCompat;
  
  private RemoteViews mContentView;
  
  private final Context mContext;
  
  private final Bundle mExtras;
  
  private int mGroupAlertBehavior;
  
  private RemoteViews mHeadsUpContentView;
  
  NotificationCompatBuilder(NotificationCompat.Builder paramBuilder) {
    boolean bool;
    this.mActionExtrasList = new ArrayList<>();
    this.mExtras = new Bundle();
    this.mBuilderCompat = paramBuilder;
    this.mContext = paramBuilder.mContext;
    if (Build.VERSION.SDK_INT >= 26) {
      this.mBuilder = new Notification.Builder(paramBuilder.mContext, paramBuilder.mChannelId);
    } else {
      this.mBuilder = new Notification.Builder(paramBuilder.mContext);
    } 
    Notification notification = paramBuilder.mNotification;
    Notification.Builder builder1 = this.mBuilder.setWhen(notification.when).setSmallIcon(notification.icon, notification.iconLevel).setContent(notification.contentView).setTicker(notification.tickerText, paramBuilder.mTickerView).setVibrate(notification.vibrate).setLights(notification.ledARGB, notification.ledOnMS, notification.ledOffMS);
    if ((notification.flags & 0x2) != 0) {
      bool = true;
    } else {
      bool = false;
    } 
    builder1 = builder1.setOngoing(bool);
    if ((notification.flags & 0x8) != 0) {
      bool = true;
    } else {
      bool = false;
    } 
    builder1 = builder1.setOnlyAlertOnce(bool);
    if ((notification.flags & 0x10) != 0) {
      bool = true;
    } else {
      bool = false;
    } 
    Notification.Builder builder2 = builder1.setAutoCancel(bool).setDefaults(notification.defaults).setContentTitle(paramBuilder.mContentTitle).setContentText(paramBuilder.mContentText).setContentInfo(paramBuilder.mContentInfo).setContentIntent(paramBuilder.mContentIntent).setDeleteIntent(notification.deleteIntent);
    PendingIntent pendingIntent = paramBuilder.mFullScreenIntent;
    if ((notification.flags & 0x80) != 0) {
      bool = true;
    } else {
      bool = false;
    } 
    builder2.setFullScreenIntent(pendingIntent, bool).setLargeIcon(paramBuilder.mLargeIcon).setNumber(paramBuilder.mNumber).setProgress(paramBuilder.mProgressMax, paramBuilder.mProgress, paramBuilder.mProgressIndeterminate);
    if (Build.VERSION.SDK_INT < 21)
      this.mBuilder.setSound(notification.sound, notification.audioStreamType); 
    if (Build.VERSION.SDK_INT >= 16) {
      this.mBuilder.setSubText(paramBuilder.mSubText).setUsesChronometer(paramBuilder.mUseChronometer).setPriority(paramBuilder.mPriority);
      Iterator<NotificationCompat.Action> iterator = paramBuilder.mActions.iterator();
      while (iterator.hasNext())
        addAction(iterator.next()); 
      if (paramBuilder.mExtras != null)
        this.mExtras.putAll(paramBuilder.mExtras); 
      if (Build.VERSION.SDK_INT < 20) {
        if (paramBuilder.mLocalOnly)
          this.mExtras.putBoolean("android.support.localOnly", true); 
        if (paramBuilder.mGroupKey != null) {
          this.mExtras.putString("android.support.groupKey", paramBuilder.mGroupKey);
          if (paramBuilder.mGroupSummary) {
            this.mExtras.putBoolean("android.support.isGroupSummary", true);
          } else {
            this.mExtras.putBoolean("android.support.useSideChannel", true);
          } 
        } 
        if (paramBuilder.mSortKey != null)
          this.mExtras.putString("android.support.sortKey", paramBuilder.mSortKey); 
      } 
      this.mContentView = paramBuilder.mContentView;
      this.mBigContentView = paramBuilder.mBigContentView;
    } 
    if (Build.VERSION.SDK_INT >= 17)
      this.mBuilder.setShowWhen(paramBuilder.mShowWhen); 
    if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
      List<String> list = combineLists(getPeople(paramBuilder.mPersonList), paramBuilder.mPeople);
      if (list != null && !list.isEmpty())
        this.mExtras.putStringArray("android.people", list.<String>toArray(new String[list.size()])); 
    } 
    if (Build.VERSION.SDK_INT >= 20) {
      this.mBuilder.setLocalOnly(paramBuilder.mLocalOnly).setGroup(paramBuilder.mGroupKey).setGroupSummary(paramBuilder.mGroupSummary).setSortKey(paramBuilder.mSortKey);
      this.mGroupAlertBehavior = paramBuilder.mGroupAlertBehavior;
    } 
    if (Build.VERSION.SDK_INT >= 21) {
      List<String> list;
      this.mBuilder.setCategory(paramBuilder.mCategory).setColor(paramBuilder.mColor).setVisibility(paramBuilder.mVisibility).setPublicVersion(paramBuilder.mPublicVersion).setSound(notification.sound, notification.audioAttributes);
      if (Build.VERSION.SDK_INT < 28) {
        list = combineLists(getPeople(paramBuilder.mPersonList), paramBuilder.mPeople);
      } else {
        list = paramBuilder.mPeople;
      } 
      if (list != null && !list.isEmpty())
        for (String str : list)
          this.mBuilder.addPerson(str);  
      this.mHeadsUpContentView = paramBuilder.mHeadsUpContentView;
      if (paramBuilder.mInvisibleActions.size() > 0) {
        Bundle bundle2 = paramBuilder.getExtras().getBundle("android.car.EXTENSIONS");
        Bundle bundle1 = bundle2;
        if (bundle2 == null)
          bundle1 = new Bundle(); 
        Bundle bundle3 = new Bundle(bundle1);
        bundle2 = new Bundle();
        for (byte b = 0; b < paramBuilder.mInvisibleActions.size(); b++)
          bundle2.putBundle(Integer.toString(b), NotificationCompatJellybean.getBundleForAction(paramBuilder.mInvisibleActions.get(b))); 
        bundle1.putBundle("invisible_actions", bundle2);
        bundle3.putBundle("invisible_actions", bundle2);
        paramBuilder.getExtras().putBundle("android.car.EXTENSIONS", bundle1);
        this.mExtras.putBundle("android.car.EXTENSIONS", bundle3);
      } 
    } 
    if (Build.VERSION.SDK_INT >= 23 && paramBuilder.mSmallIcon != null)
      this.mBuilder.setSmallIcon(paramBuilder.mSmallIcon); 
    if (Build.VERSION.SDK_INT >= 24) {
      this.mBuilder.setExtras(paramBuilder.mExtras).setRemoteInputHistory(paramBuilder.mRemoteInputHistory);
      if (paramBuilder.mContentView != null)
        this.mBuilder.setCustomContentView(paramBuilder.mContentView); 
      if (paramBuilder.mBigContentView != null)
        this.mBuilder.setCustomBigContentView(paramBuilder.mBigContentView); 
      if (paramBuilder.mHeadsUpContentView != null)
        this.mBuilder.setCustomHeadsUpContentView(paramBuilder.mHeadsUpContentView); 
    } 
    if (Build.VERSION.SDK_INT >= 26) {
      this.mBuilder.setBadgeIconType(paramBuilder.mBadgeIcon).setSettingsText(paramBuilder.mSettingsText).setShortcutId(paramBuilder.mShortcutId).setTimeoutAfter(paramBuilder.mTimeout).setGroupAlertBehavior(paramBuilder.mGroupAlertBehavior);
      if (paramBuilder.mColorizedSet)
        this.mBuilder.setColorized(paramBuilder.mColorized); 
      if (!TextUtils.isEmpty(paramBuilder.mChannelId))
        this.mBuilder.setSound(null).setDefaults(0).setLights(0, 0, 0).setVibrate(null); 
    } 
    if (Build.VERSION.SDK_INT >= 28)
      for (Person person : paramBuilder.mPersonList)
        this.mBuilder.addPerson(person.toAndroidPerson());  
    if (Build.VERSION.SDK_INT >= 29) {
      this.mBuilder.setAllowSystemGeneratedContextualActions(paramBuilder.mAllowSystemGeneratedContextualActions);
      this.mBuilder.setBubbleMetadata(NotificationCompat.BubbleMetadata.toPlatform(paramBuilder.mBubbleMetadata));
      if (paramBuilder.mLocusId != null)
        this.mBuilder.setLocusId(paramBuilder.mLocusId.toLocusId()); 
    } 
    if (BuildCompat.isAtLeastS() && paramBuilder.mFgsDeferBehavior != 0)
      this.mBuilder.setForegroundServiceBehavior(paramBuilder.mFgsDeferBehavior); 
    if (paramBuilder.mSilent) {
      if (this.mBuilderCompat.mGroupSummary) {
        this.mGroupAlertBehavior = 2;
      } else {
        this.mGroupAlertBehavior = 1;
      } 
      this.mBuilder.setVibrate(null);
      this.mBuilder.setSound(null);
      notification.defaults &= 0xFFFFFFFE;
      notification.defaults &= 0xFFFFFFFD;
      this.mBuilder.setDefaults(notification.defaults);
      if (Build.VERSION.SDK_INT >= 26) {
        if (TextUtils.isEmpty(this.mBuilderCompat.mGroupKey))
          this.mBuilder.setGroup("silent"); 
        this.mBuilder.setGroupAlertBehavior(this.mGroupAlertBehavior);
      } 
    } 
  }
  
  private void addAction(NotificationCompat.Action paramAction) {
    if (Build.VERSION.SDK_INT >= 20) {
      Notification.Action.Builder builder;
      Bundle bundle;
      IconCompat iconCompat = paramAction.getIconCompat();
      int i = Build.VERSION.SDK_INT;
      boolean bool = false;
      if (i >= 23) {
        if (iconCompat != null) {
          Icon icon = iconCompat.toIcon();
        } else {
          iconCompat = null;
        } 
        builder = new Notification.Action.Builder((Icon)iconCompat, paramAction.getTitle(), paramAction.getActionIntent());
      } else {
        if (builder != null) {
          i = builder.getResId();
        } else {
          i = 0;
        } 
        builder = new Notification.Action.Builder(i, paramAction.getTitle(), paramAction.getActionIntent());
      } 
      if (paramAction.getRemoteInputs() != null) {
        RemoteInput[] arrayOfRemoteInput = RemoteInput.fromCompat(paramAction.getRemoteInputs());
        int j = arrayOfRemoteInput.length;
        for (i = bool; i < j; i++)
          builder.addRemoteInput(arrayOfRemoteInput[i]); 
      } 
      if (paramAction.getExtras() != null) {
        bundle = new Bundle(paramAction.getExtras());
      } else {
        bundle = new Bundle();
      } 
      bundle.putBoolean("android.support.allowGeneratedReplies", paramAction.getAllowGeneratedReplies());
      if (Build.VERSION.SDK_INT >= 24)
        builder.setAllowGeneratedReplies(paramAction.getAllowGeneratedReplies()); 
      bundle.putInt("android.support.action.semanticAction", paramAction.getSemanticAction());
      if (Build.VERSION.SDK_INT >= 28)
        builder.setSemanticAction(paramAction.getSemanticAction()); 
      if (Build.VERSION.SDK_INT >= 29)
        builder.setContextual(paramAction.isContextual()); 
      bundle.putBoolean("android.support.action.showsUserInterface", paramAction.getShowsUserInterface());
      builder.addExtras(bundle);
      this.mBuilder.addAction(builder.build());
    } else if (Build.VERSION.SDK_INT >= 16) {
      this.mActionExtrasList.add(NotificationCompatJellybean.writeActionAndGetExtras(this.mBuilder, paramAction));
    } 
  }
  
  private static List<String> combineLists(List<String> paramList1, List<String> paramList2) {
    if (paramList1 == null)
      return paramList2; 
    if (paramList2 == null)
      return paramList1; 
    ArraySet arraySet = new ArraySet(paramList1.size() + paramList2.size());
    arraySet.addAll(paramList1);
    arraySet.addAll(paramList2);
    return new ArrayList<>((Collection<? extends String>)arraySet);
  }
  
  private static List<String> getPeople(List<Person> paramList) {
    if (paramList == null)
      return null; 
    ArrayList<String> arrayList = new ArrayList(paramList.size());
    Iterator<Person> iterator = paramList.iterator();
    while (iterator.hasNext())
      arrayList.add(((Person)iterator.next()).resolveToLegacyUri()); 
    return arrayList;
  }
  
  private void removeSoundAndVibration(Notification paramNotification) {
    paramNotification.sound = null;
    paramNotification.vibrate = null;
    paramNotification.defaults &= 0xFFFFFFFE;
    paramNotification.defaults &= 0xFFFFFFFD;
  }
  
  public Notification build() {
    RemoteViews remoteViews;
    NotificationCompat.Style style = this.mBuilderCompat.mStyle;
    if (style != null)
      style.apply(this); 
    if (style != null) {
      remoteViews = style.makeContentView(this);
    } else {
      remoteViews = null;
    } 
    Notification notification = buildInternal();
    if (remoteViews != null) {
      notification.contentView = remoteViews;
    } else if (this.mBuilderCompat.mContentView != null) {
      notification.contentView = this.mBuilderCompat.mContentView;
    } 
    if (Build.VERSION.SDK_INT >= 16 && style != null) {
      remoteViews = style.makeBigContentView(this);
      if (remoteViews != null)
        notification.bigContentView = remoteViews; 
    } 
    if (Build.VERSION.SDK_INT >= 21 && style != null) {
      remoteViews = this.mBuilderCompat.mStyle.makeHeadsUpContentView(this);
      if (remoteViews != null)
        notification.headsUpContentView = remoteViews; 
    } 
    if (Build.VERSION.SDK_INT >= 16 && style != null) {
      Bundle bundle = NotificationCompat.getExtras(notification);
      if (bundle != null)
        style.addCompatExtras(bundle); 
    } 
    return notification;
  }
  
  protected Notification buildInternal() {
    if (Build.VERSION.SDK_INT >= 26)
      return this.mBuilder.build(); 
    if (Build.VERSION.SDK_INT >= 24) {
      Notification notification = this.mBuilder.build();
      if (this.mGroupAlertBehavior != 0) {
        if (notification.getGroup() != null && (notification.flags & 0x200) != 0 && this.mGroupAlertBehavior == 2)
          removeSoundAndVibration(notification); 
        if (notification.getGroup() != null && (notification.flags & 0x200) == 0 && this.mGroupAlertBehavior == 1)
          removeSoundAndVibration(notification); 
      } 
      return notification;
    } 
    if (Build.VERSION.SDK_INT >= 21) {
      this.mBuilder.setExtras(this.mExtras);
      Notification notification = this.mBuilder.build();
      RemoteViews remoteViews = this.mContentView;
      if (remoteViews != null)
        notification.contentView = remoteViews; 
      remoteViews = this.mBigContentView;
      if (remoteViews != null)
        notification.bigContentView = remoteViews; 
      remoteViews = this.mHeadsUpContentView;
      if (remoteViews != null)
        notification.headsUpContentView = remoteViews; 
      if (this.mGroupAlertBehavior != 0) {
        if (notification.getGroup() != null && (notification.flags & 0x200) != 0 && this.mGroupAlertBehavior == 2)
          removeSoundAndVibration(notification); 
        if (notification.getGroup() != null && (notification.flags & 0x200) == 0 && this.mGroupAlertBehavior == 1)
          removeSoundAndVibration(notification); 
      } 
      return notification;
    } 
    if (Build.VERSION.SDK_INT >= 20) {
      this.mBuilder.setExtras(this.mExtras);
      Notification notification = this.mBuilder.build();
      RemoteViews remoteViews = this.mContentView;
      if (remoteViews != null)
        notification.contentView = remoteViews; 
      remoteViews = this.mBigContentView;
      if (remoteViews != null)
        notification.bigContentView = remoteViews; 
      if (this.mGroupAlertBehavior != 0) {
        if (notification.getGroup() != null && (notification.flags & 0x200) != 0 && this.mGroupAlertBehavior == 2)
          removeSoundAndVibration(notification); 
        if (notification.getGroup() != null && (notification.flags & 0x200) == 0 && this.mGroupAlertBehavior == 1)
          removeSoundAndVibration(notification); 
      } 
      return notification;
    } 
    if (Build.VERSION.SDK_INT >= 19) {
      SparseArray<Bundle> sparseArray = NotificationCompatJellybean.buildActionExtrasMap(this.mActionExtrasList);
      if (sparseArray != null)
        this.mExtras.putSparseParcelableArray("android.support.actionExtras", sparseArray); 
      this.mBuilder.setExtras(this.mExtras);
      Notification notification = this.mBuilder.build();
      RemoteViews remoteViews = this.mContentView;
      if (remoteViews != null)
        notification.contentView = remoteViews; 
      remoteViews = this.mBigContentView;
      if (remoteViews != null)
        notification.bigContentView = remoteViews; 
      return notification;
    } 
    if (Build.VERSION.SDK_INT >= 16) {
      Notification notification = this.mBuilder.build();
      Bundle bundle2 = NotificationCompat.getExtras(notification);
      Bundle bundle1 = new Bundle(this.mExtras);
      for (String str : this.mExtras.keySet()) {
        if (bundle2.containsKey(str))
          bundle1.remove(str); 
      } 
      bundle2.putAll(bundle1);
      SparseArray<Bundle> sparseArray = NotificationCompatJellybean.buildActionExtrasMap(this.mActionExtrasList);
      if (sparseArray != null)
        NotificationCompat.getExtras(notification).putSparseParcelableArray("android.support.actionExtras", sparseArray); 
      RemoteViews remoteViews = this.mContentView;
      if (remoteViews != null)
        notification.contentView = remoteViews; 
      remoteViews = this.mBigContentView;
      if (remoteViews != null)
        notification.bigContentView = remoteViews; 
      return notification;
    } 
    return this.mBuilder.getNotification();
  }
  
  public Notification.Builder getBuilder() {
    return this.mBuilder;
  }
  
  Context getContext() {
    return this.mContext;
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\app\NotificationCompatBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */