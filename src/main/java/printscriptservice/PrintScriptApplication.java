package printscriptservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PrintScriptApplication {

  public static void main(String[] args) {

    SpringApplication.run(PrintScriptApplication.class, args);
  }
}
