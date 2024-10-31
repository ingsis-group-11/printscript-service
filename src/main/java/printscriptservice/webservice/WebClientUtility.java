package printscriptservice.webservice;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class WebClientUtility {

  private final WebClient webClient;

  public WebClientUtility(WebClient.Builder webClientBuilder) {
    this.webClient = webClientBuilder.filter(authorizationHeaderFilter()).build();
  }

  private ExchangeFilterFunction authorizationHeaderFilter() {
    return ExchangeFilterFunction.ofRequestProcessor(
        clientRequest -> {
          String token = getCurrentToken();
          if (token != null) {
            clientRequest =
                ClientRequest.from(clientRequest)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .build();
          }
          return Mono.just(clientRequest);
        });
  }

  public <R> Mono<ResponseEntity<R>> getAsync(
      String url, ParameterizedTypeReference<R> responseType) {
    return webClient.get().uri(url).retrieve().toEntity(responseType);
  }

  public <T, R> Mono<ResponseEntity<R>> postAsync(String url, T body, Class<R> responseType) {
    return webClient.post().uri(url).bodyValue(body).retrieve().toEntity(responseType);
  }

  private String getCurrentToken() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
      return jwt.getTokenValue();
    }
    return null;
  }

  public <T> Mono<T> delete(String url, Class<T> responseEntityClass) {
    return webClient.delete().uri(url).retrieve().bodyToMono(responseEntityClass);
  }
}
