package io.realworld.microservices.authservice.kafka;


import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TestConsumer {

  @KafkaListener(topics = "test", groupId = "realworld.io")
  public void consume(String message) throws IOException {
    log.info("{}", message);
  }
}
