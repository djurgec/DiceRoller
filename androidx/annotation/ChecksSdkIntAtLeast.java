package androidx.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.METHOD, ElementType.FIELD})
public @interface ChecksSdkIntAtLeast {
  int api() default -1;
  
  String codename() default "";
  
  int lambda() default -1;
  
  int parameter() default -1;
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\annotation\ChecksSdkIntAtLeast.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */