package com.google.android.material.internal;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Checkable;
import androidx.appcompat.R;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.customview.view.AbsSavedState;

public class CheckableImageButton extends AppCompatImageButton implements Checkable {
  private static final int[] DRAWABLE_STATE_CHECKED = new int[] { 16842912 };
  
  private boolean checkable = true;
  
  private boolean checked;
  
  private boolean pressable = true;
  
  public CheckableImageButton(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public CheckableImageButton(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, R.attr.imageButtonStyle);
  }
  
  public CheckableImageButton(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    ViewCompat.setAccessibilityDelegate((View)this, new AccessibilityDelegateCompat() {
          final CheckableImageButton this$0;
          
          public void onInitializeAccessibilityEvent(View param1View, AccessibilityEvent param1AccessibilityEvent) {
            super.onInitializeAccessibilityEvent(param1View, param1AccessibilityEvent);
            param1AccessibilityEvent.setChecked(CheckableImageButton.this.isChecked());
          }
          
          public void onInitializeAccessibilityNodeInfo(View param1View, AccessibilityNodeInfoCompat param1AccessibilityNodeInfoCompat) {
            super.onInitializeAccessibilityNodeInfo(param1View, param1AccessibilityNodeInfoCompat);
            param1AccessibilityNodeInfoCompat.setCheckable(CheckableImageButton.this.isCheckable());
            param1AccessibilityNodeInfoCompat.setChecked(CheckableImageButton.this.isChecked());
          }
        });
  }
  
  public boolean isCheckable() {
    return this.checkable;
  }
  
  public boolean isChecked() {
    return this.checked;
  }
  
  public boolean isPressable() {
    return this.pressable;
  }
  
  public int[] onCreateDrawableState(int paramInt) {
    if (this.checked) {
      int[] arrayOfInt = DRAWABLE_STATE_CHECKED;
      return mergeDrawableStates(super.onCreateDrawableState(arrayOfInt.length + paramInt), arrayOfInt);
    } 
    return super.onCreateDrawableState(paramInt);
  }
  
  protected void onRestoreInstanceState(Parcelable paramParcelable) {
    if (!(paramParcelable instanceof SavedState)) {
      super.onRestoreInstanceState(paramParcelable);
      return;
    } 
    SavedState savedState = (SavedState)paramParcelable;
    super.onRestoreInstanceState(savedState.getSuperState());
    setChecked(savedState.checked);
  }
  
  protected Parcelable onSaveInstanceState() {
    SavedState savedState = new SavedState(super.onSaveInstanceState());
    savedState.checked = this.checked;
    return (Parcelable)savedState;
  }
  
  public void setCheckable(boolean paramBoolean) {
    if (this.checkable != paramBoolean) {
      this.checkable = paramBoolean;
      sendAccessibilityEvent(0);
    } 
  }
  
  public void setChecked(boolean paramBoolean) {
    if (this.checkable && this.checked != paramBoolean) {
      this.checked = paramBoolean;
      refreshDrawableState();
      sendAccessibilityEvent(2048);
    } 
  }
  
  public void setPressable(boolean paramBoolean) {
    this.pressable = paramBoolean;
  }
  
  public void setPressed(boolean paramBoolean) {
    if (this.pressable)
      super.setPressed(paramBoolean); 
  }
  
  public void toggle() {
    setChecked(this.checked ^ true);
  }
  
  static class SavedState extends AbsSavedState {
    public static final Parcelable.Creator<SavedState> CREATOR = (Parcelable.Creator<SavedState>)new Parcelable.ClassLoaderCreator<SavedState>() {
        public CheckableImageButton.SavedState createFromParcel(Parcel param2Parcel) {
          return new CheckableImageButton.SavedState(param2Parcel, null);
        }
        
        public CheckableImageButton.SavedState createFromParcel(Parcel param2Parcel, ClassLoader param2ClassLoader) {
          return new CheckableImageButton.SavedState(param2Parcel, param2ClassLoader);
        }
        
        public CheckableImageButton.SavedState[] newArray(int param2Int) {
          return new CheckableImageButton.SavedState[param2Int];
        }
      };
    
    boolean checked;
    
    public SavedState(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      super(param1Parcel, param1ClassLoader);
      readFromParcel(param1Parcel);
    }
    
    public SavedState(Parcelable param1Parcelable) {
      super(param1Parcelable);
    }
    
    private void readFromParcel(Parcel param1Parcel) {
      int i = param1Parcel.readInt();
      boolean bool = true;
      if (i != 1)
        bool = false; 
      this.checked = bool;
    }
    
    public void writeToParcel(Parcel param1Parcel, int param1Int) {
      super.writeToParcel(param1Parcel, param1Int);
      param1Parcel.writeInt(this.checked);
    }
  }
  
  static final class null implements Parcelable.ClassLoaderCreator<SavedState> {
    public CheckableImageButton.SavedState createFromParcel(Parcel param1Parcel) {
      return new CheckableImageButton.SavedState(param1Parcel, null);
    }
    
    public CheckableImageButton.SavedState createFromParcel(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      return new CheckableImageButton.SavedState(param1Parcel, param1ClassLoader);
    }
    
    public CheckableImageButton.SavedState[] newArray(int param1Int) {
      return new CheckableImageButton.SavedState[param1Int];
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\internal\CheckableImageButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */