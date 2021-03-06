package com.instacart.shopper.db.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * POJO container class for an applicant.
 *
 * @author arun
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Applicant {
    private int id;
    private String firstName;
    private String lastName;
    private String region;
    private String phone;
    private String email;
    private String phoneType;
    private String source;
    private boolean over21;
    private String reason;
    private ApplicantStatus status;
    private String createdAt;
    private String updatedAt;
}
