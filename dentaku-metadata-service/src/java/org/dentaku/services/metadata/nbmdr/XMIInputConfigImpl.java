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

import org.dentaku.services.metadata.Utils;
import org.netbeans.api.xmi.XMIInputConfig;
import org.netbeans.api.xmi.XMIReferenceResolver;
import org.netbeans.lib.jmi.xmi.XmiContext;

import javax.jmi.reflect.RefPackage;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class XMIInputConfigImpl extends XMIInputConfig {
    private Collection paths = new ArrayList();
    private XMIReferenceResolverImpl xmiReferenceResolver;

    public XMIInputConfigImpl(RefPackage[] extents, Collection searchPaths) {
        for (Iterator it = searchPaths.iterator(); it.hasNext();) {
            Object o = (Object) it.next();
            if (o instanceof PathElement) {
                paths.add(((PathElement)o).getId());
            } else {
                paths.add(o);
            }
        }
        xmiReferenceResolver = new XMIReferenceResolverImpl(extents, this);
    }

    public void setReferenceResolver(XMIReferenceResolver resolver) {
        throw new UnsupportedOperationException();
    }

    public XMIReferenceResolver getReferenceResolver() {
        return xmiReferenceResolver;
    }

    public class XMIReferenceResolverImpl extends XmiContext {
        private Map urlCache = new HashMap();

        public XMIReferenceResolverImpl(RefPackage[] extents, XMIInputConfig config) {
            super(extents, config);
        }

        public URL toURL(String systemId) {
            URL result = (URL) urlCache.get(systemId);
            if (result == null) {
                result = Utils.checkURL(systemId);

                if (result == null) {
                    result = Utils.checkURL(getClass().getClassLoader().getResource(systemId));
                }

                if (result == null) {
                    paths.add(".");
                    String filename = systemId.substring(systemId.lastIndexOf("/") + 1);
                    String rootdir = Utils.getRootDir();
                    for (Iterator it = paths.iterator(); it.hasNext();) {
                        try {
                            String parent = (String) it.next();
                            result = Utils.checkURL(new File(parent, filename).toURL());
                            if (result != null) {
                                break;
                            }
                            result = Utils.checkURL(new File(rootdir + parent, filename).toURL());
                            if (result != null) {
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

                // try classpath
                if (result == null) {
                    result = super.toURL(systemId);
                }

                if (result != null) {
                    urlCache.put(systemId, result);
                }
            }

            return result;
        }

    }
}
