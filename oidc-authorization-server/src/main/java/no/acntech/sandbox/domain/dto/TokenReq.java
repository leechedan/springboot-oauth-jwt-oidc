package no.acntech.sandbox.domain.dto;

import lombok.Data;

@Data
public class TokenReq {
    String grant_type;

    String code;

    String redirect_uri;

}
