package androidx.core.content.pm;

import java.util.ArrayList;
import java.util.List;

public abstract class ShortcutInfoCompatSaver<T> {
  public abstract T addShortcuts(List<ShortcutInfoCompat> paramList);
  
  public List<ShortcutInfoCompat> getShortcuts() throws Exception {
    return new ArrayList<>();
  }
  
  public abstract T removeAllShortcuts();
  
  public abstract T removeShortcuts(List<String> paramList);
  
  public static class NoopImpl extends ShortcutInfoCompatSaver<Void> {
    public Void addShortcuts(List<ShortcutInfoCompat> param1List) {
      return null;
    }
    
    public Void removeAllShortcuts() {
      return null;
    }
    
    public Void removeShortcuts(List<String> param1List) {
      return null;
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\content\pm\ShortcutInfoCompatSaver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */