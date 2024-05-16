package androidx.appcompat.app;

import android.app.Dialog;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;

public class AppCompatDialogFragment extends DialogFragment {
  public AppCompatDialogFragment() {}
  
  public AppCompatDialogFragment(int paramInt) {
    super(paramInt);
  }
  
  public Dialog onCreateDialog(Bundle paramBundle) {
    return new AppCompatDialog(getContext(), getTheme());
  }
  
  public void setupDialog(Dialog paramDialog, int paramInt) {
    if (paramDialog instanceof AppCompatDialog) {
      AppCompatDialog appCompatDialog = (AppCompatDialog)paramDialog;
      switch (paramInt) {
        default:
          return;
        case 3:
          paramDialog.getWindow().addFlags(24);
          break;
        case 1:
        case 2:
          break;
      } 
      appCompatDialog.supportRequestWindowFeature(1);
    } 
    super.setupDialog(paramDialog, paramInt);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\appcompat\app\AppCompatDialogFragment.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */