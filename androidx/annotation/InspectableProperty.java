package androidx.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Deprecated
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.METHOD})
public @interface InspectableProperty {
  int attributeId() default 0;
  
  EnumEntry[] enumMapping() default {};
  
  FlagEntry[] flagMapping() default {};
  
  boolean hasAttributeId() default true;
  
  String name() default "";
  
  ValueType valueType() default ValueType.INFERRED;
  
  @Retention(RetentionPolicy.SOURCE)
  @Target({ElementType.TYPE})
  public static @interface EnumEntry {
    String name();
    
    int value();
  }
  
  @Retention(RetentionPolicy.SOURCE)
  @Target({ElementType.TYPE})
  public static @interface FlagEntry {
    int mask() default 0;
    
    String name();
    
    int target();
  }
  
  public enum ValueType {
    COLOR, GRAVITY, INFERRED, INT_ENUM, INT_FLAG, NONE, RESOURCE_ID;
    
    private static final ValueType[] $VALUES;
    
    static {
      ValueType valueType1 = new ValueType("NONE", 0);
      NONE = valueType1;
      ValueType valueType4 = new ValueType("INFERRED", 1);
      INFERRED = valueType4;
      ValueType valueType6 = new ValueType("INT_ENUM", 2);
      INT_ENUM = valueType6;
      ValueType valueType2 = new ValueType("INT_FLAG", 3);
      INT_FLAG = valueType2;
      ValueType valueType3 = new ValueType("COLOR", 4);
      COLOR = valueType3;
      ValueType valueType7 = new ValueType("GRAVITY", 5);
      GRAVITY = valueType7;
      ValueType valueType5 = new ValueType("RESOURCE_ID", 6);
      RESOURCE_ID = valueType5;
      $VALUES = new ValueType[] { valueType1, valueType4, valueType6, valueType2, valueType3, valueType7, valueType5 };
    }
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\annotation\InspectableProperty.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */