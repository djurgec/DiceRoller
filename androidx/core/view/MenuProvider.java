package androidx.core.view;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public interface MenuProvider {
  void onCreateMenu(Menu paramMenu, MenuInflater paramMenuInflater);
  
  boolean onMenuItemSelected(MenuItem paramMenuItem);
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\view\MenuProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */