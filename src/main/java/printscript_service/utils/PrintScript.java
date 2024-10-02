package printscript_service.utils;

import providers.printprovider.TestPrintProvider;
import runner.Runner;

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
        return "";
    }

    @Override
    public String analyze(String code, String version) {
        return "";
    }

    @Override
    public String format(String code, String version) {
        return "";
    }
}
