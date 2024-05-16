package androidx.dynamicanimation.animation;

import android.util.FloatProperty;

public abstract class FloatPropertyCompat<T> {
  final String mPropertyName;
  
  public FloatPropertyCompat(String paramString) {
    this.mPropertyName = paramString;
  }
  
  public static <T> FloatPropertyCompat<T> createFloatPropertyCompat(final FloatProperty<T> property) {
    return new FloatPropertyCompat<T>(property.getName()) {
        final FloatProperty val$property;
        
        public float getValue(T param1T) {
          return ((Float)property.get(param1T)).floatValue();
        }
        
        public void setValue(T param1T, float param1Float) {
          property.setValue(param1T, param1Float);
        }
      };
  }
  
  public abstract float getValue(T paramT);
  
  public abstract void setValue(T paramT, float paramFloat);
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\dynamicanimation\animation\FloatPropertyCompat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */