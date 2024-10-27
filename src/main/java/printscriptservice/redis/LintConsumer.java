package printscriptservice.redis;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.stream.StreamReceiver;
import org.springframework.stereotype.Component;
import printscriptservice.service.AnalyzeService;
import reactor.core.publisher.Flux;

@Component
public class LintConsumer {

  private final ReactiveRedisTemplate<String, String> redisTemplate;
  private final StreamReceiver<String, MapRecord<String, String, String>> streamReceiver;
  private final String streamKey;
  @Autowired private AnalyzeService analyzeService;

  public LintConsumer(
      @Value("${stream.key.lint}") String streamKey,
      ReactiveRedisTemplate<String, String> redisTemplate,
      StreamReceiver<String, MapRecord<String, String, String>> streamReceiver) {
    this.streamKey = streamKey;
    this.redisTemplate = redisTemplate;
    this.streamReceiver = streamReceiver;
  }

  @PostConstruct
  public void startConsuming() {
    Flux<MapRecord<String, String, String>> messageFlux =
        streamReceiver.receive(StreamOffset.fromStart(streamKey));

    messageFlux.doOnNext(this::processMessage).subscribe();
  }

  private void processMessage(MapRecord<String, String, String> message) {
    System.out.println("Received message from stream: " + streamKey);
    System.out.println("Message ID: " + message.getId());
    System.out.println("Message Body: " + message.getValue());

    analyzeService.analyze(message.getValue().get("payload"));

    // Acknowledge processing by printing or performing additional actions here
  }
}
