package printscriptservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import printscriptservice.service.FormatService;

@RestController
@RequestMapping("/api/format")
public class FormatController {

  @Autowired private FormatService formatService;

  @GetMapping("/{assetId}")
  public ResponseEntity<String> format(@PathVariable String assetId) {
    return ResponseEntity.ok(formatService.format(assetId));
  }
}
