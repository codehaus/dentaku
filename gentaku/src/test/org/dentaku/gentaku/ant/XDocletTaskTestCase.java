/*
 * XDocletTaskTestCase.java
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
package org.dentaku.gentaku.ant;

/**
 * @foo bla bla
 * @author Aslak Helles&oslash;y
 * @version $Revision$
 */
public class XDocletTaskTestCase /*extends AbstractPicoContainerTaskTestCase*/ {
//MAKE ABSTRACT
//REMOVE REDUNDANT ASSERTS WITH XDOCLETTESTCASE. TEST ONLY ANT SPECIFIC THING
//THERE ARE LOTS OF GOOD ASSERTS THOUGH LIKE NO UNKNOWN TAGS
//    FIX THE FILE SUBS IN THE CONTENT!?!?!


//    protected PicoContainerTask createPicoContainerTask() {
//        return new GentakuTask();
//    }
//
//    public void testXDocletTasks() throws PicoInitializationException {
//        String basedir = System.getProperty("gentaku.home");
//        assertNotNull("The gentaku.home system property should be defined when tests are run", basedir);
//
//        Component test = new Component();
//        test.setClassname(SingleOutputPlugin.class.getName());
//        test.setDynamicAttribute("multioutput", "true");
//
//        test.setDynamicAttribute("destdir", basedir + "/target/test-output");
//
//        task.addConfiguredComponent(test);
//
//        // Make a fileset containing this very source
//        FileSet fileSet = new FileSet();
//        File testRoot = new File(basedir, "src/test/java");
//        fileSet.setDir(testRoot);
//        fileSet.setIncludes("**/XDocletTaskTestCase.java");
//        ((GentakuTask)task).addFileset(fileSet);
//
//        GentakuTask.Tag foo = ((GentakuTask)task).createTag();
//        foo.setName("foo");
//
//        task.execute();
//
//        // test that sources were parsed NOT NEEDED?
//        QDoxMetadataProvider qDoxMetadataProvider = (QDoxMetadataProvider) task.getPicoContainer().getComponentInstance(QDoxMetadataProvider.class);
//        assertNotNull(qDoxMetadataProvider);
//        Collection metadata = qDoxMetadataProvider.getMetadata();
//        assertEquals(1, metadata.size());
//
//        JavaClass thisClass = (JavaClass) metadata.toArray()[0];
//        assertEquals(getClass().getName(), thisClass.getFullyQualifiedName());
//
//        // test that the plugin was executed
//        SingleOutputPlugin testPlugin = (SingleOutputPlugin) task.getPicoContainer().getComponentInstance(SingleOutputPlugin.class);
//        assertTrue(testPlugin.wasExecuted);
//
//        // test that we do NOT get an unknown tag for the foo tag
//        ConfigurableDocletTagFactory tagFactory = (ConfigurableDocletTagFactory) task.getPicoContainer().getComponentInstance(ConfigurableDocletTagFactory.class);
//        assertEquals(0, tagFactory.getUnknownTags().size());
//    }
}
