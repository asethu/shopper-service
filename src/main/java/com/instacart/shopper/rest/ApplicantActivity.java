package com.instacart.shopper.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import lombok.extern.slf4j.Slf4j;

import com.instacart.shopper.ServiceMain;
import com.instacart.shopper.db.dao.ApplicantsDAO;
import com.instacart.shopper.db.dao.impl.ApplicantsDAOImpl;
import com.instacart.shopper.db.model.Applicant;
import com.instacart.shopper.db.model.ApplicantStatus;
import com.instacart.shopper.exception.EntityConflictException;
import com.instacart.shopper.exception.EntityNotFoundException;
import com.instacart.shopper.rest.method.PATCH;

/**
 * REST API implementation layer for the resource {@link Applicant}.
 *
 * @author arun
 */
@Slf4j
@Path("")
public class ApplicantActivity {

    private static final String SERVICE_ENDPOINT = String.format(
        ServiceMain.ENDPOINT_URI_PATTERN, ServiceMain.SERVICE_HOST, ServiceMain.SERVICE_PORT);

    private static final String RESOURCE_APPLICANT_PATH = "applicants/";

    private static final DateTimeFormatter ISO_DATE_TIME_FORMATTER = ISODateTimeFormat.dateTime().withZoneUTC();

    private ApplicantsDAO applicantDAO;

    public ApplicantActivity() {
        applicantDAO = new ApplicantsDAOImpl();
    }

    @POST
    @Path(RESOURCE_APPLICANT_PATH)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createApplicant(Applicant applicant)
            throws EntityConflictException, SQLException {

        log.info("Request to create a new applicant with email {}", applicant.getEmail());

        log.debug("Full applicant details {}", applicant);

        String timestamp = ISO_DATE_TIME_FORMATTER.print(System.currentTimeMillis());

        applicant.setStatus(ApplicantStatus.applied);
        applicant.setCreatedAt(timestamp);
        applicant.setUpdatedAt(timestamp);

        applicantDAO.createApplicant(applicant);

        try {
            URI applicantURI = new URI(constructApplicantURI(applicant.getEmail()));
            return Response.created(applicantURI).build();
        } catch (URISyntaxException e) {
            log.error("Failed to generate the URI for the new applicant");
            throw new RuntimeException(e);
        }
    }

    @GET
    @Path(RESOURCE_APPLICANT_PATH + "{email}")
    @Produces(MediaType.APPLICATION_JSON)
    public Applicant getApplicant(@PathParam("email") String applicantEmail)
            throws EntityNotFoundException, SQLException {

        log.info("Request to get the applicant with email {}", applicantEmail);

        return applicantDAO.getApplicantByEmail(applicantEmail);
    }

    @GET
    @Path(RESOURCE_APPLICANT_PATH + "funnel.json")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Map<String, Integer>> getApplicantsFunnelReport(
            @QueryParam("start_date") String startDate, @QueryParam("end_date") String endDate)
            throws SQLException {

        log.info("Request to get the applicant funnel report between dates {} and {}", startDate, endDate);

        return applicantDAO.getApplicantFunnelReport(startDate, endDate);
    }

    @PATCH
    @Path(RESOURCE_APPLICANT_PATH + "{email}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateApplicant(@PathParam("email") String applicantEmail, Applicant applicant)
            throws EntityNotFoundException, SQLException {

        log.info("Request to update the applicant with email {}", applicantEmail);

        applicant.setUpdatedAt(ISO_DATE_TIME_FORMATTER.print(System.currentTimeMillis()));
        applicantDAO.updateApplicant(applicantEmail, applicant);

        return Response.ok().build();
    }

    private String constructApplicantURI(String applicantId) {
        StringBuilder applicantURI = new StringBuilder(SERVICE_ENDPOINT);
        applicantURI.append(RESOURCE_APPLICANT_PATH).append(applicantId);
        return applicantURI.toString();
    }
}
