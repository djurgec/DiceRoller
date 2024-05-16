package androidx.core.content.pm;

import java.util.List;

public abstract class ShortcutInfoChangeListener {
  public void onAllShortcutsRemoved() {}
  
  public void onShortcutAdded(List<ShortcutInfoCompat> paramList) {}
  
  public void onShortcutRemoved(List<String> paramList) {}
  
  public void onShortcutUpdated(List<ShortcutInfoCompat> paramList) {}
  
  public void onShortcutUsageReported(List<String> paramList) {}
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\content\pm\ShortcutInfoChangeListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */