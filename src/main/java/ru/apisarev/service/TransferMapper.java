package ru.apisarev.service;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.apisarev.dto.TranslatedWordDto;
import ru.apisarev.entity.TranslatedWord;

import java.util.List;

@Mapper(componentModel = "cdi", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TransferMapper {

    @Mapping(target = "translationId", expression = "java(word.getTranslationId().getId())")
    TranslatedWordDto wordToDto(TranslatedWord word);

    List<TranslatedWordDto> wordsToDto(List<TranslatedWord> words);

}
