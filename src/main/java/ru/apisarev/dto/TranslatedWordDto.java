package ru.apisarev.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TranslatedWordDto {

    private Long id;

    private String word;

    private String translatedWord;

    private String originalLangCode;

    private String translatedLangCode;

    private Long translationId;

}
