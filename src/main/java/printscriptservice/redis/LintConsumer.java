package printscriptservice.redis;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.stream.StreamReceiver;
import org.springframework.stereotype.Component;
import printscriptservice.dto.SnippetReceivedDto;
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

    String messageBody = message.getValue().get("payload");

    try {
      ObjectMapper objectMapper = new ObjectMapper();
      JsonNode jsonNode = objectMapper.readTree(messageBody);

      String assetId = jsonNode.get("assetId").asText();
      String language = jsonNode.get("language").asText();
      String version = jsonNode.get("version").asText();
      String content = jsonNode.get("content").asText();
      String userId = jsonNode.get("userId").asText();

      SnippetReceivedDto snippetReceivedDto = SnippetReceivedDto.builder()
          .assetId(assetId)
          .language(language)
          .version(version)
          .content(content)
          .userId(userId)
          .build();

      //analyzeService.analyze(snippetReceivedDto);
      System.out.println(snippetReceivedDto);

    } catch (Exception e) {
      throw new RuntimeException("Error processing message", e);
    }

  }
}
