package com.google.android.material.bottomsheet;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import androidx.appcompat.app.AppCompatDialogFragment;

public class BottomSheetDialogFragment extends AppCompatDialogFragment {
  private boolean waitingForDismissAllowingStateLoss;
  
  private void dismissAfterAnimation() {
    if (this.waitingForDismissAllowingStateLoss) {
      super.dismissAllowingStateLoss();
    } else {
      super.dismiss();
    } 
  }
  
  private void dismissWithAnimation(BottomSheetBehavior<?> paramBottomSheetBehavior, boolean paramBoolean) {
    this.waitingForDismissAllowingStateLoss = paramBoolean;
    if (paramBottomSheetBehavior.getState() == 5) {
      dismissAfterAnimation();
    } else {
      if (getDialog() instanceof BottomSheetDialog)
        ((BottomSheetDialog)getDialog()).removeDefaultCallback(); 
      paramBottomSheetBehavior.addBottomSheetCallback(new BottomSheetDismissCallback());
      paramBottomSheetBehavior.setState(5);
    } 
  }
  
  private boolean tryDismissWithAnimation(boolean paramBoolean) {
    Dialog dialog = getDialog();
    if (dialog instanceof BottomSheetDialog) {
      BottomSheetDialog bottomSheetDialog = (BottomSheetDialog)dialog;
      BottomSheetBehavior<FrameLayout> bottomSheetBehavior = bottomSheetDialog.getBehavior();
      if (bottomSheetBehavior.isHideable() && bottomSheetDialog.getDismissWithAnimation()) {
        dismissWithAnimation(bottomSheetBehavior, paramBoolean);
        return true;
      } 
    } 
    return false;
  }
  
  public void dismiss() {
    if (!tryDismissWithAnimation(false))
      super.dismiss(); 
  }
  
  public void dismissAllowingStateLoss() {
    if (!tryDismissWithAnimation(true))
      super.dismissAllowingStateLoss(); 
  }
  
  public Dialog onCreateDialog(Bundle paramBundle) {
    return (Dialog)new BottomSheetDialog(getContext(), getTheme());
  }
  
  private class BottomSheetDismissCallback extends BottomSheetBehavior.BottomSheetCallback {
    final BottomSheetDialogFragment this$0;
    
    private BottomSheetDismissCallback() {}
    
    public void onSlide(View param1View, float param1Float) {}
    
    public void onStateChanged(View param1View, int param1Int) {
      if (param1Int == 5)
        BottomSheetDialogFragment.this.dismissAfterAnimation(); 
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\bottomsheet\BottomSheetDialogFragment.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */