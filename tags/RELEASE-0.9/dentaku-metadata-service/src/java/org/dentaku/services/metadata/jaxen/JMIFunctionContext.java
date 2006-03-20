/*
 * JMIFunctionContext.java
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
//
//
//package org.dentaku.services.metadata.jaxen;
//
//import org.jaxen.function.BooleanFunction;
//import org.jaxen.function.CeilingFunction;
//import org.jaxen.function.ConcatFunction;
//import org.jaxen.function.ContainsFunction;
//import org.jaxen.function.CountFunction;
//import org.jaxen.function.FalseFunction;
//import org.jaxen.function.FloorFunction;
//import org.jaxen.function.IdFunction;
//import org.jaxen.function.LangFunction;
//import org.jaxen.function.LastFunction;
//import org.jaxen.function.LocalNameFunction;
//import org.jaxen.function.NameFunction;
//import org.jaxen.function.NamespaceUriFunction;
//import org.jaxen.function.NormalizeSpaceFunction;
//import org.jaxen.function.NotFunction;
//import org.jaxen.function.NumberFunction;
//import org.jaxen.function.PositionFunction;
//import org.jaxen.function.RoundFunction;
//import org.jaxen.function.StartsWithFunction;
//import org.jaxen.function.StringFunction;
//import org.jaxen.function.StringLengthFunction;
//import org.jaxen.function.SubstringAfterFunction;
//import org.jaxen.function.SubstringBeforeFunction;
//import org.jaxen.function.SubstringFunction;
//import org.jaxen.function.SumFunction;
//import org.jaxen.function.TranslateFunction;
//import org.jaxen.function.TrueFunction;
//import org.jaxen.function.ext.EndsWithFunction;
//import org.jaxen.function.ext.EvaluateFunction;
//import org.jaxen.function.ext.LowerFunction;
//import org.jaxen.function.ext.MatrixConcatFunction;
//import org.jaxen.function.ext.UpperFunction;
//import org.jaxen.function.xslt.DocumentFunction;
//import org.jaxen.SimpleFunctionContext;
//import org.jaxen.FunctionContext;
//
///** A <code>FunctionContext</code> implementing the core XPath
// *  function library, with extensions.
// *
// *  <p>
// *  The core XPath function library is provided through this
// *  implementation of <code>FunctionContext</code>.  Additionally,
// *  extension functions have been provided, as enumerated below.
// *  </p>
// *
// *  <p>
// *  This class implements a <i>Singleton</i> pattern (see {@link #getInstance}),
// *  as it is perfectly re-entrant and thread-safe.  If using the
// *  singleton, it is inadvisable to call {@link #registerFunction}
// *  as that will extend the global function context, affecting other
// *  users of the singleton.  But that's your call, really, now isn't
// *  it?  That may be what you really want to do.
// *  </p>
// *
// *  <p>
// *  Extension functions:
// *
// *  <ul>
// *     <li>matrix-concat(..)</li>
// *     <li>evaluate(..)</li>
// *  </ul>
// *
// *  @see org.jaxen.FunctionContext
// *
// *  @author <a href="mailto:bob@werken.com">bob mcwhirter</a>
// */
//public class JMIFunctionContext extends SimpleFunctionContext
//{
//   /** Singleton implementation.
//    */
//    private static class Singleton
//    {
//        /** Singleton instance.
//         */
//        private static JMIFunctionContext instance = new JMIFunctionContext();
//    }
//
//    /** Retrieve the singleton instance.
//     *
//     *  @return The singleton instance.
//     */
//    public static FunctionContext getInstance()
//    {
//        return Singleton.instance;
//    }
//
//    /** Construct.
//     *
//     *  <p>
//     *  Construct with all core XPath functions registered.
//     *  </p>
//     */
//    public JMIFunctionContext()
//    {
//        registerFunction( null,  // namespace URI
//                          "boolean",
//                          new BooleanFunction() );
//
//        registerFunction( null,  // namespace URI
//                          "ceiling",
//                          new CeilingFunction() );
//
//        registerFunction( null,  // namespace URI
//                          "concat",
//                          new ConcatFunction() );
//
//        registerFunction( null,  // namespace URI
//                          "contains",
//                          new ContainsFunction() );
//
//        registerFunction( null,  // namespace URI
//                          "count",
//                          new CountFunction() );
//
//        registerFunction( null,  // namespace URI
//                          "document",
//                          new DocumentFunction() );
//
//        registerFunction( null,  // namespace URI
//                          "false",
//                          new FalseFunction() );
//
//        registerFunction( null,  // namespace URI
//                          "floor",
//                          new FloorFunction() );
//
//        registerFunction( null,  // namespace URI
//                          "id",
//                          new IdFunction() );
//
//        registerFunction( null,  // namespace URI
//                          "lang",
//                          new LangFunction() );
//
//        registerFunction( null,  // namespace URI
//                          "last",
//                          new LastFunction() );
//
//        registerFunction( null,  // namespace URI
//                          "local-name",
//                          new LocalNameFunction() );
//
//        registerFunction( null,  // namespace URI
//                          "name",
//                          new NameFunction() );
//
//        registerFunction( null,  // namespace URI
//                          "namespace-uri",
//                          new NamespaceUriFunction() );
//
//        registerFunction( null,  // namespace URI
//                          "normalize-space",
//                          new NormalizeSpaceFunction() );
//
//        registerFunction( null,  // namespace URI
//                          "not",
//                          new NotFunction() );
//
//        registerFunction( null,  // namespace URI
//                          "number",
//                          new NumberFunction() );
//
//        registerFunction( null,  // namespace URI
//                          "position",
//                          new PositionFunction() );
//
//        registerFunction( null,  // namespace URI
//                          "round",
//                          new RoundFunction() );
//
//        registerFunction( null,  // namespace URI
//                          "starts-with",
//                          new StartsWithFunction() );
//
//        registerFunction( null,  // namespace URI
//                          "string",
//                          new StringFunction() );
//
//        registerFunction( null,  // namespace URI
//                          "string-length",
//                          new StringLengthFunction() );
//
//        registerFunction( null,  // namespace URI
//                          "substring-after",
//                          new SubstringAfterFunction() );
//
//        registerFunction( null,  // namespace URI
//                          "substring-before",
//                          new SubstringBeforeFunction() );
//
//        registerFunction( null,  // namespace URI
//                          "substring",
//                          new SubstringFunction() );
//
//        registerFunction( null,  // namespace URI
//                          "sum",
//                          new SumFunction() );
//
//        registerFunction( null,  // namespace URI
//                          "true",
//                          new TrueFunction() );
//
//        registerFunction( null,  // namespace URI
//                          "translate",
//                          new TranslateFunction() );
//
//
//        // register extension functions
//        // extension functions should go into a namespace, but which one?
//        // for now, keep them in default namespace to not to break any code
//
//        registerFunction( null,  // namespace URI
//                          "matrix-concat",
//                          new MatrixConcatFunction() );
//
//        registerFunction( null,  // namespace URI
//                          "evaluate",
//                          new EvaluateFunction() );
//
//        registerFunction( null,  // namespace URI
//                          "lower-case",
//                          new LowerFunction() );
//
//        registerFunction( null,  // namespace URI
//                          "upper-case",
//                          new UpperFunction() );
//
//        registerFunction( null,  // namespace URI
//                          "ends-with",
//                          new EndsWithFunction() );
//
//    }
//}
