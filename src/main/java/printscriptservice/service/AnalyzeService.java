package printscriptservice.service;

import org.springframework.stereotype.Service;
import printscriptservice.utils.LanguageFactory;

@Service
public class AnalyzeService {

  public String analyze(String language, String code, String version, String rules) {
    return LanguageFactory.getLanguage(language).analyze(code, rules, version);
  }
}
