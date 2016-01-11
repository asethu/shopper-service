package com.instacart.shopper.db.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
                throw new EntityConflictException("Applicant already exists with the same email", e);
            }

            throw e;
        }
    }

    @Override
    public Applicant getApplicantByEmail(String email) throws EntityNotFoundException, SQLException {
        try (Connection conn = SQLiteDataSource.INSTANCE.getConnection();
                PreparedStatement stmt = conn.prepareStatement(GET_ITEM_BY_EMAIL_QUERY)) {
            stmt.setString(1, email);
            ResultSet result = stmt.executeQuery();
            if (result.next()) {
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
            } else {
                log.info("Applicant with email " + email + " not found.");

                throw new EntityNotFoundException("Applicant with email " + email + " not found.");
            }
        }
    }

    @Override
    public void updateApplicant(Applicant applicant) throws SQLException {
        try (Connection conn = SQLiteDataSource.INSTANCE.getConnection();
                PreparedStatement stmt = conn.prepareStatement(UPDATE_ITEM_QUERY)) {

            log.trace("Updating details for applicant " + applicant);

            stmt.setString(1, applicant.getFirstName());
            stmt.setString(2, applicant.getLastName());
            stmt.setString(3, applicant.getRegion());
            stmt.setString(4, applicant.getPhone());
            stmt.setString(5, applicant.getPhoneType());
            stmt.setBoolean(6, applicant.isOver21());
            stmt.setString(7, applicant.getUpdatedAt());

            stmt.setString(8, applicant.getEmail());

            stmt.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void createTable() throws SQLException {
        try (Connection conn = SQLiteDataSource.INSTANCE.getConnection()) {
    
            log.debug("Creating table applicants");

            try (PreparedStatement stmt = conn.prepareStatement(CREATE_TABLE_QUERY)) {
                stmt.executeUpdate();
            }

            log.debug("Creating index on table applicants (email)");

            try (PreparedStatement stmt = conn.prepareStatement(CREATE_UNIQUE_INDEX_ON_EMAIL_QUERY)) {
                stmt.executeUpdate();
            }
        }
    }

    @Override
    public void deleteTable() throws SQLException {
        try (Connection conn = SQLiteDataSource.INSTANCE.getConnection()) {
            log.debug("Deleting index on table applicants (email)");

            try (PreparedStatement stmt = conn.prepareStatement(DELETE_UNIQUE_INDEX_ON_EMAIL_QUERY)) {
                stmt.executeUpdate();
            }

            log.debug("Deleting table applicants");

            try (PreparedStatement stmt = conn.prepareStatement(DELETE_TABLE_QUERY)) {
                stmt.executeUpdate();
            }
        }
    }
}
