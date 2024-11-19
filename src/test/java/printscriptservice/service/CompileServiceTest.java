package printscriptservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import printscriptservice.utils.language.Language;
import printscriptservice.utils.language.LanguageFactory;

@ActiveProfiles("test")
@SpringBootTest
public class CompileServiceTest {

  @Mock private Language language;

  @InjectMocks private CompileService compileService;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testCompileSuccess() {
    String code = "println('Hello, World!')";
    String version = "1.1";
    String expectedOutput = "compiled code";

    try (MockedStatic<LanguageFactory> mockedFactory = mockStatic(LanguageFactory.class)) {
      mockedFactory.when(() -> LanguageFactory.getLanguage("PRINTSCRIPT")).thenReturn(language);
      when(language.compile(code, version)).thenReturn(expectedOutput);

      String result = compileService.compile("PRINTSCRIPT", code, version);

      assertEquals(expectedOutput, result);
      verify(language, times(1)).compile(code, version);
    }
  }
}
