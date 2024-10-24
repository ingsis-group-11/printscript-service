package printscriptservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
