package printscriptservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import printscriptservice.service.TestService;

import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/api/test")
public class TestController {

  @Autowired
  private TestService testService;

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
