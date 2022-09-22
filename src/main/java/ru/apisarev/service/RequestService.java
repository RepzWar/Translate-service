package ru.apisarev.service;

import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.smallrye.mutiny.Uni;
import ru.apisarev.dto.RequestDto;
import ru.apisarev.dto.RequestFormattedDto;
import ru.apisarev.dto.ResultDto;
import ru.apisarev.entity.TranslateRequest;
import ru.apisarev.repository.RequestRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class RequestService {

    @Inject
    protected RequestRepository repository;

    @Inject
    protected TranslationService translationService;

    @Inject
    protected RequestDetails requestDetails;

    public Uni<List<TranslateRequest>> findAll() {
        return repository.findAll().list();
    }

    public Uni<ResultDto> translate(RequestDto request) {

        RequestFormattedDto requestFormatted = new RequestFormattedDto();
        requestFormatted.setTargetLanguageCode(request.getTargetLanguageCode());
        requestFormatted.setTexts(List.of(request.getText().split(" ")));

        TranslateRequest translateRequest = TranslateRequest.builder().request(request.getText())
                .time(LocalDateTime.now())
                .remoteAddress(requestDetails.getRemoteAddress())
                .targetLangCode(request.getTargetLanguageCode())
                .build();

        Uni<String> result = translationService.translate(requestFormatted, translateRequest);
        return saveRequest(result, translateRequest).replaceWith(result).onItem().transform(x->
                ResultDto.builder().translation(x).build());
    }

    @ReactiveTransactional
    protected Uni<TranslateRequest> saveRequest(Uni<String> result, TranslateRequest request) {
        return result.onItem().transform(x -> {
                    request.setResult(x);
                    return request;
                })
                .onItem().transformToUni(x -> repository.persist(x));
    }

}