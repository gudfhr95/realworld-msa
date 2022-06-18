package io.realworld.microservices.profileservice.kafka;

import static io.realworld.api.message.UserMessage.CREATE;
import static io.realworld.api.message.UserMessage.UPDATE;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.realworld.api.message.UserMessage;
import io.realworld.microservices.profileservice.entity.Profile;
import io.realworld.microservices.profileservice.mapper.ProfileMapper;
import io.realworld.microservices.profileservice.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserConsumer {

  private static final String GROUP_ID = "io.realworld";

  private final ObjectMapper objectMapper;
  private final ProfileMapper profileMapper;

  private final ProfileRepository profileRepository;

  @KafkaListener(topics = CREATE, groupId = GROUP_ID)
  public void consumeCreateMessage(String message) throws JsonProcessingException {
    UserMessage userMessage = objectMapper.readValue(message, UserMessage.class);

    Profile profile = profileMapper.userMessageToEntity(userMessage);

    profileRepository.save(profile);
  }

  @KafkaListener(topics = UPDATE, groupId = GROUP_ID)
  public void consumeUpdateMessage(String message) throws JsonProcessingException {
    UserMessage userMessage = objectMapper.readValue(message, UserMessage.class);

    Profile foundProfile = profileRepository.findProfileByUsername(userMessage.getUsername())
        .orElseThrow();

    Profile updatedProfile = profileMapper.userMessageToEntity(userMessage, foundProfile);

    profileRepository.save(updatedProfile);
  }
}
