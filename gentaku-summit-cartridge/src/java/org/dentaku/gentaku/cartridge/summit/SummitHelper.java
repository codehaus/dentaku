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
import java.util.HashSet;
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
	final static String PRIMARY_KEY = "field.primary-key";
	final static String APP_NAME = "Application";
	// Validator & Processing sterotypes
	final static String GROOVY = "Groovy";
	final static String FORMICA = "FormicaValidator";
	// Association sterotypes
	final static String VIEW = "View";
	final static String VIEW_REF = "ViewRef";
	// Attribute sterotypes for view objects
	final static String VIEWSELECTOR = "ViewSelector";
	final static String VIEWSELECTORNAME = "ViewSelectorName";
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
	final static String IDPOSITION = "idposition";
	final static String DISPPOSITION = "displayposition";
	final static String SIZE = "size";
	final static String LIST_ITEMS = "listItems";
	final static String COLUMNS = "TRcolumns";
	final static String SCRN_NAME = "ScreenName";
	final static String LABEL = "label";
	final static String SPAN = "colspan";
	final static String CRUD = "hasCrud";
	final static String DIVENDTAG = "divendtag";
	final static String DIVSTARTTAG = "divstarttag";
	final static String DIVSTARTLABEL = "divstartlabel";
	final static String DIVENDLABEL = "divendlabel";
	final static String TAGSTYLE = "tagstyle";
	final static String LABELSTYLE = "labelstyle";
	final static String MULT = "multiple";
	
	private UmlPackage umlPackage;

	private String screenname;
	private HashMap valuelists = new HashMap();
	private HashSet viewEntityDone = new HashSet();
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
	
	public ArrayList getValueListnames(ClassView viewClass) {
		ArrayList names = new ArrayList();
		if(viewClass == null)
			return names;
		ArrayList attributes = viewClass.getAttributes();
		names = addName(attributes, names);
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
	public ClassView buildClassView(ClassifierImpl viewClass, ClassView parent) {
		
		String parentClassname = null;
		ArrayList anc;

		if(parent != null) {
			parentClassname = parent.getEntityclassname();
		}
		ClassView aClassView = new ClassView(this, viewClass, parentClassname);
		ArrayList classViewChildren = new ArrayList();
		ArrayList viewattributes = new ArrayList();
		int entity_count=0;
		String entityname = null;
		
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
            			entityname = entity.getName();
            			aClassView.setEntityclassname(entity.getName());
            			Collection entityattributes = entity.getAttributes();
            			for(Iterator ie = entityattributes.iterator(); ie.hasNext();) {
            				Object attr = ie.next();
            				if(attr instanceof Attribute) {
	            				Collection attributetagvalues = ((AttributeImpl)attr).getTaggedValuesForName(PRIMARY_KEY, false);
	            				if(attributetagvalues.size()>0) {
	            					aClassView.getSelectorNames().add(((AttributeImpl)attr).getName());
	            				}
            				}
            			}
            		}
            	}
        		viewEntityDone.add(viewClass.getName());
            	// Find class with assocation of type VIEW_REF, a related child display, walk down tree only
            	ClassifierImpl child = getChildClassifier((AssociationEndImpl)end, VIEW_REF);
            	if(child!=null && entityname != null && !viewEntityDone.contains(child.getName())) {
            		// the entityname sets the parent name
            		if(parent == null) {
            			anc = new ArrayList();
            			aClassView.setAncestors(anc);
            		} else {
            			anc = parent.getAncestors();
            			anc.add(parent);
            			aClassView.setAncestors(anc);
            		}
        			parent = aClassView;
            		ClassView childClassView = buildClassView(child, parent);
            		classViewChildren.add(childClassView);
            	}
            }
        }
		if(parentClassname == null) {
			// RootSelectable has the rootEntity attached thru View Assoc
			rootViewClass = aClassView;
		}

        aClassView.setChildren(classViewChildren);
        // Now extract all the attributes we will display and get their relative position
        Collection attributeImpl = viewClass.getAttributes();
		ArrayList attributeImplList = new ArrayList(attributeImpl);
        for (Iterator ie = attributeImplList.iterator(); ie.hasNext();) {
            Object attr = ie.next();
            if (attr instanceof Attribute) {
            	AttributeView aViewAttr = buildAttributeView((AttributeImpl)attr, aClassView);
            	if(aViewAttr != null)
            		viewattributes.add(aViewAttr);
            }
        }
        // need to remove all ViewSelectorName type attributes bar the 1 with the position
        int numwithposn = 0;
        for(int i=0; i< viewattributes.size(); i++) {
        	AttributeView attr = (AttributeView)viewattributes.get(i);
        	if(attr.getSterotype() != null && attr.getSterotype().equals(VIEWSELECTORNAME) && attr.getPosition()!=null) {
        		numwithposn++;
        	} else if (attr.getSterotype() != null && attr.getSterotype().equals(VIEWSELECTORNAME)) {
        		viewattributes.remove(attr);
        	}
        	if(numwithposn > 1) {
        		// model error need to report it
        	}
        }
        aClassView.setAttributes(viewattributes);
        return aClassView;
	}
	
	/**
	 * Individual attribute to be displayed on screen
	 * 
	 * @param attr
	 * @return
	 */
	private AttributeView buildAttributeView(AttributeImpl attr, ClassView currentview) {
		// 
		AttributeView attrView = new AttributeView();
		attrView.setAttributename(attr.getName());
		// set the sterotype for the widget type
		Collection steroCollection = attr.getStereotypeNames();
		if(steroCollection != null) {
			for(Iterator ie = steroCollection.iterator(); ie.hasNext();) {
	            Object stereoObject = ie.next();
				if(stereoObject instanceof String) {
					attrView.setSterotype(((String)stereoObject).trim());
					if(((String)stereoObject).equals(VIEWSELECTORNAME)) {
						setCompositeSelector(attr, currentview);
					}
				}
			}
		}
		// set the position in the table, left to right top to bottom
		TaggedValue aPosTagValue = attr.getTaggedValue(POSITION);
		Collection posnObjectCollection = null;
		if(aPosTagValue != null) {
			posnObjectCollection = aPosTagValue.getDataValue();
			if(posnObjectCollection != null) {
				Integer posn = null;
				for(Iterator ie = posnObjectCollection.iterator(); ie.hasNext();) {
		            Object posnObject = ie.next();
					if(posnObject instanceof String) {
						try {
							posn = Integer.valueOf(((String)posnObject).trim());
							attrView.setPosition(posn);
						} catch (NumberFormatException nfe) {
							// error in model need to report
						}
					}
				}
			}
		}
		// set the list size, if there is one
		TaggedValue aSizeValue = attr.getTaggedValue(SIZE);
		Collection sizeObjectCollection = null;
		if(aSizeValue != null) {
			sizeObjectCollection = aSizeValue.getDataValue();
			if(sizeObjectCollection != null) {
				Integer size = null;
				for(Iterator ie = sizeObjectCollection.iterator(); ie.hasNext();) {
		            Object sizeObject = ie.next();
					if(sizeObject instanceof String) {
						try {
							size = Integer.valueOf(((String)sizeObject).trim());
							attrView.setSize(size);
						} catch (NumberFormatException nfe) {
							// error in model need to report
						}
					}
				}
			}
		}
		// set the span for TD in the table
		TaggedValue aSpanTagValue = attr.getTaggedValue(SPAN);
		if(aSpanTagValue != null) {
			Collection spanObjectCollection = aSpanTagValue.getDataValue();
			if(spanObjectCollection != null) {
				Integer span = null;
				for(Iterator ie = spanObjectCollection.iterator(); ie.hasNext();) {
		            Object spanObject = ie.next();
					if(spanObject instanceof String) {
						try {
							span = Integer.valueOf(((String)spanObject).trim());
							attrView.setColspan(span);
						} catch (NumberFormatException nfe) {
							// error in model need to report
						}
					}
				}
			}
		} else {
			attrView.setColspan(new Integer(1));
		}
		TaggedValue aLabelTagValue = attr.getTaggedValue(LABEL);
		if(aLabelTagValue != null) {
			Collection labelObjectCollection = aLabelTagValue.getDataValue();
			if(labelObjectCollection != null) {
				for(Iterator ie = labelObjectCollection.iterator(); ie.hasNext();) {
		            Object labelObject = ie.next();
					if(labelObject instanceof String) {
						if(((String)labelObject).length()>0)
						attrView.setLabel(((String)labelObject).trim());
					}
				}
			}
		} else {
			attrView.setLabel(attrView.getAttributename());			
		}
		TaggedValue aListItemsTagValue = attr.getTaggedValue(LIST_ITEMS);
		if(aListItemsTagValue != null) {
			Collection listObjectCollection = aListItemsTagValue.getDataValue();
			if(listObjectCollection != null) {
				String listitem = null;
				ArrayList listitems = new ArrayList();
				for(Iterator ie = listObjectCollection.iterator(); ie.hasNext();) {
		            Object listObject = ie.next();
					if(listObject instanceof String) {
						listitem = ((String)listObject).trim();
						if(listitem.length()>0)
							listitems.add(listitem);
					}
				}
				attrView.setListvalues(listitems);
			}
		}
		// Be nice to check for matching CSS Class strings for each divstart* and divdend*
		TaggedValue aDivendtagTagValue = attr.getTaggedValue(DIVENDTAG);
		if(aDivendtagTagValue != null) {
			Collection divObjectCollection = aDivendtagTagValue.getDataValue();
			if(divObjectCollection != null) {
				String divitem = null;
				for(Iterator ie = divObjectCollection.iterator(); ie.hasNext();) {
		            Object divObject = ie.next();
					if(divObject instanceof String) {
						try {
							divitem = ((String)divObject).trim();
							if(divitem.length()>0)
								attrView.setDivendtag(divitem);
						} catch (Exception e) {
							// error in model need to report
						}
					}
				}
			}
		}
		TaggedValue aDivstarttagTagValue = attr.getTaggedValue(DIVSTARTTAG);
		if(aDivstarttagTagValue != null) {
			Collection divObjectCollection = aDivstarttagTagValue.getDataValue();
			if(divObjectCollection != null) {
				String divitem = null;
				for(Iterator ie = divObjectCollection.iterator(); ie.hasNext();) {
		            Object divObject = ie.next();
					if(divObject instanceof String) {
						try {
							divitem = ((String)divObject).trim();
							if(divitem.length()>0)
								attrView.setDivstarttag(divitem);
						} catch (Exception e) {
							// error in model need to report
						}
					}
				}
			}
		}
		TaggedValue aDivstartlabelTagValue = attr.getTaggedValue(DIVSTARTLABEL);
		if(aDivstartlabelTagValue != null) {
			Collection divObjectCollection = aDivstartlabelTagValue.getDataValue();
			if(divObjectCollection != null) {
				String divitem = null;
				for(Iterator ie = divObjectCollection.iterator(); ie.hasNext();) {
		            Object divObject = ie.next();
					if(divObject instanceof String) {
						try {
							divitem = ((String)divObject).trim();
							if(divitem.length()>0)
								attrView.setDivstartlabel(divitem);
						} catch (Exception e) {
							// error in model need to report
						}
					}
				}
			}
		}
		TaggedValue aDivendlabelTagValue = attr.getTaggedValue(DIVENDLABEL);
		if(aDivendtagTagValue != null) {
			Collection divObjectCollection = aDivendtagTagValue.getDataValue();
			if(divObjectCollection != null) {
				String divitem = null;
				for(Iterator ie = divObjectCollection.iterator(); ie.hasNext();) {
		            Object divObject = ie.next();
					if(divObject instanceof String) {
						try {
							divitem = ((String)divObject).trim();
							if(divitem.length()>0)
								attrView.setDivendtag(divitem);
						} catch (Exception e) {
							// error in model need to report
						}
					}
				}
			}
		}
		TaggedValue aLabelstyleTagValue = attr.getTaggedValue(LABELSTYLE);
		if(aLabelstyleTagValue != null) {
			Collection styleObjectCollection = aLabelstyleTagValue.getDataValue();
			if(styleObjectCollection != null) {
				String styleitem = null;
				for(Iterator ie = styleObjectCollection.iterator(); ie.hasNext();) {
		            Object styleObject = ie.next();
					if(styleObject instanceof String) {
						try {
							styleitem = ((String)styleObject).trim();
							if(styleitem.length()>0)
								attrView.setLabelstyle(styleitem);
						} catch (Exception e) {
							// error in model need to report
						}
					}
				}
			}
		}
		TaggedValue aTagstyleTagValue = attr.getTaggedValue(TAGSTYLE);
		if(aTagstyleTagValue != null) {
			Collection styleObjectCollection = aTagstyleTagValue.getDataValue();
			if(styleObjectCollection != null) {
				String styleitem = null;
				for(Iterator ie = styleObjectCollection.iterator(); ie.hasNext();) {
		            Object styleObject = ie.next();
					if(styleObject instanceof String) {
						try {
							styleitem = ((String)styleObject).trim();
							if(styleitem.length()>0)
								attrView.setTagstyle(styleitem);
						} catch (Exception e) {
							// error in model need to report
						}
					}
				}
			}
		}
		TaggedValue aMultValue = attr.getTaggedValue(MULT);
		if(aMultValue != null) {
			Collection multObjectCollection = aMultValue.getDataValue();
			if(multObjectCollection != null) {
				Boolean multitem = null;
				for(Iterator ie = multObjectCollection.iterator(); ie.hasNext();) {
		            Object multObject = ie.next();
					if(multObject instanceof Boolean) {
						try {
							multitem = ((Boolean)multObject);
							attrView.setMultiple(multitem);
						} catch (Exception e) {
							// error in model need to report
						}
					}
				}
			}
		}
		return attrView;
	}
	
	/**
	 * @param attr
	 */
	private void setCompositeSelector(AttributeImpl attr, ClassView currentview) {
		TaggedValue aDispPosnTagValue = attr.getTaggedValue(DISPPOSITION);
		if(aDispPosnTagValue != null) {
			Collection dispposnObjectCollection = aDispPosnTagValue.getDataValue();
			if(dispposnObjectCollection != null) {
				Integer dispposn = null;
				for(Iterator ie = dispposnObjectCollection.iterator(); ie.hasNext();) {
		            Object dispposnObject = ie.next();
					if(dispposnObject instanceof String) {
						try {
							dispposn = Integer.valueOf(((String)dispposnObject).trim());
							currentview.getSelectorDisplay().add(dispposn.intValue(), (String)attr.getName());
						} catch (NumberFormatException e) {
							// error in model need to report
						}
					}
				}
			}
		}
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
                	AssociationEndImpl otherEnd = (AssociationEndImpl) ((AssociationEndImpl)end).getTarget();
                	// 
                	if(otherEnd.isClass()) {
                		ClassifierImpl aClassifier = (ClassifierImpl)otherEnd.getParticipant();
                	return aClassifier;
                	}
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