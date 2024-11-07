package printscriptservice.dto;

import lombok.Getter;
import lombok.Setter;
import printscriptservice.utils.LintResult;

@Setter
@Getter
public class LintResultMessageDto {
  private String assetId;
  private LintResult linterResult;
}
