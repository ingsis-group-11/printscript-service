package printscriptservice.utils;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface Language {
  public String execute(MultipartFile code, String version) throws IOException;

  public String compile(String code, String version);

  public String analyze(String code, String rules, String version);

  public String format(String code, String rules, String outputPath, String version);
}
