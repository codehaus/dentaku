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
package org.dentaku.gentaku;

import java.io.Serializable;
import java.util.Map;

public interface GentakuTag extends Serializable {
    /**
     * @return the tag name
     */
    String getName();

    /**
     * @return the full tag-value
     */
    String getValue();

    /**
     * @return an array of whitespace-separatedtag parameters
     */
    String[] getParameters();

    /**
     * @param key name of a named-parameter
     * @return the corresponding value,
     *         or null if no such named-parameter was present
     */
    String getNamedParameter(String key);

    /**
     * @return a Map containing all the named-parameters
     */
    Map getNamedParameterMap();

    /**
     * @return the line-number where the tag occurred
     */
    int getLineNumber();
}
