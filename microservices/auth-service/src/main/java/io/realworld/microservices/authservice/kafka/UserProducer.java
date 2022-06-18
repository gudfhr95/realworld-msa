package io.realworld.microservices.authservice.kafka;

import static io.realworld.api.message.UserMessage.CREATE;
import static io.realworld.api.message.UserMessage.UPDATE;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.realworld.api.message.UserMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserProducer {

  private final KafkaTemplate<String, String> kafkaTemplate;

  private final ObjectMapper objectMapper;

  public void sendCreateMessage(UserMessage userMessage) throws JsonProcessingException {
    String message = objectMapper.writeValueAsString(userMessage);

    log.info("sendCreateMessage={}", message);

    kafkaTemplate.send(CREATE, message);
  }

  public void sendUpdateMessage(UserMessage userMessage) throws JsonProcessingException {
    String message = objectMapper.writeValueAsString(userMessage);

    log.info("sendUpdateMessage={}", message);

    kafkaTemplate.send(UPDATE, message);
  }
}
