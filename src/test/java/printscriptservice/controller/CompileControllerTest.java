package printscriptservice.controller;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.HttpServerErrorException;
import printscriptservice.service.CompileService;
import printscriptservice.utils.GlobalExceptionHandler;

public class CompileControllerTest {

  private MockMvc mockMvc;

  @Mock private CompileService compileService;

  @InjectMocks private CompileController compileController;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    mockMvc =
        MockMvcBuilders.standaloneSetup(compileController)
            .setControllerAdvice(new GlobalExceptionHandler())
            .build();
  }

  @Test
  public void testCompileSuccess() throws Exception {
    String code = "println('Hello, World!');";
    String language = "printscript";
    String version = "1.1";
    String expectedResponse = "Compiled successfully";

    when(compileService.compile(language, code, version)).thenReturn(expectedResponse);

    mockMvc
        .perform(
            post("/api/compile")
                .param("code", code)
                .param("language", language)
                .param("version", version)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
        .andExpect(status().isOk())
        .andExpect(content().string(expectedResponse));

    verify(compileService, times(1)).compile(language, code, version);
  }

  @Test
  public void testCompileHttpServerError() throws Exception {
    String code = "println('Hello, World!')";
    String language = "printscript";
    String version = "1.1";
    String errorMessage = "Compilation error";

    doThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage))
        .when(compileService)
        .compile(language, code, version);

    mockMvc
        .perform(
            post("/api/compile")
                .param("code", code)
                .param("language", language)
                .param("version", version)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
        .andExpect(status().isInternalServerError())
        .andExpect(content().string("Compilation error"));

    verify(compileService, times(1)).compile(language, code, version);
  }

  @Test
  public void testCompileUnexpectedError() throws Exception {
    String code = "println('Hello, World!')";
    String language = "printscript";
    String version = "1.1";
    String errorMessage = "Unexpected error";

    doThrow(new RuntimeException(errorMessage))
        .when(compileService)
        .compile(language, code, version);

    mockMvc
        .perform(
            post("/api/compile")
                .param("code", code)
                .param("language", language)
                .param("version", version)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
        .andExpect(status().isInternalServerError())
        .andExpect(content().string("Unexpected error: " + errorMessage));

    verify(compileService, times(1)).compile(language, code, version);
  }
}
