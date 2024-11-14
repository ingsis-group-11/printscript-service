package printscriptservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import printscriptservice.dto.SnippetReceivedDto;
import printscriptservice.utils.LanguageFactory;
import printscriptservice.utils.StringToMultipartFile;
import printscriptservice.webservice.asset.AssetManager;

@Service
public class FormatService {

  @Autowired private AssetManager assetManager;

  private final String assetManagerContainer = "snippets";

  private final String rulesContainer = "format-rules";

  public String format(SnippetReceivedDto snippetReceivedDto) {
    String assetId = snippetReceivedDto.getAssetId();
    String userId = snippetReceivedDto.getUserId();
    String name = snippetReceivedDto.getName();
    InputStream content = assetManager.getAsset(assetManagerContainer, assetId);
    InputStream rules = assetManager.getRules(rulesContainer, userId);
    String formattedContent =
        LanguageFactory.getLanguage(snippetReceivedDto.getLanguage())
            .format(content, rules, snippetReceivedDto.getVersion());
    MultipartFile file = new StringToMultipartFile(formattedContent, name, name, "text/plain");
    assetManager.createAsset(assetManagerContainer, assetId, file);
    return formattedContent;
  }

  public String format(String content, String userId) {
    InputStream rules = assetManager.getRules(rulesContainer, userId);
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      Map<String, String> map = objectMapper.readValue(content, Map.class);
      content = map.getOrDefault("content", "");
    } catch (Exception e) {
      e.printStackTrace();
    }

    return LanguageFactory.getLanguage("printscript").format(content, rules, "1.1");
  }
}
