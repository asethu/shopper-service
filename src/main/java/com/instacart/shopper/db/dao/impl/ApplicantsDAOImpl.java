package com.instacart.shopper.db.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import com.instacart.shopper.db.SQLiteDataSource;
import com.instacart.shopper.db.SQLiteResultCode;
import com.instacart.shopper.db.dao.ApplicantsDAO;
import com.instacart.shopper.db.model.Applicant;
import com.instacart.shopper.db.model.ApplicantStatus;
import com.instacart.shopper.exception.EntityConflictException;
import com.instacart.shopper.exception.EntityNotFoundException;

/**
 * Implementation of {@link ApplicantsDAO} to interact with an SQLite data store.
 *
 * @author arunse
 */
@Slf4j
public class ApplicantsDAOImpl implements ApplicantsDAO {

    @Override
    public void createApplicant(Applicant applicant) throws EntityConflictException, SQLException {
        try (Connection conn = SQLiteDataSource.INSTANCE.getConnection();
                PreparedStatement stmt = conn.prepareStatement(INSERT_ITEM_QUERY)) {

            log.trace("Creating a new applicant " + applicant);

            stmt.setString(1, applicant.getFirstName());
            stmt.setString(2, applicant.getLastName());
            stmt.setString(3, applicant.getRegion());
            stmt.setString(4, applicant.getPhone());
            stmt.setString(5, applicant.getEmail());
            stmt.setString(6, applicant.getPhoneType());
            stmt.setString(7, applicant.getSource());
            stmt.setBoolean(8, applicant.isOver21());
            stmt.setString(9, applicant.getReason());
            stmt.setString(10, applicant.getStatus().name());
            stmt.setString(11, applicant.getCreatedAt());
            stmt.setString(12, applicant.getUpdatedAt());
            stmt.executeUpdate();
        } catch (SQLException e) {
            if (e.getErrorCode() == SQLiteResultCode.SQLITE_CONSTRAINT.errorCode()) {
                log.info("Applicant already exists with the same email {}.", applicant.getEmail());

                throw new EntityConflictException("Applicant already exists with the same email", e);
            }

            throw e;
        }
    }

    @Override
    public Applicant getApplicantByEmail(String email) throws EntityNotFoundException, SQLException {

        log.trace("Fetching applicant with email {}.", email);

        try (Connection conn = SQLiteDataSource.INSTANCE.getConnection();
                PreparedStatement stmt = conn.prepareStatement(GET_ITEM_BY_EMAIL_QUERY)) {
            stmt.setString(1, email);
            ResultSet result = stmt.executeQuery();
            if (result.next()) {
                return constructApplicant(result);
            } else {
                log.info("Applicant with email {} not found.", email);

                throw new EntityNotFoundException("Applicant with email " + email + " not found.");
            }
        }
    }

    @Override
    public Map<String, Map<String, Integer>> getApplicantFunnelReport(String startDate, String endDate) throws SQLException {

        log.trace("Generating the applicant funnel report between {} and {} dates.", startDate, endDate);

        Map<String, Map<String, Integer>> response = new HashMap<>();

        try (Connection conn = SQLiteDataSource.INSTANCE.getConnection();
                PreparedStatement stmt = conn.prepareStatement(GET_FUNNEL_REPORT_QUERY)) {

            stmt.setString(1, startDate);
            stmt.setString(2, endDate);

            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                StringBuilder key = new StringBuilder();
                key.append(result.getString(1)).append("-").append(result.getString(2));

                Map<String, Integer> map; 
                if (response.containsKey(key.toString())) {
                    map = response.get(key.toString());
                } else {
                    map = new HashMap<>();
                    response.put(key.toString(), map);
                }

                map.put(result.getString(3), result.getInt(4));
            }
        }

        return response;
    }

    @Override
    public void updateApplicant(String email, Applicant applicant) throws EntityNotFoundException, SQLException {

        log.trace("Updating details for applicant " + applicant);

        Connection conn = null;
        try {
            conn = SQLiteDataSource.INSTANCE.getConnection();
            conn.setAutoCommit(false);

            Applicant dbApplicant = null;
            try (PreparedStatement stmt = conn.prepareStatement(GET_ITEM_BY_EMAIL_QUERY)) {
                stmt.setString(1, email);
                ResultSet result = stmt.executeQuery();
                if (result.next()) {
                    dbApplicant = constructApplicant(result);
                } else {
                    log.info("Applicant with email {} not found for update.", email);

                    throw new EntityNotFoundException("Applicant with email " + email + " not found.");
                }
            }

            // Apply all updates that we got in the request 
            applyUpdates(dbApplicant, applicant);

            try (PreparedStatement stmt = conn.prepareStatement(UPDATE_ITEM_QUERY)) {
                stmt.setString(1, dbApplicant.getFirstName());
                stmt.setString(2, dbApplicant.getLastName());
                stmt.setString(3, dbApplicant.getRegion());
                stmt.setString(4, dbApplicant.getPhone());
                stmt.setString(5, dbApplicant.getPhoneType());
                stmt.setBoolean(6, dbApplicant.isOver21());
                stmt.setString(7, dbApplicant.getUpdatedAt());

                stmt.setString(8, email);

                stmt.executeUpdate();
            }

            conn.commit();
        } catch (SQLException e) {
            log.error("Failed with updating the applicant info with email {}.", email, e);

            conn.rollback();
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    @Override
    public void createTable() throws SQLException {
        try (Connection conn = SQLiteDataSource.INSTANCE.getConnection()) {

            log.info("Creating table applicants");

            try (PreparedStatement stmt = conn.prepareStatement(CREATE_TABLE_QUERY)) {
                stmt.executeUpdate();
            }

            log.info("Creating index on table applicants (email)");

            try (PreparedStatement stmt = conn.prepareStatement(CREATE_INDEX_ON_EMAIL_QUERY)) {
                stmt.executeUpdate();
            }

            log.info("Creating index on table applicants (created_at)");

            try (PreparedStatement stmt = conn.prepareStatement(CREATE_INDEX_ON_CREATED_AT_QUERY)) {
                stmt.executeUpdate();
            }
        }
    }

    @Override
    public void deleteTable() throws SQLException {
        try (Connection conn = SQLiteDataSource.INSTANCE.getConnection()) {

            log.info("Deleting index on table applicants (created_at)");

            try (PreparedStatement stmt = conn.prepareStatement(DELETE_INDEX_ON_CREATED_AT_QUERY)) {
                stmt.executeUpdate();
            }

            log.info("Deleting index on table applicants (email)");

            try (PreparedStatement stmt = conn.prepareStatement(DELETE_INDEX_ON_EMAIL_QUERY)) {
                stmt.executeUpdate();
            }

            log.info("Deleting table applicants");

            try (PreparedStatement stmt = conn.prepareStatement(DELETE_TABLE_QUERY)) {
                stmt.executeUpdate();
            }
        }
    }

    private Applicant applyUpdates(Applicant dbApplicant, Applicant requestApplicant) {
        if (requestApplicant.getFirstName() != null) {
            dbApplicant.setFirstName(requestApplicant.getFirstName());
        }

        if (requestApplicant.getLastName() != null) {
            dbApplicant.setLastName(requestApplicant.getLastName());
        }

        if (requestApplicant.getRegion() != null) {
            dbApplicant.setRegion(requestApplicant.getRegion());
        }

        if (requestApplicant.getPhone() != null) {
            dbApplicant.setPhone(requestApplicant.getPhone());
        }

        if (requestApplicant.getPhoneType() != null) {
            dbApplicant.setPhoneType(requestApplicant.getPhoneType());
        }

        if (requestApplicant.getUpdatedAt() != null) {
            dbApplicant.setUpdatedAt(requestApplicant.getUpdatedAt());
        }

        return dbApplicant;
    }

    private Applicant constructApplicant(ResultSet result) throws SQLException {
        return Applicant.builder()
                .id(result.getInt(1))
                .firstName(result.getString(2))
                .lastName(result.getString(3))
                .region(result.getString(4))
                .phone(result.getString(5))
                .email(result.getString(6))
                .phoneType(result.getString(7))
                .source(result.getString(8))
                .over21(result.getBoolean(9))
                .reason(result.getString(10))
                .status(ApplicantStatus.valueOf(result.getString(11)))
                .createdAt(result.getString(12))
                .updatedAt(result.getString(13))
                .build();
    }
}
