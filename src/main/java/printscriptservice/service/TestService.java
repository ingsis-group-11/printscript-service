package printscriptservice.service;

import org.springframework.stereotype.Service;
import printscriptservice.utils.LanguageFactory;

import java.io.InputStream;
import java.util.List;

@Service
public class TestService {
  public String test (String content, String language, String version, List<String> input, List<String> output) {
    return LanguageFactory.getLanguage(language).test(content, language, version, input, output);
  }
}
