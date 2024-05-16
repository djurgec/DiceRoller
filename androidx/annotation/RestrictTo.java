package androidx.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.FIELD, ElementType.PACKAGE})
public @interface RestrictTo {
  Scope[] value();
  
  public enum Scope {
    GROUP_ID, LIBRARY, LIBRARY_GROUP, LIBRARY_GROUP_PREFIX, SUBCLASSES, TESTS;
    
    private static final Scope[] $VALUES;
    
    static {
      Scope scope3 = new Scope("LIBRARY", 0);
      LIBRARY = scope3;
      Scope scope1 = new Scope("LIBRARY_GROUP", 1);
      LIBRARY_GROUP = scope1;
      Scope scope5 = new Scope("LIBRARY_GROUP_PREFIX", 2);
      LIBRARY_GROUP_PREFIX = scope5;
      Scope scope2 = new Scope("GROUP_ID", 3);
      GROUP_ID = scope2;
      Scope scope6 = new Scope("TESTS", 4);
      TESTS = scope6;
      Scope scope4 = new Scope("SUBCLASSES", 5);
      SUBCLASSES = scope4;
      $VALUES = new Scope[] { scope3, scope1, scope5, scope2, scope6, scope4 };
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\annotation\RestrictTo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */