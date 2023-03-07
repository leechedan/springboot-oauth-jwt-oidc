package no.acntech.sandbox.service;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.UUID;

@Service
public class KeyService {

//    private static final String KEY_ID = UUID.randomUUID().toString();
    private final Resource privateKeyFile;
    private final Resource publicKeyFile;
    private final KeyFactory rsaKeyFactory;

    public KeyService(@Value("classpath:private.pem") final Resource privateKeyFile,
                      @Value("classpath:public.pem") final Resource publicKeyFile) throws Exception {
        this.privateKeyFile = privateKeyFile;
        this.publicKeyFile = publicKeyFile;
        this.rsaKeyFactory = KeyFactory.getInstance("RSA");
    }

    public RSAKey publicRsaKey(String kid) throws Exception {
        return new RSAKey.Builder(publicKey())
                .keyID(kid)
                .keyUse(KeyUse.SIGNATURE)
                .build();
    }

    public RSAKey privateRsaKey(String kid) throws Exception {
        return new RSAKey.Builder(publicKey())
                .privateKey(privateKey())
                .keyID(kid)
                .keyUse(KeyUse.SIGNATURE)
                .algorithm(JWSAlgorithm.RS256)
                .build();
    }

    private RSAPublicKey publicKey() throws Exception {
        byte[] publicKeyFileBytes = Files.readAllBytes(publicKeyFile.getFile().toPath());
        String publicKey = new String(publicKeyFileBytes)
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKey);
        return (RSAPublicKey) rsaKeyFactory.generatePublic(new X509EncodedKeySpec(publicKeyBytes));
    }

    private RSAPrivateKey privateKey() throws Exception {
        byte[] privateKeyFileBytes = Files.readAllBytes(privateKeyFile.getFile().toPath());
        String privateKey = new String(privateKeyFileBytes)
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");
        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKey);
        return (RSAPrivateKey) rsaKeyFactory.generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));

    }
}
