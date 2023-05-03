package niffler.jupiter.annotation;

import niffler.jupiter.extension.GenerateUserHibernateExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ExtendWith(GenerateUserHibernateExtension.class)
public @interface GenerateUserHibernate {

    String username() default "";

    String password() default "";
}
