package com.admission.repository;

import java.util.List;
import java.util.Optional;

/**
 * Generic base repository interface defining core CRUD operations.
 *
 * <p>All entity-specific repositories extend this interface, inheriting
 * a consistent contract for persistence operations. The interface is
 * storage-agnostic — implementations may use CSV, JSON, databases, or
 * in-memory stores without affecting consumers.</p>
 *
 * <p><b>SOLID:</b> Supports OCP (new entity repos extend without modifying base),
 * LSP (all implementations are substitutable), and DIP (services depend on
 * this abstraction, not concrete storage).</p>
 *
 * @param <T> the domain entity type managed by this repository
 */
public interface Repository<T> {

    /**
     * Persists a new entity and returns it with a generated ID.
     *
     * @param entity the entity to save (ID may be null; will be assigned)
     * @return the saved entity with its generated ID
     */
    T save(T entity);

    /**
     * Finds an entity by its unique identifier.
     *
     * @param id the entity's unique ID (e.g., "STU-0001")
     * @return an Optional containing the entity if found, or empty
     */
    Optional<T> findById(String id);

    /**
     * Returns all entities of this type.
     *
     * @return a list of all entities; empty list if none exist
     */
    List<T> findAll();

    /**
     * Replaces all persisted entities with the provided list.
     *
     * <p>This is the primary mechanism for bulk updates. The entire dataset
     * for this entity type is overwritten with the given list.</p>
     *
     * @param entities the complete list of entities to persist
     */
    void updateAll(List<T> entities);
}
