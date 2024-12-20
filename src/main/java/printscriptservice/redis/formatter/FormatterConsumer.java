package printscriptservice.redis.formatter;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import org.austral.ingsis.redis.RedisStreamConsumer;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.stream.StreamReceiver;
import org.springframework.stereotype.Component;
import printscriptservice.dto.SnippetReceivedDto;
import printscriptservice.service.FormatService;

@Component
@Profile("!test")
public class FormatterConsumer extends RedisStreamConsumer<String> {

  @Autowired private FormatService formatService;

  private final ObjectMapper objectMapper = new ObjectMapper();

  public FormatterConsumer(
      @Value("${redis.consumer.formatter.queue}") @NotNull String streamKey,
      @Value("${redis.consumer.formatter.group}") @NotNull String consumerGroup,
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

      formatService.format(snippetReceivedDto);

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
