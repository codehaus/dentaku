/*
 * Created on Dec 2, 2004
 *
 * Copyright STPenable Ltd. (c) 2004
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
 */
package org.dentaku.gentaku.cartridge.summit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.netbeans.jmiimpl.omg.uml.foundation.core.AssociationEndImpl;
import org.netbeans.jmiimpl.omg.uml.foundation.core.AttributeImpl;
import org.netbeans.jmiimpl.omg.uml.foundation.core.ClassifierImpl;
import org.omg.uml.UmlPackage;
import org.omg.uml.foundation.core.AssociationEnd;
import org.omg.uml.foundation.core.Attribute;
import org.omg.uml.foundation.core.Classifier;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.Stereotype;
import org.omg.uml.foundation.core.TaggedValue;
import org.omg.uml.foundation.core.UmlAssociation;

/**
 * This script helper provides the ability to traverse Associations in
 * the model starting from the <<RootSterotype>> thru a tree of sterotypes. 
 * It traverses 0 or 1 to many relationships of sterotype 
 * type <<ViewRef>> and from the found classes then traverses 1 to 1 
 * relationships of sterotype <<View>> to get the entity classes.
 * 
 * These are stored as an object model and made available to templates 
 * creating actions and views
 *
 * @author <a href="mailto:david@dwynter.plus.com">David Wynter</a>
 */
public class SummitHelper
{

	private Collection metadata;

	// Class sterotypes
	final static String ROOT_STEREOTYPE = "RootSelectable";
	final static String CHILD_SELECTOR_STEREOTYPE = "ChildSelectable";
	final static String CHILD_TABLE_STEREOTYPE = "ChildTable";
	final static String ENTITY_BEAN = "Entity";
	final static String PRIMARY_KEY = "PrimaryKey";
	final static String APP_NAME = "Application";
	// Association sterotypes
	final static String VIEW = "View";
	final static String VIEW_REF = "ViewRef";
	// Attribute sterotypes for view objects
	final static String VIEWSELECTOR = "ViewSelector";
	final static String HIDDENSELECTOR = "HiddenSelector";
	final static String SINGLELINE = "Singleline";
	final static String MULTILINE = "Multiline";
	final static String EMAILADDR = "Emailaddr";
	final static String MONEY = "Money";
	final static String CHECKBOX = "Checkbox";
	final static String DROPDOWN = "Dropdown";
	final static String RADIOBUTTON = "Radiobutton";
	// Tagged values for Attributes
	// Tag names
	final static String POSITION = "position";
	final static String LIST_ITEMS = "listItems";
	final static String COLUMNS = "TRcolumns";
	final static String SCRN_NAME = "ScreenName";
	final static String LABEL = "label";
	final static String SPAN = "span";
	final static String CRUD = "hasCrud";
	
	private UmlPackage umlPackage;

	private String screenname;
	private HashMap valuelists;
	private String viewpackage;
	private ClassView rootViewClass;
	private ArrayList crudList = new ArrayList();
	
	/**
	 * @author David Wynter
	 *
	 * Set metadata
	 *
	 * @param templateEngine
	 * @param metadataProvider
	 * @param writerMapper
	 */
	public SummitHelper() {
	}
	
	public ArrayList getListnames(ClassView rootClass) {
		ArrayList names = new ArrayList();
		ArrayList attributes = rootClass.getAttributes();
		names = addName(attributes, names);
		rootClass.getChildren();
		return names;
	}
		
	private ArrayList extractAttrs(ArrayList classes, ArrayList names) {
		for(int i=0; i<classes.size(); i++) {
			ClassView aClassView = (ClassView)classes.get(i);
			names = addName(aClassView.getAttributes(), names);
			names = extractAttrs(aClassView.getChildren(), names);
		}
		return names;
	}
	
	private ArrayList addName(ArrayList attributes, ArrayList names) {
		for(int i=0; i<attributes.size(); i++) {
			AttributeView aAttrView = (AttributeView) attributes.get(i);
			if(aAttrView.getListvalues()!=null) {
				names.add(aAttrView.getAttributename());
				valuelists.put(aAttrView.getAttributename(), aAttrView.getListvalues());
			}
		}
		return names;
	}
	
	public ArrayList getList(String key) {
		return (ArrayList)valuelists.get(key);
	}

    /**
     * Entry point to build helper model of the views within a screen
     * 
	 * @param viewClass
	 * @param sterotypeName
	 */
	public ClassView buildClassView(ClassifierImpl viewClass, String parentClassname) {
		
		ClassView aClassView = new ClassView(this, viewClass, parentClassname);
		ArrayList classViewChildren = new ArrayList();
		ArrayList viewattributes = new ArrayList();
		int entity_count=0;
		
		Collection steroNames = viewClass.getStereotypeNames();
		String steroname = getKnownViewSterotypeName(steroNames);
		if(steroname!=null) {
			aClassView.setSterotype(steroname);
		} else {
			// error
		}
		
		Collection viewAssociations = viewClass.getAssociationLinks();
		ArrayList viewAssociationsList = new ArrayList(viewAssociations);
        for (Iterator it = viewAssociationsList.iterator(); it.hasNext();) {
            Object end = it.next();
            if (end instanceof AssociationEnd) {
            	// Find class of the entity we are building the action for
            	ClassifierImpl entity = getChildClassifier((AssociationEndImpl)end, VIEW);
            	if(entity != null) {
            		// only one allowed, so lets count them
            		entity_count++;
            		if(entity_count>1){
            			// error, need to add logging so model incorrectness can be reported
            		} else {
            			aClassView.setEntityclassname(entity.getFullyQualifiedName());
            			Collection entityattributes = entity.getAttributes();
            			AttributeImpl[] entityattributeclassifiers = (AttributeImpl[])entityattributes.toArray();
            			for(int i=0; i<entityattributes.size(); i++) {
            				Collection attributestereotypes = entityattributeclassifiers[i].getStereotypeNames();
            				if(isPKname(attributestereotypes)) {
            					aClassView.getSelectorNames().add(entityattributeclassifiers[i].getName());
            					aClassView.getSelectorTypes().add(entityattributeclassifiers[i].getType().getName());
            				}
            			}
            		}
            	}
            	// Find class with assocation of type VIEW_REF, a related child display
            	ClassifierImpl child = getChildClassifier((AssociationEndImpl)end, VIEW_REF);
            	if(child!=null) {
            		// the entity.getName() sets the parent name
            		ClassView childClassView = buildClassView(child, entity.getName());
            		classViewChildren.add(childClassView);
            	}
            }
        }
		if(parentClassname == null) {
			// RootSelectable has the rooEntity attached thru View Assoc
			rootViewClass = aClassView;
		}

        aClassView.addChildren(classViewChildren);
        // Now extract all the attributes we will display and get their relative position
        Collection attributeImpl = viewClass.getAttributes();
		ArrayList attributeImplList = new ArrayList(attributeImpl);
        for (Iterator ie = attributeImplList.iterator(); ie.hasNext();) {
            Object attr = ie.next();
            if (attr instanceof Attribute) {
            	AttributeView aViewAttr = buildAttributeView((AttributeImpl)attr);
            	if(aViewAttr != null)
            		viewattributes.add(aViewAttr);
            }
        }
        aClassView.setAttributes(viewattributes);
        return aClassView;
	}
	
	/**
	 * @param attributestereotypes
	 * @return
	 */
	private boolean isPKname(Collection attributestereotypes) {
        for (Iterator it = attributestereotypes.iterator(); it.hasNext();) {
            String name = (String) it.next();
            // rather than throw an error if > 1 return the first of them
            if (name.equals(PRIMARY_KEY)) {
                return true;
            }
        }
        return false;
	}

	/**
	 * Individual attribute to be displayed on screen
	 * 
	 * @param attr
	 * @return
	 */
	private AttributeView buildAttributeView(AttributeImpl attr) {
		// 
		AttributeView attrView = new AttributeView();
		attrView.setAttributename(attr.getName());
		// set the sterotype for the widget type
		Collection steroCollection = attr.getStereotypeNames();
		if(steroCollection != null) {
			String stero = null;
			for(Iterator ie = steroCollection.iterator(); ie.hasNext();) {
	            Object steroObject = ie.next();
				if(steroObject.getClass().isInstance(stero)) {
					attrView.setSterotype(stero);
				}
			}
		}
		// set the position in the table, left to right top to bottom
		Collection posnObjectCollection = attr.getTaggedValue(POSITION).getDataValue();
		if(posnObjectCollection != null) {
			Integer posn = null;
			for(Iterator ie = posnObjectCollection.iterator(); ie.hasNext();) {
	            Object posnObject = ie.next();
				if(posnObject.getClass().isInstance(posn)) {
					attrView.setPosition(posn);
				}
			}
		}
		// set the span for TD in the table
		Collection spanObjectCollection = attr.getTaggedValue(SPAN).getDataValue();
		if(posnObjectCollection != null) {
			Integer posn = null;
			for(Iterator ie = posnObjectCollection.iterator(); ie.hasNext();) {
	            Object posnObject = ie.next();
				if(posnObject.getClass().isInstance(posn)) {
					attrView.setPosition(posn);
				}
			}
		}
		Collection labelObjectCollection = attr.getTaggedValue(LABEL).getDataValue();
		if(labelObjectCollection != null) {
			String label = null;
			for(Iterator ie = labelObjectCollection.iterator(); ie.hasNext();) {
	            Object labelObject = ie.next();
				if(labelObject.getClass().isInstance(label)) {
					attrView.setLabel(label);
				}
			}
			if(attrView.getLabel()==null)
				attrView.setLabel(attrView.getAttributename());
		}
		Collection listObjectCollection = attr.getTaggedValue(LIST_ITEMS).getDataValue();
		if(listObjectCollection != null) {
			String list = null;
			for(Iterator ie = posnObjectCollection.iterator(); ie.hasNext();) {
	            Object listObject = ie.next();
				if(listObject.getClass().isInstance(list)) {
					list = (String)listObject;
					String listItem;
					int pos=0, i=0;
					ArrayList listItems = new ArrayList();
					while(true) {
						i = list.indexOf(',', pos);
						if(i>-1) {
						listItem=list.substring(0, i).trim();
						listItems.add(listItem);
						} else {
							break;
						}
					}
					attrView.setListvalues((String[])listItems.toArray());
				}
			}
		}
		return null;
	}

	private String getKnownViewSterotypeName(Collection steroNames) {
        for (Iterator it = steroNames.iterator(); it.hasNext();) {
            String name = (String) it.next();
            // rather than throw an error if > 1 return the first of them
            if (name.equals(ROOT_STEREOTYPE) || 
            		name.equals(CHILD_SELECTOR_STEREOTYPE) ||
					name.equals(CHILD_TABLE_STEREOTYPE)) {
                return name;
            }
        }
        return null;
	}

	/**
	 * Find the 'parent most' of the entity views displayed
	 * only one per screen
	 * 
	 * @return String
	 */
	public ClassView getRootView() throws Exception {
        return rootViewClass;
    }
    
	/**
	 * Find the root or 'parent most' of the entity views displayed
	 * only one per screen
	 * 
	 * @param metadata
	 * @return ClassifierImpl
	 */
	public Classifier getRoot(Collection metadata) throws Exception {
        for (Iterator it = metadata.iterator(); it.hasNext();) {
            ModelElement elem = (ModelElement) it.next();
            if (elem instanceof Classifier && elem.getName().equals(ROOT_STEREOTYPE)) {
            	extractTaggedValues(elem);
                return (Classifier)elem;
            }
        }
        return null;
    }
    
	/**
	 * @param elem
	 * @return
	 */
	private void extractTaggedValues(ModelElement elem) {
		// Do the usual scrambling through a Collection of ModelElements
		ArrayList taggedvalsForRoot = new ArrayList(elem.getTaggedValue());
		for (Iterator ie = taggedvalsForRoot.iterator(); ie.hasNext();) {
            TaggedValue tag = (TaggedValue)ie.next();
            if(tag.getName().equals(SCRN_NAME)) {
            	// now we know it is the right type
            	Collection values = tag.getDataValue();
            	// Bit fragile, assuming one value only 
            	if(values.iterator().hasNext())
            		screenname=(String)values.iterator().next();
            }
		}
	}

	/**
	 * Find the child entity view via this Association
	 * may be called multiple times for different children of 
	 * parent
	 * 
	 * @param metadata
	 * @return ClassifierImpl
	 */
    private ClassifierImpl getChildClassifier(Object end, String type) {
        if (end instanceof AssociationEnd) {
            UmlAssociation viewRefAssoc = ((AssociationEnd) end).getAssociation();
            Collection sterotypesForAssoc = viewRefAssoc.getStereotype();
    		ArrayList sterotypesForAssocList = new ArrayList(sterotypesForAssoc);
    		for (Iterator ie = sterotypesForAssocList.iterator(); ie.hasNext();) {
                Stereotype stero = (Stereotype)ie.next();
                if(stero.getName().equals(type)) {
                	// now we know it is the right type get the other end
                	AssociationEnd otherEnd = ((AssociationEndImpl)end).getTarget();
                	// 
                	ClassifierImpl aClassifier = (ClassifierImpl)otherEnd.getParticipant();
                	return aClassifier;
                }
    		}
        }
        return null;
    }

	/**
	 * Convenience method for later use by CrudActionPlugin template
	 * 
	 * @param view
	 */
	public void addCRUD(ClassView view) {
		crudList.add(view);
	}
	/**
	 * @return Returns the crudList.
	 */
	public ArrayList getCrudList() {
		return crudList;
	}
}