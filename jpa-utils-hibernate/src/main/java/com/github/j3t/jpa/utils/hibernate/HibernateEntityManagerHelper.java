package com.github.j3t.jpa.utils.hibernate;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map.Entry;

import org.hibernate.engine.spi.EntityEntry;
import org.hibernate.engine.spi.PersistenceContext;
import org.hibernate.engine.spi.SessionImplementor;

import com.github.j3t.jpa.utils.core.AbstractEntityManagerHelper;

public class HibernateEntityManagerHelper extends AbstractEntityManagerHelper
{
    public Collection<Object> getManagedEntities()
    {
        Collection<Object> managedEntities = new LinkedList<Object>();

        SessionImplementor session = entityManager.unwrap(SessionImplementor.class);
        PersistenceContext pc = session.getPersistenceContext();
        
        for ( Entry<Object, EntityEntry> reentrantSafeEntityEntry : pc.reentrantSafeEntityEntries())
            managedEntities.add(reentrantSafeEntityEntry.getKey());
        
        return managedEntities;
    }

}
