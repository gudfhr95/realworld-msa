package io.realworld.microservices.authservice.controller;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import io.realworld.microservices.authservice.kafka.TestProducer;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/.well-known")
@RequiredArgsConstructor
@Slf4j
public class JwkController {

  private final RSAKey rsaJwk;
  private final TestProducer testProducer;

  @GetMapping("/jwks.json")
  public Map<String, Object> getRsaJwk() {
    testProducer.sendMessage("tttttt");
    return new JWKSet(rsaJwk.toPublicJWK()).toJSONObject();
  }
}
