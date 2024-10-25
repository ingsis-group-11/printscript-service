package printscriptservice.service;

import java.io.IOException;
import org.springframework.stereotype.Service;
import printscriptservice.utils.LanguageFactory;

@Service
public class ExecuteService {

  public String execute(String language, String code, String version) throws IOException {
    return LanguageFactory.getLanguage(language).execute(code, version);
  }
}
