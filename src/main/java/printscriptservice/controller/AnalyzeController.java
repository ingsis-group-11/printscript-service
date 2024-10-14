package printscriptservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import printscriptservice.service.AnalyzeService;

@RestController
@RequestMapping("/api/analyze")
public class AnalyzeController {

  @Autowired private AnalyzeService analyzeService;

  @GetMapping("/{language}")
  public String analyze(
      @PathVariable String language,
      @RequestBody String code,
      @RequestBody String rules,
      @RequestParam(name = "version", required = false) String version) {
    return analyzeService.analyze(language, code, rules, version);
  }
}
