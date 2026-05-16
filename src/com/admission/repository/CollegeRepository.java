package com.admission.repository;

import com.admission.model.College;

/**
 * Repository interface for College entity persistence.
 *
 * <p>Provides standard CRUD operations inherited from {@link Repository}.
 * No additional domain-specific queries are required — college lookups
 * are always by ID or full list.</p>
 *
 * <p><b>SRP:</b> Responsible solely for College data access.</p>
 */
public interface CollegeRepository extends Repository<College> {
    // Inherits: save, findById, findAll, updateAll
}
