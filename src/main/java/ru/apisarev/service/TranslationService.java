package ru.apisarev.service;

import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.rest.client.RestClientBuilder;
import ru.apisarev.client.TranslationClient;
import ru.apisarev.dto.RequestFormattedDto;
import ru.apisarev.dto.ResponseDto;
import ru.apisarev.dto.ResponseWordDto;
import ru.apisarev.entity.TranslateRequest;
import ru.apisarev.entity.TranslatedWord;
import ru.apisarev.repository.TranslatedWordRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class TranslationService {

    private final TranslationClient translationClient;

    @Inject
    protected TranslatedWordRepository translatedWordRepository;

    public TranslationService() {
        translationClient = RestClientBuilder.newBuilder()
                .baseUri(URI.create(ConfigProvider.getConfig().getValue("translate.api", String.class)))
                .build(TranslationClient.class);
    }

    public Uni<String> translate(RequestFormattedDto request, TranslateRequest translationId) {
        Uni<ResponseDto> response = translationClient.translateAsync(request);
        return saveWords(response, request, translationId).replaceWith(response).
                onItem().transform(x ->
                        x.getTranslations().parallelStream().map(ResponseWordDto::getText)
                                .collect(Collectors.joining(" "))
                );
    }

    protected Uni<Object> saveWords(Uni<ResponseDto> response, RequestFormattedDto request, TranslateRequest translationId) {
        return response.onItem().transform(ResponseDto::getTranslations).onItem().transform(x -> {
                    List<TranslatedWord> result = new ArrayList<>();
                    for (int y = 0; y < x.size(); y++) {
                        ResponseWordDto responseWordDTO = x.get(y);
                        TranslatedWord word = TranslatedWord.builder()
                                .translatedWord(responseWordDTO.getText())
                                .originalLangCode(responseWordDTO.getDetectedLanguageCode())
                                .translatedLangCode(request.getTargetLanguageCode())
                                .word(request.getTexts().get(y))
                                .translationId(translationId)
                                .build();
                        result.add(word);
                    }
                    return result;
                })
                .onItem().transformToUni(x -> translatedWordRepository.persist(x));
    }

}
