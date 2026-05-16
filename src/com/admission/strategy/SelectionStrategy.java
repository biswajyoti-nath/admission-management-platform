package com.admission.strategy;

import com.admission.model.Application;

import java.util.List;

/**
 * Strategy interface for admission selection algorithms.
 *
 * <p>Encapsulates the selection logic — given a list of APPLIED applications
 * and an available seat count, determines which applications are SELECTED
 * and which are REJECTED.</p>
 *
 * <p><b>OCP:</b> New selection algorithms (merit-based, lottery, weighted)
 * are added by implementing this interface — no existing code changes.</p>
 *
 * <p><b>LSP:</b> All implementations are substitutable — SelectionService
 * treats every strategy identically.</p>
 *
 * <p><b>SRP:</b> Each implementation encapsulates exactly one selection
 * algorithm.</p>
 */
public interface SelectionStrategy {

    /**
     * Selects applications from the candidate list based on the algorithm.
     *
     * <p>Contract:</p>
     * <ul>
     *   <li>Input applications should have status APPLIED</li>
     *   <li>The top {@code seatCount} candidates are set to SELECTED</li>
     *   <li>All remaining candidates are set to REJECTED</li>
     *   <li>The returned list contains ALL applications with updated statuses</li>
     * </ul>
     *
     * @param applications the list of APPLIED applications to evaluate
     * @param seatCount    the number of seats available for selection
     * @return the same list with statuses updated to SELECTED or REJECTED
     */
    List<Application> select(List<Application> applications, int seatCount);
}
