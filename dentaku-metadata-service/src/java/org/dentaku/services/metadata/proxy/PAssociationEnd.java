package org.dentaku.services.metadata.proxy;

import org.omg.uml.foundation.core.AssociationEnd;
import org.omg.uml.foundation.core.Classifier;
import org.dentaku.services.metadata.UMLStaticHelper;
import org.dentaku.services.metadata.StringUtilsHelper;

/**
 * dynamic proxy for an AssociationEnd: dynamically supports the UMLAssociationEnd,
 * and org.omg.uml.foundation.core.AssociationEnd interfaces.
 *
 * @author <A HREF="http://www.amowers.com">Anthony Mowers</A>
 */
public class PAssociationEnd
        extends PModelElement
        implements UMLAssociationEnd {
    public static AssociationEnd newInstance(UMLStaticHelper scriptHelper,
                                             AssociationEnd associationEnd) {
        Class[] interfaces = {
            UMLAssociationEnd.class,
            AssociationEnd.class
        };
        return (AssociationEnd) java.lang.reflect.Proxy.newProxyInstance(associationEnd.getClass().getClassLoader(),
                interfaces,
                new PAssociationEnd(associationEnd, scriptHelper));
    }

    protected PAssociationEnd(AssociationEnd associationEnd,
                              UMLStaticHelper scriptHelper) {
        super(associationEnd, scriptHelper);
    }

    public String getRoleName() {
        String roleName = modelElement.getName();
        if ((roleName == null) || (roleName.length() == 0)) {
            AssociationEnd ae = (AssociationEnd) modelElement;
            roleName = StringUtilsHelper.lowerCaseFirstLetter(ae.getParticipant().getName());
        }
        return roleName;
    }

    public Classifier getType() {
        AssociationEnd ae = (AssociationEnd) modelElement;
        return PClassifier.newInstance(scriptHelper, ae.getParticipant());
    }

    public Object getId() {
        return modelElement.refMofId();
    }

    public String getNavigable() {
        AssociationEnd ae = (AssociationEnd) modelElement;
        return ae.isNavigable() ? "true" : "false";
    }
}
