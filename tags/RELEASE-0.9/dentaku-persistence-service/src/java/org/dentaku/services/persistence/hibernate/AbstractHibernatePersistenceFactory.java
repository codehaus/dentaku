/*
 * AbstractHibernatePersistenceFactory.java
 * Copyright 2002-2004 Bill2, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dentaku.services.persistence.hibernate;

import org.dentaku.services.persistence.Entity;
import org.dentaku.services.persistence.PersistenceException;
import org.dentaku.services.persistence.PersistenceFactory;

public abstract class AbstractHibernatePersistenceFactory implements PersistenceFactory {
    public abstract Entity create() throws PersistenceException;

    public abstract Entity create(Object source) throws PersistenceException;

    public abstract Entity findByPrimaryKey(Long pk) throws PersistenceException;
}
