package com.scaler.userservicejune25.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendEmailEventDto {
    private String email;
    private String body;
    private String subject;
}
