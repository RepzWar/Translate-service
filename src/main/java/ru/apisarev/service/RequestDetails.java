package ru.apisarev.service;

import io.vertx.core.http.HttpServerRequest;
import lombok.AllArgsConstructor;

import javax.enterprise.context.RequestScoped;

@RequestScoped
@AllArgsConstructor
public class RequestDetails {

    protected HttpServerRequest request;

    public String getRemoteAddress() {
        return request.remoteAddress().hostAddress();
    }
}