package com.instacart.shopper.db.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import lombok.extern.slf4j.Slf4j;

import com.instacart.shopper.db.SQLiteDataSource;
import com.instacart.shopper.db.dao.ApplicantsDAO;
import com.instacart.shopper.db.model.Applicant;
import com.instacart.shopper.exception.EntityNotFoundException;

/**
 * Implementation of {@link ApplicantsDAO} to interact with an SQLite data store.
 *
 * @author arunse
 */
@Slf4j
public class ApplicantsDAOImpl implements ApplicantsDAO {

    @Override
    public void createApplicant(Applicant applicant) throws SQLException {
        try (Connection conn = SQLiteDataSource.INSTANCE.getConnection();
                PreparedStatement stmt = conn.prepareStatement(INSERT_ITEM_QUERY)) {

            log.debug("Creating a new applicant " + applicant);

            Date currentDate = new Date(System.currentTimeMillis());

            stmt.setString(1, applicant.getFirstName());
            stmt.setString(2, applicant.getLastName());
            stmt.setString(3, applicant.getRegion());
            stmt.setString(4, applicant.getPhone());
            stmt.setString(5, applicant.getEmail());
            stmt.setString(6, applicant.getPhoneType());
            stmt.setString(7, applicant.getSource());
            stmt.setBoolean(8, applicant.isOver21());
            stmt.setString(9, applicant.getReason());
            stmt.setString(10, applicant.getStatus());
            stmt.setDate(11, currentDate);
            stmt.setDate(12, currentDate);
            stmt.executeUpdate();
        }
    }

    @Override
    public Applicant getApplicantById(String applicantId) throws EntityNotFoundException, SQLException {
        try (Connection conn = SQLiteDataSource.INSTANCE.getConnection();
                PreparedStatement stmt = conn.prepareStatement(GET_ITEM_BY_ID_QUERY)) {
            stmt.setString(1, applicantId);
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
                        .status(result.getString(11))
                        .createdAt(result.getDate(12))
                        .updatedAt(result.getDate(13))
                        .build();
            } else {
                log.info("Applicant with id " + applicantId + " not found.");

                throw new EntityNotFoundException("Applicant with id " + applicantId + " not found.");
            }
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
                        .status(result.getString(11))
                        .createdAt(result.getDate(12))
                        .updatedAt(result.getDate(13))
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

            log.debug("Updating details for applicant " + applicant);

            Date currentDate = new Date(System.currentTimeMillis());

            stmt.setString(1, applicant.getFirstName());
            stmt.setString(2, applicant.getLastName());
            stmt.setString(3, applicant.getRegion());
            stmt.setString(4, applicant.getPhone());
            stmt.setString(5, applicant.getEmail());
            stmt.setString(6, applicant.getPhoneType());
            stmt.setBoolean(7, applicant.isOver21());
            stmt.setDate(8, currentDate);
            stmt.setInt(9, applicant.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void createTable() throws SQLException {
        log.debug("Creating the applicants table");

        try (Connection conn = SQLiteDataSource.INSTANCE.getConnection();
                PreparedStatement stmt = conn.prepareStatement(CREATE_TABLE_QUERY)) {
            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteTable() throws SQLException {
        log.debug("Deleting the applicants table");

        try (Connection conn = SQLiteDataSource.INSTANCE.getConnection();
                PreparedStatement stmt = conn.prepareStatement(DELETE_TABLE_QUERY)) {
            stmt.executeUpdate();
        }
    }
}
