
package com.github.j3t.jpa.utils.test.domain;


import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class User
{
    @Id
    private long id;
    
    private String name;
    
    private String email;
    
    @ManyToMany
    @JoinTable(
            name = "group_has_users",
            joinColumns = @JoinColumn(name="user_id", referencedColumnName="id"),
            inverseJoinColumns= @JoinColumn(name="group_id", referencedColumnName="id"))
    private List<Group> groups;

    @OneToOne
    @JoinColumn(name = "address_id")
    private Address address;
    
    @OneToOne(mappedBy = "user")
    private Profile profile;
    
    @OneToMany(mappedBy = "user")
    private List<Contact> contacts;

    public long getId()
    {
        return this.id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getEmail()
    {
        return this.email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public List<Group> getGroups()
    {
        return this.groups;
    }

    public void setGroups(List<Group> groups)
    {
        this.groups = groups;
    }

    public Address getAddress()
    {
        return this.address;
    }
    
    public void setAddress(Address address)
    {
        this.address = address;
    }
    
    public Profile getProfile()
    {
        return this.profile;
    }

    public void setProfile(Profile profile)
    {
        this.profile = profile;
    }

    public List<Contact> getContacts()
    {
        return this.contacts;
    }
    
    public void setContacts(List<Contact> contacts)
    {
        this.contacts = contacts;
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
        User other = (User) obj;
        if (id != other.id) return false;
        return true;
    }

}
