package printscriptservice.webservice.snippet;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import printscriptservice.dto.RuleDto;
import printscriptservice.dto.SnippetReceivedDto;
import printscriptservice.webservice.WebClientUtility;

@Service
public class SnippetManager {

  @Autowired WebClientUtility webClientUtility;

  @Value("${snippet.manager.url}")
  private String snippetManagerUrl;

  public ResponseEntity<SnippetReceivedDto> getSnippet(String snippetId) {
    String url = snippetManagerUrl + "/api/snippet/" + snippetId;
    return webClientUtility
        .getAsync(url, new ParameterizedTypeReference<SnippetReceivedDto>() {})
        .block();
  }

  public ResponseEntity<List<RuleDto>> getRules() {
    String url = snippetManagerUrl + "/api/linting-rule";
    return webClientUtility
        .getAsync(url, new ParameterizedTypeReference<List<RuleDto>>() {})
        .block();
  }
}
