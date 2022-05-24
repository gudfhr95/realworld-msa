package io.realworld.springcloud.authserver.security;

import static com.nimbusds.jose.JWSAlgorithm.RS256;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;

@RequiredArgsConstructor
public final class JwtUtils {

  private static final long EXPIRES_IN = 20 * 60;

  public static String generateToken(User user, RSAKey rsaJWK) throws JOSEException {
    JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
        .subject(user.getUsername())
        .expirationTime(new Date(new Date().getTime() + EXPIRES_IN))
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
}
