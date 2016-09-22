package com.github.j3t.jpa.utils.openjpa;

import java.util.Collection;

import org.apache.openjpa.persistence.OpenJPAEntityManager;
import org.apache.openjpa.persistence.OpenJPAPersistence;

import com.github.j3t.jpa.utils.core.AbstractEntityManagerHelper;

public class OpenJPAEntityManagerHelper extends AbstractEntityManagerHelper
{
    @SuppressWarnings("unchecked")
    public Collection<Object> getManagedEntities()
    {
        OpenJPAEntityManager oem = OpenJPAPersistence.cast(entityManager);
        
        return oem.getManagedObjects();
    }

}
