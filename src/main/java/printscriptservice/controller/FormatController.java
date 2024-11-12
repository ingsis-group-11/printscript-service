package printscriptservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import printscriptservice.service.FormatService;

@RestController
@RequestMapping("/api/format")
public class FormatController {

  @Autowired private FormatService formatService;

  @PostMapping()
  public ResponseEntity<String> format(@RequestBody String content) {
    return ResponseEntity.ok(formatService.format(content));
  }
}
