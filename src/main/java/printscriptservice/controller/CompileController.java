package printscriptservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;
import printscriptservice.service.CompileService;

@RestController
@RequestMapping("/api/compile")
public class CompileController {

  @Autowired private CompileService compileService;

  @PostMapping
  public ResponseEntity<String> compile(
      @RequestParam("code") String code,
      @RequestParam("language") String language,
      @RequestParam("version") String version) {
    try {
      String result = compileService.compile(language, code, version);
      return ResponseEntity.ok(result);
    } catch (HttpServerErrorException ex) {
      String statusText = ex.getStatusText();
      System.out.println("Compilation error: " + statusText);
      return ResponseEntity.status(ex.getStatusCode()).body(statusText);
    } catch (Exception ex) {
      String errorMessage = "Unexpected error: " + ex.getMessage();
      System.out.println(errorMessage);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
    }
  }
}
