package androidx.core.app;

import android.app.Notification;
import android.app.NotificationChannel;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import androidx.core.util.Preconditions;

public class NotificationChannelCompat {
  public static final String DEFAULT_CHANNEL_ID = "miscellaneous";
  
  private static final int DEFAULT_LIGHT_COLOR = 0;
  
  private static final boolean DEFAULT_SHOW_BADGE = true;
  
  AudioAttributes mAudioAttributes;
  
  private boolean mBypassDnd;
  
  private boolean mCanBubble;
  
  String mConversationId;
  
  String mDescription;
  
  String mGroupId;
  
  final String mId;
  
  int mImportance;
  
  private boolean mImportantConversation;
  
  int mLightColor = 0;
  
  boolean mLights;
  
  private int mLockscreenVisibility;
  
  CharSequence mName;
  
  String mParentId;
  
  boolean mShowBadge = true;
  
  Uri mSound = Settings.System.DEFAULT_NOTIFICATION_URI;
  
  boolean mVibrationEnabled;
  
  long[] mVibrationPattern;
  
  NotificationChannelCompat(NotificationChannel paramNotificationChannel) {
    this(paramNotificationChannel.getId(), paramNotificationChannel.getImportance());
    this.mName = paramNotificationChannel.getName();
    this.mDescription = paramNotificationChannel.getDescription();
    this.mGroupId = paramNotificationChannel.getGroup();
    this.mShowBadge = paramNotificationChannel.canShowBadge();
    this.mSound = paramNotificationChannel.getSound();
    this.mAudioAttributes = paramNotificationChannel.getAudioAttributes();
    this.mLights = paramNotificationChannel.shouldShowLights();
    this.mLightColor = paramNotificationChannel.getLightColor();
    this.mVibrationEnabled = paramNotificationChannel.shouldVibrate();
    this.mVibrationPattern = paramNotificationChannel.getVibrationPattern();
    if (Build.VERSION.SDK_INT >= 30) {
      this.mParentId = paramNotificationChannel.getParentChannelId();
      this.mConversationId = paramNotificationChannel.getConversationId();
    } 
    this.mBypassDnd = paramNotificationChannel.canBypassDnd();
    this.mLockscreenVisibility = paramNotificationChannel.getLockscreenVisibility();
    if (Build.VERSION.SDK_INT >= 29)
      this.mCanBubble = paramNotificationChannel.canBubble(); 
    if (Build.VERSION.SDK_INT >= 30)
      this.mImportantConversation = paramNotificationChannel.isImportantConversation(); 
  }
  
  NotificationChannelCompat(String paramString, int paramInt) {
    this.mId = (String)Preconditions.checkNotNull(paramString);
    this.mImportance = paramInt;
    if (Build.VERSION.SDK_INT >= 21)
      this.mAudioAttributes = Notification.AUDIO_ATTRIBUTES_DEFAULT; 
  }
  
  public boolean canBubble() {
    return this.mCanBubble;
  }
  
  public boolean canBypassDnd() {
    return this.mBypassDnd;
  }
  
  public boolean canShowBadge() {
    return this.mShowBadge;
  }
  
  public AudioAttributes getAudioAttributes() {
    return this.mAudioAttributes;
  }
  
  public String getConversationId() {
    return this.mConversationId;
  }
  
  public String getDescription() {
    return this.mDescription;
  }
  
  public String getGroup() {
    return this.mGroupId;
  }
  
  public String getId() {
    return this.mId;
  }
  
  public int getImportance() {
    return this.mImportance;
  }
  
  public int getLightColor() {
    return this.mLightColor;
  }
  
  public int getLockscreenVisibility() {
    return this.mLockscreenVisibility;
  }
  
  public CharSequence getName() {
    return this.mName;
  }
  
  NotificationChannel getNotificationChannel() {
    if (Build.VERSION.SDK_INT < 26)
      return null; 
    NotificationChannel notificationChannel = new NotificationChannel(this.mId, this.mName, this.mImportance);
    notificationChannel.setDescription(this.mDescription);
    notificationChannel.setGroup(this.mGroupId);
    notificationChannel.setShowBadge(this.mShowBadge);
    notificationChannel.setSound(this.mSound, this.mAudioAttributes);
    notificationChannel.enableLights(this.mLights);
    notificationChannel.setLightColor(this.mLightColor);
    notificationChannel.setVibrationPattern(this.mVibrationPattern);
    notificationChannel.enableVibration(this.mVibrationEnabled);
    if (Build.VERSION.SDK_INT >= 30) {
      String str = this.mParentId;
      if (str != null) {
        String str1 = this.mConversationId;
        if (str1 != null)
          notificationChannel.setConversationId(str, str1); 
      } 
    } 
    return notificationChannel;
  }
  
  public String getParentChannelId() {
    return this.mParentId;
  }
  
  public Uri getSound() {
    return this.mSound;
  }
  
  public long[] getVibrationPattern() {
    return this.mVibrationPattern;
  }
  
  public boolean isImportantConversation() {
    return this.mImportantConversation;
  }
  
  public boolean shouldShowLights() {
    return this.mLights;
  }
  
  public boolean shouldVibrate() {
    return this.mVibrationEnabled;
  }
  
  public Builder toBuilder() {
    return (new Builder(this.mId, this.mImportance)).setName(this.mName).setDescription(this.mDescription).setGroup(this.mGroupId).setShowBadge(this.mShowBadge).setSound(this.mSound, this.mAudioAttributes).setLightsEnabled(this.mLights).setLightColor(this.mLightColor).setVibrationEnabled(this.mVibrationEnabled).setVibrationPattern(this.mVibrationPattern).setConversationId(this.mParentId, this.mConversationId);
  }
  
  public static class Builder {
    private final NotificationChannelCompat mChannel;
    
    public Builder(String param1String, int param1Int) {
      this.mChannel = new NotificationChannelCompat(param1String, param1Int);
    }
    
    public NotificationChannelCompat build() {
      return this.mChannel;
    }
    
    public Builder setConversationId(String param1String1, String param1String2) {
      if (Build.VERSION.SDK_INT >= 30) {
        this.mChannel.mParentId = param1String1;
        this.mChannel.mConversationId = param1String2;
      } 
      return this;
    }
    
    public Builder setDescription(String param1String) {
      this.mChannel.mDescription = param1String;
      return this;
    }
    
    public Builder setGroup(String param1String) {
      this.mChannel.mGroupId = param1String;
      return this;
    }
    
    public Builder setImportance(int param1Int) {
      this.mChannel.mImportance = param1Int;
      return this;
    }
    
    public Builder setLightColor(int param1Int) {
      this.mChannel.mLightColor = param1Int;
      return this;
    }
    
    public Builder setLightsEnabled(boolean param1Boolean) {
      this.mChannel.mLights = param1Boolean;
      return this;
    }
    
    public Builder setName(CharSequence param1CharSequence) {
      this.mChannel.mName = param1CharSequence;
      return this;
    }
    
    public Builder setShowBadge(boolean param1Boolean) {
      this.mChannel.mShowBadge = param1Boolean;
      return this;
    }
    
    public Builder setSound(Uri param1Uri, AudioAttributes param1AudioAttributes) {
      this.mChannel.mSound = param1Uri;
      this.mChannel.mAudioAttributes = param1AudioAttributes;
      return this;
    }
    
    public Builder setVibrationEnabled(boolean param1Boolean) {
      this.mChannel.mVibrationEnabled = param1Boolean;
      return this;
    }
    
    public Builder setVibrationPattern(long[] param1ArrayOflong) {
      boolean bool;
      NotificationChannelCompat notificationChannelCompat = this.mChannel;
      if (param1ArrayOflong != null && param1ArrayOflong.length > 0) {
        bool = true;
      } else {
        bool = false;
      } 
      notificationChannelCompat.mVibrationEnabled = bool;
      this.mChannel.mVibrationPattern = param1ArrayOflong;
      return this;
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\app\NotificationChannelCompat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */