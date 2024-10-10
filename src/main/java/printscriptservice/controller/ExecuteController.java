package printscriptservice.controller;

import java.io.IOException;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import printscriptservice.service.ExecuteService;

@RestController
@RequestMapping("/api/run")
public class ExecuteController {
  private final ExecuteService executeService;

  public ExecuteController(ExecuteService executeService) {
    this.executeService = executeService;
  }

  @PostMapping()
  public String execute (
      @RequestParam("code") MultipartFile code,
      @RequestParam("language") String language,
      @RequestParam("version") String version
  ) throws IOException {
    return executeService.execute(language, code, version);
  }
}
