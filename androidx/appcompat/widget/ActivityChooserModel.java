package androidx.appcompat.widget;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.DataSetObservable;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

class ActivityChooserModel extends DataSetObservable {
  static final String ATTRIBUTE_ACTIVITY = "activity";
  
  static final String ATTRIBUTE_TIME = "time";
  
  static final String ATTRIBUTE_WEIGHT = "weight";
  
  static final boolean DEBUG = false;
  
  private static final int DEFAULT_ACTIVITY_INFLATION = 5;
  
  private static final float DEFAULT_HISTORICAL_RECORD_WEIGHT = 1.0F;
  
  public static final String DEFAULT_HISTORY_FILE_NAME = "activity_choser_model_history.xml";
  
  public static final int DEFAULT_HISTORY_MAX_LENGTH = 50;
  
  private static final String HISTORY_FILE_EXTENSION = ".xml";
  
  private static final int INVALID_INDEX = -1;
  
  static final String LOG_TAG = ActivityChooserModel.class.getSimpleName();
  
  static final String TAG_HISTORICAL_RECORD = "historical-record";
  
  static final String TAG_HISTORICAL_RECORDS = "historical-records";
  
  private static final Map<String, ActivityChooserModel> sDataModelRegistry;
  
  private static final Object sRegistryLock = new Object();
  
  private final List<ActivityResolveInfo> mActivities = new ArrayList<>();
  
  private OnChooseActivityListener mActivityChoserModelPolicy;
  
  private ActivitySorter mActivitySorter = new DefaultSorter();
  
  boolean mCanReadHistoricalData = true;
  
  final Context mContext;
  
  private final List<HistoricalRecord> mHistoricalRecords = new ArrayList<>();
  
  private boolean mHistoricalRecordsChanged = true;
  
  final String mHistoryFileName;
  
  private int mHistoryMaxSize = 50;
  
  private final Object mInstanceLock = new Object();
  
  private Intent mIntent;
  
  private boolean mReadShareHistoryCalled = false;
  
  private boolean mReloadActivities = false;
  
  static {
    sDataModelRegistry = new HashMap<>();
  }
  
  private ActivityChooserModel(Context paramContext, String paramString) {
    this.mContext = paramContext.getApplicationContext();
    if (!TextUtils.isEmpty(paramString) && !paramString.endsWith(".xml")) {
      this.mHistoryFileName = paramString + ".xml";
    } else {
      this.mHistoryFileName = paramString;
    } 
  }
  
  private boolean addHistoricalRecord(HistoricalRecord paramHistoricalRecord) {
    boolean bool = this.mHistoricalRecords.add(paramHistoricalRecord);
    if (bool) {
      this.mHistoricalRecordsChanged = true;
      pruneExcessiveHistoricalRecordsIfNeeded();
      persistHistoricalDataIfNeeded();
      sortActivitiesIfNeeded();
      notifyChanged();
    } 
    return bool;
  }
  
  private void ensureConsistentState() {
    boolean bool1 = loadActivitiesIfNeeded();
    boolean bool2 = readHistoricalDataIfNeeded();
    pruneExcessiveHistoricalRecordsIfNeeded();
    if (bool1 | bool2) {
      sortActivitiesIfNeeded();
      notifyChanged();
    } 
  }
  
  public static ActivityChooserModel get(Context paramContext, String paramString) {
    synchronized (sRegistryLock) {
      Map<String, ActivityChooserModel> map = sDataModelRegistry;
      ActivityChooserModel activityChooserModel2 = map.get(paramString);
      ActivityChooserModel activityChooserModel1 = activityChooserModel2;
      if (activityChooserModel2 == null) {
        activityChooserModel1 = new ActivityChooserModel();
        this(paramContext, paramString);
        map.put(paramString, activityChooserModel1);
      } 
      return activityChooserModel1;
    } 
  }
  
  private boolean loadActivitiesIfNeeded() {
    if (this.mReloadActivities && this.mIntent != null) {
      this.mReloadActivities = false;
      this.mActivities.clear();
      List<ResolveInfo> list = this.mContext.getPackageManager().queryIntentActivities(this.mIntent, 0);
      int i = list.size();
      for (byte b = 0; b < i; b++) {
        ResolveInfo resolveInfo = list.get(b);
        this.mActivities.add(new ActivityResolveInfo(resolveInfo));
      } 
      return true;
    } 
    return false;
  }
  
  private void persistHistoricalDataIfNeeded() {
    if (this.mReadShareHistoryCalled) {
      if (!this.mHistoricalRecordsChanged)
        return; 
      this.mHistoricalRecordsChanged = false;
      if (!TextUtils.isEmpty(this.mHistoryFileName))
        (new PersistHistoryAsyncTask()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Object[] { new ArrayList<>(this.mHistoricalRecords), this.mHistoryFileName }); 
      return;
    } 
    throw new IllegalStateException("No preceding call to #readHistoricalData");
  }
  
  private void pruneExcessiveHistoricalRecordsIfNeeded() {
    int i = this.mHistoricalRecords.size() - this.mHistoryMaxSize;
    if (i <= 0)
      return; 
    this.mHistoricalRecordsChanged = true;
    for (byte b = 0; b < i; b++)
      HistoricalRecord historicalRecord = this.mHistoricalRecords.remove(0); 
  }
  
  private boolean readHistoricalDataIfNeeded() {
    if (this.mCanReadHistoricalData && this.mHistoricalRecordsChanged && !TextUtils.isEmpty(this.mHistoryFileName)) {
      this.mCanReadHistoricalData = false;
      this.mReadShareHistoryCalled = true;
      readHistoricalDataImpl();
      return true;
    } 
    return false;
  }
  
  private void readHistoricalDataImpl() {
    try {
      FileInputStream fileInputStream = this.mContext.openFileInput(this.mHistoryFileName);
      try {
        XmlPullParser xmlPullParser = Xml.newPullParser();
        xmlPullParser.setInput(fileInputStream, "UTF-8");
        int i;
        for (i = 0; i != 1 && i != 2; i = xmlPullParser.next());
        if ("historical-records".equals(xmlPullParser.getName())) {
          List<HistoricalRecord> list = this.mHistoricalRecords;
          list.clear();
          while (true) {
            i = xmlPullParser.next();
            if (i == 1) {
              if (fileInputStream != null) {
                try {
                  fileInputStream.close();
                  break;
                } catch (IOException iOException) {}
                return;
              } 
              break;
            } 
            if (i == 3 || i == 4)
              continue; 
            if ("historical-record".equals(xmlPullParser.getName())) {
              String str = xmlPullParser.getAttributeValue(null, "activity");
              long l = Long.parseLong(xmlPullParser.getAttributeValue(null, "time"));
              float f = Float.parseFloat(xmlPullParser.getAttributeValue(null, "weight"));
              HistoricalRecord historicalRecord = new HistoricalRecord();
              this(str, l, f);
              list.add(historicalRecord);
              continue;
            } 
            XmlPullParserException xmlPullParserException = new XmlPullParserException();
            this("Share records file not well-formed.");
            throw xmlPullParserException;
          } 
        } else {
          XmlPullParserException xmlPullParserException = new XmlPullParserException();
          this("Share records file does not start with historical-records tag.");
          throw xmlPullParserException;
        } 
      } catch (XmlPullParserException xmlPullParserException) {
        String str = LOG_TAG;
        StringBuilder stringBuilder = new StringBuilder();
        this();
        Log.e(str, stringBuilder.append("Error reading historical recrod file: ").append(this.mHistoryFileName).toString(), (Throwable)xmlPullParserException);
        if (iOException != null)
          iOException.close(); 
      } catch (IOException iOException1) {
        String str = LOG_TAG;
        StringBuilder stringBuilder = new StringBuilder();
        this();
        Log.e(str, stringBuilder.append("Error reading historical recrod file: ").append(this.mHistoryFileName).toString(), iOException1);
        if (iOException != null)
          iOException.close(); 
      } finally {
        Exception exception;
      } 
      return;
    } catch (FileNotFoundException fileNotFoundException) {
      return;
    } 
  }
  
  private boolean sortActivitiesIfNeeded() {
    if (this.mActivitySorter != null && this.mIntent != null && !this.mActivities.isEmpty() && !this.mHistoricalRecords.isEmpty()) {
      this.mActivitySorter.sort(this.mIntent, this.mActivities, Collections.unmodifiableList(this.mHistoricalRecords));
      return true;
    } 
    return false;
  }
  
  public Intent chooseActivity(int paramInt) {
    synchronized (this.mInstanceLock) {
      if (this.mIntent == null)
        return null; 
      ensureConsistentState();
      ActivityResolveInfo activityResolveInfo = this.mActivities.get(paramInt);
      ComponentName componentName = new ComponentName();
      this(activityResolveInfo.resolveInfo.activityInfo.packageName, activityResolveInfo.resolveInfo.activityInfo.name);
      Intent intent = new Intent();
      this(this.mIntent);
      intent.setComponent(componentName);
      if (this.mActivityChoserModelPolicy != null) {
        Intent intent1 = new Intent();
        this(intent);
        if (this.mActivityChoserModelPolicy.onChooseActivity(this, intent1))
          return null; 
      } 
      HistoricalRecord historicalRecord = new HistoricalRecord();
      this(componentName, System.currentTimeMillis(), 1.0F);
      addHistoricalRecord(historicalRecord);
      return intent;
    } 
  }
  
  public ResolveInfo getActivity(int paramInt) {
    synchronized (this.mInstanceLock) {
      ensureConsistentState();
      return ((ActivityResolveInfo)this.mActivities.get(paramInt)).resolveInfo;
    } 
  }
  
  public int getActivityCount() {
    synchronized (this.mInstanceLock) {
      ensureConsistentState();
      return this.mActivities.size();
    } 
  }
  
  public int getActivityIndex(ResolveInfo paramResolveInfo) {
    synchronized (this.mInstanceLock) {
      ensureConsistentState();
      List<ActivityResolveInfo> list = this.mActivities;
      int i = list.size();
      for (byte b = 0; b < i; b++) {
        if (((ActivityResolveInfo)list.get(b)).resolveInfo == paramResolveInfo)
          return b; 
      } 
      return -1;
    } 
  }
  
  public ResolveInfo getDefaultActivity() {
    synchronized (this.mInstanceLock) {
      ensureConsistentState();
      if (!this.mActivities.isEmpty())
        return ((ActivityResolveInfo)this.mActivities.get(0)).resolveInfo; 
      return null;
    } 
  }
  
  public int getHistoryMaxSize() {
    synchronized (this.mInstanceLock) {
      return this.mHistoryMaxSize;
    } 
  }
  
  public int getHistorySize() {
    synchronized (this.mInstanceLock) {
      ensureConsistentState();
      return this.mHistoricalRecords.size();
    } 
  }
  
  public Intent getIntent() {
    synchronized (this.mInstanceLock) {
      return this.mIntent;
    } 
  }
  
  public void setActivitySorter(ActivitySorter paramActivitySorter) {
    synchronized (this.mInstanceLock) {
      if (this.mActivitySorter == paramActivitySorter)
        return; 
      this.mActivitySorter = paramActivitySorter;
      if (sortActivitiesIfNeeded())
        notifyChanged(); 
      return;
    } 
  }
  
  public void setDefaultActivity(int paramInt) {
    synchronized (this.mInstanceLock) {
      float f;
      ensureConsistentState();
      ActivityResolveInfo activityResolveInfo2 = this.mActivities.get(paramInt);
      ActivityResolveInfo activityResolveInfo1 = this.mActivities.get(0);
      if (activityResolveInfo1 != null) {
        f = activityResolveInfo1.weight - activityResolveInfo2.weight + 5.0F;
      } else {
        f = 1.0F;
      } 
      ComponentName componentName = new ComponentName();
      this(activityResolveInfo2.resolveInfo.activityInfo.packageName, activityResolveInfo2.resolveInfo.activityInfo.name);
      HistoricalRecord historicalRecord = new HistoricalRecord();
      this(componentName, System.currentTimeMillis(), f);
      addHistoricalRecord(historicalRecord);
      return;
    } 
  }
  
  public void setHistoryMaxSize(int paramInt) {
    synchronized (this.mInstanceLock) {
      if (this.mHistoryMaxSize == paramInt)
        return; 
      this.mHistoryMaxSize = paramInt;
      pruneExcessiveHistoricalRecordsIfNeeded();
      if (sortActivitiesIfNeeded())
        notifyChanged(); 
      return;
    } 
  }
  
  public void setIntent(Intent paramIntent) {
    synchronized (this.mInstanceLock) {
      if (this.mIntent == paramIntent)
        return; 
      this.mIntent = paramIntent;
      this.mReloadActivities = true;
      ensureConsistentState();
      return;
    } 
  }
  
  public void setOnChooseActivityListener(OnChooseActivityListener paramOnChooseActivityListener) {
    synchronized (this.mInstanceLock) {
      this.mActivityChoserModelPolicy = paramOnChooseActivityListener;
      return;
    } 
  }
  
  public static interface ActivityChooserModelClient {
    void setActivityChooserModel(ActivityChooserModel param1ActivityChooserModel);
  }
  
  public static final class ActivityResolveInfo implements Comparable<ActivityResolveInfo> {
    public final ResolveInfo resolveInfo;
    
    public float weight;
    
    public ActivityResolveInfo(ResolveInfo param1ResolveInfo) {
      this.resolveInfo = param1ResolveInfo;
    }
    
    public int compareTo(ActivityResolveInfo param1ActivityResolveInfo) {
      return Float.floatToIntBits(param1ActivityResolveInfo.weight) - Float.floatToIntBits(this.weight);
    }
    
    public boolean equals(Object param1Object) {
      if (this == param1Object)
        return true; 
      if (param1Object == null)
        return false; 
      if (getClass() != param1Object.getClass())
        return false; 
      param1Object = param1Object;
      return !(Float.floatToIntBits(this.weight) != Float.floatToIntBits(((ActivityResolveInfo)param1Object).weight));
    }
    
    public int hashCode() {
      return Float.floatToIntBits(this.weight) + 31;
    }
    
    public String toString() {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("[");
      stringBuilder.append("resolveInfo:").append(this.resolveInfo.toString());
      stringBuilder.append("; weight:").append(new BigDecimal(this.weight));
      stringBuilder.append("]");
      return stringBuilder.toString();
    }
  }
  
  public static interface ActivitySorter {
    void sort(Intent param1Intent, List<ActivityChooserModel.ActivityResolveInfo> param1List, List<ActivityChooserModel.HistoricalRecord> param1List1);
  }
  
  private static final class DefaultSorter implements ActivitySorter {
    private static final float WEIGHT_DECAY_COEFFICIENT = 0.95F;
    
    private final Map<ComponentName, ActivityChooserModel.ActivityResolveInfo> mPackageNameToActivityMap = new HashMap<>();
    
    public void sort(Intent param1Intent, List<ActivityChooserModel.ActivityResolveInfo> param1List, List<ActivityChooserModel.HistoricalRecord> param1List1) {
      Map<ComponentName, ActivityChooserModel.ActivityResolveInfo> map = this.mPackageNameToActivityMap;
      map.clear();
      int j = param1List.size();
      int i;
      for (i = 0; i < j; i++) {
        ActivityChooserModel.ActivityResolveInfo activityResolveInfo = param1List.get(i);
        activityResolveInfo.weight = 0.0F;
        map.put(new ComponentName(activityResolveInfo.resolveInfo.activityInfo.packageName, activityResolveInfo.resolveInfo.activityInfo.name), activityResolveInfo);
      } 
      i = param1List1.size();
      float f;
      for (f = 1.0F; --i >= 0; f = f1) {
        ActivityChooserModel.HistoricalRecord historicalRecord = param1List1.get(i);
        ActivityChooserModel.ActivityResolveInfo activityResolveInfo = map.get(historicalRecord.activity);
        float f1 = f;
        if (activityResolveInfo != null) {
          activityResolveInfo.weight += historicalRecord.weight * f;
          f1 = f * 0.95F;
        } 
        i--;
      } 
      Collections.sort(param1List);
    }
  }
  
  public static final class HistoricalRecord {
    public final ComponentName activity;
    
    public final long time;
    
    public final float weight;
    
    public HistoricalRecord(ComponentName param1ComponentName, long param1Long, float param1Float) {
      this.activity = param1ComponentName;
      this.time = param1Long;
      this.weight = param1Float;
    }
    
    public HistoricalRecord(String param1String, long param1Long, float param1Float) {
      this(ComponentName.unflattenFromString(param1String), param1Long, param1Float);
    }
    
    public boolean equals(Object param1Object) {
      if (this == param1Object)
        return true; 
      if (param1Object == null)
        return false; 
      if (getClass() != param1Object.getClass())
        return false; 
      param1Object = param1Object;
      ComponentName componentName = this.activity;
      if (componentName == null) {
        if (((HistoricalRecord)param1Object).activity != null)
          return false; 
      } else if (!componentName.equals(((HistoricalRecord)param1Object).activity)) {
        return false;
      } 
      return (this.time != ((HistoricalRecord)param1Object).time) ? false : (!(Float.floatToIntBits(this.weight) != Float.floatToIntBits(((HistoricalRecord)param1Object).weight)));
    }
    
    public int hashCode() {
      int i;
      ComponentName componentName = this.activity;
      if (componentName == null) {
        i = 0;
      } else {
        i = componentName.hashCode();
      } 
      long l = this.time;
      return ((1 * 31 + i) * 31 + (int)(l ^ l >>> 32L)) * 31 + Float.floatToIntBits(this.weight);
    }
    
    public String toString() {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("[");
      stringBuilder.append("; activity:").append(this.activity);
      stringBuilder.append("; time:").append(this.time);
      stringBuilder.append("; weight:").append(new BigDecimal(this.weight));
      stringBuilder.append("]");
      return stringBuilder.toString();
    }
  }
  
  public static interface OnChooseActivityListener {
    boolean onChooseActivity(ActivityChooserModel param1ActivityChooserModel, Intent param1Intent);
  }
  
  private final class PersistHistoryAsyncTask extends AsyncTask<Object, Void, Void> {
    final ActivityChooserModel this$0;
    
    public Void doInBackground(Object... param1VarArgs) {
      // Byte code:
      //   0: aload_1
      //   1: iconst_0
      //   2: aaload
      //   3: checkcast java/util/List
      //   6: astore #4
      //   8: aload_1
      //   9: iconst_1
      //   10: aaload
      //   11: checkcast java/lang/String
      //   14: astore_1
      //   15: aload_0
      //   16: getfield this$0 : Landroidx/appcompat/widget/ActivityChooserModel;
      //   19: getfield mContext : Landroid/content/Context;
      //   22: aload_1
      //   23: iconst_0
      //   24: invokevirtual openFileOutput : (Ljava/lang/String;I)Ljava/io/FileOutputStream;
      //   27: astore #6
      //   29: invokestatic newSerializer : ()Lorg/xmlpull/v1/XmlSerializer;
      //   32: astore #7
      //   34: aload #4
      //   36: astore #5
      //   38: aload #4
      //   40: astore #5
      //   42: aload #4
      //   44: astore #5
      //   46: aload #4
      //   48: astore #5
      //   50: aload #7
      //   52: aload #6
      //   54: aconst_null
      //   55: invokeinterface setOutput : (Ljava/io/OutputStream;Ljava/lang/String;)V
      //   60: aload #4
      //   62: astore #5
      //   64: aload #4
      //   66: astore #5
      //   68: aload #4
      //   70: astore #5
      //   72: aload #4
      //   74: astore #5
      //   76: aload #7
      //   78: ldc 'UTF-8'
      //   80: iconst_1
      //   81: invokestatic valueOf : (Z)Ljava/lang/Boolean;
      //   84: invokeinterface startDocument : (Ljava/lang/String;Ljava/lang/Boolean;)V
      //   89: aload #4
      //   91: astore #5
      //   93: aload #4
      //   95: astore #5
      //   97: aload #4
      //   99: astore #5
      //   101: aload #4
      //   103: astore #5
      //   105: aload #7
      //   107: aconst_null
      //   108: ldc 'historical-records'
      //   110: invokeinterface startTag : (Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;
      //   115: pop
      //   116: aload #4
      //   118: astore #5
      //   120: aload #4
      //   122: astore #5
      //   124: aload #4
      //   126: astore #5
      //   128: aload #4
      //   130: astore #5
      //   132: aload #4
      //   134: invokeinterface size : ()I
      //   139: istore_3
      //   140: iconst_0
      //   141: istore_2
      //   142: aload #4
      //   144: astore_1
      //   145: iload_2
      //   146: iload_3
      //   147: if_icmpge -> 283
      //   150: aload_1
      //   151: astore #5
      //   153: aload_1
      //   154: astore #5
      //   156: aload_1
      //   157: astore #5
      //   159: aload_1
      //   160: astore #5
      //   162: aload_1
      //   163: iconst_0
      //   164: invokeinterface remove : (I)Ljava/lang/Object;
      //   169: checkcast androidx/appcompat/widget/ActivityChooserModel$HistoricalRecord
      //   172: astore #4
      //   174: aload_1
      //   175: astore #5
      //   177: aload_1
      //   178: astore #5
      //   180: aload_1
      //   181: astore #5
      //   183: aload_1
      //   184: astore #5
      //   186: aload #7
      //   188: aconst_null
      //   189: ldc 'historical-record'
      //   191: invokeinterface startTag : (Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;
      //   196: pop
      //   197: aload_1
      //   198: astore #5
      //   200: aload_1
      //   201: astore #5
      //   203: aload_1
      //   204: astore #5
      //   206: aload_1
      //   207: astore #5
      //   209: aload #7
      //   211: aconst_null
      //   212: ldc 'activity'
      //   214: aload #4
      //   216: getfield activity : Landroid/content/ComponentName;
      //   219: invokevirtual flattenToString : ()Ljava/lang/String;
      //   222: invokeinterface attribute : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;
      //   227: pop
      //   228: aload #7
      //   230: aconst_null
      //   231: ldc 'time'
      //   233: aload #4
      //   235: getfield time : J
      //   238: invokestatic valueOf : (J)Ljava/lang/String;
      //   241: invokeinterface attribute : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;
      //   246: pop
      //   247: aload #7
      //   249: aconst_null
      //   250: ldc 'weight'
      //   252: aload #4
      //   254: getfield weight : F
      //   257: invokestatic valueOf : (F)Ljava/lang/String;
      //   260: invokeinterface attribute : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;
      //   265: pop
      //   266: aload #7
      //   268: aconst_null
      //   269: ldc 'historical-record'
      //   271: invokeinterface endTag : (Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;
      //   276: pop
      //   277: iinc #2, 1
      //   280: goto -> 145
      //   283: aload #7
      //   285: aconst_null
      //   286: ldc 'historical-records'
      //   288: invokeinterface endTag : (Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;
      //   293: pop
      //   294: aload #7
      //   296: invokeinterface endDocument : ()V
      //   301: aload_0
      //   302: getfield this$0 : Landroidx/appcompat/widget/ActivityChooserModel;
      //   305: iconst_1
      //   306: putfield mCanReadHistoricalData : Z
      //   309: aload #6
      //   311: ifnull -> 534
      //   314: aload #6
      //   316: invokevirtual close : ()V
      //   319: goto -> 527
      //   322: astore_1
      //   323: goto -> 339
      //   326: astore_1
      //   327: goto -> 403
      //   330: astore_1
      //   331: goto -> 467
      //   334: astore_1
      //   335: goto -> 537
      //   338: astore_1
      //   339: getstatic androidx/appcompat/widget/ActivityChooserModel.LOG_TAG : Ljava/lang/String;
      //   342: astore #4
      //   344: new java/lang/StringBuilder
      //   347: astore #5
      //   349: aload #5
      //   351: invokespecial <init> : ()V
      //   354: aload #4
      //   356: aload #5
      //   358: ldc 'Error writing historical record file: '
      //   360: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   363: aload_0
      //   364: getfield this$0 : Landroidx/appcompat/widget/ActivityChooserModel;
      //   367: getfield mHistoryFileName : Ljava/lang/String;
      //   370: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   373: invokevirtual toString : ()Ljava/lang/String;
      //   376: aload_1
      //   377: invokestatic e : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   380: pop
      //   381: aload_0
      //   382: getfield this$0 : Landroidx/appcompat/widget/ActivityChooserModel;
      //   385: iconst_1
      //   386: putfield mCanReadHistoricalData : Z
      //   389: aload #6
      //   391: ifnull -> 534
      //   394: aload #6
      //   396: invokevirtual close : ()V
      //   399: goto -> 527
      //   402: astore_1
      //   403: getstatic androidx/appcompat/widget/ActivityChooserModel.LOG_TAG : Ljava/lang/String;
      //   406: astore #4
      //   408: new java/lang/StringBuilder
      //   411: astore #5
      //   413: aload #5
      //   415: invokespecial <init> : ()V
      //   418: aload #4
      //   420: aload #5
      //   422: ldc 'Error writing historical record file: '
      //   424: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   427: aload_0
      //   428: getfield this$0 : Landroidx/appcompat/widget/ActivityChooserModel;
      //   431: getfield mHistoryFileName : Ljava/lang/String;
      //   434: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   437: invokevirtual toString : ()Ljava/lang/String;
      //   440: aload_1
      //   441: invokestatic e : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   444: pop
      //   445: aload_0
      //   446: getfield this$0 : Landroidx/appcompat/widget/ActivityChooserModel;
      //   449: iconst_1
      //   450: putfield mCanReadHistoricalData : Z
      //   453: aload #6
      //   455: ifnull -> 534
      //   458: aload #6
      //   460: invokevirtual close : ()V
      //   463: goto -> 527
      //   466: astore_1
      //   467: getstatic androidx/appcompat/widget/ActivityChooserModel.LOG_TAG : Ljava/lang/String;
      //   470: astore #4
      //   472: new java/lang/StringBuilder
      //   475: astore #5
      //   477: aload #5
      //   479: invokespecial <init> : ()V
      //   482: aload #4
      //   484: aload #5
      //   486: ldc 'Error writing historical record file: '
      //   488: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   491: aload_0
      //   492: getfield this$0 : Landroidx/appcompat/widget/ActivityChooserModel;
      //   495: getfield mHistoryFileName : Ljava/lang/String;
      //   498: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   501: invokevirtual toString : ()Ljava/lang/String;
      //   504: aload_1
      //   505: invokestatic e : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   508: pop
      //   509: aload_0
      //   510: getfield this$0 : Landroidx/appcompat/widget/ActivityChooserModel;
      //   513: iconst_1
      //   514: putfield mCanReadHistoricalData : Z
      //   517: aload #6
      //   519: ifnull -> 534
      //   522: aload #6
      //   524: invokevirtual close : ()V
      //   527: goto -> 534
      //   530: astore_1
      //   531: goto -> 527
      //   534: aconst_null
      //   535: areturn
      //   536: astore_1
      //   537: aload_0
      //   538: getfield this$0 : Landroidx/appcompat/widget/ActivityChooserModel;
      //   541: iconst_1
      //   542: putfield mCanReadHistoricalData : Z
      //   545: aload #6
      //   547: ifnull -> 560
      //   550: aload #6
      //   552: invokevirtual close : ()V
      //   555: goto -> 560
      //   558: astore #4
      //   560: aload_1
      //   561: athrow
      //   562: astore #4
      //   564: getstatic androidx/appcompat/widget/ActivityChooserModel.LOG_TAG : Ljava/lang/String;
      //   567: new java/lang/StringBuilder
      //   570: dup
      //   571: invokespecial <init> : ()V
      //   574: ldc 'Error writing historical record file: '
      //   576: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   579: aload_1
      //   580: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   583: invokevirtual toString : ()Ljava/lang/String;
      //   586: aload #4
      //   588: invokestatic e : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   591: pop
      //   592: aconst_null
      //   593: areturn
      // Exception table:
      //   from	to	target	type
      //   15	29	562	java/io/FileNotFoundException
      //   50	60	466	java/lang/IllegalArgumentException
      //   50	60	402	java/lang/IllegalStateException
      //   50	60	338	java/io/IOException
      //   50	60	334	finally
      //   76	89	466	java/lang/IllegalArgumentException
      //   76	89	402	java/lang/IllegalStateException
      //   76	89	338	java/io/IOException
      //   76	89	334	finally
      //   105	116	466	java/lang/IllegalArgumentException
      //   105	116	402	java/lang/IllegalStateException
      //   105	116	338	java/io/IOException
      //   105	116	334	finally
      //   132	140	466	java/lang/IllegalArgumentException
      //   132	140	402	java/lang/IllegalStateException
      //   132	140	338	java/io/IOException
      //   132	140	334	finally
      //   162	174	466	java/lang/IllegalArgumentException
      //   162	174	402	java/lang/IllegalStateException
      //   162	174	338	java/io/IOException
      //   162	174	334	finally
      //   186	197	466	java/lang/IllegalArgumentException
      //   186	197	402	java/lang/IllegalStateException
      //   186	197	338	java/io/IOException
      //   186	197	334	finally
      //   209	228	466	java/lang/IllegalArgumentException
      //   209	228	402	java/lang/IllegalStateException
      //   209	228	338	java/io/IOException
      //   209	228	334	finally
      //   228	277	330	java/lang/IllegalArgumentException
      //   228	277	326	java/lang/IllegalStateException
      //   228	277	322	java/io/IOException
      //   228	277	536	finally
      //   283	301	330	java/lang/IllegalArgumentException
      //   283	301	326	java/lang/IllegalStateException
      //   283	301	322	java/io/IOException
      //   283	301	536	finally
      //   314	319	530	java/io/IOException
      //   339	381	536	finally
      //   394	399	530	java/io/IOException
      //   403	445	536	finally
      //   458	463	530	java/io/IOException
      //   467	509	536	finally
      //   522	527	530	java/io/IOException
      //   550	555	558	java/io/IOException
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\appcompat\widget\ActivityChooserModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */