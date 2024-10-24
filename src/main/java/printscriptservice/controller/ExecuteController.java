package printscriptservice.controller;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import printscriptservice.service.ExecuteService;
import printscriptservice.webservice.snippet.SnippetManager;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/run")
public class ExecuteController {
  private final ExecuteService executeService;
  private final SnippetManager snippetManager;

  @Autowired
  public ExecuteController(ExecuteService executeService, SnippetManager snippetManager) {
    this.executeService = executeService;
    this.snippetManager = snippetManager;
  }

  @PostMapping()
  public Mono<String> execute(
      @RequestParam("snippetId") String snippetId) {
    return snippetManager.getSnippet(snippetId)
        .handle((snippet, sink) -> {
          try {
            sink.next(executeService.execute(snippet.getLanguage(), snippet.getContent(), snippet.getVersion()));
          } catch (IOException e) {
            sink.error(new RuntimeException(e));
          }
        });
  }
}