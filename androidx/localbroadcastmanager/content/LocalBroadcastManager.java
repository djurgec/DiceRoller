package androidx.localbroadcastmanager.content;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public final class LocalBroadcastManager {
  private static final boolean DEBUG = false;
  
  static final int MSG_EXEC_PENDING_BROADCASTS = 1;
  
  private static final String TAG = "LocalBroadcastManager";
  
  private static LocalBroadcastManager mInstance;
  
  private static final Object mLock = new Object();
  
  private final HashMap<String, ArrayList<ReceiverRecord>> mActions = new HashMap<>();
  
  private final Context mAppContext;
  
  private final Handler mHandler;
  
  private final ArrayList<BroadcastRecord> mPendingBroadcasts = new ArrayList<>();
  
  private final HashMap<BroadcastReceiver, ArrayList<ReceiverRecord>> mReceivers = new HashMap<>();
  
  private LocalBroadcastManager(Context paramContext) {
    this.mAppContext = paramContext;
    this.mHandler = new Handler(paramContext.getMainLooper()) {
        final LocalBroadcastManager this$0;
        
        public void handleMessage(Message param1Message) {
          switch (param1Message.what) {
            default:
              super.handleMessage(param1Message);
              return;
            case 1:
              break;
          } 
          LocalBroadcastManager.this.executePendingBroadcasts();
        }
      };
  }
  
  public static LocalBroadcastManager getInstance(Context paramContext) {
    synchronized (mLock) {
      if (mInstance == null) {
        LocalBroadcastManager localBroadcastManager = new LocalBroadcastManager();
        this(paramContext.getApplicationContext());
        mInstance = localBroadcastManager;
      } 
      return mInstance;
    } 
  }
  
  void executePendingBroadcasts() {
    while (true) {
      Exception exception;
      BroadcastRecord broadcastRecord;
      HashMap<BroadcastReceiver, ArrayList<ReceiverRecord>> hashMap = this.mReceivers;
      /* monitor enter ClassFileLocalVariableReferenceExpression{type=ObjectType{java/util/HashMap<[ObjectType{android/content/BroadcastReceiver}, ObjectType{java/util/ArrayList<InnerObjectType{ObjectType{androidx/localbroadcastmanager/content/LocalBroadcastManager}.Landroidx/localbroadcastmanager/content/LocalBroadcastManager$ReceiverRecord;}>}]>}, name=null} */
      try {
        int i = this.mPendingBroadcasts.size();
        if (i <= 0) {
          /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/util/HashMap<[ObjectType{android/content/BroadcastReceiver}, ObjectType{java/util/ArrayList<InnerObjectType{ObjectType{androidx/localbroadcastmanager/content/LocalBroadcastManager}.Landroidx/localbroadcastmanager/content/LocalBroadcastManager$ReceiverRecord;}>}]>}, name=null} */
          return;
        } 
        BroadcastRecord[] arrayOfBroadcastRecord = new BroadcastRecord[i];
        try {
          this.mPendingBroadcasts.toArray(arrayOfBroadcastRecord);
          this.mPendingBroadcasts.clear();
          /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/util/HashMap<[ObjectType{android/content/BroadcastReceiver}, ObjectType{java/util/ArrayList<InnerObjectType{ObjectType{androidx/localbroadcastmanager/content/LocalBroadcastManager}.Landroidx/localbroadcastmanager/content/LocalBroadcastManager$ReceiverRecord;}>}]>}, name=null} */
          for (i = 0; i < arrayOfBroadcastRecord.length; i++) {
            broadcastRecord = arrayOfBroadcastRecord[i];
            int j = broadcastRecord.receivers.size();
            for (byte b = 0; b < j; b++) {
              ReceiverRecord receiverRecord = broadcastRecord.receivers.get(b);
              if (!receiverRecord.dead)
                receiverRecord.receiver.onReceive(this.mAppContext, broadcastRecord.intent); 
            } 
          } 
          continue;
        } finally {}
      } finally {}
      /* monitor exit ClassFileLocalVariableReferenceExpression{type=InnerObjectType{ObjectType{androidx/localbroadcastmanager/content/LocalBroadcastManager}.Landroidx/localbroadcastmanager/content/LocalBroadcastManager$BroadcastRecord;}, name=null} */
      throw exception;
    } 
  }
  
  public void registerReceiver(BroadcastReceiver paramBroadcastReceiver, IntentFilter paramIntentFilter) {
    synchronized (this.mReceivers) {
      ReceiverRecord receiverRecord = new ReceiverRecord();
      this(paramIntentFilter, paramBroadcastReceiver);
      ArrayList<ReceiverRecord> arrayList2 = this.mReceivers.get(paramBroadcastReceiver);
      ArrayList<ReceiverRecord> arrayList1 = arrayList2;
      if (arrayList2 == null) {
        arrayList1 = new ArrayList();
        this(1);
        this.mReceivers.put(paramBroadcastReceiver, arrayList1);
      } 
      arrayList1.add(receiverRecord);
      for (byte b = 0; b < paramIntentFilter.countActions(); b++) {
        String str = paramIntentFilter.getAction(b);
        arrayList1 = this.mActions.get(str);
        ArrayList<ReceiverRecord> arrayList = arrayList1;
        if (arrayList1 == null) {
          arrayList = new ArrayList<>();
          this(1);
          this.mActions.put(str, arrayList);
        } 
        arrayList.add(receiverRecord);
      } 
      return;
    } 
  }
  
  public boolean sendBroadcast(Intent paramIntent) {
    synchronized (this.mReceivers) {
      byte b;
      String str3 = paramIntent.getAction();
      String str1 = paramIntent.resolveTypeIfNeeded(this.mAppContext.getContentResolver());
      Uri uri = paramIntent.getData();
      String str2 = paramIntent.getScheme();
      Set set = paramIntent.getCategories();
      if ((paramIntent.getFlags() & 0x8) != 0) {
        b = 1;
      } else {
        b = 0;
      } 
      if (b) {
        StringBuilder stringBuilder = new StringBuilder();
        this();
        Log.v("LocalBroadcastManager", stringBuilder.append("Resolving type ").append(str1).append(" scheme ").append(str2).append(" of intent ").append(paramIntent).toString());
      } 
      ArrayList<ReceiverRecord> arrayList = this.mActions.get(paramIntent.getAction());
      if (arrayList != null) {
        if (b) {
          StringBuilder stringBuilder = new StringBuilder();
          this();
          Log.v("LocalBroadcastManager", stringBuilder.append("Action list: ").append(arrayList).toString());
        } 
        ArrayList<ReceiverRecord> arrayList1 = null;
        for (byte b1 = 0; b1 < arrayList.size(); b1++) {
          ReceiverRecord receiverRecord = arrayList.get(b1);
          if (b) {
            StringBuilder stringBuilder = new StringBuilder();
            this();
            Log.v("LocalBroadcastManager", stringBuilder.append("Matching against filter ").append(receiverRecord.filter).toString());
          } 
          if (receiverRecord.broadcasting) {
            if (b)
              Log.v("LocalBroadcastManager", "  Filter's target already added"); 
          } else {
            IntentFilter intentFilter = receiverRecord.filter;
            int i = intentFilter.match(str3, str1, str2, uri, set, "LocalBroadcastManager");
            if (i >= 0) {
              if (b) {
                StringBuilder stringBuilder = new StringBuilder();
                this();
                Log.v("LocalBroadcastManager", stringBuilder.append("  Filter matched!  match=0x").append(Integer.toHexString(i)).toString());
              } 
              if (arrayList1 == null) {
                arrayList1 = new ArrayList();
                this();
              } 
              arrayList1.add(receiverRecord);
              receiverRecord.broadcasting = true;
            } else if (b) {
              String str;
              switch (i) {
                default:
                  str = "unknown reason";
                  break;
                case -1:
                  str = "type";
                  break;
                case -2:
                  str = "data";
                  break;
                case -3:
                  str = "action";
                  break;
                case -4:
                  str = "category";
                  break;
              } 
              StringBuilder stringBuilder = new StringBuilder();
              this();
              Log.v("LocalBroadcastManager", stringBuilder.append("  Filter did not match: ").append(str).toString());
            } 
          } 
        } 
        if (arrayList1 != null) {
          for (b = 0; b < arrayList1.size(); b++)
            ((ReceiverRecord)arrayList1.get(b)).broadcasting = false; 
          ArrayList<BroadcastRecord> arrayList2 = this.mPendingBroadcasts;
          BroadcastRecord broadcastRecord = new BroadcastRecord();
          this(paramIntent, arrayList1);
          arrayList2.add(broadcastRecord);
          if (!this.mHandler.hasMessages(1))
            this.mHandler.sendEmptyMessage(1); 
          return true;
        } 
      } 
      return false;
    } 
  }
  
  public void sendBroadcastSync(Intent paramIntent) {
    if (sendBroadcast(paramIntent))
      executePendingBroadcasts(); 
  }
  
  public void unregisterReceiver(BroadcastReceiver paramBroadcastReceiver) {
    synchronized (this.mReceivers) {
      ArrayList<ReceiverRecord> arrayList = this.mReceivers.remove(paramBroadcastReceiver);
      if (arrayList == null)
        return; 
      for (int i = arrayList.size() - 1; i >= 0; i--) {
        ReceiverRecord receiverRecord = arrayList.get(i);
        receiverRecord.dead = true;
        for (byte b = 0; b < receiverRecord.filter.countActions(); b++) {
          String str = receiverRecord.filter.getAction(b);
          ArrayList<ReceiverRecord> arrayList1 = this.mActions.get(str);
          if (arrayList1 != null) {
            for (int j = arrayList1.size() - 1; j >= 0; j--) {
              ReceiverRecord receiverRecord1 = arrayList1.get(j);
              if (receiverRecord1.receiver == paramBroadcastReceiver) {
                receiverRecord1.dead = true;
                arrayList1.remove(j);
              } 
            } 
            if (arrayList1.size() <= 0)
              this.mActions.remove(str); 
          } 
        } 
      } 
      return;
    } 
  }
  
  private static final class BroadcastRecord {
    final Intent intent;
    
    final ArrayList<LocalBroadcastManager.ReceiverRecord> receivers;
    
    BroadcastRecord(Intent param1Intent, ArrayList<LocalBroadcastManager.ReceiverRecord> param1ArrayList) {
      this.intent = param1Intent;
      this.receivers = param1ArrayList;
    }
  }
  
  private static final class ReceiverRecord {
    boolean broadcasting;
    
    boolean dead;
    
    final IntentFilter filter;
    
    final BroadcastReceiver receiver;
    
    ReceiverRecord(IntentFilter param1IntentFilter, BroadcastReceiver param1BroadcastReceiver) {
      this.filter = param1IntentFilter;
      this.receiver = param1BroadcastReceiver;
    }
    
    public String toString() {
      StringBuilder stringBuilder = new StringBuilder(128);
      stringBuilder.append("Receiver{");
      stringBuilder.append(this.receiver);
      stringBuilder.append(" filter=");
      stringBuilder.append(this.filter);
      if (this.dead)
        stringBuilder.append(" DEAD"); 
      stringBuilder.append("}");
      return stringBuilder.toString();
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\localbroadcastmanager\content\LocalBroadcastManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */