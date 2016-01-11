package com.instacart.shopper.rest.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import lombok.extern.slf4j.Slf4j;

import com.instacart.shopper.exception.EntityNotFoundException;

/**
 * An {@link ExceptionMapper} to map {@link EntityNotFoundException} occurrences to HTTP
 * {@link Status#NOT_FOUND} response.
 *
 * @author arun
 */
@Slf4j
@Provider
public class EntityNotFoundMapper implements ExceptionMapper<EntityNotFoundException> {

    @Override
    public Response toResponse(EntityNotFoundException e) {
        log.debug("Requested entity not found " + e);

        return Response
                .status(Status.NOT_FOUND)
                .entity(e.getMessage())
                .type(MediaType.TEXT_PLAIN)
                .build();
    }
}
