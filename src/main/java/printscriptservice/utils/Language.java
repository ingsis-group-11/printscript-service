package printscriptservice.utils;

import java.io.InputStream;

public interface Language {
  public String execute(String code, String version);

  public String compile(String code, String version);

  public String analyze(String assetId, InputStream code, InputStream rules, String version);

  public String format(InputStream code, InputStream rules, String version);

  public String format(String code, String version);
}
