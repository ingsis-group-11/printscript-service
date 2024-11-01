package printscriptservice.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import org.austral.ingsis.redis.RedisStreamConsumer;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.stream.StreamReceiver;
import org.springframework.stereotype.Component;
import printscriptservice.dto.SnippetReceivedDto;
import printscriptservice.service.AnalyzeService;

@Component
public class LintConsumer extends RedisStreamConsumer<String> {

  @Autowired private AnalyzeService analyzeService;

  private final ObjectMapper objectMapper = new ObjectMapper();

  public LintConsumer(
      @Value("${stream.key.lint}") @NotNull String streamKey,
      @Value("${stream.group.lint}") @NotNull String consumerGroup,
      @NotNull RedisTemplate<String, String> redis) {
    super(streamKey, consumerGroup, redis);
  }

  @Override
  protected void onMessage(@NotNull ObjectRecord<String, String> objectRecord) {
    try {
      String jsonValue = objectRecord.getValue();
      SnippetReceivedDto snippetReceivedDto =
          objectMapper.readValue(jsonValue, SnippetReceivedDto.class);

      // Now you can access each field of snippetReceivedDto
      System.out.println("Asset ID: " + snippetReceivedDto.getAssetId());
      System.out.println("Language: " + snippetReceivedDto.getLanguage());
      System.out.println("Version: " + snippetReceivedDto.getVersion());
      System.out.println("User ID: " + snippetReceivedDto.getUserId());

      analyzeService.analyze(snippetReceivedDto);

    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @NotNull
  @Override
  protected StreamReceiver.StreamReceiverOptions<String, ObjectRecord<String, String>> options() {
    return StreamReceiver.StreamReceiverOptions.builder()
        .pollTimeout(Duration.ofSeconds(5))
        .targetType(String.class)
        .build();
  }
}