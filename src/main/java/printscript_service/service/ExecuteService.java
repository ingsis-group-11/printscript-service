package printscript_service.service;

import org.springframework.stereotype.Service;
import printscript_service.utils.LanguageFactory;

import java.io.IOException;

@Service
public class ExecuteService {

    public String execute(String language, String code, String version) throws IOException {
        return LanguageFactory.getLanguage(language).execute(code, version);
    }
}
