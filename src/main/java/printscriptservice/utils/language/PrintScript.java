package printscriptservice.utils.language;

import iterator.FileReaderIterator;
import iterator.TokenIterator;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import parser.iterator.AstIterator;
import printscriptservice.redis.lint.LintProducer;
import printscriptservice.utils.LintResult;
import providers.inputprovider.TestInputProvider;
import providers.outputprovider.FileWriter;
import providers.printprovider.TestPrintProvider;
import runner.FormatterRunner;
import runner.LinterRunner;
import runner.Runner;

@Service
public class PrintScript implements Language {
  private final LintProducer lintProducer;

  @Autowired
  public PrintScript(LintProducer lintProducer) {
    this.lintProducer = lintProducer;
  }

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
    InputStream input = new ByteArrayInputStream(code.getBytes(StandardCharsets.UTF_8));
    try {
      FileReaderIterator fileIterator = new FileReaderIterator(input);
      TokenIterator tokens = new TokenIterator(fileIterator, version);
      AstIterator nodes = new AstIterator(tokens, version);
      while (nodes.hasNext()) {
        nodes.next();
      }
    } catch (IOException e) {
      throw new RuntimeException("I/O error during compilation: " + e.getMessage());
    } catch (Exception e) {
      throw new HttpServerErrorException(
          HttpStatus.INTERNAL_SERVER_ERROR, "Compilation error: " + e.getMessage());
    }
    return "Compiled successfully";
  }

  @Override
  public String analyze(String assetId, InputStream code, InputStream rules, String version) {
    if (version == null) {
      version = "1.1";
    }
    LinterRunner runner = new LinterRunner();
    try {
      runner.linterRun(code, rules, version);
    } catch (Exception e) {
      lintProducer.publishEvent(assetId, LintResult.FAILURE);
      return e.getMessage();
    }

    lintProducer.publishEvent(assetId, LintResult.SUCCESS);

    return "Success";
  }

  @Override
  public String format(InputStream code, InputStream rules, String version) {
    if (version == null) {
      version = "1.1";
    }
    try {
      FormatterRunner runner = new FormatterRunner();

      // Create a temporary file for output
      Path tempFile = Files.createTempFile("formatted_output", ".txt");

      // Use FileWriter with the temporary file path
      FileWriter fileWriter = new FileWriter(tempFile.toString());

      // Format the code using the provided runner
      runner.format(code, rules, fileWriter, version);

      // Read the content from the temporary file
      String formattedCode = Files.readString(tempFile);

      // Delete the temporary file
      Files.delete(tempFile);

      return formattedCode;

    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  @Override
  public String format(String code, InputStream rules, String version) {
    if (version == null) {
      version = "1.1";
    }
    try {
      FormatterRunner runner = new FormatterRunner();

      Path tempFile = Files.createTempFile("formatted_output", ".txt");

      FileWriter fileWriter = new FileWriter(tempFile.toString());

      InputStream codeStream = new ByteArrayInputStream(code.getBytes(StandardCharsets.UTF_8));

      runner.format(codeStream, rules, fileWriter, version);

      String formattedCode = Files.readString(tempFile);

      Files.delete(tempFile);

      return formattedCode;

    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  @Override
  public String test(
      String content, String language, String version, List<String> input, List<String> output) {
    if (version == null) {
      version = "1.1";
    }
    Runner runner = new Runner();
    TestPrintProvider testPrintProvider = new TestPrintProvider();
    TestInputProvider testInputProvider = new TestInputProvider(input);
    InputStream inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
    try {
      runner.run(inputStream, version, testPrintProvider, testInputProvider);

      Iterator<String> messages = testPrintProvider.getMessages();
      int i = 0;
      while (messages.hasNext()) {
        String message = messages.next();
        String expected = output.get(i) + "\n";
        if (!Objects.equals(expected, message)) {
          return "fail";
        }
        i++;
      }

      if (i != output.size()) {
        return "fail";
      }

      return "success";

    } catch (IOException e) {
      return "fail";
    }
  }
}
