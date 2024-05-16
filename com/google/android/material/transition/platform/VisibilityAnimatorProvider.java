package com.google.android.material.transition.platform;

import android.animation.Animator;
import android.view.View;
import android.view.ViewGroup;

public interface VisibilityAnimatorProvider {
  Animator createAppear(ViewGroup paramViewGroup, View paramView);
  
  Animator createDisappear(ViewGroup paramViewGroup, View paramView);
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\transition\platform\VisibilityAnimatorProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */