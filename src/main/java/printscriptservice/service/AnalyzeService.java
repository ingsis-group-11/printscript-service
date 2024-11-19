package printscriptservice.service;

import java.io.InputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import printscriptservice.dto.SnippetReceivedDto;
import printscriptservice.redis.lint.LintProducer;
import printscriptservice.utils.LintResult;
import printscriptservice.utils.language.LanguageFactory;
import printscriptservice.webservice.asset.AssetManager;

@Service
public class AnalyzeService {

  @Autowired private AssetManager assetManager;

  @Autowired private LintProducer lintProducer;

  private final String assetManagerContainer = "snippets";

  private final String rulesContainer = "lint-rules";

  public String analyze(SnippetReceivedDto snippetReceivedDto) {
    String assetId = snippetReceivedDto.getAssetId();
    String userId = snippetReceivedDto.getUserId();
    InputStream content = assetManager.getAsset(assetManagerContainer, assetId);
    InputStream rules = assetManager.getRules(rulesContainer, userId);
    lintProducer.publishEvent(assetId, LintResult.IN_PROGRESS);
    return LanguageFactory.getLanguage(snippetReceivedDto.getLanguage())
        .analyze(assetId, content, rules, snippetReceivedDto.getVersion());
  }
}
