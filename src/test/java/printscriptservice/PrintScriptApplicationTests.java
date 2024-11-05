package printscriptservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class PrintScriptApplicationTests {

  @MockBean private RedisTemplate<String, Object> redisTemplate;

  @Test
  void contextLoads() {}
}
