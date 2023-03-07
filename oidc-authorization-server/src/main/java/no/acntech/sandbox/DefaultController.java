package no.acntech.sandbox;

import no.acntech.sandbox.domain.dto.TokenReq;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/uaa/oauth")
public class DefaultController {

    @PostMapping("/token")
    public Map<String, Object> get(TokenReq req) {
        Map<String, Object> map = new HashMap<>();
        OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, "String", Instant.now(), Instant.now().plusSeconds(3600));
        map.put("access_token", accessToken);
        OAuth2RefreshToken fresh = new OAuth2RefreshToken("freshToken", Instant.now().plusSeconds(3600));
        map.put("fresh_token", fresh);
        map.put("token_type", "BEARER");
        map.put("expires_in", Instant.now().plusSeconds(3600).getEpochSecond());
        map.put("scope", "profile");
        return map;
    }

    @GetMapping("/user")
    public Map<String, Object> getUser() {
        Map<String, Object> map = new HashMap<>();
        OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, "String", Instant.now(), Instant.now().plusSeconds(3600));
        map.put("access_token", accessToken);
        OAuth2RefreshToken fresh = new OAuth2RefreshToken("freshToken", Instant.now().plusSeconds(3600));
        map.put("fresh_token", fresh);
        map.put("token_type", "BEARER");
        map.put("expires_in", Instant.now().plusSeconds(3600).getEpochSecond());
        map.put("username", "admin");
        return map;
    }

}
