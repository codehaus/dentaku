package org.dentaku.gentaku.cartridge.entity;

import org.dentaku.gentaku.cartridge.GeneratorSupport;
import org.dentaku.gentaku.cartridge.GenerationException;
import org.dentaku.services.metadata.Utils;
import org.netbeans.jmiimpl.omg.uml.modelmanagement.ModelImpl;
import org.netbeans.jmiimpl.omg.uml.foundation.core.ClassifierImpl;
import org.netbeans.jmiimpl.omg.uml.foundation.core.ModelElementImpl;
import org.netbeans.jmiimpl.omg.uml.foundation.core.AssociationEndImpl;
import org.omg.uml.foundation.core.*;
import org.omg.uml.foundation.datatypes.MultiplicityRange;
import org.omg.uml.modelmanagement.UmlPackage;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import java.util.Collection;
import java.util.Iterator;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: topping
 * Date: Oct 25, 2005
 * Time: 12:42:10 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class EntityGenerator extends GeneratorSupport {
    // add the items that we implicitly generate for Entity objects
    public void preProcessModel(ModelImpl model) throws GenerationException {
        final org.omg.uml.UmlPackage umlPackage = (org.omg.uml.UmlPackage)model.refOutermostPackage();
        CorePackage core = umlPackage.getCore();
        Stereotype classifierStereotype = getClassifierStereotype(core);

        if (classifierStereotype == null) {
            throw new RuntimeException("No stereotype of Entity found.");
        }

        UmlPackage entityPackage = getEntityPackage(umlPackage);

        ClassifierImpl odspEntity = Utils.findUmlClass(umlPackage, "org.dentaku.services.persistence", "Entity", true);
        ClassifierImpl javaUtilList = Utils.findUmlClass(umlPackage, "java.util", "List", true);

        Collection extendedElements = setupClasses(core, classifierStereotype, entityPackage, odspEntity, umlPackage);

        setupRelations(extendedElements, umlPackage, core, entityPackage, javaUtilList);
    }

    protected abstract Stereotype getClassifierStereotype(CorePackage core);

    protected abstract UmlPackage getEntityPackage(org.omg.uml.UmlPackage umlPackage);

    private void setupRelations(Collection extendedElements, org.omg.uml.UmlPackage umlPackage, CorePackage core, UmlPackage jdoPackage, ClassifierImpl javaUtilList) {
        // pass 2: do the relation fields
        for (Iterator elemIterator = extendedElements.iterator(); elemIterator.hasNext();) {
            ModelElementImpl modelElement = (ModelElementImpl) elemIterator.next();
            ClassifierImpl classifier = (ClassifierImpl) modelElement;
            String fullyQualifiedName = classifier.getFullyQualifiedName();
            String subclassName = fullyQualifiedName.substring(0, fullyQualifiedName.lastIndexOf("Base"));
            ClassifierImpl subclass = findUmlClass(umlPackage, subclassName, false);
            // handle the associations
            for (Iterator assIter = subclass.getTargetEnds().iterator(); assIter.hasNext();) {
                AssociationEndImpl end = (AssociationEndImpl) assIter.next();
                if (end.isNavigable() && !excludedAssocs(end)) {
                    ClassifierImpl endClass = (ClassifierImpl) end.getParticipant();
                    Attribute newAttr = core.getAttribute().createAttribute();
                    if (end.getName() != null) {
                        newAttr.setName(end.getName());
                    } else {
                        String name = endClass.getName();
                        name = name.substring(0, 1).toLowerCase() + name.substring(1);
                        newAttr.setName(name);
                    }

                    createFieldName(core, newAttr, jdoPackage);
                    if (end.getMultiplicity() != null) {
                        if (isCollection(end)) {
                            setupCollectionEnd(core, newAttr, jdoPackage, endClass, end, javaUtilList);
                        } else {
                            core.getATypedFeatureType().add(newAttr, endClass);
                        }
                    } else {
                        // if no multiplicity, assume
                        System.out.println("warning - no multiplicty found: " + ((ClassifierImpl) end.getParticipant()).getFullyQualifiedName() + "<->" + ((ClassifierImpl) ((AssociationEndImpl) end).getOtherEnd().getParticipant()).getFullyQualifiedName());
                        core.getATypedFeatureType().add(newAttr, endClass);
                    }
                    core.getAOwnerFeature().add(classifier, newAttr);
                }
            }
        }
    }

    protected abstract void setupCollectionEnd(CorePackage core, Attribute newAttr, UmlPackage entityPackage, ClassifierImpl endClass, AssociationEndImpl end, ClassifierImpl javaUtilList);

    protected abstract void createFieldName(CorePackage core, Attribute newAttr, UmlPackage jdoPackage);

    protected abstract Collection setupClasses(CorePackage core, Stereotype classifierStereotype, UmlPackage entityPackage, ClassifierImpl odspEntity, org.omg.uml.UmlPackage umlPackage) throws GenerationException;

    protected boolean isCollection(AssociationEndImpl end) {
        MultiplicityRange multiplicityRange = (MultiplicityRange) end.getMultiplicity().getRange().iterator().next();
        int lower = multiplicityRange.getLower();
        int upper = multiplicityRange.getUpper();
        boolean result = false;
        if (upper - lower > 1 || upper == -1) {
            result = true;
        }
        return result;
    }

    /**
     * @param end
     * @return
     */
    private boolean excludedAssocs(AssociationEndImpl end) {
        UmlAssociation anAssoc = ((AssociationEnd) end).getAssociation();
        Collection stereotypesForAssoc = anAssoc.getStereotype();
        ArrayList sterotypesForAssocList = new ArrayList(stereotypesForAssoc);
        for (Iterator ie = sterotypesForAssocList.iterator(); ie.hasNext();) {
            Stereotype stereo = (Stereotype)ie.next();
            if(stereo.getName().equals("View") ||
                    stereo.getName().equals("Validates") ||
                    stereo.getName().equals("Imports")) {
                return true;
            }
        }
        return false;
    }

    private ClassifierImpl findUmlClass(org.omg.uml.UmlPackage umlPackage, String fqn, boolean create) {
        int indexOf = fqn.lastIndexOf(".");
        if (indexOf == -1) {
            return Utils.findUmlClass(umlPackage, "", fqn, true);
        } else {
            String entityName = fqn.substring(indexOf + 1);
            String pkgName = fqn.substring(0, indexOf);
            return Utils.findUmlClass(umlPackage, pkgName, entityName, create);
        }
    }

    protected void createGeneralization(CorePackage core, ClassifierImpl parent, ClassifierImpl child) {
        Generalization g = core.getGeneralization().createGeneralization();
        g.setChild(child);
        g.setParent(parent);
    }

    protected TagDefinition findTagdef(Collection definedTag, final String key) {
        TagDefinition tag = (TagDefinition) CollectionUtils.find(definedTag, new Predicate() {
            public boolean evaluate(Object object) {
                return          object instanceof TagDefinition
                                && ((TagDefinition) object).getName() != null
                                && ((TagDefinition) object).getName().equals(key);
            }
        });
        return tag;
    }
}
