package com.instacart.shopper.db.dao;

import java.sql.SQLException;
import java.util.Map;

import com.instacart.shopper.db.model.Applicant;
import com.instacart.shopper.exception.EntityConflictException;
import com.instacart.shopper.exception.EntityNotFoundException;

/**
 * Data access layer interface for an {@link Applicant} entity. All CRUD operations for this entity
 * into the underlying data store will go through this interface.
 *
 * @author arun
 */
public interface ApplicantsDAO {

    String CREATE_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS applicants ("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, first_name VARCHAR, last_name VARCHAR,"
            + " region VARCHAR, phone VARCHAR, email VARCHAR, phone_type VARCHAR,"
            + " source VARCHAR, over_21 BOOLEAN, reason TEXT, workflow_state VARCHAR,"
            + " created_at TEXT NOT NULL, updated_at TEXT NOT NULL)";

    String CREATE_UNIQUE_INDEX_ON_EMAIL_QUERY = "CREATE UNIQUE INDEX IF NOT EXISTS index_applicants_email"
            + " ON applicants (email)";

    String INSERT_ITEM_QUERY = "INSERT INTO applicants (first_name, last_name, region, phone,"
            + " email, phone_type, source, over_21, reason, workflow_state, created_at, updated_at)"
            + " VALUES (?, ? ,?, ?, ?, ? ,?, ?, ?, ? ,?, ?)";

    String GET_ITEM_BY_EMAIL_QUERY = "SELECT id, first_name, last_name, region, phone, email, phone_type,"
            + " source, over_21, reason, workflow_state, created_at, updated_at FROM applicants"
            + " WHERE email = ?";

    String UPDATE_ITEM_QUERY = "UPDATE applicants SET first_name = ?, last_name = ?, region = ?, phone = ?,"
            + " phone_type = ?, over_21 = ?, updated_at = ? WHERE email = ?";

    String GET_FUNNEL_REPORT_QUERY = "SELECT DATE(created_at, 'WEEKDAY 0', '-6 DAYS') AS week_start,"
            + " DATE(created_at, 'WEEKDAY 0') AS week_end, workflow_state, COUNT(*) FROM applicants"
            + " WHERE STRFTIME('%Y-%m-%d', created_at) >= DATE(?) AND STRFTIME('%Y-%m-%d', created_at) < DATE(?)"
            + " GROUP BY week_start, workflow_state";

    String DELETE_UNIQUE_INDEX_ON_EMAIL_QUERY = "DROP INDEX IF EXISTS index_applicants_email";

    String DELETE_TABLE_QUERY = "DROP TABLE IF EXISTS applicants";

    /**
     * Method to create the applicants table and all indices for that table.
     *
     * @throws SQLException
     *             Internal exception from the underlying data store.
     */
    public void createTable() throws SQLException;

    /**
     * Method to delete the applicants table and all indices related to that table.
     *
     * @throws SQLException
     *             Internal exception from the underlying data store.
     */
    public void deleteTable() throws SQLException;

    /**
     * Method to create a new applicant with the provided information.
     *
     * @param applicant
     *            An instance of {@link Applicant}.
     * @throws EntityConflictException
     *             This exception is thrown when there exists a application with the same email id.
     * @throws SQLException
     *             Internal exception from the underlying data store.
     */
    public void createApplicant(Applicant applicant) throws EntityConflictException, SQLException;

    /**
     * Method to get an applicant by email.
     *
     * @param email
     *            The email id of the applicant to fetch from the data store.
     * @return An instance of {@link Applicant} for the requested email id.
     * @throws EntityNotFoundException
     *             This exception is thrown when we can't find an applicant with the provided email.
     * @throws SQLException
     *             Internal exception from the underlying data store.
     */
    public Applicant getApplicantByEmail(String email) throws EntityNotFoundException, SQLException;

    /**
     * Method to generate the applicant funnel report within the provided dates.
     *
     * @param startDate
     *            The start date for the report
     * @param endDate
     *            The end date for the report
     * @return An instance of {@link Map<String, Map<String, Integer>>}. The key in the first map is
     *         the date range and value is a Map of consolidated workflow_state to count for that
     *         week.
     * @throws SQLException
     *             Internal exception from the underlying data store.
     */
    public Map<String, Map<String, Integer>> getApplicantFunnelReport(String startDate, String endDate) throws SQLException;

    /**
     * Method to update the information of an applicant with the provided details.
     *
     * @param email
     *            The email id of the applicant to fetch from the data store.
     * @param applicant
     *            The details of the applicant to update. This instance could be sparse with only
     *            the fields to be updated contain values and the rest passed as null
     * @throws EntityNotFoundException
     *             This exception is thrown when we can't find an applicant with the provided email.
     * @throws SQLException
     *             Internal exception from the underlying data store.
     */
    public void updateApplicant(String email, Applicant applicant) throws EntityNotFoundException,  SQLException;
}
