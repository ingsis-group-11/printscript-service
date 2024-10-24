package printscriptservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import printscriptservice.service.CompileService;

@RestController
@RequestMapping("/api/compile")
public class CompileController {

  @Autowired public CompileService compileService;

  @PostMapping
  public String compile(
      @RequestBody String language,
      @RequestBody String code,
      @RequestParam(name = "version", required = false) String version) {
    return compileService.compile(language, code, version);
  }
}
