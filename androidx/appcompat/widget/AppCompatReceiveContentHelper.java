package androidx.appcompat.widget;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.text.Selection;
import android.text.Spannable;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.TextView;
import androidx.core.view.ContentInfoCompat;
import androidx.core.view.ViewCompat;

final class AppCompatReceiveContentHelper {
  private static final String LOG_TAG = "ReceiveContent";
  
  static boolean maybeHandleDragEventViaPerformReceiveContent(View paramView, DragEvent paramDragEvent) {
    if (Build.VERSION.SDK_INT >= 31 || Build.VERSION.SDK_INT < 24 || paramDragEvent.getLocalState() != null || ViewCompat.getOnReceiveContentMimeTypes(paramView) == null)
      return false; 
    Activity activity = tryGetActivity(paramView);
    if (activity == null) {
      Log.i("ReceiveContent", "Can't handle drop: no activity: view=" + paramView);
      return false;
    } 
    if (paramDragEvent.getAction() == 1)
      return paramView instanceof TextView ^ true; 
    if (paramDragEvent.getAction() == 3) {
      boolean bool;
      if (paramView instanceof TextView) {
        bool = OnDropApi24Impl.onDropForTextView(paramDragEvent, (TextView)paramView, activity);
      } else {
        bool = OnDropApi24Impl.onDropForView(paramDragEvent, paramView, activity);
      } 
      return bool;
    } 
    return false;
  }
  
  static boolean maybeHandleMenuActionViaPerformReceiveContent(TextView paramTextView, int paramInt) {
    ClipData clipData;
    int i = Build.VERSION.SDK_INT;
    boolean bool = false;
    if (i >= 31 || ViewCompat.getOnReceiveContentMimeTypes((View)paramTextView) == null || (paramInt != 16908322 && paramInt != 16908337))
      return false; 
    ClipboardManager clipboardManager = (ClipboardManager)paramTextView.getContext().getSystemService("clipboard");
    if (clipboardManager == null) {
      clipboardManager = null;
    } else {
      clipData = clipboardManager.getPrimaryClip();
    } 
    if (clipData != null && clipData.getItemCount() > 0) {
      ContentInfoCompat.Builder builder = new ContentInfoCompat.Builder(clipData, 1);
      if (paramInt == 16908322) {
        paramInt = bool;
      } else {
        paramInt = 1;
      } 
      ViewCompat.performReceiveContent((View)paramTextView, builder.setFlags(paramInt).build());
    } 
    return true;
  }
  
  static Activity tryGetActivity(View paramView) {
    for (Context context = paramView.getContext(); context instanceof ContextWrapper; context = ((ContextWrapper)context).getBaseContext()) {
      if (context instanceof Activity)
        return (Activity)context; 
    } 
    return null;
  }
  
  private static final class OnDropApi24Impl {
    static boolean onDropForTextView(DragEvent param1DragEvent, TextView param1TextView, Activity param1Activity) {
      param1Activity.requestDragAndDropPermissions(param1DragEvent);
      int i = param1TextView.getOffsetForPosition(param1DragEvent.getX(), param1DragEvent.getY());
      param1TextView.beginBatchEdit();
      try {
        Selection.setSelection((Spannable)param1TextView.getText(), i);
        ContentInfoCompat.Builder builder = new ContentInfoCompat.Builder();
        this(param1DragEvent.getClipData(), 3);
        ViewCompat.performReceiveContent((View)param1TextView, builder.build());
        return true;
      } finally {
        param1TextView.endBatchEdit();
      } 
    }
    
    static boolean onDropForView(DragEvent param1DragEvent, View param1View, Activity param1Activity) {
      param1Activity.requestDragAndDropPermissions(param1DragEvent);
      ViewCompat.performReceiveContent(param1View, (new ContentInfoCompat.Builder(param1DragEvent.getClipData(), 3)).build());
      return true;
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\appcompat\widget\AppCompatReceiveContentHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */