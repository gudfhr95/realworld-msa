package io.realworld.microservices.profileservice.security;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.SignedJWT;
import java.text.ParseException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class JwtUtils {

  public static SignedJWT verify(String jwt, RSAKey rsaJWK) throws ParseException, JOSEException {
    SignedJWT signedJWT = SignedJWT.parse(jwt);

    JWSVerifier verifier = new RSASSAVerifier(rsaJWK.toPublicJWK());

    if (!signedJWT.verify(verifier)) {
      return null;
    }

    return signedJWT;
  }
}
