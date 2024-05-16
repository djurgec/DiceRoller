package androidx.resourceinspection.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.METHOD})
public @interface Attribute {
  IntMap[] intMapping() default {};
  
  String value();
  
  @Retention(RetentionPolicy.SOURCE)
  @Target({})
  public static @interface IntMap {
    int mask() default 0;
    
    String name();
    
    int value();
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\resourceinspection\annotation\Attribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */