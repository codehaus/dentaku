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

import java.util.ArrayList;

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
	private String divstartlabel;
	private String divendlabel;
	private String divstarttag;
	private String divendtag;
	private String tagstyle;
	private String labelstyle;
	private Integer position;
	private Integer size;
	private Boolean multiple;
	private String label;
	private ArrayList listvalues;
	private Integer colspan;
	
	public AttributeView() {
		colspan= new Integer(1);
	}
	
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
	public ArrayList getListvalues() {
		return listvalues;
	}
	/**
	 * @param listItems The listvalues to set.
	 */
	public void setListvalues(ArrayList listItems) {
		this.listvalues = listItems;
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
	/**
	 * @return Returns the divendlabel.
	 */
	public String getDivendlabel() {
		return divendlabel;
	}
	/**
	 * @param divendlabel The divendlabel to set.
	 */
	public void setDivendlabel(String divendlabel) {
		this.divendlabel = divendlabel;
	}
	/**
	 * @return Returns the divendtag.
	 */
	public String getDivendtag() {
		return divendtag;
	}
	/**
	 * @param divendtag The divendtag to set.
	 */
	public void setDivendtag(String divendtag) {
		this.divendtag = divendtag;
	}
	/**
	 * @return Returns the divstartag.
	 */
	public String getDivstartag() {
		return divstarttag;
	}
	/**
	 * @param divstartag The divstartag to set.
	 */
	public void setDivstarttag(String divstarttag) {
		this.divstarttag = divstarttag;
	}
	/**
	 * @return Returns the divstartlabel.
	 */
	public String getDivstartlabel() {
		return divstartlabel;
	}
	/**
	 * @param divstartlabel The divstartlabel to set.
	 */
	public void setDivstartlabel(String divstartlabel) {
		this.divstartlabel = divstartlabel;
	}
	/**
	 * @return Returns the labelstyle.
	 */
	public String getLabelstyle() {
		return labelstyle;
	}
	/**
	 * @param labelstyle The labelstyle to set.
	 */
	public void setLabelstyle(String labelstyle) {
		this.labelstyle = labelstyle;
	}
	/**
	 * @return Returns the tagstyle.
	 */
	public String getTagstyle() {
		return tagstyle;
	}
	/**
	 * @param tagstyle The tagstyle to set.
	 */
	public void setTagstyle(String tagstyle) {
		this.tagstyle = tagstyle;
	}
	/**
	 * @return Returns the size.
	 */
	public Integer getSize() {
		return size;
	}
	/**
	 * @param size The size to set.
	 */
	public void setSize(Integer size) {
		this.size = size;
	}
	/**
	 * @return Returns the divstarttag.
	 */
	public String getDivstarttag() {
		return divstarttag;
	}
	/**
	 * @return Returns the multiple.
	 */
	public Boolean getMultiple() {
		return multiple;
	}
	/**
	 * @param multiple The multiple to set.
	 */
	public void setMultiple(Boolean multiple) {
		this.multiple = multiple;
	}
}
