/*
 * DentakuException.java
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
///CLOVER:OFF
package org.dentaku.services.exception;

public class DentakuException extends Exception {
    public DentakuException() {
        super();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public DentakuException(String message) {
        super(message);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public DentakuException(Throwable cause) {
        super(cause);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public DentakuException(String message, Throwable cause) {
        super(message, cause);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
