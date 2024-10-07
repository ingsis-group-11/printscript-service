package printscriptservice.utils;

public class LanguageFactory {
  public static Language getLanguage(String language) {
    if (language.equals("printScript")) {
      return new PrintScript();
    }
    throw new IllegalArgumentException("Language not supported");
  }
}
