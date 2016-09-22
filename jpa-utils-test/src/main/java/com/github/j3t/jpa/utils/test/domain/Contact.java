package com.github.j3t.jpa.utils.test.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "contacts")
public class Contact
{
    @Id
    private long id;
    
    private String data;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public long getId()
    {
        return this.id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getData()
    {
        return this.data;
    }

    public void setData(String data)
    {
        this.data = data;
    }
    
    public User getUser()
    {
        return this.user;
    }
    
    public void setUser(User user)
    {
        this.user = user;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Contact other = (Contact) obj;
        if (id != other.id) return false;
        return true;
    }
    
}
