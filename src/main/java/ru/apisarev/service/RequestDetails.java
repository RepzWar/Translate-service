package ru.apisarev.service;

import io.vertx.core.http.HttpServerRequest;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

@RequestScoped
public class RequestDetails {

    @Inject
    protected HttpServerRequest request;

    public String getRemoteAddress() {
        return request.remoteAddress().hostAddress();
    }
}