package printscript_service.controller;

import org.springframework.web.bind.annotation.*;
import printscript_service.service.ExecuteService;

import java.io.IOException;

@RestController
@RequestMapping("/api/run")
public class ExecuteController {
    private final ExecuteService executeService;

    public ExecuteController(ExecuteService executeService) {
        this.executeService = executeService;
    }

    @GetMapping("/{language}")
    public String execute(@PathVariable String language, @RequestBody String code, @RequestParam(name = "version", required = false) String version) throws IOException {
        return executeService.execute(language, code, version);
    }
}
