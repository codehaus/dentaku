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
package org.dentaku.services.metadata.nbmdr;

import org.netbeans.api.xmi.XMIInputConfig;
import org.netbeans.api.xmi.XMIReferenceResolver;
import org.netbeans.lib.jmi.xmi.XmiContext;

import javax.jmi.reflect.RefPackage;
import java.util.Map;
import java.util.HashMap;
import java.util.Collection;
import java.util.Iterator;
import java.net.URL;
import java.net.MalformedURLException;
import java.io.File;

public class XMIInputConfigImpl extends XMIInputConfig {
    private Collection paths;
    private XMIReferenceResolverImpl xmiReferenceResolver;

    public XMIInputConfigImpl(RefPackage[] extents, Collection searchPaths) {
        this.paths = searchPaths;
        xmiReferenceResolver = new XMIReferenceResolverImpl(extents, this);
    }

    public void setReferenceResolver(XMIReferenceResolver resolver) {
        throw new UnsupportedOperationException();
    }

    public XMIReferenceResolver getReferenceResolver() {
        return xmiReferenceResolver;
    }

    public class XMIReferenceResolverImpl extends XmiContext {
        public XMIReferenceResolverImpl(RefPackage[] extents, XMIInputConfig config) {
            super(extents, config);
        }

        public URL toURL(String systemId) {
            URL result = super.toURL(systemId);

            // try classpath
            if (result == null) {
                result = getClass().getClassLoader().getResource(systemId);
            }

            if (result == null) {
                paths.add(new PathElement("."));
                for (Iterator it = paths.iterator(); it.hasNext();) {
                    try {
                        String parent = ((PathElement) it.next()).getId();
                        File f = new File(parent, systemId);
                        if (f.exists()) {
                            result = f.toURL();
                            break;
                        }
                    } catch (MalformedURLException e) { }
                }
            }

            if (result != null) {
                String fullname = result.toExternalForm();
                if (fullname.endsWith(".zip")) {
                    String filename = fullname.substring(fullname.lastIndexOf("/") + 1, fullname.indexOf(".zip"));
                    try {
                        result = new URL("jar:" + result + "!/" + filename);
                    } catch (MalformedURLException e) {
                        result = null;
                    }
                }
            }
            return result;
        }
    }
 }
