package printscriptservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import printscriptservice.service.ExecuteService;

@RestController
@RequestMapping("/api/run")
public class ExecuteController {
  private final ExecuteService executeService;

  @Autowired
  public ExecuteController(ExecuteService executeService) {
    this.executeService = executeService;
  }

  @GetMapping()
  public ResponseEntity<String> execute(@RequestParam("assetId") String assetId) {
    return ResponseEntity.ok(executeService.execute(assetId));
  }
}
