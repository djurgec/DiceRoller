package androidx.core.app;

import android.app.Activity;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.ActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ShareActionProvider;
import androidx.core.util.Preconditions;
import java.util.ArrayList;

public final class ShareCompat {
  public static final String EXTRA_CALLING_ACTIVITY = "androidx.core.app.EXTRA_CALLING_ACTIVITY";
  
  public static final String EXTRA_CALLING_ACTIVITY_INTEROP = "android.support.v4.app.EXTRA_CALLING_ACTIVITY";
  
  public static final String EXTRA_CALLING_PACKAGE = "androidx.core.app.EXTRA_CALLING_PACKAGE";
  
  public static final String EXTRA_CALLING_PACKAGE_INTEROP = "android.support.v4.app.EXTRA_CALLING_PACKAGE";
  
  private static final String HISTORY_FILENAME_PREFIX = ".sharecompat_";
  
  @Deprecated
  public static void configureMenuItem(Menu paramMenu, int paramInt, IntentBuilder paramIntentBuilder) {
    MenuItem menuItem = paramMenu.findItem(paramInt);
    if (menuItem != null) {
      configureMenuItem(menuItem, paramIntentBuilder);
      return;
    } 
    throw new IllegalArgumentException("Could not find menu item with id " + paramInt + " in the supplied menu");
  }
  
  @Deprecated
  public static void configureMenuItem(MenuItem paramMenuItem, IntentBuilder paramIntentBuilder) {
    ShareActionProvider shareActionProvider;
    ActionProvider actionProvider = paramMenuItem.getActionProvider();
    if (!(actionProvider instanceof ShareActionProvider)) {
      shareActionProvider = new ShareActionProvider(paramIntentBuilder.getContext());
    } else {
      shareActionProvider = shareActionProvider;
    } 
    shareActionProvider.setShareHistoryFileName(".sharecompat_" + paramIntentBuilder.getContext().getClass().getName());
    shareActionProvider.setShareIntent(paramIntentBuilder.getIntent());
    paramMenuItem.setActionProvider((ActionProvider)shareActionProvider);
    if (Build.VERSION.SDK_INT < 16 && !paramMenuItem.hasSubMenu())
      paramMenuItem.setIntent(paramIntentBuilder.createChooserIntent()); 
  }
  
  public static ComponentName getCallingActivity(Activity paramActivity) {
    Intent intent = paramActivity.getIntent();
    ComponentName componentName2 = paramActivity.getCallingActivity();
    ComponentName componentName1 = componentName2;
    if (componentName2 == null)
      componentName1 = getCallingActivity(intent); 
    return componentName1;
  }
  
  static ComponentName getCallingActivity(Intent paramIntent) {
    ComponentName componentName2 = (ComponentName)paramIntent.getParcelableExtra("androidx.core.app.EXTRA_CALLING_ACTIVITY");
    ComponentName componentName1 = componentName2;
    if (componentName2 == null)
      componentName1 = (ComponentName)paramIntent.getParcelableExtra("android.support.v4.app.EXTRA_CALLING_ACTIVITY"); 
    return componentName1;
  }
  
  public static String getCallingPackage(Activity paramActivity) {
    Intent intent = paramActivity.getIntent();
    String str2 = paramActivity.getCallingPackage();
    String str1 = str2;
    if (str2 == null) {
      str1 = str2;
      if (intent != null)
        str1 = getCallingPackage(intent); 
    } 
    return str1;
  }
  
  static String getCallingPackage(Intent paramIntent) {
    String str2 = paramIntent.getStringExtra("androidx.core.app.EXTRA_CALLING_PACKAGE");
    String str1 = str2;
    if (str2 == null)
      str1 = paramIntent.getStringExtra("android.support.v4.app.EXTRA_CALLING_PACKAGE"); 
    return str1;
  }
  
  private static class Api16Impl {
    static void migrateExtraStreamToClipData(Intent param1Intent, ArrayList<Uri> param1ArrayList) {
      CharSequence charSequence = param1Intent.getCharSequenceExtra("android.intent.extra.TEXT");
      String str2 = param1Intent.getStringExtra("android.intent.extra.HTML_TEXT");
      String str1 = param1Intent.getType();
      ClipData.Item item = new ClipData.Item(charSequence, str2, null, param1ArrayList.get(0));
      ClipData clipData = new ClipData(null, new String[] { str1 }, item);
      byte b = 1;
      int i = param1ArrayList.size();
      while (b < i) {
        clipData.addItem(new ClipData.Item(param1ArrayList.get(b)));
        b++;
      } 
      param1Intent.setClipData(clipData);
      param1Intent.addFlags(1);
    }
    
    static void removeClipData(Intent param1Intent) {
      param1Intent.setClipData(null);
      param1Intent.setFlags(param1Intent.getFlags() & 0xFFFFFFFE);
    }
  }
  
  public static class IntentBuilder {
    private ArrayList<String> mBccAddresses;
    
    private ArrayList<String> mCcAddresses;
    
    private CharSequence mChooserTitle;
    
    private final Context mContext;
    
    private final Intent mIntent;
    
    private ArrayList<Uri> mStreams;
    
    private ArrayList<String> mToAddresses;
    
    public IntentBuilder(Context param1Context) {
      Activity activity;
      this.mContext = (Context)Preconditions.checkNotNull(param1Context);
      Intent intent1 = (new Intent()).setAction("android.intent.action.SEND");
      this.mIntent = intent1;
      intent1.putExtra("androidx.core.app.EXTRA_CALLING_PACKAGE", param1Context.getPackageName());
      intent1.putExtra("android.support.v4.app.EXTRA_CALLING_PACKAGE", param1Context.getPackageName());
      intent1.addFlags(524288);
      Intent intent2 = null;
      while (true) {
        intent1 = intent2;
        if (param1Context instanceof ContextWrapper) {
          if (param1Context instanceof Activity) {
            activity = (Activity)param1Context;
            break;
          } 
          param1Context = ((ContextWrapper)param1Context).getBaseContext();
          continue;
        } 
        break;
      } 
      if (activity != null) {
        ComponentName componentName = activity.getComponentName();
        this.mIntent.putExtra("androidx.core.app.EXTRA_CALLING_ACTIVITY", (Parcelable)componentName);
        this.mIntent.putExtra("android.support.v4.app.EXTRA_CALLING_ACTIVITY", (Parcelable)componentName);
      } 
    }
    
    private void combineArrayExtra(String param1String, ArrayList<String> param1ArrayList) {
      byte b;
      String[] arrayOfString2 = this.mIntent.getStringArrayExtra(param1String);
      if (arrayOfString2 != null) {
        b = arrayOfString2.length;
      } else {
        b = 0;
      } 
      String[] arrayOfString1 = new String[param1ArrayList.size() + b];
      param1ArrayList.toArray(arrayOfString1);
      if (arrayOfString2 != null)
        System.arraycopy(arrayOfString2, 0, arrayOfString1, param1ArrayList.size(), b); 
      this.mIntent.putExtra(param1String, arrayOfString1);
    }
    
    private void combineArrayExtra(String param1String, String[] param1ArrayOfString) {
      byte b;
      Intent intent = getIntent();
      String[] arrayOfString2 = intent.getStringArrayExtra(param1String);
      if (arrayOfString2 != null) {
        b = arrayOfString2.length;
      } else {
        b = 0;
      } 
      String[] arrayOfString1 = new String[param1ArrayOfString.length + b];
      if (arrayOfString2 != null)
        System.arraycopy(arrayOfString2, 0, arrayOfString1, 0, b); 
      System.arraycopy(param1ArrayOfString, 0, arrayOfString1, b, param1ArrayOfString.length);
      intent.putExtra(param1String, arrayOfString1);
    }
    
    @Deprecated
    public static IntentBuilder from(Activity param1Activity) {
      return new IntentBuilder((Context)param1Activity);
    }
    
    public IntentBuilder addEmailBcc(String param1String) {
      if (this.mBccAddresses == null)
        this.mBccAddresses = new ArrayList<>(); 
      this.mBccAddresses.add(param1String);
      return this;
    }
    
    public IntentBuilder addEmailBcc(String[] param1ArrayOfString) {
      combineArrayExtra("android.intent.extra.BCC", param1ArrayOfString);
      return this;
    }
    
    public IntentBuilder addEmailCc(String param1String) {
      if (this.mCcAddresses == null)
        this.mCcAddresses = new ArrayList<>(); 
      this.mCcAddresses.add(param1String);
      return this;
    }
    
    public IntentBuilder addEmailCc(String[] param1ArrayOfString) {
      combineArrayExtra("android.intent.extra.CC", param1ArrayOfString);
      return this;
    }
    
    public IntentBuilder addEmailTo(String param1String) {
      if (this.mToAddresses == null)
        this.mToAddresses = new ArrayList<>(); 
      this.mToAddresses.add(param1String);
      return this;
    }
    
    public IntentBuilder addEmailTo(String[] param1ArrayOfString) {
      combineArrayExtra("android.intent.extra.EMAIL", param1ArrayOfString);
      return this;
    }
    
    public IntentBuilder addStream(Uri param1Uri) {
      if (this.mStreams == null)
        this.mStreams = new ArrayList<>(); 
      this.mStreams.add(param1Uri);
      return this;
    }
    
    public Intent createChooserIntent() {
      return Intent.createChooser(getIntent(), this.mChooserTitle);
    }
    
    Context getContext() {
      return this.mContext;
    }
    
    public Intent getIntent() {
      ArrayList<String> arrayList1 = this.mToAddresses;
      if (arrayList1 != null) {
        combineArrayExtra("android.intent.extra.EMAIL", arrayList1);
        this.mToAddresses = null;
      } 
      arrayList1 = this.mCcAddresses;
      if (arrayList1 != null) {
        combineArrayExtra("android.intent.extra.CC", arrayList1);
        this.mCcAddresses = null;
      } 
      arrayList1 = this.mBccAddresses;
      if (arrayList1 != null) {
        combineArrayExtra("android.intent.extra.BCC", arrayList1);
        this.mBccAddresses = null;
      } 
      ArrayList<Uri> arrayList = this.mStreams;
      boolean bool = true;
      if (arrayList == null || arrayList.size() <= 1)
        bool = false; 
      if (!bool) {
        this.mIntent.setAction("android.intent.action.SEND");
        arrayList = this.mStreams;
        if (arrayList != null && !arrayList.isEmpty()) {
          this.mIntent.putExtra("android.intent.extra.STREAM", (Parcelable)this.mStreams.get(0));
          if (Build.VERSION.SDK_INT >= 16)
            ShareCompat.Api16Impl.migrateExtraStreamToClipData(this.mIntent, this.mStreams); 
        } else {
          this.mIntent.removeExtra("android.intent.extra.STREAM");
          if (Build.VERSION.SDK_INT >= 16)
            ShareCompat.Api16Impl.removeClipData(this.mIntent); 
        } 
      } else {
        this.mIntent.setAction("android.intent.action.SEND_MULTIPLE");
        this.mIntent.putParcelableArrayListExtra("android.intent.extra.STREAM", this.mStreams);
        if (Build.VERSION.SDK_INT >= 16)
          ShareCompat.Api16Impl.migrateExtraStreamToClipData(this.mIntent, this.mStreams); 
      } 
      return this.mIntent;
    }
    
    public IntentBuilder setChooserTitle(int param1Int) {
      return setChooserTitle(this.mContext.getText(param1Int));
    }
    
    public IntentBuilder setChooserTitle(CharSequence param1CharSequence) {
      this.mChooserTitle = param1CharSequence;
      return this;
    }
    
    public IntentBuilder setEmailBcc(String[] param1ArrayOfString) {
      this.mIntent.putExtra("android.intent.extra.BCC", param1ArrayOfString);
      return this;
    }
    
    public IntentBuilder setEmailCc(String[] param1ArrayOfString) {
      this.mIntent.putExtra("android.intent.extra.CC", param1ArrayOfString);
      return this;
    }
    
    public IntentBuilder setEmailTo(String[] param1ArrayOfString) {
      if (this.mToAddresses != null)
        this.mToAddresses = null; 
      this.mIntent.putExtra("android.intent.extra.EMAIL", param1ArrayOfString);
      return this;
    }
    
    public IntentBuilder setHtmlText(String param1String) {
      this.mIntent.putExtra("android.intent.extra.HTML_TEXT", param1String);
      if (!this.mIntent.hasExtra("android.intent.extra.TEXT"))
        setText((CharSequence)Html.fromHtml(param1String)); 
      return this;
    }
    
    public IntentBuilder setStream(Uri param1Uri) {
      this.mStreams = null;
      if (param1Uri != null)
        addStream(param1Uri); 
      return this;
    }
    
    public IntentBuilder setSubject(String param1String) {
      this.mIntent.putExtra("android.intent.extra.SUBJECT", param1String);
      return this;
    }
    
    public IntentBuilder setText(CharSequence param1CharSequence) {
      this.mIntent.putExtra("android.intent.extra.TEXT", param1CharSequence);
      return this;
    }
    
    public IntentBuilder setType(String param1String) {
      this.mIntent.setType(param1String);
      return this;
    }
    
    public void startChooser() {
      this.mContext.startActivity(createChooserIntent());
    }
  }
  
  public static class IntentReader {
    private static final String TAG = "IntentReader";
    
    private final ComponentName mCallingActivity;
    
    private final String mCallingPackage;
    
    private final Context mContext;
    
    private final Intent mIntent;
    
    private ArrayList<Uri> mStreams;
    
    public IntentReader(Activity param1Activity) {
      this((Context)Preconditions.checkNotNull(param1Activity), param1Activity.getIntent());
    }
    
    public IntentReader(Context param1Context, Intent param1Intent) {
      this.mContext = (Context)Preconditions.checkNotNull(param1Context);
      this.mIntent = (Intent)Preconditions.checkNotNull(param1Intent);
      this.mCallingPackage = ShareCompat.getCallingPackage(param1Intent);
      this.mCallingActivity = ShareCompat.getCallingActivity(param1Intent);
    }
    
    @Deprecated
    public static IntentReader from(Activity param1Activity) {
      return new IntentReader(param1Activity);
    }
    
    private static void withinStyle(StringBuilder param1StringBuilder, CharSequence param1CharSequence, int param1Int1, int param1Int2) {
      while (param1Int1 < param1Int2) {
        char c = param1CharSequence.charAt(param1Int1);
        if (c == '<') {
          param1StringBuilder.append("&lt;");
        } else if (c == '>') {
          param1StringBuilder.append("&gt;");
        } else if (c == '&') {
          param1StringBuilder.append("&amp;");
        } else if (c > '~' || c < ' ') {
          param1StringBuilder.append("&#").append(c).append(";");
        } else if (c == ' ') {
          while (param1Int1 + 1 < param1Int2 && param1CharSequence.charAt(param1Int1 + 1) == ' ') {
            param1StringBuilder.append("&nbsp;");
            param1Int1++;
          } 
          param1StringBuilder.append(' ');
        } else {
          param1StringBuilder.append(c);
        } 
        param1Int1++;
      } 
    }
    
    public ComponentName getCallingActivity() {
      return this.mCallingActivity;
    }
    
    public Drawable getCallingActivityIcon() {
      if (this.mCallingActivity == null)
        return null; 
      PackageManager packageManager = this.mContext.getPackageManager();
      try {
        return packageManager.getActivityIcon(this.mCallingActivity);
      } catch (android.content.pm.PackageManager.NameNotFoundException nameNotFoundException) {
        Log.e("IntentReader", "Could not retrieve icon for calling activity", (Throwable)nameNotFoundException);
        return null;
      } 
    }
    
    public Drawable getCallingApplicationIcon() {
      if (this.mCallingPackage == null)
        return null; 
      PackageManager packageManager = this.mContext.getPackageManager();
      try {
        return packageManager.getApplicationIcon(this.mCallingPackage);
      } catch (android.content.pm.PackageManager.NameNotFoundException nameNotFoundException) {
        Log.e("IntentReader", "Could not retrieve icon for calling application", (Throwable)nameNotFoundException);
        return null;
      } 
    }
    
    public CharSequence getCallingApplicationLabel() {
      if (this.mCallingPackage == null)
        return null; 
      PackageManager packageManager = this.mContext.getPackageManager();
      try {
        return packageManager.getApplicationLabel(packageManager.getApplicationInfo(this.mCallingPackage, 0));
      } catch (android.content.pm.PackageManager.NameNotFoundException nameNotFoundException) {
        Log.e("IntentReader", "Could not retrieve label for calling application", (Throwable)nameNotFoundException);
        return null;
      } 
    }
    
    public String getCallingPackage() {
      return this.mCallingPackage;
    }
    
    public String[] getEmailBcc() {
      return this.mIntent.getStringArrayExtra("android.intent.extra.BCC");
    }
    
    public String[] getEmailCc() {
      return this.mIntent.getStringArrayExtra("android.intent.extra.CC");
    }
    
    public String[] getEmailTo() {
      return this.mIntent.getStringArrayExtra("android.intent.extra.EMAIL");
    }
    
    public String getHtmlText() {
      String str2 = this.mIntent.getStringExtra("android.intent.extra.HTML_TEXT");
      String str1 = str2;
      if (str2 == null) {
        CharSequence charSequence = getText();
        if (charSequence instanceof Spanned) {
          str1 = Html.toHtml((Spanned)charSequence);
        } else {
          str1 = str2;
          if (charSequence != null)
            if (Build.VERSION.SDK_INT >= 16) {
              str1 = Html.escapeHtml(charSequence);
            } else {
              StringBuilder stringBuilder = new StringBuilder();
              withinStyle(stringBuilder, charSequence, 0, charSequence.length());
              str1 = stringBuilder.toString();
            }  
        } 
      } 
      return str1;
    }
    
    public Uri getStream() {
      return (Uri)this.mIntent.getParcelableExtra("android.intent.extra.STREAM");
    }
    
    public Uri getStream(int param1Int) {
      if (this.mStreams == null && isMultipleShare())
        this.mStreams = this.mIntent.getParcelableArrayListExtra("android.intent.extra.STREAM"); 
      ArrayList<Uri> arrayList = this.mStreams;
      if (arrayList != null)
        return arrayList.get(param1Int); 
      if (param1Int == 0)
        return (Uri)this.mIntent.getParcelableExtra("android.intent.extra.STREAM"); 
      throw new IndexOutOfBoundsException("Stream items available: " + getStreamCount() + " index requested: " + param1Int);
    }
    
    public int getStreamCount() {
      if (this.mStreams == null && isMultipleShare())
        this.mStreams = this.mIntent.getParcelableArrayListExtra("android.intent.extra.STREAM"); 
      ArrayList<Uri> arrayList = this.mStreams;
      return (arrayList != null) ? arrayList.size() : this.mIntent.hasExtra("android.intent.extra.STREAM");
    }
    
    public String getSubject() {
      return this.mIntent.getStringExtra("android.intent.extra.SUBJECT");
    }
    
    public CharSequence getText() {
      return this.mIntent.getCharSequenceExtra("android.intent.extra.TEXT");
    }
    
    public String getType() {
      return this.mIntent.getType();
    }
    
    public boolean isMultipleShare() {
      return "android.intent.action.SEND_MULTIPLE".equals(this.mIntent.getAction());
    }
    
    public boolean isShareIntent() {
      String str = this.mIntent.getAction();
      return ("android.intent.action.SEND".equals(str) || "android.intent.action.SEND_MULTIPLE".equals(str));
    }
    
    public boolean isSingleShare() {
      return "android.intent.action.SEND".equals(this.mIntent.getAction());
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\app\ShareCompat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */