package com.scaler.userservicejune25.dtos;

import com.scaler.userservicejune25.models.Token;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class TokenDto {
    private String tokenValue;
    private Date expiryDate;

    public static TokenDto from(Token token) {
        if (token == null) {
            return null;
        }

        TokenDto tokenDto = new TokenDto();
        tokenDto.setTokenValue(token.getValue());
        tokenDto.setExpiryDate(token.getExpiryAt());

        return tokenDto;
    }
}
