/*
 * AbstractJavaGeneratingPluginTestBase.java
 * Copyright 2004-2004 Bill2, Inc.
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
package org.dentaku.gentaku;

import java.io.IOException;
import java.net.URL;

import org.generama.astunit.ASTTestCase;
import org.generama.tests.AbstractPluginTestCase;

import antlr.RecognitionException;
import antlr.TokenStreamException;

import com.thoughtworks.qdox.junit.APITestCase;

/**
 * Baseclass for testing generation of Java sources. Uses QDox'
 * APITestCase internally to compare equality of Java Sources.
 *
 */
public abstract class AbstractJavaGeneratingPluginTestBase extends AbstractPluginTestCase {

    protected final void compare(URL expected, URL actual) throws RecognitionException, TokenStreamException, IOException {
        APITestCase.assertApiEquals(expected, actual);
        ASTTestCase.assertAstEquals(expected, actual);
    }

    protected URL getTestSource() throws IOException {
        return null;
    }
}
