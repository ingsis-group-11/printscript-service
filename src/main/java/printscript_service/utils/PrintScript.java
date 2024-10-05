package printscript_service.utils;

import providers.outputprovider.FileWriter;
import providers.printprovider.TestPrintProvider;
import runner.FormatterRunner;
import runner.LinterRunner;
import runner.Runner;
import runner.ValidationRunner;
import java.io.FileInputStream;
import java.io.IOException;


public class PrintScript implements Language {

    @Override
    public String execute(String code, String version) throws IOException {
        Runner runner = new Runner();
        TestPrintProvider testPrintProvider = new TestPrintProvider();
        runner.run(new FileInputStream(code), version, testPrintProvider);
        StringBuilder output = new StringBuilder();
        while(testPrintProvider.getMessages().hasNext()) {
            output.append(testPrintProvider.getMessages().next());
        }
        return output.toString();
    }

    @Override
    public String compile(String code, String version) {
      ValidationRunner runner = new ValidationRunner();
      try {
        runner.validate(new FileInputStream(code), version);
      } catch (Exception e) {
        return e.getMessage();
      }
      return "Success";
    }

    @Override
    public String analyze(String code, String rules, String version) {
      LinterRunner runner = new LinterRunner();
      try {
        runner.linterRun(new FileInputStream(code), new FileInputStream(rules), version);
      } catch (Exception e) {
        return e.getMessage();
      }
      return "Success";
    }

    @Override
    public String format(String code, String rules, String outputPath, String version) {
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
