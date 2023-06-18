package guru.qa.niffler.jupiter.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import guru.qa.niffler.jupiter.extension.GenerateSpendExtension;
import guru.qa.niffler.model.CurrencyValues;
import org.junit.jupiter.api.extension.ExtendWith;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ExtendWith(GenerateSpendExtension.class)
public @interface GenerateSpend {

  String description();

  String username();

  String category();

  double amount();

  CurrencyValues currency();
}
