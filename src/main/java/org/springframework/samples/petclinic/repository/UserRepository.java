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
package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.annotation.Transactional;

/**
 * Repository class for <code>User</code> domain objects All method names are compliant with Spring Data naming
 * conventions so this interface can easily be extended for Spring Data See here: http://static.springsource.org/spring-data/jpa/docs/current/reference/html/jpa.repositories.html#jpa.query-methods.query-creation
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @author Michael Isvy
 */
public interface UserRepository extends UserDetailsService{

    /**
     * Retrieve <code>User</code>s from the data store by last name, returning all users whose last name <i>starts</i>
     * with the given name.
     *
     * @param lastName Value to search for
     * @return a <code>Collection</code> of matching <code>User</code>s (or an empty <code>Collection</code> if none
     *         found)
     */
    Collection<User> findByLastName(String lastName) throws DataAccessException;

    User findByUserName(String userName) throws DataAccessException;

    /**
     * Retrieve an <code>User</code> from the data store by id.
     *
     * @param id the id to search for
     * @return the <code>User</code> if found
     * @throws org.springframework.dao.DataRetrievalFailureException
     *          if not found
     */
    User findById(int id) throws DataAccessException;


    /**
     * Save an <code>User</code> to the data store, either inserting or updating it.
     *
     * @param user the <code>User</code> to save
     * @see org.springframework.samples.petclinic.model.BaseEntity#isNew
     */
    @Transactional
    void save(User user) throws DataAccessException;

}
