package printscriptservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import printscriptservice.service.AnalyzeService;

@RestController
@RequestMapping("/api/analyze")
public class AnalyzeController {

  @Autowired private AnalyzeService analyzeService;

  @GetMapping("/{assetId}")
  public ResponseEntity<String> analyze(@PathVariable String assetId) {
    return ResponseEntity.ok(analyzeService.analyze(assetId));
  }
}
