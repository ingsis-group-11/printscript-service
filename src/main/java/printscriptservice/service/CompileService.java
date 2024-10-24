package printscriptservice.service;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import printscriptservice.utils.LanguageFactory;

import java.nio.charset.StandardCharsets;

@Service
public class CompileService {

  public String compile(String language, String code, String version) {

    return LanguageFactory.getLanguage(language).compile(code, version);
  }
}
