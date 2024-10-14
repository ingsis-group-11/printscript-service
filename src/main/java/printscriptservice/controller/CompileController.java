package printscriptservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import printscriptservice.service.CompileService;

@RestController
@RequestMapping("/api/compile")
public class CompileController {

  @Autowired public CompileService compileService;

  @GetMapping("/{language}")
  public String compile(
      @PathVariable String language,
      @RequestBody String code,
      @RequestParam(name = "version", required = false) String version) {
    return compileService.compile(language, code, version);
  }
}
