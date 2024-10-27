package printscriptservice.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpServerErrorException;
import printscriptservice.dto.RuleDto;
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
  public String analyze(String code, List<RuleDto> rules, String version) {
    if (version == null) {
      version = "1.1";
    }
    LinterRunner runner = new LinterRunner();
    try {
      InputStream rulesStream = listOfRulesDtoToInputStream(rules);
      runner.linterRun(new FileInputStream(code), rulesStream, version);
    } catch (Exception e) {
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

  private InputStream listOfRulesDtoToInputStream(List<RuleDto> rules) {
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      String jsonString = objectMapper.writeValueAsString(rules);
      return new ByteArrayInputStream(jsonString.getBytes(StandardCharsets.UTF_8));
    } catch (Exception e) {
      throw new RuntimeException("Failed to convert rules to JSON", e);
    }
  }
}
