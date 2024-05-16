package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.widget.SpinnerAdapter;
import androidx.appcompat.view.ContextThemeWrapper;

public interface ThemedSpinnerAdapter extends SpinnerAdapter {
  Resources.Theme getDropDownViewTheme();
  
  void setDropDownViewTheme(Resources.Theme paramTheme);
  
  public static final class Helper {
    private final Context mContext;
    
    private LayoutInflater mDropDownInflater;
    
    private final LayoutInflater mInflater;
    
    public Helper(Context param1Context) {
      this.mContext = param1Context;
      this.mInflater = LayoutInflater.from(param1Context);
    }
    
    public LayoutInflater getDropDownViewInflater() {
      LayoutInflater layoutInflater = this.mDropDownInflater;
      if (layoutInflater == null)
        layoutInflater = this.mInflater; 
      return layoutInflater;
    }
    
    public Resources.Theme getDropDownViewTheme() {
      Resources.Theme theme;
      LayoutInflater layoutInflater = this.mDropDownInflater;
      if (layoutInflater == null) {
        layoutInflater = null;
      } else {
        theme = layoutInflater.getContext().getTheme();
      } 
      return theme;
    }
    
    public void setDropDownViewTheme(Resources.Theme param1Theme) {
      if (param1Theme == null) {
        this.mDropDownInflater = null;
      } else if (param1Theme == this.mContext.getTheme()) {
        this.mDropDownInflater = this.mInflater;
      } else {
        this.mDropDownInflater = LayoutInflater.from((Context)new ContextThemeWrapper(this.mContext, param1Theme));
      } 
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\appcompat\widget\ThemedSpinnerAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */