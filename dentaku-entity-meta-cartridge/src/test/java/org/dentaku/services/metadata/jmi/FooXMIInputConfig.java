package org.dentaku.services.metadata.jmi;

import java.io.IOException;
import java.util.Collection;

import javax.jmi.reflect.RefObject;
import javax.jmi.reflect.RefPackage;
import javax.jmi.xmi.MalformedXMIException;

import org.netbeans.api.xmi.XMIInputConfig;
import org.netbeans.api.xmi.XMIReferenceResolver;

public class FooXMIInputConfig extends XMIInputConfig {

    private MyXMIReferenceResolver x = new MyXMIReferenceResolver();

    public void setReferenceResolver(XMIReferenceResolver x) {
    }

    public XMIReferenceResolver getReferenceResolver() {
        return this.x;
    }

    class MyXMIReferenceResolver implements XMIReferenceResolver {

        public void register(String arg0, String arg1, RefObject arg2) {
        }

        public void resolve(Client arg0, RefPackage arg1, String arg2, XMIInputConfig arg3, Collection arg4) throws MalformedXMIException, IOException {
        }

    }
}