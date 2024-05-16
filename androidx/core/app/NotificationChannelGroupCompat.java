package androidx.core.app;

import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.os.Build;
import androidx.core.util.Preconditions;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NotificationChannelGroupCompat {
  private boolean mBlocked;
  
  private List<NotificationChannelCompat> mChannels = Collections.emptyList();
  
  String mDescription;
  
  final String mId;
  
  CharSequence mName;
  
  NotificationChannelGroupCompat(NotificationChannelGroup paramNotificationChannelGroup) {
    this(paramNotificationChannelGroup, Collections.emptyList());
  }
  
  NotificationChannelGroupCompat(NotificationChannelGroup paramNotificationChannelGroup, List<NotificationChannel> paramList) {
    this(paramNotificationChannelGroup.getId());
    this.mName = paramNotificationChannelGroup.getName();
    if (Build.VERSION.SDK_INT >= 28)
      this.mDescription = paramNotificationChannelGroup.getDescription(); 
    if (Build.VERSION.SDK_INT >= 28) {
      this.mBlocked = paramNotificationChannelGroup.isBlocked();
      this.mChannels = getChannelsCompat(paramNotificationChannelGroup.getChannels());
    } else {
      this.mChannels = getChannelsCompat(paramList);
    } 
  }
  
  NotificationChannelGroupCompat(String paramString) {
    this.mId = (String)Preconditions.checkNotNull(paramString);
  }
  
  private List<NotificationChannelCompat> getChannelsCompat(List<NotificationChannel> paramList) {
    ArrayList<NotificationChannelCompat> arrayList = new ArrayList();
    for (NotificationChannel notificationChannel : paramList) {
      if (this.mId.equals(notificationChannel.getGroup()))
        arrayList.add(new NotificationChannelCompat(notificationChannel)); 
    } 
    return arrayList;
  }
  
  public List<NotificationChannelCompat> getChannels() {
    return this.mChannels;
  }
  
  public String getDescription() {
    return this.mDescription;
  }
  
  public String getId() {
    return this.mId;
  }
  
  public CharSequence getName() {
    return this.mName;
  }
  
  NotificationChannelGroup getNotificationChannelGroup() {
    if (Build.VERSION.SDK_INT < 26)
      return null; 
    NotificationChannelGroup notificationChannelGroup = new NotificationChannelGroup(this.mId, this.mName);
    if (Build.VERSION.SDK_INT >= 28)
      notificationChannelGroup.setDescription(this.mDescription); 
    return notificationChannelGroup;
  }
  
  public boolean isBlocked() {
    return this.mBlocked;
  }
  
  public Builder toBuilder() {
    return (new Builder(this.mId)).setName(this.mName).setDescription(this.mDescription);
  }
  
  public static class Builder {
    final NotificationChannelGroupCompat mGroup;
    
    public Builder(String param1String) {
      this.mGroup = new NotificationChannelGroupCompat(param1String);
    }
    
    public NotificationChannelGroupCompat build() {
      return this.mGroup;
    }
    
    public Builder setDescription(String param1String) {
      this.mGroup.mDescription = param1String;
      return this;
    }
    
    public Builder setName(CharSequence param1CharSequence) {
      this.mGroup.mName = param1CharSequence;
      return this;
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\app\NotificationChannelGroupCompat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */