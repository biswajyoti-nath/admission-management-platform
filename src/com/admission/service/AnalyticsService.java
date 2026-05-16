package com.admission.service;

import java.util.Map;

/**
 * Service interface for analytics and reporting.
 *
 * <p>Provides aggregated views over application and enrollment data
 * for admin dashboards and reports.</p>
 *
 * <p><b>ISP:</b> Separated from AdminService — reporting concerns are
 * distinct from configuration concerns. Controllers that display reports
 * don't need to depend on CRUD operations.</p>
 */
public interface AnalyticsService {

    /**
     * Returns the total number of applications for a program across all cycles.
     *
     * @param programId the program's ID
     * @return the total application count
     */
    int getApplicationCountByProgram(String programId);

    /**
     * Returns enrollment statistics for a program.
     *
     * <p>The returned map contains:</p>
     * <ul>
     *   <li>{@code "totalEnrolled"} — number of enrolled students</li>
     *   <li>{@code "totalSeats"} — total seat capacity of the program</li>
     *   <li>{@code "occupancyPercent"} — enrollment as a percentage of capacity</li>
     * </ul>
     *
     * @param programId the program's ID
     * @return a map of statistic names to their values
     */
    Map<String, Integer> getEnrollmentStats(String programId);
}
