package printscriptservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SnippetReceivedDto {
  private String assetId;
  private String userId;
  private String name;
  private String language;
  private String version;
  private String content;
  private String compliance;
  private String extension;
  private String author;
}
