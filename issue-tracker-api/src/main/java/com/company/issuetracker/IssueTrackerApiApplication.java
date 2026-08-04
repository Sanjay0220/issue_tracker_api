package com.company.issuetracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the Issue Tracker API Spring Boot application.
 */
@SpringBootApplication
public class IssueTrackerApiApplication {

    /**
     * Main method to launch the Spring Boot application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(IssueTrackerApiApplication.class, args);
    }
}