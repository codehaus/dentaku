/**
 *
 *  Copyright 2004 Brian Topping
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.dentaku.services.persistence.tranql;

import java.util.Collection;
import java.io.Serializable;

import org.tranql.field.FieldTransform;
import org.tranql.field.FieldTransformException;
import org.tranql.field.Row;
import org.tranql.ql.QueryException;
import org.tranql.query.ResultHandler;

/**
 * 
 * 
 * @version $Revision$ $Date$
 */
public class InstantiatingCollectionResultHandler implements ResultHandler, Serializable {
    private final FieldTransform accessor;

    public InstantiatingCollectionResultHandler(FieldTransform accessor) {
        this.accessor = accessor;
    }

    public Object fetched(Row row, Object arg) throws QueryException {
        Collection results = (Collection) arg;
        try {
            results.add(accessor.get(row));
            return arg;
        } catch (FieldTransformException e) {
            throw new QueryException(e);
        }
    }
}
