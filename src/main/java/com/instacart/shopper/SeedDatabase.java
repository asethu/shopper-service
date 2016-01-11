package com.instacart.shopper;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;

import com.instacart.shopper.db.dao.ApplicantsDAO;
import com.instacart.shopper.db.dao.impl.ApplicantsDAOImpl;
import com.instacart.shopper.db.model.Applicant;
import com.instacart.shopper.db.model.ApplicantStatus;
import com.instacart.shopper.exception.EntityConflictException;

/**
 * Utility class to seed the database
 *
 * @author arun
 */
@Slf4j
public class SeedDatabase {
    private static final String DATE_FORMAT = "2014-12-%sT10:30:00.000Z";
    private ApplicantsDAO applicantsDAO;
    private Random random;

    public SeedDatabase() {
        random = new Random();
        applicantsDAO = new ApplicantsDAOImpl();
    }

    public static void main(String[] args) {
        SeedDatabase seed = new SeedDatabase();

        try {
            seed.initializeTable();
            log.info("Successfully initialized tables");
        } catch (SQLException e) {
            log.error("Failed to initialize tables", e);
        }

        try {
            seed.seedTable();
            log.info("Successfully inserted seeds to the table");
        } catch (SQLException e) {
            log.error("Failed to seed the tables", e);
        }
    }

    public void initializeTable() throws SQLException {
        applicantsDAO.deleteTable();
        applicantsDAO.createTable();
    }

    public void seedTable() throws SQLException {
        // week 0: 2014-12-01 to 2014-12-07
        Map<ApplicantStatus, Integer> count = new HashMap<>();
        count.put(ApplicantStatus.applied, 100);
        count.put(ApplicantStatus.quiz_started, 50);
        count.put(ApplicantStatus.quiz_completed, 20);
        count.put(ApplicantStatus.onboarding_requested, 10);
        count.put(ApplicantStatus.onboarding_completed, 5);
        count.put(ApplicantStatus.hired, 1);
        count.put(ApplicantStatus.rejected, 0);
        seedTable(0, count);

        // week 1: 2014-12-08 to 2014-12-14
        count.clear();
        count.put(ApplicantStatus.applied, 200);
        count.put(ApplicantStatus.quiz_started, 75);
        count.put(ApplicantStatus.quiz_completed, 50);
        count.put(ApplicantStatus.onboarding_requested, 20);
        count.put(ApplicantStatus.onboarding_completed, 10);
        count.put(ApplicantStatus.hired, 5);
        count.put(ApplicantStatus.rejected, 0);
        seedTable(1, count);

        // week 2: 2014-12-15 to 2014-12-21
        count.clear();
        count.put(ApplicantStatus.applied, 70);
        count.put(ApplicantStatus.quiz_started, 20);
        count.put(ApplicantStatus.quiz_completed, 10);
        count.put(ApplicantStatus.onboarding_requested, 0);
        count.put(ApplicantStatus.onboarding_completed, 0);
        count.put(ApplicantStatus.hired, 0);
        count.put(ApplicantStatus.rejected, 0);
        seedTable(2, count);

        // week 3: 2014-12-22 to 2014-12-28
        count.clear();
        count.put(ApplicantStatus.applied, 40);
        count.put(ApplicantStatus.quiz_started, 20);
        count.put(ApplicantStatus.quiz_completed, 15);
        count.put(ApplicantStatus.onboarding_requested, 5);
        count.put(ApplicantStatus.onboarding_completed, 1);
        count.put(ApplicantStatus.hired, 1);
        count.put(ApplicantStatus.rejected, 0);
        seedTable(3, count);
    }

    private void seedTable(int week, Map<ApplicantStatus, Integer> statusCount) throws SQLException {
        log.info("Seeding table for week {}", week);

        for (Map.Entry<ApplicantStatus, Integer> entry: statusCount.entrySet()) {
            for (int i = 0; i < entry.getValue(); i++) {
                int date = (7 * week) + (random.nextInt(7) + 1);

                String createdAt = String.format(DATE_FORMAT, date > 9 ? date : "0" + date);

                log.trace("[{}] {}", createdAt, entry.getKey());

                String randomEmail = UUID.randomUUID().toString();
                Applicant applicant = Applicant.builder()
                        .firstName(randomEmail)
                        .lastName(randomEmail)
                        .region(randomEmail)
                        .phone(randomEmail)
                        .email(randomEmail)
                        .phoneType(randomEmail)
                        .over21(true)
                        .status(entry.getKey())
                        .createdAt(createdAt)
                        .updatedAt(createdAt)
                        .build();

                try {
                    applicantsDAO.createApplicant(applicant);
                } catch (EntityConflictException e) {
                    log.error("Got an entity conflict while creating an applicant", e);
                }
            }
        }
    }
}
