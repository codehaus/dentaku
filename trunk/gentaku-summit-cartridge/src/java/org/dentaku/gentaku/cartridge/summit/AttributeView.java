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
 * 
 */

package org.dentaku.gentaku.cartridge.summit;

/**
 * @author <a href="mailto:david@dwynter.plus.com">David Wynter</a>
 *
 * This is a collection of attributes with getters and setters 
 * for holding the data associated with viewing one attribute
 * 
 */
public class AttributeView {
	private String attributename;
	private String sterotype;
	private Integer position;
	private String label;
	private String[] listvalues;
	private Integer colspan;
	
	public void setAttributename(String name) {
		attributename = name;
	}
	
	public String getAttributename() {
		return attributename;
	}
	
	public void setSterotype(String name) {
		sterotype = name;
	}
	
	public String getSterotype() {
		return sterotype;
	}
	
	public void setPosition(Integer posn) {
		position = posn;
	}
	
	public Integer getPosition() {
		return position;
	}
	
	/**
	 * @return Returns the label.
	 */
	public String getLabel() {
		return label;
	}
	/**
	 * @param label The label to set.
	 */
	public void setLabel(String label) {
		this.label = label;
	}
	/**
	 * @return Returns the listvalues.
	 */
	public String[] getListvalues() {
		return listvalues;
	}
	/**
	 * @param listvalues The listvalues to set.
	 */
	public void setListvalues(String[] listvalues) {
		this.listvalues = listvalues;
	}
	/**
	 * @return Returns the colspan.
	 */
	public Integer getColspan() {
		return colspan;
	}
	/**
	 * @param colspan The colspan to set.
	 */
	public void setColspan(Integer colspan) {
		this.colspan = colspan;
	}
}
