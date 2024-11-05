package printscriptservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import printscriptservice.webservice.asset.AssetManager;
import printscriptservice.webservice.snippet.SnippetManager;

@Service
public class FormatService {

  @Autowired private AssetManager assetManager;

  @Autowired private SnippetManager snippetManager;

  private final String assetManagerContainer = "snippets";

  private final String rulesContainer = "format-rules";
  /*
   public String format(SnippetReceivedDto snippetReceivedDto) {
     String assetId = snippetReceivedDto.getAssetId();
     String userId = snippetReceivedDto.getUserId();
     InputStream content = assetManager.getAsset(assetManagerContainer, assetId);
     InputStream rules = assetManager.getRules(rulesContainer, userId);

     return LanguageFactory.getLanguage(snippetReceivedDto.getLanguage())
         .analyze(content, rules, snippetReceivedDto.getVersion());
   }

   public String format(String assetId) {
     ResponseEntity<SnippetReceivedDto> snippet = snippetManager.getSnippet(assetId);
     if (snippet.getStatusCode() != HttpStatus.OK) {
       return "Error: Could not retrieve snippet";
     }
     ResponseEntity<List<RuleDto>> rules = snippetManager.getRules();
     if (rules.getStatusCode() != HttpStatus.OK) {
       return "Error: Could not retrieve rules";
     }
     SnippetReceivedDto snippetReceived = snippet.getBody();
     assert snippetReceived != null;
     return "Not yet implemented";
     // format logic here
   }

  */
}
