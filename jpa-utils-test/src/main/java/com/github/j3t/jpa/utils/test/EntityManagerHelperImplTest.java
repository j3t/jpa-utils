
package com.github.j3t.jpa.utils.test;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.github.j3t.jpa.utils.core.EntityManagerHelper;
import com.github.j3t.jpa.utils.test.domain.Address;
import com.github.j3t.jpa.utils.test.domain.User;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * This class contains all basic tests against the test domain. The test method names follows the
 * <a href="http://martinfowler.com/bliki/GivenWhenThen.html">given_when_then pattern</a>.
 * 
 * @author j3t
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ScanClasspathConfig.class})
public class EntityManagerHelperImplTest extends AbstractTransactionalJUnit4SpringContextTests
{
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private EntityManagerHelper entityManagerHelper;

    @Test
    public void givenDatabaseIsCreated_whenNotChanged_thenCountOfUsersShouldBeThree() throws Exception
    {
        assertEquals(3, entityManager.createQuery("select u from User u").getResultList().size());
    }

    @Test
    public void givenDatabaseIsCreated_whenNotChanged_thenCountOfGroupsShouldBeTwo() throws Exception
    {
        assertEquals(2, entityManager.createQuery("select g from Group g").getResultList().size());
    }

    @Test
    public void givenDatabaseIsCreated_whenNotChanged_thenCountOfAddressesShouldBeFour() throws Exception
    {
        assertEquals(4, entityManager.createQuery("select a from Address a").getResultList().size());
    }

    @Test
    public void givenDatabaseIsCreated_whenNotChanged_thenCountOfProfilesShouldBeOne() throws Exception
    {
        assertEquals(1, entityManager.createQuery("select p from Profile p").getResultList().size());
    }
    
    @Test
    public void givenDatabaseIsCreated_whenNotChanged_thenCountOfContactsShouldBeOne() throws Exception
    {
        assertEquals(1, entityManager.createQuery("select c from Contact c").getResultList().size());
    }

    @Test
    public void givenDatabaseIsCreated_whenNotChanged_thenBobExistsAsExpected() throws Exception
    {
        User user = entityManager.find(User.class, 1L);

        assertEquals("Bob", user.getName());
        assertEquals(1, user.getGroups().size());
        assertEquals("Developer", user.getGroups().get(0).getName());
        assertEquals(3, user.getAddress().getId());
        assertNull(user.getProfile());
        assertEquals(0, user.getContacts().size());
    }

    @Test
    public void givenDatabaseIsCreated_whenNotChanged_thenMartinExistsAsExpected() throws Exception
    {
        User user = entityManager.find(User.class, 2L);

        assertEquals("Martin", user.getName());
        assertEquals(2, user.getGroups().size());
        assertEquals("Developer", user.getGroups().get(0).getName());
        assertEquals("Requirements Engineer", user.getGroups().get(1).getName());
        assertEquals(1, user.getAddress().getId());
        assertNull(user.getProfile());
        assertEquals(1, user.getContacts().size());
    }

    @Test
    public void givenDatabaseIsCreated_whenNotChanged_thenJoelExistsAsExpected() throws Exception
    {
        User user = entityManager.find(User.class, 3L);

        assertEquals("Joel", user.getName());
        assertEquals(1, user.getGroups().size());
        assertEquals("Requirements Engineer", user.getGroups().get(0).getName());
        assertEquals(2, user.getAddress().getId());
        assertEquals(1, user.getProfile().getId());
        assertEquals(0, user.getContacts().size());
    }

    @Test
    public void givenEntityIsUpdated_whenEntityManagerNotFlushed_thenRowShouldNotChanged() throws Exception
    {
        User user = entityManager.find(User.class, 1L);
        user.setName("Alex");

        assertEquals(0, countRowsInTableWhere("users", "name = 'Alex'"));
    }

    @Test
    public void givenEntityIsUpdated_whenEntityManagerIsFlushed_thenRowShouldChanged() throws Exception
    {
        User user = entityManager.find(User.class, 1L);
        user.setName("Alex");

        entityManager.flush();

        assertEquals(1, countRowsInTableWhere("users", "name = 'Alex'"));
    }

    @Test
    public void givenEntityHasChanged_whenEntityIsNotRefreshed_thenEntityShouldNotChanged() throws Exception
    {
        User user = entityManager.find(User.class, 1L);

        jdbcTemplate.update("update users set name = 'Alex' where id = 1");

        assertEquals("Bob", user.getName());
    }

    @Test
    public void givenEntityHasChanged_whenEntityIsRefreshed_thenEntityShouldChanged() throws Exception
    {
        User user = entityManager.find(User.class, 1L);

        jdbcTemplate.update("update users set name = 'Alex' where id = 1");
        entityManagerHelper.refresh();

        assertEquals("Alex", user.getName());
    }

    @Test
    public void givenEntityRelationshipHasChanged_whenEntityIsNotRefreshed_thenEntityRelationshipShouldNotChanged() throws Exception
    {
        User user = entityManager.find(User.class, 1L);
        user.getGroups().size();

        jdbcTemplate.update("delete from group_has_users where group_id = 1 and user_id = 1");

        assertEquals(1, user.getGroups().size());
    }

    @Test
    public void givenEntityRelationshipHasChanged_whenEntityIsRefreshed_thenEntityRelationshipShouldChanged() throws Exception
    {
        User user = entityManager.find(User.class, 1L);
        user.getGroups().size();

        jdbcTemplate.update("delete from group_has_users where group_id = 1 and user_id = 1");
        entityManagerHelper.refresh();

        assertEquals(0, user.getGroups().size());
    }

    @Test
    public void givenEntityWasRemoved_whenEntityIsNotRefreshed_thenEntityShouldExists() throws Exception
    {
        User user = entityManager.find(User.class, 1L);

        jdbcTemplate.update("delete from group_has_users where user_id = 1");
        jdbcTemplate.update("delete from users where id = 1");

        assertTrue(entityManager.contains(user));
    }

    @Test
    public void givenEntityWasRemoved_whenEntityIsRefreshed_thenEntityShouldNotExists() throws Exception
    {
        User user = entityManager.find(User.class, 1L);

        jdbcTemplate.update("delete from group_has_users where user_id = 1");
        jdbcTemplate.update("delete from users where id = 1");
        entityManagerHelper.refresh();

        assertFalse(entityManager.contains(user));
    }

    @Test
    public void givenEntityManyToManyRelationshipWasRemoved_whenEntityIsNotRefreshed_thenEntityRelationshipShouldExists() throws Exception
    {
        User user = entityManager.find(User.class, 2L);
        user.getGroups().size();

        jdbcTemplate.update("delete from group_has_users where group_id = 1");

        assertEquals(2, user.getGroups().size());
    }

    @Test
    public void givenEntityManyToManyRelationshipWasRemoved_whenEntityIsRefreshed_thenEntityRelationshipShouldNotExists() throws Exception
    {
        User user = entityManager.find(User.class, 2L);
        user.getGroups().size();

        jdbcTemplate.update("delete from group_has_users where group_id = 1");
        entityManagerHelper.refresh();

        assertEquals(1, user.getGroups().size());
    }

    @Test
    public void givenEntityExistsInDatabase_whenEntityIsNotRemovedfromDatabase_thenExistsInDatabaseShouldReturnTrue() throws Exception
    {
        assertTrue(entityManagerHelper.existsInDatabase(Address.class, 4L));
    }

    @Test
    public void givenEntityExistsInDatabase_whenEntityIsRemovedFromDatabase_thenExistsInDatabaseShouldReturnFalse() throws Exception
    {
        jdbcTemplate.update("delete from addresses where id = 4");

        assertFalse(entityManagerHelper.existsInDatabase(Address.class, 4L));
    }

    @Test
    public void givenEntityOneToOneRelationshipHasChanged_whenEntityIsNotRefreshed_thenEntityRelationshipShouldNotChanged() throws Exception
    {
        User user = entityManager.find(User.class, 1L);

        jdbcTemplate.update("update users set address_id = null");

        assertNotNull(user.getAddress());
    }

    @Test
    public void givenEntityOneToOneRelationshipHasChanged_whenEntityIsRefreshed_thenEntityRelationshipShouldChanged() throws Exception
    {
        User user = entityManager.find(User.class, 1L);

        jdbcTemplate.update("update users set address_id = null");
        entityManagerHelper.refresh();

        assertNull(user.getAddress());
    }

    @Test
    public void givenEntityOneToOneMappedByRelationshipHasChanged_whenEntityIsNotRefreshed_thenEntityRelationshipShouldNotChanged() throws Exception
    {
        User user = entityManager.find(User.class, 3L);

        jdbcTemplate.update("delete from profiles");

        assertNotNull(user.getProfile());
    }

    @Test
    public void givenEntityOneToOneMappedByRelationshipHasChanged_whenEntityIsRefreshed_thenEntityRelationshipShouldChanged() throws Exception
    {
        User user = entityManager.find(User.class, 3L);

        jdbcTemplate.update("delete from profiles");
        entityManagerHelper.refresh();

        assertNull(user.getProfile());
    }
    
    @Test
    public void givenEntityOneToManyRelationshipHasChanged_whenEntityIsNotRefreshed_thenEntityRelationshipShouldNotChanged() throws Exception
    {
        User user = entityManager.find(User.class, 2L);
        user.getContacts().size();

        jdbcTemplate.update("delete from contacts");

        assertEquals(1, user.getContacts().size());
    }
    
    @Test
    public void givenEntityOneToManyRelationshipHasChanged_whenEntityIsRefreshed_thenEntityRelationshipShouldChanged() throws Exception
    {
        User user = entityManager.find(User.class, 2L);
        user.getContacts().size();

        jdbcTemplate.update("delete from contacts");
        entityManagerHelper.refresh();

        assertEquals(0, user.getContacts().size());
    }
}
