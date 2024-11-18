package printscriptservice.controllers;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import printscriptservice.controller.TestController;
import printscriptservice.service.TestService;
import printscriptservice.utils.GlobalExceptionHandler;

public class TestControllerTest {

  private MockMvc mockMvc;

  @Mock
  private TestService testService;

  @InjectMocks
  private TestController testController;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    mockMvc = MockMvcBuilders.standaloneSetup(testController)
            .setControllerAdvice(new GlobalExceptionHandler())
            .build();
  }

  @Test
  public void testTestSuccess() throws Exception {
    String content = "println('Hello, World!')";
    String language = "printscript";
    String version = "1.1";
    String expectedResponse = "Test successful";
    String input = "input1,input2";
    String output = "output1,output2";

    when(testService.test(content, language, version, Arrays.asList(input.split(",")), Arrays.asList(output.split(","))))
            .thenReturn(expectedResponse);

    mockMvc.perform(post("/api/test")
                    .param("content", content)
                    .param("language", language)
                    .param("version", version)
                    .param("input", input)
                    .param("output", output)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED))
            .andExpect(status().isOk())
            .andExpect(content().string(expectedResponse));

    verify(testService, times(1)).test(content, language, version, Arrays.asList(input.split(",")), Arrays.asList(output.split(",")));
  }

  @Test
  public void testTestUnexpectedError() throws Exception {
    String content = "println('Hello, World!')";
    String language = "printscript";
    String version = "1.1";
    String input = "input1,input2";
    String output = "output1,output2";
    String errorMessage = "Unexpected error";

    when(testService.test(content, language, version, Arrays.asList(input.split(",")), Arrays.asList(output.split(","))))
            .thenThrow(new RuntimeException(errorMessage));

    mockMvc.perform(post("/api/test")
                    .param("content", content)
                    .param("language", language)
                    .param("version", version)
                    .param("input", input)
                    .param("output", output)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED))
            .andExpect(status().isInternalServerError())
            .andExpect(content().string("A runtime error occurred: " + errorMessage));

    verify(testService, times(1)).test(content, language, version, Arrays.asList(input.split(",")), Arrays.asList(output.split(",")));
  }
}