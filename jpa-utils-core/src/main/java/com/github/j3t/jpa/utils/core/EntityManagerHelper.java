
package com.github.j3t.jpa.utils.core;


import java.util.Collection;

public interface EntityManagerHelper
{
    /**
     * Refresh the state of all entities in the persistence context (LocalCache) from the database, overwriting changes
     * made to the entity, if any.
     */
    void refresh();

    /**
     * Check if an entity exists in the database.
     * 
     * @param entityClass the entity type
     * @param key the entity id
     * 
     * @return <code>true</code> if the entity exists in the database, otherwise <code>false</code>
     */
    boolean existsInDatabase(Class<?> entityClass, Object key);

    /**
     * Gets all managed entities from the persistence context.
     * 
     * @return {@link Collection} with entities or an empty {@link Collection}
     */
    Collection<Object> getManagedEntities();
}
