package org.dentaku.services.metadata.jmi;

import java.net.URL;
import java.util.Iterator;

import javax.jmi.reflect.RefPackage;

import junit.framework.TestCase;

import org.netbeans.api.mdr.MDRManager;
import org.netbeans.api.mdr.MDRepository;
import org.netbeans.api.xmi.XMIReader;
import org.netbeans.api.xmi.XMIReaderFactory;


public class JMITest extends TestCase {

    
    public void testJMI() throws Exception {
        MDRepository rep = MDRManager.getDefault().getDefaultRepository();

        RefPackage rootPackage = rep.getExtent("Component View");
        
        System.out.println(rootPackage.getClass());
        System.out.println("------------------------------------------------");
        
        XMIReader x = XMIReaderFactory.getDefault().createXMIReader(new FooXMIInputConfig());
        
        URL model = JMITest.class.getResource("/org/dentaku/services/metadata/jmi/model.xml");
        assertNotNull(model);
        System.out.println(model.toExternalForm());

        Iterator ret = x.read(model.toExternalForm(), rootPackage).iterator();

        assertTrue(ret.hasNext());
        
        while(ret.hasNext()) {
            System.out.println(ret.next());
        }
        
        

    }
    
    
}
