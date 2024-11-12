package printscriptservice.redis;

import printscriptservice.utils.LintResult;

public interface ProducerInterface {
  void publishEvent(String assetId, LintResult lintResult);
}
