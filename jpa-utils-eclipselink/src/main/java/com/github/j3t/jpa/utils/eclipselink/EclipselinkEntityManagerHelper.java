package com.github.j3t.jpa.utils.eclipselink;

import java.util.Collection;

import org.eclipse.persistence.internal.sessions.UnitOfWorkImpl;

import com.github.j3t.jpa.utils.core.AbstractEntityManagerHelper;

public class EclipselinkEntityManagerHelper extends AbstractEntityManagerHelper
{
    @SuppressWarnings("unchecked")
    public Collection<Object> getManagedEntities()
    {
        return entityManager.unwrap(UnitOfWorkImpl.class).getCloneMapping().keySet();
    }
}
