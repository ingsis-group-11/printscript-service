package printscriptservice.webservice.snippet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import printscriptservice.dto.SnippetReceivedDTO;
import printscriptservice.webservice.WebClientUtility;
import reactor.core.publisher.Mono;

@Service
public class SnippetManager {

  @Autowired
  WebClientUtility webClientUtility;

  @Value("${snippet.manager.url}")
  private String snippetManagerUrl;

  public Mono<SnippetReceivedDTO> getSnippet(String snippetId) {
    String url = snippetManagerUrl + "/api/snippet/" + snippetId;
    return webClientUtility.getAsync(url, SnippetReceivedDTO.class).mapNotNull(HttpEntity::getBody);
  }
}