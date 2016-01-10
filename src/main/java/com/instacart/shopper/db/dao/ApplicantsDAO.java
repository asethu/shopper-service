package com.instacart.shopper.db.dao;

import java.sql.SQLException;

import com.instacart.shopper.db.model.Applicant;
import com.instacart.shopper.exception.EntityNotFoundException;

/**
 * Data access layer interface for an {@link Applicant} entity. All CRUD operations for this entity
 * into the underlying data store will go through this interface.
 *
 * @author arun
 */
public interface ApplicantsDAO {
    String CREATE_TABLE_QUERY = "CREATE TABLE applicants ("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, first_name VARCHAR, last_name VARCHAR,"
            + " region VARCHAR, phone VARCHAR, email VARCHAR, phone_type VARCHAR,"
            + " source VARCHAR, over_21 BOOLEAN, reason TEXT, workflow_state VARCHAR,"
            + " created_at DATETIME NOT NULL, updated_at DATETIME NOT NULL)";

    String INSERT_ITEM_QUERY = "INSERT INTO applicants (first_name, last_name, region, phone,"
            + " email, phone_type, source, over_21, reason, workflow_status, created_at, updated_at)"
            + " VALUES (?, ? ,?, ?, ?, ? ,?, ?, ?, ? ,?, ?)";

    String GET_ITEM_BY_ID_QUERY = "SELECT id, first_name, last_name, region, phone, email, phone_type,"
            + " source, over_21, reason, workflow_status, created_at, updated_at FROM applicants"
            + " WHERE id = ?";

    String GET_ITEM_BY_EMAIL_QUERY = "SELECT id, first_name, last_name, region, phone, email, phone_type,"
            + " source, over_21, reason, workflow_status, created_at, updated_at FROM applicants"
            + " WHERE email = ?";

    String UPDATE_ITEM_QUERY = "UPDATE applicants SET first_name = ?, last_name = ?, region = ?, phone = ?,"
            + " email = ?, phone_type = ?, over_21 = ? WHERE id = ?";

    String DELETE_TABLE_QUERY = "DROP TABLE IF EXISTS applicants";

    public void createTable() throws SQLException;

    public void deleteTable() throws SQLException;

    public void createApplicant(Applicant applicant) throws SQLException;

    public Applicant getApplicantById(String applicantId) throws EntityNotFoundException, SQLException;

    public Applicant getApplicantByEmail(String email) throws EntityNotFoundException, SQLException;

    public void updateApplicant(Applicant applicant) throws SQLException;
}
