package printscriptservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
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
import org.springframework.web.multipart.MultipartFile;
import printscriptservice.dto.SnippetReceivedDto;
import printscriptservice.utils.language.Language;
import printscriptservice.utils.language.LanguageFactory;
import printscriptservice.webservice.asset.AssetManager;

@ActiveProfiles("test")
@SpringBootTest
public class FormatServiceTest {

  @Mock private AssetManager assetManager;

  @Mock private Language language;

  @InjectMocks private FormatService formatService;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testFormatSnippetReceivedDtoSuccess() {
    String assetId = "asset123";
    String userId = "user123";
    String name = "snippet";
    String languageName = "PRINTSCRIPT";
    String version = "1.1";
    InputStream content = mock(InputStream.class);
    InputStream rules = mock(InputStream.class);
    String formattedContent = "formatted content";

    SnippetReceivedDto snippetReceivedDto = new SnippetReceivedDto();
    snippetReceivedDto.setAssetId(assetId);
    snippetReceivedDto.setUserId(userId);
    snippetReceivedDto.setName(name);
    snippetReceivedDto.setLanguage(languageName);
    snippetReceivedDto.setVersion(version);

    when(assetManager.getAsset("snippets", assetId)).thenReturn(content);
    when(assetManager.getRules("format-rules", userId)).thenReturn(rules);

    try (MockedStatic<LanguageFactory> mockedFactory = mockStatic(LanguageFactory.class)) {
      mockedFactory.when(() -> LanguageFactory.getLanguage(languageName)).thenReturn(language);
      when(language.format(content, rules, version)).thenReturn(formattedContent);

      String result = formatService.format(snippetReceivedDto);

      assertEquals(formattedContent, result);
      verify(assetManager, times(1)).getAsset("snippets", assetId);
      verify(assetManager, times(1)).getRules("format-rules", userId);
      verify(assetManager, times(1))
          .createAsset(eq("snippets"), eq(assetId), any(MultipartFile.class));
      verify(language, times(1)).format(content, rules, version);
    }
  }

  @Test
  public void testFormatStringSuccess() {
    String content = "{\"content\":\"println('Hello, World!')\"}";
    String userId = "user123";
    InputStream rules = mock(InputStream.class);
    String formattedContent = "formatted content";

    when(assetManager.getRules("format-rules", userId)).thenReturn(rules);

    try (MockedStatic<LanguageFactory> mockedFactory = mockStatic(LanguageFactory.class)) {
      mockedFactory.when(() -> LanguageFactory.getLanguage("printscript")).thenReturn(language);
      when(language.format("println('Hello, World!')", rules, "1.1")).thenReturn(formattedContent);

      String result = formatService.format(content, userId);

      assertEquals(formattedContent, result);
      verify(assetManager, times(1)).getRules("format-rules", userId);
      verify(language, times(1)).format("println('Hello, World!')", rules, "1.1");
    }
  }
}
