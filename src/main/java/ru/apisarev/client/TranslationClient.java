package ru.apisarev.client;

import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam;
import ru.apisarev.dto.RequestFormattedDto;
import ru.apisarev.dto.ResponseDto;

import javax.ws.rs.POST;

@ClientHeaderParam(name = "Authorization", value = "${translate.api.key}")
public interface TranslationClient {

    @POST
    Uni<ResponseDto> translateAsync(RequestFormattedDto id);

}
