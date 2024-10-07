package printscriptservice.service;

import org.springframework.stereotype.Service;
import printscriptservice.utils.LanguageFactory;

@Service
public class CompileService {

  public String compile(String language, String code, String version) {
    return LanguageFactory.getLanguage(language).compile(code, version);
  }
}
