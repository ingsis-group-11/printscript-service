package printscriptservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.InputStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import printscriptservice.dto.SnippetReceivedDto;
import printscriptservice.redis.lint.LintProducer;
import printscriptservice.utils.LintResult;
import printscriptservice.utils.language.Language;
import printscriptservice.utils.language.LanguageFactory;
import printscriptservice.webservice.asset.AssetManager;

@ActiveProfiles("test")
@SpringBootTest
public class AnalyzeServiceTest {

  @Mock private AssetManager assetManager;

  @Mock private LintProducer lintProducer;

  @Mock private Language language;

  @InjectMocks private AnalyzeService analyzeService;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testAnalyzeSuccess() {
    String assetId = "asset123";
    String userId = "user123";
    String languageName = "PRINTSCRIPT";
    String version = "1.1";
    InputStream content = mock(InputStream.class);
    InputStream rules = mock(InputStream.class);
    String expectedOutput = "analysis result";

    SnippetReceivedDto snippetReceivedDto = new SnippetReceivedDto();
    snippetReceivedDto.setAssetId(assetId);
    snippetReceivedDto.setUserId(userId);
    snippetReceivedDto.setLanguage(languageName);
    snippetReceivedDto.setVersion(version);

    when(assetManager.getAsset("snippets", assetId)).thenReturn(content);
    when(assetManager.getRules("lint-rules", userId)).thenReturn(rules);

    try (MockedStatic<LanguageFactory> mockedFactory = mockStatic(LanguageFactory.class)) {
      mockedFactory.when(() -> LanguageFactory.getLanguage(languageName)).thenReturn(language);
      when(language.analyze(assetId, content, rules, version)).thenReturn(expectedOutput);

      String result = analyzeService.analyze(snippetReceivedDto);

      assertEquals(expectedOutput, result);
      verify(assetManager, times(1)).getAsset("snippets", assetId);
      verify(assetManager, times(1)).getRules("lint-rules", userId);
      verify(lintProducer, times(1)).publishEvent(assetId, LintResult.IN_PROGRESS);
      verify(language, times(1)).analyze(assetId, content, rules, version);
    }
  }
}
