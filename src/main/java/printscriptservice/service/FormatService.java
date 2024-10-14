package printscriptservice.service;

import org.springframework.stereotype.Service;
import printscriptservice.utils.LanguageFactory;

@Service
public class FormatService {

  public String format(
      String language, String code, String rules, String outputPath, String version) {
    return LanguageFactory.getLanguage(language).format(code, rules, outputPath, version);
  }
}
