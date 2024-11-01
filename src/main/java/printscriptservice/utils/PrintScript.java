package printscriptservice.utils;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpServerErrorException;
import providers.outputprovider.FileWriter;
import providers.printprovider.TestPrintProvider;
import runner.FormatterRunner;
import runner.LinterRunner;
import runner.Runner;
import runner.ValidationRunner;

public class PrintScript implements Language {

  @Override
  public String execute(String code, String version) {
    if (version == null) {
      version = "1.1";
    }
    Runner runner = new Runner();
    TestPrintProvider testPrintProvider = new TestPrintProvider();
    InputStream inputStream = new ByteArrayInputStream(code.getBytes(StandardCharsets.UTF_8));
    try {
      runner.run(inputStream, version, testPrintProvider);
      StringBuilder output = new StringBuilder();
      Iterator<String> messages = testPrintProvider.getMessages();
      while (messages.hasNext()) {
        output.append(messages.next());
      }
      return output.toString();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public String compile(String code, String version) {
    if (version == null) {
      version = "1.1";
    }
    ValidationRunner runner = new ValidationRunner();
    InputStream inputStream = new ByteArrayInputStream(code.getBytes(StandardCharsets.UTF_8));
    try {
      runner.validate(inputStream, version);
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (Exception e) {
      throw new HttpServerErrorException(HttpStatusCode.valueOf(500), e.getMessage());
    }

    return "Compiled successfully";
  }

  @Override
  public String analyze(InputStream code, InputStream rules, String version) {
    if (version == null) {
      version = "1.1";
    }
    LinterRunner runner = new LinterRunner();
    try {
      runner.linterRun(code, rules, version);
      System.out.println("after linting");
    } catch (Exception e) {
      System.out.println("inside catch");
      return e.getMessage();
    }
    return "Success";
  }

  @Override
  public String format(String code, String rules, String outputPath, String version) {
    if (version == null) {
      version = "1.1";
    }
    try {
      FormatterRunner runner = new FormatterRunner();
      FileInputStream codeStream = new FileInputStream(code);
      FileWriter fileWriter = new FileWriter(outputPath);
      runner.format(new FileInputStream(code), codeStream, fileWriter, version);
    } catch (Exception e) {
      return e.getMessage();
    }
    return "Success";
  }
}
