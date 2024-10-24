package printscriptservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class SnippetReceivedDTO {
  private String assetId;
  private String language;
  private String version;
  private String content;
}