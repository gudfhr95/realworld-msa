package io.realworld.util.security;

import static com.nimbusds.jose.JWSAlgorithm.RS256;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import java.text.ParseException;
import java.util.Date;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class JwtUtils {

  private static final long EXPIRES_IN = 365L * 24 * 60 * 60 * 1000;

  public static String generateToken(String email, Long userId, RSAKey rsaJWK)
      throws JOSEException {
    JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
        .subject(email)
        .expirationTime(new Date(new Date().getTime() + EXPIRES_IN))
        .claim("user_id", userId)
        .build();

    SignedJWT signedJWT = new SignedJWT(
        new JWSHeader.Builder(RS256)
            .keyID(rsaJWK.getKeyID())
            .build(),
        claimsSet
    );

    JWSSigner signer = new RSASSASigner(rsaJWK);
    signedJWT.sign(signer);

    return signedJWT.serialize();
  }

  public static SignedJWT verify(String jwt, RSAKey rsaJWK) throws ParseException, JOSEException {
    SignedJWT signedJWT = SignedJWT.parse(jwt);

    JWSVerifier verifier = new RSASSAVerifier(rsaJWK.toPublicJWK());

    if (!signedJWT.verify(verifier)) {
      return null;
    }

    return signedJWT;
  }
}
