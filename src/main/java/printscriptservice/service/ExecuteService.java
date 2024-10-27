package printscriptservice.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import printscriptservice.dto.RuleDto;
import printscriptservice.dto.SnippetReceivedDto;
import printscriptservice.utils.LanguageFactory;
import printscriptservice.webservice.snippet.SnippetManager;

@Service
public class ExecuteService {

  @Autowired private SnippetManager snippetManager;

  public String execute(String assetId) {
    ResponseEntity<SnippetReceivedDto> snippet = snippetManager.getSnippet(assetId);
    if (snippet.getStatusCode() != HttpStatus.OK) {
      return "Error: Could not retrieve snippet";
    }
    ResponseEntity<List<RuleDto>> rules = snippetManager.getRules();
    if (rules.getStatusCode() != HttpStatus.OK) {
      return "Error: Could not retrieve rules";
    }
    SnippetReceivedDto snippetReceivedDto = snippet.getBody();
    assert snippetReceivedDto != null;
    return LanguageFactory.getLanguage(snippetReceivedDto.getLanguage())
        .execute(snippetReceivedDto.getContent(), snippetReceivedDto.getVersion());
  }
}
