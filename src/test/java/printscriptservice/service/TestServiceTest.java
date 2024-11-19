package printscriptservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
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
public class TestServiceTest {

  @Mock private Language language;

  @InjectMocks private TestService testService;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testTestSuccess() {
    String content = "println('Hello, World!')";
    String languageName = "PRINTSCRIPT";
    String version = "1.1";
    List<String> input = List.of("input1", "input2");
    List<String> output = List.of("output1", "output2");
    String expectedOutput = "test result";

    try (MockedStatic<LanguageFactory> mockedFactory = mockStatic(LanguageFactory.class)) {
      mockedFactory.when(() -> LanguageFactory.getLanguage(languageName)).thenReturn(language);
      when(language.test(content, languageName, version, input, output)).thenReturn(expectedOutput);

      String result = testService.test(content, languageName, version, input, output);

      assertEquals(expectedOutput, result);
      verify(language, times(1)).test(content, languageName, version, input, output);
    }
  }
}
