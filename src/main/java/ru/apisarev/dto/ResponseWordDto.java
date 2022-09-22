package ru.apisarev.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResponseWordDto {
    private String text;
    private String detectedLanguageCode;
}

