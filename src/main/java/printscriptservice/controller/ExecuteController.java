package printscriptservice.controller;

import java.io.IOException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import printscriptservice.service.ExecuteService;

@RestController
@RequestMapping("/api/run")
public class ExecuteController {
  private final ExecuteService executeService;

  public ExecuteController(ExecuteService executeService) {
    this.executeService = executeService;
  }

  @GetMapping("/{language}")
  public String execute(
      @PathVariable String language,
      @RequestBody String code,
      @RequestParam(name = "version", required = false) String version)
      throws IOException {
    return executeService.execute(language, code, version);
  }
}
