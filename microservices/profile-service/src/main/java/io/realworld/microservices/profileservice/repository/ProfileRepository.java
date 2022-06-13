package io.realworld.microservices.profileservice.repository;

import io.realworld.microservices.profileservice.entity.Profile;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {

  Optional<Profile> findProfileByUsername(String username);
}
