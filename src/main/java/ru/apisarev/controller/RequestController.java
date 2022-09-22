package ru.apisarev.controller;

import io.smallrye.mutiny.Uni;
import ru.apisarev.dto.RequestDto;
import ru.apisarev.service.RequestService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("translate")
@ApplicationScoped
public class RequestController {

    @Inject
    protected RequestService requestService;

    @GET
    public Uni<Response> findAll() {
        return requestService.findAll().onItem().transform(x -> Response.ok(x).build());
    }

    @POST
    public Uni<Response> translate(RequestDto request) {
        return requestService.translate(request).onItem().transform(x -> Response.ok(x).build());
    }

}
