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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.netbeans.jmiimpl.omg.uml.foundation.core.ClassifierImpl;
import org.netbeans.jmiimpl.omg.uml.foundation.core.TaggedValueImpl;

	/**
	 *
	 * This is a collection of attributes with getters and setters 
	 * for holding the data associated with viewing one entity
	 * Can also sort the atrributes into the order they are to be 
	 * displayed down the screen then left to right in a html table
	 * It also has a list of the child ClassView object related 
	 * to this one as a parent
	 * 
	 * @author <a href="mailto:david@dwynter.plus.com">David Wynter</a>
	 */
	public class ClassView {
		private String entityclassname;
		private String parentsEntityname;
		private String sterotype;
		// For display of values with hidden identifiers for ViewSelectorName
		// Supports composite attribute value display with ' ' separator
		private ArrayList selectorDisplay = new ArrayList();
		// Unordered list of selector Ids that uniquely identifies the entity
		// These are used as hidden ids from which a unique id is constructed, for <<ViewSelectorNames>>
		// Supports composite keys, id constructed by concat with '|' separator
		private ArrayList selectorNames = new ArrayList();
		private ArrayList attributes; 
		private ArrayList children;
		private HashSet ancestors;
		private boolean CRUD = false;
		private Integer columncount = new Integer(2);
		
		ClassView(SummitHelper helper, ClassifierImpl viewClass, String parentClassname) {
		 	String steroname = new String();
		 	String tagname = new String();
		 	Collection steros = (Collection) viewClass.getStereotypeNames();
		 	if(steros.contains(SummitHelper.ROOT_STEREOTYPE)) {
		 		this.setSterotype(SummitHelper.ROOT_STEREOTYPE);
		 		this.setParentsEntityname(parentClassname);		 		
		 	} else if(steros.contains(SummitHelper.CHILD_SELECTOR_STEREOTYPE)) {
		 		this.setSterotype(SummitHelper.CHILD_SELECTOR_STEREOTYPE);
		 		this.setParentsEntityname(parentClassname);		 				 		
		 	} else if (	steros.contains(SummitHelper.CHILD_TABLE_STEREOTYPE)) {
		 		this.setSterotype(SummitHelper.CHILD_TABLE_STEREOTYPE);
		 		this.setParentsEntityname(parentClassname);		 		
			} else {
				//error in model, need to log this and return
			}

		 	Collection tags = (Collection) viewClass.getTaggedValue();
		 	for (Iterator elemIterator = tags.iterator(); elemIterator.hasNext();) {
		 		TaggedValueImpl tagImpl = (TaggedValueImpl)elemIterator.next();
		 		tagname = tagImpl.getName();
		 		if(tagname.equals(SummitHelper.CRUD)) {
		 			this.setCRUD(true);
		 			helper.addCRUD(this);
		 		} else if (tagname.trim().equals(SummitHelper.COLUMNS)) {
		 			String colCount = (String) tagImpl.getDataValue().iterator().next();
					columncount=Integer.valueOf(colCount);
		 		}
		 	}
		}
		 
		/**
		 * Convert all dependent children of root from hierarchy into a flat List
		 * maintaining order of hierarchy
		 */
		public List getAllDependentClassView() {
			ArrayList depclassviews = new ArrayList();
			return getImmediateChildren(depclassviews, this);
		}
		
		/**
		 * Convert all dependent children of root from hierarchy into a flat List
		 * maintaining order of hierarchy. But where entityclassname is not repeated
		 */
		public List getAllDependentClassEntity() {
			ArrayList depclassviews = new ArrayList();
			depclassviews = getImmediateChildren(depclassviews, this);

			ArrayList haveItAlready = new ArrayList();
			for(int i = 0; i<depclassviews.size(); i++) {
				ClassView aClsView = (ClassView)depclassviews.get(i);
				String aEntityname = aClsView.getEntityclassname();
				if(haveItAlready.contains(aEntityname)) {
					depclassviews.remove(aClsView);
				} else {
					haveItAlready.add(aEntityname);
				}
			}

			return depclassviews;
		}
		
		private ArrayList getImmediateChildren(ArrayList aList, ClassView aclassView) {
			ArrayList kids = aclassView.getChildren();
			for(int i=0; i<kids.size(); i++) {
				aList.add((ClassView)kids.get(i));		
			}
			for(int i=0; i<kids.size(); i++) {
				aList = getImmediateChildren(aList, (ClassView)kids.get(i));		
			}			
			return aList;
		}
		
		/**
		 * Sorting attributes into display order
		 */
		private void SortPositions() {
			int i, l=0;
			for (i = attributes.size()-1; i > l ; i--) CompareExchange(i-1, i);
			for (i = l+2; i <= attributes.size()-1; i++){
				int j = i; 
				AttributeView v = (AttributeView)attributes.get(i);
				while ( Less(v, (AttributeView)attributes.get(j-1)) ) {
					attributes.set(j, (AttributeView)attributes.get(j-1) );
					j--;
				}
				attributes.set(j, v);
			}
		}

		private boolean Less(AttributeView v, AttributeView j) {
			if(v.getPosition()!= null & j.getPosition() != null ) {
				if(v.getPosition().intValue() < j.getPosition().intValue() ) {
					return true;
				}
			}
			return false;		
		}
		/**
		 * @param i
		 * @param j
		 */
		private void CompareExchange(int i, int j) {
			if (((AttributeView)attributes.get(j)).getPosition() != null && ((AttributeView)attributes.get(i)).getPosition() != null) {
				if (((AttributeView)attributes.get(j)).getPosition().intValue() < ((AttributeView)attributes.get(i)).getPosition().intValue())
					Exchange(i, j);
			}
		}

		/**
		 * @param i
		 * @param j
		 */
		private void Exchange(int i, int j) {
			AttributeView attrview = (AttributeView)attributes.get(i);
			attributes.set(i, (AttributeView)attributes.get(j));
			attributes.set(j, attrview);
		}
		
		// Getters and Setters

		public void setSterotype (String name) {
			sterotype = name;
		}
		
		public String getSterotype() {
			return sterotype;
		}
		
		public void setAttributes(ArrayList attrs) {
			attributes = attrs;
			SortPositions();
		}
		
		public ArrayList getAttributes() {
			return attributes;
		}
		
		/**
		 * @return Returns the children.
		 */
		public ArrayList getChildren() {
			return children;
		}
		/**
		 * @param children The children to set.
		 */
		public void setChildren(ArrayList children) {
			this.children = children;
		}
		/**
		 * @param aChild The child to add.
		 */
		public void addChildren(Object aChild) {
			this.children.add(aChild);
		}
		/**
		 * @return Returns the CRUD.
		 */
		public boolean isCRUD() {
			return CRUD;
		}
		/**
		 * @param crud The cRUD to set.
		 */
		public void setCRUD(boolean crud) {
			CRUD = crud;
		}
		/**
		 * @return Returns the number of columns.
		 */
		public Integer getColumncount() {
			return columncount;
		}
		/**
		 * @param columns The number of columns to set.
		 */
		public void setColumncount(Integer columns) {
			this.columncount = columns;
		}
		/**
		 * @return Returns the entityclassname.
		 */
		public String getEntityclassname() {
			return entityclassname;
		}
		/**
		 * @param entityclassname The entityclassname to set.
		 */
		public void setEntityclassname(String entityclassname) {
			this.entityclassname = entityclassname;
		}
		/**
		 * @return Returns the parents_entityname.
		 */
		public String getParentsEntityname() {
			return parentsEntityname;
		}
		/**
		 * @param parents_entityname The parents_entityname to set.
		 */
		public void setParentsEntityname(String parentsentityname) {
			this.parentsEntityname = parentsentityname;
		}
		/**
		 * @return Returns the selectorName.
		 */
		public ArrayList getSelectorNames() {
			return selectorNames;
		}
		/**
		 * @param selectorName The selectorName to set.
		 */
		public void setSelectorName(ArrayList selectorNames) {
			this.selectorNames = selectorNames;
		}
		/**
		 * @return Returns the ancestors.
		 */
		public HashSet getAncestors() {
			return ancestors;
		}
		/**
		 * @param ancestors The ancestors to set.
		 */
		public void setAncestors(HashSet ancestors) {
			this.ancestors = ancestors;
		}
		/**
		 * @param selectorNames The selectorNames to set.
		 */
		public void setSelectorNames(ArrayList selectorNames) {
			this.selectorNames = selectorNames;
		}
		/**
		 * @return Returns the selectorDisplay.
		 */
		public ArrayList getSelectorDisplay() {
			return selectorDisplay;
		}
		/**
		 * @param selectorDisplay The selectorDisplay to set.
		 */
		public void setSelectorDisplay(ArrayList selectorDisplay) {
			this.selectorDisplay = selectorDisplay;
		}
}
