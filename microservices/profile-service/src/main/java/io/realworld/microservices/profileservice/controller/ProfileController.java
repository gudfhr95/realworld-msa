package io.realworld.microservices.profileservice.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ProfileController {

  @GetMapping("/profiles")
  public String getProfile() {
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    return null;
  }
}
