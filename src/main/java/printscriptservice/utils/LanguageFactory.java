package printscriptservice.utils;

public class LanguageFactory {
  public static Language getLanguage(String input) {
    String language = input.replace("\"", "");
    if (language.equals("PRINTSCRIPT")) {
      return new PrintScript();
    }
    throw new IllegalArgumentException("Language not supported: " + language);
  }
}
