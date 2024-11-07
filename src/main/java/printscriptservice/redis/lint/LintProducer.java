package printscriptservice.redis.lint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;
import printscriptservice.dto.LintResultMessageDto;
import printscriptservice.redis.ProducerInterface;
import printscriptservice.redis.config.RedisStreamProducer;
import printscriptservice.utils.LintResult;

@Component
public class LintProducer extends RedisStreamProducer implements ProducerInterface {

  @Autowired
  public LintProducer(
      @Value("${redis.producer.lint}") String streamKey,
      ReactiveRedisTemplate<String, String> redis) {
    super(streamKey, redis);
  }

  @Override
  public void publishEvent(String assetId, LintResult lintResult) {
    System.out.println("Publishing on stream: " + getStreamKey());
    LintResultMessageDto message = new LintResultMessageDto();
    message.setAssetId(assetId);
    message.setLinterResult(lintResult);
    emit(message).subscribe();
  }
}
