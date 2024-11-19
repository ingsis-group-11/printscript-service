package printscriptservice.utils.language;

import java.io.InputStream;
import java.util.List;

public interface Language {
  public String execute(String code, String version);

  public String compile(String code, String version);

  public String analyze(String assetId, InputStream code, InputStream rules, String version);

  public String format(InputStream code, InputStream rules, String version);

  public String format(String code, InputStream rules, String version);

  public String test(
      String content, String language, String version, List<String> input, List<String> output);
}
