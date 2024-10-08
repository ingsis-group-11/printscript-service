package printscript_service.utils;

import java.io.IOException;

public interface Language {
    public String execute(String code, String version) throws IOException;
    public String compile(String code, String version);
    public String analyze(String code, String version);
    public String format(String code, String version);
}
