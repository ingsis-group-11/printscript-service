package printscriptservice.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import printscriptservice.redis.lint.LintProducer;

@Component
public class LanguageFactory {

  private static ApplicationContext context;

  @Autowired
  public LanguageFactory(ApplicationContext context) {
    LanguageFactory.context = context;
  }

  public static Language getLanguage(String input) {
    String language = input.replace("\"", "");
    if (language.equalsIgnoreCase("PRINTSCRIPT")) {
      LintProducer lintProducer = context.getBean(LintProducer.class);
      return new PrintScript(lintProducer);
    }
    throw new IllegalArgumentException("Language not supported: " + language);
  }
}
