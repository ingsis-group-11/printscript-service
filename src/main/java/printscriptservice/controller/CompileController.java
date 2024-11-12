package printscriptservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import printscriptservice.service.CompileService;

@RestController
@RequestMapping("/api/compile")
public class CompileController {

  @Autowired public CompileService compileService;

  @PostMapping
  public ResponseEntity<String> compile(
      @RequestParam("code") String code,
      @RequestParam("language") String language,
      @RequestParam("version") String version) {
    return ResponseEntity.ok(compileService.compile(language, code, version));
  }
}
