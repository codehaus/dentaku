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

import org.tranql.query.CommandFactory;
import org.tranql.query.QueryCommand;
import org.tranql.query.UpdateCommand;
import org.tranql.field.FieldTransform;
import org.tranql.ql.QueryException;
import org.tranql.ql.Query;
import org.tranql.ql.QueryBinding;

public class DentakuCommandFactory implements CommandFactory {
    public QueryCommand createQuery(String query, FieldTransform[] paramTransforms, FieldTransform[] resultTransforms) throws QueryException {
        throw new UnsupportedOperationException("Not implemented");
    }

    public QueryCommand createQuery(String query, QueryBinding[] paramTransforms, QueryBinding[] resultTransforms) throws QueryException {
        throw new UnsupportedOperationException("Not implemented");
    }

    public QueryCommand createQuery(Query query) throws QueryException {
        return new DentakuQueryCommand(query);
    }

    public UpdateCommand createUpdate(Query query) throws QueryException {
        return new DentakuUpdateCommand(query);
    }
}
