package printscriptservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import printscriptservice.service.FormatService;

@RestController
@RequestMapping("/api/format")
public class FormatController {

  @Autowired private FormatService formatService;

  @GetMapping("/{language}")
  public String format(
      @PathVariable String language,
      @RequestBody String code,
      @RequestBody String rules,
      @RequestBody String outputPath,
      @RequestParam(name = "version", required = false) String version) {
    return formatService.format(language, code, rules, outputPath, version);
  }
}
