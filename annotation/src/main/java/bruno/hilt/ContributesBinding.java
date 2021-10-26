package bruno.hilt;

import dagger.hilt.GeneratesRootInput;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
@GeneratesRootInput
public @interface ContributesBinding {
  Class<?> component();

  Class<?> boundType() default Object.class;
}
