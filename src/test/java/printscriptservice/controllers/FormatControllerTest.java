package printscriptservice.controllers;


import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import printscriptservice.controller.FormatController;
import printscriptservice.service.FormatService;
import printscriptservice.utils.GlobalExceptionHandler;

public class FormatControllerTest {

  private MockMvc mockMvc;

  @Mock
  private FormatService formatService;

  @InjectMocks
  private FormatController formatController;

  @Mock
  private Authentication authentication;

  @Mock
  private Jwt jwt;

  @Mock
  private SecurityContext securityContext;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    mockMvc = MockMvcBuilders.standaloneSetup(formatController)
            .setControllerAdvice(new GlobalExceptionHandler())
            .build();
  }

  @Test
  public void testFormatSuccess() throws Exception {
    String content = "println('Hello, World!')";
    String userId = "user123";
    String expectedResponse = "formatted code";

    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.getPrincipal()).thenReturn(jwt);
    when(jwt.getClaimAsString("sub")).thenReturn(userId);
    when(formatService.format(content, userId)).thenReturn(expectedResponse);

    SecurityContextHolder.setContext(securityContext);

    mockMvc.perform(post("/api/format")
                    .content(content)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string(expectedResponse));

    verify(formatService, times(1)).format(content, userId);
  }

  @Test
  public void testFormatUnexpectedError() throws Exception {
    String content = "println('Hello, World!')";
    String userId = "user123";
    String errorMessage = "Unexpected error";

    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.getPrincipal()).thenReturn(jwt);
    when(jwt.getClaimAsString("sub")).thenReturn(userId);
    doThrow(new RuntimeException(errorMessage)).when(formatService).format(content, userId);

    SecurityContextHolder.setContext(securityContext);

    mockMvc.perform(post("/api/format")
                    .content(content)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError())
            .andExpect(content().string("A runtime error occurred: " + errorMessage));

    verify(formatService, times(1)).format(content, userId);
  }
}
