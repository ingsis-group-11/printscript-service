package printscriptservice.webservice.asset;

import java.io.InputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import printscriptservice.webservice.WebClientUtility;

@Component
public class AssetManager {
  @Autowired WebClientUtility webClientUtility;

  @Value("${asset.manager.url}")
  private String assetManagerUrl;

  public InputStream getAsset(String container, String assetKey) {
    String url = assetManagerUrl + "/v1/asset/" + container + "/" + assetKey;
    return webClientUtility.getInputStream(url);
  }

  public InputStream getRules(String rulesContainer, String userId) {
    String url = assetManagerUrl + "/v1/asset/" + rulesContainer + "/" + userId;
    return webClientUtility.getInputStream(url);
  }
}
