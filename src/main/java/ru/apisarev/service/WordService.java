package ru.apisarev.service;

import io.smallrye.mutiny.Uni;
import ru.apisarev.dto.TranslatedWordDto;
import ru.apisarev.repository.TranslatedWordRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

@ApplicationScoped
public class WordService {

    @Inject
    protected TranslatedWordRepository repository;

    @Inject
    protected TransferMapper mapper;

    public Uni<List<TranslatedWordDto>> findAll() {
        return repository.findAll().list().onItem().transform(x -> mapper.wordsToDto(x));
    }

}
