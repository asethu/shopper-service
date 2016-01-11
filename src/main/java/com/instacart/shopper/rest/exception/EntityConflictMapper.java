package com.instacart.shopper.rest.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import lombok.extern.slf4j.Slf4j;

import com.instacart.shopper.exception.EntityConflictException;

/**
 * An {@link ExceptionMapper} to map {@link EntityConflictException} occurrences to HTTP
 * {@link Status#CONFLICT} response.
 *
 * @author arun
 */
@Slf4j
@Provider
public class EntityConflictMapper implements ExceptionMapper<EntityConflictException> {

    @Override
    public Response toResponse(EntityConflictException e) {
        log.debug("Requested entity already exists " + e);

        return Response
                .status(Status.CONFLICT)
                .entity(e.getMessage())
                .type(MediaType.TEXT_PLAIN)
                .build();
    }
}
