package org.dentaku.gentaku.metacartridge;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Collections;

import org.netbeans.jmiimpl.omg.uml.foundation.core.AttributeImpl;
import org.netbeans.jmiimpl.omg.uml.foundation.core.ClassifierImpl;
import org.omg.uml.foundation.core.Comment;
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
        if ((me == null) || (name == null)) {
            return "";
        }

        Iterator i = me.getTaggedValue().iterator();
        while (i.hasNext()) {
            TaggedValue value = (TaggedValue) i.next();
            if (name.equals(value.getName())) {
                return value.getDataValue().iterator().next().toString();
            }
        }

        return "";
    }

    public int countPrimaryKey(ClassifierImpl c) {
        return this.countPrimaryKey(c, 0);
    }

    private int countPrimaryKey(ClassifierImpl c, int count) {
        if (c == null) {
            return count;
        }

        Iterator i = c.getAttributes().iterator();
        while (i.hasNext()) {
            AttributeImpl att = (AttributeImpl) i.next();

            if (this.matchesStereotype(att, "PrimaryKey")) {
                count++;
            }
        }

        return this.countPrimaryKey((ClassifierImpl) c.getJavaGeneralization(), count);
    }

    public List getCommentLines(ModelElement me) {
        if ((me == null) || (me.getComment() == null)) {
            return Collections.EMPTY_LIST;
        }

        List ret = new ArrayList();
        Iterator it = me.getComment().iterator();
        while (it.hasNext()) {
            Comment c = (Comment) it.next();
            if (c.getName() != null) {
                String[] lines = c.getName().split("\n");
                for (int i = 0; i < lines.length; i++) {
                    ret.add(lines[i]);
                }
            }
        }

        return ret;
    }
}