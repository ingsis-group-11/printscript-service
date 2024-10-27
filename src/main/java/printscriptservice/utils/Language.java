package printscriptservice.utils;

import java.util.List;
import printscriptservice.dto.RuleDto;

public interface Language {
  public String execute(String code, String version);

  public String compile(String code, String version);

  public String analyze(String code, List<RuleDto> rules, String version);

  public String format(String code, String rules, String outputPath, String version);
}
