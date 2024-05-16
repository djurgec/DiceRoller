package androidx.activity.result.contract;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.IntentSenderRequest;
import androidx.collection.ArrayMap;
import androidx.core.content.ContextCompat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

public final class ActivityResultContracts {
  public static class CreateDocument extends ActivityResultContract<String, Uri> {
    public Intent createIntent(Context param1Context, String param1String) {
      return (new Intent("android.intent.action.CREATE_DOCUMENT")).setType("*/*").putExtra("android.intent.extra.TITLE", param1String);
    }
    
    public final ActivityResultContract.SynchronousResult<Uri> getSynchronousResult(Context param1Context, String param1String) {
      return null;
    }
    
    public final Uri parseResult(int param1Int, Intent param1Intent) {
      return (param1Intent == null || param1Int != -1) ? null : param1Intent.getData();
    }
  }
  
  public static class GetContent extends ActivityResultContract<String, Uri> {
    public Intent createIntent(Context param1Context, String param1String) {
      return (new Intent("android.intent.action.GET_CONTENT")).addCategory("android.intent.category.OPENABLE").setType(param1String);
    }
    
    public final ActivityResultContract.SynchronousResult<Uri> getSynchronousResult(Context param1Context, String param1String) {
      return null;
    }
    
    public final Uri parseResult(int param1Int, Intent param1Intent) {
      return (param1Intent == null || param1Int != -1) ? null : param1Intent.getData();
    }
  }
  
  public static class GetMultipleContents extends ActivityResultContract<String, List<Uri>> {
    static List<Uri> getClipDataUris(Intent param1Intent) {
      LinkedHashSet<Uri> linkedHashSet = new LinkedHashSet();
      if (param1Intent.getData() != null)
        linkedHashSet.add(param1Intent.getData()); 
      ClipData clipData = param1Intent.getClipData();
      if (clipData == null && linkedHashSet.isEmpty())
        return Collections.emptyList(); 
      if (clipData != null)
        for (byte b = 0; b < clipData.getItemCount(); b++) {
          Uri uri = clipData.getItemAt(b).getUri();
          if (uri != null)
            linkedHashSet.add(uri); 
        }  
      return new ArrayList<>(linkedHashSet);
    }
    
    public Intent createIntent(Context param1Context, String param1String) {
      return (new Intent("android.intent.action.GET_CONTENT")).addCategory("android.intent.category.OPENABLE").setType(param1String).putExtra("android.intent.extra.ALLOW_MULTIPLE", true);
    }
    
    public final ActivityResultContract.SynchronousResult<List<Uri>> getSynchronousResult(Context param1Context, String param1String) {
      return null;
    }
    
    public final List<Uri> parseResult(int param1Int, Intent param1Intent) {
      return (param1Intent == null || param1Int != -1) ? Collections.emptyList() : getClipDataUris(param1Intent);
    }
  }
  
  public static class OpenDocument extends ActivityResultContract<String[], Uri> {
    public Intent createIntent(Context param1Context, String[] param1ArrayOfString) {
      return (new Intent("android.intent.action.OPEN_DOCUMENT")).putExtra("android.intent.extra.MIME_TYPES", param1ArrayOfString).setType("*/*");
    }
    
    public final ActivityResultContract.SynchronousResult<Uri> getSynchronousResult(Context param1Context, String[] param1ArrayOfString) {
      return null;
    }
    
    public final Uri parseResult(int param1Int, Intent param1Intent) {
      return (param1Intent == null || param1Int != -1) ? null : param1Intent.getData();
    }
  }
  
  public static class OpenDocumentTree extends ActivityResultContract<Uri, Uri> {
    public Intent createIntent(Context param1Context, Uri param1Uri) {
      Intent intent = new Intent("android.intent.action.OPEN_DOCUMENT_TREE");
      if (Build.VERSION.SDK_INT >= 26 && param1Uri != null)
        intent.putExtra("android.provider.extra.INITIAL_URI", (Parcelable)param1Uri); 
      return intent;
    }
    
    public final ActivityResultContract.SynchronousResult<Uri> getSynchronousResult(Context param1Context, Uri param1Uri) {
      return null;
    }
    
    public final Uri parseResult(int param1Int, Intent param1Intent) {
      return (param1Intent == null || param1Int != -1) ? null : param1Intent.getData();
    }
  }
  
  public static class OpenMultipleDocuments extends ActivityResultContract<String[], List<Uri>> {
    public Intent createIntent(Context param1Context, String[] param1ArrayOfString) {
      return (new Intent("android.intent.action.OPEN_DOCUMENT")).putExtra("android.intent.extra.MIME_TYPES", param1ArrayOfString).putExtra("android.intent.extra.ALLOW_MULTIPLE", true).setType("*/*");
    }
    
    public final ActivityResultContract.SynchronousResult<List<Uri>> getSynchronousResult(Context param1Context, String[] param1ArrayOfString) {
      return null;
    }
    
    public final List<Uri> parseResult(int param1Int, Intent param1Intent) {
      return (param1Int != -1 || param1Intent == null) ? Collections.emptyList() : ActivityResultContracts.GetMultipleContents.getClipDataUris(param1Intent);
    }
  }
  
  public static final class PickContact extends ActivityResultContract<Void, Uri> {
    public Intent createIntent(Context param1Context, Void param1Void) {
      return (new Intent("android.intent.action.PICK")).setType("vnd.android.cursor.dir/contact");
    }
    
    public Uri parseResult(int param1Int, Intent param1Intent) {
      return (param1Intent == null || param1Int != -1) ? null : param1Intent.getData();
    }
  }
  
  public static final class RequestMultiplePermissions extends ActivityResultContract<String[], Map<String, Boolean>> {
    public static final String ACTION_REQUEST_PERMISSIONS = "androidx.activity.result.contract.action.REQUEST_PERMISSIONS";
    
    public static final String EXTRA_PERMISSIONS = "androidx.activity.result.contract.extra.PERMISSIONS";
    
    public static final String EXTRA_PERMISSION_GRANT_RESULTS = "androidx.activity.result.contract.extra.PERMISSION_GRANT_RESULTS";
    
    static Intent createIntent(String[] param1ArrayOfString) {
      return (new Intent("androidx.activity.result.contract.action.REQUEST_PERMISSIONS")).putExtra("androidx.activity.result.contract.extra.PERMISSIONS", param1ArrayOfString);
    }
    
    public Intent createIntent(Context param1Context, String[] param1ArrayOfString) {
      return createIntent(param1ArrayOfString);
    }
    
    public ActivityResultContract.SynchronousResult<Map<String, Boolean>> getSynchronousResult(Context param1Context, String[] param1ArrayOfString) {
      if (param1ArrayOfString == null || param1ArrayOfString.length == 0)
        return new ActivityResultContract.SynchronousResult<>(Collections.emptyMap()); 
      ArrayMap<String, Boolean> arrayMap = new ArrayMap();
      boolean bool = true;
      int i = param1ArrayOfString.length;
      for (byte b = 0; b < i; b++) {
        boolean bool1;
        String str = param1ArrayOfString[b];
        if (ContextCompat.checkSelfPermission(param1Context, str) == 0) {
          bool1 = true;
        } else {
          bool1 = false;
        } 
        arrayMap.put(str, Boolean.valueOf(bool1));
        if (!bool1)
          bool = false; 
      } 
      return bool ? new ActivityResultContract.SynchronousResult(arrayMap) : null;
    }
    
    public Map<String, Boolean> parseResult(int param1Int, Intent param1Intent) {
      if (param1Int != -1)
        return Collections.emptyMap(); 
      if (param1Intent == null)
        return Collections.emptyMap(); 
      String[] arrayOfString = param1Intent.getStringArrayExtra("androidx.activity.result.contract.extra.PERMISSIONS");
      int[] arrayOfInt = param1Intent.getIntArrayExtra("androidx.activity.result.contract.extra.PERMISSION_GRANT_RESULTS");
      if (arrayOfInt == null || arrayOfString == null)
        return Collections.emptyMap(); 
      HashMap<Object, Object> hashMap = new HashMap<>();
      param1Int = 0;
      int i = arrayOfString.length;
      while (param1Int < i) {
        boolean bool;
        String str = arrayOfString[param1Int];
        if (arrayOfInt[param1Int] == 0) {
          bool = true;
        } else {
          bool = false;
        } 
        hashMap.put(str, Boolean.valueOf(bool));
        param1Int++;
      } 
      return (Map)hashMap;
    }
  }
  
  public static final class RequestPermission extends ActivityResultContract<String, Boolean> {
    public Intent createIntent(Context param1Context, String param1String) {
      return ActivityResultContracts.RequestMultiplePermissions.createIntent(new String[] { param1String });
    }
    
    public ActivityResultContract.SynchronousResult<Boolean> getSynchronousResult(Context param1Context, String param1String) {
      return (param1String == null) ? new ActivityResultContract.SynchronousResult<>(Boolean.valueOf(false)) : ((ContextCompat.checkSelfPermission(param1Context, param1String) == 0) ? new ActivityResultContract.SynchronousResult<>(Boolean.valueOf(true)) : null);
    }
    
    public Boolean parseResult(int param1Int, Intent param1Intent) {
      boolean bool = false;
      Boolean bool1 = Boolean.valueOf(false);
      if (param1Intent == null || param1Int != -1)
        return bool1; 
      int[] arrayOfInt = param1Intent.getIntArrayExtra("androidx.activity.result.contract.extra.PERMISSION_GRANT_RESULTS");
      if (arrayOfInt == null || arrayOfInt.length == 0)
        return bool1; 
      if (arrayOfInt[0] == 0)
        bool = true; 
      return Boolean.valueOf(bool);
    }
  }
  
  public static final class StartActivityForResult extends ActivityResultContract<Intent, ActivityResult> {
    public static final String EXTRA_ACTIVITY_OPTIONS_BUNDLE = "androidx.activity.result.contract.extra.ACTIVITY_OPTIONS_BUNDLE";
    
    public Intent createIntent(Context param1Context, Intent param1Intent) {
      return param1Intent;
    }
    
    public ActivityResult parseResult(int param1Int, Intent param1Intent) {
      return new ActivityResult(param1Int, param1Intent);
    }
  }
  
  public static final class StartIntentSenderForResult extends ActivityResultContract<IntentSenderRequest, ActivityResult> {
    public static final String ACTION_INTENT_SENDER_REQUEST = "androidx.activity.result.contract.action.INTENT_SENDER_REQUEST";
    
    public static final String EXTRA_INTENT_SENDER_REQUEST = "androidx.activity.result.contract.extra.INTENT_SENDER_REQUEST";
    
    public static final String EXTRA_SEND_INTENT_EXCEPTION = "androidx.activity.result.contract.extra.SEND_INTENT_EXCEPTION";
    
    public Intent createIntent(Context param1Context, IntentSenderRequest param1IntentSenderRequest) {
      return (new Intent("androidx.activity.result.contract.action.INTENT_SENDER_REQUEST")).putExtra("androidx.activity.result.contract.extra.INTENT_SENDER_REQUEST", (Parcelable)param1IntentSenderRequest);
    }
    
    public ActivityResult parseResult(int param1Int, Intent param1Intent) {
      return new ActivityResult(param1Int, param1Intent);
    }
  }
  
  public static class TakePicture extends ActivityResultContract<Uri, Boolean> {
    public Intent createIntent(Context param1Context, Uri param1Uri) {
      return (new Intent("android.media.action.IMAGE_CAPTURE")).putExtra("output", (Parcelable)param1Uri);
    }
    
    public final ActivityResultContract.SynchronousResult<Boolean> getSynchronousResult(Context param1Context, Uri param1Uri) {
      return null;
    }
    
    public final Boolean parseResult(int param1Int, Intent param1Intent) {
      boolean bool;
      if (param1Int == -1) {
        bool = true;
      } else {
        bool = false;
      } 
      return Boolean.valueOf(bool);
    }
  }
  
  public static class TakePicturePreview extends ActivityResultContract<Void, Bitmap> {
    public Intent createIntent(Context param1Context, Void param1Void) {
      return new Intent("android.media.action.IMAGE_CAPTURE");
    }
    
    public final ActivityResultContract.SynchronousResult<Bitmap> getSynchronousResult(Context param1Context, Void param1Void) {
      return null;
    }
    
    public final Bitmap parseResult(int param1Int, Intent param1Intent) {
      return (param1Intent == null || param1Int != -1) ? null : (Bitmap)param1Intent.getParcelableExtra("data");
    }
  }
  
  public static class TakeVideo extends ActivityResultContract<Uri, Bitmap> {
    public Intent createIntent(Context param1Context, Uri param1Uri) {
      return (new Intent("android.media.action.VIDEO_CAPTURE")).putExtra("output", (Parcelable)param1Uri);
    }
    
    public final ActivityResultContract.SynchronousResult<Bitmap> getSynchronousResult(Context param1Context, Uri param1Uri) {
      return null;
    }
    
    public final Bitmap parseResult(int param1Int, Intent param1Intent) {
      return (param1Intent == null || param1Int != -1) ? null : (Bitmap)param1Intent.getParcelableExtra("data");
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\activity\result\contract\ActivityResultContracts.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */