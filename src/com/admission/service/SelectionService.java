package com.admission.service;

import com.admission.model.Application;
import com.admission.strategy.SelectionStrategy;

import java.util.List;

/**
 * Service interface for running admission selection on a cycle.
 *
 * <p>Delegates the actual selection algorithm to a {@link SelectionStrategy},
 * keeping this service open for extension (new strategies) without modification.</p>
 *
 * <p><b>OCP:</b> New selection algorithms are added by implementing
 * SelectionStrategy — this service never changes.</p>
 *
 * <p><b>DIP:</b> Depends on the SelectionStrategy abstraction,
 * not on any concrete algorithm.</p>
 */
public interface SelectionService {

    /**
     * Runs selection for an admission cycle using the provided strategy.
     *
     * <p>Loads all APPLIED applications for the cycle, delegates to the
     * strategy to determine SELECTED/REJECTED outcomes, and persists
     * the updated application statuses.</p>
     *
     * @param cycleId  the admission cycle's ID
     * @param strategy the selection algorithm to apply
     * @return the list of all applications after selection (statuses updated)
     * @throws IllegalArgumentException if the cycle does not exist
     */
    List<Application> runSelection(String cycleId, SelectionStrategy strategy);
}
