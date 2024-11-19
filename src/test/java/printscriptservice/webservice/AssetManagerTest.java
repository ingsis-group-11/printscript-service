package printscriptservice.webservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import printscriptservice.webservice.asset.AssetManager;
import reactor.core.publisher.Mono;

public class AssetManagerTest {

  @Mock private WebClientUtility webClientUtility;

  @InjectMocks private AssetManager assetManager;

  private InputStream inputStream;

  @BeforeEach
  public void setUp() throws Exception {
    MockitoAnnotations.openMocks(this);
    inputStream = new ByteArrayInputStream("test data".getBytes());

    Field urlField = AssetManager.class.getDeclaredField("assetManagerUrl");
    urlField.setAccessible(true);
    urlField.set(assetManager, "http://asset-manager");
  }

  @Test
  public void testGetAsset() {
    String container = "snippets";
    String assetKey = "asset123";

    when(webClientUtility.getInputStream(anyString())).thenReturn(inputStream);

    InputStream result = assetManager.getAsset(container, assetKey);

    assertNotNull(result, "result must not be null");
    assertEquals(inputStream, result, "InputStream must match");
    verify(webClientUtility, times(1)).getInputStream(anyString());
  }

  @Test
  public void testGetRules() {
    String rulesContainer = "rules";
    String userId = "user123";
    when(webClientUtility.getInputStream(anyString())).thenReturn(inputStream);

    InputStream result = assetManager.getRules(rulesContainer, userId);

    assertNotNull(result);
    assertEquals(inputStream, result);
    verify(webClientUtility, times(1)).getInputStream(anyString());
  }

  @Test
  public void testCreateAsset() {
    // Arrange
    String container = "snippets";
    String assetKey = "asset123";
    MultipartFile content = mock(MultipartFile.class);
    String url = "http://asset-manager/v1/asset/" + container + "/" + assetKey;
    String expectedResponse = "Asset created successfully";

    when(webClientUtility.putFlux(any(), anyString(), eq(String.class)))
        .thenReturn(Mono.just(ResponseEntity.ok(expectedResponse)));

    ResponseEntity<String> result = assetManager.createAsset(container, assetKey, content);

    assertNotNull(result);
    assertEquals(HttpStatusCode.valueOf(200), result.getStatusCode());
    assertEquals(expectedResponse, result.getBody());

    verify(webClientUtility, times(1)).putFlux(any(), eq(url), eq(String.class));
  }
}
