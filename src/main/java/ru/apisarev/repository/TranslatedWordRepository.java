package ru.apisarev.repository;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import ru.apisarev.entity.TranslatedWord;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TranslatedWordRepository implements PanacheRepository<TranslatedWord> {
}
