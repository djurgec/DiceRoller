package androidx.fragment.app;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewTreeLifecycleOwner;
import androidx.lifecycle.ViewTreeViewModelStoreOwner;
import androidx.savedstate.ViewTreeSavedStateRegistryOwner;

public class DialogFragment extends Fragment implements DialogInterface.OnCancelListener, DialogInterface.OnDismissListener {
  private static final String SAVED_BACK_STACK_ID = "android:backStackId";
  
  private static final String SAVED_CANCELABLE = "android:cancelable";
  
  private static final String SAVED_DIALOG_STATE_TAG = "android:savedDialogState";
  
  private static final String SAVED_INTERNAL_DIALOG_SHOWING = "android:dialogShowing";
  
  private static final String SAVED_SHOWS_DIALOG = "android:showsDialog";
  
  private static final String SAVED_STYLE = "android:style";
  
  private static final String SAVED_THEME = "android:theme";
  
  public static final int STYLE_NORMAL = 0;
  
  public static final int STYLE_NO_FRAME = 2;
  
  public static final int STYLE_NO_INPUT = 3;
  
  public static final int STYLE_NO_TITLE = 1;
  
  private int mBackStackId = -1;
  
  private boolean mCancelable = true;
  
  private boolean mCreatingDialog;
  
  private Dialog mDialog;
  
  private boolean mDialogCreated = false;
  
  private Runnable mDismissRunnable = new Runnable() {
      final DialogFragment this$0;
      
      public void run() {
        DialogFragment.this.mOnDismissListener.onDismiss((DialogInterface)DialogFragment.this.mDialog);
      }
    };
  
  private boolean mDismissed;
  
  private Handler mHandler;
  
  private Observer<LifecycleOwner> mObserver = new Observer<LifecycleOwner>() {
      final DialogFragment this$0;
      
      public void onChanged(LifecycleOwner param1LifecycleOwner) {
        if (param1LifecycleOwner != null && DialogFragment.this.mShowsDialog) {
          View view = DialogFragment.this.requireView();
          if (view.getParent() == null) {
            if (DialogFragment.this.mDialog != null) {
              if (FragmentManager.isLoggingEnabled(3))
                Log.d("FragmentManager", "DialogFragment " + this + " setting the content view on " + DialogFragment.this.mDialog); 
              DialogFragment.this.mDialog.setContentView(view);
            } 
          } else {
            throw new IllegalStateException("DialogFragment can not be attached to a container view");
          } 
        } 
      }
    };
  
  private DialogInterface.OnCancelListener mOnCancelListener = new DialogInterface.OnCancelListener() {
      final DialogFragment this$0;
      
      public void onCancel(DialogInterface param1DialogInterface) {
        if (DialogFragment.this.mDialog != null) {
          DialogFragment dialogFragment = DialogFragment.this;
          dialogFragment.onCancel((DialogInterface)dialogFragment.mDialog);
        } 
      }
    };
  
  private DialogInterface.OnDismissListener mOnDismissListener = new DialogInterface.OnDismissListener() {
      final DialogFragment this$0;
      
      public void onDismiss(DialogInterface param1DialogInterface) {
        if (DialogFragment.this.mDialog != null) {
          DialogFragment dialogFragment = DialogFragment.this;
          dialogFragment.onDismiss((DialogInterface)dialogFragment.mDialog);
        } 
      }
    };
  
  private boolean mShownByMe;
  
  private boolean mShowsDialog = true;
  
  private int mStyle = 0;
  
  private int mTheme = 0;
  
  private boolean mViewDestroyed;
  
  public DialogFragment() {}
  
  public DialogFragment(int paramInt) {
    super(paramInt);
  }
  
  private void dismissInternal(boolean paramBoolean1, boolean paramBoolean2) {
    if (this.mDismissed)
      return; 
    this.mDismissed = true;
    this.mShownByMe = false;
    Dialog dialog = this.mDialog;
    if (dialog != null) {
      dialog.setOnDismissListener(null);
      this.mDialog.dismiss();
      if (!paramBoolean2)
        if (Looper.myLooper() == this.mHandler.getLooper()) {
          onDismiss((DialogInterface)this.mDialog);
        } else {
          this.mHandler.post(this.mDismissRunnable);
        }  
    } 
    this.mViewDestroyed = true;
    if (this.mBackStackId >= 0) {
      getParentFragmentManager().popBackStack(this.mBackStackId, 1);
      this.mBackStackId = -1;
    } else {
      FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
      fragmentTransaction.remove(this);
      if (paramBoolean1) {
        fragmentTransaction.commitAllowingStateLoss();
      } else {
        fragmentTransaction.commit();
      } 
    } 
  }
  
  private void prepareDialog(Bundle paramBundle) {
    if (!this.mShowsDialog)
      return; 
    if (!this.mDialogCreated)
      try {
        this.mCreatingDialog = true;
        Dialog dialog = onCreateDialog(paramBundle);
        this.mDialog = dialog;
        if (this.mShowsDialog) {
          setupDialog(dialog, this.mStyle);
          Context context = getContext();
          if (context instanceof Activity)
            this.mDialog.setOwnerActivity((Activity)context); 
          this.mDialog.setCancelable(this.mCancelable);
          this.mDialog.setOnCancelListener(this.mOnCancelListener);
          this.mDialog.setOnDismissListener(this.mOnDismissListener);
          this.mDialogCreated = true;
        } else {
          this.mDialog = null;
        } 
      } finally {
        this.mCreatingDialog = false;
      }  
  }
  
  FragmentContainer createFragmentContainer() {
    return new FragmentContainer() {
        final DialogFragment this$0;
        
        final FragmentContainer val$fragmentContainer;
        
        public View onFindViewById(int param1Int) {
          return fragmentContainer.onHasView() ? fragmentContainer.onFindViewById(param1Int) : DialogFragment.this.onFindViewById(param1Int);
        }
        
        public boolean onHasView() {
          return (fragmentContainer.onHasView() || DialogFragment.this.onHasView());
        }
      };
  }
  
  public void dismiss() {
    dismissInternal(false, false);
  }
  
  public void dismissAllowingStateLoss() {
    dismissInternal(true, false);
  }
  
  public Dialog getDialog() {
    return this.mDialog;
  }
  
  public boolean getShowsDialog() {
    return this.mShowsDialog;
  }
  
  public int getTheme() {
    return this.mTheme;
  }
  
  public boolean isCancelable() {
    return this.mCancelable;
  }
  
  public void onAttach(Context paramContext) {
    super.onAttach(paramContext);
    getViewLifecycleOwnerLiveData().observeForever(this.mObserver);
    if (!this.mShownByMe)
      this.mDismissed = false; 
  }
  
  public void onCancel(DialogInterface paramDialogInterface) {}
  
  public void onCreate(Bundle paramBundle) {
    boolean bool;
    super.onCreate(paramBundle);
    this.mHandler = new Handler();
    if (this.mContainerId == 0) {
      bool = true;
    } else {
      bool = false;
    } 
    this.mShowsDialog = bool;
    if (paramBundle != null) {
      this.mStyle = paramBundle.getInt("android:style", 0);
      this.mTheme = paramBundle.getInt("android:theme", 0);
      this.mCancelable = paramBundle.getBoolean("android:cancelable", true);
      this.mShowsDialog = paramBundle.getBoolean("android:showsDialog", this.mShowsDialog);
      this.mBackStackId = paramBundle.getInt("android:backStackId", -1);
    } 
  }
  
  public Dialog onCreateDialog(Bundle paramBundle) {
    if (FragmentManager.isLoggingEnabled(3))
      Log.d("FragmentManager", "onCreateDialog called for DialogFragment " + this); 
    return new Dialog(requireContext(), getTheme());
  }
  
  public void onDestroyView() {
    super.onDestroyView();
    Dialog dialog = this.mDialog;
    if (dialog != null) {
      this.mViewDestroyed = true;
      dialog.setOnDismissListener(null);
      this.mDialog.dismiss();
      if (!this.mDismissed)
        onDismiss((DialogInterface)this.mDialog); 
      this.mDialog = null;
      this.mDialogCreated = false;
    } 
  }
  
  public void onDetach() {
    super.onDetach();
    if (!this.mShownByMe && !this.mDismissed)
      this.mDismissed = true; 
    getViewLifecycleOwnerLiveData().removeObserver(this.mObserver);
  }
  
  public void onDismiss(DialogInterface paramDialogInterface) {
    if (!this.mViewDestroyed) {
      if (FragmentManager.isLoggingEnabled(3))
        Log.d("FragmentManager", "onDismiss called for DialogFragment " + this); 
      dismissInternal(true, true);
    } 
  }
  
  View onFindViewById(int paramInt) {
    Dialog dialog = this.mDialog;
    return (dialog != null) ? dialog.findViewById(paramInt) : null;
  }
  
  public LayoutInflater onGetLayoutInflater(Bundle paramBundle) {
    String str;
    LayoutInflater layoutInflater2 = super.onGetLayoutInflater(paramBundle);
    if (!this.mShowsDialog || this.mCreatingDialog) {
      if (FragmentManager.isLoggingEnabled(2)) {
        str = "getting layout inflater for DialogFragment " + this;
        if (!this.mShowsDialog) {
          Log.d("FragmentManager", "mShowsDialog = false: " + str);
        } else {
          Log.d("FragmentManager", "mCreatingDialog = true: " + str);
        } 
      } 
      return layoutInflater2;
    } 
    prepareDialog((Bundle)str);
    if (FragmentManager.isLoggingEnabled(2))
      Log.d("FragmentManager", "get layout inflater for DialogFragment " + this + " from dialog context"); 
    Dialog dialog = this.mDialog;
    LayoutInflater layoutInflater1 = layoutInflater2;
    if (dialog != null)
      layoutInflater1 = layoutInflater2.cloneInContext(dialog.getContext()); 
    return layoutInflater1;
  }
  
  boolean onHasView() {
    return this.mDialogCreated;
  }
  
  public void onSaveInstanceState(Bundle paramBundle) {
    super.onSaveInstanceState(paramBundle);
    Dialog dialog = this.mDialog;
    if (dialog != null) {
      Bundle bundle = dialog.onSaveInstanceState();
      bundle.putBoolean("android:dialogShowing", false);
      paramBundle.putBundle("android:savedDialogState", bundle);
    } 
    int i = this.mStyle;
    if (i != 0)
      paramBundle.putInt("android:style", i); 
    i = this.mTheme;
    if (i != 0)
      paramBundle.putInt("android:theme", i); 
    boolean bool = this.mCancelable;
    if (!bool)
      paramBundle.putBoolean("android:cancelable", bool); 
    bool = this.mShowsDialog;
    if (!bool)
      paramBundle.putBoolean("android:showsDialog", bool); 
    i = this.mBackStackId;
    if (i != -1)
      paramBundle.putInt("android:backStackId", i); 
  }
  
  public void onStart() {
    super.onStart();
    Dialog dialog = this.mDialog;
    if (dialog != null) {
      this.mViewDestroyed = false;
      dialog.show();
      View view = this.mDialog.getWindow().getDecorView();
      ViewTreeLifecycleOwner.set(view, this);
      ViewTreeViewModelStoreOwner.set(view, this);
      ViewTreeSavedStateRegistryOwner.set(view, this);
    } 
  }
  
  public void onStop() {
    super.onStop();
    Dialog dialog = this.mDialog;
    if (dialog != null)
      dialog.hide(); 
  }
  
  public void onViewStateRestored(Bundle paramBundle) {
    super.onViewStateRestored(paramBundle);
    if (this.mDialog != null && paramBundle != null) {
      paramBundle = paramBundle.getBundle("android:savedDialogState");
      if (paramBundle != null)
        this.mDialog.onRestoreInstanceState(paramBundle); 
    } 
  }
  
  void performCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
    super.performCreateView(paramLayoutInflater, paramViewGroup, paramBundle);
    if (this.mView == null && this.mDialog != null && paramBundle != null) {
      Bundle bundle = paramBundle.getBundle("android:savedDialogState");
      if (bundle != null)
        this.mDialog.onRestoreInstanceState(bundle); 
    } 
  }
  
  public final Dialog requireDialog() {
    Dialog dialog = getDialog();
    if (dialog != null)
      return dialog; 
    throw new IllegalStateException("DialogFragment " + this + " does not have a Dialog.");
  }
  
  public void setCancelable(boolean paramBoolean) {
    this.mCancelable = paramBoolean;
    Dialog dialog = this.mDialog;
    if (dialog != null)
      dialog.setCancelable(paramBoolean); 
  }
  
  public void setShowsDialog(boolean paramBoolean) {
    this.mShowsDialog = paramBoolean;
  }
  
  public void setStyle(int paramInt1, int paramInt2) {
    if (FragmentManager.isLoggingEnabled(2))
      Log.d("FragmentManager", "Setting style and theme for DialogFragment " + this + " to " + paramInt1 + ", " + paramInt2); 
    this.mStyle = paramInt1;
    if (paramInt1 == 2 || paramInt1 == 3)
      this.mTheme = 16973913; 
    if (paramInt2 != 0)
      this.mTheme = paramInt2; 
  }
  
  public void setupDialog(Dialog paramDialog, int paramInt) {
    Window window;
    switch (paramInt) {
      default:
        return;
      case 3:
        window = paramDialog.getWindow();
        if (window != null)
          window.addFlags(24); 
        break;
      case 1:
      case 2:
        break;
    } 
    paramDialog.requestWindowFeature(1);
  }
  
  public int show(FragmentTransaction paramFragmentTransaction, String paramString) {
    this.mDismissed = false;
    this.mShownByMe = true;
    paramFragmentTransaction.add(this, paramString);
    this.mViewDestroyed = false;
    int i = paramFragmentTransaction.commit();
    this.mBackStackId = i;
    return i;
  }
  
  public void show(FragmentManager paramFragmentManager, String paramString) {
    this.mDismissed = false;
    this.mShownByMe = true;
    FragmentTransaction fragmentTransaction = paramFragmentManager.beginTransaction();
    fragmentTransaction.add(this, paramString);
    fragmentTransaction.commit();
  }
  
  public void showNow(FragmentManager paramFragmentManager, String paramString) {
    this.mDismissed = false;
    this.mShownByMe = true;
    FragmentTransaction fragmentTransaction = paramFragmentManager.beginTransaction();
    fragmentTransaction.add(this, paramString);
    fragmentTransaction.commitNow();
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\fragment\app\DialogFragment.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */