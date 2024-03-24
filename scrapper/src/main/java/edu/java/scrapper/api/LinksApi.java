/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech) (7.3.0).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */

package edu.java.scrapper.api;

import edu.java.scrapper.controller.dto.request.AddLinkRequest;
import edu.java.scrapper.controller.dto.request.RemoveLinkRequest;
import edu.java.scrapper.controller.dto.response.ApiErrorResponse;
import edu.java.scrapper.controller.dto.response.LinkResponse;
import edu.java.scrapper.controller.dto.response.ListLinksResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.NativeWebRequest;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-02-29T23:19:37.800823Z[UTC]")
@Validated
@Tag(name = "links", description = "the links API")
public interface LinksApi {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    /**
     * DELETE /links : Убрать отслеживание ссылки
     *
     * @param tgChatId          (required)
     * @param removeLinkRequest (required)
     * @return Ссылка успешно убрана (status code 200)
     *     or Некорректные параметры запроса (status code 400)
     *     or Ссылка не найдена (status code 404)
     */
    @Operation(
        operationId = "linksDelete",
        summary = "Убрать отслеживание ссылки",
        responses = {
            @ApiResponse(responseCode = "200", description = "Ссылка успешно убрана", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = LinkResponse.class))
            }),
            @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))
            }),
            @ApiResponse(responseCode = "404", description = "Ссылка не найдена", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))
            })
        }
    )
    @RequestMapping(
        method = RequestMethod.DELETE,
        value = "/links",
        produces = {"application/json"},
        consumes = {"application/json"}
    )

    default ResponseEntity<LinkResponse> linksDelete(
        @NotNull @Parameter(name = "Tg-Chat-Id", description = "", required = true, in = ParameterIn.HEADER)
        @RequestHeader(value = "Tg-Chat-Id", required = true) Long tgChatId,
        @Parameter(name = "RemoveLinkRequest", description = "", required = true) @Valid @RequestBody
        RemoveLinkRequest removeLinkRequest
    ) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    /**
     * GET /links : Получить все отслеживаемые ссылки
     *
     * @param tgChatId (required)
     * @return Ссылки успешно получены (status code 200)
     *     or Некорректные параметры запроса (status code 400)
     */
    @Operation(
        operationId = "linksGet",
        summary = "Получить все отслеживаемые ссылки",
        responses = {
            @ApiResponse(responseCode = "200", description = "Ссылки успешно получены", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = ListLinksResponse.class))
            }),
            @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))
            })
        }
    )
    @RequestMapping(
        method = RequestMethod.GET,
        value = "/links",
        produces = {"application/json"}
    )

    default ResponseEntity<ListLinksResponse> linksGet(
        @NotNull @Parameter(name = "Tg-Chat-Id", description = "", required = true, in = ParameterIn.HEADER)
        @RequestHeader(value = "Tg-Chat-Id", required = true) Long tgChatId
    ) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    /**
     * POST /links : Добавить отслеживание ссылки
     *
     * @param tgChatId       (required)
     * @param addLinkRequest (required)
     * @return Ссылка успешно добавлена (status code 200)
     *     or Некорректные параметры запроса (status code 400)
     *     or Чат не зарегистрирован (status code 401)
     *     or Чат уже подписан на отслеживание ссылки (status code 409)
     */
    @Operation(
        operationId = "linksPost",
        summary = "Добавить отслеживание ссылки",
        responses = {
            @ApiResponse(responseCode = "200", description = "Ссылка успешно добавлена", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = LinkResponse.class))
            }),
            @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))
            }),
            @ApiResponse(responseCode = "401", description = "Чат не зарегистрирован", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))
            }),
            @ApiResponse(responseCode = "409", description = "Чат уже подписан на отслеживание ссылки", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))
            })
        }
    )
    @RequestMapping(
        method = RequestMethod.POST,
        value = "/links",
        produces = {"application/json"},
        consumes = {"application/json"}
    )

    default ResponseEntity<LinkResponse> linksPost(
        @NotNull @Parameter(name = "Tg-Chat-Id", description = "", required = true, in = ParameterIn.HEADER)
        @RequestHeader(value = "Tg-Chat-Id", required = true) Long tgChatId,
        @Parameter(name = "AddLinkRequest", description = "", required = true) @Valid @RequestBody
        AddLinkRequest addLinkRequest
    ) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

}
