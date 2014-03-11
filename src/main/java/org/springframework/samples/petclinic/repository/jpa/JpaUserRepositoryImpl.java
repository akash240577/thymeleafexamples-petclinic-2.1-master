/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.repository.jpa;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Collection;

/**
 * JPA implementation of the {@link org.springframework.samples.petclinic.repository.UserRepository} interface.
 *
 * @author Mike Keith
 * @author Rod Johnson
 * @author Sam Brannen
 * @author Michael Isvy
 * @since 22.4.2006
 */
@Repository
public class JpaUserRepositoryImpl implements UserRepository {

    @PersistenceContext
    private EntityManager em;


    /**
     * Important: in the current version of this method, we load Users with all their Pets and Visits while
     * we do not need Visits at all and we only need one property from the Pet objects (the 'name' property).
     * There are some ways to improve it such as:
     * - creating a Ligtweight class (example here: https://community.jboss.org/wiki/LightweightClass)
     * - Turning on lazy-loading and using {@link org.springframework.orm.hibernate3.support.OpenSessionInViewFilter}
     */
    @SuppressWarnings("unchecked")
    public Collection<User> findByLastName(String lastName) {
        // using 'join fetch' because a single query should load both users and pets
        // using 'left join fetch' because it might happen that an user does not have pets yet
        Query query = this.em.createQuery("SELECT DISTINCT user FROM User user left join fetch user.pets WHERE user.lastName LIKE :lastName");
        query.setParameter("lastName", lastName + "%");
        return query.getResultList();
    }

    public User findByUserName(String username) {
        Query query = this.em.createQuery("SELECT DISTINCT user FROM User user WHERE user.username = :username");
        query.setParameter("username", username);
        return (User) query.getSingleResult();
    }

    @Override
    public User findById(int id) {
        // using 'join fetch' because a single query should load both users and pets
        // using 'left join fetch' because it might happen that an user does not have pets yet
        Query query = this.em.createQuery("SELECT user FROM User user left join fetch user.pets WHERE user.id =:id");
        query.setParameter("id", id);
        return (User) query.getSingleResult();
    }


    @Override
    public void save(User user) {
        User mergedUser = this.em.merge(user);
        user.setId(mergedUser.getId());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
        return findByUserName(username);
    }
}
