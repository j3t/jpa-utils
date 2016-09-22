package com.github.j3t.jpa.utils.core;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractEntityManagerHelper implements EntityManagerHelper
{
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractEntityManagerHelper.class);
    
    @PersistenceContext
    protected EntityManager entityManager;
    
    public void refresh()
    {
        LOGGER.debug("refresh");
        
        for (Object entity : getManagedEntities())
        {
            LOGGER.debug("entity: {}", entity);
            
            try
            {
                LOGGER.debug("refresh");
                entityManager.refresh(entity);
            }
            catch (EntityNotFoundException e)
            {
                LOGGER.debug("remove");
                entityManager.remove(entity);
            }
        }
    }
    
    public boolean existsInDatabase(Class<?> entityClass, Object primaryKey)
    {
        LOGGER.debug("existsInDatabase [entityClass={}, primaryKey={}]", entityClass, primaryKey);
        try
        {
            return entityManager.find(entityClass, primaryKey) != null;
        }
        catch (Exception e)
        {
        }
        LOGGER.debug("not found");
        return false;
    }

}
