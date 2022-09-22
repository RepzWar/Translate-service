package ru.apisarev.repository;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import ru.apisarev.entity.TranslateRequest;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RequestRepository implements PanacheRepository<TranslateRequest> {
}
