package printscriptservice.service;

import java.util.List;
import org.springframework.stereotype.Service;
import printscriptservice.utils.LanguageFactory;

@Service
public class TestService {
  public String test(
      String content, String language, String version, List<String> input, List<String> output) {
    return LanguageFactory.getLanguage(language).test(content, language, version, input, output);
  }
}
