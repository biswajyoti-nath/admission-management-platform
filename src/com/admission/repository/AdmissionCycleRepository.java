package com.admission.repository;

import com.admission.model.AdmissionCycle;

import java.util.List;

/**
 * Repository interface for AdmissionCycle entity persistence.
 *
 * <p>Extends the base CRUD with an active-cycle query needed by
 * the application workflow — students can only apply to active cycles.</p>
 *
 * <p><b>SRP:</b> Responsible solely for AdmissionCycle data access.</p>
 */
public interface AdmissionCycleRepository extends Repository<AdmissionCycle> {

    /**
     * Finds all active admission cycles for a specific program.
     *
     * @param programId the program's ID
     * @return list of active cycles (isActive == true); empty if none
     */
    List<AdmissionCycle> findActiveByProgram(String programId);
}
