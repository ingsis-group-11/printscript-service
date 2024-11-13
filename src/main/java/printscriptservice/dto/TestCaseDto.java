package printscriptservice.dto;

import java.io.InputStream;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class TestCaseDto {
  private InputStream content;
  private String language;
  private String version;
  private List<String> input;
  private List<String> output;
}
