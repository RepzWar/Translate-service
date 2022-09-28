package ru.apisarev.controller;

import io.smallrye.mutiny.Uni;
import lombok.AllArgsConstructor;
import ru.apisarev.dto.RequestDto;
import ru.apisarev.service.RequestService;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("translate")
@ApplicationScoped
@AllArgsConstructor
public class RequestController {

    private final RequestService requestService;

    @POST
    public Uni<Response> translate(RequestDto request) {
        return requestService.translate(request).onItem().transform(x -> Response.ok(x).build());
    }

}
