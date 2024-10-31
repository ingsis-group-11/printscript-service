package printscriptservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class SnippetReceivedDto {
  private String assetId;
  private String userId;
  private String language;
  private String version;
  private String content;
}
