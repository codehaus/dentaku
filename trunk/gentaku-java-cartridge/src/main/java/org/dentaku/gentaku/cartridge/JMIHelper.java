package org.dentaku.gentaku.cartridge;

import java.util.Iterator;

import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.Stereotype;
import org.omg.uml.foundation.core.TaggedValue;

public class JMIHelper {
	
    public boolean matchesStereotype(ModelElement object, String stereotypeName) {
        if ((object == null) || (stereotypeName == null)) {
            return false;
        }

        Iterator i = object.getStereotype().iterator();
        while (i.hasNext()) {
            Stereotype stereo = (Stereotype) i.next();
            if (stereotypeName.equals(stereo.getName())) {
                return true;
            }
        }

        return false;
    }
    
    public String getTaggedSingleValue(ModelElement me, String name) {
    	if((me == null) || (name == null)) {
    		return "";
    	}

    	Iterator i = me.getTaggedValue().iterator();
    	while(i.hasNext()) {
    		TaggedValue value = (TaggedValue) i.next();
    		if (name.equals(value.getName())) {
    			return value.getDataValue().iterator().next().toString();
    		}
    	}

    	return "";
    }
}