package no.acntech.sandbox.resource;

import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.UUID;

import no.acntech.sandbox.domain.Jwt;
import no.acntech.sandbox.service.KeyService;

@RequestMapping(path = "token")
@RestController
public class JwtResource {

    private final KeyService keyService;

    public JwtResource(final KeyService keyService) {
        this.keyService = keyService;
    }

    @GetMapping
    public ResponseEntity<Jwt> getJwt(@RequestParam(defaultValue = "a1309869-c698-4cd2-8aae-a5ce7050feb6", required = false) String kid) throws Exception {
        RSAKey senderJWK = keyService.privateRsaKey(kid);

        RSASSASigner signer = new RSASSASigner(senderJWK);

        JWSHeader jwsHeader = new JWSHeader.Builder(JWSAlgorithm.RS256)
                .contentType("JWT")
                .type(JOSEObjectType.JWT)
                .keyID(kid)
                .build();

        JWTClaimsSet jwtClaims = new JWTClaimsSet.Builder()
                .subject("acntech")
                .issueTime(new Date())
                .issuer("http://localhost:8000")
                .build();

        SignedJWT signedJWT = new SignedJWT(jwsHeader, jwtClaims);
        signedJWT.sign(signer);
        System.out.println("" + UUID.randomUUID());
        Jwt jwt = new Jwt();
        jwt.setJwt(signedJWT.serialize());
        return ResponseEntity.ok(jwt);
    }

    @PostMapping
    public ResponseEntity<Jwt> postJwt(@RequestBody @Valid Jwt jwt, @RequestParam String kid) throws Exception {
        RSAKey recipientJWK = keyService.publicRsaKey(kid);

        JWSVerifier verifier = new RSASSAVerifier(recipientJWK);

        SignedJWT signedJWT = SignedJWT.parse(jwt.getJwt());

        jwt.setVerified(signedJWT.verify(verifier));

        return ResponseEntity.ok(jwt);
    }
}
