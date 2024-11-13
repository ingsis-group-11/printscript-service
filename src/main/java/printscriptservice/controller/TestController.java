package printscriptservice.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import printscriptservice.service.TestService;

@RestController
@RequestMapping("/api/test")
public class TestController {

  @Autowired private TestService testService;

  @PostMapping()
  public ResponseEntity<String> test(
      @RequestParam("content") String content,
      @RequestParam("language") String language,
      @RequestParam("version") String version,
      @RequestParam("inputs") List<String> input,
      @RequestParam("outputs") List<String> output) {
    return ResponseEntity.ok(testService.test(content, language, version, input, output));
  }
}
