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
import java.util.List;

import org.omg.uml.foundation.core.ModelElement;

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
		private ArrayList selectorNames = new ArrayList();
		private ArrayList selectorTypes = new ArrayList();
		private ArrayList attributes; 
		private ArrayList children;
		private boolean CRUD = false;
		private Integer columncount = new Integer(2);
		
		ClassView(SummitHelper helper, ModelElement object, String parentClassname) {
		 	String steroname = new String();
		 	String tagname = new String();
		 	ArrayList steros = (ArrayList) object.getStereotype();
		 	for (int i=0; i<steros.size(); i++) {
				steroname = (String)steros.get(i);
		 		if(steroname.compareTo(SummitHelper.ROOT_STEREOTYPE) == 0 ||
				steroname.compareTo(SummitHelper.CHILD_SELECTOR_STEREOTYPE) == 0 ||
				steroname.compareTo(SummitHelper.CHILD_TABLE_STEREOTYPE) == 0) {
					this.setSterotype(steroname);
					this.setParentsEntityname(parentClassname);
				}
		 	}
		 	ArrayList tags = (ArrayList) object.getTaggedValue();
		 	for (int i=0; i<tags.size(); i++) {
		 		tagname = (String)tags.get(i);
		 		if(tagname.compareTo(SummitHelper.CRUD) == 0) {
		 			this.setCRUD(true);
		 			helper.addCRUD(this);
		 		}
		 	}
		 	// Need to find the association with the sterotype <<View>>
		 	// To be able to find the <<Entity>> this view class presents
		 	// at the far end of that association 	
		}
		 
		/**
		 * Convert all dependent children of root from hierarchy into a flat List
		 * maintain order of hierarchy
		 */
		public List getAllDependentClassView() {
			ArrayList depclassviews = new ArrayList();
			return getImmediateChildren(depclassviews, this);
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
		
		public void SortPositions() {
			for (int i = 1; i < attributes.size()-1; i++) {
				for (int j = 1; j > 1; j--) {
					CompareExchange(j-1, j);
				}
			}
		}

		/**
		 * @param i
		 * @param j
		 */
		private void CompareExchange(int i, int j) {
			if (((AttributeView)attributes.get(j)).getPosition().intValue() < ((AttributeView)attributes.get(i)).getPosition().intValue())
				Exchange(i, j);
		}

		/**
		 * @param i
		 * @param j
		 */
		private void Exchange(int i, int j) {
			AttributeView attrview = (AttributeView)attributes.get(j);
			attributes.set(i, (AttributeView)attributes.get(j));
			attributes.set(j, attrview);
		}
		
		// Getters and Setters

		public void setClassname (String name) {
			entityclassname = name;
		}
		
		public String getClassname () {
			return entityclassname;
		}
		
		public void setSterotype (String name) {
			sterotype = name;
		}
		
		public String getSterotype() {
			return sterotype;
		}
		
		public void setAttributes(ArrayList attrs) {
			for(int i=0; i < attrs.size(); i++) {
				attributes.add(i, attrs.get(i));
			}
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
		 * @return Returns the selectorType.
		 */
		public ArrayList getSelectorTypes() {
			return selectorTypes;
		}
		/**
		 * @param selectorType The selectorType to set.
		 */
		public void setSelectorType(ArrayList selectorTypes) {
			this.selectorTypes = selectorTypes;
		}
}
