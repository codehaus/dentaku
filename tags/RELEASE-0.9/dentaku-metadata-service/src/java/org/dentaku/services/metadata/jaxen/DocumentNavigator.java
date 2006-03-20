/*
 * DocumentNavigator.java
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
 *////*
// * Copyright (c) 2004 Your Corporation. All Rights Reserved.
// */
//package org.dentaku.services.metadata.jaxen;
//
//import org.jaxen.Navigator;
//import org.jaxen.javabean.Element;
//import org.jaxen.javabean.ElementIterator;
//import org.jaxen.util.SingleObjectIterator;
//
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//import java.util.Collection;
//import java.util.Iterator;
//
///**
// *  DocumentNavigator implementation that has additional check ignoring bean semantics.
// */
//public class DocumentNavigator extends org.jaxen.javabean.DocumentNavigator {
//    /**
//     * Empty Class array.
//     */
//    private static final Class[] EMPTY_CLASS_ARRAY = new Class[0];
//    /**
//     * Empty Object array.
//     */
//    private static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];
//
//    /**
//     * Singleton implementation.
//     */
//    private static class Singleton {
//        /**
//         * Singleton instance.
//         */
//        private static DocumentNavigator instance = new DocumentNavigator();
//    }
//
//    /**
//     * Retrieve the singleton instance of this <code>DocumentNavigator</code>.
//     */
//    public static Navigator getInstance() {
//        return Singleton.instance;
//    }
//
//    /**
//     * Retrieves an <code>Iterator</code> over the child elements that
//     * match the supplied name.
//     *
//     * @param contextNode     the origin context node
//     * @param localName       the local name of the children to return, always present
//     * @param namespacePrefix the prefix of the namespace of the children to return
//     * @param namespaceURI    the uri of the namespace of the children to return
//     * @return an Iterator that traverses the named children, or null if none
//     */
//    public Iterator getChildAxisIterator(Object contextNode, String localName, String namespacePrefix, String namespaceURI) {
//        Class cls = ((Element) contextNode).getObject().getClass();
//
//        String methodName = javacase(localName);
//
//        Method method = null;
//
//        try {
//            method = cls.getMethod("get" + methodName, EMPTY_CLASS_ARRAY);
//        } catch (NoSuchMethodException e) {
//            try {
//                method = cls.getMethod("get" + methodName + "s", EMPTY_CLASS_ARRAY);
//            } catch (NoSuchMethodException ee) {
//                try {
//                    method = cls.getMethod(localName, EMPTY_CLASS_ARRAY);
//                } catch (NoSuchMethodException eee) {
//                    method = null;
//                }
//            }
//        }
//
//        if (method == null) {
//            return null;
//        }
//
//        try {
//            Object result = method.invoke(((Element) contextNode).getObject(), EMPTY_OBJECT_ARRAY);
//
//            if (result == null) {
//                return null;
//            }
//
//            if (result instanceof Collection) {
//                return new ElementIterator((Element) contextNode, localName, ((Collection) result).iterator());
//            }
//
//            if (result.getClass().isArray()) {
//                return null;
//            }
//
//            return new SingleObjectIterator(new Element((Element) contextNode, localName, result));
//        } catch (IllegalAccessException e) {
//            // swallow
//        } catch (InvocationTargetException e) {
//            // swallow
//        }
//
//        return null;
//    }
//}
