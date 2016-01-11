package com.instacart.shopper.rest.exception;

import java.sql.SQLException;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import lombok.extern.slf4j.Slf4j;

/**
 * An {@link ExceptionMapper} to map {@link SQLException} occurrences to HTTP
 * {@link Status#INTERNAL_SERVER_ERROR} response.
 *
 * @author arun
 */
@Slf4j
@Provider
public class SQLExceptionMapper implements ExceptionMapper<SQLException> {

    @Override
    public Response toResponse(SQLException e) {
        log.error("Got an unexpected SQL exception " + e);

        return Response.status(Status.INTERNAL_SERVER_ERROR).build();
    }
}
