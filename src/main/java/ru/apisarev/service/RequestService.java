package ru.apisarev.service;


import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.hibernate.reactive.mutiny.Mutiny.SessionFactory;
import ru.apisarev.client.TranslationClient;
import ru.apisarev.dto.RequestDto;
import ru.apisarev.dto.RequestFormattedDto;
import ru.apisarev.dto.ResponseDto;
import ru.apisarev.dto.ResponseWordDto;
import ru.apisarev.dto.ResultDto;
import ru.apisarev.entity.TranslateRequest;
import ru.apisarev.entity.TranslatedWord;

import javax.enterprise.context.ApplicationScoped;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class RequestService {

    private final TranslationClient translationClient;
    private final SessionFactory sf;
    private final RequestDetails requestDetails;

    public RequestService(SessionFactory sf, RequestDetails requestDetails) {
        translationClient = RestClientBuilder.newBuilder()
                .baseUri(URI.create(ConfigProvider.getConfig().getValue("translate.api", String.class)))
                .build(TranslationClient.class);
        this.sf = sf;
        this.requestDetails = requestDetails;
    }

    public Uni<ResultDto> translate(RequestDto request) {
        RequestFormattedDto requestFormatted = new RequestFormattedDto();//Преобразование пришедшего запроса в формат для обращения к переводчику
        requestFormatted.setTargetLanguageCode(request.getTargetLanguageCode());
        requestFormatted.setTexts(List.of(request.getText().split(" ")));

        final TranslateRequest translateRequest = TranslateRequest.builder().request(request.getText())//Создание объекта, для сохранения в бд информации о запросе и его результате
                .time(LocalDateTime.now())
                .remoteAddress(requestDetails.getRemoteAddress())
                .targetLangCode(request.getTargetLanguageCode())
                .build();

        final Uni<ResponseDto> response = translationClient.translateAsync(requestFormatted);//Запрос к переводчику

        final Uni<String> result = response.onItem().transform(ResponseDto::getTranslations).onItem().transform(x -> {//Преобразование результата в набор объектов слово-перевод и сохранение в бд
                    List<TranslatedWord> translatedWords = new ArrayList<>();
                    for (int y = 0; y < x.size(); y++) {
                        ResponseWordDto responseWordDTO = x.get(y);
                        TranslatedWord word = TranslatedWord.builder()
                                .translatedWord(responseWordDTO.getText())
                                .originalLangCode(responseWordDTO.getDetectedLanguageCode())
                                .translatedLangCode(requestFormatted.getTargetLanguageCode())
                                .word(requestFormatted.getTexts().get(y))
                                .translationId(translateRequest)
                                .build();
                        translatedWords.add(word);
                    }
                    return translatedWords;
                })
                .onItem().transformToUni(x -> sf.withTransaction(session -> session.merge(x)))
                .replaceWith(response).//Преобразование результатов перевода в формат нужный для ответа
                        onItem().transform(x ->
                        x.getTranslations().parallelStream().map(ResponseWordDto::getText)
                                .collect(Collectors.joining(" "))
                );

        return result.onItem().transform(x -> {//Вставка результата перевода в бд объект запроса и его сохранение
                    translateRequest.setResult(x);
                    return request;
                })
                .onItem().transformToUni(x -> sf.withTransaction(session -> session.merge(x)))
                .replaceWith(result).onItem().transform(x ->//Возвращаем конечный результат
                        ResultDto.builder().translation(x).build());
    }

}