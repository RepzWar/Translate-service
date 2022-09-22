package ru.apisarev.controller;

import io.smallrye.mutiny.Uni;
import ru.apisarev.service.WordService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("words")
@ApplicationScoped
public class WordController {

    @Inject
    protected WordService wordService;

    @GET
    public Uni<Response> id() {
        return wordService.findAll().onItem().transform(x -> Response.ok(x).build());
    }

}
