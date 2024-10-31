package printscriptservice.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.stream.StreamReceiver;
import org.springframework.data.redis.stream.StreamReceiver.StreamReceiverOptions;

@Configuration
public class RedisConfig {

  @Value("${redis.host}")
  private String redisHost;

  @Value("${redis.port}")
  private int redisPort;

  @Bean
  public LettuceConnectionFactory redisConnectionFactory() {
    RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(redisHost, redisPort);
    return new LettuceConnectionFactory(config);
  }

  @Bean
  public StreamReceiver<String, MapRecord<String, String, String>> streamReceiver(
      ReactiveRedisTemplate<String, String> redisTemplate) {
    StreamReceiverOptions<String, MapRecord<String, String, String>> options =
        StreamReceiverOptions.builder().pollTimeout(java.time.Duration.ofSeconds(1)).build();
    return StreamReceiver.create(redisTemplate.getConnectionFactory(), options);
  }
}
