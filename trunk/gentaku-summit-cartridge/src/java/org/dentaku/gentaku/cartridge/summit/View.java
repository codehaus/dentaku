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

/**
 * This represents the structure of the entire view on a screen
 * This starts with the class with the RootSelectable sterotype
 * at the top of the tree. This is linked to by one or more
 * relationships with sterotype of ViewRef. The class at the other end
 * is either a ChildSelectable or a ChildTable. There can be a tree
 * of related classes for display.
 * 
 * Within each class are the attributes to be displayed, each marked
 * with a sterotype signifying the type of display object. Each also 
 * has a tagged value to signify the position going left to right top 
 * to bottom down the page. It wraps at the colspan set for the classview
 * Each attribute can override it's colspan, the template allows for this
 * padding <td> as needed.
 * 
 * There are 2 extra tagged value types. 'values' is used for a static
 * list of values in a dropdown. 'hasCRUD' is used with a ChildTable 
 * to indicated a single object has a selector and individual display
 * widgets in addition to those in the table.
 * 
 * @author <a href="mailto:david@dwynter.plus.com">David Wynter</a>
 * 
 */
public class View {
	private ClassView root;
	private ArrayList relatedclasses;
	private String rootclassname;
	
	public ClassView getRoot() {
		return root;
	}
	
	public void setRoot(ClassView clsview) {
		root = clsview;
	}

	public ArrayList getRelatedclasses() {
		return relatedclasses;
	}
	
	public void setRelatedclasses(ArrayList attrs) {
		for(int i=0; i < attrs.size(); i++) {
			relatedclasses.add(i, attrs.get(i));
		}
	}
	
	public String getRootclassname() {
		return rootclassname;
	}
	
	public void setRootclassname(String entityname) {
		rootclassname = entityname;
	}

}
